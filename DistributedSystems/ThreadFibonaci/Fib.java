import java.math.*;
import java.io.*;
/*
	Implement a working version of compareTo
*/
class Print extends Thread{

}
class Compute extends Thread{

}
public class Fib extends Thread{
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
	
	}
	public BigInteger getValue(int i){
		return new BigInteger("0");
	}

	public void run(){
		

		
	}




	public static void main(String[] args) {
		if (args.length<3){
			System.err.print("Invalid number of argumnets\n");
			System.exit(0);
		}
		Integer n = 0;
		try{
			n = Integer.parseInt(args[2]);
		}catch(Exception ex){
			System.err.print("N must be of type int\n");
			System.exit(0);
		}
		Fib fib  = new Fib(n);

	}	
}