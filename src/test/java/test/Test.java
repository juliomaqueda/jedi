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
		while (true) {
			foo = random.nextInt();
			System.out.println(foo);
			
			Thread.sleep(5000);
			
			pintar();
		}
	}

	public void pintar() {
		
		String a = "";
		String b = "sdfdsf";
		
		String c = a + b;
		
		System.out.println("Pinto " + c);
	}
}
