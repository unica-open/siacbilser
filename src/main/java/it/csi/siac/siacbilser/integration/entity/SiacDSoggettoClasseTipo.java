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
 * The persistent class for the siac_d_soggetto_classe_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_soggetto_classe_tipo")
@NamedQuery(name="SiacDSoggettoClasseTipo.findAll", query="SELECT s FROM SiacDSoggettoClasseTipo s")
public class SiacDSoggettoClasseTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The soggetto classe tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_SOGGETTO_CLASSE_TIPO_SOGGETTOCLASSETIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_SOGGETTO_CLASSE_TIPO_SOGGETTO_CLASSE_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_SOGGETTO_CLASSE_TIPO_SOGGETTOCLASSETIPOID_GENERATOR")
	@Column(name="soggetto_classe_tipo_id")
	private Integer soggettoClasseTipoId;

	/** The ambito id. */
	@Column(name="ambito_id")
	private Integer ambitoId;

	/** The soggetto classe tipo code. */
	@Column(name="soggetto_classe_tipo_code")
	private String soggettoClasseTipoCode;

	/** The soggetto classe tipo desc. */
	@Column(name="soggetto_classe_tipo_desc")
	private String soggettoClasseTipoDesc;	

	//bi-directional many-to-one association to SiacDSoggettoClasse
	/** The siac d soggetto classes. */
	@OneToMany(mappedBy="siacDSoggettoClasseTipo")
	private List<SiacDSoggettoClasse> siacDSoggettoClasses;

	/**
	 * Instantiates a new siac d soggetto classe tipo.
	 */
	public SiacDSoggettoClasseTipo() {
	}

	/**
	 * Gets the soggetto classe tipo id.
	 *
	 * @return the soggetto classe tipo id
	 */
	public Integer getSoggettoClasseTipoId() {
		return this.soggettoClasseTipoId;
	}

	/**
	 * Sets the soggetto classe tipo id.
	 *
	 * @param soggettoClasseTipoId the new soggetto classe tipo id
	 */
	public void setSoggettoClasseTipoId(Integer soggettoClasseTipoId) {
		this.soggettoClasseTipoId = soggettoClasseTipoId;
	}

	/**
	 * Gets the ambito id.
	 *
	 * @return the ambito id
	 */
	public Integer getAmbitoId() {
		return this.ambitoId;
	}

	/**
	 * Sets the ambito id.
	 *
	 * @param ambitoId the new ambito id
	 */
	public void setAmbitoId(Integer ambitoId) {
		this.ambitoId = ambitoId;
	}

	

	/**
	 * Gets the soggetto classe tipo code.
	 *
	 * @return the soggetto classe tipo code
	 */
	public String getSoggettoClasseTipoCode() {
		return this.soggettoClasseTipoCode;
	}

	/**
	 * Sets the soggetto classe tipo code.
	 *
	 * @param soggettoClasseTipoCode the new soggetto classe tipo code
	 */
	public void setSoggettoClasseTipoCode(String soggettoClasseTipoCode) {
		this.soggettoClasseTipoCode = soggettoClasseTipoCode;
	}

	/**
	 * Gets the soggetto classe tipo desc.
	 *
	 * @return the soggetto classe tipo desc
	 */
	public String getSoggettoClasseTipoDesc() {
		return this.soggettoClasseTipoDesc;
	}

	/**
	 * Sets the soggetto classe tipo desc.
	 *
	 * @param soggettoClasseTipoDesc the new soggetto classe tipo desc
	 */
	public void setSoggettoClasseTipoDesc(String soggettoClasseTipoDesc) {
		this.soggettoClasseTipoDesc = soggettoClasseTipoDesc;
	}

	

	/**
	 * Gets the siac d soggetto classes.
	 *
	 * @return the siac d soggetto classes
	 */
	public List<SiacDSoggettoClasse> getSiacDSoggettoClasses() {
		return this.siacDSoggettoClasses;
	}

	/**
	 * Sets the siac d soggetto classes.
	 *
	 * @param siacDSoggettoClasses the new siac d soggetto classes
	 */
	public void setSiacDSoggettoClasses(List<SiacDSoggettoClasse> siacDSoggettoClasses) {
		this.siacDSoggettoClasses = siacDSoggettoClasses;
	}

	/**
	 * Adds the siac d soggetto class.
	 *
	 * @param siacDSoggettoClass the siac d soggetto class
	 * @return the siac d soggetto classe
	 */
	public SiacDSoggettoClasse addSiacDSoggettoClass(SiacDSoggettoClasse siacDSoggettoClass) {
		getSiacDSoggettoClasses().add(siacDSoggettoClass);
		siacDSoggettoClass.setSiacDSoggettoClasseTipo(this);

		return siacDSoggettoClass;
	}

	/**
	 * Removes the siac d soggetto class.
	 *
	 * @param siacDSoggettoClass the siac d soggetto class
	 * @return the siac d soggetto classe
	 */
	public SiacDSoggettoClasse removeSiacDSoggettoClass(SiacDSoggettoClasse siacDSoggettoClass) {
		getSiacDSoggettoClasses().remove(siacDSoggettoClass);
		siacDSoggettoClass.setSiacDSoggettoClasseTipo(null);

		return siacDSoggettoClass;
	}

	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return soggettoClasseTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.soggettoClasseTipoId = uid;
	}

}