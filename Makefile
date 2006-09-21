NAME=JTetris
JC = javac
JFLAGS = -g
OPT = -O3
WARN = -Wall
MANIFEST=manifest.txt

default:
	$(JC) $(JFLAGS) *.java

clean:
	$(RM) *.class *.jar

jar: default
	jar cvfe $(NAME).jar $(NAME) *.class
	rm *.class


