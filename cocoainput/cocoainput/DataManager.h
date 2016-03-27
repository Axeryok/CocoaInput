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
+ (instancetype)sharedManager;
- (void)keyDown:(NSEvent*)theEvent;

//形式上
- (void)org_keyDown:(NSEvent*)theEvent;
- (BOOL)org_enterFullScreenMode:(NSScreen*)screen
                    withOptions:(NSDictionary<NSString*, id>*)options;
- (void)interpretKeyEvents:(NSArray*)eventArray;
@end
