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
 * The persistent class for the siac_r_causale_ep_tipo_evento_tipo database table.
 * 
 */
@Entity
@Table(name="siac_r_causale_ep_tipo_evento_tipo")
@NamedQuery(name="SiacRCausaleEpTipoEventoTipo.findAll", query="SELECT s FROM SiacRCausaleEpTipoEventoTipo s")
public class SiacRCausaleEpTipoEventoTipo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CAUSALE_EP_TIPO_EVENTO_TIPO_CEPEVTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CAUSALE_EP_TIPO_EVENTO_TIPO_CEPEV_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CAUSALE_EP_TIPO_EVENTO_TIPO_CEPEVTIPOID_GENERATOR")
	@Column(name="cepev_tipo_id")
	private Integer cepevTipoId;
	
	//bi-directional many-to-one association to SiacDCausaleEpTipo
	@ManyToOne
	@JoinColumn(name="causale_ep_tipo_id")
	private SiacDCausaleEpTipo siacDCausaleEpTipo;

	//bi-directional many-to-one association to SiacDEventoTipo
	@ManyToOne
	@JoinColumn(name="evento_tipo_id")
	private SiacDEventoTipo siacDEventoTipo;

	public SiacRCausaleEpTipoEventoTipo() {
	}

	public Integer getCepevTipoId() {
		return this.cepevTipoId;
	}

	public void setCepevTipoId(Integer cepevTipoId) {
		this.cepevTipoId = cepevTipoId;
	}

	public SiacDCausaleEpTipo getSiacDCausaleEpTipo() {
		return siacDCausaleEpTipo;
	}

	public void setSiacDCausaleEpTipo(SiacDCausaleEpTipo siacDCausaleEpTipo) {
		this.siacDCausaleEpTipo = siacDCausaleEpTipo;
	}

	public SiacDEventoTipo getSiacDEventoTipo() {
		return siacDEventoTipo;
	}

	public void setSiacDEventoTipo(SiacDEventoTipo siacDEventoTipo) {
		this.siacDEventoTipo = siacDEventoTipo;
	}

	@Override
	public Integer getUid() {
		return this.cepevTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cepevTipoId = uid;
	}

}