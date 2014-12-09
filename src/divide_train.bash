#!/bin/bash

#Creates folders for cross validation
#Each with a randomly selected test set
#and the corresponding training set
#separated into respective subfolders
#-Run in folder containing directory "train"

#Attribution of training set to each
#randomly created test set
function subdiv {
	for i in {1..10}
	do
		mkdir train"$i"/train train"$i"/test
		mv train"$i"/*.txt train"$i"/test #move all testing files to test dir
	done

	for i in {1..10}
	do
		for j in {1..10}
		do
			if [ "$i" -ne "$j" ]; then #move all files from test folders to training in this
				cp train"$j"/test/*.txt train"$i"/train
			fi
		done
	done
}

#Remove any existing directories named train{1..10}
#Create new such directories
for (( i = 1; i < 11; i++ )); do
	rm -r "train$i"
	mkdir "train$i"
done

for(( i = 0; i < 10; i++)); do
	count["$i"]=0
done

limit=249

#Creation of random test sets from train/ directory
#Random selection of one of the train{1..10} directories
#Copy of the current file from train/ to that directory
for email in train/*; do
	rand=$[ ( $RANDOM % 10 )  + 1 ]
	while [[ ${count[${rand}]} -gt $limit ]]; do
		rand=$[ ( $RANDOM % 10 )  + 1 ]
	done
	rand_count=${count[${rand}]}
	cp $email "train$rand"
	count[${rand}]=`expr $rand_count + 1`
done

subdiv
