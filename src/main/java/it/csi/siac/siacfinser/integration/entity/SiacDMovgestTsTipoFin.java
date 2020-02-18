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
 * The persistent class for the siac_d_movgest_ts_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_movgest_ts_tipo")
public class SiacDMovgestTsTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="SIAC_D_MOVGEST_TS_TIPO_ID_GENERATOR", allocationSize=1, sequenceName="siac_d_movgest_ts_tipo_movgest_ts_tipo_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_MOVGEST_TS_TIPO_ID_GENERATOR")	
	@Column(name="movgest_ts_tipo_id")
	private Integer movgestTsTipoId;

	@Column(name="movgest_ts_tipo_code")
	private String movgestTsTipoCode;

	@Column(name="movgest_ts_tipo_desc")
	private String movgestTsTipoDesc;

	//bi-directional many-to-one association to SiacTMovgestT
	@OneToMany(mappedBy="siacDMovgestTsTipo")
	private List<SiacTMovgestTsFin> siacTMovgestTs;

	public SiacDMovgestTsTipoFin() {
	}

	public Integer getMovgestTsTipoId() {
		return this.movgestTsTipoId;
	}

	public void setMovgestTsTipoId(Integer movgestTsTipoId) {
		this.movgestTsTipoId = movgestTsTipoId;
	}

	public String getMovgestTsTipoCode() {
		return this.movgestTsTipoCode;
	}

	public void setMovgestTsTipoCode(String movgestTsTipoCode) {
		this.movgestTsTipoCode = movgestTsTipoCode;
	}

	public String getMovgestTsTipoDesc() {
		return this.movgestTsTipoDesc;
	}

	public void setMovgestTsTipoDesc(String movgestTsTipoDesc) {
		this.movgestTsTipoDesc = movgestTsTipoDesc;
	}

	public List<SiacTMovgestTsFin> getSiacTMovgestTs() {
		return this.siacTMovgestTs;
	}

	public void setSiacTMovgestTs(List<SiacTMovgestTsFin> siacTMovgestTs) {
		this.siacTMovgestTs = siacTMovgestTs;
	}

	public SiacTMovgestTsFin addSiacTMovgestT(SiacTMovgestTsFin siacTMovgestT) {
		getSiacTMovgestTs().add(siacTMovgestT);
		siacTMovgestT.setSiacDMovgestTsTipo(this);

		return siacTMovgestT;
	}

	public SiacTMovgestTsFin removeSiacTMovgestT(SiacTMovgestTsFin siacTMovgestT) {
		getSiacTMovgestTs().remove(siacTMovgestT);
		siacTMovgestT.setSiacDMovgestTsTipo(null);

		return siacTMovgestT;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.movgestTsTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.movgestTsTipoId = uid;
	}
}