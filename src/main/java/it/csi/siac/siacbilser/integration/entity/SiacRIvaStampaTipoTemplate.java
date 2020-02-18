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
 * The persistent class for the siac_r_iva_stampa_tipo_template database table.
 * 
 */
@Entity
@Table(name="siac_r_iva_stampa_tipo_template")
@NamedQuery(name="SiacRIvaStampaTipoTemplate.findAll", query="SELECT s FROM SiacRIvaStampaTipoTemplate s")
public class SiacRIvaStampaTipoTemplate extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_IVA_STAMPA_TIPO_TEMPLATE_IVASTTPLFILEID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_IVA_STAMPA_TIPO_TEMPLATE_IVASTTPL_FILE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_IVA_STAMPA_TIPO_TEMPLATE_IVASTTPLFILEID_GENERATOR")
	@Column(name="ivasttpl_file_id")
	private Integer ivasttplFileId;

	//bi-directional many-to-one association to SiacDIvaStampaTipo
	@ManyToOne
	@JoinColumn(name="ivast_tipo_id")
	private SiacDIvaStampaTipo siacDIvaStampaTipo;

	//bi-directional many-to-one association to SiacTRepjTemplate
	@ManyToOne
	@JoinColumn(name="repjt_id")
	private SiacTRepjTemplate siacTRepjTemplate;

	public SiacRIvaStampaTipoTemplate() {
	}

	public Integer getIvasttplFileId() {
		return this.ivasttplFileId;
	}

	public void setIvasttplFileId(Integer ivasttplFileId) {
		this.ivasttplFileId = ivasttplFileId;
	}

	public SiacDIvaStampaTipo getSiacDIvaStampaTipo() {
		return this.siacDIvaStampaTipo;
	}

	public void setSiacDIvaStampaTipo(SiacDIvaStampaTipo siacDIvaStampaTipo) {
		this.siacDIvaStampaTipo = siacDIvaStampaTipo;
	}

	public SiacTRepjTemplate getSiacTRepjTemplate() {
		return this.siacTRepjTemplate;
	}

	public void setSiacTRepjTemplate(SiacTRepjTemplate siacTRepjTemplate) {
		this.siacTRepjTemplate = siacTRepjTemplate;
	}

	@Override
	public Integer getUid() {
		return ivasttplFileId;
	}

	@Override
	public void setUid(Integer uid) {
		this.ivasttplFileId = uid;
	}

}