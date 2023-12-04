/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

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
 * The persistent class for the siac_t_iva_registro database table.
 * 
 */
@Entity
@Table(name="siac_t_iva_registro")
@NamedQuery(name="SiacTIvaRegistro.findAll", query="SELECT s FROM SiacTIvaRegistro s")
public class SiacTIvaRegistro extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_IVA_REGISTRO_IVAREGID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_IVA_REGISTRO_IVAREG_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_IVA_REGISTRO_IVAREGID_GENERATOR")
	@Column(name="ivareg_id")
	private Integer ivaregId;

	@Column(name="ivareg_code")
	private String ivaregCode;

	@Column(name="ivareg_desc")
	private String ivaregDesc;
	
	@Column(name="ivareg_flagbloccato")
	private Boolean ivaregFlagbloccato;
	
	// SIAC-6276 CR-1179B
	@Column(name="ivareg_flagliquidazioneiva")
	private Boolean ivaregFlagLiquidazioneIva;

	//bi-directional many-to-one association to SiacRIvaRegistroGruppo
	@OneToMany(mappedBy="siacTIvaRegistro",cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRIvaRegistroGruppo> siacRIvaRegistroGruppos;

	//bi-directional many-to-one association to SiacRIvaStampaRegistro
	@OneToMany(mappedBy="siacTIvaRegistro")
	private List<SiacRIvaStampaRegistro> siacRIvaStampaRegistros;

	//bi-directional many-to-one association to SiacDIvaRegistroTipo
	@ManyToOne
	@JoinColumn(name="ivareg_tipo_id")
	private SiacDIvaRegistroTipo siacDIvaRegistroTipo;

	//bi-directional many-to-one association to SiacTSubdocIva
	@OneToMany(mappedBy="siacTIvaRegistro")
	private List<SiacTSubdocIva> siacTSubdocIvas;
	
	//bi-directional many-to-one association to SiacTIvaRegistroTotale
	@OneToMany(mappedBy="siacTIvaRegistro")
	private List<SiacTIvaRegistroTotale> siacTIvaRegistroTotales;

	public SiacTIvaRegistro() {
	}

	public Integer getIvaregId() {
		return this.ivaregId;
	}

	public void setIvaregId(Integer ivaregId) {
		this.ivaregId = ivaregId;
	}

	public String getIvaregCode() {
		return this.ivaregCode;
	}

	public void setIvaregCode(String ivaregCode) {
		this.ivaregCode = ivaregCode;
	}

	public String getIvaregDesc() {
		return this.ivaregDesc;
	}

	public void setIvaregDesc(String ivaregDesc) {
		this.ivaregDesc = ivaregDesc;
	}

	public Boolean getIvaregFlagbloccato() {
		return ivaregFlagbloccato;
	}

	public void setIvaregFlagbloccato(Boolean ivaregFlagbloccato) {
		this.ivaregFlagbloccato = ivaregFlagbloccato;
	}
	
	public Boolean getIvaregFlagLiquidazioneIva() {
		return ivaregFlagLiquidazioneIva;
	}

	public void setIvaregFlagLiquidazioneIva(Boolean ivaregFlagLiquidazioneIva) {
		this.ivaregFlagLiquidazioneIva = ivaregFlagLiquidazioneIva;
	}

	public List<SiacRIvaRegistroGruppo> getSiacRIvaRegistroGruppos() {
		return this.siacRIvaRegistroGruppos;
	}

	public void setSiacRIvaRegistroGruppos(List<SiacRIvaRegistroGruppo> siacRIvaRegistroGruppos) {
		this.siacRIvaRegistroGruppos = siacRIvaRegistroGruppos;
	}

	public SiacRIvaRegistroGruppo addSiacRIvaRegistroGruppo(SiacRIvaRegistroGruppo siacRIvaRegistroGruppo) {
		getSiacRIvaRegistroGruppos().add(siacRIvaRegistroGruppo);
		siacRIvaRegistroGruppo.setSiacTIvaRegistro(this);

		return siacRIvaRegistroGruppo;
	}

	public SiacRIvaRegistroGruppo removeSiacRIvaRegistroGruppo(SiacRIvaRegistroGruppo siacRIvaRegistroGruppo) {
		getSiacRIvaRegistroGruppos().remove(siacRIvaRegistroGruppo);
		siacRIvaRegistroGruppo.setSiacTIvaRegistro(null);

		return siacRIvaRegistroGruppo;
	}

	public List<SiacRIvaStampaRegistro> getSiacRIvaStampaRegistros() {
		return this.siacRIvaStampaRegistros;
	}

	public void setSiacRIvaStampaRegistros(List<SiacRIvaStampaRegistro> siacRIvaStampaRegistros) {
		this.siacRIvaStampaRegistros = siacRIvaStampaRegistros;
	}

	public SiacRIvaStampaRegistro addSiacRIvaStampaRegistro(SiacRIvaStampaRegistro siacRIvaStampaRegistro) {
		getSiacRIvaStampaRegistros().add(siacRIvaStampaRegistro);
		siacRIvaStampaRegistro.setSiacTIvaRegistro(this);

		return siacRIvaStampaRegistro;
	}

	public SiacRIvaStampaRegistro removeSiacRIvaStampaRegistro(SiacRIvaStampaRegistro siacRIvaStampaRegistro) {
		getSiacRIvaStampaRegistros().remove(siacRIvaStampaRegistro);
		siacRIvaStampaRegistro.setSiacTIvaRegistro(null);

		return siacRIvaStampaRegistro;
	}

	public SiacDIvaRegistroTipo getSiacDIvaRegistroTipo() {
		return this.siacDIvaRegistroTipo;
	}

	public void setSiacDIvaRegistroTipo(SiacDIvaRegistroTipo siacDIvaRegistroTipo) {
		this.siacDIvaRegistroTipo = siacDIvaRegistroTipo;
	}

	public List<SiacTSubdocIva> getSiacTSubdocIvas() {
		return this.siacTSubdocIvas;
	}

	public void setSiacTSubdocIvas(List<SiacTSubdocIva> siacTSubdocIvas) {
		this.siacTSubdocIvas = siacTSubdocIvas;
	}

	public SiacTSubdocIva addSiacTSubdocIva(SiacTSubdocIva siacTSubdocIva) {
		getSiacTSubdocIvas().add(siacTSubdocIva);
		siacTSubdocIva.setSiacTIvaRegistro(this);

		return siacTSubdocIva;
	}

	public SiacTSubdocIva removeSiacTSubdocIva(SiacTSubdocIva siacTSubdocIva) {
		getSiacTSubdocIvas().remove(siacTSubdocIva);
		siacTSubdocIva.setSiacTIvaRegistro(null);

		return siacTSubdocIva;
	}
	
	public List<SiacTIvaRegistroTotale> getSiacTIvaRegistroTotales() {
		return this.siacTIvaRegistroTotales;
	}

	public void setSiacTIvaRegistroTotales(List<SiacTIvaRegistroTotale> siacTIvaRegistroTotales) {
		this.siacTIvaRegistroTotales = siacTIvaRegistroTotales;
	}

	public SiacTIvaRegistroTotale addSiacTIvaRegistroTotale(SiacTIvaRegistroTotale siacTIvaRegistroTotale) {
		getSiacTIvaRegistroTotales().add(siacTIvaRegistroTotale);
		siacTIvaRegistroTotale.setSiacTIvaRegistro(this);

		return siacTIvaRegistroTotale;
	}

	public SiacTIvaRegistroTotale removeSiacTIvaRegistroTotale(SiacTIvaRegistroTotale siacTIvaRegistroTotale) {
		getSiacTIvaRegistroTotales().remove(siacTIvaRegistroTotale);
		siacTIvaRegistroTotale.setSiacTIvaRegistro(null);

		return siacTIvaRegistroTotale;
	}

	@Override
	public Integer getUid() {
		return ivaregId;
	}

	@Override
	public void setUid(Integer uid) {
		this.ivaregId = uid;
	}

}