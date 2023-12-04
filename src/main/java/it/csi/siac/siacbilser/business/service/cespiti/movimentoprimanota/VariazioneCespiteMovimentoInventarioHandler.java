/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti.movimentoprimanota;

import java.util.Date;

import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.business.service.cespiti.utility.AmmortamentoAnnuoCespiteFactory;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.AmmortamentoAnnuoCespiteDad;
import it.csi.siac.siacbilser.integration.dad.DettaglioAmmortamentoAnnuoCespiteDad;
import it.csi.siac.siacbilser.integration.dad.VariazioneCespiteDad;
import it.csi.siac.siaccespser.model.AmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.CategoriaCespiti;
import it.csi.siac.siaccespser.model.CategoriaCespitiModelDetail;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.CespiteModelDetail;
import it.csi.siac.siaccespser.model.DettaglioAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.StatoVariazioneCespite;
import it.csi.siac.siaccespser.model.TipoBeneCespiteModelDetail;
import it.csi.siac.siaccespser.model.VariazioneCespite;
import it.csi.siac.siaccespser.model.VariazioneCespiteModelDetail;
import it.csi.siac.siaccommon.model.ModelDetailEnum;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.model.PrimaNota;


/**
 * The Class VariazioneCespiteMovimentoInventarioHandler.
 * @author elisa
 * @version 1.0.0 - 24-10-2018
 */
public class VariazioneCespiteMovimentoInventarioHandler extends MovimentoInventarioHandler<VariazioneCespite> {
	private LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	//DADS
	private VariazioneCespiteDad variazioneCespiteDad;
	private DettaglioAmmortamentoAnnuoCespiteDad dettaglioAmmortamentoAnnuoCespiteDad;
	private AmmortamentoAnnuoCespiteDad ammortamentoAnnuoCespiteDad;
	
	//FIELDS
	private VariazioneCespite variazioneCespite;
	private Cespite cespite;
	private CategoriaCespiti categoriaCespiti;
	

	public VariazioneCespiteMovimentoInventarioHandler(ServiceExecutor serviceExecutor, Richiedente richiedente, Ente ente, String loginOperazione) {
		super(serviceExecutor, richiedente, ente, loginOperazione);
		this.variazioneCespiteDad = serviceExecutor.getAppCtx().getBean(VariazioneCespiteDad.class);
		this.dettaglioAmmortamentoAnnuoCespiteDad = serviceExecutor.getAppCtx().getBean(DettaglioAmmortamentoAnnuoCespiteDad.class);
		this.ammortamentoAnnuoCespiteDad = serviceExecutor.getAppCtx().getBean(AmmortamentoAnnuoCespiteDad.class);
		this.variazioneCespiteDad.setEnte(ente);
		this.dettaglioAmmortamentoAnnuoCespiteDad.setEnte(ente);
		this.dettaglioAmmortamentoAnnuoCespiteDad.setLoginOperazione(loginOperazione);
		this.ammortamentoAnnuoCespiteDad.setEnte(ente);
		this.ammortamentoAnnuoCespiteDad.setLoginOperazione(loginOperazione);
	}


	@Override
	public void caricaMovimento(PrimaNota primaNota) {
		Utility.MDTL.addModelDetails(new ModelDetailEnum[] {
				CespiteModelDetail.TipoBeneCespiteModelDetail,
				TipoBeneCespiteModelDetail.CategoriaCespitiModelDetail,
				CategoriaCespitiModelDetail.AliquotaAnnua,
				CategoriaCespitiModelDetail.TipoCalcolo			
			});
		this.variazioneCespite = variazioneCespiteDad.ottieniVariazioneCespitePrimaNota(primaNota, VariazioneCespiteModelDetail.CespiteModelDetail);
		if(this.variazioneCespite == null || this.variazioneCespite.getCespite() == null ||  this.variazioneCespite.getCespite().getTipoBeneCespite() == null) {
			return;
		}
		
		this.cespite = variazioneCespite.getCespite();
		this.categoriaCespiti = cespite.getTipoBeneCespite().getCategoriaCespiti();
	}


	@Override
	public void effettuaOperazioniCollegateADefinizionePrimaNotaInventario(PrimaNota primaNota) {
		
		ricalcolaPianoAmmortamento(this.cespite, this.categoriaCespiti, this.variazioneCespite.getDataVariazione());
		variazioneCespiteDad.aggiornaStatoVariazioneCespite(this.variazioneCespite, StatoVariazioneCespite.DEFINITIVO);
	}


	/**
	 * Ricalcola piano ammortamento.
	 *
	 * @param anno the anno
	 * @param cespite the cespite
	 * @param categoriaCespiti the categoria cespiti
	 * @param dataVariazione the data variazione
	 */
	private void ricalcolaPianoAmmortamento(Cespite cespite, CategoriaCespiti categoriaCespiti, Date dataVariazione) {
		final String methodName ="ricalcolaPianoAmmortamento";
		
		AmmortamentoAnnuoCespiteFactory factoryAmmortamento = new AmmortamentoAnnuoCespiteFactory(cespite, categoriaCespiti, null,new Date(), dataVariazione);
		AmmortamentoAnnuoCespite ammortamentoCespite = ammortamentoAnnuoCespiteDad.cancellaDettagliAmmortamentoSenzaPrimaNotaDefinitiva(cespite.getUid());
		if(ammortamentoCespite == null || ammortamentoCespite.getUid() == 0) {
			log.debug(methodName, "Il cespite non ha mai avuto un piano di ammortamento, non lo ricalcolo: esco.");
			return;
		}
		factoryAmmortamento.elaboraAmmortamento(ammortamentoCespite);
		inserisciDettagli(factoryAmmortamento, ammortamentoCespite);
	}

	
	/**
	 * Inserisci dettagli.
	 *
	 * @param factoryAmmortamento the factory ammortamento
	 * @param ammortamentoCespite the ammortamento cespite
	 */
	private void inserisciDettagli(AmmortamentoAnnuoCespiteFactory factoryAmmortamento,	AmmortamentoAnnuoCespite ammortamentoCespite) {
		if(ammortamentoCespite == null || ammortamentoCespite.getUid() == 0) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("inserimento dettaglio amortamento", "testata ammortamento"));
		}
		for (DettaglioAmmortamentoAnnuoCespite dettaglio : factoryAmmortamento.getDettagliAmmortamentoElaborati()) { 
			dettaglioAmmortamentoAnnuoCespiteDad.inserisciDettaglioAmmortamentoAnnuoCespite(dettaglio, ammortamentoCespite.getUid());
		}
	}
	
}
