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
import javax.persistence.Table;


/**
 * The persistent class for the sirfel_t_ordine_acquisto database table.
 * 
 */
@Entity
@Table(name="sirfel_t_ordine_acquisto")
@NamedQuery(name="SirfelTOrdineAcquisto.findAll", query="SELECT s FROM SirfelTOrdineAcquisto s")
public class SirfelTOrdineAcquisto extends SirfelTBase<SirfelTOrdineAcquistoPK> {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name="enteProprietarioId", column=@Column(name="ente_proprietario_id")),
		@AttributeOverride(name="idFattura", column=@Column(name="id_fattura"))
	})
	private SirfelTOrdineAcquistoPK id;

	private String cig;

	@Column(name="codice_commessa_convenzione")
	private String codiceCommessaConvenzione;

	private String cup;

	@Column(name="data_documento")
	private Date dataDocumento;

	@Column(name="numero_voce")
	private String numeroVoce;

	//bi-directional many-to-one association to SirfelTOrdineAcquistoDettaglio
	@OneToMany(mappedBy="sirfelTOrdineAcquisto")
	private List<SirfelTOrdineAcquistoDettaglio> sirfelTOrdineAcquistoDettaglios;
	
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="ente_proprietario_id", referencedColumnName="ente_proprietario_id", insertable=false, updatable=false),
		@JoinColumn(name="id_fattura", referencedColumnName="id_fattura", insertable=false, updatable=false)
	})
	@MapsId("id")
	private SirfelTFattura sirfelTFattura;

	public SirfelTOrdineAcquisto() {
	}

	public SirfelTOrdineAcquistoPK getId() {
		return this.id;
	}

	public void setId(SirfelTOrdineAcquistoPK id) {
		this.id = id;
	}

	public String getCig() {
		return this.cig;
	}

	public void setCig(String cig) {
		this.cig = cig;
	}

	public String getCodiceCommessaConvenzione() {
		return this.codiceCommessaConvenzione;
	}

	public void setCodiceCommessaConvenzione(String codiceCommessaConvenzione) {
		this.codiceCommessaConvenzione = codiceCommessaConvenzione;
	}

	public String getCup() {
		return this.cup;
	}

	public void setCup(String cup) {
		this.cup = cup;
	}

	public Date getDataDocumento() {
		return this.dataDocumento;
	}

	public void setDataDocumento(Date dataDocumento) {
		this.dataDocumento = dataDocumento;
	}

	public String getNumeroVoce() {
		return this.numeroVoce;
	}

	public void setNumeroVoce(String numeroVoce) {
		this.numeroVoce = numeroVoce;
	}

	public List<SirfelTOrdineAcquistoDettaglio> getSirfelTOrdineAcquistoDettaglios() {
		return this.sirfelTOrdineAcquistoDettaglios;
	}

	public void setSirfelTOrdineAcquistoDettaglios(List<SirfelTOrdineAcquistoDettaglio> sirfelTOrdineAcquistoDettaglios) {
		this.sirfelTOrdineAcquistoDettaglios = sirfelTOrdineAcquistoDettaglios;
	}

	public SirfelTOrdineAcquistoDettaglio addSirfelTOrdineAcquistoDettaglio(SirfelTOrdineAcquistoDettaglio sirfelTOrdineAcquistoDettaglio) {
		getSirfelTOrdineAcquistoDettaglios().add(sirfelTOrdineAcquistoDettaglio);
		sirfelTOrdineAcquistoDettaglio.setSirfelTOrdineAcquisto(this);

		return sirfelTOrdineAcquistoDettaglio;
	}

	public SirfelTOrdineAcquistoDettaglio removeSirfelTOrdineAcquistoDettaglio(SirfelTOrdineAcquistoDettaglio sirfelTOrdineAcquistoDettaglio) {
		getSirfelTOrdineAcquistoDettaglios().remove(sirfelTOrdineAcquistoDettaglio);
		sirfelTOrdineAcquistoDettaglio.setSirfelTOrdineAcquisto(null);

		return sirfelTOrdineAcquistoDettaglio;
	}

	public SirfelTFattura getSirfelTFattura() {
		return sirfelTFattura;
	}

	public void setSirfelTFattura(SirfelTFattura sirfelTFattura) {
		this.sirfelTFattura = sirfelTFattura;
	}
	
	public String getNumeroDocumento() {
		return getId() != null ? getId().getNumeroDocumento() : null;
	}

}