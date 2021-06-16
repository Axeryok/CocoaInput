//
//  DataManager.m
//  libcocoainput
//
//  Created by Axer on 2019/03/23.
//  Copyright © 2019年 Axer. All rights reserved.
//

#import "DataManager.h"
#import "cocoainput.h"
#import "Logger.h"

#define SPLIT_NSRANGE(x) (int)(x.location), (int)(x.length)

static DataManager* instance = nil;

@implementation DataManager

@synthesize hasPreeditText;
@synthesize isSentedInsertText;
@synthesize isBeforeActionSetMarkedText;

+ (instancetype)sharedManager {
    if (!instance) {
        instance = [[DataManager alloc] init];
    }
    return instance;
}

- (id)init {
    CIDebug(@"Textfield table has been initialized.");
    self = [super init];
    self.hasPreeditText=NO;
    self.dic = [NSMutableDictionary dictionary];
    self.activeView = nil;
    return self;
}

- (void) modifyGLFWView{
    NSView *view;
    while(1){
        view=[[NSApp keyWindow] contentView];
        if([[view className]isEqualToString:@"GLFWContentView"]==YES)break;
    }
    [[DataManager sharedManager] setGlfwView:view];
    replaceInstanceMethod([view class], @selector(keyDown:), @selector(org_keyDown:), [[DataManager sharedManager] class]);
    replaceInstanceMethod([view class], @selector(hasMarkedText), @selector(org_hasMarkedText), [[DataManager sharedManager] class]);
    replaceInstanceMethod([view class], @selector(markedRange), @selector(org_markedRange), [[DataManager sharedManager] class]);
    replaceInstanceMethod([view class], @selector(interpretKeyEvents:), @selector(org_interpretKeyEvents:), [[DataManager sharedManager] class]);
    replaceInstanceMethod([view class], @selector(insertText:replacementRange:), @selector(org_insertText:replacementRange:), [[DataManager sharedManager] class]);
    replaceInstanceMethod([view class],@selector(firstRectForCharacterRange:actualRange:),@selector(org_firstRectForCharacterRange:actualRange:),[[DataManager sharedManager] class]);
    replaceInstanceMethod([view class], @selector(setMarkedText:selectedRange:replacementRange:), @selector(org_setMarkedText:selectedRange:replacementRange:), [[DataManager sharedManager] class]);
    replaceInstanceMethod([view class], @selector(unmarkText), @selector(org_unmarkText), [[DataManager sharedManager] class]);
    CILog(@"Complete to modify GLFWView");
}

- (void)keyDown:(NSEvent*)theEvent {//GLFWContentView改変用
    //見づらすぎて改修しづらい
    if ([DataManager sharedManager].activeView != nil ){
              CIDebug(@"New keyEvent came and sent to textfield.");
              [self org_interpretKeyEvents:@[ theEvent ]];
    }
    //NSLog(@"keydown %d %d %d\n",[DataManager sharedManager].hasPreeditText,[DataManager sharedManager].isSentedInsertText,[DataManager sharedManager].isBeforeActionSetMarkedText);
    if ([DataManager sharedManager].hasPreeditText==NO&&[DataManager sharedManager].isSentedInsertText==NO&&[DataManager sharedManager].isBeforeActionSetMarkedText==NO){
        CIDebug(@"New keyEvent came and sent to original keyDown.");
        [self org_keyDown:theEvent];
    }
    [DataManager sharedManager].isSentedInsertText = NO;
    [DataManager sharedManager].isBeforeActionSetMarkedText = NO;
}

- (void)interpretKeyEvents:(NSArray*)eventArray{//GLFWContentView改変用
    ;
}
                
- (void)unmarkText {
    [DataManager sharedManager].isBeforeActionSetMarkedText = YES;
    [DataManager sharedManager].hasPreeditText = NO;
    [self org_unmarkText];
}

//確定文字列　（漢字変換を通さなかったものもここに入る）
- (void)insertText:(id)aString replacementRange:(NSRange)replacementRange {
    if (![DataManager sharedManager].hasPreeditText) {
        [DataManager sharedManager].isSentedInsertText = NO;
        [self org_insertText:aString replacementRange:replacementRange];
        return;
    }
    [DataManager sharedManager].hasPreeditText = NO;
    [DataManager sharedManager].isSentedInsertText = YES;
    /*const char *sentString;
    if ([aString isKindOfClass:[NSAttributedString class]]) {
        sentString = [[aString string] cStringUsingEncoding:NSUTF8StringEncoding];
    }
    else{
	sentString = [aString cStringUsingEncoding:NSUTF8StringEncoding];
    }*/
    [[DataManager sharedManager] activeView].insertText("",0,0);
    CIDebug([NSString stringWithFormat:@"MarkedText was determined:\"%@\"",aString]);
    [self org_insertText:aString replacementRange:replacementRange];//GLFWのオリジナルメソッドはCharEventを発行するので利用する
}

//漢字変換途中経過
- (void)setMarkedText:(id)aString
        selectedRange:(NSRange)selectedRange
     replacementRange:(NSRange)replacementRange {
    [DataManager sharedManager].hasPreeditText = YES;
    NSString* debugText = aString;
    const char* sentString;
    if ([aString isKindOfClass:[NSAttributedString class]]) {
        sentString = [[aString string] cStringUsingEncoding:NSUTF8StringEncoding];
        debugText = [aString string];
    } else {
        sentString = [aString cStringUsingEncoding:NSUTF8StringEncoding];
    }
    CIDebug([NSString stringWithFormat:@"MarkedText changed:\"%@\"", [aString description]]);
    [[DataManager sharedManager] activeView].setMarkedText(sentString, SPLIT_NSRANGE(selectedRange),
                       SPLIT_NSRANGE(replacementRange));
    [self org_setMarkedText:aString selectedRange:selectedRange replacementRange:replacementRange];
}

- (NSRect)firstRectForCharacterRange:(NSRange)aRange
                         actualRange:(NSRangePointer)actualRange {
    CIDebug(@"Called to determine where to draw.");
    if ([DataManager sharedManager].hasPreeditText == NO) {
        return [self org_firstRectForCharacterRange:aRange actualRange:actualRange];
    }
    float* rect = [[DataManager sharedManager] activeView].firstRectForCharacterRange();
    NSRect lwjgl = [self org_firstRectForCharacterRange:aRange actualRange:actualRange];//GLFWのオリジナルを呼び出すとウィンドウのポジションが得られるので利用する
    return NSMakeRect(rect[0]+lwjgl.origin.x,
                      [[[DataManager sharedManager] glfwView] frame].size.height-rect[1]+lwjgl.origin.y,
                      rect[2],
                      rect[3]);
}

- (BOOL)hasMarkedText {
    return NO;
}

- (NSRange)selectedRange {
    return NSMakeRange(NSNotFound, 0);
}

- (NSRange)markedRange {
    [[DataManager sharedManager] activeView].insertText("", 0, 0);
    [self unmarkText];
    return NSMakeRange(NSNotFound, 0);
}

- (NSArray*)validAttributesForMarkedText {
    return nil;
}

- (NSAttributedString*)attributedSubstringForProposedRange:(NSRange)aRange
                                               actualRange:
(NSRangePointer)actualRange {
    return nil;
}

- (NSUInteger)characterIndexForPoint:(NSPoint)aPoint {
    return 0;
}

- (void)doCommandBySelector:(nonnull SEL)selector {
}


// 警告消しのためのダミーメソッド
- (NSRange)org_markedRange{return NSMakeRange(NSNotFound, 0);}
- (BOOL)org_hasMarkedText{return YES;};
- (void)org_keyDown:(NSEvent*)theEvent{}
- (void)org_unmarkText{}
- (void)org_interpretKeyEvents:(NSArray*)eventArray{}
- (NSRect)org_firstRectForCharacterRange:(NSRange)aRange
                             actualRange:(NSRangePointer)actualRange{return NSMakeRect(0,0,0,0);}
- (void)org_insertText:(id)aString replacementRange:(NSRange)replacementRange{}
- (void)org_setMarkedText:(id)aString
            selectedRange:(NSRange)selectedRange
         replacementRange:(NSRange)replacementRange {}
                

@end
