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
 * The persistent class for the siac_r_bil_tipo_stato_op database table.
 * 
 */
@Entity
@Table(name="siac_r_bil_tipo_stato_op")
public class SiacRBilTipoStatoOp extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The bil tipo stato id. */
	@Id
	@SequenceGenerator(name="SIAC_R_BIL_TIPO_STATO_OP_BILTIPOSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_BIL_TIPO_STATO_OP_BIL_TIPO_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_BIL_TIPO_STATO_OP_BILTIPOSTATOID_GENERATOR")
	@Column(name="bil_tipo_stato_id")
	private Integer bilTipoStatoId;

	//bi-directional many-to-one association to SiacDBilStatoOp
	/** The siac d bil stato op. */
	@ManyToOne
	@JoinColumn(name="bil_stato_op_id")
	private SiacDBilStatoOp siacDBilStatoOp;

	//bi-directional many-to-one association to SiacDBilTipo
	/** The siac d bil tipo. */
	@ManyToOne
	@JoinColumn(name="bil_tipo_id")
	private SiacDBilTipo siacDBilTipo;

	/**
	 * Instantiates a new siac r bil tipo stato op.
	 */
	public SiacRBilTipoStatoOp() {
	}

	/**
	 * Gets the bil tipo stato id.
	 *
	 * @return the bil tipo stato id
	 */
	public Integer getBilTipoStatoId() {
		return this.bilTipoStatoId;
	}

	/**
	 * Sets the bil tipo stato id.
	 *
	 * @param bilTipoStatoId the new bil tipo stato id
	 */
	public void setBilTipoStatoId(Integer bilTipoStatoId) {
		this.bilTipoStatoId = bilTipoStatoId;
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
	 * Gets the siac d bil tipo.
	 *
	 * @return the siac d bil tipo
	 */
	public SiacDBilTipo getSiacDBilTipo() {
		return this.siacDBilTipo;
	}

	/**
	 * Sets the siac d bil tipo.
	 *
	 * @param siacDBilTipo the new siac d bil tipo
	 */
	public void setSiacDBilTipo(SiacDBilTipo siacDBilTipo) {
		this.siacDBilTipo = siacDBilTipo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return bilTipoStatoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.bilTipoStatoId = uid;
	}

}