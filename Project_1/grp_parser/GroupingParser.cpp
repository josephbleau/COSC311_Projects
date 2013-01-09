#include "GroupingParser.h"
#include <iostream>

namespace EMU {

GroupingParser::GroupingParser() {
	registerPair('[', ']');
	registerPair('{', '}');
	registerPair('<', '>');
	registerPair('(', ')');
}

void GroupingParser::registerPair(char left, char right) {
	mGroupSymbols[left] = std::pair<bool, char>(true, right);
	mGroupSymbols[right] = std::pair<bool, char>(false, left);
}

bool GroupingParser::isValidToken(char c) {
	return c == mGroupSymbols[mGroupSymbols[c].second].second;
}

GroupingParserResult GroupingParser::parse(char* input) {
	return (mResult = parse(input, true));
}

GroupingParserResult GroupingParser::parse(char* input, bool run){
	char *p = input;
	while(p && *p != '\0') {
		char c = *p;
		p++;

		if(!isValidToken(c)) {
			return INVALID_TOKEN;
		}

		if(mGroupSymbols[c].first) {
			mStack.push(c);
		} else {
			if(mStack.empty()) {
				return UNMATCHED_RIGHT;
			}

			const char& top = mStack.top();
			if(mGroupSymbols[c].second != top) {
				return INVALID_GROUPING;
			}

			mStack.pop();
		}
	}

	if(!mStack.empty()){
		return UNMATCHED_LEFT;
	}

	return PARSE_SUCCESS;
}


void GroupingParser::printResult() {
	if(mResult == UNMATCHED_LEFT)
		std::cout << "Unmatched left symbol";
	else if(mResult  == UNMATCHED_RIGHT)
		std::cout << "Unmatched right symbol";
	else if(mResult  == INVALID_GROUPING)
		std::cout << "Invalid grouping";
	else if(mResult == INVALID_TOKEN) 
		std::cout << "Invalid token found";
	else if(mResult  == PARSE_SUCCESS)
		std::cout << "Grouping symbols match";
	else
		std::cout << "";
}

};