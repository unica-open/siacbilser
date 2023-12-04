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
 * The persistent class for the siac_r_atto_allegato_stampa_tipo_template database table.
 * 
 */
@Entity
@Table(name="siac_r_atto_allegato_stampa_tipo_template")
@NamedQuery(name="SiacRAttoAllegatoStampaTipoTemplate.findAll", query="SELECT s FROM SiacRAttoAllegatoStampaTipoTemplate s")
public class SiacRAttoAllegatoStampaTipoTemplate extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_ATTO_ALLEGATO_STAMPA_TIPO_TEMPLATE_ATTOALSTTPLFILEID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_ATTO_ALLEGATO_STAMPA_TIPO_TEMPLATE_ATTOALSTTPL_FILE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ATTO_ALLEGATO_STAMPA_TIPO_TEMPLATE_ATTOALSTTPLFILEID_GENERATOR")
	@Column(name="attoalsttpl_file_id")
	private Integer attoalsttplFileId;

	//bi-directional many-to-one association to SiacDAttoAllegatoStampaTipo
	@ManyToOne
	@JoinColumn(name="attoalst_tipo_id")
	private SiacDAttoAllegatoStampaTipo siacDAttoAllegatoStampaTipo;

	//bi-directional many-to-one association to SiacTRepjTemplate
	@ManyToOne
	@JoinColumn(name="repjt_id")
	private SiacTRepjTemplate siacTRepjTemplate;

	public SiacRAttoAllegatoStampaTipoTemplate() {
	}

	public Integer getAttoalsttplFileId() {
		return this.attoalsttplFileId;
	}

	public void setAttoalsttplFileId(Integer attoalsttplFileId) {
		this.attoalsttplFileId = attoalsttplFileId;
	}

	public SiacDAttoAllegatoStampaTipo getSiacDAttoAllegatoStampaTipo() {
		return this.siacDAttoAllegatoStampaTipo;
	}

	public void setSiacDAttoAllegatoStampaTipo(SiacDAttoAllegatoStampaTipo siacDAttoAllegatoStampaTipo) {
		this.siacDAttoAllegatoStampaTipo = siacDAttoAllegatoStampaTipo;
	}

	public SiacTRepjTemplate getSiacTRepjTemplate() {
		return this.siacTRepjTemplate;
	}

	public void setSiacTRepjTemplate(SiacTRepjTemplate siacTRepjTemplate) {
		this.siacTRepjTemplate = siacTRepjTemplate;
	}

	@Override
	public Integer getUid() {
		return this.attoalsttplFileId;
	}

	@Override
	public void setUid(Integer uid) {
		this.attoalsttplFileId = uid;
	}

}