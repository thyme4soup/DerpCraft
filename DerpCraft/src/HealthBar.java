import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import javax.swing.JPanel;

public class HealthBar
{
  Point health;
  String title;
  Point location;
  int width = 0;
  public static final int WIDTH = 135;
  public static final int HEIGHT = 35;
  
  public HealthBar(Point aHealth, Point location, String aTitle)
  {
	  this.location = location;
    this.location = new Point(location.x, location.y);
    this.health = aHealth;
    this.title = aTitle;
    update();
  }
  
  public void draw(Graphics g)
  {
    g.setColor(Color.RED);
    g.fillRect(location.x + 1, location.y + 1, this.width, HEIGHT - 1);
    g.setColor(Color.BLACK);
    g.drawString(this.title, 5, HEIGHT / 2 + 5);
    g.drawRect(location.x, location.y, WIDTH - 1, HEIGHT - 1);
  }
  
  public void update()
  {
    this.width = (this.health.x * WIDTH / this.health.y);
  }
}
