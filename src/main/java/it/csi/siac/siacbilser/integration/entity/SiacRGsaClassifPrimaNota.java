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
 * The persistent class for the siac_r_gsa_classif_prima_nota database table.
 * 
 */
@Entity
@Table(name="siac_r_gsa_classif_prima_nota")
@NamedQuery(name="SiacRGsaClassifPrimaNota.findAll", query="SELECT s FROM SiacRGsaClassifPrimaNota s")
public class SiacRGsaClassifPrimaNota extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_GSA_CLASSIF_PRIMA_NOTA_GSACLASSIFRPNOTAID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_GSA_CLASSIF_PRIMA_NOTA_GSA_CLASSIF_R_PNOTA_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_GSA_CLASSIF_PRIMA_NOTA_GSACLASSIFRPNOTAID_GENERATOR")
	@Column(name="gsa_classif_r_pnota_id")
	private Integer gsaClassifRPnotaId;

	//bi-directional many-to-one association to SiacTGsaClassif
	@ManyToOne
	@JoinColumn(name="gsa_classif_id")
	private SiacTGsaClassif siacTGsaClassif;

	//bi-directional many-to-one association to SiacTPrimaNota
	@ManyToOne
	@JoinColumn(name="pnota_id")
	private SiacTPrimaNota siacTPrimaNota;

	public SiacRGsaClassifPrimaNota() {
	}

	/**
	 * @return the gsaClassifRPnotaId
	 */
	public Integer getGsaClassifRPnotaId() {
		return this.gsaClassifRPnotaId;
	}

	/**
	 * @param gsaClassifRPnotaId the gsaClassifRPnotaId to set
	 */
	public void setGsaClassifRPnotaId(Integer gsaClassifRPnotaId) {
		this.gsaClassifRPnotaId = gsaClassifRPnotaId;
	}

	/**
	 * @return the siacTGsaClassif
	 */
	public SiacTGsaClassif getSiacTGsaClassif() {
		return this.siacTGsaClassif;
	}

	/**
	 * @param siacTGsaClassif the siacTGsaClassif to set
	 */
	public void setSiacTGsaClassif(SiacTGsaClassif siacTGsaClassif) {
		this.siacTGsaClassif = siacTGsaClassif;
	}

	public SiacTPrimaNota getSiacTPrimaNota() {
		return this.siacTPrimaNota;
	}

	public void setSiacTPrimaNota(SiacTPrimaNota siacTPrimaNota) {
		this.siacTPrimaNota = siacTPrimaNota;
	}
	
	@Override
	public Integer getUid() {
		return this.gsaClassifRPnotaId;
	}

	@Override
	public void setUid(Integer uid) {
		this.gsaClassifRPnotaId = uid;
		
	}

}