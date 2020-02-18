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
import javax.persistence.Version;


/**
 * The persistent class for the siac_t_cassa_econ_operaz_num database table.
 * 
 */
@Entity
@Table(name="siac_t_cassa_econ_operaz_num")
@NamedQuery(name="SiacTCassaEconOperazNum.findAll", query="SELECT s FROM SiacTCassaEconOperazNum s")
public class SiacTCassaEconOperazNum extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_CASSA_ECON_OPERAZ_NUM_CASSAECONOPNUMID_GENERATOR", allocationSize = 1, sequenceName="SIAC_T_CASSA_ECON_OPERAZ_NUM_CASSAECONOP_NUM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_CASSA_ECON_OPERAZ_NUM_CASSAECONOPNUMID_GENERATOR")
	@Column(name="cassaeconop_num_id")
	private Integer cassaeconopNumId;

	@Version
	@Column(name="cassaeconop_numero")
	private Integer cassaeconopNumero;

	//bi-directional many-to-one association to SiacTCassaEcon
	@ManyToOne
	@JoinColumn(name="cassaecon_id")
	private SiacTCassaEcon siacTCassaEcon;

	public SiacTCassaEconOperazNum() {
	}

	public Integer getCassaeconopNumId() {
		return cassaeconopNumId;
	}

	public void setCassaeconopNumId(Integer cassaeconopNumId) {
		this.cassaeconopNumId = cassaeconopNumId;
	}

	public Integer getCassaeconopNumero() {
		return cassaeconopNumero;
	}

	public void setCassaeconopNumero(Integer cassaeconopNumero) {
		this.cassaeconopNumero = cassaeconopNumero;
	}

	public SiacTCassaEcon getSiacTCassaEcon() {
		return siacTCassaEcon;
	}

	public void setSiacTCassaEcon(SiacTCassaEcon siacTCassaEcon) {
		this.siacTCassaEcon = siacTCassaEcon;
	}

	@Override
	public Integer getUid() {
		return cassaeconopNumId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cassaeconopNumId = uid;
		
	}

}