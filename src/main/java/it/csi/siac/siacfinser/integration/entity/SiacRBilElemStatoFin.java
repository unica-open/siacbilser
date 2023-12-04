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
 * The persistent class for the siac_r_bil_elem_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_bil_elem_stato")
public class SiacRBilElemStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_BIL_ELEM_STATO_BIL_ELEM_STATO_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_bil_elem_stato_bil_elem_stato_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_BIL_ELEM_STATO_BIL_ELEM_STATO_ID_GENERATOR")
	@Column(name="bil_elem_stato_id")
	private Integer bilElemStatoId;

	//bi-directional many-to-one association to SiacDBilElemStatoFin
	@ManyToOne
	@JoinColumn(name="elem_stato_id")
	private SiacDBilElemStatoFin siacDBilElemStato;

	//bi-directional many-to-one association to SiacTBilElemFin
	@ManyToOne
	@JoinColumn(name="elem_id")
	private SiacTBilElemFin siacTBilElem;

	public SiacRBilElemStatoFin() {
	}

	public Integer getBilElemStatoId() {
		return this.bilElemStatoId;
	}

	public void setBilElemStatoId(Integer bilElemStatoId) {
		this.bilElemStatoId = bilElemStatoId;
	}

	public SiacDBilElemStatoFin getSiacDBilElemStato() {
		return this.siacDBilElemStato;
	}

	public void setSiacDBilElemStato(SiacDBilElemStatoFin siacDBilElemStato) {
		this.siacDBilElemStato = siacDBilElemStato;
	}

	public SiacTBilElemFin getSiacTBilElem() {
		return this.siacTBilElem;
	}

	public void setSiacTBilElem(SiacTBilElemFin siacTBilElem) {
		this.siacTBilElem = siacTBilElem;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.bilElemStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.bilElemStatoId = uid;
	}
}