#include "libest_fake_headers.h"
/*
 * These prototypes are private to est_server.c and are
 * not part of the public API.
 */
void est_send_http_error(EST_CTX *ctx, void *http_ctx, int fail_code);
/*
 * These prototypes are private to est_server.c and are
 * not part of the public API.
 */
int est_enroll_auth(EST_CTX *ctx, void *http_ctx, SSL *ssl, char *path_seg,
                    int reenroll);
/*
 * These prototypes are private to est_server.c and are
 * not part of the public API.
 */
int est_handle_cacerts (EST_CTX *ctx, unsigned char *ca_certs, int ca_certs_len,
                        void *http_ctx, char *path_seg);
/*
 * These prototypes are private to est_server.c and are
 * not part of the public API.
 */
int est_tls_uid_auth(EST_CTX *ctx, SSL *ssl, X509_REQ *req);
/*
 * These prototypes are private to est_server.c and are
 * not part of the public API.
 */
X509_REQ * est_server_parse_csr(unsigned char *pkcs10, int pkcs10_len);
/*
 * These prototypes are private to est_server.c and are
 * not part of the public API.
 */
int est_server_check_csr(X509_REQ *req);
/*
 * These prototypes are private to est_server.c and are
 * not part of the public API.
 */
EST_ERROR est_server_send_http_retry_after(EST_CTX *ctx, void *http_ctx, int delay);
