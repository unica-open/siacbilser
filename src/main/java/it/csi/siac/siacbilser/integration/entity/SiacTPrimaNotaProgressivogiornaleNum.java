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
 * The persistent class for the siac_t_prima_nota_progressivogiornale_num database table.
 * 
 */
@Entity
@Table(name="siac_t_prima_nota_progressivogiornale_num")
@NamedQuery(name="SiacTPrimaNotaProgressivogiornaleNum.findAll", query="SELECT s FROM SiacTPrimaNotaProgressivogiornaleNum s")
public class SiacTPrimaNotaProgressivogiornaleNum extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The pnota num id. */
	@Id
	@SequenceGenerator(name="SIAC_T_PRIMA_NOTA_PNOTAPROGRESSIVOGIORNALENUMID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_PRIMA_NOTA_PROGRESSIVO_PNOTA_PROGRESSIVOGIORNALE_NUM_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_PRIMA_NOTA_PNOTAPROGRESSIVOGIORNALENUMID_GENERATOR")
	@Column(name="pnota_progressivogiornale_num_id")
	private Integer pnotaProgressivoGiornaleNumId;
	
	/** The pnota anno. */
	@Version
	@Column(name="pnota_anno")
	private String pnotaAnno;
	
	/** The pnota numero. */
	@Version
	@Column(name="pnota_progressivogiornale")
	private Integer pnotaProgressivogiornale;
	
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
	public SiacTPrimaNotaProgressivogiornaleNum() {
	}
	/**
	 * @return the pnotaProgressivoGiornaleNumId
	 */
	public Integer getPnotaProgressivoGiornaleNumId() {
		return pnotaProgressivoGiornaleNumId;
	}
	/**
	 * @param pnotaProgressivoGiornaleNumId the pnotaProgressivoGiornaleNumId to set
	 */
	public void setPnotaProgressivoGiornaleNumId(
			Integer pnotaProgressivoGiornaleNumId) {
		this.pnotaProgressivoGiornaleNumId = pnotaProgressivoGiornaleNumId;
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
	 * @return the pnotaProgressivogiornale
	 */
	public Integer getPnotaProgressivogiornale() {
		return pnotaProgressivogiornale;
	}
	/**
	 * @param pnotaProgressivogiornale the pnotaProgressivogiornale to set
	 */
	public void setPnotaProgressivogiornale(Integer pnotaProgressivogiornale) {
		this.pnotaProgressivogiornale = pnotaProgressivogiornale;
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

	@Override
	public Integer getUid() {
		return pnotaProgressivoGiornaleNumId;
	}
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.pnotaProgressivoGiornaleNumId = uid;		
	}


}