CC	= gcc
CFLAGS	= -Wall -fobjc-arc -mmacosx-version-min=10.10 -arch x86_64 -arch arm64
FFLAGS	= -framework Foundation -framework Cocoa
OBJS	= cocoainput.o Logger.o DataManager.o MinecraftView.o
LIBS	=
TARGET	= libcocoainput.dylib
DISTDIR	= ../../../src/main/resources/darwin/
RM	= rm
CP	= cp

all: $(TARGET)

install: $(TARGET)
	$(CP) -f $(TARGET) $(DISTDIR)

$(TARGET): $(OBJS)
	$(CC) $(CFLAGS) $(FFLAGS) $(LIBS) -shared -o $@ $(OBJS)

.c.o:
	$(CC) $(CFLAGS) $(LIBS) -c $<

clean:
	$(RM) -f $(TARGET) $(OBJS)
