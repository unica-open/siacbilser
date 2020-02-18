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
 * The persistent class for the siac_r_cassa_econ_operaz_stampa database table.
 * 
 */
@Entity
@Table(name="siac_r_cassa_econ_operaz_stampa")
@NamedQuery(name="SiacRCassaEconOperazStampa.findAll", query="SELECT s FROM SiacRCassaEconOperazStampa s")
public class SiacRCassaEconOperazStampa extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CASSA_ECON_OPERAZ_STAMPA_CESTRID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CASSA_ECON_OPERAZ_STAMPA_CEST_R_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CASSA_ECON_OPERAZ_STAMPA_CESTRID_GENERATOR")
	@Column(name="cest_r_id")
	private Integer cestRId;

	//bi-directional many-to-one association to SiacTCassaEconOperaz
	@ManyToOne
	@JoinColumn(name="cassaeconop_id")
	private SiacTCassaEconOperaz siacTCassaEconOperaz;

	//bi-directional many-to-one association to SiacTCassaEconStampa
	@ManyToOne
	@JoinColumn(name="cest_id")
	private SiacTCassaEconStampa siacTCassaEconStampa;

	public SiacRCassaEconOperazStampa() {
	}

	public Integer getCestRId() {
		return this.cestRId;
	}

	public void setCestRId(Integer cestRId) {
		this.cestRId = cestRId;
	}

	public SiacTCassaEconOperaz getSiacTCassaEconOperaz() {
		return this.siacTCassaEconOperaz;
	}

	public void setSiacTCassaEconOperaz(SiacTCassaEconOperaz siacTCassaEconOperaz) {
		this.siacTCassaEconOperaz = siacTCassaEconOperaz;
	}

	public SiacTCassaEconStampa getSiacTCassaEconStampa() {
		return this.siacTCassaEconStampa;
	}

	public void setSiacTCassaEconStampa(SiacTCassaEconStampa siacTCassaEconStampa) {
		this.siacTCassaEconStampa = siacTCassaEconStampa;
	}

	@Override
	public Integer getUid() {
		return this.cestRId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cestRId = uid;
		
	}

}