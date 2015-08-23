package com.obidea.semantika.database;

import com.obidea.semantika.app.OntologyLoader;
import com.obidea.semantika.mapping.IMetaModel;
import com.obidea.semantika.ontology.IOntology;
import com.obidea.semantika.ontology.exception.OntologyCreationException;

public class BerlinBenchmarkMetaModel implements IMetaModel
{
   @Override
   public IDatabaseMetadata getDatabaseMetadata()
   {
      return new BerlinBenchmarkDatabaseMetadata();
   }

   @Override
   public IOntology getOntology()
   {
      try {
         return new OntologyLoader().createEmptyOntology();
      }
      catch (OntologyCreationException e) {
         e.printStackTrace();
         return null;
      }
   }
}
