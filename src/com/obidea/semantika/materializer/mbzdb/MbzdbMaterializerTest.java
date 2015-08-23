package com.obidea.semantika.materializer.mbzdb;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

import com.obidea.semantika.app.ApplicationFactory;
import com.obidea.semantika.app.ApplicationManager;
import com.obidea.semantika.materializer.RdfMaterializerEngine;

public class MbzdbMaterializerTest
{
   private static final String CONFIG_FILE = "com/obidea/semantika/materializer/mbzdb/application.cfg.xml";

   private static ApplicationManager mAppManager;

   @BeforeClass
   public static void setUp() throws Exception
   {
      mAppManager = new ApplicationFactory().configure(CONFIG_FILE).createApplicationManager();
   }

   @Test
   public void testMaterializer() throws Exception
   {
      File fout = File.createTempFile("output", ".n3", new File("/tmp"));
      RdfMaterializerEngine materializer = mAppManager.createMaterializerEngine().useNTriples();
      materializer.start();
      materializer.materialize(fout);
      materializer.stop();
   }
}
