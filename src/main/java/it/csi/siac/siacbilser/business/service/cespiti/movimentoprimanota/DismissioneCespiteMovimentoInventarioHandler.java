/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti.movimentoprimanota;

import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.integration.dad.DismissioneCespiteDad;
import it.csi.siac.siaccespser.model.DismissioneCespite;
import it.csi.siac.siaccespser.model.DismissioneCespiteModelDetail;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.model.PrimaNota;


/**
 * The Class DismissioneCespiteMovimentoInventarioHandler.
 * @author elisa
 * @version 1.0.0 - 24-10-2018
 */
public class DismissioneCespiteMovimentoInventarioHandler extends MovimentoInventarioHandler<DismissioneCespite> {
	private LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	private DismissioneCespiteDad dismissioneCespiteDad;
	
	private DismissioneCespite dismissioneCespite;
	
	/**
	 * Instantiates a new dismissione cespite movimento inventario handler.
	 *
	 * @param serviceExecutor the service executor
	 * @param richiedente the richiedente
	 * @param ente the ente
	 * @param loginOperazione the login operazione
	 */
	public DismissioneCespiteMovimentoInventarioHandler(ServiceExecutor serviceExecutor, Richiedente richiedente, Ente ente, String loginOperazione) {
		super(serviceExecutor, richiedente, ente, loginOperazione);
		this.dismissioneCespiteDad = serviceExecutor.getAppCtx().getBean(DismissioneCespiteDad.class);
		this.dismissioneCespiteDad.setEnte(ente);
		this.dismissioneCespiteDad.setLoginOperazione(loginOperazione);
	}

	@Override
	public void caricaMovimento(PrimaNota primaNota) {
		this.dismissioneCespite = dismissioneCespiteDad.ottieniDismissioneCespitePrimaNota( primaNota, DismissioneCespiteModelDetail.Stato);
		if(this.dismissioneCespite == null || this.dismissioneCespite.getUid() == 0 ){
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("impossibile reperire la dismissione a cui la prima nota risulta essere collegata."));
		}
	}


	@Override
	public void effettuaOperazioniCollegateADefinizionePrimaNotaInventario(PrimaNota primaNota) {
		//NB: la prima nota di dismissione "va in coppia" con una prima nota di ammortamento residuo.
		//SI e' deciso con analista che la validazione della prima nota collegata sia pero' separata
		final String methodName ="effettuaOperazioniCollegateADefinizionePrimaNotaInventario";
		log.debug(methodName, "Prima nota legata alla dismissione con uid: " + this.dismissioneCespite.getUid() + " , non devo effettuare nessuna operazione.");
		dismissioneCespiteDad.aggiornaStatoDismissioneCespite(this.dismissioneCespite);
		
	}
	
}
