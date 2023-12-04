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
 * The persistent class for the siac_t_forma_giuridica database table.
 * 
 */
@Entity
@Table(name="siac_t_forma_giuridica")
@NamedQuery(name="SiacTFormaGiuridica.findAll", query="SELECT s FROM SiacTFormaGiuridica s")
public class SiacTFormaGiuridica extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The forma giuridica id. */
	@Id
	@SequenceGenerator(name="SIAC_T_FORMA_GIURIDICA_FORMAGIURIDICAID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_FORMA_GIURIDICA_FORMA_GIURIDICA_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_FORMA_GIURIDICA_FORMAGIURIDICAID_GENERATOR")
	@Column(name="forma_giuridica_id")
	private Integer formaGiuridicaId;

	
	/** The forma giuridica desc. */
	@Column(name="forma_giuridica_desc")
	private String formaGiuridicaDesc;

	/** The forma giuridica istat codice. */
	@Column(name="forma_giuridica_istat_codice")
	private String formaGiuridicaIstatCodice;

	//bi-directional many-to-one association to SiacRFormaGiuridica
	/** The siac r forma giuridicas. */
	@OneToMany(mappedBy="siacTFormaGiuridica")
	private List<SiacRFormaGiuridica> siacRFormaGiuridicas;

	//bi-directional many-to-one association to SiacRFormaGiuridicaMod
	/** The siac r forma giuridica mods. */
	@OneToMany(mappedBy="siacTFormaGiuridica")
	private List<SiacRFormaGiuridicaMod> siacRFormaGiuridicaMods;

	//bi-directional many-to-one association to SiacDFormaGiuridicaCat
	/** The siac d forma giuridica cat. */
	@ManyToOne
	@JoinColumn(name="forma_giuridica_cat_id")
	private SiacDFormaGiuridicaCat siacDFormaGiuridicaCat;

	//bi-directional many-to-one association to SiacDFormaGiuridicaTipo
	/** The siac d forma giuridica tipo. */
	@ManyToOne
	@JoinColumn(name="forma_giuridica_tipo_id")
	private SiacDFormaGiuridicaTipo siacDFormaGiuridicaTipo;


	/**
	 * Instantiates a new siac t forma giuridica.
	 */
	public SiacTFormaGiuridica() {
	}

	/**
	 * Gets the forma giuridica id.
	 *
	 * @return the forma giuridica id
	 */
	public Integer getFormaGiuridicaId() {
		return this.formaGiuridicaId;
	}

	/**
	 * Sets the forma giuridica id.
	 *
	 * @param formaGiuridicaId the new forma giuridica id
	 */
	public void setFormaGiuridicaId(Integer formaGiuridicaId) {
		this.formaGiuridicaId = formaGiuridicaId;
	}



	/**
	 * Gets the forma giuridica desc.
	 *
	 * @return the forma giuridica desc
	 */
	public String getFormaGiuridicaDesc() {
		return this.formaGiuridicaDesc;
	}

	/**
	 * Sets the forma giuridica desc.
	 *
	 * @param formaGiuridicaDesc the new forma giuridica desc
	 */
	public void setFormaGiuridicaDesc(String formaGiuridicaDesc) {
		this.formaGiuridicaDesc = formaGiuridicaDesc;
	}

	/**
	 * Gets the forma giuridica istat codice.
	 *
	 * @return the forma giuridica istat codice
	 */
	public String getFormaGiuridicaIstatCodice() {
		return this.formaGiuridicaIstatCodice;
	}

	/**
	 * Sets the forma giuridica istat codice.
	 *
	 * @param formaGiuridicaIstatCodice the new forma giuridica istat codice
	 */
	public void setFormaGiuridicaIstatCodice(String formaGiuridicaIstatCodice) {
		this.formaGiuridicaIstatCodice = formaGiuridicaIstatCodice;
	}

	

	/**
	 * Gets the siac r forma giuridicas.
	 *
	 * @return the siac r forma giuridicas
	 */
	public List<SiacRFormaGiuridica> getSiacRFormaGiuridicas() {
		return this.siacRFormaGiuridicas;
	}

	/**
	 * Sets the siac r forma giuridicas.
	 *
	 * @param siacRFormaGiuridicas the new siac r forma giuridicas
	 */
	public void setSiacRFormaGiuridicas(List<SiacRFormaGiuridica> siacRFormaGiuridicas) {
		this.siacRFormaGiuridicas = siacRFormaGiuridicas;
	}

	/**
	 * Adds the siac r forma giuridica.
	 *
	 * @param siacRFormaGiuridica the siac r forma giuridica
	 * @return the siac r forma giuridica
	 */
	public SiacRFormaGiuridica addSiacRFormaGiuridica(SiacRFormaGiuridica siacRFormaGiuridica) {
		getSiacRFormaGiuridicas().add(siacRFormaGiuridica);
		siacRFormaGiuridica.setSiacTFormaGiuridica(this);

		return siacRFormaGiuridica;
	}

	/**
	 * Removes the siac r forma giuridica.
	 *
	 * @param siacRFormaGiuridica the siac r forma giuridica
	 * @return the siac r forma giuridica
	 */
	public SiacRFormaGiuridica removeSiacRFormaGiuridica(SiacRFormaGiuridica siacRFormaGiuridica) {
		getSiacRFormaGiuridicas().remove(siacRFormaGiuridica);
		siacRFormaGiuridica.setSiacTFormaGiuridica(null);

		return siacRFormaGiuridica;
	}

	/**
	 * Gets the siac r forma giuridica mods.
	 *
	 * @return the siac r forma giuridica mods
	 */
	public List<SiacRFormaGiuridicaMod> getSiacRFormaGiuridicaMods() {
		return this.siacRFormaGiuridicaMods;
	}

	/**
	 * Sets the siac r forma giuridica mods.
	 *
	 * @param siacRFormaGiuridicaMods the new siac r forma giuridica mods
	 */
	public void setSiacRFormaGiuridicaMods(List<SiacRFormaGiuridicaMod> siacRFormaGiuridicaMods) {
		this.siacRFormaGiuridicaMods = siacRFormaGiuridicaMods;
	}

	/**
	 * Adds the siac r forma giuridica mod.
	 *
	 * @param siacRFormaGiuridicaMod the siac r forma giuridica mod
	 * @return the siac r forma giuridica mod
	 */
	public SiacRFormaGiuridicaMod addSiacRFormaGiuridicaMod(SiacRFormaGiuridicaMod siacRFormaGiuridicaMod) {
		getSiacRFormaGiuridicaMods().add(siacRFormaGiuridicaMod);
		siacRFormaGiuridicaMod.setSiacTFormaGiuridica(this);

		return siacRFormaGiuridicaMod;
	}

	/**
	 * Removes the siac r forma giuridica mod.
	 *
	 * @param siacRFormaGiuridicaMod the siac r forma giuridica mod
	 * @return the siac r forma giuridica mod
	 */
	public SiacRFormaGiuridicaMod removeSiacRFormaGiuridicaMod(SiacRFormaGiuridicaMod siacRFormaGiuridicaMod) {
		getSiacRFormaGiuridicaMods().remove(siacRFormaGiuridicaMod);
		siacRFormaGiuridicaMod.setSiacTFormaGiuridica(null);

		return siacRFormaGiuridicaMod;
	}

	/**
	 * Gets the siac d forma giuridica cat.
	 *
	 * @return the siac d forma giuridica cat
	 */
	public SiacDFormaGiuridicaCat getSiacDFormaGiuridicaCat() {
		return this.siacDFormaGiuridicaCat;
	}

	/**
	 * Sets the siac d forma giuridica cat.
	 *
	 * @param siacDFormaGiuridicaCat the new siac d forma giuridica cat
	 */
	public void setSiacDFormaGiuridicaCat(SiacDFormaGiuridicaCat siacDFormaGiuridicaCat) {
		this.siacDFormaGiuridicaCat = siacDFormaGiuridicaCat;
	}

	/**
	 * Gets the siac d forma giuridica tipo.
	 *
	 * @return the siac d forma giuridica tipo
	 */
	public SiacDFormaGiuridicaTipo getSiacDFormaGiuridicaTipo() {
		return this.siacDFormaGiuridicaTipo;
	}

	/**
	 * Sets the siac d forma giuridica tipo.
	 *
	 * @param siacDFormaGiuridicaTipo the new siac d forma giuridica tipo
	 */
	public void setSiacDFormaGiuridicaTipo(SiacDFormaGiuridicaTipo siacDFormaGiuridicaTipo) {
		this.siacDFormaGiuridicaTipo = siacDFormaGiuridicaTipo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return formaGiuridicaId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.formaGiuridicaId = uid;
	}

}