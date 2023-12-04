/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_t_movgest_ts_det database table.
 * 
 */
@Entity
@Table(name="siac_t_movgest_ts_det")
public class SiacTMovgestTsDetFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_MOVGEST_TS_DET_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_movgest_ts_det_movgest_ts_det_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_MOVGEST_TS_DET_ID_GENERATOR")
	@Column(name="movgest_ts_det_id")
	private Integer movgestTsDetId;

	@Column(name="movgest_ts_det_importo")
	private BigDecimal movgestTsDetImporto;

	//bi-directional many-to-one association to SiacDMovgestTsDetTipoFin
	@ManyToOne
	@JoinColumn(name="movgest_ts_det_tipo_id")
	private SiacDMovgestTsDetTipoFin siacDMovgestTsDetTipo;

	//bi-directional many-to-one association to SiacTMovgestT
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestTsFin siacTMovgestT;

	//bi-directional many-to-one association to SiacTMovgestTsDetModFin
	@OneToMany(mappedBy="siacTMovgestTsDet")
	private List<SiacTMovgestTsDetModFin> siacTMovgestTsDetMods;
	
	public SiacTMovgestTsDetFin() {
	}

	public Integer getMovgestTsDetId() {
		return this.movgestTsDetId;
	}

	public void setMovgestTsDetId(Integer movgestTsDetId) {
		this.movgestTsDetId = movgestTsDetId;
	}

	public BigDecimal getMovgestTsDetImporto() {
		return this.movgestTsDetImporto;
	}

	public void setMovgestTsDetImporto(BigDecimal movgestTsDetImporto) {
		this.movgestTsDetImporto = movgestTsDetImporto;
	}

	public SiacDMovgestTsDetTipoFin getSiacDMovgestTsDetTipo() {
		return this.siacDMovgestTsDetTipo;
	}

	public void setSiacDMovgestTsDetTipo(SiacDMovgestTsDetTipoFin siacDMovgestTsDetTipo) {
		this.siacDMovgestTsDetTipo = siacDMovgestTsDetTipo;
	}

	public SiacTMovgestTsFin getSiacTMovgestT() {
		return this.siacTMovgestT;
	}

	public void setSiacTMovgestT(SiacTMovgestTsFin siacTMovgestT) {
		this.siacTMovgestT = siacTMovgestT;
	}

	public List<SiacTMovgestTsDetModFin> getSiacTMovgestTsDetMods() {
		return this.siacTMovgestTsDetMods;
	}

	public void setSiacTMovgestTsDetMods(List<SiacTMovgestTsDetModFin> siacTMovgestTsDetMods) {
		this.siacTMovgestTsDetMods = siacTMovgestTsDetMods;
	}

	public SiacTMovgestTsDetModFin addSiacTMovgestTsDetMod(SiacTMovgestTsDetModFin siacTMovgestTsDetMod) {
		getSiacTMovgestTsDetMods().add(siacTMovgestTsDetMod);
		siacTMovgestTsDetMod.setSiacTMovgestTsDet(this);

		return siacTMovgestTsDetMod;
	}

	public SiacTMovgestTsDetModFin removeSiacTMovgestTsDetMod(SiacTMovgestTsDetModFin siacTMovgestTsDetMod) {
		getSiacTMovgestTsDetMods().remove(siacTMovgestTsDetMod);
		siacTMovgestTsDetMod.setSiacTMovgestTsDet(null);

		return siacTMovgestTsDetMod;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.movgestTsDetId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.movgestTsDetId = uid;
	}
}