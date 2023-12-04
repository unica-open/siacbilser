/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.attiamministrativi.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilser.model.BILDataDictionary;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

/**
 * Rappresenta il modello per il flusso degli Atti Amministrativi che vengono caricati dal file
 * 
 * @author Nazha Ahmad
 * @author Marchino Alessandro
 * 
 * @version 1.1.0 - 10/11/2015 - gestione modifica flusso
 *
 */
@XmlType(namespace = BILDataDictionary.NAMESPACE)
public class AttoAmministrativoElab extends AttoAmministrativo {

	private static final long serialVersionUID = 5893922179664624516L;
	
	private String tipoDiVariazione; // I=insert, U=update
	private String codiceIstat; // da trascodificare vs l'id ente ,permette di ottenere l'ente
	private Date dataProposta; // yyyyMMdd vuoto per atti comune
	private Date dataApprovazione; // yyyyMMddhh24miss
	private Date dataEsecutivita; // yyyyMMdd
	private Date dataCreazione; // yyyyMMddhh24miss
	private StrutturaAmministrativoContabile sacCentroDiCosto;
	private StrutturaAmministrativoContabile sacCentroDiResponsabilita;
    private String statoOperativoInput;

	private Bilancio bilancio;
	
	// CR-2547
	private Integer annoAttoChiave;
	private Integer numeroAttoChiave;
	private TipoAtto tipoAttoChiave;
	private StrutturaAmministrativoContabile sacCentroDiCostoChiave;
	private StrutturaAmministrativoContabile sacCentroDiResponsabilitaChiave;
	private StrutturaAmministrativoContabile strutturaAmmContabileChiave;
	private String identificativo;
	private String dirigenteResponsabile;
	private String trasparenza;
	
	public AttoAmministrativoElab() {
		super();
	}

	/**
	 * @return the tipoDiVariazione
	 */
	public String getTipoDiVariazione() {
		return tipoDiVariazione;
	}

	/**
	 * @param tipoDiVariazione the tipoDiVariazione to set
	 */
	public void setTipoDiVariazione(String tipoDiVariazione) {
		this.tipoDiVariazione = tipoDiVariazione;
	}

	/**
	 * @return the codiceIstat
	 */
	public String getCodiceIstat() {
		return codiceIstat;
	}

	/**
	 * @param codiceIstat the codiceIstat to set
	 */
	public void setCodiceIstat(String codiceIstat) {
		this.codiceIstat = codiceIstat;
	}

	/**
	 * @return the dataProposta
	 */
	public Date getDataProposta() {
		return dataProposta == null ? null : new Date(dataProposta.getTime());
	}

	/**
	 * @param dataProposta the dataProposta to set
	 */
	public void setDataProposta(Date dataProposta) {
		this.dataProposta = dataProposta == null ? null : new Date(dataProposta.getTime());
	}

	/**
	 * @return the dataApprovazione
	 */
	public Date getDataApprovazione() {
		return dataApprovazione == null ? null : new Date(dataApprovazione.getTime());
	}

	/**
	 * @param dataApprovazione the dataApprovazione to set
	 */
	public void setDataApprovazione(Date dataApprovazione) {
		this.dataApprovazione = dataApprovazione == null ? null : new Date(dataApprovazione.getTime());
	}

	/**
	 * @return the dataEsecutivita
	 */
	public Date getDataEsecutivita() {
		return dataEsecutivita == null ? null : new Date(dataEsecutivita.getTime());
	}

	/**
	 * @param dataEsecutivita the dataEsecutivita to set
	 */
	public void setDataEsecutivita(Date dataEsecutivita) {
		this.dataEsecutivita = dataEsecutivita == null ? null : new Date(dataEsecutivita.getTime());
	}

	/**
	 * @return the dataCreazione
	 */
	public Date getDataCreazione() {
		return dataCreazione == null ? null : new Date(dataCreazione.getTime());
	}

	/**
	 * @param dataCreazione the dataCreazione to set
	 */
	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione == null ? null : new Date(dataCreazione.getTime());
	}

	/**
	 * @return the sacCentroDiCosto
	 */
	public StrutturaAmministrativoContabile getSacCentroDiCosto() {
		return sacCentroDiCosto;
	}

	/**
	 * @param sacCentroDiCosto the sacCentroDiCosto to set
	 */
	public void setSacCentroDiCosto(StrutturaAmministrativoContabile sacCentroDiCosto) {
		this.sacCentroDiCosto = sacCentroDiCosto;
	}

	/**
	 * @return the sacCentroDiResponsabilita
	 */
	public StrutturaAmministrativoContabile getSacCentroDiResponsabilita() {
		return sacCentroDiResponsabilita;
	}

	/**
	 * @param sacCentroDiResponsabilita the sacCentroDiResponsabilita to set
	 */
	public void setSacCentroDiResponsabilita(StrutturaAmministrativoContabile sacCentroDiResponsabilita) {
		this.sacCentroDiResponsabilita = sacCentroDiResponsabilita;
	}

	/**
	 * @return the statoOperativoInput
	 */
	public String getStatoOperativoInput() {
		return statoOperativoInput;
	}

	/**
	 * @param statoOperativoInput the statoOperativoInput to set
	 */
	public void setStatoOperativoInput(String statoOperativoInput) {
		this.statoOperativoInput = statoOperativoInput;
	}

	/**
	 * @return the bilancio
	 */
	public Bilancio getBilancio() {
		return bilancio;
	}

	/**
	 * @param bilancio the bilancio to set
	 */
	public void setBilancio(Bilancio bilancio) {
		this.bilancio = bilancio;
	}

	/**
	 * @return the annoAttoChiave
	 */
	public Integer getAnnoAttoChiave() {
		return annoAttoChiave;
	}

	/**
	 * @param annoAttoChiave the annoAttoChiave to set
	 */
	public void setAnnoAttoChiave(Integer annoAttoChiave) {
		this.annoAttoChiave = annoAttoChiave;
	}

	/**
	 * @return the numeroAttoChiave
	 */
	public Integer getNumeroAttoChiave() {
		return numeroAttoChiave;
	}

	/**
	 * @param numeroAttoChiave the numeroAttoChiave to set
	 */
	public void setNumeroAttoChiave(Integer numeroAttoChiave) {
		this.numeroAttoChiave = numeroAttoChiave;
	}

	/**
	 * @return the tipoAttoChiave
	 */
	public TipoAtto getTipoAttoChiave() {
		return tipoAttoChiave;
	}

	/**
	 * @param tipoAttoChiave the tipoAttoChiave to set
	 */
	public void setTipoAttoChiave(TipoAtto tipoAttoChiave) {
		this.tipoAttoChiave = tipoAttoChiave;
	}

	/**
	 * @return the sacCentroDiCostoChiave
	 */
	public StrutturaAmministrativoContabile getSacCentroDiCostoChiave() {
		return sacCentroDiCostoChiave;
	}

	/**
	 * @param sacCentroDiCostoChiave the sacCentroDiCostoChiave to set
	 */
	public void setSacCentroDiCostoChiave(StrutturaAmministrativoContabile sacCentroDiCostoChiave) {
		this.sacCentroDiCostoChiave = sacCentroDiCostoChiave;
	}

	/**
	 * @return the sacCentroDiResponsabilitaChiave
	 */
	public StrutturaAmministrativoContabile getSacCentroDiResponsabilitaChiave() {
		return sacCentroDiResponsabilitaChiave;
	}

	/**
	 * @param sacCentroDiResponsabilitaChiave the sacCentroDiResponsabilitaChiave to set
	 */
	public void setSacCentroDiResponsabilitaChiave(StrutturaAmministrativoContabile sacCentroDiResponsabilitaChiave) {
		this.sacCentroDiResponsabilitaChiave = sacCentroDiResponsabilitaChiave;
	}

	/**
	 * @return the strutturaAmmContabileChiave
	 */
	public StrutturaAmministrativoContabile getStrutturaAmmContabileChiave() {
		return strutturaAmmContabileChiave;
	}

	/**
	 * @param strutturaAmmContabileChiave the strutturaAmmContabileChiave to set
	 */
	public void setStrutturaAmmContabileChiave(StrutturaAmministrativoContabile strutturaAmmContabileChiave) {
		this.strutturaAmmContabileChiave = strutturaAmmContabileChiave;
	}

	/**
	 * @return the identificativo
	 */
	public String getIdentificativo() {
		return identificativo;
	}

	/**
	 * @param identificativo the identificativo to set
	 */
	public void setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
	}

	/**
	 * @return the dirigenteResponsabile
	 */
	public String getDirigenteResponsabile() {
		return dirigenteResponsabile;
	}

	/**
	 * @param dirigenteResponsabile the dirigenteResponsabile to set
	 */
	public void setDirigenteResponsabile(String dirigenteResponsabile) {
		this.dirigenteResponsabile = dirigenteResponsabile;
	}

	/**
	 * @return the trasparenza
	 */
	public String getTrasparenza() {
		return trasparenza;
	}

	/**
	 * @param trasparenza the trasparenza to set
	 */
	public void setTrasparenza(String trasparenza) {
		this.trasparenza = trasparenza;
	}

	/**
	 * U=update
	 * 
	 * @return
	 */
	public boolean isTipoDiVariazioneVariazione() {
		return "U".equals(tipoDiVariazione);
	}

	/**
	 * I=insert
	 * 
	 * @return
	 */
	public boolean isTipoDiVariazioneInserimento() {
		return "I".equals(tipoDiVariazione);

	}

	/**
	 * @return
	 */
	public boolean isTipoDiGestioneValido() {
		return isTipoDiVariazioneInserimento() || isTipoDiVariazioneVariazione();
	}

	/**
	 * @return
	 */
	public boolean isAttoAmministrativoDaAggiornare() {
		return getUid() != 0;
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
			.append("AttoAmministrativoElab [tipoDiVariazione=").append(tipoDiVariazione)
			.append(", codiceIstat=").append(codiceIstat)
			.append(", numero=").append(getNumero())
			.append(", dataProposta=").append(dataProposta)
			.append(", dataApprovazione=").append(dataApprovazione)
			.append(", dataEsecutivita=").append(dataEsecutivita)
			.append(", dataCreazione=").append(dataCreazione)
			.append(", sacCentroDiResponsabilita=").append(sacCentroDiResponsabilita != null ? sacCentroDiResponsabilita.getCodice() : "null")
			.append(", sacCentroDiCosto=").append(sacCentroDiCosto != null ? sacCentroDiCosto.getCodice() : "null")
			.append(", statoOperativo=").append(statoOperativoInput)
			.append(", bilancio=").append(bilancio != null ? bilancio.getUid() : "null")
			// CR-2547
			.append(", annoAttoChiave=").append(annoAttoChiave)
			.append(", numeroAttoChiave=").append(numeroAttoChiave)
			.append(", tipoAttoChiave=").append(tipoAttoChiave != null ? tipoAttoChiave.getCodice() : "null")
			.append(", sacCentroDiResponsabilitaChiave=").append(sacCentroDiResponsabilitaChiave != null ? sacCentroDiResponsabilitaChiave.getCodice() : "null")
			.append(", sacCentroDiCostoChiave=").append(sacCentroDiCostoChiave != null ? sacCentroDiCostoChiave.getCodice() : "null")
			.append(", identificativo=").append(identificativo)
			.append(", dirigenteResponsabile=").append(dirigenteResponsabile)
			.append(", trasparenza=").append(trasparenza)
			.append("]")
			.toString();
	}

}
