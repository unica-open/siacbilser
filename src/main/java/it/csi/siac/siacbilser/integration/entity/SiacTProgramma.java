/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.Date;
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
 * The persistent class for the siac_t_programma database table.
 * 
 */
@Entity
@Table(name="siac_t_programma")
@NamedQuery(name="SiacTProgramma.findAll", query="SELECT s FROM SiacTProgramma s")
public class SiacTProgramma extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The programma id. */
	@Id
	@SequenceGenerator(name="SIAC_T_PROGRAMMA_PROGRAMMAID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_PROGRAMMA_PROGRAMMA_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_PROGRAMMA_PROGRAMMAID_GENERATOR")
	@Column(name="programma_id")
	private Integer programmaId;

	/** The programma code. */
	@Column(name="programma_code")
	private String programmaCode;

	/** The programma desc. */
	@Column(name="programma_desc")
	private String programmaDesc;
	
	@Column(name="investimento_in_definizione")
	private Boolean investimentoInDefinizione;
	
	@Column(name="programma_data_gara_aggiudicazione")
	private Date programmaDataGaraAggiudicazione;

	@Column(name="programma_data_gara_indizione")
	private Date programmaDataGaraIndizione;
	
	/** The programmaRUP desc. */
	@Column(name="programma_responsabile_unico")
	private String programmaResponsabileUnico;
	
	@Column(name="programma_spazi_finanziari")
	private Boolean programmaSpaziFinanziari;
	
	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBil siacTBil;
	
	//bi-directional many-to-one association to SiacRMovgestTsProgramma
	/** The siac r movgest ts programmas. */
	@OneToMany(mappedBy="siacTProgramma")
	private List<SiacRMovgestTsProgramma> siacRMovgestTsProgrammas;

	//bi-directional many-to-one association to SiacRProgrammaAttoAmm
	/** The siac r programma atto amms. */
	@OneToMany(mappedBy="siacTProgramma", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRProgrammaAttoAmm> siacRProgrammaAttoAmms;

	//bi-directional many-to-one association to SiacRProgrammaAttr
	/** The siac r programma attrs. */
	@OneToMany(mappedBy="siacTProgramma", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRProgrammaAttr> siacRProgrammaAttrs;

	//bi-directional many-to-one association to SiacRProgrammaClass
	/** The siac r programma classes. */
	@OneToMany(mappedBy="siacTProgramma", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRProgrammaClass> siacRProgrammaClasses;

	//bi-directional many-to-one association to SiacRProgrammaStato
	/** The siac r programma statos. */
	@OneToMany(mappedBy="siacTProgramma", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRProgrammaStato> siacRProgrammaStatos;

	//bi-directional many-to-one association to SiacTCronop
	/** The siac t cronops. */
	@OneToMany(mappedBy="siacTProgramma")
	private List<SiacTCronop> siacTCronops;
	
	@ManyToOne
	@JoinColumn(name="programma_affidamento_id")
	private SiacDProgrammaAffidamento siacDProgrammaAffidamento;
	
	@ManyToOne
	@JoinColumn(name="programma_tipo_id")
	private SiacDProgrammaTipo siacDProgrammaTipo;
	


	@OneToMany(mappedBy="siacTProgramma")
	private List<SiacRMutuoProgramma> siacRMutuoProgramma;	
	/**
	 * Instantiates a new siac t programma.
	 */
	public SiacTProgramma() {
	}

	/**
	 * Gets the programma id.
	 *
	 * @return the programma id
	 */
	public Integer getProgrammaId() {
		return this.programmaId;
	}

	/**
	 * Sets the programma id.
	 *
	 * @param programmaId the new programma id
	 */
	public void setProgrammaId(Integer programmaId) {
		this.programmaId = programmaId;
	}

	/**
	 * Gets the programma code.
	 *
	 * @return the programma code
	 */
	public String getProgrammaCode() {
		return this.programmaCode;
	}

	/**
	 * Sets the programma code.
	 *
	 * @param programmaCode the new programma code
	 */
	public void setProgrammaCode(String programmaCode) {
		this.programmaCode = programmaCode;
	}

	/**
	 * Gets the programma desc.
	 *
	 * @return the programma desc
	 */
	public String getProgrammaDesc() {
		return this.programmaDesc;
	}

	/**
	 * Sets the programma desc.
	 *
	 * @param programmaDesc the new programma desc
	 */
	public void setProgrammaDesc(String programmaDesc) {
		this.programmaDesc = programmaDesc;
	}
	
	/**
	 * Gets the siac r movgest ts programmas.
	 *
	 * @return the siac r movgest ts programmas
	 */
	public List<SiacRMovgestTsProgramma> getSiacRMovgestTsProgrammas() {
		return this.siacRMovgestTsProgrammas;
	}

	/**
	 * Sets the siac r movgest ts programmas.
	 *
	 * @param siacRMovgestTsProgrammas the new siac r movgest ts programmas
	 */
	public void setSiacRMovgestTsProgrammas(List<SiacRMovgestTsProgramma> siacRMovgestTsProgrammas) {
		this.siacRMovgestTsProgrammas = siacRMovgestTsProgrammas;
	}

	/**
	 * Adds the siac r movgest ts programma.
	 *
	 * @param siacRMovgestTsProgramma the siac r movgest ts programma
	 * @return the siac r movgest ts programma
	 */
	public SiacRMovgestTsProgramma addSiacRMovgestTsProgramma(SiacRMovgestTsProgramma siacRMovgestTsProgramma) {
		getSiacRMovgestTsProgrammas().add(siacRMovgestTsProgramma);
		siacRMovgestTsProgramma.setSiacTProgramma(this);

		return siacRMovgestTsProgramma;
	}

	/**
	 * Removes the siac r movgest ts programma.
	 *
	 * @param siacRMovgestTsProgramma the siac r movgest ts programma
	 * @return the siac r movgest ts programma
	 */
	public SiacRMovgestTsProgramma removeSiacRMovgestTsProgramma(SiacRMovgestTsProgramma siacRMovgestTsProgramma) {
		getSiacRMovgestTsProgrammas().remove(siacRMovgestTsProgramma);
		siacRMovgestTsProgramma.setSiacTProgramma(null);

		return siacRMovgestTsProgramma;
	}

	/**
	 * Gets the siac r programma atto amms.
	 *
	 * @return the siac r programma atto amms
	 */
	public List<SiacRProgrammaAttoAmm> getSiacRProgrammaAttoAmms() {
		return this.siacRProgrammaAttoAmms;
	}

	/**
	 * Sets the siac r programma atto amms.
	 *
	 * @param siacRProgrammaAttoAmms the new siac r programma atto amms
	 */
	public void setSiacRProgrammaAttoAmms(List<SiacRProgrammaAttoAmm> siacRProgrammaAttoAmms) {
		this.siacRProgrammaAttoAmms = siacRProgrammaAttoAmms;
	}

	/**
	 * Adds the siac r programma atto amm.
	 *
	 * @param siacRProgrammaAttoAmm the siac r programma atto amm
	 * @return the siac r programma atto amm
	 */
	public SiacRProgrammaAttoAmm addSiacRProgrammaAttoAmm(SiacRProgrammaAttoAmm siacRProgrammaAttoAmm) {
		getSiacRProgrammaAttoAmms().add(siacRProgrammaAttoAmm);
		siacRProgrammaAttoAmm.setSiacTProgramma(this);

		return siacRProgrammaAttoAmm;
	}

	/**
	 * Removes the siac r programma atto amm.
	 *
	 * @param siacRProgrammaAttoAmm the siac r programma atto amm
	 * @return the siac r programma atto amm
	 */
	public SiacRProgrammaAttoAmm removeSiacRProgrammaAttoAmm(SiacRProgrammaAttoAmm siacRProgrammaAttoAmm) {
		getSiacRProgrammaAttoAmms().remove(siacRProgrammaAttoAmm);
		siacRProgrammaAttoAmm.setSiacTProgramma(null);

		return siacRProgrammaAttoAmm;
	}

	/**
	 * Gets the siac r programma attrs.
	 *
	 * @return the siac r programma attrs
	 */
	public List<SiacRProgrammaAttr> getSiacRProgrammaAttrs() {
		return this.siacRProgrammaAttrs;
	}

	/**
	 * Sets the siac r programma attrs.
	 *
	 * @param siacRProgrammaAttrs the new siac r programma attrs
	 */
	public void setSiacRProgrammaAttrs(List<SiacRProgrammaAttr> siacRProgrammaAttrs) {
		this.siacRProgrammaAttrs = siacRProgrammaAttrs;
	}

	/**
	 * Adds the siac r programma attr.
	 *
	 * @param siacRProgrammaAttr the siac r programma attr
	 * @return the siac r programma attr
	 */
	public SiacRProgrammaAttr addSiacRProgrammaAttr(SiacRProgrammaAttr siacRProgrammaAttr) {
		getSiacRProgrammaAttrs().add(siacRProgrammaAttr);
		siacRProgrammaAttr.setSiacTProgramma(this);

		return siacRProgrammaAttr;
	}

	/**
	 * Removes the siac r programma attr.
	 *
	 * @param siacRProgrammaAttr the siac r programma attr
	 * @return the siac r programma attr
	 */
	public SiacRProgrammaAttr removeSiacRProgrammaAttr(SiacRProgrammaAttr siacRProgrammaAttr) {
		getSiacRProgrammaAttrs().remove(siacRProgrammaAttr);
		siacRProgrammaAttr.setSiacTProgramma(null);

		return siacRProgrammaAttr;
	}

	/**
	 * Gets the siac r programma classes.
	 *
	 * @return the siac r programma classes
	 */
	public List<SiacRProgrammaClass> getSiacRProgrammaClasses() {
		return this.siacRProgrammaClasses;
	}

	/**
	 * Sets the siac r programma classes.
	 *
	 * @param siacRProgrammaClasses the new siac r programma classes
	 */
	public void setSiacRProgrammaClasses(List<SiacRProgrammaClass> siacRProgrammaClasses) {
		this.siacRProgrammaClasses = siacRProgrammaClasses;
	}

	/**
	 * Adds the siac r programma class.
	 *
	 * @param siacRProgrammaClass the siac r programma class
	 * @return the siac r programma class
	 */
	public SiacRProgrammaClass addSiacRProgrammaClass(SiacRProgrammaClass siacRProgrammaClass) {
		getSiacRProgrammaClasses().add(siacRProgrammaClass);
		siacRProgrammaClass.setSiacTProgramma(this);

		return siacRProgrammaClass;
	}

	/**
	 * Removes the siac r programma class.
	 *
	 * @param siacRProgrammaClass the siac r programma class
	 * @return the siac r programma class
	 */
	public SiacRProgrammaClass removeSiacRProgrammaClass(SiacRProgrammaClass siacRProgrammaClass) {
		getSiacRProgrammaClasses().remove(siacRProgrammaClass);
		siacRProgrammaClass.setSiacTProgramma(null);

		return siacRProgrammaClass;
	}

	/**
	 * Gets the siac r programma statos.
	 *
	 * @return the siac r programma statos
	 */
	public List<SiacRProgrammaStato> getSiacRProgrammaStatos() {
		return this.siacRProgrammaStatos;
	}

	/**
	 * Sets the siac r programma statos.
	 *
	 * @param siacRProgrammaStatos the new siac r programma statos
	 */
	public void setSiacRProgrammaStatos(List<SiacRProgrammaStato> siacRProgrammaStatos) {
		this.siacRProgrammaStatos = siacRProgrammaStatos;
	}

	/**
	 * Adds the siac r programma stato.
	 *
	 * @param siacRProgrammaStato the siac r programma stato
	 * @return the siac r programma stato
	 */
	public SiacRProgrammaStato addSiacRProgrammaStato(SiacRProgrammaStato siacRProgrammaStato) {
		getSiacRProgrammaStatos().add(siacRProgrammaStato);
		siacRProgrammaStato.setSiacTProgramma(this);

		return siacRProgrammaStato;
	}

	/**
	 * Removes the siac r programma stato.
	 *
	 * @param siacRProgrammaStato the siac r programma stato
	 * @return the siac r programma stato
	 */
	public SiacRProgrammaStato removeSiacRProgrammaStato(SiacRProgrammaStato siacRProgrammaStato) {
		getSiacRProgrammaStatos().remove(siacRProgrammaStato);
		siacRProgrammaStato.setSiacTProgramma(null);

		return siacRProgrammaStato;
	}

	/**
	 * Gets the siac t cronops.
	 *
	 * @return the siac t cronops
	 */
	public List<SiacTCronop> getSiacTCronops() {
		return this.siacTCronops;
	}

	/**
	 * Sets the siac t cronops.
	 *
	 * @param siacTCronops the new siac t cronops
	 */
	public void setSiacTCronops(List<SiacTCronop> siacTCronops) {
		this.siacTCronops = siacTCronops;
	}

	/**
	 * Adds the siac t cronop.
	 *
	 * @param siacTCronop the siac t cronop
	 * @return the siac t cronop
	 */
	public SiacTCronop addSiacTCronop(SiacTCronop siacTCronop) {
		getSiacTCronops().add(siacTCronop);
		siacTCronop.setSiacTProgramma(this);

		return siacTCronop;
	}

	/**
	 * Removes the siac t cronop.
	 *
	 * @param siacTCronop the siac t cronop
	 * @return the siac t cronop
	 */
	public SiacTCronop removeSiacTCronop(SiacTCronop siacTCronop) {
		getSiacTCronops().remove(siacTCronop);
		siacTCronop.setSiacTProgramma(null);

		return siacTCronop;
	}

	/**
	 * @return the investimentoInDefinizione
	 */
	public Boolean getInvestimentoInDefinizione() {
		return investimentoInDefinizione;
	}

	/**
	 * @param investimentoInDefinizione the investimentoInDefinizione to set
	 */
	public void setInvestimentoInDefinizione(Boolean investimentoInDefinizione) {
		this.investimentoInDefinizione = investimentoInDefinizione;
	}

	/**
	 * @return the programmaDataGaraAggiudicazione
	 */
	public Date getProgrammaDataGaraAggiudicazione() {
		return programmaDataGaraAggiudicazione;
	}

	/**
	 * @param programmaDataGaraAggiudicazione the programmaDataGaraAggiudicazione to set
	 */
	public void setProgrammaDataGaraAggiudicazione(Date programmaDataGaraAggiudicazione) {
		this.programmaDataGaraAggiudicazione = programmaDataGaraAggiudicazione;
	}

	/**
	 * @return the programmaDataGaraIndizione
	 */
	public Date getProgrammaDataGaraIndizione() {
		return programmaDataGaraIndizione;
	}

	/**
	 * @param programmaDataGaraIndizione the programmaDataGaraIndizione to set
	 */
	public void setProgrammaDataGaraIndizione(Date programmaDataGaraIndizione) {
		this.programmaDataGaraIndizione = programmaDataGaraIndizione;
	}
	
	/**
	 * @return the programmaResponsabileUnico
	 */
	public String getProgrammaResponsabileUnico() {
		return programmaResponsabileUnico;
	}

	/**
	 * @param programmaResponsabileUnico the programmaResponsabileUnico to set
	 */
	public void setProgrammaResponsabileUnico(String programmaResponsabileUnico) {
		this.programmaResponsabileUnico = programmaResponsabileUnico;
	}

	/**
	 * @return the programmaSpazioFinanziario
	 */
	public Boolean getProgrammaSpaziFinanziari() {
		return programmaSpaziFinanziari;
	}

	/**
	 * Sets the programma spazi finanziari.
	 *
	 * @param programmaSpaziFinanziari the new programma spazi finanziari
	 */
	public void setProgrammaSpaziFinanziari(Boolean programmaSpaziFinanziari) {
		this.programmaSpaziFinanziari = programmaSpaziFinanziari;
	}
	
	/**
	 * @return the siacTBil
	 */
	public SiacTBil getSiacTBil() {
		return siacTBil;
	}

	/**
	 * @param siacTBil the siacTBil to set
	 */
	public void setSiacTBil(SiacTBil siacTBil) {
		this.siacTBil = siacTBil;
	}

	/**
	 * @return the siacDProgrammaAffidamento
	 */
	public SiacDProgrammaAffidamento getSiacDProgrammaAffidamento() {
		return siacDProgrammaAffidamento;
	}

	/**
	 * @param siacDProgrammaAffidamento the siacDProgrammaAffidamento to set
	 */
	public void setSiacDProgrammaAffidamento(SiacDProgrammaAffidamento siacDProgrammaAffidamento) {
		this.siacDProgrammaAffidamento = siacDProgrammaAffidamento;
	}
	
	/**
	 * @return the siacDProgrammaAffidamento
	 */
	public SiacDProgrammaTipo getSiacDProgrammaTipo() {
		return siacDProgrammaTipo;
	}

	/**
	 * Sets the siac D programma tipo.
	 *
	 * @param siacDProgrammaTipo the new siac D programma tipo
	 */
	public void setSiacDProgrammaTipo(SiacDProgrammaTipo siacDProgrammaTipo) {
		this.siacDProgrammaTipo = siacDProgrammaTipo;
	}

	public List<SiacRMutuoProgramma> getSiacRMutuoProgramma() {
		return siacRMutuoProgramma;
	}

	public void setSiacRMutuoProgramma(List<SiacRMutuoProgramma> siacRMutuoProgramma) {
		this.siacRMutuoProgramma = siacRMutuoProgramma;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return programmaId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.programmaId = uid;
		
	}

}