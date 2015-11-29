import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JPanel;

public class EnterableImage
  extends JPanel
{
  public boolean isEnterable = true;
  ImagePanel image;
  String imageName;
  Point gridCoords;
  
  public EnterableImage(Dimension size, Point gridCoords)
  {
    setLayout(null);
    setSize(size);
    setVisible(true);
    setOpaque(false);
    this.image = new ImagePanel(getSize());
    this.gridCoords = gridCoords;
    add(this.image);
  }
  
  public static Entity loadEntity(String c, Point[][] grid)
  {
    String imageName = "";
    int n = 0;
    int[] coords = MainClass.loadCoords(c);
    n = coords[2];
    while (c.charAt(n) != ';')
    {
      imageName = imageName + c.charAt(n);
      n++;
    }
    Entity tempImage = new Entity(new Point(coords[0], coords[1]), 3, grid);
    tempImage.enterableImage.setImage(imageName);
    tempImage.enterableImage.imageName = imageName;
    return tempImage;
  }
  
  public void interact() {}
  
  public void setImage(String imageName)
  {
    this.image.setImageWithName(imageName);
  }
  
  public String getString()
  {
    String temp = "";
    temp = String.format("%d; %d; %d; %s;", new Object[] { Integer.valueOf(3), Integer.valueOf(this.gridCoords.x), Integer.valueOf(this.gridCoords.y), this.imageName });
    return temp;
  }
}
