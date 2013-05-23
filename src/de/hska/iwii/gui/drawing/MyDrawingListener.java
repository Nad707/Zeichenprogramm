package de.hska.iwii.gui.drawing;

import java.util.ArrayList;


import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import de.hska.iwii.gui.drawing.Shape;

/**
 * Implementiert die Schnittstelle DrawingListener
 * 
 * @author Nadja
 */
 
public class MyDrawingListener implements DrawingListener {

	private Shape myShape;
	private double oldXPos, oldYPos;
	private Pane myPanel;

	
	double firstPosX;
	double firstPosY;
	
	//Group myGroup = new Group();

	private ArrayList<Node> selectedFigures;

	public void setPanel(Pane myPane) {
		this.myPanel = myPane;
	}

	public MyDrawingListener() {
		selectedFigures = new ArrayList<Node>();
	}

	// Erzeuge neue Figur
	/**
	 * Erzeugt neue Figur, an der Stelle, an der sich Mauszeiger befindet.
	 * String enthält Name der Figur.
	 */
	public void startCreateFigure(String figureType, double xPos, double yPos) {
		// merke mir die alte Mausposition
		this.oldXPos = xPos;
		this.oldYPos = yPos;

		// Erstelle mir mein Rechteck
		if (figureType.compareTo("rectangle") == 0) {
			this.myShape = new Rectangle(xPos, yPos, 10, 10, Color.rgb(255, 0,
					255, 0.5));
		}

		// Erstelle mir meine Linie
		if (figureType.compareTo("line") == 0) {
			this.myShape = new Line(xPos, yPos, xPos, yPos, Color.rgb(139, 10,
					80, 0.5));
		}

		// erstelle mir meine Ellipse
		if (figureType.compareTo("ellipse") == 0) {
			this.myShape = new Ellipse(xPos, yPos, 1, 1, Color.rgb(205, 41,
					144, 0.5));
		}

		// Füge die Figur der Zeichenfläche hinzu
		if (this.myShape != null) {
			// Warum getChildren ?????????????
			this.myPanel.getChildren().add((Node) this.myShape);
		}
	}

	// Bearbeite die Größe der Figur
	/**
	 * legt die Größe der Figur fest. Methode wird solange aufgerufen, bis
	 * Anwender Maustaste los lässt.
	 */
	public void workCreateFigure(double xPos, double yPos) {
		if (this.myShape != null) {
			this.myShape.resizeInteractive(this.oldXPos, this.oldYPos, xPos,
					yPos);
		}
	}

	/**
	 * Erstellung der Figur ist beendet.
	 */
	public void endCreateFigure(double xPos, double yPos) {

		if (this.myShape != null) {
			this.myShape = null;// wird eigentlich überschrieben
		}
	}

	/**
	 * Figur wird von Anwender ausgewählt.
	 */
	@Override
	public void startMoveFigure(Node node, double xPos, double yPos) {
		Node parentNode = highestInstance(node);
		if (!(parentNode instanceof MyPane)) {
			((Shape) parentNode).offset(xPos, yPos);
		}
	}

	/**
	 * Aufrufen der Methode, bis Anwender Maus los lässt.
	 */
	@Override
	public void workMoveFigure(Node node, double xPos, double yPos) {
		Node parentNode = highestInstance(node);
		if (!(parentNode instanceof MyPane)) {
			((Shape) parentNode).move(xPos, yPos);
		}
	}

	/**
	 * Beenden der Verschiebung.
	 */
	@Override
	public void endMoveFigure(Node node, double xPos, double yPos) {

	}

	@Override
	public void selectFigure(Node node, double xPos, double yPos,
			boolean shiftPressed) {
		Node parentNode = highestInstance(node);
		//m Group -- dann alle selektiert fehlt noch 
		
		
		// wenn nicht Pane
		if (!(parentNode instanceof MyPane)) {//drücken auf Figur

			// wenn nicht gedrückt, dann werden alle deselektiert
			if (!shiftPressed) {
				// alles soll false werden , for getch
				for (Node shape : myPanel.getChildren()) {
					((Shape) shape).setSelected(false);
				}
			}
			// angeklicktes immer selektiert
			((Shape) parentNode).setSelected(true);
			
		} else { // Pane angedrückt, alles soll deselektieret werden
			for (Node shape : myPanel.getChildren()) {
				((Shape) shape).setSelected(false);
			}
		}

	}

	public Node highestInstance(Node node) {
		if (!(node instanceof MyPane)) {
			while (!(node.getParent() instanceof MyPane)) {
				node = node.getParent();
			}
		}

		return node;
	}

	@Override
	public void deleteFigures() {
		// TODO Auto-generated method stub
		// remove chi
	}

	@Override
	public void copyFigures() {
		 ArrayList<Node> speicher = new ArrayList<Node>();
		 int i = 0;
		
		 //geht alle Kinder des Panel durch
		 for (Node shape : myPanel.getChildren()) {
		
			 //Shape kopieren und zu Speicher hinzufügen
				if( ((Shape)shape).isSelected()){
					
					Shape current = ((Shape) shape).copy();
					speicher.add( i,(Node) current );
					i++;
				}
			}

		 //Speicher mit kopierten Elemente zu Panel hinzufügen
		 for (int j = 0; j < speicher.size(); j++) {
			this.myPanel.getChildren().add((Node) speicher.get(j));
		}

	}

	@Override
	public void pasteFigures() {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveSelectedFiguresToTop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveSelectedFiguresToBottom() {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveSelectedFiguresDown() {
		// TODO Auto-generated method stub

	}
	@Override
	public void moveSelectedFiguresUp() {
		// TODO Auto-generated method stub

	}

	@Override
	//geht auch 
	public void groupFigures() {
		de.hska.iwii.gui.drawing.MyGroup myGroup = new de.hska.iwii.gui.drawing.MyGroup();
		
		//durchläuft Panel
		for(int i = myPanel.getChildren().size() - 1; i >= 0; i--)
		{
			Shape currentElement = (Shape)myPanel.getChildren().get(i);
			
			//wenn Element selektiert ist, füge es zur Gruppe hinzu
			if (currentElement.isSelected())
				myGroup.getChildren().add((Node)currentElement);
		}
		
		//entferne Bindung von shape zu Panel(da diese über Group verbunden sind
		for(Node node : myGroup.getChildren())
		{
			myPanel.getChildren().remove(node);
		}

		
		this.myPanel.getChildren().add((Node) myGroup);
				
	}

	@Override
	public void ungroupFigures() {
		
		//Alle Shapes durchlaufen 
		for(int i = myPanel.getChildren().size() - 1; i >= 0; i--){
			Shape currentElement = (Shape)myPanel.getChildren().get(i);
		
			//überprüfen ob Shape eine Gruppe ist & selektiert ist 
			if (currentElement instanceof MyGroup && isGroupSelected()){
				
				//Kinder der Gruppe wieder direkt zu Pane hinzufügen
				for(int j = ((MyGroup) currentElement).getChildren().size() - 1; j >= 0; j--){
					
					Shape currentChild = (Shape) ((MyGroup)currentElement).getChildren().get(j);
					this.myPanel.getChildren().add((Node)currentChild);
				}
				//Gruppe entfernen 
				this.myPanel.getChildren().remove((Node)currentElement);
			}
			
		}
	}

	@Override
	public int getSelectedFiguresCount() {
		int count = 0;
		
		//läuft alle kinder der Panel durch und zählt die selektierten 
		for (Node shape : myPanel.getChildren()) {
			if (((Shape) shape).isSelected() == true ) {
				count++;
			}
		}
		return count;
	}

	@Override
	public int getFiguresInClipboardCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isGroupSelected() {
		for(int i = myPanel.getChildren().size() - 1; i >= 0; i--)
		{
			Shape currentElement = (Shape)myPanel.getChildren().get(i);
			if (currentElement instanceof de.hska.iwii.gui.drawing.MyGroup)
				return true;
		}
		return false ;
		
	}

	@Override
	public void rotate(Node node, double angle) {
		// TODO Auto-generated method stub

	}

	@Override
	public void translate(Node node, double deltaX, double deltaY) {
		// TODO Auto-generated method stub

	}

	@Override
	public void zoom(Node node, double zoomFactor) {
		// TODO Auto-generated method stub

	}

}