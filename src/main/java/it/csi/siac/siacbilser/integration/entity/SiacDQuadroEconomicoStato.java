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
 * The persistent class for the siac_d_quadro_economico_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_quadro_economico_stato")
@NamedQuery(name="SiacDQuadroEconomicoStato.findAll", query="SELECT s FROM SiacDQuadroEconomicoStato s")
public class SiacDQuadroEconomicoStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The doc stato id. */
	@Id
	@SequenceGenerator(name="SIAC_D_QUADRO_ECONOMICO_STATO_QUADRO_ECONOMICO_STATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_QUADRO_ECONOMICO_STATO_QUADRO_ECONOMICO_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_QUADRO_ECONOMICO_STATO_QUADRO_ECONOMICO_STATOID_GENERATOR")
	@Column(name="quadro_economico_stato_id")
	private Integer quadroEconomicoStatoId;
	
	/** The doc stato code. */
	@Column(name="quadro_economico_stato_code")
	private String quadroEconomicoStatoCode;

	/** The doc stato desc. */
	@Column(name="quadro_economico_stato_desc")
	private String quadroEconomicoStatoDesc;
	

	//bi-directional many-to-one association to SiacRQuadroEconomicoStato
	/** The siac r doc statos. */
	@OneToMany(mappedBy="siacDQuadroEconomicoStato")
	private List<SiacRQuadroEconomicoStato> siacRQuadroEconomicoStatos;

	/**
	 * Instantiates a new siac d doc stato.
	 */
	public SiacDQuadroEconomicoStato() {
	}

	/**
	 * Gets the doc stato id.
	 *
	 * @return the doc stato id
	 */
	public Integer getQuadroEconomicoStatoId() {
		return this.quadroEconomicoStatoId;
	}

	/**
	 * Sets the doc stato id.
	 *
	 * @param quadroEconomicoStatoId the new doc stato id
	 */
	public void setQuadroEconomicoStatoId(Integer quadroEconomicoStatoId) {
		this.quadroEconomicoStatoId = quadroEconomicoStatoId;
	}


	/**
	 * Gets the doc stato code.
	 *
	 * @return the doc stato code
	 */
	public String getQuadroEconomicoStatoCode() {
		return this.quadroEconomicoStatoCode;
	}

	/**
	 * Sets the doc stato code.
	 *
	 * @param quadroEconomicoStatoCode the new doc stato code
	 */
	public void setQuadroEconomicoStatoCode(String quadroEconomicoStatoCode) {
		this.quadroEconomicoStatoCode = quadroEconomicoStatoCode;
	}

	/**
	 * Gets the doc stato desc.
	 *
	 * @return the doc stato desc
	 */
	public String getQuadroEconomicoStatoDesc() {
		return this.quadroEconomicoStatoDesc;
	}

	/**
	 * Sets the doc stato desc.
	 *
	 * @param quadroEconomicoStatoDesc the new doc stato desc
	 */
	public void setQuadroEconomicoStatoDesc(String quadroEconomicoStatoDesc) {
		this.quadroEconomicoStatoDesc = quadroEconomicoStatoDesc;
	}

	

	


	/**
	 * Gets the siac r doc statos.
	 *
	 * @return the siac r doc statos
	 */
	public List<SiacRQuadroEconomicoStato> getSiacRQuadroEconomicoStatos() {
		return this.siacRQuadroEconomicoStatos;
	}

	/**
	 * Sets the siac r doc statos.
	 *
	 * @param siacRQuadroEconomicoStatos the new siac r doc statos
	 */
	public void setSiacRQuadroEconomicoStatos(List<SiacRQuadroEconomicoStato> siacRQuadroEconomicoStatos) {
		this.siacRQuadroEconomicoStatos = siacRQuadroEconomicoStatos;
	}

	/**
	 * Adds the siac r doc stato.
	 *
	 * @param siacRQuadroEconomicoStato the siac r doc stato
	 * @return the siac r doc stato
	 */
	public SiacRQuadroEconomicoStato addSiacRQuadroEconomicoStato(SiacRQuadroEconomicoStato siacRQuadroEconomicoStato) {
		getSiacRQuadroEconomicoStatos().add(siacRQuadroEconomicoStato);
		siacRQuadroEconomicoStato.setSiacDQuadroEconomicoStato(this);

		return siacRQuadroEconomicoStato;
	}

	/**
	 * Removes the siac r doc stato.
	 *
	 * @param siacRQuadroEconomicoStato the siac r doc stato
	 * @return the siac r doc stato
	 */
	public SiacRQuadroEconomicoStato removeSiacRQuadroEconomicoStato(SiacRQuadroEconomicoStato siacRQuadroEconomicoStato) {
		getSiacRQuadroEconomicoStatos().remove(siacRQuadroEconomicoStato);
		siacRQuadroEconomicoStato.setSiacDQuadroEconomicoStato(null);

		return siacRQuadroEconomicoStato;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return quadroEconomicoStatoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.quadroEconomicoStatoId = uid;
		
	}

}