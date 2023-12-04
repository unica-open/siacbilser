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
@Table(name="siac_d_acc_fondi_dubbia_esig_tipo_media")
@NamedQuery(name="SiacDAccFondiDubbiaEsigTipoMedia.findAll", query="SELECT s FROM SiacDAccFondiDubbiaEsigTipoMedia s")
public class SiacDAccFondiDubbiaEsigTipoMedia extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The afde tipo media id. */
	@Id
	@SequenceGenerator(name="SIAC_D_ACC_FONDI_DUBBIA_ESIG_TIPO_MEDIA_AFDETIPOMEDIAID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_ACC_FONDI_DUBBIA_ESIG_TIPO_MEDIA_AFDE_TIPO_MEDIA_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_ACC_FONDI_DUBBIA_ESIG_TIPO_MEDIA_AFDETIPOMEDIAID_GENERATOR")
	@Column(name="afde_tipo_media_id")
	private Integer afdeTipoMediaId;
	
	/** The afde tipo media code. */
	@Column(name="afde_tipo_media_code")
	private String afdeTipoMediaCode;
	
	/** The afde tipo media desc. */
	@Column(name="afde_tipo_media_desc")
	private String afdeTipoMediaDesc;
	
	/** The siac T acc fondi dubbia esigs. */
	@OneToMany(mappedBy="siacDAccFondiDubbiaEsigTipoMedia")
	private List<SiacTAccFondiDubbiaEsig> siacTAccFondiDubbiaEsigs;
	
	/**
	 * Gets the afde tipo media id.
	 *
	 * @return the afdeTipoMediaId
	 */
	public Integer getAfdeTipoMediaId() {
		return this.afdeTipoMediaId;
	}

	/**
	 * Sets the afde tipo media id.
	 *
	 * @param afdeTipoMediaId the afdeTipoMediaId to set
	 */
	public void setAfdeTipoMediaId(Integer afdeTipoMediaId) {
		this.afdeTipoMediaId = afdeTipoMediaId;
	}

	/**
	 * Gets the afde tipo media code.
	 *
	 * @return the afdeTipoMediaCode
	 */
	public String getAfdeTipoMediaCode() {
		return this.afdeTipoMediaCode;
	}

	/**
	 * Sets the afde tipo media code.
	 *
	 * @param afdeTipoMediaCode the afdeTipoMediaCode to set
	 */
	public void setAfdeTipoMediaCode(String afdeTipoMediaCode) {
		this.afdeTipoMediaCode = afdeTipoMediaCode;
	}

	/**
	 * Gets the afde tipo media desc.
	 *
	 * @return the afdeTipoMediaDesc
	 */
	public String getAfdeTipoMediaDesc() {
		return this.afdeTipoMediaDesc;
	}

	/**
	 * Sets the afde tipo media desc.
	 *
	 * @param afdeTipoMediaDesc the afdeTipoMediaDesc to set
	 */
	public void setAfdeTipoMediaDesc(String afdeTipoMediaDesc) {
		this.afdeTipoMediaDesc = afdeTipoMediaDesc;
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
		return afdeTipoMediaId;
	}

	/**
	 * Sets the uid.
	 *
	 * @param uid the new uid
	 */
	@Override
	public void setUid(Integer uid) {
		this.afdeTipoMediaId = uid;
	}

}