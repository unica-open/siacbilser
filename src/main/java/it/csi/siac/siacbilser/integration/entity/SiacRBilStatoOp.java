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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_r_bil_stato_op database table.
 * 
 */
@Entity
@Table(name="siac_r_bil_stato_op")
public class SiacRBilStatoOp extends SiacTEnteBase {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The bil bil stato op id. */
	@Id
	@SequenceGenerator(name="SIAC_R_BIL_STATO_OP_BILBILSTATOOPID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_BIL_STATO_OP_BIL_BIL_STATO_OP_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_BIL_STATO_OP_BILBILSTATOOPID_GENERATOR")
	@Column(name="bil_bil_stato_op_id")
	private Integer bilBilStatoOpId;

	//bi-directional many-to-one association to SiacDBilStatoOp
	/** The siac d bil stato op. */
	@ManyToOne
	@JoinColumn(name="bil_stato_op_id")
	private SiacDBilStatoOp siacDBilStatoOp;

	//bi-directional many-to-one association to SiacTBil
	/** The siac t bil. */
	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBil siacTBil;

	//bi-directional many-to-one association to SiacRBilStatoOpAttoAmm
	@OneToMany(mappedBy="siacRBilStatoOp")
	private List<SiacRBilStatoOpAttoAmm> siacRBilStatoOpAttoAmms;

	/**
	 * Instantiates a new siac r bil stato op.
	 */
	public SiacRBilStatoOp() {
	}

	/**
	 * Gets the bil bil stato op id.
	 *
	 * @return the bil bil stato op id
	 */
	public Integer getBilBilStatoOpId() {
		return this.bilBilStatoOpId;
	}

	/**
	 * Sets the bil bil stato op id.
	 *
	 * @param bilBilStatoOpId the new bil bil stato op id
	 */
	public void setBilBilStatoOpId(Integer bilBilStatoOpId) {
		this.bilBilStatoOpId = bilBilStatoOpId;
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

	public List<SiacRBilStatoOpAttoAmm> getSiacRBilStatoOpAttoAmms() {
		return this.siacRBilStatoOpAttoAmms;
	}

	public void setSiacRBilStatoOpAttoAmms(List<SiacRBilStatoOpAttoAmm> siacRBilStatoOpAttoAmms) {
		this.siacRBilStatoOpAttoAmms = siacRBilStatoOpAttoAmms;
	}

	public SiacRBilStatoOpAttoAmm addSiacRBilStatoOpAttoAmm(SiacRBilStatoOpAttoAmm siacRBilStatoOpAttoAmm) {
		getSiacRBilStatoOpAttoAmms().add(siacRBilStatoOpAttoAmm);
		siacRBilStatoOpAttoAmm.setSiacRBilStatoOp(this);

		return siacRBilStatoOpAttoAmm;
	}

	public SiacRBilStatoOpAttoAmm removeSiacRBilStatoOpAttoAmm(SiacRBilStatoOpAttoAmm siacRBilStatoOpAttoAmm) {
		getSiacRBilStatoOpAttoAmms().remove(siacRBilStatoOpAttoAmm);
		siacRBilStatoOpAttoAmm.setSiacRBilStatoOp(null);

		return siacRBilStatoOpAttoAmm;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return bilBilStatoOpId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.bilBilStatoOpId = uid;
	}

}