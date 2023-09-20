#include "libest_fake_headers.h"
void est_log (EST_LOG_LEVEL lvl, char *format, ...);
void est_log_backtrace (void);
/* From est.c */
char * est_get_tls_uid(SSL *ssl, int is_client);
/* From est.c */
EST_ERROR est_load_ca_certs(EST_CTX *ctx, unsigned char *raw, int size);
/* From est.c */
EST_ERROR est_load_trusted_certs(EST_CTX *ctx, unsigned char *certs, int certs_len);
/* From est.c */
void est_log(EST_LOG_LEVEL lvl, char *format, ...);
/* From est.c */
void est_log_version(void);
/* From est.c */
void est_hex_to_str(char *dst, unsigned char *src, int len);
/* From est.c */
int est_base64_encode (const char *src, int actual_src_len, char *dst, int max_dst_len);
/* From est.c */
int est_base64_decode(const char *src, char *dst, int max_len);
/* From est_server.c */
int est_http_request(EST_CTX *ctx, void *http_ctx,
                     char *method, char *uri,
                     char *body, int body_len, const char *ct);
/* From est_client.c */
EST_ERROR est_client_connect(EST_CTX *ctx, SSL **ssl);
/* From est_client.c */
int est_client_send_enroll_request(EST_CTX *ctx, SSL *ssl, BUF_MEM *bptr,
                                   unsigned char *pkcs7, int *pkcs7_len,
				   int reenroll);
/* From est_client.c */
void est_client_disconnect(EST_CTX *ctx, SSL **ssl);
/* From est_client.c */
int est_client_set_cert_and_key(SSL_CTX *ctx, X509 *cert, EVP_PKEY *key);
/* From est_client.c */
EST_ERROR est_client_set_uid_pw(EST_CTX *ctx, const char *uid, const char *pwd);
/* From est_client_http.c */
EST_ERROR est_io_get_response (EST_CTX *ctx, SSL *ssl, EST_OPERATION op,
                         unsigned char **buf, int *payload_len);
/* From est_proxy.c */
EST_ERROR est_proxy_http_request(EST_CTX *ctx, void *http_ctx,
                           char *method, char *uri,
                           char *body, int body_len, const char *ct);
/* From est_proxy.c */
void proxy_cleanup(EST_CTX *p_ctx);
/* From est_proxy.c */
EST_ERROR est_asn1_parse_attributes(const char *p, int len, int *offset);
/* From est_proxy.c */
EST_ERROR est_is_challengePassword_present(const char *base64_ptr, int b64_len, int *offset);
/* From est_proxy.c */
EST_ERROR est_add_challengePassword(const char *base64_ptr, int b64_len, char **new_csr, int *pop_len);
/* From est_proxy.c */
EST_ERROR est_proxy_retrieve_cacerts (EST_CTX *ctx, unsigned char **cacerts_rtn,
                                      int *cacerts_rtn_len);
/* From est_proxy.c */
EST_ERROR est_send_csrattr_data(EST_CTX *ctx, char *csr_data, int csr_len, void *http_ctx);
/* From est_proxy.c */
void cleanse_auth_credentials(EST_HTTP_AUTH_HDR *auth_cred);
/* From est_proxy.c */
EST_ERROR est_parse_uri (char *uri, EST_OPERATION *operation,
                         char **path_seg);
/* From est_proxy.c */
EST_ERROR est_store_path_segment (EST_CTX *ctx, char *path_segment,
                                  int path_segment_len);
/* From est_proxy.c */
EST_OPERATION est_parse_operation (char *op_path);
/* From est_proxy.c */
int est_strcasecmp_s (char *s1, char *s2);
