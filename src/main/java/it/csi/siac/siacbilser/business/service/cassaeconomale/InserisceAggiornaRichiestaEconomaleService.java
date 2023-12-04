/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cassaeconomale;

import java.util.Calendar;
import java.util.EnumSet;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.documento.MovimentoGestioneServiceCallGroup;
import it.csi.siac.siacbilser.business.service.documentospesa.RegistrazioneGENServiceHelper;
import it.csi.siac.siacbilser.integration.dad.CassaEconomaleDad;
import it.csi.siac.siacbilser.integration.dad.CodificaDad;
import it.csi.siac.siacbilser.integration.dad.ImpegnoBilDad;
import it.csi.siac.siacbilser.integration.dad.RichiestaEconomaleDad;
import it.csi.siac.siacbilser.integration.dad.SoggettoDad;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.ModalitaPagamentoDipendente;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.StatoOperativoGiustificativi;
import it.csi.siac.siaccecser.model.StatoOperativoRichiestaEconomale;
import it.csi.siac.siaccecser.model.TipoRichiestaEconomale;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAttributiMovimentoGestioneOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.TipoCollegamento;

/**
 * The Class InserisceAggiornaRichiestaEconomaleService.
 */
public abstract class InserisceAggiornaRichiestaEconomaleService<REQ extends ServiceRequest,RES extends ServiceResponse> extends CheckedAccountBaseService<REQ, RES> {
	
	@Autowired
	protected RichiestaEconomaleDad richiestaEconomaleDad;
	@Autowired
	protected SoggettoDad soggettoDad;
	@Autowired
	protected CodificaDad codificaDad;
	@Autowired
	protected CassaEconomaleDad cassaEconomaleDad;
	@Autowired
	private ImpegnoBilDad impegnoBilDad;
	@Autowired
	protected RegistrazioneGENServiceHelper registrazioneGENServiceHelper;
	
	protected RichiestaEconomale richiestaEconomale;
	protected Impegno impegnoOSubImpegno;
	
	protected Calendar now;
	protected Integer annoBilancio;
	protected String codiceTipoRichiesta;
	protected Boolean datiEconomoPresenti;
	
	private MovimentoGestioneServiceCallGroup movimentoGestioneServiceCallGroup;

	
	/**
	 * Check dei service param specifico per un tipo di richiesta economale. 
	 * Da sovrascrivere definire un comportamento specifico di una richiesta economale.
	 */
	protected void checkServiceParamRichiestaEconomale() throws ServiceParamError {
		// Da implementare nella sottoclasse se necessario
	}
	
	/**
	 * Controlla se il soggetto e' valorizzato come soggetto SIAC o HR.
	 *
	 * @throws ServiceParamError the service param error
	 */
	protected void checkSoggettoRichiestaEconomale() throws ServiceParamError {
		if(richiestaEconomale.getSoggetto()==null) {
			//Caso in cui il soggetto arrivi da HR
			checkNotBlank(richiestaEconomale.getMatricola(), "matricola richiesta economale");
//			nome; //facoltativo
//			cognome; //facoltativo
//			codiceFiscale; //facoltativo
//			codiceBeneficiario; //facoltativo
		} else {
			//Caso in cui il soggetto sia censito su SIAC
			checkEntita(richiestaEconomale.getSoggetto(), "soggetto richiesta economale");
		}
	}

	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		richiestaEconomaleDad.setEnte(ente);
		richiestaEconomaleDad.setLoginOperazione(loginOperazione);
		codificaDad.setEnte(ente);
		
		now = new GregorianCalendar();
//		annoBilancio = now.get(Calendar.YEAR); //TODO verificare se va bene l'anno corrente
		
		movimentoGestioneServiceCallGroup = new MovimentoGestioneServiceCallGroup(serviceExecutor, req.getRichiedente(), ente, richiestaEconomale.getBilancio());
	}

	/**
	 * Controlla che il dettaglio di pagamento sia stato inserito correttamente, qualora sia obbligatorio
	 */
	protected void checkDettaglioPagamento() {
		if(richiestaEconomale.getMovimento() == null) {
			// Manca direttamente il movimento. Esco subito
			return;
		}
		
		ModalitaPagamentoDipendente modalitaPagamentoDipendente = richiestaEconomaleDad.findModalitaPagamentoDipendenteByUid(richiestaEconomale.getMovimento().getModalitaPagamentoDipendente().getUid());
		if(modalitaPagamentoDipendente == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("modalita di pagamento dipendente", "uid " + richiestaEconomale.getMovimento().getModalitaPagamentoDipendente().getUid()));
		}
		
		// Se ho 'CONTANTI', allora il dettaglio non e' obbligatorio
		if(StringUtils.isBlank(richiestaEconomale.getMovimento().getDettaglioPagamento()) && !"CON".equals(modalitaPagamentoDipendente.getCodice())) {
			// TODO: mettere in una costante da qualche parte
			throw new BusinessException(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("dettaglio pagamento movimento richiesta economale"));
		}
		
	}


	/**
	 * Check specifico per un tipo di richiesta economale. 
	 * Da sovrascrivere definire un comportamento specifico di una richiesta economale.
	 */

	protected void checkRichiestaEconomale() {
		// Da implementare nella sottoclasse se necessario
	}

	/**
	 * Da sovrascrivere per determinare lo stato dei vari tipi.
	 * Se non viene specificato nessun tipo richiesta lo stato resta "PRENOTATA"
	 */
	protected void determinaStato() {
		richiestaEconomale.setStatoOperativoRichiestaEconomale(StatoOperativoRichiestaEconomale.PRENOTATA);
	}

	protected void caricaTipoRichiestaDaCodice() {
		if(codiceTipoRichiesta==null){
			return;
		}
		
		TipoRichiestaEconomale tipoRichiestaEconomale = codificaDad.ricercaCodifica(TipoRichiestaEconomale.class, codiceTipoRichiesta);
		richiestaEconomale.setTipoRichiestaEconomale(tipoRichiestaEconomale);
		
// 		Prima era
//		Integer uidTipoRichiesta = richiestaEconomaleDad.findIdTipoRichiestaByCodice(codiceTipoRichiesta);
//		if(uidTipoRichiesta == null || uidTipoRichiesta == 0) {
//			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Tipo richiesta economale con codice ", codiceTipoRichiesta));
//		}
//		TipoRichiestaEconomale tipoRichiesta = new TipoRichiestaEconomale();
//		tipoRichiesta.setCodice(codiceTipoRichiesta);
//		tipoRichiesta.setUid(uidTipoRichiesta);
//		richiestaEconomale.setTipoRichiestaEconomale(tipoRichiesta);
		
	}
	

	protected void determinaStatoOperativoGiustificativi() {
		for(Giustificativo giustificativo : richiestaEconomale.getGiustificativi()){
			if(Boolean.TRUE.equals(giustificativo.getFlagInclusoNelPagamento())){
				giustificativo.setStatoOperativoGiustificativi(StatoOperativoGiustificativi.VALIDO);
			}else{
				giustificativo.setStatoOperativoGiustificativi(StatoOperativoGiustificativi.ESCLUSO_AL_PAGAMENTO);
			}
		}
	}

	protected void caricaImpegnoOSubImpegno() {
		this.impegnoOSubImpegno = impegnoBilDad.findMiniminalImpegnoDataByUid(richiestaEconomale.getImpegno().getUid());
		if(impegnoOSubImpegno == null) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Impegno", "uid " + richiestaEconomale.getImpegno().getUid()));
		}
		RicercaAttributiMovimentoGestioneOttimizzato ramgo = new RicercaAttributiMovimentoGestioneOttimizzato();
		ramgo.setSubPaginati(true);
		ramgo.setCaricaSub(richiestaEconomale.getSubImpegno() != null && richiestaEconomale.getSubImpegno().getUid() != 0);
		ramgo.setEscludiSubAnnullati(true);
		ramgo.setNumPagina(1);
		ramgo.setNumRisultatiPerPagina(1);
		
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		// Non richiedo NESSUN importo derivato.
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		// Non richiedo NESSUN classificatore
		datiOpzionaliCapitoli.setTipologieClassificatoriRichiesti(EnumSet.noneOf(TipologiaClassificatore.class));
		

		RicercaImpegnoPerChiaveOttimizzatoResponse resRIPC = movimentoGestioneServiceCallGroup.ricercaImpegnoPerChiaveOttimizzatoCached(impegnoOSubImpegno, ramgo, datiOpzionaliCapitoli, richiestaEconomale.getSubImpegno());
		
		if(resRIPC.getImpegno() == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Impegno", this.impegnoOSubImpegno.getAnnoMovimento() + "/" + this.impegnoOSubImpegno.getNumeroBigDecimal()));
		}
		
		this.impegnoOSubImpegno = resRIPC.getImpegno();
		
		if(richiestaEconomale.getSubImpegno() != null && richiestaEconomale.getSubImpegno().getUid() != 0) {
			// Devo trovare l'impegno
			SubImpegno subImpegno = ottieniSubimpegno(impegnoOSubImpegno);
			if(subImpegno == null) {
				throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("SubImpegno", richiestaEconomale.getSubImpegno().getUid()));
			}
			impegnoOSubImpegno = subImpegno;
		}
	}

	private SubImpegno ottieniSubimpegno(Impegno impegno) {
		if(impegno.getElencoSubImpegni() != null && !impegno.getElencoSubImpegni().isEmpty()) {
			for(SubImpegno si : impegno.getElencoSubImpegni()) {
				if(si != null && si.getUid() == richiestaEconomale.getSubImpegno().getUid()) {
					return si;
				}
			}
		}
		return null;
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
		
		
		registrazioneGENServiceHelper.init(serviceExecutor, ente, req.getRichiedente(), loginOperazione, richiestaEconomale.getBilancio());
		annullaRegistrazioniGENGSAPrecedenti();
		
		Evento evento = registrazioneGENServiceHelper.determinaEventoCassaEconomale(TipoCollegamento.RICHIESTA_ECONOMALE, richiestaEconomale.getTipoRichiestaEconomale(), false);
		ElementoPianoDeiConti elementoPianoDeiConti = determinaElementoPianoDeiConti();
		
		RegistrazioneMovFin registrazioneMovFin = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, richiestaEconomale , elementoPianoDeiConti, Ambito.AMBITO_FIN);  //o AMBITO_CEC!!
		registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioneMovFin);
		
		if(isDaRegistrareInGSA()){
			log.debug(methodName, "Inserisco anche la registrazione per GSA relativa alla richiesta economale con uid: "+richiestaEconomale.getUid());
			
			RegistrazioneMovFin registrazioneMovFinGSA = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, richiestaEconomale , elementoPianoDeiConti, Ambito.AMBITO_GSA);
			registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioneMovFinGSA);
		}
	}
	
	protected void annullaRegistrazioniGENGSAPrecedenti() {
		// To be implemented
	}
	
	private boolean isDaRegistrareInGSA() {
		// SIAC-6238
		return false;
	}


	private ElementoPianoDeiConti determinaElementoPianoDeiConti() {
		String methodName = "determinaElementoPianoDeiConti";
		
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
		boolean numeroMovimentoPresente = richiestaEconomale.getMovimento()!=null && richiestaEconomale.getMovimento().getNumeroMovimento()!=null;
		
		return numeroMovimentoPresente;
	}
	
}
