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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacLoginMultiplo;


/**
 * The persistent class for the siac_t_elenco_doc database table.
 * 
 */
@Entity
@Table(name="siac_t_elenco_doc")
public class SiacTElencoDocFin extends SiacLoginMultiplo {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="eldoc_id")
	private Integer eldocId;

	@Column(name="eldoc_anno")
	private Integer eldocAnno;

	@Column(name="eldoc_data_trasmissione")
	private Timestamp eldocDataTrasmissione;

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

	//bi-directional many-to-one association to SiacRAttoAllegatoFin
	@OneToMany(mappedBy="siacTElencoDoc")
	private List<SiacRAttoAllegatoElencoDocFin> siacRAttoAllegatos;

	//bi-directional many-to-one association to SiacRElencoDocStatoFin
	@OneToMany(mappedBy="siacTElencoDoc")
	private List<SiacRElencoDocStatoFin> siacRElencoDocStatos;

	//bi-directional many-to-one association to SiacRElencoDocSubdocFin
	@OneToMany(mappedBy="siacTElencoDoc")
	private List<SiacRElencoDocSubdocFin> siacRElencoDocSubdocs;

	public SiacTElencoDocFin() {
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

	public Timestamp getEldocDataTrasmissione() {
		return this.eldocDataTrasmissione;
	}

	public void setEldocDataTrasmissione(Timestamp eldocDataTrasmissione) {
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



	/**
	 * @return the siacRAttoAllegatos
	 */
	public List<SiacRAttoAllegatoElencoDocFin> getSiacRAttoAllegatos() {
		return siacRAttoAllegatos;
	}

	/**
	 * @param siacRAttoAllegatos the siacRAttoAllegatos to set
	 */
	public void setSiacRAttoAllegatos(
			List<SiacRAttoAllegatoElencoDocFin> siacRAttoAllegatos) {
		this.siacRAttoAllegatos = siacRAttoAllegatos;
	}


	public List<SiacRElencoDocStatoFin> getSiacRElencoDocStatos() {
		return this.siacRElencoDocStatos;
	}

	public void setSiacRElencoDocStatos(List<SiacRElencoDocStatoFin> siacRElencoDocStatos) {
		this.siacRElencoDocStatos = siacRElencoDocStatos;
	}

	public SiacRElencoDocStatoFin addSiacRElencoDocStato(SiacRElencoDocStatoFin siacRElencoDocStato) {
		getSiacRElencoDocStatos().add(siacRElencoDocStato);
		siacRElencoDocStato.setSiacTElencoDoc(this);

		return siacRElencoDocStato;
	}

	public SiacRElencoDocStatoFin removeSiacRElencoDocStato(SiacRElencoDocStatoFin siacRElencoDocStato) {
		getSiacRElencoDocStatos().remove(siacRElencoDocStato);
		siacRElencoDocStato.setSiacTElencoDoc(null);

		return siacRElencoDocStato;
	}

	public List<SiacRElencoDocSubdocFin> getSiacRElencoDocSubdocs() {
		return this.siacRElencoDocSubdocs;
	}

	public void setSiacRElencoDocSubdocs(List<SiacRElencoDocSubdocFin> siacRElencoDocSubdocs) {
		this.siacRElencoDocSubdocs = siacRElencoDocSubdocs;
	}

	public SiacRElencoDocSubdocFin addSiacRElencoDocSubdoc(SiacRElencoDocSubdocFin siacRElencoDocSubdoc) {
		getSiacRElencoDocSubdocs().add(siacRElencoDocSubdoc);
		siacRElencoDocSubdoc.setSiacTElencoDoc(this);

		return siacRElencoDocSubdoc;
	}

	public SiacRElencoDocSubdocFin removeSiacRElencoDocSubdoc(SiacRElencoDocSubdocFin siacRElencoDocSubdoc) {
		getSiacRElencoDocSubdocs().remove(siacRElencoDocSubdoc);
		siacRElencoDocSubdoc.setSiacTElencoDoc(null);

		return siacRElencoDocSubdoc;
	}


	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.eldocId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.eldocId = uid;
	}

}