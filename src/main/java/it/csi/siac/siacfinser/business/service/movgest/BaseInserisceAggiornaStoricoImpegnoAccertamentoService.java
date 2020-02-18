/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.movgest;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.BaseInserisceAggiornaStoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.BaseInserisceAggiornaStoricoImpegnoAccertamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.integration.dad.StoricoImpegnoAccertamentoDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoRicercaMovimentoPkDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.PaginazioneSubMovimentiDto;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.StoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;

/**
 * @author elisa
 * @version 1.0.0 - 23-07-2019
 *
 * @param <REQIA> the generic type
 * @param <RESIA> the generic type
 */
public abstract class BaseInserisceAggiornaStoricoImpegnoAccertamentoService<REQIA extends BaseInserisceAggiornaStoricoImpegnoAccertamento, RESIA extends BaseInserisceAggiornaStoricoImpegnoAccertamentoResponse> extends AbstractBaseService<REQIA, RESIA> {
	
	@Autowired
	protected StoricoImpegnoAccertamentoDad storicoImpegnoAccertamentoDad;
	@Autowired
	protected CommonDad commonDad;
	
	private Impegno impegno;
	private SubImpegno subImpegno;
	
	
	/**
	 * @throws ServiceParamError
	 */
	protected void checkDatiBase() throws ServiceParamError {
		checkNotNull(req.getStoricoImpegnoAccertamento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		//dati di input presi da request:
		impegno = req.getStoricoImpegnoAccertamento().getImpegno();
		subImpegno = req.getStoricoImpegnoAccertamento().getSubImpegno();
		Accertamento accertamento = req.getStoricoImpegnoAccertamento().getAccertamento();
		SubAccertamento subAccertamento = req.getStoricoImpegnoAccertamento().getSubAccertamento();
		
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		
		checkNotNull(impegno, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("impegno non valorizzato"));
		checkNotNull(accertamento, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("accertamento non valorizzato"));
		
		checkCondition(impegno.getAnnoMovimento() != 0 && impegno.getNumero() != null && impegno.getNumero().signum() == 1, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Anno/numero impegno"));
		checkCondition(subImpegno == null || subImpegno.getNumero() == null || subImpegno.getNumero().signum() == 1, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero subimpegno"));

		
		checkCondition(accertamento.getAnnoMovimento() != 0 && accertamento.getNumero() != null && accertamento.getNumero().signum() == 1, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Anno/numero accertamento"));
		checkCondition(subAccertamento == null || subAccertamento.getNumero() == null || subAccertamento.getNumero().signum() == 1, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero subimpegno"));
	}

	
	/**
	 * @param storicoImpegniAccertamenti
	 */
	protected void caricaDatiImpegnoSubimpegno(StoricoImpegnoAccertamento storicoImpegniAccertamenti) {
		
		BigDecimal numero = subImpegno != null? subImpegno.getNumero() : null;
		
		impegno = getImpegno(impegno.getAnnoMovimento(), impegno.getNumero(), numero, String.valueOf(req.getBilancio().getAnno()));
		
		if(impegno == null || (isStoricizzazioneSuSub(subImpegno) && !trovatoSubIndicato(impegno.getElencoSubImpegni(), subImpegno))) {
			String msg = impegno == null? "Impossibile reperire l'impegno " : "Impossibile reperire il subimpegno"; 
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore(msg));
		}
		
		subImpegno = impegno.getElencoSubImpegni() != null? impegno.getElencoSubImpegni().get(0) : null;
		
		storicoImpegniAccertamenti.setImpegno(impegno);
		storicoImpegniAccertamenti.setSubImpegno(subImpegno);
	}
	
	protected abstract void salvaRecordInDoppiaGestione(StoricoImpegnoAccertamento storicoResiduo, String annoBilancioPiuUno, DatiOperazioneDto datiOperazioneDto);

	/**
	 * @param numeroImpegno 
	 * @param annoImpegno 
	 * @param annoBilancioImpegno
	 */
	protected Impegno getImpegno(int annoImpegno, BigDecimal numeroImpegno, BigDecimal numeroSubImpegno, String annoBilancioImpegno) {
		PaginazioneSubMovimentiDto paginazioneSubMovimentiDto = new PaginazioneSubMovimentiDto();
		if(isStoricizzazioneSuSub(subImpegno)) {
			paginazioneSubMovimentiDto.setNumeroSubMovimentoRichiesto(numeroSubImpegno);
		}else {
			paginazioneSubMovimentiDto.setNoSub(true);
		}
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds caricaDatiOpzionaliDto = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		
		EsitoRicercaMovimentoPkDto esitoRicercaMov = impegnoOttimizzatoDad.ricercaMovimentoPk(req.getRichiedente(),	ente, annoBilancioImpegno,
				Integer.valueOf(annoImpegno), numeroImpegno, paginazioneSubMovimentiDto, caricaDatiOpzionaliDto, Constanti.MOVGEST_TIPO_IMPEGNO, false);
		
		
		return (Impegno) esitoRicercaMov.getMovimentoGestione();
	}
	
	protected void checkImpegnoAccertamento(Impegno impegno, Accertamento accertamento) {
		if(impegno.getAnnoMovimento() <accertamento.getAnnoMovimento()){
			//Gli accertamenti legati a un impegno NON possono avere anno maggiore dell’anno impegno.
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("l'anno dell'accertamento risulta essere maggiore di quello dell'impegno."));
		}
	}

	
	private boolean isDoppiaGestione(DatiOperazioneDto datiOperazioneDto) {
		return impegnoOttimizzatoDad.inserireDoppiaGestione(bilancio, impegno, datiOperazioneDto);
	}
	
	/**
	 * @param storicoImpegniAccertamenti
	 * @param datiOperazioneDto
	 */
	protected void effettuaOperazioniDoppiaGestione(StoricoImpegnoAccertamento storicoImpegniAccertamenti, DatiOperazioneDto datiOperazioneDto) {
		final String methodName = "effettuaOperazioniDoppiaGestione";
		if(!isDoppiaGestione(datiOperazioneDto)) {
			log.debug(methodName, "Non applicabile la doppia gestione. Esco. ");
			return;
		}
		String annoBilancioPiuUno = String.valueOf(req.getBilancio().getAnno() + 1);
		BigDecimal numerosub = storicoImpegniAccertamenti.getSubImpegno() != null? storicoImpegniAccertamenti.getSubImpegno().getNumero() : null;
		Impegno impegnoAnnoBilancioPiuUno = getImpegno(storicoImpegniAccertamenti.getImpegno().getAnnoMovimento(), storicoImpegniAccertamenti.getImpegno().getNumero(), numerosub, annoBilancioPiuUno);
		boolean storicizzazioneSuSub = isStoricizzazioneSuSub(storicoImpegniAccertamenti.getSubImpegno());
		if(impegnoAnnoBilancioPiuUno == null || (storicizzazioneSuSub && !trovatoSubIndicato(impegnoAnnoBilancioPiuUno.getElencoSubImpegni(), storicoImpegniAccertamenti.getSubImpegno()))) {
			log.debug(methodName, "Non ho un impegno nell'anno di bilancio + 1. Non ribalto il colleghamento.");
			return;
		}
		log.debug(methodName, " Gestisco il record di doppia gestione in anno di bilancio +1");
		StoricoImpegnoAccertamento storicoAnnoBilancioPiuUno = new StoricoImpegnoAccertamento();
		storicoAnnoBilancioPiuUno.setImpegno(impegnoAnnoBilancioPiuUno);
		storicoAnnoBilancioPiuUno.setSubImpegno(storicizzazioneSuSub? impegnoAnnoBilancioPiuUno.getElencoSubImpegni().get(0) : null);
		storicoAnnoBilancioPiuUno.setAccertamento(storicoImpegniAccertamenti.getAccertamento());
		storicoAnnoBilancioPiuUno.setSubAccertamento(storicoImpegniAccertamenti.getSubAccertamento());
		salvaRecordInDoppiaGestione(storicoAnnoBilancioPiuUno, annoBilancioPiuUno, datiOperazioneDto);
	}

	
	/**
	 * @param sub 
	 * @return
	 */
	protected boolean isStoricizzazioneSuSub(SubImpegno sub) {
		return sub != null && sub.getNumero() != null && sub.getNumero().signum() ==1;
	}


	/**
	 * @param subimpegni 
	 * @param sub
	 * @return
	 */
	protected boolean trovatoSubIndicato(List<SubImpegno> subImpegni, SubImpegno subImpegnoIndicato) {
		return  subImpegni != null && !subImpegni.isEmpty() || subImpegnoIndicato.getNumero().compareTo(subImpegni.get(0).getNumero()) ==0;
	}

}