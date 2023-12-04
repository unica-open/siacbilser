/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.ordinativo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.CommonUtil;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.StringUtilsFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceOrdinativoPagamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceOrdinativoPagamentoResponse;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.integration.dad.ImpegnoOttimizzatoDad;
import it.csi.siac.siacfinser.integration.dad.LiquidazioneDad;
import it.csi.siac.siacfinser.integration.dad.OrdinativoPagamentoDad;
import it.csi.siac.siacfinser.integration.dad.SoggettoFinDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoControlliDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OrdinativoInInserimentoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.SiopePlusDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.SubOrdinativoInModificaInfoDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione.StatoOperativoLiquidazione;
import it.csi.siac.siacfinser.model.messaggio.MessaggioFin;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.RicercaLiquidazioneK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;
import it.csi.siac.siacgenser.model.TipoCollegamento;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceOrdinativoPagamentoService extends AbstractInserisceAggiornaAnnullaOrdinativoPagamentoService<InserisceOrdinativoPagamento, InserisceOrdinativoPagamentoResponse> {

	@Autowired
	SoggettoFinDad soggettoDad;

	@Autowired
	OrdinativoPagamentoDad ordinativoPagamentoDad; 
	
	@Autowired
	ImpegnoOttimizzatoDad impegnoOttimizzatoDad;
	
	@Autowired
	private LiquidazioneDad liquidazioneDad;
	
	@Autowired
	CommonDad commonDad;
	
	
	@Override
	@Transactional
	public InserisceOrdinativoPagamentoResponse executeService(InserisceOrdinativoPagamento serviceRequest) {
		return super.executeService(serviceRequest);
	}	

	@Override
	protected void init() {
		final String methodName="InserisceOrdinativoPagamentoService : init()";
		log.debug(methodName, " - Begin");
	}	

	@Override
	public void execute() {
		String methodName = "InserisceOrdinativoPagamentoService - execute()";
		log.debug(methodName, " - Begin");
		
		//1. Vengono letti i valori ricevuti in input dalla request
		Richiedente richiedente = req.getRichiedente();
		ente = req.getEnte();
		setBilancio(req.getBilancio());
						
		OrdinativoPagamento ordinativoDiPagamento = req.getOrdinativoPagamento();
		
		//2. Si inizializza l'oggetto DatiOperazioneDto, dto di comodo generico che verra' passato tra i metodi
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.INSERIMENTO, bilancio.getAnno());

		//3. Si inizializza l'oggetto OrdinativoInInserimentoInfoDto, dto di comodo specifico di questo servizio
		OrdinativoInInserimentoInfoDto ordinativoInInserimentoInfoDto = new OrdinativoInInserimentoInfoDto();
		ordinativoInInserimentoInfoDto.setBilancio(req.getBilancio());
		ordinativoInInserimentoInfoDto.setOrdinativo(ordinativoDiPagamento);
		//SIAC-8589
		ricaricaCapitoloByLiquidazione(ordinativoDiPagamento, datiOperazione);
		
		//4. Vengono eseguiti i vari controlli di merito previsti da analisi:
		boolean verificaCondizioniInserimento = verificaCondizioniPerInserimentoOrdinativoDiPagamento(datiOperazione);
		if(!verificaCondizioniInserimento){
			return;
		}
		
		//SIAC-8017-CMTO
		impostaDatiPerContoVincolato(ordinativoDiPagamento, datiOperazione);
		
		//5. inserimento ordinativo di pagamento (si invoca il metodo "core" rispetto all'operazione di inserimento di un nuovo ordinativo):
		OrdinativoPagamento ordinativoPagamentoInserito = (OrdinativoPagamento) ordinativoPagamentoDad.inserisciOrdinativoPagamento(ordinativoDiPagamento, ordinativoInInserimentoInfoDto, datiOperazione, req.isUsaDatiLoginOrig());

		if(null!=ordinativoPagamentoInserito){
			
			//1. Chiamo il metodo valutaSubOrdinativi con idOrdinativo = null perche' voglio ottenere
			//	solo i subordinativi "da inserire", dato che siamo nel servizio di inserisce ordinativo saranno tutti "da inserire"
			//  infoSub e' quindi un dto di comodo per la doppia gestione
			SubOrdinativoInModificaInfoDto infoSub = ordinativoPagamentoDad.valutaSubOrdinativi(ordinativoDiPagamento.getElencoSubOrdinativiDiPagamento(), null, datiOperazione,
					bilancio,richiedente, CostantiFin.ORDINATIVO_TIPO_PAGAMENTO, ente);
			
			//2. Routine di doppia gestione (dentro viene eseguita solo se siamo in doppia gestione):
			EsitoControlliDto resDg = operazioniPerDoppiaGestione(ordinativoDiPagamento, bilancio, richiedente, ente, datiOperazione,infoSub,Operazione.INSERIMENTO);
			
			if(!isEmpty(resDg.getListaErrori())){
				//errori in doppia gestione:
				//Costruzione response esito negativo:
				res.setErrori(resDg.getListaErrori());
				res.setOrdinativoPagamentoInserito(null);
				res.setEsito(Esito.FALLIMENTO);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return;
			}
			
			//DICEMBRE 2017 introdotta la possibilita' di escludere le registrazioni,
			//il chiamante (l'esigenza e' nata con il servizio di reintroito) che deve
			//orchestrare in maniera atomica piu servizi che possono fallire e metterci tempo si occupera'
			//di richiamare i gen solo dopo essere certo che siano terminati tutti correttamente
			if(req.isRegistraGen()){
				
				String codCapitoloSanitario = ordinativoDiPagamento.getCodCapitoloSanitarioSpesa()!=null ? ordinativoDiPagamento.getCodCapitoloSanitarioSpesa(): "";
				
				//innesto fin-gen
				gestisciRegistrazioneGENPerOrdinativo(ordinativoPagamentoInserito, impegno , TipoCollegamento.ORDINATIVO_PAGAMENTO, false, impegno.isFlagCassaEconomale(), codCapitoloSanitario);
				
			}

			// 3. Esito ok, costruzione response
			res.setOrdinativoPagamentoInserito(ordinativoPagamentoInserito);
			res.setEsito(Esito.SUCCESSO);
			return;
		} else {
			//Esito KO
			res.setOrdinativoPagamentoInserito(null);
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
	}
	
	//SIAC-8589
	private void ricaricaCapitoloByLiquidazione(OrdinativoPagamento ordinativoDiPagamento, DatiOperazioneDto datiOperazione) {
		final String methodName ="ricaricaCapitoloByLiquidazione";
		Liquidazione liq = null;
		for (SubOrdinativoPagamento sub : ordinativoDiPagamento.getElencoSubOrdinativiDiPagamento()) {
			liq = sub.getLiquidazione();
			break;
		}
		if(liq == null) {
			StringBuilder sb1 = new StringBuilder()
					.append("Impossibile reperire una liquidazione per l'ordinativo ")
					.append(ordinativoDiPagamento.getAnno() != null? ordinativoDiPagamento.getAnno() : "null" )
					.append("/")
					.append(ordinativoDiPagamento.getNumero() != null? ordinativoDiPagamento.getNumero() : "null");
			log.error(methodName, sb1.toString());
		}
		Integer idCap = liquidazioneDad.caricaIdCapitoloAssociatoALiquidazione(liq, datiOperazione);
		if(idCap == null || idCap.intValue() == 0) {
			StringBuilder sb2 = new StringBuilder()
					.append("Impossibile reperire un capitolo associato alla liquidazione ")
					.append(liq.getAnnoLiquidazione() != null? liq.getAnnoLiquidazione() : "null" )
					.append("/")
					.append(liq.getNumeroLiquidazione() != null? liq.getNumeroLiquidazione() : "null" )
					.append(" per l'ordinativo ")
					.append(ordinativoDiPagamento.getAnno() != null? ordinativoDiPagamento.getAnno() : "null" )
					.append("/")
					.append(ordinativoDiPagamento.getNumero() != null? ordinativoDiPagamento.getNumero() : "null");
			log.error(methodName, sb2.toString());
			return;
		}
		CapitoloUscitaGestione capitoloCaricato = caricaCapitoloModulare(req.getRichiedente(), idCap, bilancio);
		ordinativoDiPagamento.setCapitoloUscitaGestione(capitoloCaricato);
	}

	private void impostaDatiPerContoVincolato(OrdinativoPagamento ordinativoDiPagamento,DatiOperazioneDto datiOperazione) {
		final String methodName = "checkCapienzaContoVincolato";
		ContoTesoreria contoTesoreriaOrdinativo = ordinativoDiPagamento.getContoTesoreria();
		CapitoloUscitaGestione capitoloUscitaGestione = ordinativoDiPagamento.getCapitoloUscitaGestione();
		
		if(!isCondizioniGestioneContoVincolatoSoddisfatte(contoTesoreriaOrdinativo, capitoloUscitaGestione)) {
			return;
		}
		
		BigDecimal disponibilitaPagareSottoConto = caricaDisponibilitaPagareSottoCointoVincolato(datiOperazione,contoTesoreriaOrdinativo, capitoloUscitaGestione);
		BigDecimal importoOrdinativo = extractImportoOrdinativo(ordinativoDiPagamento);
		
		log.info(methodName, "La disponibilita a pagare del conto " + contoTesoreriaOrdinativo.getCodice() + " sul capitolo  " + capitoloUscitaGestione.getNumeroCapitolo() + " e' di " + disponibilitaPagareSottoConto  + ", mentre l'importo ordinativo di " + importoOrdinativo);
		if(importoOrdinativo != null && importoOrdinativo.compareTo(disponibilitaPagareSottoConto) <= 0) {
			log.info(methodName, "disponibilita' a pagare sul sottoconto sufficiente a procedere.");
			return;
		}
		log.info(methodName, "disponibilita' a pagare sul sottoconto insufficiente. Utilizzo il conto per ripianamento invece che quello indicato.");
		
		ordinativoDiPagamento.setContoTesoreriaSenzaCapienza(contoTesoreriaOrdinativo);
		
		ContoTesoreria contoRipianamento = ordinativoPagamentoDad.caricaContoTesoreriaPerRipianamento(ente);
		ordinativoDiPagamento.setContoTesoreria(contoRipianamento);
		
		String disp = CommonUtil.convertiBigDecimalToImporto(disponibilitaPagareSottoConto);
		String impord = CommonUtil.convertiBigDecimalToImporto(importoOrdinativo);
		res.addMessaggio(MessaggioFin.SOSTITUITO_CONTO_PER_MANCATA_CAPIENZA.getMessaggio(contoTesoreriaOrdinativo.getCodice(), disp, impord, contoRipianamento.getCodice()));
		
	}

	private boolean verificaCondizioniPerInserimentoOrdinativoDiPagamento(DatiOperazioneDto datiOperazione){
		
		String methodName = "verificaCondizioniPerInserimentoOrdinativoDiPagamento";
		Ente ente = req.getEnte();
		OrdinativoPagamento ordinativoDiPagamento = req.getOrdinativoPagamento();
		Soggetto soggetto = ordinativoDiPagamento.getSoggetto();
		AttoAmministrativo attoAmministrativo = ordinativoDiPagamento.getAttoAmministrativo();
		Richiedente richiedente = req.getRichiedente();
		CapitoloUscitaGestione capitoloUscitaGestione = ordinativoDiPagamento.getCapitoloUscitaGestione();
		Bilancio bilancio = req.getBilancio();
		
		// Soggetto: Verifica che il soggetto sia VALIDO o SOSPESO
		Soggetto soggettoCheck = soggettoDad.ricercaSoggetto(CostantiFin.AMBITO_FIN, ente.getUid(), soggetto.getCodiceSoggetto(), false, true);
		if(null==soggettoCheck){
			addErroreFin(ErroreFin.SOGGETTO_NON_VALIDO);
			return false;
		} else if (!soggettoCheck.getStatoOperativo().name().equalsIgnoreCase(StatoOperativoAnagrafica.VALIDO.name()) && 
				   !soggettoCheck.getStatoOperativo().name().equalsIgnoreCase(StatoOperativoAnagrafica.SOSPESO.name())){
				addErroreFin(ErroreFin.SOGGETTO_NON_VALIDO);
				return false;
		}

		// Atto Amministrativo: Verifica che il PROVVEDIMENTO sia univoco e che il suo stato non sia ANNULLATO
		
		//RicercaProvvedimentoResponse ricercaProvvedimentoResponse = ricercaProvvedimento(richiedente, attoAmministrativo);
		//AttoAmministrativo attoAmministrativoEstratto = new AttoAmministrativo();
		
		//MARZO 2017 - Passo da ricercaProvvedimento a estraiAttoAmministrativo che usa 
		//sempre ricercaProvvedimento ma non fa confusione sulla struttura amministrativa:
		AttoAmministrativo attoAmministrativoEstratto = estraiAttoAmministrativo(richiedente, attoAmministrativo);
		if(attoAmministrativoEstratto!=null) {
			if(!attoAmministrativoEstratto.getStatoOperativo().equalsIgnoreCase(StatoOperativoAtti.DEFINITIVO.getDescrizione())){
				addErroreFin(ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO,"inserimento ordinativo di pagamento", "definitivo");
				return false;
			}
			ordinativoDiPagamento.setAttoAmministrativo(attoAmministrativoEstratto);
		} else {
			addErroreCore(ErroreCore.ENTITA_NON_TROVATA,  "PROVVEDIMENTO", attoAmministrativo.getAnno() + " / " + attoAmministrativo.getNumero());
			return false;
		}
		
		/*
		if(ricercaProvvedimentoResponse.isFallimento()){
			List<Errore> listaErroriAttoAmministrativo = ricercaProvvedimentoResponse.getErrori();			
			res.setErrori(listaErroriAttoAmministrativo);
			res.setOrdinativoPagamentoInserito(null);
			res.setEsito(Esito.FALLIMENTO);
			return false;
		} else {
			if(ricercaProvvedimentoResponse.getListaAttiAmministrativi()!=null) {
				
				List<AttoAmministrativo> attiFiltratuPerStatoNonAnnullato = new ArrayList<AttoAmministrativo>();
				
				for (AttoAmministrativo atto : ricercaProvvedimentoResponse.getListaAttiAmministrativi()) {
					
					if(!atto.getStatoOperativo().equals(StatoOperativoAtti.ANNULLATO.getDescrizione())){
						attiFiltratuPerStatoNonAnnullato.add(atto);
					}
					
				}
				
				if(attiFiltratuPerStatoNonAnnullato.size() == 1){
					attoAmministrativoEstratto = attiFiltratuPerStatoNonAnnullato.get(0);
					
					if(!attoAmministrativoEstratto.getStatoOperativo().equalsIgnoreCase(StatoOperativoAtti.DEFINITIVO.getDescrizione())){
						addErroreFin(ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO,"inserimento ordinativo di incasso", "definitivo");
						return false;
					}
					ordinativoDiPagamento.setAttoAmministrativo(attoAmministrativoEstratto);
				
				} else {
					addErroreFin(ErroreFin.OGGETTO_NON_UNIVOCO, "PROVVEDIMENTO");
					return false;
				}
			} else {
				addErroreCore(ErroreCore.ENTITA_NON_TROVATA,  "PROVVEDIMENTO", attoAmministrativo.getAnno() + " / " + attoAmministrativo.getNumero());
				return false;
			}
		}*/

		BigDecimal sommaImportiQuote = BigDecimal.ZERO;
		
		boolean tuttiImpegniFrazionabili = true;
		int annoMovimenti = 0;
		
		//lista di comodo per eseguire ulteriori controlli:
		List<Liquidazione> liquidazioniDelleQuote = new ArrayList<Liquidazione>();
		//
		
		if(null!=ordinativoDiPagamento && null!=ordinativoDiPagamento.getElencoSubOrdinativiDiPagamento() && ordinativoDiPagamento.getElencoSubOrdinativiDiPagamento().size() > 0){
			for(SubOrdinativoPagamento subOrdinativoPagamento : ordinativoDiPagamento.getElencoSubOrdinativiDiPagamento()){
				
				sommaImportiQuote = sommaImportiQuote.add(subOrdinativoPagamento.getImportoAttuale());

				RicercaLiquidazioneK ricercaLiquidazioneK = new RicercaLiquidazioneK();
				ricercaLiquidazioneK.setAnnoEsercizio(bilancio.getAnno());
				
				Liquidazione liquidazioneIn = new Liquidazione();
				liquidazioneIn.setAnnoLiquidazione( subOrdinativoPagamento.getLiquidazione().getAnnoLiquidazione());
				liquidazioneIn.setNumeroLiquidazione( subOrdinativoPagamento.getLiquidazione().getNumeroLiquidazione());
				ricercaLiquidazioneK.setLiquidazione(liquidazioneIn);
				
				Liquidazione liquidazione = leggiLiquidazioneSubOrdinativo(ente, richiedente, ricercaLiquidazioneK);
				if(null!=liquidazione){
					
					//aggiungo la liqudazione alla lista di comodo:
					liquidazioniDelleQuote.add(liquidazione);
					//
					
					if(liquidazione.getStatoOperativoLiquidazione().name().equalsIgnoreCase(StatoOperativoLiquidazione.ANNULLATO.name())){
						addErroreCore(ErroreCore.ENTITA_NON_TROVATA, "LIQUIDAZIONE", subOrdinativoPagamento.getLiquidazione().getAnnoLiquidazione() + " / " + subOrdinativoPagamento.getLiquidazione().getNumeroLiquidazione());
						return false;
					} else {
						if(subOrdinativoPagamento.getImportoAttuale().compareTo(liquidazione.getDisponibilitaPagare()) > 0){
							addErroreFin(ErroreFin.DISPONIBILITA_LIQUIDAZIONE_INSUFFICIENTE, "LIQUIDAZIONE");
							return false;
						}
					}
					
					if(!liquidazione.getImpegno().isFlagFrazionabile()){
						tuttiImpegniFrazionabili = false;
					}
					
					// imposto l'impegno utile poi all'innesto per la lettura del pdc, prendo il primo anche se l'ordinativo viene emesso su impegni diversi
					if(getImpegno() ==null){
						super.impegno = liquidazione.getImpegno();
						annoMovimenti = liquidazione.getImpegno().getAnnoMovimento();
					}
					
					// CR SIAC-3695: controllare per ogni sub o per l'impegno (se non ha sub) che abbia disponibilità a liquidare
					// todo... richiamare la nuova operazione che restituisce la disponibilità a liquidare 
					BigDecimal disponibilitaALiquidareMovimentoGestione = BigDecimal.ZERO;
					String stato = null;
					
					boolean impegnoInStatoDefinitivo = liquidazione.getImpegno().getStatoOperativoMovimentoGestioneSpesa().equalsIgnoreCase(CostantiFin.MOVGEST_STATO_DEFINITIVO);
					
					if(!impegnoInStatoDefinitivo && liquidazione.getImpegno().getElencoSubImpegni()!=null &&
							!liquidazione.getImpegno().getElencoSubImpegni().isEmpty()){
						SubImpegno sub = liquidazione.getImpegno().getElencoSubImpegni().get(0);
						
						//FIX PER  	SIAC-3907, ricarico lo stato che era null
						//stato = sub.getStatoOperativoMovimentoGestioneSpesa();
						stato = impegnoOttimizzatoDad.ottieniStatoCode(null, sub, datiOperazione);
						//
						
						disponibilitaALiquidareMovimentoGestione = impegnoOttimizzatoDad.ottieniDisponibilitaLiquidare(null, sub);
							
						log.debug(methodName, "stato movimento numero [ "+ sub.getNumeroBigDecimal() +"] : " + stato );
						
					}else{
						Impegno impegno = liquidazione.getImpegno();
						
						//FIX PER  	SIAC-3907, ricarico lo stato che era null
						//stato = impegno.getStatoOperativoMovimentoGestioneSpesa();
						stato = impegnoOttimizzatoDad.ottieniStatoCode(impegno, null, datiOperazione);
						//
						
						disponibilitaALiquidareMovimentoGestione = impegnoOttimizzatoDad.ottieniDisponibilitaLiquidare(impegno, null);
						
						log.debug(methodName, "stato movimento numero [ "+ impegno.getNumeroBigDecimal() +"] : " + stato );
					}
					
					
					
					log.debug(methodName,"Calcolata disponibilitaALiquidareMovimentoGestione  : " + disponibilitaALiquidareMovimentoGestione);
					
					if(!stato.equalsIgnoreCase(CostantiFin.MOVGEST_STATO_ANNULLATO)){
						if(disponibilitaALiquidareMovimentoGestione.compareTo(BigDecimal.ZERO) < 0){
							addErroreFin(ErroreFin.DISPONIBILITA_INSUFFICIENTE_MOVIMENTO_GESTIONE, "INSERISCI ORDINATIVO DI PAGAMENTO");
							return false;
						}
					}else{
						String tipoEntita =  subOrdinativoPagamento.getLiquidazione().getSubImpegno()!=null ? "SubImpegno" : "Impegno";
						addErroreFin(ErroreFin.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA, tipoEntita, "ANNULLATO");
						return false;
					}
					
					
					
				} else {
					addErroreCore(ErroreCore.ENTITA_NON_TROVATA, "LIQUIDAZIONE", subOrdinativoPagamento.getLiquidazione().getAnnoLiquidazione() + " / " + subOrdinativoPagamento.getLiquidazione().getNumeroLiquidazione());
					return false;
				}
			}
		}
		
		// Verifico disponibilita' di cassa
		
		// 	SIAC-5063 (05-07-2017)
		//Non uso piu questo dato perche' arrivando dal chiamante non e' attandibile 
		//specialmente in chiamate sequenziali tipo l'emettitore
		//List<ImportiCapitoloUG>  listaImportiCapitoloUG = capitoloUscitaGestione.getListaImportiCapitolo();
		
		//Al suo posto uso questi importi ricaricati sul momento sfruttando il servizio ricercaDettaglioModulareCapitoloUscitaGestione:
		int chiaveCapitolo = capitoloUscitaGestione.getUid();
		//SIAC-8589, il caricamento e' stato spostato precedentemente
		List<ImportiCapitoloUG>  listaImportiCapitoloUG = new ArrayList<ImportiCapitoloUG>();
		
		if(capitoloUscitaGestione.getImportiCapitoloUG()!=null){
			listaImportiCapitoloUG.add(capitoloUscitaGestione.getImportiCapitoloUG());
		}
		
		if(!it.csi.siac.siacfinser.StringUtilsFin.isEmpty(capitoloUscitaGestione.getListaImportiCapitoloUG())){
			listaImportiCapitoloUG.addAll(capitoloUscitaGestione.getListaImportiCapitoloUG());
		}
		
		if(null!=listaImportiCapitoloUG && listaImportiCapitoloUG.size() > 0) {
			for(ImportiCapitoloUG importoCapitoloUG : listaImportiCapitoloUG) {
				if(importoCapitoloUG.getAnnoCompetenza().intValue() == bilancio.getAnno()){
					if(sommaImportiQuote.compareTo(importoCapitoloUG.getDisponibilitaPagare()) > 0){
						addErroreFin(ErroreFin.DISPONIBILITA_DI_CASSA_INSUFFICIENTE, "CAPITOLO");
						return false;
					}
				}
			}				
		}
		
		if(ordinativoPagamentoDad.isBilancioInStato(bilancio, CostantiFin.BIL_FASE_OPERATIVA_ESERCIZIO_PROVVISORIO, datiOperazione) &&
				annoMovimenti==bilancio.getAnno() &&
				tuttiImpegniFrazionabili){
			BigDecimal dispDodicesimi = ordinativoPagamentoDad.calcolaDisponibilitaAPagarePerDodicesimi(capitoloUscitaGestione.getUid());
			if(sommaImportiQuote.longValue()>dispDodicesimi.longValue()){
				addErroreFin(ErroreFin.DISPONIBILITA_INSUFFICIENTE,"Ordinativo","dodicesimi");
				return false;
			}
		}
		
		//CONTROLLI DATI SIOPE PLUS:
		List<Errore> errSiopePlus = ordinativoPagamentoDad.controlliSiopePlusPerOrdPag(ordinativoDiPagamento, datiOperazione);
		if(!StringUtilsFin.isEmpty(errSiopePlus)){
			res.setErrori(errSiopePlus);
			res.setEsito(Esito.FALLIMENTO);
			return false;
		}
		//
		
		//verifichiamo che le liquidazioni abbiano tutte lo stesso SIOPE PLUS e che sia
		//uguale a quello indicato:
		SiopePlusDto siopePlusOrdinativo = new SiopePlusDto(ordinativoDiPagamento);
		boolean tuttiUguali = true;
		for(Liquidazione liqIt: liquidazioniDelleQuote){
			SiopePlusDto dtoIt = new SiopePlusDto(liqIt);
			if(dtoIt.sonoDiversi(siopePlusOrdinativo)){
				tuttiUguali = false;
				break;
			}
		}
		if(!tuttiUguali){
			addErroreCore(ErroreCore.FORMATO_NON_VALIDO,"Siope+", " (Le quote e l'ordinativo non hanno tutti lo stesso SIOPE+)");
			return false;
		}
		
//		1. Bilancio in ESERCIZIO PROVVISORIO
//		2. Se ANNO BILANCIO = ANNI MOVIMENTO (mandato su impegni di competenza)
//		3. Se tutti gli impegni che formano il mandato hanno flagFrazionabile = TRUE
//		4. Se è andato a buon fine il controllo sulla disponibilità a pagare
//
//		Il controllo si effettua in inserimento (importo Ordinativo <= disponibile12) e aggiornamento (delta importi Ordinativo <= disponibile12)
//
//		Per ricavare disponibile12 richiamare la finction:
//		fnc_siac_disponibilitadodicesimi_dpm ( id_in integer) RETURNS nume
		
		return true;
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		final String methodName="InserisceOrdinativoPagamentoService : checkServiceParam()";
		log.debug(methodName, " - Begin");

		//dati di input presi da request:
		Ente ente = req.getEnte();
		Bilancio bilancio = req.getBilancio();
		
		OrdinativoPagamento ordinativoDiPagamento = req.getOrdinativoPagamento();
		
		//lunghezza massima descrizioni:
		controlliDescrizioniOrdinativoPagamento(ordinativoDiPagamento);
		
		String elencoParamentriNonInizializzati = "";
		
		if(ente==null){
			if(elencoParamentriNonInizializzati.length() > 0){
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", ENTE";
			}else{
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "ENTE";
			}	
		}
		
		if(ordinativoDiPagamento==null){
			if(elencoParamentriNonInizializzati.length() > 0){
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", ORDINATIVO_DI_PAGAMENTO";
			}else{
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "ORDINATIVO_DI_PAGAMENTO";
			}	
		} else {
			if(ordinativoDiPagamento.getCapitoloUscitaGestione()==null){
				if(elencoParamentriNonInizializzati.length() > 0){
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", CAPITOLO";
				}else{
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "CAPITOLO";
				}	
			}

			if(ordinativoDiPagamento.getCodPdc()==null || (ordinativoDiPagamento.getCodPdc()!=null && ordinativoDiPagamento.getCodPdc().isEmpty())){
				if(elencoParamentriNonInizializzati.length() > 0){
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", ELEMENTO_PIANO_DEI_CONTI";
				}else{
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "ELEMENTO_PIANO_DEI_CONTI";
				}	
			}

			// CR - 3746 il siope diventa obbligatorio
			if(StringUtilsFin.isEmpty(ordinativoDiPagamento.getCodSiope())){
				
				if(elencoParamentriNonInizializzati.length() > 0){
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", SIOPE";
				}else{
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "SIOPE";
				}	
			}

			if(ordinativoDiPagamento.getCodiceBollo()==null || (ordinativoDiPagamento.getCodiceBollo()!=null && StringUtilsFin.isEmpty(ordinativoDiPagamento.getCodiceBollo().getCodice()))){
				if(elencoParamentriNonInizializzati.length() > 0){
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", BOLLO";
				}else{
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "BOLLO";
				}	
			}

			if(ordinativoDiPagamento.getSoggetto() == null){
				if(elencoParamentriNonInizializzati.length() > 0){
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", CREDITORE_PAGAMENTO";
				}else{
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "CREDITORE_PAGAMENTO";
				}	
			} else {
				if(ordinativoDiPagamento.getSoggetto().getElencoModalitaPagamento()==null){
					if(elencoParamentriNonInizializzati.length() > 0){
						elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", MODALITA_DI_PAGAMENTO";
					}else{
						elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "MODALITA_DI_PAGAMENTO";
					}	
				} else {
					if(ordinativoDiPagamento.getSoggetto().getElencoModalitaPagamento().size() == 0){
						if(elencoParamentriNonInizializzati.length() > 0){
							elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", MODALITA_DI_PAGAMENTO";
						}else{
							elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "MODALITA_DI_PAGAMENTO";
						}	
					}
				}
			}
			
			if(ordinativoDiPagamento.getAttoAmministrativo()==null){
				if(elencoParamentriNonInizializzati.length() > 0){
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", ATTO_AMMINISTRATIVO";
				}else{
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "ATTO_AMMINISTRATIVO";
				}	
			}
			
			if(ordinativoDiPagamento.getElencoSubOrdinativiDiPagamento()==null){
				if(elencoParamentriNonInizializzati.length() > 0){
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", ELENCO_SUBORDINATIVI_DI_PAGAMENTO";
				}else{
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "ELENCO_SUBORDINATIVI_DI_PAGAMENTO";
				}	
			} else {
				if(ordinativoDiPagamento.getElencoSubOrdinativiDiPagamento().size() == 0){
					if(elencoParamentriNonInizializzati.length() > 0){
						elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", ELENCO_SUBORDINATIVI_DI_PAGAMENTO";
					}else{
						elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "ELENCO_SUBORDINATIVI_DI_PAGAMENTO";
					}	
				} else {
					if(ordinativoDiPagamento.getElencoSubOrdinativiDiPagamento().size() > 0){
						for(SubOrdinativoPagamento subOrdinativoPagamento : ordinativoDiPagamento.getElencoSubOrdinativiDiPagamento()){
							if(null!=subOrdinativoPagamento && subOrdinativoPagamento.getLiquidazione()==null){
								if(elencoParamentriNonInizializzati.length() > 0){
									elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", LIQUIDAZIONE";
								} else {
									elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "LIQUIDAZIONE";
								}
								
								break;
							}				
						}
					}
				}
			}
		}

		if(bilancio==null){
			if(elencoParamentriNonInizializzati.length() > 0){
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", BILANCIO";
			}else{
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "BILANCIO";
			}	
		} else {
			if(bilancio.getAnno() == 0){
				if(elencoParamentriNonInizializzati.length() > 0){
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", ANNO_DI_ESERCIZIO";
				}else{
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "ANNO_DI_ESERCIZIO";
				}	
			}
		}

		if(!StringUtilsFin.isEmpty(elencoParamentriNonInizializzati)){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore(elencoParamentriNonInizializzati));
		}	
	}	
}