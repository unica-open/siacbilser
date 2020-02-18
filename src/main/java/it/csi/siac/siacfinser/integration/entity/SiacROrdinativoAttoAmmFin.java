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
 * The persistent class for the siac_r_ordinativo_atto_amm database table.
 * 
 */
@Entity
@Table(name="siac_r_ordinativo_atto_amm")
public class SiacROrdinativoAttoAmmFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_ORDINATIVO_ATTO_AMM_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_ordinativo_atto_amm_ord_amm_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ORDINATIVO_ATTO_AMM_ID_GENERATOR")
	@Column(name="ord_amm_id")
	private Integer ordAmmId;

	//bi-directional many-to-one association to SiacTAttoAmmFin
	@ManyToOne
	@JoinColumn(name="attoamm_id")
	private SiacTAttoAmmFin siacTAttoAmm;

	//bi-directional many-to-one association to SiacTOrdinativoFin
	@ManyToOne
	@JoinColumn(name="ord_id")
	private SiacTOrdinativoFin siacTOrdinativo;

	public SiacROrdinativoAttoAmmFin() {
	}

	public Integer getOrdAmmId() {
		return this.ordAmmId;
	}

	public void setOrdAmmId(Integer ordAmmId) {
		this.ordAmmId = ordAmmId;
	}

	public SiacTAttoAmmFin getSiacTAttoAmm() {
		return this.siacTAttoAmm;
	}

	public void setSiacTAttoAmm(SiacTAttoAmmFin siacTAttoAmm) {
		this.siacTAttoAmm = siacTAttoAmm;
	}

	public SiacTOrdinativoFin getSiacTOrdinativo() {
		return this.siacTOrdinativo;
	}

	public void setSiacTOrdinativo(SiacTOrdinativoFin siacTOrdinativo) {
		this.siacTOrdinativo = siacTOrdinativo;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.ordAmmId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.ordAmmId = uid;
	}

}