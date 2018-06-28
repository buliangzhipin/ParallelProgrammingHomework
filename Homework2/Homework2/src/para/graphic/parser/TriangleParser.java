package para.graphic.parser;

/**
 * 学籍番号：15B15829
 * 李墨然
 */

import java.util.Scanner;

import para.graphic.shape.Attribute;
import para.graphic.shape.Circle;
import para.graphic.shape.Triangle;

class TriangleParser implements ShapeParser{
	TriangleParser(){
	  }
	  @Override
	  public Triangle parse(Scanner s, int id){
		int x[] = new int[3];
		int y[] = new int[3];
		for(int i = 0;i<3;i++) {
			x[i] = s.nextInt();
			y[i] = s.nextInt();
		}
	    Triangle ret;
	    Attribute attr=null;
	    if(s.hasNext("Attribute")){
	      attr = AttributeParser.parse(s);
	    }
	    ret = new Triangle(id,x,y,attr);
	    return ret;
	  }
}
