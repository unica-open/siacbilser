/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.cespiti.utility.AmmortamentoAnnuoCespiteFactory;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.AmmortamentoAnnuoCespiteDad;
import it.csi.siac.siacbilser.integration.dad.DettaglioAmmortamentoAnnuoCespiteDad;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciAmmortamentoMassivoCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciAmmortamentoMassivoCespiteResponse;
import it.csi.siac.siaccespser.model.AmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.DettaglioAmmortamentoAnnuoCespite;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Inserisce e completa gli Elenchi di Documenti Spesa, Entrata, IVA Spesa, Iva
 * Entrata, comprensivi di AllegatoAtto.
 * 
 * @author Antonino
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisciAmmortamentoMassivoCespiteService extends CheckedAccountBaseService<InserisciAmmortamentoMassivoCespite, InserisciAmmortamentoMassivoCespiteResponse> {

	private static final String cespiteScartato = "CESPITE_SCARTATO";
	private static final String cespitiNonElaborati = "CESPITI_NON_ELABORATI";
	private static final String ammortamentoEffettuato = "AMMORTAMENTO_EFFETTUATO";
	//DAD
	@Autowired
	private AmmortamentoAnnuoCespiteDad ammortamentoAnnuoCespiteDad;
	@Autowired 
	private DettaglioAmmortamentoAnnuoCespiteDad dettaglioAmmortamentoAnnuoCespiteDad;
	
	private List<Integer> uidsCespiti;
	
	private List<Integer> uidsElaborati = new ArrayList<Integer>();
	
	//questa potrebbe essere mappa uid - object[]
	private List<Object[]> datiAmmortamentoCespiti;
	
	

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		this.uidsCespiti = req.getUidsCespiti();
		checkCondition(Boolean.TRUE.equals(req.getInserisciAmmortamentoCompleto()) || (req.getUltimoAnnoAmmortamento() != null && !req.getUltimoAnnoAmmortamento().equals(Integer.valueOf(0))), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ultimo anno ammortamento"));
		checkCondition(uidsCespiti != null && !uidsCespiti.isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista cespiti"));
	}

	@Override
	protected void init() {
		super.init();
		ammortamentoAnnuoCespiteDad.setEnte(ente);
		ammortamentoAnnuoCespiteDad.setLoginOperazione(loginOperazione);
		dettaglioAmmortamentoAnnuoCespiteDad.setEnte(ente);
		dettaglioAmmortamentoAnnuoCespiteDad.setLoginOperazione(loginOperazione);
	}

	@Transactional(timeout=AsyncBaseService.TIMEOUT * 4)
	@Override
	public InserisciAmmortamentoMassivoCespiteResponse executeService(InserisciAmmortamentoMassivoCespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT * 4)
	@Override
	public InserisciAmmortamentoMassivoCespiteResponse executeServiceTxRequiresNew(InserisciAmmortamentoMassivoCespite serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		caricaDatiAmmortamento();
		
		inserisciAmmortamenti();
		
		aggiungiMessaggiAmmortamentiNonEffettuatiPerMancanzaDiDati();
	}

	/**
	 * @param now
	 */
	private void inserisciAmmortamenti() {
		Date now = new Date();
		for(Object[] datiAmmortamento : datiAmmortamentoCespiti) {
			inserisciAmmortamento(datiAmmortamento, now);
		}
	}

	private void aggiungiMessaggiAmmortamentiNonEffettuatiPerMancanzaDiDati() {
		if(uidsElaborati.size() == uidsCespiti.size()) {
			return;
		}
		uidsCespiti.removeAll(uidsElaborati);
		String messaggio = new StringBuilder()
				.append("I seguenti cespiti non sono stati elaborati per mancanza di informazioni: ")
				.append(StringUtils.join(uidsCespiti, ", "))
				.append(".")
				.append(" Per l'elaborazione dell'ammortamento &egrave; necessario che il cespite sia collegato ad un tipo bene la cui categoria ha aliquota annua ed un tipo calcolo validi nell'anno di bilancio.")
				.toString();
		res.addMessaggio(cespitiNonElaborati, messaggio);
	}

	/**
	 * Crea ammortamenti.
	 *
	 * @return the list
	 */
	private void inserisciAmmortamento(Object[] datiAmmortamentoCespite,Date dataAmmortamento) {
		
		final String methodName ="creaAmmortamenti";
		
		if(datiAmmortamentoCespite == null || datiAmmortamentoCespite.length < 5) {
			log.debug(methodName, "Non sono stati reperiti tutti i dati per un certo cespite. lo salto. " );
			return;
		}
		
		Integer uidCespite = (Integer) datiAmmortamentoCespite[0];
		
		BigDecimal valoreAttuale = (BigDecimal) datiAmmortamentoCespite[1];
		Date dataIngressoInventario = (Date) datiAmmortamentoCespite[2];
		BigDecimal aliquotaAnnua = (BigDecimal) datiAmmortamentoCespite[3];
		String codiceTipoCalcolo = (String) datiAmmortamentoCespite[4];
		
		String codiceMessaggio = "";
		String descrizioneMessaggio = "";
		
		uidsElaborati.add(uidCespite);
		
		try {
			AmmortamentoAnnuoCespiteFactory factoryAmmortamento = new AmmortamentoAnnuoCespiteFactory(codiceTipoCalcolo, aliquotaAnnua, uidCespite, dataIngressoInventario, valoreAttuale, req.getUltimoAnnoAmmortamento(),dataAmmortamento);
			AmmortamentoAnnuoCespite ammortamentoCespite = ammortamentoAnnuoCespiteDad.cancellaDettagliAmmortamentoSenzaPrimaNotaDefinitiva(uidCespite);
			factoryAmmortamento.elaboraAmmortamento(ammortamentoCespite);
			//TODO: valutare se sono questi metodi sono da mettere nella factory
			ammortamentoCespite = inserisciTestataSeNecessario(factoryAmmortamento, ammortamentoCespite);
			inserisciDettagli(factoryAmmortamento, ammortamentoCespite);
			codiceMessaggio = ammortamentoEffettuato;
			descrizioneMessaggio = new StringBuilder().append("Ammortamento effettuato con successo per il cespite [uid: " + uidCespite + "].").toString();
		} catch (BusinessException e) {
			Errore errore = e.getErrore() != null? e.getErrore() : ErroreCore.ERRORE_DI_SISTEMA.getErrore("errore null, nessuna informazione disponibile.");
			codiceMessaggio = cespiteScartato;
			descrizioneMessaggio =  new StringBuilder().append("Scartato cespite [uid: " + uidCespite + "] per la seguente motivazione: " + errore.getTesto()).toString();
			log.warn(methodName, descrizioneMessaggio);
			
		}
		
		res.addMessaggio(codiceMessaggio, descrizioneMessaggio);
		
	}

	/**
	 * @param factoryAmmortamento
	 * @param ammortamentoCespite
	 */
	private void inserisciDettagli(AmmortamentoAnnuoCespiteFactory factoryAmmortamento,	AmmortamentoAnnuoCespite ammortamentoCespite) {
		if(ammortamentoCespite == null || ammortamentoCespite.getUid() == 0) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("inserimento dettaglio amortamento", "testata ammortamento"));
		}
		for (DettaglioAmmortamentoAnnuoCespite dettaglio : factoryAmmortamento.getDettagliAmmortamentoElaborati()) { 
			dettaglioAmmortamentoAnnuoCespiteDad.inserisciDettaglioAmmortamentoAnnuoCespite(dettaglio, ammortamentoCespite.getUid());
		}
	}

	/**
	 * @param factoryAmmortamento
	 * @param ammortamentoCespite
	 * @return
	 */
	private AmmortamentoAnnuoCespite inserisciTestataSeNecessario(AmmortamentoAnnuoCespiteFactory factoryAmmortamento,	AmmortamentoAnnuoCespite ammortamentoCespite) {
		if(ammortamentoCespite != null && ammortamentoCespite.getUid() != 0) {
			return ammortamentoCespite;
		}
		AmmortamentoAnnuoCespite ammCreato = ammortamentoAnnuoCespiteDad.inserisciTestataAmmortamentoAnnuoCespite(factoryAmmortamento.getTestataAmmortamento());
		return ammCreato;
	}

	/**
	 * Carica dati ammortamento.
	 */
	private void caricaDatiAmmortamento() {
		
		datiAmmortamentoCespiti = ammortamentoAnnuoCespiteDad.caricaDatiAmmortamentoCespite(uidsCespiti, Utility.ultimoGiornoDellAnno(req.getAnnoBilancio()));
		
	}
	
}


