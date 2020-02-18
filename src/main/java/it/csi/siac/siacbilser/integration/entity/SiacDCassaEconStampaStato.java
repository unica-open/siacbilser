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
 * The persistent class for the siac_d_cassa_econ_stampa_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_cassa_econ_stampa_stato")
@NamedQuery(name="SiacDCassaEconStampaStato.findAll", query="SELECT s FROM SiacDCassaEconStampaStato s")
public class SiacDCassaEconStampaStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_CASSA_ECON_STAMPA_STATO_CESTSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_CASSA_ECON_STAMPA_STATO_CEST_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_CASSA_ECON_STAMPA_STATO_CESTSTATOID_GENERATOR")
	@Column(name="cest_stato_id")
	private Integer cestStatoId;

	@Column(name="cest_stato_code")
	private String cestStatoCode;

	@Column(name="cest_stato_desc")
	private String cestStatoDesc;

	//bi-directional many-to-one association to SiacRCassaEconStampaStato
	@OneToMany(mappedBy="siacDCassaEconStampaStato")
	private List<SiacRCassaEconStampaStato> siacRCassaEconStampaStatos;

	public SiacDCassaEconStampaStato() {
	}

	public Integer getCestStatoId() {
		return this.cestStatoId;
	}

	public void setCestStatoId(Integer cestStatoId) {
		this.cestStatoId = cestStatoId;
	}

	public String getCestStatoCode() {
		return this.cestStatoCode;
	}

	public void setCestStatoCode(String cestStatoCode) {
		this.cestStatoCode = cestStatoCode;
	}

	public String getCestStatoDesc() {
		return this.cestStatoDesc;
	}

	public void setCestStatoDesc(String cestStatoDesc) {
		this.cestStatoDesc = cestStatoDesc;
	}

	public List<SiacRCassaEconStampaStato> getSiacRCassaEconStampaStatos() {
		return this.siacRCassaEconStampaStatos;
	}

	public void setSiacRCassaEconStampaStatos(List<SiacRCassaEconStampaStato> siacRCassaEconStampaStatos) {
		this.siacRCassaEconStampaStatos = siacRCassaEconStampaStatos;
	}

	public SiacRCassaEconStampaStato addSiacRCassaEconStampaStato(SiacRCassaEconStampaStato siacRCassaEconStampaStato) {
		getSiacRCassaEconStampaStatos().add(siacRCassaEconStampaStato);
		siacRCassaEconStampaStato.setSiacDCassaEconStampaStato(this);

		return siacRCassaEconStampaStato;
	}

	public SiacRCassaEconStampaStato removeSiacRCassaEconStampaStato(SiacRCassaEconStampaStato siacRCassaEconStampaStato) {
		getSiacRCassaEconStampaStatos().remove(siacRCassaEconStampaStato);
		siacRCassaEconStampaStato.setSiacDCassaEconStampaStato(null);

		return siacRCassaEconStampaStato;
	}

	@Override
	public Integer getUid() {
		return this.cestStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cestStatoId = uid;
	}

}