//
//  cocoainput.m
//  cocoainput
//
//  Created by Axer on 2016/03/24.
//  Copyright © 2016年 Axer. All rights reserved.
//

#import "cocoainput.h"

void initialize(void) {
  while ([[[[NSApp keyWindow] contentView] className]
             isEqualToString:@"MacOSXOpenGLView"] != YES)
    ;
  SEL sel;
  Method methodReplacedWith;
  IMP imp;
  const char* encoding;

  sel = @selector(keyDown:);
  methodReplacedWith =
      class_getInstanceMethod([[[NSApp keyWindow] contentView] class], sel);
  imp = method_getImplementation(methodReplacedWith);
  encoding = method_getTypeEncoding(methodReplacedWith);
  class_addMethod([[[NSApp keyWindow] contentView] class],
                  @selector(org_keyDown:), imp,
                  encoding);  //オリジナルのメソッドをに改名

  methodReplacedWith =
      class_getInstanceMethod([[DataManager sharedManager] class], sel);
  imp = method_getImplementation(methodReplacedWith);
  encoding = method_getTypeEncoding(methodReplacedWith);

  class_replaceMethod([[[NSApp keyWindow] contentView] class], sel, imp,
                      encoding);  //新しいメソッドをオーバーライド

  NSLog(@"CocoaInput has been initialized.");
}

void addInstance(const char* uuid,
                 void (*insertText_p)(const char*, const int, const int),
                 void (*setMarkedText_p)(const char*,
                                         const int,
                                         const int,
                                         const int,
                                         const int),
                 float* (*firstRectForCharacterRange_p)()) {
  MinecraftView* mc = [[MinecraftView alloc] init];
  mc.insertText = insertText_p;
  mc.setMarkedText = setMarkedText_p;
  mc.firstRectForCharacterRange = firstRectForCharacterRange_p;
  mc.hasMark = NO;
  mc.isSentedInsertText = NO;
  mc.isBeforeActionSetMarkedText = NO;
  [[[DataManager sharedManager] dic]
      setObject:mc
         forKey:[[NSString alloc] initWithCString:uuid
                                         encoding:NSUTF8StringEncoding]];
}

void removeInstance(const char* uuid) {
  [[[DataManager sharedManager] dic]
      removeObjectForKey:[[NSString alloc]
                             initWithCString:uuid
                                    encoding:NSUTF8StringEncoding]];
}

void refreshInstance(void) {
  NSDictionary* instances = [[DataManager sharedManager] dic];
  [[NSTextInputContext currentInputContext] discardMarkedText];
  [DataManager sharedManager].activeView = nil;
  for (NSString* key in [instances allKeys]) {
    [[instances objectForKey:key] unmarkText];
  }
  [DataManager sharedManager].dic = [NSMutableDictionary dictionary];
}

void discardMarkedText(const char* uuid) {
  [[NSTextInputContext currentInputContext] discardMarkedText];
  [[[[DataManager sharedManager] dic]
      objectForKey:[[NSString alloc] initWithCString:uuid
                                            encoding:NSUTF8StringEncoding]]
      unmarkText];
}

void setIfReceiveEvent(const char* uuid, int yn) {
  if (yn == 1) {
    [DataManager sharedManager].activeView = [[[DataManager sharedManager] dic]
        objectForKey:[[NSString alloc] initWithCString:uuid
                                              encoding:NSUTF8StringEncoding]];
    [[[DataManager sharedManager].activeView inputContext] activate];
  } else {
    [[[[[DataManager sharedManager] dic]
        objectForKey:[[NSString alloc] initWithCString:uuid
                                              encoding:NSUTF8StringEncoding]]
        inputContext] deactivate];
    if ([DataManager sharedManager].activeView != nil &&
        [[[[DataManager sharedManager] dic]
            objectForKey:[[NSString alloc]
                             initWithCString:uuid
                                    encoding:NSUTF8StringEncoding]]
            isEqual:[DataManager sharedManager].activeView])
      [DataManager sharedManager].activeView = nil;
  }
}
float invertYCoordinate(float y) {
  return [[NSScreen mainScreen] visibleFrame].size.height +
         [[NSScreen mainScreen] visibleFrame].origin.y - y;
}

void issueKeyEvent(const char* str) {
  NSTimeInterval timestamp = [[NSDate date] timeIntervalSince1970];
  NSString* character =
      [[NSString alloc] initWithCString:str encoding:NSUTF8StringEncoding];
  NSEvent* event = [NSEvent keyEventWithType:NSKeyDown
                                    location:NSMakePoint(0, 0)
                               modifierFlags:0
                                   timestamp:timestamp
                                windowNumber:0
                                     context:nil
                                  characters:character
                 charactersIgnoringModifiers:character
                                   isARepeat:NO
                                     keyCode:94];
  [[[NSApp keyWindow] contentView] org_keyDown:event];
  event = [NSEvent keyEventWithType:NSKeyUp
                           location:NSMakePoint(0, 0)
                      modifierFlags:0
                          timestamp:timestamp
                       windowNumber:0
                            context:nil
                         characters:character
        charactersIgnoringModifiers:character
                          isARepeat:NO
                            keyCode:94];
  [[[NSApp keyWindow] contentView] keyUp:event];
}