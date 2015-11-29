import java.awt.Dimension;
import java.awt.Point;
import java.io.PrintStream;
import javax.swing.JPanel;

public class Entity
  extends JPanel
{
  Tree tree;
  Door door;
  Path path;
  EnterableImage enterableImage;
  NonEnterableImage nonEnterableImage;
  Fight fight;
  CutSceneEntity cutScene;
  ItemPickUp itemPickUp;
  ItemCheck itemCheck;
  Point location;
  Point[][] grid;
  int type;
  
  public Entity(Point gridCoords, int aType, Point[][] aGrid)
  {
    setLayout(null);
    this.grid = aGrid;
    setSize(new Dimension(this.grid[1][1].x, this.grid[1][1].y));
    setVisible(true);
    this.location = gridCoords;
    setLocation(this.grid[this.location.x][this.location.y]);
    setOpaque(false);
    this.type = aType;
    switch (this.type)
    {
    case 0: 
      this.tree = new Tree(getSize(), this.location);
      add(this.tree);
      break;
    case 1: 
      this.door = new Door(getSize(), "", this.location, this.grid);
      add(this.door);
      break;
    case 2: 
      this.path = new Path(getSize(), this.location);
      add(this.path);
      break;
    case 3: 
      this.enterableImage = new EnterableImage(getSize(), this.location);
      add(this.enterableImage);
      break;
    case 4: 
      this.nonEnterableImage = new NonEnterableImage(getSize(), this.location);
      add(this.nonEnterableImage);
      break;
    case 5: 
      this.fight = new Fight(getSize(), this.location, this);
      add(this.fight);
      break;
    case 6: 
      this.cutScene = new CutSceneEntity(getSize(), this.location, this);
      add(this.cutScene);
      break;
    case 7: 
      this.itemPickUp = new ItemPickUp(getSize(), this.location, this);
      add(this.itemPickUp);
      break;
    case 8: 
      this.itemCheck = new ItemCheck(getSize(), this.location, this);
      add(this.itemCheck);
      break;
    default: 
      System.out.println("Unknown type: " + this.type);
    }
  }
  
  public boolean isEnterable()
  {
    switch (this.type)
    {
    case 0: 
      return this.tree.isEnterable;
    case 1: 
      return this.door.isEnterable;
    case 2: 
      return this.path.isEnterable;
    case 3: 
      return this.enterableImage.isEnterable;
    case 4: 
      return this.nonEnterableImage.isEnterable;
    case 5: 
      return this.fight.isEnterable;
    case 6: 
      return this.cutScene.isEnterable;
    case 7: 
      return this.itemPickUp.isEnterable;
    case 8: 
      return this.itemCheck.isEnterable;
    }
    return true;
  }
  
  public void interact(GamePanel panel, Char aChar)
  {
    switch (this.type)
    {
    case 0: 
      this.tree.interact();
      break;
    case 1: 
      this.door.interact(panel, aChar);
      break;
    case 2: 
      this.path.interact();
      break;
    case 3: 
      this.enterableImage.interact();
      break;
    case 4: 
      this.nonEnterableImage.interact();
      break;
    case 5: 
      this.fight.interact(panel);
      break;
    case 6: 
      this.cutScene.interact(panel);
      break;
    case 7: 
      this.itemPickUp.interact(panel);
      break;
    case 8: 
      this.itemCheck.interact(panel);
      break;
    }
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
}
