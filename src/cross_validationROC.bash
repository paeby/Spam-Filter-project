#!/bin/bash
clear

#Trains and tests on folders for cross-validation
#output result_basic_NB.txt with
#1st line count of successful classifications
#2nd line count of total classifications
#-Run in parent folder of cross-validation subfolders

for i in {1..10}
do
  total=0

  echo "--------Creating training data for train$i--------"
  echo
  java filter_train train"$i"/train
#java -cp /Users/paeby/Downloads/org.tartarus.snowball.jar filter_train train"$i"/train
  echo "--------Testing files for train$i--------"
  echo
  count=0
  countTP=0
  countTN=0
  countFP=0
  countFN=0
  rm train"$i"/roc.txt
  for file in train"$i"/test/*
  do
    ((total++))
    java filterROC "$file" > train"$i"/result_basic_NB.txt
    res=`cat train$i/result_basic_NB.txt`

    if [[ "$file" =~ .*"ham".* && "$res" == 1* ]]; then
        ((count++))
        ((countTP++))
      echo "$res" >> train"$i"/roc.txt
    fi
    if [[ "$file" =~ .*"spam".* && "$res" == 0* ]]; then
        ((count++))
        ((countTN++))
        echo "$res" >> train"$i"/roc.txt
    fi
    if [[ "$file" =~ .*"spam".* && "$res" == 1* ]]; then
        ((countFP++))
    fi
    if [[ "$file" =~ .*"ham".* && "$res" == 0* ]]; then
        ((countFN++))
    fi
    echo "count = $count"
    echo "total = $total"
  done

  echo "TP+TN $count" > train"$i"/result_basic_NB.txt
  echo "Total $total" >> train"$i"/result_basic_NB.txt
  echo "TP $countTP" >> train"$i"/result_basic_NB.txt
  echo "TN $countTN" >> train"$i"/result_basic_NB.txt
  echo "FP $countFP" >> train"$i"/result_basic_NB.txt
  echo "FN $countFN" >> train"$i"/result_basic_NB.txt

  mv training_data.txt train"$i"

done
