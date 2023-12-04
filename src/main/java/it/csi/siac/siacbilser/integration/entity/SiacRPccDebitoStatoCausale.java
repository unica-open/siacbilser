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
 * The persistent class for the siac_r_pcc_debito_stato_causale database table.
 * 
 */
@Entity
@Table(name="siac_r_pcc_debito_stato_causale")
@NamedQuery(name="SiacRPccDebitoStatoCausale.findAll", query="SELECT s FROM SiacRPccDebitoStatoCausale s")
public class SiacRPccDebitoStatoCausale extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_PCC_DEBITO_STATO_CAUSALE_PCCDESTCAUID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_PCC_DEBITO_STATO_CAUSALE_PCCDESTCAU_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_PCC_DEBITO_STATO_CAUSALE_PCCDESTCAUID_GENERATOR")
	@Column(name="pccdestcau_id")
	private Integer pccdestcauId;

	//bi-directional many-to-one association to SiacDPccCausale
	@ManyToOne
	@JoinColumn(name="pcccau_id")
	private SiacDPccCausale siacDPccCausale;

	//bi-directional many-to-one association to SiacDPccDebitoStato
	@ManyToOne
	@JoinColumn(name="pccdeb_stato_id")
	private SiacDPccDebitoStato siacDPccDebitoStato;

	public SiacRPccDebitoStatoCausale() {
	}

	public Integer getPccdestcauId() {
		return this.pccdestcauId;
	}

	public void setPccdestcauId(Integer pccdestcauId) {
		this.pccdestcauId = pccdestcauId;
	}

	public SiacDPccCausale getSiacDPccCausale() {
		return this.siacDPccCausale;
	}

	public void setSiacDPccCausale(SiacDPccCausale siacDPccCausale) {
		this.siacDPccCausale = siacDPccCausale;
	}

	public SiacDPccDebitoStato getSiacDPccDebitoStato() {
		return this.siacDPccDebitoStato;
	}

	public void setSiacDPccDebitoStato(SiacDPccDebitoStato siacDPccDebitoStato) {
		this.siacDPccDebitoStato = siacDPccDebitoStato;
	}

	@Override
	public Integer getUid() {
		return pccdestcauId;
	}

	@Override
	public void setUid(Integer uid) {
		this.pccdestcauId =uid;
	}

}