import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JPanel;

public class Path
  extends Entity
{
	public static final int TYPE = 2;
  
  public Path(Point gridCoords)
  {
	  super(gridCoords);
	  isEnterable = true;
    image.setImageWithName("cobble path.jpg");
  }
  
  public static Entity loadEntity(String c)
  {
    int[] coords = MainClass.loadCoords(c);
    return new Path(new Point(coords[0], coords[1]));
  }
  
  public void interact() {}
  
  public String getString()
  {
    String temp = "";
    temp = String.format("%d; %d; %d;", new Object[] { Integer.valueOf(2), Integer.valueOf(this.gridCoords.x), Integer.valueOf(this.gridCoords.y) });
    return temp;
  }
}
