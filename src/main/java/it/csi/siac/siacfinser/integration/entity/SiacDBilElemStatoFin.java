/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_d_bil_elem_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_bil_elem_stato")
public class SiacDBilElemStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id	
	@Column(name="elem_stato_id")
	private Integer elemStatoId;

	@Column(name="elem_stato_code")
	private String elemStatoCode;

	@Column(name="elem_stato_desc")
	private String elemStatoDesc;

	//bi-directional many-to-one association to SiacRBilElemStatoFin
	@OneToMany(mappedBy="siacDBilElemStato")
	private List<SiacRBilElemStatoFin> siacRBilElemStatos;

	public SiacDBilElemStatoFin() {
	}

	public Integer getElemStatoId() {
		return this.elemStatoId;
	}

	public void setElemStatoId(Integer elemStatoId) {
		this.elemStatoId = elemStatoId;
	}

	public String getElemStatoCode() {
		return this.elemStatoCode;
	}

	public void setElemStatoCode(String elemStatoCode) {
		this.elemStatoCode = elemStatoCode;
	}

	public String getElemStatoDesc() {
		return this.elemStatoDesc;
	}

	public void setElemStatoDesc(String elemStatoDesc) {
		this.elemStatoDesc = elemStatoDesc;
	}

	public List<SiacRBilElemStatoFin> getSiacRBilElemStatos() {
		return this.siacRBilElemStatos;
	}

	public void setSiacRBilElemStatos(List<SiacRBilElemStatoFin> siacRBilElemStatos) {
		this.siacRBilElemStatos = siacRBilElemStatos;
	}

	public SiacRBilElemStatoFin addSiacRBilElemStato(SiacRBilElemStatoFin siacRBilElemStato) {
		getSiacRBilElemStatos().add(siacRBilElemStato);
		siacRBilElemStato.setSiacDBilElemStato(this);

		return siacRBilElemStato;
	}

	public SiacRBilElemStatoFin removeSiacRBilElemStato(SiacRBilElemStatoFin siacRBilElemStato) {
		getSiacRBilElemStatos().remove(siacRBilElemStato);
		siacRBilElemStato.setSiacDBilElemStato(null);

		return siacRBilElemStato;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.elemStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.elemStatoId = uid;
	}
}