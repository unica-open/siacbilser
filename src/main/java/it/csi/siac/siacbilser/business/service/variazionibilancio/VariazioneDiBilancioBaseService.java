/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.variazionibilancio;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.CapitoloDad;
import it.csi.siac.siacbilser.integration.dad.ComponenteImportiCapitoloDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.integration.dad.AttoAmministrativoDad;
import it.csi.siac.siacbilser.integration.dad.VariazioniDad;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemDetTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneComponenteImportoCapitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.ImportoDerivatoFunctionEnum;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.model.utils.WrapperComponenteImportiCapitoloVariatiInVariazione;
import it.csi.siac.siacbilser.model.utils.WrapperImportiCapitoloVariatiInVariazione;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.CoreService;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetAzioneRichiesta;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetAzioneRichiestaResponse;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.VariabileProcesso;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Nuova implementazione di {@link VariazioneBilancioBaseService}.
 *
 * @author Domenico Lisi
 * 
 * @param <REQ> the generic type
 * @param <RES> the generic type
 */
public abstract class VariazioneDiBilancioBaseService<REQ extends ServiceRequest,RES extends ServiceResponse> extends CheckedAccountBaseService<REQ, RES> {
	
	/** The core service. */
	@Autowired protected CoreService coreService;
	/** The variazioni dad. */
	@Autowired protected VariazioniDad variazioniDad;
	/** The importi capitolo dad. */
	@Autowired protected ImportiCapitoloDad importiCapitoloDad;
	/** The provvedimento dad. */
	@Autowired protected AttoAmministrativoDad attoAmministrativoDad;
	/** The capitolo dad. */
	@Autowired protected CapitoloDad capitoloDad;
	/** The capitolo dad. */
	@Autowired protected ComponenteImportiCapitoloDad componenteImportiCapitoloDad;
	
	/** The variazione. */
	protected VariazioneImportoCapitolo variazione;
	
	//--- Check importi Variazione -----------------------------------------------------------------
	
	/**
	 * Controlla la congruità della variazione.
	 */
	@Deprecated
	protected void loadAndCheckImporti() {
		final String methodName = "checkImporti";
		//SIAC-3044
		if(StatoOperativoVariazioneBilancio.ANNULLATA.equals(variazione.getStatoOperativoVariazioneDiBilancio())){
			log.info(methodName, "Sto annullando la Variazione. Salto i check sugli importi e procedo.");
			return;
		}

		List<Integer> ids = new ArrayList<Integer>();
		for(DettaglioVariazioneImportoCapitolo dettVarImp : variazione.getListaDettaglioVariazioneImporto()){
			ids.add(dettVarImp.getCapitolo().getUid());
			//SIAC-7658
			caricaDettaglioImportiCapitolo(dettVarImp);
		}
		
		// SIAC-6883: eliminato l'anno
		Map<String, BigDecimal> mappaImporti = variazioniDad.findStanziamentoVariazioneByUidCapitoloAndUidVariazione(ids, variazione.getUid());
		
		for(DettaglioVariazioneImportoCapitolo dettVarImp : variazione.getListaDettaglioVariazioneImporto()){
			checkVariazioneImporti(dettVarImp, mappaImporti);
		}
	}

	
	/**
	 * Check variazione importi.
	 *
	 * @param dettVarImp the dett var imp
	 * @param importi 
	 */
	@Deprecated
	protected void checkVariazioneImporti(DettaglioVariazioneImportoCapitolo dettVarImp, Map<String, BigDecimal> importi) {
		boolean isCapitoloFondino = TipoCapitolo.isTipoCapitoloUscita(dettVarImp.getCapitolo()) && capitoloDad.isCapitoloFondino(dettVarImp.getCapitolo().getUid());
		checkVariazioneImporti(dettVarImp,importi, isCapitoloFondino);
	}
	/**
	 * Check variazione importi.
	 *
	 * @param dettVarImp the dett var imp
	 * @param importi 
	 */
	@Deprecated
	protected void checkVariazioneImporti(DettaglioVariazioneImportoCapitolo dettVarImp, Map<String, BigDecimal> importi, boolean capitoloFondino) {
		// SIAC-6883
		final String methodName = "checkVariazioneImporti";
		Capitolo<?,?> c = dettVarImp.getCapitolo();
		
		boolean isCapitoloUscita = TipoCapitolo.isTipoCapitoloUscita(c);
		
		//SIAC-7277
		if(isCapitoloUscita && capitoloFondino) {
			//SIAC-7792 si passa il DettaglioVariazioneImportoCapitolo
			checkVariazioniCapitoliFondino(dettVarImp);
			log.info(methodName, "capitolo fondino, salto il controllo sugli importi");
			return;
		}
		
		
		BigDecimal disponibilitaVariare;
		BigDecimal disponibilitaVariare1;
		BigDecimal disponibilitaVariare2;
		// Per cassa e residuo controllo solo per l'anno di bilancio
		BigDecimal disponibilitaVariareCassa;
		BigDecimal disponibilitaVariareResiduo = c.getImportiCapitolo().getStanziamentoResiduo();
		
		//SIAC-7267
		if(isCapitoloUscita) {
			//SIAC-7658
			ImportiCapitolo importiCapitoloUscita = c.getImportiCapitolo();
			disponibilitaVariare = importiCapitoloUscita.getDisponibilitaVariareAnno1();
			disponibilitaVariare1 = importiCapitoloUscita.getDisponibilitaVariareAnno2();
			disponibilitaVariare2 = importiCapitoloUscita.getDisponibilitaVariareAnno3();
			disponibilitaVariareCassa = importiCapitoloUscita.getDisponibilitaVariareCassa();
		} else {
			// Per l'entrata mi interessa controllare solo che non vadano a 0 gli stanziamenti.
			disponibilitaVariare = c.getImportiCapitolo().getStanziamento();
			disponibilitaVariare1 = c.findImportiCapitoloPerAnno(Integer.valueOf(variazione.getBilancio().getAnno() + 1)).getStanziamento();
			disponibilitaVariare2 = c.findImportiCapitoloPerAnno(Integer.valueOf(variazione.getBilancio().getAnno() + 2)).getStanziamento();
			disponibilitaVariareCassa = c.getImportiCapitolo().getStanziamentoCassa();
		}
		
		//SIAC-7267: i capitoli fondino non usano la disponibilita a variare, non devo adeguarla
		boolean capitoloPrecedentementeAssociatoAVariazione = variazioniDad.checkCapitoloAssociatoAllaVariazione(c.getUid(), variazione.getUid());
		log.debug(methodName, "il capitolo con uid " + c.getUid() +  " e' stato precedentemente associato alla variazione? " + capitoloPrecedentementeAssociatoAVariazione);
		
		if(isCapitoloUscita && capitoloPrecedentementeAssociatoAVariazione) {
			//adeguo la disponibilita' a variare solo se il capitolo e' gia associato alla variazione SIAC-3375
			BigDecimal stanziamento = importi.get(variazione.getBilancio().getAnno() + "_" + c.getUid() + "_" + "STA");
			BigDecimal stanziamento1 = importi.get((variazione.getBilancio().getAnno() + 1) + "_" + c.getUid() + "_" + "STA");
			BigDecimal stanziamento2 = importi.get((variazione.getBilancio().getAnno() + 2) + "_" + c.getUid() + "_" + "STA");
			BigDecimal stanziamentoCassa = importi.get(variazione.getBilancio().getAnno() + "_" + c.getUid() + "_" + "SCA");
			
			log.debug(methodName, "Adeguamento disponibilita variare e cassa per capitolo con uid: " + c.getUid());
			log.debug(methodName, "Adeguo la disponibilitaVariare prendendo come stanziamento " + stanziamento +  " e disponibilitaVariare " + disponibilitaVariare + "per il capitolo con uid: " + c.getUid());
			
			//SIAC-3044
			disponibilitaVariare = adeguaDisponibilitaVariare(stanziamento, disponibilitaVariare, "disponibilitaVariare", c, 0);
			disponibilitaVariare1 = adeguaDisponibilitaVariare(stanziamento1, disponibilitaVariare1, "disponibilitaVariare", c, 1);
			disponibilitaVariare2 = adeguaDisponibilitaVariare(stanziamento2, disponibilitaVariare2, "disponibilitaVariare", c, 2);
			disponibilitaVariareCassa = adeguaDisponibilitaVariare(stanziamentoCassa, disponibilitaVariareCassa, "disponibilitaVariareCassa", c, 0);
			
			log.debug(methodName, "Adeguo la disponibilitaVariareCassa prendendo come  stanziamentoCassa " + stanziamentoCassa +  " e disponibilitaVariareCassa " + disponibilitaVariareCassa + "per il capitolo con uid: " + c.getUid());
		}
		
		log.debug(methodName, "check Stanziamento: disponibilitaVariare + variazioneStanziamento >= 0 ");
		
		Errore errore = getErroreTemplate(dettVarImp);
		//SIAC-7349 - Start - SR220 - MR - 29/04/2020: Check disponibilita variare di ogni singola componente
		if(isCapitoloUscita){
			checkDisponibilitaVariareComponenti(dettVarImp, c, errore);			
		}
		//END SIAC-7349
		checkVariazioneImporto(disponibilitaVariare, dettVarImp.getStanziamento(), errore, "stanziamento", variazione.getBilancio().getAnno());
		checkVariazioneImporto(disponibilitaVariare1, dettVarImp.getStanziamento1(), errore, "stanziamento", variazione.getBilancio().getAnno() + 1);
		checkVariazioneImporto(disponibilitaVariare2, dettVarImp.getStanziamento2(), errore, "stanziamento", variazione.getBilancio().getAnno() + 2);
		
		checkVariazioneImporto(disponibilitaVariareResiduo, dettVarImp.getStanziamentoResiduo(), errore, "residuo", variazione.getBilancio().getAnno());
		checkVariazioneImporto(disponibilitaVariareCassa,dettVarImp.getStanziamentoCassa(), errore, "cassa", variazione.getBilancio().getAnno());
		
		
		
		
		
		boolean isCassaOk = dettVarImp.getStanziamento().setScale(2,RoundingMode.HALF_DOWN)
			.add(dettVarImp.getStanziamentoResiduo().setScale(2,RoundingMode.HALF_DOWN))
			.compareTo(dettVarImp.getStanziamentoCassa().setScale(2,RoundingMode.HALF_DOWN))<=0;
		
		log.debug(methodName, "check Competenza + Residuo <= Cassa ? "+ isCassaOk);
		
		// TODO: segnalazione SIAC-2105 - controllo non chiaro. In attesa di nuove indicazioni
//		if(!isCassaOk) {
//			throw new BusinessException(ErroreBil.OPERAZIONE_NON_POSSIBILE.getErrore("l'importo di Cassa deve essere maggiore o uguale all'importo di competenza piu'' l'importo residuo"));
//		}
	}

	@Deprecated
	private Set<ImportiCapitoloEnum> getImportiCapitoloNecessariByTipoCapitolo(Capitolo<?, ?> c) {
		if(TipoCapitolo.isTipoCapitoloUscita(c)){
			return EnumSet.of(
					ImportiCapitoloEnum.disponibilitaVariareAnno1,
					ImportiCapitoloEnum.disponibilitaVariareAnno2,
					ImportiCapitoloEnum.disponibilitaVariareAnno3,
					ImportiCapitoloEnum.disponibilitaVariareCassa);
		}
		return EnumSet.noneOf(ImportiCapitoloEnum.class);
	}
	
	
	//SIAC-7349 - SR240 - MR - Start - Metodo per check della disponibilita a variare delle componenti, solo per capitoli di uscita
	protected void checkDisponibilitaVariareComponenti(DettaglioVariazioneImportoCapitolo dettVarImp, Capitolo<?, ?> c,
			Errore errore) {
		List<DettaglioVariazioneComponenteImportoCapitolo> listaDettaglioComponenteAnno1 = dettVarImp.getListaDettaglioVariazioneComponenteImportoCapitolo();
		List<DettaglioVariazioneComponenteImportoCapitolo> listaDettaglioComponenteAnno2 = dettVarImp.getListaDettaglioVariazioneComponenteImportoCapitolo1();
		List<DettaglioVariazioneComponenteImportoCapitolo> listaDettaglioComponenteAnno3 = dettVarImp.getListaDettaglioVariazioneComponenteImportoCapitolo2();
		
		List<Integer> listaUidComponenti = new ArrayList<Integer>();
		for(DettaglioVariazioneComponenteImportoCapitolo dvcp : listaDettaglioComponenteAnno1){
			if(!dvcp.isFlagNuovaComponenteCapitolo()){
				listaUidComponenti.add(dvcp.getComponenteImportiCapitolo().getTipoComponenteImportiCapitolo().getUid());				
			}
		}
		
		Map<Integer, BigDecimal> disponibilitaComponentiAnno1 = new HashMap<Integer, BigDecimal>();
		Map<Integer, BigDecimal> disponibilitaComponentiAnno2 = new HashMap<Integer, BigDecimal>();
		Map<Integer, BigDecimal> disponibilitaComponentiAnno3 = new HashMap<Integer, BigDecimal>();
		
		boolean isCapitoloGestione = TipoCapitolo.isTipoCapitoloGestione(c.getTipoCapitolo());
		
		for(Integer uidComponente : listaUidComponenti){
			//SIAC-8120
			try {
				String importoDispCompFunction = (isCapitoloGestione==false) ? ImportoDerivatoFunctionEnum.disponibilitaVariareAnnoComponenteUP.getFunctionName() : ImportoDerivatoFunctionEnum.disponibilitaVariareAnnoComponente.getFunctionName();
				//Anno0
				importoDispCompFunction = importoDispCompFunction+String.valueOf(1);
				BigDecimal dispVarCompAnno1 = importiCapitoloDad.findDisponibilitaVariareComponente(c.getUid(), uidComponente, importoDispCompFunction);
				if (disponibilitaComponentiAnno1.get(uidComponente) == null) {
					disponibilitaComponentiAnno1.put(uidComponente, dispVarCompAnno1);
				}
				//Anno1
				importoDispCompFunction = (isCapitoloGestione==false) ? ImportoDerivatoFunctionEnum.disponibilitaVariareAnnoComponenteUP.getFunctionName() : ImportoDerivatoFunctionEnum.disponibilitaVariareAnnoComponente.getFunctionName();
				importoDispCompFunction = importoDispCompFunction+String.valueOf(2);
				BigDecimal dispVarCompAnno2 = importiCapitoloDad.findDisponibilitaVariareComponente(c.getUid(), uidComponente, importoDispCompFunction);
				if (disponibilitaComponentiAnno2.get(uidComponente) == null) {
					disponibilitaComponentiAnno2.put(uidComponente, dispVarCompAnno2);
				}
				//Anno2
				importoDispCompFunction = (isCapitoloGestione==false) ? ImportoDerivatoFunctionEnum.disponibilitaVariareAnnoComponenteUP.getFunctionName() : ImportoDerivatoFunctionEnum.disponibilitaVariareAnnoComponente.getFunctionName();
				importoDispCompFunction = importoDispCompFunction+String.valueOf(3);
				BigDecimal dispVarCompAnno3 = importiCapitoloDad.findDisponibilitaVariareComponente(c.getUid(), uidComponente, importoDispCompFunction);
				if (disponibilitaComponentiAnno3.get(uidComponente) == null) {
					disponibilitaComponentiAnno3.put(uidComponente, dispVarCompAnno3);
				}
			} catch (Exception e) {
				log.error("checkDisponibilitaVariareComponenti", "Impossibile caricare la disponibilita' ad impegnare per la componente "
						+ "su uno o piu' anni di bilancio. Verificare il capitolo con uid:" 
							+ c.getUid() + " con uid componente: " + uidComponente);
				throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Impossibile caricare la disponibilita' ad impegnare per la componente su uno o piu' anni di bilancio, verificare i dati sul capitolo."));
			}
			
		}
		
		checkVariazioneImportoComponente(disponibilitaComponentiAnno1, listaDettaglioComponenteAnno1, errore, "stanziamento",  variazione.getBilancio().getAnno());
		checkVariazioneImportoComponente(disponibilitaComponentiAnno2, listaDettaglioComponenteAnno2, errore,"stanziamento", variazione.getBilancio().getAnno() + 1);
		checkVariazioneImportoComponente(disponibilitaComponentiAnno3, listaDettaglioComponenteAnno3, errore,"stanziamento", variazione.getBilancio().getAnno() + 2);
	}
	//End SIAC-7349
	
	//SIAC-7349 - Start - SR220 - MR - 30/04/2020: Metodo che effettua il check della disponibilita componente
	private void checkVariazioneImportoComponente(Map<Integer, BigDecimal> disponibilitaComponentiAnno,
			List<DettaglioVariazioneComponenteImportoCapitolo> listaDettaglioComponenteAnno, Errore errore, String tipoStanziamento,  int anno) {
		final String methodName = "checkVariazioneImportoComponente";
		String msgLog = "ANNO %d. TIPO %s. %.2f + (%.2f) >= 0 ? %s";
		
		String erroreFormat = "%s Disponibilita' insufficiente per componente %s - (anno %d): %.2f; importo variazione (%s): %.2f";
		
		for(DettaglioVariazioneComponenteImportoCapitolo dvcic : listaDettaglioComponenteAnno){
			BigDecimal importoVariazione = dvcic.getImporto();
			int uidComponente = dvcic.getComponenteImportiCapitolo().getTipoComponenteImportiCapitolo().getUid();
			BigDecimal disponibilitaComponente = disponibilitaComponentiAnno.get(uidComponente);
			if(disponibilitaComponente !=null){ //se null, significa che si tratta di una nuova componente. 
				String nomeComponente = dvcic.getComponenteImportiCapitolo().getTipoComponenteImportiCapitolo().getDescrizione();
				boolean isOk = disponibilitaComponente.setScale(2,RoundingMode.HALF_DOWN).add(importoVariazione.setScale(2,RoundingMode.HALF_DOWN)).compareTo(BigDecimal.ZERO) >= 0;
				if(!isOk){
					String err = String.format(erroreFormat, errore.getCodice(), nomeComponente, anno, disponibilitaComponente, tipoStanziamento, importoVariazione);
					errore.setDescrizione(err);
					throw new BusinessException(errore);
				}
				
			}
			
	
		}
		
		
	}


	protected BigDecimal adeguaDisponibilitaVariare(BigDecimal stanziamento, BigDecimal disponibilita, String tipoDisponibilita, Capitolo<?, ?> capitolo, int delta) {
		//SIAC-3044
		final String methodName = "adeguaDisponibilitaVariare";
		
		if(variazione.getUid() == 0){
			log.debug(methodName, "Sto inserendo. Non devo adeguare l'importo.");
			return disponibilita;
		}
		if(stanziamento == null) {
			String baseErrorMsg = "esiste un'incongruenza nei dati. Impossibile ottenere gli importi variati (" + tipoDisponibilita + " , anno " + (variazione.getBilancio().getAnno() + delta) + ")";
			log.error(methodName, baseErrorMsg + " per il capitolo " + capitolo.getUid());
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("capitolo in variazione", baseErrorMsg));
		}
		log.debug(methodName, "Adeguo la " + tipoDisponibilita + " prendendo come stanziamento " + stanziamento +  " e disponibilita' " + disponibilita + " per il capitolo " + capitolo.getUid()
			+ " per l'anno " + (variazione.getBilancio().getAnno() + delta));
		
		
		//Sono in aggiornamento di una variazione.
		StatoOperativoVariazioneBilancio statoAttuale = variazioniDad.findStatoOperativoVariazioneDiBilancio(variazione);
		
		//solo se la variazione e' negativa
		if(stanziamento.compareTo(BigDecimal.ZERO) < 0
				&& (StatoOperativoVariazioneBilancio.BOZZA.equals(statoAttuale)
						|| StatoOperativoVariazioneBilancio.GIUNTA.equals(statoAttuale)
						|| StatoOperativoVariazioneBilancio.CONSIGLIO.equals(statoAttuale)
						// CR-3260
						|| StatoOperativoVariazioneBilancio.PRE_DEFINITIVA.equals(statoAttuale)
					)){ 
			BigDecimal disponibilitaVariareNew = disponibilita.setScale(2,RoundingMode.HALF_DOWN)
					.add(stanziamento.setScale(2,RoundingMode.HALF_DOWN).abs());
			
			log.info(methodName, "Variazione in " + statoAttuale + ". La disponibilitaVariare viene calcolata sottraendo il valore dello stanziamento della varizione. " 
					+ Utility.formatCurrencyAsString(disponibilita) + " (PRIMA) > " + Utility.formatCurrencyAsString(disponibilitaVariareNew) + " (DOPO)");
			
			return disponibilitaVariareNew;
		}
		return disponibilita;
	}
	
	private Errore getErroreTemplate(DettaglioVariazioneImportoCapitolo dettVarImp) {
		//SIAC-3227
		String descCapitolo;
		if(isGestioneUEB()){
			descCapitolo = dettVarImp.getCapitolo().getDescBilancioAnnoNumeroArticoloUEB();
		} else {
			descCapitolo = dettVarImp.getCapitolo().getDescBilancioAnnoNumeroArticolo();
		}
		
		return ErroreBil.DISPONIBILITA_INSUFFICIENTE.getErrore("Stanziamento insufficiente per il capitolo " + descCapitolo.replaceFirst("null/", "") + ".");
	}

	/**
	 * Se disponibilitaVariare sommato a importoVariazione restituisce un numero negativo viene sollevata l'eccezione di business con l'errore passato come parametro.
	 *
	 * @param disponibilitaVariareCheck la disponibilita a variare
	 * @param importoVariazione  l'importo della variazione
	 * @param errore l'errore di business da sollevare.
	 */
	protected void checkVariazioneImporto(BigDecimal disponibilitaVariare, BigDecimal importoVariazione, Errore errore, String tipoStanziamento, int anno) {
		final String methodName = "checkVariazioneImporto";
		
		String msgLog = "ANNO %d. TIPO %s. %.2f + (%.2f) >= 0 ? %s";
		
		if(importoVariazione == null) {
			log.debug(methodName, String.format(msgLog, anno, tipoStanziamento, disponibilitaVariare, null, null));
			return;
		}
		BigDecimal disponibilitaVariareCheck = disponibilitaVariare == null ? BigDecimal.ZERO : disponibilitaVariare;
		
		boolean isOk = disponibilitaVariareCheck.setScale(2,RoundingMode.HALF_DOWN).add(importoVariazione.setScale(2,RoundingMode.HALF_DOWN)).compareTo(BigDecimal.ZERO) >= 0;
		
		log.debug(methodName,  String.format(msgLog, anno, tipoStanziamento, disponibilitaVariareCheck, importoVariazione, isOk));
		String erroreFormat = "%sDisponibilita' variare (anno %d, %s): %.2f; importo variazione (%s): %.2f";
		
		if(!isOk){
			errore.setDescrizione(String.format(erroreFormat, errore.getDescrizione(), anno, tipoStanziamento, disponibilitaVariareCheck, tipoStanziamento, importoVariazione));
//			throw new BusinessException(errore);
		}
	}
	
	/**
	 * Controlla se la differenza tra gli stanziamenti di entrata e uscita &eacute; uguale a zero.
	 * 
	 * @return true se la quadratura &egrave; corretta false altrimenti.
	 * 
	 */
	protected boolean isQuadraturaCorrettaStanziamento() {
		final String methodName = "isQuadraturaCorrettaStanziamento";
		BigDecimal sommaStanziamentoEntrata = variazione.calcolaSommaStanziamentoEntrata();
		BigDecimal sommaStanziamentoUscita = variazione.calcolaSommaStanziamentoUscita();
		log.debug(methodName, "Quadratura per anno 0. E: " + Utility.formatCurrencyAsString(sommaStanziamentoEntrata) + " U: " + Utility.formatCurrencyAsString(sommaStanziamentoUscita));
		
		if(sommaStanziamentoEntrata.subtract(sommaStanziamentoUscita).compareTo(BigDecimal.ZERO) != 0) {
			log.info(methodName, "Quadratura NON corretta per anno 0. [stanziamento E: " + sommaStanziamentoEntrata + " U:" + sommaStanziamentoUscita + "]");
			return false;
		}
		
		// SIAC-6883: aggiunti controlli per anno + 1 e anno + 2
		sommaStanziamentoEntrata = variazione.calcolaSommaStanziamentoEntrata1();
		sommaStanziamentoUscita = variazione.calcolaSommaStanziamentoUscita1();
		log.debug(methodName, "Quadratura per anno 1. E: " + Utility.formatCurrencyAsString(sommaStanziamentoEntrata) + " U: " + Utility.formatCurrencyAsString(sommaStanziamentoUscita));
		
		if(sommaStanziamentoEntrata.subtract(sommaStanziamentoUscita).compareTo(BigDecimal.ZERO) != 0) {
			log.info(methodName, "Quadratura NON corretta per anno 1. [stanziamento E: " + sommaStanziamentoEntrata + " U:" + sommaStanziamentoUscita + "]");
			return false;
		}
		
		sommaStanziamentoEntrata = variazione.calcolaSommaStanziamentoEntrata2();
		sommaStanziamentoUscita = variazione.calcolaSommaStanziamentoUscita2();
		log.debug(methodName, "Quadratura per anno 2. E: " + Utility.formatCurrencyAsString(sommaStanziamentoEntrata) + " U: " + Utility.formatCurrencyAsString(sommaStanziamentoUscita));
		
		if(sommaStanziamentoEntrata.subtract(sommaStanziamentoUscita).compareTo(BigDecimal.ZERO) != 0) {
			log.info(methodName, "Quadratura NON corretta per anno 2. [stanziamento E: " + sommaStanziamentoEntrata + " U:" + sommaStanziamentoUscita + "]");
			return false;
		}
		log.info(methodName, "Quadratura corretta per i tre anni");
		return true;
	}
	
	/**
	 * Controlla se la differenza tra gli stanziamenti cassa di entrata e uscita &eacute; uguale a zero.
	 * 
	 * @return true se la quadratura &acute; corretta false altrimenti.
	 * 
	 */
	protected boolean isQuadraturaCorrettaStanziamentoCassa() {
		final String methodName = "isQuadraturaCorrettaStanziamentoCassa";
		BigDecimal sommaStanziamentoCassaEntrata = variazione.calcolaSommaStanziamentoCassaEntrata();
		BigDecimal sommaStanziamentoCassaUscita = variazione.calcolaSommaStanziamentoCassaUscita();	

		log.debug(methodName, "Quadratura cassa per anno 0. E: " + Utility.formatCurrencyAsString(sommaStanziamentoCassaEntrata) + " U: " + Utility.formatCurrencyAsString(sommaStanziamentoCassaUscita));
		
		if(sommaStanziamentoCassaEntrata.subtract(sommaStanziamentoCassaUscita).compareTo(BigDecimal.ZERO) == 0){
			log.info(methodName, "Quadratura corretta. [cassa E:"+sommaStanziamentoCassaEntrata + " U:"+sommaStanziamentoCassaUscita+ "]");
			return true;
		}
		
		log.info(methodName, "Quadratura NON corretta. [cassa E:"+sommaStanziamentoCassaEntrata + " U:"+sommaStanziamentoCassaUscita+ "]");
		return false;
	}
	
	
	//--- Controllo presenza Atto Amministrativo --------------------------------------------------------------------------------
	
	/**
	 * Il check di presenza dell'atto amministrativo va effettuato se tra i capitoli di spesa coinvolti nella variazione ci sono:
	 * <ol>
	 *     <li>capitoli con missione o programma differenti &eacute; obbligatorio un atto autorizzativo</li>
	 *     <li>capitoli con titolo o tipologie differenti &eacute; obbligatorio un atto autorizzativo.</li>
	 * </ol>
	 */
	protected void checkNecessarioAttoAmministrativoVariazioneDiBilancio() {
		if(isMissioneOProgrammaDifferenti() || isTitoloOTipologieDifferenti()){
			log.debug("checkNecessarioAttoAmministrativo", "Controllo l'atto amministrativo");
			checkParamAttoAmministrativo();
		}
		
	}

	/**
	 * Check param atto amministrativo.
	 */
	protected void checkParamAttoAmministrativo() {
		try{
			checkEntita(variazione.getAttoAmministrativo(), "atto amministrativo variazione");
		} catch(ServiceParamError spe){
			throw new BusinessException(spe.getErrore(),Esito.FALLIMENTO);
		}
	}	
	
	/**
	 * Checks if is missione o programma differenti.
	 *
	 * @return true, if is missione o programma differenti
	 */
	protected boolean isMissioneOProgrammaDifferenti() {
		return isCapitoliWithProgrammaDifferenti()
				|| isCapitoliWithMissioneDifferenti();
	}

	/**
	 * Checks if is capitoli with missione O programma differenti.
	 * @return <code>true <code>, se i capitoli hanno missione o progframma differenti, <code>false</code> altrimenti
	 */
	protected boolean isCapitoliWithProgrammaDifferenti() {
		final String methodName = "isCapitoliWithProgrammaDifferenti";
		Long programmaDifferenti = variazioniDad.countDifferentClassificatoriProgramma(variazione.getUid());
		log.debug(methodName, "Numero differenti programmi: " + programmaDifferenti);
		return programmaDifferenti.longValue() > 1L;
	}
	
	/**
	 * Checks if is capitoli with missione differenti.
	 * @return <code>true <code>, se i capitoli hanno missione differenti, <code>false</code> altrimenti
	 */
	protected boolean isCapitoliWithMissioneDifferenti() {
		final String methodName = "isCapitoliWithProgrammaDifferenti";
		Long missioneDifferenti = variazioniDad.countDifferentClassificatoriMissione(variazione.getUid());
		log.debug(methodName, "Numero differenti missioni: " + missioneDifferenti);
		return missioneDifferenti.longValue() > 1L;
	}
	
	/**
	 * Controlla se i capitoli passati in input abbiano titolo differenti.
	 * @return <code>true</code> se esiste almeno un capitolo con titolo macroaggregato differente dagli altri
	 */
	protected boolean isCapitoliWithTitoloSpesaDifferenti() {
		final String methodName = "isCapitoliWithTitoloSpesaDifferenti";
		Long titoloSpesaDifferenti = variazioniDad.countDifferentClassificatoriTitoloSpesa(variazione.getUid());
		log.debug(methodName, "Numero differenti titoli spesa: " + titoloSpesaDifferenti);
		return titoloSpesaDifferenti.longValue() > 1L;
	}
	

	/**
	 * Checks if is titolo o tipologie differenti.
	 *
	 * @return true, if is titolo o tipologie differenti
	 */
	protected boolean isTitoloOTipologieDifferenti() {
		return isCapitoliWithTitoloEntrataDifferenti()
				|| isCapitoliWithTipologieDifferenti();
	}

	/**
	 * Controlla se i capitoli passati in input abbiano titolo tipologie differenti.
	 *
	 * @return <code>true</code> se esiste almeno un capitolo con titolo tipologia differente dagli altri
	 */
	public boolean isCapitoliWithTipologieDifferenti() {
		final String methodName = "isCapitoliWithTipologieDifferenti";
		Long tipologiaTitoloDifferenti = variazioniDad.countDifferentClassificatoriTipologiaTitolo(variazione.getUid());
		log.debug(methodName, "Numero differenti tipologia titolo: " + tipologiaTitoloDifferenti);
		return tipologiaTitoloDifferenti.longValue() > 1L;
	}
	
	/**
	 * Controlla se i capitoli passati in input abbiano titolo tipologie differenti.
	 *
	 * @return <code>true</code> se esiste almeno un capitolo con titolo tipologia differente dagli altri
	 */
	public boolean isCapitoliWithTitoloEntrataDifferenti() {
		final String methodName = "isCapitoliWithTitoloEntrataDifferenti";
		Long titoloEntrataDifferenti = variazioniDad.countDifferentClassificatoriTitoloEntrata(variazione.getUid());
		log.debug(methodName, "Numero differenti titolo entrata: " + titoloEntrataDifferenti);
		return titoloEntrataDifferenti.longValue() > 1L;
	}
	
	protected boolean isProvvedimentoPresenteDefinitivo() {
		if(variazione.getAttoAmministrativo() == null || variazione.getAttoAmministrativo().getUid() == 0){
			return false;
		}
		// Provvedimento presente. Verifico lo stato DEFINITIVO
		StatoOperativoAtti statoOperativoAtti = attoAmministrativoDad.findStatoOperativoAttoAmministrativo(variazione.getAttoAmministrativo());
		return StatoOperativoAtti.DEFINITIVO.equals(statoOperativoAtti);
	}
	
	
	//--- Aggiornamento e caricamento importi del Capitolo -------------------------------------------------------------------------------
	

	



	/**
	 * Carica dentro variazione.getCapitoli().getImportiCapitolo() 
	 * gli importi del capitolo dello stesso anno di competenza della variazione reperendoli dal database.
	 */
	protected void caricaDettaglioImportiCapitoli() {
		for(DettaglioVariazioneImportoCapitolo dv : variazione.getListaDettaglioVariazioneImporto()) {
			caricaDettaglioImportiCapitolo(dv);
		}
	}

	@SuppressWarnings("unchecked")
	protected void caricaDettaglioImportiCapitolo(DettaglioVariazioneImportoCapitolo dv) {
		// SIAC-6883: carico i dettagli degli importi della variazione.
		ImportiCapitolo importiCapitolo = caricaDettaglioImportiCapitolo(dv.getCapitolo(), variazione.getBilancio().getAnno());
		ImportiCapitolo importiCapitolo1 = caricaDettaglioImportiCapitolo(dv.getCapitolo(), variazione.getBilancio().getAnno() + 1);
		ImportiCapitolo importiCapitolo2 = caricaDettaglioImportiCapitolo(dv.getCapitolo(), variazione.getBilancio().getAnno() + 2);
		
		dv.getCapitolo().setImportiCapitolo(importiCapitolo);
		dv.getCapitolo().getListaImportiCapitolo().add(importiCapitolo);
		dv.getCapitolo().getListaImportiCapitolo().add(importiCapitolo1);
		dv.getCapitolo().getListaImportiCapitolo().add(importiCapitolo2);
	}	

	/**
	 * Carica dettaglio importi capitolo.
	 *
	 * @param capitolo the capitolo
	 * @param annoDiCompetenza the anno di competenza
	 * @return the importi capitolo
	 */
	protected ImportiCapitolo caricaDettaglioImportiCapitolo(@SuppressWarnings("rawtypes") Capitolo capitolo, int annoDiCompetenza) {
		final String methodName = "caricaDettaglioImportiCapitolo";
		
		ImportiCapitolo importiCapitolo =  importiCapitoloDad.findImportiCapitolo(capitolo, annoDiCompetenza, ImportiCapitolo.class, getImportiCapitoloNecessariByTipoCapitolo(capitolo), true);
		
		log.debug(methodName, "annoDiCompetenza: "+ annoDiCompetenza + " [" + (importiCapitolo != null ? importiCapitolo.getAnnoCompetenza() : "null") + "]");
		checkBusinessCondition(importiCapitolo != null, ErroreCore.ERRORE_DI_SISTEMA.getErrore("Impossibile trovare il dettaglio degli importi dell'anno " + variazione.getBilancio().getAnno()
				+ "per il capitolo avente uid: " + capitolo.getUid()));
		
		return importiCapitolo;
	}
	

	//----------------- Gestione processo
	
	/**
	 * Imposta una variabile di processo con nome e valori passati come parametro.
	 * Se la variabile esiste ne cambia il valore, altrimenti ne crea una nuova.
	 *
	 * @param azione the azione
	 * @param nome the nome
	 * @param valore the valore
	 * @deprecated by SIAC-8332
	 */
	@Deprecated
	protected void setVariabileProcesso(AzioneRichiesta azione, String nome, Object valore) {
		
		VariabileProcesso variabile = new VariabileProcesso();
		
		for(VariabileProcesso vp : azione.getVariabiliProcesso() ) {
			if (nome.equals(vp.getNome())) {
				variabile = vp;
			}
		}
		
		variabile.setNome(nome);
		variabile.setValore(valore);
		
		azione.getVariabiliProcesso().add(variabile);
	}

	/**
	 * Gets the azione richiesta.
	 *
	 * @param uidAzioneRichiesta the uid azione richiesta
	 * @return the azione richiesta
	 */
	public AzioneRichiesta getAzioneRichiesta(int uidAzioneRichiesta) {
		
		GetAzioneRichiesta request = new GetAzioneRichiesta();
		request.setDataOra(new Date());
		request.setRichiedente(req.getRichiedente());
		
		AzioneRichiesta azioneRichiesta = new AzioneRichiesta();
		azioneRichiesta.setUid(uidAzioneRichiesta);
		request.setAzioneRichiesta(azioneRichiesta);
		
		GetAzioneRichiestaResponse response = coreService.getAzioneRichiesta(request);
		return response.getAzioneRichiesta();
	}
	
	//SIAC-7792 si passa il DettaglioVariazioneImportoCapitolo
	protected void checkVariazioniCapitoliFondino(DettaglioVariazioneImportoCapitolo dettVarImp){
		//Implementazione vuota, va implementata nelle sottoclassi
	}
	
	/*
	 *     _____ _____          _____            ______ ___ ______ ___  
	 *	  / ____|_   _|   /\   / ____|          |____  / _ \____  |__ \ 
	 *	 | (___   | |    /  \ | |       ______      / / (_) |  / /   ) |
	 *	  \___ \  | |   / /\ \| |      |______|    / / \__, | / /   / / 
	 *	  ____) |_| |_ / ____ \ |____             / /    / / / /   / /_ 
	 *	 |_____/|_____/_/    \_\_____|           /_/    /_/ /_/   |____|
     *                                                         
	 * 
	 * */

	//SIAC-7972
	protected void loadAndCheckImportiInDiminuzione() {
		final String methodName = "loadAndCheckImportiInDiminuzione";
		//SIAC-3044, modificato per mantenerne il comportamento SIAC-8332
		if(isAnnullamentoVariazione()){
			log.info(methodName, "Sto annullando la Variazione. Salto i check sugli importi e procedo.");
			return;
		}
		//carico le liste degli uid che mi serviranno
		List<Integer> idsCapitoliInVariazione = new ArrayList<Integer>();
		List<Integer> idsCapitoliSpesaInVariazione = new ArrayList<Integer>();
		for(DettaglioVariazioneImportoCapitolo dettVarImp : variazione.getListaDettaglioVariazioneImporto()){
			Capitolo<?,?> capitolo = dettVarImp.getCapitolo();
			idsCapitoliInVariazione.add(capitolo.getUid());			
			if(TipoCapitolo.isTipoCapitoloUscita(capitolo)) {
				idsCapitoliSpesaInVariazione.add(capitolo.getUid());
			}
		}
		List<Integer> uidsCapitoliFondino = capitoloDad.caricaListUidsCapitoliFondinoByUidsGruppoCapitoli(idsCapitoliSpesaInVariazione);
		
		//i capitoli fondino hanno un controllo tutto loro, che viene effettuato solo in aggiornamento della variazione di bilancio
		checkCapitoliFondinoInVariazione(uidsCapitoliFondino);
		
		controllaStanziamentiEComponenti(uidsCapitoliFondino);
		
		
	}

	protected abstract boolean isAnnullamentoVariazione();


	//SIAC-7972
	protected void controllaStanziamentiEComponenti(List<Integer> uidsCapitoliFondino) {
		//carico lo stato attuale della variazione
		//SIAC-8194
		StatoOperativoVariazioneBilancio statoAttualeVariazioneBilancio = variazioniDad.findStatoOperativoVariazioneDiBilancio(variazione);
		Map<Integer, List<WrapperImportiCapitoloVariatiInVariazione>> mappaImporti = 
				variazioniDad.findStanziamentoVariazioneInDiminuzioneUidVariazione(variazione.getUid(), uidsCapitoliFondino);
		controllaImportiInDiminuzione(mappaImporti,  statoAttualeVariazioneBilancio);
		
		Map<Integer, List<WrapperComponenteImportiCapitoloVariatiInVariazione>> mappaImportiComponenti = 
				variazioniDad.findStanziamentoComponentiVariazioneInDiminuzioneUidVariazione(variazione.getUid(),
						uidsCapitoliFondino);
		controllaImportoComponenteInDiminuzione(mappaImportiComponenti, statoAttualeVariazioneBilancio);
	}
	
	/**
	 * Controlla stanziamenti e le componenti a partire da un singolo dettaglio della variazione. Necessario in aggiornamento ed inserirmento di un dettaglio.
	 *
	 * @param dettaglio the dettaglio
	 */
	//SIAC-7972
	protected void controllaStanziamentiEComponenti(DettaglioVariazioneImportoCapitolo dettaglio) {
		
		if(BigDecimal.ZERO.compareTo(dettaglio.getStanziamento()) < 0 && BigDecimal.ZERO.compareTo(dettaglio.getStanziamento1()) < 0  && BigDecimal.ZERO.compareTo(dettaglio.getStanziamento2()) < 0 && BigDecimal.ZERO.compareTo(dettaglio.getStanziamentoCassa()) < 0){
			//tutti gli stanziamenti sono positivi, non e' necessario alcun controllo sulla disponiilita'. Esco.
			return;
		}
		StatoOperativoVariazioneBilancio statoAttualeVariazioneBilancio = variazioniDad.findStatoOperativoVariazioneDiBilancio(variazione);
		Map<Integer, List<WrapperImportiCapitoloVariatiInVariazione>> mappaImporti = getMappaImportiFromDettaglio(dettaglio);
		controllaImportiInDiminuzione(mappaImporti, statoAttualeVariazioneBilancio);
		
		if(TipoCapitolo.isTipoCapitoloEntrata(dettaglio.getCapitolo())) {
			//i capitoli di entrata non hanno le componenti, esco
			return;
		}
		
		Map<Integer, List<WrapperComponenteImportiCapitoloVariatiInVariazione>> mappaImportiComponenti = getMappaImportiComponentiFromDettaglio(dettaglio);
		controllaImportoComponenteInDiminuzione(mappaImportiComponenti, statoAttualeVariazioneBilancio);
	}
	
	//SIAC-7972
	protected void controllaImportiInDiminuzione(Map<Integer, List<WrapperImportiCapitoloVariatiInVariazione>> mappaImporti, StatoOperativoVariazioneBilancio statoAttualeVariazioneBilancio) {
		
		
		int annoBilancio = variazione.getBilancio().getAnno();
		//controllo prima i capitoli di entrata
		for (Integer idCapitolo : mappaImporti.keySet()) {
			List<WrapperImportiCapitoloVariatiInVariazione> importiVariati = mappaImporti.get(idCapitolo);
			if(importiVariati == null) {
				continue;
			}
			for (WrapperImportiCapitoloVariatiInVariazione wr : importiVariati) {
				int delta = wr.getAnnoInteger().intValue() - annoBilancio; 

				BigDecimal importoCapitolo = getImportoCapitoloInDiminuzione(idCapitolo, wr, delta, statoAttualeVariazioneBilancio);
				
				checkImporto(idCapitolo, wr, importoCapitolo);
				
			}
		}

	}
	
	//SIAC-7972: mantenuto de facto il comportamento precedente, solo adattato al nuovo raggruppamento degli importi.
	private boolean checkImporto(Integer idCapitolo, WrapperImportiCapitoloVariatiInVariazione wr, BigDecimal importoCapitolo) {
		final String methodName = "checkImporto";
		BigDecimal importoVariazione = wr.getImportoVariato() != null? wr.getImportoVariato() : BigDecimal.ZERO;
		
		String msgLog = "ANNO %d. TIPO %s. %.2f + (%.2f) >= 0 ? %s";
		
		if(importoVariazione == null) {
			log.debug(methodName, String.format(msgLog, wr.getAnnoInteger(), wr.getSiacDBilElemDetTipoEnum().getCodice(), importoCapitolo, null, null));
			return true;
		}
		BigDecimal disponibilitaVariareCheck = importoCapitolo == null ? BigDecimal.ZERO : importoCapitolo;
		
		boolean isOk = disponibilitaVariareCheck.setScale(2,RoundingMode.HALF_DOWN).add(importoVariazione.setScale(2,RoundingMode.HALF_DOWN)).compareTo(BigDecimal.ZERO) >= 0;
		
		log.debug(methodName,  String.format(msgLog, wr.getAnnoInteger(), wr.getSiacDBilElemDetTipoEnum().getCodice(), disponibilitaVariareCheck, importoVariazione, isOk));
		String erroreFormat = "%sDisponibilita' variare (anno %d, %s): %.2f; importo variazione (%s): %.2f";
		
		if(!isOk){
			Errore errore = getErrore(idCapitolo);
			errore.setDescrizione(String.format(erroreFormat, errore.getDescrizione(), wr.getAnnoInteger(), wr.getSiacDBilElemDetTipoEnum().getCodice(), disponibilitaVariareCheck, wr.getElemDetTipoCode(), importoVariazione));
			//SIAC-7972-TEST
			throw new BusinessException(errore);
		}
		return isOk;
	}
	
	/**
	 * Popola una mappa degli importi non a partire da quanto presente sul db, ma a partire dai dati del dettaglio
	 * (Necessario quando i dati presenti su db ancora non ci sono)
	 *
	 * @param dettaglio the dettaglio
	 * @return the mappa importi componenti from dettaglio
	 */
	//SIAC-7972
	private Map<Integer, List<WrapperComponenteImportiCapitoloVariatiInVariazione>> getMappaImportiComponentiFromDettaglio(DettaglioVariazioneImportoCapitolo dettaglio) {
		int annoImporto = variazione.getBilancio().getAnno();
		Map<Integer, List<WrapperComponenteImportiCapitoloVariatiInVariazione>> mappaImportiComponenti = new HashMap<Integer, List<WrapperComponenteImportiCapitoloVariatiInVariazione>>();
		TipoCapitolo tipoCapitolo = dettaglio.getCapitolo().getTipoCapitolo();
		Integer uidCapitolo = dettaglio.getCapitolo().getUid();
		
		addFromListDettaglioComponenti(annoImporto, mappaImportiComponenti, tipoCapitolo, uidCapitolo,	dettaglio.getListaDettaglioVariazioneComponenteImportoCapitolo());
		annoImporto++;
		addFromListDettaglioComponenti(annoImporto, mappaImportiComponenti, tipoCapitolo, uidCapitolo,	dettaglio.getListaDettaglioVariazioneComponenteImportoCapitolo1());
		annoImporto++;
		addFromListDettaglioComponenti(annoImporto, mappaImportiComponenti, tipoCapitolo, uidCapitolo,	dettaglio.getListaDettaglioVariazioneComponenteImportoCapitolo2());
		return mappaImportiComponenti;
	}

	//SIAC-7972
	private void addFromListDettaglioComponenti(int anno,Map<Integer, List<WrapperComponenteImportiCapitoloVariatiInVariazione>> mappaImportiComponenti,
			TipoCapitolo tipoCapitolo, Integer uidCapitolo, List<DettaglioVariazioneComponenteImportoCapitolo> listaDettaglioComponente) {
		if(listaDettaglioComponente == null) {
			return;
		}
		
		for(DettaglioVariazioneComponenteImportoCapitolo dvcp : listaDettaglioComponente){
			if(!dvcp.isFlagNuovaComponenteCapitolo() && BigDecimal.ZERO.compareTo(dvcp.getImporto()) >0 ){
				WrapperComponenteImportiCapitoloVariatiInVariazione wr = new WrapperComponenteImportiCapitoloVariatiInVariazione(dvcp.getComponenteImportiCapitolo().getTipoComponenteImportiCapitolo().getUid(),  SiacDBilElemTipoEnum.byTipoCapitolo(tipoCapitolo).getCodice(), "" + anno, "STA", dvcp.getImporto(), dvcp.getComponenteImportiCapitolo().getTipoComponenteImportiCapitolo().getDescrizione());
				if(mappaImportiComponenti.get(uidCapitolo) == null) {
					mappaImportiComponenti.put(uidCapitolo, new ArrayList<WrapperComponenteImportiCapitoloVariatiInVariazione>());
				}
				mappaImportiComponenti.get(uidCapitolo).add(wr);
			}
		}
		
	}
	
	//SIAC-7972
	private Map<Integer, List<WrapperImportiCapitoloVariatiInVariazione>> getMappaImportiFromDettaglio(DettaglioVariazioneImportoCapitolo dettaglio) {
		int annoBilancio = variazione.getBilancio().getAnno();
		Map<Integer, List<WrapperImportiCapitoloVariatiInVariazione>> mappaImporti = new HashMap<Integer, List<WrapperImportiCapitoloVariatiInVariazione>>();
		TipoCapitolo tipoCapitolo = dettaglio.getCapitolo().getTipoCapitolo();
		Integer uidCapitolo = dettaglio.getCapitolo().getUid();
		mappaImporti.put(uidCapitolo, new ArrayList<WrapperImportiCapitoloVariatiInVariazione>());
		
		addToWrapperFromDettaglio(dettaglio, mappaImporti, tipoCapitolo, uidCapitolo, dettaglio.getStanziamento(), "STA", "" +annoBilancio );
		addToWrapperFromDettaglio(dettaglio, mappaImporti, tipoCapitolo, uidCapitolo, dettaglio.getStanziamentoCassa(), "SCA", "" +annoBilancio );
		//pare non fosse utilizzato precedentemente, lo commento, riabilitato SIAC-8736
		addToWrapperFromDettaglio(dettaglio, mappaImporti, tipoCapitolo, uidCapitolo, dettaglio.getStanziamentoResiduo(), "STR", "" +annoBilancio );
		annoBilancio++;
		addToWrapperFromDettaglio(dettaglio, mappaImporti, tipoCapitolo, uidCapitolo, dettaglio.getStanziamento1(), "STA", "" +annoBilancio );
		annoBilancio++;
		addToWrapperFromDettaglio(dettaglio, mappaImporti, tipoCapitolo, uidCapitolo, dettaglio.getStanziamento2(), "STA", "" +annoBilancio );
		return mappaImporti;
	}

	//SIAC-7972
	private void addToWrapperFromDettaglio(DettaglioVariazioneImportoCapitolo dettaglio,
			Map<Integer, List<WrapperImportiCapitoloVariatiInVariazione>> mappaImporti, TipoCapitolo tipoCapitolo,
			Integer uidCapitolo, BigDecimal stanziamento, String tipoImporto, String anno) {
		if(BigDecimal.ZERO.compareTo(stanziamento) >0) {
			WrapperImportiCapitoloVariatiInVariazione wr = new WrapperImportiCapitoloVariatiInVariazione(SiacDBilElemTipoEnum.byTipoCapitolo(tipoCapitolo).getCodice(), anno, tipoImporto, stanziamento);
			mappaImporti.get(uidCapitolo).add(wr);
		}
	}
	
	
	
	//SIAC-7972
	private void controllaImportoComponenteInDiminuzione(Map<Integer, List<WrapperComponenteImportiCapitoloVariatiInVariazione>> mappaImportiComponenti, StatoOperativoVariazioneBilancio statoAttualeVariazioneBilancio) {
		int annoBilancio = variazione.getBilancio().getAnno();
		
		for (Integer uidCapitolo : mappaImportiComponenti.keySet()) {
			for (WrapperComponenteImportiCapitoloVariatiInVariazione wr : mappaImportiComponenti.get(uidCapitolo)) {
				int delta = wr. getAnnoInteger().intValue() - annoBilancio; 
				//SIAC-8194
				BigDecimal disponibilitaComponente = getImportoComponente(uidCapitolo, wr, delta, statoAttualeVariazioneBilancio);
				
				checkVariazioneImportoComponente(uidCapitolo, wr, disponibilitaComponente);
				
			}
			
		}
	}
	
	//SIAC-7972
	private boolean checkVariazioneImportoComponente(Integer idCapitolo, WrapperComponenteImportiCapitoloVariatiInVariazione wr, BigDecimal disponibilitaComponente) {
		final String methodName = "checkVariazioneImportoComponente";
		String msgLog = "ANNO %d. TIPO %s. %.2f + (%.2f) >= 0 ? %s";
		
		String erroreFormat = "%s Disponibilita' insufficiente per componente %s - (anno %d): %.2f; importo variazione (%s): %.2f";
		
		BigDecimal importoVariazione = wr.getImportoVariato();
		
		Errore errore = getErrore(idCapitolo);
		
		boolean isOk = disponibilitaComponente.setScale(2,RoundingMode.HALF_DOWN).add(importoVariazione.setScale(2,RoundingMode.HALF_DOWN)).compareTo(BigDecimal.ZERO) >= 0;
		log.debug(methodName,  String.format(msgLog, wr.getAnnoInteger(), wr.getSiacDBilElemDetTipoEnum().getCodice(), disponibilitaComponente, importoVariazione, isOk));
		
		if(!isOk){
			String err = String.format(erroreFormat, errore.getCodice(), wr.getTipoComponenteImportiCapitolo().getDescrizione(), wr.getAnnoInteger(), disponibilitaComponente, wr.getCodiceTipoImporto(), importoVariazione);
			log.info(methodName, err );
			errore.setDescrizione(err);
			//SIAC-7972- da commentare per i TEST
			throw new BusinessException(errore);
		}
		return isOk;
	}

	//SIAC-7972
	private BigDecimal getImportoComponente(Integer uidCapitolo, WrapperComponenteImportiCapitoloVariatiInVariazione wr, int delta, StatoOperativoVariazioneBilancio statoAttualeVariazioneBilancio) {
		final String methodName="getImportoComponente";
		int intervallo = delta + 1;
		String functionNameBase = wr.isSpesaPrevisione()? ImportoDerivatoFunctionEnum.disponibilitaVariareAnnoComponenteUP.getFunctionName() : ImportoDerivatoFunctionEnum.disponibilitaVariareAnnoComponente.getFunctionName();
		String functionNameComposed = functionNameBase + String.valueOf(intervallo);
		BigDecimal dispVariare =BigDecimal.ZERO; 
		try {
			 //OVVOVE: al dd non dovrebbe essere oassato il nome della function, dovrebbe calcolarselo lui. Lascio cosi'
			 dispVariare = importiCapitoloDad.findDisponibilitaVariareComponente(uidCapitolo, wr.getTipoComponenteImportiCapitolo().getUid(), functionNameComposed);
		}catch(Exception e){
			 log.warn( methodName, "Errore nel caricamento tramite " + 
					 (functionNameComposed != null ? functionNameComposed : " null " ) + 
					 " della componente " + 
					 (wr.getTipoComponenteImportiCapitolo().getUid()) + 
					 " sul capitolo " + 
					 (uidCapitolo != null? uidCapitolo : " null ") + 
					 " . ritorno zero.");
		}
		if(dispVariare == null) {
			//boh?
			 dispVariare = BigDecimal.ZERO;
		}
		// SIAC-3044 se l'importo e' stato calcolato con la function, e il capitolo era gia' associato alla variazionee la variazione e' nello stato considerato 
		if(!isAdeguaImporto(statoAttualeVariazioneBilancio, uidCapitolo)) {
			//l'importo non e' stato ottenuto con una function o non devo adeguare l'importo. esco.
			return dispVariare;
		}
		BigDecimal importoInVariazionePreModifica = getImportoInVariazionePrecedente(wr);
		//la function considera, nel suo calcolo, solo gli importi in variazione negativi
		return importoInVariazionePreModifica.compareTo(BigDecimal.ZERO) < 0?
				//sono nel caso in cui ho calcolato l'importo tramite function derivata devo adeguare l'importo
				adeguaImporto(importoInVariazionePreModifica, dispVariare) 
				: dispVariare;  

	}

	
	
	//SIAC-7972
	private Errore getErrore(Integer uidCapitolo) {
		for (DettaglioVariazioneImportoCapitolo dettVarImp : variazione.getListaDettaglioVariazioneImporto()) {
			if(dettVarImp.getCapitolo().getUid() == uidCapitolo) {
				return getErroreTemplate(dettVarImp);
			}
		}
		return ErroreBil.DISPONIBILITA_INSUFFICIENTE.getErrore("Stanziamento insufficiente per il capitolo .");
	}
	
	//SIAC-7972
	private BigDecimal getImportoCapitoloInDiminuzione(Integer idCapitolo, WrapperImportiCapitoloVariatiInVariazione wr, int delta, StatoOperativoVariazioneBilancio statoAttualeVariazioneBilancio) {
		ImportiVariatiECorrispondentiEnum importoCorrispondente = ImportiVariatiECorrispondentiEnum.byTElemTipoCodeElemDetTipoCodeAndDelta(wr.getTipoCapitolo(), wr.getSiacDBilElemDetTipoEnum(), delta);
		
		BigDecimal importo = importoCorrispondente!= null?
				//l'importo da confrontare e' un importo derivato, quindi mi predispongo con il caricamento della function
				importiCapitoloDad.findImportoDerivato(idCapitolo, importoCorrispondente.getFunctionImportoDerivato())
				// l'importo da confrontare e' l'importo del capitolo (STA o SCA).
				: importiCapitoloDad.caricaSingoloImportoCapitolo(idCapitolo,wr.getAnnoInteger(),wr.getElemDetTipoCode());
		if(importo == null) {
			//boh?
			importo = BigDecimal.ZERO;
		}
		// SIAC-3044 se l'importo e' stato calcolato con la function, e il capitolo era gia' associato alla variazionee la variazione e' nello stato considerato 
		if(importoCorrispondente == null || !isAdeguaImporto(statoAttualeVariazioneBilancio, idCapitolo)) {
			//l'importo non e' stato ottenuto con una function o non devo adeguare l'importo. esco.
			return importo;
		}
		BigDecimal importoInVariazionePreModifica = getImportoInVariazionePrecedente(wr);
		//la function considera, nel suo calcolo, solo gli importi in variazione negativi
		return importoInVariazionePreModifica.compareTo(BigDecimal.ZERO) < 0?
				//sono nel caso in cui ho calcolato l'importo tramite function derivata devo adeguare l'importo
				adeguaImporto(importoInVariazionePreModifica, importo) 
				: importo;  
	}

	//SIAC-7972
	protected BigDecimal getImportoInVariazionePrecedente(WrapperImportiCapitoloVariatiInVariazione wr) {
		return wr.getImportoVariato();
	}
	
	//SIAC-3044 e SIAC-7972
	protected boolean isAdeguaImporto(StatoOperativoVariazioneBilancio statoAttuale, Integer idCapitolo) {
		return variazione.getUid() != 0 && statoAttuale != null &&
			(StatoOperativoVariazioneBilancio.BOZZA.equals(statoAttuale)
							|| StatoOperativoVariazioneBilancio.GIUNTA.equals(statoAttuale)
							|| StatoOperativoVariazioneBilancio.CONSIGLIO.equals(statoAttuale)
							// CR-3260
							|| StatoOperativoVariazioneBilancio.PRE_DEFINITIVA.equals(statoAttuale)
						);
	}

	//SIAC-7972
	private BigDecimal adeguaImporto(BigDecimal stanziamento, BigDecimal disponibilita) {
		//SIAC-3044
		return disponibilita.setScale(2,RoundingMode.HALF_DOWN)
				.add(stanziamento.setScale(2,RoundingMode.HALF_DOWN).abs());
	}
	
	//SIAC-7972
	protected void checkCapitoliFondinoInVariazione(List<Integer> uidsCapitoliFondino) {
		// TODO da implementare in aggiornamento
		
	}
	
	//SIAC-7972
	public enum ImportiVariatiECorrispondentiEnum {
		ImportiCorrispondentiStanziamentoAnno0Spesa(Arrays.asList(TipoCapitolo.CAPITOLO_USCITA_GESTIONE,TipoCapitolo.CAPITOLO_USCITA_PREVISIONE),SiacDBilElemDetTipoEnum.Stanziamento,0, ImportoDerivatoFunctionEnum.disponibilitaVariareAnno1),
		ImportiCorrispondentiStanziamentoAnno1Spesa(Arrays.asList(TipoCapitolo.CAPITOLO_USCITA_GESTIONE,TipoCapitolo.CAPITOLO_USCITA_PREVISIONE),SiacDBilElemDetTipoEnum.Stanziamento,1, ImportoDerivatoFunctionEnum.disponibilitaVariareAnno2),
		ImportiCorrispondentiStanziamentoAnno2Spesa(Arrays.asList(TipoCapitolo.CAPITOLO_USCITA_GESTIONE,TipoCapitolo.CAPITOLO_USCITA_PREVISIONE),SiacDBilElemDetTipoEnum.Stanziamento,2, ImportoDerivatoFunctionEnum.disponibilitaVariareAnno3),
		ImportiCorrispondentiCassaSpesa(Arrays.asList(TipoCapitolo.CAPITOLO_USCITA_GESTIONE,TipoCapitolo.CAPITOLO_USCITA_PREVISIONE),SiacDBilElemDetTipoEnum.StanziamentoCassa,0, ImportoDerivatoFunctionEnum.disponibilitaVariareCassa),
		;
		
		private List<TipoCapitolo> tipoCapitoloImporto;
		private SiacDBilElemDetTipoEnum siacDBilElemDetTipoEnum; 
		private int delta;
		private ImportoDerivatoFunctionEnum functionImportoDerivato;
		
		private ImportiVariatiECorrispondentiEnum(List<TipoCapitolo> tipoCapitoloImporto, SiacDBilElemDetTipoEnum siacDBilElemDetTipoEnum, int delta, ImportoDerivatoFunctionEnum functionImportoDerivato){ 
			this.tipoCapitoloImporto = tipoCapitoloImporto;
			this.siacDBilElemDetTipoEnum = siacDBilElemDetTipoEnum;
			this.delta = delta;
			this.functionImportoDerivato = functionImportoDerivato;
		}
		
		
		
		public List<TipoCapitolo> getTipoCapitoloImporto() {
			return tipoCapitoloImporto;
		}
		



		public int getDelta() {
			return delta;
		}



		public ImportoDerivatoFunctionEnum getFunctionImportoDerivato() {
			return functionImportoDerivato;
		}
			
		public SiacDBilElemDetTipoEnum getSiacDBilElemDetTipoEnum() {
			return siacDBilElemDetTipoEnum;
		}



		public static ImportiVariatiECorrispondentiEnum byTElemTipoCodeElemDetTipoCodeAndDelta(TipoCapitolo tipoCapitolo,  SiacDBilElemDetTipoEnum siacDBilElemDetTipoEnum, int delta) {
			for (ImportiVariatiECorrispondentiEnum value : values()) {
				if(value.getTipoCapitoloImporto().contains(tipoCapitolo) && value.getDelta() == delta && siacDBilElemDetTipoEnum != null && value.getSiacDBilElemDetTipoEnum().getCodice().equals(siacDBilElemDetTipoEnum.getCodice())){
					return value;
				}
			}
			return null;
		}
			
	}

}
