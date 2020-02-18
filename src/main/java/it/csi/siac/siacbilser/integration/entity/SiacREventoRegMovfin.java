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
 * The persistent class for the siac_r_evento_reg_movfin database table.
 * 
 */
@Entity
@Table(name="siac_r_evento_reg_movfin")
@NamedQuery(name="SiacREventoRegMovfin.findAll", query="SELECT s FROM SiacREventoRegMovfin s")
public class SiacREventoRegMovfin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_EVENTO_REG_MOVFIN_EVMOVFINID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_EVENTO_REG_MOVFIN_EVMOVFIN_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_EVENTO_REG_MOVFIN_EVMOVFINID_GENERATOR")
	@Column(name="evmovfin_id")
	private Integer evmovfinId;

	@Column(name="campo_pk_id")
	private Integer campoPkId;
	
	@Column(name="campo_pk_id_2")
	private Integer campoPkId2;

	//bi-directional many-to-one association to SiacDEvento
	@ManyToOne
	@JoinColumn(name="evento_id")
	private SiacDEvento siacDEvento;

	//bi-directional many-to-one association to SiacTRegMovfin
	@ManyToOne
	@JoinColumn(name="regmovfin_id")
	private SiacTRegMovfin siacTRegMovfin;

	/**
	 * Instantiates a new siac r evento reg movfin.
	 */
	public SiacREventoRegMovfin() {
	}

	/**
	 * Gets the evmovfin id.
	 *
	 * @return the evmovfin id
	 */
	public Integer getEvmovfinId() {
		return this.evmovfinId;
	}

	/**
	 * Sets the evmovfin id.
	 *
	 * @param evmovfinId the new evmovfin id
	 */
	public void setEvmovfinId(Integer evmovfinId) {
		this.evmovfinId = evmovfinId;
	}

	/**
	 * Gets the campo pk id.
	 *
	 * @return the campo pk id
	 */
	public Integer getCampoPkId() {
		return this.campoPkId;
	}

	/**
	 * Sets the campo pk id.
	 *
	 * @param campoPkId the new campo pk id
	 */
	public void setCampoPkId(Integer campoPkId) {
		this.campoPkId = campoPkId;
	}
	
	/**
	 * Gets the campo pk id2.
	 *
	 * @return the campoPkId2
	 */
	public Integer getCampoPkId2() {
		return campoPkId2;
	}

	/**
	 * Sets the campo pk id2.
	 *
	 * @param campoPkId2 the campoPkId2 to set
	 */
	public void setCampoPkId2(Integer campoPkId2) {
		this.campoPkId2 = campoPkId2;
	}

	/**
	 * Gets the siac d evento.
	 *
	 * @return the siacDEvento
	 */
	public SiacDEvento getSiacDEvento() {
		return siacDEvento;
	}

	/**
	 * Sets the siac d evento.
	 *
	 * @param siacDEvento the siacDEvento to set
	 */
	public void setSiacDEvento(SiacDEvento siacDEvento) {
		this.siacDEvento = siacDEvento;
	}

	/**
	 * Gets the siac t reg movfin.
	 *
	 * @return the siac t reg movfin
	 */
	public SiacTRegMovfin getSiacTRegMovfin() {
		return this.siacTRegMovfin;
	}

	/**
	 * Sets the siac t reg movfin.
	 *
	 * @param siacTRegMovfin the new siac t reg movfin
	 */
	public void setSiacTRegMovfin(SiacTRegMovfin siacTRegMovfin) {
		this.siacTRegMovfin = siacTRegMovfin;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return this.evmovfinId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.evmovfinId = uid;
		
	}

}