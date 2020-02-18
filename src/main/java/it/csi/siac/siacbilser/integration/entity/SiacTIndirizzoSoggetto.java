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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_indirizzo_soggetto database table.
 * 
 */
@Entity
@Table(name="siac_t_indirizzo_soggetto")
@NamedQuery(name="SiacTIndirizzoSoggetto.findAll", query="SELECT s FROM SiacTIndirizzoSoggetto s")
public class SiacTIndirizzoSoggetto extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The indirizzo id. */
	@Id
	@SequenceGenerator(name="SIAC_T_INDIRIZZO_SOGGETTO_INDIRIZZOID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_INDIRIZZO_SOGGETTO_INDIRIZZO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_INDIRIZZO_SOGGETTO_INDIRIZZOID_GENERATOR")
	@Column(name="indirizzo_id")
	private Integer indirizzoId;

	/** The avviso. */
	private String avviso;

	/** The frazione. */
	private String frazione;

	/** The interno. */
	private String interno;

	/** The numero civico. */
	@Column(name="numero_civico")
	private String numeroCivico;

	/** The principale. */
	private String principale;

	/** The toponimo. */
	private String toponimo;

	/** The zip code. */
	@Column(name="zip_code")
	private String zipCode;

	//bi-directional many-to-one association to SiacRIndirizzoSoggettoTipo
	/** The siac r indirizzo soggetto tipos. */
	@OneToMany(mappedBy="siacTIndirizzoSoggetto")
	private List<SiacRIndirizzoSoggettoTipo> siacRIndirizzoSoggettoTipos;

	//bi-directional many-to-one association to SiacDViaTipo
	/** The siac d via tipo. */
	@ManyToOne
	@JoinColumn(name="via_tipo_id")
	private SiacDViaTipo siacDViaTipo;

	//bi-directional many-to-one association to SiacTComune
	/** The siac t comune. */
	@ManyToOne
	@JoinColumn(name="comune_id")
	private SiacTComune siacTComune;

	//bi-directional many-to-one association to SiacTSoggetto
	/** The siac t soggetto. */
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggetto siacTSoggetto;

	/**
	 * Instantiates a new siac t indirizzo soggetto.
	 */
	public SiacTIndirizzoSoggetto() {
	}

	/**
	 * Gets the indirizzo id.
	 *
	 * @return the indirizzo id
	 */
	public Integer getIndirizzoId() {
		return this.indirizzoId;
	}

	/**
	 * Sets the indirizzo id.
	 *
	 * @param indirizzoId the new indirizzo id
	 */
	public void setIndirizzoId(Integer indirizzoId) {
		this.indirizzoId = indirizzoId;
	}

	/**
	 * Gets the avviso.
	 *
	 * @return the avviso
	 */
	public String getAvviso() {
		return this.avviso;
	}

	/**
	 * Sets the avviso.
	 *
	 * @param avviso the new avviso
	 */
	public void setAvviso(String avviso) {
		this.avviso = avviso;
	}
	
	/**
	 * Gets the frazione.
	 *
	 * @return the frazione
	 */
	public String getFrazione() {
		return this.frazione;
	}

	/**
	 * Sets the frazione.
	 *
	 * @param frazione the new frazione
	 */
	public void setFrazione(String frazione) {
		this.frazione = frazione;
	}

	/**
	 * Gets the interno.
	 *
	 * @return the interno
	 */
	public String getInterno() {
		return this.interno;
	}

	/**
	 * Sets the interno.
	 *
	 * @param interno the new interno
	 */
	public void setInterno(String interno) {
		this.interno = interno;
	}

	/**
	 * Gets the numero civico.
	 *
	 * @return the numero civico
	 */
	public String getNumeroCivico() {
		return this.numeroCivico;
	}

	/**
	 * Sets the numero civico.
	 *
	 * @param numeroCivico the new numero civico
	 */
	public void setNumeroCivico(String numeroCivico) {
		this.numeroCivico = numeroCivico;
	}

	/**
	 * Gets the principale.
	 *
	 * @return the principale
	 */
	public String getPrincipale() {
		return this.principale;
	}

	/**
	 * Sets the principale.
	 *
	 * @param principale the new principale
	 */
	public void setPrincipale(String principale) {
		this.principale = principale;
	}

	/**
	 * Gets the toponimo.
	 *
	 * @return the toponimo
	 */
	public String getToponimo() {
		return this.toponimo;
	}

	/**
	 * Sets the toponimo.
	 *
	 * @param toponimo the new toponimo
	 */
	public void setToponimo(String toponimo) {
		this.toponimo = toponimo;
	}

	/**
	 * Gets the zip code.
	 *
	 * @return the zip code
	 */
	public String getZipCode() {
		return this.zipCode;
	}

	/**
	 * Sets the zip code.
	 *
	 * @param zipCode the new zip code
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * Gets the siac r indirizzo soggetto tipos.
	 *
	 * @return the siac r indirizzo soggetto tipos
	 */
	public List<SiacRIndirizzoSoggettoTipo> getSiacRIndirizzoSoggettoTipos() {
		return this.siacRIndirizzoSoggettoTipos;
	}

	/**
	 * Sets the siac r indirizzo soggetto tipos.
	 *
	 * @param siacRIndirizzoSoggettoTipos the new siac r indirizzo soggetto tipos
	 */
	public void setSiacRIndirizzoSoggettoTipos(List<SiacRIndirizzoSoggettoTipo> siacRIndirizzoSoggettoTipos) {
		this.siacRIndirizzoSoggettoTipos = siacRIndirizzoSoggettoTipos;
	}

	/**
	 * Adds the siac r indirizzo soggetto tipo.
	 *
	 * @param siacRIndirizzoSoggettoTipo the siac r indirizzo soggetto tipo
	 * @return the siac r indirizzo soggetto tipo
	 */
	public SiacRIndirizzoSoggettoTipo addSiacRIndirizzoSoggettoTipo(SiacRIndirizzoSoggettoTipo siacRIndirizzoSoggettoTipo) {
		getSiacRIndirizzoSoggettoTipos().add(siacRIndirizzoSoggettoTipo);
		siacRIndirizzoSoggettoTipo.setSiacTIndirizzoSoggetto(this);

		return siacRIndirizzoSoggettoTipo;
	}

	/**
	 * Removes the siac r indirizzo soggetto tipo.
	 *
	 * @param siacRIndirizzoSoggettoTipo the siac r indirizzo soggetto tipo
	 * @return the siac r indirizzo soggetto tipo
	 */
	public SiacRIndirizzoSoggettoTipo removeSiacRIndirizzoSoggettoTipo(SiacRIndirizzoSoggettoTipo siacRIndirizzoSoggettoTipo) {
		getSiacRIndirizzoSoggettoTipos().remove(siacRIndirizzoSoggettoTipo);
		siacRIndirizzoSoggettoTipo.setSiacTIndirizzoSoggetto(null);

		return siacRIndirizzoSoggettoTipo;
	}

	/**
	 * Gets the siac d via tipo.
	 *
	 * @return the siac d via tipo
	 */
	public SiacDViaTipo getSiacDViaTipo() {
		return this.siacDViaTipo;
	}

	/**
	 * Sets the siac d via tipo.
	 *
	 * @param siacDViaTipo the new siac d via tipo
	 */
	public void setSiacDViaTipo(SiacDViaTipo siacDViaTipo) {
		this.siacDViaTipo = siacDViaTipo;
	}

	/**
	 * Gets the siac t comune.
	 *
	 * @return the siac t comune
	 */
	public SiacTComune getSiacTComune() {
		return this.siacTComune;
	}

	/**
	 * Sets the siac t comune.
	 *
	 * @param siacTComune the new siac t comune
	 */
	public void setSiacTComune(SiacTComune siacTComune) {
		this.siacTComune = siacTComune;
	}

	/**
	 * Gets the siac t soggetto.
	 *
	 * @return the siac t soggetto
	 */
	public SiacTSoggetto getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	/**
	 * Sets the siac t soggetto.
	 *
	 * @param siacTSoggetto the new siac t soggetto
	 */
	public void setSiacTSoggetto(SiacTSoggetto siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return indirizzoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.indirizzoId = uid;
	}

}