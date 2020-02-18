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
 * The persistent class for the siac_d_relaz_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_relaz_tipo")
@NamedQuery(name="SiacDRelazTipo.findAll", query="SELECT s FROM SiacDRelazTipo s")
public class SiacDRelazTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The relaz tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_RELAZ_TIPO_RELAZTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_RELAZ_TIPO_RELAZ_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_RELAZ_TIPO_RELAZTIPOID_GENERATOR")
	@Column(name="relaz_tipo_id")
	private Integer relazTipoId;

	/** The relaz tipo code. */
	@Column(name="relaz_tipo_code")
	private String relazTipoCode;

	/** The relaz tipo desc. */
	@Column(name="relaz_tipo_desc")
	private String relazTipoDesc;
	

	//bi-directional many-to-one association to SiacDRelazEntita
	@ManyToOne
	@JoinColumn(name="relaz_entita_id")
	private SiacDRelazEntita siacDRelazEntita;

	//bi-directional many-to-one association to SiacRDoc
	/** The siac r docs. */
	@OneToMany(mappedBy="siacDRelazTipo")
	private List<SiacRDoc> siacRDocs;

	//bi-directional many-to-one association to SiacROrdinativo
	@OneToMany(mappedBy="siacDRelazTipo")
	private List<SiacROrdinativo> siacROrdinativos;

	//bi-directional many-to-one association to SiacRSoggettoRelaz
	/** The siac r soggetto relazs. */
	@OneToMany(mappedBy="siacDRelazTipo")
	private List<SiacRSoggettoRelaz> siacRSoggettoRelazs;

	//bi-directional many-to-one association to SiacRSoggettoRelazMod
	/** The siac r soggetto relaz mods. */
	@OneToMany(mappedBy="siacDRelazTipo")
	private List<SiacRSoggettoRelazMod> siacRSoggettoRelazMods;
	
	//bi-directional many-to-one association to SiacRSubdocIva
	/** The siac r subdoc ivas. */
	@OneToMany(mappedBy="siacDRelazTipo")
	private List<SiacRSubdocIva> siacRSubdocIvas;


	/**
	 * Instantiates a new siac d relaz tipo.
	 */
	public SiacDRelazTipo() {
	}

	/**
	 * Gets the relaz tipo id.
	 *
	 * @return the relaz tipo id
	 */
	public Integer getRelazTipoId() {
		return this.relazTipoId;
	}

	/**
	 * Sets the relaz tipo id.
	 *
	 * @param relazTipoId the new relaz tipo id
	 */
	public void setRelazTipoId(Integer relazTipoId) {
		this.relazTipoId = relazTipoId;
	}	

	/**
	 * Gets the relaz tipo code.
	 *
	 * @return the relaz tipo code
	 */
	public String getRelazTipoCode() {
		return this.relazTipoCode;
	}

	/**
	 * Sets the relaz tipo code.
	 *
	 * @param relazTipoCode the new relaz tipo code
	 */
	public void setRelazTipoCode(String relazTipoCode) {
		this.relazTipoCode = relazTipoCode;
	}

	/**
	 * Gets the relaz tipo desc.
	 *
	 * @return the relaz tipo desc
	 */
	public String getRelazTipoDesc() {
		return this.relazTipoDesc;
	}

	/**
	 * Sets the relaz tipo desc.
	 *
	 * @param relazTipoDesc the new relaz tipo desc
	 */
	public void setRelazTipoDesc(String relazTipoDesc) {
		this.relazTipoDesc = relazTipoDesc;
	}

	/**
	 * Gets the siac r docs.
	 *
	 * @return the siac r docs
	 */
	public SiacDRelazEntita getSiacDRelazEntita() {
		return this.siacDRelazEntita;
	}

	public void setSiacDRelazEntita(SiacDRelazEntita siacDRelazEntita) {
		this.siacDRelazEntita = siacDRelazEntita;
	}

	public List<SiacRDoc> getSiacRDocs() {
		return this.siacRDocs;
	}

	/**
	 * Sets the siac r docs.
	 *
	 * @param siacRDocs the new siac r docs
	 */
	public void setSiacRDocs(List<SiacRDoc> siacRDocs) {
		this.siacRDocs = siacRDocs;
	}

	/**
	 * Adds the siac r doc.
	 *
	 * @param siacRDoc the siac r doc
	 * @return the siac r doc
	 */
	public SiacRDoc addSiacRDoc(SiacRDoc siacRDoc) {
		getSiacRDocs().add(siacRDoc);
		siacRDoc.setSiacDRelazTipo(this);

		return siacRDoc;
	}

	/**
	 * Removes the siac r doc.
	 *
	 * @param siacRDoc the siac r doc
	 * @return the siac r doc
	 */
	public SiacRDoc removeSiacRDoc(SiacRDoc siacRDoc) {
		getSiacRDocs().remove(siacRDoc);
		siacRDoc.setSiacDRelazTipo(null);

		return siacRDoc;
	}

	public List<SiacROrdinativo> getSiacROrdinativos() {
		return this.siacROrdinativos;
	}

	public void setSiacROrdinativos(List<SiacROrdinativo> siacROrdinativos) {
		this.siacROrdinativos = siacROrdinativos;
	}

	public SiacROrdinativo addSiacROrdinativo(SiacROrdinativo siacROrdinativo) {
		getSiacROrdinativos().add(siacROrdinativo);
		siacROrdinativo.setSiacDRelazTipo(this);

		return siacROrdinativo;
	}

	public SiacROrdinativo removeSiacROrdinativo(SiacROrdinativo siacROrdinativo) {
		getSiacROrdinativos().remove(siacROrdinativo);
		siacROrdinativo.setSiacDRelazTipo(null);

		return siacROrdinativo;
	}

	/**
	 * Gets the siac r soggetto relazs.
	 *
	 * @return the siac r soggetto relazs
	 */
	public List<SiacRSoggettoRelaz> getSiacRSoggettoRelazs() {
		return this.siacRSoggettoRelazs;
	}

	/**
	 * Sets the siac r soggetto relazs.
	 *
	 * @param siacRSoggettoRelazs the new siac r soggetto relazs
	 */
	public void setSiacRSoggettoRelazs(List<SiacRSoggettoRelaz> siacRSoggettoRelazs) {
		this.siacRSoggettoRelazs = siacRSoggettoRelazs;
	}

	/**
	 * Adds the siac r soggetto relaz.
	 *
	 * @param siacRSoggettoRelaz the siac r soggetto relaz
	 * @return the siac r soggetto relaz
	 */
	public SiacRSoggettoRelaz addSiacRSoggettoRelaz(SiacRSoggettoRelaz siacRSoggettoRelaz) {
		getSiacRSoggettoRelazs().add(siacRSoggettoRelaz);
		siacRSoggettoRelaz.setSiacDRelazTipo(this);

		return siacRSoggettoRelaz;
	}

	/**
	 * Removes the siac r soggetto relaz.
	 *
	 * @param siacRSoggettoRelaz the siac r soggetto relaz
	 * @return the siac r soggetto relaz
	 */
	public SiacRSoggettoRelaz removeSiacRSoggettoRelaz(SiacRSoggettoRelaz siacRSoggettoRelaz) {
		getSiacRSoggettoRelazs().remove(siacRSoggettoRelaz);
		siacRSoggettoRelaz.setSiacDRelazTipo(null);

		return siacRSoggettoRelaz;
	}

	/**
	 * Gets the siac r soggetto relaz mods.
	 *
	 * @return the siac r soggetto relaz mods
	 */
	public List<SiacRSoggettoRelazMod> getSiacRSoggettoRelazMods() {
		return this.siacRSoggettoRelazMods;
	}

	/**
	 * Sets the siac r soggetto relaz mods.
	 *
	 * @param siacRSoggettoRelazMods the new siac r soggetto relaz mods
	 */
	public void setSiacRSoggettoRelazMods(List<SiacRSoggettoRelazMod> siacRSoggettoRelazMods) {
		this.siacRSoggettoRelazMods = siacRSoggettoRelazMods;
	}

	/**
	 * Adds the siac r soggetto relaz mod.
	 *
	 * @param siacRSoggettoRelazMod the siac r soggetto relaz mod
	 * @return the siac r soggetto relaz mod
	 */
	public SiacRSoggettoRelazMod addSiacRSoggettoRelazMod(SiacRSoggettoRelazMod siacRSoggettoRelazMod) {
		getSiacRSoggettoRelazMods().add(siacRSoggettoRelazMod);
		siacRSoggettoRelazMod.setSiacDRelazTipo(this);

		return siacRSoggettoRelazMod;
	}

	/**
	 * Removes the siac r soggetto relaz mod.
	 *
	 * @param siacRSoggettoRelazMod the siac r soggetto relaz mod
	 * @return the siac r soggetto relaz mod
	 */
	public SiacRSoggettoRelazMod removeSiacRSoggettoRelazMod(SiacRSoggettoRelazMod siacRSoggettoRelazMod) {
		getSiacRSoggettoRelazMods().remove(siacRSoggettoRelazMod);
		siacRSoggettoRelazMod.setSiacDRelazTipo(null);

		return siacRSoggettoRelazMod;
	}

	/**
	 * Gets the siac r subdoc ivas.
	 *
	 * @return the siac r subdoc ivas
	 */
	public List<SiacRSubdocIva> getSiacRSubdocIvas() {
		return this.siacRSubdocIvas;
	}

	/**
	 * Sets the siac r subdoc ivas.
	 *
	 * @param siacRSubdocIvas the new siac r subdoc ivas
	 */
	public void setSiacRSubdocIvas(List<SiacRSubdocIva> siacRSubdocIvas) {
		this.siacRSubdocIvas = siacRSubdocIvas;
	}

	/**
	 * Adds the siac r subdoc iva.
	 *
	 * @param siacRSubdocIva the siac r subdoc iva
	 * @return the siac r subdoc iva
	 */
	public SiacRSubdocIva addSiacRSubdocIva(SiacRSubdocIva siacRSubdocIva) {
		getSiacRSubdocIvas().add(siacRSubdocIva);
		siacRSubdocIva.setSiacDRelazTipo(this);

		return siacRSubdocIva;
	}

	/**
	 * Removes the siac r subdoc iva.
	 *
	 * @param siacRSubdocIva the siac r subdoc iva
	 * @return the siac r subdoc iva
	 */
	public SiacRSubdocIva removeSiacRSubdocIva(SiacRSubdocIva siacRSubdocIva) {
		getSiacRSubdocIvas().remove(siacRSubdocIva);
		siacRSubdocIva.setSiacDRelazTipo(null);

		return siacRSubdocIva;
	}

	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return relazTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.relazTipoId = uid;
	}

}