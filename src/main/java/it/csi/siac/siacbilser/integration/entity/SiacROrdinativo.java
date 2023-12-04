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
 * The persistent class for the siac_r_ordinativo database table.
 * 
 */
@Entity
@Table(name="siac_r_ordinativo")
@NamedQuery(name="SiacROrdinativo.findAll", query="SELECT s FROM SiacROrdinativo s")
public class SiacROrdinativo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ord r id. */
	@Id
	@SequenceGenerator(name="SIAC_R_ORDINATIVO_ORDRID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_ORDINATIVO_ORD_R_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ORDINATIVO_ORDRID_GENERATOR")
	@Column(name="ord_r_id")
	private Integer ordRId;

	/** The ord id. */
	@Column(name="ord_id")
	private Integer ordId;

	//bi-directional many-to-one association to SiacDRelazTipo
	/** The siac d relaz tipo. */
	@ManyToOne
	@JoinColumn(name="relaz_tipo_id")
	private SiacDRelazTipo siacDRelazTipo;

	//bi-directional many-to-one association to SiacTOrdinativo
	/** The siac t ordinativo1. */
	@ManyToOne
	@JoinColumn(name="ord_id_da")
	private SiacTOrdinativo siacTOrdinativo1;

	//bi-directional many-to-one association to SiacTOrdinativo
	/** The siac t ordinativo2. */
	@ManyToOne
	@JoinColumn(name="ord_id_a")
	private SiacTOrdinativo siacTOrdinativo2;

	/**
	 * Instantiates a new siac r ordinativo.
	 */
	public SiacROrdinativo() {
	}

	/**
	 * Gets the ord r id.
	 *
	 * @return the ord r id
	 */
	public Integer getOrdRId() {
		return this.ordRId;
	}

	/**
	 * Sets the ord r id.
	 *
	 * @param ordRId the new ord r id
	 */
	public void setOrdRId(Integer ordRId) {
		this.ordRId = ordRId;
	}

	/**
	 * Gets the ord id.
	 *
	 * @return the ord id
	 */
	public Integer getOrdId() {
		return this.ordId;
	}

	/**
	 * Sets the ord id.
	 *
	 * @param ordId the new ord id
	 */
	public void setOrdId(Integer ordId) {
		this.ordId = ordId;
	}	

	/**
	 * Gets the siac d relaz tipo.
	 *
	 * @return the siac d relaz tipo
	 */
	public SiacDRelazTipo getSiacDRelazTipo() {
		return this.siacDRelazTipo;
	}

	/**
	 * Sets the siac d relaz tipo.
	 *
	 * @param siacDRelazTipo the new siac d relaz tipo
	 */
	public void setSiacDRelazTipo(SiacDRelazTipo siacDRelazTipo) {
		this.siacDRelazTipo = siacDRelazTipo;
	}
	
	/**
	 * Gets the siac t ordinativo1.
	 *
	 * @return the siac t ordinativo1
	 */
	public SiacTOrdinativo getSiacTOrdinativo1() {
		return this.siacTOrdinativo1;
	}

	/**
	 * Sets the siac t ordinativo1.
	 *
	 * @param siacTOrdinativo1 the new siac t ordinativo1
	 */
	public void setSiacTOrdinativo1(SiacTOrdinativo siacTOrdinativo1) {
		this.siacTOrdinativo1 = siacTOrdinativo1;
	}

	/**
	 * Gets the siac t ordinativo2.
	 *
	 * @return the siac t ordinativo2
	 */
	public SiacTOrdinativo getSiacTOrdinativo2() {
		return this.siacTOrdinativo2;
	}

	/**
	 * Sets the siac t ordinativo2.
	 *
	 * @param siacTOrdinativo2 the new siac t ordinativo2
	 */
	public void setSiacTOrdinativo2(SiacTOrdinativo siacTOrdinativo2) {
		this.siacTOrdinativo2 = siacTOrdinativo2;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return ordRId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.ordRId = uid;
	}

}