import java.util.Random;

public class FibonacciHeapTest {
    public static void main(String[] args) {
        FibonacciHeap heap = new FibonacciHeap();
        Random random = new Random();
        int nodeCount = 10000;
        FibonacciHeap.HeapNode[] nodes = new FibonacciHeap.HeapNode[nodeCount];

        // Insert a large number of nodes
        System.out.println("Inserting nodes...");
        for (int i = 0; i < nodeCount; i++) {
            int key = random.nextInt(nodeCount * 10) + 1;
            nodes[i] = heap.insert(key, "Node" + i);
        }

        System.out.println("Heap size after insertion: " + heap.size());
        System.out.println("Number of trees: " + heap.numTrees());
        System.out.println("Minimum key: " + heap.findMin().key);

        // Perform a series of deleteMin operations
        System.out.println("Deleting minimum nodes...");
        for (int i = 0; i < nodeCount / 10; i++) { // DeleteMin for 10% of nodes
            heap.deleteMin();
        }

        System.out.println("Heap size after deleting min: " + heap.size());
        System.out.println("Number of trees: " + heap.numTrees());
        System.out.println("New minimum key: " + (heap.findMin() != null ? heap.findMin().key : "Heap is empty"));

        // Perform decreaseKey operations
        System.out.println("Decreasing keys...");
        for (int i = 0; i < nodeCount / 20; i++) { // Decrease key for 5% of nodes
            int index = random.nextInt(nodeCount);
            if (nodes[index] != null) {
                int newKey = Math.max(1, nodes[index].key - random.nextInt(50));
                heap.decreaseKey(nodes[index], nodes[index].key - newKey);
            }
        }

        System.out.println("Heap size after decreaseKey: " + heap.size());
        System.out.println("Minimum key after decreaseKey: " + (heap.findMin() != null ? heap.findMin().key : "Heap is empty"));

        // Perform cascading cuts and verify the heap
        System.out.println("Deleting random nodes to test cascading cuts...");
        for (int i = 0; i < nodeCount / 20; i++) { // Delete 5% of nodes
            int index = random.nextInt(nodeCount);
            if (nodes[index] != null) {
                heap.delete(nodes[index]);
                nodes[index] = null;
            }
        }

        System.out.println("Heap size after deleting random nodes: " + heap.size());
        System.out.println("Number of trees: " + heap.numTrees());
        System.out.println("Minimum key: " + (heap.findMin() != null ? heap.findMin().key : "Heap is empty"));

        // Test melding two heaps
        System.out.println("Testing meld operation...");
        FibonacciHeap anotherHeap = new FibonacciHeap();
        for (int i = 0; i < nodeCount / 2; i++) {
            anotherHeap.insert(random.nextInt(nodeCount * 10) + 1, "Heap2-Node" + i);
        }

        heap.meld(anotherHeap);

        System.out.println("Heap size after melding: " + heap.size());
        System.out.println("Number of trees after melding: " + heap.numTrees());
        System.out.println("Minimum key after melding: " + (heap.findMin() != null ? heap.findMin().key : "Heap is empty"));

        // Visualize the heap structure
        System.out.println("Visualizing heap...");
        heap.printHeap();

        // Output statistics
        System.out.println("Total links performed: " + heap.totalLinks());
        System.out.println("Total cuts performed: " + heap.totalCuts());
    }
}
