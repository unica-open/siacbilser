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
 * The persistent class for the siac_t_iva_attivita database table.
 * 
 */
@Entity
@Table(name="siac_t_iva_attivita")
@NamedQuery(name="SiacTIvaAttivita.findAll", query="SELECT s FROM SiacTIvaAttivita s")
public class SiacTIvaAttivita extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ivaatt id. */
	@Id
	@SequenceGenerator(name="SIAC_T_IVA_ATTIVITA_IVAATTID_GENERATOR", sequenceName="SIAC_T_IVA_ATTIVITA_IVAATT_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_IVA_ATTIVITA_IVAATTID_GENERATOR")
	@Column(name="ivaatt_id")
	private Integer ivaattId;

	/** The ivaatt code. */
	@Column(name="ivaatt_code")
	private String ivaattCode;

	/** The ivaatt desc. */
	@Column(name="ivaatt_desc")
	private String ivaattDesc;

	//bi-directional many-to-one association to SiacRBilElemIvaAttivita
	/** The siac r bil elem iva attivitas. */
	@OneToMany(mappedBy="siacTIvaAttivita")
	private List<SiacRBilElemIvaAttivita> siacRBilElemIvaAttivitas;

	//bi-directional many-to-one association to SiacRIvaAttAttr
	/** The siac r iva att attrs. */
	@OneToMany(mappedBy="siacTIvaAttivita")
	private List<SiacRIvaAttAttr> siacRIvaAttAttrs;

	//bi-directional many-to-one association to SiacRIvaGruppoAttivita
	/** The siac r iva gruppo attivitas. */
	@OneToMany(mappedBy="siacTIvaAttivita")
	private List<SiacRIvaGruppoAttivita> siacRIvaGruppoAttivitas;

	//bi-directional many-to-one association to SiacTSubdocIva
	@OneToMany(mappedBy="siacTIvaAttivita")
	private List<SiacTSubdocIva> siacTSubdocIvas;
	
	//bi-directional many-to-one association to SiacTIvaRegistroTotale
	@OneToMany(mappedBy="siacTIvaAttivita")
	private List<SiacTIvaRegistroTotale> siacTIvaRegistroTotales;

	/**
	 * Instantiates a new siac t iva attivita.
	 */
	public SiacTIvaAttivita() {
	}

	/**
	 * Gets the ivaatt id.
	 *
	 * @return the ivaatt id
	 */
	public Integer getIvaattId() {
		return this.ivaattId;
	}

	/**
	 * Sets the ivaatt id.
	 *
	 * @param ivaattId the new ivaatt id
	 */
	public void setIvaattId(Integer ivaattId) {
		this.ivaattId = ivaattId;
	}

	/**
	 * Gets the ivaatt code.
	 *
	 * @return the ivaatt code
	 */
	public String getIvaattCode() {
		return this.ivaattCode;
	}

	/**
	 * Sets the ivaatt code.
	 *
	 * @param ivaattCode the new ivaatt code
	 */
	public void setIvaattCode(String ivaattCode) {
		this.ivaattCode = ivaattCode;
	}

	/**
	 * Gets the ivaatt desc.
	 *
	 * @return the ivaatt desc
	 */
	public String getIvaattDesc() {
		return this.ivaattDesc;
	}

	/**
	 * Sets the ivaatt desc.
	 *
	 * @param ivaattDesc the new ivaatt desc
	 */
	public void setIvaattDesc(String ivaattDesc) {
		this.ivaattDesc = ivaattDesc;
	}

	/**
	 * Gets the siac r bil elem iva attivitas.
	 *
	 * @return the siac r bil elem iva attivitas
	 */
	public List<SiacRBilElemIvaAttivita> getSiacRBilElemIvaAttivitas() {
		return this.siacRBilElemIvaAttivitas;
	}

	/**
	 * Sets the siac r bil elem iva attivitas.
	 *
	 * @param siacRBilElemIvaAttivitas the new siac r bil elem iva attivitas
	 */
	public void setSiacRBilElemIvaAttivitas(List<SiacRBilElemIvaAttivita> siacRBilElemIvaAttivitas) {
		this.siacRBilElemIvaAttivitas = siacRBilElemIvaAttivitas;
	}

	/**
	 * Adds the siac r bil elem iva attivita.
	 *
	 * @param siacRBilElemIvaAttivita the siac r bil elem iva attivita
	 * @return the siac r bil elem iva attivita
	 */
	public SiacRBilElemIvaAttivita addSiacRBilElemIvaAttivita(SiacRBilElemIvaAttivita siacRBilElemIvaAttivita) {
		getSiacRBilElemIvaAttivitas().add(siacRBilElemIvaAttivita);
		siacRBilElemIvaAttivita.setSiacTIvaAttivita(this);

		return siacRBilElemIvaAttivita;
	}

	/**
	 * Removes the siac r bil elem iva attivita.
	 *
	 * @param siacRBilElemIvaAttivita the siac r bil elem iva attivita
	 * @return the siac r bil elem iva attivita
	 */
	public SiacRBilElemIvaAttivita removeSiacRBilElemIvaAttivita(SiacRBilElemIvaAttivita siacRBilElemIvaAttivita) {
		getSiacRBilElemIvaAttivitas().remove(siacRBilElemIvaAttivita);
		siacRBilElemIvaAttivita.setSiacTIvaAttivita(null);

		return siacRBilElemIvaAttivita;
	}

	/**
	 * Gets the siac r iva att attrs.
	 *
	 * @return the siac r iva att attrs
	 */
	public List<SiacRIvaAttAttr> getSiacRIvaAttAttrs() {
		return this.siacRIvaAttAttrs;
	}

	/**
	 * Sets the siac r iva att attrs.
	 *
	 * @param siacRIvaAttAttrs the new siac r iva att attrs
	 */
	public void setSiacRIvaAttAttrs(List<SiacRIvaAttAttr> siacRIvaAttAttrs) {
		this.siacRIvaAttAttrs = siacRIvaAttAttrs;
	}

	/**
	 * Adds the siac r iva att attr.
	 *
	 * @param siacRIvaAttAttr the siac r iva att attr
	 * @return the siac r iva att attr
	 */
	public SiacRIvaAttAttr addSiacRIvaAttAttr(SiacRIvaAttAttr siacRIvaAttAttr) {
		getSiacRIvaAttAttrs().add(siacRIvaAttAttr);
		siacRIvaAttAttr.setSiacTIvaAttivita(this);

		return siacRIvaAttAttr;
	}

	/**
	 * Removes the siac r iva att attr.
	 *
	 * @param siacRIvaAttAttr the siac r iva att attr
	 * @return the siac r iva att attr
	 */
	public SiacRIvaAttAttr removeSiacRIvaAttAttr(SiacRIvaAttAttr siacRIvaAttAttr) {
		getSiacRIvaAttAttrs().remove(siacRIvaAttAttr);
		siacRIvaAttAttr.setSiacTIvaAttivita(null);

		return siacRIvaAttAttr;
	}

	/**
	 * Gets the siac r iva gruppo attivitas.
	 *
	 * @return the siac r iva gruppo attivitas
	 */
	public List<SiacRIvaGruppoAttivita> getSiacRIvaGruppoAttivitas() {
		return this.siacRIvaGruppoAttivitas;
	}

	/**
	 * Sets the siac r iva gruppo attivitas.
	 *
	 * @param siacRIvaGruppoAttivitas the new siac r iva gruppo attivitas
	 */
	public void setSiacRIvaGruppoAttivitas(List<SiacRIvaGruppoAttivita> siacRIvaGruppoAttivitas) {
		this.siacRIvaGruppoAttivitas = siacRIvaGruppoAttivitas;
	}

	/**
	 * Gets the siac t subdoc ivas
	 *
	 * @return the siac t subdoc ivas
	 */
	public List<SiacTSubdocIva> getSiacTSubdocIvas() {
		return this.siacTSubdocIvas;
	}

	/**
	 * Sets the siac t subdoc ivas
	 *
	 * @param siacTSubdocIvas the new siac t subdoc ivas
	 */
	public void setSiacTSubdocIvas(List<SiacTSubdocIva> siacTSubdocIvas) {
		this.siacTSubdocIvas = siacTSubdocIvas;
	}

	/**
	 * Adds the siac r iva gruppo attivita.
	 *
	 * @param siacRIvaGruppoAttivita the siac r iva gruppo attivita
	 * @return the siac r iva gruppo attivita
	 */
	public SiacRIvaGruppoAttivita addSiacRIvaGruppoAttivita(SiacRIvaGruppoAttivita siacRIvaGruppoAttivita) {
		getSiacRIvaGruppoAttivitas().add(siacRIvaGruppoAttivita);
		siacRIvaGruppoAttivita.setSiacTIvaAttivita(this);

		return siacRIvaGruppoAttivita;
	}

	/**
	 * Removes the siac r iva gruppo attivita.
	 *
	 * @param siacRIvaGruppoAttivita the siac r iva gruppo attivita
	 * @return the siac r iva gruppo attivita
	 */
	public SiacRIvaGruppoAttivita removeSiacRIvaGruppoAttivita(SiacRIvaGruppoAttivita siacRIvaGruppoAttivita) {
		getSiacRIvaGruppoAttivitas().remove(siacRIvaGruppoAttivita);
		siacRIvaGruppoAttivita.setSiacTIvaAttivita(null);

		return siacRIvaGruppoAttivita;
	}
	
	public List<SiacTIvaRegistroTotale> getSiacTIvaRegistroTotales() {
		return this.siacTIvaRegistroTotales;
	}

	public void setSiacTIvaRegistroTotales(List<SiacTIvaRegistroTotale> siacTIvaRegistroTotales) {
		this.siacTIvaRegistroTotales = siacTIvaRegistroTotales;
	}

	public SiacTIvaRegistroTotale addSiacTIvaRegistroTotale(SiacTIvaRegistroTotale siacTIvaRegistroTotale) {
		getSiacTIvaRegistroTotales().add(siacTIvaRegistroTotale);
		siacTIvaRegistroTotale.setSiacTIvaAttivita(this);

		return siacTIvaRegistroTotale;
	}

	public SiacTIvaRegistroTotale removeSiacTIvaRegistroTotale(SiacTIvaRegistroTotale siacTIvaRegistroTotale) {
		getSiacTIvaRegistroTotales().remove(siacTIvaRegistroTotale);
		siacTIvaRegistroTotale.setSiacTIvaAttivita(null);

		return siacTIvaRegistroTotale;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return ivaattId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.ivaattId = uid;
	}

}