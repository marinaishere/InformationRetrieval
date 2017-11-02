Information Retrieval
============

This was a project for the Information Retrieval course in my junior year of my undergrad in computer science at UDC (Spain) and is implemented in Java using Lucene. 
It is composed of an index searcher and an index reader that both take as input an index file.


## VERSATILE LUCENE INDEX SEARCHER
In the results of the execution will always appear the document ID and the score of the satisfying documents. The code can be viewed in the IndexesSearcher.java file. In addition, it can be ran with the following parameters:

```-index indexfile``` (indexfile is the folder where the index is located)

```-showfield fld``` (show the content of the fld field for the satisfying documents)

```-out indexfile```

```-query fld1 "query"``` (show results of the query "query", qhich is any query accepted by the parser ran over the field fld1)

```-multiquery fld1 "query" fld2 "query2"...``` (same as above with more than one field)  

```-progquery fld1 -and term1 term2--- -or termi termj... -not termp termq...``` (show the results for the programatically built query ran over the field fld1)


## VERSATILE LUCENE INDEX READER

Creates an index reader with several options. The code can be viewed in the IndexesReader.java file. The first one is mandatory and only one of the others is possible:

```-index indexfile``` (indexfile is the folder where the index is located)

```-doc i ``` (show the content of document i)

```-docs i j ``` (show the content of the documents from i to j)

```-write file ``` (store the content of the index into the file file)

```-termsdfmorethan n fld ``` (show the terms of the field fld with docFreq greater or iqual to n)

```-termsdflessthan n fld ``` (show the terms of the field fld with docFreq less or iqual to n)

```-termsdfrango n1 n2 fld ``` (show the terms of the field fld with docFreq greater or iqual to n1 and less or equal to n2)

```-indexdocstermsdfmorethan n fld indexfile ``` (builds and index in the folder indexfile with the documents where the terms of the field fld appear with docFreq greater or equal to n)

```-indexdocstermsdflessthan n fld indexfile ``` (builds and index in the folder indexfile with the documents where the terms of the field fld appear with docFreq less or equal to n)

```-indexdocstermsdfrango n1 n2 fld indexfile ``` (builds and index in the folder indexfile with the documents where the terms of the field fld appear with docFreq greater or equal to n1 and less or equal to n2)

```-indexdocsij i j indexfile ``` (builds and index in the folder indexfile with the documents of the range from i to j)

```-mergeindexes index1 index2 ``` (merges the index in the fodler index1 with the index in the folder index2, where it is going to be located)


## Contact

Contact [Daniel Ruiz Perez](mailto:druiz072@fiu.edu) for requests, bug reports and good jokes.


## License

The software in this repository is available under the GNU General Public License, version 3. See the [LICENSE](https://github.com/DaniRuizPerez/InformationRetrieval/blob/master/LICENSE) file for more information.
