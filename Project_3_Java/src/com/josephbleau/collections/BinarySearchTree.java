package com.josephbleau.collections;

import java.util.EmptyStackException;
import java.util.Stack;

public class BinarySearchTree<T extends Comparable<T>> implements IBinarySearchTree<T>
{
	/* Fixed-memory allocation for our tree. */
	private Object[] tree; // Fixed size allocation for our BST.

	/* Stack of available positions in our fixed-memory. */
	private Stack<Integer> memMgr;
	
	/* Index into our fixed-memory that represents 
	 * where the root of our tree is stored.
	 */
	private int rootIndex;

	public BinarySearchTree(int maxNodes) {
		this.tree = new Object[maxNodes];
		this.memMgr = new Stack<Integer>();
		
		for(int i = maxNodes-1; i >= 0; i--) {
			this.tree[i] = null;
			this.memMgr.push(i);
		}
		
		this.rootIndex = -1;
	}
	
	/* Overload for insertion, allows creating a new node by offering
	 * just the data value instead of the awkward offer(new BSTNode(data))
	 */
	public boolean offer(T d) {
		return offer(new BSTNode<T>(d));
	}
	
	/* Iterative node insertion. */
	@SuppressWarnings("unchecked")
	private boolean offer(BSTNode<T> node) {
		if(node == null)
			return false;
		
		try {
			if(this.rootIndex == -1) {
				this.rootIndex = this.memMgr.pop();
				this.tree[this.rootIndex] = node;
				return true;
			}
			
			int curIndex = this.rootIndex;
			while(true) {
				BSTNode<T> curNode = (BSTNode<T>) this.tree[curIndex];
				if(node.data.compareTo(curNode.data) < 0) {
					if(curNode.leftIndex == -1) {
						int leftIndex = this.memMgr.pop();
						curNode.leftIndex = leftIndex;
						this.tree[leftIndex] = node;
						return true;
					} else {
						curIndex = curNode.leftIndex;
					}
				} else  {
					if(curNode.rightIndex == -1) {
						int rightIndex = this.memMgr.pop();
						curNode.rightIndex = rightIndex;
						this.tree[rightIndex] = node;
						return true;
					} else {
						curIndex = curNode.rightIndex;
					}
				}
			}
		}
		catch(IndexOutOfBoundsException e){
			return false;
		}
		catch(EmptyStackException e) {
			return false;
		}
	}
	
	/* Helper function: Re-establishes relationships appropriately when a 
	 * node is being deleted who only had one child.
	 */
	private void promoteSingleChild(int parentIndex, int curIndex, int childIndex) throws IndexOutOfBoundsException {
		if(childIndex == -1) {
			return;
		}
				
		if(parentIndex == -1) {
			this.rootIndex = childIndex;
		} else {
			BSTNode parentNode = (BSTNode) this.tree[parentIndex];

			if(curIndex == parentNode.leftIndex){
				parentNode.leftIndex = childIndex;
			} else if(curIndex == parentNode.rightIndex) {
				parentNode.rightIndex = childIndex;
			}
		}
	}
	
	/* Helper function: Takes two nodes and copies ones child 
	 * relationships into the other. */
	private void copyLinks(BSTNode<T> src, BSTNode<T> target) {
		if(src == null || target == null) {
			return;
		}
		
		target.leftIndex = src.leftIndex;
		target.rightIndex = src.rightIndex;
	}
	
	/* Helper function: finds the left-most child of the given node. */
	private BSTNode<T> getLeftmostChildNode(BSTNode<T> searchNode) throws IndexOutOfBoundsException{
		if(searchNode == null)
			return null;
		if(searchNode.leftIndex == -1)
			return searchNode;
		
		while(searchNode.leftIndex != -1) {
			if(searchNode.leftIndex == -1)
				return searchNode;
			else
				searchNode = (BSTNode<T>) this.tree[searchNode.leftIndex];
		}
		
		return searchNode;
	}
	
	/* Helper function: finds the right-most child of the given node. */
	private BSTNode getRightmostChildNode(BSTNode<T> searchNode) throws IndexOutOfBoundsException{
		if(searchNode == null)
			return null;
		if(searchNode.rightIndex == -1)
			return searchNode;

		while(searchNode.rightIndex != -1) {
			if(searchNode.rightIndex == -1)
				return searchNode;
			else
				searchNode = (BSTNode<T>) this.tree[searchNode.rightIndex];
		}
		
		return searchNode;
	}
	
	/* Helper function: removes the relationship between a child and its parent. */
	private void unlinkFromParent(int parentIndex, int curIndex) throws IndexOutOfBoundsException{
		if(parentIndex == -1) {
			this.rootIndex = -1;
		}
		else{
			BSTNode<T> parentNode = (BSTNode<T>) this.tree[parentIndex];
			
			if(parentNode.leftIndex == curIndex) {				
				parentNode.leftIndex = -1;
			} else {
				parentNode.rightIndex = -1;
			}
		}
	}
	
	/* An iterative node deletion. */
	private boolean remove(int lastIndex, int curIndex) {
		try {
			BSTNode<T> curNode = (BSTNode<T>) this.tree[curIndex];
			
			if(curNode.leftIndex != -1 && curNode.rightIndex == -1) {	
				promoteSingleChild(lastIndex, curIndex, curNode.leftIndex);	
			} else if( curNode.rightIndex != -1 && curNode.leftIndex == -1) {
				promoteSingleChild(lastIndex, curIndex, curNode.rightIndex);	
			} else if( curNode.leftIndex == -1 && curNode.rightIndex == -1 ) {
				unlinkFromParent(lastIndex, curIndex);
			} else { // Node has two children.
				BSTNode<T> lastNode = null;
				
				if(lastIndex != -1)
					lastNode = ((BSTNode<T>) this.tree[lastIndex]);				

				if(lastIndex == -1) {
					BSTNode<T> rootNode = (BSTNode<T>) this.tree[rootIndex];
					BSTNode<T> rightNode = (BSTNode<T>) this.tree[rootNode.rightIndex];
					
					this.rootIndex = curNode.leftIndex;
					memMgr.push(rootNode.rightIndex);
					offer(rightNode);
				}
				else if(lastNode.leftIndex == curIndex) {
					BSTNode<T> searchNode = getRightmostChildNode(curNode);
					searchNode.leftIndex = curNode.leftIndex;
					lastNode.leftIndex = curNode.rightIndex;
				} else {
					BSTNode<T> searchNode = getLeftmostChildNode(curNode);
					searchNode.rightIndex = curNode.rightIndex;
					lastNode.rightIndex = curNode.leftIndex;

				}
			}
			
			this.memMgr.push(curIndex);
			return true;
		}
		catch(IndexOutOfBoundsException e) {
			System.out.println("Exception!");
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	/* Iterative node search for removal. */
	public boolean remove(T d) {
		if(this.rootIndex == -1)
			return false;
		
		int lastIndex = -1;
		int curIndex = rootIndex;
			
		while(curIndex != -1) {
			BSTNode<T> curNode = (BSTNode<T>) this.tree[curIndex];
			
			if(d.compareTo(curNode.data)== 0) {
				return remove(lastIndex, curIndex);
			} else if(d.compareTo(curNode.data) < 0) {
				lastIndex = curIndex;
				curIndex = curNode.leftIndex;
			} else if(d.compareTo(curNode.data) > 0) {
				lastIndex = curIndex;
				curIndex = curNode.rightIndex;
			}
		}
		
		return false;
	}
}