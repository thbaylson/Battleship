/**
 * Tyler Baylson
 * Dillon Gorlseky
 * Scott Barlowe
 * 11/03/20 
 * Scott Barlowe
 * 
 * Mostly empty file
 */
public class AdderWorker implements Runnable{
    private SumAccumulator sum;//Keeps track of sum
    private int[] matrix;//Row of 2d array we calculate sum of
    private int id;//Id of thread

    /**
     * The purpose of this constructor is to initialize global variables
     * @param sum: Sum accumulator we increment
     * @param matrix: 2d array we read from
     * @param id: Id of thread we're using
     */
    public AdderWorker(SumAccumulator sum, int[] matrix, int id){
        this.sum = sum;
        this.matrix = matrix;
        this.id = id;
    }

    /**
     * The purpose of this function is to be the function each thread runs when
     * their .start() is called. It calculates the total of that row in the 2d
     * array.
     */
    public void run(){
        int local = 0;
        for(int i = 0; i < this.matrix.length; i++){
            local += this.matrix[i];
        }
        this.sum.setSum(local);//Increments sum total overall
        String temp = String.format("Thread %-15d added     %10d nums --> %10d", 
            this.id, this.matrix.length, local);
        System.out.println(temp);//Prints out formated String message for user
    }
}

        
