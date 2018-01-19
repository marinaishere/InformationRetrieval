package riws.fb.model;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.TextField;

public class UserInfo {

	private String nick;
	private String name;
	private String studied;
	private String studiedAt;
	private int friendsTotal;
	private String bornOn;
	private String livesIn;
	private String worksAt;
	private String from;
	private String marriedTo;
	private String relationshipWith;
	private String followedBy;
	private String wentTo;
	private String goesTo;
	private String[] friends;

	public UserInfo(String nick, String name, String studied, String studiedAt, int friendsTotal, String bornOn, String livesIn, String worksAt,
			String from, String marriedTo, String relationshipWith, String followedBy, String wentTo, String goesTo, String[] friendList) {
		this.nick = nick;
		this.name = name;
		this.studied = studied;
		this.studiedAt = studiedAt;
		this.friendsTotal = friendsTotal;
		this.bornOn = bornOn;
		this.livesIn = livesIn;
		this.worksAt = worksAt;
		this.from = from;
		this.marriedTo = marriedTo;
		this.relationshipWith = relationshipWith;
		this.followedBy = followedBy;
		this.wentTo = wentTo;
		this.goesTo = goesTo;
		this.friends = friendList;
	}

	public Document getDocument() {
		Document doc = new Document();
		doc.add(new TextField("nick", nick, Field.Store.YES));
		if (name != null)
			doc.add(new TextField("name", name, Field.Store.YES));
		if (studied != null)
			doc.add(new TextField("studied", studied, Field.Store.YES));
		if (studiedAt != null)
			doc.add(new TextField("studiedAt", studiedAt, Field.Store.YES));
		if (friendsTotal > 0)
			doc.add(new IntField("totalFriends", friendsTotal, Field.Store.NO));
		if (bornOn != null)
			doc.add(new TextField("born", bornOn, Field.Store.YES));
		if (livesIn != null)
			doc.add(new TextField("live", livesIn, Field.Store.YES));
		if (worksAt != null)
			doc.add(new TextField("work", worksAt, Field.Store.YES));
		if (from != null)
			doc.add(new TextField("from", from, Field.Store.YES));
		if (marriedTo != null)
			doc.add(new TextField("married", marriedTo, Field.Store.YES));
		if (relationshipWith != null)
			doc.add(new TextField("relation", relationshipWith, Field.Store.YES));
		if (followedBy != null)
			doc.add(new TextField("followed", followedBy, Field.Store.YES));
		if (wentTo != null)
			doc.add(new TextField("went", wentTo, Field.Store.YES));
		if (goesTo != null)
			doc.add(new TextField("goes", goesTo, Field.Store.YES));
		if (friends != null) {
			for (String friend : friends) {
				doc.add(new TextField("friends", friend, Field.Store.YES));
			}
		}
		return doc;
	}

	public String toString() {
		String text = name + "\n\t" + studied + " | " + studiedAt + " | " + friendsTotal + "\n\t" + bornOn + " | " + livesIn + " | " + worksAt
				+ "\n\t" + from + " | " + marriedTo + " | " + relationshipWith + "\n\t" + followedBy + " | " + wentTo + " | " + goesTo + "\n\t";
		if (friends != null) {
			text += "[";
			for (String friend : friends) {
				text += friend;
			}
			text += "]";
		}
		return text;

	}

	public String getNick() {
		return nick;
	}

	public String getName() {
		return name;
	}

	public String getStudied() {
		return studied;
	}

	public String getStudiedAt() {
		return studiedAt;
	}

	public int getFriendsTotal() {
		return friendsTotal;
	}

	public String getBornOn() {
		return bornOn;
	}

	public String getLivesIn() {
		return livesIn;
	}

	public String getWorksAt() {
		return worksAt;
	}

	public String getFrom() {
		return from;
	}

	public String getMarriedTo() {
		return marriedTo;
	}

	public String getRelationshipWith() {
		return relationshipWith;
	}

	public String getFollowedBy() {
		return followedBy;
	}

	public String getWentTo() {
		return wentTo;
	}

	public String getGoesTo() {
		return goesTo;
	}

	public String[] getFriends() {
		return friends;
	}

}
