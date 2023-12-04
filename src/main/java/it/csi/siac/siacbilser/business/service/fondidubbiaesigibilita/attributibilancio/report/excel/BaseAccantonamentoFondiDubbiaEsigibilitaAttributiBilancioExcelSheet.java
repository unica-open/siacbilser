/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel;

import java.math.BigDecimal;
import java.math.RoundingMode;

import it.csi.siac.siacbilser.business.service.excel.base.ExcelSheet;

public abstract class BaseAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelSheet
	<BAFDEER extends BaseAccantonamentoFondiDubbiaEsigibilitaExcelRow> 
		extends ExcelSheet<BAFDEER> {

	//SIAC-8513
	private final int maxPrecisionAllowed = 2;
	private final RoundingMode roundingMode = RoundingMode.HALF_DOWN;

	
	/**
	 * @return the maxPrecisionAllowed
	 */
	public int getMaxPrecisionAllowed() {
		return maxPrecisionAllowed;
	}

	/**
	 * @return the roundingMode
	 */
	public RoundingMode getRoundingMode() {
		return roundingMode;
	}

	/**
	 * SIAC-8513
	 * 
	 * Metodo di utilita' per restituire con la giusta formattazione i valori delle percentuali FCDE
	 * 
	 * @param <BigDecimal> perc il parametro percentuale derivante dall'array di oggetti
	 * @return BigDecimal se il valore esiste altrimenti null
	 */
	protected BigDecimal setBigDecimalPrecision(BigDecimal perc) {
		if(perc == null) return null;
		return perc.setScale(getMaxPrecisionAllowed(), getRoundingMode());
	}
	
	

	@Override
	protected String getTitle() {
		return "Fondo crediti dubbia esigibilità";
	}


}

