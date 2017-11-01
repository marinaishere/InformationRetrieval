Information Retrieval
============

Q-learning is a model-free reinforcement learning technique, which is used to make a two-wheeled simulated robot learn how to reach the other side of twisted hallways. This was a project for the robotics in my senior year of my undergrad in computer science at UDC (Spain) and is implemented in C.


## VERSATILE LUCENE INDEX SEARCHER
In the results of the execution will always appear the document ID and the score of the satisfying documents. In addition, it can be ran with the following parameters:

```-index indexfile``` (indexfile is the folder where the index is located)
```-showfield fld``` (show the content of the fld field for the satisfying documents)
```-out indexfile```
```-query fld1 "query"``` (show results of the query "query", qhich is any query accepted by the parser ran over the field fld1)
```-multiquery fld1 "query" fld2 "query2"...``` (same as above with more than one field)  
```-progquery fld1 -and term1 term2--- -or termi termj... -not termp termq...``` (show the results for the programatically built query ran over the field fld1)






## Contact

Contact [Daniel Ruiz Perez](mailto:druiz072@fiu.edu) for requests, bug reports and good jokes.


## License

The software in this repository is available under the GNU General Public License, version 3. See the [LICENSE](https://github.com/DaniRuizPerez/PyGame/blob/master/LICENSE) file for more information.
