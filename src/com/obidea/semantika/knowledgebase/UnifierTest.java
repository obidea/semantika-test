package com.obidea.semantika.knowledgebase;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;

import com.obidea.semantika.expression.ExpressionObjectFactory;
import com.obidea.semantika.expression.base.Atom;
import com.obidea.semantika.expression.base.Function;
import com.obidea.semantika.expression.base.IExpressionObject;
import com.obidea.semantika.expression.base.ITerm;
import com.obidea.semantika.expression.base.Literal;
import com.obidea.semantika.expression.base.Term;
import com.obidea.semantika.expression.base.TermUtils;
import com.obidea.semantika.expression.base.Variable;
import com.obidea.semantika.util.LogUtils;

public class UnifierTest
{
   private ExpressionObjectFactory sExpressionFactory = ExpressionObjectFactory.getInstance();

   private Variable x = sExpressionFactory.getVariable("x");
   private Variable y = sExpressionFactory.getVariable("y");
   private Variable z = sExpressionFactory.getVariable("z");
   private Literal a = sExpressionFactory.getLiteral("a");
   private Literal b = sExpressionFactory.getLiteral("b");
   private Function fx = new Function("f", x);
   private Function fy = new Function("f", y);
   private Function fa = new Function("f", a);
   private Function ffx = new Function("f", fx);
   private Function gx = new Function("g", x);
   private Function gz = new Function("g", z);
   private Function gxy = new Function("g", x , y);
   private Function gxb = new Function("g", x , b);
   private Function gfyb = new Function("g", fy , b);
   private Function hy = new Function("h", y);
   private Function hgz = new Function("h", gz);
   private Function pfagx = new Function("p", fa, gx);
   private Function pyy = new Function("p", y, y);
   private Function paxhgz = new Function("p", a, x, hgz);
   private Function pzhyhy = new Function("p", z, hy, hy);
   private Atom Ax = sExpressionFactory.createAtom("A", x);
   private Atom Ay = sExpressionFactory.createAtom("A", y);
   private Atom Aa = sExpressionFactory.createAtom("A", a);
   private Atom Afx = sExpressionFactory.createAtom("A", fx);
   private Atom Bxy = sExpressionFactory.createAtom("B", x, y);
   private Atom Bxb = sExpressionFactory.createAtom("B", x, b);
   private Atom Bfyb = sExpressionFactory.createAtom("B", fy, b);
   private Atom Pfagx = sExpressionFactory.createAtom("P", fa, gx);
   private Atom Pyy = sExpressionFactory.createAtom("P", y, y);
   private Atom Paxhgz = sExpressionFactory.createAtom("P", a, x, hgz);
   private Atom Pzhyhy = sExpressionFactory.createAtom("P", z, hy, hy);

   private static final Logger LOG = LogUtils.createLogger("semantika.test"); //$NON-NLS-1$

   @Rule
   public ExpectedException mExpectedException = ExpectedException.none();

   @Test
   public void testUnifier_x_x() throws UnificationException
   {
      TermSubstitutionBinding theta = new TermSubstitutionBinding();
      Unifier.findSubstitution(x, x, theta);
      printSubstitution(x, x, theta);
      assertTheta(theta, "{}");
   }

   @Test
   public void testUnifier_x_y() throws UnificationException
   {
      TermSubstitutionBinding theta = new TermSubstitutionBinding();
      Unifier.findSubstitution(x, y, theta);
      printSubstitution(x, y, theta);
      assertTheta(theta, "{y/x}");
   }

   @Test
   public void testUnifier_a_y() throws UnificationException
   {
      TermSubstitutionBinding theta = new TermSubstitutionBinding();
      Unifier.findSubstitution(a, y, theta);
      printSubstitution(a, y, theta);
      assertTheta(theta, "{y/\"a\"}");
   }

   @Test
   public void testUnifier_fx_y() throws UnificationException
   {
      TermSubstitutionBinding theta = new TermSubstitutionBinding();
      Unifier.findSubstitution(fx, y, theta);
      printSubstitution(fx, y, theta);
      assertTheta(theta, "{y/f(x)}");
   }

   @Test
   public void testUnifier_x_a() throws UnificationException
   {
      TermSubstitutionBinding theta = new TermSubstitutionBinding();
      Unifier.findSubstitution(x, a, theta);
      printSubstitution(x, a, theta);
      assertTheta(theta, "{x/\"a\"}");
   }

   @Test
   public void testUnifier_x_fx() throws UnificationException
   {
      TermSubstitutionBinding theta = new TermSubstitutionBinding();
      Unifier.findSubstitution(x, fx, theta);
      printSubstitution(x, fx, theta);
      assertTheta(theta, "{x/f(x)}");
   }

   @Test
   public void testUnifier_a_a() throws UnificationException
   {
      TermSubstitutionBinding theta = new TermSubstitutionBinding();
      Unifier.findSubstitution(a, a, theta);
      printSubstitution(a, a, theta);
      assertTheta(theta, "{}");
   }

   /*
    * Should fail due to constants are not equal
    */
   @Test
   public void testUnifier_a_b() throws UnificationException
   {
      mExpectedException.expect(UnificationException.class);
      TermSubstitutionBinding theta = new TermSubstitutionBinding();
      Unifier.findSubstitution(a, b, theta);
      printSubstitution(a, b, theta);
   }

   @Test
   public void testUnifier_fx_fx() throws UnificationException
   {
      TermSubstitutionBinding theta = new TermSubstitutionBinding();
      Unifier.findSubstitution(fx, fx, theta);
      printSubstitution(fx, fx, theta);
      assertTheta(theta, "{}");
   }

   @Test
   public void testUnifier_fx_fy() throws UnificationException
   {
      TermSubstitutionBinding theta = new TermSubstitutionBinding();
      Unifier.findSubstitution(fx, fy, theta);
      printSubstitution(fx, fy, theta);
      assertTheta(theta, "{y/x}");
   }

   @Test
   public void testUnifier_fx_fa() throws UnificationException
   {
      TermSubstitutionBinding theta = new TermSubstitutionBinding();
      Unifier.findSubstitution(fx, fa, theta);
      printSubstitution(fx, fa, theta);
      assertTheta(theta, "{x/\"a\"}");
   }

   /*
    * Should fail due to infinite loop in unification.
    */
   @Test
   public void testUnifier_fx_ffx() throws UnificationException
   {
      mExpectedException.expect(UnificationException.class);
      TermSubstitutionBinding theta = new TermSubstitutionBinding();
      Unifier.findSubstitution(fx, ffx, theta);
      printSubstitution(fx, ffx, theta);
   }

   @Test
   public void testUnifier_gxy_gxy() throws UnificationException
   {
      TermSubstitutionBinding theta = new TermSubstitutionBinding();
      Unifier.findSubstitution(gxy, gxy, theta);
      printSubstitution(gxy, gxy, theta);
      assertTheta(theta, "{}");
   }

   @Test
   public void testUnifier_gxy_gxb() throws UnificationException
   {
      TermSubstitutionBinding theta = new TermSubstitutionBinding();
      Unifier.findSubstitution(gxy, gxb, theta);
      printSubstitution(gxy, gxb, theta);
      assertTheta(theta, "{y/\"b\"}");
   }

   @Test
   public void testUnifier_gxy_gfxb() throws UnificationException
   {
      TermSubstitutionBinding theta = new TermSubstitutionBinding();
      Unifier.findSubstitution(gxy, gfyb, theta);
      printSubstitution(gxy, gfyb, theta);
      assertTheta(theta, "{y/\"b\", x/f(\"b\")}");
   }

   @Test
   public void testUnifier_gxb_gfxb() throws UnificationException
   {
      TermSubstitutionBinding theta = new TermSubstitutionBinding();
      Unifier.findSubstitution(gxb, gfyb, theta);
      printSubstitution(gxb, gfyb, theta);
      assertTheta(theta, "{x/f(y)}");
   }

   /*
    * Should fail due to signatures are not equal
    */
   @Test
   public void testUnifier_pfagx_pyy() throws UnificationException 
   {
      mExpectedException.expect(UnificationException.class);
      TermSubstitutionBinding theta = new TermSubstitutionBinding();
      Unifier.findSubstitution(pfagx, pyy, theta);
      printSubstitution(pfagx, pyy, theta);
   }

   @Test
   public void testUnifier_paxhgz_pzhyhy() throws UnificationException
   {
      TermSubstitutionBinding theta = new TermSubstitutionBinding();
      Unifier.findSubstitution(paxhgz, pzhyhy, theta);
      printSubstitution(paxhgz, pzhyhy, theta);
      assertTheta(theta, "{y/g(\"a\"), x/h(g(\"a\")), z/\"a\"}");
   }

   @Test
   public void testUnifier_Ax_Ax() throws UnificationException
   {
      TermSubstitutionBinding theta = new TermSubstitutionBinding();
      Unifier.findSubstitution(Ax, Ax, theta);
      printSubstitution(Ax, Ax, theta);
      assertTheta(theta, "{}");
   }

   @Test
   public void testUnifier_Ax_Ay() throws UnificationException
   {
      TermSubstitutionBinding theta = new TermSubstitutionBinding();
      Unifier.findSubstitution(Ax, Ay, theta);
      printSubstitution(Ax, Ay, theta);
      assertTheta(theta, "{y/x}");
   }

   @Test
   public void testUnifier_Ax_Aa() throws UnificationException
   {
      TermSubstitutionBinding theta = new TermSubstitutionBinding();
      Unifier.findSubstitution(Ax, Aa, theta);
      printSubstitution(Ax, Aa, theta);
      assertTheta(theta, "{x/\"a\"}");
   }

   /*
    * Should fail due to infinite loop in unification.
    */
   @Test
   public void testUnifier_Ax_Afx() throws UnificationException
   {
      mExpectedException.expect(UnificationException.class);
      TermSubstitutionBinding theta = new TermSubstitutionBinding();
      Unifier.findSubstitution(Ax, Afx, theta);
      printSubstitution(Ax, Afx, theta);
      assertTheta(theta, "{x/f(x)}");
   }

   @Test
   public void testUnifier_Bxy_Bxy() throws UnificationException
   {
      TermSubstitutionBinding theta = new TermSubstitutionBinding();
      Unifier.findSubstitution(Bxy, Bxy, theta);
      printSubstitution(Bxy, Bxy, theta);
      assertTheta(theta, "{}");
   }

   @Test
   public void testUnifier_Bxy_Bxb() throws UnificationException
   {
      TermSubstitutionBinding theta = new TermSubstitutionBinding();
      Unifier.findSubstitution(Bxy, Bxb, theta);
      printSubstitution(Bxy, Bxb, theta);
      assertTheta(theta, "{y/\"b\"}");
   }

   @Test
   public void testUnifier_Bxy_Bfxb() throws UnificationException
   {
      TermSubstitutionBinding theta = new TermSubstitutionBinding();
      Unifier.findSubstitution(Bxy, Bfyb, theta);
      printSubstitution(Bxy, Bfyb, theta);
      assertTheta(theta, "{y/\"b\", x/f(\"b\")}");
   }

   @Test
   public void testUnifier_Bx_Bfxb() throws UnificationException
   {
      TermSubstitutionBinding theta = new TermSubstitutionBinding();
      Unifier.findSubstitution(Bxb, Bfyb, theta);
      printSubstitution(Bxb, Bfyb, theta);
      assertTheta(theta, "{x/f(y)}");
   }

   /*
    * Should fail due to signatures are not equal
    */
   @Test
   public void testUnifier_Pfagx_Pyy() throws UnificationException
   {
      mExpectedException.expect(UnificationException.class);
      TermSubstitutionBinding theta = new TermSubstitutionBinding();
      Unifier.findSubstitution(Pfagx, Pyy, theta);
      printSubstitution(Pfagx, Pyy, theta);
   }

   @Test
   public void testUnifier_Paxhgz_Pzhyhy() throws UnificationException
   {
      TermSubstitutionBinding theta = new TermSubstitutionBinding();
      Unifier.findSubstitution(Paxhgz, Pzhyhy, theta);
      printSubstitution(Paxhgz, Pzhyhy, theta);
      assertTheta(theta, "{y/g(\"a\"), x/h(g(\"a\")), z/\"a\"}");
   }

   private void assertTheta(TermSubstitutionBinding theta, String expected)
   {
      assertEquals(expected, theta.toString());
   }

   private void printSubstitution(IExpressionObject o1, IExpressionObject o2, TermSubstitutionBinding theta)
   {
      StringBuilder sb = new StringBuilder();
      sb.append("S = {");
      sb.append(printObject(o1));
      sb.append(", ");
      sb.append(printObject(o2));
      sb.append("}\n");
      sb.append("theta = ");
      sb.append(theta);
      sb.append("\n");
      LOG.info(sb.toString());
   }

   private String printObject(IExpressionObject expr)
   {
      StringBuilder sb = new StringBuilder();
      if (expr instanceof Atom) {
         printAtom((Atom) expr, sb);
      }
      else if (expr instanceof Term) {
         sb.append(TermUtils.toString((Term) expr));
      }
      return sb.toString();
   }
   
   private void printAtom(Atom atom, StringBuilder sb)
   {
      sb.append(atom.getPredicate());
      sb.append("(");
      boolean needComma = false;
      for (ITerm term : atom.getTerms()) {
         if (needComma) {
            sb.append(", ");
         }
         sb.append(TermUtils.toString(term));
         needComma = true;
      }
      sb.append(")");
   }
}
