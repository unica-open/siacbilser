/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cassaeconomale;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.documentospesa.RegistrazioneGENServiceHelper;
import it.csi.siac.siacbilser.integration.dad.ImpegnoBilDad;
import it.csi.siac.siacbilser.integration.dad.RendicontoRichiestaEconomaleDad;
import it.csi.siac.siacbilser.integration.dad.RichiestaEconomaleDad;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.ModalitaPagamentoDipendente;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.StatoOperativoGiustificativi;
import it.csi.siac.siaccecser.model.StatoOperativoRichiestaEconomale;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.TipoCollegamento;

/**
 * The Class InserisceAggiornaRendicontoRichiestaService.
 */
public abstract class InserisceAggiornaRendicontoRichiestaService<REQ extends ServiceRequest,RES extends ServiceResponse> extends CheckedAccountBaseService<REQ, RES> {
	
	@Autowired
	protected RendicontoRichiestaEconomaleDad rendicontoRichiestaEconomaleDad;
	@Autowired
	protected RichiestaEconomaleDad richiestaEconomaleDad;
	@Autowired
	private ImpegnoBilDad impegnoBilDad;
	
	@Autowired
	protected RegistrazioneGENServiceHelper registrazioneGENServiceHelper;
	
	protected RendicontoRichiesta rendicontoRichiesta;
	//private Calendar now;
//	protected Integer annoBilancio;

	protected Boolean isNecessarioMovimento;
	protected Boolean isRestituzioneTotale;

	@Override
	protected void init() {
		
		richiestaEconomaleDad.setEnte(ente);
		richiestaEconomaleDad.setLoginOperazione(loginOperazione);
		
		rendicontoRichiestaEconomaleDad.setEnte(ente);
		rendicontoRichiestaEconomaleDad.setLoginOperazione(loginOperazione);
		
		//now = new GregorianCalendar();
		//annoCorrente = now.get(Calendar.YEAR); //TODO verificare se va bene l'anno corrente
	}
	
	protected void caricaRichiestaEconomale() {
		// SIAC-4714: salvo il dato per poterlo riutilizzare
		String descrizioneDellaRichiesta = rendicontoRichiesta.getRichiestaEconomale().getDescrizioneDellaRichiesta();
		RichiestaEconomale richiestaEconomale = richiestaEconomaleDad.findRichiestaEconomaleByUid(rendicontoRichiesta.getRichiestaEconomale().getUid());
		richiestaEconomale.setDescrizioneDellaRichiesta(descrizioneDellaRichiesta);
		
		rendicontoRichiesta.setRichiestaEconomale(richiestaEconomale);
	}


	/**
	 * Controlla che il dettaglio di pagamento sia stato inserito correttamente, qualora sia obbligatorio
	 */
	protected void checkDettaglioPagamento() {
		if(rendicontoRichiesta.getMovimento() == null) {
			// Manca direttamente il movimento. Esco subito
			return;
		}
		
		ModalitaPagamentoDipendente modalitaPagamentoDipendente = richiestaEconomaleDad.findModalitaPagamentoDipendenteByUid(rendicontoRichiesta.getMovimento().getModalitaPagamentoDipendente().getUid());
		if(modalitaPagamentoDipendente == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("modalita di pagamento dipendente", "uid " + rendicontoRichiesta.getMovimento().getModalitaPagamentoDipendente().getUid()));
		}
		
		// Se ho 'CONTANTI', allora il dettaglio non e' obbligatorio
		if(StringUtils.isBlank(rendicontoRichiesta.getMovimento().getDettaglioPagamento()) && !"CON".equals(modalitaPagamentoDipendente.getCodice())) {
			// TODO: mettere in una costante da qualche parte
			throw new BusinessException(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("dettaglio pagamento movimento richiesta economale"));
		}
		
	}

	protected void determinaStatoOperativoGiustificativi() {
		for(Giustificativo giustificativo : rendicontoRichiesta.getGiustificativi()){
			if(Boolean.TRUE.equals(giustificativo.getFlagInclusoNelPagamento())){
				giustificativo.setStatoOperativoGiustificativi(StatoOperativoGiustificativi.VALIDO);
			}else{
				giustificativo.setStatoOperativoGiustificativi(StatoOperativoGiustificativi.ESCLUSO_AL_PAGAMENTO);
			}
		}
	}


	protected StatoOperativoRichiestaEconomale determinaStatoOperativoRichiesta() {
		if(Boolean.TRUE.equals(isRestituzioneTotale)) {
			return StatoOperativoRichiestaEconomale.AGLI_ATTI;
		}
		if(Boolean.TRUE.equals(isNecessarioMovimento)) {
			return StatoOperativoRichiestaEconomale.EVASA;
		}
		return StatoOperativoRichiestaEconomale.RENDICONTATA;
	}

	/* ############################################ Attivazione GEN ############################################ */
	
	/**
	 * Dopo aver inserito una richiesta (tranne che per le richieste di pagamento fatture) occorre effettuare 
	 * una scrittura nel registro per la contabilit&agrave; generale (Il tipo evento deve essere impostato a ‘EC' 
	 * e l'evento deve assumere il codice associato all'operazione che si sta effettuando): fare riferimento 
	 * al documento BIL-MULT-SIAC-REQ-009-V01- Raccordi FinGen Configurazione.xls
	 * Il V livello del piano dei conti da utilizzare per il salvataggio nel registro è quello 
	 * dell'impegno o del subimpegno associato alla richiesta.

	 */
	protected void gestisciRegistrazioneGEN() {
		String methodName = "gestisciRegistrazioneGEN";
		
		if(!isCondizioneDiAttivazioneGENSoddisfatta()) {
			log.debug(methodName, "condizione di attivazione non soddisfatta. Non viene inserita la RegistrazioneMovFin.");
			return;
		}
		
		RichiestaEconomale richiestaEconomale = rendicontoRichiesta.getRichiestaEconomale();
		registrazioneGENServiceHelper.init(serviceExecutor, ente, req.getRichiedente(), loginOperazione, richiestaEconomale.getBilancio());
		annullaRegistrazioniGENGSAPrecedenti();

		Evento evento = registrazioneGENServiceHelper.determinaEventoCassaEconomale(TipoCollegamento.RENDICONTO_RICHIESTA, richiestaEconomale.getTipoRichiestaEconomale(), rendicontoRichiesta.getImportoIntegrato()==null || BigDecimal.ZERO.equals(rendicontoRichiesta.getImportoIntegrato()) ); //Boolean.TRUE.equals(isRestituzioneTotale)
		ElementoPianoDeiConti elementoPianoDeiConti = determinaElementoPianoDeiConti(richiestaEconomale);

		RegistrazioneMovFin registrazioneMovFin = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, rendicontoRichiesta , elementoPianoDeiConti, Ambito.AMBITO_FIN);
		registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioneMovFin);
		
		if(isDaRegistrareInGSA()){
			log.debug(methodName, "Inserisco anche la registrazione per GSA relativa al rendiconto con uid: "+rendicontoRichiesta.getUid());

			RegistrazioneMovFin registrazioneMovFinGSA = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, rendicontoRichiesta , elementoPianoDeiConti, Ambito.AMBITO_GSA);
			registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioneMovFinGSA);
		}
		
	}
	
	protected void annullaRegistrazioniGENGSAPrecedenti() {
		// To be implemented
	}
	

	private boolean isDaRegistrareInGSA() {
		//SIAC-6238
		return false;
	}

	private ElementoPianoDeiConti determinaElementoPianoDeiConti(RichiestaEconomale richiestaEconomale) {
		final String methodName = "determinaElementoPianoDeiConti";
		
		Impegno impegnoOSubImpegno = null; //= this.impegnoOSubImpegno;
		if(richiestaEconomale.getSubImpegno()!=null && richiestaEconomale.getSubImpegno().getUid()!=0){
			impegnoOSubImpegno = richiestaEconomale.getSubImpegno();
		} else if (richiestaEconomale.getImpegno()!=null && richiestaEconomale.getImpegno().getUid()!=0){
			impegnoOSubImpegno = richiestaEconomale.getImpegno();
		}
		
		// SIAC-6265: il caricamento del PDC deve avvenire tramite
		if(impegnoOSubImpegno == null || impegnoOSubImpegno.getUid() == 0) {
			log.debug(methodName, "Impossibile reperire l'elementoPianoDeiConti in quanto non esiste un impegno/subimpegno associato alla richiesta economale");
			return null;
		}
		ElementoPianoDeiConti elementoPianoDeiConti = null;
		if(impegnoOSubImpegno instanceof SubImpegno) {
			// Caricamento via subimpegno
			log.debug(methodName, "Uid subimpegno da cui ricavare l'elementoPianoDeiConti: " + impegnoOSubImpegno.getUid());
			elementoPianoDeiConti = impegnoBilDad.ottieniPianoDeiContiBySubMovgestId(impegnoOSubImpegno.getUid());
		} else if (impegnoOSubImpegno instanceof Impegno) {
			// Caricamento via impegno
			log.debug(methodName, "Uid impegno da cui ricavare l'elementoPianoDeiConti: " + impegnoOSubImpegno.getUid());
			elementoPianoDeiConti = impegnoBilDad.ottieniPianoDeiContiByMovgestId(impegnoOSubImpegno.getUid());
		}
		
		log.debug(methodName, "trovato elementoPianoDeiConti: " + (elementoPianoDeiConti != null ? elementoPianoDeiConti.getUid() : "null"));
		return elementoPianoDeiConti;
	}


	/**
	 * CONDIZIONE GENERALE CASSA ECONOMALE(3)
	 * INSERIRE IL DATO SUL REGISTRO QUANDO
	 * -> viene effettuato il pagamento della richiesta (tutte tranne pagamento fatture) (quindi quando alla richiesta viene assegnato un numero di movimento)
	 * 
	 * N.B. Il V livello del piano dei conti da utilizzare nella registrazione è quelo dell'impegno o del subimpegno associato alla richiesta
	 * 
	 * @return
	 */
	protected boolean isCondizioneDiAttivazioneGENSoddisfatta() {
//		boolean numeroMovimentoPresente = richiestaEconomale.getMovimento()!=null && richiestaEconomale.getMovimento().getNumeroMovimento()!=null;
		
		return Boolean.TRUE.equals(this.isNecessarioMovimento);
	}

	
	
}
