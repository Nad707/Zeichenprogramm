package de.hska.iwii.gui.drawing;

public interface Shape {

		public void setSelected(boolean selected);
		
		public boolean isSelected();
		
		public void resizeInteractive(double xFirst, double yFirst, double xNew, double yNew );
		
		public void offset(double xFirst, double yFirst);
		
		public void move(double xFirst, double yFirst);
		
		public Shape copy();
		
}
