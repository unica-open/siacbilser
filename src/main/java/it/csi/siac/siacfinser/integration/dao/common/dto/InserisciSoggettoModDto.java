/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;

import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSoggettoFin;

/**
 * Data Transfer Object per uso interno nelle classi SoggettoFinDad e SoggettoDao
 * @author claudio.picco
 *
 */
public class InserisciSoggettoModDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8053962569611707060L;
	private SiacTSoggettoFin siacTSoggettoOriginale;
	private DatiOperazioneDto datiOperazioneInserisci;
	private String soggettoCode;
	private SiacRSoggettoStatoFin stato;
	
	private boolean modificato;

	public SiacTSoggettoFin getSiacTSoggettoOriginale() {
		return siacTSoggettoOriginale;
	}

	public void setSiacTSoggettoOriginale(SiacTSoggettoFin siacTSoggettoOriginale) {
		this.siacTSoggettoOriginale = siacTSoggettoOriginale;
	}

	public DatiOperazioneDto getDatiOperazioneInserisci() {
		return datiOperazioneInserisci;
	}

	public void setDatiOperazioneInserisci(DatiOperazioneDto datiOperazioneInserisci) {
		this.datiOperazioneInserisci = datiOperazioneInserisci;
	}

	public String getSoggettoCode() {
		return soggettoCode;
	}

	public void setSoggettoCode(String soggettoCode) {
		this.soggettoCode = soggettoCode;
	}

	public boolean isModificato() {
		return modificato;
	}

	public void setModificato(boolean modificato) {
		this.modificato = modificato;
	}

	public SiacRSoggettoStatoFin getStato() {
		return stato;
	}

	public void setStato(SiacRSoggettoStatoFin stato) {
		this.stato = stato;
	}
	
}
