/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

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
@Table(name="siac_t_file_pagopa")
public class SiacTFilePagopa extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_FILEPAGOPA_FILEPAGOPAID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_FILE_PAGOPA_FILE_PAGOPA_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_FILEPAGOPA_FILEPAGOPAID_GENERATOR") 
	@Column(name="file_pagopa_id")
	private Integer filePagopaId;

	@Column(name = "file_pagopa")
	private byte[] filePagopa;

	@Column(name="file_pagopa_code")
	private String filePagopaCode;

	@Column(name="file_pagopa_size")
	private Long filePagopaSize;

	@Column(name="file_pagopa_anno")
	private Integer filePagopaAnno;

	@Column(name="file_pagopa_id_psp")
	private String filePagopaIdPsp;

	@Column(name="file_pagopa_id_flusso")
	private String filePagopaIdFlusso;

	@ManyToOne
	@JoinColumn(name="file_pagopa_stato_id")
	private SiacDFilePagopaStato siacDFilePagopaStato;

	@ManyToOne
	@JoinColumn(name="file_pagopa_errore_id", referencedColumnName="pagopa_ric_errore_id")
	private PagopaDRiconciliazioneErrore pagopaDRiconciliazioneErrore;

	public SiacTFilePagopa() {
	}

	@Override
	public Integer getUid() {
		return filePagopaId;
	}

	@Override
	public void setUid(Integer uid) {
		this.filePagopaId = uid;
	}


	public byte[] getFilePagopa() {
		return filePagopa;
	}


	public void setFilePagopa(byte[] filePagopa) {
		this.filePagopa = filePagopa;
	}


	public Integer getFilePagopaId() {
		return filePagopaId;
	}


	public void setFilePagopaId(Integer filePagopaId) {
		this.filePagopaId = filePagopaId;
	}


	public String getFilePagopaCode() {
		return filePagopaCode;
	}


	public void setFilePagopaCode(String filePagopaCode) {
		this.filePagopaCode = filePagopaCode;
	}


	public Long getFilePagopaSize() {
		return filePagopaSize;
	}


	public void setFilePagopaSize(Long filePagopaSize) {
		this.filePagopaSize = filePagopaSize;
	}

	public SiacDFilePagopaStato getSiacDFilePagopaStato() {
		return siacDFilePagopaStato;
	}

	public void setSiacDFilePagopaStato(SiacDFilePagopaStato siacDFilePagopaStato) {
		this.siacDFilePagopaStato = siacDFilePagopaStato;
	}

	public Integer getFilePagopaAnno() {
		return filePagopaAnno;
	}

	public void setFilePagopaAnno(Integer filePagopaAnno) {
		this.filePagopaAnno = filePagopaAnno;
	}

	public PagopaDRiconciliazioneErrore getPagopaDRiconciliazioneErrore() {
		return pagopaDRiconciliazioneErrore;
	}

	public void setPagopaDRiconciliazioneErrore(PagopaDRiconciliazioneErrore pagopaDRiconciliazioneErrore) {
		this.pagopaDRiconciliazioneErrore = pagopaDRiconciliazioneErrore;
	}

	public String getFilePagopaIdPsp() {
		return filePagopaIdPsp;
	}

	public void setFilePagopaIdPsp(String filePagopaIdPsp) {
		this.filePagopaIdPsp = filePagopaIdPsp;
	}

	public String getFilePagopaIdFlusso() {
		return filePagopaIdFlusso;
	}

	public void setFilePagopaIdFlusso(String filePagopaIdFlusso) {
		this.filePagopaIdFlusso = filePagopaIdFlusso;
	}
}