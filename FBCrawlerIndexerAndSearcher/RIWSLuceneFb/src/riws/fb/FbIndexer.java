package riws.fb;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import riws.fb.model.UserInfo;

public class FbIndexer {

	private static final String userDocs = "userdata";
	private static final String indexDir = "index";

	public static void main(String[] args) throws IOException, ParseException {

		// Get all users from files
		List<UserInfo> users = DataExtractor.extract(userDocs);
		System.out.println("----------------------------------------------");
		System.out.println("\t Extracted info for " + users.size() + " users");
		System.out.println("----------------------------------------------");

		// Create a standard analyzer
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_48);
		// Create a disk index
		Directory directory = FSDirectory.open(new File(indexDir));
		// Configure index
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_48, analyzer);
		// Create index writer
		IndexWriter iwriter = new IndexWriter(directory, config);

		// Get a document for each user and add to index
		for (UserInfo userInfo : users) {
			System.out.println("Adding document of user with nick " + userInfo.getNick());
			iwriter.addDocument(userInfo.getDocument());
		}
		System.out.println("----------------------------------------------");
		System.out.println("\t Added " + users.size() + " documents");
		System.out.println("----------------------------------------------");

		// Close index writer
		iwriter.close();
		// Close index directory
		directory.close();

	}
}
