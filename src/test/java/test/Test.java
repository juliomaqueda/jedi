package test;

import java.util.Random;

public class Test implements ITest{

	private Random random = new Random();

	private int foo;

	public static void main(String[] args) throws InterruptedException {
		Test test = new Test();
		test.run();
	}
	
	public void run() throws InterruptedException {
		for (int i = 0; i < 100; i++) {
			foo = random.nextInt();
			System.out.println(foo);
			
			Thread.sleep(10000);
			
			pintar();
		}
	}

	public void pintar() {
		
	}
}
