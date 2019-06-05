# A-simple-indexing-web-browser
This is a very simple indexing web browser. It is different from the normal browsers in that it builds a searchable index of web page contents dynamically as the user browses. The users can then search through pages that they have visited and have the list of results displayed as a set of clickable links using the prefix query and infix query.

[Note: The code are well documented using Java doc, however, the Java doc html files are not generated. You may want to generate the Java doc if necessary.]

// Searching Functionality:
For searching the content word and key word in the web pages, three types of quries can be used:
1) AndQuery, 2) OrQuery, and 3) NoQuery. The explanation of them is provided below. Each query will return a set of the URLs (results).

1) AndQuery: is the set intersection of the results, for the infix query, it can only consist of two components (e.g., queryA and queryB). For the prefix query, it can consist of more than two components, e.g., and(queryA, queryB, queryC). 

2) OrQuery: is the set union of the results. For the infix query, it can only consist of two components (e.g., queryA or queryB). For the prefix query, it can consist of more than two components, e.g., or(queryA, queryB, queryC).

3) NotQuery: is the set difference between the set of all documents (that the users browsed) and the result of the component in the NotQuery. NotQuery can always has one component, e.g., not(query) / not query

Example of Infix query: 
"(whale and fish) and not elephant"

Example of Prefix query:
"and(and(whale,fish),not(elephant))"

More Explanation of Functionalities please go to the Wiki page.

