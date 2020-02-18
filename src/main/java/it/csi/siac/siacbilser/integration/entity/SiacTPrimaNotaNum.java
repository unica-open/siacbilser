/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

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
import javax.persistence.Version;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_prima_nota database table.
 * 
 */
@Entity
@Table(name="siac_t_prima_nota_num")
@NamedQuery(name="SiacTPrimaNotaNum.findAll", query="SELECT s FROM SiacTPrimaNotaNum s")
public class SiacTPrimaNotaNum extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The pnota num id. */
	@Id
	@SequenceGenerator(name="SIAC_T_PRIMA_NOTA_PNOTANUMID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_PRIMA_NOTA_NUM_PNOTA_NUM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_PRIMA_NOTA_PNOTANUMID_GENERATOR")
	@Column(name="pnota_num_id")
	private Integer pnotaNumId;
	
	/** The pnota anno. */
	@Version
	@Column(name="pnota_anno")
	private String pnotaAnno;
	
	/** The pnota numero. */
	@Version
	@Column(name="pnota_numero")
	private Integer pnotaNumero;
	
	//bi-directional many-to-one association to SiacDAmbito
	@ManyToOne
	@JoinColumn(name="ambito_id")
	private SiacDAmbito siacDAmbito;

//	//bi-directional many-to-one association to SiacTDoc
//	/** The siac t pnota. */
//	@ManyToOne
//	@JoinColumn(name="pnota_id")
//	private SiacTPrimaNota siacTPrimaNota;
	
	/**
	 * Instantiates a new siac t predoc num.
	 */
	public SiacTPrimaNotaNum() {
	}
	/**
	 * @return the pnotaNumId
	 */
	public Integer getPnotaNumId() {
		return pnotaNumId;
	}
	/**
	 * @param pnotaNumId the pnotaNumId to set
	 */
	public void setPnotaNumId(Integer pnotaNumId) {
		this.pnotaNumId = pnotaNumId;
	}
	/**
	 * @return the pnotaAnno
	 */
	public String getPnotaAnno() {
		return pnotaAnno;
	}
	/**
	 * @param pnotaAnno the pnotaAnno to set
	 */
	public void setPnotaAnno(String pnotaAnno) {
		this.pnotaAnno = pnotaAnno;
	}
	/**
	 * @return the pnotaNumero
	 */
	public Integer getPnotaNumero() {
		return pnotaNumero;
	}
	/**
	 * @param pnotaNumero the pnotaNumero to set
	 */
	public void setPnotaNumero(Integer pnotaNumero) {
		this.pnotaNumero = pnotaNumero;
	}
	
	public SiacDAmbito getSiacDAmbito() {
		return siacDAmbito;
	}

	public void setSiacDAmbito(SiacDAmbito siacDAmbito) {
		this.siacDAmbito = siacDAmbito;
	}
//	/**
//	 * @return the siacTPrimaNota
//	 */
//	public SiacTPrimaNota getSiacTPrimaNota() {
//		return siacTPrimaNota;
//	}
//	/**
//	 * @param siacTPrimaNota the siacTPrimaNota to set
//	 */
//	public void setSiacTPrimaNota(SiacTPrimaNota siacTPrimaNota) {
//		this.siacTPrimaNota = siacTPrimaNota;
//	}
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return pnotaNumId;
	}
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.pnotaNumId = uid;		
	}


}