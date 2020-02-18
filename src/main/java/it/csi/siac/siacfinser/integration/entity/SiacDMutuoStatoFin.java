/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_d_mutuo_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_mutuo_stato")
public class SiacDMutuoStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_MUTUO_STATO_MUTUO_STATO_ID_GENERATOR", allocationSize=1, sequenceName="siac_d_mutuo_stato_mut_stato_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_MUTUO_STATO_MUTUO_STATO_ID_GENERATOR")
	@Column(name="mut_stato_id")
	private Integer mutStatoId;

	@Column(name="mut_stato_code")
	private String mutStatoCode;

	@Column(name="mut_stato_desc")
	private String mutStatoDesc;

	//bi-directional many-to-one association to SiacRMutoStato
	@OneToMany(mappedBy="siacDMutuoStato")
	private List<SiacRMutuoStatoFin> siacRMutuoStatos;

	public SiacDMutuoStatoFin() {
	}

	public Integer getMutStatoId() {
		return this.mutStatoId;
	}

	public void setMutStatoId(Integer mutStatoId) {
		this.mutStatoId = mutStatoId;
	}

	public String getMutStatoCode() {
		return this.mutStatoCode;
	}

	public void setMutStatoCode(String mutStatoCode) {
		this.mutStatoCode = mutStatoCode;
	}

	public String getMutStatoDesc() {
		return this.mutStatoDesc;
	}

	public void setMutStatoDesc(String mutStatoDesc) {
		this.mutStatoDesc = mutStatoDesc;
	}

	public List<SiacRMutuoStatoFin> getSiacRMutuoStatos() {
		return this.siacRMutuoStatos;
	}

	public void setSiacRMutuoStatos(List<SiacRMutuoStatoFin> siacRMutuoStatos) {
		this.siacRMutuoStatos = siacRMutuoStatos;
	}

	public SiacRMutuoStatoFin addSiacRMutoStato(SiacRMutuoStatoFin siacRMutuoStato) {
		getSiacRMutuoStatos().add(siacRMutuoStato);
		siacRMutuoStato.setSiacDMutuoStato(this);

		return siacRMutuoStato;
	}

	public SiacRMutuoStatoFin removeSiacRMutoStato(SiacRMutuoStatoFin siacRMutuoStato) {
		getSiacRMutuoStatos().remove(siacRMutuoStato);
		siacRMutuoStato.setSiacDMutuoStato(null);

		return siacRMutuoStato;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.mutStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.mutStatoId = uid;
	}
}