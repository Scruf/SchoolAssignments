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
		try{
			System.out.printf("Printing element %s ",super.getValue(super.F.length-1));
		}catch(InterruptedException ie){
			ie.printStackTrace();
		}
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
		this.n =  n;
		try{
			super.putValue(0,this.a);
			super.putValue(1,this.b);
		}catch (InterruptedException ie){

		}
		
	}

	@Override
	public void run(){
	
		if(this.a.compareTo(this.b)==1){
			System.err.print("Second argument must be greater than first\n");
			System.exit(0);
		}
			try{
				
			
				BigInteger next = super.getValue(this.n-2).add(super.getValue(this.n-1));
				
				super.putValue(n,next);
			}catch (InterruptedException ie){
				ie.printStackTrace();
			}	
					
		
	}
}



public class Fib{
	
	public BigInteger F[];
	int n;
	Fib(int n){
		if(n<0){
			System.err.print("N must be >= 0\n");
			System.exit(0);
		}else{
			F = new BigInteger[n+1];
			this.n = n;	
		}
		
	
	}
	public synchronized void putValue(int i, BigInteger element)throws InterruptedException{
		if (F[i] == null){
			F[i] = element;
			System.out.printf("Tying to put %s \n",element.toString());
			notifyAll();
		}
		else{
			System.err.printf("Element at position %d exists in the list\n",i);
			System.exit(0);
		}
	}
	public synchronized BigInteger getValue(int i) throws InterruptedException{
		while(F[i]==null){
			System.out.printf("Still stuck in %d-> ",i);
			System.out.println(F[i]);
			wait();
		}
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
		new Thread( new Print(n)).start();
		for(int i=n;i>=0;i--){
			new Thread(new Compute(a,b,n)).start();
		}
	}	
}