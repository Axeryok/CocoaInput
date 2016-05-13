//
//  Logger.h
//  cocoainput
//
//  Created by Axer on 2016/05/14.
//  Copyright © 2016年 Axer. All rights reserved.
//

#ifndef Logger_h
#define Logger_h

#import <Foundation/Foundation.h>

void initLogPointer(void (*log)(const char*),
                    void (*error)(const char*),
                    void (*debug)(const int, const char*));

void CILog(NSString* msg);
void CIError(NSString* msg);
void CIDebug(int debugLevel, NSString* msg);

#endif /* Logger_h */
