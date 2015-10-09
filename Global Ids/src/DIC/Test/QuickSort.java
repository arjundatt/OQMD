package DIC.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 3/29/15
 * Time: 5:51 PM
 */
public class QuickSort {
    public static int[] arr = {91, 7, 2, 80, 6, 51, 42, 19};

    public void quickSort(int left, int right) {
        if (left < right) {
            int i = partition(left, right);
            quickSort(left, i - 1);
            quickSort(i + 1, right);
        }
    }

    public int partition(int left, int right) {
        int pivot = arr[left];
        int i = left + 1;     // partition between <p and >p
        for (int j = left + 1; j <= right; j++) {     // partition between partitioned and un partitioned
            if (arr[j] < pivot) {
                // swap the values of j and i
                int temp = arr[j];
                arr[j] = arr[i];
                arr[i] = temp;
                i++;
            }
        }
        arr[left] = arr[i - 1];
        arr[i - 1] = pivot;
        return i - 1;
    }

    public void printArray() {
        for (int num : arr) {
            System.out.print(num + ", ");
        }
    }

    public static void main(String[] args) {
        QuickSort quickSort = new QuickSort();
        quickSort.quickSort(0, QuickSort.arr.length - 1);
        quickSort.printArray();
    }
}
