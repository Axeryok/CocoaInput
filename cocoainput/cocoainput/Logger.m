//
//  Logger.m
//  cocoainput
//
//  Created by Axer on 2016/05/14.
//  Copyright © 2016年 Axer. All rights reserved.
//

#import <Foundation/Foundation.h>

struct {
  void (*log)(const char*);
  void (*error)(const char*);
  void (*debug)(const char*);
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

void initLogPointer(void (*log)(const char*),
                    void (*error)(const char*),
                    void (*debug)(const char*)) {
  LogPointer.log = log;
  LogPointer.error = error;
  LogPointer.debug = debug;
}
