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
 * The persistent class for the siac_d_note_tesoriere database table.
 * 
 */
@Entity
@Table(name="siac_d_note_tesoriere")
@NamedQuery(name="SiacDNoteTesoriere.findAll", query="SELECT s FROM SiacDNoteTesoriere s")
public class SiacDNoteTesoriere extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The notetes id. */
	@Id
	@SequenceGenerator(name="SIAC_D_NOTE_TESORIERE_NOTETESID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_NOTE_TESORIERE_NOTETES_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_NOTE_TESORIERE_NOTETESID_GENERATOR")
	@Column(name="notetes_id")
	private Integer notetesId;

	

	/** The notetes code. */
	@Column(name="notetes_code")
	private String notetesCode;

	/** The notetes desc. */
	@Column(name="notetes_desc")
	private String notetesDesc;

	
	//bi-directional many-to-one association to SiacTOrdinativo
	@OneToMany(mappedBy="siacDNoteTesoriere")
	private List<SiacTOrdinativo> siacTOrdinativos;

	//bi-directional many-to-one association to SiacTSubdoc
	/** The siac t subdocs. */
	@OneToMany(mappedBy="siacDNoteTesoriere")
	private List<SiacTSubdoc> siacTSubdocs;

	/**
	 * Instantiates a new siac d note tesoriere.
	 */
	public SiacDNoteTesoriere() {
	}

	/**
	 * Gets the notetes id.
	 *
	 * @return the notetes id
	 */
	public Integer getNotetesId() {
		return this.notetesId;
	}

	/**
	 * Sets the notetes id.
	 *
	 * @param notetesId the new notetes id
	 */
	public void setNotetesId(Integer notetesId) {
		this.notetesId = notetesId;
	}

	

	/**
	 * Gets the notetes code.
	 *
	 * @return the notetes code
	 */
	public String getNotetesCode() {
		return this.notetesCode;
	}

	/**
	 * Sets the notetes code.
	 *
	 * @param notetesCode the new notetes code
	 */
	public void setNotetesCode(String notetesCode) {
		this.notetesCode = notetesCode;
	}

	/**
	 * Gets the notetes desc.
	 *
	 * @return the notetes desc
	 */
	public String getNotetesDesc() {
		return this.notetesDesc;
	}

	/**
	 * Sets the notetes desc.
	 *
	 * @param notetesDesc the new notetes desc
	 */
	public void setNotetesDesc(String notetesDesc) {
		this.notetesDesc = notetesDesc;
	}

	
	public List<SiacTOrdinativo> getSiacTOrdinativos() {
		return this.siacTOrdinativos;
	}

	public void setSiacTOrdinativos(List<SiacTOrdinativo> siacTOrdinativos) {
		this.siacTOrdinativos = siacTOrdinativos;
	}

	public SiacTOrdinativo addSiacTOrdinativo(SiacTOrdinativo siacTOrdinativo) {
		getSiacTOrdinativos().add(siacTOrdinativo);
		siacTOrdinativo.setSiacDNoteTesoriere(this);

		return siacTOrdinativo;
	}

	public SiacTOrdinativo removeSiacTOrdinativo(SiacTOrdinativo siacTOrdinativo) {
		getSiacTOrdinativos().remove(siacTOrdinativo);
		siacTOrdinativo.setSiacDNoteTesoriere(null);

		return siacTOrdinativo;
	}

	/**
	 * Gets the siac t subdocs.
	 *
	 * @return the siac t subdocs
	 */
	public List<SiacTSubdoc> getSiacTSubdocs() {
		return this.siacTSubdocs;
	}

	/**
	 * Sets the siac t subdocs.
	 *
	 * @param siacTSubdocs the new siac t subdocs
	 */
	public void setSiacTSubdocs(List<SiacTSubdoc> siacTSubdocs) {
		this.siacTSubdocs = siacTSubdocs;
	}

	/**
	 * Adds the siac t subdoc.
	 *
	 * @param siacTSubdoc the siac t subdoc
	 * @return the siac t subdoc
	 */
	public SiacTSubdoc addSiacTSubdoc(SiacTSubdoc siacTSubdoc) {
		getSiacTSubdocs().add(siacTSubdoc);
		siacTSubdoc.setSiacDNoteTesoriere(this);

		return siacTSubdoc;
	}

	/**
	 * Removes the siac t subdoc.
	 *
	 * @param siacTSubdoc the siac t subdoc
	 * @return the siac t subdoc
	 */
	public SiacTSubdoc removeSiacTSubdoc(SiacTSubdoc siacTSubdoc) {
		getSiacTSubdocs().remove(siacTSubdoc);
		siacTSubdoc.setSiacDNoteTesoriere(null);

		return siacTSubdoc;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return notetesId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.notetesId = uid;
		
	}

}