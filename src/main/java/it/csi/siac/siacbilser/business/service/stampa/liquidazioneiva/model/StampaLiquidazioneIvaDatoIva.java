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
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.TipoStampa;

/**
 * 
 *  La classe StampaLiquidazioneIvaDatoIva.
 *
 */
@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class StampaLiquidazioneIvaDatoIva extends DatiIva {
	
	private RegistroIva registroIva;                         // A1, B1
	private AliquotaSubdocumentoIva aliquotaSubdocumentoIva; // C1, D1, E1, F1
	private TipoStampa tipoStampaRegistro;
	
	private BigDecimal imponibile;             // G1
	private BigDecimal imposta;                // H1
	/**
	 * @return the registroIva
	 */
	public RegistroIva getRegistroIva() {
		return registroIva;
	}
	/**
	 * @param registroIva the registroIva to set
	 */
	public void setRegistroIva(RegistroIva registroIva) {
		this.registroIva = registroIva;
	}
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
	 * @return the tipoStampaRegistro
	 */
	public TipoStampa getTipoStampaRegistro() {
		return tipoStampaRegistro;
	}
	/**
	 * @param tipoStampaRegistro the tipoStampaRegistro to set
	 */
	public void setTipoStampaRegistro(TipoStampa tipoStampaRegistro) {
		this.tipoStampaRegistro = tipoStampaRegistro;
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
