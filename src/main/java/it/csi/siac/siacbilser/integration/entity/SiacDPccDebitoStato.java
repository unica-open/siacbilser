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
 * The persistent class for the siac_d_pcc_debito_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_pcc_debito_stato")
@NamedQuery(name="SiacDPccDebitoStato.findAll", query="SELECT s FROM SiacDPccDebitoStato s")
public class SiacDPccDebitoStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_PCC_DEBITO_STATO_PCCDEBSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_PCC_DEBITO_STATO_PCCDEB_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_PCC_DEBITO_STATO_PCCDEBSTATOID_GENERATOR")
	@Column(name="pccdeb_stato_id")
	private Integer pccdebStatoId;

	@Column(name="pccdeb_stato_code")
	private String pccdebStatoCode;

	@Column(name="pccdeb_stato_desc")
	private String pccdebStatoDesc;

	//bi-directional many-to-one association to SiacRPccDebitoStatoCausale
	@OneToMany(mappedBy="siacDPccDebitoStato")
	private List<SiacRPccDebitoStatoCausale> siacRPccDebitoStatoCausales;

	//bi-directional many-to-one association to SiacTRegistroPcc
	@OneToMany(mappedBy="siacDPccDebitoStato")
	private List<SiacTRegistroPcc> siacTRegistroPccs;

	public SiacDPccDebitoStato() {
	}

	public Integer getPccdebStatoId() {
		return this.pccdebStatoId;
	}

	public void setPccdebStatoId(Integer pccdebStatoId) {
		this.pccdebStatoId = pccdebStatoId;
	}

	public String getPccdebStatoCode() {
		return this.pccdebStatoCode;
	}

	public void setPccdebStatoCode(String pccdebStatoCode) {
		this.pccdebStatoCode = pccdebStatoCode;
	}

	public String getPccdebStatoDesc() {
		return this.pccdebStatoDesc;
	}

	public void setPccdebStatoDesc(String pccdebStatoDesc) {
		this.pccdebStatoDesc = pccdebStatoDesc;
	}

	public List<SiacRPccDebitoStatoCausale> getSiacRPccDebitoStatoCausales() {
		return this.siacRPccDebitoStatoCausales;
	}

	public void setSiacRPccDebitoStatoCausales(List<SiacRPccDebitoStatoCausale> siacRPccDebitoStatoCausales) {
		this.siacRPccDebitoStatoCausales = siacRPccDebitoStatoCausales;
	}

	public SiacRPccDebitoStatoCausale addSiacRPccDebitoStatoCausale(SiacRPccDebitoStatoCausale siacRPccDebitoStatoCausale) {
		getSiacRPccDebitoStatoCausales().add(siacRPccDebitoStatoCausale);
		siacRPccDebitoStatoCausale.setSiacDPccDebitoStato(this);

		return siacRPccDebitoStatoCausale;
	}

	public SiacRPccDebitoStatoCausale removeSiacRPccDebitoStatoCausale(SiacRPccDebitoStatoCausale siacRPccDebitoStatoCausale) {
		getSiacRPccDebitoStatoCausales().remove(siacRPccDebitoStatoCausale);
		siacRPccDebitoStatoCausale.setSiacDPccDebitoStato(null);

		return siacRPccDebitoStatoCausale;
	}

	public List<SiacTRegistroPcc> getSiacTRegistroPccs() {
		return this.siacTRegistroPccs;
	}

	public void setSiacTRegistroPccs(List<SiacTRegistroPcc> siacTRegistroPccs) {
		this.siacTRegistroPccs = siacTRegistroPccs;
	}

	public SiacTRegistroPcc addSiacTRegistroPcc(SiacTRegistroPcc siacTRegistroPcc) {
		getSiacTRegistroPccs().add(siacTRegistroPcc);
		siacTRegistroPcc.setSiacDPccDebitoStato(this);

		return siacTRegistroPcc;
	}

	public SiacTRegistroPcc removeSiacTRegistroPcc(SiacTRegistroPcc siacTRegistroPcc) {
		getSiacTRegistroPccs().remove(siacTRegistroPcc);
		siacTRegistroPcc.setSiacDPccDebitoStato(null);

		return siacTRegistroPcc;
	}

	@Override
	public Integer getUid() {
		return pccdebStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.pccdebStatoId = uid;
	}

}