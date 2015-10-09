package DIC.Test.algoclass;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 5/24/15
 * Time: 8:44 PM
 */
public class BinarySearchTree {
    Node root;

    public BinarySearchTree(int n) {
        root = new Node(n);
    }

    public void insert(int n) {
        Node node = root;
        boolean flag = true;
        boolean isLeft = true;

        while (flag) {
            if (n <= node.getValue()) {
                if (node.getLeftChild() == null)
                    flag = false;
                else
                    node = node.getLeftChild();
            } else {
                if (node.getRightChild() == null) {
                    isLeft = false;
                    flag = false;
                } else
                    node = node.getRightChild();
            }
        }

        Node newNode = new Node(n);
        newNode.setParent(node);
        if (isLeft)
            node.setLeftChild(newNode);
        else
            node.setRightChild(newNode);
    }

    public Node search(int n) {
        Node node = root;
        while (node != null) {
            if (node.getValue() == n)
                return node;
            else if (n < node.getValue())
                node = node.getLeftChild();
            else
                node = node.getRightChild();
        }
        return null;
    }

    public Node getMin() {
        Node minNode = root;
        while (minNode.getLeftChild() != null)
            minNode = minNode.getLeftChild();
        return minNode;
    }

    public Node getMax() {
        Node maxNode = root;
        while (maxNode.getRightChild() != null)
            maxNode = maxNode.getRightChild();
        return maxNode;
    }

    public Node findPredecessor(int n) {
        //find n
        Node node = search(n);
        //find predecessor node in parents
        if (node.getLeftChild() == null) {
            if (node.getParent() == null) {
                return null;
            } else {
                while (node.getParent() != null) {
                    node = node.getParent();
                    if (node.getValue() < n)
                        return node;
                }
                return null;
            }
        } else {
            node = node.getLeftChild();
            //find max in the left tree
            while (node.getRightChild() != null)
                node = node.getRightChild();
        }
        return node;
    }

    public void inOrderTraversal(Node node) {
        if (node.getLeftChild() != null)
            inOrderTraversal(node.getLeftChild());
        System.out.print(node.getValue() + ", ");
        if (node.getRightChild() != null)
            inOrderTraversal(node.getRightChild());
    }

    public static void main(String[] args) {
        BinarySearchTree binarySearchTree = new BinarySearchTree(10);
        binarySearchTree.insert(5);
        binarySearchTree.insert(34);
        binarySearchTree.insert(8);
        binarySearchTree.insert(3);
        binarySearchTree.insert(100);
        binarySearchTree.insert(43);
        binarySearchTree.insert(35);
        binarySearchTree.insert(2);
        System.out.println(binarySearchTree.search(2));
        System.out.println("Find 8 = " + binarySearchTree.search(8));
        System.out.println("Find 3 = " + binarySearchTree.search(3));
        System.out.println("Min = " + binarySearchTree.getMin().getValue());
        System.out.println("Max = " + binarySearchTree.getMax().getValue());
        System.out.println("Predecessor of 35 = " + binarySearchTree.findPredecessor(35).getValue());
        System.out.println("Predecessor of 8 = " + binarySearchTree.findPredecessor(8).getValue());
        System.out.println("Predecessor of 10 = " + binarySearchTree.findPredecessor(10).getValue());
        System.out.println("Inorder traversal = ");
        binarySearchTree.inOrderTraversal(binarySearchTree.root);
    }
}

class Node {
    private int value;
    private Node leftChild;
    private Node rightChild;
    private Node parent;

    Node(int value) {
        this.value = value;
    }

    Node getParent() {
        return parent;
    }

    Node getLeftChild() {
        return leftChild;
    }

    Node getRightChild() {
        return rightChild;
    }

    int getValue() {
        return value;
    }

    void setParent(Node parent) {
        this.parent = parent;
    }

    void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
    }
}
