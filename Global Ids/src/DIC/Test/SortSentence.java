package DIC.Test;

import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 8/31/15
 * Time: 11:05 AM
 */
public class SortSentence {
    String str;
    String arr[];

    public void input() {

        Scanner sc = new Scanner(System.in);
        System.out.println("INPUT: ");
        str = sc.nextLine();
        str = str + " ";
    }//end of input method.

    public void stringToArray() {

        int countWords = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ' ')
                countWords++;
        }
        arr = new String[countWords];
        int start = 0, c = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ' ') {
                arr[c++] = str.substring(start, i);
                start = i + 1;
            }
        }
    }

    public void sortArray() {

        int j;
        boolean flag = true;
        String temp;
        while (flag) {
            flag = false;
            for (j = 0; j < arr.length - 1; j++) {
                if (arr[j].length() > arr[j + 1].length()) {
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    flag = true;
                }
            }
        }
       /* char i = arr[0].charAt(0);

        i = Character.toUpperCase(arr[0].charAt(0));
        System.out.println(i );
        /arr[0].charAt(0) = i;*/
    }

    public void display() {

        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.print('.');
    }

    public static void main(String[] args) {

        SortSentence ob = new SortSentence();
        ob.input();
        ob.stringToArray();
        ob.sortArray();
        ob.convertFirstCharactersToCapitalCase();
        ob.display();
    }

    private void convertFirstCharactersToCapitalCase() {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr[i].substring(0, 1).toUpperCase() + arr[i].substring(1);
        }
    }
}

