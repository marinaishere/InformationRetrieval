Information Retrieval
============

This Facebook crawler and offline profile searcher was a project for the Information Retrieval course in my MS in computer science at UDC (Spain). The deliverables of this project are a Python Facebook crawler, Java Lucene Facebook indexer and offline facebook profiles searcher and the [report](https://github.com/DaniRuizPerez/InformationRetrieval/blob/master/FacebookofflinesearcherReport.pdf)(Spanish). The code can be seen at the [FBCrawlerIndexerAndSearcher](https://github.com/DaniRuizPerez/InformationRetrieval/blob/master/FBCrawlerIndexerAndSearcher) folder.

Also in the folder [LuceneIndexesReaderAndSearcher](https://github.com/DaniRuizPerez/InformationRetrieval/blob/master/LuceneIndexesReaderAndSearcher) are self made implementations of a versatile lucene index reader and query searcher from the homonymous course of my BS.


## Crawler implementation

We implemented our own Python Facebook crawler that extracts public information from users. To access the data we created fake facebook user and extracted the cookies sent when the user logs in with Firefox. We used the command wget with the cookies saved and Firefox as UserAgent, which simulates a real browser connection by the user. This allows us to access the public data from any facebook user programatically.
The crawler works by starting with a seed user, parsing with regular expressions the html and storing the relevant information and the profile to a list of already crawler profiles. Then we parse the contact information from all his friends, store it in a queue and start again. The seed user, number of concurrent petitions, delay (for politeness) and other parameters can be configured. Even though no crawl-delay was specified in the robots.txt file, we used 10s bewteen petitions to be nice.

<p align="center">
<img src="https://github.com/DaniRuizPerez/InformationRetrieval/blob/master/Images/example.png" width="600">
</p>


## Data Indexing

All fields are of type "Stored". WIth the Class DataExtractor we obtain the fields from the .txt files and creates an internal representation with the objects UserInfo. Once we have a list of this objects, we create a Lucene StandarAnalyzer and create the index and the writer. This index can be updated using the UI.

## Data querying

The querying process is totally configurable from the UI, using a DirectoryReader and an IndexSearcher. We can create the following types of queries:

- BooleanQuery that if no results are found, retries with a FuzzyQuery
- MultiFieldQuery over specified fields in the configuration.
- PrefixQuery for autocompletion spinner

## Execution

Execute the jar with
```java -jar fb-offline-searcher.jar ```

Between many things, you can configure the fields to perform the search over

<p align="center">
<img src="https://github.com/DaniRuizPerez/InformationRetrieval/blob/master/Images/config.png" width="400">
</p>

An example otput for a simple query can be seen here
<p align="center">
<img src="https://github.com/DaniRuizPerez/InformationRetrieval/blob/master/Images/output.png" width="500">
</p>

And the results geolocated in the map
<p align="center">
<img src="https://github.com/DaniRuizPerez/InformationRetrieval/blob/master/Images/map.png" width="500">
</p>



## Tools:
- Java
- Lucene
- Pythong
- Unfolding Maps
- Luke
- geocoder



user data in InformationRetrieval\FBCrawlerIndexerAndSearcher\RIWSLuceneFb\FbUserInfoApp\userdata





Unfolding Maps

## Contact

Contact [Daniel Ruiz Perez](mailto:druiz072@fiu.edu) for requests, bug reports and good jokes.


## License

The software in this repository is available under the GNU General Public License, version 3. See the [LICENSE](https://github.com/DaniRuizPerez/InformationRetrieval/blob/master/LICENSE) file for more information.
