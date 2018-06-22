package para.graphic.shape;

import para.graphic.target.Target;

/** 
 * 三角形
 */
public class Triangle extends Shape{
	
	  /** x1頂点座標 */
	  private int x1;
	  /** y1頂点座標 */
	  private int y1;
	  /** x2頂点座標 */
	  private int x2;
	  /** y2頂点座標 */
	  private int y2;
	  /** x3頂点座標 */
	  private int x3;
	  /** y3頂点座標 */
	  private int y3;
	  /** 属性 */
	  private Attribute attr;

	  /** 円を生成する
	   *  @param id 識別子
	   *  @param x  中心のx座標
	   *  @param y  中心のy座標
	   *  @param r  半径
	   */
	  public Triangle(int id, int x1, int y1, int x2,int y2, int x3,int y3){
	    this(id, x1, y1, x2,y2,x3,y3, null);
	  }

	  /** 円を生成する
	   *  @param id 識別子
	   *  @param x  中心のx座標
	   *  @param y  中心のy座標
	   *  @param r  半径
	   *  @param attr  属性
	   */
	  public Triangle(int id, int x1, int y1, int x2,int y2,int x3, int y3, Attribute attr){
	    super(id, x1,x1,y1,y1);
	    this.x1 =x1; 
	    this.y1 = y1;
	    this.x2 =x2; 
	    this.y2 = y2;
	    this.x3 =x3; 
	    this.y3 = y3;
	    this.attr = attr;
	  }

	  /** 属性を取得する */
	  @Override
	  public Attribute getAttribute(){
	    return attr;
	  }

	  /** この三角形を出力する
	   *  @param target 出力先
	   */
	  @Override
	  public void draw(Target target){
	    target.drawTriangle(id, x1,y1, x2,y2,x3,y3, attr);
	  }



}
