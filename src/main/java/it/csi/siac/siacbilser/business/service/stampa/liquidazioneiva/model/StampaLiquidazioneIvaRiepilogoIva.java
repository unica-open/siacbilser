/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.DatiIva;
import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;
import it.csi.siac.siacfin2ser.model.AliquotaSubdocumentoIva;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class StampaLiquidazioneIvaRiepilogoIva extends DatiIva {
	
	private AliquotaSubdocumentoIva aliquotaSubdocumentoIva; // C2, D2, E2, F2
	private BigDecimal imponibile;                           // G2
	private BigDecimal imposta;                              // H2
	/**
	 * @return the aliquotaSubdocumentoIva
	 */
	public AliquotaSubdocumentoIva getAliquotaSubdocumentoIva() {
		return aliquotaSubdocumentoIva;
	}
	/**
	 * @param aliquotaSubdocumentoIva the aliquotaSubdocumentoIva to set
	 */
	public void setAliquotaSubdocumentoIva(
			AliquotaSubdocumentoIva aliquotaSubdocumentoIva) {
		this.aliquotaSubdocumentoIva = aliquotaSubdocumentoIva;
	}
	/**
	 * @return the imponibile
	 */
	public BigDecimal getImponibile() {
		return imponibile;
	}
	/**
	 * @param imponibile the imponibile to set
	 */
	public void setImponibile(BigDecimal imponibile) {
		this.imponibile = imponibile;
	}
	/**
	 * @return the imposta
	 */
	public BigDecimal getImposta() {
		return imposta;
	}
	/**
	 * @param imposta the imposta to set
	 */
	public void setImposta(BigDecimal imposta) {
		this.imposta = imposta;
	}
	
}
