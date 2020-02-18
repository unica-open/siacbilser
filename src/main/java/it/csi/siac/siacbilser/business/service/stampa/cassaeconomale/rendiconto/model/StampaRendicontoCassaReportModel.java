/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.rendiconto.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siaccecser.model.RichiestaEconomale;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class StampaRendicontoCassaReportModel {
	
	private RendicontoRichiesta rendicontoRichiesta;

	//private List<Movimento> movimenti;
	
	private RichiestaEconomale richiestaEconomale;
	private CassaEconomale cassaEconomale;
	//private OperazioneCassa operazioneCassa;
	
	private StampaRendicontoCassaIntestazione intestazione;
	
	private BigDecimal importoTotaleMovimentoCapitoli = BigDecimal.ZERO;
	private BigDecimal importoTotaleMovimentoCapitoliASM = BigDecimal.ZERO;
	private BigDecimal importoTotaleMovimentoCapitoliNOASM = BigDecimal.ZERO;
	private BigDecimal importoTotaleOperazioniCassa = BigDecimal.ZERO;
	private BigDecimal importoTotaleRendiconto = BigDecimal.ZERO;
	
	private int numeroMovimentiTotaleNOASM;
	private int numeroMovimentiTotaleASM;
	private int numeroMovimentiTotale;
	@XmlElements({
		@XmlElement(name="operazioneCassa", type=StampaRendicontoCassaOperazioneCassa.class),
	})	
	@XmlElementWrapper(name = "operazioniCassa")
	private List<StampaRendicontoCassaOperazioneCassa> listaOperazioniDiCassa;
	
	@XmlElements({
		@XmlElement(name="capitoloMovimento", type=StampaRendicontoCassaCapitoloMovimenti.class),
	})	
	@XmlElementWrapper(name = "capitoliMovimenti")
	private List<StampaRendicontoCassaCapitoloMovimenti> listaMovimentiPerCapitoli = new ArrayList<StampaRendicontoCassaCapitoloMovimenti>();
	
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
	 * @return the richiestaEconomale
	 */
	public RichiestaEconomale getRichiestaEconomale() {
		return richiestaEconomale;
	}

	/**
	 * @param richiestaEconomale the richiestaEconomale to set
	 */
	public void setRichiestaEconomale(RichiestaEconomale richiestaEconomale) {
		this.richiestaEconomale = richiestaEconomale;
	}

	/**
	 * @return the cassaEconomale
	 */
	public CassaEconomale getCassaEconomale() {
		return cassaEconomale;
	}

	/**
	 * @param cassaEconomale the cassaEconomale to set
	 */
	public void setCassaEconomale(CassaEconomale cassaEconomale) {
		this.cassaEconomale = cassaEconomale;
	}


	/**
	 * @return the intestazione
	 */
	public StampaRendicontoCassaIntestazione getIntestazione() {
		return intestazione;
	}

	/**
	 * @param intestazione the intestazione to set
	 */
	public void setIntestazione(StampaRendicontoCassaIntestazione intestazione) {
		this.intestazione = intestazione;
	}

	/**
	 * @return the importoTotaleMovimentoCapitoli
	 */
	public BigDecimal getImportoTotaleMovimentoCapitoli() {
		return importoTotaleMovimentoCapitoli;
	}

	/**
	 * @param importoTotaleMovimentoCapitoli the importoTotaleMovimentoCapitoli to set
	 */
	public void setImportoTotaleMovimentoCapitoli(
			BigDecimal importoTotaleMovimentoCapitoli) {
		this.importoTotaleMovimentoCapitoli = importoTotaleMovimentoCapitoli;
	}

	/**
	 * @return the importoTotaleMovimentoCapitoliASM
	 */
	public BigDecimal getImportoTotaleMovimentoCapitoliASM() {
		return importoTotaleMovimentoCapitoliASM;
	}

	/**
	 * @param importoTotaleMovimentoCapitoliASM the importoTotaleMovimentoCapitoliASM to set
	 */
	public void setImportoTotaleMovimentoCapitoliASM(
			BigDecimal importoTotaleMovimentoCapitoliASM) {
		this.importoTotaleMovimentoCapitoliASM = importoTotaleMovimentoCapitoliASM;
	}

	/**
	 * @return the importoTotaleMovimentoCapitoliNOASM
	 */
	public BigDecimal getImportoTotaleMovimentoCapitoliNOASM() {
		return importoTotaleMovimentoCapitoliNOASM;
	}

	/**
	 * @param importoTotaleMovimentoCapitoliNOASM the importoTotaleMovimentoCapitoliNOASM to set
	 */
	public void setImportoTotaleMovimentoCapitoliNOASM(
			BigDecimal importoTotaleMovimentoCapitoliNOASM) {
		this.importoTotaleMovimentoCapitoliNOASM = importoTotaleMovimentoCapitoliNOASM;
	}

	/**
	 * @return the importoTotaleOperazioniCassa
	 */
	public BigDecimal getImportoTotaleOperazioniCassa() {
		return importoTotaleOperazioniCassa;
	}

	/**
	 * @param importoTotaleOperazioniCassa the importoTotaleOperazioniCassa to set
	 */
	public void setImportoTotaleOperazioniCassa(
			BigDecimal importoTotaleOperazioniCassa) {
		this.importoTotaleOperazioniCassa = importoTotaleOperazioniCassa;
	}

	/**
	 * @return the importoTotaleRendiconto
	 */
	public BigDecimal getImportoTotaleRendiconto() {
		return importoTotaleRendiconto;
	}

	/**
	 * @param importoTotaleRendiconto the importoTotaleRendiconto to set
	 */
	public void setImportoTotaleRendiconto(BigDecimal importoTotaleRendiconto) {
		this.importoTotaleRendiconto = importoTotaleRendiconto;
	}

	

	/**
	 * @return the numeroMovimentiTotaleNOASM
	 */
	public int getNumeroMovimentiTotaleNOASM() {
		return numeroMovimentiTotaleNOASM;
	}

	/**
	 * @param numeroMovimentiTotaleNOASM the numeroMovimentiTotaleNOASM to set
	 */
	public void setNumeroMovimentiTotaleNOASM(int numeroMovimentiTotaleNOASM) {
		this.numeroMovimentiTotaleNOASM = numeroMovimentiTotaleNOASM;
	}

	/**
	 * @return the numeroMovimentiTotaleASM
	 */
	public int getNumeroMovimentiTotaleASM() {
		return numeroMovimentiTotaleASM;
	}

	/**
	 * @param numeroMovimentiTotaleASM the numeroMovimentiTotaleASM to set
	 */
	public void setNumeroMovimentiTotaleASM(int numeroMovimentiTotaleASM) {
		this.numeroMovimentiTotaleASM = numeroMovimentiTotaleASM;
	}

	/**
	 * @return the numeroMovimentiTotale
	 */
	public int getNumeroMovimentiTotale() {
		return numeroMovimentiTotale;
	}

	/**
	 * @param numeroMovimentiTotale the numeroMovimentiTotale to set
	 */
	public void setNumeroMovimentiTotale(int numeroMovimentiTotale) {
		this.numeroMovimentiTotale = numeroMovimentiTotale;
	}

	/**
	 * @return the listaOperazioniDiCassa
	 */
	@XmlTransient
	public List<StampaRendicontoCassaOperazioneCassa> getListaOperazioniDiCassa() {
		return listaOperazioniDiCassa;
	}

	/**
	 * @param listaOperazioniDiCassa the listaOperazioniDiCassa to set
	 */
	public void setListaOperazioniDiCassa(
			List<StampaRendicontoCassaOperazioneCassa> listaOperazioniDiCassa) {
		this.listaOperazioniDiCassa = listaOperazioniDiCassa;
	}

	/**
	 * @return the listaMovimentiPerCapitoli
	 */
	@XmlTransient
	public List<StampaRendicontoCassaCapitoloMovimenti> getListaMovimentiPerCapitoli() {
		return listaMovimentiPerCapitoli;
	}

	/**
	 * @param listaMovimentiPerCapitoli the listaMovimentiPerCapitoli to set
	 */
	public void setListaMovimentiPerCapitoli(
			List<StampaRendicontoCassaCapitoloMovimenti> listaMovimentiPerCapitoli) {
		this.listaMovimentiPerCapitoli = listaMovimentiPerCapitoli;
	}




}
