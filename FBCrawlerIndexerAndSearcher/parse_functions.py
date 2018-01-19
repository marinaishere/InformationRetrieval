#!/usr/bin/python
import re,os,config

##########################
# Configuration constants
##########################
folder = config.data_folder

def parse_friends(filename):
	friend_names = []
	f = open(folder + '/' + filename,'r')
	text = f.read()
	f.close
	result = re.findall('https://www\.facebook\.com/.{1,30}fref',text)
	i=0
	while i<len(result):
		url = result[i]
		i+=2
		name = url.split('/',3)[3].split('?')[0]
		friend_names.append(name)
	return friend_names

def parse_bio(name):
	f = open(folder + "/"+ name, 'r')
	text = str(f.read())
	f.close()

	matchWork = re.match( r'(.*Works at[^>]+>)([^<]+)<', text,re.S)
	matchFrom = re.match( r'(.*From [^>]+>)([^<]+)<', text,re.S)
	matchFriends = re.match( r'(.*/friends">)([0-9]+)<', text,re.S)
	matchFollowed = re.match( r'(.*Followed by[^>]+>)([^<]+)<', text,re.S)
	matchLives = re.match( r'(.*Lives in[^>]+>)([^<]+)<', text,re.S)
	matchBornOn = re.match( r'(.*Born on[^>]+>)([^<]+)<', text,re.S)
	matchStudied = re.match( r'(.*Studie[d|s] )([^<]+)<[^>]+>([^<]+)<', text,re.S)
	matchName = re.match( r'(.*fb-timeline-cover-name[^>]+>)([^<]+)<', text,re.S)
	matchMarriedTo = re.match( r'(.*Married to [^>]+>)([^<]+)<', text,re.S)
	matchRelationship = re.match( r'(.*In a relationship with [^>]+>)([^<]+)<', text,re.S)
	matchWent = re.match( r'(.*Went to [^>]+>)([^<]+)<', text,re.S)
	matchGoes = re.match( r'(.*Goes to [^>]+>)([^<]+)<', text,re.S)

	s =  "Name: " + matchName.group(2) +"\n" if matchName else ""
	if (matchStudied and matchStudied.group(3)):
		s += "Studied: " + matchStudied.group(2)[0:-4] +"\n" if matchStudied.group(2)[0:-4] != "" else ""
		s += "Studied at: " + matchStudied.group(3) +"\n"
	s +=  "Friends: " + matchFriends.group(2) +"\n" if matchFriends else ""
	s +=  "Born on:  " + matchBornOn.group(2) +"\n" if matchBornOn else ""
	s +=  "Lives in: " + matchLives.group(2) +"\n" if matchLives else ""
	s +=  "Works at: " + matchWork.group(2) +"\n" if matchWork else ""
	s +=  "From: " + matchFrom.group(2) +"\n" if matchFrom else ""
	s +=  "Married to: " + matchMarriedTo.group(2) +"\n" if matchMarriedTo else ""
	s +=  "In a relationship with: " + matchRelationship.group(2) +"\n" if matchRelationship else ""
	s +=  "Followed By: " + matchFollowed.group(2) +"\n" if matchFollowed else ""
	s +=  "Went to: " + matchWent.group(2) +"\n" if matchWent else ""
	s +=  "Goes to: " + matchGoes.group(2) +"\n" if matchGoes else ""
	return s
