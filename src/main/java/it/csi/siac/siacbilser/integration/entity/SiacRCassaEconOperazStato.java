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
 * The persistent class for the siac_r_cassa_econ_operaz_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_cassa_econ_operaz_stato")
@NamedQuery(name="SiacRCassaEconOperazStato.findAll", query="SELECT s FROM SiacRCassaEconOperazStato s")
public class SiacRCassaEconOperazStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CASSA_ECON_OPERAZ_STATO_CASSAECONOPRSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CASSA_ECON_OPERAZ_STATO_CASSAECONOP_R_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CASSA_ECON_OPERAZ_STATO_CASSAECONOPRSTATOID_GENERATOR")
	@Column(name="cassaeconop_r_stato_id")
	private Integer cassaeconopRStatoId;

	//bi-directional many-to-one association to SiacDCassaEconOperazStato
	@ManyToOne
	@JoinColumn(name="cassaeconop_stato_id")
	private SiacDCassaEconOperazStato siacDCassaEconOperazStato;

	//bi-directional many-to-one association to SiacTCassaEconOperaz
	@ManyToOne
	@JoinColumn(name="cassaeconop_id")
	private SiacTCassaEconOperaz siacTCassaEconOperaz;

	public SiacRCassaEconOperazStato() {
	}

	public Integer getCassaeconopRStatoId() {
		return this.cassaeconopRStatoId;
	}

	public void setCassaeconopRStatoId(Integer cassaeconopRStatoId) {
		this.cassaeconopRStatoId = cassaeconopRStatoId;
	}

	public SiacDCassaEconOperazStato getSiacDCassaEconOperazStato() {
		return this.siacDCassaEconOperazStato;
	}

	public void setSiacDCassaEconOperazStato(SiacDCassaEconOperazStato siacDCassaEconOperazStato) {
		this.siacDCassaEconOperazStato = siacDCassaEconOperazStato;
	}

	public SiacTCassaEconOperaz getSiacTCassaEconOperaz() {
		return this.siacTCassaEconOperaz;
	}

	public void setSiacTCassaEconOperaz(SiacTCassaEconOperaz siacTCassaEconOperaz) {
		this.siacTCassaEconOperaz = siacTCassaEconOperaz;
	}

	@Override
	public Integer getUid() {
		return cassaeconopRStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cassaeconopRStatoId = uid;
	}

}