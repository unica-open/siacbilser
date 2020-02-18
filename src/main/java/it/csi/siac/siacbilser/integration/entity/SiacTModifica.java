/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.Date;
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
 * The persistent class for the siac_t_modifica database table.
 * 
 */
@Entity
@Table(name="siac_t_modifica")
@NamedQuery(name="SiacTModifica.findAll", query="SELECT s FROM SiacTModifica s")
public class SiacTModifica extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The mod id. */
	@Id
	@SequenceGenerator(name="SIAC_T_MODIFICA_MODID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_MODIFICA_MOD_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_MODIFICA_MODID_GENERATOR")
	@Column(name="mod_id")
	private Integer modId;

	/** The mod data. */
	@Column(name="mod_data")
	private Date modData;

	/** The mod desc. */
	@Column(name="mod_desc")
	private String modDesc;

	/** The mod num. */
	@Column(name="mod_num")
	private Integer modNum;

	//bi-directional many-to-one association to SiacRModificaStato
	/** The siac r modifica statos. */
	@OneToMany(mappedBy="siacTModifica")
	private List<SiacRModificaStato> siacRModificaStatos;

	//bi-directional many-to-one association to SiacDModificaTipo
	/** The siac d modifica tipo. */
	@ManyToOne
	@JoinColumn(name="mod_tipo_id")
	private SiacDModificaTipo siacDModificaTipo;

	//bi-directional many-to-one association to SiacTAttoAmm
	/** The siac t atto amm. */
	@ManyToOne
	@JoinColumn(name="attoamm_id")
	private SiacTAttoAmm siacTAttoAmm;

	/**
	 * Instantiates a new siac t modifica.
	 */
	public SiacTModifica() {
	}

	/**
	 * Gets the mod id.
	 *
	 * @return the mod id
	 */
	public Integer getModId() {
		return this.modId;
	}

	/**
	 * Sets the mod id.
	 *
	 * @param modId the new mod id
	 */
	public void setModId(Integer modId) {
		this.modId = modId;
	}

	/**
	 * Gets the mod data.
	 *
	 * @return the mod data
	 */
	public Date getModData() {
		return this.modData;
	}

	/**
	 * Sets the mod data.
	 *
	 * @param modData the new mod data
	 */
	public void setModData(Date modData) {
		this.modData = modData;
	}

	/**
	 * Gets the mod desc.
	 *
	 * @return the mod desc
	 */
	public String getModDesc() {
		return this.modDesc;
	}

	/**
	 * Sets the mod desc.
	 *
	 * @param modDesc the new mod desc
	 */
	public void setModDesc(String modDesc) {
		this.modDesc = modDesc;
	}

	/**
	 * Gets the mod num.
	 *
	 * @return the mod num
	 */
	public Integer getModNum() {
		return this.modNum;
	}

	/**
	 * Sets the mod num.
	 *
	 * @param modNum the new mod num
	 */
	public void setModNum(Integer modNum) {
		this.modNum = modNum;
	}

	/**
	 * Gets the siac r modifica statos.
	 *
	 * @return the siac r modifica statos
	 */
	public List<SiacRModificaStato> getSiacRModificaStatos() {
		return this.siacRModificaStatos;
	}

	/**
	 * Sets the siac r modifica statos.
	 *
	 * @param siacRModificaStatos the new siac r modifica statos
	 */
	public void setSiacRModificaStatos(List<SiacRModificaStato> siacRModificaStatos) {
		this.siacRModificaStatos = siacRModificaStatos;
	}

	/**
	 * Adds the siac r modifica stato.
	 *
	 * @param siacRModificaStato the siac r modifica stato
	 * @return the siac r modifica stato
	 */
	public SiacRModificaStato addSiacRModificaStato(SiacRModificaStato siacRModificaStato) {
		getSiacRModificaStatos().add(siacRModificaStato);
		siacRModificaStato.setSiacTModifica(this);

		return siacRModificaStato;
	}

	/**
	 * Removes the siac r modifica stato.
	 *
	 * @param siacRModificaStato the siac r modifica stato
	 * @return the siac r modifica stato
	 */
	public SiacRModificaStato removeSiacRModificaStato(SiacRModificaStato siacRModificaStato) {
		getSiacRModificaStatos().remove(siacRModificaStato);
		siacRModificaStato.setSiacTModifica(null);

		return siacRModificaStato;
	}

	/**
	 * Gets the siac d modifica tipo.
	 *
	 * @return the siac d modifica tipo
	 */
	public SiacDModificaTipo getSiacDModificaTipo() {
		return this.siacDModificaTipo;
	}

	/**
	 * Sets the siac d modifica tipo.
	 *
	 * @param siacDModificaTipo the new siac d modifica tipo
	 */
	public void setSiacDModificaTipo(SiacDModificaTipo siacDModificaTipo) {
		this.siacDModificaTipo = siacDModificaTipo;
	}

	/**
	 * Gets the siac t atto amm.
	 *
	 * @return the siac t atto amm
	 */
	public SiacTAttoAmm getSiacTAttoAmm() {
		return this.siacTAttoAmm;
	}

	/**
	 * Sets the siac t atto amm.
	 *
	 * @param siacTAttoAmm the new siac t atto amm
	 */
	public void setSiacTAttoAmm(SiacTAttoAmm siacTAttoAmm) {
		this.siacTAttoAmm = siacTAttoAmm;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return modId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.modId = uid;
	}

}