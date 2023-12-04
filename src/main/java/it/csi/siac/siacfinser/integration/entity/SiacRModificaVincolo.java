/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacfinser.integration.entity;

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

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;

@Entity
@Table(name = "siac_r_modifica_vincolo")
@NamedQuery(name="SiacRModificaVincolo.findAll", query="SELECT srmv FROM SiacRModificaVincolo srmv ")
public class SiacRModificaVincolo extends SiacTEnteBase {

	/** per la serializzazione */
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="SIAC_R_MODIFICA_VINCOLO_MODVINC_ID_SEQ_GENERATOR", allocationSize=1, sequenceName="siac_r_modifica_vincolo_modvinc_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MODIFICA_VINCOLO_MODVINC_ID_SEQ_GENERATOR")
	@Column(name="modvinc_id")
	private Integer modvincId;
	
	@ManyToOne
	@JoinColumn(name="mod_id")
	private SiacTModificaFin siacTmodifica;

	@ManyToOne()
	@JoinColumn(name="movgest_ts_r_id")
	private SiacRMovgestTsFin siacRMovgestTs;
	
	@Column(name="modvinc_tipo_operazione")
	private String modvincTipoOperazione;
	
	@Column(name="importo_delta")
	private BigDecimal importoDelta;

	
	public SiacRModificaVincolo() {}
	
	
	/**
	 * @return the modvincId
	 */
	public Integer getModvincId() {
		return modvincId;
	}

	/**
	 * @param modvincId the modvincId to set
	 */
	public void setModvincId(Integer modvincId) {
		this.modvincId = modvincId;
	}

	/**
	 * @return the siacTmodifica
	 */
	public SiacTModificaFin getSiacTmodifica() {
		return siacTmodifica;
	}

	/**
	 * @param siacTmodifica the siacTmodifica to set
	 */
	public void setSiacTmodifica(SiacTModificaFin siacTmodifica) {
		this.siacTmodifica = siacTmodifica;
	}

	/**
	 * @return the siacRMovgestTss
	 */
	public SiacRMovgestTsFin getSiacRMovgestTs() {
		return siacRMovgestTs;
	}

	/**
	 * @param siacRMovgestTss the siacRMovgestTss to set
	 */
	public void setSiacRMovgestTs(SiacRMovgestTsFin siacRMovgestTs) {
		this.siacRMovgestTs = siacRMovgestTs;
	}

	/**
	 * @return the modvincTipoOperazione
	 */
	public String getModvincTipoOperazione() {
		return modvincTipoOperazione;
	}

	/**
	 * @param modvincTipoOperazione the modvincTipoOperazione to set
	 */
	public void setModvincTipoOperazione(String modvincTipoOperazione) {
		this.modvincTipoOperazione = modvincTipoOperazione;
	}

	/**
	 * @return the importoDelta
	 */
	public BigDecimal getImportoDelta() {
		return importoDelta;
	}

	/**
	 * @param importoDelta the importoDelta to set
	 */
	public void setImportoDelta(BigDecimal importoDelta) {
		this.importoDelta = importoDelta;
	}

	@Override
	public Integer getUid() {
		return modvincId;
	}

	@Override
	public void setUid(Integer uid) {
		this.modvincId = uid;
	}

}
