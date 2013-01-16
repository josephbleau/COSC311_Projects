package com.josephbleau.cosc311.project1.parser;

import java.util.EmptyStackException;

public class Stack<T> {
	private Node<T> mHead;
	private Node<T> mTail;
	
	public Stack() {
		mHead = null;
		mTail = null;
	}
	
	public boolean empty() {
		return (mHead == null);
	}
	
	public T pop() throws EmptyStackException {
		if(mHead == null) {
			throw new EmptyStackException();
		}
		
		if(mHead.mNext == null) {
			T data = mHead.mData;
			
			mHead = null;
			mTail = null;
			
			return data;
		}
		
		Node<T> ptr1 = mHead;
		Node<T> ptr2 = mHead.mNext;
		
		while(ptr2.mNext != null) {
			ptr1 = ptr1.mNext;
			ptr2 = ptr2.mNext;
		}
		
		T data = ptr2.mData;
		mTail = ptr1;
		mTail.mNext = null;
		
		return data;
	}
	
	public void push(T data) {
		Node<T> node = new Node<T>(data);
		
		if(mHead == null) {
			mHead = node;
			mTail = node;
		} else {
			mTail.mNext = node;
			mTail = node;
		}
	}
	
	public T peek() {
		if(mHead == null) {
			throw new EmptyStackException();
		}
		
		return mTail.mData;
	}
	
	public int search(T needle) {
		if(mHead == null) {
			return -1;
		}
		
		int found = -1;
		int count = 0;
				
		for (Node<T> ptr = mHead; ptr != null; ptr = ptr.mNext, count++) {
			if(ptr.mData == needle) {
				found = count;
				break;
			}
		}
		
		return found;
	}
	
	public String toString() {
		if(mHead == null) {
			return "[]";
		}
		
		String print = "[";
		
		for (Node<T> ptr = mHead; ptr != null; ptr = ptr.mNext) {
			print = print + " " + ptr.mData.toString();
		}
		
		print = print + " ]";
		return print;
	}
}
