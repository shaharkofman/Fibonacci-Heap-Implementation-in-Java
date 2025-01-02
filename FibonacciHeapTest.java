import org.junit.Test;
import static org.junit.Assert.*;

public class FibonacciHeapTest {

    @Test
    public void testInsertAndSize() {
        FibonacciHeap heap = new FibonacciHeap();
        assertEquals("Initial heap size should be 0", 0, heap.size());

        FibonacciHeap.HeapNode node1 = heap.insert(10, "ten");
        assertNotNull("Insert should return a node", node1);
        assertEquals("Heap size after 1 insert should be 1", 1, heap.size());

        FibonacciHeap.HeapNode node2 = heap.insert(20, "twenty");
        assertNotNull("Insert should return a node", node2);
        assertEquals("Heap size after 2 inserts should be 2", 2, heap.size());

        FibonacciHeap.HeapNode node3 = heap.insert(5, "five");
        assertNotNull("Insert should return a node", node3);
        assertEquals("Heap size after 3 inserts should be 3", 3, heap.size());
    }

    @Test
    public void testFindMinAndDeleteMin() {
        FibonacciHeap heap = new FibonacciHeap();

        heap.insert(10, "ten");
        heap.insert(20, "twenty");
        heap.insert(5, "five");

        assertEquals("Minimum should be 5", 5, heap.findMin().key);

        heap.deleteMin();
        assertEquals("Minimum after deleting 5 should be 10", 10, heap.findMin().key);
        assertEquals("Heap size after 1 deleteMin should be 2", 2, heap.size());

        heap.deleteMin();
        assertEquals("Minimum after deleting 10 should be 20", 20, heap.findMin().key);
        assertEquals("Heap size after 2 deleteMin should be 1", 1, heap.size());

        heap.deleteMin();
        assertNull("Minimum should be null for an empty heap", heap.findMin());
        assertEquals("Heap size should be 0 after all elements are deleted", 0, heap.size());
    }
}
