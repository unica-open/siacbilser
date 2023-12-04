/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;


/**
 * The persistent class for the sirfel_t_prestatore database table.
 * 
 */
@Entity
@Table(name="sirfel_t_prestatore")
@NamedQuery(name="SirfelTPrestatore.findAll", query="SELECT s FROM SirfelTPrestatore s")
public class SirfelTPrestatore extends SirfelTBase<SirfelTPrestatorePK> {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	@AttributeOverrides({
        @AttributeOverride(name="enteProprietarioId",
                           column=@Column(name="ente_proprietario_id")),
        @AttributeOverride(name="idPrestatore",
                           column=@Column(name="id_prestatore"))
    })
	private SirfelTPrestatorePK id;

	@Column(name="cap_prestatore")
	private String capPrestatore;

	private Integer codben;

	@Column(name="codice_paese")
	private String codicePaese;

	@Column(name="codice_prestatore")
	private String codicePrestatore;

	@Column(name="cognome_prestatore")
	private String cognomePrestatore;

	@Column(name="comune_prestatore")
	private String comunePrestatore;

	@Column(name="data_inserimento")
	private Date dataInserimento;

	@Column(name="denominazione_prestatore")
	private String denominazionePrestatore;

	@Column(name="email_prestatore")
	private String emailPrestatore;

	@Column(name="fax_prestatore")
	private String faxPrestatore;

	@Column(name="indirizzo_prestatore")
	private String indirizzoPrestatore;

	@Column(name="nazione_prestatore")
	private String nazionePrestatore;

	@Column(name="nome_prestatore")
	private String nomePrestatore;

	@Column(name="numero_civico_prestatore")
	private String numeroCivicoPrestatore;

	@Column(name="provincia_albo_prestatore")
	private String provinciaAlboPrestatore;

	@Column(name="provincia_prestatore")
	private String provinciaPrestatore;

	@Column(name="telefono_prestatore")
	private String telefonoPrestatore;

	//bi-directional many-to-one association to SirfelTFattura
	@OneToMany(mappedBy="sirfelTPrestatore")
	private List<SirfelTFattura> sirfelTFatturas;

	//bi-directional many-to-one association to SirfelDRegimeFiscale
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="ente_proprietario_id", referencedColumnName="ente_proprietario_id", insertable=false, updatable=false),
		@JoinColumn(name="regime_fiscale", referencedColumnName="codice", insertable=false, updatable=false)
		})
	@MapsId("id")
	private SirfelDRegimeFiscale sirfelDRegimeFiscale;
	
	// FIXME: controllare se sia gestibile in altro modo
	@Column(name="regime_fiscale")
	private String regimeFiscale;

	public SirfelTPrestatore() {
	}
	
	@PrePersist
	@PreUpdate
	public void prePersist() {
		if(getDataInserimento() == null) {
			setDataInserimento(new Date());
		}
	}

	public SirfelTPrestatorePK getId() {
		return this.id;
	}

	public void setId(SirfelTPrestatorePK id) {
		this.id = id;
	}

	public String getCapPrestatore() {
		return this.capPrestatore;
	}

	public void setCapPrestatore(String capPrestatore) {
		this.capPrestatore = capPrestatore;
	}

	public Integer getCodben() {
		return this.codben;
	}

	public void setCodben(Integer codben) {
		this.codben = codben;
	}

	public String getCodicePaese() {
		return this.codicePaese;
	}

	public void setCodicePaese(String codicePaese) {
		this.codicePaese = codicePaese;
	}

	public String getCodicePrestatore() {
		return this.codicePrestatore;
	}

	public void setCodicePrestatore(String codicePrestatore) {
		this.codicePrestatore = codicePrestatore;
	}

	public String getCognomePrestatore() {
		return this.cognomePrestatore;
	}

	public void setCognomePrestatore(String cognomePrestatore) {
		this.cognomePrestatore = cognomePrestatore;
	}

	public String getComunePrestatore() {
		return this.comunePrestatore;
	}

	public void setComunePrestatore(String comunePrestatore) {
		this.comunePrestatore = comunePrestatore;
	}

	public Date getDataInserimento() {
		return this.dataInserimento;
	}

	public void setDataInserimento(Date dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public String getDenominazionePrestatore() {
		return this.denominazionePrestatore;
	}

	public void setDenominazionePrestatore(String denominazionePrestatore) {
		this.denominazionePrestatore = denominazionePrestatore;
	}

	public String getEmailPrestatore() {
		return this.emailPrestatore;
	}

	public void setEmailPrestatore(String emailPrestatore) {
		this.emailPrestatore = emailPrestatore;
	}

	public String getFaxPrestatore() {
		return this.faxPrestatore;
	}

	public void setFaxPrestatore(String faxPrestatore) {
		this.faxPrestatore = faxPrestatore;
	}

	public String getIndirizzoPrestatore() {
		return this.indirizzoPrestatore;
	}

	public void setIndirizzoPrestatore(String indirizzoPrestatore) {
		this.indirizzoPrestatore = indirizzoPrestatore;
	}

	public String getNazionePrestatore() {
		return this.nazionePrestatore;
	}

	public void setNazionePrestatore(String nazionePrestatore) {
		this.nazionePrestatore = nazionePrestatore;
	}

	public String getNomePrestatore() {
		return this.nomePrestatore;
	}

	public void setNomePrestatore(String nomePrestatore) {
		this.nomePrestatore = nomePrestatore;
	}

	public String getNumeroCivicoPrestatore() {
		return this.numeroCivicoPrestatore;
	}

	public void setNumeroCivicoPrestatore(String numeroCivicoPrestatore) {
		this.numeroCivicoPrestatore = numeroCivicoPrestatore;
	}

	public String getProvinciaAlboPrestatore() {
		return this.provinciaAlboPrestatore;
	}

	public void setProvinciaAlboPrestatore(String provinciaAlboPrestatore) {
		this.provinciaAlboPrestatore = provinciaAlboPrestatore;
	}

	public String getProvinciaPrestatore() {
		return this.provinciaPrestatore;
	}

	public void setProvinciaPrestatore(String provinciaPrestatore) {
		this.provinciaPrestatore = provinciaPrestatore;
	}

	public String getTelefonoPrestatore() {
		return this.telefonoPrestatore;
	}

	public void setTelefonoPrestatore(String telefonoPrestatore) {
		this.telefonoPrestatore = telefonoPrestatore;
	}

	public List<SirfelTFattura> getSirfelTFatturas() {
		return this.sirfelTFatturas;
	}

	public void setSirfelTFatturas(List<SirfelTFattura> sirfelTFatturas) {
		this.sirfelTFatturas = sirfelTFatturas;
	}

	public SirfelTFattura addSirfelTFatturas(SirfelTFattura sirfelTFatturas) {
		getSirfelTFatturas().add(sirfelTFatturas);
		sirfelTFatturas.setSirfelTPrestatore(this);

		return sirfelTFatturas;
	}

	public SirfelTFattura removeSirfelTFatturas1(SirfelTFattura sirfelTFatturas) {
		getSirfelTFatturas().remove(sirfelTFatturas);
		sirfelTFatturas.setSirfelTPrestatore(null);

		return sirfelTFatturas;
	}

	public SirfelDRegimeFiscale getSirfelDRegimeFiscale() {
		return this.sirfelDRegimeFiscale;
	}

	public void setSirfelDRegimeFiscale(SirfelDRegimeFiscale sirfelDRegimeFiscale) {
		this.sirfelDRegimeFiscale = sirfelDRegimeFiscale;
	}
	
	public String getRegimeFiscale() {
		return regimeFiscale;
	}

	public void setRegimeFiscale(String regimeFiscale) {
		this.regimeFiscale = regimeFiscale;
	}

	public Integer getIdPrestatore() {
		return getId() != null ? getId().getIdPrestatore() : null;
	}
	
}