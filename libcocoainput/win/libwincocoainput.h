//#include <GLFW/glfw3.h>
#include <windows.h>
#include <stdio.h>

#include "logger.h"



void initialize(
	long hwnd,
	int*(*c_draw)(wchar_t*,int,int),
	void(*c_done)(wchar_t*),
	int (*c_rect)(float*),
	LogFunction log,
	LogFunction error,
	LogFunction debug
	);




void set_focus(int flag);
