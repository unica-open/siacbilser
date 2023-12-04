/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.stipendi.model;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacattser.model.ATTDataDictionary;

/**
 * TiporECORD 
 * @author Nazha Ahmad
 * @date 19/07/2016
 */
@XmlType(namespace = ATTDataDictionary.NAMESPACE)
public enum TipoRecord {

	STIPENDIO_LORDO("STIPENDIO_LORDO",TipoVoce.ENTRATA_SPESE_SINGOLE,true,false,"SPESA"),
	RECUPERI("RECUPERI",TipoVoce.ENTRATA_SPESE_SINGOLE,false,true,"ENTRATA"),
	ONERI("ONERI",TipoVoce.RITENUTE_ONERI,true,false,"SPESA"),
	RITENUTE("RITENUTE",TipoVoce.ENTRATA_SPESE_SINGOLE,true,true,"ENTRATA");

	private String codice;
	private TipoVoce tipoVoce;
	private boolean impegno;
	private boolean accertamento;
	private String tipoDocumento;

	


   TipoRecord(String codice, TipoVoce tipoVoce, boolean impegnoValorizzato, boolean accertamentoValorizzato, String tipoDocumento) {
		this.codice = codice;
		this.tipoVoce = tipoVoce;
		this.impegno = impegnoValorizzato;
		this.accertamento = accertamentoValorizzato;
		this.tipoDocumento = tipoDocumento;
	}

	public String getCodice() {
		return this.codice;
	}

	/**
	 * @return the tipoVoce
	 */
	public TipoVoce getTipoVoce() {
		return tipoVoce;
	}


	/**
	 * @return the impegno
	 */
	public boolean isImpegnoValorizzato() {
		return impegno;
	}

	/**
	 * @return the accertamento
	 */
	public boolean isAccertamentoValorizzato() {
		return accertamento;
	}

	/**
	 * @return the tipoDocumento
	 */
	public String getTipoDocumento() {
		return tipoDocumento;
	}


	public static TipoRecord getTipoRecordfromStipendio(Stipendio stipendio) {

		if (stipendio.isTipoStipendioLordo()) {
			return TipoRecord.STIPENDIO_LORDO;
		} else if (stipendio.isTipoRecuperi()) {
			return TipoRecord.RECUPERI;
		} else if (stipendio.isTipoRitenute()) {
			return TipoRecord.RITENUTE;
		} else if (stipendio.isTipoOneri()) {
			return TipoRecord.ONERI;
		}
		// Tipo record non riconosciuto
		return null;
	}
}
