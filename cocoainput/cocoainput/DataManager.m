//
//  DataManager.m
//  cocoainput
//
//  Created by Axer on 2016/03/25.
//  Copyright © 2016年 Axer. All rights reserved.
//

#import "DataManager.h"

static DataManager* instance = nil;

@implementation DataManager
@synthesize activeView;
@synthesize dic;

+ (instancetype)sharedManager {
  if (!instance) {
    instance = [[DataManager alloc] init];
  }
  return instance;
}

- (id)init {
  self = [super init];
  self.dic = [NSMutableDictionary dictionary];
  self.activeView = nil;
  return self;
}

- (void)keyDown:(NSEvent*)theEvent {
  if ([DataManager sharedManager].activeView != nil) {
    [[[DataManager sharedManager] activeView] interpretKeyEvents:@[ theEvent ]];
  }
  if ([[DataManager sharedManager] activeView].hasMark == NO &&
      [[DataManager sharedManager] activeView].isSentedInsertText == NO)
    [self org_keyDown:theEvent];
  [[DataManager sharedManager] activeView].isSentedInsertText = NO;
}

- (BOOL)enterFullScreenMode:(NSScreen*)screen
                withOptions:(NSDictionary<NSString*, id>*)options {
  [[NSApp keyWindow]
      setCollectionBehavior:NSWindowCollectionBehaviorFullScreenPrimary];
  [[NSApp keyWindow] toggleFullScreen:self];
  return YES;
}
@end
