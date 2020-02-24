package functionalities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Searcher {
	String folderName;
	
	public Searcher(String folderName){
		this.folderName = folderName;
	}
	

	public void addOccurenciesForNewWords(ArrayList<String> words, HashMap<String, Integer[]> occurencies) throws IOException {
		File folder = new File(folderName);
		File[] listOfFiles = folder.listFiles();

		// For each word in query
		for(int i = 0; i < words.size(); i++) {
			
			// Assuming max 500 documents to search in
			Integer[] myWordOccs = new Integer[501];
			String currentWord = words.get(i);
			Arrays.fill(myWordOccs, 0);
			
			// I search for its occurencies unless I hadn't done it before
			if(! occurencies.containsKey(words.get(i))) {	
				
				// I read each file line by line and search the word there	
				for(int j = 0; j < listOfFiles.length; j++) {
					File file = listOfFiles[j];
				    if (file.isFile()) {	   
				    	BufferedReader br;
						br = new BufferedReader(new FileReader(folder.getName()+ System.getProperty("file.separator") +file.getName()));
						String line;

						while ((line = br.readLine()) != null) {
						    String[] wordsCurrLine = line.toLowerCase().split("[ -]");
						    for(int k = 0; k < wordsCurrLine.length; k++) {
						    	if(wordsCurrLine[k].contentEquals(words.get(i))){
						    		myWordOccs[j] = 1;
						    	}
						    }
						}
						// Closing the file
						br.close();
				    }
				}
				// Adding the occurency array for current word into the hashmap
		    	occurencies.put(currentWord, myWordOccs);	
			}			
		}
	}
	
	public Integer[] andTwoArrays(Integer[] a, Integer[] b) {
		// Assumes arrays are of same size
		Integer[] result = new Integer[a.length];
		for(int i = 0; i < a.length; i++) {
			if(a[i] == 1 && b[i] == 1) {
				result[i] = 1;
			} else {
				result[i] = 0;
			}
		}
		
		return result;
		
	}

	public Integer[] andBetweenFinalTerms(String query, HashMap<String, Integer[]> occurencies) {
		Integer[] finalResult = new Integer[501];
		Arrays.fill(finalResult, 1); // In order not to influence the first AND
		String[] terms = new String[21];
		terms = query.toLowerCase().split(" && ");
		
		for(String term : terms) {
			// Getting the occurency array for current term
			Integer[] currentOccurencyArray = new Integer[501];
			Arrays.fill(currentOccurencyArray, 0);
			currentOccurencyArray = getOccurencyArray(currentOccurencyArray, occurencies, term);
			
			// Making an AND between previous result and current array
			finalResult = andTwoArrays(finalResult, currentOccurencyArray);
		}

		return finalResult;
	}

	private Integer[] getOccurencyArray(Integer[] currentOccurencyArray, HashMap<String, Integer[]> occurencies, String term) {	
		if(term.charAt(0) == '!') {
			String newTerm = term.substring(2, term.length() - 1);
			currentOccurencyArray = getOccurencyArray(currentOccurencyArray, occurencies, newTerm);
			for(int i = 0; i < currentOccurencyArray.length ; i++) {
				currentOccurencyArray[i] = 1 - currentOccurencyArray[i];
			}
		} else {
			if(term.contains("||")) {
				// we should do an OR between terms
				String[] orTermsAux = term.split("[ \\(\\)\\| ]");
				ArrayList<String> orTerms = new ArrayList<String>();
				for(int i = 0 ; i < orTermsAux.length ; i++) {
					if(!(orTermsAux[i] == null) && !orTermsAux[i].contentEquals("")) {
						orTerms.add(orTermsAux[i]);
					}
				}
				currentOccurencyArray = makeOrBetweenMultiple(orTerms, occurencies);
			} else {
				// It is a simple term
				// We can just retrieve its occ array from the hashmap
				currentOccurencyArray = occurencies.get(term);
			}
		}
		return currentOccurencyArray;
	}

	private Integer[] makeOrBetweenMultiple(ArrayList<String> orTerms, HashMap<String, Integer[]> occurencies) {
		Integer[] finalResult = new Integer[501];
		Arrays.fill(finalResult, 0); // first is 0 to be neutral
		for(String term : orTerms) {
			Integer[] currOccurency = occurencies.get(term);
			for(int i = 0; i < currOccurency.length ; i++) {
				if(currOccurency[i] == 1)
					finalResult[i] = 1;					
			}
		}
		return finalResult;
	}
	
	public String getFileNamesFromFinalArray(Integer[] finalResult) {
	
		File folder = new File(folderName);
		File[] listOfFiles = folder.listFiles();
		StringBuilder answer = new StringBuilder("");
		for(int i = 0 ; i < finalResult.length ; i++) {
			if(finalResult[i] == 1 && i < listOfFiles.length) {
				answer.append(listOfFiles[i].getName());
				answer.append(" ");
			}
		}
		if(answer.toString().contentEquals(""))
			answer.append("No results found");
		return answer.toString();

	}
	
	public String mainFunctionality(String query) {
		// Parsing clear words from input
		String[] terms = new String[21];
		terms = query.toLowerCase().split("[ |!()]");
		ArrayList<String> words = new ArrayList<>(); // complete words, no bool logic
		for(int i = 0; i < terms.length; i++) {
			if(terms[i].length() > 0 && terms[i].charAt(0) != '&') {
				words.add(terms[i]);
			}
		}
		
		HashMap<String,Integer[]> occurencies = new HashMap<>();

		// Mapping words and their occurencies in docs
		try {
			addOccurenciesForNewWords(words, occurencies);
		} catch (IOException e1) {
			e1.printStackTrace();
			return "Invalid request";
		}
		
		// Starting the logical operations
		Integer[] finalResult = andBetweenFinalTerms(query, occurencies);
		return getFileNamesFromFinalArray(finalResult);
	}
}
