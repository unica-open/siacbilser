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
 * The persistent class for the siac_r_ruolo_op_azione database table.
 * 
 */
@Entity
@Table(name="siac_r_ruolo_op_azione")
public class SiacRRuoloOpAzione extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ruolo op azione id. */
	@Id
	@SequenceGenerator(name="SIAC_R_RUOLO_OP_AZIONE_RUOLOOPAZIONEID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_RUOLO_OP_AZIONE_RUOLO_OP_AZIONE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_RUOLO_OP_AZIONE_RUOLOOPAZIONEID_GENERATOR")
	@Column(name="ruolo_op_azione_id")
	private Integer ruoloOpAzioneId;


	//bi-directional many-to-one association to SiacDRuoloOp
	/** The siac d ruolo op. */
	@ManyToOne
	@JoinColumn(name="ruolo_op_id")
	private SiacDRuoloOp siacDRuoloOp;

	//bi-directional many-to-one association to SiacTAzione
	/** The siac t azione. */
	@ManyToOne
	@JoinColumn(name="azione_id")
	private SiacTAzione siacTAzione;

	/**
	 * Instantiates a new siac r ruolo op azione.
	 */
	public SiacRRuoloOpAzione() {
	}

	/**
	 * Gets the ruolo op azione id.
	 *
	 * @return the ruolo op azione id
	 */
	public Integer getRuoloOpAzioneId() {
		return this.ruoloOpAzioneId;
	}

	/**
	 * Sets the ruolo op azione id.
	 *
	 * @param ruoloOpAzioneId the new ruolo op azione id
	 */
	public void setRuoloOpAzioneId(Integer ruoloOpAzioneId) {
		this.ruoloOpAzioneId = ruoloOpAzioneId;
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
	 * Gets the siac t azione.
	 *
	 * @return the siac t azione
	 */
	public SiacTAzione getSiacTAzione() {
		return this.siacTAzione;
	}

	/**
	 * Sets the siac t azione.
	 *
	 * @param siacTAzione the new siac t azione
	 */
	public void setSiacTAzione(SiacTAzione siacTAzione) {
		this.siacTAzione = siacTAzione;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return ruoloOpAzioneId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.ruoloOpAzioneId = uid;
		
	}

}