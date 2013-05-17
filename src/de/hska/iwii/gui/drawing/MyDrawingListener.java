package de.hska.iwii.gui.drawing;

import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import de.hska.iwii.gui.drawing.Shape;

/**
 * Implementiert die Schnittstelle DrawingListener
 * 
 * @author Nadja
 */
 
public class MyDrawingListener implements DrawingListener {
//test hallooo
	//hier eine Ver‰‰‰‰‰‰nerung
	private Shape myShape;
	private double oldXPos, oldYPos;
	private Pane myPanel;
	private Shape actualFigure;
	private double FirstFigurePosX = 0;

	private double FirstFigurePosY = 0;
	double firstPosX;
	double firstPosY;
	
	Group myGroup = new Group();

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
	 * String enth‰lt Name der Figur.
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

		// F¸ge die Figur der Zeichenfl‰che hinzu
		if (this.myShape != null) {
			// Warum getChildren ?????????????
			this.myPanel.getChildren().add((Node) this.myShape);
		}
	}

	// Bearbeite die Grˆﬂe der Figur
	/**
	 * legt die Grˆﬂe der Figur fest. Methode wird solange aufgerufen, bis
	 * Anwender Maustaste los l‰sst.
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
			this.myShape = null;// wird eigentlich ¸berschrieben
		}
	}

	/**
	 * Figur wird von Anwender ausgew‰hlt.
	 */
	@Override
	public void startMoveFigure(Node node, double xPos, double yPos) {
		Node parentNode = highestInstance(node);
		if (!(parentNode instanceof MyPane)) {
			((Shape) parentNode).offset(xPos, yPos);
		}
	}

	/**
	 * Aufrufen der Methode, bis Anwender Maus los l‰sst.
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
		if (!(parentNode instanceof MyPane)) {//dr¸cken auf Figur

			// wenn nicht gedr¸ckt, dann werden alle deselektiert
			if (!shiftPressed) {
				// alles soll false werden , for getch
				for (Node shape : myPanel.getChildren()) {
					((Shape) shape).setSelected(false);
				}
			}
			// angeklicktes immer selektiert
			((Shape) parentNode).setSelected(true);
			
			
				
				// was m¸ssen wir tun wenn ein element ausgew‰hlt wird 
				// das zu einer Gruppe gehˆrt ????
				
			

		} else { // Pane angedr¸ckt, alles soll deselektieret werden
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
		// TODO Auto-generated method stub

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
	public void groupFigures() {
		//Group myGroup = new Group();
		
		for(int i = myPanel.getChildren().size() - 1; i >= 0; i--)
		{
			Shape currentElement = (Shape)myPanel.getChildren().get(i);
			if (currentElement.isSelected())
				myGroup.getChildren().add((Node)currentElement);
		}
		
		for(Node node : myGroup.getChildren())
		{
			myPanel.getChildren().remove(node);
		}

		
		this.myPanel.getChildren().add((Node) myGroup);
				
	}

	@Override
	public void ungroupFigures() {
		
		
		
	}

	@Override
	public int getSelectedFiguresCount() {
		int count = 0;

		for (Node shape : myPanel.getChildren()) {
			if (((Shape) shape).isSelected() == true) {
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
	
		
		return myGroup.isSelected() ;
		
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