/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;
import java.util.Date;

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


/**
 * The persistent class for the siac_d_cespiti_categoria database table.
 * 
 */
@Entity
@Table(name="siac_t_cespiti_ammortamento_dett")
@NamedQuery(name="SiacTCespitiAmmortamentoDett.findAll", query="SELECT s FROM SiacTCespitiAmmortamentoDett s")
public class SiacTCespitiAmmortamentoDett extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="siac_t_cespiti_ammortamento_dett_ces_amm_dett_idGENERATOR", allocationSize=1, sequenceName="siac_t_cespiti_ammortamento_dett_ces_amm_dett_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="siac_t_cespiti_ammortamento_dett_ces_amm_dett_idGENERATOR")
	@Column(name="ces_amm_dett_id")
	private Integer cesAmmDettId;
	
	@Column(name="ces_amm_dett_anno")
	private Integer cesAmmDettAnno;

	@Column(name="ces_amm_dett_importo")
	private BigDecimal cesAmmDettImporto;
	
	@Column(name="ces_amm_dett_data")
	private Date cesAmmDettData;

	@ManyToOne
	@JoinColumn(name="pnota_id")
	private SiacTPrimaNota siacTPrimaNota;
	
	@Column(name="num_reg_def_ammortamento")
	private String numRegDefAmmortamento;
	
	// bi-directional many-to-one association 
	@ManyToOne
	@JoinColumn(name="ces_amm_id")
	private SiacTCespitiAmmortamento siacTCespitiAmmortamento;
	
	/**
	 * @return the cesAmmDettId
	 */
	public Integer getCesAmmDettId() {
		return cesAmmDettId;
	}

	/**
	 * @param cesAmmDettId the cesAmmDettId to set
	 */
	public void setCesAmmDettId(Integer cesAmmDettId) {
		this.cesAmmDettId = cesAmmDettId;
	}


	/**
	 * @return the cesAmmDettAnno
	 */
	public Integer getCesAmmDettAnno() {
		return cesAmmDettAnno;
	}

	/**
	 * @param cesAmmDettAnno the cesAmmDettAnno to set
	 */
	public void setCesAmmDettAnno(Integer cesAmmDettAnno) {
		this.cesAmmDettAnno = cesAmmDettAnno;
	}

	/**
	 * @return the cesAmmDettImporto
	 */
	public BigDecimal getCesAmmDettImporto() {
		return cesAmmDettImporto;
	}

	/**
	 * @param cesAmmDettImporto the cesAmmDettImporto to set
	 */
	public void setCesAmmDettImporto(BigDecimal cesAmmDettImporto) {
		this.cesAmmDettImporto = cesAmmDettImporto;
	}
	
	/**
	 * @return the cesAmmDettData
	 */
	public Date getCesAmmDettData() {
		return cesAmmDettData;
	}

	/**
	 * @param cesAmmDettData the cesAmmDettData to set
	 */
	public void setCesAmmDettData(Date cesAmmDettData) {
		this.cesAmmDettData = cesAmmDettData;
	}

	/**
	 * @return the siacTPrimaNota
	 */
	public SiacTPrimaNota getSiacTPrimaNota() {
		return siacTPrimaNota;
	}

	/**
	 * @param siacTPrimaNota the siacTPrimaNota to set
	 */
	public void setSiacTPrimaNota(SiacTPrimaNota siacTPrimaNota) {
		this.siacTPrimaNota = siacTPrimaNota;
	}
	
	/**
	 * @return the numRegDefAmmortamento
	 */
	public String getNumRegDefAmmortamento() {
		return numRegDefAmmortamento;
	}

	/**
	 * @param numRegDefAmmortamento the numRegDefAmmortamento to set
	 */
	public void setNumRegDefAmmortamento(String numRegDefAmmortamento) {
		this.numRegDefAmmortamento = numRegDefAmmortamento;
	}

	/**
	 * @return the siacTCespitiAmmortamenti
	 */
	public SiacTCespitiAmmortamento getSiacTCespitiAmmortamento() {
		return siacTCespitiAmmortamento;
	}

	/**
	 * Sets the siac T cespiti ammortamento.
	 *
	 * @param siacTCespitiAmmortamento the new siac T cespiti ammortamento
	 */
	public void setSiacTCespitiAmmortamento(SiacTCespitiAmmortamento siacTCespitiAmmortamento) {
		this.siacTCespitiAmmortamento = siacTCespitiAmmortamento;
	}

	@Override
	public Integer getUid() {
		return this.cesAmmDettId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cesAmmDettId = uid;
	}

}