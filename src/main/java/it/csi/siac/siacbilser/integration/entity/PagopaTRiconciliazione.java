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

@Entity
@Table(name = "pagopa_t_riconciliazione")
public class PagopaTRiconciliazione extends SiacTEnteBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "PAGOPA_T_RICONCILIAZIONEPAGOPA_RIC_ID_GENERATOR", allocationSize = 1, sequenceName = "PAGOPA_T_RICONCILIAZIONE_PAGOPA_RIC_ID_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAGOPA_T_RICONCILIAZIONEPAGOPA_RIC_ID_GENERATOR")
	@Column(name = "pagopa_ric_id")
	private Integer pagopaRicId;

	@ManyToOne
	@JoinColumn(name="file_pagopa_id")
	private SiacTFilePagopa siacTFilePagopa;
	
	@Column(name = "pagopa_ric_file_id")
	private String pagopaRicFileId;

	@Column(name = "pagopa_ric_file_num_flussi")
	private Integer pagopaRicFileNumFlussi;

	@Column(name = "pagopa_ric_file_ora")
	private Date pagopaRicFileOra;

	@Column(name = "pagopa_ric_flusso_data")
	private Date pagopaRicFlussoData;

	@Column(name = "pagopa_ric_file_ente")
	private String pagopaRicFileEnte;

	@Column(name = "pagopa_ric_flusso_nome_mittente")
	private String pagopaRicFlussoNomeMittente;

	@Column(name = "pagopa_ric_flusso_id")
	private String pagopaRicFlussoId;

	@Column(name = "pagopa_ric_flusso_tot_pagam")
	private BigDecimal pagopaRicFlussoTotPagam;
	
	@Column(name = "pagopa_ric_flusso_anno_esercizio")
	private Integer pagopaRicFlussoAnnoEsercizio;

	@Column(name = "pagopa_ric_flusso_anno_provvisorio")
	private Integer pagopaRicFlussoAnnoProvvisorio;

	@Column(name = "pagopa_ric_flusso_num_provvisorio")
	private Integer pagopaRicFlussoNumProvvisorio;

	@Column(name = "pagopa_ric_flusso_voce_code")
	private String pagopaRicFlussoVoceCode;

	@Column(name = "pagopa_ric_flusso_voce_desc")
	private String pagopaRicFlussoVoceDesc;

	@Column(name = "pagopa_ric_flusso_sottovoce_code")
	private String pagopaRicFlussoSottovoceCode;

	@Column(name = "pagopa_ric_flusso_sottovoce_desc")
	private String pagopaRicFlussoSottovoceDesc;

	@Column(name = "pagopa_ric_flusso_tematica")
	private String pagopaRicFlussoTematica;

	@Column(name = "pagopa_ric_flusso_sottovoce_importo")
	private BigDecimal pagopaRicFlussoSottovoceImporto;

	@Column(name = "pagopa_ric_flusso_num_capitolo")
	private Integer pagopaRicFlussoNumCapitolo;

	@Column(name = "pagopa_ric_flusso_num_articolo")
	private Integer pagopaRicFlussoNumArticolo;

	@Column(name = "pagopa_ric_flusso_pdc_v_fin")
	private String pagopaRicFlussoPdcVFin;

	@Column(name = "pagopa_ric_flusso_anno_accertamento")
	private Integer pagopaRicFlussoAnnoAccertamento;

	@Column(name = "pagopa_ric_flusso_num_accertamento")
	private Integer pagopaRicFlussoNumAccertamento;

	public PagopaTRiconciliazione() {
	}

	@Override
	public Integer getUid() {
		return pagopaRicId;
	}

	@Override
	public void setUid(Integer uid) {
		this.pagopaRicId = uid;
	}

	public Integer getPagopaRicId() {
		return pagopaRicId;
	}

	public void setPagopaRicId(Integer pagopaRicId) {
		this.pagopaRicId = pagopaRicId;
	}

	public Date getPagopaRicFileOra() {
		return pagopaRicFileOra;
	}

	public void setPagopaRicFileOra(Date pagopaRicFileOra) {
		this.pagopaRicFileOra = pagopaRicFileOra;
	}

	public Date getPagopaRicFlussoData() {
		return pagopaRicFlussoData;
	}

	public void setPagopaRicFlussoData(Date pagopaRicFlussoData) {
		this.pagopaRicFlussoData = pagopaRicFlussoData;
	}

	public String getPagopaRicFileEnte() {
		return pagopaRicFileEnte;
	}

	public void setPagopaRicFileEnte(String pagopaRicFileEnte) {
		this.pagopaRicFileEnte = pagopaRicFileEnte;
	}

	public String getPagopaRicFlussoNomeMittente() {
		return pagopaRicFlussoNomeMittente;
	}

	public void setPagopaRicFlussoNomeMittente(String pagopaRicFlussoNomeMittente) {
		this.pagopaRicFlussoNomeMittente = pagopaRicFlussoNomeMittente;
	}

	public String getPagopaRicFlussoId() {
		return pagopaRicFlussoId;
	}

	public void setPagopaRicFlussoId(String pagopaRicFlussoId) {
		this.pagopaRicFlussoId = pagopaRicFlussoId;
	}

	public BigDecimal getPagopaRicFlussoTotPagam() {
		return pagopaRicFlussoTotPagam;
	}

	public void setPagopaRicFlussoTotPagam(BigDecimal pagopaRicFlussoTotPagam) {
		this.pagopaRicFlussoTotPagam = pagopaRicFlussoTotPagam;
	}

	public Integer getPagopaRicFlussoAnnoProvvisorio() {
		return pagopaRicFlussoAnnoProvvisorio;
	}

	public void setPagopaRicFlussoAnnoProvvisorio(Integer pagopaRicFlussoAnnoProvvisorio) {
		this.pagopaRicFlussoAnnoProvvisorio = pagopaRicFlussoAnnoProvvisorio;
	}

	public Integer getPagopaRicFlussoNumProvvisorio() {
		return pagopaRicFlussoNumProvvisorio;
	}

	public void setPagopaRicFlussoNumProvvisorio(Integer pagopaRicFlussoNumProvvisorio) {
		this.pagopaRicFlussoNumProvvisorio = pagopaRicFlussoNumProvvisorio;
	}

	public String getPagopaRicFlussoVoceCode() {
		return pagopaRicFlussoVoceCode;
	}

	public void setPagopaRicFlussoVoceCode(String pagopaRicFlussoVoceCode) {
		this.pagopaRicFlussoVoceCode = pagopaRicFlussoVoceCode;
	}

	public String getPagopaRicFlussoVoceDesc() {
		return pagopaRicFlussoVoceDesc;
	}

	public void setPagopaRicFlussoVoceDesc(String pagopaRicFlussoVoceDesc) {
		this.pagopaRicFlussoVoceDesc = pagopaRicFlussoVoceDesc;
	}

	public String getPagopaRicFlussoSottovoceCode() {
		return pagopaRicFlussoSottovoceCode;
	}

	public void setPagopaRicFlussoSottovoceCode(String pagopaRicFlussoSottovoceCode) {
		this.pagopaRicFlussoSottovoceCode = pagopaRicFlussoSottovoceCode;
	}

	public String getPagopaRicFlussoSottovoceDesc() {
		return pagopaRicFlussoSottovoceDesc;
	}

	public void setPagopaRicFlussoSottovoceDesc(String pagopaRicFlussoSottovoceDesc) {
		this.pagopaRicFlussoSottovoceDesc = pagopaRicFlussoSottovoceDesc;
	}

	public String getPagopaRicFlussoTematica() {
		return pagopaRicFlussoTematica;
	}

	public void setPagopaRicFlussoTematica(String pagopaRicFlussoTematica) {
		this.pagopaRicFlussoTematica = pagopaRicFlussoTematica;
	}

	public BigDecimal getPagopaRicFlussoSottovoceImporto() {
		return pagopaRicFlussoSottovoceImporto;
	}

	public void setPagopaRicFlussoSottovoceImporto(BigDecimal pagopaRicFlussoSottovoceImporto) {
		this.pagopaRicFlussoSottovoceImporto = pagopaRicFlussoSottovoceImporto;
	}


	public String getPagopaRicFlussoPdcVFin() {
		return pagopaRicFlussoPdcVFin;
	}

	public void setPagopaRicFlussoPdcVFin(String pagopaRicFlussoPdcVFin) {
		this.pagopaRicFlussoPdcVFin = pagopaRicFlussoPdcVFin;
	}

	public Integer getPagopaRicFlussoAnnoAccertamento() {
		return pagopaRicFlussoAnnoAccertamento;
	}

	public void setPagopaRicFlussoAnnoAccertamento(Integer pagopaRicFlussoAnnoAccertamento) {
		this.pagopaRicFlussoAnnoAccertamento = pagopaRicFlussoAnnoAccertamento;
	}

	public Integer getPagopaRicFlussoNumAccertamento() {
		return pagopaRicFlussoNumAccertamento;
	}

	public void setPagopaRicFlussoNumAccertamento(Integer pagopaRicFlussoNumAccertamento) {
		this.pagopaRicFlussoNumAccertamento = pagopaRicFlussoNumAccertamento;
	}

	public Integer getPagopaRicFlussoNumCapitolo() {
		return pagopaRicFlussoNumCapitolo;
	}

	public void setPagopaRicFlussoNumCapitolo(Integer pagopaRicFlussoNumCapitolo) {
		this.pagopaRicFlussoNumCapitolo = pagopaRicFlussoNumCapitolo;
	}

	public Integer getPagopaRicFlussoNumArticolo() {
		return pagopaRicFlussoNumArticolo;
	}

	public void setPagopaRicFlussoNumArticolo(Integer pagopaRicFlussoNumArticolo) {
		this.pagopaRicFlussoNumArticolo = pagopaRicFlussoNumArticolo;
	}

	public SiacTFilePagopa getSiacTFilePagopa() {
		return siacTFilePagopa;
	}

	public void setSiacTFilePagopa(SiacTFilePagopa siacTFilePagopa) {
		this.siacTFilePagopa = siacTFilePagopa;
	}

	public String getPagopaRicFileId() {
		return pagopaRicFileId;
	}

	public void setPagopaRicFileId(String pagopaRicFileId) {
		this.pagopaRicFileId = pagopaRicFileId;
	}

	public Integer getPagopaRicFileNumFlussi() {
		return pagopaRicFileNumFlussi;
	}

	public void setPagopaRicFileNumFlussi(Integer pagopaRicFileNumFlussi) {
		this.pagopaRicFileNumFlussi = pagopaRicFileNumFlussi;
	}

	public Integer getPagopaRicFlussoAnnoEsercizio() {
		return pagopaRicFlussoAnnoEsercizio;
	}

	public void setPagopaRicFlussoAnnoEsercizio(Integer pagopaRicFlussoAnnoEsercizio) {
		this.pagopaRicFlussoAnnoEsercizio = pagopaRicFlussoAnnoEsercizio;
	}

}