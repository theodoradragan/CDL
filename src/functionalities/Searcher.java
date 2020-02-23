package functionalities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

public class Searcher {
	String folderName;
	
	public Searcher(String folderName){
		this.folderName = folderName;
	}
	

	public void addOccurenciesForNewWords(ArrayList<String> words, HashMap<String, Integer[]> occurencies) throws IOException {

		File folder = new File("documente_problema");
		File[] listOfFiles = folder.listFiles();
		
		
		// For each word in query
		for(int i = 0; i < words.size(); i++) {
			// Am presupus ca max 500 de documente
			Integer[] myWordOccs = new Integer[501];
			String currentWord = words.get(i);
			Arrays.fill(myWordOccs, 0);
			// I search for its occurencies unless I hadn't done it before
			if(! occurencies.containsKey(words.get(i))) {
				
				// I read each file line by line and search the word there
				// listOfFiles.length
				for(int j = 0; j < listOfFiles.length; j++) {

					File file = listOfFiles[j];
				    if (file.isFile()) {
		   
				    	BufferedReader br;
						br = new BufferedReader(new FileReader(folder.getName()+ System.getProperty("file.separator") +file.getName()));
						String line;

						while ((line = br.readLine()) != null) {
						    String[] wordsCurrLine = line.toLowerCase().split("[ -]");
						    //System.out.println(wordsCurrLine[0]);
						    for(int k = 0; k < wordsCurrLine.length; k++) {
						    	if(wordsCurrLine[k].contentEquals(words.get(i))){
						    		//System.out.println("am gasit " + words.get(i));
						    		//System.out.println("j este " + j);
						    		myWordOccs[j] = 1;
						    		//System.out.println(Arrays.toString(myWordOccs));
						    	}
						    }
						}
				    }
				}
		    	//System.out.println("Inainte sa il pun:" + Arrays.toString(myWordOccs));
		    	
		    	occurencies.put(currentWord, myWordOccs);
		    	
//		    	for(Entry<String, Integer[]> e : occurencies.entrySet()) {
//					System.out.println(e.getKey());
//					System.out.println(Arrays.toString(e.getValue()));
//				}
//		    	System.out.println("^^^noul hashmap^^^^^");		
			}			
		}		
	}
	
	public Integer[] andTwoArrays(Integer[] a, Integer[] b) {
		// assumes arrays are of same size
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
		Arrays.fill(finalResult, 1); // for first and, it is 0
		String[] terms = new String[21];
		terms = query.toLowerCase().split(" && ");
		
		for(String term : terms) {
			Integer[] currentOccurencyArray = new Integer[501];
			Arrays.fill(currentOccurencyArray, 0); // for first and, it is 0
			currentOccurencyArray = getOccurencyArray(currentOccurencyArray, occurencies, term);
//			System.out.println("occurency array  : " + Arrays.toString(currentOccurencyArray));
			finalResult = andTwoArrays(finalResult, currentOccurencyArray);
			System.out.println("end after term : " + term + Arrays.toString(finalResult));
		}

		return finalResult;
	}

	private Integer[] getOccurencyArray(Integer[] currentOccurencyArray, HashMap<String, Integer[]> occurencies, String term) {	
		if(term.charAt(0) == '!') {
			System.out.println(term);
			String newTerm = term.substring(2, term.length() - 1);
			currentOccurencyArray = getOccurencyArray(currentOccurencyArray, occurencies, newTerm);
			System.out.println("inainte de not : " + Arrays.toString(currentOccurencyArray));
			for(int i = 0; i < currentOccurencyArray.length ; i++) {
				currentOccurencyArray[i] = 1 - currentOccurencyArray[i];
			}
			System.out.println("dupa not : " + Arrays.toString(currentOccurencyArray));

		} else {
			if(term.contains("||")) {
				// we should do an OR between terms
				System.out.println(term);
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
				System.out.println(term);
				currentOccurencyArray = occurencies.get(term);
			}
		}
		
		System.out.println("functia da " + Arrays.toString(currentOccurencyArray));
		return currentOccurencyArray;
	}

	private Integer[] makeOrBetweenMultiple(ArrayList<String> orTerms, HashMap<String, Integer[]> occurencies) {
		Integer[] finalResult = new Integer[501];
		Arrays.fill(finalResult, 0); // first is 0 to be neutral
		for(String term : orTerms) {
			System.out.println(term);
			Integer[] currOccurency = occurencies.get(term);
			for(int i = 0; i < currOccurency.length ; i++) {
				if(currOccurency[i] == 1)
					finalResult[i] = 1;					
			}
		}
		System.out.println("or result : " + Arrays.toString(finalResult));
		return finalResult;
	}
	
	public String getFileNamesFromFinalArray(Integer[] finalResult) {
	
		File folder = new File("documente_problema");
		File[] listOfFiles = folder.listFiles();
		StringBuilder answer = new StringBuilder("");
		for(int i = 0 ; i < finalResult.length ; i++) {
			if(finalResult[i] == 1 && i < listOfFiles.length) {
				answer.append(listOfFiles[i].getName());
				answer.append(" ");
			}
		}
		return answer.toString();

	}
	
	public String mainFunctionality(String query) {
		// Input
		System.out.println("Hello!");
		
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
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "Invalid request";
		}
		
		System.out.println("--------------------");
		for(Entry<String, Integer[]> e : occurencies.entrySet()) {
			System.out.println(e.getKey());
			System.out.println(Arrays.toString(e.getValue()));
		}
		System.out.println("--------------------");
		
		// Starting the logical operations
		Integer[] finalResult = andBetweenFinalTerms(query, occurencies);
		
		// Printing result to STDIN (fow now)
		return getFileNamesFromFinalArray(finalResult);
	}
}
