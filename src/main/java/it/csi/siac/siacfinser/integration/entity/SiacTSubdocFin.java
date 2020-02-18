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
 * The persistent class for the siac_t_subdoc database table.
 * 
 */
@Entity
@Table(name="siac_t_subdoc")
public class SiacTSubdocFin extends SiacLoginMultiplo {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="subdoc_id")
	private Integer subdocId;

	@Column(name="subdoc_convalida_manuale")
	private Boolean subdocConvalidaManuale;

	@Column(name="subdoc_data_scadenza")
	private Timestamp subdocDataScadenza;

	@Column(name="subdoc_desc")
	private String subdocDesc;

	@Column(name="subdoc_importo")
	private BigDecimal subdocImporto;

	@Column(name="subdoc_importo_da_dedurre")
	private BigDecimal subdocImportoDaDedurre;

	@Column(name="subdoc_nreg_iva")
	private String subdocNregIva;

	@Column(name="subdoc_numero")
	private Integer subdocNumero;

	//bi-directional many-to-one association to SiacRCartacontDetSubdocFin
	@OneToMany(mappedBy="siacTSubdoc")
	private List<SiacRCartacontDetSubdocFin> siacRCartacontDetSubdocs;

	//bi-directional many-to-one association to SiacRElencoDocSubdocFin
	@OneToMany(mappedBy="siacTSubdoc")
	private List<SiacRElencoDocSubdocFin> siacRElencoDocSubdocs;

	//bi-directional many-to-one association to SiacRPredocSubdocFin
	@OneToMany(mappedBy="siacTSubdoc")
	private List<SiacRPredocSubdocFin> siacRPredocSubdocs;

	//bi-directional many-to-one association to SiacRSubdocFin
	@OneToMany(mappedBy="siacTSubdoc1")
	private List<SiacRSubdocFin> siacRSubdocs1;

	//bi-directional many-to-one association to SiacRSubdocFin
	@OneToMany(mappedBy="siacTSubdoc2")
	private List<SiacRSubdocFin> siacRSubdocs2;

	//bi-directional many-to-one association to SiacRSubdocAttoAmmFin
	@OneToMany(mappedBy="siacTSubdoc")
	private List<SiacRSubdocAttoAmmFin> siacRSubdocAttoAmms;

	//bi-directional many-to-one association to SiacRSubdocAttrFin
	@OneToMany(mappedBy="siacTSubdoc")
	private List<SiacRSubdocAttrFin> siacRSubdocAttrs;

	//bi-directional many-to-one association to SiacRSubdocClassFin
	@OneToMany(mappedBy="siacTSubdoc")
	private List<SiacRSubdocClassFin> siacRSubdocClasses;

	//bi-directional many-to-one association to SiacRSubdocLiquidazioneFin
	@OneToMany(mappedBy="siacTSubdoc")
	private List<SiacRSubdocLiquidazioneFin> siacRSubdocLiquidaziones;

	//bi-directional many-to-one association to SiacRSubdocModpagFin
	@OneToMany(mappedBy="siacTSubdoc")
	private List<SiacRSubdocModpagFin> siacRSubdocModpags;

	//bi-directional many-to-one association to SiacRSubdocMovgestTFin
	@OneToMany(mappedBy="siacTSubdoc")
	private List<SiacRSubdocMovgestTFin> siacRSubdocMovgestTs;

	//bi-directional many-to-one association to SiacRSubdocProvCassaFin
	@OneToMany(mappedBy="siacTSubdoc")
	private List<SiacRSubdocProvCassaFin> siacRSubdocProvCassas;

	//bi-directional many-to-one association to SiacRSubdocSogFin
	@OneToMany(mappedBy="siacTSubdoc")
	private List<SiacRSubdocSogFin> siacRSubdocSogs;

	//bi-directional many-to-one association to SiacRSubdocSubdocIvaFin
	@OneToMany(mappedBy="siacTSubdoc")
	private List<SiacRSubdocSubdocIvaFin> siacRSubdocSubdocIvas;

	//bi-directional many-to-one association to SiacDCommissioneTipoFin
	@ManyToOne
	@JoinColumn(name="comm_tipo_id")
	private SiacDCommissioneTipoFin siacDCommissioneTipo;

	//bi-directional many-to-one association to SiacDContotesoreriaFin
	@ManyToOne
	@JoinColumn(name="contotes_id")
	private SiacDContotesoreriaFin siacDContotesoreria;

	//bi-directional many-to-one association to SiacDDistintaFin
	@ManyToOne
	@JoinColumn(name="dist_id")
	private SiacDDistintaFin siacDDistinta;

	//bi-directional many-to-one association to SiacDNoteTesoriereFin
	@ManyToOne
	@JoinColumn(name="notetes_id")
	private SiacDNoteTesoriereFin siacDNoteTesoriere;

	//bi-directional many-to-one association to SiacDSubdocTipoFin
	@ManyToOne
	@JoinColumn(name="subdoc_tipo_id")
	private SiacDSubdocTipoFin siacDSubdocTipo;

	//bi-directional many-to-one association to SiacTDocFin
	@ManyToOne
	@JoinColumn(name="doc_id")
	private SiacTDocFin siacTDoc;

	public SiacTSubdocFin() {
	}

	public Integer getSubdocId() {
		return this.subdocId;
	}

	public void setSubdocId(Integer subdocId) {
		this.subdocId = subdocId;
	}

	public Boolean getSubdocConvalidaManuale() {
		return this.subdocConvalidaManuale;
	}

	public void setSubdocConvalidaManuale(Boolean subdocConvalidaManuale) {
		this.subdocConvalidaManuale = subdocConvalidaManuale;
	}

	public Timestamp getSubdocDataScadenza() {
		return this.subdocDataScadenza;
	}

	public void setSubdocDataScadenza(Timestamp subdocDataScadenza) {
		this.subdocDataScadenza = subdocDataScadenza;
	}

	public String getSubdocDesc() {
		return this.subdocDesc;
	}

	public void setSubdocDesc(String subdocDesc) {
		this.subdocDesc = subdocDesc;
	}

	public BigDecimal getSubdocImporto() {
		return this.subdocImporto;
	}

	public void setSubdocImporto(BigDecimal subdocImporto) {
		this.subdocImporto = subdocImporto;
	}

	public BigDecimal getSubdocImportoDaDedurre() {
		return this.subdocImportoDaDedurre;
	}

	public void setSubdocImportoDaDedurre(BigDecimal subdocImportoDaDedurre) {
		this.subdocImportoDaDedurre = subdocImportoDaDedurre;
	}

	public String getSubdocNregIva() {
		return this.subdocNregIva;
	}

	public void setSubdocNregIva(String subdocNregIva) {
		this.subdocNregIva = subdocNregIva;
	}

	public Integer getSubdocNumero() {
		return this.subdocNumero;
	}

	public void setSubdocNumero(Integer subdocNumero) {
		this.subdocNumero = subdocNumero;
	}

	public List<SiacRCartacontDetSubdocFin> getSiacRCartacontDetSubdocs() {
		return this.siacRCartacontDetSubdocs;
	}

	public void setSiacRCartacontDetSubdocs(List<SiacRCartacontDetSubdocFin> siacRCartacontDetSubdocs) {
		this.siacRCartacontDetSubdocs = siacRCartacontDetSubdocs;
	}

	public SiacRCartacontDetSubdocFin addSiacRCartacontDetSubdoc(SiacRCartacontDetSubdocFin siacRCartacontDetSubdoc) {
		getSiacRCartacontDetSubdocs().add(siacRCartacontDetSubdoc);
		siacRCartacontDetSubdoc.setSiacTSubdoc(this);

		return siacRCartacontDetSubdoc;
	}

	public SiacRCartacontDetSubdocFin removeSiacRCartacontDetSubdoc(SiacRCartacontDetSubdocFin siacRCartacontDetSubdoc) {
		getSiacRCartacontDetSubdocs().remove(siacRCartacontDetSubdoc);
		siacRCartacontDetSubdoc.setSiacTSubdoc(null);

		return siacRCartacontDetSubdoc;
	}

	public List<SiacRElencoDocSubdocFin> getSiacRElencoDocSubdocs() {
		return this.siacRElencoDocSubdocs;
	}

	public void setSiacRElencoDocSubdocs(List<SiacRElencoDocSubdocFin> siacRElencoDocSubdocs) {
		this.siacRElencoDocSubdocs = siacRElencoDocSubdocs;
	}

	public SiacRElencoDocSubdocFin addSiacRElencoDocSubdoc(SiacRElencoDocSubdocFin siacRElencoDocSubdoc) {
		getSiacRElencoDocSubdocs().add(siacRElencoDocSubdoc);
		siacRElencoDocSubdoc.setSiacTSubdoc(this);

		return siacRElencoDocSubdoc;
	}

	public SiacRElencoDocSubdocFin removeSiacRElencoDocSubdoc(SiacRElencoDocSubdocFin siacRElencoDocSubdoc) {
		getSiacRElencoDocSubdocs().remove(siacRElencoDocSubdoc);
		siacRElencoDocSubdoc.setSiacTSubdoc(null);

		return siacRElencoDocSubdoc;
	}

	public List<SiacRPredocSubdocFin> getSiacRPredocSubdocs() {
		return this.siacRPredocSubdocs;
	}

	public void setSiacRPredocSubdocs(List<SiacRPredocSubdocFin> siacRPredocSubdocs) {
		this.siacRPredocSubdocs = siacRPredocSubdocs;
	}

	public SiacRPredocSubdocFin addSiacRPredocSubdoc(SiacRPredocSubdocFin siacRPredocSubdoc) {
		getSiacRPredocSubdocs().add(siacRPredocSubdoc);
		siacRPredocSubdoc.setSiacTSubdoc(this);

		return siacRPredocSubdoc;
	}

	public SiacRPredocSubdocFin removeSiacRPredocSubdoc(SiacRPredocSubdocFin siacRPredocSubdoc) {
		getSiacRPredocSubdocs().remove(siacRPredocSubdoc);
		siacRPredocSubdoc.setSiacTSubdoc(null);

		return siacRPredocSubdoc;
	}

	public List<SiacRSubdocFin> getSiacRSubdocs1() {
		return this.siacRSubdocs1;
	}

	public void setSiacRSubdocs1(List<SiacRSubdocFin> siacRSubdocs1) {
		this.siacRSubdocs1 = siacRSubdocs1;
	}

	public SiacRSubdocFin addSiacRSubdocs1(SiacRSubdocFin siacRSubdocs1) {
		getSiacRSubdocs1().add(siacRSubdocs1);
		siacRSubdocs1.setSiacTSubdoc1(this);

		return siacRSubdocs1;
	}

	public SiacRSubdocFin removeSiacRSubdocs1(SiacRSubdocFin siacRSubdocs1) {
		getSiacRSubdocs1().remove(siacRSubdocs1);
		siacRSubdocs1.setSiacTSubdoc1(null);

		return siacRSubdocs1;
	}

	public List<SiacRSubdocFin> getSiacRSubdocs2() {
		return this.siacRSubdocs2;
	}

	public void setSiacRSubdocs2(List<SiacRSubdocFin> siacRSubdocs2) {
		this.siacRSubdocs2 = siacRSubdocs2;
	}

	public SiacRSubdocFin addSiacRSubdocs2(SiacRSubdocFin siacRSubdocs2) {
		getSiacRSubdocs2().add(siacRSubdocs2);
		siacRSubdocs2.setSiacTSubdoc2(this);

		return siacRSubdocs2;
	}

	public SiacRSubdocFin removeSiacRSubdocs2(SiacRSubdocFin siacRSubdocs2) {
		getSiacRSubdocs2().remove(siacRSubdocs2);
		siacRSubdocs2.setSiacTSubdoc2(null);

		return siacRSubdocs2;
	}

	public List<SiacRSubdocAttoAmmFin> getSiacRSubdocAttoAmms() {
		return this.siacRSubdocAttoAmms;
	}

	public void setSiacRSubdocAttoAmms(List<SiacRSubdocAttoAmmFin> siacRSubdocAttoAmms) {
		this.siacRSubdocAttoAmms = siacRSubdocAttoAmms;
	}

	public SiacRSubdocAttoAmmFin addSiacRSubdocAttoAmm(SiacRSubdocAttoAmmFin siacRSubdocAttoAmm) {
		getSiacRSubdocAttoAmms().add(siacRSubdocAttoAmm);
		siacRSubdocAttoAmm.setSiacTSubdoc(this);

		return siacRSubdocAttoAmm;
	}

	public SiacRSubdocAttoAmmFin removeSiacRSubdocAttoAmm(SiacRSubdocAttoAmmFin siacRSubdocAttoAmm) {
		getSiacRSubdocAttoAmms().remove(siacRSubdocAttoAmm);
		siacRSubdocAttoAmm.setSiacTSubdoc(null);

		return siacRSubdocAttoAmm;
	}

	public List<SiacRSubdocAttrFin> getSiacRSubdocAttrs() {
		return this.siacRSubdocAttrs;
	}

	public void setSiacRSubdocAttrs(List<SiacRSubdocAttrFin> siacRSubdocAttrs) {
		this.siacRSubdocAttrs = siacRSubdocAttrs;
	}

	public SiacRSubdocAttrFin addSiacRSubdocAttr(SiacRSubdocAttrFin siacRSubdocAttr) {
		getSiacRSubdocAttrs().add(siacRSubdocAttr);
		siacRSubdocAttr.setSiacTSubdoc(this);

		return siacRSubdocAttr;
	}

	public SiacRSubdocAttrFin removeSiacRSubdocAttr(SiacRSubdocAttrFin siacRSubdocAttr) {
		getSiacRSubdocAttrs().remove(siacRSubdocAttr);
		siacRSubdocAttr.setSiacTSubdoc(null);

		return siacRSubdocAttr;
	}

	public List<SiacRSubdocClassFin> getSiacRSubdocClasses() {
		return this.siacRSubdocClasses;
	}

	public void setSiacRSubdocClasses(List<SiacRSubdocClassFin> siacRSubdocClasses) {
		this.siacRSubdocClasses = siacRSubdocClasses;
	}

	public SiacRSubdocClassFin addSiacRSubdocClass(SiacRSubdocClassFin siacRSubdocClass) {
		getSiacRSubdocClasses().add(siacRSubdocClass);
		siacRSubdocClass.setSiacTSubdoc(this);

		return siacRSubdocClass;
	}

	public SiacRSubdocClassFin removeSiacRSubdocClass(SiacRSubdocClassFin siacRSubdocClass) {
		getSiacRSubdocClasses().remove(siacRSubdocClass);
		siacRSubdocClass.setSiacTSubdoc(null);

		return siacRSubdocClass;
	}

	public List<SiacRSubdocLiquidazioneFin> getSiacRSubdocLiquidaziones() {
		return this.siacRSubdocLiquidaziones;
	}

	public void setSiacRSubdocLiquidaziones(List<SiacRSubdocLiquidazioneFin> siacRSubdocLiquidaziones) {
		this.siacRSubdocLiquidaziones = siacRSubdocLiquidaziones;
	}

	public SiacRSubdocLiquidazioneFin addSiacRSubdocLiquidazione(SiacRSubdocLiquidazioneFin siacRSubdocLiquidazione) {
		getSiacRSubdocLiquidaziones().add(siacRSubdocLiquidazione);
		siacRSubdocLiquidazione.setSiacTSubdoc(this);

		return siacRSubdocLiquidazione;
	}

	public SiacRSubdocLiquidazioneFin removeSiacRSubdocLiquidazione(SiacRSubdocLiquidazioneFin siacRSubdocLiquidazione) {
		getSiacRSubdocLiquidaziones().remove(siacRSubdocLiquidazione);
		siacRSubdocLiquidazione.setSiacTSubdoc(null);

		return siacRSubdocLiquidazione;
	}

	public List<SiacRSubdocModpagFin> getSiacRSubdocModpags() {
		return this.siacRSubdocModpags;
	}

	public void setSiacRSubdocModpags(List<SiacRSubdocModpagFin> siacRSubdocModpags) {
		this.siacRSubdocModpags = siacRSubdocModpags;
	}

	public SiacRSubdocModpagFin addSiacRSubdocModpag(SiacRSubdocModpagFin siacRSubdocModpag) {
		getSiacRSubdocModpags().add(siacRSubdocModpag);
		siacRSubdocModpag.setSiacTSubdoc(this);

		return siacRSubdocModpag;
	}

	public SiacRSubdocModpagFin removeSiacRSubdocModpag(SiacRSubdocModpagFin siacRSubdocModpag) {
		getSiacRSubdocModpags().remove(siacRSubdocModpag);
		siacRSubdocModpag.setSiacTSubdoc(null);

		return siacRSubdocModpag;
	}

	public List<SiacRSubdocMovgestTFin> getSiacRSubdocMovgestTs() {
		return this.siacRSubdocMovgestTs;
	}

	public void setSiacRSubdocMovgestTs(List<SiacRSubdocMovgestTFin> siacRSubdocMovgestTs) {
		this.siacRSubdocMovgestTs = siacRSubdocMovgestTs;
	}

	public SiacRSubdocMovgestTFin addSiacRSubdocMovgestT(SiacRSubdocMovgestTFin siacRSubdocMovgestT) {
		getSiacRSubdocMovgestTs().add(siacRSubdocMovgestT);
		siacRSubdocMovgestT.setSiacTSubdoc(this);

		return siacRSubdocMovgestT;
	}

	public SiacRSubdocMovgestTFin removeSiacRSubdocMovgestT(SiacRSubdocMovgestTFin siacRSubdocMovgestT) {
		getSiacRSubdocMovgestTs().remove(siacRSubdocMovgestT);
		siacRSubdocMovgestT.setSiacTSubdoc(null);

		return siacRSubdocMovgestT;
	}

	public List<SiacRSubdocProvCassaFin> getSiacRSubdocProvCassas() {
		return this.siacRSubdocProvCassas;
	}

	public void setSiacRSubdocProvCassas(List<SiacRSubdocProvCassaFin> siacRSubdocProvCassas) {
		this.siacRSubdocProvCassas = siacRSubdocProvCassas;
	}

	public SiacRSubdocProvCassaFin addSiacRSubdocProvCassa(SiacRSubdocProvCassaFin siacRSubdocProvCassa) {
		getSiacRSubdocProvCassas().add(siacRSubdocProvCassa);
		siacRSubdocProvCassa.setSiacTSubdoc(this);

		return siacRSubdocProvCassa;
	}

	public SiacRSubdocProvCassaFin removeSiacRSubdocProvCassa(SiacRSubdocProvCassaFin siacRSubdocProvCassa) {
		getSiacRSubdocProvCassas().remove(siacRSubdocProvCassa);
		siacRSubdocProvCassa.setSiacTSubdoc(null);

		return siacRSubdocProvCassa;
	}

	public List<SiacRSubdocSogFin> getSiacRSubdocSogs() {
		return this.siacRSubdocSogs;
	}

	public void setSiacRSubdocSogs(List<SiacRSubdocSogFin> siacRSubdocSogs) {
		this.siacRSubdocSogs = siacRSubdocSogs;
	}

	public SiacRSubdocSogFin addSiacRSubdocSog(SiacRSubdocSogFin siacRSubdocSog) {
		getSiacRSubdocSogs().add(siacRSubdocSog);
		siacRSubdocSog.setSiacTSubdoc(this);

		return siacRSubdocSog;
	}

	public SiacRSubdocSogFin removeSiacRSubdocSog(SiacRSubdocSogFin siacRSubdocSog) {
		getSiacRSubdocSogs().remove(siacRSubdocSog);
		siacRSubdocSog.setSiacTSubdoc(null);

		return siacRSubdocSog;
	}

	public List<SiacRSubdocSubdocIvaFin> getSiacRSubdocSubdocIvas() {
		return this.siacRSubdocSubdocIvas;
	}

	public void setSiacRSubdocSubdocIvas(List<SiacRSubdocSubdocIvaFin> siacRSubdocSubdocIvas) {
		this.siacRSubdocSubdocIvas = siacRSubdocSubdocIvas;
	}

	public SiacRSubdocSubdocIvaFin addSiacRSubdocSubdocIva(SiacRSubdocSubdocIvaFin siacRSubdocSubdocIva) {
		getSiacRSubdocSubdocIvas().add(siacRSubdocSubdocIva);
		siacRSubdocSubdocIva.setSiacTSubdoc(this);

		return siacRSubdocSubdocIva;
	}

	public SiacRSubdocSubdocIvaFin removeSiacRSubdocSubdocIva(SiacRSubdocSubdocIvaFin siacRSubdocSubdocIva) {
		getSiacRSubdocSubdocIvas().remove(siacRSubdocSubdocIva);
		siacRSubdocSubdocIva.setSiacTSubdoc(null);

		return siacRSubdocSubdocIva;
	}

	public SiacDCommissioneTipoFin getSiacDCommissioneTipo() {
		return this.siacDCommissioneTipo;
	}

	public void setSiacDCommissioneTipo(SiacDCommissioneTipoFin siacDCommissioneTipo) {
		this.siacDCommissioneTipo = siacDCommissioneTipo;
	}

	public SiacDContotesoreriaFin getSiacDContotesoreria() {
		return this.siacDContotesoreria;
	}

	public void setSiacDContotesoreria(SiacDContotesoreriaFin siacDContotesoreria) {
		this.siacDContotesoreria = siacDContotesoreria;
	}

	public SiacDDistintaFin getSiacDDistinta() {
		return this.siacDDistinta;
	}

	public void setSiacDDistinta(SiacDDistintaFin siacDDistinta) {
		this.siacDDistinta = siacDDistinta;
	}

	public SiacDNoteTesoriereFin getSiacDNoteTesoriere() {
		return this.siacDNoteTesoriere;
	}

	public void setSiacDNoteTesoriere(SiacDNoteTesoriereFin siacDNoteTesoriere) {
		this.siacDNoteTesoriere = siacDNoteTesoriere;
	}

	public SiacDSubdocTipoFin getSiacDSubdocTipo() {
		return this.siacDSubdocTipo;
	}

	public void setSiacDSubdocTipo(SiacDSubdocTipoFin siacDSubdocTipo) {
		this.siacDSubdocTipo = siacDSubdocTipo;
	}

	public SiacTDocFin getSiacTDoc() {
		return this.siacTDoc;
	}

	public void setSiacTDoc(SiacTDocFin siacTDoc) {
		this.siacTDoc = siacTDoc;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.subdocId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.subdocId = uid;
	}

}