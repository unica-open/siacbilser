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
 * The persistent class for the siac_r_ordinativo_modpag database table.
 * 
 */
@Entity
@Table(name="siac_r_ordinativo_modpag")
@NamedQuery(name="SiacROrdinativoModpag.findAll", query="SELECT s FROM SiacROrdinativoModpag s")
public class SiacROrdinativoModpag extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ord modpag id. */
	@Id
	@SequenceGenerator(name="SIAC_R_ORDINATIVO_MODPAG_ORDMODPAGID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_ORDINATIVO_MODPAG_ORD_MODPAG_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ORDINATIVO_MODPAG_ORDMODPAGID_GENERATOR")
	@Column(name="ord_modpag_id")
	private Integer ordModpagId;

	//bi-directional many-to-one association to SiacTModpag
	/** The siac t modpag. */
	@ManyToOne
	@JoinColumn(name="modpag_id")
	private SiacTModpag siacTModpag;

	//bi-directional many-to-one association to SiacTOrdinativo
	/** The siac t ordinativo. */
	@ManyToOne
	@JoinColumn(name="ord_id")
	private SiacTOrdinativo siacTOrdinativo;

	/**
	 * Instantiates a new siac r ordinativo modpag.
	 */
	public SiacROrdinativoModpag() {
	}

	/**
	 * Gets the ord modpag id.
	 *
	 * @return the ord modpag id
	 */
	public Integer getOrdModpagId() {
		return this.ordModpagId;
	}

	/**
	 * Sets the ord modpag id.
	 *
	 * @param ordModpagId the new ord modpag id
	 */
	public void setOrdModpagId(Integer ordModpagId) {
		this.ordModpagId = ordModpagId;
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

	/**
	 * Gets the siac t ordinativo.
	 *
	 * @return the siac t ordinativo
	 */
	public SiacTOrdinativo getSiacTOrdinativo() {
		return this.siacTOrdinativo;
	}

	/**
	 * Sets the siac t ordinativo.
	 *
	 * @param siacTOrdinativo the new siac t ordinativo
	 */
	public void setSiacTOrdinativo(SiacTOrdinativo siacTOrdinativo) {
		this.siacTOrdinativo = siacTOrdinativo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return ordModpagId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.ordModpagId = uid;
	}

}