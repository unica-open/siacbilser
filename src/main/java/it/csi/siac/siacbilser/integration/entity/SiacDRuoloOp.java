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

/**
 * The persistent class for the siac_d_ruolo_op database table.
 * 
 */
@Entity
@Table(name="siac_d_ruolo_op")
public class SiacDRuoloOp extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ruolo op id. */
	@Id
	@SequenceGenerator(name="SIAC_D_RUOLO_OP_RUOLOOPID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_RUOLO_OP_RUOLO_OP_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_RUOLO_OP_RUOLOOPID_GENERATOR")
	@Column(name="ruolo_op_id")
	private Integer ruoloOpId;

	/** The ruolo op code. */
	@Column(name="ruolo_op_code")
	private String ruoloOpCode;

	/** The ruolo op desc. */
	@Column(name="ruolo_op_desc")
	private String ruoloOpDesc;

	//bi-directional many-to-one association to SiacRAccountRuoloOp
	/** The siac r account ruolo ops. */
	@OneToMany(mappedBy="siacDRuoloOp")
	private List<SiacRAccountRuoloOp> siacRAccountRuoloOps;

	//bi-directional many-to-one association to SiacRGruppoRuoloOp
	/** The siac r gruppo ruolo ops. */
	@OneToMany(mappedBy="siacDRuoloOp")
	private List<SiacRGruppoRuoloOp> siacRGruppoRuoloOps;

	//bi-directional many-to-one association to SiacRGruppoRuoloOpCassaEcon
	@OneToMany(mappedBy="siacDRuoloOp")
	private List<SiacRGruppoRuoloOpCassaEcon> siacRGruppoRuoloOpCassaEcons;

	//bi-directional many-to-one association to SiacRRuoloOpAzione
	/** The siac r ruolo op aziones. */
	@OneToMany(mappedBy="siacDRuoloOp")
	private List<SiacRRuoloOpAzione> siacRRuoloOpAziones;

	/**
	 * Instantiates a new siac d ruolo op.
	 */
	public SiacDRuoloOp() {
	}

	/**
	 * Gets the ruolo op id.
	 *
	 * @return the ruolo op id
	 */
	public Integer getRuoloOpId() {
		return this.ruoloOpId;
	}

	/**
	 * Sets the ruolo op id.
	 *
	 * @param ruoloOpId the new ruolo op id
	 */
	public void setRuoloOpId(Integer ruoloOpId) {
		this.ruoloOpId = ruoloOpId;
	}

	/**
	 * Gets the ruolo op code.
	 *
	 * @return the ruolo op code
	 */
	public String getRuoloOpCode() {
		return this.ruoloOpCode;
	}

	/**
	 * Sets the ruolo op code.
	 *
	 * @param ruoloOpCode the new ruolo op code
	 */
	public void setRuoloOpCode(String ruoloOpCode) {
		this.ruoloOpCode = ruoloOpCode;
	}

	/**
	 * Gets the ruolo op desc.
	 *
	 * @return the ruolo op desc
	 */
	public String getRuoloOpDesc() {
		return this.ruoloOpDesc;
	}

	/**
	 * Sets the ruolo op desc.
	 *
	 * @param ruoloOpDesc the new ruolo op desc
	 */
	public void setRuoloOpDesc(String ruoloOpDesc) {
		this.ruoloOpDesc = ruoloOpDesc;
	}

	/**
	 * Gets the siac r account ruolo ops.
	 *
	 * @return the siac r account ruolo ops
	 */
	public List<SiacRAccountRuoloOp> getSiacRAccountRuoloOps() {
		return this.siacRAccountRuoloOps;
	}

	/**
	 * Sets the siac r account ruolo ops.
	 *
	 * @param siacRAccountRuoloOps the new siac r account ruolo ops
	 */
	public void setSiacRAccountRuoloOps(List<SiacRAccountRuoloOp> siacRAccountRuoloOps) {
		this.siacRAccountRuoloOps = siacRAccountRuoloOps;
	}

	/**
	 * Adds the siac r account ruolo op.
	 *
	 * @param siacRAccountRuoloOp the siac r account ruolo op
	 * @return the siac r account ruolo op
	 */
	public SiacRAccountRuoloOp addSiacRAccountRuoloOp(SiacRAccountRuoloOp siacRAccountRuoloOp) {
		getSiacRAccountRuoloOps().add(siacRAccountRuoloOp);
		siacRAccountRuoloOp.setSiacDRuoloOp(this);

		return siacRAccountRuoloOp;
	}

	/**
	 * Removes the siac r account ruolo op.
	 *
	 * @param siacRAccountRuoloOp the siac r account ruolo op
	 * @return the siac r account ruolo op
	 */
	public SiacRAccountRuoloOp removeSiacRAccountRuoloOp(SiacRAccountRuoloOp siacRAccountRuoloOp) {
		getSiacRAccountRuoloOps().remove(siacRAccountRuoloOp);
		siacRAccountRuoloOp.setSiacDRuoloOp(null);

		return siacRAccountRuoloOp;
	}

	/**
	 * Gets the siac r gruppo ruolo ops.
	 *
	 * @return the siac r gruppo ruolo ops
	 */
	public List<SiacRGruppoRuoloOp> getSiacRGruppoRuoloOps() {
		return this.siacRGruppoRuoloOps;
	}

	/**
	 * Sets the siac r gruppo ruolo ops.
	 *
	 * @param siacRGruppoRuoloOps the new siac r gruppo ruolo ops
	 */
	public void setSiacRGruppoRuoloOps(List<SiacRGruppoRuoloOp> siacRGruppoRuoloOps) {
		this.siacRGruppoRuoloOps = siacRGruppoRuoloOps;
	}

	/**
	 * Adds the siac r gruppo ruolo op.
	 *
	 * @param siacRGruppoRuoloOp the siac r gruppo ruolo op
	 * @return the siac r gruppo ruolo op
	 */
	public SiacRGruppoRuoloOp addSiacRGruppoRuoloOp(SiacRGruppoRuoloOp siacRGruppoRuoloOp) {
		getSiacRGruppoRuoloOps().add(siacRGruppoRuoloOp);
		siacRGruppoRuoloOp.setSiacDRuoloOp(this);

		return siacRGruppoRuoloOp;
	}

	/**
	 * Removes the siac r gruppo ruolo op.
	 *
	 * @param siacRGruppoRuoloOp the siac r gruppo ruolo op
	 * @return the siac r gruppo ruolo op
	 */
	public SiacRGruppoRuoloOp removeSiacRGruppoRuoloOp(SiacRGruppoRuoloOp siacRGruppoRuoloOp) {
		getSiacRGruppoRuoloOps().remove(siacRGruppoRuoloOp);
		siacRGruppoRuoloOp.setSiacDRuoloOp(null);

		return siacRGruppoRuoloOp;
	}

	public List<SiacRGruppoRuoloOpCassaEcon> getSiacRGruppoRuoloOpCassaEcons() {
		return this.siacRGruppoRuoloOpCassaEcons;
	}

	public void setSiacRGruppoRuoloOpCassaEcons(List<SiacRGruppoRuoloOpCassaEcon> siacRGruppoRuoloOpCassaEcons) {
		this.siacRGruppoRuoloOpCassaEcons = siacRGruppoRuoloOpCassaEcons;
	}

	public SiacRGruppoRuoloOpCassaEcon addSiacRGruppoRuoloOpCassaEcon(SiacRGruppoRuoloOpCassaEcon siacRGruppoRuoloOpCassaEcon) {
		getSiacRGruppoRuoloOpCassaEcons().add(siacRGruppoRuoloOpCassaEcon);
		siacRGruppoRuoloOpCassaEcon.setSiacDRuoloOp(this);

		return siacRGruppoRuoloOpCassaEcon;
	}

	public SiacRGruppoRuoloOpCassaEcon removeSiacRGruppoRuoloOpCassaEcon(SiacRGruppoRuoloOpCassaEcon siacRGruppoRuoloOpCassaEcon) {
		getSiacRGruppoRuoloOpCassaEcons().remove(siacRGruppoRuoloOpCassaEcon);
		siacRGruppoRuoloOpCassaEcon.setSiacDRuoloOp(null);

		return siacRGruppoRuoloOpCassaEcon;
	}

	/**
	 * Gets the siac r ruolo op aziones.
	 *
	 * @return the siac r ruolo op aziones
	 */
	public List<SiacRRuoloOpAzione> getSiacRRuoloOpAziones() {
		return this.siacRRuoloOpAziones;
	}

	/**
	 * Sets the siac r ruolo op aziones.
	 *
	 * @param siacRRuoloOpAziones the new siac r ruolo op aziones
	 */
	public void setSiacRRuoloOpAziones(List<SiacRRuoloOpAzione> siacRRuoloOpAziones) {
		this.siacRRuoloOpAziones = siacRRuoloOpAziones;
	}

	/**
	 * Adds the siac r ruolo op azione.
	 *
	 * @param siacRRuoloOpAzione the siac r ruolo op azione
	 * @return the siac r ruolo op azione
	 */
	public SiacRRuoloOpAzione addSiacRRuoloOpAzione(SiacRRuoloOpAzione siacRRuoloOpAzione) {
		getSiacRRuoloOpAziones().add(siacRRuoloOpAzione);
		siacRRuoloOpAzione.setSiacDRuoloOp(this);

		return siacRRuoloOpAzione;
	}

	/**
	 * Removes the siac r ruolo op azione.
	 *
	 * @param siacRRuoloOpAzione the siac r ruolo op azione
	 * @return the siac r ruolo op azione
	 */
	public SiacRRuoloOpAzione removeSiacRRuoloOpAzione(SiacRRuoloOpAzione siacRRuoloOpAzione) {
		getSiacRRuoloOpAziones().remove(siacRRuoloOpAzione);
		siacRRuoloOpAzione.setSiacDRuoloOp(null);

		return siacRRuoloOpAzione;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return ruoloOpId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.ruoloOpId = uid;
	}

}