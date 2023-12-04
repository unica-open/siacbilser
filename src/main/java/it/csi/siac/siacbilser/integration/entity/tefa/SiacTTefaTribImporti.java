/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.tefa;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacbilser.integration.entity.SiacTEnteBase;
import it.csi.siac.siacbilser.integration.entity.SiacTFile;

@Entity
@Table(name="siac_t_tefa_trib_importi")
public class SiacTTefaTribImporti extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_TEFATRIBIMPORTI_TEFATRIBID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_TEFA_TRIB_IMPORTI_TEFA_TRIB_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_TEFATRIBIMPORTI_TEFATRIBID_GENERATOR") 
	@Column(name="tefa_trib_id")
	private Integer tefaTribId;
	
	@Column(name = "tefa_trib_tipo_record")
	private String tipoRecord;
	
	@Column(name = "tefa_trib_data_ripart")
	private String dataRipart; 

	@Column(name = "tefa_trib_progr_ripart")
	private String progrRipart;
	
	@Column(name = "tefa_trib_provincia_code")
	private String provinciaCode;
	
	@Column(name = "tefa_trib_ente_code")
	private String enteCode;
											
	@Column(name = "tefa_trib_data_bonifico")
	private String dataBonifico;
	
	@Column(name = "tefa_trib_progr_trasm")
	private String progrTrasm;
	
	@Column(name = "tefa_trib_progr_delega")
	private String progrDelega;
	
	@Column(name = "tefa_trib_progr_modello")
	private String progrModello;
	
	@Column(name = "tefa_trib_tipo_modello")
	private String tipoModello;
	
	@Column(name = "tefa_trib_comune_code")
	private String comuneCode;
	
	@Column(name = "tefa_trib_tributo_code")
	private String tributoCode;
	
	@Column(name = "tefa_trib_valuta")
	private String valuta;
	
	@Column(name = "tefa_trib_importo_versato_deb")
	private BigDecimal importoVersatoDeb;
	
	@Column(name = "tefa_trib_importo_compensato_cred")
	private BigDecimal importoCompensatoCred;
	
	@Column(name = "tefa_trib_numero_immobili")
	private String numeroImmobili;
	
	@Column(name = "tefa_trib_rateazione")
	private String rateazione;

	@Column(name = "tefa_trib_anno_rif")
	private String annoRif;
	
	@Column(name = "tefa_trib_anno_rif_str")
	private String annoRifStr;
	
	@Column(name = "tefa_nome_file")
	private String nomeFile;
	
	@ManyToOne
	@JoinColumn(name="tefa_trib_file_id", referencedColumnName="file_id")
	private SiacTFile file;

	public SiacTTefaTribImporti() {
	}

	@Override
	public Integer getUid() {
		return tefaTribId;
	}

	@Override
	public void setUid(Integer uid) {
		this.tefaTribId = uid;
	}

	public Integer getTefaTribId() {
		return tefaTribId;
	}

	public void setTefaTribId(Integer tefaTribId) {
		this.tefaTribId = tefaTribId;
	}

	public String getTipoRecord() {
		return tipoRecord;
	}

	public void setTipoRecord(String tipoRecord) {
		this.tipoRecord = tipoRecord;
	}

	public String getDataRipart() {
		return dataRipart;
	}

	public void setDataRipart(String dataRipart) {
		this.dataRipart = dataRipart;
	}

	public String getProgrRipart() {
		return progrRipart;
	}

	public void setProgrRipart(String progrRipart) {
		this.progrRipart = progrRipart;
	}

	public String getProvinciaCode() {
		return provinciaCode;
	}

	public void setProvinciaCode(String provinciaCode) {
		this.provinciaCode = provinciaCode;
	}

	public String getEnteCode() {
		return enteCode;
	}

	public void setEnteCode(String enteCode) {
		this.enteCode = enteCode;
	}

	public String getDataBonifico() {
		return dataBonifico;
	}

	public void setDataBonifico(String dataBonifico) {
		this.dataBonifico = dataBonifico;
	}

	public String getProgrTrasm() {
		return progrTrasm;
	}

	public void setProgrTrasm(String progrTrasm) {
		this.progrTrasm = progrTrasm;
	}

	public String getProgrDelega() {
		return progrDelega;
	}

	public void setProgrDelega(String progrDelega) {
		this.progrDelega = progrDelega;
	}

	public String getProgrModello() {
		return progrModello;
	}

	public void setProgrModello(String progrModello) {
		this.progrModello = progrModello;
	}

	public String getTipoModello() {
		return tipoModello;
	}

	public void setTipoModello(String tipoModello) {
		this.tipoModello = tipoModello;
	}

	public String getComuneCode() {
		return comuneCode;
	}

	public void setComuneCode(String comuneCode) {
		this.comuneCode = comuneCode;
	}

	public String getTributoCode() {
		return tributoCode;
	}

	public void setTributoCode(String tributoCode) {
		this.tributoCode = tributoCode;
	}



	public BigDecimal getImportoVersatoDeb() {
		return importoVersatoDeb;
	}

	public void setImportoVersatoDeb(BigDecimal importoVersatoDeb) {
		this.importoVersatoDeb = importoVersatoDeb;
	}

	public BigDecimal getImportoCompensatoCred() {
		return importoCompensatoCred;
	}

	public void setImportoCompensatoCred(BigDecimal importoCompensatoCred) {
		this.importoCompensatoCred = importoCompensatoCred;
	}

	public String getNumeroImmobili() {
		return numeroImmobili;
	}

	public void setNumeroImmobili(String numeroImmobili) {
		this.numeroImmobili = numeroImmobili;
	}

	public String getRateazione() {
		return rateazione;
	}

	public void setRateazione(String rateazione) {
		this.rateazione = rateazione;
	}

	public String getAnnoRif() {
		return annoRif;
	}

	public void setAnnoRif(String annoRif) {
		this.annoRif = annoRif;
	}

	public String getNomeFile() {
		return nomeFile;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}

	public SiacTFile getFile() {
		return file;
	}

	public void setFile(SiacTFile file) {
		this.file = file;
	}

	public String getValuta() {
		return valuta;
	}

	public void setValuta(String valuta) {
		this.valuta = valuta;
	}

	public String getAnnoRifStr() {
		return annoRifStr;
	}

	public void setAnnoRifStr(String annoRifStr) {
		this.annoRifStr = annoRifStr;
	}

}