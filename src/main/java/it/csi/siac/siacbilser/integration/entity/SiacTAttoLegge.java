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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_atto_legge database table.
 * 
 */
@Entity
@Table(name="siac_t_atto_legge")
public class SiacTAttoLegge extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The attolegge id. */
	@Id
	@SequenceGenerator(name="SIAC_T_ATTO_LEGGE_ATTOLEGGEID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_ATTO_LEGGE_ATTOLEGGE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_ATTO_LEGGE_ATTOLEGGEID_GENERATOR")
	@Column(name="attolegge_id")
	private Integer attoleggeId;

	/** The attolegge anno. */
	@Column(name="attolegge_anno")
	private String attoleggeAnno;

	/** The attolegge articolo. */
	@Column(name="attolegge_articolo")
	private String attoleggeArticolo;

	/** The attolegge comma. */
	@Column(name="attolegge_comma")
	private String attoleggeComma;

	/** The attolegge numero. */
	@Column(name="attolegge_numero")
	private Integer attoleggeNumero;

	/** The attolegge punto. */
	@Column(name="attolegge_punto")
	private String attoleggePunto;

	//bi-directional many-to-one association to SiacRBilElemAttoLegge
	/** The siac r bil elem atto legges. */
	@OneToMany(mappedBy="siacTAttoLegge")
	private List<SiacRBilElemAttoLegge> siacRBilElemAttoLegges;

	//bi-directional many-to-one association to SiacRAttoLeggeStato
	@OneToMany(mappedBy="siacTAttoLegge")
	private List<SiacRAttoLeggeStato> siacRAttoLeggeStatos;

	//bi-directional many-to-one association to SiacDAttoLeggeTipo
	/** The siac d atto legge tipo. */
	@ManyToOne
	@JoinColumn(name="attolegge_tipo_id")
	private SiacDAttoLeggeTipo siacDAttoLeggeTipo;

	/**
	 * Instantiates a new siac t atto legge.
	 */
	public SiacTAttoLegge() {
	}

	/**
	 * Gets the attolegge id.
	 *
	 * @return the attolegge id
	 */
	public Integer getAttoleggeId() {
		return this.attoleggeId;
	}

	/**
	 * Sets the attolegge id.
	 *
	 * @param attoleggeId the new attolegge id
	 */
	public void setAttoleggeId(Integer attoleggeId) {
		this.attoleggeId = attoleggeId;
	}

	/**
	 * Gets the attolegge anno.
	 *
	 * @return the attolegge anno
	 */
	public String getAttoleggeAnno() {
		return this.attoleggeAnno;
	}

	/**
	 * Sets the attolegge anno.
	 *
	 * @param attoleggeAnno the new attolegge anno
	 */
	public void setAttoleggeAnno(String attoleggeAnno) {
		this.attoleggeAnno = attoleggeAnno;
	}

	/**
	 * Gets the attolegge articolo.
	 *
	 * @return the attolegge articolo
	 */
	public String getAttoleggeArticolo() {
		return this.attoleggeArticolo;
	}

	/**
	 * Sets the attolegge articolo.
	 *
	 * @param attoleggeArticolo the new attolegge articolo
	 */
	public void setAttoleggeArticolo(String attoleggeArticolo) {
		this.attoleggeArticolo = attoleggeArticolo;
	}

	/**
	 * Gets the attolegge comma.
	 *
	 * @return the attolegge comma
	 */
	public String getAttoleggeComma() {
		return this.attoleggeComma;
	}

	/**
	 * Sets the attolegge comma.
	 *
	 * @param attoleggeComma the new attolegge comma
	 */
	public void setAttoleggeComma(String attoleggeComma) {
		this.attoleggeComma = attoleggeComma;
	}

	/**
	 * Gets the attolegge numero.
	 *
	 * @return the attolegge numero
	 */
	public Integer getAttoleggeNumero() {
		return this.attoleggeNumero;
	}

	/**
	 * Sets the attolegge numero.
	 *
	 * @param attoleggeNumero the new attolegge numero
	 */
	public void setAttoleggeNumero(Integer attoleggeNumero) {
		this.attoleggeNumero = attoleggeNumero;
	}

	/**
	 * Gets the attolegge punto.
	 *
	 * @return the attolegge punto
	 */
	public String getAttoleggePunto() {
		return this.attoleggePunto;
	}

	/**
	 * Sets the attolegge punto.
	 *
	 * @param attoleggePunto the new attolegge punto
	 */
	public void setAttoleggePunto(String attoleggePunto) {
		this.attoleggePunto = attoleggePunto;
	}

	/**
	 * Gets the siac r bil elem atto legges.
	 *
	 * @return the siac r bil elem atto legges
	 */
	public List<SiacRBilElemAttoLegge> getSiacRBilElemAttoLegges() {
		return this.siacRBilElemAttoLegges;
	}

	/**
	 * Sets the siac r bil elem atto legges.
	 *
	 * @param siacRBilElemAttoLegges the new siac r bil elem atto legges
	 */
	public void setSiacRBilElemAttoLegges(List<SiacRBilElemAttoLegge> siacRBilElemAttoLegges) {
		this.siacRBilElemAttoLegges = siacRBilElemAttoLegges;
	}

	/**
	 * Adds the siac r bil elem atto legge.
	 *
	 * @param siacRBilElemAttoLegge the siac r bil elem atto legge
	 * @return the siac r bil elem atto legge
	 */
	public SiacRBilElemAttoLegge addSiacRBilElemAttoLegge(SiacRBilElemAttoLegge siacRBilElemAttoLegge) {
		getSiacRBilElemAttoLegges().add(siacRBilElemAttoLegge);
		siacRBilElemAttoLegge.setSiacTAttoLegge(this);

		return siacRBilElemAttoLegge;
	}
	
	/**
	 * Removes the siac r bil elem atto legge.
	 *
	 * @param siacRBilElemAttoLegge the siac r bil elem atto legge
	 * @return the siac r bil elem atto legge
	 */
	public SiacRBilElemAttoLegge removeSiacRBilElemAttoLegge(SiacRBilElemAttoLegge siacRBilElemAttoLegge) {
		getSiacRBilElemAttoLegges().remove(siacRBilElemAttoLegge);
		siacRBilElemAttoLegge.setSiacTAttoLegge(null);

		return siacRBilElemAttoLegge;
	}

	/**
	 * @return the siacRAttoLeggeStatos
	 */
	public List<SiacRAttoLeggeStato> getSiacRAttoLeggeStatos() {
		return siacRAttoLeggeStatos;
	}

	/**
	 * @param siacRAttoLeggeStatos the siacRAttoLeggeStatos to set
	 */
	public void setSiacRAttoLeggeStatos(List<SiacRAttoLeggeStato> siacRAttoLeggeStatos) {
		this.siacRAttoLeggeStatos = siacRAttoLeggeStatos;
	}

	public SiacRAttoLeggeStato addSiacRAttoLeggeStato(SiacRAttoLeggeStato siacRAttoLeggeStato) {
		getSiacRAttoLeggeStatos().add(siacRAttoLeggeStato);
		siacRAttoLeggeStato.setSiacTAttoLegge(this);

		return siacRAttoLeggeStato;
	}

	public SiacRAttoLeggeStato removeSiacRAttoLeggeStato(SiacRAttoLeggeStato siacRAttoLeggeStato) {
		getSiacRAttoLeggeStatos().remove(siacRAttoLeggeStato);
		siacRAttoLeggeStato.setSiacTAttoLegge(null);

		return siacRAttoLeggeStato;
	}

	public SiacDAttoLeggeTipo getSiacDAttoLeggeTipo() {
		return this.siacDAttoLeggeTipo;
	}

	public void setSiacDAttoLeggeTipo(SiacDAttoLeggeTipo siacDAttoLeggeTipo) {
		this.siacDAttoLeggeTipo = siacDAttoLeggeTipo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return attoleggeId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.attoleggeId = uid;
	}

}