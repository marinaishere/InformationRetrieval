import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

public class QueryLauncher {

	public static void main(String[] args) {

		//showfield puede mostrar un campo diferenta al que estamos usando para buscar
		//multiquery tiene que ser el ultimo argumento

		/*
		 DEFENSA:
		 
		 -index C:/Users/Acer/workspace/index
		 -showfield body
		 -showfield title
		 
		 -query body fishing
		 -out C:/Users/Acer/workspace/index/p3IndexCreate
			 
		 -multiquery title house body house	 
		 -progquery body -and said field -or house -not director
		 
		 */
		

		
		//Tienen que ir en ese orden:
		String usage = "[-index indexfile] [-showfield fld] [-out indexfile] [-query fld1 “query”] "
				+ "[-multiquery fld1 “query1” fld2 “query2” … ]"
				+ "[-progquery fld1 -and term1 term2 … -or termi termj … -not termp termq …]  ";
		
		String indexPath = null;
		boolean showField = false;
		boolean createIndex = false;
		boolean progQuery = false;
		String fieldToShow = null;
		String queryInput = null;
		String fieldQuery = null;
		ArrayList<String> fieldList = new ArrayList<String>();
		ArrayList<String> queryInputList = new ArrayList<String>();
		ArrayList<String> mustList = new ArrayList<String>();
		ArrayList<String> mustNotList = new ArrayList<String>();
		ArrayList<String> shouldList = new ArrayList<String>();
		String createIndexPath = null;
		HashSet<Document> documentSet = new HashSet<Document>();
		
		for(int i=0;i<args.length;i++)
		     if ("-index".equals(args[i])) {
		       indexPath = args[i+1];
		       i++;
		     } else if ("-showfield".equals(args[i])) {    
			     showField = true;
			     fieldToShow = args[i+1];
			     i++;
		     } else if ("-query".equals(args[i])) {    
		    	 fieldQuery = args[i+1];
				 queryInput = args[i+2];
		    	 i+=2; 
			  }  else if ("-multiquery".equals(args[i])) {
				 for(int j=i;j<args.length -1;j++){
					 fieldList.add(args[j+1]);
					 queryInputList.add(args[j+2]);
				     j+=1;
				 }
			  } else if ("-progquery".equals(args[i])) {
				 progQuery = true;
			     fieldQuery = args[i+1];
			     int j = i+2;
			     if (args[j].equals("-and")){
			    	 j++;
			    	 while(j < args.length && !args[j].equals("-or") && !args[j].equals("-not")){
				    	 mustList.add(args[j]);
				    	 j++;
				     }
			     }
			     if (args[j].equals("-or")){
			    	 j++;
			    	 while(j < args.length && !args[j].equals("-and") && !args[j].equals("-not")){
			    		 shouldList.add(args[j]);
				    	 j++;
				     }
			     }
			     if (args[j].equals("-not")){
			    	 j++;
			    	 while(j < args.length && !args[j].equals("-and") && !args[j].equals("-or")){
			    		 mustNotList.add(args[j]);
				    	 j++;
				     }			
			     }
			     i = j;
			  } else if ("-out".equals(args[i])) {
				  createIndexPath = args[i+1];
				  createIndex = true;
				  i++;
			      System.out.println("Creating index to the path " + createIndexPath + "...");
			  }

	
		if (fieldList.size() != queryInputList.size()){
			System.out.println(usage);
			System.exit(1);
		}
		
		File file = new File(indexPath);
		IndexReader reader = null;
		Directory dir = null;
		IndexSearcher searcher = null;
		QueryParser parser;
		Query query = null;

		try {
			dir = FSDirectory.open(file);
			reader = DirectoryReader.open(dir);
		} catch (CorruptIndexException e1) {
			System.out.println("Exception " + e1);
			e1.printStackTrace();
		} catch (IOException e1) {
			System.out.println("Exception " + e1);
			e1.printStackTrace();
		}

		searcher = new IndexSearcher(reader);
		//fieldlist.size tienen los mismos elementos que el numero de querys a hacer si hay que hacer varias
		for (int j = 0; j <= fieldList.size(); j++) {
			//la ultima iteracion no la tengo  que hacer porque me salgo del rango
			if (fieldList.size() != 0 && j == fieldList.size()){
				break;
			}
			//si tengo mas de una query, asigno los valores
			if (fieldList.size() != 0){
				fieldQuery = fieldList.get(j);
				queryInput = queryInputList.get(j);
			}
			
			parser = new QueryParser(Version.LUCENE_40, fieldQuery, new StandardAnalyzer(Version.LUCENE_40));
			
			if (progQuery){
				//si es una query programática la construyo con las tres listas y lo asigno a la variable query
				BooleanQuery booleanQuery = new BooleanQuery();
				Query auxQuery = null;
				for (int i = 0; i < mustList.size(); i++) {
					auxQuery = new TermQuery(new Term(fieldQuery, mustList.get(i)));
					booleanQuery.add(auxQuery, Occur.MUST);
				}
				for (int i = 0; i < mustNotList.size(); i++) {
					auxQuery = new TermQuery(new Term(fieldQuery, mustNotList.get(i)));
					booleanQuery.add(auxQuery, Occur.MUST_NOT);
				}
				for (int i = 0; i < shouldList.size(); i++) {
					auxQuery = new TermQuery(new Term(fieldQuery, shouldList.get(i)));
					booleanQuery.add(auxQuery, Occur.SHOULD);
				}	
				query = booleanQuery;
			} else
				//si no, la query es una normal
				try {
					query = parser.parse(queryInput);
				} catch (ParseException e) {
					System.out.println("Exception " + e);
					e.printStackTrace();
				}
				
			TopDocs topDocs = null;
	
			try {
				//muestro siempre como maximo 10 elementos
				topDocs = searcher.search(query, 10);
			} catch (IOException e1) {
				System.out.println("Exception " + e1);
				e1.printStackTrace();
			}
			System.out.print("\n" + topDocs.totalHits	+ " results for query \"" + query.toString()
				+ "\" \nshowing for the first " + 10 + " documents the doc id");
			if (showField)
				System.out.println(", score and the content of the "+ fieldToShow + " field...\n");
			else 
				System.out.println(" and score...\n");
	
			for (int i = 0; i < Math.min(10, topDocs.totalHits); i++) {
				try {
					System.out.println(topDocs.scoreDocs[i].doc	+ " -- score: "	+ topDocs.scoreDocs[i].score);
					//si tengo que mostrar el campo lo muestro
					if (showField)	
						System.out.println(fieldQuery.toUpperCase()+ " : " + 
							reader.document(topDocs.scoreDocs[i].doc).get(fieldToShow) +"\n");					
				} catch (CorruptIndexException e) {
					System.out.println("Exception " + e);
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println("Exception " + e);
					e.printStackTrace();
				}
			}
			
			// y si tengo que crear el indice añado al set todos los documentos
			//para lo cual tengo que volver a lanzar la query para no añadir solo 10 elementos
			if (createIndex){	
				try {
					topDocs = searcher.search(query, Integer.MAX_VALUE);
					for (int i = 0; i < topDocs.totalHits; i++) 
						documentSet.add(reader.document(topDocs.scoreDocs[i].doc));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		// ya fuera del bucle, si tengo que crear el indice, hago lo propio
	    IndexWriter writer = null;
		if (createIndex){

			Directory directory = null;
	         try {
	        	 directory = FSDirectory.open(new File(createIndexPath));
			} catch (IOException e1) {
				System.out.println("Exception " + e1);
				e1.printStackTrace();
			}
			 Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
		     IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_40, analyzer);
	         iwc.setOpenMode(OpenMode.CREATE);
			try {
				writer = new IndexWriter(directory, iwc);
			} catch (IOException e) {
				 System.out.println("Exception " + e);
				 e.printStackTrace();
			}
			
			//para cada documento del set, lo meto en el indice
			for (Document document : documentSet) {
				Document doc = new Document();
				List<IndexableField> fields = document.getFields();
					
				//para cada campo del documento lo meto de una forma u otra
				for (IndexableField field : fields) {
					if (field.name().equals("title") || field.name().equals("body") ||field.name().equals("topics"))
						doc.add(new TextField(field.name(), document.get(field.name()), Field.Store.YES));
					else
						doc.add(new StringField(field.name(), document.get(field.name()), Field.Store.YES));
				}
				
		        try {
					writer.addDocument(doc);
		        } catch (CorruptIndexException e) {
					System.out.println("Exception " + e);
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println("Exception " + e);
					e.printStackTrace();
				}
			}
	    }
			
		// finalmente cierro el reader y si es necesario el writer
		try {
			reader.close();
			if (createIndex){
				writer.commit();
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
		System.out.println("Ended with no errors");

	}
}
