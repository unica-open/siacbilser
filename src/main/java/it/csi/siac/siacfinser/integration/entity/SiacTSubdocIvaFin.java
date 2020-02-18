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

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_t_subdoc_iva database table.
 * 
 */
@Entity
@Table(name="siac_t_subdoc_iva")
public class SiacTSubdocIvaFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="subdociva_id")
	private Integer subdocivaId;

	@Column(name="subdociva_anno")
	private String subdocivaAnno;

	@Column(name="subdociva_data_cassadoc")
	private Timestamp subdocivaDataCassadoc;

	@Column(name="subdociva_data_emissione")
	private Timestamp subdocivaDataEmissione;

	@Column(name="subdociva_data_ordinativoadoc")
	private Timestamp subdocivaDataOrdinativoadoc;

	@Column(name="subdociva_data_prot_def")
	private Timestamp subdocivaDataProtDef;

	@Column(name="subdociva_data_prot_prov")
	private Timestamp subdocivaDataProtProv;

	@Column(name="subdociva_data_registrazione")
	private Timestamp subdocivaDataRegistrazione;

	@Column(name="subdociva_data_scadenza")
	private Timestamp subdocivaDataScadenza;

	@Column(name="subdociva_desc")
	private String subdocivaDesc;

	@Column(name="subdociva_importo_valuta")
	private BigDecimal subdocivaImportoValuta;

	@Column(name="subdociva_numero")
	private Integer subdocivaNumero;

	@Column(name="subdociva_numordinativodoc")
	private String subdocivaNumordinativodoc;

	@Column(name="subdociva_prot_def")
	private String subdocivaProtDef;

	@Column(name="subdociva_prot_prov")
	private String subdocivaProtProv;

	@Column(name="subdociva_soggetto_codice")
	private String subdocivaSoggettoCodice;

	//bi-directional many-to-one association to SiacRIvamovFin
	@OneToMany(mappedBy="siacTSubdocIva")
	private List<SiacRIvamovFin> siacRIvamovs;

	//bi-directional many-to-one association to SiacRSubdocIvaFin
	@OneToMany(mappedBy="siacTSubdocIva1")
	private List<SiacRSubdocIvaFin> siacRSubdocIvas1;

	//bi-directional many-to-one association to SiacRSubdocIvaFin
	@OneToMany(mappedBy="siacTSubdocIva2")
	private List<SiacRSubdocIvaFin> siacRSubdocIvas2;

	//bi-directional many-to-one association to SiacRSubdocIvaAttrFin
	@OneToMany(mappedBy="siacTSubdocIva")
	private List<SiacRSubdocIvaAttrFin> siacRSubdocIvaAttrs;

	//bi-directional many-to-one association to SiacRSubdocIvaStatoFin
	@OneToMany(mappedBy="siacTSubdocIva")
	private List<SiacRSubdocIvaStatoFin> siacRSubdocIvaStatos;

	//bi-directional many-to-one association to SiacRSubdocSubdocIvaFin
	@OneToMany(mappedBy="siacTSubdocIva")
	private List<SiacRSubdocSubdocIvaFin> siacRSubdocSubdocIvas;

	//bi-directional many-to-one association to SiacDIvaRegistrazioneTipoFin
	@ManyToOne
	@JoinColumn(name="reg_tipo_id")
	private SiacDIvaRegistrazioneTipoFin siacDIvaRegistrazioneTipo;

	//bi-directional many-to-one association to SiacDValutaFin
	@ManyToOne
	@JoinColumn(name="valuta_id")
	private SiacDValutaFin siacDValuta;

	//bi-directional many-to-one association to SiacRDocIvaFin
	@ManyToOne
	@JoinColumn(name="dociva_r_id")
	private SiacRDocIvaFin siacRDocIva;

	//bi-directional many-to-one association to SiacTIvaAttivitaFin
	@ManyToOne
	@JoinColumn(name="ivaatt_id")
	private SiacTIvaAttivitaFin siacTIvaAttivita;

	//bi-directional many-to-one association to SiacTIvaRegistroFin
	@ManyToOne
	@JoinColumn(name="ivareg_id")
	private SiacTIvaRegistroFin siacTIvaRegistro;

	public SiacTSubdocIvaFin() {
	}

	public Integer getSubdocivaId() {
		return this.subdocivaId;
	}

	public void setSubdocivaId(Integer subdocivaId) {
		this.subdocivaId = subdocivaId;
	}

	public String getSubdocivaAnno() {
		return this.subdocivaAnno;
	}

	public void setSubdocivaAnno(String subdocivaAnno) {
		this.subdocivaAnno = subdocivaAnno;
	}

	public Timestamp getSubdocivaDataCassadoc() {
		return this.subdocivaDataCassadoc;
	}

	public void setSubdocivaDataCassadoc(Timestamp subdocivaDataCassadoc) {
		this.subdocivaDataCassadoc = subdocivaDataCassadoc;
	}

	public Timestamp getSubdocivaDataEmissione() {
		return this.subdocivaDataEmissione;
	}

	public void setSubdocivaDataEmissione(Timestamp subdocivaDataEmissione) {
		this.subdocivaDataEmissione = subdocivaDataEmissione;
	}

	public Timestamp getSubdocivaDataOrdinativoadoc() {
		return this.subdocivaDataOrdinativoadoc;
	}

	public void setSubdocivaDataOrdinativoadoc(Timestamp subdocivaDataOrdinativoadoc) {
		this.subdocivaDataOrdinativoadoc = subdocivaDataOrdinativoadoc;
	}

	public Timestamp getSubdocivaDataProtDef() {
		return this.subdocivaDataProtDef;
	}

	public void setSubdocivaDataProtDef(Timestamp subdocivaDataProtDef) {
		this.subdocivaDataProtDef = subdocivaDataProtDef;
	}

	public Timestamp getSubdocivaDataProtProv() {
		return this.subdocivaDataProtProv;
	}

	public void setSubdocivaDataProtProv(Timestamp subdocivaDataProtProv) {
		this.subdocivaDataProtProv = subdocivaDataProtProv;
	}

	public Timestamp getSubdocivaDataRegistrazione() {
		return this.subdocivaDataRegistrazione;
	}

	public void setSubdocivaDataRegistrazione(Timestamp subdocivaDataRegistrazione) {
		this.subdocivaDataRegistrazione = subdocivaDataRegistrazione;
	}

	public Timestamp getSubdocivaDataScadenza() {
		return this.subdocivaDataScadenza;
	}

	public void setSubdocivaDataScadenza(Timestamp subdocivaDataScadenza) {
		this.subdocivaDataScadenza = subdocivaDataScadenza;
	}

	public String getSubdocivaDesc() {
		return this.subdocivaDesc;
	}

	public void setSubdocivaDesc(String subdocivaDesc) {
		this.subdocivaDesc = subdocivaDesc;
	}

	public BigDecimal getSubdocivaImportoValuta() {
		return this.subdocivaImportoValuta;
	}

	public void setSubdocivaImportoValuta(BigDecimal subdocivaImportoValuta) {
		this.subdocivaImportoValuta = subdocivaImportoValuta;
	}

	public Integer getSubdocivaNumero() {
		return this.subdocivaNumero;
	}

	public void setSubdocivaNumero(Integer subdocivaNumero) {
		this.subdocivaNumero = subdocivaNumero;
	}

	public String getSubdocivaNumordinativodoc() {
		return this.subdocivaNumordinativodoc;
	}

	public void setSubdocivaNumordinativodoc(String subdocivaNumordinativodoc) {
		this.subdocivaNumordinativodoc = subdocivaNumordinativodoc;
	}

	public String getSubdocivaProtDef() {
		return this.subdocivaProtDef;
	}

	public void setSubdocivaProtDef(String subdocivaProtDef) {
		this.subdocivaProtDef = subdocivaProtDef;
	}

	public String getSubdocivaProtProv() {
		return this.subdocivaProtProv;
	}

	public void setSubdocivaProtProv(String subdocivaProtProv) {
		this.subdocivaProtProv = subdocivaProtProv;
	}

	public String getSubdocivaSoggettoCodice() {
		return this.subdocivaSoggettoCodice;
	}

	public void setSubdocivaSoggettoCodice(String subdocivaSoggettoCodice) {
		this.subdocivaSoggettoCodice = subdocivaSoggettoCodice;
	}

	public List<SiacRIvamovFin> getSiacRIvamovs() {
		return this.siacRIvamovs;
	}

	public void setSiacRIvamovs(List<SiacRIvamovFin> siacRIvamovs) {
		this.siacRIvamovs = siacRIvamovs;
	}

	public SiacRIvamovFin addSiacRIvamov(SiacRIvamovFin siacRIvamov) {
		getSiacRIvamovs().add(siacRIvamov);
		siacRIvamov.setSiacTSubdocIva(this);

		return siacRIvamov;
	}

	public SiacRIvamovFin removeSiacRIvamov(SiacRIvamovFin siacRIvamov) {
		getSiacRIvamovs().remove(siacRIvamov);
		siacRIvamov.setSiacTSubdocIva(null);

		return siacRIvamov;
	}

	public List<SiacRSubdocIvaFin> getSiacRSubdocIvas1() {
		return this.siacRSubdocIvas1;
	}

	public void setSiacRSubdocIvas1(List<SiacRSubdocIvaFin> siacRSubdocIvas1) {
		this.siacRSubdocIvas1 = siacRSubdocIvas1;
	}

	public SiacRSubdocIvaFin addSiacRSubdocIvas1(SiacRSubdocIvaFin siacRSubdocIvas1) {
		getSiacRSubdocIvas1().add(siacRSubdocIvas1);
		siacRSubdocIvas1.setSiacTSubdocIva1(this);

		return siacRSubdocIvas1;
	}

	public SiacRSubdocIvaFin removeSiacRSubdocIvas1(SiacRSubdocIvaFin siacRSubdocIvas1) {
		getSiacRSubdocIvas1().remove(siacRSubdocIvas1);
		siacRSubdocIvas1.setSiacTSubdocIva1(null);

		return siacRSubdocIvas1;
	}

	public List<SiacRSubdocIvaFin> getSiacRSubdocIvas2() {
		return this.siacRSubdocIvas2;
	}

	public void setSiacRSubdocIvas2(List<SiacRSubdocIvaFin> siacRSubdocIvas2) {
		this.siacRSubdocIvas2 = siacRSubdocIvas2;
	}

	public SiacRSubdocIvaFin addSiacRSubdocIvas2(SiacRSubdocIvaFin siacRSubdocIvas2) {
		getSiacRSubdocIvas2().add(siacRSubdocIvas2);
		siacRSubdocIvas2.setSiacTSubdocIva2(this);

		return siacRSubdocIvas2;
	}

	public SiacRSubdocIvaFin removeSiacRSubdocIvas2(SiacRSubdocIvaFin siacRSubdocIvas2) {
		getSiacRSubdocIvas2().remove(siacRSubdocIvas2);
		siacRSubdocIvas2.setSiacTSubdocIva2(null);

		return siacRSubdocIvas2;
	}

	public List<SiacRSubdocIvaAttrFin> getSiacRSubdocIvaAttrs() {
		return this.siacRSubdocIvaAttrs;
	}

	public void setSiacRSubdocIvaAttrs(List<SiacRSubdocIvaAttrFin> siacRSubdocIvaAttrs) {
		this.siacRSubdocIvaAttrs = siacRSubdocIvaAttrs;
	}

	public SiacRSubdocIvaAttrFin addSiacRSubdocIvaAttr(SiacRSubdocIvaAttrFin siacRSubdocIvaAttr) {
		getSiacRSubdocIvaAttrs().add(siacRSubdocIvaAttr);
		siacRSubdocIvaAttr.setSiacTSubdocIva(this);

		return siacRSubdocIvaAttr;
	}

	public SiacRSubdocIvaAttrFin removeSiacRSubdocIvaAttr(SiacRSubdocIvaAttrFin siacRSubdocIvaAttr) {
		getSiacRSubdocIvaAttrs().remove(siacRSubdocIvaAttr);
		siacRSubdocIvaAttr.setSiacTSubdocIva(null);

		return siacRSubdocIvaAttr;
	}

	public List<SiacRSubdocIvaStatoFin> getSiacRSubdocIvaStatos() {
		return this.siacRSubdocIvaStatos;
	}

	public void setSiacRSubdocIvaStatos(List<SiacRSubdocIvaStatoFin> siacRSubdocIvaStatos) {
		this.siacRSubdocIvaStatos = siacRSubdocIvaStatos;
	}

	public SiacRSubdocIvaStatoFin addSiacRSubdocIvaStato(SiacRSubdocIvaStatoFin siacRSubdocIvaStato) {
		getSiacRSubdocIvaStatos().add(siacRSubdocIvaStato);
		siacRSubdocIvaStato.setSiacTSubdocIva(this);

		return siacRSubdocIvaStato;
	}

	public SiacRSubdocIvaStatoFin removeSiacRSubdocIvaStato(SiacRSubdocIvaStatoFin siacRSubdocIvaStato) {
		getSiacRSubdocIvaStatos().remove(siacRSubdocIvaStato);
		siacRSubdocIvaStato.setSiacTSubdocIva(null);

		return siacRSubdocIvaStato;
	}

	public List<SiacRSubdocSubdocIvaFin> getSiacRSubdocSubdocIvas() {
		return this.siacRSubdocSubdocIvas;
	}

	public void setSiacRSubdocSubdocIvas(List<SiacRSubdocSubdocIvaFin> siacRSubdocSubdocIvas) {
		this.siacRSubdocSubdocIvas = siacRSubdocSubdocIvas;
	}

	public SiacRSubdocSubdocIvaFin addSiacRSubdocSubdocIva(SiacRSubdocSubdocIvaFin siacRSubdocSubdocIva) {
		getSiacRSubdocSubdocIvas().add(siacRSubdocSubdocIva);
		siacRSubdocSubdocIva.setSiacTSubdocIva(this);

		return siacRSubdocSubdocIva;
	}

	public SiacRSubdocSubdocIvaFin removeSiacRSubdocSubdocIva(SiacRSubdocSubdocIvaFin siacRSubdocSubdocIva) {
		getSiacRSubdocSubdocIvas().remove(siacRSubdocSubdocIva);
		siacRSubdocSubdocIva.setSiacTSubdocIva(null);

		return siacRSubdocSubdocIva;
	}

	public SiacDIvaRegistrazioneTipoFin getSiacDIvaRegistrazioneTipo() {
		return this.siacDIvaRegistrazioneTipo;
	}

	public void setSiacDIvaRegistrazioneTipo(SiacDIvaRegistrazioneTipoFin siacDIvaRegistrazioneTipo) {
		this.siacDIvaRegistrazioneTipo = siacDIvaRegistrazioneTipo;
	}

	public SiacDValutaFin getSiacDValuta() {
		return this.siacDValuta;
	}

	public void setSiacDValuta(SiacDValutaFin siacDValuta) {
		this.siacDValuta = siacDValuta;
	}

	public SiacRDocIvaFin getSiacRDocIva() {
		return this.siacRDocIva;
	}

	public void setSiacRDocIva(SiacRDocIvaFin siacRDocIva) {
		this.siacRDocIva = siacRDocIva;
	}

	public SiacTIvaAttivitaFin getSiacTIvaAttivita() {
		return this.siacTIvaAttivita;
	}

	public void setSiacTIvaAttivita(SiacTIvaAttivitaFin siacTIvaAttivita) {
		this.siacTIvaAttivita = siacTIvaAttivita;
	}

	public SiacTIvaRegistroFin getSiacTIvaRegistro() {
		return this.siacTIvaRegistro;
	}

	public void setSiacTIvaRegistro(SiacTIvaRegistroFin siacTIvaRegistro) {
		this.siacTIvaRegistro = siacTIvaRegistro;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.subdocivaId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.subdocivaId = uid;
	}

}