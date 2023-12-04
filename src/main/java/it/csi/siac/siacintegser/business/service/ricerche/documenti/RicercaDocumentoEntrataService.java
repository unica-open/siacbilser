/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.ricerche.documenti;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.documentoentrata.RicercaSinteticaDocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacintegser.business.service.helper.SoggettoServiceHelper;
import it.csi.siac.siacintegser.business.service.util.converter.IntegMapId;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.RicercaDocumentoEntrata;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.RicercaDocumentoEntrataResponse;
import it.csi.siac.siacintegser.model.base.Ente;
import it.csi.siac.siacintegser.model.integ.DocumentoEntrata;
import it.csi.siac.siacintegser.model.messaggio.MessaggioInteg;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDocumentoEntrataService extends
		BaseRicercaDocumentoService<RicercaDocumentoEntrata, RicercaDocumentoEntrataResponse>
{

	@Autowired private SoggettoServiceHelper soggettoServiceHelper;
	
	@Override
	protected RicercaDocumentoEntrataResponse execute(RicercaDocumentoEntrata ireq)
	{
		RicercaSinteticaDocumentoEntrata req = map(ireq,
				RicercaSinteticaDocumentoEntrata.class, IntegMapId.RicercaDocumentoEntrata_RicercaSinteticaDocumentoEntrata);
		
		req.getDocumentoEntrata().setEnte(ente);
		RicercaDocumentoEntrataResponse ires = instantiateNewIRes();
				
		if ( StringUtils.isNotEmpty(ireq.getTipoDocumento()) ) {
			TipoDocumento tipoDoc =  ricercaTipoDocumento(ireq.getTipoDocumento(), TipoFamigliaDocumento.ENTRATA);		
			req.getDocumentoEntrata().setTipoDocumento(tipoDoc);
		}
		
		if(!StringUtils.isEmpty(ireq.getCodiceSoggetto())){
			Soggetto soggetto = soggettoServiceHelper.findSoggetto(ireq.getCodiceSoggetto(), richiedente);
			if(null== soggetto){
				addMessaggio(MessaggioInteg.NESSUN_RISULTATO_TROVATO, " tipo soggetto con codice " + ireq.getCodiceSoggetto() + " non esiste");
				return ires;
			}else req.getDocumentoEntrata().setSoggetto(soggetto);
		}
		
		RicercaSinteticaDocumentoEntrataResponse res = appCtx.getBean(RicercaSinteticaDocumentoEntrataService.class).executeService(req);
		checkServiceResponse(res);
		
		if(res.getDocumenti()==null || res.getDocumenti().isEmpty()){
			addMessaggio(MessaggioInteg.NESSUN_RISULTATO_TROVATO, " nessun filtro di ricerca soddisfatto");
			return ires;
		}
		
		List<DocumentoEntrata> elencoDocumentiEntrata = dozerUtil.mapList(res.getDocumenti(),DocumentoEntrata.class, IntegMapId.ListDocumentiEntrata_IntegDocumentiEntrata);
		
		ires.setEnte(dozerUtil.map(req.getDocumentoEntrata().getEnte(), Ente.class));
		ires.setDocumentiEntrata(elencoDocumentiEntrata);
		ires.setTotaleRisultati(res.getTotaleElementi());

		return ires;
	}





}
