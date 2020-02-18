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
 * The persistent class for the siac_r_cassa_econ_bil database table.
 * 
 */
@Entity
@Table(name="siac_r_cassa_econ_bil")
@NamedQuery(name="SiacRCassaEconBil.findAll", query="SELECT s FROM SiacRCassaEconBil s")
public class SiacRCassaEconBil extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CASSA_ECON_BIL_CASSAECONID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CASSA_ECON_BIL_CASSAECONBIL_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CASSA_ECON_BIL_CASSAECONID_GENERATOR")
	@Column(name="cassaeconbil_id")
	private Integer cassaeconbilId;

	//bi-directional many-to-one association to SiacTBil
	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBil siacTBil;

	//bi-directional many-to-one association to SiacTCassaEcon
	@ManyToOne
	@JoinColumn(name="cassaecon_id")
	private SiacTCassaEcon siacTCassaEcon;

	public SiacRCassaEconBil() {
	}

	public Integer getCassaeconbilId() {
		return cassaeconbilId;
	}

	public void setCassaeconbilId(Integer cassaeconbilId) {
		this.cassaeconbilId = cassaeconbilId;
	}

	public SiacTBil getSiacTBil() {
		return siacTBil;
	}

	public void setSiacTBil(SiacTBil siacTBil) {
		this.siacTBil = siacTBil;
	}

	public SiacTCassaEcon getSiacTCassaEcon() {
		return siacTCassaEcon;
	}

	public void setSiacTCassaEcon(SiacTCassaEcon siacTCassaEcon) {
		this.siacTCassaEcon = siacTCassaEcon;
	}

	@Override
	public Integer getUid() {
		return this.cassaeconbilId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cassaeconbilId = uid;
	}

}