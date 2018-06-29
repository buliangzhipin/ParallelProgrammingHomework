package para.graphic.shape;
import java.util.*;

import para.graphic.target.Target;

/** 描画順序をidの順とする図形集合
 */
public class OrderedShapeManager extends ShapeManager{
	public OrderedShapeManager() {
//		super(new TreeSet<Shape>());
		super();
	}

	  
	  @Override
	  /** 集合内の図形を出力する．
	   *  @param target 出力装置
	   */
	  public synchronized void draw(Target target){
		  data.stream().sorted((s1,s2)->s1.getID()-s2.getID()).forEach(s->s.draw(target));
	  }



}
