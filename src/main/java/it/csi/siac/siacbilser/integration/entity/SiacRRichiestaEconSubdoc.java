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
 * The persistent class for the siac_r_richiesta_econ_subdoc database table.
 * 
 */
@Entity
@Table(name="siac_r_richiesta_econ_subdoc")
@NamedQuery(name="SiacRRichiestaEconSubdoc.findAll", query="SELECT s FROM SiacRRichiestaEconSubdoc s")
public class SiacRRichiestaEconSubdoc extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_RICHIESTA_ECON_SUBDOC_RICECONSUBDOCID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_RICHIESTA_ECON_SUBDOC_RICECONSUBDOC_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_RICHIESTA_ECON_SUBDOC_RICECONSUBDOCID_GENERATOR")
	@Column(name="riceconsubdoc_id")
	private Integer riceconsubdocId;

	//bi-directional many-to-one association to SiacTRichiestaEcon
	@ManyToOne
	@JoinColumn(name="ricecon_id")
	private SiacTRichiestaEcon siacTRichiestaEcon;

	//bi-directional many-to-one association to SiacTSubdoc
	@ManyToOne
	@JoinColumn(name="subdoc_id")
	private SiacTSubdoc siacTSubdoc;

	public SiacRRichiestaEconSubdoc() {
	}

	public Integer getRiceconsubdocId() {
		return this.riceconsubdocId;
	}

	public void setRiceconsubdocId(Integer riceconsubdocId) {
		this.riceconsubdocId = riceconsubdocId;
	}

	public SiacTRichiestaEcon getSiacTRichiestaEcon() {
		return siacTRichiestaEcon;
	}

	public void setSiacTRichiestaEcon(SiacTRichiestaEcon siacTRichiestaEcon) {
		this.siacTRichiestaEcon = siacTRichiestaEcon;
	}

	public SiacTSubdoc getSiacTSubdoc() {
		return this.siacTSubdoc;
	}

	public void setSiacTSubdoc(SiacTSubdoc siacTSubdoc) {
		this.siacTSubdoc = siacTSubdoc;
	}

	@Override
	public Integer getUid() {
		return this.riceconsubdocId;
	}

	@Override
	public void setUid(Integer uid) {
		this.riceconsubdocId = uid;
	}

}