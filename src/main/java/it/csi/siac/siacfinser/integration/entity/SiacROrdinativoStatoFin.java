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
 * The persistent class for the siac_r_ordinativo_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_ordinativo_stato")
public class SiacROrdinativoStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_ORDINATIVO_STATO_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_ordinativo_stato_ord_stato_r_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ORDINATIVO_STATO_ID_GENERATOR")
	@Column(name="ord_stato_r_id")
	private Integer ordStatoRId;

	//bi-directional many-to-one association to SiacDOrdinativoStatoFin
	@ManyToOne
	@JoinColumn(name="ord_stato_id")
	private SiacDOrdinativoStatoFin siacDOrdinativoStato;

	//bi-directional many-to-one association to SiacTOrdinativoFin
	@ManyToOne
	@JoinColumn(name="ord_id")
	private SiacTOrdinativoFin siacTOrdinativo;

	public SiacROrdinativoStatoFin() {
	}

	public Integer getOrdStatoRId() {
		return this.ordStatoRId;
	}

	public void setOrdStatoRId(Integer ordStatoRId) {
		this.ordStatoRId = ordStatoRId;
	}

	public SiacDOrdinativoStatoFin getSiacDOrdinativoStato() {
		return this.siacDOrdinativoStato;
	}

	public void setSiacDOrdinativoStato(SiacDOrdinativoStatoFin siacDOrdinativoStato) {
		this.siacDOrdinativoStato = siacDOrdinativoStato;
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
		return this.ordStatoRId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.ordStatoRId = uid;
	}
}