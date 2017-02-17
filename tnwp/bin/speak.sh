#!/bin/sh

str=$1

espeak "$str" --stdout | aplay
