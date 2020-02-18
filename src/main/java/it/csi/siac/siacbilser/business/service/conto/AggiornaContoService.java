/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.conto;

import java.util.Date;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaContoResponse;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.errore.ErroreGEN;

/**
 * Servizio di aggiornamento di un Conto
 * 
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaContoService extends CrudContoBaseService<AggiornaConto, AggiornaContoResponse> {

	private Integer livelloDiLegge;

	/*
	 * Effettuati i controlli richiesti sui singoli campi dell’entità, vedi par. 2.8.3.3 e poi 2.8.3.2.
	 * 
	 * Successivamente effettuare i seguenti controlli.
	 * 
	 * Se è stato modificato il codice del conto:
	 * - Verificare che il codice imputato sia coerente con il codice del conto selezionato (contenere il
	 *   conto padre come prefisso) In caso di errore inviare un messaggio <
	 *   COR_ERR_0044 Operazione non consentita (messaggio: ‘codice non coerente’
	 *   )>. 
	 * 
	 * - Verificare che il codice imputato sia univoco rispetto agli altri
	 *   conti figli del conto selezionato. In caso di errore inviare un messaggio
	 *   < COR_ERR_0044 Operazione non consentita (messaggio: ‘codice non univoco’
	 *   )>.
	 * 
	 * 
	 * Se la verifica ha esito positivo si opera l’aggiornamento come descritto
	 * di seguito. Per una migliore comprensione si specifica che deve esistere
	 * una sola istanza per anno, per cui per la prima istanza della vita di un
	 * conto è ammesso aggiornare tutti i dati, per le successive si opera in
	 * aggiornamento delle relazioni per le quali è gestita la storicizzazione.
	 * 
	 * • Se data inizio validità del conto [su DB] = anno corrente [anno di bilancio su cui si opera]: si procede con
	 *   aggiornamento dei dati dell’intero Conto e sue relazioni mantenendo le
	 *   date di inizio validità di tutte le istanze e relazioni 
	 * • Se data inizio validità del conto [su DB] < anno corrente : sono possibili aggiornamenti delle
	 *   sole relazioni (e solo di quelle previste dalla maschera) . Operare in
	 *   questo caso discriminando le due casistiche. 
	 *   • Primo aggiornamento dell’anno di bilancio (data inizio validità della relazione < anno
	 *     corrente) : inserire una nuova istanza delle relazioni interessate dalle
	 *     modifiche con data inizio validità = 01/01/ anno corrente e chiudere la
	 *     precedente con data fine validità = 31/12/anno precedente 
	 *   • Successivo aggiornamento (data inizio validità della relazione = anno corrente) :
	 *     aggiornare i dati dell’istanza delle relazioni interessate.
	 * 
	 * Il servizio deve: 
	 * - salvare i dati modificati del piano (entità Conto ed
	 *   entità correlate) 
	 * - salvare per tutti i livelli sottostanti gli aggiornamenti ricorsivi, 
	 *   che devono essere effettuati per i seguenti attributi: o attivo 
	 * - impostare la data di aggiornamento con la data
	 *   corrente per l’entità e relazioni interessate dall’aggiornamento.
	 */

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		this.conto = req.getConto();
		checkEntita(conto, "conto");
		conto.setEnte(ente);
		
		checkEntita(conto.getContoPadre(), "conto padre conto");
		
		checkNotBlank(conto.getCodice(), "codice conto");
		checkNotBlank(conto.getDescrizione(), "descrizione conto");
		
		checkNotNull(conto.getAttivo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("attivo"));
		
		checkEntita(conto.getTipoConto(), "tipo conto");
		
		checkCondition( (conto.getTipoConto().isTipoCespiti() && conto.getCategoriaCespiti() != null) || (!conto.getTipoConto().isTipoCespiti() && conto.getCategoriaCespiti() == null), 
				ErroreGEN.OPERAZIONE_NON_CONSENTITA_0011.getErrore());

		this.bilancio = req.getBilancio();
		checkEntita(bilancio, "bilancio");
	}
	
	@Override
	@Transactional
	public AggiornaContoResponse executeService(AggiornaConto serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		caricaBilancio();
		caricaContoPadre();
		caricaLivelloDiLegge();

		checkCodiceConto();
		
		Date inizioAnno = Utility.primoGiornoDellAnno(bilancio.getAnno());
		conto.setDataInizioValidita(inizioAnno);
		
		conto.setLivello(conto.getContoPadre().getLivello() + 1);
		
		conto.setOrdine(conto.getCodice());
		
		conto.setAmbito(conto.getContoPadre().getAmbito());
		
		//conto.setAttivo(Boolean.TRUE);//solo Inserimento
		
		contoDad.aggiornaConto(conto);
		res.setConto(conto);
		
		aggiornaContiFiglioRicorsivamente(conto);
	}
	
	@Override
	protected void checkCodiceConto() {
		if(conto.getContoPadre().getLivello() != 0){
			super.checkCodiceConto();
		}
		
		//Verifica se il codice conto non e' già stato utilizato
		Conto contoTrovato = contoDad.ricercaContoByCodice(this.conto.getCodice(), bilancio.getAnno(), this.conto.getContoPadre().getAmbito());
		if(contoTrovato != null && contoTrovato.getUid() != conto.getUid()) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Codice non univoco: "+ this.conto.getCodice()));
		}
	}
	
		
	/**
	 * Carica livello di legge.
	 */
	private void caricaLivelloDiLegge() {
		try {
			//TODO riutilizzo il piano dei conti caricato in precedenza ma associato al padre (per evitare un nuovo accesso a db)! verificare se va bene!
			livelloDiLegge = conto.getContoPadre().getPianoDeiConti().getClassePiano().getLivelloDiLegge();
		} catch(NullPointerException npe) {
			throw new BusinessException("Impossibile determinare il livello di legge associato al conto. Verificare sulla base dati il legame con il piano dei conti e la classe piano.");
		}
	}


	/**
	 * True se il conto che si sta aggiornando e' del livello di legge.
	 *
	 * @return True se di livello di legge
	 */
	private boolean isContoDiLivelloDiLegge() {
		return conto.getLivello().equals(livelloDiLegge); 
		
	}

	/**
	 * Aggiorna i conti figlio ricorsivamente con i dati del conto padre.
	 *
	 * @param contoPadre the conto padre
	 */
	private void aggiornaContiFiglioRicorsivamente(Conto contoPadre) {
		String methodName = "aggiornaContiFiglioRicorsivamente";
		contoPadre.setDataInizioValiditaFiltro(this.conto.getDataInizioValidita());
		
		ListaPaginata<Conto> contiFiglio = contoDad.ricercaSinteticaContoFigli(contoPadre, new ParametriPaginazione(0,Integer.MAX_VALUE));
		for (Conto contoFiglio : contiFiglio) {
			
			//TODO aggiungere qui tutti i parametri da ribaltare sui conti figli.
			contoFiglio.setAttivo(contoPadre.getAttivo()); //in analisi c'è solo questo parametro!
			
			if(isContoDiLivelloDiLegge()) {
				log.debug(methodName, "Conto di livello di legge: aggiorno tutti i campi del figlio " + contoFiglio.getCodice());
				
				//TODO controllare eventuali altri parametri da ribaltare ai conti figlio.
				contoFiglio.setElementoPianoDeiConti(contoPadre.getElementoPianoDeiConti());
				contoFiglio.setCategoriaCespiti(contoPadre.getCategoriaCespiti());
				contoFiglio.setTipoConto(contoPadre.getTipoConto());
				contoFiglio.setTipoLegame(contoPadre.getTipoLegame());
				contoFiglio.setContoAPartite(contoPadre.getContoAPartite());
				contoFiglio.setContoDiLegge(contoPadre.getContoDiLegge());
				contoFiglio.setCodiceBilancio(contoPadre.getCodiceBilancio());
				contoFiglio.setContoCollegato(contoPadre.getContoCollegato());
			} else {
				log.debug(methodName, "Conto NON di livello di legge: aggiorno solo il flag Attivo del figlio " + contoFiglio.getCodice());
			}
			
			contoDad.aggiornaConto(contoFiglio);
			log.debug(methodName, "Aggiornato conto figlio: "+ contoFiglio.getCodice());
			aggiornaContiFiglioRicorsivamente(contoFiglio);
		}
	}
	

	

}
