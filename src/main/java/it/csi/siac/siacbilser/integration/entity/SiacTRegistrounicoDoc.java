/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.Date;

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
 * The persistent class for the siac_t_registrounico_doc database table.
 * 
 */
@Entity
@Table(name="siac_t_registrounico_doc")
@NamedQuery(name="SiacTRegistrounicoDoc.findAll", query="SELECT s FROM SiacTRegistrounicoDoc s")
public class SiacTRegistrounicoDoc extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_REGISTROUNICO_DOC_RUDOCID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_REGISTROUNICO_DOC_RUDOC_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_REGISTROUNICO_DOC_RUDOCID_GENERATOR")
	@Column(name="rudoc_id")
	private Integer rudocId;

	@Column(name="rudoc_registrazione_anno", updatable=false)
	private Integer rudocRegistrazioneAnno;

	@Column(name="rudoc_registrazione_data", updatable=false)
	private Date rudocRegistrazioneData;

	@Column(name="rudoc_registrazione_numero", updatable=false)
	private Integer rudocRegistrazioneNumero;
	
	//bi-directional many-to-one association to SiacTDoc
	@ManyToOne
	@JoinColumn(name="doc_id", updatable=false)
	private SiacTDoc siacTDoc;

	public SiacTRegistrounicoDoc() {
	}

	public Integer getRudocId() {
		return this.rudocId;
	}

	public void setRudocId(Integer rudocId) {
		this.rudocId = rudocId;
	}
	
	public Integer getRudocRegistrazioneAnno() {
		return this.rudocRegistrazioneAnno;
	}

	public void setRudocRegistrazioneAnno(Integer rudocRegistrazioneAnno) {
		this.rudocRegistrazioneAnno = rudocRegistrazioneAnno;
	}

	public Date getRudocRegistrazioneData() {
		return this.rudocRegistrazioneData;
	}

	public void setRudocRegistrazioneData(Date rudocRegistrazioneData) {
		this.rudocRegistrazioneData = rudocRegistrazioneData;
	}

	public Integer getRudocRegistrazioneNumero() {
		return this.rudocRegistrazioneNumero;
	}

	public void setRudocRegistrazioneNumero(Integer rudocRegistrazioneNumero) {
		this.rudocRegistrazioneNumero = rudocRegistrazioneNumero;
	}

	public SiacTDoc getSiacTDoc() {
		return this.siacTDoc;
	}

	public void setSiacTDoc(SiacTDoc siacTDoc) {
		this.siacTDoc = siacTDoc;
	}

	@Override
	public Integer getUid() {
		return rudocId;
	}

	@Override
	public void setUid(Integer uid) {
		this.rudocId = uid;
	}

}