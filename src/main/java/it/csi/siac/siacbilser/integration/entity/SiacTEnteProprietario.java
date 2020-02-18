/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siaccommonser.integration.entity.SiacTBase;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_ente_proprietario database table.
 * 
 */
@Entity
@Table(name="siac_t_ente_proprietario")
public class SiacTEnteProprietario extends SiacTBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ente proprietario id. */
	@Id
	@SequenceGenerator(name="SIAC_T_ENTE_PROPRIETARIO_ENTEPROPRIETARIOID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_ENTE_PROPRIETARIO_ENTE_PROPRIETARIO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_ENTE_PROPRIETARIO_ENTEPROPRIETARIOID_GENERATOR")
	@Column(name="ente_proprietario_id")
	private Integer enteProprietarioId;

	/** The codice fiscale. */
	@Column(name="codice_fiscale")
	private String codiceFiscale;

	/** The ente denominazione. */
	@Column(name="ente_denominazione")
	private String enteDenominazione;	
	
	
	//bi-directional many-to-one association to SiacDAmbito
	/** The siac d ambitos. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacDAmbito> siacDAmbitos;

	//bi-directional many-to-one association to SiacDAttrTipo
	/** The siac d attr tipos. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacDAttrTipo> siacDAttrTipos;

	//bi-directional many-to-one association to SiacDAzioneTipo
	/** The siac d azione tipos. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacDAzioneTipo> siacDAzioneTipos;

	//bi-directional many-to-one association to SiacDBilElemDetTipo
	/** The siac d bil elem det tipos. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacDBilElemDetTipo> siacDBilElemDetTipos;

	//bi-directional many-to-one association to SiacDBilElemStato
	/** The siac d bil elem statos. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacDBilElemStato> siacDBilElemStatos;

	//bi-directional many-to-one association to SiacDBilElemTipo
	/** The siac d bil elem tipos. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacDBilElemTipo> siacDBilElemTipos;

	//bi-directional many-to-one association to SiacDBilStatoOp
	/** The siac d bil stato ops. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacDBilStatoOp> siacDBilStatoOps;

	//bi-directional many-to-one association to SiacDBilTipo
	/** The siac d bil tipos. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacDBilTipo> siacDBilTipos;

	//bi-directional many-to-one association to SiacDClassFam
	/** The siac d class fams. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacDClassFam> siacDClassFams;

	//bi-directional many-to-one association to SiacDClassTipo
	/** The siac d class tipos. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacDClassTipo> siacDClassTipos;

	//bi-directional many-to-one association to SiacDFaseOperativa
	/** The siac d fase operativas. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacDFaseOperativa> siacDFaseOperativas;

	//bi-directional many-to-one association to SiacDGestioneLivello
	/** The siac d gestione livellos. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacDGestioneLivello> siacDGestioneLivellos;

	//bi-directional many-to-one association to SiacDGestioneTipo
	/** The siac d gestione tipos. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacDGestioneTipo> siacDGestioneTipos;

	//bi-directional many-to-one association to SiacDGruppoAzioni
	/** The siac d gruppo azionis. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacDGruppoAzioni> siacDGruppoAzionis;

	//bi-directional many-to-one association to SiacDPeriodoTipo
	/** The siac d periodo tipos. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacDPeriodoTipo> siacDPeriodoTipos;

	//bi-directional many-to-one association to SiacDRuolo
	/** The siac d ruolos. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacDRuolo> siacDRuolos;

	//bi-directional many-to-one association to SiacDRuoloOp
	/** The siac d ruolo ops. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacDRuoloOp> siacDRuoloOps;

	//bi-directional many-to-one association to SiacDSoggettoStato
	/** The siac d soggetto statos. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacDSoggettoStato> siacDSoggettoStatos;

	//bi-directional many-to-one association to SiacDVariazioneTipo
	/** The siac d variazione tipos. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacDVariazioneTipo> siacDVariazioneTipos;

	//bi-directional many-to-one association to SiacDVariazoneStato
	/** The siac d variazione statos. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacDVariazioneStato> siacDVariazioneStatos;

	//bi-directional many-to-one association to SiacRAccountRuoloOp
	/** The siac r account ruolo ops. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacRAccountRuoloOp> siacRAccountRuoloOps;

	//bi-directional many-to-one association to SiacRAttrBilElemTipo
	/** The siac r attr bil elem tipos. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacRAttrBilElemTipo> siacRAttrBilElemTipos;

	//bi-directional many-to-one association to SiacRAttrClassTipo
	/** The siac r attr class tipos. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacRAttrClassTipo> siacRAttrClassTipos;

	//bi-directional many-to-one association to SiacRBilElemAttoLegge
	/** The siac r bil elem atto legges. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacRBilElemAttoLegge> siacRBilElemAttoLegges;

	//bi-directional many-to-one association to SiacRBilElemAttr
	/** The siac r bil elem attrs. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacRBilElemAttr> siacRBilElemAttrs;

	//bi-directional many-to-one association to SiacRBilElemClass
	/** The siac r bil elem classes. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacRBilElemClass> siacRBilElemClasses;

	//bi-directional many-to-one association to SiacRBilElemClassVar
	/** The siac r bil elem class vars. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacRBilElemClassVar> siacRBilElemClassVars;

	//bi-directional many-to-one association to SiacRBilElemRelTempo
	/** The siac r bil elem rel tempos. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacRBilElemRelTempo> siacRBilElemRelTempos;

	//bi-directional many-to-one association to SiacRBilElemStato
	/** The siac r bil elem statos. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacRBilElemStato> siacRBilElemStatos;

	//bi-directional many-to-one association to SiacRBilElemTipoClassTip
	/** The siac r bil elem tipo class tips. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacRBilElemTipoClassTip> siacRBilElemTipoClassTips;

	//bi-directional many-to-one association to SiacRBilFaseOperativa
	/** The siac r bil fase operativas. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacRBilFaseOperativa> siacRBilFaseOperativas;

	//bi-directional many-to-one association to SiacRBilStatoOp
	/** The siac r bil stato ops. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacRBilStatoOp> siacRBilStatoOps;

	//bi-directional many-to-one association to SiacRBilTipoStatoOp
	/** The siac r bil tipo stato ops. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacRBilTipoStatoOp> siacRBilTipoStatoOps;

	//bi-directional many-to-one association to SiacRClass
	/** The siac r classes. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacRClass> siacRClasses;

	//bi-directional many-to-one association to SiacRClassAttr
	/** The siac r class attrs. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacRClassAttr> siacRClassAttrs;

	//bi-directional many-to-one association to SiacRClassFamTree
	/** The siac r class fam trees. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacRClassFamTree> siacRClassFamTrees;

	//bi-directional many-to-one association to SiacRFaseOperativaBilStato
	/** The siac r fase operativa bil statos. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacRFaseOperativaBilStato> siacRFaseOperativaBilStatos;

	//bi-directional many-to-one association to SiacRGestioneEnte
	/** The siac r gestione entes. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacRGestioneEnte> siacRGestioneEntes;

	//bi-directional many-to-one association to SiacRGruppoAccount
	/** The siac r gruppo accounts. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacRGruppoAccount> siacRGruppoAccounts;

	//bi-directional many-to-one association to SiacRGruppoRuoloOp
	/** The siac r gruppo ruolo ops. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacRGruppoRuoloOp> siacRGruppoRuoloOps;

	//bi-directional many-to-one association to SiacRRuoloOpAzione
	/** The siac r ruolo op aziones. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacRRuoloOpAzione> siacRRuoloOpAziones;
	
	//bi-directional many-to-one association to SiacRSoggettoEnteProprietario
	/** The siac r soggetto ente proprietarios. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacRSoggettoEnteProprietario> siacRSoggettoEnteProprietarios;

	//bi-directional many-to-one association to SiacRSoggettoRuolo
	/** The siac r soggetto ruolos. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacRSoggettoRuolo> siacRSoggettoRuolos;

	//bi-directional many-to-one association to SiacRSoggettoStato
	/** The siac r soggetto statos. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacRSoggettoStato> siacRSoggettoStatos;

	//bi-directional many-to-one association to SiacRVariazioneAttr
	/** The siac r variazione attrs. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacRVariazioneAttr> siacRVariazioneAttrs;

	//bi-directional many-to-one association to SiacRVariazioneStato
	/** The siac r variazione statos. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacRVariazioneStato> siacRVariazioneStatos;

	//bi-directional many-to-one association to SiacTAccount
	/** The siac t accounts. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacTAccount> siacTAccounts;

	//bi-directional many-to-one association to SiacTAttoAmm
	/** The siac t atto amms. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacTAttoAmm> siacTAttoAmms;

	//bi-directional many-to-one association to SiacTAttoLegge
	/** The siac t atto legges. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacTAttoLegge> siacTAttoLegges;

	//bi-directional many-to-one association to SiacTAttr
	/** The siac t attrs. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacTAttr> siacTAttrs;

	//bi-directional many-to-one association to SiacTAzione
	/** The siac t aziones. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacTAzione> siacTAziones;

	//bi-directional many-to-one association to SiacTAzioneRichiesta
	/** The siac t azione richiestas. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacTAzioneRichiesta> siacTAzioneRichiestas;

	//bi-directional many-to-one association to SiacTBil
	/** The siac t bils. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacTBil> siacTBils;

	//bi-directional many-to-one association to SiacTBilElem
	/** The siac t bil elems. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacTBilElem> siacTBilElems;

	//bi-directional many-to-one association to SiacTBilElemDet
	/** The siac t bil elem dets. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacTBilElemDet> siacTBilElemDets;

	//bi-directional many-to-one association to SiacTBilElemDetVar
	/** The siac t bil elem det vars. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacTBilElemDetVar> siacTBilElemDetVars;

	//bi-directional many-to-one association to SiacTClass
	/** The siac t classes. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacTClass> siacTClasses;

	//bi-directional many-to-one association to SiacTClassFamTree
	/** The siac t class fam trees. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacTClassFamTree> siacTClassFamTrees;

	//bi-directional many-to-one association to SiacTGruppo
	/** The siac t gruppos. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacTGruppo> siacTGruppos;

	//bi-directional many-to-one association to SiacTParametroAzioneRichiesta
	/** The siac t parametro azione richiestas. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacTParametroAzioneRichiesta> siacTParametroAzioneRichiestas;

	//bi-directional many-to-one association to SiacTPeriodo
	/** The siac t periodos. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacTPeriodo> siacTPeriodos;

	//bi-directional many-to-one association to SiacTSoggetto
	/** The siac t soggettos. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacTSoggetto> siacTSoggettos;

	//bi-directional many-to-one association to SiacTVariazione
	/** The siac t variaziones. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacTVariazione> siacTVariaziones;

	/**
	 * Instantiates a new siac t ente proprietario.
	 */
	public SiacTEnteProprietario() {
		super();
	}

	public SiacTEnteProprietario(Integer uid) {
		this();
		setEnteProprietarioId(uid);
	}

	/**
	 * Gets the ente proprietario id.
	 *
	 * @return the ente proprietario id
	 */
	public Integer getEnteProprietarioId() {
		return this.enteProprietarioId;
	}

	/**
	 * Sets the ente proprietario id.
	 *
	 * @param enteProprietarioId the new ente proprietario id
	 */
	public void setEnteProprietarioId(Integer enteProprietarioId) {
		this.enteProprietarioId = enteProprietarioId;
	}

	/**
	 * Gets the codice fiscale.
	 *
	 * @return the codice fiscale
	 */
	public String getCodiceFiscale() {
		return this.codiceFiscale;
	}

	/**
	 * Sets the codice fiscale.
	 *
	 * @param codiceFiscale the new codice fiscale
	 */
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	

	/**
	 * Gets the ente denominazione.
	 *
	 * @return the ente denominazione
	 */
	public String getEnteDenominazione() {
		return this.enteDenominazione;
	}

	/**
	 * Sets the ente denominazione.
	 *
	 * @param enteDenominazione the new ente denominazione
	 */
	public void setEnteDenominazione(String enteDenominazione) {
		this.enteDenominazione = enteDenominazione;
	}

	

	/**
	 * Gets the siac d ambitos.
	 *
	 * @return the siac d ambitos
	 */
	public List<SiacDAmbito> getSiacDAmbitos() {
		return this.siacDAmbitos;
	}

	/**
	 * Sets the siac d ambitos.
	 *
	 * @param siacDAmbitos the new siac d ambitos
	 */
	public void setSiacDAmbitos(List<SiacDAmbito> siacDAmbitos) {
		this.siacDAmbitos = siacDAmbitos;
	}

	/**
	 * Adds the siac d ambito.
	 *
	 * @param siacDAmbito the siac d ambito
	 * @return the siac d ambito
	 */
	public SiacDAmbito addSiacDAmbito(SiacDAmbito siacDAmbito) {
		getSiacDAmbitos().add(siacDAmbito);
		siacDAmbito.setSiacTEnteProprietario(this);

		return siacDAmbito;
	}

	/**
	 * Removes the siac d ambito.
	 *
	 * @param siacDAmbito the siac d ambito
	 * @return the siac d ambito
	 */
	public SiacDAmbito removeSiacDAmbito(SiacDAmbito siacDAmbito) {
		getSiacDAmbitos().remove(siacDAmbito);
		siacDAmbito.setSiacTEnteProprietario(null);

		return siacDAmbito;
	}

	/**
	 * Gets the siac d attr tipos.
	 *
	 * @return the siac d attr tipos
	 */
	public List<SiacDAttrTipo> getSiacDAttrTipos() {
		return this.siacDAttrTipos;
	}

	/**
	 * Sets the siac d attr tipos.
	 *
	 * @param siacDAttrTipos the new siac d attr tipos
	 */
	public void setSiacDAttrTipos(List<SiacDAttrTipo> siacDAttrTipos) {
		this.siacDAttrTipos = siacDAttrTipos;
	}

	/**
	 * Adds the siac d attr tipo.
	 *
	 * @param siacDAttrTipo the siac d attr tipo
	 * @return the siac d attr tipo
	 */
	public SiacDAttrTipo addSiacDAttrTipo(SiacDAttrTipo siacDAttrTipo) {
		getSiacDAttrTipos().add(siacDAttrTipo);
		siacDAttrTipo.setSiacTEnteProprietario(this);

		return siacDAttrTipo;
	}

	/**
	 * Removes the siac d attr tipo.
	 *
	 * @param siacDAttrTipo the siac d attr tipo
	 * @return the siac d attr tipo
	 */
	public SiacDAttrTipo removeSiacDAttrTipo(SiacDAttrTipo siacDAttrTipo) {
		getSiacDAttrTipos().remove(siacDAttrTipo);
		siacDAttrTipo.setSiacTEnteProprietario(null);

		return siacDAttrTipo;
	}

	/**
	 * Gets the siac d azione tipos.
	 *
	 * @return the siac d azione tipos
	 */
	public List<SiacDAzioneTipo> getSiacDAzioneTipos() {
		return this.siacDAzioneTipos;
	}

	/**
	 * Sets the siac d azione tipos.
	 *
	 * @param siacDAzioneTipos the new siac d azione tipos
	 */
	public void setSiacDAzioneTipos(List<SiacDAzioneTipo> siacDAzioneTipos) {
		this.siacDAzioneTipos = siacDAzioneTipos;
	}

	/**
	 * Adds the siac d azione tipo.
	 *
	 * @param siacDAzioneTipo the siac d azione tipo
	 * @return the siac d azione tipo
	 */
	public SiacDAzioneTipo addSiacDAzioneTipo(SiacDAzioneTipo siacDAzioneTipo) {
		getSiacDAzioneTipos().add(siacDAzioneTipo);
		siacDAzioneTipo.setSiacTEnteProprietario(this);

		return siacDAzioneTipo;
	}

	/**
	 * Removes the siac d azione tipo.
	 *
	 * @param siacDAzioneTipo the siac d azione tipo
	 * @return the siac d azione tipo
	 */
	public SiacDAzioneTipo removeSiacDAzioneTipo(SiacDAzioneTipo siacDAzioneTipo) {
		getSiacDAzioneTipos().remove(siacDAzioneTipo);
		siacDAzioneTipo.setSiacTEnteProprietario(null);

		return siacDAzioneTipo;
	}

	/**
	 * Gets the siac d bil elem det tipos.
	 *
	 * @return the siac d bil elem det tipos
	 */
	public List<SiacDBilElemDetTipo> getSiacDBilElemDetTipos() {
		return this.siacDBilElemDetTipos;
	}

	/**
	 * Sets the siac d bil elem det tipos.
	 *
	 * @param siacDBilElemDetTipos the new siac d bil elem det tipos
	 */
	public void setSiacDBilElemDetTipos(List<SiacDBilElemDetTipo> siacDBilElemDetTipos) {
		this.siacDBilElemDetTipos = siacDBilElemDetTipos;
	}

	/**
	 * Adds the siac d bil elem det tipo.
	 *
	 * @param siacDBilElemDetTipo the siac d bil elem det tipo
	 * @return the siac d bil elem det tipo
	 */
	public SiacDBilElemDetTipo addSiacDBilElemDetTipo(SiacDBilElemDetTipo siacDBilElemDetTipo) {
		getSiacDBilElemDetTipos().add(siacDBilElemDetTipo);
		siacDBilElemDetTipo.setSiacTEnteProprietario(this);

		return siacDBilElemDetTipo;
	}

	/**
	 * Removes the siac d bil elem det tipo.
	 *
	 * @param siacDBilElemDetTipo the siac d bil elem det tipo
	 * @return the siac d bil elem det tipo
	 */
	public SiacDBilElemDetTipo removeSiacDBilElemDetTipo(SiacDBilElemDetTipo siacDBilElemDetTipo) {
		getSiacDBilElemDetTipos().remove(siacDBilElemDetTipo);
		siacDBilElemDetTipo.setSiacTEnteProprietario(null);

		return siacDBilElemDetTipo;
	}

	/**
	 * Gets the siac d bil elem statos.
	 *
	 * @return the siac d bil elem statos
	 */
	public List<SiacDBilElemStato> getSiacDBilElemStatos() {
		return this.siacDBilElemStatos;
	}

	/**
	 * Sets the siac d bil elem statos.
	 *
	 * @param siacDBilElemStatos the new siac d bil elem statos
	 */
	public void setSiacDBilElemStatos(List<SiacDBilElemStato> siacDBilElemStatos) {
		this.siacDBilElemStatos = siacDBilElemStatos;
	}

	/**
	 * Adds the siac d bil elem stato.
	 *
	 * @param siacDBilElemStato the siac d bil elem stato
	 * @return the siac d bil elem stato
	 */
	public SiacDBilElemStato addSiacDBilElemStato(SiacDBilElemStato siacDBilElemStato) {
		getSiacDBilElemStatos().add(siacDBilElemStato);
		siacDBilElemStato.setSiacTEnteProprietario(this);

		return siacDBilElemStato;
	}

	/**
	 * Removes the siac d bil elem stato.
	 *
	 * @param siacDBilElemStato the siac d bil elem stato
	 * @return the siac d bil elem stato
	 */
	public SiacDBilElemStato removeSiacDBilElemStato(SiacDBilElemStato siacDBilElemStato) {
		getSiacDBilElemStatos().remove(siacDBilElemStato);
		siacDBilElemStato.setSiacTEnteProprietario(null);

		return siacDBilElemStato;
	}

	/**
	 * Gets the siac d bil elem tipos.
	 *
	 * @return the siac d bil elem tipos
	 */
	public List<SiacDBilElemTipo> getSiacDBilElemTipos() {
		return this.siacDBilElemTipos;
	}

	/**
	 * Sets the siac d bil elem tipos.
	 *
	 * @param siacDBilElemTipos the new siac d bil elem tipos
	 */
	public void setSiacDBilElemTipos(List<SiacDBilElemTipo> siacDBilElemTipos) {
		this.siacDBilElemTipos = siacDBilElemTipos;
	}

	/**
	 * Adds the siac d bil elem tipo.
	 *
	 * @param siacDBilElemTipo the siac d bil elem tipo
	 * @return the siac d bil elem tipo
	 */
	public SiacDBilElemTipo addSiacDBilElemTipo(SiacDBilElemTipo siacDBilElemTipo) {
		getSiacDBilElemTipos().add(siacDBilElemTipo);
		siacDBilElemTipo.setSiacTEnteProprietario(this);

		return siacDBilElemTipo;
	}

	/**
	 * Removes the siac d bil elem tipo.
	 *
	 * @param siacDBilElemTipo the siac d bil elem tipo
	 * @return the siac d bil elem tipo
	 */
	public SiacDBilElemTipo removeSiacDBilElemTipo(SiacDBilElemTipo siacDBilElemTipo) {
		getSiacDBilElemTipos().remove(siacDBilElemTipo);
		siacDBilElemTipo.setSiacTEnteProprietario(null);

		return siacDBilElemTipo;
	}

	/**
	 * Gets the siac d bil stato ops.
	 *
	 * @return the siac d bil stato ops
	 */
	public List<SiacDBilStatoOp> getSiacDBilStatoOps() {
		return this.siacDBilStatoOps;
	}

	/**
	 * Sets the siac d bil stato ops.
	 *
	 * @param siacDBilStatoOps the new siac d bil stato ops
	 */
	public void setSiacDBilStatoOps(List<SiacDBilStatoOp> siacDBilStatoOps) {
		this.siacDBilStatoOps = siacDBilStatoOps;
	}

	/**
	 * Adds the siac d bil stato op.
	 *
	 * @param siacDBilStatoOp the siac d bil stato op
	 * @return the siac d bil stato op
	 */
	public SiacDBilStatoOp addSiacDBilStatoOp(SiacDBilStatoOp siacDBilStatoOp) {
		getSiacDBilStatoOps().add(siacDBilStatoOp);
		siacDBilStatoOp.setSiacTEnteProprietario(this);

		return siacDBilStatoOp;
	}

	/**
	 * Removes the siac d bil stato op.
	 *
	 * @param siacDBilStatoOp the siac d bil stato op
	 * @return the siac d bil stato op
	 */
	public SiacDBilStatoOp removeSiacDBilStatoOp(SiacDBilStatoOp siacDBilStatoOp) {
		getSiacDBilStatoOps().remove(siacDBilStatoOp);
		siacDBilStatoOp.setSiacTEnteProprietario(null);

		return siacDBilStatoOp;
	}

	/**
	 * Gets the siac d bil tipos.
	 *
	 * @return the siac d bil tipos
	 */
	public List<SiacDBilTipo> getSiacDBilTipos() {
		return this.siacDBilTipos;
	}

	/**
	 * Sets the siac d bil tipos.
	 *
	 * @param siacDBilTipos the new siac d bil tipos
	 */
	public void setSiacDBilTipos(List<SiacDBilTipo> siacDBilTipos) {
		this.siacDBilTipos = siacDBilTipos;
	}

	/**
	 * Adds the siac d bil tipo.
	 *
	 * @param siacDBilTipo the siac d bil tipo
	 * @return the siac d bil tipo
	 */
	public SiacDBilTipo addSiacDBilTipo(SiacDBilTipo siacDBilTipo) {
		getSiacDBilTipos().add(siacDBilTipo);
		siacDBilTipo.setSiacTEnteProprietario(this);

		return siacDBilTipo;
	}

	/**
	 * Removes the siac d bil tipo.
	 *
	 * @param siacDBilTipo the siac d bil tipo
	 * @return the siac d bil tipo
	 */
	public SiacDBilTipo removeSiacDBilTipo(SiacDBilTipo siacDBilTipo) {
		getSiacDBilTipos().remove(siacDBilTipo);
		siacDBilTipo.setSiacTEnteProprietario(null);

		return siacDBilTipo;
	}

	/**
	 * Gets the siac d class fams.
	 *
	 * @return the siac d class fams
	 */
	public List<SiacDClassFam> getSiacDClassFams() {
		return this.siacDClassFams;
	}

	/**
	 * Sets the siac d class fams.
	 *
	 * @param siacDClassFams the new siac d class fams
	 */
	public void setSiacDClassFams(List<SiacDClassFam> siacDClassFams) {
		this.siacDClassFams = siacDClassFams;
	}

	/**
	 * Adds the siac d class fam.
	 *
	 * @param siacDClassFam the siac d class fam
	 * @return the siac d class fam
	 */
	public SiacDClassFam addSiacDClassFam(SiacDClassFam siacDClassFam) {
		getSiacDClassFams().add(siacDClassFam);
		siacDClassFam.setSiacTEnteProprietario(this);

		return siacDClassFam;
	}

	/**
	 * Removes the siac d class fam.
	 *
	 * @param siacDClassFam the siac d class fam
	 * @return the siac d class fam
	 */
	public SiacDClassFam removeSiacDClassFam(SiacDClassFam siacDClassFam) {
		getSiacDClassFams().remove(siacDClassFam);
		siacDClassFam.setSiacTEnteProprietario(null);

		return siacDClassFam;
	}

	/**
	 * Gets the siac d class tipos.
	 *
	 * @return the siac d class tipos
	 */
	public List<SiacDClassTipo> getSiacDClassTipos() {
		return this.siacDClassTipos;
	}

	/**
	 * Sets the siac d class tipos.
	 *
	 * @param siacDClassTipos the new siac d class tipos
	 */
	public void setSiacDClassTipos(List<SiacDClassTipo> siacDClassTipos) {
		this.siacDClassTipos = siacDClassTipos;
	}

	/**
	 * Adds the siac d class tipo.
	 *
	 * @param siacDClassTipo the siac d class tipo
	 * @return the siac d class tipo
	 */
	public SiacDClassTipo addSiacDClassTipo(SiacDClassTipo siacDClassTipo) {
		getSiacDClassTipos().add(siacDClassTipo);
		siacDClassTipo.setSiacTEnteProprietario(this);

		return siacDClassTipo;
	}

	/**
	 * Removes the siac d class tipo.
	 *
	 * @param siacDClassTipo the siac d class tipo
	 * @return the siac d class tipo
	 */
	public SiacDClassTipo removeSiacDClassTipo(SiacDClassTipo siacDClassTipo) {
		getSiacDClassTipos().remove(siacDClassTipo);
		siacDClassTipo.setSiacTEnteProprietario(null);

		return siacDClassTipo;
	}

	/**
	 * Gets the siac d fase operativas.
	 *
	 * @return the siac d fase operativas
	 */
	public List<SiacDFaseOperativa> getSiacDFaseOperativas() {
		return this.siacDFaseOperativas;
	}

	/**
	 * Sets the siac d fase operativas.
	 *
	 * @param siacDFaseOperativas the new siac d fase operativas
	 */
	public void setSiacDFaseOperativas(List<SiacDFaseOperativa> siacDFaseOperativas) {
		this.siacDFaseOperativas = siacDFaseOperativas;
	}

	/**
	 * Adds the siac d fase operativa.
	 *
	 * @param siacDFaseOperativa the siac d fase operativa
	 * @return the siac d fase operativa
	 */
	public SiacDFaseOperativa addSiacDFaseOperativa(SiacDFaseOperativa siacDFaseOperativa) {
		getSiacDFaseOperativas().add(siacDFaseOperativa);
		siacDFaseOperativa.setSiacTEnteProprietario(this);

		return siacDFaseOperativa;
	}

	/**
	 * Removes the siac d fase operativa.
	 *
	 * @param siacDFaseOperativa the siac d fase operativa
	 * @return the siac d fase operativa
	 */
	public SiacDFaseOperativa removeSiacDFaseOperativa(SiacDFaseOperativa siacDFaseOperativa) {
		getSiacDFaseOperativas().remove(siacDFaseOperativa);
		siacDFaseOperativa.setSiacTEnteProprietario(null);

		return siacDFaseOperativa;
	}

	/**
	 * Gets the siac d gestione livellos.
	 *
	 * @return the siac d gestione livellos
	 */
	public List<SiacDGestioneLivello> getSiacDGestioneLivellos() {
		return this.siacDGestioneLivellos;
	}

	/**
	 * Sets the siac d gestione livellos.
	 *
	 * @param siacDGestioneLivellos the new siac d gestione livellos
	 */
	public void setSiacDGestioneLivellos(List<SiacDGestioneLivello> siacDGestioneLivellos) {
		this.siacDGestioneLivellos = siacDGestioneLivellos;
	}

	/**
	 * Adds the siac d gestione livello.
	 *
	 * @param siacDGestioneLivello the siac d gestione livello
	 * @return the siac d gestione livello
	 */
	public SiacDGestioneLivello addSiacDGestioneLivello(SiacDGestioneLivello siacDGestioneLivello) {
		getSiacDGestioneLivellos().add(siacDGestioneLivello);
		siacDGestioneLivello.setSiacTEnteProprietario(this);

		return siacDGestioneLivello;
	}

	/**
	 * Removes the siac d gestione livello.
	 *
	 * @param siacDGestioneLivello the siac d gestione livello
	 * @return the siac d gestione livello
	 */
	public SiacDGestioneLivello removeSiacDGestioneLivello(SiacDGestioneLivello siacDGestioneLivello) {
		getSiacDGestioneLivellos().remove(siacDGestioneLivello);
		siacDGestioneLivello.setSiacTEnteProprietario(null);

		return siacDGestioneLivello;
	}

	/**
	 * Gets the siac d gestione tipos.
	 *
	 * @return the siac d gestione tipos
	 */
	public List<SiacDGestioneTipo> getSiacDGestioneTipos() {
		return this.siacDGestioneTipos;
	}

	/**
	 * Sets the siac d gestione tipos.
	 *
	 * @param siacDGestioneTipos the new siac d gestione tipos
	 */
	public void setSiacDGestioneTipos(List<SiacDGestioneTipo> siacDGestioneTipos) {
		this.siacDGestioneTipos = siacDGestioneTipos;
	}

	/**
	 * Adds the siac d gestione tipo.
	 *
	 * @param siacDGestioneTipo the siac d gestione tipo
	 * @return the siac d gestione tipo
	 */
	public SiacDGestioneTipo addSiacDGestioneTipo(SiacDGestioneTipo siacDGestioneTipo) {
		getSiacDGestioneTipos().add(siacDGestioneTipo);
		siacDGestioneTipo.setSiacTEnteProprietario(this);

		return siacDGestioneTipo;
	}

	/**
	 * Removes the siac d gestione tipo.
	 *
	 * @param siacDGestioneTipo the siac d gestione tipo
	 * @return the siac d gestione tipo
	 */
	public SiacDGestioneTipo removeSiacDGestioneTipo(SiacDGestioneTipo siacDGestioneTipo) {
		getSiacDGestioneTipos().remove(siacDGestioneTipo);
		siacDGestioneTipo.setSiacTEnteProprietario(null);

		return siacDGestioneTipo;
	}

	/**
	 * Gets the siac d gruppo azionis.
	 *
	 * @return the siac d gruppo azionis
	 */
	public List<SiacDGruppoAzioni> getSiacDGruppoAzionis() {
		return this.siacDGruppoAzionis;
	}

	/**
	 * Sets the siac d gruppo azionis.
	 *
	 * @param siacDGruppoAzionis the new siac d gruppo azionis
	 */
	public void setSiacDGruppoAzionis(List<SiacDGruppoAzioni> siacDGruppoAzionis) {
		this.siacDGruppoAzionis = siacDGruppoAzionis;
	}

	/**
	 * Adds the siac d gruppo azioni.
	 *
	 * @param siacDGruppoAzioni the siac d gruppo azioni
	 * @return the siac d gruppo azioni
	 */
	public SiacDGruppoAzioni addSiacDGruppoAzioni(SiacDGruppoAzioni siacDGruppoAzioni) {
		getSiacDGruppoAzionis().add(siacDGruppoAzioni);
		siacDGruppoAzioni.setSiacTEnteProprietario(this);

		return siacDGruppoAzioni;
	}

	/**
	 * Removes the siac d gruppo azioni.
	 *
	 * @param siacDGruppoAzioni the siac d gruppo azioni
	 * @return the siac d gruppo azioni
	 */
	public SiacDGruppoAzioni removeSiacDGruppoAzioni(SiacDGruppoAzioni siacDGruppoAzioni) {
		getSiacDGruppoAzionis().remove(siacDGruppoAzioni);
		siacDGruppoAzioni.setSiacTEnteProprietario(null);

		return siacDGruppoAzioni;
	}

	/**
	 * Gets the siac d periodo tipos.
	 *
	 * @return the siac d periodo tipos
	 */
	public List<SiacDPeriodoTipo> getSiacDPeriodoTipos() {
		return this.siacDPeriodoTipos;
	}

	/**
	 * Sets the siac d periodo tipos.
	 *
	 * @param siacDPeriodoTipos the new siac d periodo tipos
	 */
	public void setSiacDPeriodoTipos(List<SiacDPeriodoTipo> siacDPeriodoTipos) {
		this.siacDPeriodoTipos = siacDPeriodoTipos;
	}

	/**
	 * Adds the siac d periodo tipo.
	 *
	 * @param siacDPeriodoTipo the siac d periodo tipo
	 * @return the siac d periodo tipo
	 */
	public SiacDPeriodoTipo addSiacDPeriodoTipo(SiacDPeriodoTipo siacDPeriodoTipo) {
		getSiacDPeriodoTipos().add(siacDPeriodoTipo);
		siacDPeriodoTipo.setSiacTEnteProprietario(this);

		return siacDPeriodoTipo;
	}

	/**
	 * Removes the siac d periodo tipo.
	 *
	 * @param siacDPeriodoTipo the siac d periodo tipo
	 * @return the siac d periodo tipo
	 */
	public SiacDPeriodoTipo removeSiacDPeriodoTipo(SiacDPeriodoTipo siacDPeriodoTipo) {
		getSiacDPeriodoTipos().remove(siacDPeriodoTipo);
		siacDPeriodoTipo.setSiacTEnteProprietario(null);

		return siacDPeriodoTipo;
	}

	/**
	 * Gets the siac d ruolos.
	 *
	 * @return the siac d ruolos
	 */
	public List<SiacDRuolo> getSiacDRuolos() {
		return this.siacDRuolos;
	}

	/**
	 * Sets the siac d ruolos.
	 *
	 * @param siacDRuolos the new siac d ruolos
	 */
	public void setSiacDRuolos(List<SiacDRuolo> siacDRuolos) {
		this.siacDRuolos = siacDRuolos;
	}

	/**
	 * Adds the siac d ruolo.
	 *
	 * @param siacDRuolo the siac d ruolo
	 * @return the siac d ruolo
	 */
	public SiacDRuolo addSiacDRuolo(SiacDRuolo siacDRuolo) {
		getSiacDRuolos().add(siacDRuolo);
		siacDRuolo.setSiacTEnteProprietario(this);

		return siacDRuolo;
	}

	/**
	 * Removes the siac d ruolo.
	 *
	 * @param siacDRuolo the siac d ruolo
	 * @return the siac d ruolo
	 */
	public SiacDRuolo removeSiacDRuolo(SiacDRuolo siacDRuolo) {
		getSiacDRuolos().remove(siacDRuolo);
		siacDRuolo.setSiacTEnteProprietario(null);

		return siacDRuolo;
	}

	/**
	 * Gets the siac d ruolo ops.
	 *
	 * @return the siac d ruolo ops
	 */
	public List<SiacDRuoloOp> getSiacDRuoloOps() {
		return this.siacDRuoloOps;
	}

	/**
	 * Sets the siac d ruolo ops.
	 *
	 * @param siacDRuoloOps the new siac d ruolo ops
	 */
	public void setSiacDRuoloOps(List<SiacDRuoloOp> siacDRuoloOps) {
		this.siacDRuoloOps = siacDRuoloOps;
	}

	/**
	 * Adds the siac d ruolo op.
	 *
	 * @param siacDRuoloOp the siac d ruolo op
	 * @return the siac d ruolo op
	 */
	public SiacDRuoloOp addSiacDRuoloOp(SiacDRuoloOp siacDRuoloOp) {
		getSiacDRuoloOps().add(siacDRuoloOp);
		siacDRuoloOp.setSiacTEnteProprietario(this);

		return siacDRuoloOp;
	}

	/**
	 * Removes the siac d ruolo op.
	 *
	 * @param siacDRuoloOp the siac d ruolo op
	 * @return the siac d ruolo op
	 */
	public SiacDRuoloOp removeSiacDRuoloOp(SiacDRuoloOp siacDRuoloOp) {
		getSiacDRuoloOps().remove(siacDRuoloOp);
		siacDRuoloOp.setSiacTEnteProprietario(null);

		return siacDRuoloOp;
	}

	/**
	 * Gets the siac d soggetto statos.
	 *
	 * @return the siac d soggetto statos
	 */
	public List<SiacDSoggettoStato> getSiacDSoggettoStatos() {
		return this.siacDSoggettoStatos;
	}

	/**
	 * Sets the siac d soggetto statos.
	 *
	 * @param siacDSoggettoStatos the new siac d soggetto statos
	 */
	public void setSiacDSoggettoStatos(List<SiacDSoggettoStato> siacDSoggettoStatos) {
		this.siacDSoggettoStatos = siacDSoggettoStatos;
	}

	/**
	 * Adds the siac d soggetto stato.
	 *
	 * @param siacDSoggettoStato the siac d soggetto stato
	 * @return the siac d soggetto stato
	 */
	public SiacDSoggettoStato addSiacDSoggettoStato(SiacDSoggettoStato siacDSoggettoStato) {
		getSiacDSoggettoStatos().add(siacDSoggettoStato);
		siacDSoggettoStato.setSiacTEnteProprietario(this);

		return siacDSoggettoStato;
	}

	/**
	 * Removes the siac d soggetto stato.
	 *
	 * @param siacDSoggettoStato the siac d soggetto stato
	 * @return the siac d soggetto stato
	 */
	public SiacDSoggettoStato removeSiacDSoggettoStato(SiacDSoggettoStato siacDSoggettoStato) {
		getSiacDSoggettoStatos().remove(siacDSoggettoStato);
		siacDSoggettoStato.setSiacTEnteProprietario(null);

		return siacDSoggettoStato;
	}

	/**
	 * Gets the siac d variazione tipos.
	 *
	 * @return the siac d variazione tipos
	 */
	public List<SiacDVariazioneTipo> getSiacDVariazioneTipos() {
		return this.siacDVariazioneTipos;
	}

	/**
	 * Sets the siac d variazione tipos.
	 *
	 * @param siacDVariazioneTipos the new siac d variazione tipos
	 */
	public void setSiacDVariazioneTipos(List<SiacDVariazioneTipo> siacDVariazioneTipos) {
		this.siacDVariazioneTipos = siacDVariazioneTipos;
	}

	/**
	 * Adds the siac d variazione tipo.
	 *
	 * @param siacDVariazioneTipo the siac d variazione tipo
	 * @return the siac d variazione tipo
	 */
	public SiacDVariazioneTipo addSiacDVariazioneTipo(SiacDVariazioneTipo siacDVariazioneTipo) {
		getSiacDVariazioneTipos().add(siacDVariazioneTipo);
		siacDVariazioneTipo.setSiacTEnteProprietario(this);

		return siacDVariazioneTipo;
	}

	/**
	 * Removes the siac d variazione tipo.
	 *
	 * @param siacDVariazioneTipo the siac d variazione tipo
	 * @return the siac d variazione tipo
	 */
	public SiacDVariazioneTipo removeSiacDVariazioneTipo(SiacDVariazioneTipo siacDVariazioneTipo) {
		getSiacDVariazioneTipos().remove(siacDVariazioneTipo);
		siacDVariazioneTipo.setSiacTEnteProprietario(null);

		return siacDVariazioneTipo;
	}

	/**
	 * Gets the siac d variazone statos.
	 *
	 * @return the siac d variazone statos
	 */
	public List<SiacDVariazioneStato> getSiacDVariazoneStatos() {
		return this.siacDVariazioneStatos;
	}

	/**
	 * Sets the siac d variazone statos.
	 *
	 * @param siacDVariazoneStatos the new siac d variazone statos
	 */
	public void setSiacDVariazoneStatos(List<SiacDVariazioneStato> siacDVariazoneStatos) {
		this.siacDVariazioneStatos = siacDVariazoneStatos;
	}

	/**
	 * Adds the siac d variazone stato.
	 *
	 * @param siacDVariazoneStato the siac d variazone stato
	 * @return the siac d variazione stato
	 */
	public SiacDVariazioneStato addSiacDVariazoneStato(SiacDVariazioneStato siacDVariazoneStato) {
		getSiacDVariazoneStatos().add(siacDVariazoneStato);
		siacDVariazoneStato.setSiacTEnteProprietario(this);

		return siacDVariazoneStato;
	}

	/**
	 * Removes the siac d variazone stato.
	 *
	 * @param siacDVariazoneStato the siac d variazone stato
	 * @return the siac d variazione stato
	 */
	public SiacDVariazioneStato removeSiacDVariazoneStato(SiacDVariazioneStato siacDVariazoneStato) {
		getSiacDVariazoneStatos().remove(siacDVariazoneStato);
		siacDVariazoneStato.setSiacTEnteProprietario(null);

		return siacDVariazoneStato;
	}

	/**
	 * Gets the siac r account ruolo ops.
	 *
	 * @return the siac r account ruolo ops
	 */
	public List<SiacRAccountRuoloOp> getSiacRAccountRuoloOps() {
		return this.siacRAccountRuoloOps;
	}

	/**
	 * Sets the siac r account ruolo ops.
	 *
	 * @param siacRAccountRuoloOps the new siac r account ruolo ops
	 */
	public void setSiacRAccountRuoloOps(List<SiacRAccountRuoloOp> siacRAccountRuoloOps) {
		this.siacRAccountRuoloOps = siacRAccountRuoloOps;
	}

	/**
	 * Adds the siac r account ruolo op.
	 *
	 * @param siacRAccountRuoloOp the siac r account ruolo op
	 * @return the siac r account ruolo op
	 */
	public SiacRAccountRuoloOp addSiacRAccountRuoloOp(SiacRAccountRuoloOp siacRAccountRuoloOp) {
		getSiacRAccountRuoloOps().add(siacRAccountRuoloOp);
		siacRAccountRuoloOp.setSiacTEnteProprietario(this);

		return siacRAccountRuoloOp;
	}

	/**
	 * Removes the siac r account ruolo op.
	 *
	 * @param siacRAccountRuoloOp the siac r account ruolo op
	 * @return the siac r account ruolo op
	 */
	public SiacRAccountRuoloOp removeSiacRAccountRuoloOp(SiacRAccountRuoloOp siacRAccountRuoloOp) {
		getSiacRAccountRuoloOps().remove(siacRAccountRuoloOp);
		siacRAccountRuoloOp.setSiacTEnteProprietario(null);

		return siacRAccountRuoloOp;
	}

	/**
	 * Gets the siac r attr bil elem tipos.
	 *
	 * @return the siac r attr bil elem tipos
	 */
	public List<SiacRAttrBilElemTipo> getSiacRAttrBilElemTipos() {
		return this.siacRAttrBilElemTipos;
	}

	/**
	 * Sets the siac r attr bil elem tipos.
	 *
	 * @param siacRAttrBilElemTipos the new siac r attr bil elem tipos
	 */
	public void setSiacRAttrBilElemTipos(List<SiacRAttrBilElemTipo> siacRAttrBilElemTipos) {
		this.siacRAttrBilElemTipos = siacRAttrBilElemTipos;
	}

	/**
	 * Adds the siac r attr bil elem tipo.
	 *
	 * @param siacRAttrBilElemTipo the siac r attr bil elem tipo
	 * @return the siac r attr bil elem tipo
	 */
	public SiacRAttrBilElemTipo addSiacRAttrBilElemTipo(SiacRAttrBilElemTipo siacRAttrBilElemTipo) {
		getSiacRAttrBilElemTipos().add(siacRAttrBilElemTipo);
		siacRAttrBilElemTipo.setSiacTEnteProprietario(this);

		return siacRAttrBilElemTipo;
	}

	/**
	 * Removes the siac r attr bil elem tipo.
	 *
	 * @param siacRAttrBilElemTipo the siac r attr bil elem tipo
	 * @return the siac r attr bil elem tipo
	 */
	public SiacRAttrBilElemTipo removeSiacRAttrBilElemTipo(SiacRAttrBilElemTipo siacRAttrBilElemTipo) {
		getSiacRAttrBilElemTipos().remove(siacRAttrBilElemTipo);
		siacRAttrBilElemTipo.setSiacTEnteProprietario(null);

		return siacRAttrBilElemTipo;
	}

	/**
	 * Gets the siac r attr class tipos.
	 *
	 * @return the siac r attr class tipos
	 */
	public List<SiacRAttrClassTipo> getSiacRAttrClassTipos() {
		return this.siacRAttrClassTipos;
	}

	/**
	 * Sets the siac r attr class tipos.
	 *
	 * @param siacRAttrClassTipos the new siac r attr class tipos
	 */
	public void setSiacRAttrClassTipos(List<SiacRAttrClassTipo> siacRAttrClassTipos) {
		this.siacRAttrClassTipos = siacRAttrClassTipos;
	}

	/**
	 * Adds the siac r attr class tipo.
	 *
	 * @param siacRAttrClassTipo the siac r attr class tipo
	 * @return the siac r attr class tipo
	 */
	public SiacRAttrClassTipo addSiacRAttrClassTipo(SiacRAttrClassTipo siacRAttrClassTipo) {
		getSiacRAttrClassTipos().add(siacRAttrClassTipo);
		siacRAttrClassTipo.setSiacTEnteProprietario(this);

		return siacRAttrClassTipo;
	}

	/**
	 * Removes the siac r attr class tipo.
	 *
	 * @param siacRAttrClassTipo the siac r attr class tipo
	 * @return the siac r attr class tipo
	 */
	public SiacRAttrClassTipo removeSiacRAttrClassTipo(SiacRAttrClassTipo siacRAttrClassTipo) {
		getSiacRAttrClassTipos().remove(siacRAttrClassTipo);
		siacRAttrClassTipo.setSiacTEnteProprietario(null);

		return siacRAttrClassTipo;
	}

	/**
	 * Gets the siac r bil elem atto legges.
	 *
	 * @return the siac r bil elem atto legges
	 */
	public List<SiacRBilElemAttoLegge> getSiacRBilElemAttoLegges() {
		return this.siacRBilElemAttoLegges;
	}

	/**
	 * Sets the siac r bil elem atto legges.
	 *
	 * @param siacRBilElemAttoLegges the new siac r bil elem atto legges
	 */
	public void setSiacRBilElemAttoLegges(List<SiacRBilElemAttoLegge> siacRBilElemAttoLegges) {
		this.siacRBilElemAttoLegges = siacRBilElemAttoLegges;
	}

	/**
	 * Adds the siac r bil elem atto legge.
	 *
	 * @param siacRBilElemAttoLegge the siac r bil elem atto legge
	 * @return the siac r bil elem atto legge
	 */
	public SiacRBilElemAttoLegge addSiacRBilElemAttoLegge(SiacRBilElemAttoLegge siacRBilElemAttoLegge) {
		getSiacRBilElemAttoLegges().add(siacRBilElemAttoLegge);
		siacRBilElemAttoLegge.setSiacTEnteProprietario(this);

		return siacRBilElemAttoLegge;
	}

	/**
	 * Removes the siac r bil elem atto legge.
	 *
	 * @param siacRBilElemAttoLegge the siac r bil elem atto legge
	 * @return the siac r bil elem atto legge
	 */
	public SiacRBilElemAttoLegge removeSiacRBilElemAttoLegge(SiacRBilElemAttoLegge siacRBilElemAttoLegge) {
		getSiacRBilElemAttoLegges().remove(siacRBilElemAttoLegge);
		siacRBilElemAttoLegge.setSiacTEnteProprietario(null);

		return siacRBilElemAttoLegge;
	}

	/**
	 * Gets the siac r bil elem attrs.
	 *
	 * @return the siac r bil elem attrs
	 */
	public List<SiacRBilElemAttr> getSiacRBilElemAttrs() {
		return this.siacRBilElemAttrs;
	}

	/**
	 * Sets the siac r bil elem attrs.
	 *
	 * @param siacRBilElemAttrs the new siac r bil elem attrs
	 */
	public void setSiacRBilElemAttrs(List<SiacRBilElemAttr> siacRBilElemAttrs) {
		this.siacRBilElemAttrs = siacRBilElemAttrs;
	}

	/**
	 * Adds the siac r bil elem attr.
	 *
	 * @param siacRBilElemAttr the siac r bil elem attr
	 * @return the siac r bil elem attr
	 */
	public SiacRBilElemAttr addSiacRBilElemAttr(SiacRBilElemAttr siacRBilElemAttr) {
		getSiacRBilElemAttrs().add(siacRBilElemAttr);
		siacRBilElemAttr.setSiacTEnteProprietario(this);

		return siacRBilElemAttr;
	}

	/**
	 * Removes the siac r bil elem attr.
	 *
	 * @param siacRBilElemAttr the siac r bil elem attr
	 * @return the siac r bil elem attr
	 */
	public SiacRBilElemAttr removeSiacRBilElemAttr(SiacRBilElemAttr siacRBilElemAttr) {
		getSiacRBilElemAttrs().remove(siacRBilElemAttr);
		siacRBilElemAttr.setSiacTEnteProprietario(null);

		return siacRBilElemAttr;
	}

	/**
	 * Gets the siac r bil elem classes.
	 *
	 * @return the siac r bil elem classes
	 */
	public List<SiacRBilElemClass> getSiacRBilElemClasses() {
		return this.siacRBilElemClasses;
	}

	/**
	 * Sets the siac r bil elem classes.
	 *
	 * @param siacRBilElemClasses the new siac r bil elem classes
	 */
	public void setSiacRBilElemClasses(List<SiacRBilElemClass> siacRBilElemClasses) {
		this.siacRBilElemClasses = siacRBilElemClasses;
	}

	/**
	 * Adds the siac r bil elem class.
	 *
	 * @param siacRBilElemClass the siac r bil elem class
	 * @return the siac r bil elem class
	 */
	public SiacRBilElemClass addSiacRBilElemClass(SiacRBilElemClass siacRBilElemClass) {
		getSiacRBilElemClasses().add(siacRBilElemClass);
		siacRBilElemClass.setSiacTEnteProprietario(this);

		return siacRBilElemClass;
	}

	/**
	 * Removes the siac r bil elem class.
	 *
	 * @param siacRBilElemClass the siac r bil elem class
	 * @return the siac r bil elem class
	 */
	public SiacRBilElemClass removeSiacRBilElemClass(SiacRBilElemClass siacRBilElemClass) {
		getSiacRBilElemClasses().remove(siacRBilElemClass);
		siacRBilElemClass.setSiacTEnteProprietario(null);

		return siacRBilElemClass;
	}

	/**
	 * Gets the siac r bil elem class vars.
	 *
	 * @return the siac r bil elem class vars
	 */
	public List<SiacRBilElemClassVar> getSiacRBilElemClassVars() {
		return this.siacRBilElemClassVars;
	}

	/**
	 * Sets the siac r bil elem class vars.
	 *
	 * @param siacRBilElemClassVars the new siac r bil elem class vars
	 */
	public void setSiacRBilElemClassVars(List<SiacRBilElemClassVar> siacRBilElemClassVars) {
		this.siacRBilElemClassVars = siacRBilElemClassVars;
	}

	/**
	 * Adds the siac r bil elem class var.
	 *
	 * @param siacRBilElemClassVar the siac r bil elem class var
	 * @return the siac r bil elem class var
	 */
	public SiacRBilElemClassVar addSiacRBilElemClassVar(SiacRBilElemClassVar siacRBilElemClassVar) {
		getSiacRBilElemClassVars().add(siacRBilElemClassVar);
		siacRBilElemClassVar.setSiacTEnteProprietario(this);

		return siacRBilElemClassVar;
	}

	/**
	 * Removes the siac r bil elem class var.
	 *
	 * @param siacRBilElemClassVar the siac r bil elem class var
	 * @return the siac r bil elem class var
	 */
	public SiacRBilElemClassVar removeSiacRBilElemClassVar(SiacRBilElemClassVar siacRBilElemClassVar) {
		getSiacRBilElemClassVars().remove(siacRBilElemClassVar);
		siacRBilElemClassVar.setSiacTEnteProprietario(null);

		return siacRBilElemClassVar;
	}

	/**
	 * Gets the siac r bil elem rel tempos.
	 *
	 * @return the siac r bil elem rel tempos
	 */
	public List<SiacRBilElemRelTempo> getSiacRBilElemRelTempos() {
		return this.siacRBilElemRelTempos;
	}

	/**
	 * Sets the siac r bil elem rel tempos.
	 *
	 * @param siacRBilElemRelTempos the new siac r bil elem rel tempos
	 */
	public void setSiacRBilElemRelTempos(List<SiacRBilElemRelTempo> siacRBilElemRelTempos) {
		this.siacRBilElemRelTempos = siacRBilElemRelTempos;
	}

	/**
	 * Adds the siac r bil elem rel tempo.
	 *
	 * @param siacRBilElemRelTempo the siac r bil elem rel tempo
	 * @return the siac r bil elem rel tempo
	 */
	public SiacRBilElemRelTempo addSiacRBilElemRelTempo(SiacRBilElemRelTempo siacRBilElemRelTempo) {
		getSiacRBilElemRelTempos().add(siacRBilElemRelTempo);
		siacRBilElemRelTempo.setSiacTEnteProprietario(this);

		return siacRBilElemRelTempo;
	}

	/**
	 * Removes the siac r bil elem rel tempo.
	 *
	 * @param siacRBilElemRelTempo the siac r bil elem rel tempo
	 * @return the siac r bil elem rel tempo
	 */
	public SiacRBilElemRelTempo removeSiacRBilElemRelTempo(SiacRBilElemRelTempo siacRBilElemRelTempo) {
		getSiacRBilElemRelTempos().remove(siacRBilElemRelTempo);
		siacRBilElemRelTempo.setSiacTEnteProprietario(null);

		return siacRBilElemRelTempo;
	}

	/**
	 * Gets the siac r bil elem statos.
	 *
	 * @return the siac r bil elem statos
	 */
	public List<SiacRBilElemStato> getSiacRBilElemStatos() {
		return this.siacRBilElemStatos;
	}

	/**
	 * Sets the siac r bil elem statos.
	 *
	 * @param siacRBilElemStatos the new siac r bil elem statos
	 */
	public void setSiacRBilElemStatos(List<SiacRBilElemStato> siacRBilElemStatos) {
		this.siacRBilElemStatos = siacRBilElemStatos;
	}

	/**
	 * Adds the siac r bil elem stato.
	 *
	 * @param siacRBilElemStato the siac r bil elem stato
	 * @return the siac r bil elem stato
	 */
	public SiacRBilElemStato addSiacRBilElemStato(SiacRBilElemStato siacRBilElemStato) {
		getSiacRBilElemStatos().add(siacRBilElemStato);
		siacRBilElemStato.setSiacTEnteProprietario(this);

		return siacRBilElemStato;
	}

	/**
	 * Removes the siac r bil elem stato.
	 *
	 * @param siacRBilElemStato the siac r bil elem stato
	 * @return the siac r bil elem stato
	 */
	public SiacRBilElemStato removeSiacRBilElemStato(SiacRBilElemStato siacRBilElemStato) {
		getSiacRBilElemStatos().remove(siacRBilElemStato);
		siacRBilElemStato.setSiacTEnteProprietario(null);

		return siacRBilElemStato;
	}

	/**
	 * Gets the siac r bil elem tipo class tips.
	 *
	 * @return the siac r bil elem tipo class tips
	 */
	public List<SiacRBilElemTipoClassTip> getSiacRBilElemTipoClassTips() {
		return this.siacRBilElemTipoClassTips;
	}

	/**
	 * Sets the siac r bil elem tipo class tips.
	 *
	 * @param siacRBilElemTipoClassTips the new siac r bil elem tipo class tips
	 */
	public void setSiacRBilElemTipoClassTips(List<SiacRBilElemTipoClassTip> siacRBilElemTipoClassTips) {
		this.siacRBilElemTipoClassTips = siacRBilElemTipoClassTips;
	}

	/**
	 * Adds the siac r bil elem tipo class tip.
	 *
	 * @param siacRBilElemTipoClassTip the siac r bil elem tipo class tip
	 * @return the siac r bil elem tipo class tip
	 */
	public SiacRBilElemTipoClassTip addSiacRBilElemTipoClassTip(SiacRBilElemTipoClassTip siacRBilElemTipoClassTip) {
		getSiacRBilElemTipoClassTips().add(siacRBilElemTipoClassTip);
		siacRBilElemTipoClassTip.setSiacTEnteProprietario(this);

		return siacRBilElemTipoClassTip;
	}

	/**
	 * Removes the siac r bil elem tipo class tip.
	 *
	 * @param siacRBilElemTipoClassTip the siac r bil elem tipo class tip
	 * @return the siac r bil elem tipo class tip
	 */
	public SiacRBilElemTipoClassTip removeSiacRBilElemTipoClassTip(SiacRBilElemTipoClassTip siacRBilElemTipoClassTip) {
		getSiacRBilElemTipoClassTips().remove(siacRBilElemTipoClassTip);
		siacRBilElemTipoClassTip.setSiacTEnteProprietario(null);

		return siacRBilElemTipoClassTip;
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
		siacRBilFaseOperativa.setSiacTEnteProprietario(this);

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
		siacRBilFaseOperativa.setSiacTEnteProprietario(null);

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
		siacRBilStatoOp.setSiacTEnteProprietario(this);

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
		siacRBilStatoOp.setSiacTEnteProprietario(null);

		return siacRBilStatoOp;
	}

	/**
	 * Gets the siac r bil tipo stato ops.
	 *
	 * @return the siac r bil tipo stato ops
	 */
	public List<SiacRBilTipoStatoOp> getSiacRBilTipoStatoOps() {
		return this.siacRBilTipoStatoOps;
	}

	/**
	 * Sets the siac r bil tipo stato ops.
	 *
	 * @param siacRBilTipoStatoOps the new siac r bil tipo stato ops
	 */
	public void setSiacRBilTipoStatoOps(List<SiacRBilTipoStatoOp> siacRBilTipoStatoOps) {
		this.siacRBilTipoStatoOps = siacRBilTipoStatoOps;
	}

	/**
	 * Adds the siac r bil tipo stato op.
	 *
	 * @param siacRBilTipoStatoOp the siac r bil tipo stato op
	 * @return the siac r bil tipo stato op
	 */
	public SiacRBilTipoStatoOp addSiacRBilTipoStatoOp(SiacRBilTipoStatoOp siacRBilTipoStatoOp) {
		getSiacRBilTipoStatoOps().add(siacRBilTipoStatoOp);
		siacRBilTipoStatoOp.setSiacTEnteProprietario(this);

		return siacRBilTipoStatoOp;
	}

	/**
	 * Removes the siac r bil tipo stato op.
	 *
	 * @param siacRBilTipoStatoOp the siac r bil tipo stato op
	 * @return the siac r bil tipo stato op
	 */
	public SiacRBilTipoStatoOp removeSiacRBilTipoStatoOp(SiacRBilTipoStatoOp siacRBilTipoStatoOp) {
		getSiacRBilTipoStatoOps().remove(siacRBilTipoStatoOp);
		siacRBilTipoStatoOp.setSiacTEnteProprietario(null);

		return siacRBilTipoStatoOp;
	}

	/**
	 * Gets the siac r classes.
	 *
	 * @return the siac r classes
	 */
	public List<SiacRClass> getSiacRClasses() {
		return this.siacRClasses;
	}

	/**
	 * Sets the siac r classes.
	 *
	 * @param siacRClasses the new siac r classes
	 */
	public void setSiacRClasses(List<SiacRClass> siacRClasses) {
		this.siacRClasses = siacRClasses;
	}

	/**
	 * Adds the siac r class.
	 *
	 * @param siacRClass the siac r class
	 * @return the siac r class
	 */
	public SiacRClass addSiacRClass(SiacRClass siacRClass) {
		getSiacRClasses().add(siacRClass);
		siacRClass.setSiacTEnteProprietario(this);

		return siacRClass;
	}

	/**
	 * Removes the siac r class.
	 *
	 * @param siacRClass the siac r class
	 * @return the siac r class
	 */
	public SiacRClass removeSiacRClass(SiacRClass siacRClass) {
		getSiacRClasses().remove(siacRClass);
		siacRClass.setSiacTEnteProprietario(null);

		return siacRClass;
	}

	/**
	 * Gets the siac r class attrs.
	 *
	 * @return the siac r class attrs
	 */
	public List<SiacRClassAttr> getSiacRClassAttrs() {
		return this.siacRClassAttrs;
	}

	/**
	 * Sets the siac r class attrs.
	 *
	 * @param siacRClassAttrs the new siac r class attrs
	 */
	public void setSiacRClassAttrs(List<SiacRClassAttr> siacRClassAttrs) {
		this.siacRClassAttrs = siacRClassAttrs;
	}

	/**
	 * Adds the siac r class attr.
	 *
	 * @param siacRClassAttr the siac r class attr
	 * @return the siac r class attr
	 */
	public SiacRClassAttr addSiacRClassAttr(SiacRClassAttr siacRClassAttr) {
		getSiacRClassAttrs().add(siacRClassAttr);
		siacRClassAttr.setSiacTEnteProprietario(this);

		return siacRClassAttr;
	}

	/**
	 * Removes the siac r class attr.
	 *
	 * @param siacRClassAttr the siac r class attr
	 * @return the siac r class attr
	 */
	public SiacRClassAttr removeSiacRClassAttr(SiacRClassAttr siacRClassAttr) {
		getSiacRClassAttrs().remove(siacRClassAttr);
		siacRClassAttr.setSiacTEnteProprietario(null);

		return siacRClassAttr;
	}

	/**
	 * Gets the siac r class fam trees.
	 *
	 * @return the siac r class fam trees
	 */
	public List<SiacRClassFamTree> getSiacRClassFamTrees() {
		return this.siacRClassFamTrees;
	}

	/**
	 * Sets the siac r class fam trees.
	 *
	 * @param siacRClassFamTrees the new siac r class fam trees
	 */
	public void setSiacRClassFamTrees(List<SiacRClassFamTree> siacRClassFamTrees) {
		this.siacRClassFamTrees = siacRClassFamTrees;
	}

	/**
	 * Adds the siac r class fam tree.
	 *
	 * @param siacRClassFamTree the siac r class fam tree
	 * @return the siac r class fam tree
	 */
	public SiacRClassFamTree addSiacRClassFamTree(SiacRClassFamTree siacRClassFamTree) {
		getSiacRClassFamTrees().add(siacRClassFamTree);
		siacRClassFamTree.setSiacTEnteProprietario(this);

		return siacRClassFamTree;
	}

	/**
	 * Removes the siac r class fam tree.
	 *
	 * @param siacRClassFamTree the siac r class fam tree
	 * @return the siac r class fam tree
	 */
	public SiacRClassFamTree removeSiacRClassFamTree(SiacRClassFamTree siacRClassFamTree) {
		getSiacRClassFamTrees().remove(siacRClassFamTree);
		siacRClassFamTree.setSiacTEnteProprietario(null);

		return siacRClassFamTree;
	}

	/**
	 * Gets the siac r fase operativa bil statos.
	 *
	 * @return the siac r fase operativa bil statos
	 */
	public List<SiacRFaseOperativaBilStato> getSiacRFaseOperativaBilStatos() {
		return this.siacRFaseOperativaBilStatos;
	}

	/**
	 * Sets the siac r fase operativa bil statos.
	 *
	 * @param siacRFaseOperativaBilStatos the new siac r fase operativa bil statos
	 */
	public void setSiacRFaseOperativaBilStatos(List<SiacRFaseOperativaBilStato> siacRFaseOperativaBilStatos) {
		this.siacRFaseOperativaBilStatos = siacRFaseOperativaBilStatos;
	}

	/**
	 * Adds the siac r fase operativa bil stato.
	 *
	 * @param siacRFaseOperativaBilStato the siac r fase operativa bil stato
	 * @return the siac r fase operativa bil stato
	 */
	public SiacRFaseOperativaBilStato addSiacRFaseOperativaBilStato(SiacRFaseOperativaBilStato siacRFaseOperativaBilStato) {
		getSiacRFaseOperativaBilStatos().add(siacRFaseOperativaBilStato);
		siacRFaseOperativaBilStato.setSiacTEnteProprietario(this);

		return siacRFaseOperativaBilStato;
	}

	/**
	 * Removes the siac r fase operativa bil stato.
	 *
	 * @param siacRFaseOperativaBilStato the siac r fase operativa bil stato
	 * @return the siac r fase operativa bil stato
	 */
	public SiacRFaseOperativaBilStato removeSiacRFaseOperativaBilStato(SiacRFaseOperativaBilStato siacRFaseOperativaBilStato) {
		getSiacRFaseOperativaBilStatos().remove(siacRFaseOperativaBilStato);
		siacRFaseOperativaBilStato.setSiacTEnteProprietario(null);

		return siacRFaseOperativaBilStato;
	}

	/**
	 * Gets the siac r gestione entes.
	 *
	 * @return the siac r gestione entes
	 */
	public List<SiacRGestioneEnte> getSiacRGestioneEntes() {
		return this.siacRGestioneEntes;
	}

	/**
	 * Sets the siac r gestione entes.
	 *
	 * @param siacRGestioneEntes the new siac r gestione entes
	 */
	public void setSiacRGestioneEntes(List<SiacRGestioneEnte> siacRGestioneEntes) {
		this.siacRGestioneEntes = siacRGestioneEntes;
	}

	/**
	 * Adds the siac r gestione ente.
	 *
	 * @param siacRGestioneEnte the siac r gestione ente
	 * @return the siac r gestione ente
	 */
	public SiacRGestioneEnte addSiacRGestioneEnte(SiacRGestioneEnte siacRGestioneEnte) {
		getSiacRGestioneEntes().add(siacRGestioneEnte);
		siacRGestioneEnte.setSiacTEnteProprietario(this);

		return siacRGestioneEnte;
	}

	/**
	 * Removes the siac r gestione ente.
	 *
	 * @param siacRGestioneEnte the siac r gestione ente
	 * @return the siac r gestione ente
	 */
	public SiacRGestioneEnte removeSiacRGestioneEnte(SiacRGestioneEnte siacRGestioneEnte) {
		getSiacRGestioneEntes().remove(siacRGestioneEnte);
		siacRGestioneEnte.setSiacTEnteProprietario(null);

		return siacRGestioneEnte;
	}

	/**
	 * Gets the siac r gruppo accounts.
	 *
	 * @return the siac r gruppo accounts
	 */
	public List<SiacRGruppoAccount> getSiacRGruppoAccounts() {
		return this.siacRGruppoAccounts;
	}

	/**
	 * Sets the siac r gruppo accounts.
	 *
	 * @param siacRGruppoAccounts the new siac r gruppo accounts
	 */
	public void setSiacRGruppoAccounts(List<SiacRGruppoAccount> siacRGruppoAccounts) {
		this.siacRGruppoAccounts = siacRGruppoAccounts;
	}

	/**
	 * Adds the siac r gruppo account.
	 *
	 * @param siacRGruppoAccount the siac r gruppo account
	 * @return the siac r gruppo account
	 */
	public SiacRGruppoAccount addSiacRGruppoAccount(SiacRGruppoAccount siacRGruppoAccount) {
		getSiacRGruppoAccounts().add(siacRGruppoAccount);
		siacRGruppoAccount.setSiacTEnteProprietario(this);

		return siacRGruppoAccount;
	}

	/**
	 * Removes the siac r gruppo account.
	 *
	 * @param siacRGruppoAccount the siac r gruppo account
	 * @return the siac r gruppo account
	 */
	public SiacRGruppoAccount removeSiacRGruppoAccount(SiacRGruppoAccount siacRGruppoAccount) {
		getSiacRGruppoAccounts().remove(siacRGruppoAccount);
		siacRGruppoAccount.setSiacTEnteProprietario(null);

		return siacRGruppoAccount;
	}

	/**
	 * Gets the siac r gruppo ruolo ops.
	 *
	 * @return the siac r gruppo ruolo ops
	 */
	public List<SiacRGruppoRuoloOp> getSiacRGruppoRuoloOps() {
		return this.siacRGruppoRuoloOps;
	}

	/**
	 * Sets the siac r gruppo ruolo ops.
	 *
	 * @param siacRGruppoRuoloOps the new siac r gruppo ruolo ops
	 */
	public void setSiacRGruppoRuoloOps(List<SiacRGruppoRuoloOp> siacRGruppoRuoloOps) {
		this.siacRGruppoRuoloOps = siacRGruppoRuoloOps;
	}

	/**
	 * Adds the siac r gruppo ruolo op.
	 *
	 * @param siacRGruppoRuoloOp the siac r gruppo ruolo op
	 * @return the siac r gruppo ruolo op
	 */
	public SiacRGruppoRuoloOp addSiacRGruppoRuoloOp(SiacRGruppoRuoloOp siacRGruppoRuoloOp) {
		getSiacRGruppoRuoloOps().add(siacRGruppoRuoloOp);
		siacRGruppoRuoloOp.setSiacTEnteProprietario(this);

		return siacRGruppoRuoloOp;
	}

	/**
	 * Removes the siac r gruppo ruolo op.
	 *
	 * @param siacRGruppoRuoloOp the siac r gruppo ruolo op
	 * @return the siac r gruppo ruolo op
	 */
	public SiacRGruppoRuoloOp removeSiacRGruppoRuoloOp(SiacRGruppoRuoloOp siacRGruppoRuoloOp) {
		getSiacRGruppoRuoloOps().remove(siacRGruppoRuoloOp);
		siacRGruppoRuoloOp.setSiacTEnteProprietario(null);

		return siacRGruppoRuoloOp;
	}

	/**
	 * Gets the siac r ruolo op aziones.
	 *
	 * @return the siac r ruolo op aziones
	 */
	public List<SiacRRuoloOpAzione> getSiacRRuoloOpAziones() {
		return this.siacRRuoloOpAziones;
	}

	/**
	 * Sets the siac r ruolo op aziones.
	 *
	 * @param siacRRuoloOpAziones the new siac r ruolo op aziones
	 */
	public void setSiacRRuoloOpAziones(List<SiacRRuoloOpAzione> siacRRuoloOpAziones) {
		this.siacRRuoloOpAziones = siacRRuoloOpAziones;
	}

	/**
	 * Adds the siac r ruolo op azione.
	 *
	 * @param siacRRuoloOpAzione the siac r ruolo op azione
	 * @return the siac r ruolo op azione
	 */
	public SiacRRuoloOpAzione addSiacRRuoloOpAzione(SiacRRuoloOpAzione siacRRuoloOpAzione) {
		getSiacRRuoloOpAziones().add(siacRRuoloOpAzione);
		siacRRuoloOpAzione.setSiacTEnteProprietario(this);

		return siacRRuoloOpAzione;
	}

	/**
	 * Removes the siac r ruolo op azione.
	 *
	 * @param siacRRuoloOpAzione the siac r ruolo op azione
	 * @return the siac r ruolo op azione
	 */
	public SiacRRuoloOpAzione removeSiacRRuoloOpAzione(SiacRRuoloOpAzione siacRRuoloOpAzione) {
		getSiacRRuoloOpAziones().remove(siacRRuoloOpAzione);
		siacRRuoloOpAzione.setSiacTEnteProprietario(null);

		return siacRRuoloOpAzione;
	}

	/**
	 * @return the siacDVariazioneStatos
	 */
	public List<SiacDVariazioneStato> getSiacDVariazioneStatos() {
		return siacDVariazioneStatos;
	}

	/**
	 * @param siacDVariazioneStatos the siacDVariazioneStatos to set
	 */
	public void setSiacDVariazioneStatos(
			List<SiacDVariazioneStato> siacDVariazioneStatos) {
		this.siacDVariazioneStatos = siacDVariazioneStatos;
	}

	/**
	 * @return the siacRSoggettoEnteProprietarios
	 */
	public List<SiacRSoggettoEnteProprietario> getSiacRSoggettoEnteProprietarios() {
		return siacRSoggettoEnteProprietarios;
	}

	/**
	 * @param siacRSoggettoEnteProprietarios the siacRSoggettoEnteProprietarios to set
	 */
	public void setSiacRSoggettoEnteProprietarios(
			List<SiacRSoggettoEnteProprietario> siacRSoggettoEnteProprietarios) {
		this.siacRSoggettoEnteProprietarios = siacRSoggettoEnteProprietarios;
	}

	/**
	 * Gets the siac r soggetto ruolos.
	 *
	 * @return the siac r soggetto ruolos
	 */
	public List<SiacRSoggettoRuolo> getSiacRSoggettoRuolos() {
		return this.siacRSoggettoRuolos;
	}

	/**
	 * Sets the siac r soggetto ruolos.
	 *
	 * @param siacRSoggettoRuolos the new siac r soggetto ruolos
	 */
	public void setSiacRSoggettoRuolos(List<SiacRSoggettoRuolo> siacRSoggettoRuolos) {
		this.siacRSoggettoRuolos = siacRSoggettoRuolos;
	}

	/**
	 * Adds the siac r soggetto ruolo.
	 *
	 * @param siacRSoggettoRuolo the siac r soggetto ruolo
	 * @return the siac r soggetto ruolo
	 */
	public SiacRSoggettoRuolo addSiacRSoggettoRuolo(SiacRSoggettoRuolo siacRSoggettoRuolo) {
		getSiacRSoggettoRuolos().add(siacRSoggettoRuolo);
		siacRSoggettoRuolo.setSiacTEnteProprietario(this);

		return siacRSoggettoRuolo;
	}

	/**
	 * Removes the siac r soggetto ruolo.
	 *
	 * @param siacRSoggettoRuolo the siac r soggetto ruolo
	 * @return the siac r soggetto ruolo
	 */
	public SiacRSoggettoRuolo removeSiacRSoggettoRuolo(SiacRSoggettoRuolo siacRSoggettoRuolo) {
		getSiacRSoggettoRuolos().remove(siacRSoggettoRuolo);
		siacRSoggettoRuolo.setSiacTEnteProprietario(null);

		return siacRSoggettoRuolo;
	}

	/**
	 * Gets the siac r soggetto statos.
	 *
	 * @return the siac r soggetto statos
	 */
	public List<SiacRSoggettoStato> getSiacRSoggettoStatos() {
		return this.siacRSoggettoStatos;
	}

	/**
	 * Sets the siac r soggetto statos.
	 *
	 * @param siacRSoggettoStatos the new siac r soggetto statos
	 */
	public void setSiacRSoggettoStatos(List<SiacRSoggettoStato> siacRSoggettoStatos) {
		this.siacRSoggettoStatos = siacRSoggettoStatos;
	}

	/**
	 * Adds the siac r soggetto stato.
	 *
	 * @param siacRSoggettoStato the siac r soggetto stato
	 * @return the siac r soggetto stato
	 */
	public SiacRSoggettoStato addSiacRSoggettoStato(SiacRSoggettoStato siacRSoggettoStato) {
		getSiacRSoggettoStatos().add(siacRSoggettoStato);
		siacRSoggettoStato.setSiacTEnteProprietario(this);

		return siacRSoggettoStato;
	}

	/**
	 * Removes the siac r soggetto stato.
	 *
	 * @param siacRSoggettoStato the siac r soggetto stato
	 * @return the siac r soggetto stato
	 */
	public SiacRSoggettoStato removeSiacRSoggettoStato(SiacRSoggettoStato siacRSoggettoStato) {
		getSiacRSoggettoStatos().remove(siacRSoggettoStato);
		siacRSoggettoStato.setSiacTEnteProprietario(null);

		return siacRSoggettoStato;
	}

	/**
	 * Gets the siac r variazione attrs.
	 *
	 * @return the siac r variazione attrs
	 */
	public List<SiacRVariazioneAttr> getSiacRVariazioneAttrs() {
		return this.siacRVariazioneAttrs;
	}

	/**
	 * Sets the siac r variazione attrs.
	 *
	 * @param siacRVariazioneAttrs the new siac r variazione attrs
	 */
	public void setSiacRVariazioneAttrs(List<SiacRVariazioneAttr> siacRVariazioneAttrs) {
		this.siacRVariazioneAttrs = siacRVariazioneAttrs;
	}

	/**
	 * Adds the siac r variazione attr.
	 *
	 * @param siacRVariazioneAttr the siac r variazione attr
	 * @return the siac r variazione attr
	 */
	public SiacRVariazioneAttr addSiacRVariazioneAttr(SiacRVariazioneAttr siacRVariazioneAttr) {
		getSiacRVariazioneAttrs().add(siacRVariazioneAttr);
		siacRVariazioneAttr.setSiacTEnteProprietario(this);

		return siacRVariazioneAttr;
	}

	/**
	 * Removes the siac r variazione attr.
	 *
	 * @param siacRVariazioneAttr the siac r variazione attr
	 * @return the siac r variazione attr
	 */
	public SiacRVariazioneAttr removeSiacRVariazioneAttr(SiacRVariazioneAttr siacRVariazioneAttr) {
		getSiacRVariazioneAttrs().remove(siacRVariazioneAttr);
		siacRVariazioneAttr.setSiacTEnteProprietario(null);

		return siacRVariazioneAttr;
	}

	/**
	 * Gets the siac r variazione statos.
	 *
	 * @return the siac r variazione statos
	 */
	public List<SiacRVariazioneStato> getSiacRVariazioneStatos() {
		return this.siacRVariazioneStatos;
	}

	/**
	 * Sets the siac r variazione statos.
	 *
	 * @param siacRVariazioneStatos the new siac r variazione statos
	 */
	public void setSiacRVariazioneStatos(List<SiacRVariazioneStato> siacRVariazioneStatos) {
		this.siacRVariazioneStatos = siacRVariazioneStatos;
	}

	/**
	 * Adds the siac r variazione stato.
	 *
	 * @param siacRVariazioneStato the siac r variazione stato
	 * @return the siac r variazione stato
	 */
	public SiacRVariazioneStato addSiacRVariazioneStato(SiacRVariazioneStato siacRVariazioneStato) {
		getSiacRVariazioneStatos().add(siacRVariazioneStato);
		siacRVariazioneStato.setSiacTEnteProprietario(this);

		return siacRVariazioneStato;
	}

	/**
	 * Removes the siac r variazione stato.
	 *
	 * @param siacRVariazioneStato the siac r variazione stato
	 * @return the siac r variazione stato
	 */
	public SiacRVariazioneStato removeSiacRVariazioneStato(SiacRVariazioneStato siacRVariazioneStato) {
		getSiacRVariazioneStatos().remove(siacRVariazioneStato);
		siacRVariazioneStato.setSiacTEnteProprietario(null);

		return siacRVariazioneStato;
	}

	/**
	 * Gets the siac t accounts.
	 *
	 * @return the siac t accounts
	 */
	public List<SiacTAccount> getSiacTAccounts() {
		return this.siacTAccounts;
	}

	/**
	 * Sets the siac t accounts.
	 *
	 * @param siacTAccounts the new siac t accounts
	 */
	public void setSiacTAccounts(List<SiacTAccount> siacTAccounts) {
		this.siacTAccounts = siacTAccounts;
	}

	/**
	 * Adds the siac t account.
	 *
	 * @param siacTAccount the siac t account
	 * @return the siac t account
	 */
	public SiacTAccount addSiacTAccount(SiacTAccount siacTAccount) {
		getSiacTAccounts().add(siacTAccount);
		siacTAccount.setSiacTEnteProprietario(this);

		return siacTAccount;
	}

	/**
	 * Removes the siac t account.
	 *
	 * @param siacTAccount the siac t account
	 * @return the siac t account
	 */
	public SiacTAccount removeSiacTAccount(SiacTAccount siacTAccount) {
		getSiacTAccounts().remove(siacTAccount);
		siacTAccount.setSiacTEnteProprietario(null);

		return siacTAccount;
	}

	/**
	 * Gets the siac t atto amms.
	 *
	 * @return the siac t atto amms
	 */
	public List<SiacTAttoAmm> getSiacTAttoAmms() {
		return this.siacTAttoAmms;
	}

	/**
	 * Sets the siac t atto amms.
	 *
	 * @param siacTAttoAmms the new siac t atto amms
	 */
	public void setSiacTAttoAmms(List<SiacTAttoAmm> siacTAttoAmms) {
		this.siacTAttoAmms = siacTAttoAmms;
	}

	/**
	 * Adds the siac t atto amm.
	 *
	 * @param siacTAttoAmm the siac t atto amm
	 * @return the siac t atto amm
	 */
	public SiacTAttoAmm addSiacTAttoAmm(SiacTAttoAmm siacTAttoAmm) {
		getSiacTAttoAmms().add(siacTAttoAmm);
		siacTAttoAmm.setSiacTEnteProprietario(this);

		return siacTAttoAmm;
	}

	/**
	 * Removes the siac t atto amm.
	 *
	 * @param siacTAttoAmm the siac t atto amm
	 * @return the siac t atto amm
	 */
	public SiacTAttoAmm removeSiacTAttoAmm(SiacTAttoAmm siacTAttoAmm) {
		getSiacTAttoAmms().remove(siacTAttoAmm);
		siacTAttoAmm.setSiacTEnteProprietario(null);

		return siacTAttoAmm;
	}

	/**
	 * Gets the siac t atto legges.
	 *
	 * @return the siac t atto legges
	 */
	public List<SiacTAttoLegge> getSiacTAttoLegges() {
		return this.siacTAttoLegges;
	}

	/**
	 * Sets the siac t atto legges.
	 *
	 * @param siacTAttoLegges the new siac t atto legges
	 */
	public void setSiacTAttoLegges(List<SiacTAttoLegge> siacTAttoLegges) {
		this.siacTAttoLegges = siacTAttoLegges;
	}

	/**
	 * Adds the siac t atto legge.
	 *
	 * @param siacTAttoLegge the siac t atto legge
	 * @return the siac t atto legge
	 */
	public SiacTAttoLegge addSiacTAttoLegge(SiacTAttoLegge siacTAttoLegge) {
		getSiacTAttoLegges().add(siacTAttoLegge);
		siacTAttoLegge.setSiacTEnteProprietario(this);

		return siacTAttoLegge;
	}

	/**
	 * Removes the siac t atto legge.
	 *
	 * @param siacTAttoLegge the siac t atto legge
	 * @return the siac t atto legge
	 */
	public SiacTAttoLegge removeSiacTAttoLegge(SiacTAttoLegge siacTAttoLegge) {
		getSiacTAttoLegges().remove(siacTAttoLegge);
		siacTAttoLegge.setSiacTEnteProprietario(null);

		return siacTAttoLegge;
	}

	/**
	 * Gets the siac t attrs.
	 *
	 * @return the siac t attrs
	 */
	public List<SiacTAttr> getSiacTAttrs() {
		return this.siacTAttrs;
	}

	/**
	 * Sets the siac t attrs.
	 *
	 * @param siacTAttrs the new siac t attrs
	 */
	public void setSiacTAttrs(List<SiacTAttr> siacTAttrs) {
		this.siacTAttrs = siacTAttrs;
	}

	/**
	 * Adds the siac t attr.
	 *
	 * @param siacTAttr the siac t attr
	 * @return the siac t attr
	 */
	public SiacTAttr addSiacTAttr(SiacTAttr siacTAttr) {
		getSiacTAttrs().add(siacTAttr);
		siacTAttr.setSiacTEnteProprietario(this);

		return siacTAttr;
	}

	/**
	 * Removes the siac t attr.
	 *
	 * @param siacTAttr the siac t attr
	 * @return the siac t attr
	 */
	public SiacTAttr removeSiacTAttr(SiacTAttr siacTAttr) {
		getSiacTAttrs().remove(siacTAttr);
		siacTAttr.setSiacTEnteProprietario(null);

		return siacTAttr;
	}

	/**
	 * Gets the siac t aziones.
	 *
	 * @return the siac t aziones
	 */
	public List<SiacTAzione> getSiacTAziones() {
		return this.siacTAziones;
	}

	/**
	 * Sets the siac t aziones.
	 *
	 * @param siacTAziones the new siac t aziones
	 */
	public void setSiacTAziones(List<SiacTAzione> siacTAziones) {
		this.siacTAziones = siacTAziones;
	}

	/**
	 * Adds the siac t azione.
	 *
	 * @param siacTAzione the siac t azione
	 * @return the siac t azione
	 */
	public SiacTAzione addSiacTAzione(SiacTAzione siacTAzione) {
		getSiacTAziones().add(siacTAzione);
		siacTAzione.setSiacTEnteProprietario(this);

		return siacTAzione;
	}

	/**
	 * Removes the siac t azione.
	 *
	 * @param siacTAzione the siac t azione
	 * @return the siac t azione
	 */
	public SiacTAzione removeSiacTAzione(SiacTAzione siacTAzione) {
		getSiacTAziones().remove(siacTAzione);
		siacTAzione.setSiacTEnteProprietario(null);

		return siacTAzione;
	}

	/**
	 * Gets the siac t azione richiestas.
	 *
	 * @return the siac t azione richiestas
	 */
	public List<SiacTAzioneRichiesta> getSiacTAzioneRichiestas() {
		return this.siacTAzioneRichiestas;
	}

	/**
	 * Sets the siac t azione richiestas.
	 *
	 * @param siacTAzioneRichiestas the new siac t azione richiestas
	 */
	public void setSiacTAzioneRichiestas(List<SiacTAzioneRichiesta> siacTAzioneRichiestas) {
		this.siacTAzioneRichiestas = siacTAzioneRichiestas;
	}

	/**
	 * Adds the siac t azione richiesta.
	 *
	 * @param siacTAzioneRichiesta the siac t azione richiesta
	 * @return the siac t azione richiesta
	 */
	public SiacTAzioneRichiesta addSiacTAzioneRichiesta(SiacTAzioneRichiesta siacTAzioneRichiesta) {
		getSiacTAzioneRichiestas().add(siacTAzioneRichiesta);
		siacTAzioneRichiesta.setSiacTEnteProprietario(this);

		return siacTAzioneRichiesta;
	}

	/**
	 * Removes the siac t azione richiesta.
	 *
	 * @param siacTAzioneRichiesta the siac t azione richiesta
	 * @return the siac t azione richiesta
	 */
	public SiacTAzioneRichiesta removeSiacTAzioneRichiesta(SiacTAzioneRichiesta siacTAzioneRichiesta) {
		getSiacTAzioneRichiestas().remove(siacTAzioneRichiesta);
		siacTAzioneRichiesta.setSiacTEnteProprietario(null);

		return siacTAzioneRichiesta;
	}

	/**
	 * Gets the siac t bils.
	 *
	 * @return the siac t bils
	 */
	public List<SiacTBil> getSiacTBils() {
		return this.siacTBils;
	}

	/**
	 * Sets the siac t bils.
	 *
	 * @param siacTBils the new siac t bils
	 */
	public void setSiacTBils(List<SiacTBil> siacTBils) {
		this.siacTBils = siacTBils;
	}

	/**
	 * Adds the siac t bil.
	 *
	 * @param siacTBil the siac t bil
	 * @return the siac t bil
	 */
	public SiacTBil addSiacTBil(SiacTBil siacTBil) {
		getSiacTBils().add(siacTBil);
		siacTBil.setSiacTEnteProprietario(this);

		return siacTBil;
	}

	/**
	 * Removes the siac t bil.
	 *
	 * @param siacTBil the siac t bil
	 * @return the siac t bil
	 */
	public SiacTBil removeSiacTBil(SiacTBil siacTBil) {
		getSiacTBils().remove(siacTBil);
		siacTBil.setSiacTEnteProprietario(null);

		return siacTBil;
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
		siacTBilElem.setSiacTEnteProprietario(this);

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
		siacTBilElem.setSiacTEnteProprietario(null);

		return siacTBilElem;
	}

	/**
	 * Gets the siac t bil elem dets.
	 *
	 * @return the siac t bil elem dets
	 */
	public List<SiacTBilElemDet> getSiacTBilElemDets() {
		return this.siacTBilElemDets;
	}

	/**
	 * Sets the siac t bil elem dets.
	 *
	 * @param siacTBilElemDets the new siac t bil elem dets
	 */
	public void setSiacTBilElemDets(List<SiacTBilElemDet> siacTBilElemDets) {
		this.siacTBilElemDets = siacTBilElemDets;
	}

	/**
	 * Adds the siac t bil elem det.
	 *
	 * @param siacTBilElemDet the siac t bil elem det
	 * @return the siac t bil elem det
	 */
	public SiacTBilElemDet addSiacTBilElemDet(SiacTBilElemDet siacTBilElemDet) {
		getSiacTBilElemDets().add(siacTBilElemDet);
		siacTBilElemDet.setSiacTEnteProprietario(this);

		return siacTBilElemDet;
	}

	/**
	 * Removes the siac t bil elem det.
	 *
	 * @param siacTBilElemDet the siac t bil elem det
	 * @return the siac t bil elem det
	 */
	public SiacTBilElemDet removeSiacTBilElemDet(SiacTBilElemDet siacTBilElemDet) {
		getSiacTBilElemDets().remove(siacTBilElemDet);
		siacTBilElemDet.setSiacTEnteProprietario(null);

		return siacTBilElemDet;
	}

	/**
	 * Gets the siac t bil elem det vars.
	 *
	 * @return the siac t bil elem det vars
	 */
	public List<SiacTBilElemDetVar> getSiacTBilElemDetVars() {
		return this.siacTBilElemDetVars;
	}

	/**
	 * Sets the siac t bil elem det vars.
	 *
	 * @param siacTBilElemDetVars the new siac t bil elem det vars
	 */
	public void setSiacTBilElemDetVars(List<SiacTBilElemDetVar> siacTBilElemDetVars) {
		this.siacTBilElemDetVars = siacTBilElemDetVars;
	}

	/**
	 * Adds the siac t bil elem det var.
	 *
	 * @param siacTBilElemDetVar the siac t bil elem det var
	 * @return the siac t bil elem det var
	 */
	public SiacTBilElemDetVar addSiacTBilElemDetVar(SiacTBilElemDetVar siacTBilElemDetVar) {
		getSiacTBilElemDetVars().add(siacTBilElemDetVar);
		siacTBilElemDetVar.setSiacTEnteProprietario(this);

		return siacTBilElemDetVar;
	}

	/**
	 * Removes the siac t bil elem det var.
	 *
	 * @param siacTBilElemDetVar the siac t bil elem det var
	 * @return the siac t bil elem det var
	 */
	public SiacTBilElemDetVar removeSiacTBilElemDetVar(SiacTBilElemDetVar siacTBilElemDetVar) {
		getSiacTBilElemDetVars().remove(siacTBilElemDetVar);
		siacTBilElemDetVar.setSiacTEnteProprietario(null);

		return siacTBilElemDetVar;
	}

	/**
	 * Gets the siac t classes.
	 *
	 * @return the siac t classes
	 */
	public List<SiacTClass> getSiacTClasses() {
		return this.siacTClasses;
	}

	/**
	 * Sets the siac t classes.
	 *
	 * @param siacTClasses the new siac t classes
	 */
	public void setSiacTClasses(List<SiacTClass> siacTClasses) {
		this.siacTClasses = siacTClasses;
	}

	/**
	 * Adds the siac t class.
	 *
	 * @param siacTClass the siac t class
	 * @return the siac t class
	 */
	public SiacTClass addSiacTClass(SiacTClass siacTClass) {
		getSiacTClasses().add(siacTClass);
		siacTClass.setSiacTEnteProprietario(this);

		return siacTClass;
	}

	/**
	 * Removes the siac t class.
	 *
	 * @param siacTClass the siac t class
	 * @return the siac t class
	 */
	public SiacTClass removeSiacTClass(SiacTClass siacTClass) {
		getSiacTClasses().remove(siacTClass);
		siacTClass.setSiacTEnteProprietario(null);

		return siacTClass;
	}

	/**
	 * Gets the siac t class fam trees.
	 *
	 * @return the siac t class fam trees
	 */
	public List<SiacTClassFamTree> getSiacTClassFamTrees() {
		return this.siacTClassFamTrees;
	}

	/**
	 * Sets the siac t class fam trees.
	 *
	 * @param siacTClassFamTrees the new siac t class fam trees
	 */
	public void setSiacTClassFamTrees(List<SiacTClassFamTree> siacTClassFamTrees) {
		this.siacTClassFamTrees = siacTClassFamTrees;
	}

	/**
	 * Adds the siac t class fam tree.
	 *
	 * @param siacTClassFamTree the siac t class fam tree
	 * @return the siac t class fam tree
	 */
	public SiacTClassFamTree addSiacTClassFamTree(SiacTClassFamTree siacTClassFamTree) {
		getSiacTClassFamTrees().add(siacTClassFamTree);
		siacTClassFamTree.setSiacTEnteProprietario(this);

		return siacTClassFamTree;
	}

	/**
	 * Removes the siac t class fam tree.
	 *
	 * @param siacTClassFamTree the siac t class fam tree
	 * @return the siac t class fam tree
	 */
	public SiacTClassFamTree removeSiacTClassFamTree(SiacTClassFamTree siacTClassFamTree) {
		getSiacTClassFamTrees().remove(siacTClassFamTree);
		siacTClassFamTree.setSiacTEnteProprietario(null);

		return siacTClassFamTree;
	}

	/**
	 * Gets the siac t gruppos.
	 *
	 * @return the siac t gruppos
	 */
	public List<SiacTGruppo> getSiacTGruppos() {
		return this.siacTGruppos;
	}

	/**
	 * Sets the siac t gruppos.
	 *
	 * @param siacTGruppos the new siac t gruppos
	 */
	public void setSiacTGruppos(List<SiacTGruppo> siacTGruppos) {
		this.siacTGruppos = siacTGruppos;
	}

	/**
	 * Adds the siac t gruppo.
	 *
	 * @param siacTGruppo the siac t gruppo
	 * @return the siac t gruppo
	 */
	public SiacTGruppo addSiacTGruppo(SiacTGruppo siacTGruppo) {
		getSiacTGruppos().add(siacTGruppo);
		siacTGruppo.setSiacTEnteProprietario(this);

		return siacTGruppo;
	}

	/**
	 * Removes the siac t gruppo.
	 *
	 * @param siacTGruppo the siac t gruppo
	 * @return the siac t gruppo
	 */
	public SiacTGruppo removeSiacTGruppo(SiacTGruppo siacTGruppo) {
		getSiacTGruppos().remove(siacTGruppo);
		siacTGruppo.setSiacTEnteProprietario(null);

		return siacTGruppo;
	}

	/**
	 * Gets the siac t parametro azione richiestas.
	 *
	 * @return the siac t parametro azione richiestas
	 */
	public List<SiacTParametroAzioneRichiesta> getSiacTParametroAzioneRichiestas() {
		return this.siacTParametroAzioneRichiestas;
	}

	/**
	 * Sets the siac t parametro azione richiestas.
	 *
	 * @param siacTParametroAzioneRichiestas the new siac t parametro azione richiestas
	 */
	public void setSiacTParametroAzioneRichiestas(List<SiacTParametroAzioneRichiesta> siacTParametroAzioneRichiestas) {
		this.siacTParametroAzioneRichiestas = siacTParametroAzioneRichiestas;
	}

	/**
	 * Adds the siac t parametro azione richiesta.
	 *
	 * @param siacTParametroAzioneRichiesta the siac t parametro azione richiesta
	 * @return the siac t parametro azione richiesta
	 */
	public SiacTParametroAzioneRichiesta addSiacTParametroAzioneRichiesta(SiacTParametroAzioneRichiesta siacTParametroAzioneRichiesta) {
		getSiacTParametroAzioneRichiestas().add(siacTParametroAzioneRichiesta);
		siacTParametroAzioneRichiesta.setSiacTEnteProprietario(this);

		return siacTParametroAzioneRichiesta;
	}

	/**
	 * Removes the siac t parametro azione richiesta.
	 *
	 * @param siacTParametroAzioneRichiesta the siac t parametro azione richiesta
	 * @return the siac t parametro azione richiesta
	 */
	public SiacTParametroAzioneRichiesta removeSiacTParametroAzioneRichiesta(SiacTParametroAzioneRichiesta siacTParametroAzioneRichiesta) {
		getSiacTParametroAzioneRichiestas().remove(siacTParametroAzioneRichiesta);
		siacTParametroAzioneRichiesta.setSiacTEnteProprietario(null);

		return siacTParametroAzioneRichiesta;
	}

	/**
	 * Gets the siac t periodos.
	 *
	 * @return the siac t periodos
	 */
	public List<SiacTPeriodo> getSiacTPeriodos() {
		return this.siacTPeriodos;
	}

	/**
	 * Sets the siac t periodos.
	 *
	 * @param siacTPeriodos the new siac t periodos
	 */
	public void setSiacTPeriodos(List<SiacTPeriodo> siacTPeriodos) {
		this.siacTPeriodos = siacTPeriodos;
	}

	/**
	 * Adds the siac t periodo.
	 *
	 * @param siacTPeriodo the siac t periodo
	 * @return the siac t periodo
	 */
	public SiacTPeriodo addSiacTPeriodo(SiacTPeriodo siacTPeriodo) {
		getSiacTPeriodos().add(siacTPeriodo);
		siacTPeriodo.setSiacTEnteProprietario(this);

		return siacTPeriodo;
	}

	/**
	 * Removes the siac t periodo.
	 *
	 * @param siacTPeriodo the siac t periodo
	 * @return the siac t periodo
	 */
	public SiacTPeriodo removeSiacTPeriodo(SiacTPeriodo siacTPeriodo) {
		getSiacTPeriodos().remove(siacTPeriodo);
		siacTPeriodo.setSiacTEnteProprietario(null);

		return siacTPeriodo;
	}

	/**
	 * Gets the siac t soggettos.
	 *
	 * @return the siac t soggettos
	 */
	public List<SiacTSoggetto> getSiacTSoggettos() {
		return this.siacTSoggettos;
	}

	/**
	 * Sets the siac t soggettos.
	 *
	 * @param siacTSoggettos the new siac t soggettos
	 */
	public void setSiacTSoggettos(List<SiacTSoggetto> siacTSoggettos) {
		this.siacTSoggettos = siacTSoggettos;
	}

	/**
	 * Adds the siac t soggetto.
	 *
	 * @param siacTSoggetto the siac t soggetto
	 * @return the siac t soggetto
	 */
	public SiacTSoggetto addSiacTSoggetto(SiacTSoggetto siacTSoggetto) {
		getSiacTSoggettos().add(siacTSoggetto);
		siacTSoggetto.setSiacTEnteProprietario(this);

		return siacTSoggetto;
	}

	/**
	 * Removes the siac t soggetto.
	 *
	 * @param siacTSoggetto the siac t soggetto
	 * @return the siac t soggetto
	 */
	public SiacTSoggetto removeSiacTSoggetto(SiacTSoggetto siacTSoggetto) {
		getSiacTSoggettos().remove(siacTSoggetto);
		siacTSoggetto.setSiacTEnteProprietario(null);

		return siacTSoggetto;
	}

	/**
	 * Gets the siac t variaziones.
	 *
	 * @return the siac t variaziones
	 */
	public List<SiacTVariazione> getSiacTVariaziones() {
		return this.siacTVariaziones;
	}

	/**
	 * Sets the siac t variaziones.
	 *
	 * @param siacTVariaziones the new siac t variaziones
	 */
	public void setSiacTVariaziones(List<SiacTVariazione> siacTVariaziones) {
		this.siacTVariaziones = siacTVariaziones;
	}

	/**
	 * Adds the siac t variazione.
	 *
	 * @param siacTVariazione the siac t variazione
	 * @return the siac t variazione
	 */
	public SiacTVariazione addSiacTVariazione(SiacTVariazione siacTVariazione) {
		getSiacTVariaziones().add(siacTVariazione);
		siacTVariazione.setSiacTEnteProprietario(this);

		return siacTVariazione;
	}

	/**
	 * Removes the siac t variazione.
	 *
	 * @param siacTVariazione the siac t variazione
	 * @return the siac t variazione
	 */
	public SiacTVariazione removeSiacTVariazione(SiacTVariazione siacTVariazione) {
		getSiacTVariaziones().remove(siacTVariazione);
		siacTVariazione.setSiacTEnteProprietario(null);

		return siacTVariazione;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return enteProprietarioId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.enteProprietarioId = uid;
		
	}

}