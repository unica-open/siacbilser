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
 * The persistent class for the siac_r_ordinativo_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_ordinativo_stato")
@NamedQuery(name="SiacROrdinativoStato.findAll", query="SELECT s FROM SiacROrdinativoStato s")
public class SiacROrdinativoStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ordinativo stato r id. */
	@Id
	@SequenceGenerator(name="SIAC_R_ORDINATIVO_STATO_ORDINATIVOSTATORID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_ORDINATIVO_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ORDINATIVO_STATO_ORDINATIVOSTATORID_GENERATOR")
	@Column(name="ord_stato_r_id")
	private Integer ordStatoRId;

	//bi-directional many-to-one association to SiacDOrdinativoStato
	/** The siac d ordinativo stato. */
	@ManyToOne
	@JoinColumn(name="ord_stato_id")
	private SiacDOrdinativoStato siacDOrdinativoStato;

	//bi-directional many-to-one association to SiacTOrdinativo
	/** The siac t ordinativo. */
	@ManyToOne
	@JoinColumn(name="ord_id")
	private SiacTOrdinativo siacTOrdinativo;

	/**
	 * Instantiates a new siac r ordinativo stato.
	 */
	public SiacROrdinativoStato() {
	}

	public Integer getOrdStatoRId() {
		return this.ordStatoRId;
	}

	public void setOrdStatoRId(Integer ordStatoRId) {
		this.ordStatoRId = ordStatoRId;
	}

	/**
	 * Gets the siac d ordinativo stato.
	 *
	 * @return the siac d ordinativo stato
	 */
	public SiacDOrdinativoStato getSiacDOrdinativoStato() {
		return this.siacDOrdinativoStato;
	}

	/**
	 * Sets the siac d ordinativo stato.
	 *
	 * @param siacDOrdinativoStato the new siac d ordinativo stato
	 */
	public void setSiacDOrdinativoStato(SiacDOrdinativoStato siacDOrdinativoStato) {
		this.siacDOrdinativoStato = siacDOrdinativoStato;
	}

	/**
	 * Gets the siac t ordinativo.
	 *
	 * @return the siac t ordinativo
	 */
	public SiacTOrdinativo getSiacTOrdinativo() {
		return this.siacTOrdinativo;
	}

	/**
	 * Sets the siac t ordinativo.
	 *
	 * @param siacTOrdinativo the new siac t ordinativo
	 */
	public void setSiacTOrdinativo(SiacTOrdinativo siacTOrdinativo) {
		this.siacTOrdinativo = siacTOrdinativo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return ordStatoRId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		ordStatoRId = uid;
	}

}