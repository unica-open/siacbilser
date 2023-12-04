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


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_variazione database table.
 * 
 */
@Entity
@Table(name="siac_t_variazione_num")
public class SiacTVariazioneNum extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	

	
	/** The variazione num id. */
	@Id
	@SequenceGenerator(name="SIAC_T_VARIAZIONE_NUM_VARIAZIONE_NUM_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_variazione_num_variazione_num_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_VARIAZIONE_NUM_VARIAZIONE_NUM_ID_GENERATOR")
	@Column(name="variazione_num_id")
	private Integer variazioneNumId;


	/** The siac t bil. */
	@ManyToOne
	@JoinColumn(name="bil_id", updatable=false)
	private SiacTBil siacTBil;

	/** The variazione num. */
	@Version
	@Column(name="variazione_num")
	private Integer variazioneNum;

	/**
	 * Sets the variazione num.
	 *
	 * @param variazioneNum the new variazione num
	 */
	public void setVariazioneNum(Integer variazioneNum) {
		this.variazioneNum = variazioneNum;
	}

	/**
	 * Gets the variazione num.
	 *
	 * @return the variazioneNum
	 */
	public Integer getVariazioneNum() {
		return variazioneNum;
	}
	
	

	/**
	 * Gets the siac t bil.
	 *
	 * @return the siacTBil
	 */
	public SiacTBil getSiacTBil() {
		return siacTBil;
	}

	/**
	 * Sets the siac t bil.
	 *
	 * @param siacTBil the siacTBil to set
	 */
	public void setSiacTBil(SiacTBil siacTBil) {
		this.siacTBil = siacTBil;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
//		if(siacTBil==null){
//			return null;
//		}
//		return siacTBil.getUid();
		return variazioneNumId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
//		if(siacTBil==null){
//			siacTBil = new SiacTBil();
//		}
//		siacTBil.setUid(uid);
		this.variazioneNumId = uid;
		
	}

	

	



}