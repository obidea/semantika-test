package com.obidea.semantika.queryanswer;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;

import com.obidea.semantika.expression.base.IQueryExt;
import com.obidea.semantika.queryanswer.parser.SparqlParser;
import com.obidea.semantika.queryanswer.parser.SparqlParserException;
import com.obidea.semantika.queryanswer.parser.UnsupportedSparqlExpressionException;
import com.obidea.semantika.util.LogUtils;
import com.obidea.semantika.util.PrinterUtils;

public class SparqlParserTest
{
   private static final Logger LOG = LogUtils.createLogger("semantika.test"); //$NON-NLS-1$

   private static SparqlParser mParser = new SparqlParser();

   @Rule
   public ExpectedException mExpectedException = ExpectedException.none();

   @Test
   public void testSimpleQuery() throws SparqlParserException
   {
      final String sparql = 
            "PREFIX foaf:   <http://xmlns.com/foaf/0.1/>\n" +
            "SELECT ?name ?mbox\n" +
            "WHERE\n" +
            "{ ?x foaf:name ?name .\n" +
            "  ?x foaf:mbox ?mbox . }";
      IQueryExt result = parseQuery(sparql);
      printSummary("Simple query", sparql, result);
   }

   @Test
   public void testDistinctQuery() throws SparqlParserException
   {
      final String sparql = 
            "PREFIX foaf:   <http://xmlns.com/foaf/0.1/>\n" +
            "SELECT DISTINCT ?name ?mbox\n" +
            "WHERE\n" +
            "{ ?x foaf:name ?name .\n" +
            "  ?x foaf:mbox ?mbox . }";
      IQueryExt result = parseQuery(sparql);
      printSummary("Distinct query", sparql, result);
   }

   @Test
   public void testFilterQuery() throws SparqlParserException
   {
      final String sparql =
            "PREFIX  dc:  <http://purl.org/dc/elements/1.1/>\n" +
            "PREFIX  ns:  <http://example.org/ns#>\n" +
            "SELECT  ?title ?price\n" +
            "WHERE\n" +
            "{ ?x ns:price ?price .\n" +
            "  FILTER (?price < 30.5)\n" +
            "  ?x dc:title ?title . }";
      IQueryExt result = parseQuery(sparql);
      printSummary("Filter query", sparql, result);
   }

   @Test
   public void testFilterLangQuery() throws SparqlParserException
   {
      final String sparql =
            "PREFIX  dc:  <http://purl.org/dc/elements/1.1/>\n" +
            "PREFIX  ns:  <http://example.org/ns#>\n" +
            "SELECT  ?title ?price\n" +
            "WHERE\n" +
            "{ ?x ns:price ?price .\n" +
            "  FILTER ( LANG(?title) = 'en')\n" +
            "  ?x dc:title ?title . }";
      IQueryExt result = parseQuery(sparql);
      printSummary("Filter query", sparql, result);
   }

   @Test
   public void testFilterBoundQuery() throws SparqlParserException
   {
      final String sparql =
            "PREFIX  dc:  <http://purl.org/dc/elements/1.1/>\n" +
            "PREFIX  ns:  <http://example.org/ns#>\n" +
            "SELECT  ?title ?price\n" +
            "WHERE\n" +
            "{ ?x dc:title ?title .\n" +
            "  OPTIONAL { ?x ns:price ?price }\n" +
            "  FILTER ( !BOUND(?price) ) }";
      IQueryExt result = parseQuery(sparql);
      printSummary("Filter query", sparql, result);
   }

   @Test
   public void testFilterAndQuery() throws SparqlParserException
   {
      final String sparql =
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
            "SELECT ?name1 ?name2\n" +
            "WHERE\n" +
            "{ ?x foaf:name  ?name1 ;\n" +
            "  foaf:mbox  ?mbox1 .\n" +
            "  ?y foaf:name  ?name2 ;\n" +
            "  foaf:mbox  ?mbox2 .\n" +
            "  FILTER (?mbox1 = ?mbox2 && ?name1 != ?name2) }";
      IQueryExt result = parseQuery(sparql);
      printSummary("Filter AND query", sparql, result);
   }

   @Test
   public void testFilterOrQuery() throws SparqlParserException
   {
      final String sparql =
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
            "SELECT ?name1 ?name2\n" +
            "WHERE\n" +
            "{ ?x foaf:name  ?name1 ;\n" +
            "  foaf:mbox  ?mbox1 .\n" +
            "  ?y foaf:name  ?name2 ;\n" +
            "  foaf:mbox  ?mbox2 .\n" +
            "  FILTER (?mbox1 = ?mbox2 || ?name1 != ?name2) }";
      IQueryExt result = parseQuery(sparql);
      printSummary("Filter OR query", sparql, result);
   }

   @Test
   public void testFilterTypedQuery() throws SparqlParserException
   {
      final String sparql =
            "PREFIX a:      <http://www.w3.org/2000/10/annotation-ns#>\n" +
            "PREFIX dc:     <http://purl.org/dc/elements/1.1/>\n" +
            "PREFIX xsd:    <http://www.w3.org/2001/XMLSchema#>\n" +
            "SELECT ?annot\n" +
            "WHERE\n" +
            "{ ?annot  a:annotates  <http://www.w3.org/TR/rdf-sparql-query/> .\n" +
            "  ?annot  dc:date      ?date .\n" +
            "  FILTER ( ?date > \"2005-01-01T00:00:00Z\"^^xsd:dateTime ) }";
      IQueryExt result = parseQuery(sparql);
      printSummary("Filter typed query", sparql, result);
   }

   @Test
   public void testFilterRegexQuery() throws SparqlParserException
   {
      final String sparql =
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
            "SELECT ?name\n" +
            "WHERE\n" +
            "{ ?x foaf:name  ?name\n" +
            "  FILTER regex(?name, \"^ali\") }";
      IQueryExt result = parseQuery(sparql);
      printSummary("Filter regex query", sparql, result);
   }

   @Test
   public void testFilterStrQuery() throws SparqlParserException
   {
      final String sparql =
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
            "SELECT ?name ?mbox\n" +
            "WHERE\n" + 
            "{ ?x foaf:name  ?name;\n" +
            "     foaf:mbox  ?mbox .\n" +
            "  FILTER regex(str(?mbox), \"@work.example$\") }";
      IQueryExt result = parseQuery(sparql);
      printSummary("Filter str query", sparql, result);
   }

   @Test
   public void testOptionalQuery() throws SparqlParserException
   {
      final String sparql =
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
            "SELECT ?name ?mbox\n" +
            "WHERE\n" +
            "{ ?x foaf:name  ?name .\n" +
            "  OPTIONAL\n" +
            "  { ?x  foaf:mbox  ?mbox } }";
      IQueryExt result = parseQuery(sparql);
      printSummary("Optional query", sparql, result);
   }

   @Test
   public void testMultiOptionalQuery() throws SparqlParserException
   {
      final String sparql =
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
            "SELECT ?name ?mbox ?hpage\n" +
            "WHERE\n" +
            "{ ?x foaf:name  ?name .\n" +
            "  OPTIONAL\n" +
            "  { ?x foaf:mbox ?mbox } .\n" +
            "  OPTIONAL\n" +
            "  { ?x foaf:homepage ?hpage } }";
      IQueryExt result = parseQuery(sparql);
      printSummary("Multi optional query", sparql, result);
   }

   @Test
   public void testMultilineOptionalQuery() throws SparqlParserException
   {
      final String sparql =
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
            "SELECT ?name ?mbox ?hpage\n" +
            "WHERE\n" +
            "{ ?x foaf:name  ?name .\n" +
            "  OPTIONAL\n" +
            "  { ?x foaf:mbox ?mbox .\n" +
            "    ?x foaf:homepage ?hpage } }";
      IQueryExt result = parseQuery(sparql);
      printSummary("Multi optional query", sparql, result);
   }

   @Test
   public void testMultilineOptionalFilterQuery() throws SparqlParserException
   {
      final String sparql =
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
            "SELECT ?name ?mbox ?hpage\n" +
            "WHERE\n" +
            "{ ?x foaf:name  ?name .\n" +
            "  OPTIONAL\n" +
            "  { ?x foaf:mbox ?mbox .\n" +
            "    ?x foaf:homepage ?hpage \n" +
            "    FILTER (?mbox != 'XXX') }\n" +
            "  FILTER (?name = 'John') }";
      IQueryExt result = parseQuery(sparql);
      printSummary("Multi optional query", sparql, result);
   }

   @Test
   public void testNestedOptionalQuery() throws SparqlParserException
   {
      final String sparql =
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
            "SELECT ?name ?mbox ?hpage\n" +
            "WHERE\n" +
            "{ ?x foaf:name  ?name .\n" +
            "  OPTIONAL\n" +
            "  { ?x foaf:mbox ?mbox .\n" +
            "    ?x foaf:homepage ?hpage \n" +
            "    OPTIONAL {\n" +
            "      ?x foaf:phone ?phone } } }";
      IQueryExt result = parseQuery(sparql);
      printSummary("Multi optional query", sparql, result);
   }

   @Test
   public void testFilterOptionalQuery() throws SparqlParserException
   {
      final String sparql =
            "PREFIX  dc:  <http://purl.org/dc/elements/1.1/>\n" +
            "PREFIX  ns:  <http://example.org/ns#>\n" +
            "SELECT  ?title ?price\n" +
            "WHERE\n" +
            "{ ?x dc:title ?title .\n" +
            "  OPTIONAL\n" +
            "  { ?x ns:price ?price .\n" +
            "    FILTER (?price < 30) } }";
      IQueryExt result = parseQuery(sparql);
      printSummary("Filter optional query", sparql, result);
   }

   @Test
   public void testUnionQuery() throws SparqlParserException
   {
      mExpectedException.expect(UnsupportedSparqlExpressionException.class);
      final String sparql =
            "PREFIX dc10:  <http://purl.org/dc/elements/1.0/>\n" +
            "PREFIX dc11:  <http://purl.org/dc/elements/1.1/>\n" +
            "SELECT ?title ?author\n" +
            "WHERE\n" +
            "{ { ?book dc10:title ?title .  ?book dc10:creator ?author }\n" +
            "  UNION\n" +
            "  { ?book dc11:title ?title .  ?book dc11:creator ?author } }";
      IQueryExt result = parseQuery(sparql);
      printSummary("Union query", sparql, result);
   }

   private IQueryExt parseQuery(String sparqlString) throws SparqlParserException
   {
      return mParser.parse(sparqlString);
   }

   private void printSummary(String testId, String sparql, IQueryExt result)
   {
      StringBuilder sb = new StringBuilder();
      sb.append("\n\n" + testId);
      sb.append("\n====================================================================================================================================\n");
      sb.append(sparql);
      sb.append("\n------------------------------------------------------------------------------------------------------------------------------------\n");
      sb.append(PrinterUtils.print(result));
      sb.append("\n====================================================================================================================================\n");
      LOG.info(sb.toString());
   }
}
