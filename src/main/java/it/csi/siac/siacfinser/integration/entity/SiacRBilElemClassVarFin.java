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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_r_bil_elem_class_var database table.
 * 
 */
@Entity
@Table(name="siac_r_bil_elem_class_var")
public class SiacRBilElemClassVarFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_BIL_ELEM_CLASS_VAR_ELEM_CLASS_VAR_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_bil_elem_class_var_elem_class_var_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_BIL_ELEM_CLASS_VAR_ELEM_CLASS_VAR_ID_GENERATOR")
	@Column(name="elem_class_var_id")
	private Integer elemClassVarId;

	//bi-directional many-to-one association to SiacRBilElemClassFin
	@ManyToOne
	@JoinColumn(name="elem_classif_id")
	private SiacRBilElemClassFin siacRBilElemClass;

	//bi-directional many-to-one association to SiacRVariazioneStatoFin
	@ManyToOne
	@JoinColumn(name="variazione_stato_id")
	private SiacRVariazioneStatoFin siacRVariazioneStato;

	//bi-directional many-to-one association to SiacTBilElemFin
	@ManyToOne
	@JoinColumn(name="elem_id")
	private SiacTBilElemFin siacTBilElem;

	//bi-directional many-to-one association to SiacTClassFin
	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClassFin siacTClass;

	public SiacRBilElemClassVarFin() {
	}

	public Integer getElemClassVarId() {
		return this.elemClassVarId;
	}

	public void setElemClassVarId(Integer elemClassVarId) {
		this.elemClassVarId = elemClassVarId;
	}

	public SiacRBilElemClassFin getSiacRBilElemClass() {
		return this.siacRBilElemClass;
	}

	public void setSiacRBilElemClass(SiacRBilElemClassFin siacRBilElemClass) {
		this.siacRBilElemClass = siacRBilElemClass;
	}

	public SiacRVariazioneStatoFin getSiacRVariazioneStato() {
		return this.siacRVariazioneStato;
	}

	public void setSiacRVariazioneStato(SiacRVariazioneStatoFin siacRVariazioneStato) {
		this.siacRVariazioneStato = siacRVariazioneStato;
	}

	public SiacTBilElemFin getSiacTBilElem() {
		return this.siacTBilElem;
	}

	public void setSiacTBilElem(SiacTBilElemFin siacTBilElem) {
		this.siacTBilElem = siacTBilElem;
	}

	public SiacTClassFin getSiacTClass() {
		return this.siacTClass;
	}

	public void setSiacTClass(SiacTClassFin siacTClass) {
		this.siacTClass = siacTClass;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.elemClassVarId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.elemClassVarId = uid;
	}
}