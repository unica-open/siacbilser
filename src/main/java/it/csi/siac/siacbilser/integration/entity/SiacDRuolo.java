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
 * The persistent class for the siac_d_ruolo database table.
 * 
 */
@Entity
@Table(name="siac_d_ruolo")
public class SiacDRuolo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ruolo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_RUOLO_RUOLOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_RUOLO_RUOLO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_RUOLO_RUOLOID_GENERATOR")
	@Column(name="ruolo_id")
	private Integer ruoloId;

	/** The ruolo code. */
	@Column(name="ruolo_code")
	private String ruoloCode;

	/** The ruolo desc. */
	@Column(name="ruolo_desc")
	private String ruoloDesc;

	//bi-directional many-to-one association to SiacRDocIvaSog
	@OneToMany(mappedBy="siacDRuolo")
	private List<SiacRDocIvaSog> siacRDocIvaSogs;

	//bi-directional many-to-one association to SiacRDocSog
	@OneToMany(mappedBy="siacDRuolo")
	private List<SiacRDocSog> siacRDocSogs;

	//bi-directional many-to-one association to SiacRSoggettoRelaz
	@OneToMany(mappedBy="siacDRuolo1")
	private List<SiacRSoggettoRelaz> siacRSoggettoRelazs1;

	//bi-directional many-to-one association to SiacRSoggettoRelaz
	@OneToMany(mappedBy="siacDRuolo2")
	private List<SiacRSoggettoRelaz> siacRSoggettoRelazs2;

	//bi-directional many-to-one association to SiacRSoggettoRelazMod
	@OneToMany(mappedBy="siacDRuolo1")
	private List<SiacRSoggettoRelazMod> siacRSoggettoRelazMods1;

	//bi-directional many-to-one association to SiacRSoggettoRelazMod
	@OneToMany(mappedBy="siacDRuolo2")
	private List<SiacRSoggettoRelazMod> siacRSoggettoRelazMods2;

	//bi-directional many-to-one association to SiacRSoggettoRuolo
	/** The siac r soggetto ruolos. */
	@OneToMany(mappedBy="siacDRuolo")
	private List<SiacRSoggettoRuolo> siacRSoggettoRuolos;

	//bi-directional many-to-one association to SiacRSubdocSog
	@OneToMany(mappedBy="siacDRuolo")
	private List<SiacRSubdocSog> siacRSubdocSogs;

	/**
	 * Instantiates a new siac d ruolo.
	 */
	public SiacDRuolo() {
	}

	/**
	 * Gets the ruolo id.
	 *
	 * @return the ruolo id
	 */
	public Integer getRuoloId() {
		return this.ruoloId;
	}

	/**
	 * Sets the ruolo id.
	 *
	 * @param ruoloId the new ruolo id
	 */
	public void setRuoloId(Integer ruoloId) {
		this.ruoloId = ruoloId;
	}

	/**
	 * Gets the ruolo code.
	 *
	 * @return the ruolo code
	 */
	public String getRuoloCode() {
		return this.ruoloCode;
	}

	/**
	 * Sets the ruolo code.
	 *
	 * @param ruoloCode the new ruolo code
	 */
	public void setRuoloCode(String ruoloCode) {
		this.ruoloCode = ruoloCode;
	}

	/**
	 * Gets the ruolo desc.
	 *
	 * @return the ruolo desc
	 */
	public String getRuoloDesc() {
		return this.ruoloDesc;
	}

	/**
	 * Sets the ruolo desc.
	 *
	 * @param ruoloDesc the new ruolo desc
	 */
	public void setRuoloDesc(String ruoloDesc) {
		this.ruoloDesc = ruoloDesc;
	}

	
	/**
	 * Gets the siac r doc iva sogs.
	 *
	 * @return the siac r doc iva sogs
	 */
	public List<SiacRDocIvaSog> getSiacRDocIvaSogs() {
		return this.siacRDocIvaSogs;
	}

	/**
	 * Sets the siac r doc iva sogs.
	 *
	 * @param siacRDocIvaSogs the new siac r doc iva sogs
	 */
	public void setSiacRDocIvaSogs(List<SiacRDocIvaSog> siacRDocIvaSogs) {
		this.siacRDocIvaSogs = siacRDocIvaSogs;
	}

	/**
	 * Adds the siac r doc iva sog.
	 *
	 * @param siacRDocIvaSog the siac r doc iva sog
	 * @return the siac r doc iva sog
	 */
	public SiacRDocIvaSog addSiacRDocIvaSog(SiacRDocIvaSog siacRDocIvaSog) {
		getSiacRDocIvaSogs().add(siacRDocIvaSog);
		siacRDocIvaSog.setSiacDRuolo(this);

		return siacRDocIvaSog;
	}

	/**
	 * Removes the siac r doc iva sog.
	 *
	 * @param siacRDocIvaSog the siac r doc iva sog
	 * @return the siac r doc iva sog
	 */
	public SiacRDocIvaSog removeSiacRDocIvaSog(SiacRDocIvaSog siacRDocIvaSog) {
		getSiacRDocIvaSogs().remove(siacRDocIvaSog);
		siacRDocIvaSog.setSiacDRuolo(null);

		return siacRDocIvaSog;
	}

	public List<SiacRDocSog> getSiacRDocSogs() {
		return this.siacRDocSogs;
	}

	public void setSiacRDocSogs(List<SiacRDocSog> siacRDocSogs) {
		this.siacRDocSogs = siacRDocSogs;
	}

	public SiacRDocSog addSiacRDocSog(SiacRDocSog siacRDocSog) {
		getSiacRDocSogs().add(siacRDocSog);
		siacRDocSog.setSiacDRuolo(this);

		return siacRDocSog;
	}

	public SiacRDocSog removeSiacRDocSog(SiacRDocSog siacRDocSog) {
		getSiacRDocSogs().remove(siacRDocSog);
		siacRDocSog.setSiacDRuolo(null);

		return siacRDocSog;
	}

	public List<SiacRSoggettoRelaz> getSiacRSoggettoRelazs1() {
		return this.siacRSoggettoRelazs1;
	}

	public void setSiacRSoggettoRelazs1(List<SiacRSoggettoRelaz> siacRSoggettoRelazs1) {
		this.siacRSoggettoRelazs1 = siacRSoggettoRelazs1;
	}

	public SiacRSoggettoRelaz addSiacRSoggettoRelazs1(SiacRSoggettoRelaz siacRSoggettoRelazs1) {
		getSiacRSoggettoRelazs1().add(siacRSoggettoRelazs1);
		siacRSoggettoRelazs1.setSiacDRuolo1(this);

		return siacRSoggettoRelazs1;
	}

	public SiacRSoggettoRelaz removeSiacRSoggettoRelazs1(SiacRSoggettoRelaz siacRSoggettoRelazs1) {
		getSiacRSoggettoRelazs1().remove(siacRSoggettoRelazs1);
		siacRSoggettoRelazs1.setSiacDRuolo1(null);

		return siacRSoggettoRelazs1;
	}

	public List<SiacRSoggettoRelaz> getSiacRSoggettoRelazs2() {
		return this.siacRSoggettoRelazs2;
	}

	public void setSiacRSoggettoRelazs2(List<SiacRSoggettoRelaz> siacRSoggettoRelazs2) {
		this.siacRSoggettoRelazs2 = siacRSoggettoRelazs2;
	}

	public SiacRSoggettoRelaz addSiacRSoggettoRelazs2(SiacRSoggettoRelaz siacRSoggettoRelazs2) {
		getSiacRSoggettoRelazs2().add(siacRSoggettoRelazs2);
		siacRSoggettoRelazs2.setSiacDRuolo2(this);

		return siacRSoggettoRelazs2;
	}

	public SiacRSoggettoRelaz removeSiacRSoggettoRelazs2(SiacRSoggettoRelaz siacRSoggettoRelazs2) {
		getSiacRSoggettoRelazs2().remove(siacRSoggettoRelazs2);
		siacRSoggettoRelazs2.setSiacDRuolo2(null);

		return siacRSoggettoRelazs2;
	}

	public List<SiacRSoggettoRelazMod> getSiacRSoggettoRelazMods1() {
		return this.siacRSoggettoRelazMods1;
	}

	public void setSiacRSoggettoRelazMods1(List<SiacRSoggettoRelazMod> siacRSoggettoRelazMods1) {
		this.siacRSoggettoRelazMods1 = siacRSoggettoRelazMods1;
	}

	public SiacRSoggettoRelazMod addSiacRSoggettoRelazMods1(SiacRSoggettoRelazMod siacRSoggettoRelazMods1) {
		getSiacRSoggettoRelazMods1().add(siacRSoggettoRelazMods1);
		siacRSoggettoRelazMods1.setSiacDRuolo1(this);

		return siacRSoggettoRelazMods1;
	}

	public SiacRSoggettoRelazMod removeSiacRSoggettoRelazMods1(SiacRSoggettoRelazMod siacRSoggettoRelazMods1) {
		getSiacRSoggettoRelazMods1().remove(siacRSoggettoRelazMods1);
		siacRSoggettoRelazMods1.setSiacDRuolo1(null);

		return siacRSoggettoRelazMods1;
	}

	public List<SiacRSoggettoRelazMod> getSiacRSoggettoRelazMods2() {
		return this.siacRSoggettoRelazMods2;
	}

	public void setSiacRSoggettoRelazMods2(List<SiacRSoggettoRelazMod> siacRSoggettoRelazMods2) {
		this.siacRSoggettoRelazMods2 = siacRSoggettoRelazMods2;
	}

	public SiacRSoggettoRelazMod addSiacRSoggettoRelazMods2(SiacRSoggettoRelazMod siacRSoggettoRelazMods2) {
		getSiacRSoggettoRelazMods2().add(siacRSoggettoRelazMods2);
		siacRSoggettoRelazMods2.setSiacDRuolo2(this);

		return siacRSoggettoRelazMods2;
	}

	public SiacRSoggettoRelazMod removeSiacRSoggettoRelazMods2(SiacRSoggettoRelazMod siacRSoggettoRelazMods2) {
		getSiacRSoggettoRelazMods2().remove(siacRSoggettoRelazMods2);
		siacRSoggettoRelazMods2.setSiacDRuolo2(null);

		return siacRSoggettoRelazMods2;
	}

	public List<SiacRSoggettoRuolo> getSiacRSoggettoRuolos() {
		return this.siacRSoggettoRuolos;
	}

	public void setSiacRSoggettoRuolos(List<SiacRSoggettoRuolo> siacRSoggettoRuolos) {
		this.siacRSoggettoRuolos = siacRSoggettoRuolos;
	}

	public SiacRSoggettoRuolo addSiacRSoggettoRuolo(SiacRSoggettoRuolo siacRSoggettoRuolo) {
		getSiacRSoggettoRuolos().add(siacRSoggettoRuolo);
		siacRSoggettoRuolo.setSiacDRuolo(this);

		return siacRSoggettoRuolo;
	}

	public SiacRSoggettoRuolo removeSiacRSoggettoRuolo(SiacRSoggettoRuolo siacRSoggettoRuolo) {
		getSiacRSoggettoRuolos().remove(siacRSoggettoRuolo);
		siacRSoggettoRuolo.setSiacDRuolo(null);

		return siacRSoggettoRuolo;
	}

	public List<SiacRSubdocSog> getSiacRSubdocSogs() {
		return this.siacRSubdocSogs;
	}

	public void setSiacRSubdocSogs(List<SiacRSubdocSog> siacRSubdocSogs) {
		this.siacRSubdocSogs = siacRSubdocSogs;
	}

	public SiacRSubdocSog addSiacRSubdocSog(SiacRSubdocSog siacRSubdocSog) {
		getSiacRSubdocSogs().add(siacRSubdocSog);
		siacRSubdocSog.setSiacDRuolo(this);

		return siacRSubdocSog;
	}

	public SiacRSubdocSog removeSiacRSubdocSog(SiacRSubdocSog siacRSubdocSog) {
		getSiacRSubdocSogs().remove(siacRSubdocSog);
		siacRSubdocSog.setSiacDRuolo(null);

		return siacRSubdocSog;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return ruoloId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.ruoloId = uid;
	}

}