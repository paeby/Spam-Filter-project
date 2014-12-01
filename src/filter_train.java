
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

/**
 * @author Prisca Aeby, Alexis Semple
 * Main class for the Machine Learning assignment.
 * This file implements a naive Bayesian text classifier.
 */
public class filter_train {
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

		//directory containing ham and spam 
		File directory = new File("/home/alexis/git/Spam-Filter-project/src/train");
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
		
		try {
			toFile(pSpam, pHam);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
	
	/**
	 * Writes the training results to file "training_data.txt"
	 * The file is structured as follows:
	 * First line: pSpam[double] pHam[double]
	 * Following lines: Word[String] 'all entries of the corresponding value separated by a space'[double]
	 * Each line contains the relevant data for one single entry of the Map.
	 * @param pSpam
	 * @param pHam
	 * @throws IOException
	 */
	private static void toFile(double pSpam, double pHam) throws IOException {
		
		FileWriter fstream = new FileWriter("training_data.txt");
		BufferedWriter out = new BufferedWriter(fstream);
		
		Iterator<Entry<String, double[]>> iterator = vocabulary.entrySet().iterator();
		
		out.write(pSpam + " " + pHam + "\n");
		
		while(iterator.hasNext()) {
			Map.Entry<String, double[]> entry = iterator.next();
			out.write(entry.getKey() + " " + doubleToString(entry.getValue()) + "\n");
		}
		out.close();
	}
	
	
	/**
	 * Returns a string of all values separated by a space in an array of doubles.
	 * @param array of doubles
	 * @return String of values separated by a space
	 */
	private static String doubleToString(double[] array) {
		String s = "";
		for(double d: array){
			s = s.concat(d + " ");
		}
		return s;
	}
	
}
