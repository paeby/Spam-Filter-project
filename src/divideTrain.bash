#!/bin/bash

for (( i = 1; i < 11; i++ )); do
	rm -r "train$i"
	mkdir "train$i"
done

for(( i = 0; i < 10; i++)); do
	count["$i"]=0
done

limit=249

for email in train/*; do
	rand=$[ ( $RANDOM % 10 )  + 1 ]
	while [[ ${count[${rand}]} -gt $limit ]]; do
		rand=$[ ( $RANDOM % 10 )  + 1 ]
	done
	rand_count=${count[${rand}]}
	cp $email "train$rand"
	count[${rand}]=`expr $rand_count + 1`
	echo `expr $rand_count + 1`
done
