package com.obidea.semantika.knowledgebase;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DefaultPrefixManagerTest
{
   private DefaultPrefixManager mPrefixManager;

   private static final Logger LOG = Logger.getLogger("semantika.test"); //$NON-NLS-1$

   @Rule
   public ExpectedException mExpectedException = ExpectedException.none();

   @Before
   public void createMappingSet()
   {
      mPrefixManager = new DefaultPrefixManager();
      mPrefixManager.setDefaultPrefix("http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/"); //$NON-NLS-1$
      mPrefixManager.setPrefix("bsbm", "http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/"); //$NON-NLS-1$ //$NON-NLS-2$
      mPrefixManager.setPrefix("dc", "http://purl.org/dc/elements/1.1/"); //$NON-NLS-1$ //$NON-NLS-2$
      // +4 more prefixes by default
   }

   @Test
   public void testSetNewPrefix()
   {
      mPrefixManager.setPrefix("foaf", "http://xmlns.com/foaf/0.1/"); //$NON-NLS-1$ //$NON-NLS-2$
      assertSize(8);
   }

   @Test
   public void testSetExistingPrefix()
   {
      mPrefixManager.setPrefix("dc", "http://purl.org/dc/elements/1.1/"); //$NON-NLS-1$ //$NON-NLS-2$
      assertSize(7);
   }

   @Test
   public void testCopyPrefixManager()
   {
      DefaultPrefixManager newPrefixManager = new DefaultPrefixManager(mPrefixManager);
      newPrefixManager.setPrefix("rev", "http://purl.org/stuff/rev#"); //$NON-NLS-1$ //$NON-NLS-2$
      newPrefixManager.setPrefix("foaf", "http://xmlns.com/foaf/0.1/"); //$NON-NLS-1$ //$NON-NLS-2$
      assertEquals(9, newPrefixManager.getPrefixMapper().size());
   }

   @Test
   public void testExpandNameRdf()
   {
      String targetName = "rdf:type"; //$NON-NLS-1$
      URI result = mPrefixManager.expand(targetName);
      assertEquals(URI.create("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"), result); //$NON-NLS-1$
   }

   @Test
   public void testExpandNameBsbm()
   {
      String targetName = "bsbm:Product"; //$NON-NLS-1$
      URI result = mPrefixManager.expand(targetName);
      assertEquals(URI.create("http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/Product"), result); //$NON-NLS-1$
   }

   @Test
   public void testExpandNameDummy() // should failed
   {
      String targetName = "ex:Product"; //$NON-NLS-1$
      mExpectedException.expect(PrefixNotFoundException.class);
      mPrefixManager.expand(targetName);
   }

   @Test
   public void testExpandNameDefaultPrefix_Var1()
   {
      String targetName = ":Product"; //$NON-NLS-1$
      URI result = mPrefixManager.expand(targetName);
      assertEquals(URI.create("http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/Product"), result); //$NON-NLS-1$
   }

   @Test
   public void testExpandNameDefaultPrefix_Var2()
   {
      String targetName = "Product"; //$NON-NLS-1$
      URI result = mPrefixManager.expand(targetName);
      assertEquals(URI.create("http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/Product"), result); //$NON-NLS-1$
   }

   @Test
   public void testShortenNameRdf()
   {
      URI targetUri = URI.create("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"); //$NON-NLS-1$
      String result = mPrefixManager.shorten(targetUri);
      assertEquals("rdf:type", result); //$NON-NLS-1$
   }
 
   @Test
   public void testShortenNameBsbm()
   {
      URI targetUri = URI.create("http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/Product"); //$NON-NLS-1$
      String result = mPrefixManager.shorten(targetUri);
      assertEquals(":Product", result); //$NON-NLS-1$
   }

   @Test
   public void testShortenNameDummy() // should failed
   {
      URI targetUri = URI.create("http://www.example.org/Product"); //$NON-NLS-1$
      mExpectedException.expect(NamespaceNotFoundException.class);
      mPrefixManager.shorten(targetUri);
   }

   @Test
   public void testPrintPrefixManager()
   {
      DefaultPrefixManager newPrefixManager = new DefaultPrefixManager(mPrefixManager);
      newPrefixManager.setPrefix("rev", "http://purl.org/stuff/rev#"); //$NON-NLS-1$ //$NON-NLS-2$
      newPrefixManager.setPrefix("foaf", "http://xmlns.com/foaf/0.1/"); //$NON-NLS-1$ //$NON-NLS-2$
      LOG.log(Level.INFO, newPrefixManager.toString());
   }

   private void assertSize(int expectedSize)
   {
      assertEquals(expectedSize, mPrefixManager.getPrefixMapper().size());
   }
}
