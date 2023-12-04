/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.utility.mutuo.MutuoUtil;
import it.csi.siac.siacbilser.business.utility.mutuo.filter.RataMutuoNonScadutaFilter;
import it.csi.siac.siacbilser.business.utility.mutuo.helper.CalcolaRateMutuoHelper;
import it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.SiacRMutuoMovgestTAccertamentoAssociatoMutuoMapper;
import it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.SiacRMutuoMovgestTImpegnoAssociatoMutuoMapper;
import it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.SiacTMovgestAccertamentoMapper;
import it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.SiacTMovgestImpegnoMapper;
import it.csi.siac.siacbilser.integration.dad.mapper.mutuo.MutuoSiacTMutuoMapper;
import it.csi.siac.siacbilser.integration.dad.mapper.mutuo.RataMutuoSiacTMutuoRataMapper;
import it.csi.siac.siacbilser.integration.dad.mapper.mutuo.RipartizioneMutuoSiacRMutuoRipartizioneMapper;
import it.csi.siac.siacbilser.integration.dad.mapper.mutuo.SiacDMutuoPeriodoRimborsoPeriodoRimborsoMutuoMapper;
import it.csi.siac.siacbilser.integration.dad.mapper.mutuo.SiacTMutuoMutuoBaseMapper;
import it.csi.siac.siacbilser.integration.dad.mapper.mutuo.SiacTMutuoRataRataMutuoMapper;
import it.csi.siac.siacbilser.integration.dad.mapper.mutuo.StatoMutuoSiacDMutuoStatoMapper;
import it.csi.siac.siacbilser.integration.dad.mapper.mutuo.VariazioneMutuoSiacTMutuoVariazioneMapper;
import it.csi.siac.siacbilser.integration.dad.mapper.progetto.SiacTProgrammaProgettoMapper;
import it.csi.siac.siacbilser.integration.dad.mapper.util.MapperDecoratorHelper;
import it.csi.siac.siacbilser.integration.dao.MovimentoGestioneDao;
import it.csi.siac.siacbilser.integration.dao.ProgettoDao;
import it.csi.siac.siacbilser.integration.dao.mutuo.SiacDMutuoPeriodoRimborsoDao;
import it.csi.siac.siacbilser.integration.dao.mutuo.SiacRMutuoMovgestTDao;
import it.csi.siac.siacbilser.integration.dao.mutuo.SiacRMutuoProgrammaDao;
import it.csi.siac.siacbilser.integration.dao.mutuo.SiacRMutuoRipartizioneDao;
import it.csi.siac.siacbilser.integration.dao.mutuo.SiacSMutuoStoricoRepository;
import it.csi.siac.siacbilser.integration.dao.mutuo.SiacTMutuoDao;
import it.csi.siac.siacbilser.integration.dao.mutuo.SiacTMutuoNumRepository;
import it.csi.siac.siacbilser.integration.dao.mutuo.SiacTMutuoRataDao;
import it.csi.siac.siacbilser.integration.dao.mutuo.SiacTMutuoRataRepository;
import it.csi.siac.siacbilser.integration.dao.mutuo.SiacTMutuoRepository;
import it.csi.siac.siacbilser.integration.dao.mutuo.SiacTMutuoVariazioneDao;
import it.csi.siac.siacbilser.integration.entity.SiacDMutuoPeriodoRimborso;
import it.csi.siac.siacbilser.integration.entity.SiacDMutuoStato;
import it.csi.siac.siacbilser.integration.entity.SiacRMutuoMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacRMutuoProgramma;
import it.csi.siac.siacbilser.integration.entity.SiacRMutuoRipartizione;
import it.csi.siac.siacbilser.integration.entity.SiacRProgrammaAttr;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.SiacTMutuo;
import it.csi.siac.siacbilser.integration.entity.SiacTMutuoNum;
import it.csi.siac.siacbilser.integration.entity.SiacTMutuoRata;
import it.csi.siac.siacbilser.integration.entity.SiacTMutuoVariazione;
import it.csi.siac.siacbilser.integration.entity.SiacTProgramma;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDMovgestTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDMovgestTsDetTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTMovgestHelper;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTMutuoHelper;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTProgrammaHelper;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.mutuo.AccertamentoAssociatoMutuo;
import it.csi.siac.siacbilser.model.mutuo.ImpegnoAssociatoMutuo;
import it.csi.siac.siacbilser.model.mutuo.Mutuo;
import it.csi.siac.siacbilser.model.mutuo.MutuoModelDetail;
import it.csi.siac.siacbilser.model.mutuo.PeriodoRimborsoMutuo;
import it.csi.siac.siacbilser.model.mutuo.RataMutuo;
import it.csi.siac.siacbilser.model.mutuo.RipartizioneMutuo;
import it.csi.siac.siacbilser.model.mutuo.StatoMutuo;
import it.csi.siac.siacbilser.model.mutuo.VariazioneMutuo;
import it.csi.siac.siaccommon.model.ComposedModelDetail;
import it.csi.siac.siaccommon.model.ModelDetailEnum;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommon.util.collections.Filter;
import it.csi.siac.siaccommon.util.date.DateUtil;
import it.csi.siac.siaccommon.util.number.BigDecimalUtil;
import it.csi.siac.siaccommon.util.number.NumberUtil;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.AccertamentoModelDetail;
import it.csi.siac.siacfin2ser.model.ImpegnoModelDetail;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacgenser.model.ProgettoModelDetail;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class MutuoDad extends ExtendedBaseDadImpl {

	@Autowired private SiacTMutuoDao siacTMutuoDao;
	@Autowired private SiacTMutuoRepository siacTMutuoRepository;
	@Autowired private SiacSMutuoStoricoRepository siacSMutuoStoricoRepository;
	@Autowired private SiacDMutuoPeriodoRimborsoDao siacDMutuoPeriodoRimborsoDao;
	@Autowired private SiacTMutuoNumRepository siacTMutuoNumRepository;
	@Autowired private SiacTMutuoRataDao siacTMutuoRataDao;
	@Autowired private SiacTMutuoRataRepository siacTMutuoRataRepository;
	@Autowired private SiacTMutuoVariazioneDao siacTMutuoVariazioneDao;
	@Autowired private MovimentoGestioneDao movimentoGestioneDao;
	@Autowired private SiacRMutuoMovgestTDao siacRMutuoMutuoMovgestTDao;
	@Autowired private SiacRMutuoRipartizioneDao siacRMutuoRipartizioneDao;
	@Autowired private ProgettoDao progettoDao;
	@Autowired private SiacRMutuoProgrammaDao siacRMutuoProgrammaDao;
	
	@Autowired private MutuoSiacTMutuoMapper mutuoSiacTMutuoMapper;
	@Autowired private RataMutuoSiacTMutuoRataMapper rataMutuoSiacTMutuoRataMapper;
	@Autowired private SiacTMutuoRataRataMutuoMapper siacTMutuoRataRataMutuoMapper;
	@Autowired private SiacDMutuoPeriodoRimborsoPeriodoRimborsoMutuoMapper siacDMutuoPeriodoRimborsoPeriodoRimborsoMutuoMapper;
	@Autowired private StatoMutuoSiacDMutuoStatoMapper statoMutuoSiacDMutuoStatoMapper;
	@Autowired private SiacTMutuoMutuoBaseMapper siacTMutuoMutuoBaseMapper;
	@Autowired private VariazioneMutuoSiacTMutuoVariazioneMapper variazioneMutuoSiacTMutuoVariazioneMapper;
	@Autowired private SiacTMovgestImpegnoMapper siacTMovgestImpegnoMapper;
	@Autowired private SiacTMovgestAccertamentoMapper siacTMovgestAccertamentoMapper;
	@Autowired private SiacRMutuoMovgestTImpegnoAssociatoMutuoMapper siacRMutuoMovgestTImpegnoAssociatoMutuoMapper;
	@Autowired private SiacRMutuoMovgestTAccertamentoAssociatoMutuoMapper siacRMutuoMovgestTAccertamentoAssociatoMutuoMapper;
	@Autowired private RipartizioneMutuoSiacRMutuoRipartizioneMapper ripartizioneMutuoSiacRMutuoRipartizioneMapper;
	@Autowired private SiacTProgrammaProgettoMapper siacTProgrammaProgettoMapper;
	
	
	@Autowired private SiacTMutuoHelper siacTMutuoHelper;
	@Autowired private SiacTMovgestHelper siacTMovgestHelper;
	@Autowired private SiacTProgrammaHelper siacTProgrammaHelper;
	
	@Autowired private MapperDecoratorHelper mapperDecoratorHelper;
	
	
	public Mutuo create(Mutuo mutuo) {
		mutuo.setNumero(staccaNumeroMutuo(mutuo));
		SiacTMutuo siacTMutuo = buildSiacTMutuo(mutuo);
		siacTMutuoDao.create(siacTMutuo);
		mutuo.setUid(siacTMutuo.getUid());
		
		return mutuo;
	}

	public Mutuo update(Mutuo mutuo) {
		
		siacSMutuoStoricoRepository.insert(mutuo.getUid(), mutuo.getLoginOperazione());
		
		SiacTMutuo siacTMutuo = buildSiacTMutuo(mutuo);
		siacTMutuoDao.update(siacTMutuo);
		
		return mutuo;
	}
	
	public Mutuo annulla(Mutuo mutuo) {
		
		siacSMutuoStoricoRepository.insert(mutuo.getUid(), loginOperazione);
		
		SiacTMutuo siacTMutuo = siacTMutuoDao.findById(mutuo.getUid());

		siacTMutuo.setSiacDMutuoStato(statoMutuoSiacDMutuoStatoMapper.map(StatoMutuo.Annullato, ente.getUid()));
		siacTMutuo.setLoginModifica(loginOperazione);
		siacTMutuo.setLoginOperazione(loginOperazione);
		
		siacTMutuo = siacTMutuoDao.update(siacTMutuo);
		
		return mutuo;
	}	

	private SiacTMutuo buildSiacTMutuo(Mutuo mutuo) {
		return mutuoSiacTMutuoMapper.map(mutuo);
	}
	
	/**
	 * Stacca il numero del mutuo
	 * @param ente l'ente
	 * @return il numero del mutuo
	 */
	@Transactional(propagation=Propagation.MANDATORY)
	public Integer staccaNumeroMutuo(Mutuo mutuo) {
		final String methodName = "staccaNumeroMutuo";
		
		Integer idEnte = mutuo.getEnte().getUid();
		
		SiacTMutuoNum siacTMutuoNum = siacTMutuoNumRepository.findByEnte(idEnte);
		
		Date now = new Date();
		if(siacTMutuoNum == null) {
			siacTMutuoNum = new SiacTMutuoNum();
			
			siacTMutuoNum.setSiacTEnteProprietario(new SiacTEnteProprietario(idEnte));
			
			siacTMutuoNum.setDataCreazione(now);
			siacTMutuoNum.setDataInizioValidita(now);
			siacTMutuoNum.setLoginOperazione(mutuo.getLoginOperazione());
			siacTMutuoNum.setMutuoNumero(1);
		}
		
		siacTMutuoNum.setDataModifica(now);
		
		siacTMutuoNumRepository.saveAndFlush(siacTMutuoNum);

		Integer numeroMutuo = siacTMutuoNum.getMutuoNumero();
		
		log.info(methodName, "Numero mutuo: " + numeroMutuo);
		
		return numeroMutuo;
	}
	
	
	/**
	 * Legge la lista dei PeriodoRimborsoMutuo associati all'Ente.
	 * 
	 * @param ente proprietario id 
	 * 
	 * @return la lista
	 */
	public List<PeriodoRimborsoMutuo> leggiPeriodiRimborsoMutuo() {
		List<SiacDMutuoPeriodoRimborso> siacDMutuoPeriodoRimborsos = siacDMutuoPeriodoRimborsoDao.findAllValid(ente.getUid());
		return siacDMutuoPeriodoRimborsoPeriodoRimborsoMutuoMapper.map(siacDMutuoPeriodoRimborsos);
	}
	
	public ListaPaginata<Mutuo> ricercaSinteticaMutui(Mutuo mutuo, ParametriPaginazione parametriPaginazione, ModelDetailEnum...mutuoModelDetails) {
		Page<SiacTMutuo> pageSiacTMutuo = siacTMutuoDao.ricercaSinteticaMutuo(
				mutuo.getEnte().getUid(),
				mutuo.getNumero(),
				mutuo.getTipoTasso()!=null ? mutuo.getTipoTasso().getCodice() : null,
				mutuo.getAttoAmministrativo() != null ? mutuo.getAttoAmministrativo().getUid() : null,
				mutuo.getAttoAmministrativo() != null ? mutuo.getAttoAmministrativo().getAnno() : null,
				mutuo.getAttoAmministrativo() != null ? mutuo.getAttoAmministrativo().getNumero() : null,
				mutuo.getAttoAmministrativo() != null ? mutuo.getAttoAmministrativo().getTipoAtto().getUid() : null,
				mutuo.getAttoAmministrativo() != null ? mutuo.getAttoAmministrativo().getStrutturaAmmContabile().getUid() : null,
				mutuo.getOggetto(),
				mutuo.getSoggetto() !=null ? mutuo.getSoggetto().getUid() : null,
				mutuo.getStatoMutuo()!=null ? mutuo.getStatoMutuo().getCodice() : null,
				mutuo.getPeriodoRimborso() !=null ? mutuo.getPeriodoRimborso().getUid() : null,
				toPageable(parametriPaginazione));

		return siacTMutuoMutuoBaseMapper.map(pageSiacTMutuo, mapperDecoratorHelper.getDecoratorsFromModelDetails(mutuoModelDetails));
	}	
	
	public Mutuo ricercaDettaglioMutuo(Mutuo mutuo, MutuoModelDetail... mutuoModelDetails/*, MutuoAttoAmministrativoComposedModelDetail[] composedModelDetail*/) {
		
		SiacTMutuo siacTMutuo = siacTMutuoDao.findById(mutuo.getUid());

		return siacTMutuoMutuoBaseMapper.map(siacTMutuo, mapperDecoratorHelper.getDecoratorsFromModelDetails(mutuoModelDetails/*, composedModelDetail*/));
	}
	
//	public Mutuo ricercaDettaglioMutuo(Mutuo mutuo, MutuoModelDetail...mutuoModelDetails) {
//		return ricercaDettaglioMutuo(mutuo, mutuoModelDetails, null);
//	}		

	
	public Mutuo ricercaMutuo(Mutuo mutuo, MutuoModelDetail...mutuoModelDetails) {
		return mutuo.getUid() != 0 ?
			siacTMutuoMutuoBaseMapper.map(siacTMutuoDao.findById(mutuo.getUid()), mapperDecoratorHelper.getDecoratorsFromModelDetails(mutuoModelDetails)) :
			siacTMutuoMutuoBaseMapper.map(siacTMutuoRepository.findMutuoByLogicKey(mutuo.getNumero(), mutuo.getEnte().getUid()), mapperDecoratorHelper.getDecoratorsFromModelDetails(mutuoModelDetails));	
		
	}
	
	/**
	 * Ricerca lo stato associato al mutuo passato in input
	 * 
	 * @param mutuo 
	 * 
	 * @return statoMutuo 
	 */
	public StatoMutuo findStatoMutuoByMutuo(Mutuo mutuo) {
		SiacDMutuoStato siacDMutuoStato = siacTMutuoRepository.findSiacDMutuoStatoByMutuoId(mutuo.getUid());
		
		return StatoMutuo.fromCodice(siacDMutuoStato.getMutuoStatoCode());
	}
	
	public RataMutuo ricercaRataMutuo(RataMutuo rataMutuo, Integer mutuoId) {
		return rataMutuo.getUid() == 0 
			? NumberUtil.isValidAndGreaterThanZero(rataMutuo.getNumeroRataPiano()) 
				? siacTMutuoRataRataMutuoMapper.map(siacTMutuoRataRepository.findValidMutuoRataByLogicKey(mutuoId, rataMutuo.getNumeroRataPiano(), ente.getUid())) 
				: siacTMutuoRataRataMutuoMapper.map(siacTMutuoRataRepository.findValidMutuoRataByLogicKey(mutuoId, rataMutuo.getAnno(), rataMutuo.getNumeroRataAnno(), ente.getUid()))
			: siacTMutuoRataRataMutuoMapper.map(siacTMutuoRataDao.findById(rataMutuo.getUid()));
	}
	
	public void salvaPianoAmmortamentoMutuo(Mutuo mutuo) {
		cancellaRateNonScadute(mutuo, 1);
		
		List<RataMutuo> elencoRateNonScadute = CollectionUtil.filter(mutuo.getElencoRate(), new RataMutuoNonScadutaFilter());
		
		switch (mutuo.getStatoMutuo()) {
		case Predefinitivo:
			inserisciRate(mutuo.getUid(), elencoRateNonScadute);
			break;
		case Definitivo:
			aggiornaRate(mutuo.getUid(), elencoRateNonScadute);
			break;
		case Bozza:
		case Annullato:
		default:
			throw new IllegalArgumentException(mutuo.getStatoMutuo().getDescrizione());
		}
		
		aggiornaMutuoPerVariazionePiano(mutuo);
	}

	private void cancellaRateNonScadute(Mutuo mutuo, Integer numeroRataIniziale) {
		
		SiacTMutuo siacTMutuo = siacTMutuoDao.findById(mutuo.getUid());
		List<SiacTMutuoRata> listSiacTMutuoRata = CollectionUtil.filter(siacTMutuoHelper.getElencoRateNonScadute(siacTMutuo), new Filter<SiacTMutuoRata>() {
			@Override
			public boolean isAcceptable(SiacTMutuoRata source) {
				return source.getMutuoRataNumRataPiano().compareTo(numeroRataIniziale)>=0;
			}});

		switch (mutuo.getStatoMutuo()) {
		case Predefinitivo:
			cancellazioneFisicaRateNonScadute(listSiacTMutuoRata);
			break;
		case Definitivo:
			cancellazioneLogicaRateNonScadute(listSiacTMutuoRata);
			break;
		case Bozza:
		case Annullato:
		default:
			throw new IllegalArgumentException(mutuo.getStatoMutuo().getDescrizione());
		}
	}
		
	private void cancellazioneLogicaRateNonScadute(List<SiacTMutuoRata> listSiacTMutuoRata) {
		for( SiacTMutuoRata rataNonScaduta: listSiacTMutuoRata) { 
			rataNonScaduta.setLoginCancellazione(loginOperazione);
			siacTMutuoRataDao.logicalDelete(rataNonScaduta);
		}
		
	}
	private void cancellazioneFisicaRateNonScadute(List<SiacTMutuoRata> listSiacTMutuoRata) {
		for( SiacTMutuoRata rataNonScaduta: listSiacTMutuoRata) { 
			siacTMutuoRataDao.delete(rataNonScaduta);
		}
	}
	private void aggiornaRate(Integer mutuoId, List<RataMutuo> elencoRate) {
		for (RataMutuo rataMutuo : elencoRate) {
			siacTMutuoRataDao.create(buildSiacTMutuoRata(rataMutuo, mutuoId));
		}
		siacTMutuoRataDao.flushAndClear();
	}
	private void inserisciRate(Integer mutuoId, List<RataMutuo> elencoRate) {
		for (RataMutuo rataMutuo : elencoRate) {
			siacTMutuoRataDao.create(buildSiacTMutuoRataNew(mutuoId, rataMutuoSiacTMutuoRataMapper.map(rataMutuo)));
		}
		siacTMutuoRataDao.flushAndClear();
	}
	private SiacTMutuoRata buildSiacTMutuoRata(RataMutuo rataMutuo, Integer mutuoId ) {
		
		SiacTMutuoRata siacTMutuoRata = rataMutuo.getUid()!=0 ? 
				buildSiacTMutuoRataExisting(rataMutuo, siacTMutuoRataDao.findById(rataMutuo.getUid())) :  
				buildSiacTMutuoRataNew(mutuoId, rataMutuoSiacTMutuoRataMapper.map(rataMutuo));
				
		return siacTMutuoRata;
	}
	private SiacTMutuoRata buildSiacTMutuoRataNew(Integer mutuoId, SiacTMutuoRata siacTMutuoRata) {
		SiacTMutuo siacTMutuo = new SiacTMutuo();
		siacTMutuo.setUid(mutuoId);
		siacTMutuoRata.setSiacTMutuo(siacTMutuo);
		siacTMutuoRata.setSiacTEnteProprietario(siacTEnteProprietario);
		siacTMutuoRata.setLoginPerInserimento(loginOperazione);
		return siacTMutuoRata;
	}
	private SiacTMutuoRata buildSiacTMutuoRataExisting(RataMutuo rataMutuo, SiacTMutuoRata siacTMutuoRataExisting) {
		
		SiacTMutuoRata siacTMutuoRata = new SiacTMutuoRata(siacTMutuoRataExisting);
		siacTMutuoRata.setMutuoRataDataScadenza(rataMutuo.getDataScadenza());
		siacTMutuoRata.setMutuoRataImporto(rataMutuo.getImportoTotale());
		siacTMutuoRata.setMutuoRataImportoQuotaCapitale(rataMutuo.getImportoQuotaCapitale());
		siacTMutuoRata.setMutuoRataImportoQuotaOneri(rataMutuo.getImportoQuotaOneri());
		siacTMutuoRata.setMutuoRataImportoQuotaInteressi(rataMutuo.getImportoQuotaInteressi());
		siacTMutuoRata.setDataFineValidita(null);
		siacTMutuoRata.setLoginPerInserimento(loginOperazione);
		
		return siacTMutuoRata;
	}	
	private void aggiornaMutuoPerVariazionePiano(VariazioneMutuo variazioneMutuo) {
		
		siacSMutuoStoricoRepository.insert(variazioneMutuo.getMutuo().getUid(), loginOperazione);

		SiacTMutuo siacTMutuo = buildSiacTMutuoPerVariazionePiano(variazioneMutuo.getMutuo().getUid());
		
		siacTMutuo.setMutuoTasso(variazioneMutuo.getMutuo().getTassoInteresse());
		siacTMutuo.setMutuoTassoEuribor(variazioneMutuo.getMutuo().getTassoInteresseEuribor());
		siacTMutuo = siacTMutuoDao.update(siacTMutuo);
	}
	private void aggiornaMutuoPerVariazionePiano(Mutuo mutuo) {
		
		siacSMutuoStoricoRepository.insert(mutuo.getUid(), loginOperazione);
		SiacTMutuo siacTMutuo = buildSiacTMutuoPerVariazionePiano(mutuo.getUid());
		siacTMutuo.setSiacDMutuoStato(statoMutuoSiacDMutuoStatoMapper.map(mutuo.getStatoMutuo(), ente.getUid()));
		siacTMutuo = siacTMutuoDao.update(siacTMutuo);
		
	}
	private SiacTMutuo buildSiacTMutuoPerVariazionePiano(int mutuoId) {
		
		SiacTMutuo siacTMutuo = siacTMutuoDao.findById(mutuoId);
		siacTMutuo.setMutuoDataInizioPianoAmmortamento(siacTMutuoHelper.getDataInizioPianoAmmortamento(siacTMutuo));
		siacTMutuo.setMutuoDataScadenzaUltimaRata(siacTMutuoHelper.getDataScadenzaUltimaRata(siacTMutuo));
		siacTMutuo.setMutuoAnnoInizio(DateUtil.getYear(siacTMutuoHelper.getDataScadenzaPrimaRata(siacTMutuo)));
		siacTMutuo.setMutuoAnnoFine(DateUtil.getYear(siacTMutuo.getMutuoDataScadenzaUltimaRata()));
		siacTMutuo.setMutuoDurataAnni(siacTMutuo.getMutuoAnnoFine()-siacTMutuo.getMutuoAnnoInizio()+1);
//		siacTMutuo.setSiacDMutuoStato(statoMutuoSiacDMutuoStatoMapper.map(StatoMutuo.Definitivo, ente.getUid()));
		siacTMutuo.setLoginPerAggiornamento(loginOperazione);
		return siacTMutuo;
	}
	
	public VariazioneMutuo createVariazione(VariazioneMutuo variazioneMutuo) {
		
		cancellaRateNonScadute(variazioneMutuo.getMutuo(), variazioneMutuo.getRataMutuo().getNumeroRataPiano());
		
		inserisciRate(variazioneMutuo.getMutuo().getUid(), calcolaRate(variazioneMutuo));
		
		aggiornaMutuoPerVariazionePiano(variazioneMutuo);
		
		inserisciVariazioneMutuo(variazioneMutuo);
		
		return variazioneMutuo;	
	}

	private void inserisciVariazioneMutuo(VariazioneMutuo variazioneMutuo) {
		variazioneMutuo.setEnte(ente);
		variazioneMutuo.setLoginOperazione(loginOperazione);
		variazioneMutuo.setLoginCreazione(loginOperazione);
		variazioneMutuo.setLoginModifica(loginOperazione);
		SiacTMutuoVariazione siacTMutuoVariazione = buildSiacTMutuoVariazione(variazioneMutuo);
		siacTMutuoVariazioneDao.create(siacTMutuoVariazione);
		variazioneMutuo.setUid(siacTMutuoVariazione.getUid());
	}

	private List<RataMutuo> calcolaRate(VariazioneMutuo variazioneMutuo) {

		switch(variazioneMutuo.getTipoVariazioneMutuo()) {
			case VariazioneTasso: {
				return CalcolaRateMutuoHelper.getInstance().calcolaRate(
						variazioneMutuo.getRataMutuo().getNumeroRataPiano(),
						variazioneMutuo.getRataMutuo().getDataScadenza(),
						variazioneMutuo.getNumeroRataFinale(),
						variazioneMutuo.getRataMutuo().getImportoTotale(),
						variazioneMutuo.getRataMutuo().getDebitoIniziale(),
						BigDecimalUtil.percent(variazioneMutuo.getMutuo().getTassoInteresse()),
						variazioneMutuo.getMutuo().getPeriodoRimborso(),
						BigDecimal.ZERO
					);				
			}
			case VariazionePiano: {
				return CalcolaRateMutuoHelper.getInstance().calcolaRate(
						variazioneMutuo.getRataMutuo().getNumeroRataPiano(),
						variazioneMutuo.getRataMutuo().getDataScadenza(),
						MutuoUtil.calcNumeroRataPiano(
							variazioneMutuo.getAnnoFineAmmortamento(),
							variazioneMutuo.getNumeroRataFinale(),
							variazioneMutuo.getMutuo().getScadenzaPrimaRata(),
							variazioneMutuo.getMutuo().getPeriodoRimborso()
						),
						null,
						variazioneMutuo.getRataMutuo().getDebitoIniziale(),
						BigDecimalUtil.percent(variazioneMutuo.getMutuo().getTassoInteresse()),
						variazioneMutuo.getMutuo().getPeriodoRimborso(),
						BigDecimal.ZERO
					);				
			}
		}

		throw new IllegalArgumentException(variazioneMutuo.getTipoVariazioneMutuo().getDescrizione());
	}
	private SiacTMutuoVariazione buildSiacTMutuoVariazione(VariazioneMutuo variazioneMutuo) {
		SiacTMutuoVariazione siacTMutuoVariazione = variazioneMutuoSiacTMutuoVariazioneMapper.map(variazioneMutuo);
		return siacTMutuoVariazione;
	}
	
	public void annullaPianoAmmortamento(Mutuo mutuo) {
		
		cancellaRateNonScadute(mutuo, 1);
		
		aggiornaMutuoPerAnnullamentoPiano(mutuo);
	}

	private void aggiornaMutuoPerAnnullamentoPiano(Mutuo mutuo) {
		
		siacSMutuoStoricoRepository.insert(mutuo.getUid(), loginOperazione);
		
		SiacTMutuo siacTMutuo = siacTMutuoDao.findById(mutuo.getUid());
		
		siacTMutuo.setMutuoDataInizioPianoAmmortamento(null);
		siacTMutuo.setMutuoDataScadenzaUltimaRata(null);
		siacTMutuo.setSiacDMutuoStato(statoMutuoSiacDMutuoStatoMapper.map(StatoMutuo.Bozza, ente.getUid()));
		siacTMutuo.setLoginPerAggiornamento(loginOperazione);
		
		siacTMutuo = siacTMutuoDao.update(siacTMutuo);
	}
	
	/**
	 * Legge la lista degli impegni che possono essere associati al mutuo
	 * 
	 * @param impegno filtri di ricerca
	 * @param parametriPaginazione
	 * @param ImpegnoModelDetail[] impegnoModelDetails
	 * @param ComposedModelDetail... composedModelDetail
	 * 
	 * @return lista paginata di impegni che soddisfa la ricerca
	 */
	public ListaPaginata<Impegno> ricercaImpegniAssociabiliMutuo(MovimentoGestione movimentoGestione, ParametriPaginazione parametriPaginazione, 
			ImpegnoModelDetail[] impegnoModelDetails, ComposedModelDetail... composedModelDetail) {
		
		Page<SiacTMovgest> pageSiacTMovgest = ricercaMovimentiGestioneMutuo(null /* per compilare */, movimentoGestione,SiacDMovgestTipoEnum.Impegno, parametriPaginazione);

		return siacTMovgestImpegnoMapper.map(pageSiacTMovgest, mapperDecoratorHelper.getDecoratorsFromModelDetails(impegnoModelDetails, composedModelDetail));
	}
	
	/**
	 * Legge la lista degli accertamenti che possono essere associati al mutuo
	 * 
	 * @param impegno filtri di ricerca
	 * @param parametriPaginazione
	 * @param ImpegnoModelDetail[] impegnoModelDetails
	 * @param ComposedModelDetail... composedModelDetail
	 * 
	 * @return lista paginata di accertamenti che soddisfa la ricerca
	 */
	public ListaPaginata<Accertamento> ricercaAccertamentiAssociabiliMutuo(MovimentoGestione movimentoGestione, ParametriPaginazione parametriPaginazione, 
			AccertamentoModelDetail[] accertamentoModelDetails, ComposedModelDetail... composedModelDetail) {
		
		Page<SiacTMovgest> pageSiacTMovgest = ricercaMovimentiGestioneMutuo(null, movimentoGestione,SiacDMovgestTipoEnum.Accertamento, parametriPaginazione);
		
		return siacTMovgestAccertamentoMapper.map(pageSiacTMovgest , mapperDecoratorHelper.getDecoratorsFromModelDetails(accertamentoModelDetails, composedModelDetail));
	}
	
	private Page<SiacTMovgest> ricercaMovimentiGestioneMutuo(Integer mutuoId, MovimentoGestione movimentoGestione,SiacDMovgestTipoEnum siacDMovgestTipoEnum,
			ParametriPaginazione parametriPaginazione) {
				
		return  movimentoGestioneDao.ricercaMovimentiGestioneMutuo(
				ente.getUid(),
				mutuoId,
				movimentoGestione.getAnno(),
				movimentoGestione.getNumero(),
				siacDMovgestTipoEnum,
				getAnnoBilancio(),
				movimentoGestione.getCapitolo() != null ? movimentoGestione.getCapitolo().getUid() : null,
				movimentoGestione.getAttoAmministrativo() != null ? movimentoGestione.getAttoAmministrativo().getUid() : null,
				movimentoGestione.getAttoAmministrativo() != null ? movimentoGestione.getAttoAmministrativo().getAnno() : null,
				movimentoGestione.getAttoAmministrativo() != null ? movimentoGestione.getAttoAmministrativo().getNumero() : null,
				movimentoGestione.getAttoAmministrativo() != null && 
					movimentoGestione.getAttoAmministrativo().getTipoAtto() != null ? movimentoGestione.getAttoAmministrativo().getTipoAtto().getUid() : null,
				movimentoGestione.getAttoAmministrativo() != null &&
					movimentoGestione.getAttoAmministrativo().getStrutturaAmmContabile() != null ? 
						movimentoGestione.getAttoAmministrativo().getStrutturaAmmContabile().getUid() : null,
				movimentoGestione.getSoggetto() != null ? movimentoGestione.getSoggetto().getUid() : null,
				movimentoGestione.getComponenteBilancioMovimentoGestione() != null ? movimentoGestione.getComponenteBilancioMovimentoGestione().getIdTipoComponente() : null,
				toPageable(parametriPaginazione));
	}
	public void createAssociazioneMovimentoGestioneMutuo(Mutuo mutuo, Integer movimentoGestioneId) {
		siacRMutuoMutuoMovgestTDao.create( buildSiacRMutuoMovgestT(mutuo.getUid(), movimentoGestioneId));
	}

	private SiacRMutuoMovgestT buildSiacRMutuoMovgestT(Integer mutuoId, Integer movimentoGestioneId) {
		
		SiacTMutuo siacTMutuo = new SiacTMutuo();
		siacTMutuo.setUid(mutuoId);

		SiacRMutuoMovgestT siacRMutuoMovgestT = new SiacRMutuoMovgestT();

		siacRMutuoMovgestT.setSiacTMutuo(siacTMutuo);
		
		SiacTMovgest siacTMovgest = movimentoGestioneDao.findById(movimentoGestioneId);
		siacRMutuoMovgestT.setSiacTMovgestT(siacTMovgestHelper.getSiacTMovgestTestata(siacTMovgest));
		
		BigDecimal importoAttuale = siacTMovgestHelper.getSiacTMovgestTsDet(siacTMovgest, SiacDMovgestTsDetTipoEnum.Attuale) != null 
				? siacTMovgestHelper.getSiacTMovgestTsDet(siacTMovgest, SiacDMovgestTsDetTipoEnum.Attuale).getMovgestTsDetImporto()
						: null;
				
		siacRMutuoMovgestT.setMutuoMovgestTsImportoFinale(importoAttuale);
		siacRMutuoMovgestT.setMutuoMovgestTsImportoIniziale(importoAttuale);
		siacRMutuoMovgestT.setLoginPerInserimento(loginOperazione);
		siacRMutuoMovgestT.setSiacTEnteProprietario(siacTEnteProprietario);
		
		return siacRMutuoMovgestT;
	}
	
	public boolean esisteAssociazioneMutuoMovimentoGestione(Mutuo mutuo , MovimentoGestione movimentoGestione) {
		return siacTMutuoHelper.getSiacRMutuoMovgestT(siacTMutuoDao.findById(mutuo.getUid()), movimentoGestione.getUid()) != null;
	}
	
	public void eliminaAssociazioneMovimentoGestioneMutuo(Mutuo mutuo, Integer movimentoGestioneId) {
		SiacRMutuoMovgestT siacRMutuoMovgestT = siacTMutuoHelper.getSiacRMutuoMovgestT(siacTMutuoDao.findById(mutuo.getUid()), movimentoGestioneId);
		if (siacRMutuoMovgestT != null) {
			siacRMutuoMovgestT.setLoginCancellazione(loginOperazione);
			siacRMutuoMutuoMovgestTDao.logicalDelete(siacRMutuoMovgestT);
		}
	}
	
	public List<ImpegnoAssociatoMutuo> ricercaImpegniAssociatiMutuo (Mutuo mutuo) {
		return siacRMutuoMovgestTImpegnoAssociatoMutuoMapper.map(
				siacTMutuoHelper.getSiacRMutuoMovgestTFilteredList(siacTMutuoDao.findById(mutuo.getUid()), SiacDMovgestTipoEnum.Impegno));
	}
	public List<AccertamentoAssociatoMutuo> ricercaAccertamentiAssociatiMutuo (Mutuo mutuo) {
		return siacRMutuoMovgestTAccertamentoAssociatoMutuoMapper.map(
				siacTMutuoHelper.getSiacRMutuoMovgestTFilteredList(siacTMutuoDao.findById(mutuo.getUid()), SiacDMovgestTipoEnum.Accertamento));
	}
	
	public void salvaRipartizioneMutuo(Mutuo mutuo) {
		
		cancellaRipartizioneMutuo(mutuo);
		
		inserisciRipartizioneMutuo(mutuo);
		
	}
	public void annullaRipartizioneMutuo(Mutuo mutuo) {
		
		cancellaRipartizioneMutuo(mutuo);
		
	}
	
	/**
	 * Legge la lista dei progetti che possono essere associati al mutuo
	 * 
	 * @param progetto filtri di ricerca
	 * @param parametriPaginazione
	 * @param ProgettoModelDetail[] progettoModelDetails
	 * 
	 * @return lista paginata di progetti che soddisfa la ricerca
	 */
	public ListaPaginata<Progetto> ricercaProgettiAssociabiliMutuo(Progetto progetto, ParametriPaginazione parametriPaginazione, ProgettoModelDetail[] progetttoModelDetails) {
		
		Page<SiacTProgramma> pageSiacTProgramma = progettoDao.ricercaProgettiMutuo(
				ente.getUid(), null, progetto.getCodice(), getAnnoBilancio(),
				progetto.getAttoAmministrativo() != null ? progetto.getAttoAmministrativo().getUid() : null,
				progetto.getAttoAmministrativo() != null ? progetto.getAttoAmministrativo().getAnno() : null,
				progetto.getAttoAmministrativo() != null ? progetto.getAttoAmministrativo().getNumero() : null,
				progetto.getAttoAmministrativo() != null ? progetto.getAttoAmministrativo().getTipoAtto().getUid() : null,
				progetto.getAttoAmministrativo() != null ? progetto.getAttoAmministrativo().getStrutturaAmmContabile().getUid() : null,
				progetto.getTipoAmbito() != null ? progetto.getTipoAmbito().getUid() : null,
				toPageable(parametriPaginazione));
		
		return siacTProgrammaProgettoMapper.map(pageSiacTProgramma , mapperDecoratorHelper.getDecoratorsFromModelDetails(progetttoModelDetails));
	}
	
	private void cancellaRipartizioneMutuo(Mutuo mutuo) {
		
		switch(mutuo.getStatoMutuo()) {
			case Bozza:
			case Predefinitivo: {
				cancellazioneFisicaRipartizioneMutuo(mutuo);
				break;
			}
			case Definitivo: {
				cancellazioneLogicaRipartizioneMutuo(mutuo);
				break;
			}
			case Annullato:
			default: {
				throw new IllegalArgumentException(mutuo.getStatoMutuo().getDescrizione());
			}
		}
	}
	
	private void cancellazioneLogicaRipartizioneMutuo(Mutuo mutuo) {
		
		List<SiacRMutuoRipartizione> siacRMutuoRipartizioneList = siacTMutuoHelper.getSiacRMutuoRipartizioneFilteredList(siacTMutuoDao.findById(mutuo.getUid()));
		
		for( SiacRMutuoRipartizione siacRMutuoRipartizione: siacRMutuoRipartizioneList)
		{ 
			siacRMutuoRipartizione.setLoginCancellazione(loginOperazione);
			siacRMutuoRipartizioneDao.logicalDelete(siacRMutuoRipartizione);
		}
	}
	private void cancellazioneFisicaRipartizioneMutuo(Mutuo mutuo) {
		
		List<SiacRMutuoRipartizione> siacRMutuoRipartizioneList = siacTMutuoHelper.getSiacRMutuoRipartizioneFilteredList(siacTMutuoDao.findById(mutuo.getUid()));
		
		for( SiacRMutuoRipartizione siacRMutuoRipartizione: siacRMutuoRipartizioneList)
		{ 
			siacRMutuoRipartizioneDao.delete(siacRMutuoRipartizione);
		}
	}
	private void inserisciRipartizioneMutuo(Mutuo mutuo) {
		for (RipartizioneMutuo ripartizioneMutuo : mutuo.getElencoRipartizioneMutuo()) {
			ripartizioneMutuo.setEnte(ente);
			siacRMutuoRipartizioneDao.create(buildSiacRMutuoRipartizione(mutuo.getUid(), ripartizioneMutuoSiacRMutuoRipartizioneMapper.map(ripartizioneMutuo)));
		}
		siacTMutuoRataDao.flushAndClear();
	}	
	private SiacRMutuoRipartizione buildSiacRMutuoRipartizione(Integer mutuoId, SiacRMutuoRipartizione siacRMutuoRipartizione) {
		SiacTMutuo siacTMutuo = new SiacTMutuo();
		siacTMutuo.setUid(mutuoId);
		siacRMutuoRipartizione.setSiacTMutuo(siacTMutuo);
		siacRMutuoRipartizione.setSiacTEnteProprietario(siacTEnteProprietario);
		siacRMutuoRipartizione.setLoginPerInserimento(loginOperazione);
		return siacRMutuoRipartizione;
	}
	
	public void eliminaAssociazioneProgettoMutuo(Mutuo mutuo, Integer progettoId) {
		SiacRMutuoProgramma siacRMutuoProgramma = siacTMutuoHelper.getSiacRMutuoProgramma(siacTMutuoDao.findById(mutuo.getUid()), progettoId);
		if (siacRMutuoProgramma != null) {
			siacRMutuoProgramma.setLoginCancellazione(loginOperazione);
			siacRMutuoProgrammaDao.logicalDelete(siacRMutuoProgramma);
		}
	}	
	public void createAssociazioneProgettoMutuo(Mutuo mutuo, Integer progettoId) {
		siacRMutuoProgrammaDao.create( buildSiacRMutuoProgramma(mutuo.getUid(), progettoId));
	}
	
	private SiacRMutuoProgramma buildSiacRMutuoProgramma(Integer mutuoId, Integer programmaId) {
		
		SiacTMutuo siacTMutuo = new SiacTMutuo();
		siacTMutuo.setUid(mutuoId);

		SiacRMutuoProgramma siacRMutuoProgramma = new SiacRMutuoProgramma();

		siacRMutuoProgramma.setSiacTMutuo(siacTMutuo);
		
		SiacTProgramma siacTProgramma = progettoDao.findById(programmaId);
		siacRMutuoProgramma.setSiacTProgramma(siacTProgramma);
		
		SiacRProgrammaAttr siacRAttr = siacTProgrammaHelper.getSiacRAttr(siacTProgramma, SiacTAttrEnum.ValoreComplessivoProgetto);
		BigDecimal valoreComplessivoProgetto = siacRAttr != null ? siacRAttr.getNumerico() : null;
				
		// Bisogna considerare il bilancio ? 
		// Valorizzare l’importo iniziale e l’importo finale dell’associazione con l’importo del progetto nell'anno di bilancio nel quale si sta operando.
		siacRMutuoProgramma.setMutuoProgrammaImportoIniziale(valoreComplessivoProgetto);
		siacRMutuoProgramma.setMutuoProgrammaImportoFinale(valoreComplessivoProgetto);
		siacRMutuoProgramma.setLoginPerInserimento(loginOperazione);
		siacRMutuoProgramma.setSiacTEnteProprietario(siacTEnteProprietario);
		
		return siacRMutuoProgramma;
	}	
	public boolean esisteAssociazioneMutuoProgetto(Mutuo mutuo , Progetto progetto) {
		return siacTMutuoHelper.getSiacRMutuoProgramma(siacTMutuoDao.findById(mutuo.getUid()), progetto.getUid()) != null;
	}	
}
