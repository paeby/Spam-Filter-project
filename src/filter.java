import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * @author Prisca Aeby, Alexis Semple
 * Main class for the Machine Learning assignment.
 * This file implements a naive Bayesian text classifier.
 */
public class filter {
	/**
	 * Set of all distinct words occurring in all e-mails. 
	 * First entry of the table is for the count of the word occurring in all spam files, 
	 * the second is the same but for ham files,
	 * the third contains the conditional probability of the word knowing pSpam (P(word|pSpam)), 
	 * the fourth conditional probability of the word knowing pHam (P(word|pHam)).
	 */
	private static HashMap<String, double[]> vocabulary = new HashMap<String, double[]>();
	
	/**
	 * The total number of words 
	 * totalWords[0]: in all the spam files
	 * totalWords[1]: in all the ham files 
	 */
	private static int[] totalWords = new int[2];

	/**
	 * The main function for the program
	 * @param args The expected structure is: first, the path to the directory of training data, second the file to be classified
	 */
	public static void main (String[] args) {
		if (args.length != 2) {
			throw new IllegalArgumentException("Wrong number of arguments\n" +
					"The proper usage is: java train_directory test_file");
		}
		else {
			//directory containing ham and spam
			File directory = new File(args[0]);
			//file to test
			File testFile = new File(args[1]);
			//It selects only ham and spam files in the directory
			FileFilter filter = new FileFilter(){
				public boolean accept(File file){
					return file.getName().matches("^(spam|ham).*$") && file.isFile();
				}
			};

			//All spam and ham files
			File[] trainingFiles = directory.listFiles(filter);
			//Only spam files
			List<File> spamFiles = new ArrayList<File>();
			//Only ham files
			List<File> hamFiles = new ArrayList<File>();
			
			for(File f:trainingFiles){
				if(f.getName().matches("^spam.*$")){
					spamFiles.add(f);
				}
				else{
					hamFiles.add(f);
				}
			}
			
			// Probability of spam and ham, respectively
			double pSpam = (double)spamFiles.size()/(double)trainingFiles.length;
			double pHam = (double)hamFiles.size()/(double)trainingFiles.length;

			wordCounter(spamFiles, 0);
			wordCounter(hamFiles, 1);

			//Calculating conditional probabilities 
			for(String word:vocabulary.keySet()){
				double[] tab = vocabulary.get(word);
				tab[2] = (tab[0]+1)/((double)(totalWords[0]+vocabulary.size()));
				tab[3] = (tab[1]+1)/((double)(totalWords[1]+vocabulary.size()));
				vocabulary.put(word, tab);
			}
			if(classify(testFile, pSpam, pHam)){
				System.out.println("spam\n");
			}
			else{
				System.out.println("ham\n");
			}
		}
	}

	/**
	 * Method that runs the classification on the test file
	 * @param email the file to be classified
	 * @param pSpam probability of spam occurring, as computed above
	 * @param pHam probability of ham occurring, as computed above
	 * @return True, if the probability for spam for email is higher, else false (e.g. ham)
	 */
	private static boolean classify(File email, double pSpam, double pHam){

		double classifySpam = pSpam;
		double classifyHam = pHam;

		try {	
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(email).useDelimiter("[\\s\\p{Punct}]+");

			while(scanner.hasNext()){
				String next = scanner.next();
				if(vocabulary.containsKey(next)){
					classifySpam *= (vocabulary.get(next)[2]);
					classifyHam *= (vocabulary.get(next)[3]);
				}
			} 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return classifySpam > classifyHam;
	}

	/**
	 * Function to count all instances of each word occurring in the list of files
	 * @param list Contains all files with words to be counted
	 * @param i indicates whether files in list are spam (i = 0) or ham (i = 1)
	 */
	private static void wordCounter(List<File> list, int i){

		Scanner scanner;
		for(File f:list){
			try {
				scanner = new Scanner(f).useDelimiter("[\\s\\p{Punct}]+");
				while(scanner.hasNext()){
					String word = scanner.next();
					totalWords[i]++;
					if(vocabulary.containsKey(word)){
						double[] tab = vocabulary.get(word);
						tab[i]++;
						vocabulary.put(word,tab);
					}
					else{
						double[] tab = new double[]{0,0,0,0};
						tab[i]++;
						vocabulary.put(word, tab);
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}