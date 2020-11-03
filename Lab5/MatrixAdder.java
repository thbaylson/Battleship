/**
 *  Scott Barlowe
 *  Starter file for thread exercise
 *
 */
import java.util.concurrent.Executors;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
public class MatrixAdder{

    public static void main(String[] args){

        int [][] matrix0 = new int[25][];

        Random r = new Random();

        for(int i = 0; i < matrix0.length; i++){
              matrix0[i] = new int[r.nextInt(50000)];
        }
       
        for(int i = 0; i < matrix0.length; i++){
            for(int j = 0; j < matrix0[i].length; j++){
                matrix0[i][j] = 1;
            }
        }
        
        ArrayList<Thread> threadList = new ArrayList<Thread>();
        SumAccumulator sum = new SumAccumulator();
        AdderWorker add;
        Thread t0;                
        for(int i = 0; i < matrix0.length; i++){
            add = new AdderWorker(sum, matrix0[i], i);
            t0 = new Thread(add);
            //Make a new thread 
            threadList.add(t0);
            //Store thread in threadList

        }

        for(Thread t : threadList){
            t.start();
        }

 
        try{
            for(int i = 0; i < threadList.size(); i++){
                threadList.get(i).join();
            }
        } catch(InterruptedException e){

        }
        System.out.println("SUM: " + sum.getSum());
    }            
}
