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
 * The persistent class for the siac_d_siope_documento_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_siope_documento_tipo")
@NamedQuery(name="SiacDSiopeDocumentoTipo.findAll", query="SELECT s FROM SiacDSiopeDocumentoTipo s")
public class SiacDSiopeDocumentoTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The siope documento tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_SIOPE_DOCUMENTO_TIPO_SIOPEDOCUMENTOTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_SIOPE_DOCUMENTO_TIPO_SIOPE_DOCUMENTO_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_SIOPE_DOCUMENTO_TIPO_SIOPEDOCUMENTOTIPOID_GENERATOR")
	@Column(name="siope_documento_tipo_id")
	private Integer siopeDocumentoTipoId;

	/** The siope documento tipo code. */
	@Column(name="siope_documento_tipo_code")
	private String siopeDocumentoTipoCode;

	/** The siope documento tipo desc. */
	@Column(name="siope_documento_tipo_desc")
	private String siopeDocumentoTipoDesc;

	/** The siope documento tipo desc bnkit. */
	@Column(name="siope_documento_tipo_desc_bnkit")
	private String siopeDocumentoTipoDescBnkit;

	//bi-directional many-to-one association to SiacTDoc
	/** The siac t atto amms. */
	@OneToMany(mappedBy="siacDSiopeDocumentoTipo")
	private List<SiacTDoc> siacTDocs;

	//bi-directional many-to-one association to SiacTDoc
	/** The siac t atto amms. */
	@OneToMany(mappedBy="siacDSiopeDocumentoTipo")
	private List<SiacDSiopeDocumentoTipoAnalogico> siacDSiopeDocumentoTipoAnalogicos;

	/**
	 * Instantiates a new siac d siope documento tipo.
	 */
	public SiacDSiopeDocumentoTipo() {
	}

	/**
	 * Gets the siope documento tipo id.
	 * @return the siopeDocumentoTipoId
	 */
	public Integer getSiopeDocumentoTipoId() {
		return siopeDocumentoTipoId;
	}

	/**
	 * Sets the siope documento tipo id.
	 * @param siopeDocumentoTipoId the siopeDocumentoTipoId to set
	 */
	public void setSiopeDocumentoTipoId(Integer siopeDocumentoTipoId) {
		this.siopeDocumentoTipoId = siopeDocumentoTipoId;
	}

	/**
	 * Gets the siope documento tipo code.
	 * @return the siopeDocumentoTipoCode
	 */
	public String getSiopeDocumentoTipoCode() {
		return siopeDocumentoTipoCode;
	}

	/**
	 * Sets the siope documento tipo code.
	 * @param siopeDocumentoTipoCode the siopeDocumentoTipoCode to set
	 */
	public void setSiopeDocumentoTipoCode(String siopeDocumentoTipoCode) {
		this.siopeDocumentoTipoCode = siopeDocumentoTipoCode;
	}

	/**
	 * Gets the siope documento tipo desc.
	 * @return the siopeDocumentoTipoDesc
	 */
	public String getSiopeDocumentoTipoDesc() {
		return siopeDocumentoTipoDesc;
	}

	/**
	 * Sets the siope documento tipo desc.
	 * @param siopeDocumentoTipoDesc the siopeDocumentoTipoDesc to set
	 */
	public void setSiopeDocumentoTipoDesc(String siopeDocumentoTipoDesc) {
		this.siopeDocumentoTipoDesc = siopeDocumentoTipoDesc;
	}

	/**
	 * Gets the siope documento tipo desc bnkit.
	 * @return the siopeDocumentoTipoDescBnkit
	 */
	public String getSiopeDocumentoTipoDescBnkit() {
		return siopeDocumentoTipoDescBnkit;
	}

	/**
	 * Sets the siope documento tipo desc bnkit.
	 * @param siopeDocumentoTipoDescBnkit the siopeDocumentoTipoDescBnkit to set
	 */
	public void setSiopeDocumentoTipoDescBnkit(String siopeDocumentoTipoDescBnkit) {
		this.siopeDocumentoTipoDescBnkit = siopeDocumentoTipoDescBnkit;
	}

	/**
	 * Gets the siac t docs.
	 * @return the siacTDocs
	 */
	public List<SiacTDoc> getSiacTDocs() {
		return siacTDocs;
	}

	/**
	 * Sets the siac t docs.
	 * @param siacTDocs the siacTDdocs to set
	 */
	public void setSiacTDocs(List<SiacTDoc> siacTDocs) {
		this.siacTDocs = siacTDocs;
	}
	
	/**
	 * Adds the siac t doc.
	 *
	 * @param siacTDoc the siac t doc
	 * @return the siac t doc
	 */
	public SiacTDoc addSiacTDoc(SiacTDoc siacTDoc) {
		getSiacTDocs().add(siacTDoc);
		siacTDoc.setSiacDSiopeDocumentoTipo(this);

		return siacTDoc;
	}

	/**
	 * Removes the siac t doc.
	 *
	 * @param siacTDoc the siac t doc
	 * @return the siac t doc
	 */
	public SiacTDoc removeSiacTDoc(SiacTDoc siacTDoc) {
		getSiacTDocs().remove(siacTDoc);
		siacTDoc.setSiacDSiopeDocumentoTipo(null);

		return siacTDoc;
	}
	
	/**
	 * Gets the siac t docs.
	 * @return the siacDSiopeDocumentoTipoAnalogicos
	 */
	public List<SiacDSiopeDocumentoTipoAnalogico> getSiacDSiopeDocumentoTipoAnalogicos() {
		return siacDSiopeDocumentoTipoAnalogicos;
	}

	/**
	 * Sets the siac t docs.
	 * @param siacDSiopeDocumentoTipoAnalogicos the siacTDdocs to set
	 */
	public void setSiacDSiopeDocumentoTipoAnalogicos(List<SiacDSiopeDocumentoTipoAnalogico> siacDSiopeDocumentoTipoAnalogicos) {
		this.siacDSiopeDocumentoTipoAnalogicos = siacDSiopeDocumentoTipoAnalogicos;
	}
	
	/**
	 * Adds the siac t doc.
	 *
	 * @param siacDSiopeDocumentoTipoAnalogico the siac t doc
	 * @return the siac t doc
	 */
	public SiacDSiopeDocumentoTipoAnalogico addSiacDSiopeDocumentoTipoAnalogico(SiacDSiopeDocumentoTipoAnalogico siacDSiopeDocumentoTipoAnalogico) {
		getSiacDSiopeDocumentoTipoAnalogicos().add(siacDSiopeDocumentoTipoAnalogico);
		siacDSiopeDocumentoTipoAnalogico.setSiacDSiopeDocumentoTipo(this);

		return siacDSiopeDocumentoTipoAnalogico;
	}

	/**
	 * Removes the siac t doc.
	 *
	 * @param siacDSiopeDocumentoTipoAnalogico the siac t doc
	 * @return the siac t doc
	 */
	public SiacDSiopeDocumentoTipoAnalogico removeSiacDSiopeDocumentoTipoAnalogico(SiacDSiopeDocumentoTipoAnalogico siacDSiopeDocumentoTipoAnalogico) {
		getSiacDSiopeDocumentoTipoAnalogicos().remove(siacDSiopeDocumentoTipoAnalogico);
		siacDSiopeDocumentoTipoAnalogico.setSiacDSiopeDocumentoTipo(null);

		return siacDSiopeDocumentoTipoAnalogico;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return siopeDocumentoTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		siopeDocumentoTipoId = uid;
	}

}