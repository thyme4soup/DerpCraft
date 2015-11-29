import java.awt.Color;
import java.awt.Rectangle;
import javax.swing.JPanel;

public class Ledge
  extends JPanel
{
  public Ledge(Rectangle dimensions)
  {
    setBackground(Color.BLUE);
    setLayout(null);
    setBounds(dimensions);
    setFocusable(true);
    setVisible(true);
  }
}
