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

import it.csi.siac.siacbilser.business.service.documentospesa.RicercaSinteticaDocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacintegser.business.service.helper.SoggettoServiceHelper;
import it.csi.siac.siacintegser.business.service.util.converter.IntegMapId;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.RicercaDocumentoSpesa;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.RicercaDocumentoSpesaResponse;
import it.csi.siac.siacintegser.model.base.Ente;
import it.csi.siac.siacintegser.model.integ.DocumentoSpesa;
import it.csi.siac.siacintegser.model.messaggio.MessaggioInteg;
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDocumentoSpesaService  extends BaseRicercaDocumentoService<RicercaDocumentoSpesa, RicercaDocumentoSpesaResponse>
{

	@Autowired private SoggettoServiceHelper soggettoServiceHelper;
	
	@Override
	protected RicercaDocumentoSpesaResponse execute(RicercaDocumentoSpesa ireq)
	{
		RicercaSinteticaDocumentoSpesa req = map(ireq,
				RicercaSinteticaDocumentoSpesa.class, IntegMapId.RicercaDocumentoSpesa_RicercaSinteticaDocumentoSpesa);
		
		req.getDocumentoSpesa().setEnte(ente);
		
		RicercaDocumentoSpesaResponse ires = instantiateNewIRes();
		
		if ( !StringUtils.isEmpty(ireq.getTipoDocumento()) ) {
			TipoDocumento tipoDoc =  ricercaTipoDocumento(ireq.getTipoDocumento(), TipoFamigliaDocumento.SPESA);		
			req.getDocumentoSpesa().setTipoDocumento(tipoDoc);
//			if ( null == tipoDoc ) {
//				addMessaggio(MessaggioInteg.NESSUN_RISULTATO_TROVATO, " tipo documento con codice " + ireq.getTipoDocumento() + " non esiste");
//				return ires;
//			}
		}

		if(!StringUtils.isEmpty(ireq.getCodiceSoggetto())){
			Soggetto soggetto = soggettoServiceHelper.findSoggetto(ireq.getCodiceSoggetto(), richiedente);
			if(null== soggetto){
				addMessaggio(MessaggioInteg.NESSUN_RISULTATO_TROVATO, " tipo soggetto con codice " + ireq.getCodiceSoggetto() + " non esiste");
				return ires;
			}else req.getDocumentoSpesa().setSoggetto(soggetto);
		}
		
				
		RicercaSinteticaDocumentoSpesaResponse res = appCtx.getBean(RicercaSinteticaDocumentoSpesaService.class).executeService(req);
		checkServiceResponse(res);
		
		if(res.getDocumenti()==null || res.getDocumenti().isEmpty()){
			addMessaggio(MessaggioInteg.NESSUN_RISULTATO_TROVATO, " nessun filtro di ricerca soddisfatto");
			return ires;
		}
		
		
		List<DocumentoSpesa> elencoDocumentiSpesa = dozerUtil.mapList(res.getDocumenti(),DocumentoSpesa.class, IntegMapId.ListDocumentiSpesa_IntegDocumentiSpesa);
					
		ires.setEnte(dozerUtil.map(req.getDocumentoSpesa().getEnte(), Ente.class));
		ires.setDocumentiSpesa(elencoDocumentiSpesa);
		ires.setTotaleRisultati(res.getTotaleElementi());

		return ires;
	}
	

}
