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
 * The persistent class for the siac_d_cespiti_categoria database table.
 * 
 */
@Entity
@Table(name="siac_t_cespiti_elenco_dismissioni_num")
@NamedQuery(name="SiacTCespitiElencoDismissioniNum.findAll", query="SELECT s FROM SiacTCespitiElencoDismissioniNum s")
public class SiacTCespitiElencoDismissioniNum extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="siac_t_cespiti_elenco_dismissioni_num_idGENERATOR", allocationSize=1, sequenceName="siac_t_cespiti_elenco_dismissioni_elenco_dismissioni_num_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="siac_t_cespiti_elenco_dismissioni_num_idGENERATOR")
	@Column(name="elenco_dismissioni_num_id")
	private Integer elencoDismissioniNumId;
	
	@Column(name="elenco_dismissioni_anno")
	private Integer elencoDismissioniAnno;

	@Version
	@Column(name="elenco_dismissioni_numero")
	private Integer elencoDismissioniNumero;
	
	/**
	 * Gets the num inventario id.
	 *
	 * @return the numInventarioId
	 */
	public Integer getElencoDismissioniNumId() {
		return elencoDismissioniNumId;
	}

	/**
	 * Sets the num inventario id.
	 *
	 * @param numInventarioId the numInventarioId to set
	 */
	public void setElencoDismissioniNumId(Integer elencoDismissioniNumId) {
		this.elencoDismissioniNumId = elencoDismissioniNumId;
	}


	/**
	 * @return the elencoDismissioniAnno
	 */
	public Integer getElencoDismissioniAnno() {
		return elencoDismissioniAnno;
	}

	/**
	 * @param elencoDismissioniAnno the elencoDismissioniAnno to set
	 */
	public void setElencoDismissioniAnno(Integer elencoDismissioniAnno) {
		this.elencoDismissioniAnno = elencoDismissioniAnno;
	}

	/**
	 * @return the elencoDismissioniNumero
	 */
	public Integer getElencoDismissioniNumero() {
		return elencoDismissioniNumero;
	}

	/**
	 * @param elencoDismissioniNumero the elencoDismissioniNumero to set
	 */
	public void setElencoDismissioniNumero(Integer elencoDismissioniNumero) {
		this.elencoDismissioniNumero = elencoDismissioniNumero;
	}

	@Override
	public Integer getUid() {
		return this.elencoDismissioniNumId;
	}

	@Override
	public void setUid(Integer uid) {
		this.elencoDismissioniNumId = uid;
	}

}