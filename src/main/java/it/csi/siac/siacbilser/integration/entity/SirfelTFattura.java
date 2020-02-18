/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;


/**
 * The persistent class for the sirfel_t_fattura database table.
 * 
 */
@Entity
@Table(name="sirfel_t_fattura")
@NamedQuery(name="SirfelTFattura.findAll", query="SELECT s FROM SirfelTFattura s")
public class SirfelTFattura extends SirfelTBase<SirfelTFatturaPK> {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	@AttributeOverrides({
        @AttributeOverride(name="enteProprietarioId",
                           column=@Column(name="ente_proprietario_id")),
        @AttributeOverride(name="idFattura",
                           column=@Column(name="id_fattura"))
    })
	private SirfelTFatturaPK id;

	@Column(name="aliquota_ritenuta")
	private BigDecimal aliquotaRitenuta;

	private BigDecimal arrotondamento;

	@Column(name="bollo_virtuale")
	private String bolloVirtuale;

	@Column(name="causale_pagamento")
	private String causalePagamento;

	@Column(name="codice_destinatario")
	private String codiceDestinatario;

	@Column(name="codice_trasmittente")
	private String codiceTrasmittente;

	private Date data;

	@Column(name="data_caricamento")
	private Date dataCaricamento;

	@Column(name="data_inserimento")
	private Date dataInserimento;

	private String divisa;

	@Column(name="importo_bollo")
	private BigDecimal importoBollo;

	@Column(name="importo_ritenuta")
	private BigDecimal importoRitenuta;

	@Column(name="importo_totale_documento")
	private BigDecimal importoTotaleDocumento;

	@Column(name="importo_totale_netto")
	private BigDecimal importoTotaleNetto;

	private String numero;

	@Column(name="stato_fattura")
	private String statoFattura;

	@Column(name="tipo_ritenuta")
	private String tipoRitenuta;

	private String note;

	//bi-directional many-to-one association to SiacRDocSirfel
	@OneToMany(mappedBy="sirfelTFattura")
	private List<SiacRDocSirfel> siacRDocSirfels;

	//bi-directional many-to-one association to SirfelTCassaPrevidenziale
	@OneToMany(mappedBy="sirfelTFattura")
	private List<SirfelTCassaPrevidenziale> sirfelTCassaPrevidenziales;

//	//bi-directional many-to-one association to SirfelTCassaPrevidenziale
//	@OneToMany(mappedBy="sirfelTFattura2")
//	private List<SirfelTCassaPrevidenziale> sirfelTCassaPrevidenziales2;

	//bi-directional many-to-one association to SirfelTCausale
	@OneToMany(mappedBy="sirfelTFattura")
	private List<SirfelTCausale> sirfelTCausales;

	//bi-directional many-to-one association to SirfelTDatiGestionali
	@OneToMany(mappedBy="sirfelTFattura")
	private List<SirfelTDatiGestionali> sirfelTDatiGestionalis;
	
	//bi-directional many-to-one association to SirfelTOrdineAcquisto
	@OneToMany(mappedBy="sirfelTFattura")
	private List<SirfelTOrdineAcquisto> sirfelTOrdineAcquistos;

	//bi-directional many-to-one association to SirfelDTipoDocumento
	@ManyToOne(optional=true,fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="ente_proprietario_id", referencedColumnName="ente_proprietario_id", insertable=false, updatable=false),
		@JoinColumn(name="tipo_documento", referencedColumnName="codice", insertable=false, updatable=false)
		})
	@MapsId("id")
	private SirfelDTipoDocumento sirfelDTipoDocumento;
	
	@Column(name="tipo_documento")
	private String tipoDocumento;

	//bi-directional many-to-one association to SirfelTPrestatore
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="ente_proprietario_id", referencedColumnName="ente_proprietario_id", insertable=false, updatable=false),
		@JoinColumn(name="id_prestatore", referencedColumnName="id_prestatore", insertable=false, updatable=false)
		})
	@MapsId("id")
	private SirfelTPrestatore sirfelTPrestatore;
	
	@Column(name="id_prestatore")
	private Integer idPrestatore;


	//bi-directional many-to-one association to SirfelTFatturaContabile
	@OneToMany(mappedBy="sirfelTFattura")
	private List<SirfelTFatturaContabile> sirfelTFatturaContabiles;

	//bi-directional many-to-one association to SirfelTFattureCollegate
	@OneToMany(mappedBy="sirfelTFattura")
	private List<SirfelTFattureCollegate> sirfelTFattureCollegates;

	//bi-directional many-to-one association to SirfelTPagamento
	@OneToMany(mappedBy="sirfelTFattura")
	private List<SirfelTPagamento> sirfelTPagamentos;

	//bi-directional many-to-one association to SirfelTPortaleFatture
	@OneToOne(mappedBy="sirfelTFattura")
	private SirfelTPortaleFatture sirfelTPortaleFatture;

	//bi-directional many-to-one association to SirfelTProtocollo
	@OneToOne(mappedBy="sirfelTFattura")
	private SirfelTProtocollo sirfelTProtocollo;
	

	//bi-directional many-to-one association to SirfelTRiepilogoBeni
	@OneToMany(mappedBy="sirfelTFattura")
	private List<SirfelTRiepilogoBeni> sirfelTRiepilogoBenis;

	public SirfelTFattura() {
	}
	
	@PrePersist
	@PreUpdate
	public void prePersist() {
		// Creazione valori di default
		if(getDataInserimento() == null) {
			setDataInserimento(new Date());
		}
		if(getStatoFattura() == null) {
			setStatoFattura("N");
		}
	}

	public SirfelTFatturaPK getId() {
		return this.id;
	}

	public void setId(SirfelTFatturaPK id) {
		this.id = id;
	}

	public BigDecimal getAliquotaRitenuta() {
		return this.aliquotaRitenuta;
	}

	public void setAliquotaRitenuta(BigDecimal aliquotaRitenuta) {
		this.aliquotaRitenuta = aliquotaRitenuta;
	}

	public BigDecimal getArrotondamento() {
		return this.arrotondamento;
	}

	public void setArrotondamento(BigDecimal arrotondamento) {
		this.arrotondamento = arrotondamento;
	}

	public String getBolloVirtuale() {
		return this.bolloVirtuale;
	}

	public void setBolloVirtuale(String bolloVirtuale) {
		this.bolloVirtuale = bolloVirtuale;
	}

	public String getCausalePagamento() {
		return this.causalePagamento;
	}

	public void setCausalePagamento(String causalePagamento) {
		this.causalePagamento = causalePagamento;
	}

	public String getCodiceDestinatario() {
		return this.codiceDestinatario;
	}

	public void setCodiceDestinatario(String codiceDestinatario) {
		this.codiceDestinatario = codiceDestinatario;
	}

	public String getCodiceTrasmittente() {
		return this.codiceTrasmittente;
	}

	public void setCodiceTrasmittente(String codiceTrasmittente) {
		this.codiceTrasmittente = codiceTrasmittente;
	}

	public Date getData() {
		return this.data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Date getDataCaricamento() {
		return this.dataCaricamento;
	}

	public void setDataCaricamento(Date dataCaricamento) {
		this.dataCaricamento = dataCaricamento;
	}

	public Date getDataInserimento() {
		return this.dataInserimento;
	}

	public void setDataInserimento(Date dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public String getDivisa() {
		return this.divisa;
	}

	public void setDivisa(String divisa) {
		this.divisa = divisa;
	}

	public BigDecimal getImportoBollo() {
		return this.importoBollo;
	}

	public void setImportoBollo(BigDecimal importoBollo) {
		this.importoBollo = importoBollo;
	}

	public BigDecimal getImportoRitenuta() {
		return this.importoRitenuta;
	}

	public void setImportoRitenuta(BigDecimal importoRitenuta) {
		this.importoRitenuta = importoRitenuta;
	}

	public BigDecimal getImportoTotaleDocumento() {
		return this.importoTotaleDocumento;
	}

	public void setImportoTotaleDocumento(BigDecimal importoTotaleDocumento) {
		this.importoTotaleDocumento = importoTotaleDocumento;
	}

	public BigDecimal getImportoTotaleNetto() {
		return this.importoTotaleNetto;
	}

	public void setImportoTotaleNetto(BigDecimal importoTotaleNetto) {
		this.importoTotaleNetto = importoTotaleNetto;
	}

	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getStatoFattura() {
		return this.statoFattura;
	}

	public void setStatoFattura(String statoFattura) {
		this.statoFattura = statoFattura;
	}

	public String getTipoRitenuta() {
		return this.tipoRitenuta;
	}

	public void setTipoRitenuta(String tipoRitenuta) {
		this.tipoRitenuta = tipoRitenuta;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public List<SiacRDocSirfel> getSiacRDocSirfels() {
		return this.siacRDocSirfels;
	}

	public void setSiacRDocSirfels(List<SiacRDocSirfel> siacRDocSirfels) {
		this.siacRDocSirfels = siacRDocSirfels;
	}

	public SiacRDocSirfel addSiacRDocSirfel(SiacRDocSirfel siacRDocSirfel) {
		getSiacRDocSirfels().add(siacRDocSirfel);
		siacRDocSirfel.setSirfelTFattura(this);

		return siacRDocSirfel;
	}

	public SiacRDocSirfel removeSiacRDocSirfel(SiacRDocSirfel siacRDocSirfel) {
		getSiacRDocSirfels().remove(siacRDocSirfel);
		siacRDocSirfel.setSirfelTFattura(null);

		return siacRDocSirfel;
	}

	public List<SirfelTCassaPrevidenziale> getSirfelTCassaPrevidenziales() {
		return this.sirfelTCassaPrevidenziales;
	}

	public void setSirfelTCassaPrevidenziales(List<SirfelTCassaPrevidenziale> sirfelTCassaPrevidenziales) {
		this.sirfelTCassaPrevidenziales = sirfelTCassaPrevidenziales;
	}

	public SirfelTCassaPrevidenziale addSirfelTCassaPrevidenziales(SirfelTCassaPrevidenziale sirfelTCassaPrevidenziales) {
		getSirfelTCassaPrevidenziales().add(sirfelTCassaPrevidenziales);
		sirfelTCassaPrevidenziales.setSirfelTFattura(this);

		return sirfelTCassaPrevidenziales;
	}

	public SirfelTCassaPrevidenziale removeSirfelTCassaPrevidenziales(SirfelTCassaPrevidenziale sirfelTCassaPrevidenziales) {
		getSirfelTCassaPrevidenziales().remove(sirfelTCassaPrevidenziales);
		sirfelTCassaPrevidenziales.setSirfelTFattura(null);

		return sirfelTCassaPrevidenziales;
	}

	public List<SirfelTCausale> getSirfelTCausales() {
		return this.sirfelTCausales;
	}

	public void setSirfelTCausales(List<SirfelTCausale> sirfelTCausales) {
		this.sirfelTCausales = sirfelTCausales;
	}

	public SirfelTCausale addSirfelTCausales(SirfelTCausale sirfelTCausales) {
		getSirfelTCausales().add(sirfelTCausales);
		sirfelTCausales.setSirfelTFattura(this);

		return sirfelTCausales;
	}

	public SirfelTCausale removeSirfelTCausales(SirfelTCausale sirfelTCausales) {
		getSirfelTCausales().remove(sirfelTCausales);
		sirfelTCausales.setSirfelTFattura(null);

		return sirfelTCausales;
	}

	public List<SirfelTDatiGestionali> getSirfelTDatiGestionalis() {
		return this.sirfelTDatiGestionalis;
	}

	public void setSirfelTDatiGestionalis(List<SirfelTDatiGestionali> sirfelTDatiGestionalis) {
		this.sirfelTDatiGestionalis = sirfelTDatiGestionalis;
	}

	public SirfelTDatiGestionali addSirfelTDatiGestionalis(SirfelTDatiGestionali sirfelTDatiGestionalis) {
		getSirfelTDatiGestionalis().add(sirfelTDatiGestionalis);
		sirfelTDatiGestionalis.setSirfelTFattura(this);

		return sirfelTDatiGestionalis;
	}

	public SirfelTDatiGestionali removeSirfelTDatiGestionalis(SirfelTDatiGestionali sirfelTDatiGestionalis) {
		getSirfelTDatiGestionalis().remove(sirfelTDatiGestionalis);
		sirfelTDatiGestionalis.setSirfelTFattura(null);

		return sirfelTDatiGestionalis;
	}

	public List<SirfelTOrdineAcquisto> getSirfelTOrdineAcquistos() {
		return this.sirfelTOrdineAcquistos;
	}

	public void setSirfelTOrdineAcquistos(List<SirfelTOrdineAcquisto> sirfelTOrdineAcquistos) {
		this.sirfelTOrdineAcquistos = sirfelTOrdineAcquistos;
	}

	public SirfelTOrdineAcquisto addSirfelTOrdineAcquisto(SirfelTOrdineAcquisto sirfelTOrdineAcquisto) {
		getSirfelTOrdineAcquistos().add(sirfelTOrdineAcquisto);
		sirfelTOrdineAcquisto.setSirfelTFattura(this);

		return sirfelTOrdineAcquisto;
	}

	public SirfelTOrdineAcquisto removeSirfelTOrdineAcquisto(SirfelTOrdineAcquisto sirfelTOrdineAcquisto) {
		getSirfelTOrdineAcquistos().remove(sirfelTOrdineAcquisto);
		sirfelTOrdineAcquisto.setSirfelTFattura(null);

		return sirfelTOrdineAcquisto;
	}

	public SirfelDTipoDocumento getSirfelDTipoDocumento() {
		return this.sirfelDTipoDocumento;
	}

	public void setSirfelDTipoDocumento(SirfelDTipoDocumento sirfelDTipoDocumento) {
		this.sirfelDTipoDocumento = sirfelDTipoDocumento;
	}

	
	public SirfelTPrestatore getSirfelTPrestatore() {
		return this.sirfelTPrestatore;
	}

	public void setSirfelTPrestatore(SirfelTPrestatore sirfelTPrestatore) {
		this.sirfelTPrestatore = sirfelTPrestatore;
	}


	public List<SirfelTFatturaContabile> getSirfelTFatturaContabiles() {
		return this.sirfelTFatturaContabiles;
	}

	public void setSirfelTFatturaContabiles(List<SirfelTFatturaContabile> sirfelTFatturaContabiles) {
		this.sirfelTFatturaContabiles = sirfelTFatturaContabiles;
	}

	public SirfelTFatturaContabile addSirfelTFatturaContabiles(SirfelTFatturaContabile sirfelTFatturaContabiles) {
		getSirfelTFatturaContabiles().add(sirfelTFatturaContabiles);
		sirfelTFatturaContabiles.setSirfelTFattura(this);

		return sirfelTFatturaContabiles;
	}

	public SirfelTFatturaContabile removeSirfelTFatturaContabiles(SirfelTFatturaContabile sirfelTFatturaContabiles) {
		getSirfelTFatturaContabiles().remove(sirfelTFatturaContabiles);
		sirfelTFatturaContabiles.setSirfelTFattura(null);

		return sirfelTFatturaContabiles;
	}

	public List<SirfelTFattureCollegate> getSirfelTFattureCollegates() {
		return this.sirfelTFattureCollegates;
	}

	public void setSirfelTFattureCollegates(List<SirfelTFattureCollegate> sirfelTFattureCollegates) {
		this.sirfelTFattureCollegates = sirfelTFattureCollegates;
	}

	public SirfelTFattureCollegate addSirfelTFattureCollegates(SirfelTFattureCollegate sirfelTFattureCollegates) {
		getSirfelTFattureCollegates().add(sirfelTFattureCollegates);
		sirfelTFattureCollegates.setSirfelTFattura(this);

		return sirfelTFattureCollegates;
	}

	public SirfelTFattureCollegate removeSirfelTFattureCollegates(SirfelTFattureCollegate sirfelTFattureCollegates) {
		getSirfelTFattureCollegates().remove(sirfelTFattureCollegates);
		sirfelTFattureCollegates.setSirfelTFattura(null);

		return sirfelTFattureCollegates;
	}

	public List<SirfelTPagamento> getSirfelTPagamentos() {
		return this.sirfelTPagamentos;
	}

	public void setSirfelTPagamentos(List<SirfelTPagamento> sirfelTPagamentos) {
		this.sirfelTPagamentos = sirfelTPagamentos;
	}

	public SirfelTPagamento addSirfelTPagamentos(SirfelTPagamento sirfelTPagamentos) {
		getSirfelTPagamentos().add(sirfelTPagamentos);
		sirfelTPagamentos.setSirfelTFattura(this);

		return sirfelTPagamentos;
	}

	public SirfelTPagamento removeSirfelTPagamentos(SirfelTPagamento sirfelTPagamentos) {
		getSirfelTPagamentos().remove(sirfelTPagamentos);
		sirfelTPagamentos.setSirfelTFattura(null);

		return sirfelTPagamentos;
	}

//	public List<SirfelTPortaleFatture> getSirfelTPortaleFattures() {
//		return this.sirfelTPortaleFattures;
//	}
//
//	public void setSirfelTPortaleFattures(List<SirfelTPortaleFatture> sirfelTPortaleFattures) {
//		this.sirfelTPortaleFattures = sirfelTPortaleFattures;
//	}
//
//	public SirfelTPortaleFatture addSirfelTPortaleFatture(SirfelTPortaleFatture sirfelTPortaleFatture) {
//		getSirfelTPortaleFattures().add(sirfelTPortaleFatture);
//		sirfelTPortaleFatture.setSirfelTFattura(this);
//
//		return sirfelTPortaleFatture;
//	}
//
//	public SirfelTPortaleFatture removeSirfelTPortaleFatture(SirfelTPortaleFatture sirfelTPortaleFatture) {
//		getSirfelTPortaleFattures().remove(sirfelTPortaleFatture);
//		sirfelTPortaleFatture.setSirfelTFattura(null);
//
//		return sirfelTPortaleFatture;
//	}
	

	/**
	 * @return the sirfelTPortaleFatture
	 */
	public SirfelTPortaleFatture getSirfelTPortaleFatture() {
		return sirfelTPortaleFatture;
	}

	/**
	 * @param sirfelTPortaleFatture the sirfelTPortaleFatture to set
	 */
	public void setSirfelTPortaleFatture(SirfelTPortaleFatture sirfelTPortaleFatture) {
		this.sirfelTPortaleFatture = sirfelTPortaleFatture;
	}

	/**
	 * @return the sirfelTProtocollo
	 */
	public SirfelTProtocollo getSirfelTProtocollo() {
		return sirfelTProtocollo;
	}

	/**
	 * @param sirfelTProtocollo the sirfelTProtocollo to set
	 */
	public void setSirfelTProtocollo(SirfelTProtocollo sirfelTProtocollo) {
		this.sirfelTProtocollo = sirfelTProtocollo;
	}

	public List<SirfelTRiepilogoBeni> getSirfelTRiepilogoBenis() {
		return this.sirfelTRiepilogoBenis;
	}

	public void setSirfelTRiepilogoBenis(List<SirfelTRiepilogoBeni> sirfelTRiepilogoBenis) {
		this.sirfelTRiepilogoBenis = sirfelTRiepilogoBenis;
	}

	public SirfelTRiepilogoBeni addSirfelTRiepilogoBenis(SirfelTRiepilogoBeni sirfelTRiepilogoBenis) {
		getSirfelTRiepilogoBenis().add(sirfelTRiepilogoBenis);
		sirfelTRiepilogoBenis.setSirfelTFattura(this);

		return sirfelTRiepilogoBenis;
	}

	public SirfelTRiepilogoBeni removeSirfelTRiepilogoBenis(SirfelTRiepilogoBeni sirfelTRiepilogoBenis) {
		getSirfelTRiepilogoBenis().remove(sirfelTRiepilogoBenis);
		sirfelTRiepilogoBenis.setSirfelTFattura(null);

		return sirfelTRiepilogoBenis;
	}
	
	/**
	 * @return the tipoDocumento
	 */
	public String getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @param tipoDocumento the tipoDocumento to set
	 */
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	/**
	 * @return the idPrestatore
	 */
	public Integer getIdPrestatore() {
		return idPrestatore;
	}

	/**
	 * @param idPrestatore the idPrestatore to set
	 */
	public void setIdPrestatore(Integer idPrestatore) {
		this.idPrestatore = idPrestatore;
	}

	public Integer getIdFattura() {
		return getId() != null ? getId().getIdFattura() : null;
	}

}