# A-simple-indexing-web-browser

This is a very simple indexing web browser, it builds a searchable index of web page contents dynamically as the user browses. This program has two "views"/"windows", one is for browsing webpages, and another is for searching content words with queries. The users are able to browse online websites (by entering URL) or local html files (by providing path to these files). Once the webpages are visited, users can then search through these webpages using <b>infix or prefix queries</b>. There are two type of contents words that can be searched for: the uniques words within the <b>\<head>...\</head></b> tags and the unique words within the <b>\<body>...\</body></b> tags.

If you are only interested in what it does, you can have a look at the demos in Section 2. Using it as a simple web browser would only need to know the basic functionalities in **Section 2 (Demo)** and **Section 3 (loading web pages)**. Some basic configuration (e.g., default URL for new tab) is also explained is **Section 4 (Configuration with JSON)**. If you want to use the query functions to search through contents, you may need to have a look at **Section 5 (Query)**, it shouldn't be hard to understand, but I hope that I have explained it clearly. :D

#### Content:

1. Running Program With Maven
2. Demo of Functionalities
3. Browsing Online and local web pages
4. Custom Configuration
5. Query

This program is developed using:

<ul>
	<li>JavaSE</li>
	<li>JavaFX</li>
	<li>JSoup</li>
    <li>Jackson</li>
</ul>

---

<h2>1. Running This Program With Maven</h2>

External dependencies are resolved using Maven, which include JavaFx, JSoup and JUnit. To run this, you should have Maven installed. Navigate to where the pom.xmll file located, and execute the following command in your CLI:

    mvn compile javafx:run

If you have problems downloading the dependencies, you can execute the command below before the one above.

    mvn dependency:resolve

---

<h2>2. Demonstration of Functionalities With GIF</h2>

You can browse online websites as follows by entering the URL in the textfield as usual.

<img src="https://user-images.githubusercontent.com/45169791/71385799-7e984880-25e1-11ea-8c59-6de47e381488.gif" width=711 height=400 >

You can also browse local webpages as follows by entering the path to the file with a prefix of "file:".

<img src="https://user-images.githubusercontent.com/45169791/71385950-50ffcf00-25e2-11ea-81be-a4cb797d408d.gif" width=711 height=400 >

This simple program supports multiple tabs, you can create a new tab by pressing the "plus" symbol on the right.

<img src="https://user-images.githubusercontent.com/45169791/71385976-84daf480-25e2-11ea-99f9-0db260924f91.gif" width=711 height=400 >

Going backward and forward in history for each tab is also supported, you can do so by pressing the "<" or ">" buttons for the currently selected tab.

<img src="https://user-images.githubusercontent.com/45169791/71386022-cff50780-25e2-11ea-86f4-3dc3b5b1c00b.gif" width=711 height=400 >

As this program supports multiple views, one for browsing webpages, another for searching content with queries. You can switch between views through the menu.

<img src="https://user-images.githubusercontent.com/45169791/71386046-fadf5b80-25e2-11ea-9120-e97285de5af4.gif" width=711 height=400 >

In the "view" or "panel" for using query function, you can check the previous history and open a tab to display the one of the previously visited URL by clicking on it. This function also works for local webpage.

<img src="https://user-images.githubusercontent.com/45169791/71386748-bacea780-25e7-11ea-9a08-12527bf635e4.gif" width=711 height=400 >

Using Infix or Prefix queries allows you to search through the content of the previously visisted webpages.

<img src="https://user-images.githubusercontent.com/45169791/71386786-1731c700-25e8-11ea-9e35-2854c3b4cd2d.gif" width=711 height=400 >

By clicking on the one of the results of the query, the summary panel will display its unique content words.

<img src="https://user-images.githubusercontent.com/45169791/71386819-4a745600-25e8-11ea-85f3-0a2ccd2a4a98.gif" width=711 height=400 >

Using <b>"/all"</b> command can display all the webpages in the results panel, so that you can view the summary of each webpage that you have visisted without using query.

<img src="https://user-images.githubusercontent.com/45169791/71386854-96bf9600-25e8-11ea-90b3-e8b31654087a.gif" width=711 height=400 >

---

<h2>3. How to Browse Online Website and Local WebPage</h2>

You can either use it as a normal browser to visit online website by entering url, if no "http://" or "https://" protocols are given, the browser will automatically complete it for you.

If you want to load a local webpage (html file), then you need to use specific syntax. <b>A prefix of "file:" (case-insensitive)</b> tells the program that you are looking for a local file, then all you need to do is to append the absolute path to it as follows.

    Syntax:
    file:[path/to/file]

    E.g.,

    file:/home/yongjie/test.html

---

<h2>4. Custom Configuration</h2>

This program uses **Json** file for configuration. It now only supports configuring two properties. 1) **_"defaultUrl"_** - The default url (i.e., the one that is loaded when a new tab is created), this is usually the search engine. 2) **_"title"_** - The title of the application. The configuration file is located at **src/main/resources/config.json**. It looks like this:

    {
        "defaultUrl": "https://www.google.com",
        "title": "Hi Curtis"
    }

---

<h2>5. Explanation of Queries</h2>

<h3>Operators</h3>

To use the infix and prefix queries, it's essential to understand the operators available. In this program, there are three types of <b>operators</b>, they are:

    AND operator - the set intersection of the results
    OR operator - the set union of the results
    NOT operator - the set difference, or the results other than that for the word in this NOT query

<h3>Infix and Prefix Queries</h3>

<b>Infix Queries</b> refer to the queries wherein the operators are declared between normal words. <b>Prefix Queries</b> refer to those queries wherein the operators are declared before normal words, the parentheses and comma are needed. <b>They can be nested as long as the fundamental rules of syntax are met.</b>

<b>In the follwing examples, operators are in uppercase for demonstartion, they are case-insensitive in the program.</b>

They are as follows:

    Infix Query:
    	Syntax: [Subquery] operator [Subquery]
    	e.g., "apple AND orange"


    Prefix Query:
    	Syntax: operator ([Subquery],[Subquery],[Subquery]...)
    	"AND(apple, orange)"

So, for the example above, the two queries are looking for set intersection of results of "apple" and "orange", or i.e., which webpage has both "apple" and "orange".

<h3>Rules for Infix Query</h3>

This section describes the fundamental rules for Infix Query. <b>The fundamental rule is that there must be an operator between any two subqueries or words. Further, both infix query and prefix query can be nested, thus there can be subquries.</b> For example:

    Query:
    "(whale and fish) and not elephant"

    Here, "whale and fish", "not elephant" are treated as subqueries.
    Between these two subquery, there (must) be an operator between them. In this example, the operator is AND operator.

<b>Parentheses can be used to indicate a subquery, which makes sure this subquery is processed in one go (being treated as a single result) rather then being seperated. By default, the whole query is always processed from left to right, so ordering does matter.</b>

<b>Queries with AND operators and OR operators are very similar. There must be two words or subqueries between AND operators and OR operators</b>, for example:

    "apple AND pig"
    "apple OR pig"

    However, the following queries are incorrect:
    "AND pig"
    "pig OR"

For queries with NOT operator, it's different. <b>NOT operator only accepts one subquery or one word on its right-hand side, subqueries must be covered with parentheses, else it may lead to unexpected results</b>. For example:

    This is a correct query:
    "NOT apple"

    This one is also correct, but more attention is needed:
    "NOT apple and orange"

    The parser will treat it as below, because no parentheses are provided:
    "NOT (apple and orange)"

    If you want to make sure the "NOT apple" is processed first, you will need to add parentheses as follows:
    "(NOT apple) and orange"

More examples of infix query:

    "whale and (apple or pig or dog)"
    "not dog and not pig"

<h3>Rules for Prefix Query</h3>

Prefix queries have more restriction on the syntax, however, it may be easier to use to build a query.<b> For each query or subquery, an operator must be declared at the beginnin, and there must be parentheses covering the rest of the subquery or words.</b> For example,

    "AND(apple, organge, food, OR(cat, dog), NOT(AND(apple, orange)), no, lol)"

    There can be unlimited number of words/subqueries within the parentheses within this AND query.

    "OR(apple, organge, food, OR(cat, dog), NOT(AND(apple, orange)), no, lol)"

    There can be unlimited number of words/subqueries within the parentheses within this OR query.

Based on the structure or syntax of prefix query, this becomes much clearer on how the query will be processed. <b>Generally, the subquery in the deeper level will be processed first. To be clear, this is processed recursively, so the results of subqueries must be got before we start processing the outter layer.</b>

<b>For AND and OR operators, there can be unlimited number of words/subqueries within the parentheses, however, for NOT operator, there can only be one word or one nested subquery in it.</b> For example,

    These are correct queries:
    "NOT(apple)"
    "NOT(AND(apple, orange))"
    "NOT(AND(apple, OR(orange, banana)))"

    This is an incorrect query:
    "NOT(apple, orange)"
