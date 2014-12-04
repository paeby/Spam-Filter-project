#!/bin/bash
clear

#output result.txt with
#1st line count of successful classifications
#2nd line count of total classifications

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
    ((total++))
    java filterb "$file" > train"$i"/result_basic_NB.txt
    res=`cat train$i/result.txt`

    if [[ "$file" =~ .*"$res".* ]]; then
      ((count++))
    fi
  done

  echo "$count" > train"$i"/result.txt
  echo "$total" >> train"$i"/result.txt

  mv training_data.txt train"$i"

done
