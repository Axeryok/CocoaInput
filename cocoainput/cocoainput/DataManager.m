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
@synthesize openglView;
@synthesize openglWindow;
@synthesize activeView;
@synthesize dic;

+ (instancetype)sharedManager {
  if (!instance) {
    instance = [[DataManager alloc] init];
  }
  return instance;
}

- (id)init {
  CIDebug(@"Textfield table has been initialized.");
  self = [super init];
  self.dic = [NSMutableDictionary dictionary];
  self.activeView = nil;
  return self;
}

- (void)keyDown:(NSEvent*)theEvent {
  //見づらすぎて改修しづらい
  if ([DataManager sharedManager].activeView !=  // activeViewがnilな時
          // MarkedTextが無い状況での制御文字はtheEventを解釈させない
          nil &&
      ([[DataManager sharedManager] activeView].hasMark == YES ||
       ([[DataManager sharedManager] activeView].hasMark == NO &&
        (([[theEvent characters] characterAtIndex:0] != 0x7F &&
          ([[theEvent characters] characterAtIndex:0] > 0x1B &&
           [[theEvent characters] characterAtIndex:0] < 0xF700)) ||
         [[theEvent characters] characterAtIndex:0] > 0xF8FF)))) {
    CIDebug(@"New keyEvent came and sent to textfield.");
    [[[DataManager sharedManager] activeView] interpretKeyEvents:@[ theEvent ]];
  }
  if ([[DataManager sharedManager] activeView].hasMark == NO &&
      [[DataManager sharedManager] activeView].isSentedInsertText == NO &&
      [[DataManager sharedManager] activeView].isBeforeActionSetMarkedText ==
          NO)
    [self org_keyDown:theEvent];
  [[DataManager sharedManager] activeView].isSentedInsertText = NO;
  [[DataManager sharedManager] activeView].isBeforeActionSetMarkedText = NO;
}


@end
