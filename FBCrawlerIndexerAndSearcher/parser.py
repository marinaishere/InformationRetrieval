#!/usr/bin/python
import os,config,parse_functions

##########################
# Configuration constants
##########################
folder = config.data_folder
friends_ext = config.friends_file_ext
biography_ext = config.biography_file_ext
	
def write_info(bio_filename):
	bio_info = parse_functions.parse_bio(bio_filename)
	friend_list = parse_functions.parse_friends(bio_filename.replace(biography_ext,friends_ext))
	file = open("Data/"+bio_filename.replace(biography_ext,".txt"), 'w')
	file.write(bio_info)
	file.write("Friends: "+str(friend_list) + '\n')
	file.close()


################
# Main process
################

num=0

for file in os.listdir(folder):
    if file.endswith(biography_ext):
    	write_info(file)
	print 'Extracted info for user ' + str(file)
	num+=1

print '**********************************************'
print ' Info extracted from ' + str(num) + ' users'
print '**********************************************'
