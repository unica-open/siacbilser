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
 * The persistent class for the siac_t_giustificativo database table.
 * 
 */
@Entity
@Table(name="siac_t_giustificativo")
@NamedQuery(name="SiacTGiustificativo.findAll", query="SELECT s FROM SiacTGiustificativo s")
public class SiacTGiustificativo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_GIUSTIFICATIVO_GSTID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_GIUSTIFICATIVO_GST_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_GIUSTIFICATIVO_GSTID_GENERATOR")
	@Column(name="gst_id")
	private Integer gstId;

	@Column(name="rend_data")
	private Date rendData;

	@Column(name="rend_importo_integrato")
	private BigDecimal rendImportoIntegrato;

	@Column(name="rend_importo_restituito")
	private BigDecimal rendImportoRestituito;

	@Column(name="rend_note")
	private String rendNote;

	@Column(name="unita_organizzativa")
	private String unitaOrganizzativa;

	//bi-directional many-to-one association to SiacTRichiestaEcon
	@ManyToOne
	@JoinColumn(name="ricecon_id")
	private SiacTRichiestaEcon siacTRichiestaEcon;

	//bi-directional many-to-one association to SiacTGiustificativoDet
	@OneToMany(mappedBy="siacTGiustificativo", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacTGiustificativoDet> siacTGiustificativoDets;

	//bi-directional many-to-one association to SiacTMovimento
	@OneToMany(mappedBy="siacTGiustificativo", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacTMovimento> siacTMovimentos;
	
	//bi-directional many-to-one association to SiacRGiustificativoMovgest
	@OneToMany(mappedBy="siacTGiustificativo", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRGiustificativoMovgest> siacRGiustificativoMovgests;
	
	public SiacTGiustificativo() {
	}

	public Integer getGstId() {
		return this.gstId;
	}

	public void setGstId(Integer gstId) {
		this.gstId = gstId;
	}

	public Date getRendData() {
		return this.rendData;
	}

	public void setRendData( Date rendData) {
		this.rendData = rendData;
	}

	public BigDecimal getRendImportoIntegrato() {
		return this.rendImportoIntegrato;
	}

	public void setRendImportoIntegrato(BigDecimal rendImportoIntegrato) {
		this.rendImportoIntegrato = rendImportoIntegrato;
	}

	public BigDecimal getRendImportoRestituito() {
		return this.rendImportoRestituito;
	}

	public void setRendImportoRestituito(BigDecimal rendImportoRestituito) {
		this.rendImportoRestituito = rendImportoRestituito;
	}

	public String getRendNote() {
		return this.rendNote;
	}

	public void setRendNote(String rendNote) {
		this.rendNote = rendNote;
	}

	public String getUnitaOrganizzativa() {
		return this.unitaOrganizzativa;
	}

	public void setUnitaOrganizzativa(String unitaOrganizzativa) {
		this.unitaOrganizzativa = unitaOrganizzativa;
	}

	public SiacTRichiestaEcon getSiacTRichiestaEcon() {
		return this.siacTRichiestaEcon;
	}

	public void setSiacTRichiestaEcon(SiacTRichiestaEcon siacTRichiestaEcon) {
		this.siacTRichiestaEcon = siacTRichiestaEcon;
	}

	public List<SiacTGiustificativoDet> getSiacTGiustificativoDets() {
		return this.siacTGiustificativoDets;
	}

	public void setSiacTGiustificativoDets(List<SiacTGiustificativoDet> siacTGiustificativoDets) {
		this.siacTGiustificativoDets = siacTGiustificativoDets;
	}

	public SiacTGiustificativoDet addSiacTGiustificativoDet(SiacTGiustificativoDet siacTGiustificativoDet) {
		getSiacTGiustificativoDets().add(siacTGiustificativoDet);
		siacTGiustificativoDet.setSiacTGiustificativo(this);

		return siacTGiustificativoDet;
	}

	public SiacTGiustificativoDet removeSiacTGiustificativoDet(SiacTGiustificativoDet siacTGiustificativoDet) {
		getSiacTGiustificativoDets().remove(siacTGiustificativoDet);
		siacTGiustificativoDet.setSiacTGiustificativo(null);

		return siacTGiustificativoDet;
	}

	public List<SiacTMovimento> getSiacTMovimentos() {
		return this.siacTMovimentos;
	}

	public void setSiacTMovimentos(List<SiacTMovimento> siacTMovimentos) {
		this.siacTMovimentos = siacTMovimentos;
	}

	public SiacTMovimento addSiacTMovimento(SiacTMovimento siacTMovimento) {
		getSiacTMovimentos().add(siacTMovimento);
		siacTMovimento.setSiacTGiustificativo(this);

		return siacTMovimento;
	}

	public SiacTMovimento removeSiacTMovimento(SiacTMovimento siacTMovimento) {
		getSiacTMovimentos().remove(siacTMovimento);
		siacTMovimento.setSiacTGiustificativo(null);

		return siacTMovimento;
	}
	
	public List<SiacRGiustificativoMovgest> getSiacRGiustificativoMovgests() {
		return this.siacRGiustificativoMovgests;
	}

	public void setSiacRGiustificativoMovgests(List<SiacRGiustificativoMovgest> siacRGiustificativoMovgests) {
		this.siacRGiustificativoMovgests = siacRGiustificativoMovgests;
	}

	public SiacRGiustificativoMovgest addSiacRGiustificativoMovgest(SiacRGiustificativoMovgest siacRGiustificativoMovgest) {
		getSiacRGiustificativoMovgests().add(siacRGiustificativoMovgest);
		siacRGiustificativoMovgest.setSiacTGiustificativo(this);

		return siacRGiustificativoMovgest;
	}

	public SiacRGiustificativoMovgest removeSiacRGiustificativoMovgest(SiacRGiustificativoMovgest siacRGiustificativoMovgest) {
		getSiacRGiustificativoMovgests().remove(siacRGiustificativoMovgest);
		siacRGiustificativoMovgest.setSiacTGiustificativo(null);

		return siacRGiustificativoMovgest;
	}

	@Override
	public Integer getUid() {
		return gstId;
	}

	@Override
	public void setUid(Integer uid) {
		this.gstId = uid;
		
	}

}