package com.obidea.semantika.materializer.pgsql;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

import com.obidea.semantika.app.ApplicationFactory;
import com.obidea.semantika.app.ApplicationManager;
import com.obidea.semantika.materializer.RdfMaterializerEngine;

public class RdfMaterializerTest
{
   private static final String CONFIG_FILE = "com/obidea/semantika/materializer/pgsql/application.cfg.xml";

   private static ApplicationManager mAppManager;

   @BeforeClass
   public static void setUp() throws Exception
   {
      mAppManager = new ApplicationFactory().configure(CONFIG_FILE).createApplicationManager();
   }

   @Test
   public void testNTriplesMaterializer() throws Exception
   {
      File fout = File.createTempFile("bsbm", ".n3", new File("/tmp"));
      RdfMaterializerEngine materializer = mAppManager.createMaterializerEngine().useNTriples();
      materializer.start();
      materializer.materialize(fout);
      materializer.stop();
   }

   @Test
   public void testTurtleMaterializer() throws Exception
   {
      File fout = File.createTempFile("bsbm", ".ttl", new File("/tmp"));
      RdfMaterializerEngine materializer = mAppManager.createMaterializerEngine().useTurtle();
      materializer.start();
      materializer.materialize(fout);
      materializer.stop();
   }

   @Test
   public void testRdfXmlMaterializer() throws Exception
   {
      File fout = File.createTempFile("bsbm", ".xml", new File("/tmp"));
      RdfMaterializerEngine materializer = mAppManager.createMaterializerEngine().useRdfXml();
      materializer.start();
      materializer.materialize(fout);
      materializer.stop();
   }

   @Test
   public void testRdfJsonMaterializer() throws Exception
   {
      File fout = File.createTempFile("bsbm", ".jsonld", new File("/tmp"));
      RdfMaterializerEngine materializer = mAppManager.createMaterializerEngine().useRdfJson();
      materializer.start();
      materializer.materialize(fout);
      materializer.stop();
   }
}
