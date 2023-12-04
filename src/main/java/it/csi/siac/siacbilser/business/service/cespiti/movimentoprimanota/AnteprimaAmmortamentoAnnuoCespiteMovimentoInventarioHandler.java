/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti.movimentoprimanota;

import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * The Class CespiteMovimentoInventarioHandler.
 * @author elisa
 * @version 1.0.0 - 24-10-2018
 */
public class AnteprimaAmmortamentoAnnuoCespiteMovimentoInventarioHandler extends MovimentoInventarioHandler<Cespite> {
	
	public AnteprimaAmmortamentoAnnuoCespiteMovimentoInventarioHandler(ServiceExecutor serviceExecutor,	Richiedente richiedente, Ente ente, String loginOperazione) {
		super(serviceExecutor, richiedente, ente, loginOperazione);
		// TODO Auto-generated constructor stub
	}


	private LogSrvUtil log = new LogSrvUtil(this.getClass());

	@Override
	public void caricaMovimento(PrimaNota primaNota) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void effettuaOperazioniCollegateADefinizionePrimaNotaInventario(PrimaNota primaNota) {
		final String methodName ="effettuaOperazioniCollegateADefinizionePrimaNotaInventario";
		log.debug(methodName, "Prima nota legata ad un ammortamento annuo, non devo effettuare nessuna operazione.");
	}

	
}
