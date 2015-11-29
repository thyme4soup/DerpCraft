import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JPanel;

public class Path
  extends JPanel
{
  public boolean isEnterable = true;
  public boolean interact = false;
  Point gridCoords;
  
  public Path(Dimension size, Point gridCoords)
  {
    setLayout(null);
    setSize(size);
    setVisible(true);
    setOpaque(false);
    ImagePanel image = new ImagePanel(getSize());
    image.setImageWithName("cobble path.jpg");
    this.gridCoords = gridCoords;
    add(image);
  }
  
  public static Entity loadEntity(String c, Point[][] grid)
  {
    int[] coords = MainClass.loadCoords(c);
    return new Entity(new Point(coords[0], coords[1]), 2, grid);
  }
  
  public void interact() {}
  
  public String getString()
  {
    String temp = "";
    temp = String.format("%d; %d; %d;", new Object[] { Integer.valueOf(2), Integer.valueOf(this.gridCoords.x), Integer.valueOf(this.gridCoords.y) });
    return temp;
  }
}
