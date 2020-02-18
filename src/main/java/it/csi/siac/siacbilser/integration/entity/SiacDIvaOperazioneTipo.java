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
 * The persistent class for the siac_d_iva_operazione_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_iva_operazione_tipo")
@NamedQuery(name="SiacDIvaOperazioneTipo.findAll", query="SELECT s FROM SiacDIvaOperazioneTipo s")
public class SiacDIvaOperazioneTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ivaop tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_IVA_OPERAZIONE_TIPO_IVAOPTIPOID_GENERATOR", sequenceName="SIAC_D_IVA_OPERAZIONE_TIPO_IVAOP_TIPO_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_IVA_OPERAZIONE_TIPO_IVAOPTIPOID_GENERATOR")
	@Column(name="ivaop_tipo_id")
	private Integer ivaopTipoId;

	/** The ivaop tipo code. */
	@Column(name="ivaop_tipo_code")
	private String ivaopTipoCode;

	/** The ivaop tipo desc. */
	@Column(name="ivaop_tipo_desc")
	private String ivaopTipoDesc;

	//bi-directional many-to-one association to SiacTIvaAliquota
	/** The siac t iva aliquotas. */
	@OneToMany(mappedBy="siacDIvaOperazioneTipo")
	private List<SiacTIvaAliquota> siacTIvaAliquotas;

	/**
	 * Instantiates a new siac d iva operazione tipo.
	 */
	public SiacDIvaOperazioneTipo() {
	}

	/**
	 * Gets the ivaop tipo id.
	 *
	 * @return the ivaop tipo id
	 */
	public Integer getIvaopTipoId() {
		return this.ivaopTipoId;
	}

	/**
	 * Sets the ivaop tipo id.
	 *
	 * @param ivaopTipoId the new ivaop tipo id
	 */
	public void setIvaopTipoId(Integer ivaopTipoId) {
		this.ivaopTipoId = ivaopTipoId;
	}

	/**
	 * Gets the ivaop tipo code.
	 *
	 * @return the ivaop tipo code
	 */
	public String getIvaopTipoCode() {
		return this.ivaopTipoCode;
	}

	/**
	 * Sets the ivaop tipo code.
	 *
	 * @param ivaopTipoCode the new ivaop tipo code
	 */
	public void setIvaopTipoCode(String ivaopTipoCode) {
		this.ivaopTipoCode = ivaopTipoCode;
	}

	/**
	 * Gets the ivaop tipo desc.
	 *
	 * @return the ivaop tipo desc
	 */
	public String getIvaopTipoDesc() {
		return this.ivaopTipoDesc;
	}

	/**
	 * Sets the ivaop tipo desc.
	 *
	 * @param ivaopTipoDesc the new ivaop tipo desc
	 */
	public void setIvaopTipoDesc(String ivaopTipoDesc) {
		this.ivaopTipoDesc = ivaopTipoDesc;
	}

	/**
	 * Gets the siac t iva aliquotas.
	 *
	 * @return the siac t iva aliquotas
	 */
	public List<SiacTIvaAliquota> getSiacTIvaAliquotas() {
		return this.siacTIvaAliquotas;
	}

	/**
	 * Sets the siac t iva aliquotas.
	 *
	 * @param siacTIvaAliquotas the new siac t iva aliquotas
	 */
	public void setSiacTIvaAliquotas(List<SiacTIvaAliquota> siacTIvaAliquotas) {
		this.siacTIvaAliquotas = siacTIvaAliquotas;
	}

	/**
	 * Adds the siac t iva aliquota.
	 *
	 * @param siacTIvaAliquota the siac t iva aliquota
	 * @return the siac t iva aliquota
	 */
	public SiacTIvaAliquota addSiacTIvaAliquota(SiacTIvaAliquota siacTIvaAliquota) {
		getSiacTIvaAliquotas().add(siacTIvaAliquota);
		siacTIvaAliquota.setSiacDIvaOperazioneTipo(this);

		return siacTIvaAliquota;
	}

	/**
	 * Removes the siac t iva aliquota.
	 *
	 * @param siacTIvaAliquota the siac t iva aliquota
	 * @return the siac t iva aliquota
	 */
	public SiacTIvaAliquota removeSiacTIvaAliquota(SiacTIvaAliquota siacTIvaAliquota) {
		getSiacTIvaAliquotas().remove(siacTIvaAliquota);
		siacTIvaAliquota.setSiacDIvaOperazioneTipo(null);

		return siacTIvaAliquota;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return ivaopTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.ivaopTipoId = uid;
	}

}