import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

public class GamePanel extends Canvas implements KeyListener	{
  public static Point[][] grid = new Point[MainClass.GRID_LENGTH][MainClass.GRID_LENGTH];
  public Char pChar;
  private BufferedImage back;
  String saveName;
  public HashMap<String, Map> maps = new HashMap();
  public Map currentMap;
  ArrayList<String> mapsKeyList = new ArrayList();
  
  public GamePanel(String saveName)
  {
    setBackground(Color.DARK_GRAY);
    setPreferredSize(new Dimension(690, 690));
    initGrid();
    this.saveName = saveName;
    currentMap = new Map("firstMap.txt", this.grid, this.saveName);
    this.maps.put(currentMap.mapName, currentMap);
    this.mapsKeyList.add(currentMap.mapName);
    this.pChar = new Char(this.grid, 0, 0, this);
    addKeyListener(this);
    setFocusable(true);
    setVisible(true);
  }
  
  public void changeMap(String name)
  {
    if (this.maps.containsKey(name))
    {
      currentMap = ((Map)this.maps.get(this.currentMap));
      this.pChar.instance = this;
      this.pChar.map = currentMap;
      return;
    }
    this.maps.put(name, new Map(name, grid, this.saveName));
    this.mapsKeyList.add(name);
    this.pChar.instance = this;
    this.pChar.map = currentMap;
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
  public void update(Graphics graphics) {
	  Graphics2D twoDGraph = (Graphics2D)graphics;
	  if(back==null)
		  back = (BufferedImage)(createImage(getWidth(),getHeight()));
	  Graphics g = back.createGraphics();
	  //todo: test for in-battle? not drawing the map while playing would give higher performance
	  currentMap.draw(g);
	  pChar.draw(g);
	  
	  twoDGraph.drawImage(back, null, 0, 0);
  }
  
  public void initGrid()
  {
    int x = 700;
    int increment = x / grid.length;
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++)
      {
        grid[i][j] = new Point();
        grid[i][j].x = (increment * i);
        grid[i][j].y = (increment * j);
      }
    }
  }
  
  public void initGrid(int sideLength)
  {
    int increment = sideLength / grid.length;
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++)
      {
        grid[i][j] = new Point();
        grid[i][j].x = (increment * i);
        grid[i][j].y = (increment * j);
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