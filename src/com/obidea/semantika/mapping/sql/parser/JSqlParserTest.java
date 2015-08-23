package com.obidea.semantika.mapping.sql.parser;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;

import com.obidea.semantika.database.BerlinBenchmarkDatabaseMetadata;
import com.obidea.semantika.database.IDatabaseMetadata;
import com.obidea.semantika.database.sql.base.ISqlQuery;
import com.obidea.semantika.database.sql.parser.JSqlParser;
import com.obidea.semantika.database.sql.parser.SqlParserException;
import com.obidea.semantika.util.LogUtils;
import com.obidea.semantika.util.PrinterUtils;

public class JSqlParserTest
{
   private static JSqlParser mParser = new JSqlParser();
   private static IDatabaseMetadata mDatabaseMetadata = new BerlinBenchmarkDatabaseMetadata();

   private static final Logger LOG = LogUtils.createLogger("semantika.test"); //$NON-NLS-1$

   @Rule
   public ExpectedException mExpectedException = ExpectedException.none();

   @Test
   public void testSelectOneColumn() throws SqlParserException
   {
      final String sql = "SELECT nr FROM offer";
      assertParserSupport("Select 1 column", sql);
   }

   @Test
   public void testSelectMultipleColumns() throws SqlParserException
   {
      final String sql = "SELECT nr, product, producer, vendor FROM offer";
      assertParserSupport("Select multiple columns", sql);
   }

   @Test
   public void testSelectAllColumns() throws SqlParserException
   {
      final String sql = "SELECT * FROM offer";
      assertParserSupport("Select all columns", sql);
   }

   @Test
   public void testSelectAllTableColumns() throws SqlParserException
   {
      final String sql =
            "SELECT offer.* \n" +
            "FROM product\n" + 
            "JOIN offer ON product.nr = offer.product";
      assertParserSupport("Select all table columns", sql);
   }

   @Test
   public void testSelectWithExpressionArithmetic() throws SqlParserException
   {
      final String sql = 
            "SELECT (((rating1 * 1.5) + (rating2 * 1.75) + (rating3 * 2) - rating4) / 4) AS averageRating \n" +
            "FROM review";
      assertParserSupport("Select with column expression: ADD, SUB, MUL, DIV", sql);
   }

   @Test
   public void testSelectWithExpressionConcat() throws SqlParserException
   {
      final String sql =
            "SELECT nr, ('product/' || label || nr) as productCatalog \n" +
            "FROM product";
      assertParserSupport("Select with column expression: CONCAT", sql);
   }

   @Test
   public void testSelectWithCondition() throws SqlParserException
   {
      final String sql = 
            "SELECT nr, product, producer, vendor \n" +
            "FROM offer \n" +
            "WHERE vendor = 'XXX' AND product <> 'YYY'";
      assertParserSupport("Select with condition", sql);
   }

   @Test
   public void testSelectWithMoreConditions() throws SqlParserException
   {
      final String sql = 
            "SELECT nr, product, producer, vendor \n" +
            "FROM offer \n" +
            "WHERE (producer <> 'AAA' OR (vendor = 'XXX' AND product <> 'YYY')) AND price > 1000000";
      assertParserSupport("Select with more conditions", sql);
   }

   @Test
   public void testSelectWithSimpleJoin() throws SqlParserException
   {
      final String sql =
            "SELECT label \n" +
            "FROM product, offer \n" +
            "WHERE product.nr = product";
      assertParserSupport("Select with join tables", sql);
   }

   @Test
   public void testSelectWithJoin() throws SqlParserException
   {
      final String sql =
            "SELECT label \n" +
            "FROM product JOIN offer \n" +
            "ON product.nr = product";
      assertParserSupport("Select with JOIN keyword", sql);
   }

   @Test
   public void testSelectWithInnerJoin() throws SqlParserException
   {
      final String sql =
            "SELECT label \n" +
            "FROM product INNER JOIN offer \n" +
            "ON product.nr = product";
      assertParserSupport("Select with INNER JOIN keyword", sql);
   }

   @Test
   public void testSelectWithJoinAndConditions() throws SqlParserException
   {
      final String sql =
            "SELECT label \n" +
            "FROM product JOIN offer \n" +
            "ON product.nr = product \n" +
            "WHERE price > 500 AND product.producer = 'XXX'";
      assertParserSupport("Select with JOIN keyword", sql);
   }

   @Test
   public void testSelectWithLeftJoin() throws SqlParserException // expected to fail
   {
      final String sql =
            "SELECT label \n" +
            "FROM product LEFT JOIN offer \n" +
            "ON product.nr = product";
      assertParserSupport("Select with LEFT JOIN keyword", sql);
   }

   @Test
   public void testSelectWithJoinThreeTables() throws SqlParserException
   {
      final String sql =
            "SELECT product.label as labelProduct, producer.label as labelProducer \n" +
            "FROM product\n" +
            "JOIN producer ON product.producer = producer.nr\n" +
            "JOIN offer ON producer.nr = offer.producer";
      assertParserSupport("Select with join 3 tables", sql);
   }

   @Test
   public void testSelectWithSelfJoin() throws SqlParserException // expected to fail
   {
      final String sql =
            "SELECT r1.nr, r1.title \n" +
            "FROM review r1, review r2 \n" +
            "WHERE r1.producer = r2.person";
      assertParserSupport("Select with join self join", sql);
   }

   /*
    * Private methods for printing the results
    */

   private void assertParserSupport(String testId, String sql) throws SqlParserException
   {
      StringBuilder sb = new StringBuilder();
      sb.append("\n\n" + testId);
      sb.append("\n====================================================================================================================================\n");
      sb.append(sql);
      sb.append("\n------------------------------------------------------------------------------------------------------------------------------------\n");
      ISqlQuery query = mParser.parse(sql, mDatabaseMetadata); //$NON-NLS-1$
      sb.append(PrinterUtils.print(query));
      sb.append("\n====================================================================================================================================\n");
      LOG.info(sb.toString());
   }
}
