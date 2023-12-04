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
 * The persistent class for the siac_r_cassa_econ_operaz_tipo database table.
 * 
 */
@Entity
@Table(name="siac_r_cassa_econ_operaz_tipo")
@NamedQuery(name="SiacRCassaEconOperazTipo.findAll", query="SELECT s FROM SiacRCassaEconOperazTipo s")
public class SiacRCassaEconOperazTipo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CASSA_ECON_OPERAZ_TIPO_CASSAECONOPRTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CASSA_ECON_OPERAZ_TIPO_CASSAECONOP_R_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CASSA_ECON_OPERAZ_TIPO_CASSAECONOPRTIPOID_GENERATOR")
	@Column(name="cassaeconop_r_tipo_id")
	private Integer cassaeconopRTipoId;

	//bi-directional many-to-one association to SiacTCassaEcon
	@ManyToOne
	@JoinColumn(name="cassaeconop_id")
	private SiacTCassaEconOperaz siacTCassaEconOperaz;

	//bi-directional many-to-one association to SiacDCassaEconOperazTipo
	@ManyToOne
	@JoinColumn(name="cassaeconop_tipo_id")
	private SiacDCassaEconOperazTipo siacDCassaEconOperazTipo;

	public SiacRCassaEconOperazTipo() {
	}

	public Integer getCassaeconopRTipoId() {
		return this.cassaeconopRTipoId;
	}

	public void setCassaeconopRTipoId(Integer cassaeconopRTipoId) {
		this.cassaeconopRTipoId = cassaeconopRTipoId;
	}

	public SiacTCassaEconOperaz getSiacTCassaEconOperaz() {
		return siacTCassaEconOperaz;
	}

	public void setSiacTCassaEconOperaz(SiacTCassaEconOperaz siacTCassaEconOperaz) {
		this.siacTCassaEconOperaz = siacTCassaEconOperaz;
	}

	public SiacDCassaEconOperazTipo getSiacDCassaEconOperazTipo() {
		return this.siacDCassaEconOperazTipo;
	}

	public void setSiacDCassaEconOperazTipo(SiacDCassaEconOperazTipo siacDCassaEconOperazTipo) {
		this.siacDCassaEconOperazTipo = siacDCassaEconOperazTipo;
	}

	@Override
	public Integer getUid() {
		return this.cassaeconopRTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cassaeconopRTipoId = uid;
	}

}