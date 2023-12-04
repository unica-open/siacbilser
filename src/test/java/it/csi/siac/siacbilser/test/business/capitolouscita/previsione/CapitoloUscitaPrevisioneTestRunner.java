/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitolouscita.previsione;


import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

// TODO: Auto-generated Javadoc
/**
 * The Class CapitoloUscitaPrevisioneTestRunner.
 */
public class CapitoloUscitaPrevisioneTestRunner {
   
   /**
    * The main method.
    *
    * @param args the arguments
    */
   public static void main(String[] args) {
      Result result = JUnitCore.runClasses(CapitoloUscitaPrevisioneTestSuite.class);
      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
      System.out.println(result.wasSuccessful());
   }
}  
