/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.Where;
import org.hibernate.annotations.WhereJoinTable;

@Entity
@Table(name = "pagopa_t_elaborazione_flusso")
public class PagopaTElaborazioneFlusso extends SiacTEnteBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "PAGOPA_T_ELABORAZIONEFLUSSOPAGOPA_ELAB_FLUSSO_ID_GENERATOR", allocationSize = 1, sequenceName = "PAGOPA_T_ELABORAZIONE_FLUSSO_PAGOPA_ELAB_FLUSSO_ID_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAGOPA_T_ELABORAZIONEFLUSSOPAGOPA_ELAB_FLUSSO_ID_GENERATOR")
	@Column(name = "pagopa_elab_flusso_id")
	private Integer pagopaElabFlussoId;
		    
	
	@Column(name = "pagopa_elab_flusso_data")
	private Date pagopaElabFlussoData;
		    
	@ManyToOne
	@JoinColumn(name="pagopa_elab_flusso_stato_id")
	private PagopaDElaborazioneStato pagopaDElaborazioneStato;
	
	@Column(name = "pagopa_elab_flusso_note")
	private String pagopaElabFlussoNote;
	
	@Column(name = "pagopa_elab_ric_flusso_id")
	private String pagopaElabRicFlussoId;
	
	@Column(name = "pagopa_elab_flusso_nome_mittente")
	private String pagopaElabFlussoNomeMittente;
	
	@Column(name = "pagopa_elab_ric_flusso_data")
	private String pagopaElabRicFlussoData;
	
	@Column(name="pagopa_elab_flusso_tot_pagam")
	private BigDecimal pagopaElabFlussoTotPagam;
	
	@Column(name = "pagopa_elab_flusso_anno_esercizio")
	private Integer pagopaElabFlussoAnnoEsercizio;
	
	
	@Column(name = "pagopa_elab_flusso_anno_provvisorio")
	private Integer pagopaElabFlussoAnnoProvvisorio;
	
	@Column(name = "pagopa_elab_flusso_num_provvisorio")
	private Integer pagopaElabFlussoNumProvvisorio;
	
	
	@Transient
	private Date dataEmissioneProv;
	
	@Transient
	private String esitoElaborazione;
	

 	//SIAC-7911-2 Inizio FL Correzione per la ricerca sulla data Emissione
	/*@JoinColumn(name="pagopa_elab_flusso_provc_id")*/

	/*@JoinColumns({
	@JoinColumn(name="ente_proprietario_id", referencedColumnName="ente_proprietario_id", insertable=false, updatable=false),
	@JoinColumn(name = "pagopa_elab_flusso_anno_provvisorio", referencedColumnName = "provc_anno", insertable=false, updatable=false),
	@JoinColumn(name = "pagopa_elab_flusso_num_provvisorio", referencedColumnName = "provc_numero", insertable=false, updatable=false) })
	@Where(clause = "siacTProvCassa.siacDProvCassaTipo.provcTipoCode = 'E'") */
	
	@ManyToOne
	@JoinColumn(name="pagopa_elab_flusso_provc_id")
	private SiacTProvCassa siacTProvCassa;
	
	@ManyToOne
	@JoinColumn(name="pagopa_elab_id")
	private PagopaTElaborazione pagopaTElaborazione;

	//SIAC-7911-2 Fine FL

	public PagopaTElaborazioneFlusso() {
	}

	@Override
	public Integer getUid() {
		return pagopaElabFlussoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.pagopaElabFlussoId = uid;
	}

	/**
	 * @return the pagopaElabFlussoId
	 */
	public Integer getPagopaElabFlussoId()
	{
		return pagopaElabFlussoId;
	}

	/**
	 * @param pagopaElabFlussoId the pagopaElabFlussoId to set
	 */
	public void setPagopaElabFlussoId(Integer pagopaElabFlussoId)
	{
		this.pagopaElabFlussoId = pagopaElabFlussoId;
	}

	/**
	 * @return the pagopaElabFlussoData
	 */
	public Date getPagopaElabFlussoData()
	{
		return pagopaElabFlussoData;
	}

	/**
	 * @param pagopaElabFlussoData the pagopaElabFlussoData to set
	 */
	public void setPagopaElabFlussoData(Date pagopaElabFlussoData)
	{
		this.pagopaElabFlussoData = pagopaElabFlussoData;
	}

	/**
	 * @return the pagopaDElaborazioneStato
	 */
	public PagopaDElaborazioneStato getPagopaDElaborazioneStato()
	{
		return pagopaDElaborazioneStato;
	}

	/**
	 * @param pagopaDElaborazioneStato the pagopaDElaborazioneStato to set
	 */
	public void setPagopaDElaborazioneStato(PagopaDElaborazioneStato pagopaDElaborazioneStato)
	{
		this.pagopaDElaborazioneStato = pagopaDElaborazioneStato;
	}

	/**
	 * @return the pagopaElabFlussoNote
	 */
	public String getPagopaElabFlussoNote()
	{
		return pagopaElabFlussoNote;
	}

	/**
	 * @param pagopaElabFlussoNote the pagopaElabFlussoNote to set
	 */
	public void setPagopaElabFlussoNote(String pagopaElabFlussoNote)
	{
		this.pagopaElabFlussoNote = pagopaElabFlussoNote;
	}

	/**
	 * @return the pagopaElabRicFlussoId
	 */
	public String getPagopaElabRicFlussoId()
	{
		return pagopaElabRicFlussoId;
	}

	/**
	 * @param pagopaElabRicFlussoId the pagopaElabRicFlussoId to set
	 */
	public void setPagopaElabRicFlussoId(String pagopaElabRicFlussoId)
	{
		this.pagopaElabRicFlussoId = pagopaElabRicFlussoId;
	}

	/**
	 * @return the pagopaElabFlussoNomeMittente
	 */
	public String getPagopaElabFlussoNomeMittente()
	{
		return pagopaElabFlussoNomeMittente;
	}

	/**
	 * @param pagopaElabFlussoNomeMittente the pagopaElabFlussoNomeMittente to set
	 */
	public void setPagopaElabFlussoNomeMittente(String pagopaElabFlussoNomeMittente)
	{
		this.pagopaElabFlussoNomeMittente = pagopaElabFlussoNomeMittente;
	}

	/**
	 * @return the pagopaElabRicFlussoData
	 */
	public String getPagopaElabRicFlussoData()
	{
		return pagopaElabRicFlussoData;
	}

	/**
	 * @param pagopaElabRicFlussoData the pagopaElabRicFlussoData to set
	 */
	public void setPagopaElabRicFlussoData(String pagopaElabRicFlussoData)
	{
		this.pagopaElabRicFlussoData = pagopaElabRicFlussoData;
	}

	/**
	 * @return the pagopaElabFlussoTotPagam
	 */
	public BigDecimal getPagopaElabFlussoTotPagam()
	{
		return pagopaElabFlussoTotPagam;
	}

	/**
	 * @param pagopaElabFlussoTotPagam the pagopaElabFlussoTotPagam to set
	 */
	public void setPagopaElabFlussoTotPagam(BigDecimal pagopaElabFlussoTotPagam)
	{
		this.pagopaElabFlussoTotPagam = pagopaElabFlussoTotPagam;
	}

	/**
	 * @return the pagopaElabFlussoAnnoEsercizio
	 */
	public Integer getPagopaElabFlussoAnnoEsercizio()
	{
		return pagopaElabFlussoAnnoEsercizio;
	}

	/**
	 * @param pagopaElabFlussoAnnoEsercizio the pagopaElabFlussoAnnoEsercizio to set
	 */
	public void setPagopaElabFlussoAnnoEsercizio(Integer pagopaElabFlussoAnnoEsercizio)
	{
		this.pagopaElabFlussoAnnoEsercizio = pagopaElabFlussoAnnoEsercizio;
	}

	/**
	 * @return the pagopaElabFlussoAnnoProvvisorio
	 */
	public Integer getPagopaElabFlussoAnnoProvvisorio()
	{
		return pagopaElabFlussoAnnoProvvisorio;
	}

	/**
	 * @param pagopaElabFlussoAnnoProvvisorio the pagopaElabFlussoAnnoProvvisorio to set
	 */
	public void setPagopaElabFlussoAnnoProvvisorio(Integer pagopaElabFlussoAnnoProvvisorio)
	{
		this.pagopaElabFlussoAnnoProvvisorio = pagopaElabFlussoAnnoProvvisorio;
	}

	/**
	 * @return the pagopaElabFlussoNumProvvisorio
	 */
	public Integer getPagopaElabFlussoNumProvvisorio()
	{
		return pagopaElabFlussoNumProvvisorio;
	}

	/**
	 * @param pagopaElabFlussoNumProvvisorio the pagopaElabFlussoNumProvvisorio to set
	 */
	public void setPagopaElabFlussoNumProvvisorio(Integer pagopaElabFlussoNumProvvisorio)
	{
		this.pagopaElabFlussoNumProvvisorio = pagopaElabFlussoNumProvvisorio;
	}

	/**
	 * @return the siacTProvCassa
	 */
	public SiacTProvCassa getSiacTProvCassa()
	{
		return siacTProvCassa;
	}

	/**
	 * @param siacTProvCassa the siacTProvCassa to set
	 */
	public void setSiacTProvCassa(SiacTProvCassa siacTProvCassa)
	{
		this.siacTProvCassa = siacTProvCassa;
	}

	/**
	 * @return the dataEmissioneProv
	 */
	public Date getDataEmissioneProv() {
		return dataEmissioneProv;
	}

	/**
	 * @param dataEmissioneProv the dataEmissioneProv to set
	 */
	public void setDataEmissioneProv(Date dataEmissioneProv) {
		this.dataEmissioneProv = dataEmissioneProv;
	}

	/**
	 * @return the pagopaTElaborazione
	 */
	public PagopaTElaborazione getPagopaTElaborazione()
	{
		return pagopaTElaborazione;
	}

	/**
	 * @param pagopaTElaborazione the pagopaTElaborazione to set
	 */
	public void setPagopaTElaborazione(PagopaTElaborazione pagopaTElaborazione)
	{
		this.pagopaTElaborazione = pagopaTElaborazione;
	}

	public String getEsitoElaborazione() {
		return esitoElaborazione;
	}

	public void setEsitoElaborazione(String esitoElaborazione) {
		this.esitoElaborazione = esitoElaborazione;
	}
	
	
	
	 
}