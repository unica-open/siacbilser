/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.ArrayList;
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
 * The persistent class for the siac_r_variazione_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_variazione_stato")
@NamedQuery(name="SiacRVariazioneStato.findAll", query="SELECT s FROM SiacRVariazioneStato s")
public class SiacRVariazioneStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The variazione stato id. */
	@Id
	@SequenceGenerator(name="SIAC_R_VARIAZIONE_STATO_VARIAZIONESTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_VARIAZIONE_STATO_VARIAZIONE_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_VARIAZIONE_STATO_VARIAZIONESTATOID_GENERATOR")
	@Column(name="variazione_stato_id")
	private Integer variazioneStatoId;

	//bi-directional many-to-one association to SiacDVariazioneStato
	/** The siac d variazione stato. */
	@ManyToOne
	@JoinColumn(name="variazione_stato_tipo_id")
	private SiacDVariazioneStato siacDVariazioneStato;

	//bi-directional many-to-one association to SiacTAttoAmm
	/** The siac t atto amm. */
	@ManyToOne
	@JoinColumn(name="attoamm_id")
	private SiacTAttoAmm siacTAttoAmm;
	
	//bi-directional many-to-one association to SiacTAttoAmm
	/** The siac t atto amm. */
	@ManyToOne
	@JoinColumn(name="attoamm_id_varbil")
	private SiacTAttoAmm siacTAttoAmmVarbil;

	//bi-directional many-to-one association to SiacTVariazione
	/** The siac t variazione. */
	@ManyToOne
	@JoinColumn(name="variazione_id")
	private SiacTVariazione siacTVariazione;

	//bi-directional many-to-one association to SiacTBilElemDetVar
	/** The siac t bil elem det vars. */
	@OneToMany(mappedBy="siacRVariazioneStato", cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	private List<SiacTBilElemDetVar> siacTBilElemDetVars; //usato solo per le Variazioni di Importo
	
	//bi-directional many-to-one association to SiacRBilElemClassVar
	/** The siac r bil elem class vars. */
	@OneToMany(mappedBy="siacRVariazioneStato", cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	private List<SiacRBilElemClassVar> siacRBilElemClassVars; //usato solo per la Variazione di Codifiche

	//bi-directional many-to-one association to SiacTBilElemVar
	/** The siac t bil elem vars. */
	@OneToMany(mappedBy="siacRVariazioneStato", cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	private List<SiacTBilElemVar> siacTBilElemVars;

	/**
	 * Instantiates a new siac r variazione stato.
	 */
	public SiacRVariazioneStato() {
	}
	
	
	//ENTITY aggiunto da noi
	/**
	 * Adds the siac t class.
	 *
	 * @param siacTClass the siac t class
	 * @param siacTBilElem the siac t bil elem
	 */
	public void addSiacTClass(SiacTClass siacTClass, SiacTClass siacTClassPrec, SiacTBilElem siacTBilElem, Date dataFineValidita) {
		if (siacRBilElemClassVars == null){
			siacRBilElemClassVars = new ArrayList<SiacRBilElemClassVar>();
		}

		SiacRBilElemClassVar siacRBilElemClassVar = new SiacRBilElemClassVar();
		siacRBilElemClassVar.setSiacTClass(siacTClass);
		siacRBilElemClassVar.setSiacTClassPrec(siacTClassPrec);
		siacRBilElemClassVar.setDataFineValiditaIfNotSet(dataFineValidita);
		siacRBilElemClassVar.setSiacTBilElem(siacTBilElem);
		siacRBilElemClassVar.setSiacRVariazioneStato(this);
		siacRBilElemClassVar.setSiacTEnteProprietario(this.getSiacTEnteProprietario());
		siacRBilElemClassVar.setLoginOperazione(this.getLoginOperazione());

		siacRBilElemClassVars.add(siacRBilElemClassVar);
	}

	/**
	 * Gets the variazione stato id.
	 *
	 * @return the variazione stato id
	 */
	public Integer getVariazioneStatoId() {
		return this.variazioneStatoId;
	}

	/**
	 * Sets the variazione stato id.
	 *
	 * @param variazioneStatoId the new variazione stato id
	 */
	public void setVariazioneStatoId(Integer variazioneStatoId) {
		this.variazioneStatoId = variazioneStatoId;
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
		siacRBilElemClassVar.setSiacRVariazioneStato(this);

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
		siacRBilElemClassVar.setSiacRVariazioneStato(null);

		return siacRBilElemClassVar;
	}

	/**
	 * Gets the siac d variazione stato.
	 *
	 * @return the siac d variazione stato
	 */
	public SiacDVariazioneStato getSiacDVariazioneStato() {
		return this.siacDVariazioneStato;
	}

	/**
	 * Sets the siac d variazione stato.
	 *
	 * @param siacDVariazioneStato the new siac d variazione stato
	 */
	public void setSiacDVariazioneStato(SiacDVariazioneStato siacDVariazioneStato) {
		this.siacDVariazioneStato = siacDVariazioneStato;
	}

	/**
	 * Gets the siac t atto amm.
	 *
	 * @return the siac t atto amm
	 */
	public SiacTAttoAmm getSiacTAttoAmm() {
		return this.siacTAttoAmm;
	}

	/**
	 * Sets the siac t atto amm.
	 *
	 * @param siacTAttoAmm the new siac t atto amm
	 */
	public void setSiacTAttoAmm(SiacTAttoAmm siacTAttoAmm) {
		this.siacTAttoAmm = siacTAttoAmm;
	}
	

	/**
	 * Gets the siac t atto amm varbil.
	 *
	 * @return the siac t atto amm varbil
	 */
	public SiacTAttoAmm getSiacTAttoAmmVarbil() {
		return siacTAttoAmmVarbil;
	}

	/**
	 * Sets the siac t atto amm varbil.
	 *
	 * @param siacTAttoAmm the new siac t atto amm varbil
	 */
	public void setSiacTAttoAmmVarbil(SiacTAttoAmm siacTAttoAmmVarbil) {
		this.siacTAttoAmmVarbil = siacTAttoAmmVarbil;
	}


	/**
	 * Gets the siac t variazione.
	 *
	 * @return the siac t variazione
	 */
	public SiacTVariazione getSiacTVariazione() {
		return this.siacTVariazione;
	}

	/**
	 * Sets the siac t variazione.
	 *
	 * @param siacTVariazione the new siac t variazione
	 */
	public void setSiacTVariazione(SiacTVariazione siacTVariazione) {
		this.siacTVariazione = siacTVariazione;
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
		siacTBilElemDetVar.setSiacRVariazioneStato(this);

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
		siacTBilElemDetVar.setSiacRVariazioneStato(null);

		return siacTBilElemDetVar;
	}

	/**
	 * Gets the siac t bil elem vars.
	 *
	 * @return the siac t bil elem vars
	 */
	public List<SiacTBilElemVar> getSiacTBilElemVars() {
		return this.siacTBilElemVars;
	}

	/**
	 * Sets the siac t bil elem vars.
	 *
	 * @param siacTBilElemVars the new siac t bil elem vars
	 */
	public void setSiacTBilElemVars(List<SiacTBilElemVar> siacTBilElemVars) {
		this.siacTBilElemVars = siacTBilElemVars;
	}

	/**
	 * Adds the siac t bil elem var.
	 *
	 * @param siacTBilElemVar the siac t bil elem var
	 * @return the siac t bil elem var
	 */
	public SiacTBilElemVar addSiacTBilElemVar(SiacTBilElemVar siacTBilElemVar) {
		getSiacTBilElemVars().add(siacTBilElemVar);
		siacTBilElemVar.setSiacRVariazioneStato(this);

		return siacTBilElemVar;
	}

	/**
	 * Removes the siac t bil elem var.
	 *
	 * @param siacTBilElemVar the siac t bil elem var
	 * @return the siac t bil elem var
	 */
	public SiacTBilElemVar removeSiacTBilElemVar(SiacTBilElemVar siacTBilElemVar) {
		getSiacTBilElemVars().remove(siacTBilElemVar);
		siacTBilElemVar.setSiacRVariazioneStato(null);

		return siacTBilElemVar;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return variazioneStatoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.variazioneStatoId = uid;
		
	}

}