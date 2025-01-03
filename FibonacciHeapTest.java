import org.junit.Test;
import static org.junit.Assert.*;

public class FibonacciHeapTest {

    @Test
    public void testInsertAndSize() {
        FibonacciHeap heap = new FibonacciHeap();

        // Insert a series of nodes
        heap.insert(10, "ten");
        heap.insert(20, "twenty");
        heap.insert(5, "five");
        heap.insert(15, "fifteen");
        heap.insert(2, "two");

        // Verify size
        assertEquals("Heap size after 5 inserts should be 5", 5, heap.size());

        // Verify min
        assertEquals("Minimum should be 2", 2, heap.findMin().key);

        // Verify numTrees (each insert adds a new tree)
        assertEquals("Number of trees after 5 inserts should be 5", 5, heap.numTrees());

        // Verify totalLinks (no links yet)
        assertEquals("Total links should be 0", 0, heap.totalLinks());

        // Visualize the heap
        System.out.println("Heap after multiple inserts:");
        heap.printHeap();
    }

    @Test
    public void testFindMinAndDeleteMin() {
        FibonacciHeap heap = new FibonacciHeap();

        // Insert nodes
        heap.insert(10, "ten");
        heap.insert(20, "twenty");
        heap.insert(5, "five");
        heap.insert(15, "fifteen");
        heap.insert(2, "two");

        // Verify initial numTrees
        assertEquals("Number of trees after 5 inserts should be 5", 5, heap.numTrees());

        // Visualize the heap
        System.out.println("Heap before deleteMin:");
        heap.printHeap();

        // Delete the minimum node
        heap.deleteMin();
        assertEquals("Minimum after deleting 2 should be 5", 5, heap.findMin().key);
        assertEquals("Heap size after deleting 1 node should be 4", 4, heap.size());

        // Verify numTrees after consolidation
        assertTrue("Number of trees should be less than 5 after deleteMin", heap.numTrees() < 5);

        // Verify totalLinks after deleteMin
        assertTrue("Total links should be greater than 0 after deleteMin", heap.totalLinks() > 0);

        System.out.println("Heap after deleteMin (2):");
        heap.printHeap();
    }

    @Test
    public void testComplexOperations() {
        FibonacciHeap heap = new FibonacciHeap();

        // Insert nodes
        heap.insert(10, "ten");
        heap.insert(20, "twenty");
        heap.insert(5, "five");
        heap.insert(15, "fifteen");
        heap.insert(2, "two");

        // Delete min and insert more nodes
        heap.deleteMin();
        heap.insert(1, "one");
        heap.insert(25, "twenty-five");
        heap.insert(7, "seven");

        // Check min
        assertEquals("Minimum should be 1", 1, heap.findMin().key);

        // Verify numTrees after multiple operations
        assertTrue("Number of trees should be consistent with the operations performed", heap.numTrees() > 0);

        // Verify totalLinks after multiple operations
        assertTrue("Total links should increase after deleteMin and consolidation", heap.totalLinks() > 0);

        // Visualize heap after multiple operations
        System.out.println("Heap after complex operations:");
        heap.printHeap();

        // Perform another deleteMin and validate structure
        heap.deleteMin();
        assertEquals("Minimum should now be 5", 5, heap.findMin().key);

        // Verify numTrees and totalLinks again
        assertTrue("Number of trees should adjust after deleteMin", heap.numTrees() > 0);
        assertTrue("Total links should further increase after another deleteMin", heap.totalLinks() > 0);

        System.out.println("Heap after another deleteMin (1):");
        heap.printHeap();
    }
}
