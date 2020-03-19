# A-simple-indexing-web-browser

This is a very simple indexing web browser, it builds a searchable index of web page contents dynamically as the user browses. This program has two "views"/"windows", one is for browsing webpages, and another is for searching content words with queries. 

The users are able to browse online websites (by entering URL) or local html files (by providing path to these files). Once the webpages are visited, users can then search through these webpages using <b>infix or prefix queries</b>. There are two type of contents words that can be searched for: the uniques words within the <b>\<head>...\</head></b> tags and the unique words within the <b>\<body>...\</body></b> tags. 

This program is developed using:

<ul>
	<li>JavaSE</li>
	<li>JavaFX</li>
	<li>JSoup</li>
    <li>Jackson</li>
</ul>

<h2>How to Run This Program</h2>

A bundled version is available in RELEASE, to run it, simply execute the command below:

    java -jar IndexingWebBrowser-1.0-SNAPSHOT.jar

External dependencies are resolved using Maven, which include JavaFx, JSoup and JUnit. To run this, you should have Maven installed. Navigate to where the pom.xmll file located, and execute the following command in your CLI:

    mvn compile javafx:run

If you have problems downloading the dependencies, you can execute the command below before the one above.

    mvn dependency:resolve

To build the bundled version, execute the following command:

    mvn clean package

<h2>How to Browse Online Website and Local WebPage</h2>

You can either use it as a normal browser to visit online website by entering url, if no "http://" or "https://" protocols are given, the browser will automatically complete it for you.

If you want to load a local webpage (html file), then you need to use specific syntax. <b>A prefix of "file:" (case-insensitive)</b> tells the program that you are looking for a local file, then all you need to do is to append the absolute path to it as follows.

    Syntax:
    file:[path/to/file]

    E.g.,

    file:/home/yongjie/test.html

<h2>Custom Configuration</h2>

This program uses **Json** file for configuration. It now only supports configuring two properties. 1) **_"defaultUrl"_** - The default url (i.e., the one that is loaded when a new tab is created), this is usually the search engine. 2) **_"title"_** - The title of the application. The configuration file is located at **src/main/resources/config.json**. It looks like this:

    {
        "defaultUrl": "https://www.google.com",
        "title": "Hi Curtis"
    }

<h2>Demo</h2>

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


