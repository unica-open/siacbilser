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
 * The persistent class for the siac_d_onere database table.
 * 
 */
@Entity
@Table(name="siac_d_onere")
public class SiacDOnereFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="onere_id")
	private Integer onereId;

	@Column(name="onere_code")
	private String onereCode;

	@Column(name="onere_desc")
	private String onereDesc;

	//bi-directional many-to-one association to SiacDOnereTipoFin
	@ManyToOne
	@JoinColumn(name="onere_tipo_id")
	private SiacDOnereTipoFin siacDOnereTipo;

	//bi-directional many-to-one association to SiacROnereAttrFin
	@OneToMany(mappedBy="siacDOnere")
	private List<SiacROnereAttrFin> siacROnereAttrs;

	//bi-directional many-to-one association to SiacRSoggettoOnereFin
	@OneToMany(mappedBy="siacDOnere")
	private List<SiacRSoggettoOnereFin> siacRSoggettoOneres;

	//bi-directional many-to-one association to SiacRSoggettoOnereModFin
	@OneToMany(mappedBy="siacDOnere")
	private List<SiacRSoggettoOnereModFin> siacRSoggettoOnereMods;

	public SiacDOnereFin() {
	}

	public Integer getOnereId() {
		return this.onereId;
	}

	public void setOnereId(Integer onereId) {
		this.onereId = onereId;
	}

	public String getOnereCode() {
		return this.onereCode;
	}

	public void setOnereCode(String onereCode) {
		this.onereCode = onereCode;
	}

	public String getOnereDesc() {
		return this.onereDesc;
	}

	public void setOnereDesc(String onereDesc) {
		this.onereDesc = onereDesc;
	}

	public SiacDOnereTipoFin getSiacDOnereTipo() {
		return this.siacDOnereTipo;
	}

	public void setSiacDOnereTipo(SiacDOnereTipoFin siacDOnereTipo) {
		this.siacDOnereTipo = siacDOnereTipo;
	}

	public List<SiacROnereAttrFin> getSiacROnereAttrs() {
		return this.siacROnereAttrs;
	}

	public void setSiacROnereAttrs(List<SiacROnereAttrFin> siacROnereAttrs) {
		this.siacROnereAttrs = siacROnereAttrs;
	}

	public SiacROnereAttrFin addSiacROnereAttr(SiacROnereAttrFin siacROnereAttr) {
		getSiacROnereAttrs().add(siacROnereAttr);
		siacROnereAttr.setSiacDOnere(this);

		return siacROnereAttr;
	}

	public SiacROnereAttrFin removeSiacROnereAttr(SiacROnereAttrFin siacROnereAttr) {
		getSiacROnereAttrs().remove(siacROnereAttr);
		siacROnereAttr.setSiacDOnere(null);

		return siacROnereAttr;
	}

	public List<SiacRSoggettoOnereFin> getSiacRSoggettoOneres() {
		return this.siacRSoggettoOneres;
	}

	public void setSiacRSoggettoOneres(List<SiacRSoggettoOnereFin> siacRSoggettoOneres) {
		this.siacRSoggettoOneres = siacRSoggettoOneres;
	}

	public SiacRSoggettoOnereFin addSiacRSoggettoOnere(SiacRSoggettoOnereFin siacRSoggettoOnere) {
		getSiacRSoggettoOneres().add(siacRSoggettoOnere);
		siacRSoggettoOnere.setSiacDOnere(this);

		return siacRSoggettoOnere;
	}

	public SiacRSoggettoOnereFin removeSiacRSoggettoOnere(SiacRSoggettoOnereFin siacRSoggettoOnere) {
		getSiacRSoggettoOneres().remove(siacRSoggettoOnere);
		siacRSoggettoOnere.setSiacDOnere(null);

		return siacRSoggettoOnere;
	}

	public List<SiacRSoggettoOnereModFin> getSiacRSoggettoOnereMods() {
		return this.siacRSoggettoOnereMods;
	}

	public void setSiacRSoggettoOnereMods(List<SiacRSoggettoOnereModFin> siacRSoggettoOnereMods) {
		this.siacRSoggettoOnereMods = siacRSoggettoOnereMods;
	}

	public SiacRSoggettoOnereModFin addSiacRSoggettoOnereMod(SiacRSoggettoOnereModFin siacRSoggettoOnereMod) {
		getSiacRSoggettoOnereMods().add(siacRSoggettoOnereMod);
		siacRSoggettoOnereMod.setSiacDOnere(this);

		return siacRSoggettoOnereMod;
	}

	public SiacRSoggettoOnereModFin removeSiacRSoggettoOnereMod(SiacRSoggettoOnereModFin siacRSoggettoOnereMod) {
		getSiacRSoggettoOnereMods().remove(siacRSoggettoOnereMod);
		siacRSoggettoOnereMod.setSiacDOnere(null);

		return siacRSoggettoOnereMod;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.onereId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.onereId = uid;
	}
}