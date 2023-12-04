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
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_r_programma_atto_amm database table.
 * 
 */
@Entity
@Table(name="siac_r_cronop_atto_amm")
@NamedQuery(name="SiacRCronopAttoAmm.findAll", query="SELECT s FROM SiacRCronopAttoAmm s")
public class SiacRCronopAttoAmm extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5380386736131284013L;

	/** The programma atto amm id. */
	@Id
	@SequenceGenerator(name="SIAC_R_CRONOP_ATTO_AMM_CRONOPATTOAMMID_GENERATOR", allocationSize=1, sequenceName="siac_r_cronop_atto_amm_cronop_atto_amm_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CRONOP_ATTO_AMM_CRONOPATTOAMMID_GENERATOR")
	@Column(name="cronop_atto_amm_id")
	private Integer cronopAttoAmmId;

	//bi-directional many-to-one association to SiacTAttoAmm
	/** The siac t atto amm. */
	@ManyToOne
	@JoinColumn(name="attoamm_id")
	private SiacTAttoAmm siacTAttoAmm;

	//bi-directional many-to-one association to SiacTCronop
	/** The siac t programma. */
	@ManyToOne
	@JoinColumn(name="cronop_id")
	private SiacTCronop siacTCronop;

	/**
	 * Instantiates a new siac r programma atto amm.
	 */
	public SiacRCronopAttoAmm() {
	}

	/**
	 * Gets the programma atto amm id.
	 *
	 * @return the programma atto amm id
	 */
	public Integer getCronopAttoAmmId() {
		return this.cronopAttoAmmId;
	}

	/**
	 * Sets the programma atto amm id.
	 *
	 * @param programmaAttoAmmId the new programma atto amm id
	 */
	public void setCronopAttoAmmId(Integer cronopAttoAmmId) {
		this.cronopAttoAmmId = cronopAttoAmmId;
	}
	
	/**
	 * Gets the siac t atto amm.
	 *
	 * @return the siac t atto amm
	 */
	public SiacTAttoAmm getSiacTAttoAmm() {
		return this.siacTAttoAmm;
	}

	/**
	 * Sets the siac t atto amm.
	 *
	 * @param siacTAttoAmm the new siac t atto amm
	 */
	public void setSiacTAttoAmm(SiacTAttoAmm siacTAttoAmm) {
		this.siacTAttoAmm = siacTAttoAmm;
	}

	/**
	 * Gets the siac t programma.
	 *
	 * @return the siac t programma
	 */
	public SiacTCronop getSiacTCronop() {
		return this.siacTCronop;
	}

	/**
	 * Sets the siac t programma.
	 *
	 * @param siacTProgramma the new siac t programma
	 */
	public void setSiacTCronop(SiacTCronop siacTProgramma) {
		this.siacTCronop = siacTProgramma;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return cronopAttoAmmId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.cronopAttoAmmId = uid;
	}

}