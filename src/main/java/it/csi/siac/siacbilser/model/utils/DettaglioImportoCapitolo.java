/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.model.utils;

import java.math.BigDecimal;

import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita;

/**
 * Classe di helper per il dettaglio dell'importo capitolo
 * @author Marchino Alessandro
 * @version 1.0.0 - 24/09/2019
 *
 */
public class DettaglioImportoCapitolo extends Entita {

	/** Per la serializzazione */
	private static final long serialVersionUID = -4831066373503293944L;
	
	private String flag;
	private BigDecimal importo;

	private Capitolo<?, ?> capitolo;
	private TipoImportoCapitolo tipoImportoCapitolo;
	private Ente ente;
	private Integer anno;
	/**
	 * @return the flag
	 */
	public String getFlag() {
		return this.flag;
	}
	/**
	 * @param flag the flag to set
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}
	/**
	 * @return the importo
	 */
	public BigDecimal getImporto() {
		return this.importo;
	}
	/**
	 * @param importo the importo to set
	 */
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
	/**
	 * @return the capitolo
	 */
	public Capitolo<?, ?> getCapitolo() {
		return this.capitolo;
	}
	/**
	 * @param capitolo the capitolo to set
	 */
	public void setCapitolo(Capitolo<?, ?> capitolo) {
		this.capitolo = capitolo;
	}
	/**
	 * @return the tipoImportoCapitolo
	 */
	public TipoImportoCapitolo getTipoImportoCapitolo() {
		return this.tipoImportoCapitolo;
	}
	/**
	 * @param tipoImportoCapitolo the tipoImportoCapitolo to set
	 */
	public void setTipoImportoCapitolo(TipoImportoCapitolo tipoImportoCapitolo) {
		this.tipoImportoCapitolo = tipoImportoCapitolo;
	}
	/**
	 * @return the ente
	 */
	public Ente getEnte() {
		return this.ente;
	}
	/**
	 * @param ente the ente to set
	 */
	public void setEnte(Ente ente) {
		this.ente = ente;
	}
	/**
	 * @return the anno
	 */
	public Integer getAnno() {
		return this.anno;
	}
	/**
	 * @param anno the anno to set
	 */
	public void setAnno(Integer anno) {
		this.anno = anno;
	}
}
