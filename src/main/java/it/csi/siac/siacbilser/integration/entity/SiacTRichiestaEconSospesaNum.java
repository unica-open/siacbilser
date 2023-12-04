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
import javax.persistence.Version;


/**
 * The persistent class for the siac_t_richiesta_econ_sospesa database table.
 * 
 */
@Entity
@Table(name="siac_t_richiesta_econ_sospesa_num")
public class SiacTRichiestaEconSospesaNum extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_RICHIESTA_ECON_SOSPESA_RICECONSNUMID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_RICHIESTA_ECON_SOSPESA_NUM_RIECONS_NUM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_RICHIESTA_ECON_SOSPESA_RICECONSNUMID_GENERATOR")
	@Column(name="riecons_num_id")
	private Integer rieconsNumId;
	
	@Column(name="riecons_anno")
	private String rieconsAnno;

	@Version
	@Column(name="riecons_numero")
	private Integer rieconsNumero;

	//bi-directional many-to-one association to SiacTCassaEcon
	@ManyToOne
	@JoinColumn(name="cassaecon_id")
	private SiacTCassaEcon siacTCassaEcon;


	/**
	 * Instantiates a new siac t richiesta econ sospesa num.
	 */
	public SiacTRichiestaEconSospesaNum() {
	}
	
	/**
	 * Gets the ricecons num id.
	 *
	 * @return the riceconsNumId
	 */
	public Integer getRieconsNumId() {
		return rieconsNumId;
	}

	/**
	 * Sets the ricecons num id.
	 *
	 * @param riceconsNumId the riceconsNumId to set
	 */
	public void setRieconsNumId(Integer riceconsNumId) {
		this.rieconsNumId = riceconsNumId;
	}

	/**
	 * Gets the ricecons anno.
	 *
	 * @return the riceconsAnno
	 */
	public String getRieconsAnno() {
		return rieconsAnno;
	}

	/**
	 * Sets the ricecons anno.
	 *
	 * @param riceconsAnno the riceconsAnno to set
	 */
	public void setRieconsAnno(String riceconsAnno) {
		this.rieconsAnno = riceconsAnno;
	}

	/**
	 * Gets the riecons numero.
	 *
	 * @return the riceconsNumero
	 */
	public Integer getRieconsNumero() {
		return rieconsNumero;
	}

	/**
	 * Sets the riecons numero.
	 *
	 * @param riceconsNumero the riceconsNumero to set
	 */
	public void setRieconsNumero(Integer riceconsNumero) {
		this.rieconsNumero = riceconsNumero;
	}

	/**
	 * @return the siacTCassaEcon
	 */
	public SiacTCassaEcon getSiacTCassaEcon() {
		return siacTCassaEcon;
	}

	/**
	 * @param siacTCassaEcon the siacTCassaEcon to set
	 */
	public void setSiacTCassaEcon(SiacTCassaEcon siacTCassaEcon) {
		this.siacTCassaEcon = siacTCassaEcon;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return this.rieconsNumId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.rieconsNumId = uid;
	}

}