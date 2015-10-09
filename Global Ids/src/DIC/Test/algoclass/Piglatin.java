package DIC.Test.algoclass;

import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 6/27/15
 * Time: 4:30 PM
 */
public class Piglatin {
    private String str;

    public void input() {
        System.out.println("Enter a string");
        Scanner scanner = new Scanner(System.in);
        str = scanner.next().toLowerCase();
    }

    int getFirstVowel(int pos) {
        if (pos < str.length()) {
            System.out.print(str.charAt(pos) + " ");
            if (str.charAt(pos) == 'a' || str.charAt(pos) == 'e' || str.charAt(pos) == 'i' || str.charAt(pos) == 'o' || str.charAt(pos) == 'u')
                return pos;
            else
                return getFirstVowel(pos + 1);
        }
        return -1;
    }

    public static void main(String[] args) {
        /*System.out.print("\f");
        System.out.println("PIGLATIN");
        System.out.println("//To form Pig Latin words from words beginning with a consonant the pos of the word");
        System.out.println("like hello simply move the consonant or consonant cluster from the pos of the word");
        System.out.println("to the end of the word. Then add the suffix -ay to the end of the word.//");
        Piglatin piglatin = new Piglatin();
        piglatin.input();
        int firstVowel = piglatin.getFirstVowel(0);
        System.out.println(firstVowel);
        if (firstVowel > -1)
            System.out.println(piglatin.str.substring(firstVowel, piglatin.str.length()) + piglatin.str.substring(0, firstVowel) + "ay");
        else
            System.out.println("Piglatin cannot be formed");*/
        Piglatin piglatin = new Piglatin();
        System.out.println(piglatin.recursiveFunction(1));
    }

    public int recursiveFunction(int i) {
        System.out.println("Statement 1 - " + i);
        if (i < 3)
            return recursiveFunction(i + 1);
        System.out.println("Statement 2 - " + i);
        return i;
    }
}
