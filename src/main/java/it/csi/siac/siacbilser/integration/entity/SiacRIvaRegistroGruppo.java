/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_r_iva_registro_gruppo database table.
 * 
 */
@Entity
@Table(name="siac_r_iva_registro_gruppo")
@NamedQuery(name="SiacRIvaRegistroGruppo.findAll", query="SELECT s FROM SiacRIvaRegistroGruppo s")
public class SiacRIvaRegistroGruppo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ivaregru id. */
	@Id
	@SequenceGenerator(name="SIAC_R_IVA_REGISTRO_GRUPPO_IVAREGRUID_GENERATOR", sequenceName="SIAC_R_IVA_REGISTRO_GRUPPO_IVAREGRU_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_IVA_REGISTRO_GRUPPO_IVAREGRUID_GENERATOR")
	@Column(name="ivaregru_id")
	private Integer ivaregruId;

	//bi-directional many-to-one association to SiacTIvaGruppo
	/** The siac t iva gruppo. */
	@ManyToOne
	@JoinColumn(name="ivagru_id")
	private SiacTIvaGruppo siacTIvaGruppo;

	//bi-directional many-to-one association to SiacTIvaRegistro
	/** The siac t iva registro. */
	@ManyToOne
	@JoinColumn(name="ivareg_id")
	private SiacTIvaRegistro siacTIvaRegistro;

	/**
	 * Instantiates a new siac r iva registro gruppo.
	 */
	public SiacRIvaRegistroGruppo() {
	}

	/**
	 * Gets the ivaregru id.
	 *
	 * @return the ivaregru id
	 */
	public Integer getIvaregruId() {
		return this.ivaregruId;
	}

	/**
	 * Sets the ivaregru id.
	 *
	 * @param ivaregruId the new ivaregru id
	 */
	public void setIvaregruId(Integer ivaregruId) {
		this.ivaregruId = ivaregruId;
	}

	/**
	 * Gets the siac t iva gruppo.
	 *
	 * @return the siac t iva gruppo
	 */
	public SiacTIvaGruppo getSiacTIvaGruppo() {
		return this.siacTIvaGruppo;
	}

	/**
	 * Sets the siac t iva gruppo.
	 *
	 * @param siacTIvaGruppo the new siac t iva gruppo
	 */
	public void setSiacTIvaGruppo(SiacTIvaGruppo siacTIvaGruppo) {
		this.siacTIvaGruppo = siacTIvaGruppo;
	}

	/**
	 * Gets the siac t iva registro.
	 *
	 * @return the siac t iva registro
	 */
	public SiacTIvaRegistro getSiacTIvaRegistro() {
		return this.siacTIvaRegistro;
	}

	/**
	 * Sets the siac t iva registro.
	 *
	 * @param siacTIvaRegistro the new siac t iva registro
	 */
	public void setSiacTIvaRegistro(SiacTIvaRegistro siacTIvaRegistro) {
		this.siacTIvaRegistro = siacTIvaRegistro;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return ivaregruId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.ivaregruId = uid;
	}

}