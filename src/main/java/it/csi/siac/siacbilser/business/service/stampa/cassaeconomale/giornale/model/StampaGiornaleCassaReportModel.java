/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.giornale.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.OperazioneCassa;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.TipoDiCassa;

public class StampaGiornaleCassaReportModel {
	
	private StampaGiornaleCassaIntestazione intestazione;
	private Date dataUltimaStampaDef;
	private StampaGiornaleCassaTotali totaliCC;
	private StampaGiornaleCassaTotali totaliCO;
	@XmlElements({
		@XmlElement(name="movimento", type=StampaGiornaleCassaMovimento.class),
	})	
	@XmlElementWrapper(name = "movimenti")
	private List<StampaGiornaleCassaMovimento> movimenti;
	@XmlElements({
		@XmlElement(name="operazioneCassa", type=StampaGiornaleCassaOperazioneCassa.class),
	})	
	@XmlElementWrapper(name = "operazioniCassa")
	private List<StampaGiornaleCassaOperazioneCassa> operazioniDiCassa;
	private RichiestaEconomale richiestaEconomale;
	private CassaEconomale cassaEconomale;
	private OperazioneCassa operazioneCassa;
	private BigDecimal totaleFondoCassa;
	private BigDecimal totaleDisponibilitaCassaCC;
	private BigDecimal totaleDisponibilitaCassaCO;
	
	private TipoDiCassa tipoDiCassa;
	
	/**
	 * @return the intestazione
	 */
	public StampaGiornaleCassaIntestazione getIntestazione() {
		return intestazione;
	}
	/**
	 * @param intestazione the intestazione to set
	 */
	public void setIntestazione(StampaGiornaleCassaIntestazione intestazione) {
		this.intestazione = intestazione;
	}
	/**
	 * @return the movimenti
	 */
	@XmlTransient
	public List<StampaGiornaleCassaMovimento> getMovimenti() {
		return movimenti;
	}
	/**
	 * @param movimenti the movimenti to set
	 */
	public void setMovimenti(List<StampaGiornaleCassaMovimento> movimenti) {
		this.movimenti = movimenti;
	}
	/**
	 * @return the operazioniDiCassa
	 */
	@XmlTransient
	public List<StampaGiornaleCassaOperazioneCassa> getOperazioniDiCassa() {
		return operazioniDiCassa;
	}
	/**
	 * @param operazioniDiCassa the operazioniDiCassa to set
	 */
	public void setOperazioniDiCassa(List<StampaGiornaleCassaOperazioneCassa> operazioniDiCassa) {
		this.operazioniDiCassa = operazioniDiCassa;
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
	 * @return the operazioneCassa
	 */
	public OperazioneCassa getOperazioneCassa() {
		return operazioneCassa;
	}
	/**
	 * @param operazioneCassa the operazioneCassa to set
	 */
	public void setOperazioneCassa(OperazioneCassa operazioneCassa) {
		this.operazioneCassa = operazioneCassa;
	}

	/**
	 * @return the totaliCC
	 */
	public StampaGiornaleCassaTotali getTotaliCC() {
		return totaliCC;
	}
	/**
	 * @param totaliCC the totaliCC to set
	 */
	public void setTotaliCC(StampaGiornaleCassaTotali totaliCC) {
		this.totaliCC = totaliCC;
	}
	/**
	 * @return the totaliCO
	 */
	public StampaGiornaleCassaTotali getTotaliCO() {
		return totaliCO;
	}
	/**
	 * @param totaliCO the totaliCO to set
	 */
	public void setTotaliCO(StampaGiornaleCassaTotali totaliCO) {
		this.totaliCO = totaliCO;
	}
	/**
	 * @return the totaleFondoCassa
	 */
	public BigDecimal getTotaleFondoCassa() {
		return totaleFondoCassa;
	}
	/**
	 * @param totaleFondoCassa the totaleFondoCassa to set
	 */
	public void setTotaleFondoCassa(BigDecimal totaleFondoCassa) {
		this.totaleFondoCassa = totaleFondoCassa;
	}
	/**
	 * @return the totaleDisponibilitaCassaCC
	 */
	public BigDecimal getTotaleDisponibilitaCassaCC() {
		return totaleDisponibilitaCassaCC;
	}
	/**
	 * @param totaleDisponibilitaCassaCC the totaleDisponibilitaCassaCC to set
	 */
	public void setTotaleDisponibilitaCassaCC(BigDecimal totaleDisponibilitaCassaCC) {
		this.totaleDisponibilitaCassaCC = totaleDisponibilitaCassaCC;
	}
	/**
	 * @return the totaleDisponibilitaCassaCO
	 */
	public BigDecimal getTotaleDisponibilitaCassaCO() {
		return totaleDisponibilitaCassaCO;
	}
	/**
	 * @param totaleDisponibilitaCassaCO the totaleDisponibilitaCassaCO to set
	 */
	public void setTotaleDisponibilitaCassaCO(BigDecimal totaleDisponibilitaCassaCO) {
		this.totaleDisponibilitaCassaCO = totaleDisponibilitaCassaCO;
	}
	/**
	 * @return the tipoDiCassa
	 */
	@XmlJavaTypeAdapter(TipoDiCassa.TipoDiCassaAdapter.class)
	public TipoDiCassa getTipoDiCassa() {
		return tipoDiCassa;
	}
	/**
	 * @param tipoDiCassa the tipoDiCassa to set
	 */
	public void setTipoDiCassa(TipoDiCassa tipoDiCassa) {
		this.tipoDiCassa = tipoDiCassa;
	}

	/**
	 * @return the dataUltimaStampaDef
	 */
	public Date getDataUltimaStampaDef() {
		return dataUltimaStampaDef == null ? null : new Date(dataUltimaStampaDef.getTime());
	}
	/**
	 * @param dataUltimaStampaDef the dataUltimaStampaDef to set
	 */
	public void setDataUltimaStampaDef(Date dataUltimaStampaDef) {
		this.dataUltimaStampaDef = dataUltimaStampaDef == null ? null : new Date(dataUltimaStampaDef.getTime());
	}
	
	
}
