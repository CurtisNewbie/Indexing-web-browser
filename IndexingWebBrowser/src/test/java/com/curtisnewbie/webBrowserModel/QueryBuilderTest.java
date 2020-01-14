package com.curtisnewbie.webBrowserModel;

import org.junit.Assert;
import org.junit.Test;

public class QueryBuilderTest {

	@Test
	public void parsedPrefixQueryShouldBeCorrect() {
		Assert.assertTrue("Query \"and(apple,orange)\" should be parsed to \"AND([apple],[orange])\"",
				QueryBuilder.parse("and(apple,orange)").toString().equals("AND([apple],[orange])"));
		Assert.assertTrue("Query \"or(apple,orange)\" should be parsed to \"OR([apple],[orange])\"",
				QueryBuilder.parse("or(apple,orange)").toString().equals("OR([apple],[orange])"));
		Assert.assertTrue("Query \"not(apple)\" should be parsed to \"NOT([apple])\"",
				QueryBuilder.parse("not(apple)").toString().equals("NOT([apple])"));
		Assert.assertTrue("\"apple\" should be parsed to be an AtomicQuery object",
				QueryBuilder.parse("apple") instanceof AtomicQuery);
	}

	@Test
	public void parsedInfixQueryShouldBeCorrect() {
		Assert.assertTrue("\"apple\" should be parsed to be an AtomicQuery object",
				QueryBuilder.parseInfixForm("apple") instanceof AtomicQuery);
		Assert.assertTrue("Query \"apple and orange\" should be parsed to \"AND([apple],[orange])\"",
				QueryBuilder.parseInfixForm("apple and orange").toString().equals("AND([apple],[orange])"));
		Assert.assertTrue("Query \"apple or orange\" should be parsed to \"OR([apple],[orange])\"",
				QueryBuilder.parseInfixForm("apple or orange").toString().equals("OR([apple],[orange])"));
		Assert.assertTrue("Query \"not(apple)\" should be parsed to \"NOT([apple])\"",
				QueryBuilder.parseInfixForm("not(apple)").toString().equals("NOT([apple])"));
		Assert.assertTrue("Query \"not apple \" should be parsed to \"NOT([apple])\"",
				QueryBuilder.parseInfixForm("not apple").toString().equals("NOT([apple])"));
	}
}