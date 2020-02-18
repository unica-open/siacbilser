/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_r_variazione_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_cespiti_cespiti_elab_ammortamenti")
@NamedQuery(name="SiacRCespitiCespitiElabAmmortamentiDett.findAll", query="SELECT s FROM SiacRCespitiCespitiElabAmmortamentiDett s")
public class SiacRCespitiCespitiElabAmmortamentiDett extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -520781593738992856L;

	/** The variazione stato id. */
	@Id
	@SequenceGenerator(name="SIAC_R_CESPITI_CESPITI_ELAB_AMMORTAMENTI_DETT_CESPITIELABDETTID_GENERATOR", allocationSize=1, sequenceName="siac_r_cespiti_cespiti_elab_ammortamenti_ces_elab_dett_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CESPITI_CESPITI_ELAB_AMMORTAMENTI_DETT_CESPITIELABDETTID_GENERATOR")
	@Column(name="ces_elab_dett_id")
	private Integer cesElabDettId;

	//bi-directional many-to-one association to SiacDVariazioneStato
	/** The siac d variazione stato. */
	@ManyToOne
	@JoinColumn(name="ces_id")
	private SiacTCespiti siacTCespiti;

	//bi-directional many-to-one association to SiacTAttoAmm
	/** The siac t atto amm. */
	@ManyToOne
	@JoinColumn(name="elab_dett_id_dare")
	private SiacTCespitiElabAmmortamentiDett siacTCespitiElabAmmortamentiDettDare;
	
	@ManyToOne
	@JoinColumn(name="elab_dett_id_avere")
	private SiacTCespitiElabAmmortamentiDett siacTCespitiElabAmmortamentiDettAvere;
	
	@ManyToOne
	@JoinColumn(name="elab_id")
	private SiacTCespitiElabAmmortamenti siacTCespitiElabAmmortamenti;
	
	//bi-directional many-to-one association to SiacTAttoAmm
	/** The siac t atto amm. */
	@ManyToOne
	@JoinColumn(name="pnota_id")
	private SiacTPrimaNota siacTPrimaNota;
	
	@ManyToOne
	@JoinColumn(name="ces_amm_dett_id")
	private SiacTCespitiAmmortamentoDett siacTCespitiAmmortamentoDett;
	

	/**
	 * Instantiates a new siac r variazione stato.
	 */
	public SiacRCespitiCespitiElabAmmortamentiDett() {
	}
	


	/**
	 * Gets the siac d variazione stato.
	 *
	 * @return the siac d variazione stato
	 */
	public SiacTCespiti getSiacTCespiti() {
		return this.siacTCespiti;
	}

	/**
	 * Sets the siac d variazione stato.
	 *
	 * @param siacTCespiti the new siac T cespiti
	 */
	public void setSiacTCespiti(SiacTCespiti siacTCespiti) {
		this.siacTCespiti = siacTCespiti;
	}

	/**
	 * Gets the siac t atto amm.
	 *
	 * @return the siac t atto amm
	 */
	public SiacTCespitiElabAmmortamentiDett getSiacTCespitiElabAmmortamentiDettDare() {
		return this.siacTCespitiElabAmmortamentiDettDare;
	}

	/**
	 * Sets the siac t atto amm.
	 *
	 * @param siacTCespitiElabAmmortamentiDettDare the new siac T cespiti elab ammortamenti dett dare
	 */
	public void setSiacTCespitiElabAmmortamentiDettDare(SiacTCespitiElabAmmortamentiDett siacTCespitiElabAmmortamentiDettDare) {
		this.siacTCespitiElabAmmortamentiDettDare = siacTCespitiElabAmmortamentiDettDare;
	}
	
	/**
	 * Gets the siac t atto amm.
	 *
	 * @return the siac t atto amm
	 */
	public SiacTCespitiElabAmmortamentiDett getSiacTCespitiElabAmmortamentidettAvere() {
		return this.siacTCespitiElabAmmortamentiDettAvere;
	}

	/**
	 * Sets the siac t atto amm.
	 *
	 * @param siacTCespitiElabAmmortamentiDettAvere the new siac T cespiti elab ammortamenti dett avere
	 */
	public void setSiacTCespitiElabAmmortamentiDettAvere(SiacTCespitiElabAmmortamentiDett siacTCespitiElabAmmortamentiDettAvere) {
		this.siacTCespitiElabAmmortamentiDettAvere = siacTCespitiElabAmmortamentiDettAvere;
	}
	
	/**
	 * @return the siacTCespitiElabAmmortamenti
	 */
	public SiacTCespitiElabAmmortamenti getSiacTCespitiElabAmmortamenti() {
		return siacTCespitiElabAmmortamenti;
	}



	/**
	 * @param siacTCespitiElabAmmortamenti the siacTCespitiElabAmmortamenti to set
	 */
	public void setSiacTCespitiElabAmmortamenti(SiacTCespitiElabAmmortamenti siacTCespitiElabAmmortamenti) {
		this.siacTCespitiElabAmmortamenti = siacTCespitiElabAmmortamenti;
	}



	/**
	 * @return the siacTCespitiElabAmmortamentiDettAvere
	 */
	public SiacTCespitiElabAmmortamentiDett getSiacTCespitiElabAmmortamentiDettAvere() {
		return siacTCespitiElabAmmortamentiDettAvere;
	}


	/**
	 * @return the siacTCespitiAmmortamentoDett
	 */
	public SiacTCespitiAmmortamentoDett getSiacTCespitiAmmortamentoDett() {
		return siacTCespitiAmmortamentoDett;
	}



	/**
	 * @param siacTCespitiAmmortamentoDett the siacTCespitiAmmortamentoDett to set
	 */
	public void setSiacTCespitiAmmortamentoDett(SiacTCespitiAmmortamentoDett siacTCespitiAmmortamentoDett) {
		this.siacTCespitiAmmortamentoDett = siacTCespitiAmmortamentoDett;
	}



	/**
	 * @return the cesElabDettId
	 */
	public Integer getCesElabDettId() {
		return cesElabDettId;
	}

	/**
	 * @param cesElabDettId the cesElabDettId to set
	 */
	public void setCesElabDettId(Integer cesElabDettId) {
		this.cesElabDettId = cesElabDettId;
	}

	/**
	 * @return the siacTPrimaNota
	 */
	public SiacTPrimaNota getSiacTPrimaNota() {
		return siacTPrimaNota;
	}



	/**
	 * @param siacTPrimaNota the siacTPrimaNota to set
	 */
	public void setSiacTPrimaNota(SiacTPrimaNota siacTPrimaNota) {
		this.siacTPrimaNota = siacTPrimaNota;
	}



	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return cesElabDettId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.cesElabDettId = uid;
		
	}

}