package DIC.Test.algoclass;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 5/6/15
 * Time: 8:29 AM
 */
public class DynamicConnectivity {
    int[] nodes;

    public DynamicConnectivity(int n) {
        nodes = new int[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = i;
        }
    }

    public void union(int firstNode, int secondNode) {
        int firstNodeRoot = findRoot(firstNode);
        int secondNodeRoot = findRoot(secondNode);
        if (firstNodeRoot != secondNodeRoot) {
            nodes[firstNodeRoot] = secondNodeRoot;
        }
        System.out.println("Union of " + firstNode + " and " + secondNode);
        displayNodes();
    }

    public boolean find(int firstNode, int secondNode) {
        return (findRoot(firstNode) == findRoot(secondNode));
    }

    private int findRoot(int node) {
        int count = node;
        int root = nodes[count];
        while (root != count) {
            count = root;
            root = nodes[count];
        }
        return root;
    }

    public void displayNodes() {
        for (int i = 0; i < nodes.length; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        for (int node : nodes) {
            System.out.print(node + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        DynamicConnectivity dynamicConnectivity = new DynamicConnectivity(10);
        dynamicConnectivity.union(0, 1);
        dynamicConnectivity.union(0, 2);
        System.out.println(dynamicConnectivity.find(0, 2));
    }
}
