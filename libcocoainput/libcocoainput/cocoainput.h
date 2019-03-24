//
//  cocoainput.h
//  libcocoainput
//
//  Created by Axer on 2019/03/23.
//  Copyright © 2019年 Axer. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "Logger.h"


void initialize(LogFunction log,LogFunction error,LogFunction debug);
void replaceInstanceMethod(Class cls, SEL sel, SEL renamedSel, Class dataCls);

void addInstance(const char* uuid,
                 void (*insertText_p)(const char*, const int, const int),
                 void (*setMarkedText_p)(const char*,
                                         const int,
                                         const int,
                                         const int,
                                         const int),
                 float* (*firstRectForCharacterRange)(void));
void removeInstance(const char* uuid);
void refreshInstance(void);

void discardMarkedText(const char* uuid);
void setIfReceiveEvent(const char* uuid, int yn);
