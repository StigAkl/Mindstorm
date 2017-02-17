#!/bin/sh

python python/start_up_small.py
while [ 0 -eq 0 ]
do
	python python/server.py
	python python/speak2.py
done
