package para.graphic.shape;
import java.util.*;

import para.graphic.target.Target;

/** 描画順序をidの順とする図形集合
 */
public class OrderedShapeManager extends ShapeManager{
	final AbstractCollection<Shape> data;
	public OrderedShapeManager() {
		data = TreeSet<Shape>();
	}
	
	  /** 図形の集合を生成する（空集合）.
	   * 継承クラスが初期化に利用する
	   * @param data 集合
	   */
	  OrderedShapeManager(AbstractCollection<Shape> data){
		  super(data);
	  }
	  
//	  @Override
//	  /** 集合内の図形を出力する．
//	   *  @param target 出力装置
//	   */
//	  public synchronized void draw(Target target){
//		  data.stream().sorted((s1,s2)->s1.getID()-s2.getID()).forEach(s->s.draw(target));
//	  }



}
