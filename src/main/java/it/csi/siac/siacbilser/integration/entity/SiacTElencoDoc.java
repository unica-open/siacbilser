/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_t_elenco_doc database table.
 * 
 */
@Entity
@Table(name="siac_t_elenco_doc")
@NamedQuery(name="SiacTElencoDoc.findAll", query="SELECT s FROM SiacTElencoDoc s")
public class SiacTElencoDoc extends SiacTEnteBaseExt {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_ELENCO_DOC_ELDOCID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_ELENCO_DOC_ELDOC_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_ELENCO_DOC_ELDOCID_GENERATOR")
	@Column(name="eldoc_id")
	private Integer eldocId;

	@Column(name="eldoc_anno")
	private Integer eldocAnno;

	@Column(name="eldoc_data_trasmissione")
	private Date eldocDataTrasmissione;

	@Column(name="eldoc_numero")
	private Integer eldocNumero;

	@Column(name="eldoc_sysesterno")
	private String eldocSysesterno;

	@Column(name="eldoc_sysesterno_anno")
	private Integer eldocSysesternoAnno;

	@Column(name="eldoc_sysesterno_numero")
	private String eldocSysesternoNumero;

	@Column(name="eldoc_tot_daincassare")
	private BigDecimal eldocTotDaincassare;

	@Column(name="eldoc_tot_dapagare")
	private BigDecimal eldocTotDapagare;

	@Column(name="eldoc_tot_quoteentrate")
	private BigDecimal eldocTotQuoteentrate;

	@Column(name="eldoc_tot_quotespese")
	private BigDecimal eldocTotQuotespese;

	//bi-directional many-to-one association to SiacTAttoAllegato
	@OneToMany(mappedBy="siacTElencoDoc", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRAttoAllegatoElencoDoc> siacRAttoAllegatoElencoDocs;

	//bi-directional many-to-one association to SiacRElencoDocStato
	@OneToMany(mappedBy="siacTElencoDoc", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRElencoDocStato> siacRElencoDocStatos;

	//bi-directional many-to-one association to SiacRElencoDocSubdoc
	@OneToMany(mappedBy="siacTElencoDoc", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRElencoDocSubdoc> siacRElencoDocSubdocs;
	
	//bi-directional many-to-one association to SiacRElencoDocPredoc
	/** The siac r elenco doc predocs. */
	@OneToMany(mappedBy="siacTElencoDoc")
	private List<SiacRElencoDocPredoc> siacRElencoDocPredocs;

	public SiacTElencoDoc() {
	}

	public Integer getEldocId() {
		return this.eldocId;
	}

	public void setEldocId(Integer eldocId) {
		this.eldocId = eldocId;
	}

	public Integer getEldocAnno() {
		return this.eldocAnno;
	}

	public void setEldocAnno(Integer eldocAnno) {
		this.eldocAnno = eldocAnno;
	}

	public Date getEldocDataTrasmissione() {
		return this.eldocDataTrasmissione;
	}

	public void setEldocDataTrasmissione( Date eldocDataTrasmissione) {
		this.eldocDataTrasmissione = eldocDataTrasmissione;
	}

	public Integer getEldocNumero() {
		return this.eldocNumero;
	}

	public void setEldocNumero(Integer eldocNumero) {
		this.eldocNumero = eldocNumero;
	}

	public String getEldocSysesterno() {
		return this.eldocSysesterno;
	}

	public void setEldocSysesterno(String eldocSysesterno) {
		this.eldocSysesterno = eldocSysesterno;
	}

	public Integer getEldocSysesternoAnno() {
		return this.eldocSysesternoAnno;
	}

	public void setEldocSysesternoAnno(Integer eldocSysesternoAnno) {
		this.eldocSysesternoAnno = eldocSysesternoAnno;
	}

	public String getEldocSysesternoNumero() {
		return this.eldocSysesternoNumero;
	}

	public void setEldocSysesternoNumero(String eldocSysesternoNumero) {
		this.eldocSysesternoNumero = eldocSysesternoNumero;
	}

	public BigDecimal getEldocTotDaincassare() {
		return this.eldocTotDaincassare;
	}

	public void setEldocTotDaincassare(BigDecimal eldocTotDaincassare) {
		this.eldocTotDaincassare = eldocTotDaincassare;
	}

	public BigDecimal getEldocTotDapagare() {
		return this.eldocTotDapagare;
	}

	public void setEldocTotDapagare(BigDecimal eldocTotDapagare) {
		this.eldocTotDapagare = eldocTotDapagare;
	}

	public BigDecimal getEldocTotQuoteentrate() {
		return this.eldocTotQuoteentrate;
	}

	public void setEldocTotQuoteentrate(BigDecimal eldocTotQuoteentrate) {
		this.eldocTotQuoteentrate = eldocTotQuoteentrate;
	}

	public BigDecimal getEldocTotQuotespese() {
		return this.eldocTotQuotespese;
	}

	public void setEldocTotQuotespese(BigDecimal eldocTotQuotespese) {
		this.eldocTotQuotespese = eldocTotQuotespese;
	}

	public List<SiacRAttoAllegatoElencoDoc> getSiacRAttoAllegatoElencoDocs() {
		return this.siacRAttoAllegatoElencoDocs;
	}

	public void setSiacRAttoAllegatoElencoDocs(List<SiacRAttoAllegatoElencoDoc> siacRAttoAllegatos) {
		this.siacRAttoAllegatoElencoDocs = siacRAttoAllegatos;
	}

	public SiacRAttoAllegatoElencoDoc addSiacRAttoAllegatoElencoDoc(SiacRAttoAllegatoElencoDoc siacRAttoAllegato) {
		getSiacRAttoAllegatoElencoDocs().add(siacRAttoAllegato);
		siacRAttoAllegato.setSiacTElencoDoc(this);

		return siacRAttoAllegato;
	}

	public SiacRAttoAllegatoElencoDoc removeSiacRAttoAllegatoElencoDoc(SiacRAttoAllegatoElencoDoc siacRAttoAllegato) {
		getSiacRAttoAllegatoElencoDocs().remove(siacRAttoAllegato);
		siacRAttoAllegato.setSiacTElencoDoc(null);

		return siacRAttoAllegato;
	}

	public List<SiacRElencoDocStato> getSiacRElencoDocStatos() {
		return this.siacRElencoDocStatos;
	}

	public void setSiacRElencoDocStatos(List<SiacRElencoDocStato> siacRElencoDocStatos) {
		this.siacRElencoDocStatos = siacRElencoDocStatos;
	}

	public SiacRElencoDocStato addSiacRElencoDocStato(SiacRElencoDocStato siacRElencoDocStato) {
		getSiacRElencoDocStatos().add(siacRElencoDocStato);
		siacRElencoDocStato.setSiacTElencoDoc(this);

		return siacRElencoDocStato;
	}

	public SiacRElencoDocStato removeSiacRElencoDocStato(SiacRElencoDocStato siacRElencoDocStato) {
		getSiacRElencoDocStatos().remove(siacRElencoDocStato);
		siacRElencoDocStato.setSiacTElencoDoc(null);

		return siacRElencoDocStato;
	}

	public List<SiacRElencoDocSubdoc> getSiacRElencoDocSubdocs() {
		return this.siacRElencoDocSubdocs;
	}

	public void setSiacRElencoDocSubdocs(List<SiacRElencoDocSubdoc> siacRElencoDocSubdocs) {
		this.siacRElencoDocSubdocs = siacRElencoDocSubdocs;
	}

	public SiacRElencoDocSubdoc addSiacRElencoDocSubdoc(SiacRElencoDocSubdoc siacRElencoDocSubdoc) {
		getSiacRElencoDocSubdocs().add(siacRElencoDocSubdoc);
		siacRElencoDocSubdoc.setSiacTElencoDoc(this);

		return siacRElencoDocSubdoc;
	}

	public SiacRElencoDocSubdoc removeSiacRElencoDocSubdoc(SiacRElencoDocSubdoc siacRElencoDocSubdoc) {
		getSiacRElencoDocSubdocs().remove(siacRElencoDocSubdoc);
		siacRElencoDocSubdoc.setSiacTElencoDoc(null);

		return siacRElencoDocSubdoc;
	}
	
	/**
	 * Gets the siac r elenco doc predocs.
	 *
	 * @return the siac r elenco doc predocs
	 */
	public List<SiacRElencoDocPredoc> getSiacRElencoDocPredocs() {
		return this.siacRElencoDocPredocs;
	}

	/**
	 * Sets the siac e elenco doc predocs.
	 *
	 * @param siacRElencoDocPredocs the new siac r elenco doc predocs
	 */
	public void setSiacRElencoDocPredocs(List<SiacRElencoDocPredoc> siacRElencoDocPredocs) {
		this.siacRElencoDocPredocs = siacRElencoDocPredocs;
	}

	/**
	 * Adds the siac r elenco doc predoc.
	 *
	 * @param siacRElencoDocPredoc the siac r elenco doc predoc
	 * @return the siac r elenco doc predoc
	 */
	public SiacRElencoDocPredoc addSiacRElencoDocPredoc(SiacRElencoDocPredoc siacRElencoDocPredoc) {
		getSiacRElencoDocPredocs().add(siacRElencoDocPredoc);
		siacRElencoDocPredoc.setSiacTElencoDoc(this);

		return siacRElencoDocPredoc;
	}

	/**
	 * Removes the siac r elenco doc predoc.
	 *
	 * @param siacRElencoDocPredoc the siac r elenco doc predoc
	 * @return the siac r elenco doc predoc
	 */
	public SiacRElencoDocPredoc removeSiacRElencoDocPredoc(SiacRElencoDocPredoc siacRElencoDocPredoc) {
		getSiacRElencoDocPredocs().remove(siacRElencoDocPredoc);
		siacRElencoDocPredoc.setSiacTElencoDoc(null);

		return siacRElencoDocPredoc;
	}

	@Override
	public Integer getUid() {
		return eldocId;
	}

	@Override
	public void setUid(Integer uid) {
		this.eldocId = uid;
		
	}

}