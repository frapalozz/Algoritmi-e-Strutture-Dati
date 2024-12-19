public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        int fibSearch = 26;
        int[] memo = new int[fibSearch+1];

            System.out.println(fib(fibSearch, memo));
    }

    private static int fib(int n, int[] memo){
        int result = 0;
        if(memo[n] != 0) 
            return memo[n];
        
        if (n == 1 || n == 2)
            result = 1;
        else    
            result =  fib(n-1, memo) + fib(n-2, memo);
        
        memo[n] = result;
        return result;
    }
}
