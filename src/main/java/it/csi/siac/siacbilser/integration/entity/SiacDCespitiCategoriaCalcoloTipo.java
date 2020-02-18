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
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_d_accredito_gruppo database table.
 * 
 */
@Entity
@Table(name="siac_d_cespiti_categoria_calcolo_tipo")
@NamedQuery(name="SiacDCespitiCategoriaCalcoloTipo.findAll", query="SELECT s FROM SiacDCespitiCategoriaCalcoloTipo s")
public class SiacDCespitiCategoriaCalcoloTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The accredito gruppo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_CESPITI_CATEGORIA_CALCOLO_TIPOID_GENERATOR", allocationSize=1, sequenceName="siac_d_cespiti_categoria_calcolo_tip_cescat_calcolo_tipo_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_CESPITI_CATEGORIA_CALCOLO_TIPOID_GENERATOR")
	
	@Column(name="cescat_calcolo_tipo_id")
	private Integer cescatCalcoloTipoId;

	@Column(name="cescat_calcolo_tipo_code")
	private String cescatCalcoloTipoCode;

	@Column(name="cescat_calcolo_tipo_desc")
	private String cescatCalcoloTipoDesc;
	

	

	/**
	 * @return the cescatCalcoloTipoId
	 */
	public Integer getCescatCalcoloTipoId() {
		return cescatCalcoloTipoId;
	}

	/**
	 * @param cescatCalcoloTipoId the cescatCalcoloTipoId to set
	 */
	public void setCescatCalcoloTipoId(Integer cescatCalcoloTipoId) {
		this.cescatCalcoloTipoId = cescatCalcoloTipoId;
	}

	/**
	 * @return the cescatCalcoloTipoCode
	 */
	public String getCescatCalcoloTipoCode() {
		return cescatCalcoloTipoCode;
	}

	/**
	 * @param cescatCalcoloTipoCode the cescatCalcoloTipoCode to set
	 */
	public void setCescatCalcoloTipoCode(String cescatCalcoloTipoCode) {
		this.cescatCalcoloTipoCode = cescatCalcoloTipoCode;
	}

	/**
	 * @return the cescatCalcoloTipoDesc
	 */
	public String getCescatCalcoloTipoDesc() {
		return cescatCalcoloTipoDesc;
	}

	/**
	 * @param cescatCalcoloTipoDesc the cescatCalcoloTipoDesc to set
	 */
	public void setCescatCalcoloTipoDesc(String cescatCalcoloTipoDesc) {
		this.cescatCalcoloTipoDesc = cescatCalcoloTipoDesc;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return cescatCalcoloTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.cescatCalcoloTipoId = uid;
		
	}

}