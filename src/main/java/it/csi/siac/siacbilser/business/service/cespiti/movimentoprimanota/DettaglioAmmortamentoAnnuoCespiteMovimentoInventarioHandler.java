/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti.movimentoprimanota;

import java.util.List;

import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.integration.dad.AmmortamentoAnnuoCespiteDad;
import it.csi.siac.siacbilser.integration.dad.DettaglioAmmortamentoAnnuoCespiteDad;
import it.csi.siac.siaccespser.model.AmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.DettaglioAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.DettaglioAmmortamentoAnnuoCespiteModelDetail;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * RateoMovimentoHandler.
 * 
 * @author Domenico
 */
public class DettaglioAmmortamentoAnnuoCespiteMovimentoInventarioHandler extends MovimentoInventarioHandler<AmmortamentoAnnuoCespite> {
	private LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	private DettaglioAmmortamentoAnnuoCespiteDad dettaglioAmmortamentoAnnuoCespiteDad;
	private AmmortamentoAnnuoCespiteDad ammortamentoAnnuoCespiteDad;
	
	//FIELDS
	private List<DettaglioAmmortamentoAnnuoCespite> dettagliAmmortamentoAnnuoCespite;
	

	/**
	 * Instantiates a new dettaglio ammortamento annuo cespite movimento inventario handler.
	 *
	 * @param serviceExecutor the service executor
	 * @param richiedente the richiedente
	 * @param ente the ente
	 * @param loginOperazione the login operazione
	 */
	public DettaglioAmmortamentoAnnuoCespiteMovimentoInventarioHandler(ServiceExecutor serviceExecutor, Richiedente richiedente, Ente ente, String loginOperazione) {
		super(serviceExecutor, richiedente, ente, loginOperazione);
		this.dettaglioAmmortamentoAnnuoCespiteDad = serviceExecutor.getAppCtx().getBean(DettaglioAmmortamentoAnnuoCespiteDad.class);
		this.dettaglioAmmortamentoAnnuoCespiteDad.setEnte(ente);
		this.dettaglioAmmortamentoAnnuoCespiteDad.setLoginOperazione(loginOperazione);
		this.ammortamentoAnnuoCespiteDad = serviceExecutor.getAppCtx().getBean(AmmortamentoAnnuoCespiteDad.class);
		this.ammortamentoAnnuoCespiteDad.setEnte(ente);
		this.ammortamentoAnnuoCespiteDad.setLoginOperazione(loginOperazione);
	}


	@Override
	public void caricaMovimento(PrimaNota primaNota) {
		//l'ammortamento annuo puo' portare alla creazione di una prima nota di ammortamento che comprende piu' cespiti e quindi collegata a piu' ammortamenti dettagli
		this.dettagliAmmortamentoAnnuoCespite = dettaglioAmmortamentoAnnuoCespiteDad.caricaDettagliAmmortamentoByPrimaNota(primaNota, DettaglioAmmortamentoAnnuoCespiteModelDetail.AmmortamentoAnnuoCespiteModelDetail);
		if(this.dettagliAmmortamentoAnnuoCespite == null || this.dettagliAmmortamentoAnnuoCespite.isEmpty()) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("la prima nota risulta essere collegata ad una dismissione ma non ci sono scritture associate."));
		}
	}


	@Override
	public void effettuaOperazioniCollegateADefinizionePrimaNotaInventario(PrimaNota primaNota) {
		final String methodName ="effettuaOperazioniCollegateADefinizionePrimaNotaInventario";
		log.debug(methodName, "Prima nota legata ad un ammortamento.");
		
		for (DettaglioAmmortamentoAnnuoCespite dettaglioAmmortamentoAnnuoCespite : dettagliAmmortamentoAnnuoCespite) {
			dettaglioAmmortamentoAnnuoCespiteDad.aggiornaDatiPrimaNotaDefinitiva(dettaglioAmmortamentoAnnuoCespite, primaNota);
			gestisciEventualiAltriAmmortamentiDelloStessoAnno(dettaglioAmmortamentoAnnuoCespite);
			ammortamentoAnnuoCespiteDad.aggiornaDatiRegistrazioneDefinitivaTestataAmmortamento(dettaglioAmmortamentoAnnuoCespite.getAmmortamentoAnnuoCespite());
		}
	}


	/**
	 * Gestisci eventuali altri ammortamenti dellostesso anno.
	 *
	 * @param dettaglioAmmortamentoAnnuoCespite the dettaglio ammortamento annuo cespite
	 */
	private void gestisciEventualiAltriAmmortamentiDelloStessoAnno(DettaglioAmmortamentoAnnuoCespite dettaglioAmmortamentoAnnuoCespite) {
		final String methodName = "gestisciEventualiAltriAmmortamentiDellostessoAnno";
		List<DettaglioAmmortamentoAnnuoCespite> ammortamentiConStessoAnno = dettaglioAmmortamentoAnnuoCespiteDad.caricaDettagliAmmortamentoStessoAnnoDettaglio(dettaglioAmmortamentoAnnuoCespite);
		if(ammortamentiConStessoAnno == null || ammortamentiConStessoAnno.isEmpty()) {
			log.debug(methodName, "non vi sono altri ammortamenti per l'anno. Esco.");
			return;
		}
		log.debug(methodName, "Esistono dei dettagli di ammortamento dello stesso anno. Numero dettagli con lo stesso anno: " + ammortamentiConStessoAnno.size());
		for (DettaglioAmmortamentoAnnuoCespite dettagliStessoAnno : ammortamentiConStessoAnno) {
			dettaglioAmmortamentoAnnuoCespiteDad.eliminaDettaglioAmmortamentoAnnuoCespite(dettagliStessoAnno);
		}
	}

}
