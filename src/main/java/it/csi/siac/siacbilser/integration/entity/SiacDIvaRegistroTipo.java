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
 * The persistent class for the siac_d_iva_registro_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_iva_registro_tipo")
@NamedQuery(name="SiacDIvaRegistroTipo.findAll", query="SELECT s FROM SiacDIvaRegistroTipo s")
public class SiacDIvaRegistroTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ivareg tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_IVA_REGISTRO_TIPO_IVAREGTIPOID_GENERATOR", sequenceName="SIAC_D_IVA_REGISTRO_TIPO_IVAREG_TIPO_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_IVA_REGISTRO_TIPO_IVAREGTIPOID_GENERATOR")
	@Column(name="ivareg_tipo_id")
	private Integer ivaregTipoId;

	/** The ivareg tipo code. */
	@Column(name="ivareg_tipo_code")
	private String ivaregTipoCode;

	/** The ivareg tipo desc. */
	@Column(name="ivareg_tipo_desc")
	private String ivaregTipoDesc;

	//bi-directional many-to-one association to SiacDIvaEsigibilitaTipo
	/** The siac d iva esigibilita tipo. */
	@ManyToOne
	@JoinColumn(name="ivaes_tipo_id")
	private SiacDIvaEsigibilitaTipo siacDIvaEsigibilitaTipo;

	//bi-directional many-to-one association to SiacTIvaRegistro
	/** The siac t iva registros. */
	@OneToMany(mappedBy="siacDIvaRegistroTipo")
	private List<SiacTIvaRegistro> siacTIvaRegistros;

	/**
	 * Instantiates a new siac d iva registro tipo.
	 */
	public SiacDIvaRegistroTipo() {
	}

	/**
	 * Gets the ivareg tipo id.
	 *
	 * @return the ivareg tipo id
	 */
	public Integer getIvaregTipoId() {
		return this.ivaregTipoId;
	}

	/**
	 * Sets the ivareg tipo id.
	 *
	 * @param ivaregTipoId the new ivareg tipo id
	 */
	public void setIvaregTipoId(Integer ivaregTipoId) {
		this.ivaregTipoId = ivaregTipoId;
	}

	/**
	 * Gets the ivareg tipo code.
	 *
	 * @return the ivareg tipo code
	 */
	public String getIvaregTipoCode() {
		return this.ivaregTipoCode;
	}

	/**
	 * Sets the ivareg tipo code.
	 *
	 * @param ivaregTipoCode the new ivareg tipo code
	 */
	public void setIvaregTipoCode(String ivaregTipoCode) {
		this.ivaregTipoCode = ivaregTipoCode;
	}

	/**
	 * Gets the ivareg tipo desc.
	 *
	 * @return the ivareg tipo desc
	 */
	public String getIvaregTipoDesc() {
		return this.ivaregTipoDesc;
	}

	/**
	 * Sets the ivareg tipo desc.
	 *
	 * @param ivaregTipoDesc the new ivareg tipo desc
	 */
	public void setIvaregTipoDesc(String ivaregTipoDesc) {
		this.ivaregTipoDesc = ivaregTipoDesc;
	}

	/**
	 * Gets the siac d iva esigibilita tipo.
	 *
	 * @return the siac d iva esigibilita tipo
	 */
	public SiacDIvaEsigibilitaTipo getSiacDIvaEsigibilitaTipo() {
		return this.siacDIvaEsigibilitaTipo;
	}

	/**
	 * Sets the siac d iva esigibilita tipo.
	 *
	 * @param siacDIvaEsigibilitaTipo the new siac d iva esigibilita tipo
	 */
	public void setSiacDIvaEsigibilitaTipo(SiacDIvaEsigibilitaTipo siacDIvaEsigibilitaTipo) {
		this.siacDIvaEsigibilitaTipo = siacDIvaEsigibilitaTipo;
	}

	/**
	 * Gets the siac t iva registros.
	 *
	 * @return the siac t iva registros
	 */
	public List<SiacTIvaRegistro> getSiacTIvaRegistros() {
		return this.siacTIvaRegistros;
	}

	/**
	 * Sets the siac t iva registros.
	 *
	 * @param siacTIvaRegistros the new siac t iva registros
	 */
	public void setSiacTIvaRegistros(List<SiacTIvaRegistro> siacTIvaRegistros) {
		this.siacTIvaRegistros = siacTIvaRegistros;
	}

	/**
	 * Adds the siac t iva registro.
	 *
	 * @param siacTIvaRegistro the siac t iva registro
	 * @return the siac t iva registro
	 */
	public SiacTIvaRegistro addSiacTIvaRegistro(SiacTIvaRegistro siacTIvaRegistro) {
		getSiacTIvaRegistros().add(siacTIvaRegistro);
		siacTIvaRegistro.setSiacDIvaRegistroTipo(this);

		return siacTIvaRegistro;
	}

	/**
	 * Removes the siac t iva registro.
	 *
	 * @param siacTIvaRegistro the siac t iva registro
	 * @return the siac t iva registro
	 */
	public SiacTIvaRegistro removeSiacTIvaRegistro(SiacTIvaRegistro siacTIvaRegistro) {
		getSiacTIvaRegistros().remove(siacTIvaRegistro);
		siacTIvaRegistro.setSiacDIvaRegistroTipo(null);

		return siacTIvaRegistro;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return ivaregTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.ivaregTipoId = uid;
	}
}