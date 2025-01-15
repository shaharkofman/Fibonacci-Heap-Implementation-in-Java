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
	public HeapNode prevMin;
	//All added attributes are maintained in O(1) time
	public int size;
	public int linkCounter;
	public int treeCounter;
	public int cutCounter;

	//Empty constructor
	public FibonacciHeap(){}

	public HeapNode getFirst() {
		return min;
	}


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

	public HeapNode insertAfter(HeapNode x, HeapNode y) {
		if (y == null) {
			return null;
		}
		if (x == null) {
			min = y;
			size++;
			treeCounter++;
			return y;
		}
		HeapNode temp = x.next;
		x.next = y;
		y.prev = x;
		y.next = temp;
		temp.prev = y;
		size++;
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
		parent.rank++;
		child.mark = false; //In case child wasn't a tree root
		treeCounter--;
		linkCounter++;
	}
	/**
	 * Consolidate the heap

	public void successiveLinking() {
		if (min == null || min.next == min) {
			return; // Edge case: Heap has one or no elements
		}

		// Predefine a sufficiently large size for rankArray
		int maxRank = (int) (10*Math.ceil(Math.log(size() + 1))) + 10;
		HeapNode[] rankArray = new HeapNode[maxRank];
		Arrays.fill(rankArray, null);

		// Step 1: Collect all root nodes in an array
		int rootListSize = treeCounter;
		HeapNode[] rootList = new HeapNode[rootListSize];
		HeapNode current = min;

		for (int i = 0; i < rootListSize; i++) {
			rootList[i] = current;
			current = current.next;
		}

		// Step 2: Process each root node
		for (HeapNode node : rootList) {
			int currRank = node.rank;

			// Recursively link trees of the same rank until an empty slot is found
			while (rankArray[currRank] != null) {
				HeapNode other = rankArray[currRank];

				// Link two trees of the same rank
				HeapNode parent = (node.key < other.key) ? node : other;
				HeapNode child = (parent == node) ? other : node;
				linkTrees(child, parent);
				linkCounter++;
				treeCounter--;

				rankArray[currRank] = null; // Clear the slot
				node = parent; // Update the node to the new tree
				currRank++;
			}
			rankArray[currRank] = node; // Store the final tree in its slot
		}

		// Step 3: Rebuild the root list and determine the new min
		min = null;
		treeCounter = 0;

		for (HeapNode node : rankArray) {
			if (node != null) {
				treeCounter++;
				if (min == null || node.key < min.key) {
					min = node;
				}

				// Add the node to the root list
				if (min != node) {
					node.next = min.next;
					node.prev = min;
					min.next.prev = node;
					min.next = node;
				} else {
					node.next = node;
					node.prev = node;
				}
			}
		}

		// Final safety check
		if (min == null) {
			throw new IllegalStateException("Heap is not empty, but min is null!");
		}
	}
	*/
	public HeapNode[] toBuckets(HeapNode x)
	{
		//Create an array of buckets
		HeapNode[] buckets = new HeapNode[(int) Math.log(size() / Math.log(GOLDEN_RATIO)) + 1];
		Arrays.fill(buckets, null);

		//
		x.prev.next = null;
		while (x != null)
		{
			HeapNode y = x;
			x = x.next;
			while (buckets[y.rank] != null)
			{
				linkTrees(y, buckets[y.rank]);
				y = (y.key < buckets[y.rank].key) ? y : buckets[y.rank];
				if (y.rank > 0) {
					buckets[y.rank - 1] = null; //y's rank has increased
				}
				else {
					buckets[y.rank] = null;
					y.rank++;
				}
				buckets[y.rank] = y;
			}

		}
		return buckets;
	}

	public HeapNode fromBuckets(HeapNode[] buckets)
	{
		treeCounter = 0;
		HeapNode x = null;
		for (int i = 0; i < (int) Math.log(size() / Math.log(GOLDEN_RATIO)) + 1; i++)
		{
			if (buckets[i] != null)
			{
				if (x == null) {
					x = buckets[i];
					x.next = x;
					x.prev = x;
				}
				else
				{
					insertAfter(x, buckets[i]);
					treeCounter++;
					if (buckets[i].key < x.key)
					{
						x = buckets[i];
					}
				}
			}
		}
		return x;
	}

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
		treeCounter--;
		size--;
		//Take care of the children of the min node (if any)
		if (min.child != null)
		{
			/*
			HeapNode current = min.child;
			while (current.parent != null)
			{
				HeapNode next = current.next;
				cut(current, min);
				current = next;
			}
			*/


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

		//Remove min from root list and optionally consolidate the heap
		if (treeCounter == 1)
		{
			min = null;
		}
		else
		{
			min.prev.next = min.next;
			min.next.prev = min.prev;

			//Case: Call came directly
			if (!min.isFakeMin)
			{

				min = min.next; //Temporary min
				consolidate(min);
			}
			//Case: Call came from method delete()
			else
			{
				return;
			}

		}
	}

	/**
	 *
	 * Cut the link between x and its parent y, and make x a root
	 * pre: x.parent = y
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
			return;
		}
		//Check for violation of heap property
		if (x.key < x.parent.key)
		{
			HeapNode y = x.parent;
			cascadingCut(x,y);
		}
	}

	/**
	 * 
	 * Delete the x from the heap.
	 *
	 */
	public void delete(HeapNode x) 
	{
		if (!(x instanceof HeapNode)) {return;}

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
		public int getRank() {
			return rank;
		}
		public boolean getMarked() {
			return mark;
		}
		public HeapNode getParent() {
			return parent;
		}
		public HeapNode getNext() {
			return next;
		}
		public HeapNode getPrev() {
			return prev;
		}
		public HeapNode getChild() {
			return child;
		}
		public int getKey() {
			return key;
		}
	}
	public void printHeap() {
		if (min == null) {
			System.out.println("Heap is empty.");
			return;
		}

		System.out.println("Fibonacci Heap Visualization:");
		HeapNode current = min;
		do {
			printTree(current, 0, true);
			current = current.next;
		} while (current != min);
	}

	private void printTree(HeapNode node, int depth, boolean isRoot) {
		if (node == null) return;

		// Indent based on depth
		for (int i = 0; i < depth; i++) {
			System.out.print("    ");
		}

		// Root nodes are highlighted differently
		if (isRoot) {
			System.out.println(node.key + " (root)");
		} else {
			System.out.println("|-- " + node.key);
		}

		// Recursively print children
		HeapNode child = node.child;
		if (child != null) {
			HeapNode temp = child;
			do {
				printTree(temp, depth + 1, false);
				temp = temp.next;
			} while (temp != child);
		}
	}


}
