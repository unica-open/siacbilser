/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.Date;
import java.util.List;

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
 * The persistent class for the siac_t_operazione_asincrona database table.
 * 
 */
@Entity
@Table(name="siac_t_operazione_asincrona")
@NamedQuery(name="SiacTOperazioneAsincrona.findAll", query="SELECT s FROM SiacTOperazioneAsincrona s")
public class SiacTOperazioneAsincrona extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_OPERAZIONE_ASINCRONA_OPASID_GENERATOR", allocationSize = 1, sequenceName="SIAC_T_OPERAZIONE_ASINCRONA_OPAS_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_OPERAZIONE_ASINCRONA_OPASID_GENERATOR")
	@Column(name="opas_id")
	private Integer opasId;

	@Column(name="opas_avvio")
	private Date opasAvvio;

	@Column(name="opas_fine")
	private Date opasFine;

	@Column(name="opas_msg")
	private String opasMsg;

	@Column(name="opas_msg_notificato")
	private Boolean opasMsgNotificato;

	@Column(name="opas_stato")
	private String opasStato;

	//bi-directional many-to-one association to SiacTAccount
	@ManyToOne
	@JoinColumn(name="account_id")
	private SiacTAccount siacTAccount;

	//bi-directional many-to-one association to SiacTAzione
	@ManyToOne
	@JoinColumn(name="azione_id")
	private SiacTAzione siacTAzione;

	//bi-directional many-to-one association to SiacTOperazioneAsincronaDet
	@OneToMany(mappedBy="siacTOperazioneAsincrona")
	private List<SiacTOperazioneAsincronaDet> siacTOperazioneAsincronaDets;

	public SiacTOperazioneAsincrona() {
	}

	public Integer getOpasId() {
		return this.opasId;
	}

	public void setOpasId(Integer opasId) {
		this.opasId = opasId;
	}

	public Date getOpasAvvio() {
		return this.opasAvvio;
	}

	public void setOpasAvvio( Date opasAvvio) {
		this.opasAvvio = opasAvvio;
	}

	public Date getOpasFine() {
		return this.opasFine;
	}

	public void setOpasFine( Date opasFine) {
		this.opasFine = opasFine;
	}

	public String getOpasMsg() {
		return this.opasMsg;
	}

	public void setOpasMsg(String opasMsg) {
		this.opasMsg = opasMsg;
	}

	public Boolean getOpasMsgNotificato() {
		return this.opasMsgNotificato;
	}

	public void setOpasMsgNotificato(Boolean opasMsgNotificato) {
		this.opasMsgNotificato = opasMsgNotificato;
	}

	public String getOpasStato() {
		return this.opasStato;
	}

	public void setOpasStato(String opasStato) {
		this.opasStato = opasStato;
	}

	public SiacTAccount getSiacTAccount() {
		return this.siacTAccount;
	}

	public void setSiacTAccount(SiacTAccount siacTAccount) {
		this.siacTAccount = siacTAccount;
	}

	public SiacTAzione getSiacTAzione() {
		return this.siacTAzione;
	}

	public void setSiacTAzione(SiacTAzione siacTAzione) {
		this.siacTAzione = siacTAzione;
	}

	public List<SiacTOperazioneAsincronaDet> getSiacTOperazioneAsincronaDets() {
		return this.siacTOperazioneAsincronaDets;
	}

	public void setSiacTOperazioneAsincronaDets(List<SiacTOperazioneAsincronaDet> siacTOperazioneAsincronaDets) {
		this.siacTOperazioneAsincronaDets = siacTOperazioneAsincronaDets;
	}

	public SiacTOperazioneAsincronaDet addSiacTOperazioneAsincronaDet(SiacTOperazioneAsincronaDet siacTOperazioneAsincronaDet) {
		getSiacTOperazioneAsincronaDets().add(siacTOperazioneAsincronaDet);
		siacTOperazioneAsincronaDet.setSiacTOperazioneAsincrona(this);

		return siacTOperazioneAsincronaDet;
	}

	public SiacTOperazioneAsincronaDet removeSiacTOperazioneAsincronaDet(SiacTOperazioneAsincronaDet siacTOperazioneAsincronaDet) {
		getSiacTOperazioneAsincronaDets().remove(siacTOperazioneAsincronaDet);
		siacTOperazioneAsincronaDet.setSiacTOperazioneAsincrona(null);

		return siacTOperazioneAsincronaDet;
	}

	@Override
	public Integer getUid() {
		return opasId;
	}

	@Override
	public void setUid(Integer uid) {
		opasId = uid;
	}

}