import java.awt.Point;
import java.io.PrintStream;

public class Condition
{
  int duration;
  int frameRate;
  int type;
  int dOT;
  int timer = 0;
  int time = 0;
  double dmgDealt = 0.0D;
  boolean dmgFinished = false;
  boolean timeFinished = false;
  boolean finished;
  
  public Condition(int type, int duration, int dOT, int frameRate)
  {
    this.type = type;
    this.frameRate = 10;
    this.duration = duration;
    this.dOT = dOT;
    if (dOT == -1) {
      this.dmgFinished = true;
    }
  }
  
  public void apply(NPC npc)
  {
    this.time += this.frameRate;
    if (!this.dmgFinished)
    {
      Point tmp24_21 = npc.health;tmp24_21.x = ((int)(tmp24_21.x - getDmg()));
    }
    npc.updateLifeStatus();
    if (!this.finished) {
      switch (this.type)
      {
      case 0: 
        break;
      case 1: 
        npc.velocity.x = 0;
        npc.velocity.y = 0;
        npc.casted = true;
        npc.jumped = true;
      }
    }
    if (this.time >= this.duration) {
      this.timeFinished = true;
    }
    if ((this.timeFinished) && (this.dmgFinished))
    {
      this.finished = true;
      if (this.type == 1) {
        npc.casted = false;
      }
      if (npc.dead) {
        npc.velocity.y = -5;
      }
    }
  }
  
  public void apply(BattleChar bC)
  {
    this.time += this.frameRate;
    if ((!this.dmgFinished) && (this.dOT != 0))
    {
      Point tmp31_28 = bC.health;tmp31_28.x = ((int)(tmp31_28.x - getDmg()));
    }
    bC.updateLifeStatus();
    if (!this.finished) {
      switch (this.type)
      {
      case 0: 
        break;
      case 1: 
        bC.velocity.x = 0;
        bC.velocity.y = 0;
        bC.canCast = false;
        bC.jumped = true;
      }
    }
    if (this.time >= this.duration) {
      this.timeFinished = true;
    }
    if ((this.timeFinished) && (this.dmgFinished))
    {
      this.finished = true;
      bC.canCast = true;
      if (bC.dead) {
        bC.velocity.y = -5;
      }
    }
  }
  
  public double getDmg()
  {
    this.timer += 1;
    double dmg = 0.0D;
    double temp = this.duration / (this.dOT * this.frameRate);
    if (this.timer >= temp)
    {
      if (temp < 1.0D) {
        dmg = 1.0D / temp;
      } else {
        dmg = 1.0D;
      }
      this.timer = 0;
    }
    this.dmgDealt += dmg;
    if (this.dmgDealt >= this.dOT)
    {
      if (this.dmgDealt == this.dOT) {
        System.out.println("perfect");
      } else {
        System.out.println("error in calculating damage");
      }
      this.dmgFinished = true;
      return 0.0D;
    }
    return dmg;
  }
}
