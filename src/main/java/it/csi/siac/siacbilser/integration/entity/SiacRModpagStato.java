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
 * The persistent class for the siac_r_modpag_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_modpag_stato")
@NamedQuery(name="SiacRModpagStato.findAll", query="SELECT s FROM SiacRModpagStato s")
public class SiacRModpagStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The modpag stato r id. */
	@Id
	@SequenceGenerator(name="SIAC_R_MODPAG_STATO_MODPAGSTATORID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_MODPAG_STATO_MODPAG_STATO_R_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MODPAG_STATO_MODPAGSTATORID_GENERATOR")
	@Column(name="modpag_stato_r_id")
	private Integer modpagStatoRId;


	//bi-directional many-to-one association to SiacDModpagStato
	/** The siac d modpag stato. */
	@ManyToOne
	@JoinColumn(name="modpag_stato_id")
	private SiacDModpagStato siacDModpagStato;

	//bi-directional many-to-one association to SiacTModpag
	/** The siac t modpag. */
	@ManyToOne
	@JoinColumn(name="modpag_id")
	private SiacTModpag siacTModpag;

	/**
	 * Instantiates a new siac r modpag stato.
	 */
	public SiacRModpagStato() {
	}

	/**
	 * Gets the modpag stato r id.
	 *
	 * @return the modpag stato r id
	 */
	public Integer getModpagStatoRId() {
		return this.modpagStatoRId;
	}

	/**
	 * Sets the modpag stato r id.
	 *
	 * @param modpagStatoRId the new modpag stato r id
	 */
	public void setModpagStatoRId(Integer modpagStatoRId) {
		this.modpagStatoRId = modpagStatoRId;
	}

	

	
	/**
	 * Gets the siac d modpag stato.
	 *
	 * @return the siac d modpag stato
	 */
	public SiacDModpagStato getSiacDModpagStato() {
		return this.siacDModpagStato;
	}

	/**
	 * Sets the siac d modpag stato.
	 *
	 * @param siacDModpagStato the new siac d modpag stato
	 */
	public void setSiacDModpagStato(SiacDModpagStato siacDModpagStato) {
		this.siacDModpagStato = siacDModpagStato;
	}



	/**
	 * Gets the siac t modpag.
	 *
	 * @return the siac t modpag
	 */
	public SiacTModpag getSiacTModpag() {
		return this.siacTModpag;
	}

	/**
	 * Sets the siac t modpag.
	 *
	 * @param siacTModpag the new siac t modpag
	 */
	public void setSiacTModpag(SiacTModpag siacTModpag) {
		this.siacTModpag = siacTModpag;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return modpagStatoRId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.modpagStatoRId = uid;
		
	}

}