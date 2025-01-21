import java.util.Arrays;

/**
 * FibonacciHeap
 *
 * An implementation of Fibonacci heap over positive integers.
 *
 */
public class FibonacciHeap
{
	public static final double GOLDEN_RATIO = (1 + Math.sqrt(5)) / 2;

	public HeapNode min;
	//All added attributes are maintained in O(1) time
	public HeapNode prevMin; //Used in decreaseKey
	public int size;
	public int linkCounter;
	public int treeCounter;
	public int cutCounter;

	//Empty constructor
	public FibonacciHeap(){}

	/**
	 * 
	 * pre: key > 0
	 *
	 * Insert (key,info) into the heap and return the newly generated HeapNode.
	 * Complexity: O(1)
	 *
	 */
	public HeapNode insert(int key, String info) 
	{
		HeapNode newNode = new HeapNode(key, info);

		// If the heap is empty, create a new heap with a single node
		if (min == null) {
			min = newNode;
			size++;
			treeCounter++;
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
		treeCounter++;
		return newNode;
	}
	/**
	 *
	 * Insert y after x, tailored for the consolidate method
	 * Complexity: O(1)
	 *
	 */
	public HeapNode insertAfter(HeapNode x, HeapNode y) {
		if (y == null) {
			return null;
		}
		if (x == null) {
			min = y;
			treeCounter++;
			return y;
		}
		// Insert y after x
		HeapNode temp = x.next;
		x.next = y;
		y.prev = x;
		y.next = temp;
		temp.prev = y;
		treeCounter++;
		return y;
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
	 * Link two trees of the same rank
	 * pre: child.rank = parent.rank
	 * pre: child.key < parent.key
	 * Complexity: O(1)
	 *
	 */
	public void linkTrees(HeapNode child, HeapNode parent)
	{
		//Remove child from root list
		child.prev.next = child.next;
		if (child.next != null)
		{
			child.next.prev = child.prev;
		}

		//Attach child to parent
		if (parent.child == null)
		//If parent has no children, set child as the only child
		{
			parent.child = child;
			child.next = child;
			child.prev = child;
			child.parent = parent;
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
		child.mark = false; //In case child wasn't a tree root
		treeCounter--;
		linkCounter++;
	}
	/*
	 * Put all the trees in the root list into an array, where the index of the array is the rank of the tree.
	 * Complexity: O(n) worst case, O(log n) amortized
	 */
	public HeapNode[] toBuckets(HeapNode x)
	{
		//Create an array of buckets
		HeapNode[] buckets = new HeapNode[(int) Math.ceil(Math.log((size() + 1) / Math.log(GOLDEN_RATIO)) + 10)];
		Arrays.fill(buckets, null);

		x.prev.next = null; //Break the circular linked list

		while (x != null)
		{
			HeapNode y = x;
			x = x.next;

			y.next = y;
			y.prev = y;

			while (buckets[y.rank] != null)
			//Each bucket is occupied by one tree, link if needed
			{
				//Determine which tree is the parent and which is the child and link
				HeapNode child = (y.key >= buckets[y.rank].key) ? y : buckets[y.rank];
				HeapNode parent = (child.equals(y)) ? buckets[y.rank] : y;
				linkTrees(child, parent);
				//To handle correct pointer, y is always the parent
				y = (y.equals(parent)) ? y : buckets[y.rank];
				//Empty current bucket, and proceed to the next rank
				buckets[y.rank] = null;
				y.rank++;

			}
			buckets[y.rank] = y;
			y.mark = false; //In case y wasn't a tree root
		}
		return buckets;
	}
	/*
	 * Put all the trees in the array back into the root list, after linking.
	 * Complexity: O(log n)
	 */
	public HeapNode fromBuckets(HeapNode[] buckets)
	{
		treeCounter = 0; //Reset the number of trees

		//Re-attach roots
		HeapNode x = null;
		for (int i = 0; i < buckets.length; i++)
		{
			if (buckets[i] != null)
			{
				if (x == null) {
					x = buckets[i];
					x.next = x;
					x.prev = x;
					treeCounter++;
				}
				else
				{
					insertAfter(x, buckets[i]);
					if (buckets[i].key < x.key)
					{
						x = buckets[i];
					}
				}
			}
		}
		min = x; //x is the minimum at end of process
		return min;
	}
	/**
	 * Consolidate the heap
	 */
	public HeapNode consolidate(HeapNode x)
	{
		HeapNode[] buckets = toBuckets(x);
		return fromBuckets(buckets);
	}
	/**
	 * 
	 * Delete the minimal item
	 *
	 */
	public void deleteMin()
	{
		//Edge case: Heap is empty
		if (min == null)
		{
			return;
		}
		size--;

		//Take care of the children of the min node (if any)
		if (min.child != null)
		{

			HeapNode current = min.child;
			while (current.parent != null)
			{
				HeapNode next = current.next;
				cut(current, min);
				current = next;
			}
		}

			/*
			//Alternative implementation:
			cutCounter += min.rank;
			treeCounter += min.rank;
			HeapNode firstChild = min.child;
			HeapNode lastChild = min.child.prev;

			//Detach children from min
			HeapNode currentChild = firstChild;
			while (currentChild.parent != null)
			{
				currentChild.parent = null;
				currentChild = currentChild.next;
			}
			//Add children to root list
			min.prev.next = firstChild;
			firstChild.prev = min.prev;

			lastChild.next = min.next;
			min.next.prev = lastChild;

		}
*/
		//Remove min from root list and optionally consolidate the heap
		if (treeCounter == 1)
		{
			min = null;
			treeCounter = 0;
		}
		else
		{
			min.prev.next = min.next;
			min.next.prev = min.prev;

			//Case: Call came directly
			if (!min.isFakeMin)
			{

				//min = min.next; //Temporary min
				consolidate(min.next);
			}
			//Case: Call came from method delete()
			else
			{
				treeCounter --;
				return;
			}

		}
	}

	/**
	 *
	 * Cut the link between x and its parent y, and make x a root
	 * pre: x.parent = y
	 * Complexity: O(1)
	 *
	 */
	public void cut(HeapNode x, HeapNode y)
	{
		x.parent = null;
		x.mark = false;
		y.rank--;
		treeCounter++;
		cutCounter++;

		//If x has no siblings, set y's child pointer to null
		if (x.next.equals(x))
		{
			y.child = null;
		}
		//Otherwise, remove x from sibling list
		else
		{
			y.child = x.next;
			x.prev.next = x.next;
			x.next.prev = x.prev;
		}

		//Add x to the root list
		if (prevMin != null)
		{
			//x was decreased and is now the new min, use prevMin reference to add it to the root list
			x.next = prevMin.next;
			x.prev = prevMin;
			prevMin.next.prev = x;
			prevMin.next = x;
			prevMin = null;
		}
		else
		{
			x.next = min.next;
			x.prev = min;
			min.next.prev = x;
			min.next = x;
		}
	}
	/**
	 *
	 * Perform a cascading - cut process starting at x
	 * pre: x != null, y != null
	 * Complexity: O(1) amortized
	 *
	 */
	public void cascadingCut(HeapNode x, HeapNode y)
	{
		cut(x,y);

		if (y.parent != null)
		{
			//If y is not a root and was not marked, mark it
			if (!y.mark)
			{
				y.mark = true;
			}
			//Otherwise, recursively cut y from its parent
			else
			{
				cascadingCut(y,y.parent);
			}
		}
	}
	/**
	 * 
	 * pre: 0<diff<x.key
	 * 
	 * Decrease the key of x by diff and fix the heap.
	 * Complexity: O(1) amortized
	 * 
	 */
	public void decreaseKey(HeapNode x, int diff) 
	{
		if (min == null || x == null)
		{
			return;
		}
		//Decrease the key of x, check if it's a new minimum
		x.key -= diff;

		//Update the min node if necessary
		if (x.key < min.key)
		{
			prevMin = min; //Reference to the previous min, for potential cascading cut
			min = x;
		}

		//Edge case: x is a root, no need to fix the heap
		if (x.parent == null)
		{
			prevMin = null; //Nullify prevMin reference in case of no violation
			return;
		}
		//Check for violation of heap property
		if (x.key < x.parent.key)
		{
			HeapNode y = x.parent;
			cascadingCut(x,y);
		}
		prevMin = null; //Nullify prevMin reference in case of no violation
	}

	/**
	 * 
	 * Delete the x from the heap.
	 *
	 */
	public void delete(HeapNode x) 
	{
		if (x == null) {return;}

		//If node to delete is already minimum, execute deleteMin
		if (x == min)
		{
			deleteMin();
			return;
		}

		//Else, execute decreaseKey and deleteMin (without consolidation)
		HeapNode trueMin = min; //Save the current min
	    decreaseKey(x, x.key + 1);
		x.isFakeMin = true;
		deleteMin();
		min = trueMin; //Restore the min
	}


	/**
	 * 
	 * Return the total number of links.
	 * 
	 */
	public int totalLinks()
	{
		return linkCounter;
	}


	/**
	 * 
	 * Return the total number of cuts.
	 * 
	 */
	public int totalCuts()
	{
		return cutCounter;
	}


	/**
	 * 
	 * Meld the heap with heap2
	 * Complexity: O(1)
	 *
	 */
	public void meld(FibonacciHeap heap2)
	{
		//Concatenate the root lists
		if (heap2 instanceof FibonacciHeap && heap2.min != null)
		{
			if (min == null)
			{
				min = heap2.min;
			}
			else
			{
				HeapNode temp = min.next;
				min.next = heap2.min.next;
				heap2.min.next.prev = min;
				heap2.min.next = temp;
				temp.prev = heap2.min;
				if (heap2.min.key < min.key)
				{
					min = heap2.min;
				}
			}
			size += heap2.size;
			treeCounter += heap2.treeCounter;
		}
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
		return treeCounter;
	}

	public boolean isEmpty() {
		return min == null;
	}

	public boolean empty() {
		return min == null;
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
		public boolean isFakeMin;

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
			this.isFakeMin = false;
		}

		public int getKey() {
			return key;
		}
	}
	public int potential() {
		int t = 0; // Number of trees (roots)
		int m = 0; // Number of marked nodes
		HeapNode current = min;

		if (current != null) {
			// Traverse the circular linked list of roots
			do {
				t++; // Each root is a tree
				m += countMarkedNodes(current); // Count marked nodes in the tree
				current = current.next;
			} while (current != min);
		}

		return t + 2 * m; // Potential is t + 2 * m
	}

	// Helper method to count marked nodes in a tree
	private int countMarkedNodes(HeapNode node) {
		int count = 0;
		while (node != null) {
			if (node.mark) count++; // Increment if the node is marked
			node = node.child;
		}
		return count;
	}

	public int[] countersRep() {
		int[] counters = new int[calculateMaxRank()]; // Array to store the number of trees of each rank
		HeapNode current = min;

		// Traverse the root list and count trees by rank
		if (current != null) {
			do {
				int rank = current.rank;
				counters[rank]++; // Increment the count of trees of this rank
				current = current.next;
			} while (current != min);
		}

		return counters;
	}

	// Helper method to calculate the maximum possible rank (based on the number of nodes in the heap)
	private int calculateMaxRank() {
		return (int) Math.ceil(Math.log(size) / Math.log(2)) + 1;
	}
}
