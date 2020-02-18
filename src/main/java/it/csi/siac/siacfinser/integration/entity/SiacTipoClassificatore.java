/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;

/**
 * 
 * SiacTipoClassificatore: identifica il tipo di classificatore
 * 
 * @author rmontuori
 * 
 */
@Entity
@Table(name = "siac_d_class_tipo")
public class SiacTipoClassificatore extends SiacTEnteBase {

	/** Per la serializzazione */
	private static final long serialVersionUID = 5871308347220694808L;

	@Id
	@Column(name = "classif_tipo_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_D_CLASS_TIPO")
	@SequenceGenerator(name = "SEQ_D_CLASS_TIPO", sequenceName = "siac_d_class_tipo_classif_tipo_id_seq")
	private int uid;

	@Basic
	@Column(name = "classif_tipo_code")
	private String codice;

	@Basic
	@Column(name = "classif_tipo_desc")
	private String descrizione;

	@Override
	public Integer getUid() {
		return uid;
	}

	@Override
	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

}
