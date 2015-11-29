import java.awt.Dimension;
import java.awt.Point;
import java.util.HashMap;
import javax.swing.JPanel;

public class ItemPickUp
  extends JPanel
{
  public boolean isEnterable = true;
  ImagePanel image;
  String imageName;
  Point gridCoords;
  Point item;
  Entity ent;
  
  public ItemPickUp(Dimension size, Point gridCoords, Entity ent)
  {
    setLayout(null);
    setSize(size);
    setVisible(true);
    setOpaque(false);
    this.image = new ImagePanel(getSize());
    this.gridCoords = gridCoords;
    this.ent = ent;
    add(this.image);
  }
  
  public static Entity loadEntity(String c, Point[][] grid)
  {
    String imageName = "";
    int n = 0;
    int[] coords = MainClass.loadCoords(c);
    n = coords[2];
    String temp = "";
    while (c.charAt(n) != ';')
    {
      temp = temp + c.charAt(n);
      n++;
    }
    int itemID = Integer.valueOf(temp).intValue();
    temp = "";
    n += 2;
    while (c.charAt(n) != ';')
    {
      temp = temp + c.charAt(n);
      n++;
    }
    int itemNumber = Integer.valueOf(temp).intValue();
    temp = "";
    n += 2;
    while (c.charAt(n) != ';')
    {
      imageName = imageName + c.charAt(n);
      n++;
    }
    Entity tempItem = new Entity(new Point(coords[0], coords[1]), 7, grid);
    tempItem.itemPickUp.item = new Point(itemID, itemNumber);
    tempItem.itemPickUp.setImage(imageName);
    tempItem.itemPickUp.imageName = imageName;
    return tempItem;
  }
  
  public void interact(GamePanel instance)
  {
    ((Map)instance.maps.get(instance.currentMap)).removeEntity(this.ent);
    instance.pChar.give(this.item);
  }
  
  public void setImage(String imageName)
  {
    this.image.setImageWithName(imageName);
  }
  
  public String getString()
  {
    String temp = "";
    temp = String.format("%d; %d; %d; %d; %d; %s;", new Object[] { Integer.valueOf(7), Integer.valueOf(this.gridCoords.x), Integer.valueOf(this.gridCoords.y), Integer.valueOf(this.item.x), Integer.valueOf(this.item.y), this.imageName });
    return temp;
  }
}
