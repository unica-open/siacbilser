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
 * The persistent class for the siac_d_forma_giuridica_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_forma_giuridica_tipo")
@NamedQuery(name="SiacDFormaGiuridicaTipo.findAll", query="SELECT s FROM SiacDFormaGiuridicaTipo s")
public class SiacDFormaGiuridicaTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The forma giuridica tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_FORMA_GIURIDICA_TIPO_FORMAGIURIDICATIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_FORMA_GIURIDICA_TIPO_FORMA_GIURIDICA_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_FORMA_GIURIDICA_TIPO_FORMAGIURIDICATIPOID_GENERATOR")
	@Column(name="forma_giuridica_tipo_id")
	private Integer formaGiuridicaTipoId;

	

	/** The forma giuridica tipo code. */
	@Column(name="forma_giuridica_tipo_code")
	private String formaGiuridicaTipoCode;

	/** The forma giuridica tipo desc. */
	@Column(name="forma_giuridica_tipo_desc")
	private String formaGiuridicaTipoDesc;

	

	//bi-directional many-to-one association to SiacTFormaGiuridica
	/** The siac t forma giuridicas. */
	@OneToMany(mappedBy="siacDFormaGiuridicaTipo")
	private List<SiacTFormaGiuridica> siacTFormaGiuridicas;

	/**
	 * Instantiates a new siac d forma giuridica tipo.
	 */
	public SiacDFormaGiuridicaTipo() {
	}

	/**
	 * Gets the forma giuridica tipo id.
	 *
	 * @return the forma giuridica tipo id
	 */
	public Integer getFormaGiuridicaTipoId() {
		return this.formaGiuridicaTipoId;
	}

	/**
	 * Sets the forma giuridica tipo id.
	 *
	 * @param formaGiuridicaTipoId the new forma giuridica tipo id
	 */
	public void setFormaGiuridicaTipoId(Integer formaGiuridicaTipoId) {
		this.formaGiuridicaTipoId = formaGiuridicaTipoId;
	}

	

	/**
	 * Gets the forma giuridica tipo code.
	 *
	 * @return the forma giuridica tipo code
	 */
	public String getFormaGiuridicaTipoCode() {
		return this.formaGiuridicaTipoCode;
	}

	/**
	 * Sets the forma giuridica tipo code.
	 *
	 * @param formaGiuridicaTipoCode the new forma giuridica tipo code
	 */
	public void setFormaGiuridicaTipoCode(String formaGiuridicaTipoCode) {
		this.formaGiuridicaTipoCode = formaGiuridicaTipoCode;
	}

	/**
	 * Gets the forma giuridica tipo desc.
	 *
	 * @return the forma giuridica tipo desc
	 */
	public String getFormaGiuridicaTipoDesc() {
		return this.formaGiuridicaTipoDesc;
	}

	/**
	 * Sets the forma giuridica tipo desc.
	 *
	 * @param formaGiuridicaTipoDesc the new forma giuridica tipo desc
	 */
	public void setFormaGiuridicaTipoDesc(String formaGiuridicaTipoDesc) {
		this.formaGiuridicaTipoDesc = formaGiuridicaTipoDesc;
	}

	/**
	 * Gets the siac t forma giuridicas.
	 *
	 * @return the siac t forma giuridicas
	 */
	public List<SiacTFormaGiuridica> getSiacTFormaGiuridicas() {
		return this.siacTFormaGiuridicas;
	}

	/**
	 * Sets the siac t forma giuridicas.
	 *
	 * @param siacTFormaGiuridicas the new siac t forma giuridicas
	 */
	public void setSiacTFormaGiuridicas(List<SiacTFormaGiuridica> siacTFormaGiuridicas) {
		this.siacTFormaGiuridicas = siacTFormaGiuridicas;
	}

	/**
	 * Adds the siac t forma giuridica.
	 *
	 * @param siacTFormaGiuridica the siac t forma giuridica
	 * @return the siac t forma giuridica
	 */
	public SiacTFormaGiuridica addSiacTFormaGiuridica(SiacTFormaGiuridica siacTFormaGiuridica) {
		getSiacTFormaGiuridicas().add(siacTFormaGiuridica);
		siacTFormaGiuridica.setSiacDFormaGiuridicaTipo(this);

		return siacTFormaGiuridica;
	}

	/**
	 * Removes the siac t forma giuridica.
	 *
	 * @param siacTFormaGiuridica the siac t forma giuridica
	 * @return the siac t forma giuridica
	 */
	public SiacTFormaGiuridica removeSiacTFormaGiuridica(SiacTFormaGiuridica siacTFormaGiuridica) {
		getSiacTFormaGiuridicas().remove(siacTFormaGiuridica);
		siacTFormaGiuridica.setSiacDFormaGiuridicaTipo(null);

		return siacTFormaGiuridica;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return formaGiuridicaTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.formaGiuridicaTipoId = uid;
	}

}