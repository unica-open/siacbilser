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
 * The persistent class for the siac_d_mutuo_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_mutuo_stato")
@NamedQuery(name="SiacDMutuoStato.findAll", query="SELECT s FROM SiacDMutuoStato s")
public class SiacDMutuoStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The mut stato id. */
	@Id
	@SequenceGenerator(name="SIAC_D_MUTUO_STATO_MUTSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_MUTUO_STATO_MUT_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_MUTUO_STATO_MUTSTATOID_GENERATOR")
	@Column(name="mut_stato_id")
	private Integer mutStatoId;

	

	/** The mut stato code. */
	@Column(name="mut_stato_code")
	private String mutStatoCode;

	/** The mut stato desc. */
	@Column(name="mut_stato_desc")
	private String mutStatoDesc;

	

	//bi-directional many-to-one association to SiacRMutuoStato
	/** The siac r mutuo statos. */
	@OneToMany(mappedBy="siacDMutuoStato")
	private List<SiacRMutuoStato> siacRMutuoStatos;

	/**
	 * Instantiates a new siac d mutuo stato.
	 */
	public SiacDMutuoStato() {
	}

	/**
	 * Gets the mut stato id.
	 *
	 * @return the mut stato id
	 */
	public Integer getMutStatoId() {
		return this.mutStatoId;
	}

	/**
	 * Sets the mut stato id.
	 *
	 * @param mutStatoId the new mut stato id
	 */
	public void setMutStatoId(Integer mutStatoId) {
		this.mutStatoId = mutStatoId;
	}

	

	/**
	 * Gets the mut stato code.
	 *
	 * @return the mut stato code
	 */
	public String getMutStatoCode() {
		return this.mutStatoCode;
	}

	/**
	 * Sets the mut stato code.
	 *
	 * @param mutStatoCode the new mut stato code
	 */
	public void setMutStatoCode(String mutStatoCode) {
		this.mutStatoCode = mutStatoCode;
	}

	/**
	 * Gets the mut stato desc.
	 *
	 * @return the mut stato desc
	 */
	public String getMutStatoDesc() {
		return this.mutStatoDesc;
	}

	/**
	 * Sets the mut stato desc.
	 *
	 * @param mutStatoDesc the new mut stato desc
	 */
	public void setMutStatoDesc(String mutStatoDesc) {
		this.mutStatoDesc = mutStatoDesc;
	}

	

	/**
	 * Gets the siac r mutuo statos.
	 *
	 * @return the siac r mutuo statos
	 */
	public List<SiacRMutuoStato> getSiacRMutuoStatos() {
		return this.siacRMutuoStatos;
	}

	/**
	 * Sets the siac r mutuo statos.
	 *
	 * @param siacRMutuoStatos the new siac r mutuo statos
	 */
	public void setSiacRMutuoStatos(List<SiacRMutuoStato> siacRMutuoStatos) {
		this.siacRMutuoStatos = siacRMutuoStatos;
	}

	/**
	 * Adds the siac r mutuo stato.
	 *
	 * @param siacRMutuoStato the siac r mutuo stato
	 * @return the siac r mutuo stato
	 */
	public SiacRMutuoStato addSiacRMutuoStato(SiacRMutuoStato siacRMutuoStato) {
		getSiacRMutuoStatos().add(siacRMutuoStato);
		siacRMutuoStato.setSiacDMutuoStato(this);

		return siacRMutuoStato;
	}

	/**
	 * Removes the siac r mutuo stato.
	 *
	 * @param siacRMutuoStato the siac r mutuo stato
	 * @return the siac r mutuo stato
	 */
	public SiacRMutuoStato removeSiacRMutuoStato(SiacRMutuoStato siacRMutuoStato) {
		getSiacRMutuoStatos().remove(siacRMutuoStato);
		siacRMutuoStato.setSiacDMutuoStato(null);

		return siacRMutuoStato;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return mutStatoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.mutStatoId = uid;
	}

}