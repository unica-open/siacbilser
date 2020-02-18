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
 * The persistent class for the siac_r_variazione_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_cespiti_bene_tipo_conto_patr_cat")
@NamedQuery(name="SiacRCespitiBeneTipoContoPatrCat.findAll", query="SELECT s FROM SiacRCespitiBeneTipoContoPatrCat s")
public class SiacRCespitiBeneTipoContoPatrCat extends SiacTEnteBase {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3070565339130491325L;
	
	/**
	 * Instantiates a new siac R categoria cespiti aliquota calcolo tipo.
	 */
	public SiacRCespitiBeneTipoContoPatrCat(){
		
	}
	
//	/** The id. */ siac_r_cespiti_bene_tipo_cont_ces_bene_tipo_conto_patr_cat__seq
	@Id
    @SequenceGenerator(name="SIAC_R_CESPITI_BENE_TIPO_CONTO_PATR_CAT_CESBENETIPOCONTOPATRCATID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CESPITI_BENE_TIPO_CONT_CES_BENE_TIPO_CONTO_PATR_CAT__SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CESPITI_BENE_TIPO_CONTO_PATR_CAT_CESBENETIPOCONTOPATRCATID_GENERATOR")
	@Column(name="ces_bene_tipo_conto_patr_cat_id")
	private Integer cesBeneTipoContoPatrCatId;
	
	@ManyToOne
	@JoinColumn(name="ces_bene_tipo_id")
	private SiacDCespitiBeneTipo siacDCespitiBeneTipo;
	
	@ManyToOne
	@JoinColumn(name="cescat_id")
	private SiacDCespitiCategoria siacDCespitiCategoria;
	
	@ManyToOne
	@JoinColumn(name="pdce_conto_patrimoniale_id")
	private SiacTPdceConto siacTPdceContoPatrimoniale; 
	
	@Column(name="pdce_conto_patrimoniale_code")
	private String pdceContoPatrimonialeCode;
	
	@Column(name="pdce_conto_patrimoniale_desc")
	private String pdceContoPatrimonialeDesc;
	
	@Override
	public Integer getUid() {
		return cesBeneTipoContoPatrCatId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cesBeneTipoContoPatrCatId = uid;
	}

	/**
	 * @return the cescatAliquotaCalcoloTipoId
	 */
	public Integer getCescatAliquotaCalcoloTipoId() {
		return cesBeneTipoContoPatrCatId;
	}

	/**
	 * @param cescatAliquotaCalcoloTipoId the cescatAliquotaCalcoloTipoId to set
	 */
	public void setCescatAliquotaCalcoloTipoId(Integer cescatAliquotaCalcoloTipoId) {
		this.cesBeneTipoContoPatrCatId = cescatAliquotaCalcoloTipoId;
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
	 * @return the cesBeneTipoContoPatrCatId
	 */
	public Integer getCesBeneTipoContoPatrCatId() {
		return cesBeneTipoContoPatrCatId;
	}

	/**
	 * @param cesBeneTipoContoPatrCatId the cesBeneTipoContoPatrCatId to set
	 */
	public void setCesBeneTipoContoPatrCatId(Integer cesBeneTipoContoPatrCatId) {
		this.cesBeneTipoContoPatrCatId = cesBeneTipoContoPatrCatId;
	}

	/**
	 * @return the siacDCespitiBeneTipo
	 */
	public SiacDCespitiBeneTipo getSiacDCespitiBeneTipo() {
		return siacDCespitiBeneTipo;
	}

	/**
	 * @param siacDCespitiBeneTipo the siacDCespitiBeneTipo to set
	 */
	public void setSiacDCespitiBeneTipo(SiacDCespitiBeneTipo siacDCespitiBeneTipo) {
		this.siacDCespitiBeneTipo = siacDCespitiBeneTipo;
	}

	/**
	 * @return the siacTPdceContoPatrimoniale
	 */
	public SiacTPdceConto getSiacTPdceContoPatrimoniale() {
		return siacTPdceContoPatrimoniale;
	}

	/**
	 * @param siacTPdceContoPatrimoniale the siacTPdceContoPatrimoniale to set
	 */
	public void setSiacTPdceContoPatrimoniale(SiacTPdceConto siacTPdceContoPatrimoniale) {
		this.siacTPdceContoPatrimoniale = siacTPdceContoPatrimoniale;
	}

	/**
	 * @return the pdceContoPatrimonialeCode
	 */
	public String getPdceContoPatrimonialeCode() {
		return pdceContoPatrimonialeCode;
	}

	/**
	 * @param pdceContoPatrimonialeCode the pdceContoPatrimonialeCode to set
	 */
	public void setPdceContoPatrimonialeCode(String pdceContoPatrimonialeCode) {
		this.pdceContoPatrimonialeCode = pdceContoPatrimonialeCode;
	}

	/**
	 * @return the pdceContoPatrimonialeDesc
	 */
	public String getPdceContoPatrimonialeDesc() {
		return pdceContoPatrimonialeDesc;
	}

	/**
	 * @param pdceContoPatrimonialeDesc the pdceContoPatrimonialeDesc to set
	 */
	public void setPdceContoPatrimonialeDesc(String pdceContoPatrimonialeDesc) {
		this.pdceContoPatrimonialeDesc = pdceContoPatrimonialeDesc;
	}
	
}