/**
 * FibonacciHeap
 *
 * An implementation of Fibonacci heap over positive integers.
 *
 */
public class FibonacciHeap
{
	public HeapNode min;
	public int size;

	/**
	 * 
	 * pre: key > 0
	 *
	 * Insert (key,info) into the heap and return the newly generated HeapNode.
	 *
	 */
	public HeapNode insert(int key, String info) 
	{
		HeapNode newNode = new HeapNode(key, info);

		// If the heap is empty, create a new heap with a single node
		if (min == null) {
			min = newNode;
			size++;
			return min;
		}
		//Else, insert the new node to the right of the min node
		newNode.next = min.next;
		newNode.prev = min;
		min.next.prev = newNode;
		min.next = newNode;

		//If the new node is smaller than the min node, update the min node
		if (newNode.key < min.key) {
			min = newNode;
		}
		size++;
		return newNode;
	}
	/**
	 *
	 * Link two trees of the same rank
	 * pre: child.rank = parent.rank
	 * pre: child.key < parent.key
	 *
	 */
	public void linkTrees(HeapNode child, HeapNode parent)
	{
		//Remove child from root list
		child.prev.next = child.next;
		child.next.prev = child.prev;

		//Attach child to parent
		if (parent.child == null)
			//If parent has no children, set child as the only child
		{
			parent.child = child;
			child.next = child;
			child.prev = child;
		}
		else
			//If parent has children, add child to the left of parent child pointer
		{
			child.next = parent.child;
			child.prev = parent.child.prev;
			parent.child.prev.next = child;
			parent.child.prev = child;
			child.parent = parent;
		}
		parent.rank++;
		child.mark = false; //In case child wasn't a tree root
	}
	/**
	 * 
	 * Return the minimal HeapNode, null if empty.
	 *
	 */
	public HeapNode findMin()
	{
		return min;
	}

	/**
	 * 
	 * Delete the minimal item
	 *
	 */
	public void deleteMin()
	{
		return; // should be replaced by student code

	}

	/**
	 * 
	 * pre: 0<diff<x.key
	 * 
	 * Decrease the key of x by diff and fix the heap. 
	 * 
	 */
	public void decreaseKey(HeapNode x, int diff) 
	{    
		return; // should be replaced by student code
	}

	/**
	 * 
	 * Delete the x from the heap.
	 *
	 */
	public void delete(HeapNode x) 
	{    
		return; // should be replaced by student code
	}


	/**
	 * 
	 * Return the total number of links.
	 * 
	 */
	public int totalLinks()
	{
		return 0; // should be replaced by student code
	}


	/**
	 * 
	 * Return the total number of cuts.
	 * 
	 */
	public int totalCuts()
	{
		return 0; // should be replaced by student code
	}


	/**
	 * 
	 * Meld the heap with heap2
	 *
	 */
	public void meld(FibonacciHeap heap2)
	{
		return; // should be replaced by student code   		
	}

	/**
	 * 
	 * Return the number of elements in the heap
	 *   
	 */
	public int size()
	{
		return this.size; //
	}


	/**
	 * 
	 * Return the number of trees in the heap.
	 * 
	 */
	public int numTrees()
	{
		return 0; // should be replaced by student code
	}

	/**
	 * Class implementing a node in a Fibonacci Heap.
	 *  
	 */
	public static class HeapNode{
		public int key;
		public String info;
		public HeapNode child;
		public HeapNode next;
		public HeapNode prev;
		public HeapNode parent;
		public int rank;
		public boolean mark;

		public HeapNode(int key, String info)
		{
			this.key = key;
			this.info = info;
			this.child = null;
			this.next = this; //Doubly circular linked list
			this.prev = this;
			this.parent = null;
			this.rank = 0;
			this.mark = false;
		}
	}
}
