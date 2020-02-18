/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.ricevuta.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siacfin2ser.model.Valuta;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class StampaRicevutaRendicontoRichiestaEconomaleReportModel {
	private RendicontoRichiesta rendicontoRichiesta;
	private Date dataStampa;
	private Valuta valutaDefault;
	
	private Soggetto soggetto;

	private String modalitaPagamentoPerStampa;
	
	private String denominazioneRichiedente;

	/**
	 * @return the denominazioneRichiedente
	 */
	public String getDenominazioneRichiedente() {
		return denominazioneRichiedente;
	}

	/**
	 * @param denominazioneRichiedente the denominazioneRichiedente to set
	 */
	public void setDenominazioneRichiedente(String denominazioneRichiedente) {
		this.denominazioneRichiedente = denominazioneRichiedente;
	}
	
	/**
	 * @return the rendicontoRichiesta
	 */
	public RendicontoRichiesta getRendicontoRichiesta() {
		return rendicontoRichiesta;
	}

	/**
	 * @param rendicontoRichiesta the rendicontoRichiesta to set
	 */
	public void setRendicontoRichiesta(RendicontoRichiesta rendicontoRichiesta) {
		this.rendicontoRichiesta = rendicontoRichiesta;
	}

	/**
	 * @return the dataStampa
	 */
	public Date getDataStampa() {
		return dataStampa == null ? null : new Date(dataStampa.getTime());
	}



	/**
	 * @param dataStampa the dataStampa to set
	 */
	public void setDataStampa(Date dataStampa) {
		this.dataStampa = dataStampa == null ? null : new Date(dataStampa.getTime());
	}

	/**
	 * @return the valutaDefault
	 */
	public Valuta getValutaDefault() {
		return valutaDefault;
	}

	/**
	 * @param valutaDefault the valutaDefault to set
	 */
	public void setValutaDefault(Valuta valutaDefault) {
		this.valutaDefault = valutaDefault;
	}

	/**
	 * @return the soggetto
	 */
	public final Soggetto getSoggetto() {
		return soggetto;
	}

	/**
	 * @param soggetto the soggetto to set
	 */
	public final void setSoggetto(Soggetto soggetto) {
		this.soggetto = soggetto;
	}

	/**
	 * @return the modalitaPagamentoPerStampa
	 */
	public String getModalitaPagamentoPerStampa() {
		return modalitaPagamentoPerStampa;
	}

	/**
	 * @param modalitaPagamentoPerStampa the modalitaPagamentoPerStampa to set
	 */
	public void setModalitaPagamentoPerStampa(String modalitaPagamentoPerStampa) {
		this.modalitaPagamentoPerStampa = modalitaPagamentoPerStampa;
	}


}
