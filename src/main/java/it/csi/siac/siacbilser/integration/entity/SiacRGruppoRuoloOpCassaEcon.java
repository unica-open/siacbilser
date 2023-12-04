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
 * The persistent class for the siac_r_gruppo_ruolo_op_cassa_econ database table.
 * 
 */
@Entity
@Table(name="siac_r_gruppo_ruolo_op_cassa_econ")
@NamedQuery(name="SiacRGruppoRuoloOpCassaEcon.findAll", query="SELECT s FROM SiacRGruppoRuoloOpCassaEcon s")
public class SiacRGruppoRuoloOpCassaEcon extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_GRUPPO_RUOLO_OP_CASSA_ECON_GRUPPORUOLOOPID_GENERATOR", allocationSize = 1, sequenceName="SIAC_R_GRUPPO_RUOLO_OP_CASSA_ECON_GRUPPO_RUOLO_OP_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_GRUPPO_RUOLO_OP_CASSA_ECON_GRUPPORUOLOOPID_GENERATOR")
	@Column(name="gruppo_ruolo_op_id")
	private Integer gruppoRuoloOpId;

	//bi-directional many-to-one association to SiacDRuoloOp
	@ManyToOne
	@JoinColumn(name="ruolo_op_id")
	private SiacDRuoloOp siacDRuoloOp;

	//bi-directional many-to-one association to SiacTCassaEcon
	@ManyToOne
	@JoinColumn(name="cassaecon_id")
	private SiacTCassaEcon siacTCassaEcon;

	//bi-directional many-to-one association to SiacTGruppo
	@ManyToOne
	@JoinColumn(name="gruppo_id")
	private SiacTGruppo siacTGruppo;

	public SiacRGruppoRuoloOpCassaEcon() {
	}

	public Integer getGruppoRuoloOpId() {
		return this.gruppoRuoloOpId;
	}

	public void setGruppoRuoloOpId(Integer gruppoRuoloOpId) {
		this.gruppoRuoloOpId = gruppoRuoloOpId;
	}

	public SiacDRuoloOp getSiacDRuoloOp() {
		return this.siacDRuoloOp;
	}

	public void setSiacDRuoloOp(SiacDRuoloOp siacDRuoloOp) {
		this.siacDRuoloOp = siacDRuoloOp;
	}

	public SiacTCassaEcon getSiacTCassaEcon() {
		return this.siacTCassaEcon;
	}

	public void setSiacTCassaEcon(SiacTCassaEcon siacTCassaEcon) {
		this.siacTCassaEcon = siacTCassaEcon;
	}

	public SiacTGruppo getSiacTGruppo() {
		return this.siacTGruppo;
	}

	public void setSiacTGruppo(SiacTGruppo siacTGruppo) {
		this.siacTGruppo = siacTGruppo;
	}

	@Override
	public Integer getUid() {
		return gruppoRuoloOpId;
	}

	@Override
	public void setUid(Integer uid) {
		gruppoRuoloOpId = uid;
	}

}