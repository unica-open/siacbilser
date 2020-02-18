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


/**
 * The persistent class for the siac_d_cespiti_categoria database table.
 * 
 */
@Entity
@Table(name="siac_t_cespiti_elab_ammortamenti_dett")
@NamedQuery(name="SiacTCespitiElabAmmortamentiDett.findAll", query="SELECT s FROM SiacTCespitiElabAmmortamentiDett s")
public class SiacTCespitiElabAmmortamentiDett extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id 
	@SequenceGenerator(name="siac_t_cespiti_elab_ammortamenti_dett_ces_amm_dett_idGENERATOR", allocationSize=1, sequenceName="siac_t_cespiti_elab_ammortamenti_dett_elab_dett_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="siac_t_cespiti_elab_ammortamenti_dett_ces_amm_dett_idGENERATOR")
	@Column(name="elab_dett_id")
	private Integer elabDettId;
	
	@ManyToOne
	@JoinColumn(name="pdce_conto_id")
	private SiacTPdceConto siacTPdceConto; 
    
    @Column(name="pdce_conto_code")
	private String pdceContoCode;
	
	@Column(name="pdce_conto_desc")
	private String pdceContoDesc;
	
	@Column(name="elab_det_importo")
	private BigDecimal elabDetImporto;

	@Column(name="elab_det_segno")
	private String elabDetSegno;
	
	@Column(name="numero_cespiti")
	private Integer numeroCespiti;
	
	@ManyToOne
	@JoinColumn(name="pnota_id")
	private SiacTPrimaNota siacTPrimaNota;
	
	// bi-directional many-to-one association 
	@ManyToOne
	@JoinColumn(name="elab_id")
	private SiacTCespitiElabAmmortamenti siacTCespitiElabAmmortamenti;
	
	/**
	 * @return the elabDettId
	 */
	public Integer getElabDettId() {
		return elabDettId;
	}

	/**
	 * @param elabDettId the elabDettId to set
	 */
	public void setElabDettId(Integer elabDettId) {
		this.elabDettId = elabDettId;
	}

	/**
	 * @return the siacTPdceConto
	 */
	public SiacTPdceConto getSiacTPdceConto() {
		return siacTPdceConto;
	}

	/**
	 * @param siacTPdceConto the siacTPdceConto to set
	 */
	public void setSiacTPdceConto(SiacTPdceConto siacTPdceConto) {
		this.siacTPdceConto = siacTPdceConto;
	}

	/**
	 * @return the pdceContoCode
	 */
	public String getPdceContoCode() {
		return pdceContoCode;
	}

	/**
	 * @param pdceContoCode the pdceContoCode to set
	 */
	public void setPdceContoCode(String pdceContoCode) {
		this.pdceContoCode = pdceContoCode;
	}

	/**
	 * @return the pdceContoDesc
	 */
	public String getPdceContoDesc() {
		return pdceContoDesc;
	}

	/**
	 * @param pdceContoDesc the pdceContoDesc to set
	 */
	public void setPdceContoDesc(String pdceContoDesc) {
		this.pdceContoDesc = pdceContoDesc;
	}

	/**
	 * @return the movepDetImporto
	 */
	public BigDecimal getElabDetImporto() {
		return elabDetImporto;
	}

	/**
	 * Sets the elab det importo.
	 *
	 * @param elabDetImporto the new elab det importo
	 */
	public void setElabDetImporto(BigDecimal elabDetImporto) {
		this.elabDetImporto = elabDetImporto;
	}

	/**
	 * @return the movepDetSegno
	 */
	public String getElabDetSegno() {
		return elabDetSegno;
	}

	/**
	 * Sets the elab det segno.
	 *
	 * @param elabDetSegno the new elab det segno
	 */
	public void setElabDetSegno(String elabDetSegno) {
		this.elabDetSegno = elabDetSegno;
	}

	/**
	 * @return the siacTCespitiElabAmmortamenti
	 */
	public SiacTCespitiElabAmmortamenti getSiacTCespitiElabAmmortamenti() {
		return siacTCespitiElabAmmortamenti;
	}

	/**
	 * @param siacTCespitiElabAmmortamenti the siacTCespitiElabAmmortamenti to set
	 */
	public void setSiacTCespitiElabAmmortamenti(SiacTCespitiElabAmmortamenti siacTCespitiElabAmmortamenti) {
		this.siacTCespitiElabAmmortamenti = siacTCespitiElabAmmortamenti;
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
	 * @return the numeroCespiti
	 */
	public Integer getNumeroCespiti() {
		return numeroCespiti;
	}

	/**
	 * @param numeroCespiti the numeroCespiti to set
	 */
	public void setNumeroCespiti(Integer numeroCespiti) {
		this.numeroCespiti = numeroCespiti;
	}

	@Override
	public Integer getUid() {
		return this.elabDettId;
	}

	@Override
	public void setUid(Integer elabDettId) {
		this.elabDettId = elabDettId;
	}

}