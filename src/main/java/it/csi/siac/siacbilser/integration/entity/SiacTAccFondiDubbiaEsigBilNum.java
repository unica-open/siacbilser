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
 * The Class SiacTAccFondiDubbiaEsigBil.
 */
@Entity
@Table(name="siac_t_acc_fondi_dubbia_esig_bil_num")
@NamedQuery(name="SiacTAccFondiDubbiaEsigBilNum.findAll", query="SELECT s FROM SiacTAccFondiDubbiaEsigBilNum s")
public class SiacTAccFondiDubbiaEsigBilNum extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The afde bil id. */
	@Id
	@SequenceGenerator(name="SIAC_T_ACC_FONDI_DUBBIA_ESIG_BIL_NUM_AFDEBILNUMID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_ACC_FONDI_DUBBIA_ESIG_BIL_NUM_AFDE_BIL_NUM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_ACC_FONDI_DUBBIA_ESIG_BIL_NUM_AFDEBILNUMID_GENERATOR")
	@Column(name="afde_bil_num_id")
	private Integer afdeBilNumId;
	
	/** The afde bil versione. */
	@Column(name="afde_bil_versione")
	@Version
	private Integer afdeBilVersione;

	/** The siac T bil. */
	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBil siacTBil;
	
	/** The siac D acc fondi dubbia esig. */
	@ManyToOne
	@JoinColumn(name="afde_tipo_id")
	private SiacDAccFondiDubbiaEsigTipo siacDAccFondiDubbiaEsigTipo;
	
	/**
	 * @return the afdeBilNumId
	 */
	public Integer getAfdeBilNumId() {
		return this.afdeBilNumId;
	}

	/**
	 * @param afdeBilNumId the afdeBilNumId to set
	 */
	public void setAfdeBilNumId(Integer afdeBilNumId) {
		this.afdeBilNumId = afdeBilNumId;
	}

	/**
	 * @return the afdeBilVersione
	 */
	public Integer getAfdeBilVersione() {
		return this.afdeBilVersione;
	}

	/**
	 * @param afdeBilVersione the afdeBilVersione to set
	 */
	public void setAfdeBilVersione(Integer afdeBilVersione) {
		this.afdeBilVersione = afdeBilVersione;
	}

	/**
	 * @return the siacTBil
	 */
	public SiacTBil getSiacTBil() {
		return this.siacTBil;
	}

	/**
	 * @param siacTBil the siacTBil to set
	 */
	public void setSiacTBil(SiacTBil siacTBil) {
		this.siacTBil = siacTBil;
	}

	/**
	 * @return the siacDAccFondiDubbiaEsigTipo
	 */
	public SiacDAccFondiDubbiaEsigTipo getSiacDAccFondiDubbiaEsigTipo() {
		return this.siacDAccFondiDubbiaEsigTipo;
	}

	/**
	 * @param siacDAccFondiDubbiaEsigTipo the siacDAccFondiDubbiaEsigTipo to set
	 */
	public void setSiacDAccFondiDubbiaEsigTipo(SiacDAccFondiDubbiaEsigTipo siacDAccFondiDubbiaEsigTipo) {
		this.siacDAccFondiDubbiaEsigTipo = siacDAccFondiDubbiaEsigTipo;
	}

	@Override
	public Integer getUid() {
		return afdeBilNumId;
	}

	@Override
	public void setUid(Integer uid) {
		this.afdeBilNumId = uid;
	}

}