/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documento;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.ServiceCallGroup;
import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.business.service.base.ServiceInvoker;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoResponse;
import it.csi.siac.siaccommonser.business.service.base.cache.KeyAdapter;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;


/**
 * The Class ClassificatoreBilServiceCallGroup.
 * 
 * @author Domenico
 */
public class ClassificatoreBilServiceCallGroup extends ServiceCallGroup {

	@Autowired
	private ClassificatoreBilService classificatoreBilService;
	
	private LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoServiceInvoker leggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoServiceInvoker = new LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoServiceInvoker();


	public ClassificatoreBilServiceCallGroup(ServiceExecutor serviceExecutor, Richiedente richiedente) {
		super(serviceExecutor, richiedente);
		
		//processInjectionBasedOnCurrentContext();
		//SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		
		classificatoreBilService = serviceExecutor.getAppCtx().getBean(Utility.toDefaultBeanName(ClassificatoreBilService.class), ClassificatoreBilService.class);
	}
	
	
	public LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoResponse leggiClassificatoreGerarchicoByCodiceAndTipoAndAnno(Integer anno, String codiceClassificatore, TipologiaClassificatore tipologiaClassificatore, String... codiciErroreDaEscludere) {
		
		LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno reqLCG = createRequestLeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno(anno, codiceClassificatore, tipologiaClassificatore);
		
		return se.executeServiceSuccess(leggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoServiceInvoker, reqLCG, codiciErroreDaEscludere);
		
	}
	
	public LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoResponse leggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoCached(Integer anno, String codiceClassificatore, TipologiaClassificatore tipologiaClassificatore) {

		LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno reqLCG = createRequestLeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno(anno, codiceClassificatore, tipologiaClassificatore);

		return se.executeServiceThreadLocalCached(leggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoServiceInvoker, reqLCG,
				new KeyAdapter<LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno>() {
					@Override
					public String computeKey(LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno r) {
						return (r.getClassificatore() != null ? r.getClassificatore().getCodice() : "null" ) 
								+ "_" + r.getAnno()
								+ "_" + r.getTipologiaClassificatore();
					}
				});
		
	}

	private LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno createRequestLeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno(Integer anno, String codiceClassificatore, TipologiaClassificatore tipologiaClassificatore) {
		LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno reqLCG = new LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno();
		reqLCG.setRichiedente(this.richiedente);
		
		reqLCG.setAnno(anno);
		ClassificatoreGerarchico classificatore = new ClassificatoreGerarchico();
		classificatore.setCodice(codiceClassificatore);
		reqLCG.setClassificatore(classificatore );
		
		reqLCG.setTipologiaClassificatore(tipologiaClassificatore);
		
		return reqLCG;
	}
	

	
	//############################ ServiceInvoker Classes ######################################
	
	private class LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoServiceInvoker implements ServiceInvoker<LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno, LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoResponse> {
		@Override
		public LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoResponse invokeService(LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno r) {
			return classificatoreBilService.leggiClassificatoreGerarchicoByCodiceAndTipoAndAnno(r);
		}
	} 
	
	
	
}