import java.awt.Dimension;
import java.awt.Point;
import java.util.HashMap;
import javax.swing.JPanel;

public class Fight
  extends JPanel
{
  public boolean isEnterable = true;
  ImagePanel image;
  String cutscene;
  String fight;
  String imageName;
  Point gridCoords;
  Entity ent;
  
  public Fight(Dimension size, Point gridCoords, Entity ent)
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
    String cutsceneName = "";
    String fightName = "";
    int n = 0;
    int[] coords = MainClass.loadCoords(c);
    n = coords[2];
    while (c.charAt(n) != ';')
    {
      imageName = imageName + c.charAt(n);
      n++;
    }
    n += 2;
    while (c.charAt(n) != ';')
    {
      cutsceneName = cutsceneName + c.charAt(n);
      n++;
    }
    n += 2;
    fightName = MainClass.getNext(n, c, ';')[0];
    
    Entity tempFight = new Entity(new Point(coords[0], coords[1]), 5, grid);
    tempFight.fight.setImage(imageName);
    tempFight.fight.imageName = imageName;
    tempFight.fight.cutscene = cutsceneName;
    tempFight.fight.fight = fightName;
    return tempFight;
  }
  
  public void interact(GamePanel instance)
  {
    Cutscene sc = new Cutscene(instance, this.cutscene);
    instance.add(sc);
    sc.startLoop();
    instance.remove(sc);
    instance.requestFocus();
    FightPanel fP = new FightPanel(instance, this.fight);
    instance.add(fP);
    fP.startLoop();
    if (!fP.win)
    {
      instance.pChar.interruptInteract = true;
      instance.respawn();
      instance.pChar.health.x = instance.pChar.health.y;
    }
    else
    {
      ((Map)instance.maps.get(instance.currentMap)).removeEntity(this.ent);
      instance.pChar.map = ((Map)instance.maps.get(instance.currentMap));
    }
    instance.remove(fP);
    instance.requestFocus();
  }
  
  public void setImage(String imageName)
  {
    this.image.setImageWithName(imageName);
  }
  
  public String getString()
  {
    String temp = "";
    temp = String.format("%d; %d; %d; %s; %s; %s;", new Object[] { Integer.valueOf(5), Integer.valueOf(this.gridCoords.x), Integer.valueOf(this.gridCoords.y), this.imageName, this.cutscene, this.fight });
    return temp;
  }
}