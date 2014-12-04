#!/bin/bash
clear
total=0

echo "--------Creating training data--------"
echo
java filter_train train1/train
echo "--------Testing files--------"
echo
count=0
for file in train1/test/*
do
  ((total++))
  java filterb "$file" > train1/result.txt
  res=`cat train1/result.txt`
  echo "result = $res"
  echo "$file"

  if [[ "$file" =~ .*"$res".* ]]; then
    ((count++))
  fi
  echo "total = $total"
  echo "count = $count"
done

error="$count / $total"

mv training_data.txt train1
