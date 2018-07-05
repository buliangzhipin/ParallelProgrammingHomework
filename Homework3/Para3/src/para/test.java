package para;

import java.util.Comparator;
import java.util.TreeSet;

public class test {
	public static void main(String[] args) {
		TreeSet<Integer> ts = new TreeSet<>(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				// TODO Auto-generated method stub
				return o1-o2;
			}
		});
		
		ts.add(3);
		ts.add(2);
		ts.add(1);
		ts.add(4);
		
		for(Integer i:ts) {
			System.out.println(i);
		}
	}
}
