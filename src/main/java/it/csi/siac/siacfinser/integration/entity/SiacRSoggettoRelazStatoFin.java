/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;




/**
 * The persistent class for the siac_r_soggetto_relaz_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_soggetto_relaz_stato")
public class SiacRSoggettoRelazStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_SOGGETTO_RELAZ_STATO_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_soggetto_relaz_stato_soggetto_relaz_stato_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SOGGETTO_RELAZ_STATO_ID_GENERATOR")
	@Column(name="soggetto_relaz_stato_id")
	private Integer soggettoRelazStatoId;

	//bi-directional many-to-one association to SiacDRelazStatoFin
	@ManyToOne
	@JoinColumn(name="relaz_stato_id")
	private SiacDRelazStatoFin siacDRelazStato;

	//bi-directional many-to-one association to SiacRSoggettoRelazFin
	@ManyToOne
	@JoinColumn(name="soggetto_relaz_id")
	private SiacRSoggettoRelazFin siacRSoggettoRelaz;

	public SiacRSoggettoRelazStatoFin() {
	}

	public Integer getSoggettoRelazStatoId() {
		return this.soggettoRelazStatoId;
	}

	public void setSoggettoRelazStatoId(Integer soggettoRelazStatoId) {
		this.soggettoRelazStatoId = soggettoRelazStatoId;
	}

	public SiacDRelazStatoFin getSiacDRelazStato() {
		return this.siacDRelazStato;
	}

	public void setSiacDRelazStato(SiacDRelazStatoFin siacDRelazStato) {
		this.siacDRelazStato = siacDRelazStato;
	}

	public SiacRSoggettoRelazFin getSiacRSoggettoRelaz() {
		return this.siacRSoggettoRelaz;
	}

	public void setSiacRSoggettoRelaz(SiacRSoggettoRelazFin siacRSoggettoRelaz) {
		this.siacRSoggettoRelaz = siacRSoggettoRelaz;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.soggettoRelazStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.soggettoRelazStatoId = uid;
	}
}