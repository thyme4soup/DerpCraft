import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Cutscene
  extends JPanel
  implements KeyListener
{
  GamePanel container;
  int delay = 500;
  Color test;
  int[] counter = new int[4];
  int countah = 0;
  int delayCounter = 0;
  boolean f = false;
  double temp = 0.0D;
  int imageCounter;
  String[] images;
  ImagePanel image;
  boolean timerUp = false;
  int frameRate = 10;
  int perimiter;
  
  public Cutscene(GamePanel container, String cutsceneName)
  {
    setLayout(null);
    setSize(container.getWidth(), container.getHeight());
    setLocation(0, 0);
    setBackground(Color.BLACK);
    addKeyListener(this);
    setFocusable(true);
    setVisible(true);
    loadImages(cutsceneName);
    this.image = new ImagePanel(new Rectangle(5, 5, getWidth() - 11, getHeight() - 11));
    this.image.setImageWithName(this.images[0]);
    add(this.image);
    this.perimiter = ((getHeight() - 1) * 2 + (getWidth() - 1) * 2);
    this.test = Color.RED;
  }
  
  public void loadImages(String cutsceneName)
  {
    BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("resources/cutscenes/" + cutsceneName)));
    ArrayList<String> c = new ArrayList();
    String line = null;
    try
    {
      while ((line = reader.readLine()) != null) {
        c.add(line);
      }
    }
    catch (IOException e)
    {
      System.out.println("Well that wasn't supposed to happen");
    }
    this.images = new String[c.size()];
    for (int i = 0; i < c.size(); i++)
    {
      int n = 0;
      this.images[i] = "";
      while ((((String)c.get(i)).charAt(n) != ';') && (n < ((String)c.get(i)).length()))
      {
        int tmp120_118 = i; String[] tmp120_115 = this.images;tmp120_115[tmp120_118] = (tmp120_115[tmp120_118] + ((String)c.get(i)).charAt(n));
        n++;
      }
    }
  }
  
  public void startLoop()
  {
    requestFocus();
    while (this.imageCounter < this.images.length)
    {
      try
      {
        Thread.sleep(this.frameRate);
      }
      catch (Exception localException) {}
      repaint();
    }
    try
    {
      Thread.sleep(1000L);
    }
    catch (InterruptedException localInterruptedException) {}
  }
  
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    g.setColor(Color.darkGray);
    g.fillRect(0, 0, getWidth(), getHeight());
    g.setColor(Color.RED);
    if (!this.timerUp)
    {
      this.temp += this.perimiter / (this.delay / this.frameRate);
      this.counter[this.countah] = ((int)this.temp);
      if (this.counter[this.countah] > getWidth())
      {
        this.counter[this.countah] = getWidth();
        this.countah += 1;
        this.temp = 0.0D;
      }
      if (this.countah > 3) {
        this.timerUp = true;
      }
    }
    else
    {
      g.setColor(Color.green);
    }
    if (this.imageCounter < this.images.length)
    {
      g.fillRect(0, 0, this.counter[0], 5);
      g.fillRect(getWidth() - 6, 0, getWidth() - 1, this.counter[1]);
      g.fillRect(getWidth() - this.counter[2], getHeight() - 6, getWidth() - 1, getHeight() - 1);
      g.fillRect(0, getHeight() - this.counter[3], 5, getHeight());
    }
  }
  
  public void resetCounter()
  {
    for (int i = 0; i < this.counter.length; i++) {
      this.counter[i] = 0;
    }
  }
  
  public void keyPressed(KeyEvent arg0)
  {
    switch (arg0.getKeyCode())
    {
    case 39: 
      if (this.timerUp)
      {
        this.imageCounter += 1;
        if (this.imageCounter < this.images.length) {
          this.image.setImageWithName(this.images[this.imageCounter]);
        }
        this.countah = 0;
        this.timerUp = false;
        this.test = Color.green;
        updateUI();
        resetCounter();
      }
      break;
    case 37: 
      if (this.timerUp)
      {
        if (this.imageCounter > 0) {
          this.imageCounter -= 1;
        }
        this.image.setImageWithName(this.images[this.imageCounter]);
        this.countah = 0;
        this.timerUp = false;
        resetCounter();
      }
      break;
    }
  }
  
  public void keyReleased(KeyEvent arg0) {}
  
  public void keyTyped(KeyEvent arg0) {}
}
