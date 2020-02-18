/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaStoricoImpegnoAccertamentoParamDto;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacRMovgestTsStoricoImpAccRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.StoricoImpegnoAccertamentoDao;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsStoricoImpAccFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.mapping.FinMapId;
import it.csi.siac.siacfinser.integration.util.DatiOperazioneUtils;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.StoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaStoricoImpegnoAccertamento;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class StoricoImpegnoAccertamentoDad extends AbstractFinDad {
	
	@Autowired
	private SiacRMovgestTsStoricoImpAccRepository siacRMovgestTsStoricoImpAccRepository;
	@Autowired
	private StoricoImpegnoAccertamentoDao storicoImpegnoAccertamentoDao;
	
	/**
	 * Inserisci storico.
	 *
	 * @param storico the storico
	 * @param datiOperazioneDto the dati operazione dto
	 * @return the storico impegno accertamento
	 */
	public StoricoImpegnoAccertamento inserisciStorico(StoricoImpegnoAccertamento storico, DatiOperazioneDto datiOperazioneDto) {
		SiacRMovgestTsStoricoImpAccFin siacRMovgestTsStoricoImpAcc = creaSiacRMovgestTsStoricoImpAcc(storico, datiOperazioneDto);
		
		siacRMovgestTsStoricoImpAcc.setUid(null);
		siacRMovgestTsStoricoImpAcc = siacRMovgestTsStoricoImpAccRepository.saveAndFlush(siacRMovgestTsStoricoImpAcc);
		
		storico.setUid(siacRMovgestTsStoricoImpAcc.getUid());
		return storico;
	}

	/**
	 * Aggiorna storico.
	 *
	 * @param storico the storico
	 * @param datiOperazioneDto the dati operazione dto
	 * @return the storico impegno accertamento
	 */
	public StoricoImpegnoAccertamento aggiornaStorico(StoricoImpegnoAccertamento storico, DatiOperazioneDto datiOperazioneDto) {
		SiacRMovgestTsStoricoImpAccFin siacRMovgestTsStoricoImpAcc = creaSiacRMovgestTsStoricoImpAcc(storico, datiOperazioneDto);
		siacRMovgestTsStoricoImpAcc.setUid(storico.getUid());
		siacRMovgestTsStoricoImpAcc = siacRMovgestTsStoricoImpAccRepository.saveAndFlush(siacRMovgestTsStoricoImpAcc);
		return storico;
	}
	
	/**
	 * Cancella storico.
	 *
	 * @param storico the storico
	 * @param datiOperazioneDto the dati operazione dto
	 */
	public void cancellaStorico(StoricoImpegnoAccertamento storico, DatiOperazioneDto datiOperazioneDto) {
		SiacRMovgestTsStoricoImpAccFin siacRMovgestTsStoricoImpAcc = siacRMovgestTsStoricoImpAccRepository.findOne(storico.getUid());
		DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRMovgestTsStoricoImpAcc, datiOperazioneDto, siacTAccountRepository);
		siacRMovgestTsStoricoImpAcc = siacRMovgestTsStoricoImpAccRepository.saveAndFlush(siacRMovgestTsStoricoImpAcc);
	}
	
	/**
	 * @param datiOperazioneDto
	 * @param uidImpegno
	 */
	public void cancellaTuttiStoriciCollegatoAllImpegno(DatiOperazioneDto datiOperazioneDto, Integer uidImpegno) {
		final String methodName="cancellaStoriciCollegatoAllImpegno";
		List<SiacRMovgestTsStoricoImpAccFin> storicos = siacRMovgestTsStoricoImpAccRepository.findAllSiacRMovgestTsStoricoImpAccFinByMovgestId(uidImpegno, Arrays.asList("T", "S"));
		if(storicos == null || storicos.isEmpty()) {
			return;
		}
		log.debug(methodName, "Sono presenti degli storici collegati all'impegno, li cancello.");
		for (SiacRMovgestTsStoricoImpAccFin siacRMovgestTsStoricoImpAcc : storicos) {
			DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRMovgestTsStoricoImpAcc, datiOperazioneDto, siacTAccountRepository);
		}
	}
	
	
	/**
	 * Ricerca dettaglio.
	 *
	 * @param storico the storico
	 * @return the storico impegno accertamento
	 */
	public StoricoImpegnoAccertamento ricercaDettaglio(StoricoImpegnoAccertamento storico) {
		SiacRMovgestTsStoricoImpAccFin siacRMovgestTsStoricoImpAcc = siacRMovgestTsStoricoImpAccRepository.findSiacRMovgestTsStoricoImpAccById(storico.getUid());
		return creaStoricoImpegnoAccertamento(siacRMovgestTsStoricoImpAcc);
	}
	
	/**
	 * Ricerca record corrispondente nel bilancio di input. Se viene passata una storicizzazione dell'impegno A nel bilancio n, allora viene cercato il record di storico corrispondente 
	 * per il bilancio passato in input.
	 *
	 * @param storico the storico
	 * @param bilancio the bilancio
	 * @return the storico impegno accertamento
	 */
	public StoricoImpegnoAccertamento ricercaRecordCorrispondenteInAnnoBilancio(StoricoImpegnoAccertamento storico, String annoBilancio) {
		final String methodName ="ricercaRecordCorrispondenteInAnnoBilancio";
		if(storico == null || storico.getUid() == 0 || StringUtils.isBlank(annoBilancio)) {
			log.warn(methodName, "Impossibile cercare il recpord, parametri non forniti.");
			return null;
		}
		SiacRMovgestTsStoricoImpAccFin siacRMovgestTsStoricoImpAcc = storicoImpegnoAccertamentoDao.findSiacRMovgestTsStoricoImpAccCorrispondenteInAnnoBilancio(storico.getUid(), 
				annoBilancio, 
				storico.getSubAccertamento() != null && storico.getSubAccertamento().getUid() != 0);
		return creaStoricoImpegnoAccertamento(siacRMovgestTsStoricoImpAcc);
	}
	
	/**
	 * Calcola numero storico impegno accertamento da estrarre.
	 *
	 * @param ente the ente
	 * @param parametroRicercaStoricoImpegnoAccertamento the parametro ricerca storico impegno accertamento
	 * @return the long
	 */
	public Long calcolaNumeroStoricoImpegnoAccertamentoDaEstrarre(Ente ente, ParametroRicercaStoricoImpegnoAccertamento parametroRicercaStoricoImpegnoAccertamento) {
		RicercaStoricoImpegnoAccertamentoParamDto paramDto = convertiInDto(parametroRicercaStoricoImpegnoAccertamento, null);
		Integer conteggioOrdinativiPagamento = storicoImpegnoAccertamentoDao.contaStorico(ente.getUid(), paramDto);
		//Termino restituendo l'oggetto di ritorno: 
        return Long.valueOf(conteggioOrdinativiPagamento);
	}

	
	/**
	 * Ricerca sintetica storico.
	 *
	 * @param ente the ente
	 * @param prop the prop
	 * @param numeroPagina the numero pagina
	 * @param numeroRisultatiPerPagina the numero risultati per pagina
	 * @return the list
	 */
	public List<StoricoImpegnoAccertamento> ricercaSinteticaStorico(Ente ente, ParametroRicercaStoricoImpegnoAccertamento prop, int numeroPagina, int numeroRisultatiPerPagina){
		List<SiacRMovgestTsStoricoImpAccFin> elencoSiacRMovgestTsStoricoImpAccFin = new ArrayList<SiacRMovgestTsStoricoImpAccFin>();
		List<StoricoImpegnoAccertamento> elencoStorico = new ArrayList<StoricoImpegnoAccertamento>();
		
		RicercaStoricoImpegnoAccertamentoParamDto paramDto = convertiInDto(prop, null);

		elencoSiacRMovgestTsStoricoImpAccFin = storicoImpegnoAccertamentoDao.ricercaStoricoImpegnoAccertamento(ente.getUid(), paramDto, numeroPagina, numeroRisultatiPerPagina);
		if(elencoSiacRMovgestTsStoricoImpAccFin == null || elencoSiacRMovgestTsStoricoImpAccFin.isEmpty()){
			return elencoStorico;
		}
		
		for (SiacRMovgestTsStoricoImpAccFin siacRMovgestTsStoricoImpAcc : elencoSiacRMovgestTsStoricoImpAccFin) {
			elencoStorico.add(creaStoricoImpegnoAccertamento(siacRMovgestTsStoricoImpAcc));
		}

        return elencoStorico;

	}
	
	/**
	 * Count legami storicizzazione.
	 *
	 * @param imp the imp
	 * @param acc the acc
	 * @param subacc the subacc
	 * @param subimp the subimp
	 * @param ente the ente
	 * @return the integer
	 */
	public Integer countLegamiStoricizzazione(Impegno imp, Accertamento acc, SubAccertamento subacc, SubImpegno subimp, Boolean escludiSubimpegni, Bilancio bilancio, Ente ente) {
		RicercaStoricoImpegnoAccertamentoParamDto paramDto = creaDto(imp, acc,  subimp, subacc, escludiSubimpegni, bilancio);
		Integer count = storicoImpegnoAccertamentoDao.contaStorico(ente.getUid(), paramDto);
		return count != null? count : Integer.valueOf(0);
	}
	
	private RicercaStoricoImpegnoAccertamentoParamDto convertiInDto(ParametroRicercaStoricoImpegnoAccertamento prop, Bilancio bilancio) {
		StoricoImpegnoAccertamento storicoImpegnoAccertamento = prop.getStoricoImpegnoAccertamento();
		return creaDto(storicoImpegnoAccertamento.getImpegno(), storicoImpegnoAccertamento.getAccertamento(),  storicoImpegnoAccertamento.getSubImpegno(), storicoImpegnoAccertamento.getSubAccertamento(), prop.getEscludiSubImpegni() , prop.getBilancio());
	}

	/**
	 * @param imp
	 * @param acc
	 * @param subImpegno
	 * @param subAccertamento
	 * @param escludiSubImpegni 
	 * @return
	 */
	private RicercaStoricoImpegnoAccertamentoParamDto creaDto(Impegno imp, Accertamento acc, SubImpegno subImpegno,
			SubAccertamento subAccertamento, Boolean escludiSubImpegni, Bilancio bilancio) {
		RicercaStoricoImpegnoAccertamentoParamDto dto = new RicercaStoricoImpegnoAccertamentoParamDto();
		
		if(imp != null){
			dto.setAnnoImpegno(imp.getAnnoMovimento() != 0? Integer.valueOf(imp.getAnnoMovimento()) : null);
			dto.setNumeroImpegno(imp.getNumero() != null && BigDecimal.ZERO.compareTo(imp.getNumero()) < 0? imp.getNumero() : null);
		}
		
		dto.setNumeroSubImpegno(subImpegno != null && subImpegno.getNumero() != null && BigDecimal.ZERO.compareTo(subImpegno.getNumero()) < 0? subImpegno.getNumero() : null);
		
		if(bilancio != null) {
			dto.setBilancio(bilancio);
		}
		
		if(acc != null){
			dto.setAnnoAccertamento(acc.getAnnoMovimento() != 0? Integer.valueOf(acc.getAnnoMovimento()) : null);
			dto.setNumeroAccertamento(acc.getNumero() != null && BigDecimal.ZERO.compareTo(acc.getNumero()) < 0? acc.getNumero() : null);
		}
		
		dto.setNumeroSubAccertamento(subAccertamento != null && subAccertamento.getNumero() != null && BigDecimal.ZERO.compareTo(subAccertamento.getNumero()) < 0? subAccertamento.getNumero() : null);
		
		dto.setEscludiSubImpegni(escludiSubImpegni);
		
		return dto;
	}

	/**
	 * @param storico
	 * @param datiOperazioneDto
	 * @return
	 */
	private StoricoImpegnoAccertamento creaStoricoImpegnoAccertamento(SiacRMovgestTsStoricoImpAccFin siacRMovgestTsStoricoImpAcc) {
		if(siacRMovgestTsStoricoImpAcc == null) {
			return null;
		}
		StoricoImpegnoAccertamento storico = new StoricoImpegnoAccertamento();
		storico.setUid(siacRMovgestTsStoricoImpAcc.getUid());
		
		if(siacRMovgestTsStoricoImpAcc.getSiacTMovgestT() == null) {
			return storico;
		}
		
		storico.setImpegno(convertiMovimentoGestione(siacRMovgestTsStoricoImpAcc.getSiacTMovgestT().getSiacTMovgest()));
		
		if(!"T".equals(siacRMovgestTsStoricoImpAcc.getSiacTMovgestT().getSiacDMovgestTsTipo().getMovgestTsTipoCode())) {
			storico.setSubImpegno(convertiSubMovimento(siacRMovgestTsStoricoImpAcc.getSiacTMovgestT()));
		}

		Accertamento acc = new Accertamento();
		acc.setAnnoMovimento(siacRMovgestTsStoricoImpAcc.getMovgestAnnoAcc());
		acc.setNumero(siacRMovgestTsStoricoImpAcc.getMovgestNumeroAcc());
		
		storico.setAccertamento(acc);
		
		BigDecimal movgestSubnumeroAcc = siacRMovgestTsStoricoImpAcc.getMovgestSubnumeroAcc();	
		
		if(movgestSubnumeroAcc != null) {
			SubAccertamento subAccertamento = new SubAccertamento();
			subAccertamento.setNumero(movgestSubnumeroAcc);
			storico.setSubAccertamento(subAccertamento);
		}
		
		return storico;
	}

	
	/**
	 * @param storico
	 * @param datiOperazioneDto
	 * @return
	 */
	private SiacRMovgestTsStoricoImpAccFin creaSiacRMovgestTsStoricoImpAcc(StoricoImpegnoAccertamento storico, DatiOperazioneDto datiOperazioneDto) {
		SiacRMovgestTsStoricoImpAccFin siacRMovgestTsStoricoImpAcc = new SiacRMovgestTsStoricoImpAccFin();
		
		SiacTMovgestTsFin siacTMovgestTsFin = new SiacTMovgestTsFin();
		if(storico.getSubImpegno() != null) {
			siacTMovgestTsFin.setUid(storico.getSubImpegno().getUid());
		}else {
			Timestamp now = new Timestamp(System.currentTimeMillis());
			List<SiacTMovgestTsFin> findMovgestTsByMovgest = siacTMovgestTsRepository.findMovgestTsByMovgest(datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId(), now, storico.getImpegno().getUid());
			siacTMovgestTsFin.setUid(findMovgestTsByMovgest.get(0).getUid());
		}

		siacRMovgestTsStoricoImpAcc.setSiacTMovgestT(siacTMovgestTsFin);
		siacRMovgestTsStoricoImpAcc.setMovgestAnnoAcc(storico.getAccertamento().getAnnoMovimento());
		siacRMovgestTsStoricoImpAcc.setMovgestNumeroAcc(storico.getAccertamento().getNumero());
		siacRMovgestTsStoricoImpAcc.setMovgestSubnumeroAcc(storico.getSubAccertamento() != null? storico.getSubAccertamento().getNumero() : null);		
		
		siacRMovgestTsStoricoImpAcc = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRMovgestTsStoricoImpAcc, datiOperazioneDto, siacTAccountRepository);
		return siacRMovgestTsStoricoImpAcc;
	}

	
	protected Impegno convertiMovimentoGestione(SiacTMovgestFin siacTMovgest){
		if(siacTMovgest == null) {
			return null;
		}
		// mappo solo i dati minimi dell'impegno, anno, codice e descrizione
		return map(siacTMovgest, Impegno.class, FinMapId.SiacTMovgest_Impegno);	
	}
	
	protected SubImpegno convertiSubMovimento(SiacTMovgestTsFin siacTMovgestTs){
		if(siacTMovgestTs == null) {
			return null;
		}
		
        return map(siacTMovgestTs, SubImpegno.class, FinMapId.SiacTMovgestTs_SubImpegno);
	}

	/**
	 * Gets the testata impegno storicizzato.
	 *
	 * @param storicoImpegniAccertamenti the storico impegni accertamenti
	 * @return the testata impegno storicizzato
	 */
	public Impegno getTestataImpegnoStoricizzato(StoricoImpegnoAccertamento storicoImpegniAccertamenti) {
		SiacTMovgestFin siacTMovgest = siacRMovgestTsStoricoImpAccRepository.findSiacTMovgestByMovgestTsRStoricoId(storicoImpegniAccertamenti.getUid());
		return convertiMovimentoGestione(siacTMovgest);
	}

	/**
	 * Gets the sub impegno storicizzato.
	 *
	 * @param storicoImpegniAccertamenti the storico impegni accertamenti
	 * @return the sub impegno storicizzato
	 */
	public SubImpegno getSubImpegnoStoricizzato(StoricoImpegnoAccertamento storicoImpegniAccertamenti) {
		SiacTMovgestTsFin siacTMovgestT = siacRMovgestTsStoricoImpAccRepository.findSiacTMovgestTsByMovgestTsRStoricoId(storicoImpegniAccertamenti.getUid());
		return convertiSubMovimento(siacTMovgestT);
	}

}
