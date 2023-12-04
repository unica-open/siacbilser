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
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;

@Entity
@Table(name = "pagopa_t_riconciliazione_doc")
public class PagopaTRiconciliazioneDoc extends SiacTEnteBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "PAGOPA_T_RICONCILIAZIONEDOCPAGOPARIC_DOC_ID_GENERATOR", allocationSize = 1, sequenceName = "PAGOPA_T_RICONCILIAZIONE_DOC_PAGOPA_RIC_DOC_ID_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAGOPA_T_RICONCILIAZIONEDOCPAGOPARIC_DOC_ID_GENERATOR")
	@Column(name = "pagopa_ric_doc_id")
	private Integer pagopaRicDocId;

	@Transient
	private Integer erroreDettaglio;
	@Transient
	private String messaggioErrore;
	//SIAC-8123 CM 29/04/2021 Intervento 2 Inizio
	@Transient
	private Integer ricDocDettagliElaboratiDiAggregato;
	//SIAC-8123 CM 29/04/2021 Intervento 2 Fine
	
	@Column(name = "pagopa_ric_doc_data")
	private Date pagopaRicDocData;
	
	@Column(name = "pagopa_ric_doc_voce_code")
	private String pagopaRicDocVoceCode;
	
	@Column(name = "pagopa_ric_doc_voce_desc")
	private String pagopaRicDocVoceDesc;
	
	@Column(name = "pagopa_ric_doc_voce_tematica")
	private String pagopaRicDocVoceTematica;
	
	@Column(name = "pagopa_ric_doc_sottovoce_code")
	private String pagopaRicDocSottovoceCode;
	
	@Column(name = "pagopa_ric_doc_sottovoce_desc")
	private String pagopaRicDocSottovoceDesc;
	
	@Column(name="pagopa_ric_doc_sottovoce_importo")
	private BigDecimal pagopaRicDocSottovoceImporto;
	
	@Column(name = "pagopa_ric_doc_anno_esercizio")
	private Integer pagopaRicDocAnnoEsercizio;
	
	@Column(name = "pagopa_ric_doc_anno_accertamento")
	private Integer pagopaRicDocAnnoAccertamento;
	
	@Column(name = "pagopa_ric_doc_num_accertamento")
	private Integer pagopaRicDocNumAccertamento;
	
	@Column(name = "pagopa_ric_doc_num_capitolo")
	private Integer pagopaRicDocNumCapitolo;
	
	@Column(name = "pagopa_ric_doc_num_articolo")
	private Integer pagopaRicDocNumArticolo;
	
	@Column(name = "pagopa_ric_doc_pdc_v_fin")
	private String pagopaRicDocPdcVFfin;
	
	@Column(name = "pagopa_ric_doc_titolo")
	private String pagopaRicDocTitolo;
	
	@Column(name = "pagopa_ric_doc_tipologia")
	private String pagopaRicDocTipologia;
	
	@Column(name = "pagopa_ric_doc_categoria")
	private String pagopaRicDocCategoria;
	
	@Column(name = "pagopa_ric_doc_codice_benef")
	private String pagopaRicDocCodiceBenef;
	
	@Column(name = "pagopa_ric_doc_str_amm")
	private String pagopaRicDocStrAmm;
	
	@Column(name = "pagopa_ric_doc_subdoc_id")
	private Integer pagopaRicDocSubdocId;
	
	@ManyToOne
	@JoinColumn(name="pagopa_ric_doc_provc_id")
	private SiacTProvCassa siacTProvCassa;
	
	@ManyToOne
	@JoinColumn(name="pagopa_ric_doc_movgest_ts_id")
	private SiacTMovgestT siacTMovgestTsFin;
	
	@Column(name = "pagopa_ric_doc_stato_elab")
	private String pagopaRicDocStatoElab;
	
	@ManyToOne
	@JoinColumn(name="pagopa_ric_errore_id")
	private PagopaDRiconciliazioneErrore pagopaDRiconciliazioneErrore;

	@ManyToOne
	@JoinColumn(name="pagopa_ric_id")
	private PagopaTRiconciliazione pagopaTRiconciliazione;
	
	@ManyToOne
	@JoinColumn(name="pagopa_elab_flusso_id")
	private PagopaTElaborazioneFlusso pagopaTElaborazioneFlusso;
    
	@ManyToOne
	@JoinColumn(name="file_pagopa_id")
	private SiacTFilePagopa siacTFilePagopa;
	
    @Column(name = "pagopa_ric_doc_ragsoc_benef")
	private String pagopaRicDocRagsocBenef;
    
    @Column(name = "pagopa_ric_doc_nome_benef")
	private String pagopaRicDocNomeBenef;
    
    @Column(name = "pagopa_ric_doc_cognome_benef")
	private String pagopaRicDocCognomeBenef;
    
    @Column(name = "pagopa_ric_doc_codfisc_benef")
	private String pagopaRicDocCodfiscBenef;
    
   	@ManyToOne
   	@JoinColumn(name="pagopa_ric_doc_soggetto_id")
   	private SiacTSoggetto siacTSoggetto;
      	
	@Column(name="pagopa_ric_doc_flag_dett")
	private Boolean pagopaRicDocFlagDett;
	
	@Column(name="pagopa_ric_doc_flag_con_dett")
	private Boolean pagopaRicDocFlagConDett;
	
    @Column(name = "pagopa_ric_doc_tipo_code")
	private String pagopaRicDocTipoCode;
    
   	@ManyToOne
   	@JoinColumn(name="pagopa_ric_doc_tipo_id")
   	private SiacDDocTipo siacDDocTipo;
    
	@Column(name = "pagopa_ric_det_id")
	private Integer pagopaRicDetId;
   
    
	@Column(name = "pagopa_ric_doc_iuv")
	private String pagopaRicDocIuv;
   
    
    @Column(name = "pagopa_ric_doc_data_operazione")
	private Date pagopaRicDocFataOperazione;
	
    public PagopaTRiconciliazioneDoc() {
	}

	@Override
	public Integer getUid() {
		return pagopaRicDocId;
	}

	@Override
	public void setUid(Integer uid) {
		this.pagopaRicDocId = uid;
	}

	/**
	 * @return the pagopaRicDocId
	 */
	public Integer getPagopaRicDocId()
	{
		return pagopaRicDocId;
	}

	/**
	 * @param pagopaRicDocId the pagopaRicDocId to set
	 */
	public void setPagopaRicDocId(Integer pagopaRicDocId)
	{
		this.pagopaRicDocId = pagopaRicDocId;
	}

	/**
	 * @return the pagopaRicDocData
	 */
	public Date getPagopaRicDocData()
	{
		return pagopaRicDocData;
	}

	/**
	 * @param pagopaRicDocData the pagopaRicDocData to set
	 */
	public void setPagopaRicDocData(Date pagopaRicDocData)
	{
		this.pagopaRicDocData = pagopaRicDocData;
	}

	/**
	 * @return the pagopaRicDocVoceCode
	 */
	public String getPagopaRicDocVoceCode()
	{
		return pagopaRicDocVoceCode;
	}

	/**
	 * @param pagopaRicDocVoceCode the pagopaRicDocVoceCode to set
	 */
	public void setPagopaRicDocVoceCode(String pagopaRicDocVoceCode)
	{
		this.pagopaRicDocVoceCode = pagopaRicDocVoceCode;
	}

	/**
	 * @return the pagopaRicDocVoceDesc
	 */
	public String getPagopaRicDocVoceDesc()
	{
		return pagopaRicDocVoceDesc;
	}

	/**
	 * @param pagopaRicDocVoceDesc the pagopaRicDocVoceDesc to set
	 */
	public void setPagopaRicDocVoceDesc(String pagopaRicDocVoceDesc)
	{
		this.pagopaRicDocVoceDesc = pagopaRicDocVoceDesc;
	}

	/**
	 * @return the pagopaRicDocVoceTematica
	 */
	public String getPagopaRicDocVoceTematica()
	{
		return pagopaRicDocVoceTematica;
	}

	/**
	 * @param pagopaRicDocVoceTematica the pagopaRicDocVoceTematica to set
	 */
	public void setPagopaRicDocVoceTematica(String pagopaRicDocVoceTematica)
	{
		this.pagopaRicDocVoceTematica = pagopaRicDocVoceTematica;
	}

	/**
	 * @return the pagopaRicDocSottovoceCode
	 */
	public String getPagopaRicDocSottovoceCode()
	{
		return pagopaRicDocSottovoceCode;
	}

	/**
	 * @param pagopaRicDocSottovoceCode the pagopaRicDocSottovoceCode to set
	 */
	public void setPagopaRicDocSottovoceCode(String pagopaRicDocSottovoceCode)
	{
		this.pagopaRicDocSottovoceCode = pagopaRicDocSottovoceCode;
	}

	/**
	 * @return the pagopaRicDocSottovoceDesc
	 */
	public String getPagopaRicDocSottovoceDesc()
	{
		return pagopaRicDocSottovoceDesc;
	}

	/**
	 * @param pagopaRicDocSottovoceDesc the pagopaRicDocSottovoceDesc to set
	 */
	public void setPagopaRicDocSottovoceDesc(String pagopaRicDocSottovoceDesc)
	{
		this.pagopaRicDocSottovoceDesc = pagopaRicDocSottovoceDesc;
	}

	/**
	 * @return the pagopaRicDocSottovoceImporto
	 */
	public BigDecimal getPagopaRicDocSottovoceImporto()
	{
		return pagopaRicDocSottovoceImporto;
	}

	/**
	 * @param pagopaRicDocSottovoceImporto the pagopaRicDocSottovoceImporto to set
	 */
	public void setPagopaRicDocSottovoceImporto(BigDecimal pagopaRicDocSottovoceImporto)
	{
		this.pagopaRicDocSottovoceImporto = pagopaRicDocSottovoceImporto;
	}

	/**
	 * @return the pagopaRicDocAnnoEsercizio
	 */
	public Integer getPagopaRicDocAnnoEsercizio()
	{
		return pagopaRicDocAnnoEsercizio;
	}

	/**
	 * @param pagopaRicDocAnnoEsercizio the pagopaRicDocAnnoEsercizio to set
	 */
	public void setPagopaRicDocAnnoEsercizio(Integer pagopaRicDocAnnoEsercizio)
	{
		this.pagopaRicDocAnnoEsercizio = pagopaRicDocAnnoEsercizio;
	}

	/**
	 * @return the pagopaRicDocAnnoAccertamento
	 */
	public Integer getPagopaRicDocAnnoAccertamento()
	{
		return pagopaRicDocAnnoAccertamento;
	}

	/**
	 * @param pagopaRicDocAnnoAccertamento the pagopaRicDocAnnoAccertamento to set
	 */
	public void setPagopaRicDocAnnoAccertamento(Integer pagopaRicDocAnnoAccertamento)
	{
		this.pagopaRicDocAnnoAccertamento = pagopaRicDocAnnoAccertamento;
	}

	/**
	 * @return the pagopaRicDocNumAccertamento
	 */
	public Integer getPagopaRicDocNumAccertamento()
	{
		return pagopaRicDocNumAccertamento;
	}

	/**
	 * @param pagopaRicDocNumAccertamento the pagopaRicDocNumAccertamento to set
	 */
	public void setPagopaRicDocNumAccertamento(Integer pagopaRicDocNumAccertamento)
	{
		this.pagopaRicDocNumAccertamento = pagopaRicDocNumAccertamento;
	}

	/**
	 * @return the pagopaRicDocNumCapitolo
	 */
	public Integer getPagopaRicDocNumCapitolo()
	{
		return pagopaRicDocNumCapitolo;
	}

	/**
	 * @param pagopaRicDocNumCapitolo the pagopaRicDocNumCapitolo to set
	 */
	public void setPagopaRicDocNumCapitolo(Integer pagopaRicDocNumCapitolo)
	{
		this.pagopaRicDocNumCapitolo = pagopaRicDocNumCapitolo;
	}

	/**
	 * @return the pagopaRicDocNumArticolo
	 */
	public Integer getPagopaRicDocNumArticolo()
	{
		return pagopaRicDocNumArticolo;
	}

	/**
	 * @param pagopaRicDocNumArticolo the pagopaRicDocNumArticolo to set
	 */
	public void setPagopaRicDocNumArticolo(Integer pagopaRicDocNumArticolo)
	{
		this.pagopaRicDocNumArticolo = pagopaRicDocNumArticolo;
	}

	/**
	 * @return the pagopaRicDocPdcVFfin
	 */
	public String getPagopaRicDocPdcVFfin()
	{
		return pagopaRicDocPdcVFfin;
	}

	/**
	 * @param pagopaRicDocPdcVFfin the pagopaRicDocPdcVFfin to set
	 */
	public void setPagopaRicDocPdcVFfin(String pagopaRicDocPdcVFfin)
	{
		this.pagopaRicDocPdcVFfin = pagopaRicDocPdcVFfin;
	}

	/**
	 * @return the pagopaRicDocTitolo
	 */
	public String getPagopaRicDocTitolo()
	{
		return pagopaRicDocTitolo;
	}

	/**
	 * @param pagopaRicDocTitolo the pagopaRicDocTitolo to set
	 */
	public void setPagopaRicDocTitolo(String pagopaRicDocTitolo)
	{
		this.pagopaRicDocTitolo = pagopaRicDocTitolo;
	}

	/**
	 * @return the pagopaRicDocTipologia
	 */
	public String getPagopaRicDocTipologia()
	{
		return pagopaRicDocTipologia;
	}

	/**
	 * @param pagopaRicDocTipologia the pagopaRicDocTipologia to set
	 */
	public void setPagopaRicDocTipologia(String pagopaRicDocTipologia)
	{
		this.pagopaRicDocTipologia = pagopaRicDocTipologia;
	}

	/**
	 * @return the pagopaRicDocCategoria
	 */
	public String getPagopaRicDocCategoria()
	{
		return pagopaRicDocCategoria;
	}

	/**
	 * @param pagopaRicDocCategoria the pagopaRicDocCategoria to set
	 */
	public void setPagopaRicDocCategoria(String pagopaRicDocCategoria)
	{
		this.pagopaRicDocCategoria = pagopaRicDocCategoria;
	}

	/**
	 * @return the pagopaRicDocCodiceBenef
	 */
	public String getPagopaRicDocCodiceBenef()
	{
		return pagopaRicDocCodiceBenef;
	}

	/**
	 * @param pagopaRicDocCodiceBenef the pagopaRicDocCodiceBenef to set
	 */
	public void setPagopaRicDocCodiceBenef(String pagopaRicDocCodiceBenef)
	{
		this.pagopaRicDocCodiceBenef = pagopaRicDocCodiceBenef;
	}

	/**
	 * @return the pagopaRicDocStrAmm
	 */
	public String getPagopaRicDocStrAmm()
	{
		return pagopaRicDocStrAmm;
	}

	/**
	 * @param pagopaRicDocStrAmm the pagopaRicDocStrAmm to set
	 */
	public void setPagopaRicDocStrAmm(String pagopaRicDocStrAmm)
	{
		this.pagopaRicDocStrAmm = pagopaRicDocStrAmm;
	}

	/**
	 * @return the pagopaRicDocSubdocId
	 */
	public Integer getPagopaRicDocSubdocId()
	{
		return pagopaRicDocSubdocId;
	}

	/**
	 * @param pagopaRicDocSubdocId the pagopaRicDocSubdocId to set
	 */
	public void setPagopaRicDocSubdocId(Integer pagopaRicDocSubdocId)
	{
		this.pagopaRicDocSubdocId = pagopaRicDocSubdocId;
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
	 * @return the siacTMovgestTsFin
	 */
	public SiacTMovgestT getSiacTMovgestTsFin()
	{
		return siacTMovgestTsFin;
	}

	/**
	 * @param siacTMovgestTsFin the siacTMovgestTsFin to set
	 */
	public void setSiacTMovgestTsFin(SiacTMovgestT siacTMovgestTsFin)
	{
		this.siacTMovgestTsFin = siacTMovgestTsFin;
	}

	/**
	 * @return the pagopaRicDocStatoElab
	 */
	public String getPagopaRicDocStatoElab()
	{
		return pagopaRicDocStatoElab;
	}

	/**
	 * @param pagopaRicDocStatoElab the pagopaRicDocStatoElab to set
	 */
	public void setPagopaRicDocStatoElab(String pagopaRicDocStatoElab)
	{
		this.pagopaRicDocStatoElab = pagopaRicDocStatoElab;
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
	 * @return the pagopaTRiconciliazione
	 */
	public PagopaTRiconciliazione getPagopaTRiconciliazione()
	{
		return pagopaTRiconciliazione;
	}

	/**
	 * @param pagopaTRiconciliazione the pagopaTRiconciliazione to set
	 */
	public void setPagopaTRiconciliazione(PagopaTRiconciliazione pagopaTRiconciliazione)
	{
		this.pagopaTRiconciliazione = pagopaTRiconciliazione;
	}

	/**
	 * @return the pagopaTElaborazioneFlusso
	 */
	public PagopaTElaborazioneFlusso getPagopaTElaborazioneFlusso()
	{
		return pagopaTElaborazioneFlusso;
	}

	/**
	 * @param pagopaTElaborazioneFlusso the pagopaTElaborazioneFlusso to set
	 */
	public void setPagopaTElaborazioneFlusso(PagopaTElaborazioneFlusso pagopaTElaborazioneFlusso)
	{
		this.pagopaTElaborazioneFlusso = pagopaTElaborazioneFlusso;
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
	 * @return the pagopaRicDocRagsocBenef
	 */
	public String getPagopaRicDocRagsocBenef()
	{
		return pagopaRicDocRagsocBenef;
	}

	/**
	 * @param pagopaRicDocRagsocBenef the pagopaRicDocRagsocBenef to set
	 */
	public void setPagopaRicDocRagsocBenef(String pagopaRicDocRagsocBenef)
	{
		this.pagopaRicDocRagsocBenef = pagopaRicDocRagsocBenef;
	}

	/**
	 * @return the pagopaRicDocNomeBenef
	 */
	public String getPagopaRicDocNomeBenef()
	{
		return pagopaRicDocNomeBenef;
	}

	/**
	 * @param pagopaRicDocNomeBenef the pagopaRicDocNomeBenef to set
	 */
	public void setPagopaRicDocNomeBenef(String pagopaRicDocNomeBenef)
	{
		this.pagopaRicDocNomeBenef = pagopaRicDocNomeBenef;
	}

	/**
	 * @return the pagopaRicDocCognomeBenef
	 */
	public String getPagopaRicDocCognomeBenef()
	{
		return pagopaRicDocCognomeBenef;
	}

	/**
	 * @param pagopaRicDocCognomeBenef the pagopaRicDocCognomeBenef to set
	 */
	public void setPagopaRicDocCognomeBenef(String pagopaRicDocCognomeBenef)
	{
		this.pagopaRicDocCognomeBenef = pagopaRicDocCognomeBenef;
	}

	/**
	 * @return the pagopaRicDocCodfiscBenef
	 */
	public String getPagopaRicDocCodfiscBenef()
	{
		return pagopaRicDocCodfiscBenef;
	}

	/**
	 * @param pagopaRicDocCodfiscBenef the pagopaRicDocCodfiscBenef to set
	 */
	public void setPagopaRicDocCodfiscBenef(String pagopaRicDocCodfiscBenef)
	{
		this.pagopaRicDocCodfiscBenef = pagopaRicDocCodfiscBenef;
	}

	/**
	 * @return the siacTSoggetto
	 */
	public SiacTSoggetto getSiacTSoggetto()
	{
		return siacTSoggetto;
	}

	/**
	 * @param siacTSoggetto the siacTSoggetto to set
	 */
	public void setSiacTSoggetto(SiacTSoggetto siacTSoggetto)
	{
		this.siacTSoggetto = siacTSoggetto;
	}

	/**
	 * @return the pagopaRicDocFlagDett
	 */
	public Boolean getPagopaRicDocFlagDett()
	{
		return pagopaRicDocFlagDett;
	}

	/**
	 * @param pagopaRicDocFlagDett the pagopaRicDocFlagDett to set
	 */
	public void setPagopaRicDocFlagDett(Boolean pagopaRicDocFlagDett)
	{
		this.pagopaRicDocFlagDett = pagopaRicDocFlagDett;
	}

	/**
	 * @return the pagopaRicDocFlagConDett
	 */
	public Boolean getPagopaRicDocFlagConDett()
	{
		return pagopaRicDocFlagConDett;
	}

	/**
	 * @param pagopaRicDocFlagConDett the pagopaRicDocFlagConDett to set
	 */
	public void setPagopaRicDocFlagConDett(Boolean pagopaRicDocFlagConDett)
	{
		this.pagopaRicDocFlagConDett = pagopaRicDocFlagConDett;
	}

	/**
	 * @return the pagopaRicDocTipoCode
	 */
	public String getPagopaRicDocTipoCode()
	{
		return pagopaRicDocTipoCode;
	}

	/**
	 * @param pagopaRicDocTipoCode the pagopaRicDocTipoCode to set
	 */
	public void setPagopaRicDocTipoCode(String pagopaRicDocTipoCode)
	{
		this.pagopaRicDocTipoCode = pagopaRicDocTipoCode;
	}

	/**
	 * @return the siacDDocTipo
	 */
	public SiacDDocTipo getSiacDDocTipo()
	{
		return siacDDocTipo;
	}

	/**
	 * @param siacDDocTipo the siacDDocTipo to set
	 */
	public void setSiacDDocTipo(SiacDDocTipo siacDDocTipo)
	{
		this.siacDDocTipo = siacDDocTipo;
	}

	/**
	 * @return the pagopaRicDetId
	 */
	public Integer getPagopaRicDetId()
	{
		return pagopaRicDetId;
	}

	/**
	 * @param pagopaRicDetId the pagopaRicDetId to set
	 */
	public void setPagopaRicDetId(Integer pagopaRicDetId)
	{
		this.pagopaRicDetId = pagopaRicDetId;
	}

	/**
	 * @return the pagopaRicDocIuv
	 */
	public String getPagopaRicDocIuv()
	{
		return pagopaRicDocIuv;
	}

	/**
	 * @param pagopaRicDocIuv the pagopaRicDocIuv to set
	 */
	public void setPagopaRicDocIuv(String pagopaRicDocIuv)
	{
		this.pagopaRicDocIuv = pagopaRicDocIuv;
	}

	/**
	 * @return the pagopaRicDocFataOperazione
	 */
	public Date getPagopaRicDocFataOperazione()
	{
		return pagopaRicDocFataOperazione;
	}

	/**
	 * @param pagopaRicDocFataOperazione the pagopaRicDocFataOperazione to set
	 */
	public void setPagopaRicDocFataOperazione(Date pagopaRicDocFataOperazione)
	{
		this.pagopaRicDocFataOperazione = pagopaRicDocFataOperazione;
	}

	public Integer getErroreDettaglio() {
		return erroreDettaglio;
	}

	public void setErroreDettaglio(Integer erroreDettaglio) {
		this.erroreDettaglio = erroreDettaglio;
	}

	public String getMessaggioErrore() {
		return messaggioErrore;
	}

	public void setMessaggioErrore(String messaggioErrore) {
		this.messaggioErrore = messaggioErrore;
	}

	//SIAC-8123 CM 29/04/2021 Intervento 2 Inizio
	public Integer getRicDocDettagliElaboratiDiAggregato() {
		return ricDocDettagliElaboratiDiAggregato;
	}

	public void setRicDocDettagliElaboratiDiAggregato(Integer ricDocDettagliElaboratiDiAggregato) {
		this.ricDocDettagliElaboratiDiAggregato = ricDocDettagliElaboratiDiAggregato;
	}
	//SIAC-8123 CM 29/04/2021 Intervento 2 Fine
}