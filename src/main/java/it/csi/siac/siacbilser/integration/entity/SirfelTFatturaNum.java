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
 * The persistent class for the sirfel_t_fattura_num database table.
 * 
 */
@Entity
@Table(name="sirfel_t_fattura_num")
@NamedQuery(name="SirfelTFatturaNum.findAll", query="SELECT s FROM SirfelTFatturaNum s")
public class SirfelTFatturaNum extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIRFEL_T_FATTURA_NUM_FATTURANUMID_GENERATOR", allocationSize=1, sequenceName="SIRFEL_T_FATTURA_NUM_FATTURA_NUM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIRFEL_T_FATTURA_NUM_FATTURANUMID_GENERATOR")
	@Column(name="fattura_num_id")
	private Integer fatturaNumId;

	@Version
	private Integer numero;

	public SirfelTFatturaNum() {
	}

	public Integer getFatturaNumId() {
		return this.fatturaNumId;
	}

	public void setFatturaNumId(Integer fatturaNumId) {
		this.fatturaNumId = fatturaNumId;
	}

	public Integer getNumero() {
		return this.numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	@Override
	public Integer getUid() {
		return this.fatturaNumId;
	}

	@Override
	public void setUid(Integer uid) {
		this.fatturaNumId = uid;
	}

}