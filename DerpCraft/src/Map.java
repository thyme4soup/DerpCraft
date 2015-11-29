import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Map
  extends JPanel
{
  public ImagePanel background;
  public Point[][] grid;
  public String mapName;
  String backgroundLine;
  String saveName;
  ArrayList<Entity> mapEntities = new ArrayList();
  
  public Map(String mapName, Point[][] aGrid, String saveName)
  {
    setBackground(Color.BLUE);
    setLayout(null);
    setSize(new Dimension(700, 700));
    setVisible(true);
    this.grid = aGrid;
    this.background = new ImagePanel(getSize());
    this.saveName = saveName;
    this.background.setImageAsSquare(Color.DARK_GRAY);
    this.mapName = mapName;
    loadMap(this.mapName);
    add(this.background);
  }
  
  public void loadMap(String map)
  {
    ArrayList<String> mapContents = new ArrayList();
    try
    {
      BufferedReader reader = new BufferedReader(new FileReader(new File(System.getProperty("user.home") + "/AppData/Roaming/.DerpCraft/" + this.saveName + "/" + map)));
      String line = null;
      while ((line = reader.readLine()) != null) {
        mapContents.add(line);
      }
    }
    catch (IOException e)
    {
      System.out.println("Undiscovered map found, loading default map");
      BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("resources/maps/" + map)));
      String line = null;
      try
      {
        while ((line = reader.readLine()) != null) {
          mapContents.add(line);
        }
      }
      catch (IOException e1)
      {
        System.out.println("error reading thingy");
      }
    }
    this.backgroundLine = ((String)mapContents.get(0));
    setupBackground(this.backgroundLine);
    
    String[] c = new String[mapContents.size()];
    for (int i = 0; i < mapContents.size(); i++) {
      c[i] = ((String)mapContents.get(i));
    }
    loadEntities(c);
    System.out.println("Succesfully loaded: " + map);
  }
  
  public void loadEntities(String[] c)
  {
    String temp = "";
    for (int i = 1; i < c.length; i++)
    {
      int charHolder = 0;
      temp = "";
      int n = 0;
      while (c[i].charAt(charHolder) != ';')
      {
        temp = temp + c[i].charAt(charHolder);
        charHolder++;
      }
      n = Integer.valueOf(temp).intValue();
      switch (n)
      {
      case 0: 
        loadTree(c[i]);
        break;
      case 1: 
        loadDoor(c[i]);
        break;
      case 2: 
        loadPath(c[i]);
        break;
      case 3: 
        loadEnterableImage(c[i]);
        break;
      case 4: 
        loadNonEnterableImage(c[i]);
        break;
      case 5: 
        loadFight(c[i]);
        break;
      case 6: 
        loadCutscene(c[i]);
        break;
      case 7: 
        loadItemPickUp(c[i]);
        break;
      case 8: 
        loadItemCheck(c[i]);
      }
    }
  }
  
  public void loadTree(String c)
  {
    Entity temp = Tree.loadEntity(c, this.grid);
    add(temp);
    this.mapEntities.add(temp);
  }
  
  public void loadDoor(String c)
  {
    Entity temp = Door.loadEntity(c, this.grid);
    add(temp);
    this.mapEntities.add(temp);
  }
  
  public void loadPath(String c)
  {
    Entity temp = Path.loadEntity(c, this.grid);
    add(temp);
    this.mapEntities.add(temp);
  }
  
  public void loadEnterableImage(String c)
  {
    Entity temp = EnterableImage.loadEntity(c, this.grid);
    add(temp);
    this.mapEntities.add(temp);
  }
  
  public void loadNonEnterableImage(String c)
  {
    Entity temp = NonEnterableImage.loadEntity(c, this.grid);
    add(temp);
    this.mapEntities.add(temp);
  }
  
  public void loadFight(String c)
  {
    Entity temp = Fight.loadEntity(c, this.grid);
    add(temp);
    this.mapEntities.add(temp);
  }
  
  public void loadCutscene(String c)
  {
    Entity temp = CutSceneEntity.loadEntity(c, this.grid);
    add(temp);
    this.mapEntities.add(temp);
  }
  
  public void loadItemPickUp(String c)
  {
    Entity temp = ItemPickUp.loadEntity(c, this.grid);
    add(temp);
    this.mapEntities.add(temp);
  }
  
  public void loadItemCheck(String c)
  {
    Entity temp = ItemCheck.loadEntity(c, this.grid);
    add(temp);
    this.mapEntities.add(temp);
  }
  
  public void setupBackground(String mapBackground)
  {
    System.out.println(mapBackground);
    int n = 0;
    while (mapBackground.charAt(n) != ';') {
      n++;
    }
    mapBackground = mapBackground.substring(0, n);
    if (mapBackground.contains("file: "))
    {
      mapBackground = mapBackground.substring(6, mapBackground.length());
      this.background.setImageWithName(mapBackground);
      System.out.println(mapBackground);
    }
    else
    {
      int[] color = new int[3];
      int j = 0;
      for (int i = 0; i < color.length; i++)
      {
        String temp = "";
        while ((mapBackground.charAt(j) != ' ') && (mapBackground.charAt(j) != ';'))
        {
          temp = temp + mapBackground.charAt(j);
          j++;
          if (j == mapBackground.length()) {
            break;
          }
        }
        j++;
        color[i] = Integer.valueOf(temp).intValue();
      }
      this.background.setImageAsSquare(new Color(color[0], color[1], color[2]));
    }
  }
  
  public void removeEntity(Entity ent)
  {
    this.mapEntities.remove(ent);
    remove(ent);
  }
  
  public String[] getMapContents()
  {
    ArrayList<String> temp = new ArrayList();
    temp.add(this.backgroundLine);
    for (int i = 0; i < this.mapEntities.size(); i++) {
      temp.add(((Entity)this.mapEntities.get(i)).getString());
    }
    String[] mapContents = new String[temp.size()];
    for (int i = 0; i < temp.size(); i++) {
      mapContents[i] = ((String)temp.get(i));
    }
    return mapContents;
  }
}
