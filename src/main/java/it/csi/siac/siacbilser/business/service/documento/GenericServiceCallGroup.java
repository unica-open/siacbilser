/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documento;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacbilser.business.service.base.ServiceCallGroup;
import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.business.service.base.ServiceInvoker;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siaccommonser.business.service.base.cache.KeyAdapter;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfinser.frontend.webservice.GenericService;
import it.csi.siac.siacfinser.frontend.webservice.msg.Liste;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeResponse;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;

/**
 * The Class GenericServiceCallGroup.
 * 
 * @author Marchino Alessandro
 */
public class GenericServiceCallGroup extends ServiceCallGroup {

	private Ente ente;
	private GenericService genericService;
	
	public GenericServiceCallGroup(ServiceExecutor serviceExecutor, Richiedente richiedente, Ente ente) {
		super(serviceExecutor, richiedente);
		this.ente = ente;
		this.genericService = serviceExecutor.getAppCtx().getBean(Utility.toDefaultBeanName(GenericService.class), GenericService.class);
	}
	
	public ListeResponse listeCached(List<TipiLista> tipiLista, String... codiciErroreDaEscludere){
		Liste req = new Liste();
		
		req.setDataOra(new Date());
		req.setEnte(ente);
		req.setRichiedente(richiedente);
		
		int size = tipiLista.size();
		TipiLista[] tipi = new TipiLista[size];
		for(int i = 0; i < size; i++) {
			tipi[i] = tipiLista.get(i);
		}
		// Ordino i tipi
		Arrays.sort(tipi);
		req.setTipi(tipi);
		
		return se.executeServiceThreadLocalCachedSuccess(new ServiceInvoker<Liste, ListeResponse>() {
			@Override
			public ListeResponse invokeService(Liste r) {
				return genericService.liste(r);
			}
		}, req, new KeyAdapter<Liste>() {
			@Override
			public String computeKey(Liste r) {
				StringBuilder sb = new StringBuilder();
				sb.append(ente.getUid());
				for(TipiLista tl : r.getTipi()) {
					sb.append("_");
					sb.append(tl.name());
				}
				return sb.toString();
			}
		}, 
		codiciErroreDaEscludere);
	}
	
}

