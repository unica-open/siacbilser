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
 * The persistent class for the siac_d_liquidazione_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_liquidazione_stato")
@NamedQuery(name="SiacDLiquidazioneStato.findAll", query="SELECT s FROM SiacDLiquidazioneStato s")
public class SiacDLiquidazioneStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The liq stato id. */
	@Id
	@SequenceGenerator(name="SIAC_D_LIQUIDAZIONE_STATO_LIQSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_LIQUIDAZIONE_STATO_LIQ_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_LIQUIDAZIONE_STATO_LIQSTATOID_GENERATOR")
	@Column(name="liq_stato_id")
	private Integer liqStatoId;	

	/** The liq stato code. */
	@Column(name="liq_stato_code")
	private String liqStatoCode;

	/** The liq stato desc. */
	@Column(name="liq_stato_desc")
	private String liqStatoDesc;


	//bi-directional many-to-one association to SiacRLiquidazioneStato
	/** The siac r liquidazione statos. */
	@OneToMany(mappedBy="siacDLiquidazioneStato")
	private List<SiacRLiquidazioneStato> siacRLiquidazioneStatos;

	/**
	 * Instantiates a new siac d liquidazione stato.
	 */
	public SiacDLiquidazioneStato() {
	}

	/**
	 * Gets the liq stato id.
	 *
	 * @return the liq stato id
	 */
	public Integer getLiqStatoId() {
		return this.liqStatoId;
	}

	/**
	 * Sets the liq stato id.
	 *
	 * @param liqStatoId the new liq stato id
	 */
	public void setLiqStatoId(Integer liqStatoId) {
		this.liqStatoId = liqStatoId;
	}

	

	
	/**
	 * Gets the liq stato code.
	 *
	 * @return the liq stato code
	 */
	public String getLiqStatoCode() {
		return this.liqStatoCode;
	}

	/**
	 * Sets the liq stato code.
	 *
	 * @param liqStatoCode the new liq stato code
	 */
	public void setLiqStatoCode(String liqStatoCode) {
		this.liqStatoCode = liqStatoCode;
	}

	/**
	 * Gets the liq stato desc.
	 *
	 * @return the liq stato desc
	 */
	public String getLiqStatoDesc() {
		return this.liqStatoDesc;
	}

	/**
	 * Sets the liq stato desc.
	 *
	 * @param liqStatoDesc the new liq stato desc
	 */
	public void setLiqStatoDesc(String liqStatoDesc) {
		this.liqStatoDesc = liqStatoDesc;
	}

	/**
	 * Gets the siac r liquidazione statos.
	 *
	 * @return the siac r liquidazione statos
	 */
	public List<SiacRLiquidazioneStato> getSiacRLiquidazioneStatos() {
		return this.siacRLiquidazioneStatos;
	}

	/**
	 * Sets the siac r liquidazione statos.
	 *
	 * @param siacRLiquidazioneStatos the new siac r liquidazione statos
	 */
	public void setSiacRLiquidazioneStatos(List<SiacRLiquidazioneStato> siacRLiquidazioneStatos) {
		this.siacRLiquidazioneStatos = siacRLiquidazioneStatos;
	}

	/**
	 * Adds the siac r liquidazione stato.
	 *
	 * @param siacRLiquidazioneStato the siac r liquidazione stato
	 * @return the siac r liquidazione stato
	 */
	public SiacRLiquidazioneStato addSiacRLiquidazioneStato(SiacRLiquidazioneStato siacRLiquidazioneStato) {
		getSiacRLiquidazioneStatos().add(siacRLiquidazioneStato);
		siacRLiquidazioneStato.setSiacDLiquidazioneStato(this);

		return siacRLiquidazioneStato;
	}

	/**
	 * Removes the siac r liquidazione stato.
	 *
	 * @param siacRLiquidazioneStato the siac r liquidazione stato
	 * @return the siac r liquidazione stato
	 */
	public SiacRLiquidazioneStato removeSiacRLiquidazioneStato(SiacRLiquidazioneStato siacRLiquidazioneStato) {
		getSiacRLiquidazioneStatos().remove(siacRLiquidazioneStato);
		siacRLiquidazioneStato.setSiacDLiquidazioneStato(null);

		return siacRLiquidazioneStato;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {		
		return liqStatoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.liqStatoId = uid;
	}

}