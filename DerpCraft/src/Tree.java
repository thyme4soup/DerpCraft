import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JPanel;

public class Tree
  extends Entity
{
	public static final int TYPE = 0;
  
  public Tree(Point gridCoords)
  {
	  super(gridCoords);
	  type = 0;
	  isEnterable = false;
	  isInteractable = false;
	  image.setImageWithName("treegreen.png");
  }
  
  public static Tree loadEntity(String c)
  {
    int[] coords = MainClass.loadCoords(c);
    return new Tree(new Point(coords[0], coords[1]));
  }
  
  @Override
  public String getString()
  {
    String temp = "";
    temp = String.format("%d; %d; %d;", new Object[] { Integer.valueOf(0), Integer.valueOf(this.gridCoords.x), Integer.valueOf(this.gridCoords.y) });
    return temp;
  }
}
