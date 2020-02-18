/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.BasePopolaFondiDubbiaEsigibilitaDaAnnoPrecedente;
import it.csi.siac.siacbilser.frontend.webservice.msg.BasePopolaFondiDubbiaEsigibilitaDaAnnoPrecedenteResponse;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilitaBase;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.model.AttributiBilancio;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Popolamento dei fondi a dubbia e difficile esazione dall'anno precedente.
 *
 * @author Marchino Alessandro
 * @param <C> the generic type
 * @param <CO> the generic type
 * @param <A> the generic type
 * @param <AO> the generic type
 * @param <REQ> the generic type
 * @param <RES> the generic type
 */
public abstract class BasePopolaFondiDubbiaEsigibilitaDaAnnoPrecedenteService<
		//capitolo		
		C extends Capitolo<?, ?>,
		//capitolo equivalente
		CO extends Capitolo<?, ?>,
		//accantonamenti relativi al capitolo
		A extends AccantonamentoFondiDubbiaEsigibilitaBase<C>,
		//accantonamenti relativi al capitolo equivalente
		AO extends AccantonamentoFondiDubbiaEsigibilitaBase<CO>,
		//request per il servizio di popolamento
		REQ extends BasePopolaFondiDubbiaEsigibilitaDaAnnoPrecedente,
		//response per il servizio di popolamento
		RES extends BasePopolaFondiDubbiaEsigibilitaDaAnnoPrecedenteResponse<A>> extends CheckedAccountBaseService<REQ, RES> {

	//DADs
	@Autowired
	private BilancioDad bilancioDad;
	
	//FIELDS
	protected Bilancio bilancioAttuale;
	protected Bilancio bilancioPrecedente;
	private AttributiBilancio attributiBilancioAttuale;
	private AttributiBilancio attributiBilancioPrecedente;
	private int deltaAnnoApprovato;
	private List<Method[]> methodsToUse;

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		//il bilancio e' obbligatorio
		checkEntita(req.getBilancio(), "bilancio", false);
	}
	
	@Override
	protected void execute() {
		//imposto subito nella response il bilancio
		res.setBilancio(req.getBilancio());
		// I due bilanci
		bilancioAttuale = req.getBilancio();
		bilancioPrecedente = ottieniBilancioPrecedente();
		
		// Gli attributi
		attributiBilancioAttuale = ottieniAttributiBilancio(bilancioAttuale);
		attributiBilancioPrecedente = ottieniAttributiBilancio(bilancioPrecedente);
		
		//calcolo il delta tra l'anno corrente e l'ultimo anno approvato
		deltaAnnoApprovato = attributiBilancioAttuale.getUltimoAnnoApprovato().intValue() - attributiBilancioPrecedente.getUltimoAnnoApprovato().intValue();
		precomputeGetters();
		
		// Leggo tutti gli accantonamenti del bilancio precedente
		// TODO: paginare?
		List<AO> fondiAnnoPrecedente = findAccantonamentiBilancioPrecedente();
		for(AO afde : fondiAnnoPrecedente) {
			//popolo effettivamente i dati
			popolaNuovoDaVecchio(afde);
		}
	}

	/**
	 * Ottiene il bilancio precedente
	 * @return il bilancio relativo all'anno precedente
	 */
	private Bilancio ottieniBilancioPrecedente() {
		final String methodName = "ottiniBilancioPrecedente";
		//carico il bilancio
		Bilancio bilancio = bilancioDad.getBilancioAnnoPrecedente(req.getBilancio());
		if(bilancio == null) {
			//il bilancio indicato non esiste, lancio un'eccezione
			throw new BusinessException("Nessun bilancio con anno precedente reperito per il bilancio con uid " + req.getBilancio().getUid());
		}
		log.debug(methodName, "Trovato bilancio anno precedente con uid " + bilancio.getUid());
		//restituisco il bilancio appena caricato
		return bilancio;
	}
	
	/**
	 * Ottiene gli attributi relativi al bilancio
	 * @param bilancio il bilancio da cui ottenere gli attributi
	 * @return gli attributi del bilancio
	 */
	private AttributiBilancio ottieniAttributiBilancio(Bilancio bilancio) {
		//carico gli attributi di bilancio
		AttributiBilancio attributiBilancio = bilancioDad.getAttributiDettaglioByBilancio(bilancio);
		if(attributiBilancio == null) {
			//non vi sono attributi di bilancio
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("popolamento fondi a dubbia e difficile esazione da anno precedente",
					"il bilancio con uid " + bilancio.getUid() + " non ha attributi relativi ai fondi registrati"));
		}
		//restituisco gli attributi appena caricati
		return attributiBilancio;
	}
	
	/**
	 * Precomputa i getter da utilizzare
	 */
	private void precomputeGetters() {
		final String methodName = "precomputeGetters";
		//inizializzo la lista dei metodi
		methodsToUse = new ArrayList<Method[]>();
		int j = 0;
		Class<AccantonamentoFondiDubbiaEsigibilitaRendiconto> clazz = AccantonamentoFondiDubbiaEsigibilitaRendiconto.class;
		
		//popolo la lista di metodi
		for(int i = 0; i <= 4; i++) {
			if(i >= deltaAnnoApprovato) {
				Method attuale = getSetter(clazz, i);
				Method precedente = getGetter(clazz, j++);
				log.debug(methodName, "Calcolata coppia " + attuale.getName() + " <== " + precedente.getName());
				methodsToUse.add(new Method[] {attuale, precedente});
			}
		}
	}
	
	private Method getSetter(Class<AccantonamentoFondiDubbiaEsigibilitaRendiconto> clazz, int anno) {
		//seleziono il nome setter tra setPercentualeAccantonamentoFondi, setPercentualeAccantonamentoFondi1, setPercentualeAccantonamentoFondi2...
		String name = anno == 0 ? "setPercentualeAccantonamentoFondi" : ("setPercentualeAccantonamentoFondi" + anno);
		try {
			//prendo il metodo dalla classe
			return clazz.getMethod(name, BigDecimal.class);
		} catch (SecurityException e) {
			//trasformo un'eccezione di sicurezza in una IllegalStateException
			throw new IllegalStateException("Errore nel recupero del metodo 'setPercentualeAccantonamentoFondi' per l'anno " + anno + ": errore di sicurezza", e);
		} catch (NoSuchMethodException e) {
			//quando non e' presente il metodo, lancio una IllegalStateException
			throw new IllegalStateException("Errore nel recupero del metodo 'setPercentualeAccantonamentoFondi' per l'anno " + anno + ": metodo non esistente", e);
		}
	}
	
	private Method getGetter(Class<AccantonamentoFondiDubbiaEsigibilitaRendiconto> clazz, int anno) {
		//seleziono il  nome del getter tra getPercentualeAccantonamentoFondi, getPercentualeAccantonamentoFondi1, getPercentualeAccantonamentoFondi2...
		String name = anno == 0 ? "getPercentualeAccantonamentoFondi" : ("getPercentualeAccantonamentoFondi" + anno);
		try {
			//prendo il metodo
			return clazz.getMethod(name);
		} catch (SecurityException e) {
			//trasformo un'eccezione di sicurezza in una IllegalStateException
			throw new IllegalStateException("Errore nel recupero del metodo 'getPercentualeAccantonamentoFondi' per l'anno " + anno + ": errore di sicurezza", e);
		} catch (NoSuchMethodException e) {
			//quando non e' presente il metodo, lancio una IllegalStateException
			throw new IllegalStateException("Errore nel recupero del metodo 'getPercentualeAccantonamentoFondi' per l'anno " + anno + ": metodo non esistente", e);
		}
	}
	
	/**
	 * Popolamento del nuovo fondo a dubbia esigibilit&agrave; a partire da quello dell'anno precedente
	 * @param afdePrecedente l'accantonamento per l'anno precedente 
	 */
	private void popolaNuovoDaVecchio(AO afdePrecedente) {
		final String methodName = "popolaNuovoDaVecchio";
		//prendo il capitolo equivalente
		CO capitoloPrecedente = afdePrecedente.ottieniCapitolo();
		List<Integer> idCapitoliCorrispondenti = ricercaIdCapitoliPerChiave(capitoloPrecedente);
		if(idCapitoliCorrispondenti == null || idCapitoliCorrispondenti.isEmpty()) {
			//non ho trovato nessun capitolo:esco
			log.debug(methodName, "Nessun capitolo per anno " + bilancioAttuale.getAnno() + " con chiave "
					+ capitoloPrecedente.getNumeroCapitolo() + "/" + capitoloPrecedente.getNumeroArticolo() + "/" + capitoloPrecedente.getNumeroUEB());
			return;
		}
		
		for(Integer uid : idCapitoliCorrispondenti) {
			//effettuo effettivamente il popolamento
			popolaNuovoAccantonamento(afdePrecedente, uid);
		}
	}
	
	/**
	 * Popolamento del nuovo accantonamento
	 * @param afdePrecedente l'accantonamento per l'anno precedente
	 * @param uid l'uid del capitolo
	 */
	private void popolaNuovoAccantonamento(AO afdePrecedente, Integer uid) {
		final String methodName = "popolaNuovoAccantonamento";
		C cap = findCapitolo(uid);
		
		//cerco eventuali accantonamenti legati al capitolo
		A accantonamentoAttuale = findAccantonamentoAttuale(cap);
		if(accantonamentoAttuale != null) {
			//non posso inserire piu' di un accantonamento per capitolo
			log.debug(methodName, "Accantonamento gia' presente per il capitolo " + uid + " - salto l'inserimento");
			return;
		}
		
		A afde = initAccantonamento();
		afde.impostaCapitolo(cap);
		for(Method[] methods : methodsToUse) {
			//prendo i metodi computati prima
			Method setter = methods[0];
			Method getter = methods[1];
			
			try {
				//invoco il setter
				setter.invoke(afde, getter.invoke(afdePrecedente));
			} catch (IllegalArgumentException e) {
				//gli argomenti passati al setter non sono validi, lancio una IllegalStateException
				throw new IllegalStateException("Errore nell'invocazione del metodo " + setter.getName() + " utilizzando i dati provenienti dal metodo " + getter.getName() + ": argomenti invalidi", e);
			} catch (IllegalAccessException e) {
				//accesso non consentito, lancio una IllegalStateException
				throw new IllegalStateException("Errore nell'invocazione del metodo " + setter.getName() + " utilizzando i dati provenienti dal metodo " + getter.getName() + ": accesso non consentito", e);
			} catch (InvocationTargetException e) {
				//si e' verificato un errore nell'invocazione del metodo, lancio una IllegalStateException
				throw new IllegalStateException("Errore nell'invocazione del metodo " + setter.getName() + " utilizzando i dati provenienti dal metodo " + getter.getName() + ":  errore nel metodo invocato", e);
			}
		}
		
		//salvo i dati
		saveAccantonamento(afde);
		
		//loggo e metto in response i dati salvati
		log.debug(methodName, "Creato accantonamento con uid " + afde.getUid() + " relativo al capitolo " + uid);
		res.addAccantonamentoFondiDubbiaEsigibilita(afde);
	}
	
	// METODI ASTRATTI DA IMPLEMENTARE NELLE SOTTOCLASSI:

	/**
	 * Ottiene gli accantonamenti del precedente bilancio
	 * @return gli accantonamenti del rpecedente bilancio
	 */
	protected abstract List<AO> findAccantonamentiBilancioPrecedente();
	/**
	 * Ricarca degli id capitoli per chiave
	 * @param capitoloPrecedente il capitolo precedente
	 * @return gli id dei capitoli
	 */
	protected abstract List<Integer> ricercaIdCapitoliPerChiave(CO capitoloPrecedente);
	/**
	 * Ottiene il capitolo per uid
	 * @param uid l'uid del capitolo
	 * @return il capitolo
	 */
	protected abstract C findCapitolo(Integer uid);
	/**
	 * Ottiene l'ventuale accantonamento attuale
	 * @param cap
	 * @return
	 */
	protected abstract A findAccantonamentoAttuale(C cap);
	/**
	 * Inizializzazione dell'accantonamento
	 * @return l'accantonamento
	 */
	protected abstract A initAccantonamento();
	/**
	 * Salvataggio dell'accantonamento
	 * @param afde l'accantonamento
	 */
	protected abstract void saveAccantonamento(A afde);
}
