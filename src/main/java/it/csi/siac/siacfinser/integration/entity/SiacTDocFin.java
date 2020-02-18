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
 * The persistent class for the siac_t_doc database table.
 * 
 */
@Entity
@Table(name="siac_t_doc")
public class SiacTDocFin extends SiacLoginMultiplo {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="doc_id")
	private Integer docId;

	@Column(name="doc_anno")
	private Integer docAnno;

	@Column(name="doc_beneficiariomult")
	private Boolean docBeneficiariomult;

	@Column(name="doc_data_emissione")
	private Timestamp docDataEmissione;

	@Column(name="doc_data_scadenza")
	private Timestamp docDataScadenza;

	@Column(name="doc_desc")
	private String docDesc;

	@Column(name="doc_importo")
	private BigDecimal docImporto;

	@Column(name="doc_numero")
	private String docNumero;

//	//bi-directional many-to-one association to SiacRDoc
//	@OneToMany(mappedBy="siacTDoc1")
//	private List<SiacRDoc> siacRDocs1;

//	//bi-directional many-to-one association to SiacRDoc
//	@OneToMany(mappedBy="siacTDoc2")
//	private List<SiacRDoc> siacRDocs2;

//	//bi-directional many-to-one association to SiacRDocAttr
//	@OneToMany(mappedBy="siacTDoc")
//	private List<SiacRDocAttr> siacRDocAttrs;

//	//bi-directional many-to-one association to SiacRDocClass
//	@OneToMany(mappedBy="siacTDoc")
//	private List<SiacRDocClass> siacRDocClasses;

	//bi-directional many-to-one association to SiacRDocOnereFin
	@OneToMany(mappedBy="siacTDoc")
	private List<SiacRDocOnereFin> siacRDocOneres;

//	//bi-directional many-to-one association to SiacRDocSogFin
//	@OneToMany(mappedBy="siacTDoc")
//	private List<SiacRDocSogFin> siacRDocSogs;

	//bi-directional many-to-one association to SiacRDocStatoFin
	@OneToMany(mappedBy="siacTDoc")
	private List<SiacRDocStatoFin> siacRDocStatos;

	//bi-directional many-to-one association to SiacDCodicebolloFin
	@ManyToOne
	@JoinColumn(name="codbollo_id")
	private SiacDCodicebolloFin siacDCodicebollo;

	//bi-directional many-to-one association to SiacDDocTipoFin
	@ManyToOne
	@JoinColumn(name="doc_tipo_id")
	private SiacDDocTipoFin siacDDocTipo;

//	//bi-directional many-to-one association to SiacTSubdocFin
//	@OneToMany(mappedBy="siacTDoc")
//	private List<SiacTSubdocFin> siacTSubdocs;

//	//bi-directional many-to-one association to SiacTSubdocNum
//	@OneToMany(mappedBy="siacTDoc")
//	private List<SiacTSubdocNum> siacTSubdocNums;

	public SiacTDocFin() {
	}

	public Integer getDocId() {
		return this.docId;
	}

	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	public Integer getDocAnno() {
		return this.docAnno;
	}

	public void setDocAnno(Integer docAnno) {
		this.docAnno = docAnno;
	}

	public Boolean getDocBeneficiariomult() {
		return this.docBeneficiariomult;
	}

	public void setDocBeneficiariomult(Boolean docBeneficiariomult) {
		this.docBeneficiariomult = docBeneficiariomult;
	}

	public Timestamp getDocDataEmissione() {
		return this.docDataEmissione;
	}

	public void setDocDataEmissione(Timestamp docDataEmissione) {
		this.docDataEmissione = docDataEmissione;
	}

	public Timestamp getDocDataScadenza() {
		return this.docDataScadenza;
	}

	public void setDocDataScadenza(Timestamp docDataScadenza) {
		this.docDataScadenza = docDataScadenza;
	}

	public String getDocDesc() {
		return this.docDesc;
	}

	public void setDocDesc(String docDesc) {
		this.docDesc = docDesc;
	}

	public BigDecimal getDocImporto() {
		return this.docImporto;
	}

	public void setDocImporto(BigDecimal docImporto) {
		this.docImporto = docImporto;
	}

	public String getDocNumero() {
		return this.docNumero;
	}

	public void setDocNumero(String docNumero) {
		this.docNumero = docNumero;
	}

//	public List<SiacRDoc> getSiacRDocs1() {
//		return this.siacRDocs1;
//	}

//	public void setSiacRDocs1(List<SiacRDoc> siacRDocs1) {
//		this.siacRDocs1 = siacRDocs1;
//	}

//	public SiacRDoc addSiacRDocs1(SiacRDoc siacRDocs1) {
//		getSiacRDocs1().add(siacRDocs1);
//		siacRDocs1.setSiacTDoc1(this);
//
//		return siacRDocs1;
//	}

//	public SiacRDoc removeSiacRDocs1(SiacRDoc siacRDocs1) {
//		getSiacRDocs1().remove(siacRDocs1);
//		siacRDocs1.setSiacTDoc1(null);
//
//		return siacRDocs1;
//	}

//	public List<SiacRDoc> getSiacRDocs2() {
//		return this.siacRDocs2;
//	}

//	public void setSiacRDocs2(List<SiacRDoc> siacRDocs2) {
//		this.siacRDocs2 = siacRDocs2;
//	}

//	public SiacRDoc addSiacRDocs2(SiacRDoc siacRDocs2) {
//		getSiacRDocs2().add(siacRDocs2);
//		siacRDocs2.setSiacTDoc2(this);
//
//		return siacRDocs2;
//	}

//	public SiacRDoc removeSiacRDocs2(SiacRDoc siacRDocs2) {
//		getSiacRDocs2().remove(siacRDocs2);
//		siacRDocs2.setSiacTDoc2(null);
//
//		return siacRDocs2;
//	}

//	public List<SiacRDocAttr> getSiacRDocAttrs() {
//		return this.siacRDocAttrs;
//	}

//	public void setSiacRDocAttrs(List<SiacRDocAttr> siacRDocAttrs) {
//		this.siacRDocAttrs = siacRDocAttrs;
//	}

//	public SiacRDocAttr addSiacRDocAttr(SiacRDocAttr siacRDocAttr) {
//		getSiacRDocAttrs().add(siacRDocAttr);
//		siacRDocAttr.setSiacTDoc(this);
//
//		return siacRDocAttr;
//	}

//	public SiacRDocAttr removeSiacRDocAttr(SiacRDocAttr siacRDocAttr) {
//		getSiacRDocAttrs().remove(siacRDocAttr);
//		siacRDocAttr.setSiacTDoc(null);
//
//		return siacRDocAttr;
//	}

//	public List<SiacRDocClass> getSiacRDocClasses() {
//		return this.siacRDocClasses;
//	}

//	public void setSiacRDocClasses(List<SiacRDocClass> siacRDocClasses) {
//		this.siacRDocClasses = siacRDocClasses;
//	}

//	public SiacRDocClass addSiacRDocClass(SiacRDocClass siacRDocClass) {
//		getSiacRDocClasses().add(siacRDocClass);
//		siacRDocClass.setSiacTDoc(this);
//
//		return siacRDocClass;
//	}

//	public SiacRDocClass removeSiacRDocClass(SiacRDocClass siacRDocClass) {
//		getSiacRDocClasses().remove(siacRDocClass);
//		siacRDocClass.setSiacTDoc(null);
//
//		return siacRDocClass;
//	}

	public List<SiacRDocOnereFin> getSiacRDocOneres() {
		return this.siacRDocOneres;
	}

	public void setSiacRDocOneres(List<SiacRDocOnereFin> siacRDocOneres) {
		this.siacRDocOneres = siacRDocOneres;
	}

	public SiacRDocOnereFin addSiacRDocOnere(SiacRDocOnereFin siacRDocOnere) {
		getSiacRDocOneres().add(siacRDocOnere);
		siacRDocOnere.setSiacTDoc(this);

		return siacRDocOnere;
	}

	public SiacRDocOnereFin removeSiacRDocOnere(SiacRDocOnereFin siacRDocOnere) {
		getSiacRDocOneres().remove(siacRDocOnere);
		siacRDocOnere.setSiacTDoc(null);

		return siacRDocOnere;
	}

//	public List<SiacRDocSogFin> getSiacRDocSogs() {
//		return this.siacRDocSogs;
//	}

//	public void setSiacRDocSogs(List<SiacRDocSogFin> siacRDocSogs) {
//		this.siacRDocSogs = siacRDocSogs;
//	}

//	public SiacRDocSogFin addSiacRDocSog(SiacRDocSogFin siacRDocSog) {
//		getSiacRDocSogs().add(siacRDocSog);
//		siacRDocSog.setSiacTDoc(this);
//
//		return siacRDocSog;
//	}

//	public SiacRDocSogFin removeSiacRDocSog(SiacRDocSogFin siacRDocSog) {
//		getSiacRDocSogs().remove(siacRDocSog);
//		siacRDocSog.setSiacTDoc(null);
//
//		return siacRDocSog;
//	}

	public List<SiacRDocStatoFin> getSiacRDocStatos() {
		return this.siacRDocStatos;
	}

	public void setSiacRDocStatos(List<SiacRDocStatoFin> siacRDocStatos) {
		this.siacRDocStatos = siacRDocStatos;
	}

//	public SiacRDocStatoFin addSiacRDocStato(SiacRDocStatoFin siacRDocStato) {
//		getSiacRDocStatos().add(siacRDocStato);
//		siacRDocStato.setSiacTDoc(this);
//
//		return siacRDocStato;
//	}

//	public SiacRDocStatoFin removeSiacRDocStato(SiacRDocStatoFin siacRDocStato) {
//		getSiacRDocStatos().remove(siacRDocStato);
//		siacRDocStato.setSiacTDoc(null);
//
//		return siacRDocStato;
//	}

	public SiacDCodicebolloFin getSiacDCodicebollo() {
		return this.siacDCodicebollo;
	}

	public void setSiacDCodicebollo(SiacDCodicebolloFin siacDCodicebollo) {
		this.siacDCodicebollo = siacDCodicebollo;
	}

	public SiacDDocTipoFin getSiacDDocTipo() {
		return this.siacDDocTipo;
	}

	public void setSiacDDocTipo(SiacDDocTipoFin siacDDocTipo) {
		this.siacDDocTipo = siacDDocTipo;
	}

//	public List<SiacTSubdocFin> getSiacTSubdocs() {
//		return this.siacTSubdocs;
//	}

//	public void setSiacTSubdocs(List<SiacTSubdocFin> siacTSubdocs) {
//		this.siacTSubdocs = siacTSubdocs;
//	}

//	public SiacTSubdocFin addSiacTSubdoc(SiacTSubdocFin siacTSubdoc) {
//		getSiacTSubdocs().add(siacTSubdoc);
//		siacTSubdoc.setSiacTDoc(this);
//
//		return siacTSubdoc;
//	}

//	public SiacTSubdocFin removeSiacTSubdoc(SiacTSubdocFin siacTSubdoc) {
//		getSiacTSubdocs().remove(siacTSubdoc);
//		siacTSubdoc.setSiacTDoc(null);
//
//		return siacTSubdoc;
//	}

//	public List<SiacTSubdocNum> getSiacTSubdocNums() {
//		return this.siacTSubdocNums;
//	}

//	public void setSiacTSubdocNums(List<SiacTSubdocNum> siacTSubdocNums) {
//		this.siacTSubdocNums = siacTSubdocNums;
//	}

//	public SiacTSubdocNum addSiacTSubdocNum(SiacTSubdocNum siacTSubdocNum) {
//		getSiacTSubdocNums().add(siacTSubdocNum);
//		siacTSubdocNum.setSiacTDoc(this);
//
//		return siacTSubdocNum;
//	}

//	public SiacTSubdocNum removeSiacTSubdocNum(SiacTSubdocNum siacTSubdocNum) {
//		getSiacTSubdocNums().remove(siacTSubdocNum);
//		siacTSubdocNum.setSiacTDoc(null);
//
//		return siacTSubdocNum;
//	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.docId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.docId = uid;
	}

}