package de.hska.iwii.gui.drawing;

import javafx.scene.Node;



public class Group extends javafx.scene.Group implements Shape {

	private boolean selected;
	
	//unnötig
	public Group(Node... children){
		
	}

	@Override
	public void setSelected(boolean selected) {
		
		// Gehe alle Kinder durch und selektiere sie
       for (Node node: getChildren()) {
           ((Shape) node).setSelected(selected);
       }
	   
	   selected = true;
		
	}

	@Override
	public boolean isSelected() {
		return this.selected;
	}

	@Override
	public void resizeInteractive(double xFirst, double yFirst, double xNew,
			double yNew) {
		
		
	}

	@Override
	public void offset(double xFirst, double yFirst) {
		 for (Node node: getChildren()) {
	           ((Shape) node).offset(xFirst, yFirst);
	       }
	
	}

	@Override
	public void move(double xFirst, double yFirst) {
		for (Node node: getChildren()) {
	           ((Shape) node).move(xFirst, yFirst);
	       }
	}
	
	
}
