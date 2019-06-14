#!/bin/sh

echo 'START GENERATING PLOTS';
./temp.sh 2016-06-08_18:25:50.552;
echo '1/8';
./temp.sh 2016-06-08_18:26:10.399;
echo '2/8';
./temp.sh 2016-06-09_19:42:46.978;
echo '3/8';
./temp.sh 2016-06-09_19:50:51.983;
echo '4/8';
./temp.sh 2016-06-15_21:24:12.746;
echo '5/8';
./temp.sh 2016-06-15_15:15:45.550;
echo '6/8';
./temp.sh 2016-06-28_20:27:44.894;
echo '7/8';
./temp.sh 2016-06-16_16:29:24.401;

echo 'DONE GENERATING PLOTS';
echo 'START COPYING';
python copy.py;
echo 'DONE COPYING';
