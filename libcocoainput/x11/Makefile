CC	= gcc
CFLAGS	= -Wall -fPIC
FFLAGS	= -lX11
OBJS	= libx11cocoainput.o logger.o
LIBS	= 
TARGET	= libx11cocoainput.so
DISTDIR	= ../../src/main/resources/x11/
RM	= rm
CP	= cp

all: $(TARGET)

install: $(TARGET)
	$(CP) -f $(TARGET) $(DISTDIR)

$(TARGET): $(OBJS)
	$(CC) $(OBJS) $(CFLAGS) $(FFLAGS) $(LIBS) -shared -o $@

.c.o:
	$(CC) $(CFLAGS) $(LIBS) -c $<

clean:
	$(RM) -f $(TARGET) $(OBJS)
