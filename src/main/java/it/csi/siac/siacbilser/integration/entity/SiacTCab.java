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
 * The persistent class for the siac_t_eldoc database table.
 * 
 */
@Entity
@Table(name="siac_t_cab")
@NamedQuery(name="SiacTCab.findAll", query="SELECT c FROM SiacTCab c")
public class SiacTCab extends SiacTEnteBase {
	
	
	private static final long serialVersionUID = -7458659409295381967L;

	@Id
	@SequenceGenerator(name="SIAC_T_CAB_GENERATOR", allocationSize=1, sequenceName="SIAC_T_CAB_CAB_ID_SEQ") 
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_CAB_GENERATOR")
	@Column(name="cab_id")
	private Integer cabId;
	
	@Column(name="cab_abi")
	private String cabAbi;
	
	@Column(name="cab_code")
	private String cabCode;
	
	@Column(name="cab_desc")
	private String cabDesc;
	
	@Column(name="cab_citta")
	private String cabCitta;
	
	@Column(name="cab_indirizzo")
	private String cabIndirizzo;
	
	@Column(name="cab_cap")
	private String cabCap;
	
	@Column(name="cab_provincia")
	private String cabProvincia;
	
	@Column(name="nazione_id")
	private Integer nazioneId;
	
	@ManyToOne
	@JoinColumn(name="abi_id")
	private SiacTAbi siacTAbi;
	

	/**
	 * Instantiates a new siac t doc num.
	 */
	public SiacTCab() {
	}

	/**
	 * @return the cabId
	 */
	public Integer getCabId() {
		return cabId;
	}

	/**
	 * @param cabId the cabId to set
	 */
	public void setCabId(Integer cabId) {
		this.cabId = cabId;
	}

	/**
	 * @return the cabCode
	 */
	public String getCabCode() {
		return cabCode;
	}

	/**
	 * @param cabCode the cabCode to set
	 */
	public void setCabCode(String cabCode) {
		this.cabCode = cabCode;
	}

	/**
	 * @return the cabDesc
	 */
	public String getCabDesc() {
		return cabDesc;
	}

	/**
	 * @param cabDesc the cabDesc to set
	 */
	public void setCabDesc(String cabDesc) {
		this.cabDesc = cabDesc;
	}

	/**
	 * @return the cabAbi
	 */
	public String getCabAbi() {
		return cabAbi;
	}

	/**
	 * @param cabAbi the cabAbi to set
	 */
	public void setCabAbi(String cabAbi) {
		this.cabAbi = cabAbi;
	}

	/**
	 * @return the cabCitta
	 */
	public String getCabCitta() {
		return cabCitta;
	}

	/**
	 * @param cabCitta the cabCitta to set
	 */
	public void setCabCitta(String cabCitta) {
		this.cabCitta = cabCitta;
	}

	/**
	 * @return the cabIndirizzo
	 */
	public String getCabIndirizzo() {
		return cabIndirizzo;
	}

	/**
	 * @param cabIndirizzo the cabIndirizzo to set
	 */
	public void setCabIndirizzo(String cabIndirizzo) {
		this.cabIndirizzo = cabIndirizzo;
	}

	/**
	 * @return the cabCap
	 */
	public String getCabCap() {
		return cabCap;
	}

	/**
	 * @param cabCap the cabCap to set
	 */
	public void setCabCap(String cabCap) {
		this.cabCap = cabCap;
	}

	/**
	 * @return the cabProvincia
	 */
	public String getCabProvincia() {
		return cabProvincia;
	}

	/**
	 * @param cabProvincia the cabProvincia to set
	 */
	public void setCabProvincia(String cabProvincia) {
		this.cabProvincia = cabProvincia;
	}

	/**
	 * @return the nazioneId
	 */
	public Integer getNazioneId() {
		return nazioneId;
	}

	/**
	 * @param nazioneId the nazioneId to set
	 */
	public void setNazioneId(Integer nazioneId) {
		this.nazioneId = nazioneId;
	}

	/**
	 * @return the siacTAbi
	 */
	public SiacTAbi getSiacTAbi() {
		return siacTAbi;
	}

	/**
	 * @param siacTAbi the siacTAbi to set
	 */
	public void setSiacTAbi(SiacTAbi siacTAbi) {
		this.siacTAbi = siacTAbi;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return cabId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.cabId = uid;		
	}


}