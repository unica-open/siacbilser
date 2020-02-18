/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documento;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilser.business.service.base.ServiceCallGroup;
import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.business.service.provvedimento.RicercaProvvedimentoService;
import it.csi.siac.siacbilser.business.service.provvedimento.TipiProvvedimentoService;
import it.csi.siac.siaccommonser.business.service.base.cache.KeyAdapter;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;

/**
 * Insieme delle chiamate dei serivizi coinvolti nella gestione del provvedimento.  
 * 
 * @author Domenico
 *
 */
public class ProvvedimentoServiceCallGroup extends ServiceCallGroup {
	
	private Ente ente;
	private Map<String, AttoAmministrativo> attoAmministrativoCache = new HashMap<String, AttoAmministrativo>(); //Ho un'istanza per thread.

	public ProvvedimentoServiceCallGroup(ServiceExecutor serviceExecutor, Richiedente richiedente, Ente ente) {
		super(serviceExecutor, richiedente);
		this.ente = ente;
	}

	public List<AttoAmministrativo> ricercaProvvedimento(AttoAmministrativo attoAmministrativo) {
		RicercaProvvedimento reqRP = new RicercaProvvedimento();
		reqRP.setRichiedente(richiedente);
		reqRP.setEnte(ente);

		RicercaAtti ricercaAtti = new RicercaAtti();
		ricercaAtti.setAnnoAtto(attoAmministrativo.getAnno());
		ricercaAtti.setNumeroAtto(attoAmministrativo.getNumero());
		ricercaAtti.setTipoAtto(attoAmministrativo.getTipoAtto());
		ricercaAtti.setStrutturaAmministrativoContabile(attoAmministrativo.getStrutturaAmmContabile());

		reqRP.setRicercaAtti(ricercaAtti);

		RicercaProvvedimentoResponse resRP = se.executeServiceSuccess(RicercaProvvedimentoService.class, reqRP);
		
		return resRP.getListaAttiAmministrativi();
	}
	
	public AttoAmministrativo ricercaProvvedimentoSingolo(AttoAmministrativo attoAmministrativo) {
		List<AttoAmministrativo> listaAttiAmministrativi = ricercaProvvedimento(attoAmministrativo);

		int size = listaAttiAmministrativi.size();
		if (size > 1) {
			throw new BusinessException("Trovati "+size+" provvedimenti per "+attoAmministrativo.getAnno()+"/" +attoAmministrativo.getNumero()+ (attoAmministrativo.getTipoAtto()!=null?"("+attoAmministrativo.getTipoAtto().getCodice()+")":"") + " . Deve essercene uno solo.");
		}
		if(size==0){
			return null;
		}

		return listaAttiAmministrativi.get(0);
	}
	
	private String computeKey(AttoAmministrativo attoAmministrativo) {
		return attoAmministrativo.getAnno() + "/" + attoAmministrativo.getNumero() + "/" + attoAmministrativo.getTipoAtto();
	}

	public AttoAmministrativo ricercaProvvedimentoSingoloCached(AttoAmministrativo attoAmministrativo) {
		String key = computeKey(attoAmministrativo);
		if (!attoAmministrativoCache.containsKey(key)) {
			AttoAmministrativo attoAmministrativoTrovato = ricercaProvvedimentoSingolo(attoAmministrativo);
			attoAmministrativoCache.put(key, attoAmministrativoTrovato);
			return attoAmministrativoTrovato;
		}
		return attoAmministrativoCache.get(key);
	}
	
	
	public TipiProvvedimentoResponse tipiProvvedimento() {
		TipiProvvedimento reqTP = new TipiProvvedimento();
		reqTP.setEnte(ente);
		reqTP.setRichiedente(richiedente);
		
		return se.executeServiceSuccess(TipiProvvedimentoService.class, reqTP);
	}
	
	public TipiProvvedimentoResponse tipiProvvedimentoCached() {
		TipiProvvedimento reqLCT = new TipiProvvedimento();
		reqLCT.setEnte(ente);
		reqLCT.setRichiedente(richiedente);
		
		return se.executeServiceThreadLocalCachedSuccess(TipiProvvedimentoService.class, reqLCT, new KeyAdapter<TipiProvvedimento>() {
			@Override
			public String computeKey(TipiProvvedimento r) {
				return ""+r.getEnte().getUid();
			}
		});
		
	}

	

}
