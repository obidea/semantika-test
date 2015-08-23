package com.obidea.semantika.database;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.obidea.semantika.database.base.Column;
import com.obidea.semantika.database.base.ForeignKey;
import com.obidea.semantika.database.base.IForeignKey;
import com.obidea.semantika.database.base.ISchema;
import com.obidea.semantika.database.base.ITable;
import com.obidea.semantika.database.base.Schema;
import com.obidea.semantika.database.base.Table;
import com.obidea.semantika.database.exception.TableNotFoundException;

/**
 * A Java object representation of Berlin SPARQL Benchmark database schema.
 */
public class BerlinBenchmarkDatabase
{
   private Map<String, ITable> mTableDictionary = new HashMap<String, ITable>();
   private Set<IForeignKey> mForeignKeySet = new HashSet<IForeignKey>();

   private static final ISchema DEFAULT_SCHEMA = new Schema("default");

   private static Table sTableOffer = new Table(DEFAULT_SCHEMA, "offer");
   static {
      sTableOffer.addColumn(new Column(sTableOffer, "nr", 4, true));
      sTableOffer.addColumn(new Column(sTableOffer, "product", 4));
      sTableOffer.addColumn(new Column(sTableOffer, "producer", 4));
      sTableOffer.addColumn(new Column(sTableOffer, "vendor", 4));
      sTableOffer.addColumn(new Column(sTableOffer, "price", 8));
      sTableOffer.addColumn(new Column(sTableOffer, "validFrom", 93));
      sTableOffer.addColumn(new Column(sTableOffer, "validTo", 93));
      sTableOffer.addColumn(new Column(sTableOffer, "deliveryDays", 4));
      sTableOffer.addColumn(new Column(sTableOffer, "offerWebpage", 12));
      sTableOffer.addColumn(new Column(sTableOffer, "publisher", 4));
      sTableOffer.addColumn(new Column(sTableOffer, "publishDate", 91));
   }

   private static Table sTablePerson = new Table(DEFAULT_SCHEMA, "person");
   static {
      sTablePerson.addColumn(new Column(sTablePerson, "nr", 4, true));
      sTablePerson.addColumn(new Column(sTablePerson, "name", 12));
      sTablePerson.addColumn(new Column(sTablePerson, "mbox_sha1sum", 1));
      sTablePerson.addColumn(new Column(sTablePerson, "country", 1));
      sTablePerson.addColumn(new Column(sTablePerson, "publisher", 4));
      sTablePerson.addColumn(new Column(sTablePerson, "publishDate", 91));
   }

   private static Table sTableProducer = new Table(DEFAULT_SCHEMA, "producer");
   static {
      sTableProducer.addColumn(new Column(sTableProducer, "nr", 4, true));
      sTableProducer.addColumn(new Column(sTableProducer, "label", 12));
      sTableProducer.addColumn(new Column(sTableProducer, "comment", 12));
      sTableProducer.addColumn(new Column(sTableProducer, "homepage", 12));
      sTableProducer.addColumn(new Column(sTableProducer, "country", 1));
      sTableProducer.addColumn(new Column(sTableProducer, "publisher", 4));
      sTableProducer.addColumn(new Column(sTableProducer, "publishDate", 91));
   }

   private static Table sTableProduct = new Table(DEFAULT_SCHEMA, "product");
   static {
      sTableProduct.addColumn(new Column(sTableProduct, "nr", 4, true));
      sTableProduct.addColumn(new Column(sTableProduct, "label", 12));
      sTableProduct.addColumn(new Column(sTableProduct, "comment", 12));
      sTableProduct.addColumn(new Column(sTableProduct, "producer", 4));
      sTableProduct.addColumn(new Column(sTableProduct, "propertyNum1", 4));
      sTableProduct.addColumn(new Column(sTableProduct, "propertyNum2", 4));
      sTableProduct.addColumn(new Column(sTableProduct, "propertyNum3", 4));
      sTableProduct.addColumn(new Column(sTableProduct, "propertyNum4", 4));
      sTableProduct.addColumn(new Column(sTableProduct, "propertyNum5", 4));
      sTableProduct.addColumn(new Column(sTableProduct, "propertyNum6", 4));
      sTableProduct.addColumn(new Column(sTableProduct, "propertyTex1", 12));
      sTableProduct.addColumn(new Column(sTableProduct, "propertyTex2", 12));
      sTableProduct.addColumn(new Column(sTableProduct, "propertyTex3", 12));
      sTableProduct.addColumn(new Column(sTableProduct, "propertyTex4", 12));
      sTableProduct.addColumn(new Column(sTableProduct, "propertyTex5", 12));
      sTableProduct.addColumn(new Column(sTableProduct, "propertyTex6", 12));
      sTableProduct.addColumn(new Column(sTableProduct, "publisher", 4));
      sTableProduct.addColumn(new Column(sTableProduct, "publishDate", 91));
   }

   private static Table sTableProductFeature = new Table(DEFAULT_SCHEMA, "productfeature");
   static {
      sTableProductFeature.addColumn(new Column(sTableProductFeature, "nr", 4, true));
      sTableProductFeature.addColumn(new Column(sTableProductFeature, "label", 12));
      sTableProductFeature.addColumn(new Column(sTableProductFeature, "comment", 12));
      sTableProductFeature.addColumn(new Column(sTableProductFeature, "publisher", 4));
      sTableProductFeature.addColumn(new Column(sTableProductFeature, "publishDate", 91));
   }

   private static Table sTableProductFeatureProduct = new Table(DEFAULT_SCHEMA, "productfeatureproduct");
   static {
      sTableProductFeatureProduct.addColumn(new Column(sTableProductFeatureProduct, "product", 4));
      sTableProductFeatureProduct.addColumn(new Column(sTableProductFeatureProduct, "productFeature", 4));
   }

   private static Table sTableProductType = new Table(DEFAULT_SCHEMA, "producttype");
   static {
      sTableProductType.addColumn(new Column(sTableProductType, "nr", 4, true));
      sTableProductType.addColumn(new Column(sTableProductType, "label", 12));
      sTableProductType.addColumn(new Column(sTableProductType, "comment", 12));
      sTableProductType.addColumn(new Column(sTableProductType, "parent", 4));
      sTableProductType.addColumn(new Column(sTableProductType, "publisher", 4));
      sTableProductType.addColumn(new Column(sTableProductType, "publishDate", 91));
   }

   private static Table sTableProductTypeProduct = new Table(DEFAULT_SCHEMA, "producttypeproduct");
   static {
      sTableProductTypeProduct.addColumn(new Column(sTableProductTypeProduct, "product", 4));
      sTableProductTypeProduct.addColumn(new Column(sTableProductTypeProduct, "producttype", 4));
   }

   private static Table sTableReview = new Table(DEFAULT_SCHEMA, "review");
   static {
      sTableReview.addColumn(new Column(sTableReview, "nr", 4, true));
      sTableReview.addColumn(new Column(sTableReview, "product", 4));
      sTableReview.addColumn(new Column(sTableReview, "producer", 4));
      sTableReview.addColumn(new Column(sTableReview, "person", 4));
      sTableReview.addColumn(new Column(sTableReview, "reviewDate", 93));
      sTableReview.addColumn(new Column(sTableReview, "title", 12));
      sTableReview.addColumn(new Column(sTableReview, "text", -1));
      sTableReview.addColumn(new Column(sTableReview, "language", 1));
      sTableReview.addColumn(new Column(sTableReview, "rating1", 4));
      sTableReview.addColumn(new Column(sTableReview, "rating2", 4));
      sTableReview.addColumn(new Column(sTableReview, "rating3", 4));
      sTableReview.addColumn(new Column(sTableReview, "rating4", 4));
      sTableReview.addColumn(new Column(sTableReview, "publisher", 4));
      sTableReview.addColumn(new Column(sTableReview, "publishDate", 91));
   }

   private static Table sTableVendor = new Table(DEFAULT_SCHEMA, "vendor");
   static {
      sTableVendor.addColumn(new Column(sTableVendor, "nr", 4, true));
      sTableVendor.addColumn(new Column(sTableVendor, "label", 12));
      sTableVendor.addColumn(new Column(sTableVendor, "comment", 12));
      sTableVendor.addColumn(new Column(sTableVendor, "homepage", 12));
      sTableVendor.addColumn(new Column(sTableVendor, "country", 1));
      sTableVendor.addColumn(new Column(sTableVendor, "publisher", 4));
      sTableVendor.addColumn(new Column(sTableVendor, "publishDate", 91));
   }

   private static ForeignKey sForeignKeyProduct = new ForeignKey(sTableProduct, "product_fk1");
   static {
      sForeignKeyProduct.addReference(0, sTableProducer.getColumn("nr"), sTableProduct.getColumn("producer"));
   }

   private static ForeignKey sForeignKeyOffer1 = new ForeignKey(sTableOffer, "offer_fk1");
   static {
      sForeignKeyOffer1.addReference(0, sTableProduct.getColumn("nr"), sTableOffer.getColumn("product"));
   }

   private static ForeignKey sForeignKeyOffer2 = new ForeignKey(sTableOffer, "offer_fk2");
   static {
      sForeignKeyOffer2.addReference(0, sTableProducer.getColumn("nr"), sTableOffer.getColumn("producer"));
   }

   private static ForeignKey sForeignKeyOffer3 = new ForeignKey(sTableOffer, "offer_fk3");
   static {
      sForeignKeyOffer3.addReference(0, sTableVendor.getColumn("nr"), sTableOffer.getColumn("vendor"));
   }

   private static ForeignKey sForeignKeyProductFeatureProduct1 = new ForeignKey(sTableProductFeatureProduct, "pfp_fk1");
   static {
      sForeignKeyProductFeatureProduct1.addReference(0, sTableProduct.getColumn("nr"), sTableProductFeatureProduct.getColumn("product"));
   }

   private static ForeignKey sForeignKeyProductFeatureProduct2 = new ForeignKey(sTableProductFeatureProduct, "pfp_fk2");
   static {
      sForeignKeyProductFeatureProduct2.addReference(0, sTableProductFeature.getColumn("nr"), sTableProductFeatureProduct.getColumn("productFeature"));
   }

   private static ForeignKey sForeignKeyProductTypeProduct1 = new ForeignKey(sTableProductTypeProduct, "ptp_fk1");
   static {
      sForeignKeyProductTypeProduct1.addReference(0, sTableProductType.getColumn("nr"), sTableProductTypeProduct.getColumn("producttype"));
   }

   private static ForeignKey sForeignKeyProductTypeProduct2 = new ForeignKey(sTableProductTypeProduct, "ptp_fk2");
   static {
      sForeignKeyProductTypeProduct2.addReference(0, sTableProduct.getColumn("nr"), sTableProductTypeProduct.getColumn("product"));
   }

   private static ForeignKey sForeignKeyReview1 = new ForeignKey(sTableReview, "review_fk1");
   static {
      sForeignKeyReview1.addReference(0, sTablePerson.getColumn("nr"), sTableReview.getColumn("person"));
   }

   private static ForeignKey sForeignKeyReview2 = new ForeignKey(sTableReview, "review_fk2");
   static {
      sForeignKeyReview2.addReference(0, sTableProducer.getColumn("nr"), sTableReview.getColumn("producer"));
   }

   private static ForeignKey sForeignKeyReview3 = new ForeignKey(sTableReview, "review_fk3");
   static {
      sForeignKeyReview3.addReference(0, sTableProduct.getColumn("nr"), sTableReview.getColumn("product"));
   }

   public BerlinBenchmarkDatabase()
   {
      mTableDictionary.put(sTableOffer.getLocalName(), sTableOffer);
      mTableDictionary.put(sTableOffer.getFullName(), sTableOffer);

      mTableDictionary.put(sTablePerson.getLocalName(), sTablePerson);
      mTableDictionary.put(sTablePerson.getFullName(), sTablePerson);

      mTableDictionary.put(sTableProducer.getLocalName(), sTableProducer);
      mTableDictionary.put(sTableProducer.getFullName(), sTableProducer);

      mTableDictionary.put(sTableProduct.getLocalName(), sTableProduct);
      mTableDictionary.put(sTableProduct.getFullName(), sTableProduct);

      mTableDictionary.put(sTableProductFeature.getLocalName(), sTableProductFeature);
      mTableDictionary.put(sTableProductFeature.getFullName(), sTableProductFeature);

      mTableDictionary.put(sTableProductFeatureProduct.getLocalName(), sTableProductFeatureProduct);
      mTableDictionary.put(sTableProductFeatureProduct.getFullName(), sTableProductFeatureProduct);

      mTableDictionary.put(sTableProductType.getLocalName(), sTableProductType);
      mTableDictionary.put(sTableProductType.getFullName(), sTableProductType);

      mTableDictionary.put(sTableProductTypeProduct.getLocalName(), sTableProductTypeProduct);
      mTableDictionary.put(sTableProductTypeProduct.getFullName(), sTableProductTypeProduct);

      mTableDictionary.put(sTableReview.getLocalName(), sTableReview);
      mTableDictionary.put(sTableReview.getFullName(), sTableReview);

      mTableDictionary.put(sTableVendor.getLocalName(), sTableVendor);
      mTableDictionary.put(sTableVendor.getFullName(), sTableVendor);

      /*
       * Foreign keys
       */
      mForeignKeySet.add(sForeignKeyProduct);
      mForeignKeySet.add(sForeignKeyOffer1);
      mForeignKeySet.add(sForeignKeyOffer2);
      mForeignKeySet.add(sForeignKeyOffer3);
      mForeignKeySet.add(sForeignKeyProductFeatureProduct1);
      mForeignKeySet.add(sForeignKeyProductFeatureProduct2);
      mForeignKeySet.add(sForeignKeyProductTypeProduct1);
      mForeignKeySet.add(sForeignKeyProductTypeProduct2);
      mForeignKeySet.add(sForeignKeyReview1);
      mForeignKeySet.add(sForeignKeyReview2);
      mForeignKeySet.add(sForeignKeyReview3);
   }

   public ITable getTable(String tableName)
   {
      ITable table = mTableDictionary.get(tableName);
      if (table != null) {
         return table;
      }
      throw new TableNotFoundException(tableName);
   }

   public Set<ITable> getTables()
   {
      return new HashSet<ITable>(mTableDictionary.values());
   }

   public Set<IForeignKey> getForeignKeys()
   {
      return mForeignKeySet;
   }
}
