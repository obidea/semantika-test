package com.obidea.semantika.database;

import java.util.Set;

import com.obidea.semantika.database.base.IForeignKey;
import com.obidea.semantika.database.base.IPrimaryKey;
import com.obidea.semantika.database.base.ITable;

public class BerlinBenchmarkDatabaseMetadata implements IDatabaseMetadata
{
   private BerlinBenchmarkDatabase dbBsbm = new BerlinBenchmarkDatabase();

   @Override
   public ITable getTable(String fullName)
   {
      return dbBsbm.getTable(fullName);
   }

   @Override
   public Set<ITable> getTables()
   {
      return dbBsbm.getTables();
   }

   @Override
   public Set<IPrimaryKey> getPrimaryKeys()
   {
      return null;
   }

   @Override
   public Set<IForeignKey> getForeignKeys()
   {
      return dbBsbm.getForeignKeys();
   }
}
