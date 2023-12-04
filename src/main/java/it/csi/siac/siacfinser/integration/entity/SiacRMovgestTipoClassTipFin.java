/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;

@Entity
@Table(name = "siac_r_movgest_tipo_class_tip")
public class SiacRMovgestTipoClassTipFin extends SiacTEnteBase {

	/** Per la serializzazione */
	private static final long serialVersionUID = -218455529199580281L;


	@Id
	@Column(name = "movgest_tipo_classif_tipo_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FIN_MOVGEST_TIPO_CLASS_TIPO")
	@SequenceGenerator(name = "SEQ_FIN_MOVGEST_TIPO_CLASS_TIPO", sequenceName = "siac_r_movgest_tipo_class_tip_movgest_tipo_classif_tipo_id_seq")
	private int uid;

	
	@ManyToOne
	@JoinColumn(name = "movgest_tipo_id")
	private SiacDMovgestTipoFin tipoMovimentoGestione;

	@ManyToOne
	@JoinColumn(name = "classif_tipo_id")
	private SiacTipoClassificatore tipoClassificatore;

	@Override
	public Integer getUid() {
		return uid;
	}

	@Override
	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public SiacDMovgestTipoFin getTipoMovimentoGestione() {
		return tipoMovimentoGestione;
	}

	public void setTipoMovimentoGestione(SiacDMovgestTipoFin tipoMovimentoGestione) {
		this.tipoMovimentoGestione = tipoMovimentoGestione;
	}

	public SiacTipoClassificatore getTipoClassificatore() {
		return tipoClassificatore;
	}

	public void setTipoClassificatore(SiacTipoClassificatore tipoClassificatore) {
		this.tipoClassificatore = tipoClassificatore;
	}


	
	
}
