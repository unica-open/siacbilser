/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.ordinativo;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.business.service.AbstractBaseServiceRicercaOrdinativo;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativo;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoResponse;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaOrdinativoIncasso;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaOrdinativoIncassoService extends AbstractBaseServiceRicercaOrdinativo<RicercaOrdinativo, RicercaOrdinativoResponse> {
	
	

	@Override
	@Transactional(readOnly=true)
	public RicercaOrdinativoResponse executeService(RicercaOrdinativo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	public void execute() {
		String methodName = "RicercaOrdinativoIncassoService - execute()";
		log.debug(methodName, " - Begin");
		
		Ente ente = req.getEnte();
		Integer idEnte = ente.getUid();
		Richiedente richiedente = req.getRichiedente();
		Calendar calendar = Calendar.getInstance();
		java.util.Date nowDate = calendar.getTime();
		java.sql.Timestamp now = new java.sql.Timestamp(nowDate.getTime());
		ParametroRicercaOrdinativoIncasso parametroRicercaOrdinativoIncasso = req.getParametroRicercaOrdinativoIncasso();

		// Si invoca il metodo calcolaNumeroOrdinativiIncassoDaEstrarre che ci restituisce il numero di risultati attesi dalla query composta
		// secondo i parametri di ricerca
		
		// RM: per la jira 4556  FIN - Ordinativi e liquidazioni - Toglietre 'Ricerca tropo estesa'
		Long conteggioRecords = ordinativoIncassoDad.calcolaNumeroOrdinativiIncassoDaEstrarre(parametroRicercaOrdinativoIncasso, idEnte);
		//...solo se il numero di risultati attesi e minore del numero massimo accettabile si procede con il caricamento di tutti i dati:
		//if(conteggioRecords <= Constanti.MAX_RIGHE_ESTRAIBILI.longValue()){
			
			// si invoca il metodo che carica tutti i dati rispetto alla query composta dall'input ricevuto:
			List<OrdinativoIncasso> listaRisultati = ordinativoIncassoDad.ricercaOrdinativiIncasso(richiedente, parametroRicercaOrdinativoIncasso, idEnte, req.getNumPagina(), req.getNumRisultatiPerPagina(),now);

			BigDecimal totImporti = ordinativoIncassoDad.totImporti(idEnte, parametroRicercaOrdinativoIncasso, req.getNumPagina(), req.getNumRisultatiPerPagina());
			res.setTotImporti(totImporti);
			
			// Si inizializza l'oggetto DatiOperazioneDto, dto di comodo generico che verra' passato tra i metodi
			DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.INSERIMENTO, null);
			
			// controllo se l'incasso è un collegato di un pagamento (serve nella funzionalità di collega in aggiorna
			// ordinativo di pagamento
			for (OrdinativoIncasso ordinativoIncasso : listaRisultati) {
				
				boolean collegatoAPagamento = ordinativoIncassoDad.isCollegatoAPagamento(ordinativoIncasso.getAnno(), ordinativoIncasso.getNumero(), ordinativoIncasso.getStatoOperativoOrdinativo(), Constanti.D_ORDINATIVO_TIPO_INCASSO, datiOperazione);
				ordinativoIncasso.setCollegatoAPagamento(collegatoAPagamento);
			}
			
			
			res.setErrori(null);
			res.setEsito(Esito.SUCCESSO);
			res.setElencoOrdinativoIncasso(listaRisultati);
			res.setNumRisultati(conteggioRecords.intValue());
			res.setNumPagina(req.getNumPagina());
			
//		} else {
//			
//			res.setErrori(Arrays.asList(ErroreFin.RICERCA_TROPPO_ESTESA.getErrore()));
//			res.setEsito(Esito.FALLIMENTO);
//			res.setElencoOrdinativoPagamento(null);
//		}
	}
	

}
