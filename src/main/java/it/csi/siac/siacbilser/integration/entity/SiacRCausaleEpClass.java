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
 * The persistent class for the siac_r_causale_ep_class database table.
 * 
 */
@Entity
@Table(name="siac_r_causale_ep_class")
@NamedQuery(name="SiacRCausaleEpClass.findAll", query="SELECT s FROM SiacRCausaleEpClass s")
public class SiacRCausaleEpClass extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CAUSALE_EP_CLASS_CAUSALEEPCLASSIFID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CAUSALE_EP_CLASS_CAUSALE_EP_CLASSIF_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CAUSALE_EP_CLASS_CAUSALEEPCLASSIFID_GENERATOR")
	@Column(name="causale_ep_classif_id")
	private Integer causaleEpClassifId;

	//bi-directional many-to-one association to SiacTCausaleEp
	@ManyToOne
	@JoinColumn(name="causale_ep_id")
	private SiacTCausaleEp siacTCausaleEp;

	//bi-directional many-to-one association to SiacTClass
	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClass siacTClass;

	public SiacRCausaleEpClass() {
	}

	public Integer getCausaleEpClassifId() {
		return this.causaleEpClassifId;
	}

	public void setCausaleEpClassifId(Integer causaleEpClassifId) {
		this.causaleEpClassifId = causaleEpClassifId;
	}


	public SiacTCausaleEp getSiacTCausaleEp() {
		return this.siacTCausaleEp;
	}

	public void setSiacTCausaleEp(SiacTCausaleEp siacTCausaleEp) {
		this.siacTCausaleEp = siacTCausaleEp;
	}

	public SiacTClass getSiacTClass() {
		return this.siacTClass;
	}

	public void setSiacTClass(SiacTClass siacTClass) {
		this.siacTClass = siacTClass;
	}

	@Override
	public Integer getUid() {
		return causaleEpClassifId;
	}

	@Override
	public void setUid(Integer uid) {
		this.causaleEpClassifId = uid;
	}

}