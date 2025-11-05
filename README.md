## 💻 README: Fibonacci Heap (Java Implementation)

This repository contains a Java implementation of a **Fibonacci Heap** data structure, optimized for minimum-finding operations.

### 👥 Authors

* **Shahar Kofman** (`shaharkofman`)
* **Saar Guy** (`saarguy`)

### 📝 Project Overview

The `FibonacciHeap` class maintains a collection of heap-ordered trees and supports standard heap operations with optimal complexity guarantees (amortized analysis).

#### Key Attributes

| Attribute | Description |
| :--- | :--- |
| **`min`** | Pointer to the root node with the minimum key in the entire heap. |
| **`size`** | Total number of elements in the heap. |
| **`treeCounter`** | Total number of trees in the root list. |
| **`linkCounter`** | Total number of tree links performed. |
| **`cutCounter`** | Total number of cascading cuts performed. |
| **`prevMin`** | Temporary pointer used in `decreaseKey` to facilitate the cut and root list insertion process. |

---

### 🚀 Operations and Complexity

All implemented methods maintain the attributes (`size`, `treeCounter`, etc.) in $O(1)$ time.

| Method | Description | Complexity (Amortized) |
| :--- | :--- | :--- |
| `insert(k, info)` | Inserts a new node with key $k$ and data `info`. | $O(1)$ |
| `findMin()` | Returns the minimum node. | $O(1)$ |
| `deleteMin()` | Deletes the minimum node and performs consolidation. | $O(\log n)$ |
| `decreaseKey(x, diff)` | Decreases node $x$'s key by `diff` and fixes heap property via cascading cuts. | $O(1)$ |
| `delete(x)` | Deletes an arbitrary node $x$ using `decreaseKey` followed by `deleteMin`. | $O(\log n)$ |
| `meld(heap2)` | Merges the current heap with `heap2`. `heap2` is emptied. | $O(1)$ |
| `size()` | Returns the number of elements. | $O(1)$ |
| `numTrees()` | Returns the number of trees in the root list. | $O(1)$ |
| `totalLinks()` | Returns the total link count. | $O(1)$ |
| `totalCuts()` | Returns the total cut count. | $O(1)$ |

#### Internal/Helper Methods

* **`linkTrees(child, parent)`**: Links two trees of the same rank, making the node with the larger key a child. Updates `linkCounter`.
* **`consolidate(x)`**: Reduces the number of trees in the root list by repeatedly linking trees of the same rank, using a bucket array based on rank.
* **`cut(x, y)`**: Removes node $x$ from its parent $y$ and adds $x$ to the root list. Updates `cutCounter`.
* **`cascadingCut(x, y)`**: Performs the recursive process of cutting $x$ from $y$, marking $y$, and recursively cutting $y$ if it was already marked.

---

### 💎 `HeapNode` Class

The nested static class `HeapNode` represents a node in the heap and contains pointers necessary for maintaining the tree structure and the root list (a circular, doubly linked list).

| Field | Description |
| :--- | :--- |
| `key` | The node's key. |
| `info` | The node's associated data (String). |
| `parent`, `child` | Pointers for the tree structure. |
| `next`, `prev` | Pointers for the circular doubly linked list (root list or child list). |
| `rank` | The number of children. |
| `mark` | Boolean flag used in the cascading cut process. |
| `isFakeMin` | A specific flag used during the `delete(x)` operation. |
