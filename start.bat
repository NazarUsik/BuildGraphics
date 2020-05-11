PATH C:\Program Files\Java\jdk1.8.0_231\bin
PATH=%SYSTEMROOT%\system32;%PATH%
javac -sourcepath ./src -d out src/paint/Main.java
java -classpath ./out paint.Main
rd /s/q d:\Programming\JavaPrograms\untitled2\out\paint