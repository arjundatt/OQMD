<h1>One query multiple databases - An interface to mutually access Hadoop/Oracle databases</h1>
@author: <b>Arnab Saha</b>
@author: <b>Arjun Sharma</b>
@author: <b>Nishtha Garg</b>

<h2>Abstract</h2>
In today’s era of digitalism, where a number of technologies to maintain and analyze the huge amounts of data have evolved, querying and analyzing data from multiple data repositories with different data mechanics is challenging; esp. when one is RDBM system(like Oracle) and the other is NoSQL system(like HBase). For instance, if a company (which uses HBase) expands its product into a new location, by acquiring a similar product by another company (which uses Oracle) then processing the entire data using a single interface is an intricate task.</br>
In this project, we intend to solve the aforementioned problem in two steps. As the databases pertain to the same product, they have similar information but not necessarily similar structure. So we intend to provide an automated mapping technique for all the similar attributes in both datasets using domain mapping. Secondly we aim to create a common “SQL-like” API to access data from both datasets simultaneously. The ultimate goal is to provide a cost effective alternative to the approach of migrating the entire dataset from one technology to another.
