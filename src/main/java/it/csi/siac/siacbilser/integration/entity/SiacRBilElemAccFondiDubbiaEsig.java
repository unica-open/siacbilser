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
 * The persistent class for the siac_r_bil_elem_acc_fondi_dubbia_esig database table.
 * 
 */
//SIAC-7858
@Deprecated
@Entity
@Table(name="siac_r_bil_elem_acc_fondi_dubbia_esig")
@NamedQuery(name="SiacRBilElemAccFondiDubbiaEsig.findAll", query="SELECT s FROM SiacRBilElemAccFondiDubbiaEsig s")
public class SiacRBilElemAccFondiDubbiaEsig extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The bil elem acc fde id. */
	@Id
	@SequenceGenerator(name="SIAC_R_BIL_ELEM_ACC_FONDI_DUBBIA_ESIG_BILELEMACCFDEID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_BIL_ELEM_ACC_FONDI_DUBBIA_ESIG_BIL_ELEM_ACC_FDE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_BIL_ELEM_ACC_FONDI_DUBBIA_ESIG_BILELEMACCFDEID_GENERATOR")
	@Column(name="bil_elem_acc_fde_id")
	private Integer bilElemAccFdeId;

	//bi-directional many-to-one association to SiacTBilElem
	/** The siac t bil elem. */
	@ManyToOne
	@JoinColumn(name="elem_id")
	private SiacTBilElem siacTBilElem;

	//bi-directional many-to-one association to SiacTAccFondiDubbiaEsig
	/** The siac t siac t acc fondi dubbia esig. */
	@ManyToOne
	@JoinColumn(name="acc_fde_id")
	private SiacTAccFondiDubbiaEsig siacTAccFondiDubbiaEsig;

	/**
	 * Instantiates a new siac r bil attr.
	 */
	public SiacRBilElemAccFondiDubbiaEsig() {
	}

	/**
	 * @return the bilElemAccFdeId
	 */
	public Integer getBilElemAccFdeId() {
		return bilElemAccFdeId;
	}

	/**
	 * @param bilElemAccFdeId the bilElemAccFdeId to set
	 */
	public void setBilElemAccFdeId(Integer bilElemAccFdeId) {
		this.bilElemAccFdeId = bilElemAccFdeId;
	}

	/**
	 * @return the siacTBilElem
	 */
	public SiacTBilElem getSiacTBilElem() {
		return siacTBilElem;
	}

	/**
	 * @param siacTBilElem the siacTBilElem to set
	 */
	public void setSiacTBilElem(SiacTBilElem siacTBilElem) {
		this.siacTBilElem = siacTBilElem;
	}

	/**
	 * @return the siacTAccFondiDubbiaEsig
	 */
	public SiacTAccFondiDubbiaEsig getSiacTAccFondiDubbiaEsig() {
		return siacTAccFondiDubbiaEsig;
	}

	/**
	 * @param siacTAccFondiDubbiaEsig the siacTAccFondiDubbiaEsig to set
	 */
	public void setSiacTAccFondiDubbiaEsig(SiacTAccFondiDubbiaEsig siacTAccFondiDubbiaEsig) {
		this.siacTAccFondiDubbiaEsig = siacTAccFondiDubbiaEsig;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return bilElemAccFdeId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.bilElemAccFdeId = uid;
		
	}

}