/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.sql.Timestamp;

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
 * The persistent class for the siac_r_bil_elem_atto_legge database table.
 * 
 */
@Entity
@Table(name="siac_r_bil_elem_atto_legge")
public class SiacRBilElemAttoLeggeFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_BIL_ELEM_ATTO_LEGGE_ATTOLEGGE_BIL_ELEM_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_bil_elem_atto_legge_attolegge_bil_elem_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_BIL_ELEM_ATTO_LEGGE_ATTOLEGGE_BIL_ELEM_ID_GENERATOR")
	@Column(name="attolegge_bil_elem_id")
	private Integer attoleggeBilElemId;

	@Column(name="attolegge_id")
	private Integer attoleggeId;

	private String descrizione;

	@Column(name="finanziamento_fine")
	private Timestamp finanziamentoFine;

	@Column(name="finanziamento_inizio")
	private Timestamp finanziamentoInizio;

	private String gerarchia;

	//bi-directional many-to-one association to SiacTBilElemFin
	@ManyToOne
	@JoinColumn(name="elem_id")
	private SiacTBilElemFin siacTBilElem;

	public SiacRBilElemAttoLeggeFin() {
	}

	public Integer getAttoleggeBilElemId() {
		return this.attoleggeBilElemId;
	}

	public void setAttoleggeBilElemId(Integer attoleggeBilElemId) {
		this.attoleggeBilElemId = attoleggeBilElemId;
	}

	public Integer getAttoleggeId() {
		return this.attoleggeId;
	}

	public void setAttoleggeId(Integer attoleggeId) {
		this.attoleggeId = attoleggeId;
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Timestamp getFinanziamentoFine() {
		return this.finanziamentoFine;
	}

	public void setFinanziamentoFine(Timestamp finanziamentoFine) {
		this.finanziamentoFine = finanziamentoFine;
	}

	public Timestamp getFinanziamentoInizio() {
		return this.finanziamentoInizio;
	}

	public void setFinanziamentoInizio(Timestamp finanziamentoInizio) {
		this.finanziamentoInizio = finanziamentoInizio;
	}

	public String getGerarchia() {
		return this.gerarchia;
	}

	public void setGerarchia(String gerarchia) {
		this.gerarchia = gerarchia;
	}

	public SiacTBilElemFin getSiacTBilElem() {
		return this.siacTBilElem;
	}

	public void setSiacTBilElem(SiacTBilElemFin siacTBilElem) {
		this.siacTBilElem = siacTBilElem;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.attoleggeBilElemId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.attoleggeBilElemId = uid;
	}

}