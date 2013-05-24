package de.hska.iwii.gui.drawing;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Rectangle extends javafx.scene.shape.Rectangle implements de.hska.iwii.gui.drawing.Shape  {
	
	private boolean selected;
	double offX;
	double offY;
	
	public Rectangle(double x, double y, double width, double height, Paint color){
		//übernehme Attribute
		super(x,y, width, height);
		
		setFill(color);
		setStroke(color);
		this.selected = false;
		
	}
	
	public Shape clone(){
		Rectangle cloneRect = new Rectangle(this.getX() , this.getY(), this.getWidth(), this.getHeight(), this.getFill());
		
		return cloneRect;
	}
	
	public void resizeInteractive( double xFirst, double yFirst, double xNew, double yNew ){
	
		double newWidth = Math.abs(xFirst - xNew);
		double newHeigh = Math.abs(yFirst- yNew);
		
		//wir machen nach links gehen
		if(xNew < xFirst){
			setX(xNew);
		}
		
		//wenn wir nach oben gehen 
		if(yNew < yFirst){
			setY(yNew);
		}
		
		setWidth(newWidth);
		setHeight(newHeigh);
		
	}	
	public void setSelected(boolean selected){
		this.selected = selected;
		this.setStroke(Color.rgb(130, 0, 0));
		if(!selected){
			this.setStroke(Color.rgb(255, 0, 255, 0.5));
		}
	}
	
	public boolean isSelected(){
		return this.selected;
	}
	
	public void offset(double xFirst, double yFirst){
		offX = getX()-xFirst;
		offY = getY()-yFirst;
	}
	public void move(double xFirst, double yFirst){
		setX(xFirst+offX);
		setY(yFirst+offY);
	}

	@Override
	public Shape copy() {
		Rectangle copyRect = new Rectangle(this.getX()+15 , this.getY()+15, this.getWidth(), this.getHeight(), this.getFill());
		
		return (Shape)copyRect;
	}

	
	
	
}
