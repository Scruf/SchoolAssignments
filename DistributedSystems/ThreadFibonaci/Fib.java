import java.math.*;
import java.io.*;
/*
	Implement a working version of compareTo
*/

class Print extends Fib implements Runnable{
	Print(int n){
		super(n);
		
	}

	@Override
	public void run(){
		System.out.print("Some things are happens\n");
		

	}
}



class Compute extends Fib implements Runnable{
	BigInteger a;
	BigInteger b;
	int n;
	Compute (Integer a, Integer b,int n){
		super(n);
		this.a = BigInteger.valueOf(a);
		this.b = BigInteger.valueOf(b);
		
		super.F[0] = this.a;
		super.F[1] = this.b;
		
		if(this.a.compareTo(this.b)==1){
			System.err.print("Second argument must be greater than first\n");
			System.exit(0);
		}
		
	}

	@Override
	public void run(){
		synchronized(super.F){
			for (int i=2;i<super.F.length;i++){
				super.F[i] = super.F[i-1].add(super.F[i-2]);
			}
			System.out.print(super.F[super.F.length-1]);
		}
		
	}
}



public class Fib{
	//size of the list
	public BigInteger F[];
	Fib(int n){
		if(n<0){
			System.err.print("N must be >= 0\n");
			System.exit(0);
		}else{
			F = new BigInteger[n];	
		}
		
	
	}
	public void putValue(int i, BigInteger element){
		if (F[i]==null)
			F[i] = element;
		else{
			System.err.printf("Element at position %d exists in the list\n",i);
			System.exit(0);
		}
	}
	public BigInteger getValue(int i){
		return F[i];
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
		}catch(Exception ex){
			System.err.print("Invalid type of command line argumnents \n");
			System.exit(0);
		}
		Fib fib  = new Fib(n);
		// Compute comp = new Compute(a,b,n);
		new Thread(new Compute(a,b,n)).start();
		Print print = new Print(n);
		
	}	
}