/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_d_accredito_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_accredito_tipo")
@NamedQuery(name="SiacDAccreditoTipo.findAll", query="SELECT s FROM SiacDAccreditoTipo s")
public class SiacDAccreditoTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The accredito tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_ACCREDITO_TIPO_ACCREDITOTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_ACCREDITO_TIPO_ACCREDITO_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_ACCREDITO_TIPO_ACCREDITOTIPOID_GENERATOR")
	@Column(name="accredito_tipo_id")
	private Integer accreditoTipoId;

	/** The accredito priorita. */
	@Column(name="accredito_priorita")
	private BigDecimal accreditoPriorita;

	/** The accredito tipo code. */
	@Column(name="accredito_tipo_code")
	private String accreditoTipoCode;

	/** The accredito tipo desc. */
	@Column(name="accredito_tipo_desc")
	private String accreditoTipoDesc;

	//bi-directional many-to-one association to SiacDAccreditoGruppo
	/** The siac d accredito gruppo. */
	@ManyToOne
	@JoinColumn(name="accredito_gruppo_id")
	private SiacDAccreditoGruppo siacDAccreditoGruppo;

	//bi-directional many-to-one association to SiacTModpag
	/** The siac t modpags. */
	@OneToMany(mappedBy="siacDAccreditoTipo")
	private List<SiacTModpag> siacTModpags;

	//bi-directional many-to-one association to SiacTModpagMod
	/** The siac t modpag mods. */
	@OneToMany(mappedBy="siacDAccreditoTipo")
	private List<SiacTModpagMod> siacTModpagMods;

	/**
	 * Instantiates a new siac d accredito tipo.
	 */
	public SiacDAccreditoTipo() {
	}

	/**
	 * Gets the accredito tipo id.
	 *
	 * @return the accredito tipo id
	 */
	public Integer getAccreditoTipoId() {
		return this.accreditoTipoId;
	}

	/**
	 * Sets the accredito tipo id.
	 *
	 * @param accreditoTipoId the new accredito tipo id
	 */
	public void setAccreditoTipoId(Integer accreditoTipoId) {
		this.accreditoTipoId = accreditoTipoId;
	}

	/**
	 * Gets the accredito priorita.
	 *
	 * @return the accredito priorita
	 */
	public BigDecimal getAccreditoPriorita() {
		return this.accreditoPriorita;
	}

	/**
	 * Sets the accredito priorita.
	 *
	 * @param accreditoPriorita the new accredito priorita
	 */
	public void setAccreditoPriorita(BigDecimal accreditoPriorita) {
		this.accreditoPriorita = accreditoPriorita;
	}

	/**
	 * Gets the accredito tipo code.
	 *
	 * @return the accredito tipo code
	 */
	public String getAccreditoTipoCode() {
		return this.accreditoTipoCode;
	}

	/**
	 * Sets the accredito tipo code.
	 *
	 * @param accreditoTipoCode the new accredito tipo code
	 */
	public void setAccreditoTipoCode(String accreditoTipoCode) {
		this.accreditoTipoCode = accreditoTipoCode;
	}

	/**
	 * Gets the accredito tipo desc.
	 *
	 * @return the accredito tipo desc
	 */
	public String getAccreditoTipoDesc() {
		return this.accreditoTipoDesc;
	}

	/**
	 * Sets the accredito tipo desc.
	 *
	 * @param accreditoTipoDesc the new accredito tipo desc
	 */
	public void setAccreditoTipoDesc(String accreditoTipoDesc) {
		this.accreditoTipoDesc = accreditoTipoDesc;
	}



	/**
	 * Gets the siac d accredito gruppo.
	 *
	 * @return the siac d accredito gruppo
	 */
	public SiacDAccreditoGruppo getSiacDAccreditoGruppo() {
		return this.siacDAccreditoGruppo;
	}

	/**
	 * Sets the siac d accredito gruppo.
	 *
	 * @param siacDAccreditoGruppo the new siac d accredito gruppo
	 */
	public void setSiacDAccreditoGruppo(SiacDAccreditoGruppo siacDAccreditoGruppo) {
		this.siacDAccreditoGruppo = siacDAccreditoGruppo;
	}

	/**
	 * Gets the siac t modpags.
	 *
	 * @return the siac t modpags
	 */
	public List<SiacTModpag> getSiacTModpags() {
		return this.siacTModpags;
	}

	/**
	 * Sets the siac t modpags.
	 *
	 * @param siacTModpags the new siac t modpags
	 */
	public void setSiacTModpags(List<SiacTModpag> siacTModpags) {
		this.siacTModpags = siacTModpags;
	}

	/**
	 * Adds the siac t modpag.
	 *
	 * @param siacTModpag the siac t modpag
	 * @return the siac t modpag
	 */
	public SiacTModpag addSiacTModpag(SiacTModpag siacTModpag) {
		getSiacTModpags().add(siacTModpag);
		siacTModpag.setSiacDAccreditoTipo(this);

		return siacTModpag;
	}

	/**
	 * Removes the siac t modpag.
	 *
	 * @param siacTModpag the siac t modpag
	 * @return the siac t modpag
	 */
	public SiacTModpag removeSiacTModpag(SiacTModpag siacTModpag) {
		getSiacTModpags().remove(siacTModpag);
		siacTModpag.setSiacDAccreditoTipo(null);

		return siacTModpag;
	}

	/**
	 * Gets the siac t modpag mods.
	 *
	 * @return the siac t modpag mods
	 */
	public List<SiacTModpagMod> getSiacTModpagMods() {
		return this.siacTModpagMods;
	}

	/**
	 * Sets the siac t modpag mods.
	 *
	 * @param siacTModpagMods the new siac t modpag mods
	 */
	public void setSiacTModpagMods(List<SiacTModpagMod> siacTModpagMods) {
		this.siacTModpagMods = siacTModpagMods;
	}

	/**
	 * Adds the siac t modpag mod.
	 *
	 * @param siacTModpagMod the siac t modpag mod
	 * @return the siac t modpag mod
	 */
	public SiacTModpagMod addSiacTModpagMod(SiacTModpagMod siacTModpagMod) {
		getSiacTModpagMods().add(siacTModpagMod);
		siacTModpagMod.setSiacDAccreditoTipo(this);

		return siacTModpagMod;
	}

	/**
	 * Removes the siac t modpag mod.
	 *
	 * @param siacTModpagMod the siac t modpag mod
	 * @return the siac t modpag mod
	 */
	public SiacTModpagMod removeSiacTModpagMod(SiacTModpagMod siacTModpagMod) {
		getSiacTModpagMods().remove(siacTModpagMod);
		siacTModpagMod.setSiacDAccreditoTipo(null);

		return siacTModpagMod;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return accreditoTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.accreditoTipoId = uid;		
	}

}