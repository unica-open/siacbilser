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
 * The persistent class for the siac_d_doc_gruppo database table.
 * 
 */
@Entity
@Table(name="siac_d_doc_gruppo")
public class SiacDDocGruppoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="doc_gruppo_tipo_id")
	private Integer docGruppoTipoId;

	@Column(name="doc_gruppo_tipo_code")
	private String docGruppoTipoCode;

	@Column(name="doc_gruppo_tipo_desc")
	private String docGruppoTipoDesc;

	//bi-directional many-to-one association to SiacDDocTipoFin
	@OneToMany(mappedBy="siacDDocGruppo")
	private List<SiacDDocTipoFin> siacDDocTipos;

	public SiacDDocGruppoFin() {
	}

	public Integer getDocGruppoTipoId() {
		return this.docGruppoTipoId;
	}

	public void setDocGruppoTipoId(Integer docGruppoTipoId) {
		this.docGruppoTipoId = docGruppoTipoId;
	}

	public String getDocGruppoTipoCode() {
		return this.docGruppoTipoCode;
	}

	public void setDocGruppoTipoCode(String docGruppoTipoCode) {
		this.docGruppoTipoCode = docGruppoTipoCode;
	}

	public String getDocGruppoTipoDesc() {
		return this.docGruppoTipoDesc;
	}

	public void setDocGruppoTipoDesc(String docGruppoTipoDesc) {
		this.docGruppoTipoDesc = docGruppoTipoDesc;
	}

	public List<SiacDDocTipoFin> getSiacDDocTipos() {
		return this.siacDDocTipos;
	}

	public void setSiacDDocTipos(List<SiacDDocTipoFin> siacDDocTipos) {
		this.siacDDocTipos = siacDDocTipos;
	}

	public SiacDDocTipoFin addSiacDDocTipo(SiacDDocTipoFin siacDDocTipo) {
		getSiacDDocTipos().add(siacDDocTipo);
		siacDDocTipo.setSiacDDocGruppo(this);

		return siacDDocTipo;
	}

	public SiacDDocTipoFin removeSiacDDocTipo(SiacDDocTipoFin siacDDocTipo) {
		getSiacDDocTipos().remove(siacDDocTipo);
		siacDDocTipo.setSiacDDocGruppo(null);

		return siacDDocTipo;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.docGruppoTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.docGruppoTipoId = uid;
	}

}