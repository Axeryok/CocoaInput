//
//  MinecraftView.h
//  libcocoainput
//
//  Created by Axer on 2019/03/23.
//  Copyright © 2019年 Axer. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <Cocoa/Cocoa.h>

@interface MinecraftView : NSObject
@property void (*insertText)(const char*, const int, const int);
@property void (*setMarkedText)(const char*, const int, const int, const int, const int);
@property float* (*firstRectForCharacterRange)(void);
@end
