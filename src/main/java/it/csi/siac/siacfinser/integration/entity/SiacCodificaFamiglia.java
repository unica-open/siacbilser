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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;

@Entity
@Table(name = "siac_t_class_fam_tree")
public class SiacCodificaFamiglia extends SiacTEnteBase {

	/** Per la serializzazione */
	private static final long serialVersionUID = -3253837877761133705L;

	@Id
	@Column(name = "classif_fam_tree_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CLASS_FAM_TREE")
	@SequenceGenerator(name = "SEQ_CLASS_FAM_TREE", sequenceName = "siac_t_class_fam_tree_f_classif_fam_tree_id_seq")
	private int uid;

	@Basic
	@Column(name = "class_fam_code")
	private String codice;
	
	@Basic
	@Column(name = "class_fam_desc")
	private String descrizione;
	
	@ManyToOne
	@JoinColumn(name="classif_fam_id")
	private SiacCodiceCodificaFamigliaDto codiceCodificaFamigliaDto;
	
	
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

	public SiacCodiceCodificaFamigliaDto getCodiceCodificaFamigliaDto() {
		return codiceCodificaFamigliaDto;
	}

	public void setCodiceCodificaFamigliaDto(
			SiacCodiceCodificaFamigliaDto codiceCodificaFamigliaDto) {
		this.codiceCodificaFamigliaDto = codiceCodificaFamigliaDto;
	}

	

}
