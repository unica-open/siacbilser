/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_d_siope_assenza_motivazione database table.
 * 
 */
@Entity
@Table(name="siac_d_siope_assenza_motivazione")
public class SiacDSiopeAssenzaMotivazioneFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="siope_assenza_motivazione_id")
	private Integer siopeAssenzaMotivazioneId;

	@Column(name="siope_assenza_motivazione_code")
	private String siopeAssenzaMotivazioneCode;

	@Column(name="siope_assenza_motivazione_desc")
	private String siopeAssenzaMotivazioneDesc;
	
	public Integer getSiopeAssenzaMotivazioneId() {
		return siopeAssenzaMotivazioneId;
	}

	public void setSiopeAssenzaMotivazioneId(Integer siopeAssenzaMotivazioneId) {
		this.siopeAssenzaMotivazioneId = siopeAssenzaMotivazioneId;
	}

	public String getSiopeAssenzaMotivazioneCode() {
		return siopeAssenzaMotivazioneCode;
	}

	public void setSiopeAssenzaMotivazioneCode(String siopeAssenzaMotivazioneCode) {
		this.siopeAssenzaMotivazioneCode = siopeAssenzaMotivazioneCode;
	}

	public String getSiopeAssenzaMotivazioneDesc() {
		return siopeAssenzaMotivazioneDesc;
	}

	public void setSiopeAssenzaMotivazioneDesc(String siopeAssenzaMotivazioneDesc) {
		this.siopeAssenzaMotivazioneDesc = siopeAssenzaMotivazioneDesc;
	}

	@Override
	public Integer getUid() {
		return this.siopeAssenzaMotivazioneId;
	}

	@Override
	public void setUid(Integer uid) {
		this.siopeAssenzaMotivazioneId = uid;
	}
}