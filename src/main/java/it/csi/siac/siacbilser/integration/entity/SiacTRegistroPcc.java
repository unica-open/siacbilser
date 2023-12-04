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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the siac_t_registro_pcc database table.
 * 
 */
@Entity
@Table(name="siac_t_registro_pcc")
@NamedQuery(name="SiacTRegistroPcc.findAll", query="SELECT s FROM SiacTRegistroPcc s")
public class SiacTRegistroPcc extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_REGISTRO_PCC_RPCCID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_REGISTRO_PCC_RPCC_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_REGISTRO_PCC_RPCCID_GENERATOR")
	@Column(name="rpcc_id")
	private Integer rpccId;

	@Temporal(TemporalType.DATE)
	@Column(name="data_scadenza")
	private Date dataScadenza;

	@Temporal(TemporalType.DATE)
	@Column(name="ordinativo_data_emissione")
	private Date ordinativoDataEmissione;

	@Column(name="ordinativo_numero")
	private Integer ordinativoNumero;

	@Column(name="provvisorio_cassa_anno")
	private Integer provvisorioCassaAnno;

	@Column(name="provvisorio_cassa_numero")
	private Integer provvisorioCassaNumero;

	@Column(name="rpcc_registrazione_data")
	private Date rpccRegistrazioneData;

	@Column(name="rpcc_richiesta_stato")
	private String rpccRichiestaStato;

	@Column(name="rpcc_esito_data")
	private Date rpccEsitoData;

	@Column(name="rpcc_esito_code")
	private String rpccEsitoCode;

	@Column(name="rpcc_esito_desc")
	private String rpccEsitoDesc;

	@Column(name="rpcc_quietanza_data")
	private Date rpccQuietanzaData;

	@Column(name="rpcc_quietanza_numero")
	private Integer rpccQuietanzaNumero;

	@Column(name="rpcc_quietanza_importo")
	private BigDecimal rpccQuietanzaImporto;
	
	@Column(name="rpcc_id_trans_pa")
	private String rpccIdTransPa;

	//bi-directional many-to-one association to SiacDPccCausale
	@ManyToOne
	@JoinColumn(name="pcccau_id")
	private SiacDPccCausale siacDPccCausale;

	//bi-directional many-to-one association to SiacDPccDebitoStato
	@ManyToOne
	@JoinColumn(name="pccdeb_stato_id")
	private SiacDPccDebitoStato siacDPccDebitoStato;

	//bi-directional many-to-one association to SiacDPccOperazioneTipo
	@ManyToOne
	@JoinColumn(name="pccop_tipo_id")
	private SiacDPccOperazioneTipo siacDPccOperazioneTipo;

	//bi-directional many-to-one association to SiacTDoc
	@ManyToOne
	@JoinColumn(name="doc_id")
	private SiacTDoc siacTDoc;

	//bi-directional many-to-one association to SiacTSubdoc
	@ManyToOne
	@JoinColumn(name="subdoc_id")
	private SiacTSubdoc siacTSubdoc;

	//bi-directional many-to-one association to SiacTSoggetto
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggetto siacTSoggetto;

	public SiacTRegistroPcc() {
	}

	public Integer getRpccId() {
		return this.rpccId;
	}

	public void setRpccId(Integer rpccId) {
		this.rpccId = rpccId;
	}
	
	public Date getDataScadenza() {
		return this.dataScadenza;
	}

	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public Date getOrdinativoDataEmissione() {
		return this.ordinativoDataEmissione;
	}

	public void setOrdinativoDataEmissione(Date ordinativoDataEmissione) {
		this.ordinativoDataEmissione = ordinativoDataEmissione;
	}

	public Integer getOrdinativoNumero() {
		return this.ordinativoNumero;
	}

	public void setOrdinativoNumero(Integer ordinativoNumero) {
		this.ordinativoNumero = ordinativoNumero;
	}

	public Integer getProvvisorioCassaAnno() {
		return this.provvisorioCassaAnno;
	}

	public void setProvvisorioCassaAnno(Integer provvisorioCassaAnno) {
		this.provvisorioCassaAnno = provvisorioCassaAnno;
	}

	public Integer getProvvisorioCassaNumero() {
		return this.provvisorioCassaNumero;
	}

	public void setProvvisorioCassaNumero(Integer provvisorioCassaNumero) {
		this.provvisorioCassaNumero = provvisorioCassaNumero;
	}

	public Date getRpccRegistrazioneData() {
		return this.rpccRegistrazioneData;
	}

	public void setRpccRegistrazioneData(Date rpccRegistrazioneData) {
		this.rpccRegistrazioneData = rpccRegistrazioneData;
	}

	public String getRpccRichiestaStato() {
		return rpccRichiestaStato;
	}

	public void setRpccRichiestaStato(String rpccRichiestaStato) {
		this.rpccRichiestaStato = rpccRichiestaStato;
	}

	public Date getRpccEsitoData() {
		return rpccEsitoData;
	}

	public void setRpccEsitoData(Date rpccEsitoData) {
		this.rpccEsitoData = rpccEsitoData;
	}

	public String getRpccEsitoCode() {
		return rpccEsitoCode;
	}

	public void setRpccEsitoCode(String rpccEsitoCode) {
		this.rpccEsitoCode = rpccEsitoCode;
	}

	public String getRpccEsitoDesc() {
		return rpccEsitoDesc;
	}

	public void setRpccEsitoDesc(String rpccEsitoDesc) {
		this.rpccEsitoDesc = rpccEsitoDesc;
	}

	public Date getRpccQuietanzaData() {
		return rpccQuietanzaData;
	}

	public void setRpccQuietanzaData(Date rpccQuietanzaData) {
		this.rpccQuietanzaData = rpccQuietanzaData;
	}

	public Integer getRpccQuietanzaNumero() {
		return rpccQuietanzaNumero;
	}

	public void setRpccQuietanzaNumero(Integer rpccQuietanzaNumero) {
		this.rpccQuietanzaNumero = rpccQuietanzaNumero;
	}

	public BigDecimal getRpccQuietanzaImporto() {
		return rpccQuietanzaImporto;
	}

	public void setRpccQuietanzaImporto(BigDecimal rpccQuietanzaImporto) {
		this.rpccQuietanzaImporto = rpccQuietanzaImporto;
	}

	public SiacDPccCausale getSiacDPccCausale() {
		return this.siacDPccCausale;
	}

	public void setSiacDPccCausale(SiacDPccCausale siacDPccCausale) {
		this.siacDPccCausale = siacDPccCausale;
	}

	public SiacDPccDebitoStato getSiacDPccDebitoStato() {
		return this.siacDPccDebitoStato;
	}

	public void setSiacDPccDebitoStato(SiacDPccDebitoStato siacDPccDebitoStato) {
		this.siacDPccDebitoStato = siacDPccDebitoStato;
	}

	public SiacDPccOperazioneTipo getSiacDPccOperazioneTipo() {
		return this.siacDPccOperazioneTipo;
	}

	public void setSiacDPccOperazioneTipo(SiacDPccOperazioneTipo siacDPccOperazioneTipo) {
		this.siacDPccOperazioneTipo = siacDPccOperazioneTipo;
	}

	public SiacTDoc getSiacTDoc() {
		return this.siacTDoc;
	}

	public void setSiacTDoc(SiacTDoc siacTDoc) {
		this.siacTDoc = siacTDoc;
	}

	public SiacTSubdoc getSiacTSubdoc() {
		return this.siacTSubdoc;
	}

	public void setSiacTSubdoc(SiacTSubdoc siacTSubdoc) {
		this.siacTSubdoc = siacTSubdoc;
	}

	public SiacTSoggetto getSiacTSoggetto() {
		return siacTSoggetto;
	}

	public void setSiacTSoggetto(SiacTSoggetto siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}
	

	/**
	 * @return the rpccIdTransPa
	 */
	public String getRpccIdTransPa() {
		return rpccIdTransPa;
	}

	/**
	 * @param rpccIdTransPa the rpccIdTransPa to set
	 */
	public void setRpccIdTransPa(String rpccIdTransPa) {
		this.rpccIdTransPa = rpccIdTransPa;
	}

	@Override
	public Integer getUid() {
		return rpccId;
	}

	@Override
	public void setUid(Integer uid) {
		this.rpccId = uid;
	}


}