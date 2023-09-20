#include "libest_fake_headers.h"
tcw_err_t tcw_connect(tcw_sock_t *sock, tcw_opts_t *opts, const char *host,
                      unsigned short int port, SOCK_TYPE *fd);
tcw_err_t tcw_close(tcw_sock_t *sock);
