package riws.fb.search;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import riws.google.geolocate.Coord;
import riws.google.geolocate.LocateUtils;

public class Searcher {

	private static final String indexDir = "index";

	public static String search(String text) throws IOException, Exception {
		DirectoryReader ireader = DirectoryReader.open(FSDirectory.open(new File(indexDir)));
		IndexSearcher isearcher = new IndexSearcher(ireader);

		Query query1 = new TermQuery(new Term("name", "sonia"));
		Query query2 = new TermQuery(new Term("from", "basel"));

		BooleanQuery booleanQuery = new BooleanQuery();
		booleanQuery.add(query1, BooleanClause.Occur.MUST);
		booleanQuery.add(query2, BooleanClause.Occur.MUST);

		ScoreDoc[] hits = isearcher.search(booleanQuery, null, 10).scoreDocs;

		processResult(hits, isearcher);

		ireader.close();

		return "heyy";
	}

	public static String fuzzy(String text) throws IOException, Exception {
		DirectoryReader ireader = DirectoryReader.open(FSDirectory.open(new File(indexDir)));
		IndexSearcher isearcher = new IndexSearcher(ireader);

		Query query = new FuzzyQuery(new Term("name", text));

		ScoreDoc[] hits = isearcher.search(query, null, 10).scoreDocs;

		processResult(hits, isearcher);

		ireader.close();

		return "heyy";
	}

	public static String processResult(ScoreDoc[] hits, IndexSearcher isearcher) throws IOException {
		for (ScoreDoc hit : hits) {
			Document hitDoc = isearcher.doc(hit.doc);
			System.out.println(hit.score + "-> Name: " + hitDoc.get("name"));
		}

		fileCoords(hits, isearcher);

		return null;
	}

	public static void multi(String term) throws IOException, Exception {
		DirectoryReader ireader = DirectoryReader.open(FSDirectory.open(new File(indexDir)));
		IndexSearcher isearcher = new IndexSearcher(ireader);

		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_48);

		MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_48, new String[] { "name", "from", "live" }, analyzer);

		Query query = parser.parse(term);

		ScoreDoc[] hits = isearcher.search(query, null, 10).scoreDocs;

		processResult(hits, isearcher);

		ireader.close();
	}

	public static void search(String term, String queryTerm) throws IOException, Exception {
		DirectoryReader ireader = DirectoryReader.open(FSDirectory.open(new File(indexDir)));
		IndexSearcher isearcher = new IndexSearcher(ireader);
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_48);

		QueryParser parser = new QueryParser(Version.LUCENE_48, term, analyzer);
		Query query = parser.parse(queryTerm);

		ScoreDoc[] hits = isearcher.search(query, null, 10).scoreDocs;

		fileCoords(hits, isearcher);

		ireader.close();
	}

	public static void fileCoords(ScoreDoc[] hits, IndexSearcher isearcher) throws IOException {
		PrintWriter writer = new PrintWriter(new FileOutputStream(new File("data/markers.xml"), false));
		writer.println("<rss>");
		writer.println("<channel>");
		for (ScoreDoc hit : hits) {
			Document hitDoc = isearcher.doc(hit.doc);
			Coord coord = LocateUtils.getLocationCoord(hitDoc.get("from"));
			if (coord != null) {
				writer.println("<item>");
				writer.println("<title>" + hitDoc.get("name") + "</title>");
				writer.println("<geo:lat>" + coord.getLat() + "</geo:lat>");
				writer.println("<geo:long>" + coord.getLon() + "</geo:long>");
				writer.println("</item>");
			}
		}
		writer.println("</channel>");
		writer.println("</rss>");
		writer.close();

	}

	public static void main(String[] args) throws Exception {

		// search("name", "sonia");

		// fuzzy("Sonia");
		multi("sonia coruña");

		// PApplet.main(new String[] { SimpleMapApp.class.getName() });

	}
}
