package com.obidea.semantika.mapping.parser.termalxml;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;

import com.obidea.semantika.database.BerlinBenchmarkMetaModel;
import com.obidea.semantika.io.FileDocumentSource;
import com.obidea.semantika.mapping.IMetaModel;
import com.obidea.semantika.mapping.MappingSet;
import com.obidea.semantika.mapping.exception.MappingParserException;
import com.obidea.semantika.mapping.parser.MappingParserConfiguration;
import com.obidea.semantika.util.ConfigHelper;
import com.obidea.semantika.util.LogUtils;
import com.obidea.semantika.util.PrinterUtils;

public class TermalXmlParserTest
{
   private static final String ROOT_PATH = "parser/termalxml/";

   private static TermalXmlParser sTermalParser = new TermalXmlParser(getMetaModel());

   private static MappingParserConfiguration sConfiguration = new MappingParserConfiguration();
   static {
      sConfiguration.setStrictParsing(false);
   }

   private MappingSet mMappingSet;

   private static final Logger LOG = LogUtils.createLogger("semantika.test"); //$NON-NLS-1$

   @Rule
   public ExpectedException mExpectedException = ExpectedException.none();

   @Before
   public void createMappingSet()
   {
      mMappingSet = new MappingSet();
   }

   @Test
   public void testSubjectMap() throws MappingParserException, IOException
   {
      FileDocumentSource inputFile = getDocumentSourceFromFile("subject-map.xml");
      sTermalParser.parse(inputFile, mMappingSet, sConfiguration);
      printMappingSet("Test Subject Map"); //$NON-NLS-1$
      assertMappingSize(1);
   }

   @Test
   public void testPredicateObjectMap() throws MappingParserException, IOException
   {
      FileDocumentSource inputFile = getDocumentSourceFromFile("predicate-object-map.xml");
      sTermalParser.parse(inputFile, mMappingSet, sConfiguration);
      printMappingSet("Test Predicate-Object Map"); //$NON-NLS-1$
      assertMappingSize(4);
   }

   @Test
   public void testPredicateObjectMapManyToMany() throws MappingParserException, IOException
   {
      FileDocumentSource inputFile = getDocumentSourceFromFile("many-to-many.xml");
      sTermalParser.parse(inputFile, mMappingSet, sConfiguration);
      printMappingSet("Test Many-to-Many Map"); //$NON-NLS-1$
      assertMappingSize(1);
   }

   /*
    * Privete utility methods
    */

   private FileDocumentSource getDocumentSourceFromFile(String filename)
   {
      final String path = ROOT_PATH + filename;
      File fin = new File(ConfigHelper.findAsResource(path).getPath());
      return new FileDocumentSource(fin);
   }

   private static IMetaModel getMetaModel()
   {
      return new BerlinBenchmarkMetaModel();
   }

   private void assertMappingSize(int expected)
   {
      assertEquals(expected, mMappingSet.size());
   }

   private void printMappingSet(String testName)
   {
      String message = String.format("%s\n%s\n", testName, PrinterUtils.print(mMappingSet));
      LOG.info(message);
   }
}
