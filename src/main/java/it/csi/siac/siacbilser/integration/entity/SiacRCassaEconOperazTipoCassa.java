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
 * The persistent class for the siac_r_cassa_econ_operaz_tipo_cassa database table.
 * 
 */
@Entity
@Table(name="siac_r_cassa_econ_operaz_tipo_cassa")
@NamedQuery(name="SiacRCassaEconOperazTipoCassa.findAll", query="SELECT s FROM SiacRCassaEconOperazTipoCassa s")
public class SiacRCassaEconOperazTipoCassa extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CASSA_ECON_OPERAZ_TIPO_CASSA_CASSAOPID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CASSA_ECON_OPERAZ_TIPO_CASSA_CASSAOP_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CASSA_ECON_OPERAZ_TIPO_CASSA_CASSAOPID_GENERATOR")
	@Column(name="cassaop_id")
	private Integer cassaopId;

	//bi-directional many-to-one association to SiacTCassaEcon
	@ManyToOne
	@JoinColumn(name="cassaecon_id")
	private SiacTCassaEcon siacTCassaEcon;

	//bi-directional many-to-one association to SiacDCassaEconOperazTipo
	@ManyToOne
	@JoinColumn(name="cassaeconop_tipo_id")
	private SiacDCassaEconOperazTipo siacDCassaEconOperazTipo;

	public SiacRCassaEconOperazTipoCassa() {
	}

	public Integer getCassaopId() {
		return this.cassaopId;
	}

	public void setCassaopId(Integer cassaopId) {
		this.cassaopId = cassaopId;
	}

	public SiacTCassaEcon getSiacTCassaEcon() {
		return siacTCassaEcon;
	}

	public void setSiacTCassaEcon(SiacTCassaEcon siacTCassaEcon) {
		this.siacTCassaEcon = siacTCassaEcon;
	}

	public SiacDCassaEconOperazTipo getSiacDCassaEconOperazTipo() {
		return this.siacDCassaEconOperazTipo;
	}

	public void setSiacDCassaEconOperazTipo(SiacDCassaEconOperazTipo siacDCassaEconOperazTipo) {
		this.siacDCassaEconOperazTipo = siacDCassaEconOperazTipo;
	}

	@Override
	public Integer getUid() {
		return this.cassaopId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cassaopId = uid;
	}

}