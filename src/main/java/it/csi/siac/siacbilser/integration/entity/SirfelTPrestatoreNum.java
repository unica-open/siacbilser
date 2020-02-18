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
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the sirfel_t_prestatore_num database table.
 * 
 */
@Entity
@Table(name="sirfel_t_prestatore_num")
@NamedQuery(name="SirfelTPrestatoreNum.findAll", query="SELECT s FROM SirfelTPrestatoreNum s")
public class SirfelTPrestatoreNum extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIRFEL_T_PRESTATORE_NUM_PRESTATORENUMID_GENERATOR", allocationSize=1, sequenceName="SIRFEL_T_PRESTATORE_NUM_PRESTATORE_NUM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIRFEL_T_PRESTATORE_NUM_PRESTATORENUMID_GENERATOR")
	@Column(name="prestatore_num_id")
	private Integer prestatoreNumId;

	@Version
	private Integer numero;

	public SirfelTPrestatoreNum() {
	}

	public Integer getNumero() {
		return this.numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	@Override
	public Integer getUid() {
		return this.prestatoreNumId;
	}

	@Override
	public void setUid(Integer uid) {
		this.prestatoreNumId = uid;
	}

}