/* Project 1 for COSC 311 (Advanced Datastructures & Algorithms) at Eastern Michigan University
   Written on: 1/9/2013
   Author (Student): Joseph J. Bleau, III

   Purpose: The purpose of this program is to generate random characters and writing them to file for the purpose of
            supplying them to the other program in this project (grp_parser.exe). 
			
			This program requires parameters # of groups, result, and filename.
			
			The number of groups is simply how many pairs there will be. 
			The result can be valid, unmatched_right, unmatched_left, and invalid_grouping.

			While still containing randomness, this program is designed to generate a text file which will invoke
			the appropriate response from our main parser.

			And finally, the filename is the name of the file it will output to.

   Implementation:
			Algorithm for generating random valid pairs:
				Select a random location for a new pair.
				Randomly insert the pair.
				Repeat this process for the number of groups desired.

			Algorithm for generating invalid grouping:
				Run algorithm for generating random valid pairs.
				Randomly substitute a symbol for another of its orientation.

			Algorithm for generating unmatched left member:
				Run algorithm for generating random valid pairs.
				Insert right-side symbol randomly.

			Algorithm for generating unmatched right member:
				Run algorithm for generating random valid pairs.
				Insert left-side symbol randomly.

	Note: No real design decisions made during the creation of this program aside from vague notions, 
	      not really seen as an important application, but just a testing tool for 
		  the grp_parser. Code is notably messier than the other program. :-)

	Main returns: 
		0 on success
		1 on generic invalid usage
		2 on invalid number of groups provided
		3 on invalid type of generation provided
		4 on file-write error
*/

#include <iostream>
#include <string>
#include <sstream>
#include <time.h>
#include <random>
#include <fstream>

/* Random left side symbol. */
char rlss() {
	char symbols[] = "([{<";
	return symbols[rand()%strlen(symbols)];
}

/* Get associated right-side-symbol */
char get_rss(char lss) {
	if(lss == '(') return ')';
	if(lss == '[') return ']';
	if(lss == '{') return '}';
	if(lss = '<') return '>';
	
	return lss;
}

/* Random right side symbol. */
char rrss(){
	return get_rss(rlss());
}

/* Is right side symbol? */
bool is_rss(char c) {
	return (c == ')' || c == '}' || c == ']' || c == '>');
}

int main(int argc, char* argv[]) {
	srand(time(0));

	if(argc != 4) {
		std::cout << "Usage: rand_groups.exe [num_of_groups] [type: valid | unmatched_right " 
			      << "| unmatched_left | invalid_grouping] [filename]" << std::endl;
		return 1;
	}

	int num_groups = 0;
	std::stringstream convert;
	convert << argv[1];
	convert >> num_groups;

	if(num_groups <= 0) {
		std::cout << "Error: number of groups specified was either less than one or non-numeric. " << std::endl;
		return 2;
	}

	std::string output;
	while(num_groups-- > 0) {
		char ls = rlss(); // Generate a random left-side symbol.
		
		int insert_at = 0;
		if(output.size() > 0)
			insert_at = (rand()%output.size())+1;

		std::string output1 = output.substr(0,insert_at);
		std::string output2 = output.substr(insert_at);

		output = output1 + ls;
		output += get_rss(ls);
		output += output2;
	}

	std::string type = argv[2];
	if(type == "valid") {
		/* Output is good, write. */
	} else if(type == "invalid_grouping") {
		int r  = rand()%output.size()+1;
		if(output[r] == '[') output[r] = '('; if(output[r] == ']') output[r] = ')';
		if(output[r] == '{') output[r] = '['; if(output[r] == '}') output[r] = ']';
		if(output[r] == '(') output[r] = '<'; if(output[r] == ')') output[r] = '>';
		if(output[r] == '<') output[r] = '}'; if(output[r] == '>') output[r] = '}';
	} else if(type == "unmatched_right") {
		output = rrss() + output;
	} else if(type == "unmatched_left") {
		output = output + rlss();
	} else {
		std::cout << "Error: invalid generation type\n";
		return 3;
	}

	/* If we're here, write output to argv3 */
	std::ofstream out(argv[3], std::ios::out);

	if(out){
		out.write(output.c_str(), output.size());
		out.close();
	} else {
		std::cout << "Error: could not open file " << argv[3] << " for writing.\n";
		return 4;
	}

	return 0;
}