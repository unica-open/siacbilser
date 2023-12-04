/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.helper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;

import it.csi.siac.siacfinser.StringUtilsFin;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneModalitaPagamentoDto;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacTNazioneRepository;
import it.csi.siac.siacfinser.integration.entity.SiacTNazioneFin;
import it.csi.siac.siacfinser.model.codifiche.ModalitaAccreditoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.DescrizioneInfoModPagSog;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

public class ModalitaPagamentoSoggettoHelper {

	
	private SiacTNazioneRepository siacTNazioneRepository;
	private ApplicationContext ac;
	
	public ModalitaPagamentoSoggettoHelper(ApplicationContext ac){
		this.ac = ac;
	}
	
	public void init(){
		this.siacTNazioneRepository = ac.getBean(SiacTNazioneRepository.class);
	}
	
	
	public List<ModalitaPagamentoSoggetto> componiDescrizioneArricchita(List<ModalitaPagamentoSoggetto> listaModPags,
			OttimizzazioneModalitaPagamentoDto ottimizzazioneModPag, Integer idEnte){
		if (listaModPags != null && listaModPags.size() > 0) {
			//PRIMA DI AGGIUNGERE LA MOD PAG NELLA LISTA FINALE
			//COSTRUISCO E SETTO LA DESCRIZIONE ARRICCHITA:
			for(ModalitaPagamentoSoggetto modIt: listaModPags){
				if(modIt!=null){
					DescrizioneInfoModPagSog info = componiDescrizioneArricchitaModalitaPagamento(modIt,ottimizzazioneModPag,idEnte);
					modIt.setDescrizioneInfo(info);
				}
			}
			//
		}
		return listaModPags;
	}
	
	/**
	 * Compone la descrizione da visualizare composta da tutta una serie di dati secondo le 
	 * nuove regole indicate nella  SIAC-5156 FIN - CHG 757 -
	 *  Completamento dettaglio modalita' di pagamento - cessioni e numero d'ordine
	 * @param modPagDef
	 * @return
	 */
	public DescrizioneInfoModPagSog componiDescrizioneArricchitaModalitaPagamento(ModalitaPagamentoSoggetto modPagDef,
			OttimizzazioneModalitaPagamentoDto ottimizzazioneModPag, Integer idEnte) {
		
		DescrizioneInfoModPagSog info = new DescrizioneInfoModPagSog();
		
		List<String> tmp = new ArrayList<String>();
		
		//LEGGO LE VARIABILI COINVOLTE:
		String iban = modPagDef.getIban();
		String bic = modPagDef.getBic();
		String contoCorrente = modPagDef.getContoCorrente();
		String intestazioneConto = modPagDef.getIntestazioneConto();
		Soggetto soggettoCessione = modPagDef.getSoggettoCessione();
		ModalitaAccreditoSoggetto modalitaAccreditoSoggetto = modPagDef.getModalitaAccreditoSoggetto();
		String soggettoQuietanzante = modPagDef.getSoggettoQuietanzante();
		String codiceFiscaleQuietanzante = modPagDef.getCodiceFiscaleQuietanzante();
		String dataNascitaQuietanzante = modPagDef.getDataNascitaQuietanzante();
		String luogoNascitaQuietanzante = modPagDef.getLuogoNascitaQuietanzante();
		String statoNascitaQuietanzante = modPagDef.getStatoNascitaQuietanzante();
		ModalitaPagamentoSoggetto modalitaPagamentoSoggettoCessione2 = modPagDef.getModalitaPagamentoSoggettoCessione2();
		String cessioneCodSoggetto = modPagDef.getCessioneCodSoggetto();
		
		
		//(codice - descrizione)
		//per ora non lo metto, su indicazione si silvia sembra ridondante aggiungerlo
//		if(siacTSoggettoFin!=null){
//			String s = siacTSoggettoFin.getSoggettoCode();
//			s += " - " + siacTSoggettoFin.getSoggettoDesc();
//			tmp.add(s);
//		}
		
		//Soggetto ricevente <codice>-<ragione sociale>
		if(soggettoCessione!=null && org.apache.commons.lang.StringUtils.isNotBlank(soggettoCessione.getDenominazione())){
			String s = "Soggetto ricevente : "+ soggettoCessione.getCodiceSoggetto();
			if (org.apache.commons.lang.StringUtils.isNotBlank(soggettoCessione.getDenominazione())){
				s += " - " + soggettoCessione.getDenominazione();
			}
			tmp.add(s);
		}
		
		//Tipo Accredito <..> 
		if (modalitaAccreditoSoggetto != null){
			String s = "Tipo accredito: "+ modalitaAccreditoSoggetto.getCodice();
			if (org.apache.commons.lang.StringUtils.isNotBlank(modalitaAccreditoSoggetto.getDescrizione())){
				s += " - " + modalitaAccreditoSoggetto.getDescrizione();
			}
			tmp.add(s);
		}
		
		//iban: <..> - bic <..> - conto <..> - intestato a <..>
		if (org.apache.commons.lang.StringUtils.isNotBlank(iban)){
			tmp.add("IBAN: " + iban);
		} else {
			//FIX per JIRA SIAC-5367
			//(risolve anche la SIAC-5433)
			// Nel caso di Cessioni nell'elenco delle modalita di pagamento del soggetto, 
			//non e' completa la descrizione ad esempio se il ricevente e' lo stesso
			//di piu' cessioni manca l'Iban e quindi e' impossibile riconoscerle.
			if(modalitaPagamentoSoggettoCessione2!=null &&
					org.apache.commons.lang.StringUtils.isNotBlank(modalitaPagamentoSoggettoCessione2.getIban())){
				tmp.add("IBAN: " + modalitaPagamentoSoggettoCessione2.getIban());
			}
		}
		if (org.apache.commons.lang.StringUtils.isNotBlank(bic)){
			tmp.add("BIC: " + bic);
		}
		if (org.apache.commons.lang.StringUtils.isNotBlank(contoCorrente)){
			String s = "Conto: " + contoCorrente;
			if (org.apache.commons.lang.StringUtils.isNotBlank(intestazioneConto)){
				s += " intestato a " + intestazioneConto;
			}
			tmp.add(s);
		}
		
		//- quietanzante:  <..> -  CF: <..> - nato il <..>   a < comune e nazione>
		if (org.apache.commons.lang.StringUtils.isNotBlank(soggettoQuietanzante)){
			String s = "Quietanzante: " + soggettoQuietanzante;

			if (org.apache.commons.lang.StringUtils.isNotBlank(codiceFiscaleQuietanzante)){
				s += " (CF: " + codiceFiscaleQuietanzante + ")";
			}

			if (org.apache.commons.lang.StringUtils.isNotBlank(dataNascitaQuietanzante)){
				s += ", nato il " + dataNascitaQuietanzante;
			}

			if (org.apache.commons.lang.StringUtils.isNotBlank(luogoNascitaQuietanzante)){
				s += " a " + luogoNascitaQuietanzante;
			}
			
			if(org.apache.commons.lang.StringUtils.isNotBlank(statoNascitaQuietanzante)){
				String nomeNazione = risolviDescrizioneNazione(statoNascitaQuietanzante, ottimizzazioneModPag, idEnte);
				s += ", " + nomeNazione;
			}

			tmp.add(s);
		}


		String descrizioneArricchita = org.apache.commons.lang.StringUtils.join(tmp, " - ");
		
		info.setDescrizioneArricchita(descrizioneArricchita);
		
		return info;
	}
	
	
	/**
	 * Risolve la descrizione di una nazione a partire dal codice nazione
	 * indicato
	 * @param nazioneCode
	 * @param ottimizzazioneSoggDto
	 * @param idEnte
	 * @return
	 */
	private String risolviDescrizioneNazione(String nazioneCode,OttimizzazioneModalitaPagamentoDto ottimizzazioneModPag, Integer idEnte){
		String descStato = null;
		SiacTNazioneFin nazione = null;
		//FIX PER RECUPERARE IL NOME
		if(ottimizzazioneModPag!=null && !StringUtilsFin.isEmpty(ottimizzazioneModPag.getDistintiSiacTNazioneFinCoinvolti()) ){
			//RAMO OTTIMIZZATO
			 nazione = ottimizzazioneModPag.findSiacTNazioneFinCode(nazioneCode);
		} else {
			//RAMO CLASSICO
			nazione =  siacTNazioneRepository.findByCodice(nazioneCode, idEnte);
		}
		if(nazione!=null){
			descStato = nazione.getNazioneDesc();
		} else {
			descStato = nazioneCode;
		}
		return descStato;
	}
	
}
