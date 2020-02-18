/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.registroiva;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.documentoivaentrata.RicercaSinteticaSubdocumentoIvaEntrataService;
import it.csi.siac.siacbilser.business.service.documentoivaspesa.RicercaSinteticaSubdocumentoIvaSpesaService;
import it.csi.siac.siacbilser.integration.dad.RegistroIvaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaRegistroIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaSubdocumentoIvaEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaSubdocumentoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

// TODO: Auto-generated Javadoc
/**
 * The Class EliminaRegistroIvaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EliminaRegistroIvaService extends CheckedAccountBaseService<EliminaRegistroIva, EliminaRegistroIvaResponse> {
	
	/** The registro iva dad. */
	@Autowired
	private RegistroIvaDad registroIvaDad;
	
	/** The registro. */
	private RegistroIva registro;
	
	@Autowired
	private RicercaSinteticaSubdocumentoIvaSpesaService ricercaSinteticaSubdocumentoIvaSpesaService;
	
	@Autowired
	private RicercaSinteticaSubdocumentoIvaEntrataService ricercaSinteticaSubdocumentoIvaEntrataService;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		registro = req.getRegistroIva();
		
		checkNotNull(registro, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("registro"));
		checkCondition(registro.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid registro"));
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	@Override
	public EliminaRegistroIvaResponse executeService(EliminaRegistroIva serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		checkRegistroEliminabile();
		registroIvaDad.eliminaRegistroIva(registro);
		res.setRegistroIva(registro);
	}
	
	
	private void checkRegistroEliminabile() {
		
		ParametriPaginazione pp = new ParametriPaginazione();
		pp.setElementiPerPagina(1);
		pp.setNumeroPagina(0);
		
		//cerco eventuali Subdocumenti Iva Spesa collegati
		RicercaSinteticaSubdocumentoIvaSpesa reqRSSIS = new RicercaSinteticaSubdocumentoIvaSpesa();
		reqRSSIS.setRichiedente(req.getRichiedente());
		SubdocumentoIvaSpesa subdocIvaSpesa = new SubdocumentoIvaSpesa();
		subdocIvaSpesa.setEnte(registro.getEnte());
		subdocIvaSpesa.setRegistroIva(registro);
		reqRSSIS.setParametriPaginazione(pp);
		reqRSSIS.setSubdocumentoIvaSpesa(subdocIvaSpesa);
		RicercaSinteticaSubdocumentoIvaSpesaResponse resRSSIS = executeExternalService(ricercaSinteticaSubdocumentoIvaSpesaService, reqRSSIS);
		
		//cerco eventuali Subdocumenti Iva Entrata collegati
		RicercaSinteticaSubdocumentoIvaEntrata reqRSSIE = new RicercaSinteticaSubdocumentoIvaEntrata();
		reqRSSIE.setRichiedente(req.getRichiedente());
		SubdocumentoIvaEntrata subdocIvaEntrata = new SubdocumentoIvaEntrata();
		subdocIvaEntrata.setEnte(registro.getEnte());
		subdocIvaEntrata.setRegistroIva(registro);
		reqRSSIE.setParametriPaginazione(pp);
		reqRSSIE.setSubdocumentoIvaEntrata(subdocIvaEntrata);
		RicercaSinteticaSubdocumentoIvaEntrataResponse resRSSIE = executeExternalService(ricercaSinteticaSubdocumentoIvaEntrataService, reqRSSIE);
		
		//se si trova anche solo un subdoc collegato non e' possibile eliminare il registro
		if( !(resRSSIS.getListaSubdocumentoIvaSpesa().isEmpty() && resRSSIE.getListaSubdocumentoIvaEntrata().isEmpty()) ){
			throw new BusinessException(ErroreFin.CANCELAZIONE_REGISTRO_IVA_IMPOSSIBILE.getErrore());
		}
		

		
		
		
		
		
		
	}
}
