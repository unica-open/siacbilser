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
 * The persistent class for the siac_t_prima_nota_ratei_risconti database table.
 * 
 */
@Entity
@Table(name="siac_t_prima_nota_ratei_risconti")
@NamedQuery(name="SiacTPrimaNotaRateiRisconti.findAll", query="SELECT s FROM SiacTPrimaNotaRateiRisconti s")
public class SiacTPrimaNotaRateiRisconti extends SiacTEnteBase {
	private static final long serialVersionUID = -1390580532509044228L;


	@Id
	@SequenceGenerator(name="siac_t_prima_nota_ratei_risconti_pnotarr_id_seq_GENERATOR", allocationSize=1, sequenceName="siac_t_prima_nota_ratei_risconti_pnotarr_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="siac_t_prima_nota_ratei_risconti_pnotarr_id_seq_GENERATOR")
	@Column(name="pnotarr_id")
	private Integer pnotarrId;
	
	
	@Column(name="anno")
	private Integer anno;
	
	@Column(name="importo")
	private BigDecimal importo;
	
	
	@ManyToOne
	@JoinColumn(name="pnota_id")
	private SiacTPrimaNota siacTPrimaNota;
	
	//bi-directional many-to-one association to SiacTPrimaNota
	@ManyToOne
	@JoinColumn(name="pnota_rel_tipo_id")
	private SiacDPrimaNotaRelTipo siacDPrimaNotaRelTipo;
	
	
	/**
	 * @return the pnotarrId
	 */
	public Integer getPnotarrId() {
		return pnotarrId;
	}

	/**
	 * @param pnotarrId the pnotarrId to set
	 */
	public void setPnotarrId(Integer pnotarrId) {
		this.pnotarrId = pnotarrId;
	}

	/**
	 * @return the anno
	 */
	public Integer getAnno() {
		return anno;
	}

	/**
	 * @param anno the anno to set
	 */
	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	
	/**
	 * @return the importo
	 */
	public BigDecimal getImporto() {
		return importo;
	}

	/**
	 * @param importo the importo to set
	 */
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}

	/**
	 * @return the siacTPrimaNota
	 */
	public SiacTPrimaNota getSiacTPrimaNota() {
		return siacTPrimaNota;
	}

	/**
	 * @param siacTPrimaNota the siacTPrimaNota to set
	 */
	public void setSiacTPrimaNota(SiacTPrimaNota siacTPrimaNota) {
		this.siacTPrimaNota = siacTPrimaNota;
	}
	
	/**
	 * @return the siacDPrimaNotaRelTipo
	 */
	public SiacDPrimaNotaRelTipo getSiacDPrimaNotaRelTipo() {
		return siacDPrimaNotaRelTipo;
	}

	/**
	 * @param siacDPrimaNotaRelTipo the siacDPrimaNotaRelTipo to set
	 */
	public void setSiacDPrimaNotaRelTipo(SiacDPrimaNotaRelTipo siacDPrimaNotaRelTipo) {
		this.siacDPrimaNotaRelTipo = siacDPrimaNotaRelTipo;
	}
	

	@Override
	public Integer getUid() {
		return pnotarrId;
	}

	@Override
	public void setUid(Integer uid) {
		this.pnotarrId = uid;
	}

}