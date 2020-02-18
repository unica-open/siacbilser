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
 * The persistent class for the siac_r_muto_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_mutuo_stato")
@NamedQuery(name="SiacRMutuoStato.findAll", query="SELECT s FROM SiacRMutuoStato s")
public class SiacRMutuoStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The mut stato r id. */
	@Id
	@SequenceGenerator(name="SIAC_R_MUTO_STATO_MUTSTATORID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_MUTO_STATO_MUT_STATO_R_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MUTO_STATO_MUTSTATORID_GENERATOR")
	@Column(name="mut_stato_r_id")
	private Integer mutStatoRId;

	//bi-directional many-to-one association to SiacDMutuoStato
	/** The siac d mutuo stato. */
	@ManyToOne
	@JoinColumn(name="mut_stato_id")
	private SiacDMutuoStato siacDMutuoStato;

	//bi-directional many-to-one association to SiacTMutuo
	/** The siac t mutuo. */
	@ManyToOne
	@JoinColumn(name="mut_id")
	private SiacTMutuo siacTMutuo;

	/**
	 * Instantiates a new siac r mutuo stato.
	 */
	public SiacRMutuoStato() {
	}

	/**
	 * Gets the mut stato r id.
	 *
	 * @return the mut stato r id
	 */
	public Integer getMutStatoRId() {
		return this.mutStatoRId;
	}

	/**
	 * Sets the mut stato r id.
	 *
	 * @param mutStatoRId the new mut stato r id
	 */
	public void setMutStatoRId(Integer mutStatoRId) {
		this.mutStatoRId = mutStatoRId;
	}

	/**
	 * Gets the siac d mutuo stato.
	 *
	 * @return the siac d mutuo stato
	 */
	public SiacDMutuoStato getSiacDMutuoStato() {
		return this.siacDMutuoStato;
	}

	/**
	 * Sets the siac d mutuo stato.
	 *
	 * @param siacDMutuoStato the new siac d mutuo stato
	 */
	public void setSiacDMutuoStato(SiacDMutuoStato siacDMutuoStato) {
		this.siacDMutuoStato = siacDMutuoStato;
	}

	/**
	 * Gets the siac t mutuo.
	 *
	 * @return the siac t mutuo
	 */
	public SiacTMutuo getSiacTMutuo() {
		return this.siacTMutuo;
	}

	/**
	 * Sets the siac t mutuo.
	 *
	 * @param siacTMutuo the new siac t mutuo
	 */
	public void setSiacTMutuo(SiacTMutuo siacTMutuo) {
		this.siacTMutuo = siacTMutuo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return mutStatoRId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.mutStatoRId = uid;
	}

}