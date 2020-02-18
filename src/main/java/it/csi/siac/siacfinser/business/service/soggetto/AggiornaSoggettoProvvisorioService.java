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

import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.StringUtils;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSoggettoProvvisorio;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSoggettoProvvisorioResponse;
import it.csi.siac.siacfinser.integration.dad.SoggettoFinDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto.StatoOperativoSedeSecondaria;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaSoggettoProvvisorioService extends AbstractSoggettoService<AggiornaSoggettoProvvisorio, AggiornaSoggettoProvvisorioResponse> {
	
	@Autowired
	SoggettoFinDad soggettoDad;
	
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
	public AggiornaSoggettoProvvisorioResponse executeService(
			AggiornaSoggettoProvvisorio serviceRequest) {
		// SIAC-6847
		log.debug("WAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA EXECUTESERVICE REQUEST", JAXBUtility.marshall(serviceRequest));
		return super.executeService(serviceRequest);
	}
	
	private boolean controlloTipoSogg(String codice){
		if(!(codice.equalsIgnoreCase("PGI") || codice.equalsIgnoreCase("PG"))){
		return true	;
		}else{
			return false;
		}
	}
	
	@Override
	public void execute() {
		final String methodName = "AggiornaSoggettoProvvisorioService - execute()";
		
		//1. Leggiamo i dati dalla request:
		Soggetto soggettoRicevuto = req.getSoggetto();
		Richiedente richiedente = req.getRichiedente();
		Ente ente = req.getEnte();
		
		String codiceAmbito = req.getCodificaAmbito();
		
		if (codiceAmbito == null)
			codiceAmbito = Constanti.AMBITO_FIN;

		//2. Ricarichiamo il soggetto per confronto dei dati prima della modifica:
		Soggetto soggettoMaster = soggettoDad.ricercaSoggetto(Constanti.AMBITO_FIN, req.getEnte().getUid(), soggettoRicevuto.getCodiceSoggetto(),true, true);
		Integer idSoggetto = soggettoMaster.getUid();
		
		DatiOperazioneDto datiOperazioneDto = commonDad.inizializzaDatiOperazione(ente, richiedente, Operazione.MODIFICA, codiceAmbito, null);
		
		//3. Invochiamo il metodo controlliDiMerito dove sono implementati i vari controlli definiti in analisi:
		boolean esitoControlli = controlliDiMerito(soggettoMaster,datiOperazioneDto);
		if(!esitoControlli){
			return;
		}
		
		//4. Dobbiamo capire se c'e' gia' il soggetto in modifica invocando la ricerca soggeto in modifica:
		Soggetto soggettoDaVerificare = soggettoDad.ricercaSoggettoInModifica(req.getEnte().getUid(), soggettoRicevuto.getCodiceSoggetto());
		
		/*
		 *  DISCRIMINANTE PER LANCIARE INSERT O UPDATE
		 */
		if(null==soggettoDaVerificare){
			//nel caso in cui non c'e' ancora nessun soggetto in modifica, si effettua un inserimento nelle tabelle _mod
			soggettoDad.inserisciSoggettoMod(idSoggetto,soggettoRicevuto, richiedente, codiceAmbito, ente, req.isAggiornaSoloSedi(),datiOperazioneDto);
		} else {
			//nel caso in cui esiste gia' un soggetto in modifica, si effettua l'update di tale occorrenza
			soggettoDad.aggiornaSoggettoMod(idSoggetto,soggettoRicevuto, richiedente, codiceAmbito, ente, req.isAggiornaSoloSedi(), datiOperazioneDto);
		}
		
		// Ricarico il soggetto prima di restituirlo (cosi il chiamante puo' risparmiarsi di richiamare il servizio di ricerca per chiave)
		boolean includeModifica = true;
		Soggetto soggettoReload = soggettoDad.ricercaSoggetto(Constanti.AMBITO_FIN, ente.getUid(), soggettoRicevuto.getCodiceSoggetto(), includeModifica, true);
		//vengono vestiti dei dati accessori:
		soggettoReload = completaInformazioni(soggettoReload, codiceAmbito, ente.getUid(), soggettoRicevuto.getCodiceSoggetto(), includeModifica, richiedente,datiOperazioneDto);
		
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
		//Compongo la response:
		res.setSoggettoAggiornato(soggettoReload);
		res.setErrori(null);
		res.setEsito(Esito.SUCCESSO);
	}
	
	
	private boolean controlliDiMerito(Soggetto soggettoMaster,DatiOperazioneDto datiOperazioneDto){
		
		// 2.5.4 Controllo di duplicazione codice
		// Viene controllata l'esistenza in archivio di un soggetto con  SOGGETTO.StatoOperativoAnagrafica <> ANNULLATO il cui  codice fiscale e eventuale  partita iva corrispondo a 
		// quelli passati a parametro.
		// Se presente il Codice Fiscale, ricerca l'esistenza di un soggetto non annullato (con  SOGGETTO. StatoOperativoAnagrafica <> ANNULLATO ) con Codice fiscale indicato.
		// Se presente la partita IVA, ricerca l'esistenza di un soggetto valido con la PIVA uguale a quella indicata.
		// Se almeno una delle due ricerche restituisce un soggetto e se i valori del Soggetto.codice  sono diversi da quello del Soggetto che si sta trattando 
		// (vale per i casi di aggiornamento del soggetto),  restituire un messaggio bloccante <FIN_ERR_0025, Soggetto esistente 
		// (codice =indicare se presente COD. FISC., P.IVA o entrambi)
		
		
		Soggetto soggettoRicevuto = req.getSoggetto();
		Richiedente richiedente = req.getRichiedente();
		Ente ente = req.getEnte();
		Integer idSoggetto = soggettoMaster.getUid();
		
		//
		if(controlloTipoSogg(soggettoRicevuto.getTipoSoggetto().getCodice())){
			soggettoRicevuto.setNome(soggettoRicevuto.getNome().toUpperCase());
			soggettoRicevuto.setCognome(soggettoRicevuto.getCognome().toUpperCase());
		}
		
		// Verifico se il codice_fiscale / partita_iva e cambiato rispetto al soggetto "master" sulla tabella siac_t_soggetto
		//Codice fiscale e partita iva vanno controllati solo se sono diversi e valorizzati:
		boolean controllaCodiceFiscale = StringUtils.valorizzatiAndDiversi(soggettoRicevuto.getCodiceFiscale(), soggettoMaster.getCodiceFiscale());
		boolean controllaPartitaIva = StringUtils.valorizzatiAndDiversi(soggettoRicevuto.getPartitaIva(), soggettoMaster.getPartitaIva());
		
		
		boolean[] listaControlloDuplicazione = soggettoDad.controlloDuplicazioneCodice(soggettoRicevuto.getCodiceSoggetto(), soggettoRicevuto.getCodiceFiscale(), soggettoRicevuto.getPartitaIva(), ente);
		if (listaControlloDuplicazione != null && listaControlloDuplicazione.length > 0) {
			
			if ((listaControlloDuplicazione[0] || listaControlloDuplicazione[1]) && (controllaCodiceFiscale || controllaPartitaIva)) {
				String supportErrore = "";
				if (listaControlloDuplicazione[0] && listaControlloDuplicazione[1] && controllaCodiceFiscale && controllaPartitaIva) {
					supportErrore = "Codice Fiscale e Partita Iva";
				} else if (listaControlloDuplicazione[0] && controllaCodiceFiscale) {
					supportErrore = "Codice Fiscale";
				} else {
					supportErrore = "Partita Iva";
				}
				addErroreFin(ErroreFin.SOGGETTO_ESISTENTE, supportErrore);
				return false;
			}
			
		}	
			
		boolean controlloCompatibilitaStatoEntita = soggettoDad.controlloCompatibilitaStatoSoggettoProvvisorio(req.getRichiedente().getAccount().getNome(),idSoggetto);
		if (controlloCompatibilitaStatoEntita == false) {
			addErroreFin(ErroreFin.OPERAZIONE_INCOMPATIBILE);
			return false;
		} 
				
				
		boolean[] listaControlloSedi = soggettoDad.controlloSediSecondarieSoggettoProvvisorio(idSoggetto, req.getEnte(), req.getRichiedente().getAccount().getNome());
		if (listaControlloSedi != null && listaControlloSedi.length > 0 ) {
			boolean skipForTest = false;
			if (listaControlloSedi[0] && !listaControlloSedi[1]) {
				skipForTest = true;
			} 
			if (!skipForTest && (listaControlloSedi[0] || listaControlloSedi[1])) {
				String supportErrore = "";
				if (listaControlloSedi[0] && listaControlloSedi[1]) {
					supportErrore = "Operazione incompatibile con lo stato di una sede e Sede gi&agrave; presente in archivio";
				} else if (listaControlloSedi[0]) {
					supportErrore = "Operazione incompatibile con lo stato di una sede ";
				} else {
					supportErrore = "Sede gia' presente in archivio";
				}
				addErroreFin(ErroreFin.SOGGETTO_ESISTENTE, supportErrore);
				return false;
			} 
		}
		
		List<Errore> listaErrori = soggettoDad.controlliAggiornamentoSediSecondarie(idSoggetto,soggettoRicevuto.getSediSecondarie(), ente, richiedente,datiOperazioneDto);
		if(listaErrori!=null && listaErrori.size()>0){
			res.setSoggettoAggiornato(null);
			res.setErrori(listaErrori);
			res.setEsito(Esito.FALLIMENTO);
			return false;
		}
		
		return true;
	}
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		
		Soggetto soggettoInput = req.getSoggetto();
		Ente ente = req.getEnte();
		
		if(soggettoInput == null){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Soggetto"));			
		} else if(ente == null){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Ente"));
		}
		
		// Soggetto.TipoNaturaGiuridica obbligatorio
		checkNotNull(soggettoInput.getTipoSoggetto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Tipo Natura giuridica"));
		
		// Se Soggetto.ResidenteEstero = 'N' Soggetto.CodiceFiscale e' obbligatorio
		if(StringUtils.isEmpty(soggettoInput.getCodiceFiscale()) && null!=soggettoInput.getResidenteEsteroStringa() && soggettoInput.getResidenteEsteroStringa().equalsIgnoreCase(Constanti.FALSE)){
			checkNotNull(null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Codice Fiscale"));
		}

		// Se Soggetto.ResidenteEstero = 'S' Soggetto.CodiceFiscaleEstero e' obbligatorio
		if(StringUtils.isEmpty(soggettoInput.getCodiceFiscaleEstero()) && null!=soggettoInput.getResidenteEsteroStringa() && soggettoInput.getResidenteEsteroStringa().equalsIgnoreCase(Constanti.TRUE)){
			checkNotNull(null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Codice Fiscale Estero"));
		}
		
		String confronto = soggettoInput.getTipoSoggetto().getSoggettoTipoCode().trim().toUpperCase();
		if(Constanti.PERSONA_FISICA.equals(confronto) || Constanti.PERSONA_FISICA_I.equals(confronto)){
			// Se Soggetto.TipoNaturaGiuridica = 'Persona Fisica' o 'Persona Fisica con PIVA'
			// Soggetto.nome, Soggetto.cognome, Soggetto.sesso, Soggetto.dataNascita sono obbligatori
			// Soggetto.TipoComune deve essere di tipo 'Nascita'
			checkNotNull(soggettoInput.getCognome(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Cognome"));
			checkNotNull(soggettoInput.getNome(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Nome"));
			checkNotNull(soggettoInput.getComuneNascita(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Comune Nascita"));
			checkNotNull(soggettoInput.getSesso(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Sesso"));
			checkNotNull(soggettoInput.getDataNascita(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Data Nascita"));
		} else if(Constanti.PERSONA_GIURIDICA.equals(confronto) || Constanti.PERSONA_GIURIDICA_I.equals(confronto)){
			// Se Soggetto.TipoNaturaGiuridica = 'Persona Giuridica' o 'Persona Giuridica senza PIVA'
			// Soggetto.RagioneSociale e Soggetto.NaturaGiuridicaSoggetto sono obbligatori 
			checkNotNull(soggettoInput.getDenominazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Ragione Sociale"));
			checkNotNull(soggettoInput.getNaturaGiuridicaSoggetto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Natura giuridica"));
		}
		
		// Se Soggetto.TipoNaturaGiuridica = 'Persona Fisica con PIVA'  o 'Persona Giuridica'
		// Soggetto.partitaIVA e' obbligatorio
		if(Constanti.PERSONA_FISICA_I.equals(confronto) || Constanti.PERSONA_GIURIDICA.equals(confronto)){
			checkNotNull(soggettoInput.getPartitaIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Partita iva"));
		}
		
		// Deve esserci almeno un indirizzo
		if(soggettoInput.getIndirizzi()==null || soggettoInput.getIndirizzi().size()==0){
			checkNotNull(null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Indirizzi"));
		} else {
			// Deve esserci almeno un indirizzo con Indirizzo.Principale = TRUE
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
