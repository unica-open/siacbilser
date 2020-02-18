/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.causale;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceCausaleResponse;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.ContoTipoOperazione;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.StatoOperativoCausaleEP;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.errore.ErroreGEN;

/**
 * Servizio di inserimento di una CausaleEP
 * 
 * @author Domenico
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceCausaleService extends CrudCausaleBaseService<InserisceCausale, InserisceCausaleResponse> {

	
	/*
	 * Per il salvataggio effettuare i controlli descritti al par.  2.10.1  
	 * 
	 * 2.10.1	Algoritmo di salvataggio della causale
     * I controlli indicati si effettuano per l'operazione di inserimento e per quella di aggiornamento.
     * 
     * Il sistema effettuerà i seguenti controlli:
     * 
     * #	Il codice di una Causale deve  essere univoco, se si prova ad inserire un codice Causale 
     *      già esistente (anche nel caso appartenga ad una causale in stato ANNULLATO) il sistema deve visualizzare 
     *      il messaggio di errore <COR_ERR_0008, Entità già presente, ‘Causale', ‘Codice della causale che si sta inserendo'>.
     * 
     * Per le le causali DI RACCORDO effettuare  i seguenti controlli.
     * #	Ci siano almeno due conti, uno con TipoOperazione-segnoConto=DARE e uno con TipoOperazione-segnoConto=AVERE; se non è così il sistema presenta il seguente messaggio < GEN_ERR_0007, Assenza Conti obbligatori causali di raccordo.>.
     * #	Ci sia almeno un conto collegato allo stesso V livello del Piano dei Conti Finanziario della causale, se non è così il sistema presenta il seguente messaggio <COR_ERR_043 Entità non completa (<nome entità>: “La causale“,   <motivo> :  “deve essere presente un conto collegato ad conto finanziario di  V livello”.>.
     * #	Se la causale  è associata ad un Soggetto deve essere presente un Conto collegato allo stesso Soggetto; se non è così il sistema presenta il seguente messaggio <COR_ERR_043 Entità non completa (<nome entità>: “La causale“,   <motivo> :  “deve essere presente un conto collegato al soggetto della causale”.>.
     * 
     * Si aggiungono poi condizioni specifiche di una tipologia di eventi.
     * Per TipoEvento = EP – Scrittura epilogativa
     * #	Deve essere presente almeno un conto appartenente alla ClasseConto = EP – Conti epilogativi. In caso non sia rispettato il controllo inviare il messaggio <COR_ERR_043 Entità non completa (<nome entità>: “La causale“,  <motivo> :  “deve essere presente un conto epilogativo” )>. 
	 *
	 * 
	 * e in caso di esito positivo impostare gli attributi della causale non gestiti dalla maschera. 
     * 
     * -	data inizio validità del conto al 01/01/ dell'anno di bilancio su cui si sta lavorando
     * -	StatoOperativoCausaleEP = P- Provvisorio  con data inizio validità della relazione 01/01/ dell'anno di bilancio su cui si sta lavorando
     * -	impostare le date di logging con la data dell'operazione di inserimento (data corrente)
     * 
     * E procedere salvando 
     * -	i dati del conto (entità Conto ed entità correlate).
     * -	le relazioni con codifiche e classificatori generici se compilati 01/01/ dell'anno di bilancio su cui si sta lavorando
     * 
     * Il sistema notifica l'esito dell'operazione con eventuali segnalazioni di errore.
     * 
	 */
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getCausaleEP(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("causaleEP"));
		this.causaleEP = req.getCausaleEP();
		this.causaleEP.setEnte(ente);
		
		this.bilancio = req.getBilancio();
		checkEntita(bilancio, "bilancio", false);
		
		checkNotBlank(causaleEP.getCodice(), "codice causaleEP", false);
		checkNotBlank(causaleEP.getDescrizione(), "descrizione causaleEP", false);
		
		checkNotNull(causaleEP.getTipoCausale(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo causale"), false);
		
		checkNotNull(causaleEP.getEventi(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("eventi causale"));
		checkCondition(!causaleEP.getEventi().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("eventi causale"));
		
		checkNotNull(causaleEP.getAmbito(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ambito causale"));
		
		for(Evento evento: causaleEP.getEventi()){
			checkEntita(evento, "evento conto", false);
		}
		
//		checkNotNull(causaleEP.getContiTipoOperazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("conti tipo operazione"));
//		checkCondition(!causaleEP.getContiTipoOperazione().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("conti tipo operazione"));
		
		if(causaleEP.getContiTipoOperazione()!=null){
			for(ContoTipoOperazione cto : causaleEP.getContiTipoOperazione()){
				if(cto.getClasseDiConciliazione() == null || !Ambito.AMBITO_GSA.equals(causaleEP.getAmbito())){
					checkEntita(cto.getConto(), "conto",false);
				}
				checkNotNull(cto.getOperazioneSegnoConto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("operazione segno conto conto"),false);
				checkNotNull(cto.getOperazioneTipoImporto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("operazione tipo importo conto"),false);
				if(TipoCausale.Integrata.equals(causaleEP.getTipoCausale())){
					checkNotNull(cto.getOperazioneUtilizzoConto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("operazione utilizzo conto conto"),false);
					checkNotNull(cto.getOperazioneUtilizzoImporto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("operazione utilizzo importo conto"),false);
				}
			}
		}
		
		if(causaleEP.getCausaleDiDefault() == null){
			causaleEP.setCausaleDiDefault(Boolean.FALSE);
		}
		
	}
	
	@Override
	@Transactional
	public InserisceCausaleResponse executeService(InserisceCausale serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	protected void execute() {
		
		caricaBilancio();
		checkFaseBilancio();
		
		checkContiConciliazione();
		checkCodiceCausale();
		checkCausaleEPIntegrata();
		checkCausaleEPConEventoEP();
		checkUguaglianzaContiCausale();
		
		checkContiCoerentiConElementoPianoDeiConti();
		
		causaleEP.setStatoOperativoCausaleEP(StatoOperativoCausaleEP.PROVVISORIO);
		
		Date inizioAnno = Utility.primoGiornoDellAnno(bilancio.getAnno());
		causaleEP.setDataInizioValidita(inizioAnno);
		causaleEPDad.inserisciCausaleEP(causaleEP);
		
		res.setCausaleEP(causaleEP);
		
	}

	/**
	 * Controlla se il codice causale e' già sato utilizzato (SOLO inserimento)
	 */
	private void checkCodiceCausale() {
		CausaleEP causaleEPTrovata = causaleEPDad.ricercaCausaleEPByCodice(causaleEP);
		if(causaleEPTrovata!=null) {
			throw new BusinessException(ErroreCore.ENTITA_PRESENTE.getErrore("CausaleEP", "il codice \""+ causaleEPTrovata.getCodice()+ "\" "));
		}
		
	}

	/**
	 * Controlla se esiste gia' una causale con gli stessi conti ep e con lo stesso piano dei conti ed eventi.
	 */
	protected void checkUguaglianzaContiCausale() {
		String methodName = "checkUguaglianzaContiCausale";
		if(!TipoCausale.Integrata.equals(causaleEP.getTipoCausale())){
			log.debug(methodName, "tipoCausale diverso da Integrata. Salto il check.");
			return;
		}
		
		if(Ambito.AMBITO_GSA.equals(causaleEP.getAmbito())){
			log.debug(methodName, "tipoCausale con Ambito GSA. Salto il check.");
			return;
		}
		
		List<CausaleEP> causali = causaleEPDad.findCausaleEpIdByConti(causaleEP);
		if(!causali.isEmpty()){
			throw new BusinessException(ErroreGEN.OPERAZIONE_NON_CONSENTITA_0016.getErrore(), Esito.FALLIMENTO);
		}
	}

}
