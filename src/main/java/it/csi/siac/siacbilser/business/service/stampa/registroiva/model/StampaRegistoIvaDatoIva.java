/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.registroiva.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.DatiIva;
import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;
import it.csi.siac.siacfin2ser.model.AliquotaSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.AttivitaIva;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoIva;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class StampaRegistoIvaDatoIva<SDI extends SubdocumentoIva<?, ?,?>, D extends Documento<?, ?>, SD extends Subdocumento<?, ?>> extends DatiIva {
	
	// Campi comuni nella sezione 1 del registro iva
	
	private SDI subdocumentoIva;                             // A1, B1
	private D documento;                                     // C1, D1; E1, F1 tipoDocumento; F1, G1 soggetto
	private AttivitaIva attivitaIva;                         // H1
	private AliquotaSubdocumentoIva aliquotaSubdocumentoIva; // M1, N1 aliquotaIva
	private SD subdocumento;                                 // P1, Q1
	
	private BigDecimal totale;                               //O1

	/**
	 * @return the subdocumentoIva
	 */
	public SDI getSubdocumentoIva() {
		return subdocumentoIva;
	}

	/**
	 * @param subdocumentoIva the subdocumentoIva to set
	 */
	public void setSubdocumentoIva(SDI subdocumentoIva) {
		this.subdocumentoIva = subdocumentoIva;
	}

	/**
	 * @return the documento
	 */
	public D getDocumento() {
		return documento;
	}

	/**
	 * @param documento the documento to set
	 */
	public void setDocumento(D documento) {
		this.documento = documento;
	}

	/**
	 * @return the attivitaIva
	 */
	public AttivitaIva getAttivitaIva() {
		return attivitaIva;
	}

	/**
	 * @param attivitaIva the attivitaIva to set
	 */
	public void setAttivitaIva(AttivitaIva attivitaIva) {
		this.attivitaIva = attivitaIva;
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
	 * @return the subdocumento
	 */
	public SD getSubdocumento() {
		return subdocumento;
	}

	/**
	 * @param subdocumento the subdocumento to set
	 */
	public void setSubdocumento(SD subdocumento) {
		this.subdocumento = subdocumento;
	}

	/**
	 * @return the totale
	 */
	public BigDecimal getTotale() {
		return totale;
	}

	/**
	 * @param totale the totale to set
	 */
	public void setTotale(BigDecimal totale) {
		this.totale = totale;
	}
	
}
