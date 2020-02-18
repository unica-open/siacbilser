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
 * The persistent class for the siac_r_ordinativo database table.
 * 
 */
@Entity
@Table(name="siac_r_ordinativo")
public class SiacROrdinativoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_ORDINATIVO_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_ordinativo_ord_r_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ORDINATIVO_ID_GENERATOR")
	@Column(name="ord_r_id")
	private Integer ordRId;

	@Column(name="ord_id")
	private Integer ordId;

	//bi-directional many-to-one association to SiacDRelazTipoFin
	@ManyToOne
	@JoinColumn(name="relaz_tipo_id")
	private SiacDRelazTipoFin siacDRelazTipo;

	//bi-directional many-to-one association to SiacTOrdinativoFin
	@ManyToOne
	@JoinColumn(name="ord_id_da")
	private SiacTOrdinativoFin siacTOrdinativo1;

	//bi-directional many-to-one association to SiacTOrdinativoFin
	@ManyToOne
	@JoinColumn(name="ord_id_a")
	private SiacTOrdinativoFin siacTOrdinativo2;

	public SiacROrdinativoFin() {
	}

	public Integer getOrdRId() {
		return this.ordRId;
	}

	public void setOrdRId(Integer ordRId) {
		this.ordRId = ordRId;
	}

	public Integer getOrdId() {
		return this.ordId;
	}

	public void setOrdId(Integer ordId) {
		this.ordId = ordId;
	}

	public SiacDRelazTipoFin getSiacDRelazTipo() {
		return this.siacDRelazTipo;
	}

	public void setSiacDRelazTipo(SiacDRelazTipoFin siacDRelazTipo) {
		this.siacDRelazTipo = siacDRelazTipo;
	}

	public SiacTOrdinativoFin getSiacTOrdinativo1() {
		return this.siacTOrdinativo1;
	}

	public void setSiacTOrdinativo1(SiacTOrdinativoFin siacTOrdinativo1) {
		this.siacTOrdinativo1 = siacTOrdinativo1;
	}

	public SiacTOrdinativoFin getSiacTOrdinativo2() {
		return this.siacTOrdinativo2;
	}

	public void setSiacTOrdinativo2(SiacTOrdinativoFin siacTOrdinativo2) {
		this.siacTOrdinativo2 = siacTOrdinativo2;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.ordRId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.ordRId = uid;
	}
}