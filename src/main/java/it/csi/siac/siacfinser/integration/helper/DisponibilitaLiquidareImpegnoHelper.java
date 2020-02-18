/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.helper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;

import it.csi.siac.siaccorser.model.Account;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfinser.CommonUtils;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.integration.dad.ImpegnoOttimizzatoDad;
import it.csi.siac.siacfinser.integration.dad.MovimentoGestioneOttimizzatoDad;
import it.csi.siac.siacfinser.integration.dad.datacontainer.DisponibilitaMovimentoGestioneContainer;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneMovGestDto;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTMovgestTsRepository;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;

public class DisponibilitaLiquidareImpegnoHelper {

	
	private SiacTMovgestTsRepository siacTMovgestTsRepository;
	private MovimentoGestioneOttimizzatoDad<Impegno,SubImpegno> impegnoOttimizzatoDad;
	private CommonDad commonDad;
	private ApplicationContext ac;
	
	public DisponibilitaLiquidareImpegnoHelper(ApplicationContext ac){
		this.ac = ac;
	}
	
	public void init(){
		this.impegnoOttimizzatoDad = ac.getBean(ImpegnoOttimizzatoDad.class);
		this.commonDad = ac.getBean(CommonDad.class);
		this.siacTMovgestTsRepository = ac.getBean(SiacTMovgestTsRepository.class);
	}
	
	public DisponibilitaMovimentoGestioneContainer calcolaDisponibilitaALiquidare(Integer movgestTsI, Integer annoBilancio){
		return CommonUtils.getFirst(calcolaDisponibilitaALiquidare(CommonUtils.toList(movgestTsI), annoBilancio));
	}
	
	public List<DisponibilitaMovimentoGestioneContainer> calcolaDisponibilitaALiquidare(List<Integer> listaInput, Integer annoBilancio){
		
		List<DisponibilitaMovimentoGestioneContainer> disponibili = new ArrayList<DisponibilitaMovimentoGestioneContainer>();
		
		if(listaInput!=null && listaInput.size()>0){
			
			//carico il primo elemento della lista per dedurre l'ente:
			SiacTMovgestTsFin siacTMovgestTs = siacTMovgestTsRepository.findOne(listaInput.get(0));
			int idEnte = siacTMovgestTs.getSiacTEnteProprietario().getEnteProprietarioId();
			
			Ente ente = new Ente();
			ente.setUid(idEnte);
			Richiedente richiedente = new Richiedente();
			richiedente.setAccount(new Account());

			if(annoBilancio == null || annoBilancio == 0) {
				//metto un anno nel futuro perche' cosi lo considera non residuo come default
				//(sempre meglio un valore errato che farlo scoppiare)
				annoBilancio = 2100;
			}
			
			String annoEsercizio = annoBilancio.toString();
			
			DatiOperazioneDto datiOperazioneDto = commonDad.inizializzaDatiOperazione(ente , richiedente , Operazione.RICERCA, annoBilancio);
			
			OttimizzazioneMovGestDto datiOttimizzazione = impegnoOttimizzatoDad.datiOttimizzatiPerCalcolaDispLiquidare(listaInput);
			
			for(Integer it: listaInput){
				DisponibilitaMovimentoGestioneContainer dispLiquidareContainer = impegnoOttimizzatoDad.calcolaDisponibilitaALiquidare(it, datiOperazioneDto , datiOttimizzazione, annoEsercizio);
				disponibili.add(dispLiquidareContainer);
			}
			
		}
		
		return disponibili;
	}
	
	
}
