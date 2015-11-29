import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JPanel;

public class Tree
  extends JPanel
{
  public boolean isEnterable = false;
  public boolean interact = false;
  public Point gridCoords;
  
  public Tree(Dimension size, Point gridCoords)
  {
    this.gridCoords = gridCoords;
    setLayout(null);
    setSize(size);
    setVisible(true);
    setOpaque(false);
    ImagePanel image = new ImagePanel(getSize());
    image.setImageWithName("treegreen.png");
    add(image);
  }
  
  public static Entity loadEntity(String c, Point[][] grid)
  {
    int[] coords = MainClass.loadCoords(c);
    return new Entity(new Point(coords[0], coords[1]), 0, grid);
  }
  
  public void interact() {}
  
  public String getString()
  {
    String temp = "";
    temp = String.format("%d; %d; %d;", new Object[] { Integer.valueOf(0), Integer.valueOf(this.gridCoords.x), Integer.valueOf(this.gridCoords.y) });
    return temp;
  }
}
