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
 * The persistent class for the siac_d_cespiti_categoria database table.
 * 
 */
@Entity
@Table(name="siac_t_cespiti_variazione")
@NamedQuery(name="SiacTCespitiVariazione.findAll", query="SELECT s FROM SiacTCespitiVariazione s")
public class SiacTCespitiVariazione extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="siac_t_cespiti_variazione_ces_var_idGENERATOR", allocationSize=1, sequenceName="siac_t_cespiti_variazione_ces_var_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="siac_t_cespiti_variazione_ces_var_idGENERATOR")
	@Column(name="ces_var_id")
	private Integer cesVarId;

	@Column(name="ces_var_desc")
	private String cesVarDesc;

	
	@Column(name="ces_var_anno")
	private String cesVarAnno;
	
	@Column(name="ces_var_data")
	private Date cesVarData;
	
	@Column(name="ces_var_importo")
	private BigDecimal cesVarImporto;
	
	@Column(name="flg_tipo_variazione_incr")
	private Boolean flgTipoVariazioneIncr;


	@ManyToOne
	@JoinColumn(name="ces_var_stato_id")
	private SiacDCespitiVariazioneStato siacDCespitiVariazioneStato;
	

	@ManyToOne
	@JoinColumn(name="ces_id")
	private SiacTCespiti siacTCespiti;

	//bi-directional many-to-one association to siacRCespitiVariazionePrimaNota
	@OneToMany(mappedBy="siacTCespitiVariazione", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRCespitiVariazionePrimaNota> siacRCespitiVariazionePrimaNotas;



	
	/**
	 * @return the cesVarId
	 */
	public Integer getCesVarId() {
		return cesVarId;
	}

	/**
	 * @param cesVarId the cesVarId to set
	 */
	public void setCesVarId(Integer cesVarId) {
		this.cesVarId = cesVarId;
	}

	/**
	 * @return the cesVarDesc
	 */
	public String getCesVarDesc() {
		return cesVarDesc;
	}

	/**
	 * @param cesVarDesc the cesVarDesc to set
	 */
	public void setCesVarDesc(String cesVarDesc) {
		this.cesVarDesc = cesVarDesc;
	}

	/**
	 * @return the cesVarAnno
	 */
	public String getCesVarAnno() {
		return cesVarAnno;
	}

	/**
	 * @param cesVarAnno the cesVarAnno to set
	 */
	public void setCesVarAnno(String cesVarAnno) {
		this.cesVarAnno = cesVarAnno;
	}

	/**
	 * @return the cesVarData
	 */
	public Date getCesVarData() {
		return cesVarData;
	}

	/**
	 * @param cesVarData the cesVarData to set
	 */
	public void setCesVarData(Date cesVarData) {
		this.cesVarData = cesVarData;
	}

	/**
	 * @return the cesVarImporto
	 */
	public BigDecimal getCesVarImporto() {
		return cesVarImporto;
	}

	/**
	 * @param cesVarImporto the cesVarImporto to set
	 */
	public void setCesVarImporto(BigDecimal cesVarImporto) {
		this.cesVarImporto = cesVarImporto;
	}

	/**
	 * @return the flgTipoVariazioneIncr
	 */
	public Boolean isFlgTipoVariazioneIncr() {
		return flgTipoVariazioneIncr;
	}

	/**
	 * @param flgTipoVariazioneIncr the flgTipoVariazioneIncr to set
	 */
	public void setFlgTipoVariazioneIncr(Boolean flgTipoVariazioneIncr) {
		this.flgTipoVariazioneIncr = flgTipoVariazioneIncr;
	}

	/**
	 * @return the siacDCespitiVariazioniStati
	 */
	public SiacDCespitiVariazioneStato getSiacDCespitiVariazioneStato() {
		return siacDCespitiVariazioneStato;
	}

	/**
	 * @param siacDCespitiVariazioniStati the siacDCespitiVariazioniStati to set
	 */
	public void setSiacDCespitiVariazioniStati(SiacDCespitiVariazioneStato siacDCespitiVariazioneStato) {
		this.siacDCespitiVariazioneStato = siacDCespitiVariazioneStato;
	}

	/**
	 * @return the siacTCespiti
	 */
	public SiacTCespiti getSiacTCespiti() {
		return siacTCespiti;
	}

	/**
	 * @param siacTCespiti the siacTCespiti to set
	 */
	public void setSiacTCespiti(SiacTCespiti siacTCespiti) {
		this.siacTCespiti = siacTCespiti;
	}

	/**
	 * @return the siacRCespitiVariazionePrimaNotas
	 */
	public List<SiacRCespitiVariazionePrimaNota> getSiacRCespitiVariazionePrimaNotas() {
		return siacRCespitiVariazionePrimaNotas;
	}

	/**
	 * @param siacRCespitiVariazionePrimaNotas the siacRCespitiVariazionePrimaNotas to set
	 */
	public void setSiacRCespitiVariazionePrimaNotas(List<SiacRCespitiVariazionePrimaNota> siacRCespitiVariazionePrimaNotas) {
		this.siacRCespitiVariazionePrimaNotas = siacRCespitiVariazionePrimaNotas;
	}

	/**
	 * @return the flgTipoVariazioneIncr
	 */
	public Boolean getFlgTipoVariazioneIncr() {
		return flgTipoVariazioneIncr;
	}

	/**
	 * @param siacDCespitiVariazioneStato the siacDCespitiVariazioneStato to set
	 */
	public void setSiacDCespitiVariazioneStato(SiacDCespitiVariazioneStato siacDCespitiVariazioneStato) {
		this.siacDCespitiVariazioneStato = siacDCespitiVariazioneStato;
	}

	@Override
	public Integer getUid() {
		return this.cesVarId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cesVarId = uid;
	}

}