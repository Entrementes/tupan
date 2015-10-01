#!/usr/bin/python

import socket
from datetime import datetime
import threading
import optparse

parser = optparse.OptionParser()

parser.add_option('-s', '--server', action="store", dest="server")
parser.add_option('-p', '--port', action="store", dest="port")
parser.add_option('-t', '--time', action="store", dest="time", default="0")
parser.add_option('-o', '--output', action="store", dest="output", default="udp_test.csv")
parser.add_option('-i', '--iterations', action="store", dest="iterations", default="10")
parser.add_option('-x', '--threads', action="store", dest="threads", default="10")

options, args = parser.parse_args()

def millis(start_time):
   dt = datetime.now() - start_time
   ms = (dt.days * 24 * 60 * 60 + dt.seconds) * 1000 + dt.microseconds / 1000.0
   return ms

MESSAGE = "[" + str(options.time) + ",{\"userId\":\"gunisalvo\",\"utlitiesProviderId\":\"INFNET\"}]"

print "UDP target IP:", options.server
print "UDP target port:", options.port
print "message:", MESSAGE

threads = []
result = {}

start_test = datetime.now()

def test_udp():
	for t in range(int(options.iterations)):
		start_time = datetime.now()
		failed = False

		try:
			sock = socket.socket(socket.AF_INET, # Internet
			             socket.SOCK_DGRAM) # UDP
			sock.sendto(MESSAGE, (options.server, int(options.port)))
			data, server = sock.recvfrom(4096)
		except socket.error as e:
			failed = True

		tName = threading.currentThread().getName()
		if tName in result:
			result[tName].append({"time" : millis(start_time), "bytes" : len(data), "failed" : str(failed) })
		else:
			result[tName] = [{"time" : millis(start_time), "bytes" : len(data), "failed" : str(failed) }]

for n in range(int(options.threads)):
    thread = threading.Thread(target=test_udp, name= "t[" + str(n) + "]")
    thread.start()
    threads.append(thread)

print "Processing..."

for thread in threads:
    thread.join()

print "Saving results..."

file_ = open(options.output, 'w')

for group in result:
	for r in result[group]:
		file_.write(group + "\t" + str(r["failed"]) + "\t" + str(r["bytes"]) + "\t" + str(r["time"]) + "\n")

file_.write("\n\n" + str(millis(start_test)))

file_.close()