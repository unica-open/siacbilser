/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacLoginMultiplo;


/**
 * The persistent class for the siac_t_predoc database table.
 * 
 */
@Entity
@Table(name="siac_t_predoc")
public class SiacTPredocFin extends SiacLoginMultiplo {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="predoc_id")
	private Integer predocId;

	@Column(name="predoc_codice_iuv")
	private String predocCodiceIuv;

	@Column(name="predoc_data")
	private Timestamp predocData;

	@Column(name="predoc_data_competenza")
	private Timestamp predocDataCompetenza;

	@Column(name="predoc_desc")
	private String predocDesc;

	@Column(name="predoc_importo")
	private BigDecimal predocImporto;

	@Column(name="predoc_note")
	private String predocNote;

	@Column(name="predoc_numero")
	private String predocNumero;

	@Column(name="predoc_periodo_competenza")
	private String predocPeriodoCompetenza;

	//bi-directional many-to-one association to SiacRPredocAttoAmmFin
	@OneToMany(mappedBy="siacTPredoc")
	private List<SiacRPredocAttoAmmFin> siacRPredocAttoAmms;

	//bi-directional many-to-one association to SiacRPredocBilElemFin
	@OneToMany(mappedBy="siacTPredoc")
	private List<SiacRPredocBilElemFin> siacRPredocBilElems;

	//bi-directional many-to-one association to SiacRPredocCausaleFin
	@OneToMany(mappedBy="siacTPredoc")
	private List<SiacRPredocCausaleFin> siacRPredocCausales;

	//bi-directional many-to-one association to SiacRPredocClassFin
	@OneToMany(mappedBy="siacTPredoc")
	private List<SiacRPredocClassFin> siacRPredocClasses;

//	//bi-directional many-to-one association to SiacRPredocModpag
//	@OneToMany(mappedBy="siacTPredoc")
//	private List<SiacRPredocModpag> siacRPredocModpags;

//	//bi-directional many-to-one association to SiacRPredocMovgestT
//	@OneToMany(mappedBy="siacTPredoc")
//	private List<SiacRPredocMovgestT> siacRPredocMovgestTs;

	//bi-directional many-to-one association to SiacRPredocProvCassaFin
	@OneToMany(mappedBy="siacTPredoc")
	private List<SiacRPredocProvCassaFin> siacRPredocProvCassas;

//	//bi-directional many-to-one association to SiacRPredocSog
//	@OneToMany(mappedBy="siacTPredoc")
//	private List<SiacRPredocSog> siacRPredocSogs;

//	//bi-directional many-to-one association to SiacRPredocStato
//	@OneToMany(mappedBy="siacTPredoc")
//	private List<SiacRPredocStato> siacRPredocStatos;

//	//bi-directional many-to-one association to SiacRPredocSubdocFin
//	@OneToMany(mappedBy="siacTPredoc")
//	private List<SiacRPredocSubdocFin> siacRPredocSubdocs;

	//bi-directional many-to-one association to SiacDContotesoreriaFin
	@ManyToOne
	@JoinColumn(name="contotes_id")
	private SiacDContotesoreriaFin siacDContotesoreria;

	//bi-directional many-to-one association to SiacDDocFamTipoFin
	@ManyToOne
	@JoinColumn(name="doc_fam_tipo_id")
	private SiacDDocFamTipoFin siacDDocFamTipo;

//	//bi-directional many-to-one association to SiacTPredocAnagr
//	@OneToMany(mappedBy="siacTPredoc")
//	private List<SiacTPredocAnagr> siacTPredocAnagrs;

	public SiacTPredocFin() {
	}

	public Integer getPredocId() {
		return this.predocId;
	}

	public void setPredocId(Integer predocId) {
		this.predocId = predocId;
	}

	public String getPredocCodiceIuv() {
		return this.predocCodiceIuv;
	}

	public void setPredocCodiceIuv(String predocCodiceIuv) {
		this.predocCodiceIuv = predocCodiceIuv;
	}

	public Timestamp getPredocData() {
		return this.predocData;
	}

	public void setPredocData(Timestamp predocData) {
		this.predocData = predocData;
	}

	public Timestamp getPredocDataCompetenza() {
		return this.predocDataCompetenza;
	}

	public void setPredocDataCompetenza(Timestamp predocDataCompetenza) {
		this.predocDataCompetenza = predocDataCompetenza;
	}

	public String getPredocDesc() {
		return this.predocDesc;
	}

	public void setPredocDesc(String predocDesc) {
		this.predocDesc = predocDesc;
	}

	public BigDecimal getPredocImporto() {
		return this.predocImporto;
	}

	public void setPredocImporto(BigDecimal predocImporto) {
		this.predocImporto = predocImporto;
	}

	public String getPredocNote() {
		return this.predocNote;
	}

	public void setPredocNote(String predocNote) {
		this.predocNote = predocNote;
	}

	public String getPredocNumero() {
		return this.predocNumero;
	}

	public void setPredocNumero(String predocNumero) {
		this.predocNumero = predocNumero;
	}

	public String getPredocPeriodoCompetenza() {
		return this.predocPeriodoCompetenza;
	}

	public void setPredocPeriodoCompetenza(String predocPeriodoCompetenza) {
		this.predocPeriodoCompetenza = predocPeriodoCompetenza;
	}

	public List<SiacRPredocAttoAmmFin> getSiacRPredocAttoAmms() {
		return this.siacRPredocAttoAmms;
	}

	public void setSiacRPredocAttoAmms(List<SiacRPredocAttoAmmFin> siacRPredocAttoAmms) {
		this.siacRPredocAttoAmms = siacRPredocAttoAmms;
	}

	public SiacRPredocAttoAmmFin addSiacRPredocAttoAmm(SiacRPredocAttoAmmFin siacRPredocAttoAmm) {
		getSiacRPredocAttoAmms().add(siacRPredocAttoAmm);
		siacRPredocAttoAmm.setSiacTPredoc(this);

		return siacRPredocAttoAmm;
	}

	public SiacRPredocAttoAmmFin removeSiacRPredocAttoAmm(SiacRPredocAttoAmmFin siacRPredocAttoAmm) {
		getSiacRPredocAttoAmms().remove(siacRPredocAttoAmm);
		siacRPredocAttoAmm.setSiacTPredoc(null);

		return siacRPredocAttoAmm;
	}

	public List<SiacRPredocBilElemFin> getSiacRPredocBilElems() {
		return this.siacRPredocBilElems;
	}

	public void setSiacRPredocBilElems(List<SiacRPredocBilElemFin> siacRPredocBilElems) {
		this.siacRPredocBilElems = siacRPredocBilElems;
	}

	public SiacRPredocBilElemFin addSiacRPredocBilElem(SiacRPredocBilElemFin siacRPredocBilElem) {
		getSiacRPredocBilElems().add(siacRPredocBilElem);
		siacRPredocBilElem.setSiacTPredoc(this);

		return siacRPredocBilElem;
	}

	public SiacRPredocBilElemFin removeSiacRPredocBilElem(SiacRPredocBilElemFin siacRPredocBilElem) {
		getSiacRPredocBilElems().remove(siacRPredocBilElem);
		siacRPredocBilElem.setSiacTPredoc(null);

		return siacRPredocBilElem;
	}

	public List<SiacRPredocCausaleFin> getSiacRPredocCausales() {
		return this.siacRPredocCausales;
	}

	public void setSiacRPredocCausales(List<SiacRPredocCausaleFin> siacRPredocCausales) {
		this.siacRPredocCausales = siacRPredocCausales;
	}

	public SiacRPredocCausaleFin addSiacRPredocCausale(SiacRPredocCausaleFin siacRPredocCausale) {
		getSiacRPredocCausales().add(siacRPredocCausale);
		siacRPredocCausale.setSiacTPredoc(this);

		return siacRPredocCausale;
	}

	public SiacRPredocCausaleFin removeSiacRPredocCausale(SiacRPredocCausaleFin siacRPredocCausale) {
		getSiacRPredocCausales().remove(siacRPredocCausale);
		siacRPredocCausale.setSiacTPredoc(null);

		return siacRPredocCausale;
	}

	public List<SiacRPredocClassFin> getSiacRPredocClasses() {
		return this.siacRPredocClasses;
	}

	public void setSiacRPredocClasses(List<SiacRPredocClassFin> siacRPredocClasses) {
		this.siacRPredocClasses = siacRPredocClasses;
	}

	public SiacRPredocClassFin addSiacRPredocClass(SiacRPredocClassFin siacRPredocClass) {
		getSiacRPredocClasses().add(siacRPredocClass);
		siacRPredocClass.setSiacTPredoc(this);

		return siacRPredocClass;
	}

	public SiacRPredocClassFin removeSiacRPredocClass(SiacRPredocClassFin siacRPredocClass) {
		getSiacRPredocClasses().remove(siacRPredocClass);
		siacRPredocClass.setSiacTPredoc(null);

		return siacRPredocClass;
	}

//	public List<SiacRPredocModpag> getSiacRPredocModpags() {
//		return this.siacRPredocModpags;
//	}

//	public void setSiacRPredocModpags(List<SiacRPredocModpag> siacRPredocModpags) {
//		this.siacRPredocModpags = siacRPredocModpags;
//	}

//	public SiacRPredocModpag addSiacRPredocModpag(SiacRPredocModpag siacRPredocModpag) {
//		getSiacRPredocModpags().add(siacRPredocModpag);
//		siacRPredocModpag.setSiacTPredoc(this);
//
//		return siacRPredocModpag;
//	}

//	public SiacRPredocModpag removeSiacRPredocModpag(SiacRPredocModpag siacRPredocModpag) {
//		getSiacRPredocModpags().remove(siacRPredocModpag);
//		siacRPredocModpag.setSiacTPredoc(null);
//
//		return siacRPredocModpag;
//	}

//	public List<SiacRPredocMovgestT> getSiacRPredocMovgestTs() {
//		return this.siacRPredocMovgestTs;
//	}

//	public void setSiacRPredocMovgestTs(List<SiacRPredocMovgestT> siacRPredocMovgestTs) {
//		this.siacRPredocMovgestTs = siacRPredocMovgestTs;
//	}

//	public SiacRPredocMovgestT addSiacRPredocMovgestT(SiacRPredocMovgestT siacRPredocMovgestT) {
//		getSiacRPredocMovgestTs().add(siacRPredocMovgestT);
//		siacRPredocMovgestT.setSiacTPredoc(this);
//
//		return siacRPredocMovgestT;
//	}

//	public SiacRPredocMovgestT removeSiacRPredocMovgestT(SiacRPredocMovgestT siacRPredocMovgestT) {
//		getSiacRPredocMovgestTs().remove(siacRPredocMovgestT);
//		siacRPredocMovgestT.setSiacTPredoc(null);
//
//		return siacRPredocMovgestT;
//	}

	public List<SiacRPredocProvCassaFin> getSiacRPredocProvCassas() {
		return this.siacRPredocProvCassas;
	}

	public void setSiacRPredocProvCassas(List<SiacRPredocProvCassaFin> siacRPredocProvCassas) {
		this.siacRPredocProvCassas = siacRPredocProvCassas;
	}

	public SiacRPredocProvCassaFin addSiacRPredocProvCassa(SiacRPredocProvCassaFin siacRPredocProvCassa) {
		getSiacRPredocProvCassas().add(siacRPredocProvCassa);
		siacRPredocProvCassa.setSiacTPredoc(this);

		return siacRPredocProvCassa;
	}

	public SiacRPredocProvCassaFin removeSiacRPredocProvCassa(SiacRPredocProvCassaFin siacRPredocProvCassa) {
		getSiacRPredocProvCassas().remove(siacRPredocProvCassa);
		siacRPredocProvCassa.setSiacTPredoc(null);

		return siacRPredocProvCassa;
	}

//	public List<SiacRPredocSog> getSiacRPredocSogs() {
//		return this.siacRPredocSogs;
//	}

//	public void setSiacRPredocSogs(List<SiacRPredocSog> siacRPredocSogs) {
//		this.siacRPredocSogs = siacRPredocSogs;
//	}

//	public SiacRPredocSog addSiacRPredocSog(SiacRPredocSog siacRPredocSog) {
//		getSiacRPredocSogs().add(siacRPredocSog);
//		siacRPredocSog.setSiacTPredoc(this);
//
//		return siacRPredocSog;
//	}

//	public SiacRPredocSog removeSiacRPredocSog(SiacRPredocSog siacRPredocSog) {
//		getSiacRPredocSogs().remove(siacRPredocSog);
//		siacRPredocSog.setSiacTPredoc(null);
//
//		return siacRPredocSog;
//	}

//	public List<SiacRPredocStato> getSiacRPredocStatos() {
//		return this.siacRPredocStatos;
//	}

//	public void setSiacRPredocStatos(List<SiacRPredocStato> siacRPredocStatos) {
//		this.siacRPredocStatos = siacRPredocStatos;
//	}

//	public SiacRPredocStato addSiacRPredocStato(SiacRPredocStato siacRPredocStato) {
//		getSiacRPredocStatos().add(siacRPredocStato);
//		siacRPredocStato.setSiacTPredoc(this);
//
//		return siacRPredocStato;
//	}

//	public SiacRPredocStato removeSiacRPredocStato(SiacRPredocStato siacRPredocStato) {
//		getSiacRPredocStatos().remove(siacRPredocStato);
//		siacRPredocStato.setSiacTPredoc(null);
//
//		return siacRPredocStato;
//	}

//	public List<SiacRPredocSubdocFin> getSiacRPredocSubdocs() {
//		return this.siacRPredocSubdocs;
//	}

//	public void setSiacRPredocSubdocs(List<SiacRPredocSubdocFin> siacRPredocSubdocs) {
//		this.siacRPredocSubdocs = siacRPredocSubdocs;
//	}

//	public SiacRPredocSubdocFin addSiacRPredocSubdoc(SiacRPredocSubdocFin siacRPredocSubdoc) {
//		getSiacRPredocSubdocs().add(siacRPredocSubdoc);
//		siacRPredocSubdoc.setSiacTPredoc(this);
//
//		return siacRPredocSubdoc;
//	}

//	public SiacRPredocSubdocFin removeSiacRPredocSubdoc(SiacRPredocSubdocFin siacRPredocSubdoc) {
//		getSiacRPredocSubdocs().remove(siacRPredocSubdoc);
//		siacRPredocSubdoc.setSiacTPredoc(null);
//
//		return siacRPredocSubdoc;
//	}

	public SiacDContotesoreriaFin getSiacDContotesoreria() {
		return this.siacDContotesoreria;
	}

	public void setSiacDContotesoreria(SiacDContotesoreriaFin siacDContotesoreria) {
		this.siacDContotesoreria = siacDContotesoreria;
	}

	public SiacDDocFamTipoFin getSiacDDocFamTipo() {
		return this.siacDDocFamTipo;
	}

	public void setSiacDDocFamTipo(SiacDDocFamTipoFin siacDDocFamTipo) {
		this.siacDDocFamTipo = siacDDocFamTipo;
	}

//	public List<SiacTPredocAnagr> getSiacTPredocAnagrs() {
//		return this.siacTPredocAnagrs;
//	}

//	public void setSiacTPredocAnagrs(List<SiacTPredocAnagr> siacTPredocAnagrs) {
//		this.siacTPredocAnagrs = siacTPredocAnagrs;
//	}

//	public SiacTPredocAnagr addSiacTPredocAnagr(SiacTPredocAnagr siacTPredocAnagr) {
//		getSiacTPredocAnagrs().add(siacTPredocAnagr);
//		siacTPredocAnagr.setSiacTPredoc(this);
//
//		return siacTPredocAnagr;
//	}

//	public SiacTPredocAnagr removeSiacTPredocAnagr(SiacTPredocAnagr siacTPredocAnagr) {
//		getSiacTPredocAnagrs().remove(siacTPredocAnagr);
//		siacTPredocAnagr.setSiacTPredoc(null);
//
//		return siacTPredocAnagr;
//	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.predocId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.predocId = uid;
	}
}