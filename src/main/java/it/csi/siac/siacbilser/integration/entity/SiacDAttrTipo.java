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
 * The persistent class for the siac_d_attr_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_attr_tipo")
@NamedQuery(name="SiacDAttrTipo.findAll", query="SELECT s FROM SiacDAttrTipo s")
public class SiacDAttrTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The attr tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_ATTR_TIPO_ATTRTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_ATTR_TIPO_ATTR_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_ATTR_TIPO_ATTRTIPOID_GENERATOR")
	@Column(name="attr_tipo_id")
	private Integer attrTipoId;

	/** The attr tipo code. */
	@Column(name="attr_tipo_code")
	private String attrTipoCode;

	/** The attr tipo desc. */
	@Column(name="attr_tipo_desc")
	private String attrTipoDesc;

	//bi-directional many-to-one association to SiacTAttr
	/** The siac t attrs. */
	@OneToMany(mappedBy="siacDAttrTipo")
	private List<SiacTAttr> siacTAttrs;

	/**
	 * Instantiates a new siac d attr tipo.
	 */
	public SiacDAttrTipo() {
	}

	/**
	 * Gets the attr tipo id.
	 *
	 * @return the attr tipo id
	 */
	public Integer getAttrTipoId() {
		return this.attrTipoId;
	}

	/**
	 * Sets the attr tipo id.
	 *
	 * @param attrTipoId the new attr tipo id
	 */
	public void setAttrTipoId(Integer attrTipoId) {
		this.attrTipoId = attrTipoId;
	}

	/**
	 * Gets the attr tipo code.
	 *
	 * @return the attr tipo code
	 */
	public String getAttrTipoCode() {
		return this.attrTipoCode;
	}

	/**
	 * Sets the attr tipo code.
	 *
	 * @param attrTipoCode the new attr tipo code
	 */
	public void setAttrTipoCode(String attrTipoCode) {
		this.attrTipoCode = attrTipoCode;
	}

	/**
	 * Gets the attr tipo desc.
	 *
	 * @return the attr tipo desc
	 */
	public String getAttrTipoDesc() {
		return this.attrTipoDesc;
	}

	/**
	 * Sets the attr tipo desc.
	 *
	 * @param attrTipoDesc the new attr tipo desc
	 */
	public void setAttrTipoDesc(String attrTipoDesc) {
		this.attrTipoDesc = attrTipoDesc;
	}

	/**
	 * Gets the siac t attrs.
	 *
	 * @return the siac t attrs
	 */
	public List<SiacTAttr> getSiacTAttrs() {
		return this.siacTAttrs;
	}

	/**
	 * Sets the siac t attrs.
	 *
	 * @param siacTAttrs the new siac t attrs
	 */
	public void setSiacTAttrs(List<SiacTAttr> siacTAttrs) {
		this.siacTAttrs = siacTAttrs;
	}

	/**
	 * Adds the siac t attr.
	 *
	 * @param siacTAttr the siac t attr
	 * @return the siac t attr
	 */
	public SiacTAttr addSiacTAttr(SiacTAttr siacTAttr) {
		getSiacTAttrs().add(siacTAttr);
		siacTAttr.setSiacDAttrTipo(this);

		return siacTAttr;
	}

	/**
	 * Removes the siac t attr.
	 *
	 * @param siacTAttr the siac t attr
	 * @return the siac t attr
	 */
	public SiacTAttr removeSiacTAttr(SiacTAttr siacTAttr) {
		getSiacTAttrs().remove(siacTAttr);
		siacTAttr.setSiacDAttrTipo(null);

		return siacTAttr;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return attrTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.attrTipoId = uid;
	}

}