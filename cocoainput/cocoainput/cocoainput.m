//
//  cocoainput.m
//  cocoainput
//
//  Created by Axer on 2016/03/24.
//  Copyright © 2016年 Axer. All rights reserved.
//

#import "cocoainput.h"

void initialize(void (*log)(const char*),
                void (*error)(const char*),
                void (*debug)(const int, const char*)) {
  initLogPointer(log, error, debug);

  while ([[[[NSApp keyWindow] contentView] className]
             isEqualToString:@"MacOSXOpenGLView"] != YES)
    ;
  SEL sel;
  Method methodReplacedWith;
  IMP imp;
  const char* encoding;
  CIDebug(1, @"Replacing KeyWindow's keyDown method with new one.");
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

  CIDebug(1, @"Modifying Quit keyboard-shortcut.");
  NSMenu* minecraftMenu = [[[NSApp mainMenu] itemAtIndex:0] submenu];
  [minecraftMenu itemAtIndex:[minecraftMenu numberOfItems] - 1]
      .keyEquivalentModifierMask +=
      NSControlKeyMask;  // NSCommandKeyMask+NSControlKeyMask
  CIDebug(1, [NSString stringWithFormat:@"Libcocoainput was built on %s %s.",
                                        __DATE__, __TIME__]);
}

void addInstance(const char* uuid,
                 void (*insertText_p)(const char*, const int, const int),
                 void (*setMarkedText_p)(const char*,
                                         const int,
                                         const int,
                                         const int,
                                         const int),
                 float* (*firstRectForCharacterRange_p)()) {
  CIDebug(
      1, [NSString stringWithFormat:@"New textfield %s has registered.", uuid]);
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
  CIDebug(1,
          [NSString stringWithFormat:@"Textfield %s has been removed.", uuid]);
  [[[DataManager sharedManager] dic]
      removeObjectForKey:[[NSString alloc]
                             initWithCString:uuid
                                    encoding:NSUTF8StringEncoding]];
}

void refreshInstance(void) {
  CIDebug(1, @"All textfields has been removed.");
  NSDictionary* instances = [[DataManager sharedManager] dic];
  [[NSTextInputContext currentInputContext] discardMarkedText];
  [DataManager sharedManager].activeView = nil;
  for (NSString* key in [instances allKeys]) {
    [[instances objectForKey:key] unmarkText];
  }
  [DataManager sharedManager].dic = [NSMutableDictionary dictionary];
}

void discardMarkedText(const char* uuid) {
  CIDebug(2, @"Active marked text has been discarded.");
  [[NSTextInputContext currentInputContext] discardMarkedText];
  [[[[DataManager sharedManager] dic]
      objectForKey:[[NSString alloc] initWithCString:uuid
                                            encoding:NSUTF8StringEncoding]]
      unmarkText];
}

void setIfReceiveEvent(const char* uuid, int yn) {
  CIDebug(1,
          [NSString stringWithFormat:@"Textfield %s's flag has changed to %d.",
                                     uuid, yn]);
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
  CIDebug(3, @"InvertYCoordinate function used");
  return [[NSScreen mainScreen] visibleFrame].size.height +
         [[NSScreen mainScreen] visibleFrame].origin.y - y;
}
