/**
 * Tyler Baylson
 * Dillon Gorlseky
 * Scott Barlowe
 * 11/03/20 
 * Scott Barlowe
 * Starter file for thread exercise
 */
import java.util.concurrent.Executors;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
public class MatrixAdder{
    /**
    * The purpose of this function is to initalize a double matrix made up 
    * of random values, then creates new threads and concurrently calls their
    * .start() functions 
    * @param args: Command Line Arguments
    */
    public static void main(String[] args){

        int [][] matrix0 = new int[25][];//Initalize 2d array of size 25

        Random r = new Random();

        for(int i = 0; i < matrix0.length; i++){//Placing random values inside
              matrix0[i] = new int[r.nextInt(50000)];
        }
       
        for(int i = 0; i < matrix0.length; i++){
            for(int j = 0; j < matrix0[i].length; j++){
                matrix0[i][j] = 1;
            }
        }
        
        //Making new ArrayList to hold our threads
        ArrayList<Thread> threadList = new ArrayList<Thread>();
        //New SumAccumulator to keep track of global sum between threads
        SumAccumulator sum = new SumAccumulator();
        AdderWorker add;
        Thread t0;                
        for(int i = 0; i < matrix0.length; i++){
            add = new AdderWorker(sum, matrix0[i], i);
            //Make a new thread
            t0 = new Thread(add);
            //Store thread in threadList
            threadList.add(t0);
        }

        //Starting threads concurrently
        for(Thread t : threadList){
            t.start();
        }

        //Trys to force each thread to wait for the rest to finish so 
        //Sum is printed afterwards
        try{
            for(int i = 0; i < threadList.size(); i++){
                threadList.get(i).join();
            }
        } catch(InterruptedException e){
            System.out.println(e.getMessage());
        }
        System.out.println("SUM: " + sum.getSum());
    }            
}
