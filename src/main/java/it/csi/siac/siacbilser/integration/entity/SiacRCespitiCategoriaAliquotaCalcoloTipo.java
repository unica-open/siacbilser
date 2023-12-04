/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;

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
 * The persistent class for the siac_r_variazione_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_cespiti_categoria_aliquota_calcolo_tipo")
@NamedQuery(name="SiacRCespitiCategoriaAliquotaCalcoloTipo.findAll", query="SELECT s FROM SiacRCespitiCategoriaAliquotaCalcoloTipo s")
public class SiacRCespitiCategoriaAliquotaCalcoloTipo extends SiacTEnteBase {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3070565339130491325L;
	
	/**
	 * Instantiates a new siac R categoria cespiti aliquota calcolo tipo.
	 */
	public SiacRCespitiCategoriaAliquotaCalcoloTipo(){
		
	}
	
//	/** The id. */
	@Id
    @SequenceGenerator(name="SIAC_R_CESPITI_CATEGORIA_ALIQUOTA_CALCOLO_TIPO_CESCATALIQUOTACALCOLOTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CESPITI_CATEGORIA_ALIQ_CESCAT_ALIQUOTA_CALCOLO_TIPO__SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CESPITI_CATEGORIA_ALIQUOTA_CALCOLO_TIPO_CESCATALIQUOTACALCOLOTIPOID_GENERATOR")
	@Column(name="cescat_aliquota_calcolo_tipo_id")
	private Integer cescatAliquotaCalcoloTipoId;
	
	@ManyToOne
	@JoinColumn(name="cescat_id")
	private SiacDCespitiCategoria siacDCespitiCategoria;
	
	@ManyToOne
	@JoinColumn(name="cescat_calcolo_tipo_id")
	private SiacDCespitiCategoriaCalcoloTipo siacDCespitiCategoriaCalcoloTipo;
	
	@Column(name="aliquota_annua")
	private BigDecimal aliquotaAnnua;
	
	
	@Override
	public Integer getUid() {
		return cescatAliquotaCalcoloTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cescatAliquotaCalcoloTipoId = uid;
	}

	/**
	 * @return the cescatAliquotaCalcoloTipoId
	 */
	public Integer getCescatAliquotaCalcoloTipoId() {
		return cescatAliquotaCalcoloTipoId;
	}

	/**
	 * @param cescatAliquotaCalcoloTipoId the cescatAliquotaCalcoloTipoId to set
	 */
	public void setCescatAliquotaCalcoloTipoId(Integer cescatAliquotaCalcoloTipoId) {
		this.cescatAliquotaCalcoloTipoId = cescatAliquotaCalcoloTipoId;
	}

	/**
	 * @return the siacDCespitiCategoria
	 */
	public SiacDCespitiCategoria getSiacDCespitiCategoria() {
		return siacDCespitiCategoria;
	}

	/**
	 * @param siacDCespitiCategoria the siacDCespitiCategoria to set
	 */
	public void setSiacDCespitiCategoria(SiacDCespitiCategoria siacDCespitiCategoria) {
		this.siacDCespitiCategoria = siacDCespitiCategoria;
	}

	/**
	 * @return the siacDCespitiCategoriaCalcoloTipo
	 */
	public SiacDCespitiCategoriaCalcoloTipo getSiacDCespitiCategoriaCalcoloTipo() {
		return siacDCespitiCategoriaCalcoloTipo;
	}

	/**
	 * @param siacDCespitiCategoriaCalcoloTipo the siacDCespitiCategoriaCalcoloTipo to set
	 */
	public void setSiacDCespitiCategoriaCalcoloTipo(SiacDCespitiCategoriaCalcoloTipo siacDCespitiCategoriaCalcoloTipo) {
		this.siacDCespitiCategoriaCalcoloTipo = siacDCespitiCategoriaCalcoloTipo;
	}

	/**
	 * @return the aliquotaAnnua
	 */
	public BigDecimal getAliquotaAnnua() {
		return aliquotaAnnua;
	}

	/**
	 * @param aliquotaAnnua the aliquotaAnnua to set
	 */
	public void setAliquotaAnnua(BigDecimal aliquotaAnnua) {
		this.aliquotaAnnua = aliquotaAnnua;
	}
	
}