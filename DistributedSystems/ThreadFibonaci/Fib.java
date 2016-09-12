import java.math.*;

public class Fib{
	public int n;
	public int F[];
	Fib(int n){
		this.n = n;
		F = new int [n];
	}

	public void main(String[] args) {
		if(args.length<3){
			System.err.print("Number if arguments must account to 3\n");
		}
		Integer a = 0;
		Integer b = 0;
		Integer c = 0;
		try{
			a = Integer.parseInt(args[0]);
			b = Integer.parseInt(args[1]);
			c = Integer.parseInt(args[2]);
		}catch (Exception ex){
			System.err.print("Invalid parameter format\n");
		}
		if (c<0){
			System.err.print("argument C must be greater than 0");
		}
	}
	Fib fib = new Fib(0);
}