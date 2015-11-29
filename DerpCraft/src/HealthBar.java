import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import javax.swing.JPanel;

public class HealthBar
  extends JPanel
{
  Point health;
  String title;
  int width;
  
  public HealthBar(Point aHealth, Point location, String aTitle)
  {
    setLayout(null);
    setSize(150, 35);
    setLocation(location.x, location.y);
    setOpaque(false);
    setVisible(true);
    this.health = aHealth;
    this.title = aTitle;
    update();
  }
  
  public void paintComponent(Graphics g)
  {
    g.setColor(Color.RED);
    g.fillRect(1, 1, this.width, getHeight() - 1);
    g.setColor(Color.BLACK);
    g.drawString(this.title, 5, getHeight() / 2 + 5);
    g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
  }
  
  public void update()
  {
    this.width = (this.health.x * getWidth() / this.health.y);
  }
}
