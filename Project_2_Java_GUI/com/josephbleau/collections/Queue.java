package com.josephbleau.collections;

import java.util.NoSuchElementException;

public class Queue<T> {
	private Node<T> mHead;
	private Node<T> mTail;
	
	public Queue() {
		mHead = null;
		mTail = null;
	}
	
	public T element() throws NoSuchElementException {
		if(mHead == null) {
			throw new NoSuchElementException();
		} else {
			return peek();
		}
	}
	
        public boolean isEmpty() {
            return (mHead == null);
        }
        
	public boolean offer(T d) {
		Node<T> n = new Node<T>(d);
		
		if(mHead == null) {
			mHead = n;
			mTail = n;
		}
		else {
			mTail.mNext = n;
			mTail = n;
		}
		
		return true;
	}
	
	public T peek() {
		if(mHead == null)
			return null;
		else
			return mHead.mData;
	}
	
	public T poll() {
		T d = peek();
		remove();
		return d;
	}
	
	public void remove() throws NoSuchElementException {
		if(mHead != null) {
			mHead = mHead.mNext;
		} else {
			throw new NoSuchElementException();
		}
	}
	
	public String toString() {
		if(mHead == null) {
			return "[]";
		}
		
		Node<T> ptr = mHead;
		String out = "[";
		while(ptr != null) {
			out += " " + ptr.mData.toString();
			ptr = ptr.mNext;
		}
		
		out += " ]";
		return out;
	}
}
