//
//  cocoainput.h
//  cocoainput
//
//  Created by Axer on 2016/03/24.
//  Copyright © 2016年 Axer. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <objc/runtime.h>

#import "DataManager.h"
#import "MinecraftView.h"

void initialize(void);

void addInstance(const char* uuid,
                 void (*insertText_p)(const char*, const int, const int),
                 void (*setMarkedText_p)(const char*,
                                         const int,
                                         const int,
                                         const int,
                                         const int),
                 float* (*firstRectForCharacterRange)());

void removeInstance(const char* uuid);

void refreshInstance(void);

void discardMarkedText(const char* uuid);

void setIfReceiveEvent(const char* uuid, int yn);

float invertYCoordinate(float y);

void issueKeyEvent(const char* str);