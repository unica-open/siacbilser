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
 * The persistent class for the siac_r_atto_amm_class database table.
 * 
 */
@Entity
@Table(name="siac_r_prov_cassa_class")
public class SiacRProvCassaClassFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;
	
	/** The atto amm class id. */
	@Id
	@SequenceGenerator(name="SIAC_R_PROV_CASSA_CLASS_PROVCASSACLASSID_GENERATOR", allocationSize=1, sequenceName="siac_r_prov_cassa_class_provc_class_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_PROV_CASSA_CLASS_PROVCASSACLASSID_GENERATOR")
	@Column(name="provc_class_id")
	private Integer provcClassId;

	//bi-directional many-to-one association to SiacTAttoAmmFin
	@ManyToOne
	@JoinColumn(name="provc_id")
	private SiacTProvCassaFin siacTProvCassaFin;

	//bi-directional many-to-one association to SiacTClassFin
	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClassFin siacTClass;

	public SiacRProvCassaClassFin() {
	}

	public SiacTClassFin getSiacTClass() {
		return this.siacTClass;
	}

	public void setSiacTClass(SiacTClassFin siacTClass) {
		this.siacTClass = siacTClass;
	}

	public Integer getProvcClassId() {
		return provcClassId;
	}

	public void setProvcClassId(Integer provcClassId) {
		this.provcClassId = provcClassId;
	}

	public SiacTProvCassaFin getSiacTProvCassaFin() {
		return siacTProvCassaFin;
	}

	public void setSiacTProvCassaFin(SiacTProvCassaFin siacTProvCassaFin) {
		this.siacTProvCassaFin = siacTProvCassaFin;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.provcClassId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.provcClassId = uid;
	}
}