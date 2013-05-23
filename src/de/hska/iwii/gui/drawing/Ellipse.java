package de.hska.iwii.gui.drawing;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Ellipse  extends javafx.scene.shape.Ellipse implements  de.hska.iwii.gui.drawing.Shape {

	private boolean selected;
	double offX;
	double offY;
	
	public Ellipse(double centerX, double centerY, double radiusX, double radiusY, Paint color){
		//übernehme Attribute
		
		
		//Warum super ??????????????????????????????????????????????????????????????????????????
		super(centerX, centerY,  radiusX,  radiusY);
		
		setFill(color);
		setStroke(color);
		this.selected = false;
	}

	@Override
	public void resizeInteractive(double xFirst, double yFirst, double xNew,
			double yNew) {
		
		
		double newRadiusX = Math.abs(xFirst - xNew);
		double newRadiusY = Math.abs(yFirst- yNew);
		
		setRadiusX(newRadiusX);
		setRadiusY(newRadiusY);
		
	}

	@Override
	public Shape clone() throws CloneNotSupportedException {
		Ellipse cloneEllipse = new Ellipse(this.getCenterX() , this.getCenterY(), this.getRadiusX(), this.getRadiusY(), this.getFill());
		
		return cloneEllipse;
	}
	
	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
		
		if(selected){
			this.setStroke(Color.rgb(130, 0, 0));
		}
		
		
		if(!selected){
			System.out.println("Deselect ellipse");
			this.setStroke(Color.rgb(205,41,144, 0.5));
		}
		
	}

	@Override
	public boolean isSelected() {
		return this.selected;
	}
	/**
	 * Vom geklickten Punkt(xFirst, YFirst) zum Mittelpunkt
	 * 
	 */
	public void offset(double xFirst, double yFirst){
		offX = getCenterX()-xFirst;
		offY = getCenterY()-yFirst;
	}
	public void move(double xFirst, double yFirst){
		setCenterX(xFirst + offX);
		setCenterY(yFirst + offY);
	}

	@Override
	public Shape copy() {
		Ellipse copyEllipse = new Ellipse(this.getCenterX() , this.getCenterY(), this.getRadiusX(), this.getRadiusY(), this.getFill());
		
		return copyEllipse;
	}

}
