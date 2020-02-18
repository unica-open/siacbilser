/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.ordinativo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.business.service.AbstractBaseServiceRicercaOrdinativo;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoIncassoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoIncassoPerChiaveResponse;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.integration.dad.OrdinativoIncassoDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.integration.util.dto.SiacTOrdinativoCollegatoCustom;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ric.RicercaOrdinativoIncassoK;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaOrdinativoIncassoPerChiaveService extends AbstractBaseServiceRicercaOrdinativo<RicercaOrdinativoIncassoPerChiave, RicercaOrdinativoIncassoPerChiaveResponse> {
	
	@Autowired
	OrdinativoIncassoDad ordinativoIncassoDad;
	
	@Autowired
	CommonDad commonDad;
	
	@Autowired
	ProvvedimentoService provvedimentoService;
	
	@Autowired
	CapitoloEntrataGestioneService capitoloEntrataGestioneService;
		
	@Override
	protected void init() {
		final String methodName="init";
		log.debug(methodName, " - Begin");
	}	
	
	
	
	@Override
	@Transactional(readOnly=true)
	public RicercaOrdinativoIncassoPerChiaveResponse executeService(RicercaOrdinativoIncassoPerChiave serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	public void execute() {
		String methodName = "execute";
		log.debug(methodName, " - Begin");
		
		//1. Vengono letti i valori ricevuti in input dalla request
		Ente ente = req.getEnte();
		Richiedente richiedente = req.getRichiedente();
		RicercaOrdinativoIncassoK pk = req.getpRicercaOrdinativoIncassoK();
		
		// Aggiunte queste righe perche' altrimenti andava in errore in caso di richiamo senza aver settato dataOra
		if(null==req.getDataOra()){
			req.setDataOra(new Date());
		}	
		
		//2. Si inizializza l'oggetto DatiOperazioneDto, dto di comodo generico che verra' passato tra i metodi
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.INSERIMENTO, null);
		
		//3. Si richiama il metodo "ottieniOrdinativoIncasso" il quale si occupa di orchestrare la ricerca per chiave e di 
		// "vestire" l'oggetto con i dati relativi al capitolo e provvedimento invocando gli opportuni servizi esterni
		OrdinativoIncasso ordinativoIncasso = ottieniOrdinativoIncasso(pk, datiOperazione, richiedente);
		
		//4. Vengono vestiti ulteriormente i dati ottenuti con l'elenco degli ordinativi collegati:
		List<Ordinativo> listaOrdinativiCollegati = new ArrayList<Ordinativo>();
		List<SiacTOrdinativoCollegatoCustom> listaSiacTOrdinativoCollegatoCustom = ordinativoIncassoDad.checkOrdinativiCollegati(
				ordinativoIncasso.getAnno(), ordinativoIncasso.getNumero(), 
				ordinativoIncasso.getStatoOperativoOrdinativo(), Constanti.D_ORDINATIVO_TIPO_INCASSO,
				req.getPaginazioneOrdinativiCollegati(),
				datiOperazione);
		listaOrdinativiCollegati = caricaOrdinativiInCollegamento(listaSiacTOrdinativoCollegatoCustom, datiOperazione, richiedente);
		ordinativoIncasso.setElencoOrdinativiCollegati(listaOrdinativiCollegati);
		
		ordinativoIncasso.setTotaleOrdinativiCollegati(ordinativoPagamentoDad.readTotaleOrdinativiCollegati(ordinativoIncasso.getUid(), datiOperazione));
		
		//5. Viene composta la response
		res.setEsito(Esito.SUCCESSO);
		res.setOrdinativoIncasso(ordinativoIncasso);
			
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		final String methodName="checkServiceParam";
		log.debug(methodName, " - Begin");

		if (req.getpRicercaOrdinativoIncassoK() == null) {
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Chiave primaria ordinativo incasso"));			
		}  else {
			OrdinativoIncasso ordInc = req.getpRicercaOrdinativoIncassoK().getOrdinativoIncasso();
			if (ordInc == null) {
				checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Ordinativo incasso"));
			}
			if (ordInc.getNumero() == null) {
				checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Numero ordinativo incasso"));
			}
			Bilancio bil = req.getpRicercaOrdinativoIncassoK().getBilancio();
			if (bil == null) {
				checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Bilancio"));
			}
		    if (bil.getAnno() == 0) {
		    	checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Anno bilancio"));
		    }
		}
		Ente ente = req.getRichiedente().getAccount().getEnte();
		if (ente == null){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Ente"));
		}
	}

}