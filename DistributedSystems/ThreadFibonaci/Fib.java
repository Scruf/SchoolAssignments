import java.math.*;
import java.io.*;
/*
	Implement a working version of compareTo
*/
public class Fib extends Thread{
	//size of the list
	public int n;
	//first argument
	public BigInteger a;
	//second argumennt
	public BigInteger b;
	//array of fibonacci numbers
	public BigInteger F[];
	private Thread output;
	private Thread calc;
	Fib(int n){
		this.n = n;
		this.F = new BigInteger [n];
	}
	public void putValue(int i, BigInteger element){
		F[0] = this.a;
		F[1] = this.b;
		if(F[i]!=null)
			System.out.print("There is an element at this location");
		else{
			F[i] = element;
		}
	}

	public void run(){
		
		Thread compute = new Thread(new Runnable(){
			public void run(){
				
			}
		});
		Thread output = new Thread(new Runnable(){
			
			public void run(){
				System.out.print("I am thread two\n");
			}
		});
		compute.start();
		output.start();
		try{
			compute.join();
			output.join();
		}catch(InterruptedException ex){
			System.err.print(ex);
		}

		
	}




	public static void main(String[] args) {
		if (args.length<3){
			System.err.print("Invalid number of argumnets\n");
			System.exit(0);
		}
		Integer n = 0;
		Integer a = 0;
		Integer b = 0;
		try{
			 n = Integer.parseInt(args[2]);
			 a = Integer.parseInt(args[0]);
			 b = Integer.parseInt(args[1]);
			if(n<0){
				System.err.print("N must be >= 0");
				System.exit(0);
			}
		}catch(Exception ex){
			System.err.print("Invalid arguments type");
			System.exit(0);
		}
		Fib fib = new Fib(n);
		fib.a = BigInteger.valueOf(a);
		fib.b = BigInteger.valueOf(b);
	
		fib.putValue(2,BigInteger.valueOf(5));
	}	
}