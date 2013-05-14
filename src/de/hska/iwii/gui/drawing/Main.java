package de.hska.iwii.gui.drawing;

import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String args[]) {
		
		//lege die javaFx Applikation an 
		//MainWindow myMainWindow = new MainWindow();
		
		//erstelle meine eigene Zeichenfl�che
		MyPane myPane = new MyPane(800,600);
		
		//Erstellen Test-Rechteck
		//Rectangle myRectangle = new Rectangle(10.0, 50.0, 50.0, 10.0, Color.rgb(255, 0, 255, 0.5));
		
		//Erstelle  Test-Line
		//Line myLine = new Line(50.0 , 50.0, 0.0 , 0.0, Color.rgb(150, 150, 255, 0.5));
		
		//Erstelle Ellipse
		//Ellipse myEllipse = new Ellipse( 50.0 , 50.0, 60.0 , 60.0  ,Color.rgb(255, 255, 255, 0.5));
		
		//F�ge er meiner Pane hinzu
		//myPane.getChildren().add(myRectangle);
		
		//�beregbedie _meine_Zeichenfl�che dem MainWindow
		MainWindow.setMainPanel(myPane);
		
		// Erzeuge meinen DrawingListener
        MyDrawingListener myDrawingListener = new MyDrawingListener();
        
        // �bergebe meinem DrawingListener die Zeichenfl�che
        myDrawingListener.setPanel(myPane);
        
        // �bergebe _meinen_ DrawingListener dem MainWindow
        MainWindow.setDrawingListener(myDrawingListener);

		
		//startet das Zeihenprogramm
		Application.launch(MainWindow.class, args);
			
			
			
	}

}
