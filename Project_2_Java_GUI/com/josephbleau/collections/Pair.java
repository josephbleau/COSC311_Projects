package com.josephbleau.collections;



public class Pair<A,B> {
	public A mFirst;
	public B mSecond;
	
	public Pair() {
		mFirst = null;
		mSecond = null;
	}
	
	public Pair(A first, B second) {
		mFirst = first;
		mSecond = second;
	}
}
