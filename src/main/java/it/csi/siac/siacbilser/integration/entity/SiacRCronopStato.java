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


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_r_cronop_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_cronop_stato")
@NamedQuery(name="SiacRCronopStato.findAll", query="SELECT s FROM SiacRCronopStato s")
public class SiacRCronopStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The cronop stato r id. */
	@Id
	@SequenceGenerator(name="SIAC_R_CRONOP_STATO_CRONOPSTATORID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CRONOP_STATO_CRONOP_STATO_R_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CRONOP_STATO_CRONOPSTATORID_GENERATOR")
	@Column(name="cronop_stato_r_id")
	private Integer cronopStatoRId;

	//bi-directional many-to-one association to SiacDCronopStato
	/** The siac d cronop stato. */
	@ManyToOne
	@JoinColumn(name="cronop_stato_id")
	private SiacDCronopStato siacDCronopStato;

	//bi-directional many-to-one association to SiacTCronop
	/** The siac t cronop. */
	@ManyToOne
	@JoinColumn(name="cronop_id")
	private SiacTCronop siacTCronop;

	/**
	 * Instantiates a new siac r cronop stato.
	 */
	public SiacRCronopStato() {
	}

	/**
	 * Gets the cronop stato r id.
	 *
	 * @return the cronop stato r id
	 */
	public Integer getCronopStatoRId() {
		return this.cronopStatoRId;
	}

	/**
	 * Gets the siac d cronop stato.
	 *
	 * @return the siac d cronop stato
	 */
	public SiacDCronopStato getSiacDCronopStato() {
		return siacDCronopStato;
	}

	/**
	 * Sets the siac d cronop stato.
	 *
	 * @param siacDCronopStato the new siac d cronop stato
	 */
	public void setSiacDCronopStato(SiacDCronopStato siacDCronopStato) {
		this.siacDCronopStato = siacDCronopStato;
	}

	/**
	 * Sets the cronop stato r id.
	 *
	 * @param cronopStatoRId the new cronop stato r id
	 */
	public void setCronopStatoRId(Integer cronopStatoRId) {
		this.cronopStatoRId = cronopStatoRId;
	}

	/**
	 * Gets the siac t cronop.
	 *
	 * @return the siac t cronop
	 */
	public SiacTCronop getSiacTCronop() {
		return this.siacTCronop;
	}

	/**
	 * Sets the siac t cronop.
	 *
	 * @param siacTCronop the new siac t cronop
	 */
	public void setSiacTCronop(SiacTCronop siacTCronop) {
		this.siacTCronop = siacTCronop;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return cronopStatoRId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.cronopStatoRId = uid;
		
	}

}