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
 * The persistent class for the siac_d_causale database table.
 * 
 */
@Entity
@Table(name="siac_d_causale")
public class SiacDCausaleFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="caus_id")
	private Integer causId;

	@Column(name="caus_code")
	private String causCode;

	@Column(name="caus_desc")
	private String causDesc;

	//bi-directional many-to-one association to SiacDModelloFin
	@ManyToOne
	@JoinColumn(name="model_id")
	private SiacDModelloFin siacDModello;

	//bi-directional many-to-one association to SiacRCausaleAttoAmmFin
	@OneToMany(mappedBy="siacDCausale")
	private List<SiacRCausaleAttoAmmFin> siacRCausaleAttoAmms;

	//bi-directional many-to-one association to SiacRCausaleBilElemFin
	@OneToMany(mappedBy="siacDCausale")
	private List<SiacRCausaleBilElemFin> siacRCausaleBilElems;

	//bi-directional many-to-one association to SiacRCausaleClassFin
	@OneToMany(mappedBy="siacDCausale")
	private List<SiacRCausaleClassFin> siacRCausaleClasses;

	//bi-directional many-to-one association to SiacRCausaleModpagFin
	@OneToMany(mappedBy="siacDCausale")
	private List<SiacRCausaleModpagFin> siacRCausaleModpags;

	//bi-directional many-to-one association to SiacRCausaleMovgestTFin
	@OneToMany(mappedBy="siacDCausale")
	private List<SiacRCausaleMovgestTFin> siacRCausaleMovgestTs;

	//bi-directional many-to-one association to SiacRCausaleSoggettoFin
	@OneToMany(mappedBy="siacDCausale")
	private List<SiacRCausaleSoggettoFin> siacRCausaleSoggettos;

	//bi-directional many-to-one association to SiacRCausaleTipoFin
	@OneToMany(mappedBy="siacDCausale")
	private List<SiacRCausaleTipoFin> siacRCausaleTipos;

	//bi-directional many-to-one association to SiacRDocOnereFin
	@OneToMany(mappedBy="siacDCausale")
	private List<SiacRDocOnereFin> siacRDocOneres;

	//bi-directional many-to-one association to SiacROnereCausaleFin
	@OneToMany(mappedBy="siacDCausale")
	private List<SiacROnereCausaleFin> siacROnereCausales;

	//bi-directional many-to-one association to SiacRPredocCausaleFin
	@OneToMany(mappedBy="siacDCausale")
	private List<SiacRPredocCausaleFin> siacRPredocCausales;

	public SiacDCausaleFin() {
	}

	public Integer getCausId() {
		return this.causId;
	}

	public void setCausId(Integer causId) {
		this.causId = causId;
	}

	public String getCausCode() {
		return this.causCode;
	}

	public void setCausCode(String causCode) {
		this.causCode = causCode;
	}

	public String getCausDesc() {
		return this.causDesc;
	}

	public void setCausDesc(String causDesc) {
		this.causDesc = causDesc;
	}

	public SiacDModelloFin getSiacDModello() {
		return this.siacDModello;
	}

	public void setSiacDModello(SiacDModelloFin siacDModello) {
		this.siacDModello = siacDModello;
	}

	public List<SiacRCausaleAttoAmmFin> getSiacRCausaleAttoAmms() {
		return this.siacRCausaleAttoAmms;
	}

	public void setSiacRCausaleAttoAmms(List<SiacRCausaleAttoAmmFin> siacRCausaleAttoAmms) {
		this.siacRCausaleAttoAmms = siacRCausaleAttoAmms;
	}

	public SiacRCausaleAttoAmmFin addSiacRCausaleAttoAmm(SiacRCausaleAttoAmmFin siacRCausaleAttoAmm) {
		getSiacRCausaleAttoAmms().add(siacRCausaleAttoAmm);
		siacRCausaleAttoAmm.setSiacDCausale(this);

		return siacRCausaleAttoAmm;
	}

	public SiacRCausaleAttoAmmFin removeSiacRCausaleAttoAmm(SiacRCausaleAttoAmmFin siacRCausaleAttoAmm) {
		getSiacRCausaleAttoAmms().remove(siacRCausaleAttoAmm);
		siacRCausaleAttoAmm.setSiacDCausale(null);

		return siacRCausaleAttoAmm;
	}

	public List<SiacRCausaleBilElemFin> getSiacRCausaleBilElems() {
		return this.siacRCausaleBilElems;
	}

	public void setSiacRCausaleBilElems(List<SiacRCausaleBilElemFin> siacRCausaleBilElems) {
		this.siacRCausaleBilElems = siacRCausaleBilElems;
	}

	public SiacRCausaleBilElemFin addSiacRCausaleBilElem(SiacRCausaleBilElemFin siacRCausaleBilElem) {
		getSiacRCausaleBilElems().add(siacRCausaleBilElem);
		siacRCausaleBilElem.setSiacDCausale(this);

		return siacRCausaleBilElem;
	}

	public SiacRCausaleBilElemFin removeSiacRCausaleBilElem(SiacRCausaleBilElemFin siacRCausaleBilElem) {
		getSiacRCausaleBilElems().remove(siacRCausaleBilElem);
		siacRCausaleBilElem.setSiacDCausale(null);

		return siacRCausaleBilElem;
	}

	public List<SiacRCausaleClassFin> getSiacRCausaleClasses() {
		return this.siacRCausaleClasses;
	}

	public void setSiacRCausaleClasses(List<SiacRCausaleClassFin> siacRCausaleClasses) {
		this.siacRCausaleClasses = siacRCausaleClasses;
	}

	public SiacRCausaleClassFin addSiacRCausaleClass(SiacRCausaleClassFin siacRCausaleClass) {
		getSiacRCausaleClasses().add(siacRCausaleClass);
		siacRCausaleClass.setSiacDCausale(this);

		return siacRCausaleClass;
	}

	public SiacRCausaleClassFin removeSiacRCausaleClass(SiacRCausaleClassFin siacRCausaleClass) {
		getSiacRCausaleClasses().remove(siacRCausaleClass);
		siacRCausaleClass.setSiacDCausale(null);

		return siacRCausaleClass;
	}

	public List<SiacRCausaleModpagFin> getSiacRCausaleModpags() {
		return this.siacRCausaleModpags;
	}

	public void setSiacRCausaleModpags(List<SiacRCausaleModpagFin> siacRCausaleModpags) {
		this.siacRCausaleModpags = siacRCausaleModpags;
	}

	public SiacRCausaleModpagFin addSiacRCausaleModpag(SiacRCausaleModpagFin siacRCausaleModpag) {
		getSiacRCausaleModpags().add(siacRCausaleModpag);
		siacRCausaleModpag.setSiacDCausale(this);

		return siacRCausaleModpag;
	}

	public SiacRCausaleModpagFin removeSiacRCausaleModpag(SiacRCausaleModpagFin siacRCausaleModpag) {
		getSiacRCausaleModpags().remove(siacRCausaleModpag);
		siacRCausaleModpag.setSiacDCausale(null);

		return siacRCausaleModpag;
	}

	public List<SiacRCausaleMovgestTFin> getSiacRCausaleMovgestTs() {
		return this.siacRCausaleMovgestTs;
	}

	public void setSiacRCausaleMovgestTs(List<SiacRCausaleMovgestTFin> siacRCausaleMovgestTs) {
		this.siacRCausaleMovgestTs = siacRCausaleMovgestTs;
	}

	public SiacRCausaleMovgestTFin addSiacRCausaleMovgestT(SiacRCausaleMovgestTFin siacRCausaleMovgestT) {
		getSiacRCausaleMovgestTs().add(siacRCausaleMovgestT);
		siacRCausaleMovgestT.setSiacDCausale(this);

		return siacRCausaleMovgestT;
	}

	public SiacRCausaleMovgestTFin removeSiacRCausaleMovgestT(SiacRCausaleMovgestTFin siacRCausaleMovgestT) {
		getSiacRCausaleMovgestTs().remove(siacRCausaleMovgestT);
		siacRCausaleMovgestT.setSiacDCausale(null);

		return siacRCausaleMovgestT;
	}

	public List<SiacRCausaleSoggettoFin> getSiacRCausaleSoggettos() {
		return this.siacRCausaleSoggettos;
	}

	public void setSiacRCausaleSoggettos(List<SiacRCausaleSoggettoFin> siacRCausaleSoggettos) {
		this.siacRCausaleSoggettos = siacRCausaleSoggettos;
	}

	public SiacRCausaleSoggettoFin addSiacRCausaleSoggetto(SiacRCausaleSoggettoFin siacRCausaleSoggetto) {
		getSiacRCausaleSoggettos().add(siacRCausaleSoggetto);
		siacRCausaleSoggetto.setSiacDCausale(this);

		return siacRCausaleSoggetto;
	}

	public SiacRCausaleSoggettoFin removeSiacRCausaleSoggetto(SiacRCausaleSoggettoFin siacRCausaleSoggetto) {
		getSiacRCausaleSoggettos().remove(siacRCausaleSoggetto);
		siacRCausaleSoggetto.setSiacDCausale(null);

		return siacRCausaleSoggetto;
	}

	public List<SiacRCausaleTipoFin> getSiacRCausaleTipos() {
		return this.siacRCausaleTipos;
	}

	public void setSiacRCausaleTipos(List<SiacRCausaleTipoFin> siacRCausaleTipos) {
		this.siacRCausaleTipos = siacRCausaleTipos;
	}

	public SiacRCausaleTipoFin addSiacRCausaleTipo(SiacRCausaleTipoFin siacRCausaleTipo) {
		getSiacRCausaleTipos().add(siacRCausaleTipo);
		siacRCausaleTipo.setSiacDCausale(this);

		return siacRCausaleTipo;
	}

	public SiacRCausaleTipoFin removeSiacRCausaleTipo(SiacRCausaleTipoFin siacRCausaleTipo) {
		getSiacRCausaleTipos().remove(siacRCausaleTipo);
		siacRCausaleTipo.setSiacDCausale(null);

		return siacRCausaleTipo;
	}

	public List<SiacRDocOnereFin> getSiacRDocOneres() {
		return this.siacRDocOneres;
	}

	public void setSiacRDocOneres(List<SiacRDocOnereFin> siacRDocOneres) {
		this.siacRDocOneres = siacRDocOneres;
	}

	public SiacRDocOnereFin addSiacRDocOnere(SiacRDocOnereFin siacRDocOnere) {
		getSiacRDocOneres().add(siacRDocOnere);
		siacRDocOnere.setSiacDCausale(this);

		return siacRDocOnere;
	}

	public SiacRDocOnereFin removeSiacRDocOnere(SiacRDocOnereFin siacRDocOnere) {
		getSiacRDocOneres().remove(siacRDocOnere);
		siacRDocOnere.setSiacDCausale(null);

		return siacRDocOnere;
	}

	public List<SiacROnereCausaleFin> getSiacROnereCausales() {
		return this.siacROnereCausales;
	}

	public void setSiacROnereCausales(List<SiacROnereCausaleFin> siacROnereCausales) {
		this.siacROnereCausales = siacROnereCausales;
	}

	public SiacROnereCausaleFin addSiacROnereCausale(SiacROnereCausaleFin siacROnereCausale) {
		getSiacROnereCausales().add(siacROnereCausale);
		siacROnereCausale.setSiacDCausale(this);

		return siacROnereCausale;
	}

	public SiacROnereCausaleFin removeSiacROnereCausale(SiacROnereCausaleFin siacROnereCausale) {
		getSiacROnereCausales().remove(siacROnereCausale);
		siacROnereCausale.setSiacDCausale(null);

		return siacROnereCausale;
	}

	public List<SiacRPredocCausaleFin> getSiacRPredocCausales() {
		return this.siacRPredocCausales;
	}

	public void setSiacRPredocCausales(List<SiacRPredocCausaleFin> siacRPredocCausales) {
		this.siacRPredocCausales = siacRPredocCausales;
	}

	public SiacRPredocCausaleFin addSiacRPredocCausale(SiacRPredocCausaleFin siacRPredocCausale) {
		getSiacRPredocCausales().add(siacRPredocCausale);
		siacRPredocCausale.setSiacDCausale(this);

		return siacRPredocCausale;
	}

	public SiacRPredocCausaleFin removeSiacRPredocCausale(SiacRPredocCausaleFin siacRPredocCausale) {
		getSiacRPredocCausales().remove(siacRPredocCausale);
		siacRPredocCausale.setSiacDCausale(null);

		return siacRPredocCausale;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.causId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.causId = uid;
	}

}