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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name="siac_d_file_pagopa_stato")
public class SiacDFilePagopaStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_FILE_PAGOPA_STATO_FILEPAGOPASTATOID_GENERATOR", allocationSize=1, 
						sequenceName="SIAC_D_FILE_PAGOPA_STATO_FILE_PAGOPA_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_FILE_PAGOPA_STATO_FILEPAGOPASTATOID_GENERATOR")
	@Column(name="file_pagopa_stato_id")
	private Integer filePagopaStatoId;

	@Column(name="file_pagopa_stato_code")
	private String filePagopaStatoCode;

	@Column(name="file_pagopa_stato_desc")
	private String filePagopaStatoDesc;

	public SiacDFilePagopaStato() {
	}

	@Override
	public Integer getUid() {
		return filePagopaStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.filePagopaStatoId = uid;
	}

	public String getFilePagopaStatoCode() {
		return filePagopaStatoCode;
	}

	public void setFilePagopaStatoCode(String filePagopaStatoCode) {
		this.filePagopaStatoCode = filePagopaStatoCode;
	}

	public String getFilePagopaStatoDesc() {
		return filePagopaStatoDesc;
	}

	public void setFilePagopaStatoDesc(String filePagopaStatoDesc) {
		this.filePagopaStatoDesc = filePagopaStatoDesc;
	}

	public Integer getFilePagopaStatoId() {
		return filePagopaStatoId;
	}

	public void setFilePagopaStatoId(Integer filePagopaStatoId) {
		this.filePagopaStatoId = filePagopaStatoId;
	}

	public String getCodiceDescrizione() {
		return String.format("%s - %s", filePagopaStatoCode, filePagopaStatoDesc);
	}

}