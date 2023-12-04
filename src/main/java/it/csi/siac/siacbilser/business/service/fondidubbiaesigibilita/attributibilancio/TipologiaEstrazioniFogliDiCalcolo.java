/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilser.model.fcde.TipoAccantonamentoFondiDubbiaEsigibilita;

public enum TipologiaEstrazioniFogliDiCalcolo {

	CREDITI_STRALCIATI(TipoAccantonamentoFondiDubbiaEsigibilita.RENDICONTO, "CREDITI_STRALCIATI"),
	ACCERTAMENTI_ANNI_SUCCESSIVI(TipoAccantonamentoFondiDubbiaEsigibilita.RENDICONTO, "ACCERTAMENTI_ANNI_SUCCESSIVI"),
	;

	TipologiaEstrazioniFogliDiCalcolo(TipoAccantonamentoFondiDubbiaEsigibilita tipoAccantonamento, String codice) {
		this.tipologia = tipoAccantonamento;
		this.codice = codice;
	}
	
	private TipoAccantonamentoFondiDubbiaEsigibilita tipologia;
	private String codice;
	
	/**
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}
	
	/**
	 * @param codice the codice to set
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}

	/**
	 * @return the tipologia
	 */
	public TipoAccantonamentoFondiDubbiaEsigibilita getTipologia() {
		return tipologia;
	}

	/**
	 * @param tipologia the tipologia to set
	 */
	public void setTipologia(TipoAccantonamentoFondiDubbiaEsigibilita tipologia) {
		this.tipologia = tipologia;
	}
	
	public TipologiaEstrazioniFogliDiCalcolo[] estraiTipologiePerTipoAccantonamento(TipoAccantonamentoFondiDubbiaEsigibilita tipoAccantonamento) {
		List<TipologiaEstrazioniFogliDiCalcolo> tipologie = new ArrayList<TipologiaEstrazioniFogliDiCalcolo>();
		for(TipologiaEstrazioniFogliDiCalcolo tipologia : TipologiaEstrazioniFogliDiCalcolo.values()) {
			if(tipologia.getTipologia().getCodice().equals(tipoAccantonamento.getCodice())) {
				tipologie.add(tipologia);
			}
		}
		return (TipologiaEstrazioniFogliDiCalcolo[]) tipologie.toArray();
	}

	public TipologiaEstrazioniFogliDiCalcolo estraiTipologiaPerCodice(String codice) {
		TipologiaEstrazioniFogliDiCalcolo tipologia = null;
		if(StringUtils.isNotBlank(codice)) {
			for(TipologiaEstrazioniFogliDiCalcolo tipo : TipologiaEstrazioniFogliDiCalcolo.values()) {
				if(tipo.getCodice().equals(codice)) {
					tipologia = tipo;
				}
			}
		}
		return tipologia;
	}
}
