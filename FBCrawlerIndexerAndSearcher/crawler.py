#!/usr/bin/python
import os
from multiprocessing import Pool
import config
import parse_functions
import time

##########################
# Configuration constants
##########################
friends_ext = config.friends_file_ext
biography_ext = config.biography_file_ext
seed = config.user_seed
n_it = config.max_depth
folder = config.data_folder
crawled_file=config.crawled_users

##########################
# Imported functions
##########################
def search_friends(username):
	return parse_functions.parse_friends(username+friends_ext)

##########################
# Crawling start
##########################
command = 'wget -q'
fbini = ' "https://www.facebook.com/'
fbend_biography = '"'
fbend_friend = '/friends"'
cookies =  ' --load-cookies cookies.txt'
user_agent = ' --user-agent="Mozilla/5.0 \(Windows NT 6.2; rv:10.0\) Gecko/20100101 Firefox/33.0"'
output_doc = ' --output-document=' 

start_url_friends = command + output_doc + folder +'/' + seed + friends_ext + fbini + seed + fbend_friend + cookies + user_agent
start_url_biography = command + output_doc + folder + '/' +  seed + biography_ext + fbini + seed + fbend_biography + cookies + user_agent
		

def run_command(wget):
	os.system(wget)
	print 'User info saved | ' + wget.split(output_doc)[1].split(fbini)[0]
	
current_milli_time = lambda: int(round(time.time() * 1000))

depth = 0;

urls = []

#Initialize user seed to start crawling
usernames=[]
usernames.append(seed)

user_friends = []

crawled = []

f = open(crawled_file, 'a')
f.close()
f = open(crawled_file, 'r')
for user in f.readlines():
	crawled.append(user.strip())
f.close()

if not (seed in crawled):
	urls.append(start_url_friends)
	urls.append(start_url_biography)

pool = Pool()

start = current_milli_time()

while depth<n_it:
	if len(urls)>0:
		pool.map(run_command,urls)
		#for url in urls:
		#	run_command(url)
		#	time.sleep(5)
	del urls[:]
	del user_friends[:]
	for user in usernames:
		if not (user in crawled):
			crawled.append(user)
		user_friends += search_friends(user)
	del usernames[:]
	for friend in user_friends:
		url_friends = command + output_doc + folder + '/' + friend + friends_ext + fbini + friend + fbend_friend + cookies + user_agent
		url_biography = command + output_doc + folder + '/' + friend + biography_ext + fbini + friend + fbend_biography + cookies + user_agent
		if not (friend in crawled):
			urls.append(url_friends)
			urls.append(url_biography)
			usernames.append(friend)
	depth+=1;

f = open(crawled_file, 'w')
for user in crawled:
	f.write(user+"\n")
f.close()

end = current_milli_time() - start

pool.close()
pool.join()

print '*******************************************************************'
print ' Crawled ' + str(len(crawled)) + ' users in ' + str(end/1000) + ' secs'
print '*******************************************************************'
