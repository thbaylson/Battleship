/**
 *  Scott Barlowe
 * 
 *  Mostly empty file
 */

public class AdderWorker implements Runnable{
    private SumAccumulator sum;
    private int[] matrix;
    private int id;

    public AdderWorker(SumAccumulator sum, int[] matrix, int id){
        this.sum = sum;
        this.matrix = matrix;
        this.id = id;
    }

    public void run(){
        int local = 0;
        for(int i = 0; i < this.matrix.length; i++){
            local += this.matrix[i];
        }
        this.sum.setSum(local);
        String temp = String.format("Thread %-15d added     %10d nums --> %10d", 
            this.id, this.matrix.length, local);
        System.out.println(temp);

    }
}

        
