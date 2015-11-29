import java.awt.Color;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Spell
  extends JPanel
{
  int velocity;
  int dir;
  int dmg;
  double dmgModifier;
  int baseDmg;
  int castingTime = 0;
  int id;
  int frameRate;
  ImagePanel image;
  ArrayList<Condition> conditions = new ArrayList();
  Point coords = new Point();
  FightPanel container;
  
  public Spell(Point aCoords, int dir, int id, FightPanel aContainer, String spellName, int frameRate, int castingTime)
  {
    setLayout(null);
    setSize(50, 50);
    setLocation(aCoords.x, aCoords.y);
    this.container = aContainer;
    this.coords.x = aCoords.x;
    this.coords.y = aCoords.y;
    this.velocity = (dir * 5);
    this.dir = dir;
    this.id = id;
    this.castingTime = castingTime;
    this.frameRate = frameRate;
    this.image = new ImagePanel(getSize());
    this.image.setImageAsCircle(Color.RED);
    loadSpell(spellName);
    add(this.image);
    setVisible(true);
    setOpaque(false);
  }
  
  public Spell(Point aCoords, int dir, int id, FightPanel aContainer, String spellName, int frameRate)
  {
    setLayout(null);
    setSize(50, 50);
    setLocation(aCoords.x, aCoords.y);
    this.image = new ImagePanel(getSize());
    this.container = aContainer;
    this.coords.x = aCoords.x;
    this.coords.y = aCoords.y;
    this.velocity = (dir * 8);
    this.dir = dir;
    this.id = id;
    this.frameRate = frameRate;
    loadSpell(spellName);
    add(this.image);
    setVisible(true);
    setOpaque(false);
  }
  
  public void update()
  {
    this.coords.x += this.velocity;
    if ((this.coords.x > this.container.getWidth()) || (this.coords.x < 0 - getWidth()))
    {
      this.container.remove(this);
      this.container.castedSpells.remove(this);
      return;
    }
    updateLocation();
  }
  
  public void updateLocation()
  {
    setLocation(this.coords.x, this.coords.y);
  }
  
  public void interact(BattleChar bC)
  {
    if (this.id != bC.id)
    {
      bC.health.x -= this.dmg; Point 
        tmp31_28 = bC.velocity;tmp31_28.x = ((int)(tmp31_28.x + (this.dmg * 0.1D + 5.0D) * this.dir)); Point 
        tmp64_61 = bC.velocity;tmp64_61.y = ((int)(tmp64_61.y - this.dmg * 0.1D));
      bC.jumped = false;
      bC.stopCasting();
      bC.updateLifeStatus();
      for (int i = 0; i < this.conditions.size(); i++) {
        bC.conditions.add((Condition)this.conditions.get(i));
      }
    }
  }
  
  public void interact(NPC npc)
  {
    if (this.id != npc.id)
    {
      npc.health.x -= this.dmg; Point 
        tmp31_28 = npc.velocity;tmp31_28.x = ((int)(tmp31_28.x + (this.dmg * 0.1D + 5.0D) * this.dir)); Point 
        tmp64_61 = npc.velocity;tmp64_61.y = ((int)(tmp64_61.y - this.dmg * 0.1D));
      npc.jumped = false;
      npc.updateLifeStatus();
      for (int i = 0; i < this.conditions.size(); i++) {
        npc.conditions.add((Condition)this.conditions.get(i));
      }
    }
  }
  
  public void loadSpell(String spellName)
  {
    System.out.println(spellName);
    BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("resources/" + spellName)));
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
    String[] c = new String[spellContents.size()];
    for (int i = 0; i < spellContents.size(); i++) {
      c[i] = ((String)spellContents.get(i));
    }
    loadBackground(c[1]);
    loadDamage(c[2]);
    loadConditions(c);
  }
  
	public void loadBackground(String c) { //c follows format "type: modifier; (images if necessary separated by ';');
		String temp = "";
		int i = 0;
		while(i < c.length() && c.charAt(i) != ':') {
			temp += c.charAt(i);
			i++;
		}
		i+=2;
		switch(temp) {
		case "gif":
			temp = "";
			while(i < c.length() && c.charAt(i) != ';') {
				temp += c.charAt(i);
				i++;
			}
			i+=2;
			int duration = Integer.valueOf(temp);
			temp = "";
			ArrayList<String> tempNames = new ArrayList<String>();
			while(i < c.length()) {
				while(i < c.length() && c.charAt(i) != ';') {
					temp += c.charAt(i);
					i++;
				}
				tempNames.add(temp);
				i+= 2;
				temp = "";
			}
			int j = 0;
			String[] names = new String[tempNames.size()];
			while(j < tempNames.size()) {
				names[j] = tempNames.get(j);
				j++;
			}
			image.setImageAsGif(names, duration, frameRate, dir);
			break;
		case "color":
			int[] color = new int[3];
			for(int l = 0; l < color.length; l ++) {
				String temp2 = "";
				while(c.charAt(i)!=' ' && c.charAt(i)!=';') {
					temp2+=c.charAt(i);
					i++;
					if(i == c.length())
						break;
				}
				i++;
				System.out.println(temp2);
				color[l] = Integer.valueOf(temp2);
			}
			image = new ImagePanel(this.getSize());
			image.setImageAsCircle(new Color(color[0], color[1], color[2]));
			break;
		default:
			System.out.println("Unknown command: "+temp+". Try 'gif: ' or 'color: ', or check other spells");
			break;
		}
	}
  
  public void loadDamage(String c)
  {
    String temp = "";
    int i = 0;
    while ((i < c.length()) && (c.charAt(i) != ';'))
    {
      temp = temp + c.charAt(i);
      i++;
    }
    this.baseDmg = Integer.valueOf(temp).intValue();
    i += 2;
    temp = "";
    while ((i < c.length()) && (c.charAt(i) != ';'))
    {
      temp = temp + c.charAt(i);
      i++;
    }
    this.dmgModifier = Double.valueOf(temp).doubleValue();
    i += 2;
    temp = "";
    while ((i < c.length()) && (c.charAt(i) != ';'))
    {
      temp = temp + c.charAt(i);
      i++;
    }
    int cap = Integer.valueOf(temp).intValue();
    i += 2;
    temp = "";
    this.dmg = ((int)(this.castingTime * this.dmgModifier + this.baseDmg));
    if (this.dmg > cap) {
      this.dmg = cap;
    }
  }
  
  public void loadConditions(String[] c)
  {
    for (int n = 3; n < c.length; n++)
    {
      int i = 0;
      String temp = "";
      while ((i < c[n].length()) && (c[n].charAt(i) != ';'))
      {
        temp = temp + c[n].charAt(i);
        i++;
      }
      int type = Integer.valueOf(temp).intValue();
      i += 2;
      temp = "";
      while ((i < c[n].length()) && (c[n].charAt(i) != ';'))
      {
        temp = temp + c[n].charAt(i);
        i++;
      }
      int duration = Integer.valueOf(temp).intValue();
      i += 2;
      temp = "";
      while ((i < c[n].length()) && (c[n].charAt(i) != ';'))
      {
        temp = temp + c[n].charAt(i);
        i++;
      }
      int dOT = Integer.valueOf(temp).intValue();
      this.conditions.add(new Condition(type, duration, dOT, this.frameRate));
    }
  }
}
