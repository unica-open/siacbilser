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
 * The persistent class for the sirfel_t_protocollo database table.
 * 
 */
@Entity
@Table(name="sirfel_t_protocollo")
@NamedQuery(name="SirfelTProtocollo.findAll", query="SELECT s FROM SirfelTProtocollo s")
public class SirfelTProtocollo extends SirfelTBase<SirfelTFatturaPK> {
	private static final long serialVersionUID = 1L;

	// Evidentemente, JPA/HIbernate non e' in grado di comprendere la PK corretta. Ma e' possibile?
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

	@Column(name="anno_protocollo")
	private String annoProtocollo;

	@Column(name="data_reg_protocollo")
	private Date dataRegProtocollo;

	@Column(name="id_classificazione")
	private String idClassificazione;

	@Column(name="indice_classificazione_estesa")
	private String indiceClassificazioneEstesa;

	@Column(name="numero_protocollo")
	private String numeroProtocollo;

	private String oggetto;

	@Column(name="principal_id_archiviazione")
	private String principalIdArchiviazione;

	@Column(name="registro_protocollo")
	private String registroProtocollo;

	//bi-directional many-to-one association to SirfelTFattura
	@OneToOne
	@JoinColumns({
		@JoinColumn(name="ente_proprietario_id", referencedColumnName="ente_proprietario_id"),
		@JoinColumn(name="id_fattura", referencedColumnName="id_fattura")
		})
	@MapsId("id")
	private SirfelTFattura sirfelTFattura;

	public SirfelTProtocollo() {
	}

	public SirfelTFatturaPK getId() {
		return this.id;
	}

	public void setId(SirfelTFatturaPK id) {
		this.id = id;
	}

	public String getAnnoProtocollo() {
		return this.annoProtocollo;
	}

	public void setAnnoProtocollo(String annoProtocollo) {
		this.annoProtocollo = annoProtocollo;
	}

	public Date getDataRegProtocollo() {
		return this.dataRegProtocollo;
	}

	public void setDataRegProtocollo(Date dataRegProtocollo) {
		this.dataRegProtocollo = dataRegProtocollo;
	}

	public String getIdClassificazione() {
		return this.idClassificazione;
	}

	public void setIdClassificazione(String idClassificazione) {
		this.idClassificazione = idClassificazione;
	}

	public String getIndiceClassificazioneEstesa() {
		return this.indiceClassificazioneEstesa;
	}

	public void setIndiceClassificazioneEstesa(String indiceClassificazioneEstesa) {
		this.indiceClassificazioneEstesa = indiceClassificazioneEstesa;
	}

	public String getNumeroProtocollo() {
		return this.numeroProtocollo;
	}

	public void setNumeroProtocollo(String numeroProtocollo) {
		this.numeroProtocollo = numeroProtocollo;
	}

	public String getOggetto() {
		return this.oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public String getPrincipalIdArchiviazione() {
		return this.principalIdArchiviazione;
	}

	public void setPrincipalIdArchiviazione(String principalIdArchiviazione) {
		this.principalIdArchiviazione = principalIdArchiviazione;
	}

	public String getRegistroProtocollo() {
		return this.registroProtocollo;
	}

	public void setRegistroProtocollo(String registroProtocollo) {
		this.registroProtocollo = registroProtocollo;
	}

	public SirfelTFattura getSirfelTFattura() {
		return this.sirfelTFattura;
	}

	public void setSirfelTFattura(SirfelTFattura sirfelTFattura) {
		this.sirfelTFattura = sirfelTFattura;
	}

}