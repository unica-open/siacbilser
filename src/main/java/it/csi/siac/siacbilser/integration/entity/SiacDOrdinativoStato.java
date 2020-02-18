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
 * The persistent class for the siac_d_ordinativo_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_ordinativo_stato")
@NamedQuery(name="SiacDOrdinativoStato.findAll", query="SELECT s FROM SiacDOrdinativoStato s")
public class SiacDOrdinativoStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ordinativo stato id. */
	@Id
	@SequenceGenerator(name="SIAC_D_ORDINATIVO_STATO_ORDINATIVOSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_ORDINATIVO_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_ORDINATIVO_STATO_ORDINATIVOSTATOID_GENERATOR")
	@Column(name="ord_stato_id")
	private Integer ordinativoStatoId;

	/** The ordinativo stato code. */
	@Column(name="ord_stato_code")
	private String ordinativoStatoCode;

	/** The ordinativostato desc. */
	@Column(name="ord_stato_desc")
	private String ordinativostatoDesc;

	//bi-directional many-to-one association to SiacROrdinativoStato
	/** The siac r ordinativo statos. */
	@OneToMany(mappedBy="siacDOrdinativoStato")
	private List<SiacROrdinativoStato> siacROrdinativoStatos;

	/**
	 * Instantiates a new siac d ordinativo stato.
	 */
	public SiacDOrdinativoStato() {
	}

	/**
	 * Gets the ordinativo stato id.
	 *
	 * @return the ordinativo stato id
	 */
	public Integer getOrdinativoStatoId() {
		return this.ordinativoStatoId;
	}

	/**
	 * Sets the ordinativo stato id.
	 *
	 * @param ordinativoStatoId the new ordinativo stato id
	 */
	public void setOrdinativoStatoId(Integer ordinativoStatoId) {
		this.ordinativoStatoId = ordinativoStatoId;
	}

	/**
	 * Gets the ordinativo stato code.
	 *
	 * @return the ordinativo stato code
	 */
	public String getOrdinativoStatoCode() {
		return this.ordinativoStatoCode;
	}

	/**
	 * Sets the ordinativo stato code.
	 *
	 * @param ordinativoStatoCode the new ordinativo stato code
	 */
	public void setOrdinativoStatoCode(String ordinativoStatoCode) {
		this.ordinativoStatoCode = ordinativoStatoCode;
	}

	/**
	 * Gets the ordinativostato desc.
	 *
	 * @return the ordinativostato desc
	 */
	public String getOrdinativostatoDesc() {
		return this.ordinativostatoDesc;
	}

	/**
	 * Sets the ordinativostato desc.
	 *
	 * @param ordinativostatoDesc the new ordinativostato desc
	 */
	public void setOrdinativostatoDesc(String ordinativostatoDesc) {
		this.ordinativostatoDesc = ordinativostatoDesc;
	}

	/**
	 * Gets the siac r ordinativo statos.
	 *
	 * @return the siac r ordinativo statos
	 */
	public List<SiacROrdinativoStato> getSiacROrdinativoStatos() {
		return this.siacROrdinativoStatos;
	}

	/**
	 * Sets the siac r ordinativo statos.
	 *
	 * @param siacROrdinativoStatos the new siac r ordinativo statos
	 */
	public void setSiacROrdinativoStatos(List<SiacROrdinativoStato> siacROrdinativoStatos) {
		this.siacROrdinativoStatos = siacROrdinativoStatos;
	}

	/**
	 * Adds the siac r ordinativo stato.
	 *
	 * @param siacROrdinativoStato the siac r ordinativo stato
	 * @return the siac r ordinativo stato
	 */
	public SiacROrdinativoStato addSiacROrdinativoStato(SiacROrdinativoStato siacROrdinativoStato) {
		getSiacROrdinativoStatos().add(siacROrdinativoStato);
		siacROrdinativoStato.setSiacDOrdinativoStato(this);

		return siacROrdinativoStato;
	}

	/**
	 * Removes the siac r ordinativo stato.
	 *
	 * @param siacROrdinativoStato the siac r ordinativo stato
	 * @return the siac r ordinativo stato
	 */
	public SiacROrdinativoStato removeSiacROrdinativoStato(SiacROrdinativoStato siacROrdinativoStato) {
		getSiacROrdinativoStatos().remove(siacROrdinativoStato);
		siacROrdinativoStato.setSiacDOrdinativoStato(null);

		return siacROrdinativoStato;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return ordinativoStatoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		ordinativoStatoId = uid;
	}

}