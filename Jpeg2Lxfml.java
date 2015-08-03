import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.net.*;

/**
 * Converts JPEG image into LXFML, so you can import to Lego Digital
 * Designer.  Example:
 *
 *  # outputs to input.jpg.lxfml
 *  % java Jpeg2Lxfml input.jpg 
 */
public final class Jpeg2Lxfml {

  public static void main(String[] args) {
    if (args == null || args.length == 0) {
      args = new String[]{"test.jpg"};
    }
    try {
      new Jpeg2Lxfml().realMain(args);
    } catch (Exception e) {e.printStackTrace();}
  }

  void realMain(String[] args) throws Exception {
    for (int i=0; i<args.length; i++) convert(args[i]);
  }

  private void convert(String image) throws Exception {
    note("image: " + image);

    Image img = Toolkit.getDefaultToolkit().createImage(image);
    JButton obs = new JButton(new ImageIcon(img));
    int w = img.getWidth(obs);
    int h = img.getHeight(obs);
    note("size: " + w + "x" + h);

    int[] pixels = new int[w*h];
    PixelGrabber pg = new PixelGrabber(img,0,0,w,h,pixels,0,w);
    try {pg.grabPixels();} catch (Exception e) {e.printStackTrace();}
    if ((pg.getStatus() & ImageObserver.ABORT) != 0) return;

    note("Done creating new image");
    File outFile = new File(image + ".lxfml");
    int refID = 0;
    PrintWriter out = new PrintWriter(new FileWriter(outFile));
    out("Writing to " + outFile + "...");
    out.println(header());
  outer: for (int i=0; i<h; i++) {
      note((i+1) + " / " + h + " [" + (100*i/h) + "%]");
    inner: for (int j=0; j<w; j++) {
        int pixel = pixels[i*w+j];
        int alpha = (pixel >> 24) & 0xff;
        int red   = (pixel >> 16) & 0xff;
        int green = (pixel >>  8) & 0xff;
        int blue  = (pixel      ) & 0xff;
	Brick brick = getBrick(nearestColor(red,green,blue));
	double row = START_ROW - j*ROW_DIFF;
	double col = START_COL - i*COL_DIFF;
	String str = brickString(refID++,brick,row,col);
	out.println(str);
      }
    }
    out.println(footer());
    out.close();
    out("Wrote to " + outFile + "\n");
    out("Done\n");
  }

  private String hex(int i) {
     return Integer.toHexString(i);
  }

  private void out(Object msg) {
    System.out.print(msg);
  }

  protected final void note(Object msg) {
    System.err.println("[" + getClass().getName() + "] " + String.valueOf(msg));
  }

  private static double START_ROW = -16.3999996185302734375;
  private static double ROW_DIFF = START_ROW - -15.6000003814697265625;
  private static double START_COL = 12.3999996185302734375;
  private static double COL_DIFF = START_COL - 11.59999942779541015625;

  private static class Brick {
    final int materials, itemNos;
    Brick(int materials, int itemNos) {
      this.materials = materials;
      this.itemNos = itemNos;
    }
  }

  private Brick getBrick(Color c) {
    return colors2bricks.get(c);
  }
  private final static Map<Color,Brick> colors2bricks = new HashMap<Color,Brick>();
  private static void addColor(int r, int g, int b, int itemNos, int materials) {
    Color c = new Color(r,g,b);
    Brick br = new Brick(materials,itemNos);
    colors2bricks.put(c,br);
  }
  static {
    addColor(21,32,40,300526,26);
    addColor(101,101,101,4211098,199);
    addColor(112,3,16,4209383,154);
    addColor(94,51,0,4211242,192);
    addColor(132,120,78,300505,5);
    addColor(145,82,10,4122456,38);
    addColor(170,128,80,4569624,312);
    addColor(214,124,0,4173805,106);
    addColor(252,204,0,300524,24);
    addColor(13,70,18,4521915,141);
    addColor(115,144,124,4155050,151);
    addColor(34,135,19,300528,28);
    addColor(171,206,0,4122446,119);
    addColor(112,197,232,4619652,322);
    addColor(25,50,94,4255413,140);
    addColor(23,66,130,300523,23);
    addColor(117,151,207,4179830,102);
    addColor(114,131,158,4169428,135);
    addColor(152,152,152,4211389,194);
    addColor(255,255,255,300501,1);
  }

  private static String brickString(int refID, Brick b, double row, double col) {
    String s = "";
    s += "<Brick refID=\"" + refID + 
      "\" designID=\"3005\" itemNos=\"" + b.itemNos + "\">\n";
    s += "<Part refID=\"" + refID + 
      "\" designID=\"3005\" materials=\"" + b.materials + ",0\" decoration=\"0\">\n";
    s += "<Bone refID=\"" + (refID+1) + "\" transformation=\"1,0,0,0,1,0,0,0,1," + 
      row + ",0," + col + "\">";
    s += "</Bone>\n";
    s += "</Part>\n";
    s += "</Brick>\n";
    return s;
  }

  private static String footer() {
    String s = "";
    s += "</Bricks>\n";
    s += "<GroupSystems>\n";
    s += "<GroupSystem>\n";
    s += "</GroupSystem>\n";
    s += "</GroupSystems>\n";
    s += "<BuildingInstructions>\n";
    s += "</BuildingInstructions>\n";
    s += "</LXFML>\n";
    return s;
  }

  private static String header() {
    String s = "";
    s += "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\n";
    s += "<LXFML versionMajor=\"5\" versionMinor=\"0\" name=\"Name\">\n";
    s += "<Meta>\n";
    s += "<Application name=\"LEGO Digital Designer\" versionMajor=\"4\" versionMinor=\"3\"/>\n";
    s += "<Brand name=\"LDD\"/>\n";
    s += "<BrickSet version=\"835.4\"/>\n";
    s += "</Meta>\n";
    s += "<Cameras>\n";
    s += "<Camera refID=\"1\" fieldOfView=\"80\" distance=\"82.5515899658203125\" transformation=\"0.06752951443195343017578125,0,-0.997717320919036865234375,-0.3047949373722076416015625,0.95219457149505615234375,-0.02062974683940410614013671875,0.9500210285186767578125,0.3054922521114349365234375,0.06430123746395111083984375,66.02573394775390625,25.7878704071044921875,17.70816802978515625\"/>\n";
    s += "</Cameras>\n";
    s += "<Bricks cameraRef=\"1\">\n";
    return s;
  }

  private static Collection<Color> constantColors() {
    return colors2bricks.keySet();
  }
  
  private Color nearestColor(int r, int g, int b) {
    Color color = new Color(r,g,b);
    Color nearestColor = null;
    double nearestDistance = Double.MAX_VALUE;
    for (Color constantColor : constantColors()) {
      double dist = Math.sqrt(
	Math.pow(color.getRed() - constantColor.getRed(), 2) +
	Math.pow(color.getGreen() - constantColor.getGreen(), 2) +
	Math.pow(color.getBlue() - constantColor.getBlue(), 2));
      if (nearestDistance > dist) {
	nearestColor = constantColor;
	nearestDistance = dist;
      }
    }
    return nearestColor;
  }
  
}
