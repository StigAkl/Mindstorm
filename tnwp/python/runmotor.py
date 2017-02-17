from ev3dev.auto import *
import time
from random import choice, randint
from speak import *
# port A and B
addre = ['outA', 'outB']
motors = [LargeMotor(a) for a in addre]
assert all([m.connected for m in motors]), "Two large motors should be connected to ports B and C"

# b otton
btn = Button()

# speed
speed = 500

us = UltrasonicSensor();
assert us.connected
cs = ColorSensor();
assert cs.connected
cs.mode = 'COL-REFLECT'
print(us.connected)
print(cs.connected)


def forward(speed, ti):
    print("start forward")
    for m in motors:
        m.reset()
        # Speed power
        m.speed_sp = speed
        m.time_sp = ti
	m.stop(stop_action='brake')
	m.run_timed()


def backward(speed, ti):
    print("start backward")
    for m in motors:
        m.speed_sp = -speed
        m.time_sp = ti  # milliseconds
        # m.stop(stop_action='coast') # it takes the motor about 1 additional second to actually stop
        m.stop(
            stop_action='brake')  # brake is faster for the motor to stop than coast and it is more difficult to turn after stop.
        m.run_timed()

    # Must have this part of code to wait motors to stop. When motor is stopped, its `state` attribute returns empty list.
    # Othervise, motors will go forward and backward continuously very fast
    #while any(m.state for m in motors):
        #time.sleep(0.1)


def turn(direction, ti):
    # choice(seq) eg: choice([1,2,3,4,5,6])
	if(direction == "left"):
    		power = (1,-1) #choice([(1, -1), (-1, 1)])
    	if(direction == "right"):
		power = (-1, 1)
	t = ti #430 for 90 grader
    	for m, p in zip(motors, power):
        	m.speed_sp = p * 240
        	m.time_sp = t  # milliseconds
        	m.stop(stop_action='brake')
        	m.run_timed()
    	# The same with backward
    	#while any(m.state for m in motors):
        	#time.sleep(0.1)


def getValueOfUs():
    valueOfUs = us.value()
    print("The value of UltrasonicSensor: " + str(valueOfUs))
    return valueOfUs


def getValueOfCs():
    valueOfCs = cs.value()
    print("The value of ColorSensor: " + str(valueOfCs))
    return valueOfCs


def run():
	turn(5)
	t1 = time.time()
	forward(speed)
	t2 = time.time()
   	print(t2 - t1)
 	distance = getValueOfUs()
   	color = getValueOfCs()
    	if distance < 320 or color < 2:
        	backward(speed)
        	turn(speed)


# Run demo

def rundemo(y):
	x = y
	t = 500
	ti = t/1000
	while x > 0:
		action = randint(0,3)
		if(action == 0):
			distance = getValueOfUs()
			color = getValueOfCs()
			if distance > 320 or color > 10:
				print("FORWARD")
				forward(500, t)
			else: 
				#SPEAK
				talk("ooops that's too close")
		elif(action == 1):
			print("BACKWARD")
			backward(500, t)
		elif(action == 2):
			print("LEFT")
			turn("left", t)
		elif(action == 3):
			print("RIGHT")
			turn("right", t)
		x -= 1
		time.sleep(ti)

def stop():
	for m in motors:
		m.stop()


for m in motors:
	m.stop()
