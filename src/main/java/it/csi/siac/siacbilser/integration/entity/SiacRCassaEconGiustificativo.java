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
 * The persistent class for the siac_r_cassa_econ_giustificativo database table.
 * 
 */
@Entity
@Table(name="siac_r_cassa_econ_giustificativo")
@NamedQuery(name="SiacRCassaEconGiustificativo.findAll", query="SELECT s FROM SiacRCassaEconGiustificativo s")
public class SiacRCassaEconGiustificativo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CASSA_ECON_GIUSTIFICATIVO_CASSAECONGIUSTID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CASSA_ECON_GIUSTIFICATIVO_CASSAECONGIUST_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CASSA_ECON_GIUSTIFICATIVO_CASSAECONGIUSTID_GENERATOR")
	@Column(name="cassaecongiust_id")
	private Integer cassaecongiustId;

	//bi-directional many-to-one association to SiacTCassaEconOperaz
	@ManyToOne
	@JoinColumn(name="cassaecon_id")
	private SiacTCassaEcon siacTCassaEcon;
	
	//bi-directional many-to-one association to SiacTGiustificativo
	@ManyToOne
	@JoinColumn(name="giust_id")
	private SiacDGiustificativo siacDGiustificativo;

	public SiacRCassaEconGiustificativo() {
	}

	public Integer getCassaecongiustId() {
		return this.cassaecongiustId;
	}

	public void setCassaecongiustId(Integer cassaecongiustId) {
		this.cassaecongiustId = cassaecongiustId;
	}

	public SiacTCassaEcon getSiacTCassaEcon() {
		return siacTCassaEcon;
	}

	public void setSiacTCassaEcon(SiacTCassaEcon siacTCassaEcon) {
		this.siacTCassaEcon = siacTCassaEcon;
	}

	public SiacDGiustificativo getSiacDGiustificativo() {
		return siacDGiustificativo;
	}

	public void setSiacDGiustificativo(SiacDGiustificativo siacDGiustificativo) {
		this.siacDGiustificativo = siacDGiustificativo;
	}

	@Override
	public Integer getUid() {
		return this.cassaecongiustId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cassaecongiustId = uid;
	}

}