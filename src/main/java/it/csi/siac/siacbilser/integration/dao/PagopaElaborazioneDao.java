/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.pagopa.model.Riconciliazione;
import it.csi.siac.pagopa.model.RiconciliazioneDoc;
import it.csi.siac.siacbilser.integration.entity.PagopaTElaborazioneFlusso;
import it.csi.siac.siacbilser.integration.entity.PagopaTRiconciliazione;
import it.csi.siac.siacbilser.integration.entity.PagopaTRiconciliazioneDoc;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

public interface PagopaElaborazioneDao extends Dao<PagopaTElaborazioneFlusso, Integer> {


	Page<PagopaTElaborazioneFlusso> ricercaElaborazioni(Integer enteProprietarioId,
			String numeroProvvisorio, 
			String  flusso,
			Date dataEmissioneDa, 
			Date  dataEmissioneA, 
			Date dataElaborazioneFlussoDa, 
			Date  dataElaborazioneFlussoA,
			String esitoElaborazioneFlussoFE, 
			Pageable pageable);
	
	
	Page<PagopaTRiconciliazione> ricercaRiconciliazioni(Integer enteProprietarioId,
			Riconciliazione riconciliazione,
			Pageable pageable);
	
	
	Page<PagopaTRiconciliazioneDoc> ricercaRiconciliazioniDoc(Integer enteProprietarioId,
			RiconciliazioneDoc riconciliazioneDoc,
			Pageable pageable);
	
	Page<Object[]> ricercaRiconciliazioniConDettagli(Integer enteProprietarioId,  
			RiconciliazioneDoc riconciliazione, 
			Pageable pageable);
	
	List<PagopaTRiconciliazioneDoc> ricercaRiconciliazioniDocByElabFlussoId(Integer enteProprietarioId,
			Integer elabFlussoId);	
	

	//SIAC-8046 Task 2.2 CM 01/04/2021 Inizio
	Integer ricercaAccertamentoInRiconciliazione(Integer enteProprietarioId, String annoEsercizio, 
			RiconciliazioneDoc riconciliazione);
	//SIAC-8046 Task 2.2 CM 01/04/2021 Fine
}
