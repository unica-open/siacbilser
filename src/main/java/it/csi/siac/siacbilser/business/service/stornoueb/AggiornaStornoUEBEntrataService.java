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

import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaStornoUEB;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaStornoUEBResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaPrevisioneDad;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class AggiornaStornoUEBEntrataService.
 * @deprecated da eliminare con le UEB
 */
@Deprecated
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaStornoUEBEntrataService extends AggiornaStornoUEBBaseService {
	
	/** The capitolo uscita previsione dad. */
	@Autowired
	private CapitoloUscitaPrevisioneDad capitoloUscitaPrevisioneDad;
	
	
	@Override
	@Transactional
	public AggiornaStornoUEBResponse executeService(AggiornaStornoUEB serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/**
	 * Implementa il check della variazione degli importi per capitoli di tipo entrata.
	 *
	 * @param importiCapSorgente the importi cap sorgente
	 * @param importiCapDestinazione the importi cap destinazione
	 */
	protected void checkImporti(List<ImportiCapitolo> importiCapSorgente, List<ImportiCapitolo> importiCapDestinazione) {
		// SIAC-6883
//		int annoStorno = req.getBilancio().getAnno();
//		
//		for(DettaglioVariazioneImportoCapitolo dettVarImp : listaDettaglioVariazioneImportiDelta){
//			Integer annoCompetenza = dettVarImp.getAnnoCompetenza();
//			int deltaAnno = annoCompetenza.intValue() - annoStorno + 1;
//			
//			ImportiCapitolo importiCapitoloSorgente = findByAnnoCompetenza(importiCapSorgente, annoCompetenza);
//			checkVariazioneImporti(dettVarImp, importiCapitoloSorgente, deltaAnno, true);
//			ImportiCapitolo importiCapitoloDestinazione = findByAnnoCompetenza(importiCapDestinazione, annoCompetenza);
//			checkVariazioneImporti(dettVarImp, importiCapitoloDestinazione, deltaAnno, false);
//			
//		}
	}
	
	//#############################################################################################################
	
	//#############################################################################################################
	
	
	/**
	 * Check variazione importi.
	 *
	 * @param dettVarImp the dett var imp
	 * @param importiCapitolo the importi capitolo
	 * @param disponibilitaVariare the disponibilita variare
	 */
	private void checkVariazioneImporti(DettaglioVariazioneImportoCapitolo dettVarImp, ImportiCapitolo importiCapitolo, int delta, boolean isSorgente/*, BigDecimal disponibilitaVariare*/) {
		// SIAC-6883
//		final String methodName = "checkVariazineImporti";
//		
//		if(dettVarImp==null){
//			log.debug(methodName, "anno: "+ importiCapitolo.getAnnoCompetenza() + " [dettaglio variazione importo null]");
//			return;
//		}
//		
//		log.debug(methodName, "anno: "+ importiCapitolo.getAnnoCompetenza() + " ["+dettVarImp.getAnnoCompetenza()+"]");
//		
//		BigDecimal disponibilitaVariare = importiCapitolo.getStanziamento();
//		BigDecimal disponibilitaVariareCassa = importiCapitolo.getStanziamentoCassa();
//		
//		log.logXmlTypeObject(dettVarImp, "DettaglioVariazioneImportoCapitolo da variare con disponibilitaVariare: "+disponibilitaVariare +" e disponibilitaVariareCassa: "+disponibilitaVariareCassa);
//		
//		log.debug(methodName, "check Stanziamento: disponibilitaVariare + variazioneStanziamento >= 0 ");
//		if(dettVarImp.getStanziamento() != null && dettVarImp.getStanziamento().signum() != 0 && (dettVarImp.getStanziamento().signum() < 0) == isSorgente) {
//			checkVariazioneImporto(disponibilitaVariare,dettVarImp.getStanziamento(), getErrore(dettVarImp));
//		}
//		
//		if(delta==1) {
//			//la cassa è presente solo per l'anno 1!
//			log.debug(methodName, "check Stanziamento Cassa: disponibilitaVariareCassa + variazioneStanziamentoCassa >= 0 ");
//			if(dettVarImp.getStanziamentoCassa() != null && dettVarImp.getStanziamentoCassa().signum() != 0 && (dettVarImp.getStanziamentoCassa().signum() < 0) == isSorgente) {
//				checkVariazioneImporto(disponibilitaVariareCassa,dettVarImp.getStanziamentoCassa(), getErrore(dettVarImp));
//			}
//		
//			boolean isCassaOk = dettVarImp.getStanziamento().setScale(2,RoundingMode.HALF_DOWN)
//				.add(dettVarImp.getStanziamentoResiduo().setScale(2,RoundingMode.HALF_DOWN))
//				.compareTo(dettVarImp.getStanziamentoCassa().setScale(2,RoundingMode.HALF_DOWN))<=0;
//			
//			log.debug(methodName, "check Competenza + Residuo <= Cassa ? "+ isCassaOk);
//			
//			// TODO: segnalazione SIAC-2105 - controllo non chiaro. In attesa di nuove indicazioni
////			if(!isCassaOk) {
////				throw new BusinessException(ErroreBil.OPERAZIONE_NON_POSSIBILE.getErrore("l'importo di Cassa deve essere maggiore o uguale all'importo di competenza piu'' l'importo residuo"));
////			}
//		} else {
//			log.debug(methodName, "check Stanziamento Cassa: salato perche' anno"+delta);
//			log.debug(methodName, "check Competenza + Residuo <= Cassa : salato perche' anno"+delta);
//		}
	}
	
	/**
	 * Gets the disponibilita variare.
	 *
	 * @param importiCapitolo the ImportiCapitolo
	 * @param d anno valori possibili: 1, 2 o 3
	 * @return disponibilita variare dell'anno 1, 2 o 3
	 */
//	private BigDecimal getDisponibilitaVariare(ImportiCapitolo importiCapitolo, Integer d) {
//		final String methodName = "getDisponibilitaVariare";
//		
//		String disponibilitaVariareFieldName = "disponibilitaVariareAnno" + d;
//		
//		BigDecimal result = Utility.getFieldValue(importiCapitolo, disponibilitaVariareFieldName);
//		
//		log.info(methodName, "returning "+ disponibilitaVariareFieldName + ": "+ result);
//		
//		return result;
//	}

	
	private Errore getErrore(DettaglioVariazioneImportoCapitolo dettVarImp) {
//		if(TipoCapitolo.isTipoCapitoloEntrata(dettVarImp.getCapitolo())){
			return ErroreBil.STANZIAMENTI_CAPITOLO_UEB_CHE_SI_STANNO_VARIANDO_MINORI_DI_ZERO.getErrore();
//		} else {
//			return ErroreBil.DISPONIBILITA_UEB_INSUFFICIENTE.getErrore(dettVarImp.getCapitolo().getNumeroUEB(),dettVarImp.getAnnoCompetenza() );
//		}
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
	 */
//	private void checkVariazioneImportiOld(DettaglioVariazioneImportoCapitolo dettVarImp, ImportiCapitolo importiCapitolo) {		
//		final String methodName = "checkVariazineImporti";
//		
//		if(dettVarImp==null){
//			log.debug(methodName, "anno: "+ importiCapitolo.getAnnoCompetenza() + " [dettaglio variazione importo null]");
//			return;
//		}
//		
//		log.debug(methodName, "anno: "+ importiCapitolo.getAnnoCompetenza() + " ["+dettVarImp.getAnnoCompetenza()+"]");
//		log.debug(methodName, "check Stanziamento");
//		checkVariazioneImportoMaggioreUgualeAZero(importiCapitolo.getStanziamento(),dettVarImp.getStanziamento());
//		log.debug(methodName, "check Stanziamento cassa");
//		checkVariazioneImportoMaggioreUgualeAZero(importiCapitolo.getStanziamentoCassa(),dettVarImp.getStanziamentoCassa());
//		log.debug(methodName, "check Stanziamento residuo");
//		checkVariazioneImportoMaggioreUgualeAZero(importiCapitolo.getStanziamentoResiduo(),dettVarImp.getStanziamentoResiduo());	
//				
//	}

	
	/**
	 * Se importo sorgente sommato a importo variazione restituisce un numero negativo viene sollevata l'eccezione di business.
	 *
	 * @param importoSorgente the importo sorgente
	 * @param importoVariazione  - è sempre negativo
	 */
//	private void checkVariazioneImportoMaggioreUgualeAZero(BigDecimal importoSorgente, BigDecimal importoVariazione) {
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
//			throw new BusinessException(ErroreBil.STANZIAMENTI_CAPITOLO_UEB_CHE_SI_STANNO_VARIANDO_MINORI_DI_ZERO.getErrore());
//		}
//		
//		
//	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.business.service.stornoueb.AggiornaStornoUEBBaseService#checkTipoCapitolo()
	 */
	@Override
	protected void checkTipoCapitolo() {
		if(!capitoloUscitaPrevisioneDad.isTipoEntrataGestione(stornoUEB.getCapitoloSorgente().getUid())){
			throw new BusinessException(ErroreCore.VALORE_NON_VALIDO.getErrore("uid capitolo sorgente","Deve essere un capitolo di entrata gestione."));
		}
		
		if(!capitoloUscitaPrevisioneDad.isTipoEntrataGestione(stornoUEB.getCapitoloDestinazione().getUid())){
			throw new BusinessException(ErroreCore.VALORE_NON_VALIDO.getErrore("uid capitolo destinazione","Deve essere un capitolo di entrata gestione."));
		}		
	}
	
	
//	/**
//	 * Somma la variazione all'importo sorgente.
//	 * 
//	 * @param importoSorgente
//	 * @param importoVariazione
//	 * @return
//	 */
//	private BigDecimal sommaSorgenteEVariazione(BigDecimal importoSorgente, BigDecimal importoVariazione) {
//		final String methodName = "sommaSorgenteEVariazione";
//		
//		log.debug(methodName, "importoSorgente: "+ importoSorgente + " importoVariazione: "+importoVariazione);
//		
//		if(importoVariazione ==null){
//			return importoSorgente;
//		}
//		if(importoSorgente == null){
//			return importoVariazione;
//		}
//				
//		
//		return importoSorgente.add(importoVariazione);	
//	}
	
//	/**
//	 * Come sommaSorgenteEVariazione ma la variazione viene sommata in valore assoluto
//	 * @param importoSorgente
//	 * @param importoVariazione
//	 * @return
//	 */
//	private BigDecimal sommaSorgenteEVariazioneAbs(BigDecimal importoSorgente, BigDecimal importoVariazione) {		
//		BigDecimal importoVariazioneAbs = importoVariazione!=null?importoVariazione.abs():null;		
//		return sommaSorgenteEVariazione(importoSorgente, importoVariazioneAbs);	
//	}
	
	

	
	

	

	
	
	
	
	

}
