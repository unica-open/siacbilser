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
 * The persistent class for the siac_r_pdce_conto_class database table.
 * 
 */
@Entity
@Table(name="siac_r_pdce_conto_class")
@NamedQuery(name="SiacRPdceContoClass.findAll", query="SELECT s FROM SiacRPdceContoClass s")
public class SiacRPdceContoClass extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_PDCE_CONTO_CLASS_PDCECONTOCLASSIFID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_PDCE_CONTO_CLASS_PDCE_CONTO_CLASSIF_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_PDCE_CONTO_CLASS_PDCECONTOCLASSIFID_GENERATOR")
	@Column(name="pdce_conto_classif_id")
	private Integer pdceContoClassifId;

	//bi-directional many-to-one association to SiacTClass
	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClass siacTClass;

	//bi-directional many-to-one association to SiacTPdceConto
	@ManyToOne
	@JoinColumn(name="pdce_conto_id")
	private SiacTPdceConto siacTPdceConto;

	public SiacRPdceContoClass() {
	}

	public Integer getPdceContoClassifId() {
		return this.pdceContoClassifId;
	}

	public void setPdceContoClassifId(Integer pdceContoClassifId) {
		this.pdceContoClassifId = pdceContoClassifId;
	}

	public SiacTClass getSiacTClass() {
		return this.siacTClass;
	}

	public void setSiacTClass(SiacTClass siacTClass) {
		this.siacTClass = siacTClass;
	}

	public SiacTPdceConto getSiacTPdceConto() {
		return this.siacTPdceConto;
	}

	public void setSiacTPdceConto(SiacTPdceConto siacTPdceConto) {
		this.siacTPdceConto = siacTPdceConto;
	}

	@Override
	public Integer getUid() {
		return pdceContoClassifId;
	}

	@Override
	public void setUid(Integer uid) {
		this.pdceContoClassifId = uid;
	}

}