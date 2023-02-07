//
//  DataManager.h
//  cocoainput
//
//  Created by Axer on 2016/03/25.
//  Copyright © 2016年 Axer. All rights reserved.
//

#import <Cocoa/Cocoa.h>
#import "MinecraftView.h"

@interface DataManager : NSObject
@property NSMutableDictionary* dic;
@property MinecraftView* activeView;
@property NSView* openglView;
@property NSWindow* openglWindow;
@property void (*toggleFullScreen)();
+ (instancetype)sharedManager;
- (void)keyDown:(NSEvent*)theEvent;

//形式上
- (void)org_keyDown:(NSEvent*)theEvent;
- (void)interpretKeyEvents:(NSArray*)eventArray;
@end
