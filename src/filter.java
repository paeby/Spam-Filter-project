import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.lang.IllegalArgumentException;

import javax.swing.JFileChooser;


public class filter {
	
	public static void main (String[] args) {
		if (args.length != 2) {
			throw new IllegalArgumentException("Wrong number of arguments\n" +
					"The proper usage is: java train_directory test_file");
		}
		else {
        	//directory containing hams and spams
        	JFileChooser directory = new JFileChooser(args[0]);
        	//file to test
        	File test_file = new File(args[1]);
        	//It selects only hams and spams in the directory
        	FileFilter filter = new FileFilter(){
        		public boolean accept(File file){
        			return file.getName().matches("^(spam|ham)") || file.isFile();
        		}
        	};
        	directory.setFileFilter((javax.swing.filechooser.FileFilter) filter);
        	
        	//All spams and hams
        	File[] training_files = directory.getSelectedFiles();
        	//Only spam files (in the algorithm, docsj)
        	List<File> spam_files = new ArrayList<File>();
        	//Only ham files
        	List<File> ham_files = new ArrayList<File>();
        	
        	for(File f:training_files){
        		if(f.getName().matches("^spam")){
        			spam_files.add(f);
        		}
        		else{
        			ham_files.add(f);
        		}
        	}
        	
        	double p_spam = (double)spam_files.size()/(double)training_files.length;
        	double p_ham = (double)ham_files.size()/(double)training_files.length;
        	
        	//Vocabulary: set of all distinct words occurring in all e-mails. First column of the table is for spams, second for hams
        	HashMap<String, int[]> vocabulary = new HashMap();
        	
        	for(File f:spam_files){
        		
        	}
       
        }
    }
}
