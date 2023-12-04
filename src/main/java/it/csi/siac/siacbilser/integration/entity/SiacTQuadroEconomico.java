/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.CascadeType;
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


/**
 * The persistent class for the siac_t_QuadroEconomico database table.
 * 
 */
@Entity
@Table(name="siac_t_quadro_economico")
@NamedQuery(name="SiacTQuadroEconomico.findAll", query="SELECT s FROM SiacTQuadroEconomico s")
public class SiacTQuadroEconomico extends SiacTEnteBaseExt {

	/** Per la serializzazione */
	private static final long serialVersionUID = -6602888341482196966L;

	@Id
	@SequenceGenerator(name="SIAC_T_QUADRO_ECONOMICO_QUADRO_ECONOMICOID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_QUADRO_ECONOMICO_QUADRO_ECONOMICO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_QUADRO_ECONOMICO_QUADRO_ECONOMICOID_GENERATOR")
	@Column(name="quadro_economico_id")
	private Integer quadroEconomicoId;

	private Integer livello;

	@Column(name="quadro_economico_code")
	private String quadroEconomicoCode;

	@Column(name="quadro_economico_desc")
	private String quadroEconomicoDesc;

	@ManyToOne
	@JoinColumn(name="quadro_economico_id_padre")
	private SiacTQuadroEconomico siacTQuadroEconomicoPadre;

	@ManyToOne
	@JoinColumn(name="parte_id")
	private SiacDQuadroEconomicoParte siacDQuadroEconomicoParte;

	/** The siac r doc statos. */
	@OneToMany(mappedBy="siacTQuadroEconomico", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRQuadroEconomicoStato> siacRQuadroEconomicoStatos;
	
	/** The siac r doc statos. */
	@OneToMany(mappedBy="siacTQuadroEconomicoPadre")
	private List<SiacTQuadroEconomico> siacTQuadroEconomicoFiglios;

	/**
	 * @return the QuadroEconomicoId
	 */
	public Integer getQuadroEconomicoId() {
		return quadroEconomicoId;
	}

	/**
	 * @param QuadroEconomicoId the QuadroEconomicoId to set
	 */
	public void setQuadroEconomicoId(Integer quadroEconomicoId) {
		this.quadroEconomicoId = quadroEconomicoId;
	}

	/**
	 * @return the livello
	 */
	public Integer getLivello() {
		return livello;
	}

	/**
	 * @param livello the livello to set
	 */
	public void setLivello(Integer livello) {
		this.livello = livello;
	}

	/**
	 * @return the siacTQuadroeconomico
	 */
	public SiacTQuadroEconomico getSiacQuadroEconomicoPadre() {
		return siacTQuadroEconomicoPadre;
	}

	/**
	 * @param siacTQuadroeconomico the siacTQuadroeconomico to set
	 */
	public void setSiacTQuadroEconomicoPadre(SiacTQuadroEconomico siacTQuadroEconomico) {
		this.siacTQuadroEconomicoPadre = siacTQuadroEconomico;
	}


	/**
	 * @param QuadroeconomicoDesc the QuadroeconomicoDesc to set
	 */
	public void setQuadroEconomicoDesc(String quadroEconomicoDesc) {
		this.quadroEconomicoDesc = quadroEconomicoDesc;
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
	 * Adds the siac r QuadroEconomico  stato.
	 *
	 * @param siacRQuadroEconomicoStato the siac r doc stato
	 * @return the siac r doc stato
	 */
	public SiacRQuadroEconomicoStato addSiacRQuadroEconomicoStato(SiacRQuadroEconomicoStato siacRQuadroEconomicoStato) {
		getSiacRQuadroEconomicoStatos().add(siacRQuadroEconomicoStato);
		siacRQuadroEconomicoStato.setSiacTQuadroEconomico(this);
		return siacRQuadroEconomicoStato;
	}

	/**
	 * Removes the iac r QuadroEconomico stato.
	 *
	 * @param siacRQuadroEconomicoStato the siac r doc stato
	 * @return the siac r doc stato
	 */
	public SiacRQuadroEconomicoStato removeSiacRQuadroEconomicoStato(SiacRQuadroEconomicoStato siacRQuadroEconomicoStato) {
		getSiacRQuadroEconomicoStatos().remove(siacRQuadroEconomicoStato);
		siacRQuadroEconomicoStato.setSiacTQuadroEconomico(null);

		return siacRQuadroEconomicoStato;
	}
	
	/**
	 * Gets the siac r doc statos.
	 *
	 * @return the siac r doc statos
	 */
	public List<SiacTQuadroEconomico> getSiacTQuadroEconomicoFiglios() {
		return this.siacTQuadroEconomicoFiglios;
	}

	/**
	 * Sets the siac r doc statos.
	 *
	 * @param siacTQuadroEconomicoFiglios the new siac r doc statos
	 */
	public void setSiacTQuadroEconomicoFiglios(List<SiacTQuadroEconomico> siacTQuadroEconomicoFiglios) {
		this.siacTQuadroEconomicoFiglios = siacTQuadroEconomicoFiglios;
	}

	/**
	 * Adds the siac r QuadroEconomico stato.
	 *
	 * @param siacTQuadroEconomicoFiglio the siac r doc stato
	 * @return the siac r doc stato
	 */
	public SiacTQuadroEconomico addSiacTQuadroEconomicoFiglio(SiacTQuadroEconomico siacTQuadroEconomicoFiglio) {
		getSiacTQuadroEconomicoFiglios().add(siacTQuadroEconomicoFiglio);
		siacTQuadroEconomicoFiglio.setSiacTQuadroEconomicoPadre(this);

		return siacTQuadroEconomicoFiglio;
	}

	/**
	 * Removes the iac r QuadroEconomico stato.
	 *
	 * @param siacTQuadroEconomicoFiglio the siac r doc stato
	 * @return the siac r doc stato
	 */
	public SiacTQuadroEconomico removeSiacTQuadroEconomicofFiglio(SiacTQuadroEconomico siacTQuadroEconomicoFiglio) {
		getSiacTQuadroEconomicoFiglios().remove(siacTQuadroEconomicoFiglio);
		siacTQuadroEconomicoFiglio.setSiacTQuadroEconomicoPadre(null);

		return siacTQuadroEconomicoFiglio;
	}
	
	/**
	 * @return the quadroEconomicoCode
	 */
	public String getQuadroEconomicoCode() {
		return quadroEconomicoCode;
	}

	/**
	 * @param quadroEconomicoCode the quadroEconomicoCode to set
	 */
	public void setQuadroEconomicoCode(String quadroEconomicoCode) {
		this.quadroEconomicoCode = quadroEconomicoCode;
	}

	/**
	 * @return the quadroEconomicoDesc
	 */
	public String getQuadroEconomicoDesc() {
		return quadroEconomicoDesc;
	}

	/**
	 * @return the siacTQuadroEconomicoPadre
	 */
	public SiacTQuadroEconomico getSiacTQuadroEconomicoPadre() {
		return siacTQuadroEconomicoPadre;
	}

	/**
	 * @return the siacDQuadroEconomicoParte
	 */
	public SiacDQuadroEconomicoParte getSiacDQuadroEconomicoParte() {
		return siacDQuadroEconomicoParte;
	}

	/**
	 * @param siacDQuadroEconomicoParte the siacDQuadroEconomicoParte to set
	 */
	public void setSiacDQuadroEconomicoParte(SiacDQuadroEconomicoParte siacDQuadroEconomicoParte) {
		this.siacDQuadroEconomicoParte = siacDQuadroEconomicoParte;
	}

	@Override
	public Integer getUid() {
		return quadroEconomicoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.quadroEconomicoId = uid;
	}

	
	
}