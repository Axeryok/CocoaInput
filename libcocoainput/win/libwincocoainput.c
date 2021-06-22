#include "libwincocoainput.h"
#include <imm.h>

void (*javaDone)(wchar_t*);
int* (*javaDraw)(wchar_t*,int,int);
int (*javaRect)(float*);



void setCallback(int*(*c_draw)(wchar_t*,int,int),void(*c_done)(wchar_t*),int (*c_rect)(float*)){
	javaDraw=c_draw;
	javaDone=c_done;
	javaRect=c_rect;
}


HWND hwnd;
HIMC himc;


LRESULT compositionLocationNotify(HWND hWnd){
	HIMC imc=NULL;
	float *rect= malloc(sizeof(float)*4);
	CILog("ready call javaRect");
	if(javaRect(rect)){
		free(rect);
		return FALSE;
	}
	CANDIDATEFORM rectStruct = {0,CFS_EXCLUDE,{rect[0],rect[1]},{rect[0]-rect[2],rect[1]-rect[3],rect[0]+1,rect[1]+1}};
	imc=ImmGetContext(hwnd);
	ImmSetCandidateWindow(imc,&rectStruct);
	ImmReleaseContext(hWnd,imc);
	free(rect);
	return TRUE;
}

LRESULT CALLBACK (*glfwWndProc)(HWND,UINT,WPARAM,LPARAM);
LRESULT CALLBACK wrapper_wndProc(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam){
	switch(msg){
		case WM_IME_NOTIFY:{
			if(wParam==IMN_OPENCANDIDATE){
				compositionLocationNotify(hWnd);
				return TRUE;
			}
			else{
				break;
			}
		}
		case WM_IME_STARTCOMPOSITION:{
			compositionLocationNotify(hWnd);
			return TRUE;
		}
		case WM_IME_ENDCOMPOSITION:{
			javaDone(L"");
			return TRUE;
		}
		case WM_IME_COMPOSITION:{
			HIMC imc=NULL;
			LONG textSize,attrSize,clauseSize;
			int i, focusedBlock, length;
			LPWSTR buffer;
			LPSTR attributes;
			DWORD* clauses;
			LONG cursor=0;
			if(lParam&GCS_RESULTSTR){
				CILog("ResultStr");
				imc=ImmGetContext(hwnd);
				textSize = ImmGetCompositionStringW(imc, GCS_RESULTSTR, NULL, 0);
				length = textSize / sizeof(WCHAR);
				buffer = calloc(length + 1, sizeof(WCHAR));
				ImmGetCompositionStringW(imc, GCS_RESULTSTR, buffer, textSize);
				ImmReleaseContext(hWnd, imc);
				javaDone(buffer);
			}
			if(lParam&GCS_COMPSTR){
				imc=ImmGetContext(hwnd);
				if(lParam&GCS_CURSORPOS){
					cursor=ImmGetCompositionStringW(imc, GCS_CURSORPOS, NULL, 0);
				}

				textSize = ImmGetCompositionStringW(imc, GCS_COMPSTR, NULL, 0);
				attrSize = ImmGetCompositionStringW(imc, GCS_COMPATTR, NULL, 0);
				clauseSize = ImmGetCompositionStringW(imc, GCS_COMPCLAUSE, NULL, 0);
				if(textSize<=0){
					ImmReleaseContext(hWnd, imc);
					javaDone(L"");
				}
				else{
					length = textSize / sizeof(WCHAR);
					buffer = calloc(length + 1, sizeof(WCHAR));
					attributes = calloc(attrSize, 1);
					clauses = calloc(clauseSize, 1);
					ImmGetCompositionStringW(imc, GCS_COMPSTR, buffer, textSize);
					ImmGetCompositionStringW(imc, GCS_COMPATTR, attributes, attrSize);
					ImmGetCompositionStringW(imc, GCS_COMPCLAUSE, clauses, clauseSize);
					ImmReleaseContext(hWnd, imc);
					int selected_begin=-1;
					int selected_length=0;
					int i;
					for(i=0;i<attrSize;i++){
						if(attributes[i]&(ATTR_TARGET_CONVERTED)){
							if(selected_begin==0){
								selected_begin=i;
							}
							selected_length++;
						}
					}
					if(selected_begin>=0){
						cursor=selected_begin;
					}
					compositionLocationNotify(hWnd);
					javaDraw(buffer,cursor,selected_length);
				}
			}
			return TRUE;
		}
		default:break;
	}
	return CallWindowProc(glfwWndProc,hWnd,msg,wParam,lParam);
}



void initialize(
	long hwndp,
	int*(*c_draw)(wchar_t*,int,int),
	void(*c_done)(wchar_t*),
	int (*c_rect)(float*),
	LogFunction log,
	LogFunction error,
	LogFunction debug
){
	initLogPointer(log,error,debug);
	CILog("CocoaInput Windows Clang Initializer start. library compiled at  %s %s",__DATE__,__TIME__);
	setCallback(c_draw,c_done,c_rect);
	hwnd=(HWND)hwndp;
	glfwWndProc = GetWindowLongPtr(hwnd,GWLP_WNDPROC);
	SetWindowLongPtr(hwnd,GWLP_WNDPROC,wrapper_wndProc);
	CILog("Window procedure replaced");
	//input_himc = ImmGetContext(hwnd);
	/*if(!hImc){
	hImc = ImmCreateContext();
	HIMC oldhImc = ImmAssociateContext( hwnd, hImc );
}*/
//ImmReleaseContext(hwnd,input_himc);
himc = ImmGetContext(hwnd);
if(!himc){
	himc = ImmCreateContext();
}
ImmReleaseContext(hwnd,himc);
himc=ImmAssociateContext(hwnd,0);
CILog("CocoaInput Windows initializer done!");
}

void set_focus(int flag){

	CILog("setFocused:%d",flag);
	if(flag){
		ImmAssociateContext(hwnd,himc);
		compositionLocationNotify(hwnd);
	}
	else{
		himc=ImmAssociateContext(hwnd,0);
	}
}
