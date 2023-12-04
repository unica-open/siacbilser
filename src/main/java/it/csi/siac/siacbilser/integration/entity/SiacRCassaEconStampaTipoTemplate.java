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
 * The persistent class for the siac_r_cassa_econ_stampa_tipo_template database table.
 * 
 */
@Entity
@Table(name="siac_r_cassa_econ_stampa_tipo_template")
@NamedQuery(name="SiacRCassaEconStampaTipoTemplate.findAll", query="SELECT s FROM SiacRCassaEconStampaTipoTemplate s")
public class SiacRCassaEconStampaTipoTemplate extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CASSA_ECON_STAMPA_TIPO_TEMPLATE_CESTTPLFILEID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CASSA_ECON_STAMPA_TIPO_TEMPLATE_CESTTPL_FILE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CASSA_ECON_STAMPA_TIPO_TEMPLATE_CESTTPLFILEID_GENERATOR")
	@Column(name="cesttpl_file_id")
	private Integer cesttplFileId;

	//bi-directional many-to-one association to SiacDCassaEconStampaTipo
	@ManyToOne
	@JoinColumn(name="cest_tipo_id")
	private SiacDCassaEconStampaTipo siacDCassaEconStampaTipo;

	//bi-directional many-to-one association to SiacTRepjTemplate
	@ManyToOne
	@JoinColumn(name="repjt_id")
	private SiacTRepjTemplate siacTRepjTemplate;

	public SiacRCassaEconStampaTipoTemplate() {
	}

	public Integer getCesttplFileId() {
		return this.cesttplFileId;
	}

	public void setCesttplFileId(Integer cesttplFileId) {
		this.cesttplFileId = cesttplFileId;
	}

	public SiacDCassaEconStampaTipo getSiacDCassaEconStampaTipo() {
		return this.siacDCassaEconStampaTipo;
	}

	public void setSiacDCassaEconStampaTipo(SiacDCassaEconStampaTipo siacDCassaEconStampaTipo) {
		this.siacDCassaEconStampaTipo = siacDCassaEconStampaTipo;
	}

	public SiacTRepjTemplate getSiacTRepjTemplate() {
		return this.siacTRepjTemplate;
	}

	public void setSiacTRepjTemplate(SiacTRepjTemplate siacTRepjTemplate) {
		this.siacTRepjTemplate = siacTRepjTemplate;
	}

	@Override
	public Integer getUid() {
		return this.cesttplFileId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cesttplFileId = uid;
	}

}