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
 * The persistent class for the siac_r_pcc_codice_class database table.
 * 
 */
@Entity
@Table(name="siac_r_pcc_codice_class")
@NamedQuery(name="SiacRPccCodiceClass.findAll", query="SELECT s FROM SiacRPccCodiceClass s")
public class SiacRPccCodiceClass extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_PCC_CODICE_CLASS_PCCCODCLASSIFID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_PCC_CODICE_CLASS_PCCCOD_CLASSIF_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_PCC_CODICE_CLASS_PCCCODCLASSIFID_GENERATOR")
	@Column(name="pcccod_classif_id")
	private Integer pcccodClassifId;

	// bi-directional many-to-one association to SiacTClass
	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClass siacTClass;
	
	//bi-directional many-to-one association to SiacDPccCodice
	@ManyToOne
	@JoinColumn(name="pcccod_id")
	private SiacDPccCodice siacDPccCodice;

	public SiacRPccCodiceClass() {
	}

	public Integer getPcccodClassifId() {
		return this.pcccodClassifId;
	}

	public void setPcccodClassifId(Integer pcccodClassifId) {
		this.pcccodClassifId = pcccodClassifId;
	}

	public SiacTClass getSiacTClass() {
		return siacTClass;
	}

	public void setSiacTClass(SiacTClass siacTClass) {
		this.siacTClass = siacTClass;
	}

	public SiacDPccCodice getSiacDPccCodice() {
		return this.siacDPccCodice;
	}

	public void setSiacDPccCodice(SiacDPccCodice siacDPccCodice) {
		this.siacDPccCodice = siacDPccCodice;
	}

	@Override
	public Integer getUid() {
		return getPcccodClassifId();
	}

	@Override
	public void setUid(Integer uid) {
		setPcccodClassifId(uid);
	}

}