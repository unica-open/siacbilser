/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_d_iva_stampa_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_iva_stampa_stato")
@NamedQuery(name="SiacDIvaStampaStato.findAll", query="SELECT s FROM SiacDIvaStampaStato s")
public class SiacDIvaStampaStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_IVA_STAMPA_STATO_IVASTSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_IVA_STAMPA_STATO_IVAST_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_IVA_STAMPA_STATO_IVASTSTATOID_GENERATOR")
	@Column(name="ivast_stato_id")
	private Integer ivastStatoId;

	@Column(name="ivast_stato_code")
	private String ivastStatoCode;

	@Column(name="ivast_stato_desc")
	private String ivastStatoDesc;

	
	//bi-directional many-to-one association to SiacRIvaStampaStato
	@OneToMany(mappedBy="siacDIvaStampaStato")
	private List<SiacRIvaStampaStato> siacRIvaStampaStatos;

	public SiacDIvaStampaStato() {
	}

	public Integer getIvastStatoId() {
		return this.ivastStatoId;
	}

	public void setIvastStatoId(Integer ivastStatoId) {
		this.ivastStatoId = ivastStatoId;
	}

	public String getIvastStatoCode() {
		return this.ivastStatoCode;
	}

	public void setIvastStatoCode(String ivastStatoCode) {
		this.ivastStatoCode = ivastStatoCode;
	}

	public String getIvastStatoDesc() {
		return this.ivastStatoDesc;
	}

	public void setIvastStatoDesc(String ivastStatoDesc) {
		this.ivastStatoDesc = ivastStatoDesc;
	}

	public List<SiacRIvaStampaStato> getSiacRIvaStampaStatos() {
		return this.siacRIvaStampaStatos;
	}

	public void setSiacRIvaStampaStatos(List<SiacRIvaStampaStato> siacRIvaStampaStatos) {
		this.siacRIvaStampaStatos = siacRIvaStampaStatos;
	}

	public SiacRIvaStampaStato addSiacRIvaStampaStato(SiacRIvaStampaStato siacRIvaStampaStato) {
		getSiacRIvaStampaStatos().add(siacRIvaStampaStato);
		siacRIvaStampaStato.setSiacDIvaStampaStato(this);

		return siacRIvaStampaStato;
	}

	public SiacRIvaStampaStato removeSiacRIvaStampaStato(SiacRIvaStampaStato siacRIvaStampaStato) {
		getSiacRIvaStampaStatos().remove(siacRIvaStampaStato);
		siacRIvaStampaStato.setSiacDIvaStampaStato(null);

		return siacRIvaStampaStato;
	}

	@Override
	public Integer getUid() {
		return ivastStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.ivastStatoId = uid;
	}

}