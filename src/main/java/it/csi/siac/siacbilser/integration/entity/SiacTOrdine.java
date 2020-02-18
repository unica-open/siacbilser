/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_t_ordine database table.
 * 
 */
@Entity
@Table(name="siac_t_ordine")
@NamedQuery(name="SiacTOrdine.findAll", query="SELECT s FROM SiacTOrdine s")
public class SiacTOrdine extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_ORDINE_ORDINEID_GENERATOR", allocationSize=1,  sequenceName="SIAC_T_ORDINE_ORDINE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_ORDINE_ORDINEID_GENERATOR")
	@Column(name="ordine_id")
	private Integer ordineId;
	
	@Column(name="ordine_numero")
	private String ordineNumero;

	//bi-directional many-to-one association to SiacRDocOrdine
	@OneToMany(mappedBy="siacTOrdine", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRDocOrdine> siacRDocOrdines;

	public SiacTOrdine() {
	}

	public Integer getOrdineId() {
		return this.ordineId;
	}

	public void setOrdineId(Integer ordineId) {
		this.ordineId = ordineId;
	}
	
	public String getOrdineNumero() {
		return ordineNumero;
	}

	public void setOrdineNumero(String ordineNumero) {
		this.ordineNumero = ordineNumero;
	}

	public List<SiacRDocOrdine> getSiacRDocOrdines() {
		return this.siacRDocOrdines;
	}

	public void setSiacRDocOrdines(List<SiacRDocOrdine> siacRDocOrdines) {
		this.siacRDocOrdines = siacRDocOrdines;
	}

	public SiacRDocOrdine addSiacRDocOrdine(SiacRDocOrdine siacRDocOrdine) {
		getSiacRDocOrdines().add(siacRDocOrdine);
		siacRDocOrdine.setSiacTOrdine(this);

		return siacRDocOrdine;
	}

	public SiacRDocOrdine removeSiacRDocOrdine(SiacRDocOrdine siacRDocOrdine) {
		getSiacRDocOrdines().remove(siacRDocOrdine);
		siacRDocOrdine.setSiacTOrdine(null);

		return siacRDocOrdine;
	}

	@Override
	public Integer getUid() {
		return this.ordineId;
	}

	@Override
	public void setUid(Integer uid) {
		this.ordineId = uid;
	}

}