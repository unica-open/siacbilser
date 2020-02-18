/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_r_fondo_econ_movgest database table.
 * 
 */
@Entity
@Table(name="siac_r_fondo_econ_movgest")
@NamedQuery(name="SiacRFondoEconMovgest.findAll", query="SELECT s FROM SiacRFondoEconMovgest s")
public class SiacRFondoEconMovgest extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_FONDO_ECON_MOVGEST_LIQMOVGESTID_GENERATOR", allocationSize = 1, sequenceName="SIAC_R_FONDO_ECON_MOVGEST_LIQ_MOVGEST_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_FONDO_ECON_MOVGEST_LIQMOVGESTID_GENERATOR")
	@Column(name="liq_movgest_id")
	private Integer liqMovgestId;

	//bi-directional many-to-one association to SiacTFondoEcon
	@ManyToOne
	@JoinColumn(name="fondoecon_id")
	private SiacTFondoEcon siacTFondoEcon;

	//bi-directional many-to-one association to SiacTMovgestT
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestT siacTMovgestT;

	public SiacRFondoEconMovgest() {
	}

	public Integer getLiqMovgestId() {
		return this.liqMovgestId;
	}

	public void setLiqMovgestId(Integer liqMovgestId) {
		this.liqMovgestId = liqMovgestId;
	}

	public SiacTFondoEcon getSiacTFondoEcon() {
		return this.siacTFondoEcon;
	}

	public void setSiacTFondoEcon(SiacTFondoEcon siacTFondoEcon) {
		this.siacTFondoEcon = siacTFondoEcon;
	}

	public SiacTMovgestT getSiacTMovgestT() {
		return this.siacTMovgestT;
	}

	public void setSiacTMovgestT(SiacTMovgestT siacTMovgestT) {
		this.siacTMovgestT = siacTMovgestT;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return liqMovgestId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.liqMovgestId = uid;
		
	}


}