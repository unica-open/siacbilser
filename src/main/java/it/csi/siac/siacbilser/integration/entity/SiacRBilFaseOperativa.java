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
 * The persistent class for the siac_r_bil_fase_operativa database table.
 * 
 */
@Entity
@Table(name="siac_r_bil_fase_operativa")
public class SiacRBilFaseOperativa extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The bil fase operativa id. */
	@Id
	@SequenceGenerator(name="SIAC_R_BIL_FASE_OPERATIVA_BILFASEOPERATIVAID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_BIL_FASE_OPERATIVA_BIL_FASE_OPERATIVA_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_BIL_FASE_OPERATIVA_BILFASEOPERATIVAID_GENERATOR")
	@Column(name="bil_fase_operativa_id")
	private Integer bilFaseOperativaId;

	//bi-directional many-to-one association to SiacDFaseOperativa
	/** The siac d fase operativa. */
	@ManyToOne
	@JoinColumn(name="fase_operativa_id")
	private SiacDFaseOperativa siacDFaseOperativa;

	//bi-directional many-to-one association to SiacTBil
	/** The siac t bil. */
	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBil siacTBil;

	/**
	 * Instantiates a new siac r bil fase operativa.
	 */
	public SiacRBilFaseOperativa() {
	}

	/**
	 * Gets the bil fase operativa id.
	 *
	 * @return the bil fase operativa id
	 */
	public Integer getBilFaseOperativaId() {
		return this.bilFaseOperativaId;
	}

	/**
	 * Sets the bil fase operativa id.
	 *
	 * @param bilFaseOperativaId the new bil fase operativa id
	 */
	public void setBilFaseOperativaId(Integer bilFaseOperativaId) {
		this.bilFaseOperativaId = bilFaseOperativaId;
	}

	/**
	 * Gets the siac d fase operativa.
	 *
	 * @return the siac d fase operativa
	 */
	public SiacDFaseOperativa getSiacDFaseOperativa() {
		return this.siacDFaseOperativa;
	}

	/**
	 * Sets the siac d fase operativa.
	 *
	 * @param siacDFaseOperativa the new siac d fase operativa
	 */
	public void setSiacDFaseOperativa(SiacDFaseOperativa siacDFaseOperativa) {
		this.siacDFaseOperativa = siacDFaseOperativa;
	}

	/**
	 * Gets the siac t bil.
	 *
	 * @return the siac t bil
	 */
	public SiacTBil getSiacTBil() {
		return this.siacTBil;
	}

	/**
	 * Sets the siac t bil.
	 *
	 * @param siacTBil the new siac t bil
	 */
	public void setSiacTBil(SiacTBil siacTBil) {
		this.siacTBil = siacTBil;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return bilFaseOperativaId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.bilFaseOperativaId = uid;
	}

}