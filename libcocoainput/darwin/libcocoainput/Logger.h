//
//  Logger.h
//  libcocoainput
//
//  Created by Axer on 2019/03/23.
//  Copyright © 2019年 Axer. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef void (*LogFunction)(const char*);

void initLogPointer(LogFunction log,LogFunction error,LogFunction debug);

void CILog(NSString* msg);
void CIError(NSString* msg);
void CIDebug(NSString* msg);
