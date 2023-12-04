/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.io.Serializable;

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
@Table(name = "pagopa_r_elaborazione_file")
public class PagopaRElaborazioneFile extends SiacTEnteBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "PAGOPA_R_ELABORAZIONEFILEPAGOPA_R_ELAB_ID_GENERATOR", allocationSize = 1, sequenceName = "PAGOPA_R_ELABORAZIONE_FILE_PAGOPA_R_ELAB_ID_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAGOPA_R_ELABORAZIONEFILEPAGOPA_R_ELAB_ID_GENERATOR")
	@Column(name = "pagopa_r_elab_id")
	private Integer pagopaRElabId;
		    
	
	@ManyToOne
	@JoinColumn(name="pagopa_elab_id")
	private PagopaTElaborazione pagopaTElaborazione;
	
	
	@ManyToOne
	@JoinColumn(name="file_pagopa_id")
	private SiacTFilePagopa siacTFilePagopa;
	
	 

	public PagopaRElaborazioneFile() {
	}

	@Override
	public Integer getUid() {
		return pagopaRElabId;
	}

	@Override
	public void setUid(Integer uid) {
		this.pagopaRElabId = uid;
	}
	 
}