/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_bil database table.
 * 
 */
@Entity
@Table(name="siac_t_bil")
public class SiacTBil extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The bil id. */
	@Id
	@SequenceGenerator(name="SIAC_T_BIL_BILID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_BIL_BIL_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_BIL_BILID_GENERATOR")
	@Column(name="bil_id")
	private Integer bilId;

	/** The bil code. */
	@Column(name="bil_code")
	private String bilCode;

	/** The bil desc. */
	@Column(name="bil_desc")
	private String bilDesc;

	
	
	
	

	//bi-directional many-to-one association to SiacRBilFaseOperativa
	/** The siac r bil fase operativas. */
	@OneToMany(mappedBy="siacTBil")
	private List<SiacRBilFaseOperativa> siacRBilFaseOperativas;

	//bi-directional many-to-one association to SiacRBilStatoOp
	/** The siac r bil stato ops. */
	@OneToMany(mappedBy="siacTBil")
	private List<SiacRBilStatoOp> siacRBilStatoOps;

	//bi-directional many-to-one association to SiacDBilTipo
	/** The siac d bil tipo. */
	@ManyToOne
	@JoinColumn(name="bil_tipo_id")
	private SiacDBilTipo siacDBilTipo;

	//bi-directional many-to-one association to SiacTPeriodo
	/** The siac t periodo. */
	@ManyToOne
	@JoinColumn(name="periodo_id")
	private SiacTPeriodo siacTPeriodo;

	//bi-directional many-to-one association to SiacTBilElem
	/** The siac t bil elems. */
	@OneToMany(mappedBy="siacTBil")
	private List<SiacTBilElem> siacTBilElems;

	//bi-directional many-to-one association to SiacTCartacont
	@OneToMany(mappedBy="siacTBil")
	private List<SiacTCartacont> siacTCartaconts;

	//bi-directional many-to-one association to SiacTCassaEconStanz
	@OneToMany(mappedBy="siacTBil")
	private List<SiacTCassaEconStanz> siacTCassaEconStanzs;

	//bi-directional many-to-one association to SiacTCronop
	@OneToMany(mappedBy="siacTBil")
	private List<SiacTCronop> siacTCronops;

	//bi-directional many-to-one association to SiacTElencoDocNum
	@OneToMany(mappedBy="siacTBil")
	private List<SiacTElencoDocNum> siacTElencoDocNums;

	//bi-directional many-to-one association to SiacTLiquidazione
	@OneToMany(mappedBy="siacTBil")
	private List<SiacTLiquidazione> siacTLiquidaziones;

	//bi-directional many-to-one association to SiacTMovgest
	@OneToMany(mappedBy="siacTBil")
	private List<SiacTMovgest> siacTMovgests;

	//bi-directional many-to-one association to SiacTOrdinativo
	@OneToMany(mappedBy="siacTBil")
	private List<SiacTOrdinativo> siacTOrdinativos;

	//bi-directional many-to-one association to SiacTReportImporti
	@OneToMany(mappedBy="siacTBil")
	private List<SiacTReportImporti> siacTReportImportis;
	
	//bi-directional many-to-one association to SiacTPrimaNota
	@OneToMany(mappedBy="siacTBil")
	private List<SiacTPrimaNota> siacTPrimaNotas;

	//bi-directional many-to-one association to SiacTRegMovfin
	@OneToMany(mappedBy="siacTBil")
	private List<SiacTRegMovfin> siacTRegMovfins;

	//bi-directional many-to-one association to SiacTRichiestaEcon
	@OneToMany(mappedBy="siacTBil")
	private List<SiacTRichiestaEcon> siacTRichiestaEcons;

	//bi-directional many-to-one association to SiacTVariazione
	@OneToMany(mappedBy="siacTBil")
	private List<SiacTVariazione> siacTVariaziones;

	//bi-directional many-to-one association to SiacTVariazioneNum
	@OneToMany(mappedBy="siacTBil")
	private List<SiacTVariazioneNum> siacTVariazioneNums;

	//bi-directional many-to-one association to SiacRCassaEconBil
	@OneToMany(mappedBy="siacTBil")
	private List<SiacRCassaEconBil> siacRCassaEconBils;
	
	//bi-directional many-to-one association to SiacRBilAttr
	@OneToMany(mappedBy="siacTBil", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRBilAttr> siacRBilAttrs;

	/**
	 * Instantiates a new siac t bil.
	 */
	public SiacTBil() {
	}

	/**
	 * Gets the bil id.
	 *
	 * @return the bil id
	 */
	public Integer getBilId() {
		return this.bilId;
	}

	/**
	 * Sets the bil id.
	 *
	 * @param bilId the new bil id
	 */
	public void setBilId(Integer bilId) {
		this.bilId = bilId;
	}

	/**
	 * Gets the bil code.
	 *
	 * @return the bil code
	 */
	public String getBilCode() {
		return this.bilCode;
	}

	/**
	 * Sets the bil code.
	 *
	 * @param bilCode the new bil code
	 */
	public void setBilCode(String bilCode) {
		this.bilCode = bilCode;
	}

	/**
	 * Gets the bil desc.
	 *
	 * @return the bil desc
	 */
	public String getBilDesc() {
		return this.bilDesc;
	}

	/**
	 * Sets the bil desc.
	 *
	 * @param bilDesc the new bil desc
	 */
	public void setBilDesc(String bilDesc) {
		this.bilDesc = bilDesc;
	}

	

	/**
	 * Gets the siac r bil fase operativas.
	 *
	 * @return the siac r bil fase operativas
	 */
	public List<SiacRBilFaseOperativa> getSiacRBilFaseOperativas() {
		return this.siacRBilFaseOperativas;
	}

	/**
	 * Sets the siac r bil fase operativas.
	 *
	 * @param siacRBilFaseOperativas the new siac r bil fase operativas
	 */
	public void setSiacRBilFaseOperativas(List<SiacRBilFaseOperativa> siacRBilFaseOperativas) {
		this.siacRBilFaseOperativas = siacRBilFaseOperativas;
	}

	/**
	 * Adds the siac r bil fase operativa.
	 *
	 * @param siacRBilFaseOperativa the siac r bil fase operativa
	 * @return the siac r bil fase operativa
	 */
	public SiacRBilFaseOperativa addSiacRBilFaseOperativa(SiacRBilFaseOperativa siacRBilFaseOperativa) {
		getSiacRBilFaseOperativas().add(siacRBilFaseOperativa);
		siacRBilFaseOperativa.setSiacTBil(this);

		return siacRBilFaseOperativa;
	}

	/**
	 * Removes the siac r bil fase operativa.
	 *
	 * @param siacRBilFaseOperativa the siac r bil fase operativa
	 * @return the siac r bil fase operativa
	 */
	public SiacRBilFaseOperativa removeSiacRBilFaseOperativa(SiacRBilFaseOperativa siacRBilFaseOperativa) {
		getSiacRBilFaseOperativas().remove(siacRBilFaseOperativa);
		siacRBilFaseOperativa.setSiacTBil(null);

		return siacRBilFaseOperativa;
	}

	/**
	 * Gets the siac r bil stato ops.
	 *
	 * @return the siac r bil stato ops
	 */
	public List<SiacRBilStatoOp> getSiacRBilStatoOps() {
		return this.siacRBilStatoOps;
	}

	/**
	 * Sets the siac r bil stato ops.
	 *
	 * @param siacRBilStatoOps the new siac r bil stato ops
	 */
	public void setSiacRBilStatoOps(List<SiacRBilStatoOp> siacRBilStatoOps) {
		this.siacRBilStatoOps = siacRBilStatoOps;
	}

	/**
	 * Adds the siac r bil stato op.
	 *
	 * @param siacRBilStatoOp the siac r bil stato op
	 * @return the siac r bil stato op
	 */
	public SiacRBilStatoOp addSiacRBilStatoOp(SiacRBilStatoOp siacRBilStatoOp) {
		getSiacRBilStatoOps().add(siacRBilStatoOp);
		siacRBilStatoOp.setSiacTBil(this);

		return siacRBilStatoOp;
	}

	/**
	 * Removes the siac r bil stato op.
	 *
	 * @param siacRBilStatoOp the siac r bil stato op
	 * @return the siac r bil stato op
	 */
	public SiacRBilStatoOp removeSiacRBilStatoOp(SiacRBilStatoOp siacRBilStatoOp) {
		getSiacRBilStatoOps().remove(siacRBilStatoOp);
		siacRBilStatoOp.setSiacTBil(null);

		return siacRBilStatoOp;
	}

	/**
	 * Gets the siac d bil tipo.
	 *
	 * @return the siac d bil tipo
	 */
	public SiacDBilTipo getSiacDBilTipo() {
		return this.siacDBilTipo;
	}

	/**
	 * Sets the siac d bil tipo.
	 *
	 * @param siacDBilTipo the new siac d bil tipo
	 */
	public void setSiacDBilTipo(SiacDBilTipo siacDBilTipo) {
		this.siacDBilTipo = siacDBilTipo;
	}



	/**
	 * Gets the siac t periodo.
	 *
	 * @return the siac t periodo
	 */
	public SiacTPeriodo getSiacTPeriodo() {
		return this.siacTPeriodo;
	}

	/**
	 * Sets the siac t periodo.
	 *
	 * @param siacTPeriodo the new siac t periodo
	 */
	public void setSiacTPeriodo(SiacTPeriodo siacTPeriodo) {
		this.siacTPeriodo = siacTPeriodo;
	}

	/**
	 * Gets the siac t bil elems.
	 *
	 * @return the siac t bil elems
	 */
	public List<SiacTBilElem> getSiacTBilElems() {
		return this.siacTBilElems;
	}

	/**
	 * Sets the siac t bil elems.
	 *
	 * @param siacTBilElems the new siac t bil elems
	 */
	public void setSiacTBilElems(List<SiacTBilElem> siacTBilElems) {
		this.siacTBilElems = siacTBilElems;
	}

	/**
	 * Adds the siac t bil elem.
	 *
	 * @param siacTBilElem the siac t bil elem
	 * @return the siac t bil elem
	 */
	public SiacTBilElem addSiacTBilElem(SiacTBilElem siacTBilElem) {
		getSiacTBilElems().add(siacTBilElem);
		siacTBilElem.setSiacTBil(this);

		return siacTBilElem;
	}

	/**
	 * Removes the siac t bil elem.
	 *
	 * @param siacTBilElem the siac t bil elem
	 * @return the siac t bil elem
	 */
	public SiacTBilElem removeSiacTBilElem(SiacTBilElem siacTBilElem) {
		getSiacTBilElems().remove(siacTBilElem);
		siacTBilElem.setSiacTBil(null);

		return siacTBilElem;
	}

	public List<SiacTCartacont> getSiacTCartaconts() {
		return this.siacTCartaconts;
	}

	public void setSiacTCartaconts(List<SiacTCartacont> siacTCartaconts) {
		this.siacTCartaconts = siacTCartaconts;
	}

	public SiacTCartacont addSiacTCartacont(SiacTCartacont siacTCartacont) {
		getSiacTCartaconts().add(siacTCartacont);
		siacTCartacont.setSiacTBil(this);

		return siacTCartacont;
	}

	public SiacTCartacont removeSiacTCartacont(SiacTCartacont siacTCartacont) {
		getSiacTCartaconts().remove(siacTCartacont);
		siacTCartacont.setSiacTBil(null);

		return siacTCartacont;
	}

	public List<SiacTCassaEconStanz> getSiacTCassaEconStanzs() {
		return this.siacTCassaEconStanzs;
	}

	public void setSiacTCassaEconStanzs(List<SiacTCassaEconStanz> siacTCassaEconStanzs) {
		this.siacTCassaEconStanzs = siacTCassaEconStanzs;
	}

	public SiacTCassaEconStanz addSiacTCassaEconStanz(SiacTCassaEconStanz siacTCassaEconStanz) {
		getSiacTCassaEconStanzs().add(siacTCassaEconStanz);
		siacTCassaEconStanz.setSiacTBil(this);

		return siacTCassaEconStanz;
	}

	public SiacTCassaEconStanz removeSiacTCassaEconStanz(SiacTCassaEconStanz siacTCassaEconStanz) {
		getSiacTCassaEconStanzs().remove(siacTCassaEconStanz);
		siacTCassaEconStanz.setSiacTBil(null);

		return siacTCassaEconStanz;
	}

	public List<SiacTCronop> getSiacTCronops() {
		return this.siacTCronops;
	}

	public void setSiacTCronops(List<SiacTCronop> siacTCronops) {
		this.siacTCronops = siacTCronops;
	}

	public SiacTCronop addSiacTCronop(SiacTCronop siacTCronop) {
		getSiacTCronops().add(siacTCronop);
		siacTCronop.setSiacTBil(this);

		return siacTCronop;
	}

	public SiacTCronop removeSiacTCronop(SiacTCronop siacTCronop) {
		getSiacTCronops().remove(siacTCronop);
		siacTCronop.setSiacTBil(null);

		return siacTCronop;
	}

	public List<SiacTElencoDocNum> getSiacTElencoDocNums() {
		return this.siacTElencoDocNums;
	}

	public void setSiacTElencoDocNums(List<SiacTElencoDocNum> siacTElencoDocNums) {
		this.siacTElencoDocNums = siacTElencoDocNums;
	}

	public SiacTElencoDocNum addSiacTElencoDocNum(SiacTElencoDocNum siacTElencoDocNum) {
		getSiacTElencoDocNums().add(siacTElencoDocNum);
		siacTElencoDocNum.setSiacTBil(this);

		return siacTElencoDocNum;
	}

	public SiacTElencoDocNum removeSiacTElencoDocNum(SiacTElencoDocNum siacTElencoDocNum) {
		getSiacTElencoDocNums().remove(siacTElencoDocNum);
		siacTElencoDocNum.setSiacTBil(null);

		return siacTElencoDocNum;
	}

	public List<SiacTLiquidazione> getSiacTLiquidaziones() {
		return this.siacTLiquidaziones;
	}

	public void setSiacTLiquidaziones(List<SiacTLiquidazione> siacTLiquidaziones) {
		this.siacTLiquidaziones = siacTLiquidaziones;
	}

	public SiacTLiquidazione addSiacTLiquidazione(SiacTLiquidazione siacTLiquidazione) {
		getSiacTLiquidaziones().add(siacTLiquidazione);
		siacTLiquidazione.setSiacTBil(this);

		return siacTLiquidazione;
	}

	public SiacTLiquidazione removeSiacTLiquidazione(SiacTLiquidazione siacTLiquidazione) {
		getSiacTLiquidaziones().remove(siacTLiquidazione);
		siacTLiquidazione.setSiacTBil(null);

		return siacTLiquidazione;
	}

	public List<SiacTMovgest> getSiacTMovgests() {
		return this.siacTMovgests;
	}

	public void setSiacTMovgests(List<SiacTMovgest> siacTMovgests) {
		this.siacTMovgests = siacTMovgests;
	}

	public SiacTMovgest addSiacTMovgest(SiacTMovgest siacTMovgest) {
		getSiacTMovgests().add(siacTMovgest);
		siacTMovgest.setSiacTBil(this);

		return siacTMovgest;
	}

	public SiacTMovgest removeSiacTMovgest(SiacTMovgest siacTMovgest) {
		getSiacTMovgests().remove(siacTMovgest);
		siacTMovgest.setSiacTBil(null);

		return siacTMovgest;
	}

	public List<SiacTOrdinativo> getSiacTOrdinativos() {
		return this.siacTOrdinativos;
	}

	public void setSiacTOrdinativos(List<SiacTOrdinativo> siacTOrdinativos) {
		this.siacTOrdinativos = siacTOrdinativos;
	}

	public SiacTOrdinativo addSiacTOrdinativo(SiacTOrdinativo siacTOrdinativo) {
		getSiacTOrdinativos().add(siacTOrdinativo);
		siacTOrdinativo.setSiacTBil(this);

		return siacTOrdinativo;
	}

	public SiacTOrdinativo removeSiacTOrdinativo(SiacTOrdinativo siacTOrdinativo) {
		getSiacTOrdinativos().remove(siacTOrdinativo);
		siacTOrdinativo.setSiacTBil(null);

		return siacTOrdinativo;
	}

	public List<SiacTReportImporti> getSiacTReportImportis() {
		return this.siacTReportImportis;
	}

	public void setSiacTReportImportis(List<SiacTReportImporti> siacTReportImportis) {
		this.siacTReportImportis = siacTReportImportis;
	}

	public SiacTReportImporti addSiacTReportImporti(SiacTReportImporti siacTReportImporti) {
		getSiacTReportImportis().add(siacTReportImporti);
		siacTReportImporti.setSiacTBil(this);

		return siacTReportImporti;
	}

	public SiacTReportImporti removeSiacTReportImporti(SiacTReportImporti siacTReportImporti) {
		getSiacTReportImportis().remove(siacTReportImporti);
		siacTReportImporti.setSiacTBil(null);

		return siacTReportImporti;
	}
	
	public List<SiacTPrimaNota> getSiacTPrimaNotas() {
		return this.siacTPrimaNotas;
	}

	public void setSiacTPrimaNotas(List<SiacTPrimaNota> siacTPrimaNotas) {
		this.siacTPrimaNotas = siacTPrimaNotas;
	}

	public SiacTPrimaNota addSiacTPrimaNota(SiacTPrimaNota siacTPrimaNota) {
		getSiacTPrimaNotas().add(siacTPrimaNota);
		siacTPrimaNota.setSiacTBil(this);

		return siacTPrimaNota;
	}

	public SiacTPrimaNota removeSiacTPrimaNota(SiacTPrimaNota siacTPrimaNota) {
		getSiacTPrimaNotas().remove(siacTPrimaNota);
		siacTPrimaNota.setSiacTBil(null);

		return siacTPrimaNota;
	}

	public List<SiacTRegMovfin> getSiacTRegMovfins() {
		return this.siacTRegMovfins;
	}

	public void setSiacTRegMovfins(List<SiacTRegMovfin> siacTRegMovfins) {
		this.siacTRegMovfins = siacTRegMovfins;
	}

	public SiacTRegMovfin addSiacTRegMovfin(SiacTRegMovfin siacTRegMovfin) {
		getSiacTRegMovfins().add(siacTRegMovfin);
		siacTRegMovfin.setSiacTBil(this);

		return siacTRegMovfin;
	}

	public SiacTRegMovfin removeSiacTRegMovfin(SiacTRegMovfin siacTRegMovfin) {
		getSiacTRegMovfins().remove(siacTRegMovfin);
		siacTRegMovfin.setSiacTBil(null);

		return siacTRegMovfin;
	}

	public List<SiacTRichiestaEcon> getSiacTRichiestaEcons() {
		return this.siacTRichiestaEcons;
	}

	public void setSiacTRichiestaEcons(List<SiacTRichiestaEcon> siacTRichiestaEcons) {
		this.siacTRichiestaEcons = siacTRichiestaEcons;
	}

	public SiacTRichiestaEcon addSiacTRichiestaEcon(SiacTRichiestaEcon siacTRichiestaEcon) {
		getSiacTRichiestaEcons().add(siacTRichiestaEcon);
		siacTRichiestaEcon.setSiacTBil(this);

		return siacTRichiestaEcon;
	}

	public SiacTRichiestaEcon removeSiacTRichiestaEcon(SiacTRichiestaEcon siacTRichiestaEcon) {
		getSiacTRichiestaEcons().remove(siacTRichiestaEcon);
		siacTRichiestaEcon.setSiacTBil(null);

		return siacTRichiestaEcon;
	}

	public List<SiacTVariazione> getSiacTVariaziones() {
		return this.siacTVariaziones;
	}

	public void setSiacTVariaziones(List<SiacTVariazione> siacTVariaziones) {
		this.siacTVariaziones = siacTVariaziones;
	}

	public SiacTVariazione addSiacTVariazione(SiacTVariazione siacTVariazione) {
		getSiacTVariaziones().add(siacTVariazione);
		siacTVariazione.setSiacTBil(this);

		return siacTVariazione;
	}

	public SiacTVariazione removeSiacTVariazione(SiacTVariazione siacTVariazione) {
		getSiacTVariaziones().remove(siacTVariazione);
		siacTVariazione.setSiacTBil(null);

		return siacTVariazione;
	}

	public List<SiacTVariazioneNum> getSiacTVariazioneNums() {
		return this.siacTVariazioneNums;
	}

	public void setSiacTVariazioneNums(List<SiacTVariazioneNum> siacTVariazioneNums) {
		this.siacTVariazioneNums = siacTVariazioneNums;
	}

	public SiacTVariazioneNum addSiacTVariazioneNum(SiacTVariazioneNum siacTVariazioneNum) {
		getSiacTVariazioneNums().add(siacTVariazioneNum);
		siacTVariazioneNum.setSiacTBil(this);

		return siacTVariazioneNum;
	}

	public SiacTVariazioneNum removeSiacTVariazioneNum(SiacTVariazioneNum siacTVariazioneNum) {
		getSiacTVariazioneNums().remove(siacTVariazioneNum);
		siacTVariazioneNum.setSiacTBil(null);

		return siacTVariazioneNum;
	}

	public List<SiacRCassaEconBil> getSiacRCassaEconBils() {
		return this.siacRCassaEconBils;
	}

	public void setSiacRCassaEconBils(List<SiacRCassaEconBil> siacRCassaEconBils) {
		this.siacRCassaEconBils = siacRCassaEconBils;
	}

	public SiacRCassaEconBil addSiacRCassaEconBil(SiacRCassaEconBil siacRCassaEconBil) {
		getSiacRCassaEconBils().add(siacRCassaEconBil);
		siacRCassaEconBil.setSiacTBil(this);

		return siacRCassaEconBil;
	}

	public SiacRCassaEconBil removeSiacRCassaEconBil(SiacRCassaEconBil siacRCassaEconBil) {
		getSiacRCassaEconBils().remove(siacRCassaEconBil);
		siacRCassaEconBil.setSiacTBil(null);

		return siacRCassaEconBil;
	}
	
	public List<SiacRBilAttr> getSiacRBilAttrs() {
		return this.siacRBilAttrs;
	}

	public void setSiacRBilAttrs(List<SiacRBilAttr> siacRBilAttrs) {
		this.siacRBilAttrs = siacRBilAttrs;
	}

	public SiacRBilAttr addSiacRBilAttr(SiacRBilAttr siacRBilAttr) {
		getSiacRBilAttrs().add(siacRBilAttr);
		siacRBilAttr.setSiacTBil(this);

		return siacRBilAttr;
	}

	public SiacRBilAttr removeSiacRBilAttr(SiacRBilAttr siacRBilAttr) {
		getSiacRBilAttrs().remove(siacRBilAttr);
		siacRBilAttr.setSiacTBil(null);

		return siacRBilAttr;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return bilId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.bilId = uid;
		
	}

}