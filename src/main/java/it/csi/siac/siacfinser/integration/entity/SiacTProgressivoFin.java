/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_t_progressivo database table.
 * 
 */
@Entity
@Table(name="siac_t_progressivo")
public class SiacTProgressivoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_PROGRESSIVO_PROG_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_progressivo_prog_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_PROGRESSIVO_PROG_ID_GENERATOR")
	@Column(name="prog_id")
	private Integer progId;

	@Column(name="prog_key")
	private String progKey;

	@Column(name="prog_value")
	private Long progValue;
	
	@Column(name="ambito_id")
	private Integer ambitoId;

	public Integer getProgId() {
		return progId;
	}

	public void setProgId(Integer progId) {
		this.progId = progId;
	}

	public String getProgKey() {
		return progKey;
	}

	public void setProgKey(String progKey) {
		this.progKey = progKey;
	}

	public Long getProgValue() {
		return progValue;
	}

	public void setProgValue(Long progValue) {
		this.progValue = progValue;
	}

	public Integer getAmbitoId() {
		return ambitoId;
	}

	public void setAmbitoId(Integer ambitoId) {
		this.ambitoId = ambitoId;
	}

	@Override
	public Integer getUid() {
		return getProgId();
	}

	@Override
	public void setUid(Integer uid) {
		setProgId(uid);
	}
}