import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.PrintStream;
import javax.swing.JPanel;

public class Door
  extends JPanel
{
  public boolean isEnterable = true;
  public boolean interact = true;
  public String toMap;
  public Point[][] grid;
  public Point gridDestination;
  public Point gridCoords;
  
  public Door(Dimension size, String toMapName, Point location, Point[][] aGrid)
  {
    setLayout(null);
    setSize(size);
    setVisible(true);
    setOpaque(false);
    this.gridCoords = location;
    this.toMap = toMapName;
    this.grid = aGrid;
    ImagePanel image = new ImagePanel(getSize());
    image.setImageAsText("Door", Color.white);
    add(image);
  }
  
  public static Entity loadEntity(String c, Point[][] grid)
  {
    String temp = "";
    int n = 0;
    int destX = 0;
    int destY = 0;
    int[] coords = MainClass.loadCoords(c);
    n = coords[2];
    while (c.charAt(n) != ';')
    {
      temp = temp + c.charAt(n);
      n++;
    }
    n += 2;
    destX = Integer.valueOf(temp).intValue();
    temp = "";
    while (c.charAt(n) != ';')
    {
      temp = temp + c.charAt(n);
      n++;
    }
    n += 2;
    destY = Integer.valueOf(temp).intValue();
    temp = "";
    while (c.charAt(n) != ';')
    {
      temp = temp + c.charAt(n);
      n++;
    }
    n += 2;
    
    Entity tempDoor = new Entity(new Point(coords[0], coords[1]), 1, grid);
    tempDoor.door.toMap = temp;
    tempDoor.door.gridDestination = new Point(destX, destY);
    return tempDoor;
  }
  
  public void interact(GamePanel panel, Char aChar)
  {
    aChar.changeLocation(this.gridDestination);
    panel.changeMap(this.toMap);
    System.out.println("Went through door");
  }
  
  public String getString()
  {
    String temp = "";
    temp = String.format("%d; %d; %d; %d; %d; %s;", new Object[] { Integer.valueOf(1), Integer.valueOf(this.gridCoords.x), Integer.valueOf(this.gridCoords.y), Integer.valueOf(this.gridDestination.x), Integer.valueOf(this.gridDestination.y), this.toMap });
    return temp;
  }
}
