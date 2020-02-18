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

/**
 * The persistent class for the siac_d_siope_documento_tipo_analogico database table.
 * 
 */
@Entity
@Table(name="siac_d_siope_documento_tipo_analogico")
@NamedQuery(name="SiacDSiopeDocumentoTipoAnalogico.findAll", query="SELECT s FROM SiacDSiopeDocumentoTipoAnalogico s")
public class SiacDSiopeDocumentoTipoAnalogico extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The siope documento tipo analogico id. */
	@Id
	@SequenceGenerator(name="SIAC_D_SIOPE_DOCUMENTO_TIPO_ANALOGICO_SIOPEDOCUMENTOTIPOANALOGICOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_SIOPE_DOCUMENTO_TIPO_ANALOGICO_SIOPE_DOCUMENTO_TIPO_ANALOGICO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_SIOPE_DOCUMENTO_TIPO_ANALOGICO_SIOPEDOCUMENTOTIPOANALOGICOID_GENERATOR")
	@Column(name="siope_documento_tipo_analogico_id")
	private Integer siopeDocumentoTipoAnalogicoId;

	/** The siope documento tipo analogico code. */
	@Column(name="siope_documento_tipo_analogico_code")
	private String siopeDocumentoTipoAnalogicoCode;

	/** The siope documento tipo analogico desc. */
	@Column(name="siope_documento_tipo_analogico_desc")
	private String siopeDocumentoTipoAnalogicoDesc;
	
	/** The siope documento tipo analogico desc bnkit. */
	@Column(name="siope_documento_tipo_analogico_desc_bnkit")
	private String siopeDocumentoTipoAnalogicoDescBnkit;
	
	//bi-directional many-to-one association to SiacTDoc
	/** The siac t atto amms. */
	@OneToMany(mappedBy="siacDSiopeDocumentoTipoAnalogico")
	private List<SiacTDoc> siacTDocs;

	//bi-directional many-to-one association to SiacDSiopeDocumentoTipo
	/** The siac d siope tipo debito. */
	@ManyToOne
	@JoinColumn(name="siope_documento_tipo_id")
	private SiacDSiopeDocumentoTipo siacDSiopeDocumentoTipo;

	/**
	 * Instantiates a new siac d siope documento tipo analogico.
	 */
	public SiacDSiopeDocumentoTipoAnalogico() {
	}

	/**
	 * Gets the siope documento tipo analogico id.
	 * @return the siopeDocumentoTipoAnalogicoId
	 */
	public Integer getSiopeDocumentoTipoAnalogicoId() {
		return siopeDocumentoTipoAnalogicoId;
	}

	/**
	 * Sets the siope documento tipo analogico id.
	 * @param siopeDocumentoTipoAnalogicoId the siopeDocumentoTipoAnalogicoId to set
	 */
	public void setSiopeDocumentoTipoAnalogicoId(Integer siopeDocumentoTipoAnalogicoId) {
		this.siopeDocumentoTipoAnalogicoId = siopeDocumentoTipoAnalogicoId;
	}

	/**
	 * Gets the siope documento tipo analogico code.
	 * @return the siopeDocumentoTipoAnalogicoCode
	 */
	public String getSiopeDocumentoTipoAnalogicoCode() {
		return siopeDocumentoTipoAnalogicoCode;
	}

	/**
	 * Sets the siope documento tipo analogico code.
	 * @param siopeDocumentoTipoAnalogicoCode the siopeDocumentoTipoAnalogicoCode to set
	 */
	public void setSiopeDocumentoTipoAnalogicoCode(String siopeDocumentoTipoAnalogicoCode) {
		this.siopeDocumentoTipoAnalogicoCode = siopeDocumentoTipoAnalogicoCode;
	}

	/**
	 * Gets the siope documento tipo analogico desc.
	 * @return the siopeDocumentoTipoAnalogicoDesc
	 */
	public String getSiopeDocumentoTipoAnalogicoDesc() {
		return siopeDocumentoTipoAnalogicoDesc;
	}

	/**
	 * Sets the siope documento tipo analogico desc.
	 * @param siopeDocumentoTipoAnalogicoDesc the siopeDocumentoTipoAnalogicoDesc to set
	 */
	public void setSiopeDocumentoTipoAnalogicoDesc(String siopeDocumentoTipoAnalogicoDesc) {
		this.siopeDocumentoTipoAnalogicoDesc = siopeDocumentoTipoAnalogicoDesc;
	}

	/**
	 * Gets the siope documento tipo analogico desc bnkit.
	 * @return the siopeDocumentoTipoAnalogicoDescBnkit
	 */
	public String getSiopeDocumentoTipoAnalogicoDescBnkit() {
		return siopeDocumentoTipoAnalogicoDescBnkit;
	}

	/**
	 * Sets the siope documento tipo analogico desc bnkit.
	 * @param siopeDocumentoTipoAnalogicoDescBnkit the siopeDocumentoTipoAnalogicoDescBnkit to set
	 */
	public void setSiopeDocumentoTipoAnalogicoDescBnkit(String siopeDocumentoTipoAnalogicoDescBnkit) {
		this.siopeDocumentoTipoAnalogicoDescBnkit = siopeDocumentoTipoAnalogicoDescBnkit;
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
		siacTDoc.setSiacDSiopeDocumentoTipoAnalogico(this);

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
		siacTDoc.setSiacDSiopeDocumentoTipoAnalogico(null);

		return siacTDoc;
	}

	/**
	 * Gets the siac d siope documento tipo.
	 *
	 * @return the siac d siope documento tipo
	 */
	public SiacDSiopeDocumentoTipo getSiacDSiopeDocumentoTipo() {
		return this.siacDSiopeDocumentoTipo;
	}

	/**
	 * Sets the siac d siope documento tipo.
	 *
	 * @param siacDSiopeDocumentoTipo the new siac d siope documento tipo
	 */
	public void setSiacDSiopeDocumentoTipo(SiacDSiopeDocumentoTipo siacDSiopeDocumentoTipo) {
		this.siacDSiopeDocumentoTipo = siacDSiopeDocumentoTipo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return siopeDocumentoTipoAnalogicoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		siopeDocumentoTipoAnalogicoId = uid;
	}

}