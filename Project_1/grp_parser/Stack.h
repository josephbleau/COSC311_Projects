#ifndef STACK_H
#define STACK_H

/* Stack implementation written for Project 1 of COSC311 at Eastern Michigan University
   Date Written: 1/9/2013
   Author (Student): Joseph J. Bleau, III 

   Short description: A standard stack data structure, implements empty, pop, push, and top. We were unallowed ot use
                      the standard library stack implementation for the purposes of demonstrating our programming
					  abilities during this project. 
					  
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
	SinglyNode<T>* mHead;
	SinglyNode<T>* mTail;

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
	mHead = NULL;
	mTail = NULL;
}

template <class T>
Stack<T>::~Stack() {
	clear(mHead);
}

template <class T>
void Stack<T>::clear(SinglyNode<T>* n = NULL){
	if(mHead == NULL)
		n = mHead;
	if(n == NULL)
		return;

	if(n->next) clear(n->next);
	
	delete n;
}

template <class T>
bool Stack<T>::empty() {
	return (mHead == NULL);
}

template <class T>
void Stack<T>::pop() {
	if(mHead == NULL)
		return;

	if(mHead->next == NULL){
		delete mHead;
		mHead = NULL;
		mTail = NULL;
		return;
	}

	SinglyNode<T>* ptr1 = mHead;
	SinglyNode<T>* ptr2 = mHead->next;

	while(ptr2->next){ ptr2 = ptr2->next; ptr1 = ptr1->next; }
	mTail = ptr1;
	ptr1->next = NULL;
	delete ptr2;
}

template <class T>
void Stack<T>::push(T value) {
	SinglyNode<T>* n = new SinglyNode<T>(value);
	
	mTail = n;

	if(mHead == NULL)
		mHead = n;
	else {
		SinglyNode<T>* ptr = mHead;
		while(ptr->next){ ptr = ptr->next; }
		ptr->next = n;
	}
}

template <class T>
T& Stack<T>::top() {
	return mTail->data;
}

};

#endif