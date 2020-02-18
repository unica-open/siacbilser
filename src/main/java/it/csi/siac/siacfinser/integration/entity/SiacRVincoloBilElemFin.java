/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

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

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_r_vincolo_bil_elem database table.
 * 
 */
@Entity
@Table(name="siac_r_vincolo_bil_elem")
@NamedQuery(name="SiacRVincoloBilElemFin.findAll", query="SELECT s FROM SiacRVincoloBilElemFin s")
public class SiacRVincoloBilElemFin extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The vincolo elem id. */
	@Id
	@SequenceGenerator(name="SIAC_R_VINCOLO_BIL_ELEM_VINCOLOELEMID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_VINCOLO_BIL_ELEM_VINCOLO_ELEM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_VINCOLO_BIL_ELEM_VINCOLOELEMID_GENERATOR")
	@Column(name="vincolo_elem_id")
	private Integer vincoloElemId;

	//bi-directional many-to-one association to SiacTBilElemFin
	/** The siac t bil elem. */
	@ManyToOne
	@JoinColumn(name="elem_id")
	private SiacTBilElemFin siacTBilElem;

	//bi-directional many-to-one association to SiacTVincoloFin
	/** The siac t vincolo. */
	@ManyToOne
	@JoinColumn(name="vincolo_id")
	private SiacTVincoloFin siacTVincolo;

	/**
	 * Instantiates a new siac r vincolo bil elem.
	 */
	public SiacRVincoloBilElemFin() {
	}

	/**
	 * Gets the vincolo elem id.
	 *
	 * @return the vincolo elem id
	 */
	public Integer getVincoloElemId() {
		return this.vincoloElemId;
	}

	/**
	 * Sets the vincolo elem id.
	 *
	 * @param vincoloElemId the new vincolo elem id
	 */
	public void setVincoloElemId(Integer vincoloElemId) {
		this.vincoloElemId = vincoloElemId;
	}

	/**
	 * Gets the siac t bil elem.
	 *
	 * @return the siac t bil elem
	 */
	public SiacTBilElemFin getSiacTBilElem() {
		return this.siacTBilElem;
	}

	/**
	 * Sets the siac t bil elem.
	 *
	 * @param siacTBilElem the new siac t bil elem
	 */
	public void setSiacTBilElem(SiacTBilElemFin siacTBilElem) {
		this.siacTBilElem = siacTBilElem;
	}

	/**
	 * Gets the siac t vincolo.
	 *
	 * @return the siac t vincolo
	 */
	public SiacTVincoloFin getSiacTVincolo() {
		return this.siacTVincolo;
	}

	/**
	 * Sets the siac t vincolo.
	 *
	 * @param siacTVincolo the new siac t vincolo
	 */
	public void setSiacTVincolo(SiacTVincoloFin siacTVincolo) {
		this.siacTVincolo = siacTVincolo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return vincoloElemId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.vincoloElemId = uid;
		
	}

}