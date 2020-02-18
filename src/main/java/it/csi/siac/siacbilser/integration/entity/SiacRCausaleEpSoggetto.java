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
 * The persistent class for the siac_r_causale_ep_soggetto database table.
 * 
 */
@Entity
@Table(name="siac_r_causale_ep_soggetto")
@NamedQuery(name="SiacRCausaleEpSoggetto.findAll", query="SELECT s FROM SiacRCausaleEpSoggetto s")
public class SiacRCausaleEpSoggetto extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CAUSALE_EP_SOGGETTO_CAUSALEEPSOGGETTOID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CAUSALE_EP_SOGGETTO_CAUSALE_EP_SOGGETTO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CAUSALE_EP_SOGGETTO_CAUSALEEPSOGGETTOID_GENERATOR")
	@Column(name="causale_ep_soggetto_id")
	private Integer causaleEpSoggettoId;

	//bi-directional many-to-one association to SiacTCausaleEp
	@ManyToOne
	@JoinColumn(name="causale_ep_id")
	private SiacTCausaleEp siacTCausaleEp;

	//bi-directional many-to-one association to SiacTSoggetto
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggetto siacTSoggetto;

	public SiacRCausaleEpSoggetto() {
	}

	public Integer getCausaleEpSoggettoId() {
		return this.causaleEpSoggettoId;
	}

	public void setCausaleEpSoggettoId(Integer causaleEpSoggettoId) {
		this.causaleEpSoggettoId = causaleEpSoggettoId;
	}

	public SiacTCausaleEp getSiacTCausaleEp() {
		return this.siacTCausaleEp;
	}

	public void setSiacTCausaleEp(SiacTCausaleEp siacTCausaleEp) {
		this.siacTCausaleEp = siacTCausaleEp;
	}

	public SiacTSoggetto getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	public void setSiacTSoggetto(SiacTSoggetto siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	@Override
	public Integer getUid() {
		return this.causaleEpSoggettoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.causaleEpSoggettoId = uid;
	}

}