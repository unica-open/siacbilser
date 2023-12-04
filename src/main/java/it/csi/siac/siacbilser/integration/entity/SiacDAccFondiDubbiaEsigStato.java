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


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_bil_elem database table.
 * 
 */
@Entity
@Table(name="siac_d_acc_fondi_dubbia_esig_stato")
@NamedQuery(name="SiacDAccFondiDubbiaEsigStato.findAll", query="SELECT s FROM SiacDAccFondiDubbiaEsigStato s")
public class SiacDAccFondiDubbiaEsigStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The afde stato id. */
	@Id
	@SequenceGenerator(name="SIAC_D_ACC_FONDI_DUBBIA_ESIG_STATO_AFDESTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_ACC_FONDI_DUBBIA_ESIG_STATO_AFDE_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_ACC_FONDI_DUBBIA_ESIG_STATO_AFDESTATOID_GENERATOR")
	@Column(name="afde_stato_id")
	private Integer afdeStatoId;
	
	/** The afde stato code. */
	@Column(name="afde_stato_code")
	private String afdeStatoCode;
	
	/** The afde stato desc. */
	@Column(name="afde_stato_desc")
	private String afdeStatoDesc;
	
	/** The afde stato priorita. */
	@Column(name="afde_stato_priorita")
	private String afdeStatoPriorita;
	
	/** The siac T acc fondi dubbia esig bils. */
	@OneToMany(mappedBy="siacDAccFondiDubbiaEsigStato")
	private List<SiacTAccFondiDubbiaEsigBil> siacTAccFondiDubbiaEsigBils;

	/**
	 * Gets the afde stato id.
	 *
	 * @return the afdeStatoId
	 */
	public Integer getAfdeStatoId() {
		return this.afdeStatoId;
	}

	/**
	 * Sets the afde stato id.
	 *
	 * @param afdeStatoId the afdeStatoId to set
	 */
	public void setAfdeStatoId(Integer afdeStatoId) {
		this.afdeStatoId = afdeStatoId;
	}

	/**
	 * Gets the afde stato code.
	 *
	 * @return the afdeStatoCode
	 */
	public String getAfdeStatoCode() {
		return this.afdeStatoCode;
	}

	/**
	 * Sets the afde stato code.
	 *
	 * @param afdeStatoCode the afdeStatoCode to set
	 */
	public void setAfdeStatoCode(String afdeStatoCode) {
		this.afdeStatoCode = afdeStatoCode;
	}

	/**
	 * Gets the afde stato desc.
	 *
	 * @return the afdeStatoDesc
	 */
	public String getAfdeStatoDesc() {
		return this.afdeStatoDesc;
	}

	/**
	 * Sets the afde stato desc.
	 *
	 * @param afdeStatoDesc the afdeStatoDesc to set
	 */
	public void setAfdeStatoDesc(String afdeStatoDesc) {
		this.afdeStatoDesc = afdeStatoDesc;
	}

	/**
	 * Gets the afde stato priorita.
	 *
	 * @return the afdeStatoPriorita
	 */
	public String getAfdeStatoPriorita() {
		return this.afdeStatoPriorita;
	}

	/**
	 * Sets the afde stato priorita.
	 *
	 * @param afdeStatoPriorita the afdeStatoPriorita to set
	 */
	public void setAfdeStatoPriorita(String afdeStatoPriorita) {
		this.afdeStatoPriorita = afdeStatoPriorita;
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
		return afdeStatoId;
	}

	/**
	 * Sets the uid.
	 *
	 * @param uid the new uid
	 */
	@Override
	public void setUid(Integer uid) {
		this.afdeStatoId = uid;
	}

}