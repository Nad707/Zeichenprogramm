package de.hska.iwii.gui.drawing;

import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import de.hska.iwii.gui.drawing.Shape;

/**
 * Implementiert die Schnittstelle DrawingListener
 * 
 * @author Nadja
 * 
 */
public class MyDrawingListener implements DrawingListener {

	private Shape myShape;
	private double oldXPos, oldYPos;
	private Pane myPanel;
	private Shape actualFigure;
	private double FirstFigurePosX = 0;

	private double FirstFigurePosY = 0;
	double firstPosX;
	double firstPosY;

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

		if (!(node instanceof MyPane)) {
			((Shape) node).offset(xPos, yPos);
		}
	}

	/**
	 * Aufrufen der Methode, bis Anwender Maus los lässt.
	 */
	@Override
	public void workMoveFigure(Node node, double xPos, double yPos) {

		if (!(node instanceof MyPane)) {
			((Shape) node).move(xPos, yPos);
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

		// wenn nicht Pane
		if (!(node instanceof MyPane)) {

			// wenn nicht gedrückt, dann werden alle deselektiert
			if (!shiftPressed) {
				// alles soll false werden , for getch
				for (Node shape : myPanel.getChildren()) {
					((Shape) shape).setSelected(false);
				}

			}
			// angeklicktes immer selektiert
			((Shape) node).setSelected(true);

		} else { // Pane angedrückt, alles soll deselektieret werden
			for (Node shape : myPanel.getChildren()) {
				((Shape) shape).setSelected(false);
			}
		}

		//
		//
		// // Shift wurde gedrückt, wir selektieren munter drauf los
		// if (shiftPressed) {
		// // Falls wir eine Gruppe angewählt haben, wähle diese aus und füge
		// sie der selektierten Figuren hinzu
		// if (parentNode != null && parentNode instanceof Group) {
		// ((Shape) parentNode).setSelected(true);
		// this.selectedFigures.add(parentNode);
		// } else {
		// // Wir haben nur eine einzelne Figur angeklickt.
		// if (node instanceof Shape) {
		// ((Shape) node).setSelected(true);
		// this.selectedFigures.add(node);
		// }
		// }
		// }
		// // Egal was wir angeklickt haben, es wird deselektiert
		// else {
		// for (Node shape : selectedFigures) {
		// ((Shape) shape).setSelected(false);
		// }
		//
		// selectedFigures = new ArrayList<Node>();
		//
		// }
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
		Group myGroup = new Group();
		// geht alle Kinder von Panel durch von hinten
		for (int i = myPanel.getChildren().size() - 1; i >= 0; i--) {

			// Node ( bis get(i)) soll zu Shape werden, isSelected ist schon
			// Methode von Shape
			if (((Shape) myPanel.getChildren().get(i)).isSelected() == true) {
				myGroup.getChildren().add(myPanel.getChildren().get(i));
			}
		}

		this.myPanel.getChildren().add((Node) myGroup);
		
		for (Node node : myPanel.getChildren() ) {
			
		}
		
		
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
		// TODO Auto-generated method stub
		return false;
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