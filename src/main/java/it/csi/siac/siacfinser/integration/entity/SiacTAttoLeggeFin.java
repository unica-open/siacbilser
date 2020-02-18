/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_t_atto_legge database table.
 * 
 */
@Entity
@Table(name="siac_t_atto_legge")
public class SiacTAttoLeggeFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="attolegge_id")
	private Integer attoleggeId;

	@Column(name="attolegge_anno")
	private String attoleggeAnno;

	@Column(name="attolegge_articolo")
	private String attoleggeArticolo;

	@Column(name="attolegge_comma")
	private String attoleggeComma;

	@Column(name="attolegge_numero")
	private Integer attoleggeNumero;

	@Column(name="attolegge_punto")
	private String attoleggePunto;

	//bi-directional many-to-one association to SiacRAttoLeggeStatoFin
	@OneToMany(mappedBy="siacTAttoLegge")
	private List<SiacRAttoLeggeStatoFin> siacRAttoLeggeStatos;

	//bi-directional many-to-one association to SiacDAttoLeggeTipoFin
	@ManyToOne
	@JoinColumn(name="attolegge_tipo_id")
	private SiacDAttoLeggeTipoFin siacDAttoLeggeTipo;

	public SiacTAttoLeggeFin() {
	}

	public Integer getAttoleggeId() {
		return this.attoleggeId;
	}

	public void setAttoleggeId(Integer attoleggeId) {
		this.attoleggeId = attoleggeId;
	}

	public String getAttoleggeAnno() {
		return this.attoleggeAnno;
	}

	public void setAttoleggeAnno(String attoleggeAnno) {
		this.attoleggeAnno = attoleggeAnno;
	}

	public String getAttoleggeArticolo() {
		return this.attoleggeArticolo;
	}

	public void setAttoleggeArticolo(String attoleggeArticolo) {
		this.attoleggeArticolo = attoleggeArticolo;
	}

	public String getAttoleggeComma() {
		return this.attoleggeComma;
	}

	public void setAttoleggeComma(String attoleggeComma) {
		this.attoleggeComma = attoleggeComma;
	}

	public Integer getAttoleggeNumero() {
		return this.attoleggeNumero;
	}

	public void setAttoleggeNumero(Integer attoleggeNumero) {
		this.attoleggeNumero = attoleggeNumero;
	}

	public String getAttoleggePunto() {
		return this.attoleggePunto;
	}

	public void setAttoleggePunto(String attoleggePunto) {
		this.attoleggePunto = attoleggePunto;
	}

	public List<SiacRAttoLeggeStatoFin> getSiacRAttoLeggeStatos() {
		return this.siacRAttoLeggeStatos;
	}

	public void setSiacRAttoLeggeStatos(List<SiacRAttoLeggeStatoFin> siacRAttoLeggeStatos) {
		this.siacRAttoLeggeStatos = siacRAttoLeggeStatos;
	}

	public SiacRAttoLeggeStatoFin addSiacRAttoLeggeStato(SiacRAttoLeggeStatoFin siacRAttoLeggeStato) {
		getSiacRAttoLeggeStatos().add(siacRAttoLeggeStato);
		siacRAttoLeggeStato.setSiacTAttoLegge(this);

		return siacRAttoLeggeStato;
	}

	public SiacRAttoLeggeStatoFin removeSiacRAttoLeggeStato(SiacRAttoLeggeStatoFin siacRAttoLeggeStato) {
		getSiacRAttoLeggeStatos().remove(siacRAttoLeggeStato);
		siacRAttoLeggeStato.setSiacTAttoLegge(null);

		return siacRAttoLeggeStato;
	}

	public SiacDAttoLeggeTipoFin getSiacDAttoLeggeTipo() {
		return this.siacDAttoLeggeTipo;
	}

	public void setSiacDAttoLeggeTipo(SiacDAttoLeggeTipoFin siacDAttoLeggeTipo) {
		this.siacDAttoLeggeTipo = siacDAttoLeggeTipo;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.attoleggeId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.attoleggeId = uid;
	}
}