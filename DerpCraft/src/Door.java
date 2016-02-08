import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.PrintStream;
import javax.swing.JPanel;

public class Door extends Entity	{
	public static final int TYPE = 1;
  public String toMap;
  public Point gridDestination;
  
  public Door(Point gridCoords, String toMapName, Point toLocation)
  {
	 super(gridCoords);
	 isEnterable = true;
    this.toMap = toMapName;
    this.gridDestination = toLocation;
    image.setImageAsText("Door", Color.white);
  }
  
  public static Entity loadEntity(String c)
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
    
    Entity tempDoor = new Door(new Point(coords[0], coords[1]), temp, new Point(destX, destY));
    return tempDoor;
  }
  
  @Override
  public void interact(GamePanel panel, Char aChar)
  {
    aChar.changeLocation(this.gridDestination);
    panel.changeMap(this.toMap);
    System.out.println("Went through door");
  }
  
  public String getString()
  {
    String temp = "";
    temp = String.format("%d; %d; %d; %d; %d; %s;", new Object[] { Integer.valueOf(TYPE), Integer.valueOf(this.gridCoords.x), Integer.valueOf(this.gridCoords.y), Integer.valueOf(this.gridDestination.x), Integer.valueOf(this.gridDestination.y), this.toMap });
    return temp;
  }
}
