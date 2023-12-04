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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_d_bil_stato_op database table.
 * 
 */
@Entity
@Table(name="siac_d_bil_stato_op")
public class SiacDBilStatoOp extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The bil stato op id. */
	@Id
	@SequenceGenerator(name="SIAC_D_BIL_STATO_OP_BILSTATOOPID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_BIL_STATO_OP_BIL_STATO_OP_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_BIL_STATO_OP_BILSTATOOPID_GENERATOR")
	@Column(name="bil_stato_op_id")
	private Integer bilStatoOpId;

	/** The bil stato op code. */
	@Column(name="bil_stato_op_code")
	private String bilStatoOpCode;

	/** The bil stato op desc. */
	@Column(name="bil_stato_op_desc")
	private String bilStatoOpDesc;

	//bi-directional many-to-one association to SiacRBilStatoOp
	/** The siac r bil stato ops. */
	@OneToMany(mappedBy="siacDBilStatoOp")
	private List<SiacRBilStatoOp> siacRBilStatoOps;

	//bi-directional many-to-one association to SiacRBilTipoStatoOp
	/** The siac r bil tipo stato ops. */
	@OneToMany(mappedBy="siacDBilStatoOp")
	private List<SiacRBilTipoStatoOp> siacRBilTipoStatoOps;

	//bi-directional many-to-one association to SiacRFaseOperativaBilStato
	/** The siac r fase operativa bil statos. */
	@OneToMany(mappedBy="siacDBilStatoOp")
	private List<SiacRFaseOperativaBilStato> siacRFaseOperativaBilStatos;

	/**
	 * Instantiates a new siac d bil stato op.
	 */
	public SiacDBilStatoOp() {
	}

	/**
	 * Gets the bil stato op id.
	 *
	 * @return the bil stato op id
	 */
	public Integer getBilStatoOpId() {
		return this.bilStatoOpId;
	}

	/**
	 * Sets the bil stato op id.
	 *
	 * @param bilStatoOpId the new bil stato op id
	 */
	public void setBilStatoOpId(Integer bilStatoOpId) {
		this.bilStatoOpId = bilStatoOpId;
	}

	/**
	 * Gets the bil stato op code.
	 *
	 * @return the bil stato op code
	 */
	public String getBilStatoOpCode() {
		return this.bilStatoOpCode;
	}

	/**
	 * Sets the bil stato op code.
	 *
	 * @param bilStatoOpCode the new bil stato op code
	 */
	public void setBilStatoOpCode(String bilStatoOpCode) {
		this.bilStatoOpCode = bilStatoOpCode;
	}

	/**
	 * Gets the bil stato op desc.
	 *
	 * @return the bil stato op desc
	 */
	public String getBilStatoOpDesc() {
		return this.bilStatoOpDesc;
	}

	/**
	 * Sets the bil stato op desc.
	 *
	 * @param bilStatoOpDesc the new bil stato op desc
	 */
	public void setBilStatoOpDesc(String bilStatoOpDesc) {
		this.bilStatoOpDesc = bilStatoOpDesc;
	}

	/**
	 * Gets the siac r bil stato ops.
	 *
	 * @return the siac r bil stato ops
	 */
	public List<SiacRBilStatoOp> getSiacRBilStatoOps() {
		return this.siacRBilStatoOps;
	}

	/**
	 * Sets the siac r bil stato ops.
	 *
	 * @param siacRBilStatoOps the new siac r bil stato ops
	 */
	public void setSiacRBilStatoOps(List<SiacRBilStatoOp> siacRBilStatoOps) {
		this.siacRBilStatoOps = siacRBilStatoOps;
	}

	/**
	 * Adds the siac r bil stato op.
	 *
	 * @param siacRBilStatoOp the siac r bil stato op
	 * @return the siac r bil stato op
	 */
	public SiacRBilStatoOp addSiacRBilStatoOp(SiacRBilStatoOp siacRBilStatoOp) {
		getSiacRBilStatoOps().add(siacRBilStatoOp);
		siacRBilStatoOp.setSiacDBilStatoOp(this);

		return siacRBilStatoOp;
	}

	/**
	 * Removes the siac r bil stato op.
	 *
	 * @param siacRBilStatoOp the siac r bil stato op
	 * @return the siac r bil stato op
	 */
	public SiacRBilStatoOp removeSiacRBilStatoOp(SiacRBilStatoOp siacRBilStatoOp) {
		getSiacRBilStatoOps().remove(siacRBilStatoOp);
		siacRBilStatoOp.setSiacDBilStatoOp(null);

		return siacRBilStatoOp;
	}

	/**
	 * Gets the siac r bil tipo stato ops.
	 *
	 * @return the siac r bil tipo stato ops
	 */
	public List<SiacRBilTipoStatoOp> getSiacRBilTipoStatoOps() {
		return this.siacRBilTipoStatoOps;
	}

	/**
	 * Sets the siac r bil tipo stato ops.
	 *
	 * @param siacRBilTipoStatoOps the new siac r bil tipo stato ops
	 */
	public void setSiacRBilTipoStatoOps(List<SiacRBilTipoStatoOp> siacRBilTipoStatoOps) {
		this.siacRBilTipoStatoOps = siacRBilTipoStatoOps;
	}

	/**
	 * Adds the siac r bil tipo stato op.
	 *
	 * @param siacRBilTipoStatoOp the siac r bil tipo stato op
	 * @return the siac r bil tipo stato op
	 */
	public SiacRBilTipoStatoOp addSiacRBilTipoStatoOp(SiacRBilTipoStatoOp siacRBilTipoStatoOp) {
		getSiacRBilTipoStatoOps().add(siacRBilTipoStatoOp);
		siacRBilTipoStatoOp.setSiacDBilStatoOp(this);

		return siacRBilTipoStatoOp;
	}

	/**
	 * Removes the siac r bil tipo stato op.
	 *
	 * @param siacRBilTipoStatoOp the siac r bil tipo stato op
	 * @return the siac r bil tipo stato op
	 */
	public SiacRBilTipoStatoOp removeSiacRBilTipoStatoOp(SiacRBilTipoStatoOp siacRBilTipoStatoOp) {
		getSiacRBilTipoStatoOps().remove(siacRBilTipoStatoOp);
		siacRBilTipoStatoOp.setSiacDBilStatoOp(null);

		return siacRBilTipoStatoOp;
	}

	/**
	 * Gets the siac r fase operativa bil statos.
	 *
	 * @return the siac r fase operativa bil statos
	 */
	public List<SiacRFaseOperativaBilStato> getSiacRFaseOperativaBilStatos() {
		return this.siacRFaseOperativaBilStatos;
	}

	/**
	 * Sets the siac r fase operativa bil statos.
	 *
	 * @param siacRFaseOperativaBilStatos the new siac r fase operativa bil statos
	 */
	public void setSiacRFaseOperativaBilStatos(List<SiacRFaseOperativaBilStato> siacRFaseOperativaBilStatos) {
		this.siacRFaseOperativaBilStatos = siacRFaseOperativaBilStatos;
	}

	/**
	 * Adds the siac r fase operativa bil stato.
	 *
	 * @param siacRFaseOperativaBilStato the siac r fase operativa bil stato
	 * @return the siac r fase operativa bil stato
	 */
	public SiacRFaseOperativaBilStato addSiacRFaseOperativaBilStato(SiacRFaseOperativaBilStato siacRFaseOperativaBilStato) {
		getSiacRFaseOperativaBilStatos().add(siacRFaseOperativaBilStato);
		siacRFaseOperativaBilStato.setSiacDBilStatoOp(this);

		return siacRFaseOperativaBilStato;
	}

	/**
	 * Removes the siac r fase operativa bil stato.
	 *
	 * @param siacRFaseOperativaBilStato the siac r fase operativa bil stato
	 * @return the siac r fase operativa bil stato
	 */
	public SiacRFaseOperativaBilStato removeSiacRFaseOperativaBilStato(SiacRFaseOperativaBilStato siacRFaseOperativaBilStato) {
		getSiacRFaseOperativaBilStatos().remove(siacRFaseOperativaBilStato);
		siacRFaseOperativaBilStato.setSiacDBilStatoOp(null);

		return siacRFaseOperativaBilStato;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return bilStatoOpId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.bilStatoOpId = uid;
	}

}