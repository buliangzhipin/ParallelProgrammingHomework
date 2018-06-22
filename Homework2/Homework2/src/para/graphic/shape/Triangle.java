package para.graphic.shape;

import para.graphic.target.Target;

/**
 * 三角形
 */
public class Triangle extends Shape {

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


	/**
	 * 円を生成する
	 * 
	 * @param id
	 *            識別子
	 * @param x1
	 *            三角形点1のx座標
	 * @param y1
	 *            三角形点1のy座標
	 * @param x2
	 *            三角形点2のx座標
	 * @param y2
	 *            三角形点2のy座標
	 * @param x3
	 *            三角形点3のx座標
	 * @param y3
	 *            三角形点3のy座標
	 */
	public Triangle(int id, int x1, int y1, int x2, int y2, int x3, int y3) {
		this(id, x1, y1, x2, y2, x3, y3, null);
	}
	
	
	/**
	 * 円を生成する
	 * 
	 * @param id
	 *            識別子
	 * @param x1
	 *            三角形点1のx座標
	 * @param y1
	 *            三角形点1のy座標
	 * @param x2
	 *            三角形点2のx座標
	 * @param y2
	 *            三角形点2のy座標
	 * @param x3
	 *            三角形点3のx座標
	 * @param y3
	 *            三角形点3のy座標
	 * @param attr
	 *            属性
	 */
	public Triangle(int id, int x1, int y1, int x2, int y2, int x3, int y3, Attribute attr) {
		super(id, compareMin(x1, x2, x3),compareMax(x1, x2, x3), compareMin(y1, y2, y3), compareMax(y1, y2, y3));

		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.x3 = x3;
		this.y3 = y3;
		this.attr = attr;
	}

	private static int compareMin(int x1, int x2, int x3) {
		int num = x1 < x2 ? x1 : x2;
		num = num < x3 ? num : x3;
		return num;
	}

	private static int compareMax(int x1, int x2, int x3) {
		int num = x1 > x2 ? x1 : x2;
		num = num > x3 ? num : x3;
		return num;
	}

	public int getX1() {
		return x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public int getY1() {
		return y1;
	}

	public void setY1(int y1) {
		this.y1 = y1;
	}

	public int getX2() {
		return x2;
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}

	public int getY2() {
		return y2;
	}

	public void setY2(int y2) {
		this.y2 = y2;
	}

	public int getX3() {
		return x3;
	}

	public void setX3(int x3) {
		this.x3 = x3;
	}

	public int getY3() {
		return y3;
	}

	public void setY3(int y3) {
		this.y3 = y3;
	}


	public void setAttr(Attribute attr) {
		this.attr = attr;
	}

	/** 属性を取得する */
	@Override
	public Attribute getAttribute() {
		return attr;
	}

	/**
	 * この三角形を出力する
	 * 
	 * @param target
	 *            出力先
	 */
	@Override
	public void draw(Target target) {
		target.drawTriangle(id, x1, y1, x2, y2, x3, y3, attr);
	}

}
