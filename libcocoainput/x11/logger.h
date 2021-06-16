#include <stdarg.h>

typedef void (*LogFunction)(const char *);


void initLogPointer(LogFunction log,LogFunction error,LogFunction debug);

void CILog(const char *msg,...);
void CIError(const char *msg,...);
void CIDebug(const char *msg,...);
