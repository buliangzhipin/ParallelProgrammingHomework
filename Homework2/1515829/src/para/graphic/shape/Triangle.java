package para.graphic.shape;
/**
 * 学籍番号：15B15829
 * 李墨然
 */

import para.graphic.target.Target;

/**
 * 三角形
 */
public class Triangle extends Shape {
	
	
	private int x[];
	private int y[];
	final int nPoints =3;

	/** 属性 */
	private Attribute attr;


	/**
	 * 円を生成する
	 * 
	 * @param id
	 *            識別子
	 * @param x
	 *            三角形3点のx座標
	 * @param y
	 *            三角形3点のy座標

	 */
	public Triangle(int id, int[] x, int[] y, int y3) {
		this(id, x, y, null);
	}
	
	
	/**
	 * 円を生成する
	 * 
	 * @param id
	 *            識別子
	 * @param x
	 *            三角形3点のx座標
	 * @param y
	 *            三角形3点のy座標

	 * @param attr
	 *            属性
	 */
	public Triangle(int id, int[] x, int[] y, Attribute attr) {
		super(id, compareMin(x[0], x[1], x[2]),compareMax(x[0], x[1], x[2]), compareMin(y[0], y[1], y[2]), compareMax(y[0], y[1], y[2]));

		this.x = x;
		this.y = y;
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
		target.drawTriangle(id, x, y, attr);
	}

}
