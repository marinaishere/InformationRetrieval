package riws.fb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import riws.fb.model.UserInfo;

public class DataExtractor {

	/**
	 * Extracts all info of user info files in a given folder
	 * 
	 * @param folderPath
	 *            Folder name
	 * @return List of user info objects
	 * @throws IOException
	 */
	public static List<UserInfo> extract(String folderPath) throws IOException {
		List<UserInfo> listData = new ArrayList<UserInfo>();
		File folder = new File(folderPath);
		if (folder.isDirectory()) {
			for (File f : folder.listFiles()) {
				System.out.println("Extracting data of file " + f.getName());
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
				String text = br.readLine();
				while (text != null) {
					String aux = br.readLine();
					if (aux != null) {
						text += "\n" + aux;
					} else {
						break;
					}
				}
				UserInfo info = parseUserData(f.getName().substring(0, f.getName().length() - 4), text);
				listData.add(info);
				br.close();
			}
		}
		return listData;
	}

	/**
	 * Gets a object representation for the user info
	 * 
	 * @param info
	 *            String with user info
	 * @return Object representation
	 */
	private static UserInfo parseUserData(String nick, String info) {
		String[] lines = info.split("\n");
		String name, studied, studiedAt, bornOn, livesIn, worksAt, from, marriedTo, relationWith, followedBy, wentTo, goesTo;
		name = studied = studiedAt = bornOn = livesIn = worksAt = from = marriedTo = relationWith = followedBy = wentTo = goesTo = null;
		int friendsTotal = 0;
		String[] friendList = null;
		for (String line : lines) {
			int index = line.indexOf(':') + 1;
			name = line.startsWith("Name:") ? line.substring(index).trim() : name;
			studied = line.startsWith("Studied:") ? line.substring(index).trim() : studied;
			studiedAt = line.startsWith("Studied at:") ? line.substring(index).trim() : studiedAt;
			friendsTotal = line.startsWith("Friends:") ? Integer.parseInt(line.substring(index).trim()) : friendsTotal;
			bornOn = line.startsWith("Born on:") ? line.substring(index).trim() : bornOn;
			livesIn = line.startsWith("Lives in:") ? line.substring(index).trim() : livesIn;
			worksAt = line.startsWith("Works at:") ? line.substring(index).trim() : worksAt;
			from = line.startsWith("From:") ? line.substring(index).trim() : from;
			marriedTo = line.startsWith("Married to:") ? line.substring(index).trim() : marriedTo;
			relationWith = line.startsWith("In a relationship with:") ? line.substring(index).trim() : relationWith;
			followedBy = line.startsWith("Followed By:") ? line.substring(index).trim() : followedBy;
			wentTo = line.startsWith("Went to:") ? line.substring(index).trim() : wentTo;
			goesTo = line.startsWith("Goes to:") ? line.substring(index).trim() : goesTo;
			friendList = line.startsWith("Friends list:") ? line.substring(line.indexOf('[') + 1, line.length() - 1).split(",") : friendList;
		}
		return new UserInfo(nick, name, studied, studiedAt, friendsTotal, bornOn, livesIn, worksAt, from, marriedTo, relationWith, followedBy, wentTo,
				goesTo, friendList);
	}

}
