package com.josephbleau.collections;
/* Internal node class, not for outside use. */
public class BSTNode<T> {
	public T data;
	public int leftIndex;
	public int rightIndex;
	
	BSTNode(){}
	
	BSTNode(T data){ 
		this.data = data;
		this.leftIndex = -1;
		this.rightIndex = -1;
	}
}