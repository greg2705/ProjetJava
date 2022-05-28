#!/bin/bash
`javac --module-path ./Lib --add-modules=javafx.controls,javafx.fxml,javafx.base,javafx.graphics,javafx.media,gson  Main.java Jeux/*.java Controleur/*.java Model/*.java Util/*.java Vue/*.java`
direc=`pwd`
`java --module-path $direc/Lib --add-modules=javafx.fxml,javafx.controls,javafx.media --add-modules javafx.base,javafx.graphics --add-reads javafx.base=ALL-UNNAMED --add-reads javafx.graphics=ALL-UNNAMED -Dfile.encoding=UTF-8 -classpath $direc:/home/mass/int/Graphes2/Lib/gson.jar:$direc/Lib/javafx-swt.jar:$direc/Lib/javafx.base.jar:$direc/Lib/javafx.controls.jar:$direc/Lib/javafx.fxml.jar:$direc/Lib/javafx.graphics.jar:$direc/Lib/javafx.media.jar:$direc/Lib/javafx.swing.jar:$direc/Lib/javafx.web.jar Main`
