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
 * The persistent class for the siac_t_richiesta_econ database table.
 * 
 */
@Entity
@Table(name="siac_t_richiesta_econ_num")
public class SiacTRichiestaEconNum extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_RICHIESTA_ECON_RICECONNUMID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_RICHIESTA_ECON_NUM_RICECON_NUM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_RICHIESTA_ECON_RICECONNUMID_GENERATOR")
	@Column(name="ricecon_num_id")
	private Integer riceconNumId;
	
	@Column(name="ricecon_anno")
	private String riceconAnno;

	@Version
	@Column(name="ricecon_numero")
	private Integer riceconNumero;
	
	//bi-directional many-to-one association to SiacTCassaEcon
	@ManyToOne
	@JoinColumn(name="cassaecon_id")
	private SiacTCassaEcon siacTCassaEcon;

	/**
	 * @return the riceconNumId
	 */
	public Integer getRiceconNumId() {
		return riceconNumId;
	}

	/**
	 * @param riceconNumId the riceconNumId to set
	 */
	public void setRiceconNumId(Integer riceconNumId) {
		this.riceconNumId = riceconNumId;
	}

	/**
	 * @return the riceconAnno
	 */
	public String getRiceconAnno() {
		return riceconAnno;
	}

	/**
	 * @param riceconAnno the riceconAnno to set
	 */
	public void setRiceconAnno(String riceconAnno) {
		this.riceconAnno = riceconAnno;
	}

	/**
	 * @return the riceconNumero
	 */
	public Integer getRiceconNumero() {
		return riceconNumero;
	}

	/**
	 * @param riceconNumero the riceconNumero to set
	 */
	public void setRiceconNumero(Integer riceconNumero) {
		this.riceconNumero = riceconNumero;
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
		return this.riceconNumId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.riceconNumId = uid;
		
	}
}