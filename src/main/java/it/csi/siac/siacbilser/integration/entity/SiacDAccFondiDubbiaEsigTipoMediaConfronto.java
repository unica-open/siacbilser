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
 * The persistent class for the siac_d_acc_fondi_dubbia_esig_tipo_media_confronto database table.
 * 
 */
@Entity
@Table(name="siac_d_acc_fondi_dubbia_esig_tipo_media_confronto")
@NamedQuery(name="SiacDAccFondiDubbiaEsigTipoMediaConfronto.findAll", query="SELECT s FROM SiacDAccFondiDubbiaEsigTipoMediaConfronto s")
public class SiacDAccFondiDubbiaEsigTipoMediaConfronto extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The afde tipo media conf id. */
	@Id
	@SequenceGenerator(name="SIAC_D_ACC_FONDI_DUBBIA_ESIG_TIPO_MEDIA_CONFRONTO_AFDETIPOMEDIACONFID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_ACC_FONDI_DUBBIA_ESIG_TIPO_M_AFDE_TIPO_MEDIA_CONF_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_ACC_FONDI_DUBBIA_ESIG_TIPO_MEDIA_CONFRONTO_AFDETIPOMEDIACONFID_GENERATOR")
	@Column(name="afde_tipo_media_conf_id")
	private Integer afdeTipoMediaConfId;
	
	/** The afde tipo media conf code. */
	@Column(name="afde_tipo_media_conf_code")
	private String afdeTipoMediaConfCode;
	
	/** The afde tipo media conf desc. */
	@Column(name="afde_tipo_media_conf_desc")
	private String afdeTipoMediaConfDesc;
	
	/** The siac T acc fondi dubbia esigs. */
	@OneToMany(mappedBy="siacDAccFondiDubbiaEsigTipoMediaConfronto")
	private List<SiacTAccFondiDubbiaEsig> siacTAccFondiDubbiaEsigs;

	/**
	 * @return the afdeTipoMediaConfId
	 */
	public Integer getAfdeTipoMediaConfId() {
		return this.afdeTipoMediaConfId;
	}

	/**
	 * @param afdeTipoMediaConfId the afdeTipoMediaConfId to set
	 */
	public void setAfdeTipoMediaConfId(Integer afdeTipoMediaConfId) {
		this.afdeTipoMediaConfId = afdeTipoMediaConfId;
	}

	/**
	 * @return the afdeTipoMediaConfCode
	 */
	public String getAfdeTipoMediaConfCode() {
		return this.afdeTipoMediaConfCode;
	}

	/**
	 * @param afdeTipoMediaConfCode the afdeTipoMediaConfCode to set
	 */
	public void setAfdeTipoMediaConfCode(String afdeTipoMediaConfCode) {
		this.afdeTipoMediaConfCode = afdeTipoMediaConfCode;
	}

	/**
	 * @return the afdeTipoMediaConfDesc
	 */
	public String getAfdeTipoMediaConfDesc() {
		return this.afdeTipoMediaConfDesc;
	}

	/**
	 * @param afdeTipoMediaConfDesc the afdeTipoMediaConfDesc to set
	 */
	public void setAfdeTipoMediaConfDesc(String afdeTipoMediaConfDesc) {
		this.afdeTipoMediaConfDesc = afdeTipoMediaConfDesc;
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
	 * Gets the uid.
	 *
	 * @return the uid
	 */
	@Override
	public Integer getUid() {
		return afdeTipoMediaConfId;
	}

	/**
	 * Sets the uid.
	 *
	 * @param uid the new uid
	 */
	@Override
	public void setUid(Integer uid) {
		this.afdeTipoMediaConfId = uid;
	}

}