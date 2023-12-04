/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.sql.Timestamp;
import java.util.List;

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

import it.csi.siac.siacbilser.integration.entity.SiacRSubdocMovgestT;
import it.csi.siac.siacfinser.integration.entity.base.SiacLoginMultiplo;


/**
 * The persistent class for the siac_t_movgest_ts database table.
 * 
 */
@Entity
@Table(name="siac_t_movgest_ts")
public class SiacTMovgestTsFin extends SiacLoginMultiplo {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_MOVGEST_TS_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_movgest_ts_movgest_ts_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_MOVGEST_TS_ID_GENERATOR")
	@Column(name="movgest_ts_id")
	private Integer movgestTsId;

	private Integer livello;

	@Column(name="movgest_ts_code")
	private String movgestTsCode;

	@Column(name="movgest_ts_desc")
	private String movgestTsDesc;

	@Column(name="movgest_ts_id_padre")
	private Integer movgestTsIdPadre;

	@Column(name="movgest_ts_scadenza_data")
	private Timestamp movgestTsScadenzaData;
	
	private String ordine;

	//bi-directional many-to-one association to SiacRMovgestClassFin
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacRMovgestClassFin> siacRMovgestClasses;

	//bi-directional many-to-one association to SiacRMovgestTsAttrFin
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacRMovgestTsAttrFin> siacRMovgestTsAttrs;

	//bi-directional many-to-one association to SiacRMovgestTsProgrammaFin
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacRMovgestTsProgrammaFin> siacRMovgestTsProgrammas;

	//bi-directional many-to-one association to SiacRMovgestTsSogFin
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacRMovgestTsSogFin> siacRMovgestTsSogs;

	//bi-directional many-to-one association to SiacRMovgestTsSogModFin
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacRMovgestTsSogModFin> siacRMovgestTsSogMods;

	//bi-directional many-to-one association to SiacRMovgestTsSogclasseFin
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacRMovgestTsSogclasseFin> siacRMovgestTsSogclasses;

	
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacRMovgestTsCronopElemFin> siacRMovgestTsCronopElems;
	
	
	//bi-directional many-to-one association to SiacRMovgestTsSogclasseModFin
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacRMovgestTsSogclasseModFin> siacRMovgestTsSogclasseMods;

	//bi-directional many-to-one association to SiacDMovgestTsTipoFin
	@ManyToOne
	@JoinColumn(name="movgest_ts_tipo_id")
	private SiacDMovgestTsTipoFin siacDMovgestTsTipo;

	//bi-directional many-to-one association to SiacTMovgestFin
	@ManyToOne
	@JoinColumn(name="movgest_id")
	private SiacTMovgestFin siacTMovgest;

	//bi-directional many-to-one association to SiacTMovgestTsDetFin
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacTMovgestTsDetFin> siacTMovgestTsDets;

	//bi-directional many-to-one association to SiacTMovgestTsDetModFin
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacTMovgestTsDetModFin> siacTMovgestTsDetMods;

	//bi-directional many-to-one association to SiacRMovgestTsStatoFin
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacRMovgestTsStatoFin> siacRMovgestTsStatos;

	//bi-directional many-to-one association to SiacRMovgestTsAttoAmmFin
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacRMovgestTsAttoAmmFin> siacRMovgestTsAttoAmms;
	

	//bi-directional many-to-one association to SiacRCartacontDetMovgestTFin
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacRCartacontDetMovgestTFin> siacRCartacontDetMovgestTs;
		
	// 02/07/2914 : inizio
	//bi-directional many-to-one association to SiacRMovgestClassFin
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacROrdinativoTsMovgestTFin> siacROrdinativoTsMovgestTs;
	
	
	//ottobre 2017 SIOPE PLUS:
	
	//bi-directional many-to-one association to SiacDSiopeAssenzaMotivazioneFin
	@ManyToOne
	@JoinColumn(name="siope_assenza_motivazione_id")
	private SiacDSiopeAssenzaMotivazioneFin siacDSiopeAssenzaMotivazione;
	
	//bi-directional many-to-one association to SiacDSiopeTipoDebitoFin
	@ManyToOne
	@JoinColumn(name="siope_tipo_debito_id")
	private SiacDSiopeTipoDebitoFin siacDSiopeTipoDebitoFin;
	
	
	//bi-directional many-to-one associationsiacTMovgestT to SiacRMovgestTsSogclasseModFin
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacRMovgestTsCronopElemFin> siacRMovgestTsCronopElemFins;
	
	//SIAC-6997
	//bi-directional many-to-one association to SiacRSubdocMovgestT
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacRSubdocMovgestTFin> siacRSubdocMovgestTs;
	
	//bi-directional many-to-one association to SiacRMovgestT
	@OneToMany(mappedBy="siacTMovgestTsB")
	private List<SiacRMovgestTsFin> siacRMovgestTsB;

	//bi-directional many-to-one association to SiacRMovgestT
	@OneToMany(mappedBy="siacTMovgestTsA")
	private List<SiacRMovgestTsFin> siacRMovgestTsA;
	
	/**
	 * @return the siacRMovgestTsCronopElemFins
	 */
	public List<SiacRMovgestTsCronopElemFin> getSiacRMovgestTsCronopElemFins() {
		return siacRMovgestTsCronopElemFins;
	}

	/**
	 * @param siacRMovgestTsCronopElemFins the siacRMovgestTsCronopElemFins to set
	 */
	public void setSiacRMovgestTsCronopElemFins(List<SiacRMovgestTsCronopElemFin> siacRMovgestTsCronopElemFins) {
		this.siacRMovgestTsCronopElemFins = siacRMovgestTsCronopElemFins;
	}

	public List<SiacROrdinativoTsMovgestTFin> getSiacROrdinativoTsMovgestTs() {
		return siacROrdinativoTsMovgestTs;
	}

	public void setSiacROrdinativoTsMovgestTs(
			List<SiacROrdinativoTsMovgestTFin> siacROrdinativoTsMovgestTs) {
		this.siacROrdinativoTsMovgestTs = siacROrdinativoTsMovgestTs;
	}
	
	//bi-directional many-to-one association to SiacRLiquidazioneMovgestFin
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacRLiquidazioneMovgestFin> siacRLiquidazioneMovgests;

	public List<SiacRLiquidazioneMovgestFin> getSiacRLiquidazioneMovgests() {
		return this.siacRLiquidazioneMovgests;
	}

	public void setSiacRLiquidazioneMovgests(List<SiacRLiquidazioneMovgestFin> siacRLiquidazioneMovgests) {
		this.siacRLiquidazioneMovgests = siacRLiquidazioneMovgests;
	}

	public SiacRLiquidazioneMovgestFin addSiacRLiquidazioneMovgest(SiacRLiquidazioneMovgestFin siacRLiquidazioneMovgest) {
		getSiacRLiquidazioneMovgests().add(siacRLiquidazioneMovgest);
		siacRLiquidazioneMovgest.setSiacTMovgestTs(this);

		return siacRLiquidazioneMovgest;
	}

	public SiacRLiquidazioneMovgestFin removeSiacRLiquidazioneMovgest(SiacRLiquidazioneMovgestFin siacRLiquidazioneMovgest) {
		getSiacRLiquidazioneMovgests().remove(siacRLiquidazioneMovgest);
		siacRLiquidazioneMovgest.setSiacTMovgestTs(null);

		return siacRLiquidazioneMovgest;
	}
	// 02/07/2914 : fine
	
	
	public SiacTMovgestTsFin() {
	}

	public Integer getMovgestTsId() {
		return this.movgestTsId;
	}

	public void setMovgestTsId(Integer movgestTsId) {
		this.movgestTsId = movgestTsId;
	}

	public Integer getLivello() {
		return this.livello;
	}

	public void setLivello(Integer livello) {
		this.livello = livello;
	}

	public String getMovgestTsCode() {
		return this.movgestTsCode;
	}

	public void setMovgestTsCode(String movgestTsCode) {
		this.movgestTsCode = movgestTsCode;
	}

	public String getMovgestTsDesc() {
		return this.movgestTsDesc;
	}

	public void setMovgestTsDesc(String movgestTsDesc) {
		this.movgestTsDesc = movgestTsDesc;
	}

	public Integer getMovgestTsIdPadre() {
		return this.movgestTsIdPadre;
	}

	public void setMovgestTsIdPadre(Integer movgestTsIdPadre) {
		this.movgestTsIdPadre = movgestTsIdPadre;
	}

	public Timestamp getMovgestTsScadenzaData() {
		return this.movgestTsScadenzaData;
	}

	public void setMovgestTsScadenzaData(Timestamp movgestTsScadenzaData) {
		this.movgestTsScadenzaData = movgestTsScadenzaData;
	}

	public String getOrdine() {
		return this.ordine;
	}

	public void setOrdine(String ordine) {
		this.ordine = ordine;
	}

	public List<SiacRMovgestClassFin> getSiacRMovgestClasses() {
		return this.siacRMovgestClasses;
	}

	public void setSiacRMovgestClasses(List<SiacRMovgestClassFin> siacRMovgestClasses) {
		this.siacRMovgestClasses = siacRMovgestClasses;
	}

	public SiacRMovgestClassFin addSiacRMovgestClass(SiacRMovgestClassFin siacRMovgestClass) {
		getSiacRMovgestClasses().add(siacRMovgestClass);
		siacRMovgestClass.setSiacTMovgestT(this);

		return siacRMovgestClass;
	}

	public SiacRMovgestClassFin removeSiacRMovgestClass(SiacRMovgestClassFin siacRMovgestClass) {
		getSiacRMovgestClasses().remove(siacRMovgestClass);
		siacRMovgestClass.setSiacTMovgestT(null);

		return siacRMovgestClass;
	}

	public List<SiacRMovgestTsAttrFin> getSiacRMovgestTsAttrs() {
		return this.siacRMovgestTsAttrs;
	}

	public void setSiacRMovgestTsAttrs(List<SiacRMovgestTsAttrFin> siacRMovgestTsAttrs) {
		this.siacRMovgestTsAttrs = siacRMovgestTsAttrs;
	}

	public SiacRMovgestTsAttrFin addSiacRMovgestTsAttr(SiacRMovgestTsAttrFin siacRMovgestTsAttr) {
		getSiacRMovgestTsAttrs().add(siacRMovgestTsAttr);
		siacRMovgestTsAttr.setSiacTMovgestT(this);

		return siacRMovgestTsAttr;
	}

	public SiacRMovgestTsAttrFin removeSiacRMovgestTsAttr(SiacRMovgestTsAttrFin siacRMovgestTsAttr) {
		getSiacRMovgestTsAttrs().remove(siacRMovgestTsAttr);
		siacRMovgestTsAttr.setSiacTMovgestT(null);

		return siacRMovgestTsAttr;
	}

	public List<SiacRMovgestTsProgrammaFin> getSiacRMovgestTsProgrammas() {
		return this.siacRMovgestTsProgrammas;
	}

	public void setSiacRMovgestTsProgrammas(List<SiacRMovgestTsProgrammaFin> siacRMovgestTsProgrammas) {
		this.siacRMovgestTsProgrammas = siacRMovgestTsProgrammas;
	}

	public SiacRMovgestTsProgrammaFin addSiacRMovgestTsProgramma(SiacRMovgestTsProgrammaFin siacRMovgestTsProgramma) {
		getSiacRMovgestTsProgrammas().add(siacRMovgestTsProgramma);
		siacRMovgestTsProgramma.setSiacTMovgestT(this);

		return siacRMovgestTsProgramma;
	}

	public SiacRMovgestTsProgrammaFin removeSiacRMovgestTsProgramma(SiacRMovgestTsProgrammaFin siacRMovgestTsProgramma) {
		getSiacRMovgestTsProgrammas().remove(siacRMovgestTsProgramma);
		siacRMovgestTsProgramma.setSiacTMovgestT(null);

		return siacRMovgestTsProgramma;
	}

	public List<SiacRMovgestTsSogFin> getSiacRMovgestTsSogs() {
		return this.siacRMovgestTsSogs;
	}

	public void setSiacRMovgestTsSogs(List<SiacRMovgestTsSogFin> siacRMovgestTsSogs) {
		this.siacRMovgestTsSogs = siacRMovgestTsSogs;
	}

	public SiacRMovgestTsSogFin addSiacRMovgestTsSog(SiacRMovgestTsSogFin siacRMovgestTsSog) {
		getSiacRMovgestTsSogs().add(siacRMovgestTsSog);
		siacRMovgestTsSog.setSiacTMovgestT(this);

		return siacRMovgestTsSog;
	}

	public SiacRMovgestTsSogFin removeSiacRMovgestTsSog(SiacRMovgestTsSogFin siacRMovgestTsSog) {
		getSiacRMovgestTsSogs().remove(siacRMovgestTsSog);
		siacRMovgestTsSog.setSiacTMovgestT(null);

		return siacRMovgestTsSog;
	}

	public List<SiacRMovgestTsSogModFin> getSiacRMovgestTsSogMods() {
		return this.siacRMovgestTsSogMods;
	}

	public void setSiacRMovgestTsSogMods(List<SiacRMovgestTsSogModFin> siacRMovgestTsSogMods) {
		this.siacRMovgestTsSogMods = siacRMovgestTsSogMods;
	}

	public SiacRMovgestTsSogModFin addSiacRMovgestTsSogMod(SiacRMovgestTsSogModFin siacRMovgestTsSogMod) {
		getSiacRMovgestTsSogMods().add(siacRMovgestTsSogMod);
		siacRMovgestTsSogMod.setSiacTMovgestT(this);

		return siacRMovgestTsSogMod;
	}

	public SiacRMovgestTsSogModFin removeSiacRMovgestTsSogMod(SiacRMovgestTsSogModFin siacRMovgestTsSogMod) {
		getSiacRMovgestTsSogMods().remove(siacRMovgestTsSogMod);
		siacRMovgestTsSogMod.setSiacTMovgestT(null);

		return siacRMovgestTsSogMod;
	}

	public List<SiacRMovgestTsSogclasseFin> getSiacRMovgestTsSogclasses() {
		return this.siacRMovgestTsSogclasses;
	}

	public void setSiacRMovgestTsSogclasses(List<SiacRMovgestTsSogclasseFin> siacRMovgestTsSogclasses) {
		this.siacRMovgestTsSogclasses = siacRMovgestTsSogclasses;
	}

	public SiacRMovgestTsSogclasseFin addSiacRMovgestTsSogclass(SiacRMovgestTsSogclasseFin siacRMovgestTsSogclass) {
		getSiacRMovgestTsSogclasses().add(siacRMovgestTsSogclass);
		siacRMovgestTsSogclass.setSiacTMovgestT(this);

		return siacRMovgestTsSogclass;
	}

	public SiacRMovgestTsSogclasseFin removeSiacRMovgestTsSogclass(SiacRMovgestTsSogclasseFin siacRMovgestTsSogclass) {
		getSiacRMovgestTsSogclasses().remove(siacRMovgestTsSogclass);
		siacRMovgestTsSogclass.setSiacTMovgestT(null);

		return siacRMovgestTsSogclass;
	}

	public List<SiacRMovgestTsSogclasseModFin> getSiacRMovgestTsSogclasseMods() {
		return this.siacRMovgestTsSogclasseMods;
	}

	public void setSiacRMovgestTsSogclasseMods(List<SiacRMovgestTsSogclasseModFin> siacRMovgestTsSogclasseMods) {
		this.siacRMovgestTsSogclasseMods = siacRMovgestTsSogclasseMods;
	}

	public SiacRMovgestTsSogclasseModFin addSiacRMovgestTsSogclasseMod(SiacRMovgestTsSogclasseModFin siacRMovgestTsSogclasseMod) {
		getSiacRMovgestTsSogclasseMods().add(siacRMovgestTsSogclasseMod);
		siacRMovgestTsSogclasseMod.setSiacTMovgestT(this);

		return siacRMovgestTsSogclasseMod;
	}

	public SiacRMovgestTsSogclasseModFin removeSiacRMovgestTsSogclasseMod(SiacRMovgestTsSogclasseModFin siacRMovgestTsSogclasseMod) {
		getSiacRMovgestTsSogclasseMods().remove(siacRMovgestTsSogclasseMod);
		siacRMovgestTsSogclasseMod.setSiacTMovgestT(null);

		return siacRMovgestTsSogclasseMod;
	}

	public SiacDMovgestTsTipoFin getSiacDMovgestTsTipo() {
		return this.siacDMovgestTsTipo;
	}

	public void setSiacDMovgestTsTipo(SiacDMovgestTsTipoFin siacDMovgestTsTipo) {
		this.siacDMovgestTsTipo = siacDMovgestTsTipo;
	}

	public SiacTMovgestFin getSiacTMovgest() {
		return this.siacTMovgest;
	}

	public void setSiacTMovgest(SiacTMovgestFin siacTMovgest) {
		this.siacTMovgest = siacTMovgest;
	}

	public List<SiacTMovgestTsDetFin> getSiacTMovgestTsDets() {
		return this.siacTMovgestTsDets;
	}

	public void setSiacTMovgestTsDets(List<SiacTMovgestTsDetFin> siacTMovgestTsDets) {
		this.siacTMovgestTsDets = siacTMovgestTsDets;
	}

	public SiacTMovgestTsDetFin addSiacTMovgestTsDet(SiacTMovgestTsDetFin siacTMovgestTsDet) {
		getSiacTMovgestTsDets().add(siacTMovgestTsDet);
		siacTMovgestTsDet.setSiacTMovgestT(this);

		return siacTMovgestTsDet;
	}

	public SiacTMovgestTsDetFin removeSiacTMovgestTsDet(SiacTMovgestTsDetFin siacTMovgestTsDet) {
		getSiacTMovgestTsDets().remove(siacTMovgestTsDet);
		siacTMovgestTsDet.setSiacTMovgestT(null);

		return siacTMovgestTsDet;
	}

	public List<SiacTMovgestTsDetModFin> getSiacTMovgestTsDetMods() {
		return this.siacTMovgestTsDetMods;
	}

	public void setSiacTMovgestTsDetMods(List<SiacTMovgestTsDetModFin> siacTMovgestTsDetMods) {
		this.siacTMovgestTsDetMods = siacTMovgestTsDetMods;
	}

	public SiacTMovgestTsDetModFin addSiacTMovgestTsDetMod(SiacTMovgestTsDetModFin siacTMovgestTsDetMod) {
		getSiacTMovgestTsDetMods().add(siacTMovgestTsDetMod);
		siacTMovgestTsDetMod.setSiacTMovgestT(this);

		return siacTMovgestTsDetMod;
	}

	public SiacTMovgestTsDetModFin removeSiacTMovgestTsDetMod(SiacTMovgestTsDetModFin siacTMovgestTsDetMod) {
		getSiacTMovgestTsDetMods().remove(siacTMovgestTsDetMod);
		siacTMovgestTsDetMod.setSiacTMovgestT(null);

		return siacTMovgestTsDetMod;
	}

	public List<SiacRMovgestTsStatoFin> getSiacRMovgestTsStatos() {
		return this.siacRMovgestTsStatos;
	}

	public void setSiacRMovgestTsStatos(List<SiacRMovgestTsStatoFin> siacRMovgestTsStatos) {
		this.siacRMovgestTsStatos = siacRMovgestTsStatos;
	}

	public SiacRMovgestTsStatoFin addSiacRMovgestTsStato(SiacRMovgestTsStatoFin siacRMovgestTsStato) {
		getSiacRMovgestTsStatos().add(siacRMovgestTsStato);
		siacRMovgestTsStato.setSiacTMovgestT(this);

		return siacRMovgestTsStato;
	}

	public SiacRMovgestTsStatoFin removeSiacRMovgestTsStato(SiacRMovgestTsStatoFin siacRMovgestTsStato) {
		getSiacRMovgestTsStatos().remove(siacRMovgestTsStato);
		siacRMovgestTsStato.setSiacTMovgestT(null);

		return siacRMovgestTsStato;
	}

	public List<SiacRMovgestTsAttoAmmFin> getSiacRMovgestTsAttoAmms() {
		return this.siacRMovgestTsAttoAmms;
	}

	public void setSiacRMovgestTsAttoAmms(List<SiacRMovgestTsAttoAmmFin> siacRMovgestTsAttoAmms) {
		this.siacRMovgestTsAttoAmms = siacRMovgestTsAttoAmms;
	}

	public SiacRMovgestTsAttoAmmFin addSiacRMovgestTsAttoAmm(SiacRMovgestTsAttoAmmFin siacRMovgestTsAttoAmm) {
		getSiacRMovgestTsAttoAmms().add(siacRMovgestTsAttoAmm);
		siacRMovgestTsAttoAmm.setSiacTMovgestT(this);

		return siacRMovgestTsAttoAmm;
	}

	public SiacRMovgestTsAttoAmmFin removeSiacRMovgestTsAttoAmm(SiacRMovgestTsAttoAmmFin siacRMovgestTsAttoAmm) {
		getSiacRMovgestTsAttoAmms().remove(siacRMovgestTsAttoAmm);
		siacRMovgestTsAttoAmm.setSiacTMovgestT(null);

		return siacRMovgestTsAttoAmm;
	}
	
	

	public List<SiacRCartacontDetMovgestTFin> getSiacRCartacontDetMovgestTs() {
		return this.siacRCartacontDetMovgestTs;
	}

	public void setSiacRCartacontDetMovgestTs(List<SiacRCartacontDetMovgestTFin> siacRCartacontDetMovgestTs) {
		this.siacRCartacontDetMovgestTs = siacRCartacontDetMovgestTs;
	}

	public SiacRCartacontDetMovgestTFin addSiacRCartacontDetMovgestT(SiacRCartacontDetMovgestTFin siacRCartacontDetMovgestT) {
		getSiacRCartacontDetMovgestTs().add(siacRCartacontDetMovgestT);
		siacRCartacontDetMovgestT.setSiacTMovgestT(this);

		return siacRCartacontDetMovgestT;
	}

	public SiacRCartacontDetMovgestTFin removeSiacRCartacontDetMovgestT(SiacRCartacontDetMovgestTFin siacRCartacontDetMovgestT) {
		getSiacRCartacontDetMovgestTs().remove(siacRCartacontDetMovgestT);
		siacRCartacontDetMovgestT.setSiacTMovgestT(null);

		return siacRCartacontDetMovgestT;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.movgestTsId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.movgestTsId = uid;
	}

	public List<SiacRMovgestTsFin> getSiacRMovgestTsB() {
		return siacRMovgestTsB;
	}

	public void setSiacRMovgestTsB(List<SiacRMovgestTsFin> siacRMovgestTsB) {
		this.siacRMovgestTsB = siacRMovgestTsB;
	}

	public List<SiacRMovgestTsFin> getSiacRMovgestTsA() {
		return siacRMovgestTsA;
	}

	public void setSiacRMovgestTsA(List<SiacRMovgestTsFin> siacRMovgestTsA) {
		this.siacRMovgestTsA = siacRMovgestTsA;
	}

	public SiacDSiopeAssenzaMotivazioneFin getSiacDSiopeAssenzaMotivazione() {
		return siacDSiopeAssenzaMotivazione;
	}

	public void setSiacDSiopeAssenzaMotivazione(SiacDSiopeAssenzaMotivazioneFin siacDSiopeAssenzaMotivazione) {
		this.siacDSiopeAssenzaMotivazione = siacDSiopeAssenzaMotivazione;
	}

	public SiacDSiopeTipoDebitoFin getSiacDSiopeTipoDebitoFin() {
		return siacDSiopeTipoDebitoFin;
	}

	public void setSiacDSiopeTipoDebitoFin(SiacDSiopeTipoDebitoFin siacDSiopeTipoDebitoFin) {
		this.siacDSiopeTipoDebitoFin = siacDSiopeTipoDebitoFin;
	}

	public List<SiacRMovgestTsCronopElemFin> getSiacRMovgestTsCronopElems() {
		return siacRMovgestTsCronopElems;
	}

	public void setSiacRMovgestTsCronopElems(List<SiacRMovgestTsCronopElemFin> siacRMovgestTsCronopElems) {
		this.siacRMovgestTsCronopElems = siacRMovgestTsCronopElems;
	}

	/**
	 * @return the siacRSubdocMovgestTs
	 */
	public List<SiacRSubdocMovgestTFin> getSiacRSubdocMovgestTs() {
		return siacRSubdocMovgestTs;
	}

	/**
	 * @param siacRSubdocMovgestTs the siacRSubdocMovgestTs to set
	 */
	public void setSiacRSubdocMovgestTs(List<SiacRSubdocMovgestTFin> siacRSubdocMovgestTs) {
		this.siacRSubdocMovgestTs = siacRSubdocMovgestTs;
	}
	
}