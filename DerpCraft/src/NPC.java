import java.awt.Dimension;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;

public class NPC
  extends JPanel
{
  Point coords = new Point(500, 650);
  Point velocity = new Point();
  int id = -1;
  boolean dead = false;
  boolean casted = true;
  boolean avoiding = false;
  int cCounter = 0;
  boolean vS;
  double gravity = 15.0D;
  double airSpeed = 500.0D;
  int castTime = 3000;
  ArrayList<Integer> castingSequence = new ArrayList();
  ArrayList<Condition> conditions = new ArrayList();
  int frameRate;
  boolean l;
  boolean r;
  boolean drop;
  BattleChar bC;
  Point health;
  int timeCasting;
  int distance = 200;
  Ledge[] ledges;
  boolean jumped;
  Dimension container;
  FightPanel panel;
  ImagePanel image;
  String imageLocation = "ManLeft.png";
  int gCounter = 0;
  int aCounter = 0;
  int dir = 1;
  int delay = 0;
  ArrayList<String> spells = new ArrayList();
  
  public NPC(Dimension aContainer, int aFR, Ledge[] aLedges, FightPanel aPanel, BattleChar aBC, String npc)
  {
    setLayout(null);
    setSize(50, 50);
    this.bC = aBC;
    this.health = new Point(100, 100);
    this.ledges = aLedges;
    this.panel = aPanel;
    this.vS = true;
    this.container = aContainer;
    this.jumped = false;
    loadInfo(npc);
    this.velocity.x = 0;
    this.velocity.y = 0;
    this.frameRate = 10;
    setLocation(this.coords.x, this.coords.y);
    this.image = new ImagePanel(getSize());
    this.image.setImageWithName(this.imageLocation);
    turn();
    add(this.image);
    setVisible(true);
    setOpaque(false);
  }
  
  public void loadInfo(String fileLocation)
  {
    BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("resources/" + fileLocation)));
    ArrayList<String> f = new ArrayList();
    String line = null;
    try
    {
      while ((line = reader.readLine()) != null) {
        f.add(line);
      }
    }
    catch (IOException e)
    {
      System.out.println("Well that wasn't supposed to happen");
    }
    int n = 0;
    String[] temp = getNext(n, (String)f.get(0), ';');
    this.imageLocation = temp[0];
    n = Integer.valueOf(temp[1]).intValue();
    Point temp2 = new Point();
    temp = getNext(n, (String)f.get(0), ';');
    temp2.x = Integer.valueOf(temp[0]).intValue();
    n = Integer.valueOf(temp[1]).intValue();
    temp = getNext(n, (String)f.get(0), ';');
    temp2.y = Integer.valueOf(temp[0]).intValue();
    n = Integer.valueOf(temp[1]).intValue();
    setSize(temp2.x, temp2.y);
    this.id = Integer.valueOf(getNext(n, (String)f.get(0), ';')[0]).intValue();
    System.out.println(this.id);
    n = 0;
    temp = getNext(n, (String)f.get(1), ';');
    this.coords.x = Integer.valueOf(temp[0]).intValue();
    n = Integer.valueOf(temp[1]).intValue();
    this.coords.y = Integer.valueOf(getNext(n, (String)f.get(1), ';')[0]).intValue();
    n = 0;
    this.health.y = Integer.valueOf(getNext(n, (String)f.get(2), ';')[0]).intValue();
    this.health.x = this.health.y;
    int i = 4;
    while (i < f.size())
    {
      this.spells.add((String)f.get(i));
      i++;
    }
  }
  
  public String[] getNext(int n, String c, char stopChar)
  {
    String[] temp = new String[2];
    temp[0] = "";
    temp[1] = "";
    while ((n < c.length()) && (c.charAt(n) != stopChar))
    {
      int tmp26_25 = 0; String[] tmp26_23 = temp;tmp26_23[tmp26_25] = (tmp26_23[tmp26_25] + c.charAt(n));
      n++;
    }
    n += 2;
    temp[1] = ""+n;
    return temp;
  }
  
  public void updateLifeStatus()
  {
    if ((!this.dead) && (this.health.x < 1))
    {
      this.velocity.y = -5;
      this.velocity.x = (new Random().nextInt(8) - 4);
      this.dead = true;
    }
  }
  
  public void applyDecisions()
  {
    if (!avoidOtherNPCs())
    {
      if (this.bC.dead) {
        this.distance = 0;
      }
      if (this.bC.getX() < getX() - this.distance)
      {
        this.l = true;
        turn();
      }
      else if ((this.bC.getX() - getX() < this.distance - 50) && (this.bC.getX() - getX() > 0) && (!this.bC.dead))
      {
        this.l = true;
        turn();
      }
      else
      {
        this.l = false;
      }
      if (this.bC.getX() > getX() + this.distance)
      {
        this.r = true;
        turn();
      }
      else if ((this.bC.getX() - getX() > -this.distance + 50) && (this.bC.getX() - getX() < 0) && (!this.bC.dead))
      {
        this.r = true;
        turn();
      }
      else
      {
        this.r = false;
      }
      if (this.bC.getY() < getY() - 250) {
        jump();
      } else if (this.bC.getY() > getY() + 100) {
        this.drop = true;
      } else {
        this.drop = false;
      }
    }
    if ((Math.abs(this.bC.getY() - getY()) < 50) && (!this.casted) && (this.velocity.y <= 0))
    {
      castSpell();
      this.casted = true;
    }
  }
  
  public boolean avoidOtherNPCs()
  {
    for (int i = 0; i < this.panel.npcs.size(); i++)
    {
      NPC temp = (NPC)this.panel.npcs.get(i);
      if ((!temp.equals(this)) && (Math.abs(temp.getX() - getX()) < 50))
      {
        if (!temp.avoiding)
        {
          if (getX() - temp.getX() < 0)
          {
            this.l = true;
            turn();
          }
          else
          {
            this.r = true;
            turn();
          }
          this.avoiding = true;
          return true;
        }
        this.avoiding = false;
      }
    }
    return false;
  }
  
  public void update()
  {
    if ((this.cCounter < this.castTime) && (this.casted))
    {
      this.cCounter += this.frameRate;
    }
    else if (this.casted)
    {
      this.casted = false;
      this.cCounter = 0;
    }
    for (int i = 0; i < this.conditions.size(); i++)
    {
      ((Condition)this.conditions.get(i)).apply(this);
      if (((Condition)this.conditions.get(i)).finished)
      {
        this.conditions.remove(this.conditions.get(i));
        if (i > 0) {
          i--;
        }
      }
    }
    if ((!this.dead) && (this.delay >= 200))
    {
      applyDecisions();
      this.delay = 0;
    }
    this.delay += this.frameRate;
    applyVelocity();
  }
  
  public void castSpell()
  {
    Spell spell = new Spell(getLocation(), this.dir, this.id, this.panel, (String)this.spells.get(new Random().nextInt(this.spells.size())), this.frameRate);
    this.panel.castedSpells.add(spell);
    this.panel.add(spell);
  }
  
  public void applyVelocity()
  {
    Point newCoords = new Point();
    newCoords.x = this.coords.x;
    newCoords.y = this.coords.y;
    newCoords.x += this.velocity.x;
    newCoords.y += this.velocity.y;
    updateVelocity();
    if (!this.dead) {
      newCoords = updateVS(newCoords, this.drop);
    }
    this.coords.y = newCoords.y;
    this.coords.x = newCoords.x;
    updateLocation();
  }
  
  public void jump()
  {
    if (!this.jumped)
    {
      if (this.vS) {
        this.velocity.y = -10;
      } else {
        this.velocity.y = -8;
      }
      this.vS = false;
      this.jumped = true;
    }
  }
  
  public void updateVelocity()
  {
    if ((this.vS) && (!this.l) && (!this.r))
    {
      if (this.velocity.x < 0) {
        this.velocity.x += 1;
      } else if (this.velocity.x > 0) {
        this.velocity.x -= 1;
      }
    }
    else if (this.vS)
    {
      if ((this.velocity.x > -2) && (this.l)) {
        this.velocity.x -= 1;
      }
      if ((this.velocity.x < 2) && (this.r)) {
        this.velocity.x += 1;
      }
    }
    else
    {
      if (this.gCounter < 1000.0D / this.gravity / this.frameRate)
      {
        this.gCounter += 1;
      }
      else
      {
        this.velocity.y += 1;
        this.gCounter = 0;
      }
      if (this.aCounter < 1000.0D / this.airSpeed / this.frameRate)
      {
        this.aCounter += 1;
      }
      else
      {
        if ((this.velocity.x > -1) && (this.l)) {
          this.velocity.x -= 1;
        }
        if ((this.velocity.x < 1) && (this.r)) {
          this.velocity.x += 1;
        }
        this.aCounter = 0;
      }
    }
    this.vS = false;
  }
  
  public void turn()
  {
    if (this.bC.getX() < getX())
    {
      this.image.flip = false;
      this.image.setImageWithName(this.imageLocation);
      this.dir = -1;
    }
    else
    {
      this.image.flip = true;
      this.image.setImageWithName(this.imageLocation);
      this.dir = 1;
    }
  }
  
  public Point updateVS(Point newCoords, boolean drop)
  {
    for (int i = 0; (i < this.ledges.length) && (!drop) && (this.velocity.y >= 0); i++) {
      if ((newCoords.x + getWidth() > this.ledges[i].getX()) && (newCoords.x < this.ledges[i].getX() + this.ledges[i].getWidth()))
      {
        int tempY = newCoords.y;
        if ((this.velocity.y == 0) && 
          (newCoords.y == this.ledges[i].getY() - getHeight()))
        {
          this.velocity.y = 0;
          this.vS = true;
          this.jumped = false;
          return newCoords;
        }
        for (newCoords.y -= this.velocity.y; newCoords.y < tempY; newCoords.y += 1) {
          if (newCoords.y == this.ledges[i].getY() - getHeight())
          {
            this.velocity.y = 0;
            this.vS = true;
            this.jumped = false;
            return newCoords;
          }
        }
      }
    }
    if (drop) {
      this.vS = false;
    }
    if (newCoords.y >= this.container.height - getHeight())
    {
      newCoords.y = (this.container.height - getHeight());
      this.vS = true;
      this.jumped = false;
      this.velocity.y = 0;
    }
    if (newCoords.x < 0) {
      newCoords.x = 0;
    } else if (newCoords.x > this.container.width - getWidth()) {
      newCoords.x = (this.container.width - getWidth());
    }
    return newCoords;
  }
  
  public void updateLocation()
  {
    setLocation(this.coords.x, this.coords.y);
  }
}
