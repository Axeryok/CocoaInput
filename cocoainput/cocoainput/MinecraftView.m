//
//  MinecraftView.m
//  cocoainput
//
//  Created by Axer on 2016/03/24.
//  Copyright © 2016年 Axer. All rights reserved.
//

#import "MinecraftView.h"
#define SPLIT_NSRANGE(x) (int)(x.location), (int)(x.length)
#define MAKE_NSRECT_FROM_ARRAY(x) NSMakeRect(x[0], x[1], x[2], x[3])

@implementation MinecraftView
@synthesize hasMark;
@synthesize isSentedInsertText;
@synthesize isBeforeActionSetMarkedText;

- (id)init {
  self = [super init];
  return self;
}

- (void)unmarkText {
  self.isBeforeActionSetMarkedText = YES;
  self.hasMark = NO;
}

//確定文字列　（漢字変換を通さなかったものもここに入る）
- (void)insertText:(id)aString replacementRange:(NSRange)replacementRange {
  if (!hasMark) {
    self.isSentedInsertText = NO;
    return;
  }
  NSString* debugText = aString;
  self.hasMark = NO;
  self.isSentedInsertText = YES;
  const char* sentString;
  if ([aString isKindOfClass:[NSAttributedString class]]) {
    sentString = [[aString string] cStringUsingEncoding:NSUTF8StringEncoding];
    debugText = [aString string];
  } else {
    sentString = [aString cStringUsingEncoding:NSUTF8StringEncoding];
  }
  CIDebug([NSString stringWithFormat:@"MarkedText was determined:\"%@\"",debugText]);
  self.insertText(sentString, SPLIT_NSRANGE(replacementRange));
}

//漢字変換途中経過
- (void)setMarkedText:(id)aString
        selectedRange:(NSRange)selectedRange
     replacementRange:(NSRange)replacementRange {
  NSString* debugText = aString;
  self.hasMark = YES;
  const char* sentString;
  if ([aString isKindOfClass:[NSAttributedString class]]) {
    sentString = [[aString string] cStringUsingEncoding:NSUTF8StringEncoding];
    debugText = [aString string];
  } else {
    sentString = [aString cStringUsingEncoding:NSUTF8StringEncoding];
  }
  CIDebug([NSString stringWithFormat:@"MarkedText changed:\"%@\"", [aString description]]);
  self.setMarkedText(sentString, SPLIT_NSRANGE(selectedRange),
                     SPLIT_NSRANGE(replacementRange));
}

- (NSRect)firstRectForCharacterRange:(NSRange)aRange
                         actualRange:(NSRangePointer)actualRange {
  CIDebug(@"Called to determine where to draw.");
  if (self.hasMark == NO) {
    return NSMakeRect(0, 0, 0, 0);
  }
  float* rect = self.firstRectForCharacterRange();

  return MAKE_NSRECT_FROM_ARRAY(rect);
}

- (BOOL)hasMarkedText {
  return NO;
}

- (NSRange)selectedRange {
  return NSMakeRange(NSNotFound, 0);
}

- (NSRange)markedRange {
  self.insertText("", 0, 0);
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

@end
