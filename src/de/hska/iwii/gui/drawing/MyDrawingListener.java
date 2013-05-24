package de.hska.iwii.gui.drawing;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.scene.Node;
//import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
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

	// Group myGroup = new Group();

	private ArrayList<Node> selectedFigures;
	ArrayList<Node> speicher = new ArrayList<Node>();

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
		Node node = myPanel.getChildren().get(myPanel.getChildren().size() - 1);

		FadeTransition ft = new FadeTransition(Duration.millis(300), node);

		ft.setFromValue(1.0);
		ft.setToValue(0.1);
		ft.setCycleCount(2);
		ft.setAutoReverse(true);

		ft.play();

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

		if (!(node instanceof MyPane)) {
			translate(node, xPos, yPos);
			rotate(node, 360);
		}

	}

	@Override
	public void selectFigure(Node node, double xPos, double yPos,
			boolean shiftPressed) {
		Node parentNode = highestInstance(node);
		// m Group -- dann alle selektiert fehlt noch

		// wenn nicht Pane
		if (!(parentNode instanceof MyPane)) {// drücken auf Figur

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
		// Vater finden
		if (!(node instanceof MyPane)) {
			while (!(node.getParent() instanceof MyPane)) {
				node = node.getParent();
			}
		}
		return node;
	}

	@Override
	public void deleteFigures() {
		for (int i = myPanel.getChildren().size() - 1; i >= 0; i--) {
			Shape currentElement = (Shape) myPanel.getChildren().get(i);
			if (currentElement.isSelected()) {
				myPanel.getChildren().remove(currentElement);
			}
		}

	}

	@Override
	public void copyFigures() {
		// ArrayList<Node> speicher = new ArrayList<Node>();
		int i = 0;

		// geht alle Kinder des Panel durch
		for (Node shape : myPanel.getChildren()) {

			// Shape kopieren und zu Speicher hinzufügen
			if (((Shape) shape).isSelected()) {

				Shape current = ((Shape) shape).copy();
				speicher.add(i, (Node) current);
				i++;
				System.out.println("Reinkopieren in Speicher");
			}
		}
	}

	@Override
	public void pasteFigures() {

		// Speicher mit kopierten Elemente zu Panel hinzufügen
		for (int j = 0; j < speicher.size(); j++) {
			this.myPanel.getChildren().add((Node) ((Shape) speicher.get(j)).copy());
		}

	}

	@Override
	public void moveSelectedFiguresToTop() {
		// /ganzes Panel durchlaufen
		for (int i = myPanel.getChildren().size() - 1; i >= 0; i--) {
			// zu verschiebendes Element zwischenspeichern
			Shape current = (Shape) myPanel.getChildren().get(i);

			// zu verschiebendes Element wird gelöscht,
			// anschließend ganze hinten eingefügt
			if (((Shape) current).isSelected()) {
				rotate((Node)current, 360);
			}
				myPanel.getChildren().remove(i);
				myPanel.getChildren().add((Node) current);
			}
		
		

	}

	@Override
	public void moveSelectedFiguresToBottom() {
		// /ganzes Panel durchlaufen
		for (int i = myPanel.getChildren().size() - 1; i >= 0; i--) {
			// zu verschiebendes Element zwischenspeichern
			Shape current = (Shape) myPanel.getChildren().get(i);

			// zu verschiebendes Element wird gelöscht,
			// anschließend ganz vorne eingefügt(index 0)
			if (((Shape) current).isSelected()) {
				rotate((Node)current, 360);
				myPanel.getChildren().remove(i);
				myPanel.getChildren().add(0, (Node) current);
			}
		}

	}

	@Override
	public void moveSelectedFiguresDown() {
		int ebene = 0;
		for (int i = 0; i < myPanel.getChildren().size(); i++) {
			// zu verschiebendes Element zwischenspeichern
			Shape current = (Shape) myPanel.getChildren().get(i);

			if (((Shape) current).isSelected()) {
				rotate((Node)current, 360);
				ebene = myPanel.getChildren().indexOf(current);
				System.out.println(ebene);
				myPanel.getChildren().remove(i);

				// fallse es an vorletzter stelle ist
				int index = i - 1;
				if (index <= 0) index = 0;
				
				myPanel.getChildren().add(index, (Node) current);
				rotate((Node)current, 360);
			}
		}
	}

	@Override
	public void moveSelectedFiguresUp() {
		// ganzes Panel durchlaufen
		for (int i = myPanel.getChildren().size() - 1; i >= 0; i--) {
			// zu verschiebendes Element zwischenspeichern
			Shape current = (Shape) myPanel.getChildren().get(i);
			
			// zu verschiebendes Element wird gelöscht,
			// anschließend wieder an stelle i+1(darüber) eingefügt
			if (((Shape) current).isSelected()) {
				myPanel.getChildren().remove(i);
				
				int index = i + 1;
				if (index >= this.myPanel.getChildren().size()) index = this.myPanel.getChildren().size();
				
				myPanel.getChildren().add(index, (Node) current);
				rotate((Node)current, 360);
			}
		}

	}

	@Override
	// geht auch
	public void groupFigures() {
		de.hska.iwii.gui.drawing.MyGroup myGroup = new de.hska.iwii.gui.drawing.MyGroup();

		// durchläuft Panel
		for (int i = myPanel.getChildren().size() - 1; i >= 0; i--) {
			Shape currentElement = (Shape) myPanel.getChildren().get(i);

			// wenn Element selektiert ist, füge es zur Gruppe hinzu
			if (currentElement.isSelected())
				myGroup.getChildren().add((Node) currentElement);
		}

		// entferne Bindung von shape zu Panel(da diese über Group verbunden
		// sind
		for (Node node : myGroup.getChildren()) {
			myPanel.getChildren().remove(node);
		}

		this.myPanel.getChildren().add((Node) myGroup);

	}

	@Override
	public void ungroupFigures() {

		// Alle Shapes durchlaufen
		for (int i = myPanel.getChildren().size() - 1; i >= 0; i--) {
			Shape currentElement = (Shape) myPanel.getChildren().get(i);

			// überprüfen ob Shape eine Gruppe ist & selektiert ist
			if (currentElement.isSelected()) {
				Node parent = highestInstance((Node)currentElement);				
				if (parent instanceof MyGroup) {
					// Kinder der Gruppe wieder direkt zu Pane hinzufügen
					for (int j = ((MyGroup) parent).getChildren().size() - 1; j >= 0; j--) {

						Shape currentChild = (Shape) ((MyGroup) parent)
								.getChildren().get(j);
						this.myPanel.getChildren().add((Node) currentChild);
					}
					// Gruppe entfernen
					this.myPanel.getChildren().remove((Node) parent);
	
				}
			}
		}
	}

	@Override
	public int getSelectedFiguresCount() {
		int count = 0;

		// läuft alle kinder der Panel durch und zählt die selektierten
		for (Node shape : myPanel.getChildren()) {
			if (((Shape) shape).isSelected() == true) {
				count++;
			}
		}
		return count;
	}

	@Override
	public int getFiguresInClipboardCount() {

		return speicher.size();
	}

	@Override
	public boolean isGroupSelected() {
		for (int i = myPanel.getChildren().size() - 1; i >= 0; i--) {
			Shape currentElement = (Shape) myPanel.getChildren().get(i);
			if (currentElement.isSelected()) {
				Node parent = highestInstance((Node)currentElement);
				
				if (parent instanceof MyGroup) {
					return true;	
				}
			}
		}
		return false;
	}

	@Override
	public void rotate(Node node, double angle) {

		Node newNode = highestInstance(node);

		RotateTransition rt = new RotateTransition(Duration.millis(300),
				newNode);
		rt.setByAngle(360);
		rt.setCycleCount(4);
		rt.setAutoReverse(true);

		rt.play();

	}

	@Override
	public void translate(Node node, double deltaX, double deltaY) {
		Node newNode = highestInstance(node);
		FadeTransition ft = new FadeTransition(Duration.millis(300), newNode);
		ft.setFromValue(1.0);// wie transparent es wird
		ft.setToValue(0.1);
		ft.setCycleCount(2);
		ft.setAutoReverse(true);

		ft.play();

	}

	@Override
	public void zoom(Node node, double zoomFactor) {
		// TODO Auto-generated method stub

	}

}