import sys
from datetime import date

if __name__ == "__main__":
	file = open(sys.argv[1], "a")
	file.write("bw; " + sys.argv[2] + "\n")
	file.close()