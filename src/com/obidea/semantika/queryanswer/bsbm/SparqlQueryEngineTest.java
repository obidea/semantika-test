package com.obidea.semantika.queryanswer.bsbm;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;

import com.obidea.semantika.app.ApplicationFactory;
import com.obidea.semantika.app.ApplicationManager;
import com.obidea.semantika.queryanswer.SparqlQueryEngine;
import com.obidea.semantika.queryanswer.internal.QueryTranslationException;
import com.obidea.semantika.queryanswer.result.IQueryResult;
import com.obidea.semantika.util.LogUtils;

public class SparqlQueryEngineTest
{
   private static final String CONFIG_FILE = "com/obidea/semantika/queryanswer/bsbm/application.cfg.xml"; //$NON-NLS-1$

   private static SparqlQueryEngine mQueryEngine;

   private static final Logger LOG = LogUtils.createLogger("semantika.test"); //$NON-NLS-1$

   @Rule
   public ExpectedException mExpectedException = ExpectedException.none();

   @BeforeClass
   public static void setUp() throws Exception
   {
      ApplicationManager appManager = new ApplicationFactory().configure(CONFIG_FILE).createApplicationManager();
      mQueryEngine = appManager.createQueryEngine();
      mQueryEngine.start();
   }

   @AfterClass
   public static void close() throws Exception
   {
      mQueryEngine.stop();
   }

   @Test
   public void testClassQuery() throws Exception
   {
      final String sparql = 
            "PREFIX :   <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/>\n" +
            "SELECT ?x \n" +
            "WHERE\n" +
            "{ ?x a :Product . }"; //$NON-NLS-1$
      
      IQueryResult result = mQueryEngine.evaluate(sparql);
      assertTotalRow(result, 71431);
   }

   @Test
   public void testPropertyQuery() throws Exception
   {
      final String sparql = 
            "PREFIX :   <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/>\n" +
            "SELECT ?label \n" +
            "WHERE\n" +
            "{ ?x rdfs:label ?label . }"; //$NON-NLS-1$
      
      IQueryResult result = mQueryEngine.evaluate(sparql);
      assertTotalRow(result, 92899);
   }

   @Test
   public void testSimpleQuery() throws Exception
   {
      final String sparql = 
            "PREFIX :   <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/>\n" +
            "SELECT ?x ?label \n" +
            "WHERE\n" +
            "{ ?x a :Product .\n" +
            "  ?x rdfs:label ?label . }"; //$NON-NLS-1$
      
      IQueryResult result = mQueryEngine.evaluate(sparql);
      assertTotalRow(result, 71431);
   }

   @Test
   public void testSimpleQueryWithPagination() throws Exception
   {
      final String sparql = 
            "PREFIX :   <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/>\n" +
            "SELECT ?x ?label \n" +
            "WHERE\n" +
            "{ ?x a :Product .\n" +
            "  ?x rdfs:label ?label . }"; //$NON-NLS-1$
      
      int offset = 0;
      int limit = 100;
      int maxPage = 5;
      int pageNum = 1;
      while (pageNum <= maxPage) {
         IQueryResult result = mQueryEngine.createQuery(sparql).setFirstResult(offset).setMaxResults(limit).evaluate();
         assertTotalRow(result, 100);
         offset += limit;
         pageNum++;
      }
   }

   @Test
   public void testSimpleQueryWithSlice() throws Exception
   {
      final String sparql = 
            "PREFIX :   <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/>\n" +
            "SELECT ?x ?label \n" +
            "WHERE\n" +
            "{ ?x a :Product .\n" +
            "  ?x rdfs:label ?label . }\n" +
            "LIMIT 100 OFFSET 50"; //$NON-NLS-1$
      
      IQueryResult result = mQueryEngine.evaluate(sparql);
      assertTotalRow(result, 100);
   }

   @Test
   public void testBenchmarkQuery01() throws Exception
   {
      final String sparql =
            "PREFIX bsbm: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/>\n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n\n" +
            "SELECT DISTINCT ?product ?label\n" +
            "WHERE\n" +
            "{ ?product rdfs:label ?label .\n" +
            "  ?product bsbm:type <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductType430> .\n" +
            "  ?product bsbm:productFeature <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductFeature107> .\n" +
            "  ?product bsbm:productFeature <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductFeature1609> .\n" +
            "  ?product bsbm:productPropertyNumeric1 ?value1 . \n" +
            "  FILTER (?value1 > 354) }";
      
      IQueryResult result = mQueryEngine.evaluate(sparql);
      assertTotalRow(result, 2);
   }

   @Test
   public void testBenchmarkQuery02() throws Exception
   {
      final String sparql =
            "PREFIX bsbm: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/>\n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n\n" +
            "SELECT ?label ?comment ?producer ?productFeature ?propertyTextual1 ?propertyTextual2 ?propertyTextual3 \n" +
            "       ?propertyNumeric1 ?propertyNumeric2 ?propertyTextual4 ?propertyTextual5 ?propertyNumeric4 \n" +
            "WHERE\n" +
            "{ <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer1168/Product57925> rdfs:label ?label .\n" +
            "  <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer1168/Product57925> rdfs:comment ?comment .\n" +
            "  <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer1168/Product57925> bsbm:producer ?p .\n" +
            "  ?p rdfs:label ?producer .\n" +
            "  <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer1168/Product57925> dc:publisher ?p .\n" +
            "  <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer1168/Product57925> bsbm:productFeature ?f . \n" +
            "  ?f rdfs:label ?productFeature . \n" +
            "  <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer1168/Product57925> bsbm:productPropertyTextual1 ?propertyTextual1 . \n" +
            "  <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer1168/Product57925> bsbm:productPropertyTextual2 ?propertyTextual2 . \n" +
            "  <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer1168/Product57925> bsbm:productPropertyTextual3 ?propertyTextual3 . \n" +
            "  <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer1168/Product57925> bsbm:productPropertyNumeric1 ?propertyNumeric1 . \n" +
            "  <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer1168/Product57925> bsbm:productPropertyNumeric2 ?propertyNumeric2 . \n" +
            "  OPTIONAL { <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer1168/Product57925> bsbm:productPropertyTextual4 ?propertyTextual4 } . \n" +
            "  OPTIONAL { <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer1168/Product57925> bsbm:productPropertyTextual5 ?propertyTextual5 } . \n" +
            "  OPTIONAL { <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer1168/Product57925> bsbm:productPropertyNumeric4 ?propertyNumeric4 } . }";
      
      IQueryResult result = mQueryEngine.evaluate(sparql);
      assertTotalRow(result, 24);
   }

   @Test
   public void testBenchmarkQuery03() throws Exception
   {
      final String sparql =
            "PREFIX bsbm: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/>\n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n\n" +
            "SELECT ?product ?label \n" +
            "WHERE\n" +
            "{ ?product rdfs:label ?label .\n" +
            "  ?product bsbm:type <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductType354> .\n" +
            "  ?product bsbm:productFeature <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductFeature11345> .\n" +
            "  ?product bsbm:productPropertyNumeric1 ?p1 .\n" +
            "  FILTER ( ?p1 > 266 ) .\n" +
            "  ?product bsbm:productPropertyNumeric3 ?p3 .\n" +
            "  FILTER (?p3 < 275 ) .\n" +
            "  OPTIONAL {\n" +
            "    ?product bsbm:productFeature <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductFeature11344> .\n" +
            "    ?product rdfs:label ?testVar } .\n" +
            "  FILTER (!bound(?testVar)) }";
      
      IQueryResult result = mQueryEngine.evaluate(sparql);
      assertTotalRow(result, 6);
   }

   @Test
   public void testBenchmarkQuery04() throws Exception
   {
      mExpectedException.expect(QueryTranslationException.class);
      final String sparql =
            "PREFIX bsbm: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/>\n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n\n" +
            "SELECT DISTINCT ?product ?label ?propertyTextual \n" +
            "WHERE {\n" +
            "{ ?product rdfs:label ?label .\n" +
            "  ?product bsbm:type <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductType326> .\n" +
            "  ?product bsbm:productFeature <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductFeature10414> .\n" +
            "  ?product bsbm:productFeature <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductFeature64> .\n" +
            "  ?product bsbm:productPropertyTextual1 ?propertyTextual .\n" +
            "  ?product bsbm:productPropertyNumeric1 ?p1 .\n" +
            "  FILTER ( ?p1 > 188 ) .\n" +
            "} UNION {\n" +
            "  ?product rdfs:label ?label .\n" +
            "  ?product bsbm:type <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductType326> .\n" +
            "  ?product bsbm:productFeature <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductFeature10414> .\n" +
            "  ?product bsbm:productFeature <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductFeature10415> .\n" +
            "  ?product bsbm:productPropertyTextual1 ?propertyTextual .\n" +
            "  ?product bsbm:productPropertyNumeric2 ?p2 .\n" +
            "  FILTER ( ?p2> 284 )\n" +
            "} }";
      
      IQueryResult result = mQueryEngine.evaluate(sparql);
      assertTotalRow(result, 6);
   }

   @Test
   public void testBenchmarkQuery05() throws Exception
   {
      final String sparql =
            "PREFIX bsbm: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/>\n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n\n" +
            "SELECT DISTINCT ?product ?productLabel \n" +
            "WHERE\n" +
            "{ ?product rdfs:label ?productLabel .\n" +
            "  FILTER (<http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer293/Product14348> != ?product) .\n" +
            "  <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer293/Product14348> bsbm:productFeature ?prodFeature .\n" +
            "  ?product bsbm:productFeature ?prodFeature .\n" +
            "  <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer293/Product14348> bsbm:productPropertyNumeric1 ?origProperty1 .\n" +
            "  ?product bsbm:productPropertyNumeric1 ?simProperty1 .\n" +
            "  FILTER (?simProperty1 < (?origProperty1 + 120) && ?simProperty1 > (?origProperty1 - 120)) .\n" +
            "  <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer293/Product14348> bsbm:productPropertyNumeric2 ?origProperty2 .\n" +
            "  ?product bsbm:productPropertyNumeric2 ?simProperty2 .\n" +
            "  FILTER (?simProperty2 < (?origProperty2 + 170) && ?simProperty2 > (?origProperty2 - 170)) }";
      
      IQueryResult result = mQueryEngine.evaluate(sparql);
      assertTotalRow(result, 242);
   }

   @Test
   public void testBenchmarkQuery06() throws Exception
   {
      final String sparql =
            "PREFIX bsbm: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/>\n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n\n" +
            "SELECT ?product ?label \n" +
            "WHERE\n" +
            "{ ?product rdfs:label ?label .\n" +
            "  ?product rdf:type bsbm:Product .\n" +
            "  FILTER regex(?label, \"risks\") }";
      
      IQueryResult result = mQueryEngine.evaluate(sparql);
      assertTotalRow(result, 11);
   }

   @Test
   public void testBenchmarkQuery07() throws Exception
   {
      final String sparql =
            "PREFIX bsbm: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/>\n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n" +
            "PREFIX rev: <http://purl.org/stuff/rev#>\n" +
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n\n" +
            "SELECT * \n" +
            "WHERE {\n" +
            "{ <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer921/Product45722> rdfs:label ?productLabel .\n" +
            "  OPTIONAL {\n" +
            "    ?offer bsbm:product <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer921/Product45722> .\n" +
            "    ?offer bsbm:price ?price .\n" +
            "    ?offer bsbm:vendor ?vendor .\n" +
            "    ?vendor rdfs:label ?vendorTitle .\n" +
            "    ?vendor bsbm:country <http://downlode.org/rdf/iso-3166/countries#DE> .\n" +
            "    ?offer dc:publisher ?vendor .\n" +
            "    ?offer bsbm:validTo ?date .\n" +
            "    FILTER (?date > \"2008-06-20T00:00:00\"^^<http://www.w3.org/2001/XMLSchema#dateTime> ) }\n" +
            "  }\n" +
            "  OPTIONAL {\n" +
            "    ?review bsbm:reviewFor <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer921/Product45722> .\n" +
            "    ?review rev:reviewer ?reviewer .\n" +
            "    ?reviewer foaf:name ?revName .\n" +
            "    ?review dc:title ?revTitle .\n" +
            "    OPTIONAL { ?review bsbm:rating1 ?rating1 . }\n" +
            "    OPTIONAL { ?review bsbm:rating2 ?rating2 . }\n" +
            "  } }";
      
      IQueryResult result = mQueryEngine.evaluate(sparql);
      assertTotalRow(result, 11);
   }

   @Test
   public void testBenchmarkQuery08() throws Exception
   {
      final String sparql =
            "PREFIX bsbm: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/>\n" +
            "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n" +
            "PREFIX rev: <http://purl.org/stuff/rev#>\n" +
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n\n" +
            "SELECT ?title ?text ?reviewDate ?reviewer ?reviewerName ?rating1 ?rating2 ?rating3 ?rating4 \n" +
            "WHERE\n" +
            "{ ?review bsbm:reviewFor <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer1245/Product62033> .\n" +
            "  ?review dc:title ?title .\n" +
            "  ?review rev:text ?text .\n" +
//            "  FILTER langMatches( lang(?text), \"EN\" )\n" +
            "  FILTER ( lang(?text) = \"en\" )\n" +
            "  ?review bsbm:reviewDate ?reviewDate .\n" +
            "  ?review rev:reviewer ?reviewer .\n" +
            "  ?reviewer foaf:name ?reviewerName .\n" +
            "  OPTIONAL { ?review bsbm:rating1 ?rating1 . }\n" +
            "  OPTIONAL { ?review bsbm:rating2 ?rating2 . }\n" +
            "  OPTIONAL { ?review bsbm:rating3 ?rating3 . }\n" +
            "  OPTIONAL { ?review bsbm:rating4 ?rating4 . } }";
      
      IQueryResult result = mQueryEngine.evaluate(sparql);
      assertTotalRow(result, 4);
   }

   @Test
   public void testBenchmarkQuery09() throws Exception
   {
      // SKIP: Use DESCRIBE
   }

   @Test
   public void testBenchmarkQuery10() throws Exception
   {
      final String sparql =
            "PREFIX bsbm: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/>\n" +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
            "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n\n" +
            "SELECT DISTINCT ?offer ?price \n" +
            "WHERE\n" +
            "{ ?offer bsbm:product <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer387/Product18992> .\n" +
            "  ?offer bsbm:vendor ?vendor .\n" +
            "  ?offer dc:publisher ?vendor .\n" +
            "  ?vendor bsbm:country <http://downlode.org/rdf/iso-3166/countries#US> .\n" +
            "  ?offer bsbm:deliveryDays ?deliveryDays .\n" +
            "  FILTER (?deliveryDays <= 3)\n" +
            "  ?offer bsbm:price ?price .\n" +
            "  ?offer bsbm:validTo ?date .\n" +
            "  FILTER (?date > \"2008-06-20T00:00:00\"^^<http://www.w3.org/2001/XMLSchema#dateTime> ) }";
      
      IQueryResult result = mQueryEngine.evaluate(sparql);
      assertTotalRow(result, 7);
   }

   @Test
   public void testBenchmarkQuery11() throws Exception
   {
      mExpectedException.expect(QueryTranslationException.class);
      final String sparql =
            "SELECT ?property ?hasValue ?isValueOf \n" +
            "WHERE {\n" +
            "{ <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromVendor476/Offer935935> ?property ?hasValue } \n" +
            "UNION\n" +
            "{ ?isValueOf ?property <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromVendor476/Offer935935> } }";
      
      IQueryResult result = mQueryEngine.evaluate(sparql);
      assertTotalRow(result, 10);
   }

   @Test
   public void testBenchmarkQuery12() throws Exception
   {
      // SKIP: Use CONSTRUCT
   }

   private void assertTotalRow(IQueryResult result, int expectedSize) throws SQLException
   {
      int counter = 0;
      while (result.next()) {
         counter++;
         LOG.debug(result.getValueArray().toString());
      }
      LOG.info("{} row(s) returned\n", counter);
      assertEquals(expectedSize, counter);
   }
}
