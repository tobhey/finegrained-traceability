#include "libest_fake_headers.h"
int ossl_verify_cb(int ok, X509_STORE_CTX *ctx);
void ossl_dump_ssl_errors(void);
EST_ERROR ossl_init_cert_store (X509_STORE *store, unsigned char *raw1, int size1);
