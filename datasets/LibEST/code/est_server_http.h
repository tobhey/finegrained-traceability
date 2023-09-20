#include "libest_fake_headers.h"
// MINGW typedefs pid_t to int. Using #define here.
static int pthread_mutex_lock(pthread_mutex_t *);
// MINGW typedefs pid_t to int. Using #define here.
static int pthread_mutex_unlock(pthread_mutex_t *);
// MINGW typedefs pid_t to int. Using #define here.
static void to_unicode(const char *path, wchar_t *wbuf, size_t wbuf_len);
