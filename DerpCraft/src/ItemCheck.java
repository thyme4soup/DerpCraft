import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JPanel;

public class ItemCheck
  extends JPanel
{
  public boolean isEnterable = true;
  ImagePanel image;
  String imageName;
  Point gridCoords;
  int takeItem;
  Point item;
  Entity ent;
  
  public ItemCheck(Dimension size, Point gridCoords, Entity ent)
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
    temp = "";
    n += 2;
    while (c.charAt(n) != ';')
    {
      temp = temp + c.charAt(n);
      n++;
    }
    int takeItem = Integer.valueOf(temp).intValue();
    
    Entity tempItem = new Entity(new Point(coords[0], coords[1]), 8, grid);
    tempItem.itemCheck.item = new Point(itemID, itemNumber);
    tempItem.itemCheck.setImage(imageName);
    tempItem.itemCheck.imageName = imageName;
    tempItem.itemCheck.takeItem = takeItem;
    return tempItem;
  }
  
  public void interact(GamePanel instance)
  {
    for (int i = 0; i < instance.pChar.inv.size(); i++) {
      if ((((Point)instance.pChar.inv.get(i)).x == this.item.x) && 
        (((Point)instance.pChar.inv.get(i)).y >= this.item.y))
      {
        ((Point)instance.pChar.inv.get(i)).y -= this.item.y;
        ((Map)instance.maps.get(instance.currentMap)).removeEntity(this.ent);
        return;
      }
    }
    instance.pChar.interruptInteract = true;
  }
  
  public void setImage(String imageName)
  {
    this.image.setImageWithName(imageName);
  }
  
  public String getString()
  {
    String temp = "";
    temp = String.format("%d; %d; %d; %d; %d; %s; %d", new Object[] { Integer.valueOf(8), Integer.valueOf(this.gridCoords.x), Integer.valueOf(this.gridCoords.y), Integer.valueOf(this.item.x), Integer.valueOf(this.item.y), this.imageName, Integer.valueOf(this.takeItem) });
    return temp;
  }
}
