/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_d_movgest_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_movgest_tipo")
public class SiacDMovgestTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="movgest_tipo_id")
	private Integer movgestTipoId;

	@Column(name="movgest_tipo_code")
	private String movgestTipoCode;

	@Column(name="movgest_tipo_desc")
	private String movgestTipoDesc;

	//bi-directional many-to-one association to SiacTMovgestFin
	@OneToMany(mappedBy="siacDMovgestTipo")
	private List<SiacTMovgestFin> siacTMovgests;
	
	
	@OneToMany(mappedBy = "tipoMovimentoGestione")
	private Set<SiacRMovgestTipoClassTipFin> tipoMovimentoGestione;
	

	public SiacDMovgestTipoFin() {
	}

	public Integer getMovgestTipoId() {
		return this.movgestTipoId;
	}

	public void setMovgestTipoId(Integer movgestTipoId) {
		this.movgestTipoId = movgestTipoId;
	}

	public String getMovgestTipoCode() {
		return this.movgestTipoCode;
	}

	public void setMovgestTipoCode(String movgestTipoCode) {
		this.movgestTipoCode = movgestTipoCode;
	}

	public String getMovgestTipoDesc() {
		return this.movgestTipoDesc;
	}

	public void setMovgestTipoDesc(String movgestTipoDesc) {
		this.movgestTipoDesc = movgestTipoDesc;
	}

	public List<SiacTMovgestFin> getSiacTMovgests() {
		return this.siacTMovgests;
	}

	public void setSiacTMovgests(List<SiacTMovgestFin> siacTMovgests) {
		this.siacTMovgests = siacTMovgests;
	}

	public SiacTMovgestFin addSiacTMovgest(SiacTMovgestFin siacTMovgest) {
		getSiacTMovgests().add(siacTMovgest);
		siacTMovgest.setSiacDMovgestTipo(this);

		return siacTMovgest;
	}

	public SiacTMovgestFin removeSiacTMovgest(SiacTMovgestFin siacTMovgest) {
		getSiacTMovgests().remove(siacTMovgest);
		siacTMovgest.setSiacDMovgestTipo(null);

		return siacTMovgest;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.movgestTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.movgestTipoId = uid;
	}

	public Set<SiacRMovgestTipoClassTipFin> getTipoMovimentoGestione() {
		return tipoMovimentoGestione;
	}

	public void setTipoMovimentoGestione(
			Set<SiacRMovgestTipoClassTipFin> tipoMovimentoGestione) {
		this.tipoMovimentoGestione = tipoMovimentoGestione;
	}

}