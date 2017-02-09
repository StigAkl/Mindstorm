from bluetooth import *
from runmotor import *
import time
server_sock=BluetoothSocket(RFCOMM)
server_sock.bind(("", PORT_ANY))
server_sock.listen(1)

port = server_sock.getsockname()[1]

uuid = "94f39d29-7d6d-973b-fba39e49d4ee"

def listen():
	x = 0
	while x < 3:
		print("Waiting for connection on RFCOMM channel ")
		client_sock, client_info = server_sock.accept()
		print("Accepted connection from ")
		x = 1
		choice = 99
		while x == 1:
			try:
				data = client_sock.recv(1024)
				if len(data) == 0: break
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
				else:
					x = 5
					choice = 5

			except IOError as e:
				print
				choice = 6
				pass
			

			client_sock.close()
			return choice


#except KeyboardInterrupt:
#
#			print("disconnected")
#
#			client_sock.close()
#			server_sock.close()
#			print("all done")
#
#			break

	if x == 5:
		print("disconnected")
		client_sock.close()
		server_sock.close()
		print("all done")


def main():
	while True: 
		choice = listen()
		if(choice == 0):
			print("TEMP")
		elif(choice == 1):
			print("FORWARD")
			forward(500)
		elif(choice == 2):
			print("BACKWARD")
			backward(500)
		elif(choice == 3):
			print("TURN RIGHT")
			turn(500)
		elif(choice == 4):
			print("TURN LEFT")
			turn(500)
		elif(choice == 5):
			print("DEFAULT")
		elif(choice == 6):
			print("[ERROR]: READING DATA")
		else:
			print("DEFAULT")


if __name__ == "__main__": main()
