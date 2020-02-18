/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.ricerche.documenti;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacbilser.business.service.documento.RicercaTipoDocumentoService;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumentoResponse;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacintegser.business.service.base.RicercaPaginataBaseService;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.BaseRicercaDocumento;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.BaseRicercaDocumentoResponse;

public abstract class BaseRicercaDocumentoService<IREQ extends BaseRicercaDocumento, IRES extends BaseRicercaDocumentoResponse>
		extends RicercaPaginataBaseService<IREQ, IRES> {

	protected TipoDocumento ricercaTipoDocumento(String codiceTipoDocumento, TipoFamigliaDocumento tipoFamigliaDocumento) {
		
		TipoDocumento tipoDoc = null;
		
		RicercaTipoDocumento reqTipoDocumento = new RicercaTipoDocumento();
		reqTipoDocumento.setEnte(ente);
		reqTipoDocumento.setRichiedente(richiedente);
		reqTipoDocumento.setTipoFamDoc(tipoFamigliaDocumento);
		RicercaTipoDocumentoResponse resTipoDocumento = appCtx.getBean(RicercaTipoDocumentoService.class).executeService(
				reqTipoDocumento);

		for (TipoDocumento tipoDocumento : resTipoDocumento.getElencoTipiDocumento()) {
			if (tipoDocumento.getCodice().equalsIgnoreCase(codiceTipoDocumento)) {
				tipoDoc = tipoDocumento;
			}
		}

		return tipoDoc;
	}

	
	@Override
	protected void checkServiceParameters(BaseRicercaDocumento ireq)
			throws ServiceParamError {

		checkCondition( false 
				|| ( isDefined(ireq.getAnnoDocumento()) && StringUtils.isNotEmpty(ireq.getTipoDocumento()) && ( StringUtils.isNotEmpty(ireq.getNumeroDocumento()) || StringUtils.isNotEmpty(ireq.getCodiceSoggetto()) ) ) 
				|| ( ireq.getNumeroRepertorio() !=null && isDefined(ireq.getAnnoRepertorio()) /*&& ireq.getDataRepertorio() != null*/ )
				,ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		
		if ( isDefined(ireq.getAnnoDocumento()) || StringUtils.isNotEmpty(ireq.getTipoDocumento()) ||  StringUtils.isNotEmpty(ireq.getNumeroDocumento()) || StringUtils.isNotEmpty(ireq.getCodiceSoggetto()) ) {
			// presume che si voglia fare la ricerca per dati del documento
			checkCondition( isDefined(ireq.getAnnoDocumento()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("annoDocumento") );
			checkCondition( StringUtils.isNotEmpty(ireq.getTipoDocumento()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipoDocumento") );
			checkCondition( StringUtils.isNotEmpty(ireq.getNumeroDocumento()) || StringUtils.isNotEmpty(ireq.getCodiceSoggetto()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numeroDocumento o codiceSoggetto") );
			return;
		}

		if ( StringUtils.isNotEmpty(ireq.getNumeroRepertorio()) || isDefined(ireq.getAnnoRepertorio()) /*|| ireq.getDataRepertorio()!=null*/ ) {
			// presume che si voglia fare la ricerca per protocollo, informa i campi no valorizzati
			checkCondition(StringUtils.isNotEmpty(ireq.getNumeroRepertorio()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numeroRepertorio"));
			checkCondition(isDefined(ireq.getAnnoRepertorio()) , ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("annoRepertorio"));
//			checkCondition(ireq.getDataRepertorio() != null , ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("dataRepertorio"));
			return;
		}		
	}
	
	/**
	 * Ritorna true se il numero i è definito, ovvero un qualsiasi valore diverso da zero.
	 * Il metodo puo essere usato per testare quei campi dichiarati come interi che sono valorizzati
	 * con la stringa vuota nella request e che vengono rappesentati  come "zero" nel modello degli oggetti.  
	 * 
	 */
	private boolean isDefined(Integer i) {
		return i != null && i != 0;
	}

}
