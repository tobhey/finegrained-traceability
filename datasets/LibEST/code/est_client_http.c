#include "libest_fake_headers.h"
/*
** signed long to signed int
*/
int curlx_sltosi(long slnum)
{
#ifdef __INTEL_COMPILER
#  pragma warning(push)
#  pragma warning(disable:810) /* conversion may lose significant bits */
#endif

  assert(slnum >= 0);
#if (SIZEOF_INT < CURL_SIZEOF_LONG)
  assert((unsigned long) slnum <= (unsigned long) CURL_MASK_SINT);
#endif
  return (int)(slnum & (long) CURL_MASK_SINT);

#ifdef __INTEL_COMPILER
#  pragma warning(pop)
#endif
}
/*
 * parsedate()
 *
 * Returns:
 *
 * PARSEDATE_OK     - a fine conversion
 * PARSEDATE_FAIL   - failed to convert
 * PARSEDATE_LATER  - time overflow at the far end of time_t
 * PARSEDATE_SOONER - time underflow at the low end of time_t
 */
static int parsedate(const char *date, time_t *output);
/* returns:
   -1 no day
   0 monday - 6 sunday
*/
static int checkday(const char *check, size_t len)
{
  int i;
  const char * const *what;
  int found= 0;
  if(len > 3)
    what = &weekday[0];
  else
    what = &Curl_wkday[0];
  for(i=0; i<7; i++) {
    if(est_client_Curl_raw_equal(check, what[0])) {
      found=1;
      break;
    }
    what++;
  }
  return found?i:-1;
}
/* returns:
   -1 no day
   0 monday - 6 sunday
*/
static int checkmonth(const char *check)
{
  int i;
  const char * const *what;
  int found= 0;

  what = &Curl_month[0];
  for(i=0; i<12; i++) {
    if(est_client_Curl_raw_equal(check, what[0])) {
        found=1;
      break;
    }
    what++;
  }
  return found?i:-1; /* return the offset or -1, no real offset is -1 */
}
/* return the time zone offset between GMT and the input one, in number
   of seconds or -1 if the timezone wasn't found/legal */
static int checktz(const char *check)
{
  unsigned int i;
  const struct tzinfo *what;
  int found= 0;

  what = tz;
  for(i=0; i< sizeof(tz)/sizeof(tz[0]); i++) {
    if(est_client_Curl_raw_equal(check, what->name)) {
      found=1;
      break;
    }
    what++;
  }
  return found?what->offset*60:-1;
}
/* return the time zone offset between GMT and the input one, in number
   of seconds or -1 if the timezone wasn't found/legal */
static void skip_over_white(const char **date)
{
  /* skip everything that aren't letters or digits */
  while(**date && !ISALNUM(**date))
    (*date)++;
}
/* struct tm to time since epoch in GMT time zone.
 * This is similar to the standard mktime function but for GMT only, and
 * doesn't suffer from the various bugs and portability problems that
 * some systems' implementations have.
 */
static time_t my_timegm(struct my_tm *tm)
{
  static const int month_days_cumulative [12] =
    { 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334 };
  int month, year, leap_days;

  if(tm->tm_year < 70)
    /* we don't support years before 1970 as they will cause this function
       to return a negative value */
    return -1;

  year = tm->tm_year + 1900;
  month = tm->tm_mon;
  if(month < 0) {
    year += (11 - month) / 12;
    month = 11 - (11 - month) % 12;
  }
  else if(month >= 12) {
    year -= month / 12;
    month = month % 12;
  }

  leap_days = year - (tm->tm_mon <= 1);
  leap_days = ((leap_days / 4) - (leap_days / 100) + (leap_days / 400)
               - (1969 / 4) + (1969 / 100) - (1969 / 400));

  return ((((time_t) (year - 1970) * 365
            + leap_days + month_days_cumulative [month] + tm->tm_mday - 1) * 24
           + tm->tm_hour) * 60 + tm->tm_min) * 60 + tm->tm_sec;
}
/*
 * parsedate()
 *
 * Returns:
 *
 * PARSEDATE_OK     - a fine conversion
 * PARSEDATE_FAIL   - failed to convert
 * PARSEDATE_LATER  - time overflow at the far end of time_t
 * PARSEDATE_SOONER - time underflow at the low end of time_t
 */
static int parsedate(const char *date, time_t *output)
{
  time_t t = 0;
  int wdaynum=-1;  /* day of the week number, 0-6 (mon-sun) */
  int monnum=-1;   /* month of the year number, 0-11 */
  int mdaynum=-1; /* day of month, 1 - 31 */
  int hournum=-1;
  int minnum=-1;
  int secnum=-1;
  int yearnum=-1;
  int tzoff=-1;
  struct my_tm tm;
  enum assume dignext = DATE_MDAY;
  const char *indate = date; /* save the original pointer */
  int part = 0; /* max 6 parts */

  while(*date && (part < 6)) {
    int found=0;

    skip_over_white(&date);

    if(ISALPHA(*date)) {
      /* a name coming up */
      char buf[EST_CURL_MAX_NAME_STR]="";
      size_t len;
      sscanf(date, "%31[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz]",
             buf);
      len = strnlen_s(buf, EST_CURL_MAX_NAME_STR);

      if(wdaynum == -1) {
        wdaynum = checkday(buf, len);
        if(wdaynum != -1)
          found = 1;
      }
      if(!found && (monnum == -1)) {
        monnum = checkmonth(buf);
        if(monnum != -1)
          found = 1;
      }

      if(!found && (tzoff == -1)) {
        /* this just must be a time zone string */
        tzoff = checktz(buf);
        if(tzoff != -1)
          found = 1;
      }

      if(!found)
        return PARSEDATE_FAIL; /* bad string */

      date += len;
    }
    else if(ISDIGIT(*date)) {
      /* a digit */
      int val;
      char *end;
      if((secnum == -1) &&
         (3 == sscanf(date, "%02d:%02d:%02d", &hournum, &minnum, &secnum))) {
        /* time stamp! */
        date += 8;
      }
      else if((secnum == -1) &&
              (2 == sscanf(date, "%02d:%02d", &hournum, &minnum))) {
        /* time stamp without seconds */
        date += 5;
        secnum = 0;
      }
      else {
        long lval;
        int error;
        int old_errno;

        old_errno = ERRNO;
        SET_ERRNO(0);
        lval = strtol(date, &end, 10);
        error = ERRNO;
        if(error != old_errno)
          SET_ERRNO(old_errno);

        if(error)
          return PARSEDATE_FAIL;

        if((lval > (long)INT_MAX) || (lval < (long)INT_MIN))
          return PARSEDATE_FAIL;

        val = curlx_sltosi(lval);

        if((tzoff == -1) &&
           ((end - date) == 4) &&
           (val <= 1400) &&
           (indate< date) &&
           ((date[-1] == '+' || date[-1] == '-'))) {
          /* four digits and a value less than or equal to 1400 (to take into
             account all sorts of funny time zone diffs) and it is preceded
             with a plus or minus. This is a time zone indication.  1400 is
             picked since +1300 is frequently used and +1400 is mentioned as
             an edge number in the document "ISO C 200X Proposal: Timezone
             Functions" at http://david.tribble.com/text/c0xtimezone.html If
             anyone has a more authoritative source for the exact maximum time
             zone offsets, please speak up! */
          found = 1;
          tzoff = (val/100 * 60 + val%100)*60;

          /* the + and - prefix indicates the local time compared to GMT,
             this we need ther reversed math to get what we want */
          tzoff = date[-1]=='+'?-tzoff:tzoff;
        }

        if(((end - date) == 8) &&
           (yearnum == -1) &&
           (monnum == -1) &&
           (mdaynum == -1)) {
          /* 8 digits, no year, month or day yet. This is YYYYMMDD */
          found = 1;
          yearnum = val/10000;
          monnum = (val%10000)/100-1; /* month is 0 - 11 */
          mdaynum = val%100;
        }

        if(!found && (dignext == DATE_MDAY) && (mdaynum == -1)) {
          if((val > 0) && (val<32)) {
            mdaynum = val;
            found = 1;
          }
          dignext = DATE_YEAR;
        }

        if(!found && (dignext == DATE_YEAR) && (yearnum == -1)) {
          yearnum = val;
          found = 1;
          if(yearnum < 1900) {
            if(yearnum > 70)
              yearnum += 1900;
            else
              yearnum += 2000;
          }
          if(mdaynum == -1)
            dignext = DATE_MDAY;
        }

        if(!found)
          return PARSEDATE_FAIL;

        date = end;
      }
    }

    part++;
  }

  if(-1 == secnum)
    secnum = minnum = hournum = 0; /* no time, make it zero */

  if((-1 == mdaynum) ||
     (-1 == monnum) ||
     (-1 == yearnum))
    /* lacks vital info, fail */
    return PARSEDATE_FAIL;

#if SIZEOF_TIME_T < 5
  /* 32 bit time_t can only hold dates to the beginning of 2038 */
  if(yearnum > 2037) {
    *output = 0x7fffffff;
    return PARSEDATE_LATER;
  }
#endif

  if(yearnum < 1970) {
    *output = 0;
    return PARSEDATE_SOONER;
  }

  if((mdaynum > 31) || (monnum > 11) ||
     (hournum > 23) || (minnum > 59) || (secnum > 60))
    return PARSEDATE_FAIL; /* clearly an illegal date */

  tm.tm_sec = secnum;
  tm.tm_min = minnum;
  tm.tm_hour = hournum;
  tm.tm_mday = mdaynum;
  tm.tm_mon = monnum;
  tm.tm_year = yearnum - 1900;

  /* my_timegm() returns a time_t. time_t is often 32 bits, even on many
     architectures that feature 64 bit 'long'.

     Some systems have 64 bit time_t and deal with years beyond 2038. However,
     even on some of the systems with 64 bit time_t mktime() returns -1 for
     dates beyond 03:14:07 UTC, January 19, 2038. (Such as AIX 5100-06)
  */
  t = my_timegm(&tm);

  /* time zone adjust (cast t to int to compare to negative one) */
  if(-1 != (int)t) {

    /* Add the time zone diff between local time zone and GMT. */
    long delta = (long)(tzoff!=-1?tzoff:0);

    if((delta>0) && (t + delta < t))
      return -1; /* time_t overflow */

    t += delta;
  }

  *output = t;

  return PARSEDATE_OK;
}
/*	Find next Field
**	---------------
**	Finds the next RFC822 token in a string
**	On entry,
**	*pstr	points to a string containing a word separated
**		by white white space "," ";" or "=". The word
**		can optionally be quoted using <"> or "<" ">"
**		Comments surrrounded by '(' ')' are filtered out
**
**  On exit,
**	*pstr	has been moved to the first delimiter past the
**		field
**		THE STRING HAS BEEN MUTILATED by a 0 terminator
**
**	Returns	a pointer to the first word or NULL on error
*/
static char * HTNextField (char ** pstr)
{
    char * p;
    char * start = NULL;

    if (!pstr || !*pstr) {
        return NULL;
    }
    p = *pstr;
    
    while (1) {
        /* Strip white space and other delimiters */
        while (*p && (isspace((int)*p) || *p == ',' || *p == ';' || *p == '=')) {
            p++;
        }
        if (!*p) {
            *pstr = p;
            return NULL;                                         /* No field */
        }

        if (*p == '"') {                                     /* quoted field */
            start = ++p;
            for (; *p && *p != '"'; p++) {
                if (*p == '\\' && *(p + 1)) {
                    p++;                               /* Skip escaped chars */
                }
            }
            break;                          /* kr95-10-9: needs to stop here */
        } else if (*p == '<') {             /* quoted field */
            start = ++p;
            for (; *p && *p != '>'; p++) {
                if (*p == '\\' && *(p + 1)) {
                    p++;                               /* Skip escaped chars */
                }
            }
            break;                          /* kr95-10-9: needs to stop here */
        } else if (*p == '(') {             /* Comment */
            for (; *p && *p != ')'; p++) {
                if (*p == '\\' && *(p + 1)) {
                    p++;                               /* Skip escaped chars */
                }
            }
            p++;
        } else {                                              /* Spool field */
            start = p;
            while (*p && !isspace((int)*p) && *p != ',' && *p != ';' && *p != '=') {
                p++;
            }
            break;                                                 /* Got it */
        }
    }
    if (*p) {
        *p++ = '\0';
    }
    *pstr = p;
    return start;
}
/*
 * This function parses the authentication tokens from
 * the server when the server is requesting HTTP digest
 * authentication.  The tokens are required to generate
 * a valid authentication response in future HTTP
 * requests.
 */
static EST_ERROR est_io_parse_auth_tokens (EST_CTX *ctx, char *hdr)
{
    int rv = EST_ERR_NONE;
    char *p = hdr;
    char *token = NULL;
    char *value = NULL;
    int diff;
    errno_t safec_rc;

    /*
     * header will come in with the basic or digest field still on the front.
     * skip over it.
     */

    token = HTNextField(&p);

    while ((token = HTNextField(&p))) {
        if (!est_strcasecmp_s(token, "realm")) {
            if ((value = HTNextField(&p))) {
                if (EOK != strncpy_s(ctx->realm, MAX_REALM, value, MAX_REALM)) {
                    rv = EST_ERR_INVALID_TOKEN;
                }
            } else {
                rv = EST_ERR_INVALID_TOKEN;
            }
        } else if (!est_strcasecmp_s(token, "nonce")) {
            if ((value = HTNextField(&p))) {
                if (EOK != strncpy_s(ctx->s_nonce, MAX_NONCE, value, MAX_NONCE)) {
                    rv = EST_ERR_INVALID_TOKEN;
                }                
            } else {
                rv = EST_ERR_INVALID_TOKEN;
            }
        } else if (!est_strcasecmp_s(token, "qop")) {
            if ((value = HTNextField(&p))) {

                if (value[0] == '\0') {
                    EST_LOG_WARN("Unsupported qop value: %s", value);
                } else {
                    safec_rc = memcmp_s(value, sizeof("auth"), "auth", sizeof("auth"), &diff);
                    if (safec_rc != EOK) {
                        EST_LOG_INFO("memcmp_s error 0x%xO\n", safec_rc);
                    }
                    if (diff && (safec_rc == EOK)) {
                        EST_LOG_WARN("Unsupported qop value: %s", value);
                    }
                }
            } else {
                rv = EST_ERR_INVALID_TOKEN;
            }
        } else if (!est_strcasecmp_s(token, "algorithm")) {
            if ((value = HTNextField(&p)) && est_strcasecmp_s(value, "md5")) {
                EST_LOG_ERR("Unsupported digest algorithm: %s", value);
                /*
                 **  We only support MD5 for the moment
                 */
                rv = EST_ERR_INVALID_TOKEN;
            }
        } else if (!est_strcasecmp_s(token, "error")) {
            if ((value = HTNextField(&p))) {
                if (EOK != strncpy_s(ctx->token_error, MAX_TOKEN_ERROR, value, MAX_TOKEN_ERROR)) {
                    rv = EST_ERR_INVALID_TOKEN;
                }
            } else {
                rv = EST_ERR_INVALID_TOKEN;
            }
        } else if (!est_strcasecmp_s(token, "error_description")) {
            if ((value = HTNextField(&p))) {
                if (EOK != strncpy_s(ctx->token_error_desc, MAX_TOKEN_ERROR_DESC, value, MAX_TOKEN_ERROR_DESC)) {
                    rv = EST_ERR_INVALID_TOKEN;
                }
            } else {
                rv = EST_ERR_INVALID_TOKEN;
            }
        } else {
            EST_LOG_WARN("Unsupported auth token ignored: %s", token);
        }

        if (rv == EST_ERR_INVALID_TOKEN) {
            memzero_s(ctx->s_nonce, MAX_NONCE+1);
            break;
        }   
    }
    return (rv);
}
/*
 * This function parses the authentication tokens from
 * the server when the server is requesting HTTP digest
 * authentication.  The tokens are required to generate
 * a valid authentication response in future HTTP
 * requests.
 */
static HTTP_HEADER * parse_http_headers (unsigned char **buf, int *num_headers)
{
    int i;
    HTTP_HEADER *hdrs;
    char *hdr_end;
    errno_t safec_rc;

    *num_headers = 0;
    hdrs = malloc(sizeof(HTTP_HEADER) * MAX_HEADERS);
    if (!hdrs) {
        EST_LOG_ERR("malloc failure");
        return (NULL);
    }

    /*
     * Find offset of header deliminter
     */
    safec_rc = strstr_s((char *) *buf, strnlen_s((char *) *buf, RSIZE_MAX_STR),
            "\r\n\r\n", MAX_HEADER_DELIMITER_LEN, &hdr_end);

    if (safec_rc != EOK) {
        EST_LOG_INFO("strstr_s error 0x%xO\n", safec_rc);
    }

    /*
     * Skip the first line
     */
    skip((char **)buf, "\r\n");

    for (i = 0; i < MAX_HEADERS; i++) {
        hdrs[i].name = skip_quoted((char **)buf, ":", " ", 0);
        hdrs[i].value = skip((char **)buf, "\r\n");
        fflush(stdout);
        EST_LOG_INFO("Found HTTP header -> %s:%s", hdrs[i].name, hdrs[i].value);
        fflush(stdout);
        if (hdrs[i].name[0] == '\0') {
            break;
        }
        *num_headers = i + 1;
        if ((*buf) > (unsigned char *)hdr_end) {
            break;
        }
    }
    EST_LOG_INFO("Found %d HTTP headers\n", *num_headers);
    return (hdrs);
}
/*
 * This function parses the HTTP status code
 * in the first header.  Only a handful of codes are
 * handled by EST.  We are not a full HTTP stack.  Any
 * unrecognized codes will result in an error.
 * Note that HTTP 1.1 is expected.
 */
static int est_io_parse_response_status_code (unsigned char *buf)
{
    if (!strncmp((const char *)buf, EST_HTTP_HDR_200,
                        strnlen_s(EST_HTTP_HDR_200, EST_HTTP_HDR_MAX))) {
        return 200;
    } else if (!strncmp((const char *)buf, EST_HTTP_HDR_202,
                        strnlen_s(EST_HTTP_HDR_202, EST_HTTP_HDR_MAX))) {
        return 202;
    } else if (!strncmp((const char *)buf, EST_HTTP_HDR_204,
                        strnlen_s(EST_HTTP_HDR_204, EST_HTTP_HDR_MAX))) {
        return 204;
    } else if (!strncmp((const char *)buf, EST_HTTP_HDR_400,
                        strnlen_s(EST_HTTP_HDR_400, EST_HTTP_HDR_MAX))) {
        return 400;
    } else if (!strncmp((const char *)buf, EST_HTTP_HDR_401,
                        strnlen_s(EST_HTTP_HDR_401, EST_HTTP_HDR_MAX))) {
        return 401;
    } else if (!strncmp((const char *)buf, EST_HTTP_HDR_404,
                        strnlen_s(EST_HTTP_HDR_404, EST_HTTP_HDR_MAX))) {
        return 404;
    } else if (!strncmp((const char *)buf, EST_HTTP_HDR_423,
                        strnlen_s(EST_HTTP_HDR_423, EST_HTTP_HDR_MAX))) {
        return 423;
    } else {
        EST_LOG_ERR("Unhandled HTTP response %s", buf);
        return -1;
    }
}
/*
 * This function searches for and processes the WWW-Authenticate header from
 * the server.  The result is the setting of the auth_mode value in the
 * context.  If there is no WWW-Authenticate header, or the values in the
 * header are invalid, it will set the auth_mode to a failure setting.  If
 * there are multiple Authenticate headers, only the first one will be
 * processed.
 */
static void est_io_parse_http_auth_request (EST_CTX *ctx,
                                            HTTP_HEADER *hdrs,
                                            int hdr_cnt)
{
    int i;
    EST_ERROR rv;
    int auth_found = 0;

    /*
     * Walk the headers looking for the WWW-Authenticate.  We'll
     * only process the first one.  If an erroneous second one
     * is included, it will be ignored.
     */
    for (i = 0; i < hdr_cnt; i++) {
        if (!strncmp(hdrs[i].name, EST_HTTP_HDR_AUTH, 16)) {

            auth_found = 1;
            
            if (!strncmp(hdrs[i].value, "Basic", 5)) {
                ctx->auth_mode = AUTH_BASIC;
                /* Parse the realm */
                rv = est_io_parse_auth_tokens(ctx, hdrs[i].value);
                if (rv != EST_ERR_NONE) {
                    ctx->auth_mode = AUTH_FAIL;
                }    
            } else
            if (!strncmp(hdrs[i].value, "Digest", 6)) {
                ctx->auth_mode = AUTH_DIGEST;
                /* Parse the realm and nonce */
                rv = est_io_parse_auth_tokens(ctx, hdrs[i].value);
                if (rv != EST_ERR_NONE) {
                    ctx->auth_mode = AUTH_FAIL;
                }    
            } else if (!strncmp(hdrs[i].value, "Bearer", 6)) {
                ctx->auth_mode = AUTH_TOKEN;
                /* Parse the realm and possible token error fields */
                rv = est_io_parse_auth_tokens(ctx, hdrs[i].value);
                if (rv != EST_ERR_NONE) {
                    ctx->auth_mode = AUTH_FAIL;
                }    
            } else {
                EST_LOG_ERR("Unsupported WWW-Authenticate method");
                ctx->auth_mode = AUTH_FAIL;
            }

            break;
        }
    }

    if (!auth_found) {
        EST_LOG_ERR("No WWW-Authenticate header found");
        ctx->auth_mode = AUTH_FAIL;
    }    
    return;
}
/*
 * This function takes in the list of headers that were in the server's
 * response, it walks through the headers looking for a Retry-After response
 * header.  If one is found, the value is parsed and saved away in the EST
 * context.  This value can be in one of two formats, both are represented as
 * an ASCII string.  The first format can be a count of the number of seconds
 * the client should wait before retrying the request.  The second format is a
 * time/date stamp of the point in time at which the client should retry the
 * request.  The result of this function is the setting of the retry_after
 * values in the context.  If no retry-after header was received, or was
 * received and could not be parsed, the values will be zero, otherwise, they
 * are set to the value received.
 *
 * NOTE: The EST client currently does not support the time/date format
 * response and will not process a response in this format.
 */
static EST_ERROR est_io_parse_http_retry_after_resp (EST_CTX *ctx,
                                                     HTTP_HEADER *hdrs,
                                                     int hdr_cnt)
{
    EST_ERROR rv = EST_ERR_INVALID_RETRY_VALUE;
    int i;
    int cmp_result, diff;
    int rc;
    long long int temp_ll;
    int found = 0;
    
    /*
     * Initialize assuming there was no retry-after header.
     */
    ctx->retry_after_delay = 0;
    ctx->retry_after_date = 0;
    
    for (i = 0; i < hdr_cnt; i++) {
        
        cmp_result = strcasecmp_s(hdrs[i].name, sizeof(EST_HTTP_HDR_RETRY_AFTER),
                                  EST_HTTP_HDR_RETRY_AFTER, &diff);
        if (cmp_result == EOK && !diff) {
            
            EST_LOG_INFO("Retry-After value = %s", hdrs[i].value);
            found = 1;
            /*
             * Determine whether or not the value is a date/time string
             * or is an integer representing the number of seconds
             * that the client must wait.
             */
            if (isalpha(*(char *)hdrs[i].value)) {
#ifdef RETRY_AFTER_DELAY_TIME_SUPPORT
                int rc;
                /*
                 * Convert the date/time string into a time_t
                 */
                rc = parsedate(hdrs[i].value, &ctx->retry_after_date);
                if (rc != PARSEDATE_OK) {
                    EST_LOG_ERR("Retry-After value could not be parsed");
                }
#else
                /*
                 * This format is not currently supported.
                 */
                EST_LOG_ERR("Retry-After value not in the correct format");
#endif                
            } else {
                /*
                 * make sure it's all digits, make sure it's no larger than a
                 * four byte integer, and cache away the value returned for
                 * the retry delay.
                 */
                rc = strisdigit_s(hdrs[i].value, 10); // max of 10 decimal places
                if (rc) {
                    temp_ll = atoll(hdrs[i].value);
                    if (temp_ll <= INT_MAX) {
                        ctx->retry_after_delay = (int) temp_ll;
                        rv = EST_ERR_CA_ENROLL_RETRY;
                    } else {
                        EST_LOG_ERR("Retry-After value too large");
                    }
                    
                } else {
                    EST_LOG_ERR("Retry-After value could not be parsed");
                }
            }
        }
    }
    if (found == 0) {
        EST_LOG_ERR("Retry-After header missing");
    }    
    return rv;
}
/*
 * This function verifies the content type header and also
 * returns the length of the content header.  The
 * content type is important.  For example, the content
 * type is expected to be pkcs7 on a simple enrollment.
 */
static int est_io_check_http_hdrs (HTTP_HEADER *hdrs, int hdr_cnt,
                                   EST_OPERATION op)
{
    int i;
    int cl = 0;
    int content_type_present = 0, content_length_present = 0;
    int cmp_result;

    /*
     * Traverse all the http headers and process the ones that need to be
     * checked
     */
    for (i = 0; i < hdr_cnt; i++) {
        /*
         * Content type
         */
        memcmp_s(hdrs[i].name, sizeof(EST_HTTP_HDR_CT), EST_HTTP_HDR_CT,
            sizeof(EST_HTTP_HDR_CT), &cmp_result);
        if (!cmp_result) {
            content_type_present = 1;
            /*
             * Verify content is pkcs7 data
             */
            memcmp_s(hdrs[i].value,
                     strnlen_s(est_op_map[op].content_type, est_op_map[op].length),
                     est_op_map[op].content_type, strnlen_s(est_op_map[op].content_type, est_op_map[op].length),
                      &cmp_result);
            if (cmp_result) {
                EST_LOG_ERR("HTTP content type is %s", hdrs[i].value);
                return 0;
                }
        } else {
            /*
             * Content Length
             */
            memcmp_s(hdrs[i].name, sizeof(EST_HTTP_HDR_CL), EST_HTTP_HDR_CL,
                sizeof(EST_HTTP_HDR_CL), &cmp_result);
            if (!cmp_result) {
                content_length_present = 1;
                cl = atoi(hdrs[i].value);
            }
        }
    }
    
    /*
     * Make sure all the necessary headers were present.
     */
    if (content_type_present == 0 ) {
        EST_LOG_ERR("Missing HTTP content type  header");
        return 0;
    } else if (content_length_present == 0 ) {
        EST_LOG_ERR("Missing HTTP content length header");
        return 0;
    } 
    
    return cl;
}
/*
 * This function verifies the content type header and also
 * returns the length of the content header.  The
 * content type is important.  For example, the content
 * type is expected to be pkcs7 on a simple enrollment.
 */
static int est_ssl_read (SSL *ssl, unsigned char *buf, int buf_max,
                       int sock_read_timeout) 
{
    int timeout;
    int read_fd;
    int rv;
    struct pollfd pfd;
    
    /*
     * load up the timeval struct to be passed to the select
     */
    timeout = sock_read_timeout * 1000;

    read_fd = SSL_get_fd(ssl);
    pfd.fd = read_fd;
    pfd.events = POLLIN;
    pfd.revents = 0;

    errno = 0;
    rv = POLL(&pfd, 1, timeout);
    if (rv == 0) {
        EST_LOG_ERR("Socket poll timeout.  No data received from server.");
        return -1;
    } else if ( rv == -1) {
        EST_LOG_ERR("Socket read failure. errno = %d", errno);
        return -1;
    } else {
        return (SSL_read(ssl, buf, buf_max));
    }
}
/*
 * This function extracts data from the SSL context and puts
 * it into a buffer.
 */
static int est_io_read_raw (SSL *ssl, unsigned char *buf, int buf_max,
                            int *read_cnt, int sock_read_timeout)
{
    int cur_cnt;
    char peek_read_buf;

    *read_cnt = 0;
    cur_cnt  = est_ssl_read(ssl, buf, buf_max, sock_read_timeout);
    if (cur_cnt < 0) {
        EST_LOG_ERR("TLS read error 1");
	ossl_dump_ssl_errors();
        return (EST_ERR_SSL_READ);
    }
    *read_cnt += cur_cnt;

    /*
     * Multiple calls to SSL_read may be required to get the full
     * HTTP payload.
     */
    while (cur_cnt > 0 && *read_cnt < buf_max) {
        cur_cnt = est_ssl_read(ssl, (buf + *read_cnt), (buf_max - *read_cnt),
                               sock_read_timeout);
        if (cur_cnt < 0) {
            EST_LOG_ERR("TLS read error");
	    ossl_dump_ssl_errors();
            return (EST_ERR_SSL_READ);
        }
        *read_cnt += cur_cnt;
    }

    if ((*read_cnt == buf_max) && SSL_peek(ssl, &peek_read_buf, 1)) {
        EST_LOG_ERR("Buffer too small for received message");
        return(EST_ERR_READ_BUFFER_TOO_SMALL);
    }
    
    return (EST_ERR_NONE);
}
/*
 * This function provides the primary entry point into
 * this module.  It's used by the EST client to read the
 * HTTP response from the server.  The data is read from
 * the SSL context and HTTP parsing is invoked.
 *
 * If EST_ERR_NONE is returned then the raw_buf buffer must
 * be freed by the caller, otherwise, it is freed here.
 */
EST_ERROR est_io_get_response (EST_CTX *ctx, SSL *ssl, EST_OPERATION op,
                               unsigned char **buf, int *payload_len)
{
    int rv = EST_ERR_NONE;
    HTTP_HEADER *hdrs;
    int hdr_cnt;
    int http_status;
    unsigned char *raw_buf, *payload_buf, *payload;    
    int raw_len = 0;
    

    raw_buf = malloc(EST_CA_MAX);
    if (raw_buf == NULL) {
        EST_LOG_ERR("Unable to allocate memory");
        return EST_ERR_MALLOC;
    }
    memzero_s(raw_buf, EST_CA_MAX);
    payload = raw_buf;
    
    /*
     * Read the raw data from the SSL connection
     */
    rv = est_io_read_raw(ssl, raw_buf, EST_CA_MAX, &raw_len, ctx->read_timeout);
    if (rv != EST_ERR_NONE) {
        EST_LOG_INFO("No valid response to process");
        free(raw_buf);
        return (rv);
    }
    if (raw_len <= 0) {
        EST_LOG_WARN("Received empty HTTP response from server");
        free(raw_buf);
        return (EST_ERR_HTTP_NOT_FOUND);
    }
    EST_LOG_INFO("Read %d bytes of HTTP data", raw_len);
    
    /*
     * Parse the HTTP header to get the status
     * Look for status 200 for success
     */
    http_status = est_io_parse_response_status_code(raw_buf);
    ctx->last_http_status = http_status;
    hdrs = parse_http_headers(&payload, &hdr_cnt);
    EST_LOG_INFO("HTTP status %d received", http_status);

    /*
     * Check the Status header first to see
     * if the server accepted our request.
     */
    switch (http_status) {
    case 200:
        /* Server reported OK, nothing to do */
        break;
    case 204:
    case 404:
        EST_LOG_ERR("Server responded with 204/404, no content or not found");
        if (op == EST_OP_CSRATTRS) {
	    rv = EST_ERR_NONE;
        } else if (http_status == 404) {
            rv = EST_ERR_HTTP_NOT_FOUND;            
        } else {
            rv = EST_ERR_UNKNOWN;
        }
        break;
    case 202:
        /* Server is asking for a retry */
        EST_LOG_INFO("EST server responded with retry-after");
        rv = est_io_parse_http_retry_after_resp(ctx, hdrs, hdr_cnt);
        break;
    case 400:
        EST_LOG_ERR("HTTP response from EST server was BAD REQUEST");
        rv = EST_ERR_HTTP_BAD_REQ;
	break;
    case 401:
        /* Server is requesting user auth credentials */
        EST_LOG_INFO("EST server requesting user authentication");

        /* Check if we've already tried authenticating, if so, then bail
         * First time through, auth_mode will be set to NONE
         */
        if (ctx->auth_mode == AUTH_DIGEST ||
            ctx->auth_mode == AUTH_BASIC ||
            ctx->auth_mode == AUTH_TOKEN) {
            ctx->auth_mode = AUTH_FAIL;
            rv = EST_ERR_AUTH_FAIL;
            break;
        }
        est_io_parse_http_auth_request(ctx, hdrs, hdr_cnt);
        rv = EST_ERR_AUTH_FAIL;
        break;
            
    case 423:
        EST_LOG_ERR("Server responded with 423, the content we are attempting to access is locked");
        rv = EST_ERR_HTTP_LOCKED;
        break;
    case -1:
        /* Unsupported HTTP response */
        EST_LOG_ERR("Unsupported HTTP response from EST server (%d)", http_status);
        rv = EST_ERR_UNKNOWN;
        break;
    default:
        /* Some other HTTP response was given, do we want to handle these? */
        EST_LOG_ERR("HTTP response from EST server was %d", http_status);
        rv = EST_ERR_HTTP_UNSUPPORTED;
        break;
    }

    if (rv == EST_ERR_NONE) {
        /*
         * Get the Content-Type and Content-Length headers
         * and verify the HTTP response contains the correct amount
         * of data.
         */
        *payload_len = est_io_check_http_hdrs(hdrs, hdr_cnt, op);
        EST_LOG_INFO("HTTP Content len=%d", *payload_len);

        if (*payload_len > EST_CA_MAX) {
            EST_LOG_ERR("Content Length larger than maximum value of %d.",
                        EST_CA_MAX);
            rv = EST_ERR_UNKNOWN;
            *payload_len = 0;
            *buf = NULL;
        } else if (*payload_len == 0) {
            *payload_len = 0;
            *buf = NULL;
        } else {
            /*
             * Allocate the buffer to hold the payload to be passed back
             */
            payload_buf = malloc(*payload_len);   
            if (!payload_buf) {
                EST_LOG_ERR("Unable to allocate memory");
                free(raw_buf);
                free(hdrs);
                return EST_ERR_MALLOC;
            }
            memcpy_s(payload_buf, *payload_len, payload, *payload_len);
            *buf = payload_buf;
        }
    }
    
    if (raw_buf) {
        free(raw_buf);
    }
    if (hdrs) {
        free(hdrs);
    }
    return (rv);
}
