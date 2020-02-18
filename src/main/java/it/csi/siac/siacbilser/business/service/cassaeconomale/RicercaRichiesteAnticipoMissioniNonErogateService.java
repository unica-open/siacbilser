/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cassaeconomale;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.hr.HRServiceDelegate;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaRichiesteAnticipoMissioniNonErogate;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaRichiesteAnticipoMissioniNonErogateResponse;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siacfinser.model.soggetto.ComuneNascita;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.srvpers.rapws.interfaccecxf.services.missioni.MissioneConsuntivoType;
import it.csi.srvpers.rapws.interfaccecxf.services.missioni.MissioneContabileType;

/**
 * Ricerca l'elenco delle Richieste economali di Anticipo Spese Missione autorizzate ma non ancora erogate.
 * <br/>
 * Utilizza i servizi di HR: vm140 e vm160: Elenca le trasferte con richiesrta anticipo con o senza tutti i giustificativi associati. 
 * I dati esposti sono quelli già usati attualmente e in aggiunta i dati anagrafici del dipendente (vista VI_DIPENDENTI_REG) 
 * <br/>
 * Il servizio non richiede transazionalit&agrave;
 * @author Domenico
 * @author AR
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaRichiesteAnticipoMissioniNonErogateService extends CheckedAccountBaseService<RicercaRichiesteAnticipoMissioniNonErogate, RicercaRichiesteAnticipoMissioniNonErogateResponse> {
	

	@Autowired
	protected HRServiceDelegate hrServiceDelegate;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		//Nessun parametro in input.
	}
	
	// Il servizio non richiede transazionalita'
	
	@Override
	protected void execute() {
		String methodName = "execute";
		List<MissioneContabileType> vm140s = hrServiceDelegate.vm140();
		log.debug(methodName, "Numero richieste anticipo spese missione pendenti trovate [vm140]: " + (vm140s != null ? vm140s.size() : "null"));
		
		Map<String, List<MissioneConsuntivoType>> mapDettagliGiustificativi = caricaDettaglioGiustificativi();
		
		if(vm140s!=null) {
			for(MissioneContabileType mct : vm140s) {
				List<MissioneConsuntivoType> mconsuntivos = mapDettagliGiustificativi.get(mct.getId());
				res.addRichiestaEconomale(toRichiestaEconomale(mct, mconsuntivos));
			}
		}
		
		res.setCardinalitaComplessiva(vm140s != null ? vm140s.size() : 0);
	}

	/**
	 * Carica il dettaglio di tutti i giustificativi associati alle richieste tramite il servizio vm160 di HR.
	 * 
	 * @return mappa con chiave l'idMissioneEsterna e valore l'elenco dei giustificativi associati.
	 */
	private Map<String, List<MissioneConsuntivoType>> caricaDettaglioGiustificativi() {
		final String methodName = "caricaDettaglioGiustificativi";
		
		Map<String, List<MissioneConsuntivoType>> m = new HashMap<String, List<MissioneConsuntivoType>>();
		
		if(!req.isCaricaDettaglioGiustificativi()) {
			log.debug(methodName, "Dettaglio giustificativi non richiesto. Per richiederlo impostare in request isCaricaDettaglioGiustificativi a true.");
			return m;
		}
		
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
	 * I dati necessari attualmente sono i seguenti:
	 * Codice della missione, matricola, cognome-nome del dipendente.
	 * 
	 * @param mct
	 * @param mconsuntivos 
	 * @return
	 */
	private RichiestaEconomale toRichiestaEconomale(MissioneContabileType mct, List<MissioneConsuntivoType> mconsuntivos) {
		RichiestaEconomale re = new RichiestaEconomale();
		re.setIdMissioneEsterna(mct.getId()); //Id della Missione di HR.
		re.setDataMissioneEsternaDa(mct.getDataDa());
		re.setDataMissioneEsternaA(mct.getDataA());
		
		
		//TODO valutare utilizzo di Dozer! in modo da riutilizzare il codice anche nella RicercaSoggettoHRPerChiaveService e RicercaSoggettiHRService
		
		Soggetto soggetto = new Soggetto();
		// SIAC-2783: Il campo matricola va ricercato nel campo HR EX_MATRICOLA.
		//soggetto.setMatricola(""+mct.getNewMatricola()); //mct.getExMatricola();
		soggetto.setMatricola(mct.getExMatricola());
		soggetto.setCognome(mct.getDipendente()); //TODO split nome e cognome
		soggetto.setNome(mct.getDipendente());
		soggetto.setDenominazione(mct.getDipendente());
		ComuneNascita comuneNascita = new ComuneNascita();
		comuneNascita.setDescrizione(mct.getComuneNascita());
		soggetto.setComuneNascita(comuneNascita);
		re.setSoggetto(soggetto);
		
		re.setNumeroRichiesta(mct.getProgressivo());
		//re.setNumeroRendicontoStampato(new Integer(mct.getProtocollo())); //TODO need to catch NumberFormatException
		
		
		/*Esempio di valori che arrivano da vm140:
		 *      <abiCod>03069</abiCod>
			    <blocco>N</blocco>
			    <cabCod>30090</cabCod>
			    <cin>C</cin>
			    <comuneNascita>SERRE</comuneNascita>
			    <contoCorrente>100000005402</contoCorrente>
			    <dataA>01-12-2015</dataA>
			    <dataDa>01-12-2015</dataDa>
			    <dataNascita>19-07-1959</dataNascita>
			    <dipendente>SOLE MICHELINA</dipendente>
			    <exMatricola>06625SM</exMatricola>
			    <flagDestinazione>R</flagDestinazione>
			    <flagIspettiva>N</flagIspettiva>
			    <flagTipoAccredito>1</flagTipoAccredito>
			    <ibanDip>IT73C0306930090100000005402</ibanDip>
			    <id>755149</id>
			    <localita>TORINO</localita>
			    <mezziTrasporto>TAXI</mezziTrasporto>
			    <newMatricola>3143</newMatricola>
			    <oraA>18.00</oraA>
			    <oraDa>08.00</oraDa>
			    <progressivo>15236</progressivo>
			    <protocollo>2015005678</protocollo>
			    <raggrCont>100856</raggrCont>
			    <status>1</status>
			    <tipoDip>D</tipoDip>
			    <tipoRegistrazione>DIP1</tipoRegistrazione>
			    <unitaOrg>A1108A</unitaOrg>
			    <uorDescr>A1108A_DESCR</uorDescr>
		 */
		
		if(mconsuntivos!=null) {
			for(MissioneConsuntivoType mconsuntivo: mconsuntivos){
				Giustificativo g = new Giustificativo();
				g.setNumeroGiustificativo(""+mconsuntivo.getProgressivo());
				g.setQuantita(mconsuntivo.getQuantita() != null ? mconsuntivo.getQuantita().intValue() : null);
				
				TipoGiustificativo tipoGiustificativo = new TipoGiustificativo();
				tipoGiustificativo.setCodice(mconsuntivo.getCodice());
				g.setTipoGiustificativo(tipoGiustificativo);
				
				/*
				Esempio di valori che arrivano da vm160: 
					<codice>TAX</codice>
				    <id>755143</id>
				    <progressivo>15236</progressivo>
				    <quantita>25.0</quantita>
				 */
				
				re.getGiustificativi().add(g);
			}
		}
		
		return re;
	}

}
