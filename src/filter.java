import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author Prisca Aeby, Alexis Semple
 * Main class for the classification of the Machine Learning assignment.
 * This file implements a naive Bayesian text classification.
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
	 * The main function for the program
	 * @param args The expected structure is: first, the path to the directory of training data, second the file to be classified
	 */
	public static void main (String[] args) {
		if (args.length != 1) {
			throw new IllegalArgumentException("Wrong number of arguments\n" +
					"The proper usage is: java filter test_file");
		}
		
		File testFile = new File(args[0]);
		Scanner trainingData = null;
		try {
			trainingData = new Scanner(new File("training_data.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Error: training_data.txt could not be found!");
			e.printStackTrace();
		}
		
		String[] probabilities = trainingData.nextLine().split(" ");
		double pSpam = Double.parseDouble((probabilities[0]));
		double pHam = Double.parseDouble((probabilities[1]));
		
		//Put the training data in the vocabulary
		while(trainingData.hasNextLine()){
			String[] entry = trainingData.nextLine().split(" ");
			vocabulary.put(entry[0], 
					new double[]{Double.parseDouble(entry[1]),Double.parseDouble(entry[2]),Double.parseDouble(entry[3]),Double.parseDouble(entry[4])});
		}
		trainingData.close();
		
		if(classify(testFile, pSpam, pHam)){
			System.out.println("spam\n");
		}
		else{
			System.out.println("ham\n");
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
					classifySpam += (vocabulary.get(next)[2]);
					classifyHam += (vocabulary.get(next)[3]);
				}
			} 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return classifySpam > classifyHam;
	}
}
