#define _GNU_SOURCE

#include "logger.h"
#include <stdio.h>
#include <stdlib.h>

struct {
    LogFunction log;
    LogFunction error;
    LogFunction debug;
} LogPointer;

void CILog(const char* format,...) {
	char *msg;
	va_list args;
	va_start(args,format);
	vasprintf(&msg,format,args);
	LogPointer.log(msg);
	free(msg);
	va_end(args);
}


void CIError(const char* format,...) {
	char *msg;
	va_list args;
	va_start(args,format);
	vasprintf(&msg,format,args);
	LogPointer.error(msg);
	free(msg);
	va_end(args);
}


void CIDebug(const char* format,...) {
	char *msg;
	va_list args;
	va_start(args,format);
	vasprintf(&msg,format,args);
	LogPointer.debug(msg);
	free(msg);
	va_end(args);
}



void initLogPointer(LogFunction log,LogFunction error,LogFunction debug) {
    LogPointer.log = log;
    LogPointer.error = error;
    LogPointer.debug = debug;
}
