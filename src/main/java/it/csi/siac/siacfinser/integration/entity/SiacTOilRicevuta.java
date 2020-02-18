/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_t_oil_ricevuta database table.
 * 
 */
@Entity
@Table(name="siac_t_oil_ricevuta")
public class SiacTOilRicevuta extends SiacTEnteBase {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_OIL_RICEVUTA_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_oil_ricevuta_oil_ricevuta_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_OIL_RICEVUTA_ID_GENERATOR")
	@Column(name="oil_ricevuta_id")
	private Integer oilRicevutaId;


	@Column(name="flusso_elab_mif_id")
	private Integer flussoElabMifId;

	@Column(name="oil_ord_anno_bil")
	private Integer oilOrdAnnoBil;

	@Column(name="oil_ord_bil_id")
	private Integer oilOrdBilId;

	@Column(name="oil_ord_data_annullamento")
	private Timestamp oilOrdDataAnnullamento;

	@Column(name="oil_ord_data_emissione")
	private Timestamp oilOrdDataEmissione;

	@Column(name="oil_ord_data_firma")
	private Timestamp oilOrdDataFirma;

	@Column(name="oil_ord_data_quietanza")
	private Timestamp oilOrdDataQuietanza;

	@Column(name="oil_ord_importo")
	private BigDecimal oilOrdImporto;

	@Column(name="oil_ord_importo_quiet")
	private BigDecimal oilOrdImportoQuiet;

	@Column(name="oil_ord_importo_quiet_tot")
	private BigDecimal oilOrdImportoQuietTot;

	@Column(name="oil_ord_importo_storno")
	private BigDecimal oilOrdImportoStorno;

	@Column(name="oil_ord_nome_firma")
	private String oilOrdNomeFirma;

	@Column(name="oil_ord_numero")
	private Integer oilOrdNumero;

	@Column(name="oil_ord_trasm_oil_data")
	private Timestamp oilOrdTrasmOilData;

	@Column(name="oil_progr_dett_ricevuta_id")
	private Integer oilProgrDettRicevutaId;

	@Column(name="oil_progr_ricevuta_id")
	private Integer oilProgrRicevutaId;

	@Column(name="oil_ricevuta_anno")
	private Integer oilRicevutaAnno;

	@Column(name="oil_ricevuta_cro1")
	private String oilRicevutaCro1;

	@Column(name="oil_ricevuta_cro2")
	private String oilRicevutaCro2;

	@Column(name="oil_ricevuta_data")
	private Timestamp oilRicevutaData;

	@Column(name="oil_ricevuta_denominazione")
	private String oilRicevutaDenominazione;

	@Column(name="oil_ricevuta_errore_id")
	private Integer oilRicevutaErroreId;

	@Column(name="oil_ricevuta_importo")
	private BigDecimal oilRicevutaImporto;

	@Column(name="oil_ricevuta_note")
	private String oilRicevutaNote;

	@Column(name="oil_ricevuta_note_tes")
	private String oilRicevutaNoteTes;

	@Column(name="oil_ricevuta_numero")
	private Integer oilRicevutaNumero;

	@Column(name="oil_ricevuta_tipo")
	private String oilRicevutaTipo;



	//bi-directional many-to-one association to SiacROrdinativoFirma
	@OneToMany(mappedBy="siacTOilRicevuta")
	private List<SiacROrdinativoFirma> siacROrdinativoFirmas;

	//bi-directional many-to-one association to SiacROrdinativoQuietanza
	@OneToMany(mappedBy="siacTOilRicevuta")
	private List<SiacROrdinativoQuietanza> siacROrdinativoQuietanzas;

	//bi-directional many-to-one association to SiacROrdinativoStorno
//	@OneToMany(mappedBy="siacTOilRicevuta")
//	private List<SiacROrdinativoStorno> siacROrdinativoStornos;

	//bi-directional many-to-one association to SiacRProvCassaOilRicevuta
//	@OneToMany(mappedBy="siacTOilRicevuta")
//	private List<SiacRProvCassaOilRicevuta> siacRProvCassaOilRicevutas;

	//bi-directional many-to-one association to SiacDOilRicevutaTipo
	@ManyToOne
	@JoinColumn(name="oil_ricevuta_tipo_id")
	private SiacDOilRicevutaTipo siacDOilRicevutaTipo;

	//bi-directional many-to-one association to SiacTOrdinativo
	@ManyToOne
	@JoinColumn(name="oil_ord_id")
	private SiacTOrdinativoFin siacTOrdinativo;

	public SiacTOilRicevuta() {
	}

	public Integer getOilRicevutaId() {
		return this.oilRicevutaId;
	}

	public void setOilRicevutaId(Integer oilRicevutaId) {
		this.oilRicevutaId = oilRicevutaId;
	}

	public Integer getFlussoElabMifId() {
		return this.flussoElabMifId;
	}

	public void setFlussoElabMifId(Integer flussoElabMifId) {
		this.flussoElabMifId = flussoElabMifId;
	}


	public Integer getOilOrdAnnoBil() {
		return this.oilOrdAnnoBil;
	}

	public void setOilOrdAnnoBil(Integer oilOrdAnnoBil) {
		this.oilOrdAnnoBil = oilOrdAnnoBil;
	}

	public Integer getOilOrdBilId() {
		return this.oilOrdBilId;
	}

	public void setOilOrdBilId(Integer oilOrdBilId) {
		this.oilOrdBilId = oilOrdBilId;
	}

	public Timestamp getOilOrdDataAnnullamento() {
		return this.oilOrdDataAnnullamento;
	}

	public void setOilOrdDataAnnullamento(Timestamp oilOrdDataAnnullamento) {
		this.oilOrdDataAnnullamento = oilOrdDataAnnullamento;
	}

	public Timestamp getOilOrdDataEmissione() {
		return this.oilOrdDataEmissione;
	}

	public void setOilOrdDataEmissione(Timestamp oilOrdDataEmissione) {
		this.oilOrdDataEmissione = oilOrdDataEmissione;
	}

	public Timestamp getOilOrdDataFirma() {
		return this.oilOrdDataFirma;
	}

	public void setOilOrdDataFirma(Timestamp oilOrdDataFirma) {
		this.oilOrdDataFirma = oilOrdDataFirma;
	}

	public Timestamp getOilOrdDataQuietanza() {
		return this.oilOrdDataQuietanza;
	}

	public void setOilOrdDataQuietanza(Timestamp oilOrdDataQuietanza) {
		this.oilOrdDataQuietanza = oilOrdDataQuietanza;
	}

	public BigDecimal getOilOrdImporto() {
		return this.oilOrdImporto;
	}

	public void setOilOrdImporto(BigDecimal oilOrdImporto) {
		this.oilOrdImporto = oilOrdImporto;
	}

	public BigDecimal getOilOrdImportoQuiet() {
		return this.oilOrdImportoQuiet;
	}

	public void setOilOrdImportoQuiet(BigDecimal oilOrdImportoQuiet) {
		this.oilOrdImportoQuiet = oilOrdImportoQuiet;
	}

	public BigDecimal getOilOrdImportoQuietTot() {
		return this.oilOrdImportoQuietTot;
	}

	public void setOilOrdImportoQuietTot(BigDecimal oilOrdImportoQuietTot) {
		this.oilOrdImportoQuietTot = oilOrdImportoQuietTot;
	}

	public BigDecimal getOilOrdImportoStorno() {
		return this.oilOrdImportoStorno;
	}

	public void setOilOrdImportoStorno(BigDecimal oilOrdImportoStorno) {
		this.oilOrdImportoStorno = oilOrdImportoStorno;
	}

	public String getOilOrdNomeFirma() {
		return this.oilOrdNomeFirma;
	}

	public void setOilOrdNomeFirma(String oilOrdNomeFirma) {
		this.oilOrdNomeFirma = oilOrdNomeFirma;
	}

	public Integer getOilOrdNumero() {
		return this.oilOrdNumero;
	}

	public void setOilOrdNumero(Integer oilOrdNumero) {
		this.oilOrdNumero = oilOrdNumero;
	}

	public Timestamp getOilOrdTrasmOilData() {
		return this.oilOrdTrasmOilData;
	}

	public void setOilOrdTrasmOilData(Timestamp oilOrdTrasmOilData) {
		this.oilOrdTrasmOilData = oilOrdTrasmOilData;
	}

	public Integer getOilProgrDettRicevutaId() {
		return this.oilProgrDettRicevutaId;
	}

	public void setOilProgrDettRicevutaId(Integer oilProgrDettRicevutaId) {
		this.oilProgrDettRicevutaId = oilProgrDettRicevutaId;
	}

	public Integer getOilProgrRicevutaId() {
		return this.oilProgrRicevutaId;
	}

	public void setOilProgrRicevutaId(Integer oilProgrRicevutaId) {
		this.oilProgrRicevutaId = oilProgrRicevutaId;
	}

	public Integer getOilRicevutaAnno() {
		return this.oilRicevutaAnno;
	}

	public void setOilRicevutaAnno(Integer oilRicevutaAnno) {
		this.oilRicevutaAnno = oilRicevutaAnno;
	}

	public String getOilRicevutaCro1() {
		return this.oilRicevutaCro1;
	}

	public void setOilRicevutaCro1(String oilRicevutaCro1) {
		this.oilRicevutaCro1 = oilRicevutaCro1;
	}

	public String getOilRicevutaCro2() {
		return this.oilRicevutaCro2;
	}

	public void setOilRicevutaCro2(String oilRicevutaCro2) {
		this.oilRicevutaCro2 = oilRicevutaCro2;
	}

	public Timestamp getOilRicevutaData() {
		return this.oilRicevutaData;
	}

	public void setOilRicevutaData(Timestamp oilRicevutaData) {
		this.oilRicevutaData = oilRicevutaData;
	}

	public String getOilRicevutaDenominazione() {
		return this.oilRicevutaDenominazione;
	}

	public void setOilRicevutaDenominazione(String oilRicevutaDenominazione) {
		this.oilRicevutaDenominazione = oilRicevutaDenominazione;
	}

	public Integer getOilRicevutaErroreId() {
		return this.oilRicevutaErroreId;
	}

	public void setOilRicevutaErroreId(Integer oilRicevutaErroreId) {
		this.oilRicevutaErroreId = oilRicevutaErroreId;
	}

	public BigDecimal getOilRicevutaImporto() {
		return this.oilRicevutaImporto;
	}

	public void setOilRicevutaImporto(BigDecimal oilRicevutaImporto) {
		this.oilRicevutaImporto = oilRicevutaImporto;
	}

	public String getOilRicevutaNote() {
		return this.oilRicevutaNote;
	}

	public void setOilRicevutaNote(String oilRicevutaNote) {
		this.oilRicevutaNote = oilRicevutaNote;
	}

	public String getOilRicevutaNoteTes() {
		return this.oilRicevutaNoteTes;
	}

	public void setOilRicevutaNoteTes(String oilRicevutaNoteTes) {
		this.oilRicevutaNoteTes = oilRicevutaNoteTes;
	}

	public Integer getOilRicevutaNumero() {
		return this.oilRicevutaNumero;
	}

	public void setOilRicevutaNumero(Integer oilRicevutaNumero) {
		this.oilRicevutaNumero = oilRicevutaNumero;
	}

	public String getOilRicevutaTipo() {
		return this.oilRicevutaTipo;
	}

	public void setOilRicevutaTipo(String oilRicevutaTipo) {
		this.oilRicevutaTipo = oilRicevutaTipo;
	}

	public List<SiacROrdinativoFirma> getSiacROrdinativoFirmas() {
		return this.siacROrdinativoFirmas;
	}

	public void setSiacROrdinativoFirmas(List<SiacROrdinativoFirma> siacROrdinativoFirmas) {
		this.siacROrdinativoFirmas = siacROrdinativoFirmas;
	}

	public List<SiacROrdinativoQuietanza> getSiacROrdinativoQuietanzas() {
		return this.siacROrdinativoQuietanzas;
	}

	public void setSiacROrdinativoQuietanzas(List<SiacROrdinativoQuietanza> siacROrdinativoQuietanzas) {
		this.siacROrdinativoQuietanzas = siacROrdinativoQuietanzas;
	}

//	public List<SiacROrdinativoStorno> getSiacROrdinativoStornos() {
//		return this.siacROrdinativoStornos;
//	}
//
//	public void setSiacROrdinativoStornos(List<SiacROrdinativoStorno> siacROrdinativoStornos) {
//		this.siacROrdinativoStornos = siacROrdinativoStornos;
//	}
//
//	public List<SiacRProvCassaOilRicevuta> getSiacRProvCassaOilRicevutas() {
//		return this.siacRProvCassaOilRicevutas;
//	}
//
//	public void setSiacRProvCassaOilRicevutas(List<SiacRProvCassaOilRicevuta> siacRProvCassaOilRicevutas) {
//		this.siacRProvCassaOilRicevutas = siacRProvCassaOilRicevutas;
//	}

	public SiacDOilRicevutaTipo getSiacDOilRicevutaTipo() {
		return this.siacDOilRicevutaTipo;
	}

	public void setSiacDOilRicevutaTipo(SiacDOilRicevutaTipo siacDOilRicevutaTipo) {
		this.siacDOilRicevutaTipo = siacDOilRicevutaTipo;
	}

	public SiacTOrdinativoFin getSiacTOrdinativo() {
		return this.siacTOrdinativo;
	}

	public void setSiacTOrdinativo(SiacTOrdinativoFin siacTOrdinativo) {
		this.siacTOrdinativo = siacTOrdinativo;
	}

	@Override
	public Integer getUid() {
		return this.oilRicevutaId;
	}

	@Override
	public void setUid(Integer uid) {
		this.oilRicevutaId = uid;
	}

}