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
 * The persistent class for the siac_t_persona_giuridica database table.
 * 
 */
@Entity
@Table(name="siac_t_persona_giuridica")
@NamedQuery(name="SiacTPersonaGiuridica.findAll", query="SELECT s FROM SiacTPersonaGiuridica s")
public class SiacTPersonaGiuridica extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The perg id. */
	@Id
	@SequenceGenerator(name="SIAC_T_PERSONA_GIURIDICA_PERGID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_PERSONA_GIURIDICA_PERG_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_PERSONA_GIURIDICA_PERGID_GENERATOR")
	@Column(name="perg_id")
	private Integer pergId;

	/** The ragione sociale. */
	@Column(name="ragione_sociale")
	private String ragioneSociale;


	//bi-directional many-to-one association to SiacTSoggetto
	/** The siac t soggetto. */
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggetto siacTSoggetto;

	/**
	 * Instantiates a new siac t persona giuridica.
	 */
	public SiacTPersonaGiuridica() {
	}

	/**
	 * Gets the perg id.
	 *
	 * @return the perg id
	 */
	public Integer getPergId() {
		return this.pergId;
	}

	/**
	 * Sets the perg id.
	 *
	 * @param pergId the new perg id
	 */
	public void setPergId(Integer pergId) {
		this.pergId = pergId;
	}

	/**
	 * Gets the ragione sociale.
	 *
	 * @return the ragione sociale
	 */
	public String getRagioneSociale() {
		return this.ragioneSociale;
	}

	/**
	 * Sets the ragione sociale.
	 *
	 * @param ragioneSociale the new ragione sociale
	 */
	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}


	/**
	 * Gets the siac t soggetto.
	 *
	 * @return the siac t soggetto
	 */
	public SiacTSoggetto getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	/**
	 * Sets the siac t soggetto.
	 *
	 * @param siacTSoggetto the new siac t soggetto
	 */
	public void setSiacTSoggetto(SiacTSoggetto siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return pergId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.pergId = uid;
	}

}