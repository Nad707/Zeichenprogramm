package de.hska.iwii.gui.drawing;

import javafx.scene.Node;



public class MyGroup extends javafx.scene.Group  implements de.hska.iwii.gui.drawing.Shape  {

	private boolean selected;
	
	//unnötig
	public MyGroup(){
		
	}

	@Override
	public void setSelected(boolean selected) {
		
		// Gehe alle Kinder durch und selektiere sie
       for (Node node : getChildren()) {
           ((Shape) node).setSelected(selected);
       }
	   
       this.selected = selected;
	}

	@Override
	public boolean isSelected() {
		return this.selected;
	}

	@Override
	public void resizeInteractive(double xFirst, double yFirst, double xNew,
			double yNew) {
		
		//TODO? offset(xNew - xFirst, yNew - yFirst)
	
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
	           System.out.println("dfd");
	       }
	}
}
