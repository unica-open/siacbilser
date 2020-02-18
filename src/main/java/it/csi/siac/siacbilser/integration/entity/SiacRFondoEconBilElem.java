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
 * The persistent class for the siac_r_fondo_econ_bil_elem database table.
 * 
 */
@Entity
@Table(name="siac_r_fondo_econ_bil_elem")
@NamedQuery(name="SiacRFondoEconBilElem.findAll", query="SELECT s FROM SiacRFondoEconBilElem s")
public class SiacRFondoEconBilElem extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_FONDO_ECON_BIL_ELEM_LIQMOVGESTID_GENERATOR", allocationSize = 1, sequenceName="SIAC_R_FONDO_ECON_BIL_ELEM_LIQ_MOVGEST_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_FONDO_ECON_BIL_ELEM_LIQMOVGESTID_GENERATOR")
	@Column(name="liq_movgest_id")
	private Integer liqMovgestId;

	//bi-directional many-to-one association to SiacTBilElem
	@ManyToOne
	@JoinColumn(name="elem_id")
	private SiacTBilElem siacTBilElem;

	//bi-directional many-to-one association to SiacTFondoEcon
	@ManyToOne
	@JoinColumn(name="fondoecon_id")
	private SiacTFondoEcon siacTFondoEcon;

	public SiacRFondoEconBilElem() {
	}

	public Integer getLiqMovgestId() {
		return this.liqMovgestId;
	}

	public void setLiqMovgestId(Integer liqMovgestId) {
		this.liqMovgestId = liqMovgestId;
	}

	public SiacTBilElem getSiacTBilElem() {
		return this.siacTBilElem;
	}

	public void setSiacTBilElem(SiacTBilElem siacTBilElem) {
		this.siacTBilElem = siacTBilElem;
	}

	public SiacTFondoEcon getSiacTFondoEcon() {
		return this.siacTFondoEcon;
	}

	public void setSiacTFondoEcon(SiacTFondoEcon siacTFondoEcon) {
		this.siacTFondoEcon = siacTFondoEcon;
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