import java.util.Arrays;

/**
 * FibonacciHeap
 *
 * An implementation of Fibonacci heap over positive integers.
 *
 */
public class FibonacciHeap
{
	public HeapNode min;
	//All added attributes are maintained in O(1) time
	public int size;
	public int linkCounter;
	public int treeCounter;

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
	 * Consolidate the heap
	 */
	public void successiveLinking() {
		if (min == null || min.next == min) {
			return;
		}
		// Create the "bucket" array
		int maxRank = (int) Math.ceil(Math.log(size() + 1)) + 1;
		HeapNode[] rankArray = new HeapNode[maxRank];
		Arrays.fill(rankArray, null);

		HeapNode current = min;
		HeapNode start = min; //exit condition for the do-while loop

		do {
			HeapNode node = current;
			current = current.next; // Move to the next node before modifying the root list

			int currRank = node.rank;
			// Recursively link trees of the same rank until slot available
			while (rankArray[currRank] != null) {
				HeapNode other = rankArray[currRank];

				// Link two trees of the same rank
				HeapNode parent = (node.key < other.key) ? node : other;
				HeapNode child = (parent == node) ? other : node;
				linkTrees(child, parent);
				linkCounter++;
				treeCounter--;

				rankArray[currRank] = null;
				node = parent;
				currRank++;
			}
			rankArray[currRank] = node;

		} while (current != start);
		// Rebuild root list and find the new min
		min = null;
		for (HeapNode node : rankArray) {
			if (node != null) {
				if (min == null || node.key < min.key) {
					min = node;
				}
				// Configure the new root list
				if (min != node)
				{
					//If min is not the current node, add the node to the right of min
					node.next = min.next;
					node.prev = min;
					min.next.prev = node;
					min.next = node;
				}
				else
				{
					//First node in the list, or only node
					node.next = node;
					node.prev = node;
				}
			}
		}
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
		//Remove min from root list and consolidate the heap
		if (min.next.equals(min))
		{
			min = null;
		}
		else
		{
			min.prev.next = min.next;
			min.next.prev = min.prev;
			min = min.next; //Temporary min
			successiveLinking();
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
		return linkCounter;
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
		return treeCounter;
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
	public void printHeap() {
		if (min == null) {
			System.out.println("Heap is empty.");
			return;
		}

		System.out.println("Root list:");
		HeapNode current = min;
		do {
			printSubTree(current, 0);
			current = current.next;
		} while (current != min);
	}

	private void printSubTree(HeapNode node, int depth) {
		if (node == null) return;

		// Indent based on depth
		for (int i = 0; i < depth; i++) {
			System.out.print("  ");
		}

		// Print the node
		System.out.println("Key: " + node.key + ", Rank: " + node.rank);

		// Print children
		HeapNode child = node.child;
		if (child != null) {
			HeapNode temp = child;
			do {
				printSubTree(temp, depth + 1);
				temp = temp.next;
			} while (temp != child);
		}
	}

}
