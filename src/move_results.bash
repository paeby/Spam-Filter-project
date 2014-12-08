#!/bin/bash

if [[ $# -ne 1 ]]; then
  echo "Illegal number of parameters. Correct usage is ./moveres.bash destination_folder_name"
  exit 1;
fi

dest="$1"
newDest=Results/"$dest"
mkdir Results/"$dest"

for i in {1..10}
do
  echo "Copying contents of train$i to Results/$dest/train$i ..."
  mkdir "$newDest"/train"$i"
  cp -r train"$i"/* "$newDest"/train"$i"/
  echo "Done - Removing unneeded files from train$i now..."
  rm train"$i"/*.txt
  rm -r train"$i"/misclassified
  echo "Done"
done
