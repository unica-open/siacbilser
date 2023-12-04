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
 * The persistent class for the siac_d_causale_ep_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_causale_ep_stato")
@NamedQuery(name="SiacDCausaleEpStato.findAll", query="SELECT s FROM SiacDCausaleEpStato s")
public class SiacDCausaleEpStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_CAUSALE_EP_STATO_CAUSALEEPSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_CAUSALE_EP_STATO_CAUSALE_EP_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_CAUSALE_EP_STATO_CAUSALEEPSTATOID_GENERATOR")
	@Column(name="causale_ep_stato_id")
	private Integer causaleEpStatoId;

	@Column(name="causale_ep_stato_code")
	private String causaleEpStatoCode;

	@Column(name="causale_ep_stato_desc")
	private String causaleEpStatoDesc;

	//bi-directional many-to-one association to SiacRCausaleEpStato
	@OneToMany(mappedBy="siacDCausaleEpStato")
	private List<SiacRCausaleEpStato> siacRCausaleEpStatos;

	public SiacDCausaleEpStato() {
	}

	public Integer getCausaleEpStatoId() {
		return this.causaleEpStatoId;
	}

	public void setCausaleEpStatoId(Integer causaleEpStatoId) {
		this.causaleEpStatoId = causaleEpStatoId;
	}

	public String getCausaleEpStatoCode() {
		return this.causaleEpStatoCode;
	}

	public void setCausaleEpStatoCode(String causaleEpStatoCode) {
		this.causaleEpStatoCode = causaleEpStatoCode;
	}

	public String getCausaleEpStatoDesc() {
		return this.causaleEpStatoDesc;
	}

	public void setCausaleEpStatoDesc(String causaleEpStatoDesc) {
		this.causaleEpStatoDesc = causaleEpStatoDesc;
	}

	public List<SiacRCausaleEpStato> getSiacRCausaleEpStatos() {
		return this.siacRCausaleEpStatos;
	}

	public void setSiacRCausaleEpStatos(List<SiacRCausaleEpStato> siacRCausaleEpStatos) {
		this.siacRCausaleEpStatos = siacRCausaleEpStatos;
	}

	public SiacRCausaleEpStato addSiacRCausaleEpStato(SiacRCausaleEpStato siacRCausaleEpStato) {
		getSiacRCausaleEpStatos().add(siacRCausaleEpStato);
		siacRCausaleEpStato.setSiacDCausaleEpStato(this);

		return siacRCausaleEpStato;
	}

	public SiacRCausaleEpStato removeSiacRCausaleEpStato(SiacRCausaleEpStato siacRCausaleEpStato) {
		getSiacRCausaleEpStatos().remove(siacRCausaleEpStato);
		siacRCausaleEpStato.setSiacDCausaleEpStato(null);

		return siacRCausaleEpStato;
	}

	@Override
	public Integer getUid() {
		return this.causaleEpStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.causaleEpStatoId = uid;
	}

}