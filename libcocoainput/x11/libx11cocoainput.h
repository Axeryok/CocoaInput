//#include <GLFW/glfw3.h>
#include <X11/Xlib.h>
//#include "internal.h"
//#include "x11_platform.h"
#include <stdio.h>

#include <X11/X.h>
#include <X11/XKBlib.h>
#include <X11/Xutil.h>
#include <X11/keysym.h>
#include <X11/Xlocale.h>

#include "logger.h"

typedef int GLFWbool;

typedef struct _GLFWwindowX11
{
    Colormap        colormap;
    Window          handle;
    Window          parent;
    XIC             ic;

    GLFWbool        overrideRedirect;
    GLFWbool        iconified;
    GLFWbool        maximized;

    // Whether the visual supports framebuffer transparency
    GLFWbool        transparent;

    // Cached position and size used to filter out duplicate events
    int             width, height;
    int             xpos, ypos;

    // The last received cursor position, regardless of source
    int             lastCursorPosX, lastCursorPosY;
    // The last position the cursor was warped to by GLFW
    int             warpCursorPosX, warpCursorPosY;

    // The time of the last KeyPress event per keycode, for discarding
    // duplicate key events generated for some keys by ibus
    Time            keyPressTimes[256];

} _GLFWwindowX11;


void initialize(
	long waddr,
	long xw,
	int*(*c_draw)(int,int,int,short,int,char*,wchar_t*,int,int,int),
	void(*c_done)(),
	LogFunction log,
	LogFunction error,
	LogFunction debug
	);




void set_focus(int flag);
