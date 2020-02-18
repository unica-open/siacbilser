/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.mutuo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceMutuo;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceMutuoResponse;
import it.csi.siac.siacfinser.integration.dad.MutuoDad;
import it.csi.siac.siacfinser.integration.dad.SoggettoFinDad;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.mutuo.Mutuo;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceMutuoService extends AbstractBaseService<InserisceMutuo, InserisceMutuoResponse> {
	
	@Autowired
	ProvvedimentoService provvedimentoService;

	@Autowired
	MutuoDad mutuoDad;

	@Autowired
	SoggettoFinDad soggettoDad;

	@Override
	protected void init() {
		final String methodName = "InserisceMutuoService : init()";
		log.debug(methodName, "- Begin");
	}
	
	/*
	 * VARIAZIONE LINEE GUIDA 3.2.2.2
	 */
	
	@Override
	@Transactional
	public InserisceMutuoResponse executeService(InserisceMutuo serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
	@Override
	public void execute() {
		final String methodName = "InserisceMutuoService : execute()";
			
		log.debug(methodName, "- Begin");
		
		//dati di input presi da request:
		Richiedente richiedente = req.getRichiedente();
		Ente ente = req.getEnte();
		Mutuo mutuo = req.getMutuo();
		AttoAmministrativo attoAmministrativoInput = mutuo.getAttoAmministrativoMutuo();
		Soggetto soggettoInput = mutuo.getSoggettoMutuo();

		if(mutuo.getImportoInizialeMutuo().compareTo(BigDecimal.ZERO) <= 0){
			// Importo mutuo: l'importo del mutuo deve essere maggiore di zero
			res.setErrori(Arrays.asList(ErroreFin.OPERAZIONE_INCOMPATIBILE.getErrore()));
			res.setMutuo(null);
			res.setEsito(Esito.FALLIMENTO);
			return;
		} 
		// verifica sul soggetto ed estrapolazione di tutti i dati
		Soggetto soggettoCheck = soggettoDad.ricercaSoggetto(Constanti.AMBITO_FIN, ente.getUid(), soggettoInput.getCodiceSoggetto(),true, true);
		if(null==soggettoCheck){
			res.setErrori(Arrays.asList(ErroreFin.SOGGETTO_NON_VALIDO.getErrore("")));
			res.setMutuo(null);
			res.setEsito(Esito.FALLIMENTO);
			return;
		} else if (soggettoCheck.getStatoOperativo().name().equalsIgnoreCase(StatoOperativoAnagrafica.ANNULLATO.name()) || 
				   soggettoCheck.getStatoOperativo().name().equalsIgnoreCase(StatoOperativoAnagrafica.SOSPESO.name()) || 
				   soggettoCheck.getStatoOperativo().name().equalsIgnoreCase(StatoOperativoAnagrafica.PROVVISORIO.name())){
				// Soggetto: verifica che il soggetto (Istituto Mutuante) sia in stato diverso da ANNULLATO, SOSPESO e PROVVISORIO.
				// In caso il risultato sia falso il servizio emette l'errore: <FIN_ERR_0162 Soggetto Bloccato per mutuo>
				res.setErrori(Arrays.asList(ErroreFin.SOGGETTO_BLOCCATO_PER_MUTUO.getErrore("")));
				res.setMutuo(null);
				res.setEsito(Esito.FALLIMENTO);
				return;
		}

		// Atto Amministrativo: verifica che lo stato del Provvedimento non sia annullato
		RicercaProvvedimentoResponse ricercaProvvedimentoResponse = ricercaProvvedimento(richiedente, attoAmministrativoInput);
		AttoAmministrativo attoAmministrativoEstratto = new AttoAmministrativo();
		if(ricercaProvvedimentoResponse.isFallimento()){
			// ATT_ERR_0001	Provvedimento Inesistente
			// ATT_ERR_0002	Provvedimento Annullato
			List<Errore> listaErroriAttoAmministrativo = ricercaProvvedimentoResponse.getErrori();			
			res.setErrori(listaErroriAttoAmministrativo);
			res.setMutuo(null);
			res.setEsito(Esito.FALLIMENTO);
			return;
		} else {
			if(ricercaProvvedimentoResponse.getListaAttiAmministrativi()!=null && ricercaProvvedimentoResponse.getListaAttiAmministrativi().size()>0){
				attoAmministrativoEstratto = ricercaProvvedimentoResponse.getListaAttiAmministrativi().get(0);
				attoAmministrativoInput.setUid(attoAmministrativoEstratto.getUid());
			}
		}
		
		/*
		 *  Inserimento del mutuo:
		 *  - passaggio di tutti i suoi dati propri
		 *  - creazione del progressivo
		 *  - reperimento e settaggio dello stato del mutuo nella tavola di R
		 *  
		 */

		Mutuo mutuoInserito = mutuoDad.inserisciMutuo(ente, richiedente, mutuo);

		if(null!=mutuoInserito){
			mutuoInserito.setAttoAmministrativoMutuo(attoAmministrativoEstratto);

			res.setMutuo(mutuoInserito);
			res.setEsito(Esito.SUCCESSO);
		} else {
			res.setMutuo(null);
			res.setEsito(Esito.FALLIMENTO);
		}
		
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		final String methodName = "InserisceMutuoService : checkServiceParam()";
		log.debug(methodName, "- Begin");
		
		//dati di input presi da request:
		Ente ente = req.getEnte();
		Mutuo mutuo = req.getMutuo();

		String elencoParamentriNonInizializzati = "";
		// corretta valorizzazione paramentri
		if(null==ente){
			if(elencoParamentriNonInizializzati.length() > 0)
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", ENTE";
			else
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "ENTE";
		}

		if(null==mutuo){
			if(elencoParamentriNonInizializzati.length() > 0)
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", MUTUO_DA_INSERIRE";
			else
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "MUTUO_DA_INSERIRE";
		}
		
		if(null!=mutuo && null==mutuo.getTipoMutuo()){
			if(elencoParamentriNonInizializzati.length() > 0)
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", TIPO_MUTUO";
			else
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "TIPO_MUTUO";
		}
		
		if(null!=mutuo && null==mutuo.getAttoAmministrativoMutuo()){
			if(elencoParamentriNonInizializzati.length() > 0)
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", ATTO_AMMINISTRATIVO";
			else
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "ATTO_AMMINISTRATIVO";
		}
		
		if(null!=mutuo && null==mutuo.getSoggettoMutuo()){
			if(elencoParamentriNonInizializzati.length() > 0)
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", SOGGETTO_MUTUANTE";
			else
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "SOGGETTO_MUTUANTE";
		}

		if(null!=mutuo.getSoggettoMutuo() && mutuo.getSoggettoMutuo().getCodiceSoggetto().isEmpty()){
			if(elencoParamentriNonInizializzati.length() > 0)
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", CODICE_SOGGETTO_MUTUANTE";
			else
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "CODICE_SOGGETTO_MUTUANTE";
		}

		if(null!=mutuo && null==mutuo.getStatoOperativoMutuo()){
			if(elencoParamentriNonInizializzati.length() > 0)
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", STATO_OPERATIVO_MUTUO";
			else
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "STATO_OPERATIVO_MUTUO";
		}
		// se c'e' almeno un errore nelle inizializzazioni espongo il msg
		if(elencoParamentriNonInizializzati.length() > 0)
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore(elencoParamentriNonInizializzati));
	}	
}