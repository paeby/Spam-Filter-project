#!/bin/bash

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

for (( i = 1; i < 11; i++ )); do
	rm -r "train$i"
	mkdir "train$i"
done

for(( i = 0; i < 10; i++)); do
	count["$i"]=0
done

limit=250

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
