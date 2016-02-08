import java.awt.Color;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import javax.swing.JFrame;

public class MainClass
{
	static final int SIDE_LENGTH = 700;
	static final int GRID_LENGTH = 20;
	static final int UNIT_SIZE = SIDE_LENGTH / GRID_LENGTH; //35
  public static void main(String[] args)
  {
    JFrame frame = new JFrame();
    frame.setBackground(Color.LIGHT_GRAY);
    frame.setDefaultCloseOperation(3);
    GamePanel panel = new GamePanel("saveName");
    frame.add(panel);
    frame.pack();
    frame.setResizable(false);
    frame.setVisible(true);
    frame.setLocation(new Point(5, 5));
    panel.startGameLoop();
    frame.dispose();
  }
  
  public static String[] getNext(int n, String c, char stopChar)
  {
    String[] temp = new String[2];
    temp[0] = "";
    temp[1] = "";
    while ((n < c.length()) && (c.charAt(n) != stopChar))
    {
      int tmp20_19 = 0; String[] tmp20_18 = temp;tmp20_18[tmp20_19] = (tmp20_18[tmp20_19] + c.charAt(n));
      n++;
    }
    n += 2;
    temp[1] = ""+n;
    return temp;
  }
  
  public static int[] loadCoords(String c)
  {
    String temp = "";
    int x = 0;
    int n = 0;
    int y = 0;
    while (c.charAt(n) != ';') {
      n++;
    }
    n += 2;
    while (c.charAt(n) != ';')
    {
      temp = temp + c.charAt(n);
      n++;
    }
    n += 2;
    x = Integer.valueOf(temp).intValue();
    temp = "";
    while (c.charAt(n) != ';')
    {
      temp = temp + c.charAt(n);
      n++;
    }
    n += 2;
    y = Integer.valueOf(temp).intValue();
    return new int[] { x, y, n };
  }
  
  public static void saveFile(String saveName, String[] contents, String fileName)
    throws IOException
  {
    System.out.println("saving: " + fileName);
    try
    {
      File save = new File(System.getProperty("user.home") + "/AppData/Roaming/.DerpCraft/" + saveName + "/" + fileName);
      BufferedWriter BWrite = new BufferedWriter(new FileWriter(save));
      for (int i = 0; i < contents.length; i++)
      {
        BWrite.write(contents[i]);
        if (i < contents.length - 1) {
          BWrite.newLine();
        }
      }
      BWrite.close();
      System.out.println("Old save of " + fileName + " detected, rewriten");
    }
    catch (IOException e)
    {
      File dir = new File(System.getProperty("user.home") + "/AppData/Roaming/.DerpCraft/" + saveName);
      dir.mkdirs();
      try
      {
        System.out.println("No older saves of " + fileName + " detected, creating new file...");
        File old = new File(System.getProperty("user.home") + "/AppData/Roaming/.DerpCraft/" + saveName + "/" + fileName);
        BufferedWriter BWrite = new BufferedWriter(new FileWriter(old));
        for (int i = 0; i < contents.length; i++)
        {
          BWrite.write(contents[i]);
          if (i < contents.length - 1) {
            BWrite.newLine();
          }
        }
        BWrite.close();
      }
      catch (IOException f)
      {
        System.out.println("An error occured in saving..."); return;
      }
    }
    BufferedWriter BWrite;
    System.out.println("saved: " + fileName);
  }
}