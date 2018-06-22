package para.graphic.target;
import para.graphic.shape.Attribute;
import para.graphic.shape.ShapeManager;

import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import java.util.Base64;

/**
 * 文字出力装置
 */
public class TextTarget implements Target{
  static final int WIDTH=320;
  static final int HEIGHT=240;
  final int width,height;
  final BufferedImage image;
  final Base64.Encoder encoder;
  private DirectAccessByteArrayOutputStream baos = 
    new DirectAccessByteArrayOutputStream();
  protected PrintStream pstream;

  public TextTarget(OutputStream stream){
    this(new PrintStream(stream));
  }

  public TextTarget(PrintStream pstream){
    this(WIDTH, HEIGHT, pstream);
  }

  public TextTarget(int width, int height, OutputStream stream){
    this(width, height, new PrintStream(stream));
  }

  public TextTarget(int width, int height, PrintStream pstream){
    this.width = width;
    this.height = height;
    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    encoder = Base64.getMimeEncoder(76, lineseparator);
    this.pstream = pstream;
  }

  @Override
  public synchronized void clear(){
   pstream.println("target clear");
  }
  @Override
  public synchronized void draw(ShapeManager sm){
    predraw();
    sm.draw(this);
    postdraw();
  }

  @Override
  public synchronized void draw(Target t,ShapeManager sm){
    predraw();
    t.drawCore(sm);
    postdraw();
  }

  @Override
  public void drawCore(final ShapeManager sm){
    draw(sm);
  }

  private void predraw(){
  }

  private void postdraw(){
    pstream.println("target draw");
  }
  @Override
  /**
   描画領域の{@link BufferedImage}を返す。スレッドセーフではなく一般利用は非推奨
   @return 描画領域の{@link BufferedImage}
   */
  public BufferedImage getBufferedImage(){
    return image;
  }
  
  @Override
  public synchronized BufferedImage copyBufferedImage(){
    return new BufferedImage(image.getColorModel(),
                             (WritableRaster)image.getData(),
                             image.isAlphaPremultiplied(),null);
  }
  @Override
  public synchronized BufferedImage copyBufferedImage3Channel(){
    return new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
  }

  @Override
  public void init(){
  }
  
  @Override
  public void finish(){
  }

  @Override
  public synchronized void flush(){
    pstream.println("target flush");
  }

  /** 属性を表示する．
   *  @param attr 属性
   */
  private void print(Attribute attr){
    if(attr!=null){
      pstream.println(" Attribute "+
                      "Color "+attr.getColor()[0]+" "+
                               attr.getColor()[1]+" "+
                               attr.getColor()[2]+" "+
                      "Fill " +attr.getFill()+" "+
                      "Iaux0 "+attr.getIaux0()+" "+
                      "Faux0 "+attr.getFaux0()+" "+
                      "Faux1 "+attr.getFaux1());
    }else{
      pstream.println();
    }
  }

  static int count=0;
  static final byte[] lineseparator = {'\n'};
  private void print(BufferedImage image){
    try {
      baos.reset();
      OutputStream bos = encoder.wrap(baos);
      //Base64OutputStream bos = new Base64OutputStream(baos,true,76,
      //                                                lineseparator);
      ImageIO.write(image, "png", bos); 
      bos.close();
      pstream.write(baos.getBuffer(),0,baos.getCount());
      pstream.flush();
    }catch(IOException e){
      System.err.print(e);
    }
    pstream.print(" &");
  }

  /**
   *  円の情報を文字出力する
   */
  @Override
  public void drawCircle(int id, int x, int y, int r, 
      Attribute attr){
    pstream.print("shape "+id+" Circle "+x+" "+y+" "+r);
    print(attr);
  }

  /** 
   *  矩形の情報を文字出力する
   */
  @Override
  public void drawRectangle(int id, int x, int y, int w, int h, 
      Attribute attr){
    pstream.print("shape "+id+" Rectangle "+x+" "+y+" "+w+" "+h);
    print(attr);
  }

  /** 
   *  画像の情報を文字出力する
   */
  @Override
  public void drawImage(int id, int x, int y, BufferedImage img, 
                        Attribute attr){
    pstream.println("shape "+id+" Image "+x+" "+y);
    print(img);
    print(attr);
  }
  
  /** 
   *  三角形の情報を文字出力する
   */
  @Override
	public void drawTriangle(int id, int x1, int y1, int x2, int y2, int x3, int y3, Attribute attr) {
		// TODO Auto-generated method stub
		
	}

  private class DirectAccessByteArrayOutputStream extends java.io.ByteArrayOutputStream{
    DirectAccessByteArrayOutputStream(){
      super();
    } 
    DirectAccessByteArrayOutputStream(int size){
      super(size);
    } 
    byte[] getBuffer(){
      return buf;
    }
    int getCount(){
      return count;
    }
  }

}
