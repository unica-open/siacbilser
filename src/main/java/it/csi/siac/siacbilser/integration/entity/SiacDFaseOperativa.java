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
 * The persistent class for the siac_d_fase_operativa database table.
 * 
 */
@Entity
@Table(name="siac_d_fase_operativa")
public class SiacDFaseOperativa extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The fase operativa id. */
	@Id
	@SequenceGenerator(name="SIAC_D_FASE_OPERATIVA_FASEOPERATIVAID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_FASE_OPERATIVA_FASE_OPERATIVA_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_FASE_OPERATIVA_FASEOPERATIVAID_GENERATOR")
	@Column(name="fase_operativa_id")
	private Integer faseOperativaId;

	/** The fase operativa code. */
	@Column(name="fase_operativa_code")
	private String faseOperativaCode;

	/** The fase operativa desc. */
	@Column(name="fase_operativa_desc")
	private String faseOperativaDesc;

	//bi-directional many-to-one association to SiacRBilFaseOperativa
	/** The siac r bil fase operativas. */
	@OneToMany(mappedBy="siacDFaseOperativa")
	private List<SiacRBilFaseOperativa> siacRBilFaseOperativas;

	//bi-directional many-to-one association to SiacRFaseOperativaBilStato
	/** The siac r fase operativa bil statos. */
	@OneToMany(mappedBy="siacDFaseOperativa")
	private List<SiacRFaseOperativaBilStato> siacRFaseOperativaBilStatos;

	/**
	 * Instantiates a new siac d fase operativa.
	 */
	public SiacDFaseOperativa() {
	}

	/**
	 * Gets the fase operativa id.
	 *
	 * @return the fase operativa id
	 */
	public Integer getFaseOperativaId() {
		return this.faseOperativaId;
	}

	/**
	 * Sets the fase operativa id.
	 *
	 * @param faseOperativaId the new fase operativa id
	 */
	public void setFaseOperativaId(Integer faseOperativaId) {
		this.faseOperativaId = faseOperativaId;
	}

	/**
	 * Gets the fase operativa code.
	 *
	 * @return the fase operativa code
	 */
	public String getFaseOperativaCode() {
		return this.faseOperativaCode;
	}

	/**
	 * Sets the fase operativa code.
	 *
	 * @param faseOperativaCode the new fase operativa code
	 */
	public void setFaseOperativaCode(String faseOperativaCode) {
		this.faseOperativaCode = faseOperativaCode;
	}

	/**
	 * Gets the fase operativa desc.
	 *
	 * @return the fase operativa desc
	 */
	public String getFaseOperativaDesc() {
		return this.faseOperativaDesc;
	}

	/**
	 * Sets the fase operativa desc.
	 *
	 * @param faseOperativaDesc the new fase operativa desc
	 */
	public void setFaseOperativaDesc(String faseOperativaDesc) {
		this.faseOperativaDesc = faseOperativaDesc;
	}

	/**
	 * Gets the siac r bil fase operativas.
	 *
	 * @return the siac r bil fase operativas
	 */
	public List<SiacRBilFaseOperativa> getSiacRBilFaseOperativas() {
		return this.siacRBilFaseOperativas;
	}

	/**
	 * Sets the siac r bil fase operativas.
	 *
	 * @param siacRBilFaseOperativas the new siac r bil fase operativas
	 */
	public void setSiacRBilFaseOperativas(List<SiacRBilFaseOperativa> siacRBilFaseOperativas) {
		this.siacRBilFaseOperativas = siacRBilFaseOperativas;
	}

	/**
	 * Adds the siac r bil fase operativa.
	 *
	 * @param siacRBilFaseOperativa the siac r bil fase operativa
	 * @return the siac r bil fase operativa
	 */
	public SiacRBilFaseOperativa addSiacRBilFaseOperativa(SiacRBilFaseOperativa siacRBilFaseOperativa) {
		getSiacRBilFaseOperativas().add(siacRBilFaseOperativa);
		siacRBilFaseOperativa.setSiacDFaseOperativa(this);

		return siacRBilFaseOperativa;
	}

	/**
	 * Removes the siac r bil fase operativa.
	 *
	 * @param siacRBilFaseOperativa the siac r bil fase operativa
	 * @return the siac r bil fase operativa
	 */
	public SiacRBilFaseOperativa removeSiacRBilFaseOperativa(SiacRBilFaseOperativa siacRBilFaseOperativa) {
		getSiacRBilFaseOperativas().remove(siacRBilFaseOperativa);
		siacRBilFaseOperativa.setSiacDFaseOperativa(null);

		return siacRBilFaseOperativa;
	}

	/**
	 * Gets the siac r fase operativa bil statos.
	 *
	 * @return the siac r fase operativa bil statos
	 */
	public List<SiacRFaseOperativaBilStato> getSiacRFaseOperativaBilStatos() {
		return this.siacRFaseOperativaBilStatos;
	}

	/**
	 * Sets the siac r fase operativa bil statos.
	 *
	 * @param siacRFaseOperativaBilStatos the new siac r fase operativa bil statos
	 */
	public void setSiacRFaseOperativaBilStatos(List<SiacRFaseOperativaBilStato> siacRFaseOperativaBilStatos) {
		this.siacRFaseOperativaBilStatos = siacRFaseOperativaBilStatos;
	}

	/**
	 * Adds the siac r fase operativa bil stato.
	 *
	 * @param siacRFaseOperativaBilStato the siac r fase operativa bil stato
	 * @return the siac r fase operativa bil stato
	 */
	public SiacRFaseOperativaBilStato addSiacRFaseOperativaBilStato(SiacRFaseOperativaBilStato siacRFaseOperativaBilStato) {
		getSiacRFaseOperativaBilStatos().add(siacRFaseOperativaBilStato);
		siacRFaseOperativaBilStato.setSiacDFaseOperativa(this);

		return siacRFaseOperativaBilStato;
	}

	/**
	 * Removes the siac r fase operativa bil stato.
	 *
	 * @param siacRFaseOperativaBilStato the siac r fase operativa bil stato
	 * @return the siac r fase operativa bil stato
	 */
	public SiacRFaseOperativaBilStato removeSiacRFaseOperativaBilStato(SiacRFaseOperativaBilStato siacRFaseOperativaBilStato) {
		getSiacRFaseOperativaBilStatos().remove(siacRFaseOperativaBilStato);
		siacRFaseOperativaBilStato.setSiacDFaseOperativa(null);

		return siacRFaseOperativaBilStato;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return faseOperativaId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.faseOperativaId = uid;		
	}

}