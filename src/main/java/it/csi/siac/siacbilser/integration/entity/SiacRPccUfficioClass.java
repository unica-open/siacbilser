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
 * The persistent class for the siac_r_pcc_ufficio_class database table.
 * 
 */
@Entity
@Table(name="siac_r_pcc_ufficio_class")
@NamedQuery(name="SiacRPccUfficioClass.findAll", query="SELECT s FROM SiacRPccUfficioClass s")
public class SiacRPccUfficioClass extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_PCC_UFFICIO_CLASS_PCCUFFCLASSIFID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_PCC_UFFICIO_CLASS_PCCUFF_CLASSIF_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_PCC_UFFICIO_CLASS_PCCUFFCLASSIFID_GENERATOR")
	@Column(name="pccuff_classif_id")
	private Integer pccuffClassifId;

	// bi-directional many-to-one association to SiacTClass
	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClass siacTClass;

	//bi-directional many-to-one association to SiacDPccUfficio
	@ManyToOne
	@JoinColumn(name="pccuff_id")
	private SiacDPccUfficio siacDPccUfficio;

	public SiacRPccUfficioClass() {
	}

	public Integer getPccuffClassifId() {
		return this.pccuffClassifId;
	}

	public void setPccuffClassifId(Integer pccuffClassifId) {
		this.pccuffClassifId = pccuffClassifId;
	}

	public SiacTClass getSiacTClass() {
		return siacTClass;
	}

	public void setSiacTClass(SiacTClass siacTClass) {
		this.siacTClass = siacTClass;
	}

	public SiacDPccUfficio getSiacDPccUfficio() {
		return this.siacDPccUfficio;
	}

	public void setSiacDPccUfficio(SiacDPccUfficio siacDPccUfficio) {
		this.siacDPccUfficio = siacDPccUfficio;
	}

	@Override
	public Integer getUid() {
		return getPccuffClassifId();
	}

	@Override
	public void setUid(Integer uid) {
		setPccuffClassifId(uid);
	}

}