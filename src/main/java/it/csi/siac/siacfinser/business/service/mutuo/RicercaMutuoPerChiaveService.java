/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.mutuo;

import java.sql.Timestamp;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaMutuoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaMutuoPerChiaveResponse;
import it.csi.siac.siacfinser.integration.dad.MutuoDad;
import it.csi.siac.siacfinser.model.mutuo.Mutuo;
import it.csi.siac.siacfinser.model.mutuo.VoceMutuo;
import it.csi.siac.siacfinser.model.ric.RicercaMutuoK;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaMutuoPerChiaveService extends AbstractBaseService<RicercaMutuoPerChiave, RicercaMutuoPerChiaveResponse> {
	
	@Autowired
	MutuoDad mutuoDad;
	
		
	@Override
	protected void init() {
		final String methodName="init";
		log.debug(methodName, " - Begin");
	}	
	
	@Override
	@Transactional(readOnly=true)
	public RicercaMutuoPerChiaveResponse executeService(RicercaMutuoPerChiave serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	public void execute() {
		String methodName = "execute";
		log.debug(methodName, " - Begin");
		
		Ente ente = req.getEnte();
		Richiedente richiedente = req.getRichiedente();
		Integer idEnte = ente.getUid();
		// caricato l'oggetto per la ricerca con code
		RicercaMutuoK pk = req.getpRicercaMutuoK();
		
		/*
		 * Avvio la ricerca del mutuo per chiave, al suo interno vengono reperite le seguenti informazioni:
		 * 
		 * - mutuo : tipo, descrizione, login, provvedimento, totale voci e disponibile mutuo
		 * - istituto mutuante: soggetto e classificazione soggetto
		 * - array delle voci di mutuo con i dati propri
		 * 		- per ogni voce: impegno e/o lista di subimpegni
		 *      - per ogni voce: variazioni e disponibilita' a liquidare  
		 */
		
		
		
		long startUNO = System.currentTimeMillis();
		
		Mutuo mutuo = mutuoDad.ricercaMutuo(idEnte, pk.getMutCode(), new Timestamp(req.getDataOra().getTime()));
		
		long stopUNO = System.currentTimeMillis();
		
		
		long startDUE = System.currentTimeMillis();
		// con i dati base dell'atto ne estraggo tramite routine l'atto amministrativo per intero
		mutuo.setAttoAmministrativoMutuo(estraiAttoAmministrativo(richiedente, mutuo.getAttoAmministrativoMutuo()));
		long stopDUE = System.currentTimeMillis();
		
		long startTRE = System.currentTimeMillis();
		HashMap<Integer, CapitoloUscitaGestione> cache = new HashMap<Integer, CapitoloUscitaGestione>();
		// per ogni voce di mutuo setto il capitolo uscita gestione per intero
		if (mutuo.getListaVociMutuo() != null){ 
			for (VoceMutuo voceMutuo : mutuo.getListaVociMutuo()) {
				if (voceMutuo.getImpegno() != null) {
					CapitoloUscitaGestione capitoloUscitaGest = caricaCapitoloUscitaGestioneSinteticaCaching(richiedente, voceMutuo.getImpegno().getChiaveCapitoloUscitaGestione(), cache );
					voceMutuo.getImpegno().setCapitoloUscitaGestione(capitoloUscitaGest);
				}
			}
		}
		long stopTRE = System.currentTimeMillis();
		
		long totUNO = stopUNO-startUNO;
		long totDUE = stopDUE-startDUE;
		long totTRE = stopTRE-startTRE;
		
		//System.out.println("TOT UNO: " + totUNO);
		//System.out.println("TOT DUE: " + totDUE);
		//System.out.println("TOT TRE: " + totTRE);
		
		
		res.setMutuo(mutuo);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		final String methodName="checkServiceParam";
		log.debug(methodName, " - Begin");

		// valorizzazione obbligatoria param di ricerca
		if (req.getpRicercaMutuoK() == null) {
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Chiave primaria mutuo"));			
		}  else {
			String numeroMutuo = req.getpRicercaMutuoK().getMutCode();
			if (numeroMutuo == null) {
				checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Numero mutuo"));
			}
		}
		Ente ente = req.getRichiedente().getAccount().getEnte();
		if (ente == null){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Ente"));
		}
		
	}
	
}