import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.PrintStream;

import javax.swing.JPanel;

public class Entity {
  EnterableImage enterableImage;
  NonEnterableImage nonEnterableImage;
  Fight fight;
  CutSceneEntity cutScene;
  ItemPickUp itemPickUp;
  ItemCheck itemCheck;
  
  public boolean isEnterable = false;
  public boolean isInteractable = false;
  public Point gridCoords;
  protected ImagePanel image;
  public int type = -1;
  
  public Entity(Point gridCoords)
  {
	  this.gridCoords = gridCoords;
	  Point location = GamePanel.grid[gridCoords.x][gridCoords.y];
	  image = new ImagePanel(new Rectangle(location.x, location.y, MainClass.UNIT_SIZE, MainClass.UNIT_SIZE));
	  image.setImageAsCircle(Color.CYAN);
  }
  
  public boolean isEnterable() {
	  return isEnterable;
  }
  
  public void interact(GamePanel panel, Char aChar)
  {
	  
  }
  
  public String getString()
  {
    switch (this.type)
    {
    case 0: 
      return this.tree.getString();
    case 1: 
      return this.door.getString();
    case 2: 
      return this.path.getString();
    case 3: 
      return this.enterableImage.getString();
    case 4: 
      return this.nonEnterableImage.getString();
    case 5: 
      return this.fight.getString();
    case 6: 
      return this.cutScene.getString();
    case 7: 
      return this.itemPickUp.getString();
    case 8: 
      return this.itemCheck.getString();
    }
    return "ERROR";
  }
  public void draw(Graphics g) {
	  image.draw(g);
  }
  public Point getLocation() {
	  return new Point(gridCoords.x, gridCoords.y);
  }
}
