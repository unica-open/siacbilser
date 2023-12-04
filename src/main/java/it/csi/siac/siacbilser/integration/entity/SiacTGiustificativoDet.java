/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;
import java.util.Date;
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
 * The persistent class for the siac_t_giustificativo_det database table.
 * 
 */
@Entity
@Table(name="siac_t_giustificativo_det")
@NamedQuery(name="SiacTGiustificativoDet.findAll", query="SELECT s FROM SiacTGiustificativoDet s")
public class SiacTGiustificativoDet extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_GIUSTIFICATIVO_DET_GSTDETID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_GIUSTIFICATIVO_DET_GST_DET_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_GIUSTIFICATIVO_DET_GSTDETID_GENERATOR")
	@Column(name="gst_det_id")
	private Integer gstDetId;

	@Column(name="gst_det_code")
	private String gstDetCode;

	@Column(name="gst_det_desc")
	private String gstDetDesc;

	@Column(name="gst_emissione_data")
	private Date gstEmissioneData;

	@Column(name="gst_importo")
	private BigDecimal gstImporto;

	@Column(name="gst_importo_spettante")
	private BigDecimal gstImportoSpettante;

	@Column(name="gst_importo_valuta")
	private BigDecimal gstImportoValuta;

	@Column(name="gst_incluso_in_pag")
	private String gstInclusoInPag;

	@Column(name="gst_protocollo_anno")
	private String gstProtocolloAnno;

	@Column(name="gst_protocollo_numero")
	private String gstProtocolloNumero;

	@Column(name="gst_tasso_di_cambio")
	private BigDecimal gstTassoDiCambio;
	
	// Lotto P
	// Annotazione inutile, ma posta per uniformita
	@Column(name="quantita")
	private BigDecimal quantita;

	//bi-directional many-to-one association to SiacRGiustificativoStato
	@OneToMany(mappedBy="siacTGiustificativoDet", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRGiustificativoStato> siacRGiustificativoStatos;

	//bi-directional many-to-one association to SiacDGiustificativo
	@ManyToOne
	@JoinColumn(name="giust_id")
	private SiacDGiustificativo siacDGiustificativo;

	//bi-directional many-to-one association to SiacDValuta
	@ManyToOne
	@JoinColumn(name="valuta_id")
	private SiacDValuta siacDValuta;

	//bi-directional many-to-one association to SiacTGiustificativo
	@ManyToOne
	@JoinColumn(name="gst_id")
	private SiacTGiustificativo siacTGiustificativo;

	//bi-directional many-to-one association to SiacTRichiestaEcon
	@ManyToOne
	@JoinColumn(name="ricecon_id")
	private SiacTRichiestaEcon siacTRichiestaEcon;

	public SiacTGiustificativoDet() {
	}

	public Integer getGstDetId() {
		return this.gstDetId;
	}

	public void setGstDetId(Integer gstDetId) {
		this.gstDetId = gstDetId;
	}

	public String getGstDetCode() {
		return this.gstDetCode;
	}

	public void setGstDetCode(String gstDetCode) {
		this.gstDetCode = gstDetCode;
	}

	public String getGstDetDesc() {
		return this.gstDetDesc;
	}

	public void setGstDetDesc(String gstDetDesc) {
		this.gstDetDesc = gstDetDesc;
	}

	public Date getGstEmissioneData() {
		return this.gstEmissioneData;
	}

	public void setGstEmissioneData( Date gstEmissioneData) {
		this.gstEmissioneData = gstEmissioneData;
	}

	public BigDecimal getGstImporto() {
		return this.gstImporto;
	}

	public void setGstImporto(BigDecimal gstImporto) {
		this.gstImporto = gstImporto;
	}

	public BigDecimal getGstImportoSpettante() {
		return this.gstImportoSpettante;
	}

	public void setGstImportoSpettante(BigDecimal gstImportoSpettante) {
		this.gstImportoSpettante = gstImportoSpettante;
	}

	public BigDecimal getGstImportoValuta() {
		return this.gstImportoValuta;
	}

	public void setGstImportoValuta(BigDecimal gstImportoValuta) {
		this.gstImportoValuta = gstImportoValuta;
	}

	public String getGstInclusoInPag() {
		return this.gstInclusoInPag;
	}

	public void setGstInclusoInPag(String gstInclusoInPag) {
		this.gstInclusoInPag = gstInclusoInPag;
	}

	public String getGstProtocolloAnno() {
		return this.gstProtocolloAnno;
	}

	public void setGstProtocolloAnno(String gstProtocolloAnno) {
		this.gstProtocolloAnno = gstProtocolloAnno;
	}

	public String getGstProtocolloNumero() {
		return this.gstProtocolloNumero;
	}

	public void setGstProtocolloNumero(String gstProtocolloNumero) {
		this.gstProtocolloNumero = gstProtocolloNumero;
	}

	public BigDecimal getGstTassoDiCambio() {
		return this.gstTassoDiCambio;
	}

	public void setGstTassoDiCambio(BigDecimal gstTassoDiCambio) {
		this.gstTassoDiCambio = gstTassoDiCambio;
	}

	public BigDecimal getQuantita() {
		return quantita;
	}

	public void setQuantita(BigDecimal quantita) {
		this.quantita = quantita;
	}

	public List<SiacRGiustificativoStato> getSiacRGiustificativoStatos() {
		return this.siacRGiustificativoStatos;
	}

	public void setSiacRGiustificativoStatos(List<SiacRGiustificativoStato> siacRGiustificativoStatos) {
		this.siacRGiustificativoStatos = siacRGiustificativoStatos;
	}

	public SiacRGiustificativoStato addSiacRGiustificativoStato(SiacRGiustificativoStato siacRGiustificativoStato) {
		getSiacRGiustificativoStatos().add(siacRGiustificativoStato);
		siacRGiustificativoStato.setSiacTGiustificativoDet(this);

		return siacRGiustificativoStato;
	}

	public SiacRGiustificativoStato removeSiacRGiustificativoStato(SiacRGiustificativoStato siacRGiustificativoStato) {
		getSiacRGiustificativoStatos().remove(siacRGiustificativoStato);
		siacRGiustificativoStato.setSiacTGiustificativoDet(null);

		return siacRGiustificativoStato;
	}

	public SiacDGiustificativo getSiacDGiustificativo() {
		return this.siacDGiustificativo;
	}

	public void setSiacDGiustificativo(SiacDGiustificativo siacDGiustificativo) {
		this.siacDGiustificativo = siacDGiustificativo;
	}

	public SiacDValuta getSiacDValuta() {
		return this.siacDValuta;
	}

	public void setSiacDValuta(SiacDValuta siacDValuta) {
		this.siacDValuta = siacDValuta;
	}

	public SiacTGiustificativo getSiacTGiustificativo() {
		return this.siacTGiustificativo;
	}

	public void setSiacTGiustificativo(SiacTGiustificativo siacTGiustificativo) {
		this.siacTGiustificativo = siacTGiustificativo;
	}

	public SiacTRichiestaEcon getSiacTRichiestaEcon() {
		return this.siacTRichiestaEcon;
	}

	public void setSiacTRichiestaEcon(SiacTRichiestaEcon siacTRichiestaEcon) {
		this.siacTRichiestaEcon = siacTRichiestaEcon;
	}
	
	@Override
	public Integer getUid() {
		return gstDetId;
	}

	@Override
	public void setUid(Integer uid) {
		this.gstDetId = uid;
		
	}

}