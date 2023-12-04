/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_t_bil_elem database table.
 * 
 */
@Entity
@Table(name="siac_d_acc_fondi_dubbia_esig_tipo")
@NamedQuery(name="SiacDAccFondiDubbiaEsigTipo.findAll", query="SELECT s FROM SiacDAccFondiDubbiaEsigTipo s")
public class SiacDAccFondiDubbiaEsigTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The afde tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_ACC_FONDI_DUBBIA_ESIG_TIPO_AFDETIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_ACC_FONDI_DUBBIA_ESIG_TIPO_AFDE_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_ACC_FONDI_DUBBIA_ESIG_TIPO_AFDETIPOID_GENERATOR")
	@Column(name="afde_tipo_id")
	private Integer afdeTipoId;
	
	/** The afde tipo code. */
	@Column(name="afde_tipo_code")
	private String afdeTipoCode;
	
	/** The afde tipo descr. */
	@Column(name="afde_tipo_desc")
	private String afdeTipoDesc;
	
	/** The siac T acc fondi dubbia esigs. */
	@OneToMany(mappedBy="siacDAccFondiDubbiaEsigTipo")
	private List<SiacTAccFondiDubbiaEsig> siacTAccFondiDubbiaEsigs;
	
	/** The siac T acc fondi dubbia esig bils. */
	@OneToMany(mappedBy="siacDAccFondiDubbiaEsigTipo")
	private List<SiacTAccFondiDubbiaEsigBil> siacTAccFondiDubbiaEsigBils;
	
	/**
	 * Gets the afde tipo id.
	 *
	 * @return the afdeTipoId
	 */
	public Integer getAfdeTipoId() {
		return this.afdeTipoId;
	}

	/**
	 * Sets the afde tipo id.
	 *
	 * @param afdeTipoId the afdeTipoId to set
	 */
	public void setAfdeTipoId(Integer afdeTipoId) {
		this.afdeTipoId = afdeTipoId;
	}

	/**
	 * Gets the afde tipo code.
	 *
	 * @return the afdeTipoCode
	 */
	public String getAfdeTipoCode() {
		return this.afdeTipoCode;
	}

	/**
	 * Sets the afde tipo code.
	 *
	 * @param afdeTipoCode the afdeTipoCode to set
	 */
	public void setAfdeTipoCode(String afdeTipoCode) {
		this.afdeTipoCode = afdeTipoCode;
	}

	/**
	 * Gets the afde tipo desr.
	 *
	 * @return the afdeTipoDesc
	 */
	public String getAfdeTipoDesc() {
		return this.afdeTipoDesc;
	}

	/**
	 * Sets the afde tipo desc.
	 *
	 * @param afdeTipoDesc the afdeTipoDesc to set
	 */
	public void setAfdeTipoDesc(String afdeTipoDesc) {
		this.afdeTipoDesc = afdeTipoDesc;
	}

	/**
	 * Gets the siac T acc fondi dubbia esigs.
	 *
	 * @return the siacTAccFondiDubbiaEsigs
	 */
	public List<SiacTAccFondiDubbiaEsig> getSiacTAccFondiDubbiaEsigs() {
		return this.siacTAccFondiDubbiaEsigs;
	}

	/**
	 * Sets the siac T acc fondi dubbia esigs.
	 *
	 * @param siacTAccFondiDubbiaEsigs the siacTAccFondiDubbiaEsigs to set
	 */
	public void setSiacTAccFondiDubbiaEsigs(List<SiacTAccFondiDubbiaEsig> siacTAccFondiDubbiaEsigs) {
		this.siacTAccFondiDubbiaEsigs = siacTAccFondiDubbiaEsigs;
	}

	/**
	 * Gets the siac T acc fondi dubbia esig bils.
	 *
	 * @return the siacTAccFondiDubbiaEsigBils
	 */
	public List<SiacTAccFondiDubbiaEsigBil> getSiacTAccFondiDubbiaEsigBils() {
		return this.siacTAccFondiDubbiaEsigBils;
	}

	/**
	 * Sets the siac T acc fondi dubbia esig bils.
	 *
	 * @param siacTAccFondiDubbiaEsigBils the siacTAccFondiDubbiaEsigBils to set
	 */
	public void setSiacTAccFondiDubbiaEsigBils(List<SiacTAccFondiDubbiaEsigBil> siacTAccFondiDubbiaEsigBils) {
		this.siacTAccFondiDubbiaEsigBils = siacTAccFondiDubbiaEsigBils;
	}

	/**
	 * Gets the uid.
	 *
	 * @return the uid
	 */
	@Override
	public Integer getUid() {
		return afdeTipoId;
	}

	/**
	 * Sets the uid.
	 *
	 * @param uid the new uid
	 */
	@Override
	public void setUid(Integer uid) {
		this.afdeTipoId = uid;
	}

}