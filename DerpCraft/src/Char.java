import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JPanel;

public class Char
  extends JPanel
{
  int x;
  int y;
  int oldX;
  int oldY;
  int xCoord;
  int yCoord;
  int xDest;
  int yDest;
  int sideLength;
  Point health = new Point(100, 100);
  boolean animating;
  GamePanel instance;
  Map map;
  boolean dir;
  ImagePanel image;
  ArrayList<Point> inv = new ArrayList();
  Point[][] grid;
  String[][] spells;
  boolean interruptInteract = false;
  
  public Char(Point[][] aGrid, int x2, int y2, GamePanel game)
  {
    this.grid = aGrid;
    this.instance = game;
    this.map = ((Map)this.instance.maps.get(this.instance.currentMap));
    this.sideLength = this.grid[1][1].x;
    System.out.println(this.sideLength);
    this.x = x2;
    this.y = y2;
    setLayout(null);
    setSize(this.sideLength, this.sideLength);
    setLocation(this.grid[this.x][this.y]);
    this.image = new ImagePanel(getSize());
    System.out.println(getSize());
    this.image.setImageWithName("derp/derpwizleft.png");
    add(this.image);
    setVisible(true);
    setOpaque(false);
    this.dir = true;
    loadSpells();
  }
  
  public void moveLeft()
  {
    this.oldX = this.x;
    this.oldY = this.y;
    this.x -= 1;
    this.dir = true;
    updateImage();
    if ((checkEnterableSquare()) || (this.animating))
    {
      revert();
      return;
    }
    this.animating = true;
    this.yDest = this.y;
    this.xDest = this.x;
  }
  
  public void moveUp()
  {
    this.oldX = this.x;
    this.oldY = this.y;
    this.y -= 1;
    if ((checkEnterableSquare()) || (this.animating))
    {
      revert();
      return;
    }
    this.animating = true;
    this.yDest = this.y;
    this.xDest = this.x;
  }
  
  public void moveRight()
  {
    this.oldX = this.x;
    this.oldY = this.y;
    this.x += 1;
    this.dir = false;
    updateImage();
    if ((checkEnterableSquare()) || (this.animating))
    {
      revert();
      return;
    }
    this.animating = true;
    this.yDest = this.y;
    this.xDest = this.x;
  }
  
  public void moveDown()
  {
    this.oldX = this.x;
    this.oldY = this.y;
    this.y += 1;
    if ((checkEnterableSquare()) || (this.animating))
    {
      revert();
      return;
    }
    this.animating = true;
    this.yDest = this.y;
    this.xDest = this.x;
  }
  
  public void updateImage()
  {
    if (this.dir) {
      this.image.setImageWithName("derp/derpwizleft.png");
    } else {
      this.image.setImageWithName("derp/derpwizright.png");
    }
  }
  
  public void interactWithSquare()
  {
    ArrayList<Entity> eC = new ArrayList();
    for (int i = 0; i < this.map.mapEntities.size(); i++) {
      eC.add((Entity)this.map.mapEntities.get(i));
    }
    this.interruptInteract = false;
    for (int i = 0; (i < eC.size()) && (!this.interruptInteract); i++) {
      if (((Entity)eC.get(i)).getLocation().equals(getLocation())) {
        ((Entity)eC.get(i)).interact(this.instance, this);
      }
    }
    this.interruptInteract = false;
  }
  
  public boolean checkEnterableSquare()
  {
    if (this.x < 0) {
      return true;
    }
    if (this.y < 0) {
      return true;
    }
    if (this.x >= this.grid.length) {
      return true;
    }
    if (this.y >= this.grid.length) {
      return true;
    }
    if (checkNonEnterableObjects(this.x, this.y)) {
      return true;
    }
    return false;
  }
  
  public boolean checkNonEnterableObjects(int x, int y)
  {
    for (int i = 0; i < this.map.mapEntities.size(); i++) {
      if ((((Entity)this.map.mapEntities.get(i)).location.equals(new Point(x, y))) && (!((Entity)this.map.mapEntities.get(i)).isEnterable())) {
        return true;
      }
    }
    return false;
  }
  
  public void revert()
  {
    this.y = this.oldY;
    this.x = this.oldX;
  }
  
  public void updateBounds()
  {
    int inc = 15;
    if (this.xCoord < this.grid[this.xDest][0].x)
    {
      this.xCoord += inc;
    }
    else if (this.xCoord > this.grid[this.xDest][0].x)
    {
      this.xCoord -= inc;
    }
    else if (this.yCoord < this.grid[0][this.yDest].y)
    {
      this.yCoord += inc;
    }
    else if (this.yCoord > this.grid[0][this.yDest].y)
    {
      this.yCoord -= inc;
    }
    else if (this.animating)
    {
      this.animating = false;
      interactWithSquare();
    }
    if (Math.abs(Math.abs(this.xCoord) - Math.abs(this.grid[this.xDest][0].x)) < inc) {
      this.xCoord = this.grid[this.xDest][0].x;
    }
    if (Math.abs(Math.abs(this.yCoord) - Math.abs(this.grid[0][this.yDest].y)) < inc) {
      this.yCoord = this.grid[0][this.yDest].y;
    }
    setLocation(this.xCoord, this.yCoord);
  }
  
  public void changeLocation(Point gridCoords)
  {
    this.animating = false;
    this.x = gridCoords.x;
    this.y = gridCoords.y;
    this.xDest = this.x;
    this.yDest = this.y;
    this.xCoord = this.grid[this.x][0].x;
    this.yCoord = this.grid[0][this.y].y;
  }
  
  public void give(Point item)
  {
    for (int i = 0; i < this.inv.size(); i++) {
      if (((Point)this.inv.get(i)).x == item.x)
      {
        ((Point)this.inv.get(i)).y += item.y;
        return;
      }
    }
    this.inv.add(item);
  }
  
  public void loadSpells()
  {
    BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("resources/spells")));
    ArrayList<String> spellContents = new ArrayList();
    String line = null;
    try
    {
      while ((line = reader.readLine()) != null) {
        spellContents.add(line);
      }
    }
    catch (IOException e)
    {
      System.out.println("Well that wasn't supposed to happen");
    }
    this.spells = new String[spellContents.size()][2];
    for (int i = 1; i < spellContents.size(); i++)
    {
      this.spells[i][0] = ((String)spellContents.get(i));
      this.spells[i][1] = loadSequence(this.spells[i][0]);
    }
  }
  
  public String loadSequence(String spellFile)
  {
    System.out.println(spellFile);
    BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("resources/" + spellFile)));
    ArrayList<String> spellContents = new ArrayList();
    String line = null;
    try
    {
      while ((line = reader.readLine()) != null) {
        spellContents.add(line);
      }
    }
    catch (IOException e)
    {
      System.out.println("Well that wasn't supposed to happen");
    }
    return (String)spellContents.get(0);
  }
}
