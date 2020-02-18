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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_atto_amm database table.
 * 
 */
@Entity
@Table(name="siac_t_atto_amm")
@NamedQuery(name="SiacTAttoAmm.findAll", query="SELECT s FROM SiacTAttoAmm s")
public class SiacTAttoAmm extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The attoamm id. */
	@Id
	@SequenceGenerator(name="SIAC_T_ATTO_AMM_ATTOAMMID_GENERATOR", allocationSize=1, sequenceName="siac_t_atto_amm_attoamm_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_ATTO_AMM_ATTOAMMID_GENERATOR")
	@Column(name="attoamm_id")
	private Integer attoammId;

	/** The attoamm anno. */
	@Column(name="attoamm_anno")
	private String attoammAnno;

	/** The attoamm note. */
	@Column(name="attoamm_note")
	private String attoammNote;

	/** The attoamm numero. */
	@Column(name="attoamm_numero")
	private Integer attoammNumero;

	/** The attoamm oggetto. */
	@Column(name="attoamm_oggetto")
	private String attoammOggetto;

	/** The attoamm temporaneo. */
	@Column(name="parere_regolarita_contabile")
	private Boolean parereRegolaritaContabile;

	@Column(name="attoamm_blocco")
	private Boolean attoammBlocco;

	@Column(name="attoamm_provenienza")
	private String attoammProvenienza;

	

	//bi-directional many-to-one association to SiacRAttoAmmClass
	/** The siac r atto amm classes. */
	@OneToMany(mappedBy="siacTAttoAmm",  cascade = {CascadeType.ALL})
	private List<SiacRAttoAmmClass> siacRAttoAmmClasses;

	//bi-directional many-to-one association to SiacRAttoAmmStato
	/** The siac r atto amm statos. */
	@OneToMany(mappedBy="siacTAttoAmm",  cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRAttoAmmStato> siacRAttoAmmStatos;

	//bi-directional many-to-one association to SiacRBilStatoOpAttoAmm
	@OneToMany(mappedBy="siacTAttoAmm")
	private List<SiacRBilStatoOpAttoAmm> siacRBilStatoOpAttoAmms;

	//bi-directional many-to-one association to SiacRCausaleAttoAmm
	@OneToMany(mappedBy="siacTAttoAmm")
	private List<SiacRCausaleAttoAmm> siacRCausaleAttoAmms;

	//bi-directional many-to-one association to SiacRLiquidazioneAttoAmm
	@OneToMany(mappedBy="siacTAttoAmm")
	private List<SiacRLiquidazioneAttoAmm> siacRLiquidazioneAttoAmms;

	//bi-directional many-to-one association to SiacRMovgestTsAttoAmm
	/** The siac r movgest ts atto amms. */
	@OneToMany(mappedBy="siacTAttoAmm")
	private List<SiacRMovgestTsAttoAmm> siacRMovgestTsAttoAmms;

	//bi-directional many-to-one association to SiacRMutuoAttoAmm
	@OneToMany(mappedBy="siacTAttoAmm")
	private List<SiacRMutuoAttoAmm> siacRMutuoAttoAmms;

	//bi-directional many-to-one association to SiacROrdinativoAttoAmm
	@OneToMany(mappedBy="siacTAttoAmm")
	private List<SiacROrdinativoAttoAmm> siacROrdinativoAttoAmms;

	//bi-directional many-to-one association to SiacRPredocAttoAmm
	@OneToMany(mappedBy="siacTAttoAmm")
	private List<SiacRPredocAttoAmm> siacRPredocAttoAmms;

	//bi-directional many-to-one association to SiacRProgrammaAttoAmm
	@OneToMany(mappedBy="siacTAttoAmm")
	private List<SiacRProgrammaAttoAmm> siacRProgrammaAttoAmms;

	//bi-directional many-to-one association to SiacRSubdocAttoAmm
	@OneToMany(mappedBy="siacTAttoAmm")
	private List<SiacRSubdocAttoAmm> siacRSubdocAttoAmms;

	//bi-directional many-to-one association to SiacRVariazioneStato
	/** The siac r variazione statos. */
	@OneToMany(mappedBy="siacTAttoAmm")
	private List<SiacRVariazioneStato> siacRVariazioneStatos;

	//bi-directional many-to-one association to SiacTAttoAllegato
	/** The siac t atto allegatos. */
	@OneToMany(mappedBy="siacTAttoAmm")
	private List<SiacTAttoAllegato> siacTAttoAllegatos;

	//bi-directional many-to-one association to SiacDAttoAmmTipo
	/** The siac d atto amm tipo. */
	@ManyToOne
	@JoinColumn(name="attoamm_tipo_id")
	private SiacDAttoAmmTipo siacDAttoAmmTipo;

	//bi-directional many-to-one association to SiacTCartacont
	@OneToMany(mappedBy="siacTAttoAmm")
	private List<SiacTCartacont> siacTCartaconts;

	//bi-directional many-to-one association to SiacTCassaEconOperaz
	@OneToMany(mappedBy="siacTAttoAmm")
	private List<SiacTCassaEconOperaz> siacTCassaEconOperazs;

	//bi-directional many-to-one association to SiacTModifica
	@OneToMany(mappedBy="siacTAttoAmm")
	private List<SiacTModifica> siacTModificas;

	/**
	 * Instantiates a new siac t atto amm.
	 */
	public SiacTAttoAmm() {
	}

	/**
	 * Gets the attoamm id.
	 *
	 * @return the attoamm id
	 */
	public Integer getAttoammId() {
		return this.attoammId;
	}

	/**
	 * Sets the attoamm id.
	 *
	 * @param attoammId the new attoamm id
	 */
	public void setAttoammId(Integer attoammId) {
		this.attoammId = attoammId;
	}

	/**
	 * Gets the attoamm anno.
	 *
	 * @return the attoamm anno
	 */
	public String getAttoammAnno() {
		return this.attoammAnno;
	}

	/**
	 * Sets the attoamm anno.
	 *
	 * @param attoammAnno the new attoamm anno
	 */
	public void setAttoammAnno(String attoammAnno) {
		this.attoammAnno = attoammAnno;
	}

	/**
	 * Gets the attoamm note.
	 *
	 * @return the attoamm note
	 */
	public String getAttoammNote() {
		return this.attoammNote;
	}

	/**
	 * Sets the attoamm note.
	 *
	 * @param attoammNote the new attoamm note
	 */
	public void setAttoammNote(String attoammNote) {
		this.attoammNote = attoammNote;
	}

	/**
	 * Gets the attoamm numero.
	 *
	 * @return the attoamm numero
	 */
	public Integer getAttoammNumero() {
		return this.attoammNumero;
	}

	/**
	 * Sets the attoamm numero.
	 *
	 * @param attoammNumero the new attoamm numero
	 */
	public void setAttoammNumero(Integer attoammNumero) {
		this.attoammNumero = attoammNumero;
	}

	/**
	 * Gets the attoamm oggetto.
	 *
	 * @return the attoamm oggetto
	 */
	public String getAttoammOggetto() {
		return this.attoammOggetto;
	}

	/**
	 * Sets the attoamm oggetto.
	 *
	 * @param attoammOggetto the new attoamm oggetto
	 */
	public void setAttoammOggetto(String attoammOggetto) {
		this.attoammOggetto = attoammOggetto;
	}

	/**
	 * Gets the parere regolarita contabile.
	 * 
	 * @return the parereRegolaritaContabile
	 */
	public Boolean getParereRegolaritaContabile() {
		return parereRegolaritaContabile;
	}

	/**
	 * Sets the parere regolarita contabile.
	 * 
	 * @param parereRegolaritaContabile the parereRegolaritaContabile to set
	 */
	public void setParereRegolaritaContabile(Boolean parereRegolaritaContabile) {
		this.parereRegolaritaContabile = parereRegolaritaContabile;
	}

	/**
	 * Gets the siac r atto amm classes.
	 *
	 * @return the siac r atto amm classes
	 */
	public List<SiacRAttoAmmClass> getSiacRAttoAmmClasses() {
		return this.siacRAttoAmmClasses;
	}

	/**
	 * Sets the siac r atto amm classes.
	 *
	 * @param siacRAttoAmmClasses the new siac r atto amm classes
	 */
	public void setSiacRAttoAmmClasses(List<SiacRAttoAmmClass> siacRAttoAmmClasses) {
		this.siacRAttoAmmClasses = siacRAttoAmmClasses;
	}

	/**
	 * Adds the siac r atto amm class.
	 *
	 * @param siacRAttoAmmClass the siac r atto amm class
	 * @return the siac r atto amm class
	 */
	public SiacRAttoAmmClass addSiacRAttoAmmClass(SiacRAttoAmmClass siacRAttoAmmClass) {
		getSiacRAttoAmmClasses().add(siacRAttoAmmClass);
		siacRAttoAmmClass.setSiacTAttoAmm(this);

		return siacRAttoAmmClass;
	}

	/**
	 * Removes the siac r atto amm class.
	 *
	 * @param siacRAttoAmmClass the siac r atto amm class
	 * @return the siac r atto amm class
	 */
	public SiacRAttoAmmClass removeSiacRAttoAmmClass(SiacRAttoAmmClass siacRAttoAmmClass) {
		getSiacRAttoAmmClasses().remove(siacRAttoAmmClass);
		siacRAttoAmmClass.setSiacTAttoAmm(null);

		return siacRAttoAmmClass;
	}

	/**
	 * Gets the siac r atto amm statos.
	 *
	 * @return the siac r atto amm statos
	 */
	public List<SiacRAttoAmmStato> getSiacRAttoAmmStatos() {
		return this.siacRAttoAmmStatos;
	}

	/**
	 * Sets the siac r atto amm statos.
	 *
	 * @param siacRAttoAmmStatos the new siac r atto amm statos
	 */
	public void setSiacRAttoAmmStatos(List<SiacRAttoAmmStato> siacRAttoAmmStatos) {
		this.siacRAttoAmmStatos = siacRAttoAmmStatos;
	}

	/**
	 * Adds the siac r atto amm stato.
	 *
	 * @param siacRAttoAmmStato the siac r atto amm stato
	 * @return the siac r atto amm stato
	 */
	public SiacRAttoAmmStato addSiacRAttoAmmStato(SiacRAttoAmmStato siacRAttoAmmStato) {
		getSiacRAttoAmmStatos().add(siacRAttoAmmStato);
		siacRAttoAmmStato.setSiacTAttoAmm(this);

		return siacRAttoAmmStato;
	}

	/**
	 * Removes the siac r atto amm stato.
	 *
	 * @param siacRAttoAmmStato the siac r atto amm stato
	 * @return the siac r atto amm stato
	 */
	public SiacRAttoAmmStato removeSiacRAttoAmmStato(SiacRAttoAmmStato siacRAttoAmmStato) {
		getSiacRAttoAmmStatos().remove(siacRAttoAmmStato);
		siacRAttoAmmStato.setSiacTAttoAmm(null);

		return siacRAttoAmmStato;
	}

	public List<SiacRBilStatoOpAttoAmm> getSiacRBilStatoOpAttoAmms() {
		return this.siacRBilStatoOpAttoAmms;
	}

	public void setSiacRBilStatoOpAttoAmms(List<SiacRBilStatoOpAttoAmm> siacRBilStatoOpAttoAmms) {
		this.siacRBilStatoOpAttoAmms = siacRBilStatoOpAttoAmms;
	}

	public SiacRBilStatoOpAttoAmm addSiacRBilStatoOpAttoAmm(SiacRBilStatoOpAttoAmm siacRBilStatoOpAttoAmm) {
		getSiacRBilStatoOpAttoAmms().add(siacRBilStatoOpAttoAmm);
		siacRBilStatoOpAttoAmm.setSiacTAttoAmm(this);

		return siacRBilStatoOpAttoAmm;
	}

	public SiacRBilStatoOpAttoAmm removeSiacRBilStatoOpAttoAmm(SiacRBilStatoOpAttoAmm siacRBilStatoOpAttoAmm) {
		getSiacRBilStatoOpAttoAmms().remove(siacRBilStatoOpAttoAmm);
		siacRBilStatoOpAttoAmm.setSiacTAttoAmm(null);

		return siacRBilStatoOpAttoAmm;
	}

	public List<SiacRCausaleAttoAmm> getSiacRCausaleAttoAmms() {
		return this.siacRCausaleAttoAmms;
	}

	public void setSiacRCausaleAttoAmms(List<SiacRCausaleAttoAmm> siacRCausaleAttoAmms) {
		this.siacRCausaleAttoAmms = siacRCausaleAttoAmms;
	}

	public SiacRCausaleAttoAmm addSiacRCausaleAttoAmm(SiacRCausaleAttoAmm siacRCausaleAttoAmm) {
		getSiacRCausaleAttoAmms().add(siacRCausaleAttoAmm);
		siacRCausaleAttoAmm.setSiacTAttoAmm(this);

		return siacRCausaleAttoAmm;
	}

	public SiacRCausaleAttoAmm removeSiacRCausaleAttoAmm(SiacRCausaleAttoAmm siacRCausaleAttoAmm) {
		getSiacRCausaleAttoAmms().remove(siacRCausaleAttoAmm);
		siacRCausaleAttoAmm.setSiacTAttoAmm(null);

		return siacRCausaleAttoAmm;
	}

	public List<SiacRLiquidazioneAttoAmm> getSiacRLiquidazioneAttoAmms() {
		return this.siacRLiquidazioneAttoAmms;
	}

	public void setSiacRLiquidazioneAttoAmms(List<SiacRLiquidazioneAttoAmm> siacRLiquidazioneAttoAmms) {
		this.siacRLiquidazioneAttoAmms = siacRLiquidazioneAttoAmms;
	}

	public SiacRLiquidazioneAttoAmm addSiacRLiquidazioneAttoAmm(SiacRLiquidazioneAttoAmm siacRLiquidazioneAttoAmm) {
		getSiacRLiquidazioneAttoAmms().add(siacRLiquidazioneAttoAmm);
		siacRLiquidazioneAttoAmm.setSiacTAttoAmm(this);

		return siacRLiquidazioneAttoAmm;
	}

	public SiacRLiquidazioneAttoAmm removeSiacRLiquidazioneAttoAmm(SiacRLiquidazioneAttoAmm siacRLiquidazioneAttoAmm) {
		getSiacRLiquidazioneAttoAmms().remove(siacRLiquidazioneAttoAmm);
		siacRLiquidazioneAttoAmm.setSiacTAttoAmm(null);

		return siacRLiquidazioneAttoAmm;
	}

	/**
	 * Gets the siac r movgest ts atto amms.
	 *
	 * @return the siac r movgest ts atto amms
	 */
	public List<SiacRMovgestTsAttoAmm> getSiacRMovgestTsAttoAmms() {
		return this.siacRMovgestTsAttoAmms;
	}

	/**
	 * Sets the siac r movgest ts atto amms.
	 *
	 * @param siacRMovgestTsAttoAmms the new siac r movgest ts atto amms
	 */
	public void setSiacRMovgestTsAttoAmms(List<SiacRMovgestTsAttoAmm> siacRMovgestTsAttoAmms) {
		this.siacRMovgestTsAttoAmms = siacRMovgestTsAttoAmms;
	}

	/**
	 * Adds the siac r movgest ts atto amm.
	 *
	 * @param siacRMovgestTsAttoAmm the siac r movgest ts atto amm
	 * @return the siac r movgest ts atto amm
	 */
	public SiacRMovgestTsAttoAmm addSiacRMovgestTsAttoAmm(SiacRMovgestTsAttoAmm siacRMovgestTsAttoAmm) {
		getSiacRMovgestTsAttoAmms().add(siacRMovgestTsAttoAmm);
		siacRMovgestTsAttoAmm.setSiacTAttoAmm(this);

		return siacRMovgestTsAttoAmm;
	}

	/**
	 * Removes the siac r movgest ts atto amm.
	 *
	 * @param siacRMovgestTsAttoAmm the siac r movgest ts atto amm
	 * @return the siac r movgest ts atto amm
	 */
	public SiacRMovgestTsAttoAmm removeSiacRMovgestTsAttoAmm(SiacRMovgestTsAttoAmm siacRMovgestTsAttoAmm) {
		getSiacRMovgestTsAttoAmms().remove(siacRMovgestTsAttoAmm);
		siacRMovgestTsAttoAmm.setSiacTAttoAmm(null);

		return siacRMovgestTsAttoAmm;
	}

	public List<SiacRMutuoAttoAmm> getSiacRMutuoAttoAmms() {
		return this.siacRMutuoAttoAmms;
	}

	public void setSiacRMutuoAttoAmms(List<SiacRMutuoAttoAmm> siacRMutuoAttoAmms) {
		this.siacRMutuoAttoAmms = siacRMutuoAttoAmms;
	}

	public SiacRMutuoAttoAmm addSiacRMutuoAttoAmm(SiacRMutuoAttoAmm siacRMutuoAttoAmm) {
		getSiacRMutuoAttoAmms().add(siacRMutuoAttoAmm);
		siacRMutuoAttoAmm.setSiacTAttoAmm(this);

		return siacRMutuoAttoAmm;
	}

	public SiacRMutuoAttoAmm removeSiacRMutuoAttoAmm(SiacRMutuoAttoAmm siacRMutuoAttoAmm) {
		getSiacRMutuoAttoAmms().remove(siacRMutuoAttoAmm);
		siacRMutuoAttoAmm.setSiacTAttoAmm(null);

		return siacRMutuoAttoAmm;
	}

	public List<SiacROrdinativoAttoAmm> getSiacROrdinativoAttoAmms() {
		return this.siacROrdinativoAttoAmms;
	}

	public void setSiacROrdinativoAttoAmms(List<SiacROrdinativoAttoAmm> siacROrdinativoAttoAmms) {
		this.siacROrdinativoAttoAmms = siacROrdinativoAttoAmms;
	}

	public SiacROrdinativoAttoAmm addSiacROrdinativoAttoAmm(SiacROrdinativoAttoAmm siacROrdinativoAttoAmm) {
		getSiacROrdinativoAttoAmms().add(siacROrdinativoAttoAmm);
		siacROrdinativoAttoAmm.setSiacTAttoAmm(this);

		return siacROrdinativoAttoAmm;
	}

	public SiacROrdinativoAttoAmm removeSiacROrdinativoAttoAmm(SiacROrdinativoAttoAmm siacROrdinativoAttoAmm) {
		getSiacROrdinativoAttoAmms().remove(siacROrdinativoAttoAmm);
		siacROrdinativoAttoAmm.setSiacTAttoAmm(null);

		return siacROrdinativoAttoAmm;
	}

	public List<SiacRPredocAttoAmm> getSiacRPredocAttoAmms() {
		return this.siacRPredocAttoAmms;
	}

	public void setSiacRPredocAttoAmms(List<SiacRPredocAttoAmm> siacRPredocAttoAmms) {
		this.siacRPredocAttoAmms = siacRPredocAttoAmms;
	}

	public SiacRPredocAttoAmm addSiacRPredocAttoAmm(SiacRPredocAttoAmm siacRPredocAttoAmm) {
		getSiacRPredocAttoAmms().add(siacRPredocAttoAmm);
		siacRPredocAttoAmm.setSiacTAttoAmm(this);

		return siacRPredocAttoAmm;
	}

	public SiacRPredocAttoAmm removeSiacRPredocAttoAmm(SiacRPredocAttoAmm siacRPredocAttoAmm) {
		getSiacRPredocAttoAmms().remove(siacRPredocAttoAmm);
		siacRPredocAttoAmm.setSiacTAttoAmm(null);

		return siacRPredocAttoAmm;
	}

	public List<SiacRProgrammaAttoAmm> getSiacRProgrammaAttoAmms() {
		return this.siacRProgrammaAttoAmms;
	}

	public void setSiacRProgrammaAttoAmms(List<SiacRProgrammaAttoAmm> siacRProgrammaAttoAmms) {
		this.siacRProgrammaAttoAmms = siacRProgrammaAttoAmms;
	}

	public SiacRProgrammaAttoAmm addSiacRProgrammaAttoAmm(SiacRProgrammaAttoAmm siacRProgrammaAttoAmm) {
		getSiacRProgrammaAttoAmms().add(siacRProgrammaAttoAmm);
		siacRProgrammaAttoAmm.setSiacTAttoAmm(this);

		return siacRProgrammaAttoAmm;
	}

	public SiacRProgrammaAttoAmm removeSiacRProgrammaAttoAmm(SiacRProgrammaAttoAmm siacRProgrammaAttoAmm) {
		getSiacRProgrammaAttoAmms().remove(siacRProgrammaAttoAmm);
		siacRProgrammaAttoAmm.setSiacTAttoAmm(null);

		return siacRProgrammaAttoAmm;
	}

	public List<SiacRSubdocAttoAmm> getSiacRSubdocAttoAmms() {
		return this.siacRSubdocAttoAmms;
	}

	public void setSiacRSubdocAttoAmms(List<SiacRSubdocAttoAmm> siacRSubdocAttoAmms) {
		this.siacRSubdocAttoAmms = siacRSubdocAttoAmms;
	}

	public SiacRSubdocAttoAmm addSiacRSubdocAttoAmm(SiacRSubdocAttoAmm siacRSubdocAttoAmm) {
		getSiacRSubdocAttoAmms().add(siacRSubdocAttoAmm);
		siacRSubdocAttoAmm.setSiacTAttoAmm(this);

		return siacRSubdocAttoAmm;
	}

	public SiacRSubdocAttoAmm removeSiacRSubdocAttoAmm(SiacRSubdocAttoAmm siacRSubdocAttoAmm) {
		getSiacRSubdocAttoAmms().remove(siacRSubdocAttoAmm);
		siacRSubdocAttoAmm.setSiacTAttoAmm(null);

		return siacRSubdocAttoAmm;
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
		siacRVariazioneStato.setSiacTAttoAmm(this);

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
		siacRVariazioneStato.setSiacTAttoAmm(null);

		return siacRVariazioneStato;
	}

	public List<SiacTAttoAllegato> getSiacTAttoAllegatos() {
		return this.siacTAttoAllegatos;
	}

	public void setSiacTAttoAllegatos(List<SiacTAttoAllegato> siacTAttoAllegatos) {
		this.siacTAttoAllegatos = siacTAttoAllegatos;
	}

	public SiacTAttoAllegato addSiacTAttoAllegato(SiacTAttoAllegato siacTAttoAllegato) {
		getSiacTAttoAllegatos().add(siacTAttoAllegato);
		siacTAttoAllegato.setSiacTAttoAmm(this);

		return siacTAttoAllegato;
	}

	public SiacTAttoAllegato removeSiacTAttoAllegato(SiacTAttoAllegato siacTAttoAllegato) {
		getSiacTAttoAllegatos().remove(siacTAttoAllegato);
		siacTAttoAllegato.setSiacTAttoAmm(null);

		return siacTAttoAllegato;
	}

	/**
	 * Gets the siac d atto amm tipo.
	 *
	 * @return the siac d atto amm tipo
	 */
	public SiacDAttoAmmTipo getSiacDAttoAmmTipo() {
		return this.siacDAttoAmmTipo;
	}

	/**
	 * Sets the siac d atto amm tipo.
	 *
	 * @param siacDAttoAmmTipo the new siac d atto amm tipo
	 */
	public void setSiacDAttoAmmTipo(SiacDAttoAmmTipo siacDAttoAmmTipo) {
		this.siacDAttoAmmTipo = siacDAttoAmmTipo;
	}

	public List<SiacTCartacont> getSiacTCartaconts() {
		return this.siacTCartaconts;
	}

	public void setSiacTCartaconts(List<SiacTCartacont> siacTCartaconts) {
		this.siacTCartaconts = siacTCartaconts;
	}

	public SiacTCartacont addSiacTCartacont(SiacTCartacont siacTCartacont) {
		getSiacTCartaconts().add(siacTCartacont);
		siacTCartacont.setSiacTAttoAmm(this);

		return siacTCartacont;
	}

	public SiacTCartacont removeSiacTCartacont(SiacTCartacont siacTCartacont) {
		getSiacTCartaconts().remove(siacTCartacont);
		siacTCartacont.setSiacTAttoAmm(null);

		return siacTCartacont;
	}

	public List<SiacTCassaEconOperaz> getSiacTCassaEconOperazs() {
		return this.siacTCassaEconOperazs;
	}

	public void setSiacTCassaEconOperazs(List<SiacTCassaEconOperaz> siacTCassaEconOperazs) {
		this.siacTCassaEconOperazs = siacTCassaEconOperazs;
	}

	public SiacTCassaEconOperaz addSiacTCassaEconOperaz(SiacTCassaEconOperaz siacTCassaEconOperaz) {
		getSiacTCassaEconOperazs().add(siacTCassaEconOperaz);
		siacTCassaEconOperaz.setSiacTAttoAmm(this);

		return siacTCassaEconOperaz;
	}

	public SiacTCassaEconOperaz removeSiacTCassaEconOperaz(SiacTCassaEconOperaz siacTCassaEconOperaz) {
		getSiacTCassaEconOperazs().remove(siacTCassaEconOperaz);
		siacTCassaEconOperaz.setSiacTAttoAmm(null);

		return siacTCassaEconOperaz;
	}

	public List<SiacTModifica> getSiacTModificas() {
		return this.siacTModificas;
	}

	public void setSiacTModificas(List<SiacTModifica> siacTModificas) {
		this.siacTModificas = siacTModificas;
	}

	public SiacTModifica addSiacTModifica(SiacTModifica siacTModifica) {
		getSiacTModificas().add(siacTModifica);
		siacTModifica.setSiacTAttoAmm(this);

		return siacTModifica;
	}

	public SiacTModifica removeSiacTModifica(SiacTModifica siacTModifica) {
		getSiacTModificas().remove(siacTModifica);
		siacTModifica.setSiacTAttoAmm(null);

		return siacTModifica;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return attoammId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		attoammId = uid;
	}

	public Boolean getAttoammBlocco() {
		return attoammBlocco;
	}

	public void setAttoammBlocco(Boolean attoammBlocco) {
		this.attoammBlocco = attoammBlocco;
	}

	public String getAttoammProvenienza() {
		return attoammProvenienza;
	}

	public void setAttoammProvenienza(String attoammProvenienza) {
		this.attoammProvenienza = attoammProvenienza;
	}
}