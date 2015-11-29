import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.HashMap;
import javax.swing.JPanel;

public class CutSceneEntity
  extends JPanel
{
  Point gridCoords;
  String cutsceneName;
  int remove;
  Entity ent;
  boolean isEnterable = true;
  
  public CutSceneEntity(Dimension size, Point location, Entity ent)
  {
    setLayout(null);
    setSize(size);
    setVisible(true);
    setOpaque(false);
    this.gridCoords = location;
    this.ent = ent;
    ImagePanel image = new ImagePanel(getSize());
    image.setImageAsText("Cutscene", Color.white);
    add(image);
  }
  
  public static Entity loadEntity(String c, Point[][] grid)
  {
    String temp = "";
    int n = 0;
    int[] coords = MainClass.loadCoords(c);
    n = coords[2];
    String cS = "";
    while (c.charAt(n) != ';')
    {
      cS = cS + c.charAt(n);
      n++;
    }
    n += 2;
    temp = "";
    if (n < c.length()) {
      while (c.charAt(n) != ';')
      {
        temp = temp + c.charAt(n);
        n++;
      }
    }
    if (temp.equals("")) {
      temp = "0";
    }
    int remove = Integer.valueOf(temp).intValue();
    
    Entity tempCS = new Entity(new Point(coords[0], coords[1]), 6, grid);
    tempCS.cutScene.cutsceneName = cS;
    tempCS.cutScene.remove = remove;
    return tempCS;
  }
  
  public void interact(GamePanel instance)
  {
    Cutscene sc = new Cutscene(instance, this.cutsceneName);
    instance.add(sc);
    sc.startLoop();
    instance.remove(sc);
    instance.requestFocus();
    if (this.remove == 1) {
      ((Map)instance.maps.get(instance.currentMap)).removeEntity(this.ent);
    }
  }
  
  public String getString()
  {
    String temp = "";
    temp = String.format("%d; %d; %d; %s; %d", new Object[] { Integer.valueOf(6), Integer.valueOf(this.gridCoords.x), Integer.valueOf(this.gridCoords.y), this.cutsceneName, Integer.valueOf(this.remove) });
    return temp;
  }
}
