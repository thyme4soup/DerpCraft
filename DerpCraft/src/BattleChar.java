import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class BattleChar
{
  public static final int SIDE_LENGTH = 50;
  Point coords = new Point();
  Point velocity = new Point();
  int id = 0;
  boolean vS;
  double gravity = 15.0D;
  double airSpeed = 500.0D;
  boolean casting = false;
  boolean moving = false;
  Point health;
  String castingSequence = "";
  int frameRate;
  int dmgTimer = 1000;
  int castingTime = 0;
  boolean dead = false;
  boolean canCast = true;
  Ledge[] ledges;
  boolean jumped;
  FightPanel container;
  ImagePanel image;
  ImagePanel dmg;
  JLayeredPane lP = new JLayeredPane();
  int gCounter = 0;
  int aCounter = 0;
  int dir = 1;
  boolean check = false;
  String[] castingImages = { "derp/cast1.png", "derp/cast2.png", "derp/cast3.png", "derp/cast4.png", "derp/cast5.png", "derp/cast6.png" };
  ArrayList<Condition> conditions = new ArrayList();
  
  public BattleChar(FightPanel aContainer, int aFR, Point health)
  {
    this.health = health;
    this.vS = true;
    this.container = aContainer;
    this.jumped = false;
    this.coords.x = 20;
    this.coords.y = (MainClass.SIDE_LENGTH - SIDE_LENGTH);
    this.velocity.x = 0;
    this.velocity.y = 0;
    this.frameRate = aFR;
    this.image = new ImagePanel(new Rectangle(coords.x, coords.y, SIDE_LENGTH, SIDE_LENGTH));
    this.image.setImageWithName("derp/battlewizright.png");
    this.dmg = new ImagePanel(new Rectangle(coords.x, coords.y, SIDE_LENGTH, SIDE_LENGTH));
  }
  public void draw(Graphics g) {
	  image.draw(g);
  }
  
  public void updateImage()
  {
    if (this.dir == 1) {
      this.image.flip = false;
    } else {
      this.image.flip = true;
    }
    if (this.casting)
    {
      int x = this.castingTime / 250;
      if (x > 5) {
        x = 5;
      }
      this.image.setImageWithName(this.castingImages[x]);
    }
    else if (this.velocity.y > 0)
    {
      this.image.setImageWithName("derp/fallin.png");
    }
    else
    {
      this.image.setImageWithName("derp/battlewizright.png");
    }
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
  
  public void parse(char n)
  {
    if ((this.canCast) && 
      (!this.moving))
    {
      if (!this.casting) {
        this.casting = true;
      }
      this.castingSequence += n;
      checkCastSeq();
    }
  }
  
  public void stopCasting()
  {
    this.casting = false;
    this.castingSequence = "";
    updateImage();
  }
  
  public boolean matches(int n)
  {
    if (!this.castingSequence.equals(this.container.spells[n][1])) {
      return false;
    }
    return true;
  }
  
  public void checkCastSeq()
  {
    for (int n = 1; n < this.container.spells.length; n++) {
      if (this.castingSequence.equals(this.container.spells[n][1]))
      {
        Spell spell = new Spell(new Point(coords.x, coords.y), this.dir, this.id, this.container, this.container.spells[n][0], this.frameRate, this.castingTime);
        this.container.castedSpells.add(spell);
        stopCasting();
      }
    }
  }
  
  public void update(boolean l, boolean r, boolean drop)
  {
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
    applyVelocity(l, r, drop);
    if ((l) || (r)) {
      this.moving = true;
    } else {
      this.moving = false;
    }
    int x = this.castingTime / 250;
    if (this.casting) {
      this.castingTime += this.frameRate;
    } else {
      this.castingTime = 0;
    }
    if (x != this.castingTime / 250) {
      updateImage();
    }
  }
  
  public void applyVelocity(boolean l, boolean r, boolean drop)
  {
    Point newCoords = new Point();
    newCoords.x = this.coords.x;
    newCoords.y = this.coords.y;
    newCoords.x += this.velocity.x;
    newCoords.y += this.velocity.y;
    boolean x = false;
    if (this.velocity.y <= 0) {
      x = true;
    }
    updateVelocity(l, r, drop);
    if ((this.velocity.y >= 0) && (x)) {
      updateImage();
    }
    if (!this.dead) {
      newCoords = updateVS(newCoords, drop);
    } else {
      this.vS = false;
    }
    this.coords.y = newCoords.y;
    this.coords.x = newCoords.x;
    updateLocation();
  }
  
  public void jump()
  {
    if (!this.dead)
    {
      stopCasting();
      if (!this.jumped)
      {
        if (this.vS) {
          this.velocity.y = -10;
        } else {
          this.velocity.y = -8;
        }
        this.vS = false;
        this.jumped = true;
        this.check = true;
        updateImage();
      }
    }
  }
  
  public void updateVelocity(boolean l, boolean r, boolean drop)
  {
    if ((this.vS) && (!l) && (!r))
    {
      if (this.velocity.x < 0) {
        this.velocity.x += 1;
      } else if (this.velocity.x > 0) {
        this.velocity.x -= 1;
      }
    }
    else if (this.vS)
    {
      if ((this.velocity.x > -5) && (l)) {
        this.velocity.x -= 1;
      }
      if ((this.velocity.x < 5) && (r)) {
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
        if (drop) {
          this.velocity.y += 2;
        } else {
          this.velocity.y += 1;
        }
        this.gCounter = 0;
      }
      if (this.aCounter < 1000.0D / this.airSpeed / this.frameRate)
      {
        this.aCounter += 1;
      }
      else
      {
        if ((this.velocity.x > -3) && (l)) {
          this.velocity.x -= 1;
        }
        if ((this.velocity.x < 3) && (r)) {
          this.velocity.x += 1;
        }
        this.aCounter = 0;
      }
    }
  }
  
  public void turn(int x)
  {
    this.dir = x;
    stopCasting();
  }
  
  public Point updateVS(Point newCoords, boolean drop)
  {
    this.vS = false;
    for (int i = 0; (i < this.ledges.length) && (!drop) && (this.velocity.y >= 0); i++) {
      if ((newCoords.x + SIDE_LENGTH > this.ledges[i].getX()) && (newCoords.x < this.ledges[i].getX() + this.ledges[i].getWidth()))
      {
        int tempY = newCoords.y;
        if ((this.velocity.y == 0) && 
          (newCoords.y == this.ledges[i].getY() - SIDE_LENGTH))
        {
          this.velocity.y = 0;
          this.vS = true;
          this.jumped = false;
          return newCoords;
        }
        for (newCoords.y -= this.velocity.y; newCoords.y < tempY; newCoords.y += 1)
        {
          this.vS = false;
          if (newCoords.y == this.ledges[i].getY() - SIDE_LENGTH)
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
    if (newCoords.y >= MainClass.SIDE_LENGTH - SIDE_LENGTH) {
      if (!this.check)
      {
        newCoords.y = (MainClass.SIDE_LENGTH - SIDE_LENGTH);
        this.vS = true;
        this.jumped = false;
        this.velocity.y = 0;
      }
      else
      {
        this.check = false;
      }
    }
    if (newCoords.x < 0) {
      newCoords.x = 0;
    } else if (newCoords.x > MainClass.SIDE_LENGTH - SIDE_LENGTH) {
      newCoords.x = (MainClass.SIDE_LENGTH - SIDE_LENGTH);
    }
    return newCoords;
  }
  public void updateLocation() {
	  image.setLocation(coords);
  }
  public Rectangle getBounds() {
	  return new Rectangle(coords.x, coords.y, SIDE_LENGTH, SIDE_LENGTH);
  }
  public int getX() {
	  return coords.x;
  }
  public int getY() {
	  return coords.y;
  }
}
