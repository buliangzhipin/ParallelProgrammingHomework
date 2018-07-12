package para;

public class Test {
	public Test() {
		this.id = new Id();
	}
	
	Id id;
	void plusId() {
		synchronized (this) {
			for(int i=0;i<1000000;i++) {
				id.id++;
				if(id.id%100000==0) {
					System.out.println("plus");
				}
			}
		}

	}
	synchronized void  showId() {
		for(int i = 0;i<1000000;i++) {
			if(i%100000==0) {
				System.out.println("minus"+id.id+" "+i);
			}
		}
	}
	
	public static void main(String[] args) {
		new Thread(()->{
			new Thread(()-> {
				while(true) {
					System.out.println("233");
				}
			}).start();
			
			try {
				Thread.sleep(1000);
				System.out.println("finished");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();
		
//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	class Id{
		int id; 
		public Id() {
			this.id = 0;
		}
	}

}
