# Inverted index-based query solver

This looks in all the files inside a directory (specified as argument in command line)
and says which documents match the given query.

A query is of type : "open && !(source) && (security || C)",
the application assumes all queries are correctly written and have no
more than 20 terms.

The workflow is:
1) The query is parsed in order to extract the words in the mainFunctionality method

2) The words are given to the addOccurenciesForNewWords method, which adds new entries
	in the occurencies HashMap. An enty is of type (word, array of integers),
	The arrays is made of 0s and 1s, array[i]=1 if the i-th document(in alphabetical order)
	contains said word, and 0 otherwise.

3) An AND operation is performed on the arrays, via the andBetweenFinalTerms method.
	If a term is actually a !(term) or (term1 || term2 || ...), it is recursively 
	processed through the getOccurencyArray.

4) Once the final arrays of 0s and 1s is obtained, it is translated into the
	document names using getFileNamesFromFinalArray.

The document names ar then fed to the GUI Interface, which is implemented using Java Swing.

## Installation

1) Go inside the "CDL" directory.
2) Run setup.sh

## Usage
java Main <directory_name>

## Possible improvements
These are improvements I though of but did not have time to implement.
1) Serialising the occurencies hashmap at the end of each program,
	considering the documents do not change. And opening it at the beginning
	of a program.

2) Parallelisation of occurency searching or term processing.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License
[MIT](https://choosealicense.com/licenses/mit/)