/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cassaeconomale;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.hr.HRServiceDelegate;
import it.csi.siac.siacbilser.business.utility.BilUtilities;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.TipoGiustificativoDad;
import it.csi.siac.siacbilser.integration.dad.ValutaDad;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaAnticipoMissioneNonErogata;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaAnticipoMissioneNonErogataResponse;
import it.csi.siac.siaccecser.model.DatiTrasfertaMissione;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccecser.model.TipologiaGiustificativo;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.Valuta;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.srvpers.rapws.interfaccecxf.services.missioni.MissioneConsuntivoType;
import it.csi.srvpers.rapws.interfaccecxf.services.missioni.MissioneContabileType;

/**
 * Ricerca la Richiesta economale di Anticipo Spese Missione per id.
 * 
 * Utilizza i servizi di HR: vm140 e vm160, e popola la singola richiesta desiderata con i dati dei giustificativi.
 *
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/12/2015
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioRichiestaAnticipoMissioneNonErogataService extends CheckedAccountBaseService<RicercaDettaglioRichiestaAnticipoMissioneNonErogata, RicercaDettaglioRichiestaAnticipoMissioneNonErogataResponse> {

	@Autowired
	private HRServiceDelegate hrServiceDelegate;
	@Autowired
	private TipoGiustificativoDad tipoGiustificativoDad;
	@Autowired
	private ValutaDad valutaDad;
	
	private final Date now = Utility.truncateToStartOfDay(new Date());
	private final DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.ITALIAN);
	private final Map<String, TipoGiustificativo> cacheTipoGiustificativo = new HashMap<String, TipoGiustificativo>();
	private final Map<String, Valuta> cacheValuta = new HashMap<String, Valuta>();
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotBlank(req.getIdMissioneEsterna(), "Id missione esterna");
		checkEntita(req.getCassaEconomale(), "Cassa economale");
	}
	
	@Override
	protected void init() {
		tipoGiustificativoDad.setEnte(ente);
		valutaDad.setEnte(ente);
	}
	
	@Override
	@Transactional
	public RicercaDettaglioRichiestaAnticipoMissioneNonErogataResponse executeService(RicercaDettaglioRichiestaAnticipoMissioneNonErogata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	protected void execute() {
		String methodName = "execute";
		List<MissioneContabileType> vm140s = hrServiceDelegate.vm140();
		if(vm140s == null) {
			log.info(methodName, "Nessuna missione da caricamento esterno reperita");
			return;
		}
		
		log.debug(methodName, "Numero richieste anticipo spese missione pendenti trovate [vm140]: " + vm140s.size());
		
		MissioneContabileType missioneContabileType = findMissioneContabileTypeByIdMissione(vm140s);
		Map<String, List<MissioneConsuntivoType>> mapDettagliGiustificativi = caricaDettaglioGiustificativi();
		
		if(missioneContabileType == null || !mapDettagliGiustificativi.containsKey(req.getIdMissioneEsterna())) {
			log.info(methodName, "Nessuna missione da caricamento esterna reperita per id " + req.getIdMissioneEsterna());
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Richiesta da caricamento esterno", req.getIdMissioneEsterna()));
		}
		List<MissioneConsuntivoType> missioneConsuntivoTypes = mapDettagliGiustificativi.get(req.getIdMissioneEsterna());
		
		RichiestaEconomale richiestaEconomale = toRichiestaEconomale(missioneContabileType, missioneConsuntivoTypes);
		res.setRichiestaEconomale(richiestaEconomale);
	}

	/**
	 * Cerca la missione per id.
	 * 
	 * @param vm140s la lista delle missioni ottenute dall'esterno
	 * @return la missione corrispondente all'id fornito
	 */
	private MissioneContabileType findMissioneContabileTypeByIdMissione(List<MissioneContabileType> vm140s) {
		for(MissioneContabileType mct : vm140s) {
			if(mct != null && req.getIdMissioneEsterna().equals(mct.getId())) {
				return mct;
			}
		}
		return null;
	}

	/**
	 * Carica il dettaglio di tutti i giustificativi associati alle richieste tramite il servizio vm160 di HR.
	 * 
	 * @return mappa con chiave l'idMissioneEsterna e valore l'elenco dei giustificativi associati.
	 */
	private Map<String, List<MissioneConsuntivoType>> caricaDettaglioGiustificativi() {
		final String methodName = "caricaDettaglioGiustificativi";
		
		Map<String, List<MissioneConsuntivoType>> m = new HashMap<String, List<MissioneConsuntivoType>>();
		
		List<MissioneConsuntivoType> vm160s = hrServiceDelegate.vm160();
		log.debug(methodName, "Numero giustificativi delle richieste anticipo spese missione pendenti trovati [vm160]: " + (vm160s != null ? vm160s.size() : "null"));
		
		if(vm160s!=null){
			for(MissioneConsuntivoType mconst : vm160s) {
				if (!m.containsKey(mconst.getId())) {
					m.put(mconst.getId(), new ArrayList<MissioneConsuntivoType>());
				}
				m.get(mconst.getId()).add(mconst);
			}
		}
		return m;
	}


	/**
	 * Popla la richiestaEconomale a partire da MissioneContabileType di HR.
	 * 
	 * @param mct  la missione contabile
	 * @param mcts i consuntivi
	 * @return la richiesta economale
	 */
	private RichiestaEconomale toRichiestaEconomale(MissioneContabileType mct, List<MissioneConsuntivoType> mcts) {
		RichiestaEconomale re = new RichiestaEconomale();
		
		Soggetto soggetto = new Soggetto();
		re.setSoggetto(soggetto);
		
		DatiTrasfertaMissione dtm = new DatiTrasfertaMissione();
		re.setDatiTrasfertaMissione(dtm);
		
		List<String> chunksNote = new ArrayList<String>();
		List<Giustificativo> giustificativi = new ArrayList<Giustificativo>();
		
		re.setNumeroRichiesta(mct.getProgressivo());
		
		// ID => ID missione HR
		re.setIdMissioneEsterna(mct.getId());
		
		// NEW_MATRICOLA + DIPENDENTE => Matricola del dipendente + cognome/nome
		// JIRA-2783: Il campo matricola va ricercato nel campo HR EX_MATRICOLA. 
//		soggetto.setMatricola(mct.getNewMatricola() != null ? mct.getNewMatricola().toString() : null);
		soggetto.setMatricola(mct.getExMatricola());
		soggetto.setDenominazione(mct.getDipendente());
		
		// UNITA_ORG + UOR_DESCR => Unita' organizzativa
		dtm.setUnitaOrganizzativa(composeString(mct.getUnitaOrg(), " ", mct.getUorDescr()));
		
		// PROTOCOLLO + MOTIVAZIONE => Compilare il campo Motivo della trasferta in questo modo: 'Caricamento da esterno protocollo N. ' + PROTOCOLLO + MOTIVAZIONE
		dtm.setMotivo(composeString("Caricamento da esterno protocollo N. ", mct.getProtocollo(), " ", mct.getMotivazioni()));
		
		// FLAG_DESTINAZIONE (R=Regione – I=Italia E=Estero) => Estero Si/No
		// Se 'R' mettere Estero=no ma nel campo Note mettere 'Trasferta in Regione'
		dtm.setFlagEstero("E".equals(mct.getFlagDestinazione()));
		if("R".equals(mct.getFlagDestinazione())) {
			chunksNote.add("Trasferta in Regione");
		}
		
		// LOCALITA => Luogo della trasferta
		dtm.setLuogo(mct.getLocalita());
		
		// DATADA => Data inizio trasferta
		dtm.setDataInizio(parseDate(mct.getDataDa()));
		
		// DATAA => Data fine trasferta
		dtm.setDataFine(parseDate(mct.getDataA()));
		
		// DELEGATO => Delegato incasso
		re.setDelegatoAllIncasso(mct.getDelegato());
		
		// MEZZI_TRA-SPORTO => Mettere nel campo Note con prefisso 'Mezzo di trasporto: '
		chunksNote.add(composeString("Mezzo di trasporto: ", mct.getMezziTrasporto()));
		
		re.setNote(StringUtils.join(chunksNote, "\n"));
		
		if(mcts != null) {
			for(MissioneConsuntivoType m: mcts) {
				Giustificativo g = caricaGiustificativo(m);
				giustificativi.add(g);
			}
		}
		re.setGiustificativi(giustificativi);
		
		return re;
	}
	
	/**
	 * Compone la stringa aggiungendo i vari segmenti che la compongono. Trasforma i <code>null</code> in stringhe vuote.
	 * 
	 * @param chunks i pezzi della stringa
	 * @return la stringa
	 */
	private String composeString(String... chunks) {
		StringBuilder sb = new StringBuilder();
		for(String chunk : chunks) {
			sb.append(chunk != null ? chunk : "");
		}
		return sb.toString();
	}

	/**
	 * Parse della data.
	 * @param source la data in testo
	 * @return la data, se parsificabile; <code>null</code> altrimenti
	 */
	private Date parseDate(String source) {
		final String methodName = "parseDate";
		try {
			return df.parse(source);
		} catch (ParseException e) {
			log.info(methodName, "Parse exception for date " + source + ": returning null");
			return null;
		}
	}
	
	/**
	 * Caricamento dei dati del giustificativo.
	 * 
	 * @param m il tipo da cui ottenere i dati del giustificativo
	 * @return il giustificativo SIAC
	 */
	private Giustificativo caricaGiustificativo(MissioneConsuntivoType mct) {
		final String methodName = "caricaGiustificativo";
		Giustificativo g = new Giustificativo();
		
		g.setEnte(ente);
		// FIXME: e' questa la data corretta?
		g.setDataEmissione(now);
		g.setNumeroGiustificativo(mct.getProgressivo() != null ? mct.getProgressivo().toString() : null);
		
		// Valuta mettere EUR fisso
		g.setValuta(ottieniValuta("EUR"));
		
		TipoGiustificativo tipoGiustificativo = ottieniTipoGiustificativo(mct.getCodice());
		g.setTipoGiustificativo(tipoGiustificativo);
		if(tipoGiustificativo.getImporto() == null || BigDecimal.ZERO.compareTo(tipoGiustificativo.getImporto()) == 0) {
			// Caricamento come importo
			log.debug(methodName, "Caricamento come importo: " + mct.getQuantita());
			impostaImporto(g, mct.getQuantita(), tipoGiustificativo);
		} else {
			// Caricamento come quantita
			log.debug(methodName, "Caricamento come quantita: " + mct.getQuantita() + " - importo tipo: " + tipoGiustificativo.getImporto());
			BigDecimal quantita = new BigDecimal(mct.getQuantita().toString());
			BigDecimal importo = quantita.multiply(tipoGiustificativo.getImporto());
			impostaImporto(g, importo, tipoGiustificativo);
			g.setQuantita(quantita.intValue());
		}
		
		return g;
	}
	
	/**
	 * Ottiene il tipo di giustificativo da cache o da DB.
	 * 
	 * @param codice il codice del giustificativo
	 * @return il tipo giustificativo di dato codice
	 */
	private TipoGiustificativo ottieniTipoGiustificativo(String codice) {
		String methodName = "ottieniTipoGiustificativo";
		if(cacheTipoGiustificativo.containsKey(codice)) {
			return cacheTipoGiustificativo.get(codice);
		}
		TipoGiustificativo temp = new TipoGiustificativo();
		temp.setCassaEconomale(req.getCassaEconomale());
		temp.setCodice(codice);
		temp.setTipologiaGiustificativo(TipologiaGiustificativo.ANTICIPO_MISSIONE);
		List<TipoGiustificativo> tipoGiustificativos = tipoGiustificativoDad.findTipoGiustificativoByCodiceETipologia(temp);
		if(tipoGiustificativos == null || tipoGiustificativos.isEmpty()) {
			log.error(methodName, "PROBABILE DISALLINEAMENTO CON HR codice " + codice);
			throw new BusinessException("Impossibile reperire un tipo giustificativo di codice " + codice + " per la cassa economale");
		}
		
		TipoGiustificativo tipoGiustificativo = tipoGiustificativos.get(0);
		cacheTipoGiustificativo.put(codice, tipoGiustificativo);
		return tipoGiustificativo;
	}
	
	/**
	 * Ottiene la valuta da db o da cache.
	 * 
	 * @param codice il codice della valuta
	 * @return la valuta
	 */
	private Valuta ottieniValuta(String codice) {
		if(cacheValuta.containsKey(codice)) {
			return cacheValuta.get(codice);
		}
		List<Valuta> valutas = valutaDad.findByCodice(codice);
		if(valutas == null || valutas.isEmpty()) {
			throw new BusinessException("Impossibile reperire una valuta di codice " + codice);
		}
		
		Valuta valuta = valutas.get(0);
		cacheValuta.put(codice, valuta);
		return valuta;
	}
	
	/**
	 * Impostazione dell'importo.
	 * 
	 * @param giustificativo il giustificativo da popolare
	 * @param importo l'importo da impostare
	 * @param tipoGiustificativo il tipo di giustificativo
	 */
	private void impostaImporto(Giustificativo giustificativo, Number importo, TipoGiustificativo tipoGiustificativo) {
		BigDecimal importoGiustificativo = new BigDecimal(importo.toString()).setScale(2, RoundingMode.HALF_EVEN);
		BigDecimal impostoSpettanteGiustificativo = importoGiustificativo
				.multiply(tipoGiustificativo.getPercentualeAnticipoMissioneNotNull())
				.divide(BilUtilities.BIG_DECIMAL_ONE_HUNDRED)
				.setScale(2, RoundingMode.HALF_EVEN);
		
		giustificativo.setImportoGiustificativo(importoGiustificativo);
		// La valuta e' EUR
//		giustificativo.setImportoGiustificativoInValuta(importoGiustificativo);
		giustificativo.setImportoSpettanteGiustificativo(impostoSpettanteGiustificativo);
	}
	
}
