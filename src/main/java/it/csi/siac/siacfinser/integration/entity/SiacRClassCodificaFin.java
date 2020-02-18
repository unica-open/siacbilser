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
@Table(name = "siac_r_class")
public class SiacRClassCodificaFin extends SiacTEnteBase {

	/** Per la serializzazione */
	private static final long serialVersionUID = 4638471905255833205L;

	@Id
	@Column(name = "classif_classif_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_R_CLASS")
	@SequenceGenerator(name = "SEQ_R_CLASS", sequenceName = "siac_r_class_classif_classif_id_seq")
	private int uid;

	@Override
	public Integer getUid() {
		return uid;
	}

	@Override
	public void setUid(Integer uid) {
		this.uid = uid;
	}

	@ManyToOne
	@JoinColumn(name = "classif_a_id") 
	private SiacCodifica codifica;

	@ManyToOne
	@JoinColumn(name = "classif_b_id") 
	private SiacCodifica codificaFiglia;

	public SiacCodifica getCodifica() {
		return codifica;
	}

	public void setCodifica(SiacCodifica codifica) {
		this.codifica = codifica;
	}

	public SiacCodifica getCodificaFiglia() {
		return codificaFiglia;
	}

	public void setCodificaFiglia(SiacCodifica codificaFiglia) {
		this.codificaFiglia = codificaFiglia;
	}

}
