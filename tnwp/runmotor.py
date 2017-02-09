from ev3dev.auto import *
import time
from random import choice, randint

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


def forward(speed):
    print("start forward")
    for m in motors:
        m.reset()
        # Speed power
        m.speed_sp = speed
        m.time_sp = 2000
	m.stop(stop_action='brake')
	m.run_timed()


def backward(speed):
    print("start backward")
    for m in motors:
        m.speed_sp = -speed
        m.time_sp = 1500  # milliseconds
        # m.stop(stop_action='coast') # it takes the motor about 1 additional second to actually stop
        m.stop(
            stop_action='brake')  # brake is faster for the motor to stop than coast and it is more difficult to turn after stop.
        m.run_timed()

    # Must have this part of code to wait motors to stop. When motor is stopped, its `state` attribute returns empty list.
    # Othervise, motors will go forward and backward continuously very fast
    while any(m.state for m in motors):
        time.sleep(0.1)


def turn(speed):
    # choice(seq) eg: choice([1,2,3,4,5,6])
    power = (1,-1) #choice([(1, -1), (-1, 1)])
    t = 1000
    for m, p in zip(motors, power):
        m.speed_sp = p * 360
        m.time_sp = t  # milliseconds
        m.stop(stop_action='brake')
        m.run_timed()
    # The same with backward
    while any(m.state for m in motors):
        time.sleep(0.1)


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

def rundemo():
	time.sleep(4.0)
	forward(speed)
	time.sleep(4.0)
	backward(speed)
	time.sleep(2.0)
	turn(speed)
	time.sleep(2.0)
	forward(speed)


def stop():
	for m in motors:
		m.stop()


for m in motors:
	m.stop()
