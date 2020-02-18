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
 * The persistent class for the siac_r_vincolo_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_vincolo_stato")
@NamedQuery(name="SiacRVincoloStato.findAll", query="SELECT s FROM SiacRVincoloStato s")
public class SiacRVincoloStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The vincolo stato r id. */
	@Id
	@SequenceGenerator(name="SIAC_R_VINCOLO_STATO_VINCOLOSTATORID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_VINCOLO_STATO_VINCOLO_STATO_R_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_VINCOLO_STATO_VINCOLOSTATORID_GENERATOR")
	@Column(name="vincolo_stato_r_id")
	private Integer vincoloStatoRId;

	//bi-directional many-to-one association to SiacDVincoloStato
	/** The siac d vincolo stato. */
	@ManyToOne
	@JoinColumn(name="vincolo_stato_id")
	private SiacDVincoloStato siacDVincoloStato;

	//bi-directional many-to-one association to SiacTVincolo
	/** The siac t vincolo. */
	@ManyToOne
	@JoinColumn(name="vincolo_id")
	private SiacTVincolo siacTVincolo;

	/**
	 * Instantiates a new siac r vincolo stato.
	 */
	public SiacRVincoloStato() {
	}

	/**
	 * Gets the vincolo stato r id.
	 *
	 * @return the vincolo stato r id
	 */
	public Integer getVincoloStatoRId() {
		return this.vincoloStatoRId;
	}

	/**
	 * Sets the vincolo stato r id.
	 *
	 * @param vincoloStatoRId the new vincolo stato r id
	 */
	public void setVincoloStatoRId(Integer vincoloStatoRId) {
		this.vincoloStatoRId = vincoloStatoRId;
	}

	/**
	 * Gets the siac d vincolo stato.
	 *
	 * @return the siac d vincolo stato
	 */
	public SiacDVincoloStato getSiacDVincoloStato() {
		return this.siacDVincoloStato;
	}

	/**
	 * Sets the siac d vincolo stato.
	 *
	 * @param siacDVincoloStato the new siac d vincolo stato
	 */
	public void setSiacDVincoloStato(SiacDVincoloStato siacDVincoloStato) {
		this.siacDVincoloStato = siacDVincoloStato;
	}

	/**
	 * Gets the siac t vincolo.
	 *
	 * @return the siac t vincolo
	 */
	public SiacTVincolo getSiacTVincolo() {
		return this.siacTVincolo;
	}

	/**
	 * Sets the siac t vincolo.
	 *
	 * @param siacTVincolo the new siac t vincolo
	 */
	public void setSiacTVincolo(SiacTVincolo siacTVincolo) {
		this.siacTVincolo = siacTVincolo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return vincoloStatoRId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.vincoloStatoRId = uid;
		
	}

}