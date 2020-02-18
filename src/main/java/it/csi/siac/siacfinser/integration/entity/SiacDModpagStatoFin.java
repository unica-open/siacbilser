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
 * The persistent class for the siac_d_modpag_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_modpag_stato")
public class SiacDModpagStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="modpag_stato_id")
	private Integer modpagStatoId;

	@Column(name="modpag_stato_code")
	private String modpagStatoCode;

	@Column(name="modpag_stato_desc")
	private String modpagStatoDesc;

	//bi-directional many-to-one association to SiacRModpagStatoFin
	@OneToMany(mappedBy="siacDModpagStato")
	private List<SiacRModpagStatoFin> siacRModpagStatos;

	public SiacDModpagStatoFin() {
	}

	public Integer getModpagStatoId() {
		return this.modpagStatoId;
	}

	public void setModpagStatoId(Integer modpagStatoId) {
		this.modpagStatoId = modpagStatoId;
	}

	public String getModpagStatoCode() {
		return this.modpagStatoCode;
	}

	public void setModpagStatoCode(String modpagStatoCode) {
		this.modpagStatoCode = modpagStatoCode;
	}

	public String getModpagStatoDesc() {
		return this.modpagStatoDesc;
	}

	public void setModpagStatoDesc(String modpagStatoDesc) {
		this.modpagStatoDesc = modpagStatoDesc;
	}

	public List<SiacRModpagStatoFin> getSiacRModpagStatos() {
		return this.siacRModpagStatos;
	}

	public void setSiacRModpagStatos(List<SiacRModpagStatoFin> siacRModpagStatos) {
		this.siacRModpagStatos = siacRModpagStatos;
	}

	public SiacRModpagStatoFin addSiacRModpagStato(SiacRModpagStatoFin siacRModpagStato) {
		getSiacRModpagStatos().add(siacRModpagStato);
		siacRModpagStato.setSiacDModpagStato(this);

		return siacRModpagStato;
	}

	public SiacRModpagStatoFin removeSiacRModpagStato(SiacRModpagStatoFin siacRModpagStato) {
		getSiacRModpagStatos().remove(siacRModpagStato);
		siacRModpagStato.setSiacDModpagStato(null);

		return siacRModpagStato;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.modpagStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.modpagStatoId = uid;
	}

}