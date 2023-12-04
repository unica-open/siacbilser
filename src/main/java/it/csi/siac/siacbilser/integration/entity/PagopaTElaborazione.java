/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "pagopa_t_elaborazione")
@NamedQuery(name="PagopaTElaborazione.findAll", query="SELECT s FROM PagopaTElaborazione s")
public class PagopaTElaborazione extends SiacTEnteBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "PAGOPA_T_ELABORAZIONEPAGOPA_ELAB_ID_GENERATOR", allocationSize = 1, sequenceName = "PAGOPA_T_ELABORAZIONE_PAGOPA_ELAB_ID_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAGOPA_T_ELABORAZIONEPAGOPA_ELAB_ID_GENERATOR")
	@Column(name = "pagopa_elab_id")
	private Integer pagopaElabId;
		    
	@Column(name = "pagopa_elab_data")
	private Date pagopaElabData;
		    
	@ManyToOne
	@JoinColumn(name="pagopa_elab_stato_id")
	private PagopaDElaborazioneStato pagopaDElaborazioneStato;
	
	@Column(name = "pagopa_elab_note")
	private String pagopaElabNote;
	
	@Column(name = "pagopa_elab_file_id")
	private String pagopaElabFileId;
	
	@Column(name = "pagopa_elab_file_ora")
	private String pagopaElabFileOra;
	
	@Column(name = "pagopa_elab_file_ente")
	private String pagopaElabFileEnte;
	
	@Column(name = "pagopa_elab_file_fruitore")
	private String pagopaElabFileFruitore;
	
	@ManyToOne
	@JoinColumn(name="file_pagopa_id")
	private SiacTFilePagopa siacTFilePagopa;
	  
	@ManyToOne
	@JoinColumn(name="pagopa_elab_errore_id")
	private PagopaDRiconciliazioneErrore pagopaDRiconciliazioneErrore;
	
	
	//bi-directional many-to-one association to PagopaTElaborazioneFlusso
	@OneToMany(mappedBy="pagopaTElaborazione")
	private List<PagopaTElaborazioneFlusso> pagopaTElaborazioneFlusso;
	    

	public PagopaTElaborazione() {
	}

	@Override
	public Integer getUid() {
		return pagopaElabId;
	}

	@Override
	public void setUid(Integer uid) {
		this.pagopaElabId = uid;
	}

	/**
	 * @return the pagopaElabId
	 */
	public Integer getPagopaElabId()
	{
		return pagopaElabId;
	}

	/**
	 * @param pagopaElabId the pagopaElabId to set
	 */
	public void setPagopaElabId(Integer pagopaElabId)
	{
		this.pagopaElabId = pagopaElabId;
	}

	/**
	 * @return the pagopaElabData
	 */
	public Date getPagopaElabData()
	{
		return pagopaElabData;
	}

	/**
	 * @param pagopaElabData the pagopaElabData to set
	 */
	public void setPagopaElabData(Date pagopaElabData)
	{
		this.pagopaElabData = pagopaElabData;
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
	 * @return the pagopaElabNote
	 */
	public String getPagopaElabNote()
	{
		return pagopaElabNote;
	}

	/**
	 * @param pagopaElabNote the pagopaElabNote to set
	 */
	public void setPagopaElabNote(String pagopaElabNote)
	{
		this.pagopaElabNote = pagopaElabNote;
	}

	/**
	 * @return the pagopaElabFileId
	 */
	public String getPagopaElabFileId()
	{
		return pagopaElabFileId;
	}

	/**
	 * @param pagopaElabFileId the pagopaElabFileId to set
	 */
	public void setPagopaElabFileId(String pagopaElabFileId)
	{
		this.pagopaElabFileId = pagopaElabFileId;
	}

	/**
	 * @return the pagopaElabFileOra
	 */
	public String getPagopaElabFileOra()
	{
		return pagopaElabFileOra;
	}

	/**
	 * @param pagopaElabFileOra the pagopaElabFileOra to set
	 */
	public void setPagopaElabFileOra(String pagopaElabFileOra)
	{
		this.pagopaElabFileOra = pagopaElabFileOra;
	}

	/**
	 * @return the pagopaElabFileEnte
	 */
	public String getPagopaElabFileEnte()
	{
		return pagopaElabFileEnte;
	}

	/**
	 * @param pagopaElabFileEnte the pagopaElabFileEnte to set
	 */
	public void setPagopaElabFileEnte(String pagopaElabFileEnte)
	{
		this.pagopaElabFileEnte = pagopaElabFileEnte;
	}

	/**
	 * @return the pagopaElabFileFruitore
	 */
	public String getPagopaElabFileFruitore()
	{
		return pagopaElabFileFruitore;
	}

	/**
	 * @param pagopaElabFileFruitore the pagopaElabFileFruitore to set
	 */
	public void setPagopaElabFileFruitore(String pagopaElabFileFruitore)
	{
		this.pagopaElabFileFruitore = pagopaElabFileFruitore;
	}

	/**
	 * @return the siacTFilePagopa
	 */
	public SiacTFilePagopa getSiacTFilePagopa()
	{
		return siacTFilePagopa;
	}

	/**
	 * @param siacTFilePagopa the siacTFilePagopa to set
	 */
	public void setSiacTFilePagopa(SiacTFilePagopa siacTFilePagopa)
	{
		this.siacTFilePagopa = siacTFilePagopa;
	}

	/**
	 * @return the pagopaDRiconciliazioneErrore
	 */
	public PagopaDRiconciliazioneErrore getPagopaDRiconciliazioneErrore()
	{
		return pagopaDRiconciliazioneErrore;
	}

	/**
	 * @param pagopaDRiconciliazioneErrore the pagopaDRiconciliazioneErrore to set
	 */
	public void setPagopaDRiconciliazioneErrore(PagopaDRiconciliazioneErrore pagopaDRiconciliazioneErrore)
	{
		this.pagopaDRiconciliazioneErrore = pagopaDRiconciliazioneErrore;
	}

	/**
	 * @return the pagopaTElaborazioneFlusso
	 */
	public List<PagopaTElaborazioneFlusso> getPagopaTElaborazioneFlusso()
	{
		return pagopaTElaborazioneFlusso;
	}

	/**
	 * @param pagopaTElaborazioneFlusso the pagopaTElaborazioneFlusso to set
	 */
	public void setPagopaTElaborazioneFlusso(List<PagopaTElaborazioneFlusso> pagopaTElaborazioneFlusso)
	{
		this.pagopaTElaborazioneFlusso = pagopaTElaborazioneFlusso;
	}
 
	
	
	
	 
}