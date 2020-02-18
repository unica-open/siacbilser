/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;
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
 * The persistent class for the siac_t_cassa_econ_operaz database table.
 * 
 */
@Entity
@Table(name="siac_t_cassa_econ_operaz")
@NamedQuery(name="SiacTCassaEconOperaz.findAll", query="SELECT s FROM SiacTCassaEconOperaz s")
public class SiacTCassaEconOperaz extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_CASSA_ECON_OPERAZ_CASSAECONOPID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_CASSA_ECON_OPERAZ_CASSAECONOP_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_CASSA_ECON_OPERAZ_CASSAECONOPID_GENERATOR")
	@Column(name="cassaeconop_id")
	private Integer cassaeconopId;

	@Column(name="cassaeconop_importo")
	private BigDecimal cassaeconopImporto;

	@Column(name="cassaeconop_note")
	private String cassaeconopNote;

	@Column(name="cassaeconop_numero")
	private Integer cassaeconopNumero;

	//bi-directional many-to-one association to SiacRCassaEconOperazStato
	@OneToMany(mappedBy="siacTCassaEconOperaz", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRCassaEconOperazStato> siacRCassaEconOperazStatos;

	//bi-directional many-to-one association to SiacRCassaEconOperazTipo
	@OneToMany(mappedBy="siacTCassaEconOperaz", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRCassaEconOperazTipo> siacRCassaEconOperazTipos;

	//bi-directional many-to-one association to SiacDCassaEconModpagTipo
	@ManyToOne
	@JoinColumn(name="cassamodpag_tipo_id")
	private SiacDCassaEconModpagTipo siacDCassaEconModpagTipo;

	//bi-directional many-to-one association to SiacTAttoAmm
	@ManyToOne
	@JoinColumn(name="attoamm_id")
	private SiacTAttoAmm siacTAttoAmm;

	//bi-directional many-to-one association to SiacTBil
	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBil siacTBil;

	//bi-directional many-to-one association to SiacTCassaEcon
	@ManyToOne
	@JoinColumn(name="cassaec_id")
	private SiacTCassaEcon siacTCassaEcon;
	
	public SiacTCassaEconOperaz() {
	}

	public Integer getCassaeconopId() {
		return this.cassaeconopId;
	}

	public void setCassaeconopId(Integer cassaeconopId) {
		this.cassaeconopId = cassaeconopId;
	}

	public BigDecimal getCassaeconopImporto() {
		return this.cassaeconopImporto;
	}

	public void setCassaeconopImporto(BigDecimal cassaeconopImporto) {
		this.cassaeconopImporto = cassaeconopImporto;
	}

	public String getCassaeconopNote() {
		return this.cassaeconopNote;
	}

	public void setCassaeconopNote(String cassaeconopNote) {
		this.cassaeconopNote = cassaeconopNote;
	}

	public Integer getCassaeconopNumero() {
		return this.cassaeconopNumero;
	}

	public void setCassaeconopNumero(Integer cassaeconopNumero) {
		this.cassaeconopNumero = cassaeconopNumero;
	}

	public List<SiacRCassaEconOperazStato> getSiacRCassaEconOperazStatos() {
		return this.siacRCassaEconOperazStatos;
	}

	public void setSiacRCassaEconOperazStatos(List<SiacRCassaEconOperazStato> siacRCassaEconOperazStatos) {
		this.siacRCassaEconOperazStatos = siacRCassaEconOperazStatos;
	}

	public SiacRCassaEconOperazStato addSiacRCassaEconOperazStato(SiacRCassaEconOperazStato siacRCassaEconOperazStato) {
		getSiacRCassaEconOperazStatos().add(siacRCassaEconOperazStato);
		siacRCassaEconOperazStato.setSiacTCassaEconOperaz(this);

		return siacRCassaEconOperazStato;
	}

	public SiacRCassaEconOperazStato removeSiacRCassaEconOperazStato(SiacRCassaEconOperazStato siacRCassaEconOperazStato) {
		getSiacRCassaEconOperazStatos().remove(siacRCassaEconOperazStato);
		siacRCassaEconOperazStato.setSiacTCassaEconOperaz(null);

		return siacRCassaEconOperazStato;
	}

	public List<SiacRCassaEconOperazTipo> getSiacRCassaEconOperazTipos() {
		return this.siacRCassaEconOperazTipos;
	}

	public void setSiacRCassaEconOperazTipos(List<SiacRCassaEconOperazTipo> siacRCassaEconOperazTipos) {
		this.siacRCassaEconOperazTipos = siacRCassaEconOperazTipos;
	}

	public SiacRCassaEconOperazTipo addSiacRCassaEconOperazTipo(SiacRCassaEconOperazTipo siacRCassaEconOperazTipo) {
		getSiacRCassaEconOperazTipos().add(siacRCassaEconOperazTipo);
		siacRCassaEconOperazTipo.setSiacTCassaEconOperaz(this);

		return siacRCassaEconOperazTipo;
	}

	public SiacRCassaEconOperazTipo removeSiacRCassaEconOperazTipo(SiacRCassaEconOperazTipo siacRCassaEconOperazTipo) {
		getSiacRCassaEconOperazTipos().remove(siacRCassaEconOperazTipo);
		siacRCassaEconOperazTipo.setSiacTCassaEconOperaz(null);

		return siacRCassaEconOperazTipo;
	}

	public SiacDCassaEconModpagTipo getSiacDCassaEconModpagTipo() {
		return this.siacDCassaEconModpagTipo;
	}

	public void setSiacDCassaEconModpagTipo(SiacDCassaEconModpagTipo siacDCassaEconModpagTipo) {
		this.siacDCassaEconModpagTipo = siacDCassaEconModpagTipo;
	}

	public SiacTAttoAmm getSiacTAttoAmm() {
		return this.siacTAttoAmm;
	}

	public void setSiacTAttoAmm(SiacTAttoAmm siacTAttoAmm) {
		this.siacTAttoAmm = siacTAttoAmm;
	}

	public SiacTBil getSiacTBil() {
		return this.siacTBil;
	}

	public void setSiacTBil(SiacTBil siacTBil) {
		this.siacTBil = siacTBil;
	}

	public SiacTCassaEcon getSiacTCassaEcon() {
		return this.siacTCassaEcon;
	}

	public void setSiacTCassaEcon(SiacTCassaEcon siacTCassaEcon) {
		this.siacTCassaEcon = siacTCassaEcon;
	}

	@Override
	public Integer getUid() {
		return cassaeconopId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cassaeconopId = uid;
		
	}

}