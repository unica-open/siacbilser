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
 * The persistent class for the siac_r_causale_ep_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_causale_ep_stato")
@NamedQuery(name="SiacRCausaleEpStato.findAll", query="SELECT s FROM SiacRCausaleEpStato s")
public class SiacRCausaleEpStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CAUSALE_EP_STATO_CAUSALEEPSTATORID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CAUSALE_EP_STATO_CAUSALE_EP_STATO_R_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CAUSALE_EP_STATO_CAUSALEEPSTATORID_GENERATOR")
	@Column(name="causale_ep_stato_r_id")
	private Integer causaleEpStatoRId;

	

	//bi-directional many-to-one association to SiacDCausaleEpStato
	@ManyToOne
	@JoinColumn(name="causale_ep_stato_id")
	private SiacDCausaleEpStato siacDCausaleEpStato;

	//bi-directional many-to-one association to SiacTCausaleEp
	@ManyToOne
	@JoinColumn(name="causale_ep_id")
	private SiacTCausaleEp siacTCausaleEp;

	public SiacRCausaleEpStato() {
	}

	public Integer getCausaleEpStatoRId() {
		return this.causaleEpStatoRId;
	}

	public void setCausaleEpStatoRId(Integer causaleEpStatoRId) {
		this.causaleEpStatoRId = causaleEpStatoRId;
	}

	public SiacDCausaleEpStato getSiacDCausaleEpStato() {
		return this.siacDCausaleEpStato;
	}

	public void setSiacDCausaleEpStato(SiacDCausaleEpStato siacDCausaleEpStato) {
		this.siacDCausaleEpStato = siacDCausaleEpStato;
	}

	public SiacTCausaleEp getSiacTCausaleEp() {
		return this.siacTCausaleEp;
	}

	public void setSiacTCausaleEp(SiacTCausaleEp siacTCausaleEp) {
		this.siacTCausaleEp = siacTCausaleEp;
	}

	@Override
	public Integer getUid() {
		return causaleEpStatoRId;
	}

	@Override
	public void setUid(Integer uid) {
		this.causaleEpStatoRId = uid;
	}

}