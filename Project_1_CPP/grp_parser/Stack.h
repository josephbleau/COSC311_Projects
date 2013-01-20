#ifndef STACK_H
#define STACK_H

/*	Stack implementation written for Project 1 of COSC311 at Eastern Michigan University
	Date Written: 1/9/2013
	Author (Student): Joseph J. Bleau, III 

	Short description: A standard stack data structure, implements empty, pop, push, 
	and top. We were unallowed ot use the standard library stack implementation for 
	the purposes of demonstrating our programming abilities during this project. 
				  
*/

namespace EMU {

template <class T>
class SinglyNode {
public:
	SinglyNode(T data) { this->data = data; this->next = NULL; }
	SinglyNode(T data, SinglyNode<T>* next) { this->data = data; this->next = next; }

	T data;
	SinglyNode<T>* next;
};

template <class T>
class Stack {
private:
	SinglyNode<T>* mTop;

	void clear(SinglyNode<T>* n = NULL);

public:
	Stack();
	~Stack();

	bool empty();
	void pop();
	void push(T value);
	T& top();
};


template <class T>
Stack<T>::Stack() {
	mTop = NULL;
}

template <class T>
Stack<T>::~Stack() {
	clear(mTop);
	mTop = NULL;
}

template <class T>
void Stack<T>::clear(SinglyNode<T>* n = NULL){
	while(mTop) {
		SinglyNode<T>* old_top = mTop;
		mTop = mTop->next;
		delete old_top;
	}
}

template <class T>
bool Stack<T>::empty() {
	return (mTop == NULL);
}

template <class T>
void Stack<T>::pop() {
	if(mTop == NULL)
		return;

	SinglyNode<T>* old_top = mTop; 
	mTop = mTop->next;
	delete old_top;
}

template <class T>
void Stack<T>::push(T value) {
	SinglyNode<T>* n = new SinglyNode<T>(value);
	
	if(mTop == NULL) {
		mTop = n;
		return;
	}

	n->next = mTop;
	mTop = n;
}

template <class T>
T& Stack<T>::top() {
	return mTop->data;
}

};

#endif