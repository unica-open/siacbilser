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
 * The persistent class for the siac_r_allegato_atto_stampa_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_allegato_atto_stampa_stato")
@NamedQuery(name="SiacRAllegatoAttoStampaStato.findAll", query="SELECT s FROM SiacRAllegatoAttoStampaStato s")
public class SiacRAllegatoAttoStampaStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_ALLEGATO_ATTO_STAMPA_STATO_ATTOALSTSTATORID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_ALLEGATO_ATTO_STAMPA_STATO_ATTOALST_STATO_R_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ALLEGATO_ATTO_STAMPA_STATO_ATTOALSTSTATORID_GENERATOR")
	@Column(name="attoalst_stato_r_id")
	private Integer attoalstStatoRId;

	//bi-directional many-to-one association to SiacDAllegatoAttoStampaStato
	@ManyToOne
	@JoinColumn(name="attoalst_stato_id")
	private SiacDAllegatoAttoStampaStato siacDAllegatoAttoStampaStato;

	//bi-directional many-to-one association to SiacTAttoAllegatoStampa
	@ManyToOne
	@JoinColumn(name="attoalst_id")
	private SiacTAttoAllegatoStampa siacTAttoAllegatoStampa;

	public SiacRAllegatoAttoStampaStato() {
	}

	public Integer getAttoalstStatoRId() {
		return this.attoalstStatoRId;
	}

	public void setAttoalstStatoRId(Integer attoalstStatoRId) {
		this.attoalstStatoRId = attoalstStatoRId;
	}

	public SiacDAllegatoAttoStampaStato getSiacDAllegatoAttoStampaStato() {
		return this.siacDAllegatoAttoStampaStato;
	}

	public void setSiacDAllegatoAttoStampaStato(SiacDAllegatoAttoStampaStato siacDAllegatoAttoStampaStato) {
		this.siacDAllegatoAttoStampaStato = siacDAllegatoAttoStampaStato;
	}

	public SiacTAttoAllegatoStampa getSiacTAttoAllegatoStampa() {
		return this.siacTAttoAllegatoStampa;
	}

	public void setSiacTAttoAllegatoStampa(SiacTAttoAllegatoStampa siacTAttoAllegatoStampa) {
		this.siacTAttoAllegatoStampa = siacTAttoAllegatoStampa;
	}

	@Override
	public Integer getUid() {
		return this.attoalstStatoRId;
	}

	@Override
	public void setUid(Integer uid) {
		this.attoalstStatoRId = uid;
	}

}