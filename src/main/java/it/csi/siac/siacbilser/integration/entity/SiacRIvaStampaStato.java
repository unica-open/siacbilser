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
 * The persistent class for the siac_r_iva_stampa_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_iva_stampa_stato")
@NamedQuery(name="SiacRIvaStampaStato.findAll", query="SELECT s FROM SiacRIvaStampaStato s")
public class SiacRIvaStampaStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_IVA_STAMPA_STATO_IVASTSTATORID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_IVA_STAMPA_STATO_IVAST_STATO_R_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_IVA_STAMPA_STATO_IVASTSTATORID_GENERATOR")
	@Column(name="ivast_stato_r_id")
	private Integer ivastStatoRId;


	//bi-directional many-to-one association to SiacDIvaStampaStato
	@ManyToOne
	@JoinColumn(name="ivast_stato_id")
	private SiacDIvaStampaStato siacDIvaStampaStato;

	//bi-directional many-to-one association to SiacTIvaStampa
	@ManyToOne
	@JoinColumn(name="ivast_id")
	private SiacTIvaStampa siacTIvaStampa;

	public SiacRIvaStampaStato() {
	}

	public Integer getIvastStatoRId() {
		return this.ivastStatoRId;
	}

	public void setIvastStatoRId(Integer ivastStatoRId) {
		this.ivastStatoRId = ivastStatoRId;
	}

	public SiacDIvaStampaStato getSiacDIvaStampaStato() {
		return this.siacDIvaStampaStato;
	}

	public void setSiacDIvaStampaStato(SiacDIvaStampaStato siacDIvaStampaStato) {
		this.siacDIvaStampaStato = siacDIvaStampaStato;
	}

	public SiacTIvaStampa getSiacTIvaStampa() {
		return this.siacTIvaStampa;
	}

	public void setSiacTIvaStampa(SiacTIvaStampa siacTIvaStampa) {
		this.siacTIvaStampa = siacTIvaStampa;
	}

	@Override
	public Integer getUid() {
		return ivastStatoRId;
	}

	@Override
	public void setUid(Integer uid) {
		this.ivastStatoRId = uid;
	}

}