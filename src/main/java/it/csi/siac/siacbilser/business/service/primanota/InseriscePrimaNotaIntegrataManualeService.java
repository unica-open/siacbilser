/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dad.CodificaDad;
import it.csi.siac.siacbilser.integration.dad.ImpegnoBilDad;
import it.csi.siac.siacbilser.integration.dad.SoggettoDad;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNotaIntegrataManuale;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNotaIntegrataManualeResponse;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.StatoOperativoRegistrazioneMovFin;

/**
 * Servizio di inserimento di una PrimaNota
 * 
 * @author Valentina
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InseriscePrimaNotaIntegrataManualeService extends InseriscePrimaNotaBaseService<InseriscePrimaNotaIntegrataManuale, InseriscePrimaNotaIntegrataManualeResponse> {

	private Evento evento;
	private RegistrazioneMovFin registrazioneMovFin;
	
	@Autowired
	private CodificaDad codificaDad;
	@Autowired
	private ImpegnoBilDad impegnoBilDad;
	@Autowired
	private SoggettoDad soggettoDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		super.checkServiceParam();
		checkNotNull(primaNota.getAmbito(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ambito prima nota"), false);
		checkEntita(req.getEntita(), "entita' collegata", false);
		checkEntita(req.getEvento(), "evento", false);
	}
	
	@Override
	protected void init() {
		super.init();
		codificaDad.setEnte(ente);
	}
	
	@Override
	@Transactional
	public InseriscePrimaNotaIntegrataManualeResponse executeService(InseriscePrimaNotaIntegrataManuale serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		checkImportiDareAvere();
		checkEvento();
		popolaSoggetto();
		
		// Inserimento registrazioni
		inserisceRegistrazioneMovFin();
		
		popolaPrimaNota();
		popolaMovimentiEP();
		
		primaNotaDad.inserisciPrimaNota(primaNota);
		
		res.setPrimaNota(primaNota);
	}
	
	private void checkEvento() {
		final String methodName = "checkEvento";
		// Gli eventi devono essere della famiglia EXTR-xxx. Nel caso in cui si sia selezionato l'EXTR, sovrascrive l'evento con quello corretto
		Evento e = req.getEvento();
		evento = codificaDad.ricercaCodifica(Evento.class, e.getUid());
		if(evento == null || evento.getCodice() == null) {
			throw new BusinessException("Impossibile reperire l'evento con uid " + e.getUid());
		}
		if(!evento.getCodice().startsWith("EXTR")) {
			throw new BusinessException("Gli unici eventi coerenti con la gestione della prima nota integrata manuale sono gli eventi di tipo EXTR");
		}
		if(!"EXTR".equals(evento.getCodice())) {
			// Ho gia' la codifica corretta
			log.debug(methodName, "Evento correttamente impostato dal chiamante: codice " + evento.getCodice());
			return;
		}
		// Devo calcolare l'evento corretto
		Class<?> clazz = req.getEntita().getClass();
		log.debug(methodName, "Evento con codice \"EXTR\" non valido: calcolo dell'evento corrispondente per entita' collegata di tipo " + clazz.getSimpleName());
		String newCode = "EXTR-" + clazz.getSimpleName().replaceAll("[a-z]", "");
		evento = codificaDad.ricercaCodifica(Evento.class, newCode);
		if(evento == null || evento.getCodice() == null) {
			throw new BusinessException("Impossibile reperire l'evento con codice " + newCode);
		}
	}
	
	private void inserisceRegistrazioneMovFin() {
		final String methodName = "inserisceRegistrazioneMovFin";
		registrazioneMovFin = new RegistrazioneMovFin();
		
		registrazioneMovFin.setEnte(ente);
		registrazioneMovFin.setBilancio(primaNota.getBilancio());
		registrazioneMovFin.setElementoPianoDeiContiIniziale(ottieniPDCIniziale(req.getEntita()));
		registrazioneMovFin.setMovimento(req.getEntita());
		registrazioneMovFin.setEvento(evento);
		// Gia' in stato registrato in quanto inserisco contestualmente alla prima nota
		registrazioneMovFin.setStatoOperativoRegistrazioneMovFin(StatoOperativoRegistrazioneMovFin.REGISTRATO);
		registrazioneMovFin.setAmbito(req.getPrimaNota().getAmbito());
		// ???
//		registrazioneMovFin.setConto(conto);
//		registrazioneMovFin.setListaMovimentiEP(listaMovimentiEP);
		
		registrazioneMovFinDad.inserisciRegistrazioneMovFin(registrazioneMovFin);
		log.info(methodName, "Inserita registrazione movfin con uid " + registrazioneMovFin.getUid());
		res.setRegistrazioneMovFin(registrazioneMovFin);
	}

	private ElementoPianoDeiConti ottieniPDCIniziale(Entita entita) {
		ElementoPianoDeiConti epdc = null;
		if(req.getEntita() instanceof SubImpegno || req.getEntita() instanceof SubAccertamento) {
			epdc = impegnoBilDad.ottieniPianoDeiContiBySubMovgestId(req.getEntita().getUid());
		} else if(req.getEntita() instanceof Impegno || req.getEntita() instanceof Accertamento) {
			epdc = impegnoBilDad.ottieniPianoDeiContiByMovgestId(req.getEntita().getUid());
		}
		return epdc;
	}

	private void popolaPrimaNota() {
		final String methodName = "popolaPrimaNota";
		Integer numero = primaNotaDad.staccaNumeroPrimaNota(Integer.toString(primaNota.getBilancio().getAnno()), primaNota.getAmbito());
		log.debug(methodName, "Numero della PrimaNota: " + numero);
		primaNota.setNumero(numero);
		primaNota.setStatoOperativoPrimaNota(StatoOperativoPrimaNota.PROVVISORIO);
		
		// Imposto la registrazione
		for(MovimentoEP mep : primaNota.getListaMovimentiEP()) {
			mep.setRegistrazioneMovFin(registrazioneMovFin);
			mep.setAmbito(req.getPrimaNota().getAmbito());
		}
	}
	
	private void popolaSoggetto() {
		final String methodName = "popolaSoggetto";
		Soggetto soggetto = null;
		
		// Caricamento dati a partire dal tipo di entita'
		if(req.getEntita() instanceof SubImpegno || req.getEntita() instanceof SubAccertamento) {
			soggetto = soggettoDad.findSoggettoByIdSubMovimentoGestione(req.getEntita().getUid());
		} else if(req.getEntita() instanceof Impegno || req.getEntita() instanceof Accertamento) {
			soggetto = soggettoDad.findSoggettoByIdMovimentoGestione(req.getEntita().getUid());
		}
		
		if(soggetto != null){
			log.debug(methodName, "Individuato soggetto: " + soggetto.getUid() + " per l'entita' di tipo " + req.getEntita().getClass().getSimpleName() + " con uid: " + req.getEntita().getUid());
			primaNota.setSoggetto(soggetto);
		} else {
			log.warn(methodName, "Non sono riuscito ad individuare il Soggetto per l'entita' di tipo " + req.getEntita().getClass().getSimpleName() + " con uid: " + req.getEntita().getUid() + " lo lascio null.");
		}
	}
	
}
