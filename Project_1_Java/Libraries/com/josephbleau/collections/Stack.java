package com.josephbleau.collections;

import java.util.EmptyStackException;

public class Stack<T> {
	private Node<T> mTop;
	
	public Stack() {
		mTop = null;
	}
	
	public boolean empty() {
		return (mTop == null);
	}
	
	public T pop() throws EmptyStackException {
		if(mTop == null) {
			throw new EmptyStackException();
		}
		
		T data = mTop.mData;
		mTop = mTop.mNext;
		
		return data;
	}
	
	public void push(T data) {
		Node<T> node = new Node<T>(data);
		
		if(mTop == null) {
			mTop = node;
			return;
		}
		
		Node<T> old_top = mTop;
		node.mNext = old_top;
		mTop = node;
	}
	
	public T peek() {
		if(mTop == null) {
			throw new EmptyStackException();
		}
		
		return mTop.mData;
	}
	
	public int search(T needle) {
		if(mTop == null) {
			return -1;
		}
		
		int found = -1;
		int count = 0;
				
		for (Node<T> ptr = mTop; ptr != null; ptr = ptr.mNext, count++) {
			if(ptr.mData == needle) {
				found = count;
				break;
			}
		}
		
		return found;
	}
	
	public String toString() {
		if(mTop == null) {
			return "[]";
		}
		
		String print = "[";
		
		for (Node<T> ptr = mTop; ptr != null; ptr = ptr.mNext) {
			print = print + " " + ptr.mData.toString();
		}
		
		print = print + " ]";
		return print;
	}
}
