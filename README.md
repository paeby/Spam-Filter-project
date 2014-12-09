Spam-Filter-project
===================
####Authors: Prisca Aeby, Alexis Semple
Implementation of a spam-filter using **Naive Bayes text classification**

For compilation, run

    javac *.java

To classify a test instance, run

    java filter.java test_instance

This will classify the instance according to the training data provided in the file training_data.txt. If you want to generate your own training data, first run

    java filter_train.java training_directory

There are some preprocessing steps that can be activated in the filter_train.java file, if desired. To do so, you need to uncomment a few lines.
- To enable stop-words removal preprocessing, uncomment line 92
- To enable stemming preprocessing, uncomment the following:
  - line 126
  - the 'import' declarations at the beginning of the document
  - the 'throws' declarations in the definition of wordCounter() (line 116) and main() (line 51)

10-fold cross-validation performed using bash scripts divideTrain.bash, cross_validation.bash
