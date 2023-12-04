/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.CascadeType;
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
 * The persistent class for the siac_d_cespiti_categoria database table.
 * 
 */
@Entity
@Table(name="siac_d_cespiti_bene_tipo")
@NamedQuery(name="SiacDCespitiBeneTipo.findAll", query="SELECT s FROM SiacDCespitiBeneTipo s")
public class SiacDCespitiBeneTipo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_CESPITI_BENE_TIPOID_GENERATOR", allocationSize=1, sequenceName="siac_d_cespiti_bene_tipo_ces_bene_tipo_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_CESPITI_BENE_TIPOID_GENERATOR")

	@Column(name="ces_bene_tipo_id")
	private Integer cesBeneTipoId;

	@Column(name="ces_bene_tipo_code")
	private String cesBeneTipoCode;

	@Column(name="ces_bene_tipo_desc")
	private String cesBeneTipoDesc;

	@Column(name="testo_scrittura_ammortamento")
	private String testoScritturaAmmortamento;

	@ManyToOne
	@JoinColumn(name="evento_ammortamento_id")
	private SiacDEvento siacDEventoAmmortamento;
	
	@Column(name="evento_ammortamento_code")
	private String eventoAmmortamentoCode;
	
	@Column(name="evento_ammortamento_desc")
	private String eventoAmmortamentoDesc;
	
	
	
	@ManyToOne
	@JoinColumn(name="causale_ep_ammortamento_id")
	private SiacTCausaleEp siacTCausaleEpAmmortamento;
	
	@Column(name="causale_ep_ammortamento_code")
	private String causaleEpAmmortamentoCode;
	
	@Column(name="causale_ep_ammortamento_desc")
	private String causaleEpAmmortamentoDesc;

	
	
	@ManyToOne
	@JoinColumn(name="evento_incremento_id")
	private SiacDEvento siacDEventoIncr;

	@Column(name="evento_incremento_code")
	private String eventoIncrementoCode;
	
	@Column(name="evento_incremento_desc")
	private String eventoIncrementoDesc;

	
	@ManyToOne
	@JoinColumn(name="causale_ep_incremento_id")
	private SiacTCausaleEp siacTCausaleEpIncr;

	@Column(name="causale_ep_incremento_code")
	private String causaleEpIncrementoCode;
	
	@Column(name="causale_ep_incremento_desc")
	private String causaleEpIncrementoDesc;

	
	@ManyToOne
	@JoinColumn(name="evento_decremento_id")
	private SiacDEvento siacDEventoDec;
	
	@Column(name="evento_decremento_code")
	private String eventoDecrementoCode;
	
	@Column(name="evento_decremento_desc")
	private String eventoDecrementoDesc;

	
	
	@ManyToOne
	@JoinColumn(name="causale_ep_decremento_id")
	private SiacTCausaleEp siacTCausaleEpDecremento;              

	@Column(name="causale_ep_decremento_code")
	private String causaleEpDecrementoCode;
	
	@Column(name="causale_ep_decremento_desc")
	private String causaleEpDecrementoDesc;

	
	
	@ManyToOne
	@JoinColumn(name="pdce_conto_ammortamento_id")
	private SiacTPdceConto siacTPdceContoAmmortamento ; 
    
	@Column(name="pdce_conto_ammortamento_code")
	private String pdceContoAmmortamentoCode;
	
	@Column(name="pdce_conto_ammortamento_desc")
	private String pdceContoAmmortamentoDesc;

	
	
    @ManyToOne
	@JoinColumn(name="pdce_conto_fondo_ammortamento_id")
	private SiacTPdceConto siacTPdceContoFondoAmmortamento; 
    
    @Column(name="pdce_conto_fondo_ammortamento_code")
	private String pdceContoFondoAmmortamentoCode;
	
	@Column(name="pdce_conto_fondo_ammortamento_desc")
	private String pdceContoFondoAmmortamentoDesc;
	
	
    
    @ManyToOne
	@JoinColumn(name="pdce_conto_plusvalenza_id")
	private SiacTPdceConto siacTPdceContoPlusvalenza; 
   
	@Column(name="pdce_conto_plusvalenza_code")
	private String pdceContoPlusvalenzaCode;

	@Column(name="pdce_conto_plusvalenza_desc")
	private String pdceContoPlusvalenzaDesc;
	

	
    @ManyToOne
	@JoinColumn(name="pdce_conto_minusvalenza_id")
	private SiacTPdceConto siacTPdceContoMinusvalenza; 
   
	@Column(name="pdce_conto_minusvalenza_code")
	private String pdceContoMinusvalenzaCode;

	@Column(name="pdce_conto_minusvalenza_desc")
	private String pdceContoMinusvalenzaDesc;

	
    
    @ManyToOne
	@JoinColumn(name="pdce_conto_incremento_id")
	private SiacTPdceConto siacTPdceContoIncremento; 

	@Column(name="pdce_conto_incremento_code")
	private String pdceContoIncrementoCode;

	@Column(name="pdce_conto_incremento_desc")
	private String pdceContoIncrementoDesc;

	
	
    @ManyToOne
	@JoinColumn(name="pdce_conto_decremento_id")
	private SiacTPdceConto siacTPdceContoDecremento; 

	@Column(name="pdce_conto_decremento_code")
	private String pdceContoDecrementoCode;

	@Column(name="pdce_conto_decremento_desc")
	private String pdceContoDecrementoDesc;

	
    @ManyToOne
	@JoinColumn(name="pdce_conto_alienazione_id")
	private SiacTPdceConto siacTPdceContoAlienazione; 
    
    @Column(name="pdce_conto_alienazione_code")
	private String pdceContoAlienazioneCode;

	@Column(name="pdce_conto_alienazione_desc")
	private String pdceContoAlienazioneDesc;
	
	
    @ManyToOne
	@JoinColumn(name="pdce_conto_donazione_id")
	private SiacTPdceConto siacTPdceContoDonazione; 
	
    @Column(name="pdce_conto_donazione_code")
	private String pdceContoDonazioneCode;

	@Column(name="pdce_conto_donazione_desc")
	private String pdceContoDonazioneDesc;
	
	@OneToMany(mappedBy="siacDCespitiBeneTipo", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRCespitiBeneTipoContoPatrCat> siacRCespitiBeneTipoContoPatrCats;
		
	/**
	 * @return the cesBeneTipoId
	 */
	public Integer getCesBeneTipoId() {
		return cesBeneTipoId;
	}

	/**
	 * @param cesBeneTipoId the cesBeneTipoId to set
	 */
	public void setCesBeneTipoId(Integer cesBeneTipoId) {
		this.cesBeneTipoId = cesBeneTipoId;
	}

	/**
	 * @return the cesBeneTipoCode
	 */
	public String getCesBeneTipoCode() {
		return cesBeneTipoCode;
	}

	/**
	 * @param cesBeneTipoCode the cesBeneTipoCode to set
	 */
	public void setCesBeneTipoCode(String cesBeneTipoCode) {
		this.cesBeneTipoCode = cesBeneTipoCode;
	}

	/**
	 * @return the cesBeneTipoDesc
	 */
	public String getCesBeneTipoDesc() {
		return cesBeneTipoDesc;
	}

	/**
	 * @param cesBeneTipoDesc the cesBeneTipoDesc to set
	 */
	public void setCesBeneTipoDesc(String cesBeneTipoDesc) {
		this.cesBeneTipoDesc = cesBeneTipoDesc;
	}

	/**
	 * @return the testoScritturaAmmortamento
	 */
	public String getTestoScritturaAmmortamento() {
		return testoScritturaAmmortamento;
	}

	/**
	 * @param testoScritturaAmmortamento the testoScritturaAmmortamento to set
	 */
	public void setTestoScritturaAmmortamento(String testoScritturaAmmortamento) {
		this.testoScritturaAmmortamento = testoScritturaAmmortamento;
	}

//	/**
//	 * @return the SiacDCespitiCategoriaCalcoloTipo
//	 */
//	public SiacDCespitiCategoriaCalcoloTipo getSiacDCespitiCategoriaCalcoloTipo() {
//		return siacDCespitiCategoriaCalcoloTipo;
//	}
//
//	/**
//	 * @param SiacDCespitiCategoriaCalcoloTipo the SiacDCespitiCategoriaCalcoloTipo to set
//	 */
//	public void setSiacDCespitiCategoriaCalcoloTipo(SiacDCespitiCategoriaCalcoloTipo siacDCespitiCategoriaCalcoloTipo) {
//		this.siacDCespitiCategoriaCalcoloTipo = siacDCespitiCategoriaCalcoloTipo;
//	}

	/**
	 * @return the siacDEventoAmmortamento
	 */
	public SiacDEvento getSiacDEventoAmmortamento() {
		return siacDEventoAmmortamento;
	}

	/**
	 * @param siacDEventoAmmortamento the siacDEventoAmmortamento to set
	 */
	public void setSiacDEventoAmmortamento(SiacDEvento siacDEventoAmmortamento) {
		this.siacDEventoAmmortamento = siacDEventoAmmortamento;
	}

	/**
	 * @return the eventoAmmortamentoCode
	 */
	public String getEventoAmmortamentoCode() {
		return eventoAmmortamentoCode;
	}

	/**
	 * @param eventoAmmortamentoCode the eventoAmmortamentoCode to set
	 */
	public void setEventoAmmortamentoCode(String eventoAmmortamentoCode) {
		this.eventoAmmortamentoCode = eventoAmmortamentoCode;
	}

	/**
	 * @return the eventoAmmortamentoDesc
	 */
	public String getEventoAmmortamentoDesc() {
		return eventoAmmortamentoDesc;
	}

	/**
	 * @param eventoAmmortamentoDesc the eventoAmmortamentoDesc to set
	 */
	public void setEventoAmmortamentoDesc(String eventoAmmortamentoDesc) {
		this.eventoAmmortamentoDesc = eventoAmmortamentoDesc;
	}

	/**
	 * @return the siacTCausaleEpAmmortamento
	 */
	public SiacTCausaleEp getSiacTCausaleEpAmmortamento() {
		return siacTCausaleEpAmmortamento;
	}

	/**
	 * @param siacTCausaleEpAmmortamento the siacTCausaleEpAmmortamento to set
	 */
	public void setSiacTCausaleEpAmmortamento(SiacTCausaleEp siacTCausaleEpAmmortamento) {
		this.siacTCausaleEpAmmortamento = siacTCausaleEpAmmortamento;
	}

	/**
	 * @return the causaleEpAmmortamentoCode
	 */
	public String getCausaleEpAmmortamentoCode() {
		return causaleEpAmmortamentoCode;
	}

	/**
	 * @param causaleEpAmmortamentoCode the causaleEpAmmortamentoCode to set
	 */
	public void setCausaleEpAmmortamentoCode(String causaleEpAmmortamentoCode) {
		this.causaleEpAmmortamentoCode = causaleEpAmmortamentoCode;
	}

	/**
	 * @return the causaleEpAmmortamentoDesc
	 */
	public String getCausaleEpAmmortamentoDesc() {
		return causaleEpAmmortamentoDesc;
	}

	/**
	 * @param causaleEpAmmortamentoDesc the causaleEpAmmortamentoDesc to set
	 */
	public void setCausaleEpAmmortamentoDesc(String causaleEpAmmortamentoDesc) {
		this.causaleEpAmmortamentoDesc = causaleEpAmmortamentoDesc;
	}

	/**
	 * @return the siacDEventoIncr
	 */
	public SiacDEvento getSiacDEventoIncr() {
		return siacDEventoIncr;
	}

	/**
	 * @param siacDEventoIncr the siacDEventoIncr to set
	 */
	public void setSiacDEventoIncr(SiacDEvento siacDEventoIncr) {
		this.siacDEventoIncr = siacDEventoIncr;
	}

	/**
	 * @return the eventoIncrementoCode
	 */
	public String getEventoIncrementoCode() {
		return eventoIncrementoCode;
	}

	/**
	 * @param eventoIncrementoCode the eventoIncrementoCode to set
	 */
	public void setEventoIncrementoCode(String eventoIncrementoCode) {
		this.eventoIncrementoCode = eventoIncrementoCode;
	}

	/**
	 * @return the eventoIncrementoDesc
	 */
	public String getEventoIncrementoDesc() {
		return eventoIncrementoDesc;
	}

	/**
	 * @param eventoIncrementoDesc the eventoIncrementoDesc to set
	 */
	public void setEventoIncrementoDesc(String eventoIncrementoDesc) {
		this.eventoIncrementoDesc = eventoIncrementoDesc;
	}

	/**
	 * @return the siacTCausaleEpIncr
	 */
	public SiacTCausaleEp getSiacTCausaleEpIncr() {
		return siacTCausaleEpIncr;
	}

	/**
	 * @param siacTCausaleEpIncr the siacTCausaleEpIncr to set
	 */
	public void setSiacTCausaleEpIncr(SiacTCausaleEp siacTCausaleEpIncr) {
		this.siacTCausaleEpIncr = siacTCausaleEpIncr;
	}

	/**
	 * @return the causaleEpIncrementoCode
	 */
	public String getCausaleEpIncrementoCode() {
		return causaleEpIncrementoCode;
	}

	/**
	 * @param causaleEpIncrementoCode the causaleEpIncrementoCode to set
	 */
	public void setCausaleEpIncrementoCode(String causaleEpIncrementoCode) {
		this.causaleEpIncrementoCode = causaleEpIncrementoCode;
	}

	/**
	 * @return the causaleEpIncrementoDesc
	 */
	public String getCausaleEpIncrementoDesc() {
		return causaleEpIncrementoDesc;
	}

	/**
	 * @param causaleEpIncrementoDesc the causaleEpIncrementoDesc to set
	 */
	public void setCausaleEpIncrementoDesc(String causaleEpIncrementoDesc) {
		this.causaleEpIncrementoDesc = causaleEpIncrementoDesc;
	}

	/**
	 * @return the siacDEventoDec
	 */
	public SiacDEvento getSiacDEventoDec() {
		return siacDEventoDec;
	}

	/**
	 * @param siacDEventoDec the siacDEventoDec to set
	 */
	public void setSiacDEventoDec(SiacDEvento siacDEventoDec) {
		this.siacDEventoDec = siacDEventoDec;
	}

	/**
	 * @return the eventoDecrementoCode
	 */
	public String getEventoDecrementoCode() {
		return eventoDecrementoCode;
	}

	/**
	 * @param eventoDecrementoCode the eventoDecrementoCode to set
	 */
	public void setEventoDecrementoCode(String eventoDecrementoCode) {
		this.eventoDecrementoCode = eventoDecrementoCode;
	}

	/**
	 * @return the eventoDecrementoDesc
	 */
	public String getEventoDecrementoDesc() {
		return eventoDecrementoDesc;
	}

	/**
	 * @param eventoDecrementoDesc the eventoDecrementoDesc to set
	 */
	public void setEventoDecrementoDesc(String eventoDecrementoDesc) {
		this.eventoDecrementoDesc = eventoDecrementoDesc;
	}

	/**
	 * @return the siacTCausaleEpDecremento
	 */
	public SiacTCausaleEp getSiacTCausaleEpDecremento() {
		return siacTCausaleEpDecremento;
	}

	/**
	 * @param siacTCausaleEpDecremento the siacTCausaleEpDecremento to set
	 */
	public void setSiacTCausaleEpDecremento(SiacTCausaleEp siacTCausaleEpDecremento) {
		this.siacTCausaleEpDecremento = siacTCausaleEpDecremento;
	}

	/**
	 * @return the causaleEpDecrementoCode
	 */
	public String getCausaleEpDecrementoCode() {
		return causaleEpDecrementoCode;
	}

	/**
	 * @param causaleEpDecrementoCode the causaleEpDecrementoCode to set
	 */
	public void setCausaleEpDecrementoCode(String causaleEpDecrementoCode) {
		this.causaleEpDecrementoCode = causaleEpDecrementoCode;
	}

	/**
	 * @return the causaleEpDecrementoDesc
	 */
	public String getCausaleEpDecrementoDesc() {
		return causaleEpDecrementoDesc;
	}

	/**
	 * @param causaleEpDecrementoDesc the causaleEpDecrementoDesc to set
	 */
	public void setCausaleEpDecrementoDesc(String causaleEpDecrementoDesc) {
		this.causaleEpDecrementoDesc = causaleEpDecrementoDesc;
	}

	/**
	 * @return the siacTPdceContoAmmortamento
	 */
	public SiacTPdceConto getSiacTPdceContoAmmortamento() {
		return siacTPdceContoAmmortamento;
	}

	/**
	 * @param siacTPdceContoAmmortamento the siacTPdceContoAmmortamento to set
	 */
	public void setSiacTPdceContoAmmortamento(SiacTPdceConto siacTPdceContoAmmortamento) {
		this.siacTPdceContoAmmortamento = siacTPdceContoAmmortamento;
	}

	/**
	 * @return the pdceContoAmmortamentoCode
	 */
	public String getPdceContoAmmortamentoCode() {
		return pdceContoAmmortamentoCode;
	}

	/**
	 * @param pdceContoAmmortamentoCode the pdceContoAmmortamentoCode to set
	 */
	public void setPdceContoAmmortamentoCode(String pdceContoAmmortamentoCode) {
		this.pdceContoAmmortamentoCode = pdceContoAmmortamentoCode;
	}

	/**
	 * @return the pdceContoAmmortamentoDesc
	 */
	public String getPdceContoAmmortamentoDesc() {
		return pdceContoAmmortamentoDesc;
	}

	/**
	 * @param pdceContoAmmortamentoDesc the pdceContoAmmortamentoDesc to set
	 */
	public void setPdceContoAmmortamentoDesc(String pdceContoAmmortamentoDesc) {
		this.pdceContoAmmortamentoDesc = pdceContoAmmortamentoDesc;
	}

	/**
	 * @return the siacTPdceContoFondoAmmortamento
	 */
	public SiacTPdceConto getSiacTPdceContoFondoAmmortamento() {
		return siacTPdceContoFondoAmmortamento;
	}

	/**
	 * @param siacTPdceContoFondoAmmortamento the siacTPdceContoFondoAmmortamento to set
	 */
	public void setSiacTPdceContoFondoAmmortamento(SiacTPdceConto siacTPdceContoFondoAmmortamento) {
		this.siacTPdceContoFondoAmmortamento = siacTPdceContoFondoAmmortamento;
	}

	/**
	 * @return the pdceContoFondoAmmortamentoCode
	 */
	public String getPdceContoFondoAmmortamentoCode() {
		return pdceContoFondoAmmortamentoCode;
	}

	/**
	 * @param pdceContoFondoAmmortamentoCode the pdceContoFondoAmmortamentoCode to set
	 */
	public void setPdceContoFondoAmmortamentoCode(String pdceContoFondoAmmortamentoCode) {
		this.pdceContoFondoAmmortamentoCode = pdceContoFondoAmmortamentoCode;
	}

	/**
	 * @return the pdceContoFondoAmmortamentoDesc
	 */
	public String getPdceContoFondoAmmortamentoDesc() {
		return pdceContoFondoAmmortamentoDesc;
	}

	/**
	 * @param pdceContoFondoAmmortamentoDesc the pdceContoFondoAmmortamentoDesc to set
	 */
	public void setPdceContoFondoAmmortamentoDesc(String pdceContoFondoAmmortamentoDesc) {
		this.pdceContoFondoAmmortamentoDesc = pdceContoFondoAmmortamentoDesc;
	}

	/**
	 * @return the siacTPdceContoPlusvalenza
	 */
	public SiacTPdceConto getSiacTPdceContoPlusvalenza() {
		return siacTPdceContoPlusvalenza;
	}

	/**
	 * @param siacTPdceContoPlusvalenza the siacTPdceContoPlusvalenza to set
	 */
	public void setSiacTPdceContoPlusvalenza(SiacTPdceConto siacTPdceContoPlusvalenza) {
		this.siacTPdceContoPlusvalenza = siacTPdceContoPlusvalenza;
	}

	/**
	 * @return the pdceContoPlusvalenzaCode
	 */
	public String getPdceContoPlusvalenzaCode() {
		return pdceContoPlusvalenzaCode;
	}

	/**
	 * @param pdceContoPlusvalenzaCode the pdceContoPlusvalenzaCode to set
	 */
	public void setPdceContoPlusvalenzaCode(String pdceContoPlusvalenzaCode) {
		this.pdceContoPlusvalenzaCode = pdceContoPlusvalenzaCode;
	}

	/**
	 * @return the pdceContoPlusvalenzaDesc
	 */
	public String getPdceContoPlusvalenzaDesc() {
		return pdceContoPlusvalenzaDesc;
	}

	/**
	 * @param pdceContoPlusvalenzaDesc the pdceContoPlusvalenzaDesc to set
	 */
	public void setPdceContoPlusvalenzaDesc(String pdceContoPlusvalenzaDesc) {
		this.pdceContoPlusvalenzaDesc = pdceContoPlusvalenzaDesc;
	}

	/**
	 * @return the siacTPdceContoMinusvalenza
	 */
	public SiacTPdceConto getSiacTPdceContoMinusvalenza() {
		return siacTPdceContoMinusvalenza;
	}

	/**
	 * @param siacTPdceContoMinusvalenza the siacTPdceContoMinusvalenza to set
	 */
	public void setSiacTPdceContoMinusvalenza(SiacTPdceConto siacTPdceContoMinusvalenza) {
		this.siacTPdceContoMinusvalenza = siacTPdceContoMinusvalenza;
	}

	/**
	 * @return the pdceContoMinusvalenzaCode
	 */
	public String getPdceContoMinusvalenzaCode() {
		return pdceContoMinusvalenzaCode;
	}

	/**
	 * @param pdceContoMinusvalenzaCode the pdceContoMinusvalenzaCode to set
	 */
	public void setPdceContoMinusvalenzaCode(String pdceContoMinusvalenzaCode) {
		this.pdceContoMinusvalenzaCode = pdceContoMinusvalenzaCode;
	}

	/**
	 * @return the pdceContoMinusvalenzaDesc
	 */
	public String getPdceContoMinusvalenzaDesc() {
		return pdceContoMinusvalenzaDesc;
	}

	/**
	 * @param pdceContoMinusvalenzaDesc the pdceContoMinusvalenzaDesc to set
	 */
	public void setPdceContoMinusvalenzaDesc(String pdceContoMinusvalenzaDesc) {
		this.pdceContoMinusvalenzaDesc = pdceContoMinusvalenzaDesc;
	}

	/**
	 * @return the siacTPdceContoIncremento
	 */
	public SiacTPdceConto getSiacTPdceContoIncremento() {
		return siacTPdceContoIncremento;
	}

	/**
	 * @param siacTPdceContoIncremento the siacTPdceContoIncremento to set
	 */
	public void setSiacTPdceContoIncremento(SiacTPdceConto siacTPdceContoIncremento) {
		this.siacTPdceContoIncremento = siacTPdceContoIncremento;
	}

	/**
	 * @return the pdceContoIncrementoCode
	 */
	public String getPdceContoIncrementoCode() {
		return pdceContoIncrementoCode;
	}

	/**
	 * @param pdceContoIncrementoCode the pdceContoIncrementoCode to set
	 */
	public void setPdceContoIncrementoCode(String pdceContoIncrementoCode) {
		this.pdceContoIncrementoCode = pdceContoIncrementoCode;
	}

	/**
	 * @return the pdceContoIncrementoDesc
	 */
	public String getPdceContoIncrementoDesc() {
		return pdceContoIncrementoDesc;
	}

	/**
	 * @param pdceContoIncrementoDesc the pdceContoIncrementoDesc to set
	 */
	public void setPdceContoIncrementoDesc(String pdceContoIncrementoDesc) {
		this.pdceContoIncrementoDesc = pdceContoIncrementoDesc;
	}

	/**
	 * @return the siacTPdceContoDecremento
	 */
	public SiacTPdceConto getSiacTPdceContoDecremento() {
		return siacTPdceContoDecremento;
	}

	/**
	 * @param siacTPdceContoDecremento the siacTPdceContoDecremento to set
	 */
	public void setSiacTPdceContoDecremento(SiacTPdceConto siacTPdceContoDecremento) {
		this.siacTPdceContoDecremento = siacTPdceContoDecremento;
	}

	/**
	 * @return the pdceContoDecrementoCode
	 */
	public String getPdceContoDecrementoCode() {
		return pdceContoDecrementoCode;
	}

	/**
	 * @param pdceContoDecrementoCode the pdceContoDecrementoCode to set
	 */
	public void setPdceContoDecrementoCode(String pdceContoDecrementoCode) {
		this.pdceContoDecrementoCode = pdceContoDecrementoCode;
	}

	/**
	 * @return the pdceContoDecrementooDesc
	 */
	public String getPdceContoDecrementoDesc() {
		return pdceContoDecrementoDesc;
	}

	/**
	 * @param pdceContoDecrementooDesc the pdceContoDecrementooDesc to set
	 */
	public void setPdceContoDecrementoDesc(String pdceContoDecrementooDesc) {
		this.pdceContoDecrementoDesc = pdceContoDecrementooDesc;
	}

	/**
	 * @return the siacTPdceContoAlienazione
	 */
	public SiacTPdceConto getSiacTPdceContoAlienazione() {
		return siacTPdceContoAlienazione;
	}

	/**
	 * @param siacTPdceContoAlienazione the siacTPdceContoAlienazione to set
	 */
	public void setSiacTPdceContoAlienazione(SiacTPdceConto siacTPdceContoAlienazione) {
		this.siacTPdceContoAlienazione = siacTPdceContoAlienazione;
	}

	/**
	 * @return the pdceContoAlienazioneoCode
	 */
	public String getPdceContoAlienazioneCode() {
		return pdceContoAlienazioneCode;
	}

	/**
	 * @param pdceContoAlienazioneoCode the pdceContoAlienazioneoCode to set
	 */
	public void setPdceContoAlienazioneCode(String pdceContoAlienazioneoCode) {
		this.pdceContoAlienazioneCode = pdceContoAlienazioneoCode;
	}

	/**
	 * @return the pdceContoAlienazioneDesc
	 */
	public String getPdceContoAlienazioneDesc() {
		return pdceContoAlienazioneDesc;
	}

	/**
	 * @param pdceContoAlienazioneDesc the pdceContoAlienazioneDesc to set
	 */
	public void setPdceContoAlienazioneDesc(String pdceContoAlienazioneDesc) {
		this.pdceContoAlienazioneDesc = pdceContoAlienazioneDesc;
	}

	/**
	 * @return the siacTPdceContoDonazione
	 */
	public SiacTPdceConto getSiacTPdceContoDonazione() {
		return siacTPdceContoDonazione;
	}

	/**
	 * @param siacTPdceContoDonazione the siacTPdceContoDonazione to set
	 */
	public void setSiacTPdceContoDonazione(SiacTPdceConto siacTPdceContoDonazione) {
		this.siacTPdceContoDonazione = siacTPdceContoDonazione;
	}

	/**
	 * @return the pdceContoDonazioneCode
	 */
	public String getPdceContoDonazioneCode() {
		return pdceContoDonazioneCode;
	}

	/**
	 * @param pdceContoDonazioneCode the pdceContoDonazioneCode to set
	 */
	public void setPdceContoDonazioneCode(String pdceContoDonazioneCode) {
		this.pdceContoDonazioneCode = pdceContoDonazioneCode;
	}

	/**
	 * @return the pdceContoDonazioneDesc
	 */
	public String getPdceContoDonazioneDesc() {
		return pdceContoDonazioneDesc;
	}

	/**
	 * @param pdceContoDonazioneDesc the pdceContoDonazioneDesc to set
	 */
	public void setPdceContoDonazioneDesc(String pdceContoDonazioneDesc) {
		this.pdceContoDonazioneDesc = pdceContoDonazioneDesc;
	}

	@Override
	public Integer getUid() {
		return this.cesBeneTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cesBeneTipoId = uid;
	}

	/**
	 * @return the siacRCespitiBeneTipoContoPatrCats
	 */
	public List<SiacRCespitiBeneTipoContoPatrCat> getSiacRCespitiBeneTipoContoPatrCats() {
		return siacRCespitiBeneTipoContoPatrCats;
	}

	/**
	 * @param siacRCespitiBeneTipoContoPatrCats the siacRCespitiBeneTipoContoPatrCats to set
	 */
	public void setSiacRCespitiBeneTipoContoPatrCats(List<SiacRCespitiBeneTipoContoPatrCat> siacRCespitiBeneTipoContoPatrCats) {
		this.siacRCespitiBeneTipoContoPatrCats = siacRCespitiBeneTipoContoPatrCats;
	}
	
	/**
	 * Adds the siac r variazione attr.
	 *
	 * @param siacRCategoriaCespitiAliquotaCalcoloTipo the siac r variazione attr
	 * @return the siac r variazione attr
	 */
	public SiacRCespitiBeneTipoContoPatrCat addSiacRCategoriaCespitiAliquotaCalcoloTipo(SiacRCespitiBeneTipoContoPatrCat siacRCategoriaCespitiAliquotaCalcoloTipo) {
		getSiacRCespitiBeneTipoContoPatrCats().add(siacRCategoriaCespitiAliquotaCalcoloTipo);
		siacRCategoriaCespitiAliquotaCalcoloTipo.setSiacDCespitiBeneTipo(this);
		return siacRCategoriaCespitiAliquotaCalcoloTipo;
	}

	/**
	 * Removes the SiacRCategoriaCespitiAliquotaCalcoloTipo
	 *
	 * @param siacRCategoriaCespitiAliquotaCalcoloTipo the siac r variazione attr
	 * @return the siac r variazione attr
	 */
	public SiacRCespitiBeneTipoContoPatrCat removeSiacRCespitiBeneTipoContoPatrCat(SiacRCespitiBeneTipoContoPatrCat siacRCategoriaCespitiAliquotaCalcoloTipo) {
		getSiacRCespitiBeneTipoContoPatrCats().remove(siacRCategoriaCespitiAliquotaCalcoloTipo);
		siacRCategoriaCespitiAliquotaCalcoloTipo.setSiacDCespitiBeneTipo(null);
		return siacRCategoriaCespitiAliquotaCalcoloTipo;
	}
	
	

}