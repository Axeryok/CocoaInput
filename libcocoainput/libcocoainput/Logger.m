//
//  Logger.m
//  libcocoainput
//
//  Created by Axer on 2019/03/23.
//  Copyright © 2019年 Axer. All rights reserved.
//

#import "Logger.h"

struct {
    LogFunction log;
    LogFunction error;
    LogFunction debug;
} LogPointer;

void CILog(NSString* msg) {
    LogPointer.log([msg cStringUsingEncoding:NSUTF8StringEncoding]);
}
void CIError(NSString* msg) {
    LogPointer.error([msg cStringUsingEncoding:NSUTF8StringEncoding]);
}
void CIDebug(NSString* msg) {
    LogPointer.debug([msg cStringUsingEncoding:NSUTF8StringEncoding]);
}

void initLogPointer(LogFunction log,LogFunction error,LogFunction debug) {
    LogPointer.log = log;
    LogPointer.error = error;
    LogPointer.debug = debug;
}
