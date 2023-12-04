/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.util;

import java.util.Date;
import java.util.Map;

import it.csi.siac.siacfinser.StringUtilsFin;
import it.csi.siac.siacfinser.TimingUtils;

public class DataValiditaUtil {
	
	
	//costruzione clausola sulla validita degli oggetti nelle query:
	
	public static String NOW_DATE_PARAM_JPQL = "nowDate";
	
	/**
	 * Utile nella composizione di query per evitare di aggiungerlo piu' di una volta, ma quando non e' possibile sapere
	 * a priopri se il codice lo dovra' aggiungere zero volte, una o enne..
	 * @param param
	 */
	public static void aggiungiParametroDataValidita(Map<String, Object> param){
		Date nowDate = TimingUtils.getNowDate();
		if(param!=null && !param.containsKey(NOW_DATE_PARAM_JPQL)){
			param.put(NOW_DATE_PARAM_JPQL, nowDate);
		}
	}
	
	
	/**
	 *  costruisce la condizione di validita alla data indicata con nomeParametroDate di DEFAULT -  considera data cancellazione
	 * @param aliasOggetto
	 * @return
	 */
	public static String validitaForQuery(String aliasOggetto){
		return validitaForQuery(aliasOggetto, null, true);
	}
	
	/**
	 * costruisce la condizione di validita alla data indicata con nomeParametroDate
	 * @param aliasOggetto
	 * @param nomeParametroDate
	 * @return
	 */
	public static String validitaForQuery(String aliasOggetto, String nomeParametroDate,boolean consideraDataCancellazione){
		String dateParamName = "";
		if(StringUtilsFin.isEmpty(nomeParametroDate)){
			dateParamName = NOW_DATE_PARAM_JPQL;//default
		} else {
			dateParamName = nomeParametroDate;
		}
		String validita = validitaForQuery(aliasOggetto, dateParamName, dateParamName,consideraDataCancellazione);
		return validita;
	}

	public static String validitaForQuery(String aliasOggetto, String nomeParametroDateInizio,String nomeParametroDateFine,boolean consideraDataCancellazione){
		String validita = "( ("+aliasOggetto+".dataInizioValidita IS NULL OR "+aliasOggetto+".dataInizioValidita <= :"+nomeParametroDateInizio+") " +
				" AND ("+aliasOggetto+".dataFineValidita IS NULL OR "+aliasOggetto+".dataFineValidita >= :"+nomeParametroDateFine+" ) ";
		
		
		if(consideraDataCancellazione){
			validita = validita + " AND "+aliasOggetto+".dataCancellazione IS NULL )";
		} else {
			validita = validita + ")";
		}
		
		return validita;
	}
	//la maggior parte della gestione delle data validita_fine e' pari a quella della data cancellazione
	public static String ottieniClauseCheEscludeRecordCancellatiLogicamente(String aliasOggetto){
		return " AND ("+aliasOggetto+".dataFineValidita IS NULL AND "+aliasOggetto+".dataCancellazione IS NULL )";
	}
	
	
	
}
