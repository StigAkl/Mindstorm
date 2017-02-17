import subprocess


def talk(text):
	print(text[1:])
	process = subprocess.Popen('/home/robot/tnwp/bin/speak.sh "' + text[1:] + '"', shell=True, stdout=subprocess.PIPE)
	process.wait()
	print process.returncode

