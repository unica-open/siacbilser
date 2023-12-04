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
import javax.persistence.Version;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_mov_ep_num database table.
 * 
 */
@Entity
@Table(name="siac_t_mov_ep_num")
@NamedQuery(name="SiacTMovEpNum.findAll", query="SELECT s FROM SiacTMovEpNum s")
public class SiacTMovEpNum extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The movep num id. */
	@Id
	@SequenceGenerator(name="SIAC_T_MOV_EP_MOVEONUMID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_MOV_EP_NUM_MOVEP_NUM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_MOV_EP_MOVEONUMID_GENERATOR")
	@Column(name="movep_num_id")
	private Integer movepNumId;
	
	/** The movep anno. */
	@Version
	@Column(name="movep_anno")
	private Integer movepAnno;
	
	/** The movep code. */
	@Version
	@Column(name="movep_code")
	private Integer movepCode;
	
	//bi-directional many-to-one association to SiacDAmbito
	@ManyToOne
	@JoinColumn(name="ambito_id")
	private SiacDAmbito siacDAmbito;
	
	/**
	 * Instantiates a new siac t predoc num.
	 */
	public SiacTMovEpNum() {
	}

	/**
	 * @return the movepNumId
	 */
	public Integer getMovepNumId() {
		return movepNumId;
	}

	/**
	 * @param movepNumId the movepNumId to set
	 */
	public void setMovepNumId(Integer movepNumId) {
		this.movepNumId = movepNumId;
	}

	/**
	 * @return the movepAnno
	 */
	public Integer getMovepAnno() {
		return movepAnno;
	}

	/**
	 * @param movepAnno the movepAnno to set
	 */
	public void setMovepAnno(Integer movepAnno) {
		this.movepAnno = movepAnno;
	}

	/**
	 * @return the movepCode
	 */
	public Integer getMovepCode() {
		return movepCode;
	}

	/**
	 * @param movepCode the movepCode to set
	 */
	public void setMovepCode(Integer movepCode) {
		this.movepCode = movepCode;
	}

	public SiacDAmbito getSiacDAmbito() {
		return siacDAmbito;
	}

	public void setSiacDAmbito(SiacDAmbito siacDAmbito) {
		this.siacDAmbito = siacDAmbito;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return movepNumId;
	}
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.movepNumId = uid;		
	}


}