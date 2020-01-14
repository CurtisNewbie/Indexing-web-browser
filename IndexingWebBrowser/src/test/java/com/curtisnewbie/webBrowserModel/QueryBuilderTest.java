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
	}

	@Test
	public void parsedPrefixQueryTypeShouldBeCorrect() {
		// test whether it will get confused by simple word that contains the operators.
		Assert.assertTrue("\"orange\" should be an AtomicQuery Object",
				QueryBuilder.parse("orange") instanceof AtomicQuery);
		Assert.assertTrue("\"andcccc\" should be an AtomicQuery Object",
				QueryBuilder.parse("andcccc") instanceof AtomicQuery);
		Assert.assertTrue("\"notme\" should be an AtomicQuery Object",
				QueryBuilder.parse("notme") instanceof AtomicQuery);

		// test prefix query for types: AndQuery, OrQuery, NotQuery
		Assert.assertTrue("\"and(apple,orange)\" should be an AndQuery",
				QueryBuilder.parse("and(apple,orange)") instanceof AndQuery);
		Assert.assertTrue("\"or(apple,orange)\" should be an OrQuery",
				QueryBuilder.parse("or(apple,orange)") instanceof OrQuery);
		Assert.assertTrue("\"not(apple)\" should be a NotQuery", QueryBuilder.parse("not(apple)") instanceof NotQuery);
	}

	@Test
	public void parsedInfixQueryTypeShouldBeCorrect() {
		// test whether it will get confused by simple word that contains the operators.
		Assert.assertTrue("\"orange\" should be an AtomicQuery Object",
				QueryBuilder.parseInfixForm("orange") instanceof AtomicQuery);
		Assert.assertTrue("\"andcccc\" should be an AtomicQuery Object",
				QueryBuilder.parseInfixForm("andcccc") instanceof AtomicQuery);
		Assert.assertTrue("\"notme\" should be an AtomicQuery Object",
				QueryBuilder.parseInfixForm("notme") instanceof AtomicQuery);

		// test prefix query for types: AndQuery, OrQuery, NotQuery
		Assert.assertTrue("\"apple and orange\" should be an AndQuery",
				QueryBuilder.parseInfixForm("apple and orange") instanceof AndQuery);
		Assert.assertTrue("\"apple or orange\" should be an OrQuery",
				QueryBuilder.parseInfixForm("apple or orange") instanceof OrQuery);
		Assert.assertTrue("\"not apple\" should be a NotQuery",
				QueryBuilder.parseInfixForm("not apple") instanceof NotQuery);

	}

	@Test
	public void parsedInfixQueryShouldBeCorrect() {
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