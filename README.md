Information Retrieval
============

This was a project for the Information Retrieval course in my junior year of my BS in computer science at UDC (Spain) and is implemented in Java using Lucene. 
It is composed of an index searcher and an index reader that both take as input an index file.
 VERSATILE LUCENE INDEX SEARCHER
VERSATILE LUCENE INDEX READER



We implemented our own Python Facebook crawler that extracts public information from users. To access the data we created fake facebook user and extracted the cookies sent when the user logs in with Firefox. We used the command wget with the cookies saved and Firefox as UserAgent, which simulates a real browser connection by the user. This allows us to access the public data from any facebook user programatically.
The crawler works by starting with a seed user, parsing with regular expressions the html and storing the relevant information and the profile to a list of already crawler profiles. Then we parse the contact information from all his friends, store it in a queue and start again. The seed user, number of concurrent petitions, delay (for politeness) and other parameters can be configured. Even though no crawl-delay was specified in the robots.txt file, we used 10s bewteen petitions to be nice.

<p align="center">
<img src="https://github.com/DaniRuizPerez/InformationRetrieval/blob/master/example.png" width="500">
</p>




## Contact

Contact [Daniel Ruiz Perez](mailto:druiz072@fiu.edu) for requests, bug reports and good jokes.


## License

The software in this repository is available under the GNU General Public License, version 3. See the [LICENSE](https://github.com/DaniRuizPerez/InformationRetrieval/blob/master/LICENSE) file for more information.
