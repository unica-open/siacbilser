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
@Table(name="siac_t_cespiti_num_inventario")
@NamedQuery(name="SiacTCespitiNumInventario.findAll", query="SELECT s FROM SiacTCespitiNumInventario s")
public class SiacTCespitiNumInventario extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="siac_t_cespiti_num_inventario_idGENERATOR", allocationSize=1, sequenceName="siac_t_cespiti_num_inventario_num_inventario_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="siac_t_cespiti_num_inventario_idGENERATOR")
	@Column(name="num_inventario_id")
	private Integer numInventarioId;
	
	@Column(name="num_inventario_prefisso")
	private String numInventarioPrefisso;

	@Version
	@Column(name="num_inventario_numero")
	private Integer numInventarioNumero;
	
	/**
	 * Gets the num inventario id.
	 *
	 * @return the numInventarioId
	 */
	public Integer getNumInventarioId() {
		return numInventarioId;
	}

	/**
	 * Sets the num inventario id.
	 *
	 * @param numInventarioId the numInventarioId to set
	 */
	public void setNumInventarioId(Integer numInventarioId) {
		this.numInventarioId = numInventarioId;
	}

	/**
	 * Gets the num inventario prefisso.
	 *
	 * @return the numInventarioPrefisso
	 */
	public String getNumInventarioPrefisso() {
		return numInventarioPrefisso;
	}

	/**
	 * Sets the num inventario prefisso.
	 *
	 * @param numInventarioPrefisso the numInventarioPrefisso to set
	 */
	public void setNumInventarioPrefisso(String numInventarioPrefisso) {
		this.numInventarioPrefisso = numInventarioPrefisso;
	}

	/**
	 * Gets the num inventario numero.
	 *
	 * @return the subdocivaNumero
	 */
	public Integer getNumInventarioNumero() {
		return numInventarioNumero;
	}

	/**
	 * Sets the num inventario numero.
	 *
	 * @param numInventarioNumero the new num inventario numero
	 */
	public void setNumInventarioNumero(Integer numInventarioNumero) {
		this.numInventarioNumero = numInventarioNumero;
	}

	@Override
	public Integer getUid() {
		return this.numInventarioId;
	}

	@Override
	public void setUid(Integer uid) {
		this.numInventarioId = uid;
	}

}