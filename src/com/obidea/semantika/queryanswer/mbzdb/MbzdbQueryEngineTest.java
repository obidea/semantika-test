package com.obidea.semantika.queryanswer.mbzdb;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

import com.obidea.semantika.app.ApplicationFactory;
import com.obidea.semantika.app.ApplicationManager;
import com.obidea.semantika.queryanswer.SparqlQueryEngine;
import com.obidea.semantika.queryanswer.result.IQueryResult;
import com.obidea.semantika.util.LogUtils;

public class MbzdbQueryEngineTest
{
   private static final String CONFIG_FILE = "com/obidea/semantika/materializer/mbzdb/application.cfg.xml"; //$NON-NLS-1$

   private static SparqlQueryEngine mQueryEngine;

   private static final Logger LOG = LogUtils.createLogger("semantika.test"); //$NON-NLS-1$

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
   public void testQuery1() throws Exception
   {
      /*
       * Query 1: Show all artists and their DBPedia URI resource.
       */
      final String sparql = 
            "PREFIX owl:   <http://www.w3.org/2002/07/owl#>\n" +
            "SELECT ?artist ?url \n" +
            "WHERE\n" +
            "{ ?artist owl:sameAs ?url . }"; //$NON-NLS-1$
      
      IQueryResult result = mQueryEngine.evaluate(sparql);
      assertTotalRow(result, 203526);
   }

   @Test
   public void testQuery2() throws Exception
   {
      /*
       * Query 2: Show all records that has track count less than 3
       */
      final String sparql = 
            "PREFIX mo:   <http://purl.org/ontology/mo/>\n" +
            "SELECT ?record ?count \n" +
            "WHERE\n" +
            "{ ?record mo:track_count ?count\n" +
            "  FILTER ( ?count < 3 ) }"; //$NON-NLS-1$
      
      IQueryResult result = mQueryEngine.evaluate(sparql);
      assertTotalRow(result, 128763);
   }

   @Test
   public void testQuery3() throws Exception
   {
      /*
       * Query 3: Show all release events in January 22, 2012
       */
      final String sparql = 
            "PREFIX dc:    <http://purl.org/dc/elements/1.1/>\n" +
            "PREFIX event: <http://purl.org/NET/c4dm/event.owl#>\n" +
            "SELECT ?event \n" +
            "WHERE\n" +
            "{ ?event dc:date ?date\n" +
            "  FILTER ( ?date = '2012-02-22' ) }"; //$NON-NLS-1$
      
      IQueryResult result = mQueryEngine.evaluate(sparql);
      assertTotalRow(result, 120);
   }

   @Test
   public void testQuery4() throws Exception
   {
      /*
       * Query 4: Show all tract title that contains full mix.
       */
      final String sparql = 
            "PREFIX mo:   <http://purl.org/ontology/mo/>\n" +
            "PREFIX dc:    <http://purl.org/dc/elements/1.1/>\n" +
            "SELECT ?title \n" +
            "WHERE\n" +
            "{ ?x a mo:Track;\n" +
            "     dc:title ?title\n" +
            "  FILTER ( REGEX(STR(?title), '.*(full mix).*') ) }"; //$NON-NLS-1$
      
      IQueryResult result = mQueryEngine.evaluate(sparql);
      assertTotalRow(result, 54455);
   }

   private void assertTotalRow(IQueryResult result, int expectedNumber) throws SQLException
   {
      int counter = 0;
      while (result.next()) {
         counter++;
         LOG.debug(result.getValueArray().toString());
      }
      LOG.info("{} row(s) returned", counter);
      assertEquals(expectedNumber, counter);
   }
}
