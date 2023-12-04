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
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the siac_t_registrounico_doc_num database table.
 * 
 */
@Entity
@Table(name="siac_t_registrounico_doc_num")
@NamedQuery(name="SiacTRegistrounicoDocNum.findAll", query="SELECT s FROM SiacTRegistrounicoDocNum s")
public class SiacTRegistrounicoDocNum extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_REGISTROUNICO_DOC_NUM_RUDOCNUMID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_REGISTROUNICO_DOC_NUM_RUDOC_NUM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_REGISTROUNICO_DOC_NUM_RUDOCNUMID_GENERATOR")
	@Column(name="rudoc_num_id")
	private Integer rudocNumId;

	@Column(name="rudoc_registrazione_anno")
	private Integer rudocRegistrazioneAnno;

	@Column(name="rudoc_registrazione_numero")
	@Version
	private Integer rudocRegistrazioneNumero;

	public SiacTRegistrounicoDocNum() {
	}

	public Integer getRudocNumId() {
		return this.rudocNumId;
	}

	public void setRudocNumId(Integer rudocNumId) {
		this.rudocNumId = rudocNumId;
	}

	public Integer getRudocRegistrazioneAnno() {
		return this.rudocRegistrazioneAnno;
	}

	public void setRudocRegistrazioneAnno(Integer rudocRegistrazioneAnno) {
		this.rudocRegistrazioneAnno = rudocRegistrazioneAnno;
	}

	public Integer getRudocRegistrazioneNumero() {
		return this.rudocRegistrazioneNumero;
	}

	public void setRudocRegistrazioneNumero(Integer rudocRegistrazioneNumero) {
		this.rudocRegistrazioneNumero = rudocRegistrazioneNumero;
	}

	@Override
	public Integer getUid() {
		return rudocNumId;
	}

	@Override
	public void setUid(Integer uid) {
		this.rudocNumId  = uid;
	}

}