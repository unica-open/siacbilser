/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaDisponibilitaCassaCapitoloByMovimento;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaDisponibilitaCassaCapitoloByMovimentoResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.ImportoDerivatoFunctionEnum;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;


// TODO: Auto-generated Javadoc
/* Consente di calcolare la disponibilità di un capitolo di Bilancio 
 * con i dati passati in input.
 * VM*/

/**
 * The Class CalcoloDisponibilitaDiUnCapitoloService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ControllaDisponibilitaCassaCapitoloByMovimentoService
		extends CheckedAccountBaseService<ControllaDisponibilitaCassaCapitoloByMovimento, ControllaDisponibilitaCassaCapitoloByMovimentoResponse>{ 
	
	@Autowired private transient CapitoloDad capitoloDad;
	@Autowired private transient ImportiCapitoloDad importiCapitoloDad;
	
	Map<CapitoloUscitaGestione, List<SubdocumentoSpesa>> findCapitoliBySubdoc;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		//devo avere dei subdocumenti o degli elenchi da cercare
		checkCondition((req.getIdsSubdocumentiSpesa() != null && !req.getIdsSubdocumentiSpesa().isEmpty()) || (req.getIdsElenchi() != null && !req.getIdsElenchi().isEmpty()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista di subdoc"));
		//la ricerca deve essere effettuata per anno di bilancio
		checkEntita(req.getBilancio(), "bilancio");
		checkCondition(req.getBilancio().getAnno() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno di bilancio"));
		
	}
	@Override
	protected void init() {
		importiCapitoloDad.setEnte(ente);
		super.init();
	}
	
	@Transactional
	public ControllaDisponibilitaCassaCapitoloByMovimentoResponse executeService(ControllaDisponibilitaCassaCapitoloByMovimento serviceRequest) {
		return super.executeService(serviceRequest);
	}
	

	@Override
	protected void execute() {		
		
		//carico la mappa capitolo - lista quote
		caricaCapitoliMovimenti();
		
		//controllo la disponibilita'
		controllaDisponibilitaCapitolo();
		
	}

	/**
	 * Controlla la disponibilita dei capitoli rispetto alla lista di subdoc.
	 */
	private void controllaDisponibilitaCapitolo() {
		for(Entry<CapitoloUscitaGestione, List<SubdocumentoSpesa>> mapValue : findCapitoliBySubdoc.entrySet()) {
			//prendo il capitolo
			CapitoloUscitaGestione cap = mapValue.getKey();
			//prendo la lista di subdocumenti
			List<SubdocumentoSpesa> listaSubdocumenti = findCapitoliBySubdoc.get(cap);
			//calcolo la somma degli importi delle quote
			BigDecimal sommaImportiQuote = calcolaSommaImportiQuote(listaSubdocumenti);
			//calcolo la disponibilita' a pagare del capitolo
			BigDecimal disponibilitaPagare = importiCapitoloDad.findImportoDerivato(cap.getUid(), ImportoDerivatoFunctionEnum.disponibilitaPagare);
			//la somma degli importi delle quote NON PUO' essere maggiore della disponibilita' a pagare del capitolo
			if(sommaImportiQuote.compareTo(disponibilitaPagare) >0) {
				//solo quando so che le quote sfondano la disponibilta' del capitolo, ottengo gli altri importi del capitolo che mi servono solo per il messaggio di errore (PERFORMANCE)
				ImportiCapitoloUG importi = importiCapitoloDad.findImportiCapitolo(cap, req.getBilancio().getAnno(), ImportiCapitoloUG.class, EnumSet.noneOf(ImportiCapitoloEnum.class));
				//calcolo la chiave dei movimenti
				String chiaveCapitolo = computeChiaveCapitolo(cap);
				String chiaveSubdocs = computeChiaveSubdoc(listaSubdocumenti);
				//aggiungo l'errore
				res.addErrore(ErroreFin.DISPONIBILITA_INSUFFICIENTE_PER_QUOTE.getErrore(chiaveCapitolo, importi.getStanziamentoCassa(), disponibilitaPagare, chiaveSubdocs));
			}
			
		}
		
	}

	/**
	 * Compute chiave subdoc.
	 *
	 * @param listaSubdocumenti the lista subdocumenti da cui ottenere le chiavi
	 * @return the string la chiave annoDoc/numeroDoc-numQuota, annoDoc/numeroDoc-numQuota  per il subdocumento
	 */
	private String computeChiaveSubdoc(List<SubdocumentoSpesa> listaSubdocumenti) {
		List<String> chunks = new ArrayList<String>();
		
		for (SubdocumentoSpesa subdocumentoSpesa : listaSubdocumenti) {
			//ciclo sui subdocumenti per ottenere la chiave del documento corrispondente
			DocumentoSpesa documento = subdocumentoSpesa.getDocumento();
			StringBuilder sb = new StringBuilder()
					.append(documento.getAnno())
					.append("/")
					.append(documento.getNumero())
					.append("-")
					.append(subdocumentoSpesa.getNumero())
					.append(" , ");
			chunks.add(sb.toString());
		}
		return StringUtils.join(chunks, ", ");
	}

	/**
	 * Compute la chiave capitolo anno /numero
	 *
	 * @param cap il capitolo
	 * @return the string la chiave
	 */
	private String computeChiaveCapitolo(CapitoloUscitaGestione cap) {
		StringBuilder sb = new StringBuilder();
		sb.append(cap.getAnnoCapitolo());
		sb.append("/");
		sb.append(cap.getNumeroCapitolo());
		sb.append("/");
		sb.append(cap.getNumeroArticolo());
		return sb.toString();
	}

	/**
	 * Calcola somma degli importi da pagare di una lista di quote.
	 *
	 * @param la lista di quote di c
	 * @return the big decimal
	 */
	private BigDecimal calcolaSommaImportiQuote(List<SubdocumentoSpesa> list) {
		BigDecimal somma = BigDecimal.ZERO;
		for (SubdocumentoSpesa subdocumentoSpesa : list) {
			//per ogni subdocumento in lista, prendo l'importo da pagare e lo aggiungo
			somma = somma.add(subdocumentoSpesa.getImportoDaPagare());
		}
		return somma;
	}

	/**
	 * Carica  i capitoli a partire da una lista di sub
	 */
	private void caricaCapitoliMovimenti() {
		if(req.getIdsSubdocumentiSpesa() != null && !req.getIdsSubdocumentiSpesa().isEmpty()) {
			//sto ricercando un elenco di subdocumenti: popolo la mappa chiamando il metodo del dad
			this.findCapitoliBySubdoc = capitoloDad.findCapitoliSpesaGestioneBySubdoc(req.getIdsSubdocumentiSpesa());
		}
		if(req.getIdsElenchi() != null && !req.getIdsElenchi().isEmpty()) {
			//sto ricercando un elenco di elenchi: popolo la mappa chiamando il metodo del dad
			this.findCapitoliBySubdoc = capitoloDad.findCapitoliSpesaGestioneByElenco(req.getIdsElenchi());
		}
		
		if(this.findCapitoliBySubdoc == null) {
			//non ho trovato nessun capitolo associato a nessuno dei subdocumenti in request: lancio un'eccezione perche' e' successo qualcosa di brutto
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("capitolo legato al subodcumento"));
		}
		
	}
}
