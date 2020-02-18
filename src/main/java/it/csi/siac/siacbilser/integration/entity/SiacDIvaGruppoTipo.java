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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

// TODO: Auto-generated Javadoc
/**
 * The Class SiacDIvaGruppoTipo.
 */
@Entity
@Table(name="siac_d_iva_gruppo_tipo")
public class SiacDIvaGruppoTipo extends SiacTEnteBase{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ivagru tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_IVA_ATTIVITA_TIPO_IVAATTTIPOID_GENERATOR", sequenceName="SIAC_D_IVA_ATTIVITA_TIPO_IVAATT_TIPO_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_IVA_ATTIVITA_TIPO_IVAATTTIPOID_GENERATOR")
	@Column(name="ivagru_tipo_id")
	private Integer ivagruTipoId;
	
	/** The ivagru tipo code. */
	@Column(name="ivagru_tipo_code")
	private String ivagruTipoCode;

	/** The ivagru tipo desc. */
	@Column(name="ivagru_tipo_desc")
	private String ivagruTipoDesc;

	//bi-directional many-to-one association to SiacTIvaGruppo
	/** The siac t iva gruppos. */
	@OneToMany(mappedBy="siacDIvaGruppoTipo")
	private List<SiacTIvaGruppo> siacTIvaGruppos;

	/**
	 * Instantiates a new siac d iva gruppo tipo.
	 */
	public SiacDIvaGruppoTipo() {
	}

	/**
	 * Gets the ivagru tipo id.
	 *
	 * @return the ivagru tipo id
	 */
	public Integer getIvagruTipoId() {
		return this.ivagruTipoId;
	}

	/**
	 * Sets the ivagru tipo id.
	 *
	 * @param ivagruTipoId the new ivagru tipo id
	 */
	public void setIvagruTipoId(Integer ivagruTipoId) {
		this.ivagruTipoId = ivagruTipoId;
	}

	/**
	 * Gets the ivagru tipo code.
	 *
	 * @return the ivagru tipo code
	 */
	public String getIvagruTipoCode() {
		return this.ivagruTipoCode;
	}

	/**
	 * Sets the ivagru tipo code.
	 *
	 * @param ivagruTipoCode the new ivagru tipo code
	 */
	public void setIvagruTipoCode(String ivagruTipoCode) {
		this.ivagruTipoCode = ivagruTipoCode;
	}

	/**
	 * Gets the ivagru tipo desc.
	 *
	 * @return the ivagru tipo desc
	 */
	public String getIvagruTipoDesc() {
		return this.ivagruTipoDesc;
	}

	/**
	 * Sets the ivagru tipo desc.
	 *
	 * @param ivagruTipoDesc the new ivagru tipo desc
	 */
	public void setIvagruTipoDesc(String ivagruTipoDesc) {
		this.ivagruTipoDesc = ivagruTipoDesc;
	}

	/**
	 * Gets the siac t iva gruppos.
	 *
	 * @return the siac t iva gruppos
	 */
	public List<SiacTIvaGruppo> getSiacTIvaGruppos() {
		return this.siacTIvaGruppos;
	}

	/**
	 * Sets the siac t iva gruppos.
	 *
	 * @param siacTIvaGruppos the new siac t iva gruppos
	 */
	public void setSiacTIvaGruppos(List<SiacTIvaGruppo> siacTIvaGruppos) {
		this.siacTIvaGruppos = siacTIvaGruppos;
	}

	/**
	 * Adds the siac t iva gruppo.
	 *
	 * @param siacTIvaGruppo the siac t iva gruppo
	 * @return the siac t iva gruppo
	 */
	public SiacTIvaGruppo addSiacTIvaGruppo(SiacTIvaGruppo siacTIvaGruppo) {
		getSiacTIvaGruppos().add(siacTIvaGruppo);
		siacTIvaGruppo.setSiacDIvaGruppoTipo(this);

		return siacTIvaGruppo;
	}

	/**
	 * Removes the siac t iva gruppo.
	 *
	 * @param siacTIvaGruppo the siac t iva gruppo
	 * @return the siac t iva gruppo
	 */
	public SiacTIvaGruppo removeSiacTIvaGruppo(SiacTIvaGruppo siacTIvaGruppo) {
		getSiacTIvaGruppos().remove(siacTIvaGruppo);
		siacTIvaGruppo.setSiacDIvaGruppoTipo(null);

		return siacTIvaGruppo;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return ivagruTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.ivagruTipoId = uid;
	}

}
