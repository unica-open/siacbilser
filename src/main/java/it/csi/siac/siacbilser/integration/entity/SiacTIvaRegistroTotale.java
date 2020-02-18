/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_t_iva_registro_totale database table.
 * 
 */
@Entity
@Table(name="siac_t_iva_registro_totale")
@NamedQuery(name="SiacTIvaRegistroTotale.findAll", query="SELECT s FROM SiacTIvaRegistroTotale s")
public class SiacTIvaRegistroTotale extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_IVA_REGISTRO_TOTALE_IVAST_TOT_ID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_IVA_REGISTRO_TOTALE_IVAST_TOT_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_IVA_REGISTRO_TOTALE_IVAST_TOT_ID_GENERATOR")
	@Column(name="ivast_tot_id")
	private Integer ivastTotId;

	private BigDecimal totimponibiledef;

	private BigDecimal totimponibileprovv;

	private BigDecimal totivadef;

	private BigDecimal totivaprovv;

	private BigDecimal totivadetraibiledef;

	private BigDecimal totivadetraibileprovv;

	private BigDecimal totivaindetraibiledef;

	private BigDecimal totivaindetraibileprovv;

	@ManyToOne
	@JoinColumn(name="ivareg_id")
	private SiacTIvaRegistro siacTIvaRegistro;
	
	@ManyToOne
	@JoinColumn(name="periodo_id")
	private SiacTPeriodo siacTPeriodo;
	
	//bi-directional many-to-one association to SiacTIvaAliquota
	@ManyToOne
	@JoinColumn(name="ivaaliquota_id")
	private SiacTIvaAliquota siacTIvaAliquota;

	//bi-directional many-to-one association to SiacTIvaAttivita
	@ManyToOne
	@JoinColumn(name="ivaatt_id")
	private SiacTIvaAttivita siacTIvaAttivita;

	public SiacTIvaRegistroTotale() {
	}

	/**
	 * @return the ivastTotId
	 */
	public Integer getIvastTotId() {
		return ivastTotId;
	}

	/**
	 * @param ivastTotId the ivastTotId to set
	 */
	public void setIvastTotId(Integer ivastTotId) {
		this.ivastTotId = ivastTotId;
	}

	/**
	 * @return the totimponibiledef
	 */
	public BigDecimal getTotimponibiledef() {
		return totimponibiledef;
	}

	/**
	 * @param totimponibiledef the totimponibiledef to set
	 */
	public void setTotimponibiledef(BigDecimal totimponibiledef) {
		this.totimponibiledef = totimponibiledef;
	}

	/**
	 * @return the totimponibileprovv
	 */
	public BigDecimal getTotimponibileprovv() {
		return totimponibileprovv;
	}

	/**
	 * @param totimponibileprovv the totimponibileprovv to set
	 */
	public void setTotimponibileprovv(BigDecimal totimponibileprovv) {
		this.totimponibileprovv = totimponibileprovv;
	}

	/**
	 * @return the totivadef
	 */
	public BigDecimal getTotivadef() {
		return totivadef;
	}

	/**
	 * @param totivadef the totivadef to set
	 */
	public void setTotivadef(BigDecimal totivadef) {
		this.totivadef = totivadef;
	}

	/**
	 * @return the totivaprovv
	 */
	public BigDecimal getTotivaprovv() {
		return totivaprovv;
	}

	/**
	 * @param totivaprovv the totivaprovv to set
	 */
	public void setTotivaprovv(BigDecimal totivaprovv) {
		this.totivaprovv = totivaprovv;
	}

	/**
	 * @return the totivadetraibiledef
	 */
	public BigDecimal getTotivadetraibiledef() {
		return totivadetraibiledef;
	}

	/**
	 * @param totivadetraibiledef the totivadetraibiledef to set
	 */
	public void setTotivadetraibiledef(BigDecimal totivadetraibiledef) {
		this.totivadetraibiledef = totivadetraibiledef;
	}

	/**
	 * @return the totivadetraibileprovv
	 */
	public BigDecimal getTotivadetraibileprovv() {
		return totivadetraibileprovv;
	}

	/**
	 * @param totivadetraibileprovv the totivadetraibileprovv to set
	 */
	public void setTotivadetraibileprovv(BigDecimal totivadetraibileprovv) {
		this.totivadetraibileprovv = totivadetraibileprovv;
	}

	/**
	 * @return the totivaindetraibiledef
	 */
	public BigDecimal getTotivaindetraibiledef() {
		return totivaindetraibiledef;
	}

	/**
	 * @param totivaindetraibiledef the totivaindetraibiledef to set
	 */
	public void setTotivaindetraibiledef(BigDecimal totivaindetraibiledef) {
		this.totivaindetraibiledef = totivaindetraibiledef;
	}

	/**
	 * @return the totivaindetraibileprovv
	 */
	public BigDecimal getTotivaindetraibileprovv() {
		return totivaindetraibileprovv;
	}

	/**
	 * @param totivaindetraibileprovv the totivaindetraibileprovv to set
	 */
	public void setTotivaindetraibileprovv(BigDecimal totivaindetraibileprovv) {
		this.totivaindetraibileprovv = totivaindetraibileprovv;
	}

	/**
	 * @return the siacTIvaRegistro
	 */
	public SiacTIvaRegistro getSiacTIvaRegistro() {
		return siacTIvaRegistro;
	}

	/**
	 * @param siacTIvaRegistro the siacTIvaRegistro to set
	 */
	public void setSiacTIvaRegistro(SiacTIvaRegistro siacTIvaRegistro) {
		this.siacTIvaRegistro = siacTIvaRegistro;
	}

	/**
	 * @return the siacTPeriodo
	 */
	public SiacTPeriodo getSiacTPeriodo() {
		return siacTPeriodo;
	}

	/**
	 * @param siacTPeriodo the siacTPeriodo to set
	 */
	public void setSiacTPeriodo(SiacTPeriodo siacTPeriodo) {
		this.siacTPeriodo = siacTPeriodo;
	}

	/**
	 * @return the siacTIvaAliquota
	 */
	public SiacTIvaAliquota getSiacTIvaAliquota() {
		return siacTIvaAliquota;
	}

	/**
	 * @param siacTIvaAliquota the siacTIvaAliquota to set
	 */
	public void setSiacTIvaAliquota(SiacTIvaAliquota siacTIvaAliquota) {
		this.siacTIvaAliquota = siacTIvaAliquota;
	}

	/**
	 * @return the siacTIvaAttivita
	 */
	public SiacTIvaAttivita getSiacTIvaAttivita() {
		return siacTIvaAttivita;
	}

	/**
	 * @param siacTIvaAttivita the siacTIvaAttivita to set
	 */
	public void setSiacTIvaAttivita(SiacTIvaAttivita siacTIvaAttivita) {
		this.siacTIvaAttivita = siacTIvaAttivita;
	}

	@Override
	public Integer getUid() {
		return ivastTotId;
	}

	@Override
	public void setUid(Integer uid) {
		this.ivastTotId = uid;
	}

}