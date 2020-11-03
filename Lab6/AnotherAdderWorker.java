/**
 * Tyler Baylson
 * Dillon Gorlseky
 * Scott Barlowe
 * 11/03/20
 * Additional AdderWorker for thread exercise.
 */
public class AnotherAdderWorker implements Runnable{

    private int sum;
    private int ID;

    private Thread thread;
     
    public AnotherAdderWorker(int id){
        this.ID = id;
        sum = 0;
    }

    public void run(){
        this.thread = this.thread.currentThread();

        while(! this.thread.isInterrupted()){
            sum++;
        }

        System.out.println(sum);
   }
}