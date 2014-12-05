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
  echo "--------Testing files for train$i--------"
  echo
  count=0
  for file in train"$i"/test/*
  do
    echo "file = $file"
    ((total++))
    java filterb "$file" > train"$i"/result_basic_NB.txt
    res=`cat train$i/result_basic_NB.txt`
    echo "result = $res"

    if [[ "$file" =~ .*"$res".* ]]; then
      ((count++))
    fi
    echo "count = $count"
    echo "total = $total"
  done

  echo "$count" > train"$i"/result_basic_NB.txt
  echo "$total" >> train"$i"/result_basic_NB.txt

  mv training_data.txt train"$i"

done
