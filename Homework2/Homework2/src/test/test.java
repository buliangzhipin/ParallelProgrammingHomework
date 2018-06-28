package test;

public class test extends Thread{
	boolean flag;
	TestThread test2;
	public static void main(String[] args) throws InterruptedException {
		TestThread test2 = new TestThread();
		test thread1 = new test(test2, true);
		test thread2 = new test(test2,false);
		thread2.start();
		thread1.start();

		thread1.join();
		thread2.join();
	}
	
	public test(TestThread test2,boolean flag) {
		this.flag = flag;
		this.test2 = test2;
	}
	@Override
	public void run() {
		super.run();
		if(flag) {
			function1();
		}else {
			function2();
		}
	}
	
	public void function1() {
		test2.function1();
	}
	
	public void function2() {
		test2.function2();
	}


}

class TestThread{
	public TestThread() {
		// TODO Auto-generated constructor stub
	}
	synchronized  void function1() {
		for(long i =0;i< (long)999990000;i++) {
			i--;
		}
	}
	
	synchronized void function2() {
		System.out.println("test2");
	}
}


