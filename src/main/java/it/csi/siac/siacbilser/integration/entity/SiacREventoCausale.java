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
 * The persistent class for the siac_r_evento_causale database table.
 * 
 */
@Entity
@Table(name="siac_r_evento_causale")
@NamedQuery(name="SiacREventoCausale.findAll", query="SELECT s FROM SiacREventoCausale s")
public class SiacREventoCausale extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_EVENTO_CAUSALE_CAUSALEEPSOGGETTOID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_EVENTO_CAUSALE_CAUSALE_EP_SOGGETTO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_EVENTO_CAUSALE_CAUSALEEPSOGGETTOID_GENERATOR")
	@Column(name="causale_ep_soggetto_id")
	private Integer causaleEpSoggettoId;

	//bi-directional many-to-one association to SiacDEvento
	@ManyToOne
	@JoinColumn(name="evento_id")
	private SiacDEvento siacDEvento;

	//bi-directional many-to-one association to SiacTCausaleEp
	@ManyToOne
	@JoinColumn(name="causale_ep_id")
	private SiacTCausaleEp siacTCausaleEp;

	public SiacREventoCausale() {
	}

	public Integer getCausaleEpSoggettoId() {
		return this.causaleEpSoggettoId;
	}

	public void setCausaleEpSoggettoId(Integer causaleEpSoggettoId) {
		this.causaleEpSoggettoId = causaleEpSoggettoId;
	}

	public SiacDEvento getSiacDEvento() {
		return this.siacDEvento;
	}

	public void setSiacDEvento(SiacDEvento siacDEvento) {
		this.siacDEvento = siacDEvento;
	}

	public SiacTCausaleEp getSiacTCausaleEp() {
		return this.siacTCausaleEp;
	}

	public void setSiacTCausaleEp(SiacTCausaleEp siacTCausaleEp) {
		this.siacTCausaleEp = siacTCausaleEp;
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