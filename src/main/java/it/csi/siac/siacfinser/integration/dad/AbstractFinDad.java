/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.SerializationUtils;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocLiquidazione;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siaccommon.util.threadlocal.ThreadLocalUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;
import it.csi.siac.siaccorser.model.Account;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfinser.CommonUtil;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.StringUtilsFin;
import it.csi.siac.siacfinser.TimingUtils;
import it.csi.siac.siacfinser.business.service.util.NumericUtils;
import it.csi.siac.siacfinser.integration.dad.datacontainer.DisponibilitaMovimentoGestioneContainer;
import it.csi.siac.siacfinser.integration.dao.carta.SiacRCartacontAttrRepository;
import it.csi.siac.siacfinser.integration.dao.carta.SiacRCartacontDetAttrRepository;
import it.csi.siac.siacfinser.integration.dao.carta.SiacRCartacontEsteraAttrRepository;
import it.csi.siac.siacfinser.integration.dao.common.SiacDAttoAmmTipoFinRepository;
import it.csi.siac.siacfinser.integration.dao.common.SiacRAccountRuoloOpRepository;
import it.csi.siac.siacfinser.integration.dao.common.SiacRGruppoAccountRepository;
import it.csi.siac.siacfinser.integration.dao.common.SiacRGruppoRuoloOpRepository;
import it.csi.siac.siacfinser.integration.dao.common.SiacRRuoloOpAzioneRepository;
import it.csi.siac.siacfinser.integration.dao.common.SiacTAccountFinRepository;
import it.csi.siac.siacfinser.integration.dao.common.dto.AttributoTClassInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.AttributoTClassInfoMassiveDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.CaricaDatiVisibilitaSacCapitoloDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ChiaveLogicaCapitoloDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.CodificaImportoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoSalvataggioTransazioneElmentare;
import it.csi.siac.siacfinser.integration.dao.common.dto.MovGestInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OggettoDellAttributoTClass;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneAvanzoVincoliDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneModalitaPagamentoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneModificheMovimentoGestioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneMovGestDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneOrdinativoPagamentoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneSoggettoDto;
import it.csi.siac.siacfinser.integration.dao.liquidazione.LiquidazioneFinDao;
import it.csi.siac.siacfinser.integration.dao.liquidazione.SiacRLiquidazioneAttrRepository;
import it.csi.siac.siacfinser.integration.dao.liquidazione.SiacRLiquidazioneClassRepository;
import it.csi.siac.siacfinser.integration.dao.liquidazione.SiacRLiquidazioneMovgestRepository;
import it.csi.siac.siacfinser.integration.dao.liquidazione.SiacRLiquidazioneOrdRepository;
import it.csi.siac.siacfinser.integration.dao.liquidazione.SiacTLiquidazioneFinRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.AccertamentoDao;
import it.csi.siac.siacfinser.integration.dao.movgest.ImpegnoDao;
import it.csi.siac.siacfinser.integration.dao.movgest.MovimentoGestioneFinDao;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacDClassTipoRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacDSiopeAssenzaMotivazioneFinRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacDSiopeTipoDebitoFinRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacRBilElemSacVisibilitaFinRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacRBilFaseOperativaRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacRBilStatoOpRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacRMovgestClassRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacRMovgestTsAttrRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTAttoAmmFinRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTBilElemFinRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTBilFinRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTClassFinRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTMovgestRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTMovgestTsDetRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTMovgestTsRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTProgressivoRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.OrdinativoDao;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacROrdinativoAttrRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacROrdinativoClassRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacROrdinativoStatoRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacROrdinativoTsMovgestTsRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacTOrdinativoTsDetRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacDAttrTipoRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacRSoggettoAttrModRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacRSoggettoAttrRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacTAttrRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SoggettoDao;
import it.csi.siac.siacfinser.integration.dao.subdoc.SubdocumentoDaoCustom;
import it.csi.siac.siacfinser.integration.entity.SiacDAttoAmmTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDAttrTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDClassTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDModificaTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDSiopeAssenzaMotivazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacDSiopeTipoDebitoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRAccountRuoloOpFin;
import it.csi.siac.siacfinser.integration.entity.SiacRAttoAmmClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacRAttoAmmStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRAttrBaseFin;
import it.csi.siac.siacfinser.integration.entity.SiacRBilFaseOperativaFin;
import it.csi.siac.siacfinser.integration.entity.SiacRCartacontAttrFin;
import it.csi.siac.siacfinser.integration.entity.SiacRCartacontDetAttrFin;
import it.csi.siac.siacfinser.integration.entity.SiacRCartacontEsteraAttrFin;
import it.csi.siac.siacfinser.integration.entity.SiacRClassBaseFin;
import it.csi.siac.siacfinser.integration.entity.SiacRComuneProvinciaFin;
import it.csi.siac.siacfinser.integration.entity.SiacRComuneRegioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacRDocStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRFormaGiuridicaFin;
import it.csi.siac.siacfinser.integration.entity.SiacRGruppoAccountFin;
import it.csi.siac.siacfinser.integration.entity.SiacRGruppoRuoloOpFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneAttoAmmFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneAttrFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneOrdFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRModificaStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRModpagOrdineFin;
import it.csi.siac.siacfinser.integration.entity.SiacRModpagStatoFin;
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
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoAttrFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoModpagFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoTsMovgestTFin;
import it.csi.siac.siacfinser.integration.entity.SiacRProgrammaAttrFin;
import it.csi.siac.siacfinser.integration.entity.SiacRRuoloOpAzioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoAttrFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoAttrModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoClasseFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoOnereFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoRelazFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoRelazStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggrelModpagFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSubdocLiquidazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSubdocOrdinativoTFin;
import it.csi.siac.siacfinser.integration.entity.SiacRVincoloAttrFin;
import it.csi.siac.siacfinser.integration.entity.SiacRVincoloBilElemFin;
import it.csi.siac.siacfinser.integration.entity.SiacTAttoAmmFin;
import it.csi.siac.siacfinser.integration.entity.SiacTAttrFin;
import it.csi.siac.siacfinser.integration.entity.SiacTAvanzovincoloFin;
import it.csi.siac.siacfinser.integration.entity.SiacTBilElemFin;
import it.csi.siac.siacfinser.integration.entity.SiacTBilFin;
import it.csi.siac.siacfinser.integration.entity.SiacTCartacontDetFin;
import it.csi.siac.siacfinser.integration.entity.SiacTCartacontEsteraFin;
import it.csi.siac.siacfinser.integration.entity.SiacTCartacontFin;
import it.csi.siac.siacfinser.integration.entity.SiacTClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacTComuneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTDocFin;
import it.csi.siac.siacfinser.integration.entity.SiacTEnteProprietarioFin;
import it.csi.siac.siacfinser.integration.entity.SiacTIndirizzoSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTLiquidazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModificaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModpagFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModpagModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsDetFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsDetModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.SiacTNazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoTFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoTsDetFin;
import it.csi.siac.siacfinser.integration.entity.SiacTPersonaFisicaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTPersonaGiuridicaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTProgrammaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTProgressivoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTRecapitoSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSoggettoModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTVincoloFin;
import it.csi.siac.siacfinser.integration.entity.base.SiacConModificaStato;
import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;
import it.csi.siac.siacfinser.integration.helper.ModalitaPagamentoSoggettoHelper;
import it.csi.siac.siacfinser.integration.util.DatiOperazioneUtil;
import it.csi.siac.siacfinser.integration.util.EntityToModelConverter;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.integration.util.TipoOggettoConClassificatori;
import it.csi.siac.siacfinser.integration.util.TransazioneElementareEntityToModelConverter;
import it.csi.siac.siacfinser.integration.utility.threadlocal.UseClockTimeThreadLocal;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.TransazioneElementare;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;
import it.csi.siac.siacfinser.model.siopeplus.SiopeTipoDebito;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

public abstract class AbstractFinDad extends ExtendedBaseFinDad {
	
	@Autowired
	protected SiacTLiquidazioneFinRepository siacTLiquidazioneRepository;
	
	@Autowired
	protected AccertamentoDao accertamentoDao;
	
    @Autowired
    private SubdocumentoDaoCustom subdocumentoDaoCustom;
	
	@Autowired
	protected ImpegnoDao impegnoDao;
	
	@Autowired
	protected SoggettoDao soggettoDao;
	
	@Autowired
	protected MovimentoGestioneFinDao movimentoGestioneDao;
	
	@Autowired
	private LiquidazioneFinDao liquidazioneDao;
	
	@Autowired
	private SiacRAccountRuoloOpRepository siacRAccountRuoloOpRepository;

	@Autowired
	private SiacRRuoloOpAzioneRepository siacRRuoloOpAzioneRepository;

	@Autowired
	private SiacTProgressivoRepository siacTProgressivoRepository;

	@Autowired
	private SiacRGruppoAccountRepository siacRGruppoAccountRepository;

	@Autowired
	private SiacRGruppoRuoloOpRepository siacRGruppoRuoloOpRepository;

	@Autowired
	protected SiacTAccountFinRepository siacTAccountRepository;

	@Autowired
	protected SiacTAttoAmmFinRepository siacTAttoAmmRepository;
	
	@Autowired
	protected SiacDAttoAmmTipoFinRepository siacDAttoAmmTipoFinRepository;

	@Autowired
	protected SiacTBilElemFinRepository siacTBilElemRepository;
	
	@Autowired
	protected SiacRBilElemSacVisibilitaFinRepository siacRBilElemSacVisibilitaFinRepository;
	
	@Autowired
	protected SiacDClassTipoRepository siacDClassTipoRepository;

	@Autowired
	protected SiacTClassFinRepository siacTClassRepository;

	@Autowired
	protected SiacRMovgestClassRepository siacRMovgestClassRepository;

	@Autowired
	protected SiacRLiquidazioneClassRepository siacRLiquidazioneClassRepository;

	// e salvataggio "t_attr"
	@Autowired
	protected SiacRMovgestTsAttrRepository siacRMovgestTsAttrRepository;

	@Autowired
	protected SiacRLiquidazioneAttrRepository siacRLiquidazioneAttrRepository;

	@Autowired
	protected SiacROrdinativoClassRepository siacROrdinativoClassRepository;

	@Autowired
	protected SiacROrdinativoAttrRepository siacROrdinativoAttrRepository;

	@Autowired
	protected SiacRSoggettoAttrRepository siacRSoggettoAttrRepository;

	@Autowired
	protected SiacRSoggettoAttrModRepository siacRSoggettoAttrModRepository;

	@Autowired
	protected SiacTAttrRepository siacTAttrRepository;

	@Autowired
	protected SiacDAttrTipoRepository siacDAttrTipoRepository;

	@Autowired
	protected SiacTBilFinRepository siacTBilRepository;

	@Autowired
	protected SiacRBilStatoOpRepository siacRBilStatoOpRepository;

	@Autowired
	protected SiacRBilFaseOperativaRepository siacRBilFaseOperativaRepository;

	@Autowired
	protected SiacRCartacontAttrRepository siacRCartacontAttrRepository;

	@Autowired
	protected SiacRCartacontEsteraAttrRepository siacRCartacontEsteraAttrRepository;

	@Autowired
	protected SiacRCartacontDetAttrRepository siacRCartacontDetAttrRepository;

	@Autowired
	protected SiacROrdinativoStatoRepository siacROrdinativoStatoRepository;

	@Autowired
	protected SiacROrdinativoTsMovgestTsRepository siacROrdinativoTsMovgestTsRepository;

	@Autowired
	protected SiacTOrdinativoTsDetRepository siacTOrdinativoTsDetRepository;

	@Autowired
	protected SiacTMovgestTsDetRepository siacTMovgestTsDetRepository;

	@Autowired
	protected SiacRLiquidazioneOrdRepository siacRLiquidazioneOrdRepository;

	@Autowired
	protected SiacRLiquidazioneMovgestRepository siacRLiquidazioneMovgestRepository;
	
	@Autowired
	protected SiacTMovgestTsRepository siacTMovgestTsRepository;
	
	@Autowired
	protected SiacTMovgestRepository siacTMovgestRepository;
	
	@Autowired
	protected SiacDSiopeAssenzaMotivazioneFinRepository siacDSiopeAssenzaMotivazioneFinRepository;
	
	@Autowired
	protected SiacDSiopeTipoDebitoFinRepository siacDSiopeTipoDebitoFinRepository;
	
    @Autowired
    protected OrdinativoDao ordinativoDao;

	@Autowired
	protected ApplicationContext appCtx;

	@PersistenceContext
	protected EntityManager entityManager;
	
	protected ModalitaPagamentoSoggettoHelper modalitaPagamentoSoggettoHelper;
	
	private static final UseClockTimeThreadLocal USE_CLOCK_TIME = (UseClockTimeThreadLocal) ThreadLocalUtil.registerThreadLocal(UseClockTimeThreadLocal.class);

	
	/**
	 * Inizializza l'helper per la modalita pagamento soggetto
	 * iniettandoci il contesto spring
	 */
	protected void initModalitaPagamentoSoggettoHelper(){
		if(modalitaPagamentoSoggettoHelper==null){
			//MODALITA PAGAMENTO SOGGETTO HELPER:
			modalitaPagamentoSoggettoHelper = new ModalitaPagamentoSoggettoHelper(appCtx);
			modalitaPagamentoSoggettoHelper.init();
		}
	}
	
	@PostConstruct
	public void init(){
		//MODALITA PAGAMENTO SOGGETTO HELPER:
		initModalitaPagamentoSoggettoHelper();
	}

	protected Session getSession() {
		Session session = entityManager.unwrap(org.hibernate.Session.class);
		return session;
	}
	
	protected <T extends SiacTBase> void entitiesRefresh(List<T> l) {
		if(l!=null && l.size()>0){
			for(T o: l){
				entityRefresh(o);
			}
		}
	}

	protected <T extends SiacTBase> void entityRefresh(T o) {
		Session session = getSession();
		session.refresh(o);
		// entityManager.refresh(o);//oppure e' meglio cosi?
	}

	/**
	 * Aggiorna il time stamp in un oggetto datiOperazioneDto
	 * 
	 * E' nata questa esigenza nel caso di reintroito ordinativo di pagamento
	 * dove un servizio orchestra tanti servizi passandosi di mano un datiOperazioneDto
	 * e deve "avanzare" la data per evitare di non trovare entita con date inizio validita'
	 * piu nuove di quella che era stata inizializzata in datiOperazioneDto
	 * 
	 * @param datiOperazioneDto
	 * @return
	 */
	public DatiOperazioneDto aggiornaTimeStamp(DatiOperazioneDto datiOperazioneDto){
		long currMillisec = getCurrentMilliseconds();
		datiOperazioneDto.setCurrMillisec(currMillisec);
        return datiOperazioneDto;
	}
	
	
	protected long getCurrentMilliseconds(){
		Boolean usaClockTs = USE_CLOCK_TIME.get();
		if(usaClockTs!=null && usaClockTs == true){
			//CASO PARTICOLARE (es. reintroito service)
			return getClockTimestamp();
		} else {
			//CASO TIPICO
			return getCurrentTimestamp();
		}
	}
	
	/**
	 * Ritorna il timing dell'istante corrente preso da db
	 * @return
	 */
	protected long getClockTimestamp(){
		return ((Timestamp) entityManager.createNativeQuery("SELECT CLOCK_TIMESTAMP()").getSingleResult()).getTime();
	}
	
	/**
	 * Ritorna il timing dell'istante di inizio transazione
	 * @return
	 */
	protected long getCurrentTimestamp(){
		return ((Timestamp) entityManager.createNativeQuery("SELECT CURRENT_TIMESTAMP").getSingleResult()).getTime();
	}
	
	/**
	 * 
	 * Ritorna un clone dell'oggetto in input nel quale il time stamp
	 * viene avanzato all'istante attuale (CLOCK_TIMESTAMP)
	 * 
	 * @param datiOperazione
	 * @return
	 */
	public DatiOperazioneDto avanzaAClockTime(DatiOperazioneDto datiOperazione){
		DatiOperazioneDto datiOperazioneAvanzaTs = clone(datiOperazione);
		datiOperazioneAvanzaTs.setCurrMillisec(getClockTimestamp());
		return datiOperazioneAvanzaTs;
	}

	protected <T extends SiacTBase> void adjustDates(T siacEntity, Date d) {
		siacEntity.setDataCreazione(d);
		siacEntity.setDataInizioValidita(d);
		siacEntity.setDataModifica(d);
	}



	protected long currentTimeMillis()
	{
		return System.currentTimeMillis();
	}
	
	/**
	 * SIAC-7619
	 * Metodo che permette di ottenere una lista 
	 * con la prima entita valida trovata
	 * 
	 * In caso non vi sia nessuna entita valida verra' fornita una lista vuota
	 * 
	 * @param List<T> lista
	 * @return List<T> listaValidata
	 */
	protected <T extends SiacTBase> List<T> ottieniListaConEnitaValida(List<T> lista){
		List<T> listaValidata = new ArrayList<T>();
		for (T oggettoIterato : lista) {
			if(oggettoIterato.isEntitaValida()) {
				listaValidata.add(oggettoIterato);
				break;
			}
		}
		return listaValidata;
	}
	
	/**
	 * Metodo che permette di effettuare la copia di un oggetto, cambiando
	 * l'allocazione di memoria dell'oggetto copiato
	 * 
	 * @param o
	 * @return T
	 */
	@SuppressWarnings("unchecked")
	public static <T> T clone(T o) {
		return (T) SerializationUtils.deserialize(SerializationUtils.serialize(o));
	}

	/**
	 * Si occupa di caricare i dati dell'atto amministrativo indicato
	 * 
	 * @param attoRicevutoDaFrontEnd
	 * @param idEnte
	 * @return
	 */
	public AttoAmministrativo ricaricaAttoAmministrativo(
			AttoAmministrativo attoRicevutoDaFrontEnd, int idEnte) {
		AttoAmministrativo caricato = null;
		
		if (attoRicevutoDaFrontEnd != null
				&& attoRicevutoDaFrontEnd.getTipoAtto() != null) {
			
			// EVENTUALE STRUTTURA AMMINISTRATIVA:
			Integer idStrutturaAmmCont = null;
			if (attoRicevutoDaFrontEnd.getStrutturaAmmContabile() != null && attoRicevutoDaFrontEnd.getStrutturaAmmContabile().getUid() > 0) {
				idStrutturaAmmCont = attoRicevutoDaFrontEnd.getStrutturaAmmContabile().getUid();
			}
			
			log.debug("ricaricaAttoAmministrativo"," Ricerco per anno: "+ attoRicevutoDaFrontEnd.getAnno()+ ",  tipoAtto.codice:  " +
					attoRicevutoDaFrontEnd.getTipoAtto().getCodice()+ ", numero: " + ", idStruttura: "+ attoRicevutoDaFrontEnd.getNumero()+idStrutturaAmmCont);
			caricato = getAttoAministrativo(idEnte, attoRicevutoDaFrontEnd.getAnno(), attoRicevutoDaFrontEnd.getTipoAtto().getCodice(),
					attoRicevutoDaFrontEnd.getNumero(), idStrutturaAmmCont);
			
			//log.debug("ricaricaAttoAmministrativo"," Risultato uid: "+ caricato.getUid());
		}
		// Termino restituendo l'oggetto di ritorno:
		return caricato;
	}

	/**
	 * carica l'atto indicato con ente, anno, tipo atto e numero atto
	 * 
	 * @param idEnte
	 * @param attoAmmAnno
	 * @param codeTipoAtto
	 * @param attoAmmNumero
	 * @return
	 */
	// public AttoAmministrativo getAttoAministrativo(int idEnte,int
	// attoAmmAnno, String codeTipoAtto,int attoAmmNumero){
	// AttoAmministrativo attoAmministrativoEstratto = null;
	// String anno = Integer.toString(attoAmmAnno);
	// List<SiacTAttoAmmFin>
	// listaSiacTAttoAmm=siacTAttoAmmRepository.getValidoByNumeroAndAnnoAndTipo(idEnte,
	// anno, getNow(),codeTipoAtto, attoAmmNumero);
	// if(listaSiacTAttoAmm==null || listaSiacTAttoAmm.size()==0 &&
	// codeTipoAtto.length()==1){
	// //FIX per codice sb errato da front end
	// codeTipoAtto = StringUtils.formattaConZeriInTesta(codeTipoAtto, 2);
	// listaSiacTAttoAmm=siacTAttoAmmRepository.getValidoByNumeroAndAnnoAndTipo(idEnte,
	// anno, getNow(),codeTipoAtto, attoAmmNumero);
	// }
	// if(listaSiacTAttoAmm!=null && listaSiacTAttoAmm.size()==1 &&
	// listaSiacTAttoAmm.get(0)!=null){
	// attoAmministrativoEstratto = new AttoAmministrativo();
	// attoAmministrativoEstratto.setAnno(attoAmmAnno);
	// attoAmministrativoEstratto.setNumero(attoAmmNumero);
	// attoAmministrativoEstratto.setUid(listaSiacTAttoAmm.get(0).getUid());
	// TipoAtto tipoAtto = new TipoAtto();
	// tipoAtto.setCodice(codeTipoAtto);
	// attoAmministrativoEstratto.setTipoAtto(tipoAtto);
	//
	// List<SiacRAttoAmmStatoFin>
	// listaRAttoAmmStato=listaSiacTAttoAmm.get(0).getSiacRAttoAmmStatos();
	// if (listaRAttoAmmStato!=null && listaRAttoAmmStato.size()>0) {
	// List<SiacRAttoAmmStatoFin>
	// listaRAttoAmmStatoValidi=DatiOperazioneUtil.soloValidi(listaRAttoAmmStato,
	// getNow());
	// if (listaRAttoAmmStatoValidi!=null && listaRAttoAmmStatoValidi.size()==1)
	// {
	// attoAmministrativoEstratto.setStatoOperativo(listaRAttoAmmStatoValidi.get(0).getSiacDAttoAmmStato().getAttoammStatoCode());
	// }
	// }
	// }
	// //Termino restituendo l'oggetto di ritorno:
	// return attoAmministrativoEstratto;
	// }

	/**
	 * Ricarica l'atto indicato, serve per i check di esistenza
	 * 
	 * @param attoAmministrativo
	 * @return
	 */
	public AttoAmministrativo getAttoAministrativo(int idEnte,
			AttoAmministrativo attoAmministrativo) {
		Integer idStrutturaAmministrativa = null;
		if (attoAmministrativo.getStrutturaAmmContabile() != null
				&& attoAmministrativo.getStrutturaAmmContabile().getUid() > 0) {
			idStrutturaAmministrativa = attoAmministrativo
					.getStrutturaAmmContabile().getUid();
		}
		return getAttoAministrativo(idEnte, attoAmministrativo.getAnno(),
				attoAmministrativo.getTipoAtto().getCodice(),
				attoAmministrativo.getNumero(), idStrutturaAmministrativa);
	}

	/**
	 * carica l'atto indicato con ente, anno, tipo atto e numero atto e
	 * eventuale struttura amministrativa
	 * 
	 * @param idEnte
	 * @param attoAmmAnno
	 * @param codeTipoAtto
	 * @param attoAmmNumero
	 * @return
	 */
	public AttoAmministrativo getAttoAministrativo(int idEnte, int attoAmmAnno,
			String codeTipoAtto, int attoAmmNumero,
			Integer idStrutturaAmministrativa) {
		AttoAmministrativo attoAmministrativoEstratto = null;

		AttoAmministrativo attoAmm = new AttoAmministrativo();

		attoAmm.setNumero(attoAmmNumero);
		attoAmm.setAnno(attoAmmAnno);
		TipoAtto tipoAtto = new TipoAtto();
		tipoAtto.setCodice(codeTipoAtto);
		attoAmm.setTipoAtto(tipoAtto);

		// eventuale struttura amministrativa:
		if (idStrutturaAmministrativa != null) {
			StrutturaAmministrativoContabile strutturaAmmContabile = new StrutturaAmministrativoContabile();
			strutturaAmmContabile.setUid(idStrutturaAmministrativa);
			attoAmm.setStrutturaAmmContabile(strutturaAmmContabile);
		}

		// CHIAMO IL METODO "CORE":
		SiacTAttoAmmFin siatTAttoAmm = getSiacTAttoAmmFromAttoAmministrativo(
				attoAmm, idEnte);

		if (siatTAttoAmm != null) {
		
			attoAmministrativoEstratto = attoAmm;
			attoAmministrativoEstratto.setUid(siatTAttoAmm.getUid());
			SiacRAttoAmmStatoFin rAttoAmmStato = DatiOperazioneUtil.getValido(
					siatTAttoAmm.getSiacRAttoAmmStatos(), null);
			if (rAttoAmmStato != null) {
				attoAmministrativoEstratto.setStatoOperativo(rAttoAmmStato
						.getSiacDAttoAmmStato().getAttoammStatoCode());
			}
			
//			// mi salvo l'informazione dell'allegato (per ora solo l'uid) che puo essere utile in alcuni servizi
//			// vedi aggiorna liquidazione
//			if(siatTAttoAmm.getSiacTAttoAllegatos()!=null && !siatTAttoAmm.getSiacTAttoAllegatos().isEmpty()){
//				AllegatoAtto allegatoAtto = new AllegatoAtto();
//				
//				for (SiacTAllegatoAttoFin tAllegatoAtto: siatTAttoAmm.getSiacTAttoAllegatos()) {
//					if(tAllegatoAtto.getDataFineValidita()== null && tAllegatoAtto.getDataCancellazione()== null){
//				
//						allegatoAtto.setUid(tAllegatoAtto.getUid());
//						break;
//					}
//				}
//				
//				attoAmministrativoEstratto.setAllegatoAtto(allegatoAtto);
//			}
		} else {
			attoAmministrativoEstratto = null;
		}
		// Termino restituendo l'oggetto di ritorno:
		return attoAmministrativoEstratto;
	}

	/**
	 * Metodo generalizzato per salvare un attributo t_class per oggetti di
	 * vario tipo: movimenti gestione, ordinativi e liquidazioni
	 * 
	 * @param datiOperazioneDto
	 * @param attributoInfo
	 * @param codeSelezionato
	 * @param codeTipoClass
	 * @return
	 */
	public SiacRClassBaseFin salvaAttributoTClass(
			DatiOperazioneDto datiOperazioneDto,
			AttributoTClassInfoDto attributoInfo, String codeSelezionato,
			String codeTipoClass) {
		SiacRClassBaseFin ritorno = null;
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario()
				.getEnteProprietarioId();
		List<SiacDClassTipoFin> l = siacDClassTipoRepository.findByCode(idEnte,
				datiOperazioneDto.getTs(), codeTipoClass);
		ritorno = salvaAttributoTClassInternal(datiOperazioneDto,
				attributoInfo, codeSelezionato, l);
		// Termino restituendo l'oggetto di ritorno:
		return ritorno;
	}
	
	public void salvaAttributoTClassMassive(DatiOperazioneDto datiOperazioneDto,AttributoTClassInfoMassiveDto attributoInfo, String codeSelezionato,String codeTipoClass) {
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		List<SiacDClassTipoFin> l = siacDClassTipoRepository.findByCode(idEnte,datiOperazioneDto.getTs(), codeTipoClass);
		salvaAttributoTClassMassivoInternal(datiOperazioneDto,attributoInfo, codeSelezionato, l);
	}

	/**
	 * Metodo generalizzato per salvare un attributo t_class per oggetti di
	 * vario tipo: movimenti gestione, ordinativi e liquidazioni
	 * 
	 * validitaRispettoAnnoBilancio se true considera i record validi all'anno di bilancio se diverso da anno di sistema (esempio siope)
	 * validitaRispettoAnnoBilancio se false considera i record validi alla data di sistema
	 * 
	 * @param datiOperazioneDto
	 * @param attributoInfo
	 * @param codeSelezionato
	 * @param codeTipoClassMultiplo
	 * @param validitaRispettoAnnoBilancio
	 * @return
	 */
	public <T extends SiacTEnteBase, E extends JpaRepository> List<SiacRClassBaseFin> salvaAttributoTClassMultiplo(DatiOperazioneDto datiOperazioneDto,
																							AttributoTClassInfoDto attributoInfo, String codeSelezionato,
																												List<String> codeTipoClassMultiplo,
																												boolean validitaRispettoAnnoBilancio,
																												boolean obbligatorio) 
	{
		
		List<SiacRClassBaseFin> ritorno = new ArrayList<SiacRClassBaseFin>();
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		
		if (codeTipoClassMultiplo != null && codeTipoClassMultiplo.size() > 0) {
			
			E repositoryRelazione = stabilisciRepositoryRClass(attributoInfo);
			List<SiacRMovgestClassFin> old = getRClassEsistentiMultiplo(attributoInfo, datiOperazioneDto, codeTipoClassMultiplo);
			
			// INVALIDO GLI (eventuali) OLD:
			DatiOperazioneUtil.cancellaRecords(old, repositoryRelazione, datiOperazioneDto, siacTAccountRepository);

			// restituisce il 31/12 dell'anno di Bilancio
			// Timestamp tstampFineAnno =
			// TimingUtils.getUltimoGiornoAnnoBilancio(datiOperazioneDto.getAnnoBilancio());

			// leggo l'attributo e salvo la relazione 
			
			int annoDiSistema = TimingUtils.getAnnoCorrente();
			int annoBilancio = datiOperazioneDto.getAnnoBilancio();
			boolean validiAdAnnoBilancio = false;
			if(validitaRispettoAnnoBilancio && annoBilancio!=annoDiSistema){
				validiAdAnnoBilancio = true;
			}
			
			List<SiacTClassFin> lclass = null;
			if(validiAdAnnoBilancio){
				//COMPORTAMENTO PARTICOLARE: i record che sono stati validi almeno una volta nell'anno di bilancio (es. per siope)
				
				Timestamp inizioAnno =  TimingUtils.getStartYearTs(annoBilancio);
				Timestamp fineAnno =  TimingUtils.getEndYearTs(annoBilancio);
				
				lclass = siacTClassRepository.findByTipoCodesAndEnteAndSelezionato(idEnte, codeTipoClassMultiplo, codeSelezionato,inizioAnno,fineAnno);	
			} else {
				//COMPORTAMENTO STANDARD: i record validi rispetto ad ora
				lclass = siacTClassRepository.findByTipoCodesAndEnteAndSelezionato(idEnte,datiOperazioneDto.getTs(), codeTipoClassMultiplo, codeSelezionato);	
			}
			
			if (lclass != null && lclass.size() > 0) {
				
				for (SiacTClassFin siacTClass : lclass) {
					if (codeSelezionato.equals(siacTClass.getClassifCode())) {
						datiOperazioneDto.setOperazione(Operazione.INSERIMENTO);
						// SAVE:
						SiacRClassBaseFin itSaved = saveRClass(attributoInfo,datiOperazioneDto, siacTClass);
						ritorno.add(itSaved);
					}
				}
			}else{
				if(obbligatorio){
					//TIPICAMENTE SOLO PER PDC
					
					//lancio un errore, se non viene inserito il legame xchè non si trova il pdc (potrebbe essere che abbia dt fine validita)l'entità (tipo impegno) viene inserita senza 
					throw new BusinessException("Attenzione, non è stato trovato un pdc valido con codice ["+codeSelezionato+"].");
				}
			}
		}
		// Termino restituendo l'oggetto di ritorno:
		return ritorno;
	}
	
	/**
	 * SIAC-8281
	 * 
	 * Metodo per la gestione della disponibilita' con movimenti su anni successivi rispetto a quello di bilancio
	 * 
	 * @param valoreModifica
	 * @param capitoloEscGest
	 * @param accertamento
	 * 
	 * @return La nuova disponibilita' ad accertare
	 */
	protected BigDecimal calcolaNuovaDisponibilitaAccertareSuPiuAnniCapitolo(BigDecimal valoreModifica,
			CapitoloEntrataGestione capitoloEscGest, Accertamento accertamento) {
		//passo a zero il valore di defualt in modo che se i controlli formali non passino
		//venga generato l'errore <FIN_INF_0062 - Superamento della disponibilita' >
		BigDecimal vecchiaDispAccCap = BigDecimal.ZERO;
		
		if(NumericUtils.valorizzatoEMaggioreDiZero(capitoloEscGest.getBilancioAnno()) 
				&& NumericUtils.valorizzatoEMaggioreDiZero(accertamento.getAnnoMovimento())) {
			
			switch (accertamento.getAnnoMovimento() - capitoloEscGest.getBilancioAnno()) {
				case 0:
					//anno del movimento uguale a quello di bilancio
					vecchiaDispAccCap = capitoloEscGest.getImportiCapitoloEG().getDisponibilitaAccertareAnno1();
					break;
				case 1:
					//anno del movimento successivo a quello di bilancio (+1)
					vecchiaDispAccCap = capitoloEscGest.getImportiCapitoloEG().getDisponibilitaAccertareAnno2();
					break;
				case 2:
					//anno del movimento successivo a quello di bilancio (+2)
					vecchiaDispAccCap = capitoloEscGest.getImportiCapitoloEG().getDisponibilitaAccertareAnno3();
					break;
				default:
					//c'e' qualcosa che non va, il caso potrebbe essere sporco
					break;
			}
			
		}
		//nuovaDispAccCap = vecchiaDispAccCap - valoreModifica
		return vecchiaDispAccCap.subtract(valoreModifica);
	}

	public static void main(String[] args) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 31);
		cal.set(Calendar.MONTH, 11);// -1 as month is zero-based
		cal.set(Calendar.YEAR, 2014);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

	}

	/**
	 * Serve per vedere se un certo attributo t_class (per un oggetto di vario
	 * tipo) e' stato modificato
	 * 
	 * @param datiOperazioneDto
	 * @param attributoInfo
	 * @param codeSelezionato
	 * @param codeTipoClass
	 * @return
	 */
	public boolean isModificatoAttributoTClass(
			DatiOperazioneDto datiOperazioneDto,
			AttributoTClassInfoDto attributoInfo, String codeSelezionato,
			String codeTipoClass) {
		ArrayList<String> codici = new ArrayList<String>();
		codici.add(codeTipoClass);
		return isModificatoAttributoTClassMultiplo(datiOperazioneDto,
				attributoInfo, codeSelezionato, codici);
	}

	/**
	 * Serve per vedere se un certo attributo t_class (per un oggetto di vario
	 * tipo) e' stato modificato
	 * 
	 * @param datiOperazioneDto
	 * @param attributoInfo
	 * @param codeSelezionato
	 * @param codeTipoClassMultiplo
	 * @return
	 */
	public <T extends SiacTEnteBase, E extends JpaRepository> boolean isModificatoAttributoTClassMultiplo(
			DatiOperazioneDto datiOperazioneDto,
			AttributoTClassInfoDto attributoInfo, String codeSelezionato,
			List<String> codeTipoClassMultiplo) {
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario()
				.getEnteProprietarioId();
		boolean isModificato = false;
		if (codeTipoClassMultiplo != null && codeTipoClassMultiplo.size() > 0) {
			List<SiacRMovgestClassFin> old = getRClassEsistentiMultiplo(
					attributoInfo, datiOperazioneDto, codeTipoClassMultiplo);
			List<SiacTClassFin> lclassOld = new ArrayList<SiacTClassFin>();
			if (old != null && old.size() > 0) {
				for (SiacRMovgestClassFin it : old) {
					lclassOld.add(it.getSiacTClass());
				}
			}
			List<SiacTClassFin> lclassNew = siacTClassRepository
					.findByTipoCodesAndEnteAndSelezionato(idEnte,
							datiOperazioneDto.getTs(), codeTipoClassMultiplo,
							codeSelezionato);
			if (!CommonUtil.entrambiVuotiOrEntrambiConElementi(lclassOld,
					lclassNew)) {
				// uno dei due e' vuoto e l'altro non lo e' --> sono sicuramenti
				// diversi
				isModificato = true;
				return isModificato;
			} else if (CommonUtil.entrambiConElementi(lclassOld, lclassNew)) {
				// c'e' la possibilita' che ci sia una modifica
				boolean tuttiPresenti = DatiOperazioneUtil
						.tuttiPresentiNellAltraLista(lclassOld, lclassNew);
				if (!tuttiPresenti) {
					isModificato = true;
					return isModificato;
				}
			}
		}
		return isModificato;
	}

	/**
	 * Routine "core" interna a salvaAttributoTClass
	 * 
	 * @param datiOperazioneDto
	 * @param attributoInfo
	 * @param codeSelezionato
	 * @param l
	 * @return
	 */
	private <T extends SiacTEnteBase, E extends JpaRepository> SiacRClassBaseFin salvaAttributoTClassInternal(
			DatiOperazioneDto datiOperazioneDto,
			AttributoTClassInfoDto attributoInfo, String codeSelezionato,
			List<SiacDClassTipoFin> l) {
		SiacRClassBaseFin ritorno = null;
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario()
				.getEnteProprietarioId();
		// stabiliamo il repository:
		E repositoryRelazione = stabilisciRepositoryRClass(attributoInfo);
		if (l != null && l.size() > 0) {
			for (SiacDClassTipoFin tipo : l) {
				String codeForSearch = StringUtilsFin.toLowerSafe(codeSelezionato);
				List<SiacTClassFin> lclass = siacTClassRepository
						.findByCodeAndTipoAndEnte(idEnte,
								datiOperazioneDto.getTs(), codeForSearch,
								tipo.getClassifTipoId());
				List<T> old = getRClassEsistenti(attributoInfo,
						datiOperazioneDto, tipo);
				// INVALIDIAMO EVENTUALE OLDS:
				DatiOperazioneUtil.cancellaRecords(old, repositoryRelazione,
						datiOperazioneDto, siacTAccountRepository);
				// ///////////////////////////
				// SAVE:
				if (lclass != null && lclass.size() > 0) {
					// mi aspetto un solo risultato
					SiacTClassFin siacTClass = lclass.get(0);
					datiOperazioneDto.setOperazione(Operazione.INSERIMENTO);
					ritorno = saveRClass(attributoInfo, datiOperazioneDto,
							siacTClass);
				}
			}
		}
		// Termino restituendo l'oggetto di ritorno:
		return ritorno;
	}

	
	/**
	 * Routine "core" interna a salvaAttributoTClass
	 * 
	 * @param datiOperazioneDto
	 * @param attributoInfo
	 * @param codeSelezionato
	 * @param l
	 * @return
	 */
	private <T extends SiacTEnteBase, E extends JpaRepository> void salvaAttributoTClassMassivoInternal(
			DatiOperazioneDto datiOperazioneDto,
			AttributoTClassInfoMassiveDto attributoInfo, String codeSelezionato,
			List<SiacDClassTipoFin> l) {
		SiacRClassBaseFin ritorno = null;
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario()
				.getEnteProprietarioId();
		// stabiliamo il repository:
		E repositoryRelazione = stabilisciRepositoryRClass(attributoInfo);
		if (l != null && l.size() > 0) {
			for (SiacDClassTipoFin tipo : l) {
				String codeForSearch = StringUtilsFin.toLowerSafe(codeSelezionato);
				
				List<SiacTClassFin> lclass = siacTClassRepository.findByCodeAndTipoAndEnte(idEnte,datiOperazioneDto.getTs(), codeForSearch,tipo.getClassifTipoId());
				
				List<T> old = getRClassEsistenti(attributoInfo,datiOperazioneDto, tipo);
				
				// INVALIDIAMO EVENTUALE OLDS:
				DatiOperazioneUtil.cancellaRecordsOttimizzato(old, repositoryRelazione,datiOperazioneDto, siacTAccountRepository);
				// ///////////////////////////
				// SAVE:
				if (lclass != null && lclass.size() > 0) {
					// mi aspetto un solo risultato
					SiacTClassFin siacTClass = lclass.get(0);
					datiOperazioneDto.setOperazione(Operazione.INSERIMENTO);
					saveRClassMassive(attributoInfo, datiOperazioneDto,siacTClass);
				}
			}
		}
	}
	
	
	/**
	 * Routine "core" interna a salvaAttributoTClass
	 * 
	 * @param attributoInfo
	 * @param datiOperazioneDto
	 * @param siacTClass
	 * @return
	 */
	private SiacRClassBaseFin saveRClass(AttributoTClassInfoDto attributoInfo,
			DatiOperazioneDto datiOperazioneDto, SiacTClassFin siacTClass) {
		SiacRClassBaseFin ritorno = null;
		OggettoDellAttributoTClass tipoOgg = attributoInfo.getTipoOggetto();
		datiOperazioneDto.setOperazione(Operazione.INSERIMENTO);
		if (OggettoDellAttributoTClass.T_MOVGEST_TS.equals(tipoOgg)) {
			SiacRMovgestClassFin siacRMovgestClass = null;
			SiacTMovgestTsFin siacTMovgestTs = attributoInfo
					.getSiacTMovgestTs();
			siacRMovgestClass = saveMovgesClass(siacRMovgestClass, siacTClass,
					siacTMovgestTs, datiOperazioneDto);
			ritorno = siacRMovgestClass;
		} else if (OggettoDellAttributoTClass.T_LIQUIDAZIONE.equals(tipoOgg)) {
			SiacRLiquidazioneClassFin siacRLiquidazioneClass = null;
			SiacTLiquidazioneFin siacTLiquidazione = attributoInfo
					.getSiacTLiquidazione();
			siacRLiquidazioneClass = saveLiquidazioneClass(
					siacRLiquidazioneClass, siacTClass, siacTLiquidazione,
					datiOperazioneDto);
			ritorno = siacRLiquidazioneClass;
		} else if (OggettoDellAttributoTClass.T_ORDINATIVO.equals(tipoOgg)) {
			SiacROrdinativoClassFin siacROrdinativoClass = null;
			SiacTOrdinativoFin siacTOrdinativo = attributoInfo
					.getSiacTOrdinativo();
			siacROrdinativoClass = saveOrdinativoClass(siacROrdinativoClass,
					siacTClass, siacTOrdinativo, datiOperazioneDto);
			ritorno = siacROrdinativoClass;
		}
		// Termino restituendo l'oggetto di ritorno:
		return ritorno;
	}
	
	private void saveRClassMassive(AttributoTClassInfoMassiveDto attributoInfo,DatiOperazioneDto datiOperazioneDto, SiacTClassFin siacTClass) {
		OggettoDellAttributoTClass tipoOgg = attributoInfo.getTipoOggetto();
		datiOperazioneDto.setOperazione(Operazione.INSERIMENTO);
		if (OggettoDellAttributoTClass.T_MOVGEST_TS.equals(tipoOgg)) {
			List<SiacRMovgestClassFin> siacRMovgestClass = null;
			List<SiacTMovgestTsFin> siacTMovgestTs = attributoInfo.getSiacTMovgestTs();
			siacRMovgestClass = saveMovgesClassMassive(siacTClass,siacTMovgestTs, datiOperazioneDto);
		} else if (OggettoDellAttributoTClass.T_LIQUIDAZIONE.equals(tipoOgg)) {
			//implementare se dovesse servire
		} else if (OggettoDellAttributoTClass.T_ORDINATIVO.equals(tipoOgg)) {
			//implementare se dovesse servire
		}
	}

	/**
	 * Routine che determina il repository di relazione verso il quale accedere
	 * per un generico attributo t_class
	 * 
	 * @param attributoInfo
	 * @return
	 */
	private <E extends JpaRepository> E stabilisciRepositoryRClass(
			AttributoTClassInfoDto attributoInfo) {
		// stabiliamo il repository:
		E repositoryRelazione = null;
		OggettoDellAttributoTClass tipoOgg = attributoInfo.getTipoOggetto();
		if (OggettoDellAttributoTClass.T_MOVGEST_TS.equals(tipoOgg)) {
			repositoryRelazione = (E) siacRMovgestClassRepository;
		} else if (OggettoDellAttributoTClass.T_LIQUIDAZIONE.equals(tipoOgg)) {
			repositoryRelazione = (E) siacRLiquidazioneClassRepository;
		} else if (OggettoDellAttributoTClass.T_ORDINATIVO.equals(tipoOgg)) {
			repositoryRelazione = (E) siacROrdinativoClassRepository;
		}
		// Termino restituendo l'oggetto di ritorno:
		return repositoryRelazione;
	}
	
	private <E extends JpaRepository> E stabilisciRepositoryRClass(
			AttributoTClassInfoMassiveDto attributoInfo) {
		// stabiliamo il repository:
		E repositoryRelazione = null;
		OggettoDellAttributoTClass tipoOgg = attributoInfo.getTipoOggetto();
		if (OggettoDellAttributoTClass.T_MOVGEST_TS.equals(tipoOgg)) {
			repositoryRelazione = (E) siacRMovgestClassRepository;
		} else if (OggettoDellAttributoTClass.T_LIQUIDAZIONE.equals(tipoOgg)) {
			repositoryRelazione = (E) siacRLiquidazioneClassRepository;
		} else if (OggettoDellAttributoTClass.T_ORDINATIVO.equals(tipoOgg)) {
			repositoryRelazione = (E) siacROrdinativoClassRepository;
		}
		// Termino restituendo l'oggetto di ritorno:
		return repositoryRelazione;
	}

	/**
	 * Legge e restituisce le relazioni presenti sul db per il generico
	 * attributo t_class indicato
	 * 
	 * @param attributoInfo
	 * @param datiOperazioneDto
	 * @param codeTipoClassMultiplo
	 * @return
	 */
	private <T extends SiacTEnteBase> List<T> getRClassEsistentiMultiplo(
			AttributoTClassInfoDto attributoInfo,
			DatiOperazioneDto datiOperazioneDto,
			List<String> codeTipoClassMultiplo) {
		List<T> old = null;
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario()
				.getEnteProprietarioId();
		Integer idOggetto = attributoInfo.getIdOggetto();
		OggettoDellAttributoTClass tipoOgg = attributoInfo.getTipoOggetto();
		if (OggettoDellAttributoTClass.T_MOVGEST_TS.equals(tipoOgg)) {
			old = (List<T>) siacRMovgestClassRepository
					.findByTipoCodesAndEnteAndMovgestTs(idEnte,
							datiOperazioneDto.getTs(), codeTipoClassMultiplo,
							idOggetto);
		} else if (OggettoDellAttributoTClass.T_LIQUIDAZIONE.equals(tipoOgg)) {
			old = (List<T>) siacRLiquidazioneClassRepository
					.findByTipoCodesAndEnteAndLiquidazione(idEnte,
							datiOperazioneDto.getTs(), codeTipoClassMultiplo,
							idOggetto);
		} else if (OggettoDellAttributoTClass.T_ORDINATIVO.equals(tipoOgg)) {
			old = (List<T>) siacROrdinativoClassRepository
					.findByTipoCodesAndEnteAndOrdinativoId(idEnte,
							datiOperazioneDto.getTs(), codeTipoClassMultiplo,
							idOggetto);
		}
		// Termino restituendo l'oggetto di ritorno:
		return old;
	}

	
	protected DatiOperazioneDto impostaBilancioSeNull(DatiOperazioneDto datioperazione, AttributoTClassInfoDto attributoInfo) {
		// stabiliamo il repository:
		OggettoDellAttributoTClass tipoOgg = attributoInfo.getTipoOggetto();
		if(datioperazione!=null && datioperazione.getAnnoBilancio()==null){
			Integer annoBilancio = null;
			SiacTBilFin siacTBil = null;
			if (OggettoDellAttributoTClass.T_MOVGEST_TS.equals(tipoOgg) 
					&& attributoInfo.getSiacTMovgestTs()!=null && attributoInfo.getSiacTMovgestTs().getSiacTMovgest()!=null) {
				siacTBil = attributoInfo.getSiacTMovgestTs().getSiacTMovgest().getSiacTBil();
			} else if (OggettoDellAttributoTClass.T_LIQUIDAZIONE.equals(tipoOgg) && attributoInfo.getSiacTLiquidazione()!=null) {
				siacTBil = attributoInfo.getSiacTLiquidazione().getSiacTBil();
			} else if (OggettoDellAttributoTClass.T_ORDINATIVO.equals(tipoOgg) && attributoInfo.getSiacTOrdinativo()!=null){
				siacTBil = attributoInfo.getSiacTOrdinativo().getSiacTBil();
			}
			if(siacTBil!=null && siacTBil.getSiacTPeriodo()!=null){
				annoBilancio = new Integer(siacTBil.getSiacTPeriodo().getAnno());
			}
			datioperazione.setAnnoBilancio(annoBilancio);
		}
		return datioperazione;
	}
	
	
	protected List<SiacTMovgestFin> getSiacTMovgestFinBySiacTMovgestTsFin(List<SiacTMovgestTsFin> listaTs){
		List<SiacTMovgestFin> ritorno = new ArrayList<SiacTMovgestFin>();
		if(!StringUtilsFin.isEmpty(listaTs)){
			for(SiacTMovgestTsFin it : listaTs){
				if(it!=null){
					ritorno.add(it.getSiacTMovgest());
				}
			}
		}
		return ritorno;
	}

	/**
	 * Legge e restituisce le relazioni presenti sul db per il generico
	 * attributo t_class indicato
	 * 
	 * @param attributoInfo
	 * @param datiOperazioneDto
	 * @param tipo
	 * @return
	 */
	protected <T extends SiacTEnteBase> List<T> getRClassEsistenti(
			AttributoTClassInfoDto attributoInfo,
			DatiOperazioneDto datiOperazioneDto, SiacDClassTipoFin tipo) {
		List<T> old = null;
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario()
				.getEnteProprietarioId();
		Integer idOggetto = attributoInfo.getIdOggetto();
		OggettoDellAttributoTClass tipoOgg = attributoInfo.getTipoOggetto();
		if (OggettoDellAttributoTClass.T_MOVGEST_TS.equals(tipoOgg)) {
			old = (List<T>) siacRMovgestClassRepository
					.findByTipoAndEnteAndMovgestTs(idEnte,
							datiOperazioneDto.getTs(), tipo.getClassifTipoId(),
							idOggetto);
		} else if (OggettoDellAttributoTClass.T_LIQUIDAZIONE.equals(tipoOgg)) {
			old = (List<T>) siacRLiquidazioneClassRepository
					.findByTipoAndEnteAndLiquidazione(idEnte,
							datiOperazioneDto.getTs(), tipo.getClassifTipoId(),
							idOggetto);
		} else if (OggettoDellAttributoTClass.T_ORDINATIVO.equals(tipoOgg)) {
			old = (List<T>) siacROrdinativoClassRepository
					.findByTipoAndEnteAndOrdinativoId(idEnte,
							datiOperazioneDto.getTs(), tipo.getClassifTipoId(),
							idOggetto);
		}
		// Termino restituendo l'oggetto di ritorno:
		return old;
	}
	
	
	protected <T extends SiacTEnteBase> List<T> getRClassEsistenti(AttributoTClassInfoMassiveDto attributoInfo,DatiOperazioneDto datiOperazioneDto, SiacDClassTipoFin tipo) {
		List<T> old = null;
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario()
				.getEnteProprietarioId();
		List<Integer> idOggetti = attributoInfo.getIdOggetti();
		OggettoDellAttributoTClass tipoOgg = attributoInfo.getTipoOggetto();
		if (OggettoDellAttributoTClass.T_MOVGEST_TS.equals(tipoOgg)) {
			old = (List<T>) siacRMovgestClassRepository.findByTipoAndEnteAndMovgestTsList(idEnte,datiOperazioneDto.getTs(), tipo.getClassifTipoId(),idOggetti);
		} else if (OggettoDellAttributoTClass.T_LIQUIDAZIONE.equals(tipoOgg)) {
			//per ora non mi serve, implementare se servisse
		} else if (OggettoDellAttributoTClass.T_ORDINATIVO.equals(tipoOgg)) {
			//per ora non mi serve, implementare se servisse
		}
		// Termino restituendo l'oggetto di ritorno:
		return old;
	}

	/**
	 * Senza accedere (esplicitamente) ai repository legge dentro ai model
	 * passati le relazioni
	 * 
	 * @param attributoInfo
	 * @param classifTipoCode
	 * @return
	 */
	protected <T extends SiacRClassBaseFin> T getRClassEsistentiSenzaQueryDb(
			AttributoTClassInfoDto attributoInfo, String classifTipoCode) {
		T trovato = null;
		OggettoDellAttributoTClass tipoOgg = attributoInfo.getTipoOggetto();
		if (OggettoDellAttributoTClass.T_MOVGEST_TS.equals(tipoOgg)) {
			// TODO
		} else if (OggettoDellAttributoTClass.T_LIQUIDAZIONE.equals(tipoOgg)) {
			// TODO
		} else if (OggettoDellAttributoTClass.T_ORDINATIVO.equals(tipoOgg)) {
			SiacTOrdinativoFin siacTOrdinativo = attributoInfo
					.getSiacTOrdinativo();
			List<SiacROrdinativoClassFin> siacROrdinativoClass = siacTOrdinativo
					.getSiacROrdinativoClasses();
			if (siacROrdinativoClass != null && siacROrdinativoClass.size() > 0) {
				siacROrdinativoClass = DatiOperazioneUtil.soloValidi(
						siacROrdinativoClass, getNow());
				for (SiacROrdinativoClassFin it : siacROrdinativoClass) {
					if (it != null
							&& it.getSiacTClass() != null
							&& it.getSiacTClass().getSiacDClassTipo() != null
							&& StringUtilsFin.sonoUgualiTrimmed(it.getSiacTClass()
									.getSiacDClassTipo().getClassifTipoCode(),
									classifTipoCode)) {
						trovato = (T) it;
						break;
					}
				}
			}

		}
		// Termino restituendo l'oggetto di ritorno:
		return trovato;
	}

	/**
	 * Metodo generalizzato da richiamare per salvare la transazione elementare
	 * 
	 * @param attributoInfo
	 * @param datiOperazioneDto
	 * @param transazioneElementare
	 * @return
	 */
	public EsitoSalvataggioTransazioneElmentare salvaTransazioneElementare(
			AttributoTClassInfoDto attributoInfo,
			DatiOperazioneDto datiOperazioneDto,
			TransazioneElementare transazioneElementare) {

		EsitoSalvataggioTransazioneElmentare ritorno = new EsitoSalvataggioTransazioneElmentare();
		// List<AttributoTClassInfoDto> saved = new
		// ArrayList<AttributoTClassInfoDto>();

		//GENNAIO 2017, in alcuni casi datiOperazione arriva senza anno di bilancio e crea null pointer:
		datiOperazioneDto = impostaBilancioSeNull(datiOperazioneDto, attributoInfo);
		//
		
		List<SiacRClassBaseFin> saved = new ArrayList<SiacRClassBaseFin>();

		// TRANSAZIONE ELEMENTARE:
		// Missione
		// Programma
		// Piano conti Finanziario --> pianoDeiConti
		// Conto Economico
		// COFOG
		// Codifica Transazione Europea
		// CUP
		// Ricorrente
		// SIOPE
		// ASL --> perimetroSanitarioSpesaSelezionato
		// Programma Pol. Reg. Unitaria --> politicaRegionaleSelezionato

		boolean entrata = TransazioneElementareEntityToModelConverter
				.isEntrata(transazioneElementare);

		// MISSIONE:
		if (!entrata) {
			saved.add(salvaAttributoTClass(datiOperazioneDto, attributoInfo,
					transazioneElementare.getCodMissione(),
					CostantiFin.D_CLASS_TIPO_MISSIONE));
		}

		// PROGRAMMA:
		if (!entrata) {
			saved.add(salvaAttributoTClass(datiOperazioneDto, attributoInfo,
					transazioneElementare.getCodProgramma(),
					CostantiFin.D_CLASS_TIPO_PROGRAMMA));
		}

		addAll(saved,
				salvaAttributoTClassMultiplo(datiOperazioneDto, attributoInfo,
						transazioneElementare.getCodPdc(),
						CostantiFin.getCodiciPianoDeiConti(),false,true));

		// PIANO DEI CONTI ECONOMICO:
		// CR-2023 si elimina
		// addAll(saved, salvaAttributoTClassMultiplo(datiOperazioneDto,
		// attributoInfo, transazioneElementare.getCodContoEconomico(),
		// CostantiFin.getCodiciPianoDeiContiEconomico()));

		// Cofog
		if (!entrata) {
			saved.add(salvaAttributoTClass(datiOperazioneDto, attributoInfo,
					transazioneElementare.getCodCofog(),
					CostantiFin.D_CLASS_TIPO_GRUPPO_COFOG));
		}
		// Cofog

		// TRANSAZIONE UE SPESA:
		if (entrata) {
			saved.add(salvaAttributoTClass(datiOperazioneDto, attributoInfo,
					transazioneElementare.getCodTransazioneEuropeaSpesa(),
					CostantiFin.D_CLASS_TIPO_TRANSAZIONE_UE_ENTRATA));
		} else {
			saved.add(salvaAttributoTClass(datiOperazioneDto, attributoInfo,
					transazioneElementare.getCodTransazioneEuropeaSpesa(),
					CostantiFin.D_CLASS_TIPO_TRANSAZIONE_UE_SPESA));
		}

		// CUP:
		if (!entrata) {
			String cup = transazioneElementare.getCup();
			SiacRAttrBaseFin siacRAttrBaseCupSaved = salvaAttributoCup(
					attributoInfo, datiOperazioneDto, cup);
			ritorno.setSiacRAttrBaseCupSaved(siacRAttrBaseCupSaved);
		}

		// Ricorrente spesa
		if (entrata) {
			saved.add(salvaAttributoTClass(datiOperazioneDto, attributoInfo,
					transazioneElementare.getCodRicorrenteSpesa(),
					CostantiFin.D_CLASS_TIPO_CLASSE_RICORRENTE_ENTRATA));
		} else {
			saved.add(salvaAttributoTClass(datiOperazioneDto, attributoInfo,
					transazioneElementare.getCodRicorrenteSpesa(),
					CostantiFin.D_CLASS_TIPO_CLASSE_RICORRENTE_SPESA));
		}

		//

		// SIOPE
		if (!entrata) {
			
			//per il siope si considerano i record che sono stati validi nell'anno di bilancio
			//se anno bilancio diverso da anno corrente
			boolean validitaRispettoAnnoBilancio = true;
			
			addAll(saved,
					salvaAttributoTClassMultiplo(datiOperazioneDto,
							attributoInfo, transazioneElementare.getCodSiope(),
							CostantiFin.getCodiciSiopeSpesa(),validitaRispettoAnnoBilancio,false));
		} else {
			
			//per il siope si considerano i record che sono stati validi nell'anno di bilancio
			//se anno bilancio diverso da anno corrente
			boolean validitaRispettoAnnoBilancio = true;
			
			addAll(saved,
					salvaAttributoTClassMultiplo(datiOperazioneDto,
							attributoInfo, transazioneElementare.getCodSiope(),
							CostantiFin.getCodiciSiopeEntrata(),validitaRispettoAnnoBilancio,false));
		}

		// Capitoli perimetro sanitario spesa
		if (entrata) {
			saved.add(salvaAttributoTClass(datiOperazioneDto, attributoInfo,
					transazioneElementare.getCodCapitoloSanitarioSpesa(),
					CostantiFin.D_CLASS_TIPO_CLASSE_PERIMETRO_SANITARIO_ENTRATA));
		} else {
			saved.add(salvaAttributoTClass(datiOperazioneDto, attributoInfo,
					transazioneElementare.getCodCapitoloSanitarioSpesa(),
					CostantiFin.D_CLASS_TIPO_CLASSE_PERIMETRO_SANITARIO_SPESA));
		}

		// PROGRAMMA_POLITICHE_REGIONALI_UNITARIE:
		if (!entrata) {
			saved.add(salvaAttributoTClass(
					datiOperazioneDto,
					attributoInfo,
					transazioneElementare.getCodPrgPolReg(),
					CostantiFin.D_CLASS_TIPO_PROGRAMMA_POLITICHE_REGIONALI_UNITARIE));
		}

		// i classficatori hanno un metodo centralizzato dedicato:
		addAll(saved,
				salvaClassificatori(attributoInfo, datiOperazioneDto,
						transazioneElementare));

		// SIAC-8612
		List<SiacRClassBaseFin> savedNotNull = new ArrayList<SiacRClassBaseFin>();
		for (SiacRClassBaseFin siacRClassBaseFin : saved) {
			if (siacRClassBaseFin != null) {
				savedNotNull.add(siacRClassBaseFin);
			} 
		}
		
		ritorno.settClassSaved(savedNotNull);

		return ritorno;
	}

	/**
	 * Metodo generalizzato per salvare i classificatori
	 * 
	 * @param attributoInfo
	 * @param datiOperazioneDto
	 * @param transazioneElementare
	 * @return
	 */
	public List<SiacRClassBaseFin> salvaClassificatori(
			AttributoTClassInfoDto attributoInfo,
			DatiOperazioneDto datiOperazioneDto,
			TransazioneElementare transazioneElementare) {
		List<SiacRClassBaseFin> saved = new ArrayList<SiacRClassBaseFin>();
		if (TransazioneElementareEntityToModelConverter.getTipoClassificatori(
				transazioneElementare).equals(
				TipoOggettoConClassificatori.ACCERTAMENTO)) {
			// if(entrata){
			// accertamento
			// classficatore fin 16
			saved.add(salvaAttributoTClass(datiOperazioneDto, attributoInfo,
					transazioneElementare.getCodClassGen16(),
					CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_16));
			// classficatore fin 17
			saved.add(salvaAttributoTClass(datiOperazioneDto, attributoInfo,
					transazioneElementare.getCodClassGen17(),
					CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_17));
			// classficatore fin 18
			saved.add(salvaAttributoTClass(datiOperazioneDto, attributoInfo,
					transazioneElementare.getCodClassGen18(),
					CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_18));
			// classficatore fin 19
			saved.add(salvaAttributoTClass(datiOperazioneDto, attributoInfo,
					transazioneElementare.getCodClassGen19(),
					CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_19));
			// classficatore fin 20
			saved.add(salvaAttributoTClass(datiOperazioneDto, attributoInfo,
					transazioneElementare.getCodClassGen20(),
					CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_20));

		} else if (TransazioneElementareEntityToModelConverter
				.getTipoClassificatori(transazioneElementare).equals(
						TipoOggettoConClassificatori.IMPEGNO)) {
			// classficatore fin 11
			saved.add(salvaAttributoTClass(datiOperazioneDto, attributoInfo,
					transazioneElementare.getCodClassGen11(),
					CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_11));
			// classficatore fin 12
			saved.add(salvaAttributoTClass(datiOperazioneDto, attributoInfo,
					transazioneElementare.getCodClassGen12(),
					CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_12));
			// classficatore fin 13
			saved.add(salvaAttributoTClass(datiOperazioneDto, attributoInfo,
					transazioneElementare.getCodClassGen13(),
					CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_13));
			// classficatore fin 14
			saved.add(salvaAttributoTClass(datiOperazioneDto, attributoInfo,
					transazioneElementare.getCodClassGen14(),
					CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_14));
			// classficatore fin 15
			saved.add(salvaAttributoTClass(datiOperazioneDto, attributoInfo,
					transazioneElementare.getCodClassGen15(),
					CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_15));

		} else if (TransazioneElementareEntityToModelConverter
				.getTipoClassificatori(transazioneElementare).equals(
						TipoOggettoConClassificatori.ORDINATIVO_PAGAMENTO)) {

			// classficatore fin 21
			saved.add(salvaAttributoTClass(datiOperazioneDto, attributoInfo,
					transazioneElementare.getCodClassGen11(),
					CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_21));
			// classficatore fin 22
			saved.add(salvaAttributoTClass(datiOperazioneDto, attributoInfo,
					transazioneElementare.getCodClassGen12(),
					CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_22));
			// classficatore fin 23
			saved.add(salvaAttributoTClass(datiOperazioneDto, attributoInfo,
					transazioneElementare.getCodClassGen13(),
					CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_23));
			// classficatore fin 24
			saved.add(salvaAttributoTClass(datiOperazioneDto, attributoInfo,
					transazioneElementare.getCodClassGen14(),
					CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_24));
			// classficatore fin 25
			saved.add(salvaAttributoTClass(datiOperazioneDto, attributoInfo,
					transazioneElementare.getCodClassGen15(),
					CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_25));

		} else if (TransazioneElementareEntityToModelConverter
				.getTipoClassificatori(transazioneElementare).equals(
						TipoOggettoConClassificatori.ORDINATIVO_INCASSO)) {
			// classficatore fin 16
			saved.add(salvaAttributoTClass(datiOperazioneDto, attributoInfo,
					transazioneElementare.getCodClassGen16(),
					CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_26));
			// classficatore fin 17
			saved.add(salvaAttributoTClass(datiOperazioneDto, attributoInfo,
					transazioneElementare.getCodClassGen17(),
					CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_27));
			// classficatore fin 18
			saved.add(salvaAttributoTClass(datiOperazioneDto, attributoInfo,
					transazioneElementare.getCodClassGen18(),
					CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_28));
			// classficatore fin 19
			saved.add(salvaAttributoTClass(datiOperazioneDto, attributoInfo,
					transazioneElementare.getCodClassGen19(),
					CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_29));
			// classficatore fin 20
			saved.add(salvaAttributoTClass(datiOperazioneDto, attributoInfo,
					transazioneElementare.getCodClassGen20(),
					CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_30));
		}
		// Termino restituendo l'oggetto di ritorno:
		return saved;
		// ///
	}

	/**
	 * Metodo generalizzare per testare se la transazione elementare ha subito
	 * modifiche (ovvero almeno uno dei suoi attributi modificato)
	 * 
	 * @param attributoInfo
	 * @param transazioneElementare
	 * @param datiOperazioneDto
	 * @return
	 */
	public <T extends SiacTEnteBase> boolean isModificataTransazioneElementare(
			AttributoTClassInfoDto attributoInfo,
			TransazioneElementare transazioneElementare,
			DatiOperazioneDto datiOperazioneDto) {
		boolean isModificato = false;
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario()
				.getEnteProprietarioId();
		Integer idOggetto = attributoInfo.getIdOggetto();
		Timestamp now = datiOperazioneDto.getTs();

		boolean entrata = TransazioneElementareEntityToModelConverter
				.isEntrata(transazioneElementare);

		if (transazioneElementare instanceof SubImpegno) {
			// CUP:
			isModificato = isAttributoModificato(
					((SubImpegno) transazioneElementare).getCup(),
					CostantiFin.T_ATTR_CODE_CUP, datiOperazioneDto, attributoInfo);
			if (isModificato) {
				return isModificato;
			}
		}

		// PIANO DEI CONTI:
		isModificato = isModificatoAttributoTClassMultiplo(datiOperazioneDto,
				attributoInfo, transazioneElementare.getCodPdc(),
				CostantiFin.getCodiciPianoDeiConti());
		if (isModificato) {
			return isModificato;
		}

		// PIANO DEI CONTI ECONOMICO:
		// CR-2023 si elimina
		// isModificato = isModificatoAttributoTClassMultiplo(datiOperazioneDto,
		// attributoInfo, transazioneElementare.getCodContoEconomico(),
		// CostantiFin.getCodiciPianoDeiContiEconomico());
		// if(isModificato){
		// return isModificato;
		// }

		// TRANSAZIONE UE SPESA:
		if (entrata) {
			isModificato = isModificatoAttributoTClass(datiOperazioneDto,
					attributoInfo,
					transazioneElementare.getCodTransazioneEuropeaSpesa(),
					CostantiFin.D_CLASS_TIPO_TRANSAZIONE_UE_ENTRATA);
		} else {
			isModificato = isModificatoAttributoTClass(datiOperazioneDto,
					attributoInfo,
					transazioneElementare.getCodTransazioneEuropeaSpesa(),
					CostantiFin.D_CLASS_TIPO_TRANSAZIONE_UE_SPESA);
		}
		if (isModificato) {
			return isModificato;
		}

		// Cofog
		isModificato = isModificatoAttributoTClass(datiOperazioneDto,
				attributoInfo, transazioneElementare.getCodCofog(),
				CostantiFin.D_CLASS_TIPO_GRUPPO_COFOG);
		if (isModificato) {
			return isModificato;
		}

		// SIOPE:
		if (entrata) {
			isModificato = isModificatoAttributoTClassMultiplo(
					datiOperazioneDto, attributoInfo,
					transazioneElementare.getCodSiope(),
					CostantiFin.getCodiciSiopeEntrata());
		} else {
			isModificato = isModificatoAttributoTClassMultiplo(
					datiOperazioneDto, attributoInfo,
					transazioneElementare.getCodSiope(),
					CostantiFin.getCodiciSiopeSpesa());
		}
		if (isModificato) {
			return isModificato;
		}

		// Ricorrente spesa
		isModificato = isModificatoAttributoTClass(datiOperazioneDto,
				attributoInfo, transazioneElementare.getCodRicorrenteSpesa(),
				CostantiFin.D_CLASS_TIPO_CLASSE_RICORRENTE_SPESA);
		if (isModificato) {
			return isModificato;
		}
		//

		// Capitoli perimetro sanitario spesa
		isModificato = isModificatoAttributoTClass(datiOperazioneDto,
				attributoInfo,
				transazioneElementare.getCodCapitoloSanitarioSpesa(),
				CostantiFin.D_CLASS_TIPO_CLASSE_PERIMETRO_SANITARIO_SPESA);
		if (isModificato) {
			return isModificato;
		}

		// PROGRAMMA_POLITICHE_REGIONALI_UNITARIE:
		isModificato = isModificatoAttributoTClass(datiOperazioneDto,
				attributoInfo, transazioneElementare.getCodPrgPolReg(),
				CostantiFin.D_CLASS_TIPO_PROGRAMMA_POLITICHE_REGIONALI_UNITARIE);
		if (isModificato) {
			return isModificato;
		}

		return isModificato;
	}

	/**
	 * Verifica se un attributo di tipo "t_attr" e' stato modificato
	 * 
	 * @param nuovoValore
	 * @param code
	 * @param datiOperazioneDto
	 * @param attributoInfo
	 * @return
	 */
	public boolean isAttributoModificato(String nuovoValore, String code,
			DatiOperazioneDto datiOperazioneDto,
			AttributoTClassInfoDto attributoInfo) {
		boolean isModificato = false;
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario()
				.getEnteProprietarioId();
		String cigOld = getValoreAttr(attributoInfo, datiOperazioneDto, idEnte,
				code);
		if (!StringUtilsFin.sonoUguali(nuovoValore, cigOld)) {
			isModificato = true;
		}
		return isModificato;
	}

	/**
	 * Salva la relazione r_class verso i movimenti gestione
	 * 
	 * @param siacRMovgestClass
	 * @param siacTClass
	 * @param siacTMovgestTs
	 * @param datiOperazioneDto
	 * @return
	 */
	private SiacRMovgestClassFin saveMovgesClass(
			SiacRMovgestClassFin siacRMovgestClass, SiacTClassFin siacTClass,
			SiacTMovgestTsFin siacTMovgestTs,
			DatiOperazioneDto datiOperazioneDto) {
		if (siacRMovgestClass == null) {
			siacRMovgestClass = new SiacRMovgestClassFin();
		}
		siacRMovgestClass = DatiOperazioneUtil.impostaDatiOperazioneLogin(
				siacRMovgestClass, datiOperazioneDto, siacTAccountRepository);
		siacRMovgestClass.setSiacTClass(siacTClass);
		siacRMovgestClass.setSiacTMovgestT(siacTMovgestTs);
		siacRMovgestClass = siacRMovgestClassRepository
				.saveAndFlush(siacRMovgestClass);
		// Termino restituendo l'oggetto di ritorno:
		return siacRMovgestClass;
	}
	
	
	private List<SiacRMovgestClassFin> saveMovgesClassMassive(SiacTClassFin siacTClass, List<SiacTMovgestTsFin> siacTMovgestTs,DatiOperazioneDto datiOperazioneDto) {
		
		
		List<SiacRMovgestClassFin> siacRMovgestClassList = new ArrayList<SiacRMovgestClassFin>();
		
		if(!StringUtilsFin.isEmpty(siacTMovgestTs)){
			for(SiacTMovgestTsFin it: siacTMovgestTs){
				SiacRMovgestClassFin siacRMovgestClass = new SiacRMovgestClassFin();
				siacRMovgestClass = DatiOperazioneUtil.impostaDatiOperazioneLogin(siacRMovgestClass, datiOperazioneDto, siacTAccountRepository);
				siacRMovgestClass.setSiacTClass(siacTClass);
				siacRMovgestClass.setSiacTMovgestT(it);
				siacRMovgestClassList.add(siacRMovgestClass);
			}
		}
		
		
		
		siacRMovgestClassList = siacRMovgestClassRepository.save(siacRMovgestClassList);
		siacRMovgestClassRepository.flush();
		
		
		// Termino restituendo l'oggetto di ritorno:
		return siacRMovgestClassList;
	}

	/**
	 * Salva la relazione r_class verso la liquidazione
	 * 
	 * @param siacRLiquidazioneClass
	 * @param siacTClass
	 * @param siacTLiquidazione
	 * @param datiOperazioneDto
	 * @return
	 */
	private SiacRLiquidazioneClassFin saveLiquidazioneClass(
			SiacRLiquidazioneClassFin siacRLiquidazioneClass,
			SiacTClassFin siacTClass, SiacTLiquidazioneFin siacTLiquidazione,
			DatiOperazioneDto datiOperazioneDto) {
		if (siacRLiquidazioneClass == null) {
			siacRLiquidazioneClass = new SiacRLiquidazioneClassFin();
		}
		siacRLiquidazioneClass = DatiOperazioneUtil
				.impostaDatiOperazioneLogin(siacRLiquidazioneClass,
						datiOperazioneDto, siacTAccountRepository);
		siacRLiquidazioneClass.setSiacTClass(siacTClass);
		siacRLiquidazioneClass.setSiacTLiquidazione(siacTLiquidazione);
		siacRLiquidazioneClass = siacRLiquidazioneClassRepository
				.saveAndFlush(siacRLiquidazioneClass);
		// Termino restituendo l'oggetto di ritorno:
		return siacRLiquidazioneClass;
	}

	/**
	 * Salva la relazione r_class verso gli ordinativi
	 * 
	 * @param siacROrdinativoClass
	 * @param siacTClass
	 * @param siacTOrdinativo
	 * @param datiOperazioneDto
	 * @return
	 */
	protected SiacROrdinativoClassFin saveOrdinativoClass(
			SiacROrdinativoClassFin siacROrdinativoClass,
			SiacTClassFin siacTClass, SiacTOrdinativoFin siacTOrdinativo,
			DatiOperazioneDto datiOperazioneDto) {
		if (siacROrdinativoClass == null) {
			siacROrdinativoClass = new SiacROrdinativoClassFin();
		}
		siacROrdinativoClass = DatiOperazioneUtil
				.impostaDatiOperazioneLogin(siacROrdinativoClass,
						datiOperazioneDto, siacTAccountRepository);
		siacROrdinativoClass.setSiacTClass(siacTClass);
		siacROrdinativoClass.setSiacTOrdinativo(siacTOrdinativo);
		siacROrdinativoClass = siacROrdinativoClassRepository
				.saveAndFlush(siacROrdinativoClass);
		// Termino restituendo l'oggetto di ritorno:
		return siacROrdinativoClass;
	}

	/**
	 * CHIAMA IL DB
	 * 
	 * @param attributoInfo
	 * @param datiOperazioneDto
	 * @param enteId
	 * @param codice
	 * @return
	 */
	public String getValoreAttr(AttributoTClassInfoDto attributoInfo,
			DatiOperazioneDto datiOperazioneDto, int enteId, String codice) {
		List<SiacRAttrBaseFin> lista = getRAttrEsistenti(attributoInfo,
				datiOperazioneDto, codice);
		SiacRAttrBaseFin vecchioAttr = CommonUtil.getFirst(lista);
		if (vecchioAttr != null) {
			return getValoreAttr(vecchioAttr);
		} else {
			return null;
		}
	}

	/**
	 * NON CHIAMA IL DB, presuppone sia gia' nell'oggetto passato
	 * 
	 * @param attributoInfo
	 * @param datiOperazioneDto
	 * @param enteId
	 * @param codice
	 * @return
	 */
	public String getValoreAttrSenzaChiamataDb(
			AttributoTClassInfoDto attributoInfo, String codice) {
		SiacRAttrBaseFin attr = leggiAttrDaOggetto(attributoInfo, codice);
		if (attr != null) {
			return getValoreAttr(attr);
		} else {
			return null;
		}
	}

	/**
	 * Estrae e converte il valore dell'attributo
	 * 
	 * @param siacRAttrBase
	 * @return
	 */
	private String getValoreAttr(SiacRAttrBaseFin siacRAttrBase) {
		String testoOld = "";
		SiacDAttrTipoFin siacDAttrTipoOld = siacRAttrBase.getSiacTAttr()
				.getSiacDAttrTipo();
		String attrTipoDescOld = siacDAttrTipoOld.getAttrTipoDesc();
		if (attrTipoDescOld.equals(CostantiFin.TIPO_ATTR_TESTO)) {
			testoOld = siacRAttrBase.getTesto();
		} else if (attrTipoDescOld.equals(CostantiFin.TIPO_ATTR_NUMERICO)) {
			testoOld = siacRAttrBase.getNumerico().toString();
		} else if (attrTipoDescOld.equals(CostantiFin.TIPO_ATTR_BOOLEAN)) {
			testoOld = siacRAttrBase.getBoolean_();
		} else if (attrTipoDescOld.equals(CostantiFin.TIPO_ATTR_PERCENTUALE)) {
			testoOld = siacRAttrBase.getPercentuale().toString();
		} else if (attrTipoDescOld.equals(CostantiFin.TIPO_ATTR_TABELLA)) {
			testoOld = siacRAttrBase.getTabellaId().toString();
		}
		// Termino restituendo l'oggetto di ritorno:
		return testoOld;
	}

	/**
	 * cerca solo l'attributo valido sul db
	 * 
	 * @param attributoInfo
	 * @param datiOperazioneDto
	 * @param codice
	 * @return
	 */
	private <T extends SiacRAttrBaseFin> List<T> getRAttrEsistenti(
			AttributoTClassInfoDto attributoInfo,
			DatiOperazioneDto datiOperazioneDto, String codice) {
		List<T> old = null;
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario()
				.getEnteProprietarioId();
		Integer idOggetto = attributoInfo.getIdOggetto();
		OggettoDellAttributoTClass tipoOgg = attributoInfo.getTipoOggetto();
		if (OggettoDellAttributoTClass.T_MOVGEST_TS.equals(tipoOgg)) {
			old = (List<T>) siacRMovgestTsAttrRepository
					.findValidoByIdMovGestTsAndCode(idOggetto, codice,
							datiOperazioneDto.getTs());
		} else if (OggettoDellAttributoTClass.T_LIQUIDAZIONE.equals(tipoOgg)) {
			old = (List<T>) siacRLiquidazioneAttrRepository
					.findValidoByILiqAndCode(idEnte, datiOperazioneDto.getTs(),
							idOggetto, codice);
		} else if (OggettoDellAttributoTClass.T_ORDINATIVO.equals(tipoOgg)) {
			old = (List<T>) siacROrdinativoAttrRepository
					.findListaSiacROrdinativoAttrValidiByEnteIdAndIdOrdinativoAndAttrCode(
							idEnte, idOggetto, codice,
							datiOperazioneDto.getTs());
		} else if (OggettoDellAttributoTClass.T_SOGGETTO.equals(tipoOgg)) {
			old = (List<T>) siacRSoggettoAttrRepository
					.findValidaByIdSoggettoAndCode(idOggetto, codice,
							datiOperazioneDto.getTs());
		} else if (OggettoDellAttributoTClass.T_SOGGETTO_MOD.equals(tipoOgg)) {
			old = (List<T>) siacRSoggettoAttrModRepository
					.findValidaByIdSoggettoAndCode(idOggetto, codice,
							datiOperazioneDto.getTs());
		} else if (OggettoDellAttributoTClass.T_CARTACONT.equals(tipoOgg)) {
			old = (List<T>) siacRCartacontAttrRepository
					.findValidaByIdCartaAndCode(idEnte, idOggetto, codice,
							datiOperazioneDto.getTs());
		} else if (OggettoDellAttributoTClass.T_CARTACONT_ESTERA
				.equals(tipoOgg)) {
			old = (List<T>) siacRCartacontEsteraAttrRepository
					.findValidaByIdCartaEsteraAndCode(idEnte, idOggetto,
							codice, datiOperazioneDto.getTs());
		} else if (OggettoDellAttributoTClass.T_CARTACONT_DET.equals(tipoOgg)) {
			old = (List<T>) siacRCartacontDetAttrRepository
					.findValidaByIdCartaDetAndCode(idEnte, idOggetto, codice,
							datiOperazioneDto.getTs());
		}
		// Termino restituendo l'oggetto di ritorno:
		return old;
	}

	/**
	 * cerca solo l'attributo valido tra quelli dentro l'oggetto passato (NON
	 * INTERROGA IL DATABASE)
	 * 
	 * @param attributoInfo
	 * @param datiOperazioneDto
	 * @param codice
	 * @return
	 */
	protected <T extends SiacRAttrBaseFin> T leggiAttrDaOggetto(
			AttributoTClassInfoDto attributoInfo, String codice) {
		T ritorno = null;
		OggettoDellAttributoTClass tipoOgg = attributoInfo.getTipoOggetto();
		if (OggettoDellAttributoTClass.T_MOVGEST_TS.equals(tipoOgg)) {
			// TODO
		} else if (OggettoDellAttributoTClass.T_LIQUIDAZIONE.equals(tipoOgg)) {
			// TODO
		} else if (OggettoDellAttributoTClass.T_ORDINATIVO.equals(tipoOgg)) {
			SiacTOrdinativoFin siacTOrdinativo = attributoInfo
					.getSiacTOrdinativo();
			List<SiacROrdinativoAttrFin> list = siacTOrdinativo
					.getSiacROrdinativoAttrs();
			list = DatiOperazioneUtil.soloValidi(list, getNow());
			if (list != null && list.size() > 0) {
				for (SiacROrdinativoAttrFin it : list) {
					if (it.getSiacTAttr() != null
							&& StringUtilsFin.sonoUgualiTrimmed(it.getSiacTAttr()
									.getAttrCode(), codice)) {
						ritorno = (T) it;
						break;
					}
				}
			}
		} else if (OggettoDellAttributoTClass.T_SOGGETTO.equals(tipoOgg)) {
			// TODO
		} else if (OggettoDellAttributoTClass.T_SOGGETTO_MOD.equals(tipoOgg)) {
			// TODO
		}
		// Termino restituendo l'oggetto di ritorno:
		return ritorno;
	}

	/**
	 * Salva l'attributo attr indicato, tipo boolean
	 * 
	 * @param attributoInfo
	 * @param datiOperazioneDto
	 * @param valore
	 * @param codice
	 * @return
	 */
	protected SiacRAttrBaseFin salvaAttributoTAttr(AttributoTClassInfoDto attributoInfo,DatiOperazioneDto datiOperazioneDto, boolean valore, String codice) {
		String valoreString = "";
		if (valore) {
			valoreString = CostantiFin.TRUE;
		} else {
			valoreString = CostantiFin.FALSE;
		}
		return salvaAttributoTAttr(attributoInfo, datiOperazioneDto,valoreString, codice);
	}

	/**
	 * Salva l'attributo attr indicato, tipo Integer
	 * 
	 * @param attributoInfo
	 * @param datiOperazioneDto
	 * @param valore
	 * @param codice
	 * @return
	 */
	protected SiacRAttrBaseFin salvaAttributoTAttr(
			AttributoTClassInfoDto attributoInfo,
			DatiOperazioneDto datiOperazioneDto, Integer valore, String codice) {
		String testo = null;
		if (valore != null) {
			testo = valore.toString();
		}
		return salvaAttributoTAttr(attributoInfo, datiOperazioneDto, testo,
				codice);
	}

	
	/**
	 * Salva l'attributo CIG
	 * 
	 * @param attributoInfo
	 * @param datiOperazioneDto
	 * @param valoreNuovo
	 * @return
	 */
	protected <R extends SiacRAttrBaseFin> SiacRAttrBaseFin salvaAttributoCig(
			AttributoTClassInfoDto attributoInfo,
			DatiOperazioneDto datiOperazioneDto, String valoreNuovo) {
		
		return salvaAttributoTAttr(attributoInfo,
				 datiOperazioneDto, org.apache.commons.lang.StringUtils.upperCase(valoreNuovo),
				 CostantiFin.T_ATTR_CODE_CIG);
	}	

	
	/**
	 * Salva l'attributo CUP
	 * 
	 * @param attributoInfo
	 * @param datiOperazioneDto
	 * @param valoreNuovo
	 * @return
	 */
	protected <R extends SiacRAttrBaseFin> SiacRAttrBaseFin salvaAttributoCup(
			AttributoTClassInfoDto attributoInfo,
			DatiOperazioneDto datiOperazioneDto, String valoreNuovo) {
		
		return salvaAttributoTAttr(attributoInfo,
				 datiOperazioneDto, org.apache.commons.lang.StringUtils.upperCase(valoreNuovo),
				 CostantiFin.T_ATTR_CODE_CUP);
	}	

	
	/**
	 * Salva l'attributo attr indicato
	 * 
	 * @param attributoInfo
	 * @param datiOperazioneDto
	 * @param valoreNuovo
	 * @param codice
	 * @return
	 */
	protected <R extends SiacRAttrBaseFin> SiacRAttrBaseFin salvaAttributoTAttr(
			AttributoTClassInfoDto attributoInfo,
			DatiOperazioneDto datiOperazioneDto, String valoreNuovo,
			String codice) {
		if (StringUtilsFin.isEmpty(valoreNuovo)) {
			valoreNuovo = "";// set to blank
		}

		int enteId = datiOperazioneDto.getSiacTEnteProprietario()
				.getEnteProprietarioId();

		Integer idOggetto = attributoInfo.getIdOggetto();
		OggettoDellAttributoTClass tipoOgg = attributoInfo.getTipoOggetto();

		// TIPO NUOVO:
		SiacTAttrFin sta = caricaInfoSiacTAttr(codice, enteId);
		SiacDAttrTipoFin sda = sta.getSiacDAttrTipo();
		String atd = sda.getAttrTipoDesc();
		//

		// invalido il vecchio:
		if (idOggetto != null && idOggetto.intValue() > 0) {

			List<R> lista = getRAttrEsistenti(attributoInfo, datiOperazioneDto,
					codice);

			if (lista != null && lista.size() > 0) {
				SiacRAttrBaseFin vecchioAttr = lista.get(0);
				if (vecchioAttr != null) {

					// HANNO LO STESSO TIPO?
					String oldTipoDesc = vecchioAttr.getSiacTAttr()
							.getSiacDAttrTipo().getAttrTipoDesc();
					String newTipoDesc = sda.getAttrTipoDesc();
					boolean modificato = false;
					if (!StringUtilsFin.sonoUguali(oldTipoDesc, newTipoDesc)) {
						// se cambia il tipo per forza e' cambiato
						modificato = true;
					} else {
						String valoreOld = getValoreAttr(vecchioAttr);
						if (!StringUtilsFin.sonoUguali(valoreNuovo, valoreOld)) {
							modificato = true;
						}
					}

					if (!modificato) {
						// se sono uguali non c'e' nulla da aggiornare
						return vecchioAttr;
					} else {
						if (OggettoDellAttributoTClass.T_MOVGEST_TS
								.equals(tipoOgg)) {
							DatiOperazioneUtil.cancellaRecord(vecchioAttr,
									siacRMovgestTsAttrRepository,
									datiOperazioneDto, siacTAccountRepository);
						} else if (OggettoDellAttributoTClass.T_LIQUIDAZIONE
								.equals(tipoOgg)) {
							DatiOperazioneUtil.cancellaRecord(vecchioAttr,
									siacRLiquidazioneAttrRepository,
									datiOperazioneDto, siacTAccountRepository);
						} else if (OggettoDellAttributoTClass.T_ORDINATIVO
								.equals(tipoOgg)) {
							DatiOperazioneUtil.cancellaRecord(vecchioAttr,
									siacROrdinativoAttrRepository,
									datiOperazioneDto, siacTAccountRepository);
						} else if (OggettoDellAttributoTClass.T_SOGGETTO
								.equals(tipoOgg)) {
							DatiOperazioneUtil.cancellaRecord(vecchioAttr,
									siacRSoggettoAttrRepository,
									datiOperazioneDto, siacTAccountRepository);
						} else if (OggettoDellAttributoTClass.T_SOGGETTO_MOD
								.equals(tipoOgg)) {
							DatiOperazioneUtil.cancellaRecord(vecchioAttr,
									siacRSoggettoAttrModRepository,
									datiOperazioneDto, siacTAccountRepository);
						} else if (OggettoDellAttributoTClass.T_CARTACONT
								.equals(tipoOgg)) {
							DatiOperazioneUtil.cancellaRecord(vecchioAttr,
									siacRCartacontAttrRepository,
									datiOperazioneDto, siacTAccountRepository);
						} else if (OggettoDellAttributoTClass.T_CARTACONT_DET
								.equals(tipoOgg)) {
							DatiOperazioneUtil.cancellaRecord(vecchioAttr,
									siacRCartacontDetAttrRepository,
									datiOperazioneDto, siacTAccountRepository);
						} else if (OggettoDellAttributoTClass.T_CARTACONT_ESTERA
								.equals(tipoOgg)) {
							DatiOperazioneUtil.cancellaRecord(vecchioAttr,
									siacRCartacontEsteraAttrRepository,
									datiOperazioneDto, siacTAccountRepository);
						}
					}
				}
			}
		}

		//
		// inserisco il nuovo attr:

		SiacRAttrBaseFin siacRAttr = null;
		if (OggettoDellAttributoTClass.T_MOVGEST_TS.equals(tipoOgg)) {
			siacRAttr = new SiacRMovgestTsAttrFin();
			SiacTMovgestTsFin siacTMovgestTs = attributoInfo
					.getSiacTMovgestTs();
			((SiacRMovgestTsAttrFin) siacRAttr)
					.setSiacTMovgestT(siacTMovgestTs);
		} else if (OggettoDellAttributoTClass.T_LIQUIDAZIONE.equals(tipoOgg)) {
			siacRAttr = new SiacRLiquidazioneAttrFin();
			SiacTLiquidazioneFin siacTLiquidazione = attributoInfo
					.getSiacTLiquidazione();
			((SiacRLiquidazioneAttrFin) siacRAttr)
					.setSiacTLiquidazione(siacTLiquidazione);
		} else if (OggettoDellAttributoTClass.T_ORDINATIVO.equals(tipoOgg)) {
			siacRAttr = new SiacROrdinativoAttrFin();
			SiacTOrdinativoFin siacTOrdinativo = attributoInfo
					.getSiacTOrdinativo();
			((SiacROrdinativoAttrFin) siacRAttr)
					.setSiacTOrdinativo(siacTOrdinativo);
		} else if (OggettoDellAttributoTClass.T_SOGGETTO.equals(tipoOgg)) {
			siacRAttr = new SiacRSoggettoAttrFin();
			SiacTSoggettoFin siacTSoggetto = attributoInfo.getSiacTSoggetto();
			((SiacRSoggettoAttrFin) siacRAttr).setSiacTSoggetto(siacTSoggetto);
		} else if (OggettoDellAttributoTClass.T_SOGGETTO_MOD.equals(tipoOgg)) {
			siacRAttr = new SiacRSoggettoAttrFin();
			SiacTSoggettoModFin siacTSoggettoMod = attributoInfo
					.getSiacTSoggettoMod();
			siacRAttr = new SiacRSoggettoAttrModFin();
			((SiacRSoggettoAttrModFin) siacRAttr)
					.setSiacTSoggettoMod(siacTSoggettoMod);
			((SiacRSoggettoAttrModFin) siacRAttr)
					.setSiacTSoggetto(siacTSoggettoMod.getSiacTSoggetto());
		} else if (OggettoDellAttributoTClass.T_CARTACONT.equals(tipoOgg)) {
			siacRAttr = new SiacRCartacontAttrFin();
			SiacTCartacontFin siacTCartacont = attributoInfo
					.getSiacTCartacont();
			((SiacRCartacontAttrFin) siacRAttr)
					.setSiacTCartacont(siacTCartacont);
		} else if (OggettoDellAttributoTClass.T_CARTACONT_DET.equals(tipoOgg)) {
			siacRAttr = new SiacRCartacontDetAttrFin();
			SiacTCartacontDetFin siacTCartacontDet = attributoInfo
					.getSiacTCartacontDet();
			((SiacRCartacontDetAttrFin) siacRAttr)
					.setSiacTCartacontDet(siacTCartacontDet);
		} else if (OggettoDellAttributoTClass.T_CARTACONT_ESTERA
				.equals(tipoOgg)) {
			siacRAttr = new SiacRCartacontEsteraAttrFin();
			SiacTCartacontEsteraFin siacTCartacontEstera = attributoInfo
					.getSiacTCartacontEstera();
			((SiacRCartacontEsteraAttrFin) siacRAttr)
					.setSiacTCartacontEstera(siacTCartacontEstera);
		}

		siacRAttr.setSiacTAttr(sta);

		// Controllo di che tipo deve essere la nota
		if (atd.equals(CostantiFin.TIPO_ATTR_TESTO)) {
			siacRAttr.setTesto(valoreNuovo);
		} else if (atd.equals(CostantiFin.TIPO_ATTR_NUMERICO)) {
			siacRAttr.setNumerico(new BigDecimal(valoreNuovo));
		} else if (atd.equals(CostantiFin.TIPO_ATTR_BOOLEAN)) {
			siacRAttr.setBoolean_(valoreNuovo);
		} else if (atd.equals(CostantiFin.TIPO_ATTR_PERCENTUALE)) {
			siacRAttr.setPercentuale(new BigDecimal(valoreNuovo));
		} else if (atd.equals(CostantiFin.TIPO_ATTR_TABELLA)) {
			siacRAttr.setTabellaId(Integer.parseInt(valoreNuovo));
		}
		DatiOperazioneDto datiOperazioneInserimento = DatiOperazioneDto
				.buildDatiOperazione(datiOperazioneDto, Operazione.INSERIMENTO);
		siacRAttr = DatiOperazioneUtil.impostaDatiOperazioneLogin(siacRAttr,
				datiOperazioneInserimento, siacTAccountRepository);

		if (OggettoDellAttributoTClass.T_MOVGEST_TS.equals(tipoOgg)) {
			siacRAttr = siacRMovgestTsAttrRepository
					.saveAndFlush((SiacRMovgestTsAttrFin) siacRAttr);
		} else if (OggettoDellAttributoTClass.T_LIQUIDAZIONE.equals(tipoOgg)) {
			siacRAttr = siacRLiquidazioneAttrRepository
					.saveAndFlush((SiacRLiquidazioneAttrFin) siacRAttr);
		} else if (OggettoDellAttributoTClass.T_ORDINATIVO.equals(tipoOgg)) {
			siacRAttr = siacROrdinativoAttrRepository
					.saveAndFlush((SiacROrdinativoAttrFin) siacRAttr);
		} else if (OggettoDellAttributoTClass.T_SOGGETTO.equals(tipoOgg)) {
			siacRAttr = siacRSoggettoAttrRepository
					.saveAndFlush((SiacRSoggettoAttrFin) siacRAttr);
		} else if (OggettoDellAttributoTClass.T_SOGGETTO_MOD.equals(tipoOgg)) {
			siacRAttr = siacRSoggettoAttrModRepository
					.saveAndFlush((SiacRSoggettoAttrModFin) siacRAttr);
		} else if (OggettoDellAttributoTClass.T_CARTACONT.equals(tipoOgg)) {
			siacRCartacontAttrRepository
					.saveAndFlush((SiacRCartacontAttrFin) siacRAttr);
		} else if (OggettoDellAttributoTClass.T_CARTACONT_DET.equals(tipoOgg)) {
			siacRCartacontDetAttrRepository
					.saveAndFlush((SiacRCartacontDetAttrFin) siacRAttr);
		} else if (OggettoDellAttributoTClass.T_CARTACONT_ESTERA
				.equals(tipoOgg)) {
			siacRCartacontEsteraAttrRepository
					.saveAndFlush((SiacRCartacontEsteraAttrFin) siacRAttr);
		}
		// Termino restituendo l'oggetto di ritorno:
		return siacRAttr;
	}

	/**
	 * Ricarica i validi nel model
	 * 
	 * @param attributoInfo
	 * @param datiOperazioneDto
	 */
	protected <R extends SiacRAttrBaseFin> void aggiornaAllAttrValidiInEntityJPA(
			AttributoTClassInfoDto attributoInfo,
			DatiOperazioneDto datiOperazioneDto) {
		int enteId = datiOperazioneDto.getSiacTEnteProprietario()
				.getEnteProprietarioId();
		OggettoDellAttributoTClass tipoOgg = attributoInfo.getTipoOggetto();
		Timestamp now = getNow();
		Integer idOggetto = attributoInfo.getIdOggetto();
		if (OggettoDellAttributoTClass.T_MOVGEST_TS.equals(tipoOgg)) {
			SiacTMovgestTsFin siacTMovgestTs = attributoInfo
					.getSiacTMovgestTs();
			List<SiacRMovgestTsAttrFin> validi = siacRMovgestTsAttrRepository
					.findAllValidiByMovgestTs(idOggetto, now);
			siacTMovgestTs.setSiacRMovgestTsAttrs(validi);
		} else if (OggettoDellAttributoTClass.T_LIQUIDAZIONE.equals(tipoOgg)) {
			SiacTLiquidazioneFin siacTLiquidazione = attributoInfo
					.getSiacTLiquidazione();
			List<SiacRLiquidazioneAttrFin> validi = siacRLiquidazioneAttrRepository
					.findAllValidiByILiq(enteId, now, idOggetto);
			siacTLiquidazione.setSiacRLiquidazioneAttrs(validi);
		} else if (OggettoDellAttributoTClass.T_ORDINATIVO.equals(tipoOgg)) {
			SiacTOrdinativoFin siacTOrdinativo = attributoInfo
					.getSiacTOrdinativo();
			List<SiacROrdinativoAttrFin> validi = siacROrdinativoAttrRepository
					.findAllValidiByIdOrdinativo(enteId, idOggetto, now);
			siacTOrdinativo.setSiacROrdinativoAttrs(validi);
		} else if (OggettoDellAttributoTClass.T_SOGGETTO.equals(tipoOgg)) {
			SiacTSoggettoFin siacTSoggetto = attributoInfo.getSiacTSoggetto();
			List<SiacRSoggettoAttrFin> validi = siacRSoggettoAttrRepository
					.findAllValidiByIdSoggetto(idOggetto, now);
			siacTSoggetto.setSiacRSoggettoAttrs(validi);
		} else if (OggettoDellAttributoTClass.T_SOGGETTO_MOD.equals(tipoOgg)) {
			SiacTSoggettoModFin siacTSoggettoMod = attributoInfo
					.getSiacTSoggettoMod();
			List<SiacRSoggettoAttrModFin> validi = siacRSoggettoAttrModRepository
					.findAllValidiBySoggettoId(idOggetto, now);
			siacTSoggettoMod.setSiacRSoggettoAttrMods(validi);
		}
	}

	/**
	 * Ricarica i validi nel model
	 * 
	 * @param attributoInfo
	 * @param datiOperazioneDto
	 */
	protected <T extends SiacTEnteBase> void aggiornaAllRClassValidiInEntityJPA(
			AttributoTClassInfoDto attributoInfo,
			DatiOperazioneDto datiOperazioneDto) {
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario()
				.getEnteProprietarioId();
		Integer idOggetto = attributoInfo.getIdOggetto();
		Timestamp now = getNow();
		OggettoDellAttributoTClass tipoOgg = attributoInfo.getTipoOggetto();
		if (OggettoDellAttributoTClass.T_MOVGEST_TS.equals(tipoOgg)) {
			SiacTMovgestTsFin siacTMovgestTs = attributoInfo
					.getSiacTMovgestTs();
			List<SiacRMovgestClassFin> lrmvgclasses = siacRMovgestClassRepository
					.findAllValidiByMovgestTs(idEnte, now, idOggetto);
			if (lrmvgclasses == null) {
				lrmvgclasses = new ArrayList<SiacRMovgestClassFin>();
			}
			siacTMovgestTs.setSiacRMovgestClasses(lrmvgclasses);
		} else if (OggettoDellAttributoTClass.T_LIQUIDAZIONE.equals(tipoOgg)) {
			SiacTLiquidazioneFin siacTLiquidazione = attributoInfo
					.getSiacTLiquidazione();
			List<SiacRLiquidazioneClassFin> validi = siacRLiquidazioneClassRepository
					.findAllValidiByLiquidazione(idEnte, now, idOggetto);
			if (validi == null) {
				validi = new ArrayList<SiacRLiquidazioneClassFin>();
			}
			siacTLiquidazione.setSiacRLiquidazioneClasses(validi);
		} else if (OggettoDellAttributoTClass.T_ORDINATIVO.equals(tipoOgg)) {
			SiacTOrdinativoFin siacTOrdinativo = attributoInfo
					.getSiacTOrdinativo();
			List<SiacROrdinativoClassFin> validi = siacROrdinativoClassRepository
					.findAllValidiByOrdinativoId(idEnte, now, idOggetto);
			siacTOrdinativo.setSiacROrdinativoClasses(validi);
		}
	}

	/**
	 * Ricarica i validi nel model
	 * 
	 * @param attributoInfo
	 * @param siacRClassBaseAggiornati
	 * @param datiOperazioneDto
	 */
	protected <T extends SiacTEnteBase> void aggiornaAllRClassValidiInEntityJPA(
			AttributoTClassInfoDto attributoInfo,
			List<SiacRClassBaseFin> siacRClassBaseAggiornati,
			DatiOperazioneDto datiOperazioneDto) {
		// Integer idEnte =
		// datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		if (siacRClassBaseAggiornati != null
				&& siacRClassBaseAggiornati.size() > 0) {
			OggettoDellAttributoTClass tipoOgg = attributoInfo.getTipoOggetto();
			if (OggettoDellAttributoTClass.T_MOVGEST_TS.equals(tipoOgg)) {
				SiacTMovgestTsFin siacTMovgestTs = attributoInfo
						.getSiacTMovgestTs();
				List<SiacRMovgestClassFin> siacRMovgestClasses = new ArrayList<SiacRMovgestClassFin>();
				for (SiacRClassBaseFin iterato : siacRClassBaseAggiornati) {
					siacRMovgestClasses.add((SiacRMovgestClassFin) iterato);
				}
				siacTMovgestTs.setSiacRMovgestClasses(siacRMovgestClasses);
			} else if (OggettoDellAttributoTClass.T_LIQUIDAZIONE
					.equals(tipoOgg)) {
				SiacTLiquidazioneFin siacTLiquidazione = attributoInfo
						.getSiacTLiquidazione();
				List<SiacRLiquidazioneClassFin> siacRLiquidazioneClasses = new ArrayList<SiacRLiquidazioneClassFin>();
				for (SiacRClassBaseFin iterato : siacRClassBaseAggiornati) {
					siacRLiquidazioneClasses
							.add((SiacRLiquidazioneClassFin) iterato);
				}
				siacTLiquidazione
						.setSiacRLiquidazioneClasses(siacRLiquidazioneClasses);
			} else if (OggettoDellAttributoTClass.T_ORDINATIVO.equals(tipoOgg)) {
				SiacTOrdinativoFin siacTOrdinativo = attributoInfo
						.getSiacTOrdinativo();
				List<SiacROrdinativoClassFin> siacROrdinativoClasses = new ArrayList<SiacROrdinativoClassFin>();
				for (SiacRClassBaseFin iterato : siacRClassBaseAggiornati) {
					siacROrdinativoClasses
							.add((SiacROrdinativoClassFin) iterato);
				}
				siacTOrdinativo
						.setSiacROrdinativoClasses(siacROrdinativoClasses);
			}
		}
	}

	/**
	 * verifica se l'attributo e' stato modificato
	 * 
	 * @param attributoInfo
	 * @param datiOperazioneDto
	 * @param valoreNuovo
	 * @param codice
	 * @return
	 */
	protected <R extends SiacRAttrBaseFin> boolean isAttributoTAttrModificato(
			AttributoTClassInfoDto attributoInfo,
			DatiOperazioneDto datiOperazioneDto, String valoreNuovo,
			String codice) {
		if (StringUtilsFin.isEmpty(valoreNuovo)) {
			valoreNuovo = "";// set to blank
		}

		int enteId = datiOperazioneDto.getSiacTEnteProprietario()
				.getEnteProprietarioId();

		Integer idOggetto = attributoInfo.getIdOggetto();

		// TIPO NUOVO:
		SiacTAttrFin sta = caricaInfoSiacTAttr(codice, enteId);
		SiacDAttrTipoFin sda = sta.getSiacDAttrTipo();
		//

		boolean modificato = false;
		if (idOggetto != null && idOggetto.intValue() > 0) {

			List<R> lista = getRAttrEsistenti(attributoInfo, datiOperazioneDto,
					codice);

			if (lista != null && lista.size() > 0) {
				SiacRAttrBaseFin vecchioAttr = lista.get(0);
				if (vecchioAttr != null) {
					// HANNO LO STESSO TIPO?
					String oldTipoDesc = vecchioAttr.getSiacTAttr()
							.getSiacDAttrTipo().getAttrTipoDesc();
					String newTipoDesc = sda.getAttrTipoDesc();

					if (!StringUtilsFin.sonoUguali(oldTipoDesc, newTipoDesc)) {
						// se cambia il tipo per forza e' cambiato
						modificato = true;
					} else {
						String valoreOld = getValoreAttr(vecchioAttr);
						if (!valoreNuovo.equals(valoreOld)) {
							modificato = true;
						}
					}

				}
			} else {
				// non ci sono attributi gia salvati
				if (!StringUtilsFin.isEmpty(valoreNuovo)) {
					// si puo' considerare modificato se c'e' un valore
					modificato = true;
				}
			}
		}
		// Termino restituendo l'oggetto di ritorno:
		return modificato;
	}

	/**
	 * Dato il codice di un certo attributo t_attr carica e restituisce
	 * l'oggetto SiacTAttrFin completo
	 * 
	 * @param codice
	 * @param enteId
	 * @return
	 */
	private SiacTAttrFin caricaInfoSiacTAttr(String codice, int enteId) {
		SiacTAttrFin sta = siacTAttrRepository.getTAttrByCode(codice, enteId);
		if(sta == null) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Impossibile trovare l'attributo con codice: " + org.apache.commons.lang3.StringUtils.defaultIfBlank(codice, "null")));
		}
		SiacDAttrTipoFin sda = sta.getSiacDAttrTipo();
		sda.setAttrTipoDesc(sda.getAttrTipoDesc());
		sda.setAttrTipoId(sda.getAttrTipoId());
		sta.setSiacDAttrTipo(sda);
		// Termino restituendo l'oggetto di ritorno:
		return sta;
	}

	/**
	 * Per verificare se l'account indicato e' amministratore o meno
	 * 
	 * @param account
	 * @return
	 */
	public boolean isDecentrato(Account account) {
		return isAzioneConsentita(account, AzioneConsentitaEnum.OP_SOG_gestisciSoggDec);
	}

	/**
	 * Per verificare se l'account indicato e' abilitato o meno all'azione
	 * indicataa
	 * 
	 * @param account
	 * @param codeAzione
	 * @return
	 */
	public boolean isAzioneConsentita(Account account, AzioneConsentitaEnum azioneConsentita) {
				
		List<SiacRAccountRuoloOpFin> l = siacRAccountRuoloOpRepository
				.findByAccount(account.getUid());
		List<Integer> listaRuoloOpId = new ArrayList<Integer>();
		int counter = 0;
		if (l != null && l.size() > 0) {
			for (SiacRAccountRuoloOpFin sraro : l) {
				listaRuoloOpId.add(sraro.getSiacDRuoloOp().getRuoloOpId());
			}
		} 
		//SIAC-7986 controllo anche i gruppi collegati all'account
//		else {
			// Non ci sono ruoli direttamente collegati all'account passato in input
			// Estratto i gruppi di appartenenza dell'account
			List<SiacRGruppoAccountFin> listaSiacRGruppoAccount = siacRGruppoAccountRepository
					.findGruppiByAccountId(account.getUid());
			if (listaSiacRGruppoAccount != null
					&& listaSiacRGruppoAccount.size() > 0) {
				// Estraggo i ruoli per ogni gruppo precedentemente estratto
				for (SiacRGruppoAccountFin siacRGruppoAccount : listaSiacRGruppoAccount) {
					List<SiacRGruppoRuoloOpFin> listaSiacRGruppoRuoloOp = siacRGruppoRuoloOpRepository
							.findRuoliByGruppoId(siacRGruppoAccount
									.getSiacTGruppo().getGruppoId());
					if (listaSiacRGruppoRuoloOp != null
							&& listaSiacRGruppoRuoloOp.size() > 0) {
						for (SiacRGruppoRuoloOpFin siacRGruppoRuoloOp : listaSiacRGruppoRuoloOp) {
							listaRuoloOpId.add(siacRGruppoRuoloOp
									.getSiacDRuoloOp().getRuoloOpId());
						}
					}
				}
			}
//		}
		if (null != listaRuoloOpId && listaRuoloOpId.size() > 0) {
			for (Integer ruoloId : listaRuoloOpId) {
				List<SiacRRuoloOpAzioneFin> linner = siacRRuoloOpAzioneRepository
						.findByAzioneERuolo(ruoloId, azioneConsentita.getNomeAzione());
				if (linner != null)
					counter += linner.size();
				if (counter > 0)
					break;
			}
		}
		return counter > 0;
	}

	/**
	 * Genera un progressivo in una transazione separata in base al contesto
	 * formato dai parametri indicati <br/>
	 * 
	 * @param type
	 *            tipo del progressivo da restituire (cui vengono concatenati
	 *            gli eventuali id, se presenti)
	 * @param idEnte
	 * @param idAmbito
	 * @param loginOperazione
	 * @param ids
	 *            id per contestualizzare in modo piu ristretto il progressivo
	 *            restituito
	 * @return il progressivo gi� incrementato
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Long getMaxCode(ProgressivoType type, Number idEnte,
			Number idAmbito, String loginOperazione, Number... ids) {
		// formo la chiave di ricerca del progressivo
		StringBuilder keyBuilder = new StringBuilder(type.getVal());
		if (ids != null) {
			for (Number id : ids) {
				keyBuilder.append('_').append(id);
			}
		}
		String key = keyBuilder.toString();

		Long l = null;
		SiacTProgressivoFin prog = null;
		// faccio la select for update per fermare le altre sessioni
		List<SiacTProgressivoFin> lstp = siacTProgressivoRepository.findByKey(
				key, idAmbito.intValue(), idEnte.intValue());
		Date now = getNow();
		if (lstp == null || lstp.isEmpty()) {
			// se la select non ritorna dati, � il primo progressivo per il
			// contesto passato e quindi inserisco una nuova riga
			l = 1L;
			prog = new SiacTProgressivoFin();
			prog.setProgKey(key);
			prog.setProgValue(l);
			prog.setDataCreazione(now);
			prog.setDataInizioValidita(now);
			prog.setAmbitoId(idAmbito.intValue());
			prog.setLoginOperazione(loginOperazione);
			SiacTEnteProprietarioFin step = new SiacTEnteProprietarioFin();
			step.setUid(idEnte.intValue());
			prog.setSiacTEnteProprietario(step);
		} else {
			// se la select ritorna dati, incremento il progressivo
			prog = lstp.get(0);
			l = prog.getProgValue() + 1;
			prog.setProgValue(l);
		}
		prog.setDataModifica(now);
		// salvo i dati sul db
		siacTProgressivoRepository.saveAndFlush(prog);
		// Termino restituendo l'oggetto di ritorno:
		return l;
	}

	// nel caso non gestisco lo stato delle entita torna sempre true
	protected boolean checkStatoEntita(String stato) {
		return true;
	}

	/**
	 * verifica se l'entita passata e' nella situazione in cui si deve gestire
	 * la doppia gestione
	 * 
	 * @param bilancio
	 * @param entita
	 * @param datiOperazioneDto
	 * @return
	 */
	public boolean inserireDoppiaGestione(Bilancio bilancio, Entita entita,
			DatiOperazioneDto datiOperazioneDto) {
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario()
				.getEnteProprietarioId();
		String statoEntitaCode = determinaStatoEntita(idEnte, entita);

		boolean statoEntitaPerDoppiaGest = checkStatoEntita(statoEntitaCode);

		if (!statoEntitaPerDoppiaGest)
			return false;
		
		boolean statoBilancioPerDoggiaGest = false;
		
		String bilCode = caricaCodiceBilancio(datiOperazioneDto, bilancio.getAnno());

		if (!StringUtilsFin.isEmpty(bilCode) && CostantiFin.BIL_FASE_OPERATIVA_PREDISPOSIZIONE_CONSUNTIVO.equals(bilCode)) {
			statoBilancioPerDoggiaGest = true;
		}

		if (statoEntitaPerDoppiaGest && statoBilancioPerDoggiaGest) {
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * m
	 * @param datiOperazioneDto
	 * @param annoBilancio
	 */
	public String caricaCodiceBilancio(DatiOperazioneDto datiOperazioneDto,Integer annoBilancio){
		
		String bilCode = null;
		
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		
		String annoBil = null;
		try{
			annoBil = Integer.toString(annoBilancio);
		}catch (Throwable t){
			annoBil = null;	
		}
		
		if(!StringUtilsFin.isEmpty(annoBil)){

			List<SiacTBilFin> siacTBils = siacTBilRepository.getValidoByAnno(idEnte,annoBil, datiOperazioneDto.getTs());
			
			SiacTBilFin siacTBil = CommonUtil.getFirst(siacTBils);
			
			if(siacTBil!=null){
				
				
				SiacRBilFaseOperativaFin siacRBilFaseOperativaValido = null;
				List<SiacRBilFaseOperativaFin> listaSiacRBilFaseOperativa = siacRBilFaseOperativaRepository.findValido(idEnte, siacTBil.getBilId(),datiOperazioneDto.getTs());

				if (listaSiacRBilFaseOperativa != null && listaSiacRBilFaseOperativa.size() > 0) {
					for (SiacRBilFaseOperativaFin siacRBilFaseOperativaIterato : listaSiacRBilFaseOperativa) {
						if (siacRBilFaseOperativaIterato.getDataFineValidita() == null) {
							siacRBilFaseOperativaValido = siacRBilFaseOperativaIterato;
						}
					}
				}

				if (siacRBilFaseOperativaValido != null) {
					bilCode = siacRBilFaseOperativaValido.getSiacDFaseOperativa().getFaseOperativaCode();
				}
				
			}
			
		}
		
		return bilCode;
	}
	
	public BigDecimal calcolaDisponibilitaAPagarePerDodicesimi(Integer elemId){
		return accertamentoDao.calcolaDisponibilitaAPagarePerDodicesimi(elemId);
	}
	
	public boolean isBilancioInStato(Bilancio bilancio, String stato,DatiOperazioneDto datiOperazione){
		if(bilancio!=null && stato!=null && datiOperazione!=null){
			int annoBilancio = bilancio.getAnno();
			String codiceBil = caricaCodiceBilancio(datiOperazione, annoBilancio);
			return stato.equals(codiceBil);	
		}
		return false;
	}

	/**
	 * Cerca le quote valide
	 * 
	 * @param idEnte
	 * @param siacTMovgestTs
	 * @return
	 */
	protected List<SiacTOrdinativoTsDetFin> findQuoteValideFromAccertamentoSubAccertamento(
			Integer idEnte, SiacTMovgestTsFin siacTMovgestTs) {
		// Tutti i sub-ordinativi con stato VALIDO relativi a tutti gli
		// ordinativi con stato VALIDO dell�accertamento.
		List<SiacTOrdinativoTsDetFin> elencoSiacTOrdinativoTsDet = new ArrayList<SiacTOrdinativoTsDetFin>();

		// Controllo se ci sono legami tra l'accertamento / sub-accertamento ed
		// eventuali quote di ordinativo
		// Da prendere direttamente dal db per problemi con cache di hibernate
		// sull'inserimento quote ordinativo
		// List<SiacROrdinativoTsMovgestTFin> elencoSiacROrdinativoTsMovgestT =
		// siacTMovgestTs.getSiacROrdinativoTsMovgestTs();
		List<SiacROrdinativoTsMovgestTFin> elencoSiacROrdinativoTsMovgestT = siacROrdinativoTsMovgestTsRepository
				.findValidoByIdMovGestTs(idEnte, getNow(),
						siacTMovgestTs.getMovgestTsId());
		if (elencoSiacROrdinativoTsMovgestT != null
				&& elencoSiacROrdinativoTsMovgestT.size() > 0) {
			for (SiacROrdinativoTsMovgestTFin siacROrdinativoTsMovgestT : elencoSiacROrdinativoTsMovgestT) {
				if (siacROrdinativoTsMovgestT.getDataFineValidita() == null) {
					// ho trovato legame con quota valido
					// controllo se la quota � valida
					SiacTOrdinativoTFin siacTOrdinativoT = siacROrdinativoTsMovgestT
							.getSiacTOrdinativoT();
					if (siacTOrdinativoT.getDataFineValidita() == null) {
						// la quota � valida
						// adesso controllo che sia in stato valido anche
						// l'ordinativo a della quota
						SiacTOrdinativoFin siacTOrdinativo = siacTOrdinativoT
								.getSiacTOrdinativo();
						// Da prendere direttamente dal db per problemi con
						// cache di hibernate sull'inserimento quote ordinativo
						// List<SiacROrdinativoStatoFin>
						// elencoSiacROrdinativoStato =
						// siacTOrdinativo.getSiacROrdinativoStatos();
						List<SiacROrdinativoStatoFin> elencoSiacROrdinativoStato = siacROrdinativoStatoRepository
								.findSiacROrdinativoStatoValidoByIdOrdinativo(
										idEnte, siacTOrdinativo.getOrdId(),
										getNow());
						if (elencoSiacROrdinativoStato != null
								&& elencoSiacROrdinativoStato.size() > 0) {
							for (SiacROrdinativoStatoFin siacROrdinativoStato : elencoSiacROrdinativoStato) {
								if (siacROrdinativoStato.getDataFineValidita() == null) {
									if (!siacROrdinativoStato
											.getSiacDOrdinativoStato()
											.getOrdStatoCode()
											.equalsIgnoreCase(
													CostantiFin.D_ORDINATIVO_STATO_ANNULLATO)) {
										// l'ordinativo � in stato valido, cio�
										// non annullato
										// quindi la quota estratta � valida ed
										// � legata ad un ordinativo valido
										// devo allora prenderla in
										// considerazione per il calcolo
										// Da prendere direttamente dal db per
										// problemi con cache di hibernate
										// sull'inserimento quote ordinativo
										// List<SiacTOrdinativoTsDetFin>
										// listaSiacTOrdinativoTsDet =
										// siacTOrdinativoT.getSiacTOrdinativoTsDets();
										List<SiacTOrdinativoTsDetFin> listaSiacTOrdinativoTsDet = siacTOrdinativoTsDetRepository
												.findOrdinativoTsDetValidoByOrdTsId(
														idEnte,
														siacTOrdinativoT
																.getOrdTsId(),
														getNow());
										if (listaSiacTOrdinativoTsDet != null
												& listaSiacTOrdinativoTsDet
														.size() > 0) {
											for (SiacTOrdinativoTsDetFin siacTOrdinativoTsDet : listaSiacTOrdinativoTsDet) {
												// estraggo solo la riga valida
												// e relativa all'importo
												// attuale
												if (siacTOrdinativoTsDet
														.getDataFineValidita() == null
														&& siacTOrdinativoTsDet
																.getSiacDOrdinativoTsDetTipo()
																.getOrdTsDetTipoCode()
																.equalsIgnoreCase(
																		CostantiFin.D_ORDINATIVO_TS_DET_TIPO_IMPORTO_ATTUALE)) {
													elencoSiacTOrdinativoTsDet
															.add(siacTOrdinativoTsDet);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		// Termino restituendo l'oggetto di ritorno:
		return elencoSiacTOrdinativoTsDet;
	}

	/**
	 * cerca le quote valide, versione ottimizzata
	 * 
	 * @param idEnte
	 * @param siacTMovgestTs
	 * @param ottimizzazioneDto
	 * @return
	 */
	protected List<SiacTOrdinativoTsDetFin> findQuoteValideFromAccertamentoSubAccertamentoOPT(
			Integer idEnte, SiacTMovgestTsFin siacTMovgestTs,
			OttimizzazioneMovGestDto ottimizzazioneDto) {
		// Tutti i sub-ordinativi con stato VALIDO relativi a tutti gli
		// ordinativi con stato VALIDO dell�accertamento.

		List<SiacTOrdinativoTsDetFin> elencoSiacTOrdinativoTsDet = new ArrayList<SiacTOrdinativoTsDetFin>();

		// Controllo se ci sono legami tra l'accertamento / sub-accertamento ed
		// eventuali quote di ordinativo
		// Da prendere direttamente dal db per problemi con cache di hibernate
		// sull'inserimento quote ordinativo
		// List<SiacROrdinativoTsMovgestTFin> elencoSiacROrdinativoTsMovgestT =
		// siacTMovgestTs.getSiacROrdinativoTsMovgestTs();

		List<SiacROrdinativoTsMovgestTFin> elencoSiacROrdinativoTsMovgestT = ottimizzazioneDto
				.filtraSiacROrdinativoTsMovgestTByMovgestTs(siacTMovgestTs
						.getMovgestTsId());
		// /List<SiacROrdinativoTsMovgestTFin> elencoSiacROrdinativoTsMovgestT =
		// siacROrdinativoTsMovgestTsRepository.findValidoByIdMovGestTs(idEnte,
		// getNow(), siacTMovgestTs.getMovgestTsId());

		elencoSiacROrdinativoTsMovgestT = DatiOperazioneUtil.soloValidi(
				elencoSiacROrdinativoTsMovgestT, getNow());
		if (elencoSiacROrdinativoTsMovgestT != null
				&& elencoSiacROrdinativoTsMovgestT.size() > 0) {
			for (SiacROrdinativoTsMovgestTFin siacROrdinativoTsMovgestT : elencoSiacROrdinativoTsMovgestT) {
				SiacTOrdinativoTFin siacTOrdinativoT = siacROrdinativoTsMovgestT
						.getSiacTOrdinativoT();
				List<SiacTOrdinativoTFin> soloSeValido = DatiOperazioneUtil
						.soloValidi(toList(siacTOrdinativoT), getNow());
				if (soloSeValido != null && soloSeValido.size() == 1) {
					// la quota � valida
					// adesso controllo che sia in stato valido anche
					// l'ordinativo a della quota
					SiacTOrdinativoFin siacTOrdinativo = siacTOrdinativoT
							.getSiacTOrdinativo();

					List<SiacROrdinativoStatoFin> elencoSiacROrdinativoStato = siacTOrdinativo
							.getSiacROrdinativoStatos();
					elencoSiacROrdinativoStato = DatiOperazioneUtil
							.soloValidi(elencoSiacROrdinativoStato, getNow());

					if (elencoSiacROrdinativoStato != null
							&& elencoSiacROrdinativoStato.size() > 0) {
						for (SiacROrdinativoStatoFin siacROrdinativoStato : elencoSiacROrdinativoStato) {
							if (!siacROrdinativoStato
									.getSiacDOrdinativoStato()
									.getOrdStatoCode()
									.equalsIgnoreCase(
											CostantiFin.D_ORDINATIVO_STATO_ANNULLATO)) {
								// l'ordinativo � in stato valido, cio� non
								// annullato
								// quindi la quota estratta � valida ed � legata
								// ad un ordinativo valido
								// devo allora prenderla in considerazione per
								// il calcolo

								List<SiacTOrdinativoTsDetFin> listaSiacTOrdinativoTsDet = siacTOrdinativoT
										.getSiacTOrdinativoTsDets();
								listaSiacTOrdinativoTsDet = DatiOperazioneUtil
										.soloValidi(listaSiacTOrdinativoTsDet,
												getNow());

								if (listaSiacTOrdinativoTsDet != null
										& listaSiacTOrdinativoTsDet.size() > 0) {
									for (SiacTOrdinativoTsDetFin siacTOrdinativoTsDet : listaSiacTOrdinativoTsDet) {
										// estraggo solo la riga valida e
										// relativa all'importo attuale
										if (siacTOrdinativoTsDet
												.getDataFineValidita() == null
												&& siacTOrdinativoTsDet
														.getSiacDOrdinativoTsDetTipo()
														.getOrdTsDetTipoCode()
														.equalsIgnoreCase(
																CostantiFin.D_ORDINATIVO_TS_DET_TIPO_IMPORTO_ATTUALE)) {
											elencoSiacTOrdinativoTsDet
													.add(siacTOrdinativoTsDet);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		// Termino restituendo l'oggetto di ritorno:
		return elencoSiacTOrdinativoTsDet;
	}

	/**
	 * calcola la disponibilita' ad incassare di un accertamento indicato
	 * 
	 * @param siacTMovgestTs
	 * @param statoCod
	 * @param idEnte
	 * @return
	 */
	@Deprecated
	protected BigDecimal calcolaDisponibiltaAIncassareAccertamentoOLD(
			SiacTMovgestTsFin siacTMovgestTs, String statoCod, Integer idEnte) {

		// SE Accertamento.stato = DEFINITIVO
		// disponibilitaIncassare = accertamento.importoAttuale -
		// SOMMATORIAsub-ordinativo.importoAttuale
		// Ricercando tutti i sub con stato VALIDO relativi a tutti gli
		// ordinativi con stato VALIDO dell�accertamento.
		// ALTRIMENTI
		// disponibilitaIncassare = 0

		BigDecimal disponibilitaIncassare = BigDecimal.ZERO;
		if (!StringUtilsFin.isEmpty(statoCod)
				&& (statoCod.equals(CostantiFin.MOVGEST_STATO_DEFINITIVO) || statoCod
						.equals(CostantiFin.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE))) {
			BigDecimal importoAttualeAccertamento = siacTMovgestTsDetRepository
					.findImporto(idEnte, CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE,
							siacTMovgestTs.getUid());
			BigDecimal totaleSubOrdinativi = BigDecimal.ZERO;
			List<SiacTOrdinativoTsDetFin> elencoSiacTOrdinativoTsDet = findQuoteValideFromAccertamentoSubAccertamento(
					idEnte, siacTMovgestTs);
			if (elencoSiacTOrdinativoTsDet != null
					&& elencoSiacTOrdinativoTsDet.size() > 0) {
				for (SiacTOrdinativoTsDetFin siacTOrdinativoTsDet : elencoSiacTOrdinativoTsDet) {
					totaleSubOrdinativi = totaleSubOrdinativi
							.add(siacTOrdinativoTsDet.getOrdTsDetImporto());
				}
			}
			disponibilitaIncassare = importoAttualeAccertamento
					.subtract(totaleSubOrdinativi);
		} else {
			disponibilitaIncassare = BigDecimal.ZERO;
		}
		// Termino restituendo l'oggetto di ritorno:
		return disponibilitaIncassare;
	}

	/**
	 * calcola la disponibilita' ad incassare di un accertamento indicato
	 * 
	 * @param siacTMovgestTs
	 * @param statoCod
	 * @param idEnte
	 * @return
	 */
	protected DisponibilitaMovimentoGestioneContainer calcolaDisponibiltaAIncassareAccertamento(
			SiacTMovgestTsFin siacTMovgestTs, String statoCod, Integer idEnte) {
		// nuovo metodo che claudio mi ha dato in sostituzione
		// di quello commentato di seguito
		// 25-11-2014 - CR calcolo disp liq tramite function su database

		// il vecchio calcolo era questo:
		// calcolaDisponibiltaAIncassareAccertamentoOLD(siacTMovgestTs, statoCod, idEnte);

		if (siacTMovgestTs != null) {
			BigDecimal disp = accertamentoDao.calcolaDisponibilitaAIncassare(siacTMovgestTs.getMovgestTsId());
			return new DisponibilitaMovimentoGestioneContainer(disp, "Disponibilita' calcolata dalla function");
		}
		return new DisponibilitaMovimentoGestioneContainer(BigDecimal.ZERO, "Movimento di gestione non fornito alla procedura Java");
	}


	/**
	 * calcola la disponibilita' ad incassare di un accertamento indicato
	 * 
	 * @param siacTMovgestTs
	 * @param statoCod
	 * @param idEnte
	 * @return
	 */
	public DisponibilitaMovimentoGestioneContainer calcolaDisponibiltaAIncassareAccertamentoOSub(Integer uidMovimentoGestione) {
		
		// 25-11-2014 - CR calcolo disp tramite function su database
		BigDecimal disp;
		if (uidMovimentoGestione != null) {
			disp = accertamentoDao.calcolaDisponibilitaAIncassare(uidMovimentoGestione);
			return new DisponibilitaMovimentoGestioneContainer(disp, "Disponibilita' calcolata dalla function");
		}
		return new DisponibilitaMovimentoGestioneContainer(BigDecimal.ZERO, "Movimento di gestione non fornito alla procedura Java");
	}
	
	
	/**
	 * calcola la disponibilita' ad incassare di un accertamento indicato
	 * 
	 * @param siacTMovgestTs
	 * @param statoCod
	 * @param idEnte
	 * @return
	 */
	public BigDecimal calcolaDisponibiltaAIncassareAccertamentoOSub(SiacTMovgestTsFin siacTMovgestTs) {
		
		// 25-11-2014 - CR calcolo disp tramite function su database
		BigDecimal disp = BigDecimal.ZERO;
		
		if (siacTMovgestTs != null) {
			disp = accertamentoDao.calcolaDisponibilitaAIncassare(siacTMovgestTs.getMovgestTsId());
		}
		return disp;
	}

	/**
	 * calcola la disponibilita' ad incassare di un accertamento indicato,
	 * versione ottimizzata
	 * 
	 * @param siacTMovgestTs
	 * @param statoCod
	 * @param idEnte
	 * @param ottimizzazioneDto
	 * @return
	 */
	protected DisponibilitaMovimentoGestioneContainer calcolaDisponibiltaAIncassareAccertamentoOPT(
			SiacTMovgestTsFin siacTMovgestTs, String statoCod, Integer idEnte,
			OttimizzazioneMovGestDto ottimizzazioneDto) {

		// SE Accertamento.stato = DEFINITIVO
		// disponibilitaIncassare = accertamento.importoAttuale -
		// SOMMATORIAsub-ordinativo.importoAttuale
		// Ricercando tutti i sub con stato VALIDO relativi a tutti gli
		// ordinativi con stato VALIDO dell�accertamento.
		// ALTRIMENTI
		// disponibilitaIncassare = 0

		// 25-11-2014 - CR calcolo disp liq tramite function su database
		BigDecimal disponibilitaIncassare = ottimizzazioneDto
				.estraiDisponibileIncassare(siacTMovgestTs.getMovgestTsId());

		// il vecchio calcolo era questo:
		/*
		 * if(!StringUtils.isEmpty(statoCod) &&
		 * (statoCod.equals(CostantiFin.MOVGEST_STATO_DEFINITIVO) ||
		 * statoCod.equals
		 * (CostantiFin.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE))){ BigDecimal
		 * importoAttualeAccertamento =
		 * ottimizzazioneDto.estraiImporto(siacTMovgestTs.getUid(),
		 * CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE); BigDecimal
		 * totaleSubOrdinativi = BigDecimal.ZERO; List<SiacTOrdinativoTsDetFin>
		 * elencoSiacTOrdinativoTsDet =
		 * findQuoteValideFromAccertamentoSubAccertamentoOPT(idEnte,
		 * siacTMovgestTs,ottimizzazioneDto);
		 * if(elencoSiacTOrdinativoTsDet!=null &&
		 * elencoSiacTOrdinativoTsDet.size()>0){ for(SiacTOrdinativoTsDetFin
		 * siacTOrdinativoTsDet : elencoSiacTOrdinativoTsDet){
		 * totaleSubOrdinativi =
		 * totaleSubOrdinativi.add(siacTOrdinativoTsDet.getOrdTsDetImporto()); }
		 * } disponibilitaIncassare =
		 * importoAttualeAccertamento.subtract(totaleSubOrdinativi); }else{
		 * disponibilitaIncassare = BigDecimal.ZERO; }
		 */
		// Termino restituendo l'oggetto di ritorno:
		return new DisponibilitaMovimentoGestioneContainer(disponibilitaIncassare, "Disponibilita' calcolata dalla function");
	}

	/**
	 * calcola la disponibilita' ad incassare di un sub-accertamento indicato
	 * 
	 * @param siacTMovgest
	 * @param statoCod
	 * @param idEnte
	 * @return
	 */
	protected DisponibilitaMovimentoGestioneContainer calcolaDisponibiltaAIncassareSubAccertamento(
			SiacTMovgestTsFin siacTMovgest, String statoCod, Integer idEnte) {

		// 25-11-2014 - CR calcolo disp liq tramite function su database

		// COMMENTATO IL VECCHIO CODICE:

		// SE subimpegno.stato = DEFINITIVO
		// disponibilitaIncassare = subaccertamento.importoAttuale -
		// SOMMATORIAsub-ordinbativo.importoAttuale
		// Ricercando tutti i sub con stato VALIDO relativi a tutti i MANDATI
		// con stato VALIDO legati al subaccertamento

		// BigDecimal importoAttualeSubAccertamento =
		// siacTMovgestTsDetRepository.findImporto(idEnte,
		// CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE, siacTMovgest.getUid());
		// BigDecimal disponibilitaIncassare = BigDecimal.ZERO;
		// if(!StringUtils.isEmpty(statoCod) &&
		// statoCod.equals(CostantiFin.MOVGEST_STATO_DEFINITIVO)){
		// BigDecimal totaleSubOrdinativi = BigDecimal.ZERO;
		// List<SiacTOrdinativoTsDetFin> elencoSiacTOrdinativoTsDet =
		// findQuoteValideFromAccertamentoSubAccertamento(idEnte, siacTMovgest);
		// if(elencoSiacTOrdinativoTsDet!=null &&
		// elencoSiacTOrdinativoTsDet.size()>0){
		// for(SiacTOrdinativoTsDetFin siacTOrdinativoTsDet :
		// elencoSiacTOrdinativoTsDet){
		// totaleSubOrdinativi =
		// totaleSubOrdinativi.add(siacTOrdinativoTsDet.getOrdTsDetImporto());
		// }
		// }
		// disponibilitaIncassare =
		// importoAttualeSubAccertamento.subtract(totaleSubOrdinativi);
		// }else{
		// disponibilitaIncassare = BigDecimal.ZERO;
		// }
		// //Termino restituendo l'oggetto di ritorno:
		// return disponibilitaIncassare;

		// IN FAVORE DEL NUOVO:

		return calcolaDisponibiltaAIncassareAccertamento(siacTMovgest,
				statoCod, idEnte);

	}

	/**
	 * calcola la disponibilita' ad incassare di un sub-accertamento indicato,
	 * versione ottimizzata
	 * 
	 * @param siacTMovgestTs
	 * @param statoCod
	 * @param idEnte
	 * @param ottimizzazioneDto
	 * @return
	 */
	protected DisponibilitaMovimentoGestioneContainer calcolaDisponibiltaAIncassareSubAccertamentoOPT(
			SiacTMovgestTsFin siacTMovgestTs, String statoCod, Integer idEnte,
			OttimizzazioneMovGestDto ottimizzazioneDto) {
		// SE subimpegno.stato = DEFINITIVO
		// disponibilitaIncassare = subaccertamento.importoAttuale -
		// SOMMATORIAsub-ordinbativo.importoAttuale
		// Ricercando tutti i sub con stato VALIDO relativi a tutti i MANDATI
		// con stato VALIDO legati al subaccertamento

		// 25-11-2014 - CR calcolo disp liq tramite function su database
		BigDecimal disponibilitaIncassare = ottimizzazioneDto
				.estraiDisponibileIncassare(siacTMovgestTs.getMovgestTsId());

		// il vecchio calcolo era questo:

		// BigDecimal importoAttualeSubAccertamento =
		// ottimizzazioneDto.estraiImporto(siacTMovgestTs.getMovgestTsId(),
		// CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE);

		/*
		 * BigDecimal disponibilitaIncassare = BigDecimal.ZERO;
		 * if(!StringUtils.isEmpty(statoCod) &&
		 * statoCod.equals(CostantiFin.MOVGEST_STATO_DEFINITIVO)){ //
		 * disponibilitaIncassare = new BigDecimal("14"); BigDecimal
		 * totaleSubOrdinativi = BigDecimal.ZERO; List<SiacTOrdinativoTsDetFin>
		 * elencoSiacTOrdinativoTsDet =
		 * findQuoteValideFromAccertamentoSubAccertamentoOPT(idEnte,
		 * siacTMovgestTs,ottimizzazioneDto);
		 * if(elencoSiacTOrdinativoTsDet!=null &&
		 * elencoSiacTOrdinativoTsDet.size()>0){ for(SiacTOrdinativoTsDetFin
		 * siacTOrdinativoTsDet : elencoSiacTOrdinativoTsDet){
		 * totaleSubOrdinativi =
		 * totaleSubOrdinativi.add(siacTOrdinativoTsDet.getOrdTsDetImporto()); }
		 * } disponibilitaIncassare =
		 * importoAttualeSubAccertamento.subtract(totaleSubOrdinativi); }else{
		 * disponibilitaIncassare = BigDecimal.ZERO; }
		 */
		// Termino restituendo l'oggetto di ritorno:
		return new DisponibilitaMovimentoGestioneContainer(disponibilitaIncassare, "Disponibilita' calcolata dalla function");
	}
	
	// Ricercando tutti i submandati con stato VALIDO relativi a tutti gli
	// ordinativi con stato VALIDO di tutte le LIQUIDAZIONI
	// con stato VALIDO legate all'impegno e ai suoi subimpegni.
	private List<SiacTOrdinativoTsDetFin> findQuoteValideFromImpegno(
			Integer idEnte, SiacTMovgestFin siacTMovgest,
			SiacTMovgestTsFin siacTMovgestTs,OttimizzazioneMovGestDto ottimizzazioneMovGestDtoPerISub) {
		
		// Tutti i sub-ordinativi con stato VALIDO relativi a tutti gli
		// ordinativi con stato VALIDO dell'accertamento.
		
		List<SiacTOrdinativoTsDetFin> elencoSiacTOrdinativoTsDet = new ArrayList<SiacTOrdinativoTsDetFin>();

		// Controllo se ci sono legami diretti tra l'impegno e liquidazioni
		List<SiacRLiquidazioneMovgestFin> elencoSiacRLiquidazioneMovgest = null;
		
		if(ottimizzazioneMovGestDtoPerISub!=null){
			//NEW OTTIMIZZATO
			elencoSiacRLiquidazioneMovgest = ottimizzazioneMovGestDtoPerISub.filtraSiacRLiquidazioneMovgestFinByMovgestTs(siacTMovgestTs.getMovgestTsId());
		} else {
			//OLD LENTO
			elencoSiacRLiquidazioneMovgest = siacTMovgestTs.getSiacRLiquidazioneMovgests();
		}
		
		if (elencoSiacRLiquidazioneMovgest != null && elencoSiacRLiquidazioneMovgest.size() > 0) {
			// Ci sono legami diretti tra impegno e liquidazioni
			// Non ci saranno quindi legami tra sub-impegni e liquidazioni
			
			elencoSiacTOrdinativoTsDet = estraiElencoSiacTOrdinativoTsDet(elencoSiacRLiquidazioneMovgest, idEnte,ottimizzazioneMovGestDtoPerISub);
			
		} else {
			// Non ci sono legami diretti tra impegno e liquidazioni
			// Verifico se ci sono legami tra i suoi sub-impegni e liquidazioni
			List<SiacTMovgestTsFin> elencoSiacTMovgestTs = siacTMovgest.getSiacTMovgestTs();
			if (elencoSiacTMovgestTs != null && elencoSiacTMovgestTs.size() > 0) {
				for (SiacTMovgestTsFin siacTMovgestTsIterato : elencoSiacTMovgestTs) {
					if (CommonUtil.isValidoSiacTBase(siacTMovgestTsIterato, getNow()) 
							&& siacTMovgestTsIterato.getMovgestTsId().compareTo(siacTMovgestTs.getMovgestTsId()) != 0) {
						// controllo se ci sono liquidazioni collegate
						// verifico se ci sono quote di orinativo collegate alla
						// liquidazione
						
						List<SiacRLiquidazioneMovgestFin> elencoSiacRLiquidazioneMovgestSubImp = null;
						if(ottimizzazioneMovGestDtoPerISub!=null){
							//NEW OTTIMIZZATO
							elencoSiacRLiquidazioneMovgestSubImp = ottimizzazioneMovGestDtoPerISub.filtraSiacRLiquidazioneMovgestFinByMovgestTs(siacTMovgestTsIterato.getMovgestTsId());
						} else {
							//OLD LENTO
							elencoSiacRLiquidazioneMovgestSubImp = siacRLiquidazioneMovgestRepository.findByEnteAndMovGestTsId(idEnte, getNow(),siacTMovgestTsIterato.getMovgestTsId());
						}
						
						//FIX PER JIRA  SIAC-3960 (non veniva fatto l'add all ma riportava solo l'ultimo ciclato)
						List<SiacTOrdinativoTsDetFin> elencoSiacTOrdinativoTsDetSubIt = estraiElencoSiacTOrdinativoTsDet(elencoSiacRLiquidazioneMovgestSubImp, idEnte,ottimizzazioneMovGestDtoPerISub);
						elencoSiacTOrdinativoTsDet = CommonUtil.addAllConNew(elencoSiacTOrdinativoTsDet, elencoSiacTOrdinativoTsDetSubIt);
						//
					}
				}
			}
		}

		// Termino restituendo l'oggetto di ritorno:
		return elencoSiacTOrdinativoTsDet;
	}
	
	private List<SiacTOrdinativoTsDetFin> findQuoteValideFromSubImpegno(Integer idEnte,
			SiacTMovgestTsFin siacTMovgestTs,OttimizzazioneMovGestDto ottimizzazioneMovGestDtoPerISub) {
		
		// Tutti i sub-ordinativi con stato VALIDO relativi a tutti gli
		// ordinativi con stato VALIDO del sub.
		
		List<SiacTOrdinativoTsDetFin> elencoSiacTOrdinativoTsDet = new ArrayList<SiacTOrdinativoTsDetFin>();

		// Controllo se ci sono legami diretti tra l'impegno e liquidazioni
		List<SiacRLiquidazioneMovgestFin> elencoSiacRLiquidazioneMovgest = null;
		
		if(ottimizzazioneMovGestDtoPerISub!=null){
			//NEW OTTIMIZZATO
			elencoSiacRLiquidazioneMovgest = ottimizzazioneMovGestDtoPerISub.filtraSiacRLiquidazioneMovgestFinByMovgestTs(siacTMovgestTs.getMovgestTsId());
		} else {
			//OLD LENTO
			elencoSiacRLiquidazioneMovgest = siacTMovgestTs.getSiacRLiquidazioneMovgests();
		}
		
		elencoSiacTOrdinativoTsDet = estraiElencoSiacTOrdinativoTsDet(elencoSiacRLiquidazioneMovgest, idEnte,ottimizzazioneMovGestDtoPerISub);

		// Termino restituendo l'oggetto di ritorno:
		return elencoSiacTOrdinativoTsDet;
	}
	
	
	private List<SiacTOrdinativoTsDetFin> estraiElencoSiacTOrdinativoTsDet(List<SiacRLiquidazioneMovgestFin> elencoSiacRLiquidazioneMovgest,Integer idEnte,
			OttimizzazioneMovGestDto ottimizzazioneMovGestDtoPerISub){
		final String methodName="estraiElencoSiacTOrdinativoTsDet";
		List<SiacTOrdinativoTsDetFin> elencoSiacTOrdinativoTsDet = new ArrayList<SiacTOrdinativoTsDetFin>();
		
		elencoSiacRLiquidazioneMovgest = CommonUtil.soloValidiSiacTBase(elencoSiacRLiquidazioneMovgest, getNow());
		
		if (elencoSiacRLiquidazioneMovgest != null && elencoSiacRLiquidazioneMovgest.size() > 0) {
			
			for (SiacRLiquidazioneMovgestFin siacRLiquidazioneMovgest : elencoSiacRLiquidazioneMovgest) {
				
				// Controllo lo stato della liquidazione
				SiacTLiquidazioneFin siacTLiquidazione = siacRLiquidazioneMovgest.getSiacTLiquidazione();
				
				List<SiacRLiquidazioneStatoFin> elencoSiacRLiquidazioneStato = siacTLiquidazione.getSiacRLiquidazioneStatos();
				SiacRLiquidazioneStatoFin siacRLiquidazioneStato = CommonUtil.getValidoSiacTBase(elencoSiacRLiquidazioneStato, getNow());
				
				//GIUGNO 2016 - fix per errore apparentemente randomico che portava ad una null pointer
				if(isStatoLiqNull(siacRLiquidazioneStato)){
					log.debug(methodName, "TROVATO PROBLEMA isStatoLiqNull(siacRLiquidazioneStato)");
					entityRefresh(siacTLiquidazione);
					siacTLiquidazioneRepository.flush();
					SiacTLiquidazioneFin siacTLiquidazioneReload =siacTLiquidazioneRepository.findOne(siacTLiquidazione.getLiqId());
					elencoSiacRLiquidazioneStato = siacTLiquidazioneReload.getSiacRLiquidazioneStatos();
					siacRLiquidazioneStato = CommonUtil.getValidoSiacTBase(elencoSiacRLiquidazioneStato, getNow());
					siacTLiquidazione = siacTLiquidazioneReload;
					
					if(isStatoLiqNull(siacRLiquidazioneStato)){
						log.error(methodName, "KO, NON RISOLTO PROBLEMA isStatoLiqNull(siacRLiquidazioneStato)");
					} else {
						log.info(methodName, "OK, RISOLTO PROBLEMA isStatoLiqNull(siacRLiquidazioneStato)");	
					}
				}
				// END fix -
							
				if (!isStatoLiqNull(siacRLiquidazioneStato) && siacRLiquidazioneStato.getSiacDLiquidazioneStato().getLiqStatoCode().equalsIgnoreCase(CostantiFin.LIQUIDAZIONE_STATO_VALIDO)) {
					// la liquidazione e' in stato valido verifico se ci sono quote di orinativo collegate alla liquidazione
					
					
					List<SiacRLiquidazioneOrdFin> elencoSiacRLiquidazioneOrd = null;
					if(ottimizzazioneMovGestDtoPerISub==null){
						//VERSIONE OLD NON OTTIMIZZATA
						elencoSiacRLiquidazioneOrd = siacRLiquidazioneOrdRepository.findValidoByIdLiquidazione(idEnte,getNow(),siacTLiquidazione.getLiqId());
					} else {
						//VERSIONE NEW OTTIMIZZATA
						elencoSiacRLiquidazioneOrd = ottimizzazioneMovGestDtoPerISub.filtraSiacRLiquidazioneOrdFinBySiacTLiquidazioneFin(siacTLiquidazione);
					}
					
					elencoSiacRLiquidazioneOrd = CommonUtil.soloValidiSiacTBase(elencoSiacRLiquidazioneOrd, getNow());
					
					if (elencoSiacRLiquidazioneOrd != null && elencoSiacRLiquidazioneOrd.size() > 0) {
						
						for (SiacRLiquidazioneOrdFin siacRLiquidazioneOrd : elencoSiacRLiquidazioneOrd) {
							
							// ci sono relazioni liquidazione sub-ordinativo valide,  controllo se  la quota e' valida
							SiacTOrdinativoTFin siacTOrdinativoT = siacRLiquidazioneOrd.getSiacTOrdinativoT();
							
							if (CommonUtil.isValidoSiacTBase(siacTOrdinativoT, getNow())) {
								// la quota  e' valida adesso controllo  che sia in stato valido anche l'ordinativo  a della quota
								SiacTOrdinativoFin siacTOrdinativo = siacTOrdinativoT.getSiacTOrdinativo();
								// Da prendere direttamente dal db per problemi  con cache di hibernate sull'inserimento quote ordinativo
								
								
								List<SiacROrdinativoStatoFin> elencoSiacROrdinativoStato = null;
								if(ottimizzazioneMovGestDtoPerISub==null){
									//VERSIONE OLD NON OTTIMIZZATA
									 elencoSiacROrdinativoStato = siacROrdinativoStatoRepository.findSiacROrdinativoStatoValidoByIdOrdinativo(idEnte,siacTOrdinativo.getOrdId(),getNow());
								} else {
									//VERSIONE NEW OTTIMIZZATA
									elencoSiacROrdinativoStato = ottimizzazioneMovGestDtoPerISub.filtraSiacROrdinativoStatoFinBySiacTOrdinativoFin(siacTOrdinativo);
								}
								
								SiacROrdinativoStatoFin siacROrdinativoStato = CommonUtil.getValidoSiacTBase(elencoSiacROrdinativoStato, getNow());
								
								if (!siacROrdinativoStato.getSiacDOrdinativoStato().getOrdStatoCode().equalsIgnoreCase(CostantiFin.D_ORDINATIVO_STATO_ANNULLATO)) {
									// l'ordinativo e' in stato valido cioe' non annullato quindi la quota estratta e' valida e legata ad un ordinativo valido
									//allora devo prenderla in considerazione per il calcolo 
									
									List<SiacTOrdinativoTsDetFin> listaSiacTOrdinativoTsDet = null;
									if(ottimizzazioneMovGestDtoPerISub==null){
										//VERSIONE OLD NON OTTIMIZZATA
										listaSiacTOrdinativoTsDet = siacTOrdinativoTsDetRepository.findOrdinativoTsDetValidoByOrdTsId(idEnte,siacTOrdinativoT.getOrdTsId(),getNow());
									} else {
										//VERSIONE NEW OTTIMIZZATA
										listaSiacTOrdinativoTsDet = ottimizzazioneMovGestDtoPerISub.filtraSiacTOrdinativoTsDetFinBySiacTOrdinativoTFin(siacTOrdinativoT);
									}
									
									listaSiacTOrdinativoTsDet = CommonUtil.soloValidiSiacTBase(listaSiacTOrdinativoTsDet, getNow());
									if (listaSiacTOrdinativoTsDet != null && listaSiacTOrdinativoTsDet.size() > 0) {
										for (SiacTOrdinativoTsDetFin siacTOrdinativoTsDet : listaSiacTOrdinativoTsDet) {
											// estraggo solo la riga valida e relativa all'importo attuale:
											if (siacTOrdinativoTsDet.getSiacDOrdinativoTsDetTipo().getOrdTsDetTipoCode().equalsIgnoreCase(CostantiFin.D_ORDINATIVO_TS_DET_TIPO_IMPORTO_ATTUALE)) {
												elencoSiacTOrdinativoTsDet.add(siacTOrdinativoTsDet);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		return elencoSiacTOrdinativoTsDet;
		
	}
	
	/**
	 * GIUGNO 2016 Utility per il fix per errore apparentemente randomico che portava ad una null pointer
	 * @param siacRLiquidazioneStato
	 * @return
	 */
	protected boolean isStatoLiqNull(SiacRLiquidazioneStatoFin siacRLiquidazioneStato){
		if(siacRLiquidazioneStato==null || siacRLiquidazioneStato.getSiacDLiquidazioneStato() == null || 
				siacRLiquidazioneStato.getSiacDLiquidazioneStato().getLiqStatoCode() ==null){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Wrapper di retroCompatibilita'
	 * @param siacTMovgest
	 * @param siacTMovgestTs
	 * @param statoCod
	 * @param idEnte
	 * @return
	 */
	protected DisponibilitaMovimentoGestioneContainer calcolaDisponibilitaAPagareImpegno( SiacTMovgestTsFin siacTMovgestTs,
			String statoCod, Integer idEnte) {
		return calcolaDisponibilitaAPagareImpegno(siacTMovgestTs, statoCod, idEnte, null,null);
	}

	/**
	 * Calcola la disponibilita' di cassa di un impegno
	 * 
	 * @param siacTMovgest
	 * @param siacTMovgestTs
	 * @param statoCod
	 * @param idEnte
	 * @return
	 */
	protected DisponibilitaMovimentoGestioneContainer calcolaDisponibilitaAPagareImpegno(
			SiacTMovgestTsFin siacTMovgestTs,
			String statoCod, Integer idEnte,OttimizzazioneMovGestDto ottimizzazioneMovGestDtoPerISub,BigDecimal importoAttualeImpegno) {
		final String methodName="calcolaDisponibilitaAPagareImpegno";
		// SE impegno.stato = DEFINITIVO o DEFINITIVO NON LIQUIDABILE
		// disponibilitaPagare = impegno.importoAttuale - SOMMATORIA
		// submandato.importoAttuale
		// Ricercando tutti i submandati con stato VALIDO relativi a tutti gli
		// ordinativi con stato VALIDO di tutte le LIQUIDAZIONI
		// con stato VALIDO legate all'impegno e ai suoi subimpegni.
		// ALTRIMENTI
		// disponibilitaPagare = 0
		BigDecimal disponibilitaAPagare = BigDecimal.ZERO;
		
		Integer movgestTsId = siacTMovgestTs.getMovgestTsId();
		
		log.debug(methodName, "movgestTsId : " + movgestTsId);
		
		if(ottimizzazioneMovGestDtoPerISub!=null){
			disponibilitaAPagare = ottimizzazioneMovGestDtoPerISub.estraiDisponibilePagareDaFunction(movgestTsId);
			log.debug(methodName, "estraiDisponibilePagareDaFunction - disponibilitaAPagare : " + disponibilitaAPagare);
		} else {
			disponibilitaAPagare = impegnoDao.calcolaDisponibileAPagare(movgestTsId);
			log.debug(methodName, "calcolaDisponibileAPagare - calcolaDisponibileAPagare : " + disponibilitaAPagare);
		}
		
		/*
		if (!StringUtils.isEmpty(statoCod) && (statoCod.equals(CostantiFin.MOVGEST_STATO_DEFINITIVO) || statoCod.equals(CostantiFin.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE))) {
			
			// se il chiamante ha gia' calcolato l'importo attuale e' buona norma che vi venga passato per ottimizzare:
			if(importoAttualeImpegno==null){
				importoAttualeImpegno = siacTMovgestTsDetRepository.findImporto(idEnte, CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE,siacTMovgestTs.getUid());
			}
			//

			BigDecimal totaleSubOrdinativi = BigDecimal.ZERO;
			List<SiacTOrdinativoTsDetFin> elencoSiacTOrdinativoTsDet = findQuoteValideFromImpegno(idEnte, siacTMovgest, siacTMovgestTs,ottimizzazioneMovGestDtoPerISub);
			if (elencoSiacTOrdinativoTsDet != null && elencoSiacTOrdinativoTsDet.size() > 0) {
				for (SiacTOrdinativoTsDetFin siacTOrdinativoTsDet : elencoSiacTOrdinativoTsDet) {
					totaleSubOrdinativi = totaleSubOrdinativi.add(siacTOrdinativoTsDet.getOrdTsDetImporto());
				}
			}
			disponibilitaAPagare = importoAttualeImpegno.subtract(totaleSubOrdinativi);
		} else {
			disponibilitaAPagare = BigDecimal.ZERO;
		} */
		
		
		// Termino restituendo l'oggetto di ritorno:
		return new DisponibilitaMovimentoGestioneContainer(disponibilitaAPagare, "Disponibilita calcolata dalla function");
	}

	
	/**
	 * 
	 * Wrapper di retro compatibilita'
	 * 
	 * @param siacTMovgestTs
	 * @param statoCod
	 * @param idEnte
	 * @return
	 */
	protected DisponibilitaMovimentoGestioneContainer calcolaDisponibilitaAPagareSubImpegno(
			SiacTMovgestTsFin siacTMovgestTs, String statoCod, Integer idEnte) {
		return calcolaDisponibilitaAPagareSubImpegno(siacTMovgestTs, statoCod, idEnte, null,null);
	}

	/**
	 * Calcola il disponibile di cassa di un sub impegno
	 * 
	 * @param siacTMovgestTs
	 * @param statoCod
	 * @param idEnte
	 * @return
	 */
	protected DisponibilitaMovimentoGestioneContainer calcolaDisponibilitaAPagareSubImpegno(SiacTMovgestTsFin siacTMovgestTs, String statoCod, Integer idEnte,
			OttimizzazioneMovGestDto ottimizzazioneDto, BigDecimal importoAttualeSubImpegno) {
	
		BigDecimal disponibilitaAPagareSubImpegno = BigDecimal.ZERO;
		
		Integer movgestTsId = siacTMovgestTs.getMovgestTsId();
		
		if(ottimizzazioneDto!=null){
			disponibilitaAPagareSubImpegno = ottimizzazioneDto.estraiDisponibilePagareDaFunction(movgestTsId);
		} else {
			disponibilitaAPagareSubImpegno = impegnoDao.calcolaDisponibileAPagare(movgestTsId);
		}
		
		/*
		
		// SE subimpegno.stato = DEFINITIVO
		// disponibilitaPagare = subimpegno.importoAttuale -
		// SOMMATORIAsub-ordinbativo.importoAttuale
		// Ricercando tutti i sub con stato VALIDO relativi a tutti i MANDATI
		// con stato VALIDO di tutte le LIQUIDAZIONI con stato VALIDO legate al
		// subimpegno.
		// ALTRIMENTI
		// disponibilitaPagare = 0
		
		if (!StringUtils.isEmpty(statoCod) && statoCod.equals(CostantiFin.MOVGEST_STATO_DEFINITIVO)) {
			
			// se il chiamante ha gia' calcolato l'importo attuale e' buona norma che vi venga passato per ottimizzare:
			if(importoAttualeSubImpegno==null){
				importoAttualeSubImpegno = siacTMovgestTsDetRepository.findImporto(idEnte, CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE,siacTMovgestTs.getUid());
			}
			//
			
			BigDecimal totaleSubOrdinativi = BigDecimal.ZERO;
			List<SiacTOrdinativoTsDetFin> elencoSiacTOrdinativoTsDet = findQuoteValideFromSubImpegno(idEnte, siacTMovgestTs,ottimizzazioneDto);
			if (elencoSiacTOrdinativoTsDet != null && elencoSiacTOrdinativoTsDet.size() > 0) {
				for (SiacTOrdinativoTsDetFin siacTOrdinativoTsDet : elencoSiacTOrdinativoTsDet) {
					totaleSubOrdinativi = totaleSubOrdinativi.add(siacTOrdinativoTsDet.getOrdTsDetImporto());
				}
			}
			disponibilitaAPagareSubImpegno = importoAttualeSubImpegno.subtract(totaleSubOrdinativi);
		} else {
			disponibilitaAPagareSubImpegno = BigDecimal.ZERO;
		}
		
		*/
		
		// Termino restituendo l'oggetto di ritorno:
		return new DisponibilitaMovimentoGestioneContainer(disponibilitaAPagareSubImpegno, "Disponibilita' calcolata dalla function");
	}

	/**
	 * Va ad individuare lo specifico record in siac_t_atto_amm a partire dai
	 * dati in un AttoAmministrativo
	 * 
	 * @param attAmm
	 * @param idEnte
	 * @return
	 */
	protected SiacTAttoAmmFin getSiacTAttoAmmFromAttoAmministrativo(
			AttoAmministrativo attAmm, int idEnte) {
		SiacTAttoAmmFin attoTrovato = null;
		Timestamp now = new Timestamp(currentTimeMillis());
		if (attAmm != null) {
			String anno = Integer.toString(attAmm.getAnno());
			Integer numero = attAmm.getNumero();
			String tipoCode = null;
			
			if (attAmm.getTipoAtto() != null
					&& !StringUtilsFin.isEmpty(attAmm.getTipoAtto().getCodice())) {
				
				// BACO! il codice del tipo atto non è detto che sia a due cifre (può anche essere di un solo carattere
				// tipo CMTO (ente = 3) la delibera a tipo atto 1 e non va ricercato con 01!!!
				// soluzione: eseguo la ricerca col tipo codice che arriva, se è null riprovo con le due cifre
				SiacDAttoAmmTipoFin siacDAttoAmministrativoTipo = siacDAttoAmmTipoFinRepository.findDAttoAmmTipoValidoByTipoCodiceAndIdEnte(idEnte, attAmm.getTipoAtto().getCodice(), now); 
				
				if(siacDAttoAmministrativoTipo ==null){
					
					tipoCode = attAmm.getTipoAtto().getCodice();
					if (tipoCode.length() == 1) {
						tipoCode = StringUtilsFin.formattaConZeriInTesta(tipoCode, 2);
					}
					
				}else{
					
					// se lo trovo uso quello trovato e non aggiungo i due zeri!
					tipoCode = siacDAttoAmministrativoTipo.getAttoammTipoCode();
				}
					
			}
			
			List<SiacTAttoAmmFin> siacTAttoAmms = siacTAttoAmmRepository.getValidoByNumeroAndAnnoAndTipo(idEnte, anno, now,	tipoCode, numero);

			if (siacTAttoAmms != null && siacTAttoAmms.size() > 0) {

				// DOBBIAMO ANCORA FILTRARE PER STRUTTURA AMMINISTRATIVA:
				if (attAmm.getStrutturaAmmContabile() != null && attAmm.getStrutturaAmmContabile().getUid()>0) {
					
					List<SiacTAttoAmmFin> siacTAttoAmmsFiltratiPerSac = new ArrayList<SiacTAttoAmmFin>();
					
					int idTClass = attAmm.getStrutturaAmmContabile().getUid();
					
					for (SiacTAttoAmmFin it : siacTAttoAmms) {
						
						SiacRAttoAmmClassFin legameValido = DatiOperazioneUtil.getValido(it.getSiacRAttoAmmClasses(),getNow());
						
						if (legameValido != null && legameValido.getSiacTClass().getUid().intValue() == idTClass) {
							siacTAttoAmmsFiltratiPerSac.add(it);
						}
					}
					// CI ASPETTIAMO UN SOLO ELEMENTO IN
					// siacTAttoAmmsFiltratiPerSac:
					if (siacTAttoAmmsFiltratiPerSac != null && siacTAttoAmmsFiltratiPerSac.size() > 0) {
						attoTrovato = siacTAttoAmmsFiltratiPerSac.get(0);
					}
				} else {
					// E' del tutto lecito avere provvedimenti privi di
					// struttura amministrativa:
					//prendo quello senza struttura:
					attoTrovato = siacTAttoAmms.get(0);
					for (SiacTAttoAmmFin it : siacTAttoAmms) {
						SiacRAttoAmmClassFin legameValido = DatiOperazioneUtil.getValido(it.getSiacRAttoAmmClasses(),getNow());
						if (legameValido == null) {
							attoTrovato = it;
							break;
						}
					}
				}
			}
		}
		// Termino restituendo l'oggetto di ritorno:
		return attoTrovato;
	}

	protected AttoAmministrativo buildAttoAmministrativo(int idEnte,
			Integer numero, String codice, Integer annoAtto,
			Integer idStrutturaAmm) {
		return buildAttoAmministrativo(idEnte, numero, codice, annoAtto, idStrutturaAmm,null);
	}
	/**
	 * Istanzia e setta i dati minimi per poterlo identificare
	 * 
	 * @param idEnte
	 * @param numero
	 * @param codice
	 * @param annoAtto
	 * @param idStrutturaAmm
	 * @return
	 */
	protected AttoAmministrativo buildAttoAmministrativo(int idEnte,
			Integer numero, String codice, Integer annoAtto,
			Integer idStrutturaAmm, Integer uidTipoAtto) {
		// provvedimento
		AttoAmministrativo am = null;
		if (null != annoAtto) {
			// Istanzio l'oggetto:
			am = new AttoAmministrativo();
			// Numero, anno e stato operativo:
			am.setNumero(numero);
			am.setAnno(annoAtto);
			// Tipo atto:
			TipoAtto ta = new TipoAtto();
			ta.setCodice(codice);
			if(uidTipoAtto!=null){
				ta.setUid(uidTipoAtto.intValue());
			}
			am.setTipoAtto(ta);
			// Struttura amministrativa selezionata
			if (idStrutturaAmm != null && idStrutturaAmm.intValue() > 0) {
				StrutturaAmministrativoContabile sac = new StrutturaAmministrativoContabile();
				sac.setUid(idStrutturaAmm);
				am.setStrutturaAmmContabile(sac);
			}
		}
		return am;
	}

	protected String determinaStatoEntita(Integer idEnte, Entita entita) {
		return "";
	}

	protected Timestamp getNow() {
		return new Timestamp(currentTimeMillis());
	}
	//

	// ////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Restituisce l'atto amministrativo.
	 * 
	 * @param atti
	 * @return
	 */
	protected AttoAmministrativo buildAttoAmministrativoFromListMovgestTsAttoAmm(List<SiacRMovgestTsAttoAmmFin> list) {
		return buildAttoAmministrativoFromListMovgestTsAttoAmm(DatiOperazioneUtil.getValido(list,null));
	}
	
	protected AttoAmministrativo buildAttoAmministrativoFromListMovgestTsAttoAmm(SiacRMovgestTsAttoAmmFin rAttoAmm) {
		AttoAmministrativo atto = null;
		if (rAttoAmm != null) {
			SiacTAttoAmmFin attoAmm = rAttoAmm.getSiacTAttoAmm();
			if (attoAmm != null) {
				atto = EntityToModelConverter.siacTAttoToAttoAmministrativo(attoAmm);
			}
		}
		return atto;
	}

	/**
	 * Metodo di comodo per istnaziare al volo un oggetto RicercaAtti
	 * 
	 * @param siacTAttoAmm
	 * @return
	 */
	protected RicercaAtti buildRicercaAtti(SiacTAttoAmmFin siacTAttoAmm) {
		RicercaAtti ricercaAtti = new RicercaAtti();
		AttoAmministrativo atto = EntityToModelConverter
				.siacTAttoToAttoAmministrativo(siacTAttoAmm);
		ricercaAtti.setAnnoAtto(atto.getAnno());
		ricercaAtti.setNumeroAtto(atto.getNumero());
		ricercaAtti.setTipoAtto(atto.getTipoAtto());
		// EVENTUALE STRUTTURA AMMINISTRATIVA:
		if (atto.getStrutturaAmmContabile() != null) {
			StrutturaAmministrativoContabile sac = new StrutturaAmministrativoContabile();
			sac.setUid(atto.getStrutturaAmmContabile().getUid());
			ricercaAtti.setStrutturaAmministrativoContabile(sac);
		}
		return ricercaAtti;
	}
	
	/**
	 * Wrapper di retro compatibilita'
	 * @param siacTProgramma
	 * @param ottimizzazioneDto
	 * @return
	 */
	protected boolean isRilevanteFPV(SiacTProgrammaFin siacTProgramma) {
		return isRilevanteFPV(siacTProgramma, null);
	}

	/**
	 * Semplice utility per checkare se il progetto e' rilevante
	 * 
	 * @param siacTProgramma
	 * @return
	 */
	protected boolean isRilevanteFPV(SiacTProgrammaFin siacTProgramma,OttimizzazioneMovGestDto ottimizzazioneDto) {
		boolean rilevanteFPV = false;
		// verifico che abbia l'attributo rilevanteFPV
		SiacRProgrammaAttrFin siacRProgrammaAttr = getSiacRProgrammaAttr(siacTProgramma, ottimizzazioneDto, CostantiFin.PROGRAMMA_T_ATTR_RILEVANTE_FPV);
		if(siacRProgrammaAttr!=null && !StringUtilsFin.isEmpty(siacRProgrammaAttr.getBoolean_()) && siacRProgrammaAttr.getBoolean_().equals(CostantiFin.TRUE)) {
			rilevanteFPV = true;
		}
		return rilevanteFPV;
	}
	
	protected BigDecimal getValoreComplessivo(SiacTProgrammaFin siacTProgramma) {
		return getValoreComplessivo(siacTProgramma, null);
	}
	
	protected BigDecimal getValoreComplessivo(SiacTProgrammaFin siacTProgramma,OttimizzazioneMovGestDto ottimizzazioneDto) {
		BigDecimal valoreComplessivo = BigDecimal.ZERO;
		SiacRProgrammaAttrFin siacRProgrammaAttr = getSiacRProgrammaAttr(siacTProgramma, ottimizzazioneDto, CostantiFin.PROGRAMMA_T_ATTR_VALORE_COMPLESSIVO);
		if (siacRProgrammaAttr != null) {
			valoreComplessivo  = siacRProgrammaAttr.getNumerico();
		}
		return valoreComplessivo;
	}
	
	protected SiacRProgrammaAttrFin getSiacRProgrammaAttr(SiacTProgrammaFin siacTProgramma,OttimizzazioneMovGestDto ottimizzazioneDto,String codice) {
		SiacRProgrammaAttrFin trovato = null;
		if(siacTProgramma!=null && codice!=null){
			
			List<SiacRProgrammaAttrFin> rProgr = null;
			
			if(ottimizzazioneDto!=null){
				//RAMO OTTIMIZZATO
				rProgr = ottimizzazioneDto.filtraSiacRProgrammaAttrFinBySiacTProgrammaFin(siacTProgramma);
			} else {
				//RAMO CLASSICO
				rProgr = siacTProgramma.getSiacRProgrammaAttrs();
			}
			
			if (rProgr != null && !rProgr.isEmpty()) {
				for (SiacRProgrammaAttrFin siacRProgrammaAttr : rProgr) {
					if(CommonUtil.isValidoSiacTBase(siacRProgrammaAttr, getNow())){
						if (siacRProgrammaAttr.getSiacTAttr().getAttrCode().equals(codice)) {
							trovato  = siacRProgrammaAttr;
							break;
						}
					}
				}
			}
		}
		return trovato;
	}

	/**
	 * Metodo di comodo per recuperare la chiave logica di un capitolo avendo in
	 * input la sua chiave fisica nella tabella siac_t_bil_elem Ritorna null se
	 * non esiste
	 * 
	 * @param chiaveFisica
	 * @return
	 */
	public ChiaveLogicaCapitoloDto chiaveFisicaToLogicaCapitolo(int chiaveFisica) {
		ChiaveLogicaCapitoloDto chiave = null;
		if (chiaveFisica > 0) {
			// se la chiave e' significativa
			SiacTBilElemFin siatTBilElem = siacTBilElemRepository
					.findOne(chiaveFisica);
			if (siatTBilElem != null) {
				// OK elemento esistente
				chiave = new ChiaveLogicaCapitoloDto();

				// estraggo l'anno del capitolo:
				String annoString = siatTBilElem.getSiacTBil()
						.getSiacTPeriodo().getAnno();
				Integer annoInt = new Integer(annoString);
				chiave.setAnnoCapitolo(annoInt);

				// setto gli altri parametri:
				chiave.setNumeroCapitolo(new Integer(siatTBilElem.getElemCode()));
				chiave.setNumeroArticolo(new Integer(siatTBilElem
						.getElemCode2()));
				chiave.setNumeroUeb(new Integer(siatTBilElem.getElemCode3()));

			}
		}
		return chiave;
	}

	public CaricaDatiVisibilitaSacCapitoloDto caricaDatiVisibilitaSacCapitoloService(int elemId, DatiOperazioneDto datiOperazione) {
		CaricaDatiVisibilitaSacCapitoloDto datiVisibilita = new CaricaDatiVisibilitaSacCapitoloDto();
		SiacTBilElemFin siacTBilElem = siacTBilElemRepository.findOne(elemId);
		if(siacTBilElem!=null){
		
			Timestamp now = datiOperazione.getTs();
			Integer enteProprietarioId = datiOperazione.getSiacTEnteProprietario().getUid();
			
			List<Integer> idsAll = siacRBilElemSacVisibilitaFinRepository.getIdTClassVisibilitaAllValidiBySiacTBilElem(enteProprietarioId, elemId, now);
			
			if(!StringUtilsFin.isEmpty(idsAll)){
				datiVisibilita.setVisibiliAll(true);
			} else {
				datiVisibilita.setVisibiliAll(false);
				List<Integer> idsPuntuali = siacRBilElemSacVisibilitaFinRepository.getIdsTClassValidiBySiacTBilElem(enteProprietarioId, elemId, now);
				datiVisibilita.setIdSacVisibili(idsPuntuali);
			}
		}
		return datiVisibilita;
	}
	
	
	
	
	public Bilancio buildBilancioAnnoSuccessivo(Bilancio bilancio,
			DatiOperazioneDto datiOperazioneDto) {
		Bilancio bilancioAnnoSuccessivo = new Bilancio();
		int anno = bilancio.getAnno();
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getUid();
		List<SiacTBilFin> siacTBilList = siacTBilRepository.getValidoByAnno(
				idEnte, String.valueOf(anno + 1), datiOperazioneDto.getTs());
		if (siacTBilList != null && siacTBilList.size() > 0
				&& siacTBilList.get(0) != null) {
			SiacTBilFin siacTBil = siacTBilList.get(0);
			if (siacTBil != null) {
				bilancioAnnoSuccessivo.setAnno(anno + 1);
				bilancioAnnoSuccessivo.setUid(siacTBil.getBilId());
			}
		}
		return bilancioAnnoSuccessivo;
	}
	
	/**
	 * Per la paginazione nei servizi di ricerca
	 * @param totale
	 * @param numPagina
	 * @param numRisultatiPerPagina
	 * @return
	 */
	protected <T> List<T> getPaginata(List<T> totale, int numPagina, int numRisultatiPerPagina) {
		return CommonUtil.getPaginata(totale, numPagina, numRisultatiPerPagina);
	}
	
	protected String getIdsPaginati(List<String> totale, int numPagina, int numRisultatiPerPagina) {
		return CommonUtil.getIdsPaginati(totale, numPagina, numRisultatiPerPagina);
	}
	
	protected  List<Integer> getIdsPaginatiList( List<Integer> ids, int numPagina, int numRisultatiPerPagina) {
		return CommonUtil.getIdsPaginatiListIntegerToInteger(ids, numPagina, numRisultatiPerPagina);
	}
	
	
	protected SiacTMovgestTsFin findTestataByUidMovimento(Integer uid) {
		List<SiacTMovgestTsFin> siacTMovgestTs = siacTMovgestTsRepository.findSiacTMovgestTestataBySiacTMovgestId(uid);
		return siacTMovgestTs != null && !siacTMovgestTs.isEmpty() ? siacTMovgestTs.get(0) : null;
	}
	
	/**
	 *
	 * siacTOrdinativo e siacTLiquidazioneFin sono in mutua esclusione.
	 * 
	 * richiamo da ricerca ordinativo pagamento: viene passto siacTOrdinativo
	 * richiamo da ricerca liquidazione: viene passato siacTLiquidazioneFin
	 * 
	 * @param siacTOrdinativo
	 * @param siacTLiquidazioneFin
	 * @param datiOperazioneDto
	 * @return
	 */
	public OttimizzazioneOrdinativoPagamentoDto caricaOttimizzazioneRicercaOrdPagOppureLiquidazione(SiacTOrdinativoFin siacTOrdinativo,
			SiacTLiquidazioneFin siacTLiquidazioneFin,DatiOperazioneDto datiOperazioneDto,SoggettoFinDad soggettoDad){
		OttimizzazioneOrdinativoPagamentoDto ottimizzazioneDto = new OttimizzazioneOrdinativoPagamentoDto();
		
		//NON MODIFICARE L'ORDINE DELLE ISTRUZIONI IN QUESTO METODO, ALCUNE DIPENDONO DA ALTRE !!!
		
		
		List<SiacTOrdinativoTFin> listaSubOrdinativiCoinvolti = null;
		List<SiacTLiquidazioneFin> distintiSiacTLiquidazioneCoinvolti = null;
		if(siacTOrdinativo!=null){
			
			listaSubOrdinativiCoinvolti = siacTOrdinativo.getSiacTOrdinativoTs();
			
			ottimizzazioneDto.setListaSubOrdinativiCoinvolti(listaSubOrdinativiCoinvolti);
			
			//DISTINTI SiacRLiquidazioneOrdFin
			List<SiacRLiquidazioneOrdFin> distintiSiacRLiquidazioneOrdFinCoinvolti = ordinativoDao.ricercaBySiacTOrdinativoTFinMassive(listaSubOrdinativiCoinvolti, "SiacRLiquidazioneOrdFin");
			ottimizzazioneDto.setDistintiSiacRLiquidazioneOrdFinCoinvolti(distintiSiacRLiquidazioneOrdFinCoinvolti);
			
			// dagli SiacRLiquidazioneOrdFin ottengo gia' i SiacTLiquidazioneFin
			distintiSiacTLiquidazioneCoinvolti = ottimizzazioneDto.estraiSiacTLiquidazioneFinCoinvoltiBySiacRLiquidazioneOrdFinCoinvolti();
			
		} else if(siacTLiquidazioneFin!=null){
			//dal punto di vista della liquidazione:
			//mi riconduco, per la liquidaizone ricevuta in input, al caricamento delle n liquidazioni di un ordinativo
			distintiSiacTLiquidazioneCoinvolti = toList(siacTLiquidazioneFin);
		}
		
		//DISTINTI SiacRLiquidazioneOrdFin verso altri ordinativi (serve quando il ricerca liquidazione vorra sapere gli altri suoi ordinativi):
		List<SiacRLiquidazioneOrdFin> distintiSiacRLiquidazioneOrdFinCoinvoltiAncheVersoAltriOrdinativi = liquidazioneDao.ricercaByLiquidazioneMassive(distintiSiacTLiquidazioneCoinvolti, "SiacRLiquidazioneOrdFin");
		ottimizzazioneDto.setDistintiSiacRLiquidazioneOrdFinCoinvoltiAncheVersoAltriOrdinativi(distintiSiacRLiquidazioneOrdFinCoinvoltiAncheVersoAltriOrdinativi);
		
		if(siacTLiquidazioneFin!=null){
			//dal punto di vista della liquidazione coincidono:
			List<SiacRLiquidazioneOrdFin> distintiSiacRLiquidazioneOrdFinCoinvolti = distintiSiacRLiquidazioneOrdFinCoinvoltiAncheVersoAltriOrdinativi;
			ottimizzazioneDto.setDistintiSiacRLiquidazioneOrdFinCoinvolti(distintiSiacRLiquidazioneOrdFinCoinvolti);
		}

		//compongo la lista di tutti gli ordinativi coinvolti:
		List<SiacTOrdinativoFin> altriOrdinativi = ottimizzazioneDto.estraiSiacTOrdinativoFinCoinvoltiBySiacRLiquidazioneOrdFinCoinvoltiAncheVersoAltriOrdinativi();
		List<SiacTOrdinativoFin> allOrdinativi = CommonUtil.addAllConNewAndSoloDistintiByUid(altriOrdinativi,toList(siacTOrdinativo));
		
		//ottengo cosi gli stati di tutti gli ordinativi anche di quelli indirettamente collegati:
		List<SiacROrdinativoStatoFin> distintiSiacROrdinativoStatoFin = ordinativoDao.ricercaBySiacTOrdinativoFinMassive(allOrdinativi, "SiacROrdinativoStatoFin");
		ottimizzazioneDto.setDistintiSiacROrdinativoStatoFinCoinvolti(distintiSiacROrdinativoStatoFin);
		
		//compongo la lista di tutti gli ordinativi T coinvolti:
		List<SiacTOrdinativoTFin> altriOrdinativiT = ottimizzazioneDto.estraiSiacTOrdinativoTFinCoinvoltiBySiacRLiquidazioneOrdFinCoinvoltiAncheVersoAltriOrdinativi();
		List<SiacTOrdinativoTFin> allOrdinativiT = CommonUtil.addAllConNewAndSoloDistintiByUid(altriOrdinativiT,listaSubOrdinativiCoinvolti);
		
		//ottengo cosi gli importi di tutti i sub ordinativi anche di quelli indirettamente collegati:
		List<SiacTOrdinativoTsDetFin> distintiSiacTOrdinativoTsDetFin = ordinativoDao.ricercaBySiacTOrdinativoTFinMassive(allOrdinativiT, "SiacTOrdinativoTsDetFin");
		ottimizzazioneDto.setDistintiSiacTOrdinativoTsDetFinCoinvolti(distintiSiacTOrdinativoTsDetFin);
		
		
		//DISTINTI SiacRSubdocOrdinativoTFin
		List<SiacRSubdocOrdinativoTFin> distintiSiacRSubdocOrdinativoTFinCoinvolti = ordinativoDao.ricercaBySiacTOrdinativoTFinMassive(listaSubOrdinativiCoinvolti, "SiacRSubdocOrdinativoTFin");
		ottimizzazioneDto.setDistintiSiacRSubdocOrdinativoTFinCoinvolti(distintiSiacRSubdocOrdinativoTFinCoinvolti);
		
		//DISTINTI SiacRDocStatoFin
		List<SiacTDocFin> listaInput = ottimizzazioneDto.estraiSiacTDocFinCoinvoltiBySiacRSubdocOrdinativoTFinCoinvolti();
		List<SiacRDocStatoFin> distintiSiacRDocStatoFinCoinvolti = subdocumentoDaoCustom.ricercaBySiacTDocFinMassive(listaInput, "SiacRDocStatoFin");
		ottimizzazioneDto.setDistintiSiacRDocStatoFinCoinvolti(distintiSiacRDocStatoFinCoinvolti);
		
		
		//MODALITA PAGAMENTO DELLE LIQUDAZIONI:
		// dai SiacTLiquidazioneFin ottengo i SiacTModpagFin
		List<SiacTModpagFin> distintiSiacTModpagFinCoinvoltiVersoLiquidazioni = null;
		if(siacTOrdinativo!=null){
			distintiSiacTModpagFinCoinvoltiVersoLiquidazioni = ottimizzazioneDto.estraiSiacTModpagFinBySiacTLiquidazioneFinCoinvoltiDaOrdFinCoinvolti();
		} else if(siacTLiquidazioneFin!=null){
			distintiSiacTModpagFinCoinvoltiVersoLiquidazioni = toList(siacTLiquidazioneFin.getSiacTModpag());
		}
		
		OttimizzazioneModalitaPagamentoDto modalitaPagamentoLiquidazioni = popolaOttimizzazioneModPag(distintiSiacTModpagFinCoinvoltiVersoLiquidazioni);
		ottimizzazioneDto.setModalitaPagamentoLiquidazioniCoinvolte(modalitaPagamentoLiquidazioni);
		//
		
		if(siacTOrdinativo!=null){
			//MODALITA PAGAMENTO DELL' ORDINATIVO:
			List<SiacROrdinativoModpagFin> distintiSiacROrdinativoModpagFinCoinvolti = siacTOrdinativo.getSiacROrdinativoModpags();
			ottimizzazioneDto.setDistintiSiacROrdinativoModpagFinCoinvolti(distintiSiacROrdinativoModpagFinCoinvolti);
			List<SiacTModpagFin> distintiSiacTModpagFinCoinvoltiVersoOrdinativo = ottimizzazioneDto.estraiSiacTModpagFinBySiacROrdinativoModpagFinCoinvolti();
			OttimizzazioneModalitaPagamentoDto modalitaPagamentoOrdinativo = popolaOttimizzazioneModPag(distintiSiacTModpagFinCoinvoltiVersoOrdinativo);
			ottimizzazioneDto.setModalitaPagamentoOrdinativo(modalitaPagamentoOrdinativo);
			//
		}
		
		
		// Distinti SiacRLiquidazioneClassFin
		List<SiacRLiquidazioneSoggettoFin> distintiSiacRLiquidazioneSoggetto = liquidazioneDao.ricercaByLiquidazioneMassive(distintiSiacTLiquidazioneCoinvolti, "SiacRLiquidazioneSoggettoFin");
		ottimizzazioneDto.setDistintiSiacRLiquidazioneSoggetto(distintiSiacRLiquidazioneSoggetto);
		
		// Distinti SiacRLiquidazioneStatoFin
		List<SiacRLiquidazioneStatoFin> distintiSiacRLiquidazioneStatoFinCoinvolti = liquidazioneDao.ricercaByLiquidazioneMassive(distintiSiacTLiquidazioneCoinvolti, "SiacRLiquidazioneStatoFin");
		ottimizzazioneDto.setDistintiSiacRLiquidazioneStatoFinCoinvolti(distintiSiacRLiquidazioneStatoFinCoinvolti);
		
		// Distinti SiacRLiquidazioneClassFin
		List<SiacRLiquidazioneClassFin> distintiSiacRLiquidazioneClassFinCoinvolti = liquidazioneDao.ricercaByLiquidazioneMassive(distintiSiacTLiquidazioneCoinvolti, "SiacRLiquidazioneClassFin");
		ottimizzazioneDto.setDistintiSiacRLiquidazioneClassFinCoinvolti(distintiSiacRLiquidazioneClassFinCoinvolti);
		
		// Distinti SiacRLiquidazioneAttrFin
		List<SiacRLiquidazioneAttrFin> distintiSiacRLiquidazioneAttrFinCoinvolti = liquidazioneDao.ricercaByLiquidazioneMassive(distintiSiacTLiquidazioneCoinvolti, "SiacRLiquidazioneAttrFin");
		ottimizzazioneDto.setDistintiSiacRLiquidazioneAttrFinCoinvolti(distintiSiacRLiquidazioneAttrFinCoinvolti);
		
		// Distinti SiacRLiquidazioneAttoAmmFin
		List<SiacRLiquidazioneAttoAmmFin> distintiSiacRLiquidazioneAttoAmmFinCoinvolti = liquidazioneDao.ricercaByLiquidazioneMassive(distintiSiacTLiquidazioneCoinvolti, "SiacRLiquidazioneAttoAmmFin");
		ottimizzazioneDto.setDistintiSiacRLiquidazioneAttoAmmFinCoinvolti(distintiSiacRLiquidazioneAttoAmmFinCoinvolti);
		
		// Distinti SiacRSubdocLiquidazioneFin
		List<SiacRSubdocLiquidazioneFin> distintiSiacRSubdocLiquidazioneFinCoinvolti = liquidazioneDao.ricercaByLiquidazioneMassive(distintiSiacTLiquidazioneCoinvolti, "SiacRSubdocLiquidazioneFin");
		ottimizzazioneDto.setDistintiSiacRSubdocLiquidazioneFinCoinvolti(distintiSiacRSubdocLiquidazioneFinCoinvolti);
		
		//prendo anche il lato "bil" per poter richiamare il carica documento spesa ottimizzato:
		List<SiacRSubdocLiquidazione> distintiSiacRSubdocLiquidazioneCoinvolti = liquidazioneDao.ricercaByLiquidazioneBilMassive(distintiSiacTLiquidazioneCoinvolti, "SiacRSubdocLiquidazione");
		ottimizzazioneDto.setDistintiSiacRSubdocLiquidazioneCoinvolti(distintiSiacRSubdocLiquidazioneCoinvolti);
		//
		
			
		
		// Distinti SiacRLiquidazioneMovgestFin
		List<SiacRLiquidazioneMovgestFin> distintiSiacRLiquidazioneMovgestFin = liquidazioneDao.ricercaByLiquidazioneMassive(distintiSiacTLiquidazioneCoinvolti, "SiacRLiquidazioneMovgestFin");
		ottimizzazioneDto.setDistintiSiacRLiquidazioneMovgestFinCoinvolti(distintiSiacRLiquidazioneMovgestFin);
		
		//dagli SiacRLiquidazioneMovgestFin ottengo gli SiacTMovgestFin gia' caricati
		List<SiacTMovgestFin> distintiSiacTMovgestFinCoinvolti = ottimizzazioneDto.estraiSiacTMovgestFinBySiacRLiquidazioneMovgestFinCoinvolti();
		
		//sempre dagli SiacRLiquidazioneMovgestFin ottengo gli SiacTMovgestTsFin gia' caricati
		List<SiacTMovgestTsFin> distintiSiacTMovgestTsFinCoinvoltiDirettamente = ottimizzazioneDto.estraiDistintiSiacTMovgestTsFinBySiacRLiquidazioneMovgestFinCoinvolti();
		//in distintiSiacTMovgestTsFinCoinvoltiDirettamente abbiamo solo quelli direttamente collegati. Se una liquidazione e' collegata per 
		//esempio ad un sub di un impegno avremo tale sub (e sua testata) ma non gli altri sub dello stesso impegno,
		//mentre la lista distintiSiacTMovgestTsFin conterra'a anche gli altri
		//
		
		
		//OTTIMIZZAZIONI MOV GEST COLLEGATI: 
		OttimizzazioneMovGestDto ottimizzazioneMovGest = new OttimizzazioneMovGestDto();
		ottimizzazioneMovGest = caricaDatiOttimizzazioneMovGest(ottimizzazioneMovGest, distintiSiacTMovgestFinCoinvolti, CostantiFin.MOVGEST_TIPO_IMPEGNO, datiOperazioneDto);
		ottimizzazioneDto.setOttimizzazioneMovimentiCoinvolti(ottimizzazioneMovGest);
		List<SiacTMovgestTsFin> distintiSiacTMovgestTsFin = ottimizzazioneMovGest.getDistintiSiacTMovgestTsFinCoinvolti();
		
		//MODIFICHE DEI MOVIMENTI COINVOLTI:
		List<SiacTSoggettoFin> soggettiModificheDeiMovimenti = ottimizzazioneMovGest.getOttimizzazioneModDto().getDistintiSiacTSoggettiCoinvolti(); 
		//
		
		//DISPONIBILI A LIQUIDARE DEI SOLI MOVIMENTI COINVOLTI non avrebbe senso caricarli per tutti dato che poi si invochera' il ricerca impegno per chiave
		//solo verso l'eventuale singolo sub (e sua testata) coinvolto verso una liquidazione:
		List<SiacTMovgestTsFin> testateDeiSubDirettamenteCoinvolti = listaTestateDeiSubCoinvolti(distintiSiacTMovgestTsFinCoinvoltiDirettamente, distintiSiacTMovgestTsFin);
		List<SiacTMovgestTsFin> distintiSiacTMovgestTsFinCoinvoltiDirettamenteERelativeTestate = CommonUtil.addAllConNewAndSoloDistintiByUid(distintiSiacTMovgestTsFinCoinvoltiDirettamente,testateDeiSubDirettamenteCoinvolti);
				
				
		List<CodificaImportoDto> listaDisponibiliLiquidareMovimentiCoinvoltiDirettamente = impegnoDao.calcolaDisponibilitaALiquidareMassive(distintiSiacTMovgestTsFinCoinvoltiDirettamenteERelativeTestate);
		ottimizzazioneMovGest.setListaDisponibiliLiquidareDaFunction(listaDisponibiliLiquidareMovimentiCoinvoltiDirettamente);
		
		//Dati per risalire ai vincoli (come per il disp a liquidare SOLO per i direttamente coinvolti)
		//SIAC-7596, aggiunto boolean escludiMovgestAAnnullati, qui messo a false per mantenere comportamento precedente
		List<SiacRMovgestTsFin> distintiSiacRMovgestTsFinCoinvolti =movimentoGestioneDao.ricercaBySiacTMovgestTsFinMassive(distintiSiacTMovgestTsFinCoinvoltiDirettamenteERelativeTestate, Boolean.TRUE, Boolean.FALSE);
		ottimizzazioneMovGest.setDistintiSiacRMovgestTsFinCoinvolti(distintiSiacRMovgestTsFinCoinvolti);
		//i bil elem li prendo tutti tanto sono pochi dati:
		List<SiacTMovgestFin> listaAllSiacTMovgestFin = CommonUtil.addAllConNewAndSoloDistintiByUid(ottimizzazioneMovGest.getDistintiSiacTMovgestFinCoinvolti(),ottimizzazioneMovGest.estraiSiacTMovgestFinBySiacRMovgestTsFinCoinvolti());
		List<SiacRMovgestBilElemFin> distintiSiacRMovgestBilElemCoinvolti =  movimentoGestioneDao.ricercaSiacRMovgestBilElemMassive(listaAllSiacTMovgestFin);
		ottimizzazioneMovGest.setDistintiSiacRMovgestBilElemCoinvolti(distintiSiacRMovgestBilElemCoinvolti);
		
		List<SiacTBilElemFin> distintiSiacTBilElemFinCoinvolti = ottimizzazioneMovGest.estraiSiacTBilElemFinBySiacRMovgestBilElemFinCoinvolti();
		
		List<SiacRVincoloBilElemFin> distintSiacRVincoloBilElemFinCoinvolti = movimentoGestioneDao.ricercaSiacRVincoloBilElemFinMassive(distintiSiacTBilElemFinCoinvolti);
		ottimizzazioneMovGest.setDistintSiacRVincoloBilElemFinCoinvolti(distintSiacRVincoloBilElemFinCoinvolti);
		
		List<SiacTVincoloFin> distintiSiacTVincoloFinCoinvolti = ottimizzazioneMovGest.estraiSiacTVincoloFinBySiacRVincoloBilElemFinCoinvolti();
		
		List<SiacRVincoloAttrFin> distintiSiacRVincoloAttrFinCoinvolti = movimentoGestioneDao.ricercaSiacRVincoloAttrFinMassive(distintiSiacTVincoloFinCoinvolti);
		ottimizzazioneMovGest.setDistintiSiacRVincoloAttrFinCoinvolti(distintiSiacRVincoloAttrFinCoinvolti);
		
		//
		
		List<SiacTSoggettoFin> soggettoOrdinativo = null;
		if(siacTOrdinativo!=null){
			//SOGGETTO DELL'ORDINATIVO:
			List<SiacROrdinativoSoggettoFin> distintiSiacROrdinativoSoggettoFinCoinvolti = siacTOrdinativo.getSiacROrdinativoSoggettos();
			ottimizzazioneDto.setDistintiSiacROrdinativoSoggettoFinCoinvolti(distintiSiacROrdinativoSoggettoFinCoinvolti);
			soggettoOrdinativo =ottimizzazioneDto.estraiDistintiSiacTSoggettoFinBySiacROrdinativoSoggettoFinCoinvolti();
			//
		}
		
		
		//Soggetti coinvolti tramite impegni e sub:
		OttimizzazioneSoggettoDto ottimizzazioneSoggettoDto = new OttimizzazioneSoggettoDto();
		//DISTINTI SiacTSoggettoFin
		List<SiacTSoggettoFin> soggettiImpegni = soggettoDao.ricercaBySiacTMovgestPkMassive(distintiSiacTMovgestTsFin);
		//
		
		//OttimizzazioneModificheMovimentoGestioneDto ottimizzazioneModificheImpegni = caricaOttimizzazioneModificheMovimentoGestioneDto(distintiSiacTMovgestTsFin);
		//TODO QUI EVENTUALMENTE ANDARE A PRENDERE TUTTE LE RELAZIONI VERSO SOGGETTI TRAMITE ALTRE ENETITA E AGGIUNGERLI A allSoggettiCoinvolti
		
		List<SiacTSoggettoFin> soggettiLiquidazioni = ottimizzazioneDto.estraiSiacTSoggettoFinCoinvoltiBySiacRLiquidazioneSoggettoFinCoinvolti();
		List<SiacTSoggettoFin> allSoggettiCoinvolti = CommonUtil.addAllConNewAndSoloDistintiByUid(soggettoOrdinativo,soggettiImpegni,soggettiLiquidazioni,soggettiModificheDeiMovimenti);
		
		
		//METODO CORE DEI SOGGETTI:
		ottimizzazioneSoggettoDto = caricaDatiOttimizzazioneRicercaSoggettoByDistintiSoggetti(allSoggettiCoinvolti);
		//allSoggettiCoinvolti CAMBIA e si arricchisce con i soggetti (eventualmente) associati tramite relazione:
		allSoggettiCoinvolti = ottimizzazioneSoggettoDto.getDistintiSiacTSoggettiCoinvolti();
		//
		
		boolean caricaSediSecondarie=true;//OTTOBRE 2016 aggiunto il caricamento ottimizzato anche delle sedi secondarie
		boolean isDecentrato = false;
		//NOTARE QUESTO FLAG:
		boolean includeModifica = false;//se includeModifica fosse a true estrarrebbe il soggetto con stato portato artificiosamente in
		// modifica da una vecchia jira siac-138, invece settando includeModifica a false trova lo stato reale
		//
		boolean caricaDatiUlteriori = true;
		
		List<Soggetto> distintiSoggettiCoinvolti = soggettoDad.ricercaSoggettoOPT(allSoggettiCoinvolti, includeModifica, caricaDatiUlteriori,ottimizzazioneSoggettoDto,
				datiOperazioneDto,caricaSediSecondarie,isDecentrato);
		
		//SIAC R SOGGETTI MOV GEST COINVOLI:
		List<SiacRMovgestTsSogFin> distintiSiacRSoggettiCoinvolti = movimentoGestioneDao.ricercaByMovGestTsMassive(distintiSiacTMovgestTsFin, "SiacRMovgestTsSogFin");
		ottimizzazioneSoggettoDto.setDistintiSiacRSoggettiCoinvolti(distintiSiacRSoggettiCoinvolti);
		ottimizzazioneMovGest.setDistintiSiacRSoggettiCoinvolti(distintiSiacRSoggettiCoinvolti);
		
		//SIAC R CLASSI MOV GEST COINVOLTI 
		List<SiacRMovgestTsSogclasseFin> distintiSiacRMovgestTsSogclasseFinCoinvolti = movimentoGestioneDao.ricercaByMovGestTsMassive(distintiSiacTMovgestTsFin, "SiacRMovgestTsSogclasseFin");
		ottimizzazioneSoggettoDto.setDistintiSiacRMovgestTsSogclasseFinCoinvolti(distintiSiacRMovgestTsSogclasseFinCoinvolti);
		ottimizzazioneMovGest.setDistintiSiacRMovgestTsSogclasseCoinvolti(distintiSiacRMovgestTsSogclasseFinCoinvolti);
		
		//QUESTO E' ECCESSIVO...
		//List<SiacRMovgestTsSogFin> distintiSiacRSoggettiCoinvoltiTEST =soggettoDao.ricercaBySoggettoMassive(allSoggettiCoinvolti, "SiacRMovgestTsSogFin");
		
		//SETTING SOGGETTI:
		ottimizzazioneSoggettoDto.setSoggettiGiaCaricati(distintiSoggettiCoinvolti);
		ottimizzazioneSoggettoDto.setDistintiSiacTSoggettiCoinvolti(allSoggettiCoinvolti);
		ottimizzazioneDto.setOttimizzazioneSoggettoDto(ottimizzazioneSoggettoDto);
		ottimizzazioneMovGest.setOttimizzazioneSoggetti(ottimizzazioneSoggettoDto);
		//
		
		
		//Termino restituendo l'oggetto di ritorno: 
       return ottimizzazioneDto;
       
	}
	
	protected List<SiacTOrdinativoFin> getDistintiOrdinativi(List<SiacTOrdinativoTFin> elencoSiacTOrdinativoT){
		List<SiacTOrdinativoFin> ordinativi = new ArrayList<SiacTOrdinativoFin>();
		for(SiacTOrdinativoTFin subIt : elencoSiacTOrdinativoT){
			ordinativi = CommonUtil.addAllConNewAndSoloDistintiByUid(ordinativi,toList(subIt.getSiacTOrdinativo()));
		}
		return ordinativi;
	}
	
	/**
	 * 
	 * Carica solo i dati necessari per vestire l'elenco di sub ordinativi / ordinativi 
	 * dei servizi 
	 * RicercaSubOrdinativoIncassoService
	 * RicercaSubOrdinativoPagamentoService
	 * 
	 * A maggio 2018 carica solo:
	 *  -importi dei sub ordinativi
	 *  -modalita pagamento
	 *  -soggetti
	 *  dei vari ordinativi indicati
	 *  
	 *  Se dovessero servire altri dati aggiungerli (solo il minimo indispensabile)
	 *  aggiornando questa documentazione.
	 * 
	 * @param allSubOrdinativi
	 * @param datiOperazioneDto
	 * @param soggettoDad
	 * @return
	 */
	public OttimizzazioneOrdinativoPagamentoDto caricaOttimizzazionePerRicercaSubOrdinativiByOrdinativi(List<SiacTOrdinativoTFin> allSubOrdinativi,
			DatiOperazioneDto datiOperazioneDto,SoggettoFinDad soggettoDad){
		OttimizzazioneOrdinativoPagamentoDto ottimizzazioneDto = new OttimizzazioneOrdinativoPagamentoDto();
		
		List<SiacTOrdinativoFin> allOrdinativi = getDistintiOrdinativi(allSubOrdinativi);
		
		// importi di tutti i sub ordinativi anche di quelli indirettamente collegati:
		List<SiacTOrdinativoTsDetFin> distintiSiacTOrdinativoTsDetFin = ordinativoDao.ricercaBySiacTOrdinativoTFinMassive(allSubOrdinativi, "SiacTOrdinativoTsDetFin");
		ottimizzazioneDto.setDistintiSiacTOrdinativoTsDetFinCoinvolti(distintiSiacTOrdinativoTsDetFin);
		
		//
		
		List<SiacROrdinativoModpagFin> distintiSiacROrdinativoModpagFinCoinvolti = null;
		for(SiacTOrdinativoFin siacTOrdinativo: allOrdinativi){
			distintiSiacROrdinativoModpagFinCoinvolti = CommonUtil.addAllConNewAndSoloDistintiByUid(distintiSiacROrdinativoModpagFinCoinvolti,siacTOrdinativo.getSiacROrdinativoModpags());
		}
		
		//MODALITA PAGAMENTO DEGLI ORDINATIVI:
		ottimizzazioneDto.setDistintiSiacROrdinativoModpagFinCoinvolti(distintiSiacROrdinativoModpagFinCoinvolti);
		List<SiacTModpagFin> distintiSiacTModpagFinCoinvoltiVersoOrdinativo = ottimizzazioneDto.estraiSiacTModpagFinBySiacROrdinativoModpagFinCoinvolti();
		OttimizzazioneModalitaPagamentoDto modalitaPagamentoOrdinativo = popolaOttimizzazioneModPag(distintiSiacTModpagFinCoinvoltiVersoOrdinativo);
		ottimizzazioneDto.setModalitaPagamentoOrdinativo(modalitaPagamentoOrdinativo);
		//
		
		//
		
		List<SiacROrdinativoSoggettoFin> distintiSiacROrdinativoSoggettoFinCoinvolti = null;
		for(SiacTOrdinativoFin siacTOrdinativo: allOrdinativi){
			distintiSiacROrdinativoSoggettoFinCoinvolti = CommonUtil.addAllConNewAndSoloDistintiByUid(distintiSiacROrdinativoSoggettoFinCoinvolti,siacTOrdinativo.getSiacROrdinativoSoggettos());
		}
		
		List<SiacTSoggettoFin> soggettoOrdinativo = null;
		//SOGGETTI DEGLI ORDINATIVI:
		ottimizzazioneDto.setDistintiSiacROrdinativoSoggettoFinCoinvolti(distintiSiacROrdinativoSoggettoFinCoinvolti);
		soggettoOrdinativo =ottimizzazioneDto.estraiDistintiSiacTSoggettoFinBySiacROrdinativoSoggettoFinCoinvolti();
		//
	
		
		//Soggetti coinvolti tramite impegni e sub:
		OttimizzazioneSoggettoDto ottimizzazioneSoggettoDto = new OttimizzazioneSoggettoDto();
		List<SiacTSoggettoFin> allSoggettiCoinvolti = CommonUtil.addAllConNewAndSoloDistintiByUid(soggettoOrdinativo);
		
		//METODO CORE DEI SOGGETTI:
		ottimizzazioneSoggettoDto = caricaDatiOttimizzazioneRicercaSoggettoByDistintiSoggetti(allSoggettiCoinvolti);
		//allSoggettiCoinvolti CAMBIA e si arricchisce con i soggetti (eventualmente) associati tramite relazione:
		allSoggettiCoinvolti = ottimizzazioneSoggettoDto.getDistintiSiacTSoggettiCoinvolti();
		//
		
		boolean caricaSediSecondarie=true;//OTTOBRE 2016 aggiunto il caricamento ottimizzato anche delle sedi secondarie
		boolean isDecentrato = false;
		//NOTARE QUESTO FLAG:
		boolean includeModifica = false;//se includeModifica fosse a true estrarrebbe il soggetto con stato portato artificiosamente in
		// modifica da una vecchia jira siac-138, invece settando includeModifica a false trova lo stato reale
		//
		boolean caricaDatiUlteriori = true;
		
		List<Soggetto> distintiSoggettiCoinvolti = soggettoDad.ricercaSoggettoOPT(allSoggettiCoinvolti, includeModifica, caricaDatiUlteriori,ottimizzazioneSoggettoDto,
				datiOperazioneDto,caricaSediSecondarie,isDecentrato);
		
		//SETTING SOGGETTI:
		ottimizzazioneSoggettoDto.setSoggettiGiaCaricati(distintiSoggettiCoinvolti);
		ottimizzazioneSoggettoDto.setDistintiSiacTSoggettiCoinvolti(allSoggettiCoinvolti);
		ottimizzazioneDto.setOttimizzazioneSoggettoDto(ottimizzazioneSoggettoDto);
		
		
		//Termino restituendo l'oggetto di ritorno: 
      return ottimizzazioneDto;
      
	}
	
	
	private List<SiacTMovgestTsFin> listaTestateDeiSubCoinvolti(List<SiacTMovgestTsFin> distintiSiacTMovgestTsFinCoinvoltiDirettamente, List<SiacTMovgestTsFin>  distintiSiacTMovgestTsFin){
		List<SiacTMovgestTsFin> testateDeiSubDirettamenteCoinvolti = new ArrayList<SiacTMovgestTsFin>();
		if(!StringUtilsFin.isEmpty(distintiSiacTMovgestTsFinCoinvoltiDirettamente) && !StringUtilsFin.isEmpty(distintiSiacTMovgestTsFin)){
			for(SiacTMovgestTsFin itCoinvoltoDirettamente : distintiSiacTMovgestTsFinCoinvoltiDirettamente){
				if(itCoinvoltoDirettamente!=null && itCoinvoltoDirettamente.getMovgestTsIdPadre()!=null){
					//e' un sub
					for(SiacTMovgestTsFin it : distintiSiacTMovgestTsFin){
						if(it!=null && it.getMovgestTsIdPadre()==null && it.getMovgestTsId().intValue()==itCoinvoltoDirettamente.getMovgestTsIdPadre().intValue()){
							testateDeiSubDirettamenteCoinvolti.add(it);
						}
					}
				}
			}
		}
		return testateDeiSubDirettamenteCoinvolti;
	}
	
	private OttimizzazioneModalitaPagamentoDto popolaOttimizzazioneModPag(List<SiacTModpagFin> modalitaPagamentoCoinvolte){
		OttimizzazioneModalitaPagamentoDto modPagOptDto = new OttimizzazioneModalitaPagamentoDto();
		List<SiacTModpagModFin> distintiSiacTModpagModFinCoinvolti =  soggettoDao.ricercaSiacTModpagModFinMassive(modalitaPagamentoCoinvolte);
		modPagOptDto.setDistintiSiacTSiacTModpagFinCoinvolti(modalitaPagamentoCoinvolte);
		modPagOptDto.setDistintiSiacTModpagModFinCoinvolti(distintiSiacTModpagModFinCoinvolti);
		return modPagOptDto;
	}
	
	/**
	 * deprecato - troppo lento per tanti dati
	 * 
	 * @param distintiSiacTAvanzovincoloFinCoinvolti
	 * @return
	 */
	@Deprecated
	public OttimizzazioneAvanzoVincoliDto caricaDatiOttimizzazioneAvanzoVincoli(List<SiacTAvanzovincoloFin> distintiSiacTAvanzovincoloFinCoinvolti){
		OttimizzazioneAvanzoVincoliDto datiOttimizzazione = new OttimizzazioneAvanzoVincoliDto();
		
		List<SiacRMovgestTsFin> distintiSiacRMovgestTsFinCoinvoltiByAvanzi = movimentoGestioneDao.ricercaSiacRMovgestTsFinBySiacTAvanzovincoloFinMassive(distintiSiacTAvanzovincoloFinCoinvolti, true);
		datiOttimizzazione.setDistintiSiacRMovgestTsFinCoinvolti(distintiSiacRMovgestTsFinCoinvoltiByAvanzi);
		
		
		List<SiacTMovgestTsFin> distintiSiacTMovgestTsFinCoinvolti = datiOttimizzazione.estraiDistintiSiacTMovgestTsFinBySiacRMovgestTsFinCoinvolti();
		datiOttimizzazione.setDistintiSiacTMovgestTsFinCoinvolti(distintiSiacTMovgestTsFinCoinvolti);
		
		//IMPORTI DEI MOVIMENTI COLLEGATI:
		List<SiacTMovgestTsDetFin> distintiSiacTMovgestTsDetCoinvolti = movimentoGestioneDao.ricercaByMovGestTsMassive(distintiSiacTMovgestTsFinCoinvolti,"SiacTMovgestTsDetFin");
		datiOttimizzazione.setDistintiSiacTMovgestTsDetCoinvolti(distintiSiacTMovgestTsDetCoinvolti);
		//
		
		//STATI DEI MOVIMENTI COLLEGATI:
		List<SiacRMovgestTsStatoFin> distintiSiacRMovgestTsStatoCoinvolti = movimentoGestioneDao.ricercaByMovGestTsMassive(distintiSiacTMovgestTsFinCoinvolti,"SiacRMovgestTsStatoFin");
		datiOttimizzazione.setDistintiSiacRMovgestTsStatoCoinvolti(distintiSiacRMovgestTsStatoCoinvolti);
		//
		
		return datiOttimizzazione;
	}
	
	/**
	 * 
	 * Non caricare i soggetti vanno caricati dal chiamante a seconda di contesti piu' ampi..esempio
	 * se il chiamante e' il ricerca ord pag deve caricare i soggetti in un colpo solo assieme ai suoi soggetti diretti e anche delle liquidazioni ecc
	 * 
	 * 
	 * @param ottimizzazioneDto
	 * @param distintiSiacTMovgestFinCoinvolti
	 * @param tipoMovimento
	 * @param datiOperazione
	 * @return
	 */
	public OttimizzazioneMovGestDto caricaDatiOttimizzazioneMovGest(OttimizzazioneMovGestDto ottimizzazioneDto,
			List<SiacTMovgestFin> distintiSiacTMovgestFinCoinvolti, String tipoMovimento, DatiOperazioneDto datiOperazione){
		
		if(ottimizzazioneDto==null){
			ottimizzazioneDto = new OttimizzazioneMovGestDto();
		}
		
		ottimizzazioneDto.setDistintiSiacTMovgestFinCoinvolti(distintiSiacTMovgestFinCoinvolti);
		
		// Distinti SiacTMovgestTsFin
		List<Integer> listaMovgestIds = CommonUtil.getIdListSiacTBase(distintiSiacTMovgestFinCoinvolti);
		List<SiacTMovgestTsFin> distintiSiacTMovgestTsFin = movimentoGestioneDao.ricercaSiacTMovgestTsFinBySiacTMovgestMassive(listaMovgestIds, true);
		
		ottimizzazioneDto = caricaDatiOttimizzazioneMovGestTs(ottimizzazioneDto, distintiSiacTMovgestTsFin, tipoMovimento,datiOperazione);
		
		
		return ottimizzazioneDto;
	}
	
	/**
	 *  Non caricare i soggetti vanno caricati dal chiamante a seconda di contesti piu' ampi..esempio
	 *  se il chiamante e' il ricerca ord pag deve caricare i soggetti in un colpo solo assieme ai suoi soggetti diretti e anche delle liquidazioni ecc
	 * 
	 * @param ottimizzazioneDto
	 * @param distintiSiacTMovgestTsFin
	 * @param tipoMovimento
	 * @param datiOperazione
	 * @return
	 */
	public OttimizzazioneMovGestDto caricaDatiOttimizzazioneMovGestTs(OttimizzazioneMovGestDto ottimizzazioneDto,
			List<SiacTMovgestTsFin> distintiSiacTMovgestTsFin, String tipoMovimento, DatiOperazioneDto datiOperazione){
		
		if(ottimizzazioneDto==null){
			ottimizzazioneDto = new OttimizzazioneMovGestDto();
		}
		
		ottimizzazioneDto.setDistintiSiacTMovgestTsFinCoinvolti(distintiSiacTMovgestTsFin);
		
		//Pre carichiamo gli importi in maniera ottimizzata:
		
		Integer idEnte = datiOperazione.getSiacTEnteProprietario().getEnteProprietarioId();
		
		//STATI:
		List<SiacRMovgestTsStatoFin> distintiSiacRMovgestTsStatoCoinvolti = movimentoGestioneDao.ricercaByMovGestTsMassive(distintiSiacTMovgestTsFin,"SiacRMovgestTsStatoFin");
		ottimizzazioneDto.setDistintiSiacRMovgestTsStatoCoinvolti(distintiSiacRMovgestTsStatoCoinvolti);
		//
		
		
		//IMPORTI DEI MOVIMENTI COLLEGATI:
		List<SiacTMovgestTsDetFin> distintiSiacTMovgestTsDetCoinvolti = movimentoGestioneDao.ricercaByMovGestTsMassive(distintiSiacTMovgestTsFin,"SiacTMovgestTsDetFin");
		ottimizzazioneDto.setDistintiSiacTMovgestTsDetCoinvolti(distintiSiacTMovgestTsDetCoinvolti);
		//
		
		//In caso di impegno ci serve caricare i legami verso le liquidazioni:
		if(tipoMovimento.equalsIgnoreCase(CostantiFin.MOVGEST_TIPO_IMPEGNO)){
			ottimizzazioneDto = caricaDatiOttimizzazioneVersoLiquidazioniEOrdinativiByImpegnoTsDto(distintiSiacTMovgestTsFin, ottimizzazioneDto);
		}else if (tipoMovimento.equalsIgnoreCase(CostantiFin.MOVGEST_TIPO_ACCERTAMENTO)){
			ottimizzazioneDto = caricaDatiOttimizzazioneVersoOrdinativiByAccertamentoTsDto(distintiSiacTMovgestTsFin, ottimizzazioneDto);
		}
		
	
		
		
		//T CLASS:
		List<SiacRMovgestClassFin> distintiSiacRMovgestClassCoinvolti =  movimentoGestioneDao.ricercaByMovGestTsMassive(distintiSiacTMovgestTsFin,"SiacRMovgestClassFin");
		ottimizzazioneDto.setDistintiSiacRMovgestClassCoinvolti(distintiSiacRMovgestClassCoinvolti);
		
		//T ATTR:
		List<SiacRMovgestTsAttrFin> distintiSiacRMovgestTsAttrCoinvolti =  movimentoGestioneDao.ricercaByMovGestTsMassive(distintiSiacTMovgestTsFin,"SiacRMovgestTsAttrFin");
		ottimizzazioneDto.setDistintiSiacRMovgestTsAttrCoinvolti(distintiSiacRMovgestTsAttrCoinvolti);
		
		
		
		//ATTI AMMINISTRATIVI:
		List<SiacRMovgestTsAttoAmmFin> distintiSiacRMovgestTsAttoAmmCoinvolti =movimentoGestioneDao.ricercaByMovGestTsMassive(distintiSiacTMovgestTsFin, "SiacRMovgestTsAttoAmmFin");
		ottimizzazioneDto.setDistintiSiacRMovgestTsAttoAmmCoinvolti(distintiSiacRMovgestTsAttoAmmCoinvolti);
		
		//PROGRAMMA:
		List<SiacRMovgestTsProgrammaFin> distintiSiacRMovgestTsProgrammaCoinvolti =movimentoGestioneDao.ricercaByMovGestTsMassive(distintiSiacTMovgestTsFin, "SiacRMovgestTsProgrammaFin");
		ottimizzazioneDto.setDistintiSiacRMovgestTsProgrammaCoinvolti(distintiSiacRMovgestTsProgrammaCoinvolti);
		
		List<SiacRProgrammaAttrFin> distintiSiacRProgrammaAttrFinCoinvolti =movimentoGestioneDao.ricercaSiacRProgrammaAttrFinBySiacRMovgestTsProgrammaFinMassive(distintiSiacRMovgestTsProgrammaCoinvolti, true);
		ottimizzazioneDto.setDistintiSiacRProgrammaAttrFinCoinvolti(distintiSiacRProgrammaAttrFinCoinvolti);
		
		
		//MODIFICHE DEI MOVIMENTI COINVOLTI:
		OttimizzazioneModificheMovimentoGestioneDto optModMov = caricaOttimizzazioneModificheMovimentoGestioneDto(distintiSiacTMovgestTsFin);
		ottimizzazioneDto.setOttimizzazioneModDto(optModMov);
		//
		
		return ottimizzazioneDto;
		
	}
	
	/**
	 * Ricerco eventuali legami tra movimento (accertamento) e ordinativi
	 * @param siacTMovgest
	 * @param ottimizzazioneDto
	 * @return
	 */
	public OttimizzazioneMovGestDto caricaDatiOttimizzazioneVersoOrdinativiByAccertamentoDto(SiacTMovgestFin siacTMovgest,OttimizzazioneMovGestDto ottimizzazioneDto){
			return caricaDatiOttimizzazioneVersoOrdinativiByAccertamentoTsDto(siacTMovgest.getSiacTMovgestTs(), ottimizzazioneDto);
	}

	
	public OttimizzazioneMovGestDto caricaDatiOttimizzazioneVersoLiquidazioniEOrdinativiByImpegnoDto(SiacTMovgestFin siacTMovgest,OttimizzazioneMovGestDto ottimizzazioneDto){
		return caricaDatiOttimizzazioneVersoLiquidazioniEOrdinativiByImpegnoTsDto(siacTMovgest.getSiacTMovgestTs(), ottimizzazioneDto);
	}
	
	public OttimizzazioneMovGestDto caricaDatiOttimizzazioneVersoLiquidazioniEOrdinativiByImpegnoTsDto(List<SiacTMovgestTsFin> distintiSiacTMovgestTsFin,OttimizzazioneMovGestDto ottimizzazioneDto){
		
		List<CodificaImportoDto> dispAPagareDaFunction = impegnoDao.calcolaDisponibileAPagareMassive(distintiSiacTMovgestTsFin);
		ottimizzazioneDto.setListaDisponibiliPagareDaFunction(dispAPagareDaFunction);
		
		/* APRILE 2017 COMMENTO TUTTO: SERVIA SOLO PER IL CALCOLO DEL DISP A PAGARE, CHE ADESSO SVOLGO DA FUNCTION
		
		List<SiacRLiquidazioneMovgestFin> distintiSiacRLiquidazioneMovgestFinCoinvolti = movimentoGestioneDao.ricercaByMovGestTsMassive(distintiSiacTMovgestTsFin,"SiacRLiquidazioneMovgestFin");
		ottimizzazioneDto.setDistintiSiacRLiquidazioneMovgestFinCoinvolti(distintiSiacRLiquidazioneMovgestFinCoinvolti);
		
		List<SiacTLiquidazioneFin> distintiSiacTLiquidazioneFinCoinvolti = liquidazioneDao.ricercaDistiniteLiquidazioniByRLiquidazioneMovgest(distintiSiacRLiquidazioneMovgestFinCoinvolti);
		ottimizzazioneDto.setDistintiSiacTLiquidazioneFinCoinvolti(distintiSiacTLiquidazioneFinCoinvolti);
		
		//Al momento sembra non servire, scommentare all'evenienza
		//List<SiacRLiquidazioneStatoFin> distintiSiacRLiquidazioneStatoFinCoinvolti = liquidazioneDao.ricercaByLiquidazioneMassive(distintiSiacTLiquidazioneFinCoinvolti, "SiacRLiquidazioneStatoFin");
		//ottimizzazioneDto.setDistintiSiacRLiquidazioneStatoFinCoinvolti(distintiSiacRLiquidazioneStatoFinCoinvolti);
		
		List<SiacRLiquidazioneOrdFin> distintiSiacRLiquidazioneOrdFinCoinvolti = liquidazioneDao.ricercaByLiquidazioneMassive(distintiSiacTLiquidazioneFinCoinvolti, "SiacRLiquidazioneOrdFin");
		ottimizzazioneDto.setDistintiSiacRLiquidazioneOrdFinCoinvolti(distintiSiacRLiquidazioneOrdFinCoinvolti);
		
		
		List<SiacTOrdinativoTFin> distintiSiacTOrdinativoTFinCoinvolti = ordinativoDao.ricercaDistinitiSiacTOrdinativoTFinByRLiquidazioneOrd(distintiSiacRLiquidazioneOrdFinCoinvolti);
		ottimizzazioneDto.setDistintiSiacTOrdinativoTFinCoinvolti(distintiSiacTOrdinativoTFinCoinvolti);
		
		
		List<SiacTOrdinativoFin> distintiSiacTOrdinativoFinCoinvolti = ordinativoDao.ricercaDistinitiOrdinativoByOrdinativoTestata(distintiSiacTOrdinativoTFinCoinvolti);
		ottimizzazioneDto.setDistintiSiacTOrdinativoFinCoinvolti(distintiSiacTOrdinativoFinCoinvolti);
		
		List<SiacTOrdinativoTsDetFin> distintiSiacTOrdinativoTsDetFinCoinvolti = ordinativoDao.ricercaDistinitiSiacTOrdinativoTsDetFinByOrdinativoTestata(distintiSiacTOrdinativoTFinCoinvolti);
		ottimizzazioneDto.setDistintiSiacTOrdinativoTsDetFinCoinvolti(distintiSiacTOrdinativoTsDetFinCoinvolti); 
		
		List<SiacROrdinativoStatoFin> distintiSiacROrdinativoStatoFinCoinvolti = ordinativoDao.ricercaDistinitiSiacROrdinativoStatoFinByOrdinativo(distintiSiacTOrdinativoFinCoinvolti);
		ottimizzazioneDto.setDistintiSiacROrdinativoStatoFinCoinvolti(distintiSiacROrdinativoStatoFinCoinvolti);
		
		*/
		
		return ottimizzazioneDto;
	} 
	
	/**
	 * Ricerco eventuali legami tra movimento (accertamento) e oridinativi
	 * @param siacTMovgest
	 * @param ottimizzazioneDto
	 * @return
	 */
	public OttimizzazioneMovGestDto caricaDatiOttimizzazioneVersoOrdinativiByAccertamentoTsDto(List<SiacTMovgestTsFin> distintiSiacTMovgestTsFin,OttimizzazioneMovGestDto ottimizzazioneDto){
			
			List<SiacROrdinativoTsMovgestTFin> siacRordinativiTsMovgest = movimentoGestioneDao.ricercaByMovGestTsMassive(distintiSiacTMovgestTsFin,"SiacROrdinativoTsMovgestTFin");
			ottimizzazioneDto.setDistintiSiacROrdinativoTsMovgestTCoinvolti(siacRordinativiTsMovgest);
			return ottimizzazioneDto;
	}
	
	
	
	/**
	 * Metodo che aggrega i dati in maniera ottimizzata
	 * @param distintiSiacTSoggettiCoinvolti
	 * @return
	 */
	public OttimizzazioneSoggettoDto caricaDatiOttimizzazioneRicercaSoggettoByDistintiSoggetti(List<SiacTSoggettoFin> soggettiInput){
		OttimizzazioneSoggettoDto ottimizzazioneSoggettoDto = new OttimizzazioneSoggettoDto();
		
		//SOGGETTI INPUT:
		List<SiacTSoggettoFin> distintiSiacTSoggettiCoinvoltiDirettamente = CommonUtil.ritornaSoloDistintiByUid(soggettiInput);
		ottimizzazioneSoggettoDto.setDistintiSiacTSoggettiCoinvoltiDirettamente(distintiSiacTSoggettiCoinvoltiDirettamente );
		
		//DISTINTI SiacRSoggettoRelazFin
		List<SiacRSoggettoRelazFin> distintiSiacRSoggettoRelaz = soggettoDao.ricercaSiacRSoggettoRelazMassive(soggettiInput);
		
		//Prendiamo i soggetti da relazioni:
		List<SiacTSoggettoFin> soggettiDaRelazioni = soggettoDao.ricercaSiacTSoggettoFinBySiacRSoggettoRelazFin(distintiSiacRSoggettoRelaz);
		ottimizzazioneSoggettoDto.setDistintiSiacTSoggettiCoinvoltiTramiteRelazioni(soggettiDaRelazioni);
		//
		
		//FACCIO UNA LISTA SIA CON I SOGGETTI RICHIESTI DIRETTAMENTE CHE CON QUELLI RELAZIONATI (i chiamanti spesso compilano i
		// dati delle sedi secondarie e delle loro modalita' di pagamento) :
		List<SiacTSoggettoFin> distintiSiacTSoggettiCoinvolti = CommonUtil.addAllConNewAndSoloDistintiByUid(soggettiInput, soggettiDaRelazioni);
		//
		
		//Da qui in poi si ragiona con distintiSiacTSoggettiCoinvolti composta da soggettiInput sommato a soggettiDaRelazioni
		
		//DISTINTI SiacTSoggettoFin
		ottimizzazioneSoggettoDto.setDistintiSiacTSoggettiCoinvolti(distintiSiacTSoggettiCoinvolti);
		//
		
		List<SiacRSoggettoStatoFin> distintiSiacRSoggettoStatoCoinvolti = soggettoDao.ricercaBySoggettoMassive(distintiSiacTSoggettiCoinvolti, "SiacRSoggettoStatoFin");
		List<SiacRFormaGiuridicaFin> distintiSiacRFormaGiuridicaCoinvolti = soggettoDao.ricercaBySoggettoMassive(distintiSiacTSoggettiCoinvolti, "SiacRFormaGiuridicaFin");
		List<SiacRSoggettoAttrFin> distintiSiacRSoggettoAttrCoinvolti = soggettoDao.ricercaBySoggettoMassive(distintiSiacTSoggettiCoinvolti, "SiacRSoggettoAttrFin");
		ottimizzazioneSoggettoDto.setDistintiSiacRSoggettoAttrCoinvolti(distintiSiacRSoggettoAttrCoinvolti);
		
		ottimizzazioneSoggettoDto.setDistintiSiacRSoggettoRelaz(distintiSiacRSoggettoRelaz);
		//
		
		//per mod pag e mod pag cessioni:
		List<SiacTModpagFin> distintiSiacTModpagFinCoinvolti = soggettoDao.ricercaBySoggettoMassive(distintiSiacTSoggettiCoinvolti,"SiacTModpagFin");
		List<SiacRSoggrelModpagFin> distintiSiacRSoggrelModpagFinCoinvolti = soggettoDao.ricercaSiacRSoggrelModpagFinMassive(distintiSiacRSoggettoRelaz);
		List<SiacRSoggettoRelazStatoFin> distintSiacRSoggettoRelazStatoFinCoinvolti = soggettoDao.ricercaSiacRSoggettoRelazStatoFinMassive(distintiSiacRSoggettoRelaz);
		
		ottimizzazioneSoggettoDto.setDistintiSiacTModpagFinCoinvolti(distintiSiacTModpagFinCoinvolti);
		ottimizzazioneSoggettoDto.setDistintiSiacRSoggrelModpagFinCoinvolti(distintiSiacRSoggrelModpagFinCoinvolti);
		ottimizzazioneSoggettoDto.setDistintSiacRSoggettoRelazStatoFinCoinvolti(distintSiacRSoggettoRelazStatoFinCoinvolti);
		//
		
		List<SiacTModpagFin> distintiSiacTModpagFinCoinvoltiPerCessioni = ottimizzazioneSoggettoDto.getListaTModpagsCessioniAll();
		
		List<SiacTModpagFin> tuttiIModPagCoinvolti = CommonUtil.addAllConNewAndSoloDistintiByUid(distintiSiacTModpagFinCoinvolti, distintiSiacTModpagFinCoinvoltiPerCessioni);
		
		List<SiacTModpagModFin> distintiSiacTModpagModFinCoinvolti =  soggettoDao.ricercaSiacTModpagModFinMassive(tuttiIModPagCoinvolti);
		ottimizzazioneSoggettoDto.setDistintiSiacTModpagModFinCoinvolti(distintiSiacTModpagModFinCoinvolti);
		//
		
		//cerchiamo ora i SiacRModpagStato
		List<SiacRModpagStatoFin> distintiSiacRModpagStatoFinCoinvolti = soggettoDao.ricercaSiacRModpagStatoBySiacTModpagFin(tuttiIModPagCoinvolti);
		ottimizzazioneSoggettoDto.setDistintiSiacRModpagStatoFinCoinvolti(distintiSiacRModpagStatoFinCoinvolti);
		//
		
		
		//cerchiamo ora i SiacRModpagStato
		List<SiacRModpagOrdineFin> distintiSiacRModpagOrdineFinCoinvoltiByModPag = soggettoDao.ricercaSiacRModpagOrdineFinBySiacTModpagFin(tuttiIModPagCoinvolti);
		List<SiacRModpagOrdineFin> distintiSiacRModpagOrdineFinCoinvoltiBySoggetti = soggettoDao.ricercaBySoggettoMassive(distintiSiacTSoggettiCoinvolti, "SiacRModpagOrdineFin");
		List<SiacRModpagOrdineFin> distintiSiacRModpagOrdineFinCoinvolti = CommonUtil.addAllConNewAndSoloDistintiByUid(distintiSiacRModpagOrdineFinCoinvoltiByModPag, distintiSiacRModpagOrdineFinCoinvoltiBySoggetti);
		ottimizzazioneSoggettoDto.setDistintiSiacRModpagOrdineFinCoinvolti(distintiSiacRModpagOrdineFinCoinvolti);
		//
		
		List<SiacRSoggettoClasseFin> distintiSiacRSoggettoClasses = soggettoDao.ricercaBySoggettoMassive(distintiSiacTSoggettiCoinvolti,"SiacRSoggettoClasseFin");
		List<SiacRSoggettoTipoFin> distintiSiacRSoggettoTipo = soggettoDao.ricercaBySoggettoMassive(distintiSiacTSoggettiCoinvolti,"SiacRSoggettoTipoFin");
		List<SiacTRecapitoSoggettoFin> distintiSiacTRecapitoSoggetto = soggettoDao.ricercaBySoggettoMassive(distintiSiacTSoggettiCoinvolti,"SiacTRecapitoSoggettoFin");
		List<SiacRSoggettoOnereFin> distintiSiacRSoggettoOnere = soggettoDao.ricercaBySoggettoMassive(distintiSiacTSoggettiCoinvolti,"SiacRSoggettoOnereFin");
		List<SiacTIndirizzoSoggettoFin> distintiSiacTIndirizzoSoggetto = soggettoDao.ricercaBySoggettoMassive(distintiSiacTSoggettiCoinvolti,"SiacTIndirizzoSoggettoFin");
		List<SiacTPersonaFisicaFin> distintiSiacTPersonaFisica = soggettoDao.ricercaBySoggettoMassive(distintiSiacTSoggettiCoinvolti,"SiacTPersonaFisicaFin");
		List<SiacTPersonaGiuridicaFin> distintiSiacTPersonaGiuridica = soggettoDao.ricercaBySoggettoMassive(distintiSiacTSoggettiCoinvolti,"SiacTPersonaGiuridicaFin");
		//set nel dto:
		ottimizzazioneSoggettoDto.setDistintiSiacRSoggettoStatoCoinvolti(distintiSiacRSoggettoStatoCoinvolti);
		ottimizzazioneSoggettoDto.setDistintiSiacRFormaGiuridicaCoinvolti(distintiSiacRFormaGiuridicaCoinvolti);
		
		//SiacTSoggettoModFin
		List<SiacTSoggettoModFin> distintiSiacTSoggettoModFinCoinvolti = soggettoDao.ricercaBySoggettoMassive(distintiSiacTSoggettiCoinvolti,"SiacTSoggettoModFin");
		ottimizzazioneSoggettoDto.setDistintiSiacTSoggettoModFinCoinvolti(distintiSiacTSoggettoModFinCoinvolti);
		//
		
		ottimizzazioneSoggettoDto.setDistintiSiacRSoggettoTipo(distintiSiacRSoggettoTipo);
		ottimizzazioneSoggettoDto.setDistintiSiacRSoggettoClasses(distintiSiacRSoggettoClasses);
		ottimizzazioneSoggettoDto.setDistintiSiacTRecapitoSoggetto(distintiSiacTRecapitoSoggetto);
		ottimizzazioneSoggettoDto.setDistintiSiacRSoggettoOnere(distintiSiacRSoggettoOnere);
		ottimizzazioneSoggettoDto.setDistintiSiacTIndirizzoSoggetto(distintiSiacTIndirizzoSoggetto);
		ottimizzazioneSoggettoDto.setDistintiSiacTPersonaFisica(distintiSiacTPersonaFisica);
		ottimizzazioneSoggettoDto.setDistintiSiacTPersonaGiuridica(distintiSiacTPersonaGiuridica);
		
		
		//Data una persona fisica occorre anche ottimizzare le relazioni verso comune, provincia, regione e nazione:
		List<SiacTComuneFin> distintiSiacTComuneFinCoinvoltiByPersoneFisiche = soggettoDao.ricercaSiacTComuneFinBySiacTPersonaFisicaFin(distintiSiacTPersonaFisica);
		//I COMUNI COINVOLTI SONO ANCHE DIPENDENTI DAGLI INDIRIZZI:
		List<SiacTComuneFin> distintiSiacTComuneFinCoinvoltiByIndirizziSogg = soggettoDao.ricercaSiacTComuneFinBySiacTIndirizzoSoggettoFin(distintiSiacTIndirizzoSoggetto);
		
		//COMPONGO TUTTI I COMUNI IN GIOCO:
		List<SiacTComuneFin> distintiSiacTComuneFinCoinvolti = CommonUtil.addAllConNewAndSoloDistintiByUid(distintiSiacTComuneFinCoinvoltiByPersoneFisiche, distintiSiacTComuneFinCoinvoltiByIndirizziSogg);
		
		
		List<SiacRComuneProvinciaFin> distintiSiacRComuneProvinciaFinCoinvolti =  soggettoDao.ricercaSiacRComuneProvinciaFinBySiacTComuneFin(distintiSiacTComuneFinCoinvolti);
		List<SiacRComuneRegioneFin> distintiSiacRComuneRegioneFinCoinvolti = soggettoDao.ricercaSiacRComuneRegioneFinBySiacTComuneFin(distintiSiacTComuneFinCoinvolti);
		
		/*
		List<SiacTProvinciaFin>  distintiSiacTProvinciaFinCoinvolti =   soggettoDao.ricercaSiacTProvinciaFinBySiacRComuneProvinciaFin(distintiSiacRComuneProvinciaFinCoinvolti);
		List<SiacRProvinciaRegioneFin>  distintiSiacRProvinciaRegioneFinCoinvolti =  soggettoDao.ricercaSiacRProvinciaRegioneFinBySiacTProvinciaFin(distintiSiacTProvinciaFinCoinvolti);
		List<SiacTRegioneFin> distintiSiacTRegioneFinCoinvolti =   soggettoDao.ricercaSiacTRegioneFinFinBySiacRProvinciaRegioneFin(distintiSiacRProvinciaRegioneFinCoinvolti);*/
		
		//AGOSTO 2017 scommento il caricamento nazioni, mi servono per risolvere la nazione dei riceventi delle mod pag:
		List<SiacTNazioneFin>  distintiSiacTNazioneFinCoinvolti =  soggettoDao.ricercaSiacTNazioneFinBySiacTComuneFin(distintiSiacTComuneFinCoinvolti);
		
		
		ottimizzazioneSoggettoDto.setDistintiSiacTComuneFinCoinvolti(distintiSiacTComuneFinCoinvolti);
		ottimizzazioneSoggettoDto.setDistintiSiacRComuneProvinciaFinCoinvolti(distintiSiacRComuneProvinciaFinCoinvolti);
		ottimizzazioneSoggettoDto.setDistintiSiacRComuneRegioneFinCoinvolti(distintiSiacRComuneRegioneFinCoinvolti);
		
		/*
		ottimizzazioneSoggettoDto.setDistintiSiacTProvinciaFinCoinvolti(distintiSiacTProvinciaFinCoinvolti);
		ottimizzazioneSoggettoDto.setDistintiSiacRProvinciaRegioneFinCoinvolti(distintiSiacRProvinciaRegioneFinCoinvolti);
		ottimizzazioneSoggettoDto.setDistintiSiacTRegioneFinCoinvolti(distintiSiacTRegioneFinCoinvolti);*/
		ottimizzazioneSoggettoDto.setDistintiSiacTNazioneFinCoinvolti(distintiSiacTNazioneFinCoinvolti);
		
		//
		
		//Termino restituendo l'oggetto di ritorno: 
        return ottimizzazioneSoggettoDto;
	}
	
	/***
	 * Dato un oggetto ottimizzazioneSoggettoDto ne estrapola (travasa)
	 *  solo i dati della modalita pagamento.
	 * 
	 * (non carica nulla devono gia' essere caricati)
	 * 
	 * 
	 * @param ottimizzazioneSoggettoDto
	 * @return
	 */
	protected OttimizzazioneModalitaPagamentoDto estraiDatiModPag(OttimizzazioneSoggettoDto ottimizzazioneSoggettoDto){
		OttimizzazioneModalitaPagamentoDto ottimizzazioneModPag = null;
		if(ottimizzazioneSoggettoDto!=null){
			//OK VALORIZZATO
			ottimizzazioneModPag = new OttimizzazioneModalitaPagamentoDto();
			ottimizzazioneModPag.setDistintiSiacTNazioneFinCoinvolti(ottimizzazioneSoggettoDto.getDistintiSiacTNazioneFinCoinvolti());
			ottimizzazioneModPag.setDistintiSiacTModpagModFinCoinvolti(ottimizzazioneSoggettoDto.getDistintiSiacTModpagModFinCoinvolti());
			ottimizzazioneModPag.setDistintiSiacTSiacTModpagFinCoinvolti(ottimizzazioneSoggettoDto.getDistintiSiacTModpagFinCoinvolti());
		}
		return ottimizzazioneModPag;
	}
	
	

	
	/**
	 * Somma le modifiche di importo di tipo RIUtilizzo applicate all'impegno /
	 * subimpegno
	 * 
	 * @param tMovgestTsDetMods
	 */
	protected BigDecimal getDisponibileModifiche(List<SiacTMovgestTsDetModFin> tMovgestTsDetMods) {
		BigDecimal totale = BigDecimal.ZERO;
		if(!StringUtilsFin.isEmpty(tMovgestTsDetMods)){
			for (SiacTMovgestTsDetModFin movgestTsDetMod : tMovgestTsDetMods){
				if (movgestTsDetMod.getDataFineValidita() == null) {
					SiacDModificaTipoFin modTipo = movgestTsDetMod.getSiacRModificaStato().getSiacTModifica().getSiacDModificaTipo();

					if (null != modTipo && modTipo.getModTipoCode().equals("RIU")){
						totale = totale.add(movgestTsDetMod.getMovgestTsDetImporto());
					}
						
				}
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return totale;
	}
	

	/**
	 * Metodo che aggrega i dati in maniera ottimizzata
	 * @param listaSiacTMovgestTsCoinvolti
	 * @return
	 */
	public OttimizzazioneModificheMovimentoGestioneDto caricaOttimizzazioneModificheMovimentoGestioneDto(List<SiacTMovgestTsFin> listaSiacTMovgestTsCoinvolti){
		OttimizzazioneModificheMovimentoGestioneDto ottimizzazioneModificheDto = new OttimizzazioneModificheMovimentoGestioneDto();
		
		//DISTINTI SIAC_R_MOVGEST_TS_SOG_MOD:
		List<SiacRMovgestTsSogModFin> distintiSiacRMovgestTsSogModFinCoinvolti =movimentoGestioneDao.ricercaByMovGestTsMassive(listaSiacTMovgestTsCoinvolti, "SiacRMovgestTsSogModFin",Boolean.TRUE);
		//
		
		//DISTINTI SIAC_T_MOVGEST_TS_DET_MOD_FIN:
		//
		//Per  FIN - ACCERTAMENTI con tante Modifiche (CR 615) cambio la query con una che esclude le (eventuali) modifiche automatiche:
		//List<SiacTMovgestTsDetModFin> distintiSiacTMovgestTsDetModFinCoinvolti =movimentoGestioneDao.ricercaByMovGestTsMassive(listaSiacTMovgestTsCoinvolti, "SiacTMovgestTsDetModFin",Boolean.TRUE);
		List<SiacTMovgestTsDetModFin> distintiSiacTMovgestTsDetModFinCoinvolti = movimentoGestioneDao.ricercaEscludendoModificheAutomatiche(listaSiacTMovgestTsCoinvolti);
		//
		
		//DISTINTI SiacRMovgestTsSogclasseModFin
		List<SiacRMovgestTsSogclasseModFin> distintiSiacRMovgestTsSogclasseModFinCoinvolti =movimentoGestioneDao.ricercaByMovGestTsMassive(listaSiacTMovgestTsCoinvolti, "SiacRMovgestTsSogclasseModFin",Boolean.TRUE);
		//
		
		//DISTINTI SiacTSoggettoFin
		List<SiacTSoggettoFin> distintiSiacTSoggettiCoinvolti = soggettoDao.ricercaBySiacRMovgestTsSogModFin(distintiSiacRMovgestTsSogModFinCoinvolti, null);
		ottimizzazioneModificheDto.setDistintiSiacTSoggettiCoinvolti(distintiSiacTSoggettiCoinvolti);
		//
		
		//DISTINTI SIAC_R_MOVGEST_TS_SOG_MOD:
		//List<SiacTMovgestTsDetModFin> distintiSiacTMovgestTsDetModFin =movimentoGestioneDao.ricercaByMovGestTsMassive(listaSiacTMovgestTsCoinvolti, "SiacTMovgestTsDetModFin",Boolean.TRUE);
		//
		
		List<SiacConModificaStato> listaSiacConModificaStato = new ArrayList<SiacConModificaStato>();
		if(distintiSiacRMovgestTsSogModFinCoinvolti!=null && distintiSiacRMovgestTsSogModFinCoinvolti.size()>0){
			listaSiacConModificaStato.addAll(distintiSiacRMovgestTsSogModFinCoinvolti);
		}
		if(distintiSiacTMovgestTsDetModFinCoinvolti!=null && distintiSiacTMovgestTsDetModFinCoinvolti.size()>0){
			listaSiacConModificaStato.addAll(distintiSiacTMovgestTsDetModFinCoinvolti);
		}
		if(distintiSiacRMovgestTsSogclasseModFinCoinvolti!=null && distintiSiacRMovgestTsSogclasseModFinCoinvolti.size()>0){
			listaSiacConModificaStato.addAll(distintiSiacRMovgestTsSogclasseModFinCoinvolti);
		}
		
		//DISTINTI SIAC_R_MODIFICA_STATO:
		List<SiacRModificaStatoFin> distintiSiacRModificaStatoCoinvolti = movimentoGestioneDao.ricercaBySiacConModificaStatoMassive(listaSiacConModificaStato, Boolean.TRUE);
		List<SiacTModificaFin> distintiSiacTModificaCoinvolti = movimentoGestioneDao.ricercaBySiacRModificaStatoFinMassive(distintiSiacRModificaStatoCoinvolti, Boolean.TRUE);
		
		ottimizzazioneModificheDto.setDistintiSiacRModificaStatoCoinvolti(distintiSiacRModificaStatoCoinvolti);
		ottimizzazioneModificheDto.setDistintiSiacTModificaCoinvolti(distintiSiacTModificaCoinvolti);
		
		
		//POTENZIALE RIDONDANZA. Non sono sicuro di questa query (forse e' ridondante):
		List<SiacTMovgestTsDetModFin> distintiSiacTMovgestTsDetModFinCoinvoltiByRModificaStato =movimentoGestioneDao.ricercaSiacTMovgestTsDetModFinBySiacRModificaStatoFinMassive(distintiSiacRModificaStatoCoinvolti, Boolean.TRUE);
		List<SiacTMovgestTsDetModFin> distintiSiacTMovgestTsDetModFinCoinvoltiALL = CommonUtil.addAllConNewAndSoloDistintiByUid(distintiSiacTMovgestTsDetModFinCoinvolti, distintiSiacTMovgestTsDetModFinCoinvoltiByRModificaStato);
		ottimizzazioneModificheDto.setDistintiSiacTMovgestTsDetModFinCoinvolti(distintiSiacTMovgestTsDetModFinCoinvoltiALL);
		//il mio dubbio e' che distintiSiacTMovgestTsDetModFinCoinvolti sia gia' uguale a distintiSiacTMovgestTsDetModFinCoinvoltiALL
		
		
		//POTENZIALE RIDONDANZA. Non sono sicuro di questa query (forse e' ridondante):
		List<SiacRMovgestTsSogModFin> distintiSiacRMovgestTsSogModFinCoinvoltiByRModificaStato =movimentoGestioneDao.ricercaSiacRMovgestTsSogModFinBySiacRModificaStatoFinMassive(distintiSiacRModificaStatoCoinvolti, Boolean.TRUE);
		List<SiacRMovgestTsSogModFin> distintiSiacRMovgestTsSogModFinCoinvoltiALL = CommonUtil.addAllConNewAndSoloDistintiByUid(distintiSiacRMovgestTsSogModFinCoinvolti, distintiSiacRMovgestTsSogModFinCoinvoltiByRModificaStato);
		ottimizzazioneModificheDto.setDistintiSiacRMovgestTsSogModFinCoinvolti(distintiSiacRMovgestTsSogModFinCoinvoltiALL);
		//il mio dubbio e' che distintiSiacRMovgestTsSogModFinCoinvolti sia gia' uguale a distintiSiacRMovgestTsSogModFinCoinvoltiALL
		
		
		//POTENZIALE RIDONDANZA. Non sono sicuro di questa query (forse e' ridondante):
		List<SiacRMovgestTsSogclasseModFin> distintiSiacRMovgestTsSogclasseModFinCoinvoltiByRModificaStato =movimentoGestioneDao.ricercaSiacRMovgestTsSogclasseModFinBySiacRModificaStatoFinMassive(distintiSiacRModificaStatoCoinvolti, Boolean.TRUE);
		List<SiacRMovgestTsSogclasseModFin> distintiSiacRMovgestTsSogclasseModFinCoinvoltiALL = CommonUtil.addAllConNewAndSoloDistintiByUid(distintiSiacRMovgestTsSogclasseModFinCoinvolti, distintiSiacRMovgestTsSogclasseModFinCoinvoltiByRModificaStato);
		ottimizzazioneModificheDto.setDistintiSiacRMovgestTsSogclasseModFinCoinvolti(distintiSiacRMovgestTsSogclasseModFinCoinvoltiALL);
		//il mio dubbio e' che distintiSiacRMovgestTsSogclasseModFinCoinvolti sia gia' uguale a distintiSiacRMovgestTsSogclasseModFinCoinvoltiALL	
		
		//Termino restituendo l'oggetto di ritorno: 
        return ottimizzazioneModificheDto;
	}
	
	
	public OttimizzazioneModificheMovimentoGestioneDto caricaOttimizzazioneModificheMovimentoGestioneDtoPerMaxNumber(List<SiacTMovgestTsFin> listaSiacTMovgestTsCoinvolti){
		OttimizzazioneModificheMovimentoGestioneDto ottimizzazioneModificheDto = new OttimizzazioneModificheMovimentoGestioneDto();
		
		//DISTINTI SIAC_R_MOVGEST_TS_SOG_MOD:
		List<SiacRMovgestTsSogModFin> distintiSiacRMovgestTsSogModFinCoinvolti =movimentoGestioneDao.ricercaByMovGestTsMassive(listaSiacTMovgestTsCoinvolti, "SiacRMovgestTsSogModFin",Boolean.TRUE);
		//
		
		//DISTINTI SIAC_T_MOVGEST_TS_DET_MOD_FIN:
		//
		//Per  FIN - ACCERTAMENTI con tante Modifiche (CR 615) cambio la query con una che esclude le (eventuali) modifiche automatiche:
		//List<SiacTMovgestTsDetModFin> distintiSiacTMovgestTsDetModFinCoinvolti =movimentoGestioneDao.ricercaByMovGestTsMassive(listaSiacTMovgestTsCoinvolti, "SiacTMovgestTsDetModFin",Boolean.TRUE);
		List<SiacTMovgestTsDetModFin> distintiSiacTMovgestTsDetModFinCoinvolti = movimentoGestioneDao.ricercaEscludendoModificheAutomatiche(listaSiacTMovgestTsCoinvolti);
		//
		
		//DISTINTI SiacRMovgestTsSogclasseModFin
		List<SiacRMovgestTsSogclasseModFin> distintiSiacRMovgestTsSogclasseModFinCoinvolti =movimentoGestioneDao.ricercaByMovGestTsMassive(listaSiacTMovgestTsCoinvolti, "SiacRMovgestTsSogclasseModFin",Boolean.TRUE);
		//
		
		//DISTINTI SiacTSoggettoFin
		List<SiacTSoggettoFin> distintiSiacTSoggettiCoinvolti = soggettoDao.ricercaBySiacRMovgestTsSogModFin(distintiSiacRMovgestTsSogModFinCoinvolti, null);
		ottimizzazioneModificheDto.setDistintiSiacTSoggettiCoinvolti(distintiSiacTSoggettiCoinvolti);
		//
		
		//DISTINTI SIAC_R_MOVGEST_TS_SOG_MOD:
		//List<SiacTMovgestTsDetModFin> distintiSiacTMovgestTsDetModFin =movimentoGestioneDao.ricercaByMovGestTsMassive(listaSiacTMovgestTsCoinvolti, "SiacTMovgestTsDetModFin",Boolean.TRUE);
		//
		
		List<SiacConModificaStato> listaSiacConModificaStato = new ArrayList<SiacConModificaStato>();
		if(distintiSiacRMovgestTsSogModFinCoinvolti!=null && distintiSiacRMovgestTsSogModFinCoinvolti.size()>0){
			listaSiacConModificaStato.addAll(distintiSiacRMovgestTsSogModFinCoinvolti);
		}
		if(distintiSiacTMovgestTsDetModFinCoinvolti!=null && distintiSiacTMovgestTsDetModFinCoinvolti.size()>0){
			listaSiacConModificaStato.addAll(distintiSiacTMovgestTsDetModFinCoinvolti);
		}
		if(distintiSiacRMovgestTsSogclasseModFinCoinvolti!=null && distintiSiacRMovgestTsSogclasseModFinCoinvolti.size()>0){
			listaSiacConModificaStato.addAll(distintiSiacRMovgestTsSogclasseModFinCoinvolti);
		}
		
		//DISTINTI SIAC_R_MODIFICA_STATO:
		List<SiacRModificaStatoFin> distintiSiacRModificaStatoCoinvolti = movimentoGestioneDao.ricercaBySiacConModificaStatoMassive(listaSiacConModificaStato, Boolean.TRUE);
		List<SiacTModificaFin> distintiSiacTModificaCoinvolti = movimentoGestioneDao.ricercaBySiacRModificaStatoFinMassive(distintiSiacRModificaStatoCoinvolti, Boolean.TRUE);
		
		ottimizzazioneModificheDto.setDistintiSiacRModificaStatoCoinvolti(distintiSiacRModificaStatoCoinvolti);
		ottimizzazioneModificheDto.setDistintiSiacTModificaCoinvolti(distintiSiacTModificaCoinvolti);
		
		
		//POTENZIALE RIDONDANZA. Non sono sicuro di questa query (forse e' ridondante):
		List<SiacTMovgestTsDetModFin> distintiSiacTMovgestTsDetModFinCoinvoltiByRModificaStato =movimentoGestioneDao.ricercaSiacTMovgestTsDetModFinBySiacRModificaStatoFinMassive(distintiSiacRModificaStatoCoinvolti, Boolean.TRUE);
		List<SiacTMovgestTsDetModFin> distintiSiacTMovgestTsDetModFinCoinvoltiALL = CommonUtil.addAllConNewAndSoloDistintiByUid(distintiSiacTMovgestTsDetModFinCoinvolti, distintiSiacTMovgestTsDetModFinCoinvoltiByRModificaStato);
		ottimizzazioneModificheDto.setDistintiSiacTMovgestTsDetModFinCoinvolti(distintiSiacTMovgestTsDetModFinCoinvoltiALL);
		//il mio dubbio e' che distintiSiacTMovgestTsDetModFinCoinvolti sia gia' uguale a distintiSiacTMovgestTsDetModFinCoinvoltiALL
		
		
		//POTENZIALE RIDONDANZA. Non sono sicuro di questa query (forse e' ridondante):
		List<SiacRMovgestTsSogModFin> distintiSiacRMovgestTsSogModFinCoinvoltiByRModificaStato =movimentoGestioneDao.ricercaSiacRMovgestTsSogModFinBySiacRModificaStatoFinMassive(distintiSiacRModificaStatoCoinvolti, Boolean.TRUE);
		List<SiacRMovgestTsSogModFin> distintiSiacRMovgestTsSogModFinCoinvoltiALL = CommonUtil.addAllConNewAndSoloDistintiByUid(distintiSiacRMovgestTsSogModFinCoinvolti, distintiSiacRMovgestTsSogModFinCoinvoltiByRModificaStato);
		ottimizzazioneModificheDto.setDistintiSiacRMovgestTsSogModFinCoinvolti(distintiSiacRMovgestTsSogModFinCoinvoltiALL);
		//il mio dubbio e' che distintiSiacRMovgestTsSogModFinCoinvolti sia gia' uguale a distintiSiacRMovgestTsSogModFinCoinvoltiALL
		
		
		//POTENZIALE RIDONDANZA. Non sono sicuro di questa query (forse e' ridondante):
		List<SiacRMovgestTsSogclasseModFin> distintiSiacRMovgestTsSogclasseModFinCoinvoltiByRModificaStato =movimentoGestioneDao.ricercaSiacRMovgestTsSogclasseModFinBySiacRModificaStatoFinMassive(distintiSiacRModificaStatoCoinvolti, Boolean.TRUE);
		List<SiacRMovgestTsSogclasseModFin> distintiSiacRMovgestTsSogclasseModFinCoinvoltiALL = CommonUtil.addAllConNewAndSoloDistintiByUid(distintiSiacRMovgestTsSogclasseModFinCoinvolti, distintiSiacRMovgestTsSogclasseModFinCoinvoltiByRModificaStato);
		ottimizzazioneModificheDto.setDistintiSiacRMovgestTsSogclasseModFinCoinvolti(distintiSiacRMovgestTsSogclasseModFinCoinvoltiALL);
		//il mio dubbio e' che distintiSiacRMovgestTsSogclasseModFinCoinvolti sia gia' uguale a distintiSiacRMovgestTsSogclasseModFinCoinvoltiALL
		
		//Termino restituendo l'oggetto di ritorno: 
        return ottimizzazioneModificheDto;
	}
	
	/**
	 * Carica i dati di ottimizzazione secondo le esigenze specifiche della consultazione degli impegni vincolati
	 * ad un accertamento particolare.
	 * @param siacTMovgestTsAccertamento
	 * @param datiOperazioneDto
	 * @param soggettoDad
	 * @return
	 */
	protected OttimizzazioneMovGestDto caricaOttimizzazioneImpegniVincolatiAdUnAccertamento(
			SiacTMovgestTsFin siacTMovgestTsAccertamento,
			DatiOperazioneDto datiOperazioneDto,SoggettoFinDad soggettoDad){
		
		OttimizzazioneMovGestDto ottimizzazioneMovGest = new OttimizzazioneMovGestDto();
		
		//RELAZIONE VERSO I VINCOLI:
		List<SiacRMovgestTsFin> listaRelazioniVincoli = movimentoGestioneDao.ricercaBySiacTMovgestTsFinMassive(toList(siacTMovgestTsAccertamento), Boolean.TRUE, Boolean.TRUE);
		ottimizzazioneMovGest.setDistintiSiacRMovgestTsFinCoinvolti(listaRelazioniVincoli);
		
		List<SiacTMovgestFin> distintiSiacTMovgestFinCoinvolti = ottimizzazioneMovGest.estraiDistintiSiacTMovgestFinImpegniBySiacRMovgestTsFinCoinvolti();
		
		
		//metodo core di ottimizzazione:
		ottimizzazioneMovGest = caricaDatiOttimizzazioneMovGestTsPerConultaVincoliAccertamento(ottimizzazioneMovGest, distintiSiacTMovgestFinCoinvolti, CostantiFin.MOVGEST_TIPO_IMPEGNO, datiOperazioneDto);
		
		List<SiacTMovgestTsFin> distintiSiacTMovgestTsFin = ottimizzazioneMovGest.getDistintiSiacTMovgestTsFinCoinvolti();
		
		//Soggetti coinvolti:
		OttimizzazioneSoggettoDto ottimizzazioneSoggettoDto = new OttimizzazioneSoggettoDto();
		//DISTINTI SiacTSoggettoFin
		List<SiacTSoggettoFin> soggettiImpegni = soggettoDao.ricercaBySiacTMovgestPkMassive(distintiSiacTMovgestTsFin);
		ottimizzazioneSoggettoDto.setDistintiSiacTSoggettiCoinvolti(soggettiImpegni);
		//
		
		//SIAC R SOGGETTI MOV GEST COINVOLI:
		List<SiacRMovgestTsSogFin> distintiSiacRSoggettiCoinvolti = movimentoGestioneDao.ricercaByMovGestTsMassive(distintiSiacTMovgestTsFin, "SiacRMovgestTsSogFin");
		ottimizzazioneSoggettoDto.setDistintiSiacRSoggettiCoinvolti(distintiSiacRSoggettiCoinvolti);
		ottimizzazioneMovGest.setDistintiSiacRSoggettiCoinvolti(distintiSiacRSoggettiCoinvolti);
		
		//SIAC R CLASSI MOV GEST COINVOLTI 
		List<SiacRMovgestTsSogclasseFin> distintiSiacRMovgestTsSogclasseFinCoinvolti = movimentoGestioneDao.ricercaByMovGestTsMassive(distintiSiacTMovgestTsFin, "SiacRMovgestTsSogclasseFin");
		ottimizzazioneSoggettoDto.setDistintiSiacRMovgestTsSogclasseFinCoinvolti(distintiSiacRMovgestTsSogclasseFinCoinvolti);
		ottimizzazioneMovGest.setDistintiSiacRMovgestTsSogclasseCoinvolti(distintiSiacRMovgestTsSogclasseFinCoinvolti);
		
		//SETTING SOGGETTI:
		ottimizzazioneMovGest.setOttimizzazioneSoggetti(ottimizzazioneSoggettoDto);
		//
		
		
		//Termino restituendo l'oggetto di ritorno: 
       return ottimizzazioneMovGest;
       
	}
	
	/**
	 * 
	 * Consultanto un accertamento con vincoli si deve accedere ai dati dei suoi impegni.
	 * 
	 * Questo metodo di occupa di caricare i dati minimi degli impegni
	 *  (legati ad un accertamento tramite vincoli) necessari per tale consultazione.
	 * 
	 * @param ottimizzazioneDto
	 * @param distintiSiacTMovgestTsFin
	 * @param tipoMovimento
	 * @param datiOperazione
	 * @return
	 */
	public OttimizzazioneMovGestDto caricaDatiOttimizzazioneMovGestTsPerConultaVincoliAccertamento(OttimizzazioneMovGestDto ottimizzazioneDto,
			List<SiacTMovgestFin> distintiSiacTMovgestFinCoinvolti, String tipoMovimento, DatiOperazioneDto datiOperazione){
		
		
		if(ottimizzazioneDto==null){
			ottimizzazioneDto = new OttimizzazioneMovGestDto();
		}
		ottimizzazioneDto.setDistintiSiacTMovgestFinCoinvolti(distintiSiacTMovgestFinCoinvolti);
		
		// Distinti SiacTMovgestTsFin
		List<Integer> listaMovgestIds = CommonUtil.getIdListSiacTBase(distintiSiacTMovgestFinCoinvolti);
		List<SiacTMovgestTsFin> distintiSiacTMovgestTsFin = movimentoGestioneDao.ricercaSiacTMovgestTsFinBySiacTMovgestMassive(listaMovgestIds, true);
		ottimizzazioneDto.setDistintiSiacTMovgestTsFinCoinvolti(distintiSiacTMovgestTsFin);
		
		
		//STATI:
		List<SiacRMovgestTsStatoFin> distintiSiacRMovgestTsStatoCoinvolti = movimentoGestioneDao.ricercaByMovGestTsMassive(distintiSiacTMovgestTsFin,"SiacRMovgestTsStatoFin");
		ottimizzazioneDto.setDistintiSiacRMovgestTsStatoCoinvolti(distintiSiacRMovgestTsStatoCoinvolti);
		//
		
		//IMPORTI DEI MOVIMENTI COLLEGATI:
		List<SiacTMovgestTsDetFin> distintiSiacTMovgestTsDetCoinvolti = movimentoGestioneDao.ricercaByMovGestTsMassive(distintiSiacTMovgestTsFin,"SiacTMovgestTsDetFin");
		ottimizzazioneDto.setDistintiSiacTMovgestTsDetCoinvolti(distintiSiacTMovgestTsDetCoinvolti);
		//
		
		//T CLASS:
		List<SiacRMovgestClassFin> distintiSiacRMovgestClassCoinvolti =  movimentoGestioneDao.ricercaByMovGestTsMassive(distintiSiacTMovgestTsFin,"SiacRMovgestClassFin");
		ottimizzazioneDto.setDistintiSiacRMovgestClassCoinvolti(distintiSiacRMovgestClassCoinvolti);
		
		//T ATTR:
		List<SiacRMovgestTsAttrFin> distintiSiacRMovgestTsAttrCoinvolti =  movimentoGestioneDao.ricercaByMovGestTsMassive(distintiSiacTMovgestTsFin,"SiacRMovgestTsAttrFin");
		ottimizzazioneDto.setDistintiSiacRMovgestTsAttrCoinvolti(distintiSiacRMovgestTsAttrCoinvolti);
		
		//ATTI AMMINISTRATIVI:
		List<SiacRMovgestTsAttoAmmFin> distintiSiacRMovgestTsAttoAmmCoinvolti =movimentoGestioneDao.ricercaByMovGestTsMassive(distintiSiacTMovgestTsFin, "SiacRMovgestTsAttoAmmFin");
		ottimizzazioneDto.setDistintiSiacRMovgestTsAttoAmmCoinvolti(distintiSiacRMovgestTsAttoAmmCoinvolti);
		
		return ottimizzazioneDto;
		
	}
	
	/**
	 * Metodo centralizzato per impegno, liquidazione, ordinativo pagamento per verificare la coerenza dei dati siope plus
	 * 
	 * daImpegno viene passato a true per impegni e sub impegni
	 * a false per liquidazioni e ordinativi di pagamento
	 * 
	 * @param siopeTipoDebito
	 * @param siopeAssenzaMotivazione
	 * @param cig
	 * @param datiOperazione
	 * @param daImpegno
	 * @return
	 */
	protected List<Errore> controlliSiopePlus(SiopeTipoDebito siopeTipoDebito,SiopeAssenzaMotivazione siopeAssenzaMotivazione,
			String cig, DatiOperazioneDto datiOperazione, boolean daImpegno){
		
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		Integer idEnte = datiOperazione.getSiacTEnteProprietario().getEnteProprietarioId();
		
		//SIOPE TIPO DEBITO
		String codiceTipoDebito = null;
		if(siopeTipoDebito!=null && !StringUtilsFin.isEmpty(siopeTipoDebito.getCodice())){
			//recuperiamo il record della codifica ricevuta:
			List<SiacDSiopeTipoDebitoFin> dstdebitos = siacDSiopeTipoDebitoFinRepository.findByCode(idEnte, datiOperazione.getTs(), siopeTipoDebito.getCodice());
			SiacDSiopeTipoDebitoFin siacDSiopeTipoDebito = CommonUtil.getFirst(dstdebitos);
			if(siacDSiopeTipoDebito!=null){
				//ok esiste valido
				codiceTipoDebito = siopeTipoDebito.getCodice();
			} else {
				//non esiste
				listaErrori.add(ErroreCore.ENTITA_NON_TROVATA.getErrore("Siope Tipo Debito", "codice: " + siopeTipoDebito.getCodice() ));
				return listaErrori;
			}
		} else {
			//tipo debito omesso
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Siope Tipo Debito"));
			return listaErrori;
		}
		
		
		//MOTIVAZIONE ASSENZA CIG
		
		String codiceMotivazioneAssenzaCig = null;
		if(siopeAssenzaMotivazione!=null && !StringUtilsFin.isEmpty(siopeAssenzaMotivazione.getCodice())){
			codiceMotivazioneAssenzaCig = siopeAssenzaMotivazione.getCodice();
		}
		
		if(CostantiFin.SIOPE_CODE_COMMERCIALE.equals(codiceTipoDebito)
	    		&& StringUtilsFin.entrambiVuoti(cig,codiceMotivazioneAssenzaCig)){
	    	//SE Tipo SIOPE = Commerciale
	        //CIG o, in alternativa, Motivazione assenza CIG sono obbligatori 
	    	listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("CIG", " (Per tipo debito Commerciale occorre indicare il CIG oppure la Motivazione assenza CIG )"));
	    	return listaErrori;
		}
		
		if(StringUtilsFin.entrambiValorizzati(cig,codiceMotivazioneAssenzaCig)){
	    	//CIG e Motivazione assenza CIG non possono coesistere
	    	listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("CIG", " (Compilare un solo campo tra CIG e Motivazione assenza CIG)"));
	    	return listaErrori;
	    }
		
		if(!StringUtilsFin.isEmpty(codiceMotivazioneAssenzaCig)){
			//recuperiamo il record della codifica ricevuta:
			List<SiacDSiopeAssenzaMotivazioneFin> aMtvs = siacDSiopeAssenzaMotivazioneFinRepository.findByCode(idEnte, datiOperazione.getTs(), siopeAssenzaMotivazione.getCodice());
			SiacDSiopeAssenzaMotivazioneFin siacDSiopeAssenzaMotivazione = CommonUtil.getFirst(aMtvs);
			if(siacDSiopeAssenzaMotivazione==null){
				//non esiste
				listaErrori.add(ErroreCore.ENTITA_NON_TROVATA.getErrore("Siope Assenza Motivazione CIG", "codice: " + codiceMotivazioneAssenzaCig ));
				return listaErrori;
			} else {
				
				if(!daImpegno){
					
					//Non sono da impegno (quindi liq o ordinativo), queste motivazioni assenza non sono accettabili:
					
					//SIAC-5526
					if(CostantiFin.ASSENZA_CIG_DA_DEFINIRE_IN_FASE_DI_LIQUIDAZIONE.equals(siacDSiopeAssenzaMotivazione.getSiopeAssenzaMotivazioneCode())){
						//SIAC-7904
//						listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("CIG", "Cig da definire in fase di liquidazione non accettabile"));
						listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("motivo assenza cig :"  + CostantiFin.ASSENZA_CIG_DA_DEFINIRE_IN_FASE_DI_LIQUIDAZIONE,"selezionare una motivazione valida"));
						return listaErrori;
					}
					
					// SIAC-5543
					if(CostantiFin.ASSENZA_CIG_CODE_IN_CORSO_DI_DEFINIZIONE.equals(siacDSiopeAssenzaMotivazione.getSiopeAssenzaMotivazioneCode())){
						//SIAC-7904
//						listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("CIG", "Cig in corso di definizione non accettabile"));
						listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("motivo assenza cig :"  + CostantiFin.ASSENZA_CIG_CODE_IN_CORSO_DI_DEFINIZIONE,"selezionare una motivazione valida"));
						return listaErrori;
					}
				}
				
			}
		}
		
		return listaErrori;
	}
	
	/**
	 * 
	 * Assume che in subAccertamento.uid ci sia il movgestts id (dato che non ha movgest)
	 * 
	 * @param subAccertamento
	 * @return
	 */
	public MovGestInfoDto caricaSiactMovgestBySubAccertamento(SubAccertamento subAccertamento){
		MovGestInfoDto movGestInfoDto = new MovGestInfoDto();
		if(subAccertamento!=null){
			return caricaSiactMovgestETsByMovGestTsId(subAccertamento.getUid());
		}
		return movGestInfoDto;
	}
	
	/**
	 * 
	 * Assume che in subImpegno.uid ci sia il movgestts id (dato che non ha movgest)
	 * 
	 * @param subImpegno
	 * @return
	 */
	public MovGestInfoDto caricaSiactMovgestBySubImpegno(SubImpegno subImpegno){
		MovGestInfoDto movGestInfoDto = new MovGestInfoDto();
		if(subImpegno!=null){
			return caricaSiactMovgestETsByMovGestTsId(subImpegno.getUid());
		}
		return movGestInfoDto;
	}
	
	/**
	 * 
	 * Assume che in accertamento.uid ci sia il movgest id
	 * in accordo con EntityToModelConverter metodo siacTMovgestEntityToImpegnoModel
	 * che vedo basarsi come confronto su int idConfronto = itsiac.getMovgestId();
	 * 
	 * @param accertamento
	 * @return
	 */
	public MovGestInfoDto caricaSiactMovgestByAccertamento(Accertamento accertamento){
		MovGestInfoDto movGestInfoDto = new MovGestInfoDto();
		if(accertamento!=null){
			return caricaSiactMovgestETsByMovGestId(accertamento.getUid());
		}
		return movGestInfoDto;
	}
	
	/**
	 * 
	 * Assume che in impegno.uid ci sia il movgest id
	 * in accordo con EntityToModelConverter metodo siacTMovgestEntityToImpegnoModel
	 * che vedo basarsi come confronto su int idConfronto = itsiac.getMovgestId();
	 * 
	 * @param impegno
	 * @return
	 */
	public MovGestInfoDto caricaSiactMovgestByImpegno(Impegno impegno){
		MovGestInfoDto movGestInfoDto = new MovGestInfoDto();
		if(impegno!=null){
			return caricaSiactMovgestETsByMovGestId(impegno.getUid());
		}
		return movGestInfoDto;
	}
	
	private MovGestInfoDto caricaSiactMovgestETsByMovGestTsId(int movGestTsId){
		MovGestInfoDto movGestInfoDto = new MovGestInfoDto();
		SiacTMovgestTsFin siacTMovgestTs = siacTMovgestTsRepository.findOne(movGestTsId);
		if(siacTMovgestTs!=null){
			movGestInfoDto.setSiacTMovgest(siacTMovgestTs.getSiacTMovgest());
			movGestInfoDto.setSiacTMovgestTs(siacTMovgestTs);
		}
		return movGestInfoDto;
	}
	
	public MovGestInfoDto caricaSiactMovgestETsByMovGestId(int movGestId){
		MovGestInfoDto movGestInfoDto = new MovGestInfoDto();
		SiacTMovgestFin siacTMovgest = siacTMovgestRepository.findOne(movGestId);
		if(siacTMovgest!=null){
			movGestInfoDto.setSiacTMovgest(siacTMovgest);
			SiacTMovgestTsFin siacTMovgestTs = estraiTestata(siacTMovgest);
			movGestInfoDto.setSiacTMovgestTs(siacTMovgestTs);
		}
		return movGestInfoDto;
	}
	
	/**
	 * Dato un SiacTMovgestFin restituisce il SiacTMovgestTsFin testata
	 * @param siacTMovgest
	 * @return
	 */
	protected SiacTMovgestTsFin estraiTestata(SiacTMovgestFin siacTMovgest){
		SiacTMovgestTsFin testata = null;
		if(siacTMovgest!=null && siacTMovgest.getSiacTMovgestTs()!=null && siacTMovgest.getSiacTMovgestTs().size()>0){
			List<SiacTMovgestTsFin> puliti = DatiOperazioneUtil.soloValidi(siacTMovgest.getSiacTMovgestTs(), getNow());
			for(SiacTMovgestTsFin it: puliti){
				if(it.getMovgestTsIdPadre()==null){
					testata = it;
					break;
				}
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return testata;
	}

}
