import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFileChooser;


public class filter {
	//Vocabulary: set of all distinct words occurring in all e-mails. First column of the table is for spams, second for hams,
	//third conditional probability of the word knowing pSpam, fourth conditional probability of the word knowing pHam.
	private static HashMap<String, double[]> vocabulary = new HashMap<String, double[]>();
	private static int[] totalWords = new int[2];

	public static void main (String[] args) {
		if (args.length == 2) {
			//directory containing hams and spams
			JFileChooser directory = new JFileChooser(args[0]);
			//file to test
			File testFile = new File(args[1]);
			//It selects only hams and spams in the directory
			FileFilter filter = new FileFilter(){
				public boolean accept(File file){
					return file.getName().matches("^(spam|ham)") && file.isFile();
				}
			};
			directory.setFileFilter((javax.swing.filechooser.FileFilter) filter);

			//All spams and hams
			File[] trainingFiles = directory.getSelectedFiles();
			//Only spam files (in the algorithm, docsj)
			List<File> spamFiles = new ArrayList<File>();
			//Only ham files
			List<File> hamFiles = new ArrayList<File>();

			for(File f:trainingFiles){
				if(f.getName().matches("^spam")){
					spamFiles.add(f);
				}
				else{
					hamFiles.add(f);
				}
			}

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

	public static boolean classify(File email, double pSpam, double pHam){
		
		double classifySpam = pSpam;
		double classifyHam = pHam;
		
		try {	
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(email).useDelimiter("[\\s\\p{Punct}]+");
			
			while(scanner.hasNext()){
				classifySpam = classifySpam*(vocabulary.get(scanner.next())[2]);
				classifyHam = classifyHam*(vocabulary.get(scanner.next())[3]);
			} 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		if(classifySpam > classifyHam){
			return true;
		}
		else {
			return false;
		}
	}

	public static void wordCounter(List<File> list, int i){
		
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