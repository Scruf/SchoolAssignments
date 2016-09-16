
import java.math.BigInteger;

/**
 * main method. read the inputs from command line and then provide a new thread to output and calculate every one field.
 * @author Egor Kozitski
 */
public class Fib {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.printf("%d arguments supplied but 3 required\n",args.length);
            System.exit(0);
        }else {
        	BigInteger a = new BigInteger("0");
        	BigInteger b = new BigInteger("1");
        	int n = 0;
            try{
            	a = new BigInteger(args[0]);
            	b = new BigInteger(args[1]);
            	n = Integer.valueOf(args[2]);
            }
            catch(NumberFormatException num){
            	System.err.print("Arguments must be of type Int\n");
            	System.exit(0);
            }
           Monitor monitor = new Monitor(n + 1);
        new OutputThread(monitor, n).start();
        for(int index = n; index >= 0; index--) {
            if (index == 0) {
                new CalculateFirstNumbersThread(monitor, index, a).start();
            } else if (index == 1) {
                new CalculateFirstNumbersThread(monitor, index, b).start();
            } else {
                new CalculateThread(monitor, index).start();
            }
        }

        }

    }
}

/**
 * print the result
 */
class OutputThread extends Thread {
    volatile Monitor monitor;
    int index;

    public OutputThread(Monitor m, int index) {
        this.monitor = m;
        this.index = index;
    }
    @Override
    public void run() {
        while (monitor.getValue(index) == null) {
            try {
                sleep(1);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println(monitor.getValue(index));

    }
}

/**
 * calculate the number in according index
 */
class CalculateThread extends Thread {
    volatile Monitor monitor;
    int index;
    BigInteger number;

    public CalculateThread(Monitor m, int index) {
        this.monitor = m;
        this.index = index;
    }

    @Override
    public void run() {

        while (monitor.getValue(index - 1) == null || monitor.getValue(index - 2) == null) {
             try {
                 sleep(1);
             } catch (InterruptedException ex) {
                 ex.printStackTrace();
             }
        }

        monitor.putValue(index, monitor.getValue(index - 1).add(monitor.getValue(index - 2)));
    }
}

/**
 * calculate first two numbers in the array
 */
class CalculateFirstNumbersThread extends Thread {
    private Monitor monitor;
    private int index;
    private BigInteger value;

    public CalculateFirstNumbersThread(Monitor m, int i, BigInteger v) {
        this.monitor = m;
        this.index = i;
        this.value = v;
    }

    @Override
    public void run() {
        monitor.putValue(index, value);
    }
}



class Monitor {
    volatile BigInteger[] F;

    /**
     * initializes the monitor to calculate the generalized Fibonacci number for argument n
     * @param n size of the list
     */
    public Monitor(int n) {
        F = new BigInteger[n];
    }

    /**
     * puts the given value into array element i
     * @param i index of the element where  element will be put
     * @param value value which will be putted into the array
     */
    synchronized void putValue(int i, BigInteger value) {
        if (F[i] == null) {
            F[i] = value;
        } else {
            System.err.println("A value has already been put into array element i");
        }
    }

    /**
     * @param i index of the element to be returned
     * @return the value stored in array element i
     */
    BigInteger getValue(int i) {
        return F[i];
    }


}