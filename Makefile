SOURCE_DIR  = src

OUTPUT_DIR  = bin

JFLAGS = -sourcepath $(SOURCE_DIR)        \
               -d $(OUTPUT_DIR)           \

JC = javac

CLASSES = \
	  src/game/Alien.java \
        src/game/Asteroid.java \
        src/game/Entity.java \
	  src/game/GameWindow.java \
   	  src/game/Gravitational.java \
	  src/game/Menu.java \
 	  src/game/Rogue.java \
	  src/game/Score.java \
	  src/game/Ship.java \
	  src/game/Shot.java \
	  src/game/SoundAsteroids.java \

default : $(CLASSES:.java=.class)

clean : 
	rm -r $(OUTPUT_DIR)/*.class

src/game/%.class: src/game/%.java
	javac $(JFLAGS) $<