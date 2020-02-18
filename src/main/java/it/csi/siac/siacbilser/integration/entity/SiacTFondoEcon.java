/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_t_fondo_econ database table.
 * 
 */
@Entity
@Table(name="siac_t_fondo_econ")
@NamedQuery(name="SiacTFondoEcon.findAll", query="SELECT s FROM SiacTFondoEcon s")
public class SiacTFondoEcon extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_FONDO_ECON_FONDOECONID_GENERATOR", allocationSize = 1, sequenceName="SIAC_T_FONDO_ECON_FONDOECON_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_FONDO_ECON_FONDOECONID_GENERATOR")
	@Column(name="fondoecon_id")
	private Integer fondoeconId;

	@Column(name="fondoecon_code")
	private String fondoeconCode;

	@Column(name="fondoecon_desc")
	private String fondoeconDesc;

	@Column(name="fondoecon_rendi_uffpers")
	private String fondoeconRendiUffpers;

	//bi-directional many-to-one association to SiacRFondoEconBilElem
	@OneToMany(mappedBy="siacTFondoEcon")
	private List<SiacRFondoEconBilElem> siacRFondoEconBilElems;

	//bi-directional many-to-one association to SiacRFondoEconMovgest
	@OneToMany(mappedBy="siacTFondoEcon")
	private List<SiacRFondoEconMovgest> siacRFondoEconMovgests;

	//bi-directional many-to-one association to SiacTCassaEcon
	@ManyToOne
	@JoinColumn(name="cassaecon_id")
	private SiacTCassaEcon siacTCassaEcon;

	public SiacTFondoEcon() {
	}

	public Integer getFondoeconId() {
		return this.fondoeconId;
	}

	public void setFondoeconId(Integer fondoeconId) {
		this.fondoeconId = fondoeconId;
	}

	public String getFondoeconCode() {
		return this.fondoeconCode;
	}

	public void setFondoeconCode(String fondoeconCode) {
		this.fondoeconCode = fondoeconCode;
	}

	public String getFondoeconDesc() {
		return this.fondoeconDesc;
	}

	public void setFondoeconDesc(String fondoeconDesc) {
		this.fondoeconDesc = fondoeconDesc;
	}

	public String getFondoeconRendiUffpers() {
		return fondoeconRendiUffpers;
	}

	public void setFondoeconRendiUffpers(String fondoeconRendiUffpers) {
		this.fondoeconRendiUffpers = fondoeconRendiUffpers;
	}

	public List<SiacRFondoEconBilElem> getSiacRFondoEconBilElems() {
		return this.siacRFondoEconBilElems;
	}

	public void setSiacRFondoEconBilElems(List<SiacRFondoEconBilElem> siacRFondoEconBilElems) {
		this.siacRFondoEconBilElems = siacRFondoEconBilElems;
	}

	public SiacRFondoEconBilElem addSiacRFondoEconBilElem(SiacRFondoEconBilElem siacRFondoEconBilElem) {
		getSiacRFondoEconBilElems().add(siacRFondoEconBilElem);
		siacRFondoEconBilElem.setSiacTFondoEcon(this);

		return siacRFondoEconBilElem;
	}

	public SiacRFondoEconBilElem removeSiacRFondoEconBilElem(SiacRFondoEconBilElem siacRFondoEconBilElem) {
		getSiacRFondoEconBilElems().remove(siacRFondoEconBilElem);
		siacRFondoEconBilElem.setSiacTFondoEcon(null);

		return siacRFondoEconBilElem;
	}

	public List<SiacRFondoEconMovgest> getSiacRFondoEconMovgests() {
		return this.siacRFondoEconMovgests;
	}

	public void setSiacRFondoEconMovgests(List<SiacRFondoEconMovgest> siacRFondoEconMovgests) {
		this.siacRFondoEconMovgests = siacRFondoEconMovgests;
	}

	public SiacRFondoEconMovgest addSiacRFondoEconMovgest(SiacRFondoEconMovgest siacRFondoEconMovgest) {
		getSiacRFondoEconMovgests().add(siacRFondoEconMovgest);
		siacRFondoEconMovgest.setSiacTFondoEcon(this);

		return siacRFondoEconMovgest;
	}

	public SiacRFondoEconMovgest removeSiacRFondoEconMovgest(SiacRFondoEconMovgest siacRFondoEconMovgest) {
		getSiacRFondoEconMovgests().remove(siacRFondoEconMovgest);
		siacRFondoEconMovgest.setSiacTFondoEcon(null);

		return siacRFondoEconMovgest;
	}

	public SiacTCassaEcon getSiacTCassaEcon() {
		return this.siacTCassaEcon;
	}

	public void setSiacTCassaEcon(SiacTCassaEcon siacTCassaEcon) {
		this.siacTCassaEcon = siacTCassaEcon;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return fondoeconId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.fondoeconId = uid;
		
	}
}