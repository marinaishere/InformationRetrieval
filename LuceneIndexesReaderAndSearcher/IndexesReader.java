import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CompositeReader;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.SlowCompositeReaderWrapper;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class IndexesReader {

	public static void main(String[] args) {

		/*
		 DEFENSA:
		 
	 	-doc 10
	 	-docs 10 12
		-write C:\Users\Acer\workspace\index\file.txt
		-write C:\Users\Acer\workspace\index\file.txt -docs 10 12	
		-termsdfmorethan 10000 body
		-termsdflessthan 50 body
		-termsdfrango 50 100 body
		-indexdocstermsdfmorethan 10000 body C:/Users/Acer/workspace/index/more
		-indexdocstermsdflessthan 5 body C:/Users/Acer/workspace/index/less
		-indexdocstermsdfrango 0 5 body C:/Users/Acer/workspace/index/rango
		-indexdocsij 10 12 C:/Users/Acer/workspace/index/indexij
		-mergeindexes C:\Users\Acer\workspace\index\less C:\Users\Acer\workspace\index\more		

		 */
		

		String usage = "[-index INDEX_PATH] [-doc i] [-docs i j] [-write filename] [-termsdfmorethan n fld] "
				+ "[-termsdflessthan n fld] [-termsdfrango n1 n2 fld] [-indexdocstermsdfmorethan n fld indexfile]"
				+ " [-indexdocstermsdflessthan n fld indexfile] [-indexdocstermsdfrango n1 n2 fld indexfile]"
				+ "[-indexdocsij i j indexfile] [-mergeindexes index1 index2]\n\n";
			
		
		String indexPath = null; //Ruta donde están los índices a leer
		int first = -1; //primer documento a mostrar
		int last = -1; //último documento a mostrar, inicializados a -1 pero si uno toma un valor, el otro tambien
		String fileToWriteS = null; //ruta del fichero en el que escribir los resultados
		int docFrecMoreThan = Integer.MIN_VALUE; // toma otro valor si queremos los articulos con frecuencia mayor de ese valor
		int docFrecLessThan = Integer.MAX_VALUE;// toma otro valor si queremos los articulos con frecuencia menor de ese valor
		boolean showFrecsTerms = false; // true si se activaron las opciones termsdflessthan termsdfmorethan o termsdfrango
		String fieldInput = null; // el campo del que queremos mirar la frecuencia
		boolean buildIndexFrec = false; // true si tenemos que crear un índice con los campos de una frecuencia determinada
		String buildIndexFile = null; //ruta donde tenemos que crear el índice
		boolean createIndexDocij = false; //true si se activo la opcin indexdocsij
		boolean mergeIndex = false; //a true si la opcion mergeindex esta activada
		String mergeIndexPath1 = null; //ruta del primer indice a fusionar
		String mergeIndexPath2 = null;//ruta del segundo indice a fusionar
		IndexWriter writer = null; //index writer para crear indices

		
		for(int i=0;i<args.length;i++)
		     if ("-index".equals(args[i])) {
		       indexPath = args[i+1];
		       i++;
		     } else if ("-doc".equals(args[i])) {    
			       try{
			    	   first = Integer.parseInt(args[i+1]);
				       last = first;
				       System.out.println("Mostrando documento numero " + first + "...");
					}
					catch (NumberFormatException e){
						System.err.println("Usage: " + usage);
						e.printStackTrace();		
						System.exit(1);
					}		
			       i++;	 			       
		     } else if ("-docs".equals(args[i]) || "-indexdocsij".equals(args[i]) ) {
		    	 try{
			    	   first = Integer.parseInt(args[i+1]);
			    	   last = Integer.parseInt(args[i+2]);
					}
					catch (NumberFormatException e){
						System.err.println("Usage: " + usage);
						e.printStackTrace();
						System.exit(1);
					}
		    	 if ("-indexdocsij".equals((args[i]))){
		    		 buildIndexFile = args[i+3];
		    		 System.out.println(buildIndexFile);
	    			 createIndexDocij = true;
		    		 i++;
				     System.out.println("Indexando documentos del " + first +  " al " + last + " en la carpeta " + buildIndexFile + "...");

		    	 }
		    	 else 
				      System.out.println("Mostrando documentos del " + first +  " al " + last + "...");

			     i+=2;	
			 } else if ("-write".equals(args[i])) {
				 fileToWriteS = args[i+1];
				 i++;
			     System.out.println("Escribiendo los documentos al fichero " + fileToWriteS + "...");

			 } else if ("-termsdfmorethan".equals(args[i]) || "-indexdocstermsdfmorethan".equals(args[i])) {
				 try{
					 docFrecMoreThan = Integer.parseInt(args[i+1]);
					}
					catch (NumberFormatException e){
						System.err.println("Usage: " + usage);
						e.printStackTrace();
						System.exit(1);
					}			
				 fieldInput = args[i+2];
				 if ("-indexdocstermsdfmorethan".equals(args[i])){
					 buildIndexFrec = true;
					 buildIndexFile = args[i+3];
					 i++;
				      System.out.println("Creando indice con los documentos con una frecuencia mayor de " + docFrecMoreThan + " en el campo " + fieldInput + "...");
				 }
				 else{
					 showFrecsTerms = true;
				      System.out.println("Mostrando los campos con una frecuencia mayor de " + docFrecMoreThan + " en el campo " + fieldInput + "...");
				 }
				 i+=2;
			 } else if ("-termsdflessthan".equals(args[i]) || "-indexdocstermsdflessthan".equals(args[i])) {
				 try{
					 docFrecLessThan = Integer.parseInt(args[i+1]);
					}
					catch (NumberFormatException e){
						   System.err.println("Usage: " + usage);
						   e.printStackTrace();
						   System.exit(1);
					}				 
				 fieldInput = args[i+2];
				 if ("-indexdocstermsdflessthan".equals(args[i])){
					 buildIndexFrec = true;
					 buildIndexFile = args[i+3];
					 i++;
				      System.out.println("Creando indice con los documentos con una frecuencia menor de " + docFrecLessThan + " en el campo " + fieldInput + "...");

				 }		
				 else{
					 showFrecsTerms = true;
					 System.out.println("Mostrando los campos con una frecuencia menor de " + docFrecLessThan + " en el campo " + fieldInput + "...");
				 }
				 i+=2;
			 }else if ("-termsdfrango".equals(args[i]) || "-indexdocstermsdfrango".equals(args[i])) {
				 try{
					 docFrecMoreThan = Integer.parseInt(args[i+1]);
					 docFrecLessThan = Integer.parseInt(args[i+2]);
					}
					catch (NumberFormatException e){
						   System.err.println("Usage: " + usage);
						   e.printStackTrace();
						   System.exit(1);
					}		
				 fieldInput = args[i+3];
				 if ("-indexdocstermsdfrango".equals(args[i])){
					 buildIndexFrec = true;
					 buildIndexFile = args[i+4];
					 i++;
				     System.out.println("Creando indice con los documentos con una frecuencia menor de " + docFrecLessThan + " y mayor de " + docFrecMoreThan +  " en el campo " + fieldInput + "...");
				 }
				 else {
					 showFrecsTerms = true;
				     System.out.println("Mostrando los campos con una frecuencia menor de " + docFrecLessThan + " y mayor de " + docFrecMoreThan +  " en el campo " + fieldInput + "...");
				 }
				 i+=3;
			 }else if ("-mergeindexes".equals(args[i])){
				 mergeIndex = true;
				 mergeIndexPath1 = args[i+1];
				 mergeIndexPath2 = args[i+2];
				 i+=2;
				 System.out.println("fusionando el indice de la carpeta " +  mergeIndexPath1 +  " en la carpeta " + mergeIndexPath2 + "...");
			 }
		     		     
	//**************PARA ESCRIBIR EN EL FICHERO *****************//     
		FileWriter fw = null;
		BufferedWriter bw = null;	
		if (fileToWriteS != null){
		    File fileToWrite = new File(fileToWriteS);    
			if (!fileToWrite.exists())
				try {
					fileToWrite.createNewFile();
				} catch (IOException e) {
					System.out.println("Exception " + e);
					e.printStackTrace();		
					System.exit(1);				
				}
			try {
				fw = new FileWriter(fileToWrite.getAbsoluteFile());
				bw = new BufferedWriter(fw);
			} catch (IOException e2) {
				System.out.println("Exception " + e2);
				e2.printStackTrace();		
				System.exit(1);
			} 
		}
	//***********************************************************//
	     
		     
	
		
	//****************SI TENEMOS QUE CREAR UN NUEVO ÍNDICE********************//		
		if (buildIndexFrec || createIndexDocij){
			Directory dirBI = null;
	         try {
				dirBI = FSDirectory.open(new File(buildIndexFile));
			} catch (IOException e1) {
				System.out.println("Exception " + e1);
				e1.printStackTrace();
			}
			 Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
		     IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_40, analyzer);
	        iwc.setOpenMode(OpenMode.CREATE);

		     try {
				writer = new IndexWriter(dirBI, iwc);
			} catch (IOException e) {
				 System.out.println("Exception " + e);
				 e.printStackTrace();
			}
		}
	//************************************************************************//
		
	//************************* FUSIONAR INDICES *****************************//		

		if (mergeIndex){
			//creamos indezreader para el primer path
			File file1 = new File(mergeIndexPath1);
			Directory dir1 = null;
			DirectoryReader indexReader1 = null;
			try {
				dir1 = FSDirectory.open(file1);
				indexReader1 = DirectoryReader.open(dir1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			File file2 = new File(mergeIndexPath2);
			Directory dir2 = null;
			DirectoryReader indexReader2 = null;
			try {
				dir2 = FSDirectory.open(file2);
				indexReader2 = DirectoryReader.open(dir2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Directory dirBI1 = null;
	         try {
				dirBI1 = FSDirectory.open(new File(mergeIndexPath2));
			} catch (IOException e1) {
				System.out.println("Exception " + e1);
				e1.printStackTrace();
			}
			 Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
		     IndexWriterConfig iwc1 = new IndexWriterConfig(Version.LUCENE_40, analyzer);
		     iwc1.setOpenMode(OpenMode.CREATE);
		     try {
				writer = new IndexWriter(dirBI1, iwc1);
			} catch (IOException e) {
				 System.out.println("Exception " + e);
				 e.printStackTrace();
			}
		
			try {
				writer.addIndexes(indexReader1);
				writer.addIndexes(indexReader2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	//************************************************************************//

	
	//********** CUANDO HAY FRECUENCIA DE APARICION DE TERMINOS EN DOCUMENTOS ************//
		File file = new File(indexPath);
		Directory dir = null;
		DirectoryReader indexReader = null;
		Document document = null;
		List<IndexableField> fields = null;
		try {
			dir = FSDirectory.open(file);
			indexReader = DirectoryReader.open(dir);
		} catch (CorruptIndexException e1) {
			System.out.println("Exception " + e1);
			e1.printStackTrace();
		} catch (IOException e1) {
			System.out.println("Exception " + e1);
			e1.printStackTrace();
		}
		
		SlowCompositeReaderWrapper atomicReader = null;
		try{
			atomicReader = new SlowCompositeReaderWrapper((CompositeReader) indexReader);
		} catch (IOException e1) {
			System.out.println("Exception " + e1);
			e1.printStackTrace();
		}
		
		//para evitar mostrar repetidos lo guardo en un set
		HashSet<String> vistos = new HashSet<String>();
		
		if (showFrecsTerms || buildIndexFrec){
			//solo entro si tengo que mostrar articulos con una frecuencia o crear un indice con la misma
			Terms terms = null;
			TermsEnum termsEnum = null;
			Fields fieldss = atomicReader.fields();
			for (String field : fieldss) {
				if ((field.equals(fieldInput))){
					try {
						terms = fieldss.terms(field.toString());
						termsEnum = terms.iterator(null);
						while (termsEnum.next() != null) {
							String tt = termsEnum.term().utf8ToString();

							if (termsEnum.docFreq() >= docFrecMoreThan && termsEnum.docFreq() <= docFrecLessThan){	
								
								/*
								 								
								System.out.println(tt + "\n\n\n\n\n\\n\n\n\n\n\\n\n\n\n\n\\n\n\n\n\n\\n\n\n\n\n\\n\n\n\n\n\\n\n\n\n\n\\n\n\n\n\n\n");
								DocsEnum docsEnum = null;
								DocsEnum docsen = termsEnum.docs(null, docsEnum);
								int doc;
								while ((doc = docsen.nextDoc()) != docsen.NO_MORE_DOCS) {
									System.out.println(doc);
								}
									 
								 
								 */
								if (!vistos.contains(tt)){
									vistos.add(tt);
									if (showFrecsTerms)
										System.out.println(fieldInput.toUpperCase() + ": \n " 
												+ tt + " totalFreq()="+ termsEnum.totalTermFreq() + " docFreq=" + termsEnum.docFreq());								
								}	
							}
						}
					} catch (IOException e1) {
						System.out.println("Exception " + e1);
						e1.printStackTrace();
					}
				}
			}	
		}
	//***************************************************************//

		
	//******PARA CREAR EL INDICE CON LOS DOCUMENTOS CON FRECUENCIA TAL QUE *******//
		if (buildIndexFrec){		
			HashSet<Integer> docsMatchFrec = new HashSet<Integer>();
			int doc;
			for (String text : vistos) {	
				Term t = new Term(fieldInput,text);
				DocsEnum docsEnum = null;
				try {
					docsEnum = atomicReader.termDocsEnum(t);
					while ((doc = docsEnum.nextDoc()) != DocsEnum.NO_MORE_DOCS) {
						if (!docsMatchFrec.contains(doc)){
							docsMatchFrec.add(doc);
							Document d = atomicReader.document(doc);
							createIndex(writer,d);
						}
					}	
				} catch (IOException e) {
					System.out.println("Exception " + e);
					e.printStackTrace();
				}	
				
			}
		}
	//***************************************************************//

		
	//*************BUCLE PARA CADA ARTÍCULO DEL INDICE LEÍDO*************//

		if (createIndexDocij || fileToWriteS != null ||first != -1 || last != -1)
		for (int j = 0; j < indexReader.maxDoc(); j++) {
			if (first == -1 || (j >= first && j <= last)){ //si first y last no estan definidos, el primer caso es cierto
				try {
					document = indexReader.document(j);
				} catch (CorruptIndexException e1) {
					System.out.println("Exception " + e1);
					e1.printStackTrace();
				} catch (IOException e1) {
					System.out.println("Exception " + e1);
					e1.printStackTrace();
				}
	 			
				//guardo toda la información del articulo en la variable article que es un string, 
				// y lo escribo por la salida estandar o fichero
				String article = "\n\n ************* DOCUMENTO " + j + " ************* \n";
				fields = document.getFields();
				for (IndexableField field : fields) 
					article = article + field.name().toUpperCase() + ": " + document.get(field.name());					

				if (first != -1 || last != -1)
					System.out.println(article);

				if (fileToWriteS != null){
					try {
						bw.write(article);
					} catch (IOException e1) {
						System.out.println("Exception " + e1);
						e1.printStackTrace();
					} 
				}
				
				if (createIndexDocij){
					createIndex(writer,document);
				}
				
			}	
		}
	//**************************************************************************************//

		try {
			if (fileToWriteS != null)
				bw.close();
			indexReader.close();
			if (buildIndexFrec || createIndexDocij || mergeIndex){
				writer.commit();
				writer.close();
			}
		} catch (IOException e) {
			System.out.println("Exception " + e);
			e.printStackTrace();
		}
	
	System.out.println("Ended with no errors");
	
	}
	

	static void createIndex(IndexWriter writer, Document document){
		Document doc = new Document();
		List<IndexableField> fields = document.getFields();
			
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
	
