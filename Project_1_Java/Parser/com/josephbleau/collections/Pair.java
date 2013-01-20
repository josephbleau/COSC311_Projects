package com.josephbleau.collections;

public class Pair<A,B> {
	public A mFirst;
	public B mSecond;
	
	Pair() {
		mFirst = null;
		mSecond = null;
	}
	
	Pair(A first, B second) {
		mFirst = first;
		mSecond = second;
	}
}
