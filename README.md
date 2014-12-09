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

In order to compile filter_train.java, if using stemming preprocessing, you need to have a copy of org.tartarus.snowball.jar, which has been provided with the submission. To compile and run, you then need to

    javac -cp .:path/to/jar/org.tartarus.snowball.jar filter_train.java

    java -cp '.:path/to/jar/org.tartarus.snowball.jar' filter_train training_directory

We performed 10-fold cross-validation using bash scripts divideTrain.bash and cross_validation.bash.

We randomly split the provided training data using divideTrain.bash into 10 different test sets of equal size, and then used the remaining 90% of the training set as training data for each of the test sets respectively.
