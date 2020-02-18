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
 * The persistent class for the siac_r_gruppo_ruolo_op database table.
 * 
 */
@Entity
@Table(name="siac_r_gruppo_ruolo_op")
public class SiacRGruppoRuoloOp extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The gruppo ruolo op id. */
	@Id
	@SequenceGenerator(name="SIAC_R_GRUPPO_RUOLO_OP_GRUPPORUOLOOPID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_GRUPPO_RUOLO_OP_GRUPPO_RUOLO_OP_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_GRUPPO_RUOLO_OP_GRUPPORUOLOOPID_GENERATOR")
	@Column(name="gruppo_ruolo_op_id")
	private Integer gruppoRuoloOpId;


	//bi-directional many-to-one association to SiacDRuoloOp
	/** The siac d ruolo op. */
	@ManyToOne
	@JoinColumn(name="ruolo_operativo_id")
	private SiacDRuoloOp siacDRuoloOp;

	//bi-directional many-to-one association to SiacTClass
	/** The siac t class. */
	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClass siacTClass;

	//bi-directional many-to-one association to SiacTGruppo
	/** The siac t gruppo. */
	@ManyToOne
	@JoinColumn(name="gruppo_id")
	private SiacTGruppo siacTGruppo;

	/**
	 * Instantiates a new siac r gruppo ruolo op.
	 */
	public SiacRGruppoRuoloOp() {
	}

	/**
	 * Gets the gruppo ruolo op id.
	 *
	 * @return the gruppo ruolo op id
	 */
	public Integer getGruppoRuoloOpId() {
		return this.gruppoRuoloOpId;
	}

	/**
	 * Sets the gruppo ruolo op id.
	 *
	 * @param gruppoRuoloOpId the new gruppo ruolo op id
	 */
	public void setGruppoRuoloOpId(Integer gruppoRuoloOpId) {
		this.gruppoRuoloOpId = gruppoRuoloOpId;
	}


	/**
	 * Gets the siac d ruolo op.
	 *
	 * @return the siac d ruolo op
	 */
	public SiacDRuoloOp getSiacDRuoloOp() {
		return this.siacDRuoloOp;
	}

	/**
	 * Sets the siac d ruolo op.
	 *
	 * @param siacDRuoloOp the new siac d ruolo op
	 */
	public void setSiacDRuoloOp(SiacDRuoloOp siacDRuoloOp) {
		this.siacDRuoloOp = siacDRuoloOp;
	}

	/**
	 * Gets the siac t class.
	 *
	 * @return the siac t class
	 */
	public SiacTClass getSiacTClass() {
		return this.siacTClass;
	}

	/**
	 * Sets the siac t class.
	 *
	 * @param siacTClass the new siac t class
	 */
	public void setSiacTClass(SiacTClass siacTClass) {
		this.siacTClass = siacTClass;
	}

	/**
	 * Gets the siac t gruppo.
	 *
	 * @return the siac t gruppo
	 */
	public SiacTGruppo getSiacTGruppo() {
		return this.siacTGruppo;
	}

	/**
	 * Sets the siac t gruppo.
	 *
	 * @param siacTGruppo the new siac t gruppo
	 */
	public void setSiacTGruppo(SiacTGruppo siacTGruppo) {
		this.siacTGruppo = siacTGruppo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return gruppoRuoloOpId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.gruppoRuoloOpId = uid;
		
	}

}