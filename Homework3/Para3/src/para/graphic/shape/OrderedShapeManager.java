package para.graphic.shape;
import java.util.*;

import para.graphic.target.Target;

/** 描画順序をidの順とする図形集合
 */
public class OrderedShapeManager extends ShapeManager{
<<<<<<< HEAD
//	final AbstractCollection<Shape> data;
	public OrderedShapeManager() {
//		data = TreeSet<Shape>();
		super();
=======
	public OrderedShapeManager() {
		super(new TreeSet<Shape>(new Comparator<Shape>() {
			public int compare(Shape s1, Shape s2) {
				return (s1.getID() - s2.getID());
			};
		}));
>>>>>>> f7a30c1d919021709cbfbc4ff91d94fd79683be4
	}

	  
	  @Override
	  /** 集合内の図形を出力する．
	   *  @param target 出力装置
	   */
	  public synchronized void draw(Target target){
		  data.stream().sorted(((s1,s2)->s1.getID()-s2.getID()).forEach(s->s.draw(target));
	  }



}
