import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.JPanel;

public class FightPanel
  implements KeyListener
{
  BattleChar bC;
  Ledge[] ledges;
  GamePanel aInstance;
  ArrayList<HealthBar> health = new ArrayList();
  HealthBar pcHealth;
  boolean l = false;
  boolean r = false;
  boolean win = false;
  boolean drop = false;
  int waitCounter = 0;
  int frameRate = 10;
  ImagePanel background;
  ArrayList<NPC> npcs = new ArrayList();
  String[][] spells;
  ArrayList<Spell> castedSpells = new ArrayList();
  
  public FightPanel(GamePanel instance, String fightName)
  {
    background = new ImagePanel(new Rectangle(0, 0, MainClass.SIDE_LENGTH, MainClass.SIDE_LENGTH));
    this.aInstance = instance;
    this.spells = instance.pChar.spells;
    this.bC = new BattleChar(this, 10, instance.pChar.health);
    loadFight(fightName);
    this.bC.ledges = this.ledges;
    this.pcHealth = new HealthBar(instance.pChar.health, new Point(0, 0), "Derp");
    for (int i = 0; i < this.npcs.size(); i++)
    {
      this.health.add(new HealthBar(((NPC)this.npcs.get(i)).health, new Point(MainClass.SIDE_LENGTH - 151, 45 * i), "Enemy"));
    }
  }
  public void draw(Graphics g) {
	  background.draw(g);
	  bC.draw(g);
	  pcHealth.draw(g);
	  for(NPC npc : npcs) npc.draw(g);
	  for(Spell s : castedSpells) s.draw(g);
	  for(Ledge l : ledges) l.draw(g);
	  for(HealthBar h : health) h.draw(g);
  }
  
	public void loadFight(String fight) {
		System.out.println(fight);
		BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("resources/"+fight)));
		ArrayList<String> c = new ArrayList<String>();
		String line = null;
		try {
			while((line = reader.readLine())!=null) {
				c.add(line);
			}
		} catch (IOException e) {System.out.println("Well that wasn't supposed to happen");}
		int n = 0;
		String[] temp;
		temp = getNext(n, c.get(0), ':');
		String background = temp[0];
		n = Integer.valueOf(temp[1]);
		switch(background) {
		case "gif":
			String temp3 = "";
			while(n < c.get(0).length() && c.get(0).charAt(n) != ';') {
				temp3 += c.get(0).charAt(n);
				n++;
			}
			n+=2;
			int duration = Integer.valueOf(background);
			temp3 = "";
			ArrayList<String> tempNames = new ArrayList<String>();
			while(n < c.get(0).length()) {
				while(n < c.get(0).length() && c.get(0).charAt(n) != ';') {
					temp3 += c.get(0).charAt(n);
					n++;
				}
				tempNames.add(temp3);
				n+= 2;
				temp3 = "";
			}
			int j = 0;
			String[] names = new String[tempNames.size()];
			while(j < tempNames.size()) {
				names[j] = tempNames.get(j);
				j++;
			}
			this.background.setImageAsGif(names, duration, frameRate);
			break;
		case "color":
			int[] color = new int[3];
			for(int l = 0; l < color.length; l ++) {
				String temp2 = "";
				while(c.get(0).charAt(n)!=' ' && c.get(0).charAt(n)!=';') {
					temp2+=c.get(0).charAt(n);
					n++;
					if(n == c.get(0).length())
						break;
				}
				n++;
				color[l] = Integer.valueOf(temp2);
			}
			this.background.setImageAsCircle(new Color(color[0], color[1], color[2]));
			break;
		default:
			System.out.println("Unknown command: "+temp+". Try 'gif: ' or 'color: ', or check other spells");
			break;
		}
		String temp2 = "";
		ArrayList<Ledge> tempLedges = new ArrayList<Ledge>();
		int i = 2;
		n = 0;
		int[] data = new int[4];
		while(true && i < c.size()) { //load ledges
			if(c.get(i).equals("npcs:")) { //breaks when reaches npc section
				i++;
				break;
			}
			n = 0;
			for(int j = 0; j < data.length; j++) { //load ledge info
				temp = getNext(n, c.get(i), ';');
				temp2 = temp[0];
				n = Integer.valueOf(temp[1]);
				data[j] = Integer.valueOf(temp2);
			}
			tempLedges.add(new Ledge(new Rectangle(data[0], data[1], data[2], data[3])));
			i++;
		}
		ledges = new Ledge[tempLedges.size()];
		for(int j = 0; j < tempLedges.size(); j++)
			ledges[j] = tempLedges.get(j);
		while(i < c.size()) {
			npcs.add(new NPC(this.getSize(), 10, ledges, this, bC, getNext(0, c.get(i), ';')[0]));
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
  
  public void startLoop()
  {
    try
    {
      Thread.sleep(1500L);
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }
    do
    {
      try
      {
        Thread.sleep(this.frameRate);
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
      this.bC.update(this.l, this.r, this.drop);
      this.pcHealth.update();
      for (int i = 0; i < this.npcs.size(); i++) {
        ((NPC)this.npcs.get(i)).update();
      }
      for (int i = 0; i < this.castedSpells.size(); i++) {
        ((Spell)this.castedSpells.get(i)).update();
      }
      checkSpellIntersections();
      for (int i = 0; i < this.health.size(); i++) {
        ((HealthBar)this.health.get(i)).update();
      }
      updateDeadNPCs();
      if (this.bC.getY() >= MainClass.SIDE_LENGTH * 2) {
        break;
      }
    } while (!this.win);
    while (this.waitCounter < 1500)
    {
      try
      {
        Thread.sleep(this.frameRate);
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
      this.waitCounter += this.frameRate;
    }
  }
  
  public void updateDeadNPCs()
  {
    for (int i = 0; i < this.npcs.size(); i++) {
      if ((((NPC)this.npcs.get(i)).dead) && (((NPC)this.npcs.get(i)).getY() > MainClass.SIDE_LENGTH * 2))
      {
        this.npcs.remove(this.npcs.get(i));
        
        this.health.remove(this.health.get(i));
        for (int j = i; j < this.health.size(); j++) {
          ((HealthBar)this.health.get(j)).setLocation(MainClass.SIDE_LENGTH - 151, 45 * j);
        }
      }
    }
    if (this.npcs.size() == 0) {
      this.win = true;
    }
  }
  
  public void checkSpellIntersections()
  {
    for (int i = 0; i < this.castedSpells.size(); i++)
    {
      if ((((Spell)this.castedSpells.get(i)).getBounds().intersects(this.bC.getBounds())) && (((Spell)this.castedSpells.get(i)).id != this.bC.id))
      {
        ((Spell)this.castedSpells.get(i)).interact(this.bC);
        this.castedSpells.remove(this.castedSpells.get(i));
        if (i > 0) {
          i--;
        }
      }
      for (int j = 0; (j < this.npcs.size()) && (i < this.castedSpells.size()); j++) {
        if ((((Spell)this.castedSpells.get(i)).getBounds().intersects(((NPC)this.npcs.get(j)).getBounds())) && (((Spell)this.castedSpells.get(i)).id != ((NPC)this.npcs.get(j)).id))
        {
          ((Spell)this.castedSpells.get(i)).interact((NPC)this.npcs.get(j));
          this.castedSpells.remove(this.castedSpells.get(i));
          if (j > 0) {
            j--;
          }
          if (i > 0) {
            i--;
          }
        }
      }
    }
  }
  public Rectangle getSize() {
	  return new Rectangle(0, 0, MainClass.SIDE_LENGTH, MainClass.SIDE_LENGTH);
  }
  
  public void keyPressed(KeyEvent arg0)
  {
    switch (arg0.getKeyCode())
    {
    case 37: 
      this.l = true;
      this.bC.turn(-1);
      break;
    case 38: 
      this.bC.jump();
      break;
    case 39: 
      this.r = true;
      this.bC.turn(1);
      break;
    case 40: 
      this.drop = true;
      break;
    case 16: 
      this.bC.stopCasting();
    }
  }
  
  public void keyReleased(KeyEvent arg0)
  {
    switch (arg0.getKeyCode())
    {
    case 37: 
      this.l = false;
      break;
    case 39: 
      this.r = false;
      break;
    case 38: 
      break;
    case 40: 
      this.drop = false;
      break;
    case 16: 
      break;
    default: 
      this.bC.parse(arg0.getKeyChar());
    }
  }
  
  public void keyTyped(KeyEvent arg0) {}
}