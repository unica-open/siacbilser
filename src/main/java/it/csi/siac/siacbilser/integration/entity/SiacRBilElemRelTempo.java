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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_r_bil_elem_rel_tempo database table.
 * 
 */
@Entity
@Table(name="siac_r_bil_elem_rel_tempo")
public class SiacRBilElemRelTempo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The elem elem id. */
	@Id
	@SequenceGenerator(name="SIAC_R_BIL_ELEM_REL_TEMPO_ELEMELEMID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_BIL_ELEM_REL_TEMPO_ELEM_ELEM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_BIL_ELEM_REL_TEMPO_ELEMELEMID_GENERATOR")
	@Column(name="elem_elem_id")
	private Integer elemElemId;

	//bi-directional many-to-one association to SiacTBilElem
	/** The siac t bil elem. */
	@ManyToOne
	@JoinColumn(name="elem_id")
	private SiacTBilElem siacTBilElem;

	//bi-directional many-to-one association to SiacTBilElem
	/** The siac t bil elem old. */
	@ManyToOne
	@JoinColumn(name="elem_id_old")
	private SiacTBilElem siacTBilElemOld;

	/*//bi-directional many-to-one association to SiacTBilElem
	@ManyToOne
	@JoinColumn(name="elem_id")
	private SiacTBilElem siacTBilElem3;*/

	/**
	 * Instantiates a new siac r bil elem rel tempo.
	 */
	public SiacRBilElemRelTempo() {
	}

	/**
	 * Gets the elem elem id.
	 *
	 * @return the elem elem id
	 */
	public Integer getElemElemId() {
		return this.elemElemId;
	}

	/**
	 * Sets the elem elem id.
	 *
	 * @param elemElemId the new elem elem id
	 */
	public void setElemElemId(Integer elemElemId) {
		this.elemElemId = elemElemId;
	}

	/**
	 * Gets the siac t bil elem.
	 *
	 * @return the siac t bil elem
	 */
	public SiacTBilElem getSiacTBilElem() {
		return this.siacTBilElem;
	}

	/**
	 * Sets the siac t bil elem.
	 *
	 * @param siacTBilElem1 the new siac t bil elem
	 */
	public void setSiacTBilElem(SiacTBilElem siacTBilElem1) {
		this.siacTBilElem = siacTBilElem1;
	}

	/**
	 * Gets the siac t bil elem old.
	 *
	 * @return the siac t bil elem old
	 */
	public SiacTBilElem getSiacTBilElemOld() {
		return this.siacTBilElemOld;
	}

	/**
	 * Sets the siac t bil elem old.
	 *
	 * @param siacTBilElem2 the new siac t bil elem old
	 */
	public void setSiacTBilElemOld(SiacTBilElem siacTBilElem2) {
		this.siacTBilElemOld = siacTBilElem2;
	}

	/*public SiacTBilElem getSiacTBilElem3() {
		return this.siacTBilElem3;
	}

	public void setSiacTBilElem3(SiacTBilElem siacTBilElem3) {
		this.siacTBilElem3 = siacTBilElem3;
	}*/

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return elemElemId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.elemElemId = uid;
	}

}