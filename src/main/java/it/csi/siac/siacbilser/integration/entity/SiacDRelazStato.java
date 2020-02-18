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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_d_relaz_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_relaz_stato")
@NamedQuery(name="SiacDRelazStato.findAll", query="SELECT s FROM SiacDRelazStato s")
public class SiacDRelazStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The relaz stato id. */
	@Id
	@SequenceGenerator(name="SIAC_D_RELAZ_STATO_RELAZSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_RELAZ_STATO_RELAZ_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_RELAZ_STATO_RELAZSTATOID_GENERATOR")
	@Column(name="relaz_stato_id")
	private Integer relazStatoId;

	/** The relaz stato code. */
	@Column(name="relaz_stato_code")
	private String relazStatoCode;

	/** The relaz stato desc. */
	@Column(name="relaz_stato_desc")
	private String relazStatoDesc;
	

	//bi-directional many-to-one association to SiacRSoggettoRelazStato
	/** The siac r soggetto relaz statos. */
	@OneToMany(mappedBy="siacDRelazStato")
	private List<SiacRSoggettoRelazStato> siacRSoggettoRelazStatos;

	/**
	 * Instantiates a new siac d relaz stato.
	 */
	public SiacDRelazStato() {
	}

	/**
	 * Gets the relaz stato id.
	 *
	 * @return the relaz stato id
	 */
	public Integer getRelazStatoId() {
		return this.relazStatoId;
	}

	/**
	 * Sets the relaz stato id.
	 *
	 * @param relazStatoId the new relaz stato id
	 */
	public void setRelazStatoId(Integer relazStatoId) {
		this.relazStatoId = relazStatoId;
	}



	/**
	 * Gets the relaz stato code.
	 *
	 * @return the relaz stato code
	 */
	public String getRelazStatoCode() {
		return this.relazStatoCode;
	}

	/**
	 * Sets the relaz stato code.
	 *
	 * @param relazStatoCode the new relaz stato code
	 */
	public void setRelazStatoCode(String relazStatoCode) {
		this.relazStatoCode = relazStatoCode;
	}

	/**
	 * Gets the relaz stato desc.
	 *
	 * @return the relaz stato desc
	 */
	public String getRelazStatoDesc() {
		return this.relazStatoDesc;
	}

	/**
	 * Sets the relaz stato desc.
	 *
	 * @param relazStatoDesc the new relaz stato desc
	 */
	public void setRelazStatoDesc(String relazStatoDesc) {
		this.relazStatoDesc = relazStatoDesc;
	}

	/**
	 * Gets the siac r soggetto relaz statos.
	 *
	 * @return the siac r soggetto relaz statos
	 */
	public List<SiacRSoggettoRelazStato> getSiacRSoggettoRelazStatos() {
		return this.siacRSoggettoRelazStatos;
	}

	/**
	 * Sets the siac r soggetto relaz statos.
	 *
	 * @param siacRSoggettoRelazStatos the new siac r soggetto relaz statos
	 */
	public void setSiacRSoggettoRelazStatos(List<SiacRSoggettoRelazStato> siacRSoggettoRelazStatos) {
		this.siacRSoggettoRelazStatos = siacRSoggettoRelazStatos;
	}

	/**
	 * Adds the siac r soggetto relaz stato.
	 *
	 * @param siacRSoggettoRelazStato the siac r soggetto relaz stato
	 * @return the siac r soggetto relaz stato
	 */
	public SiacRSoggettoRelazStato addSiacRSoggettoRelazStato(SiacRSoggettoRelazStato siacRSoggettoRelazStato) {
		getSiacRSoggettoRelazStatos().add(siacRSoggettoRelazStato);
		siacRSoggettoRelazStato.setSiacDRelazStato(this);

		return siacRSoggettoRelazStato;
	}

	/**
	 * Removes the siac r soggetto relaz stato.
	 *
	 * @param siacRSoggettoRelazStato the siac r soggetto relaz stato
	 * @return the siac r soggetto relaz stato
	 */
	public SiacRSoggettoRelazStato removeSiacRSoggettoRelazStato(SiacRSoggettoRelazStato siacRSoggettoRelazStato) {
		getSiacRSoggettoRelazStatos().remove(siacRSoggettoRelazStato);
		siacRSoggettoRelazStato.setSiacDRelazStato(null);

		return siacRSoggettoRelazStato;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return relazStatoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.relazStatoId = uid;
		
	}

}