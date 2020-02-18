/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.ElencoDocumentiAllegatoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElencoResponse;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;

/**
 * The Class RicercaElencoService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaElencoService extends CheckedAccountBaseService<RicercaElenco,RicercaElencoResponse> {
	
	@Autowired
	private ElencoDocumentiAllegatoDad elencoDocumentiAllegatoDad;
	
	private ElencoDocumentiAllegato elencoDocumentiAllegato;
	private AttoAmministrativo attoAmministrativo;
	
	@Override
	@Transactional(readOnly=true)
	public RicercaElencoResponse executeService(RicercaElenco serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		elencoDocumentiAllegato = req.getElencoDocumentiAllegato();
		attoAmministrativo = req.getAttoAmministrativo();
		checkNotNull(elencoDocumentiAllegato, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elenco"));
		
		checkEntita(elencoDocumentiAllegato.getEnte(), "ente elenco");
		
		checkCondition((attoAmministrativo != null && attoAmministrativo.getUid() != 0) || elencoDocumentiAllegato.getAnno() != null || elencoDocumentiAllegato.getNumero() != null || req.getNumeroDa() != null,
			ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		
//		CR lottoK
//		checkCondition(req.getStatiOperativiElencoDocumenti() != null && !req.getStatiOperativiElencoDocumenti().isEmpty(),
//				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato elenco"));
		
		checkParametriPaginazione(req.getParametriPaginazione());
	}

	@Override
	protected void execute() {
		// Ricerca degli elenchi
		ListaPaginata<ElencoDocumentiAllegato> lista = elencoDocumentiAllegatoDad.ricercaSinteticaElencoDocumentiAllegato(elencoDocumentiAllegato, attoAmministrativo,
				req.getStatiOperativiElencoDocumenti(),req.getNumeroDa(), req.getNumeroA(), req.getParametriPaginazione(), req.getModelDetails());
		res.setElenchiDocumentiAllegato(lista);
	}

}
