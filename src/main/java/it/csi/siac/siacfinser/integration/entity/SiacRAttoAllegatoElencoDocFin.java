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
 * The persistent class for the siac_r_atto_allegato_elenco_doc database table.
 * 
 */
@Entity
@Table(name="siac_r_atto_allegato_elenco_doc")
public class SiacRAttoAllegatoElencoDocFin extends SiacTEnteBase  {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_ATTO_ALLEGATO_ELENCO_DOC_ATTOALEDOC_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_atto_allegato_elenco_doc_attoaleldoc_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ATTO_ALLEGATO_ELENCO_DOC_ATTOALEDOC_ID_GENERATOR")
	@Column(name="attoaleldoc_id")
	private Integer attoaleldocId;

	//bi-directional many-to-one association to SiacTAttoAllegato
	@ManyToOne
	@JoinColumn(name="attoal_id")
	private SiacTAllegatoAttoFin siacTAttoAllegato;

	//bi-directional many-to-one association to SiacTElencoDoc
	@ManyToOne
	@JoinColumn(name="eldoc_id")
	private SiacTElencoDocFin siacTElencoDoc;

	public Integer getAttoaleldocId() {
		return this.attoaleldocId;
	}

	public void setAttoaleldocId(Integer attoaleldocId) {
		this.attoaleldocId = attoaleldocId;
	}

	public SiacTAllegatoAttoFin getSiacTAttoAllegato() {
		return this.siacTAttoAllegato;
	}

	public void setSiacTAttoAllegato(SiacTAllegatoAttoFin siacRAttoAllegato) {
		this.siacTAttoAllegato = siacRAttoAllegato;
	}

	public SiacTElencoDocFin getSiacTElencoDoc() {
		return this.siacTElencoDoc;
	}

	public void setSiacTElencoDoc(SiacTElencoDocFin siacTElencoDoc) {
		this.siacTElencoDoc = siacTElencoDoc;
	}

	@Override
	public Integer getUid() {
		return attoaleldocId;
	}

	@Override
	public void setUid(Integer uid) {
		this.attoaleldocId = uid;
	}

}