/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_variazione database table.
 * 
 */
@Entity
@Table(name="siac_t_numero_atto")
public class SiacTNumeroAtto extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The num id. */
	@Id
	@SequenceGenerator(name="SIAC_T_NUMERO_ATTO_NUM_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_numero_atto_num_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_NUMERO_ATTO_NUM_ID_GENERATOR")
	@Column(name="num_id")
	private Integer numId;


	/** The anno atto. */
	@Column(name="anno_atto")
	private Integer annoAtto;
	
	/** The numero atto. */
	@Version
	@Column(name="numero_atto")
	private Integer numeroAtto;

	/** The tipo numerazione. */
	@Column(name="tipo_numerazione")
	private String tipoNumerazione;

	//bi-directional many-to-one association to SiacDAttoAmmTipo
	/** The siac d atto amm tipo. */
	@ManyToOne
	@JoinColumn(name="attoamm_tipo_id")
	private SiacDAttoAmmTipo siacDAttoAmmTipo;

	/**
	 * @return the numId
	 */
	public Integer getNumId() {
		return numId;
	}

	/**
	 * @param numId the numId to set
	 */
	public void setNumId(Integer numId) {
		this.numId = numId;
	}

	/**
	 * Gets the anno atto.
	 *
	 * @return the anno atto
	 */
	public Integer getAnnoAtto() {
		return annoAtto;
	}

	/**
	 * Sets the anno atto.
	 *
	 * @param anno the new anno atto
	 */
	public void setAnnoAtto(Integer anno) {
		this.annoAtto = anno;
	}

	

	/**
	 * Gets the numero atto.
	 *
	 * @return the numero atto
	 */
	public Integer getNumeroAtto() {
		return numeroAtto;
	}

	/**
	 * Sets the numero atto.
	 *
	 * @param numeroAtto the new numero atto
	 */
	public void setNumeroAtto(Integer numeroAtto) {
		this.numeroAtto = numeroAtto;
	}

	/**
	 * @return the tipoNumerazione
	 */
	public String getTipoNumerazione() {
		return tipoNumerazione;
	}

	/**
	 * @param tipoNumerazione the tipoNumerazione to set
	 */
	public void setTipoNumerazione(String tipoNumerazione) {
		this.tipoNumerazione = tipoNumerazione;
	}

	/**
	 * @return the siacDAttoAmmTipo
	 */
	public SiacDAttoAmmTipo getSiacDAttoAmmTipo() {
		return siacDAttoAmmTipo;
	}

	/**
	 * @param siacDAttoAmmTipo the siacDAttoAmmTipo to set
	 */
	public void setSiacDAttoAmmTipo(SiacDAttoAmmTipo siacDAttoAmmTipo) {
		this.siacDAttoAmmTipo = siacDAttoAmmTipo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return numId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.numId = uid;
	}

}