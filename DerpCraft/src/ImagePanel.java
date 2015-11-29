import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.PrintStream;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel
  extends JPanel
{
  public Image image;
  public Image[] animationImages;
  public int drawIndicator;
  public Color drawColor;
  public int durationOfLoop;
  public int waitTime;
  public int animationCounter;
  public int timer;
  public int n;
  public boolean scale = false;
  public boolean flip = false;
  public String text;
  
  public ImagePanel(Dimension size)
  {
    setSize(size.width, size.height);
    setOpaque(false);
    this.drawIndicator = -1;
    this.drawColor = Color.WHITE;
    this.durationOfLoop = 0;
    this.waitTime = 0;
    this.image = null;
    this.animationImages = null;
    setLayout(null);
  }
  
  public ImagePanel(Rectangle bounds)
  {
    setSize((int)bounds.getWidth(), (int)bounds.getHeight());
    setLocation(bounds.getLocation());
    setOpaque(false);
    this.drawIndicator = -1;
    this.drawColor = Color.WHITE;
    this.durationOfLoop = 0;
    this.waitTime = 0;
    this.image = null;
    this.animationImages = null;
    setLayout(null);
  }
  
  public void setImageAsGif(String[] names, int aDurationOfLoop, int aWaitTime)
  {
    this.drawIndicator = 3;
    this.durationOfLoop = aDurationOfLoop;
    this.waitTime = aWaitTime;
    this.animationImages = new Image[names.length];
    this.n = (this.durationOfLoop / (this.waitTime * names.length));
    File[] f = new File[names.length];
    for (int i = 0; i < names.length; i++) {
      f[i] = new File(names[i]);
    }
    try
    {
      for (int i = 0; i < f.length; i++)
      {
        this.animationImages[i] = ImageIO.read(getClass().getResource("resources/" + names[i]));
        System.out.println(i);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    repaint();
  }
  
  public void setImageAsGif(String[] names, int aDurationOfLoop, int aWaitTime, int x)
  {
    this.drawIndicator = 3;
    this.durationOfLoop = aDurationOfLoop;
    this.waitTime = aWaitTime;
    this.animationImages = new Image[names.length];
    this.n = (this.durationOfLoop / (this.waitTime * names.length));
    File[] f = new File[names.length];
    for (int i = 0; i < names.length; i++) {
      f[i] = new File(names[i]);
    }
    try
    {
      for (int i = 0; i < f.length; i++) {
        this.animationImages[i] = ImageIO.read(getClass().getResource("resources/" + names[i]));
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    if (x == -1) {
      this.flip = true;
    }
    repaint();
  }
  
  public void setImageWithName(String imageName, int x)
  {
    this.drawIndicator = 0;
    try
    {
      this.image = ImageIO.read(getClass().getResource("resources/" + imageName));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    if (x == -1) {
      this.flip = true;
    }
    repaint();
  }
  
  public void setImageWithName(String imageName)
  {
    this.drawIndicator = 0;
    try
    {
      this.image = ImageIO.read(getClass().getResource("resources/" + imageName));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    repaint();
  }
  
  public void setImageAsCircle(Color c)
  {
    this.drawIndicator = 1;
    this.drawColor = c;
    repaint();
  }
  
  public void setImageAsSquare(Color c)
  {
    this.drawIndicator = 2;
    this.drawColor = c;
    repaint();
  }
  
  public void setImageAsText(String text, Color c)
  {
    this.drawIndicator = 4;
    this.drawColor = c;
    repaint();
    this.text = text;
  }
  
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    
    g.setColor(this.drawColor);
    Image im = this.image;
    switch (this.drawIndicator)
    {
    case 0: 
      if (this.flip) {
        g.drawImage(im, 
          im.getWidth(this), 0, 0, im.getHeight(this), 
          0, 0, im.getWidth(this), im.getHeight(this), 
          this);
      } else {
        g.drawImage(im, 0, 0, null);
      }
      break;
    case 1: 
      g.fillOval(0, 0, getWidth(), getHeight());
      break;
    case 2: 
      g.fillRect(0, 0, getWidth(), getHeight());
      break;
    case 3: 
      this.timer += 1;
      if (this.timer == this.n)
      {
        this.animationCounter += 1;
        this.timer = 0;
        if (this.animationCounter == this.animationImages.length) {
          this.animationCounter = 0;
        }
      }
      im = this.animationImages[this.animationCounter];
      if (this.flip) {
        g.drawImage(im, 
          im.getWidth(this), 0, 0, im.getHeight(this), 
          0, 0, im.getWidth(this), im.getHeight(this), 
          this);
      } else {
        g.drawImage(im, 0, 0, null);
      }
      break;
    case 4: 
      g.drawString(this.text, getWidth() / 2 - this.text.length() * 7 / 2, getHeight() / 2);
      break;
    }
  }
}