public class FibonacciHeapTest {
    public static void main(String[] args) {
        FibonacciHeap heap = new FibonacciHeap();

        // Insert multiple keys into the heap
        System.out.println("Building a large heap with 12 nodes...");
        FibonacciHeap.HeapNode[] nodes = new FibonacciHeap.HeapNode[12];
        int[] keys = {50, 40, 30, 60, 70, 20, 80, 10, 90, 100, 5, 25};

        for (int i = 0; i < keys.length; i++) {
            nodes[i] = heap.insert(keys[i], "Node " + keys[i]);
        }

        System.out.println("Heap after initial insertions:");
        heap.printHeap();

        // Step 1: Perform deleteMin
        System.out.println("\nStep 1: Performing deleteMin...");
        heap.deleteMin();

        System.out.println("Heap after deleteMin:");
        heap.printHeap();

        // Step 2: Decrease key of node with initial key 60 to 10
        System.out.println("\nStep 2: Decreasing key of node with initial key 60 to 10...");
        heap.decreaseKey(nodes[3], 50); // 60 becomes 10

        System.out.println("Heap after decreasing key of 60 to 10:");
        heap.printHeap();

        // Step 3: Decrease key of node with initial key 50 to 5
        System.out.println("\nStep 3: Decreasing key of node with initial key 50 to 5...");
        heap.decreaseKey(nodes[0], 45); // 50 becomes 5

        System.out.println("Heap after decreasing key of 50 to 5:");
        heap.printHeap();

        // Step 4: Perform deleteMin again
        System.out.println("\nStep 4: Performing deleteMin again...");
        heap.deleteMin();

        System.out.println("Heap after second deleteMin:");
        heap.printHeap();

        // Final state
        System.out.println("\nFinal state of the heap:");
        heap.printHeap();
    }
}
