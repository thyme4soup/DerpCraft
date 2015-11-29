import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JPanel;

public class GamePanel
  extends JPanel
  implements KeyListener
{
  public Point[][] grid = new Point[20][20];
  public Char pChar;
  String currentMap;
  String saveName;
  public HashMap<String, Map> maps = new HashMap();
  ArrayList<String> mapsKeyList = new ArrayList();
  
  public GamePanel(String saveName)
  {
    setBackground(Color.DARK_GRAY);
    setLayout(null);
    setPreferredSize(new Dimension(690, 690));
    initGrid();
    this.saveName = saveName;
    this.currentMap = "firstMap.txt";
    Map temp = new Map(this.currentMap, this.grid, this.saveName);
    this.maps.put(temp.mapName, temp);
    this.mapsKeyList.add(temp.mapName);
    this.pChar = new Char(this.grid, 0, 0, this);
    addKeyListener(this);
    setFocusable(true);
    add(this.pChar);
    add((Component)this.maps.get(this.currentMap));
    setVisible(true);
  }
  
  public void changeMap(String name)
  {
    remove((Component)this.maps.get(this.currentMap));
    this.currentMap = name;
    if (this.maps.containsKey(name))
    {
      add((Component)this.maps.get(this.currentMap));
      this.pChar.instance = this;
      this.pChar.map = ((Map)this.maps.get(this.currentMap));
      return;
    }
    this.maps.put(this.currentMap, new Map(this.currentMap, this.grid, this.saveName));
    this.mapsKeyList.add(this.currentMap);
    add((Component)this.maps.get(this.currentMap));
    this.pChar.instance = this;
    this.pChar.map = ((Map)this.maps.get(this.currentMap));
  }
  
  public void startGameLoop()
  {
    requestFocus();
    for (;;)
    {
      try
      {
        Thread.sleep(50L);
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
      this.pChar.updateBounds();
      repaint();
      revalidate();
    }
  }
  
  public void initGrid()
  {
    int x = 700;
    int increment = x / this.grid.length;
    for (int i = 0; i < this.grid.length; i++) {
      for (int j = 0; j < this.grid[i].length; j++)
      {
        this.grid[i][j] = new Point();
        this.grid[i][j].x = (increment * i);
        this.grid[i][j].y = (increment * j);
      }
    }
  }
  
  public void initGrid(int sideLength)
  {
    int increment = sideLength / this.grid.length;
    for (int i = 0; i < this.grid.length; i++) {
      for (int j = 0; j < this.grid[i].length; j++)
      {
        this.grid[i][j] = new Point();
        this.grid[i][j].x = (increment * i);
        this.grid[i][j].y = (increment * j);
      }
    }
  }
  
  public void respawn()
  {
    changeMap("firstMap.txt");
    this.pChar.changeLocation(new Point(0, 0));
  }
  
  public void saveGame()
  {
    for (int i = 0; i < this.maps.size(); i++) {
      try
      {
        MainClass.saveFile(this.saveName, ((Map)this.maps.get(this.mapsKeyList.get(i))).getMapContents(), ((Map)this.maps.get(this.mapsKeyList.get(i))).mapName);
      }
      catch (IOException e)
      {
        System.out.println("Error saving map: " + ((Map)this.maps.get(this.mapsKeyList.get(i))).mapName);
        e.printStackTrace();
      }
    }
  }
  
  public void keyPressed(KeyEvent arg0)
  {
    switch (arg0.getKeyCode())
    {
    case 37: 
      this.pChar.moveLeft();
      break;
    case 38: 
      this.pChar.moveUp();
      break;
    case 39: 
      this.pChar.moveRight();
      break;
    case 40: 
      this.pChar.moveDown();
      break;
    case 27: 
      saveGame();
      break;
    default: 
      System.out.println(arg0.getKeyCode());
    }
  }
  
  public void keyReleased(KeyEvent arg0) {}
  
  public void keyTyped(KeyEvent arg0) {}
}