package com.obidea.semantika.app;

import static org.junit.Assert.*;

import org.junit.Test;

import com.obidea.semantika.exception.ConfigurationException;

public class ApplicationFactoryTest
{
   @Test
   public void defaultConfigurationTest() throws ConfigurationException
   {
      ApplicationManager manager = new ApplicationFactory().configure().createApplicationManager();
      assertNotNull("Create QueryEngine", manager.createQueryEngine());
      assertNotNull("Create MaterializerEngine", manager.createMaterializerEngine());
   }

   @Test
   public void fileConfigurationTest() throws ConfigurationException
   {
      ApplicationManager manager = new ApplicationFactory().configure("application.cfg.xml").createApplicationManager();
      assertNotNull("Create QueryEngine", manager.createQueryEngine());
      assertNotNull("Create MaterializerEngine", manager.createMaterializerEngine());
   }

   @Test
   public void programmaticConfigurationTest()
   {
      ApplicationManager manager = new ApplicationFactory()
            .setName("app-test")
            .addProperty(Environment.CONNECTION_URL, "jdbc:mysql://localhost:3306/empdb")
            .addProperty(Environment.CONNECTION_DRIVER, "com.mysql.jdbc.Driver")
            .addProperty(Environment.CONNECTION_USERNAME, "semantika")
            .addProperty(Environment.CONNECTION_PASSWORD, "semantika")
            .addProperty(Environment.TRANSACTION_TIMEOUT, 600)
            .addProperty(Environment.TRANSACTION_FETCH_SIZE, 200)
            .setOntologySource("model/empdb/empdb.owl")
            .addMappingSource("model/empdb/mysql/empdb.tml.xml")
            .createApplicationManager();
      assertNotNull("Create QueryEngine", manager.createQueryEngine());
      assertNotNull("Create MaterializerEngine", manager.createMaterializerEngine());
   }
}
