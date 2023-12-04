/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.movgest;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.EliminaStoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.EliminaStoricoImpegnoAccertamentoResponse;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.integration.dad.StoricoImpegnoAccertamentoDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoRicercaMovimentoPkDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.PaginazioneSubMovimentiDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.StoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EliminaStoricoImpegnoAccertamentoService extends AbstractBaseService<EliminaStoricoImpegnoAccertamento, EliminaStoricoImpegnoAccertamentoResponse> {
	
	@Autowired
	private StoricoImpegnoAccertamentoDad storicoImpegnoAccertamentoDad;
	@Autowired
	private CommonDad commonDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {	
		checkEntita(req.getStoricoImpegnoAccertamento(), "storico");
		checkEntita(req.getBilancio(), "bilancio");
	}

	
	@Override
	protected void init() {
		bilancio = req.getBilancio();
	}
	
	
	
	@Override
	@Transactional
	public EliminaStoricoImpegnoAccertamentoResponse executeService(EliminaStoricoImpegnoAccertamento serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
	@Override
	public void execute() {
		final String methodName = "RicercaStoricoImpegnoAccertamento : execute()";
		log.debug(methodName, "- Begin");
		DatiOperazioneDto datiOperazioneDto = commonDad.inizializzaDatiOperazione(req.getRichiedente().getAccount().getEnte(), req.getRichiedente(), Operazione.CANCELLAZIONE_LOGICA_RECORD, req.getAnnoBilancio());
		//lo devo fare qui, prima gestisco il record precedente perchè poi non posso piu' trovare il record di doppia gestione
		effettuaOperazioniDoppiaGestione(req.getStoricoImpegnoAccertamento(), datiOperazioneDto);
		storicoImpegnoAccertamentoDad.cancellaStorico(req.getStoricoImpegnoAccertamento(), datiOperazioneDto);
	}
	
	protected void effettuaOperazioniDoppiaGestione(StoricoImpegnoAccertamento storicoImpegniAccertamenti, DatiOperazioneDto datiOperazioneDto) {
		final String methodName = "effettuaOperazioniDoppiaGestione";
		Impegno impegno = storicoImpegnoAccertamentoDad.getTestataImpegnoStoricizzato(storicoImpegniAccertamenti);
		//che cosa orribile, tutto perche' dopo lo stato per la doppia gestione viene preso dal provvedimento
		impegnoOttimizzatoDad.caricaAttoAmministrativoSeNonValorizzato(impegno);
		if(!impegnoOttimizzatoDad.inserireDoppiaGestione(bilancio, impegno, datiOperazioneDto)) {
			log.debug(methodName, "Non applicabile la doppia gestione. Esco. ");
			return;
		}
		String annoBilancioPiuUno = String.valueOf(req.getAnnoBilancio() + 1);
		SubImpegno sub = storicoImpegnoAccertamentoDad.getSubImpegnoStoricizzato(storicoImpegniAccertamenti);
		BigDecimal numerosub = sub != null? sub.getNumeroBigDecimal() : null;
		boolean storicizzazioneSuSub = isStoricizzazioneSuSub(sub);
		Impegno impegnoAnnoBilancioPiuUno = getImpegno(impegno.getAnnoMovimento(), impegno.getNumeroBigDecimal(), numerosub, annoBilancioPiuUno, storicizzazioneSuSub);
		if(impegnoAnnoBilancioPiuUno == null || (storicizzazioneSuSub && !trovatoSubIndicato(impegnoAnnoBilancioPiuUno.getElencoSubImpegni(), sub))) {
			log.debug(methodName, "Non ho un impegno nell'anno di bilancio + 1. Non ribalto il colleghamento.");
			return;
		}
		log.debug(methodName, " Cancello il record di doppia gestione in anno di bilancio +1");
		StoricoImpegnoAccertamento recordCorrispondenteInAnnoBilancioPiuUno = storicoImpegnoAccertamentoDad.ricercaRecordCorrispondenteInAnnoBilancio(req.getStoricoImpegnoAccertamento(), annoBilancioPiuUno);
		if(recordCorrispondenteInAnnoBilancioPiuUno != null && recordCorrispondenteInAnnoBilancioPiuUno.getUid() != 0) {
			storicoImpegnoAccertamentoDad.cancellaStorico(recordCorrispondenteInAnnoBilancioPiuUno, datiOperazioneDto);
		}
	}
	
	/**
	 * @param subimpegni 
	 * @param sub
	 * @return
	 */
	protected boolean trovatoSubIndicato(List<SubImpegno> subImpegni, SubImpegno subImpegnoIndicato) {
		return  subImpegni != null && !subImpegni.isEmpty() || subImpegnoIndicato.getNumeroBigDecimal().compareTo(subImpegni.get(0).getNumeroBigDecimal()) ==0;
	}

	
	/**
	 * @param sub 
	 * @return
	 */
	protected boolean isStoricizzazioneSuSub(SubImpegno sub) {
		return sub != null && sub.getNumeroBigDecimal() != null && sub.getNumeroBigDecimal().signum() ==1;
	}

	
	protected Impegno getImpegno(int annoImpegno, BigDecimal numeroImpegno, BigDecimal numeroSubImpegno, String annoBilancioImpegno, boolean storicizzazioneSuSub) {
		PaginazioneSubMovimentiDto paginazioneSubMovimentiDto = new PaginazioneSubMovimentiDto();
		if(storicizzazioneSuSub) {
			paginazioneSubMovimentiDto.setNumeroSubMovimentoRichiesto(numeroSubImpegno);
		}else {
			paginazioneSubMovimentiDto.setNoSub(true);
		}
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds caricaDatiOpzionaliDto = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		
		EsitoRicercaMovimentoPkDto esitoRicercaMov = impegnoOttimizzatoDad.ricercaMovimentoPk(req.getRichiedente(),	ente, annoBilancioImpegno,
				Integer.valueOf(annoImpegno), numeroImpegno, paginazioneSubMovimentiDto, caricaDatiOpzionaliDto,
				CostantiFin.MOVGEST_TIPO_IMPEGNO, false, true);
		
		
		return (Impegno) esitoRicercaMov.getMovimentoGestione();
	}
	
}