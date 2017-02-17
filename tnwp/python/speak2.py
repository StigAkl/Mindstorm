import subprocess


process = subprocess.Popen('/home/robot/tnwp/bin/speak.sh "Connection lost!"', shell=True, stdout=subprocess.PIPE)
process.wait()
print process.returncode

