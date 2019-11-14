# credit: https://www.cs.swarthmore.edu/~newhall/unixhelp/javamakefiles.html

JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
  DatagramServer.java \
	DatagramClient.java

default: classes

classes: $(CLASSES:.java=.class)

