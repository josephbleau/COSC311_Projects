package com.josephbleau.utilities.parsing;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import com.josephbleau.collections.Stack;

public class PairSequenceParser {
	private Stack<Character> mStack;
	
	public enum GroupParserResult { 
		PARSE_SUCCESS,
		INVALID_GROUPING,
		UNMATCHED_LEFT,
		UNMATCHED_RIGHT,
	};
	
	public PairSequenceParser() { 
		mStack = new Stack<Character>(); 
	}
	
	public Character randomLeftHand() {
		Random generator = new Random();
		String lefts= "[{(<";
		return lefts.charAt(generator.nextInt(lefts.length()));
	}
	
	public Character randomRightHand() {
		Random generator = new Random();
		String rights= "]})>";
		return rights.charAt(generator.nextInt(rights.length()));
	}
	
	public boolean isLeftToken(Character c){ 
		return (c == '(' || c == '[' || c == '<' || c == '{');
	}
	
	public boolean isRightToken(Character c){
		return (c == ')' || c == ']' || c == '>' || c == '}');
	}
	
	public boolean isProperMatch(Character a, Character b) {
		return (a == '[' && b == ']') ||
			   (a == '{' && b == '}') ||
			   (a == '<' && b == '>') ||
			   (a == '(' && b == ')');
	}
	
	public GroupParserResult parse(String data) {
		return parse(new ByteArrayInputStream(data.getBytes()));
	}
	
	public GroupParserResult parse(InputStream stream){ 
		mStack = new Stack<Character>();
		
		try {
			Character c = (char) stream.read();
			while(true) {
				
				if(isLeftToken(c)) {
					mStack.push(c);
				}
				else if(isRightToken(c)) {
					if(mStack.empty()) {
						return GroupParserResult.UNMATCHED_RIGHT;
					}
					
					if(!isProperMatch(mStack.pop(), c)) {
						return GroupParserResult.INVALID_GROUPING;
					}
				}
				else {
					break;
				}
				
				c = (char) stream.read();
			}
		} catch (EOFException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(!mStack.empty()) {
			return GroupParserResult.UNMATCHED_LEFT;
		}
		
		return GroupParserResult.PARSE_SUCCESS;	
	}
	
	public String resultToString(GroupParserResult result){
		switch(result) {
			case PARSE_SUCCESS:
				return "valid input";
			case UNMATCHED_LEFT:
				return "unmatched left symbol";
			case UNMATCHED_RIGHT:
				return "unmatched right symbol";
			case INVALID_GROUPING:
				return "invalid grouping";
			default:
				return "";
		}
	}
}
