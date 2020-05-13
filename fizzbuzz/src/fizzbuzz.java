import java.util.Arrays;

public class fizzbuzz {

    public static void main(String args[]) {

        int[] entiers = new int[100];

        for(int i = 0; i < entiers.length; i++) {
            entiers[i] = ;
        }

        for (int i = 0; i < entiers.length; i++) {
            if (entiers[i] % 3 == 0) {
                //entiers[i] = Integer.parseInt("Fizz");
            } else if (entiers[i] % 5 == 0) {
                //entiers[i] = Integer.parseInt("Buzz");
            } else if (entiers[i] % 15 == 0) {
                //entiers[i] = Integer.parseInt("FizzBuzz");
            }
        }

        for (int i = 0; i < entiers.length; i++) {
            System.out.println(Arrays.toString(entiers));
        }
    }
}
