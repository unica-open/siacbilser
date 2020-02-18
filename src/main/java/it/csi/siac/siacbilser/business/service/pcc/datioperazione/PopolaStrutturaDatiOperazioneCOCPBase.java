/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.pcc.datioperazione;

import java.text.SimpleDateFormat;
import java.util.EnumSet;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacfin2ser.model.RegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.tesoro.fatture.StatoDebitoTipo;

/**
 * Base comune tra CO e CP.
 * 
 * @author Domenico
 */
public abstract class PopolaStrutturaDatiOperazioneCOCPBase extends PopolaStrutturaDatiOperazione {

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);


	public PopolaStrutturaDatiOperazioneCOCPBase(RegistroComunicazioniPCC registrazione) {
		super(registrazione);
	}


	/**
	 * Ottiene la descrizione di un capitolo
	 * 
	 * @param capitolo
	 * @return
	 */
	protected String getDescrizioneCapitolo(CapitoloUscitaGestione capitolo) {
		
		if(capitolo==null){
			return null;
		}
		
		return capitolo.getAnnoCapitolo()
				+"-"+capitolo.getNumeroCapitolo()
				+"-"+capitolo.getNumeroArticolo()
				+"-"+capitolo.getNumeroUEB();
	}
	
	
	/**
	 * CUP della quota documento se presente, altrimenti CUP dell'impegno se presente, altrimenti NA
	 * 
	 * @param subdocumentoSpesa
	 * @param statoDebitoTipo 
	 * @return
	 */
	protected String getCUP(SubdocumentoSpesa subdocumentoSpesa, StatoDebitoTipo statoDebitoTipo) {
		if(statoDebitoTipo!=null && 
				EnumSet.of(StatoDebitoTipo.SOSP, StatoDebitoTipo.NOLIQ).contains(statoDebitoTipo)){
			return "NA";
		}
		
		
		String result = subdocumentoSpesa.getCup();
		if(StringUtils.isNotBlank(result)){
			return result;
		}
		result = subdocumentoSpesa.getImpegno()!=null?subdocumentoSpesa.getImpegno().getCup():null;
		if(StringUtils.isNotBlank(result)){
			return result;
		}
		
		return "NA";
	}


	/**
	 * CIG della quota documento se presente, altrimenti CIG dell'impegno se presente, altrimenti NA
	 * 
	 * @param subdocumentoSpesa
	 * @param statoDebitoTipo 
	 * @return
	 */
	protected String getCIG(SubdocumentoSpesa subdocumentoSpesa, StatoDebitoTipo statoDebitoTipo) {
		if(statoDebitoTipo!=null && 
				EnumSet.of(StatoDebitoTipo.SOSP, StatoDebitoTipo.NOLIQ).contains(statoDebitoTipo)){
			return "NA";
		}
		
		String result = subdocumentoSpesa.getCig();
		if(StringUtils.isNotBlank(result)){
			return result;
		}
		result = subdocumentoSpesa.getImpegno()!=null?subdocumentoSpesa.getImpegno().getCig():null;
		if(StringUtils.isNotBlank(result)){
			return result;
		}
		
		return "NA";
	}
	
	/**
	 * 
	 * data emissione-anno-numero impegno-subimpegno. Ad esempio: 
	 * 30/01/2014-2014/2994
	 * 
	 * @param impegno
	 * @return
	 */
	protected String getEstremiImpegno(Impegno impegno) {
		
		if(impegno==null){
			return null;
		}
		
		String result = sdf.format(impegno.getDataEmissione()) 
				+ "-" + impegno.getAnnoMovimento() 
				+ "/" + impegno.getNumero();
		
		return result;
	}
	
	/**
	 * Gets the id fiscale iva.
	 *
	 * @param soggetto the soggetto
	 * @return the id fiscale iva
	 */
	protected String getIdFiscaleIva(Soggetto soggetto) {
		String result;
		if(StringUtils.isNotBlank(soggetto.getPartitaIva())){
			result = "IT"+ soggetto.getPartitaIva();
		} else {
			result = soggetto.getCodiceFiscale();
		}
		return result;
	}
	

}
