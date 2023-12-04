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
 * The persistent class for the siac_d_iva_esigibilita_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_iva_esigibilita_tipo")
@NamedQuery(name="SiacDIvaEsigibilitaTipo.findAll", query="SELECT s FROM SiacDIvaEsigibilitaTipo s")
public class SiacDIvaEsigibilitaTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ivaes tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_IVA_ESIGIBILITA_TIPO_IVAESTIPOID_GENERATOR", sequenceName="SIAC_D_IVA_ESIGIBILITA_TIPO_IVAES_TIPO_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_IVA_ESIGIBILITA_TIPO_IVAESTIPOID_GENERATOR")
	@Column(name="ivaes_tipo_id")
	private Integer ivaesTipoId;

	/** The ivaes tipo code. */
	@Column(name="ivaes_tipo_code")
	private String ivaesTipoCode;

	/** The ivaes tipo desc. */
	@Column(name="ivaes_tipo_desc")
	private String ivaesTipoDesc;

	//bi-directional many-to-one association to SiacDIvaRegistroTipo
	/** The siac d iva registro tipos. */
	@OneToMany(mappedBy="siacDIvaEsigibilitaTipo")
	private List<SiacDIvaRegistroTipo> siacDIvaRegistroTipos;

	/**
	 * Instantiates a new siac d iva esigibilita tipo.
	 */
	public SiacDIvaEsigibilitaTipo() {
	}

	/**
	 * Gets the ivaes tipo id.
	 *
	 * @return the ivaes tipo id
	 */
	public Integer getIvaesTipoId() {
		return this.ivaesTipoId;
	}

	/**
	 * Sets the ivaes tipo id.
	 *
	 * @param ivaesTipoId the new ivaes tipo id
	 */
	public void setIvaesTipoId(Integer ivaesTipoId) {
		this.ivaesTipoId = ivaesTipoId;
	}

	/**
	 * Gets the ivaes tipo code.
	 *
	 * @return the ivaes tipo code
	 */
	public String getIvaesTipoCode() {
		return this.ivaesTipoCode;
	}

	/**
	 * Sets the ivaes tipo code.
	 *
	 * @param ivaesTipoCode the new ivaes tipo code
	 */
	public void setIvaesTipoCode(String ivaesTipoCode) {
		this.ivaesTipoCode = ivaesTipoCode;
	}

	/**
	 * Gets the ivaes tipo desc.
	 *
	 * @return the ivaes tipo desc
	 */
	public String getIvaesTipoDesc() {
		return this.ivaesTipoDesc;
	}

	/**
	 * Sets the ivaes tipo desc.
	 *
	 * @param ivaesTipoDesc the new ivaes tipo desc
	 */
	public void setIvaesTipoDesc(String ivaesTipoDesc) {
		this.ivaesTipoDesc = ivaesTipoDesc;
	}

	/**
	 * Gets the siac d iva registro tipos.
	 *
	 * @return the siac d iva registro tipos
	 */
	public List<SiacDIvaRegistroTipo> getSiacDIvaRegistroTipos() {
		return this.siacDIvaRegistroTipos;
	}

	/**
	 * Sets the siac d iva registro tipos.
	 *
	 * @param siacDIvaRegistroTipos the new siac d iva registro tipos
	 */
	public void setSiacDIvaRegistroTipos(List<SiacDIvaRegistroTipo> siacDIvaRegistroTipos) {
		this.siacDIvaRegistroTipos = siacDIvaRegistroTipos;
	}

	/**
	 * Adds the siac d iva registro tipo.
	 *
	 * @param siacDIvaRegistroTipo the siac d iva registro tipo
	 * @return the siac d iva registro tipo
	 */
	public SiacDIvaRegistroTipo addSiacDIvaRegistroTipo(SiacDIvaRegistroTipo siacDIvaRegistroTipo) {
		getSiacDIvaRegistroTipos().add(siacDIvaRegistroTipo);
		siacDIvaRegistroTipo.setSiacDIvaEsigibilitaTipo(this);

		return siacDIvaRegistroTipo;
	}

	/**
	 * Removes the siac d iva registro tipo.
	 *
	 * @param siacDIvaRegistroTipo the siac d iva registro tipo
	 * @return the siac d iva registro tipo
	 */
	public SiacDIvaRegistroTipo removeSiacDIvaRegistroTipo(SiacDIvaRegistroTipo siacDIvaRegistroTipo) {
		getSiacDIvaRegistroTipos().remove(siacDIvaRegistroTipo);
		siacDIvaRegistroTipo.setSiacDIvaEsigibilitaTipo(null);

		return siacDIvaRegistroTipo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return ivaesTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.ivaesTipoId = uid;
	}
}