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

/**
 * The persistent class for the siac_d_siope_scadenza_motivo database table.
 * 
 */
@Entity
@Table(name="siac_d_siope_scadenza_motivo")
@NamedQuery(name="SiacDSiopeScadenzaMotivo.findAll", query="SELECT s FROM SiacDSiopeScadenzaMotivo s")
public class SiacDSiopeScadenzaMotivo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The siope scadenza motivo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_SIOPE_SCADENZA_MOTIVO_SIOPESCADENZAMOTIVOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_SIOPE_SCADENZA_MOTIVO_SIOPE_SCADENZA_MOTIVO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_SIOPE_SCADENZA_MOTIVO_SIOPESCADENZAMOTIVOID_GENERATOR")
	@Column(name="siope_scadenza_motivo_id")
	private Integer siopeScadenzaMotivoId;

	/** The siope scadenza motivo code. */
	@Column(name="siope_scadenza_motivo_code")
	private String siopeScadenzaMotivoCode;

	/** The siope scadenza motivo desc. */
	@Column(name="siope_scadenza_motivo_desc")
	private String siopeScadenzaMotivoDesc;

	/** The siope scadenza motivo desc bnkit. */
	@Column(name="siope_scadenza_motivo_desc_bnkit")
	private String siopeScadenzaMotivoDescBnkit;
	
	//bi-directional many-to-one association to SiacTSubdoc
	/** The siac t atto amms. */
	@OneToMany(mappedBy="siacDSiopeScadenzaMotivo")
	private List<SiacTSubdoc> siacTSubdocs;

	/**
	 * Instantiates a new siac d siope scadenza motivo.
	 */
	public SiacDSiopeScadenzaMotivo() {
	}

	/**
	 * Gets the siope scadenza motivo id.
	 * @return the siopeScadenzaMotivoId
	 */
	public Integer getSiopeScadenzaMotivoId() {
		return siopeScadenzaMotivoId;
	}

	/**
	 * Sets the siope scadenza motivo id.
	 * @param siopeScadenzaMotivoId the siopeScadenzaMotivoId to set
	 */
	public void setSiopeScadenzaMotivoId(Integer siopeScadenzaMotivoId) {
		this.siopeScadenzaMotivoId = siopeScadenzaMotivoId;
	}

	/**
	 * Gets the siope scadenza motivo code.
	 * @return the siopeScadenzaMotivoCode
	 */
	public String getSiopeScadenzaMotivoCode() {
		return siopeScadenzaMotivoCode;
	}

	/**
	 * Sets the siope scadenza motivo code.
	 * @param siopeScadenzaMotivoCode the siopeScadenzaMotivoCode to set
	 */
	public void setSiopeScadenzaMotivoCode(String siopeScadenzaMotivoCode) {
		this.siopeScadenzaMotivoCode = siopeScadenzaMotivoCode;
	}

	/**
	 * Gets the siope scadenza motivo desc.
	 * @return the siopeScadenzaMotivoDesc
	 */
	public String getSiopeScadenzaMotivoDesc() {
		return siopeScadenzaMotivoDesc;
	}

	/**
	 * Sets the siope scadenza motivo desc.
	 * @param siopeScadenzaMotivoDesc the siopeScadenzaMotivoDesc to set
	 */
	public void setSiopeScadenzaMotivoDesc(String siopeScadenzaMotivoDesc) {
		this.siopeScadenzaMotivoDesc = siopeScadenzaMotivoDesc;
	}

	/**
	 * Gets the siope scadenza motivo desc bnkit.
	 * @return the siopeScadenzaMotivoDescBnkit
	 */
	public String getSiopeScadenzaMotivoDescBnkit() {
		return siopeScadenzaMotivoDescBnkit;
	}

	/**
	 * Sets the siope scadenza motivo desc bnkit.
	 * @param siopeScadenzaMotivoDescBnkit the siopeScadenzaMotivoDescBnkit to set
	 */
	public void setSiopeScadenzaMotivoDescBnkit(String siopeScadenzaMotivoDescBnkit) {
		this.siopeScadenzaMotivoDescBnkit = siopeScadenzaMotivoDescBnkit;
	}

	/**
	 * Gets the siac t subdocs.
	 * @return the siacTSubdocs
	 */
	public List<SiacTSubdoc> getSiacTSubdocs() {
		return siacTSubdocs;
	}

	/**
	 * Sets the siac t subdocs.
	 * @param siacTSubdocs the siacTSubdocs to set
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
		siacTSubdoc.setSiacDSiopeScadenzaMotivo(this);

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
		siacTSubdoc.setSiacDSiopeScadenzaMotivo(null);

		return siacTSubdoc;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return siopeScadenzaMotivoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		siopeScadenzaMotivoId = uid;
	}

}