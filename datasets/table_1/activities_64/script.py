from os import system as cmd
from sys import argv
import glob, os, sys
import subprocess
import re
from collections import Counter


number = "7"
file = number + ".xes"
output = "created" + number + ".xes"

trace = 4

head1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"
head2 = "<log xes.version=\"1.0\" xes.features=\"nested-attributes\" openxes.version=\"1.0RC7\" xmlns=\"http://www.xes-standard.org/\">\n"

fp = open(file, 'r')
fw = open(output, 'w')
fw.writelines(head1)
fw.writelines(head2)
line = fp.readline()

count = 0
while (line):
	if (line[1:-1] == "<trace>"):
		fw.writelines(line[1::])

	if (line[2:-1] == "<event>"):
		fw.writelines(line[2::])

	if (line[3:29] == "<string key=\"concept:name\""):
		fw.writelines(line[3::])

	if (line[2:-1] == "</event>"):
		fw.writelines(line[2::])

	if (line[1:-1] == "</trace>"):
		fw.writelines(line[1::])
		count += 1

	if (count == trace):
		line = fp.readline()
		break

	line = fp.readline()

print(count)


os.mkdir("./goal"+number+"/")

count = 0
while (line):
	if (line[1:-1] == "<trace>"):
		fs = open("./goal"+number+"/sas_plan."+str(count), 'w')

	if (line[3:29] == "<string key=\"concept:name\""):
		parts = line.split('\"')
		fs.writelines(parts[3]+"\n")

	if (line[1:-1] == "</trace>"):
		fs.writelines(";cost\n")
		fs.close()
		count += 1

	if (count == trace*0.5):
		break

	line = fp.readline()


tail = "</log>\n"
fw.writelines(tail)
fp.close()
fw.close()


