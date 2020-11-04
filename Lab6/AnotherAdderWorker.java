/**
 * Tyler Baylson
 * Dillon Gorlseky
 * Scott Barlowe
 * 11/03/20
 * Additional AdderWorker for thread exercise.
 */
public class AnotherAdderWorker implements Runnable{

    private int sum;//Sum we increment
    private int ID;//Id of thread

    private Thread thread;//New thread we create
     
    /**
     * The purpose of this constructor is to set the id and sum variables
     * @param id
     */
    public AnotherAdderWorker(int id){
        this.ID = id;
        sum = 0;
    }

    /**
     * The purpose of this function is to create a new thread based off the 
     * current thread, then continue to increment sum until the thread is 
     * interrupted.
     */
    public void run(){
        this.thread = this.thread.currentThread();

        while(! this.thread.isInterrupted()){
            sum++;
        }

        System.out.println(sum);
   }
}