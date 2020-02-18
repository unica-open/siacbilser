/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import it.csi.siac.siacfinser.integration.entity.SiacDAmbitoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTEnteProprietarioFin;
import it.csi.siac.siacfinser.integration.util.Operazione;

/**
 * Contenitore per le informazioni sulla tracciabilita' dell'operazione in corso (che si ripetono uguali in ogni servizio)
 * e le altre informazioni generali che possono propagarsi tra service e dad e tra vari metodi del dad
 * @author claudio.picco
 *
 */
public class DatiOperazioneDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4573650250263123474L;
	private long currMillisec;
	private Operazione operazione;
	private SiacTEnteProprietarioFin siacTEnteProprietario;
	private SiacDAmbitoFin  siacDAmbito;
	private String accountCode;
	private Integer annoBilancio;
	
	public static DatiOperazioneDto buildDatiOperazione(DatiOperazioneDto dati, Operazione operaz){
		DatiOperazioneDto datiCostruiti = new DatiOperazioneDto();
		datiCostruiti.setCurrMillisec(dati.getCurrMillisec());
		datiCostruiti.setSiacTEnteProprietario(dati.getSiacTEnteProprietario());
//		datiCostruiti.setUtenteOperazione(dati.getUtenteOperazione());
		datiCostruiti.setAccountCode(dati.getAccountCode());
		datiCostruiti.setOperazione(operaz);
		datiCostruiti.setSiacDAmbito(dati.getSiacDAmbito());
		datiCostruiti.setAnnoBilancio(dati.getAnnoBilancio());
		return datiCostruiti;
	}
	
	public DatiOperazioneDto(){
		super();
	}
	
	public DatiOperazioneDto(long currMillisec,Operazione operazione,SiacTEnteProprietarioFin siacTEnteProprietario,String accountCode){
		this.currMillisec=currMillisec;
		this.accountCode = accountCode;
		this.operazione=operazione;
		this.siacTEnteProprietario=siacTEnteProprietario;
	}
	
	public DatiOperazioneDto(long currMillisec,Operazione operazione,SiacTEnteProprietarioFin siacTEnteProprietario,SiacDAmbitoFin siacDAmbito,String accountCode){
		this.currMillisec=currMillisec;
		this.accountCode = accountCode;
		this.operazione=operazione;
		this.siacTEnteProprietario=siacTEnteProprietario;
		this.siacDAmbito=siacDAmbito;
	}
	
	public DatiOperazioneDto(long currMillisec,Operazione operazione,SiacTEnteProprietarioFin siacTEnteProprietario,SiacDAmbitoFin siacDAmbito,String accountCode, Integer annoBilancio){
		this.currMillisec=currMillisec;
		this.accountCode = accountCode;
		this.operazione=operazione;
		this.siacTEnteProprietario=siacTEnteProprietario;
		this.siacDAmbito=siacDAmbito;
		this.annoBilancio = annoBilancio;
	}
	
	public long getCurrMillisec() {
		return currMillisec;
	}
	public void setCurrMillisec(long currMillisec) {
		this.currMillisec = currMillisec;
	}

	/**
	 * Versione rinominata di getUtenteOperazione, sono la stessa cosa
	 * @return
	 */
	public Operazione getOperazione() {
		return operazione;
	}
	public void setOperazione(Operazione operazione) {
		this.operazione = operazione;
	}
	public SiacTEnteProprietarioFin getSiacTEnteProprietario() {
		return siacTEnteProprietario;
	}
	public void setSiacTEnteProprietario(SiacTEnteProprietarioFin siacTEnteProprietario) {
		this.siacTEnteProprietario = siacTEnteProprietario;
	}
	
	public Timestamp getTs(){
		return new Timestamp(currMillisec);
	}

	public SiacDAmbitoFin getSiacDAmbito() {
		return siacDAmbito;
	}

	public void setSiacDAmbito(SiacDAmbitoFin siacDAmbito) {
		this.siacDAmbito = siacDAmbito;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public Integer getAnnoBilancio() {
		return annoBilancio;
	}

	public void setAnnoBilancio(Integer annoBilancio) {
		this.annoBilancio = annoBilancio;
	}
	
	public Date getCurMillisecDate() {
		return new Date(currMillisec);
	}
}
