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
                void (*debug)(const char*)
                ) {
  initLogPointer(log, error, debug);
  while(1){
      [DataManager sharedManager].openglView=[[NSApp keyWindow] contentView];
      if([[[DataManager sharedManager].openglView className]isEqualToString:@"MacOSXOpenGLView"]==YES)break;
  }
  NSView* mainView = [DataManager sharedManager].openglView;
  CIDebug(@"Replacing KeyWindow's keyDown method with new one.");
  replaceInstanceMethod([mainView class], @selector(keyDown:),
                        @selector(org_keyDown:),
                        [[DataManager sharedManager] class]);
  replaceInstanceMethod([[mainView window] class], @selector(toggleFullScreen:),
                        @selector(org_toggleFullScreen:),
                        [[DataManager sharedManager] class]);
  CIDebug(@"Modifying Quit keyboard-shortcut.");
  NSMenu* minecraftMenu = [[[NSApp mainMenu] itemAtIndex:0] submenu];
  [minecraftMenu itemAtIndex:[minecraftMenu numberOfItems] - 1]
      .keyEquivalentModifierMask +=
      NSControlKeyMask;  // NSCommandKeyMask+NSControlKeyMask
  CIDebug([NSString stringWithFormat:@"Libcocoainput was built on %s %s.",
                                        __DATE__, __TIME__]);
}

void replaceInstanceMethod(Class cls, SEL sel, SEL renamedSel, Class dataCls) {
  Method methodReplacedWith = class_getInstanceMethod(cls, sel);
  IMP imp = method_getImplementation(methodReplacedWith);
  const char* encoding = method_getTypeEncoding(methodReplacedWith);
  class_addMethod(cls, renamedSel, imp, encoding);
  methodReplacedWith = class_getInstanceMethod(dataCls, sel);
  imp = method_getImplementation(methodReplacedWith);
  encoding = method_getTypeEncoding(methodReplacedWith);
  class_replaceMethod(cls, sel, imp, encoding);
}

void addInstance(const char* uuid,
                 void (*insertText_p)(const char*, const int, const int),
                 void (*setMarkedText_p)(const char*,
                                         const int,
                                         const int,
                                         const int,
                                         const int),
                 float* (*firstRectForCharacterRange_p)()) {
  CIDebug([NSString stringWithFormat:@"New textfield %s has registered.", uuid]);
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
  CIDebug([NSString stringWithFormat:@"Textfield %s has been removed.", uuid]);
  [[[DataManager sharedManager] dic]
      removeObjectForKey:[[NSString alloc]
                             initWithCString:uuid
                                    encoding:NSUTF8StringEncoding]];
}

void refreshInstance(void) {
  CIDebug(@"All textfields has been removed.");
  if([DataManager sharedManager].activeView){
    [[[DataManager sharedManager].activeView inputContext] deactivate];
  }
  NSDictionary* instances = [[DataManager sharedManager] dic];
  [[NSTextInputContext currentInputContext] discardMarkedText];
  [DataManager sharedManager].activeView = nil;
  for (NSString* key in [instances allKeys]) {
    [[instances objectForKey:key] unmarkText];
  }
  [DataManager sharedManager].dic = [NSMutableDictionary dictionary];
}

void discardMarkedText(const char* uuid) {
  CIDebug(@"Active marked text has been discarded.");
  [[NSTextInputContext currentInputContext] discardMarkedText];
  [[[[DataManager sharedManager] dic]
      objectForKey:[[NSString alloc] initWithCString:uuid
                                            encoding:NSUTF8StringEncoding]]
      unmarkText];
}

void setIfReceiveEvent(const char* uuid, int yn) {
  CIDebug([NSString stringWithFormat:@"Textfield %s's flag has changed to %d.",
                                     uuid, yn]);
  if (yn == 1) {
    [DataManager sharedManager].activeView = [[[DataManager sharedManager] dic]
        objectForKey:[[NSString alloc] initWithCString:uuid
                                              encoding:NSUTF8StringEncoding]];
    [[[DataManager sharedManager].activeView inputContext]
        performSelectorOnMainThread:@selector(activate)
                         withObject:nil
                      waitUntilDone:YES];//activateメソッドはmainThreadで実行しないとExceptionを起こすらしい
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
  CIDebug(@"InvertYCoordinate function used");
  return [[NSScreen mainScreen] visibleFrame].size.height +
         [[NSScreen mainScreen] visibleFrame].origin.y - y;
}

void toggleFullScreen(){
    [[NSApp keyWindow] toggleFullScreen:@"CocoaInput"];
}
