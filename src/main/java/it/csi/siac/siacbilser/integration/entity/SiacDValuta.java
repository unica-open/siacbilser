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
 * The persistent class for the siac_d_valuta database table.
 * 
 */
@Entity
@Table(name="siac_d_valuta")
@NamedQuery(name="SiacDValuta.findAll", query="SELECT s FROM SiacDValuta s")
public class SiacDValuta extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The valuta id. */
	@Id
	@SequenceGenerator(name="SIAC_D_VALUTA_VALUTAID_GENERATOR", sequenceName="SIAC_D_VALUTA_VALUTA_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_VALUTA_VALUTAID_GENERATOR")
	@Column(name="valuta_id")
	private Integer valutaId;

	/** The valuta code. */
	@Column(name="valuta_code")
	private String valutaCode;

	/** The valuta desc. */
	@Column(name="valuta_desc")
	private String valutaDesc;

	//bi-directional many-to-one association to SiacTCartacontEstera
	@OneToMany(mappedBy="siacDValuta")
	private List<SiacTCartacontEstera> siacTCartacontEsteras;

	//bi-directional many-to-one association to SiacTSubdocIva
	/** The siac t subdoc ivas. */
	@OneToMany(mappedBy="siacDValuta")
	private List<SiacTSubdocIva> siacTSubdocIvas;

	/**
	 * Instantiates a new siac d valuta.
	 */
	public SiacDValuta() {
	}

	/**
	 * Gets the valuta id.
	 *
	 * @return the valuta id
	 */
	public Integer getValutaId() {
		return this.valutaId;
	}

	/**
	 * Sets the valuta id.
	 *
	 * @param valutaId the new valuta id
	 */
	public void setValutaId(Integer valutaId) {
		this.valutaId = valutaId;
	}

	/**
	 * Gets the valuta code.
	 *
	 * @return the valuta code
	 */
	public String getValutaCode() {
		return this.valutaCode;
	}

	/**
	 * Sets the valuta code.
	 *
	 * @param valutaCode the new valuta code
	 */
	public void setValutaCode(String valutaCode) {
		this.valutaCode = valutaCode;
	}

	/**
	 * Gets the valuta desc.
	 *
	 * @return the valuta desc
	 */
	public String getValutaDesc() {
		return this.valutaDesc;
	}

	/**
	 * Sets the valuta desc.
	 *
	 * @param valutaDesc the new valuta desc
	 */
	public void setValutaDesc(String valutaDesc) {
		this.valutaDesc = valutaDesc;
	}

	public List<SiacTCartacontEstera> getSiacTCartacontEsteras() {
		return this.siacTCartacontEsteras;
	}

	public void setSiacTCartacontEsteras(List<SiacTCartacontEstera> siacTCartacontEsteras) {
		this.siacTCartacontEsteras = siacTCartacontEsteras;
	}

	public SiacTCartacontEstera addSiacTCartacontEstera(SiacTCartacontEstera siacTCartacontEstera) {
		getSiacTCartacontEsteras().add(siacTCartacontEstera);
		siacTCartacontEstera.setSiacDValuta(this);

		return siacTCartacontEstera;
	}

	public SiacTCartacontEstera removeSiacTCartacontEstera(SiacTCartacontEstera siacTCartacontEstera) {
		getSiacTCartacontEsteras().remove(siacTCartacontEstera);
		siacTCartacontEstera.setSiacDValuta(null);

		return siacTCartacontEstera;
	}

	/**
	 * Gets the siac t subdoc ivas.
	 *
	 * @return the siac t subdoc ivas
	 */
	public List<SiacTSubdocIva> getSiacTSubdocIvas() {
		return this.siacTSubdocIvas;
	}

	/**
	 * Sets the siac t subdoc ivas.
	 *
	 * @param siacTSubdocIvas the new siac t subdoc ivas
	 */
	public void setSiacTSubdocIvas(List<SiacTSubdocIva> siacTSubdocIvas) {
		this.siacTSubdocIvas = siacTSubdocIvas;
	}

	/**
	 * Adds the siac t subdoc iva.
	 *
	 * @param siacTSubdocIva the siac t subdoc iva
	 * @return the siac t subdoc iva
	 */
	public SiacTSubdocIva addSiacTSubdocIva(SiacTSubdocIva siacTSubdocIva) {
		getSiacTSubdocIvas().add(siacTSubdocIva);
		siacTSubdocIva.setSiacDValuta(this);

		return siacTSubdocIva;
	}

	/**
	 * Removes the siac t subdoc iva.
	 *
	 * @param siacTSubdocIva the siac t subdoc iva
	 * @return the siac t subdoc iva
	 */
	public SiacTSubdocIva removeSiacTSubdocIva(SiacTSubdocIva siacTSubdocIva) {
		getSiacTSubdocIvas().remove(siacTSubdocIva);
		siacTSubdocIva.setSiacDValuta(null);

		return siacTSubdocIva;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return valutaId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.valutaId = uid;
	}
}