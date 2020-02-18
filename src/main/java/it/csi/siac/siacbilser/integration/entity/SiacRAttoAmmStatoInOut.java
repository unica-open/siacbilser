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
 * The persistent class for the siac_r_atto_amm_stato_in_out database table.
 * 
 */
@Entity
@Table(name="siac_r_atto_amm_stato_in_out")
@NamedQuery(name="SiacRAttoAmmStatoInOut.findAll", query="SELECT s FROM SiacRAttoAmmStatoInOut s")
public class SiacRAttoAmmStatoInOut extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_ATTO_AMM_STATO_IN_OUT_INOUTATTOAMMSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_ATTO_AMM_STATO_IN_OUT_INOUT_ATTOAMM_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ATTO_AMM_STATO_IN_OUT_INOUTATTOAMMSTATOID_GENERATOR")
	@Column(name="inout_attoamm_stato_id")
	private Integer inoutAttoammStatoId;

	//bi-directional many-to-one association to SiacDAttoAmmStato
	/** The siac d atto amm stato. */
	@ManyToOne
	@JoinColumn(name="attoamm_stato_id")
	private SiacDAttoAmmStato siacDAttoAmmStato;

	@Column(name="out_attoamm_stato_code")
	private String outAttoammStatoCode;

	public SiacRAttoAmmStatoInOut() {
	}

	public Integer getInoutAttoammStatoId() {
		return this.inoutAttoammStatoId;
	}

	public void setInoutAttoammStatoId(Integer inoutAttoammStatoId) {
		this.inoutAttoammStatoId = inoutAttoammStatoId;
	}

	public SiacDAttoAmmStato getSiacDAttoAmmStato() {
		return siacDAttoAmmStato;
	}

	public void setSiacDAttoAmmStato(SiacDAttoAmmStato siacDAttoAmmStato) {
		this.siacDAttoAmmStato = siacDAttoAmmStato;
	}

	public String getOutAttoammStatoCode() {
		return this.outAttoammStatoCode;
	}

	public void setOutAttoammStatoCode(String outAttoammStatoCode) {
		this.outAttoammStatoCode = outAttoammStatoCode;
	}

	@Override
	public Integer getUid() {
		return this.inoutAttoammStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.inoutAttoammStatoId = uid;
	}
	
}