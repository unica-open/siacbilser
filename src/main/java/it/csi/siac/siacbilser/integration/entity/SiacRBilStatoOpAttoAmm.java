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
 * The persistent class for the siac_r_bil_stato_op_atto_amm database table.
 * 
 */
@Entity
@Table(name="siac_r_bil_stato_op_atto_amm")
@NamedQuery(name="SiacRBilStatoOpAttoAmm.findAll", query="SELECT s FROM SiacRBilStatoOpAttoAmm s")
public class SiacRBilStatoOpAttoAmm extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_BIL_STATO_OP_ATTO_AMM_BILSTATOOPATTOAMMID_GENERATOR", allocationSize = 1, sequenceName="SIAC_R_BIL_STATO_OP_ATTO_AMM_BIL_STATO_OP_ATTO_AMM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_BIL_STATO_OP_ATTO_AMM_BILSTATOOPATTOAMMID_GENERATOR")
	@Column(name="bil_stato_op_atto_amm_id")
	private Integer bilStatoOpAttoAmmId;

	//bi-directional many-to-one association to SiacRBilStatoOp
	@ManyToOne
	@JoinColumn(name="bil_bil_stato_op_id")
	private SiacRBilStatoOp siacRBilStatoOp;

	//bi-directional many-to-one association to SiacTAttoAmm
	@ManyToOne
	@JoinColumn(name="attoamm_id")
	private SiacTAttoAmm siacTAttoAmm;

	public SiacRBilStatoOpAttoAmm() {
	}

	public Integer getBilStatoOpAttoAmmId() {
		return this.bilStatoOpAttoAmmId;
	}

	public void setBilStatoOpAttoAmmId(Integer bilStatoOpAttoAmmId) {
		this.bilStatoOpAttoAmmId = bilStatoOpAttoAmmId;
	}

	public SiacRBilStatoOp getSiacRBilStatoOp() {
		return this.siacRBilStatoOp;
	}

	public void setSiacRBilStatoOp(SiacRBilStatoOp siacRBilStatoOp) {
		this.siacRBilStatoOp = siacRBilStatoOp;
	}

	public SiacTAttoAmm getSiacTAttoAmm() {
		return this.siacTAttoAmm;
	}

	public void setSiacTAttoAmm(SiacTAttoAmm siacTAttoAmm) {
		this.siacTAttoAmm = siacTAttoAmm;
	}

	@Override
	public Integer getUid() {
		return bilStatoOpAttoAmmId;
	}

	@Override
	public void setUid(Integer uid) {
		bilStatoOpAttoAmmId = uid;
	}

}