/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.entitymapping;

import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacintegser.model.enumeration.SiNoEnum;
import it.csi.siac.siacintegser.model.enumeration.SiNoIndifferenteEnum;

public abstract class AbstractIntegToFinConverter {

	/**
	 * Data la enum SI, NO, INDIFFERENTE 
	 * converte in true,false,null
	 * @param siNoIndifferente
	 * @return
	 */
	public static String siNoIndifferenteToStringTrueFalse(SiNoIndifferenteEnum siNoIndifferente){
		String stringaTrueFalse = null;
		if(siNoIndifferente!=null){
			if(siNoIndifferente.equals(SiNoIndifferenteEnum.SI)){
				stringaTrueFalse = CostantiFin.TRUE;
			} else if (siNoIndifferente.equals(SiNoIndifferenteEnum.NO)){
				stringaTrueFalse = CostantiFin.FALSE;
			} else if (siNoIndifferente.equals(SiNoIndifferenteEnum.INDIFFERENTE)){
				stringaTrueFalse = null;
			}
		} else {
			stringaTrueFalse = null;
		}
		return stringaTrueFalse;
	}
	
	public static String siNoToStringTrueFalse(SiNoEnum siNo){
		String stringaTrueFalse = null;
		if(SiNoEnum.SI.equals(siNo)){
			stringaTrueFalse = CostantiFin.TRUE;
		} else if (SiNoEnum.NO.equals(siNo)){
			stringaTrueFalse = CostantiFin.FALSE;
		}
		return stringaTrueFalse;
	}
	
}
