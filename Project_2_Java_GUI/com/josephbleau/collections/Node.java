package com.josephbleau.collections;

public class Node<T> {
	public T mData;
	public Node<T> mNext;
	
	public Node() {
		mNext = null;
	}
	
	public Node(T data) {
		mData = data;
		mNext = null;
	}
	
	public Node(T data, Node<T> next) {
		mData = data;
		mNext = next;
	}
}
