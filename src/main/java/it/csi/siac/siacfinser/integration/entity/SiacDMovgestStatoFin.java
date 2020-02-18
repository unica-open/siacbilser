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
 * The persistent class for the siac_d_movgest_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_movgest_stato")
public class SiacDMovgestStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_MOVGEST_STATO_ID_GENERATOR", allocationSize=1, sequenceName="siac_d_movgest_stato_movgest_stato_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_MOVGEST_STATO_ID_GENERATOR")
	@Column(name="movgest_stato_id")
	private Integer movgestStatoId;

	@Column(name="movgest_stato_code")
	private String movgestStatoCode;

	@Column(name="movgest_stato_desc")
	private String movgestStatoDesc;

	//bi-directional many-to-one association to SiacRMovgestStatoFin
	@OneToMany(mappedBy="siacDMovgestStato")
	private List<SiacRMovgestStatoFin> siacRMovgestStatos;

	public SiacDMovgestStatoFin() {
	}

	public Integer getMovgestStatoId() {
		return this.movgestStatoId;
	}

	public void setMovgestStatoId(Integer movgestStatoId) {
		this.movgestStatoId = movgestStatoId;
	}

	public String getMovgestStatoCode() {
		return this.movgestStatoCode;
	}

	public void setMovgestStatoCode(String movgestStatoCode) {
		this.movgestStatoCode = movgestStatoCode;
	}

	public String getMovgestStatoDesc() {
		return this.movgestStatoDesc;
	}

	public void setMovgestStatoDesc(String movgestStatoDesc) {
		this.movgestStatoDesc = movgestStatoDesc;
	}

	public List<SiacRMovgestStatoFin> getSiacRMovgestStatos() {
		return this.siacRMovgestStatos;
	}

	public void setSiacRMovgestStatos(List<SiacRMovgestStatoFin> siacRMovgestStatos) {
		this.siacRMovgestStatos = siacRMovgestStatos;
	}

	public SiacRMovgestStatoFin addSiacRMovgestStato(SiacRMovgestStatoFin siacRMovgestStato) {
		getSiacRMovgestStatos().add(siacRMovgestStato);
		siacRMovgestStato.setSiacDMovgestStato(this);

		return siacRMovgestStato;
	}

	public SiacRMovgestStatoFin removeSiacRMovgestStato(SiacRMovgestStatoFin siacRMovgestStato) {
		getSiacRMovgestStatos().remove(siacRMovgestStato);
		siacRMovgestStato.setSiacDMovgestStato(null);

		return siacRMovgestStato;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.movgestStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.movgestStatoId = uid;
	}
}