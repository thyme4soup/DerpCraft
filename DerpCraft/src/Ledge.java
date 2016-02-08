import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JPanel;

public class Ledge
{
	Rectangle dim;
  public Ledge(Rectangle dimensions)
  {
	  dim = new Rectangle(dimensions.x, dimensions.y, dimensions.width, dimensions.height);
  }
  public void draw(Graphics g) {
	  g.setColor(Color.blue);
	  g.drawRect(dim.x, dim.y, dim.width, dim.height);
  }
  public int getX() {
	  return dim.x;
  }
  public int getY() {
	  return dim.y;
  }
  public int getWidth() {
	  return dim.width;
  }
  public int getHeight() {
	  return dim.height;
  }
}
