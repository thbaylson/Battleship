/**
 * Tyler Baylson
 * Dillon Gorlseky
 * Scott Barlowe
 * 11/03/20
 * Mostly empty file for thread exercise
 */
import java.util.*;

public class ThreadInterrupted{

    /**
     * The purpose of this function is to create two threads each with their
     * own AnotherAdderWorker objects, start them, then periodically make 
     * them sleep for a set amount of time before interrupting them.
     */
    public static void main(String[] args){
        AnotherAdderWorker worker1 = new AnotherAdderWorker(0);
        AnotherAdderWorker worker2 = new AnotherAdderWorker(1);

        Thread thread1 = new Thread(worker1);
        Thread thread2 = new Thread(worker2);

        thread1.start();
        thread2.start();

        try{
            for(int i = 0; i < 5; i++){
                thread1.sleep(10*i);
                thread2.sleep(10*i);
            }
        }catch(InterruptedException ine){
            //System.out.println("Inturrupted");
        }

        thread1.interrupt();
        thread2.interrupt();

    }            
}