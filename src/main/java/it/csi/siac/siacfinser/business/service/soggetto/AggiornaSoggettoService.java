/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.soggetto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.StringUtils;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSoggettoResponse;
import it.csi.siac.siacfinser.integration.dad.SoggettoFinDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoControlliAggiornamentoMDPDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ValidaSoggettoInfoDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.integration.util.OperazioneValidaSoggetto;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto.StatoOperativoSedeSecondaria;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaSoggettoService extends AbstractSoggettoService<AggiornaSoggetto, AggiornaSoggettoResponse> {

	@Autowired
	SoggettoFinDad soggettoDad;

	@Autowired
	protected DocumentoSpesaService documentoSpesaService;
	
	@Override
	protected void init() {
		final String methodName="init";
		log.debug(methodName, " - Begin");
		initModalitaPagamentoSoggettoHelper();
	}

	/*
	 * VARIAZIONE LINEE GUIDA 3.2.2.2
	 */
	@Override
	@Transactional
	public AggiornaSoggettoResponse executeService(AggiornaSoggetto serviceRequest) {
		//  Auto-generated method stub
		return super.executeService(serviceRequest);
	}

	@Override
	public void execute() {
		final String methodName = "AggiornaSoggettoService - execute()";

		//1. Lettura variabili di input
		Richiedente richiedente = req.getRichiedente();
		Ente ente = req.getEnte();
		Integer idEnte = ente.getUid();
		Soggetto soggettoRicevuto = req.getSoggetto();
		String loginOperazione = richiedente.getAccount().getNome();
		Soggetto soggetto = null;
		StatoOperativoAnagrafica statoIndicato = soggettoRicevuto.getStatoOperativo();
		
		String codiceAmbito = req.getCodificaAmbito();
		
		if (org.apache.commons.lang.StringUtils.isEmpty(codiceAmbito))
			codiceAmbito = Constanti.AMBITO_FIN;

		
		DatiOperazioneDto datiOperazioneDto = commonDad.inizializzaDatiOperazione(ente, richiedente, Operazione.MODIFICA, codiceAmbito, null);
		
		
		//2. Viene invocato il metodo getIdForQuery di SoggettoFinDad. 
		//Tale metodo svolge la funzione iniziale di interprete del model Soggetto ricevuto nella request, 
		//dovendo capire se la richiesta di aggiornamento si riferisce ad un soggetto piuttosto che ad un 
		//soggetto_mod o ancora ad una sede. Restituisce la chiave primaria per permettere ai metodi successivi
		//di lavorare comodamente per id fisico, ovviamente nel model in input riceve il codice del soggetto 
		//(in modo che il servizio stesso verso l'esterno "lavori per codice e non per id").
		ValidaSoggettoInfoDto infoValida = soggettoDad.getIdForQuery(soggettoRicevuto,codiceAmbito, idEnte);
		Integer idQuery = infoValida.getIdSiacTSoggetto();

		if(StatoOperativoAnagrafica.ANNULLATO.equals(statoIndicato)){
			//SIAMO NELLA SITUAZIONE IN CUI E' STATO RICHIESTO L'ANNULLAMENTO DEL SOGGETTO/SEDE
			
			
			String ambitoDaService = req.getCodificaAmbito();
			
			if(org.apache.commons.lang.StringUtils.isEmpty(ambitoDaService))
				ambitoDaService = Constanti.AMBITO_FIN;
			
			
			// Se FIN allora devo far partire i controlli
			if(null!=ambitoDaService && ambitoDaService.equals(Constanti.AMBITO_FIN)){
				//Occorre verificare che non ci siano legami con altri oggetti che ci impediscono di procedere con l'annullamento:
				List<Errore> listaErroriLegami = controlliDipendenzaEntita(idQuery, ente, richiedente, Operazione.ANNULLA);
				if(listaErroriLegami!=null && listaErroriLegami.size()>0){
					//per presenza di legami il servizio termina con errore
					res.setSoggettoAggiornato(null);
					res.setErrori(listaErroriLegami);
					res.setEsito(Esito.FALLIMENTO);
					return;
				}
			}
			//e possibile procedere con l'annullamento:
			soggettoDad.annullaSoggetto(richiedente, idQuery, ente);
			soggetto = soggettoRicevuto;
		 
		} else if(StatoOperativoAnagrafica.BLOCCATO.equals(statoIndicato)){
			//SIAMO NELLA SITUAZIONE IN CUI E' STATO RICHIESTO DI BLOCCARE IL SOGGETTO/SEDE:
			//SIAC-7114 si uniforma il comportamento di blocca soggetto come sospendi soggetto
			soggettoDad.bloccaSoggetto(richiedente, idQuery, ente, soggettoRicevuto.getNotaStatoOperativo());
			soggetto = soggettoRicevuto;
		} else if(StatoOperativoAnagrafica.SOSPESO.equals(statoIndicato)){
			//SIAMO NELLA SITUAZIONE IN CUI E' STATO RICHIESTO DI SOSPENDERE IL SOGGETTO/SEDE:
			soggettoDad.sospendiSoggetto(richiedente, idQuery, ente, soggettoRicevuto.getNotaStatoOperativo());
			soggetto = soggettoRicevuto;
		} else if(StatoOperativoAnagrafica.VALIDO.equals(statoIndicato)){

			//E' STATO INDICATO IN INPUT LO STATO "VALIDO", dobbiamo ancora capire se e' una richiesta di passaggio in stato valido
			//oppure un semplice aggiornamento di metadati per un soggetto/sede gia' in stato valido (se lo stato e' lo stesso si tratta di aggiornamento metadati)
			
			//valutazione dell'operazione
			OperazioneValidaSoggetto operazioneDaEseguire = soggettoDad.valutaOperazioneValidaSoggetto(statoIndicato,idQuery);
			//isSede ci dice invece se in input al servizio e' stato indicato un soggetto oppure una sede secondaria:
			boolean isSede = soggettoDad.isSedeSecondaria(idQuery, idEnte);
			//
			Integer idSoggettoOrSoggettoMod = infoValida.getIdSoggettoOrMod();

			//Effettuiamo dei controlli preliminari rispetto alla funzione di aggiornamento:
			boolean controlliAgg = controlliPreAggiornamento(soggettoRicevuto, idSoggettoOrSoggettoMod, datiOperazioneDto, operazioneDaEseguire);
			if(!controlliAgg){
				return;
			}
			
			if(isSede && (operazioneDaEseguire.equals(OperazioneValidaSoggetto.VALIDA_DA_IN_MODIFICA) || operazioneDaEseguire.equals(OperazioneValidaSoggetto.VALIDA_DA_PROVVISORIO)
							) ){
				// GESTIONE VALIDAZIONE SEDE SECONDARIA
				soggetto = soggettoDad.validaSedeSecondaria(loginOperazione, idEnte, idSoggettoOrSoggettoMod, richiedente);
				// 01/10/2014 : inizio
				// Ricarico il soggetto prima di restituirlo
				boolean includeModifica = true;
				Soggetto soggettoReload = soggettoDad.ricercaSoggetto(codiceAmbito, idEnte, soggetto.getCodiceSoggetto(), includeModifica, true);
				soggettoReload = completaInformazioni(soggettoReload, codiceAmbito, idEnte, soggetto.getCodiceSoggetto(), includeModifica, richiedente,datiOperazioneDto);
				/*
				 *  METTE LA LABEL SENZA UNDERSCORE
				 */
				if(soggettoReload.getSediSecondarie()!=null && soggettoReload.getSediSecondarie().size()>0){
					
					for (SedeSecondariaSoggetto sedeSecondariaSoggetto : soggettoReload.getSediSecondarie()) {
						if(sedeSecondariaSoggetto.getStatoOperativoSedeSecondaria().equals(StatoOperativoSedeSecondaria.IN_MODIFICA)){				
							sedeSecondariaSoggetto.setDescrizioneStatoOperativoSedeSecondaria(Constanti.STATO_IN_MODIFICA_no_underscore);
						}
					}
				} 
				res.setSoggettoAggiornato(soggettoReload);
				res.setErrori(null);
				res.setEsito(Esito.SUCCESSO);
				return;
			}

			if(isSede && (  operazioneDaEseguire.equals(OperazioneValidaSoggetto.VALIDA_DA_BLOCCATO) || operazioneDaEseguire.equals(OperazioneValidaSoggetto.VALIDA_DA_SOSPESO) )){
				//QUESTO CASO NON DOVREBBE MAI VERIFICARSI
				return;
			}

			if(!isSede && operazioneDaEseguire.equals(OperazioneValidaSoggetto.VALIDA_DA_BLOCCATO)){
				//SI VALIDA UN SOGGETTO CHE ERA BLOCCATO
				soggettoDad.validaDaBloccato(richiedente, idSoggettoOrSoggettoMod, ente);
				soggetto = soggettoRicevuto;
			} else if(!isSede && operazioneDaEseguire.equals(OperazioneValidaSoggetto.VALIDA_DA_SOSPESO)){
				//SI VALIDA UN SOGGETTO CHE ERA SOSPESO
				soggettoDad.validaDaSospeso(richiedente, idSoggettoOrSoggettoMod, ente);
				soggetto = soggettoRicevuto;
			} else if(!isSede && operazioneDaEseguire.equals(OperazioneValidaSoggetto.VALIDA_DA_IN_MODIFICA)){
				//SI VALIDA UN SOGGETTO CHE ERA IN MODIFICA
				Integer idPadreSiacTSoggettoInModifica = infoValida.getIdSiacTSoggetto();
				Integer idSiacTSoggettoInModifica = infoValida.getIdSiacTSoggettoMod();
				soggetto = soggettoDad.validaDaInModifica(richiedente, ente, idSiacTSoggettoInModifica, idPadreSiacTSoggettoInModifica, datiOperazioneDto);
			} else if(!isSede && operazioneDaEseguire.equals(OperazioneValidaSoggetto.VALIDA_DA_PROVVISORIO)){
				//SI VALIDA UN SOGGETTO CHE ERA PROVVISORIO
				soggettoDad.validaDaProvvisorio(richiedente, idSoggettoOrSoggettoMod, ente);
				soggetto = soggettoRicevuto;
			} else {
				//NON RESTA CHE UN UPDATE GENERICO
				//messi a zero i campi degli id, per evitare che ci si faccia affidamento (dal front end non dovrebbero arrivare e non devono)
				soggettoRicevuto.setUid(0);
				soggettoRicevuto.setUidSoggettoPadre(0);
				//si usa idQuery che invece e' derivato dai codici, vedi sopra
				soggetto = soggettoDad.aggiornaSoggetto(idQuery,soggettoRicevuto, codiceAmbito, ente, richiedente, req.isAggiornaSoloSedi(),datiOperazioneDto);
			}				
		} else {
			//NON RESTA CHE UN UPDATE GENERICO
			soggetto = soggettoDad.aggiornaSoggetto(idQuery,soggettoRicevuto,codiceAmbito,  ente, richiedente, req.isAggiornaSoloSedi(),datiOperazioneDto);
		}

		// 01/10/2014 : inizio
		// Ricarico il soggetto prima di restituirlo
		boolean includeModifica = true;
		Soggetto soggettoReload = soggettoDad.ricercaSoggetto(codiceAmbito, idEnte, soggetto.getCodiceSoggetto(), includeModifica, true);
		soggettoReload = completaInformazioni(soggettoReload, codiceAmbito, idEnte, soggetto.getCodiceSoggetto(), includeModifica, richiedente,datiOperazioneDto);
		// 01/10/2014 : fine
		/*
		 *  METTE LA LABEL SENZA UNDERSCORE
		 */
		if(soggettoReload.getSediSecondarie()!=null && soggettoReload.getSediSecondarie().size()>0){
			
			for (SedeSecondariaSoggetto sedeSecondariaSoggetto : soggettoReload.getSediSecondarie()) {
				if(sedeSecondariaSoggetto.getStatoOperativoSedeSecondaria().equals(StatoOperativoSedeSecondaria.IN_MODIFICA)){				
					sedeSecondariaSoggetto.setDescrizioneStatoOperativoSedeSecondaria(Constanti.STATO_IN_MODIFICA_no_underscore);
				}
			}
		} 
		res.setSoggettoAggiornato(soggettoReload);
		res.setErrori(null);
		res.setEsito(Esito.SUCCESSO);
	}
	
	
	private boolean controlliPreAggiornamento(Soggetto soggettoRicevuto,Integer idSoggettoOrSoggettoMod,DatiOperazioneDto datiOperazioneDto, OperazioneValidaSoggetto operazioneDaEseguire){
		Richiedente richiedente = req.getRichiedente();
		Ente ente = req.getEnte();
		List<Errore> listaErrori = null;
		if(soggettoRicevuto.getSediSecondarie()!=null && soggettoRicevuto.getSediSecondarie().size()>0){
			//SI EFFETTUANO I CONTROLLI RELATIVI ALLE SEDI SECONDARIE:
		listaErrori = soggettoDad.controlliAggiornamentoSediSecondarie(idSoggettoOrSoggettoMod,soggettoRicevuto.getSediSecondarie(), ente, richiedente,datiOperazioneDto);
			if(listaErrori!=null && listaErrori.size()>0){
				res.setSoggettoAggiornato(null);
				res.setErrori(listaErrori);
				res.setEsito(Esito.FALLIMENTO);
				return false;
			}								
		}
		
		if (OperazioneValidaSoggetto.UNDEFINED.equals(operazioneDaEseguire)) {
			//SI EFFETTUANO I CONTROLLI RELATIVI ALLE MODALITA' DI PAGAMENTO:
			List<ModalitaPagamentoSoggetto> mdp = soggettoRicevuto.getModalitaPagamentoList();
			List<SedeSecondariaSoggetto> sedi = soggettoRicevuto.getSediSecondarie();
			EsitoControlliAggiornamentoMDPDto esitoControlliMDP = soggettoDad.controlliMdp(mdp, sedi, ente,richiedente,idSoggettoOrSoggettoMod);
			if(esitoControlliMDP!=null && esitoControlliMDP.getListaErrori()!=null && esitoControlliMDP.getListaErrori().size()>0){
				res.setSoggettoAggiornato(null);
				res.setErrori(esitoControlliMDP.getListaErrori());
				res.setEsito(Esito.FALLIMENTO);
				return false;
			}
			
			if (esitoControlliMDP.getModalitaPagamentoDaAnnullare()!= null) {
				if (soggettoDad.isModpagCollegataSubdocNonIncassati(esitoControlliMDP.getModalitaPagamentoDaAnnullare()
						.getUid()))
				{
					addErroreFin(ErroreFin.ESISTONO_MOVIMENTI_COLLEGATI, "ANNULLAMENTO", "(documenti)");
					return false;
				}
			}
		}
		
		return true;
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError {	

		Soggetto soggettoInput = req.getSoggetto();

		if(soggettoInput.isControlloSuSoggetto()){
			//2.5.10	Controlli per inserimento/aggiornamento Soggetto

			//		a.	Soggetto.TipoNaturaGiuridica obbligatorio
			checkNotNull(soggettoInput.getTipoSoggetto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Tipo Natura giuridica"));

			//		b.	Se l'attributo Soggetto.residenteEstero = FALSE verificare che sia stato valorizzato Soggetto.codiceFiscale 
			//		e in caso Soggetto.residenteEstero = FALSE verificare che sia valorizzato il CF estero.
			if(StringUtils.isEmpty(soggettoInput.getCodiceFiscale()) && StringUtils.isEmpty(soggettoInput.getCodiceFiscaleEstero())){
				checkNotNull(null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Codice Fiscale"));
			}

			String confronto = "";
			if (soggettoInput.getTipoSoggetto().getCodice() != null) {
				confronto = soggettoInput.getTipoSoggetto().getCodice().trim().toUpperCase();
			} else {
				confronto = (soggettoInput.getTipoSoggetto().getSoggettoTipoCode() != null) ? soggettoInput.getTipoSoggetto().getSoggettoTipoCode().trim().toUpperCase() : "";
			}

			if(Constanti.PERSONA_FISICA.equals(confronto) || Constanti.PERSONA_FISICA_I.equals(confronto)){
				//			c.	Se Soggetto.TipoNaturaGiuridica = 'Persona Fisica' o 'Persona Fisica con PIVA' verificare che siano stati
				//			valorizzati i campi  Soggetto.nome, Soggetto.cognome, Soggetto.sesso, Soggetto.dataNascita, Soggetto.Comune di tipo TipoComune = 'Nascita'.
				checkNotNull(soggettoInput.getCognome(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Cognome"));
				checkNotNull(soggettoInput.getNome(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Nome"));
				checkNotNull(soggettoInput.getComuneNascita(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Comune Nascita"));
				checkNotNull(soggettoInput.getSesso(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Sesso"));
				checkNotNull(soggettoInput.getDataNascita(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Data Nascita"));
			} else if(Constanti.PERSONA_GIURIDICA.equals(confronto) || Constanti.PERSONA_GIURIDICA_I.equals(confronto)){
				//			d.	Se Soggetto.TipoNaturaGiuridica = 'Persona Giuridica o 'Persona Giuridica senza PIVA' verificare che siano stati valorizzati
				//			i campi  Soggetto.ragioneSociale e Soggetto.NaturaGiuridicaSoggetto; 
				checkNotNull(soggettoInput.getDenominazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Ragione Sociale"));
				checkNotNull(soggettoInput.getNaturaGiuridicaSoggetto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Natura giuridica"));
			}

			if(Constanti.PERSONA_FISICA_I.equals(confronto) || Constanti.PERSONA_GIURIDICA_I.equals(confronto)){
				//			e.	Se Soggetto.TipoNaturaGiuridica = 'Persona Fisica con PIVA'  o 'Persona Giuridica' verificare che sia stato valorizzato Soggetto.partitaIVA
				checkNotNull(soggettoInput.getPartitaIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Partita iva"));
			}

			//		f.	Deve essere valorizzato un Indirizzo del Soggetto che ha l'attributo Indirizzo.principale = TRUE
			if(soggettoInput.getIndirizzi()==null || soggettoInput.getIndirizzi().size()==0){
				checkNotNull(null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Indirizzi"));
			} else {
				boolean trovatoPrincipale = false;
				for(IndirizzoSoggetto iterato :soggettoInput.getIndirizzi()){
					if(iterato!=null && Boolean.valueOf(iterato.getPrincipale())){
						trovatoPrincipale = true;
					}
				}
				if(!trovatoPrincipale){
					checkNotNull(null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Indirizzi (inserire almeno un indirizzo principale)"));
				}
			}
		}			 
	}
}