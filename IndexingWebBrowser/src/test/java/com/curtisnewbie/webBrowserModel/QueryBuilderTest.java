package com.curtisnewbie.webBrowserModel;

import org.junit.Assert;
import org.junit.Test;

public class QueryBuilderTest {

    @Test
    public void parsedQueryShouldBeCorrect() {
        Assert.assertTrue("Query \"and(apple,orange)\" should be parsed to \"AND([apple],[orange])\"",
                QueryBuilder.parse("and(apple,orange)").toString().equals("AND([apple],[orange])"));
        Assert.assertTrue("Query \"or(apple,orange)\" should be parsed to \"OR([apple],[orange])\"",
                QueryBuilder.parse("or(apple,orange)").toString().equals("OR([apple],[orange])"));
        Assert.assertTrue("Query \"not(apple)\" should be parsed to \"NOT([apple])\"",
                QueryBuilder.parse("not(apple)").toString().equals("NOT([apple])"));
        Assert.assertTrue("\"apple\" should be parsed to be an AtomicQuery object",
                QueryBuilder.parse("apple") instanceof AtomicQuery);
    }
}