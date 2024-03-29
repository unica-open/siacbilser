/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
//import it.csi.siac.siacfinser.StringUtils;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.StatoOperativoProgetto;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipoClassificatore;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.CommonUtil;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.StringUtilsFin;
import it.csi.siac.siacfinser.TimingUtils;
import it.csi.siac.siacfinser.business.service.util.NumericUtils;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentiSubAccertamentiResponse;
import it.csi.siac.siacfinser.integration.dad.datacontainer.DisponibilitaMovimentoGestioneContainer;
import it.csi.siac.siacfinser.integration.dao.common.SiacDAmbitoRepository;
import it.csi.siac.siacfinser.integration.dao.common.SiacDAttoAmmTipoFinRepository;
import it.csi.siac.siacfinser.integration.dao.common.SiacTEnteProprietarioFinRepository;
import it.csi.siac.siacfinser.integration.dao.common.dto.AccertamentoDaVincolareInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.AttiAmmModificatiGestioneEntrataInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.AttiAmmModificatiGestioneSpesaInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.AttributoTClassInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.CapitoliInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.CodificaImportoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoAggiornamentoMovimentoGestioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoAggiornamentoSubMovGestTs;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoCalcoloImportiVincoliDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoControlliAggiornamentoAccertamentoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoControlliAggiornamentoImpegnoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoControlliAggiornamentoMovimentoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoControlliDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoControlliInserimentoMovimentoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoGestioneModificheMovimentiDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoInserimentoMovimentoGestioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoSalvataggioTransazioneElmentare;
import it.csi.siac.siacfinser.integration.dao.common.dto.ImpegnoInAnnullamentoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ImpegnoInModificaInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ImportoImpegnoInAnnullamentoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ModificaMovimentoGestioneEntrataInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ModificaMovimentoGestioneSpesaInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ModificaVincoliImpegnoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.MovGestInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.MovGestMainModelEntityInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.MovGestModelEntityInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.MovgestPkDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.MovimentoGestioneLigthAbstractDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.MovimentoGestioneLigthDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.MovimentoGestioneSubLigthDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.MovimentoInInserimentoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OggettoDellAttributoTClass;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneMovGestDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneSoggettoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaAccSubAccParamDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaAccertamentoParamDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaImpSubParamDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaImpegnoParamDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.SubImpegnoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.SubMovgestInModificaInfoDto;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacDModificaStatoRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacDModificaTipoRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacDMovgestStatoRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacDMovgestTipoRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacDMovgestTsDetTipoRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacDMovgestTsTipoRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacRAttoAmmStatoRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacRModificaStatoRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacRMovgestBilElemRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacRMovgestTsAttoAmmRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacRMovgestTsProgrammaRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacRMovgestTsRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacRMovgestTsSogClasseModRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacRMovgestTsSogClasseRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacRMovgestTsSogModRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacRMovgestTsSogRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacRMovgestTsStatoRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTBilElemFinRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTModificaRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTMovgestRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTMovgestTsDetModRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTMovgestTsDetRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTMovgestTsRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTProgrammaFinRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacDSoggettoClasseRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacRSoggettoClasseRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacTSoggettoFinRepository;
import it.csi.siac.siacfinser.integration.entity.SiacDAmbitoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDAttoAmmStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDAttoAmmTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDModificaStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDModificaTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDMovgestStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDMovgestTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDMovgestTsDetTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDMovgestTsTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDSoggettoClasseFin;
import it.csi.siac.siacfinser.integration.entity.SiacRAttoAmmStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRBilElemClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacRBilFaseOperativaFin;
import it.csi.siac.siacfinser.integration.entity.SiacRClassBaseFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRModificaStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestBilElemFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsAttoAmmFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsAttrFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsProgrammaFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogclasseFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogclasseModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoTsMovgestTFin;
import it.csi.siac.siacfinser.integration.entity.SiacRProgrammaStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoClasseFin;
import it.csi.siac.siacfinser.integration.entity.SiacTAttoAmmFin;
import it.csi.siac.siacfinser.integration.entity.SiacTBilElemFin;
import it.csi.siac.siacfinser.integration.entity.SiacTBilFin;
import it.csi.siac.siacfinser.integration.entity.SiacTClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacTEnteProprietarioFin;
import it.csi.siac.siacfinser.integration.entity.SiacTLiquidazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModificaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsDetFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsDetModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoTFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoTsDetFin;
import it.csi.siac.siacfinser.integration.entity.SiacTProgrammaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.base.SiacConModificaStato;
import it.csi.siac.siacfinser.integration.entity.ext.IdAccertamentoSubAccertamento;
import it.csi.siac.siacfinser.integration.entity.ext.IdImpegnoSubimpegno;
import it.csi.siac.siacfinser.integration.entity.ext.IdMovgestSubmovegest;
import it.csi.siac.siacfinser.integration.entity.mapping.FinMapId;
import it.csi.siac.siacfinser.integration.util.ChiamanteDoppiaGestImpegno;
import it.csi.siac.siacfinser.integration.util.DatiOperazioneUtil;
import it.csi.siac.siacfinser.integration.util.EntityLiquidazioneToModelLiquidazioneConverter;
import it.csi.siac.siacfinser.integration.util.EntityToModelConverter;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.integration.util.TransazioneElementareEntityToModelConverter;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.AccertamentoAbstract;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.ImpegnoAbstract;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.MovimentoGestione.AttributoMovimentoGestione;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.UnitaElementareGestione;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione.StatoOperativoModificaMovimentoGestione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.movgest.VincoloAccertamento;
import it.csi.siac.siacfinser.model.movgest.VincoloImpegno;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaAccSubAcc;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaImpSub;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaImpegno;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamento;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

@Deprecated
@Transactional
public abstract class MovimentoGestioneDad<T extends MovimentoGestione, ST extends MovimentoGestione> extends AbstractFinDad {

	@Autowired
	protected SiacTMovgestRepository siacTMovgestRepository;
	
	@Autowired
	private SiacTMovgestTsRepository siacTMovgestTsRepository;
	
	@Autowired	
	SiacRMovgestTsSogModRepository siacRMovgestTsSogModRepository;
	
	@Autowired
	private SiacRMovgestTsSogClasseModRepository siacRMovgestTsSogClasseModRepository;
	
	@Autowired
	private SiacRMovgestTsSogClasseRepository siacRMovgestTsSogClasseRepository;
	
	@Autowired
	private SiacTMovgestTsDetModRepository siacTMovgestTsDetModRepository;
	
	@Autowired
	private SiacDAmbitoRepository siacDAmbitoRepository;
	
	@Autowired
	private SiacDMovgestTsTipoRepository siacDMovgestTsTipoRepository;

	@Autowired
	protected SiacTEnteProprietarioFinRepository siacTEnteProprietarioRepository;
	
	@Autowired
	private SiacDMovgestStatoRepository siacDMovgestStatoRepository;
	
	@Autowired
	private SiacRMovgestTsStatoRepository siacRMovgestTsStatoRepository;
	
	@Autowired
	private SiacDMovgestTsDetTipoRepository siacDMovgestTsDetTipoRepository;
	
	@Autowired
	private SiacTMovgestTsDetRepository siacTMovgestTsDetRepository;
	
	@Autowired
	private SiacDMovgestTipoRepository siacDMovgestTipoRepository;
	
	@Autowired
	private SiacRMovgestTsSogRepository siacRMovgestTsSogRepository;
	
	@Autowired
	private SiacTSoggettoFinRepository siacTSoggettoRepository;

	@Autowired
	protected SoggettoFinDad soggettoDad;	
	
	@Autowired
	private SiacTBilElemFinRepository siacTBilElemRepository;
	
	@Autowired
	private SiacRMovgestBilElemRepository siacRMovgestBilElemRepository;
	
	@Autowired
	private SiacRMovgestTsAttoAmmRepository siacRMovgestTsAttoAmmRepository;
	
	@Autowired
	private SiacRMovgestTsProgrammaRepository siacRMovgestTsProgrammaRepository;
	
	@Autowired
	private SiacRSoggettoClasseRepository siacRSoggettoClasseRepository;
	
	@Autowired
	private SiacRAttoAmmStatoRepository siacRAttoAmmStatoRepository;
	
	@Autowired
	private SiacDSoggettoClasseRepository siacDSoggettoClasseRepository;
	
	@Autowired
	private SiacDModificaStatoRepository siacDModificaStatoRepository;
	
	@Autowired
	private SiacTProgrammaFinRepository siacTProgrammaRepository;
	
	@Autowired
	private SiacDModificaTipoRepository siacDModificaTipoRepository;
	
	@Autowired
	private SiacTModificaRepository siacTModificaRepository;
	
	@Autowired
	private SiacRModificaStatoRepository siacRModificaStatoRepository;
	
	@Autowired
	private SiacDAttoAmmTipoFinRepository siacDAttoAmmTipoRepository;
	
	@Autowired
	private SiacRMovgestTsRepository siacRMovgestTsRepository;
	
	// gestione astratta per evitare if else
	
	protected abstract T convertiMovimentoGestione(SiacTMovgestFin siacTMovgest,OttimizzazioneMovGestDto ottimizzazioneDto);
	
	protected abstract T convertiMovimentoGestione(SiacTMovgestFin siacTMovgest);
	
	protected abstract T convertiMovimentoGestioneOPT(SiacTMovgestFin siacTMovgest,OttimizzazioneMovGestDto ottimizzazioneDto);
	
	protected abstract ST convertiSubMovimento(SiacTMovgestTsFin siacTMovgestTs, SiacTMovgestFin siacTMovgest, boolean caricaDatiUlteriori);
	
	protected abstract ST convertiSubMovimento(SiacTMovgestTsFin siacTMovgestTs, SiacTMovgestFin siacTMovgest, boolean caricaDatiUlteriori,OttimizzazioneMovGestDto ottimizzazioneDto);
	
	protected abstract ST convertiSubMovimentoOPT(SiacTMovgestTsFin siacTMovgestTs, SiacTMovgestFin siacTMovgest,OttimizzazioneMovGestDto ottimizzazioneDto);
	
	/**
	  * Lavora tramite siacTMovgestId
	  * @param siacTMovgestId
	  * @return
	  * @throws Throwable 
	  */
	 public MovimentoGestione ricercaMovimentoPkByUid(Integer siacTMovgestId,Richiedente richiedente, Ente ente, String tipoMovimento, boolean caricaDatiUlteriori) {
	  SiacTMovgestFin siacTMovgest = siacTMovgestRepository.findOne(siacTMovgestId);
		  
	  String annoEsercizio = siacTMovgest.getSiacTBil().getSiacTPeriodo().getAnno();
	  Integer annoMovimento = siacTMovgest.getMovgestAnno();
	  BigDecimal numeroMovimento = siacTMovgest.getMovgestNumero();
		  
	  T trovatoMovGestione = (T) ricercaMovimentoPk(richiedente, ente, annoEsercizio, annoMovimento, numeroMovimento, tipoMovimento, caricaDatiUlteriori);
		  
	  //Termino restituendo l'oggetto di ritorno: 
        return trovatoMovGestione;
	}
	
	 /**
	  * Operazione "core" di annullamento di impegni o accertamenti
	  * @param ente
	  * @param richiedente
	  * @param movimentoGestione
	  * @param tipoMovimento
	  * @param datiOperazioneAnnullamento
	  * @param bilancio
	  * @return
	  */
	public EsitoAggiornamentoMovimentoGestioneDto annullaMovimento(Ente ente, Richiedente richiedente, MovimentoGestione movimentoGestione, String tipoMovimento, DatiOperazioneDto datiOperazioneAnnullamento, Bilancio bilancio){

		EsitoAggiornamentoMovimentoGestioneDto esito=new EsitoAggiornamentoMovimentoGestioneDto();

		MovgestPkDto chiaveMov = null;
		SiacTMovgestTsFin siacTMovgestTsImpOrAcc = null;
		
		if(movimentoGestione!=null){
			String annoBilancio = ""+bilancio.getAnno();
			
			//CI SONO TRE POSSIBILITA': 1- dobbiamo annullare tutto un impegno 2 - uno (o piu') sub impegni indicati 3- una modifica di importo
			List<ImpegnoInAnnullamentoInfoDto> lista = null;
			
			boolean annullaImpegno=false;
			boolean doppiaGestione=false;
						
			if(movimentoGestione instanceof Impegno){
				if( ((Impegno)movimentoGestione).getElencoSubImpegni()!=null && ((Impegno) movimentoGestione).getElencoSubImpegni().size()>0){
					
					chiaveMov = new MovgestPkDto(annoBilancio, movimentoGestione.getAnnoMovimento(), movimentoGestione.getNumeroBigDecimal(), tipoMovimento);
					
					MovGestMainModelEntityInfoDto oggettoConSub = getListaSiactTMovgestTs(chiaveMov,((Impegno)movimentoGestione).getElencoSubImpegni(),datiOperazioneAnnullamento);
					List<MovGestModelEntityInfoDto> subs = oggettoConSub.getListaSub();
					siacTMovgestTsImpOrAcc = oggettoConSub.getSiacTMovgestTs();
					
					lista = annullamentoTabelleMovgestModSubImpegno(subs, datiOperazioneAnnullamento,bilancio);
					annullaImpegno=true;
					
					//DOPPIA GESTIONE
					if (lista!=null && lista.size()==1) {
						MovimentoGestione movGes=lista.get(0).getMovGestModelEntityInfoDto().getMovimentoGestione();
						boolean inserireDoppiaGestione = inserireDoppiaGestione(bilancio, ((Impegno) movGes), datiOperazioneAnnullamento);
						if(inserireDoppiaGestione){
							doppiaGestione=true;
						}
					}
					
					
				} else if(((Impegno)movimentoGestione).getListaModificheMovimentoGestioneSpesa()!=null && ((Impegno)movimentoGestione).getListaModificheMovimentoGestioneSpesa().size()>0){
					/*
					 *  Routine per l'annullamento della modificaMovGestioneSpesa
					 */
					// prendo l'unico elemento della lista
					lista = new ArrayList<ImpegnoInAnnullamentoInfoDto>();
					for(ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa : ((Impegno)movimentoGestione).getListaModificheMovimentoGestioneSpesa()){
						ImpegnoInAnnullamentoInfoDto infoAnnullamento =  getDatiPerAnnullamentoModificaMovGest(datiOperazioneAnnullamento, modificaMovimentoGestioneSpesa);
						lista.add(infoAnnullamento);
					}
					siacTMovgestTsImpOrAcc = getSiacTMovgestDaAnnullamentoModificaMovGest(datiOperazioneAnnullamento, ((Impegno)movimentoGestione).getListaModificheMovimentoGestioneSpesa().get(0).getUid());
					
					
				} else {
					annullaImpegno=true;
					SiacTMovgestFin siacTMovgest = siacTMovgestRepository.findOne(movimentoGestione.getUid());
					siacTMovgestTsImpOrAcc = estraiTestata(siacTMovgest);
					lista = annullamentoTabelleMovgestMod(siacTMovgest, datiOperazioneAnnullamento);
				}
			}else if(movimentoGestione instanceof Accertamento){
				if( ((Accertamento)movimentoGestione).getElencoSubAccertamenti()!=null && ((Accertamento) movimentoGestione).getElencoSubAccertamenti().size()>0){
					
					chiaveMov = new MovgestPkDto(annoBilancio, movimentoGestione.getAnnoMovimento(), movimentoGestione.getNumeroBigDecimal(), tipoMovimento);
					
					MovGestMainModelEntityInfoDto oggettoConSub = getListaSiactTMovgestTsAccertamenti(chiaveMov,((Accertamento)movimentoGestione).getElencoSubAccertamenti(),datiOperazioneAnnullamento);
					List<MovGestModelEntityInfoDto> subs = oggettoConSub.getListaSub();
					siacTMovgestTsImpOrAcc = oggettoConSub.getSiacTMovgestTs();
					
					lista = annullamentoTabelleMovgestModSubImpegno(subs, datiOperazioneAnnullamento,bilancio);
					annullaImpegno=true;
					
					//DOPPIA GESTIONE
					if (lista!=null && lista.size()==1) {
						MovimentoGestione movGes=lista.get(0).getMovGestModelEntityInfoDto().getMovimentoGestione();
						boolean inserireDoppiaGestione = inserireDoppiaGestione(bilancio, ((Accertamento) movGes), datiOperazioneAnnullamento);
						if(inserireDoppiaGestione){
							doppiaGestione=true;
						}
					}
				} else if(((Accertamento)movimentoGestione).getListaModificheMovimentoGestioneEntrata()!=null && ((Accertamento)movimentoGestione).getListaModificheMovimentoGestioneEntrata().size()>0){
					/*
					 *  Routine per l'annullamento della modificaMovGestioneEntrata
					 */
					// prendo l'unico elemento della lista
					lista = new ArrayList<ImpegnoInAnnullamentoInfoDto>();
					for(ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata : ((Accertamento)movimentoGestione).getListaModificheMovimentoGestioneEntrata()){
						ImpegnoInAnnullamentoInfoDto infoAnnullamento =  getDatiPerAnnullamentoModificaMovGest(datiOperazioneAnnullamento, modificaMovimentoGestioneEntrata);
						lista.add(infoAnnullamento);
					}
					siacTMovgestTsImpOrAcc = getSiacTMovgestDaAnnullamentoModificaMovGest(datiOperazioneAnnullamento, ((Accertamento)movimentoGestione).getListaModificheMovimentoGestioneEntrata().get(0).getUid());
				} else {
					annullaImpegno=true;
					SiacTMovgestFin siacTMovgest = siacTMovgestRepository.findOne(movimentoGestione.getUid());
					siacTMovgestTsImpOrAcc = estraiTestata(siacTMovgest);
					lista = annullamentoTabelleMovgestMod(siacTMovgest, datiOperazioneAnnullamento);
				}
			}
			
			applicaAnnullamenti(datiOperazioneAnnullamento, lista,annullaImpegno, tipoMovimento);
			if (doppiaGestione) {
				SiacTMovgestTsFin siacTMovgestTs=lista.get(0).getMovGestModelEntityInfoDto().getSiacTMovgestTs();
				List<Errore> esitoCancellaSub=cancellaSubImpegnoRibaltato(richiedente, ente, bilancio, siacTMovgestTs, tipoMovimento, datiOperazioneAnnullamento);
				if (esitoCancellaSub!=null && esitoCancellaSub.size()>0) {
					esito.setListaErrori(esitoCancellaSub);
					esito.setMovimentoGestione(null);
					return esito;
				}
			}
		}
		MovimentoGestione mg= null;
		if(movimentoGestione instanceof Impegno){
			if(chiaveMov!=null){
				entityRefresh(siacTMovgestTsImpOrAcc);
				mg = (Impegno) ricercaMovimentoPk(richiedente, ente, chiaveMov.getAnnoEsercizio(), chiaveMov.getAnnoMovimento(),
						chiaveMov.getNumeroMovimento(), tipoMovimento, true);
			} else {
				entityRefresh(siacTMovgestTsImpOrAcc);
				mg = (Impegno)  ricercaMovimentoPkByUid(siacTMovgestTsImpOrAcc.getSiacTMovgest().getUid(), richiedente, ente, tipoMovimento, true);
			}

		}else{
			if(chiaveMov!=null){
				entityRefresh(siacTMovgestTsImpOrAcc);
				mg = (Accertamento) ricercaMovimentoPk(richiedente, ente, chiaveMov.getAnnoEsercizio(), chiaveMov.getAnnoMovimento(),
						chiaveMov.getNumeroMovimento(), tipoMovimento, true);
			} else {
				entityRefresh(siacTMovgestTsImpOrAcc);
				mg = (Accertamento) ricercaMovimentoPkByUid(siacTMovgestTsImpOrAcc.getSiacTMovgest().getUid(), richiedente, ente, tipoMovimento, true);
			}
		}
		
		esito.setListaErrori(null);
		esito.setMovimentoGestione(mg);
		
		return esito;
	}
	
	/**
	 * si occupa della cancellazione del sub impegno in doppia gestione
	 * @param richiedente
	 * @param ente
	 * @param bilancio
	 * @param siacTMovgestTs
	 * @param tipoMovimento
	 * @param datiOperazioneDto
	 * @return
	 */
	private List<Errore> cancellaSubImpegnoRibaltato(Richiedente richiedente, Ente ente, Bilancio bilancio, SiacTMovgestTsFin siacTMovgestTs, String tipoMovimento, DatiOperazioneDto datiOperazioneDto){
		List<Errore> listaErrori=new ArrayList<Errore>();

		int anno=bilancio.getAnno();

		SiacTMovgestFin siacTMovgestPadreRibaltato=null;
				
		if (siacTMovgestTs!=null && siacTMovgestTs.getMovgestTsIdPadre()!=null) {
			SiacTMovgestTsFin siacTMovgestTsPadre=siacTMovgestTsRepository.findOne(siacTMovgestTs.getMovgestTsIdPadre());
			if (siacTMovgestTsPadre!=null) {
				siacTMovgestPadreRibaltato = movimentoGestioneDao.findByEnteAnnoNumeroBilancioValido(ente.getUid(), anno, new BigDecimal(siacTMovgestTsPadre.getMovgestTsCode()), tipoMovimento, String.valueOf(anno+1), getNow());
			}
		}

		if (siacTMovgestPadreRibaltato!=null) {

			List<SiacTMovgestTsFin> listaSiacTMovgestTsEliminatoResiduo =siacTMovgestTsRepository.findSubMovgestTsByCodeAndMovgestId(ente.getUid(), datiOperazioneDto.getTs(), siacTMovgestPadreRibaltato.getMovgestId(), siacTMovgestTs.getMovgestTsCode());

			if (listaSiacTMovgestTsEliminatoResiduo!=null && listaSiacTMovgestTsEliminatoResiduo.size()>0) {
				SiacTMovgestTsFin siacTMovgestTsEliminatoResiduo=listaSiacTMovgestTsEliminatoResiduo.get(0);
				if (siacTMovgestTsEliminatoResiduo!=null) {
					if (tipoMovimento.equalsIgnoreCase(CostantiFin.MOVGEST_TIPO_IMPEGNO)) {
						// caso movimenti collegati impegno
						List<SiacTLiquidazioneFin> elencoSiacTLiquidazione=findLiquidazioniValideFromSubImpegno(siacTMovgestTsEliminatoResiduo);
						if (elencoSiacTLiquidazione!=null && elencoSiacTLiquidazione.size()>0) {
							listaErrori.add(ErroreFin.ESISTONO_MOVIMENTI_COLLEGATI.getErrore("Cancellazione subimpegno residuo",""));
							return listaErrori;
						}
						//Eliminare sub
						DatiOperazioneDto datiOperazioneCancella = new DatiOperazioneDto(System.currentTimeMillis(), Operazione.CANCELLAZIONE_LOGICA_RECORD, datiOperazioneDto.getSiacTEnteProprietario(), richiedente.getAccount().getId());
						SiacTMovgestTsFin siacTMovgestTsResiduoCanc = DatiOperazioneUtil.cancellaRecord(siacTMovgestTsEliminatoResiduo, siacTMovgestTsRepository, datiOperazioneCancella, siacTAccountRepository);
					}else if (tipoMovimento.equalsIgnoreCase(CostantiFin.MOVGEST_TIPO_ACCERTAMENTO)) {
						// caso movimenti collegati accertamento
						List<SiacTOrdinativoTsDetFin> elencoQuote=findQuoteValideFromAccertamentoSubAccertamento(ente.getUid(), siacTMovgestTsEliminatoResiduo);
						if (elencoQuote!=null && elencoQuote.size()>0) {
							listaErrori.add(ErroreFin.ESISTONO_MOVIMENTI_COLLEGATI.getErrore("Cancellazione subaccertamento residuo",""));
							return listaErrori;
						}
						//Eliminare sub
						DatiOperazioneDto datiOperazioneCancella = new DatiOperazioneDto(System.currentTimeMillis(), Operazione.CANCELLAZIONE_LOGICA_RECORD, datiOperazioneDto.getSiacTEnteProprietario(), richiedente.getAccount().getId());
						SiacTMovgestTsFin siacTMovgestTsResiduoCanc = DatiOperazioneUtil.cancellaRecord(siacTMovgestTsEliminatoResiduo, siacTMovgestTsRepository, datiOperazioneCancella, siacTAccountRepository);
					}
				}
			}
		}
		
		//Termino restituendo l'oggetto di ritorno: 
        return listaErrori;
	}
	
	/**
	 * Esegue i controlli di merito per l'annullamento di un accertamento.
	 * Si tratta dei controlli sui dati in input che devono essere verificati con accessi al database.
	 * @param richiedente
	 * @param ente
	 * @param bilancio
	 * @param accertamento
	 * @param datiOperazione
	 * @return
	 */
	public List<Errore> controlliDiMeritoAnnullamentoAccertamentoServizio(Richiedente richiedente, Ente ente, Bilancio bilancio, Accertamento accertamento, DatiOperazioneDto datiOperazione){
		List<Errore> listaErrori = new ArrayList<Errore>();
	
		if(accertamento.getElencoSubAccertamenti()!=null && accertamento.getElencoSubAccertamenti().size()>0){
			// Si cerca di annullare uno (o pi�) sub-accertamenti
			// Eventuali controlli da effettuare in caso di annullamento di uno (o pi�) sub-accertamenti

			MovgestPkDto chiaveMov = new MovgestPkDto(String.valueOf(bilancio.getAnno()), accertamento.getAnnoMovimento(), accertamento.getNumeroBigDecimal(), CostantiFin.MOVGEST_TIPO_ACCERTAMENTO);
			MovGestMainModelEntityInfoDto oggettoConSub = getListaSiactTMovgestTsAccertamenti(chiaveMov,accertamento.getElencoSubAccertamenti(), datiOperazione);
			List<MovGestModelEntityInfoDto> subs = oggettoConSub.getListaSub();

			// Verifico che non ci siano quote di ordinativi di incasso collegate ai sub-accertamenti che si sta cercando di annullare
			if(subs!=null && subs.size() > 0){
				for(MovGestModelEntityInfoDto siacTMovgestTsInfo : subs){
					SiacTMovgestTsFin siacTMovgestTs = siacTMovgestTsInfo.getSiacTMovgestTs();
					boolean esistonoOrdinativi = esistonoOrdinativiIncassoFromAccertamentoSubAccertamento(siacTMovgestTs);
					if(esistonoOrdinativi){
						String datiSubAccertamento = siacTMovgestTs.getSiacTMovgest().getMovgestAnno() + "/" + siacTMovgestTs.getSiacTMovgest().getMovgestNumero() + "-" + siacTMovgestTs.getMovgestTsCode();
						listaErrori.add(ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("Annullamento sub-accertamento: " + datiSubAccertamento, " esistono ordinativi di incasso collegati "));
					}
				}
			}
			
		} else if(accertamento.getListaModificheMovimentoGestioneEntrata()!=null && accertamento.getListaModificheMovimentoGestioneEntrata().size()>0){
			// Si cerca di annullare una modifica dell'accertamento
			// controlli da effettuare in caso di annullamento di modifiche di accertamento andranno inseriti qui
			SiacConModificaStato entityDaCheckareStato = null;
			
			//Dobbiamo controllare che la modifica per la quale e' stato richiesto l'annullamento non sia gia' stata annullata
			ModificaMovimentoGestioneEntrata modificaInAnnullamento =  accertamento.getListaModificheMovimentoGestioneEntrata().get(0);
			ImpegnoInAnnullamentoInfoDto infoDto = null;
			if(modificaInAnnullamento!=null){
				infoDto = getDatiPerAnnullamentoModificaMovGest(datiOperazione, modificaInAnnullamento);
				if(infoDto!=null && infoDto.getSiacRMovgestTsSogModListDaAnnullare()!=null && infoDto.getSiacRMovgestTsSogModListDaAnnullare().size()>0){
					//modifica sul soggetto
					entityDaCheckareStato = infoDto.getSiacRMovgestTsSogModListDaAnnullare().get(0);
				} else if(infoDto!=null && infoDto.getSiacTMovgestTsDetModDaAnnullare()!=null && infoDto.getSiacTMovgestTsDetModDaAnnullare().size()>0){
					//modifica importo
					entityDaCheckareStato = infoDto.getSiacTMovgestTsDetModDaAnnullare().get(0);
				}
			}
			
			if(entityDaCheckareStato == null || DatiOperazioneUtil.isInStato(entityDaCheckareStato, CostantiFin.D_MODIFICA_STATO_ANNULLATO)){
				//lanciare errore, modifica gia annullata
			}
			
			//non e' annullata, altri controlli qui di seguito:
			
			if(infoDto!=null && infoDto.getSiacTMovgestTsDetModDaAnnullare()!=null && infoDto.getSiacTMovgestTsDetModDaAnnullare().size()>0){
				//qui i controlli specifici per tipologia modifica importo
				
				SiacTMovgestTsDetModFin modificaDb = (SiacTMovgestTsDetModFin) entityDaCheckareStato;
				BigDecimal importoModificaDb = modificaDb.getMovgestTsDetImporto();
				
				// controllo il delta importi
				
//				Verifica della disponibilit� ad utilizzare  
				//Se  l�Accertamento � interessato da una  modifica  di segno negativo occorre calcolare la nuova disponibilit� ad utilizzare e verificare che risulti ancora >= 0
				//	NuovaDispUtilizare (acc) = VecchiaDispUtilizare (Acc) + TotaleModifiche (Acc)  
				//In caso di errore inviare il messaggio   <FIN_ERR_0230 Disponibilit� Insufficiente (<operazione>: Modifica accertamento num:
				//riportare il numero identificativo della modifica; <tipo>: disponibilit� ad utilizzare per vincoli.>
				//verifichiamo l'importo rispetto ai vincoli con gli impegni:
				
				SiacTMovgestTsFin siacTMovgestTs = modificaDb.getSiacTMovgestT();
				BigDecimal importoUtilizzabileOld = estraiImportoUtilizzabileByMovgestTsId(siacTMovgestTs.getMovgestTsId(), datiOperazione);
				
				//il new sara' dato dall'old meno il valore della modifica che stiamo annullando:
				BigDecimal importoUtilizzabileNew = importoUtilizzabileOld.subtract(importoModificaDb);
				
				List<VincoloAccertamento> vincoli = getImpegniVincolati(siacTMovgestTs);
				BigDecimal totQuoteVincoli = BigDecimal.ZERO;
				if(vincoli!=null && vincoli.size()>0){
					totQuoteVincoli = calcolaTotQuoteVincoliAcc(vincoli);
					if(importoUtilizzabileNew.compareTo(totQuoteVincoli)<0){
						//importoUtilizzabileNew e' minore della sommatoria dei vincoli: lancio errore:
						listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE.getErrore("Modifica accertamento num:" + accertamento.getNumeroBigDecimal(),"disponibilità ad utilizzare per vincoli"));
						return listaErrori;
					}
				}
				
			}
			
		} else {
			// Si cerca di annullare l'accertamento
			// Eventuali controlli da effettuare in caso di annullamento dell'accertamento andranno inseriti qui

			// Verifico che non ci siano quote di ordinativi di incasso collegate all' accertamento che si sta cercando di annullare
			SiacTMovgestFin siacTMovgest = siacTMovgestRepository.findOne(accertamento.getUid());
			SiacTMovgestTsFin siacTMovgestTs = siacTMovgestTsRepository.findMovgestTsByMovgest(ente.getUid(), datiOperazione.getTs(), siacTMovgest.getMovgestId()).get(0);
			if(siacTMovgestTs!=null){
				boolean esistonoOrdinativi = esistonoOrdinativiIncassoFromAccertamentoSubAccertamento(siacTMovgestTs);
				if(esistonoOrdinativi){
					String datiAccertamento = siacTMovgestTs.getSiacTMovgest().getMovgestAnno() + "/" + siacTMovgestTs.getSiacTMovgest().getMovgestNumero();
					listaErrori.add(ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("Annullamento accertamento: " + datiAccertamento, " esistono ordinativi di incasso collegati "));
				}
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaErrori;
	}

	/**
	 * Controlla la presenza di subordinativi di incasso (validi) per l'accertamento/subaccertamento indicato
	 * @param siacTMovgestTs
	 * @return
	 */
	private boolean esistonoOrdinativiIncassoFromAccertamentoSubAccertamento(SiacTMovgestTsFin siacTMovgestTs){
		boolean esistonoOrdinativiIncasso = false;
		// Controllo se ci sono legami tra l'accertamento / sub-accertamento ed eventuali quote di ordinativo
		List<SiacROrdinativoTsMovgestTFin> elencoSiacROrdinativoTsMovgestT = siacTMovgestTs.getSiacROrdinativoTsMovgestTs();
		//filtriamo  in base alla validita':
		elencoSiacROrdinativoTsMovgestT = DatiOperazioneUtil.soloValidi(elencoSiacROrdinativoTsMovgestT, null);
		if(elencoSiacROrdinativoTsMovgestT!=null && elencoSiacROrdinativoTsMovgestT.size()>0){
			for(SiacROrdinativoTsMovgestTFin siacROrdinativoTsMovgestT : elencoSiacROrdinativoTsMovgestT){
				// ho trovato legame con quota valido
				// controllo se la quota � valida
				SiacTOrdinativoTFin siacTOrdinativoT = siacROrdinativoTsMovgestT.getSiacTOrdinativoT();
				if(DatiOperazioneUtil.isValido(siacTOrdinativoT, null)){
					// la quota � valida
					// adesso controllo che sia in stato valido anche l'ordinativo a della quota
					SiacTOrdinativoFin siacTOrdinativo = siacTOrdinativoT.getSiacTOrdinativo();
					List<SiacROrdinativoStatoFin> elencoSiacROrdinativoStato = siacTOrdinativo.getSiacROrdinativoStatos();
					elencoSiacROrdinativoStato = DatiOperazioneUtil.soloValidi(elencoSiacROrdinativoStato, null);
					if(elencoSiacROrdinativoStato!=null && elencoSiacROrdinativoStato.size()>0){
						for(SiacROrdinativoStatoFin siacROrdinativoStato : elencoSiacROrdinativoStato){
							if(!siacROrdinativoStato.getSiacDOrdinativoStato().getOrdStatoCode().equalsIgnoreCase(CostantiFin.D_ORDINATIVO_STATO_ANNULLATO)){
								esistonoOrdinativiIncasso = true;
							}
						}
					}
				}
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return esistonoOrdinativiIncasso;
	}
	
	/**
	 * Metodo privato ad uso interno del metodo "annullaMovimento"
	 * @param datiOperazioneAnnullamento
	 * @param lista
	 * @param annullaImpegno
	 */
	private void applicaAnnullamenti( DatiOperazioneDto datiOperazioneAnnullamento,List<ImpegnoInAnnullamentoInfoDto> lista,boolean annullaImpegno,String tipoMovimento){
		Integer idEnte = datiOperazioneAnnullamento.getSiacTEnteProprietario().getEnteProprietarioId();
		Timestamp now = datiOperazioneAnnullamento.getTs();
		if(lista!=null && lista.size()>0){
			for(ImpegnoInAnnullamentoInfoDto iterato : lista){
				
				SiacDModificaStatoFin statoAnnullato = siacDModificaStatoRepository.findByCode(idEnte, now, CostantiFin.D_MODIFICA_STATO_ANNULLATO).get(0);
				
				//EVENTUALI MODIFCHE SUL SOG CLASSE
				List<SiacRMovgestTsSogclasseModFin> siacRMovgestTsSogclasseModList_Da_Annullare= iterato.getSiacRMovgestTsSogclasseModListDaAnnullare();
				if(siacRMovgestTsSogclasseModList_Da_Annullare!=null && siacRMovgestTsSogclasseModList_Da_Annullare.size()>0){
					for(SiacRMovgestTsSogclasseModFin it : siacRMovgestTsSogclasseModList_Da_Annullare){
						DatiOperazioneUtil.cambiaModificaStato(it, siacRMovgestTsSogClasseModRepository, datiOperazioneAnnullamento, statoAnnullato,siacRModificaStatoRepository, siacTAccountRepository);
					}
				}
				
				//EVENTUALI MODIFCHE SUL SOGGETTO:
				List<SiacRMovgestTsSogModFin> siacRMovgestTsSogModList_Da_Annullare= iterato.getSiacRMovgestTsSogModListDaAnnullare();
				if(siacRMovgestTsSogModList_Da_Annullare!=null && siacRMovgestTsSogModList_Da_Annullare.size()>0){
					for(SiacRMovgestTsSogModFin it : siacRMovgestTsSogModList_Da_Annullare){
						DatiOperazioneUtil.cambiaModificaStato(it, siacRMovgestTsSogModRepository, datiOperazioneAnnullamento, statoAnnullato,siacRModificaStatoRepository, siacTAccountRepository);
					}
				}
				
				//EVENTUALI MODIFICHE SUGLI IMPORTI:
				List<SiacTMovgestTsDetModFin> siacTMovgestTsDetMod_Da_Annullare= iterato.getSiacTMovgestTsDetModDaAnnullare();
				if(siacTMovgestTsDetMod_Da_Annullare!=null && siacTMovgestTsDetMod_Da_Annullare.size()>0){
					for(SiacTMovgestTsDetModFin it : siacTMovgestTsDetMod_Da_Annullare){
						DatiOperazioneUtil.cambiaModificaStato(it, siacTMovgestTsDetModRepository, datiOperazioneAnnullamento, statoAnnullato,siacRModificaStatoRepository, siacTAccountRepository);
					}
				}
			    List<ImportoImpegnoInAnnullamentoInfoDto> elencoModificheImporti = iterato.getElencoModifiche();
			    if(elencoModificheImporti!=null && elencoModificheImporti.size()>0){
				     for(ImportoImpegnoInAnnullamentoInfoDto it: elencoModificheImporti){
					      SiacTMovgestTsDetFin daAggiornare = it.getSiacTMovgestTsDet();
					      BigDecimal oldImporto = daAggiornare.getMovgestTsDetImporto();
					      BigDecimal totMod = it.getTotaleModifiche();
					      BigDecimal nuovoImporto = oldImporto.subtract(totMod);
					      DatiOperazioneDto datiUpdate = DatiOperazioneDto.buildDatiOperazione(datiOperazioneAnnullamento, Operazione.MODIFICA);
					      
					      SiacTMovgestTsDetFin nuovoAttuale = saveImporto(daAggiornare, nuovoImporto, datiUpdate, null, CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE);
					      
					      
					      SiacTMovgestTsFin siacTMovgestTs = daAggiornare.getSiacTMovgestT();
					      List<SiacTMovgestTsDetFin> lImporti = siacTMovgestTs.getSiacTMovgestTsDets();
					      
					      //FIX PER JIRA  SIAC-3400
					      // Quando viene annullata una modifica di importo di accertamenti di competenza e futuri,
					      // dovrebbe essere controaggiornato non solo l'importo attuale ma anche quello utilizzabile
					      SiacTMovgestTsDetFin nuovoUtilizzabile = null;
					      if(CostantiFin.MOVGEST_TIPO_ACCERTAMENTO.equals(tipoMovimento)){
					    	  List<SiacTMovgestTsDetFin> utilizzabileL = siacTMovgestTsDetRepository.findValidoByTipo(idEnte, getNow(), CostantiFin.MOVGEST_TS_DET_TIPO_UTILIZZABILE, siacTMovgestTs.getMovgestTsId());
					    	  SiacTMovgestTsDetFin daAggiornareUtilizzabile = utilizzabileL.get(0);
					    	  BigDecimal utilizzabileOld = estraiImportoUtilizzabileByMovgestTsId(siacTMovgestTs.getMovgestTsId(), datiOperazioneAnnullamento);
					    	  BigDecimal nuovoImportoUtilizzabile = utilizzabileOld.subtract(totMod);
					    	  nuovoUtilizzabile = saveImporto(daAggiornareUtilizzabile, nuovoImportoUtilizzabile, datiUpdate, null, CostantiFin.MOVGEST_TS_DET_TIPO_UTILIZZABILE);
					    	  lImporti = DatiOperazioneUtil.replaceInList(lImporti, nuovoUtilizzabile);
					      }
					      //
					      
					      lImporti = DatiOperazioneUtil.replaceInList(lImporti, nuovoAttuale);
					      siacTMovgestTs.setSiacTMovgestTsDets(lImporti);
					      
					      //NON PENSO ABBIA SENSO METTERE L'IMPEGNPO IN STATO ANNULLATO
					      if(annullaImpegno){
					    	SiacRMovgestTsStatoFin siacRMovgestTsStato = saveStatoImpegno(datiOperazioneAnnullamento, CostantiFin.MOVGEST_STATO_ANNULLATO, siacTMovgestTs); 
					  		siacTMovgestTs.setSiacRMovgestTsStatos(toList(siacRMovgestTsStato));
					      }
					      /////////////////////
				     }
			    }
			
			}
		}
	}

	

	/**
	 * chiaveMovimento e' il riferimento all'impegno/accertamento
	 * listaSub e' la lista di subimpegni che si vuole caricare
	 * @param chiaveMovimento
	 * @param listaSub
	 * @param datiOperazioneDto
	 * @return
	 */
	public MovGestMainModelEntityInfoDto getListaSiactTMovgestTs(MovgestPkDto chiaveMovimento, List<SubImpegno> listaSub,DatiOperazioneDto datiOperazioneDto){
		
		MovGestMainModelEntityInfoDto oggettoRitorno = new MovGestMainModelEntityInfoDto();
		
		Timestamp ts = datiOperazioneDto.getTs();
		int enteid = datiOperazioneDto.getSiacTEnteProprietario().getUid();
		
		//nuova gestione per numero:
		SiacTMovgestFin siacTMovgest  = siacTMovgestRepository.ricercaSiacTMovgestPk(enteid, chiaveMovimento.getAnnoEsercizio(), chiaveMovimento.getAnnoMovimento(), chiaveMovimento.getNumeroMovimento(), chiaveMovimento.getTipoTMovGest());
		SiacTMovgestTsFin siacTMovgestTsPadre = siacTMovgestTsRepository.findMovgestTsByMovgest(enteid, ts, siacTMovgest.getMovgestId()).get(0);			
		List<SiacTMovgestTsFin> figliAlls = siacTMovgestTsRepository.findListaSiacTMovgestTsFigli(enteid, getNow(), siacTMovgestTsPadre.getMovgestTsId());
		
		oggettoRitorno.setSiacTMovgestTs(siacTMovgestTsPadre);
		
		List<SiacTMovgestTsFin> subs = new ArrayList<SiacTMovgestTsFin>();
		for(SubImpegno it: listaSub){
			SiacTMovgestTsFin figlioIt = findByCode(figliAlls, it.getNumeroBigDecimal().toString());
			subs.add(figlioIt);
		}
		//
		List<MovGestModelEntityInfoDto> listaRitorno = new ArrayList<MovGestModelEntityInfoDto>();
		if(subs!=null && subs.size()>0){
			for(SiacTMovgestTsFin subsIt : subs){
				MovGestModelEntityInfoDto movGestModelEntityInfoDto = new MovGestModelEntityInfoDto();
				SubImpegno subImpegno =  findByCode(listaSub, new BigDecimal(subsIt.getMovgestTsCode()));
				movGestModelEntityInfoDto.setMovimentoGestione(subImpegno);
				movGestModelEntityInfoDto.setSiacTMovgestTs(subsIt);
				listaRitorno.add(movGestModelEntityInfoDto);
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
		oggettoRitorno.setListaSub(listaRitorno);
		return oggettoRitorno;
	}
	
	/**
	 * Metodo di comodo per cercare tra una lista di SiacTMovgestTsFin quello con il codice indicato
	 * @param lista
	 * @param code
	 * @return
	 */
	private SiacTMovgestTsFin findByCode(List<SiacTMovgestTsFin> lista, String code){
		SiacTMovgestTsFin finded = null;
		for(SiacTMovgestTsFin it : lista){
			if(StringUtilsFin.sonoUgualiTrimmed(it.getMovgestTsCode(), code)){
				finded = it;
				break;
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return finded;
	}
	
	/**
	 * Metodo di comodo per cercare tra una lista di SiacTMovgestTsFin quello con il codice indicato
	 * @param lista
	 * @param code
	 * @return
	 */
	public <MVG extends MovimentoGestione> MVG findByCode(List<MVG> lista, BigDecimal code){
		MVG finded = null;
		for(MVG it : lista){
			if(StringUtilsFin.sonoUguali(it.getNumeroBigDecimal(), code)){
				finded = it;
				break;
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return finded;
	}
	
	/**
	 * 
	 * @param chiaveMovimento
	 * @param listaSub
	 * @param datiOperazioneDto
	 * @return
	 */
	public MovGestMainModelEntityInfoDto getListaSiactTMovgestTsAccertamenti(MovgestPkDto chiaveMovimento,List<SubAccertamento> listaSub,DatiOperazioneDto datiOperazioneDto){
		MovGestMainModelEntityInfoDto oggettoRitorno = new MovGestMainModelEntityInfoDto();
		Timestamp ts = datiOperazioneDto.getTs();
		int enteid = datiOperazioneDto.getSiacTEnteProprietario().getUid();
		
		//nuova gestione per numero (non si deve usare l'id!)
		SiacTMovgestFin siacTMovgest  = siacTMovgestRepository.ricercaSiacTMovgestPk(enteid, chiaveMovimento.getAnnoEsercizio(), chiaveMovimento.getAnnoMovimento(), chiaveMovimento.getNumeroMovimento(), chiaveMovimento.getTipoTMovGest());
		SiacTMovgestTsFin siacTMovgestTsPadre = siacTMovgestTsRepository.findMovgestTsByMovgest(enteid, ts, siacTMovgest.getMovgestId()).get(0);			
		List<SiacTMovgestTsFin> figliAlls = siacTMovgestTsRepository.findListaSiacTMovgestTsFigli(enteid, getNow(), siacTMovgestTsPadre.getMovgestTsId());
		
		
		oggettoRitorno.setSiacTMovgestTs(siacTMovgestTsPadre);
		
		List<SiacTMovgestTsFin> subs = new ArrayList<SiacTMovgestTsFin>();
		for(SubAccertamento it: listaSub){
			
			SiacTMovgestTsFin figlioIt = findByCode(figliAlls, it.getNumeroBigDecimal().toString());
			subs.add(figlioIt);
		}
		
		List<MovGestModelEntityInfoDto> listaRitorno = new ArrayList<MovGestModelEntityInfoDto>();
		if(subs!=null && subs.size()>0){
			for(SiacTMovgestTsFin subsIt : subs){
				MovGestModelEntityInfoDto movGestModelEntityInfoDto = new MovGestModelEntityInfoDto();
				SubAccertamento subImpegno =  findByCode(listaSub, new BigDecimal(subsIt.getMovgestTsCode()));
				movGestModelEntityInfoDto.setMovimentoGestione(subImpegno);
				movGestModelEntityInfoDto.setSiacTMovgestTs(subsIt);
				listaRitorno.add(movGestModelEntityInfoDto);
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
		oggettoRitorno.setListaSub(listaRitorno);
        return oggettoRitorno;
	}

	/**
	 * routine da usare quando si intende annullare l'impegno e tutti i sub
	 * @param siacTMovgest
	 * @param datiOperazioneAnnullamento
	 * @return
	 */
	private List<ImpegnoInAnnullamentoInfoDto> annullamentoTabelleMovgestMod(SiacTMovgestFin siacTMovgest, DatiOperazioneDto datiOperazioneAnnullamento){
		List<ImpegnoInAnnullamentoInfoDto> impegnoInAnnullamentoInfoDto = new ArrayList<ImpegnoInAnnullamentoInfoDto>();
		Integer idEnte = datiOperazioneAnnullamento.getSiacTEnteProprietario().getEnteProprietarioId();
		Timestamp now = datiOperazioneAnnullamento.getTs();
		if(siacTMovgest != null){
			Integer idMovgest = siacTMovgest.getMovgestId();
			//impegno:
			List<SiacTMovgestTsFin> listaSiacTMovgestTs = siacTMovgestTsRepository.findMovgestTsByMovgest(idEnte, now, idMovgest);
			if(listaSiacTMovgestTs!=null && listaSiacTMovgestTs.size()>0){
				//listaSiacTMovgestTs dovrebbe avere un solo elemento, ovvero la riga in siac_t_movgest_ts corrispondente all'impegno:
				SiacTMovgestTsFin siacTMovgestTsImpegno = listaSiacTMovgestTs.get(0);
				Integer idMovgestTs = siacTMovgestTsImpegno.getMovgestTsId();
				ImpegnoInAnnullamentoInfoDto infoAnnullamento= getDatiPerAnnullamentoImpegno(datiOperazioneAnnullamento, idMovgestTs);
				if(infoAnnullamento!=null){
					impegnoInAnnullamentoInfoDto.add(infoAnnullamento);
				}
				//sub impegni:
				List<SiacTMovgestTsFin> listaSiacTMovgestTs_SubImpegni = siacTMovgestTsRepository.findListaSiacTMovgestTsFigli(idEnte, now, idMovgestTs);
				if(listaSiacTMovgestTs_SubImpegni!=null && listaSiacTMovgestTs_SubImpegni.size()>0){
					for(SiacTMovgestTsFin subImpIt : listaSiacTMovgestTs_SubImpegni){
						Integer idMovgestTs_Sub = subImpIt.getMovgestTsId();
						ImpegnoInAnnullamentoInfoDto infoAnnullamento_Sub= getDatiPerAnnullamentoImpegno(datiOperazioneAnnullamento, idMovgestTs_Sub);
						if(infoAnnullamento_Sub!=null){
							impegnoInAnnullamentoInfoDto.add(infoAnnullamento_Sub);
						}
					}
				}
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return impegnoInAnnullamentoInfoDto;
	}
	
	/**
	 * routine da usare quando si intende annullare un sub impegno
	 * @param siacTMovgest
	 * @param datiOperazioneAnnullamento
	 * @return
	 */
	private List<ImpegnoInAnnullamentoInfoDto> annullamentoTabelleMovgestModSubImpegno(List<MovGestModelEntityInfoDto> subImpegniDaAnnullare, DatiOperazioneDto datiOperazioneAnnullamento, Bilancio bilancio){
		List<ImpegnoInAnnullamentoInfoDto> impegnoInAnnullamentoInfoDto = new ArrayList<ImpegnoInAnnullamentoInfoDto>();
		//sub impegni:
		if(subImpegniDaAnnullare!=null && subImpegniDaAnnullare.size()>0){
			for(MovGestModelEntityInfoDto subImpItInfo : subImpegniDaAnnullare){
				SiacTMovgestTsFin subImpIt = subImpItInfo.getSiacTMovgestTs();
				Integer idMovgestTs_Sub = subImpIt.getMovgestTsId();
				
				subImpItInfo.setMovimentoGestione(EntityToModelConverter.settaDatiAttoAmm(subImpIt, subImpItInfo.getMovimentoGestione()));
				ImpegnoInAnnullamentoInfoDto infoAnnullamento_Sub= getDatiPerAnnullamentoImpegno(datiOperazioneAnnullamento, idMovgestTs_Sub);
				
				if(infoAnnullamento_Sub!=null){
					boolean inserireDoppiaGest = inserireDoppiaGestione(bilancio, subImpItInfo.getMovimentoGestione(), datiOperazioneAnnullamento);
					infoAnnullamento_Sub.setAnnullaInDoppiaGestione(inserireDoppiaGest);
					infoAnnullamento_Sub.setMovGestModelEntityInfoDto(subImpItInfo);
					impegnoInAnnullamentoInfoDto.add(infoAnnullamento_Sub);
				}
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return impegnoInAnnullamentoInfoDto;
	}
	
	/**
	 * Si occupa di valutare le modifiche riguardanti le modifiche movimento spesa, individuando
	 * quelle da aggiornare, eliminare, inserire o invariate
	 * @param lista
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ModificaMovimentoGestioneSpesaInfoDto valutaModificheMovimentoSpesa(List<ModificaMovimentoGestioneSpesa> lista,DatiOperazioneDto datiOperazione){
		ModificaMovimentoGestioneSpesaInfoDto dto = new ModificaMovimentoGestioneSpesaInfoDto();
		
		int idEnte = datiOperazione.getSiacTEnteProprietario().getEnteProprietarioId();
		
		List<ModificaMovimentoGestioneSpesa> nuove = new ArrayList<ModificaMovimentoGestioneSpesa>();
		List<ModificaMovimentoGestioneSpesa> modificate = new ArrayList<ModificaMovimentoGestioneSpesa>();
		List<ModificaMovimentoGestioneSpesa> potenzialmenteModificate = new ArrayList<ModificaMovimentoGestioneSpesa>();
		List<ModificaMovimentoGestioneSpesa> invariate = new ArrayList<ModificaMovimentoGestioneSpesa>();
		
		List<AttiAmmModificatiGestioneSpesaInfoDto> attiModificati = new ArrayList<AttiAmmModificatiGestioneSpesaInfoDto>();
		List<AttiAmmModificatiGestioneSpesaInfoDto> attiInseriti = new ArrayList<AttiAmmModificatiGestioneSpesaInfoDto>();
		
		if(lista != null && lista.size() > 0){
			//Prendo le nuove e le salvo
			for(ModificaMovimentoGestioneSpesa spesa : lista){
				if(spesa.getUid() == 0){
					nuove.add(spesa);
				} else {
					potenzialmenteModificate.add(spesa);
				}
			}
			
			
			//Ciclo su quelle per le quali l'id e' diverso da zero ovvero quelle potenzialmente modificate e vado
			//quindi a capire se sono state modificate o se restano invariate:
			for(ModificaMovimentoGestioneSpesa updApp : potenzialmenteModificate){
				
				//leggo quanto vale sul db per confrontare i valori ricevuti:
				SiacTModificaFin modificaDb = siacTModificaRepository.findOne(updApp.getUid());
				
				if(modificaDb != null){
					
					//inizializzo modificata a false:
					boolean isModificata = false;
					
//						if((!updApp.getAttoAmministrativoAnno().equals(compare.getSiacTAttoAmm().getAttoammAnno()) && !updApp.getAttoAmministrativoAnno().equals("0"))
//								||	(updApp.getAttoAmministrativoNumero() != compare.getSiacTAttoAmm().getAttoammNumero())
//								||  (!updApp.getAttoAmministrativoTipoCode().equalsIgnoreCase(compare.getSiacTAttoAmm().getSiacDAttoAmmTipo().getAttoammTipoCode()))){
//								equal = false ;
//							} else if(!updApp.getAttoAmministrativoAnno().equals(compare.getSiacTAttoAmm().getAttoammAnno()) && updApp.getAttoAmministrativoAnno().equals("0")){
//								if(!updApp.getAttoAmministrativoAnno().equalsIgnoreCase(compare.getSiacTAttoAmm().getAttoammAnno())){
//									equal = false;
//								}
//							}
//							if(!updApp.getAttoAmministrativoNumero().equals(compare.getSiacTAttoAmm().getAttoammNumero()) && !updApp.getAttoAmministrativoAnno().equals("0")){
//								equal = false;
//							} else if(!updApp.getAttoAmministrativoNumero().equals(compare.getSiacTAttoAmm().getAttoammNumero()) && updApp.getAttoAmministrativoAnno().equals("0")){
//								if(updApp.getAttoAmministrativoNumero() != Integer.valueOf(compare.getSiacTAttoAmm().getAttoammNumero())){
//									equal = false;
//								}
//							}
							
					AttoAmministrativo attoAmmInInput = buildAttoAmministrativo(idEnte, updApp.getAttoAmministrativoNumero(), updApp.getAttoAmministrativoTipoCode(), Integer.valueOf(updApp.getAttoAmministrativoAnno()), updApp.getIdStrutturaAmministrativa());
					SiacTAttoAmmFin nuovoAtto = getSiacTAttoAmmFromAttoAmministrativo(attoAmmInInput, idEnte);
					SiacTAttoAmmFin oldAtto = modificaDb.getSiacTAttoAmm();
					if(!DatiOperazioneUtil.sonoUgualiAncheNull(oldAtto, nuovoAtto)){
						//L'atto amministrativo ha subito un cambiamento:
						isModificata = true;
						//raccolgo le info su questi atti amministrativi (verrano validati dopo chiamando il provvedimentoService):
						AttiAmmModificatiGestioneSpesaInfoDto attoMod = new AttiAmmModificatiGestioneSpesaInfoDto(updApp.getAttoAmministrativo(), nuovoAtto, oldAtto, updApp);
						attiModificati.add(attoMod);
					}
					
					if(!updApp.getDescrizioneModificaMovimentoGestione().trim().equalsIgnoreCase(modificaDb.getModDesc().trim())){
						isModificata = true;
					}
					
					if(updApp.getTipoModificaMovimentoGestione()!=null && modificaDb.getSiacDModificaTipo()!=null){
						if(!updApp.getTipoModificaMovimentoGestione().equalsIgnoreCase(modificaDb.getSiacDModificaTipo().getModTipoCode())){
							isModificata = true;
						}
					} else if(updApp.getTipoModificaMovimentoGestione()!=null){
						isModificata = true;
					} 
						
					if(isModificata){
						modificate.add(updApp);
					} else {
						invariate.add(updApp);
					}
				}
			}
			
			//Ciclo sui nuovi per raccogliere le prime info sugli atti amministrativi (verrano validati dopo chiamando il provvedimentoService):
			for(ModificaMovimentoGestioneSpesa newApp : nuove){
				SiacTAttoAmmFin nuovoAtto = getSiacTAttoAmmFromAttoAmministrativo(newApp.getAttoAmministrativo(), idEnte);
				//L'atto old non esiste perche' siamo in inserimento:
				SiacTAttoAmmFin oldAtto = null;
				AttoAmministrativo attoAmmInInput = buildAttoAmministrativo(idEnte, newApp.getAttoAmministrativoNumero(), newApp.getAttoAmministrativoTipoCode(), Integer.valueOf(newApp.getAttoAmministrativoAnno()), newApp.getIdStrutturaAmministrativa());
				AttiAmmModificatiGestioneSpesaInfoDto attoNuovo = new AttiAmmModificatiGestioneSpesaInfoDto(attoAmmInInput, nuovoAtto, oldAtto, newApp);
				attiInseriti.add(attoNuovo);
			}
			
			
		}
		
		//Setto le liste
		dto.setModificheDaAggiornare(modificate);
		dto.setModificheDaCreare(nuove);
		dto.setModificheResidue(invariate);
		dto.setAttiInseriti(attiInseriti);
		dto.setAttiModificati(attiModificati);
		dto.setAttiInseritiEModificati(toList(attiInseriti,attiModificati));
		
		//Termino restituendo l'oggetto di ritorno: 
        return dto;
	}
	
	/**
	 * Si occupa di valutare le modifiche riguardanti le modifiche movimento entrata, individuando
	 * quelle da aggiornare, eliminare, inserire o invariate
	 * @param lista
	 * @return
	 */
	public ModificaMovimentoGestioneEntrataInfoDto valutaModificheMovimentoEntrata(List<ModificaMovimentoGestioneEntrata> lista,DatiOperazioneDto datiOperazione){
		
		int idEnte = datiOperazione.getSiacTEnteProprietario().getEnteProprietarioId();
		
		ModificaMovimentoGestioneEntrataInfoDto dto = new ModificaMovimentoGestioneEntrataInfoDto();
		
		List<ModificaMovimentoGestioneEntrata> nuove = new ArrayList<ModificaMovimentoGestioneEntrata>();
		List<ModificaMovimentoGestioneEntrata> modificate = new ArrayList<ModificaMovimentoGestioneEntrata>();
		List<ModificaMovimentoGestioneEntrata> potenzialmenteModificate = new ArrayList<ModificaMovimentoGestioneEntrata>();
		List<ModificaMovimentoGestioneEntrata> invariate = new ArrayList<ModificaMovimentoGestioneEntrata>();
		
		List<AttiAmmModificatiGestioneEntrataInfoDto> attiModificati = new ArrayList<AttiAmmModificatiGestioneEntrataInfoDto>();
		List<AttiAmmModificatiGestioneEntrataInfoDto> attiInseriti = new ArrayList<AttiAmmModificatiGestioneEntrataInfoDto>();
		
		if(lista != null && lista.size() > 0){
			//Prendo le nuove e le salvo
			for(ModificaMovimentoGestioneEntrata spesa : lista){
				if(spesa.getUid() == 0){
					//non ha un id quindi e' sicuramente nuova
					nuove.add(spesa);
				} else {
					//ha un id diverso da zero quindi potrebbe essere stata modificata
					potenzialmenteModificate.add(spesa);
				}
			}
			
			//Ciclo su quelle per le quali l'id e' diverso da zero ovvero quelle potenzialmente modificate e vado
			//quindi a capire se sono state modificate o se restano invariate:
			for(ModificaMovimentoGestioneEntrata updApp : potenzialmenteModificate){
				
				//leggo quanto vale sul db per confrontare i valori ricevuti:
				SiacTModificaFin modificaDb = siacTModificaRepository.findOne(updApp.getUid());
				
				if(modificaDb != null){
					
					//inizializzo modificata a false:
					boolean isModificata = false;
					
					//basta che un campo differisca e la considero da aggiornare:
					/*if((updApp.getAttoAmministrativo().getAnno() != Integer.valueOf(modificaDb.getSiacTAttoAmm().getAttoammAnno()) && updApp.getAttoAmministrativo().getAnno()!=0)
							||	(updApp.getAttoAmministrativoNumero() != modificaDb.getSiacTAttoAmm().getAttoammNumero())
							||  (!updApp.getAttoAmministrativo().getTipoAtto().getCodice().equalsIgnoreCase(modificaDb.getSiacTAttoAmm().getSiacDAttoAmmTipo().getAttoammTipoCode()))){
						isModificata = true ;
					}
					if(updApp.getAttoAmministrativo().getNumero() != modificaDb.getSiacTAttoAmm().getAttoammNumero()){
						isModificata = true;
					}*/
					AttoAmministrativo attoAmmInInput = buildAttoAmministrativo(idEnte, updApp.getAttoAmministrativoNumero(), updApp.getAttoAmministrativoTipoCode(), Integer.valueOf(updApp.getAttoAmministrativoAnno()), updApp.getIdStrutturaAmministrativa());
					SiacTAttoAmmFin nuovoAtto = getSiacTAttoAmmFromAttoAmministrativo(attoAmmInInput, idEnte);
					SiacTAttoAmmFin oldAtto = modificaDb.getSiacTAttoAmm();
					if(!DatiOperazioneUtil.sonoUgualiAncheNull(oldAtto, nuovoAtto)){
						//L'atto amministrativo ha subito un cambiamento:
						isModificata = true;
						//raccolgo le info su questi atti amministrativi (verrano validati dopo chiamando il provvedimentoService):
						AttiAmmModificatiGestioneEntrataInfoDto attoMod = new AttiAmmModificatiGestioneEntrataInfoDto(updApp.getAttoAmministrativo(), nuovoAtto, oldAtto, updApp);
						attiModificati.add(attoMod);
					}
					
					if(!updApp.getDescrizioneModificaMovimentoGestione().trim().equalsIgnoreCase(modificaDb.getModDesc().trim())){
						isModificata = true;
					}
															
					if(updApp.getTipoModificaMovimentoGestione()!=null && modificaDb.getSiacDModificaTipo()!=null){
						if(!updApp.getTipoModificaMovimentoGestione().equalsIgnoreCase(modificaDb.getSiacDModificaTipo().getModTipoCode())){
							isModificata = true;
						}
					} else if(updApp.getTipoModificaMovimentoGestione()!=null){
						isModificata = true;
					}	
					
					if(isModificata){
						modificate.add(updApp);
					} else {
						invariate.add(updApp);
					}
				}
			}
			
			//Ciclo sui nuovi per raccogliere le prime info sugli atti amministrativi (verrano validati dopo chiamando il provvedimentoService):
			for(ModificaMovimentoGestioneEntrata newApp : nuove){
				SiacTAttoAmmFin nuovoAtto = getSiacTAttoAmmFromAttoAmministrativo(newApp.getAttoAmministrativo(), idEnte);
				//L'atto old non esiste perche' siamo in inserimento:
				SiacTAttoAmmFin oldAtto = null;
				AttoAmministrativo attoAmmInInput = buildAttoAmministrativo(idEnte, newApp.getAttoAmministrativoNumero(), newApp.getAttoAmministrativoTipoCode(), Integer.valueOf(newApp.getAttoAmministrativoAnno()), newApp.getIdStrutturaAmministrativa());
				AttiAmmModificatiGestioneEntrataInfoDto attoNuovo = new AttiAmmModificatiGestioneEntrataInfoDto(attoAmmInInput, nuovoAtto, oldAtto, newApp);
				attiInseriti.add(attoNuovo);
			}
		}
		
		//Setto le liste
		dto.setModificheDaAggiornare(modificate);
		dto.setModificheDaCreare(nuove);
		dto.setModificheResidue(invariate);
		dto.setAttiInseriti(attiInseriti);
		dto.setAttiModificati(attiModificati);
		dto.setAttiInseritiEModificati(toList(attiInseriti,attiModificati));
		
		//Termino restituendo l'oggetto di ritorno: 
        return dto;
	}
	
	/**
	 * Metodo ad uso interno di getDatiPerAnnullamentoModificaMovGest, usato per caricare i dati necessari all'operazione di annullamento modifica
	 * @param listaImportiMod
	 * @param datiOperazioneAnnullamento
	 * @return
	 */
	private ImpegnoInAnnullamentoInfoDto getDatiPerAnnullaModificaImporti(List<SiacTMovgestTsDetModFin> listaImportiMod, DatiOperazioneDto datiOperazioneAnnullamento){
		ImpegnoInAnnullamentoInfoDto impegnoInAnnullamentoInfoDto = new ImpegnoInAnnullamentoInfoDto();
		Integer idEnte = datiOperazioneAnnullamento.getSiacTEnteProprietario().getEnteProprietarioId();
		Timestamp now = datiOperazioneAnnullamento.getTs();
		List<ImportoImpegnoInAnnullamentoInfoDto> importiList = new ArrayList<ImportoImpegnoInAnnullamentoInfoDto>();
		if(listaImportiMod!=null && listaImportiMod.size()>0){
			SiacTMovgestTsDetModFin first = CommonUtil.getFirst(listaImportiMod);
			Integer idMovgestTs = first.getSiacTMovgestTsDet().getSiacTMovgestT().getMovgestTsId();
			List<SiacTMovgestTsDetFin> importoAttualeList = siacTMovgestTsDetRepository.findValidoByTipo(idEnte, now, CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE, idMovgestTs);
			//importoAttualeL dovrebbe avere un solo elemento, ovvero la riga in siac_t_movgest_ts_det corrispondente all'importo attuale:
			SiacTMovgestTsDetFin siacTMovgestTsDet = importoAttualeList.get(0);
			BigDecimal sommatoria = BigDecimal.ZERO;
			impegnoInAnnullamentoInfoDto.setSiacTMovgestTsDetModDaAnnullare(listaImportiMod);
			for(SiacTMovgestTsDetModFin it : listaImportiMod){
				BigDecimal importoIt = NumericUtils.getZeroIfNull(it.getMovgestTsDetImporto());
				sommatoria = sommatoria.add(importoIt);
			}
			ImportoImpegnoInAnnullamentoInfoDto importoImpegnoInAnnullamentoInfoDto = new ImportoImpegnoInAnnullamentoInfoDto();
			importoImpegnoInAnnullamentoInfoDto.setSiacTMovgestTsDet(siacTMovgestTsDet);
			importoImpegnoInAnnullamentoInfoDto.setTotaleModifiche(sommatoria);
			importiList.add(importoImpegnoInAnnullamentoInfoDto);
		}
		impegnoInAnnullamentoInfoDto.setElencoModifiche(importiList);
		//Termino restituendo l'oggetto di ritorno: 
        return impegnoInAnnullamentoInfoDto;
	}
	
	/**
	 * Per annullare una modifica ben precisa
	 * @param datiOperazioneAnnullamento
	 * @param idMovgestTs
	 * @return
	 */
	private ImpegnoInAnnullamentoInfoDto getDatiPerAnnullamentoModificaMovGest(DatiOperazioneDto datiOperazioneAnnullamento,ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa){
		ImpegnoInAnnullamentoInfoDto impegnoInAnnullamentoInfoDto = new ImpegnoInAnnullamentoInfoDto();
		Integer idEnte = datiOperazioneAnnullamento.getSiacTEnteProprietario().getEnteProprietarioId();
		Timestamp now = datiOperazioneAnnullamento.getTs();
		Integer idModifica = modificaMovimentoGestioneSpesa.getUid();
		//1. CAPIRE DI CHE TIPO DI MODIFICA SI TRATTA: importo o soggetto
		
		List<SiacTMovgestTsDetModFin> listaSiacTMovgestTsDetMod = siacTMovgestTsDetModRepository.findListaFromModifica(idEnte,idModifica,now);
		
		if(listaSiacTMovgestTsDetMod!=null && listaSiacTMovgestTsDetMod.size()>0){
			impegnoInAnnullamentoInfoDto =  getDatiPerAnnullaModificaImporti(listaSiacTMovgestTsDetMod, datiOperazioneAnnullamento);
		} else{
			List<SiacRMovgestTsSogModFin> listaRMovgestTsSogMod = siacRMovgestTsSogModRepository.findListaFromModifica(idEnte,idModifica,now);
			if(listaRMovgestTsSogMod!=null && listaRMovgestTsSogMod.size()>0){
				impegnoInAnnullamentoInfoDto.setSiacRMovgestTsSogModListDaAnnullare(listaRMovgestTsSogMod);
			} 
		}
		//Termino restituendo l'oggetto di ritorno: 
        return impegnoInAnnullamentoInfoDto;
	}
	
	/**
	 * Per annullare una modifica ben precisa
	 * @param datiOperazioneAnnullamento
	 * @param idMovgestTs
	 * @return
	 */
	private ImpegnoInAnnullamentoInfoDto getDatiPerAnnullamentoModificaMovGest(DatiOperazioneDto datiOperazioneAnnullamento,ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata){
		ImpegnoInAnnullamentoInfoDto impegnoInAnnullamentoInfoDto = new ImpegnoInAnnullamentoInfoDto();
		Integer idEnte = datiOperazioneAnnullamento.getSiacTEnteProprietario().getEnteProprietarioId();
		Timestamp now = datiOperazioneAnnullamento.getTs();
		Integer idModifica = modificaMovimentoGestioneEntrata.getUid();
		//1. CAPIRE DI CHE TIPO DI MODIFICA SI TRATTA: importo o soggetto
		
		List<SiacTMovgestTsDetModFin> listaSiacTMovgestTsDetMod = siacTMovgestTsDetModRepository.findListaFromModifica(idEnte,idModifica,now);
		
		if(listaSiacTMovgestTsDetMod!=null && listaSiacTMovgestTsDetMod.size()>0){
			impegnoInAnnullamentoInfoDto =  getDatiPerAnnullaModificaImporti(listaSiacTMovgestTsDetMod, datiOperazioneAnnullamento);
		} else{
			List<SiacRMovgestTsSogModFin> listaRMovgestTsSogMod = siacRMovgestTsSogModRepository.findListaFromModifica(idEnte,idModifica,now);
			if(listaRMovgestTsSogMod!=null && listaRMovgestTsSogMod.size()>0){
				impegnoInAnnullamentoInfoDto.setSiacRMovgestTsSogModListDaAnnullare(listaRMovgestTsSogMod);
			} 
		}
		//Termino restituendo l'oggetto di ritorno: 
        return impegnoInAnnullamentoInfoDto;
	}
	

	/**
	 * da usare se si intende annullare tutte le modifiche su un impegno
	 * @param datiOperazioneAnnullamento
	 * @param idMovgestTs
	 * @return
	 */
	private ImpegnoInAnnullamentoInfoDto getDatiPerAnnullamentoImpegno(DatiOperazioneDto datiOperazioneAnnullamento,Integer idMovgestTs){
		ImpegnoInAnnullamentoInfoDto impegnoInAnnullamentoInfoDto = new ImpegnoInAnnullamentoInfoDto();
		Integer idEnte = datiOperazioneAnnullamento.getSiacTEnteProprietario().getEnteProprietarioId();
		Timestamp now = datiOperazioneAnnullamento.getTs();
		
		List<SiacTMovgestTsDetFin> importoAttualeList = siacTMovgestTsDetRepository.findValidoByTipo(idEnte, now, CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE, idMovgestTs);
		
		List<SiacRMovgestTsSogclasseModFin> siacRMovgestTsSogclasseModList = siacRMovgestTsSogClasseModRepository.findValidiByMovgestTs(idEnte, now, idMovgestTs);
		List<SiacRMovgestTsSogModFin> siacRMovgestTsSogModList = siacRMovgestTsSogModRepository.findValidiByMovgestTs(idEnte, now, idMovgestTs);
		impegnoInAnnullamentoInfoDto.setSiacRMovgestTsSogclasseModListDaAnnullare(siacRMovgestTsSogclasseModList);
		impegnoInAnnullamentoInfoDto.setSiacRMovgestTsSogModListDaAnnullare(siacRMovgestTsSogModList);
		
		List<ImportoImpegnoInAnnullamentoInfoDto> importiList = new ArrayList<ImportoImpegnoInAnnullamentoInfoDto>();
		
		if(importoAttualeList!=null && importoAttualeList.size()>0){
			//importoAttualeL dovrebbe avere un solo elemento, ovvero la riga in siac_t_movgest_ts_det corrispondente all'importo attuale:
			SiacTMovgestTsDetFin siacTMovgestTsDet = importoAttualeList.get(0);
			
			Integer idMovgestTsDet = siacTMovgestTsDet.getMovgestTsDetId();
			List<SiacTMovgestTsDetModFin> listaImportiMod = siacTMovgestTsDetModRepository.findValidiByMovgestTsDet(idEnte, now, idMovgestTsDet);
			BigDecimal sommatoria = BigDecimal.ZERO;
			if(listaImportiMod!=null && listaImportiMod.size()>0){
				impegnoInAnnullamentoInfoDto.setSiacTMovgestTsDetModDaAnnullare(listaImportiMod);
				for(SiacTMovgestTsDetModFin it : listaImportiMod){
					BigDecimal importoIt = NumericUtils.getZeroIfNull(it.getMovgestTsDetImporto());
					sommatoria = sommatoria.add(importoIt);
				}
			}
			ImportoImpegnoInAnnullamentoInfoDto importoImpegnoInAnnullamentoInfoDto = new ImportoImpegnoInAnnullamentoInfoDto();
			importoImpegnoInAnnullamentoInfoDto.setSiacTMovgestTsDet(siacTMovgestTsDet);
			importoImpegnoInAnnullamentoInfoDto.setTotaleModifiche(sommatoria);
			importiList.add(importoImpegnoInAnnullamentoInfoDto);
			
		}
		impegnoInAnnullamentoInfoDto.setElencoModifiche(importiList);
		//Termino restituendo l'oggetto di ritorno: 
        return impegnoInAnnullamentoInfoDto;
	}
	
	/**
	 * Recupera il soggetto a partire da movimento indicato
	 * @param idEnte
	 * @param siacTMovgestTs
	 * @return
	 */
	public Soggetto estraiSoggettoMovimento(String codiceAmbito,int idEnte, SiacTMovgestTsFin siacTMovgestTs,DatiOperazioneDto datiOperazioneDto){
		Soggetto soggettoMovimento = new Soggetto();
		List<SiacRMovgestTsSogFin> listaSiacRMovgestTsSog = siacTMovgestTs.getSiacRMovgestTsSogs();
		if(null!=listaSiacRMovgestTsSog && listaSiacRMovgestTsSog.size() > 0){
			listaSiacRMovgestTsSog = DatiOperazioneUtil.soloValidi(listaSiacRMovgestTsSog, getNow());
			if(null!=listaSiacRMovgestTsSog && listaSiacRMovgestTsSog.size() > 0){
				for(SiacRMovgestTsSogFin siacRMovgestTsSog : listaSiacRMovgestTsSog){
					if(null!=siacRMovgestTsSog &&  siacRMovgestTsSog.getDataFineValidita() == null){
						//Se valido:
						soggettoMovimento = soggettoDad.ricercaSoggetto(codiceAmbito, idEnte, siacRMovgestTsSog.getSiacTSoggetto().getSoggettoCode(), false, true);
						
						
						//ricerca sedi secondarie
						List<SedeSecondariaSoggetto> listaSediSecondarie = soggettoDad.ricercaSediSecondarie(idEnte, soggettoMovimento.getUid(), false, false,datiOperazioneDto);
						soggettoMovimento.setSediSecondarie((listaSediSecondarie!=null && !listaSediSecondarie.isEmpty()) ? listaSediSecondarie: new ArrayList<SedeSecondariaSoggetto>());
						
						//ricerca modalita di pagamento
						List<ModalitaPagamentoSoggetto> listaModPag = soggettoDad.ricercaModalitaPagamento(codiceAmbito, idEnte, siacRMovgestTsSog.getSiacTSoggetto().getUid(), "Soggetto", false);
						if(listaModPag!=null && listaModPag.size()>0){
							soggettoMovimento.setElencoModalitaPagamento(listaModPag);
						}
					}
				}
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return soggettoMovimento;
	}
	
	
	public Soggetto estraiSediSecondarieEModalitaPagamento(Richiedente richiedente,int idEnte, String codiceSoggetto, String codiceAmbito,DatiOperazioneDto datiOperazione){
		
		// 2. Richiamo il metodo che va a caricare il soggetto richiesto:
		Soggetto soggetto = new Soggetto();
		ParametroRicercaSoggettoK parametroRicercaSoggettoK = new ParametroRicercaSoggettoK();
		parametroRicercaSoggettoK.setCodice(codiceSoggetto);
		parametroRicercaSoggettoK.setIncludeModif(false);
		soggetto = soggettoDad.ricercaSoggetto(codiceAmbito, idEnte, parametroRicercaSoggettoK, true);
		
		if (soggetto != null)
		{
			// Richiamo il metodo completaInformazioni che "veste" il soggetto
			// con dati ulteriori:
			soggetto = soggettoDad.completaInformazioni(soggetto, codiceAmbito, idEnte, soggetto.getCodiceSoggetto(),
					parametroRicercaSoggettoK.isIncludeModif(), richiedente,datiOperazione);
			// Ricerco l'eventuale versione in modifica (vedi tabelle _mod):
			//soggMod = soggettoDad.ricercaSoggettoInModifica(idEnte, sogg.getCodiceSoggetto());
		}


		//ricerca sedi secondarie
//		List<SedeSecondariaSoggetto> listaSediSecondarie = soggettoDad.ricercaSediSecondarie(idEnte, soggetto.getUid(), false, false);
//		soggetto.setSediSecondarie((listaSediSecondarie!=null && !listaSediSecondarie.isEmpty()) ? listaSediSecondarie: new ArrayList<SedeSecondariaSoggetto>());
		
		//ricerca modalita di pagamento
//		List<ModalitaPagamentoSoggetto> listaModPag = soggettoDad.ricercaModalitaPagamento(codiceAmbito, idEnte, soggetto.getUid(), "Soggetto", false);
//		if(listaModPag!=null && listaModPag.size()>0){
//			soggetto.setElencoModalitaPagamento(listaModPag);
//		}
					
		//Termino restituendo l'oggetto di ritorno: 
        return soggetto;
	}

	/**
	 * metodo che recupera l'elenco delle modifiche movimento gestione spesa
	 * @param richiedente
	 * @param siacTMovgestTs
	 * @return
	 */
	public List<ModificaMovimentoGestioneSpesa> estraiElencoModificheMovimentoGestioneSpesa(Richiedente richiedente, SiacTMovgestTsFin siacTMovgestTs){

		int idEnte = richiedente.getAccount().getEnte().getUid();
		
		List<ModificaMovimentoGestioneSpesa> elencoModificheMovimentoGestioneSpesa = new ArrayList<ModificaMovimentoGestioneSpesa>();
		List<SiacTMovgestTsDetModFin> listaSiacTMovgestTsDetMod = siacTMovgestTs.getSiacTMovgestTsDetMods();
		List<SiacRMovgestTsSogModFin> listaSiacRMovgestTsSogMod = siacTMovgestTs.getSiacRMovgestTsSogMods();		
		List<SiacRMovgestTsSogclasseModFin> listaSiacRMovgestTsSogClasseMod = siacTMovgestTs.getSiacRMovgestTsSogclasseMods();
		
		//System.out.println("Ricerco le modifiche per uid: "+siacTMovgestTs.getUid());
		listaSiacRMovgestTsSogMod = (List<SiacRMovgestTsSogModFin>) DatiOperazioneUtil.soloValidi(listaSiacRMovgestTsSogMod, getNow());
		listaSiacRMovgestTsSogClasseMod = (List<SiacRMovgestTsSogclasseModFin>) DatiOperazioneUtil.soloValidi(listaSiacRMovgestTsSogClasseMod, getNow());
		
		//estrazione modifiche importi
		if(null!=listaSiacTMovgestTsDetMod && listaSiacTMovgestTsDetMod.size() > 0){
			for(SiacTMovgestTsDetModFin siacTMovgestTsDetMod : listaSiacTMovgestTsDetMod){
				SiacTModificaFin siacTModifica = siacTMovgestTsDetMod.getSiacRModificaStato().getSiacTModifica();
				if(null!=siacTModifica && siacTModifica.getDataFineValidita() == null){
					//Se valido:
					ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa = new ModificaMovimentoGestioneSpesa();
					
					modificaMovimentoGestioneSpesa = map(siacTModifica, ModificaMovimentoGestioneSpesa.class, FinMapId.SiacTModifica_ModificaMovimentoGestioneSpesa);	
					modificaMovimentoGestioneSpesa = EntityToModelConverter.siacTModificaEntityToModificaMovimentoGestioneSpesaModel(siacTModifica, modificaMovimentoGestioneSpesa);
					
					elencoModificheMovimentoGestioneSpesa.add(modificaMovimentoGestioneSpesa);
				}
			}
		}
		
		int numeroSogMod = StringUtilsFin.numeroElementi(listaSiacRMovgestTsSogMod);
		int numeroSogClasseMod = StringUtilsFin.numeroElementi(listaSiacRMovgestTsSogClasseMod);
		
		if(numeroSogMod==1 && numeroSogClasseMod==1){
			//situzione class -> sogg OPPURE sogg -> class
			SiacRMovgestTsSogModFin soggMod = listaSiacRMovgestTsSogMod.get(0);
			SiacRMovgestTsSogclasseModFin soggClassMod = listaSiacRMovgestTsSogClasseMod.get(0);
			if(soggMod.getSiacTSoggetto2() == null){
				//situazione da sogg --> class
				SiacTModificaFin siacTModifica = soggClassMod.getSiacRModificaStato().getSiacTModifica();
				ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa = initModificaMovimentoGestioneSpesa(siacTModifica,idEnte);
				ClasseSoggetto classeNewUno = buildSoggettoClasse(soggClassMod.getSiacDSoggettoClasse1());
				modificaMovimentoGestioneSpesa.setClasseSoggettoOldMovimentoGestione(classeNewUno);
				ClasseSoggetto classeNewDue = buildSoggettoClasse(soggClassMod.getSiacDSoggettoClasse2());
				modificaMovimentoGestioneSpesa.setClasseSoggettoNewMovimentoGestione(classeNewDue);
				elencoModificheMovimentoGestioneSpesa.add(modificaMovimentoGestioneSpesa);
			} else if(soggClassMod.getSiacDSoggettoClasse2()==null){
				//situazione da class --> sogg
				SiacTModificaFin siacTModifica = soggMod.getSiacRModificaStato().getSiacTModifica();
				ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa = initModificaMovimentoGestioneSpesa(siacTModifica,idEnte);
				ClasseSoggetto classeOld = buildSoggettoClasse(soggClassMod.getSiacDSoggettoClasse1());
				modificaMovimentoGestioneSpesa.setClasseSoggettoOldMovimentoGestione(classeOld);
				elencoModificheMovimentoGestioneSpesa.add(modificaMovimentoGestioneSpesa);
			}
		} else {
			//estrazione modifiche soggetti
			if(null!=listaSiacRMovgestTsSogMod && listaSiacRMovgestTsSogMod.size() > 0){
				for(SiacRMovgestTsSogModFin siacRMovgestTsSogMod : listaSiacRMovgestTsSogMod){
					SiacTModificaFin siacTModifica = siacRMovgestTsSogMod.getSiacRModificaStato().getSiacTModifica();
					if(null!=siacTModifica && siacTModifica.getDataFineValidita() == null){
						//Se valido:
						ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa = initModificaMovimentoGestioneSpesa(siacTModifica,idEnte);
						elencoModificheMovimentoGestioneSpesa.add(modificaMovimentoGestioneSpesa);
					}
				}
			}
		
		//estrazione modifiche classi
		if(null!=listaSiacRMovgestTsSogClasseMod && listaSiacRMovgestTsSogClasseMod.size() > 0){
			for(SiacRMovgestTsSogclasseModFin siacRMovgestTsSogClasseMod : listaSiacRMovgestTsSogClasseMod){
				SiacTModificaFin siacTModifica = siacRMovgestTsSogClasseMod.getSiacRModificaStato().getSiacTModifica();
				if(null!=siacTModifica && siacTModifica.getDataFineValidita() == null){
						//Se valido:
						ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa = initModificaMovimentoGestioneSpesa(siacTModifica,idEnte);
						ClasseSoggetto classeOld = buildSoggettoClasse(siacRMovgestTsSogClasseMod.getSiacDSoggettoClasse1());
							modificaMovimentoGestioneSpesa.setClasseSoggettoOldMovimentoGestione(classeOld);
						ClasseSoggetto classeNew = buildSoggettoClasse(siacRMovgestTsSogClasseMod.getSiacDSoggettoClasse2());
						modificaMovimentoGestioneSpesa.setClasseSoggettoNewMovimentoGestione(classeNew);
						elencoModificheMovimentoGestioneSpesa.add(modificaMovimentoGestioneSpesa);
					}
				}
			}
						
		}
		
		//FIX per jira SIAC-2251
		elencoModificheMovimentoGestioneSpesa = scremaDoppioni(elencoModificheMovimentoGestioneSpesa);
		//
							
		//Termino restituendo l'oggetto di ritorno: 
        return elencoModificheMovimentoGestioneSpesa;
	}
	
	/**
	 * utility per rimuovere i doppioni dalla lista con stesso id di siac_t_modifica
	 * 
	 * @param elencoModificheMovimentoGestioneSpesa
	 * @return
	 */
	private <MMG extends ModificaMovimentoGestione> List<MMG> scremaDoppioni(List<MMG> elencoModificheMovimentoGestioneSpesa){
		ArrayList<MMG> listaRicostruita = new ArrayList<MMG>();
		ArrayList<Integer> idTModificaAggiunti = new ArrayList<Integer>();
		if(elencoModificheMovimentoGestioneSpesa!=null && elencoModificheMovimentoGestioneSpesa.size()>0){
			for(MMG modIt : elencoModificheMovimentoGestioneSpesa){
				if(!idTModificaAggiunti.contains(modIt.getUid())){
					listaRicostruita.add(modIt);
					idTModificaAggiunti.add(modIt.getUid());
				}
			}
		}
		return listaRicostruita;
	}
	
	/**
	 * Metodo di comodo per costruire un oggetto ClasseSoggetto a partire da un oggetto SiacDSoggettoClasseFin
	 * @param dSoggClasse
	 * @return
	 */
	private ClasseSoggetto buildSoggettoClasse(SiacDSoggettoClasseFin dSoggClasse){
		ClasseSoggetto classeSoggetto= null;
		if(dSoggClasse!=null){
			classeSoggetto = new ClasseSoggetto();
			if(!StringUtilsFin.isEmpty(dSoggClasse.getSoggettoClasseCode())){							
				classeSoggetto.setCodice(dSoggClasse.getSoggettoClasseCode());
				classeSoggetto.setDescrizione(dSoggClasse.getSoggettoClasseDesc());
			}		
		}
		//Termino restituendo l'oggetto di ritorno: 
        return classeSoggetto;
	}
	
	/**
	 * sotto-metodo di "estraiElencoModificheMovimentoGestioneEntrata" serve per inizializzare
	 * un oggetto ModificaMovimentoGestioneEntrata a partire da SiacTModificaFin
	 * @param siacTModifica
	 * @param idEnte
	 * @return
	 */
	private ModificaMovimentoGestioneEntrata initModificaMovimentoGestioneEntrata(SiacTModificaFin siacTModifica, int idEnte){
		ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata = new ModificaMovimentoGestioneEntrata();
		modificaMovimentoGestioneEntrata.setTipoMovimento(CostantiFin.MODIFICA_TIPO_ACC);
		modificaMovimentoGestioneEntrata = map(siacTModifica, ModificaMovimentoGestioneEntrata.class, FinMapId.SiacTModifica_ModificaMovimentoGestioneEntrata);	
		modificaMovimentoGestioneEntrata = EntityToModelConverter.siacTModificaEntityToModificaMovimentoGestioneEntrataModel(siacTModifica, modificaMovimentoGestioneEntrata);
		if(null!=modificaMovimentoGestioneEntrata.getNewSoggettoCodeMovimentoGestione() && !StringUtilsFin.isEmpty(modificaMovimentoGestioneEntrata.getNewSoggettoCodeMovimentoGestione())){
			Soggetto soggettoNew = soggettoDad.ricercaSoggetto(CostantiFin.AMBITO_FIN, idEnte, modificaMovimentoGestioneEntrata.getNewSoggettoCodeMovimentoGestione(),true, true);
			if(null!=soggettoNew){
				modificaMovimentoGestioneEntrata.setSoggettoNewMovimentoGestione(soggettoNew);
				}
			}
		if(null!=modificaMovimentoGestioneEntrata.getOldSoggettoCodeMovimentoGestione() && !StringUtilsFin.isEmpty(modificaMovimentoGestioneEntrata.getOldSoggettoCodeMovimentoGestione())){
			Soggetto soggettoOld = soggettoDad.ricercaSoggetto(CostantiFin.AMBITO_FIN, idEnte, modificaMovimentoGestioneEntrata.getOldSoggettoCodeMovimentoGestione(),true, true);
			if(null!=soggettoOld){
				modificaMovimentoGestioneEntrata.setSoggettoOldMovimentoGestione(soggettoOld);
		}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return modificaMovimentoGestioneEntrata;
	}
	
	/**
	 * sotto-metodo di "estraiElencoModificheMovimentoGestioneSpesa" serve per inizializzare
	 * un oggetto ModificaMovimentoGestioneSpesa a partire da SiacTModificaFin
	 * @param siacTModifica
	 * @param idEnte
	 * @return
	 */
	private ModificaMovimentoGestioneSpesa initModificaMovimentoGestioneSpesa(SiacTModificaFin siacTModifica, int idEnte){
		
		ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa = new ModificaMovimentoGestioneSpesa();
		modificaMovimentoGestioneSpesa.setTipoMovimento(CostantiFin.MODIFICA_TIPO_IMP);
		modificaMovimentoGestioneSpesa = map(siacTModifica, ModificaMovimentoGestioneSpesa.class, FinMapId.SiacTModifica_ModificaMovimentoGestioneSpesa);	
		modificaMovimentoGestioneSpesa = EntityToModelConverter.siacTModificaEntityToModificaMovimentoGestioneSpesaModel(siacTModifica, modificaMovimentoGestioneSpesa);
		if(modificaMovimentoGestioneSpesa.getNewSoggettoCodeMovimentoGestione()!=null && !StringUtilsFin.isEmpty(modificaMovimentoGestioneSpesa.getNewSoggettoCodeMovimentoGestione())){
			Soggetto soggettoNew = soggettoDad.ricercaSoggetto(CostantiFin.AMBITO_FIN, idEnte, modificaMovimentoGestioneSpesa.getNewSoggettoCodeMovimentoGestione(),true, true);
			if(null!=soggettoNew){
				modificaMovimentoGestioneSpesa.setSoggettoNewMovimentoGestione(soggettoNew);
			}
		}

		if(modificaMovimentoGestioneSpesa.getOldSoggettoCodeMovimentoGestione()!=null && !StringUtilsFin.isEmpty(modificaMovimentoGestioneSpesa.getOldSoggettoCodeMovimentoGestione())){
			//JIra 1885 alla ricerca del soggetto non veniva passatp l'ambito_fin ma null 
			Soggetto soggettoOld = soggettoDad.ricercaSoggetto(CostantiFin.AMBITO_FIN, idEnte, modificaMovimentoGestioneSpesa.getOldSoggettoCodeMovimentoGestione(),true, true);
			if(null!=soggettoOld){
				modificaMovimentoGestioneSpesa.setSoggettoOldMovimentoGestione(soggettoOld);
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return modificaMovimentoGestioneSpesa;
	}

	/**
	 * Metodo che si occupa di etrarre le modifiche movimento gestione entrata per il movimento indicato
	 * @param richiedente
	 * @param siacTMovgestTs
	 * @return
	 */
	public List<ModificaMovimentoGestioneEntrata> estraiElencoModificheMovimentoGestioneEntrata(Richiedente richiedente, SiacTMovgestTsFin siacTMovgestTs){
	
		int idEnte = richiedente.getAccount().getEnte().getUid();
	
		List<ModificaMovimentoGestioneEntrata> elencoModificheMovimentoGestioneEntrata = new ArrayList<ModificaMovimentoGestioneEntrata>();
		List<SiacTMovgestTsDetModFin> listaSiacTMovgestTsDetMod = siacTMovgestTs.getSiacTMovgestTsDetMods();
		List<SiacRMovgestTsSogModFin> listaSiacRMovgestTsSogMod = siacTMovgestTs.getSiacRMovgestTsSogMods();
		List<SiacRMovgestTsSogclasseModFin> listaSiacRMovgestTsSogClasseMod = siacTMovgestTs.getSiacRMovgestTsSogclasseMods();
		
		listaSiacRMovgestTsSogMod = (List<SiacRMovgestTsSogModFin>) DatiOperazioneUtil.soloValidi(listaSiacRMovgestTsSogMod, getNow());
		listaSiacRMovgestTsSogClasseMod = (List<SiacRMovgestTsSogclasseModFin>) DatiOperazioneUtil.soloValidi(listaSiacRMovgestTsSogClasseMod, getNow());
		
		//estrazione modifiche importi
		if(null!=listaSiacTMovgestTsDetMod && listaSiacTMovgestTsDetMod.size() > 0){
			for(SiacTMovgestTsDetModFin siacTMovgestTsDetMod : listaSiacTMovgestTsDetMod){
				SiacTModificaFin siacTModifica = siacTMovgestTsDetMod.getSiacRModificaStato().getSiacTModifica();
				if(null!=siacTModifica && siacTModifica.getDataFineValidita() == null){
					//Se valido:
					ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata = new ModificaMovimentoGestioneEntrata();
					
					modificaMovimentoGestioneEntrata = map(siacTModifica, ModificaMovimentoGestioneEntrata.class, FinMapId.SiacTModifica_ModificaMovimentoGestioneEntrata);	
					modificaMovimentoGestioneEntrata = EntityToModelConverter.siacTModificaEntityToModificaMovimentoGestioneEntrataModel(siacTModifica, modificaMovimentoGestioneEntrata);
	
					elencoModificheMovimentoGestioneEntrata.add(modificaMovimentoGestioneEntrata);
				}
			}
		}
		
		int numeroSogMod = StringUtilsFin.numeroElementi(listaSiacRMovgestTsSogMod);
		int numeroSogClasseMod = StringUtilsFin.numeroElementi(listaSiacRMovgestTsSogClasseMod);
		
		if(numeroSogMod==1 && numeroSogClasseMod==1){
			//situzione class -> sogg OPPURE sogg -> class
			SiacRMovgestTsSogModFin soggMod = listaSiacRMovgestTsSogMod.get(0);
			SiacRMovgestTsSogclasseModFin soggClassMod = listaSiacRMovgestTsSogClasseMod.get(0);
			if(soggMod.getSiacTSoggetto2() == null){
				//situazione da sogg --> class
				SiacTModificaFin siacTModifica = soggClassMod.getSiacRModificaStato().getSiacTModifica();
				ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata = initModificaMovimentoGestioneEntrata(siacTModifica,idEnte);
				ClasseSoggetto classeNewUno = buildSoggettoClasse(soggClassMod.getSiacDSoggettoClasse1());
				modificaMovimentoGestioneEntrata.setClasseSoggettoOldMovimentoGestione(classeNewUno);
				ClasseSoggetto classeNewDue = buildSoggettoClasse(soggClassMod.getSiacDSoggettoClasse2());
				modificaMovimentoGestioneEntrata.setClasseSoggettoNewMovimentoGestione(classeNewDue);
				elencoModificheMovimentoGestioneEntrata.add(modificaMovimentoGestioneEntrata);
			} else if(soggClassMod.getSiacDSoggettoClasse2()==null){
				//situazione da class --> sogg
				SiacTModificaFin siacTModifica = soggMod.getSiacRModificaStato().getSiacTModifica();
				ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata = initModificaMovimentoGestioneEntrata(siacTModifica,idEnte);
				ClasseSoggetto classeOld = buildSoggettoClasse(soggClassMod.getSiacDSoggettoClasse1());
				modificaMovimentoGestioneEntrata.setClasseSoggettoOldMovimentoGestione(classeOld);
				elencoModificheMovimentoGestioneEntrata.add(modificaMovimentoGestioneEntrata);
			}
		} else {
		//estrazione modifiche soggetti
		if(null!=listaSiacRMovgestTsSogMod && listaSiacRMovgestTsSogMod.size() > 0){
			for(SiacRMovgestTsSogModFin siacRMovgestTsSogMod : listaSiacRMovgestTsSogMod){
				SiacTModificaFin siacTModifica = siacRMovgestTsSogMod.getSiacRModificaStato().getSiacTModifica();
				if(null!=siacTModifica && siacTModifica.getDataFineValidita() == null){
					//Se valido:
					ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata = initModificaMovimentoGestioneEntrata(siacTModifica, idEnte);
					elencoModificheMovimentoGestioneEntrata.add(modificaMovimentoGestioneEntrata);
				}
			}
		}
			
		//estrazione modifiche classi
		if(null!=listaSiacRMovgestTsSogClasseMod && listaSiacRMovgestTsSogClasseMod.size() > 0){
			for(SiacRMovgestTsSogclasseModFin siacRMovgestTsSogClasseMod : listaSiacRMovgestTsSogClasseMod){
				SiacTModificaFin siacTModifica = siacRMovgestTsSogClasseMod.getSiacRModificaStato().getSiacTModifica();
				if(null!=siacTModifica && siacTModifica.getDataFineValidita() == null){
					//Se valido:
						ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata = initModificaMovimentoGestioneEntrata(siacTModifica, idEnte);
						ClasseSoggetto classeOld = buildSoggettoClasse(siacRMovgestTsSogClasseMod.getSiacDSoggettoClasse1());
							modificaMovimentoGestioneEntrata.setClasseSoggettoOldMovimentoGestione(classeOld);
						ClasseSoggetto classeNew = buildSoggettoClasse(siacRMovgestTsSogClasseMod.getSiacDSoggettoClasse2());
							modificaMovimentoGestioneEntrata.setClasseSoggettoNewMovimentoGestione(classeNew);
					elencoModificheMovimentoGestioneEntrata.add(modificaMovimentoGestioneEntrata);
				}
			}
		}
		}
		
		//FIX per jira SIAC-2251
		elencoModificheMovimentoGestioneEntrata = scremaDoppioni(elencoModificheMovimentoGestioneEntrata);
		//
				
		//Termino restituendo l'oggetto di ritorno: 
        return elencoModificheMovimentoGestioneEntrata;
	}
	
	/**
	 * restituisce l'elenco dei SiacTMovgestTsFin comprensivo del record che rappresenta la testata seguito dagli (eventuali) record che
	 * rappresentano i subimpegni
	 * @param siacTMovgest
	 * @return
	 */
	protected List<SiacTMovgestTsFin> getTestataPiuListaSub(SiacTMovgestFin siacTMovgest){
		  Timestamp now = getNow();
		  Integer idEnte = siacTMovgest.getSiacTEnteProprietario().getEnteProprietarioId();
		  List<SiacTMovgestTsFin> listaSiacTMovgestTsSUB = null;
		  List<SiacTMovgestTsFin> listaSiacTMovgestTs = null;
		  List<SiacTMovgestTsFin> l = siacTMovgestTsRepository.findMovgestTsByMovgest(idEnte, now, siacTMovgest.getMovgestId());
		  if(l!=null && l.size()>0){
			   SiacTMovgestTsFin testata = l.get(0);
			   listaSiacTMovgestTsSUB = siacTMovgestTsRepository.findListaSiacTMovgestTsFigli(idEnte, now, testata.getMovgestTsId());
			   listaSiacTMovgestTs = new ArrayList<SiacTMovgestTsFin>();
			   listaSiacTMovgestTs.add(testata);
			   if(listaSiacTMovgestTsSUB!=null && listaSiacTMovgestTsSUB.size()>0){
				   listaSiacTMovgestTs.addAll(listaSiacTMovgestTsSUB);
			   }
		  }
		  //Termino restituendo l'oggetto di ritorno: 
		  return listaSiacTMovgestTs;
	 }
	
	/**
	 * e' una specie di ricercaMovimentoPk ridotto all'osso per usi interni, ritorna null se non esiste nessun movimento che rispecchia i dati indicati
	 * 
	 * !!!!!!   nasce come metodo di appoggio al servizio di inserimento ordinativo di incasso !!!
	 * 
	 * @return
	 */
	public MovimentoGestioneLigthDto caricaMovimentoLigth(Integer idEnte,String annoEsercizio,Integer annoMovimento,BigDecimal numeroMovimento,String tipoMovimento, DatiOperazioneDto datiOperazioneDto){
		MovimentoGestioneLigthDto info = null;
		SiacTMovgestFin siacTMovgest = siacTMovgestRepository.ricercaSiacTMovgestPk(idEnte, annoEsercizio, annoMovimento, numeroMovimento, tipoMovimento);	
		Timestamp now = datiOperazioneDto.getTs();
		if(null!=siacTMovgest){
			
			SiacTMovgestTsFin testata = siacTMovgestTsRepository.findMovgestTsByMovgest(idEnte, now, siacTMovgest.getMovgestId()).get(0);
			if(testata!=null){
				
				info = caricaMovimentoLigthRoutine(testata, idEnte, annoEsercizio, annoMovimento, numeroMovimento, tipoMovimento, datiOperazioneDto, false);
				//id
				info.setMovgestId(siacTMovgest.getMovgestId());
				//subs:
				List<SiacTMovgestTsFin> listaSiacTMovgestTsSUB = siacTMovgestTsRepository.findListaSiacTMovgestTsFigli(idEnte, now, testata.getMovgestTsId());
				listaSiacTMovgestTsSUB = DatiOperazioneUtil.soloValidi(listaSiacTMovgestTsSUB, now);
				
				if(listaSiacTMovgestTsSUB!=null && listaSiacTMovgestTsSUB.size() > 0){
					//ci sono oltre alla testa dei sub
					List<MovimentoGestioneSubLigthDto> listaSub = new ArrayList<MovimentoGestioneSubLigthDto>();
					for(SiacTMovgestTsFin subIt: listaSiacTMovgestTsSUB){
						MovimentoGestioneSubLigthDto subItInfo = caricaMovimentoLigthRoutine(subIt, idEnte, annoEsercizio, annoMovimento,
								numeroMovimento, tipoMovimento, datiOperazioneDto, true);
						//in caso di sub il soggetto e' considerato lo stesso anche a livello di testata
						info.setSoggettoMovimento(subItInfo.getSoggettoMovimento());
						////////////////////////////////////////////////////////////////////////////////
						//in caso di sub il soggetto e' considerato lo stesso anche a livello di testata
						info.setSoggettoClasseCode(subItInfo.getSoggettoClasseCode());
						////////////////////////////////////////////////////////////////////////////////
						subItInfo.setMovgestTsIdPadre(testata.getMovgestTsId());
						subItInfo.setMovgestIdPadre(siacTMovgest.getMovgestId());
						listaSub.add(subItInfo);
					}
					info.setListaSub(listaSub);
				}
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return info;
	}
	
	/**
	 * Sotto metodo di "caricaMovimentoLigth"
	 * @param siacTMovgestTs
	 * @param idEnte
	 * @param annoEsercizio
	 * @param annoMovimento
	 * @param numeroMovimento
	 * @param tipoMovimento
	 * @param datiOperazioneDto
	 * @param isSub
	 * @return
	 */
	private <M extends MovimentoGestioneLigthAbstractDto> M caricaMovimentoLigthRoutine(SiacTMovgestTsFin siacTMovgestTs,Integer idEnte,String annoEsercizio,Integer annoMovimento,
			BigDecimal numeroMovimento,String tipoMovimento, DatiOperazioneDto datiOperazioneDto, boolean isSub){
		BigDecimal numero = new BigDecimal(siacTMovgestTs.getMovgestTsCode());
		M oggettoInfo = null;
		if(isSub){
			oggettoInfo = (M) new MovimentoGestioneSubLigthDto(idEnte, annoEsercizio, annoMovimento, numero, tipoMovimento);
		} else {
			oggettoInfo = (M) new MovimentoGestioneLigthDto(idEnte, annoEsercizio, annoMovimento, numero, tipoMovimento);
		}
		
		SiacDMovgestStatoFin statoSub = getStato(siacTMovgestTs, datiOperazioneDto);
		String codiceStatoSub = statoSub.getMovgestStatoCode();
		oggettoInfo.setStatoCode(codiceStatoSub);
		oggettoInfo.setMovgestTsId(siacTMovgestTs.getMovgestTsId());
		//disponibilita' ad incassare sub accertamento
		DisponibilitaMovimentoGestioneContainer disponibilitaAIncassare;
		if(isSub){
			disponibilitaAIncassare = calcolaDisponibiltaAIncassareSubAccertamento(siacTMovgestTs, codiceStatoSub, idEnte);
		} else {
			disponibilitaAIncassare = calcolaDisponibiltaAIncassareAccertamento(siacTMovgestTs, codiceStatoSub, idEnte);
		}
		oggettoInfo.setDisponibilitaAIncassare(disponibilitaAIncassare.getDisponibilita());
		oggettoInfo.setMotivazioneDisponibilitaAIncassare(disponibilitaAIncassare.getMotivazione());
		///
		//importo attuale sub:
		BigDecimal importoAttualeSub = siacTMovgestTsDetRepository.findImporto(idEnte, CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE, siacTMovgestTs.getMovgestTsId());
		oggettoInfo.setImportoAttuale(importoAttualeSub);
		///
		Soggetto soggettoMovimento = estraiSoggettoMovimento(datiOperazioneDto.getSiacDAmbito().getAmbitoCode(), idEnte, siacTMovgestTs,datiOperazioneDto);
		oggettoInfo.setSoggettoMovimento(soggettoMovimento);
		//SOGGETTO CLASSE:
		if(soggettoMovimento!=null){
			SiacDSoggettoClasseFin siacDSoggettoClasse = estraiSoggettoClasse(soggettoMovimento.getUid(), datiOperazioneDto);
			if(siacDSoggettoClasse!=null){
				oggettoInfo.setSoggettoClasseCode(siacDSoggettoClasse.getSoggettoClasseCode());
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return oggettoInfo;
	}
	
	/**
	 * Dato un soggetto estrae il codice della sua classe di appartenenza
	 * @param idSoggetto
	 * @param datiOperazioneDto
	 * @return
	 */
	public String estraiSoggettoClasseCode(Integer idSoggetto,DatiOperazioneDto datiOperazioneDto){
		String code = null;
		SiacDSoggettoClasseFin siacDSoggettoClasse = estraiSoggettoClasse(idSoggetto, datiOperazioneDto);
		if(siacDSoggettoClasse!=null){
			code = siacDSoggettoClasse.getSoggettoClasseCode();
		}
		//Termino restituendo l'oggetto di ritorno: 
        return code;
	}
	
	/**
	 * Dato un soggetto estrae la sua classe di appartenenza
	 * @param idSoggetto
	 * @param datiOperazioneDto
	 * @return
	 */
	private SiacDSoggettoClasseFin estraiSoggettoClasse(Integer idSoggetto,DatiOperazioneDto datiOperazioneDto){
		SiacDSoggettoClasseFin siacDSoggettoClasse = null;
		List<SiacRSoggettoClasseFin> l = siacRSoggettoClasseRepository.findValidiByIdSoggetto(idSoggetto, datiOperazioneDto.getTs());
		if(l!=null && l.size()>0){
			SiacRSoggettoClasseFin legameValido = l.get(0);
			siacDSoggettoClasse= legameValido.getSiacDSoggettoClasse();
		}
		//Termino restituendo l'oggetto di ritorno: 
        return siacDSoggettoClasse;
	}
	
	
	/**
	 * 
	 * Ricerca meno aggressiva della ricerca per chiave
	 * 
	 * @param richiedente
	 * @param ente
	 * @param annoEsercizio
	 * @param annoMovimento
	 * @param numeroMovimento
	 * @param tipoMovimento
	 * @param caricaDatiUlteriori (serve per caricare o meno le modifiche, per ottimizzare i tempi di ricerca)
	 * @return
	 */
	public Impegno ricercaImpegnoDettaglio(Richiedente richiedente, Ente ente, String annoEsercizio, Integer annoMovimento,BigDecimal numeroMovimento,  String tipoMovimento, boolean caricaSediEModalitaPagamento , boolean caricaDatiUlteriori)  {

		String methodname="ricercaImpegnoDettaglio";
		
		Impegno impegno = null;
		Integer idEnte = ente.getUid();
		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository.findOne(idEnte);
		DatiOperazioneDto datiOperazioneDto = new DatiOperazioneDto(System.currentTimeMillis(), Operazione.RICERCA , siacTEnteProprietario, richiedente.getAccount().getId());

		// leggo dal db
		SiacTMovgestFin siacTMovgest = siacTMovgestRepository.ricercaSiacTMovgestPk(idEnte, annoEsercizio, annoMovimento, numeroMovimento, tipoMovimento);	
		
		if(null!=siacTMovgest){
			
			List<SiacTMovgestTsFin> siacTMovgestTss = getTestataPiuListaSub(siacTMovgest);
			
			impegno = map(siacTMovgest, Impegno.class, FinMapId.SiacTMovgest_Impegno);
			impegno.setParereFinanziario(siacTMovgest.getParereFinanziario()); // si potrebbe provare a mettere il wildcare a true sul dozer 
			
			Boolean movimentoConSub = isMovimentoConSub(siacTMovgest.getSiacTMovgestTs()); // mi serve in alcuni controlli sotto
			
			impegno = (Impegno) convertiDatiTestataMovimentoGestione(siacTMovgest, siacTMovgestTss,  impegno, false);

			if(null!=siacTMovgestTss  && !siacTMovgestTss.isEmpty()){

				List<SubImpegno> elencoSubImpegni = new ArrayList<SubImpegno>();
				
				for(SiacTMovgestTsFin siacTMovgestTs : siacTMovgestTss){
					
					if(siacTMovgestTs.getDataFineValidita() == null && siacTMovgestTs.getDataCancellazione() == null){
						
						//System.out.println("ts.uid: "+siacTMovgestTs.getUid());
						//System.out.println("ts.tipocode: "+siacTMovgestTs.getSiacDMovgestTsTipo().getMovgestTsTipoCode());
						// SOGGETTO 
						Soggetto soggetto = EntityToModelConverter.setSoggettoMovimentoGestione(siacTMovgestTs);
						
						if(caricaSediEModalitaPagamento && soggetto!=null && soggetto.getUid()> 0  ){
							soggetto = estraiSediSecondarieEModalitaPagamento(richiedente,idEnte,  soggetto.getCodiceSoggetto(), CostantiFin.AMBITO_FIN,datiOperazioneDto);
						}
						
						// CLASSE SOGGETTO
						ClasseSoggetto classe = EntityToModelConverter.setClasseSoggettoMovimentoGestione(siacTMovgestTs);
						
						// Estraggo gli eventuali record di modifica ma solo se il flag caricaDatiUlteriori è true
						List<ModificaMovimentoGestioneSpesa> elencoModificheMovimentoGestioneSpesa = new ArrayList<ModificaMovimentoGestioneSpesa>();
						
						/*
						 * utilizzate in ricerca impegni per chiave e ricerca accertamento per chiave
						 * con il false esegue lo skip di questa parte di codice per aumentare le performance
						 */
						if(caricaDatiUlteriori){
						   elencoModificheMovimentoGestioneSpesa = estraiElencoModificheMovimentoGestioneSpesa(richiedente, siacTMovgestTs);
						}
										
						
						// STO CONVERTENDO UN IMPEGNO	
						if(CostantiFin.MOVGEST_TS_TIPO_TESTATA.equalsIgnoreCase(siacTMovgestTs.getSiacDMovgestTsTipo().getMovgestTsTipoCode())){ 
							
							// STATO_OPERATIVO_MOVIMENTO_GESTIONE_SPESA								
							EntityToModelConverter.setStatoOperativoMovimentoGestione(impegno, siacTMovgestTs);
						
							// Aggiungo il soggetto all'impegno estratto
							if(null!=soggetto){
								impegno.setSoggetto(soggetto);
							}
							
							if(null!=classe){
								impegno.setClasseSoggetto(classe);
							}
							
							
													
							// ATTO AMMINISTRATIVO
							// richiamo il set della struttura amm. base, quella della ricerca per chiave imposta il provvedimento anche sulle modifiche di imp/acc 
							List<SiacRMovgestTsAttoAmmFin> listaSiacRMovgestTsAttoAmm = siacTMovgestTs.getSiacRMovgestTsAttoAmms();
							if(null!=listaSiacRMovgestTsAttoAmm && listaSiacRMovgestTsAttoAmm.size() > 0){
								//estraggo l'unico record valido:
								SiacRMovgestTsAttoAmmFin siacRMovgestTsAttoAmm = DatiOperazioneUtil.getValido(listaSiacRMovgestTsAttoAmm, null);
								if(null!= siacRMovgestTsAttoAmm){
									EntityToModelConverter.settaAttoAmministrativoBase(siacRMovgestTsAttoAmm.getSiacTAttoAmm(), impegno);
								}
							}


							if(caricaDatiUlteriori){
								if(null!=elencoModificheMovimentoGestioneSpesa && elencoModificheMovimentoGestioneSpesa.size() > 0){									
									impegno.setListaModificheMovimentoGestioneSpesa(elencoModificheMovimentoGestioneSpesa);
								}
							}
							
							List<SiacTMovgestTsFin> listaTMovGestTs =  siacTMovgest.getSiacTMovgestTs();
							BigDecimal sommatoriaImportoAttualeSubImpegni = BigDecimal.ZERO;
							BigDecimal importoAttualeImpegno = BigDecimal.ZERO;
							DisponibilitaMovimentoGestioneContainer disponibilitaPagare = new DisponibilitaMovimentoGestioneContainer(BigDecimal.ZERO, "Disponibilita' non caricata");
							DisponibilitaMovimentoGestioneContainer disponibilitaFinanziare = new DisponibilitaMovimentoGestioneContainer(BigDecimal.ZERO, "Disponibilita' non caricata");
							
							importoAttualeImpegno = impegno.getImportoAttuale();
							
							if(caricaDatiUlteriori){
								//disp modifica:
								DisponibilitaMovimentoGestioneContainer disponibilitaModifica = calcolaDisponibilitaImpegnoModifica(siacTMovgestTs.getMovgestTsId(), datiOperazioneDto);
								impegno.setDisponibilitaImpegnoModifica(disponibilitaModifica.getDisponibilita());
								// SIAC-6695
								impegno.setMotivazioneDisponibilitaImpegnoModifica(disponibilitaModifica.getMotivazione());
							}
																				
							if(movimentoConSub){
							
								for(SiacTMovgestTsFin itSubs : listaTMovGestTs){
									if(itSubs.getDataFineValidita()==null && CostantiFin.MOVGEST_TS_TIPO_SUBIMPEGNO.equalsIgnoreCase(itSubs.getSiacDMovgestTsTipo().getMovgestTsTipoCode())){
										sommatoriaImportoAttualeSubImpegni = sommatoriaImportoAttualeSubImpegni.add(siacTMovgestTsDetRepository.findImporto(idEnte,  CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE,itSubs.getUid()));
									}
								}
							}
							
							DisponibilitaMovimentoGestioneContainer disponibilitaSubimpegnare;
							if(!StringUtilsFin.isEmpty(impegno.getStatoOperativoMovimentoGestioneSpesa()) && impegno.getStatoOperativoMovimentoGestioneSpesa().equals(CostantiFin.MOVGEST_STATO_ANNULLATO)){
								disponibilitaSubimpegnare = new DisponibilitaMovimentoGestioneContainer(BigDecimal.ZERO, "Se lo stato dell'impegno e' annullato, la disponibilita' deve essere ZERO");
							}else{
								disponibilitaSubimpegnare = new DisponibilitaMovimentoGestioneContainer(importoAttualeImpegno.subtract(sommatoriaImportoAttualeSubImpegni),
									"Disponibilita' calcolata come differenza tra importo attuale (" + importoAttualeImpegno + ") e totale importo subimpegni (" + sommatoriaImportoAttualeSubImpegni + ")"); 
							}
								
							if(caricaDatiUlteriori){
								disponibilitaFinanziare = calcolaDisponibilitaAFinanziare(siacTMovgestTs.getUid(), importoAttualeImpegno, impegno.getStatoOperativoMovimentoGestioneSpesa(), idEnte, datiOperazioneDto);
								disponibilitaPagare = calcolaDisponibilitaAPagareImpegno(siacTMovgestTs, impegno.getStatoOperativoMovimentoGestioneSpesa(), idEnte);
								
							}
														
							
							if(caricaDatiUlteriori){
								// carico eventuali VINCOLI legati
								List<VincoloImpegno> listaVincoli = getAccertamentiVincolati(siacTMovgestTs);
								// setto cmq la lista anche se nulla
								impegno.setVincoliImpegno(listaVincoli);
							}
							
							
							// dopo aver preso i vincoli calcolo la disponibilita a liquidare
							DisponibilitaMovimentoGestioneContainer disponibilitaLiquidareContainer = calcolaDisponibilitaALiquidare(siacTMovgestTs.getUid(), idEnte, datiOperazioneDto);
							
							impegno.setDisponibilitaPagare(disponibilitaPagare.getDisponibilita());
							impegno.setMotivazioneDisponibilitaPagare(disponibilitaPagare.getMotivazione());
							
							impegno.setSommaLiquidazioniDoc(importoAttualeImpegno.subtract(disponibilitaLiquidareContainer.getDisponibilita()));
							impegno.setTotaleSubImpegniBigDecimal(sommatoriaImportoAttualeSubImpegni);
							impegno.setDisponibilitaSubimpegnare(disponibilitaSubimpegnare.getDisponibilita());
							//SIAC-6695
							impegno.setMotivazioneDisponibilitaSubImpegnare(disponibilitaSubimpegnare.getMotivazione());
							impegno.setDisponibilitaFinanziare(disponibilitaFinanziare.getDisponibilita());
							// SIAC-6695
							impegno.setMotivazioneDisponibilitaFinanziare(disponibilitaFinanziare.getMotivazione());
							impegno.setDisponibilitaLiquidare(disponibilitaLiquidareContainer.getDisponibilita());
							// SIAC-6695
							impegno.setMotivazioneDisponibilitaLiquidare(disponibilitaLiquidareContainer.getMotivazione());
							
							BigDecimal disponibilitaLiquidareBase = impegnoDao.calcolaDisponibilitaALiquidare(siacTMovgestTs.getUid());
							impegno.setDisponibilitaLiquidareBase(disponibilitaLiquidareBase);
						
						} // STO CONVERTENDO UN SUBIMPEGNO
						else if(CostantiFin.MOVGEST_TS_TIPO_SUBIMPEGNO.equalsIgnoreCase(siacTMovgestTs.getSiacDMovgestTsTipo().getMovgestTsTipoCode())){
	
							SubImpegno sub = null;
							
							sub = map(siacTMovgestTs, SubImpegno.class, FinMapId.SiacTMovgestTs_SubImpegno);
							
							// STATO_OPERATIVO_MOVIMENTO_GESTIONE_SPESA								
							EntityToModelConverter.setStatoOperativoMovimentoGestione(sub, siacTMovgestTs);
						
							// Aggiungo il soggetto al sub-impegno estratto
							if(null!=soggetto){
								sub.setSoggetto(soggetto);
							}
							
							
							if(null!=classe){
								sub.setClasseSoggetto(classe);
							}
							
							
							EntityToModelConverter.setImporti(sub, siacTMovgestTs); // mi serve l'importo attuale in un controllo sotto
							
							// ATTO AMMINISTRATIVO
							// richiamo il set della struttura amm. base, quella della ricerca per chiave imposta il provvedimento anche sulle modifiche di imp/acc 
							List<SiacRMovgestTsAttoAmmFin> listaSiacRMovgestTsAttoAmm = siacTMovgestTs.getSiacRMovgestTsAttoAmms();
							if(null!=listaSiacRMovgestTsAttoAmm && listaSiacRMovgestTsAttoAmm.size() > 0){
								//estraggo l'unico record valido:
								SiacRMovgestTsAttoAmmFin siacRMovgestTsAttoAmm = DatiOperazioneUtil.getValido(listaSiacRMovgestTsAttoAmm, null);
								if(null!= siacRMovgestTsAttoAmm){
									EntityToModelConverter.settaAttoAmministrativoBase(siacRMovgestTsAttoAmm.getSiacTAttoAmm(), sub);
								}
							}
							
							if(caricaDatiUlteriori){
								if(null!=elencoModificheMovimentoGestioneSpesa && elencoModificheMovimentoGestioneSpesa.size() > 0){
									List<ModificaMovimentoGestioneSpesa> listaModificheDefinitiva = new ArrayList<ModificaMovimentoGestioneSpesa>();
									for(ModificaMovimentoGestioneSpesa spesa : elencoModificheMovimentoGestioneSpesa){
										spesa.setTipoMovimento(CostantiFin.MODIFICA_TIPO_SIM);
										spesa.setUidSubImpegno(sub.getUid());
										spesa.setNumeroSubImpegno(sub.getNumeroBigDecimal().intValue());
										listaModificheDefinitiva.add(spesa);
									}
									sub.setListaModificheMovimentoGestioneSpesa(listaModificheDefinitiva);
								}
							}
							
							DisponibilitaMovimentoGestioneContainer disponibilitaFinanziare = new DisponibilitaMovimentoGestioneContainer(BigDecimal.ZERO, "Disponibilita non caricata");
							DisponibilitaMovimentoGestioneContainer disponibilitaPagare = new DisponibilitaMovimentoGestioneContainer(BigDecimal.ZERO, "Disponibilita non caricata");
							DisponibilitaMovimentoGestioneContainer disponibilitaModifica = new DisponibilitaMovimentoGestioneContainer(BigDecimal.ZERO, "Disponibilita non caricata");;
							BigDecimal importoAttuale = BigDecimal.ZERO;
							
							// controllare!! RAFFA
							// DATI TRANSAZIONE ELEMENTARE:
							List<SiacRMovgestClassFin> listaSiacRMovgestClass = siacTMovgestTs.getSiacRMovgestClasses();
							sub = (SubImpegno) TransazioneElementareEntityToModelConverter.
									convertiDatiTransazioneElementare(sub, listaSiacRMovgestClass, siacTMovgestTs.getSiacRMovgestTsAttrs());
							
							//FIX PER  SIAC-2962 PER SETTARE IL CIG CHE SI PERDEVA (serve per inserisci liquidazione da sub impegno):
							EntityToModelConverter.setAttributiMovimentoGestione(sub, siacTMovgestTs);
								
							// calcolo disponibilità a liquidare
							// Jira 1896:  
							// se il sub è di un impegno parzialmente vincolato, la su disponiblità deve essere = 0 (quindi valgono le stesse 
							// condizioni impostate x l'impegno)
							DisponibilitaMovimentoGestioneContainer disponibilitaLiquidareContainer = calcolaDisponibilitaALiquidare(siacTMovgestTs.getUid(), idEnte, datiOperazioneDto);
														
							if(caricaDatiUlteriori){
								
								//disp modifica:
								disponibilitaModifica = calcolaDisponibilitaImpegnoModifica(siacTMovgestTs.getMovgestTsId(), datiOperazioneDto);
								
								// finanziarie
								// Jira-1784, l'importo a finanziare è sempre 0 perchè l'importo attuale, che si passa al metodo calcola disponibilità, 
								// viene passato sempre uguale a 0, mentre deve essere l'importo dell'impegno se siamo nell'impegno.. del sub se siamo nei sub
								importoAttuale = sub.getImportoAttuale() == null ? BigDecimal.ZERO : sub.getImportoAttuale();
								disponibilitaFinanziare = calcolaDisponibilitaAFinanziare(siacTMovgestTs.getUid(), importoAttuale, sub.getStatoOperativoMovimentoGestioneSpesa(), idEnte, datiOperazioneDto);
								disponibilitaPagare = calcolaDisponibilitaAPagareSubImpegno(siacTMovgestTs, sub.getStatoOperativoMovimentoGestioneSpesa(), idEnte);
								
							}
							
							sub.setDisponibilitaImpegnoModifica(disponibilitaModifica.getDisponibilita());
							// SIAC-6695
							sub.setMotivazioneDisponibilitaImpegnoModifica(disponibilitaModifica.getMotivazione());
							sub.setDisponibilitaLiquidare(disponibilitaLiquidareContainer.getDisponibilita()); // disp liquidare
							// SIAC-6695
							sub.setMotivazioneDisponibilitaLiquidare(disponibilitaLiquidareContainer.getMotivazione());
							sub.setDisponibilitaFinanziare(disponibilitaFinanziare.getDisponibilita()); // disp finanz
							// SIAC-6695
							sub.setMotivazioneDisponibilitaFinanziare(disponibilitaFinanziare.getMotivazione());
							sub.setDisponibilitaPagare(disponibilitaPagare.getDisponibilita());
							// SIAC-6695
							sub.setMotivazioneDisponibilitaPagare(disponibilitaPagare.getMotivazione());
							
							BigDecimal disponibilitaLiquidareBase = impegnoDao.calcolaDisponibilitaALiquidare(siacTMovgestTs.getUid());
							sub.setDisponibilitaLiquidareBase(disponibilitaLiquidareBase);
							
							elencoSubImpegni.add(sub);
						}
					}
				}
				
				
				if(null!=elencoSubImpegni && elencoSubImpegni.size() > 0)
					impegno.setElencoSubImpegni(elencoSubImpegni);
					
				DisponibilitaMovimentoGestioneContainer disponibilitaSubimpegnare = calcolaDisponibilitaImpegnoASubImpegnareEValorizzaTotaleSubImpegni(impegno);
				impegno.setDisponibilitaSubimpegnare(disponibilitaSubimpegnare.getDisponibilita());
				// SIAC-6695
				impegno.setMotivazioneDisponibilitaSubImpegnare(disponibilitaSubimpegnare.getMotivazione());

			} 
		} 
		

        return impegno;
	}




	protected Boolean isMovimentoConSub(
			List<SiacTMovgestTsFin> listaSiacTMovgestTs) {
		Boolean movimentoConSub = Boolean.FALSE;
		for (SiacTMovgestTsFin ts : listaSiacTMovgestTs) {
			
			if(ts.getDataCancellazione()== null && ts.getMovgestTsIdPadre()!=null){
				movimentoConSub = Boolean.TRUE;
				break;
			}
		}
		return movimentoConSub;
	}
	
	
	
	/**
	 * @param boolean caricaModifiche serve per caricare o meno le modifiche,
	 * per ottimizzare i tempi di ricerca
	 * @return
	 */
	public MovimentoGestione ricercaMovimentoPk(Richiedente richiedente, Ente ente, String annoEsercizio, Integer annoMovimento, BigDecimal numeroMovimento, String tipoMovimento, boolean caricaDatiUlteriori)  {

		String methodname="ricercaMovimentoPk";
		Integer codiceEnte = ente.getUid();
		T trovatoMovGestione = null;
		int idEnte = ente.getUid();
		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository.findOne(idEnte);
		DatiOperazioneDto datiOperazioneDto = new DatiOperazioneDto(System.currentTimeMillis(), Operazione.INSERIMENTO, siacTEnteProprietario, richiedente.getAccount().getId());

		SiacTMovgestFin siacTMovgest = siacTMovgestRepository.ricercaSiacTMovgestPk(codiceEnte, annoEsercizio, annoMovimento, numeroMovimento, tipoMovimento);	
		
		if(siacTMovgest != null){

			// Ciclo sulla tabella SiacTMovgestTsFin per estrarre i dettagli dei movimenti gestionali
			List<SiacTMovgestTsFin> listaSiacTMovgestTs = getTestataPiuListaSub(siacTMovgest);
			
			Boolean movimentoConSub = isMovimentoConSub(listaSiacTMovgestTs);
			
			if(listaSiacTMovgestTs != null && listaSiacTMovgestTs.size() > 0){

				List<SubImpegno> elencoSubImpegni = new ArrayList<SubImpegno>();
				List<SubAccertamento> elencoSubAccertamenti = new ArrayList<SubAccertamento>();
				
				// 11 FEB 2016: ottimizzazione soggetto:
				OttimizzazioneMovGestDto ottimizzazioneDto = new OttimizzazioneMovGestDto();
				List<Soggetto> distintiSoggettiCoinvolti = null;
				if(caricaDatiUlteriori){
					OttimizzazioneSoggettoDto ottimizzazioneSoggettoDto = caricaDatiOttimizzazioneRicercaSoggetto(listaSiacTMovgestTs);
					List<SiacTSoggettoFin> distintiSiacTSoggettiCoinvolti = ottimizzazioneSoggettoDto.getDistintiSiacTSoggettiCoinvolti();
					List<SiacRMovgestTsSogFin> distintiSiacRSoggettiCoinvolti = ottimizzazioneSoggettoDto.getDistintiSiacRSoggettiCoinvolti();
					//mapping model:
					distintiSoggettiCoinvolti = soggettoDad.ricercaSoggettoOPT(distintiSiacTSoggettiCoinvolti, true, false,ottimizzazioneSoggettoDto,datiOperazioneDto);
					//
					ottimizzazioneDto.setDistintiSoggettiCoinvolti(distintiSoggettiCoinvolti);
					ottimizzazioneDto.setDistintiSiacRSoggettiCoinvolti(distintiSiacRSoggettiCoinvolti);
					ottimizzazioneDto.setDistintiSiacTSoggettiCoinvolti(distintiSiacTSoggettiCoinvolti);
				}
				//
				
				//Pre carichiamo gli importi in maniera ottimizzata:
				List<SiacTMovgestTsDetFin> distintiSiacTMovgestTsDetCoinvolti = movimentoGestioneDao.ricercaByMovGestTsMassive(siacTMovgest.getSiacTMovgestTs(),"SiacTMovgestTsDetFin");
				ottimizzazioneDto.setDistintiSiacTMovgestTsDetCoinvolti(distintiSiacTMovgestTsDetCoinvolti);
				//
				
				//Converter con ottimizzazione dto:
				trovatoMovGestione = convertiMovimentoGestione(siacTMovgest,ottimizzazioneDto);
				//
				
				for(SiacTMovgestTsFin siacTMovgestTs : listaSiacTMovgestTs){
					if(siacTMovgestTs != null && siacTMovgestTs.getDataFineValidita() == null && siacTMovgestTs.getDataCancellazione() == null){
						//Se valido
						
						// Estraggo il soggetto
						Soggetto soggettoMovimento = null; 
						
						// 11 FEB 2016: ottimizzazione soggetto:
						//OLD LENTO:
//						if(caricaDatiUlteriori){
//							soggettoMovimento = estraiSoggettoMovimento(CostantiFin.AMBITO_FIN, idEnte, siacTMovgestTs);
//						}
						//NUOVO AGGIUNTO 11 FEB 2016: ottimizzazione soggetto:
						if(caricaDatiUlteriori){
							SiacTSoggettoFin siacTSog = ottimizzazioneDto.getSoggettoByMovGestTsId(siacTMovgestTs.getMovgestTsId());
							if(siacTSog != null){
								//puo' non averlo se ha dei sub
								soggettoMovimento = CommonUtil.getSoggettoByCode(distintiSoggettiCoinvolti, siacTSog.getSoggettoCode());
								
								if(soggettoMovimento != null && soggettoMovimento.getUid()> 0  ){
									soggettoMovimento = estraiSediSecondarieEModalitaPagamento(richiedente,idEnte,  soggettoMovimento.getCodiceSoggetto(), CostantiFin.AMBITO_FIN,datiOperazioneDto);
								}
							}
						}
						if(soggettoMovimento == null){
							soggettoMovimento = new Soggetto();
						}
						//
						
						String statoValido = "";
						 
						SiacDMovgestStatoFin statoSub = getStato(siacTMovgestTs, datiOperazioneDto);
						statoValido = statoSub.getMovgestStatoCode();

						// Estraggo gli eventuali record di modifica
						List<ModificaMovimentoGestioneSpesa> elencoModificheMovimentoGestioneSpesa = new ArrayList<ModificaMovimentoGestioneSpesa>();
						List<ModificaMovimentoGestioneEntrata> elencoModificheMovimentoGestioneEntrata = new ArrayList<ModificaMovimentoGestioneEntrata>();

						/*
						 * utilizzate in ricerca impegni per chiave e ricerca accertamento per chiave
						 * con il false esegue lo skip di questa parte di codice per aumentare le performance
						 */
						if(caricaDatiUlteriori){
						   
							if(tipoMovimento.equalsIgnoreCase(CostantiFin.MOVGEST_TIPO_IMPEGNO)){
								elencoModificheMovimentoGestioneSpesa = estraiElencoModificheMovimentoGestioneSpesa(richiedente, siacTMovgestTs);
							} else if(tipoMovimento.equalsIgnoreCase(CostantiFin.MOVGEST_TIPO_ACCERTAMENTO)){
								elencoModificheMovimentoGestioneEntrata = estraiElencoModificheMovimentoGestioneEntrata(richiedente, siacTMovgestTs);
							}
						}
						// Fine estrazione degli eventuali record di modifica

				
							
						
						if(CostantiFin.MOVGEST_TS_TIPO_SUBIMPEGNO.equalsIgnoreCase(siacTMovgestTs.getSiacDMovgestTsTipo().getMovgestTsTipoCode())){
	
							ST trovatoSubMovimento = null;
							trovatoSubMovimento = convertiSubMovimento(siacTMovgestTs, siacTMovgest, caricaDatiUlteriori,ottimizzazioneDto);							

							if(tipoMovimento.equalsIgnoreCase(CostantiFin.MOVGEST_TIPO_IMPEGNO)){
								// Aggiungo il soggetto al sub-impegno estratto
								if(soggettoMovimento != null){
									trovatoSubMovimento.setSoggetto(soggettoMovimento);
								}
								
								if(elencoModificheMovimentoGestioneSpesa != null && elencoModificheMovimentoGestioneSpesa.size() > 0){
									List<ModificaMovimentoGestioneSpesa> listaModificheDefinitiva = new ArrayList<ModificaMovimentoGestioneSpesa>();
									for(ModificaMovimentoGestioneSpesa spesa : elencoModificheMovimentoGestioneSpesa){
										spesa.setTipoMovimento(CostantiFin.MODIFICA_TIPO_SIM);
										spesa.setUidSubImpegno(trovatoSubMovimento.getUid());
										spesa.setNumeroSubImpegno(trovatoSubMovimento.getNumeroBigDecimal().intValue());
										listaModificheDefinitiva.add(spesa);
									}
									((SubImpegno)trovatoSubMovimento).setListaModificheMovimentoGestioneSpesa(listaModificheDefinitiva);
								}
								
																
								BigDecimal importoAttuale =  BigDecimal.ZERO;
								
								//disp modifica:
								DisponibilitaMovimentoGestioneContainer disponibilitaModifica = calcolaDisponibilitaImpegnoModifica(siacTMovgestTs.getMovgestTsId(), datiOperazioneDto);
								((SubImpegno)trovatoSubMovimento).setDisponibilitaImpegnoModifica(disponibilitaModifica.getDisponibilita());
								// SIAC-6695
								((SubImpegno)trovatoSubMovimento).setMotivazioneDisponibilitaImpegnoModifica(disponibilitaModifica.getMotivazione());
									
								// calcolo disponibilità a liquidare
								// Jira 1896:  
								// se il sub è di un impegno parzialmente vincolato, la su disponiblità deve essere = 0 (quindi valgono le stesse 
								// condizioni impostate x l'impegno)
								DisponibilitaMovimentoGestioneContainer disponibilitaLiquidareContainer = calcolaDisponibilitaALiquidare(siacTMovgestTs.getUid(), idEnte, datiOperazioneDto);
															
								
								// finanziarie
								// Jira-1784, l'importo a finanziare è sempre 0 perchè l'importo attuale, che si passa al metodo calcola disponibilità, 
								// viene passato sempre uguale a 0, mentre deve essere l'importo dell'impegno se siamo nell'impegno.. del sub se siamo nei sub
								if(((SubImpegno)trovatoSubMovimento).getImportoAttuale()!=null){
									importoAttuale = ((SubImpegno)trovatoSubMovimento).getImportoAttuale();
									
								}
								
								DisponibilitaMovimentoGestioneContainer disponibilitaFinanziare = calcolaDisponibilitaAFinanziare(siacTMovgestTs.getUid(), importoAttuale, statoValido, idEnte, datiOperazioneDto);
																
								((SubImpegno)trovatoSubMovimento).setDisponibilitaLiquidare(disponibilitaLiquidareContainer.getDisponibilita()); // disp liquidare
								// SIAC-6695
								((SubImpegno)trovatoSubMovimento).setMotivazioneDisponibilitaLiquidare(disponibilitaLiquidareContainer.getMotivazione());
								((SubImpegno)trovatoSubMovimento).setDisponibilitaFinanziare(disponibilitaFinanziare.getDisponibilita()); // disp finanz
								// SIAC-6695
								((SubImpegno)trovatoSubMovimento).setMotivazioneDisponibilitaFinanziare(disponibilitaFinanziare.getMotivazione());
								
								BigDecimal disponibilitaLiquidareBase = impegnoDao.calcolaDisponibilitaALiquidare(siacTMovgestTs.getUid());
								((SubImpegno)trovatoSubMovimento).setDisponibilitaLiquidareBase(disponibilitaLiquidareBase);
								
								DisponibilitaMovimentoGestioneContainer disponibilitaPagare = calcolaDisponibilitaAPagareSubImpegno(siacTMovgestTs, statoValido, idEnte);
								((SubImpegno)trovatoSubMovimento).setDisponibilitaPagare(disponibilitaPagare.getDisponibilita());
								((SubImpegno)trovatoSubMovimento).setMotivazioneDisponibilitaPagare(disponibilitaPagare.getMotivazione());
								elencoSubImpegni.add(((SubImpegno)trovatoSubMovimento));
								
							} else if(tipoMovimento.equalsIgnoreCase(CostantiFin.MOVGEST_TIPO_ACCERTAMENTO)){
								// Aggiungo il soggetto al sub-accertamento estratto
								if(null!=soggettoMovimento){
									trovatoSubMovimento.setSoggetto(soggettoMovimento);
								}
								if(null!=elencoModificheMovimentoGestioneEntrata && elencoModificheMovimentoGestioneEntrata.size() > 0){
									List<ModificaMovimentoGestioneEntrata> listaModificheDefinitiva = new ArrayList<ModificaMovimentoGestioneEntrata>();
									for(ModificaMovimentoGestioneEntrata entrata : elencoModificheMovimentoGestioneEntrata){
										entrata.setTipoMovimento(CostantiFin.MODIFICA_TIPO_SAC);
										entrata.setUidSubAccertamento(trovatoSubMovimento.getUid());
										entrata.setNumeroSubAccertamento(trovatoSubMovimento.getNumeroBigDecimal().intValue());
										listaModificheDefinitiva.add(entrata);
									}
									((SubAccertamento)trovatoSubMovimento).setListaModificheMovimentoGestioneEntrata(listaModificheDefinitiva);
								}
								
								
								DisponibilitaMovimentoGestioneContainer disponibilitaIncassare = calcolaDisponibiltaAIncassareSubAccertamento(siacTMovgestTs, statoValido, idEnte);
								((SubAccertamento)trovatoSubMovimento).setDisponibilitaIncassare(disponibilitaIncassare.getDisponibilita());
								((SubAccertamento)trovatoSubMovimento).setMotivazioneDisponibilitaIncassare(disponibilitaIncassare.getMotivazione());
								
								elencoSubAccertamenti.add(((SubAccertamento)trovatoSubMovimento));
								
							}
						} else if(CostantiFin.MOVGEST_TS_TIPO_TESTATA.equalsIgnoreCase(siacTMovgestTs.getSiacDMovgestTsTipo().getMovgestTsTipoCode())){
							if(tipoMovimento.equalsIgnoreCase(CostantiFin.MOVGEST_TIPO_IMPEGNO)){
								// Aggiungo il soggetto all'impegno estratto
								if(null!=soggettoMovimento){
									trovatoMovGestione.setSoggetto(soggettoMovimento);
								}
								
								if(null!=elencoModificheMovimentoGestioneSpesa && elencoModificheMovimentoGestioneSpesa.size() > 0){									
									 ((Impegno)trovatoMovGestione).setListaModificheMovimentoGestioneSpesa(elencoModificheMovimentoGestioneSpesa);
								}
								
								
								List<SiacTMovgestTsFin> listaTMovGestTs =  siacTMovgest.getSiacTMovgestTs();
								BigDecimal sommatoriaImportoAttualeSubImpegni = BigDecimal.ZERO;
								BigDecimal importoAttualeImpegno = BigDecimal.ZERO;
								
								//disp modifica:
								DisponibilitaMovimentoGestioneContainer disponibilitaModifica = calcolaDisponibilitaImpegnoModifica(siacTMovgestTs.getMovgestTsId(), datiOperazioneDto);
								((Impegno)trovatoMovGestione).setDisponibilitaImpegnoModifica(disponibilitaModifica.getDisponibilita());
								// SIAC-6695
								((Impegno)trovatoMovGestione).setMotivazioneDisponibilitaImpegnoModifica(disponibilitaModifica.getMotivazione());
								
								importoAttualeImpegno = ottimizzazioneDto.estraiImporto(siacTMovgestTs.getUid(), CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE);
								
								if(null!=listaTMovGestTs && listaTMovGestTs.size()>0){
									for(SiacTMovgestTsFin itSubs : listaTMovGestTs){
										if(itSubs.getDataFineValidita()==null && 
											CostantiFin.MOVGEST_TS_TIPO_SUBIMPEGNO.equalsIgnoreCase(itSubs.getSiacDMovgestTsTipo().getMovgestTsTipoCode())){
											//Se valido e di tipo sub impegno
											BigDecimal importoAttualeSubIt = ottimizzazioneDto.estraiImporto(itSubs.getUid(), CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE);
											sommatoriaImportoAttualeSubImpegni = sommatoriaImportoAttualeSubImpegni.add(importoAttualeSubIt);
										}
									}
									
									DisponibilitaMovimentoGestioneContainer disponibilitaFinanziare = calcolaDisponibilitaAFinanziare(siacTMovgestTs.getUid(), importoAttualeImpegno, statoValido, idEnte, datiOperazioneDto);
									DisponibilitaMovimentoGestioneContainer disponibilitaSubimpegnare;
									
									if(!StringUtilsFin.isEmpty(statoValido) && statoValido.equals(CostantiFin.MOVGEST_STATO_ANNULLATO)){
										disponibilitaSubimpegnare = new DisponibilitaMovimentoGestioneContainer(BigDecimal.ZERO, "Se lo stato e' annullato, la disponibilita' deve essere ZERO");
									}else{
										disponibilitaSubimpegnare = new DisponibilitaMovimentoGestioneContainer(importoAttualeImpegno.subtract(sommatoriaImportoAttualeSubImpegni),
											"Disponibilita calcolata come differenza tra l'importo attuale (" + importoAttualeImpegno + ") e totale dei subimpegni (" + sommatoriaImportoAttualeSubImpegni + ")"); 
									}
									
									DisponibilitaMovimentoGestioneContainer disponibilitaPagare = calcolaDisponibilitaAPagareImpegno(siacTMovgestTs, statoValido, idEnte);
									((Impegno)trovatoMovGestione).setDisponibilitaPagare(disponibilitaPagare.getDisponibilita());
									((Impegno)trovatoMovGestione).setMotivazioneDisponibilitaPagare(disponibilitaPagare.getMotivazione());
									
									// carico eventuali VINCOLI legati
									List<VincoloImpegno> listaVincoli = getAccertamentiVincolati(siacTMovgestTs);
									// setto cmq la lista anche se nulla
									((Impegno)trovatoMovGestione).setVincoliImpegno(listaVincoli);
									
									
									// dopo aver preso i vincoli calcolo la disponibilita a liquidare
									DisponibilitaMovimentoGestioneContainer disponibilitaLiquidareContainer = calcolaDisponibilitaALiquidare(siacTMovgestTs.getUid(), idEnte, datiOperazioneDto);
									((Impegno)trovatoMovGestione).setSommaLiquidazioniDoc(importoAttualeImpegno.subtract(disponibilitaLiquidareContainer.getDisponibilita()));
									
									((Impegno)trovatoMovGestione).setTotaleSubImpegniBigDecimal(sommatoriaImportoAttualeSubImpegni);
									((Impegno)trovatoMovGestione).setDisponibilitaSubimpegnare(disponibilitaSubimpegnare.getDisponibilita());
									// SIAC-6695
									((Impegno)trovatoMovGestione).setMotivazioneDisponibilitaSubImpegnare(disponibilitaSubimpegnare.getMotivazione());
									((Impegno)trovatoMovGestione).setDisponibilitaFinanziare(disponibilitaFinanziare.getDisponibilita());
									// SIAC-6695
									((Impegno)trovatoMovGestione).setMotivazioneDisponibilitaFinanziare(disponibilitaFinanziare.getMotivazione());
									((Impegno)trovatoMovGestione).setDisponibilitaLiquidare(disponibilitaLiquidareContainer.getDisponibilita());
									// SIAC-6695
									((Impegno)trovatoMovGestione).setMotivazioneDisponibilitaLiquidare(disponibilitaLiquidareContainer.getMotivazione());
									
									BigDecimal disponibilitaLiquidareBase = impegnoDao.calcolaDisponibilitaALiquidare(siacTMovgestTs.getUid());
									((Impegno)trovatoMovGestione).setDisponibilitaLiquidareBase(disponibilitaLiquidareBase);
								}
								
							} else if(tipoMovimento.equalsIgnoreCase(CostantiFin.MOVGEST_TIPO_ACCERTAMENTO)){
								// Aggiungo il soggetto all'impegno estratto
								if(null!=soggettoMovimento){
									trovatoMovGestione.setSoggetto(soggettoMovimento);
								}
								if(null!=elencoModificheMovimentoGestioneEntrata && elencoModificheMovimentoGestioneEntrata.size() > 0){
									((Accertamento)trovatoMovGestione).setListaModificheMovimentoGestioneEntrata(elencoModificheMovimentoGestioneEntrata);
								}
								
								// jira 2335: si richiede di abbattare a 0 la disponibilità a inccassare dell'accertamento se ci sono subAccertamenti
								// xchè il disponibile a incassare si calcola poi sui sub
								DisponibilitaMovimentoGestioneContainer disponibilitaIncassare;
								
								if(!movimentoConSub) {
									disponibilitaIncassare = calcolaDisponibiltaAIncassareAccertamento(siacTMovgestTs, statoValido, idEnte);
								} else {
									disponibilitaIncassare = new DisponibilitaMovimentoGestioneContainer(BigDecimal.ZERO, "Se movimento con sub, la disponibilita' a incassare deve essere ZERO");
								}
								
								((Accertamento)trovatoMovGestione).setDisponibilitaIncassare(disponibilitaIncassare.getDisponibilita());
								((Accertamento)trovatoMovGestione).setMotivazioneDisponibilitaIncassare(disponibilitaIncassare.getMotivazione());

								// disponibilita a ulitizzare
								DisponibilitaMovimentoGestioneContainer disponibilitaUtilizzare = calcolaDisponibilitaAUtilizzare(siacTMovgestTs,datiOperazioneDto);
								((Accertamento)trovatoMovGestione).setDisponibilitaUtilizzare(disponibilitaUtilizzare.getDisponibilita());
								// SIAC-6695
								((Accertamento)trovatoMovGestione).setMotivazioneDisponibilitaUtilizzare(disponibilitaUtilizzare.getMotivazione());
								
							}
						}
					}
				}
				
				if(tipoMovimento.equalsIgnoreCase(CostantiFin.MOVGEST_TIPO_IMPEGNO)){
					if(null!=elencoSubImpegni && elencoSubImpegni.size() > 0)
						((Impegno)trovatoMovGestione).setElencoSubImpegni(elencoSubImpegni);
					
					DisponibilitaMovimentoGestioneContainer disponibilitaSubimpegnare = calcolaDisponibilitaImpegnoASubImpegnareEValorizzaTotaleSubImpegni(((Impegno)trovatoMovGestione));
					((Impegno)trovatoMovGestione).setDisponibilitaSubimpegnare(disponibilitaSubimpegnare.getDisponibilita());
					// SIAC-6695
					((Impegno)trovatoMovGestione).setMotivazioneDisponibilitaSubImpegnare(disponibilitaSubimpegnare.getMotivazione());
				} else if(tipoMovimento.equalsIgnoreCase(CostantiFin.MOVGEST_TIPO_ACCERTAMENTO)){
					if(null!=elencoSubAccertamenti && elencoSubAccertamenti.size() > 0)
						((Accertamento)trovatoMovGestione).setElencoSubAccertamenti(elencoSubAccertamenti);
					
					DisponibilitaMovimentoGestioneContainer disponibilitaSubAccertare = calcolaDisponibilitaAccertamentoASubAccertareEValorizzaTotaleSubAccertamenti(((Accertamento)trovatoMovGestione));
					((Accertamento)trovatoMovGestione).setDisponibilitaSubAccertare(disponibilitaSubAccertare.getDisponibilita());
					// SIAC-6695
					((Accertamento)trovatoMovGestione).setMotivazioneDisponibilitaSubAccertare(disponibilitaSubAccertare.getMotivazione());
				}
				
			} 
		} 
		

        return trovatoMovGestione;
	}
	
	/**
	 * Dato un record SiacTMovgestTsc che rappresenta un impegno viene restituita
	 * la lista degli (eventuali) Vincoli con Accertamenti
	 * @param siacTMovgestTsImpegno
	 * @return
	 */
	protected List<VincoloImpegno> getAccertamentiVincolati(SiacTMovgestTsFin siacTMovgestTsImpegno){
		// carico eventuali VINCOLI legati
		List<VincoloImpegno> listaVincoli = null;
		if(siacTMovgestTsImpegno!=null){
			List<SiacRMovgestTsFin> listaRelazioniVincoli = siacTMovgestTsImpegno.getSiacRMovgestTsB();
			
			if(listaRelazioniVincoli!=null && listaRelazioniVincoli.size()>0){
				// presenza di accertamenti vincolati
				listaVincoli = new ArrayList<VincoloImpegno>();
				List<SiacRMovgestTsFin> siacTMovgestTsValidi = DatiOperazioneUtil.soloValidi(listaRelazioniVincoli, getNow());
				for (SiacRMovgestTsFin siacRMovgestTs : siacTMovgestTsValidi) {
					
					if(siacRMovgestTs!=null){
						
						SiacTMovgestTsFin siacTMovgestTsAcc = siacRMovgestTs.getSiacTMovgestTsA();
						
						if(siacTMovgestTsAcc!=null){
							
							SiacTMovgestFin siacTMovgesAcc = siacTMovgestTsAcc.getSiacTMovgest();
							
							VincoloImpegno vincolo = new VincoloImpegno();
							vincolo.setImporto(siacRMovgestTs.getMovgestTsImporto());
							Accertamento accertamento = new Accertamento();
							accertamento.setUid(siacTMovgesAcc.getUid());
							accertamento.setAnnoMovimento(siacTMovgesAcc.getMovgestAnno());
							accertamento.setNumeroBigDecimal(siacTMovgesAcc.getMovgestNumero());
							if(siacTMovgesAcc.getSiacRMovgestBilElems()!=null && !siacTMovgesAcc.getSiacRMovgestBilElems().isEmpty()){
								// prendo sempre il primo e unico
								// id chiave univoca del capitolo, mi servira nello strato superiore per avere il capitolo completo
								accertamento.setChiaveCapitoloEntrataGestione(siacTMovgesAcc.getSiacRMovgestBilElems().get(0).getSiacTBilElem().getElemId());
							}
							// anche id vincolo
							vincolo.setUid(siacRMovgestTs.getMovgestTsRId());
							vincolo.setAccertamento(accertamento);
							// aggiungo nella lista 
							listaVincoli.add(vincolo);
							
						}
						
					}
					
				}
			}
		}
		return listaVincoli;
	}
	
	/**
	 * Dato un record SiacTMovgestTsc che rappresenta un accertamento viene restituita
	 * la lista degli (eventuali) Vincoli con Impegni 
	 * @param siacTMovgestTsAccertamento
	 * @return
	 */
	private List<VincoloAccertamento> getImpegniVincolati(SiacTMovgestTsFin siacTMovgestTsAccertamento){
		// carico eventuali VINCOLI legati
		List<VincoloAccertamento> listaVincoli = null;
		if(siacTMovgestTsAccertamento!=null){
			List<SiacRMovgestTsFin> listaRelazioniVincoli = siacTMovgestTsAccertamento.getSiacRMovgestTsA();
			if(listaRelazioniVincoli!=null && listaRelazioniVincoli.size()>0){
				// presenza di accertamenti vincolati
				listaVincoli = new ArrayList<VincoloAccertamento>();
				List<SiacRMovgestTsFin> siacTMovgestTsValidi = DatiOperazioneUtil.soloValidi(listaRelazioniVincoli, getNow());
				for (SiacRMovgestTsFin siacRMovgestTs : siacTMovgestTsValidi) {
					
					SiacTMovgestTsFin siacTMovgestTsImp = siacRMovgestTs.getSiacTMovgestTsB();
					
					if(siacTMovgestTsImp!=null){
						
						SiacTMovgestFin siacTMovgesImp = siacTMovgestTsImp.getSiacTMovgest();
						
						VincoloAccertamento vincolo = new VincoloAccertamento();
						vincolo.setImporto(siacRMovgestTs.getMovgestTsImporto());
						Impegno impegno = new Impegno();
						impegno.setAnnoMovimento(siacTMovgesImp.getMovgestAnno());
						impegno.setNumeroBigDecimal(siacTMovgesImp.getMovgestNumero());
						impegno.setUid(siacTMovgesImp.getUid());
						if(siacTMovgesImp.getSiacRMovgestBilElems()!=null && !siacTMovgesImp.getSiacRMovgestBilElems().isEmpty()){
							// prendo sempre il primo e unico
							// id chiave univoca del capitolo, mi servira nello strato superiore per avere il capitolo completo
							impegno.setChiaveCapitoloUscitaGestione(siacTMovgesImp.getSiacRMovgestBilElems().get(0).getSiacTBilElem().getElemId());
						}
						// anche id vincolo
						vincolo.setUid(siacRMovgestTs.getMovgestTsRId());
						vincolo.setImpegno(impegno);
						// aggiungo nella lista 
						listaVincoli.add(vincolo);
						
					}
					
				}
			}
		}
		return listaVincoli;
	}
	
	/**
	 * Accetta un SiacTMovgestTsFin che puo' rappresentare un impegno o un accertamento.
	 * Capisce di che tipo si tratta (impegno o accertamento) e calcola il tot delle quote vincoli.
	 * @param siacTMovgestTs
	 * @return
	 */
	private BigDecimal calcolaTotQuoteVincoli(SiacTMovgestTsFin siacTMovgestTs){
		BigDecimal totQuoteVincoli = BigDecimal.ZERO;
		if(siacTMovgestTs!=null){
			if(CostantiFin.MOVGEST_TIPO_IMPEGNO.equalsIgnoreCase(siacTMovgestTs.getSiacTMovgest().getSiacDMovgestTipo().getMovgestTipoCode())){
				List<VincoloImpegno> vincoli = getAccertamentiVincolati(siacTMovgestTs);
				totQuoteVincoli = calcolaTotQuoteVincoliImp(vincoli);
			} else if(CostantiFin.MOVGEST_TIPO_ACCERTAMENTO.equalsIgnoreCase(siacTMovgestTs.getSiacTMovgest().getSiacDMovgestTipo().getMovgestTipoCode())){
				List<VincoloAccertamento> vincoli = getImpegniVincolati(siacTMovgestTs);
				totQuoteVincoli = calcolaTotQuoteVincoliAcc(vincoli);
			}
		}
		return totQuoteVincoli;
	}
	
	/**
	 * Semplice utility per calcolare la somma degli importi indicati
	 * @param vincoli
	 * @return
	 */
	private BigDecimal calcolaTotQuoteVincoliImp(List<VincoloImpegno> vincoli){
		BigDecimal totQuoteVincoli = BigDecimal.ZERO;
		if(vincoli!=null && vincoli.size()>0){
			for(VincoloImpegno it: vincoli){
				totQuoteVincoli = totQuoteVincoli.add(it.getImporto());
			}
		}
		return totQuoteVincoli;
	}
	
	/**
	 * Semplice utility per calcolare la somma degli importi indicati
	 * @param vincoli
	 * @return
	 */
	private BigDecimal calcolaTotQuoteVincoliAcc(List<VincoloAccertamento> vincoli){
		BigDecimal totQuoteVincoli = BigDecimal.ZERO;
		if(vincoli!=null && vincoli.size()>0){
			for(VincoloAccertamento it: vincoli){
				totQuoteVincoli = totQuoteVincoli.add(it.getImporto());
			}
		}
		return totQuoteVincoli;
	}
	
	
	/**
	 * 
	 * @param richiedente
	 * @param ente
	 * @param resultList
	 * @param caricaDatiUlteriori
	 * @param ottimizzazioneDto
	 * @return
	 */
	public List<T> ricercaMovimentoPkMssive(Richiedente richiedente, Ente ente,List<SiacTMovgestFin> resultList,
					boolean caricaDatiUlteriori,OttimizzazioneMovGestDto ottimizzazioneDto)  {

		Integer codiceEnte = ente.getUid();
		
		List<T> listaResult = new ArrayList<T>();
		
		int idEnte = ente.getUid();
		
		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository.findOne(idEnte);
		DatiOperazioneDto datiOperazioneDto = new DatiOperazioneDto(System.currentTimeMillis(), Operazione.INSERIMENTO, siacTEnteProprietario, richiedente.getAccount().getId());
		
		if(resultList!=null){
			
			for(SiacTMovgestFin siacTMovgest : resultList){
				
				T trovatoMovGestione = null;
				
				Integer annoMovimento = siacTMovgest.getMovgestAnno();
				BigDecimal numeroMovimento = siacTMovgest.getMovgestNumero();
				String tipoMovimento = siacTMovgest.getSiacDMovgestTipo().getMovgestTipoCode();
				
				trovatoMovGestione = convertiMovimentoGestioneOPT(siacTMovgest,ottimizzazioneDto);
				
				// Ciclo sulla tabella SiacTMovgestTsFin per estrarre i dettagli dei movimenti gestionali
				//List<SiacTMovgestTsFin> listaSiacTMovgestTs = getTestataPiuListaSub(siacTMovgest);
				List<SiacTMovgestTsFin> listaSiacTMovgestTs =siacTMovgest.getSiacTMovgestTs();
				listaSiacTMovgestTs = DatiOperazioneUtil.soloValidi(listaSiacTMovgestTs, getNow());
				
				if(null!=listaSiacTMovgestTs && listaSiacTMovgestTs.size() > 0){
					
	
					List<SubImpegno> elencoSubImpegni = new ArrayList<SubImpegno>();
					List<SubAccertamento> elencoSubAccertamenti = new ArrayList<SubAccertamento>();
					
					for(SiacTMovgestTsFin siacTMovgestTs : listaSiacTMovgestTs){
						if(null!=siacTMovgestTs && siacTMovgestTs.getDataFineValidita() == null && siacTMovgestTs.getDataCancellazione() == null){
							//Se valido
							
							// Estraggo il soggetto
							Soggetto soggettoMovimento = null; 
							
							if(caricaDatiUlteriori){
								soggettoMovimento = estraiSoggettoMovimento(CostantiFin.AMBITO_FIN, idEnte, siacTMovgestTs,datiOperazioneDto);
							}
							
							String statoValido = "";
							List<SiacRMovgestTsStatoFin> listaSiacRMovgestTsStato =ottimizzazioneDto.filtraSiacRMovgestTsStatoByMovgestTs(siacTMovgestTs.getMovgestTsId());
							SiacDMovgestStatoFin statoSub = getStato(siacTMovgestTs, datiOperazioneDto,listaSiacRMovgestTsStato);
							
							statoValido = statoSub.getMovgestStatoCode();
	
							// Estraggo gli eventuali record di modifica
							List<ModificaMovimentoGestioneSpesa> elencoModificheMovimentoGestioneSpesa = new ArrayList<ModificaMovimentoGestioneSpesa>();
							List<ModificaMovimentoGestioneEntrata> elencoModificheMovimentoGestioneEntrata = new ArrayList<ModificaMovimentoGestioneEntrata>();
	
							/*
							 * utilizzate in ricerca impegni per chiave e ricerca accertamento per chiave
							 * con il false esegue lo skip di questa parte di codice per aumentare le performance
							 */
							if(caricaDatiUlteriori){
							   
								if(tipoMovimento.equalsIgnoreCase(CostantiFin.MOVGEST_TIPO_IMPEGNO)){
									elencoModificheMovimentoGestioneSpesa = estraiElencoModificheMovimentoGestioneSpesa(richiedente, siacTMovgestTs);
								} else if(tipoMovimento.equalsIgnoreCase(CostantiFin.MOVGEST_TIPO_ACCERTAMENTO)){
									elencoModificheMovimentoGestioneEntrata = estraiElencoModificheMovimentoGestioneEntrata(richiedente, siacTMovgestTs);
								}
							}
							// Fine estrazione degli eventuali record di modifica
	
							
							if(CostantiFin.MOVGEST_TS_TIPO_SUBIMPEGNO.equalsIgnoreCase(siacTMovgestTs.getSiacDMovgestTsTipo().getMovgestTsTipoCode())){
		
								ST trovatoSubMovimento = null;
								
								trovatoSubMovimento = convertiSubMovimentoOPT(siacTMovgestTs, siacTMovgest,ottimizzazioneDto);
	
								if(tipoMovimento.equalsIgnoreCase(CostantiFin.MOVGEST_TIPO_IMPEGNO)){
									// Aggiungo il soggetto al sub-impegno estratto
									if(null!=soggettoMovimento){
										trovatoSubMovimento.setSoggetto(soggettoMovimento);
									}
									
									if(null!=elencoModificheMovimentoGestioneSpesa && elencoModificheMovimentoGestioneSpesa.size() > 0){
										List<ModificaMovimentoGestioneSpesa> listaModificheDefinitiva = new ArrayList<ModificaMovimentoGestioneSpesa>();
										for(ModificaMovimentoGestioneSpesa spesa : elencoModificheMovimentoGestioneSpesa){
											spesa.setTipoMovimento(CostantiFin.MODIFICA_TIPO_SIM);
											spesa.setUidSubImpegno(trovatoSubMovimento.getUid());
											spesa.setNumeroSubImpegno(trovatoSubMovimento.getNumeroBigDecimal().intValue());
											listaModificheDefinitiva.add(spesa);
										}
										((SubImpegno)trovatoSubMovimento).setListaModificheMovimentoGestioneSpesa(listaModificheDefinitiva);
									}
									
									
									BigDecimal importoAttuale = siacTMovgestTsDetRepository.findImporto(idEnte, CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE, siacTMovgestTs.getUid());
										
									// liquidare
									DisponibilitaMovimentoGestioneContainer disponibilitaLiquidareContainer = calcolaDisponibilitaALiquidare(siacTMovgestTs.getUid(), idEnte, datiOperazioneDto);
									// finanziarie
									DisponibilitaMovimentoGestioneContainer disponibilitaFinanziare = calcolaDisponibilitaAFinanziare(siacTMovgestTs.getUid(), importoAttuale, statoValido, idEnte, datiOperazioneDto);
										
									((SubImpegno)trovatoSubMovimento).setDisponibilitaLiquidare(disponibilitaLiquidareContainer.getDisponibilita()); // disp liquidare
									// SIAC-6695
									((SubImpegno)trovatoSubMovimento).setMotivazioneDisponibilitaLiquidare(disponibilitaLiquidareContainer.getMotivazione());
									((SubImpegno)trovatoSubMovimento).setDisponibilitaFinanziare(disponibilitaFinanziare.getDisponibilita()); // disp finanz
									// SIAC-6695
									((SubImpegno)trovatoSubMovimento).setMotivazioneDisponibilitaFinanziare(disponibilitaFinanziare.getMotivazione());
									
									BigDecimal disponibilitaLiquidareBase = impegnoDao.calcolaDisponibilitaALiquidare(siacTMovgestTs.getUid());
									((SubImpegno)trovatoSubMovimento).setDisponibilitaLiquidareBase(disponibilitaLiquidareBase);
									
									DisponibilitaMovimentoGestioneContainer disponibilitaPagare = calcolaDisponibilitaAPagareSubImpegno(siacTMovgestTs, statoValido, idEnte);
									((SubImpegno)trovatoSubMovimento).setDisponibilitaPagare(disponibilitaPagare.getDisponibilita());
									((SubImpegno)trovatoSubMovimento).setMotivazioneDisponibilitaPagare(disponibilitaPagare.getMotivazione());
									elencoSubImpegni.add(((SubImpegno)trovatoSubMovimento));
									
								} else if(tipoMovimento.equalsIgnoreCase(CostantiFin.MOVGEST_TIPO_ACCERTAMENTO)){
									// Aggiungo il soggetto al sub-accertamento estratto
									if(null!=soggettoMovimento){
										trovatoSubMovimento.setSoggetto(soggettoMovimento);
									}
									if(null!=elencoModificheMovimentoGestioneEntrata && elencoModificheMovimentoGestioneEntrata.size() > 0){
										List<ModificaMovimentoGestioneEntrata> listaModificheDefinitiva = new ArrayList<ModificaMovimentoGestioneEntrata>();
										for(ModificaMovimentoGestioneEntrata entrata : elencoModificheMovimentoGestioneEntrata){
											entrata.setTipoMovimento(CostantiFin.MODIFICA_TIPO_SAC);
											entrata.setUidSubAccertamento(trovatoSubMovimento.getUid());
											entrata.setNumeroSubAccertamento(trovatoSubMovimento.getNumeroBigDecimal().intValue());
											listaModificheDefinitiva.add(entrata);
										}
										((SubAccertamento)trovatoSubMovimento).setListaModificheMovimentoGestioneEntrata(listaModificheDefinitiva);
									}
									
									
									//BigDecimal disponibilitaIncassare = calcolaDisponibiltaAIncassareSubAccertamento(siacTMovgestTs, statoValido, idEnte);
									DisponibilitaMovimentoGestioneContainer disponibilitaIncassare =  calcolaDisponibiltaAIncassareSubAccertamentoOPT(siacTMovgestTs, statoValido, idEnte, ottimizzazioneDto);
									((SubAccertamento)trovatoSubMovimento).setDisponibilitaIncassare(disponibilitaIncassare.getDisponibilita());
									((SubAccertamento)trovatoSubMovimento).setMotivazioneDisponibilitaIncassare(disponibilitaIncassare.getMotivazione());
									
									elencoSubAccertamenti.add(((SubAccertamento)trovatoSubMovimento));
									
								}
								
							} else if(CostantiFin.MOVGEST_TS_TIPO_TESTATA.equalsIgnoreCase(siacTMovgestTs.getSiacDMovgestTsTipo().getMovgestTsTipoCode())){
								
								if(tipoMovimento.equalsIgnoreCase(CostantiFin.MOVGEST_TIPO_IMPEGNO)){
									
									// Aggiungo il soggetto all'impegno estratto
									if(null!=soggettoMovimento){
										trovatoMovGestione.setSoggetto(soggettoMovimento);
									}
									
									if(null!=elencoModificheMovimentoGestioneSpesa && elencoModificheMovimentoGestioneSpesa.size() > 0){									
										 ((Impegno)trovatoMovGestione).setListaModificheMovimentoGestioneSpesa(elencoModificheMovimentoGestioneSpesa);
									}
									
									
									List<SiacTMovgestTsFin> listaTMovGestTs =  siacTMovgest.getSiacTMovgestTs();
									BigDecimal sommatoriaImportoAttualeSubImpegni = BigDecimal.ZERO;
									BigDecimal importoAttualeImpegno = BigDecimal.ZERO;
									
									importoAttualeImpegno = siacTMovgestTsDetRepository.findImporto(idEnte, CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE, siacTMovgestTs.getUid());
									
									if(null!=listaTMovGestTs && listaTMovGestTs.size()>0){
										for(SiacTMovgestTsFin itSubs : listaTMovGestTs){
											if(itSubs.getDataFineValidita()==null && 
												CostantiFin.MOVGEST_TS_TIPO_SUBIMPEGNO.equalsIgnoreCase(itSubs.getSiacDMovgestTsTipo().getMovgestTsTipoCode())){
												//Se valido e di tipo subimpegno
												sommatoriaImportoAttualeSubImpegni = sommatoriaImportoAttualeSubImpegni.add(siacTMovgestTsDetRepository.findImporto(idEnte,  CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE,itSubs.getUid()));
											}
										}
										
										DisponibilitaMovimentoGestioneContainer disponibilitaFinanziare = calcolaDisponibilitaAFinanziare(siacTMovgestTs.getUid(), importoAttualeImpegno, statoValido, idEnte, datiOperazioneDto);
										DisponibilitaMovimentoGestioneContainer disponibilitaLiquidareContainer = calcolaDisponibilitaALiquidare(siacTMovgestTs.getUid(), idEnte, datiOperazioneDto);
										DisponibilitaMovimentoGestioneContainer disponibilitaSubimpegnare;
										
										if(!StringUtilsFin.isEmpty(statoValido) && statoValido.equals(CostantiFin.MOVGEST_STATO_ANNULLATO)){
											disponibilitaSubimpegnare = new DisponibilitaMovimentoGestioneContainer(BigDecimal.ZERO, "Se lo stato e' annullato, la disponibilita' deve essere ZERO");
										}else{
											disponibilitaSubimpegnare = new DisponibilitaMovimentoGestioneContainer(importoAttualeImpegno.subtract(sommatoriaImportoAttualeSubImpegni),
												"Disponibilita calcolata come differenza tra l'importo attuale (" + importoAttualeImpegno + ") e totale dei subimpegni (" + sommatoriaImportoAttualeSubImpegni + ")"); 
										}
										
										((Impegno)trovatoMovGestione).setTotaleSubImpegniBigDecimal(sommatoriaImportoAttualeSubImpegni);
										((Impegno)trovatoMovGestione).setDisponibilitaSubimpegnare(disponibilitaSubimpegnare.getDisponibilita());
										// SIAC-6695
										((Impegno)trovatoMovGestione).setMotivazioneDisponibilitaSubImpegnare(disponibilitaSubimpegnare.getMotivazione());
										((Impegno)trovatoMovGestione).setDisponibilitaFinanziare(disponibilitaFinanziare.getDisponibilita());
										// SIAC-6695
										((Impegno)trovatoMovGestione).setMotivazioneDisponibilitaFinanziare(disponibilitaFinanziare.getMotivazione());
										((Impegno)trovatoMovGestione).setDisponibilitaLiquidare(disponibilitaLiquidareContainer.getDisponibilita());
										// SIAC-6695
										((Impegno)trovatoMovGestione).setMotivazioneDisponibilitaLiquidare(disponibilitaLiquidareContainer.getMotivazione());
										
										BigDecimal disponibilitaLiquidareBase = impegnoDao.calcolaDisponibilitaALiquidare(siacTMovgestTs.getUid());
										((Impegno)trovatoMovGestione).setDisponibilitaLiquidareBase(disponibilitaLiquidareBase);
	
										DisponibilitaMovimentoGestioneContainer disponibilitaPagare = calcolaDisponibilitaAPagareImpegno(siacTMovgestTs, statoValido, idEnte);
										((Impegno)trovatoMovGestione).setDisponibilitaPagare(disponibilitaPagare.getDisponibilita());
										// SIAC-6695
										((Impegno)trovatoMovGestione).setMotivazioneDisponibilitaPagare(disponibilitaPagare.getMotivazione());
									}
									
								} else if(tipoMovimento.equalsIgnoreCase(CostantiFin.MOVGEST_TIPO_ACCERTAMENTO)){
									
									// Aggiungo il soggetto all'impegno estratto
									if(null!=soggettoMovimento){
										trovatoMovGestione.setSoggetto(soggettoMovimento);
									}
									if(null!=elencoModificheMovimentoGestioneEntrata && elencoModificheMovimentoGestioneEntrata.size() > 0){
										((Accertamento)trovatoMovGestione).setListaModificheMovimentoGestioneEntrata(elencoModificheMovimentoGestioneEntrata);
									}
									
									DisponibilitaMovimentoGestioneContainer disponibilitaIncassare = calcolaDisponibiltaAIncassareAccertamentoOPT(siacTMovgestTs, statoValido, idEnte,ottimizzazioneDto);
									((Accertamento)trovatoMovGestione).setDisponibilitaIncassare(disponibilitaIncassare.getDisponibilita());
									((Accertamento)trovatoMovGestione).setMotivazioneDisponibilitaIncassare(disponibilitaIncassare.getMotivazione());
									
								}
							}
						}
					}
					
					
					if(tipoMovimento.equalsIgnoreCase(CostantiFin.MOVGEST_TIPO_IMPEGNO)){
						if(null!=elencoSubImpegni && elencoSubImpegni.size() > 0)
							((Impegno)trovatoMovGestione).setElencoSubImpegni(elencoSubImpegni);
						
						DisponibilitaMovimentoGestioneContainer disponibilitaSubimpegnare = calcolaDisponibilitaImpegnoASubImpegnareEValorizzaTotaleSubImpegni(((Impegno)trovatoMovGestione));
						((Impegno)trovatoMovGestione).setDisponibilitaSubimpegnare(disponibilitaSubimpegnare.getDisponibilita());
						// SIAC-6695
						((Impegno)trovatoMovGestione).setMotivazioneDisponibilitaSubImpegnare(disponibilitaSubimpegnare.getMotivazione());
					} else if(tipoMovimento.equalsIgnoreCase(CostantiFin.MOVGEST_TIPO_ACCERTAMENTO)){
						if(null!=elencoSubAccertamenti && elencoSubAccertamenti.size() > 0)
							((Accertamento)trovatoMovGestione).setElencoSubAccertamenti(elencoSubAccertamenti);
						
						DisponibilitaMovimentoGestioneContainer disponibilitaSubAccertare = calcolaDisponibilitaAccertamentoASubAccertareEValorizzaTotaleSubAccertamenti(((Accertamento)trovatoMovGestione));
						((Accertamento)trovatoMovGestione).setDisponibilitaSubAccertare(disponibilitaSubAccertare.getDisponibilita());
						// SIAC-6695
						((Accertamento)trovatoMovGestione).setMotivazioneDisponibilitaSubAccertare(disponibilitaSubAccertare.getMotivazione());
					}
					
				} 
				
				if(trovatoMovGestione!=null){
					listaResult.add(trovatoMovGestione);
				}
				
			}
		}
		
		//Termino restituendo l'oggetto di ritorno: 
        return listaResult;
	}
	
	/**
	 * Ad uso interno al metodo "componiListaSubPerResponse", il quale a sua volta e' un sotto-metodo di aggiornaImpegno
	 * @param tipoMovimento
	 * @param annoMovimento
	 * @param numeroMovimento
	 * @param richiedente
	 * @param siacTMovgestTs
	 * @param datiOperazioneDto
	 * @param siactMovegst
	 * @return
	 */
	private ST completaSubImpegnoDaAggiorna(String tipoMovimento,Integer annoMovimento, BigDecimal numeroMovimento,Richiedente richiedente, 
			SiacTMovgestTsFin siacTMovgestTs, DatiOperazioneDto datiOperazioneDto,SiacTMovgestFin siactMovegst){
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		datiOperazioneDto.setCurrMillisec(getNow().getTime());
		
		ST trovatoSubMovimento = convertiSubMovimento(siacTMovgestTs, siactMovegst, true);	
		
		Soggetto soggettoMovimento = estraiSoggettoMovimento(CostantiFin.AMBITO_FIN, idEnte, siacTMovgestTs,datiOperazioneDto);
		
		SiacDMovgestStatoFin statoSub = getStato(siacTMovgestTs, datiOperazioneDto);
		String statoValido = statoSub.getMovgestStatoCode();


		if(null!=soggettoMovimento){
			trovatoSubMovimento.setSoggetto(soggettoMovimento);
		}
		
		
		BigDecimal importoAttuale =  BigDecimal.ZERO;
		
		importoAttuale = siacTMovgestTsDetRepository.findImporto(idEnte, CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE, siacTMovgestTs.getUid());
			
		// liquidare
		DisponibilitaMovimentoGestioneContainer disponibilitaLiquidareContainer = calcolaDisponibilitaALiquidare(siacTMovgestTs.getUid(), idEnte, datiOperazioneDto);
		// finanziarie
		DisponibilitaMovimentoGestioneContainer disponibilitaFinanziare = calcolaDisponibilitaAFinanziare(siacTMovgestTs.getUid(), importoAttuale, statoValido, idEnte, datiOperazioneDto);
			
		((SubImpegno)trovatoSubMovimento).setDisponibilitaLiquidare(disponibilitaLiquidareContainer.getDisponibilita()); // disp liquidare
		// SIAC-6695
		((SubImpegno)trovatoSubMovimento).setMotivazioneDisponibilitaLiquidare(disponibilitaLiquidareContainer.getMotivazione());
		((SubImpegno)trovatoSubMovimento).setDisponibilitaFinanziare(disponibilitaFinanziare.getDisponibilita()); // disp finanz
		// SIAC-6695
		((SubImpegno)trovatoSubMovimento).setMotivazioneDisponibilitaFinanziare(disponibilitaFinanziare.getMotivazione());
		
		BigDecimal disponibilitaLiquidareBase = impegnoDao.calcolaDisponibilitaALiquidare(siacTMovgestTs.getUid());
		((SubImpegno)trovatoSubMovimento).setDisponibilitaLiquidareBase(disponibilitaLiquidareBase);
		
		DisponibilitaMovimentoGestioneContainer disponibilitaPagare = calcolaDisponibilitaAPagareSubImpegno(siacTMovgestTs, statoValido, idEnte);
		((SubImpegno)trovatoSubMovimento).setDisponibilitaPagare(disponibilitaPagare.getDisponibilita());
		// SIAC-6695
		((SubImpegno)trovatoSubMovimento).setMotivazioneDisponibilitaPagare(disponibilitaPagare.getMotivazione());
		//Termino restituendo l'oggetto di ritorno: 
        return trovatoSubMovimento;
	}
	
	/**
	 * Ad uso interno al metodo "componiListaSubPerResponse", il quale a sua volta e' un sotto-metodo di aggiornaImpegno
	 * @param tipoMovimento
	 * @param annoMovimento
	 * @param numeroMovimento
	 * @param richiedente
	 * @param siacTMovgestTs
	 * @param datiOperazioneDto
	 * @param siactMovegst
	 * @return
	 */
	private ST completaSubAccertamentoDaAggiorna(String tipoMovimento,Integer annoMovimento, BigDecimal numeroMovimento,Richiedente richiedente, 
			SiacTMovgestTsFin siacTMovgestTs, DatiOperazioneDto datiOperazioneDto,SiacTMovgestFin siactMovegst){
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		datiOperazioneDto.setCurrMillisec(getNow().getTime());
		
		ST trovatoSubMovimento = convertiSubMovimento(siacTMovgestTs, siactMovegst, true);	
		
		Soggetto soggettoMovimento = estraiSoggettoMovimento(CostantiFin.AMBITO_FIN, idEnte, siacTMovgestTs,datiOperazioneDto);
		
		SiacDMovgestStatoFin statoSub = getStato(siacTMovgestTs, datiOperazioneDto);
		String statoValido = statoSub.getMovgestStatoCode();
		
		// Aggiungo il soggetto al sub-accertamento estratto
		if(null!=soggettoMovimento){
			trovatoSubMovimento.setSoggetto(soggettoMovimento);
		}
		DisponibilitaMovimentoGestioneContainer disponibilitaIncassare = calcolaDisponibiltaAIncassareSubAccertamento(siacTMovgestTs, statoValido, idEnte);
		((SubAccertamento)trovatoSubMovimento).setDisponibilitaIncassare(disponibilitaIncassare.getDisponibilita());
		((SubAccertamento)trovatoSubMovimento).setMotivazioneDisponibilitaIncassare(disponibilitaIncassare.getMotivazione());
		
		//Termino restituendo l'oggetto di ritorno: 
        return trovatoSubMovimento;
	}

	/**
	 * Si occupa di calcolare la disponibilita a utilizzare per un accertamento indicato
	 * @param siacTMovgestTs
	 * @return
	 */
	protected DisponibilitaMovimentoGestioneContainer calcolaDisponibilitaAUtilizzare(SiacTMovgestTsFin siacTMovgestTs,DatiOperazioneDto datiOperazioneDto){		
		//diff tra utilizzabile meno sommatoria a tutte le quote legate all'accertamento
		BigDecimal risultato = BigDecimal.ZERO;
		BigDecimal utilizzabile = BigDecimal.ZERO;
		BigDecimal totQuoteVincoli = BigDecimal.ZERO;
		if(siacTMovgestTs!=null){
			utilizzabile = estraiImportoUtilizzabileByMovgestTsId(siacTMovgestTs.getMovgestTsId(), datiOperazioneDto);
			totQuoteVincoli = calcolaTotQuoteVincoli(siacTMovgestTs);
			risultato = utilizzabile.subtract(totQuoteVincoli);
		}
		return new DisponibilitaMovimentoGestioneContainer(risultato, "Disponibilita' calcolata come differenza tra utilizzabile (" + utilizzabile
				+ ") e totale delle quote vincolate (" + totQuoteVincoli + ")");
	}	
	
	
	/**
	 * calcola la disponibilita a finanziare per un impegno o per un subimpegno indicato
	 * @param idMovGestTs
	 * @param importoAttuale
	 * @param statoCod
	 * @param idEnte
	 * @param datiOperazioneDto
	 * @return
	 */
	protected DisponibilitaMovimentoGestioneContainer calcolaDisponibilitaAFinanziare(Integer idMovGestTs,BigDecimal importoAttuale,String statoCod,Integer idEnte,DatiOperazioneDto datiOperazioneDto){
		return new DisponibilitaMovimentoGestioneContainer(BigDecimal.ZERO, "Se lo stato non e' DEFINITIVO, la disponibilita' deve essere ZERO");
	}
	
	/**
	 * calcola la disponibilita a liquidare per un impegno o per un subimpegno indicato
	 * @param idMovGestTs
	 * @param importoAttuale
	 * @param statoCod
	 * @param idEnte
	 * @param datiOperazioneDto
	 * @return
	 */
	protected DisponibilitaMovimentoGestioneContainer calcolaDisponibilitaALiquidare(Integer idMovGestTs,Integer idEnte,DatiOperazioneDto datiOperazioneDto){
		//il vecchio calcolo era questo:
		//calcolaDisponibilitaALiquidareOLD(idMovGestTs, importoAttuale, statoCod, idEnte, datiOperazioneDto);
		
		SiacTMovgestTsFin siacTMovgestTs = siacTMovgestTsRepository.findOne(idMovGestTs);
		
		if(siacTMovgestTs!=null){
			
			//variabili che rappresenteranno sempre l'impegno (nel caso di sub verra' ricavato):
			SiacTMovgestTsFin siacTMovgestTsImpegno = siacTMovgestTs;
			SiacTMovgestFin siacTMovgestImpegno = siacTMovgestTs.getSiacTMovgest();
			Integer idMovGestTsImpegno = idMovGestTs;
			//////////////////////////////////////////////////////////////////////////////////
//			
			//Dobbiamo capire se si tratta di un impegno o di un subimpegno:
			
			if(siacTMovgestTs.getMovgestTsIdPadre()!=null){
				log.debug("calcolaDisponibilitaALiquidare", " ricerca l'impegno (" +siacTMovgestTs.getMovgestTsIdPadre() +" per il subId (" + siacTMovgestTs.getUid() +")");
				//e' un SUBIMPEGNO
				siacTMovgestTsImpegno = siacTMovgestTsRepository.findOne(siacTMovgestTs.getMovgestTsIdPadre());
				idMovGestTsImpegno = siacTMovgestTs.getMovgestTsIdPadre();
				siacTMovgestImpegno = siacTMovgestTsImpegno.getSiacTMovgest();
				
			}
			
			BigDecimal importoAttuale = estraiImportoAttualeByMovgestTsId(idMovGestTsImpegno, datiOperazioneDto);
			
				
			// (1)	SE impegno. stato <> DEFINITIVO  --> disponibilitaLiquidare = 0
			SiacRMovgestTsStatoFin siacRMovgestStato =  DatiOperazioneUtil.getValido(siacTMovgestTs.getSiacRMovgestTsStatos(), getNow());
			if(siacRMovgestStato.getSiacDMovgestStato() != null 
					&& ( siacRMovgestStato.getSiacDMovgestStato().getMovgestStatoCode().equals(CostantiFin.MOVGEST_STATO_PROVVISORIO) ||
							siacRMovgestStato.getSiacDMovgestStato().getMovgestStatoCode().equals(CostantiFin.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE))){
				return new DisponibilitaMovimentoGestioneContainer(BigDecimal.ZERO, "Se lo stato del movimento e' PROVVISORIO ovvero DEFINITIVO_NON_LIQUIDABILE la disponibilita' deve essere ZERO");
			}
			
			// (1)bis  impegno.anno > bilancio.anno --> disponibilitaLiquidare = 0
			if(siacTMovgestTs.getSiacTMovgest()!=null && siacTMovgestTsImpegno.getSiacTMovgest().getMovgestAnno() > Integer.valueOf(siacTMovgestTsImpegno.getSiacTMovgest().getSiacTBil().getSiacTPeriodo().getAnno())){
				return new DisponibilitaMovimentoGestioneContainer(BigDecimal.ZERO, "Se l'anno dell'impegno e' maggiore dell'anno di bilancio la disponibilita' deve essere ZERO");
			}
			
			// CR -1965
			// Se Impegno.validato = FALSE disponibilita liquidare = 0
			if(siacTMovgestTs.getSiacTMovgest()!=null && !siacTMovgestTsImpegno.getSiacTMovgest().getParereFinanziario()){
				return new DisponibilitaMovimentoGestioneContainer(BigDecimal.ZERO, "Se l'impegno non e' validato (flag parere finanziario non a TRUE) la disponibilita' deve essere ZERO");
			}
			
			// Jira: 1896: i vincoli si devono ricercare sempre sull'impegno, in caso di sub venivano ricercati erroneamente sul subImpegno
			List<VincoloImpegno> vincoli = getAccertamentiVincolati(siacTMovgestTsImpegno);
			//log.debug("calcolaDisponibilitaALiquidare", " vincoli per impegno (" + siacTMovgestTsImpegno.getUid() +") " + (!vincoli.isEmpty()? vincoli.size() : " nessun vincolo! "));
			
			BigDecimal disponibilitaVincolare = calcolaDisponibilitaAVincolareImpegno(idMovGestTsImpegno, datiOperazioneDto);
			
			
			if(vincoli!=null && vincoli.size()>0){
				// (2) SE e' parzialmente vincolato  (disponibilitaVincolare >0 e disponibilitaVincolare <  importoAttuale)
				if(disponibilitaVincolare.compareTo(BigDecimal.ZERO)>0 && disponibilitaVincolare.compareTo(importoAttuale)<0){
					return new DisponibilitaMovimentoGestioneContainer(BigDecimal.ZERO,
							"Se l'impegno e' parzialmente vincolato (disponibilitaVincolare > 0 e disponibilitaVincolare < importoAttuale. Nel caso attuale disponibilitaVincolare: " + disponibilitaVincolare.toPlainString()
							+ " e importoAttuale: " + importoAttuale.toPlainString() + ") la disponibilita' deve essere ZERO");
				}
			}else{
				//(3)	SE non e' vincolato  ed e' vera una delle 2 condizioni seguenti
				//		a.	legato ad un Progetto rilevante fondo (disponibilitaVincolare >0 e Progetto.RilevanteFPV) 
				//		b.	leegato ad un Capitolo che un vincolo di trasferimento  (disponibilitaVincolare >0 e esiste almeno Capitolo.ListaVincoli.Vincolo.flagTrasferimpentiVincolati  = TRUE
				boolean progettoRilevante = isLegatoAProgettoVincolante(siacTMovgestTsImpegno, datiOperazioneDto);
				
				if(disponibilitaVincolare.compareTo(BigDecimal.ZERO)>0 && progettoRilevante){
					//a.	legato ad un Progetto rilevante fondo (disponibilitaVincolare >0 e Progetto.RilevanteFPV)
					return new DisponibilitaMovimentoGestioneContainer(BigDecimal.ZERO,
							"Se l'impegno e' residuo e legato a un progetto rilevante fondo (disponibilitaVincolare > 0 e Progetto.RilevanteFPV = TRUE. Nel caso attuale disponibilitaVincolare: " + disponibilitaVincolare.toPlainString()
							+ ") la disponibilita' deve essere ZERO");
				}
				boolean trasferimentiVincolati = flagTrasferimentiVincolati(siacTMovgestImpegno);
				//b.	leegato ad un Capitolo che un vincolo di trasferimento  
				//(disponibilitaVincolare >0 e esiste almeno Capitolo.ListaVincoli.Vincolo.flagTrasferimpentiVincolati  = TRUE
				if(disponibilitaVincolare.compareTo(BigDecimal.ZERO)>0 && trasferimentiVincolati){
					return new DisponibilitaMovimentoGestioneContainer(BigDecimal.ZERO,
							"Se l'impegno e' residuo e legato ad un Capitolo con un vincolo di trasferimento (disponibilitaVincolare > 0 ed esiste almeno Capitolo.ListaVincoli.Vincolo.flagTrasferimpentiVincolati = TRUE. Nel caso attuale disponibilitaVincolare: " + disponibilitaVincolare.toPlainString()
							+ ") la disponibilita' deve essere ZERO");
				}
			}
			//}
			
			//(4)	ALTRIMENTI 	= Impegno.importoAttuale - sommaLiquidazioniDoc
			//nuovo calcolo con function db:
			BigDecimal disp = impegnoDao.calcolaDisponibilitaALiquidare(idMovGestTs);
			return new DisponibilitaMovimentoGestioneContainer(disp != null ? disp : BigDecimal.ZERO, "Disponibilita' calcolata dalla function");
		}
		return new DisponibilitaMovimentoGestioneContainer(BigDecimal.ZERO, "Movimento di gestione non fornito alla procedura Java");
	}
	
	/**
	 * Definito da RM perchè in fase di aggiornamento mancava il controllo sui vincoli
	 * 
	 * @param idSiacTMovgestTsImpegno
	 * @param datiOperazioneDto
	 * @return
	 */
	private Boolean checkEsistenzaVincoli(Integer idSiacTMovgestTsImpegno,DatiOperazioneDto datiOperazioneDto){
		
		SiacTMovgestTsFin siacTMovgestTsImpegno = siacTMovgestTsRepository.findOne(idSiacTMovgestTsImpegno);
		SiacTMovgestFin siacTMovgestImpegno = siacTMovgestTsImpegno.getSiacTMovgest();
		
		Boolean esistenzaVincoli = Boolean.TRUE;
		
		boolean progettoRilevante = isLegatoAProgettoVincolante(siacTMovgestTsImpegno, datiOperazioneDto);
		boolean trasferimentiVincolati = flagTrasferimentiVincolati(siacTMovgestImpegno);
		
		if(!progettoRilevante && !trasferimentiVincolati){
			esistenzaVincoli = Boolean.FALSE;
		}
		
		return esistenzaVincoli;

	}
	
	
	private BigDecimal calcolaDiffImportoAttualeUtilizzato(Integer idMovGestTs,Integer idEnte,DatiOperazioneDto datiOperazioneDto){
		BigDecimal disp = BigDecimal.ZERO;
		
		SiacTMovgestTsFin siacTMovgestTs = siacTMovgestTsRepository.findOne(idMovGestTs);
		
		if(siacTMovgestTs!=null){
			
			disp = impegnoDao.calcolaDisponibilitaALiquidare(idMovGestTs);
			
			if(null==disp){
				disp = BigDecimal.ZERO;
			}
		}
		return disp;
	}
	
	
	/**
	 * Dato un certo movimento va a verificare se e' legato
	 * a un progetto vincolante
	 * @param siacTMovgestTs
	 * @param datiOperazioneDto
	 * @return
	 */
	private boolean isLegatoAProgettoVincolante(SiacTMovgestTsFin siacTMovgestTs,DatiOperazioneDto datiOperazioneDto){
		SiacTProgrammaFin siacTProgramma = getProgetto(siacTMovgestTs, datiOperazioneDto);
		boolean progettoRilevante = false;
		if(siacTProgramma!=null){
			progettoRilevante = isRilevanteFPV(siacTProgramma);
		}
		return progettoRilevante;
	}
	
	/**
	 *  Dato un movimento va a verificare per il suo capitolo il flag sui trasferimenti vincolati
	 * @param siacTMovgest
	 * @return
	 */
	public boolean flagTrasferimentiVincolati(SiacTMovgestFin siacTMovgest){
		Integer idCapitoloUscitaGest = EntityToModelConverter.getChiaveCapitolo(siacTMovgest);
		boolean trasferimentiVincolati = movimentoGestioneDao.flagTrasferimentiVincolati(idCapitoloUscitaGest);
		return trasferimentiVincolati;
	}
	

	/**
	 * Dato un impegno ne calcola la disponibilita a vincolare
	 * @param idMovGestTs
	 * @param idEnte
	 * @param datiOperazioneDto
	 * @return
	 */
	private BigDecimal calcolaDisponibilitaAVincolareImpegno(Integer idMovGestTs,DatiOperazioneDto datiOperazioneDto){
		BigDecimal disponibilitaVincolare = BigDecimal.ZERO;
		//SE impegno.stato = Annullato 
		//disponibilitaVincolare = 0
		//ALTRIMENTI
		//disponibilitaVincolare =
		//importoAttuale  -  SOMMATORIA (Vincoli.importo)
		SiacTMovgestTsFin siacTMovgestTs = siacTMovgestTsRepository.findOne(idMovGestTs);
		if(siacTMovgestTs!=null){
			String statoCod = getStatoCode(siacTMovgestTs, datiOperazioneDto);
			if(CostantiFin.MOVGEST_STATO_ANNULLATO.equalsIgnoreCase(statoCod)){
				disponibilitaVincolare = BigDecimal.ZERO;
			} else {
				List<VincoloImpegno> vincoli = getAccertamentiVincolati(siacTMovgestTs);
				BigDecimal sommaVincoli = BigDecimal.ZERO;
				if(vincoli!=null && vincoli.size()>0){
					sommaVincoli = calcolaTotQuoteVincoliImp(vincoli);
				}
				BigDecimal importoAttuale = estraiImportoAttualeByMovgestTsId(idMovGestTs, datiOperazioneDto);
				disponibilitaVincolare = importoAttuale.subtract(sommaVincoli);
			}
		}
		return disponibilitaVincolare;
	}
	
	/**
	 * Si occupa di calcolare la disponibilita' in modifica per l'impegno indicato
	 * @param idMovGestTs
	 * @param datiOperazioneDto
	 * @return
	 */
	public DisponibilitaMovimentoGestioneContainer calcolaDisponibilitaImpegnoModifica(Integer idMovGestTs,DatiOperazioneDto datiOperazioneDto){
		//SE impegno. stato = DEFINITIVO 
		//= MIN Impegno.disponibilitaVincolare; ( Impegno.importoAttuale SOMMATORIA sommaLiquidazioniDoc)
		//SE impegno. stato = NON LIQUIDABILE
		//= MIN (Impegno.disponibilitaSubimpegnare;Impegno.disponibilitaVincolare)
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getUid();
		SiacTMovgestTsFin siacTMovgestTs = siacTMovgestTsRepository.findOne(idMovGestTs);
		if(siacTMovgestTs == null) {
			return new DisponibilitaMovimentoGestioneContainer(BigDecimal.ZERO, "Impegno non fornito alla prcedura Java");
		}
		String statoCod = getStatoCode(siacTMovgestTs, datiOperazioneDto);
		BigDecimal disponibilitaVincolare = calcolaDisponibilitaAVincolareImpegno(idMovGestTs, datiOperazioneDto);

		if(CostantiFin.MOVGEST_STATO_DEFINITIVO.equalsIgnoreCase(statoCod)){
			
			/// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			//	BigDecimal dispLiquidare = calcolaDisponibilitaALiquidare(idMovGestTs, idEnte, datiOperazioneDto);
			//  chiamata diretta ad un wrapper della function
			BigDecimal dispLiquidare = calcolaDiffImportoAttualeUtilizzato(idMovGestTs, idEnte, datiOperazioneDto);
			
			return new DisponibilitaMovimentoGestioneContainer(dispLiquidare.min(disponibilitaVincolare),
					"Se lo stato e' DEFINITIVO, la disponibilita' e' pari al minimo tra la differenza tra importo attuale e utilizzato (" + dispLiquidare
					+ ") e la disponibilita' a vincolare (" + disponibilitaVincolare + ")");

		}
		if(CostantiFin.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE.equalsIgnoreCase(statoCod)){
			
			BigDecimal disponibilitaSubimpegnare = calcolaDisponibilitaImpegnoASubImpegnare(siacTMovgestTs, datiOperazioneDto);
			return new DisponibilitaMovimentoGestioneContainer(disponibilitaSubimpegnare.min(disponibilitaVincolare),
					"Se lo stato e' DEFINITIVO, la disponibilita' e' pari al minimo tra la disponibilita' a subimpegnare (" + disponibilitaSubimpegnare
					+ ") e la disponibilita' a vincolare (" + disponibilitaVincolare + ")");
			
		}
		if(CostantiFin.MOVGEST_STATO_PROVVISORIO.equalsIgnoreCase(statoCod) && isSub(siacTMovgestTs)){
			//Se e' provvisorio ed e' un sub: disponibilitaModifica = calcolaDiffImportoAttualeUtilizzato (function disp liquidare)
			BigDecimal dispLiquidare = calcolaDiffImportoAttualeUtilizzato(idMovGestTs, idEnte, datiOperazioneDto);
			return new DisponibilitaMovimentoGestioneContainer(dispLiquidare, "Se lo stato e' PROVVISORIO, la disponibilita' e' pari alla differenza tra importo attuale e utilizzato");
		}
		return new DisponibilitaMovimentoGestioneContainer(BigDecimal.ZERO, "Disponibilita' non calcolata, pari a ZERO");
	}
	
	
	/**
	 * Dato un certo sub impegno ne ricerca le liquidazioni associate
	 * @param siacTMovgestTs
	 * @return
	 */
	private List<SiacTLiquidazioneFin> findLiquidazioniValideFromSubImpegno(SiacTMovgestTsFin siacTMovgestTs){
		List<SiacTLiquidazioneFin> elencoSiacTLiquidazione = new ArrayList<SiacTLiquidazioneFin>();
		List<SiacRLiquidazioneMovgestFin> elencoSiacRLiquidazioneMovgestSubImp = siacTMovgestTs.getSiacRLiquidazioneMovgests();
		if(elencoSiacRLiquidazioneMovgestSubImp!=null && elencoSiacRLiquidazioneMovgestSubImp.size()>0){
			for(SiacRLiquidazioneMovgestFin siacRLiquidazioneMovgest : elencoSiacRLiquidazioneMovgestSubImp){
				if(siacRLiquidazioneMovgest.getDataFineValidita()==null){
					//Se valido:
					// Controllo lo stato della liquidazione
					SiacTLiquidazioneFin siacTLiquidazione = siacRLiquidazioneMovgest.getSiacTLiquidazione();
					List<SiacRLiquidazioneStatoFin> elencoSiacRLiquidazioneStato = siacTLiquidazione.getSiacRLiquidazioneStatos();
					if(elencoSiacRLiquidazioneStato!=null && elencoSiacRLiquidazioneStato.size()>0){
						for(SiacRLiquidazioneStatoFin siacRLiquidazioneStato : elencoSiacRLiquidazioneStato){
							if(siacRLiquidazioneStato.getDataFineValidita()==null){
								//Se valido:
								if(siacRLiquidazioneStato.getSiacDLiquidazioneStato().getLiqStatoCode().equalsIgnoreCase(CostantiFin.LIQUIDAZIONE_STATO_VALIDO)){
									// la liquidazione � in stato valido
								}
							}
						}
					}
				}
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return elencoSiacTLiquidazione;
	}

	/**
	 * Dato un certo impegno calcola la disponibilita a subimpegnare e la restituisce in output.
	 * Inoltre setta dentro l'oggetto impegno ricevuto il totale sub impegni (definito come
	 * sommatoria degli importi attuali di tutti i subimpegni)
	 * @param impegno
	 * @return
	 */
	public DisponibilitaMovimentoGestioneContainer calcolaDisponibilitaImpegnoASubImpegnareEValorizzaTotaleSubImpegni(Impegno impegno){
		BigDecimal importoAttuale = BigDecimal.ZERO;
		BigDecimal disponibilitaSubimpegnare = BigDecimal.ZERO;
		BigDecimal sommatoriaImportoAttualeSubImpegni = BigDecimal.ZERO;
		if(null!=impegno){
			if(!CostantiFin.MOVGEST_STATO_DEFINITIVO.equalsIgnoreCase(impegno.getStatoOperativoMovimentoGestioneSpesa())){
				if(!CostantiFin.MOVGEST_STATO_ANNULLATO.equalsIgnoreCase(impegno.getStatoOperativoMovimentoGestioneSpesa())){
					List<SubImpegno> listaSubImpegni = new ArrayList<SubImpegno>();
					listaSubImpegni = impegno.getElencoSubImpegni();
					if(null!=listaSubImpegni && listaSubImpegni.size() > 0){
						for(SubImpegno subImpegno : listaSubImpegni){
							if(!CostantiFin.MOVGEST_STATO_ANNULLATO.equalsIgnoreCase(subImpegno.getStatoOperativoMovimentoGestioneSpesa())){
								sommatoriaImportoAttualeSubImpegni = sommatoriaImportoAttualeSubImpegni.add(subImpegno.getImportoAttuale());
							}
						}
					}
					disponibilitaSubimpegnare = impegno.getImportoAttuale().subtract(sommatoriaImportoAttualeSubImpegni); 
				}
			}
			impegno.setTotaleSubImpegniBigDecimal(sommatoriaImportoAttualeSubImpegni);
		}
		//Termino restituendo l'oggetto di ritorno: 
		return new DisponibilitaMovimentoGestioneContainer(disponibilitaSubimpegnare,
				"Disponibilita calcolata come differenza tra l'importo attuale (" + importoAttuale + ") e totale dei subimpegni (" + sommatoriaImportoAttualeSubImpegni + ")");
	}
	
	/**
	 * Dato un certo impegno calcola la disponibilita a subimpegnare e la restituisce in output.
	 * @param siacTMovgestTs
	 * @param datiOperazioneDto
	 * @return
	 */
	public BigDecimal calcolaDisponibilitaImpegnoASubImpegnare(SiacTMovgestTsFin siacTMovgestTs, DatiOperazioneDto datiOperazioneDto){
		BigDecimal disponibilitaSubimpegnare = BigDecimal.ZERO;
		BigDecimal sommatoriaImportoAttualeSubImpegni = BigDecimal.ZERO;
		if(null!=siacTMovgestTs){
			String statoCode = getStatoCode(siacTMovgestTs, datiOperazioneDto);
			
			if(!CostantiFin.MOVGEST_STATO_DEFINITIVO.equalsIgnoreCase(statoCode)){
				if(!CostantiFin.MOVGEST_STATO_ANNULLATO.equalsIgnoreCase(statoCode)){
					SiacTMovgestFin siacTMovgest = siacTMovgestTs.getSiacTMovgest();
					List<SiacTMovgestTsFin> listaTs = getTestataPiuListaSub(siacTMovgest);
					
					if(listaTs!=null && listaTs.size()>1){
						for(int i=1;i<listaTs.size();i++){
							SiacTMovgestTsFin subIt = listaTs.get(i);
							
							String statoCodeSub = getStatoCode(subIt, datiOperazioneDto);
							if(!statoCodeSub.equals(CostantiFin.MOVGEST_STATO_ANNULLATO)){
															
								BigDecimal impAttualeSubIt = estraiImportoAttualeByMovgestTsId(subIt.getMovgestTsId(), datiOperazioneDto);
								sommatoriaImportoAttualeSubImpegni = sommatoriaImportoAttualeSubImpegni.add(impAttualeSubIt);
								
							}
						}
					}
					BigDecimal importoAttuale = estraiImportoAttualeByMovgestTsId(siacTMovgestTs.getMovgestTsId(), datiOperazioneDto);
					disponibilitaSubimpegnare = importoAttuale.subtract(sommatoriaImportoAttualeSubImpegni); 
					
				}
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
		return disponibilitaSubimpegnare;
	}

	/**
	 * Dato un certo accertamento calcola la disponibilita a subaccertare e la restituisce in output.
	 * Inoltre setta dentro l'oggetto accertamento ricevuto il totale sub accertamenti (definito come
	 * sommatoria degli importi attuali di tutti i subaccertamenti)
	 * @param accertamento
	 * @return
	 */
	public DisponibilitaMovimentoGestioneContainer calcolaDisponibilitaAccertamentoASubAccertareEValorizzaTotaleSubAccertamenti(Accertamento accertamento){
		BigDecimal disponibilitaSubAccertare = BigDecimal.ZERO;
		BigDecimal sommatoriaImportoAttualeSubAccertamenti = BigDecimal.ZERO;
		if(null!=accertamento){
			if(!CostantiFin.MOVGEST_STATO_DEFINITIVO.equalsIgnoreCase(accertamento.getStatoOperativoMovimentoGestioneEntrata())){
				if(!CostantiFin.MOVGEST_STATO_ANNULLATO.equalsIgnoreCase(accertamento.getStatoOperativoMovimentoGestioneEntrata())){
					List<SubAccertamento> listaSubAccertamenti = new ArrayList<SubAccertamento>();
					listaSubAccertamenti = accertamento.getElencoSubAccertamenti();
					if(null!=listaSubAccertamenti && listaSubAccertamenti.size() > 0){
						for(SubAccertamento subAccertamento : listaSubAccertamenti){
							if(!CostantiFin.MOVGEST_STATO_ANNULLATO.equalsIgnoreCase(subAccertamento.getStatoOperativoMovimentoGestioneEntrata())){
								sommatoriaImportoAttualeSubAccertamenti = sommatoriaImportoAttualeSubAccertamenti.add(subAccertamento.getImportoAttuale());
							}
						}
					}
					disponibilitaSubAccertare = accertamento.getImportoAttuale().subtract(sommatoriaImportoAttualeSubAccertamenti); 
				}
			}
			accertamento.setTotaleSubAccertamentiBigDecimal(sommatoriaImportoAttualeSubAccertamenti);
		}
		//Termino restituendo l'oggetto di ritorno: 
		return new DisponibilitaMovimentoGestioneContainer(disponibilitaSubAccertare, "Disponibilita' calcolata come differenza tra importo attuale (" + accertamento.getImportoAttuale()
    		+ ") e totale delle disponibilita' dei subaccertamenti non annullati (" + sommatoriaImportoAttualeSubAccertamenti + ")");
	}
	
	/**
	 * Va a leggere e restituisce l'importo attuale di un movimento indicato.
	 * Riceve l'id di siat_t_movgest
	 * @param movgestId
	 * @param datiOperazioneDto
	 * @return
	 */
	public BigDecimal estraiImportoAttualeByMovgestId(Integer movgestId, DatiOperazioneDto datiOperazioneDto){		
		BigDecimal importoAttualeAccertamento = BigDecimal.ZERO;
		Timestamp now = getNow();
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		SiacTMovgestTsFin siacTMovgestTs = siacTMovgestTsRepository.findMovgestTsByMovgest(idEnte, now, movgestId).get(0);
		importoAttualeAccertamento = estraiImportoAttualeByMovgestTsId(siacTMovgestTs.getUid(), datiOperazioneDto);
		//Termino restituendo l'oggetto di ritorno: 
        return importoAttualeAccertamento;
	}
	
	/**
	 * Va a leggere e restituisce l'importo attuale di un movimento indicato.
	 * Riceve l'id di siat_t_movgest_ts
	 * @param movgestTsId
	 * @param datiOperazioneDto
	 * @return
	 */
	public BigDecimal estraiImportoAttualeByMovgestTsId(Integer movgestTsId, DatiOperazioneDto datiOperazioneDto){		
		BigDecimal importoAttuale = BigDecimal.ZERO;
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		importoAttuale = siacTMovgestTsDetRepository.findImporto(idEnte, CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE, movgestTsId);
		return importoAttuale;
	}
	
	/**
	 * Va a leggere e restituisce l'importo attuale di un accertamento indicato.
	 * @param movgestTsIdAccertamento
	 * @param datiOperazioneDto
	 * @return
	 */
	public BigDecimal estraiImportoUtilizzabileByMovgestTsId(Integer movgestTsIdAccertamento, DatiOperazioneDto datiOperazioneDto){		
		BigDecimal importoUtilizzabileAccertamento = BigDecimal.ZERO;
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		importoUtilizzabileAccertamento = siacTMovgestTsDetRepository.findImporto(idEnte, CostantiFin.MOVGEST_TS_DET_TIPO_UTILIZZABILE, movgestTsIdAccertamento);
		if(importoUtilizzabileAccertamento==null){
			importoUtilizzabileAccertamento = BigDecimal.ZERO;
		}
		//Termino restituendo l'oggetto di ritorno: 
        return importoUtilizzabileAccertamento;
	}
	
	/**
	 * Dato un certo accertamento restituisce la disponibilta a subaccertare
	 * @param movgestId
	 * @param datiOperazioneDto
	 * @return
	 */
	public BigDecimal calcolaDisponibilitaASubAccertare(Integer movgestId, DatiOperazioneDto datiOperazioneDto){		
		Timestamp now = getNow();
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		
		SiacTMovgestTsFin siacTMovgestTs = siacTMovgestTsRepository.findMovgestTsByMovgest(idEnte, now,movgestId).get(0);
		
        return calcolaDisponibilitaASubAccertare(siacTMovgestTs, datiOperazioneDto);
	}
	
	/**
	 * Dato un certo accertamento restituisce la disponibilta a subaccertare
	 * @param siacTMovgestTs
	 * @param datiOperazioneDto
	 * @return
	 */
	public BigDecimal calcolaDisponibilitaASubAccertare(SiacTMovgestTsFin siacTMovgestTs, DatiOperazioneDto datiOperazioneDto){		
		BigDecimal disponibilitaAPagare = BigDecimal.ZERO;
		Timestamp now = getNow();
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		
		String statoCod = getStatoCode(siacTMovgestTs, datiOperazioneDto);
		
		if(!StringUtilsFin.isEmpty(statoCod) && !statoCod.equals(CostantiFin.MOVGEST_STATO_ANNULLATO)){
			BigDecimal importoAttualeAccertamento = siacTMovgestTsDetRepository.findImporto(idEnte, CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE, siacTMovgestTs.getUid());
			
			BigDecimal totaleSubOrdinativi = BigDecimal.ZERO;
			List<SiacTMovgestTsFin> subAccertamenti = siacTMovgestTsRepository.findListaSiacTMovgestTsFigli(idEnte, now, siacTMovgestTs.getMovgestTsId());
			subAccertamenti = DatiOperazioneUtil.soloValidi(subAccertamenti, now);
			
			if(subAccertamenti!=null && subAccertamenti.size()>0){
				for(SiacTMovgestTsFin subAccIt : subAccertamenti){
					BigDecimal importoAttualeSubAcc = siacTMovgestTsDetRepository.findImporto(idEnte, CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE, subAccIt.getUid());
					totaleSubOrdinativi = totaleSubOrdinativi.add(importoAttualeSubAcc);
				}
			}
			disponibilitaAPagare = importoAttualeAccertamento.subtract(totaleSubOrdinativi);
		}else{
			disponibilitaAPagare = BigDecimal.ZERO;
		}
		//Termino restituendo l'oggetto di ritorno: 
        return disponibilitaAPagare;
	}

	/**
	 * Routine principale di ricerca impegni utilizzata dal servizio di ricerca impegni
	 * @param richiedente
	 * @param idEnte
	 * @param prs
	 * @return
	 */
	public List<Impegno> ricercaImpegni(Richiedente richiedente ,String codiceAmbito,Integer idEnte, ParametroRicercaImpegno prs){
		List<Impegno> impegnoList = new ArrayList<Impegno>();
		List<Impegno> impegniListApp = new ArrayList<Impegno>();
		List<SiacTMovgestFin> dtos = new ArrayList<SiacTMovgestFin>();
		//viene cambiato oggetto perche' i model non possono essere passati ai DaoImpl (specifica CSI)
		RicercaImpegnoParamDto paramSearch = (RicercaImpegnoParamDto) map(prs, RicercaImpegnoParamDto.class);
		///
		dtos = impegnoDao.ricercaImpegni(idEnte, paramSearch);
		if(dtos!= null && dtos.size() != 0){
			impegniListApp = convertiLista(dtos, Impegno.class, FinMapId.SiacTMovgest_Impegno);
			impegniListApp = EntityToModelConverter.siacTMovgestEntityToImpegnoModelPerRicerca(dtos, impegniListApp);
			for(Impegno impegnoApp : impegniListApp){
				Impegno impegnoToInsert = new Impegno();
				impegnoToInsert =  impegnoApp;
				//Gestione Soggetto
				if(!StringUtilsFin.isEmpty(impegnoApp.getSoggettoCode())){
					Soggetto soggetto = new Soggetto();
					SiacTSoggettoFin siacTSoggetto = siacTSoggettoRepository.ricercaSoggettoNoSeSede(codiceAmbito, idEnte, impegnoApp.getSoggettoCode(),CostantiFin.SEDE_SECONDARIA,getNow());
					soggetto = map(siacTSoggetto, Soggetto.class, FinMapId.SiacTSoggetto_Soggetto);
					impegnoToInsert.setSoggetto(soggetto);
				}
				//Inserisco
				impegnoList.add(impegnoToInsert);
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return impegnoList;
	}
	
	/**
	 * Routine principale di ricerca accertamenti utilizzata dal servizio di ricerca impegni
	 * @param richiedente
	 * @param idEnte
	 * @param prs
	 * @return
	 */
	public List<Accertamento> ricercaAccertamenti(Richiedente richiedente ,Integer idEnte, RicercaAccertamento prs){
		
		List<Accertamento> accertamentoList = new ArrayList<Accertamento>();
		List<Accertamento> accertamentoListApp = new ArrayList<Accertamento>();
		List<SiacTMovgestFin> dtos = new ArrayList<SiacTMovgestFin>();
		
		//viene cambiato oggetto perche' i model non possono essere passati ai DaoImpl (specifica CSI)
		RicercaAccertamentoParamDto paramSearch = (RicercaAccertamentoParamDto) map(prs, RicercaAccertamentoParamDto.class);
		
		dtos = accertamentoDao.ricercaAccertamenti(idEnte, paramSearch);
		if(dtos!= null && dtos.size() != 0){
			accertamentoListApp = convertiLista(dtos, Accertamento.class, FinMapId.SiacTMovgest_Accertamento);
			accertamentoListApp = EntityToModelConverter.siacTMovgestEntityToAccertamentoModelPerRicerca(dtos, accertamentoListApp);
			
			for(Accertamento accertamentoApp : accertamentoListApp){
				Accertamento accertamentoToInsert = new Accertamento();
				accertamentoToInsert =  accertamentoApp;
				//Gestione Soggetto
				if(!StringUtilsFin.isEmpty(accertamentoApp.getSoggettoCode())){
					Soggetto soggetto = new Soggetto();
					SiacTSoggettoFin siacTSoggetto = siacTSoggettoRepository.ricercaSoggettoNoSeSede(CostantiFin.AMBITO_FIN, idEnte, accertamentoApp.getSoggettoCode(),CostantiFin.SEDE_SECONDARIA, getNow());
					
					soggetto = map(siacTSoggetto, Soggetto.class, FinMapId.SiacTSoggetto_Soggetto);	
					
					accertamentoToInsert.setSoggetto(soggetto);
				}
				//Inserisco
				accertamentoList.add(accertamentoToInsert);
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return accertamentoList;
	}

	/**
	 * Carica il soggetto dato un certo movimento, riceve un MovimentoGestione e restituisce un SiacTSoggettoFin
	 * @param impegno
	 * @param datiOperazioneDto
	 * @return
	 */
	private SiacTSoggettoFin getSoggetto(MovimentoGestione impegno,DatiOperazioneDto datiOperazioneDto){
		SiacTSoggettoFin siacTSoggetto = null;
		if(impegno!=null && impegno.getSoggetto()!=null){
			Soggetto soggetto = impegno.getSoggetto();
			if(soggetto!=null && !StringUtilsFin.isEmpty(soggetto.getCodiceSoggetto())){
				Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
				Integer idAmbito = datiOperazioneDto.getSiacDAmbito().getAmbitoId();
				String codeSogg = soggetto.getCodiceSoggetto();
				List<SiacTSoggettoFin> l = siacTSoggettoRepository.findSoggettoByCodeAndAmbitoAndEnte(codeSogg, idEnte, datiOperazioneDto.getTs(), idAmbito, CostantiFin.SEDE_SECONDARIA);
				if(l!=null && l.size()>0){
					siacTSoggetto = l.get(0);
				}
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return siacTSoggetto;
	}
	
	/**
	 * Dato un certo movimento in input recupera e restituisce il relativo SiacTProgrammaFin
	 * @param impegno
	 * @param datiOperazioneDto
	 * @return
	 */
	private SiacTProgrammaFin getProgetto(MovimentoGestione impegno,DatiOperazioneDto datiOperazioneDto){
		// Si chiama progetto ma viene memorizzato sulla tabella siac_t_programma
		// Al momento non � cos� ma gestisco comunque con instanceof nel caso in cui in futuro
		// ci possano essere dei progetti / programmi distinti per impegno / accertamento
		
		Progetto progetto = ((Impegno) impegno).getProgetto();
		if(progetto!=null && !StringUtilsFin.isEmpty(progetto.getCodice())){
			
			Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
			
			return siacTProgrammaRepository.findProgrammaByCodiceAndStatoOperativoAndEnteProprietarioId(
					progetto.getCodice(), 
					StatoOperativoProgetto.VALIDO.getCodice(),
					progetto.getTipoProgetto().getCodice(),
					progetto.getBilancio().getUid(),
					idEnte);
		}

        return null;
	}
	
	/**
	 * Dato un certo movimento in input recupera e restituisce il relativo SiacTProgrammaFin
	 * @param siacTMovgestTs
	 * @param datiOperazioneDto
	 * @return
	 */
	private SiacTProgrammaFin getProgetto(SiacTMovgestTsFin siacTMovgestTs, DatiOperazioneDto datiOperazioneDto){
		SiacTProgrammaFin siacTProgramma = null;
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getUid();
		List<SiacRMovgestTsProgrammaFin> l = siacRMovgestTsProgrammaRepository.findByMovgestTs(idEnte, datiOperazioneDto.getTs(), siacTMovgestTs.getMovgestTsId());
		if(l!=null && l.size()>0){
			//assicuriamoci che sia valido:
			l = DatiOperazioneUtil.soloValidi(l, getNow());
			SiacRMovgestTsProgrammaFin legameValido = DatiOperazioneUtil.getValido(l, getNow());
			if(legameValido!=null){
				siacTProgramma = legameValido.getSiacTProgramma();
			}
		}
		return siacTProgramma;
	}
	
	/**
	 * Dato un certo movimento in input recupera e restituisce il relativo SiacDSoggettoClasseFin
	 * @param impegno
	 * @param datiOperazioneDto
	 * @return
	 */
	private SiacDSoggettoClasseFin getSoggettoClasse(MovimentoGestione impegno,DatiOperazioneDto datiOperazioneDto){
		SiacDSoggettoClasseFin siacDSoggettoClasse = null;
		ClasseSoggetto classeSoggetto = impegno.getClasseSoggetto();
		if(classeSoggetto!=null && !StringUtilsFin.isEmpty(classeSoggetto.getCodice())){
			Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
			Integer idAmbito = datiOperazioneDto.getSiacDAmbito().getAmbitoId();
			String code = classeSoggetto.getCodice();
			List<SiacDSoggettoClasseFin> l = siacDSoggettoClasseRepository.findByCodeAndAmbitoAndEnte(code, idEnte, datiOperazioneDto.getTs(), idAmbito);
			if(l!=null && l.size()>0){
				siacDSoggettoClasse = l.get(0);
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return siacDSoggettoClasse;
	}
	
	/**
	 * Dato un certo movimento in input recupera e restituisce il relativo SiacTAttoAmmFin
	 * @param impegno
	 * @param idEnte
	 * @return
	 */
	private SiacTAttoAmmFin getAttoAmministrativo(MovimentoGestione impegno,int idEnte){
		SiacTAttoAmmFin attoTrovato = null;
		if(impegno!=null && impegno.getAttoAmministrativo()!=null){
			//Invoco la routine di ricerca dell'atto:
			attoTrovato = getSiacTAttoAmmFromAttoAmministrativo(impegno.getAttoAmministrativo(), idEnte);
		}
		//Termino restituendo l'oggetto di ritorno: 
        return attoTrovato;
	}

	/**
	 * Dato un certo movimento in input recupera e restituisce il relativo SiacTBilElemFin
	 * @param impegno
	 * @param idEnte
	 * @return
	 */
	private SiacTBilElemFin getCapitoloUscitaGestione(Impegno impegno,int idEnte){
		SiacTBilElemFin capitoloTrovato = null;
		Timestamp now = new Timestamp(System.currentTimeMillis());
		if(impegno!=null && impegno.getCapitoloUscitaGestione()!=null){
			if(impegno.getCapitoloUscitaGestione().getNumeroArticolo()!=null && 
					impegno.getCapitoloUscitaGestione().getNumeroCapitolo()!=null && 
					impegno.getCapitoloUscitaGestione().getNumeroUEB()!=null){
				String codeArticolo = impegno.getCapitoloUscitaGestione().getNumeroArticolo().toString();
				String codeCapitolo =  impegno.getCapitoloUscitaGestione().getNumeroCapitolo().toString();
				String codeUeb = impegno.getCapitoloUscitaGestione().getNumeroUEB().toString();;
				String annoCap = impegno.getCapitoloUscitaGestione().getAnnoCapitolo().toString();
				List<SiacTBilElemFin> siacTBilElems = siacTBilElemRepository.getValidoByCodes(idEnte, annoCap, now, codeCapitolo, codeArticolo, codeUeb, CostantiFin.D_BIL_ELEM_TIPO_ELEM_TIPO_CODE_CAP_UG);
				if(siacTBilElems!=null && siacTBilElems.size()>0){
					capitoloTrovato = siacTBilElems.get(0);
				}
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return capitoloTrovato;
	}
	
	/**
	 * Dato un certo accertamento in input recupera e restituisce il relativo SiacTBilElemFin
	 * @param accertamento
	 * @param idEnte
	 * @return
	 */
	private SiacTBilElemFin getCapitoloEntrataGestione(Accertamento accertamento, int idEnte){
		SiacTBilElemFin capitoloTrovato = null;
		Timestamp now = new Timestamp(System.currentTimeMillis());
		if(accertamento!=null && accertamento.getCapitoloEntrataGestione()!=null){
			if(accertamento.getCapitoloEntrataGestione().getNumeroArticolo()!=null && 
			   accertamento.getCapitoloEntrataGestione().getNumeroCapitolo()!=null && 
			   accertamento.getCapitoloEntrataGestione().getNumeroUEB()!=null){
				String codeArticolo = accertamento.getCapitoloEntrataGestione().getNumeroArticolo().toString();
				String codeCapitolo = accertamento.getCapitoloEntrataGestione().getNumeroCapitolo().toString();
				String codeUeb = accertamento.getCapitoloEntrataGestione().getNumeroUEB().toString();;
				String annoCap = accertamento.getCapitoloEntrataGestione().getAnnoCapitolo().toString();
				List<SiacTBilElemFin> siacTBilElems = siacTBilElemRepository.getValidoByCodes(idEnte, annoCap, now, codeCapitolo, codeArticolo, codeUeb, CostantiFin.D_BIL_ELEM_TIPO_ELEM_TIPO_CODE_CAP_EG);
				if(siacTBilElems!=null && siacTBilElems.size()>0){
					capitoloTrovato = siacTBilElems.get(0);
				}
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return capitoloTrovato;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * IMPLEMENTARE IN QUESTO METODO I VARI CONTROLLI PREVISTI AL PASSO "3.	Verifica dati trasmessi"
	 * DEL CAPITOLO "2.4.4	Operazione: Aggiorna Impegni"
	 * @param richiedente
	 * @param ente
	 * @param bilancio
	 * @param impegno
	 * @param datiOperazione
	 * @param impegnoInModificaInfoDto 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public EsitoControlliAggiornamentoImpegnoDto controlliDiMeritoAggiornamentoImpegno(Richiedente richiedente, Ente ente, Bilancio bilancio, Impegno impegno,DatiOperazioneDto datiOperazione, ImpegnoInModificaInfoDto impegnoInModificaInfoDto){
		EsitoControlliAggiornamentoImpegnoDto esito = new EsitoControlliAggiornamentoImpegnoDto();
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		SiacTMovgestTsFin siacTMovgestTs = impegnoInModificaInfoDto.getSiacTMovgestTs();
		
		String statoCod = getStatoCode(siacTMovgestTs, datiOperazione);
		//carico l'importo attuale:
		BigDecimal importoAttualeOld = estraiImportoAttualeByMovgestTsId(siacTMovgestTs.getMovgestTsId(), datiOperazione);
		BigDecimal nuovoImportoAttuale = impegno.getImportoAttuale();
		BigDecimal variazioneImportoAttuale = nuovoImportoAttuale.subtract(importoAttualeOld);
		BigDecimal importoUtilizzabileOld = estraiImportoUtilizzabileByMovgestTsId(siacTMovgestTs.getMovgestTsId(), datiOperazione);
		BigDecimal importoUtilizzabileNew = importoUtilizzabileOld.add(variazioneImportoAttuale);
		
		Integer annoMovimento = siacTMovgestTs.getSiacTMovgest().getMovgestAnno();
		
		String statoCodeNew = impegno.getStato().toString();
		statoCodeNew = CostantiFin.convertiStatoMovgest(statoCodeNew);
		if(statoCodeNew==null){
			//in caso di stato indicato non riconosciuto impostiamo come se non ci siano cambiamenti
			statoCodeNew = statoCod;
		}
		
		//STATO OPERATIVO: verificare la coerenza di passaggio di stato con  le regole descritte ai par. 2.5.4 e 2.5.5.
		//In caso contrario inviare il messaggio <COR_ERR_0010. Valore non valido
		//(nome parametro = STATO, messaggio aggiuntivo= non è prevista la transazione di stato>
		boolean controlloCambioStato = controlloCambioStatoImpegnoAccertamento(statoCod,statoCodeNew);
		if(!controlloCambioStato){
			listaErrori.add(ErroreCore.TRANSAZIONE_DI_STATO_NON_POSSIBILE.getErrore("Stato","non è prevista la transazione di stato"));
			esito.setListaErrori(listaErrori);
			return esito;
		}
		
		/////////////////////////////////////////////////////////////
		
		
		//IMPORTO:
		//Controllo Stato/Importo: l'importo e' modificabile solo se STATO IMPEGNO = PROVVISIORIO,
		//in caso contrario deve essere emesso l'errore:
		//<FIN_ERR_0030: Modifica Importo Impegno con stato impegno non provvisorio>
		if(!CostantiFin.MOVGEST_STATO_PROVVISORIO.equalsIgnoreCase(statoCod) && importoAttualeOld.compareTo(nuovoImportoAttuale)!=0){
			//prima di dire che e' non modificabile, occore valutare se puo' esserlo per predispozione consuntivo
			boolean modificabilePerPredisposizioneConsuntivo = modificabilePerPredisposizioneConsuntivo(annoMovimento, datiOperazione, bilancio);
			if(!modificabilePerPredisposizioneConsuntivo){
				listaErrori.add(ErroreFin.MODIFICA_IMPORTO_IMPEGNO_CON_STATO_PROVV.getErrore());
				esito.setListaErrori(listaErrori);
				return esito;
			}
		}
		
		
		//SUBIMPEGNI
		//Se sono presenti subimpegni effettuare i controlli descritti di seguito.

		//o	IMPORTO: da considerare solo gli importi dei subimpegni NON  ANNULLATI
		//o	Controllo Stato/Importo: L'importo è modificabile solo se STATO SUBIMPEGNO = PROVVISIORIO,
		//in caso contrario deve essere emesso l'errore:
		//<FIN_ERR_0030: Modifica Importo IMPEGNO con stato impegno non provvisorio>

		SubMovgestInModificaInfoDto infoSub = impegnoInModificaInfoDto.getInfoSubValutati();
		if(infoSub!=null){
			ArrayList<SubImpegno> subModificati = infoSub.getSubImpegniDaModificare();
			List<SiacTMovgestTsFin> subsSuDb = infoSub.getSubImpegniOld();
			
			if(subModificati!=null && subModificati.size()>0){
				for(SubImpegno subIt : subModificati){
					SiacTMovgestTsFin siacTMovgestTsSub = DatiOperazioneUtil.getById(subsSuDb, subIt.getUid());
					String statoSub = getStatoCode(siacTMovgestTsSub, datiOperazione);
					if(!CostantiFin.MOVGEST_STATO_PROVVISORIO.equals(statoSub) && !CostantiFin.MOVGEST_STATO_ANNULLATO.equals(statoSub)){
						BigDecimal nuovoImpSub = subIt.getImportoAttuale();
						BigDecimal oldImpSub = estraiImportoAttualeByMovgestTsId(subIt.getUid(), datiOperazione);
						if(oldImpSub.compareTo(nuovoImpSub)!=0){
							//importo modificato..
							boolean importoModificabile = true;
							if(CostantiFin.MOVGEST_STATO_ANNULLATO.equals(statoSub)){
								importoModificabile = false;
							} else {
								Integer annoSubMovimento = siacTMovgestTsSub.getSiacTMovgest().getMovgestAnno();
								boolean modificabilePerPredisposizioneConsuntivo = modificabilePerPredisposizioneConsuntivo(annoSubMovimento, datiOperazione, bilancio);
								importoModificabile = modificabilePerPredisposizioneConsuntivo;
							}
							if(!importoModificabile){
								listaErrori.add(ErroreFin.MODIFICA_IMPORTO_ACCERTAMENTO_CON_STATO_PROVV.getErrore());
								esito.setListaErrori(listaErrori);
								return esito;
							}
							
						}
					}
				}
			}
		}
		
		
        return esito;
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	private boolean presenzaModificheImportoValide(SiacTMovgestTsFin siacTMovgest){
		boolean presenzaModificheValide = false;
		//DISTINTI SIAC_T_MOVGEST_TS_DET_MOD_FIN:
		List<SiacTMovgestTsDetModFin> distintiSiacTMovgestTsDetModFinCoinvolti =movimentoGestioneDao.ricercaByMovGestTsMassive(toList(siacTMovgest), "SiacTMovgestTsDetModFin");
		//
		if(!StringUtilsFin.isEmpty(distintiSiacTMovgestTsDetModFinCoinvolti)){
			
			for(SiacTMovgestTsDetModFin it :distintiSiacTMovgestTsDetModFinCoinvolti){
				
				SiacRModificaStatoFin rStato = it.getSiacRModificaStato();
				
				if(CommonUtil.isValidoSiacTBase(rStato,null)){
					
					SiacDModificaStatoFin dstato = rStato.getSiacDModificaStato();
					
					if(CostantiFin.D_MODIFICA_STATO_VALIDO.equalsIgnoreCase(dstato.getModStatoCode())){
						return true;//trovato almeno una modifica valida
					}
					
				}
				
			}
			
		}
		return presenzaModificheValide;
	}
	
	
	private boolean isResiduo(Integer annoImpegno, Bilancio bilancio){
		boolean residuo = false;
		if(annoImpegno.compareTo(bilancio.getAnno())<0){
			residuo = true;
		}
		return residuo;
	}
	
	private boolean modificabilePerPredisposizioneConsuntivo(Integer annoImpegno, DatiOperazioneDto datiOperazione, Bilancio bilancio){
		boolean modificabilePerPredisposizioneConsuntivo = false;
		int annoBilancio = bilancio.getAnno();
		if(annoImpegno.compareTo(bilancio.getAnno())<0){
			String codiceFaseBilancioPrecedente = caricaCodiceBilancio(datiOperazione, annoBilancio-1);
			if (!StringUtilsFin.isEmpty(codiceFaseBilancioPrecedente) && CostantiFin.BIL_FASE_OPERATIVA_PREDISPOSIZIONE_CONSUNTIVO.equals(codiceFaseBilancioPrecedente)) {
				modificabilePerPredisposizioneConsuntivo = true;
			}
		}
		return modificabilePerPredisposizioneConsuntivo;
	}
	
	/**
	 * IMPLEMENTARE IN QUESTO METODO I VARI CONTROLLI PREVISTI AL PASSO "3.	Verifica dati trasmessi"
	 * DEL CAPITOLO 2.4.4	Operazione: Aggiorna Accertamenti
	 * @param richiedente
	 * @param ente
	 * @param bilancio
	 * @param accertamento
	 * @param datiOperazione
	 * @param impegnoInModificaInfoDto
	 * @return
	 */
	public EsitoControlliAggiornamentoAccertamentoDto controlliDiMeritoAggiornamentoAccertamento(Richiedente richiedente, Ente ente, Bilancio bilancio, Accertamento accertamento,DatiOperazioneDto datiOperazione, ImpegnoInModificaInfoDto impegnoInModificaInfoDto){
		
		EsitoControlliAggiornamentoAccertamentoDto esito = new EsitoControlliAggiornamentoAccertamentoDto();
		
		List<Errore> listaErrori = new ArrayList<Errore>();
		//in questo sotto metodo vanno i controli relativi all'impegno in aggiornamento:
		
		
		//TODO: QUESTO CONTROLLO NECESSITA DI PASSARE DA ORDINATIVO DAD
		//TODO: CAPIRE SE VA ESEGUITO, HA SENSO QUESTO PASSAGGIO DI STATO?
//		Se l'accertamento in fase di aggiornamento passa da stato definitivo o N (definitivo non liquidabile) a provvisorio, 
//		il sistema verifica che non esistano ordinativi di incasso VALIDO emessi a fronte dell�accertamento stesso 
//		o di suoi subaccertamenti. 
//		A tale scopo può essere richiamato il metodo Ricerca sintetica Ordinativo di Incasso del servizio 
//		Gestione Ordinativo di Incasso , passando l'anno di bilancio ricevuto in input, gli estremi dell'accertamento
//		in oggetto e la lista degli stati operativi dell�ordinativo di incasso valorizzata con tutti gli stati ammessi
//		ad eccezione di ANNULLATO.  Se vi sono incassi associati restituisce il messaggio di errore 
//		<FIN_ERR_0004- Esistono movimenti collegati>
		
		SiacTMovgestTsFin siacTMovgestTs = impegnoInModificaInfoDto.getSiacTMovgestTs();
		String statoCod = getStatoCode(siacTMovgestTs, datiOperazione);
		//carico l'importo attuale:
		BigDecimal importoAttualeOld = estraiImportoAttualeByMovgestTsId(siacTMovgestTs.getMovgestTsId(), datiOperazione);
		BigDecimal nuovoImportoAttuale = accertamento.getImportoAttuale();
		BigDecimal variazioneImportoAttuale = nuovoImportoAttuale.subtract(importoAttualeOld);
		BigDecimal importoUtilizzabileOld = estraiImportoUtilizzabileByMovgestTsId(siacTMovgestTs.getMovgestTsId(), datiOperazione);
		BigDecimal importoUtilizzabileNew = importoUtilizzabileOld.add(variazioneImportoAttuale);
		
		Integer annoMovimento = siacTMovgestTs.getSiacTMovgest().getMovgestAnno();
		
		boolean modificabilePerPredisposizioneConsuntivo = modificabilePerPredisposizioneConsuntivo(annoMovimento, datiOperazione, bilancio);
		
		//settiamo l'importo utilizzabile new nell'oggetto di ritorno per poterlo successivamente propagare all'operazione 
		//interna di aggiornamento dell'accertamento
		
		//JIRA SIAC-2813: si introduce la possibilita' di modificare manualmente l'importo utilizzabile
		//per accertamenti residui AND anno precedente in predisposizione consuntivo:
		if(modificabilePerPredisposizioneConsuntivo && accertamento.getImportoUtilizzabile()!=null){
			esito.setImportoUtilizzabileNew(accertamento.getImportoUtilizzabile());
		} else {
			esito.setImportoUtilizzabileNew(importoUtilizzabileNew);
		}
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		String statoCodeNew = accertamento.getStato().toString();
		statoCodeNew = CostantiFin.convertiStatoMovgest(statoCodeNew);
		if(statoCodeNew==null){
			//in caso di stato indicato non riconosciuto impostiamo come se non ci siano cambiamenti
			statoCodeNew = statoCod;
		}
		
		//	STATO OPERATIVO: verificare la coerenza di passaggio di stato con  le regole descritte ai par. 2.5.4 e 2.5.5.
		//In caso contrario inviare il messaggio <COR_ERR_0010. Valore non valido
		//(nome parametro = Stato, messaggio aggiuntivo= non è prevista la transazione di stato)>
		boolean controlloCambioStato = controlloCambioStatoImpegnoAccertamento(statoCod,statoCodeNew);
		if(!controlloCambioStato){
			listaErrori.add(ErroreCore.TRANSAZIONE_DI_STATO_NON_POSSIBILE.getErrore("Stato","non è prevista la transazione di stato"));
			esito.setListaErrori(listaErrori);
			return esito;
		}
		
		//	Verifica della disponibilità ad utilizzare  
		//Se  l'Accertamento è interessato da una  modifica  di segno negativo occorre calcolare la nuova disponibilità ad utilizzare e verificare che risulti ancora >= 0
		//	NuovaDispUtilizare (acc) = VecchiaDispUtilizare (Acc) + TotaleModifiche (Acc)  
		//In caso di errore inviare il messaggio   <FIN_ERR_0230 Disponibilità Insufficiente (<operazione>: Modifica accertamento num:
		//riportare il numero identificativo della modifica; <tipo>: disponibilit� ad utilizzare per vincoli.>
		//verifichiamo l'importo rispetto ai vincoli con gli impegni:
		List<VincoloAccertamento> vincoli = getImpegniVincolati(siacTMovgestTs);
		BigDecimal totQuoteVincoli = BigDecimal.ZERO;
		if(vincoli!=null && vincoli.size()>0){
			totQuoteVincoli = calcolaTotQuoteVincoliAcc(vincoli);
			if(importoUtilizzabileNew.compareTo(totQuoteVincoli)<0){
				//importoUtilizzabileNew e' minore della sommatoria dei vincoli: lancio errore:
				listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE.getErrore("Modifica accertamento num:" + accertamento.getNumeroBigDecimal(),"disponibilità ad utilizzare per vincoli"));
				esito.setListaErrori(listaErrori);
				return esito;
			}
		}
		
		/////////////////////////////////////////////////////////////

		
		//IMPORTO:
		//Controllo Stato/Importo: l�importo � modificabile solo se STATO ACCERTAMENTO = PROVVISIORIO,
		//in caso contrario deve essere emesso l�errore:
		//<FIN_ERR_0030: Modifica Importo Accertamento con stato accertamento non provvisorio>
		if(!CostantiFin.MOVGEST_STATO_PROVVISORIO.equalsIgnoreCase(statoCod) && importoAttualeOld.compareTo(nuovoImportoAttuale)!=0){
			//prima di dire che e' non modificabile, occore valutare se puo' esserlo per predispozione consuntivo
			if(!modificabilePerPredisposizioneConsuntivo){
				listaErrori.add(ErroreFin.MODIFICA_IMPORTO_ACCERTAMENTO_CON_STATO_PROVV.getErrore());
				esito.setListaErrori(listaErrori);
				return esito;
			}
		}
		
		
		//SUBACCERTAMENTI
		//Se sono presenti subaccertamenti effettuare i controlli descritti di seguito.

		//o	IMPORTO: da considerare solo gli importi dei subaccertamenti NON  ANNULLATI
		//o	Controllo Stato/Importo: l�importo � modificabile solo se STATO SUBACCERTAMENTO = PROVVISIORIO,
		//in caso contrario deve essere emesso l�errore:
		//<FIN_ERR_0030: Modifica Importo Accertamento con stato accertamento non provvisorio>

		SubMovgestInModificaInfoDto infoSub = impegnoInModificaInfoDto.getInfoSubValutati();
		if(infoSub!=null){
			ArrayList<SubAccertamento> subModificati = infoSub.getSubImpegniDaModificare();
			List<SiacTMovgestTsFin> subsSuDb = infoSub.getSubImpegniOld();
			
			if(subModificati!=null && subModificati.size()>0){
				for(SubAccertamento subIt : subModificati){
					SiacTMovgestTsFin siacTMovgestTsSub = DatiOperazioneUtil.getById(subsSuDb, subIt.getUid());
					String statoSub = getStatoCode(siacTMovgestTsSub, datiOperazione);
					if(!CostantiFin.MOVGEST_STATO_PROVVISORIO.equals(statoSub)){
						BigDecimal nuovoImpSub = subIt.getImportoAttuale();
						BigDecimal oldImpSub = estraiImportoAttualeByMovgestTsId(subIt.getUid(), datiOperazione);
						if(oldImpSub.compareTo(nuovoImpSub)!=0){
							//importo modificato..
							boolean importoModificabile = true;
							if(CostantiFin.MOVGEST_STATO_ANNULLATO.equals(statoSub)){
								importoModificabile = false;
							} else {
								Integer annoSubMovimento = siacTMovgestTsSub.getSiacTMovgest().getMovgestAnno();
								boolean subModificabilePerPredisposizioneConsuntivo = modificabilePerPredisposizioneConsuntivo(annoSubMovimento, datiOperazione, bilancio);
								importoModificabile = subModificabilePerPredisposizioneConsuntivo;
							}
							if(!importoModificabile){
								listaErrori.add(ErroreFin.MODIFICA_IMPORTO_ACCERTAMENTO_CON_STATO_PROVV.getErrore());
								esito.setListaErrori(listaErrori);
								return esito;
							}
						}
					}
				}
			}
		}
		
        return esito;
	}
	
	/**
	 * IMPLEMENTARE IN QUESTO METODO I VARI CONTROLLI PREVISTI AL PASSO "3.	Verifica dati trasmessi"
	 * DEL CAPITOLO 2.4.5	Operazione: Gestisce Modifica Movimento Entrata 
	 * @param richiedente
	 * @param ente
	 * @param bilancio
	 * @param accertamento
	 * @param datiOperazione
	 * @param impegnoInModificaInfoDto
	 * @param capitoliInfo
	 * @return
	 */
	public EsitoControlliDto controlliDiMeritoAggiornamentoModificaMovimentoEntrata(Richiedente richiedente, Ente ente, Bilancio bilancio, Accertamento accertamento,DatiOperazioneDto datiOperazione, 
			ImpegnoInModificaInfoDto impegnoInModificaInfoDto,ModificaMovimentoGestioneEntrataInfoDto valutazioneModMov,List<ModificaMovimentoGestioneEntrataInfoDto> valutazioneModMovSubs, CapitoliInfoDto capitoliInfo){
		
		EsitoControlliDto esito = new EsitoControlliDto();
		
		int idEnte = datiOperazione.getSiacTEnteProprietario().getUid();
		
		SiacTMovgestTsFin siacTMovgestTs = impegnoInModificaInfoDto.getSiacTMovgestTs();
		String statoCod = getStatoCode(siacTMovgestTs, datiOperazione);
		//carico l'importo attuale:
		BigDecimal importoAttualeOld = estraiImportoAttualeByMovgestTsId(siacTMovgestTs.getMovgestTsId(), datiOperazione);
		
		//Operazioni da ripetere per accertamento e SUB passati.
		
		//esito valutazione modifiche sull'accertamento:
		List<ModificaMovimentoGestioneEntrata> modificheDaAggiornareAcceratamento = valutazioneModMov.getModificheDaAggiornare();
		List<ModificaMovimentoGestioneEntrata> modificheDaCreareAcceratamento = valutazioneModMov.getModificheDaCreare();
		List<ModificaMovimentoGestioneEntrata> daCrearePiuAggiornareAcceratamento = toList(modificheDaAggiornareAcceratamento,modificheDaCreareAcceratamento);
		//esito valutazione modifiche sui sub accertamenti:
		List<ModificaMovimentoGestioneEntrata> modificheDaAggiornareSubAcc = null;
		List<ModificaMovimentoGestioneEntrata> modificheDaCreareSubAcc = null;
		if(valutazioneModMovSubs!=null && valutazioneModMovSubs.size()>0){
			
			
			for(ModificaMovimentoGestioneEntrataInfoDto mmgendIt : valutazioneModMovSubs){
								
				modificheDaAggiornareSubAcc = toList(modificheDaAggiornareSubAcc,mmgendIt.getModificheDaAggiornare());
				modificheDaCreareSubAcc = toList(modificheDaCreareSubAcc,mmgendIt.getModificheDaCreare());
			}
		}
		List<ModificaMovimentoGestioneEntrata> daCrearePiuAggiornareSubAcc = toList(modificheDaAggiornareSubAcc,modificheDaCreareSubAcc);
		//Dati gia caricati relativi ai sub:
		SubMovgestInModificaInfoDto infoSub = impegnoInModificaInfoDto.getInfoSubValutati();
		List<SiacTMovgestTsFin> subsSuDb = null;
		if(infoSub!=null){
			subsSuDb = infoSub.getSubImpegniOld();
		}
		List<Integer> distintiSubCoinvoltiNelleModifiche = new ArrayList<Integer>();
		if(daCrearePiuAggiornareSubAcc!=null && daCrearePiuAggiornareSubAcc.size()>0){
			for(ModificaMovimentoGestioneEntrata modIt : daCrearePiuAggiornareSubAcc){
				SiacTMovgestTsFin siacTMovgestTsSub = DatiOperazioneUtil.getById(subsSuDb, modIt.getUidSubAccertamento());
				if(!distintiSubCoinvoltiNelleModifiche.contains(siacTMovgestTsSub.getUid())){
					distintiSubCoinvoltiNelleModifiche.add(siacTMovgestTsSub.getUid());
				}
			}
		}
		
		
		//	VALIDIT�: se lo stato ACCERTAMENTO o SUBACCERTAMENTO originale � ANNULLATO o PROVVISORIO 
		//il servizio restituisce l�errore 
		//<COR_ERR_0028	Operazione incompatibile con stato dell'entit� (entit�:accertamento  stato: statoOperativo)>
		List<Errore> listaErroriVerificaStato = 
				verificaStatoPerAggiornaModificheMovimento(statoCod, distintiSubCoinvoltiNelleModifiche, subsSuDb, datiOperazione, CostantiFin.MOVGEST_TIPO_ACCERTAMENTO);
		if(listaErroriVerificaStato!=null && listaErroriVerificaStato.size()>0){
			esito.setListaErrori(listaErroriVerificaStato);
			return esito;
		}

		//DATO che il servizio puo accettare (per volta) al massimo una sola mod di acc e/o una sola mod di subacc istanzio le variabili:
		BigDecimal valoreModifica = BigDecimal.ZERO;
		BigDecimal valoreModificaSub = BigDecimal.ZERO;
		ModificaMovimentoGestioneEntrata modificaImportoAcc = null;
		ModificaMovimentoGestioneEntrata modificaImportoSub = null;
		
		//EVENTUALE MODIFICA IMPORTO ACCERTAMENTO:
		if(modificheDaCreareAcceratamento!=null && modificheDaCreareAcceratamento.size()>0){
			for(ModificaMovimentoGestioneEntrata modNewIt : modificheDaCreareAcceratamento){
				//andiamo a cercare l'unica (eventuale) modifica di tipo importo:
				if(modNewIt.isModificaDiImporto()){
					modificaImportoAcc = modNewIt;
					valoreModifica = modNewIt.getImportoOld();
					//dato che puo' essere una solo interrompo il ciclo:
					break;
				}
			}
		}
		//EVENTUALE MODIFICA IMPORTO SUB ACC:
		if(modificheDaCreareSubAcc!=null && modificheDaCreareSubAcc.size()>0){
			for(ModificaMovimentoGestioneEntrata modNewIt : modificheDaCreareSubAcc){
				//andiamo a cercare l'unica (eventuale) modifica di tipo importo:
				if(modNewIt.isModificaDiImporto()){
					modificaImportoSub = modNewIt;
					valoreModificaSub = modNewIt.getImportoOld();
					//dato che puo' essere una solo interrompo il ciclo:
					break;
				}
			}
		}
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		//verifica su disp incassare
		//Per ciascun Accertamento o sub interessato dalle modifiche occorre calcolare la nuova disponibilit� ad incassare e verificare che risulti ancora >= 0
		//NuovaDispIncassare (acc/sub) = VecchiaDispincassare (Acc/sub) + TotaleModifiche (Acc/sub)  
		//In caso di errore inviare il messaggio   <FIN_ERR_0230 Disponibilit� Insufficiente (<operazione>: 
		//indicare se si tratta di Modifica accertamento  o subaccertamento e riportare il numero identificativo della modifica; <tipo>: disponibilit� ad incassare.>
		if(modificaImportoAcc!=null){
			if(valoreModifica.compareTo(BigDecimal.ZERO)<0){
				//eseguo il controllo sul disp a incassare solo se ricevo una modifica negativa:
				DisponibilitaMovimentoGestioneContainer dispAIncassareAccertamento = calcolaDisponibiltaAIncassareAccertamento(siacTMovgestTs, statoCod, idEnte);
				BigDecimal nuovaDispIncassareAcc = dispAIncassareAccertamento.getDisponibilita().add(valoreModifica);
				if(nuovaDispIncassareAcc.compareTo(BigDecimal.ZERO)<0){
					esito.addErrore(ErroreFin.DISPONIBILITA_INSUFFICIENTE.getErrore("Modifica importo accertamento","a incassare"));
					return esito;
				}
			}
		}
		
		if(modificaImportoSub!=null){
			if(valoreModificaSub.compareTo(BigDecimal.ZERO)<0){
				//eseguo il controllo sul disp a incassare solo se ricevo una modifica negativa:
				Integer tempUidSafe = null;
				if(modificaImportoSub.getSubAccertamento()!=null && modificaImportoSub.getSubAccertamento().getUid()!=0){
					tempUidSafe = modificaImportoSub.getSubAccertamento().getUid();
				}else{
					tempUidSafe = modificaImportoSub.getUidSubAccertamento();
				}
				
				SiacTMovgestTsFin siacTMovgestTsSub = DatiOperazioneUtil.getById(subsSuDb, tempUidSafe);
				DisponibilitaMovimentoGestioneContainer dispAIncassareAccertamento = calcolaDisponibiltaAIncassareAccertamento(siacTMovgestTsSub, statoCod, idEnte);
				BigDecimal nuovaDispIncassareAcc = dispAIncassareAccertamento.getDisponibilita().add(valoreModificaSub);
				if(nuovaDispIncassareAcc.compareTo(BigDecimal.ZERO)<0){
					esito.addErrore(ErroreFin.DISPONIBILITA_INSUFFICIENTE.getErrore("Modifica importo sub accertamento","a incassare"));
					return esito;
				}
			}
		}
		
		//Verifica della disponibilit� ad utilizzare  
		//Sel�Accertamento � interessato dalle modifiche occorre calcolare la nuova disponibilit� ad utilizzare e verificare che risulti ancora >= 0
		//NuovaDispUtilizare (acc) = VecchiaDispUtilizare (Acc) + TotaleModifiche (Acc)  
		//In caso di errore inviare il messaggio   <FIN_ERR_0230 Disponibilit� Insufficiente (<operazione>: Modifica accertamento num:  
		//	riportare il numero identificativo della modifica; <tipo>: disponibilit� ad utilizzare per vincoli.>
		
		if(modificaImportoAcc!=null){
			if(valoreModifica.compareTo(BigDecimal.ZERO)<0){
				//eseguo il controllo sul disp a incassare solo se ricevo una modifica negativa:
				DisponibilitaMovimentoGestioneContainer dispUtilizzabile = calcolaDisponibilitaAUtilizzare(siacTMovgestTs,datiOperazione);
				BigDecimal nuovaDispUtilizzabileAcc = dispUtilizzabile.getDisponibilita().add(valoreModifica);
				if(nuovaDispUtilizzabileAcc.compareTo(BigDecimal.ZERO)<0){
					esito.addErrore(ErroreFin.DISPONIBILITA_INSUFFICIENTE.getErrore("Modifica importo accertamento","a utilizzare"));
					return esito;
				}
			}
		}

		//DISP ACCERTARE:
		if(modificaImportoAcc!=null || modificaImportoSub!=null){
			//NuovaDispAcc = VecchiaDispAcc + TotaleModificheAcc - SOMMATORIA TotaleModificheSub
			//eseguo il controllo sul disp a incassare solo se ricevo una modifica negativa:
			BigDecimal vecchiaDispAcc= calcolaDisponibilitaASubAccertare(siacTMovgestTs, datiOperazione);
			BigDecimal nuovaDispAcc = vecchiaDispAcc.add(valoreModifica).subtract(valoreModificaSub);
			if(nuovaDispAcc.compareTo(BigDecimal.ZERO)<0){
				//Se NuovaDispAcc < 0 si resituisce il seguente messaggio di errore bloccante:
				//<FIN_ERR_0230. Disponibilit� Insufficiente (operazione=�Gestione Modifiche, tipo= a Accertare�)>
				esito.addErrore(ErroreFin.DISPONIBILITA_INSUFFICIENTE.getErrore("Modifica importo accertamento","a Accertare"));
				return esito;
			}
		}
		
		//DISP ACCERTARE CAPITOLO:
		if(modificaImportoAcc!=null){
			//Se fra i parametri in input � passata la lista di errori vuota o diversamente non è presente in elenco il  codice errore relativo al 
			//controllo di disponibilit� FIN_INF_0062, il sistema verifica che la disponibilità ad accertare 
			//sul capitolo di riferimento non sia superata con l�inserimento del nuovo accertamento.
			CapitoloEntrataGestione capitoloEscGest = capitoliInfo.getCapitoliDaServizioEntrata().get(accertamento.getChiaveCapitoloEntrataGestione());
			if(capitoloEscGest!=null){
				//NuovaDispAccCap = VecchiaDispAccCap - TotaleModificheAcc
//				BigDecimal vecchiaDispAccCap = capitoloEscGest.getImportiCapitoloEG().getDisponibilitaAccertareAnno1();
				//SIAC-82821 nuovo controllo su piu' anni del capitolo in base all'anno del movimento
				BigDecimal nuovaDispAcc = calcolaNuovaDisponibilitaAccertareSuPiuAnniCapitolo(valoreModifica, capitoloEscGest, accertamento);
				//Se NuovaDispAccCap  < 0 si restituisce il messaggio di warning relativo:
				//<FIN_INF_0062 - Superamento della disponibilita >
				if(nuovaDispAcc.compareTo(BigDecimal.ZERO)<0){
					//TODO: POTREBBE NON E' UN ERRORE BLOCCANTE: manca la lista errori in input con gli errori da escludere
					esito.addErrore(ErroreFin.SUPERAMENTO_DISPONIBILITA.getErrore());
					return esito;
				}
			}
		}
		
		
        return esito;
	}
	
	/**
	 * Funziona sia per impegni che per accertamenti
	 * @param statoCod
	 * @param distintiSubCoinvoltiNelleModifiche
	 * @param subsSuDb
	 * @param datiOperazione
	 * @return
	 */
	private List<Errore> verificaStatoPerAggiornaModificheMovimento(String statoCod,List<Integer> distintiSubCoinvoltiNelleModifiche,List<SiacTMovgestTsFin> subsSuDb,
			DatiOperazioneDto datiOperazione,String tipoMovimento){
		
		String messaggio = null;
		if(CostantiFin.MOVGEST_TIPO_IMPEGNO.equals(tipoMovimento)){
			messaggio = "impegno";
		} else {
			messaggio = "accertamento";
		}
		
		List<Errore> listaErrori = new ArrayList<Errore>();
//		VALIDIT�: se lo stato ACCERTAMENTO/IMPEGNO o SUBACCERTAMENTO/IMPEGNO originale � ANNULLATO o PROVVISORIO 
			//il servizio restituisce l�errore 
			//<COR_ERR_0028	Operazione incompatibile con stato dell'entit� (entit�:accertamento  stato: statoOperativo)>
		if(CostantiFin.MOVGEST_STATO_PROVVISORIO.equalsIgnoreCase(statoCod) || CostantiFin.MOVGEST_STATO_ANNULLATO.equalsIgnoreCase(statoCod)) {
			//Accertamento PROVVOSIORIO o ANNULLATO
			listaErrori.add(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore(messaggio,statoCod));
			return listaErrori;
		}
		if(distintiSubCoinvoltiNelleModifiche!=null && distintiSubCoinvoltiNelleModifiche.size()>0){
			for(Integer subCoinvoltoInModifiche : distintiSubCoinvoltiNelleModifiche){
				SiacTMovgestTsFin siacTMovgestTsSub = DatiOperazioneUtil.getById(subsSuDb, subCoinvoltoInModifiche);
				String statoSub = getStatoCode(siacTMovgestTsSub, datiOperazione);
				if(CostantiFin.MOVGEST_STATO_PROVVISORIO.equalsIgnoreCase(statoCod) || CostantiFin.MOVGEST_STATO_ANNULLATO.equalsIgnoreCase(statoCod)) {
					//SUB Accertamento PROVVOSIORIO o ANNULLATO
					listaErrori.add(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore(messaggio,statoCod));
					return listaErrori;
				}
			}
		}
		return listaErrori;
	}
	
	
	/**
	 * IMPLEMENTARE IN QUESTO METODO I VARI CONTROLLI PREVISTI AL PASSO "3.	Verifica dati trasmessi"
	 * DEL CAPITOLO 2.4.5	Operazione: Gestisce Modifica Movimento Entrata 
	 * @param richiedente
	 * @param ente
	 * @param bilancio
	 * @param accertamento
	 * @param datiOperazione
	 * @param impegnoInModificaInfoDto
	 * @param capitoliInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public EsitoControlliDto controlliDiMeritoAggiornamentoModificaMovimentoSpesa(Richiedente richiedente, String codiceAmbito,Ente ente, Bilancio bilancio, Impegno impegno,DatiOperazioneDto datiOperazione, 
			ImpegnoInModificaInfoDto impegnoInModificaInfoDto,ModificaMovimentoGestioneSpesaInfoDto valutazioneModMov,List<ModificaMovimentoGestioneSpesaInfoDto> valutazioneModMovSubs, CapitoliInfoDto capitoliInfo){
		
		EsitoControlliDto esito = new EsitoControlliDto();
		
		int idEnte = datiOperazione.getSiacTEnteProprietario().getUid();
		
		SiacTMovgestTsFin siacTMovgestTs = impegnoInModificaInfoDto.getSiacTMovgestTs();
		String statoCod = getStatoCode(siacTMovgestTs, datiOperazione);
				
		//Operazioni da ripetere per impegno e SUB passati.
		
		//esito valutazione modifiche sull'impegno:
		List<ModificaMovimentoGestioneSpesa> modificheDaCreareImpegno = valutazioneModMov.getModificheDaCreare();
		
		//esito valutazione modifiche sui sub accertamenti:
		List<ModificaMovimentoGestioneSpesa> modificheDaAggiornareSubImp = null;
		List<ModificaMovimentoGestioneSpesa> modificheDaCreareSubImp = null;
		if(valutazioneModMovSubs!=null && valutazioneModMovSubs.size()>0){
			for(ModificaMovimentoGestioneSpesaInfoDto mmgendIt : valutazioneModMovSubs){
				modificheDaAggiornareSubImp = toList(modificheDaAggiornareSubImp,mmgendIt.getModificheDaAggiornare());
				modificheDaCreareSubImp = toList(modificheDaCreareSubImp,mmgendIt.getModificheDaCreare());
			}
		}
		List<ModificaMovimentoGestioneSpesa> daCrearePiuAggiornareSubImp = toList(modificheDaAggiornareSubImp,modificheDaCreareSubImp);
		//Dati gia caricati relativi ai sub:
		SubMovgestInModificaInfoDto infoSub = impegnoInModificaInfoDto.getInfoSubValutati();
		List<SiacTMovgestTsFin> subsSuDb = null;
		if(infoSub!=null){
			//ramo ottimizzato, gia caricai dai chiamanti per qualche atro motivo
			subsSuDb = infoSub.getSubImpegniOld();
		} else {
			//devo ricarica i sub da db
			subsSuDb = siacTMovgestTsRepository.findListaSiacTMovgestTsFigli(idEnte, datiOperazione.getTs(), siacTMovgestTs.getUid());
		}
		List<Integer> distintiSubCoinvoltiNelleModifiche = new ArrayList<Integer>();
		if(daCrearePiuAggiornareSubImp!=null && daCrearePiuAggiornareSubImp.size()>0){
			for(ModificaMovimentoGestioneSpesa modIt : daCrearePiuAggiornareSubImp){
				SiacTMovgestTsFin siacTMovgestTsSub = DatiOperazioneUtil.getById(subsSuDb, modIt.getUidSubImpegno());
				if(!distintiSubCoinvoltiNelleModifiche.contains(siacTMovgestTsSub.getUid())){
					distintiSubCoinvoltiNelleModifiche.add(siacTMovgestTsSub.getUid());
				}
			}
		}
		
		
		//	VALIDITA': se lo stato IMPEGNO o SUBIMPEGNO originale e' ANNULLATO o PROVVISORIO 
		//  il servizio restituisce l'errore 
		//  <COR_ERR_0028	Operazione incompatibile con stato dell'entità (entità:impegno  stato: statoOperativo)>
		List<Errore> listaErroriVerificaStato = 
				verificaStatoPerAggiornaModificheMovimento(statoCod, distintiSubCoinvoltiNelleModifiche, subsSuDb, datiOperazione, CostantiFin.MOVGEST_TIPO_IMPEGNO);
		if(listaErroriVerificaStato!=null && listaErroriVerificaStato.size()>0){
			esito.setListaErrori(listaErroriVerificaStato);
			return esito;
		}

		//DATO che il servizio puo accettare (per volta) al massimo una sola mod di imp e/o una sola mod di subimp istanzio le variabili:
		BigDecimal valoreModifica = BigDecimal.ZERO;
		BigDecimal valoreModificaSub = BigDecimal.ZERO;
		ModificaMovimentoGestioneSpesa modificaImportoImp = null;
		ModificaMovimentoGestioneSpesa modificaImportoSub = null;
		
		//EVENTUALE MODIFICA IMPORTO IMPEGNO:
		if(modificheDaCreareImpegno!=null && modificheDaCreareImpegno.size()>0){
			for(ModificaMovimentoGestioneSpesa modNewIt : modificheDaCreareImpegno){
				//andiamo a cercare l'unica (eventuale) modifica di tipo importo:
				if(modNewIt.isModificaDiImporto()){
					modificaImportoImp = modNewIt;
					valoreModifica = modNewIt.getImportoOld();
					//dato che puo' essere una solo interrompo il ciclo:
					break;
				}
			}
		}
		//EVENTUALE MODIFICA IMPORTO SUB IMP:
		if(modificheDaCreareSubImp!=null && modificheDaCreareSubImp.size()>0){
			for(ModificaMovimentoGestioneSpesa modNewIt : modificheDaCreareSubImp){
				//andiamo a cercare l'unica (eventuale) modifica di tipo importo:
				if(modNewIt.isModificaDiImporto()){
					modificaImportoSub = modNewIt;
					valoreModificaSub = modNewIt.getImportoOld();
					//dato che puo' essere una solo interrompo il ciclo:
					break;
				}
			}
		}
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		if(modificaImportoSub!=null){
			//Disp liquidare modifica sub imp
			Integer tempUidSafe = null;
			if(modificaImportoSub.getSubImpegno()!=null && modificaImportoSub.getSubImpegno().getUid()!=0){
				tempUidSafe = modificaImportoSub.getSubImpegno().getUid();
			}else{
				tempUidSafe = modificaImportoSub.getUidSubImpegno();
			}
			//Per i Subimpegni: per ogni sub viene calcolato il nuovo disponibile come segue:
			//	NuovaDispLiqSub = VecchiaDispLiqSub + TotaleModificheSub
			//dove:
			//	VecchiaDispLiqSub e' il campo calcolato disponibilitaLiquidare del subimpegno
			SiacTMovgestTsFin siacTMovgestTsSub = DatiOperazioneUtil.getById(subsSuDb, tempUidSafe);
			DisponibilitaMovimentoGestioneContainer disponibilitaLiquidareContainer = calcolaDisponibilitaALiquidare(siacTMovgestTsSub.getMovgestTsId(), idEnte, datiOperazione);
			BigDecimal nuovaDispLiqSub = disponibilitaLiquidareContainer.getDisponibilita().add(valoreModificaSub);
			if(nuovaDispLiqSub.compareTo(BigDecimal.ZERO)<0){
				esito.addErrore(ErroreFin.DISPONIBILITA_INSUFFICIENTE.getErrore("Modifica importo sub impegno","a liquidare"));
				return esito;
			}
		}
		
		//DISP IMPEGNARE:
		if(modificaImportoImp!=null || modificaImportoSub!=null){
			//NuovaDispImp = VecchiaDispImp + TotaleModificheImp - SOMMATORIA TotaleModificheSub
//			VecchiaDispImpassume il valore di DISPONIBILE IMPEGNO descritto in Calcolo disponibile Impegno
			DisponibilitaMovimentoGestioneContainer vecchiaDispImp= calcolaDisponibilitaImpegnoModifica(siacTMovgestTs.getMovgestTsId(), datiOperazione);
			BigDecimal nuovaDispImp = vecchiaDispImp.getDisponibilita().add(valoreModifica).subtract(valoreModificaSub);
			//Se NuovaDispAcc < 0 si resituisce il seguente messaggio di errore bloccante:
			//<FIN_ERR_0230. Disponibilit� Insufficiente (operazione=�Gestione Modifiche, tipo= a Accertare�)>
			if(nuovaDispImp.compareTo(BigDecimal.ZERO)<0){
				esito.addErrore(ErroreFin.DISPONIBILITA_INSUFFICIENTE.getErrore("Modifica importo impegno","a Impegnare"));
				return esito;
			}
		
		}
		
		//DISP IMPEGNARE CAPITOLO:
		if(modificaImportoImp!=null){
			//	Impegno: per l'impegno vengono calcolati i nuovi disponibili come segue:
			//	NuovaDispImpCap = VecchiaDispImpCap-TotaleModificheImp
			//	VecchiaDispImpCap assume il valore DISPONIBILE a IMPEGNARE sul CAPITOLO descritto in Ricerca disponibilit� ad impegnare

			CapitoloUscitaGestione capitoloEscGest = capitoliInfo.getCapitoliDaServizioUscita().get(impegno.getChiaveCapitoloUscitaGestione());
			if(capitoloEscGest!=null){
				//NuovaDispAccCap = VecchiaDispAccCap - TotaleModificheAcc
				BigDecimal vecchiaDispImpCap = capitoloEscGest.getImportiCapitoloUG().getDisponibilitaImpegnareAnno1();
				BigDecimal nuovaDispImpCap = vecchiaDispImpCap.subtract(valoreModifica);
				//Se NuovaDispAccCap  < 0 si restituisce il messaggio di warning relativo:
				//<FIN_INF_0062 - Superamento della disponibilita' >
				if(nuovaDispImpCap.compareTo(BigDecimal.ZERO)<0){
					esito.addErrore(ErroreFin.DISPONIBILITA_INSUFFICIENTE.getErrore("Modifica importo impegno","a Impegnare"));
					return esito;
				}
			}
		}
		
		
		//SOGGETTO:
		if(modificaImportoImp==null && modificaImportoSub==null){
			//se non e' modifica di importo puo' essere modifica di soggetto:
			//EVENTUALE MODIFICA IMPORTO IMPEGNO:
			ModificaMovimentoGestioneSpesa modificaSoggettoImp = null;
			ModificaMovimentoGestioneSpesa modificaSoggettoSub = null;
			if(modificheDaCreareImpegno!=null && modificheDaCreareImpegno.size()>0){
				for(ModificaMovimentoGestioneSpesa modNewIt : modificheDaCreareImpegno){
					//andiamo a cercare l'unica (eventuale) modifica di tipo importo:
					if(!modNewIt.isModificaDiImporto()){
						modificaSoggettoImp = modNewIt;
						//dato che puo' essere una solo interrompo il ciclo:
						break;
					}
				}
			}
			//EVENTUALE MODIFICA IMPORTO SUB IMP:
			if(modificheDaCreareSubImp!=null && modificheDaCreareSubImp.size()>0){
				for(ModificaMovimentoGestioneSpesa modNewIt : modificheDaCreareSubImp){
					//andiamo a cercare l'unica (eventuale) modifica di tipo importo:
					if(!modNewIt.isModificaDiImporto()){
						modificaSoggettoSub = modNewIt;
						//dato che puo' essere una solo interrompo il ciclo:
						break;
					}
				}
			}
			
			//SOGGETTO NUOVO:(controlli da effettuarsi per le modifiche in inserimento e con dati �soggetto nuovo non nulli)
			//	Presenza liquidazioni: se esistono liquidazioni non annullate per il subimpegno o impegno per cui viene modificato il soggetto deve essere emesso l'errore:
			// <FIN_ERR_0278: Movimento liquidato (movimento = IMPEGNO, tipopaginc = LIQUIDATO)

			Soggetto soggettoNuovo = null;
			if(modificaSoggettoImp!=null){
				soggettoNuovo = modificaSoggettoImp.getSoggettoNewMovimentoGestione();
			} else if(modificaSoggettoSub!=null){
				soggettoNuovo = modificaSoggettoSub.getSoggettoNewMovimentoGestione();
			}
			if(soggettoNuovo!=null && !StringUtilsFin.isEmpty(soggettoNuovo.getCodiceSoggetto())){
				String codiceNuovoSogg = soggettoNuovo.getCodiceSoggetto();
				SiacTSoggettoFin siacTSoggettoNew = siacTSoggettoRepository.ricercaSoggettoNoSeSede(codiceAmbito, idEnte,codiceNuovoSogg,CostantiFin.SEDE_SECONDARIA,getNow());
				List<SiacRLiquidazioneSoggettoFin>  rLiqSoggetto = siacTSoggettoNew.getSiacRLiquidazioneSoggettos();
				rLiqSoggetto = DatiOperazioneUtil.soloValidi(rLiqSoggetto, null);
				if(rLiqSoggetto!=null && rLiqSoggetto.size()>0){
					for(SiacRLiquidazioneSoggettoFin rLiq : rLiqSoggetto){
						String codeStatoLiq = EntityLiquidazioneToModelLiquidazioneConverter.getStatoCodeLiquidazione(rLiq.getSiacTLiquidazione());
						if(!CostantiFin.D_LIQUIDAZIONE_STATO_ANNULLATO.equals(codeStatoLiq)){
							esito.addErrore(ErroreFin.MOV_LIQUIDATO.getErrore("Modifica importo impegno","a Impegnare"));
							return esito;
						}
					}
				}
			}
		}
		
        return esito;
	}
	
	
	
	/**
	 * IMPLEMENTARE IN QUESTO METODO I VARI CONTROLLI PREVISTI AL PASSO "3.	Verifica dati trasmessi"
	 * DEL CAPITOLO "2.4.2	Operazione Interna: Gestisce Impegno"
	 * @param richiedente
	 * @param ente
	 * @param bilancio
	 * @param impegno
	 * @param datiOperazione
	 * @return
	 */
	public <I extends MovimentoGestione> EsitoControlliAggiornamentoMovimentoDto controlliDiMeritoAggiornamentoImpegnoOperazioneInterna(Richiedente richiedente, Ente ente, Bilancio bilancio, Impegno impegno,
			DatiOperazioneDto datiOperazione, ImpegnoInModificaInfoDto impegnoInModificaInfoDto,HashMap<Integer, CapitoloUscitaGestione> capitoliUscita){
		
		String methodName ="controlliDiMeritoAggiornamentoImpegnoOperazioneInterna";
		EsitoControlliAggiornamentoMovimentoDto esitoControlli = new EsitoControlliAggiornamentoMovimentoDto();
		
		// Dobbiamo capire se il metodo e' stato invocato per:
		// A. Aggiornare l'impegno/accertamento in senso stretto
		// B. Aggiornare le modifiche movimento
		boolean presentiModifiche = presentiModifiche(impegno, datiOperazione);
		
		int idEnte = ente.getUid();
		List<Errore> listaErrori = new ArrayList<Errore>();
		SiacTMovgestTsFin siacTMovgestTs = impegnoInModificaInfoDto.getSiacTMovgestTs();
		
		//carico l'importo attuale:
		BigDecimal importoAttualeOld = 
				estraiImportoAttualeByMovgestTsId(siacTMovgestTs.getMovgestTsId(), datiOperazione);
		String statoCod = getStatoCode(siacTMovgestTs, datiOperazione);
		///
		
		Integer annoMovimento = siacTMovgestTs.getSiacTMovgest().getMovgestAnno();
		int annoBilancio = bilancio.getAnno();
		
		BigDecimal nuovoImportoAttuale = impegno.getImportoAttuale();
		String statoCodeNew = impegno.getStato().toString();
		statoCodeNew = CostantiFin.convertiStatoMovgest(statoCodeNew);
		if(statoCodeNew==null){
			//in caso di stato indicato non riconosciuto impostiamo come se non ci siano cambiamenti
			statoCodeNew = statoCod;
		}
		
		SubMovgestInModificaInfoDto infoValutati = impegnoInModificaInfoDto.getInfoSubValutati();
		ArrayList<I> subImpegniRimanentiDopoModifiche = infoValutati.getRimanentiDopoModifiche();
		
		boolean resterannoDeiSub = false;
		if(subImpegniRimanentiDopoModifiche!=null && subImpegniRimanentiDopoModifiche.size()>0){
			for(MovimentoGestione it : subImpegniRimanentiDopoModifiche){
				//String newStatoSub = it.getStato().toString();
				
				String newStatoSub = ((SubImpegno)it).getStatoOperativoMovimentoGestioneSpesa();
				
				if(!CostantiFin.MOVGEST_STATO_ANNULLATO.equalsIgnoreCase(newStatoSub)){
					resterannoDeiSub = true;
					break;
				}
			}
		}
		
		/*
		 * PARTE PRIMA - CONTROLLI RIGUARDANTI DIRETTAMENTE L'IMPEGNO 
		 */
		
		//1	STATO IMPEGNO:
		//o	Transizione Stati: il passaggio dal vecchio al nuovo stato deve rispettare le transizioni descritte a par. 2.5.4, in caso contrario viene emesso l'errore:
		//<FIN_ERR_0055. Stato Movimento Impossibile (movimento = impegno, statoOld = Impegno.stato(su DB), statoNew=impegno.stato (dati input))>
		boolean controlloCambioStato = controlloCambioStatoImpegnoAccertamento(statoCod,statoCodeNew);
		if(!controlloCambioStato){
			listaErrori.add(ErroreFin.STATO_MOVIMENTO_IMPOSSIBILE.getErrore("Impegno",statoCod,statoCodeNew));
			esitoControlli.setListaErrori(listaErrori);
			return esitoControlli;
		}
		
		//	o Annullamento : se il nuovo stato e' ANNULLATO l'impegno non deve essere associato a nessun Sub Impegno valido
		//(la lista dei Subimpegni da Gestire deve essere vuota o contenere solo subimpegni annullatiin caso contrario viene emesso l'rrore 
		//<FIN_ERR_004: Esistono movimenti collegati (operazione=annullamento)>
		if(CostantiFin.MOVGEST_STATO_ANNULLATO.equalsIgnoreCase(statoCodeNew)){
			if(resterannoDeiSub){
				listaErrori.add(ErroreFin.ESISTONO_MOVIMENTI_COLLEGATI.getErrore("annullamento"));
				esitoControlli.setListaErrori(listaErrori);
				return esitoControlli;
			}
		}
	
		
		//2  IMPORTO:controllo da effettuare se stato operativo e' diverso da ANNULLATO
		//  Controllo di disponibilita' sul capitolo --> applicare la seguente condizione:
		//	(DISPONIBILE a IMPEGNARE SUL CAPITOLO + VECCHIO IMPORTO IMPEGNO) >= NUOVO IMPORTO IMPEGNO
		//	In caso il risultato sia falso il servizio emette l'errore:
		//	<BIL_ERR_0004. Disponibilita Insufficiente (operazione=Aggiornamento impegno)>
		if(!CostantiFin.MOVGEST_STATO_ANNULLATO.equalsIgnoreCase(statoCodeNew)){
			CapitoloUscitaGestione capitoloUscGest = capitoliUscita.get(impegno.getChiaveCapitoloUscitaGestione());
			
			Integer annoCapitolo = capitoloUscGest.getAnnoCapitolo();
			
			BigDecimal dispImpegnareCapitolo = null;
			if(annoMovimento.intValue()==annoCapitolo.intValue()){
				dispImpegnareCapitolo= capitoloUscGest.getImportiCapitoloUG().getDisponibilitaImpegnareAnno1();
			} else if(annoMovimento.intValue()==(annoCapitolo.intValue()+1)){
				dispImpegnareCapitolo= capitoloUscGest.getImportiCapitoloUG().getDisponibilitaImpegnareAnno2();
			} else if(annoMovimento.intValue()==(annoCapitolo.intValue()+2)){
				dispImpegnareCapitolo= capitoloUscGest.getImportiCapitoloUG().getDisponibilitaImpegnareAnno3();
			}
			
			if(dispImpegnareCapitolo!=null){
				
				BigDecimal sommaUno = dispImpegnareCapitolo.add(importoAttualeOld);
				if(!(sommaUno.compareTo(nuovoImportoAttuale)>=0)){
					listaErrori.add(ErroreBil.DISPONIBILITA_INSUFFICIENTE.getErrore("Aggiornamento Impegno non possibile, disponibilita' attuale "+CommonUtil.convertiBigDecimalToImporto(dispImpegnareCapitolo)));
					//listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE.getErrore());
					esitoControlli.setListaErrori(listaErrori);
					return esitoControlli;
				}
				
			}
			
		}
		
		
		//3	Controllo di disponibilita' dell'impegno, da effettuare solo se l'impegno e' in stato DEFINITIVO, verificare che:
		//(VECCHIO IMPORTO IMPEGNO -DISPONIBILE IMPEGNO descritto in Calcolo disponibile Impegno) <= NUOVO IMPORTO IMPEGNO
		//	In caso il risultato sia falso il servizio emette l'errore:
		//	<FIN_ERR_0001. Disponibilita Insufficiente (operazione=Aggiornamento Impegno)>
		//	Controllo di disponibilità a finanziare:  se l'importo è stato aggiornato verificare che la DISPONIBILITA' A FINANZIARE risulti ancora >= 0 ovvero   (VECCHIO IMPORTO IMPEGNO -DISPONIBILE A FINANZIARE ) <= NUOVO IMPORTO IMPEGNO  
		//	In caso il risultato sia falso il servizio emette l'errore:
		//	<FIN_ERR_0001. Disponibilità Insufficiente (operazione=Aggiornamento Impegno)>
		if(CostantiFin.MOVGEST_STATO_DEFINITIVO.equalsIgnoreCase(statoCod)){
			//Ha dei sub impegni
			DisponibilitaMovimentoGestioneContainer dispImpegnoMod = calcolaDisponibilitaImpegnoModifica(siacTMovgestTs.getUid(), datiOperazione);
			BigDecimal diffUno = importoAttualeOld.subtract(dispImpegnoMod.getDisponibilita());
			if(diffUno.compareTo(nuovoImportoAttuale)>0){
				listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE_ORIG.getErrore("Aggiornamento Impegno"));
				esitoControlli.setListaErrori(listaErrori);
				return esitoControlli;
			}
		}
		
		//4. ATTO AMMINISTRATIVO:
		//o	Stato Impegno/Atto: l'associazione con l'atto è modificabile solo se STATO IMPEGNO = PROVVISIORIO, in caso contrario viene emesso l'errore 
		//<FIN_ERR_0029.Modifica Atto Autorizzativo con stato impegno non provvisorio>
		//o	Stato Atto e Movimento: Se il NUOVO STATO OPERATIVO del movimento è diverso da annullato l'atto deve esistere e non essere ANNULLATO (vedi Ricerca Provvedimento), in caso contrario viene emesso l'errore:
		//<FIN_ERR_0075. Stato Provvedimento non consentito (operazione = Aggiornamento Impegno, stato = Definitivo)
		List<Errore> listaErroriControlloAttoAmm = controlloAggiornamentoAttoAmministrativo(impegno.getAttoAmministrativo(), datiOperazione, siacTMovgestTs, statoCodeNew, true, false);
		if(listaErroriControlloAttoAmm!=null && listaErroriControlloAttoAmm.size()>0){
			esitoControlli.setListaErrori(listaErroriControlloAttoAmm);
			return esitoControlli;
		}
		
		
		//  5 SOGGETTO creditore dell�impegno(solo se il dato è presente in input):
		//	Stato ed esistenza:il soggetto deve esistere ed essere valido in caso contrario deve essere emesso l'errore:
		//	<COR_ERR_0012 Entità inesistente  (<nome entità> : soggetto <chiave ricerca entità>codice>
		//	O
		//	<COR_ERR_0028 Operazione incompatibile con stato dell'entità (<nome entità> : il soggetto  codice- denominazione  <stato>annullato>
		Errore erroreSoggetto = controlliSoggettoPerAggiorna(impegno, resterannoDeiSub, datiOperazione);
		if(erroreSoggetto!=null){
			listaErrori.add(erroreSoggetto);
			esitoControlli.setListaErrori(listaErrori);
			return esitoControlli;
		}
		if(resterannoDeiSub){
			//	CLASSE SOGGETTO(solo se il dato è presente in input)
			//	Subimpegni: la classe può essere caricata solo se non sono presenti subimpegni tra i dati in input, in caso contrario deve essere emesso l'errore 
			//	<FIN_ERR_0057. Dato Soggetto Presente (entità:  Classe   valore: codice-descrizione,) >
			if(impegno.getClasseSoggetto()!=null){
				//non deve essere valorizzata la classe soggetto dell'impegno principale
				listaErrori.add(ErroreFin.DATO_SOGGETTO_PRESENTE.getErrore("Soggetto Classe",impegno.getClasseSoggetto().getCodice()));
				esitoControlli.setListaErrori(listaErrori);
				return esitoControlli;
			}
		}
		
		//PROGETTO: controllo da effettuare se stato operativo e' diverso da ANNULLATO il progetto collegato all'impegno deve esiste e non essere annullato
		List<Errore> listaErroriProgetto = controlloValiditaProgetto(impegno, datiOperazione);
		if(listaErroriProgetto!=null && listaErroriProgetto.size()>0){
			listaErrori.addAll(listaErroriProgetto);
			esitoControlli.setListaErrori(listaErrori);
			return esitoControlli;
		}
		
		//Controlli sui vincoli:
		ModificaVincoliImpegnoInfoDto valutati = impegnoInModificaInfoDto.getInfoVincoliValutati();
		// Lista che contiene solo i vincoli da inserire / aggiornare
		List<VincoloImpegno> vincoliDaInserireEAggiornare = valutati.getVincoliDaInserireEAggiornare();
		
		// Lista vincoli presenti sul db da confrontare con quelli della lista vincoliDaInserireEAggiornare
		List<SiacRMovgestTsFin> vincoliOld = valutati.getVincoliOld();
		
		BigDecimal sommaImportoVincoli = BigDecimal.ZERO;
		// VINCOLO:controllo da effettuare se stato operativo e' diverso da ANNULLATO 
		// per ogni vincolo
		// o	Accertamento deve esistere e non essere annullato
		// o	Vincolo.importo<= (accertamento.disponibilitaUtilizzare+vecchioVincolo.importo) e SOMMATORIA Vincolo.importo<= Impegno.importoAttuale, in caso contrario viene emesso l'errore:
		// <FIN_ERR_0230: Disponibilita' Insufficiente (operazione = gestione vincolo, <tipo >=a collegare)>
		boolean presenzaVincoli = false;
		
		if(vincoliDaInserireEAggiornare!=null && vincoliDaInserireEAggiornare.size()>0){
			presenzaVincoli = true;
			for(VincoloImpegno vIt : vincoliDaInserireEAggiornare){
				Accertamento acc = vIt.getAccertamento();
				//vediamo se l'accertamento esiste ed e' valido:
				
				boolean accertamentoValido = false;
				annoMovimento = acc.getAnnoMovimento();
				
				log.debug("MovimentoGestioneDad.controlliDiMeritoAggiornamentoImpegnoOperazioneInterna","Parametri ricerca accertamento ");
				log.debug("MovimentoGestioneDad.controlliDiMeritoAggiornamentoImpegnoOperazioneInterna","idEnte: "+idEnte);
				log.debug("MovimentoGestioneDad.controlliDiMeritoAggiornamentoImpegnoOperazioneInterna","annoBilancio: "+Integer.toString(annoBilancio));
				log.debug("MovimentoGestioneDad.controlliDiMeritoAggiornamentoImpegnoOperazioneInterna","annoMovimento: "+annoMovimento);
				log.debug("MovimentoGestioneDad.controlliDiMeritoAggiornamentoImpegnoOperazioneInterna","numero accertamento: "+acc.getNumeroBigDecimal());
				log.debug("MovimentoGestioneDad.controlliDiMeritoAggiornamentoImpegnoOperazioneInterna","MOVGEST_TIPO_ACCERTAMENTO: "+CostantiFin.MOVGEST_TIPO_ACCERTAMENTO.toString());
				
				SiacTMovgestFin siacTMovgestAcc = siacTMovgestRepository.ricercaSiacTMovgestPk(idEnte, Integer.toString(annoBilancio), annoMovimento, acc.getNumeroBigDecimal(), CostantiFin.MOVGEST_TIPO_ACCERTAMENTO);	
				SiacTMovgestTsFin siacTMovgestTsAcc = null;
				if(null!=siacTMovgestAcc){
					siacTMovgestTsAcc = siacTMovgestTsRepository.findMovgestTsByMovgest(idEnte, getNow(), siacTMovgestAcc.getMovgestId()).get(0);
				}else{
					listaErrori.add(ErroreCore.ENTITA_NON_TROVATA.getErrore("Accertamento", acc.getNumeroBigDecimal() + " - anno: "+ annoMovimento ));
					esitoControlli.setListaErrori(listaErrori);
					return esitoControlli;
				}
				

				if(siacTMovgestTsAcc!=null){
					//esiste
					String statoAccertamento = getStatoCode(siacTMovgestTsAcc, datiOperazione);
					log.debug("MovimentoGestioneDad.controlliDiMeritoAggiornamentoImpegnoOperazioneInterna","Controllo statoAccertamento: " + statoAccertamento);
					if(!CostantiFin.MOVGEST_STATO_ANNULLATO.equalsIgnoreCase(statoAccertamento)){
						//non annullato
						accertamentoValido = true;
					}
				}
				
				if(!accertamentoValido){
					listaErrori.add(ErroreFin.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("Accertamento", "ANNULLATO"));
					esitoControlli.setListaErrori(listaErrori);
					return esitoControlli;
				}
				DisponibilitaMovimentoGestioneContainer disponibilitaUtilizzareAcc = calcolaDisponibilitaAUtilizzare(siacTMovgestTsAcc, datiOperazione);
				
				BigDecimal nuovoImportoVincolo = vIt.getImporto();
				BigDecimal oldImportoVincolo = BigDecimal.ZERO;
				if(vIt.getUid()>0){
					//si tratta di nuovo vincolo
					SiacRMovgestTsFin vincoloDb = DatiOperazioneUtil.getById(vincoliOld,vIt.getUid());
					oldImportoVincolo = vincoloDb.getMovgestTsImporto();
				}
				//verichiamo che: Vincolo.importo<= (accertamento.disponibilitaUtilizzare+vecchioVincolo.importo) 
				if(nuovoImportoVincolo.compareTo(disponibilitaUtilizzareAcc.getDisponibilita().add(oldImportoVincolo))>0){
					listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE.getErrore("gestione vincolo", "a collegare"));
					esitoControlli.setListaErrori(listaErrori);
					return esitoControlli;
				}
				// RM Jira-1804 calcolo la somma dei vincoli sotto, quando fa il confronto 
				// con la disponibilità a liquidare perchè a questo livello passano solo i vincoli da inserire / aggiornare
				// sommaImportoVincoli = sommaImportoVincoli.add(nuovoImportoVincolo);
			}
		}
		
		//Se non siamo stati invocati dall'aggiorna generico ma dal gestisci modifiche:
		if(presentiModifiche){
			sommaImportoVincoli = calcolaTotQuoteVincoli(siacTMovgestTs);
			if(sommaImportoVincoli.compareTo(BigDecimal.ZERO)!=0){
				presenzaVincoli = true;
			}
		}
		
		if(sommaImportoVincoli.compareTo(nuovoImportoAttuale)>0){
			listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE.getErrore("gestione vincolo", "a collegare"));
			esitoControlli.setListaErrori(listaErrori);
			return esitoControlli;
		}
		
		// Per l'impegno
		// o	La somma dei vincoli non deve essere inferiore all'eventuale liquidato:
		// SE SOMMATORIA(Vincolo.importo) < Impegno.importoAttuale e Impegno.disponibilitaImpegno - SOMMATORIA(Subimpegno.disponibilitaSubimpegno)
		// <FIN_ERR_0279: Movimento vincolato e liquidato>
		// BigDecimal disponibilitaModifica = calcolaDisponibilitaImpegnoModifica(siacTMovgestTs.getMovgestTsId(), datiOperazione);
		// non piu' utilizzato, devo chiamare direttamente la function e fare i confronti sulle cifre !!
				
		// SUB
		BigDecimal disponibilitaSubimpegno = BigDecimal.ZERO; 
		
		BigDecimal sommatoriaNuoviImportiAttualiSub = BigDecimal.ZERO; 
		
		log.debug(methodName,"--- calcolo la disponbilità dei subimpegni ---");


		if(resterannoDeiSub && subImpegniRimanentiDopoModifiche!=null && subImpegniRimanentiDopoModifiche.size()>0){
			for(MovimentoGestione it : subImpegniRimanentiDopoModifiche){
							
				if(it!=null && it.getUid()!=0){
					// Jira- 1870 se il sub è in stato annullato non partecipa al calcolo della disponiblità a liquidare
					// anche perchè la function comunque considera solo i sub con stato <> annullato
					// Qui lo stato arriva sempre VALIDO, quindin richiamo il metodo getStato passandogli il sub, cosi lo leggo direttamente da db!
					log.debug(methodName," StatoOperativoMovimentoGestioneSpesa del subimpegno ("+ it.getUid() +") :" +((SubImpegno)it).getStatoOperativoMovimentoGestioneSpesa());
					if(!((SubImpegno)it).getStatoOperativoMovimentoGestioneSpesa().equals(CostantiFin.MOVGEST_STATO_ANNULLATO)){
					
						BigDecimal disponibilitaSubimpegnoIt = BigDecimal.ZERO;
						disponibilitaSubimpegnoIt = impegnoDao.calcolaDisponibilitaALiquidare(it.getUid());
						log.debug(methodName," disponibilitaSubimpegno del subimpegno ("+ it.getUid() +") :" +disponibilitaSubimpegnoIt);
						disponibilitaSubimpegno = disponibilitaSubimpegno.add(disponibilitaSubimpegnoIt);
						log.debug(methodName," disponibilitaSubimpegno totale: "+ disponibilitaSubimpegno);
						
						if(it.getImportoAttuale()!=null){
							sommatoriaNuoviImportiAttualiSub = sommatoriaNuoviImportiAttualiSub.add(it.getImportoAttuale());
						}
						
					}
				}else if(it!=null && it.getUid()==0){
					//IL CASO IN CUI L'ID VALE ZERO (NUOVO SUB IN INSERIMENTO) DEVE COMUNQUE ESSERE VALUTATO
					//PER IL CALCOLO DELLA sommatoriaNuoviImportiAttualiSub:
					if(it.getImportoAttuale()!=null){
						sommatoriaNuoviImportiAttualiSub = sommatoriaNuoviImportiAttualiSub.add(it.getImportoAttuale());
					}
				}
			}
		}
		
		if(nuovoImportoAttuale!=null && nuovoImportoAttuale.compareTo(sommatoriaNuoviImportiAttualiSub)<0){
			//la sommattoria degli importi dei sub non puo' superare quella dell'impegno:
			listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE.getErrore("Aggiornamento Impegno","Importo Attuale"));
			esitoControlli.setListaErrori(listaErrori);
			return esitoControlli;
		}
		
		
		// IMPEGNO
		BigDecimal disponibilitaLiquidare = impegnoDao.calcolaDisponibilitaALiquidare(siacTMovgestTs.getUid());
		
		BigDecimal sommatoriaConfrontoLiquidato = BigDecimal.ZERO;
		if(disponibilitaSubimpegno.compareTo(BigDecimal.ZERO)==0){
			
			// sto lavorando sugli impegni
			log.debug(methodName, " sto lavorando sugli impegni, calcolo la differenza tra importo attuale e il liquidato ");
			sommatoriaConfrontoLiquidato = nuovoImportoAttuale.subtract(disponibilitaLiquidare);
			log.debug(methodName, " nuovoImportoAttuale: "+nuovoImportoAttuale);
			log.debug(methodName, " disponibilitaLiquidare: "+disponibilitaLiquidare);
			log.debug(methodName, " differenza: "+sommatoriaConfrontoLiquidato);
			 
		}else{
			// sto lavorando sui subImpegni
			sommatoriaConfrontoLiquidato = nuovoImportoAttuale.subtract(disponibilitaSubimpegno);
			log.debug(methodName," sto lavorando sui subImpegni ---");
			log.debug(methodName," nuovoImportoAttuale: " +nuovoImportoAttuale);
			log.debug(methodName," disponibilitaSubimpegno: " +disponibilitaSubimpegno);
			log.debug(methodName," sommatoriaConfrontoLiquidato: " +sommatoriaConfrontoLiquidato);
			
		}
		
		//calcoliamo questa sommatoria: Impegno.importoAttuale � Impegno.disponibilitaImpegno - SOMMATORIA(Subimpegno.disponibilitaSubimpegno)
		//chiamiamola sommatoria confronto liquidato:
		//BigDecimal sommatoriaConfrontoLiquidato = nuovoImportoAttuale.subtract(disponibilitaLiquidare).subtract(disponibilitaSubimpegno);
		
		
		// Se non ci sono vincoli il controllo non si fa
		// RM Jira-1804 causa anomalia: la somma dei vincoli non è = alla somma degli importi di tutti i vincoli dell'impegno, 
		// ma solo di quelli che il sistema valuta da inserire/aggiornare
		// quindi calcolo la sommaImportoVincoli partendo dall'oggetto Impengo in input
		
		List<VincoloImpegno> vincoli = impegno.getVincoliImpegno();

		if (vincoli != null)
			if (checkEsistenzaVincoli(siacTMovgestTs.getUid(), datiOperazione))
			{
				sommaImportoVincoli = BigDecimal.ZERO;
				for (VincoloImpegno vincolo : vincoli)
				{
					sommaImportoVincoli = sommaImportoVincoli.add(vincolo.getImporto());
				}
				log.debug("MovimentoGestioneDad",
						"sommaImportoVincoli (in presenza di vincoli deve essere maggiore o uguale al liquidato) : "
								+ sommaImportoVincoli);
				log.debug("MovimentoGestioneDad", "sommatoriaConfrontoLiquidato : "
						+ sommatoriaConfrontoLiquidato);
				if (!presentiModifiche && sommaImportoVincoli != BigDecimal.ZERO
						&& sommaImportoVincoli.compareTo(sommatoriaConfrontoLiquidato) < 0)
				{
					listaErrori.add(ErroreFin.MOV_VINCOLATO_LIQUIDATO.getErrore());
					esitoControlli.setListaErrori(listaErrori);
					return esitoControlli;
				}
			}
		
				
		/*
		 * PARTE SECONDA - CONTROLLI RIGUARDANTI I SUBIMPEGNI
		 */
		List<Errore> listaErroriSub = controlliDiMeritoOperazioneInternaGestisciMovGestSub(richiedente, ente, bilancio, 
				impegno, datiOperazione, impegnoInModificaInfoDto,resterannoDeiSub,subImpegniRimanentiDopoModifiche);
		if(listaErroriSub!=null && listaErroriSub.size()>0){
			esitoControlli.setListaErrori(listaErroriSub);
			return esitoControlli;
		}
		
		if(presenzaVincoli){
			//se non e' completamente vincolato, warning:
			if(sommaImportoVincoli.compareTo(nuovoImportoAttuale)<0){
				//<FIN_INF_0276: Impegno non totalmente vincolato >
				// RM Jira-1808 è richiesto di eliminare il msg (lo commento)
				// esitoControlli.addWarning(ErroreFin.IMPEGNO_NON_TOTALMENTE_VINCOLATO.getErrore());
			}
		}
		
		//Termino restituendo l'oggetto di ritorno: 
        return esitoControlli;
	}
	
	/**
	 * IMPLEMENTARE IN QUESTO METODO I VARI CONTROLLI PREVISTI AL PASSO "3.	Verifica dati trasmessi"
	 * DEL CAPITOLO "2.4.2	Operazione Interna: Gestisce accertamento"
	 * @param richiedente
	 * @param ente
	 * @param bilancio
	 * @param accertamento
	 * @param datiOperazione
	 * @param impegnoInModificaInfoDto
	 * @param capitoliEntrata
	 * @return
	 */
	public <I extends MovimentoGestione> List<Errore> controlliDiMeritoAggiornamentoAccertamentoOperazioneInterna(Richiedente richiedente, Ente ente, Bilancio bilancio, Accertamento accertamento,
			DatiOperazioneDto datiOperazione, ImpegnoInModificaInfoDto impegnoInModificaInfoDto,HashMap<Integer, CapitoloEntrataGestione> capitoliEntrata){
		
		int idEnte = ente.getUid();
		List<Errore> listaErrori = new ArrayList<Errore>();
		SiacTMovgestTsFin siacTMovgestTs = impegnoInModificaInfoDto.getSiacTMovgestTs();
		
		
		//carico l'importo attuale:
		//System.out.println("siacTMovgestTs.getMovgestTsId: " + siacTMovgestTs.getMovgestTsId());
		//System.out.println("datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId: " + datiOperazione.getSiacTEnteProprietario().getEnteProprietarioId());
		BigDecimal importoAttualeOld = estraiImportoAttualeByMovgestTsId(siacTMovgestTs.getMovgestTsId(), datiOperazione);
		
		String statoCod = getStatoCode(siacTMovgestTs, datiOperazione);
		///
		
		BigDecimal nuovoImportoAttuale = accertamento.getImportoAttuale();
		String statoCodeNew = accertamento.getStato().toString();
		statoCodeNew = CostantiFin.convertiStatoMovgest(statoCodeNew);
		if(statoCodeNew==null){
			//in caso di stato indicato non riconosciuto impostiamo come se non ci siano cambiamenti
			statoCodeNew = statoCod;
		}
		
		SubMovgestInModificaInfoDto infoValutati = impegnoInModificaInfoDto.getInfoSubValutati();
		ArrayList<I> subAccertamentiRimanentiDopoModifiche = infoValutati.getRimanentiDopoModifiche();
		
		BigDecimal sommatoriaNuoviImportiAttualiSub = BigDecimal.ZERO; 
		boolean resterannoDeiSub = false;
		if(subAccertamentiRimanentiDopoModifiche!=null && subAccertamentiRimanentiDopoModifiche.size()>0){
			for(MovimentoGestione it : subAccertamentiRimanentiDopoModifiche){
//				String newStatoSub = it.getStato().toString();
				String newStatoSub = ((SubAccertamento)it).getStatoOperativoMovimentoGestioneEntrata();
				if(!CostantiFin.MOVGEST_STATO_ANNULLATO.equalsIgnoreCase(newStatoSub)){
					resterannoDeiSub = true;
					if(it.getImportoAttuale()!=null){
						sommatoriaNuoviImportiAttualiSub = sommatoriaNuoviImportiAttualiSub.add(it.getImportoAttuale());
					}
				}
			}
		}
		
		if(nuovoImportoAttuale!=null && nuovoImportoAttuale.compareTo(sommatoriaNuoviImportiAttualiSub)<0){
			//la sommattoria degli importi dei sub non puo' superare quella dell'impegno:
			listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE.getErrore("Aggiornamento Accertamento","Importo Attuale"));
			return listaErrori;
		}
		
		/*
		 * PARTE PRIMA - CONTROLLI RIGUARDANTI DIRETTAMENTE L'ACCERTAMENTO 
		 */

		//1	STATO IMPEGNO:
		//STATO ACCERTAMENTO:
		//o	Transizione Stati: il passaggio dal vecchio al nuovo stato deve rispettare le transizioni descritte a par. 2.5.4, in caso contrario viene emesso l�errore:
		//<FIN_ERR_0055. Stato Movimento Impossibile (movimento = �Accertamento�, statoOld = Accertamento.stato (su DB), statoNew=accertamento.stato (dati input))>
		boolean controlloCambioStato = controlloCambioStatoImpegnoAccertamento(statoCod,statoCodeNew);
		if(!controlloCambioStato){
			listaErrori.add(ErroreFin.STATO_MOVIMENTO_IMPOSSIBILE.getErrore("Accertamento",statoCod,statoCodeNew));
			return listaErrori;
		}
		//Annullamento: se il nuovo stato � ANNULLATO l�accertamento non deve essere associato a nessun Sub Accertamento valido
		//(la lista �Subaccertamenti da Gestire� deve essere vuota o contenere solo subaccertamenti annullati in caso contrario viene emesso l�errore 
		if(CostantiFin.MOVGEST_STATO_ANNULLATO.equalsIgnoreCase(statoCodeNew)){
			if(resterannoDeiSub){
				listaErrori.add(ErroreFin.ESISTONO_MOVIMENTI_COLLEGATI.getErrore("annullamento"));
				return listaErrori;
			}
		}
	
		
		//2  IMPORTO:controllo: controllo da effettuare se stato operativo � diverso da ANNULLATO
		//Controllo di disponibilit� sul capitolo - applicare la seguente condizione:
		//Se fra i parametri in input � passata la lista di errori vuota o diversamente non � presente in elenco il codice  errore relativo 
		//al controllo di disponibilit� FIN_INF_0062, il sistema verifica che la disponibilit� ad accertare sul capitolo di riferimento non sia superata:
		// (DISPONIBILE a ACCERTARE SUL CAPITOLO + VECCHIO IMPORTO ACCERTAMENTO) >= NUOVO IMPORTO ACCERTAMENTO
		//Nel caso in cui la condizione di disponibilit� non sia rispettata il servizio restituisce di warning:
		//<FIN_INF_0062 - Superamento della disponibilit� >
		if(!CostantiFin.MOVGEST_STATO_ANNULLATO.equalsIgnoreCase(statoCodeNew)){
			CapitoloEntrataGestione capitoloEscGest = capitoliEntrata.get(accertamento.getChiaveCapitoloEntrataGestione());
			if(capitoloEscGest!=null){
				BigDecimal dispAccertareCapitolo = capitoloEscGest.getImportiCapitoloEG().getDisponibilitaAccertareAnno1();
				BigDecimal sommaUno = dispAccertareCapitolo.add(importoAttualeOld);
				if(!(sommaUno.compareTo(nuovoImportoAttuale)>=0)){
					//TODO: NON E' UN ERRORE BLOCCANTE: capire come il servizio deve propagare i WARNING
				}
			}
		}
		
		
		// Controllo di disponibilit� a incassare dell�accertamento
		// da effettuare solo se l�accertamento non possiede sub, verificare che:
		// (VECCHIO IMPORTO ACC -DISPONIBILE A INCASSARE ) <= NUOVO IMPORTO ACC
		// In caso il risultato sia falso il servizio emette l�errore:
		//<FIN_ERR_0001. Disponibilit� Insufficiente (operazione=�Aggiornamento Accertamento�)>
//		if(infoValutati==null || infoValutati.getSubImpegniOld()==null || infoValutati.getSubImpegniOld().size()==0){
		// ATTENZIONE :: 06/11/2014 da effettuare il controllo solo se lo stato e' DEFINITIVO !!!! (non avra' sicuramente subimpegni)
		if(CostantiFin.MOVGEST_STATO_DEFINITIVO.equalsIgnoreCase(statoCod)){
			//Ha dei sub impegni
			DisponibilitaMovimentoGestioneContainer dispIncassare = calcolaDisponibiltaAIncassareAccertamento(siacTMovgestTs, statoCod, idEnte);
			BigDecimal diffUno = importoAttualeOld.subtract(dispIncassare.getDisponibilita());
			if(diffUno.compareTo(nuovoImportoAttuale)>0){
				listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE_ORIG.getErrore("Aggiornamento "));
				return listaErrori;
			}
		}
		
		//4.	ATTO AMMINISTRATIVO:
		//o	StatoAccertamento/Atto: l'associazione con l'atto è modificabile solo se STATO ACCERTAMENTO = PROVVISIORIO, in caso contrario viene emesso l'errore 
		//<FIN_ERR_0029.Modifica Atto Autorizzativo con stato accertamento non provvisorio>
		//o	Stato Atto e Movimento: Se il NUOVO STATO OPERATIVO del movimento è diverso da annullato l'atto deve esistere e non essere ANNULLATO (vedi Ricerca Provvedimento), in caso contrario viene emesso l'errore:
		//<FIN_ERR_0075. Stato Provvedimento non consentito (operazione = Aggiornamento Accertamento, stato = Definitivo)
		List<Errore> listaErroriControlloAttoAmm = 
				controlloAggiornamentoAttoAmministrativo(accertamento.getAttoAmministrativo(), datiOperazione, siacTMovgestTs, statoCodeNew, false, false);
		if(listaErroriControlloAttoAmm!=null && listaErroriControlloAttoAmm.size()>0){
			return listaErroriControlloAttoAmm;
		}
		
		
		//5 	SOGGETTO creditore dell'accertamento(solo se il dato è presente in input):
		//Stato ed esistenza: il soggetto deve esistere ed essere valido in caso contrario deve essere emesso l'errore:
		//< COR_ERR_0012 Entità inesistente  (<nome entità> : soggetto  <chiave ricerca entità> codice>
		//O
		//<COR_ERR_0028 Operazione incompatibile con stato dell'entità (<nome entità> : il soggetto  codice- denominazione  <stato>annullato>
		Errore erroreSoggetto = controlliSoggettoPerAggiorna(accertamento, resterannoDeiSub, datiOperazione);
		if(erroreSoggetto!=null){
			listaErrori.add(erroreSoggetto);
			return listaErrori;
		}
		
		if(accertamento.getClasseSoggetto()!=null){
			//Stato ed esistenza: la classe deve esistere ed essere valida in caso contrario deve essere emesso l�errore:
			//< COR_ERR_0012 Entità inesistente  (<nome entità> : classe soggetto  <chiave ricerca entità> codice>
			//O
			//<COR_ERR_0028 Operazione incompatibile con stato dell'entità (<nome entità> : la classe soggetto   codice ,  <stato>annullato>
			
			SiacDSoggettoClasseFin classeCaricata = getSoggettoClasse(accertamento, datiOperazione);
			if(classeCaricata==null){
				classeCaricata = siacDSoggettoClasseRepository.findOne(Integer.valueOf(accertamento.getClasseSoggetto().getCodice()));
			}			
			//TODO: riusare l'altro e non lavorare per uid!!!!!
			if(classeCaricata==null){
				//inesitente oppure non valido dato che la query si basa sulle date validita'
				listaErrori.add(ErroreCore.ENTITA_INESISTENTE.getErrore("Classe soggett",accertamento.getClasseSoggetto().getCodice()));
				return listaErrori;
			}
		}
		if(resterannoDeiSub){
			//CLASSE SOGGETTO(solo se il dato è presente in input)
			//o	Subaccertamenti: la classe può essere caricata solo se non sono presenti subaccertamenti tra i dati in input, in caso contrario deve essere emesso l'errore 
			//<FIN_ERR_0057. Dato Soggetto Presente (entità:  Classe   valore: codice-descrizione,) >
			if(accertamento.getClasseSoggetto()!=null){
				//non deve essere valorizzata la classe soggetto dell'impegno principale
				listaErrori.add(ErroreFin.DATO_SOGGETTO_PRESENTE.getErrore("Soggetto Classe",accertamento.getClasseSoggetto().getCodice()));
				return listaErrori;
			}
		}
		
		/*
		 * PARTE SECONDA - CONTROLLI RIGUARDANTI I SUB
		 */
		controlliDiMeritoOperazioneInternaGestisciMovGestSub(richiedente, ente, bilancio, 
				accertamento, datiOperazione, impegnoInModificaInfoDto,resterannoDeiSub,subAccertamentiRimanentiDopoModifiche);
		
		//Termino restituendo l'oggetto di ritorno: 
        return listaErrori;
	}
	
	/**
	 * Metodo privato per utilizzo interno in controlliDiMeritoAggiornamentoAccertamentoOperazioneInterna 
	 * e in controlliDiMeritoAggiornamentoImpegnoOperazioneInterna
	 * @param movimentoGestione
	 * @param resterannoDeiSub
	 * @param datiOperazione
	 * @return
	 */
	private Errore controlliSoggettoPerAggiorna(MovimentoGestione movimentoGestione,boolean resterannoDeiSub,DatiOperazioneDto datiOperazione){
		if(movimentoGestione.getSoggetto()!=null  &&  !StringUtilsFin.isEmpty(movimentoGestione.getSoggetto().getCodiceSoggetto()) ){
			SiacTSoggettoFin siacTSoggetto = getSoggetto(movimentoGestione, datiOperazione);
			if(siacTSoggetto==null){
				//inesitente oppure non valido dato che la query si basa sulle date validita'
				return ErroreFin.SOGGETTO_NON_VALIDO.getErrore();
			}
		}
		if(resterannoDeiSub){
			//Subaccertamenti: il soggetto pu� essere caricato solo se non sono presenti subaccertamenti tra i dati in input, in caso contrario deve essere emesso l�errore 
			//<FIN_ERR_0057. Dato Soggetto Presente (entit�:  Soggetto   valore: codice-denominazione,) >
			if(movimentoGestione.getSoggetto()!=null  &&  !StringUtilsFin.isEmpty(movimentoGestione.getSoggetto().getCodiceSoggetto())){
				//non deve essere valorizzato il soggetto dell'impegno principale
				return ErroreFin.DATO_SOGGETTO_PRESENTE.getErrore("Soggetto",movimentoGestione.getSoggetto().getCodiceSoggetto());
			}
		}
		return null;
	}
	
	/**
	 *  Metodo privato per utilizzo interno in controlliDiMeritoAggiornamentoAccertamentoOperazioneInterna 
	 * e in controlliDiMeritoAggiornamentoImpegnoOperazioneInterna
	 * @param richiedente
	 * @param ente
	 * @param bilancio
	 * @param movGest
	 * @param datiOperazione
	 * @param impegnoInModificaInfoDto
	 * @param resterannoDeiSub
	 * @param subsRimanentiDopoModifiche
	 * @return
	 */
	private <I extends MovimentoGestione> List<Errore> controlliDiMeritoOperazioneInternaGestisciMovGestSub(Richiedente richiedente, Ente ente,
			Bilancio bilancio, MovimentoGestione movGest,DatiOperazioneDto datiOperazione,ImpegnoInModificaInfoDto impegnoInModificaInfoDto,
			boolean resterannoDeiSub,ArrayList<I> subsRimanentiDopoModifiche){
		int idEnte = ente.getUid();
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		//IMPEGNO O ACCERTAMENTO
		boolean isImpegno = true;
		Impegno impegno = null;
		Accertamento accertamento = null;
		if(movGest instanceof Accertamento){
			isImpegno = false;
			accertamento = (Accertamento) movGest;
		} else {
			impegno = (Impegno) movGest;
		}
		//
		
		SubMovgestInModificaInfoDto<MovimentoGestione> infoValutati =  impegnoInModificaInfoDto.getInfoSubValutati();
		ArrayList<I> subDaInserire = (ArrayList<I>) infoValutati.getSubImpegniDaInserire();
		ArrayList<I> subDaModificare = (ArrayList<I>) infoValutati.getSubImpegniDaModificare();
		List<SiacTMovgestTsFin> subImpegniOld = infoValutati.getSubImpegniOld();
		ArrayList<SiacTMovgestTsFin> subDaEliminare = infoValutati.getSubImpegniDaEliminare();
		
		
		//1. IMPORTO:da considerare i subimpegni in stato diverso da  ANNULLATO
		//	Disponibilità:   SOMMATORIAsubimpegno.importoAttuale<= Impegno.importoAttuale.
		//	Se non è rispettata la disponibilità viene emesso l'errore: 
		//	<FIN_ERR_0230. Disponibilità Insufficiente (operazione=Gestione Impegno, tipo= a subimpegnare)>
		if(resterannoDeiSub){
			String operazione="Gestione Impegno";
			String tipo= "a subimpegnare";
			if(isImpegno){
				listaErrori = verificaDisponibilitaImpegno((List<SubImpegno>) subsRimanentiDopoModifiche, listaErrori, impegno, operazione, tipo);
			} else {
				listaErrori = verificaDisponibilitaAccertamento((List<SubAccertamento>) subsRimanentiDopoModifiche, listaErrori, accertamento, operazione, tipo);
				operazione="Gestione Accertamento";
				tipo= "a subaccertare";
			}
			if(listaErrori!=null && listaErrori.size()>0){
				return listaErrori;
			}
		}
		
		//2. CANCELLAZIONE: la cancellazione è ammessa solo se lo STATO = PROVVISORIO
		//<FIN_ERR_0055. Stato Movimento Impossibile (movimento = Impegno, statoOld = subimpegno.stato (su DB), statoNew=cancellato)>
		if(subDaEliminare!=null && subDaEliminare.size()>0){
			for(SiacTMovgestTsFin it : subDaEliminare){
				String statoOld = getStatoCode(it, datiOperazione);
				if(!CostantiFin.MOVGEST_STATO_PROVVISORIO.equalsIgnoreCase(statoOld)){
					if(isImpegno){
						listaErrori.add(ErroreFin.STATO_MOVIMENTO_IMPOSSIBILE.getErrore("Impegno",statoOld,"cancellato"));	
					} else {
						listaErrori.add(ErroreFin.STATO_MOVIMENTO_IMPOSSIBILE.getErrore("Accertamento",statoOld,"cancellato"));
					}
					return listaErrori;
				}
			}
		}
		//ArrayList<Integer> elencoIdSogg = new ArrayList<Integer>();
		if(subDaModificare!=null && subDaModificare.size()>0){
			for(I it :subDaModificare){
				SiacTMovgestTsFin siacTMovgestTsSub = DatiOperazioneUtil.getById(subImpegniOld, it.getUid());
				BigDecimal nuovoImportoAttuale = it.getImportoAttuale();
				//3.	Transizione Stati: il passaggio dal vecchio al nuovo stato deve rispettare le transizioni descritte a par. 2.5.5, 
				//in caso contrario viene emesso l�errore:
				//<FIN_ERR_0055. Stato Movimento Impossibile (movimento = �Subimpegno�, statoOld = Subimpegno.stato(su DB), statoNew=subimpegno.stato (dati input))>
				String statoCodSub = getStatoCode(siacTMovgestTsSub, datiOperazione);
				String statoCodeNewSub = it.getStato().toString();
				statoCodeNewSub = CostantiFin.convertiStatoMovgest(statoCodeNewSub);
				if(statoCodeNewSub==null){
					//in caso di stato indicato non riconosciuto impostiamo come se non ci siano cambiamenti
					statoCodeNewSub = statoCodSub;
				}
				boolean controlloCambioStatoSub= controlloCambioStatoSubMovimento(statoCodSub,statoCodeNewSub);
				if(!controlloCambioStatoSub){
					if(isImpegno){
						listaErrori.add(ErroreFin.STATO_MOVIMENTO_IMPOSSIBILE.getErrore("Subimpegno",statoCodSub,statoCodeNewSub));
					} else {
						listaErrori.add(ErroreFin.STATO_MOVIMENTO_IMPOSSIBILE.getErrore("SubAccertamento",statoCodSub,statoCodeNewSub));
					}
					return listaErrori;
				}
		
						
				//5.	ATTOAMMINISTRATIVO:
				//Stato Subimpegno/Atto: l'atto è modificabile solo se STATO SUBIMPEGNO= PROVVISIORIO, in caso contrario viene emesso l'errore 
				//<FIN_ERR_0029.Modifica Atto Autorizzativo con stato impegno non provvisorio>
				
				List<Errore> listaErroriControlloAttoAmm = 
						controlloAggiornamentoAttoAmministrativo(it.getAttoAmministrativo(), datiOperazione, siacTMovgestTsSub, statoCodeNewSub, isImpegno, true);
				if(listaErroriControlloAttoAmm!=null && listaErroriControlloAttoAmm.size()>0){
					return listaErroriControlloAttoAmm;
				}
		
				//6.	SOGGETTO (creditore dell'impegno):
				//Stato Subimpegno/Creditore: il soggetto creditore è modificabile solo se STATO SUBIMPEGNO= PROVVISIORIO, in caso contrario viene emesso l'errore 
				//<FIN_ERR_0152. Modifica Creditore con stato impegno non provvisorio>
				boolean isModificatoSogg = isModificatoSoggettoCreditore(it, siacTMovgestTsSub, datiOperazione);
				if(isModificatoSogg && !CostantiFin.MOVGEST_STATO_PROVVISORIO.equalsIgnoreCase(statoCodeNewSub)){
					//TODO lanciare FIN_ERR_0152.Modifica Atto Autorizzativo con stato impegno non provvisorio
					listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE.getErrore());
					return listaErrori;
				}
		
				//	Stato ed esistenza:(solo se il dato � presente in input) il soggetto deve esistere ed essere valido in caso contrario deve essere emesso l�errore:
				//< COR_ERR_0012 Entit� inesistente  (<nome entit�> : soggetto  <chiave ricerca entit�> codice> o
				//<COR_ERR_0028 Operazione incompatibile con stato dell'entit� (<nome entit�> : il soggetto  codice- denominazione  <stato>annullato>
				if(it.getSoggetto()!=null){
					SiacTSoggettoFin soggettoNew = getSoggetto(it, datiOperazione);
					if(soggettoNew==null){
						listaErrori.add(ErroreFin.SOGGETTO_NON_VALIDO.getErrore());
						return listaErrori;
					} else {
						//Presenza soggetto: la lista �Subimpegni da Gestire� non deve presentare pi� di un subimpegno valido con lo stesso creditore,
						//  in caso contrario viene emesso l�errore:
						//<FIN_ERR_0057. Dato Soggetto Presente (entit�:  Creditore   valore: codice-denominazione,) >
//						Integer idSoggIterato = soggettoNew.getSoggettoId();
						
						//MARZO 2016 COMMENTATO PER CR  FIN - IMPEGNO e ACCERTAMENTO: togliere univocita soggetti nei sub JIRA SIAC-3253:
						/*if(elencoIdSogg.contains(idSoggIterato)){
							listaErrori.add(ErroreFin.DATO_SOGGETTO_PRESENTE.getErrore("Soggetto",it.getSoggetto().getCodiceSoggetto()));
							return listaErrori;
						}
						elencoIdSogg.add(idSoggIterato);*/
						
						
					}
				}
		
		
				//IMPORTO:controllo da effettuare se stato operativo del subimpegno è diverso da ANNULLATO. 
				if(!CostantiFin.MOVGEST_STATO_ANNULLATO.equalsIgnoreCase(statoCodeNewSub)){
					//carico l'importo attuale:
					BigDecimal importoAttualeOld = 
							estraiImportoAttualeByMovgestTsId(siacTMovgestTsSub.getMovgestTsId(), datiOperazione);
					if(importoAttualeOld.compareTo(nuovoImportoAttuale)!=0){
						//Controllo di disponibilit� a liquidare:  se l'importo è stato aggiornato verificare che la DISPONIBILITa' A LIQUIDARE
						//risulti ancora >= 0  ovvero (VECCHIO IMPORTO SUB -DISPONIBILE A LIQUIDARE ) <= NUOVO IMPORTO SUB
						//In caso il risultato sia falso il servizio emette l'errore:
						//<FIN_ERR_0001. Disponibilita' Insufficiente (operazione=Aggiornamento Impegno)>
						if(isImpegno){
							
							//22-12-2014, per CR viene cambiato dispLiquidare con dispSubImpegno:
							//BigDecimal dispLiquidare = calcolaDisponibilitaALiquidare(siacTMovgestTsSub.getUid(), idEnte, datiOperazione);
							DisponibilitaMovimentoGestioneContainer dispSubImpegno = calcolaDisponibilitaImpegnoModifica(siacTMovgestTsSub.getUid(), datiOperazione);
							//
							
							BigDecimal diffUno = importoAttualeOld.subtract(dispSubImpegno.getDisponibilita());
							if(diffUno.compareTo(nuovoImportoAttuale)>0){
								listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE_ORIG.getErrore("Aggiornamento Impegno"));
								return listaErrori;
							}

							
							//FEBBRAIO 2016 viene aggiunta la if stato diverso da provvisorio per questo controllo:
							if(!CostantiFin.MOVGEST_STATO_PROVVISORIO.equalsIgnoreCase(statoCodeNewSub)){
								
								//Controllo di disponibilita' a finanziare:  se l'importo e' stato aggiornato verificare che la DISPONIBILITA' A FINANZIARE
								//risulti ancora >= 0 ovvero   (VECCHIO IMPORTO SUB -DISPONIBILE A SUB ) <= NUOVO IMPORTO IMPEGNO
								//In caso il risultato sia falso il servizio emette l'errore:
								//<FIN_ERR_0001. Disponibilita' Insufficiente (operazione='Aggiornamento Impegno')>
								DisponibilitaMovimentoGestioneContainer dispFinanziare = calcolaDisponibilitaAFinanziare(siacTMovgestTsSub.getUid(), importoAttualeOld, statoCodeNewSub, idEnte, datiOperazione);
								diffUno = importoAttualeOld.subtract(dispFinanziare.getDisponibilita());
								if(diffUno.compareTo(nuovoImportoAttuale)>0){
									listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE_ORIG.getErrore("Aggiornamento Impegno"));
									return listaErrori;

								}
								
							}
								
							
						} else {
							//Controllo di disponibilita' a incassare :  se l'importo è stato aggiornato verificare che 
							//la DISPONIBILIT� A INCASSARE risulti ancora >= 0  ovvero  (VECCHIO IMPORTO SUB -DISPONIBILE A INCASSARE ) <= NUOVO IMPORTO SUB
							//In caso il risultato sia falso il servizio emette l'errore:
							//<FIN_ERR_0001. Disponibilita' Insufficiente (operazione=Aggiornamento subaccertamento)>
							DisponibilitaMovimentoGestioneContainer dispIncassare = calcolaDisponibiltaAIncassareSubAccertamento(siacTMovgestTsSub, statoCodSub, idEnte);
							BigDecimal diffUno = importoAttualeOld.subtract(dispIncassare.getDisponibilita());
							if(diffUno.compareTo(nuovoImportoAttuale)>0){
								listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE_ORIG.getErrore("Aggiornamento Accertamento"));
								return listaErrori;
							}
						}
		
					}
				}
			}
		}
		
		//OLTRE A QUELLI IN MODIFICA QUELLI IN INSERIMENTO:
		if(subDaInserire!=null && subDaInserire.size()>0){
			for(I it :subDaInserire){
				//Stato ed esistenza:(solo se il dato � presente in input) il soggetto deve esistere ed essere valido in caso contrario deve essere emesso l�errore:
				//< COR_ERR_0012 Entit� inesistente  (<nome entit�> : soggetto  <chiave ricerca entit�> codice> o
				//<COR_ERR_0028 Operazione incompatibile con stato dell'entit� (<nome entit�> : il soggetto  codice- denominazione  <stato>annullato>
				if(it.getSoggetto()!=null){
					SiacTSoggettoFin soggettoNew = getSoggetto(it, datiOperazione);
					if(soggettoNew==null){
						listaErrori.add(ErroreFin.SOGGETTO_NON_VALIDO.getErrore());
						return listaErrori;
					} else {
						//Presenza soggetto: la lista �Subimpegni da Gestire� non deve presentare pi� di un subimpegno valido con lo stesso creditore,
						//  in caso contrario viene emesso l�errore:
						//<FIN_ERR_0057. Dato Soggetto Presente (entit�:  Creditore   valore: codice-denominazione,) >
						
						//MARZO 2016 COMMENTATO PER CR  FIN - IMPEGNO e ACCERTAMENTO: togliere univocita soggetti nei sub JIRA SIAC-3253:
						/*
						Integer idSoggIterato = soggettoNew.getSoggettoId();
						if(elencoIdSogg.contains(idSoggIterato)){
							listaErrori.add(ErroreFin.DATO_SOGGETTO_PRESENTE.getErrore("Soggetto",it.getSoggetto().getCodiceSoggetto()));
							return listaErrori;
						}
						elencoIdSogg.add(idSoggIterato);*/
						
						
						
					}
				}
			}
		}
	
		//Termino restituendo l'oggetto di ritorno: 
        return listaErrori;
	}
	
	private List<Errore> controlloAggiornamentoAttoAmministrativo(AttoAmministrativo attoInModifica,DatiOperazioneDto datiOperazione,
			SiacTMovgestTsFin siacTMovgestTsSub, String statoMovimento, boolean isImpegno, boolean isSub){
		List<Errore> listaErrori = new ArrayList<Errore>();
		Integer idEnte = datiOperazione.getSiacTEnteProprietario().getUid();
		SiacRMovgestTsAttoAmmFin vecchioAtto = getAtto(idEnte, datiOperazione.getTs(), siacTMovgestTsSub);
		
		
		
		AttoAmministrativo attoNewSubCaricato = ricaricaAttoAmministrativo(attoInModifica,idEnte);
		if(attoInModifica!=null && attoNewSubCaricato==null){
			//Stato Atto: L'atto deve esistere e non deve essere ANNULLATO (vedi Ricerca Provvedimento), in caso contrario viene emesso l'errore:
			//<FIN_ERR_0075. Stato Provvedimento non consentito (operazione = Aggiornamento Subimpegno, stato = Definitivo)
			if(isImpegno){
				if(isSub){
					//SUB IMPEGNO
					listaErrori.add(ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("Aggiornamento Subimpegno", "Definitivo o Provvisorio"));
				} else {
					//IMPEGNO 
					listaErrori.add(ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("Aggiornamento Impegno", "Definitivo o Provvisorio"));
				}
			} else {
				if(isSub){
					//SUB ACCERTAMENTO
					listaErrori.add(ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("Aggiornamento Subaccertamento", "Definitivo o Provvisorio"));
				} else {
					//ACCERTAMENTO
					listaErrori.add(ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("Aggiornamento Accertamento", "Definitivo o Provvisorio"));
				}
			}
			return listaErrori;
		}

		if(isAttoModificato(vecchioAtto, attoNewSubCaricato)){
			
			if(!CostantiFin.MOVGEST_STATO_PROVVISORIO.equalsIgnoreCase(statoMovimento)){
				
				if(isImpegno){
					//IMPEGNO o SUBIMPEGNO
					listaErrori.add(ErroreFin.MODIFICA_ATTO_AUTORIZZATIVO_CON_STATO_IMPEGNO_NON_PROVVISORIO.getErrore());
				} else {
					//ACCERTAMENTO o SUBACCERTAMENTO
					listaErrori.add(ErroreFin.MODIFICA_ATTO_AUTORIZZATIVO_CON_STATO_ACCERTAMENTO_NON_PROVVISORIO.getErrore());
				}
				return listaErrori;
			}
		}
		return listaErrori;
	}
	
	/**
	 * E' richiamato nelle operazioni interna di aggiornamento impegni/accertamenti
	 * per verificare che il cambiamento di stato richiesto sia accettabile
	 * @param statoOld
	 * @param statoNew
	 * @return
	 */
	private boolean controlloCambioStatoImpegnoAccertamento(String statoOld, String statoNew) {
		boolean esito = true;
		if(StringUtilsFin.isEmpty(statoOld) || StringUtilsFin.isEmpty(statoNew)){
			//1.	c'e' qualcosa di sbagliato nei parametri ricevuti
			return false;
		}
		if(StringUtilsFin.sonoUguali(statoOld, statoNew)){
			//2.	se sono uguali non c'e' passaggio di stato
			return true;
		}
		if(CostantiFin.MOVGEST_STATO_ANNULLATO.equalsIgnoreCase(statoOld)){
			//3.	lo stato annullato non ha sbocchi
			return false;
		}
		if(CostantiFin.MOVGEST_STATO_DEFINITIVO.equalsIgnoreCase(statoOld)){
			//4. da definitivo non si puo' passare a provvisorio o annullato
			if(CostantiFin.MOVGEST_STATO_PROVVISORIO.equalsIgnoreCase(statoNew) ||
					CostantiFin.MOVGEST_STATO_ANNULLATO.equalsIgnoreCase(statoNew)){
				return false;
			}
		}
		if(CostantiFin.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE.equalsIgnoreCase(statoOld)){
			//5. da definitivo non liquidabile non si puo' passare a provvisorio o annullato
			if(CostantiFin.MOVGEST_STATO_PROVVISORIO.equalsIgnoreCase(statoNew) ||
					CostantiFin.MOVGEST_STATO_ANNULLATO.equalsIgnoreCase(statoNew)){
				return false;
			}
		}
		return esito;
	}
	
	/**
	 * E' richiamato nelle operazioni interna di aggiornamento impegni/accertamenti
	 * per verificare che il cambiamento di stato richiesto sia accettabile
	 * @param statoOld
	 * @param statoNew
	 * @return
	 */
	private boolean controlloCambioStatoSubMovimento(String statoOld, String statoNew) {
		boolean esito = true;
		if(StringUtilsFin.isEmpty(statoOld) || StringUtilsFin.isEmpty(statoNew)){
			//1.	c'e' qualcosa di sbagliato nei parametri ricevuti
			return false;
		}
		if(StringUtilsFin.sonoUguali(statoOld, statoNew)){
			//2.	se sono uguali non c'e' passaggio di stato
			return true;
		}
		if(CostantiFin.MOVGEST_STATO_ANNULLATO.equalsIgnoreCase(statoOld)){
			//3.	lo stato annullato non ha sbocchi
			return false;
		}
		if(CostantiFin.MOVGEST_STATO_PROVVISORIO.equalsIgnoreCase(statoOld)){
			//4. da provvisiorio non si puo' passare ad annullato
			if(CostantiFin.MOVGEST_STATO_ANNULLATO.equalsIgnoreCase(statoNew)){
				return false;
			}
		}
		if(CostantiFin.MOVGEST_STATO_DEFINITIVO.equalsIgnoreCase(statoOld)){
			//5. da definitivo non si puo' passare a provvisorio
			if(CostantiFin.MOVGEST_STATO_PROVVISORIO.equalsIgnoreCase(statoNew)){
				return false;
			}
		}
		return esito;
	}

	/**
	 * Richiamato dalla routine di aggiornamento di un impegno/accertamento, serve
	 * per capire se l'atto ha subito modifiche
	 * @param attoOld
	 * @param attoNew
	 * @return
	 */
	private boolean isAttoModificato(SiacRMovgestTsAttoAmmFin attoOld,AttoAmministrativo attoNew){
		boolean modificato = false;
		if(CommonUtil.entrambiNull(attoOld, attoOld)){
			modificato = false;
		} else if(CommonUtil.unoNullunoIstanziato(attoOld, attoOld)){
			modificato = true;
		} else {
			//tutti e due diversi da null
			if(attoOld.getSiacTAttoAmm().getUid().intValue()==attoNew.getUid()){
				modificato = false;
			} else {
				modificato = true;
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return modificato;
	}
	
	/**
	 * Effettua il controllo di disponibilita' sull'impegno indicato rispetto ai sub impegni che si vuole gestire
	 * @param elencoSubImpegni
	 * @param listaErrori
	 * @param impegno
	 * @param operazione
	 * @param tipo
	 * @return
	 */
	private List<Errore> verificaDisponibilitaImpegno(List<SubImpegno> elencoSubImpegni,List<Errore> listaErrori,Impegno impegno,String operazione, String tipo){
		if(elencoSubImpegni!=null && elencoSubImpegni.size()>0){
			BigDecimal importoImpegno = NumericUtils.getZeroIfNull(impegno.getImportoAttuale());
			BigDecimal sommatoriaSub = BigDecimal.ZERO;
			for(SubImpegno subImpIterato : elencoSubImpegni){
				if(subImpIterato!=null){
					
					// JIRA 1872, i sub annullati non concorrono alla sommatoriaSub (ho quinid aggiunto il controllo)
					if(!subImpIterato.getStatoOperativoMovimentoGestioneSpesa().equals(CostantiFin.MOVGEST_STATO_ANNULLATO)){
						BigDecimal importoSubImpegno = NumericUtils.getZeroIfNull(subImpIterato.getImportoAttuale());
						sommatoriaSub = sommatoriaSub.add(importoSubImpegno);
					}
				}
			}
			
			
			if(sommatoriaSub.compareTo(importoImpegno)>0){
				listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE.getErrore(operazione,tipo));
				return listaErrori;
			}
		}
        return listaErrori;
	}

	/**
	 * Effettua il controllo di disponibilita' sull'accertamento indicato rispetto ai sub accertamenti che si vuole gestire
	 * @param elencoSubAccertamenti
	 * @param listaErrori
	 * @param accertamento
	 * @param operazione
	 * @param tipo
	 * @return
	 */
	private List<Errore> verificaDisponibilitaAccertamento(List<SubAccertamento> elencoSubAccertamenti,List<Errore> listaErrori,Accertamento accertamento,String operazione, String tipo){
		if(elencoSubAccertamenti!=null && elencoSubAccertamenti.size()>0){
			BigDecimal importoAccertamento = NumericUtils.getZeroIfNull(accertamento.getImportoAttuale());
			BigDecimal sommatoriaAcc = BigDecimal.ZERO;
			for(SubAccertamento subAccIterato : elencoSubAccertamenti){
				if(subAccIterato!=null){
					BigDecimal importoSubAccertamento = NumericUtils.getZeroIfNull(subAccIterato.getImportoAttuale());
					sommatoriaAcc = sommatoriaAcc.add(importoSubAccertamento);
				}
			}
			if(sommatoriaAcc.compareTo(importoAccertamento)>0){
				listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE.getErrore(operazione,tipo));
				return listaErrori;
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaErrori;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * implementa il passo "3.	Verifica dati trasmessi" del capitolo 2.4.1	Operazione Interna: Inserisce Impegno
	 * @param richiedente
	 * @param ente
	 * @param bilancio
	 * @param impegno
	 * @param datiOperazione
	 * @return
	 */
		
	public EsitoControlliInserimentoMovimentoDto controlliDiMeritoInserimentoImpegnoOperazioneInterna(Richiedente richiedente, Ente ente, Bilancio bilancio, Impegno impegno,DatiOperazioneDto datiOperazione,Integer forzaMaxCodePerDoppia){
		EsitoControlliInserimentoMovimentoDto esitoControlli = new EsitoControlliInserimentoMovimentoDto();
		
		int idEnte = ente.getUid();
		long currMillisec = datiOperazione.getCurrMillisec();
		Timestamp now = new Timestamp(currMillisec);
		List<Errore> listaErrori = new ArrayList<Errore>();
		List<SubImpegno> elencoSubImpegni = impegno.getElencoSubImpegni();
		
		//1 Capitolo: verificare che il Capitolo Spesa esista e sia VALIDO. 
		//Altrimenti restituisce lerrore: < BIL_ERR_0004	Disponibilita Insufficiente (operazione: Inserimento impegno) >
		//2 Importo Impegno: il controllo si differenzia a seconda dell'anno del movimento.  
		// (importo attuale ed importo iniziale all'atto dell'inserimento coincidono)
		//o	Se ANNO BILANCIO  < = ANNO MOVIMENTO <= ANNO BILANCIO + 2 :
		//  Impegno.importoAttuale  <= Capitolo Spesa.ImportiCapitolo UG (anno di competenza = anno movimento).disponibilitaImpegnare
		//o	Negli altri casi   non effettuare controlli di disponibilità.
		//  Nel caso in cui la condizione di diponibilità non sia rispettata il servizio restituisce lerrore:
		//  BIL_ERR_0004. Disponibilit Insufficiente(operazione=Inserimento Impegno)>
		
		int annoMovimento = impegno.getAnnoMovimento();
		int annoBilancio = bilancio.getAnno();
		BigDecimal importoAttuale = NumericUtils.getZeroIfNull(impegno.getImportoAttuale());
		
		
		//UNIVOCITA RISPETTO AL NUMERO INSERITO MANUALMENTE:
		Errore erroreUnivocita = controlloUnivocitaMovimento(forzaMaxCodePerDoppia, annoBilancio, annoMovimento, idEnte, CostantiFin.MOVGEST_TIPO_IMPEGNO);
		if(erroreUnivocita!=null){
			listaErrori.add(erroreUnivocita);
			esitoControlli.setListaErrori(listaErrori);
			return esitoControlli;
		}
		//
		
		if(annoBilancio<=annoMovimento && annoMovimento<=(annoBilancio+2)){
			BigDecimal disponibilitaImpegnare=BigDecimal.ZERO;
			
			// verifico la disponibilita per i primi tre anni
			if(annoMovimento==annoBilancio){
				disponibilitaImpegnare = NumericUtils.getZeroIfNull(impegno.getCapitoloUscitaGestione().getImportiCapitoloUG().getDisponibilitaImpegnareAnno1());
			}else if(annoMovimento == (annoBilancio + 1)){
				disponibilitaImpegnare = NumericUtils.getZeroIfNull(impegno.getCapitoloUscitaGestione().getImportiCapitoloUG().getDisponibilitaImpegnareAnno2());
			}else if(annoMovimento == (annoBilancio + 2)){
				disponibilitaImpegnare = NumericUtils.getZeroIfNull(impegno.getCapitoloUscitaGestione().getImportiCapitoloUG().getDisponibilitaImpegnareAnno3());
			}
			
		
			if(importoAttuale.compareTo(disponibilitaImpegnare)>0){
				//errore sulle disponibilita
				listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE.getErrore("Impegno",""+annoMovimento));
				esitoControlli.setListaErrori(listaErrori);
				return esitoControlli;
			}
		}
		
		//3
		//	Importo Subimpegno: (solo se presente) 
		//SOMMATORIA Subimpegno.importoAttuale <= Impegno.importoAttuale(Impegno)
		//In caso il risultato sia falso il servizio restituisce l'errore:
		//FIN_ERR_0004. Disponibilita' Insufficiente (operazione=Inserimento Impegno con Subimpegni)>
		String operazione="Inserimento Impegno";
		String tipo= "Importo Attuale";
		listaErrori = verificaDisponibilitaImpegno(elencoSubImpegni, listaErrori, impegno, operazione, tipo);
		
		//4
		// Atto Amministrativo: verifica che lo stato del Provvedimento non sia annullato (vedi Ricerca Provvedimento)  
		//		AttoAmministrativo attoAmministrativo = impegno.getAttoAmministrativo();
		//		int annoAtto = attoAmministrativo.getAnno();
		//		int numeroAtto = attoAmministrativo.getNumero();
		SiacTAttoAmmFin siacTAttoAmm = getAttoAmministrativo(impegno, idEnte);
		if(siacTAttoAmm!=null){
			Integer idAttoAmm = siacTAttoAmm.getAttoammId();
			List<SiacRAttoAmmStatoFin> siacRAttoAmmStatol = siacRAttoAmmStatoRepository.findValidoByIdAttoAmm(idAttoAmm, now);
			if(siacRAttoAmmStatol!=null && siacRAttoAmmStatol.size()>0){
				SiacRAttoAmmStatoFin siacRAttoAmmStato = siacRAttoAmmStatol.get(0);
				SiacDAttoAmmStatoFin siacDAttoAmmStato = siacRAttoAmmStato.getSiacDAttoAmmStato();
				if(CostantiFin.ATTO_AMM_STATO_ANNULLATO.equals(siacDAttoAmmStato.getAttoammStatoCode())){
					// nell'analisi manca il messaggio di errore da dover lanciare!!!!!!!!!!!!!!!!!!
					listaErrori.add(ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("inserisce impegno","non annullato"));
					esitoControlli.setListaErrori(listaErrori);
					return esitoControlli;
				}
			}
		}
		
		//5
		//	Soggetto: se presente verifica che il soggetto sia valido 
		//	Subimpegno: se e' richiesto l'inserimento di subimpegni non devono essere memorizzati, nell'impegno principale, ne' il Soggetto ne' la Classe Soggetto,
		//in caso contrario viene emesso l'errore
		//<FIN_ERR_0057 Dato Soggetto Presente (entita':  Creditore o Classe a seconda dell'entita' riscontrata  valore:
		//per Creditore riportare codice-denominazione,. per Classe: codice-descrizione,) >
		if(impegno.getSoggetto()!=null  &&  !StringUtilsFin.isEmpty(impegno.getSoggetto().getCodiceSoggetto()) ){
			SiacTSoggettoFin siacTSoggetto = getSoggetto(impegno, datiOperazione);
			if(siacTSoggetto==null){
				//inesitente oppure non valido dato che la query si basa sulle date validita'
				listaErrori.add(ErroreFin.SOGGETTO_NON_VALIDO.getErrore());
				esitoControlli.setListaErrori(listaErrori);
				return esitoControlli;
			}
		}
		if(impegno.getElencoSubImpegni()!=null && impegno.getElencoSubImpegni().size()>0){
			if(impegno.getSoggetto()!=null  &&  !StringUtilsFin.isEmpty(impegno.getSoggetto().getCodiceSoggetto())){
				//non deve essere valorizzato il soggetto dell'impegno principale
				listaErrori.add(ErroreFin.DATO_SOGGETTO_PRESENTE.getErrore("Soggetto",impegno.getSoggetto().getCodiceSoggetto()));
				esitoControlli.setListaErrori(listaErrori);
				return esitoControlli;
			}
			if(impegno.getClasseSoggetto()!=null){
				//non deve essere valorizzata la classe soggetto dell'impegno principale
				listaErrori.add(ErroreFin.DATO_SOGGETTO_PRESENTE.getErrore("Soggetto Classe",impegno.getClasseSoggetto().getCodice()));
				esitoControlli.setListaErrori(listaErrori);
				return esitoControlli;
			}
		}
		
		//6
		//	Soggetto nei Subimpegni: se i subimpegni sono piu' di uno, tutti i soggetti devono essere diversi 
		//(non posso inserire piu' subimpegni con lo stesso soggetto), in caso contrario viene emesso l'errore:
		//	<FIN_ERR_0057 Dato Soggetto Presente (entita':  Creditore   valore: codice-denominazione,) >
		if(impegno.getElencoSubImpegni()!=null && impegno.getElencoSubImpegni().size()>0){
			ArrayList<Integer> elencoIdSogg = new ArrayList<Integer>();
			for(SubImpegno subImp : impegno.getElencoSubImpegni()){
				SiacTSoggettoFin siacTSoggetto = getSoggetto(subImp, datiOperazione);
				if(siacTSoggetto!=null){
					Integer idSoggIterato = siacTSoggetto.getSoggettoId();
					if(elencoIdSogg.contains(idSoggIterato)){
						listaErrori.add(ErroreFin.DATO_SOGGETTO_PRESENTE.getErrore("Soggetto",subImp.getSoggetto().getCodiceSoggetto()));
						esitoControlli.setListaErrori(listaErrori);
						return esitoControlli;
					}
					elencoIdSogg.add(idSoggIterato);
				}
			}
		}
		
		
		// controllo disponibilita su vincoli
		if(impegno.getVincoliImpegno()!=null && !impegno.getVincoliImpegno().isEmpty()){
			
			for (VincoloImpegno vincoloImpegno : impegno.getVincoliImpegno()) {
				if(vincoloImpegno.getImporto()==null || vincoloImpegno.getImporto().compareTo(BigDecimal.ZERO)==0){
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("importo "));
					esitoControlli.setListaErrori(listaErrori);
					return esitoControlli;
				}else if(vincoloImpegno.getAccertamento().getDisponibilitaUtilizzare()!=null &&
						 vincoloImpegno.getImporto().compareTo(vincoloImpegno.getAccertamento().getDisponibilitaUtilizzare())>0){
					listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE.getErrore("Vincolo Impegno","utilizzare"));
					esitoControlli.setListaErrori(listaErrori);
					return esitoControlli;  
				}
			}
		}
		
		// Controlli per doppia gestione
		
		String bilCode = estraeFaseDiBilancio(bilancio, idEnte,  datiOperazione);
			
		if(bilCode.equals(CostantiFin.BIL_FASE_OPERATIVA_PREDISPOSIZIONE_CONSUNTIVO) &&
				impegno.getAnnoMovimento()>bilancio.getAnno()){
			listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("Anno movimento", "impegno futuro non ammesso in doppia gestione"));
			esitoControlli.setListaErrori(listaErrori);
			return esitoControlli;
		}
		
		//PROGETTO: controllo da effettuare se stato operativo � diverso da ANNULLATO il progetto collegato all�impegno deve esiste e non essere annullato
		List<Errore> listaErroriProgetto = controlloValiditaProgetto(impegno, datiOperazione);
		if(listaErroriProgetto!=null && listaErroriProgetto.size()>0){
			esitoControlli.setListaErrori(listaErroriProgetto);
			return esitoControlli;
		}
		
		//VINCOLO:per ogni vincolo da inserire:
			//o	 l'accertamento deve esistere e non essere annullato
			//o	Vincolo.importo<= accertamento.disponibilitaUtilizzare e SOMMATORIA Vincolo.importo<= Impegno.importoAttuale, in caso contrario viene emesso l'errore:
			//<FIN_ERR_0230: Disponibilit� Insufficiente (operazione = gestione vincolo , <tipo >= a collegare)>
		List<VincoloImpegno> vincoliDaInserire = impegno.getVincoliImpegno();
		BigDecimal sommaImportoVincoli = BigDecimal.ZERO;
		boolean verificaDisponibilitaVincoli = false;
		if(vincoliDaInserire!=null && vincoliDaInserire.size()>0){
			
			verificaDisponibilitaVincoli = true;
			
			for(VincoloImpegno vIt : vincoliDaInserire){
				if(vIt!=null){
					Accertamento acc = vIt.getAccertamento();
					BigDecimal importoVincoloIterato = vIt.getImporto();
					
					//vediamo se l'accertamento esiste ed e' valido:
					annoMovimento = acc.getAnnoMovimento();
					boolean accertamentoValido = false;
					SiacTMovgestFin siacTMovgestAcc = siacTMovgestRepository.ricercaSiacTMovgestPk(idEnte, Integer.toString(annoBilancio), annoMovimento, acc.getNumeroBigDecimal(), CostantiFin.MOVGEST_TIPO_ACCERTAMENTO);	
					SiacTMovgestTsFin siacTMovgestTsAcc = null;
					
					log.debug("MovimentoGestioneDad.controlliDiMeritoAggiornamentoImpegnoOperazioneInterna","Parametri ricerca accertamento ");
					log.debug("MovimentoGestioneDad.controlliDiMeritoAggiornamentoImpegnoOperazioneInterna","idEnte: "+idEnte);
					log.debug("MovimentoGestioneDad.controlliDiMeritoAggiornamentoImpegnoOperazioneInterna","annoBilancio: "+Integer.toString(annoBilancio));
					log.debug("MovimentoGestioneDad.controlliDiMeritoAggiornamentoImpegnoOperazioneInterna","annoMovimento: "+annoMovimento);
					log.debug("MovimentoGestioneDad.controlliDiMeritoAggiornamentoImpegnoOperazioneInterna","numero accertamento: "+acc.getNumeroBigDecimal());
					log.debug("MovimentoGestioneDad.controlliDiMeritoAggiornamentoImpegnoOperazioneInterna","MOVGEST_TIPO_ACCERTAMENTO: "+CostantiFin.MOVGEST_TIPO_ACCERTAMENTO.toString());
					if(null!=siacTMovgestAcc){
						siacTMovgestTsAcc = siacTMovgestTsRepository.findMovgestTsByMovgest(idEnte, now, siacTMovgestAcc.getMovgestId()).get(0);
					}else{
						listaErrori.add(ErroreCore.ENTITA_NON_TROVATA.getErrore("Accertamento", acc.getNumeroBigDecimal() + " - anno: "+ annoMovimento ));
						esitoControlli.setListaErrori(listaErrori);
						return esitoControlli;
					}
					if(siacTMovgestTsAcc!=null){
						//esiste
						String statoAccertamento = getStatoCode(siacTMovgestTsAcc, datiOperazione);
						if(!CostantiFin.MOVGEST_STATO_ANNULLATO.equalsIgnoreCase(statoAccertamento)){
							//non annullato
							accertamentoValido = true;
						}
					}
					if(!accertamentoValido){
						listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE.getErrore("gestione vincolo", "a collegare"));
						esitoControlli.setListaErrori(listaErrori);
						return esitoControlli;
					}
					
					DisponibilitaMovimentoGestioneContainer disponibilitaUtilizzareAcc = calcolaDisponibilitaAUtilizzare(siacTMovgestTsAcc, datiOperazione);
					
					
					if(importoVincoloIterato.compareTo(disponibilitaUtilizzareAcc.getDisponibilita())>0){
						listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE.getErrore("gestione vincolo", "a collegare"));
						esitoControlli.setListaErrori(listaErrori);
						return esitoControlli;
					}
					sommaImportoVincoli = sommaImportoVincoli.add(importoVincoloIterato);
				}
			}
		}
		
		
		
		
		if(sommaImportoVincoli.compareTo(importoAttuale)>0){
			listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE.getErrore("gestione vincolo", "a collegare"));
			esitoControlli.setListaErrori(listaErrori);
			return esitoControlli;
		}
		
		if(verificaDisponibilitaVincoli){
			//se non e' completamente vincolato, warning:
			if(sommaImportoVincoli.compareTo(importoAttuale)<0){
				//<FIN_INF_0276: Impegno non totalmente vincolato >
				// RM Jira-1808 è richiesto di eliminare il msg (lo commento)
				// esitoControlli.addWarning(ErroreFin.IMPEGNO_NON_TOTALMENTE_VINCOLATO.getErrore());
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return esitoControlli;
	}
	
	
	/**
	 * 23 / DICEMBRE / 2015
	 * Claudio Picco
	 * E' stato introdotto in seguito alla nuova possibilita di inserire manualmente il numero di movimento
	 * per i movimenti degli anni prima da importare a mano nel sistema...
	 * @param forzaMaxCodePerDoppia
	 * @param annoBilancio
	 * @param annoMovimento
	 * @param idEnte
	 * @param tipoMovimento
	 * @return
	 */
	private Errore controlloUnivocitaMovimento(Integer forzaMaxCodePerDoppia,int annoBilancio,int annoMovimento, int idEnte, String tipoMovimento){
		if(forzaMaxCodePerDoppia!=null && forzaMaxCodePerDoppia.intValue()>0){
			String annoBilancioString = Integer.toString(annoBilancio);
			BigDecimal numeroImpegno = new  BigDecimal(forzaMaxCodePerDoppia);
			SiacTMovgestFin siacTMovgest = siacTMovgestRepository.ricercaSiacTMovgestPk(idEnte, annoBilancioString, annoMovimento, numeroImpegno, tipoMovimento);
			if(siacTMovgest!=null && siacTMovgest.getUid()>0){
				//ESISTE!!
				if(CostantiFin.MOVGEST_TIPO_IMPEGNO.equals(tipoMovimento)){
					return ErroreCore.ATTRIBUTO_GIA_PRESENTE.getErrore("Impegno",annoBilancio + "/" + annoMovimento + "/" + numeroImpegno);
				} else {
					return ErroreCore.ATTRIBUTO_GIA_PRESENTE.getErrore("Accertamento",annoBilancio + "/" + annoMovimento + "/" + numeroImpegno);
				}
			}
		}
		return null;
	}
	
	/**
	 * Ritorna NULL se TUTTO OK
	 * @param impegno
	 * @param datiOperazione
	 * @return
	 */
	private List<Errore> controlloValiditaProgetto(Impegno impegno, DatiOperazioneDto datiOperazione){
		//PROGETTO: il progetto collegato all�impegno deve esiste e non essere annullato
		List<Errore> listaErrori = null;
		if(impegno.getProgetto()!=null && !StringUtilsFin.isEmpty((impegno.getProgetto().getCodice()))){
			
			
			SiacTProgrammaFin siacTProgramma = getProgetto(impegno, datiOperazione);
			boolean programmaOK = false;
			if(siacTProgramma!=null){
				//check se stato annullato
				SiacRProgrammaStatoFin relValida = DatiOperazioneUtil.getValido(siacTProgramma.getSiacRProgrammaStatos(), null);
				if(relValida!=null && !CostantiFin.D_PROGRAMMA_STATO_ANNULLATO.equalsIgnoreCase(relValida.getSiacDProgrammaStato().getProgrammaStatoCode())){
					//NON ANNULLATO
					programmaOK = true;
				}
			}
			if(!programmaOK){
				listaErrori = new ArrayList<Errore>();
				listaErrori.add(ErroreFin.VALORE_NON_VALIDO.getErrore("Progetto", "progetto inesistente o non valido"));
			}
		}
		return listaErrori;
	}

	/**
	 * implementa il passo "3.	Verifica dati trasmessi" del capitolo 2.4.1	Operazione Interna: Inserisce Accertamento
	 * @param richiedente
	 * @param ente
	 * @param bilancio
	 * @param accertamento
	 * @param datiOperazione
	 * @return
	 */
	public List<Errore> controlliDiMeritoInserimentoAccertamentoOperazioneInterna(Richiedente richiedente, Ente ente, Bilancio bilancio, Accertamento accertamento, DatiOperazioneDto datiOperazione,Integer forzaMaxCodePerDoppia){
		int idEnte = ente.getUid();
		long currMillisec = datiOperazione.getCurrMillisec();
		Timestamp now = new Timestamp(currMillisec);
		List<Errore> listaErrori = new ArrayList<Errore>();
		List<SubAccertamento> elencoSubAccertamenti = accertamento.getElencoSubAccertamenti();
		
		//1 Capitolo: verificare che il Capitolo Spesa esista e sia VALIDO. 
		//Altrimenti restituisce lerrore: < BIL_ERR_0004	Disponibilit Insufficiente (operazione: Inserimento impegno) >
		/*
		 * non serve piu' questa chiamata a Db 
		 * 
		SiacTBilElemFin siacTBilElem = getCapitoloEntrataGestione(accertamento, idEnte);
		if(siacTBilElem==null){
//			listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE.getErrore());
//			return listaErrori;
		}
		*/
		
		//2 	Importo Impegno: il controllo si differenzia a seconda dellanno del movimento.  
		//(importo attuale ed importo iniziale allatto dellinserimento coincidono)
		//o	Se ANNO BILANCIO  < = ANNO MOVIMENTO <= ANNO BILANCIO + 2 :
		//Impegno.importoAttuale  <= Capitolo Spesa.ImportiCapitolo UG (anno di competenza = anno movimento).disponibilitaImpegnare
		//o	Negli altri casi   non effettuare controlli di disponibilit.
		//Nel caso in cui la condizione di diponibilit non sia rispettata il servizio restituisce lerrore:
		//BIL_ERR_0004. Disponibilit Insufficiente(operazione=Inserimento Impegno)>
		int annoMovimento = accertamento.getAnnoMovimento();
		int annoBilancio = bilancio.getAnno();
		
		//UNIVOCITA RISPETTO AL NUMERO INSERITO MANUALMENTE:
		Errore erroreUnivocita = controlloUnivocitaMovimento(forzaMaxCodePerDoppia, annoBilancio, annoMovimento, idEnte, CostantiFin.MOVGEST_TIPO_ACCERTAMENTO);
		if(erroreUnivocita!=null){
			listaErrori.add(erroreUnivocita);
			return listaErrori;
		}
		//
		
		
		if(annoBilancio<=annoMovimento && annoMovimento<=(annoBilancio+2)){
			BigDecimal importoAttuale = NumericUtils.getZeroIfNull(accertamento.getImportoAttuale());
			BigDecimal disponibilitaAccertare=BigDecimal.ZERO;
			
			// verifico la disponibilita per i primi tre anni
			if(annoMovimento==annoBilancio){
				disponibilitaAccertare = NumericUtils.getZeroIfNull(accertamento.getCapitoloEntrataGestione().getImportiCapitoloEG().getDisponibilitaAccertareAnno1());
			}else if(annoMovimento == (annoBilancio + 1)){
					disponibilitaAccertare = NumericUtils.getZeroIfNull(accertamento.getCapitoloEntrataGestione().getImportiCapitoloEG().getDisponibilitaAccertareAnno2());
			}else if(annoMovimento == (annoBilancio + 2)){
					disponibilitaAccertare = NumericUtils.getZeroIfNull(accertamento.getCapitoloEntrataGestione().getImportiCapitoloEG().getDisponibilitaAccertareAnno3());
			}
			
//			if(accertamento.getCapitoloEntrataGestione()!=null && accertamento.getCapitoloEntrataGestione().getImportiCapitoloEG()!=null){
//				disponibilitaAccertare = NumericUtils.getZeroIfNull(accertamento.getCapitoloEntrataGestione().getImportiCapitoloEG().getDisponibilitaAccertare());
//			}
			// Jira-1847
			// su richiesta di Vitelli si elimina l'errore bloccante sul salva dell'accertamento con capitolo con disponibilità insufficiente
			/*if(importoAttuale.compareTo(disponibilitaAccertare)>0){
                // errore sulle disponibilita 
				listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE.getErrore("Accertamento", ""+annoMovimento));
				return listaErrori;
			}*/
		}
		
		//3
		//	Importo Subimpegno: (solo se presente) 
		//SOMMATORIA Subimpegno.importoAttuale <= Impegno.importoAttuale(Impegno)
		//In caso il risultato sia falso il servizio restituisce l'errore:
		//FIN_ERR_0004. Disponibilita' Insufficiente (operazione=Inserimento Impegno con Subimpegni)>
		String operazione="Inserimento Accertamento";
		String tipo= "Importo Attuale";
		listaErrori = verificaDisponibilitaAccertamento(elencoSubAccertamenti, listaErrori, accertamento, operazione, tipo);
		
		//4
		// Atto Amministrativo: verifica che lo stato del Provvedimento non sia annullato (vedi Ricerca Provvedimento)  
		SiacTAttoAmmFin siacTAttoAmm = getAttoAmministrativo(accertamento, idEnte);
		if(siacTAttoAmm!=null){
			Integer idAttoAmm = siacTAttoAmm.getAttoammId();
			List<SiacRAttoAmmStatoFin> siacRAttoAmmStatol = siacRAttoAmmStatoRepository.findValidoByIdAttoAmm(idAttoAmm, now);
			if(siacRAttoAmmStatol!=null && siacRAttoAmmStatol.size()>0){
				SiacRAttoAmmStatoFin siacRAttoAmmStato = siacRAttoAmmStatol.get(0);
				SiacDAttoAmmStatoFin siacDAttoAmmStato = siacRAttoAmmStato.getSiacDAttoAmmStato();
				if(CostantiFin.ATTO_AMM_STATO_ANNULLATO.equals(siacDAttoAmmStato.getAttoammStatoCode())){
					//TODO: nell'analisi manca il messaggio di errore da dover lanciare!!!!!!!!!!!!!!!!!!
					listaErrori.add(ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("inserisce accertamento","non annullato"));
					return listaErrori;
				}
			}
		}
		
		//5
		//	Soggetto: se presente verifica che il soggetto sia valido 
		//	Subimpegno: se e' richiesto l'inserimento di subimpegni non devono essere memorizzati, nell'impegno principale, ne' il Soggetto ne' la Classe Soggetto,
		//in caso contrario viene emesso l'errore
		//<FIN_ERR_0057 Dato Soggetto Presente (entita':  Creditore o Classe a seconda dell'entita' riscontrata  valore:
		//per Creditore riportare codice-denominazione,. per Classe: codice-descrizione,) >
		if(accertamento.getSoggetto()!=null  &&  !StringUtilsFin.isEmpty(accertamento.getSoggetto().getCodiceSoggetto()) ){
			SiacTSoggettoFin siacTSoggetto = getSoggetto(accertamento, datiOperazione);
			if(siacTSoggetto==null){
				//inesitente oppure non valido dato che la query si basa sulle date validita'
				listaErrori.add(ErroreFin.SOGGETTO_NON_VALIDO.getErrore());
				return listaErrori;
			}
		}
		if(accertamento.getElencoSubAccertamenti()!=null && accertamento.getElencoSubAccertamenti().size()>0){
			if(accertamento.getSoggetto()!=null){
				//non deve essere valorizzato il soggetto dell'impegno principale
				listaErrori.add(ErroreFin.DATO_SOGGETTO_PRESENTE.getErrore("Soggetto", accertamento.getSoggetto().getCodiceSoggetto()));
				return listaErrori;
			}
			if(accertamento.getClasseSoggetto()!=null){
				//non deve essere valorizzata la classe soggetto dell'impegno principale
				listaErrori.add(ErroreFin.DATO_SOGGETTO_PRESENTE.getErrore("Soggetto Classe", accertamento.getClasseSoggetto().getCodice()));
				return listaErrori;
			}
		}
		
		//6
		//	Soggetto nei Subimpegni: se i subimpegni sono piu' di uno, tutti i soggetti devono essere diversi 
		//(non posso inserire piu' subimpegni con lo stesso soggetto), in caso contrario viene emesso l'errore:
		//	<FIN_ERR_0057 Dato Soggetto Presente (entita':  Creditore   valore: codice-denominazione,) >
		if(accertamento.getElencoSubAccertamenti()!=null && accertamento.getElencoSubAccertamenti().size()>0){
			ArrayList<Integer> elencoIdSogg = new ArrayList<Integer>();
			for(SubAccertamento subAcc : accertamento.getElencoSubAccertamenti()){
				SiacTSoggettoFin siacTSoggetto = getSoggetto(subAcc, datiOperazione);
				if(siacTSoggetto!=null){
					Integer idSoggIterato = siacTSoggetto.getSoggettoId();
					if(elencoIdSogg.contains(idSoggIterato)){
						listaErrori.add(ErroreFin.DATO_SOGGETTO_PRESENTE.getErrore("Soggetto", subAcc.getSoggetto().getCodiceSoggetto()));
						return listaErrori;
					}
					elencoIdSogg.add(idSoggIterato);
				}
			}
		}
		
		String bilCode = estraeFaseDiBilancio(bilancio, idEnte, datiOperazione);
		
		if(bilCode.equals(CostantiFin.BIL_FASE_OPERATIVA_PREDISPOSIZIONE_CONSUNTIVO) && accertamento.getAnnoMovimento()>bilancio.getAnno()){
			listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("Anno movimento", "accertamento futuro non ammesso in doppia gestione"));
		}
		
		//Termino restituendo l'oggetto di ritorno: 
        return listaErrori;
	}
	
	/**
	 * E' il metodo unificato che effettua l'inserimento di impegni e' accertamenti.
	 * @param richiedente
	 * @param ente
	 * @param bilancio
	 * @param impegno
	 * @param datiOperazioneDto
	 * @param forzaMaxCodePerDoppia
	 * @param infoVincoliValutati 
	 * @return
	 */
	public MovimentoGestione inserisceMovimento(Richiedente richiedente, Ente ente, Bilancio bilancio, MovimentoGestione impegno,DatiOperazioneDto datiOperazioneDto,Integer forzaMaxCodePerDoppia, MovimentoInInserimentoInfoDto movimentoInInserimentoInfoDto){
		int idEnte = ente.getUid();
		SiacDAmbitoFin  siacDAmbitoPerCode = datiOperazioneDto.getSiacDAmbito();
		Integer idAmbito = siacDAmbitoPerCode.getAmbitoId();
		///////////////////////////////////////IMPEGNO PRINCIPALE//////////////////////////////////////////////////
		// nuova gestione MAX
		
		long nuovoCode = 0;
		if(forzaMaxCodePerDoppia!=null && forzaMaxCodePerDoppia.intValue()>0){
			nuovoCode = forzaMaxCodePerDoppia.longValue();
		} else {
			nuovoCode = getMaxCode(impegno instanceof Impegno ? ProgressivoType.IMPEGNO : ProgressivoType.ACCERTAMENTO, idEnte, idAmbito, richiedente.getAccount().getNome(), impegno.getAnnoMovimento());
		}

		if(CostantiFin.GESTIONE_PARERE_FINANZIARIO.equals(ente.getGestioneLivelli().get(TipologiaGestioneLivelli.GESTIONE_PARERE_FINANZIARIO))){
			impegno.setParereFinanziario(Boolean.FALSE);
		}else impegno.setParereFinanziario(Boolean.TRUE);
		
		SiacTMovgestFin siacTMovgest = inserisciMovimentoGestione(impegno, datiOperazioneDto,bilancio, new BigDecimal(nuovoCode));   // movgestNumero);
		
		String code = siacTMovgest.getMovgestNumero().toString();
		SiacTMovgestTsFin siacTMovgestTs = inserisciMovgestTs(siacTMovgest, impegno, datiOperazioneDto,code,null,idAmbito,bilancio);
		
		
		///////////////////////////////////////SUB IMPEGNI//////////////////////////////////////////////////
		List<SubImpegno> listaSubImpegni = null;
		List<SubAccertamento> listaSubAccertamenti = null;
		if(impegno instanceof Impegno){
			listaSubImpegni = ((Impegno) impegno).getElencoSubImpegni();
		} else if(impegno instanceof Accertamento){
			listaSubAccertamenti =  ((Accertamento) impegno).getElencoSubAccertamenti();
		}
		inserisciSubMovimenti(listaSubImpegni,listaSubAccertamenti, siacTMovgest, datiOperazioneDto, siacTMovgestTs, forzaMaxCodePerDoppia,bilancio);
		
		// eventuale presenza di vincoli
		if(impegno instanceof Impegno){
			//solo per tipologia impegno
			List<VincoloImpegno> vincoliDaInserire = ((Impegno)impegno).getVincoliImpegno();
			//se ci sono dei vincoli:
			if(vincoliDaInserire!=null && vincoliDaInserire.size()>0){
				for(VincoloImpegno vincoloImpegno: vincoliDaInserire){
					//invoco la routine di salvataggio per il singolo vincolo iterato:
					salvaVincoloImpegno(vincoloImpegno, datiOperazioneDto, siacTMovgestTs);
				}
			}
		}
		
		
		//GESTIONE (EVENTUALE) DOPPIA GESTIONE:
		doppiaGestioneInInserimentoMovGest(bilancio, impegno, datiOperazioneDto, ente, richiedente, nuovoCode,movimentoInInserimentoInfoDto);
			
		// RM 28/07/2015
		// Agggiunto per l'innesto fin nel registro della generale:
		// l'impengo appena inserito era incompleto di tutte le informazioni utili poi alla registrazione per gen 
		// perchè in siacTMovgestEntityToImpegnoModel serve l'id della mov ts  
		if(siacTMovgestTs!=null){
			List<SiacTMovgestTsFin> siacTMovgestTsList = new ArrayList<SiacTMovgestTsFin>();
			siacTMovgestTsList.add(siacTMovgestTs);
			siacTMovgest.setSiacTMovgestTs(siacTMovgestTsList);
		}
		if(impegno instanceof Impegno){
			Impegno impegnoInserito = (Impegno) map(siacTMovgest, Impegno.class, FinMapId.SiacTMovgest_Impegno);	
			impegnoInserito = EntityToModelConverter.siacTMovgestEntityToImpegnoModel(siacTMovgest, impegnoInserito,null);
			return impegnoInserito;
			
		} else if(impegno instanceof Accertamento){
			Accertamento accertamentoInserito = null;
			accertamentoInserito = (Accertamento) map(siacTMovgest, Accertamento.class, FinMapId.SiacTMovgest_Accertamento);	
			accertamentoInserito = EntityToModelConverter.siacTMovgestEntityToAccertamentoModel(siacTMovgest, accertamentoInserito);
			return accertamentoInserito;
			
		}else{
			return null;
		}
	}
	
	/**
	 * Routine che si occupa della "doppia gestione" in fase di inserimento di un nuovo movimento.
	 * (sotto-metodo di inserisciImpegno)
	 * @param bilancio
	 * @param movimento
	 * @param datiOperazioneDto
	 * @param ente
	 * @param richiedente
	 * @param nuovoCode
	 * @param movimentoInInserimentoInfoDto 
	 */
	private void doppiaGestioneInInserimentoMovGest(Bilancio bilancio,MovimentoGestione movimento,DatiOperazioneDto datiOperazioneDto,Ente ente,Richiedente richiedente,long nuovoCode, MovimentoInInserimentoInfoDto movimentoInInserimentoInfoDto){
		boolean inserireDoppiaGestione = inserireDoppiaGestione(bilancio, movimento, datiOperazioneDto);
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		if(inserireDoppiaGestione){
			// ripasso la data e ora attuale altrimenti l'indice va in errore
			datiOperazioneDto.setCurrMillisec(System.currentTimeMillis());
			//Inserisce Impegno Residuo
			//Il servizio inserisce in archivio l'impegno residuo con i medesimi dati dell'impegno appena inserito al passo 4 (numero compreso) ma con:
			//Bilancio.anno = anno + 1
			Bilancio bilancioPiuUno = bilancio;
			bilancioPiuUno.setAnno(bilancio.getAnno() + 1);
			String bilancioPiuUnoString = Integer.toString(bilancioPiuUno.getAnno());
			
			Integer annoCapitoloIncrementato = null;
			// ricalcolo il capitolo anno + 1
			if(movimento instanceof Impegno){
				
			  annoCapitoloIncrementato =  ((Impegno) movimento).getCapitoloUscitaGestione().getAnnoCapitolo();
			  annoCapitoloIncrementato = annoCapitoloIncrementato + 1;
			  
			  ((Impegno) movimento).getCapitoloUscitaGestione().setAnnoCapitolo(annoCapitoloIncrementato);
			  
			  
			  
			  
			  //08-01-2015 Doppia gestione vincoli *****************************************************************
			  // in inserimento di un impegno
			  //Eseguo il flusso relativo ad un impegno inserito del paragrafo gest imp in doppia gest
			  List<VincoloImpegno> listaVincoli = ((Impegno) movimento).getVincoliImpegno();
			  if(listaVincoli!=null && listaVincoli.size()>0){
				  //ci sono dei vincoli
				  for(VincoloImpegno vincoloIterato : listaVincoli){
					  BigDecimal importoVincoloIt = vincoloIterato.getImporto();
					  //il delta dato che siamo in inserimento di un nuovo impegno e' uguale all'importo del vincolo:
					  BigDecimal deltaVincolo = importoVincoloIt;
					  
					  MovGestInfoDto infoMov = movimentoInInserimentoInfoDto.findInfoAccertamento(vincoloIterato.getAccertamento().getUid());
					  Accertamento accCollegato = (Accertamento) infoMov.getMovGestCompleto();
					  //Accertamento accCollegato = vincoloIterato.getAccertamento();
					  
					  SiacTMovgestFin siacTMovgestAccResiduo = infoMov.getSiacTMovgestResiduo();
					  //SiacTMovgestFin siacTMovgestAccResiduo = movimentoGestioneDao.findAccertamento(idEnte, accCollegato.getAnnoMovimento(), accCollegato.getNumero(), bilancioPiuUnoString, getNow());
					  Integer idMovGestAccResiduo = aggiornaImportoAccertamentoResiduoPerVincoloModificato(siacTMovgestAccResiduo, datiOperazioneDto, richiedente, accCollegato, deltaVincolo, ente, bilancioPiuUno);
					  
					  //Per ogni vincolo bisogna puntare all'accertamento residuo in modo che l'op interna di ins impegno
					  //che verra' invocata piu' avanti crei il corretto vincolo residuo:
					  vincoloIterato.getAccertamento().setUid(idMovGestAccResiduo);
					  vincoloIterato.setUid(0);
					  //
				  }
			  }
			  ///                                 *****************************************************************
			  
			  
			  
			}else if(movimento instanceof Accertamento){
				
				annoCapitoloIncrementato =  ((Accertamento) movimento).getCapitoloEntrataGestione().getAnnoCapitolo();
				annoCapitoloIncrementato = annoCapitoloIncrementato + 1;
				
				((Accertamento) movimento).getCapitoloEntrataGestione().setAnnoCapitolo(annoCapitoloIncrementato);
			}
			
			/*
			 *  MOCK
			 * 
			 */
			movimento.setDescrizione(movimento.getDescrizione()+" RIBALTAMENTO");
			
			List<SiacTBilFin> siacTBilList = siacTBilRepository.getValidoByAnno(idEnte, bilancioPiuUnoString, datiOperazioneDto.getTs());
			if(siacTBilList!=null && siacTBilList.size()>0 && siacTBilList.get(0)!=null){
				if(movimento instanceof Impegno){
					//IMPEGNO
					operazioneInternaInserisceImpegno(richiedente, ente, bilancioPiuUno, movimento, datiOperazioneDto,new Integer(new Long(nuovoCode).intValue()),null);
				} else {
					//ACCERTAMENTO
					((Accertamento)movimento).setImportoUtilizzabile(BigDecimal.ZERO);
					operazioneInternaInserisceAccertamento(richiedente, ente, bilancioPiuUno, movimento, datiOperazioneDto,new Integer(new Long(nuovoCode).intValue()));
				}
			} else {
				//SE NON E' PRESENTE IL BILANCIO DELL'ANNO SUCCESSIVO CHE SI FA???
			}
		}
		//
	}
	
	/**
	 * L'implementazione di questo metodo si trova in ImpegnoDad e AccertamentoDad in funzione dell'istanza corrente del dad
	 * che fanno l'override di questo metodo vuoto.
	 * @param richiedente
	 * @param ente
	 * @param bilancio
	 * @param primoImpegnoDaInserire
	 * @param datiOperazione
	 * @param forzaMaxCodePerDoppia
	 * @return
	 */
//	public EsitoInserimentoMovimentoGestioneDto operazioneInternaInserisceMovimento(Richiedente richiedente, Ente ente, Bilancio bilancio, MovimentoGestione primoImpegnoDaInserire, DatiOperazioneDto datiOperazione,Integer forzaMaxCodePerDoppia){
//		return null;
//	}
	
	public EsitoInserimentoMovimentoGestioneDto operazioneInternaInserisceAccertamento(Richiedente richiedente, Ente ente, Bilancio bilancio, MovimentoGestione primoImpegnoDaInserire, DatiOperazioneDto datiOperazione,Integer forzaMaxCodePerDoppia){
		EsitoInserimentoMovimentoGestioneDto esito = new EsitoInserimentoMovimentoGestioneDto();
		List<Errore> listaErrori = this.controlliDiMeritoInserimentoAccertamentoOperazioneInterna(richiedente,  ente, bilancio, (Accertamento) primoImpegnoDaInserire, datiOperazione, forzaMaxCodePerDoppia);
		// setto eventuali errori
		esito.setListaErrori(listaErrori);
		// se non ci sono errori procedo con inserimento
		if(listaErrori==null || listaErrori.size()==0){
			Accertamento accertamento  = (Accertamento) this.inserisceMovimento(richiedente, ente, bilancio, primoImpegnoDaInserire, datiOperazione,forzaMaxCodePerDoppia,null);
			// setto accertamento inserito
			esito.setMovimentoGestione(accertamento);
		}
		//Termino restituendo l'oggetto di ritorno: 
		return esito;
	}
	
	public EsitoInserimentoMovimentoGestioneDto operazioneInternaInserisceImpegno(Richiedente richiedente, Ente ente, Bilancio bilancio, MovimentoGestione primoImpegnoDaInserire, DatiOperazioneDto datiOperazione,Integer forzaMaxCodePerDoppia, MovimentoInInserimentoInfoDto infoInserimento){
		EsitoInserimentoMovimentoGestioneDto esito = new EsitoInserimentoMovimentoGestioneDto();
		EsitoControlliInserimentoMovimentoDto esitoControlli = 	this.controlliDiMeritoInserimentoImpegnoOperazioneInterna(richiedente,  ente, bilancio, (Impegno) primoImpegnoDaInserire, datiOperazione,forzaMaxCodePerDoppia);
		// setto eventuali errori
		List<Errore> listaErrori = esitoControlli.getListaErrori();
		esito.setListaErrori(listaErrori);
		esito.addWarning(esitoControlli.getListaWarning());
		// se non ci sono errori procedo con inserimento
		if(listaErrori==null || listaErrori.size()==0){
			Impegno impegno = (Impegno) this.inserisceMovimento(richiedente, ente, bilancio, primoImpegnoDaInserire,datiOperazione,forzaMaxCodePerDoppia,infoInserimento);
			// setto impegno inserito
			esito.setMovimentoGestione(impegno);
		}
		//Termino restituendo l'oggetto di ritorno: 
        return esito;
		
	}
	
	/**
	 * Routine interna a inserisciImpegno: inserisce i subImpegni o i subAccertamenti indicati 
	 * per l'impegno appena inserito dal chiamante
	 * @param listaSubImpegni
	 * @param listaSubAccertamenti
	 * @param siacTMovgest
	 * @param datiOperazioneDto
	 * @param siacTMovgestTs
	 * @param forzaMaxCodePerDoppia
	 * @return
	 */
	private ArrayList<SubImpegnoDto> inserisciSubMovimenti(List<SubImpegno> listaSubImpegni,List<SubAccertamento> listaSubAccertamenti,
			SiacTMovgestFin siacTMovgest,DatiOperazioneDto datiOperazioneDto ,SiacTMovgestTsFin siacTMovgestTs,Integer forzaMaxCodePerDoppia, Bilancio bilancio){
		SiacDAmbitoFin  siacDAmbitoPerCode = datiOperazioneDto.getSiacDAmbito();
		Integer idAmbito = siacDAmbitoPerCode.getAmbitoId();
		ArrayList<SubImpegnoDto> listaSubImpegniInseriti = new ArrayList<SubImpegnoDto>();
		
		//ci chiediamo se siamo in doppia gest:
		boolean inRibaltamento = false;
		if(forzaMaxCodePerDoppia!=null && forzaMaxCodePerDoppia.intValue()>0){
			inRibaltamento = true;
		}
		
		int numermoSubImpegno = 1;
		int numeroSubDaMax = 1;
		if(forzaMaxCodePerDoppia!=null){
			numeroSubDaMax = forzaMaxCodePerDoppia;
		}
		if(listaSubImpegni!=null && listaSubImpegni.size()>0){
			for(SubImpegno subImpegnoIterato: listaSubImpegni){
				String codeSub = null;
				if(!inRibaltamento){
					codeSub = Integer.toString(numermoSubImpegno);
				} else {
					if(subImpegnoIterato.getNumeroBigDecimal()!=null && subImpegnoIterato.getNumeroBigDecimal().intValue()>0){
						codeSub = subImpegnoIterato.getNumeroBigDecimal().toString();
					} else {
						codeSub = Integer.toString(numeroSubDaMax);
						numeroSubDaMax++;
					}
				}
				SiacTMovgestTsFin siacTMovgestTs_Sub =inserisciMovgestTs(siacTMovgest, subImpegnoIterato, datiOperazioneDto,codeSub,siacTMovgestTs,idAmbito,bilancio);
				SubImpegnoDto infoSubImpegno = new SubImpegnoDto();
				infoSubImpegno.setSiacTMovgestTs(siacTMovgestTs_Sub);
				infoSubImpegno.setSubImpegno(subImpegnoIterato);
				infoSubImpegno.setNumermoSubImpegno(numermoSubImpegno);
				listaSubImpegniInseriti.add(infoSubImpegno);
				numermoSubImpegno++;
			}
		} else if(listaSubAccertamenti!=null && listaSubAccertamenti.size()>0){
			for(SubAccertamento subImpegnoIterato: listaSubAccertamenti){
				String codeSub = null;
				if(!inRibaltamento){
					codeSub = Integer.toString(numermoSubImpegno);
				} else {
					if(subImpegnoIterato.getNumeroBigDecimal()!=null && subImpegnoIterato.getNumeroBigDecimal().intValue()>0){
						codeSub = subImpegnoIterato.getNumeroBigDecimal().toString();
					} else {
						codeSub = Integer.toString(numeroSubDaMax);
						numeroSubDaMax++;
					}
				}
				SiacTMovgestTsFin siacTMovgestTs_Sub =inserisciMovgestTs(siacTMovgest, subImpegnoIterato, datiOperazioneDto,codeSub,siacTMovgestTs,idAmbito,bilancio);
				SubImpegnoDto infoSubImpegno = new SubImpegnoDto();
				infoSubImpegno.setSiacTMovgestTs(siacTMovgestTs_Sub);
				infoSubImpegno.setSubAccertamento(subImpegnoIterato);
				infoSubImpegno.setNumermoSubImpegno(numermoSubImpegno);
				listaSubImpegniInseriti.add(infoSubImpegno);
				numermoSubImpegno++;
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaSubImpegniInseriti;
	}
	
	/**
	 * Metodo di comodo: dato un SiacTMovgestTsFin legge e restituisce il codice del suo stato corrente
	 * @param siacTMovgestTs
	 * @param datiOperazioneDto
	 * @return
	 */
	private String getStatoCode(SiacTMovgestTsFin siacTMovgestTs, DatiOperazioneDto datiOperazioneDto){
		SiacDMovgestStatoFin siacDMovgestStato = getStato(siacTMovgestTs, datiOperazioneDto);
		if(siacDMovgestStato!=null){
			return siacDMovgestStato.getMovgestStatoCode();
		} else {
			return null;
		}
	}
	
	/**
	 * Metodo di comodo: dato un SiacTMovgestTsFin legge e restituisce l'intero oggetto del suo stato corrente
	 * restituendo un SiacDMovgestStatoFin
	 * @param siacTMovgestTs
	 * @param datiOperazioneDto
	 * @return
	 */
	protected SiacDMovgestStatoFin getStato(SiacTMovgestTsFin siacTMovgestTs, DatiOperazioneDto datiOperazioneDto){
		SiacDMovgestStatoFin stato = null;
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		List<SiacRMovgestTsStatoFin> listaRMovgestTsStato = siacRMovgestTsStatoRepository.findValido(idEnte, datiOperazioneDto.getTs(), siacTMovgestTs.getMovgestTsId());
		stato = getStato(siacTMovgestTs, datiOperazioneDto, listaRMovgestTsStato);

		//Termino restituendo l'oggetto di ritorno: 
        return stato;
	}
	
	/**
	 * Metodo di comodo: dato un SiacTMovgestTsFin legge e restituisce l'intero oggetto del suo stato corrente
	 * restituendo un SiacDMovgestStatoFin.
	 * Non accede al database ma riceve in input la lista degli SiacRMovgestTsStatoFin tra i quali accedere allo stato corrente.
	 * Serve nel caso in cui dal chiamante gia' si abbia a disposizione tale lista e si voglia evitare un'ulteriore
	 * interrogazione al database.
	 * @param siacTMovgestTs
	 * @param datiOperazioneDto
	 * @param listaRMovgestTsStato
	 * @return
	 */
	public SiacDMovgestStatoFin getStato(SiacTMovgestTsFin siacTMovgestTs, DatiOperazioneDto datiOperazioneDto,List<SiacRMovgestTsStatoFin> listaRMovgestTsStato){
		SiacDMovgestStatoFin stato = null;
		SiacRMovgestTsStatoFin siacRMovgestTsStato = listaRMovgestTsStato.get(0);
		if(siacRMovgestTsStato!=null){
			stato = siacRMovgestTsStato.getSiacDMovgestStato();
		}
		//Termino restituendo l'oggetto di ritorno: 
        return stato;
	}

	/**
	 * Metodo di comodo che restituisce il codice della fase operativa di un certo Bilnacio indicato
	 * @param bilancio
	 * @param idEnte
	 * @param datiOperazioneDto
	 * @return
	 */
	private String estraeFaseDiBilancio(Bilancio bilancio, Integer idEnte, DatiOperazioneDto datiOperazioneDto){
		String bilCode = "";
		
		String annoBil = Integer.toString(bilancio.getAnno());
		SiacTBilFin siacTBil =  siacTBilRepository.getValidoByAnno(idEnte, annoBil, datiOperazioneDto.getTs()).get(0);
		
		SiacRBilFaseOperativaFin siacRBilFaseOperativaValido = null;
		List<SiacRBilFaseOperativaFin> listaSiacRBilFaseOperativa = siacRBilFaseOperativaRepository.findValido(idEnte, siacTBil.getBilId(), datiOperazioneDto.getTs());
		
		if(listaSiacRBilFaseOperativa!=null && listaSiacRBilFaseOperativa.size()>0){
			for(SiacRBilFaseOperativaFin siacRBilFaseOperativaIterato : listaSiacRBilFaseOperativa){
				if(siacRBilFaseOperativaIterato.getDataFineValidita()==null){
					//Se valido:
					siacRBilFaseOperativaValido = siacRBilFaseOperativaIterato;
				}
			}			
		}
		
		if(siacRBilFaseOperativaValido!=null){
			bilCode = siacRBilFaseOperativaValido.getSiacDFaseOperativa().getFaseOperativaCode();
		}
		
		//Termino restituendo l'oggetto di ritorno: 
        return bilCode;
	}
	
	/**
	 * Metodo vuoto, viene implementato in AccertamentoDad e ImpegnoDad
	 * @param stato
	 * @return
	 */
	protected abstract boolean checkStato(String stato);
	
	/**
	 * Override dell'omonimo metodo dell'abstractfindad, e' coinvolto nei meccanismi di valutazione 
	 * se siamo in doppia gestione o meno (vedi inserireDoppiaGestione)
	 */
	@Override
	protected boolean checkStatoEntita(String stato) {
		return checkStato(stato);
	}

	/**
	 * Override dell'omonimo metodo dell'abstractfindad, e' coinvolto nei meccanismi di valutazione 
	 * se siamo in doppia gestione o meno (vedi inserireDoppiaGestione)
	 */
	@Override
	protected String determinaStatoEntita(Integer idEnte, Entita entita) {
		return determinaStatoImpegno(idEnte, (MovimentoGestione)entita);
	}

	/**
	 * Routine interna a inserisciImpegno: inserisce l'impegno o l'accertamento
	 * @param impegno
	 * @param datiOperazioneDto
	 * @param bilancio
	 * @param movgestNumero
	 * @return
	 */
	private SiacTMovgestFin inserisciMovimentoGestione(MovimentoGestione impegno, DatiOperazioneDto datiOperazioneDto, Bilancio bilancio,BigDecimal movgestNumero){
		SiacTMovgestFin siacTMovgest = null;
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		Integer annoGest = impegno.getAnnoMovimento();
		String annoBil = Integer.toString(bilancio.getAnno());
		List<SiacTBilFin> siacTBilList = siacTBilRepository.getValidoByAnno(idEnte, annoBil, datiOperazioneDto.getTs());
		if(siacTBilList!=null && siacTBilList.size()>0 && siacTBilList.get(0)!=null){
			siacTMovgest = new SiacTMovgestFin();
			siacTMovgest = DatiOperazioneUtil.impostaDatiOperazioneLogin(siacTMovgest, datiOperazioneDto, siacTAccountRepository);
			SiacTBilFin siacTBil =  siacTBilList.get(0);
			siacTMovgest.setSiacTBil(siacTBil);
			siacTMovgest.setMovgestAnno(annoGest);
			siacTMovgest.setMovgestNumero(movgestNumero);
			if(!StringUtilsFin.isEmpty(impegno.getDescrizione())){
				siacTMovgest.setMovgestDesc(impegno.getDescrizione());
			}
			String costanteTipo = null;
			if(impegno instanceof ImpegnoAbstract){
				costanteTipo =  CostantiFin.MOVGEST_TIPO_IMPEGNO;
			} else if(impegno instanceof AccertamentoAbstract){
				costanteTipo =  CostantiFin.MOVGEST_TIPO_ACCERTAMENTO;
			}
			SiacDMovgestTipoFin siacDMovgestTipo =  siacDMovgestTipoRepository.findValidoByCode(idEnte, datiOperazioneDto.getTs(), costanteTipo).get(0);
			
			siacTMovgest.setSiacDMovgestTipo(siacDMovgestTipo);
			
			
			siacTMovgest.setParereFinanziario(impegno.getParereFinanziario());
			
			//salvo sul db:
			siacTMovgest = siacTMovgestRepository.saveAndFlush(siacTMovgest);
			
			//save capitolo:
			if(impegno instanceof Impegno){
				SiacTBilElemFin siacTBilElem = getCapitoloUscitaGestione((Impegno) impegno, idEnte);
				if(siacTBilElem!=null){
					SiacRMovgestBilElemFin siacRMovgestBilElem = new SiacRMovgestBilElemFin();
					siacRMovgestBilElem = DatiOperazioneUtil.impostaDatiOperazioneLogin(siacRMovgestBilElem, datiOperazioneDto, siacTAccountRepository);
					siacRMovgestBilElem.setSiacTBilElem(siacTBilElem);
					siacRMovgestBilElem.setSiacTMovgest(siacTMovgest);
					//salvo sul db:
					siacRMovgestBilElem = siacRMovgestBilElemRepository.saveAndFlush(siacRMovgestBilElem);
				}
			} else if(impegno instanceof Accertamento){
				SiacTBilElemFin siacTBilElem = getCapitoloEntrataGestione((Accertamento) impegno, idEnte);
				if(siacTBilElem!=null){
					SiacRMovgestBilElemFin siacRMovgestBilElem = new SiacRMovgestBilElemFin();
					siacRMovgestBilElem = DatiOperazioneUtil.impostaDatiOperazioneLogin(siacRMovgestBilElem, datiOperazioneDto, siacTAccountRepository);
					siacRMovgestBilElem.setSiacTBilElem(siacTBilElem);
					siacRMovgestBilElem.setSiacTMovgest(siacTMovgest);
					//salvo sul db:
					siacRMovgestBilElem = siacRMovgestBilElemRepository.saveAndFlush(siacRMovgestBilElem);
				}
			}
			
		}
		//Termino restituendo l'oggetto di ritorno: 
        return siacTMovgest;
	}
	
	/**
	 * Questa routine inserisce un record SiacTMovgestTsFin e nelle tabelle accessorie.
	 * E' coinvolta nell'inserimento di impegni, accertamenti, subimpegni e subaccertamenti perche'
	 * tutti questi oggetti prevedono una riga su tale tabella.
	 * @param siacTMovgest
	 * @param movimentoGestione
	 * @param datiOperazioneDto
	 * @param code
	 * @param padre
	 * @param idAmbito
	 * @return
	 */
	private SiacTMovgestTsFin inserisciMovgestTs(SiacTMovgestFin siacTMovgest, MovimentoGestione movimentoGestione, DatiOperazioneDto datiOperazioneDto, String code,SiacTMovgestTsFin padre,Integer idAmbito, Bilancio bilancio){
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		
		SiacTMovgestTsFin siacTMovgestTs = new SiacTMovgestTsFin();
		siacTMovgestTs = DatiOperazioneUtil.impostaDatiOperazioneLogin(siacTMovgestTs, datiOperazioneDto, siacTAccountRepository);
		
		siacTMovgestTs.setMovgestTsCode(code);//incrementale per sub ; numero calc per imp
		
		if(!StringUtilsFin.isEmpty(movimentoGestione.getDescrizione())){
			siacTMovgestTs.setMovgestTsDesc(movimentoGestione.getDescrizione());
		}
		
		Timestamp dataScadenza = TimingUtils.convertiDataInTimeStamp(movimentoGestione.getDataScadenza());
		siacTMovgestTs.setMovgestTsScadenzaData(dataScadenza);
		siacTMovgestTs.setOrdine("nd");//TODO ISSUE --> non valorizzare diventara' nullable
		
		siacTMovgestTs.setSiacTMovgest(siacTMovgest);
		
		//DETERMININIAMO SE STIAMO INSERENDO UNA TESTATA O UN SUBIMPEGNO:
		String codiceTipoTs = null;
		
		if(padre!=null){
			codiceTipoTs = CostantiFin.MOVGEST_TS_TIPO_SUBIMPEGNO;
			siacTMovgestTs.setLivello(2);
			siacTMovgestTs.setMovgestTsIdPadre(padre.getMovgestTsId());
		}else{
			codiceTipoTs = CostantiFin.MOVGEST_TS_TIPO_TESTATA;
			siacTMovgestTs.setLivello(1);
		}
		SiacDMovgestTsTipoFin siacDMovgestTsTipo = siacDMovgestTsTipoRepository.findValidoByCode(idEnte, datiOperazioneDto.getTs(), codiceTipoTs).get(0);
		//////////////////////////////////////////////////////////////
		
		siacTMovgestTs.setSiacDMovgestTsTipo(siacDMovgestTsTipo);
		//salvo sul db:
		siacTMovgestTs = siacTMovgestTsRepository.saveAndFlush(siacTMovgestTs);
		
		AttributoTClassInfoDto attributoInfo = new AttributoTClassInfoDto();
		attributoInfo.setTipoOggetto(OggettoDellAttributoTClass.T_MOVGEST_TS);
		attributoInfo.setSiacTMovgestTs(siacTMovgestTs);
		
		//
		SiacTMovgestTsDetFin iniziale = saveImporto(null,movimentoGestione.getImportoIniziale(), datiOperazioneDto, siacTMovgestTs, CostantiFin.MOVGEST_TS_DET_TIPO_INIZIALE);
		SiacTMovgestTsDetFin attuale = saveImporto(null,movimentoGestione.getImportoAttuale(), datiOperazioneDto, siacTMovgestTs, CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE);
		SiacTMovgestTsDetFin utilizzabile = null;
		if(movimentoGestione instanceof AccertamentoAbstract){
			//SE l'accertamento è di competenza o futuro (annoMovimento >= annoBilancio): 
			//valorizzare l'attributo  importoUtilizzabile = importo attuale 
			//Se nell'input importoUtilizzabile è nullo e l'accertamento è residuo ((annoMovimento < annoBilancio)
			//valorizzare l'attributo  importoUtilizzabile = 0 
			BigDecimal importoUtilizzabile = BigDecimal.ZERO;
			if(movimentoGestione.getAnnoMovimento()>=bilancio.getAnno()){
				//CASO: l'accertamento è di competenza o futuro (annoMovimento >= annoBilancio)
				importoUtilizzabile = movimentoGestione.getImportoIniziale(); 
			} else {
				//CASO: l'accertamento è residuo ((annoMovimento < annoBilancio)
				if(((Accertamento)movimentoGestione).getImportoUtilizzabile()==null){
					//CASO: nell'input importoUtilizzabile è nullo 
					importoUtilizzabile = BigDecimal.ZERO;
				} else {
					//CASO: nell'input importoUtilizzabile NON è nullo 
					importoUtilizzabile = ((Accertamento)movimentoGestione).getImportoUtilizzabile();
				}
			}
			utilizzabile = saveImporto(null,importoUtilizzabile, datiOperazioneDto, siacTMovgestTs, CostantiFin.MOVGEST_TS_DET_TIPO_UTILIZZABILE);
		}
		
		siacTMovgestTs.setSiacTMovgestTsDets(toList(iniziale,attuale,utilizzabile));
		//
		
		//STATO
		String statoImpegnoCode = determinaStatoImpegno(idEnte, movimentoGestione);
		SiacRMovgestTsStatoFin siacRMovgestTsStato = saveStatoImpegno(datiOperazioneDto, statoImpegnoCode,siacTMovgestTs);
		siacTMovgestTs.setSiacRMovgestTsStatos(toList(siacRMovgestTsStato));
		////////
		
		List<SiacRMovgestTsAttrFin> listaAttrInseriti = new ArrayList<SiacRMovgestTsAttrFin>();
		
		//CIG:
		if(movimentoGestione instanceof ImpegnoAbstract){
			String cig = ( (ImpegnoAbstract) movimentoGestione).getCig();
			if(!StringUtilsFin.isEmpty(cig)){
				listaAttrInseriti.add((SiacRMovgestTsAttrFin) salvaAttributoCig(attributoInfo, datiOperazioneDto, cig));
			}
		}
			
		//FLAG_DA_RIACCERTAMENTO
		listaAttrInseriti.add((SiacRMovgestTsAttrFin) salvaAttributoTAttr(attributoInfo, datiOperazioneDto, movimentoGestione.isFlagDaRiaccertamento(), CostantiFin.T_ATTR_CODE_FLAG_DA_RIACCERTAMENTO));
		
		//durc
		listaAttrInseriti.add((SiacRMovgestTsAttrFin) salvaAttributoTAttr(attributoInfo, datiOperazioneDto, movimentoGestione.isFlagSoggettoDurc(), CostantiFin.T_ATTR_CODE_FLAG_SOGGETTO_DURC));
		
		//FLAG_VALIDATO
		listaAttrInseriti.add((SiacRMovgestTsAttrFin) salvaAttributoTAttr(attributoInfo, datiOperazioneDto, movimentoGestione.isValidato(), CostantiFin.T_ATTR_CODE_FLAG_VALIDATO));
		
		
		//ACCERTAMENTO con FATTURA PREVISTA (FLAG FATTURA)
		if (movimentoGestione instanceof AccertamentoAbstract) {
			listaAttrInseriti.add((SiacRMovgestTsAttrFin) salvaAttributoTAttr(attributoInfo, datiOperazioneDto,
					((AccertamentoAbstract) movimentoGestione).isFlagFattura(),
					CostantiFin.T_ATTR_CODE_FLAG_PREVISTA_FATTURA));
			listaAttrInseriti.add((SiacRMovgestTsAttrFin) salvaAttributoTAttr(attributoInfo, datiOperazioneDto,
					((AccertamentoAbstract) movimentoGestione).isFlagCorrispettivo(),
					CostantiFin.T_ATTR_CODE_FLAG_PREVISTO_CORRISPETTIVO));
		}
		
		//FLAG ATTIVA GSA:
		listaAttrInseriti.add((SiacRMovgestTsAttrFin) salvaAttributoTAttr(attributoInfo, datiOperazioneDto, movimentoGestione.isFlagAttivaGsa(), CostantiFin.T_ATTR_CODE_FLAG_ATTIVA_GSA));
		
		//ANNO_FINANZIAMENTO
		if(movimentoGestione instanceof ImpegnoAbstract){
			int annoF = ( (ImpegnoAbstract) movimentoGestione).getAnnoFinanziamento();
			listaAttrInseriti.add((SiacRMovgestTsAttrFin) salvaAttributoTAttr(attributoInfo, datiOperazioneDto, annoF, CostantiFin.T_ATTR_CODE_ANNO_FINANZIAMENTO));
		}
		
		
		//ACC_AUTO
		if(movimentoGestione instanceof AccertamentoAbstract){
			boolean automatico=( (AccertamentoAbstract) movimentoGestione).isAutomatico();
			SiacRMovgestTsAttrFin accAuto = (SiacRMovgestTsAttrFin) salvaAttributoTAttr(attributoInfo, datiOperazioneDto, automatico, CostantiFin.T_ATTR_CODE_ACC_AUTO);
			listaAttrInseriti.add(accAuto);
		}

		//NUMERO_ACC_FINANZIAMENTO
		if(movimentoGestione instanceof ImpegnoAbstract){
			int numF = ( (ImpegnoAbstract) movimentoGestione).getNumeroAccFinanziamento();
			listaAttrInseriti.add((SiacRMovgestTsAttrFin) salvaAttributoTAttr(attributoInfo, datiOperazioneDto, numF, CostantiFin.T_ATTR_CODE_NUMERO_ACC_FINANZIAMENTO));
		}
		
		//ANNO_RIACCERTATO
		if(movimentoGestione.getAnnoRiaccertato()!=0){
			String annoRiaccertato = ""+movimentoGestione.getAnnoRiaccertato();
			listaAttrInseriti.add((SiacRMovgestTsAttrFin) salvaAttributoTAttr(attributoInfo, datiOperazioneDto, annoRiaccertato, CostantiFin.T_ATTR_CODE_ANNO_RIACCERTATO));
		}
		
		//NUMERO_RIACCERTATO
		if(null!=movimentoGestione.getNumeroRiaccertato() && !movimentoGestione.getNumeroRiaccertato().equals(BigDecimal.ZERO)){
			String numeroRiaccertato = ""+movimentoGestione.getNumeroRiaccertato();
			listaAttrInseriti.add((SiacRMovgestTsAttrFin) salvaAttributoTAttr(attributoInfo, datiOperazioneDto, numeroRiaccertato, CostantiFin.T_ATTR_CODE_NUMERO_RIACCERTATO));
		}
		
		//ANNO_CAPITOLO_ORIGINE
		if(movimentoGestione.getAnnoCapitoloOrigine()!=0){
			String annoCapitoloOrigine = ""+movimentoGestione.getAnnoCapitoloOrigine();
			listaAttrInseriti.add((SiacRMovgestTsAttrFin)salvaAttributoTAttr(attributoInfo, datiOperazioneDto, annoCapitoloOrigine, CostantiFin.T_ATTR_CODE_ANNO_CAPITOLO_ORIGINE));
		}
		
		//NUMERO_CAPITOLO_ORIGINE
		if(movimentoGestione.getNumeroCapitoloOrigine()!=0){
			String numeroCapitoloOrigine = ""+movimentoGestione.getNumeroCapitoloOrigine();
			listaAttrInseriti.add((SiacRMovgestTsAttrFin)salvaAttributoTAttr(attributoInfo, datiOperazioneDto, numeroCapitoloOrigine, CostantiFin.T_ATTR_CODE_NUMERO_CAPITOLO_ORIGINE));
		}
		
		//ANNO_ORIGINE_PLUR
		if(movimentoGestione instanceof ImpegnoAbstract){
			Integer annoImpOrigine = ( (ImpegnoAbstract) movimentoGestione).getAnnoImpegnoOrigine();
			if(null!=annoImpOrigine){
				listaAttrInseriti.add((SiacRMovgestTsAttrFin)salvaAttributoTAttr(attributoInfo, datiOperazioneDto, annoImpOrigine, CostantiFin.T_ATTR_CODE_ANNO_ORIGINE_PLUR));
			}
		}else if(movimentoGestione instanceof AccertamentoAbstract){
			Integer annoImpOrigine = ( (AccertamentoAbstract) movimentoGestione).getAnnoAccertamentoOrigine();
			if(null!=annoImpOrigine){
				listaAttrInseriti.add((SiacRMovgestTsAttrFin)salvaAttributoTAttr(attributoInfo, datiOperazioneDto, annoImpOrigine, CostantiFin.T_ATTR_CODE_ANNO_ORIGINE_PLUR));
			}
		}
		//NUMERO_ORIGINE_PLUR
		if(movimentoGestione instanceof ImpegnoAbstract){
			Integer numImpOrigine = ( (ImpegnoAbstract) movimentoGestione).getNumImpegnoOrigine();
			if(null!=numImpOrigine){
				listaAttrInseriti.add((SiacRMovgestTsAttrFin)salvaAttributoTAttr(attributoInfo, datiOperazioneDto, numImpOrigine, CostantiFin.T_ATTR_CODE_NUMERO_ORIGINE_PLUR));
			}
		}else if(movimentoGestione instanceof AccertamentoAbstract){
			Integer numImpOrigine = ( (AccertamentoAbstract) movimentoGestione).getNumAccertamentoOrigine();
			if(null!=numImpOrigine){
				listaAttrInseriti.add((SiacRMovgestTsAttrFin)salvaAttributoTAttr(attributoInfo, datiOperazioneDto, numImpOrigine, CostantiFin.T_ATTR_CODE_NUMERO_ORIGINE_PLUR));
			}
		}
		/***************************************************************************************************************/
		
		//NUMERO_ARTICOLO_ORIGINE
		if(movimentoGestione.getNumeroArticoloOrigine()!=0){
			String numeroArticoloOrigine = ""+movimentoGestione.getNumeroArticoloOrigine();
			listaAttrInseriti.add((SiacRMovgestTsAttrFin)salvaAttributoTAttr(attributoInfo, datiOperazioneDto, numeroArticoloOrigine, CostantiFin.T_ATTR_CODE_NUMERO_ARTICOLO_ORIGINE));
		}
		
		//NUMERO_UEB_ORIGINE
		if(movimentoGestione.getNumeroUEBOrigine()!=0){
			String numeroUEBOrigine = ""+movimentoGestione.getNumeroUEBOrigine();
			listaAttrInseriti.add((SiacRMovgestTsAttrFin)salvaAttributoTAttr(attributoInfo, datiOperazioneDto, numeroUEBOrigine, CostantiFin.T_ATTR_CODE_NUMERO_UEB_ORIGINE));
		}
		
		//EVENTUALE SOGGETTO CREDITORE:
		Soggetto soggetto = movimentoGestione.getSoggetto();
		List<SiacRMovgestTsSogFin> siacRMovgestTsSogs = new ArrayList<SiacRMovgestTsSogFin>();
		if(soggetto!=null && !StringUtilsFin.isEmpty(soggetto.getCodiceSoggetto())){
			SiacTSoggettoFin siacTSoggetto = getSoggetto(movimentoGestione, datiOperazioneDto);
			if(siacTSoggetto!=null){
				SiacRMovgestTsSogFin siacRMovgestTsSog = new SiacRMovgestTsSogFin();
				siacRMovgestTsSog = DatiOperazioneUtil.impostaDatiOperazioneLogin(siacRMovgestTsSog, datiOperazioneDto, siacTAccountRepository);
				siacRMovgestTsSog.setSiacTMovgestT(siacTMovgestTs);
				siacRMovgestTsSog.setSiacTSoggetto(siacTSoggetto);
				//salvo sul db:
				siacRMovgestTsSog = siacRMovgestTsSogRepository.saveAndFlush(siacRMovgestTsSog);
				siacRMovgestTsSogs.add(siacRMovgestTsSog);
			}
		}
		siacTMovgestTs.setSiacRMovgestTsSogs(siacRMovgestTsSogs);
		////////////////////
		
		//EVENTUALE CLASSE SOGGETTO:
		List<SiacRMovgestTsSogclasseFin> siacRMovgestTsSogclasses = new ArrayList<SiacRMovgestTsSogclasseFin>();
		ClasseSoggetto classeSoggetto = movimentoGestione.getClasseSoggetto();
		if(classeSoggetto!=null && !StringUtilsFin.isEmpty(classeSoggetto.getCodice())){
			SiacDSoggettoClasseFin siacDSoggettoClasse = getSoggettoClasse(movimentoGestione, datiOperazioneDto);
			if(siacDSoggettoClasse!=null){
				SiacRMovgestTsSogclasseFin siacRMovgestTsSogclasse = new SiacRMovgestTsSogclasseFin();
				siacRMovgestTsSogclasse = DatiOperazioneUtil.impostaDatiOperazioneLogin(siacRMovgestTsSogclasse, datiOperazioneDto, siacTAccountRepository);
				siacRMovgestTsSogclasse.setSiacTMovgestT(siacTMovgestTs);
				siacRMovgestTsSogclasse.setSiacDSoggettoClasse(siacDSoggettoClasse);
				//salvo sul db:
				siacRMovgestTsSogclasse = siacRMovgestTsSogClasseRepository.saveAndFlush(siacRMovgestTsSogclasse);
				siacRMovgestTsSogclasses.add(siacRMovgestTsSogclasse);
			}
		}
		siacTMovgestTs.setSiacRMovgestTsSogclasses(siacRMovgestTsSogclasses);
		////////////////////
		
		//EVENTUALE ATTO AMMINISTRATIVO:
		if(movimentoGestione.getAttoAmministrativo()!=null){
			SiacTAttoAmmFin siacTAttoAmm = getAttoAmministrativo(movimentoGestione, idEnte);
			if(siacTAttoAmm!=null){
				SiacRMovgestTsAttoAmmFin siacRMovgestTsAttoAmm  = new SiacRMovgestTsAttoAmmFin();
				siacRMovgestTsAttoAmm = DatiOperazioneUtil.impostaDatiOperazioneLogin(siacRMovgestTsAttoAmm, datiOperazioneDto, siacTAccountRepository);
				siacRMovgestTsAttoAmm.setSiacTAttoAmm(siacTAttoAmm);
				siacRMovgestTsAttoAmm.setSiacTMovgestT(siacTMovgestTs);
				siacRMovgestTsAttoAmm = siacRMovgestTsAttoAmmRepository.saveAndFlush(siacRMovgestTsAttoAmm);
				siacTMovgestTs.setSiacRMovgestTsAttoAmms(toList(siacRMovgestTsAttoAmm));
			}
		}
		
		//EVENTUALE progetto su r_movgests_ts_programma
		SiacTProgrammaFin siacTProgramma = null;
		List<SiacRMovgestTsProgrammaFin> siacRMovgestTsProgrammas = new ArrayList<SiacRMovgestTsProgrammaFin>();
		if(padre!=null){
			// SUB-IMPEGNO / SUB-ACCERTAMENTO
			List<SiacRMovgestTsProgrammaFin> l = siacRMovgestTsProgrammaRepository.findByMovgestTs(idEnte, datiOperazioneDto.getTs(), padre.getMovgestTsId());
			if(l!=null && l.size()>0){
				//puo' essere solo l'elemento in prima posizione:
				siacTProgramma = l.get(0).getSiacTProgramma();
			}
		}else{
			// IMPEGNO / ACCERTAMENTO
			siacTProgramma = getProgetto(movimentoGestione, datiOperazioneDto);
		}
		if(siacTProgramma!=null){
			SiacRMovgestTsProgrammaFin siacRMovgestTsProgramma  = new SiacRMovgestTsProgrammaFin();
			siacRMovgestTsProgramma = DatiOperazioneUtil.impostaDatiOperazioneLogin(siacRMovgestTsProgramma, datiOperazioneDto, siacTAccountRepository);
			siacRMovgestTsProgramma.setSiacTProgramma(siacTProgramma);
			siacRMovgestTsProgramma.setSiacTMovgestT(siacTMovgestTs);
			//salvo sul db:
			siacRMovgestTsProgramma = siacRMovgestTsProgrammaRepository.saveAndFlush(siacRMovgestTsProgramma);
			siacRMovgestTsProgrammas.add(siacRMovgestTsProgramma);
		}
		siacTMovgestTs.setSiacRMovgestTsProgrammas(siacRMovgestTsProgrammas);
		
		//TIPO:
		List<SiacRClassBaseFin> attributiTClassAggiornati = new ArrayList<SiacRClassBaseFin>();
		if(movimentoGestione instanceof ImpegnoAbstract){
			String codeImp = ((ImpegnoAbstract)movimentoGestione).getTipoImpegno().getCodice();
			SiacRClassBaseFin rClassTipo = salvaAttributoTClass(datiOperazioneDto, attributoInfo, codeImp, CostantiFin.D_CLASS_TIPO_TIPO_IMPEGNO);
			attributiTClassAggiornati.add(rClassTipo);
		}
		
		//TRANSAZIONE ELEMENTARE:
		EsitoSalvataggioTransazioneElmentare esitoSave = salvaTransazioneElementare(attributoInfo, datiOperazioneDto, movimentoGestione);
		/////
	
		attributiTClassAggiornati = addAll(attributiTClassAggiornati, esitoSave.gettClassSaved());
		aggiornaAllRClassValidiInEntityJPA(attributoInfo, attributiTClassAggiornati, datiOperazioneDto);
		
		listaAttrInseriti.add((SiacRMovgestTsAttrFin) esitoSave.getSiacRAttrBaseCupSaved());
		siacTMovgestTs.setSiacRMovgestTsAttrs(listaAttrInseriti);
		
		//Termino restituendo l'oggetto di ritorno: 
        return siacTMovgestTs;
	}
	
	/**
	 * Questa routine aggiorna un record SiacTMovgestTsFin e nelle tabelle accessorie.
	 * E' coinvolta nell'aggiornamento di impegni, accertamenti, subimpegni e subaccertamenti perche'
	 * tutti questi oggetti prevedono una riga su tale tabella.
	 * @param siacTMovgestTsDaAggiornare
	 * @param siacTMovgest
	 * @param movimentoGestione
	 * @param datiOperazioneDto
	 * @param padre
	 * @param idAmbito
	 * @param bilancio 
	 * @return
	 */
	private SiacTMovgestTsFin aggiornaMovgestTs(SiacTMovgestTsFin siacTMovgestTsDaAggiornare,SiacTMovgestFin siacTMovgest, MovimentoGestione movimentoGestione, 
			DatiOperazioneDto datiOperazioneDto,SiacTMovgestTsFin padre,Integer idAmbito, Bilancio bilancio){
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		
		siacTMovgestTsDaAggiornare = DatiOperazioneUtil.impostaDatiOperazioneLogin(siacTMovgestTsDaAggiornare, datiOperazioneDto, siacTAccountRepository);
		
		siacTMovgestTsDaAggiornare.setMovgestTsDesc(movimentoGestione.getDescrizione());//arriva da front end (ma non e' obbligatorio verra' reso nullable)
		
		Timestamp dataScadenza = TimingUtils.convertiDataInTimeStamp(movimentoGestione.getDataScadenza());
		siacTMovgestTsDaAggiornare.setMovgestTsScadenzaData(dataScadenza);
		siacTMovgestTsDaAggiornare.setOrdine("nd");//TODO ISSUE --> non valorizzare diventara' nullable
		
		siacTMovgestTsDaAggiornare.setSiacTMovgest(siacTMovgest);
		
		String statoPrimaDellaModifica = getStatoCode(siacTMovgestTsDaAggiornare, datiOperazioneDto);
		
		//DETERMININIAMO SE STIAMO INSERENDO UNA TESTATA O UN SUBIMPEGNO:
		String codiceTipoTs = null;
		if(padre!=null){
			codiceTipoTs = CostantiFin.MOVGEST_TS_TIPO_SUBIMPEGNO;
			siacTMovgestTsDaAggiornare.setLivello(2);
			siacTMovgestTsDaAggiornare.setMovgestTsIdPadre(padre.getMovgestTsId());//TODO aspettiamo che lo modifichino in FK per cambiare l'entity
		}else{
			codiceTipoTs = CostantiFin.MOVGEST_TS_TIPO_TESTATA;
			siacTMovgestTsDaAggiornare.setLivello(1);
		}
		
		SiacDMovgestTsTipoFin siacDMovgestTsTipo = siacDMovgestTsTipoRepository.findValidoByCode(idEnte, datiOperazioneDto.getTs(), codiceTipoTs).get(0);
		//////////////////////////////////////////////////////////////
		
		siacTMovgestTsDaAggiornare.setSiacDMovgestTsTipo(siacDMovgestTsTipo);
		//salvo sul db:
		siacTMovgestTsDaAggiornare = siacTMovgestTsRepository.saveAndFlush(siacTMovgestTsDaAggiornare);
		
		AttributoTClassInfoDto attributoInfo = new AttributoTClassInfoDto();
		attributoInfo.setTipoOggetto(OggettoDellAttributoTClass.T_MOVGEST_TS);
		attributoInfo.setSiacTMovgestTs(siacTMovgestTsDaAggiornare);
		
		//
		SiacTMovgestTsDetFin siacDetAttuale = null;
		SiacTMovgestTsDetFin siacDetIniziale = null;
		SiacTMovgestTsDetFin siacDetUtilizzabile= null;
		for(SiacTMovgestTsDetFin det : siacTMovgestTsDaAggiornare.getSiacTMovgestTsDets()){
			if(det.getSiacDMovgestTsDetTipo().getMovgestTsDetTipoCode().equalsIgnoreCase(CostantiFin.MOVGEST_TS_DET_TIPO_INIZIALE)){
				siacDetIniziale = det;
			} else if(det.getSiacDMovgestTsDetTipo().getMovgestTsDetTipoCode().equalsIgnoreCase(CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE)) {
				siacDetAttuale = det;
			} else if(det.getSiacDMovgestTsDetTipo().getMovgestTsDetTipoCode().equalsIgnoreCase(CostantiFin.MOVGEST_TS_DET_TIPO_UTILIZZABILE)) {
				siacDetUtilizzabile = det;
			}
		}
		
		//Calcoliamo il delta di modifica importo confrontando l'importo attuale da db e l'importo attuale ricevuto dal chiamante:
		//BigDecimal importoAttualeOld = siacDetAttuale.getMovgestTsDetImporto();
		//BigDecimal importoAttualeNew = impegno.getImportoAttuale();
		//BigDecimal deltaImporto = importoAttualeNew.subtract(importoAttualeOld);
		
		boolean modPerPredispozioneConsuntivo = modificabilePerPredisposizioneConsuntivo(siacTMovgest.getMovgestAnno(), datiOperazioneDto, bilancio);
		
		//JIRA  SIAC-3506 in caso di residuo con presenza di modifiche di importo valide, non si devono toccare gli importi:
		boolean residuo = isResiduo(siacTMovgest.getMovgestAnno(), bilancio);
		boolean presenzaModificheImportoValide = presenzaModificheImportoValide(siacTMovgestTsDaAggiornare);
		boolean residuoConModificheImporto = residuo && presenzaModificheImportoValide;
		//
		
		if(CostantiFin.MOVGEST_STATO_PROVVISORIO.equals(statoPrimaDellaModifica) || modPerPredispozioneConsuntivo){
			//SE E' PROVVISORIO L'IMPORTO INZIALE VIENE ALIMENTATO CON LO STESSO VALORE DI QUELLO ATTUALE
			if(!residuoConModificheImporto){
				siacDetIniziale = saveImporto(siacDetIniziale,movimentoGestione.getImportoAttuale(), datiOperazioneDto, siacTMovgestTsDaAggiornare, CostantiFin.MOVGEST_TS_DET_TIPO_INIZIALE);
			}
		}
		//
		
		if(!residuoConModificheImporto){
			siacDetAttuale = saveImporto(siacDetAttuale,movimentoGestione.getImportoAttuale(), datiOperazioneDto, siacTMovgestTsDaAggiornare, CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE);
		}
		
		
		//SOLO PER acc e subacc:
		if(movimentoGestione instanceof Accertamento){
			
			siacDetUtilizzabile = saveImporto(siacDetUtilizzabile,((Accertamento) movimentoGestione).getImportoUtilizzabile(), datiOperazioneDto, siacTMovgestTsDaAggiornare, CostantiFin.MOVGEST_TS_DET_TIPO_UTILIZZABILE);
			
			
			//Solo se l�accertamento � di competenza o futuro (annoMovimento >= annoBilancio)
			//importoUtilizzabile = importoUtilizzabile + TotaleModificheAccertamento
//			if(impegno.getAnnoMovimento()>=bilancio.getAnno()){
//				BigDecimal importoUtilizzabileOld = siacDetUtilizzabile.getMovgestTsDetImporto();
//				BigDecimal importoUtilizzabileNew = importoUtilizzabileOld.add(deltaImporto);
//				//importo utilizzabile mantenuto uguale all'attuale
//				siacDetUtilizzabile = saveImporto(siacDetUtilizzabile,importoUtilizzabileNew, 
//						datiOperazioneDto, siacTMovgestTsDaAggiornare, CostantiFin.MOVGEST_TS_DET_TIPO_UTILIZZABILE);
//			}
		}
		//set che genera refresh dei dati aggiornati:
		siacTMovgestTsDaAggiornare.setSiacTMovgestTsDets(toList(siacDetIniziale,siacDetAttuale,siacDetUtilizzabile));
		//
		
		//STATO
		// Jira-628 quando confronto gli stati devo omettere lo stato annullato
		//
		List<SiacRMovgestTsStatoFin> listaStati = siacTMovgestTsDaAggiornare.getSiacRMovgestTsStatos();
		if(listaStati!=null && listaStati.size()>0){
			for(SiacRMovgestTsStatoFin siacRMovgestTsStato : listaStati){
				if(siacRMovgestTsStato.getDataFineValidita()==null && 
				   !siacRMovgestTsStato.getSiacDMovgestStato().getMovgestStatoCode().equalsIgnoreCase(CostantiFin.MOVGEST_STATO_ANNULLATO)){
						//Se valido e non annullato
						String statoImpegnoCode = determinaStatoImpegno(idEnte, movimentoGestione);
						SiacRMovgestTsStatoFin newStato = saveStatoImpegno(datiOperazioneDto, statoImpegnoCode,siacTMovgestTsDaAggiornare);
						siacTMovgestTsDaAggiornare.setSiacRMovgestTsStatos(toList(newStato));
				}
			}
		}
	
		//CIG:
		if(movimentoGestione instanceof ImpegnoAbstract){
			String cig = ((ImpegnoAbstract)movimentoGestione).getCig();
			salvaAttributoCig(attributoInfo, datiOperazioneDto, cig);
		}	
		
		//FLAG_DA_RIACCERTAMENTO
		salvaAttributoTAttr(attributoInfo, datiOperazioneDto, movimentoGestione.isFlagDaRiaccertamento(), CostantiFin.T_ATTR_CODE_FLAG_DA_RIACCERTAMENTO);
		
		//FLAG DURC
		salvaAttributoTAttr(attributoInfo, datiOperazioneDto, movimentoGestione.isFlagSoggettoDurc(), CostantiFin.T_ATTR_CODE_FLAG_SOGGETTO_DURC);
		
		//ACCERTAMENTO con FATTURA PREVISTA (FLAG FATTURA)
		if(movimentoGestione instanceof AccertamentoAbstract){
			salvaAttributoTAttr(attributoInfo, datiOperazioneDto, ((AccertamentoAbstract) movimentoGestione).isFlagFattura(), CostantiFin.T_ATTR_CODE_FLAG_PREVISTA_FATTURA);
			salvaAttributoTAttr(attributoInfo, datiOperazioneDto, ((AccertamentoAbstract) movimentoGestione).isFlagCorrispettivo(), CostantiFin.T_ATTR_CODE_FLAG_PREVISTO_CORRISPETTIVO);
		}
		
		//Attiva GSA
	    salvaAttributoTAttr(attributoInfo, datiOperazioneDto, movimentoGestione.isFlagAttivaGsa(), CostantiFin.T_ATTR_CODE_FLAG_ATTIVA_GSA);
		
		//ANNO_FINANZIAMENTO
		if(movimentoGestione instanceof ImpegnoAbstract){
			int annoF = ((ImpegnoAbstract) movimentoGestione).getAnnoFinanziamento();
			salvaAttributoTAttr(attributoInfo, datiOperazioneDto, String.valueOf(annoF), CostantiFin.T_ATTR_CODE_ANNO_FINANZIAMENTO);
		}	
			
		//NUMERO_ACC_FINANZIAMENTO
		if(movimentoGestione instanceof ImpegnoAbstract){
			int numF = ((ImpegnoAbstract) movimentoGestione).getNumeroAccFinanziamento();
			salvaAttributoTAttr(attributoInfo, datiOperazioneDto, String.valueOf(numF), CostantiFin.T_ATTR_CODE_NUMERO_ACC_FINANZIAMENTO);
		}
		
		//ANNO_RIACCERTATO
			String annoRiaccertato = ""+movimentoGestione.getAnnoRiaccertato();
			salvaAttributoTAttr(attributoInfo, datiOperazioneDto, annoRiaccertato, CostantiFin.T_ATTR_CODE_ANNO_RIACCERTATO);
			
		//NUMERO_RIACCERTATO
			String numeroRiaccertato = ""+movimentoGestione.getNumeroRiaccertato();
			salvaAttributoTAttr(attributoInfo, datiOperazioneDto, numeroRiaccertato, CostantiFin.T_ATTR_CODE_NUMERO_RIACCERTATO);
				
		//ANNO_CAPITOLO_ORIGINE
		if(movimentoGestione.getAnnoCapitoloOrigine()!=0){
			String annoCapitoloOrigine = ""+movimentoGestione.getAnnoCapitoloOrigine();
			salvaAttributoTAttr(attributoInfo, datiOperazioneDto, annoCapitoloOrigine, CostantiFin.T_ATTR_CODE_ANNO_CAPITOLO_ORIGINE);
		}
				
		//NUMERO_CAPITOLO_ORIGINE	
		if(movimentoGestione.getNumeroCapitoloOrigine()!=0){
			String numeroCapitoloOrigine = ""+movimentoGestione.getNumeroCapitoloOrigine();
			salvaAttributoTAttr(attributoInfo, datiOperazioneDto, numeroCapitoloOrigine, CostantiFin.T_ATTR_CODE_NUMERO_CAPITOLO_ORIGINE);
		}
		
		/******************************************************************************************************************************/
		//ANNO_ORIGINE_PLUR
		if(movimentoGestione instanceof ImpegnoAbstract){
			Integer annoImpOrigine = ( (ImpegnoAbstract) movimentoGestione).getAnnoImpegnoOrigine();
				salvaAttributoTAttr(attributoInfo, datiOperazioneDto, annoImpOrigine, CostantiFin.T_ATTR_CODE_ANNO_ORIGINE_PLUR);
		}else if(movimentoGestione instanceof AccertamentoAbstract){
			Integer annoImpOrigine = ( (AccertamentoAbstract) movimentoGestione).getAnnoAccertamentoOrigine();
				salvaAttributoTAttr(attributoInfo, datiOperazioneDto, annoImpOrigine, CostantiFin.T_ATTR_CODE_ANNO_ORIGINE_PLUR);
			}
		//NUMERO_ORIGINE_PLUR
		if(movimentoGestione instanceof ImpegnoAbstract){
			Integer numImpOrigine = ( (ImpegnoAbstract) movimentoGestione).getNumImpegnoOrigine();
				salvaAttributoTAttr(attributoInfo, datiOperazioneDto, numImpOrigine, CostantiFin.T_ATTR_CODE_NUMERO_ORIGINE_PLUR);
		}else if(movimentoGestione instanceof AccertamentoAbstract){
			Integer numImpOrigine = ( (AccertamentoAbstract) movimentoGestione).getNumAccertamentoOrigine();
				salvaAttributoTAttr(attributoInfo, datiOperazioneDto, numImpOrigine, CostantiFin.T_ATTR_CODE_NUMERO_ORIGINE_PLUR);
			}
		
		//NUMERO_ARTICOLO_ORIGINE	
		if(movimentoGestione.getNumeroArticoloOrigine()!=0){
			String numeroArticoloOrigine = ""+movimentoGestione.getNumeroArticoloOrigine();
			salvaAttributoTAttr(attributoInfo, datiOperazioneDto, numeroArticoloOrigine, CostantiFin.T_ATTR_CODE_NUMERO_ARTICOLO_ORIGINE);
		}
				
		//NUMERO_UEB_ORIGINE	
		if(movimentoGestione.getNumeroUEBOrigine()!=0){
			String numeroUEBOrigine = ""+movimentoGestione.getNumeroUEBOrigine();
			salvaAttributoTAttr(attributoInfo, datiOperazioneDto, numeroUEBOrigine, CostantiFin.T_ATTR_CODE_NUMERO_UEB_ORIGINE);
		}
		
		//EVENTUALE SOGGETTO CREDITORE:
		Soggetto soggetto = movimentoGestione.getSoggetto();
		if(soggetto!=null && !StringUtilsFin.isEmpty(soggetto.getCodiceSoggetto())){
			SiacTSoggettoFin siacTSoggetto = getSoggetto(movimentoGestione, datiOperazioneDto);
			if(siacTSoggetto!=null){
				Integer idMovGestTs = siacTMovgestTsDaAggiornare.getMovgestTsId();
				List<SiacRMovgestTsSogFin> lista = siacRMovgestTsSogRepository.findValidoMovGestTsSogByIdMovGestAndEnte(idEnte, datiOperazioneDto.getTs(), idMovGestTs); 
				if(lista!=null && lista.size()>0){
					SiacRMovgestTsSogFin vecchioSog = lista.get(0);
					if(vecchioSog!=null){
						Integer soggIdOld = vecchioSog.getSiacTSoggetto().getUid();
						if(soggetto.getUid() == soggIdOld){
							//se sono uguali non c'e' nessun passaggio di stato
						} else {
							vecchioSog.setSiacTSoggetto(siacTSoggetto);
							DatiOperazioneDto datiOperazione = DatiOperazioneDto.buildDatiOperazione(datiOperazioneDto, Operazione.MODIFICA);
							vecchioSog = DatiOperazioneUtil.impostaDatiOperazioneLogin(vecchioSog, datiOperazione, siacTAccountRepository);
							//salvo sul db:
							SiacRMovgestTsSogFin newSoggetto = siacRMovgestTsSogRepository.saveAndFlush(vecchioSog);
							siacTMovgestTsDaAggiornare.setSiacRMovgestTsSogs(toList(newSoggetto));
						}

					}
				}else{
					// inserisco il soggetto per la prima volta SOGGETTO CREDITORE
					if(siacTSoggetto!=null){
						SiacRMovgestTsSogFin siacRMovgestTsSog = new SiacRMovgestTsSogFin();
						DatiOperazioneDto datiOperazione = DatiOperazioneDto.buildDatiOperazione(datiOperazioneDto, Operazione.INSERIMENTO);						
						siacRMovgestTsSog = DatiOperazioneUtil.impostaDatiOperazioneLogin(siacRMovgestTsSog, datiOperazione, siacTAccountRepository);
						siacRMovgestTsSog.setSiacTMovgestT(siacTMovgestTsDaAggiornare);
						siacRMovgestTsSog.setSiacTSoggetto(siacTSoggetto);
						//salvo sul db:
						SiacRMovgestTsSogFin newSoggetto = siacRMovgestTsSogRepository.saveAndFlush(siacRMovgestTsSog);
						siacTMovgestTsDaAggiornare.setSiacRMovgestTsSogs(toList(newSoggetto));
					}
				}
			}
		}else{
			//ELIMINAZIONE DEL SOGGETTO NULLO
			Integer idMovGestTs = siacTMovgestTsDaAggiornare.getMovgestTsId();
			List<SiacRMovgestTsSogFin> lista = siacRMovgestTsSogRepository.findValidoMovGestTsSogByIdMovGestAndEnte(idEnte, datiOperazioneDto.getTs(), idMovGestTs); 
			if(lista!=null && lista.size()>0){
				SiacRMovgestTsSogFin vecchioSog = lista.get(0);
				if(vecchioSog!=null){
						DatiOperazioneDto datiOperazione = DatiOperazioneDto.buildDatiOperazione(datiOperazioneDto, Operazione.CANCELLAZIONE_LOGICA_RECORD);
						vecchioSog = DatiOperazioneUtil.impostaDatiOperazioneLogin(vecchioSog, datiOperazione, siacTAccountRepository);
						//salvo sul db:
						siacRMovgestTsSogRepository.saveAndFlush(vecchioSog);
				}
			}
			siacTMovgestTsDaAggiornare.setSiacRMovgestTsSogs(new ArrayList<SiacRMovgestTsSogFin>());
			
		}
		////////////////////
		
		//EVENTUALE CLASSE SOGGETTO:
		ClasseSoggetto classeSoggetto = movimentoGestione.getClasseSoggetto();
		if(classeSoggetto!=null && !StringUtilsFin.isEmpty(classeSoggetto.getCodice())){
			SiacDSoggettoClasseFin siacDSoggettoClasse = getSoggettoClasse(movimentoGestione, datiOperazioneDto);
			if(siacDSoggettoClasse!=null){
				Integer idMovGestTs = siacTMovgestTsDaAggiornare.getMovgestTsId();
				List<SiacRMovgestTsSogclasseFin> lista = siacRMovgestTsSogClasseRepository.findValidoMovGestTsSogClasseByIdMovGestAndEnte(idEnte, idMovGestTs, datiOperazioneDto.getTs()); // datiOperazioneDto.getTs(),  
				if(lista!=null && lista.size()>0){
					SiacRMovgestTsSogclasseFin vecchioSogClasse = lista.get(0);
					if(vecchioSogClasse!=null){
						Integer soggClasseIdOld = vecchioSogClasse.getSiacDSoggettoClasse().getUid();
						if(classeSoggetto.getUid() == soggClasseIdOld){
							//se sono uguali non c'e' nessun passaggio di stato
						} else {
							vecchioSogClasse.setSiacDSoggettoClasse(siacDSoggettoClasse);
							DatiOperazioneDto datiOperazione = DatiOperazioneDto.buildDatiOperazione(datiOperazioneDto, Operazione.MODIFICA);
							vecchioSogClasse = DatiOperazioneUtil.impostaDatiOperazioneLogin(vecchioSogClasse, datiOperazione, siacTAccountRepository);
							//salvo sul db:
							SiacRMovgestTsSogclasseFin newSogClass = siacRMovgestTsSogClasseRepository.saveAndFlush(vecchioSogClasse);
							siacTMovgestTsDaAggiornare.setSiacRMovgestTsSogclasses(toList(newSogClass));
						}

					}
				}else{
					// inserisco la classe per la prima volta CLASSE CREDITORE
					if(siacDSoggettoClasse!=null){
						SiacRMovgestTsSogclasseFin siacRMovgestTsSogclasse = new SiacRMovgestTsSogclasseFin();
						DatiOperazioneDto datiOperazione = DatiOperazioneDto.buildDatiOperazione(datiOperazioneDto, Operazione.INSERIMENTO);
						siacRMovgestTsSogclasse = DatiOperazioneUtil.impostaDatiOperazioneLogin(siacRMovgestTsSogclasse, datiOperazione, siacTAccountRepository);
						siacRMovgestTsSogclasse.setSiacTMovgestT(siacTMovgestTsDaAggiornare);
						siacRMovgestTsSogclasse.setSiacDSoggettoClasse(siacDSoggettoClasse);
						//salvo sul db:
						SiacRMovgestTsSogclasseFin newSogClass = siacRMovgestTsSogClasseRepository.saveAndFlush(siacRMovgestTsSogclasse);
						siacTMovgestTsDaAggiornare.setSiacRMovgestTsSogclasses(toList(newSogClass));
					}
				}
			}
		}else{
			//ELIMINAZIONE DEL CLASSE NULLA
			Integer idMovGestTs = siacTMovgestTsDaAggiornare.getMovgestTsId();
			List<SiacRMovgestTsSogclasseFin> lista = siacRMovgestTsSogClasseRepository.findValidoMovGestTsSogClasseByIdMovGestAndEnte(idEnte, idMovGestTs, datiOperazioneDto.getTs()); 
			
			if(lista!=null && lista.size()>0){
				SiacRMovgestTsSogclasseFin vecchioSogClasse = lista.get(0);
				if(vecchioSogClasse!=null){
						DatiOperazioneDto datiOperazione = DatiOperazioneDto.buildDatiOperazione(datiOperazioneDto, Operazione.CANCELLAZIONE_LOGICA_RECORD);
						vecchioSogClasse = DatiOperazioneUtil.impostaDatiOperazioneLogin(vecchioSogClasse, datiOperazione, siacTAccountRepository);
						//salvo sul db:
						siacRMovgestTsSogClasseRepository.saveAndFlush(vecchioSogClasse);
				}
			}
			siacTMovgestTsDaAggiornare.setSiacRMovgestTsSogclasses(new ArrayList<SiacRMovgestTsSogclasseFin>());
		}	
			
		////////////////////
		
		//EVENTUALE ATTO AMMINISTRATIVO:
		AttoAmministrativo attoAmministrativo = movimentoGestione.getAttoAmministrativo();
		
		//per correggere passaggio dati del provvedimento in campi errati
		if(attoAmministrativo!=null 
				&& movimentoGestione.getAttoAmmNumero()!=null && movimentoGestione.getAttoAmmNumero()!=0 && !movimentoGestione.getAttoAmmAnno().isEmpty() 
				&& movimentoGestione.getAttoAmmTipoAtto()!=null 
				&& !movimentoGestione.getAttoAmmTipoAtto().getCodice().isEmpty()){
			
			
			attoAmministrativo = new AttoAmministrativo();
			attoAmministrativo.setNumero(movimentoGestione.getAttoAmmNumero());
			attoAmministrativo.setAnno(Integer.valueOf(movimentoGestione.getAttoAmmAnno()));						
			
			TipoAtto ta = new TipoAtto();
			ta.setCodice(movimentoGestione.getAttoAmmTipoAtto().getCodice());					
					
			attoAmministrativo.setTipoAtto(ta);
			
			if(movimentoGestione.getAttoAmministrativo().getStrutturaAmmContabile()!=null &&
					movimentoGestione.getAttoAmministrativo().getStrutturaAmmContabile().getUid()!= 0){
				
				attoAmministrativo.setStrutturaAmmContabile(movimentoGestione.getAttoAmministrativo().getStrutturaAmmContabile());
			}
			
			movimentoGestione.setAttoAmministrativo(attoAmministrativo);
			
		}
		
		if(attoAmministrativo!=null){
			
			SiacTAttoAmmFin siacTAttoAmm = getAttoAmministrativo(movimentoGestione, idEnte);
			
			if(siacTAttoAmm!=null){
				Integer idMovGestTs = siacTMovgestTsDaAggiornare.getMovgestTsId();
				log.debug("aggiornaMovgestTs"," Ricerco per idMovGestTs. uid: " + idMovGestTs);
				
				List<SiacRMovgestTsAttoAmmFin> lista = siacRMovgestTsAttoAmmRepository.findValidoByMovgestTs(idEnte, datiOperazioneDto.getTs(), idMovGestTs);
				if(lista!=null && lista.size()>0){
					SiacRMovgestTsAttoAmmFin vecchioAtto = lista.get(0);
					
					if(vecchioAtto!=null){
						Integer idAttoOld = vecchioAtto.getSiacTAttoAmm().getUid();
						if(attoAmministrativo.getUid() == idAttoOld){
							
							log.debug(""," L'atto del movimento ["+idMovGestTs +"] no e' cambiato rimane [" + vecchioAtto.getUid()+"] ");
							
						} else{
							
							log.debug(""," L'atto del movimento ["+idMovGestTs +"] e' cambiato in [" + attoAmministrativo.getUid()+"] , atto sul db ["+ idAttoOld +"]" );
							log.debug(""," verifico cosa vado a salvare nella rAttoMovGest ["+siacTAttoAmm.getUid() +"] " );
							
							vecchioAtto.setSiacTAttoAmm(siacTAttoAmm);
							DatiOperazioneDto datiOperazione = DatiOperazioneDto.buildDatiOperazione(datiOperazioneDto, Operazione.MODIFICA);
							vecchioAtto = DatiOperazioneUtil.impostaDatiOperazioneLogin(vecchioAtto, datiOperazione, siacTAccountRepository);
							//salvo sul db:
							
							log.debug(""," Prima del salvataggio per ts ["+idMovGestTs +"] salvo atto [" + vecchioAtto.getSiacTAttoAmm().getUid()+"] " );
							
							SiacRMovgestTsAttoAmmFin newAtto = siacRMovgestTsAttoAmmRepository.saveAndFlush(vecchioAtto);
							
							log.debug(""," Dopo il salvataggio per ts ["+idMovGestTs +"] salvato atto [" + newAtto.getSiacTAttoAmm().getUid()+"] " );
							
							siacTMovgestTsDaAggiornare.setSiacRMovgestTsAttoAmms(toList(newAtto));
						}
					}
				}else{
					//inserisco l'atto per la prima volta
					if(siacTAttoAmm!=null){
						SiacRMovgestTsAttoAmmFin siacRMovgestTsAttoAmm  = new SiacRMovgestTsAttoAmmFin();
						DatiOperazioneDto datiOperazione = DatiOperazioneDto.buildDatiOperazione(datiOperazioneDto, Operazione.INSERIMENTO);
						siacRMovgestTsAttoAmm = DatiOperazioneUtil.impostaDatiOperazioneLogin(siacRMovgestTsAttoAmm, datiOperazione, siacTAccountRepository);
						siacRMovgestTsAttoAmm.setSiacTAttoAmm(siacTAttoAmm);
						siacRMovgestTsAttoAmm.setSiacTMovgestT(siacTMovgestTsDaAggiornare);
						//salvo sul db:
						SiacRMovgestTsAttoAmmFin newAtto = siacRMovgestTsAttoAmmRepository.saveAndFlush(siacRMovgestTsAttoAmm);
						siacTMovgestTsDaAggiornare.setSiacRMovgestTsAttoAmms(toList(newAtto));
					}	
				}
			}
		}else{
			//Eliminazione dell'atto vecchio
			Integer idMovGestTs = siacTMovgestTsDaAggiornare.getMovgestTsId();
			List<SiacRMovgestTsAttoAmmFin> lista = siacRMovgestTsAttoAmmRepository.findValidoByMovgestTs(idEnte, datiOperazioneDto.getTs(), idMovGestTs);
			
			if(lista!=null && lista.size()>0){
				SiacRMovgestTsAttoAmmFin vecchioAtto = lista.get(0);
				if(vecchioAtto!=null){
					DatiOperazioneDto datiOperazione = DatiOperazioneDto.buildDatiOperazione(datiOperazioneDto, Operazione.CANCELLAZIONE_LOGICA_RECORD);
					vecchioAtto = DatiOperazioneUtil.impostaDatiOperazioneLogin(vecchioAtto, datiOperazione, siacTAccountRepository);
					//salvo sul db:
					siacRMovgestTsAttoAmmRepository.saveAndFlush(vecchioAtto);
				}
				siacTMovgestTsDaAggiornare.setSiacRMovgestTsAttoAmms(new ArrayList<SiacRMovgestTsAttoAmmFin>());
			}
		}

				
		//EVENTUALE progetto su r_movgests_ts_programma
		SiacTProgrammaFin siacTProgramma = getProgetto(movimentoGestione, datiOperazioneDto);
		if(siacTProgramma!=null){	
			Integer idMovGestTs = siacTMovgestTsDaAggiornare.getMovgestTsId();
			List<SiacRMovgestTsProgrammaFin> lista = siacRMovgestTsProgrammaRepository.findByMovgestTs(idEnte, datiOperazioneDto.getTs(), idMovGestTs);
			if(lista!=null && lista.size()>0){
				SiacRMovgestTsProgrammaFin vecchioProgramma = lista.get(0);
				if(vecchioProgramma!=null){
					Integer idProgrammaOld = vecchioProgramma.getSiacTProgramma().getUid();
					if(siacTProgramma.getUid() == idProgrammaOld){
						//return
					} else{
						//sosstituisco il vecchio programma con il nuovo
						vecchioProgramma.setSiacTProgramma(siacTProgramma);
						DatiOperazioneDto datiOperazione = DatiOperazioneDto.buildDatiOperazione(datiOperazioneDto, Operazione.MODIFICA);
						vecchioProgramma = DatiOperazioneUtil.impostaDatiOperazioneLogin(vecchioProgramma, datiOperazione, siacTAccountRepository);
						//salvo sul db:
						SiacRMovgestTsProgrammaFin newProgramma = siacRMovgestTsProgrammaRepository.saveAndFlush(vecchioProgramma);			
						siacTMovgestTsDaAggiornare.setSiacRMovgestTsProgrammas(toList(newProgramma));
					}
				}
			} else {
				// inserisco il programma per la prima volta
				if(siacTProgramma!=null){
					SiacRMovgestTsProgrammaFin siacRMovgestTsProgramma  = new SiacRMovgestTsProgrammaFin();
					DatiOperazioneDto datiOperazione = DatiOperazioneDto.buildDatiOperazione(datiOperazioneDto, Operazione.INSERIMENTO);
					siacRMovgestTsProgramma = DatiOperazioneUtil.impostaDatiOperazioneLogin(siacRMovgestTsProgramma, datiOperazione, siacTAccountRepository);
					siacRMovgestTsProgramma.setSiacTProgramma(siacTProgramma);
					siacRMovgestTsProgramma.setSiacTMovgestT(siacTMovgestTsDaAggiornare);
					//salvo sul db:
					SiacRMovgestTsProgrammaFin newProgramma = siacRMovgestTsProgrammaRepository.saveAndFlush(siacRMovgestTsProgramma);
					siacTMovgestTsDaAggiornare.setSiacRMovgestTsProgrammas(toList(newProgramma));
				}
			}
		} else {
			//Eliminazione del programma vecchio
			if(movimentoGestione instanceof Impegno){
				if(((Impegno) movimentoGestione).getProgetto() == null || StringUtilsFin.isEmpty(((Impegno) movimentoGestione).getProgetto().getCodice())){
					Integer idMovGestTs = siacTMovgestTsDaAggiornare.getMovgestTsId();
					List<SiacRMovgestTsProgrammaFin> lista = siacRMovgestTsProgrammaRepository.findByMovgestTs(idEnte, datiOperazioneDto.getTs(), idMovGestTs);
					if(lista!=null && lista.size()>0){
						SiacRMovgestTsProgrammaFin vecchioProgramma = lista.get(0);
						if(vecchioProgramma!=null){
							DatiOperazioneDto datiOperazione = DatiOperazioneDto.buildDatiOperazione(datiOperazioneDto, Operazione.CANCELLAZIONE_LOGICA_RECORD);
							vecchioProgramma = DatiOperazioneUtil.impostaDatiOperazioneLogin(vecchioProgramma, datiOperazione, siacTAccountRepository);
							//salvo sul db:
							siacRMovgestTsProgrammaRepository.saveAndFlush(vecchioProgramma);
						}
					}				
					siacTMovgestTsDaAggiornare.setSiacRMovgestTsProgrammas(new ArrayList<SiacRMovgestTsProgrammaFin>());
				}
			}
		}	
				
		
		//TIPO:
		List<SiacRClassBaseFin> attributiTClassAggiornati = new ArrayList<SiacRClassBaseFin>();
		if(movimentoGestione instanceof ImpegnoAbstract){
			String codeImp = ((ImpegnoAbstract)movimentoGestione).getTipoImpegno().getCodice();
			SiacRClassBaseFin rClassTipo = salvaAttributoTClass(datiOperazioneDto, attributoInfo, codeImp, CostantiFin.D_CLASS_TIPO_TIPO_IMPEGNO);
			attributiTClassAggiornati.add(rClassTipo);
		}	
		
		EsitoSalvataggioTransazioneElmentare esitoSave = salvaTransazioneElementare(attributoInfo, datiOperazioneDto, movimentoGestione);
		addAll(attributiTClassAggiornati, esitoSave.gettClassSaved());
		aggiornaAllRClassValidiInEntityJPA(attributoInfo, datiOperazioneDto);
		aggiornaAllAttrValidiInEntityJPA(attributoInfo, datiOperazioneDto);
		
		//Termino restituendo l'oggetto di ritorno: 
        return siacTMovgestTsDaAggiornare;
	}
	
	/**
	 * Gestisce l'inserimento o l'aggiornamento degli importi (attuale o iniziale o utilizzabile) per i movimenti gestione
	 * @param siacTMovgestTsDet
	 * @param importo
	 * @param datiOperazioneDto
	 * @param siacTMovgestTs
	 * @param codeTipoImporto
	 * @return
	 */
	private SiacTMovgestTsDetFin saveImporto(SiacTMovgestTsDetFin siacTMovgestTsDet,BigDecimal importo,DatiOperazioneDto datiOperazioneDto,SiacTMovgestTsFin siacTMovgestTs,String codeTipoImporto){
		if(siacTMovgestTsDet==null){
			//siamo in inserimento
			siacTMovgestTsDet = new SiacTMovgestTsDetFin();
		}
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		siacTMovgestTsDet = DatiOperazioneUtil.impostaDatiOperazioneLogin(siacTMovgestTsDet, datiOperazioneDto, siacTAccountRepository);
		if(siacTMovgestTs!=null){
			//in update si puo' passare null
			siacTMovgestTsDet.setSiacTMovgestT(siacTMovgestTs);
		}
		SiacDMovgestTsDetTipoFin siacDMovgestTsDetTipo = siacDMovgestTsDetTipoRepository.findValidoByCode(idEnte, datiOperazioneDto.getTs(),codeTipoImporto).get(0);
		siacTMovgestTsDet.setSiacDMovgestTsDetTipo(siacDMovgestTsDetTipo);
		siacTMovgestTsDet.setMovgestTsDetImporto(importo);
		//salvo sul db:
		siacTMovgestTsDet = siacTMovgestTsDetRepository.saveAndFlush(siacTMovgestTsDet);
		//Termino restituendo l'oggetto di ritorno: 
        return siacTMovgestTsDet;
	}
	
	/**
	 * Gestisce l'inserimento o l'aggiornamento degli importi (attuale o iniziale o utilizzabile) per i movimenti gestione
	 * @param importo
	 * @param datiOperazioneDto
	 * @param siacTMovgestTs
	 * @param codeTipoImporto
	 * @return
	 */
	private SiacTMovgestTsDetFin saveImporto(BigDecimal importo,DatiOperazioneDto datiOperazioneDto,SiacTMovgestTsFin siacTMovgestTs,String codeTipoImporto){
		Integer enteId = datiOperazioneDto.getSiacTEnteProprietario().getUid();
		List<SiacDMovgestTsDetTipoFin> listDetTipo = siacDMovgestTsDetTipoRepository.findValidoByCode(enteId, datiOperazioneDto.getTs(), codeTipoImporto);
		//valutiamo se l'importo e' da inserire o aggiornare: 
		SiacTMovgestTsDetFin siacTMovgestTsDet = null;
		//nel caso sia da inserire SiacTMovgestTsDetFin siacTMovgestTsDet restera' nullo
		if(listDetTipo!=null && listDetTipo.size()>0){
			SiacDMovgestTsDetTipoFin siacDMovgestTsDetTipo = listDetTipo.get(0);
			List<SiacTMovgestTsDetFin> siacTMovgestTsDetList = siacTMovgestTsDetRepository.findValidoByTipo(enteId, datiOperazioneDto.getTs(), siacDMovgestTsDetTipo.getMovgestTsDetTipoCode(), siacTMovgestTs.getUid());
			if(siacTMovgestTsDetList!=null && siacTMovgestTsDetList.size()>0){
				//siamo nel caso in cui l'importo gia' c'e' e va aggiornato
				siacTMovgestTsDet = siacTMovgestTsDetList.get(0);
			}
		}
		return saveImporto(siacTMovgestTsDet, importo, datiOperazioneDto, siacTMovgestTs, codeTipoImporto);
	}
	
	/**
	 * Gestisce il salvataggio dello stato per un movimento gestione, sia in inserimento che in aggiornamento
	 * @param datiOperazioneDto
	 * @param statoCode
	 * @param siacTMovgestTs
	 * @return
	 */
	private SiacRMovgestTsStatoFin saveStatoImpegno(DatiOperazioneDto datiOperazioneDto, String statoCode,SiacTMovgestTsFin siacTMovgestTs){
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		//invalido il vecchio stato (se siamo in aggiornamento):
		if(siacTMovgestTs.getMovgestTsId()!=null && siacTMovgestTs.getMovgestTsId().intValue()>0){
			List<SiacRMovgestTsStatoFin> statoOldL = siacRMovgestTsStatoRepository.findValido(idEnte, datiOperazioneDto.getTs(), siacTMovgestTs.getMovgestTsId());
			if(statoOldL!=null && statoOldL.size()>0){
				SiacRMovgestTsStatoFin statoOld = statoOldL.get(0);
				if(statoOld!=null){
					String codeOld = statoOld.getSiacDMovgestStato().getMovgestStatoCode();
					if(!StringUtilsFin.isEmpty(statoCode) && statoCode.equals(codeOld)){
						//se sono uguali non c'e' nessun passaggio di stato
						return statoOld;
					} else {
						DatiOperazioneUtil.cancellaRecord(statoOld, siacRMovgestTsStatoRepository, datiOperazioneDto, siacTAccountRepository);
					}
				}
			}
		}
		//
		SiacRMovgestTsStatoFin siacRMovgestTsStato = new SiacRMovgestTsStatoFin();
		if(!StringUtilsFin.isEmpty(statoCode)){
			SiacDMovgestStatoFin siacDMovgestStato = siacDMovgestStatoRepository.findValidoByCode(idEnte, statoCode, datiOperazioneDto.getTs()).get(0);
			datiOperazioneDto.setOperazione(Operazione.INSERIMENTO);//perche' in siac_r_movgest_ts_stato INSERISCO
			siacRMovgestTsStato = DatiOperazioneUtil.impostaDatiOperazioneLogin(siacRMovgestTsStato, datiOperazioneDto, siacTAccountRepository);
			siacRMovgestTsStato.setSiacDMovgestStato(siacDMovgestStato);
			siacRMovgestTsStato.setSiacTMovgestT(siacTMovgestTs);
			//salvo sul db:
			siacRMovgestTsStato = siacRMovgestTsStatoRepository.saveAndFlush(siacRMovgestTsStato);
		}
		//Termino restituendo l'oggetto di ritorno: 
        return siacRMovgestTsStato;
	}
	
	/**
	 * Dato un certo movimento ne determina lo stato
	 * @param idEnte
	 * @param impegno
	 * @return
	 */
	private String determinaStatoImpegno(Integer idEnte, MovimentoGestione impegno){
//		Se non  stato caricato un PROVVEDIMENTO lo Stato Impegno viene impostato a  PROVVISORIO
//		 Se  stato caricato un PROVVEDIMENTO 
//		- Se Stato Provvedimento = PROVVISORIO  anche  stato Impegno = PROVVISORIO 
//		- Se Stato Provvedimento = DEFINITIVO occorre verificare che sia valorizzato il codice soggetto o la classe per impostare  Stato Impegno = DEFINITIVO,
//		altrimenti  Stato Impegno =  DEFINITIVO NON LIQUIDABILE.
		boolean isSub = false;
		if (impegno instanceof SubImpegno || impegno instanceof SubAccertamento) {
			isSub = true;
		}
		AttoAmministrativo attoAmm = impegno.getAttoAmministrativo();
		String statoImpegno = CostantiFin.MOVGEST_STATO_PROVVISORIO;
		
		String statoProvvedimento = null;
		
		if(attoAmm!=null && !StringUtilsFin.isEmpty(attoAmm.getStatoOperativo())){
			 statoProvvedimento = attoAmm.getStatoOperativo();
		}else if(impegno.getAttoAmmAnno()!=null && impegno.getAttoAmmNumero()!=null && impegno.getAttoAmmTipoAtto()!=null){
			
			AttoAmministrativo attoAmmSupport = buildAttoAmministrativo(idEnte, impegno.getAttoAmmNumero(), impegno.getAttoAmmTipoAtto().getCodice(), new Integer(impegno.getAttoAmmAnno()), impegno.getIdStrutturaAmministrativa());
			
			SiacTAttoAmmFin attoTrovato= getSiacTAttoAmmFromAttoAmministrativo(attoAmmSupport, idEnte);
			
			List<SiacRAttoAmmStatoFin> listaRAttoAmmStato = attoTrovato.getSiacRAttoAmmStatos();
			SiacRAttoAmmStatoFin rAttoStato = DatiOperazioneUtil.getValido(listaRAttoAmmStato, null);
			statoProvvedimento=rAttoStato.getSiacDAttoAmmStato().getAttoammStatoCode();
			
			//stato da siacTAttoAmmRepository
//			SiacTAttoAmmFin attoTrovato=null;
//			List<SiacTAttoAmmFin> listaSiacTAttoAmm=siacTAttoAmmRepository.getValidoByNumeroAndAnnoAndTipo(idEnte, impegno.getAttoAmmAnno(), getNow(), impegno.getAttoAmmTipoAtto().getCodice(), impegno.getAttoAmmNumero());
//			if(listaSiacTAttoAmm!=null && listaSiacTAttoAmm.size()>0){
//				attoTrovato = listaSiacTAttoAmm.get(0);
//				List<SiacRAttoAmmStatoFin> listaRAttoAmmStato = attoTrovato.getSiacRAttoAmmStatos();
//				if (listaRAttoAmmStato!=null && listaRAttoAmmStato.size()>0) {
//					for (SiacRAttoAmmStatoFin siacRAttoAmmStato : listaRAttoAmmStato) {
//						if (siacRAttoAmmStato.getDataFineValidita()==null && siacRAttoAmmStato.getDataCancellazione()==null) {
//							//Se valido e non cancellato:
//							statoProvvedimento=siacRAttoAmmStato.getSiacDAttoAmmStato().getAttoammStatoCode();
//						}
//					}
//				}
//			}
		}

		if(statoProvvedimento!=null){
			if(statoProvvedimento.equals(CostantiFin.ATTO_AMM_STATO_PROVVISORIO)){
				statoImpegno = CostantiFin.MOVGEST_STATO_PROVVISORIO;
			} else {
				boolean codiceSoggettoValorizzato = impegno.getSoggetto()!=null && !StringUtilsFin.isEmpty(impegno.getSoggetto().getCodiceSoggetto());
				boolean classeValorizzato = impegno.getClasseSoggetto()!=null && !StringUtilsFin.isEmpty(impegno.getClasseSoggetto().getCodice());
				if(isSub || codiceSoggettoValorizzato || classeValorizzato){
					statoImpegno = CostantiFin.MOVGEST_STATO_DEFINITIVO;
				} else {
					statoImpegno = CostantiFin.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE;
				}
			}
		}

		//
		//Termino restituendo l'oggetto di ritorno: 
        return statoImpegno;
	}
	
	/**
	 * Si occupa di valutare quali sub-impegni/accertamenti sono da modificare/inserire/eliminare
	 * @param listaSubImpegni
	 * @param idMovGestId
	 * @param datiOperazioneDto
	 * @return
	 */
	private <I extends MovimentoGestione> SubMovgestInModificaInfoDto valutaSubMovgestDaModificare(List<I> listaSubImpegni, Integer idMovGestId, DatiOperazioneDto datiOperazioneDto){
		
		SubMovgestInModificaInfoDto info =  new SubMovgestInModificaInfoDto();
		
		ArrayList<I> subImpegniDaInserire = new ArrayList<I>();
		ArrayList<I> subImpegniDaModificare = new ArrayList<I>();
		
		ArrayList<I> subImpegniInvariati = new ArrayList<I>();
		
		if(listaSubImpegni!=null && listaSubImpegni.size()>0){
			if(listaSubImpegni.get(0) instanceof SubImpegno){
				info =  new SubMovgestInModificaInfoDto<SubImpegno>();
				subImpegniDaInserire = (ArrayList<I>) new ArrayList<SubImpegno>();
				subImpegniDaModificare = (ArrayList<I>) new ArrayList<SubAccertamento>();
			} else if(listaSubImpegni.get(0) instanceof SubAccertamento){
				info =  new SubMovgestInModificaInfoDto<SubAccertamento>();
				subImpegniDaInserire = (ArrayList<I>) new ArrayList<SubAccertamento>();
				subImpegniDaModificare = (ArrayList<I>) new ArrayList<SubAccertamento>();
			} else {
				return null;//parametri input errati
			}
		}
		
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		
		
		List<SiacTMovgestTsFin> siacTMovgestTsl = siacTMovgestTsRepository.findMovgestTsByMovgest(idEnte, datiOperazioneDto.getTs(), idMovGestId);
		SiacTMovgestTsFin siacTMovgestTs = siacTMovgestTsl.get(0);
		Integer movgestTsId = siacTMovgestTs.getMovgestTsId();
		List<SiacTMovgestTsFin> subImpegniOld = siacTMovgestTsRepository.findListaSiacTMovgestTsFigli(idEnte, datiOperazioneDto.getTs(), movgestTsId);
		
		ArrayList<Integer> listaIdRicevutiDalFe = new ArrayList<Integer>();
		
		//Primo ciclo per mdp associate al soggetto e quelle nuove
		if(listaSubImpegni!=null && listaSubImpegni.size()>0){
			for(I subImpegnoIterato : listaSubImpegni){
				//Se non ha un uid vuol dire che e' nuovo
				if( (subImpegnoIterato.getNumeroBigDecimal()==null || subImpegnoIterato.getNumeroBigDecimal().intValue()==0) && subImpegnoIterato.getUid()==0){
					subImpegniDaInserire.add(subImpegnoIterato);
				} else {
					listaIdRicevutiDalFe.add(subImpegnoIterato.getUid());
					//Valutiamo se ha subito modifiche:
					SiacTMovgestTsFin siacTMovgestTsDaAggiornare = getByNumeroOrUid(subImpegnoIterato, datiOperazioneDto, siacTMovgestTs);
					boolean isModificato = isModificato(subImpegnoIterato,siacTMovgestTsDaAggiornare,datiOperazioneDto);
					if(isModificato){
						subImpegniDaModificare.add(subImpegnoIterato);
					} else {
						subImpegniInvariati.add(subImpegnoIterato);
					}
				}
			}
		}
		
		ArrayList<SiacTMovgestTsFin> subImpegniDaEliminare = new ArrayList<SiacTMovgestTsFin>();
		for(SiacTMovgestTsFin iterateOld : subImpegniOld){
			Integer uuidOld = iterateOld.getUid();
			if(!listaIdRicevutiDalFe.contains(uuidOld)){
				//Non e' presente e va eliminato
				subImpegniDaEliminare.add(iterateOld);
			}
		}
		
		info.setSubImpegniDaEliminare(subImpegniDaEliminare);
		info.setSubImpegniDaInserire(subImpegniDaInserire);
		info.setSubImpegniDaModificare(subImpegniDaModificare);
		info.setSubImpegniInvariati(subImpegniInvariati);
		info.setSubImpegniOld(subImpegniOld);
		info.setListaIdModificati(listaIdRicevutiDalFe);
		
		//Termino restituendo l'oggetto di ritorno: 
        return info;
	}
	
	/**
	 * Va a verificare se il soggetto e' stato modificato rispetto a quanto ancora persistente nel database
	 * @param subImpegno
	 * @param siacTMovgestTsSubImp
	 * @param datiOperazioneDto
	 * @return
	 */
	private boolean isModificatoSoggettoCreditore(MovimentoGestione subImpegno,SiacTMovgestTsFin siacTMovgestTsSubImp,DatiOperazioneDto datiOperazioneDto){
		boolean isModificato = false;
		Timestamp now = datiOperazioneDto.getTs();
		Integer idMovgestTs = siacTMovgestTsSubImp.getMovgestTsId();
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		SiacTSoggettoFin siacTSoggettoNew = getSoggetto(subImpegno, datiOperazioneDto);
		List<SiacRMovgestTsSogFin> listSiacRMovgestTsSog = siacRMovgestTsSogRepository.findValidoMovGestTsSogByIdMovGestAndEnte(idEnte, now, idMovgestTs);
		SiacRMovgestTsSogFin siacRMovgestTsSogOld = CommonUtil.getFirst(listSiacRMovgestTsSog);
		if(!CommonUtil.entrambiNullOrEntrambiIstanziati(siacRMovgestTsSogOld, siacTSoggettoNew)){
			//uno dei due e' null e l'altro non lo e' --> sono sicuramenti diversi
			isModificato = true;
			return isModificato;
		} else if(CommonUtil.entrambiDiversiDaNull(siacRMovgestTsSogOld, siacTSoggettoNew)){
			//c'e' la possibilita' che ci sia una modifica
			SiacTSoggettoFin siacTSoggettoOld = siacRMovgestTsSogOld.getSiacTSoggetto();
			if(!DatiOperazioneUtil.hannoLoStessoUid(siacTSoggettoOld, siacTSoggettoNew)){
				isModificato = true;
				return isModificato;
			}
		}
		return isModificato;
	}
	
	/**
	 * Verifica se per il movimento indicato ci sono delle modifiche (almeno una) rispetto a quanto persistente sul database
	 * @param subImpegno
	 * @param siacTMovgestTsSubImp
	 * @param datiOperazioneDto
	 * @return
	 */
	private boolean isModificato(MovimentoGestione subImpegno,SiacTMovgestTsFin siacTMovgestTsSubImp,DatiOperazioneDto datiOperazioneDto) {
		boolean isModificato = false;
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		Integer idMovgestTs = siacTMovgestTsSubImp.getMovgestTsId();
		Timestamp now = datiOperazioneDto.getTs();
		
		AttributoTClassInfoDto attributoInfo = new AttributoTClassInfoDto();
		attributoInfo.setTipoOggetto(OggettoDellAttributoTClass.T_MOVGEST_TS);
		attributoInfo.setSiacTMovgestTs(siacTMovgestTsSubImp);
		
		//EVENTUALE ATTO AMMINISTRATIVO:
		List<SiacRMovgestTsAttoAmmFin> listSiacRMovgestTsAttoAmm = siacRMovgestTsAttoAmmRepository.findValidoByMovgestTs(idEnte, now, idMovgestTs);
		SiacRMovgestTsAttoAmmFin siacRMovgestTsAttoAmm = CommonUtil.getFirst(listSiacRMovgestTsAttoAmm);
		SiacTAttoAmmFin siacTAttoAmmNew =  getAttoAmministrativo(subImpegno, idEnte);
		if(!CommonUtil.entrambiNullOrEntrambiIstanziati(siacRMovgestTsAttoAmm, siacTAttoAmmNew)){
			//uno dei due e' null e l'altro non lo e' --> sono sicuramenti diversi
			isModificato = true;
			return isModificato;
		} else if(CommonUtil.entrambiDiversiDaNull(siacRMovgestTsAttoAmm, siacTAttoAmmNew)){
			//c'e' la possibilita' che ci sia una modifica
			SiacTAttoAmmFin siacTAttoAmmOld = siacRMovgestTsAttoAmm.getSiacTAttoAmm();
			if(!DatiOperazioneUtil.hannoLoStessoUid(siacTAttoAmmOld, siacTAttoAmmNew)){
				isModificato = true;
				return isModificato;
			}
		}
		
		//EVENTUALE SOGGETTO CREDITORE:
		isModificato = isModificatoSoggettoCreditore(subImpegno, siacTMovgestTsSubImp, datiOperazioneDto);
		if(isModificato){
			return true;
			}
		////////////////////
		
		
		//DESCRIZIONE:
		String descrizioneNew = subImpegno.getDescrizione();

		String descrizioneOld = siacTMovgestTsSubImp.getMovgestTsDesc();
		if(!StringUtilsFin.sonoUguali(descrizioneNew, descrizioneOld)){
			isModificato = true;
			return isModificato;
		}
		
		//IMPORTO:
		List<SiacTMovgestTsDetFin> importoAttualeList = siacTMovgestTsDetRepository.findValidoByTipo(idEnte, now, CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE, idMovgestTs);
		SiacTMovgestTsDetFin siacTMovgestTsDet = CommonUtil.getFirst(importoAttualeList);
		BigDecimal importoOld = siacTMovgestTsDet.getMovgestTsDetImporto();
		BigDecimal importoNew = subImpegno.getImportoAttuale();
		if(!StringUtilsFin.sonoUguali(importoOld, importoNew)){
			isModificato = true;
			return isModificato;
		}
		
		//SCADENZA:
		Timestamp dataScadenzaNew = TimingUtils.convertiDataInTimeStamp(subImpegno.getDataScadenza());
		Timestamp dataScadenzaOld = siacTMovgestTsSubImp.getMovgestTsScadenzaData();
		if(!StringUtilsFin.sonoUguali(dataScadenzaNew, dataScadenzaOld)){
			isModificato = true;
			return isModificato;
		}
		
		if(subImpegno instanceof SubImpegno){
			//CIG:
			isModificato = isAttributoModificato(((SubImpegno)subImpegno).getCig(), CostantiFin.T_ATTR_CODE_CIG, datiOperazioneDto, attributoInfo);
			if(isModificato){
				return isModificato;
			}
		}
		
		isModificato = isModificataTransazioneElementare(attributoInfo, subImpegno, datiOperazioneDto);
		if(isModificato){
			return isModificato;
		}
		
		//Termino restituendo l'oggetto di ritorno: 
        return isModificato;
	}

	private int getMaxCodePiuUno(List<SiacTMovgestTsFin> subImpegniOld){
		//calcolo il max code tra quelli presenti:
		int numermoSubImpegno = 1;
		if(subImpegniOld!=null && subImpegniOld.size()>0){
			for(SiacTMovgestTsFin oldIterato: subImpegniOld){
				String codeIt = oldIterato.getMovgestTsCode();
				try{
					int numeroIt = Integer.parseInt(codeIt);
					if(numeroIt>numermoSubImpegno){
						numermoSubImpegno = numeroIt;
					}
				}catch(Exception e){
					
				}
			}
			numermoSubImpegno = numermoSubImpegno+1;
		} else {
			 numermoSubImpegno = 1;
		}
		//Termino restituendo l'oggetto di ritorno: 
        return numermoSubImpegno;
	}
	
	/**
	 * metodo da richiamare ad inizio del servizio di aggiorna impegno, serve per salvare nell'oggetto ImpegnoInModificaInfoDto
	 * i dati che richiedono interrogazione del database e che conviene quindi invocare una sola volta all'inizio e passarli
	 * di mano tra i vari metodi che compongono l'aggiorna impegno per evitare di ricaricarli in continuazione
	 * @return
	 */
	public ImpegnoInModificaInfoDto getDatiGeneraliImpegnoInAggiornamento(MovimentoGestione impegno,DatiOperazioneDto datiOperazioneDto, Bilancio bilancio){
		
		ImpegnoInModificaInfoDto impegnoInModificaInfoDto = new ImpegnoInModificaInfoDto();
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		
		String tipoMovimentoTrattato = "";
		if(impegno instanceof Impegno){
			tipoMovimentoTrattato = CostantiFin.MOVGEST_TIPO_IMPEGNO;
		}else if(impegno instanceof Accertamento){
			tipoMovimentoTrattato = CostantiFin.MOVGEST_TIPO_ACCERTAMENTO;
		}
		
		Integer annoInteger = impegno.getAnnoMovimento();
		BigDecimal numero = impegno.getNumeroBigDecimal();
		
		
				
		SiacTMovgestFin siacTMovgest = null;
		List<SiacTMovgestFin> siacTMovgestS = siacTMovgestRepository.findByEnteAnnoNumeroBilancioValido(idEnte, annoInteger, numero, tipoMovimentoTrattato, String.valueOf(bilancio.getAnno()),getNow());
		if(siacTMovgestS!=null && siacTMovgestS.size()>0){
			siacTMovgest = siacTMovgestS.get(0);
		} else if(impegno instanceof Impegno){
			if(((Impegno)impegno).getElencoSubImpegni()!=null && ((Impegno)impegno).getElencoSubImpegni().size()>0){
				
				SubImpegno primoSubDaFe = ((Impegno)impegno).getElencoSubImpegni().get(0);
				MovgestPkDto chiaveMov = new MovgestPkDto(String.valueOf(bilancio.getAnno()), primoSubDaFe.getAnnoMovimento(), primoSubDaFe.getNumeroImpegnoPadre(), tipoMovimentoTrattato);
				
				MovGestMainModelEntityInfoDto oggettoConSub = getListaSiactTMovgestTs(chiaveMov,((Impegno)impegno).getElencoSubImpegni(),datiOperazioneDto);
				List<MovGestModelEntityInfoDto> subs = oggettoConSub.getListaSub();
				SiacTMovgestTsFin primoSub = subs.get(0).getSiacTMovgestTs();
				siacTMovgest = siacTMovgestRepository.findOne(primoSub.getMovgestTsIdPadre());
				Boolean parereFinanziario = impegno.getParereFinanziario();
				// setto il parare finanziario
				// per l'imepgno arriva dal chiamante
				siacTMovgest.setParereFinanziario(parereFinanziario);
			}	
		} else if(impegno instanceof Accertamento){
			if(((Accertamento)impegno).getElencoSubAccertamenti()!=null && ((Accertamento)impegno).getElencoSubAccertamenti().size()>0){
				SubAccertamento primoSubDaFe = ((Accertamento)impegno).getElencoSubAccertamenti().get(0);
				MovgestPkDto chiaveMov = new MovgestPkDto(String.valueOf(bilancio.getAnno()), primoSubDaFe.getAnnoMovimento(), primoSubDaFe.getNumeroAccertamentoPadre(), tipoMovimentoTrattato);
				MovGestMainModelEntityInfoDto oggettoConSub = getListaSiactTMovgestTsAccertamenti(chiaveMov,((Accertamento)impegno).getElencoSubAccertamenti(),datiOperazioneDto);
				List<MovGestModelEntityInfoDto> subs = oggettoConSub.getListaSub();
				// da gestire	List<SiacTMovgestTsFin> subs = getListaSiactTMovgestTs(((Accertamento)impegno).getElencoSubAccertamenti(),datiOperazioneDto);
				SiacTMovgestTsFin primoSub = subs.get(0).getSiacTMovgestTs();
				siacTMovgest = siacTMovgestRepository.findOne(primoSub.getMovgestTsIdPadre());
				
				// setto il parare finanziario
				// per l'accertamento è sempre true
				siacTMovgest.setParereFinanziario(Boolean.TRUE);
			}
		}
						
		SiacTMovgestTsFin siacTMovgestTs = siacTMovgestTsRepository.findMovgestTsByMovgest(idEnte, datiOperazioneDto.getTs(), siacTMovgest.getMovgestId()).get(0);
		
		
		impegno = EntityToModelConverter.setChiaveCapitolo(siacTMovgest, impegno);
		//carico il provvedimento OLD:
		SiacRMovgestTsAttoAmmFin vecchioAtto = getAtto(idEnte, datiOperazioneDto.getTs(), siacTMovgestTs);
		impegnoInModificaInfoDto.setSiacRMovgestTsAttoAmmOld(vecchioAtto);
		
		//////////////////////
		
		//EVENTUALE RIBALTAMENTO ALL'ANNO DOPO:
		SiacTMovgestFin siacTMovgestTsResiduo = individuaMovGestResiduo(datiOperazioneDto, impegno.getAnnoMovimento(), impegno.getNumeroBigDecimal().intValue(),tipoMovimentoTrattato);
		Integer chiaveCapitoloResiduo = null;
		if(siacTMovgestTsResiduo!=null){
			chiaveCapitoloResiduo = EntityToModelConverter.getChiaveCapitolo(siacTMovgestTsResiduo);
		}
		impegnoInModificaInfoDto.setChiaveCapitoloResiduo(chiaveCapitoloResiduo);
		//////////////////////7
		
		
		impegnoInModificaInfoDto.setSiacTMovgest(siacTMovgest);
		impegnoInModificaInfoDto.setSiacTMovgestTs(siacTMovgestTs);
		//Termino restituendo l'oggetto di ritorno: 
        return impegnoInModificaInfoDto;
	}
	
	public AccertamentoDaVincolareInfoDto getDatiGeneraliAccertamentoDaVincolare(Accertamento accertamento,DatiOperazioneDto datiOperazioneDto, Bilancio bilancio){
		
		AccertamentoDaVincolareInfoDto impegnoInModificaInfoDto = new AccertamentoDaVincolareInfoDto();
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		
		String tipoMovimentoTrattato = CostantiFin.MOVGEST_TIPO_ACCERTAMENTO;
		
		Integer annoInteger = accertamento.getAnnoMovimento();
		BigDecimal numero = accertamento.getNumeroBigDecimal();
		
		SiacTMovgestFin siacTMovgest = null;
		List<SiacTMovgestFin> siacTMovgestS = siacTMovgestRepository.findByEnteAnnoNumeroBilancioValido(idEnte, annoInteger, numero, tipoMovimentoTrattato, String.valueOf(bilancio.getAnno()),getNow());
		if(siacTMovgestS!=null && siacTMovgestS.size()>0){
			siacTMovgest = siacTMovgestS.get(0);
		}
		SiacTMovgestTsFin siacTMovgestTs = null;
		
		Integer chiaveCapitolo = null;
		if(siacTMovgest!=null){
			siacTMovgestTs = siacTMovgestTsRepository.findMovgestTsByMovgest(idEnte, datiOperazioneDto.getTs(), siacTMovgest.getMovgestId()).get(0);
			chiaveCapitolo = EntityToModelConverter.getChiaveCapitolo(siacTMovgest);
		}
		impegnoInModificaInfoDto.setChiaveCapitolo(chiaveCapitolo);
		//////////////////////
		
		//EVENTUALE RIBALTAMENTO ALL'ANNO DOPO:
		SiacTMovgestFin siacTMovgestResiduo = individuaMovGestResiduo(datiOperazioneDto, accertamento.getAnnoMovimento(), accertamento.getNumeroBigDecimal().intValue(),tipoMovimentoTrattato);
		Integer chiaveCapitoloResiduo = null;
		if(siacTMovgestResiduo!=null){
			chiaveCapitoloResiduo = EntityToModelConverter.getChiaveCapitolo(siacTMovgestResiduo);
		}
		impegnoInModificaInfoDto.setChiaveCapitoloResiduo(chiaveCapitoloResiduo);
		//////////////////////
		
		
		impegnoInModificaInfoDto.setSiacTMovgest(siacTMovgest);
		impegnoInModificaInfoDto.setSiacTMovgestTs(siacTMovgestTs);
		//Termino restituendo l'oggetto di ritorno: 
        return impegnoInModificaInfoDto;
	}
	
	public Integer getChiaveCapitoloImpegnoResiduo(DatiOperazioneDto datiOperazione, int annoMovimento, int numeroMovimento){
		SiacTMovgestFin siacTMovgestTsResiduo = individuaImpegnoResiduo(datiOperazione, annoMovimento, numeroMovimento);
		Integer chiaveCapitoloResiduo = null;
		if(siacTMovgestTsResiduo!=null){
			chiaveCapitoloResiduo = EntityToModelConverter.getChiaveCapitolo(siacTMovgestTsResiduo);
		}
		//Termino restituendo l'oggetto di ritorno: 
        return chiaveCapitoloResiduo;
	}
	
	public Integer getChiaveCapitoloAccertamentoResiduo(DatiOperazioneDto datiOperazione, int annoMovimento, int numeroMovimento){
		SiacTMovgestFin siacTMovgestTsResiduo = individuaAccertamentoResiduo(datiOperazione, annoMovimento, numeroMovimento);
		Integer chiaveCapitoloResiduo = null;
		if(siacTMovgestTsResiduo!=null){
			chiaveCapitoloResiduo = EntityToModelConverter.getChiaveCapitolo(siacTMovgestTsResiduo);
		}
		//Termino restituendo l'oggetto di ritorno: 
        return chiaveCapitoloResiduo;
	}
	
	/**
	 * semplice wrapper per siacRMovgestTsAttoAmmRepository.findValidoByMovgestTs
	 * @param idEnte
	 * @param timestamp
	 * @param siacTMovgestTs
	 * @return
	 */
	private SiacRMovgestTsAttoAmmFin getAtto(Integer idEnte, Timestamp timestamp, SiacTMovgestTsFin siacTMovgestTs){
		List<SiacRMovgestTsAttoAmmFin> lista = siacRMovgestTsAttoAmmRepository.findValidoByMovgestTs(idEnte, timestamp, siacTMovgestTs.getMovgestTsId());
		SiacRMovgestTsAttoAmmFin vecchioAtto = null;
		if(lista!=null && lista.size()>0){
			vecchioAtto = lista.get(0);
		}
		//Termino restituendo l'oggetto di ritorno: 
        return vecchioAtto;
	}
	
	/**
	 * Si occupa di valutare quali sub-impegni/accertamenti sono da modificare/inserire/eliminare
	 * @param impegno
	 * @param impegnoInModificaInfoDto
	 * @param datiOperazioneDto
	 * @param bilancio
	 * @return
	 */
	public ImpegnoInModificaInfoDto valutaSubImp(MovimentoGestione impegno, ImpegnoInModificaInfoDto impegnoInModificaInfoDto,
			DatiOperazioneDto datiOperazioneDto, Bilancio bilancio){
		
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		
		List<SubImpegno> elencoSubImpegni=null;
		List<SubAccertamento> elencoSubAccertamenti = null;
		String tipoMovimentoTrattato = "";
		if(impegno instanceof Impegno){
			tipoMovimentoTrattato = CostantiFin.MOVGEST_TIPO_IMPEGNO;
			elencoSubImpegni = ((Impegno) impegno).getElencoSubImpegni();
		}else if(impegno instanceof Accertamento){
			tipoMovimentoTrattato = CostantiFin.MOVGEST_TIPO_ACCERTAMENTO;
			elencoSubAccertamenti = ((Accertamento) impegno).getElencoSubAccertamenti();
		}
		
		Integer annoInteger = impegno.getAnnoMovimento();
		BigDecimal numero = impegno.getNumeroBigDecimal();
		
		
		SiacTMovgestFin siacTMovgest = null;
		List<SiacTMovgestFin> siacTMovgestS = siacTMovgestRepository.findByEnteAnnoNumeroBilancioValido(idEnte, annoInteger, numero, tipoMovimentoTrattato, String.valueOf(bilancio.getAnno()),getNow());
		if(siacTMovgestS!=null && siacTMovgestS.size()>0){
			siacTMovgest = siacTMovgestS.get(0);
		} else if(impegno instanceof Impegno){
			if(((Impegno)impegno).getElencoSubImpegni()!=null && ((Impegno)impegno).getElencoSubImpegni().size()>0){
				
				SubImpegno primoSubDaFe = ((Impegno)impegno).getElencoSubImpegni().get(0);
				MovgestPkDto chiaveMov = new MovgestPkDto(String.valueOf(bilancio.getAnno()), primoSubDaFe.getAnnoMovimento(), primoSubDaFe.getNumeroImpegnoPadre(), tipoMovimentoTrattato);
				
				MovGestMainModelEntityInfoDto oggettoConSub = getListaSiactTMovgestTs(chiaveMov,((Impegno)impegno).getElencoSubImpegni(),datiOperazioneDto);
				List<MovGestModelEntityInfoDto> subs = oggettoConSub.getListaSub();
				SiacTMovgestTsFin primoSub = subs.get(0).getSiacTMovgestTs();
				siacTMovgest = siacTMovgestRepository.findOne(primoSub.getMovgestTsIdPadre());
			}	
		} else if(impegno instanceof Accertamento){
			if(((Accertamento)impegno).getElencoSubAccertamenti()!=null && ((Accertamento)impegno).getElencoSubAccertamenti().size()>0){
				SubAccertamento primoSubDaFe = ((Accertamento)impegno).getElencoSubAccertamenti().get(0);
				MovgestPkDto chiaveMov = new MovgestPkDto(String.valueOf(bilancio.getAnno()), primoSubDaFe.getAnnoMovimento(), primoSubDaFe.getNumeroAccertamentoPadre(), tipoMovimentoTrattato);
				MovGestMainModelEntityInfoDto oggettoConSub = getListaSiactTMovgestTsAccertamenti(chiaveMov,((Accertamento)impegno).getElencoSubAccertamenti(),datiOperazioneDto);
				List<MovGestModelEntityInfoDto> subs = oggettoConSub.getListaSub();
				// da gestire	List<SiacTMovgestTsFin> subs = getListaSiactTMovgestTs(((Accertamento)impegno).getElencoSubAccertamenti(),datiOperazioneDto);
				SiacTMovgestTsFin primoSub = subs.get(0).getSiacTMovgestTs();
				siacTMovgest = siacTMovgestRepository.findOne(primoSub.getMovgestTsIdPadre());
			}
		}
						
		SiacTMovgestTsFin siacTMovgestTs = siacTMovgestTsRepository.findMovgestTsByMovgest(idEnte, datiOperazioneDto.getTs(), siacTMovgest.getMovgestId()).get(0);
		if(impegno instanceof Impegno){
			SubMovgestInModificaInfoDto infoSubValutati = valutaSubMovgestDaModificare(elencoSubImpegni, siacTMovgest.getMovgestId(), datiOperazioneDto);
			impegnoInModificaInfoDto.setInfoSubValutati(infoSubValutati);
		}else if(impegno instanceof Accertamento){
			//da gestire ** PAOLO
			SubMovgestInModificaInfoDto infoSubAccValutati = valutaSubMovgestDaModificare(elencoSubAccertamenti, siacTMovgest.getMovgestId(), datiOperazioneDto);
			impegnoInModificaInfoDto.setInfoSubValutati(infoSubAccValutati);
		}
		//Termino restituendo l'oggetto di ritorno: 
        return impegnoInModificaInfoDto;
	}
		
	/**
	 * Si occupa di aggiornare (inserendo, aggiornando, eliminando) i sub-impegni/accertamenti
	 * @param listaSubImpegni
	 * @param impegno
	 * @param datiOperazioneDto
	 * @param impegnoInModificaInfoDto
	 */
	private <Sub extends MovimentoGestione, Mvg extends MovimentoGestione> EsitoAggiornamentoSubMovGestTs aggiornaSubMovGest(List<Sub> listaSubImpegni, Mvg impegno,DatiOperazioneDto datiOperazioneDto, 
			ImpegnoInModificaInfoDto impegnoInModificaInfoDto, Bilancio bilancio){
//		Se nell'elenco dei subimpegni da gestire sono assenti subimpegni presenti in archivio, questi devono essere cancellati
//		Se nell'elenco dei subimpegni da gestire sono presenti subimpegni non presenti in archivio (si possono presentare 2 casistiche: 
//				nuovi SUB di un impegno relativo al bilancio in gestione oppure nuovi SUB relativi all'impegno residuo per i quali deve essere
//				indicato anche il SubImpegno.numero),  questi devono essere inseriti. Il progressivo univoco del subimpegno viene calcolato 
//				nellambito dellimpegno. Se per l'entita passata in input il progressivo  gi presente, il sistema non provvede ad effettuare il calcolo.
//			Se nellelenco dei subimpegni da gestire sono presenti subimpegni esistenti in archivio con dati modificati, questi vengono aggiornati. 
//		-	Se stato subImpegno = PROVVISORIO, impostare importo iniziale =  importo attuale
//			Per i subimpegni da annullare, se non presente data annullamednto impostare la data corrente. 
		EsitoAggiornamentoSubMovGestTs esito = new EsitoAggiornamentoSubMovGestTs();
		List<SiacTMovgestTsFin> inseriti = new ArrayList<SiacTMovgestTsFin>();
		List<SiacTMovgestTsFin> aggiornati = new ArrayList<SiacTMovgestTsFin>();
		List<SiacTMovgestTsFin> eliminati = new ArrayList<SiacTMovgestTsFin>();
		SiacTMovgestFin siacTMovgest = impegnoInModificaInfoDto.getSiacTMovgest();
		SiacTMovgestTsFin siacTMovgestTs = impegnoInModificaInfoDto.getSiacTMovgestTs();
		
		Integer idAmbito = datiOperazioneDto.getSiacDAmbito().getAmbitoId();
		
		SubMovgestInModificaInfoDto infoValutati = impegnoInModificaInfoDto.getInfoSubValutati();
		
		ArrayList<Sub> subImpegniDaInserire = infoValutati.getSubImpegniDaInserire();
		ArrayList<Sub> subImpegniDaModificare =  infoValutati.getSubImpegniDaModificare();
		
		ArrayList<Sub> subImpegniInvariati =  infoValutati.getSubImpegniInvariati();
		
		List<SiacTMovgestTsFin> subImpegniOld = infoValutati.getSubImpegniOld();
		ArrayList<SiacTMovgestTsFin> subImpegniDaEliminare = infoValutati.getSubImpegniDaEliminare();
		
		int numermoSubImpegno = getMaxCodePiuUno(subImpegniOld);
		
		//Inserimento Nuovi
		DatiOperazioneDto datiOperazioneInserimento = DatiOperazioneDto.buildDatiOperazione(datiOperazioneDto, Operazione.INSERIMENTO); 
		if(subImpegniDaInserire != null && subImpegniDaInserire.size() > 0){
			for(Sub subImpegnoToInsert : subImpegniDaInserire){
				String code = Integer.toString(numermoSubImpegno);
				//Se sono in doppia gestione devo inserire un sub ribaltato con un numero specifico
				if (impegnoInModificaInfoDto.isDoppiaGestione() && subImpegnoToInsert.getNumeroBigDecimal()!=null) {
					code=subImpegnoToInsert.getNumeroBigDecimal().toString();
				}
				datiOperazioneDto.setOperazione(Operazione.INSERIMENTO);
				if(impegno instanceof ImpegnoAbstract){
					 ((SubImpegno) subImpegnoToInsert).setTipoImpegno(((ImpegnoAbstract)impegno).getTipoImpegno());
				}
				SiacTMovgestTsFin inserito = inserisciMovgestTs(siacTMovgest, subImpegnoToInsert, datiOperazioneInserimento, code, siacTMovgestTs, idAmbito,bilancio);
				inseriti.add(inserito);
				numermoSubImpegno++;
			}
			
			
		}
		
		//da modificare:
		DatiOperazioneDto datiOperazioneModifica = DatiOperazioneDto.buildDatiOperazione(datiOperazioneDto, Operazione.MODIFICA); 
		if(subImpegniDaModificare != null && subImpegniDaModificare.size() > 0){
			for(Sub subImpegnoToUpdate : subImpegniDaModificare){
				SiacTMovgestTsFin siacTMovgestTsDaAggiornare = getByNumeroOrUid(subImpegnoToUpdate, datiOperazioneDto, siacTMovgestTs);
				if(siacTMovgestTsDaAggiornare!=null){
					// PAOLO
					if(impegno instanceof ImpegnoAbstract){
						((SubImpegno) subImpegnoToUpdate).setTipoImpegno(((ImpegnoAbstract)impegno).getTipoImpegno());
					}
					datiOperazioneModifica.setOperazione(Operazione.MODIFICA);
					SiacTMovgestTsFin aggiornato = aggiornaMovgestTs(siacTMovgestTsDaAggiornare, siacTMovgest, subImpegnoToUpdate, datiOperazioneModifica, siacTMovgestTs, idAmbito,bilancio);
					aggiornati.add(aggiornato);
				}
			}
		}

		//da eliminare:
		for(SiacTMovgestTsFin iterato : subImpegniDaEliminare){
			DatiOperazioneUtil.cancellaRecord(iterato, siacTMovgestTsRepository, datiOperazioneDto, siacTAccountRepository);
			String statoImpegnoCode = null;//NON ESISTE LO STATO CANCELLATO
			saveStatoImpegno(datiOperazioneInserimento, statoImpegnoCode,iterato);
			eliminati.add(iterato);
		}
		
		//invariati
		List<SiacTMovgestTsFin> invariati = new ArrayList<SiacTMovgestTsFin>();
		if(subImpegniInvariati!=null && subImpegniInvariati.size()>0){
			for(Sub subImpegnoToUpdate : subImpegniInvariati){
				SiacTMovgestTsFin siacTMovgestTsInvariato = getByNumeroOrUid(subImpegnoToUpdate, datiOperazioneDto, siacTMovgestTs);
				invariati.add(siacTMovgestTsInvariato);
			}
		}
		
		//SETTO L'OGGETTO DI RITORNO:
		esito.setAggiornati(aggiornati);
		esito.setInseriti(inseriti);
		esito.setEliminati(eliminati);
		esito.setInvariati(invariati);
			
			
		//Termino restituendo l'oggetto di ritorno: 
        return esito;
	}
	
	/**
	 * Restituisce SiacTMovgestTsFin relativo ad un subimpegno ricevuto dal front end **PAOLO
	 * @param subImpegno
	 * @param datiOperazioneDto
	 * @param testataImpegno
	 * @return
	 */
	private SiacTMovgestTsFin getByNumeroOrUid(MovimentoGestione subImpegno, DatiOperazioneDto datiOperazioneDto, SiacTMovgestTsFin testataImpegno){
		SiacTMovgestTsFin siacTMovgestTsSubImpegno = null;
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		 if(subImpegno.getUid()>0){
			siacTMovgestTsSubImpegno = siacTMovgestTsRepository.findOne(subImpegno.getUid());
		} else if(subImpegno.getNumeroBigDecimal()!=null && subImpegno.getNumeroBigDecimal().intValue()>0){
			String codeIterato = subImpegno.getNumeroBigDecimal().toString();
			List<SiacTMovgestTsFin> l = siacTMovgestTsRepository.findFiglioByCode(idEnte, datiOperazioneDto.getTs(), testataImpegno.getMovgestTsId(), codeIterato);
			if(l!=null && l.size()>0){
				siacTMovgestTsSubImpegno = l.get(0);
			}
		} 
		//Termino restituendo l'oggetto di ritorno: 
        return siacTMovgestTsSubImpegno;
	}
	
	/**
	 * E' il metodo unificato che effettua l'aggiornamento di impegni e' accertamenti.
	 * @param richiedente
	 * @param ente
	 * @param bilancio
	 * @param impegnoDaAggiornare
	 * @param soggettoCreditore (17-07-2015 rm eliminato perchè parametro non usato!)
	 * @param unitaElemDiGest
	 * @param datiOperazioneDto
	 * @param impegnoInModificaInfoDto
	 * @param capitoliInfo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public EsitoAggiornamentoMovimentoGestioneDto aggiornaMovimento(Richiedente richiedente, Ente ente, Bilancio bilancio, MovimentoGestione impegnoDaAggiornare,  UnitaElementareGestione unitaElemDiGest,
			DatiOperazioneDto datiOperazioneDto, ImpegnoInModificaInfoDto impegnoInModificaInfoDto,CapitoliInfoDto capitoliInfo){
		EsitoAggiornamentoMovimentoGestioneDto esito=new EsitoAggiornamentoMovimentoGestioneDto();
		
		Integer annoMovimento = impegnoDaAggiornare.getAnnoMovimento();
		BigDecimal numeroMovimento = impegnoDaAggiornare.getNumeroBigDecimal();
		
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getUid();
		
		//	Dobbiamo capire se il metodo e' stato invocato per:
			//A. Aggiornare l'impegno/accertamento in senso stretto
			//B. Aggiornare le modifiche movimento
		boolean presentiModifiche = presentiModifiche(impegnoDaAggiornare, datiOperazioneDto);
		
		SiacTMovgestTsFin siacTMovgestTsAggiornato =  null;
		SiacTMovgestFin siacTMovgestAggiornato = null;
		List<SiacTMovgestTsFin> aggiornati = null;
		List<SiacTMovgestTsFin> inseriti = null;
		List<SiacTMovgestTsFin> invariati = null;
		List<SubImpegno> subImpegnoList = null;
		List<SubAccertamento> subAccertamentoList = null;
		List<ModificaMovimentoGestioneSpesa> movimentiImpegno = null;
		List<ModificaMovimentoGestioneEntrata> movimentiAccertamenti = null;
		
		if(!presentiModifiche){
			//CASO : A. Aggiornare l'impegno/accertamento in senso stretto
			///////////////////////////////////////IMPEGNO PRINCIPALE//////////////////////////////////////////////////
			siacTMovgestAggiornato = aggiornaSiacTMovgest(impegnoDaAggiornare, datiOperazioneDto, bilancio);
			siacTMovgestTsAggiornato = aggiornaSiacTMovgestTs(impegnoDaAggiornare, datiOperazioneDto, siacTMovgestAggiornato,bilancio,impegnoInModificaInfoDto);
			
			EsitoAggiornamentoSubMovGestTs esitoAggiornamentoSub = null;
			if(impegnoDaAggiornare instanceof Impegno){
				//GESTIONE SUB IMPEGNI
				esitoAggiornamentoSub = aggiornaSubMovGest(((Impegno)impegnoDaAggiornare).getElencoSubImpegni(), (Impegno)impegnoDaAggiornare, datiOperazioneDto, impegnoInModificaInfoDto,bilancio);
				subImpegnoList = ((Impegno)impegnoDaAggiornare).getElencoSubImpegni();
			} else if(impegnoDaAggiornare instanceof Accertamento){
				//GESTIONE SUB ACCERTAMENTI
				esitoAggiornamentoSub = aggiornaSubMovGest(((Accertamento)impegnoDaAggiornare).getElencoSubAccertamenti(), (Accertamento)impegnoDaAggiornare, datiOperazioneDto,impegnoInModificaInfoDto,bilancio);
				subAccertamentoList = ((Accertamento)impegnoDaAggiornare).getElencoSubAccertamenti();
			}
			if(esitoAggiornamentoSub!=null){
				aggiornati = esitoAggiornamentoSub.getAggiornati();
				inseriti = esitoAggiornamentoSub.getInseriti();
				invariati = esitoAggiornamentoSub.getInvariati();
				
				// setto flag su sub inseriti sull'oggetto esito in modo che poi il service possa sapere se gestire la prima nota
				esito.setSubInseriti(true);
				
			}
			List<SiacTMovgestTsFin> listaSiacTMovgestTs = new ArrayList<SiacTMovgestTsFin>();
			listaSiacTMovgestTs.add(siacTMovgestTsAggiornato);
			listaSiacTMovgestTs = addAll(listaSiacTMovgestTs, aggiornati);
			listaSiacTMovgestTs = addAll(listaSiacTMovgestTs, inseriti);
			listaSiacTMovgestTs = addAll(listaSiacTMovgestTs, invariati);
			siacTMovgestAggiornato.setSiacTMovgestTs(listaSiacTMovgestTs);
			//////////////
			if(impegnoDaAggiornare instanceof Impegno){
				movimentiImpegno = estraiElencoModificheMovimentoGestioneSpesa(richiedente, siacTMovgestTsAggiornato);
			} else if(impegnoDaAggiornare instanceof Accertamento){
				movimentiAccertamenti = estraiElencoModificheMovimentoGestioneEntrata(richiedente, siacTMovgestTsAggiornato);
			}
			////////////////////////////////////////////////////////////////////////////
		} else {
			//CASO B. Aggiornare le modifiche movimento
			siacTMovgestAggiornato = siacTMovgestRepository.findOne(impegnoDaAggiornare.getUid()); 
			int movgestId = siacTMovgestAggiornato.getUid();
			siacTMovgestTsAggiornato = siacTMovgestTsRepository.findMovgestTsByMovgest(idEnte, datiOperazioneDto.getTs(), movgestId).get(0);
			//Chiamo la routine di gestione delle modifiche:
			EsitoGestioneModificheMovimentiDto esitoGestMod = gestioneModificheMovimenti(impegnoDaAggiornare, datiOperazioneDto);
			//uno dei due sara' null a seconda del tipo di movimento che stiamo gestendo:
			subImpegnoList = esitoGestMod.getSubImpegnoList();
			subAccertamentoList = esitoGestMod.getSubAccertamentoList();
			//////////////
			//uno dei due sara' null a seconda del tipo di movimento che stiamo gestendo:
			movimentiImpegno = esitoGestMod.getMovimentiImpegno();
			movimentiAccertamenti = esitoGestMod.getMovimentiAccertamenti();
			////////////////////////////////////////////////////////////////////////////
			invariati  = siacTMovgestTsRepository.findListaSiacTMovgestTsFigli(idEnte, datiOperazioneDto.getTs(), siacTMovgestTsAggiornato.getMovgestTsId());
		}
		
		
		//DOPPIA GESTIONE
		boolean inserireDoppiaGestione = inserireDoppiaGestione(bilancio, impegnoDaAggiornare, datiOperazioneDto);
		if(inserireDoppiaGestione){
			

				DatiOperazioneDto datiOperazioneDtoDoppiaGestione = new DatiOperazioneDto(System.currentTimeMillis(), Operazione.MODIFICA, datiOperazioneDto.getSiacTEnteProprietario(), richiedente.getAccount().getId());

				if(impegnoDaAggiornare instanceof Impegno){
					//Doppia Gestione Impegno
					Impegno impegnoClonePerDoppiaGest = clone((Impegno)impegnoDaAggiornare);
					List<Errore> erroriDaDoppiaGestione=aggiornamentoInDoppiaGestioneImpegno(richiedente, ente, bilancio, impegnoClonePerDoppiaGest,
							datiOperazioneDtoDoppiaGestione,capitoliInfo,impegnoInModificaInfoDto.getInfoVincoliValutati(),ChiamanteDoppiaGestImpegno.AGGIORNA_IMPEGNO);
					
					if (erroriDaDoppiaGestione!=null && erroriDaDoppiaGestione.size()>0) {
						esito.setListaErrori(erroriDaDoppiaGestione);
						return esito;
					}
				} else if(impegnoDaAggiornare instanceof Accertamento ){
					//Doppia Gestione Accertamento
					Accertamento accClonePerDoppiaGest = clone((Accertamento)impegnoDaAggiornare);
					List<Errore> erroriDaDoppiaGestione=aggiornamentoInDoppiaGestioneAccertamento(richiedente, ente, bilancio, accClonePerDoppiaGest,
							datiOperazioneDtoDoppiaGestione,capitoliInfo);
					
					if (erroriDaDoppiaGestione!=null && erroriDaDoppiaGestione.size()>0) {
						esito.setListaErrori(erroriDaDoppiaGestione);
						return esito;
					}
				}
		}
		/////
		
		// Ritorno risultati:
		if(impegnoDaAggiornare instanceof Impegno){
		
			Impegno impegnoAggiornato = (Impegno)map(siacTMovgestAggiornato, Impegno.class, FinMapId.SiacTMovgest_Impegno);
				
			impegnoAggiornato.setListaModificheMovimentoGestioneSpesa(movimentiImpegno);
			for(SiacTMovgestTsDetFin det : siacTMovgestTsAggiornato.getSiacTMovgestTsDets()){
				if(det.getSiacDMovgestTsDetTipo().getMovgestTsDetTipoCode().equalsIgnoreCase(CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE)){
					impegnoAggiornato.setImportoAttuale(det.getMovgestTsDetImporto());
				}
			}
			
			DisponibilitaMovimentoGestioneContainer disponibilitaSubimpegnare;
			if(subImpegnoList != null && subImpegnoList.size() > 0){
				disponibilitaSubimpegnare = calcolaDisponibilitaImpegnoASubImpegnareEValorizzaTotaleSubImpegni(impegnoAggiornato);
			} else {
				disponibilitaSubimpegnare = new DisponibilitaMovimentoGestioneContainer(impegnoAggiornato.getImportoAttuale(), "Se non sono presenti sub, la disponibilita' deve essere pari a ZERO");
			}
			impegnoAggiornato.setDisponibilitaSubimpegnare(disponibilitaSubimpegnare.getDisponibilita());
			// SIAC-6695
			impegnoAggiornato.setMotivazioneDisponibilitaSubImpegnare(disponibilitaSubimpegnare.getMotivazione());
			
			impegnoAggiornato = EntityToModelConverter.siacTMovgestEntityToImpegnoModel(siacTMovgestAggiornato, impegnoAggiornato, null);
			
			////////
			List<ST> listaSubRicostruita = componiListaSubPerResponse((List<ST>) subImpegnoList, CostantiFin.MOVGEST_TIPO_IMPEGNO, richiedente, datiOperazioneDto, inseriti, aggiornati, 
					invariati, annoMovimento, numeroMovimento, siacTMovgestAggiornato);
			impegnoAggiornato.setElencoSubImpegni((List<SubImpegno>) listaSubRicostruita);
			///////////
			
			esito.setMovimentoGestione(impegnoAggiornato);
			return esito;
			
		} else if(impegnoDaAggiornare instanceof Accertamento){
			Accertamento accertamentoAggiornato = (Accertamento)map(siacTMovgestAggiornato, Accertamento.class, FinMapId.SiacTMovgest_Accertamento);
			
			accertamentoAggiornato.setListaModificheMovimentoGestioneEntrata(movimentiAccertamenti);
			for(SiacTMovgestTsDetFin det : siacTMovgestTsAggiornato.getSiacTMovgestTsDets()){
				if(det.getSiacDMovgestTsDetTipo().getMovgestTsDetTipoCode().equalsIgnoreCase(CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE)){
					accertamentoAggiornato.setImportoAttuale(det.getMovgestTsDetImporto());
				}
			}
			
			DisponibilitaMovimentoGestioneContainer disponibilitaSubAccertare;
			if(subAccertamentoList != null && subAccertamentoList.size() > 0){
				disponibilitaSubAccertare = calcolaDisponibilitaAccertamentoASubAccertareEValorizzaTotaleSubAccertamenti(accertamentoAggiornato);
			}else {
				disponibilitaSubAccertare = new DisponibilitaMovimentoGestioneContainer(accertamentoAggiornato.getImportoAttuale(), "In assenza di sub la disponibilita' a subaccertare e' apri all'importo attuale dell'accertamento");
			}
			accertamentoAggiornato.setDisponibilitaSubAccertare(disponibilitaSubAccertare.getDisponibilita());
			// SIAC-6695
			accertamentoAggiornato.setMotivazioneDisponibilitaSubAccertare(disponibilitaSubAccertare.getMotivazione());
			
			accertamentoAggiornato = EntityToModelConverter.siacTMovgestEntityToAccertamentoModel(siacTMovgestAggiornato, accertamentoAggiornato );
			
			////////
			List<ST> listaSubRicostruita = componiListaSubPerResponse((List<ST>) subAccertamentoList, CostantiFin.MOVGEST_TIPO_ACCERTAMENTO, richiedente, datiOperazioneDto, inseriti, aggiornati, 
					invariati, annoMovimento, numeroMovimento, siacTMovgestAggiornato);
			accertamentoAggiornato.setElencoSubAccertamenti((List<SubAccertamento>) listaSubRicostruita);
			///////////
			
			esito.setMovimentoGestione(accertamentoAggiornato);
			return esito;
			
		} else {
			return null;
		}	
	}
	
	/**
	 * Si occupa di gestire l'inserimento/modifica/eliminazione delle modifiche gestione
	 * Ritorna la lista di SubImpegni o SubAccertamenti refreshata per motivi di retrocompatibilita'
	 * con il metodo chiamante dal quale e' stato estratto questo codice e messo dentro questo nuovo metodo
	 * @param impegnoDaAggiornare
	 * @param datiOperazioneDto
	 * @return
	 */
	private EsitoGestioneModificheMovimentiDto gestioneModificheMovimenti(MovimentoGestione impegnoDaAggiornare, DatiOperazioneDto datiOperazioneDto){
		
		
		EsitoGestioneModificheMovimentiDto esitoGestioneModificheMovimentiDto = new EsitoGestioneModificheMovimentiDto();
		
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getUid();
		
		//Aggiornamento Modifiche movimento Gestione Spesa
		List<ModificaMovimentoGestioneSpesa> movimentiImpegno = null;
		List<ModificaMovimentoGestioneEntrata> movimentiAccertamenti = null;		
		
		//Gestisco numero modifiche
		Integer numeroModifiche = 0;
		Integer numeroModifichePerSub = 0;
		//Gestione modificaAbbinata (se > 1 = modifica abbinata)
		Integer numeroModificheNuove = 0;
		
		if(impegnoDaAggiornare instanceof Impegno){
		
			//calcolo numero modifiche dell'impegno
			if(((Impegno)impegnoDaAggiornare).getListaModificheMovimentoGestioneSpesa()!=null){
				numeroModifiche = ((Impegno)impegnoDaAggiornare).getListaModificheMovimentoGestioneSpesa().size();
				numeroModifichePerSub = ((Impegno)impegnoDaAggiornare).getListaModificheMovimentoGestioneSpesa().size();
			}
		
			//calcolo numero modifiche per ogni subImpegno
			if(((Impegno) impegnoDaAggiornare).getElencoSubImpegni() != null && ((Impegno) impegnoDaAggiornare).getElencoSubImpegni().size() > 0){
				List<SubImpegno> listaSubImpegni = new ArrayList<SubImpegno>();
				listaSubImpegni = ((Impegno) impegnoDaAggiornare).getElencoSubImpegni();
				for(SubImpegno sub : listaSubImpegni){						
					if(sub.getListaModificheMovimentoGestioneSpesa()!=null && sub.getListaModificheMovimentoGestioneSpesa().size()>0){
						numeroModifiche = numeroModifiche + sub.getListaModificheMovimentoGestioneSpesa().size();
						numeroModifichePerSub = numeroModifichePerSub + sub.getListaModificheMovimentoGestioneSpesa().size();
				
						//calcolo nuove modifiche subImpegno (uid 0)
						List<ModificaMovimentoGestioneSpesa> listaModificheImpegno = new ArrayList<ModificaMovimentoGestioneSpesa>();
						listaModificheImpegno = sub.getListaModificheMovimentoGestioneSpesa();
						for(ModificaMovimentoGestioneSpesa modificaSubImp : listaModificheImpegno ){
							if(modificaSubImp!=null && modificaSubImp.getUid()==0){
								//nuova modifica su Sub
								numeroModificheNuove = numeroModificheNuove + 1;																	
							}
						}						
					}
				}
			}
		
			//calcolo nuove modifiche impegno (uid 0)
			if(((Impegno)impegnoDaAggiornare).getListaModificheMovimentoGestioneSpesa()!=null){
				List<ModificaMovimentoGestioneSpesa> listaModificheImpegno = new ArrayList<ModificaMovimentoGestioneSpesa>();
				listaModificheImpegno = ((Impegno)impegnoDaAggiornare).getListaModificheMovimentoGestioneSpesa();
				for(ModificaMovimentoGestioneSpesa modificaImp : listaModificheImpegno ){
					if(modificaImp!=null && modificaImp.getUid()==0){
						//caso abbina (se esiste gia una modifica sui sub e un sull'imp => numero modifiche - 1)
						if(numeroModificheNuove > 0){
							numeroModifiche = numeroModifiche - 1;							
						}	
					}
				}
			}			
		} else if(impegnoDaAggiornare instanceof Accertamento){

			//calcolo numero modifiche dell'accertamento
			if(((Accertamento)impegnoDaAggiornare).getListaModificheMovimentoGestioneEntrata()!=null){
				numeroModifiche = ((Accertamento)impegnoDaAggiornare).getListaModificheMovimentoGestioneEntrata().size();
				numeroModifichePerSub = ((Accertamento)impegnoDaAggiornare).getListaModificheMovimentoGestioneEntrata().size();
			}	
	
			//calcolo numero modifiche per ogni subAccertamento
			if(((Accertamento) impegnoDaAggiornare).getElencoSubAccertamenti() != null && ((Accertamento) impegnoDaAggiornare).getElencoSubAccertamenti().size() > 0){
		
				List<SubAccertamento> listaSubAccertamenti = new ArrayList<SubAccertamento>();
				listaSubAccertamenti = ((Accertamento) impegnoDaAggiornare).getElencoSubAccertamenti();
				for(SubAccertamento sub : listaSubAccertamenti){						
					if(sub.getListaModificheMovimentoGestioneEntrata()!=null && sub.getListaModificheMovimentoGestioneEntrata().size()>0){
						numeroModifiche = numeroModifiche + sub.getListaModificheMovimentoGestioneEntrata().size();
						numeroModifichePerSub = numeroModifichePerSub + sub.getListaModificheMovimentoGestioneEntrata().size();
			
						//calcolo nuove modifiche subAccertamento (uid 0)
						List<ModificaMovimentoGestioneEntrata> listaModificheAccertamento = new ArrayList<ModificaMovimentoGestioneEntrata>();
						listaModificheAccertamento = sub.getListaModificheMovimentoGestioneEntrata();
						for(ModificaMovimentoGestioneEntrata modificaSubAcc : listaModificheAccertamento ){
							if(modificaSubAcc!=null && modificaSubAcc.getUid()==0){
								//nuova modifica su Sub
								numeroModificheNuove = numeroModificheNuove + 1;																	
							}
						}							
					}
				}
			}
		
			if(((Accertamento)impegnoDaAggiornare).getListaModificheMovimentoGestioneEntrata()!=null){
				//calcolo nuove modifiche accertamento (uid 0)
				List<ModificaMovimentoGestioneEntrata> listaModificheAccertamento = new ArrayList<ModificaMovimentoGestioneEntrata>();
				listaModificheAccertamento = ((Accertamento)impegnoDaAggiornare).getListaModificheMovimentoGestioneEntrata();
				for(ModificaMovimentoGestioneEntrata modificaAcc : listaModificheAccertamento){
					if(modificaAcc!=null && modificaAcc.getUid()==0){
						//caso abbina (se esiste gia una modifica sui sub e un sull'acc => numero modifiche - 1)
						if(numeroModificheNuove > 0){
							numeroModifiche = numeroModifiche - 1;							
						}	
					}
				}											
			}						
		}
		
		if(impegnoDaAggiornare instanceof Impegno){			
			movimentiImpegno = aggiornaModificheMovimentoGestioneSpesa(((Impegno)impegnoDaAggiornare).getListaModificheMovimentoGestioneSpesa(), impegnoDaAggiornare.getUid() , idEnte , datiOperazioneDto, numeroModifiche);			
		} else if(impegnoDaAggiornare instanceof Accertamento){			
			movimentiAccertamenti = aggiornaModificheMovimentoGestioneEntrata(((Accertamento)impegnoDaAggiornare).getListaModificheMovimentoGestioneEntrata(), impegnoDaAggiornare.getUid() , idEnte , datiOperazioneDto, numeroModifiche);
		}
		
		
		//Aggiornamento Eventuali Modifiche movimento Gestione Spesa SUBIMPEGNI
		List<SubImpegno> subImpegnoList = null;
		List<SubAccertamento> subAccertamentoList = null;
		if(impegnoDaAggiornare instanceof Impegno){
			subImpegnoList = aggiornaModificheMovimentoGestioneSpesaSubImpegno(((Impegno)impegnoDaAggiornare).getElencoSubImpegni(), idEnte, datiOperazioneDto, numeroModifichePerSub);
		} else if(impegnoDaAggiornare instanceof Accertamento){
			subAccertamentoList = aggiornaModificheMovimentoGestioneEntrataSubAccertamento(((Accertamento)impegnoDaAggiornare).getElencoSubAccertamenti(),idEnte, datiOperazioneDto, numeroModifichePerSub);
		}
		
		//RETURN DEI RISULTATI:
		if(impegnoDaAggiornare instanceof Impegno){
			esitoGestioneModificheMovimentiDto.setSubImpegnoList(subImpegnoList);
			esitoGestioneModificheMovimentiDto.setMovimentiImpegno(movimentiImpegno);
		} else if(impegnoDaAggiornare instanceof Accertamento){
			esitoGestioneModificheMovimentiDto.setSubAccertamentoList(subAccertamentoList);
			esitoGestioneModificheMovimentiDto.setMovimentiAccertamenti(movimentiAccertamenti);
		}
		
		return esitoGestioneModificheMovimentiDto;
	}
	
	/**
	 * Serve a capire, nel contesto del metodo "aggiornaImpegno" se e' stato richiesto
	 * di aggiornare l'impegno/accertamento in senso stretto 
	 * oppure se
	 * e' stato richiesto di inserire/aggiornare/eliminare delle modifiche
	 * @param movimentoInAggiornamento
	 * @param datiOperazioneDto
	 * @return
	 */
	public boolean presentiModifiche(MovimentoGestione movimentoInAggiornamento, DatiOperazioneDto datiOperazioneDto){
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getUid();
		boolean modifichePresenti = false;
		if(movimentoInAggiornamento instanceof Impegno){
			ModificaMovimentoGestioneSpesaInfoDto valutazioneModMov = valutaModificheMovimentoSpesa(((Impegno)movimentoInAggiornamento).getListaModificheMovimentoGestioneSpesa(),datiOperazioneDto);
			List<ModificaMovimentoGestioneSpesaInfoDto> valutazioneModMovSubs = valutaModificheMovimentoSubImp(((Impegno)movimentoInAggiornamento).getElencoSubImpegni(), idEnte, datiOperazioneDto);
			List<ModificaMovimentoGestioneSpesaInfoDto> valutazioneModMovAll = CommonUtil.toList( CommonUtil.toList(valutazioneModMov),valutazioneModMovSubs);
			modifichePresenti = presentiModificheSpesa(valutazioneModMovAll);
		} else if(movimentoInAggiornamento instanceof Accertamento){
			ModificaMovimentoGestioneEntrataInfoDto valutazioneModMov = valutaModificheMovimentoEntrata(((Accertamento)movimentoInAggiornamento).getListaModificheMovimentoGestioneEntrata(),datiOperazioneDto);
			List<ModificaMovimentoGestioneEntrataInfoDto> valutazioneModMovSubs = valutaModificheMovimentoSubAcc(((Accertamento)movimentoInAggiornamento).getElencoSubAccertamenti(), idEnte, datiOperazioneDto);
			List<ModificaMovimentoGestioneEntrataInfoDto> valutazioneModMovAll = CommonUtil.toList( CommonUtil.toList(valutazioneModMov),valutazioneModMovSubs);
			modifichePresenti = presentiModificheEntrata(valutazioneModMovAll);
		}
		return modifichePresenti;
	}
	
	/**
	 * sub routine per capire se ci sono modifiche entrata
	 * @param list
	 * @return
	 */
	public boolean presentiModificheEntrata(List<ModificaMovimentoGestioneEntrataInfoDto> list){
		boolean modifichePresenti = false;
		for(ModificaMovimentoGestioneEntrataInfoDto it: list){
			if(it.modifichePresenti()){
				modifichePresenti = true;
				break;
			}
		}
		return modifichePresenti;
	}
	
	/**
	 * sub routine per capire se ci sono modifiche spesa
	 * @param list
	 * @return
	 */
	public boolean presentiModificheSpesa(List<ModificaMovimentoGestioneSpesaInfoDto> list){
		boolean modifichePresenti = false;
		for(ModificaMovimentoGestioneSpesaInfoDto it: list){
			if(it.modifichePresenti()){
				modifichePresenti = true;
				break;
			}
		}
		return modifichePresenti;
	}
	
	
	/**
	 * scorre la lista delle modifiche sui sub per definire se ci sono modifiche da INSERIRE
	 * @param list
	 * @return
	 */
	public boolean presentiModificheSpesaSuiSubDaInserire(List<ModificaMovimentoGestioneSpesaInfoDto> list){
		boolean modifichePresenti = false;
		for(ModificaMovimentoGestioneSpesaInfoDto it: list){
			if(it.isModificheDaCrearePresenti()){
				modifichePresenti = true;
				break;
			}
		}
		return modifichePresenti;
	}
	
	/**
	 * Compone la lista di model di Sub(Impegni o Accertamenti) da restituire.
	 * @param subImpegnoList
	 * @param tipoMovimento
	 * @param richiedente
	 * @param datiOperazioneDto
	 * @param subinseriti
	 * @param aggiornati
	 * @param invariati
	 * @param annoMovimento
	 * @param numeroMovimento
	 * @param siacTMovgestAggiornato
	 * @return
	 */
	private List<ST> componiListaSubPerResponse(List<ST> subImpegnoList, String tipoMovimento,Richiedente richiedente,
			DatiOperazioneDto datiOperazioneDto,List<SiacTMovgestTsFin> subinseriti,
			List<SiacTMovgestTsFin> aggiornati,List<SiacTMovgestTsFin> invariati,Integer annoMovimento, BigDecimal numeroMovimento
			,SiacTMovgestFin siacTMovgestAggiornato){
		List<ST> listaSubInvariati = new ArrayList<ST>();
		List<ST> listaSubAggiornati = new ArrayList<ST>();
		List<ST> listaSubInseriti= new ArrayList<ST>();
		datiOperazioneDto.setCurrMillisec(getNow().getTime());
		if(subImpegnoList != null && subImpegnoList.size() > 0){
			for(ST subIt : subImpegnoList){
				int idSubIt = subIt.getUid();
				if(idSubIt==0){
					//si tratta di quelli nuovi NOTHING TO DO HERE
				} else {
					SiacTMovgestTsFin siacTMovgestTsSub = DatiOperazioneUtil.getById(invariati, idSubIt);
					if(siacTMovgestTsSub!=null){
						//INVARIATO
						listaSubInvariati.add(subIt);
					} else {
						siacTMovgestTsSub = DatiOperazioneUtil.getById(aggiornati, idSubIt);
						if(siacTMovgestTsSub!=null){
							//AGGIORNATO
							ST ricostruito = null;
							if(tipoMovimento.equalsIgnoreCase(CostantiFin.MOVGEST_TIPO_IMPEGNO)){
								ricostruito =completaSubImpegnoDaAggiorna(tipoMovimento, annoMovimento, numeroMovimento, 
										richiedente, siacTMovgestTsSub, datiOperazioneDto,siacTMovgestAggiornato);
								((SubImpegno)ricostruito).setListaModificheMovimentoGestioneSpesa(((SubImpegno)subIt).getListaModificheMovimentoGestioneSpesa());
							} else {
								ricostruito = completaSubAccertamentoDaAggiorna(tipoMovimento, annoMovimento, numeroMovimento, 
										richiedente, siacTMovgestTsSub, datiOperazioneDto, siacTMovgestAggiornato);
								((SubAccertamento)ricostruito).setListaModificheMovimentoGestioneEntrata(((SubAccertamento)subIt).getListaModificheMovimentoGestioneEntrata());
							}
							listaSubAggiornati.add(ricostruito);
						}
					}
				}
			}
		}
		
		if(subinseriti!=null && subinseriti.size()>0){
			for(SiacTMovgestTsFin siacTMovgestTsSub : subinseriti){
				ST subIt = null ;
				if(tipoMovimento.equalsIgnoreCase(CostantiFin.MOVGEST_TIPO_IMPEGNO)){
					subIt = (ST) new SubImpegno();
					subIt = completaSubImpegnoDaAggiorna(tipoMovimento, annoMovimento, numeroMovimento, 
							richiedente, siacTMovgestTsSub, datiOperazioneDto,siacTMovgestAggiornato);
				} else {
					subIt = (ST) new SubAccertamento();	
					subIt = completaSubAccertamentoDaAggiorna(tipoMovimento, annoMovimento, numeroMovimento, 
							richiedente, siacTMovgestTsSub, datiOperazioneDto,siacTMovgestAggiornato);
				}
				subIt.setUid(siacTMovgestTsSub.getUid());
				listaSubInseriti.add(subIt);
			}
		}
		List<ST> listaSubRicostruita = (List<ST>) toList(listaSubInvariati,listaSubAggiornati,listaSubInseriti);
		
		if(listaSubRicostruita!=null && listaSubRicostruita.size()>1){
			Collections.sort(listaSubRicostruita, new Comparator<ST>() {
		        @Override
		        public int compare(ST  sub1, ST  sub2){
		            return  sub1.getNumeroBigDecimal().compareTo(sub2.getNumeroBigDecimal());
		        }
		    });
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaSubRicostruita;
	}
	
	/**
	 * Dato un impegno cerca l'eventuale ribaltamento nell'anno dopo (potrebbe non esistere)
	 * @param datiOperazione
	 * @param annoMovimento
	 * @param numeroMovimento
	 * @return
	 */
	private SiacTMovgestFin individuaImpegnoResiduo(DatiOperazioneDto datiOperazione, int annoMovimento, int numeroMovimento){
		return individuaMovGestResiduo(datiOperazione, annoMovimento, numeroMovimento, CostantiFin.MOVGEST_TIPO_IMPEGNO);
	}
	
	/**
	 *  Dato un accertamento cerca l'eventuale ribaltamento nell'anno dopo (potrebbe non esistere)
	 * @param datiOperazione
	 * @param annoMovimento
	 * @param numeroMovimento
	 * @return
	 */
	private SiacTMovgestFin individuaAccertamentoResiduo(DatiOperazioneDto datiOperazione, int annoMovimento, int numeroMovimento){
		return individuaMovGestResiduo(datiOperazione, annoMovimento, numeroMovimento, CostantiFin.MOVGEST_TIPO_ACCERTAMENTO);
	}
	
	/**
	 * tipoMovGest --> CostantiFin.MOVGEST_TIPO_IMPEGNO oppure CostantiFin.MOVGEST_TIPO_ACCERTAMENTO
	 * @param datiOperazione
	 * @param annoMovimento
	 * @param numeroMovimento
	 * @param tipoMovGest
	 * @return
	 */
	private SiacTMovgestFin individuaMovGestResiduo(DatiOperazioneDto datiOperazione, int annoMovimento, int numeroMovimento,String tipoMovGest){
		int annoBilancioResiduo = annoMovimento + 1;
		BigDecimal numeroMovimentoBd = new BigDecimal(numeroMovimento);
		SiacTMovgestFin siacTMovgestResiduo = null;
		Integer idEnte = datiOperazione.getSiacTEnteProprietario().getEnteProprietarioId();
		siacTMovgestResiduo = movimentoGestioneDao.findByEnteAnnoNumeroBilancioValido(idEnte, annoMovimento , numeroMovimentoBd, tipoMovGest,String.valueOf(annoBilancioResiduo), getNow());
		//Termino restituendo l'oggetto di ritorno: 
        return siacTMovgestResiduo;
	}
	
	/**
	 * 2.5.7.1.1	Pagato sui vincoli
	 * @param richiedente
	 * @param ente
	 * @param bilancio
	 * @param impegno
	 * @param datiOperazioneDto
	 * @param capitoliInfo
	 * @return
	 */
	public ArrayList<EsitoCalcoloImportiVincoliDto> calcolaPagatoSuVincoli(Bilancio bilancio,Bilancio bilancioAnnoSuccessivo, SiacTMovgestTsFin siacTMovgestTsImpegno,SiacTMovgestTsFin siacTMovgestTsImpegnoResiduo,
			DatiOperazioneDto datiOperazioneDto){
		ArrayList<EsitoCalcoloImportiVincoliDto> listaEsito = new ArrayList<EsitoCalcoloImportiVincoliDto>();
		//definire i vincoli da inserire, agg eliminare in base
		//alla ripartizione dell'importo PAGATO
		
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getUid();
		
		SiacTMovgestFin siacTMovgestImpegno = siacTMovgestTsImpegno.getSiacTMovgest();
		
		//eventuali vincoli gia' a residuo:
		List<VincoloImpegno> vincoliResidui = getAccertamentiVincolati(siacTMovgestTsImpegnoResiduo);
		
		//1.	Si calcola il PAGATO totale dell�impegno:
		BigDecimal importoAttuale = siacTMovgestTsDetRepository.findImporto(idEnte, CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE, siacTMovgestTsImpegno.getUid());
		String statoCod = getStatoCode(siacTMovgestTsImpegno, datiOperazioneDto);
		DisponibilitaMovimentoGestioneContainer dipPagareImpegno = calcolaDisponibilitaAPagareImpegno(siacTMovgestTsImpegno, statoCod, idEnte);
		BigDecimal pagato = importoAttuale.subtract(dipPagareImpegno.getDisponibilita());
		///////////////////////////////////////////////////////////////
		
		
		//2.	Si recuperano tutti i vincoli non annullati
		List<VincoloImpegno> listVincoliImpegno = getAccertamentiVincolati(siacTMovgestTsImpegno);
		
		//3.	Si ordinano i vincoli rispetto anno e numero accertamento ascendente
		if(listVincoliImpegno!=null && listVincoliImpegno.size()>0){
			listVincoliImpegno = ordinaPerAnnoNumeroAccertamentoAscendente(listVincoliImpegno);
			
			for(VincoloImpegno vIt : listVincoliImpegno){
				
				BigDecimal importoVincoliIt = vIt.getImporto();
				BigDecimal distribuitoSulVincoloIterato = BigDecimal.ZERO;
				BigDecimal valoreResiduo = BigDecimal.ZERO;
				if(pagato.compareTo(importoVincoliIt)>=0){
					//il pagato manda a zero il valore del residuo del vincolo iterato:
					valoreResiduo = BigDecimal.ZERO;
					//a questo giro abbiamo scalato dal pagato tutto l'importo del vincolo iterato:
					distribuitoSulVincoloIterato = importoVincoliIt;
				} else {
					//il pagato e' minore del valore del vincolo iterato, il residuo verra' scalato solo del valore del pagato rimato:
					valoreResiduo = importoVincoliIt.subtract(pagato);
					distribuitoSulVincoloIterato = pagato;
				}
				pagato = pagato.subtract(distribuitoSulVincoloIterato);
				
				MovGestInfoDto movGestInfoDto = caricaInfoAccertamentoDelVincolo(datiOperazioneDto, bilancio, bilancioAnnoSuccessivo, vIt, null);
				
				//Recupero l'eventuale vincolo residuo:
				VincoloImpegno vincoloResiduo = individuaVincoloResiduo(vincoliResidui, movGestInfoDto.getSiacTMovgestResiduo());
				//setto i dati:
				EsitoCalcoloImportiVincoliDto infoIterato = settaDatiInfoCalcoloVincolo(valoreResiduo,movGestInfoDto,vincoloResiduo);
				//aggiungo in lista:
				listaEsito.add(infoIterato);
				
				
				listaEsito.add(infoIterato);
			}
			
		}
		
		
		return listaEsito;
	}
	
	
	private List<VincoloImpegno> ordinaPerAnnoNumeroAccertamentoAscendente(List<VincoloImpegno> listVincoliImpegno){
		if(listVincoliImpegno!=null && listVincoliImpegno.size()>0){
			Collections.sort(listVincoliImpegno, new Comparator<VincoloImpegno>() {
				@Override
				public int compare(VincoloImpegno v1, VincoloImpegno v2) {
					
					Accertamento o1 = v1.getAccertamento();
					Accertamento o2 = v2.getAccertamento();
					
					if(o1.getAnnoMovimento()==o2.getAnnoMovimento()){
						//anno uguale, ordiniamo per numero:
						return o1.getNumeroBigDecimal().compareTo(o2.getNumeroBigDecimal());
					} else {
						//anno diverso, ordiniamo per anno:
						if(o1.getAnnoMovimento() > o2.getAnnoMovimento()){
							return 1;
						} else {
							return -1;
						}
					}
				}			
			});
		}
		return listVincoliImpegno;
	}
	
	/**
	 * Routine che si occupa della "doppia gestione" in fase di aggiornamento di un nuovo impegno.
	 * (Si tratta del paragrafo 2.5.6)
	 * @param richiedente
	 * @param ente
	 * @param bilancio
	 * @param impegnoDaAggiornare
	 * @param datiOperazioneDto
	 * @param capitoliInfo
	 * @param modificaVincoliImpegnoInfoDto 
	 * @return
	 */
	public List<Errore> aggiornamentoInDoppiaGestioneImpegno(Richiedente richiedente, Ente ente, Bilancio bilancio, Impegno impegnoDaAggiornare,
			DatiOperazioneDto datiOperazioneDto,CapitoliInfoDto capitoliInfo, ModificaVincoliImpegnoInfoDto modificaVincoliImpegnoInfoDto,ChiamanteDoppiaGestImpegno chiamante){
		List<Errore> listaErrori=new ArrayList<Errore>();

		//metodo doppiaGestioneInAggiornaImpegno
		boolean fromDoppiaGestione = true;
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		
		Bilancio bilancioAnnoSuccessivo = buildBilancioAnnoSuccessivo(bilancio, datiOperazioneDto);
		
		DatiOperazioneDto datiOperazioneDtoMod = new DatiOperazioneDto(System.currentTimeMillis(), Operazione.MODIFICA, datiOperazioneDto.getSiacTEnteProprietario(), richiedente.getAccount().getId());
		DatiOperazioneDto datiOperazioneDtoIns = new DatiOperazioneDto(System.currentTimeMillis(), Operazione.INSERIMENTO, datiOperazioneDto.getSiacTEnteProprietario(), richiedente.getAccount().getId());
		DatiOperazioneDto datiOperazioneCancella = new DatiOperazioneDto(System.currentTimeMillis(), Operazione.CANCELLAZIONE_LOGICA_RECORD, datiOperazioneDto.getSiacTEnteProprietario(), richiedente.getAccount().getId());

		//ricerco residuo
		SiacTMovgestFin siacTMovgestResiduo = movimentoGestioneDao.findByEnteAnnoNumeroBilancioValido(idEnte, impegnoDaAggiornare.getAnnoMovimento() , impegnoDaAggiornare.getNumeroBigDecimal(), CostantiFin.MOVGEST_TIPO_IMPEGNO, String.valueOf(impegnoDaAggiornare.getAnnoMovimento()+1), datiOperazioneDtoMod.getTs());
		SiacTMovgestTsFin siacTMovgestTsResiduo = null;
		Integer movgestIdResiduo=null;
		Integer movgestTsIdResiduo = null;
		if(siacTMovgestResiduo!=null){
			movgestIdResiduo=siacTMovgestResiduo.getMovgestId();
			siacTMovgestTsResiduo = siacTMovgestTsRepository.findMovgestTsByMovgest(idEnte, datiOperazioneDtoMod.getTs(), movgestIdResiduo).get(0);	
			movgestTsIdResiduo = siacTMovgestTsResiduo.getUid();
		}
		
		//ricerco i sub del residuo
		//ricavo movgest e movgestts dell'impegno aggiornato
		SiacTMovgestFin siacTMovgestAggiornato = movimentoGestioneDao.findByEnteAnnoNumeroBilancioValido(idEnte, impegnoDaAggiornare.getAnnoMovimento() , impegnoDaAggiornare.getNumeroBigDecimal(), CostantiFin.MOVGEST_TIPO_IMPEGNO, String.valueOf(impegnoDaAggiornare.getAnnoMovimento()), datiOperazioneDtoMod.getTs());
		
		//List<SiacTMovgestFin> siacTMovgestAggiornatoSOld = siacTMovgestRepository.findByEnteAnnoNumeroBilancioValido(idEnte, impegnoDaAggiornare.getAnnoMovimento() , impegnoDaAggiornare.getNumero(), CostantiFin.MOVGEST_TIPO_IMPEGNO, String.valueOf(impegnoDaAggiornare.getAnnoMovimento()), datiOperazioneDtoMod.getTs());
		
		SiacTMovgestTsFin siacTMovgestTsDaAggiornare = siacTMovgestTsRepository.findMovgestTsByMovgest(idEnte, datiOperazioneDtoMod.getTs(), siacTMovgestAggiornato.getMovgestId()).get(0);			
		//ricerco i sub dell'impegno aggiornato
		List<SiacTMovgestTsFin> siacTMovgestTsDaAggiornareSub=siacTMovgestTsRepository.findListaSiacTMovgestTsFigli(idEnte, getNow(), siacTMovgestTsDaAggiornare.getMovgestTsId());

		SiacDAmbitoFin  siacDAmbitoPerCode = siacDAmbitoRepository.findAmbitoByCode(CostantiFin.AMBITO_FIN, idEnte);

		//Calcolo disponibile di cassa impegno
		DisponibilitaMovimentoGestioneContainer dispCassaImpegno;
		String statoCod = null;
		
		if(siacTMovgestResiduo!=null){
			SiacDMovgestStatoFin statoSub = getStato(siacTMovgestTsDaAggiornare, datiOperazioneDtoMod);
			statoCod = statoSub.getMovgestStatoCode();

			dispCassaImpegno = calcolaDisponibilitaAPagareImpegno(siacTMovgestTsDaAggiornare, statoCod, idEnte);
		}else {
			dispCassaImpegno= new DisponibilitaMovimentoGestioneContainer(impegnoDaAggiornare.getImportoAttuale(), "Se il movimento non e' residuo, la disponibilita' a pagare deve essere pari all'importo attuale");
		}
		
		
		//Invochiamo il metodo calcolaImportiVincoli che corrisponde al passo "1.	Calcolo importi" di "2.5.7.1	Impatto dei Vincoli sulla doppia gestione"
		List<EsitoCalcoloImportiVincoliDto> calcoloImpVincoli = calcoloImportiVincoli(datiOperazioneDto,modificaVincoliImpegnoInfoDto,siacTMovgestTsDaAggiornare,siacTMovgestTsResiduo,bilancio,bilancioAnnoSuccessivo,chiamante);
		
		//"2.	Aggiornamento Accertamenti" di "2.5.7.1	Impatto dei Vincoli sulla doppia gestione"
		if(calcoloImpVincoli!=null && calcoloImpVincoli.size()>0){
			for(EsitoCalcoloImportiVincoliDto itVinc: calcoloImpVincoli){
				SiacTMovgestTsFin siacTMovgestTsAccResiduo =itVinc.getSiacTMovgestTsAccResiduo();
				SiacTMovgestFin siacTMovgestAccResiduo = null;
				Accertamento accCollegato = null;
				if(siacTMovgestTsAccResiduo!=null){
					//esiste l'accertamento residuo
					siacTMovgestAccResiduo = siacTMovgestTsAccResiduo.getSiacTMovgest();
				} else {
					//non esiste l'accertamento residuo
					accCollegato = itVinc.getAccertamentoFresco();
				}
				BigDecimal deltaVincolo = itVinc.getDeltaVincolo();
				Integer idMovGestAccResiduo = aggiornaImportoAccertamentoResiduoPerVincoloModificato(siacTMovgestAccResiduo, datiOperazioneDtoIns, richiedente, accCollegato, deltaVincolo, ente, bilancioAnnoSuccessivo);
				//Per ogni vincolo bisogna puntare all'accertamento residuo in modo che l'op interna di ins impegno
				//che verra' invocata piu' avanti crei il corretto vincolo residuo:
				for(VincoloImpegno vincoloImpegno : impegnoDaAggiornare.getVincoliImpegno()){
					if(siacTMovgestAccResiduo.getMovgestNumero().compareTo(vincoloImpegno.getAccertamento().getNumeroBigDecimal())==0){
						itVinc.setVincoloResiduo(vincoloImpegno);
					}
				}
			}
		}
		
		
		if(NumericUtils.maggioreDiZero(dispCassaImpegno.getDisponibilita())){

			if(siacTMovgestResiduo!=null){
				//Aggiorno l'impegno residuo
				//ricerco i sub dell'impegno aggiornato
				List<SiacTMovgestTsFin> siacTMovgestTsResiduoSub=siacTMovgestTsRepository.findListaSiacTMovgestTsFigli(idEnte, datiOperazioneDtoMod.getTs(), movgestTsIdResiduo);

				impegnoDaAggiornare.setImportoAttuale(dispCassaImpegno.getDisponibilita());

				impegnoDaAggiornare.setUid(movgestIdResiduo);
				
				//EVENTUALI SUB:
				EsitoAggiornamentoSubMovGestTs infoSubValutati=catalogaSubDoppiaGestione(siacTMovgestTsDaAggiornareSub, siacTMovgestTsResiduoSub);
				List<SiacTMovgestTsFin> subInseriti = infoSubValutati.getInseriti();
				List<SiacTMovgestTsFin> subAggiornati = infoSubValutati.getAggiornati();
				List<SiacTMovgestTsFin> subEliminati = infoSubValutati.getEliminati();

				SubMovgestInModificaInfoDto subMovgestInModificaInfoDto=new SubMovgestInModificaInfoDto();
				ArrayList<SubImpegno> subImpegniDaInserire=new ArrayList<SubImpegno>();
				ArrayList<SubImpegno> subImpegniDaModificare=new ArrayList<SubImpegno>();
				ArrayList<SiacTMovgestTsFin> subImpegniDaEliminare=new ArrayList<SiacTMovgestTsFin>();
				List<SubImpegno> listaSubImpegniDaAggRes=new ArrayList<SubImpegno>();

				if(subInseriti!=null && subInseriti.size()>0){
					//ciclare su subInseriti DEFINITVI e ribaltarli creando dei nuovi sub, ogni ribaltamento avra' importo = importo del nuovo sub
					//per ogni sub della lista  da inserire
					for(SiacTMovgestTsFin siacTMovgestTsInserito : subInseriti){
						String code = siacTMovgestTsInserito.getMovgestTsCode();
						BigDecimal codeBD = new BigDecimal(code);
						List<SubImpegno> listaSubDaAgg=impegnoDaAggiornare.getElencoSubImpegni();
						boolean esisteInListaSubDaAgg=false;
						if (listaSubDaAgg!=null && listaSubDaAgg.size()>0) {
							for (SubImpegno subImpegnoDaAgg : listaSubDaAgg) {
								if (subImpegnoDaAgg.getNumeroBigDecimal()!=null) {
									if (subImpegnoDaAgg.getNumeroBigDecimal().compareTo(codeBD)==0) {
										esisteInListaSubDaAgg=true;
										break;
									}
								}else {
									if (subImpegnoDaAgg.getStatoOperativoMovimentoGestioneSpesa().equals(CostantiFin.MOVGEST_STATO_DEFINITIVO) &&
											subImpegnoDaAgg.getImportoAttuale().compareTo(BigDecimal.ZERO)==1) {
										subImpegnoDaAgg.setNumeroBigDecimal(codeBD);
										subImpegniDaInserire.add(subImpegnoDaAgg);
										listaSubImpegniDaAggRes.add(subImpegnoDaAgg);
									}
								}
							}	
						}
						
						if (esisteInListaSubDaAgg) {
							//se il sub esisteva gia' lo metto tra quelli da aggiornare
							subAggiornati.add(siacTMovgestTsInserito);
						}
					}
				}
				
				if(subAggiornati!=null && subAggiornati.size()>0){
					for(SiacTMovgestTsFin siacTMovgestTsAggiornato : subAggiornati){
						//per ogni sub nella lista cerco il corrispondente sul residuo
						List<SiacTMovgestTsFin> listaSiacTMovgestTsResiduo =siacTMovgestTsRepository.findSubMovgestTsByCodeAndMovgestId(idEnte, datiOperazioneDtoMod.getTs(), movgestIdResiduo, siacTMovgestTsAggiornato.getMovgestTsCode());

						if(listaSiacTMovgestTsResiduo!=null && listaSiacTMovgestTsResiduo.size()>0){
							//se esiste il sub ribaltato viene aggiornato l'importo 

							SiacTMovgestTsFin siacTMovgestTsSubAggResiduo=listaSiacTMovgestTsResiduo.get(0);

							List<SubImpegno> listaSubDaAgg=impegnoDaAggiornare.getElencoSubImpegni();
							if (listaSubDaAgg!=null && listaSubDaAgg.size()>0) {
								for (SubImpegno subImpegnoDaAgg : listaSubDaAgg) {
									if (subImpegnoDaAgg.getUid()!=0) {
										SiacTMovgestTsFin siacTMovgestTsCorr=siacTMovgestTsRepository.findMovgestTsByMovgestTsId(idEnte, getNow(), subImpegnoDaAgg.getUid());
										subImpegnoDaAgg.setNumeroBigDecimal(new BigDecimal(siacTMovgestTsCorr.getMovgestTsCode()));
										if (subImpegnoDaAgg.getNumeroBigDecimal()!=null && 
												subImpegnoDaAgg.getNumeroBigDecimal().compareTo(new BigDecimal(siacTMovgestTsSubAggResiduo.getMovgestTsCode()))==0) {
											DisponibilitaMovimentoGestioneContainer dispCassaSub = calcolaDisponibilitaAPagareSubImpegno(siacTMovgestTsAggiornato, subImpegnoDaAgg.getStatoOperativoMovimentoGestioneSpesa(), idEnte);
											if(NumericUtils.maggioreDiZero(dispCassaSub.getDisponibilita())){
												//Aggiorno sub residuo se esiste e 
												subImpegnoDaAgg.setImportoAttuale(dispCassaSub.getDisponibilita());
												subImpegnoDaAgg.setImportoIniziale(dispCassaSub.getDisponibilita());
												subImpegnoDaAgg.setUid(siacTMovgestTsSubAggResiduo.getUid());
												subImpegnoDaAgg.setIdMovimentoPadre(movgestIdResiduo);
												
												//Modifiche soggetto
												if (subImpegnoDaAgg.getListaModificheMovimentoGestioneSpesa()!=null && 
														subImpegnoDaAgg.getListaModificheMovimentoGestioneSpesa().size()>0) {
													for (ModificaMovimentoGestioneSpesa modifica : subImpegnoDaAgg.getListaModificheMovimentoGestioneSpesa()) {
														if (modifica.getSoggettoNewMovimentoGestione()!=null) {
															subImpegnoDaAgg.setSoggetto(modifica.getSoggettoNewMovimentoGestione());
															subImpegnoDaAgg.setSoggettoCode(modifica.getSoggettoNewMovimentoGestione().getCodiceSoggetto());
														}
													}
												}
												
												subImpegnoDaAgg.setListaModificheMovimentoGestioneSpesa(null);
												
												//Aggiornare sub
												subImpegniDaModificare.add(subImpegnoDaAgg);
												listaSubImpegniDaAggRes.add(subImpegnoDaAgg);
											}else if(dispCassaSub!=null && dispCassaSub.getDisponibilita() != null && dispCassaSub.getDisponibilita().compareTo(BigDecimal.ZERO)==0){
												//se importo residuo=0 elimino il sub
												List<SiacTMovgestTsFin> listaSiacTMovgestTsEliminatoResiduo =siacTMovgestTsRepository.findSubMovgestTsByCodeAndMovgestId(idEnte, datiOperazioneDtoMod.getTs(), movgestIdResiduo, siacTMovgestTsAggiornato.getMovgestTsCode());

												if (listaSiacTMovgestTsEliminatoResiduo!=null && listaSiacTMovgestTsEliminatoResiduo.size()>0) {
													SiacTMovgestTsFin siacTMovgestTsEliminatoResiduo=listaSiacTMovgestTsEliminatoResiduo.get(0);
													if (siacTMovgestTsEliminatoResiduo!=null) {
														// caso movimenti collegati
														List<SiacTLiquidazioneFin> elencoSiacTLiquidazione=findLiquidazioniValideFromSubImpegno(siacTMovgestTsEliminatoResiduo);
														if (elencoSiacTLiquidazione!=null && elencoSiacTLiquidazione.size()>0) {
															listaErrori.add(ErroreFin.ESISTONO_MOVIMENTI_COLLEGATI.getErrore("Cancellazione subimpegno residuo",""));
															return listaErrori;
														}
														//Eliminare sub
														datiOperazioneCancella = new DatiOperazioneDto(System.currentTimeMillis(), Operazione.CANCELLAZIONE_LOGICA_RECORD, datiOperazioneDto.getSiacTEnteProprietario(), richiedente.getAccount().getId());
														SiacTMovgestTsFin siacTMovgestTsResiduoCanc = DatiOperazioneUtil.cancellaRecord(siacTMovgestTsEliminatoResiduo, siacTMovgestTsRepository, datiOperazioneCancella, siacTAccountRepository);
													}
												}

											}
										}
									}
								}
							}
						} else {
							//se non esiste il sub ribaltato viene creato con importo = dispCassaSub
							//(es. se sub passa da provv a definitivo)

							List<SubImpegno> listaSubDaAgg=impegnoDaAggiornare.getElencoSubImpegni();
							if (listaSubDaAgg!=null && listaSubDaAgg.size()>0) {
								for (SubImpegno subImpegnoDaAgg : listaSubDaAgg) {

									if (subImpegnoDaAgg.getUid()!=0) {
										SiacTMovgestTsFin  subImpegnoDaAggRep=siacTMovgestTsRepository.findOne(subImpegnoDaAgg.getUid());
										if(subImpegnoDaAggRep!=null){
											String numeroSubDaAggRep=subImpegnoDaAggRep.getMovgestTsCode();
											subImpegnoDaAgg.setNumeroBigDecimal(new BigDecimal(numeroSubDaAggRep));

											if (subImpegnoDaAgg.getNumeroBigDecimal()!=null && 
													subImpegnoDaAgg.getNumeroBigDecimal().compareTo(new BigDecimal(siacTMovgestTsAggiornato.getMovgestTsCode()))==0) {
												if (subImpegnoDaAgg.getImportoAttuale().compareTo(BigDecimal.ZERO)==1 &&
														subImpegnoDaAgg.getStatoOperativoMovimentoGestioneSpesa().equals(CostantiFin.MOVGEST_STATO_DEFINITIVO)) {
													//Inserisco sub residuo
													subImpegnoDaAgg.setImportoAttuale(subImpegnoDaAgg.getImportoAttuale());
													subImpegnoDaAgg.setImportoIniziale(subImpegnoDaAgg.getImportoAttuale());
													subImpegniDaInserire.add(subImpegnoDaAgg);
													listaSubImpegniDaAggRes.add(subImpegnoDaAgg);

												}
											}
										}
									}
								}
							}
						}
					}
				}
				if(subEliminati!=null && subEliminati.size()>0){
					for(SiacTMovgestTsFin siacTMovgestTsEliminato : subEliminati){

						List<SiacTMovgestTsFin> listaSiacTMovgestTsEliminatoResiduo =siacTMovgestTsRepository.findSubMovgestTsByCodeAndMovgestId(idEnte, datiOperazioneDtoMod.getTs(), movgestIdResiduo, siacTMovgestTsEliminato.getMovgestTsCode());

						if (listaSiacTMovgestTsEliminatoResiduo!=null && listaSiacTMovgestTsEliminatoResiduo.size()>0) {
							SiacTMovgestTsFin siacTMovgestTsEliminatoResiduo=listaSiacTMovgestTsEliminatoResiduo.get(0);
							if (siacTMovgestTsEliminatoResiduo!=null) {
								//controllo movimenti collegati
								List<SiacTLiquidazioneFin> elencoSiacTLiquidazione=findLiquidazioniValideFromSubImpegno(siacTMovgestTsEliminatoResiduo);
								if (elencoSiacTLiquidazione!=null && elencoSiacTLiquidazione.size()>0) {
									listaErrori.add(ErroreFin.ESISTONO_MOVIMENTI_COLLEGATI.getErrore("Cancellazione subimpegno residuo",""));
									return listaErrori;
								}
								datiOperazioneCancella = new DatiOperazioneDto(System.currentTimeMillis(), Operazione.CANCELLAZIONE_LOGICA_RECORD, datiOperazioneDto.getSiacTEnteProprietario(), richiedente.getAccount().getId());
								SiacTMovgestTsFin siacTMovgestTsResiduoCanc = DatiOperazioneUtil.cancellaRecord(siacTMovgestTsEliminatoResiduo, siacTMovgestTsRepository, datiOperazioneCancella, siacTAccountRepository);

							}
						}
					}
				}



				subMovgestInModificaInfoDto.setSubImpegniDaInserire(subImpegniDaInserire);
				subMovgestInModificaInfoDto.setSubImpegniDaModificare(subImpegniDaModificare);
				subMovgestInModificaInfoDto.setSubImpegniDaEliminare(subImpegniDaEliminare);
				subMovgestInModificaInfoDto.setSubImpegniOld(siacTMovgestTsResiduoSub);

				impegnoDaAggiornare.setUid(siacTMovgestResiduo.getUid());
				impegnoDaAggiornare.setElencoSubImpegni(listaSubImpegniDaAggRes);

				datiOperazioneDtoMod = new DatiOperazioneDto(System.currentTimeMillis(), Operazione.MODIFICA, datiOperazioneDto.getSiacTEnteProprietario(), siacDAmbitoPerCode, richiedente.getAccount().getId());

				EsitoAggiornamentoMovimentoGestioneDto esitoOperazioneInternaAggiornaImpegno = operazioneInternaAggiornaImpegno(richiedente, ente, bilancioAnnoSuccessivo, impegnoDaAggiornare, 
						impegnoDaAggiornare.getSoggetto(), null, datiOperazioneDtoMod,
						subMovgestInModificaInfoDto,fromDoppiaGestione,capitoliInfo,null);

				if(esitoOperazioneInternaAggiornaImpegno.getListaErrori()!=null && esitoOperazioneInternaAggiornaImpegno.getListaErrori().size()>0){
					return esitoOperazioneInternaAggiornaImpegno.getListaErrori();
				}


			} else {
				//Inserisco l'impegno residuo

				impegnoDaAggiornare.setUid(0);
				

				EsitoAggiornamentoSubMovGestTs infoSubValutati=catalogaSubDoppiaGestione(siacTMovgestTsDaAggiornareSub, null);
				List<SiacTMovgestTsFin> subInseriti = infoSubValutati.getInseriti();
				ArrayList<SubImpegno> subInseritiResidui = new ArrayList<SubImpegno>();
				List<SubImpegno> listaSubImpegnoDaInserire=new ArrayList<SubImpegno>();

				if(subInseriti!=null && subInseriti.size()>0){
					for(SiacTMovgestTsFin siacTMovgestTsAggiornato : subInseriti){
						List<SubImpegno> listaSubDaAgg=impegnoDaAggiornare.getElencoSubImpegni();
						if (listaSubDaAgg!=null && listaSubDaAgg.size()>0) {
							for (SubImpegno subImpegnoDaAgg : listaSubDaAgg) {

								if (subImpegnoDaAgg.getUid()!=0) {
									SiacTMovgestTsFin  subImpegnoDaAggRep=siacTMovgestTsRepository.findOne(subImpegnoDaAgg.getUid());
									if(subImpegnoDaAggRep!=null){
										String numeroSubDaAggRep=subImpegnoDaAggRep.getMovgestTsCode();
										subImpegnoDaAgg.setNumeroBigDecimal(new BigDecimal(numeroSubDaAggRep));

										if (subImpegnoDaAgg.getNumeroBigDecimal()!=null && 
												subImpegnoDaAgg.getNumeroBigDecimal().compareTo(new BigDecimal(siacTMovgestTsAggiornato.getMovgestTsCode()))==0) {
											if (subImpegnoDaAgg.getImportoAttuale().compareTo(BigDecimal.ZERO)==1 &&
													subImpegnoDaAgg.getStatoOperativoMovimentoGestioneSpesa().equals(CostantiFin.MOVGEST_STATO_DEFINITIVO)) {
												//Inserisco sub residuo
												subImpegnoDaAgg.setImportoAttuale(subImpegnoDaAgg.getImportoAttuale());
												subImpegnoDaAgg.setImportoIniziale(subImpegnoDaAgg.getImportoAttuale());
												//Inserisco subImpegno
												listaSubImpegnoDaInserire.add(subImpegnoDaAgg);
												subInseritiResidui.add(subImpegnoDaAgg);
											}
										}
									}
								}
							}
						}
					}
					//Se ci sono subimpegni devo aggiornare l'impegno appena inserito
					if(listaSubImpegnoDaInserire!=null && listaSubImpegnoDaInserire.size()>0){
						impegnoDaAggiornare.setElencoSubImpegni(listaSubImpegnoDaInserire);

					}
				}
				//Inserisco l'impegno con l'operazione interna

				Integer numeroImpegno=new Integer(impegnoDaAggiornare.getNumeroBigDecimal().intValue());
				impegnoDaAggiornare.setListaModificheMovimentoGestioneSpesa(new ArrayList<ModificaMovimentoGestioneSpesa>());
				datiOperazioneDtoIns = new DatiOperazioneDto(System.currentTimeMillis(), Operazione.INSERIMENTO, datiOperazioneDto.getSiacTEnteProprietario(), siacDAmbitoPerCode, richiedente.getAccount().getId());
				
				EsitoInserimentoMovimentoGestioneDto esitoOperazioneInternaCiclo = operazioneInternaInserisceImpegno(richiedente, ente, bilancioAnnoSuccessivo, impegnoDaAggiornare, datiOperazioneDtoIns, numeroImpegno,null);
				//EsitoInserimentoMovimentoGestioneDto esitoOperazioneInternaCiclo = operazioneInternaInserisceMovimento(richiedente, ente, bilancioAnnoSuccessivo, impegnoDaAggiornare, datiOperazioneDtoIns, numeroImpegno);
				
				if(esitoOperazioneInternaCiclo.getListaErrori()!=null && esitoOperazioneInternaCiclo.getListaErrori().size()>0){
					return esitoOperazioneInternaCiclo.getListaErrori();
				}
				
			}


		} else {
			//LA NUOVA DISPONIBILITA' E' ZERO

			//esiste residuo?
			//allora viene cancellato l'impegno e i sub impegni residui se non ci sono liq residue agganciate
			if(siacTMovgestResiduo!=null){
				//controllo movimenti collegati
				List<SiacTLiquidazioneFin> elencoSiacTLiquidazione=findLiquidazioniValideFromSubImpegno(siacTMovgestTsResiduo);
				if (elencoSiacTLiquidazione!=null && elencoSiacTLiquidazione.size()>0) {
					listaErrori.add(ErroreFin.ESISTONO_MOVIMENTI_COLLEGATI.getErrore("Cancellazione impegno residuo",""));
					return listaErrori;
				}

				List<SiacTMovgestTsFin> listaSiacTMovgestTsSubResiduo =siacTMovgestTsRepository.findSubMovgestTsByCodeAndMovgestId(idEnte, datiOperazioneDtoMod.getTs(), movgestIdResiduo, siacTMovgestTsResiduo.getMovgestTsCode());

				//se esistono subimpegno li cancello
				if (listaSiacTMovgestTsSubResiduo!=null && listaSiacTMovgestTsSubResiduo.size()>0) {
					for (SiacTMovgestTsFin siacTMovgestTsSubResiduo : listaSiacTMovgestTsSubResiduo) {
						if (siacTMovgestTsSubResiduo!=null) {
							//controllo movimenti collegati
							List<SiacTLiquidazioneFin> elencoSiacTLiquidazioneSub=findLiquidazioniValideFromSubImpegno(siacTMovgestTsSubResiduo);
							if (elencoSiacTLiquidazioneSub!=null && elencoSiacTLiquidazioneSub.size()>0) {
								listaErrori.add(ErroreFin.ESISTONO_MOVIMENTI_COLLEGATI.getErrore("Cancellazione subimpegno residuo",""));
								return listaErrori;
							}
							SiacTMovgestTsFin siacTMovgestTsResiduoCanc = DatiOperazioneUtil.cancellaRecord(siacTMovgestTsSubResiduo, siacTMovgestTsRepository, datiOperazioneCancella, siacTAccountRepository);

						}
					}
				}

				SiacTMovgestTsFin siacTMovgestTsResiduoCanc = DatiOperazioneUtil.cancellaRecord(siacTMovgestTsResiduo, siacTMovgestTsRepository, datiOperazioneCancella, siacTAccountRepository);
				SiacTMovgestFin siacTMovgestResiduoCanc = DatiOperazioneUtil.cancellaRecord(siacTMovgestResiduo, siacTMovgestRepository, datiOperazioneCancella, siacTAccountRepository);

			}

		}
		
		//Termino restituendo l'oggetto di ritorno: 
        return listaErrori;
	}
	
	/**
	 * Esegue i punti 3 e 4 di "2.	Aggiornamento Accertamenti" di "2.5.7.1	Impatto dei Vincoli sulla doppia gestione"
	 * @param siacTMovgestAccResiduo
	 * @param datiOperazioneDto
	 * @param richiedente
	 * @param accCollegato
	 * @param deltaVincolo
	 * @param ente
	 * @param bilancioAnnoSuccessivo
	 * @return
	 */
	private Integer aggiornaImportoAccertamentoResiduoPerVincoloModificato(SiacTMovgestFin siacTMovgestAccResiduo, DatiOperazioneDto datiOperazioneDto, 
			Richiedente richiedente, Accertamento accCollegato, BigDecimal deltaVincolo, Ente ente, Bilancio bilancioAnnoSuccessivo){
		//3.	Per ogni accertamento trovato richiamare l�aggiorna accertamento con:
		//	Accertamento.importoUtilizzabileRes= Accertamento.importoUtilizzabile RES + deltaVincolo 
		//4.	Se non viene trovato l�accertamento allora � necessario inserire l�accertamento residuo (come descritto nel metodo Inserisce Accertamenti del servizio FINENTS002  � Gestione Accertamento) trasmettendo tutti i dati dell�accertamento di competenza ad eccezione di: 
		//	Bilancio.anno = anno + 1
		//	importoUtilizzabileRes= deltaVincolo
		//	importoAttualeRes= importoInizialeRes = 0
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getUid();
		Integer idMovGestAccResiduo = null;
		//ora dobbiamo verificare se esiste o meno l'accertamento "ribaltato" dell'accertamento:
		if(siacTMovgestAccResiduo!=null){
			 //Esiste l'accertamento residuo  
			 idMovGestAccResiduo = siacTMovgestAccResiduo.getUid();
			 SiacTMovgestTsFin siacTMovgestTsAccResiduo = siacTMovgestTsRepository.findMovgestTsByMovgest(idEnte, datiOperazioneDto.getTs(), siacTMovgestAccResiduo.getMovgestId()).get(0);
			 BigDecimal impUtilizzabileResiduo = estraiImportoUtilizzabileByMovgestTsId(siacTMovgestTsAccResiduo.getMovgestTsId(), datiOperazioneDto);
			 //Accertamento.importoUtilizzabileRes= Accertamento.importoUtilizzabile RES + deltaVincolo 
			 BigDecimal nuovoImpUtilizzabileRes = impUtilizzabileResiduo.add(deltaVincolo);
			 SiacTMovgestTsDetFin siacDetUtilizzabile = saveImporto(nuovoImpUtilizzabileRes, datiOperazioneDto, siacTMovgestTsAccResiduo, CostantiFin.MOVGEST_TS_DET_TIPO_UTILIZZABILE);
		 } else {
			 //Non esiste l'accertamento residuo
			 Accertamento accClonePerDoppiaGest = clone((Accertamento)accCollegato);
			 Integer numeroAccertamento=new Integer(accClonePerDoppiaGest.getNumeroBigDecimal().intValue());
			 accClonePerDoppiaGest.setImportoUtilizzabile(deltaVincolo);
			 accClonePerDoppiaGest.setImportoIniziale(BigDecimal.ZERO);
			 accClonePerDoppiaGest.setImportoAttuale(BigDecimal.ZERO);
			 EsitoInserimentoMovimentoGestioneDto esitoOperazioneInternaInsAccResiduo
			 	= operazioneInternaInserisceAccertamento(richiedente, ente, bilancioAnnoSuccessivo, accClonePerDoppiaGest, datiOperazioneDto, numeroAccertamento);
			 idMovGestAccResiduo = esitoOperazioneInternaInsAccResiduo.getMovimentoGestione().getUid();
		 }
		return idMovGestAccResiduo;
	}
	
	/**
	 * Metodo relativo alla doppia gestione di un impegno in aggiornamento
	 * @param datiOperazioneDto
	 * @param modificaVincoliImpegnoInfoDto
	 * @param siacTMovgestTsImpegnoDaAggiornare
	 * @param bilancio
	 * @param bilancioAnnoSuccessivo
	 * @param chiamante 
	 * @return
	 */
	private List<EsitoCalcoloImportiVincoliDto> calcoloImportiVincoli(DatiOperazioneDto datiOperazioneDto, ModificaVincoliImpegnoInfoDto modificaVincoliImpegnoInfoDto,
			SiacTMovgestTsFin siacTMovgestTsImpegnoDaAggiornare,SiacTMovgestTsFin siacTMovgestTsImpegnoResiduo, Bilancio bilancio,Bilancio bilancioAnnoSuccessivo, ChiamanteDoppiaGestImpegno chiamante) {
		
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		String bilancioString = Integer.toString(bilancio.getAnno());
		String bilancioPiuUnoString = Integer.toString(bilancioAnnoSuccessivo.getAnno());
		
		ArrayList<EsitoCalcoloImportiVincoliDto> listaEsito = new ArrayList<EsitoCalcoloImportiVincoliDto>();
		
		if(modificaVincoliImpegnoInfoDto!=null && chiamante.equals(ChiamanteDoppiaGestImpegno.AGGIORNA_IMPEGNO)){
			
			List<VincoloImpegno> vincoliResidui = getAccertamentiVincolati(siacTMovgestTsImpegnoResiduo);
			List<SiacRMovgestTsFin> vincoliDaAnnullare = modificaVincoliImpegnoInfoDto.getVincoliDaAnnullare();
			List<VincoloImpegno> vincoliDaInserire = modificaVincoliImpegnoInfoDto.getVincoliDaInserire();
			List<VincoloImpegno> vincoliDaAggiornare = modificaVincoliImpegnoInfoDto.getVincoliDaAggiornare();
			List<SiacRMovgestTsFin> vincoliOld=modificaVincoliImpegnoInfoDto.getVincoliOld();
			
			//Se si � effettuato l�aggiornamento di vincoli di un impegno anche provvisorio bisogna:
			//1.	Calcolare il deltaVincolo = Vincolo.importoNuovo  � Vincolo.importoVecchio considerando:
			//Vincolo.importoNuovo = 0 per gli annullamenti
			//Vincolo.importoVecchio = 0 per gli inserimenti
			//2.	Ricalcolare Vincolo.importo Res = Vincolo.importo + deltaVincolo
			if(vincoliDaAnnullare!=null && vincoliDaAnnullare.size()>0 && vincoliOld!=null){
				//VINCOLI ANNULLATI
				for (SiacRMovgestTsFin siacRMovgestTsDaAnnullare : vincoliDaAnnullare) {
					//calcolo delta importo:
					SiacRMovgestTsFin vincoloDb = DatiOperazioneUtil.getById(vincoliOld,siacRMovgestTsDaAnnullare.getUid());
					BigDecimal oldImportoVincolo = vincoloDb.getMovgestTsImporto();
					BigDecimal deltaVincolo = BigDecimal.ZERO.subtract(oldImportoVincolo);
					/////////////////////
					
					if(siacRMovgestTsDaAnnullare.getSiacTMovgestTsA()!=null){
						
						//Recupero i dati dell'accertamento:
						Integer idMovGestAccertamento = siacRMovgestTsDaAnnullare.getSiacTMovgestTsA().getSiacTMovgest().getMovgestId();
						MovGestInfoDto movGestInfoDto = modificaVincoliImpegnoInfoDto.findInfoAccertamento(idMovGestAccertamento);
						//commentato perche' deve gia arrivare da sopra:
						//MovGestInfoDto movGestInfoDto = caricaInfoAccertamentoDelVincolo(datiOperazioneDto, bilancio, bilancioAnnoSuccessivo,null,siacRMovgestTsDaAnnullare);
						
						//Recupero l'eventuale vincolo residuo:
						VincoloImpegno vincoloResiduo = individuaVincoloResiduo(vincoliResidui, movGestInfoDto.getSiacTMovgestResiduo());
						//setto i dati:
						EsitoCalcoloImportiVincoliDto infoIterato = settaDatiInfoCalcoloVincolo(deltaVincolo,movGestInfoDto,vincoloResiduo);
						//aggiungo in lista:
						listaEsito.add(infoIterato);
						
					}
					
				}
			}
			if(vincoliDaInserire!=null && vincoliDaInserire.size()>0){
				//VINCOLI INSERITI
				for(VincoloImpegno vincoloImpegno: vincoliDaInserire){
					//calcolo delta importo:
					BigDecimal deltaVincolo = vincoloImpegno.getImporto();
					
					//Recupero i dati dell'accertamento:
					Integer idMovGestAccertamento = vincoloImpegno.getAccertamento().getUid();
					MovGestInfoDto movGestInfoDto = modificaVincoliImpegnoInfoDto.findInfoAccertamento(idMovGestAccertamento);
					//commentato perche' deve gia arrivare da sopra:
					//MovGestInfoDto movGestInfoDto = caricaInfoAccertamentoDelVincolo(datiOperazioneDto, bilancio, bilancioAnnoSuccessivo,vincoloImpegno,null);
					
					//Recupero l'eventuale vincolo residuo:
					VincoloImpegno vincoloResiduo = individuaVincoloResiduo(vincoliResidui, movGestInfoDto.getSiacTMovgestResiduo());
					//setto i dati:
					EsitoCalcoloImportiVincoliDto infoIterato = settaDatiInfoCalcoloVincolo(deltaVincolo,movGestInfoDto,vincoloResiduo);
					//aggiungo in lista:
					listaEsito.add(infoIterato);
				}
			}
			if(vincoliDaAggiornare!=null && vincoliDaAggiornare.size()>0){
				//VINCOLI MODIFICATI
				for(VincoloImpegno vincoloImpegno: vincoliDaAggiornare){
					//calcolo delta importo:
					SiacRMovgestTsFin vincoloDb = DatiOperazioneUtil.getById(vincoliOld,vincoloImpegno.getUid());
					BigDecimal importoNuovo = vincoloImpegno.getImporto();
					BigDecimal oldImportoVincolo = vincoloDb.getMovgestTsImporto();
					BigDecimal deltaVincolo = importoNuovo.subtract(oldImportoVincolo);
					/////////////////////
					
					//Recupero i dati dell'accertamento:
					Integer idMovGestAccertamento = vincoloImpegno.getAccertamento().getUid();
					MovGestInfoDto movGestInfoDto = modificaVincoliImpegnoInfoDto.findInfoAccertamento(idMovGestAccertamento);
					//commentato perche' deve gia arrivare da sopra:
					//MovGestInfoDto movGestInfoDto = caricaInfoAccertamentoDelVincolo(datiOperazioneDto, bilancio, bilancioAnnoSuccessivo,vincoloImpegno,null);
					
					//Recupero l'eventuale vincolo residuo:
					VincoloImpegno vincoloResiduo = individuaVincoloResiduo(vincoliResidui, movGestInfoDto.getSiacTMovgestResiduo());
					//setto i dati:
					EsitoCalcoloImportiVincoliDto infoIterato = settaDatiInfoCalcoloVincolo(deltaVincolo,movGestInfoDto,vincoloResiduo);
					//aggiungo in lista:
					listaEsito.add(infoIterato);
				}
			}
			
		} else if(chiamante.equals(ChiamanteDoppiaGestImpegno.ORDINATIVI_PAGAMENTO)){
			//Se il servizio � richiamato dalla gestone ordinativi di pagamento:
				// 1.	Ridistribuire il nuovo Vincolo.pagato sui tutti i vincoli dell�impegno come descritto a par. Pagato sui vincoli
				// 2.	Ricalcolare Vincolo.importo Res = Vincolo.importo - Vincolo.pagato
			listaEsito = calcolaPagatoSuVincoli(bilancio,bilancioAnnoSuccessivo, siacTMovgestTsImpegnoDaAggiornare, siacTMovgestTsImpegnoResiduo, datiOperazioneDto);
		}
		
		
		return listaEsito;
	}
	
	private EsitoCalcoloImportiVincoliDto settaDatiInfoCalcoloVincolo(
			BigDecimal deltaVincolo, MovGestInfoDto movGestInfoDto,
			VincoloImpegno vincoloResiduo) {
		EsitoCalcoloImportiVincoliDto infoIterato = new EsitoCalcoloImportiVincoliDto();
		infoIterato.setDeltaVincolo(deltaVincolo);
		infoIterato.setSiacTMovgestTsAcc(movGestInfoDto.getSiacTMovgestTs());
		infoIterato.setSiacTMovgestTsAccResiduo(movGestInfoDto.getSiacTMovgestTsResiduo());
		infoIterato.setVincoloResiduo(vincoloResiduo);
		infoIterato.setAccertamentoFresco((Accertamento) movGestInfoDto.getMovGestCompleto());
		return infoIterato;
	}

	private VincoloImpegno individuaVincoloResiduo(List<VincoloImpegno> vincoliResidui,SiacTMovgestFin siacTMovgestAccResiduo){
		VincoloImpegno vincoloResiduo = null;
		if(siacTMovgestAccResiduo!=null && vincoliResidui!=null && vincoliResidui.size()>0){
			for(VincoloImpegno it :vincoliResidui){
				if(it.getAccertamento().getUid()==siacTMovgestAccResiduo.getUid()){
					//Trovato
					vincoloResiduo = it;
				}
			}
		}
		return vincoloResiduo;
	}
	
	/**
	 * Metodo di comodo per caricare i puntamenti all'accertamento e al suo eventuale residuo riguardo all'accertamento
	 * di un certo vincolo considerato
	 * 
	 * VincoloImpegno vincoloImpegno,SiacRMovgestTsFin vincoloImpegnoEntity --> SONO IN MUTUA ESCLUSIONE a volte dispongo di uno a volte dell'altro...
	 * 
	 * @param datiOperazioneDto
	 * @param bilancio
	 * @param bilancioAnnoSuccessivo
	 * @param vincoloImpegno
	 * @param vincoloImpegnoEntity
	 * @return
	 */
	public MovGestInfoDto caricaInfoAccertamentoDelVincolo(DatiOperazioneDto datiOperazioneDto,Bilancio bilancio,Bilancio bilancioAnnoSuccessivo,
			VincoloImpegno vincoloImpegno,SiacRMovgestTsFin vincoloImpegnoEntity){
		
		MovGestInfoDto movGestInfoDto = new MovGestInfoDto();
		
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getUid();
		
		String bilancioString = Integer.toString(bilancio.getAnno());
		String bilancioPiuUnoString = Integer.toString(bilancioAnnoSuccessivo.getAnno());
		
		//Recupero i dati dell'accertamento:
		SiacTMovgestFin siacTMovgestAcc = null;
		SiacTMovgestTsFin siacTMovgestTsAcc = null;
		if(vincoloImpegno!=null){
			siacTMovgestAcc = movimentoGestioneDao.findAccertamento(idEnte, vincoloImpegno.getAccertamento().getAnnoMovimento(), vincoloImpegno.getAccertamento().getNumeroBigDecimal(), bilancioString, getNow());
			siacTMovgestTsAcc= siacTMovgestTsRepository.findMovgestTsByMovgest(idEnte, datiOperazioneDto.getTs(), siacTMovgestAcc.getMovgestId()).get(0);
		} else if(vincoloImpegnoEntity!=null){
			if(vincoloImpegnoEntity.getSiacTMovgestTsA()!=null){
				siacTMovgestTsAcc = vincoloImpegnoEntity.getSiacTMovgestTsA();
				siacTMovgestAcc = siacTMovgestTsAcc.getSiacTMovgest();
			}
		}
		
		if(siacTMovgestAcc!=null){
			
			//Recupero i dati dell'accertamento residuo:
			SiacTMovgestFin siacTMovgestAccResiduo= movimentoGestioneDao.findAccertamento(idEnte, siacTMovgestAcc.getMovgestAnno(), siacTMovgestAcc.getMovgestNumero(), bilancioPiuUnoString, getNow());
			SiacTMovgestTsFin siacTMovgestTsAccResiduo = null;
			if(siacTMovgestAccResiduo!=null){
				siacTMovgestTsAccResiduo = siacTMovgestTsRepository.findMovgestTsByMovgest(idEnte, datiOperazioneDto.getTs(), siacTMovgestAccResiduo.getMovgestId()).get(0);
			}
			
			movGestInfoDto.setSiacTMovgest(siacTMovgestAcc);
			movGestInfoDto.setSiacTMovgestTs(siacTMovgestTsAcc);
			
			movGestInfoDto.setSiacTMovgestResiduo(siacTMovgestAccResiduo);
			movGestInfoDto.setSiacTMovgestTsResiduo(siacTMovgestTsAccResiduo);
			
			movGestInfoDto.setMovGestId(siacTMovgestAcc.getMovgestId());
			
		}
		
		return movGestInfoDto;
	}

	/**
	 * Routine che si occupa della "doppia gestione" in fase di aggiornamento di un nuovo accertamento.
	 * @param richiedente
	 * @param ente
	 * @param bilancio
	 * @param accertamentoDaAggiornare
	 * @param datiOperazioneDto
	 * @param capitoliDaServizio
	 * @return
	 */
	public List<Errore> aggiornamentoInDoppiaGestioneAccertamento(Richiedente richiedente, Ente ente, Bilancio bilancio, Accertamento accertamentoDaAggiornare,
			DatiOperazioneDto datiOperazioneDto, CapitoliInfoDto capitoliDaServizio){
		List<Errore> listaErrori=new ArrayList<Errore>();
		boolean fromDoppiaGestione = true;
		//metodo doppiaGestioneInAggiornaImpegno
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		
		Bilancio bilancioAnnoSuccessivo = buildBilancioAnnoSuccessivo(bilancio, datiOperazioneDto);
		
		//System.out.println("aggiornamentoInDoppiaGestioneAccertamento. annoBilancio: " + datiOperazioneDto.getAnnoBilancio());
		
		DatiOperazioneDto datiOperazioneDtoMod = new DatiOperazioneDto(System.currentTimeMillis(), Operazione.MODIFICA, datiOperazioneDto.getSiacTEnteProprietario(), datiOperazioneDto.getSiacDAmbito(), richiedente.getAccount().getId(), datiOperazioneDto.getAnnoBilancio());
		DatiOperazioneDto datiOperazioneDtoIns = new DatiOperazioneDto(System.currentTimeMillis(), Operazione.INSERIMENTO, datiOperazioneDto.getSiacTEnteProprietario(), datiOperazioneDto.getSiacDAmbito(), richiedente.getAccount().getId(), datiOperazioneDto.getAnnoBilancio());
		DatiOperazioneDto datiOperazioneCancella = new DatiOperazioneDto(System.currentTimeMillis(), Operazione.CANCELLAZIONE_LOGICA_RECORD, datiOperazioneDto.getSiacTEnteProprietario(), datiOperazioneDto.getSiacDAmbito(), richiedente.getAccount().getId(), datiOperazioneDto.getAnnoBilancio());

		//ricerco residuo
		SiacTMovgestFin siacTMovgestResiduo = movimentoGestioneDao.findByEnteAnnoNumeroBilancioValido(idEnte, accertamentoDaAggiornare.getAnnoMovimento() , accertamentoDaAggiornare.getNumeroBigDecimal(), CostantiFin.MOVGEST_TIPO_ACCERTAMENTO, String.valueOf(accertamentoDaAggiornare.getAnnoMovimento()+1), datiOperazioneDto.getTs());
//		List<SiacTMovgestFin> siacTMovgestS = siacTMovgestRepository.findByEnteAnnoNumeroBilancioValido(idEnte, accertamentoDaAggiornare.getAnnoMovimento() , accertamentoDaAggiornare.getNumero(), CostantiFin.MOVGEST_TIPO_ACCERTAMENTO, String.valueOf(accertamentoDaAggiornare.getAnnoMovimento()+1), datiOperazioneDto.getTs());
//		if(siacTMovgestS!=null && siacTMovgestS.size()>0){
//			siacTMovgestResiduo = siacTMovgestS.get(0);
//		}
		//ricerco i sub del residuo
		
		//ricavo movgest e movgestts dell'impegno aggiornato
		SiacTMovgestFin siacTMovgestAggiornato = movimentoGestioneDao.findByEnteAnnoNumeroBilancioValido(idEnte, accertamentoDaAggiornare.getAnnoMovimento() , accertamentoDaAggiornare.getNumeroBigDecimal(), CostantiFin.MOVGEST_TIPO_ACCERTAMENTO, String.valueOf(accertamentoDaAggiornare.getAnnoMovimento()), datiOperazioneDto.getTs());
		
		SiacTMovgestTsFin siacTMovgestTsDaAggiornare = siacTMovgestTsRepository.findMovgestTsByMovgest(idEnte, datiOperazioneDtoMod.getTs(), siacTMovgestAggiornato.getMovgestId()).get(0);			
		//ricerco i sub dell'impegno aggiornato
		List<SiacTMovgestTsFin> siacTMovgestTsDaAggiornareSub=siacTMovgestTsRepository.findListaSiacTMovgestTsFigli(idEnte, getNow(), siacTMovgestTsDaAggiornare.getMovgestTsId());

		SiacDAmbitoFin  siacDAmbitoPerCode = siacDAmbitoRepository.findAmbitoByCode(CostantiFin.AMBITO_FIN, idEnte);

		//Calcolo disponibile di cassa impegno
		DisponibilitaMovimentoGestioneContainer dispCassaImpegno;
		String statoCod = null;
		
		if(siacTMovgestResiduo!=null){
			SiacDMovgestStatoFin statoSub = getStato(siacTMovgestTsDaAggiornare, datiOperazioneDtoMod);
			statoCod = statoSub.getMovgestStatoCode();

			dispCassaImpegno = calcolaDisponibiltaAIncassareAccertamento(siacTMovgestTsDaAggiornare, statoCod, idEnte);
		}else {
			dispCassaImpegno = new DisponibilitaMovimentoGestioneContainer(accertamentoDaAggiornare.getImportoAttuale(), "Se il movimento non e' residuo, la disponibilita' deve essere l'importo attuale dell'accertamento");
		}
		
		if(NumericUtils.maggioreDiZero(dispCassaImpegno.getDisponibilita())){

			Integer movgestIdResiduo=null;

			if(siacTMovgestResiduo!=null){
				//Aggiorno l'impegno residuo

				movgestIdResiduo=siacTMovgestResiduo.getMovgestId();
				SiacTMovgestTsFin siacTMovgestTsResiduo = siacTMovgestTsRepository.findMovgestTsByMovgest(idEnte, datiOperazioneDtoMod.getTs(), movgestIdResiduo).get(0);			
				Integer movgestTsIdResiduo = siacTMovgestTsResiduo.getUid();
				//ricerco i sub dell'impegno aggiornato
				List<SiacTMovgestTsFin> siacTMovgestTsResiduoSub=siacTMovgestTsRepository.findListaSiacTMovgestTsFigli(idEnte, datiOperazioneDto.getTs(), movgestTsIdResiduo);


				accertamentoDaAggiornare.setImportoAttuale(dispCassaImpegno.getDisponibilita());

				accertamentoDaAggiornare.setUid(movgestIdResiduo);
				
				//EVENTUALI SUB:
				EsitoAggiornamentoSubMovGestTs infoSubValutati=catalogaSubDoppiaGestione(siacTMovgestTsDaAggiornareSub, siacTMovgestTsResiduoSub);
				List<SiacTMovgestTsFin> subInseriti = infoSubValutati.getInseriti();
				List<SiacTMovgestTsFin> subAggiornati = infoSubValutati.getAggiornati();
				List<SiacTMovgestTsFin> subEliminati = infoSubValutati.getEliminati();

				SubMovgestInModificaInfoDto subMovgestInModificaInfoDto=new SubMovgestInModificaInfoDto();
				ArrayList<SubAccertamento> subAccertamentiDaInserire=new ArrayList<SubAccertamento>();
				ArrayList<SubAccertamento> subAccertamentiDaModificare=new ArrayList<SubAccertamento>();
				ArrayList<SiacTMovgestTsFin> subAccertamentiDaEliminare=new ArrayList<SiacTMovgestTsFin>();
				List<SubAccertamento> listaSubAccertamentiDaAggRes=new ArrayList<SubAccertamento>();

				if(subInseriti!=null && subInseriti.size()>0){
					//ciclare su subInseriti DEFINITVI e ribaltarli creando dei nuovi sub, ogni ribaltamento avra' importo = importo del nuovo sub
					//per ogni sub della lista  da inserire
					for(SiacTMovgestTsFin siacTMovgestTsInserito : subInseriti){
						String code = siacTMovgestTsInserito.getMovgestTsCode();
						BigDecimal codeBD = new BigDecimal(code);
						List<SubAccertamento> listaSubDaAgg=accertamentoDaAggiornare.getElencoSubAccertamenti();
						boolean esisteInListaSubDaAgg=false;
						if (listaSubDaAgg!=null && listaSubDaAgg.size()>0) {
							for (SubAccertamento subAccertamentoDaAgg : listaSubDaAgg) {
								if (subAccertamentoDaAgg.getNumeroBigDecimal()!=null) {
									if (subAccertamentoDaAgg.getNumeroBigDecimal().compareTo(codeBD)==0) {
										esisteInListaSubDaAgg=true;
										break;
									}
								}else {
									if (subAccertamentoDaAgg.getStatoOperativoMovimentoGestioneEntrata().equals(CostantiFin.MOVGEST_STATO_DEFINITIVO) &&
											subAccertamentoDaAgg.getImportoAttuale().compareTo(BigDecimal.ZERO)==1) {
										subAccertamentoDaAgg.setNumeroBigDecimal(codeBD);
										subAccertamentiDaInserire.add(subAccertamentoDaAgg);
										listaSubAccertamentiDaAggRes.add(subAccertamentoDaAgg);
									}
								}
							}	
						}
						
						if (esisteInListaSubDaAgg) {
							//se il sub esisteva gia' lo metto tra quelli da aggiornare
							subAggiornati.add(siacTMovgestTsInserito);
						}
					}
				}
				
				if(subAggiornati!=null && subAggiornati.size()>0){
					for(SiacTMovgestTsFin siacTMovgestTsAggiornato : subAggiornati){
						//per ogni sub nella lista cerco il corrispondente sul residuo
						List<SiacTMovgestTsFin> listaSiacTMovgestTsResiduo =siacTMovgestTsRepository.findSubMovgestTsByCodeAndMovgestId(idEnte, datiOperazioneDtoMod.getTs(), movgestIdResiduo, siacTMovgestTsAggiornato.getMovgestTsCode());

						if(listaSiacTMovgestTsResiduo!=null && listaSiacTMovgestTsResiduo.size()>0){
							//se esiste il sub ribaltato viene aggiornato l'importo 

							SiacTMovgestTsFin siacTMovgestTsSubAggResiduo=listaSiacTMovgestTsResiduo.get(0);

							List<SubAccertamento> listaSubDaAgg=accertamentoDaAggiornare.getElencoSubAccertamenti();
							if (listaSubDaAgg!=null && listaSubDaAgg.size()>0) {
								for (SubAccertamento subAccertamentoDaAgg : listaSubDaAgg) {
									if (subAccertamentoDaAgg.getUid()!=0) {
										SiacTMovgestTsFin siacTMovgestTsCorr=siacTMovgestTsRepository.findMovgestTsByMovgestTsId(idEnte, getNow(), subAccertamentoDaAgg.getUid());
										subAccertamentoDaAgg.setNumeroBigDecimal(new BigDecimal(siacTMovgestTsCorr.getMovgestTsCode()));
										if (subAccertamentoDaAgg.getNumeroBigDecimal()!=null && 
												subAccertamentoDaAgg.getNumeroBigDecimal().compareTo(new BigDecimal(siacTMovgestTsSubAggResiduo.getMovgestTsCode()))==0) {
											DisponibilitaMovimentoGestioneContainer dispCassaSub = calcolaDisponibiltaAIncassareSubAccertamento(siacTMovgestTsAggiornato, subAccertamentoDaAgg.getStatoOperativoMovimentoGestioneEntrata(), idEnte);
											if(NumericUtils.maggioreDiZero(dispCassaSub.getDisponibilita())){
												//Aggiorno sub residuo se esiste e 
												subAccertamentoDaAgg.setImportoAttuale(dispCassaSub.getDisponibilita());
												subAccertamentoDaAgg.setImportoIniziale(dispCassaSub.getDisponibilita());
												subAccertamentoDaAgg.setUid(siacTMovgestTsSubAggResiduo.getUid());
												
												//Modifiche soggetto
												if (subAccertamentoDaAgg.getListaModificheMovimentoGestioneEntrata()!=null && 
														subAccertamentoDaAgg.getListaModificheMovimentoGestioneEntrata().size()>0) {
													for (ModificaMovimentoGestioneEntrata modifica : subAccertamentoDaAgg.getListaModificheMovimentoGestioneEntrata()) {
														if (modifica.getSoggettoNewMovimentoGestione()!=null) {
															subAccertamentoDaAgg.setSoggetto(modifica.getSoggettoNewMovimentoGestione());
															subAccertamentoDaAgg.setSoggettoCode(modifica.getSoggettoNewMovimentoGestione().getCodiceSoggetto());
														}
													}
												}
												
												subAccertamentoDaAgg.setListaModificheMovimentoGestioneEntrata(null);
												
												//Aggiornare sub
												subAccertamentiDaModificare.add(subAccertamentoDaAgg);
												listaSubAccertamentiDaAggRes.add(subAccertamentoDaAgg);
											}else if(dispCassaSub!=null && dispCassaSub.getDisponibilita() != null && dispCassaSub.getDisponibilita().compareTo(BigDecimal.ZERO)==0){
												//se importo residuo=0 elimino il sub
												List<SiacTMovgestTsFin> listaSiacTMovgestTsEliminatoResiduo =siacTMovgestTsRepository.findSubMovgestTsByCodeAndMovgestId(idEnte, datiOperazioneDtoMod.getTs(), movgestIdResiduo, siacTMovgestTsAggiornato.getMovgestTsCode());

												if (listaSiacTMovgestTsEliminatoResiduo!=null && listaSiacTMovgestTsEliminatoResiduo.size()>0) {
													SiacTMovgestTsFin siacTMovgestTsEliminatoResiduo=listaSiacTMovgestTsEliminatoResiduo.get(0);
													if (siacTMovgestTsEliminatoResiduo!=null) {
														// caso movimenti collegati
														List<SiacTOrdinativoTsDetFin> elencoQuote=findQuoteValideFromAccertamentoSubAccertamento(idEnte, siacTMovgestTsEliminatoResiduo);
														if (elencoQuote!=null && elencoQuote.size()>0) {
															listaErrori.add(ErroreFin.ESISTONO_MOVIMENTI_COLLEGATI.getErrore("Cancellazione subaccertamento residuo",""));
															return listaErrori;
														}
														//Eliminare sub
														datiOperazioneCancella = new DatiOperazioneDto(System.currentTimeMillis(), Operazione.CANCELLAZIONE_LOGICA_RECORD, datiOperazioneDto.getSiacTEnteProprietario(), richiedente.getAccount().getId());
														SiacTMovgestTsFin siacTMovgestTsResiduoCanc = DatiOperazioneUtil.cancellaRecord(siacTMovgestTsEliminatoResiduo, siacTMovgestTsRepository, datiOperazioneCancella, siacTAccountRepository);
													}
												}

											}
										}
									}
								}
							}
						} else {
							//se non esiste il sub ribaltato viene creato con importo = dispCassaSub
							//(es. se sub passa da provv a definitivo)

							List<SubAccertamento> listaSubDaAgg=accertamentoDaAggiornare.getElencoSubAccertamenti();
							if (listaSubDaAgg!=null && listaSubDaAgg.size()>0) {
								for (SubAccertamento subAccertamentoDaAgg : listaSubDaAgg) {

									if (subAccertamentoDaAgg.getUid()!=0) {
										SiacTMovgestTsFin  subAccertamentoDaAggRep=siacTMovgestTsRepository.findOne(subAccertamentoDaAgg.getUid());
										if(subAccertamentoDaAggRep!=null){
											String numeroSubDaAggRep=subAccertamentoDaAggRep.getMovgestTsCode();
											subAccertamentoDaAgg.setNumeroBigDecimal(new BigDecimal(numeroSubDaAggRep));

											if (subAccertamentoDaAgg.getNumeroBigDecimal()!=null && 
													subAccertamentoDaAgg.getNumeroBigDecimal().compareTo(new BigDecimal(siacTMovgestTsAggiornato.getMovgestTsCode()))==0) {
												if (subAccertamentoDaAgg.getImportoAttuale().compareTo(BigDecimal.ZERO)==1 &&
														subAccertamentoDaAgg.getStatoOperativoMovimentoGestioneEntrata().equals(CostantiFin.MOVGEST_STATO_DEFINITIVO)) {
													//Inserisco sub residuo
													subAccertamentoDaAgg.setImportoAttuale(subAccertamentoDaAgg.getImportoAttuale());
													subAccertamentoDaAgg.setImportoIniziale(subAccertamentoDaAgg.getImportoAttuale());
													subAccertamentiDaInserire.add(subAccertamentoDaAgg);
													listaSubAccertamentiDaAggRes.add(subAccertamentoDaAgg);

												}
											}
										}
									}
								}
							}
						}
					}
				}
				if(subEliminati!=null && subEliminati.size()>0){
					for(SiacTMovgestTsFin siacTMovgestTsEliminato : subEliminati){

						List<SiacTMovgestTsFin> listaSiacTMovgestTsEliminatoResiduo =siacTMovgestTsRepository.findSubMovgestTsByCodeAndMovgestId(idEnte, datiOperazioneDtoMod.getTs(), movgestIdResiduo, siacTMovgestTsEliminato.getMovgestTsCode());

						if (listaSiacTMovgestTsEliminatoResiduo!=null && listaSiacTMovgestTsEliminatoResiduo.size()>0) {
							SiacTMovgestTsFin siacTMovgestTsEliminatoResiduo=listaSiacTMovgestTsEliminatoResiduo.get(0);
							if (siacTMovgestTsEliminatoResiduo!=null) {
								//controllo movimenti collegati
								List<SiacTOrdinativoTsDetFin> elencoQuote=findQuoteValideFromAccertamentoSubAccertamento(idEnte, siacTMovgestTsEliminatoResiduo);
								if (elencoQuote!=null && elencoQuote.size()>0) {
									listaErrori.add(ErroreFin.ESISTONO_MOVIMENTI_COLLEGATI.getErrore("Cancellazione subaccertamento residuo",""));
									return listaErrori;
								}
								datiOperazioneCancella = new DatiOperazioneDto(System.currentTimeMillis(), Operazione.CANCELLAZIONE_LOGICA_RECORD, datiOperazioneDto.getSiacTEnteProprietario(), richiedente.getAccount().getId());
								SiacTMovgestTsFin siacTMovgestTsResiduoCanc = DatiOperazioneUtil.cancellaRecord(siacTMovgestTsEliminatoResiduo, siacTMovgestTsRepository, datiOperazioneCancella, siacTAccountRepository);

							}
						}
					}
				}



				subMovgestInModificaInfoDto.setSubImpegniDaInserire(subAccertamentiDaInserire);
				subMovgestInModificaInfoDto.setSubImpegniDaModificare(subAccertamentiDaModificare);
				subMovgestInModificaInfoDto.setSubImpegniDaEliminare(subAccertamentiDaEliminare);
				subMovgestInModificaInfoDto.setSubImpegniOld(siacTMovgestTsResiduoSub);
				

				accertamentoDaAggiornare.setUid(siacTMovgestResiduo.getUid());
				accertamentoDaAggiornare.setElencoSubAccertamenti(listaSubAccertamentiDaAggRes);
				accertamentoDaAggiornare.setListaModificheMovimentoGestioneEntrata(null);
				
				//per correggere passaggio dati del provvedimento in campi errati
				if (accertamentoDaAggiornare.getAttoAmministrativo()==null &&
						!StringUtilsFin.isEmpty(accertamentoDaAggiornare.getAttoAmmAnno()) &&
						accertamentoDaAggiornare.getAttoAmmNumero()!=null && accertamentoDaAggiornare.getAttoAmmNumero()!=0 &&
						accertamentoDaAggiornare.getAttoAmmTipoAtto()!=null && !StringUtilsFin.isEmpty(accertamentoDaAggiornare.getAttoAmmTipoAtto().getCodice())) {
					AttoAmministrativo atto=new AttoAmministrativo();
					atto.setAnno(Integer.valueOf(accertamentoDaAggiornare.getAttoAmmAnno()));
					atto.setNumero(accertamentoDaAggiornare.getAttoAmmNumero());
					atto.setTipoAtto(accertamentoDaAggiornare.getAttoAmmTipoAtto());
					accertamentoDaAggiornare.setAttoAmministrativo(atto);
				}

				
				System.err.println("datiOperazioneDto.getAnnoBilancio: " + datiOperazioneDto.getAnnoBilancio());
				
				datiOperazioneDtoMod = new DatiOperazioneDto(System.currentTimeMillis(), Operazione.MODIFICA, datiOperazioneDto.getSiacTEnteProprietario(), siacDAmbitoPerCode, richiedente.getAccount().getId(), datiOperazioneDto.getAnnoBilancio());

				System.err.println("datiOperazioneDtoMod.getAnnoBilancio: " + datiOperazioneDtoMod.getAnnoBilancio());
				
				EsitoAggiornamentoMovimentoGestioneDto esitoOperazioneInternaAggiornaAccertamento = operazioneInternaAggiornaAccertamento(richiedente, ente, bilancioAnnoSuccessivo, 
						accertamentoDaAggiornare, accertamentoDaAggiornare.getSoggetto(), null,
						datiOperazioneDtoMod, subMovgestInModificaInfoDto,fromDoppiaGestione,capitoliDaServizio);

				if(esitoOperazioneInternaAggiornaAccertamento.getListaErrori()!=null && esitoOperazioneInternaAggiornaAccertamento.getListaErrori().size()>0){
					return esitoOperazioneInternaAggiornaAccertamento.getListaErrori();
				}


			} else {
				//Inserisco l'impegno residuo

				accertamentoDaAggiornare.setUid(0);
				

				EsitoAggiornamentoSubMovGestTs infoSubValutati=catalogaSubDoppiaGestione(siacTMovgestTsDaAggiornareSub, null);
				List<SiacTMovgestTsFin> subInseriti = infoSubValutati.getInseriti();
				ArrayList<SubAccertamento> subInseritiResidui = new ArrayList<SubAccertamento>();
				List<SubAccertamento> listaSubAccertamentiDaInserire=new ArrayList<SubAccertamento>();

				if(subInseriti!=null && subInseriti.size()>0){
					for(SiacTMovgestTsFin siacTMovgestTsAggiornato : subInseriti){
						List<SubAccertamento> listaSubDaAgg=accertamentoDaAggiornare.getElencoSubAccertamenti();
						if (listaSubDaAgg!=null && listaSubDaAgg.size()>0) {
							for (SubAccertamento subAccertamentoDaAgg : listaSubDaAgg) {

								if (subAccertamentoDaAgg.getUid()!=0) {
									SiacTMovgestTsFin  subAccertamentoDaAggRep=siacTMovgestTsRepository.findOne(subAccertamentoDaAgg.getUid());
									if(subAccertamentoDaAggRep!=null){
										String numeroSubDaAggRep=subAccertamentoDaAggRep.getMovgestTsCode();
										subAccertamentoDaAgg.setNumeroBigDecimal(new BigDecimal(numeroSubDaAggRep));

										if (subAccertamentoDaAgg.getNumeroBigDecimal()!=null && 
												subAccertamentoDaAgg.getNumeroBigDecimal().compareTo(new BigDecimal(siacTMovgestTsAggiornato.getMovgestTsCode()))==0) {
											if (subAccertamentoDaAgg.getImportoAttuale().compareTo(BigDecimal.ZERO)==1 &&
													subAccertamentoDaAgg.getStatoOperativoMovimentoGestioneEntrata().equals(CostantiFin.MOVGEST_STATO_DEFINITIVO)) {
												//Inserisco sub residuo
												subAccertamentoDaAgg.setImportoAttuale(subAccertamentoDaAgg.getImportoAttuale());
												subAccertamentoDaAgg.setImportoIniziale(subAccertamentoDaAgg.getImportoAttuale());
												//Inserisco subImpegno
												listaSubAccertamentiDaInserire.add(subAccertamentoDaAgg);
												subInseritiResidui.add(subAccertamentoDaAgg);
											}
										}
									}
								}
							}
						}
					}
					//Se ci sono subimpegni devo aggiornare l'impegno appena inserito
					if(listaSubAccertamentiDaInserire!=null && listaSubAccertamentiDaInserire.size()>0){
						accertamentoDaAggiornare.setElencoSubAccertamenti(listaSubAccertamentiDaInserire);

					}
				}
				//Inserisco l'accertamento con l'operazione interna

				Integer numeroAccertamento=new Integer(accertamentoDaAggiornare.getNumeroBigDecimal().intValue());
				accertamentoDaAggiornare.setListaModificheMovimentoGestioneEntrata(new ArrayList<ModificaMovimentoGestioneEntrata>());
				datiOperazioneDtoIns = new DatiOperazioneDto(System.currentTimeMillis(), Operazione.INSERIMENTO, datiOperazioneDto.getSiacTEnteProprietario(), siacDAmbitoPerCode, richiedente.getAccount().getId());
				EsitoInserimentoMovimentoGestioneDto esitoOperazioneInternaCiclo = operazioneInternaInserisceAccertamento(richiedente, ente, bilancioAnnoSuccessivo, accertamentoDaAggiornare, datiOperazioneDtoIns, numeroAccertamento);
				
				if(esitoOperazioneInternaCiclo.getListaErrori()!=null && esitoOperazioneInternaCiclo.getListaErrori().size()>0){
					return esitoOperazioneInternaCiclo.getListaErrori();
				}
				
			}


		} else {
			//LA NUOVA DISPONIBILITA' E' ZERO

			//esiste residuo? AND importoUtil == 0
			//allora viene cancellato l'impegno e i sub impegni residui se non ci sono liq residue agganciate
			if(siacTMovgestResiduo!=null){

				Integer movgestIdResiduo=siacTMovgestResiduo.getMovgestId();
				SiacTMovgestTsFin siacTMovgestTsResiduo = siacTMovgestTsRepository.findMovgestTsByMovgest(idEnte, datiOperazioneDtoMod.getTs(), movgestIdResiduo).get(0);			
				BigDecimal importoUtilizzabileRibaltato = estraiImportoUtilizzabileByMovgestTsId(siacTMovgestTsResiduo.getMovgestTsId(), datiOperazioneDtoMod);
				
				if(importoUtilizzabileRibaltato.compareTo(BigDecimal.ZERO)==0){
					//anche l'importo utilizzabile e' zero
					
					//controllo movimenti collegati
					List<SiacTLiquidazioneFin> elencoSiacTLiquidazione=findLiquidazioniValideFromSubImpegno(siacTMovgestTsResiduo);
					if (elencoSiacTLiquidazione!=null && elencoSiacTLiquidazione.size()>0) {
						listaErrori.add(ErroreFin.ESISTONO_MOVIMENTI_COLLEGATI.getErrore("Cancellazione accertamento residuo",""));
						return listaErrori;
					}

					List<SiacTMovgestTsFin> listaSiacTMovgestTsSubResiduo =siacTMovgestTsRepository.findSubMovgestTsByCodeAndMovgestId(idEnte, datiOperazioneDtoMod.getTs(), movgestIdResiduo, siacTMovgestTsResiduo.getMovgestTsCode());

					//se esistono subimpegno li cancello
					if (listaSiacTMovgestTsSubResiduo!=null && listaSiacTMovgestTsSubResiduo.size()>0) {
						for (SiacTMovgestTsFin siacTMovgestTsSubResiduo : listaSiacTMovgestTsSubResiduo) {
							if (siacTMovgestTsSubResiduo!=null) {
								//controllo movimenti collegati
								SiacTMovgestTsFin siacTMovgestTsResiduoCanc = DatiOperazioneUtil.cancellaRecord(siacTMovgestTsSubResiduo, siacTMovgestTsRepository, datiOperazioneCancella, siacTAccountRepository);

							}
						}
					}

					SiacTMovgestTsFin siacTMovgestTsResiduoCanc = DatiOperazioneUtil.cancellaRecord(siacTMovgestTsResiduo, siacTMovgestTsRepository, datiOperazioneCancella, siacTAccountRepository);
					SiacTMovgestFin siacTMovgestResiduoCanc = DatiOperazioneUtil.cancellaRecord(siacTMovgestResiduo, siacTMovgestRepository, datiOperazioneCancella, siacTAccountRepository);
					
					
				} else if(importoUtilizzabileRibaltato.compareTo(BigDecimal.ZERO)>0){
					//aggiornare importo attuale = iniz = zero
					//rimane solo per tenere traccia dell importoUtil che e' diverso da zero
					
					//eventuali sub si cancellano:
					List<SiacTMovgestTsFin> listaSiacTMovgestTsSubResiduo =siacTMovgestTsRepository.findSubMovgestTsByCodeAndMovgestId(idEnte, datiOperazioneDtoMod.getTs(), movgestIdResiduo, siacTMovgestTsResiduo.getMovgestTsCode());
					if (listaSiacTMovgestTsSubResiduo!=null && listaSiacTMovgestTsSubResiduo.size()>0) {
						for (SiacTMovgestTsFin siacTMovgestTsSubResiduo : listaSiacTMovgestTsSubResiduo) {
							if (siacTMovgestTsSubResiduo!=null) {
								//controllo movimenti collegati
								SiacTMovgestTsFin siacTMovgestTsResiduoCanc = DatiOperazioneUtil.cancellaRecord(siacTMovgestTsSubResiduo, siacTMovgestTsRepository, datiOperazioneCancella, siacTAccountRepository);
							}
						}
					}
					//pongo a zero importo attuale e iniziale per l'accertamento residuo:
					saveImporto(BigDecimal.ZERO, datiOperazioneDto, siacTMovgestTsResiduo, CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE);
					saveImporto(BigDecimal.ZERO, datiOperazioneDto, siacTMovgestTsResiduo, CostantiFin.MOVGEST_TS_DET_TIPO_INIZIALE);
				}

			}

		}
		
		//Termino restituendo l'oggetto di ritorno: 
        return listaErrori;
	}
	
	/**
	 * Metodo vuoto, viene fatto l'override in ImpegnoDad e AccertamentoDad
	 * @param richiedente
	 * @param ente
	 * @param bilancio
	 * @param impegnoDaAggiornare
	 * @param soggettoCreditore
	 * @param unitaElemDiGest
	 * @param datiOperazione
	 * @param subMovgestInModificaValutati
	 * @param fromDoppiaGestione
	 * @param capitoliInfo
	 * @return
	 */
//	public EsitoAggiornamentoMovimentoGestioneDto operazioneInternaAggiornaMovGest(Richiedente richiedente, 
//			Ente ente, Bilancio bilancio, MovimentoGestione impegnoDaAggiornare,
//			Soggetto soggettoCreditore, UnitaElementareGestione unitaElemDiGest,
//			DatiOperazioneDto datiOperazione, SubMovgestInModificaInfoDto subMovgestInModificaValutati,
//			boolean fromDoppiaGestione,CapitoliInfoDto capitoliInfo){
//		return null;
//	}
	
	public EsitoAggiornamentoMovimentoGestioneDto operazioneInternaAggiornaAccertamento(Richiedente richiedente, Ente ente, Bilancio bilancio, MovimentoGestione impegnoDaAggiornare, Soggetto soggettoCreditore, UnitaElementareGestione unitaElemDiGest, 
			DatiOperazioneDto datiOperazione, SubMovgestInModificaInfoDto subMovgestInModificaValutati,boolean fromDoppiaGestione,CapitoliInfoDto capitoliInfo){
		EsitoAggiornamentoMovimentoGestioneDto esito=new EsitoAggiornamentoMovimentoGestioneDto();
		ImpegnoInModificaInfoDto impegnoInModificaInfoDto = getDatiGeneraliImpegnoInAggiornamento((Accertamento)impegnoDaAggiornare, datiOperazione, bilancio);
	
		impegnoInModificaInfoDto.setDoppiaGestione(fromDoppiaGestione);
	
		if(subMovgestInModificaValutati==null){
			impegnoInModificaInfoDto = valutaSubImp(impegnoDaAggiornare, impegnoInModificaInfoDto, datiOperazione, bilancio);
		} else {
			impegnoInModificaInfoDto.setInfoSubValutati(subMovgestInModificaValutati);
		}
		
		HashMap<Integer, CapitoloEntrataGestione> capitoliEntrata = capitoliInfo.getCapitoliDaServizioEntrata();
		
		List<Errore> listaErrori = this.controlliDiMeritoAggiornamentoAccertamentoOperazioneInterna(richiedente,  ente, bilancio, (Accertamento) impegnoDaAggiornare,
				datiOperazione, impegnoInModificaInfoDto,capitoliEntrata);
		if(listaErrori!=null && listaErrori.size()>0){
			esito.setListaErrori(listaErrori);
			return esito;
		}
		
		esito = this.aggiornaMovimento(richiedente, ente, bilancio, impegnoDaAggiornare, unitaElemDiGest,datiOperazione,
				impegnoInModificaInfoDto,capitoliInfo);
			
		//Termino restituendo l'oggetto di ritorno: 
		return esito;
	}
	
	public EsitoAggiornamentoMovimentoGestioneDto operazioneInternaAggiornaImpegno(Richiedente richiedente, Ente ente, Bilancio bilancio,
			MovimentoGestione impegnoDaAggiornare, Soggetto soggettoCreditore, UnitaElementareGestione unitaElemDiGest,
			DatiOperazioneDto datiOperazione,SubMovgestInModificaInfoDto subMovgestInModificaValutati,boolean fromDoppiaGestione,CapitoliInfoDto capitoliInfo, ModificaVincoliImpegnoInfoDto infoVincoliValutati){
		
		EsitoAggiornamentoMovimentoGestioneDto esito=new EsitoAggiornamentoMovimentoGestioneDto();
		
		//carica i dati dell'impegno
		ImpegnoInModificaInfoDto impegnoInModificaInfoDto = getDatiGeneraliImpegnoInAggiornamento(impegnoDaAggiornare, datiOperazione, bilancio);
		SiacTMovgestTsFin siacTMovgestTs = impegnoInModificaInfoDto.getSiacTMovgestTs();
		
		impegnoInModificaInfoDto.setDoppiaGestione(fromDoppiaGestione);
		
		if(subMovgestInModificaValutati==null || impegnoInModificaInfoDto==null){
			impegnoInModificaInfoDto = valutaSubImp(impegnoDaAggiornare, impegnoInModificaInfoDto, datiOperazione, bilancio);
		} else {
			impegnoInModificaInfoDto.setInfoSubValutati(subMovgestInModificaValutati);
		}
		
		//valutazione vincoli eventualmente inseriti/modificati:
		if(infoVincoliValutati==null){
			//per motivi di ottimizzazione potrebbe gia arrivare dal chiamante se ha gia avuto modo di doverlo caricare...
			
			Impegno impegnoDaAggiornareCast = (Impegno) impegnoDaAggiornare;
			
//			System.out.println("vincoli impegno" + impegnoDaAggiornareCast.getVincoliImpegno()!=null ? impegnoDaAggiornareCast.getVincoliImpegno().size(): " impegno senza vincoli");
//			for (VincoloImpegno vincolo : impegnoDaAggiornareCast.getVincoliImpegno()) {
//				System.out.println("------- ");
//				System.out.println("vincolo -> Accertamento. numero: " + vincolo.getAccertamento().getNumero());
//				System.out.println("vincolo -> importo: " + vincolo.getImporto());	
//				System.out.println("------- ");
//			}
			
			infoVincoliValutati = valutaVincoliInAggiornamento(impegnoDaAggiornareCast.getVincoliImpegno(), siacTMovgestTs, datiOperazione);
		}
		impegnoInModificaInfoDto.setInfoVincoliValutati(infoVincoliValutati);
		
		HashMap<Integer, CapitoloUscitaGestione> capitoliUscita = capitoliInfo.getCapitoliDaServizioUscita();
		// controlli per aggiornamento
		EsitoControlliAggiornamentoMovimentoDto esitoControlli = this.controlliDiMeritoAggiornamentoImpegnoOperazioneInterna(richiedente,  ente, bilancio, (Impegno) impegnoDaAggiornare,
				datiOperazione, impegnoInModificaInfoDto,capitoliUscita);
		// setto eventuali errori o warning:
		List<Errore> listaErrori = esitoControlli.getListaErrori();
		esito.setListaErrori(listaErrori);
		
		if(listaErrori!=null && listaErrori.size()>0){
			// esito.setListaErrori(listaErrori);
			return esito;
		}
		
		// eliminato param soggettoCreditore, arriva null e non viene usato nel metodo aggiornaMovimento
		esito = this.aggiornaMovimento(richiedente, ente, bilancio, impegnoDaAggiornare, unitaElemDiGest,datiOperazione,
				impegnoInModificaInfoDto,capitoliInfo);
		
		esito.addWarning(esitoControlli.getListaWarning());

		//Termino restituendo l'oggetto di ritorno: 
        return esito;
	}

	/**
	 * Routine interna a aggiornamentoInDoppiaGestioneAccertamento e aggiornamentoInDoppiaGestioneImpegno 
	 * @param siacTMovgestTsDaAggiornareSub
	 * @param siacTMovgestTsResiduoSub
	 * @return
	 */
	private EsitoAggiornamentoSubMovGestTs catalogaSubDoppiaGestione(List<SiacTMovgestTsFin> siacTMovgestTsDaAggiornareSub, List<SiacTMovgestTsFin> siacTMovgestTsResiduoSub){
		EsitoAggiornamentoSubMovGestTs esito=new EsitoAggiornamentoSubMovGestTs();

		List<SiacTMovgestTsFin> inseriti=new ArrayList<SiacTMovgestTsFin>();
		List<SiacTMovgestTsFin> aggiornati=new ArrayList<SiacTMovgestTsFin>();
		List<SiacTMovgestTsFin> eliminati=new ArrayList<SiacTMovgestTsFin>();
		
		if (siacTMovgestTsDaAggiornareSub!=null && siacTMovgestTsDaAggiornareSub.size()>0) {
			for (SiacTMovgestTsFin subDaAggiornare : siacTMovgestTsDaAggiornareSub) {
				boolean daIns=true;
				if (siacTMovgestTsResiduoSub!=null && siacTMovgestTsResiduoSub.size()>0) {
					for (SiacTMovgestTsFin subResiduo : siacTMovgestTsResiduoSub) {
						if (subResiduo.getMovgestTsCode().equalsIgnoreCase(subDaAggiornare.getMovgestTsCode())) {
							daIns=false;
							break;
						}
					}
				}
				
				if (daIns) {
					inseriti.add(subDaAggiornare);
				}else {
					aggiornati.add(subDaAggiornare);
				}
			}
		}
		

		esito.setAggiornati(aggiornati);
		esito.setEliminati(eliminati);
		esito.setInseriti(inseriti);
		
		//Termino restituendo l'oggetto di ritorno: 
        return esito;
	}
	
	/**
	 * Si occupa di aggiornare le modifiche movimento gestione spesa. 
	 * @param listaMovimenti
	 * @param uIdImpegno
	 * @param enteId
	 * @param datiOperazione
	 * @param numeroModifiche
	 * @return
	 */
	private List<ModificaMovimentoGestioneSpesa> aggiornaModificheMovimentoGestioneSpesa(List<ModificaMovimentoGestioneSpesa> listaMovimenti , Integer uIdImpegno, Integer enteId ,DatiOperazioneDto datiOperazione, Integer numeroModifiche){
		List<ModificaMovimentoGestioneSpesa> movimenti = new ArrayList<ModificaMovimentoGestioneSpesa>();		
		//Gestisto modifiche impegno
		ModificaMovimentoGestioneSpesaInfoDto dtoImpegno = valutaModificheMovimentoSpesa(listaMovimenti,datiOperazione);
		//Gestisco Inserimento modifiche
		if(dtoImpegno.getModificheDaCreare() != null && dtoImpegno.getModificheDaCreare().size() > 0){
			for(ModificaMovimentoGestioneSpesa spesa : dtoImpegno.getModificheDaCreare()){
				//Modifica movimento gestione spesa importo
				if(spesa.isModificaDiImporto()){
						spesa = (ModificaMovimentoGestioneSpesa)inserisciModificaMovimentoImporto(spesa, uIdImpegno, enteId , datiOperazione, numeroModifiche);
				}
				//Modifica movimento gestione spesa soggetto
				if(spesa.isModificaDiSoggetto()){
					spesa = (ModificaMovimentoGestioneSpesa)inserisciModificaMovimentoSpesaSoggetto(spesa, uIdImpegno, enteId, datiOperazione, numeroModifiche);
				}
				movimenti.add(spesa);
			}
		}
		
		//Gestisco aggiornamentoModifica
		if(dtoImpegno.getModificheDaAggiornare() != null && dtoImpegno.getModificheDaAggiornare().size() > 0){
			for(ModificaMovimentoGestioneSpesa spesa : dtoImpegno.getModificheDaAggiornare()){
				spesa = (ModificaMovimentoGestioneSpesa) updateModificaMovimentoGestioneSpesa(spesa, uIdImpegno , enteId, datiOperazione);
				movimenti.add(spesa);
			}
		}
		
		//Ciclo rimanenti per inserirli nella lista di ritorno
		if(dtoImpegno.getModificheResidue() != null && dtoImpegno.getModificheResidue().size() > 0){
			for(ModificaMovimentoGestioneSpesa spesa :  dtoImpegno.getModificheResidue()){
				movimenti.add(spesa);
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return movimenti;
	}
	
	/**
	 * Si occupa di aggiornare le modifiche movimento gestione entrata. 
	 * @param movimento
	 * @param uIdAccertamento
	 * @param enteId
	 * @param datiOperazione
	 * @param numeroModifiche
	 * @return
	 */
	public ModificaMovimentoGestioneEntrata aggiornaModificheMovimentoGestioneEntrata(ModificaMovimentoGestioneEntrata movimento , Integer uIdAccertamento, Integer enteId ,DatiOperazioneDto datiOperazione, Integer numeroModifiche){
		List<ModificaMovimentoGestioneEntrata> listaMovimenti = new ArrayList<ModificaMovimentoGestioneEntrata>();
		listaMovimenti.add(movimento);
		List<ModificaMovimentoGestioneEntrata> aggiornata = 
				aggiornaModificheMovimentoGestioneEntrata(listaMovimenti, uIdAccertamento, enteId, datiOperazione, numeroModifiche);
		if(aggiornata!=null && aggiornata.size()>0){
			return aggiornata.get(0);
		}
		return null;
	}
		
	/**
	 * Si occupa di aggiornare le modifiche movimento gestione entrata. 
	 * @param listaMovimenti
	 * @param uIdAccertamento
	 * @param enteId
	 * @param datiOperazione
	 * @param numeroModifiche
	 * @return
	 */
	public List<ModificaMovimentoGestioneEntrata> aggiornaModificheMovimentoGestioneEntrata(List<ModificaMovimentoGestioneEntrata> listaMovimenti , Integer uIdAccertamento, Integer enteId ,DatiOperazioneDto datiOperazione, Integer numeroModifiche){
		List<ModificaMovimentoGestioneEntrata> movimenti = new ArrayList<ModificaMovimentoGestioneEntrata>();			
		//Gestisto modifiche impegno
		ModificaMovimentoGestioneEntrataInfoDto dtoAccertamento = valutaModificheMovimentoEntrata(listaMovimenti,datiOperazione);
		//Gestisco Inserimento modifiche
		if(dtoAccertamento.getModificheDaCreare() != null && dtoAccertamento.getModificheDaCreare().size() > 0){
			for(ModificaMovimentoGestioneEntrata spesa : dtoAccertamento.getModificheDaCreare()){
				//Modifica movimento gestione spesa importo
				if(spesa.getImportoNew()!=null && spesa.getImportoOld()!=null){
					if(spesa.getImportoOld().compareTo(BigDecimal.ZERO)!=0){
						spesa = (ModificaMovimentoGestioneEntrata)inserisciModificaMovimentoImporto(spesa, uIdAccertamento, enteId , datiOperazione, numeroModifiche);
					}
				}
				//Modifica movimento gestione spesa soggetto
				if(spesa.getSoggettoNewMovimentoGestione() != null || spesa.getClasseSoggettoNewMovimentoGestione() != null){
					spesa = (ModificaMovimentoGestioneEntrata)inserisciModificaMovimentoSpesaSoggetto(spesa, uIdAccertamento, enteId, datiOperazione, numeroModifiche);
				}
				movimenti.add(spesa);
			}
		}
		//Gestisco aggiornamentoModifica
		if(dtoAccertamento.getModificheDaAggiornare() != null && dtoAccertamento.getModificheDaAggiornare().size() > 0){
			for(ModificaMovimentoGestioneEntrata spesa : dtoAccertamento.getModificheDaAggiornare()){
				spesa = (ModificaMovimentoGestioneEntrata) updateModificaMovimentoGestioneSpesa(spesa, uIdAccertamento , enteId, datiOperazione);
				movimenti.add(spesa);
			}
		}
		//Ciclo rimanenti per inserirli nella lista di ritorno
		if(dtoAccertamento.getModificheResidue() != null && dtoAccertamento.getModificheResidue().size() > 0){
			for(ModificaMovimentoGestioneEntrata spesa :  dtoAccertamento.getModificheResidue()){
				movimenti.add(spesa);
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return movimenti;
	}
	
	/**
	 * Si occupa di aggiornare le modifiche gestione spesa per i sub imp
	 * @param listaSub
	 * @param enteId
	 * @param dto
	 * @param numeroModifiche
	 * @return
	 */
	private List<SubImpegno> aggiornaModificheMovimentoGestioneSpesaSubImpegno(List<SubImpegno> listaSub, Integer enteId, DatiOperazioneDto dto, Integer numeroModifiche){			
		List<SubImpegno> subImpegno = new ArrayList<SubImpegno>();
		if(listaSub != null && listaSub.size() > 0){
			for(SubImpegno sub : listaSub){
				List<ModificaMovimentoGestioneSpesa> movimenti = new ArrayList<ModificaMovimentoGestioneSpesa>();							
				//dto sub
				ModificaMovimentoGestioneSpesaInfoDto dtoSub = valutaModificheMovimentoSpesa(sub.getListaModificheMovimentoGestioneSpesa(),dto);
				if(dtoSub.getModificheDaCreare() != null && dtoSub.getModificheDaCreare().size() > 0){
					for(ModificaMovimentoGestioneSpesa spesa : dtoSub.getModificheDaCreare()){
							//Modifica movimento gestione spesa importo
							if(spesa.getImportoNew()!=null && spesa.getImportoOld()!=null){
								if(spesa.getImportoOld().compareTo(BigDecimal.ZERO)!=0){
									spesa = (ModificaMovimentoGestioneSpesa)inserisciModificaMovimentoImporto(spesa, sub.getUid(), enteId , dto, numeroModifiche);
									spesa.setNumeroSubImpegno(sub.getNumeroBigDecimal().intValue());
								}
							}
							//Modifica movimento gestione spesa soggetto
							if((spesa.getSoggettoNewMovimentoGestione() != null && spesa.getSoggettoOldMovimentoGestione()!=null)
									|| (spesa.getClasseSoggettoNewMovimentoGestione() != null || spesa.getClasseSoggettoOldMovimentoGestione()!=null)){
								spesa = (ModificaMovimentoGestioneSpesa)inserisciModificaMovimentoSpesaSoggetto(spesa, sub.getUid(), enteId, dto, numeroModifiche);
								spesa.setNumeroSubImpegno(sub.getNumeroBigDecimal().intValue());
								
								//setteo il nuovo soggetto del sub
								sub.setSoggetto(spesa.getSoggettoNewMovimentoGestione());
								sub.setSoggettoCode(spesa.getSoggettoNewMovimentoGestione().getCodiceSoggetto());
							}
						movimenti.add(spesa);
					}
				}
				
				if(dtoSub.getModificheDaAggiornare() != null && dtoSub.getModificheDaAggiornare().size() > 0){
					for(ModificaMovimentoGestioneSpesa spesa : dtoSub.getModificheDaAggiornare()){
						spesa = (ModificaMovimentoGestioneSpesa) updateModificaMovimentoGestioneSpesa(spesa, sub.getUid(), enteId, dto);
						movimenti.add(spesa);
					}
				}
				
				if(dtoSub.getModificheResidue() != null && dtoSub.getModificheResidue().size() > 0){
					for(ModificaMovimentoGestioneSpesa spesa : dtoSub.getModificheResidue()){
						movimenti.add(spesa);
					}
				}
				
				sub.setListaModificheMovimentoGestioneSpesa(movimenti);
				subImpegno.add(sub);
			}
		}
		
		//Termino restituendo l'oggetto di ritorno: 
        return subImpegno;
	}
	
	
	public List<ModificaMovimentoGestioneEntrataInfoDto> valutaModificheMovimentoSubAcc(List<SubAccertamento> listaSub, Integer enteId, DatiOperazioneDto dto){			
		List<ModificaMovimentoGestioneEntrataInfoDto> elencoValutazioni = new ArrayList<ModificaMovimentoGestioneEntrataInfoDto>();
		if(listaSub != null && listaSub.size() > 0){
			for(SubAccertamento sub : listaSub){
				//List<ModificaMovimentoGestioneEntrataInfoDto> movimenti = new ArrayList<ModificaMovimentoGestioneEntrataInfoDto>();							
				//dto sub
				ModificaMovimentoGestioneEntrataInfoDto valutato = valutaModificheMovimentoEntrata(sub.getListaModificheMovimentoGestioneEntrata(),dto);
				
				//start fix
				if(valutato!=null && valutato.getModificheDaCreare()!=null && valutato.getModificheDaCreare().size()>0){
					//FIX id del subimpegno al quale si riferiscono le nuove modifiche
					for(ModificaMovimentoGestioneEntrata it : valutato.getModificheDaCreare()){
						it.setUidSubAccertamento(sub.getUid());
						if(sub.getNumeroBigDecimal()!=null){
							it.setNumeroSubAccertamento(sub.getNumeroBigDecimal().intValue());
						}
					}
				}
				//end fix
				elencoValutazioni.add(valutato);
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return elencoValutazioni;
	}
	
	public List<ModificaMovimentoGestioneSpesaInfoDto> valutaModificheMovimentoSubImp(List<SubImpegno> listaSub, Integer enteId, DatiOperazioneDto dto){			
		List<ModificaMovimentoGestioneSpesaInfoDto> elencoValutazioni = new ArrayList<ModificaMovimentoGestioneSpesaInfoDto>();
		if(listaSub != null && listaSub.size() > 0){
			for(SubImpegno sub : listaSub){
				List<ModificaMovimentoGestioneSpesaInfoDto> movimenti = new ArrayList<ModificaMovimentoGestioneSpesaInfoDto>();							
				//dto sub
				ModificaMovimentoGestioneSpesaInfoDto valutato = valutaModificheMovimentoSpesa(sub.getListaModificheMovimentoGestioneSpesa(),dto);
				
				//start fix
				if(valutato!=null && valutato.getModificheDaCreare()!=null && valutato.getModificheDaCreare().size()>0){
					//FIX id del subimpegno al quale si riferiscono le nuove modifiche
					for(ModificaMovimentoGestioneSpesa it : valutato.getModificheDaCreare()){
						it.setUidSubImpegno(sub.getUid());
						if(sub.getNumeroBigDecimal()!=null){
							it.setNumeroSubImpegno(sub.getNumeroBigDecimal().intValue());
						}
					}
				}
				//end fix
				
				elencoValutazioni.add(valutato);
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return elencoValutazioni;
	}
	
	/**
	 * Si occupa di aggiornare le modifiche gestione spesa per i sub acc
	 * @param listaSub
	 * @param enteId
	 * @param dto
	 * @param numeroModifiche
	 * @return
	 */
	private List<SubAccertamento> aggiornaModificheMovimentoGestioneEntrataSubAccertamento(List<SubAccertamento> listaSub, Integer enteId, DatiOperazioneDto dto, Integer numeroModifiche){
		List<SubAccertamento> subAccertmaneto = new ArrayList<SubAccertamento>();
		if(listaSub != null && listaSub.size() > 0){
			for(SubAccertamento sub : listaSub){
				List<ModificaMovimentoGestioneEntrata> movimenti = new ArrayList<ModificaMovimentoGestioneEntrata>();
				//dto sub
				ModificaMovimentoGestioneEntrataInfoDto dtoSub = valutaModificheMovimentoEntrata(sub.getListaModificheMovimentoGestioneEntrata(),dto);
				if(dtoSub.getModificheDaCreare() != null && dtoSub.getModificheDaCreare().size() > 0){
					for(ModificaMovimentoGestioneEntrata spesa : dtoSub.getModificheDaCreare()){
							//Modifica movimento gestione spesa importo
							if(spesa.getImportoNew()!=null && spesa.getImportoOld()!=null){
								if(spesa.getImportoOld().compareTo(BigDecimal.ZERO)!=0){
									spesa = (ModificaMovimentoGestioneEntrata)inserisciModificaMovimentoImporto(spesa, sub.getUid(), enteId , dto, numeroModifiche);
									spesa.setNumeroSubAccertamento(sub.getNumeroBigDecimal().intValue());
								}
							}
							//Modifica movimento gestione spesa soggetto
							if(spesa.getSoggettoNewMovimentoGestione() != null || spesa.getClasseSoggettoNewMovimentoGestione() != null){
								spesa = (ModificaMovimentoGestioneEntrata)inserisciModificaMovimentoSpesaSoggetto(spesa, sub.getUid(), enteId, dto, numeroModifiche);
								spesa.setNumeroSubAccertamento(sub.getNumeroBigDecimal().intValue());
								
								//setteo il nuovo soggetto del sub
								sub.setSoggetto(spesa.getSoggettoNewMovimentoGestione());
								sub.setSoggettoCode(spesa.getSoggettoNewMovimentoGestione().getCodiceSoggetto());
							}
						movimenti.add(spesa);
					}
				}
				
				if(dtoSub.getModificheDaAggiornare() != null && dtoSub.getModificheDaAggiornare().size() > 0){
					for(ModificaMovimentoGestioneEntrata spesa : dtoSub.getModificheDaAggiornare()){
						spesa = (ModificaMovimentoGestioneEntrata) updateModificaMovimentoGestioneSpesa(spesa, sub.getUid(), enteId, dto);
						movimenti.add(spesa);
					}
				}
				
				if(dtoSub.getModificheResidue() != null && dtoSub.getModificheResidue().size() > 0){
					for(ModificaMovimentoGestioneEntrata spesa : dtoSub.getModificheResidue()){
						movimenti.add(spesa);
					}
				}
				
				sub.setListaModificheMovimentoGestioneEntrata(movimenti);
				subAccertmaneto.add(sub);
			}
		}
		
		//Termino restituendo l'oggetto di ritorno: 
        return subAccertmaneto;
	}
	
	/**
	 * Si occupa di aggiornare le modifiche gestione spesa
	 * @param spesa
	 * @param uIdImpegno
	 * @param enteId
	 * @param datiOperazioniDto
	 * @return
	 */
	private ModificaMovimentoGestione updateModificaMovimentoGestioneSpesa(ModificaMovimentoGestione spesa, Integer uIdImpegno, Integer enteId, DatiOperazioneDto datiOperazioniDto){
		SiacTModificaFin update = siacTModificaRepository.findOne(spesa.getUid());
		update.setDataCreazione(update.getDataCreazione());
		update.setDataModifica(new Date());
		
		//scrivo il nuovo atto amministrativo
		AttoAmministrativo attoAmm = buildAttoAmministrativo(enteId, spesa.getAttoAmministrativoNumero(), spesa.getAttoAmministrativoTipoCode(), Integer.valueOf(spesa.getAttoAmministrativoAnno()), spesa.getIdStrutturaAmministrativa());
		SiacTAttoAmmFin siacTAttoAmm = getSiacTAttoAmmFromAttoAmministrativo(attoAmm, enteId);
		if(siacTAttoAmm != null){
			update.setSiacTAttoAmm(siacTAttoAmm);
		}
		
		//scrivo il nuovo motivo
		update.setSiacDModificaTipo(null);
		if(spesa.getTipoModificaMovimentoGestione()!=null){
			SiacDModificaTipoFin siacDModificaTipo = new SiacDModificaTipoFin();
			List<SiacDModificaTipoFin> siacDModificaTipoList = siacDModificaTipoRepository.findValidoByCode(enteId, datiOperazioniDto.getTs(), spesa.getTipoModificaMovimentoGestione());
			if(siacDModificaTipoList != null && !siacDModificaTipoList.isEmpty()){
				siacDModificaTipo = siacDModificaTipoList.get(0);
				update.setSiacDModificaTipo(siacDModificaTipo);
			}			
			
		}
				
		update.setModNum(update.getModNum());
		update.setModDesc(spesa.getDescrizioneModificaMovimentoGestione());
		//salvo sul db:
		update = siacTModificaRepository.saveAndFlush(update);
		
		//Salvo l'eventuale numero del sub della modifica per non perderlo dopo il map
		int numeroSub=-1;
		
		if(spesa instanceof ModificaMovimentoGestioneSpesa) {
			
			if(((ModificaMovimentoGestioneSpesa) spesa).getNumeroSubImpegno()!=null && ((ModificaMovimentoGestioneSpesa) spesa).getNumeroSubImpegno()!=0){
				numeroSub=((ModificaMovimentoGestioneSpesa) spesa).getNumeroSubImpegno();
			}
			
			spesa = map(update, ModificaMovimentoGestioneSpesa.class, FinMapId.SiacTModifica_ModificaMovimentoGestioneSpesa);	
			spesa = EntityToModelConverter.siacTModificaEntityToModificaMovimentoGestioneSpesaModel(update, ((ModificaMovimentoGestioneSpesa)spesa));
			
			if(numeroSub!=-1){
				((ModificaMovimentoGestioneSpesa) spesa).setNumeroSubImpegno(numeroSub);
			}
			
		}else if(spesa instanceof ModificaMovimentoGestioneEntrata) {
			
			if(((ModificaMovimentoGestioneEntrata) spesa).getNumeroSubAccertamento()!=null && ((ModificaMovimentoGestioneEntrata) spesa).getNumeroSubAccertamento()!=0){
				numeroSub=((ModificaMovimentoGestioneEntrata) spesa).getNumeroSubAccertamento();
			}
			
			spesa = map(update, ModificaMovimentoGestioneEntrata.class, FinMapId.SiacTModifica_ModificaMovimentoGestioneEntrata);	
			spesa = EntityToModelConverter.siacTModificaEntityToModificaMovimentoGestioneEntrataModel(update, ((ModificaMovimentoGestioneEntrata)spesa));
			
			if(numeroSub!=-1){
				((ModificaMovimentoGestioneEntrata) spesa).setNumeroSubAccertamento(numeroSub);
			}
		}
	
		SiacRModificaStatoFin siacRModificaStato = DatiOperazioneUtil.getValido(update.getSiacRModificaStatos(), getNow());
		if(siacRModificaStato!=null){
			SiacRMovgestTsSogModFin siacRMovgestTsSogMod = DatiOperazioneUtil.getValido(siacRModificaStato.getSiacRMovgestTsSogMods(),getNow());
			if(siacRMovgestTsSogMod!=null){
				spesa = impostaDatiModSogg(siacRMovgestTsSogMod, spesa, enteId);
			}	
			SiacRMovgestTsSogclasseModFin siacRMovgestTsSogClasseMod = DatiOperazioneUtil.getValido(siacRModificaStato.getSiacRMovgestTsSogclasseMods(),getNow());
			if(siacRMovgestTsSogClasseMod!=null){
				spesa = impostaDatiModSoggClass(siacRMovgestTsSogClasseMod, spesa, enteId);
			}	
		}
		
		//Termino restituendo l'oggetto di ritorno: 
        return spesa;
	}
	
	/**
	 * Metodo interno a inserisciModificaMovimentoSpesaSoggetto e updateModificaMovimentoSpesaSoggetto
	 * @param siacRMovgestTsSogMod
	 * @param spesa
	 * @param enteId
	 * @return
	 */
	private ModificaMovimentoGestione impostaDatiModSogg(SiacRMovgestTsSogModFin siacRMovgestTsSogMod, ModificaMovimentoGestione spesa,int enteId){
		if(siacRMovgestTsSogMod!=null){
			SiacTSoggettoFin siacTSoggettoOld =  siacRMovgestTsSogMod.getSiacTSoggetto1();
			SiacTSoggettoFin siacTSoggettoNew =  siacRMovgestTsSogMod.getSiacTSoggetto2();
			if(siacTSoggettoNew!=null){
				Soggetto soggettoNew = soggettoDad.ricercaSoggetto(CostantiFin.AMBITO_FIN, enteId, siacTSoggettoNew.getSoggettoCode(),true, true);
				if(null!=soggettoNew){
					spesa.setSoggettoNewMovimentoGestione(soggettoNew);
				}
			}
			if(siacTSoggettoOld!=null){
				Soggetto soggettoOld = soggettoDad.ricercaSoggetto(CostantiFin.AMBITO_FIN, enteId, siacTSoggettoOld.getSoggettoCode(),true, true);
				if(null!=soggettoOld){
					spesa.setSoggettoOldMovimentoGestione(soggettoOld);
				}
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return spesa;
	}
	
	/**
	 * Metodo interno a inserisciModificaMovimentoSpesaSoggetto e updateModificaMovimentoSpesaSoggetto
	 * @param siacRMovgestTsSogClasseMod
	 * @param spesa
	 * @param enteId
	 * @return
	 */
	private ModificaMovimentoGestione impostaDatiModSoggClass(SiacRMovgestTsSogclasseModFin siacRMovgestTsSogClasseMod,ModificaMovimentoGestione spesa,int enteId){
		if(siacRMovgestTsSogClasseMod!=null){
			if(siacRMovgestTsSogClasseMod.getSiacDSoggettoClasse1()!=null){
				ClasseSoggetto classeOld = buildSoggettoClasse(siacRMovgestTsSogClasseMod.getSiacDSoggettoClasse1());
				spesa.setClasseSoggettoOldMovimentoGestione(classeOld);
			}
			if(siacRMovgestTsSogClasseMod.getSiacDSoggettoClasse2()!=null){
				ClasseSoggetto classeNew = buildSoggettoClasse(siacRMovgestTsSogClasseMod.getSiacDSoggettoClasse2());
				spesa.setClasseSoggettoNewMovimentoGestione(classeNew);
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return spesa;
	}
	
	/**
	 * Si occupa di gestire l'inserimento di una modifica di movimento spesa importo
	 * @param modificaMovImporto
	 * @param uIdImpegno
	 * @param enteId
	 * @param datiOperazioneDto
	 * @param numeroModifica
	 * @return
	 */
	private ModificaMovimentoGestione inserisciModificaMovimentoImporto(ModificaMovimentoGestione modificaMovImporto , Integer uIdImpegno, Integer enteId ,DatiOperazioneDto datiOperazioneDto, Integer numeroModifica){
		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository.findOne(enteId);
		DatiOperazioneDto datiOperazioneInserimento = DatiOperazioneDto.buildDatiOperazione(datiOperazioneDto, Operazione.INSERIMENTO);
		List<SiacTMovgestTsFin> siacTMovgestTsList;
		if(modificaMovImporto!=null && modificaMovImporto.getTipoMovimento()!=null){
			if(!modificaMovImporto.getTipoMovimento().equalsIgnoreCase(CostantiFin.MODIFICA_TIPO_SIM) && !modificaMovImporto.getTipoMovimento().equalsIgnoreCase(CostantiFin.MODIFICA_TIPO_SAC) ){
				siacTMovgestTsList = siacTMovgestTsRepository.findMovgestTsByMovgest(enteId, datiOperazioneDto.getTs() , uIdImpegno);
			} else {
				siacTMovgestTsList = siacTMovgestTsRepository.findSubMovgestTsByMovgest(enteId, datiOperazioneDto.getTs() , uIdImpegno);
			}

			if(siacTMovgestTsList != null && !siacTMovgestTsList.isEmpty()){
				//La Dovra' aggiornare alla fine con le nuove informazioni
				SiacTMovgestTsFin siacTMovgestTs = siacTMovgestTsList.get(0);
				
				
				//variazione, impropriamente salvata dentro "importoOld":
				BigDecimal deltaModifica = modificaMovImporto.getImportoOld();
				
				//Gestione SiacTMovgestTsDetFin
				SiacDMovgestTsDetTipoFin siacDMovgestTsDetTipoAttuale = siacDMovgestTsDetTipoRepository.findValidoByCode(enteId, datiOperazioneDto.getTs(), CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE).get(0);
				SiacTMovgestTsDetFin siacTMovgestTsDet = siacTMovgestTsDetRepository.findValidoByTipo(enteId, datiOperazioneDto.getTs(), siacDMovgestTsDetTipoAttuale.getMovgestTsDetTipoCode(), siacTMovgestTs.getUid()).get(0);
				siacTMovgestTsDet = saveImporto(siacTMovgestTsDet, modificaMovImporto.getImportoNew(), datiOperazioneDto, siacTMovgestTs, CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE);
				
				SiacTMovgestTsDetFin detUtilzz = null;
				//Biosgna gestire anche l'importo utilizzabile:
				if(modificaMovImporto instanceof ModificaMovimentoGestioneEntrata){
					//siamo nel caso di accertamenti
					//solo se anno movimento >= dell'anno bilancio:
					int annoMovimento = siacTMovgestTs.getSiacTMovgest().getMovgestAnno();
					int annoBilancio = new Integer(siacTMovgestTs.getSiacTMovgest().getSiacTBil().getSiacTPeriodo().getAnno());
					if(annoMovimento>=annoBilancio){
						BigDecimal importoUtilizzabileOld = estraiImportoUtilizzabileByMovgestTsId(siacTMovgestTs.getMovgestTsId(), datiOperazioneDto);
						BigDecimal importoUtilizzabileNew = importoUtilizzabileOld.add(deltaModifica);
						detUtilzz = saveImporto(importoUtilizzabileNew, datiOperazioneDto, siacTMovgestTs, CostantiFin.MOVGEST_TS_DET_TIPO_UTILIZZABILE);
					}
				}
				
				//Gestione SiacTMovgestTsDetModFin
				SiacTMovgestTsDetModFin siacTMovgestTsDetMod = new SiacTMovgestTsDetModFin();
				siacTMovgestTsDetMod = DatiOperazioneUtil.impostaDatiOperazioneLogin(siacTMovgestTsDetMod, datiOperazioneInserimento, siacTAccountRepository);
						
				siacTMovgestTsDetMod.setMovgestTsDetImporto(deltaModifica);
				siacTMovgestTsDetMod.setSiacTMovgestTsDet(siacTMovgestTsDet);
				siacTMovgestTsDetMod.setSiacTMovgestT(siacTMovgestTs);
				siacTMovgestTsDetMod.setSiacDMovgestTsDetTipo(siacDMovgestTsDetTipoAttuale);
				siacTMovgestTsDetMod.setSiacTEnteProprietario(siacTEnteProprietario);
				
				//Preparo SiacDModificaStatoFin
				SiacDModificaStatoFin siacDModificaStato = siacDModificaStatoRepository.findByCode(enteId, datiOperazioneDto.getTs(), CostantiFin.D_MODIFICA_STATO_VALIDO).get(0);
			
				//Istanza SiacTModificaFin
				SiacTModificaFin siacTModifica = new SiacTModificaFin();
				siacTModifica = DatiOperazioneUtil.impostaDatiOperazioneLogin(siacTModifica, datiOperazioneInserimento, siacTAccountRepository);
				
				//Preparo SiacDModificaTipoFin    Impegno /Subimpegno
				if(modificaMovImporto!=null && modificaMovImporto.getTipoModificaMovimentoGestione()!=null && modificaMovImporto.getTipoModificaMovimentoGestione().toString()!=null && !StringUtilsFin.isEmpty(modificaMovImporto.getTipoModificaMovimentoGestione())){
					SiacDModificaTipoFin siacDModificaTipo = siacDModificaTipoRepository.findValidoByCode(enteId, datiOperazioneDto.getTs(), modificaMovImporto.getTipoModificaMovimentoGestione().toString()).get(0);			
					modificaMovImporto.setTipoMovimentoDesc(siacDModificaTipo.getModTipoDesc());			
					siacTModifica.setSiacDModificaTipo(siacDModificaTipo);
				}
				
				siacTModifica.setModDesc(modificaMovImporto.getDescrizione());
				siacTModifica.setModData(datiOperazioneDto.getTs());
				
				
				if((modificaMovImporto.getAttoAmministrativoAnno() != null && Integer.valueOf(modificaMovImporto.getAttoAmministrativoAnno()) != 0) && (modificaMovImporto.getAttoAmministrativoNumero() != null && modificaMovImporto.getAttoAmministrativoNumero() != 0)){
					AttoAmministrativo attoAmm = buildAttoAmministrativo(enteId, modificaMovImporto.getAttoAmministrativoNumero(), modificaMovImporto.getAttoAmministrativoTipoCode(), Integer.valueOf(modificaMovImporto.getAttoAmministrativoAnno()), modificaMovImporto.getIdStrutturaAmministrativa());
					SiacTAttoAmmFin siacTAttoAmm = getSiacTAttoAmmFromAttoAmministrativo(attoAmm, enteId);
					if(siacTAttoAmm != null){
						siacTModifica.setSiacTAttoAmm(siacTAttoAmm);
						AttoAmministrativo atto = new AttoAmministrativo();
						atto.setUid(siacTAttoAmm.getUid());
						modificaMovImporto.setAttoAmministrativo(atto);
					}
				}
				
				//Per settare il numero della modifica per ora eseguo una count sul numero di modifiche relative allo specifico movgest_ts in attesa di maggior informazioni
				//count modifiche + 1
				siacTModifica.setModNum(Integer.valueOf(numeroModifica));
				//salvo sul db:
				siacTModifica = siacTModificaRepository.saveAndFlush(siacTModifica);
				
				//Gestisco SiacRModificaStatoFin
				SiacRModificaStatoFin siacRModificaStato = new SiacRModificaStatoFin();
				siacRModificaStato = DatiOperazioneUtil.impostaDatiOperazioneLogin(siacRModificaStato, datiOperazioneInserimento, siacTAccountRepository);
				
				siacRModificaStato.setSiacDModificaStato(siacDModificaStato);
				siacRModificaStato.setSiacTEnteProprietario(siacTEnteProprietario);
				siacRModificaStato.setSiacTModifica(siacTModifica);
				//salvo sul db:
				siacRModificaStato = siacRModificaStatoRepository.saveAndFlush(siacRModificaStato);
				
				//SAlvo SiacTMovgestTsDetModFin
				siacTMovgestTsDetMod.setSiacRModificaStato(siacRModificaStato);
				//salvo sul db:
				siacTMovgestTsDetMod = siacTMovgestTsDetModRepository.saveAndFlush(siacTMovgestTsDetMod);
			
				siacRModificaStato.setSiacTMovgestTsDetMods(toList(siacTMovgestTsDetMod));
				siacTModifica.setSiacRModificaStatos(toList(siacRModificaStato));
				
				modificaMovImporto.setUid(siacTModifica.getUid());
				modificaMovImporto.setNumeroModificaMovimentoGestione(siacTModifica.getModNum());
				modificaMovImporto.setDescrizioneModificaMovimentoGestione(siacTModifica.getModDesc());
				modificaMovImporto.setStatoOperativoModificaMovimentoGestione(StatoOperativoModificaMovimentoGestione.VALIDO);
				
				modificaMovImporto.setDataCreazione(siacTModifica.getDataCreazione());
				modificaMovImporto.setUtenteCreazione(siacTModifica.getLoginOperazione());
				
				//salvo il tipo modifica per evitare di perderlo dopo il map
				String tipoMod = modificaMovImporto.getTipoMovimento();		
				
				if(modificaMovImporto instanceof ModificaMovimentoGestioneEntrata ){
					
					modificaMovImporto = map(siacTModifica, ModificaMovimentoGestioneEntrata.class, FinMapId.SiacTModifica_ModificaMovimentoGestioneEntrata);	
					modificaMovImporto = EntityToModelConverter.siacTModificaEntityToModificaMovimentoGestioneEntrataModel(siacTModifica, ((ModificaMovimentoGestioneEntrata)modificaMovImporto));
					
				}else if(modificaMovImporto instanceof ModificaMovimentoGestioneSpesa){
					
					modificaMovImporto = map(siacTModifica, ModificaMovimentoGestioneSpesa.class, FinMapId.SiacTModifica_ModificaMovimentoGestioneSpesa);	
					modificaMovImporto = EntityToModelConverter.siacTModificaEntityToModificaMovimentoGestioneSpesaModel(siacTModifica, ((ModificaMovimentoGestioneSpesa)modificaMovImporto));
				}
				
				modificaMovImporto.setTipoMovimento(tipoMod);
				
				if(modificaMovImporto.getTipoMovimento().equalsIgnoreCase(CostantiFin.MODIFICA_TIPO_SIM)){
					((ModificaMovimentoGestioneSpesa)modificaMovImporto).setUidSubImpegno(siacTMovgestTs.getSiacTMovgest().getUid());
				} else if(modificaMovImporto.getTipoMovimento().equalsIgnoreCase(CostantiFin.MODIFICA_TIPO_IMP)){
					((ModificaMovimentoGestioneSpesa)modificaMovImporto).setUidImpegno(siacTMovgestTs.getSiacTMovgest().getUid());
				} else if(modificaMovImporto.getTipoMovimento().equalsIgnoreCase(CostantiFin.MODIFICA_TIPO_ACC)){
					((ModificaMovimentoGestioneEntrata)modificaMovImporto).setUidAccertamento(siacTMovgestTs.getSiacTMovgest().getUid());
				} else if(modificaMovImporto.getTipoMovimento().equalsIgnoreCase(CostantiFin.MODIFICA_TIPO_SAC)){
					((ModificaMovimentoGestioneEntrata)modificaMovImporto).setUidSubAccertamento(siacTMovgestTs.getSiacTMovgest().getUid());
				}
				
			}		
		
		}
		
		//Termino restituendo l'oggetto di ritorno: 
        return modificaMovImporto;
	}
	
	/**
	 * Si occupa di gestire l'inserimento di una modifica di movimento spesa soggetto
	 * @param spesa
	 * @param uIdImpegno
	 * @param enteId
	 * @param datiOperazioneDto
	 * @param numeroModifiche
	 * @return
	 */
	private  ModificaMovimentoGestione inserisciModificaMovimentoSpesaSoggetto(ModificaMovimentoGestione spesa, Integer uIdImpegno, Integer enteId ,DatiOperazioneDto datiOperazioneDto, Integer numeroModifiche){
		ModificaMovimentoGestione spesaNew = spesa;
		
		
		//INTRODOTTE QUESTE VARIABILI PER CERCARE DI RENDERE PIU' LEGGILE IL METODO:
		boolean isEmptySoggettoOld = isSoggettoVuoto(spesa.getSoggettoOldMovimentoGestione());
		boolean isEmptySoggettoNew = isSoggettoVuoto(spesa.getSoggettoNewMovimentoGestione());
		boolean isEmptyClasseOld = isClasseSoggettoVuoto(spesa.getClasseSoggettoOldMovimentoGestione());
		boolean isEmptyClasseNew = isClasseSoggettoVuoto(spesa.getClasseSoggettoNewMovimentoGestione());
		//
		
		
//		System.out.print("inserisciModificaMovimentoSpesaSoggetto parametri INPUT ");
//		System.out.print("uIdImpegno:  " + uIdImpegno);
//		System.out.print("numeroModifiche: " + numeroModifiche);
		
		//Inizializzo parametri per classe soggetto
		SiacRMovgestTsSogclasseFin siacRMovgestTsSogClasse = new SiacRMovgestTsSogclasseFin();
		SiacRMovgestTsSogclasseModFin siacRMovgestTsSogClasseMod = new SiacRMovgestTsSogclasseModFin();
		SiacRMovgestTsSogclasseFin siacRMovgestTsSogClasseOld = new SiacRMovgestTsSogclasseFin();		
		String codeSoggClass = "";
		
		//Inizializzo parametri per soggetto
		Integer idAmbito = datiOperazioneDto.getSiacDAmbito().getAmbitoId();;
		String codeSogg = "";
		SiacRMovgestTsSogFin siacRMovgestTsSog = new SiacRMovgestTsSogFin();
		SiacRMovgestTsSogFin siacRMovgestTsSogOld = new SiacRMovgestTsSogFin();
		SiacRMovgestTsSogModFin siacRMovgestTsSogMod = new SiacRMovgestTsSogModFin();
		
		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository.findOne(enteId);
		DatiOperazioneDto datiOperazioneInserimento = DatiOperazioneDto.buildDatiOperazione(datiOperazioneDto, Operazione.INSERIMENTO);

		List<SiacTMovgestTsFin> siacTMovgestTsList; 
		
		// leggo il movimento gestione ts da db
		if(!spesa.getTipoMovimento().equalsIgnoreCase(CostantiFin.MODIFICA_TIPO_SIM) && !spesa.getTipoMovimento().equalsIgnoreCase(CostantiFin.MODIFICA_TIPO_SAC)){
			siacTMovgestTsList = siacTMovgestTsRepository.findMovgestTsByMovgest(enteId, datiOperazioneDto.getTs() , uIdImpegno);
		} else {
			siacTMovgestTsList = siacTMovgestTsRepository.findSubMovgestTsByMovgest(enteId, datiOperazioneDto.getTs() , uIdImpegno);
		}
		
		
		if(siacTMovgestTsList != null && !siacTMovgestTsList.isEmpty()){
			
			SiacTMovgestTsFin siacTMovgestTs = siacTMovgestTsList.get(0);
			
			//prepare un soggettoOld copia nel caso di una modifica di S->C per la siacRmovgestSog (find Ultimoinvalidato perche l'aggiorna impegno cancella la relaz con il vecchio soggetto)
			List<SiacRMovgestTsSogFin> listaSogOld = siacRMovgestTsSogRepository.findUltimoInvalidato(enteId, datiOperazioneDto.getTs(), siacTMovgestTs.getMovgestTsId());
			if(listaSogOld!=null && listaSogOld.size()>0){
				log.debug("","Sto modificando un soggetto in classe soggetto ");
				siacRMovgestTsSogOld = listaSogOld.get(0); 
			}
			
			// prepare una classeOld copia nel caso di una modifica di C->S per la siacRmovgestSog (find Ultimoinvalidato perche l'aggiorna impegno cancella la relaz con la vecchia classe)
			// ricerco l'ultimo invalidato
			List<SiacRMovgestTsSogclasseFin> listaClasseOld = siacRMovgestTsSogClasseRepository.findUltimoInvalidato(enteId, siacTMovgestTs.getMovgestTsId(), datiOperazioneDto.getTs());
			if(listaClasseOld!=null && listaClasseOld.size()>0){
				log.debug(""," ricerco l'ultimo invalidato ----------- Ho trovato un record precedentemente invalidato! ");
				siacRMovgestTsSogClasseOld = listaClasseOld.get(0); 
				log.debug(""," classe soggetto prec invalidata:  " + siacRMovgestTsSogClasseOld.getMovgestTsSogclasseId());
			}
			
			//Gestione SiacRMovgestTsSogFin
			if(spesa.getSoggettoNewMovimentoGestione()!=null || spesa.getSoggettoOldMovimentoGestione()!=null){
				
				List<SiacRMovgestTsSogFin> lista = siacRMovgestTsSogRepository.findValidoMovGestTsSogByIdMovGestAndEnte(enteId, datiOperazioneDto.getTs(), siacTMovgestTs.getMovgestTsId());
				
				if(lista!=null && lista.size()>0){
					// QUI NON DOVREBBE TROVARE NULLA PERCHE' IL SOGGETTO NON è ANCORA STATO INSERITO 
					siacRMovgestTsSog = lista.get(0); 
				}
				
				//Gestione Modifica: Sogg -> Sogg  +  null -> Sogg (inserimento/sostituzione nuovo soggetto)
				
				
				//QUESTO if(spesa.getSoggettoNewMovimentoGestione()!=null && !StringUtils.isEmpty(spesa.getSoggettoNewMovimentoGestione().getCodiceSoggetto())){
				//VIENE RISCRITTO PIU' LEGGIBILE:
				if(!isEmptySoggettoNew){
					
					// ........................ entra qui
					log.debug(""," ------  entra qui XCHè DEVO INSERIRE IL SOGGETTO --------");
					log.debug(""," DA INSERIRE CodiceSoggetto : " + spesa.getSoggettoNewMovimentoGestione().getCodiceSoggetto());
					
					siacRMovgestTsSog = DatiOperazioneUtil.impostaDatiOperazioneLogin(siacRMovgestTsSog, datiOperazioneInserimento, siacTAccountRepository);	//1 dati login			
					siacRMovgestTsSog.setDataCreazione(new Date());	//2data creazione
					siacRMovgestTsSog.setSiacTMovgestT(siacTMovgestTs); //3 id movgest
					siacRMovgestTsSog.setDataInizioValidita(new Date()); //2 bis data inizio validita'
					siacRMovgestTsSog.setSiacTEnteProprietario(siacTEnteProprietario); //1 bis ente
			
					SiacTSoggettoFin siacTSoggetto = null;
					codeSogg = spesa.getSoggettoNewMovimentoGestione().getCodiceSoggetto();
					List<SiacTSoggettoFin> l = siacTSoggettoRepository.findSoggettoByCodeAndAmbitoAndEnte(codeSogg, enteId, datiOperazioneDto.getTs(), idAmbito, CostantiFin.SEDE_SECONDARIA);
					if(l!=null && l.size()>0){
						siacTSoggetto = l.get(0);
						siacRMovgestTsSog.setSiacTSoggetto(siacTSoggetto);	
					}else{
						return spesaNew;
					}
					//salvo sul db:
					siacRMovgestTsSog = siacRMovgestTsSogRepository.saveAndFlush(siacRMovgestTsSog);
					log.debug(""," ------ ho inserito il soggetto --------");
				}
				//Gestione Modifica: Sogg -> Null (eliminazione soggetto)
				else
				{
					
					if(siacRMovgestTsSog.getDataCreazione()!=null){
						DatiOperazioneUtil.cancellaRecord(siacRMovgestTsSog, siacRMovgestTsSogRepository, datiOperazioneDto, siacTAccountRepository);
					}	
				}
			}
		
			//Gestione SiacRMovgestTsSogclasseFin
			boolean condizione = (spesa.getClasseSoggettoNewMovimentoGestione() !=null || spesa.getClasseSoggettoOldMovimentoGestione()!=null)	//correzione anomalia 544 (Nessun associazione all'impegno della classe a cui appartiene un soggetto non nullo)
					&& isEmptySoggettoNew ;
			
			if(condizione){
				
				
				log.debug(""," ------ Blocco gestione classe [SiacRMovgestTsSogclasseFin]--------");
				List<SiacRMovgestTsSogclasseFin> lista = siacRMovgestTsSogClasseRepository.findValidoMovGestTsSogClasseByIdMovGestAndEnte(enteId, siacTMovgestTs.getMovgestTsId(), datiOperazioneDto.getTs());
				if(lista!=null && lista.size()>0){
					siacRMovgestTsSogClasse = lista.get(0); 
				}
				
				//Gestione Modifica: Classe -> Classe +  null -> Classe  
				
				// QUESTO if(spesa.getClasseSoggettoNewMovimentoGestione()!=null && !StringUtils.isEmpty(spesa.getClasseSoggettoNewMovimentoGestione().getCodice())){
				//VIENE RISCRITTO PIU' LEGGIBILE:
				if(!isEmptyClasseNew){
				
					siacRMovgestTsSogClasse = DatiOperazioneUtil.impostaDatiOperazioneLogin(siacRMovgestTsSogClasse, datiOperazioneInserimento, siacTAccountRepository);
					siacRMovgestTsSogClasse.setDataCreazione(new Date());
					siacRMovgestTsSogClasse.setSiacTMovgestT(siacTMovgestTs); 
					siacRMovgestTsSogClasse.setDataInizioValidita(new Date()); 
					siacRMovgestTsSogClasse.setSiacTEnteProprietario(siacTEnteProprietario); 
			
					SiacDSoggettoClasseFin siacDSoggettoClasse = null;
					
					if(StringUtilsFin.contieneSoloNumeri(spesa.getClasseSoggettoNewMovimentoGestione().getCodice())){
						codeSoggClass = spesa.getClasseSoggettoNewMovimentoGestione().getCodice();
					} else {
						List<SiacDSoggettoClasseFin> listClass = siacDSoggettoClasseRepository.findValidoByCode(enteId, datiOperazioneDto.getSiacDAmbito().getAmbitoId(), spesa.getClasseSoggettoNewMovimentoGestione().getCodice(), datiOperazioneDto.getTs());
						codeSoggClass = listClass.get(0).getUid().toString();
					}
					
					if(codeSoggClass!=null){
						SiacDSoggettoClasseFin lc = siacDSoggettoClasseRepository.findOne(Integer.valueOf(codeSoggClass));
						if(lc!=null){
							siacDSoggettoClasse = lc;
							siacRMovgestTsSogClasse.setSiacDSoggettoClasse(siacDSoggettoClasse);
						}
						//salvo sul db:
						siacRMovgestTsSogClasse = siacRMovgestTsSogClasseRepository.saveAndFlush(siacRMovgestTsSogClasse);
					}	
				}
				//Gestione Modifica: Class -> Null
				else {
					log.debug("","Gestione Modifica: Class -> Null cancellaRecord ....");
					if(siacRMovgestTsSogClasse.getDataCreazione()!=null){
						DatiOperazioneUtil.cancellaRecord(siacRMovgestTsSogClasse, siacRMovgestTsSogClasseRepository, datiOperazioneDto, siacTAccountRepository); //verificare
					}	
				}
			} else {
				
				if(isEmptySoggettoOld && isEmptyClasseNew && !isEmptySoggettoNew && !isEmptyClasseOld){
					//FIX PER JIRA SIAC-2734, non veniva invalidata la classe soggetto in caso di modifica da CLASSE A SOGGETTO:
					List<SiacRMovgestTsSogclasseFin> lista = siacRMovgestTsSogClasseRepository.findValidoMovGestTsSogClasseByIdMovGestAndEnte(enteId, siacTMovgestTs.getMovgestTsId(), datiOperazioneDto.getTs());
					if(lista!=null && lista.size()>0){
						siacRMovgestTsSogClasse = lista.get(0); 
					}
					if(siacRMovgestTsSogClasse.getDataCreazione()!=null){
						DatiOperazioneUtil.cancellaRecord(siacRMovgestTsSogClasse, siacRMovgestTsSogClasseRepository, datiOperazioneDto, siacTAccountRepository); //verificare
					}
				}
				
			}

			
			// ***********************************************
			//
			// 		GESTIONE SiacRMovgestTsSogModFin
			//
			// ***********************************************
			if(spesa.getSoggettoNewMovimentoGestione()!=null && !StringUtilsFin.isEmpty(spesa.getSoggettoNewMovimentoGestione().getCodiceSoggetto())){
			
				siacRMovgestTsSogMod = DatiOperazioneUtil.impostaDatiOperazioneLogin(siacRMovgestTsSogMod, datiOperazioneInserimento, siacTAccountRepository);			
				siacRMovgestTsSogMod.setDataCreazione(new Date());
				siacRMovgestTsSogMod.setSiacTMovgestT(siacTMovgestTs);
				siacRMovgestTsSogMod.setDataInizioValidita(new Date());
			
				SiacTSoggettoFin siacTSoggettoNew = null;
				
				List<SiacTSoggettoFin> ln = siacTSoggettoRepository.findSoggettoByCodeAndAmbitoAndEnte(codeSogg, enteId, datiOperazioneDto.getTs(), idAmbito, CostantiFin.SEDE_SECONDARIA);				
					if(ln!=null && ln.size()>0){
						siacTSoggettoNew = ln.get(0);
						siacRMovgestTsSogMod.setSiacTSoggetto2(siacTSoggettoNew);
					}else{return spesaNew;}
			
				SiacTSoggettoFin siacTSoggettoOld = null;
				//Condizione modifica sostituzione vecchio soggetto con nuovo soggetto
				if(spesa.getSoggettoOldMovimentoGestione()!=null && !StringUtilsFin.isEmpty(spesa.getSoggettoOldMovimentoGestione().getCodiceSoggetto())){
				String codeSoggOld = spesa.getSoggettoOldMovimentoGestione().getCodiceSoggetto();
					List<SiacTSoggettoFin> lo = siacTSoggettoRepository.findSoggettoByCodeAndAmbitoAndEnte(codeSoggOld, enteId, datiOperazioneDto.getTs(), idAmbito, CostantiFin.SEDE_SECONDARIA);
				if(lo!=null && lo.size()>0){
					siacTSoggettoOld = lo.get(0);
					siacRMovgestTsSogMod.setSiacTSoggetto1(siacTSoggettoOld); 
					}else{return spesaNew;}
				} else {
					//Condizione modifica inserimento nuovo soggetto
					siacTSoggettoOld = siacTSoggettoNew;
					siacRMovgestTsSogMod.setSiacTSoggetto1(siacTSoggettoOld); 
				}	
			
				siacRMovgestTsSogMod.setSiacRMovgestTsSog(siacRMovgestTsSog); 
				siacRMovgestTsSogMod.setSiacTEnteProprietario(siacTEnteProprietario);
			} 
			//Condizione modifica sostituzione vecchio soggetto con niente 
			else if(spesa.getSoggettoOldMovimentoGestione()!=null && !StringUtilsFin.isEmpty(spesa.getSoggettoOldMovimentoGestione().getCodiceSoggetto())){
				
				//creo una nuova modifica con il solo old
				siacRMovgestTsSogMod = DatiOperazioneUtil.impostaDatiOperazioneLogin(siacRMovgestTsSogMod, datiOperazioneInserimento, siacTAccountRepository);			
				siacRMovgestTsSogMod.setDataCreazione(new Date());
				siacRMovgestTsSogMod.setSiacTMovgestT(siacTMovgestTs);
				siacRMovgestTsSogMod.setDataInizioValidita(new Date());
			
				SiacTSoggettoFin siacTSoggettoOld = null;
				
				List<SiacTSoggettoFin> ln = siacTSoggettoRepository.findSoggettoByCodeAndAmbitoAndEnte(spesa.getSoggettoOldMovimentoGestione().getCodiceSoggetto(), enteId, datiOperazioneDto.getTs(), idAmbito, CostantiFin.SEDE_SECONDARIA);				
				if(ln!=null && ln.size()>0){
					siacTSoggettoOld = ln.get(0);
					siacRMovgestTsSogMod.setSiacTSoggetto1(siacTSoggettoOld);
				}else{return spesaNew;}
				
				siacRMovgestTsSogMod.setSiacTSoggetto2(null);
				
				//gestione relazione soggetto annullata sulla modifica accessoria per coerenza con db
				if(siacRMovgestTsSog == null || siacRMovgestTsSog.getMovgestTsSogId()==null){
					siacRMovgestTsSogMod.setSiacRMovgestTsSog(siacRMovgestTsSogOld);
				}
				else{
					siacRMovgestTsSogMod.setSiacRMovgestTsSog(siacRMovgestTsSog);
				}	
				siacRMovgestTsSogMod.setSiacTEnteProprietario(siacTEnteProprietario);			
			}
			
			
			// ***********************************************
			// 		GESTIONE SiacRMovgestTsClasseMod 
			// ***********************************************
			if((spesa.getClasseSoggettoNewMovimentoGestione()!=null && !StringUtilsFin.isEmpty(spesa.getClasseSoggettoNewMovimentoGestione().getCodice()))	//correzione anomalia 544 (Nessun associazione all'impegno della classe a cui appartiene un soggetto non nullo)
					&& (spesa.getSoggettoNewMovimentoGestione()==null || StringUtilsFin.isEmpty(spesa.getSoggettoNewMovimentoGestione().getCodiceSoggetto()))){
			
				siacRMovgestTsSogClasseMod = DatiOperazioneUtil.impostaDatiOperazioneLogin(siacRMovgestTsSogClasseMod, datiOperazioneInserimento, siacTAccountRepository); 			
				siacRMovgestTsSogClasseMod.setDataCreazione(new Date());	
				siacRMovgestTsSogClasseMod.setSiacTMovgestT(siacTMovgestTs); 
				siacRMovgestTsSogClasseMod.setDataInizioValidita(new Date());
			
				SiacDSoggettoClasseFin siacDSoggettoClasseNew = null;
				
				if(StringUtilsFin.contieneSoloNumeri(spesa.getClasseSoggettoNewMovimentoGestione().getCodice())){
					codeSoggClass = spesa.getClasseSoggettoNewMovimentoGestione().getCodice();
				} 
				else 
				{
					List<SiacDSoggettoClasseFin> listClass = siacDSoggettoClasseRepository.findValidoByCode(enteId, datiOperazioneDto.getSiacDAmbito().getAmbitoId(), spesa.getClasseSoggettoNewMovimentoGestione().getCodice(), datiOperazioneDto.getTs());
					codeSoggClass = listClass.get(0).getUid().toString();
				}
								
				SiacDSoggettoClasseFin lcn = siacDSoggettoClasseRepository.findOne(Integer.valueOf(codeSoggClass));
				if(lcn!=null){
						siacDSoggettoClasseNew = lcn;
						siacRMovgestTsSogClasseMod.setSiacDSoggettoClasse2(siacDSoggettoClasseNew); 
				}else{
					return spesaNew;
				}
				
				SiacDSoggettoClasseFin siacDSoggettoClasseOld = null;
				//Condizione modifica sostituzione vecchia classe con nuova classe
				if(spesa.getClasseSoggettoOldMovimentoGestione()!=null && !StringUtilsFin.isEmpty(spesa.getClasseSoggettoOldMovimentoGestione().getCodice())){
					
					String codeSoggClassOld = spesa.getClasseSoggettoOldMovimentoGestione().getCodice();
					List<SiacDSoggettoClasseFin> lco = siacDSoggettoClasseRepository.findByCodeAndAmbitoAndEnte(codeSoggClassOld, enteId, datiOperazioneDto.getTs(), idAmbito);
					if(lco!=null && lco.size()>0){
						siacDSoggettoClasseOld = lco.get(0);
						siacRMovgestTsSogClasseMod.setSiacDSoggettoClasse1(siacDSoggettoClasseOld); 
					}
					else
					{
						return spesaNew;
					}
				} else {
					//Condizione modifica inserimento nuovo soggetto
					siacDSoggettoClasseOld = siacDSoggettoClasseNew; 
					siacRMovgestTsSogClasseMod.setSiacDSoggettoClasse1(siacDSoggettoClasseOld);
				}	
			
				siacRMovgestTsSogClasseMod.setSiacRMovgestTsSogclasse(siacRMovgestTsSogClasse); 
				siacRMovgestTsSogClasseMod.setSiacTEnteProprietario(siacTEnteProprietario);
			}
			//Condizione modifica sostituzione vecchia classe con niente 
			else if(spesa.getClasseSoggettoOldMovimentoGestione()!=null && !StringUtilsFin.isEmpty(spesa.getClasseSoggettoOldMovimentoGestione().getCodice())){

				//creo nuova modifica solo la vecchia classe
				siacRMovgestTsSogClasseMod = DatiOperazioneUtil.impostaDatiOperazioneLogin(siacRMovgestTsSogClasseMod, datiOperazioneInserimento, siacTAccountRepository); 			
				siacRMovgestTsSogClasseMod.setDataCreazione(new Date());	
				siacRMovgestTsSogClasseMod.setSiacTMovgestT(siacTMovgestTs); 
				siacRMovgestTsSogClasseMod.setDataInizioValidita(new Date());
			
				SiacDSoggettoClasseFin siacDSoggettoClasseOld = null;
				
				String codeSoggClassOld = spesa.getClasseSoggettoOldMovimentoGestione().getCodice();
				List<SiacDSoggettoClasseFin> lco = siacDSoggettoClasseRepository.findByCodeAndAmbitoAndEnte(codeSoggClassOld, enteId, datiOperazioneDto.getTs(), idAmbito);
				if(lco!=null && lco.size()>0){
					siacDSoggettoClasseOld = lco.get(0);
					siacRMovgestTsSogClasseMod.setSiacDSoggettoClasse1(siacDSoggettoClasseOld); 
				}else{return spesaNew;}
				
				siacRMovgestTsSogClasseMod.setSiacDSoggettoClasse2(null);
				
				// jira 2121, siacRMovgestTsSogClasse.getMovgestTsSogclasseId() è null mentre c'è, nelle righe su si carica solo se ci sono righe precedenti
				// in questo caso non c'è la riga precedente sul db (ci sarebbe in caso di modifica classe --> classe), xchè sto modificando da Class a null
				List<SiacRMovgestTsSogclasseFin> lista = siacRMovgestTsSogClasseRepository.findValidoMovGestTsSogClasseByIdMovGestAndEnte(enteId, siacTMovgestTs.getMovgestTsId(), datiOperazioneDto.getTs());
				if(lista!=null && lista.size()>0){
					siacRMovgestTsSogClasse = lista.get(0); 
				}
				
				//gestione relazione classe annullata sulla modifica accessoria per coerenza con db
				if(siacRMovgestTsSogClasse==null || siacRMovgestTsSogClasse.getMovgestTsSogclasseId()!=null){
					siacRMovgestTsSogClasseMod.setSiacRMovgestTsSogclasse(siacRMovgestTsSogClasse); 
				}
				else
				{
					siacRMovgestTsSogClasseMod.setSiacRMovgestTsSogclasse(siacRMovgestTsSogClasseOld);
				}
			
				siacRMovgestTsSogClasseMod.setSiacTEnteProprietario(siacTEnteProprietario);
			}
			
			//Preparo SiacDModificaStatoFin
			SiacDModificaStatoFin siacDModificaStato = siacDModificaStatoRepository.findByCode(enteId, datiOperazioneDto.getTs(), "V").get(0);
		
			//Istanzo SiacTModificaFin
			SiacTModificaFin siacTModifica = new SiacTModificaFin();
			siacTModifica = DatiOperazioneUtil.impostaDatiOperazioneLogin(siacTModifica, datiOperazioneInserimento, siacTAccountRepository);
			
			//Preparo SiacDModificaTipoFin    Impegno /Subimpegno
			if(spesa.getTipoModificaMovimentoGestione().toString()!=null && !StringUtilsFin.isEmpty(spesa.getTipoModificaMovimentoGestione())){
			SiacDModificaTipoFin siacDModificaTipo = siacDModificaTipoRepository.findValidoByCode(enteId, datiOperazioneDto.getTs(), spesa.getTipoModificaMovimentoGestione().toString()).get(0);
				spesa.setTipoMovimentoDesc(siacDModificaTipo.getModTipoDesc());
				siacTModifica.setSiacDModificaTipo(siacDModificaTipo);				
			}
			
			siacTModifica.setModDesc(spesa.getDescrizione());
			siacTModifica.setModData(datiOperazioneDto.getTs());
			
			if((spesa.getAttoAmministrativoAnno() != null && Integer.valueOf(spesa.getAttoAmministrativoAnno()) != 0) && (spesa.getAttoAmministrativoNumero() != null && spesa.getAttoAmministrativoNumero() != 0)){
				AttoAmministrativo attoAmm = buildAttoAmministrativo(enteId, spesa.getAttoAmministrativoNumero(), spesa.getAttoAmministrativoTipoCode(), Integer.valueOf(spesa.getAttoAmministrativoAnno()), spesa.getIdStrutturaAmministrativa());
				SiacTAttoAmmFin siacTAttoAmm = getSiacTAttoAmmFromAttoAmministrativo(attoAmm, enteId);
				if(siacTAttoAmm != null){
					siacTModifica.setSiacTAttoAmm(siacTAttoAmm);
					AttoAmministrativo atto = new AttoAmministrativo();
					atto.setUid(siacTAttoAmm.getUid());
					spesa.setAttoAmministrativo(atto);
				}
			}
			
			//Per settare il numero della modifica per ora eseguo una count sul numero di modifiche relative allo specifico movgest_ts in attesa di maggior informazioni
			
			siacTModifica.setModNum(Integer.valueOf(numeroModifiche));
			//salvo sul db:
			siacTModifica = siacTModificaRepository.saveAndFlush(siacTModifica);
			
			//Gestisco SiacRModificaStatoFin
			SiacRModificaStatoFin siacRModificaStato = new SiacRModificaStatoFin();
			siacRModificaStato = DatiOperazioneUtil.impostaDatiOperazioneLogin(siacRModificaStato, datiOperazioneInserimento, siacTAccountRepository);
			
			siacRModificaStato.setSiacDModificaStato(siacDModificaStato);
			siacRModificaStato.setSiacTEnteProprietario(siacTEnteProprietario);
			siacRModificaStato.setSiacTModifica(siacTModifica);
			//salvo sul db:
			siacRModificaStato = siacRModificaStatoRepository.saveAndFlush(siacRModificaStato);
			
			
			//Salvo SiacRMovgestTsSogModFin
			if((spesa.getSoggettoOldMovimentoGestione()!=null && !StringUtilsFin.isEmpty(spesa.getSoggettoOldMovimentoGestione().getCodiceSoggetto()))
					|| spesa.getSoggettoNewMovimentoGestione()!=null && !StringUtilsFin.isEmpty(spesa.getSoggettoNewMovimentoGestione().getCodiceSoggetto())){
				siacRMovgestTsSogMod.setSiacRModificaStato(siacRModificaStato); 
				// stato
				//salvo sul db:
				siacRMovgestTsSogMod = siacRMovgestTsSogModRepository.saveAndFlush(siacRMovgestTsSogMod);
			}	
			
			//Salvo SiacRMovgestTsSogClasseMod	(salvataggio caso classe old -> classe new && null-> classe new)
			if((spesa.getClasseSoggettoOldMovimentoGestione()!=null && !StringUtilsFin.isEmpty(spesa.getClasseSoggettoOldMovimentoGestione().getCodice()))
				||	(spesa.getClasseSoggettoNewMovimentoGestione()!=null && !StringUtilsFin.isEmpty(spesa.getClasseSoggettoNewMovimentoGestione().getCodice()))){								
				siacRMovgestTsSogClasseMod.setSiacRModificaStato(siacRModificaStato);
				
//				System.out.println("siacRModificaStato: " + siacRModificaStato.getModStatoRId());
//				System.out.println("SiacRMovgestTsSogclasse.uid : " + siacRMovgestTsSogClasseMod.getSiacRMovgestTsSogclasse().getUid());
//				System.out.println("SiacDSoggettoClasse1 - old : " + siacRMovgestTsSogClasseMod.getSiacDSoggettoClasse1());
//				System.out.println("SiacDSoggettoClasse2 - new : " + siacRMovgestTsSogClasseMod.getSiacDSoggettoClasse2());
				// stato
				//salvo sul db:
				siacRMovgestTsSogClasseMod = siacRMovgestTsSogClasseModRepository.saveAndFlush(siacRMovgestTsSogClasseMod);				
			}
			
			siacRModificaStato.setSiacRMovgestTsSogMods(toList(siacRMovgestTsSogMod));
			siacRModificaStato.setSiacRMovgestTsSogclasseMods(toList(siacRMovgestTsSogClasseMod));
			siacTModifica.setSiacRModificaStatos(toList(siacRModificaStato));
			
			spesaNew.setUid(siacTModifica.getUid());
			spesaNew.setIdModificaMovimentoGestione(siacTModifica.getUid());
			spesaNew.setNumeroModificaMovimentoGestione(siacTModifica.getModNum());
			spesaNew.setDescrizioneModificaMovimentoGestione(siacTModifica.getModDesc());
			spesaNew.setStatoOperativoModificaMovimentoGestione(StatoOperativoModificaMovimentoGestione.VALIDO);			
			
			spesaNew.setDataCreazione(siacTModifica.getDataCreazione());
			spesaNew.setUtenteCreazione(siacTModifica.getLoginOperazione());
			
			//salvo il tipo modifica per evitare di perderlo dopo il map
			String tipoMod=spesa.getTipoMovimento();
			
			if(spesa instanceof ModificaMovimentoGestioneSpesa) {
				
				spesaNew = map(siacTModifica, ModificaMovimentoGestioneSpesa.class, FinMapId.SiacTModifica_ModificaMovimentoGestioneSpesa);	
				spesaNew = EntityToModelConverter.siacTModificaEntityToModificaMovimentoGestioneSpesaModel(siacTModifica, ((ModificaMovimentoGestioneSpesa)spesaNew));
				
			}else if(spesa instanceof ModificaMovimentoGestioneEntrata) {
				spesaNew = map(siacTModifica, ModificaMovimentoGestioneEntrata.class, FinMapId.SiacTModifica_ModificaMovimentoGestioneEntrata);	
				spesaNew = EntityToModelConverter.siacTModificaEntityToModificaMovimentoGestioneEntrataModel(siacTModifica, ((ModificaMovimentoGestioneEntrata)spesaNew));
			}
			
			if(siacRMovgestTsSogMod!=null){
				spesaNew = impostaDatiModSogg(siacRMovgestTsSogMod, spesaNew, enteId);
			}
			
			if(siacRMovgestTsSogClasseMod!=null){
				spesaNew = impostaDatiModSoggClass(siacRMovgestTsSogClasseMod, spesaNew, enteId);
			}
						
			spesa.setTipoMovimento(tipoMod);
			
			if(spesa.getTipoMovimento().equalsIgnoreCase(CostantiFin.MODIFICA_TIPO_SIM)){
				((ModificaMovimentoGestioneSpesa)spesa).setUidSubImpegno(siacTMovgestTs.getSiacTMovgest().getUid());
				((ModificaMovimentoGestioneSpesa)spesa).setNumeroSubImpegno(siacTMovgestTs.getSiacTMovgest().getMovgestNumero().intValue());
			} else if(spesa.getTipoMovimento().equalsIgnoreCase(CostantiFin.MODIFICA_TIPO_IMP)){
				((ModificaMovimentoGestioneSpesa)spesa).setUidImpegno(siacTMovgestTs.getSiacTMovgest().getUid());
			} else if(spesa.getTipoMovimento().equalsIgnoreCase(CostantiFin.MODIFICA_TIPO_ACC)){
				((ModificaMovimentoGestioneEntrata)spesa).setUidAccertamento(siacTMovgestTs.getSiacTMovgest().getUid());
			} else if(spesa.getTipoMovimento().equalsIgnoreCase(CostantiFin.MODIFICA_TIPO_SAC)){
				((ModificaMovimentoGestioneEntrata)spesa).setUidSubAccertamento(siacTMovgestTs.getSiacTMovgest().getUid());
				((ModificaMovimentoGestioneEntrata)spesa).setNumeroSubAccertamento(siacTMovgestTs.getSiacTMovgest().getMovgestNumero().intValue());
			}
		
			
		}
		//Termino restituendo l'oggetto di ritorno: 
        return spesaNew;
	}
	
	
	private boolean isSoggettoVuoto(Soggetto soggetto){
		boolean isEmptySoggetto = soggetto==null || StringUtilsFin.isEmpty(soggetto.getCodiceSoggetto());
		return isEmptySoggetto;
	}
	
	private boolean isClasseSoggettoVuoto(ClasseSoggetto classeSoggetto){
		boolean isEmptyClasse = classeSoggetto==null || StringUtilsFin.isEmpty(classeSoggetto.getCodice());
		return isEmptyClasse;
	}

	/**
	 * Aggiorna i dati relativi SiacTMovgestFin e tabelle accessorie. Utilizzato ampiamente per aggiornare impegni e subimpengi.
	 * @param impegnoDaAggiornare
	 * @param datiOperazioneDto
	 * @param bilancio
	 * @return
	 */
	private SiacTMovgestFin aggiornaSiacTMovgest(MovimentoGestione impegnoDaAggiornare, DatiOperazioneDto datiOperazioneDto, Bilancio bilancio){
		SiacTMovgestFin siacTMovgestUpdated = new SiacTMovgestFin();
		SiacTMovgestFin siacTMovgestDaAggiornare  = new SiacTMovgestFin();
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		String annoBil = Integer.toString(bilancio.getAnno());
		
		List<SiacTBilFin> siacTBilList = siacTBilRepository.getValidoByAnno(idEnte, annoBil, datiOperazioneDto.getTs());
		if(siacTBilList!=null && siacTBilList.size()>0 && siacTBilList.get(0)!=null){
			siacTMovgestDaAggiornare = siacTMovgestRepository.findOne(impegnoDaAggiornare.getUid()); 
			siacTMovgestDaAggiornare.setUid(impegnoDaAggiornare.getUid());
			
			//imposto operazioni di base
			siacTMovgestDaAggiornare = DatiOperazioneUtil.impostaDatiOperazioneLogin(siacTMovgestDaAggiornare, datiOperazioneDto, siacTAccountRepository);
			
			//sovrascrivo i campi di siacTMovgest
			SiacTBilFin siacTBil =  siacTBilList.get(0);
			siacTMovgestDaAggiornare.setSiacTBil(siacTBil);
			siacTMovgestDaAggiornare.setMovgestAnno(impegnoDaAggiornare.getAnnoMovimento());
			siacTMovgestDaAggiornare.setMovgestNumero(impegnoDaAggiornare.getNumeroBigDecimal());
			siacTMovgestDaAggiornare.setMovgestDesc(impegnoDaAggiornare.getDescrizione());
			
		}		
		
		String costanteTipo = null;
		if(impegnoDaAggiornare instanceof ImpegnoAbstract){
			costanteTipo =  CostantiFin.MOVGEST_TIPO_IMPEGNO;
		} else if(impegnoDaAggiornare instanceof AccertamentoAbstract){
			costanteTipo =  CostantiFin.MOVGEST_TIPO_ACCERTAMENTO;
		}
		
		
		// setto il parere finanziario
		if(!siacTMovgestDaAggiornare.getParereFinanziario().equals(impegnoDaAggiornare.getParereFinanziario())){
			siacTMovgestDaAggiornare.setParereFinanziario(impegnoDaAggiornare.getParereFinanziario());
			siacTMovgestDaAggiornare.setParereFinanziarioDataModifica(new Date(System.currentTimeMillis()));
			String parereFinanziarioLoginOperazione = DatiOperazioneUtil.determinaUtenteLogin(datiOperazioneDto, siacTAccountRepository);
			siacTMovgestDaAggiornare.setParereFinanziarioLoginOperazione(parereFinanziarioLoginOperazione);
		}
		
		
		SiacDMovgestTipoFin siacDMovgestTipo =  siacDMovgestTipoRepository.findValidoByCode(idEnte, datiOperazioneDto.getTs(), costanteTipo).get(0);
		siacTMovgestDaAggiornare.setSiacDMovgestTipo(siacDMovgestTipo);
		
		//salvo sul db:	
		siacTMovgestUpdated = siacTMovgestRepository.saveAndFlush(siacTMovgestDaAggiornare);
		
		//save capitolo:
		//Modificato per duplicazione riferimanti a capitolo si r_movgest_bil_elem
		SiacRMovgestBilElemFin capitoloModificato = null;
		List<SiacRMovgestBilElemFin> rMovgestBilElems=siacTMovgestDaAggiornare.getSiacRMovgestBilElems();
		if(impegnoDaAggiornare instanceof Impegno){
			SiacTBilElemFin siacTBilElem = getCapitoloUscitaGestione((Impegno) impegnoDaAggiornare, idEnte);
			if(siacTBilElem!=null){
				if (rMovgestBilElems!=null && rMovgestBilElems.size()>0) {
					for (SiacRMovgestBilElemFin rMovgestBilElemCorr : rMovgestBilElems) {
						if (rMovgestBilElemCorr.getDataFineValidita()==null && rMovgestBilElemCorr.getDataCancellazione()==null) {
							//Se valido e non cancellato
							rMovgestBilElemCorr = DatiOperazioneUtil.impostaDatiOperazioneLogin(rMovgestBilElemCorr, datiOperazioneDto, siacTAccountRepository);
							rMovgestBilElemCorr.setSiacTBilElem(siacTBilElem);
							rMovgestBilElemCorr.setSiacTMovgest(siacTMovgestUpdated);
							//salvo sul db:
							capitoloModificato = siacRMovgestBilElemRepository.saveAndFlush(rMovgestBilElemCorr);
							break;
						}
					}
				}
			}
		} else if(impegnoDaAggiornare instanceof Accertamento){
			SiacTBilElemFin siacTBilElem = getCapitoloEntrataGestione((Accertamento) impegnoDaAggiornare, idEnte);
			if(siacTBilElem!=null){
				if (rMovgestBilElems!=null && rMovgestBilElems.size()>0) {
					for (SiacRMovgestBilElemFin rMovgestBilElemCorr : rMovgestBilElems) {
						if (rMovgestBilElemCorr.getDataFineValidita()==null && rMovgestBilElemCorr.getDataCancellazione()==null) {
							//Se valido e non cancellato
							rMovgestBilElemCorr = DatiOperazioneUtil.impostaDatiOperazioneLogin(rMovgestBilElemCorr, datiOperazioneDto, siacTAccountRepository);
							rMovgestBilElemCorr.setSiacTBilElem(siacTBilElem);
							rMovgestBilElemCorr.setSiacTMovgest(siacTMovgestUpdated);
							//salvo sul db:
							capitoloModificato = siacRMovgestBilElemRepository.saveAndFlush(rMovgestBilElemCorr);
							break;
						}
					}
				}
			}
		}		
		if(capitoloModificato!=null){
			List<SiacRMovgestBilElemFin> rMovgestBilElemsAggiornata= DatiOperazioneUtil.replaceInList(rMovgestBilElems, capitoloModificato);
			siacTMovgestUpdated.setSiacRMovgestBilElems(rMovgestBilElemsAggiornata);
		}

		//Termino restituendo l'oggetto di ritorno: 
        return siacTMovgestUpdated;
	}
	
	/**
	 * Wrapper di aggiornaMovgestTs. Serve per richiamare aggiornaMovgestTs con tipologie diverse di dto in input
	 * @param impegnoDaAggiornare
	 * @param datiOperazioneDto
	 * @param siacTMovgestDaAggiornare
	 * @param idAmbito
	 * @param bilancio 
	 * @param impegnoInModificaInfoDto 
	 * @return
	 */
	private SiacTMovgestTsFin aggiornaSiacTMovgestTs(MovimentoGestione impegnoDaAggiornare, DatiOperazioneDto datiOperazioneDto, SiacTMovgestFin siacTMovgestDaAggiornare, Bilancio bilancio, ImpegnoInModificaInfoDto impegnoInModificaInfoDto){
		
		int idAmbito = datiOperazioneDto.getSiacDAmbito().getAmbitoId();
		
		///richiamo metodo aggiorna
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		int movgestId = siacTMovgestDaAggiornare.getUid();
		SiacTMovgestTsFin siacTMovgestTsDaAggiornare = siacTMovgestTsRepository.findMovgestTsByMovgest(idEnte, datiOperazioneDto.getTs(), movgestId).get(0);
		// il padre fa passato a null
		
		SiacTMovgestTsFin siacTMovgestTsUpdated = aggiornaMovgestTs(siacTMovgestTsDaAggiornare, siacTMovgestDaAggiornare, impegnoDaAggiornare, datiOperazioneDto, null, idAmbito,bilancio);
		siacTMovgestTsUpdated.setSiacTMovgest(siacTMovgestDaAggiornare);
		
		// eventuale gestione di VINCOLI
		if(impegnoDaAggiornare instanceof Impegno){
			
			ModificaVincoliImpegnoInfoDto valutati = impegnoInModificaInfoDto.getInfoVincoliValutati();
			
			List<SiacRMovgestTsFin> vincoliDaAnnullare = valutati.getVincoliDaAnnullare();
			List<VincoloImpegno> vincoliDaInserire = valutati.getVincoliDaInserire();
			List<VincoloImpegno> vincoliDaAggiornare = valutati.getVincoliDaAggiornare();
			List<SiacRMovgestTsFin> vincoliOld=valutati.getVincoliOld();
			
			//vincoli eliminati:
			if(vincoliDaAnnullare!=null && vincoliDaAnnullare.size()>0){
				for (SiacRMovgestTsFin siacRMovgestTsDaAnnullare : vincoliDaAnnullare) {
					DatiOperazioneUtil.annullaRecord(siacRMovgestTsDaAnnullare, siacRMovgestTsRepository, datiOperazioneDto,siacTAccountRepository);
				}
			}
			
			//nuovi vincoli inseriti:
			if(vincoliDaInserire!=null && vincoliDaInserire.size()>0){
				// inserimento
				for(VincoloImpegno vincoloImpegno: vincoliDaInserire){
					salvaVincoloImpegno(vincoloImpegno, datiOperazioneDto, siacTMovgestTsUpdated);
				}
			}
			
			//vincoli da modificare:
			if(vincoliDaAggiornare!=null && vincoliDaAggiornare.size()>0){
				// inserimento
				for(VincoloImpegno vincoloImpegno: vincoliDaAggiornare){
					salvaVincoloImpegno(vincoloImpegno, datiOperazioneDto, siacTMovgestTsUpdated);
				}
			}
			
		}

		
		///fine richiamo		
		//Termino restituendo l'oggetto di ritorno: 
        return siacTMovgestTsUpdated;
	}
	
	public ModificaVincoliImpegnoInfoDto valutaVincoliInAggiornamento(List<VincoloImpegno> listaVincoli, SiacTMovgestTsFin siacTMovgestTsUpdated, DatiOperazioneDto datiOperazioneDto){
		
		ModificaVincoliImpegnoInfoDto esito = new ModificaVincoliImpegnoInfoDto();
		
		List<SiacRMovgestTsFin> vincoliDaAnnullare = new ArrayList<SiacRMovgestTsFin>();
		List<VincoloImpegno> vincoliDaInserire = new ArrayList<VincoloImpegno>();
		List<VincoloImpegno> vincoliDaAggiornare = new ArrayList<VincoloImpegno>();
		
		List<VincoloImpegno> listaVincoliDaFE = listaVincoli;
		
		List<SiacRMovgestTsFin> vincoliOld=null;
		
		if(listaVincoli!=null && !listaVincoli.isEmpty()){
			// valuto i vincoli
			
			if(siacTMovgestTsUpdated.getSiacRMovgestTsB()!=null && !siacTMovgestTsUpdated.getSiacRMovgestTsB().isEmpty()){
								
				
				vincoliOld = DatiOperazioneUtil.soloValidi(siacTMovgestTsUpdated.getSiacRMovgestTsB(), getNow());
				
				List<Integer> listaIdsuFE = new ArrayList<Integer>();
				List<Integer> listaIdsuDB = new ArrayList<Integer>();
				
				for (VincoloImpegno vincoloFE : listaVincoliDaFE) {
					listaIdsuFE.add(vincoloFE.getUid());
				}
				
				//VINCOLI DA AGGIORNARE:
				for (SiacRMovgestTsFin siacRMovgestTs : vincoliOld) {
						// solo vincoli validi
					    // creo la lista di ID presenti su DB
					    listaIdsuDB.add(siacRMovgestTs.getUid());
					
						for (VincoloImpegno vinc : listaVincoli) {
							if(vinc.getUid()!=0 && vinc.getUid()==siacRMovgestTs.getMovgestTsRId()){
								// e' proprio il vincolo vado a vedere se e' cambiato
								if(!vinc.getImporto().equals(siacRMovgestTs.getMovgestTsImporto())){
									// allora e' cambiato l'importo quindi lo annullo
									vincoliDaAggiornare.add(vinc);
									break;
								}
								
							}
						}
				}
				
				
				//VINCOLI DA INSERIRE:
				for (VincoloImpegno vinc : listaVincoli) {
					 if(vinc.getUid()==0){
						   // e' un vincolo nuovo aggiunto nella lista
						   vincoliDaInserire.add(vinc);
					}
				}
				
				
				// lista vincoliFE < di vincoli a db
				if(listaIdsuFE.size() < listaIdsuDB.size()){
					// significa che ho eliminato dei vincoli da FE e quindi devo
					// cercare quello elimininato ed inserirlo nella lista degli eliminabili
					// valuto
					for (SiacRMovgestTsFin dbVincolo : vincoliOld) {
						
						if(!listaIdsuFE.contains(dbVincolo.getUid())){
							vincoliDaAnnullare.add(dbVincolo);
						}
					}
				}else if(listaIdsuFE.size() > listaIdsuDB.size() && listaIdsuDB.size()==0){
					   // sul FE sono stati inseriti dei vincoli e
					   // sono nel caso in cui non ci sono vincoli precedenti ma e' la prima volta che 
					   // inserisco i vincoli
					   for (VincoloImpegno vincoloDaInserire : listaVincoliDaFE) {
						   if(vincoloDaInserire.getUid()==0){
							   vincoliDaInserire.add(vincoloDaInserire);
						   }
					   }
				}
			}else{
				
				// non sono presenti vincoli, ma se dalla maschera ne ho inseriti allora devo inserirli ex-novo
			   for (VincoloImpegno vincoloDaInserire : listaVincoliDaFE) {
				   if(vincoloDaInserire.getUid()==0){
					   vincoliDaInserire.add(vincoloDaInserire);
				   }
			   }
				
			}
			
//			if(!vincoliDaAnnullare.isEmpty()){
//				// annullo
//				annullaVincoliImpegno(vincoliDaAnnullare);
//			}
//			
//			// eventuale inserimento di nuovi vincoli
//			if(!vincoliDaInserire.isEmpty()){
//				// inserimento
//				inserisciVincoliImpegno(vincoliDaInserire, datiOperazioneDto, siacTMovgestTsUpdated);
//				
//			}
			
		}else{
			// da FE ho eliminato tutti i vincoli, ma devo andare a controllare
			// che prima non esistessero, quindi devo andare ad annullarli tutti
			if(siacTMovgestTsUpdated.getSiacRMovgestTsB()!=null && !siacTMovgestTsUpdated.getSiacRMovgestTsB().isEmpty()){
				
				List<SiacRMovgestTsFin> siacRMovgestTsValidiDaAnnullare = DatiOperazioneUtil.soloValidi(siacTMovgestTsUpdated.getSiacRMovgestTsB(), getNow());
				
				vincoliDaAnnullare.addAll(siacRMovgestTsValidiDaAnnullare);
				
//				if(!vincoliDaAnnullare.isEmpty()){
//					// annullo
//					annullaVincoliImpegno(vincoliDaAnnullare);
//				}
			}	
			
		}
		esito.setVincoliDaAggiornare(vincoliDaAggiornare);
		esito.setVincoliDaAnnullare(vincoliDaAnnullare);
		esito.setVincoliDaInserire(vincoliDaInserire);
		esito.setVincoliOld(vincoliOld);
		return esito;
	}

	/**
	 * Costruisce una clausola di id seperati da virgola per lanciare una query con una IN 
	 * @param obj
	 * @return
	 */
	private String parseArrayToIn(List<IdImpegnoSubimpegno> obj){
	    StringBuffer result = new StringBuffer();
	    HashMap<Integer, Integer> idUtilizzati = new HashMap<Integer, Integer>();
    	boolean first = true;
    	if(obj!=null && obj.size()>0){
			for (IdImpegnoSubimpegno ogg : obj){
				if (ogg.getIdImpegno()!=null && !idUtilizzati.containsKey(ogg.getIdImpegno())){
					if(!first){
						result.append(",");
					}
					result.append(ogg.getIdImpegno());
					idUtilizzati.put(ogg.getIdImpegno(), ogg.getIdImpegno());
					first = false;
				}
			}
    	}
	   //Termino restituendo l'oggetto di ritorno: 
        return result.toString();
	  }

	/**
	 * Metodo che si occupa di ricerca impegni e subimpegni
	 * @param ente
	 * @param richiedente
	 * @param paramRic
	 * @param numPagina
	 * @param numRisPerPagina
	 * @return
	 */
	public List<Impegno> ricercaSinteticaImpegniSubImpegni(Ente ente, Richiedente richiedente , ParametroRicercaImpSub paramRic,int numPagina, int numRisPerPagina){
		
		Map<Integer,List<Impegno>> mapResultRicerca = new HashMap<Integer,List<Impegno>>();
		Integer numRisultati=0;
		
		RicercaImpSubParamDto paramSearch = (RicercaImpSubParamDto) map(paramRic, RicercaImpSubParamDto.class);

		if(paramRic.getUidStrutturaAmministrativoContabile()!=null){
			paramSearch.setUidStrutturaAmministrativoContabile(paramRic.getUidStrutturaAmministrativoContabile());
		}

		// QUERY PRINCIPALE - IMPEGNI
		// JIRA-1057
		List<IdImpegnoSubimpegno> listaIdImpegni = new ArrayList<IdImpegnoSubimpegno>();
		//task-168
		listaIdImpegni = impegnoDao.ricercaImpegniSubImpegni(ente.getUid(), paramSearch, true, false);

		List<Impegno> listaImpegni = new ArrayList<Impegno>();
		List<IdImpegnoSubimpegno> listaPaginata = new ArrayList<IdImpegnoSubimpegno>();
		
		if(listaIdImpegni!=null && listaIdImpegni.size() > 0){

			String clausolaIN =  parseArrayToIn(listaIdImpegni);
			//System.out.println("clausolaIN: "+clausolaIN);
			String[] ids =  clausolaIN.split(",");
			List<String> idsList = new ArrayList<String>(Arrays.asList(ids));
			String idsPaginati  = getIdsPaginati(idsList, numPagina, numRisPerPagina);
			
			//System.out.println("query in(idsPaginati): "+idsPaginati);

			// query totale con la IN
			List<SiacTMovgestFin> listaOggettiFinale = movimentoGestioneDao.ricercaSiacTMovgestPerIN(idsPaginati);
			
			// nuova versione
			if(listaOggettiFinale!=null && listaOggettiFinale.size()>0){
								
				for (SiacTMovgestFin siacTMovgest : listaOggettiFinale) {
			
					Impegno impegnoTrovato = map(siacTMovgest, Impegno.class, FinMapId.SiacTMovgest_Impegno);	
					impegnoTrovato = (Impegno) convertiSiacTMovgestFinToMovimentoGestione(siacTMovgest, impegnoTrovato);
					listaImpegni.add(impegnoTrovato);
					
				}	
			}
		
		}

        return listaImpegni;
	}
	
	
	/**
	 * Metodo che si occupa di ricerca impegni e subimpegni
	 * @param ente
	 * @param richiedente
	 * @param paramRic
	 * @param numPagina
	 * @param numRisPerPagina
	 * @return
	 */
	public List<Impegno> ricercaImpegniSubImpegni(Ente ente, Richiedente richiedente , ParametroRicercaImpSub paramRic,int numPagina, int numRisPerPagina){
		
		Map<Integer,List<Impegno>> mapResultRicerca = new HashMap<Integer,List<Impegno>>();
		Integer numRisultati=0;
		
		// fixme da verificare xchè non posso passare direttamente l'id qui nella query o far arrivare il codice dal client
		if(paramRic.getTipoProvvedimento()!=null && paramRic.getTipoProvvedimento().getUid()!=0){
			Timestamp now = new Timestamp(System.currentTimeMillis());
			List<SiacDAttoAmmTipoFin> siacDAttoAmmTipoList = siacDAttoAmmTipoRepository.findDAttoAmmTipoValidoByAttoAmmTipoIdAndEnteId(ente.getUid(), paramRic.getTipoProvvedimento().getUid(), now); 
			if(null!=siacDAttoAmmTipoList && siacDAttoAmmTipoList.size() > 0){
				String codiceTipoProvvedimento = siacDAttoAmmTipoList.get(0).getAttoammTipoCode();
				paramRic.getTipoProvvedimento().setCodice(codiceTipoProvvedimento);
			}
		}
		
		RicercaImpSubParamDto paramSearch = (RicercaImpSubParamDto) map(paramRic, RicercaImpSubParamDto.class);

		if(paramRic.getUidStrutturaAmministrativoContabile()!=null){
			paramSearch.setUidStrutturaAmministrativoContabile(paramRic.getUidStrutturaAmministrativoContabile());
		}

		// QUERY : ritorna una li
		// JIRA-1057
		List<IdImpegnoSubimpegno> listaIdImpegni = new ArrayList<IdImpegnoSubimpegno>();
		//task-168
		listaIdImpegni = impegnoDao.ricercaImpegniSubImpegni(ente.getUid(), paramSearch, true, false);

		
		
		// JIRA-1057
		List<Impegno> listaImpegnoNew = new ArrayList<Impegno>();
		List<IdImpegnoSubimpegno> listaPaginata = new ArrayList<IdImpegnoSubimpegno>();
		
		
		// JIRA-1057
		if(listaIdImpegni!=null && listaIdImpegni.size() > 0){

			String clausolaIN =  parseArrayToIn(listaIdImpegni);
			String[] ids =  clausolaIN.split(",");
			List<String> idsList = new ArrayList<String>(Arrays.asList(ids));
			String idsPaginati  = getIdsPaginati(idsList, numPagina, numRisPerPagina);
				
			
			List<SiacTMovgestFin> listaOggettiFinale = movimentoGestioneDao.ricercaSiacTMovgestPerIN(idsPaginati);
			
			for (SiacTMovgestFin movgest : listaOggettiFinale) {
				
				//System.out.println(String.format("%s-%s",movgest.getUid(), movgest.getMovgestNumero()));
				
			}
			
			// nuova versione
			if(listaOggettiFinale!=null && listaOggettiFinale.size()>0){
			
				Map<String, Soggetto> mappaSog = new HashMap<String, Soggetto>();
				
				for (SiacTMovgestFin siacTMovgest : listaOggettiFinale) {
			
					
					Impegno impNew = (Impegno)ricercaMovimentoPk(richiedente, 
															 ente , 
														     String.valueOf(paramRic.getAnnoEsercizio()), 
														     siacTMovgest.getMovgestAnno() , 
														     siacTMovgest.getMovgestNumero(), 
														     CostantiFin.MOVGEST_TIPO_IMPEGNO,
														     false);
				
					if(impNew!=null){
						Soggetto soggettoMovimento = new Soggetto();
						if(mappaSog.get(impNew.getSoggettoCode())==null){		
							soggettoMovimento = soggettoDad.ricercaSoggetto(CostantiFin.AMBITO_FIN, ente.getUid(), impNew.getSoggettoCode(), true, false);
							mappaSog.put(impNew.getSoggettoCode(), soggettoMovimento);
						}else{
							
							soggettoMovimento = mappaSog.get(impNew.getSoggettoCode());
						}
				
				
						if(impNew.getElencoSubImpegni()!=null && impNew.getElencoSubImpegni().size()>0){
							Soggetto soggettoSUBMovimento = new Soggetto();
							for (SubImpegno subImpegno : impNew.getElencoSubImpegni()) {									
								if(mappaSog.get(subImpegno.getSoggettoCode())==null){		
									soggettoSUBMovimento = soggettoDad.ricercaSoggetto(CostantiFin.AMBITO_FIN, ente.getUid(), subImpegno.getSoggettoCode(), true, false);
									mappaSog.put(subImpegno.getSoggettoCode(), soggettoMovimento);
								}else{										
									soggettoSUBMovimento = mappaSog.get(subImpegno.getSoggettoCode());
								}									
								subImpegno.setSoggetto(soggettoSUBMovimento);									
							}								
						}
						
						impNew.setSoggetto(soggettoMovimento);
						listaImpegnoNew.add(impNew);
					}
				}	
			}
		
		}

		return listaImpegnoNew;
	}
	
	
	
	
	
	private List<IdMovgestSubmovegest> triplicaIds(List<IdMovgestSubmovegest> listaIdImpegni) {
		List<IdMovgestSubmovegest> listaImpegnoFake = new ArrayList<IdMovgestSubmovegest>();
		for(int i=0;i<3;i++){
			for(IdMovgestSubmovegest it : listaIdImpegni){
				IdMovgestSubmovegest nuovo = clone(it);
				listaImpegnoFake.add(nuovo);
			}
		}
		return listaImpegnoFake;
	}

	private List<Impegno> triplica(List<Impegno> listaImpegnoNew){
		List<Impegno> listaImpegnoFake = new ArrayList<Impegno>();
		for(int i=0;i<3;i++){
			for(Impegno it : listaImpegnoNew){
				Impegno nuovo = clone(it);
				listaImpegnoFake.add(nuovo);
			}
		}
		return listaImpegnoFake;
	}


	
	private ArrayList<Integer> getDistintiMovgestId(List<IdMovgestSubmovegest> listaIdImpegni){
		ArrayList<Integer> listaMovegestId = new ArrayList<Integer>();
		for(IdMovgestSubmovegest it: listaIdImpegni){
			int movGestIdit = it.getMovgestId();
			if(!listaIdImpegni.contains(movGestIdit)){
				listaMovegestId.add(movGestIdit);
			}
		}
		return listaMovegestId;
	}
	
	
	private List<IdMovgestSubmovegest> soloConDispAFinanziareMaggioreDiZero(List<IdMovgestSubmovegest> listaIdImpegni,Ente ente){
		
		List<IdMovgestSubmovegest> conDispFinanziareMaggioreDiZero = new ArrayList<IdMovgestSubmovegest>();
		
		
		if(listaIdImpegni!=null && listaIdImpegni.size()>0){
			
			ArrayList<Integer> listaMovegestTsId = new ArrayList<Integer>();
			
			for(IdMovgestSubmovegest it: listaIdImpegni){
				listaMovegestTsId.add(it.getMovgestTsId());
			}
			
			List<SiacTMovgestTsDetFin> listaImportiAttuali = movimentoGestioneDao.findImportoMassive(ente.getUid(), listaMovegestTsId, CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE);
			
			
			for(IdMovgestSubmovegest it: listaIdImpegni){
				Integer movgestTsId = it.getMovgestTsId();
				BigDecimal importoAttuale = findImpByMovgestTsId(listaImportiAttuali, movgestTsId);
			//	BigDecimal sommaVoci = findSommaVociByMovgestTsId(elencoSommeVoci,movgestTsId);
				
				BigDecimal disponibilitaFinanziare = importoAttuale;
//				BigDecimal disponibilitaFinanziare = importoAttuale.subtract(sommaVoci);
				
				if(disponibilitaFinanziare.compareTo(BigDecimal.ZERO)>0){
					conDispFinanziareMaggioreDiZero.add(it);
				}
			}
			
		}
		
		return conDispFinanziareMaggioreDiZero;
	}
	
	/**
	 * utility che data una lista di SiacTMovgestTsDetFin individua quello relativo al movgestTsId indicato
	 * @param listaImportiAttuali
	 * @param movgestTsId
	 * @return
	 */
	private BigDecimal findImpByMovgestTsId(List<SiacTMovgestTsDetFin> listaImportiAttuali, Integer movgestTsId){
		BigDecimal importoAttuale = BigDecimal.ZERO;
		SiacTMovgestTsDetFin siacTMovgestTsDetFinIt = findImportoByMovgestTsId(listaImportiAttuali,movgestTsId);
		if(siacTMovgestTsDetFinIt!=null && siacTMovgestTsDetFinIt.getMovgestTsDetImporto()!=null){
			importoAttuale = siacTMovgestTsDetFinIt.getMovgestTsDetImporto(); 
		}
		return importoAttuale;
	}
	
	/**
	 * utility che data una lista di SiacTMovgestTsDetFin individua quello relativo al movgestTsId indicato
	 * @param listaImportiAttuali
	 * @param movgestTsId
	 * @return
	 */
	private SiacTMovgestTsDetFin findImportoByMovgestTsId(List<SiacTMovgestTsDetFin> listaImportiAttuali, Integer movgestTsId){
		SiacTMovgestTsDetFin importoAttuale = null;
		if(movgestTsId!=null && listaImportiAttuali!=null && listaImportiAttuali.size()>0){
			for(SiacTMovgestTsDetFin importoIt : listaImportiAttuali){
				if(importoIt!=null && importoIt.getSiacTMovgestT()!=null && importoIt.getSiacTMovgestT().getMovgestTsId()!=null){
					if(importoIt.getSiacTMovgestT().getMovgestTsId().intValue()==movgestTsId.intValue()){
						importoAttuale = importoIt;
						break;
					}
				}
			}
		}
		return importoAttuale;
	}
	
	/**
	 * Utility che dato un elenco di somme voci di diversi impegni/sub, restituisce l'importo della somma
	 * delle voci per l'imp/sub indicato con l'id  movgestTsId
	 * @param elencoSommeVoci
	 * @param movgestTsId
	 * @return
	 */
	private BigDecimal findSommaVociByMovgestTsId(List<CodificaImportoDto> elencoSommeVoci, Integer movgestTsId){
		BigDecimal sommaVoci = BigDecimal.ZERO;
		if(movgestTsId!=null && elencoSommeVoci!=null && elencoSommeVoci.size()>0){
			for(CodificaImportoDto sommaVociIt : elencoSommeVoci){
				if(sommaVociIt!=null && sommaVociIt.getIdOggetto()!=null){
					if(sommaVociIt.getIdOggetto().intValue()==movgestTsId.intValue()){
						sommaVoci = sommaVociIt.getImporto();
						break;
					}
				}
			}
		}
		if(sommaVoci==null){
			sommaVoci = BigDecimal.ZERO;
		}
		return sommaVoci;
	}
	
	
	private Impegno lasciaSoloUnSubImpegno(Impegno impNew,ArrayList<Integer> uidSubImpGiaAggiunti){
		ArrayList<SubImpegno> subImpegnoDiQuestoGiro = new ArrayList<SubImpegno>();
		for (SubImpegno subImpegno : impNew.getElencoSubImpegni()) {
			Integer uidIterato = subImpegno.getUid();
			if(!uidSubImpGiaAggiunti.contains(uidIterato)){
				subImpegnoDiQuestoGiro.add(subImpegno);
				uidSubImpGiaAggiunti.add(uidIterato);
				break;
			}
		}
		impNew.setElencoSubImpegni(subImpegnoDiQuestoGiro);
		return impNew;
	}
	
	
	private Impegno lasciaSoloIlSubImpegno(Impegno impNew, Integer movgestTsId) {
		ArrayList<SubImpegno> subImpegnoDiQuestoGiro = new ArrayList<SubImpegno>();
		for (SubImpegno subImpegno : impNew.getElencoSubImpegni()) {
			Integer uidIterato = subImpegno.getUid();
			if(movgestTsId!=null && uidIterato!=null && movgestTsId.intValue()==uidIterato.intValue()){
				subImpegnoDiQuestoGiro.add(subImpegno);
				break;
			}
		}
		impNew.setElencoSubImpegni(subImpegnoDiQuestoGiro);
		return impNew;
	}
	
	
	private Impegno caricaImpegnoConCache(SiacTMovgestFin siacTMovgest,HashMap<Integer, Impegno> cacheImpegni, Ente ente, Richiedente richiedente
			,Integer annoEsercizio){
		Integer uidImpegno = siacTMovgest.getUid();
		Impegno impFinded = null;
		if(cacheImpegni.containsKey(uidImpegno)){
			Impegno fromCache = cacheImpegni.get(uidImpegno);
			impFinded = clone(fromCache);
		} else {
			impFinded = (Impegno)ricercaMovimentoPk(richiedente, 
					 ente , 
				     String.valueOf(annoEsercizio), 
				     siacTMovgest.getMovgestAnno() , 
				     siacTMovgest.getMovgestNumero(), 
				     CostantiFin.MOVGEST_TIPO_IMPEGNO,
				     false);
			cacheImpegni.put(uidImpegno, impFinded);
		}
		Impegno daRestituire = null;
		if(impFinded!=null){
			daRestituire = clone(impFinded);
		}
		return daRestituire;
	}
	
	
	private Soggetto caricaSoggettoConCache(String codiceSoggetto,HashMap<String, Soggetto> soggettoCache, Ente ente, Richiedente richiedente){
		Soggetto soggettoMovimento = null;
		if(soggettoCache.get(codiceSoggetto)==null){		
			soggettoMovimento  = soggettoDad.ricercaSoggetto(CostantiFin.AMBITO_FIN, ente.getUid(), codiceSoggetto, true, false);
			soggettoCache.put(codiceSoggetto, soggettoMovimento);
		}else{
			soggettoMovimento = soggettoCache.get(codiceSoggetto);
		}
		return soggettoMovimento;
	}
	
	
	private String parseArrayToListMovgestId(List<IdMovgestSubmovegest> lista){
	    StringBuffer result = new StringBuffer();
	    HashMap<Integer, Integer> idUtilizzati = new HashMap<Integer, Integer>();
    	boolean first = true;
    	if(lista!=null && lista.size()>0){
			for (IdMovgestSubmovegest ogg : lista){
				if (!idUtilizzati.containsKey(ogg.getMovgestId())){
					if(!first){
						result.append(",");
					}
					result.append(ogg.getMovgestId());
					idUtilizzati.put(ogg.getMovgestId(), ogg.getMovgestId());
					first = false;
				}
			}
    	}
	   //Termino restituendo l'oggetto di ritorno: 
        return result.toString();
	  }
	
	/**
	 * metodo di appoggio per parseArrayToInReplicaSub
	 * @param lista
	 * @param obj
	 * @return
	 */
	private boolean haDeiSubInList(List<IdMovgestSubmovegest> lista, IdMovgestSubmovegest obj){
    	boolean haDeiSub = false;
    	Integer idImpegno = obj.getMovgestId();
    	if(lista!=null && lista.size()>0){
			for (IdMovgestSubmovegest it : lista){
				if (it.getMovgestTsIdPadre()!=null && it.getMovgestId().intValue()==idImpegno.intValue()){
					haDeiSub=true;
					break;
				}
			}
    	}
        return haDeiSub;
	  }
	
	
	private List<SiacTMovgestFin> espandiDuplicati(List<SiacTMovgestFin> listaOggettiFinaleTEST,List<String> idsListTEST) {
		List<SiacTMovgestFin> listaOggettiFinaleTESTConDoppioni = new ArrayList<SiacTMovgestFin>();
		for(SiacTMovgestFin it : listaOggettiFinaleTEST){
			//Integer keyIt = it.getSiacTMovgestTs().get(0).getMovgestTsId();
			Integer keyIt = it.getMovgestId();
			int count = contaOccorrenze(idsListTEST, keyIt);
			for(int i=0;i<count;i++){
				SiacTMovgestFin daAggiungere = clone(it);
				listaOggettiFinaleTESTConDoppioni.add(daAggiungere);	
			}
		}
		return listaOggettiFinaleTESTConDoppioni;
	}
	
	/**
	 * metodo di appoggio per espandiDuplicati
	 * @param idsListTEST
	 * @param keyIt
	 * @return
	 */
	private int contaOccorrenze(List<String> idsListTEST, Integer keyIt) {
		int count = 0;
		for(String idIt : idsListTEST){
			Integer idIterato = Integer.parseInt(idIt);
				if(idIterato.intValue()==keyIt.intValue()){
					count = count + 1;
				}
		}
		return count;
	}

	private void ordinaImpegniSubImpegni(List<IdImpegnoSubimpegno> idImpegnoSubimpegnoList) {
		Collections.sort(idImpegnoSubimpegnoList, new Comparator<IdImpegnoSubimpegno>(){

			@Override
			public int compare(IdImpegnoSubimpegno s1, IdImpegnoSubimpegno s2) {
				return new CompareToBuilder()
						.append(s1.getAnno(), s2.getAnno()).append(s1.getNumero().intValue(), s2.getNumero().intValue()).toComparison();
			}
		});
	}
	
	
	/**
	 * Metodo che si occupa di calcolare solo il numero di impegni trovati
	 * @param ente
	 * @param richiedente
	 * @param paramRic
	 * @param numPagina
	 * @param numRisPerPagina
	 * @return
	 */
	public Integer getCountRicercaImpegniSubImpegni(Ente ente, Richiedente richiedente , ParametroRicercaImpSub paramRic){
		
		Integer countResult = 0;
		if(paramRic.getTipoProvvedimento()!=null && paramRic.getTipoProvvedimento().getUid()!=0){
			Timestamp now = new Timestamp(System.currentTimeMillis());
			List<SiacDAttoAmmTipoFin> siacDAttoAmmTipoList = siacDAttoAmmTipoRepository.findDAttoAmmTipoValidoByAttoAmmTipoIdAndEnteId(ente.getUid(), paramRic.getTipoProvvedimento().getUid(), now); 
			if(null!=siacDAttoAmmTipoList && siacDAttoAmmTipoList.size() > 0){
				String codiceTipoProvvedimento = siacDAttoAmmTipoList.get(0).getAttoammTipoCode();
				paramRic.getTipoProvvedimento().setCodice(codiceTipoProvvedimento);
			}
		}
		
		RicercaImpSubParamDto paramSearch = (RicercaImpSubParamDto) map(paramRic, RicercaImpSubParamDto.class);

		if(paramRic.getUidStrutturaAmministrativoContabile()!=null){
			paramSearch.setUidStrutturaAmministrativoContabile(paramRic.getUidStrutturaAmministrativoContabile());
		}

		// QUERY PRINCIPALE - IMPEGNI
		// JIRA-1057
		List<IdImpegnoSubimpegno> listaIdImpegni = new ArrayList<IdImpegnoSubimpegno>();
		//task-168
		listaIdImpegni = impegnoDao.ricercaImpegniSubImpegni(ente.getUid(), paramSearch, true, false);
		
//		log.debug("getCountRicercaImpegniSubImpegni", " impegni trovati: " + listaIdImpegni !=null ? listaIdImpegni.size() : " [nessun id impegno trovato]");
//		for (IdImpegnoSubimpegno idImpegnoSubimpegno : listaIdImpegni) {
//			log.debug("getCountRicercaImpegniSubImpegni", " ID impegno: " + idImpegnoSubimpegno.getIdImpegno());
//		}
		
		if(listaIdImpegni!=null && listaIdImpegni.size() > 0){
				
			String clausolaIN =  parseArrayToIn(listaIdImpegni);
			String[] ids =  clausolaIN.split(",");
			countResult = ids.length;
		
		}else countResult = 0;

		return countResult;
	}
	
	


	/**
	 * Versione ottimizzata
	 * @param ente
	 * @param richiedente
	 * @param paramRic
	 * @param numPagina
	 * @param numRisPerPagina
	 * @return
	 */
	public Integer getCountRicercaAccertamentiSubAccertamenti(Ente ente, Richiedente richiedente, ParametroRicercaAccSubAcc paramRic,
			int numPagina, int numRisPerPagina){
		
		Integer countResult = 0;
		
		if(null!=paramRic.getTipoProvvedimento() && paramRic.getTipoProvvedimento().getUid()!=0){	
			Timestamp now = new Timestamp(System.currentTimeMillis());
			List<SiacDAttoAmmTipoFin> siacDAttoAmmTipoList = siacDAttoAmmTipoRepository.findDAttoAmmTipoValidoByAttoAmmTipoIdAndEnteId(ente.getUid(), paramRic.getTipoProvvedimento().getUid(), now); 
			if(null!=siacDAttoAmmTipoList && siacDAttoAmmTipoList.size() > 0){
				String codiceTipoProvvedimento = siacDAttoAmmTipoList.get(0).getAttoammTipoCode();
				paramRic.getTipoProvvedimento().setCodice(codiceTipoProvvedimento);
			}
		}
		
		RicercaAccSubAccParamDto paramSearch = (RicercaAccSubAccParamDto) map(paramRic, RicercaAccSubAccParamDto.class);

		if(paramRic.getUidStrutturaAmministrativoContabile()!=null){
			paramSearch.setUidStrutturaAmministrativoContabile(paramRic.getUidStrutturaAmministrativoContabile());
		}
		
		// QUERY PRINCIPALE - ACCERTAMENTI
		// JIRA-1057
		List<IdImpegnoSubimpegno> listaIdImpegni = new ArrayList<IdImpegnoSubimpegno>();
		listaIdImpegni = accertamentoDao.ricercaAccertamentiSubAccertamenti(ente.getUid(), paramSearch, true);
		
		if(listaIdImpegni!=null && listaIdImpegni.size() > 0){
			
			String clausolaIN =  parseArrayToIn(listaIdImpegni);
			String[] ids =  clausolaIN.split(",");
			countResult = ids.length;
			
		}else countResult = 0;

		return countResult;
	}
	
	
	
	
	/**
	 * Versione ottimizzata
	 * @param ente
	 * @param richiedente
	 * @param paramRic
	 * @param numPagina
	 * @param numRisPerPagina
	 * @return
	 */
	public List<Accertamento> ricercaAccertamentiSubAccertamentiOPT(Ente ente, Richiedente richiedente, ParametroRicercaAccSubAcc paramRic,
			int numPagina, int numRisPerPagina){
		
		if(null!=paramRic.getTipoProvvedimento() && paramRic.getTipoProvvedimento().getUid()!=0){	
			Timestamp now = new Timestamp(System.currentTimeMillis());
			List<SiacDAttoAmmTipoFin> siacDAttoAmmTipoList = siacDAttoAmmTipoRepository.findDAttoAmmTipoValidoByAttoAmmTipoIdAndEnteId(ente.getUid(), paramRic.getTipoProvvedimento().getUid(), now); 
			if(null!=siacDAttoAmmTipoList && siacDAttoAmmTipoList.size() > 0){
				String codiceTipoProvvedimento = siacDAttoAmmTipoList.get(0).getAttoammTipoCode();
				paramRic.getTipoProvvedimento().setCodice(codiceTipoProvvedimento);
			}
		}
		
		RicercaAccSubAccParamDto paramSearch = (RicercaAccSubAccParamDto) map(paramRic, RicercaAccSubAccParamDto.class);

		if(paramRic.getUidStrutturaAmministrativoContabile()!=null){
			paramSearch.setUidStrutturaAmministrativoContabile(paramRic.getUidStrutturaAmministrativoContabile());
		}
		
		// QUERY PRINCIPALE - ACCERTAMENTI
		// JIRA-1057
		List<IdImpegnoSubimpegno> listaIdImpegni = new ArrayList<IdImpegnoSubimpegno>();
		listaIdImpegni = accertamentoDao.ricercaAccertamentiSubAccertamenti(ente.getUid(), paramSearch, true);
		
		// JIRA-1057
		if(listaIdImpegni==null)
			listaIdImpegni = new ArrayList<IdImpegnoSubimpegno>();

		
		// JIRA-1057
		List<Accertamento> listaAccertamentoNew = new ArrayList<Accertamento>();
		List<IdImpegnoSubimpegno> listaPaginata = new ArrayList<IdImpegnoSubimpegno>();
		
		// JIRA-1057
		if(listaIdImpegni!=null && listaIdImpegni.size() > 0){
			
			String clausolaIN =  parseArrayToIn(listaIdImpegni);
			
			String[] ids =  clausolaIN.split(",");
			List<String> idsList = new ArrayList<String>(Arrays.asList(ids));
			String idsPaginati  = getIdsPaginati(idsList, numPagina, numRisPerPagina);
			
			// query totale con la IN
			List<SiacTMovgestFin> listaOggettiFinale = movimentoGestioneDao.ricercaSiacTMovgestPerIN(idsPaginati);
			
			if(listaOggettiFinale!=null && !listaOggettiFinale.isEmpty()){
				
				completaAccertamentoSubAccDaRicerca(ente, richiedente,  String.valueOf(paramRic.getAnnoEsercizio()),
						listaAccertamentoNew, listaOggettiFinale);

			}
			
		}	
		
		//Termino restituendo l'oggetto di ritorno: 
	    return listaAccertamentoNew;
	}
	
	/**
	 * Versione ottimizzata
	 * @param ente
	 * @param richiedente
	 * @param paramRic
	 * @param numPagina
	 * @param numRisPerPagina
	 * @return
	 */
	public List<Accertamento> ricercaSinteticaAccertamentiSubAccertamenti(Ente ente, Richiedente richiedente, ParametroRicercaAccSubAcc paramRic,
			int numPagina, int numRisPerPagina){
		
	
		RicercaAccSubAccParamDto paramSearch = (RicercaAccSubAccParamDto) map(paramRic, RicercaAccSubAccParamDto.class);

		if(paramRic.getUidStrutturaAmministrativoContabile()!=null){
			paramSearch.setUidStrutturaAmministrativoContabile(paramRic.getUidStrutturaAmministrativoContabile());
		}
		
		// QUERY PRINCIPALE - ACCERTAMENTI
		// JIRA-1057
		List<IdImpegnoSubimpegno> listaIdImpegni = new ArrayList<IdImpegnoSubimpegno>();
		listaIdImpegni = accertamentoDao.ricercaAccertamentiSubAccertamenti(ente.getUid(), paramSearch, true);
		
		if(listaIdImpegni==null)
			listaIdImpegni = new ArrayList<IdImpegnoSubimpegno>();

		List<Accertamento> listaAccertamenti = new ArrayList<Accertamento>();
		List<IdImpegnoSubimpegno> listaPaginata = new ArrayList<IdImpegnoSubimpegno>();
		
		if(listaIdImpegni!=null && listaIdImpegni.size() > 0){
			
			String clausolaIN =  parseArrayToIn(listaIdImpegni);
			
			String[] ids =  clausolaIN.split(",");
			List<String> idsList = new ArrayList<String>(Arrays.asList(ids));
			String idsPaginati  = getIdsPaginati(idsList, numPagina, numRisPerPagina);
			
			// query totale con la IN
			List<SiacTMovgestFin> listaOggettiFinale = movimentoGestioneDao.ricercaSiacTMovgestPerIN(idsPaginati);
			
			if(listaOggettiFinale!=null && !listaOggettiFinale.isEmpty()){
				
				for (SiacTMovgestFin siacTMovgest : listaOggettiFinale) {
					
					Accertamento accertamentoTrovato = map(siacTMovgest, Accertamento.class, FinMapId.SiacTMovgest_Accertamento);	
					accertamentoTrovato = (Accertamento) convertiSiacTMovgestFinToMovimentoGestione(siacTMovgest, accertamentoTrovato);
					listaAccertamenti.add(accertamentoTrovato);
					
				}	

			}
			
		}	
		
		//Termino restituendo l'oggetto di ritorno: 
	    return listaAccertamenti;
	}

	private void completaAccertamentoSubAccDaRicerca(Ente ente, Richiedente richiedente,
			String annoEsercizio,
			List<Accertamento> listaAccertamentoNew,
			List<SiacTMovgestFin> listaOggettiFinale) {

		Map<String, Soggetto> mappaSog = new HashMap<String, Soggetto>();
		
		for (SiacTMovgestFin siacTMovgest : listaOggettiFinale) {

			Accertamento accertamento = (Accertamento)ricercaMovimentoPk(richiedente, 
													 ente , 
													 annoEsercizio, 
												     siacTMovgest.getMovgestAnno() , 
												     siacTMovgest.getMovgestNumero(), 
												     CostantiFin.MOVGEST_TIPO_ACCERTAMENTO,
												     false);
	
			if(accertamento!=null){
				Soggetto soggettoMovimento = new Soggetto();
				if(mappaSog.get(accertamento.getSoggettoCode())==null){		
					soggettoMovimento = soggettoDad.ricercaSoggetto(CostantiFin.AMBITO_FIN, ente.getUid(), accertamento.getSoggettoCode(), true, false);
					mappaSog.put(accertamento.getSoggettoCode(), soggettoMovimento);
				}else{
					
					soggettoMovimento = mappaSog.get(accertamento.getSoggettoCode());
				}
	
	
				if(accertamento.getElencoSubAccertamenti()!=null && accertamento.getElencoSubAccertamenti().size()>0){
					Soggetto soggettoSUBMovimento = new Soggetto();
					for (SubAccertamento sub : accertamento.getElencoSubAccertamenti()) {									
						if(mappaSog.get(sub.getSoggettoCode())==null){		
							soggettoSUBMovimento = soggettoDad.ricercaSoggetto(CostantiFin.AMBITO_FIN, ente.getUid(), sub.getSoggettoCode(), true, false);
							mappaSog.put(sub.getSoggettoCode(), soggettoMovimento);
						}else{										
							soggettoSUBMovimento = mappaSog.get(sub.getSoggettoCode());
						}									
								sub.setSoggetto(soggettoSUBMovimento);									
					}								
				}
				
				accertamento.setSoggetto(soggettoMovimento);
				listaAccertamentoNew.add(accertamento);
			}
		}	

	}

	private void completaAccertamentiSubAccMassivaDaRicerca(Ente ente, Richiedente richiedente,
			RicercaAccSubAccParamDto paramSearch,
			List<Accertamento> listaAccertamentoNew,
			List<SiacTMovgestFin> listaOggettiFinale, DatiOperazioneDto datiOperazioneDto) {
		// Qui prende i sub, solo i validi!
		List<SiacTMovgestTsFin> listaSiacTMovgestTsCoinvolti = new ArrayList<SiacTMovgestTsFin>();
		
		for (SiacTMovgestFin siacTMovgest : listaOggettiFinale) {
			List<SiacTMovgestTsFin> listaSiacTMovgestTs =siacTMovgest.getSiacTMovgestTs();
			
			
			listaSiacTMovgestTs = DatiOperazioneUtil.soloValidi(listaSiacTMovgestTs, getNow());
			listaSiacTMovgestTsCoinvolti.addAll(listaSiacTMovgestTs);
		}
		
		//ottimizzazione soggetto:
		OttimizzazioneSoggettoDto ottimizzazioneSoggettoDto = caricaDatiOttimizzazioneRicercaSoggetto(listaSiacTMovgestTsCoinvolti);
		List<SiacTSoggettoFin> distintiSiacTSoggettiCoinvolti = ottimizzazioneSoggettoDto.getDistintiSiacTSoggettiCoinvolti();
		List<SiacRMovgestTsSogFin> distintiSiacRSoggettiCoinvolti = ottimizzazioneSoggettoDto.getDistintiSiacRSoggettiCoinvolti();
		//mapping model:
		List<Soggetto> distintiSoggettiCoinvolti = soggettoDad.ricercaSoggettoOPT(distintiSiacTSoggettiCoinvolti, true, false,ottimizzazioneSoggettoDto,datiOperazioneDto);
		
		//OTTIMIZZAZIONE movgest:
		List<CodificaImportoDto> listaDisponibiliIncassare = new ArrayList<CodificaImportoDto>();

		listaDisponibiliIncassare = accertamentoDao.calcolaDisponibilitaAIncassareMassive(listaSiacTMovgestTsCoinvolti);

		
		List<SiacRMovgestTsAttrFin> distintiSiacRMovgestTsAttrCoinvolti =movimentoGestioneDao.ricercaByMovGestTsMassive(listaSiacTMovgestTsCoinvolti, "SiacRMovgestTsAttrFin");
		List<SiacRMovgestClassFin> distintiSiacRMovgestClassCoinvolti =  movimentoGestioneDao.ricercaByMovGestTsMassive(listaSiacTMovgestTsCoinvolti,"SiacRMovgestClassFin");
		List<SiacRMovgestTsStatoFin> distintiSiacRMovgestTsStatoCoinvolti = movimentoGestioneDao.ricercaByMovGestTsMassive(listaSiacTMovgestTsCoinvolti,"SiacRMovgestTsStatoFin");
		List<SiacTMovgestTsDetFin> distintiSiacTMovgestTsDetCoinvolti = movimentoGestioneDao.ricercaByMovGestTsMassive(listaSiacTMovgestTsCoinvolti,"SiacTMovgestTsDetFin");
		List<SiacRMovgestTsSogclasseFin> distintiSiacRMovgestTsSogclasseCoinvolti = movimentoGestioneDao.ricercaByMovGestTsMassive(listaSiacTMovgestTsCoinvolti,"SiacRMovgestTsSogclasseFin");
		List<SiacRMovgestTsProgrammaFin> distintiSiacRMovgestTsProgrammaCoinvolti = movimentoGestioneDao.ricercaByMovGestTsMassive(listaSiacTMovgestTsCoinvolti,"SiacRMovgestTsProgrammaFin");
		List<SiacRMovgestTsAttoAmmFin> distintiSiacRMovgestTsAttoAmmCoinvolti =movimentoGestioneDao.ricercaByMovGestTsMassive(listaSiacTMovgestTsCoinvolti, "SiacRMovgestTsAttoAmmFin");
		List<SiacROrdinativoTsMovgestTFin> distintiSiacROrdinativoTsMovgestTCoinvolti =movimentoGestioneDao.ricercaByMovGestTsMassive(listaSiacTMovgestTsCoinvolti, "SiacROrdinativoTsMovgestTFin");
		List<SiacRMovgestBilElemFin> distintiSiacRMovgestBilElemCoinvolti =  movimentoGestioneDao.ricercaSiacRMovgestBilElemMassive(listaOggettiFinale);
		/////set nel dto:
		OttimizzazioneMovGestDto ottimizzazioneDto = new OttimizzazioneMovGestDto();
		ottimizzazioneDto.setListaDisponibiliIncassare(listaDisponibiliIncassare);
		ottimizzazioneDto.setDistintiSiacRMovgestClassCoinvolti(distintiSiacRMovgestClassCoinvolti);
		ottimizzazioneDto.setDistintiSiacRMovgestTsAttrCoinvolti(distintiSiacRMovgestTsAttrCoinvolti);
		ottimizzazioneDto.setDistintiSiacRMovgestTsStatoCoinvolti(distintiSiacRMovgestTsStatoCoinvolti);
		ottimizzazioneDto.setDistintiSiacRSoggettiCoinvolti(distintiSiacRSoggettiCoinvolti);
		ottimizzazioneDto.setDistintiSiacTSoggettiCoinvolti(distintiSiacTSoggettiCoinvolti);
		ottimizzazioneDto.setDistintiSoggettiCoinvolti(distintiSoggettiCoinvolti);
		ottimizzazioneDto.setDistintiSiacTMovgestTsDetCoinvolti(distintiSiacTMovgestTsDetCoinvolti);
		ottimizzazioneDto.setDistintiSiacRMovgestTsSogclasseCoinvolti(distintiSiacRMovgestTsSogclasseCoinvolti);
		ottimizzazioneDto.setDistintiSiacRMovgestTsProgrammaCoinvolti(distintiSiacRMovgestTsProgrammaCoinvolti);
		ottimizzazioneDto.setDistintiSiacRMovgestTsAttoAmmCoinvolti(distintiSiacRMovgestTsAttoAmmCoinvolti);
		ottimizzazioneDto.setDistintiSiacRMovgestBilElemCoinvolti(distintiSiacRMovgestBilElemCoinvolti);
		ottimizzazioneDto.setDistintiSiacROrdinativoTsMovgestTCoinvolti(distintiSiacROrdinativoTsMovgestTCoinvolti);
		
		if(listaOggettiFinale!=null && listaOggettiFinale.size()>0){
			
			List<Accertamento> accNewList = (List<Accertamento>) ricercaMovimentoPkMssive(richiedente, ente, listaOggettiFinale, false,ottimizzazioneDto);
			
			for (SiacTMovgestFin siacTMovgest : listaOggettiFinale) {
				
				Accertamento accNew = CommonUtil.getById(accNewList, siacTMovgest.getUid());
				
				SiacTMovgestTsFin testata = estraiTestata(siacTMovgest);
				
				if(accNew!=null){
					
					Soggetto soggettoMovimento = new Soggetto();
					
					SiacTSoggettoFin siacTSog = ottimizzazioneDto.getSoggettoByMovGestTsId(testata.getMovgestTsId());
					if(siacTSog!=null){
						//puo' non averlo se ha dei sub
						soggettoMovimento = CommonUtil.getSoggettoByCode(distintiSoggettiCoinvolti, siacTSog.getSoggettoCode());
						
					}

					boolean isSubAccPresent = false;
					boolean isSubAccDisponibilitaIncassare = false;

					if(accNew.getElencoSubAccertamenti()!=null && accNew.getElencoSubAccertamenti().size()>0){
						isSubAccPresent = true;
						Soggetto soggettoSUBMovimento = new Soggetto();
						for (SubAccertamento subAccertamento : accNew.getElencoSubAccertamenti()) {
							
							siacTSog = ottimizzazioneDto.getSoggettoByMovGestTsId(subAccertamento.getUid());
							soggettoSUBMovimento = CommonUtil.getSoggettoByCode(distintiSoggettiCoinvolti, siacTSog.getSoggettoCode());
							
							subAccertamento.setSoggetto(soggettoSUBMovimento);
									
							if(subAccertamento.getDisponibilitaIncassare().compareTo(BigDecimal.ZERO) > 0){
								isSubAccDisponibilitaIncassare = true;
							}
						}
					}

					accNew.setSoggetto(soggettoMovimento);

					// 2.4.9.2 : se il flag disponibilitaAdIncassare == true, restituire l'accertamento e tutti i suoi subAccertamenti se almeno uno dei
					// subAccertamenti ha il campo subAccertamento.disponibilitaIncassare > 0
					if(paramSearch.isDisponibilitaAdIncassare()) {
						 // verifico se l'accertamento ha dei sub-accertamenti collegati
						 if(isSubAccPresent){
							 // l'accertamento ha dei subAccertamenti, verifico se almeno uno di essi ha disponibilit� ad incassare sufficiente
							 // in caso contrario, l'accertamento non verr� restituito
							 if(isSubAccDisponibilitaIncassare){
								 // almeno uno dei sub-accertmaneit ha disponibilit� ad incassare sufficiente, restituisco l'accertamento
								 // e tutti i suoi sub-accertamenti
								 listaAccertamentoNew.add(accNew);
							 }
						 } else {
							 // l'accertamento non ha dei subAccertamenti, verifico se l'accertamento ha disponibilit� ad incassare sufficiente
							 if(accNew.getDisponibilitaIncassare().compareTo(BigDecimal.ZERO) > 0){
								 // l'accertamento ha disponibilit� ad incassare sufficiente, restituisco l'accertamento
								 listaAccertamentoNew.add(accNew);
							 }
						 }
					} else {
						// non effettuo nessun controllo sulla disponibilità ad incassare, e restituisco l'accertamento
						listaAccertamentoNew.add(accNew);
					}
				}
			}
		}
	}
	
	
	
	/**
	 * Metodo che aggrega i dati in maniera ottimizzata
	 * @param listaSiacTMovgestTsCoinvolti
	 * @return
	 */
	public OttimizzazioneSoggettoDto caricaDatiOttimizzazioneRicercaSoggetto(List<SiacTMovgestTsFin> listaSiacTMovgestTsCoinvolti){
		OttimizzazioneSoggettoDto ottimizzazioneSoggettoDto = new OttimizzazioneSoggettoDto();
		List<SiacTSoggettoFin> distintiSiacTSoggettiCoinvolti = soggettoDao.ricercaBySiacTMovgestPkMassive(listaSiacTMovgestTsCoinvolti);
		List<SiacRMovgestTsSogFin> distintiSiacRSoggettiCoinvolti =movimentoGestioneDao.ricercaByMovGestTsMassive(listaSiacTMovgestTsCoinvolti, "SiacRMovgestTsSogFin");

		//METODO CORE DEI SOGGETTI OTTIMIZATI:
		ottimizzazioneSoggettoDto = caricaDatiOttimizzazioneRicercaSoggettoByDistintiSoggetti(distintiSiacTSoggettiCoinvolti);
		//
		
		//set nel dto:
		ottimizzazioneSoggettoDto.setDistintiSiacRSoggettiCoinvolti(distintiSiacRSoggettiCoinvolti);
		
		//Termino restituendo l'oggetto di ritorno: 
        return ottimizzazioneSoggettoDto;
	}
	
	private boolean isSub(SiacTMovgestTsFin siacTMovgestTs){
		boolean isSub=false;
		if(siacTMovgestTs!=null && siacTMovgestTs.getMovgestTsIdPadre()!=null){
			isSub = true;
		}
		return isSub;
	}
	
	/**
	 * Dato l'id in un movimento ne conta le modifiche presenti
	 * @param movgestTsId
	 * @param datiOperazioneDto
	 * @return
	 */
	public int countModifiche(Integer movgestTsId, DatiOperazioneDto datiOperazioneDto){
		Integer enteProprietarioId = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		Timestamp dataInput = datiOperazioneDto.getTs();
		List<SiacTMovgestTsDetModFin> lista = siacTMovgestTsDetModRepository.findValidiByMovgestTs(enteProprietarioId, movgestTsId, dataInput);
		int count = 0;
		if(lista!=null){
			count = lista.size();
		}
		//Termino restituendo l'oggetto di ritorno: 
        return count;
	}

	private List<IdMovgestSubmovegest> getIdMovgestSubmovegestPaginati(List<IdMovgestSubmovegest> totale, int numPagina, int numRisultatiPerPagina) {
		if (totale == null || numPagina > totale.size()) return null;
		if (numPagina == 0 || numRisultatiPerPagina == 0) return null;
		int start = (numPagina - 1) * numRisultatiPerPagina;
		int end = start + numRisultatiPerPagina;
		if (end > totale.size()) end = totale.size();
		List<IdMovgestSubmovegest> sublist = new ArrayList<IdMovgestSubmovegest>(totale.subList(start, end));
	   //Termino restituendo l'oggetto di ritorno: 
        return sublist;
		
	}
	
	
	private SiacTMovgestTsFin getSiacTMovgestDaAnnullamentoModificaMovGest(DatiOperazioneDto datiOperazioneAnnullamento,Integer idModifica){
		Integer idEnte = datiOperazioneAnnullamento.getSiacTEnteProprietario().getEnteProprietarioId();
		Timestamp now = datiOperazioneAnnullamento.getTs();
		//1. CAPIRE DI CHE TIPO DI MODIFICA SI TRATTA: importo o soggetto
		List<SiacTMovgestTsDetModFin> listaSiacTMovgestTsDetMod = siacTMovgestTsDetModRepository.findListaFromModifica(idEnte,idModifica,now);
		SiacTMovgestTsFin trovato = null;
		SiacTMovgestTsFin siacTMovgestTs = null;
		if(listaSiacTMovgestTsDetMod!=null && listaSiacTMovgestTsDetMod.size()>0){
			siacTMovgestTs = listaSiacTMovgestTsDetMod.get(0).getSiacTMovgestT();
		} else{
			List<SiacRMovgestTsSogModFin> listaRMovgestTsSogMod = siacRMovgestTsSogModRepository.findListaFromModifica(idEnte,idModifica,now);
			siacTMovgestTs = listaRMovgestTsSogMod.get(0).getSiacTMovgestT();
		}
		if(siacTMovgestTs.getMovgestTsIdPadre()!=null){
			siacTMovgestTs = siacTMovgestTsRepository.findOne(siacTMovgestTs.getMovgestTsIdPadre());
		}
		trovato = siacTMovgestTs;
		//Termino restituendo l'oggetto di ritorno: 
        return trovato;
	}
	
	
	private SiacRMovgestTsFin salvaVincoloImpegno(VincoloImpegno vincoloImpegno, DatiOperazioneDto datiOperazione, SiacTMovgestTsFin siacTMovgestTs){
		SiacRMovgestTsFin inserito = null;
		if (vincoloImpegno!=null) {
			
			SiacRMovgestTsFin siacRMovgestTs = null;
			//bisogna vedere se il vincolo esiste gia o no:
			if(vincoloImpegno.getUid()>0){
				//esiste lo carichiamo per aggiornarlo
				siacRMovgestTs = siacRMovgestTsRepository.findOne(vincoloImpegno.getUid());
			}
			
			if(siacRMovgestTs==null){
				//non esiste si tratta di doverlo inserire per la prima volta
				siacRMovgestTs = new SiacRMovgestTsFin();
				// recupero il movgest di accertamento
				SiacTMovgestFin siacTMovgestAcc = siacTMovgestRepository.findOne(vincoloImpegno.getAccertamento().getUid());
				SiacTMovgestTsFin siacTMovgestTsAcc = null;
				if(siacTMovgestAcc!=null && siacTMovgestAcc.getSiacTMovgestTs()!=null && !siacTMovgestAcc.getSiacTMovgestTs().isEmpty()){
					// prendo sempre il primo e l'unico (forse)
					siacTMovgestTsAcc = siacTMovgestAcc.getSiacTMovgestTs().get(0);
				}
				datiOperazione.setOperazione(Operazione.INSERIMENTO);
				siacRMovgestTs = DatiOperazioneUtil.impostaDatiOperazioneLogin(siacRMovgestTs, datiOperazione, siacTAccountRepository);
				siacRMovgestTs.setMovgestTsImporto(vincoloImpegno.getImporto());
				siacRMovgestTs.setSiacTMovgestTsB(siacTMovgestTs);
				siacRMovgestTs.setSiacTMovgestTsA(siacTMovgestTsAcc);
			} else {
				//in aggiornamento puo' variare solo l'importo:
				datiOperazione.setOperazione(Operazione.MODIFICA);
				siacRMovgestTs.setMovgestTsImporto(vincoloImpegno.getImporto());
			}
		
			//salvo sul db:
			siacRMovgestTs = siacRMovgestTsRepository.saveAndFlush(siacRMovgestTs);
		}
		return inserito;
	}
	
	
	
	private MovimentoGestione convertiSiacTMovgestFinToMovimentoGestione(SiacTMovgestFin siacTMovgest, MovimentoGestione movGest){
		
		//MovimentoGestione movGest= map(siacTMovgest, Impegno.class, FinMapId.SiacTMovgest_Impegno);	
		
		List<SiacTMovgestTsFin> siacTMovgestTss = siacTMovgest.getSiacTMovgestTs();
		
		for(SiacTMovgestTsFin siacTMovgestTs : siacTMovgestTss){
		
			// Qui inizia il mapping con le entità che si relazionano al movimento gestione dalla siacTmovgestTs
			if(siacTMovgestTs.getMovgestTsIdPadre()==null && siacTMovgestTs.getDataFineValidita()==null 
					&& CostantiFin.MOVGEST_TS_TIPO_TESTATA.equalsIgnoreCase(siacTMovgestTs.getSiacDMovgestTsTipo().getMovgestTsTipoCode())){
		
					// START ATTRIBUTI ESTRATTI DALLE TABELLE siac_t_movgest_ts + siac_t_movgest_ts_det
					// MAPPO IL SOGGETTO ASSOCIATO
					if(siacTMovgestTs.getSiacRMovgestTsSogs() != null && !siacTMovgestTs.getSiacRMovgestTsSogs().isEmpty()){
						
						for(SiacRMovgestTsSogFin rMovGestTsSog : siacTMovgestTs.getSiacRMovgestTsSogs()){
							
							if(rMovGestTsSog.getDataFineValidita()==null){
																
								Soggetto soggetto = new Soggetto();
								soggetto.setUid(rMovGestTsSog.getSiacTSoggetto().getUid());
								soggetto.setCodiceSoggetto(rMovGestTsSog.getSiacTSoggetto().getSoggettoCode());
								soggetto.setCodiceFiscale(rMovGestTsSog.getSiacTSoggetto().getCodiceFiscale());
								soggetto.setDenominazione(rMovGestTsSog.getSiacTSoggetto().getSoggettoDesc());
								
								movGest.setSoggetto(soggetto);
								
								break;
							}
						}
					}
					
					
					// MAPPO CLASSE_SOGGETTO
					List<SiacRMovgestTsSogclasseFin> listaSiacRMovgestTsSogclasse = siacTMovgestTs.getSiacRMovgestTsSogclasses();
					for(SiacRMovgestTsSogclasseFin siacRMovgestTsSogclasse : listaSiacRMovgestTsSogclasse){
						if(null!=siacRMovgestTsSogclasse && siacRMovgestTsSogclasse.getDataFineValidita()==null){
							ClasseSoggetto classeSoggetto = new ClasseSoggetto();
							classeSoggetto.setCodice(siacRMovgestTsSogclasse.getSiacDSoggettoClasse().getSoggettoClasseCode());
							classeSoggetto.setDescrizione(siacRMovgestTsSogclasse.getSiacDSoggettoClasse().getSoggettoClasseDesc());
							movGest.setClasseSoggetto(classeSoggetto);
						}
					}


					// MAPPO L'IMPORTO_ATTUALE
					List<SiacTMovgestTsDetFin> listaSiacTMovgestTsDet = siacTMovgestTs.getSiacTMovgestTsDets();
					if(listaSiacTMovgestTsDet!=null && listaSiacTMovgestTsDet.size()>0){
						
						//prendo solo i validi
						listaSiacTMovgestTsDet = DatiOperazioneUtil.soloValidi(listaSiacTMovgestTsDet, null);
						
						for (SiacTMovgestTsDetFin siacTMovgestTsDet : listaSiacTMovgestTsDet) {
								
							//controllo x sicurezza di nuovo la validita del dato
							if(siacTMovgestTsDet!=null && DatiOperazioneUtil.isValido(siacTMovgestTsDet, null)){
								
								if(CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE.equalsIgnoreCase(siacTMovgestTsDet.getSiacDMovgestTsDetTipo().getMovgestTsDetTipoCode())){
									movGest.setImportoAttuale(siacTMovgestTsDet.getMovgestTsDetImporto());											
								} 
						
							}
						}
					}

					//MAPPO LO STATO_OPERATIVO_MOVIMENTO_GESTIONE_SPESA	/ ENTRATA							
					List<SiacRMovgestTsStatoFin> listaSiacRMovgestTsStato = siacTMovgestTs.getSiacRMovgestTsStatos();
					if(null!=listaSiacRMovgestTsStato && listaSiacRMovgestTsStato.size() > 0){
						for(SiacRMovgestTsStatoFin siacRMovgestTsStato : listaSiacRMovgestTsStato){
							if(null!=siacRMovgestTsStato && siacRMovgestTsStato.getDataFineValidita()==null){
								if(movGest instanceof Impegno){
									((ImpegnoAbstract) movGest).setStatoOperativoMovimentoGestioneSpesa(siacRMovgestTsStato.getSiacDMovgestStato().getMovgestStatoCode());
									((ImpegnoAbstract) movGest).setDescrizioneStatoOperativoMovimentoGestioneSpesa(siacRMovgestTsStato.getSiacDMovgestStato().getMovgestStatoDesc());
									((ImpegnoAbstract) movGest).setDataStatoOperativoMovimentoGestioneSpesa(siacRMovgestTsStato.getDataInizioValidita());
								} else if(movGest instanceof Accertamento){
									((AccertamentoAbstract) movGest).setStatoOperativoMovimentoGestioneEntrata(siacRMovgestTsStato.getSiacDMovgestStato().getMovgestStatoCode());
									((AccertamentoAbstract) movGest).setDescrizioneStatoOperativoMovimentoGestioneEntrata(siacRMovgestTsStato.getSiacDMovgestStato().getMovgestStatoDesc());
									((AccertamentoAbstract) movGest).setDataStatoOperativoMovimentoGestioneEntrata(siacRMovgestTsStato.getDataInizioValidita());
								}
							}									
						}
					
					}

					
					// MAPPO ATTO AMMINISTRATIVO
					List<SiacRMovgestTsAttoAmmFin> listaSiacRMovgestTsAttoAmm = siacTMovgestTs.getSiacRMovgestTsAttoAmms();
					if(null!=listaSiacRMovgestTsAttoAmm && listaSiacRMovgestTsAttoAmm.size() > 0){
						//estraggo l'unico record valido:
						SiacRMovgestTsAttoAmmFin siacRMovgestTsAttoAmm = DatiOperazioneUtil.getValido(listaSiacRMovgestTsAttoAmm, null);
						if(null!= siacRMovgestTsAttoAmm){
							EntityToModelConverter.settaAttoAmministrativoBase(siacRMovgestTsAttoAmm.getSiacTAttoAmm(), movGest);
						}
					}
					
				}
				// END ATTRIBUTI ESTRATTI DALLE TABELLE siac_t_movgest_ts + siac_t_movgest_ts_det

				
			}
			
			// MAPPO CAPITOLO DI ENTRATA / USCITA, legame che si ha sulla SiacTMovgest e non sulla Ts
			List<SiacRMovgestBilElemFin> listaSiacRMovgestBilElem = siacTMovgest.getSiacRMovgestBilElems();
			
			if(null!=listaSiacRMovgestBilElem && listaSiacRMovgestBilElem.size() > 0){
				SiacRMovgestBilElemFin relazioneValida = DatiOperazioneUtil.getValido(listaSiacRMovgestBilElem, null);
				SiacTBilElemFin siacTBilElem = relazioneValida.getSiacTBilElem();
				
				if(movGest instanceof Impegno){
					
					CapitoloUscitaGestione capUG = new CapitoloUscitaGestione();
					capUG.setUid(siacTBilElem.getUid());
					capUG.setAnnoCapitolo(Integer.parseInt(siacTBilElem.getSiacTBil().getSiacTPeriodo().getAnno()));
					capUG.setNumeroCapitolo(Integer.parseInt(siacTBilElem.getElemCode()));
					capUG.setNumeroArticolo(Integer.parseInt(siacTBilElem.getElemCode2()));
					capUG.setNumeroUEB(Integer.parseInt(siacTBilElem.getElemCode3()));
					
					
					capUG.setStrutturaAmministrativoContabile(mapStrutturaAmministrativaContabileCapitolo(siacTBilElem.getSiacRBilElemClasses()));
					
					((Impegno) movGest).setCapitoloUscitaGestione(capUG);
					
				}else if(movGest instanceof Accertamento){
					
					
					CapitoloEntrataGestione capEG = new CapitoloEntrataGestione();
					capEG.setUid(siacTBilElem.getUid());
					capEG.setAnnoCapitolo(Integer.parseInt(siacTBilElem.getSiacTBil().getSiacTPeriodo().getAnno()));
					capEG.setNumeroCapitolo(Integer.parseInt(siacTBilElem.getElemCode()));
					capEG.setNumeroArticolo(Integer.parseInt(siacTBilElem.getElemCode2()));
					capEG.setNumeroUEB(Integer.parseInt(siacTBilElem.getElemCode3()));
					
					capEG.setStrutturaAmministrativoContabile(mapStrutturaAmministrativaContabileCapitolo(siacTBilElem.getSiacRBilElemClasses()));
					
					((Accertamento) movGest).setCapitoloEntrataGestione(capEG);
					
				}
				
			}
			return movGest;
	}

	protected StrutturaAmministrativoContabile mapStrutturaAmministrativaContabileCapitolo(List<SiacRBilElemClassFin> siacRBilElemClasses) {
		
		StrutturaAmministrativoContabile sac = new StrutturaAmministrativoContabile();
		for(SiacRBilElemClassFin siacRClass: siacRBilElemClasses){
			
			SiacTClassFin siacTClass = siacRClass.getSiacTClass();
			if(siacTClass.getSiacDClassTipo().getClassifTipoCode().equalsIgnoreCase(TipologiaClassificatore.CDC.toString())
					|| siacTClass.getSiacDClassTipo().getClassifTipoCode().equalsIgnoreCase(TipologiaClassificatore.CDR.toString())){
				
				
				sac.setUid(siacTClass.getUid());
				sac.setCodice(siacTClass.getClassifCode());
				sac.setDescrizione(siacTClass.getClassifDesc());
				TipoClassificatore tipoClassificatore = new TipoClassificatore();
				tipoClassificatore.setUid(siacTClass.getUid());
				tipoClassificatore.setCodice(siacTClass.getClassifCode());
				sac.setTipoClassificatore(tipoClassificatore);
				
				break; // prendo la prima, ce ne deve essere solo una 
			}
				
		}
		
		return sac;
	}
	
	
	
	/**
	 * Mappa il movimento gestione (si differenzia dal siacTMovgestEntityToImpegnoModel perchè non si sofferma su tutti gli attirbuti)
	 * Usato principalmente nelle ricerche dell'impegno da altri cdu
	 * @param dtos
	 * @param movimentos
	 * @return
	 */
	public MovimentoGestione convertiDatiTestataMovimentoGestione(SiacTMovgestFin siacTMovgest, List<SiacTMovgestTsFin> siacTMovgestTss, MovimentoGestione movgest, boolean caricaDatiUlteriori ) {
		
		String methodName = "convertiDatiTestataMovimentoGestione";
		
		if(siacTMovgestTss!=null && !siacTMovgestTss.isEmpty()){
		
			for(SiacTMovgestTsFin siacTMovgestTs : siacTMovgestTss){
					
//				System.out.println("ts.uid: "+siacTMovgestTs.getUid());
//				System.out.println("ts.tipocode: "+siacTMovgestTs.getSiacDMovgestTsTipo().getMovgestTsTipoCode());
				
				if(siacTMovgestTs.getDataFineValidita()==null &&
						siacTMovgest.getDataCancellazione() == null && 
								CostantiFin.MOVGEST_TS_TIPO_TESTATA.equalsIgnoreCase(siacTMovgestTs.getSiacDMovgestTsTipo().getMovgestTsTipoCode()))
				{
				  
					// *******************************************************
					// qui setto i soli dati dell'impegno - testata! 
					// *******************************************************
					
					// IMPORTO_INIZIALE - IMPORTO_ATTUALE - UTILIZZABILE
					// per ora sugli importi lascio cosi, qui non c'è una lettura du db
					EntityToModelConverter.setImporti(movgest, siacTMovgestTs);
					
					// TIPO IMPEGNO
					List<SiacRMovgestClassFin> listaSiacRMovgestClass = EntityToModelConverter.setTipoImpegno(movgest, siacTMovgestTs);
					
					
					// DATI TRANSAZIONE ELEMENTARE:
					movgest = (MovimentoGestione) TransazioneElementareEntityToModelConverter.
							convertiDatiTransazioneElementare(movgest, listaSiacRMovgestClass, siacTMovgestTs.getSiacRMovgestTsAttrs());
					
						
					// ATTRIBUTI ESTRATTI DALLE TABELLE siac_r_movgest_ts_attr + siac_t_attr
					// per ora x ottimizzare la ricerca di dettaglio commento, non mi pare servano
					List<SiacRMovgestTsAttrFin> listaSiacRMovgestTsAttr = siacTMovgestTs.getSiacRMovgestTsAttrs();
					convertiAttributi(movgest, listaSiacRMovgestTsAttr);
					
					// PROGETTO
					// per ora x ottimizzare la ricerca di dettaglio commento, non mi pare servano
							
					if(caricaDatiUlteriori){						
						// PROGETTO
						convertiProgetto(movgest, siacTMovgestTs);
						// END PROGETTO
					}
				}
	
			}
		}
				
		
		// CAPITOLO DI USCITA
		movgest = EntityToModelConverter.setChiaveLogicaCapitolo(siacTMovgest, movgest);
		
		return movgest;
	}

	protected void convertiAttributi(MovimentoGestione movgest,
			List<SiacRMovgestTsAttrFin> listaSiacRMovgestTsAttr) {
		
		for(SiacRMovgestTsAttrFin siacRMovgestTsAttr : listaSiacRMovgestTsAttr){
			if(null!=siacRMovgestTsAttr && siacRMovgestTsAttr.getDataFineValidita()==null){
				String codiceAttributo = siacRMovgestTsAttr.getSiacTAttr().getAttrCode();
				AttributoMovimentoGestione attributoMovimentoGestione = CostantiFin.attributoMovimentoGestioneStringToEnum(codiceAttributo);
				switch (attributoMovimentoGestione) {
					case annoCapitoloOrigine:
						if(siacRMovgestTsAttr.getTesto() != null){
							if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
								movgest.setAnnoCapitoloOrigine(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
							}
						}
						break;
						
					case annoOriginePlur:
						if(siacRMovgestTsAttr.getTesto() != null){
							if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
								movgest.setAnnoOriginePlur(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
							}
						
						}										
						break;
						
					case annoRiaccertato:
						if(siacRMovgestTsAttr.getTesto() != null && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
							if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
								movgest.setAnnoRiaccertato(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
							}
							
						}
						break;
						
					case numeroArticoloOrigine:
						if(siacRMovgestTsAttr.getTesto() != null){
							if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
								movgest.setNumeroArticoloOrigine(Integer.parseInt(siacRMovgestTsAttr.getTesto()));	
							}
							
						}									
						break;
						
					case flagDaRiaccertamento:
						if(null!=siacRMovgestTsAttr.getBoolean_()){
							// if("S".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_()) || "s".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
							if(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
								movgest.setFlagDaRiaccertamento(true);
							}else {
								movgest.setFlagDaRiaccertamento(false);
							}
						}												
						break;
						
						
					case flagSoggettoDurc:
						if(null!=siacRMovgestTsAttr.getBoolean_()){
							movgest.setFlagSoggettoDurc(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_()));
						}												
						break;
						
					case FlagCollegamentoAccertamentoFattura:
						if(null!=siacRMovgestTsAttr.getBoolean_()){
							if(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
								((AccertamentoAbstract) movgest).setFlagFattura(true);
							}else {
								((AccertamentoAbstract) movgest).setFlagFattura(false);
							}
						}												
						break;
						
					case FlagCollegamentoAccertamentoCorrispettivo:
						if(null!=siacRMovgestTsAttr.getBoolean_()){
							((AccertamentoAbstract) movgest).setFlagCorrispettivo(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_()));
						}												
						break;
						
					case FlagAttivaGsa:
						if(null!=siacRMovgestTsAttr.getBoolean_()){
							if(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
								movgest.setFlagAttivaGsa(true);
							}else {
								movgest.setFlagAttivaGsa(false);
							}
						}												
						break;
						
					case numeroCapitoloOrigine:
						if(siacRMovgestTsAttr.getTesto() != null){
							if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
								movgest.setNumeroCapitoloOrigine(Integer.parseInt(siacRMovgestTsAttr.getTesto()));	
							}
						}						
						break;
						
					case numeroOriginePlur:
						if(siacRMovgestTsAttr.getTesto() != null){
							if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
								movgest.setNumeroOriginePlur(new BigDecimal(Integer.parseInt(siacRMovgestTsAttr.getTesto())));
							}
							
						}
						break;
						
					case numeroRiaccertato:
						if(siacRMovgestTsAttr.getTesto() != null){
							if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
								movgest.setNumeroRiaccertato(new BigDecimal(Integer.parseInt(siacRMovgestTsAttr.getTesto())));
							}
						}
						break;
						
					case numeroUEBOrigine:
						if(siacRMovgestTsAttr.getTesto() != null){
							if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
								movgest.setNumeroUEBOrigine(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
							}
						
						} 
						break;
					
					case annoFinanziamento:
						if(movgest instanceof Impegno){
							if(siacRMovgestTsAttr.getTesto() != null){
								if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
									((ImpegnoAbstract) movgest).setAnnoFinanziamento(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
								}
							}
						}
						break;
						
					case cig:
						if(movgest instanceof Impegno){
							if(siacRMovgestTsAttr.getTesto() != null){
								if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
									((ImpegnoAbstract) movgest).setCig(siacRMovgestTsAttr.getTesto());
								}
							}
						}
						break;
						
					case numeroAccFinanziamento:
						if(movgest instanceof Impegno){
							if(siacRMovgestTsAttr.getTesto() != null){
								if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
									((ImpegnoAbstract) movgest).setNumeroAccFinanziamento(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
								}
							}
						}
						break;
						
					case validato:
						if(null!=siacRMovgestTsAttr.getBoolean_()){
							// if("S".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_()) || "s".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
							if(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
								movgest.setValidato(true);
							}else {
								movgest.setValidato(false);
							}
						}
						break;

					default:
						break;
				}
			}									
		}
		// END ATTRIBUTI ESTRATTI DALLE TABELLE siac_r_movgest_ts_attr + siac_t_attr
	}
	
	
	/**
	 * 
	 * @param siacTMovgestTs
	 */
	protected void convertiProgetto(MovimentoGestione movgest, SiacTMovgestTsFin siacTMovgestTs) {

		List<SiacRMovgestTsProgrammaFin> listaSiacRMovgestTsProgramma = siacTMovgestTs.getSiacRMovgestTsProgrammas();

		for (SiacRMovgestTsProgrammaFin siacRMovgestTsProgramma : listaSiacRMovgestTsProgramma) {
			if (null != siacRMovgestTsProgramma && siacRMovgestTsProgramma.getDataFineValidita() == null) {
				Progetto progetto = new Progetto();
				progetto.setUid(siacRMovgestTsProgramma.getSiacTProgramma().getProgrammaId());
				progetto.setCodice(siacRMovgestTsProgramma.getSiacTProgramma().getProgrammaCode());
				progetto.setDescrizione(siacRMovgestTsProgramma.getSiacTProgramma().getProgrammaDesc());
				movgest.setProgetto(progetto);
			}
		}
	}
	
	/**
	 * Versione ottimizzata
	 * @param ente
	 * @param richiedente
	 * @param paramRic
	 * @param numPagina
	 * @param numRisPerPagina
	 * @return
	 */
	public List<Accertamento> ricercaAccertamentiSubAccertamentiOPTPerOrdinativoIncasso(Ente ente, 
																						Richiedente richiedente, 
																						ParametroRicercaAccSubAcc paramRic,
																						int numPagina, 
																						int numRisPerPagina,
																						String codiceSoggettoOrdinativo,
																						RicercaAccertamentiSubAccertamentiResponse res){																			
		
		if(null!=paramRic.getTipoProvvedimento() && paramRic.getTipoProvvedimento().getUid()!=0){	
			Timestamp now = new Timestamp(System.currentTimeMillis());
			List<SiacDAttoAmmTipoFin> siacDAttoAmmTipoList = siacDAttoAmmTipoRepository.findDAttoAmmTipoValidoByAttoAmmTipoIdAndEnteId(ente.getUid(), paramRic.getTipoProvvedimento().getUid(), now); 
			if(null!=siacDAttoAmmTipoList && siacDAttoAmmTipoList.size() > 0){
				String codiceTipoProvvedimento = siacDAttoAmmTipoList.get(0).getAttoammTipoCode();
				paramRic.getTipoProvvedimento().setCodice(codiceTipoProvvedimento);
			}
		}
		
		RicercaAccSubAccParamDto paramSearch = (RicercaAccSubAccParamDto) map(paramRic, RicercaAccSubAccParamDto.class);

		if(paramRic.getUidStrutturaAmministrativoContabile()!=null){
			paramSearch.setUidStrutturaAmministrativoContabile(paramRic.getUidStrutturaAmministrativoContabile());
		}
		

		List<IdAccertamentoSubAccertamento> listaIdAccertamenti = accertamentoDao.ricercaAccertamentiSubAccertamentiPerOrdinativoIncasso(ente.getUid(), paramSearch, true);
		
		 List<IdAccertamentoSubAccertamento> listaIdAccertamentiFiltrati = null;
			
		 if(listaIdAccertamenti!=null &&  !listaIdAccertamenti.isEmpty()){
			 listaIdAccertamentiFiltrati =  controlliPerAccertamentoInOrdinativoRicercaEstesa(listaIdAccertamenti, paramSearch, codiceSoggettoOrdinativo);
		 }

		res.setNumRisultati(listaIdAccertamentiFiltrati.size());
		
		List<Accertamento> listaAccertamentoNew = new ArrayList<Accertamento>();
		
		if(listaIdAccertamentiFiltrati != null && listaIdAccertamentiFiltrati.size() > 0){
			
			String clausolaIN =  parseArrayToInAccertamento(listaIdAccertamentiFiltrati);
			
			String[] ids =  clausolaIN.split(",");
			List<String> idsList = new ArrayList<String>(Arrays.asList(ids));
			String idsPaginati  = getIdsPaginati(idsList, numPagina, numRisPerPagina);
			
			// query totale con la IN
			List<SiacTMovgestFin> listaOggettiFinale = movimentoGestioneDao.ricercaSiacTMovgestPerIN(idsPaginati);
			
			if(listaOggettiFinale!=null && !listaOggettiFinale.isEmpty()){
				
				completaAccertamentoSubAccDaRicerca(ente, richiedente,  String.valueOf(paramRic.getAnnoEsercizio()),
						listaAccertamentoNew, listaOggettiFinale);

			}
			
		}	
		
		//Termino restituendo l'oggetto di ritorno: 
	    return listaAccertamentoNew;
	}
	
	private List<IdAccertamentoSubAccertamento> controlliPerAccertamentoInOrdinativoRicercaEstesa(List<IdAccertamentoSubAccertamento> listaIdAccertamenti,
			RicercaAccSubAccParamDto paramSearch, String codiceSoggettoOrdinativo) {

		List<IdAccertamentoSubAccertamento> listaIdAccertamentiResult = new ArrayList<IdAccertamentoSubAccertamento>();

		for (IdAccertamentoSubAccertamento accertamento : listaIdAccertamenti) {

			Integer annoMovimento = paramSearch.getAnnoAccertamento();
			Integer annoEsercizio = paramSearch.getAnnoEsercizio();

			if(annoMovimento <= annoEsercizio){

				if(accertamento.getStato().equals(CostantiFin.MOVGEST_STATO_DEFINITIVO)){

					// se non presente sub allora cerco in accertamenti
					if(accertamento.getIdAccertamentoPadre() == null){

						if(accertamento.getCodiceSoggetto()!=null){

							if(codiceSoggettoOrdinativo.equals(accertamento.getCodiceSoggetto())){
								// aggiungo accertamenti associati al soggetto
								listaIdAccertamentiResult.add(accertamento);		
							}

						}else{

							if(accertamento.getCodiceSoggettoClasse()!=null){
								//Aggiungo gli accrtamenti associati a una classe soggetto
								listaIdAccertamentiResult.add(accertamento);		

							}


						}


					}


					// verifico subaccertamenti collegati
					else{


						if(codiceSoggettoOrdinativo.equals(accertamento.getCodiceSoggetto())){
							// aggiungo accertamenti associati al soggetto
							listaIdAccertamentiResult.add(accertamento);		
						}

					}

				}

			}

		}


		return listaIdAccertamentiResult;
}
	
	/**
	 * Costruisce una clausola di id seperati da virgola per lanciare una query con una IN 
	 * @param obj
	 * @return
	 */
	private String parseArrayToInAccertamento(List<IdAccertamentoSubAccertamento> obj){
	    StringBuffer result = new StringBuffer();
	    HashMap<Integer, Integer> idUtilizzati = new HashMap<Integer, Integer>();
    	boolean first = true;
    	if(obj!=null && obj.size()>0){
			for (IdAccertamentoSubAccertamento ogg : obj){
				if (ogg.getIdAccertamento()!=null && !idUtilizzati.containsKey(ogg.getIdAccertamento())){
					if(!first){
						result.append(",");
					}
					result.append(ogg.getIdAccertamento());
					idUtilizzati.put(ogg.getIdAccertamento(), ogg.getIdAccertamento());
					first = false;
				}
			}
    	}
	   //Termino restituendo l'oggetto di ritorno: 
        return result.toString();
	  }
	
}
			
