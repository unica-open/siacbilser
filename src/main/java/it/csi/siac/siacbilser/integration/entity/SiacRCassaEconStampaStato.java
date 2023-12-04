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
 * The persistent class for the siac_r_cassa_econ_stampa_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_cassa_econ_stampa_stato")
@NamedQuery(name="SiacRCassaEconStampaStato.findAll", query="SELECT s FROM SiacRCassaEconStampaStato s")
public class SiacRCassaEconStampaStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CASSA_ECON_STAMPA_STATO_CESTSTATORID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CASSA_ECON_STAMPA_STATO_CEST_STATO_R_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CASSA_ECON_STAMPA_STATO_CESTSTATORID_GENERATOR")
	@Column(name="cest_stato_r_id")
	private Integer cestStatoRId;

	//bi-directional many-to-one association to SiacDCassaEconStampaStato
	@ManyToOne
	@JoinColumn(name="cest_stato_id")
	private SiacDCassaEconStampaStato siacDCassaEconStampaStato;

	//bi-directional many-to-one association to SiacTCassaEconStampa
	@ManyToOne
	@JoinColumn(name="cest_id")
	private SiacTCassaEconStampa siacTCassaEconStampa;

	public SiacRCassaEconStampaStato() {
	}

	public Integer getCestStatoRId() {
		return this.cestStatoRId;
	}

	public void setCestStatoRId(Integer cestStatoRId) {
		this.cestStatoRId = cestStatoRId;
	}

	public SiacDCassaEconStampaStato getSiacDCassaEconStampaStato() {
		return this.siacDCassaEconStampaStato;
	}

	public void setSiacDCassaEconStampaStato(SiacDCassaEconStampaStato siacDCassaEconStampaStato) {
		this.siacDCassaEconStampaStato = siacDCassaEconStampaStato;
	}

	public SiacTCassaEconStampa getSiacTCassaEconStampa() {
		return this.siacTCassaEconStampa;
	}

	public void setSiacTCassaEconStampa(SiacTCassaEconStampa siacTCassaEconStampa) {
		this.siacTCassaEconStampa = siacTCassaEconStampa;
	}

	@Override
	public Integer getUid() {
		return this.cestStatoRId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cestStatoRId = uid;
	}

}