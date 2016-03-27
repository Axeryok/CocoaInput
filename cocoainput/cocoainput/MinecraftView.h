//
//  MinecraftView.h
//  cocoainput
//
//  Created by Axer on 2016/03/24.
//  Copyright © 2016年 Axer. All rights reserved.
//

#import <Cocoa/Cocoa.h>

@interface MinecraftView : NSView<NSTextInputClient>

@property BOOL hasMark;
@property BOOL isSentedInsertText;
@property BOOL isBeforeActionSetMarkedText;
@property void (*insertText)(const char*, const int, const int);
@property void (*setMarkedText)
    (const char*, const int, const int, const int, const int);
@property float* (*firstRectForCharacterRange)();
@end
