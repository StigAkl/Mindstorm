from bluetooth import *
from runmotor import *
from speak import *
from threading import Thread
import time
import socket
import subprocess

server_sock=BluetoothSocket(RFCOMM)
server_sock.bind(("", PORT_ANY))
server_sock.listen(1)
port = server_sock.getsockname()[1]
client_sock = None
client_info = None
data = None
uuid = "94f39d29-7d6d-973b-fba39e49d4ee"

print("Waiting for connection on RFCOMM channel ")
client_sock, client_info = server_sock.accept() 
print("accepted connection from android device")
output = subprocess.Popen('/home/robot/tnwp/bin/play.sh', shell=True, stdout=subprocess.PIPE)
output.wait()
print output.returncode
def createConnection():
	data = None
	client_sock = None
	client_info = None
	server_sock=BluetoothSocket(RFCOMM)
	server_sock.bind(("", PORT_ANY))
	server_sock.listen(1)
	port = server_sock.getsockname()[1]
	print("Waiting for connection on RFCOMM channel ") 
	client_sock, client_info = server_sock.accept() 
	print("accepted connection from android device")
	main()

def listen():
	x = 0
	print("Reading data")   
	while x < 3:
		x = 1
		choice = 99
		while x == 1:
			try:
				print("Before recv(1024) call")
				data = client_sock.recv(1024)
				if not data:
					x = 3
					break
				print("received " + data)

				if data == 'temp':
					data = str(read_temp())+'!'
					choice = 0
				elif data == 'a':
					#FORWARD
					choice = 1
				elif data == 'b':
					#BACKWARD
					choice = 2
				elif data == 'c':
					#RIGHT
					choice = 3
				elif data == 'd':
					#LEFT
					choice = 4
				elif data == 'e':
					#STOP
					choice = 7
				elif data[0] == "#":
					if(data == "#thestigmode69"):
						choice = 98
					else:
						choice = 5
				else:
					choice = 99

			except IOError as e:
				choice = 6
				pass
			

			return (choice, data)
	if x == 5:
		print("disconnected")


def controll(action):
	distance = getValueOfUs()
	color = getValueOfCs()
	print("DISTANCE: ")
	print(distance)
	print("COLOR: ")
	print(color)
	if(action == "FORWARD"):
		if distance > 320 and color > 10:
			forward(500, 100000)
		else:
			talk("Something is in the way, master")
			talk("turn around")
	elif(action == "BACKWARD"):
		backward(500, 100000)
	elif(action == "RIGHT"):
		turn("right", 100000)
	elif(action == "LEFT"):
		turn("left", 100000)
	elif(action == "STOP"):
		stop()
	else:
		stop()	

def main():
	run = True
	while run:
		(choice, data) = listen()
		if(choice == 0):
			print("TEMP")
		elif(choice == 1):
			controll("FORWARD")
			#forward(500)
		elif(choice == 2):
			controll("BACKWARD")
			#backward(500)
		elif(choice == 3):
			controll("RIGHT")
			#turn("right")
		elif(choice == 4):
			controll("LEFT")
			#turn("left")
		elif(choice == 5):
			#Text to voice
			print(data)
			talk(data)		
		elif(choice == 6):
			stop()
			print("[ERROR]: READING DATA")
			client_sock.close()
			server_sock.close()
			print("Sockets closed")
			run = False
		elif(choice == 7):
			controll("STOP")
			#stop()
		elif(choice == 98):
			rundemo(10)
		else:
			print("DEFAULT")


if __name__ == "__main__": main()
