package de.hska.iwii.gui.drawing;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Line extends javafx.scene.shape.Line implements
		de.hska.iwii.gui.drawing.Shape {

	private boolean selected;
	double offX;
	double offY;
	double offXEnd;
	double offYEnd;

	public Line(double startX, double startY, double endX, double endY,
			Paint color) {
		// übernehme Attribute
		super(startX, startY, endX, endY);

		setFill(color);
		setStroke(color);
		setStrokeWidth(5);
		this.selected = false;

	}

	@Override
	public void resizeInteractive(double xFirst, double yFirst, double xNew,
			double yNew) {

		setEndX(xNew);
		setEndY(yNew);

	}

	@Override
	public Shape clone() throws CloneNotSupportedException {
		Line cloneLine = new Line(this.getStartX(), this.getStartY(),
				this.getEndX(), this.getEndY(), this.getFill());

		return cloneLine;
	}

	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
		this.setStroke(Color.rgb(130, 0, 0));

		if (!selected) {
			this.setStroke(Color.rgb(139, 10, 80, 0.5));
		}
	}

	@Override
	public boolean isSelected() {
		return this.selected;
	}

	public void offset(double xFirst, double yFirst) {
		// Maus bis alter Startpunkt
		// xfirst ist geklickter Punkt , getStX alter Start
		offX = getStartX() - xFirst;
		offY = getStartY() - yFirst;
		// Länge der Linie
		offXEnd = getEndX() - getStartX();
		offYEnd = getEndY() - getStartY();

	}

	public void move(double xFirst, double yFirst) {
		// setzen neuer Startpunkt
		// xFirst ist Mauspos
		setStartX(xFirst + offX);
		setStartY(yFirst + offY);
		// Startpunkt + länge der Linie ist Endpunkt
		setEndX(getStartX() + offXEnd);
		setEndY(getStartY() + offYEnd);
	}

	@Override
	public Shape copy() {
		Line copyLine = new Line(this.getStartX(), this.getStartY(),
				this.getEndX(), this.getEndY(), this.getFill());

		return copyLine;
	}

}
