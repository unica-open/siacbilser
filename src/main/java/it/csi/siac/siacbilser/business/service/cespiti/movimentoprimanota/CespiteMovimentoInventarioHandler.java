/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti.movimentoprimanota;

import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * The Class CespiteMovimentoInventarioHandler.
 * @author elisa
 * @version 1.0.0 - 24-10-2018
 */
public class CespiteMovimentoInventarioHandler extends MovimentoInventarioHandler<Cespite> {
	
	private LogUtil log = new LogUtil(this.getClass());
	
	/**
	 * Instantiates a new cespite movimento inventario handler.
	 *
	 * @param serviceExecutor the service executor
	 * @param richiedente the richiedente
	 * @param ente the ente
	 */
	public CespiteMovimentoInventarioHandler(ServiceExecutor serviceExecutor, Richiedente richiedente, Ente ente, String loginOperazione) {
		super(serviceExecutor, richiedente, ente, loginOperazione);
	}


	@Override
	public void caricaMovimento(PrimaNota primaNota) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void effettuaOperazioniCollegateADefinizionePrimaNotaInventario(PrimaNota primaNota) {
		final String methodName ="effettuaOperazioniCollegateADefinizionePrimaNotaInventario";
		log.debug(methodName, "Prima nota legata ad un cespite, non devo effettuare nessuna operazione.");
	}

	
}
