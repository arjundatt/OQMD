package DIC.Test.algoclass;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 6/9/15
 * Time: 5:04 PM
 */
public class EdgeHeap {
    private Edge[] heapArray;
    private int lastPos;

    public EdgeHeap(int size) {
        heapArray = new Edge[size];
        lastPos = 0;
    }

    private int getParentPos(int n) {
        if (n <= 0)
            return 0;
        else if (n % 2 == 0)
            return n / 2 - 1;
        else
            return n / 2;
    }

    private int getFirstChildPos(int n) {
        if ((2 * n + 1) > lastPos)
            return n;
        else return 2 * n + 1;
    }

    public void insert(Edge n) {
        heapArray[lastPos] = n;
        int currentPos = lastPos++;

        //bubble up
        while (heapArray[getParentPos(currentPos)].compareTo(heapArray[currentPos]) > 0 && currentPos > 0)
            currentPos = swap(currentPos);
    }

    private int swap(int currentPos) {
        int parentPos = getParentPos(currentPos);
        Edge temp = heapArray[parentPos];
        heapArray[parentPos] = heapArray[currentPos];
        heapArray[currentPos] = temp;
        return parentPos;
    }

    public Edge extractMin() {
        int currentPos = 0;
        Edge min = heapArray[currentPos];
        lastPos--;
        if (lastPos >= 0) {
            heapArray[currentPos] = heapArray[lastPos];

            //bubble down
            int minChildPos;
            if (heapArray[getFirstChildPos(currentPos) + 1] == null)
                minChildPos = getFirstChildPos(currentPos);
            else
                minChildPos = heapArray[getFirstChildPos(currentPos)].compareTo(heapArray[getFirstChildPos(currentPos) + 1]) < 0 ? getFirstChildPos(currentPos) : getFirstChildPos(currentPos) + 1;
            while (heapArray[currentPos].compareTo(heapArray[minChildPos]) > 0) {
                swap(minChildPos);
                currentPos = minChildPos;
                if (heapArray[getFirstChildPos(currentPos) + 1] == null)
                    minChildPos = getFirstChildPos(currentPos);
                else
                    minChildPos = heapArray[getFirstChildPos(currentPos)].compareTo(heapArray[getFirstChildPos(currentPos) + 1]) < 0 ? getFirstChildPos(currentPos) : getFirstChildPos(currentPos) + 1;
            }
        }
        return min;
    }

    public static void main(String[] args) {
        EdgeHeap edgeHeap = new EdgeHeap(5);
        edgeHeap.insert(new Edge(1, 2, 4));
        edgeHeap.insert(new Edge(1, 2, 3));
        edgeHeap.insert(new Edge(1, 2, 8));
        edgeHeap.insert(new Edge(1, 2, 2));
        System.out.println(edgeHeap.extractMin().weight);
        System.out.println(edgeHeap.extractMin().weight);
        System.out.println(edgeHeap.extractMin().weight);
        System.out.println(edgeHeap.extractMin().weight);
    }
}
