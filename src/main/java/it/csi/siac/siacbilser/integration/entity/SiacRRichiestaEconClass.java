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
 * The persistent class for the siac_r_richiesta_econ_class database table.
 * 
 */
@Entity
@Table(name="siac_r_richiesta_econ_class")
@NamedQuery(name="SiacRRichiestaEconClass.findAll", query="SELECT s FROM SiacRRichiestaEconClass s")
public class SiacRRichiestaEconClass extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_RICHIESTA_ECON_CLASS_RICECONCLASSID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_RICHIESTA_ECON_CLASS_RICECONCLASS_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_RICHIESTA_ECON_CLASS_RICECONCLASSID_GENERATOR")
	@Column(name="riceconclass_id")
	private Integer riceconclassId;

	//bi-directional many-to-one association to SiacTClass
	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClass siacTClass;

	//bi-directional many-to-one association to SiacTRichiestaEcon
	@ManyToOne
	@JoinColumn(name="ricecon_id")
	private SiacTRichiestaEcon siacTRichiestaEcon;

	public SiacRRichiestaEconClass() {
	}

	public Integer getRiceconclassId() {
		return this.riceconclassId;
	}

	public void setRiceconclassId(Integer riceconclassId) {
		this.riceconclassId = riceconclassId;
	}

	public SiacTClass getSiacTClass() {
		return this.siacTClass;
	}

	public void setSiacTClass(SiacTClass siacTClass) {
		this.siacTClass = siacTClass;
	}

	public SiacTRichiestaEcon getSiacTRichiestaEcon() {
		return this.siacTRichiestaEcon;
	}

	public void setSiacTRichiestaEcon(SiacTRichiestaEcon siacTRichiestaEcon) {
		this.siacTRichiestaEcon = siacTRichiestaEcon;
	}
	
	@Override
	public Integer getUid() {
		return riceconclassId;
	}

	@Override
	public void setUid(Integer uid) {
		this.riceconclassId = uid;
	}

}