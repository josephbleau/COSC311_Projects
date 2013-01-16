package com.josephbleau.cosc311.project1.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.EmptyStackException;

import com.josephbleau.collections.Stack;
import com.josephbleau.utilities.parsing.GroupParser;
import com.josephbleau.utilities.parsing.GroupParser.GroupParserResult;

public class Application {
	
	public static void main(String[] args) {
		// stackTests(); // Remove comment to run unit tests on our custom stack.
		// groupParserTests(); // Remove comment to run unit tests on our group parser.
		
		if(args.length != 1) {
			System.out.println("Usage error, you must provide a pathname to the file to parse as an argument.");
			System.exit(-1);
		}
		
		try {
			FileInputStream stream = new FileInputStream(args[0]);
			GroupParser parser = new GroupParser();
			
			GroupParserResult result = parser.parse(stream);
			System.out.println(parser.resultToString(result));
			
			stream.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("Filesystem error, file " + args[0] + " could not be found.");
			System.exit(-2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.exit(0);
	}
	
	public static void groupParserTests() {
		GroupParser parser = new GroupParser();
		
		GroupParserResult result = parser.parse("([][]({}))<>");
		System.out.println(parser.resultToString(result));
		
		result = parser.parse("(");
		System.out.println(parser.resultToString(result));
		
		result = parser.parse("()]]()");
		System.out.println(parser.resultToString(result));
		
		result = parser.parse("((){))");
		System.out.println(parser.resultToString(result));
	}
	
	public static void stackTests() {
		
		Stack<Integer> stack = new Stack<Integer>();
		
		stack.push(5);
		stack.push(10);
		stack.push(3);
		stack.push(-20);
		
		System.out.println(stack.toString());
		
		stack.pop();
		stack.pop();
		
		System.out.println(stack.toString());
		System.out.println("The tail value of the stack is: " + stack.peek().toString());
		
		stack.pop();
		stack.pop();
		
		System.out.println("The stack is empty: " + stack.empty());
		
		try {
			stack.pop();
		} catch (EmptyStackException e) {
			System.out.println("You tried to pop an empty stack.");
		}
		
		try {
			stack.peek();
		} catch (EmptyStackException e) {
			System.out.println("You tried to peek at an empty stack.");
		}
	}
}
