package DIC.Test.algoclass;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 5/23/15
 * Time: 1:22 PM
 */
public class Heap {
    int[] heapArray;
    int lastPos;

    public Heap(int n) {
        lastPos = 0;
        heapArray = new int[n];
    }

    private int getParentPos(int n) {
        if (n <= 0)
            return 0;
        else if (n % 2 == 0)
            return n / 2 - 1;
        else
            return n / 2;
    }

    protected int getFirstChildPos(int n) {
        if ((2 * n + 1) > lastPos)
            return n;
        else return 2 * n + 1;
    }

    protected int getSecondChildPos(int n) {
        if ((2 * n + 2) > lastPos)
            return n;
        else return 2 * n + 1;
    }

    public void insert(int n) {
        heapArray[lastPos] = n;
        int currentPos = lastPos++;

        //bubble up
        while (heapArray[getParentPos(currentPos)] > heapArray[currentPos])
            currentPos = swap(currentPos);
    }

    private int swap(int currentPos) {
        int parentPos = getParentPos(currentPos);
        int temp = heapArray[parentPos];
        heapArray[parentPos] = heapArray[currentPos];
        heapArray[currentPos] = temp;
        return parentPos;
    }

    public void printHeap() {
        System.out.println("Elements are : ");
        for (int i = 0; i < lastPos; i++) {
            System.out.print(heapArray[i] + ", ");
        }
    }

    public int extractMin() {
        int currentPos = 0;
        int min = heapArray[currentPos];
        lastPos--;
        heapArray[currentPos] = heapArray[lastPos];

        //bubble down
        int minChildPos = heapArray[getFirstChildPos(currentPos)] < heapArray[getFirstChildPos(currentPos) + 1] ? getFirstChildPos(currentPos) : getFirstChildPos(currentPos) + 1;
        while (heapArray[currentPos] > heapArray[minChildPos]) {
            swap(minChildPos);
            currentPos = minChildPos;
            minChildPos = heapArray[getFirstChildPos(currentPos)] < heapArray[getFirstChildPos(currentPos) + 1] ? getFirstChildPos(currentPos) : getFirstChildPos(currentPos) + 1;
        }

        return min;
    }

    public static void main(String[] args) {
        Stock heap = new Stock(10);
        heap.insert(5);
        heap.insert(8);
        heap.insert(4);
        heap.insert(7);
        heap.insert(6);
        heap.insert(3);
        heap.insert(2);
        heap.insert(1);
        heap.insert(9);
        heap.insert(11);
        heap.insert(10);
        heap.insert(15);
        heap.insert(13);
        heap.printHeap();
    }
}

class Stock extends Heap {
    int size;

    Stock(int n) {
        super(n);
        size = n;
    }

    @Override
    public void insert(int newElement) {
        lastPos = lastPos + 1 >= size ? size - 1 : lastPos + 1;

        if (lastPos == size - 1) {
            System.out.println("Extracted element = " + heapArray[0]);
        }
        heapArray[0] = newElement;
        heapify(0);
    }

    private void heapify(int currentPos) {
        int minChildPos = heapArray[getFirstChildPos(currentPos)] < heapArray[getSecondChildPos(currentPos)] ? getFirstChildPos(currentPos) : getSecondChildPos(currentPos);
        if (heapArray[currentPos] > heapArray[minChildPos]) {
            int temp = heapArray[minChildPos];
            heapArray[minChildPos] = heapArray[currentPos];
            heapArray[currentPos] = temp;
            heapify(minChildPos);
        }
    }
}