package para.graphic.parser;

import java.util.Scanner;

import para.graphic.shape.Attribute;
import para.graphic.shape.Circle;
import para.graphic.shape.Triangle;

public class TriangleParser implements ShapeParser{
	TriangleParser(){
	  }
	  @Override
	  public Triangle parse(Scanner s, int id){
	    int x1 = s.nextInt();
	    int y1 = s.nextInt();
	    int x2 = s.nextInt();
	    int y2 = s.nextInt();
	    int x3 = s.nextInt();
	    int y3 = s.nextInt();
	    Triangle ret;
	    Attribute attr=null;
	    if(s.hasNext("Attribute")){
	      attr = AttributeParser.parse(s);
	    }
	    ret = new Triangle(id,x1,y1,x2,y2,x3,y3,attr);
	    return ret;
	  }
}
