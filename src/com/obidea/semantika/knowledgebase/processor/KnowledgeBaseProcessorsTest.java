package com.obidea.semantika.knowledgebase.processor;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import org.junit.Test;
import org.slf4j.Logger;

import com.obidea.semantika.app.MappingLoader;
import com.obidea.semantika.app.MappingLoaderFactory;
import com.obidea.semantika.app.OntologyLoader;
import com.obidea.semantika.database.BerlinBenchmarkDatabaseMetadata;
import com.obidea.semantika.database.IDatabase;
import com.obidea.semantika.database.IDatabaseMetadata;
import com.obidea.semantika.database.sql.dialect.IDialect;
import com.obidea.semantika.exception.SemantikaException;
import com.obidea.semantika.knowledgebase.DefaultPrefixManager;
import com.obidea.semantika.knowledgebase.model.IKnowledgeBase;
import com.obidea.semantika.mapping.IMappingSet;
import com.obidea.semantika.mapping.parser.MappingParserFactoryRegistry;
import com.obidea.semantika.mapping.parser.termalxml.TermalXmlParserFactory;
import com.obidea.semantika.ontology.IOntology;
import com.obidea.semantika.util.ConfigHelper;
import com.obidea.semantika.util.LogUtils;
import com.obidea.semantika.util.PrinterUtils;

public class KnowledgeBaseProcessorsTest
{
   private static final String ROOT_PATH = "processor/"; //$NON-NLS-1$

   private static final Logger LOG = LogUtils.createLogger("semantika.test"); //$NON-NLS-1$

   private IKnowledgeBase mKnowledgeBase;

   private static DefaultPrefixManager sPrefixManager = new DefaultPrefixManager();
   static {
      sPrefixManager.setDefaultPrefix("http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/"); //$NON-NLS-1$
   }

   static {
      MappingParserFactoryRegistry registry = MappingParserFactoryRegistry.getInstance();
      registry.register(0, new TermalXmlParserFactory()); // top priority
   }

   @Test
   public void testSubClassOf() throws Exception
   {
      setupSettings("bsbm.owl", "bsbm_case1.xml"); //$NON-NLS-1$ //$NON-NLS-2$
      
      applyTMappingOptimization();
      printMappingSet();
      assertMappingSize(6);
      
      applyDisjunctionOptimization();
      printMappingSet();
      assertMappingSize(4);
      
      applyForeignKeyOptimization();
      printMappingSet();
      assertMappingSize(4);
   }

   @Test
   public void testSubDataPropertyOf() throws Exception
   {
      setupSettings("bsbm.owl", "bsbm_case2.xml"); //$NON-NLS-1$ //$NON-NLS-2$
      
      applyTMappingOptimization();
      printMappingSet();
      assertMappingSize(4);
      
      applyDisjunctionOptimization();
      printMappingSet();
      assertMappingSize(4);
      
      applyForeignKeyOptimization();
      printMappingSet();
      assertMappingSize(4);
   }

   @Test
   public void testSubObjectPropertyOf() throws Exception
   {
      setupSettings("bsbm.owl", "bsbm_case3.xml"); //$NON-NLS-1$ //$NON-NLS-2$
      
      applyTMappingOptimization();
      printMappingSet();
      assertMappingSize(4);
      
      applyDisjunctionOptimization();
      printMappingSet();
      assertMappingSize(3);
      
      applyForeignKeyOptimization();
      printMappingSet();
      assertMappingSize(3);
   }

   @Test
   public void testDataPropertyDomain() throws Exception
   {
      setupSettings("bsbm.owl", "bsbm_case4.xml"); //$NON-NLS-1$ //$NON-NLS-2$
      
      applyTMappingOptimization();
      printMappingSet();
      assertMappingSize(2);
      
      applyDisjunctionOptimization();
      printMappingSet();
      assertMappingSize(2);
      
      applyForeignKeyOptimization();
      printMappingSet();
      assertMappingSize(2);
   }

   @Test
   public void testObjectPropertyDomainAndRange() throws Exception
   {
      setupSettings("bsbm.owl", "bsbm_case5.xml"); //$NON-NLS-1$ //$NON-NLS-2$
      
      applyTMappingOptimization();
      printMappingSet();
      assertMappingSize(4);
      
      applyDisjunctionOptimization();
      printMappingSet();
      assertMappingSize(4);
      
      applyForeignKeyOptimization();
      printMappingSet();
      assertMappingSize(3);
   }

   @Test
   public void testInverseObjectProperty() throws Exception
   {
      setupSettings("bsbm.owl", "bsbm_case6.xml"); //$NON-NLS-1$ //$NON-NLS-2$
      
      applyTMappingOptimization();
      printMappingSet();
      assertMappingSize(2);
      
      applyDisjunctionOptimization();
      printMappingSet();
      assertMappingSize(2);
      
      applyForeignKeyOptimization();
      printMappingSet();
      assertMappingSize(2);
   }

   @Test
   public void testInverseObjectPropertyWithDomainAndRange() throws Exception
   {
      setupSettings("bsbm.owl", "bsbm_case7.xml"); //$NON-NLS-1$ //$NON-NLS-2$

      applyTMappingOptimization();
      printMappingSet();
      assertMappingSize(6);
      
      applyDisjunctionOptimization();
      printMappingSet();
      assertMappingSize(6);
      
      applyForeignKeyOptimization();
      printMappingSet();
      assertMappingSize(6);
   }

   @Test
   public void testSymmetricObjectProperty() throws Exception
   {
      setupSettings("bsbm.owl", "bsbm_case8.xml"); //$NON-NLS-1$ //$NON-NLS-2$
      
      applyTMappingOptimization();
      printMappingSet();
      assertMappingSize(2);
      
      applyDisjunctionOptimization();
      printMappingSet();
      assertMappingSize(2);
      
      applyForeignKeyOptimization();
      printMappingSet();
      assertMappingSize(2);
   }

   @Test
   public void testEquivalentClasses() throws Exception
   {
      setupSettings("bsbm.owl", "bsbm_case9.xml"); //$NON-NLS-1$ //$NON-NLS-2$
      
      applyTMappingOptimization();
      printMappingSet();
      assertMappingSize(3);
      
      applyDisjunctionOptimization();
      printMappingSet();
      assertMappingSize(3);
      
      applyForeignKeyOptimization();
      printMappingSet();
      assertMappingSize(3);
   }

   @Test
   public void testEquivalentDataProperties() throws Exception
   {
      setupSettings("bsbm.owl", "bsbm_case10.xml"); //$NON-NLS-1$ //$NON-NLS-2$
      
      applyTMappingOptimization();
      printMappingSet();
      assertMappingSize(6);
      
      applyDisjunctionOptimization();
      printMappingSet();
      assertMappingSize(6);
      
      applyForeignKeyOptimization();
      printMappingSet();
      assertMappingSize(6);
   }

   @Test
   public void testEquivalentObjectProperties() throws Exception
   {
      setupSettings("bsbm.owl", "bsbm_case11.xml"); //$NON-NLS-1$ //$NON-NLS-2$
      
      applyTMappingOptimization();
      printMappingSet();
      assertMappingSize(2);
      
      applyDisjunctionOptimization();
      printMappingSet();
      assertMappingSize(2);
      
      applyForeignKeyOptimization();
      printMappingSet();
      assertMappingSize(2);
   }

   @Test
   public void testAll() throws Exception
   {
      setupSettings("bsbm.owl", "bsbmapp.xml"); //$NON-NLS-1$ //$NON-NLS-2$
      
      applyTMappingOptimization();
      printMappingSet();
      assertMappingSize(40);
      
      applyDisjunctionOptimization();
      printMappingSet();
      assertMappingSize(37);
      
      applyForeignKeyOptimization();
      printMappingSet();
      assertMappingSize(37);
   }

   /*
    * Utility methods to initialize knowledge base
    */

   private void setupSettings(final String owlSource, final String mapSource) throws Exception
   {
      final IOntology ontology = loadOntology(owlSource);
      final IDatabase database = loadDatabase();
      final IMappingSet mappingSet = loadMappings(mapSource, database, ontology);
      
      mKnowledgeBase = new IKnowledgeBase()
      {
         @Override
         public IOntology getOntology() { return ontology; }
         @Override
         public IDatabase getDatabase() { return database; }
         @Override
         public IMappingSet getMappingSet() { return mappingSet; }
      };
   }

   private IOntology loadOntology(String owlSource) throws SemantikaException
   {
      InputStream in = ConfigHelper.getResourceStream(ROOT_PATH + owlSource);
      OntologyLoader ontologyLoader = new OntologyLoader();
      return ontologyLoader.loadOntologyFromDocument(in);
   }

   private IDatabase loadDatabase() throws SemantikaException
   {
      return new IDatabase() 
      {
         @Override
         public IDatabaseMetadata getMetadata() { return new BerlinBenchmarkDatabaseMetadata(); }
         @Override
         public String getDatabaseProduct() { return ""; } //$NON-NLS-1$
         @Override
         public int getDatabaseMajorVersion() { return 0; }
         @Override
         public int getDatabaseMinorVersion() { return 0; }
         @Override
         public IDialect getDialect() { return null; }
      };
   }

   private IMappingSet loadMappings(String mapSource, IDatabase database, IOntology ontology) throws SemantikaException
   {
      InputStream in = ConfigHelper.getResourceStream(ROOT_PATH + mapSource);
      MappingLoader mappingLoader = buildMappingLoader(database, ontology);
      return mappingLoader.loadMappingFromDocument(in);
   }

   private static MappingLoader buildMappingLoader(IDatabase database, IOntology ontology)
   {
      return MappingLoaderFactory.createMappingLoader(database, ontology);
   }

   private void applyTMappingOptimization() throws KnowledgeBaseProcessorException
   {
      TMappingProcessor processor = new TMappingProcessor();
      processor.optimize(mKnowledgeBase);
   }

   private void applyDisjunctionOptimization() throws KnowledgeBaseProcessorException
   {
      DisjunctionProcessor processor = new DisjunctionProcessor();
      processor.optimize(mKnowledgeBase);
   }

   private void applyForeignKeyOptimization() throws KnowledgeBaseProcessorException
   {
      ReferentialIntegrityProcessor processor = new ReferentialIntegrityProcessor();
      processor.optimize(mKnowledgeBase);
   }

   /*
    * Private utility methods
    */

   private void assertMappingSize(int expectedSize)
   {
      assertEquals(expectedSize, mKnowledgeBase.getMappingSet().size());
   }

   private void printMappingSet()
   {
      IMappingSet mappingSet = mKnowledgeBase.getMappingSet();
      String msg = PrinterUtils.print(mappingSet, sPrefixManager);
      LOG.info("Mappings printout:\n{}", msg); //$NON-NLS-1$
   }
}
