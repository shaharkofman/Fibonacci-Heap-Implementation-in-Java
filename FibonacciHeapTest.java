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

        System.out.println("Heap after inserts:");
        heap.printHeap();

        heap.deleteMin();
        System.out.println("Heap after deleteMin:");
        heap.printHeap();

        heap.deleteMin();
        System.out.println("Heap after second deleteMin:");
        heap.printHeap();

        heap.deleteMin();
        System.out.println("Heap after all deleteMin:");
        heap.printHeap();
    }
}

