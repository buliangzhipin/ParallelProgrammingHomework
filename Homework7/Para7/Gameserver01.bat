javac  -cp lib/* -d bin -encoding UTF-8 -sourcepath src src\para\GameServer01.java
java -cp bin;lib/*;resource --illegal-access=deny para.GameServer01