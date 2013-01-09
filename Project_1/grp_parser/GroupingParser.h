#ifndef GROUPING_PARSER
#define GROUPING_PARSER

/* Grouping pair parser written for Project 1 of COSC311 at Eastern Michigan University.
   Date written: 1/9/2013
   Author (Student): Joseph J. Bleau, III

   Short description: This class will take, after instantialized, will take a string of characters through
                      its parse member and determine whether or not the string of characters is a set of valid
					  and appropriately matching groups. The currently pre-set valid groupings are [], (), {}, and <>

					  The design is flexible and allows for anyone with access to the class code to specify their own groupings,
					  with the possibility to expand this into the classes public API. This was out of the scope of the project.
*/

#include <map>
#include <stack>
#include "Stack.h"

namespace EMU {

enum GroupingParserResult {
	UNMATCHED_LEFT = 0,
	UNMATCHED_RIGHT,
	INVALID_GROUPING,
	INVALID_TOKEN,
	PARSE_SUCCESS 
};

class GroupingParser {
private:
	EMU::Stack<char> mStack;
	std::map<char, std::pair<bool, char>> mGroupSymbols;
	GroupingParserResult mResult;

	GroupingParserResult parse(char* input, bool run);
	void registerPair(char left, char right);
	bool isValidToken(char c);


public:
	GroupingParser();

	GroupingParserResult parse(char* input);
	void printResult();
};

};

#endif