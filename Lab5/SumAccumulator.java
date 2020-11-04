/**
 * Tyler Baylson
 * Dillon Gorlseky
 * Scott Barlowe
 * 11/03/20
 * Support file inspired by https://www.cs-book.com/OS9/java-dir/4.pdf
 */
public class SumAccumulator{
    private int sum;

    /**
     * The purpose of this function is to get the sum 
     * @return int: Total sum
     */
    public int getSum(){
        return sum;
    }

    /**
     * This function's purpose is to increment the sum 
     * @param int: New sum to be added
     */
    public void setSum(int sum){
        this.sum += sum;
    }
}
