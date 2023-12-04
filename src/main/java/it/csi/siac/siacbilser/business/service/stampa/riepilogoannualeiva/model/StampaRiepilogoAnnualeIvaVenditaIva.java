/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.riepilogoannualeiva.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;
import it.csi.siac.siacfin2ser.model.AliquotaIva;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class StampaRiepilogoAnnualeIvaVenditaIva {
	
	private AliquotaIva aliquotaIva; //A1, B1, C1, D1
	
	//-Per ogni codice aliquota (ogni riga della tabella) si selezionano 
	// dall'entità "Progressivi Iva" tutti i registri del gruppo selezionato e di tipo "VENDITE IVA IMMEDIATA".
	//-Dall'elenco così ottenuto si fa la somma dei valori del campo "totImponibileDef"
	private BigDecimal imponibile; //F1
	
	//-Per ogni codice aliquota (ogni riga della tabella) si selezionano 
	// dall'entità "Progressivi Iva" tutti i registri del gruppo selezionato e di tipo "VENDITE IVA IMMEDIATA".
	//-Dall'elenco così ottenuto si fa la somma dei valori del campo "totIvaDef"
	private BigDecimal iva; //G1
	/**
	 * @return the aliquotaIva
	 */
	public AliquotaIva getAliquotaIva() {
		return aliquotaIva;
	}
	/**
	 * @param aliquotaIva the aliquotaIva to set
	 */
	public void setAliquotaIva(AliquotaIva aliquotaIva) {
		this.aliquotaIva = aliquotaIva;
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
	 * @return the iva
	 */
	public BigDecimal getIva() {
		return iva;
	}
	/**
	 * @param iva the iva to set
	 */
	public void setIva(BigDecimal iva) {
		this.iva = iva;
	}
	
	
	
	
	

}
