#include "libx11cocoainput.h"

void (*javaDone)();
int* (*javaDraw)(int,int,int,short,int,char*,wchar_t*,int,int,int);
XICCallback calet,start,done,draw;
XICCallback s_start,s_done,s_draw;




void setCallback(int*(*c_draw)(int,int,int,short,int,char*,wchar_t*,int,int,int),void(*c_done)()){
	javaDraw=c_draw;
	javaDone=c_done;
}


int preeditCalet(XIC xic,XPointer clientData,XPointer data){
	return 0;
}
int preeditStart(XIC xic,XPointer clientData,XPointer data){
	CILog("Preedit start");
	return 0;
}
int preeditDone(XIC xic,XPointer clientData,XPointer data){
	CILog("Preedit end");
	javaDone();
	return 0;
}
int preeditDraw(XIC xic,XPointer clientData,XPointer structptr){
	CILog("Preedit draw start");
	XIMPreeditDrawCallbackStruct* structure=(XIMPreeditDrawCallbackStruct*)structptr;
	int *array;
	int secondary=0;
	int length=0;
	if(structure->text){
		int i=0;
		int secondary_determined=0;
		for(i=0;i!=structure->text->length;i++){
			if(!secondary_determined&&structure->text->feedback[i]!=XIMUnderline){
				secondary=i;
				secondary_determined=1;
			}
			if(secondary_determined&&(structure->text->feedback[i]==0||structure->text->feedback[i]!=XIMUnderline)){
				length++;
			}
			else if(secondary_determined)break;
		}
	}

	CILog("Invoke Javaside");
	if(structure->text){
		array=javaDraw(
			structure->caret,
			structure->chg_first,
			structure->chg_length,
			structure->text->length,
			structure->text->encoding_is_wchar,
			structure->text->encoding_is_wchar?"":structure->text->string.multi_byte,
			structure->text->encoding_is_wchar?structure->text->string.wide_char:L"",
			0,
			secondary,
			secondary+length
		);
	}
	else {
		array=javaDraw(
			structure->caret,
			structure->chg_first,
			structure->chg_length,
			0,
			0,
			"",
			L"",
			0,
			0,
			0		
		);
	}
	
	//TODO なんとかしてon-the-spotの候補ウィンドウをちゃんとした位置にしたいね（願望）
	XVaNestedList attr;
	XPoint place;
	place.x=array[0];place.y=array[1];
	attr=XVaCreateNestedList(0,XNSpotLocation,&place,NULL);
	XSetICValues(xic,XNPreeditAttributes,attr,NULL);
	XFree(attr);
	CILog("Preedit draw end");
	return 0;
}

int statusStart(XIC xic,XPointer clientData,XPointer data){
	return 0;
}
int statusDone(XIC xic,XPointer clientData,XPointer data){
	return 0;
}
int statusDraw(XIC xic,XPointer clientData,XPointer data/*,XIMStatusDrawCallbackStruct *structure*/){
	return 0;
}



XVaNestedList preeditCallbacksList(){
	calet.client_data=NULL;
	start.client_data=NULL;
	done.client_data=NULL;
	draw.client_data=NULL;
	calet.callback=preeditCalet;
	start.callback=preeditStart;
	done.callback=preeditDone;
	draw.callback=preeditDraw;
	return XVaCreateNestedList(0,
		XNPreeditStartCallback,
		&start,
		XNPreeditCaretCallback,
		&calet,
		XNPreeditDoneCallback,
		&done,
		XNPreeditDrawCallback,
		&draw,NULL);
}


XVaNestedList statusCallbacksList(){
	s_start.client_data=NULL;
	s_done.client_data=NULL;
	s_draw.client_data=NULL;
	s_start.callback=statusStart;
	s_done.callback=statusDone;
	s_draw.callback=statusDraw;
	return XVaCreateNestedList(0,
		XNStatusStartCallback,
		&s_start,
		XNStatusDoneCallback,
		&s_done,
		XNStatusDrawCallback,
		&s_draw,NULL);
}



_GLFWwindowX11 * x11c; //_GLFWwindowX11のアドレス
XIM xim; //_GLFWwindowX11が保持するXIM
Window xwindow; //


XIC activeic; // IMが有効なIC
XIC inactiveic; //IMが無効なIC



void initialize(
	long waddr,
	long xw,
	int*(*c_draw)(int,int,int,short,int,char*,wchar_t*,int,int,int),
	void(*c_done)(),
	LogFunction log,
	LogFunction error,
	LogFunction debug
	){
	initLogPointer(log,error,debug);
	CILog("CocoaInput X11 Clang Initializer start. library compiled at  %s %s",__DATE__,__TIME__);
	
	setCallback(c_draw,c_done);
	
	CILog("Window ptr:%p",(Window)xw);
	CILog("GLFWwindow ptr:%p",(void*)waddr);
	CILog("Searching _GLFWwindowx11 from GLFWwindow ptr...");
	int i;
	XIC ic=NULL;
	for(i=0;i<0x500;i++){
		Window po = (*(((_GLFWwindowX11*)(waddr+i)))).handle;
                if(po!=xw)continue;
		x11c = (((_GLFWwindowX11*)(waddr+i)));
                ic = (*(((_GLFWwindowX11*)(waddr+i)))).ic;
                CILog("Found offset:%d ,_GLFWwindowX11(%p)=GLFWwindow(%p)+%d ",i,x11c,(void*)waddr,i);
		break;
	}
	CILog("XIC mem address:%p",x11c->ic);
        xim = XIMOfIC(ic);
	CILog("XIM mem address:%p",xim);
	xwindow=xw;
	inactiveic = XCreateIC(
                xim,
                XNClientWindow,
                (Window)xwindow,
                XNFocusWindow,
                (Window)xwindow,
                XNInputStyle,
                XIMPreeditNone|XIMStatusNone,
                NULL);
	CILog("Created inactiveic-> default");
	activeic = XCreateIC(
                xim,
                XNClientWindow,
                xwindow,
                XNFocusWindow,
                xwindow,
                XNInputStyle,
                XIMPreeditCallbacks|XIMStatusNone,
                //XIMPreeditNothing|XIMStatusNothing,
                XNPreeditAttributes,
                preeditCallbacksList(),
                XNStatusAttributes,
                statusCallbacksList(),
                NULL);
	CILog("Created activeic");
	XSetICFocus(inactiveic);
	XUnsetICFocus(activeic);
	CILog("Completed ic focus");
	XDestroyIC(x11c->ic);
	x11c->ic = inactiveic;
	CILog("Destroyed glfw ic");
	CILog("CocoaInput X11 initializer done!");
}

void set_focus(int flag){
	XUnsetICFocus(x11c->ic);
	if(flag){
		x11c->ic=activeic;
	}
	else{
		x11c->ic= inactiveic;
	}
        XSetICFocus(x11c->ic);
	CILog("setFocused:%d",flag);
}
