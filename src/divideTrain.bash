#!/bin/bash

for (( i = 1; i < 11; i++ )); do
	mkdir "train$i"
done

count1=0
count2=0
count3=0
count4=0
count5=0
count6=0
count7=0
count8=0
count9=0
count10=0

filecount=0

for email in train/*; do
	filecount=`expr $filecount + 1`
done

limit=250

for email in train/*; do
	rand=$[ ( $RANDOM % 10 )  + 1 ]
	count="count"
	randCount=$count$rand
	while [[ $randCount -gt $limit ]]; do
		rand=$[ ( $RANDOM % 10 )  + 1 ]
	done
	cp $email "train$rand"
done