/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_t_cassa_econ database table.
 * 
 */
@Entity
@Table(name="siac_t_cassa_econ")
@NamedQuery(name="SiacTCassaEcon.findAll", query="SELECT s FROM SiacTCassaEcon s")
public class SiacTCassaEcon extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_CASSA_ECON_CASSAECONID_GENERATOR", allocationSize = 1, sequenceName="SIAC_T_CASSA_ECON_CASSAECON_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_CASSA_ECON_CASSAECONID_GENERATOR")
	@Column(name="cassaecon_id")
	private Integer cassaeconId;

	@Column(name="cassaecon_cc")
	private String cassaeconCc;

	@Column(name="cassaecon_code")
	private String cassaeconCode;

	@Column(name="cassaecon_desc")
	private String cassaeconDesc;

	@Column(name="cassaecon_limiteimporto")
	private BigDecimal cassaeconLimiteimporto;

	@Column(name="cassaecon_resp")
	private String cassaeconResp;

	//bi-directional many-to-one association to SiacRAccountCassaEcon
	@OneToMany(mappedBy="siacTCassaEcon")
	private List<SiacRAccountCassaEcon> siacRAccountCassaEcons;
	
	//bi-directional many-to-one association to SiacRCassaEconAttr
	@OneToMany(mappedBy="siacTCassaEcon")
	private List<SiacRCassaEconAttr> siacRCassaEconAttrs;

	//bi-directional many-to-one association to SiacRGruppoRuoloOpCassaEcon
	@OneToMany(mappedBy="siacTCassaEcon")
	private List<SiacRGruppoRuoloOpCassaEcon> siacRGruppoRuoloOpCassaEcons;

	//bi-directional many-to-one association to SiacDCassaEconTipo
	@ManyToOne
	@JoinColumn(name="cassa_tipo_id")
	private SiacDCassaEconTipo siacDCassaEconTipo;
	
	//bi-directional many-to-one association to SiacDCassaEconTipo
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggetto siacTSoggetto;

	//bi-directional many-to-one association to SiacTCassaEconOperaz
	@OneToMany(mappedBy="siacTCassaEcon")
	private List<SiacTCassaEconOperaz> siacTCassaEconOperazs;

	//bi-directional many-to-one association to SiacTCassaEconStanz
	@OneToMany(mappedBy="siacTCassaEcon")
	private List<SiacTCassaEconStanz> siacTCassaEconStanzs;

	//bi-directional many-to-one association to SiacTFondoEcon
	@OneToMany(mappedBy="siacTCassaEcon")
	private List<SiacTFondoEcon> siacTFondoEcons;

	//bi-directional many-to-one association to SiacTCassaEconOperazNum
	@OneToMany(mappedBy="siacTCassaEcon")
	private List<SiacTCassaEconOperazNum> siacTCassaEconOperazNums;

	//bi-directional many-to-one association to SiacRCassaEconGiustificativo
	@OneToMany(mappedBy="siacTCassaEcon")
	private List<SiacRCassaEconGiustificativo> siacRCassaEconGiustificativos;

	//bi-directional many-to-one association to SiacRCassaEconGiustificativo
	@OneToMany(mappedBy="siacTCassaEcon")
	private List<SiacRCassaEconOperazTipoCassa> siacRCassaEconOperazTipoCassas;

	//bi-directional many-to-one association to SiacTRichiestaEconNum
	@OneToMany(mappedBy="siacTCassaEcon")
	private List<SiacTRichiestaEconNum> siacTRichiestaEconNums;
	
	//bi-directional many-to-one association to SiacTMovimentoNum
	@OneToMany(mappedBy="siacTCassaEcon")
	private List<SiacTMovimentoNum> siacTMovimentoNums;

	//bi-directional many-to-one association to SiacTMovimentoNum
	@OneToMany(mappedBy="siacTCassaEcon")
	private List<SiacTRichiestaEconSospesaNum> siacTRichiestaEconSospesaNums;

	//bi-directional many-to-one association to SiacRCassaEconBil
	@OneToMany(mappedBy="siacTCassaEcon")
	private List<SiacRCassaEconBil> siacRCassaEconBils;

	public Integer getCassaeconId() {
		return this.cassaeconId;
	}

	public void setCassaeconId(Integer cassaeconId) {
		this.cassaeconId = cassaeconId;
	}

	public String getCassaeconCc() {
		return this.cassaeconCc;
	}

	public void setCassaeconCc(String cassaeconCc) {
		this.cassaeconCc = cassaeconCc;
	}

	public String getCassaeconCode() {
		return this.cassaeconCode;
	}

	public void setCassaeconCode(String cassaeconCode) {
		this.cassaeconCode = cassaeconCode;
	}

	public String getCassaeconDesc() {
		return this.cassaeconDesc;
	}

	public void setCassaeconDesc(String cassaeconDesc) {
		this.cassaeconDesc = cassaeconDesc;
	}

	public BigDecimal getCassaeconLimiteimporto() {
		return this.cassaeconLimiteimporto;
	}

	public void setCassaeconLimiteimporto(BigDecimal cassaeconLimiteimporto) {
		this.cassaeconLimiteimporto = cassaeconLimiteimporto;
	}

	public String getCassaeconResp() {
		return this.cassaeconResp;
	}

	public void setCassaeconResp(String cassaeconResp) {
		this.cassaeconResp = cassaeconResp;
	}

	public List<SiacRAccountCassaEcon> getSiacRAccountCassaEcons() {
		return this.siacRAccountCassaEcons;
	}

	public void setSiacRAccountCassaEcons(List<SiacRAccountCassaEcon> siacRAccountCassaEcons) {
		this.siacRAccountCassaEcons = siacRAccountCassaEcons;
	}

	public SiacRAccountCassaEcon addSiacRAccountCassaEcon(SiacRAccountCassaEcon siacRAccountCassaEcon) {
		getSiacRAccountCassaEcons().add(siacRAccountCassaEcon);
		siacRAccountCassaEcon.setSiacTCassaEcon(this);

		return siacRAccountCassaEcon;
	}

	public SiacRAccountCassaEcon removeSiacRAccountCassaEcon(SiacRAccountCassaEcon siacRAccountCassaEcon) {
		getSiacRAccountCassaEcons().remove(siacRAccountCassaEcon);
		siacRAccountCassaEcon.setSiacTCassaEcon(null);

		return siacRAccountCassaEcon;
	}

	public List<SiacRGruppoRuoloOpCassaEcon> getSiacRGruppoRuoloOpCassaEcons() {
		return this.siacRGruppoRuoloOpCassaEcons;
	}

	public void setSiacRGruppoRuoloOpCassaEcons(List<SiacRGruppoRuoloOpCassaEcon> siacRGruppoRuoloOpCassaEcons) {
		this.siacRGruppoRuoloOpCassaEcons = siacRGruppoRuoloOpCassaEcons;
	}

	public SiacRGruppoRuoloOpCassaEcon addSiacRGruppoRuoloOpCassaEcon(SiacRGruppoRuoloOpCassaEcon siacRGruppoRuoloOpCassaEcon) {
		getSiacRGruppoRuoloOpCassaEcons().add(siacRGruppoRuoloOpCassaEcon);
		siacRGruppoRuoloOpCassaEcon.setSiacTCassaEcon(this);

		return siacRGruppoRuoloOpCassaEcon;
	}

	public SiacRGruppoRuoloOpCassaEcon removeSiacRGruppoRuoloOpCassaEcon(SiacRGruppoRuoloOpCassaEcon siacRGruppoRuoloOpCassaEcon) {
		getSiacRGruppoRuoloOpCassaEcons().remove(siacRGruppoRuoloOpCassaEcon);
		siacRGruppoRuoloOpCassaEcon.setSiacTCassaEcon(null);

		return siacRGruppoRuoloOpCassaEcon;
	}

	public SiacDCassaEconTipo getSiacDCassaEconTipo() {
		return this.siacDCassaEconTipo;
	}

	public void setSiacDCassaEconTipo(SiacDCassaEconTipo siacDCassaEconTipo) {
		this.siacDCassaEconTipo = siacDCassaEconTipo;
	}
	
	public SiacTSoggetto getSiacTSoggetto() {
		return siacTSoggetto;
	}

	public void setSiacTSoggetto(SiacTSoggetto siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	public List<SiacTCassaEconOperaz> getSiacTCassaEconOperazs() {
		return this.siacTCassaEconOperazs;
	}

	public void setSiacTCassaEconOperazs(List<SiacTCassaEconOperaz> siacTCassaEconOperazs) {
		this.siacTCassaEconOperazs = siacTCassaEconOperazs;
	}

	public SiacTCassaEconOperaz addSiacTCassaEconOperaz(SiacTCassaEconOperaz siacTCassaEconOperaz) {
		getSiacTCassaEconOperazs().add(siacTCassaEconOperaz);
		siacTCassaEconOperaz.setSiacTCassaEcon(this);

		return siacTCassaEconOperaz;
	}

	public SiacTCassaEconOperaz removeSiacTCassaEconOperaz(SiacTCassaEconOperaz siacTCassaEconOperaz) {
		getSiacTCassaEconOperazs().remove(siacTCassaEconOperaz);
		siacTCassaEconOperaz.setSiacTCassaEcon(null);

		return siacTCassaEconOperaz;
	}

	public List<SiacTCassaEconStanz> getSiacTCassaEconStanzs() {
		return this.siacTCassaEconStanzs;
	}

	public void setSiacTCassaEconStanzs(List<SiacTCassaEconStanz> siacTCassaEconStanzs) {
		this.siacTCassaEconStanzs = siacTCassaEconStanzs;
	}

	public SiacTCassaEconStanz addSiacTCassaEconStanz(SiacTCassaEconStanz siacTCassaEconStanz) {
		getSiacTCassaEconStanzs().add(siacTCassaEconStanz);
		siacTCassaEconStanz.setSiacTCassaEcon(this);

		return siacTCassaEconStanz;
	}

	public SiacTCassaEconStanz removeSiacTCassaEconStanz(SiacTCassaEconStanz siacTCassaEconStanz) {
		getSiacTCassaEconStanzs().remove(siacTCassaEconStanz);
		siacTCassaEconStanz.setSiacTCassaEcon(null);

		return siacTCassaEconStanz;
	}

	public List<SiacTFondoEcon> getSiacTFondoEcons() {
		return this.siacTFondoEcons;
	}

	public void setSiacTFondoEcons(List<SiacTFondoEcon> siacTFondoEcons) {
		this.siacTFondoEcons = siacTFondoEcons;
	}

	public SiacTFondoEcon addSiacTFondoEcon(SiacTFondoEcon siacTFondoEcon) {
		getSiacTFondoEcons().add(siacTFondoEcon);
		siacTFondoEcon.setSiacTCassaEcon(this);

		return siacTFondoEcon;
	}

	public SiacTFondoEcon removeSiacTFondoEcon(SiacTFondoEcon siacTFondoEcon) {
		getSiacTFondoEcons().remove(siacTFondoEcon);
		siacTFondoEcon.setSiacTCassaEcon(null);

		return siacTFondoEcon;
	}

	public List<SiacTCassaEconOperazNum> getSiacTCassaEconOperazNums() {
		return this.siacTCassaEconOperazNums;
	}

	public void setSiacTCassaEconOperazNums(List<SiacTCassaEconOperazNum> siacTCassaEconOperazNums) {
		this.siacTCassaEconOperazNums = siacTCassaEconOperazNums;
	}

	public SiacTCassaEconOperazNum addSiacTCassaEconOperazNum(SiacTCassaEconOperazNum siacTCassaEconOperazNum) {
		getSiacTCassaEconOperazNums().add(siacTCassaEconOperazNum);
		siacTCassaEconOperazNum.setSiacTCassaEcon(this);

		return siacTCassaEconOperazNum;
	}

	public SiacTCassaEconOperazNum removeSiacTCassaEconOperazNum(SiacTCassaEconOperazNum siacTCassaEconOperazNum) {
		getSiacTCassaEconOperazNums().remove(siacTCassaEconOperazNum);
		siacTCassaEconOperazNum.setSiacTCassaEcon(null);

		return siacTCassaEconOperazNum;
	}
	
	
	public List<SiacRCassaEconAttr> getSiacRCassaEconAttrs() {
		return this.siacRCassaEconAttrs;
	}

	public void setSiacRCassaEconAttrs(List<SiacRCassaEconAttr> siacRCassaEconAttrs) {
		this.siacRCassaEconAttrs = siacRCassaEconAttrs;
	}

	public SiacRCassaEconAttr addSiacRCassaEconAttr(SiacRCassaEconAttr siacRCassaEconAttr) {
		getSiacRCassaEconAttrs().add(siacRCassaEconAttr);
		siacRCassaEconAttr.setSiacTCassaEcon(this);

		return siacRCassaEconAttr;
	}

	public SiacRCassaEconAttr removeSiacRCassaEconAttr(SiacRCassaEconAttr siacRCassaEconAttr) {
		getSiacRCassaEconAttrs().remove(siacRCassaEconAttr);
		siacRCassaEconAttr.setSiacTCassaEcon(null);

		return siacRCassaEconAttr;
	}
	
	public List<SiacRCassaEconGiustificativo> getSiacRCassaEconGiustificativos() {
		return this.siacRCassaEconGiustificativos;
	}

	public void setSiacRCassaEconGiustificativos(List<SiacRCassaEconGiustificativo> siacRCassaEconGiustificativos) {
		this.siacRCassaEconGiustificativos = siacRCassaEconGiustificativos;
	}

	public SiacRCassaEconGiustificativo addSiacRCassaEconGiustificativo(SiacRCassaEconGiustificativo siacRCassaEconGiustificativo) {
		getSiacRCassaEconGiustificativos().add(siacRCassaEconGiustificativo);
		siacRCassaEconGiustificativo.setSiacTCassaEcon(this);

		return siacRCassaEconGiustificativo;
	}

	public SiacRCassaEconGiustificativo removeSiacRCassaEconGiustificativo(SiacRCassaEconGiustificativo siacRCassaEconGiustificativo) {
		getSiacRCassaEconGiustificativos().remove(siacRCassaEconGiustificativo);
		siacRCassaEconGiustificativo.setSiacTCassaEcon(null);

		return siacRCassaEconGiustificativo;
	}

	public List<SiacRCassaEconOperazTipoCassa> getSiacRCassaEconOperazTipoCassas() {
		return this.siacRCassaEconOperazTipoCassas;
	}

	public void setSiacRCassaEconOperazTipoCassas(List<SiacRCassaEconOperazTipoCassa> siacRCassaEconOperazTipoCassas) {
		this.siacRCassaEconOperazTipoCassas = siacRCassaEconOperazTipoCassas;
	}

	public SiacRCassaEconOperazTipoCassa addSiacRCassaEconOperazTipoCassa(SiacRCassaEconOperazTipoCassa siacRCassaEconOperazTipoCassa) {
		getSiacRCassaEconOperazTipoCassas().add(siacRCassaEconOperazTipoCassa);
		siacRCassaEconOperazTipoCassa.setSiacTCassaEcon(this);

		return siacRCassaEconOperazTipoCassa;
	}

	public SiacRCassaEconOperazTipoCassa removeSiacRCassaEconOperazTipoCassa(SiacRCassaEconOperazTipoCassa siacRCassaEconOperazTipoCassa) {
		getSiacRCassaEconOperazTipoCassas().remove(siacRCassaEconOperazTipoCassa);
		siacRCassaEconOperazTipoCassa.setSiacTCassaEcon(null);

		return siacRCassaEconOperazTipoCassa;
	}

	public List<SiacTRichiestaEconNum> getSiacTRichiestaEconNums() {
		return this.siacTRichiestaEconNums;
	}

	public void setSiacTRichiestaEconNums(List<SiacTRichiestaEconNum> siacTRichiestaEconNums) {
		this.siacTRichiestaEconNums = siacTRichiestaEconNums;
	}

	public SiacTRichiestaEconNum addSiacTRichiestaEconNum(SiacTRichiestaEconNum siacTRichiestaEconNum) {
		getSiacTRichiestaEconNums().add(siacTRichiestaEconNum);
		siacTRichiestaEconNum.setSiacTCassaEcon(this);

		return siacTRichiestaEconNum;
	}

	public SiacTRichiestaEconNum removeSiacTRichiestaEconNum(SiacTRichiestaEconNum siacTRichiestaEconNum) {
		getSiacTRichiestaEconNums().remove(siacTRichiestaEconNum);
		siacTRichiestaEconNum.setSiacTCassaEcon(null);

		return siacTRichiestaEconNum;
	}
	
	public List<SiacTMovimentoNum> getSiacTMovimentoNums() {
		return this.siacTMovimentoNums;
	}

	public void setSiacTMovimentoNums(List<SiacTMovimentoNum> siacTMovimentoNums) {
		this.siacTMovimentoNums = siacTMovimentoNums;
	}

	public SiacTMovimentoNum addSiacTMovimentoNum(SiacTMovimentoNum siacTMovimentoNum) {
		getSiacTMovimentoNums().add(siacTMovimentoNum);
		siacTMovimentoNum.setSiacTCassaEcon(this);

		return siacTMovimentoNum;
	}

	public SiacTMovimentoNum removeSiacTMovimentoNum(SiacTMovimentoNum siacTMovimentoNum) {
		getSiacTMovimentoNums().remove(siacTMovimentoNum);
		siacTMovimentoNum.setSiacTCassaEcon(null);

		return siacTMovimentoNum;
	}

	public List<SiacTRichiestaEconSospesaNum> getSiacTRichiestaEconSospesaNums() {
		return this.siacTRichiestaEconSospesaNums;
	}

	public void setSiacTRichiestaEconSospesaNums(List<SiacTRichiestaEconSospesaNum> siacTRichiestaEconSospesaNums) {
		this.siacTRichiestaEconSospesaNums = siacTRichiestaEconSospesaNums;
	}

	public SiacTRichiestaEconSospesaNum addSiacTRichiestaEconSospesaNum(SiacTRichiestaEconSospesaNum siacTRichiestaEconSospesaNum) {
		getSiacTRichiestaEconSospesaNums().add(siacTRichiestaEconSospesaNum);
		siacTRichiestaEconSospesaNum.setSiacTCassaEcon(this);

		return siacTRichiestaEconSospesaNum;
	}

	public SiacTRichiestaEconSospesaNum removeSiacTRichiestaEconSospesaNum(SiacTRichiestaEconSospesaNum siacTRichiestaEconSospesaNum) {
		getSiacTRichiestaEconSospesaNums().remove(siacTRichiestaEconSospesaNum);
		siacTRichiestaEconSospesaNum.setSiacTCassaEcon(null);

		return siacTRichiestaEconSospesaNum;
	}
	
	public List<SiacRCassaEconBil> getSiacRCassaEconBils() {
		return this.siacRCassaEconBils;
	}

	public void setSiacRCassaEconBils(List<SiacRCassaEconBil> siacRCassaEconBils) {
		this.siacRCassaEconBils = siacRCassaEconBils;
	}

	public SiacRCassaEconBil addSiacRCassaEconBil(SiacRCassaEconBil siacRCassaEconBil) {
		getSiacRCassaEconBils().add(siacRCassaEconBil);
		siacRCassaEconBil.setSiacTCassaEcon(this);

		return siacRCassaEconBil;
	}

	public SiacRCassaEconBil removeSiacRCassaEconBil(SiacRCassaEconBil siacRCassaEconBil) {
		getSiacRCassaEconBils().remove(siacRCassaEconBil);
		siacRCassaEconBil.setSiacTCassaEcon(null);

		return siacRCassaEconBil;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return cassaeconId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.cassaeconId = uid;
		
	}

}