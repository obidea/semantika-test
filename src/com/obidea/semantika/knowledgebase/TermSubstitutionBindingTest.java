package com.obidea.semantika.knowledgebase;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import com.obidea.semantika.expression.ExpressionObjectFactory;
import com.obidea.semantika.expression.base.Function;
import com.obidea.semantika.expression.base.Literal;
import com.obidea.semantika.expression.base.Variable;
import com.obidea.semantika.util.LogUtils;

public class TermSubstitutionBindingTest
{
   private static ExpressionObjectFactory sExpressionFactory = ExpressionObjectFactory.getInstance();

   private Variable x = sExpressionFactory.getVariable("x"); //$NON-NLS-1$
   private Variable y = sExpressionFactory.getVariable("y"); //$NON-NLS-1$
   private Variable z = sExpressionFactory.getVariable("z"); //$NON-NLS-1$
   private Literal a = sExpressionFactory.getLiteral("a"); //$NON-NLS-1$
   private Literal b = sExpressionFactory.getLiteral("b"); //$NON-NLS-1$
   private Function fy = new Function("f", y); //$NON-NLS-1$
   private Function hy = new Function("h", y); //$NON-NLS-1$
   private Function ga = new Function("g", a); //$NON-NLS-1$

   private TermSubstitutionBinding subs0;
   private TermSubstitutionBinding subs1;
   private TermSubstitutionBinding subs2;
   private TermSubstitutionBinding subs3;
   private TermSubstitutionBinding subs4;
   private TermSubstitutionBinding subs5;
   private TermSubstitutionBinding subs6;

   private static final Logger LOG = LogUtils.createLogger("semantika.test"); //$NON-NLS-1$

   @Before
   public void setUp()
   {
      subs0 = new TermSubstitutionBinding();
      
      subs1 = new TermSubstitutionBinding();
      subs1.put(z, a);
      
      subs2 = new TermSubstitutionBinding();
      subs2.put(x, hy);
      
      subs3 = new TermSubstitutionBinding();
      subs3.put(z, a);
      subs3.put(x, hy);
      
      subs4 = new TermSubstitutionBinding();
      subs4.put(y, ga);

      subs5 = new TermSubstitutionBinding();
      subs5.put(x, fy);
      subs5.put(y, z);
      
      subs6 = new TermSubstitutionBinding();
      subs6.put(x, a);
      subs6.put(y, b);
      subs6.put(z, y);
   }

   @Test
   public void compose_1()
   {
      print("theta", subs1); //$NON-NLS-1$
      print("sigma", subs2); //$NON-NLS-1$
      subs1.compose(subs2);
      print("theta-sigma", subs1); //$NON-NLS-1$
      assertComposition(subs1, "{x/h(y), z/\"a\"}"); //$NON-NLS-1$
   }

   @Test
   public void compose_2()
   {
      print("theta", subs3); //$NON-NLS-1$
      print("sigma", subs4); //$NON-NLS-1$
      subs3.compose(subs4);
      print("theta-sigma", subs3); //$NON-NLS-1$
      assertComposition(subs3, "{y/g(\"a\"), x/h(g(\"a\")), z/\"a\"}"); //$NON-NLS-1$
   }

   @Test
   public void compose_3()
   {
      print("theta", subs5); //$NON-NLS-1$
      print("sigma", subs6); //$NON-NLS-1$
      subs5.compose(subs6);
      print("theta-sigma", subs5); //$NON-NLS-1$
      assertComposition(subs5, "{x/f(\"b\"), z/y}"); //$NON-NLS-1$
   }

   @Test
   public void compose_4()
   {
      print("theta", subs0); //$NON-NLS-1$
      print("sigma", subs3); //$NON-NLS-1$
      subs0.compose(subs3);
      print("theta-sigma", subs0); //$NON-NLS-1$
      assertComposition(subs0, "{x/h(y), z/\"a\"}"); //$NON-NLS-1$
   }

   @Test
   public void compose_5()
   {
      print("theta", subs3); //$NON-NLS-1$
      print("sigma", subs0); //$NON-NLS-1$
      subs3.compose(subs0);
      print("theta-sigma", subs3); //$NON-NLS-1$
      assertComposition(subs3, "{x/h(y), z/\"a\"}"); //$NON-NLS-1$
   }

   private void assertComposition(TermSubstitutionBinding subs, String expected)
   {
      assertEquals(expected, subs.toString());
   }

   private void print(String name, TermSubstitutionBinding binding)
   {
      LOG.info("{} = {}", name, binding.toString()); //$NON-NLS-1$
   }
}
