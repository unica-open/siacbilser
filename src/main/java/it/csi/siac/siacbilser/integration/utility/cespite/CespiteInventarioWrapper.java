/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.utility.cespite;

public class CespiteInventarioWrapper {

	private String prefisso;
	private Integer numero;
	private String numeroInventario;
	/**
	 * @return the prefisso
	 */
	public String getPrefisso() {
		return this.prefisso;
	}
	/**
	 * @param prefisso the prefisso to set
	 */
	public void setPrefisso(String prefisso) {
		this.prefisso = prefisso;
	}
	/**
	 * @return the numero
	 */
	public Integer getNumero() {
		return this.numero;
	}
	/**
	 * @param numero the numero to set
	 */
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	/**
	 * @return the numeroInventario
	 */
	public String getNumeroInventario() {
		return this.numeroInventario;
	}
	/**
	 * @param numeroInventario the numeroInventario to set
	 */
	public void setNumeroInventario(String numeroInventario) {
		this.numeroInventario = numeroInventario;
	}
}
