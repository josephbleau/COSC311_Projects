/* Project 1 for COSC311 Datastructures and Algorithms course at Eastern Michigan University.
   Written on: 1/9/2013
   Author (Student): Joseph J. Bleau, III

   Purpose: This application accepts a single parameter (file-name.) It reads the file into memory and
            parses its characters. It does so with the intention of discovering whether or not the file
			contains valid character group parings. Such valid pairs would be (), {}, [], <>, by default.
			These pairs may also be nested, for example ((<<[]>>)) would be a valid set of groups.

			This is implemented in two major stages, first with a custom stack (written for the purposes of this
			assignment.) I do not supose it to be faster, or in any way superior to the standard implementation (std::stack)
			but it was a requirement of the assignment.

			The second is the class GroupingParser. This class was not a pre-defined solution or specification for the problem, but
			simply my particular design preference with regards to the overarching problem.

			Unit tests are included in the main source file.

*/

#include <iostream>
#include <fstream>
#include <string>

#include "GroupingParser.h"

//#define RUN_UNIT_TESTS /* Uncomment to run unit tests. */

int main(int argc, char* argv[]) {
	EMU::GroupingParser gp;
	
#ifdef RUN_UNIT_TESTS
	/* Parse successfully. */
	gp.parse("[<<<<(())>>>>]");
	gp.printResult();
	std::cout << std::endl;

	/* Unmatched right grouping symbol. */
	gp.parse("({})<>]");
	gp.printResult();
	std::cout << std::endl;

	/* Unmatched left grouping symbol. */
	gp.parse("()(()){[]}{");
	gp.printResult();
	std::cout << std::endl;

	/* Invalid grouping. */
	gp.parse("(([>))");
	gp.printResult();
	std::cout << std::endl;

	/* Invalid token found. */
	gp.parse("(([{[{d}]}");
	gp.printResult();
	std::cout << std::endl;

	return 0;
#endif

	if(argc != 2) {
		std::cout << "Usage: grp_parser.exe [filename]";
		return 1;
	}

	/* Cannibalized 'read whole file into string' from
	   http://stackoverflow.com/questions/2602013/read-whole-ascii-file-into-c-stdstring */
	std::ifstream in(argv[1], std::ios::in | std::ios::binary);
	if(in) {
		std::string contents;
		in.seekg(0, std::ios::end);
		contents.resize(in.tellg());
		in.seekg(0, std::ios::beg);
		in.read(&contents[0], contents.size());
		in.close();

		gp.parse((char*)contents.c_str());
		gp.printResult();

	} else {
		std::cerr << "Unable to open file: " << argv[1] << std::endl;
	}

	return 0;
}