/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.util;

import java.math.BigDecimal;
/*
 *  utility per verificare esistenza e grandezza di BigDecimal
 */
public class NumericUtils {
	
	public static BigDecimal getZeroIfNull(BigDecimal bd){
		if(bd!=null){
			return bd;
		} else {
			return BigDecimal.ZERO;
		}
	}
	
	public static boolean maggioreDiZero(BigDecimal bd){
		if(bd.compareTo(BigDecimal.ZERO)>0){
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean valorizzatoEMaggioreDiZero(Number bd){
		if(bd !=null && bd.intValue()>0){
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean nulloOMinoreUgualeAZero(Number bd){
		if(bd == null || bd.intValue()<=0){
			return true;
		} else {
			return false;
		}
	}

}
