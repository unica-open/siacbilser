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
 * The persistent class for the siac_t_movimento database table.
 * 
 */
@Entity
@Table(name="siac_t_movimento_num")
public class SiacTMovimentoNum extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_MOVIMENTO_MOVTNUMID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_MOVIMENTO_NUM_MOVT_NUM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_MOVIMENTO_MOVTNUMID_GENERATOR")
	@Column(name="movt_num_id")
	private Integer movtNumId;

	@Column(name="movt_anno")
	private String movtAnno;

	/** The movt numero. */
	@Version
	@Column(name="movt_numero")
	private Integer movtNumero;
	
	//bi-directional many-to-one association to SiacTCassaEcon
	@ManyToOne
	@JoinColumn(name="cassaecon_id")
	private SiacTCassaEcon siacTCassaEcon;

	/**
	 * Instantiates a new siac t movimento num.
	 */
	public SiacTMovimentoNum() {
	}

	/**
	 * Gets the movt num id.
	 *
	 * @return the movt num id
	 */
	public Integer getMovtNumId() {
		return this.movtNumId;
	}

	/**
	 * Sets the movt num id.
	 *
	 * @param movtId the new movt num id
	 */
	public void setMovtNumId(Integer movtId) {
		this.movtNumId = movtId;
	}

	/**
	 * Gets the movt anno.
	 *
	 * @return the movtAnno
	 */
	public String getMovtAnno() {
		return movtAnno;
	}

	/**
	 * Sets the movt anno.
	 *
	 * @param movtAnno the movtAnno to set
	 */
	public void setMovtAnno(String movtAnno) {
		this.movtAnno = movtAnno;
	}

	/**
	 * Gets the movt numero.
	 *
	 * @return the movt numero
	 */
	public Integer getMovtNumero() {
		return this.movtNumero;
	}

	/**
	 * Sets the movt numero.
	 *
	 * @param movtNumero the new movt numero
	 */
	public void setMovtNumero(Integer movtNumero) {
		this.movtNumero = movtNumero;
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
		return this.movtNumId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.movtNumId = uid;
		
	}

}