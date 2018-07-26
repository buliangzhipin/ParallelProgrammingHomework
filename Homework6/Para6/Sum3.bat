javac  -cp lib/* -d bin -encoding UTF-8 -sourcepath src src\para\opencl\Max3.java
java -cp bin;lib/*;resource --illegal-access=deny para.opencl.Max3