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
 * The persistent class for the siac_d_allegato_atto_stampa_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_allegato_atto_stampa_stato")
@NamedQuery(name="SiacDAllegatoAttoStampaStato.findAll", query="SELECT s FROM SiacDAllegatoAttoStampaStato s")
public class SiacDAllegatoAttoStampaStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_ALLEGATO_ATTO_STAMPA_STATO_ATTOALSTSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_ALLEGATO_ATTO_STAMPA_STATO_ATTOALST_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_ALLEGATO_ATTO_STAMPA_STATO_ATTOALSTSTATOID_GENERATOR")
	@Column(name="attoalst_stato_id")
	private Integer attoalstStatoId;

	@Column(name="attoalst_stato_code")
	private String attoalstStatoCode;

	@Column(name="attoalst_stato_desc")
	private String attoalstStatoDesc;

	//bi-directional many-to-one association to SiacRAllegatoAttoStampaStato
	@OneToMany(mappedBy="siacDAllegatoAttoStampaStato")
	private List<SiacRAllegatoAttoStampaStato> siacRAllegatoAttoStampaStatos;

	public SiacDAllegatoAttoStampaStato() {
	}

	public Integer getAttoalstStatoId() {
		return this.attoalstStatoId;
	}

	public void setAttoalstStatoId(Integer attoalstStatoId) {
		this.attoalstStatoId = attoalstStatoId;
	}

	public String getAttoalstStatoCode() {
		return this.attoalstStatoCode;
	}

	public void setAttoalstStatoCode(String attoalstStatoCode) {
		this.attoalstStatoCode = attoalstStatoCode;
	}

	public String getAttoalstStatoDesc() {
		return this.attoalstStatoDesc;
	}

	public void setAttoalstStatoDesc(String attoalstStatoDesc) {
		this.attoalstStatoDesc = attoalstStatoDesc;
	}

	public List<SiacRAllegatoAttoStampaStato> getSiacRAllegatoAttoStampaStatos() {
		return this.siacRAllegatoAttoStampaStatos;
	}

	public void setSiacRAllegatoAttoStampaStatos(List<SiacRAllegatoAttoStampaStato> siacRAllegatoAttoStampaStatos) {
		this.siacRAllegatoAttoStampaStatos = siacRAllegatoAttoStampaStatos;
	}

	public SiacRAllegatoAttoStampaStato addSiacRAllegatoAttoStampaStato(SiacRAllegatoAttoStampaStato siacRAllegatoAttoStampaStato) {
		getSiacRAllegatoAttoStampaStatos().add(siacRAllegatoAttoStampaStato);
		siacRAllegatoAttoStampaStato.setSiacDAllegatoAttoStampaStato(this);

		return siacRAllegatoAttoStampaStato;
	}

	public SiacRAllegatoAttoStampaStato removeSiacRAllegatoAttoStampaStato(SiacRAllegatoAttoStampaStato siacRAllegatoAttoStampaStato) {
		getSiacRAllegatoAttoStampaStatos().remove(siacRAllegatoAttoStampaStato);
		siacRAllegatoAttoStampaStato.setSiacDAllegatoAttoStampaStato(null);

		return siacRAllegatoAttoStampaStato;
	}

	@Override
	public Integer getUid() {
		return this.attoalstStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.attoalstStatoId = uid;
	}

}