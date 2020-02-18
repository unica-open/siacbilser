/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_d_evento_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_evento_tipo")
@NamedQuery(name="SiacDEventoTipo.findAll", query="SELECT s FROM SiacDEventoTipo s")
public class SiacDEventoTipo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_EVENTO_TIPO_EVENTOTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_EVENTO_TIPO_EVENTO_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_EVENTO_TIPO_EVENTOTIPOID_GENERATOR")
	@Column(name="evento_tipo_id")
	private Integer eventoTipoId;

	@Column(name="evento_tipo_code")
	private String eventoTipoCode;

	@Column(name="evento_tipo_desc")
	private String eventoTipoDesc;
	
	//bi-directional many-to-one association to SiacDEvento
	@OneToMany(mappedBy="siacDEventoTipo")
	private List<SiacDEvento> siacDEventos;

//	//bi-directional many-to-one association to SiacREventoTipoTabella
//	@OneToMany(mappedBy="siacDEventoTipo")
//	private List<SiacREventoTipoTabella> siacREventoTipoTabellas;
	
	//bi-directional many-to-one association to SiacRCausaleEpTipoEventoTipo
	@OneToMany(mappedBy="siacDEventoTipo")
	private List<SiacRCausaleEpTipoEventoTipo> siacRCausaleEpTipoEventoTipos;

	public SiacDEventoTipo() {
	}

	public Integer getEventoTipoId() {
		return this.eventoTipoId;
	}

	public void setEventoTipoId(Integer eventoTipoId) {
		this.eventoTipoId = eventoTipoId;
	}

	

	public String getEventoTipoCode() {
		return this.eventoTipoCode;
	}

	public void setEventoTipoCode(String eventoTipoCode) {
		this.eventoTipoCode = eventoTipoCode;
	}

	public String getEventoTipoDesc() {
		return this.eventoTipoDesc;
	}

	public void setEventoTipoDesc(String eventoTipoDesc) {
		this.eventoTipoDesc = eventoTipoDesc;
	}

	public List<SiacDEvento> getSiacDEventos() {
		return this.siacDEventos;
	}

	public void setSiacDEventos(List<SiacDEvento> siacDEventos) {
		this.siacDEventos = siacDEventos;
	}

	public SiacDEvento addSiacDEvento(SiacDEvento siacDEvento) {
		getSiacDEventos().add(siacDEvento);
		siacDEvento.setSiacDEventoTipo(this);

		return siacDEvento;
	}

	public SiacDEvento removeSiacDEvento(SiacDEvento siacDEvento) {
		getSiacDEventos().remove(siacDEvento);
		siacDEvento.setSiacDEventoTipo(null);

		return siacDEvento;
	}

//	public List<SiacREventoTipoTabella> getSiacREventoTipoTabellas() {
//		return this.siacREventoTipoTabellas;
//	}
//
//	public void setSiacREventoTipoTabellas(List<SiacREventoTipoTabella> siacREventoTipoTabellas) {
//		this.siacREventoTipoTabellas = siacREventoTipoTabellas;
//	}
//
//	public SiacREventoTipoTabella addSiacREventoTipoTabella(SiacREventoTipoTabella siacREventoTipoTabella) {
//		getSiacREventoTipoTabellas().add(siacREventoTipoTabella);
//		siacREventoTipoTabella.setSiacDEventoTipo(this);
//
//		return siacREventoTipoTabella;
//	}
//
//	public SiacREventoTipoTabella removeSiacREventoTipoTabella(SiacREventoTipoTabella siacREventoTipoTabella) {
//		getSiacREventoTipoTabellas().remove(siacREventoTipoTabella);
//		siacREventoTipoTabella.setSiacDEventoTipo(null);
//
//		return siacREventoTipoTabella;
//	}
	
	public List<SiacRCausaleEpTipoEventoTipo> getSiacRCausaleEpTipoEventoTipos() {
		return this.siacRCausaleEpTipoEventoTipos;
	}

	public void setSiacRCausaleEpTipoEventoTipos(List<SiacRCausaleEpTipoEventoTipo> siacRCausaleEpTipoEventoTipos) {
		this.siacRCausaleEpTipoEventoTipos = siacRCausaleEpTipoEventoTipos;
	}

	public SiacRCausaleEpTipoEventoTipo addSiacRCausaleEpTipoEventoTipo(SiacRCausaleEpTipoEventoTipo siacRCausaleEpTipoEventoTipo) {
		getSiacRCausaleEpTipoEventoTipos().add(siacRCausaleEpTipoEventoTipo);
		siacRCausaleEpTipoEventoTipo.setSiacDEventoTipo(this);

		return siacRCausaleEpTipoEventoTipo;
	}

	public SiacRCausaleEpTipoEventoTipo removeSiacRCausaleEpTipoEventoTipo(SiacRCausaleEpTipoEventoTipo siacRCausaleEpTipoEventoTipo) {
		getSiacRCausaleEpTipoEventoTipos().remove(siacRCausaleEpTipoEventoTipo);
		siacRCausaleEpTipoEventoTipo.setSiacDEventoTipo(null);

		return siacRCausaleEpTipoEventoTipo;
	}

	@Override
	public Integer getUid() {
		return this.eventoTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.eventoTipoId = uid;
	}

}