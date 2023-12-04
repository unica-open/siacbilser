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
 * The persistent class for the siac_r_gsa_classif_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_gsa_classif_stato")
@NamedQuery(name="SiacRGsaClassifStato.findAll", query="SELECT s FROM SiacRGsaClassifStato s")
public class SiacRGsaClassifStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_GSA_CLASSIF_STATO_GSACLASSIFRSTATOID_GENERATOR", allocationSize=1, sequenceName="siac_r_gsa_classif_stato_gsa_classif_r_stato_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_GSA_CLASSIF_STATO_GSACLASSIFRSTATOID_GENERATOR")
	@Column(name="gsa_classif_r_stato_id")
	private Integer gsaClassifRStatoId;

	//bi-directional many-to-one association to SiacTGsaClassif
	@ManyToOne
	@JoinColumn(name="gsa_classif_id")
	private SiacTGsaClassif siacTGsaClassif;

	//bi-directional many-to-one association to SiacDGsaClassifStato
	@ManyToOne
	@JoinColumn(name="gsa_classif_stato_id")
	private SiacDGsaClassifStato siacDGsaClassifStato;

	public SiacRGsaClassifStato() {
	}

	public Integer getGsaClassifStatoId() {
		return this.gsaClassifRStatoId;
	}

	public void setGsaClassifStatoId(Integer gsaClassifStatoId) {
		this.gsaClassifRStatoId = gsaClassifStatoId;
	}

	public SiacTGsaClassif getSiacTGsaClassif() {
		return this.siacTGsaClassif;
	}

	public void setSiacTGsaClassif(SiacTGsaClassif siacTGsaClassif) {
		this.siacTGsaClassif = siacTGsaClassif;
	}

	public SiacDGsaClassifStato getSiacDGsaClassifStato() {
		return this.siacDGsaClassifStato;
	}

	public void setSiacDGsaClassifStato(SiacDGsaClassifStato siacDGsaClassifStato) {
		this.siacDGsaClassifStato = siacDGsaClassifStato;
	}

	@Override
	public Integer getUid() {
		return gsaClassifRStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.gsaClassifRStatoId = uid;
	}

}