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
 * The persistent class for the siac_r_fase_operativa_bil_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_fase_operativa_bil_stato")
public class SiacRFaseOperativaBilStato extends SiacTEnteBase {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The fase operativa bil stato id. */
	@Id
	@SequenceGenerator(name="SIAC_R_FASE_OPERATIVA_BIL_STATO_FASEOPERATIVABILSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_FASE_OPERATIVA_BIL_STATO_FASE_OPERATIVA_BIL_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_FASE_OPERATIVA_BIL_STATO_FASEOPERATIVABILSTATOID_GENERATOR")
	@Column(name="fase_operativa_bil_stato_id")
	private Integer faseOperativaBilStatoId;


	//bi-directional many-to-one association to SiacDBilStatoOp
	/** The siac d bil stato op. */
	@ManyToOne
	@JoinColumn(name="bil_stato_op_id")
	private SiacDBilStatoOp siacDBilStatoOp;

	//bi-directional many-to-one association to SiacDFaseOperativa
	/** The siac d fase operativa. */
	@ManyToOne
	@JoinColumn(name="fase_operativa_id")
	private SiacDFaseOperativa siacDFaseOperativa;

	/**
	 * Instantiates a new siac r fase operativa bil stato.
	 */
	public SiacRFaseOperativaBilStato() {
	}

	/**
	 * Gets the fase operativa bil stato id.
	 *
	 * @return the fase operativa bil stato id
	 */
	public Integer getFaseOperativaBilStatoId() {
		return this.faseOperativaBilStatoId;
	}

	/**
	 * Sets the fase operativa bil stato id.
	 *
	 * @param faseOperativaBilStatoId the new fase operativa bil stato id
	 */
	public void setFaseOperativaBilStatoId(Integer faseOperativaBilStatoId) {
		this.faseOperativaBilStatoId = faseOperativaBilStatoId;
	}

	

	/**
	 * Gets the siac d bil stato op.
	 *
	 * @return the siac d bil stato op
	 */
	public SiacDBilStatoOp getSiacDBilStatoOp() {
		return this.siacDBilStatoOp;
	}

	/**
	 * Sets the siac d bil stato op.
	 *
	 * @param siacDBilStatoOp the new siac d bil stato op
	 */
	public void setSiacDBilStatoOp(SiacDBilStatoOp siacDBilStatoOp) {
		this.siacDBilStatoOp = siacDBilStatoOp;
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

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {		
		return faseOperativaBilStatoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.faseOperativaBilStatoId = uid;
		
	}

	

}