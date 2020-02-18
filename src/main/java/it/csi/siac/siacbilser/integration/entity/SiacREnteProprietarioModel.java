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
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_r_ente_proprietario_model database table.
 * 
 */
@Entity
@Table(name="siac_r_ente_proprietario_model")
@NamedQuery(name="SiacREnteProprietarioModel.findAll", query="SELECT s FROM SiacREnteProprietarioModel s")
public class SiacREnteProprietarioModel extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_ENTE_PROPRIETARIO_MODEL_OPMODRID_GENERATOR", allocationSize = 1, sequenceName="SIAC_R_ENTE_PROPRIETARIO_MODEL_OPMOD_R_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ENTE_PROPRIETARIO_MODEL_OPMODRID_GENERATOR")
	@Column(name="opmod_r_id")
	private Integer opmodRId;

	//bi-directional many-to-one association to SiacDEnteProprietarioTipo
	@ManyToOne
	@JoinColumn(name="eptipo_id")
	private SiacDEnteProprietarioTipo siacDEnteProprietarioTipo;

	public SiacREnteProprietarioModel() {
	}

	public Integer getOpmodRId() {
		return this.opmodRId;
	}

	public void setOpmodRId(Integer opmodRId) {
		this.opmodRId = opmodRId;
	}

	public SiacDEnteProprietarioTipo getSiacDEnteProprietarioTipo() {
		return this.siacDEnteProprietarioTipo;
	}

	public void setSiacDEnteProprietarioTipo(SiacDEnteProprietarioTipo siacDEnteProprietarioTipo) {
		this.siacDEnteProprietarioTipo = siacDEnteProprietarioTipo;
	}

	@Override
	public Integer getUid() {
		return opmodRId;
	}

	@Override
	public void setUid(Integer uid) {
		opmodRId = uid;
	}

}