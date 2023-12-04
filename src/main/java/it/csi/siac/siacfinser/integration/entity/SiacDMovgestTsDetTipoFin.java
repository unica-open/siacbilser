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
 * The persistent class for the siac_d_movgest_ts_det_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_movgest_ts_det_tipo")
public class SiacDMovgestTsDetTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_MOVGEST_TS_DET_TIPO_ID_GENERATOR", allocationSize=1, sequenceName="siac_d_movgest_ts_det_tipo_movgest_ts_det_tipo_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_MOVGEST_TS_DET_TIPO_ID_GENERATOR")
	@Column(name="movgest_ts_det_tipo_id")
	private Integer movgestTsDetTipoId;

	@Column(name="movgest_ts_det_tipo_code")
	private String movgestTsDetTipoCode;

	@Column(name="movgest_ts_det_tipo_desc")
	private String movgestTsDetTipoDesc;

	//bi-directional many-to-one association to SiacTMovgestTsDetFin
	@OneToMany(mappedBy="siacDMovgestTsDetTipo")
	private List<SiacTMovgestTsDetFin> siacTMovgestTsDets;

	//bi-directional many-to-one association to SiacTMovgestTsDetModFin
	@OneToMany(mappedBy="siacDMovgestTsDetTipo")
	private List<SiacTMovgestTsDetModFin> siacTMovgestTsDetMods;

	public SiacDMovgestTsDetTipoFin() {
	}

	public Integer getMovgestTsDetTipoId() {
		return this.movgestTsDetTipoId;
	}

	public void setMovgestTsDetTipoId(Integer movgestTsDetTipoId) {
		this.movgestTsDetTipoId = movgestTsDetTipoId;
	}	

	public String getMovgestTsDetTipoCode() {
		return this.movgestTsDetTipoCode;
	}

	public void setMovgestTsDetTipoCode(String movgestTsDetTipoCode) {
		this.movgestTsDetTipoCode = movgestTsDetTipoCode;
	}

	public String getMovgestTsDetTipoDesc() {
		return this.movgestTsDetTipoDesc;
	}

	public void setMovgestTsDetTipoDesc(String movgestTsDetTipoDesc) {
		this.movgestTsDetTipoDesc = movgestTsDetTipoDesc;
	}	

	public List<SiacTMovgestTsDetFin> getSiacTMovgestTsDets() {
		return this.siacTMovgestTsDets;
	}

	public void setSiacTMovgestTsDets(List<SiacTMovgestTsDetFin> siacTMovgestTsDets) {
		this.siacTMovgestTsDets = siacTMovgestTsDets;
	}

	public SiacTMovgestTsDetFin addSiacTMovgestTsDet(SiacTMovgestTsDetFin siacTMovgestTsDet) {
		getSiacTMovgestTsDets().add(siacTMovgestTsDet);
		siacTMovgestTsDet.setSiacDMovgestTsDetTipo(this);

		return siacTMovgestTsDet;
	}

	public SiacTMovgestTsDetFin removeSiacTMovgestTsDet(SiacTMovgestTsDetFin siacTMovgestTsDet) {
		getSiacTMovgestTsDets().remove(siacTMovgestTsDet);
		siacTMovgestTsDet.setSiacDMovgestTsDetTipo(null);

		return siacTMovgestTsDet;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.movgestTsDetTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.movgestTsDetTipoId = uid;
	}
	
	public List<SiacTMovgestTsDetModFin> getSiacTMovgestTsDetMods() {
		return this.siacTMovgestTsDetMods;
	}

	public void setSiacTMovgestTsDetMods(List<SiacTMovgestTsDetModFin> siacTMovgestTsDetMods) {
		this.siacTMovgestTsDetMods = siacTMovgestTsDetMods;
	}

	public SiacTMovgestTsDetModFin addSiacTMovgestTsDetMod(SiacTMovgestTsDetModFin siacTMovgestTsDetMod) {
		getSiacTMovgestTsDetMods().add(siacTMovgestTsDetMod);
		siacTMovgestTsDetMod.setSiacDMovgestTsDetTipo(this);

		return siacTMovgestTsDetMod;
	}

	public SiacTMovgestTsDetModFin removeSiacTMovgestTsDetMod(SiacTMovgestTsDetModFin siacTMovgestTsDetMod) {
		getSiacTMovgestTsDetMods().remove(siacTMovgestTsDetMod);
		siacTMovgestTsDetMod.setSiacDMovgestTsDetTipo(null);

		return siacTMovgestTsDetMod;
	}
}