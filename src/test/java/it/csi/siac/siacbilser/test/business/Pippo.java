/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

// TODO: Auto-generated Javadoc
/**
 * The Class Pippo.
 */
public class Pippo {
	
	/**
	 * Dummy method.
	 *
	 * @param i the i
	 * @param strings the strings
	 */
	public void dummyMethod(Integer i, String...strings ) {		
		System.out.println("i + strings: "+ToStringBuilder.reflectionToString(strings,ToStringStyle.SIMPLE_STYLE));
		
		for(String string: strings){
			System.out.println("string: "+string);
		}

	}
	
	/**
	 * Dummy method.
	 *
	 * @param i the i
	 */
	public void dummyMethod(Integer i) {
		System.out.println("i");

	}
		
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		Pippo p = new Pippo();
		p.dummyMethod(null,null);
		p.dummyMethod(null,"COD001","COD002","COD003");
		
	}
	

}
