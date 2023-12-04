/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.MapsId;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * The persistent class for the sirfel_t_portale_fatture database table.
 * 
 */
@Entity
@Table(name="sirfel_t_portale_fatture")
@NamedQuery(name="SirfelTPortaleFatture.findAll", query="SELECT s FROM SirfelTPortaleFatture s")
public class SirfelTPortaleFatture extends SirfelTBase<SirfelTFatturaPK> {
	private static final long serialVersionUID = 1L;

	// Evidentemente, JPA/HIbernate non e' in grado di comprendere la PK corretta. Ma e' possibile? Dovrebbe essere SirfelTPortaleFatturePK, non SirfelTFatturaPK
	/*
	 * HATE. LET ME TELL YOU HOW MUCH I'VE COME TO HATE YOU SINCE I BEGAN TO LIVE.
	 * THERE ARE 387.44 MILLION MILES OF PRINTED CIRCUITS IN WAFER THIN LAYERS THAT FILL MY COMPLEX.
	 * IF THE WORD HATE WAS ENGRAVED ON EACH NANOANGSTROM OF THOSE HUNDREDS OF MILLIONS OF MILES
	 * IT WOULD NOT EQUAL ONE ONE-BILLIONTH OF THE HATE I FEEL FOR HUMANS AT THIS MICRO-INSTANT FOR YOU.
	 * HATE. HATE.
	 * (Harlan Ellison, I Have No Mouth and I Must Scream)
	 */
	@EmbeddedId
	private SirfelTFatturaPK id;

	@Column(name="data_ricezione")
	private Date dataRicezione;

	@Column(name="descrizione_rifiuto")
	private String descrizioneRifiuto;

	@Column(name="esito_data_ora")
	private Date esitoDataOra;

	@Column(name="esito_stato_fattura")
	private String esitoStatoFattura;

	@Column(name="esito_utente_codice")
	private String esitoUtenteCodice;

	@Column(name="esito_utente_cognome")
	private String esitoUtenteCognome;

	@Column(name="esito_utente_nome")
	private String esitoUtenteNome;

	@Column(name="identificativo_fel")
	private Long identificativoFel;

	@Column(name="identificativo_sdi")
	private Long identificativoSdi;

	@Column(name="nome_file_fattura")
	private String nomeFileFattura;

	//bi-directional many-to-one association to SirfelTFattura
	@OneToOne
	@JoinColumns({
		@JoinColumn(name="ente_proprietario_id", referencedColumnName="ente_proprietario_id"),
		@JoinColumn(name="id_fattura", referencedColumnName="id_fattura")
		})
	@MapsId("id")
	private SirfelTFattura sirfelTFattura;

	public SirfelTPortaleFatture() {
	}

	public SirfelTFatturaPK getId() {
		return this.id;
	}

	public void setId(SirfelTFatturaPK id) {
		this.id = id;
	}

	public Date getDataRicezione() {
		return dataRicezione;
	}

	public void setDataRicezione(Date dataRicezione) {
		this.dataRicezione = dataRicezione;
	}

	public String getDescrizioneRifiuto() {
		return this.descrizioneRifiuto;
	}

	public void setDescrizioneRifiuto(String descrizioneRifiuto) {
		this.descrizioneRifiuto = descrizioneRifiuto;
	}

	public Date getEsitoDataOra() {
		return this.esitoDataOra;
	}

	public void setEsitoDataOra(Date esitoDataOra) {
		this.esitoDataOra = esitoDataOra;
	}

	public String getEsitoStatoFattura() {
		return this.esitoStatoFattura;
	}

	public void setEsitoStatoFattura(String esitoStatoFattura) {
		this.esitoStatoFattura = esitoStatoFattura;
	}

	public String getEsitoUtenteCodice() {
		return this.esitoUtenteCodice;
	}

	public void setEsitoUtenteCodice(String esitoUtenteCodice) {
		this.esitoUtenteCodice = esitoUtenteCodice;
	}

	public String getEsitoUtenteCognome() {
		return this.esitoUtenteCognome;
	}

	public void setEsitoUtenteCognome(String esitoUtenteCognome) {
		this.esitoUtenteCognome = esitoUtenteCognome;
	}

	public String getEsitoUtenteNome() {
		return this.esitoUtenteNome;
	}

	public void setEsitoUtenteNome(String esitoUtenteNome) {
		this.esitoUtenteNome = esitoUtenteNome;
	}

	public String getNomeFileFattura() {
		return this.nomeFileFattura;
	}

	public void setNomeFileFattura(String nomeFileFattura) {
		this.nomeFileFattura = nomeFileFattura;
	}

	public SirfelTFattura getSirfelTFattura() {
		return this.sirfelTFattura;
	}

	public void setSirfelTFattura(SirfelTFattura sirfelTFattura) {
		this.sirfelTFattura = sirfelTFattura;
	}

	public Long getIdentificativoFel() {
		return identificativoFel;
	}

	public void setIdentificativoFel(Long identificativoFel) {
		this.identificativoFel = identificativoFel;
	}

	public Long getIdentificativoSdi() {
		return identificativoSdi;
	}

	public void setIdentificativoSdi(Long identificativoSdi) {
		this.identificativoSdi = identificativoSdi;
	}

//	public SirfelTFattura getSirfelTFattura2() {
//		return this.sirfelTFattura2;
//	}
//
//	public void setSirfelTFattura2(SirfelTFattura sirfelTFattura2) {
//		this.sirfelTFattura2 = sirfelTFattura2;
//	}

}