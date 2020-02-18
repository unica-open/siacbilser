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

@Entity
@Table(name = "siac_d_class_fam")
public class SiacCodiceCodificaFamigliaDto extends SiacTEnteBase {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8417779579485727810L;

	@Id
	@Column(name = "classif_fam_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_D_CLASS_FAM")
	@SequenceGenerator(name = "SEQ_D_CLASS_FAM", sequenceName = "siac_d_class_fam_classif_fam_id_seq")
	private int uid;
	
	@Basic
	@Column(name = "classif_fam_code")
	private String codice;
	
	@Basic
	@Column(name = "classif_fam_desc")
	private String descrizione;

	
	@Override
	public Integer getUid() {
		return uid;
	}
	
	@Override
	public void setUid(Integer uid) {
		this.uid= uid;
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
