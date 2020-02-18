/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stornoueb;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.capitolo.CalcoloDisponibilitaDiUnCapitoloService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceStornoUEB;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceStornoUEBResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaPrevisioneDad;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class InserisceStornoUEBUscitaService.
 * @deprecated da eliminare con le UEB
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Deprecated
public class InserisceStornoUEBUscitaService extends InserisceStornoUEBBaseService {
	
	/** The calcolo disponibilita di un capitolo service. */
	@Autowired
	private CalcoloDisponibilitaDiUnCapitoloService calcoloDisponibilitaDiUnCapitoloService;
	
	/** The capitolo uscita previsione dad. */
	@Autowired
	private CapitoloUscitaPrevisioneDad capitoloUscitaPrevisioneDad;	
	
	@Override
	@Transactional
	public InserisceStornoUEBResponse executeService(InserisceStornoUEB serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
//	/**
//	 * Implementa il check della variazione degli importi per capitoli di tipo uscita.
//	 *
//	 * @param importiCapSorgente the importi cap sorgente
//	 * @param importiCapDestinazione the importi cap destinazione
//	 */
//	protected void checkImportiOld(List<ImportiCapitolo> importiCapSorgente, List<ImportiCapitolo> importiCapDestinazione) {
//		
//		BigDecimal disponibilitaVariare = calcolaDisponibilitaVariare();
//		
//		for(DettaglioVariazioneImportoCapitolo dettVarImp : stornoUEB.getListaDettaglioVariazioneImporto()){
//			Integer annoCompetenza = dettVarImp.getAnnoCompetenza();
//			ImportiCapitolo importiCapitolo = findByAnnoCompetenza(importiCapSorgente, annoCompetenza);
//			checkVariazioneImporti(dettVarImp, importiCapitolo, disponibilitaVariare);
//			
//		}
//		
//		// TODO Auto-generated method stub
//		
//	}
	
	
	/**
	 * Implementa il check della variazione degli importi per capitoli di tipo uscita.
	 *
	 * @param importiCapSorgente the importi cap sorgente
	 * @param importiCapDestinazione the importi cap destinazione
	 */
	protected void checkImporti(List<ImportiCapitolo> importiCapSorgente, List<ImportiCapitolo> importiCapDestinazione) {
		// SIAC-6883
////		BigDecimal disponibilitaVariare = calcolaDisponibilitaVariareCapitoloSorgente();
////		BigDecimal disponibilitaVariareDest = calcolaDisponibilitaVariareCapitoloDestinazione();
//		
//		ImportiCapitolo importiCapitoloSorgenteConDerivatiValorizzati = ottieniImportiCapitoloConDerivatiValorizzati(importiCapSorgente);
//		ImportiCapitolo importiCapitoloDestinazioneConDerivatiValorizzati = ottieniImportiCapitoloConDerivatiValorizzati(importiCapDestinazione);
//		
//		BigDecimal disponibilitaVariareCassaSorgente = importiCapitoloSorgenteConDerivatiValorizzati != null ? importiCapitoloSorgenteConDerivatiValorizzati.getDisponibilitaVariareCassa() : BigDecimal.ZERO;
//		BigDecimal disponibilitaVariareCassaDestinazione = importiCapitoloDestinazioneConDerivatiValorizzati != null ? importiCapitoloDestinazioneConDerivatiValorizzati.getDisponibilitaVariareCassa() : BigDecimal.ZERO;
//		
//		int annoStorno = req.getBilancio().getAnno();
//		
//		for(DettaglioVariazioneImportoCapitolo dettVarImp : stornoUEB.getListaDettaglioVariazioneImporto()){
//			Integer annoCompetenza = dettVarImp.getAnnoCompetenza();
//			int deltaAnno = annoCompetenza.intValue() - annoStorno + 1;
//			BigDecimal disponibilitaVariareSorgente = getDisponibilitaVariare(importiCapitoloSorgenteConDerivatiValorizzati, deltaAnno);
//			BigDecimal disponibilitaVariareDestinazione = getDisponibilitaVariare(importiCapitoloDestinazioneConDerivatiValorizzati, deltaAnno);
//			
//			ImportiCapitolo importiCapitoloSorgente = findByAnnoCompetenza(importiCapSorgente, annoCompetenza);
//			checkVariazioneImporti(dettVarImp, importiCapitoloSorgente, deltaAnno, disponibilitaVariareSorgente, deltaAnno == 1 ? disponibilitaVariareCassaSorgente : BigDecimal.ZERO);
//			ImportiCapitolo importiCapitoloDestinazione = findByAnnoCompetenza(importiCapDestinazione, annoCompetenza);
//			checkVariazioneImporti(dettVarImp, importiCapitoloDestinazione, deltaAnno, disponibilitaVariareDestinazione, deltaAnno == 1 ? disponibilitaVariareCassaDestinazione : BigDecimal.ZERO);
//			
//		}
		
	}
	
	

	
	//#############################################################################################################
	
	//#############################################################################################################
	
	
	private ImportiCapitolo ottieniImportiCapitoloConDerivatiValorizzati(List<ImportiCapitolo> importiCapitoloList) {
		for(ImportiCapitolo ic : importiCapitoloList) {
			// Controllo che almeno uno delle tre disponibilita' non sia zero
			if(isNotNullAndNotZero(ic.getDisponibilitaVariareAnno1()) || isNotNullAndNotZero(ic.getDisponibilitaVariareAnno2()) || isNotNullAndNotZero(ic.getDisponibilitaVariareAnno3())) {
				return ic;
			}
		}
		
		return null;
	}

	private boolean isNotNullAndNotZero(BigDecimal value) {
		return value != null && value.signum() != 0;
	}

	/**
	 * Check variazione importi.
	 *
	 * @param dettVarImp the dett var imp
	 * @param importiCapitolo the importi capitolo
	 * @param disponibilitaVariare the disponibilita variare
	 */
	private void checkVariazioneImporti(DettaglioVariazioneImportoCapitolo dettVarImp, ImportiCapitolo importiCapitolo, int delta, BigDecimal disponibilitaVariare, BigDecimal disponibilitaVariareCassa) {
		final String methodName = "checkVariazineImporti";
		
		if(dettVarImp == null){
			log.debug(methodName, "anno: " + importiCapitolo.getAnnoCompetenza() + " [dettaglio variazione importo null]");
			return;
		}
		
		log.logXmlTypeObject(dettVarImp, "DettaglioVariazioneImportoCapitolo da variare con disponibilitaVariare: " + disponibilitaVariare + " e disponibilitaVariareCassa: " + disponibilitaVariareCassa);
		
		log.debug(methodName, "check Stanziamento: disponibilitaVariare + variazioneStanziamento >= 0 ");
		checkVariazioneImporto(disponibilitaVariare,dettVarImp.getStanziamento(), getErrore(dettVarImp));
		
		if(delta==1) {
			//la cassa è presente solo per l'anno 1!
			log.debug(methodName, "check Stanziamento Cassa: disponibilitaVariareCassa + variazioneStanziamentoCassa >= 0 ");
			checkVariazioneImporto(disponibilitaVariareCassa,dettVarImp.getStanziamentoCassa(), getErrore(dettVarImp));
		
			boolean isCassaOk = dettVarImp.getStanziamento().setScale(2,RoundingMode.HALF_DOWN)
				.add(dettVarImp.getStanziamentoResiduo().setScale(2,RoundingMode.HALF_DOWN))
				.compareTo(dettVarImp.getStanziamentoCassa().setScale(2,RoundingMode.HALF_DOWN))<=0;
			
			log.debug(methodName, "check Competenza + Residuo <= Cassa ? "+ isCassaOk);
			
			// TODO: segnalazione SIAC-2105 - controllo non chiaro. In attesa di nuove indicazioni
//			if(!isCassaOk) {
//				throw new BusinessException(ErroreBil.OPERAZIONE_NON_POSSIBILE.getErrore("l'importo di Cassa deve essere maggiore o uguale all'importo di competenza piu'' l'importo residuo"));
//			}
		} else {
			log.debug(methodName, "check Stanziamento Cassa: salato perche' anno"+delta);
			log.debug(methodName, "check Competenza + Residuo <= Cassa : salato perche' anno"+delta);
		}
		
				
	}
	
	/**
	 * Gets the disponibilita variare.
	 *
	 * @param importiCapitolo the ImportiCapitolo
	 * @param d anno valori possibili: 1, 2 o 3
	 * @return disponibilita variare dell'anno 1, 2 o 3
	 */
	private BigDecimal getDisponibilitaVariare(ImportiCapitolo importiCapitolo, Integer d) {
		final String methodName = "getDisponibilitaVariare";
		String disponibilitaVariareFieldName = "disponibilitaVariareAnno" + d;
		if(importiCapitolo == null) {
			log.info(methodName, "returning "+ disponibilitaVariareFieldName + ": 0 (for null)");
			return BigDecimal.ZERO;
		}
		
		BigDecimal result = Utility.getFieldValue(importiCapitolo, disponibilitaVariareFieldName);
		
		log.info(methodName, "returning "+ disponibilitaVariareFieldName + ": "+ result);
		
		return result;
	}

	
	private Errore getErrore(DettaglioVariazioneImportoCapitolo dettVarImp) {
		// SIAC-6883
//		return ErroreBil.DISPONIBILITA_UEB_INSUFFICIENTE.getErrore(stornoUEB.getCapitoloSorgente().getNumeroUEB(), dettVarImp.getAnnoCompetenza());
		return ErroreBil.DISPONIBILITA_UEB_INSUFFICIENTE.getErrore(stornoUEB.getCapitoloSorgente().getNumeroUEB());
	}
	


	/**
	 * Se disponibilitaVariare sommato a importoVariazione restituisce un numero negativo viene sollevata l'eccezione di business con l'errore passato come parametro.
	 *
	 * @param disponibilitaVariare la disponibilita a variare
	 * @param importoVariazione  l'importo della variazione
	 * @param errore l'errore di business da sollevare.
	 */
	protected void checkVariazioneImporto(BigDecimal disponibilitaVariare, BigDecimal importoVariazione, Errore errore) {
		final String methodName = "checkVariazioneImporto";
		
		String msg = "%.2f + (%.2f) >= 0 ? %s";
		
		if(importoVariazione == null) {
			log.debug(methodName, String.format(msg, disponibilitaVariare, null, null));
			return;
		}
		if(disponibilitaVariare == null){
			disponibilitaVariare = BigDecimal.ZERO;
		}
		
		boolean isOk = disponibilitaVariare.setScale(2,RoundingMode.HALF_DOWN).add(importoVariazione.setScale(2,RoundingMode.HALF_DOWN)).compareTo(BigDecimal.ZERO)>=0;
		
		log.debug(methodName,  String.format(msg, disponibilitaVariare, importoVariazione, isOk));
		
		if(!isOk){
			throw new BusinessException(errore);
		}
	}
	
	//################################################################################################################
	
	
	
	/**
	 * Check variazione importi.
	 *
	 * @param dettVarImp the dett var imp
	 * @param importiCapitolo the importi capitolo
	 * @param disponibilitaVariare the disponibilita variare
	 */
//	private void checkVariazioneImportiOld(DettaglioVariazioneImportoCapitolo dettVarImp, ImportiCapitolo importiCapitolo, BigDecimal disponibilitaVariare) {
//		
//		final String methodName = "checkVariazineImporti";
//		
//		if(dettVarImp==null){
//			log.debug(methodName, "anno: "+ importiCapitolo.getAnnoCompetenza() + " [dettaglio variazione importo null]");
//			return;
//		}
//		
//		log.debug(methodName, "anno: "+ importiCapitolo.getAnnoCompetenza() + " ["+dettVarImp.getAnnoCompetenza()+"]");
//		log.debug(methodName, "check Stanziamento");		
//		checkVariazioneImportoMaggioreUgualeAZero(disponibilitaVariare.add(importiCapitolo.getStanziamento()),dettVarImp.getStanziamento(), importiCapitolo.getAnnoCompetenza());
//		log.debug(methodName, "check Stanziamento cassa");
//		checkVariazioneImportoMaggioreUgualeAZero(disponibilitaVariare.add(importiCapitolo.getStanziamentoCassa()),dettVarImp.getStanziamentoCassa(), importiCapitolo.getAnnoCompetenza());
//		log.debug(methodName, "check Stanziamento residuo");
//		checkVariazioneImportoMaggioreUgualeAZero(disponibilitaVariare.add(importiCapitolo.getStanziamentoResiduo()),dettVarImp.getStanziamentoResiduo(), importiCapitolo.getAnnoCompetenza());	
//		
//		log.debug(methodName, "check Sommatoria importi");
//		checkVariazioneImportoMaggioreUgualeAZero(disponibilitaVariare
//												.add(importiCapitolo.getStanziamento())
//												.add(importiCapitolo.getStanziamentoCassa())
//												.add(importiCapitolo.getStanziamentoResiduo()),
//												
//												dettVarImp.getStanziamento()
//												.add(dettVarImp.getStanziamentoCassa()
//												.add(dettVarImp.getStanziamentoResiduo())), 
//												
//												importiCapitolo.getAnnoCompetenza());
//		
//				
//	}
	

	
	/**
	 * Se importo sorgente sommato a importo variazione restituisce un numero negativo viene sollevata l'eccezione di business.
	 *
	 * @param importoSorgente the importo sorgente
	 * @param importoVariazione  - è sempre negativo
	 * @param annoCompetenza the anno competenza
	 */
//	private void checkVariazioneImportoMaggioreUgualeAZero(BigDecimal importoSorgente, BigDecimal importoVariazione, Integer annoCompetenza) {
//		final String methodName = "checkVariazioneImportoMaggioreUgualeAZero";
//		
//		log.debug(methodName, "importoSorgente: "+ importoSorgente + " importoVariazione: "+importoVariazione);
//		
//		if(importoVariazione ==null){
//			return;
//		}
//		if(importoSorgente == null){
//			importoSorgente = BigDecimal.ZERO;
//		}
//		
//		
//		if(importoSorgente.add(importoVariazione).compareTo(BigDecimal.ZERO)<0){
//			@SuppressWarnings("rawtypes")
//			Capitolo cap = capitoloUscitaPrevisioneDad.findOne(stornoUEB.getCapitoloSorgente().getUid());
//			throw new BusinessException(ErroreBil.DISPONIBILITA_UEB_INSUFFICIENTE.getErrore(cap.getNumeroCapitolo()+"/"+cap.getNumeroArticolo()+"/"+cap.getNumeroUEB(),""+annoCompetenza, importoSorgente, importoVariazione ));
//		}		
//	}


	/**
	 * Calcola disponibilita variare.
	 *
	 * @return the big decimal
	 */
//	private BigDecimal calcolaDisponibilitaVariare() {
//		Capitolo capitolo = stornoUEB.getCapitoloSorgente();
		
//		CalcoloDisponibilitaDiUnCapitolo req = new CalcoloDisponibilitaDiUnCapitolo();
////		req.setAnnoCapitolo(capitolo.getAnnoCapitolo());
////		req.setNumroCapitolo(capitolo.getNumeroCapitolo());
//		//req.setNumeroArticolo(capitolo.getNumeroArticolo()); //TODO MANCA!!		
//		req.setTipoDisponibilitaRichiesta("TIPO DISP A impegnare/variare"); //TODO Impostare il tipo disp richiesta a "impegnare/variare"
//		
//		CalcoloDisponibilitaDiUnCapitoloResponse response = executeExternalService(calcoloDisponibilitaDiUnCapitoloService, req);
		
		
		
//		return response.getDisponibilitaRichiesta();
//		return BigDecimal.ZERO;
//	}


	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.business.service.stornoueb.InserisceStornoUEBBaseService#checkTipoCapitolo()
	 */
	@Override
	protected void checkTipoCapitolo() {
		if(!capitoloUscitaPrevisioneDad.isTipoUscitaGestione(stornoUEB.getCapitoloSorgente().getUid())){
			throw new BusinessException(ErroreCore.VALORE_NON_VALIDO.getErrore("uid capitolo sorgente","Deve essere un capitolo di uscita gestione."));
		}
		
		if(!capitoloUscitaPrevisioneDad.isTipoUscitaGestione(stornoUEB.getCapitoloDestinazione().getUid())){
			throw new BusinessException(ErroreCore.VALORE_NON_VALIDO.getErrore("uid capitolo destinazione","Deve essere un capitolo di uscita gestione."));
		}		
	}


	

}
