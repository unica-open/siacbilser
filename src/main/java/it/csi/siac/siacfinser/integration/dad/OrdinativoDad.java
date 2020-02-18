/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ClassificatoreStipendi;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEGest;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUGest;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.TipoClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.DettaglioOnere;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoOnere;
import it.csi.siac.siacfinser.CommonUtils;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.ReintroitoUtils;
import it.csi.siac.siacfinser.StringUtils;
import it.csi.siac.siacfinser.TimingUtils;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.integration.dao.common.SiacDAmbitoRepository;
import it.csi.siac.siacfinser.integration.dao.common.SiacDCausaleFinRepository;
import it.csi.siac.siacfinser.integration.dao.common.SiacDRelazTipoFinRepository;
import it.csi.siac.siacfinser.integration.dao.common.SiacTEnteProprietarioFinRepository;
import it.csi.siac.siacfinser.integration.dao.common.dto.AccertamentoModAutomaticaPerReintroitoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.AccertamentoPerReintroitoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.AttributoTClassInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoRicercaMovimentoPkDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ImpegnoPerReintroitoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.MovGestInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.MovimentoGestioneLigthDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OggettoDellAttributoTClass;
import it.csi.siac.siacfinser.integration.dao.common.dto.OrdinativoInInserimentoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OrdinativoInModificaInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OrdinativoInReintroitoDatiDiInputDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OrdinativoInReintroitoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneModalitaPagamentoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneMovGestDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneMutuoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneOrdinativoIncassoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneOrdinativoPagamentoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneSoggettoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.PaginazioneSubMovimentiDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RegolarizzazioniDiCassaInModificaInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaOrdinativoPerChiaveDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RitenutaSpiltPerReintroitoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RitenuteReintroitoConStessoMovimentoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.SediEModPagOrdinativoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.SubOrdinativoImportoVariatoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.SubOrdinativoInModificaInfoDto;
import it.csi.siac.siacfinser.integration.dao.liquidazione.SiacDContotesoreriaFinRepository;
import it.csi.siac.siacfinser.integration.dao.liquidazione.SiacDDistintaRepository;
import it.csi.siac.siacfinser.integration.dao.liquidazione.SiacRLiquidazioneOrdRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTAttoAmmFinRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTBilElemFinRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTBilFinRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTMovgestTsRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.OrdinativoDao;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacDCodiceBolloFinRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacDCommissioneTipoRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacDNoteTesoriereFinRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacDOrdinativoStatoRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacDOrdinativoTipoRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacDOrdinativoTsDetTipoRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacRDocOnereOrdinativoTsFinRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacROrdinativoAttoAmmRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacROrdinativoBilElemRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacROrdinativoClassRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacROrdinativoModpagRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacROrdinativoProvCassaRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacROrdinativoRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacROrdinativoSoggettoRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacROrdinativoStatoRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacROrdinativoTsMovgestTsRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacRSubdocOrdinativoTFinRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacTOilRicevutaRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacTOrdinativoRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacTOrdinativoTRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacTOrdinativoTsDetRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacTProvCassaRepository;
import it.csi.siac.siacfinser.integration.dao.provvisorioDiCassa.ProvvisorioDiCassaDao;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacDSoggettoClasseRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacRSoggettoClasseRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacRSoggettoRelazFinRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacTModpagFinRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacTSoggettoFinRepository;
import it.csi.siac.siacfinser.integration.entity.SiacDAccreditoTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDAmbitoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDCausaleFin;
import it.csi.siac.siacfinser.integration.entity.SiacDCodicebolloFin;
import it.csi.siac.siacfinser.integration.entity.SiacDCommissioneTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDContotesoreriaFin;
import it.csi.siac.siacfinser.integration.entity.SiacDDistintaFin;
import it.csi.siac.siacfinser.integration.entity.SiacDNoteTesoriereFin;
import it.csi.siac.siacfinser.integration.entity.SiacDOilRicevutaTipo;
import it.csi.siac.siacfinser.integration.entity.SiacDOnereFin;
import it.csi.siac.siacfinser.integration.entity.SiacDOrdinativoStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDOrdinativoTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDOrdinativoTsDetTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDRelazTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDSiopeAssenzaMotivazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacDSiopeTipoDebitoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRDocOnereFin;
import it.csi.siac.siacfinser.integration.entity.SiacRDocOnereOrdinativoTsFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneOrdFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoAttoAmmFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoBilElemFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoFirma;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoModpagFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoProvCassaFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoQuietanza;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoTsMovgestTFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoRelazFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSubdocOrdinativoTFin;
import it.csi.siac.siacfinser.integration.entity.SiacTAttoAmmFin;
import it.csi.siac.siacfinser.integration.entity.SiacTBilElemFin;
import it.csi.siac.siacfinser.integration.entity.SiacTBilFin;
import it.csi.siac.siacfinser.integration.entity.SiacTClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacTDocFin;
import it.csi.siac.siacfinser.integration.entity.SiacTEnteProprietarioFin;
import it.csi.siac.siacfinser.integration.entity.SiacTLiquidazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModpagFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOilRicevuta;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoTFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoTsDetFin;
import it.csi.siac.siacfinser.integration.entity.SiacTProvCassaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSubdocFin;
import it.csi.siac.siacfinser.integration.entity.enumeration.SiacDOilRicevutaTipoEnum;
import it.csi.siac.siacfinser.integration.entity.mapping.FinMapId;
import it.csi.siac.siacfinser.integration.util.DatiOperazioneUtils;
import it.csi.siac.siacfinser.integration.util.EntityOrdinativiToModelOrdinativiConverter;
import it.csi.siac.siacfinser.integration.util.EntityToModelConverter;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.integration.util.TransazioneElementareEntityToModelConverter;
import it.csi.siac.siacfinser.integration.util.dto.SiacTOrdinativoCollegatoCustom;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.ContoTesoreria;
import it.csi.siac.siacfinser.model.Distinta;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.codifiche.CodiceBollo;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.CommissioneDocumento;
import it.csi.siac.siacfinser.model.codifiche.ModalitaAccreditoSoggetto;
import it.csi.siac.siacfinser.model.codifiche.NoteTesoriere;
import it.csi.siac.siacfinser.model.codifiche.TipoAvviso;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.mutuo.Mutuo;
import it.csi.siac.siacfinser.model.mutuo.VoceMutuo;
import it.csi.siac.siacfinser.model.ordinativo.DatiOrdinativoTrasmesso;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo.StatoOperativoOrdinativo;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo.TipoAssociazioneEmissione;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ordinativo.RegolarizzazioneProvvisorio;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativo;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoPagamento;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.ric.MovimentoKey;
import it.csi.siac.siacfinser.model.ric.OrdinativoKey;
import it.csi.siac.siacfinser.model.ric.ReintroitoRitenutaSplit;
import it.csi.siac.siacfinser.model.ric.ReintroitoRitenute;
import it.csi.siac.siacfinser.model.ric.RicercaOrdinativoIncassoK;
import it.csi.siac.siacfinser.model.ric.RicercaOrdinativoPagamentoK;
import it.csi.siac.siacfinser.model.ric.RicercaProvvisorioDiCassaK;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;
import it.csi.siac.siacfinser.model.siopeplus.SiopeTipoDebito;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto.TipoAccredito;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;


@Transactional
public abstract class OrdinativoDad <T extends Ordinativo, S extends SubOrdinativo>  extends AbstractFinDad {
	
	
	@Autowired
	SiacROrdinativoClassRepository siacROrdinativoClassRepository;
	
	@Autowired
	SiacTProvCassaRepository siacTProvCassaRepository;
	
	@Autowired
	SiacROrdinativoProvCassaRepository siacROrdinativoProvCassaRepository;
	
	@Autowired
	SiacTEnteProprietarioFinRepository siacTEnteProprietarioRepository;
	
	@Autowired
	SiacDAmbitoRepository siacDAmbitoRepository;
	
	@Autowired
	SiacDCausaleFinRepository siacDCausaleFinRepository;
	
	@Autowired
	SiacTOrdinativoRepository siacTOrdinativoRepository;
	
	@Autowired
	SiacDOrdinativoTipoRepository siacDOrdinativoTipoRepository;
	
	@Autowired
	SiacDCodiceBolloFinRepository siacDCodiceBolloRepository;
	
	@Autowired
	SiacDCommissioneTipoRepository siacDCommissioneTipoRepository;
	
	@Autowired
	SiacTOilRicevutaRepository siacTOilRicevutaRepository;
	
	@Autowired
	SiacDContotesoreriaFinRepository siacDContotesoreriaRepository;
	
	@Autowired
	SiacDNoteTesoriereFinRepository siacDNoteTesoriereRepository;
	
	@Autowired
	SiacDDistintaRepository siacDDistintaRepository;
	
	@Autowired
	SiacDRelazTipoFinRepository siacDRelazTipoFinRepository;

	@Autowired
	SiacTBilElemFinRepository siacTBilElemRepository;
	
	@Autowired
	SiacROrdinativoBilElemRepository siacROrdinativoBilElemRepository;
	
	@Autowired
	SiacDOrdinativoStatoRepository  siacDOrdinativoStatoRepository;
	
	@Autowired
	SiacROrdinativoStatoRepository siacROrdinativoStatoRepository;
	
	@Autowired
	SiacRDocOnereOrdinativoTsFinRepository  siacRDocOnereOrdinativoTsFinRepository;

	@Autowired
	SiacTSoggettoFinRepository siacTSoggettoRepository;
	
	@Autowired
	SiacROrdinativoSoggettoRepository siacROrdinativoSoggettoRepository;
	
	@Autowired
	SiacROrdinativoRepository siacROrdinativoRepository;
	
	@Autowired
	SiacTAttoAmmFinRepository siacTAttoAmmRepository;
	
	@Autowired
	SiacROrdinativoAttoAmmRepository siacROrdinativoAttoAmmRepository;
	
	@Autowired
	SiacTOrdinativoTRepository siacTOrdinativoTRepository;
	
	@Autowired
	SiacDOrdinativoTsDetTipoRepository siacDOrdinativoTsDetTipoRepository;
	
	@Autowired
	SiacTOrdinativoTsDetRepository siacTOrdinativoTsDetRepository;
	
	@Autowired
	SiacTModpagFinRepository siacTModpagRepository;
	
	@Autowired
	SiacRSoggettoRelazFinRepository siacRSoggettoRelazFinRepository;
	
	@Autowired
	SiacROrdinativoModpagRepository siacROrdinativoModpagRepository;

	@Autowired
	SiacTMovgestTsRepository siacTMovgestTsRepository;
	
	@Autowired
	SiacROrdinativoTsMovgestTsRepository SiacROrdinativoTsMovgestTsRepository;
	
	@Autowired
	SiacRLiquidazioneOrdRepository siacRLiquidazioneOrdRepository;
	
	@Autowired
	SiacTBilFinRepository siacTBilRepository;
	
    @Autowired
    OrdinativoDao ordinativoDao;
    
	@Autowired
	CapitoloUscitaGestioneService capitoloUscitaGestioneService;
        
    @Autowired
	SoggettoFinDad soggettoDad;
        
    @Autowired
	ProvvedimentoService provvedimentoService;
        
    @Autowired
	LiquidazioneDad liquidazioneDad;
    
    @Autowired
	ProvvisorioDad provvisorioDad;
    
	@Autowired
	ProvvisorioDiCassaDao provvisorioDiCassaDao;

    @Autowired
	CommonDad commonDad;
    
    @Autowired
   	ImpegnoOttimizzatoDad impegnoOttimizzatoDad;
    
    @Autowired
	AccertamentoOttimizzatoDad accertamentoOttimizzatoDad;
    
    @Autowired
	AccertamentoDad accertamentoDad;
    
    @Autowired
    SiacRSubdocOrdinativoTFinRepository siacRSubdocOrdinativoTFinRepository; 
    
    @Autowired
	SiacRSoggettoClasseRepository siacRSoggettoClasseRepository;
    
    @Autowired
	SiacDSoggettoClasseRepository siacDSoggettoClasseRepository;
    
	/**
	 * richiama il servizio base per i controlli che permettono 
	 * l'ingresso in doppia gestione
	 * 
	 * @param bilancio
	 * @param entita
	 * @param datiOperazioneDto
	 * @return boolean
	 */
    public boolean abilitaDoppiaGestione(Bilancio bilancio, Entita entita, DatiOperazioneDto datiOperazioneDto){
    	
    	return inserireDoppiaGestione(bilancio, entita, datiOperazioneDto);
    }	
	
	/**
	 * si occupa di aggiornare i dati dell'ordinativo
	 * @param ordinativo
	 * @param ordinativoInModificaInfoDto
	 * @param datiOperazione
	 * @param bilancio
	 * @param richiedente
	 * @return
	 * @throws RuntimeException
	 */
	public Ordinativo aggiornaOrdinativo(Ordinativo ordinativo, OrdinativoInModificaInfoDto ordinativoInModificaInfoDto, 
			DatiOperazioneDto datiOperazione, Bilancio bilancio, Richiedente richiedente, Ente ente) throws RuntimeException{
		Ordinativo ordinativoDaRestituire = new Ordinativo();
		
		//individuo il tipo dell'ordinativo
		//ricevuto in input al metodo:
		String dOrdinativoTipo = getDOrdinativoTipo(ordinativo);
		
		SiacTOrdinativoFin siacTOrdinativoModificato = aggiornaSiacTOrdinativo(bilancio, ordinativo, dOrdinativoTipo, ordinativoInModificaInfoDto, datiOperazione,ente);
		
		// SUB ORDINATIVI :
		SubOrdinativoInModificaInfoDto infoModificheSubOrdinativi = ordinativoInModificaInfoDto.getInfoModificheSubOrdinativi();
		
		ArrayList<S> listaSubOrdinativiDaAggiornare = (ArrayList<S>) infoModificheSubOrdinativi.getSubOrdinativiDaModificare();
				
		ArrayList<S> listaSubOrdinativiDaInserire = (ArrayList<S>) infoModificheSubOrdinativi.getSubOrdinativiDaInserire();
		
		ArrayList<SiacTOrdinativoTFin> listaSubOrdinativiDaEliminare = infoModificheSubOrdinativi.getSubOrdinativiDaEliminare();
		
		ArrayList<SiacTOrdinativoTFin> listaSubOrdinativiInvariati = infoModificheSubOrdinativi.getSubOrdinativiInvariati();
		
		int numermoSubOrdinativo = infoModificheSubOrdinativi.getMaxCodeOld();
		
		List<SiacTOrdinativoTFin> listaValidiDopoModifiche = new ArrayList<SiacTOrdinativoTFin>();
		
		if(listaSubOrdinativiDaInserire!=null && listaSubOrdinativiDaInserire.size()>0){
			//INSERIMENTO NUOVI
			for(S subOrdinativo : listaSubOrdinativiDaInserire){
				String codeSubOrdinativo = Integer.toString(numermoSubOrdinativo);
				SiacTOrdinativoTFin siacTOrdinativoTsInsert = inserisciSiacTOrdinativoTs(bilancio, siacTOrdinativoModificato, codeSubOrdinativo, subOrdinativo, datiOperazione,null);
				listaValidiDopoModifiche.add(siacTOrdinativoTsInsert);
				numermoSubOrdinativo++;
			}
		}
		
		if(listaSubOrdinativiDaAggiornare!=null && listaSubOrdinativiDaAggiornare.size()>0){
			//AGGIORNAMENTO MODIFICATI
			for(S subOrdinativo : listaSubOrdinativiDaAggiornare){
				SiacTOrdinativoTFin siacTOrdinativoTsAggiornato = aggiornaSiacTOrdinativoTs(bilancio, siacTOrdinativoModificato, subOrdinativo, dOrdinativoTipo, datiOperazione);
				listaValidiDopoModifiche.add(siacTOrdinativoTsAggiornato);
			}
		}
		
		List<SiacTOrdinativoTFin> listaSubRicostruita =  toList(listaSubOrdinativiInvariati,listaValidiDopoModifiche);
		if(listaSubRicostruita!=null && listaSubRicostruita.size()>1){
			Collections.sort(listaSubRicostruita, new Comparator<SiacTOrdinativoTFin>() {
		        @Override
		        public int compare(SiacTOrdinativoTFin  sub1, SiacTOrdinativoTFin  sub2){
		            return  sub1.getOrdTsCode().compareTo(sub2.getOrdTsCode());
		        }
		    });
		}
		siacTOrdinativoModificato.setSiacTOrdinativoTs(listaSubRicostruita);
		
		if(listaSubOrdinativiDaEliminare!=null && listaSubOrdinativiDaEliminare.size()>0){
			//ELIMINA RIMOSSI
			for(SiacTOrdinativoTFin iterato : listaSubOrdinativiDaEliminare){
				DatiOperazioneUtils.annullaRecord(iterato, siacTOrdinativoTRepository, datiOperazione,siacTAccountRepository);
			}
		}
		
		// REGOLARIZZAZIONI DI CASSA : INIZIO
		RegolarizzazioniDiCassaInModificaInfoDto infoModificheRegolarizzazioniDiCassa = ordinativoInModificaInfoDto.getInfoModificheRegolarizzazioniDiCassa();
		ArrayList<SiacROrdinativoProvCassaFin> listaRegolarizzazioniDiCassaInvariate = infoModificheRegolarizzazioniDiCassa.getRegolarizzazioniDiCassaInvariate();
		ArrayList<RegolarizzazioneProvvisorio> listaRegolarizzazioniDiCassaDaAggiornare = infoModificheRegolarizzazioniDiCassa.getRegolarizzazioniDiCassaDaModificare();
		ArrayList<RegolarizzazioneProvvisorio> listaRegolarizzazioniDiCassaDaInserire = infoModificheRegolarizzazioniDiCassa.getRegolarizzazioniDiCassaDaInserire();
		ArrayList<SiacROrdinativoProvCassaFin> listaRegolarizzazioniDiCassaDaEliminare = infoModificheRegolarizzazioniDiCassa.getRegolarizzazioniDiCassaDaEliminare();

		List<SiacROrdinativoProvCassaFin> listaRegolarizzazioniDiCassaValideDopoUpdate = new ArrayList<SiacROrdinativoProvCassaFin>();
		
		if(listaRegolarizzazioniDiCassaDaInserire!=null && listaRegolarizzazioniDiCassaDaInserire.size()>0){
			//INSERIMENTO NUOVI
			List<SiacROrdinativoProvCassaFin> l = salvaSiacROrdinativoProvCassa(listaRegolarizzazioniDiCassaDaInserire, datiOperazione, siacTOrdinativoModificato, Constanti.D_PROV_CASSA_TIPO_SPESA);
			listaRegolarizzazioniDiCassaValideDopoUpdate = addAll(listaRegolarizzazioniDiCassaValideDopoUpdate,l);
		}

		if(listaRegolarizzazioniDiCassaDaAggiornare!=null && listaRegolarizzazioniDiCassaDaAggiornare.size()>0){
			//AGGIORNAMENTO MODIFICATI
			List<SiacROrdinativoProvCassaFin> l = aggiornaSiacROrdinativoProvCassa(listaRegolarizzazioniDiCassaDaAggiornare, datiOperazione, siacTOrdinativoModificato, Constanti.D_PROV_CASSA_TIPO_SPESA);
			listaRegolarizzazioniDiCassaValideDopoUpdate = addAll(listaRegolarizzazioniDiCassaValideDopoUpdate,l);
		}
		
		List<SiacROrdinativoProvCassaFin> listaOrdCassaRicostruita =  toList(listaRegolarizzazioniDiCassaInvariate,listaRegolarizzazioniDiCassaValideDopoUpdate);
		if(listaOrdCassaRicostruita!=null && listaOrdCassaRicostruita.size()>1){
			Collections.sort(listaOrdCassaRicostruita, new Comparator<SiacROrdinativoProvCassaFin>() {
		        @Override
		        public int compare(SiacROrdinativoProvCassaFin  sub1, SiacROrdinativoProvCassaFin  sub2){
		            return  sub1.getUid().compareTo(sub2.getUid());
		        }
		    });
		}
		siacTOrdinativoModificato.setSiacROrdinativoProvCassas(listaOrdCassaRicostruita);

		if(listaRegolarizzazioniDiCassaDaEliminare!=null && listaRegolarizzazioniDiCassaDaEliminare.size()>0){
			//ELIMINA RIMOSSI
			for(SiacROrdinativoProvCassaFin iterato : listaRegolarizzazioniDiCassaDaEliminare){
				DatiOperazioneUtils.cancellaRecord(iterato, siacROrdinativoProvCassaRepository, datiOperazione, siacTAccountRepository);
			}
		}
		// REGOLARIZZAZIONI DI CASSA : FINE
		
		// Inserisco eventuali ordinativi collegati
		
		//leggo prima i collegati presenti su db
		List<SiacROrdinativoFin> siacROrdinativoListTutti = siacTOrdinativoModificato.getSiacROrdinativos1();
		
		//DEVO ESCLUDERE DALLA VALUTAZIONE QUELLI GIA' ELIMINATI:
		List<SiacROrdinativoFin> siacROrdinativoList = CommonUtils.soloValidiSiacTBase(siacROrdinativoListTutti, getNow());
		//
		
		// prima condizione controllo se non arrivano ordinativi da inserire mentre sul db ne ho, quinid devo cancellare tutto!
		boolean cancellaTuttiICollegati = StringUtils.isEmpty(ordinativo.getElencoOrdinativiCollegati()) &&  !StringUtils.isEmpty(siacROrdinativoList);
		
		boolean inserisciTuttiICollegati =  !StringUtils.isEmpty(ordinativo.getElencoOrdinativiCollegati()) && StringUtils.isEmpty(siacROrdinativoList);
		
		//System.out.println("inserisciTuttiICollegati: " + inserisciTuttiICollegati);
		//System.out.println("cancellaTuttiICollegati: " + cancellaTuttiICollegati);
		
		if(ordinativo.getElencoOrdinativiCollegati()!=null && !ordinativo.getElencoOrdinativiCollegati().isEmpty()){
			
			List<Ordinativo> listaCollegatiDaSalvare = new ArrayList<Ordinativo>();
			
			if(siacROrdinativoList!=null && !siacROrdinativoList.isEmpty()){
				
				for (Ordinativo oincasso : ordinativo.getElencoOrdinativiCollegati()) {
					
					boolean esiste = false;
					
					for (SiacROrdinativoFin siacROrdinativo : siacROrdinativoList) {
						
						SiacTOrdinativoFin siacTOrdinativoFin = siacROrdinativo.getSiacTOrdinativo2();
					
						if(new BigDecimal(oincasso.getNumero().intValue()).equals(siacTOrdinativoFin.getOrdNumero())){
							esiste = true;
							break;
						}else{
							esiste = false;
							
						}
					}
					
					if(!esiste){
						listaCollegatiDaSalvare.add(oincasso);
					}
				}
				
				if(!listaCollegatiDaSalvare.isEmpty()){
					salvaRelazioneOrdinativiCollegati(datiOperazione, siacTOrdinativoModificato, null, listaCollegatiDaSalvare);
				}
				
				for (SiacROrdinativoFin siacROrdinativo : siacROrdinativoList) {
					
					boolean esiste = false;
					
					for (Ordinativo oincasso : ordinativo.getElencoOrdinativiCollegati()) {
						
						SiacTOrdinativoFin siacTOrdinativoFin = siacROrdinativo.getSiacTOrdinativo2();
					
						if(siacTOrdinativoFin.getOrdNumero().equals(new BigDecimal(oincasso.getNumero().intValue()))){
							esiste = true;
							break;
						}else{
							esiste = false;
							
						}
					}
					
					if(!esiste){
						DatiOperazioneUtils.cancellaRecord(siacROrdinativo, siacROrdinativoRepository, datiOperazione, siacTAccountRepository);
					}
				}
			}else{
				
				if(inserisciTuttiICollegati){
					salvaRelazioneOrdinativiCollegati(datiOperazione, siacTOrdinativoModificato, null, ordinativo.getElencoOrdinativiCollegati());
				}
			}
			
		}else{
			if(cancellaTuttiICollegati){
				
				for (SiacROrdinativoFin siacROrdinativo : siacTOrdinativoModificato.getSiacROrdinativos1()) {
					DatiOperazioneUtils.cancellaRecord(siacROrdinativo, siacROrdinativoRepository, datiOperazione, siacTAccountRepository);
				}
				
				ordinativo.setElencoOrdinativiCollegati(new ArrayList<Ordinativo>());
			}
		}
		
			
		
		// POPOLAMENTO DEL MODEL DA RESTITUIRE IN OUTPUT:
		AttributoTClassInfoDto attributoInfo = new AttributoTClassInfoDto();
		attributoInfo.setTipoOggetto(OggettoDellAttributoTClass.T_ORDINATIVO);
		attributoInfo.setSiacTOrdinativo(siacTOrdinativoModificato);

		// Restituisco l'ordinativo inserito
		if(ordinativo instanceof OrdinativoPagamento){
			ordinativoDaRestituire = componiOrdinativoModel(attributoInfo, datiOperazione, richiedente, bilancio.getAnno(),Constanti.D_ORDINATIVO_TIPO_PAGAMENTO);
				
			if(ordinativo.getElencoOrdinativiCollegati()!=null){				
				ordinativoDaRestituire.setElencoOrdinativiCollegati(ordinativo.getElencoOrdinativiCollegati());
			}
			
		} else if(ordinativo instanceof OrdinativoIncasso){
			ordinativoDaRestituire = componiOrdinativoModel(attributoInfo, datiOperazione, richiedente, bilancio.getAnno(),Constanti.D_ORDINATIVO_TIPO_INCASSO);
		
			//DICEMBRE 2017 ANCHE PER INCASSO ABBIAMO GLI ORDINATIVI COLLEGATI:
			if(ordinativo.getElencoOrdinativiCollegati()!=null){				
				ordinativoDaRestituire.setElencoOrdinativiCollegati(ordinativo.getElencoOrdinativiCollegati());
			}
			
		}else{
			ordinativoDaRestituire = null;
		}

		return ordinativoDaRestituire;		
	}
	
	/**
	 * semplice metodo di appoggio per leggere la data scadenza
	 * @param subOrdinativo
	 * @return
	 */
	private Date getDataScadenza(SubOrdinativo subOrdinativo){
		Date dataScadenza = null;
		if(subOrdinativo instanceof SubOrdinativoIncasso ){
			dataScadenza = ((SubOrdinativoIncasso) subOrdinativo).getDataScadenza();
		}  else if(subOrdinativo instanceof SubOrdinativoPagamento){
			
			dataScadenza = ((SubOrdinativoPagamento) subOrdinativo).getDataEsecuzionePagamento();
		}
		return dataScadenza;
	}
	
	/**
	 * semplice metodo di appoggio per testare se l'ordinativo/subordinativo e' di incasso o pagamento
	 * @param ordinativo
	 * @return
	 */
	private <O extends Object> boolean isPagamento(O o){
		if(o instanceof SubOrdinativoIncasso || o instanceof OrdinativoIncasso){
			return false;
		} else if(o instanceof SubOrdinativoPagamento || o instanceof OrdinativoPagamento){
			return true;
		}
		return true;//non deve succedere
	}
	
	/**
	 * semplice metodo di appoggio per testare se l'ordinativo e' di incasso o pagamento
	 * @param subOrdinativo
	 * @return
	 */
	private <O extends Object> String getDOrdinativoTipo(O o){
		String tipoOrdinativo = null;
		if(o instanceof SubOrdinativoIncasso || o instanceof OrdinativoIncasso){
			tipoOrdinativo = Constanti.D_ORDINATIVO_TIPO_INCASSO;
		} else if(o instanceof SubOrdinativoPagamento || o instanceof OrdinativoPagamento){
			tipoOrdinativo = Constanti.D_ORDINATIVO_TIPO_PAGAMENTO;
		}
		return tipoOrdinativo;
	}
	
	/**
	 *  semplice metodo di appoggio per ricavare il tipo di provvisiorio di cassa
	 * @param ordinativo
	 * @return
	 */
	private String getTipoProvvisorioCassa(Ordinativo ordinativo){
		String tipoProvvisorioDiCassa =  null;
		if(ordinativo instanceof OrdinativoPagamento){
			tipoProvvisorioDiCassa = Constanti.D_PROV_CASSA_TIPO_SPESA;
		} else if(ordinativo instanceof OrdinativoIncasso){
			tipoProvvisorioDiCassa = Constanti.D_PROV_CASSA_TIPO_ENTRATA;
		}
		return tipoProvvisorioDiCassa;
	}

	/**
	 * Si occupa dell'inserimento nel sistema di un nuovo ordinativo
	 * @param ordinativo
	 * @param datiInsInfo
	 * @param datiOperazioneDto
	 * @return
	 * @throws RuntimeException
	 */
	@SuppressWarnings("unchecked")
	public Ordinativo inserisciOrdinativo(Ordinativo ordinativo, OrdinativoInInserimentoInfoDto datiInsInfo,DatiOperazioneDto datiOperazioneDto) throws RuntimeException {
		
		Ordinativo ordinativoDaRestituire = null;

		//testiamo come prima cosa che tipo di ordinativo dobbiamo trattare:
		boolean diPagamento = isPagamento(ordinativo);
		
		Bilancio bilancio = datiInsInfo.getBilancio();
		
		OrdinativoPagamento ordinativoPagamento = null;
		OrdinativoIncasso ordinativoIncasso = null;
		String D_ORDINAMENTO_TIPO = getDOrdinativoTipo(ordinativo);
		if(diPagamento){
			//Lo istanziamo di tipologia pagamento
			ordinativoPagamento = (OrdinativoPagamento) ordinativo;
		}else {
			//..oppure incasso
			ordinativoIncasso = (OrdinativoIncasso) ordinativo;
		}

		SiacTOrdinativoFin siacTOrdinativoInsert = inserisciSiacTOrdinativo(bilancio, ordinativo,D_ORDINAMENTO_TIPO, datiOperazioneDto, false);
		
		//GESTIONE DEI SUBORDINATIVI:
		List<S> elencoSubOrdinativi = null;
		List<SiacTOrdinativoTFin> listaSiacTOrdinativoTFinInseriti = new ArrayList<SiacTOrdinativoTFin>();
		if(diPagamento){
			elencoSubOrdinativi = (List<S>) ordinativoPagamento.getElencoSubOrdinativiDiPagamento();
		} else {
			elencoSubOrdinativi = (List<S>) ordinativoIncasso.getElencoSubOrdinativiDiIncasso();
		}
		if(null!=elencoSubOrdinativi && elencoSubOrdinativi.size() > 0){
			int numermoSubOrdinativo = 1;
			for(S subOrdinativoPagamento : elencoSubOrdinativi){
				String codeSubOrdinativo = Integer.toString(numermoSubOrdinativo);
				SiacTOrdinativoTFin siacTOrdinativoTsInsert = inserisciSiacTOrdinativoTs(bilancio, siacTOrdinativoInsert, codeSubOrdinativo, subOrdinativoPagamento, datiOperazioneDto,datiInsInfo);
				numermoSubOrdinativo++;
				
				listaSiacTOrdinativoTFinInseriti.add(siacTOrdinativoTsInsert);
				
			}
		}

		// Eventuale regolarizzazione di provvisori
		List<RegolarizzazioneProvvisorio> elencoRegolarizzazioneProvvisori = ordinativo.getElencoRegolarizzazioneProvvisori();
		String tipoProvvisorioDiCassa = getTipoProvvisorioCassa(ordinativo);
		salvaSiacROrdinativoProvCassa(elencoRegolarizzazioneProvvisori, datiOperazioneDto, siacTOrdinativoInsert, tipoProvvisorioDiCassa);
		
		//ORDINATIVI COLLEGATI:
		if(ordinativo.getElencoOrdinativiCollegati()!=null && !ordinativo.getElencoOrdinativiCollegati().isEmpty()){
			salvaRelazioneOrdinativiCollegati(datiOperazioneDto, siacTOrdinativoInsert, null, ordinativo.getElencoOrdinativiCollegati());
		}
		//
		

		// Restituisco l'ordinativo inserito
		if(ordinativo instanceof OrdinativoPagamento){
			OrdinativoPagamento ordinativoPagamentoDaRestituire = 
					map(siacTOrdinativoInsert, OrdinativoPagamento.class, FinMapId.SiacTOrdinativo_OrdinativoPagamento);
			ordinativoDaRestituire = ordinativoPagamentoDaRestituire;
		} else if(ordinativo instanceof OrdinativoIncasso){
				
			OrdinativoIncasso ordinativoIncassoDaRestituire = 
					map(siacTOrdinativoInsert, OrdinativoIncasso.class, FinMapId.SiacTOrdinativo_OrdinativoIncasso);
			ordinativoDaRestituire =  ordinativoIncassoDaRestituire;
	
		}else{
			ordinativoDaRestituire = null;
		}
			
		return ordinativoDaRestituire;		
	}
	
	public List<Errore> controlliSiopePlusPerOrdPag(OrdinativoPagamento ordPag, DatiOperazioneDto datiOperazione){
		
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		boolean daImpegno = false;
		
		//CONTROLLI SIOPE PLUS GENERICI:
		List<Errore> errSiopePlus = controlliSiopePlus(ordPag.getSiopeTipoDebito(), ordPag.getSiopeAssenzaMotivazione(), ordPag.getCig(), datiOperazione,daImpegno);
		if(!StringUtils.isEmpty(errSiopePlus)){
			return errSiopePlus;
		}
		//
		
		return listaErrori;
	}
	
	/**
	 * Si occupa dell'inserimento nel sistema di un nuovo ordinativo
	 * @param ordinativo
	 * @param datiInsInfo
	 * @param datiOperazioneDto
	 * @return
	 * @throws RuntimeException
	 */
	@SuppressWarnings("unchecked")
	public OrdinativoPagamento inserisciOrdinativoPagamento(Ordinativo ordinativo, OrdinativoInInserimentoInfoDto datiInsInfo,DatiOperazioneDto datiOperazioneDto, boolean usaDatiLoginOrig) throws RuntimeException {
		
		List<SubOrdinativoPagamento> elencoSubOrdinativoPagamentoInseriti = new ArrayList<SubOrdinativoPagamento>();
		Bilancio bilancio = datiInsInfo.getBilancio();
		
		OrdinativoPagamento ordinativoPagamento = (OrdinativoPagamento) ordinativo;
				
		SiacTOrdinativoFin siacTOrdinativoInsert = inserisciSiacTOrdinativo(bilancio, ordinativo,Constanti.D_ORDINATIVO_TIPO_PAGAMENTO, datiOperazioneDto, usaDatiLoginOrig);
		
		//GESTIONE DEI SUBORDINATIVI:
		List<S> elencoSubOrdinativi = null;
		List<SiacTOrdinativoTFin> listaSiacTOrdinativoTFinInseriti = new ArrayList<SiacTOrdinativoTFin>();
		elencoSubOrdinativi = (List<S>) ordinativoPagamento.getElencoSubOrdinativiDiPagamento();
		
		if(null!=elencoSubOrdinativi && elencoSubOrdinativi.size() > 0){
			int numermoSubOrdinativo = 1;
			for(S subOrdinativoPagamento : elencoSubOrdinativi){
				String codeSubOrdinativo = Integer.toString(numermoSubOrdinativo);
			
				SiacTOrdinativoTFin siacTOrdinativoTsInsert = inserisciSiacTOrdinativoTs(bilancio, siacTOrdinativoInsert, codeSubOrdinativo, subOrdinativoPagamento, datiOperazioneDto,datiInsInfo);
				numermoSubOrdinativo++;
				
				listaSiacTOrdinativoTFinInseriti.add(siacTOrdinativoTsInsert);
			
			}
		}

		// Eventuale regolarizzazione di provvisori
		List<RegolarizzazioneProvvisorio> elencoRegolarizzazioneProvvisori = ordinativo.getElencoRegolarizzazioneProvvisori();
		String tipoProvvisorioDiCassa = getTipoProvvisorioCassa(ordinativo);
		salvaSiacROrdinativoProvCassa(elencoRegolarizzazioneProvvisori, datiOperazioneDto, siacTOrdinativoInsert, tipoProvvisorioDiCassa);

		
		// Inserisco eventuali ordinativi collegati
		if(ordinativo.getElencoOrdinativiCollegati()!=null && !ordinativo.getElencoOrdinativiCollegati().isEmpty()){
			salvaRelazioneOrdinativiCollegati(datiOperazioneDto, siacTOrdinativoInsert, datiInsInfo, ordinativo.getElencoOrdinativiCollegati());
		}

		// RM: problema x l'emettitore --> mappo prima i sub che poi setto sull'ordinativo, in modo che l'oggetto restituito sia completo
		if(listaSiacTOrdinativoTFinInseriti!=null && listaSiacTOrdinativoTFinInseriti.size()> 0){
			elencoSubOrdinativoPagamentoInseriti = convertiLista(listaSiacTOrdinativoTFinInseriti, SubOrdinativoPagamento.class, FinMapId.SiacTOrdinativoT_SubOrdinativoPagamento);
		}
						
		ordinativoPagamento = map(siacTOrdinativoInsert, OrdinativoPagamento.class, FinMapId.SiacTOrdinativo_OrdinativoPagamento);
		ordinativoPagamento.setElencoSubOrdinativiDiPagamento(elencoSubOrdinativoPagamentoInseriti);
		
		
		ClassificatoreStipendi cs = ordinativo.getClassificatoreStipendi();
		
		if (cs != null && cs.getUid() > 0) {
			relOrdinativoClassificatore(siacTOrdinativoInsert.getOrdId(), cs.getUid(), datiOperazioneDto);
		}
		
		return ordinativoPagamento;			
	}
	
	
	/**
	 * Si occupa dell'inserimento nel sistema di un nuovo ordinativo
	 * @param ordinativo
	 * @param datiInsInfo
	 * @param datiOperazioneDto
	 * @return
	 * @throws RuntimeException
	 */
	@SuppressWarnings("unchecked")
	public OrdinativoIncasso inserisciOrdinativoIncasso(Ordinativo ordinativo, OrdinativoInInserimentoInfoDto datiInsInfo,DatiOperazioneDto datiOperazioneDto) throws RuntimeException {
		
		
		List<SubOrdinativoIncasso> elencoSubOrdinativoIncassoInseriti = new ArrayList<SubOrdinativoIncasso>();
		
		Bilancio bilancio = datiInsInfo.getBilancio();
		
		OrdinativoIncasso ordinativoIncasso = (OrdinativoIncasso) ordinativo;
		
		SiacTOrdinativoFin siacTOrdinativoInsert = inserisciSiacTOrdinativo(bilancio, ordinativo,Constanti.D_ORDINATIVO_TIPO_INCASSO, datiOperazioneDto, false);
		
		//GESTIONE DEI SUBORDINATIVI:
		List<S> elencoSubOrdinativi = null;
		List<SiacTOrdinativoTFin> listaSiacTOrdinativoTFinInseriti = new ArrayList<SiacTOrdinativoTFin>();
		
		elencoSubOrdinativi = (List<S>) ordinativoIncasso.getElencoSubOrdinativiDiIncasso();
		
		if(null!=elencoSubOrdinativi && elencoSubOrdinativi.size() > 0){
			int numermoSubOrdinativo = 1;
			
			for(S subOrdinativoPagamento : elencoSubOrdinativi){
				String codeSubOrdinativo = Integer.toString(numermoSubOrdinativo);
				SiacTOrdinativoTFin siacTOrdinativoTsInsert = inserisciSiacTOrdinativoTs(bilancio, siacTOrdinativoInsert, codeSubOrdinativo, subOrdinativoPagamento, datiOperazioneDto,datiInsInfo);
				numermoSubOrdinativo++;
				
				listaSiacTOrdinativoTFinInseriti.add(siacTOrdinativoTsInsert);
				
			}
		}

		// Eventuale regolarizzazione di provvisori
		List<RegolarizzazioneProvvisorio> elencoRegolarizzazioneProvvisori = ordinativo.getElencoRegolarizzazioneProvvisori();
		String tipoProvvisorioDiCassa = getTipoProvvisorioCassa(ordinativo);
		salvaSiacROrdinativoProvCassa(elencoRegolarizzazioneProvvisori, datiOperazioneDto, siacTOrdinativoInsert, tipoProvvisorioDiCassa);

		// Inserisco eventuali ordinativi collegati
		// inserisco i collegati
		// FIXME: per ora l'operazione dei collegati è void, verificare se servono gli id inseriti
		if(ordinativo.getElencoOrdinativiCollegati()!=null && !ordinativo.getElencoOrdinativiCollegati().isEmpty()){
			salvaRelazioneOrdinativiCollegati(datiOperazioneDto, siacTOrdinativoInsert, datiInsInfo, ordinativo.getElencoOrdinativiCollegati());
		}

		
		//mappo prima i sub che poi incollo all'ordinativo, in modo che l'oggetto restituito sia completo
		if(listaSiacTOrdinativoTFinInseriti!=null && listaSiacTOrdinativoTFinInseriti.size()> 0){
			elencoSubOrdinativoIncassoInseriti = convertiLista(listaSiacTOrdinativoTFinInseriti, SubOrdinativoIncasso.class, FinMapId.SiacTOrdinativoT_SubOrdinativoIncasso);
		}
				
		OrdinativoIncasso ordinativoIncassoDaRestituire = 
					map(siacTOrdinativoInsert, OrdinativoIncasso.class, FinMapId.SiacTOrdinativo_OrdinativoIncasso);
		
		ordinativoIncassoDaRestituire.setElencoSubOrdinativiDiIncasso(elencoSubOrdinativoIncassoInseriti);
		
		
		ClassificatoreStipendi cs = ordinativo.getClassificatoreStipendi();
		
		if (cs != null && cs.getUid() > 0) {
			relOrdinativoClassificatore(siacTOrdinativoInsert.getOrdId(), cs.getUid(), datiOperazioneDto);
		}
		
		return ordinativoIncassoDaRestituire;		
	}

	private SiacROrdinativoClassFin relOrdinativoClassificatore(Integer idOrdinativo, Integer idClassificatore, DatiOperazioneDto datiOperazioneDto) {

		String loginOperazione = DatiOperazioneUtils.determinaUtenteLogin(datiOperazioneDto, siacTAccountRepository);
		
		siacROrdinativoClassRepository.removeValidSiacROrdinativoClass(
				idOrdinativo, 
				SiacDClassTipoEnum.ClassificatoreStipendi.getCodice(), 
				loginOperazione);
		
		if(idClassificatore != null && idClassificatore.intValue() != 0) {
			SiacROrdinativoClassFin siacROrdinativoClass = new SiacROrdinativoClassFin();
			
			Date data = new Date(datiOperazioneDto.getCurrMillisec());
			
			siacROrdinativoClass.setDataModifica(data );
			siacROrdinativoClass.setLoginOperazione(loginOperazione);
			siacROrdinativoClass.setDataCreazione(data);
			siacROrdinativoClass.setDataInizioValidita(data);
			siacROrdinativoClass.setDataModifica(data);
			siacROrdinativoClass.setSiacTEnteProprietario(datiOperazioneDto.getSiacTEnteProprietario());
			siacROrdinativoClass.setSiacTOrdinativo(new SiacTOrdinativoFin());
			siacROrdinativoClass.getSiacTOrdinativo().setUid(idOrdinativo);
			SiacTClassFin siacTClassFin = siacTClassRepository.findOne(idClassificatore);
			siacROrdinativoClass.setSiacTClass(siacTClassFin);
			siacROrdinativoClassRepository.saveAndFlush(siacROrdinativoClass);
			return siacROrdinativoClass;
		}
		return null;
	}
	
	

	/**
	 * Si occupa di salvare i provvisori di cassa per un ordinativo
	 * @param elencoRegolarizzazioneProvvisori
	 * @param datiOperazioneDto
	 * @param siacTOrdinativo
	 * @param tipoProvvisorioDiCassa
	 * @return
	 * @throws RuntimeException
	 */
	private List<SiacROrdinativoProvCassaFin> salvaSiacROrdinativoProvCassa(List<RegolarizzazioneProvvisorio> elencoRegolarizzazioneProvvisori, DatiOperazioneDto datiOperazioneDto, SiacTOrdinativoFin siacTOrdinativo, String tipoProvvisorioDiCassa) throws RuntimeException {

		List<SiacROrdinativoProvCassaFin> elencoSiacROrdinativoProvCassa = new ArrayList<SiacROrdinativoProvCassaFin>();
		String methodName= "salvaSiacROrdinativoProvCassa";
			
		if(null!=elencoRegolarizzazioneProvvisori && elencoRegolarizzazioneProvvisori.size() > 0){
			for(RegolarizzazioneProvvisorio regolarizzazioneProvvisorio : elencoRegolarizzazioneProvvisori){

				ProvvisorioDiCassa provvisorioDiCassa = regolarizzazioneProvvisorio.getProvvisorioDiCassa();
				if(null!=provvisorioDiCassa && null!=provvisorioDiCassa.getAnno() && null!=provvisorioDiCassa.getNumero()){

					SiacROrdinativoProvCassaFin siacROrdinativoProvCassa = new SiacROrdinativoProvCassaFin();
					siacROrdinativoProvCassa = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacROrdinativoProvCassa, datiOperazioneDto,siacTAccountRepository);
					siacROrdinativoProvCassa.setSiacTOrdinativo(siacTOrdinativo);
	
					SiacTProvCassaFin siacTProvCassa = new SiacTProvCassaFin();
					
					siacTProvCassa = siacTProvCassaRepository.findOne(provvisorioDiCassa.getUid());

					siacROrdinativoProvCassa.setSiacTProvCassa(siacTProvCassa);
	
					// siacROrdinativoProvCassa.setOrdProvcImporto(provvisorioDiCassa.getImportoDaEmettere());
					siacROrdinativoProvCassa.setOrdProvcImporto(regolarizzazioneProvvisorio.getImporto());
					//salvo sul db:
					siacROrdinativoProvCassa = siacROrdinativoProvCassaRepository.saveAndFlush(siacROrdinativoProvCassa);

					BigDecimal  importoDaRegolarizzare = BigDecimal.ZERO;
					importoDaRegolarizzare =  provvisorioDiCassaDao.calcolaImportoDaRegolarizzare(siacTProvCassa.getUid());
						
					log.debug(methodName,"totale importo provvisorio: " + importoDaRegolarizzare);
					//System.out.println(" totale importo provvisorio: " + importoDaRegolarizzare);
				
					if(importoDaRegolarizzare.compareTo(BigDecimal.ZERO) == 0){
						siacTProvCassa.setProvcDataRegolarizzazione(datiOperazioneDto.getTs());
						siacTProvCassa = siacTProvCassaRepository.saveAndFlush(siacTProvCassa);
					}

					elencoSiacROrdinativoProvCassa.add(siacROrdinativoProvCassa);
				}
			}
		}
		

		return elencoSiacROrdinativoProvCassa;
	}

	/**
	 * si occupa di aggiornare i provvisori di cassa per un ordinativo
	 * @param elencoRegolarizzazioneProvvisori
	 * @param datiOperazioneDto
	 * @param siacTOrdinativo
	 * @param tipoProvvisorioDiCassa
	 * @return
	 * @throws RuntimeException
	 */
	private List<SiacROrdinativoProvCassaFin> aggiornaSiacROrdinativoProvCassa(List<RegolarizzazioneProvvisorio> elencoRegolarizzazioneProvvisori, DatiOperazioneDto datiOperazioneDto, SiacTOrdinativoFin siacTOrdinativo, String tipoProvvisorioDiCassa) throws RuntimeException {

		List<SiacROrdinativoProvCassaFin> elencoSiacROrdinativoProvCassa = new ArrayList<SiacROrdinativoProvCassaFin>();
		
		if(null!=elencoRegolarizzazioneProvvisori && elencoRegolarizzazioneProvvisori.size() > 0){
			for(RegolarizzazioneProvvisorio regolarizzazioneProvvisorio : elencoRegolarizzazioneProvvisori){

				SiacROrdinativoProvCassaFin siacROrdinativoProvCassa = siacROrdinativoProvCassaRepository.findSiacROrdinativoProvCassaValidoById(datiOperazioneDto.getSiacTEnteProprietario().getUid(),
																																			  regolarizzazioneProvvisorio.getIdRegolarizzazioneProvvisorio(),
						                                                                                                                      datiOperazioneDto.getTs());
				
				ProvvisorioDiCassa provvisorioDiCassa = regolarizzazioneProvvisorio.getProvvisorioDiCassa();
				if(null!=provvisorioDiCassa && null!=provvisorioDiCassa.getAnno() && null!=provvisorioDiCassa.getNumero()){

					siacROrdinativoProvCassa = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacROrdinativoProvCassa, datiOperazioneDto,siacTAccountRepository);
					siacROrdinativoProvCassa.setSiacTOrdinativo(siacTOrdinativo);
	
					SiacTProvCassaFin siacTProvCassa = new SiacTProvCassaFin();
					
					siacTProvCassa = siacTProvCassaRepository.findOne(provvisorioDiCassa.getUid());
					// 17/06/2014 : Commento questa chiamata, per adesso continuo ad estrarre per id il provvisorio passato in input
					// Faccio questo perche', ad oggi, sulla tabella siac_for2.siac_t_prov_cassa non e' possibile estrarre
					// univocamente un record passando come parametri di input annoProvvisorio e numeroProvvisorio
					// siacTProvCassa = siacTProvCassaRepository.findProvvisorioDiCassaValidoByAnnoNumero(datiOperazioneDto.getSiacTEnteProprietario().getUid(),
					// 				                                                                   provvisorioDiCassa.getAnno(),
					//				                                                                   BigDecimal.valueOf(provvisorioDiCassa.getNumero()),
					//		                                                                           datiOperazioneDto.getTs(),
					//		                                                                           tipoProvvisorioDiCassa);

					siacROrdinativoProvCassa.setSiacTProvCassa(siacTProvCassa);
	
					// siacROrdinativoProvCassa.setOrdProvcImporto(provvisorioDiCassa.getImportoDaEmettere());
					siacROrdinativoProvCassa.setOrdProvcImporto(regolarizzazioneProvvisorio.getImporto());
					//salvo sul db:
					siacROrdinativoProvCassa = siacROrdinativoProvCassaRepository.saveAndFlush(siacROrdinativoProvCassa);
					
					RicercaProvvisorioDiCassaK ricercaProvvisorioDiCassaK = new RicercaProvvisorioDiCassaK();
					ricercaProvvisorioDiCassaK.setAnnoProvvisorioDiCassa(provvisorioDiCassa.getAnno());
					ricercaProvvisorioDiCassaK.setNumeroProvvisorioDiCassa(provvisorioDiCassa.getNumero());

					// 17/06/2014 : Commento questa chiamata
					// Faccio questo perche', ad oggi, sulla tabella siac_for2.siac_t_prov_cassa non e' possibile estrarre
					// univocamente un record passando come parametri di input annoProvvisorio e numeroProvvisorio
					// 
					// ProvvisorioDiCassa provvisorioDiCassaEstratto = provvisorioDad.ricercaProvvisorioDiCassaPerChiave(datiOperazioneDto.getSiacTEnteProprietario().getUid(),
					//		                                                                                          ricercaProvvisorioDiCassaK,
					//		                                                                                          datiOperazioneDto.getTs(),
					//		                                                                                          tipoProvvisorioDiCassa);

					// if(null!=provvisorioDiCassaEstratto && null!=provvisorioDiCassaEstratto.getImportoDaEmettere() &&
					//   provvisorioDiCassaEstratto.getImportoDaEmettere().compareTo(BigDecimal.ZERO) == 0 &&
					//   null==provvisorioDiCassaEstratto.getDataRegolarizzazione()){
					//	 // siacTProvCassa.setProvcDataRegolarizzazione(datiOperazioneDto.getTs());
					// } else if (null!=provvisorioDiCassaEstratto && null!=provvisorioDiCassaEstratto.getImportoDaEmettere() &&
					//		   provvisorioDiCassaEstratto.getImportoDaEmettere().compareTo(BigDecimal.ZERO) > 0 &&
					//		   null!=provvisorioDiCassaEstratto.getDataRegolarizzazione()){
					//	// siacTProvCassa.setProvcDataRegolarizzazione(null);
					// }

					siacTProvCassa.setProvcDataRegolarizzazione(datiOperazioneDto.getTs());
					//salvo sul db:
					siacTProvCassa = siacTProvCassaRepository.saveAndFlush(siacTProvCassa);

					elencoSiacROrdinativoProvCassa.add(siacROrdinativoProvCassa);
				}
			}
		}
		

		return elencoSiacROrdinativoProvCassa;
	}
	
	/**
	 * si occupa dell'inserimento in SiacTOrdinativoFin e tabelle accessorie
	 * @param bilancio
	 * @param ordinativo
	 * @param tipoOrdinativo
	 * @param datiOperazioneDto
	 * @param usaDatiLoginOrig 
	 * @return
	 * @throws RuntimeException
	 */
	private SiacTOrdinativoFin inserisciSiacTOrdinativo(Bilancio bilancio, Ordinativo ordinativo, String tipoOrdinativo,DatiOperazioneDto datiOperazioneDto, boolean usaDatiLoginOrig) throws RuntimeException {

		SiacTOrdinativoFin siacTOrdinativoInsert = new SiacTOrdinativoFin();
		
		//testiamo come prima cosa che tipo di ordinativo dobbiamo trattare:
		boolean diPagamento = isPagamento(ordinativo);
		
		datiOperazioneDto.setOperazione(Operazione.INSERIMENTO);
		
		ProgressivoType progressivoType = null;
		OrdinativoPagamento ordinativoDiPagamento = null;
		OrdinativoIncasso ordinativoIncasso = null;
		if(diPagamento){
			 progressivoType = ProgressivoType.ORDINATIVO_DI_PAGAMENTO;
			 ordinativoDiPagamento = (OrdinativoPagamento) ordinativo;
		}else {
			progressivoType = ProgressivoType.ORDINATIVO_DI_INCASSO;
			ordinativoIncasso = (OrdinativoIncasso) ordinativo;
		}
		
		AttoAmministrativo attoAmministrativo = ordinativo.getAttoAmministrativo();

		String loginOperazione = datiOperazioneDto.getAccountCode();
		
		int idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository.findOne(idEnte);
		
		SiacDAmbitoFin  siacDAmbito = siacDAmbitoRepository.findAmbitoByCode(Constanti.AMBITO_FIN, idEnte);
		Integer idAmbito = siacDAmbito.getAmbitoId();
		datiOperazioneDto.setSiacDAmbito(siacDAmbito);

		SiacTBilFin siacTBilOrdinativo = new SiacTBilFin();
		List<SiacTBilFin> siacTBilList = siacTBilRepository.getValidoByAnno(idEnte, Integer.toString(bilancio.getAnno()), datiOperazioneDto.getTs());
		if(siacTBilList!=null && siacTBilList.size()>0 && siacTBilList.get(0)!=null){
			siacTBilOrdinativo = siacTBilList.get(0);
		}
		
		//CAUSALE:
		if(!diPagamento){
			//per ordinativo di incasso puo' essere indicata una causale di entrata
			siacTOrdinativoInsert = impostaCausaleOrdinativoIncasso(ordinativoIncasso, datiOperazioneDto, siacTOrdinativoInsert);
		}

		//ANNO
		Integer annoOrdinativo = ordinativo.getAnno();
		if(null==annoOrdinativo || (null!=annoOrdinativo && annoOrdinativo == 0)){
			// Se anno_ordinativo non istanziato o anno_ordinativo = 0, setto anno_ordinativo = anno_esercizio
			annoOrdinativo = bilancio.getAnno();
			ordinativo.setAnno(annoOrdinativo);
		}
		
		long nuovoCode = getMaxCode(progressivoType, idEnte, idAmbito, loginOperazione, annoOrdinativo.intValue());
		
		siacTOrdinativoInsert.setOrdAnno(annoOrdinativo);
		siacTOrdinativoInsert.setOrdNumero(new BigDecimal(nuovoCode));
		siacTOrdinativoInsert.setOrdDesc(ordinativo.getDescrizione());

		// siac_t_ordinativo.ord_emissione_data : inizio
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		if(calendar.get(Calendar.YEAR) == bilancio.getAnno()){
			// Se anno di sistema uguale ad anno esericizio
			// la data di emissione va valorizzata con la data di sistema
			siacTOrdinativoInsert.setOrdEmissioneData(datiOperazioneDto.getTs());
		} else if(calendar.get(Calendar.YEAR) > bilancio.getAnno()){
			// Se anno di sistema maggiore di anno esericizio
			// la data di emissione va valorizzata con la data 31/12/anno_di_esercizio
			Calendar cal = GregorianCalendar.getInstance();
			cal.set(Calendar.DAY_OF_MONTH, 31);
			cal.set(Calendar.MONTH, 11);// -1 as month is zero-based
			cal.set(Calendar.YEAR, bilancio.getAnno());
			Timestamp tstamp = new Timestamp(cal.getTimeInMillis());
			siacTOrdinativoInsert.setOrdEmissioneData(tstamp);
		}
		// siac_t_ordinativo.ord_emissione_data : fine
		
		/** 
		 * impostaCodificheDOrdinativo:
		 * 
		 * siac_d_ordinativo_tipo
		 * siac_d_codicebollo
		 * siac_d_commissione_tipo
		 * siac_d_contotesoreria
		 * siac_d_note_tesoriere
		 * siac_d_distinta
		 */
		siacTOrdinativoInsert = impostaCodificheDOrdinativo(siacTOrdinativoInsert, ordinativo, tipoOrdinativo, datiOperazioneDto,true);
		
		// siac_t_ordinativo.ord_cast_cassa + siac_t_ordinativo.ord_cast_competenza + siac_t_ordinativo.ord_cast_emessi : inizio
		CapitoloUscitaGestione capitoloUscitaGestione = null;
		CapitoloEntrataGestione capitoloEntrataGestione = null;
		if(diPagamento){
			capitoloUscitaGestione = ordinativoDiPagamento.getCapitoloUscitaGestione();
			List<ImportiCapitoloUG>  listaImportiCapitoloUG = capitoloUscitaGestione.getListaImportiCapitolo();
			 if(null!=listaImportiCapitoloUG && listaImportiCapitoloUG.size() > 0) {
					for(ImportiCapitoloUG importoCapitoloUG : listaImportiCapitoloUG) {
						if(importoCapitoloUG.getAnnoCompetenza().intValue() == bilancio.getAnno()){

							
							
							// FIXME: raffa 14-01-2016
							// per ora forziamo a 0 l'importo dei tre castelletti: è cambiata la normativa e il loro importo non deriva piu da capitolo ma da una classificazione 
							// Inoltre non è piu obbligatorio
//							siacTOrdinativoInsert.setOrdCastCassa(importoCapitoloUG.getStanziamentoCassa());
//							siacTOrdinativoInsert.setOrdCastCompetenza(importoCapitoloUG.getStanziamento());
//							siacTOrdinativoInsert.setOrdCastEmessi(importoCapitoloUG.getTotalePagato());
							
							siacTOrdinativoInsert.setOrdCastCassa(BigDecimal.ZERO);
							siacTOrdinativoInsert.setOrdCastCompetenza(BigDecimal.ZERO);
							siacTOrdinativoInsert.setOrdCastEmessi(BigDecimal.ZERO);
							
						}
					}				
				}
		} else {
			capitoloEntrataGestione = ordinativoIncasso.getCapitoloEntrataGestione();
			List<ImportiCapitoloEG>  listaImportiCapitoloEG =  capitoloEntrataGestione.getListaImportiCapitolo();
			if(null!=listaImportiCapitoloEG && listaImportiCapitoloEG.size() > 0) {
				for(ImportiCapitoloEG importoCapitoloEG : listaImportiCapitoloEG) {
					if(importoCapitoloEG.getAnnoCompetenza().intValue() == bilancio.getAnno()){
						
						// FIXME: raffa 14-01-2016
						// per ora forziamo a 0 l'importo dei tre castelletti: è cambiata la normativa e il loro importo non deriva piu da capitolo ma da una classificazione 
						// Inoltre non è piu obbligatorio
//						siacTOrdinativoInsert.setOrdCastCassa(importoCapitoloEG.getStanziamentoCassa());
//						siacTOrdinativoInsert.setOrdCastCompetenza(importoCapitoloEG.getStanziamento());
//						siacTOrdinativoInsert.setOrdCastEmessi(importoCapitoloEG.getTotaleIncassato());
						
						siacTOrdinativoInsert.setOrdCastCassa(BigDecimal.ZERO);
						siacTOrdinativoInsert.setOrdCastCompetenza(BigDecimal.ZERO);
						siacTOrdinativoInsert.setOrdCastEmessi(BigDecimal.ZERO);
					}
				}				
			}
		}
		
		// siac_t_ordinativo.ord_cast_cassa + siac_t_ordinativo.ord_cast_competenza + siac_t_ordinativo.ord_cast_emessi : fine

		// flag beneficiario multiplo
		// non viene gestito con la stringa Constanti.TRUE / Constanti.FALSE perche' sul db il campo della tabella e' di tipo boolean 
		siacTOrdinativoInsert.setOrdBeneficiariomult(ordinativo.isFlagBeneficiMultiplo());
		
		siacTOrdinativoInsert.setLoginModifica(loginOperazione);
		siacTOrdinativoInsert.setSiacTEnteProprietario(siacTEnteProprietario);
		siacTOrdinativoInsert.setSiacTBil(siacTBilOrdinativo);
		
		//SIOPE PLUS:
		// (attenzione: siope plus va qui prima del saveAndFlush su siacTMovgestTs
		//  perche' sono colonne di fk dentro siacTMovgestTs )
		if(diPagamento){
			impostaDatiSiopePlus(siacTOrdinativoInsert, ordinativo, datiOperazioneDto);
		}
		//END SIOPE PLUS
		
		
		// SIAC-6175
		siacTOrdinativoInsert.setOrdDaTrasmettere(Boolean.valueOf(ordinativo.isDaTrasmettere()));
		
		siacTOrdinativoInsert = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTOrdinativoInsert, datiOperazioneDto,siacTAccountRepository);
		
		if (usaDatiLoginOrig) {
			siacTOrdinativoInsert.setLoginCreazione(ordinativo.getLoginCreazione());
			siacTOrdinativoInsert.setLoginModifica(ordinativo.getLoginModifica());
			siacTOrdinativoInsert.setLoginOperazione(ordinativo.getLoginOperazione());
		}
		
		//salvo sul db:
		siacTOrdinativoInsert = siacTOrdinativoRepository.saveAndFlush(siacTOrdinativoInsert);
		
		// siac_r_ordinativo_bil_elem + siac_t_bil_elem : inizio
		if(diPagamento){
			salvaOrdinativoBilElem(capitoloUscitaGestione, datiOperazioneDto, siacTOrdinativoInsert);
		} else {
			salvaOrdinativoBilElem(capitoloEntrataGestione, datiOperazioneDto, siacTOrdinativoInsert);
		}
		// siac_r_ordinativo_bil_elem + siac_t_bil_elem : fine
		
		// siac_r_ordinativo_stato + siac_d_ordinativo_stato : inizio
		salvaSiacROrdinativoStato(ordinativo, datiOperazioneDto, siacTOrdinativoInsert);
		// siac_r_ordinativo_stato + siac_d_ordinativo_stato : fine
		
		//SOGGETTO E SEDI:
		if(diPagamento){
			//gestione soggetto/sede-mod pag
			gestisciSoggSediModalitaPag(ordinativoDiPagamento, datiOperazioneDto, siacTOrdinativoInsert);
		} else {
			//gestione soggeddo/sede
			gestisciSoggSede(ordinativoIncasso, datiOperazioneDto, siacTOrdinativoInsert);
		}

		// siac_r_ordinativo_atto_amm + siac_t_atto_amm : inizio
		salvaSiacROrdinativoAttoAmm(datiOperazioneDto,attoAmministrativo, siacTOrdinativoInsert);
		// siac_r_ordinativo_atto_amm + siac_t_atto_amm : fine

		AttributoTClassInfoDto attributoInfo = new AttributoTClassInfoDto();
		attributoInfo.setTipoOggetto(OggettoDellAttributoTClass.T_ORDINATIVO);
		attributoInfo.setSiacTOrdinativo(siacTOrdinativoInsert);

		// siac_r_ordinativo_attr + siac_t_attr : inizio
		salvaAttributoTAttr(attributoInfo, datiOperazioneDto, ordinativo.getNote(), Constanti.ORDINATIVO_T_ATTR_NOTE);

		//flag cartaceo
		salvaAttributoTAttr(attributoInfo, datiOperazioneDto, ordinativo.isFlagAllegatoCartaceo(), Constanti.ORDINATIVO_T_ATTR_FLAG_ALLEGATO_CARTACEO);

		//SIOPE PLUS, per l'ordinativo di pagamento viene introdotto il CIG:
		if(diPagamento){
			String cig = ( (OrdinativoPagamento) ordinativo).getCig();
			if(!StringUtils.isEmpty(cig)){
				salvaAttributoCig(attributoInfo, datiOperazioneDto, cig);
			}
		}
		//END CIG PER SIOPE PLUS
		
		// salvataggio dei dati relativi alla transazione elementare
		salvaTransazioneElementare(attributoInfo, datiOperazioneDto, ordinativo);

		// tipo_avviso
		if(ordinativo.getTipoAvviso()!=null){
			salvaAttributoTClass(datiOperazioneDto, attributoInfo, ordinativo.getTipoAvviso().getCodice(), Constanti.D_CLASS_TIPO_TIPO_AVVISO);
		}
		

		return siacTOrdinativoInsert;		
	}
	
	/**
	 * imposta i dati di siope plus in un siacTOrdinativoFin in inserimento o aggiornamento
	 * a partire da quanto ricevuto in impegno
	 * @param siacTOrdinativoFin
	 * @param ord
	 * @param datiOperazioneDto
	 */
	private void impostaDatiSiopePlus(SiacTOrdinativoFin siacTOrdinativoFin, Ordinativo ord, DatiOperazioneDto datiOperazioneDto){
		
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getUid();
		
		boolean inAggiornamentoMovimento = false; //inserimento imp / sub
		if(siacTOrdinativoFin.getUid()!=null && siacTOrdinativoFin.getUid()>0){
			inAggiornamentoMovimento = true;  //aggiornamento imp / sub
		}
		
		//SIOPE TIPO DEBITO
		SiopeTipoDebito siopeTipoDebito = ord.getSiopeTipoDebito();
		if(siopeTipoDebito!=null && !StringUtils.isEmpty(siopeTipoDebito.getCodice())){
			//recuperiamo il record della codifica ricevuta:
			List<SiacDSiopeTipoDebitoFin> dstdebitos = siacDSiopeTipoDebitoFinRepository.findByCode(idEnte, datiOperazioneDto.getTs(), siopeTipoDebito.getCodice());
			SiacDSiopeTipoDebitoFin siacDSiopeTipoDebito = CommonUtils.getFirst(dstdebitos);
			if(siacDSiopeTipoDebito!=null){
				siacTOrdinativoFin.setSiacDSiopeTipoDebitoFin(siacDSiopeTipoDebito);
			}
		} else if(inAggiornamentoMovimento){
			//sto aggiornando un movimento e non ho ricevuto un tipo debito, cosa faccio?
			//lo tolgo oppure tengo quello che eventualmente gia' c'e'?
			//per ora nulla..
		}
		
		//MOTIVAZIONE ASSENZA CIG
		SiopeAssenzaMotivazione siopeAssenzaMotivazione = ord.getSiopeAssenzaMotivazione();
		if(siopeAssenzaMotivazione!=null && !StringUtils.isEmpty(siopeAssenzaMotivazione.getCodice())){
			//recuperiamo il record della codifica ricevuta:
			List<SiacDSiopeAssenzaMotivazioneFin> aMtvs = siacDSiopeAssenzaMotivazioneFinRepository.findByCode(idEnte, datiOperazioneDto.getTs(), siopeAssenzaMotivazione.getCodice());
			SiacDSiopeAssenzaMotivazioneFin siacDSiopeAssenzaMotivazione = CommonUtils.getFirst(aMtvs);
			//puo' essere nullato a piacere (siacDSiopeAssenzaMotivazione puo' essere null):
			siacTOrdinativoFin.setSiacDSiopeAssenzaMotivazione(siacDSiopeAssenzaMotivazione);
		} else {
			//puo' essere nullato a piacere (siopeAssenzaMotivazione puo' essere null):
			siacTOrdinativoFin.setSiacDSiopeAssenzaMotivazione(null);
		}
	}
	
	/**
	 * Si occupa di impostare dentro siacTOrdinativo la causale ricevuta dentro OrdinativoIncasso
	 *
	 * vale sia per inserisci ordinativo di incasso che aggiorna
	 * 
	 * @param ordinativoIncasso
	 * @param datiOperazioneDto
	 * @param siacTOrdinativo
	 * @return
	 */
	private SiacTOrdinativoFin impostaCausaleOrdinativoIncasso(OrdinativoIncasso ordinativoIncasso, DatiOperazioneDto datiOperazioneDto,SiacTOrdinativoFin siacTOrdinativo){
		if(siacTOrdinativo==null){
			siacTOrdinativo = new SiacTOrdinativoFin();
		}
		
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		
		//per ordinativo di incasso puo' essere indicata una causale di entrata
		CausaleEntrata causaleEntrata = ordinativoIncasso.getCausale();
		SiacDCausaleFin siacDCausale = null;
		if(causaleEntrata!=null && !StringUtils.isEmpty(causaleEntrata.getCodice())){
			String causCode = causaleEntrata.getCodice();
			String causFamTipoCode = Constanti.CODE_TIPO_FAMIGLIA_CAUSALE_PREDOC_E;
			List<SiacDCausaleFin> casuali = siacDCausaleFinRepository.findCausaleValidaByCodice(idEnte, datiOperazioneDto.getTs(), causCode , causFamTipoCode );
			if(casuali!=null && casuali.size()==1){
				siacDCausale = casuali.get(0);
			}
		}
		
		siacTOrdinativo.setSiacDCausale(siacDCausale);
		
		return siacTOrdinativo;
	}

	/**
	 * capitoloGestione deve essere un CapitoloUscitaGestione oppure un CapitoloEntrataGestione
	 * @param capitoloGestione
	 * @param datiOperazioneDto
	 * @param siacTOrdinativo
	 * @return
	 * @throws RuntimeException
	 */
	private <O extends Object> SiacROrdinativoBilElemFin salvaOrdinativoBilElem(O capitoloGestione, DatiOperazioneDto datiOperazioneDto, SiacTOrdinativoFin siacTOrdinativo) throws RuntimeException {
		
		SiacROrdinativoBilElemFin siacROrdinativoBilElem = new SiacROrdinativoBilElemFin();
		
		Timestamp now = datiOperazioneDto.getTs();
		Integer idOrdinativo = siacTOrdinativo.getOrdId();
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		
		String codeArticolo = null;
		String codeCapitolo = null;
		String codeUeb = null;
		String annoCap = null;
		String codiceGestione = null;
		
		CapitoloUscitaGestione capitoloUscitaGestione = null;
		CapitoloEntrataGestione capitoloEntrataGestione = null;
		if(capitoloGestione instanceof CapitoloUscitaGestione){
			capitoloUscitaGestione = (CapitoloUscitaGestione) capitoloGestione;
			codiceGestione = Constanti.D_BIL_ELEM_TIPO_ELEM_TIPO_CODE_CAP_UG;
		} else if(capitoloGestione instanceof CapitoloEntrataGestione){
			capitoloEntrataGestione = (CapitoloEntrataGestione) capitoloGestione;
			codiceGestione = Constanti.D_BIL_ELEM_TIPO_ELEM_TIPO_CODE_CAP_EG;
		} else {
			return null; //errore di invocazione
		}

		if(capitoloUscitaGestione!=null){
			codeArticolo = capitoloUscitaGestione.getNumeroArticolo().toString();
			codeCapitolo = capitoloUscitaGestione.getNumeroCapitolo().toString();
			codeUeb = capitoloUscitaGestione.getNumeroUEB().toString();;
			annoCap = capitoloUscitaGestione.getAnnoCapitolo().toString();
		} else {
			codeArticolo = capitoloEntrataGestione.getNumeroArticolo().toString();
			codeCapitolo = capitoloEntrataGestione.getNumeroCapitolo().toString();
			codeUeb = capitoloEntrataGestione.getNumeroUEB().toString();;
			annoCap = capitoloEntrataGestione.getAnnoCapitolo().toString();
		}
		
		List<SiacTBilElemFin> siacTBilElems = siacTBilElemRepository.getValidoByCodes(idEnte, annoCap, datiOperazioneDto.getTs(), codeCapitolo, codeArticolo, codeUeb, codiceGestione);
		SiacTBilElemFin siacTBilElem = new SiacTBilElemFin();
		
		if(siacTBilElems != null && siacTBilElems.size() > 0){
			siacTBilElem = siacTBilElems.get(0);				
		
			List<SiacROrdinativoBilElemFin> caricatoDaDb = siacROrdinativoBilElemRepository.findValidoByIdOrdinativo(idEnte, now, idOrdinativo);
			
			boolean salvare = false;
			
			if(caricatoDaDb!=null && caricatoDaDb.size()>0){
				// SIAMO IN MODIFICA
				datiOperazioneDto.setOperazione(Operazione.MODIFICA);
				siacROrdinativoBilElem = caricatoDaDb.get(0);
				//si controlla il (o gli) campo che puo' essere modificato:
				if(siacROrdinativoBilElem.getSiacTBilElem().getElemId().intValue() != siacTBilElem.getElemId().intValue()){
					//se e' cambiato qualcosa devo aggiornare
					salvare = true;
				} 
			} else {
				// SIAMO IN INSERIMENTO
				siacROrdinativoBilElem = new SiacROrdinativoBilElemFin();//cosi viene inserito
				datiOperazioneDto.setOperazione(Operazione.INSERIMENTO);
				salvare = true;
			}
			
			if(salvare){
				siacROrdinativoBilElem = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacROrdinativoBilElem, datiOperazioneDto,siacTAccountRepository);
				siacROrdinativoBilElem.setSiacTBilElem(siacTBilElem);
				siacROrdinativoBilElem.setSiacTOrdinativo(siacTOrdinativo);
				//salvo sul db:
				siacROrdinativoBilElem = siacROrdinativoBilElemRepository.saveAndFlush(siacROrdinativoBilElem);			
			}
		}

		return siacROrdinativoBilElem;
	}
	
	/**
	 * Si occupa del salvataggio (inteso sia come inserimento che come aggiornamento)
	 * dello stato dell'ordinativo
	 * @param ordinativo
	 * @param datiOperazioneDto
	 * @param siacTOrdinativo
	 * @return
	 * @throws RuntimeException
	 */
	private SiacROrdinativoStatoFin salvaSiacROrdinativoStato(Ordinativo ordinativo,DatiOperazioneDto datiOperazioneDto, SiacTOrdinativoFin siacTOrdinativo) throws RuntimeException{

		SiacROrdinativoStatoFin siacROrdinativoStato = new SiacROrdinativoStatoFin();		

		SiacTEnteProprietarioFin siacTEnteProprietario =  datiOperazioneDto.getSiacTEnteProprietario();
		Integer idEnte = siacTEnteProprietario.getEnteProprietarioId();
		Timestamp now = datiOperazioneDto.getTs();
		Integer idOrdinativo = siacTOrdinativo.getOrdId();
		
		SiacDOrdinativoStatoFin siacDOrdinativoStato = new SiacDOrdinativoStatoFin();
		siacDOrdinativoStato = siacDOrdinativoStatoRepository.findDOrdinativoStatoValidoByEnteAndCode(idEnte, Constanti.statoOperativoOrdinativoEnumToString(ordinativo.getStatoOperativoOrdinativo()), datiOperazioneDto.getTs());
		
		List<SiacROrdinativoStatoFin> caricatoDaDb = siacROrdinativoStatoRepository.findSiacROrdinativoStatoValidoByIdOrdinativo(idEnte, idOrdinativo, now);
		
		boolean salvare = false;
		
		if(caricatoDaDb!=null && caricatoDaDb.size()>0){
			String methodName ="salvaSiacROrdinativoStato";
			log.info(methodName , "siamo in modifica");
			// SIAMO IN MODIFICA
			datiOperazioneDto.setOperazione(Operazione.MODIFICA);
			siacROrdinativoStato = caricatoDaDb.get(0);
			//si controlla il (o gli) campo che puo' essere modificato:
/*
			log.info(methodName , "siacDOrdinativoStato.getOrdStatoCode() " + siacDOrdinativoStato== null);
			log.info(methodName , "siacDOrdinativoStato " + siacDOrdinativoStato== null);
			log.info(methodName , "siacROrdinativoStato.getSiacDOrdinativoStato().getOrdStatoCode() " + siacROrdinativoStato.getSiacDOrdinativoStato().getOrdStatoCode());
			log.info(methodName , "siacDOrdinativoStato.getOrdStatoCode() " + siacDOrdinativoStato.getOrdStatoCode());
*/
			//SIAC-6646 se lo stato è firmato anche a fronte di modifica lo stato non va cambiato
			if(!siacROrdinativoStato.getSiacDOrdinativoStato().getOrdStatoCode().equalsIgnoreCase(siacDOrdinativoStato.getOrdStatoCode()) && !siacROrdinativoStato.getSiacDOrdinativoStato().getOrdStatoCode().equals("F") ){
				//se e' cambiato qualcosa devo aggiornare
				salvare = true;
			} 
		} else {
			// SIAMO IN INSERIMENTO
			siacROrdinativoStato = new SiacROrdinativoStatoFin();//cosi viene inserito
			datiOperazioneDto.setOperazione(Operazione.INSERIMENTO);
			salvare = true;
		}
		
		//if(true) throw new BusinessException("test");
		
		if(salvare){
			siacROrdinativoStato = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacROrdinativoStato, datiOperazioneDto,siacTAccountRepository);
			siacROrdinativoStato.setSiacDOrdinativoStato(siacDOrdinativoStato);
			siacROrdinativoStato.setSiacTOrdinativo(siacTOrdinativo);
			//salvo sul db:
			siacROrdinativoStato = siacROrdinativoStatoRepository.saveAndFlush(siacROrdinativoStato);
		}

		return siacROrdinativoStato;
	}
	
	/**
	 * 	Si occupa del salvataggio (inteso sia come inserimento che come aggiornamento)
	 * del Soggetto dell'ordinativo
	 * @param datiOperazioneDto
	 * @param siacTOrdinativo
	 * @param siacTSoggettoOrdine
	 * @return
	 * @throws RuntimeException
	 */
	private SiacROrdinativoSoggettoFin salvaSiacROrdinativoSoggetto(DatiOperazioneDto datiOperazioneDto, SiacTOrdinativoFin siacTOrdinativo, SiacTSoggettoFin siacTSoggettoOrdine) throws RuntimeException {

		SiacROrdinativoSoggettoFin siacROrdinativoSoggetto = new SiacROrdinativoSoggettoFin();

		SiacTEnteProprietarioFin siacTEnteProprietario =  datiOperazioneDto.getSiacTEnteProprietario();
		Integer idEnte = siacTEnteProprietario.getEnteProprietarioId();
		Timestamp now = datiOperazioneDto.getTs();
//		Integer idSoggetto = siacTSoggettoOrdine.getSoggettoId();
		Integer idOrdinativo = siacTOrdinativo.getOrdId();
		
		List<SiacROrdinativoSoggettoFin> caricatoDaDb = siacROrdinativoSoggettoRepository.findValidoByIdOrdinativo(idEnte, now, idOrdinativo);
		
		boolean salvare = false;
	
		if(caricatoDaDb!=null && caricatoDaDb.size()>0){
			// SIAMO IN MODIFICA
			datiOperazioneDto.setOperazione(Operazione.MODIFICA);
			siacROrdinativoSoggetto = caricatoDaDb.get(0);
			//si controlla il (o gli) campo che puo' essere modificato:
			if(siacROrdinativoSoggetto.getSiacTSoggetto().getSoggettoId().intValue() == siacTSoggettoOrdine.getSoggettoId().intValue()){
				//se e' cambiato qualcosa devo aggiornare
				salvare = false;
			} else {
				// 30/06/2014 : jira siac-
				// non veniva correttamente identificato il cambiamento del soggetto / sede secondaria
				salvare = true;
			} 
			
		} else {
			// SIAMO IN INSERIMENTO
			 siacROrdinativoSoggetto = new SiacROrdinativoSoggettoFin();//cosi viene inserito
			 datiOperazioneDto.setOperazione(Operazione.INSERIMENTO);
			 salvare = true;
		}
		
		if(salvare){
			siacROrdinativoSoggetto = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacROrdinativoSoggetto, datiOperazioneDto,siacTAccountRepository);
			siacROrdinativoSoggetto.setSiacTSoggetto(siacTSoggettoOrdine);
			siacROrdinativoSoggetto.setSiacTOrdinativo(siacTOrdinativo);
			//salvo sul db:
			siacROrdinativoSoggetto = siacROrdinativoSoggettoRepository.saveAndFlush(siacROrdinativoSoggetto);
		}
			
		
		return siacROrdinativoSoggetto;
	}	
	
	/**
	 * Si occupa del salvataggio (inteso sia come inserimento che come aggiornamento)
	 * della modalita' di pagamento dell'ordinativo
	 * @param datiOperazioneDto
	 * @param siacTOrdinativo
	 * @param siacTModpagOrdinativo
	 * @return
	 * @throws RuntimeException
	 */
	private SiacROrdinativoModpagFin salvaSiacROrdinativoModpag(DatiOperazioneDto datiOperazioneDto, SiacTOrdinativoFin siacTOrdinativo, SiacTModpagFin siacTModpagOrdinativo) throws RuntimeException {

		SiacROrdinativoModpagFin siacROrdinativoModpag = new SiacROrdinativoModpagFin();
		String methodName = "salvaSiacROrdinativoModpag";
		
		SiacTEnteProprietarioFin siacTEnteProprietario =  datiOperazioneDto.getSiacTEnteProprietario();
		Integer idEnte = siacTEnteProprietario.getEnteProprietarioId();
		Timestamp now = datiOperazioneDto.getTs();
		Integer idOrdinativo = siacTOrdinativo.getOrdId();
		
		List<SiacROrdinativoModpagFin> caricatoDaDb = siacROrdinativoModpagRepository.findValidoByIdOrdinativo(idEnte, now, idOrdinativo);
		
		boolean salvare = false;
		
	
		if(caricatoDaDb!=null && caricatoDaDb.size()>0){
			// SIAMO IN MODIFICA
			datiOperazioneDto.setOperazione(Operazione.MODIFICA);
			siacROrdinativoModpag = caricatoDaDb.get(0);
			
			//si controlla il (o gli) campo che puo' essere modificato:
			log.debug(methodName," modpagId letto dal soggetto in input: " + siacTModpagOrdinativo.getModpagId() );
			
			Integer uidModpagDb = null;
			if(siacROrdinativoModpag.getSiacTModpag()!=null && siacROrdinativoModpag.getSiacTModpag().getUid()!=null){
				uidModpagDb = siacROrdinativoModpag.getSiacTModpag().getUid();
			}else{
				uidModpagDb = siacROrdinativoModpag.getCessioneId().getUid();
			}
			if(uidModpagDb == siacTModpagOrdinativo.getModpagId()){
				//se e' cambiato qualcosa devo aggiornare
				salvare = false;
			} else {
				// 30/06/2014 : jira siac-
				// non veniva correttamente identificato il cambiamento della modalita' di pagamento
				salvare = true;
			}
		} else {
			// SIAMO IN INSERIMENTO
			siacROrdinativoModpag = new SiacROrdinativoModpagFin();//cosi viene inserito
			datiOperazioneDto.setOperazione(Operazione.INSERIMENTO);
			salvare = true;
		}
		
		if(salvare){
			siacROrdinativoModpag = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacROrdinativoModpag, datiOperazioneDto,siacTAccountRepository);
			siacROrdinativoModpag.setSiacTOrdinativo(siacTOrdinativo);
			
			log.debug(methodName,"AccreditoTipoCode: " + siacTModpagOrdinativo.getSiacDAccreditoTipo().getAccreditoTipoCode());
			
			// qui devo verificare se siamo in modalita pagamento normale o cessione/pignoramento/fallimento
			if(!siacTModpagOrdinativo.getSiacDAccreditoTipo().getAccreditoTipoCode().equals(TipoAccredito.CSI.toString()) &&
					!siacTModpagOrdinativo.getSiacDAccreditoTipo().getAccreditoTipoCode().equals(TipoAccredito.FA.toString()) &&
					!siacTModpagOrdinativo.getSiacDAccreditoTipo().getAccreditoTipoCode().equals(TipoAccredito.SU.toString()) &&
					!siacTModpagOrdinativo.getSiacDAccreditoTipo().getAccreditoTipoCode().equals(TipoAccredito.CPT.toString()) &&
					!siacTModpagOrdinativo.getSiacDAccreditoTipo().getAccreditoTipoCode().equals(TipoAccredito.CSIG.toString()) &&
					!siacTModpagOrdinativo.getSiacDAccreditoTipo().getAccreditoTipoCode().equals(TipoAccredito.PI.toString()))
			{
				siacROrdinativoModpag.setSiacTModpag(siacTModpagOrdinativo);
				siacROrdinativoModpag.setCessioneId(null);
			}
			else 
			{
				SiacRSoggettoRelazFin siacRSoggettoRelazFin = new  SiacRSoggettoRelazFin();
				siacRSoggettoRelazFin.setUid(siacTModpagOrdinativo.getUid());
				siacROrdinativoModpag.setCessioneId(siacRSoggettoRelazFin);
				
				siacROrdinativoModpag.setSiacTModpag(null);
				
			}
				
			//salvo sul db:
			siacROrdinativoModpag = siacROrdinativoModpagRepository.saveAndFlush(siacROrdinativoModpag);
		}

		return siacROrdinativoModpag;
	}
	
	/**
	 * Si occupa del salvataggio (inteso sia come inserimento che come aggiornamento)
	 * dell'AttoAmm dell'ordinativo
	 * @param datiOperazioneDto
	 * @param attoAmministrativo
	 * @param siacTOrdinativo
	 * @return
	 * @throws RuntimeException
	 */
	private SiacROrdinativoAttoAmmFin salvaSiacROrdinativoAttoAmm(DatiOperazioneDto datiOperazioneDto, AttoAmministrativo attoAmministrativo, SiacTOrdinativoFin siacTOrdinativo) throws RuntimeException {

		SiacROrdinativoAttoAmmFin siacROrdinativoAttoAmm = new SiacROrdinativoAttoAmmFin();

		SiacTEnteProprietarioFin siacTEnteProprietario =  datiOperazioneDto.getSiacTEnteProprietario();
		Integer idEnte = siacTEnteProprietario.getEnteProprietarioId();
		Timestamp now = datiOperazioneDto.getTs();
		Integer idOrdinativo = siacTOrdinativo.getOrdId();
		
		
		SiacTAttoAmmFin siacTAttoAmm =getSiacTAttoAmmFromAttoAmministrativo(attoAmministrativo, idEnte);
		
		List<SiacROrdinativoAttoAmmFin> caricatoDaDb = siacROrdinativoAttoAmmRepository.findValidoByIdOrdinativo(idEnte, now, idOrdinativo);
		
		boolean salvare = false;

		if(caricatoDaDb!=null && caricatoDaDb.size()>0){
			// SIAMO IN MODIFICA
			datiOperazioneDto.setOperazione(Operazione.MODIFICA);
			siacROrdinativoAttoAmm = caricatoDaDb.get(0);
			//si controlla il (o gli) campo che puo' essere modificato:
			if(siacROrdinativoAttoAmm.getSiacTAttoAmm().getAttoammId() != siacTAttoAmm.getAttoammId()){
				//se e' cambiato qualcosa devo aggiornare
				salvare = true;
			} 
		} else {
			// SIAMO IN INSERIMENTO
			siacROrdinativoAttoAmm = new SiacROrdinativoAttoAmmFin();//cosi viene inserito
			datiOperazioneDto.setOperazione(Operazione.INSERIMENTO);
			salvare = true;
		}
		
		if(salvare){
			siacROrdinativoAttoAmm = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacROrdinativoAttoAmm, datiOperazioneDto,siacTAccountRepository);
			siacROrdinativoAttoAmm.setSiacTOrdinativo(siacTOrdinativo);
			siacROrdinativoAttoAmm.setSiacTAttoAmm(siacTAttoAmm);
			//salvo sul db:
			siacROrdinativoAttoAmm = siacROrdinativoAttoAmmRepository.saveAndFlush(siacROrdinativoAttoAmm);
		}

		
		return siacROrdinativoAttoAmm;
	}
	
	/**
	 * 
	 * Crea un collegamento del tipo indicato tra i due ordinativi indicati.
	 * 
	 * Assume che entrabi gli ordinativi abbiano l'uid valorizzato per individuarli tramite ord_id
	 * 
	 * @param ordinativoDa
	 * @param ordinativoA
	 * @param tipo
	 * @param datiOperazione
	 */
	public void collegaDueOrdinativi(Ordinativo ordinativoDa, Ordinativo ordinativoA, TipoAssociazioneEmissione tipo, DatiOperazioneDto datiOperazione){
		SiacTOrdinativoFin siacTOrdinativoDa = siacTOrdinativoRepository.findOne(ordinativoDa.getUid());
		Ordinativo ordA = clone(ordinativoA);
		ordA.setTipoAssociazioneEmissione(tipo);
		salvaRelazioneOrdinativiCollegati(datiOperazione, siacTOrdinativoDa, null, toList(ordA));
	}
	
	/**
	 * Salva gli oridnativi collegati
	 * @param datiOperazioneDto
	 * @param attoAmministrativo
	 * @param siacTOrdinativo
	 * @return(datiOperazioneDto, siacTOrdinativoInsert, ordinativo.getElencoOrdinativiCollegati());
	 * @throws RuntimeException
	 */
	private <O extends Ordinativo> void salvaRelazioneOrdinativiCollegati(DatiOperazioneDto datiOperazioneDto, SiacTOrdinativoFin siacTOrdinativo, OrdinativoInInserimentoInfoDto datiInsInfo, List<O> ordinativiCollegati) throws RuntimeException {
	
		SiacTEnteProprietarioFin siacTEnteProprietario =  datiOperazioneDto.getSiacTEnteProprietario();
		Integer idEnte = siacTEnteProprietario.getEnteProprietarioId();
		Timestamp now = datiOperazioneDto.getTs();
			
		// quando scorro i collegati prima di inserire il legame con l'ordinativo associato devo inserire l'ordinativo stesso, quello che poi collego!
		for (O ordinativoCollegato : ordinativiCollegati) {
			
			// 1- inserisco l'ordinativo
			// 2- lo collego
			// l'inserimento del collegato viene fatto dall'emettitore, mi devono gia arrivare i collegati completi di uid e tipo relazione
			// 
			SiacTOrdinativoFin siacTOrdinativo2 = new SiacTOrdinativoFin();
			siacTOrdinativo2.setUid(ordinativoCollegato.getUid());
			
			SiacDRelazTipoFin dRelazTipo = siacDRelazTipoFinRepository.findDRelazTipoValidoByEnteAndCode(idEnte, 
					ordinativoCollegato.getTipoAssociazioneEmissione().toString());
			
			SiacROrdinativoFin siacROrdinativo = new SiacROrdinativoFin();
			siacROrdinativo = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacROrdinativo, datiOperazioneDto,siacTAccountRepository);
			siacROrdinativo.setSiacTOrdinativo1(siacTOrdinativo); // da
			siacROrdinativo.setSiacTOrdinativo2(siacTOrdinativo2); // a
			siacROrdinativo.setSiacDRelazTipo(dRelazTipo); 
			
			siacROrdinativoRepository.saveAndFlush(siacROrdinativo);
		}

	}

	protected void salvaRelazSubOrdinativoEDettaglioOnere(
			DatiOperazioneDto datiOperazioneDto, SiacTOrdinativoTFin siacTOrdinativoTs, SubOrdinativo subOrdinativo) {

		String methodName = "salvaRelazSubOrdinativoEDettaglioOnere";
		if(subOrdinativo.getDettaglioOnere()!=null){
		
					DettaglioOnere dettaglioOnere = subOrdinativo.getDettaglioOnere();
					
					SiacRDocOnereOrdinativoTsFin siacRDocOnereOrdinativoTFin = new SiacRDocOnereOrdinativoTsFin();
					siacRDocOnereOrdinativoTFin = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRDocOnereOrdinativoTFin, 
							datiOperazioneDto,siacTAccountRepository);
					
					SiacRDocOnereFin rdocOnere = new SiacRDocOnereFin();
					rdocOnere.setUid(dettaglioOnere.getUid());
					
					siacRDocOnereOrdinativoTFin.setSiacRDocOnere(rdocOnere);
					siacRDocOnereOrdinativoTFin.setSiacTOrdinativoT(siacTOrdinativoTs);
					
					log.debug(methodName,"salvo relazione tra siacTOrdinativoTFin.uid [ " +siacTOrdinativoTs.getUid()+" ] e onere [ " + dettaglioOnere.getUid()+" ]");
					siacRDocOnereOrdinativoTFin = siacRDocOnereOrdinativoTsFinRepository.saveAndFlush(siacRDocOnereOrdinativoTFin);
		
		} else {
			log.debug(methodName, "Nessun onere da inserire sul subOrdinativo [ " + siacTOrdinativoTs.getUid()+" ]");
		}

	}
	

	/**
	 * Si occupa di creare il record in SiacTOrdinativoTs e tabelle accessorie
	 * @param bilancio
	 * @param siacTOrdinativo
	 * @param codeSubOrdinativo
	 * @param subOrdinativo
	 * @param datiOperazioneDto
	 * @param info
	 * @return
	 * @throws RuntimeException
	 */
	@SuppressWarnings("unchecked")
	private SiacTOrdinativoTFin inserisciSiacTOrdinativoTs(Bilancio bilancio, SiacTOrdinativoFin siacTOrdinativo, String codeSubOrdinativo, SubOrdinativo subOrdinativo,
			DatiOperazioneDto datiOperazioneDto,OrdinativoInInserimentoInfoDto info) throws RuntimeException {
		
		SiacTOrdinativoTFin siacTOrdinativoTsInsert = new SiacTOrdinativoTFin();
			
		boolean isPagamento = isPagamento(subOrdinativo);
		
		datiOperazioneDto.setOperazione(Operazione.INSERIMENTO);
		
		SubOrdinativoPagamento subOrdinativoPagamento = null;
		SubOrdinativoIncasso subOrdinativoIncasso = null;
		if(isPagamento){
			subOrdinativoPagamento = (SubOrdinativoPagamento) subOrdinativo;
		}else {
			subOrdinativoIncasso = (SubOrdinativoIncasso) subOrdinativo;
		}
		//carico l'ente:
		int idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository.findOne(idEnte);

		siacTOrdinativoTsInsert.setOrdTsCode(codeSubOrdinativo);
		siacTOrdinativoTsInsert.setOrdTsDesc(subOrdinativo.getDescrizione());
		
		//estraggo la data scadenza:
		Date dataScadenza = getDataScadenza((S) subOrdinativo);
		if (dataScadenza != null) {		
				
			// CR- 3451: se la data esecuzione pagamento passata al servizio non è relativa all'anno di bilancio, 
			// viene forzata con il 31/12 dell'anno di bilancio 
			Integer inputAnnoScadenzaQuotaOrdinativo = TimingUtils.getAnno(dataScadenza);
			if(inputAnnoScadenzaQuotaOrdinativo.compareTo(bilancio.getAnno()) > 0 ){
				
				siacTOrdinativoTsInsert.setOrdTsDataScadenza(TimingUtils.getUltimoGiornoAnnoBilancio(bilancio.getAnno()));
			
			}else{
			
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(dataScadenza);
				Timestamp tstamp = new Timestamp(calendar.getTimeInMillis());
				siacTOrdinativoTsInsert.setOrdTsDataScadenza(tstamp);
			}
		}
		
		siacTOrdinativoTsInsert.setSiacTEnteProprietario(siacTEnteProprietario);
		siacTOrdinativoTsInsert.setSiacTOrdinativo(siacTOrdinativo);
		
//		SiacRDocOnereFin siacRDocOnere = new SiacRDocOnereFin();
//		siacRDocOnere.setUid(subOrdinativo.getDettaglioOnere().getUid());
//		siacTOrdinativoTsInsert.setSiacRDocOnere(siacRDocOnere);
		
		siacTOrdinativoTsInsert = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTOrdinativoTsInsert, datiOperazioneDto,siacTAccountRepository);
		//salvo sul db:
		siacTOrdinativoTsInsert = siacTOrdinativoTRepository.saveAndFlush(siacTOrdinativoTsInsert);

		// importo iniziale : inizio
		SiacTOrdinativoTsDetFin iniziale =
		salvaImportoOrdinativoTs(siacTOrdinativoTsInsert, datiOperazioneDto, subOrdinativo.getImportoIniziale(), Constanti.D_ORDINATIVO_TS_DET_TIPO_IMPORTO_INIZIALE);
		// importo iniziale : fine

		// importo attuale : inizio
		SiacTOrdinativoTsDetFin attuale = 
		salvaImportoOrdinativoTs(siacTOrdinativoTsInsert, datiOperazioneDto, subOrdinativo.getImportoAttuale(), Constanti.D_ORDINATIVO_TS_DET_TIPO_IMPORTO_ATTUALE);
		// importo attuale : fine
		
		siacTOrdinativoTsInsert.setSiacTOrdinativoTsDets(toList(iniziale,attuale));
		
		// liquidazione oppure accertamento 
		if(isPagamento){
			Liquidazione liquidazione = subOrdinativoPagamento.getLiquidazione();
			Integer idLiquidazione = liquidazione.getUid();
			SiacRLiquidazioneOrdFin siacRLiquidazioneOrd = salvaSiacRLiquidazioneOrd(siacTOrdinativoTsInsert, datiOperazioneDto,idLiquidazione);
			siacTOrdinativoTsInsert.setSiacRLiquidazioneOrds(toList(siacRLiquidazioneOrd));
			
		} else {
			Integer idAccertamento = subOrdinativoIncasso.getAccertamento().getUid();
			if(info!=null && (subOrdinativoIncasso.getAccertamento() instanceof SubAccertamento)){
				
			} else if(info!=null && (subOrdinativoIncasso.getAccertamento() instanceof Accertamento)){
				MovimentoGestioneLigthDto mgli = info.getByIdMovgest(idAccertamento);
				if(mgli!=null){
					idAccertamento = mgli.getMovgestTsId();
				}
			}
			SiacROrdinativoTsMovgestTFin siacROrdinativoTsMovgestT = salvaSiacROrdinativoTsMovgestTs(siacTOrdinativoTsInsert, datiOperazioneDto, idAccertamento);
			siacTOrdinativoTsInsert.setSiacROrdinativoTsMovgestTs(toList(siacROrdinativoTsMovgestT));
		}
		
		
		// RM 04/05/2015: Sposto logica inserimento legame subOrdinativo - SubDocumento qui, non è piu l'emettitore a gestirla
		Integer uidSubDocumento;
		if(isPagamento){
			uidSubDocumento = (subOrdinativoPagamento.getSubDocumentoSpesa()!=null?subOrdinativoPagamento.getSubDocumentoSpesa().getUid():null);
		}else{
			uidSubDocumento = (subOrdinativoIncasso.getSubDocumentoEntrata()!=null?subOrdinativoIncasso.getSubDocumentoEntrata().getUid():null);
		}
		
		if(uidSubDocumento!=null){
			SiacRSubdocOrdinativoTFin siacRSubdocOrdinativoTs = salvaSiacRSubdocOrdinativoTFin(siacTOrdinativoTsInsert, datiOperazioneDto,uidSubDocumento);
			siacTOrdinativoTsInsert.setSiacRSubdocOrdinativoTs(toList(siacRSubdocOrdinativoTs));
		}
		
		// jira 2601 inserisco relazione tra subOrdinativo del collegato con il dettaglio onere 
		salvaRelazSubOrdinativoEDettaglioOnere(datiOperazioneDto, siacTOrdinativoTsInsert, subOrdinativo);

		return siacTOrdinativoTsInsert;		
	}
	
	/**
	 * Si occupa del salvataggio (inteso sia come inserimento che come aggiornamento)
	 * dell'importo dell'ordinativo
	 * @param siacTOrdinativoTs
	 * @param datiOperazioneDto
	 * @param importo
	 * @param codiceTipoImporto
	 * @return
	 * @throws RuntimeException
	 */
	private SiacTOrdinativoTsDetFin salvaImportoOrdinativoTs(SiacTOrdinativoTFin siacTOrdinativoTs, DatiOperazioneDto datiOperazioneDto, BigDecimal importo, String codiceTipoImporto) throws RuntimeException {
		
		SiacTOrdinativoTsDetFin siacTOrdinativoTsDet = new SiacTOrdinativoTsDetFin();
		
		SiacTEnteProprietarioFin siacTEnteProprietario =  datiOperazioneDto.getSiacTEnteProprietario();
		Integer idEnte = siacTEnteProprietario.getEnteProprietarioId();
		Timestamp now = datiOperazioneDto.getTs();
		SiacDOrdinativoTsDetTipoFin siacDOrdinativoTsDetTipo = siacDOrdinativoTsDetTipoRepository.findDOrdinativoTsDetTipoValidoByEnteAndCode(idEnte, codiceTipoImporto, datiOperazioneDto.getTs());
		Integer idSiacTOrdinativoTs = siacTOrdinativoTs.getOrdTsId();
		List<SiacTOrdinativoTsDetFin> importoCaricato = siacTOrdinativoTsDetRepository.findValidoByTipo(idEnte, now, Constanti.D_ORDINATIVO_TS_DET_TIPO_IMPORTO_ATTUALE, idSiacTOrdinativoTs);
		
		if(importoCaricato!=null && importoCaricato.size()>0){
			//SIAMO IN MODIFICA importoCaricato avra' un unico elemento caricato:
			siacTOrdinativoTsDet = importoCaricato.get(0);
			datiOperazioneDto.setOperazione(Operazione.MODIFICA);
			siacTOrdinativoTsDet = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTOrdinativoTsDet, datiOperazioneDto,siacTAccountRepository);
		} else {
			//SIAMO IN INSERIMENTO
			siacTOrdinativoTsDet = new SiacTOrdinativoTsDetFin();
			datiOperazioneDto.setOperazione(Operazione.INSERIMENTO);
			siacTOrdinativoTsDet = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTOrdinativoTsDet, datiOperazioneDto,siacTAccountRepository);
		}
		
		siacTOrdinativoTsDet.setSiacTEnteProprietario(siacTEnteProprietario);
		siacTOrdinativoTsDet.setSiacTOrdinativoT(siacTOrdinativoTs);
		siacTOrdinativoTsDet.setSiacDOrdinativoTsDetTipo(siacDOrdinativoTsDetTipo);
		siacTOrdinativoTsDet.setOrdTsDetImporto(importo);
		//salvo sul db:
		siacTOrdinativoTsDet = siacTOrdinativoTsDetRepository.saveAndFlush(siacTOrdinativoTsDet);
		
		return siacTOrdinativoTsDet;
	}
	
	/**
	 * gestisce il legame tra ordinativo di incasso e accertamento
	 * @param siacTOrdinativoTs
	 * @param datiOperazioneDto
	 * @param idLiquidazione
	 * @return
	 * @throws RuntimeException
	 */
	private SiacROrdinativoTsMovgestTFin salvaSiacROrdinativoTsMovgestTs(SiacTOrdinativoTFin siacTOrdinativoTs, DatiOperazioneDto datiOperazioneDto, Integer idAccertamento) throws RuntimeException {
		
		SiacROrdinativoTsMovgestTFin siacROrdinativoTsMovgestTs = new SiacROrdinativoTsMovgestTFin();

		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		Timestamp now = datiOperazioneDto.getTs();
		Integer idOrdinativoTs = siacTOrdinativoTs.getOrdTsId();
		
		SiacTMovgestTsFin siacTMovgestTs = siacTMovgestTsRepository.findOne(idAccertamento);
		
		List<SiacROrdinativoTsMovgestTFin> caricatoDaDb = SiacROrdinativoTsMovgestTsRepository.findValidoByIdOrdinativoTs(idEnte, now, idOrdinativoTs);

		boolean salvare = false;

		if(caricatoDaDb!=null && caricatoDaDb.size()>0){
			// SIAMO IN MODIFICA
			datiOperazioneDto.setOperazione(Operazione.MODIFICA);
			siacROrdinativoTsMovgestTs = caricatoDaDb.get(0);
			//si controlla il (o gli) campo che puo' essere modificato:
			if(siacROrdinativoTsMovgestTs.getSiacTMovgestTs().getMovgestTsId() != siacTMovgestTs.getMovgestTsId()){
				//se e' cambiato qualcosa devo aggiornare
				salvare = true;
			} 
		} else {
			// SIAMO IN INSERIMENTO
			siacROrdinativoTsMovgestTs = new SiacROrdinativoTsMovgestTFin();//cosi viene inserito
			datiOperazioneDto.setOperazione(Operazione.INSERIMENTO);
			salvare = true;
		}

		if(salvare){
			siacROrdinativoTsMovgestTs = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacROrdinativoTsMovgestTs, datiOperazioneDto,siacTAccountRepository);
			siacROrdinativoTsMovgestTs.setSiacTMovgestTs(siacTMovgestTs);
			siacROrdinativoTsMovgestTs.setSiacTOrdinativoT(siacTOrdinativoTs);
			//salvo sul db:
			siacROrdinativoTsMovgestTs = SiacROrdinativoTsMovgestTsRepository.saveAndFlush(siacROrdinativoTsMovgestTs);
		}
			
		return siacROrdinativoTsMovgestTs;
	}
	
	/**
	 * gestisce il legame tra subOrdinativo e quota documento
	 * @param siacTOrdinativoTs
	 * @param datiOperazioneDto
	 * @param idSubDocumento
	 * @return
	 * @throws RuntimeException
	 */
	private SiacRSubdocOrdinativoTFin salvaSiacRSubdocOrdinativoTFin(SiacTOrdinativoTFin siacTOrdinativoTs, DatiOperazioneDto datiOperazioneDto, Integer idSubDocumento) throws RuntimeException {
		
		SiacRSubdocOrdinativoTFin siacRSubdocOrdinativoTFin = new SiacRSubdocOrdinativoTFin();
		datiOperazioneDto.setOperazione(Operazione.INSERIMENTO);
		siacRSubdocOrdinativoTFin = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRSubdocOrdinativoTFin, datiOperazioneDto,siacTAccountRepository);
		
		SiacTSubdocFin siacTSubdoc = new SiacTSubdocFin();
		siacTSubdoc.setUid(idSubDocumento);
				
		siacRSubdocOrdinativoTFin.setSiacTSubdoc(siacTSubdoc);
		siacRSubdocOrdinativoTFin.setSiacTOrdinativoT(siacTOrdinativoTs);
		
		siacRSubdocOrdinativoTFin = siacRSubdocOrdinativoTFinRepository.saveAndFlush(siacRSubdocOrdinativoTFin);

		return siacRSubdocOrdinativoTFin;
	}
	
	
	private SiacRLiquidazioneOrdFin salvaSiacRLiquidazioneOrd(SiacTOrdinativoTFin siacTOrdinativoTs, DatiOperazioneDto datiOperazioneDto, Integer idLiquidazione) throws RuntimeException {
		
		SiacRLiquidazioneOrdFin siacRLiquidazioneOrd = new SiacRLiquidazioneOrdFin();

		//leggo le variabili di input:
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		Timestamp now = datiOperazioneDto.getTs();
		Integer idOrdinativoTs = siacTOrdinativoTs.getOrdTsId();
		
		//carico la liquidazione:
		SiacTLiquidazioneFin siacTLiquidazione = new SiacTLiquidazioneFin();
		
		siacTLiquidazione = siacTLiquidazioneRepository.findLiquidazioneValidaByEnteAndId(idEnte, idLiquidazione, datiOperazioneDto.getTs());

		List<SiacRLiquidazioneOrdFin> caricatoDaDb = siacRLiquidazioneOrdRepository.findValidoByIdOrdinativoTs(idEnte, now, idOrdinativoTs);

		boolean salvare = false;

		if(caricatoDaDb!=null && caricatoDaDb.size()>0){
			// SIAMO IN MODIFICA
			datiOperazioneDto.setOperazione(Operazione.MODIFICA);
			siacRLiquidazioneOrd = caricatoDaDb.get(0);
			//si controlla il (o gli) campo che puo' essere modificato:
			if(siacRLiquidazioneOrd.getSiacTLiquidazione().getLiqId() != siacTLiquidazione.getLiqId()){
				//se e' cambiato qualcosa devo aggiornare
				salvare = true;
			} 
		} else {
			// SIAMO IN INSERIMENTO
			siacRLiquidazioneOrd = new SiacRLiquidazioneOrdFin();//cosi viene inserito
			datiOperazioneDto.setOperazione(Operazione.INSERIMENTO);
			salvare = true;
		}

		if(salvare){
			siacRLiquidazioneOrd = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRLiquidazioneOrd, datiOperazioneDto,siacTAccountRepository);
			siacRLiquidazioneOrd.setSiacTLiquidazione(siacTLiquidazione);
			siacRLiquidazioneOrd.setSiacTOrdinativoT(siacTOrdinativoTs);
			//salvo sul db:
			siacRLiquidazioneOrd = siacRLiquidazioneOrdRepository.saveAndFlush(siacRLiquidazioneOrd);
		}

		return siacRLiquidazioneOrd;
	}
	
	
	/**
	 * Esegue i controlli preliminari rispetto all'aggiornamento di un ordinativo di pagamento
	 * @param ordinativoDiPagamento
	 * @param ordinativoInModificaInfoDto
	 * @param datiOperazione
	 * @return
	 */
	public List<Errore> controlliDiMeritoAggiornamentoOrdinativoDiPagamento(OrdinativoPagamento ordinativoDiPagamento,OrdinativoInModificaInfoDto ordinativoInModificaInfoDto,DatiOperazioneDto datiOperazione){
		
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		SiacTOrdinativoFin siacTOrdinativo = ordinativoInModificaInfoDto.getSiacTOrdinativo();

		if(null!=siacTOrdinativo){
		    OrdinativoPagamento ordinativoPagamentoDb = (OrdinativoPagamento)ordinativoInModificaInfoDto.getOrdinativo();
	        
			// Stato Operativo: l'operazione e' permessa solo se statoOperativoOrdinativo e' diverso da ANNULLATO
			if(Constanti.D_ORDINATIVO_STATO_ANNULLATO.equalsIgnoreCase(Constanti.statoOperativoOrdinativoEnumToString(ordinativoPagamentoDb.getStatoOperativoOrdinativo()))){
				listaErrori.add(ErroreFin.OPERAZIONE_NON_POSSIBILE.getErrore("AGGIORNAMENTO ORDINATIVO"));
			}
			
			// Verifico che ci sia almeno un sub-ordinativo da inserire o da modificare
		} else {
			listaErrori.add(ErroreCore.ENTITA_NON_TROVATA.getErrore("Ordinativo di pagamento", ordinativoDiPagamento.getAnno() + " / " + ordinativoDiPagamento.getNumero()));
		}

		return listaErrori;
	}

	/**
	 * Si occupa di valutare le modifiche riguardanti le regolarizzazioni di cassa, individuando
	 * quelle da aggiornare, eliminare, inserire o invariate
	 * @param listaRegolarizzazioniDiCassa
	 * @param idOrdinativo
	 * @param datiOperazioneDto
	 * @return
	 */
    protected RegolarizzazioniDiCassaInModificaInfoDto valutaRegolarizzazioniDiCassa(List<RegolarizzazioneProvvisorio> listaRegolarizzazioniDiCassa, Integer idOrdinativo, DatiOperazioneDto datiOperazioneDto){
		RegolarizzazioniDiCassaInModificaInfoDto info = new RegolarizzazioniDiCassaInModificaInfoDto();
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		
		ArrayList<SiacROrdinativoProvCassaFin> regolarizzazioniDiCassaInvariate = new ArrayList<SiacROrdinativoProvCassaFin>();
		ArrayList<RegolarizzazioneProvvisorio> regolarizzazioniDiCassaDaInserire = new ArrayList<RegolarizzazioneProvvisorio>();
		ArrayList<RegolarizzazioneProvvisorio> regolarizzazioniDiCassaDaModificare = new ArrayList<RegolarizzazioneProvvisorio>();
		
		List<SiacROrdinativoProvCassaFin> regolarizzazioniDiCassaOld = siacROrdinativoProvCassaRepository.findROrdinativoProvCassaByIdOrdinativo(idEnte,idOrdinativo,datiOperazioneDto.getTs());

		ArrayList<Integer> listaIdRicevutiDalFe = new ArrayList<Integer>();

		//Primo ciclo per valutare gli inseriti nuovi e i modificati
		if(listaRegolarizzazioniDiCassa!=null && listaRegolarizzazioniDiCassa.size() > 0){
			for(RegolarizzazioneProvvisorio regolarizzazioneProvvisorioIterato : listaRegolarizzazioniDiCassa){
				//Se non ha un uid vuol dire che e' nuovo
				if((regolarizzazioneProvvisorioIterato.getIdRegolarizzazioneProvvisorio()==null || regolarizzazioneProvvisorioIterato.getIdRegolarizzazioneProvvisorio().intValue()==0)){
					regolarizzazioniDiCassaDaInserire.add(regolarizzazioneProvvisorioIterato);
				} else {
					listaIdRicevutiDalFe.add(regolarizzazioneProvvisorioIterato.getIdRegolarizzazioneProvvisorio());
					//Valutiamo se ha subito modifiche:
					SiacROrdinativoProvCassaFin siacROrdinativoProvCassaConsiderato = siacROrdinativoProvCassaRepository.findOne(regolarizzazioneProvvisorioIterato.getIdRegolarizzazioneProvvisorio());
					boolean isModificato = isModificato(regolarizzazioneProvvisorioIterato, siacROrdinativoProvCassaConsiderato, datiOperazioneDto);
					if(isModificato){
						regolarizzazioniDiCassaDaModificare.add(regolarizzazioneProvvisorioIterato);
					} else {
						regolarizzazioniDiCassaInvariate.add(siacROrdinativoProvCassaConsiderato);
					}
				}
			}
		}

		ArrayList<SiacROrdinativoProvCassaFin> regolarizzazioniDiCassaDaEliminare = new ArrayList<SiacROrdinativoProvCassaFin>();

		if(regolarizzazioniDiCassaOld!=null && regolarizzazioniDiCassaOld.size()>0){
			for(SiacROrdinativoProvCassaFin iterateOld : regolarizzazioniDiCassaOld){
				Integer uuidOld = iterateOld.getUid();
				if(!listaIdRicevutiDalFe.contains(uuidOld)){
					//Non e' presente e va eliminato
					regolarizzazioniDiCassaDaEliminare.add(iterateOld);
				}
			}
		}

		info.setRegolarizzazioniDiCassaInvariate(regolarizzazioniDiCassaInvariate);
		info.setRegolarizzazioniDiCassaDaEliminare(regolarizzazioniDiCassaDaEliminare);
		info.setRegolarizzazioniDiCassaDaInserire(regolarizzazioniDiCassaDaInserire);
		info.setRegolarizzazioniDiCassaDaModificare(regolarizzazioniDiCassaDaModificare);
		info.setRegolarizzazioniDiCassaOld(regolarizzazioniDiCassaOld);
		info.setListaIdModificati(listaIdRicevutiDalFe);
		
		return info;
	}
    
    /**
     * Si occupa di valutare le modifiche riguardanti i subordinativi, individuando
	 * quelle da aggiornare, eliminare, inserire o invariate
     * @param listaSubOrdinativi
     * @param idTOrdinativo
     * @param datiOperazioneDto
     * @param bilancio
     * @param richiedente
     * @return
     */
	public SubOrdinativoInModificaInfoDto valutaSubOrdinativi(List<S> listaSubOrdinativi, Integer idTOrdinativo, DatiOperazioneDto datiOperazioneDto,
			Bilancio bilancio, Richiedente richiedente, String tipoOrdinativo, Ente ente){
		SubOrdinativoInModificaInfoDto info = new SubOrdinativoInModificaInfoDto();
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		ArrayList<SiacTOrdinativoTFin> subOrdinativiInvariati = new ArrayList<SiacTOrdinativoTFin>();
		ArrayList<SubOrdinativo> subOrdinativiDaInserire = new ArrayList<SubOrdinativo>();
		ArrayList<SubOrdinativo> subOrdinativiDaModificare = new ArrayList<SubOrdinativo>();
		ArrayList<SubOrdinativo> subOrdinativiDaModificarePerImportoAumentato = new ArrayList<SubOrdinativo>();
		ArrayList<SubOrdinativo> subOrdinativiDaModificarePerImportoDiminuito = new ArrayList<SubOrdinativo>();
		ArrayList<SubOrdinativoImportoVariatoDto> subOrdinativiPerImportoVariato = new ArrayList<SubOrdinativoImportoVariatoDto>();
		ArrayList<SubOrdinativoImportoVariatoDto> subOrdinativiNuoviPerImportoVariato = new ArrayList<SubOrdinativoImportoVariatoDto>();
		ArrayList<SubOrdinativoImportoVariatoDto> subOrdinativiEliminatiPerImportoVariato = new ArrayList<SubOrdinativoImportoVariatoDto>();
		
		List<SiacTOrdinativoTFin> subOrdinativiOld = null;
		if(idTOrdinativo!=null){
			subOrdinativiOld = siacTOrdinativoTRepository.findSubOrdinativiByOrdinativo(idTOrdinativo, datiOperazioneDto.getTs(),idEnte);
		}
		
		ArrayList<Integer> listaIdRicevutiDalFe = new ArrayList<Integer>();
		
		//Primo ciclo per valutare gli inseriti nuovi e i modificati
		if(listaSubOrdinativi!=null && listaSubOrdinativi.size()>0){
			for(SubOrdinativo subOrdinativoIterato : listaSubOrdinativi){
				//Se non ha un uid vuol dire che e' nuovo
				//if( (subOrdinativoIterato.getNumero()==null || subOrdinativoIterato.getNumero().intValue()==0) && subOrdinativoIterato.getUid()==0){
				if( subOrdinativoIterato.getUid()==0){
					subOrdinativiDaInserire.add(subOrdinativoIterato);
					BigDecimal delta = getDeltaVariazioneImporto(subOrdinativoIterato,null,datiOperazioneDto);
					subOrdinativiNuoviPerImportoVariato.add(new SubOrdinativoImportoVariatoDto(subOrdinativoIterato, delta));
				} else {
					listaIdRicevutiDalFe.add(subOrdinativoIterato.getUid());
					//Valutiamo se ha subito modifiche:
					SiacTOrdinativoTFin siacTOrdinativoTsConsiderato = siacTOrdinativoTRepository.findOne(subOrdinativoIterato.getUid());
					boolean isModificato = isModificato(subOrdinativoIterato,siacTOrdinativoTsConsiderato,datiOperazioneDto);
					if(isModificato){
						subOrdinativiDaModificare.add(subOrdinativoIterato);
						int comparazioneImporto = compareVariazioneImporto(subOrdinativoIterato,siacTOrdinativoTsConsiderato,datiOperazioneDto);
						if(comparazioneImporto>0){
							subOrdinativiDaModificarePerImportoAumentato.add(subOrdinativoIterato);
						} else if(comparazioneImporto<0){
							subOrdinativiDaModificarePerImportoDiminuito.add(subOrdinativoIterato);
						}
						if(comparazioneImporto!=0){
							BigDecimal delta = getDeltaVariazioneImporto(subOrdinativoIterato,siacTOrdinativoTsConsiderato,datiOperazioneDto);
							subOrdinativiPerImportoVariato.add(new SubOrdinativoImportoVariatoDto(subOrdinativoIterato, delta));
						}
					} else {
						subOrdinativiInvariati.add(siacTOrdinativoTsConsiderato);
					}
				}
			}
		}
		
		ArrayList<SiacTOrdinativoTFin> subOrdinativiDaEliminare = new ArrayList<SiacTOrdinativoTFin>();
		
		int maxCode = 1;
		
		if(subOrdinativiOld!=null && subOrdinativiOld.size()>0){
			for(SiacTOrdinativoTFin iterateOld : subOrdinativiOld){
				Integer uuidOld = iterateOld.getUid();
				if(!listaIdRicevutiDalFe.contains(uuidOld)){
					//Non e' presente e va eliminato
					subOrdinativiDaEliminare.add(iterateOld);
					BigDecimal delta = getDeltaVariazioneImporto(null,iterateOld,datiOperazioneDto);
					
					SubOrdinativo subOrdPag = null;
					if(Constanti.ORDINATIVO_TIPO_PAGAMENTO.equals(tipoOrdinativo)){
						subOrdPag = caricaSubOrdinativoPagamento(iterateOld, null, datiOperazioneDto, richiedente, bilancio.getAnno());
					} else {
						subOrdPag = caricaSubOrdinativoIncasso(iterateOld, null, datiOperazioneDto, richiedente, bilancio.getAnno(), ente, null);
					}
					
					
					subOrdinativiEliminatiPerImportoVariato.add(new SubOrdinativoImportoVariatoDto(subOrdPag, delta));
				}
				try{
					Integer codeInt = new Integer(iterateOld.getOrdTsCode());
					if(codeInt>maxCode){
						maxCode = codeInt;
					}
				}catch(NumberFormatException nfe){
				}
			}
		}
		
		info.setSubOrdinativiDaModificarePerImportoAumentato(subOrdinativiDaModificarePerImportoAumentato);
		info.setSubOrdinativiDaModificarePerImportoDiminuito(subOrdinativiDaModificarePerImportoDiminuito);
		info.setSubOrdinativiModificatiConImportoVariato(subOrdinativiPerImportoVariato);
		info.setSubOrdinativiNuoviConImportoVariato(subOrdinativiNuoviPerImportoVariato);
		info.setSubOrdinativiEliminatiConImportoVariato(subOrdinativiEliminatiPerImportoVariato);
		info.setSubOrdinativiDaEliminare(subOrdinativiDaEliminare);
		info.setSubOrdinativiDaInserire(subOrdinativiDaInserire);
		info.setSubOrdinativiDaModificare(subOrdinativiDaModificare);
		info.setSubOrdinativiOld(subOrdinativiOld);
		info.setListaIdModificati(listaIdRicevutiDalFe);
		info.setMaxCodeOld(maxCode);
		info.setSubOrdinativiInvariati(subOrdinativiInvariati);
		return info;
	}
	
	/**
	 * verifica se l'ordinativo e' stato modificato
	 * @param subOrdinativo
	 * @param siacTOrdinativoTs
	 * @param datiOperazioneDto
	 * @return
	 */
	private boolean isModificato(SubOrdinativo subOrdinativo,SiacTOrdinativoTFin siacTOrdinativoTs,DatiOperazioneDto datiOperazioneDto) {
		boolean isModificato = false;
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		Integer idSiacTOrdinativoTs = siacTOrdinativoTs.getOrdTsId();
		Timestamp now = datiOperazioneDto.getTs();
		
//		boolean isPagamento = isPagamento(subOrdinativo);
		
		//IMPORTO:
		List<SiacTOrdinativoTsDetFin> importoAttualeList = siacTOrdinativoTsDetRepository.findValidoByTipo(idEnte, now, Constanti.D_ORDINATIVO_TS_DET_TIPO_IMPORTO_ATTUALE, idSiacTOrdinativoTs);
		SiacTOrdinativoTsDetFin siacTOrdinativoTsDet = CommonUtils.getFirst(importoAttualeList);
		BigDecimal importoOld = siacTOrdinativoTsDet.getOrdTsDetImporto();
		BigDecimal importoNew = subOrdinativo.getImportoAttuale();
		if(!StringUtils.sonoUguali(importoOld, importoNew)){
			isModificato = true;
			return isModificato;
		}
			
		//DATA:
		Date dataScadenza = getDataScadenza(subOrdinativo);
		Timestamp dataScadenzaNew = TimingUtils.convertiDataInTimeStamp(dataScadenza);
		Timestamp dataScadenzaOld = siacTOrdinativoTs.getOrdTsDataScadenza();
		if(!StringUtils.sonoUguali(dataScadenzaNew, dataScadenzaOld)){
			isModificato = true;
			return isModificato;
		}
		
		// descrizione
		String descrizioneOld = siacTOrdinativoTs.getOrdTsDesc();
		String descrizioneNew = subOrdinativo.getDescrizione();
		
		if(!StringUtils.sonoUguali(descrizioneOld, descrizioneNew)){
			isModificato = true;
			return isModificato;
		}
			
		return isModificato;
	}
	
	/**
	 * Serve per capire se c'e' stata una variazione di importo per il subordinativo indicato
	 * @param subOrdinativo
	 * @param siacTOrdinativoTs
	 * @param datiOperazioneDto
	 * @return
	 */
	private int compareVariazioneImporto(SubOrdinativo subOrdinativo,SiacTOrdinativoTFin siacTOrdinativoTs,DatiOperazioneDto datiOperazioneDto) {
		int isAumentato = 0;
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		Integer idSiacTOrdinativoTs = siacTOrdinativoTs.getOrdTsId();
		Timestamp now = datiOperazioneDto.getTs();
		//IMPORTO:
		List<SiacTOrdinativoTsDetFin> importoAttualeList = siacTOrdinativoTsDetRepository.findValidoByTipo(idEnte, now, Constanti.D_ORDINATIVO_TS_DET_TIPO_IMPORTO_ATTUALE, idSiacTOrdinativoTs);
		SiacTOrdinativoTsDetFin siacTOrdinativoTsDet = CommonUtils.getFirst(importoAttualeList);
		BigDecimal importoOld = siacTOrdinativoTsDet.getOrdTsDetImporto();
		BigDecimal importoNew = subOrdinativo.getImportoAttuale();
		if(importoOld!=null && importoNew!=null){
			isAumentato = importoNew.compareTo(importoOld);
		}
		return isAumentato;
	}
	
	/**
	 * in caso di modifica passare in input SubOrdinativo subOrdinativo,SiacTOrdinativoTFin siacTOrdinativoTs entrambi diversi da null
	 * in caso di suordinativo nuovo passare in input SubOrdinativo subOrdinativo valorizzato e SiacTOrdinativoTFin siacTOrdinativoTs NULL
	 * in caso di subordinarivo eliminato passare in input SubOrdinativo subOrdinativo NULL e SiacTOrdinativoTFin siacTOrdinativoTs valorizzato
	 * @param subOrdinativo
	 * @param siacTOrdinativoTs
	 * @param datiOperazioneDto
	 * @return
	 */
	private BigDecimal getDeltaVariazioneImporto(SubOrdinativo subOrdinativo,SiacTOrdinativoTFin siacTOrdinativoTs,DatiOperazioneDto datiOperazioneDto) {
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		Timestamp now = datiOperazioneDto.getTs();
		//IMPORTO:
		BigDecimal importoOld = BigDecimal.ZERO;
		if(siacTOrdinativoTs!=null){
			Integer idSiacTOrdinativoTs = siacTOrdinativoTs.getOrdTsId();
			List<SiacTOrdinativoTsDetFin> importoAttualeList = siacTOrdinativoTsDetRepository.findValidoByTipo(idEnte, now, Constanti.D_ORDINATIVO_TS_DET_TIPO_IMPORTO_ATTUALE, idSiacTOrdinativoTs);
			SiacTOrdinativoTsDetFin siacTOrdinativoTsDet = CommonUtils.getFirst(importoAttualeList);
			importoOld = siacTOrdinativoTsDet.getOrdTsDetImporto();
		}
		BigDecimal importoNew = BigDecimal.ZERO;
		if(subOrdinativo!=null){
			//questa cosa non puo' funzionare. Suppongo che dovrebbe associare l'importo attuale all'importoNew, ma visto che non si e' mai verificato questo problema lascio
			subOrdinativo.getImportoAttuale();
		}
		BigDecimal delta = importoNew.subtract(importoOld);
		return delta;
	}

	/**
	 * Serve per valutare se la regolarizzazione ha subito modifiche
	 * @param regolarizzazioneProvvisorio
	 * @param siacROrdinativoProvCassa
	 * @param datiOperazioneDto
	 * @return
	 */
	private boolean isModificato(RegolarizzazioneProvvisorio regolarizzazioneProvvisorio, SiacROrdinativoProvCassaFin siacROrdinativoProvCassa, DatiOperazioneDto datiOperazioneDto) {
		boolean isModificato = false;
		
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		Integer idSiacROrdinativoProvCassa = siacROrdinativoProvCassa.getOrdProvcId();
		Timestamp now = datiOperazioneDto.getTs();
		
		//IMPORTO:
		SiacROrdinativoProvCassaFin siacROrdinativoProvCassaDb = siacROrdinativoProvCassaRepository.findSiacROrdinativoProvCassaValidoById(idEnte,idSiacROrdinativoProvCassa,now);
				
		BigDecimal importoOld = siacROrdinativoProvCassaDb.getOrdProvcImporto();
		BigDecimal importoNew = regolarizzazioneProvvisorio.getImporto();
		
		if(!StringUtils.sonoUguali(importoOld, importoNew)){
			isModificato = true;
			return isModificato;
		}

		return isModificato;
	}
	
	/**
	 * Si occupa dell'update a SiacTOrdinativoFin e tabelle accessorie
	 * @param bilancio
	 * @param ordinativo
	 * @param tipoOrdinativo
	 * @param ordinativoInModificaInfoDto
	 * @param datiOperazioneDto
	 * @return
	 */
	private SiacTOrdinativoFin aggiornaSiacTOrdinativo(Bilancio bilancio, Ordinativo ordinativo, String tipoOrdinativo, OrdinativoInModificaInfoDto ordinativoInModificaInfoDto, DatiOperazioneDto datiOperazioneDto, Ente ente){
		
		OrdinativoPagamento ordinativoDiPagamento = null;
		OrdinativoIncasso ordinativoDiIncasso = null;
		if(ordinativo instanceof OrdinativoPagamento){
			ordinativoDiPagamento = (OrdinativoPagamento) ordinativo;
		} else {
			ordinativoDiIncasso = (OrdinativoIncasso) ordinativo;
		}
		boolean diPagamento = isPagamento(ordinativo);
		
		//leggo l'id ente:
		int idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		
		//carico l'ambito:
		SiacDAmbitoFin  siacDAmbito = siacDAmbitoRepository.findAmbitoByCode(Constanti.AMBITO_FIN, idEnte);
		datiOperazioneDto.setSiacDAmbito(siacDAmbito);
		
		// Se anno_ordinativo non istanziato o anno_ordinativo = 0, setto anno_ordinativo = anno_esercizio
		//aggiorna i dati direttamente presenti nella tabella siac_t_ordinativo:
		SiacTOrdinativoFin siacTOrdinativoAggiornato = 
				aggiornaSiacTOrdinativoMain(bilancio, ordinativo, tipoOrdinativo, ordinativoInModificaInfoDto, datiOperazioneDto,ente);
		
		// siac_r_ordinativo_stato + siac_d_ordinativo_stato : inizio
		SiacROrdinativoStatoFin statoR = salvaSiacROrdinativoStato(ordinativo, datiOperazioneDto, siacTOrdinativoAggiornato);
		siacTOrdinativoAggiornato.setSiacROrdinativoStatos(toList(statoR));
		// siac_r_ordinativo_stato + siac_d_ordinativo_stato : fine
		
		//aggiorna la transazione elemntare
		AttributoTClassInfoDto attributoInfo = new AttributoTClassInfoDto();
		attributoInfo.setTipoOggetto(OggettoDellAttributoTClass.T_ORDINATIVO);
		attributoInfo.setSiacTOrdinativo(siacTOrdinativoAggiornato);
		
		//in fase di aggiorna la transazione elementare non e' piu' necessaria
		salvaClassificatori(attributoInfo, datiOperazioneDto, ordinativo);
		
		ClassificatoreStipendi cs = ordinativo.getClassificatoreStipendi();
		
		relOrdinativoClassificatore(siacTOrdinativoAggiornato.getOrdId(), cs != null && cs.getUid() > 0? cs.getUid() : null, datiOperazioneDto);
		
		// tipo_avviso
		if(ordinativo.getTipoAvviso()!=null){
			salvaAttributoTClass(datiOperazioneDto, attributoInfo, ordinativo.getTipoAvviso().getCodice(), Constanti.D_CLASS_TIPO_TIPO_AVVISO);
		}
		
		//SOGGETTO E SEDI:
		if(diPagamento){
			//gestione soggetto/sede-mod pag
			SiacROrdinativoModpagFin siacROrdinativoModpag = gestisciSoggSediModalitaPag(ordinativoDiPagamento, datiOperazioneDto, siacTOrdinativoAggiornato);
			if(siacROrdinativoModpag!=null){
				siacTOrdinativoAggiornato.setSiacROrdinativoModpags(toList(siacROrdinativoModpag));
			}
			
		} else {
			
			//gestione soggeddo/sede
			SiacROrdinativoSoggettoFin siacROrdinativoSoggetto = gestisciSoggSede(ordinativoDiIncasso, datiOperazioneDto, siacTOrdinativoAggiornato);
				if(siacROrdinativoSoggetto!=null){
					siacTOrdinativoAggiornato.setSiacROrdinativoSoggettos(toList(siacROrdinativoSoggetto));
			}
		}

		// siac_r_ordinativo_attr + siac_t_attr : inizio
		salvaAttributoTAttr(attributoInfo, datiOperazioneDto, ordinativo.getNote(), Constanti.ORDINATIVO_T_ATTR_NOTE);
		
		// flag cartaceo
		salvaAttributoTAttr(attributoInfo, datiOperazioneDto, ordinativo.isFlagAllegatoCartaceo(), Constanti.ORDINATIVO_T_ATTR_FLAG_ALLEGATO_CARTACEO);
		
		//refresh per entity hibernate:
		aggiornaAllAttrValidiInEntityJPA(attributoInfo, datiOperazioneDto);
		aggiornaAllRClassValidiInEntityJPA(attributoInfo, datiOperazioneDto);
		
		return siacTOrdinativoAggiornato;		
	}
	
	/**
	 * aggiorna i dati direttamente presenti nella tabella siac_t_ordinativo
	 * @param ente
	 * @param richiedente
	 * @param bilancio
	 * @param ordinativoDiPagamento
	 * @param tipoOrdinativo
	 * @param ordinativoInModificaInfoDto
	 * @return
	 */
	private SiacTOrdinativoFin aggiornaSiacTOrdinativoMain(Bilancio bilancio, Ordinativo ordinativo, String tipoOrdinativo,
			OrdinativoInModificaInfoDto ordinativoInModificaInfoDto, DatiOperazioneDto datiOperazioneDto, Ente ente){

		// Se anno_ordinativo non istanziato o anno_ordinativo = 0, setto anno_ordinativo = anno_esercizio
		if(null==ordinativo.getAnno() || (null!=ordinativo.getAnno() && ordinativo.getAnno().intValue() == 0)){
			ordinativo.setAnno(bilancio.getAnno());
		}
		
		//ricarichiamo i dati da db:
		SiacTOrdinativoFin siacTOrdinativoDaAggiornare = ordinativoInModificaInfoDto.getSiacTOrdinativo();
		
		if(!StringUtils.isEmpty(ordinativo.getDescrizione())){
			siacTOrdinativoDaAggiornare.setOrdDesc(ordinativo.getDescrizione());
		}

		if(Constanti.D_ORDINATIVO_STATO_TRASMESSO.equalsIgnoreCase(Constanti.statoOperativoOrdinativoEnumToString(ordinativo.getStatoOperativoOrdinativo()))){
			siacTOrdinativoDaAggiornare.setOrdVariazioneData(datiOperazioneDto.getTs());
		}
		
		//CAUSALE:
		if(ordinativo instanceof OrdinativoIncasso){
			siacTOrdinativoDaAggiornare = impostaCausaleOrdinativoIncasso((OrdinativoIncasso)ordinativo, datiOperazioneDto, siacTOrdinativoDaAggiornare);
		}

		siacTOrdinativoDaAggiornare = impostaCodificheDOrdinativo(siacTOrdinativoDaAggiornare, ordinativo, tipoOrdinativo, datiOperazioneDto, false);
		
		siacTOrdinativoDaAggiornare.setOrdBeneficiariomult(ordinativo.isFlagBeneficiMultiplo());
		
		// SIAC-6175
		siacTOrdinativoDaAggiornare.setOrdDaTrasmettere(Boolean.valueOf(ordinativo.isDaTrasmettere()));
		
		siacTOrdinativoDaAggiornare = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTOrdinativoDaAggiornare, datiOperazioneDto, siacTAccountRepository);
		//salvo sul db:
		siacTOrdinativoDaAggiornare = siacTOrdinativoRepository.saveAndFlush(siacTOrdinativoDaAggiornare);

		return siacTOrdinativoDaAggiornare;		
	}
	
	/**
	 * metodo da richiamare ad inizio del servizio di aggiorna ordinativo, serve per salvare nell'oggetto OrdinativoInModificaInfoDto
	 * i dati che richiedono interrogazione del database e che conviene quindi invocare una sola volta all'inizio e passarli
	 * di mano tra i vari metodi che compongono l'aggiorna ordinativo per evitare di ricaricarli in continuazione
	 * @return
	 */
	public OrdinativoInModificaInfoDto getDatiGeneraliOrdinativoInAggiornamento(Ordinativo ordinativo, DatiOperazioneDto datiOperazioneDto, Bilancio bilancio, Richiedente richiedente, Ente ente){
		OrdinativoInModificaInfoDto ordinativoInModificaInfoDto = new OrdinativoInModificaInfoDto();
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		
		String dOrdinativoTipo = getDOrdinativoTipo(ordinativo);
		
		boolean isPagamento = isPagamento(ordinativo);
		
		// nb
		// 18/04/2014 : Sarebbe meglio chiamare il servizio ricercaOrdinativoDiPagamentoPerChiave
		// per poter cosi' avere l'oggetto ordinativoDiPagamento completo e con tutte le eventuali decodifiche
		// In questo momento pero', non e' ancora disponibile.
		SiacTOrdinativoFin siacTOrdinativo = siacTOrdinativoRepository.findOrdinativoValidoByAnnoAndNumeroAndTipo(idEnte,ordinativo.getAnno(),
																										       new BigDecimal(ordinativo.getNumero()),
																										       dOrdinativoTipo,
		                                                                                                       datiOperazioneDto.getTs());
		if(siacTOrdinativo!=null && siacTOrdinativo.getUid()!=null){
			// 14/06/2014 : Aggiunta la chiamata al metodo ricercaOrdinativo
			
			 RicercaOrdinativoPerChiaveDto ordInfo = null;
			
			 OrdinativoPagamento ordinativoPagamentoEstratto = null;
			 OrdinativoIncasso ordinativoIncassoEstratto = null;
			 
			 if(isPagamento){
			 OrdinativoPagamento ordinativoPagamentoDaEstrarre = new OrdinativoPagamento();
			 ordinativoPagamentoDaEstrarre.setAnno(Integer.valueOf(ordinativo.getAnno()));
			 ordinativoPagamentoDaEstrarre.setNumero(Integer.valueOf(ordinativo.getNumero()));
			 RicercaOrdinativoPagamentoK ropk = new RicercaOrdinativoPagamentoK();
	         ropk.setBilancio(bilancio);
	         ropk.setOrdinativoPagamento(ordinativoPagamentoDaEstrarre);
		         ordInfo = this.ricercaOrdinativoPagamento(ropk, datiOperazioneDto, richiedente);
		         ordinativoPagamentoEstratto = ordInfo.getOrdinativoPagamento();
			 }else{
				 OrdinativoIncasso ordinativoIncassoDaEstrarre = new OrdinativoIncasso();
				 ordinativoIncassoDaEstrarre.setAnno(Integer.valueOf(ordinativo.getAnno()));
				 ordinativoIncassoDaEstrarre.setNumero(Integer.valueOf(ordinativo.getNumero()));
				 RicercaOrdinativoIncassoK ripk = new RicercaOrdinativoIncassoK();
				 ripk.setBilancio(bilancio);
				 ripk.setOrdinativoIncasso(ordinativoIncassoDaEstrarre);
		         ordInfo = this.ricercaOrdinativoIncasso(ripk, datiOperazioneDto, richiedente);
		         ordinativoIncassoEstratto = ordInfo.getOrdinativoIncasso();
				 
			 }
	         List<SubOrdinativoPagamento> listaSubPag = null;
	         List<SubOrdinativoIncasso> listaSubInc = null;
	         SubOrdinativoInModificaInfoDto infoModificheSubOrdinativi = null;
	         
	         if(isPagamento){
	        	 listaSubPag = ((OrdinativoPagamento)ordinativo).getElencoSubOrdinativiDiPagamento();
	        	 infoModificheSubOrdinativi = valutaSubOrdinativi((List<S>) listaSubPag, siacTOrdinativo.getOrdId(),datiOperazioneDto,bilancio,richiedente,Constanti.ORDINATIVO_TIPO_PAGAMENTO,ente);
	         } else {
	        	 listaSubInc = ((OrdinativoIncasso)ordinativo).getElencoSubOrdinativiDiIncasso();
	        	 infoModificheSubOrdinativi = valutaSubOrdinativi((List<S>) listaSubInc, siacTOrdinativo.getOrdId(),datiOperazioneDto,bilancio,richiedente,Constanti.ORDINATIVO_TIPO_INCASSO,ente);
	         }
	        
	        RegolarizzazioniDiCassaInModificaInfoDto infoRegolarizzazioniDiCassa = valutaRegolarizzazioniDiCassa(ordinativo.getElencoRegolarizzazioneProvvisori(), siacTOrdinativo.getOrdId(), datiOperazioneDto);
	        
			ordinativoInModificaInfoDto.setInfoModificheSubOrdinativi(infoModificheSubOrdinativi);
			ordinativoInModificaInfoDto.setSiacTOrdinativo(siacTOrdinativo);
		    
			if(isPagamento){
			ordinativoInModificaInfoDto.setOrdinativo(ordinativoPagamentoEstratto);
			}else{
				ordinativoInModificaInfoDto.setOrdinativo(ordinativoIncassoEstratto);
			}
		    
			ordinativoInModificaInfoDto.setInfoModificheRegolarizzazioniDiCassa(infoRegolarizzazioniDiCassa);
		}
		return ordinativoInModificaInfoDto;
	}
	
	/**
	 * Si occupa di aggiornare i dati in siac_t_ordinativo_ts e tabella accessorie
	 * @param bilancio
	 * @param siacTOrdinativo
	 * @param subOrdinativoDiPagamento
	 * @param tipoOrdinativo
	 * @param datiOperazioneDto
	 * @return
	 */
	private SiacTOrdinativoTFin aggiornaSiacTOrdinativoTs(Bilancio bilancio, SiacTOrdinativoFin siacTOrdinativo, SubOrdinativo subOrdinativo, String tipoOrdinativo,DatiOperazioneDto datiOperazioneDto){
		
		SiacTEnteProprietarioFin siacTEnteProprietario = datiOperazioneDto.getSiacTEnteProprietario();
		
		datiOperazioneDto.setOperazione(Operazione.MODIFICA);

		SiacTOrdinativoTFin siacTOrdinativoDaAggiornare = siacTOrdinativoTRepository.findOne(subOrdinativo.getUid());
		siacTOrdinativoDaAggiornare.setOrdTsDesc(subOrdinativo.getDescrizione());
		
		
			
		
		Date dataScadenza = getDataScadenza(subOrdinativo);
		if (dataScadenza != null) {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(dataScadenza);
			Timestamp tstamp = new Timestamp(calendar.getTimeInMillis());
			siacTOrdinativoDaAggiornare.setOrdTsDataScadenza(tstamp);
		}else{
			
			// RM 07/03/2017:  cambia la condizione --> la data di esecuzione pagamento si aggiorna anche con piu quote, SOLO SE PAGAMENTO
			// se l'ordinativo ha piu di una quota queste prendono il valore della prima quota 
			if(subOrdinativo instanceof SubOrdinativoIncasso ){
				
				if(siacTOrdinativo.getSiacTOrdinativoTs()!=null && siacTOrdinativo.getSiacTOrdinativoTs().size()==1){
					siacTOrdinativoDaAggiornare.setOrdTsDataScadenza(null);
				}
				
			}else{
				
				if(siacTOrdinativo.getSiacTOrdinativoTs()!=null){
					
					siacTOrdinativoDaAggiornare.setOrdTsDataScadenza(null);
				}
			}
		}
		
		siacTOrdinativoDaAggiornare.setSiacTEnteProprietario(siacTEnteProprietario);
		
		siacTOrdinativoDaAggiornare = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTOrdinativoDaAggiornare, datiOperazioneDto,siacTAccountRepository);
		//salvo sul db:
		siacTOrdinativoDaAggiornare = siacTOrdinativoTRepository.saveAndFlush(siacTOrdinativoDaAggiornare);

		// importo iniziale : inizio
		// salvaImportoOrdinativoTs(siacTOrdinativoTsInsert, datiOperazioneDto, subOrdinativoDiPagamento.getImportoIniziale(), Constanti.D_ORDINATIVO_TS_DET_TIPO_IMPORTO_INIZIALE);
		// importo iniziale : fine

		// importo attuale : inizio
		SiacTOrdinativoTsDetFin importo = salvaImportoOrdinativoTs(siacTOrdinativoDaAggiornare, datiOperazioneDto, subOrdinativo.getImportoAttuale(), Constanti.D_ORDINATIVO_TS_DET_TIPO_IMPORTO_ATTUALE);
		siacTOrdinativoDaAggiornare.setSiacTOrdinativoTsDets(toList(importo));
		// importo attuale : fine
		
		// liquidazione : inizio
		// salvaSiacRLiquidazioneOrd(siacTOrdinativoDaAggiornare, datiOperazioneDto,subOrdinativoDiPagamento.getLiquidazione().getUid());
		// liquidazione : fine
		
		return siacTOrdinativoDaAggiornare;		
	}
	
	/**
	 * gestisce l'inserimento e l'update del soggetto/sede secondaria e modalita pagamento
	 * @param ordinativo
	 * @param datiOperazioneDto
	 * @param siacTOrdinativo
	 */
	private SiacROrdinativoModpagFin gestisciSoggSediModalitaPag(OrdinativoPagamento ordinativo, DatiOperazioneDto datiOperazioneDto, SiacTOrdinativoFin siacTOrdinativo){
		SediEModPagOrdinativoDto sediEModPagOrdinativoDto = leggiSedeSecondariaEModPagDaFrontEnd(ordinativo,datiOperazioneDto);
		SiacROrdinativoModpagFin siacROrdinativoModpag = null;
		
		//il soggetto da relazionare potra' essere la sede secondaria o il soggetto stesso:
		SiacTSoggettoFin siacTSoggettoDaRelazionare = sediEModPagOrdinativoDto.getSoggettoDaRelazionare();
		salvaSiacROrdinativoSoggetto(datiOperazioneDto, siacTOrdinativo, siacTSoggettoDaRelazionare);
		
		if(sediEModPagOrdinativoDto.getSiacTModpagOrdinativo()!= null){
			SiacTModpagFin siacTModpagOrdinativo = sediEModPagOrdinativoDto.getSiacTModpagOrdinativo();
			siacROrdinativoModpag = salvaSiacROrdinativoModpag(datiOperazioneDto, siacTOrdinativo, siacTModpagOrdinativo);
		}
		return siacROrdinativoModpag;
	}
	
	

	
	/**
	 * gestisce l'inserimento e l'update del soggetto/sede secondaria 
	 * @param ordinativo
	 * @param datiOperazioneDto
	 * @param siacTOrdinativo
	 */
	private SiacROrdinativoSoggettoFin gestisciSoggSede(OrdinativoIncasso ordinativo, DatiOperazioneDto datiOperazioneDto, SiacTOrdinativoFin siacTOrdinativo){
		SediEModPagOrdinativoDto sediEModPagOrdinativoDto = leggiSedeSecondariaEModPagDaFrontEnd(ordinativo,datiOperazioneDto);
		//il soggetto da relazionare potra' essere la sede secondaria o il soggetto stesso:
		SiacTSoggettoFin siacTSoggettoDaRelazionare = sediEModPagOrdinativoDto.getSoggettoDaRelazionare();
		return salvaSiacROrdinativoSoggetto(datiOperazioneDto, siacTOrdinativo, siacTSoggettoDaRelazionare);
	}
	
	/**
	 * interpreta i dati ricevuti dal front end in merito a Soggetto e Sedi Secondarie
	 * @param ordinativo
	 * @param datiOperazioneDto
	 * @return
	 */
	private SediEModPagOrdinativoDto leggiSedeSecondariaEModPagDaFrontEnd(OrdinativoPagamento ordinativo, DatiOperazioneDto datiOperazioneDto){
		
		SediEModPagOrdinativoDto sediEModPagOrdinativoDto = new SediEModPagOrdinativoDto();
		String methodName = "leggiSedeSecondariaEModPagDaFrontEnd";
		
		SiacTSoggettoFin siacTSoggetto = null;
		SiacTSoggettoFin siacTSoggettoSedeSecondaria = null;
		SiacTModpagFin siacTModpagOrdinativo = null;
		
		int idEnte = datiOperazioneDto.getSiacTEnteProprietario().getUid();
		
		//SI DETERMINA COSA E' ARRIVATO SE MOD PAG DI UNA SEDE O DEL SOGGETTO STESSO:
		siacTSoggetto = siacTSoggettoRepository.ricercaSoggettoNoSeSede(Constanti.AMBITO_FIN,idEnte, ordinativo.getSoggetto().getCodiceSoggetto(), Constanti.SEDE_SECONDARIA, getNow());
		
		if(ordinativo.getSoggetto().getElencoModalitaPagamento()!= null && ordinativo.getSoggetto().getElencoModalitaPagamento().size()>0){
			
			Integer uidModpag = ordinativo.getSoggetto().getElencoModalitaPagamento().get(0).getUid();
			ModalitaAccreditoSoggetto modalitaAccreditoSoggetto = ordinativo.getSoggetto().getElencoModalitaPagamento().get(0).getModalitaAccreditoSoggetto();
			
			log.debug(methodName, " uidModpag: " + uidModpag==null ? "uid nullo ": uidModpag );
			log.debug(methodName, " modalitaAccreditoSoggetto.getCodice: " + (!modalitaAccreditoSoggetto.getCodice().isEmpty()? modalitaAccreditoSoggetto.getCodice(): "tipo accredito nullo" ));
			
			SiacTSoggettoFin soggettoModalitaPagamento = null;
			if(!modalitaAccreditoSoggetto.getCodice().equals(TipoAccredito.CSI.toString()) &&
					!modalitaAccreditoSoggetto.getCodice().equals(TipoAccredito.FA.toString()) && 
					!modalitaAccreditoSoggetto.getCodice().equals(TipoAccredito.PI.toString())&&
					!modalitaAccreditoSoggetto.getCodice().equals(TipoAccredito.CPT.toString())&&
					!modalitaAccreditoSoggetto.getCodice().equals(TipoAccredito.CSIG.toString())&&
					!modalitaAccreditoSoggetto.getCodice().equals(TipoAccredito.SU.toString())&&
					!modalitaAccreditoSoggetto.getCodice().equals(TipoAccredito.CSC.toString())){
				
				siacTModpagOrdinativo = siacTModpagRepository.findOne(uidModpag);
				soggettoModalitaPagamento = siacTModpagOrdinativo.getSiacTSoggetto();
				
			}else{
				
				SiacRSoggettoRelazFin siacRSoggettoRelazFin = siacRSoggettoRelazFinRepository.findOne(uidModpag);
				soggettoModalitaPagamento = siacRSoggettoRelazFin.getSiacTSoggetto1();
				
				siacTModpagOrdinativo = new SiacTModpagFin();
				SiacDAccreditoTipoFin siacDAccreditoTipo = new SiacDAccreditoTipoFin();
				siacDAccreditoTipo.setAccreditoTipoCode(modalitaAccreditoSoggetto.getCodice());
				siacTModpagOrdinativo.setUid(uidModpag);
				siacTModpagOrdinativo.setSiacDAccreditoTipo(siacDAccreditoTipo);
				
			}
			
			boolean isSedeSec = soggettoDad.isSedeSecondaria(soggettoModalitaPagamento.getSoggettoId(),idEnte);
			if(isSedeSec){
				siacTSoggettoSedeSecondaria = soggettoModalitaPagamento;
			}
			
			sediEModPagOrdinativoDto.setSiacTModpagOrdinativo(siacTModpagOrdinativo);
		}
		
		sediEModPagOrdinativoDto.setSiacTSedeSecondariaOrdine(siacTSoggettoSedeSecondaria);
		sediEModPagOrdinativoDto.setSiacTSoggettoOrdine(siacTSoggetto);
		return sediEModPagOrdinativoDto;
	}
	
	/**
	 * analizza il model ricevuto dal front end e ne carica le sedi e mod pag indicate dentro
	 * @param ordinativo
	 * @param datiOperazioneDto
	 * @return
	 */
	private SediEModPagOrdinativoDto leggiSedeSecondariaEModPagDaFrontEnd(OrdinativoIncasso ordinativo, DatiOperazioneDto datiOperazioneDto){
		SediEModPagOrdinativoDto sediEModPagOrdinativoDto = new SediEModPagOrdinativoDto();
		SiacTSoggettoFin siacTSoggetto = null;
		SiacTSoggettoFin siacTSoggettoSedeSecondaria = null;
		int idEnte = datiOperazioneDto.getSiacTEnteProprietario().getUid();
		
		//SI DETERMINA COSA E' ARRIVATO SE MOD PAG DI UNA SEDE O DEL SOGGETTO STESSO:
		siacTSoggetto = siacTSoggettoRepository.ricercaSoggettoNoSeSede(Constanti.AMBITO_FIN, idEnte, ordinativo.getSoggetto().getCodiceSoggetto(), Constanti.SEDE_SECONDARIA, datiOperazioneDto.getTs());

		SedeSecondariaSoggetto sede = CommonUtils.getFirst(ordinativo.getSoggetto().getSediSecondarie());
		if(sede!=null){
			List<SiacTSoggettoFin> sediDb =  siacTSoggettoRepository.ricercaSedi(idEnte, siacTSoggetto.getSoggettoId(), Constanti.SEDE_SECONDARIA);
			siacTSoggettoSedeSecondaria = DatiOperazioneUtils.getById(sediDb, sede.getUid());
			sediEModPagOrdinativoDto.setSiacTSedeSecondariaOrdine(siacTSoggettoSedeSecondaria);
		}
		sediEModPagOrdinativoDto.setSiacTSoggettoOrdine(siacTSoggetto);
		return sediEModPagOrdinativoDto;
	}
	
	/**
	 * Ricerca dell'ordinativo pagamento per chiave per l'ente proprietario
	 * @param numeroPaginaOrdinativiCollegati 
	 * @param numeroOrdinativiCollegatiPerPagina 
	 * @param RicercaOrdinativoPagamentoK
	 * @return
	 */
	public RicercaOrdinativoPerChiaveDto ricercaOrdinativoPagamento(RicercaOrdinativoPagamentoK pk, DatiOperazioneDto datiOperazioneDto, Richiedente richiedente) {
		return ricercaOrdinativoPagamento(pk, datiOperazioneDto, null, null, richiedente);
	}

	public RicercaOrdinativoPerChiaveDto ricercaOrdinativoPagamento(RicercaOrdinativoPagamentoK pk, DatiOperazioneDto datiOperazioneDto, 
			Integer numeroOrdinativiCollegatiPerPagina, Integer numeroPaginaOrdinativiCollegati, Richiedente richiedente) {
	    RicercaOrdinativoPerChiaveDto ordinativoPagamentoDto = new RicercaOrdinativoPerChiaveDto();
	    OrdinativoPagamento ordinativoPagamento = null;
	    Integer codiceEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
	    
	    //CHIAMATA "CORE" DI RICERCA
		SiacTOrdinativoFin siacTOrdinativo = ordinativoDao.ricercaOrdinativo(codiceEnte, pk, Constanti.D_ORDINATIVO_TIPO_PAGAMENTO, datiOperazioneDto.getTs());
		///////////////////////////////
		
		if(siacTOrdinativo!=null && siacTOrdinativo.getUid()!=null){
			//OK ESISTE
			
			AttributoTClassInfoDto attributoInfo = new AttributoTClassInfoDto();
			attributoInfo.setTipoOggetto(OggettoDellAttributoTClass.T_ORDINATIVO);
			attributoInfo.setSiacTOrdinativo(siacTOrdinativo);
			
			//CONVERSIONE DATI:
			//DATI MAPPATI SUL MODEL
			ordinativoPagamento = componiOrdinativoModel(attributoInfo, datiOperazioneDto, richiedente, pk.getBilancio().getAnno(),Constanti.D_ORDINATIVO_TIPO_PAGAMENTO);
			//
			
			//capitolo spesa
			List<SiacROrdinativoBilElemFin> listaSiacROrdinativoBilElem = siacTOrdinativo.getSiacROrdinativoBilElems();
			if(null != listaSiacROrdinativoBilElem && listaSiacROrdinativoBilElem.size() > 0){
				for(SiacROrdinativoBilElemFin siacROrdinativoBilElem : listaSiacROrdinativoBilElem){
					if(null!= siacROrdinativoBilElem && siacROrdinativoBilElem.getDataFineValidita() == null){
						if(Constanti.D_BIL_ELEM_TIPO_ELEM_TIPO_CODE_CAP_UG.equalsIgnoreCase(siacROrdinativoBilElem.getSiacTBilElem().getSiacDBilElemTipo().getElemTipoCode())) {
						    RicercaDettaglioCapitoloUGest ricercaDettaglioCapitoloUGest = new RicercaDettaglioCapitoloUGest();
							ricercaDettaglioCapitoloUGest.setChiaveCapitolo(siacROrdinativoBilElem.getSiacTBilElem().getElemId());
							ordinativoPagamentoDto.setRicercaDettaglioCapitoloUGest(ricercaDettaglioCapitoloUGest);
						}
					}
				}
			}
			
			//provvedimento
			List<SiacROrdinativoAttoAmmFin> listaSiacROrdinativoAttoAmm = siacTOrdinativo.getSiacROrdinativoAttoAmms();
			SiacROrdinativoAttoAmmFin siacROrdinativoAttoAmm = DatiOperazioneUtils.getValido(listaSiacROrdinativoAttoAmm, null);
			if(siacROrdinativoAttoAmm!=null){
				RicercaAtti ricercaAtti = buildRicercaAtti(siacROrdinativoAttoAmm.getSiacTAttoAmm());
				ordinativoPagamentoDto.setRicercaAtti(ricercaAtti);
			}
			
			
			// DATI OIL 
			DatiOrdinativoTrasmesso oil = convertiDatiTrasmissioneOil(datiOperazioneDto.getSiacTEnteProprietario().getUid(), siacTOrdinativo);
			ordinativoPagamento.setDatiOrdinativoTrasmesso(oil);
			
			ordinativoPagamentoDto.setOrdinativoPagamento(ordinativoPagamento);
			
		}
		
	    return ordinativoPagamentoDto;
	}
	

	/**
	 * Imposta i dati di trasmissione dell'ordinativo informatico
	 * @param siacTEnteProprietarioId
	 * @param siacTOrdinativo
	 * @return
	 */
	protected DatiOrdinativoTrasmesso convertiDatiTrasmissioneOil(Integer siacTEnteProprietarioId, SiacTOrdinativoFin siacTOrdinativo) {

		//ANTO
		//SIAC-5988
		DatiOrdinativoTrasmesso oil = new DatiOrdinativoTrasmesso();
		
		List<SiacTOilRicevuta> siacTOilRicevutas = siacTOilRicevutaRepository.findTOilRicevutaByOrdinativo(siacTEnteProprietarioId, siacTOrdinativo.getUid());
		if(siacTOilRicevutas!=null && !siacTOilRicevutas.isEmpty() ) {
		
			for (SiacTOilRicevuta siacTOilRicevuta : siacTOilRicevutas) {
				
				SiacDOilRicevutaTipo siacDOilRicevutaTipo = siacTOilRicevuta.getSiacDOilRicevutaTipo();
				
				if(siacDOilRicevutaTipo.getOilRicevutaTipoCode().equals(SiacDOilRicevutaTipoEnum.FIRMA.getCodice())){
					
					// leggo i dati firma
					if(siacTOilRicevuta.getSiacROrdinativoFirmas()!=null && !siacTOilRicevuta.getSiacROrdinativoFirmas().isEmpty()){			
						for (SiacROrdinativoFirma siacROrdinativoFirma : siacTOilRicevuta.getSiacROrdinativoFirmas()) {
							if(siacROrdinativoFirma.getDataCancellazione()==null){
								oil.setDataFirma(siacROrdinativoFirma.getOrdFirmaData());
								oil.setFirma(siacROrdinativoFirma.getOrdFirma());
								break;
							}
						}
					}
					
				}
	
				
				if(siacDOilRicevutaTipo.getOilRicevutaTipoCode().equals(SiacDOilRicevutaTipoEnum.QUIETANZA.getCodice())){
				
					// leggo i dati quietanza
					if(siacTOilRicevuta.getSiacROrdinativoQuietanzas()!=null && !siacTOilRicevuta.getSiacROrdinativoQuietanzas().isEmpty()){
						for (SiacROrdinativoQuietanza siacROrdinativoQuietanza : siacTOilRicevuta.getSiacROrdinativoQuietanzas()) {
							if(siacROrdinativoQuietanza.getDataCancellazione()==null){
								oil.setDataQuietanza(siacROrdinativoQuietanza.getOrdQuietanzaData());
								oil.setCro(siacROrdinativoQuietanza.getOrdQuietanzaCro());
								break;
							}
						}
					}
				
				}
				
			}
			
			
		}
		
		oil.setDataTrasmissione(siacTOrdinativo.getOrdTrasmisisoneData());
		
		return oil;
	}
	
	/**
	 * a partire da un oggetto SiacTOrdinativoFin popola un oggetto Soggetto con i dati del soggetto collegato
	 * @param siacTOrdinativo
	 * @param codiceEnte
	 * @return
	 */
	protected Soggetto componiSoggettoDiOrdinativo(SiacTOrdinativoFin siacTOrdinativo, String codiceAmbito,int codiceEnte,
			DatiOperazioneDto datiOperazioneDto,OttimizzazioneOrdinativoPagamentoDto datiOttimizzazione ){
		Soggetto creditore = new Soggetto();
		SedeSecondariaSoggetto sedeSecondaria = new SedeSecondariaSoggetto();
		String codSoggetto = null;
		boolean isSedeSecondaria = false;
		
		SiacROrdinativoSoggettoFin siacROrdinativoSoggetto = DatiOperazioneUtils.getValido(siacTOrdinativo.getSiacROrdinativoSoggettos(), getNow());
		
		if(null != siacROrdinativoSoggetto){
//			System.out.println("siacROrdinativoSoggetto.getSiacTOrdinativo().getUid():" + siacROrdinativoSoggetto.getSiacTOrdinativo().getUid());
//			System.out.println("siacROrdinativoSoggetto.getSiacTSoggetto().getUid():" + siacROrdinativoSoggetto.getSiacTSoggetto().getUid());
			codSoggetto = siacROrdinativoSoggetto.getSiacTSoggetto().getSoggettoCode();
			
//			System.out.println("siacROrdinativoSoggetto.getSiacTSoggetto().getSoggettoCode():" + siacROrdinativoSoggetto.getSiacTSoggetto().getSoggettoCode());
		    
		    if(datiOttimizzazione!=null){
				//RAMO OTTIMIZZATO
				
				SiacTSoggettoFin siacTSoggettoFin = siacROrdinativoSoggetto.getSiacTSoggetto();
				OttimizzazioneSoggettoDto ottimizzazioneSoggetto = datiOttimizzazione.getOttimizzazioneSoggettoDto();
				// Estrazione Soggetto
				boolean caricaSediSecondarie = true;
				boolean isDecentrato = false;
				boolean caricaDatiUlteriori = true;
				boolean includeModifica = false;
				creditore = soggettoDad.ricercaSoggettoOPT( siacTSoggettoFin, includeModifica, caricaDatiUlteriori, ottimizzazioneSoggetto,
						datiOperazioneDto, caricaSediSecondarie , isDecentrato);
				
			} else {
				//RAMO CLASSICO
				creditore = soggettoDad.ricercaSoggetto(codiceAmbito, codiceEnte, codSoggetto, false, true);
			}
				
		   
		    Integer idSoggetto = siacROrdinativoSoggetto.getSiacTSoggetto().getSoggettoId();
		   
		    if (soggettoDad.isSedeSecondaria(idSoggetto, codiceEnte)) {
		    	isSedeSecondaria = true;
		    	sedeSecondaria = soggettoDad.ricercaSedeSecondariaPerChiave(codiceEnte, codSoggetto, idSoggetto);
				List<SedeSecondariaSoggetto> listaSediSecondarie = new ArrayList<SedeSecondariaSoggetto>();
				listaSediSecondarie.add(sedeSecondaria);
				creditore.setSediSecondarie(listaSediSecondarie);
		    } else {
		    	//PULISCO LE SEDI SECONDARIE DEL SOGGETTO CARICATO (dato che il front end assume che siano dell'ordinativo):
		    	creditore.setSediSecondarie(null);
		    }
		}
		
		SiacROrdinativoModpagFin siacROrdinativoModpag = DatiOperazioneUtils.getValido(siacTOrdinativo.getSiacROrdinativoModpags(), getNow());
		if(null != siacROrdinativoModpag){
			//aggiunto il parametro codiceMDP = null
			Integer uidSiacROrdinativoModpag = null;
			
			if(siacROrdinativoModpag.getSiacTModpag()!=null && siacROrdinativoModpag.getSiacTModpag().getUid()!=null){
				uidSiacROrdinativoModpag =  siacROrdinativoModpag.getSiacTModpag().getUid();
			}else{
				uidSiacROrdinativoModpag =  siacROrdinativoModpag.getCessioneId().getUid();
			}
			
			List<ModalitaPagamentoSoggetto> listaModalitaPagamento = null;
			if(datiOttimizzazione!=null){
				//RAMO OTTIMIZZATO
				OttimizzazioneModalitaPagamentoDto ottimizzazioneModPag = datiOttimizzazione.getModalitaPagamentoOrdinativo();
				listaModalitaPagamento = soggettoDad.ricercaModalitaPagamentoPerChiave(codiceAmbito, codiceEnte,codSoggetto, uidSiacROrdinativoModpag, null,ottimizzazioneModPag);
			} else {
				//RAMO CLASSICO
				listaModalitaPagamento = soggettoDad.ricercaModalitaPagamentoPerChiave(codiceAmbito,codiceEnte, codSoggetto, uidSiacROrdinativoModpag, null);
			}
			
			if(null != listaModalitaPagamento && listaModalitaPagamento.size() > 0){
				for(ModalitaPagamentoSoggetto modPag : listaModalitaPagamento){
					
					log.debug("componiSoggettoDiOrdinativo", " listaModalitaPagamento. modPag. uid: " + modPag.getUid());
					log.debug("componiSoggettoDiOrdinativo", " listaModalitaPagamento. modPag. codiceStatoModalitaPagamento: " + modPag.getCodiceStatoModalitaPagamento());
					log.debug("componiSoggettoDiOrdinativo", " listaModalitaPagamento. modPag. isInModifica: " + modPag.isInModifica());
										
									
					if(modPag.isInModifica()){
						break;
					}
					
					List<ModalitaPagamentoSoggetto> elencoModalitaPagamento = new ArrayList<ModalitaPagamentoSoggetto>();
						
					elencoModalitaPagamento.add(modPag);
					if(isSedeSecondaria){
						creditore.getSediSecondarie().get(0).setModalitaPagamentoSoggettos(elencoModalitaPagamento);
					} else {
						creditore.setModalitaPagamentoList(elencoModalitaPagamento);
					}
				}
			}
		}
		
		return creditore;
	}
	
	/**
	 * Si occupa della creazione del model Ordinativo
	 * @param attributoInfo
	 * @param datiOperazioneDto
	 * @param richiedente
	 * @param annoBilancio
	 * @param tipoOrdinativo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <O extends Ordinativo> O componiOrdinativoModel( AttributoTClassInfoDto attributoInfo, DatiOperazioneDto datiOperazioneDto, 
			Richiedente richiedente, int annoBilancio,String tipoOrdinativo){
		int codiceEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		SiacTOrdinativoFin siacTOrdinativo = attributoInfo.getSiacTOrdinativo(); 
		datiOperazioneDto.setCurrMillisec(getCurrentMillisecondsTrentaMaggio2017());
		
		
		long startZero = System.currentTimeMillis();
		//DATI DI OTTIMIZZAZIONE:
		OttimizzazioneOrdinativoPagamentoDto datiOttimizzazione = caricaOttimizzazioneRicercaOrdPag(siacTOrdinativo, datiOperazioneDto);
		//
		long endZero = System.currentTimeMillis();
		
		long startUno = System.currentTimeMillis();
		
		O ordinativo = null;
		if(Constanti.D_ORDINATIVO_TIPO_INCASSO.equals(tipoOrdinativo)){
			ordinativo = (O) new OrdinativoPagamento();
			ordinativo = (O) map(siacTOrdinativo, OrdinativoIncasso.class, FinMapId.SiacTOrdinativo_OrdinativoIncasso);
		} else {
			ordinativo = (O) new OrdinativoIncasso();
			ordinativo = (O) map(siacTOrdinativo, OrdinativoPagamento.class, FinMapId.SiacTOrdinativo_OrdinativoPagamento);
	    }
		ordinativo.setDataCreazioneSupport(ordinativo.getDataCreazione());
		
		if(Constanti.D_ORDINATIVO_TIPO_INCASSO.equals(tipoOrdinativo)){
			ordinativo = (O) EntityOrdinativiToModelOrdinativiConverter.siacTOrdinativoEntityToOrdinativoIncassoModel(siacTOrdinativo, (OrdinativoIncasso) ordinativo);
		} else {
			ordinativo = (O) EntityOrdinativiToModelOrdinativiConverter.siacTOrdinativoEntityToOrdinativoPagamentoModel(siacTOrdinativo, (OrdinativoPagamento) ordinativo);
		}
	
		//distinta
		if (siacTOrdinativo.getSiacDDistinta() != null) {
	    	Distinta distinta = map(siacTOrdinativo.getSiacDDistinta(), Distinta.class, FinMapId.SiacDDistinta_Distinta);
	    	ordinativo.setDistinta(distinta);
		}
	
		if(Constanti.D_ORDINATIVO_TIPO_PAGAMENTO.equals(tipoOrdinativo)){
			// commissioni
			if (siacTOrdinativo.getSiacDCommissioneTipo() != null) {
				CommissioneDocumento commissioneDocumento = map(siacTOrdinativo.getSiacDCommissioneTipo(), CommissioneDocumento.class, FinMapId.SiacDCommissioneTipo_CommissioneDocumento);
				((OrdinativoPagamento)ordinativo).setCommissioneDocumento(commissioneDocumento);
			}
		}
		
		//conto tesoreria
		if (siacTOrdinativo.getSiacDContotesoreria() != null) {
		    ContoTesoreria contoTesoreria = map(siacTOrdinativo.getSiacDContotesoreria(), ContoTesoreria.class, FinMapId.SiacDContotesoreria_Contotesoreria);
			ordinativo.setContoTesoreria(contoTesoreria);
		}
		
		//note tesoriere
		if (siacTOrdinativo.getSiacDNoteTesoriere() != null) {
		    	NoteTesoriere noteTesoriere = map(siacTOrdinativo.getSiacDNoteTesoriere(), NoteTesoriere.class, FinMapId.SiacDNoteTesoriere_NoteTesoriere);
			ordinativo.setNoteTesoriere(noteTesoriere);
		}
		
		//codice bollo
		if (siacTOrdinativo.getSiacDCodicebollo() != null) {
	    	CodiceBollo codiceBollo = map(siacTOrdinativo.getSiacDCodicebollo(), CodiceBollo.class, FinMapId.SiacDCodiceBollo_CodiceBollo);
	    	ordinativo.setCodiceBollo(codiceBollo);
		}
		
		//note
		//String note = getValoreAttr(attributoInfo, datiOperazioneDto, codiceEnte, Constanti.T_ATTR_CODE_NOTE_ORDINATIVO);
		String note = getValoreAttrSenzaChiamataDb(attributoInfo, Constanti.T_ATTR_CODE_NOTE_ORDINATIVO);
		ordinativo.setNote(note);
		
		//flag Allegato Cartaceo
		String stringAllegatoCartaceo = getValoreAttrSenzaChiamataDb(attributoInfo, Constanti.T_ATTR_CODE_FLAG_ALLEGATO_CARTACEO);
		//String stringAllegatoCartaceo = getValoreAttr(attributoInfo, datiOperazioneDto, codiceEnte, Constanti.T_ATTR_CODE_FLAG_ALLEGATO_CARTACEO);
		Boolean flagAllegatoCartaceo = "S".equalsIgnoreCase(stringAllegatoCartaceo);
		ordinativo.setFlagAllegatoCartaceo(flagAllegatoCartaceo);
		
		//CIG PER SIOPE PLUS:
		if(Constanti.D_ORDINATIVO_TIPO_PAGAMENTO.equals(tipoOrdinativo)){
			String cig = getValoreAttrSenzaChiamataDb(attributoInfo, Constanti.T_ATTR_CODE_CIG);
			((OrdinativoPagamento)ordinativo).setCig(cig);
		}
		
		
		// tipo avviso
		TipoAvviso tipoAvviso = new TipoAvviso();
		SiacROrdinativoClassFin siacROrdinativoClass =  getRClassEsistentiSenzaQueryDb(attributoInfo, Constanti.D_CLASS_TIPO_TIPO_AVVISO);
		if(siacROrdinativoClass!=null){
			tipoAvviso.setCodice(siacROrdinativoClass.getSiacTClass().getClassifCode());
			tipoAvviso.setDescrizione(siacROrdinativoClass.getSiacTClass().getClassifDesc());
		}
		ordinativo.setTipoAvviso(tipoAvviso);
		
		//CREDITORE/DEBITORE
		Soggetto creditore = componiSoggettoDiOrdinativo(siacTOrdinativo, Constanti.AMBITO_FIN, codiceEnte,datiOperazioneDto,datiOttimizzazione);
		ordinativo.setSoggetto(creditore);
		
		//transazione elementare
		List<SiacROrdinativoClassFin> listaSiacROrdinativoClass = siacTOrdinativo.getSiacROrdinativoClasses();
		if(Constanti.D_ORDINATIVO_TIPO_INCASSO.equals(tipoOrdinativo)){
			ordinativo = (O) TransazioneElementareEntityToModelConverter.
					convertiDatiTransazioneElementare((OrdinativoIncasso) ordinativo, listaSiacROrdinativoClass,siacTOrdinativo.getSiacROrdinativoAttrs());
		} else {
			ordinativo = (O) TransazioneElementareEntityToModelConverter.
					convertiDatiTransazioneElementare((OrdinativoPagamento) ordinativo, listaSiacROrdinativoClass,siacTOrdinativo.getSiacROrdinativoAttrs());
		}

		
		ordinativo.setClassificatoreStipendi(getClassificatoreStipendi(listaSiacROrdinativoClass));
		
		
		long endUno = System.currentTimeMillis();
		long startDue = System.currentTimeMillis();
		
		//quote ordinativo
		if(Constanti.D_ORDINATIVO_TIPO_INCASSO.equals(tipoOrdinativo)){
			ordinativo = (O) completaDatiSubOrdinativoIncasso((OrdinativoIncasso) ordinativo, siacTOrdinativo, datiOperazioneDto, richiedente, annoBilancio);
		} else {
			ordinativo = (O) completaDatiSubOrdinativoPagamento((OrdinativoPagamento) ordinativo, siacTOrdinativo, datiOperazioneDto, richiedente, annoBilancio,datiOttimizzazione);
		}
		
		long endDue = System.currentTimeMillis();
		long startTre = System.currentTimeMillis();
		
		
		
		//regolarizzazione provvisorio
		BigDecimal totImportiProvvisori = BigDecimal.ZERO;
		BigDecimal totImportiRegolarizzati = BigDecimal.ZERO;
		List<RegolarizzazioneProvvisorio> listaRegolarizzazioneProvvisorio = new ArrayList<RegolarizzazioneProvvisorio>();
		List<SiacROrdinativoProvCassaFin> listaSiacROrdinativoProvCassa = siacTOrdinativo.getSiacROrdinativoProvCassas();
		if(null != listaSiacROrdinativoProvCassa && listaSiacROrdinativoProvCassa.size() > 0){
			for(SiacROrdinativoProvCassaFin siacROrdinativoProvCassa : listaSiacROrdinativoProvCassa){
				if(null != siacROrdinativoProvCassa &&  siacROrdinativoProvCassa.getDataFineValidita() == null){
					
					Integer annoProvvisorio=siacROrdinativoProvCassa.getSiacTProvCassa().getProvcAnno();
					Integer numeroProvvisorio=siacROrdinativoProvCassa.getSiacTProvCassa().getProvcNumero().intValue();
					String tipoProvvisorio=siacROrdinativoProvCassa.getSiacTProvCassa().getSiacDProvCassaTipo().getProvcTipoCode();

					ProvvisorioDiCassa provvisorioDiCassa = provvisorioDad.ricercaProvvisorioDiCassaPerChiave(codiceEnte, annoProvvisorio, numeroProvvisorio, tipoProvvisorio, datiOperazioneDto);
				    RegolarizzazioneProvvisorio regolarizzazioneProvvisorio = new RegolarizzazioneProvvisorio();
				    regolarizzazioneProvvisorio.setImporto(siacROrdinativoProvCassa.getOrdProvcImporto());
				    if (null != siacROrdinativoProvCassa.getOrdProvcImporto()) {
				    	totImportiRegolarizzati = totImportiRegolarizzati.add(siacROrdinativoProvCassa.getOrdProvcImporto());
				    }
				    if (null != provvisorioDiCassa) {
					    regolarizzazioneProvvisorio.setProvvisorioDiCassa(provvisorioDiCassa);
					    // id regolarizzazione
					    regolarizzazioneProvvisorio.setIdRegolarizzazioneProvvisorio(siacROrdinativoProvCassa.getUid());
					    if (null != provvisorioDiCassa.getImporto()) {
					    	totImportiProvvisori = totImportiProvvisori.add(provvisorioDiCassa.getImporto());
					    }
				    }
				    listaRegolarizzazioneProvvisorio.add(regolarizzazioneProvvisorio);
				}
			}
		}
		
		// SIAC-6175
		ordinativo.setDaTrasmettere(siacTOrdinativo.getOrdDaTrasmettere());
		
		//flag copertura
		if (listaRegolarizzazioneProvvisorio.size() > 0) {
		    ordinativo.setFlagCopertura(true);
		}
		//listaRegolarizzazioneProvvisorio.
		ordinativo.setElencoRegolarizzazioneProvvisori(listaRegolarizzazioneProvvisorio);
		ordinativo.setTotImportiProvvisori(totImportiProvvisori);
		ordinativo.setTotImportiRegolarizzati(totImportiRegolarizzati);
		
		long endTre = System.currentTimeMillis();
		
		long totZero = endZero - startZero;
		long totTre = endTre - startTre;
		long totUno = endUno - startUno;
		long totDue = endDue - startDue;
		
		CommonUtils.println("totZero: " + totZero);
		CommonUtils.println("totUno: " + totUno + " - totDue: " + totDue + " - totTre: " + totTre);
		
		return ordinativo;
	}

	private ClassificatoreStipendi getClassificatoreStipendi(List<SiacROrdinativoClassFin> listaSiacROrdinativoClass) {
		
		for (SiacROrdinativoClassFin oc : listaSiacROrdinativoClass) {
			
			if(oc.getDataCancellazione() != null || oc.getSiacTClass() == null || !Constanti.D_CLASS_TIPO_CLASSIFICATORE_STIPENDI.equals(oc.getSiacTClass().getSiacDClassTipo().getClassifTipoCode())) {
				continue;
			}
		
			ClassificatoreStipendi cs = new ClassificatoreStipendi();
			cs.setUid(oc.getSiacTClass().getUid());
			cs.setCodice(oc.getSiacTClass().getClassifCode());
			cs.setDescrizione(oc.getSiacTClass().getClassifDesc());
			
			if (oc.getSiacTClass().getSiacDClassTipo() != null) {
				TipoClassificatore tc = new TipoClassificatore();
				tc.setCodice(oc.getSiacTClass().getSiacDClassTipo().getClassifTipoCode());
				tc.setDescrizione(oc.getSiacTClass().getSiacDClassTipo().getClassifTipoDesc());
				cs.setTipoClassificatore(tc);
			}
			return cs;
		}
		
		return null;
	}
	
	
	public OttimizzazioneOrdinativoPagamentoDto caricaOttimizzazioneRicercaOrdPag(SiacTOrdinativoFin siacTOrdinativo,DatiOperazioneDto datiOperazioneDto){
		return caricaOttimizzazioneRicercaOrdPagOppureLiquidazione(siacTOrdinativo, null, datiOperazioneDto,this.soggettoDad);
	}
	
	/**
	 * sotto-metodo di componiOrdinativoModel
	 * Mappa i subOrdinativi e con essi il subDocumento, il documento e il tipoDocumento
	 * @param ordinativoPagamento
	 * @param siacTOrdinativo
	 * @param datiOperazioneDto
	 * @param richiedente
	 * @param annoBilancio
	 * @return
	 */
	private OrdinativoPagamento completaDatiSubOrdinativoPagamento(OrdinativoPagamento ordinativoPagamento, SiacTOrdinativoFin siacTOrdinativo,
			DatiOperazioneDto datiOperazioneDto, Richiedente richiedente, int annoBilancio,OttimizzazioneOrdinativoPagamentoDto datiOttimizzazione){
		BigDecimal importoOrdinativo=BigDecimal.ZERO;
		List<SubOrdinativoPagamento> listaQuoteOrdinativo = new ArrayList<SubOrdinativoPagamento>();
		
		List<SiacTOrdinativoTFin> listaSiacTOrdinativoT = siacTOrdinativo.getSiacTOrdinativoTs();
		
		if(datiOttimizzazione!=null){
			//RAMO OTTIMIZZATO
			listaSiacTOrdinativoT = datiOttimizzazione.getListaSubOrdinativiCoinvolti();
			listaSiacTOrdinativoT = CommonUtils.soloValidiSiacTBase(listaSiacTOrdinativoT, getNow());
		} else {
			//RAMO CLASSICO 
			listaSiacTOrdinativoT = siacTOrdinativo.getSiacTOrdinativoTs();
		}
		
		
		if(null != listaSiacTOrdinativoT && listaSiacTOrdinativoT.size() > 0){
	    	for(SiacTOrdinativoTFin siacTOrdinativoT : listaSiacTOrdinativoT){
    	    	if(null != siacTOrdinativoT && siacTOrdinativoT.getDataFineValidita() == null){
    	    		SubOrdinativoPagamento subOrdinativoPagamento = caricaSubOrdinativoPagamento(siacTOrdinativoT, ordinativoPagamento, datiOperazioneDto, richiedente, annoBilancio,datiOttimizzazione);
    	    		if(subOrdinativoPagamento!=null){
    	    			importoOrdinativo=importoOrdinativo.add(subOrdinativoPagamento.getImportoAttuale());	
	    	    		listaQuoteOrdinativo.add(subOrdinativoPagamento);
    	    		}
    	    	}
	    	}
		}
		ordinativoPagamento.setElencoSubOrdinativiDiPagamento(listaQuoteOrdinativo);
		ordinativoPagamento.setImportoOrdinativo(importoOrdinativo);
		return ordinativoPagamento;
	}
	
	/**
	 * sotto-metodo di componiOrdinativoModel
	 * @param ordinativoIncasso
	 * @param siacTOrdinativo
	 * @param datiOperazioneDto
	 * @param richiedente
	 * @param annoBilancio
	 * @return
	 */
	private OrdinativoIncasso completaDatiSubOrdinativoIncasso(OrdinativoIncasso ordinativoIncasso, SiacTOrdinativoFin siacTOrdinativo,
			DatiOperazioneDto datiOperazioneDto, Richiedente richiedente, int annoBilancio){
		//quote ordinativo
				BigDecimal importoOrdinativo=BigDecimal.ZERO;
				List<SubOrdinativoIncasso> listaQuoteOrdinativo = new ArrayList<SubOrdinativoIncasso>();
				List<SiacTOrdinativoTFin> listaSiacTOrdinativoT = siacTOrdinativo.getSiacTOrdinativoTs();
				if(null != listaSiacTOrdinativoT && listaSiacTOrdinativoT.size() > 0){
				    	for(SiacTOrdinativoTFin siacTOrdinativoT : listaSiacTOrdinativoT){
				    	    	if(null != siacTOrdinativoT && siacTOrdinativoT.getDataFineValidita() == null){
				    	    			SubOrdinativoIncasso subOrdinativoIncasso = map(siacTOrdinativoT, SubOrdinativoIncasso.class, FinMapId.SiacTOrdinativoT_SubOrdinativoIncasso_ModelDetail);
				    	    			subOrdinativoIncasso.setIdOrdinativo(ordinativoIncasso.getIdOrdinativo());
				    	    			
				    	    			Accertamento accertamento = new Accertamento();
				    	    			List<SiacROrdinativoTsMovgestTFin> listaSiacROrdinativoTsMovgestTs = siacTOrdinativoT.getSiacROrdinativoTsMovgestTs();
				    	    			if(null != listaSiacROrdinativoTsMovgestTs && listaSiacROrdinativoTsMovgestTs.size() > 0){
				    	    				
				    	    				for(SiacROrdinativoTsMovgestTFin siacROrdinativoTsMovgestTs : listaSiacROrdinativoTsMovgestTs){
				    	    					
				    	    					if(null != siacROrdinativoTsMovgestTs && siacROrdinativoTsMovgestTs.getDataFineValidita() == null && siacROrdinativoTsMovgestTs.getDataCancellazione() == null){
				    	    						
				    	    						if (siacROrdinativoTsMovgestTs.getSiacTMovgestTs() != null && siacROrdinativoTsMovgestTs.getSiacTMovgestTs().getSiacTMovgest() != null) {
				    	    							
				    	    							SubAccertamento subAccertamento = map(siacROrdinativoTsMovgestTs.getSiacTMovgestTs(), SubAccertamento.class, FinMapId.SiacTMovgestTs_SubAccertamento);
				    	    							accertamento = map(siacROrdinativoTsMovgestTs.getSiacTMovgestTs().getSiacTMovgest(), Accertamento.class, FinMapId.SiacTMovgest_Accertamento);
				    	    							
				    	    							// Leggo il Pdc
				    	    							CodificaFin codifica = EntityToModelConverter.convertiCodificaFromSiacTClass(siacROrdinativoTsMovgestTs.getSiacTMovgestTs().getSiacRMovgestClasses(), 
				    	    										Constanti.D_CLASS_TIPO_PIANO_DEI_CONTI_I, true);
				    	    							
				    	    							
				    	    							accertamento.setIdPdc(codifica.getUid());
				    	    							
				    	    							//System.out.println(" codifica.getUid: " + codifica.getUid());
				    	    							//System.out.println(" accertamento.getIdPdc: " + accertamento.getIdPdc());
				    	    							
				    	    							accertamento.setCodicePdc(codifica.getCodice());
				    	    							accertamento.setDescPdc(codifica.getDescrizione());
				    	    							
				    	    							
				    	    							if (!isTestataAccertamento(siacROrdinativoTsMovgestTs.getSiacTMovgestTs())) {
				    	    								List<SubAccertamento> listaSubAccertamenti = new ArrayList<SubAccertamento>();
				    	    								listaSubAccertamenti.add(subAccertamento);
				    	    								accertamento.setElencoSubAccertamenti(listaSubAccertamenti);
				    	    							}
				    	    						}
				    	    					}
				    	    				}
				    	    			}
				    	    			subOrdinativoIncasso.setAccertamento(accertamento);
				    	    			
				    	    			//Estrazione dati importo Quota
										if (siacTOrdinativoT.getSiacTOrdinativoTsDets()!=null && siacTOrdinativoT.getSiacTOrdinativoTsDets().size()>0) {
											for (SiacTOrdinativoTsDetFin siacTOrdinativoTsDet : siacTOrdinativoT.getSiacTOrdinativoTsDets()) {
												// - DECODIFICA!!!
												if (siacTOrdinativoTsDet.getSiacDOrdinativoTsDetTipo().getOrdTsDetTipoCode().equalsIgnoreCase("A")) {
													subOrdinativoIncasso.setImportoAttuale(siacTOrdinativoTsDet.getOrdTsDetImporto());
													importoOrdinativo=importoOrdinativo.add(subOrdinativoIncasso.getImportoAttuale());
												}
											}
											
										}
										
				    			    	listaQuoteOrdinativo.add(subOrdinativoIncasso);
				    			    	
				    			    	
				    			    	
				    			    	
				    			    	// Scorro la relazione col subDocumentoEntrata
						    	    	List<SiacRSubdocOrdinativoTFin> listaSiacSubdocOrdinativo = siacTOrdinativoT.getSiacRSubdocOrdinativoTs();
						    	    	//System.out.println("listaSiacSubdocOrdinativo: "+(listaSiacSubdocOrdinativo !=null && !listaSiacSubdocOrdinativo.isEmpty() ? listaSiacSubdocOrdinativo.size():" VUOTA"));
						    	    	
						    	    	if(listaSiacSubdocOrdinativo!=null && !listaSiacSubdocOrdinativo.isEmpty()){
						    	    		
						    	    		for (SiacRSubdocOrdinativoTFin siacRSubdocOrdinativoTFin : listaSiacSubdocOrdinativo) {
						    	    			//SIAC-6831						    	    			
						    	    			if(siacRSubdocOrdinativoTFin.getDataCancellazione() != null || !CommonUtils.isValidoSiacTBase(siacRSubdocOrdinativoTFin, null)) {
						    	    				continue;
						    	    			}
						    	    			
						    	    			//leggo il sucDoc collegato al subOrdinativo
						    	    			SiacTSubdocFin siacTSubDoc = siacRSubdocOrdinativoTFin.getSiacTSubdoc();
						    	    			//System.out.println("siacTSubDoFin: "+siacTSubDoc.getUid() + " - " + siacTSubDoc.getSubdocNumero());
						    	    			//System.out.println("siacTSubDoFin: "+siacTSubDoc.getSiacTDoc().getUid() + " - " + siacTSubDoc.getSiacTDoc().getDocNumero());
						    	    			SubdocumentoEntrata subDocumentoEntrata = map(siacTSubDoc, SubdocumentoEntrata.class, FinMapId.SiacTSubdocFin_SubdocumentoEntrata);
						    	    			subOrdinativoIncasso.setSubDocumentoEntrata(subDocumentoEntrata);

						    				}
						    	    	
						    	    	}
						    	    	
						    	    	// gestione onere
						    	    	// FIXME: Rm 03/04 verificare che la relazione sia giusta, leggo da r_doc_onere (a cosa serve la  r_subdoc_ordinatvo_ts??)
						    	    	SiacRDocOnereFin siacRDocOnere = siacTOrdinativoT.getSiacRDocOnere();
						    	    	if(siacRDocOnere!=null){
						    	    		
						    	    		//leggo l'onere collegato al subOrdinativo
						    	    		SiacDOnereFin siacDOnere = siacRDocOnere.getSiacDOnere();
						    	    		//System.out.println("siacDOnere: "+siacDOnere.getUid() + " - " + siacDOnere.getOnereCode());
						    	    		TipoOnere tipoOnere = map(siacDOnere, TipoOnere.class, FinMapId.SiacDOnereFin_TipoOnere);
						    	    		subOrdinativoIncasso.setTipoOnere(tipoOnere);
						    	    	}
				    	    	}
		
				    	}
				}
				
				ordinativoIncasso.setElencoSubOrdinativiDiIncasso(listaQuoteOrdinativo);
				ordinativoIncasso.setImportoOrdinativo(importoOrdinativo);
				return ordinativoIncasso;
	}
	
	/**
	 * Wrapper di retrocompatibilita'
	 * @param siacTOrdinativoT
	 * @param ordinativoPagamento
	 * @param datiOperazioneDto
	 * @param richiedente
	 * @param annobilancio
	 * @return
	 */
	public SubOrdinativoPagamento caricaSubOrdinativoPagamento(SiacTOrdinativoTFin siacTOrdinativoT, 
			OrdinativoPagamento ordinativoPagamento, DatiOperazioneDto datiOperazioneDto, Richiedente richiedente, int annobilancio){
		return caricaSubOrdinativoPagamento(siacTOrdinativoT, ordinativoPagamento, datiOperazioneDto, richiedente, annobilancio,null);
	}
	
	/**
	 * Questo metodo deve effetture un mapping equivalente a map-id="SiacTOrdinativoT_SubOrdinativoPagamento"
	 * ma usando un approccio ottimizzato
	 * 
	 * @param siacTOrdinativoT
	 * @param subOrdinativoPagamento
	 * @param datiOttimizzazione
	 * @return
	 */
	private SubOrdinativoPagamento siaSiacTOrdinativoTFinToSubOrdinativoPagamentoModel(SiacTOrdinativoTFin siacTOrdinativoT,OttimizzazioneOrdinativoPagamentoDto datiOttimizzazione){
		
		SubOrdinativoPagamento subOrdinativoPagamento = new SubOrdinativoPagamento();
		
		if(siacTOrdinativoT!=null && datiOttimizzazione!=null){
			
			subOrdinativoPagamento = map(siacTOrdinativoT, SubOrdinativoPagamento.class, FinMapId.SiacTOrdinativoT_SubOrdinativoPagamento_Minimo);
			
			//1. Mappin della liquidazione
			//deve essere equivalente al mapping:
			//	<field map-id="SiacTLiquidazione_Liquidazione_Base">
			//	<a>liquidazione</a> 
			//	<b>siacRLiquidazioneOrds[0].siacTLiquidazione</b> 
			//	</field>
			
			List<SiacRLiquidazioneOrdFin> liqOrds = datiOttimizzazione.filtraSiacRLiquidazioneOrdFinBySiacTOrdinativoTFin(siacTOrdinativoT);
			if(liqOrds!=null && liqOrds.size()>0){
				SiacRLiquidazioneOrdFin siacRLiquidazioneOrdFin = liqOrds.get(0);
				if(siacRLiquidazioneOrdFin!=null){
					SiacTLiquidazioneFin siacTLiquidazioneFin = siacRLiquidazioneOrdFin.getSiacTLiquidazione();
					if(siacTLiquidazioneFin!=null){
						Liquidazione liquidazione = map(siacTLiquidazioneFin, Liquidazione.class, FinMapId.SiacTLiquidazione_Liquidazione_Base);
						subOrdinativoPagamento.setLiquidazione(liquidazione);
					}
				}
			}
		
			
			//2. Mapping del sub documento spesa
			//deve essere equivalente al mapping:
			//	<field map-id="SiacTSubdocFin_SubdocumentoSpesa">
			//	<a>subDocumentoSpesa</a> 
			//	<b>siacRSubdocOrdinativoTs[0].siacTSubdoc</b> 
			//	</field>
			List<SiacRSubdocOrdinativoTFin> rSubDocs = datiOttimizzazione.filtraSiacRSubdocOrdinativoTFinBySiacTOrdinativoTFin(siacTOrdinativoT);
			if(rSubDocs!=null && rSubDocs.size()>0){
				SiacRSubdocOrdinativoTFin siacRSubdocOrdinativoTFin = rSubDocs.get(0);
				//SIAC-6831
				if(siacRSubdocOrdinativoTFin!=null && siacRSubdocOrdinativoTFin.getDataCancellazione()!= null &&  CommonUtils.isValidoSiacTBase(siacRSubdocOrdinativoTFin, null)){
					SiacTSubdocFin siacTSubdocFin = siacRSubdocOrdinativoTFin.getSiacTSubdoc();
					if(siacTSubdocFin!=null){
						
						SubdocumentoSpesa subdocumentoSpesa = map(siacTSubdocFin, SubdocumentoSpesa.class, FinMapId.SiacTSubdocFin_SubdocumentoSpesa_Minimo);
						
						SiacTDocFin siacTDoc = siacTSubdocFin.getSiacTDoc();
						if(siacTDoc!=null){
							//Qui invece il mapping equivalente a:
							//	<field map-id="SiacTDocFin_DocumentoSpesa">
							//	<a>documento</a> 
							//	<b>siacTDoc</b> 
							//	</field>
							
							DocumentoSpesa docSpesa = map(siacTDoc, DocumentoSpesa.class, FinMapId.SiacTDocFin_DocumentoSpesa_Minimo);
							StatoOperativoDocumento statoOperativo = EntityOrdinativiToModelOrdinativiConverter.statoOperativoDocumentoOttimizzato(siacTDoc, datiOttimizzazione);
							docSpesa.setStatoOperativoDocumento(statoOperativo );
							
							subdocumentoSpesa.setDocumento(docSpesa);
						}
						
						subOrdinativoPagamento.setSubDocumentoSpesa(subdocumentoSpesa);
					}
				}
			}

		}
		
		return subOrdinativoPagamento;
	}
	
	
	/**
	 * carica i dati accessori e li mette nel model
	 * @param siacTOrdinativoT
	 * @param ordinativoPagamento
	 * @param datiOperazioneDto
	 * @param richiedente
	 * @param annobilancio
	 * @return
	 */
	public SubOrdinativoPagamento caricaSubOrdinativoPagamento(SiacTOrdinativoTFin siacTOrdinativoT, 
			OrdinativoPagamento ordinativoPagamento, DatiOperazioneDto datiOperazioneDto, Richiedente richiedente, int annobilancio,
			OttimizzazioneOrdinativoPagamentoDto datiOttimizzazione){
		
		Integer codiceEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		
		SubOrdinativoPagamento subOrdinativoPagamento = null;
		
		if(datiOttimizzazione!=null){
			//RAMO OTTIMIZZATO
			subOrdinativoPagamento = siaSiacTOrdinativoTFinToSubOrdinativoPagamentoModel(siacTOrdinativoT, datiOttimizzazione);
		} else {
			//RAMO CLASSICO
			subOrdinativoPagamento = map(siacTOrdinativoT, SubOrdinativoPagamento.class, FinMapId.SiacTOrdinativoT_SubOrdinativoPagamento_Medium);
		}
		
		
		
		if(ordinativoPagamento!=null){
			subOrdinativoPagamento.setIdOrdinativo(ordinativoPagamento.getIdOrdinativo());
		}
		
		
		List<SiacRLiquidazioneOrdFin> listaSiacRLiquidazioneOrd = null;
		if(datiOttimizzazione!=null){
			//RAMO OTTIMIZZATO
			listaSiacRLiquidazioneOrd = datiOttimizzazione.filtraSiacRLiquidazioneOrdFinBySiacTOrdinativoTFin(siacTOrdinativoT);
		} else {
			//RAMO CLASSICO
			listaSiacRLiquidazioneOrd = siacTOrdinativoT.getSiacRLiquidazioneOrds();
		}
		
		OttimizzazioneMutuoDto ottimizzazioneMutui=null;
    	if(datiOttimizzazione!=null && datiOttimizzazione.getOttimizzazioneMovimentiCoinvolti() != null){
    		//RAMO OTTIMIZZATO MUTUI
    		ottimizzazioneMutui = datiOttimizzazione.getOttimizzazioneMovimentiCoinvolti().getOttimizzazioneMutuoDto();
    	}
		
    	if(null != listaSiacRLiquidazioneOrd && listaSiacRLiquidazioneOrd.size() > 0){
    	    	
    		for(SiacRLiquidazioneOrdFin siacRLiquidazioneOrd : listaSiacRLiquidazioneOrd){
    	    	    	
    				// Rm : il controllo va fatto sulla dt cancellazione e non solo sulla dt fine validita'
    	    		if(null != siacRLiquidazioneOrd && siacRLiquidazioneOrd.getDataFineValidita() == null && siacRLiquidazioneOrd.getDataCancellazione()==null ){
    	    	    	    	
    	    				if (siacRLiquidazioneOrd.getSiacTLiquidazione() != null) {
    	    					
    	    					//LIQUIDAZIONE ITERATA:
    	    					SiacTLiquidazioneFin siacTLiquidazioneFin = siacRLiquidazioneOrd.getSiacTLiquidazione();
		    			    	Liquidazione liquidazioneT = map(siacTLiquidazioneFin, Liquidazione.class, FinMapId.SiacTLiquidazione_Liquidazione);
		    			    	if(datiOttimizzazione!=null){
		    			    		datiOttimizzazione.setSiacTLiquidazioneConsiderata(siacTLiquidazioneFin);
		    			    	}
		    			    	//
		    			    	
		    			    	Liquidazione liquidazione = liquidazioneDad.ricercaLiquidazionePerChiave(liquidazioneT, Constanti.TIPO_RICERCA_DA_LIQUIDAZIONE, 
		    			    			richiedente, annobilancio, Constanti.AMBITO_FIN, richiedente.getAccount().getEnte(),datiOperazioneDto,datiOttimizzazione);
		    			    	
		    			    	
		    			    	if(liquidazione.getImpegno().getListaVociMutuo()!=null && !liquidazione.getImpegno().getListaVociMutuo().isEmpty()){
			    			    	for(VoceMutuo voceMutuo : liquidazione.getImpegno().getListaVociMutuo()) {
			    			    	    	int i = 0;
			    			    	    	Mutuo mutuo = null;
			    			    	    	if(ottimizzazioneMutui!=null){
			    			    	    		//RAMO OTTIMIZZATO 
			    			    	    		mutuo = ricercaMutuo(codiceEnte, voceMutuo.getNumeroMutuo(), datiOperazioneDto.getTs(),ottimizzazioneMutui);
			    			    	    	} else {
			    			    	    		//RAMO CLASSICO
			    			    	    		mutuo = ricercaMutuo(codiceEnte, voceMutuo.getNumeroMutuo(), datiOperazioneDto.getTs());
			    			    	    	}
			    			    	    	liquidazione.getImpegno().getListaVociMutuo().get(i).setMutuo(mutuo);
			    			    	    	i++;
			    			    	}
    	    	    	    	}
		    			    	subOrdinativoPagamento.setLiquidazione(liquidazione);
		    			    	
		    			    	//Estrazione dati importo Quota
								if (siacTOrdinativoT.getSiacTOrdinativoTsDets()!=null && siacTOrdinativoT.getSiacTOrdinativoTsDets().size()>0) {
									for (SiacTOrdinativoTsDetFin siacTOrdinativoTsDet : siacTOrdinativoT.getSiacTOrdinativoTsDets()) {
										// - DECODIFICA!!!
										if (siacTOrdinativoTsDet.getSiacDOrdinativoTsDetTipo().getOrdTsDetTipoCode().equalsIgnoreCase("A")) {
											subOrdinativoPagamento.setImportoAttuale(siacTOrdinativoTsDet.getOrdTsDetImporto());
											//importoOrdinativo=importoOrdinativo.add(subOrdinativoPagamento.getImportoAttuale());
										}
									}
									
								}
								//return subOrdinativoPagamento;
    	    	    	    }
    	    	    	}
    	    	}
    	}
    	
    	// Scorro la relazione col subDocumentoSpesa
    	List<SiacRSubdocOrdinativoTFin> listaSiacSubdocOrdinativo = null;
    	if(datiOttimizzazione!=null){
    		//RAMO OTTIMIZZATO
    		listaSiacSubdocOrdinativo = datiOttimizzazione.filtraSiacRSubdocOrdinativoTFinBySiacTOrdinativoTFin(siacTOrdinativoT);
    	} else {
    		//RAMO CLASSICO
    		listaSiacSubdocOrdinativo = siacTOrdinativoT.getSiacRSubdocOrdinativoTs();
    	}
    	
    	//System.out.println("listaSiacSubdocOrdinativo: "+(listaSiacSubdocOrdinativo !=null && !listaSiacSubdocOrdinativo.isEmpty() ? listaSiacSubdocOrdinativo.size():" VUOTA"));
    	
    	if(listaSiacSubdocOrdinativo!=null && !listaSiacSubdocOrdinativo.isEmpty()){
    		
    		for (SiacRSubdocOrdinativoTFin siacRSubdocOrdinativoTFin : listaSiacSubdocOrdinativo) {
    			
    			//leggo il sucDoc collegato al subOrdinativo
    			SiacTSubdocFin siacTSubDoc = siacRSubdocOrdinativoTFin.getSiacTSubdoc();
    			//System.out.println("siacTSubDoFin: "+siacTSubDoc.getUid() + " - " + siacTSubDoc.getSubdocNumero());
    			//System.out.println("siacTSubDoFin: "+siacTSubDoc.getSiacTDoc().getUid() + " - " + siacTSubDoc.getSiacTDoc().getDocNumero());
    			SubdocumentoSpesa subDocumentoSpesa = map(siacTSubDoc, SubdocumentoSpesa.class, FinMapId.SiacTSubdocFin_SubdocumentoSpesa);
    			subOrdinativoPagamento.setSubDocumentoSpesa(subDocumentoSpesa);

			}
    	
    	}
    	
    	// gestione onere
    	// FIXME: Rm 03/04 verificare se la relazione è giusta, leggo da r_doc_onere (a cosa serve la  r_subdoc_ordinatvo_ts??)
    	SiacRDocOnereFin siacRDocOnere = siacTOrdinativoT.getSiacRDocOnere();
    	if(siacRDocOnere!=null){
    		
    		//leggo l'onere collegato al subOrdinativo
    		SiacDOnereFin siacDOnere = siacRDocOnere.getSiacDOnere();
    		//System.out.println("siacDOnere: "+siacDOnere.getUid() + " - " + siacDOnere.getOnereCode());
    		TipoOnere tipoOnere = map(siacDOnere, TipoOnere.class, FinMapId.SiacDOnereFin_TipoOnere);
    		subOrdinativoPagamento.setTipoOnere(tipoOnere);
    	}
    	
    	return subOrdinativoPagamento;
	}
	
	
	
	public SubOrdinativoIncasso caricaSubOrdinativoIncasso(SiacTOrdinativoTFin siacTOrdinativoT, 
			OrdinativoIncasso ordinativoIncasso, DatiOperazioneDto datiOperazioneDto, Richiedente richiedente, int annobilancio,Ente ente,
			OttimizzazioneOrdinativoIncassoDto datiOttimizzazione){
		
		datiOttimizzazione = null;//metto a null per ora non e' ancora sviluppato
		
		Integer codiceEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		
		SubOrdinativoIncasso subOrdinativoIncasso = null;
		
		if(datiOttimizzazione!=null){
			//RAMO OTTIMIZZATO
			//subOrdinativoIncasso = siaSiacTOrdinativoTFinToSubOrdinativoPagamentoModel(siacTOrdinativoT, datiOttimizzazione);
		} else {
			//RAMO CLASSICO
			subOrdinativoIncasso = map(siacTOrdinativoT, SubOrdinativoIncasso.class, FinMapId.SiacTOrdinativoT_SubOrdinativoIncasso);
		}
		
		
		if(ordinativoIncasso!=null){
			subOrdinativoIncasso.setIdOrdinativo(ordinativoIncasso.getIdOrdinativo());
		}
		
		
		List<SiacROrdinativoTsMovgestTFin> listaSiacROrdinativoTsMovgestTFin = null;
		if(datiOttimizzazione!=null){
			//RAMO OTTIMIZZATO
			//listaSiacRLiquidazioneOrd = datiOttimizzazione.filtraSiacRLiquidazioneOrdFinBySiacTOrdinativoTFin(siacTOrdinativoT);
		} else {
			//RAMO CLASSICO
			listaSiacROrdinativoTsMovgestTFin = siacTOrdinativoT.getSiacROrdinativoTsMovgestTs();
			listaSiacROrdinativoTsMovgestTFin = CommonUtils.soloValidiSiacTBase(listaSiacROrdinativoTsMovgestTFin, null);
		}
		
		
		if(listaSiacROrdinativoTsMovgestTFin!=null && listaSiacROrdinativoTsMovgestTFin.size()>0){
			SiacROrdinativoTsMovgestTFin relazioneVersoAccertamento = listaSiacROrdinativoTsMovgestTFin.get(0);
			if(relazioneVersoAccertamento!=null && relazioneVersoAccertamento.getSiacTMovgestTs()!=null){
				SiacTMovgestFin movGest = relazioneVersoAccertamento.getSiacTMovgestTs().getSiacTMovgest();
				String annoEsercizio = movGest.getSiacTBil().getSiacTPeriodo().getAnno();
				BigDecimal numeroMovimento = movGest.getMovgestNumero();
				Integer annoMovimento = movGest.getMovgestAnno();
				MovimentoGestione accertamento = accertamentoOttimizzatoDad.ricercaMovimentoPk(richiedente, ente, annoEsercizio, annoMovimento, numeroMovimento, Constanti.MOVGEST_TIPO_ACCERTAMENTO, false);
				subOrdinativoIncasso.setAccertamento((Accertamento) accertamento);
			}
			
		}
		
    	
    	// Scorro la relazione col subDocumentoSpesa
    	List<SiacRSubdocOrdinativoTFin> listaSiacSubdocOrdinativo = null;
    	if(datiOttimizzazione!=null){
    		//RAMO OTTIMIZZATO
    		//listaSiacSubdocOrdinativo = datiOttimizzazione.filtraSiacRSubdocOrdinativoTFinBySiacTOrdinativoTFin(siacTOrdinativoT);
    	} else {
    		//RAMO CLASSICO
    		listaSiacSubdocOrdinativo = siacTOrdinativoT.getSiacRSubdocOrdinativoTs();
    	}
    	
    	if(listaSiacSubdocOrdinativo!=null && !listaSiacSubdocOrdinativo.isEmpty()){
    		for (SiacRSubdocOrdinativoTFin siacRSubdocOrdinativoTFin : listaSiacSubdocOrdinativo) {
    			//leggo il sucDoc collegato al subOrdinativo
    			SiacTSubdocFin siacTSubDoc = siacRSubdocOrdinativoTFin.getSiacTSubdoc();
    			SubdocumentoEntrata subdocumentoEntrata = map(siacTSubDoc, SubdocumentoEntrata.class, FinMapId.SiacTSubdocFin_SubdocumentoEntrata);
    			subOrdinativoIncasso.setSubDocumentoEntrata(subdocumentoEntrata);
			}
    	}
    	
    	// gestione onere
    	// FIXME: Rm 03/04 verificare se la relazione è giusta, leggo da r_doc_onere (a cosa serve la  r_subdoc_ordinatvo_ts??)
    	SiacRDocOnereFin siacRDocOnere = siacTOrdinativoT.getSiacRDocOnere();
    	if(siacRDocOnere!=null){
    		
    		//leggo l'onere collegato al subOrdinativo
    		SiacDOnereFin siacDOnere = siacRDocOnere.getSiacDOnere();
    		//System.out.println("siacDOnere: "+siacDOnere.getUid() + " - " + siacDOnere.getOnereCode());
    		TipoOnere tipoOnere = map(siacDOnere, TipoOnere.class, FinMapId.SiacDOnereFin_TipoOnere);
    		subOrdinativoIncasso.setTipoOnere(tipoOnere);
    	}
    	
    	return subOrdinativoIncasso;
	}
	
	/**
	 * Metodo di comodo per settare i dati codificati direttamente connessi a SiacTOrdinativoFin
	 * @param siacTOrdinativo
	 * @param ordinativo
	 * @param tipoOrdinativo
	 * @param datiOperazioneDto
	 * @param inserimento
	 * @return
	 */
	private SiacTOrdinativoFin impostaCodificheDOrdinativo(SiacTOrdinativoFin siacTOrdinativo, Ordinativo ordinativo, String tipoOrdinativo,
			DatiOperazioneDto datiOperazioneDto, boolean inserimento) {
		
		// siac_d_ordinativo_tipo
		if(inserimento){
			//il tipo viene scritto in inserimento e non si aggiorna piu'
			siacTOrdinativo = impostaTipoOrdinativo(siacTOrdinativo, tipoOrdinativo, datiOperazioneDto);
		}
		
		// siac_d_codicebollo
		siacTOrdinativo = impostaCodiceBollo(siacTOrdinativo, ordinativo, datiOperazioneDto);
		
		// siac_d_commissione_tipo
		if(ordinativo instanceof OrdinativoPagamento){
			OrdinativoPagamento ordinativoPagamento = (OrdinativoPagamento) ordinativo;
			siacTOrdinativo = impostaCommissioneTipo(siacTOrdinativo, ordinativoPagamento, datiOperazioneDto);
		}
		
		// siac_d_contotesoreria
		siacTOrdinativo = impostaContoTesoreria(siacTOrdinativo, ordinativo, datiOperazioneDto);
		
		// siac_d_note_tesoriere
		siacTOrdinativo = impostaNoteTesorerie(siacTOrdinativo, ordinativo, datiOperazioneDto);
		
		// siac_d_distinta 
		siacTOrdinativo = impostaTipoDistinta(siacTOrdinativo, ordinativo, datiOperazioneDto);
		
		return siacTOrdinativo;
	}
	
	/**
	 * imposta il conto tesoreria
	 * @param siacTOrdinativo
	 * @param ordinativoRicevuto
	 * @param datiOperazione
	 * @return
	 */
	private SiacTOrdinativoFin impostaContoTesoreria(SiacTOrdinativoFin siacTOrdinativo, Ordinativo ordinativoRicevuto,DatiOperazioneDto datiOperazione){
		if(ordinativoRicevuto!=null){
			SiacDContotesoreriaFin siacDContotesoreria = caricaContoTesoreria(ordinativoRicevuto, datiOperazione);
			siacTOrdinativo.setSiacDContotesoreria(siacDContotesoreria);
		}
		return siacTOrdinativo;
	}
	
	/**
	 * carica il conto tesoreria indicato nell'ordinativo
	 * @param ordinativoRicevuto
	 * @param datiOperazione
	 * @return
	 */
	private SiacDContotesoreriaFin caricaContoTesoreria(Ordinativo ordinativoRicevuto,DatiOperazioneDto datiOperazione){
		SiacDContotesoreriaFin siacDContotesoreria = null;
		Date dateInserimento = new Date(getCurrentMillisecondsTrentaMaggio2017());
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);
		
		String codiceRicevuto = ordinativoRicevuto.getContoTesoreria() != null ? ordinativoRicevuto.getContoTesoreria().getCodice()  : "" ;
		Integer idEnte = datiOperazione.getSiacTEnteProprietario().getEnteProprietarioId();
		
		if (!StringUtils.isEmpty(codiceRicevuto)) {
				siacDContotesoreria = siacDContotesoreriaRepository.findContotesoreriaByCode(idEnte, codiceRicevuto, datiOperazione.getTs());
		}else{
			List<SiacDContotesoreriaFin> siacDContotesoreriaList = siacDContotesoreriaRepository.findContotesoreriaByEnte(idEnte, timestampInserimento);
			if (siacDContotesoreriaList != null && !siacDContotesoreriaList.isEmpty()){
				siacDContotesoreria = siacDContotesoreriaList.get(0);
			}
			
		}
		return siacDContotesoreria;
	}
	
	/**
	 * verifica l'esiste sul db del conto tesoreria indicato per l'ordinativo in questione
	 * @param ordinativoRicevuto
	 * @param datiOperazione
	 * @return
	 */
	public boolean esisteContoTesoreria(Ordinativo ordinativoRicevuto,DatiOperazioneDto datiOperazione){
		SiacDContotesoreriaFin siacDContoTes = caricaContoTesoreria(ordinativoRicevuto, datiOperazione);
		if(siacDContoTes!=null && siacDContoTes.getContotesId()!=null){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * imposta la commissione tipo
	 * @param siacTOrdinativo
	 * @param ordinativoRicevuto
	 * @param datiOperazione
	 * @return
	 */
	private SiacTOrdinativoFin impostaCommissioneTipo(SiacTOrdinativoFin siacTOrdinativo, OrdinativoPagamento ordinativoRicevuto,DatiOperazioneDto datiOperazione){
		if(ordinativoRicevuto!=null && ordinativoRicevuto.getCommissioneDocumento() != null ){
			String codiceRicevuto = ordinativoRicevuto.getCommissioneDocumento().getCodice();
			Integer idEnte = datiOperazione.getSiacTEnteProprietario().getEnteProprietarioId();
			if (!StringUtils.isEmpty(codiceRicevuto)) {
				SiacDCommissioneTipoFin siacDCommissioneTipo = new SiacDCommissioneTipoFin();
				siacDCommissioneTipo = siacDCommissioneTipoRepository.findDCommissioneTipoValidoByEnteAndCode(idEnte, codiceRicevuto, datiOperazione.getTs());
				siacTOrdinativo.setSiacDCommissioneTipo(siacDCommissioneTipo);
			}
		}
		return siacTOrdinativo;
	}
	
	/**
	 * imposta le note tesoreria
	 * @param siacTOrdinativo
	 * @param ordinativoRicevuto
	 * @param datiOperazione
	 * @return
	 */
	private SiacTOrdinativoFin impostaNoteTesorerie(SiacTOrdinativoFin siacTOrdinativo, Ordinativo ordinativoRicevuto,DatiOperazioneDto datiOperazione){
		if(ordinativoRicevuto!=null && ordinativoRicevuto.getNoteTesoriere() != null ){
			String codiceRicevuto = ordinativoRicevuto.getNoteTesoriere().getCodice();
			Integer idEnte = datiOperazione.getSiacTEnteProprietario().getEnteProprietarioId();
			if (!StringUtils.isEmpty(codiceRicevuto)) {
				SiacDNoteTesoriereFin siacDNoteTesoriere = new SiacDNoteTesoriereFin();
				siacDNoteTesoriere = siacDNoteTesoriereRepository.findDNoteTesoriereValidoByEnteAndCode(idEnte, codiceRicevuto, datiOperazione.getTs());
				siacTOrdinativo.setSiacDNoteTesoriere(siacDNoteTesoriere);
			}
		}
		return siacTOrdinativo;
	}
	
	/**
	 * imposta il codice bollo
	 * @param siacTOrdinativo
	 * @param ordinativoRicevuto
	 * @param datiOperazione
	 * @return
	 */
	private SiacTOrdinativoFin impostaCodiceBollo(SiacTOrdinativoFin siacTOrdinativo, Ordinativo ordinativoRicevuto,DatiOperazioneDto datiOperazione){
		if(ordinativoRicevuto!=null && ordinativoRicevuto.getCodiceBollo() != null){
			String codiceRicevuto = ordinativoRicevuto.getCodiceBollo().getCodice();
			Integer idEnte = datiOperazione.getSiacTEnteProprietario().getEnteProprietarioId();
			if (!StringUtils.isEmpty(codiceRicevuto)) {
				SiacDCodicebolloFin siacDCodicebollo = new SiacDCodicebolloFin();
				siacDCodicebollo = siacDCodiceBolloRepository.findDCodiceBolloValidoByEnteAndCode(idEnte,codiceRicevuto, datiOperazione.getTs());
				siacTOrdinativo.setSiacDCodicebollo(siacDCodicebollo);
			}
		}
		return siacTOrdinativo;
	}
	
	/**
	 * imposta il tipo di ordinativo (Pagamento o Incasso)
	 * @param siacTOrdinativo
	 * @param tipoOrdinativo
	 * @param datiOperazione
	 * @return
	 */
	private SiacTOrdinativoFin impostaTipoOrdinativo(SiacTOrdinativoFin siacTOrdinativo, String tipoOrdinativo,DatiOperazioneDto datiOperazione){
		if(!StringUtils.isEmpty(tipoOrdinativo)){
			Integer idEnte = datiOperazione.getSiacTEnteProprietario().getEnteProprietarioId();
			SiacDOrdinativoTipoFin siacDOrdinativoTipo = new SiacDOrdinativoTipoFin();
			siacDOrdinativoTipo = siacDOrdinativoTipoRepository.findDOrdinativoTipoValidoByEnteAndCode(idEnte, tipoOrdinativo, datiOperazione.getTs());
			siacTOrdinativo.setSiacDOrdinativoTipo(siacDOrdinativoTipo);
		}
		return siacTOrdinativo;
	}
	
	/**
	 * imposta la distinta di spesa / entrata
	 * @param siacTOrdinativo
	 * @param ordinativoRicevuto
	 * @param datiOperazione
	 * @return
	 */
	private SiacTOrdinativoFin impostaTipoDistinta(SiacTOrdinativoFin siacTOrdinativo, Ordinativo ordinativoRicevuto,DatiOperazioneDto datiOperazione){
		SiacDDistintaFin siacDDistinta = caricaDistinta(ordinativoRicevuto, datiOperazione);
		siacTOrdinativo.setSiacDDistinta(siacDDistinta);
		return siacTOrdinativo;
	}
	
	/**
	 * carica la distinta relativa ad un ordinativo rispetto al fatto che sia di spesa o di entrata
	 * @param ordinativoRicevuto
	 * @param datiOperazione
	 * @return
	 */
	private SiacDDistintaFin caricaDistinta(Ordinativo ordinativoRicevuto,DatiOperazioneDto datiOperazione){
		SiacDDistintaFin siacDDistinta = null;
		Date dateInserimento = new Date(getCurrentMillisecondsTrentaMaggio2017());
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);
		
		if(ordinativoRicevuto!=null ){
			String tipoDistinta = "";
			
			if(ordinativoRicevuto instanceof OrdinativoPagamento){
				tipoDistinta = Constanti.D_ORDINATIVO_DISTINTA_TIPO_SPESA;
			} else if(ordinativoRicevuto instanceof OrdinativoIncasso){
				tipoDistinta = Constanti.D_ORDINATIVO_DISTINTA_TIPO_ENTRATA;
			} 
			
			String codiceRicevuto = ordinativoRicevuto.getDistinta()!=null ? ordinativoRicevuto.getDistinta().getCodice() : "";
			Integer idEnte = datiOperazione.getSiacTEnteProprietario().getEnteProprietarioId();
			
			if (!StringUtils.isEmpty(codiceRicevuto)){
				siacDDistinta = siacDDistintaRepository.findDDistintaValidaByEnteAndTipoAndCode(idEnte, tipoDistinta, codiceRicevuto, datiOperazione.getTs());
			}
			else{
				List<SiacDDistintaFin> siacDDistintaList = siacDDistintaRepository.findDistintaByCodTipo(idEnte,timestampInserimento, tipoDistinta);
				if (siacDDistintaList != null && !siacDDistintaList.isEmpty()){
					 siacDDistinta = siacDDistintaList.get(0);
				}
				
			}
		}
		return siacDDistinta;
	}
	
	/**
	 * verifica l'esiste sul db della distinta indicata per l'ordinativo in questione
	 * @param ordinativoRicevuto
	 * @param datiOperazione
	 * @return
	 */
	public boolean esisteDistinta(Ordinativo ordinativoRicevuto,DatiOperazioneDto datiOperazione){
		SiacDDistintaFin siacDDistinta = caricaDistinta(ordinativoRicevuto, datiOperazione);
		if(siacDDistinta!=null && siacDDistinta.getDistId()!=null){
			return true;
		} else {
			return false;
		}
	}

	
	/**
	 * verifica l'esistenza di ordinativi collegati
	 * @param annoOrdinativo
	 * @param numeroOrdinativo
	 * @param statoOperativoOrdinativo
	 * @param tipoOrdinativo
	 * @param datiOperazioneDto
	 * @return List<SiacTOrdinativoFin>
	 */

	public List<SiacTOrdinativoCollegatoCustom> checkOrdinativiCollegati(Integer annoOrdinativo, Integer numeroOrdinativo, 
			StatoOperativoOrdinativo statoOperativoOrdinativo, String tipoOrdinativo, DatiOperazioneDto datiOperazioneDto) {
		return checkOrdinativiCollegati(annoOrdinativo, numeroOrdinativo, statoOperativoOrdinativo, tipoOrdinativo, 
				null, datiOperazioneDto);
	}

	public List<SiacTOrdinativoCollegatoCustom> checkOrdinativiCollegati(Integer annoOrdinativo, Integer numeroOrdinativo, 
			StatoOperativoOrdinativo statoOperativoOrdinativo, String tipoOrdinativo, 
			ParametriPaginazione paginazioneOrdinativiCollegati,
			DatiOperazioneDto datiOperazioneDto) {
	
		List<SiacTOrdinativoCollegatoCustom> siacTOrdinativoCollegatoCustomList=new ArrayList<SiacTOrdinativoCollegatoCustom>();

		SiacTOrdinativoFin siacTOrdinativo = siacTOrdinativoRepository.findOrdinativoValidoByAnnoAndNumeroAndTipo(
				datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId(),
				annoOrdinativo, BigDecimal.valueOf(numeroOrdinativo), tipoOrdinativo, datiOperazioneDto.getTs());
		
		
		List<SiacROrdinativoFin> siacROrdinativoList = 
				siacROrdinativoRepository.findValideByIdOrdUno(siacTOrdinativo.getUid(), datiOperazioneDto.getTs(), 
						toPageable(paginazioneOrdinativiCollegati!= null ? paginazioneOrdinativiCollegati : ParametriPaginazione.TUTTI_GLI_ELEMENTI));
		
		if(siacROrdinativoList!=null && !siacROrdinativoList.isEmpty()){
		
			for (SiacROrdinativoFin siacROrdinativo : siacROrdinativoList) {
				if(siacROrdinativo.getDataCancellazione() == null){
					SiacTOrdinativoCollegatoCustom siacTOrdinativoCollegatoCustom = new SiacTOrdinativoCollegatoCustom();
					siacTOrdinativoCollegatoCustom.setSiacTOrdinativo(siacROrdinativo.getSiacTOrdinativo2());
					siacTOrdinativoCollegatoCustom.setRelazioneOrdinativiCollegati(siacROrdinativo.getSiacDRelazTipo().getRelazTipoCode());
					siacTOrdinativoCollegatoCustomList.add(siacTOrdinativoCollegatoCustom);
				}
			}
		}
		return siacTOrdinativoCollegatoCustomList;
	}
	
	
	public Integer readTotaleOrdinativiCollegati(Integer idOrdinativo, DatiOperazioneDto datiOperazioneDto) {
		Long countValideByIdOrdUno = siacROrdinativoRepository.countValideByIdOrdUno(idOrdinativo, datiOperazioneDto.getTs());
		return countValideByIdOrdUno != null ? countValideByIdOrdUno.intValue() : null; 
	}
	
	
	
	
	public List<SiacTOrdinativoCollegatoCustom> checkOrdinativiACuiSonoCollegato(Integer annoOrdinativo, Integer numeroOrdinativo, StatoOperativoOrdinativo statoOperativoOrdinativo, String tipoOrdinativo, DatiOperazioneDto datiOperazioneDto){

		List<SiacTOrdinativoCollegatoCustom> siacTOrdinativoCollegatoCustomList=new ArrayList<SiacTOrdinativoCollegatoCustom>();

		SiacTOrdinativoFin siacTOrdinativo = siacTOrdinativoRepository.findOrdinativoValidoByAnnoAndNumeroAndTipo(datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId(),
				annoOrdinativo, BigDecimal.valueOf(numeroOrdinativo), tipoOrdinativo, datiOperazioneDto.getTs());
		
		List<SiacROrdinativoFin> siacROrdinativoList = siacTOrdinativo.getSiacROrdinativos2();
		
		if(siacROrdinativoList!=null && !siacROrdinativoList.isEmpty()){
		
			for (SiacROrdinativoFin siacROrdinativo : siacROrdinativoList) {
				if(siacROrdinativo.getDataCancellazione() == null){
					SiacTOrdinativoCollegatoCustom siacTOrdinativoCollegatoCustom = new SiacTOrdinativoCollegatoCustom();
					siacTOrdinativoCollegatoCustom.setSiacTOrdinativo(siacROrdinativo.getSiacTOrdinativo1());
					siacTOrdinativoCollegatoCustom.setRelazioneOrdinativiCollegati(siacROrdinativo.getSiacDRelazTipo().getRelazTipoCode());
					siacTOrdinativoCollegatoCustomList.add(siacTOrdinativoCollegatoCustom);
				}
			}
		}
		return siacTOrdinativoCollegatoCustomList;
	}
	
	
	/**
	 * verifica se l'ordinativo di incasso è lui un collegato (quindi si controlla la colonna getSiacROrdinativos2) 
	 * @param annoOrdinativo
	 * @param numeroOrdinativo
	 * @param statoOperativoOrdinativo
	 * @param tipoOrdinativo
	 * @param datiOperazioneDto
	 * @return List<SiacTOrdinativoFin>
	 */
	public boolean isCollegatoAPagamento(Integer annoOrdinativo, Integer numeroOrdinativo, StatoOperativoOrdinativo statoOperativoOrdinativo, String tipoOrdinativo, DatiOperazioneDto datiOperazioneDto){

		boolean isCollegatoAPagamento = false;

		SiacTOrdinativoFin siacTOrdinativo = siacTOrdinativoRepository.findOrdinativoValidoByAnnoAndNumeroAndTipo(datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId(),
				annoOrdinativo, BigDecimal.valueOf(numeroOrdinativo), tipoOrdinativo, datiOperazioneDto.getTs());
		
		List<SiacROrdinativoFin> siacROrdinativoList = siacTOrdinativo.getSiacROrdinativos2();
		
		if(siacROrdinativoList!=null && !siacROrdinativoList.isEmpty()){
		
			for (SiacROrdinativoFin siacROrdinativo : siacROrdinativoList) {
				
				if(siacROrdinativo.getDataCancellazione() == null){
					isCollegatoAPagamento = true;
					break;
				}
			}
		}
		return isCollegatoAPagamento;
	}
	
	/**
	 * Ritorna true se esiste una relaziona di tipo SOS_ORD verso un altro ordinativo
	 * dove l'ordinativo indicato e' siacTOrdinativo1 (ovvero ord_id_da)
	 * @param annoOrdinativo
	 * @param numeroOrdinativo
	 * @param tipoOrdinativo
	 * @param datiOperazioneDto
	 * @return
	 */
	public boolean presenteRelezioneReintroito(Integer annoOrdinativo, 
			 Integer numeroOrdinativo, 
			 String tipoOrdinativo, 
			 DatiOperazioneDto datiOperazioneDto){
		
		boolean presenteReleazioneReintroito = false;
		
		//Carico l'ordinativo:
		Timestamp dataInput = datiOperazioneDto.getTs();
		SiacTOrdinativoFin siacTOrdinativo = siacTOrdinativoRepository.findOrdinativoValidoByAnnoAndNumeroAndTipo(datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId(),
																												  annoOrdinativo,
																												  BigDecimal.valueOf(numeroOrdinativo),
																												  tipoOrdinativo, 
																												  dataInput);
		
		if(siacTOrdinativo!=null && siacTOrdinativo.getOrdId()!=null){
			//ricarico le relazioni esplicitamente perche' dentro siacTOrdinativo.getSiacROrdinativos1 non trovavo refreshate
			//le ultime appena inserite dal chiamant che sta reintroitando:
			List<SiacROrdinativoFin> relaz = siacROrdinativoRepository.findValideByIdOrdUno(siacTOrdinativo.getOrdId(),dataInput);
			//
			
			//itero le relazioni cercando la presenza di quella SOS_ORD:
			if(!StringUtils.isEmpty(relaz)){
				for(SiacROrdinativoFin it: relaz){
					if(it!=null && it.getSiacDRelazTipo()!=null
							&& Constanti.D_SOSTITUZIONE_ORDINATIVO.equals(it.getSiacDRelazTipo().getRelazTipoCode())){
						presenteReleazioneReintroito = true;
						break;
					}
				}
			}
		}
		return presenteReleazioneReintroito;
	}
	
	
	public List<SiacTOrdinativoCollegatoCustom> checkOrdinativiCollegatiNonAnnullati(Integer annoOrdinativo, 
																					 Integer numeroOrdinativo, 
																					 String tipoOrdinativo, 
																					 DatiOperazioneDto datiOperazioneDto){

		List<SiacTOrdinativoCollegatoCustom> siacTOrdinativoCollegatoCustomList = new ArrayList<SiacTOrdinativoCollegatoCustom>();

		Timestamp dataInput = datiOperazioneDto.getTs();

		SiacTOrdinativoFin siacTOrdinativo = siacTOrdinativoRepository.findOrdinativoValidoByAnnoAndNumeroAndTipo(datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId(),
																												  annoOrdinativo,
																												  BigDecimal.valueOf(numeroOrdinativo),
																												  tipoOrdinativo, 
																												  dataInput);
		List<SiacROrdinativoFin> siacROrdinativoList = siacTOrdinativo.getSiacROrdinativos1();


		if(siacROrdinativoList!=null && !siacROrdinativoList.isEmpty()){

			for (SiacROrdinativoFin siacROrdinativo : siacROrdinativoList) {
				
				if(!DatiOperazioneUtils.isValido(siacROrdinativo, dataInput)) {
					continue;
				}
				
				List<SiacROrdinativoStatoFin>  siacROrdinativoStatoListCollegati = siacROrdinativo.getSiacTOrdinativo2().getSiacROrdinativoStatos();

				siacROrdinativoStatoListCollegati = DatiOperazioneUtils.soloValidi(siacROrdinativoStatoListCollegati, dataInput);
				
				if(!siacROrdinativoStatoListCollegati.isEmpty()){

					for (SiacROrdinativoStatoFin siacROrdinativoStatoFin : siacROrdinativoStatoListCollegati) {


						if(!siacROrdinativoStatoFin.getSiacDOrdinativoStato().getOrdStatoCode().equalsIgnoreCase(Constanti.D_ORDINATIVO_STATO_ANNULLATO)){


							SiacTOrdinativoCollegatoCustom siacTOrdinativoCollegatoCustom = new SiacTOrdinativoCollegatoCustom();
							siacTOrdinativoCollegatoCustom.setSiacTOrdinativo(siacROrdinativo.getSiacTOrdinativo2());
							siacTOrdinativoCollegatoCustom.setRelazioneOrdinativiCollegati(siacROrdinativo.getSiacDRelazTipo().getRelazTipoCode());
							siacTOrdinativoCollegatoCustomList.add(siacTOrdinativoCollegatoCustom);

						}

					}
				}

			}
		}
		return siacTOrdinativoCollegatoCustomList;
	}

	/**
	 * verifica il tipo di ordinativo
	 * @param annoOrdinativo
	 * @param numeroOrdinativo
	 * @param statoOperativoOrdinativo
	 * @param tipoOrdinativo
	 * @param datiOperazioneDto
	 * @return boolean
	 */
	public boolean checkTipoOrdinativo(Integer annoOrdinativo, Integer numeroOrdinativo, StatoOperativoOrdinativo statoOperativoOrdinativo, String tipoOrdinativo, DatiOperazioneDto datiOperazioneDto){

		SiacTOrdinativoFin siacTOrdinativo = siacTOrdinativoRepository.findOrdinativoValidoByAnnoAndNumeroAndTipo(datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId(), annoOrdinativo, BigDecimal.valueOf(numeroOrdinativo), tipoOrdinativo, datiOperazioneDto.getTs());

		if (siacTOrdinativo!=null && siacTOrdinativo.getSiacROrdinativoStatos()!=null && siacTOrdinativo.getSiacROrdinativoStatos().size()>0) {
			for (SiacROrdinativoStatoFin it : siacTOrdinativo.getSiacROrdinativoStatos()) {
				if (it.getDataFineValidita()==null) {
					if (it.getSiacDOrdinativoStato()!=null && it.getSiacDOrdinativoStato().getOrdStatoCode().equalsIgnoreCase(Constanti.statoOperativoOrdinativoEnumToString(statoOperativoOrdinativo))) {
						return true;
					}
				}
			}
		}


		return false;
	}
	
	/**
	 * Si occupa di calcolare la dispinibilia a regolazzire
	 * @param idSiacTProvCassa
	 * @param importo
	 * @param idEnte
	 * @param datiOperazioneDto
	 * @return
	 */
	protected BigDecimal calcolaDisponibilitaARegolarizzare(Integer idSiacTProvCassa,BigDecimal importo,Integer idEnte,DatiOperazioneDto datiOperazioneDto){
		BigDecimal disponibilitaRegolarizzare = BigDecimal.ZERO;
//		Calcolo importoDaRegolarizzare
//		importoDaRegolarizzare = provvisorio.importo - SOMMATORIA<RegolarizzazioneProvvisiorio.importo> - 
//				SOMMATORIA<Subdocumento.importo> (se il subdocumento non e' ancora legato a ordinativo) 
//				- SOMMATORIA<predocumento.importo> (se il pre-documento non e' ancora diventato un documento).
		//: da non sviluppare in V1
		return disponibilitaRegolarizzare;
	}
	
	/**
	 * Ricerca dell'ordinativo incasso per chiave per l'ente proprietario
	 * @param RicercaOrdinativoIncassoK
	 * @return
	 */
	public RicercaOrdinativoPerChiaveDto ricercaOrdinativoIncasso (RicercaOrdinativoIncassoK pk, DatiOperazioneDto datiOperazioneDto, 
			Richiedente richiedente) {
		Bilancio bilancio = pk.getBilancio();
	    RicercaOrdinativoPerChiaveDto ordinativoIncassoDto= new RicercaOrdinativoPerChiaveDto();
	    OrdinativoIncasso ordinativoIncasso = null;
	    Integer codiceEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
	    
	    //RICERCA "CORE"
		SiacTOrdinativoFin siacTOrdinativo = ordinativoDao.ricercaOrdinativo(codiceEnte, pk, Constanti.D_ORDINATIVO_TIPO_INCASSO, datiOperazioneDto.getTs());
		////////////////
		
		AttributoTClassInfoDto attributoInfo = new AttributoTClassInfoDto();
		attributoInfo.setTipoOggetto(OggettoDellAttributoTClass.T_ORDINATIVO);
		attributoInfo.setSiacTOrdinativo(siacTOrdinativo);
		
		ordinativoIncasso = componiOrdinativoModel(attributoInfo, datiOperazioneDto, richiedente, bilancio.getAnno(),Constanti.D_ORDINATIVO_TIPO_INCASSO);
		
		//capitolo entrata
		List<SiacROrdinativoBilElemFin> listaSiacROrdinativoBilElem = siacTOrdinativo.getSiacROrdinativoBilElems();
		if(null != listaSiacROrdinativoBilElem && listaSiacROrdinativoBilElem.size() > 0){
			for(SiacROrdinativoBilElemFin siacROrdinativoBilElem : listaSiacROrdinativoBilElem){
				if(null!= siacROrdinativoBilElem && siacROrdinativoBilElem.getDataFineValidita() == null){
					if(Constanti.D_BIL_ELEM_TIPO_ELEM_TIPO_CODE_CAP_EG.equalsIgnoreCase(siacROrdinativoBilElem.getSiacTBilElem().getSiacDBilElemTipo().getElemTipoCode())) {
						RicercaDettaglioCapitoloEGest ricercaDettaglioCapitoloEGest = new RicercaDettaglioCapitoloEGest();
						ricercaDettaglioCapitoloEGest.setChiaveCapitolo(siacROrdinativoBilElem.getSiacTBilElem().getElemId());
						ordinativoIncassoDto.setRicercaDettaglioCapitoloEGest(ricercaDettaglioCapitoloEGest);
					}
				}
			}
		}
		
		//provvedimento
		List<SiacROrdinativoAttoAmmFin> listaSiacROrdinativoAttoAmm = siacTOrdinativo.getSiacROrdinativoAttoAmms();
		SiacROrdinativoAttoAmmFin siacROrdinativoAttoAmm = DatiOperazioneUtils.getValido(listaSiacROrdinativoAttoAmm, null);
		if(siacROrdinativoAttoAmm!=null){
			RicercaAtti ricercaAtti = buildRicercaAtti(siacROrdinativoAttoAmm.getSiacTAttoAmm());
			ordinativoIncassoDto.setRicercaAtti(ricercaAtti);
		}
		
		
		// DATI OIL 
		DatiOrdinativoTrasmesso oil = convertiDatiTrasmissioneOil(datiOperazioneDto.getSiacTEnteProprietario().getUid(), siacTOrdinativo);
		ordinativoIncasso.setDatiOrdinativoTrasmesso(oil);

		ordinativoIncassoDto.setOrdinativoIncasso(ordinativoIncasso);
	    return ordinativoIncassoDto;
	}
	
	/**
	 * Serve per testare se il SiacTMovgestTsFin e' una testata oppure no
	 * @param siacTMovgestTs
	 * @return
	 */
	private boolean isTestataAccertamento (SiacTMovgestTsFin siacTMovgestTs) {
		boolean res = false;
		if (null != siacTMovgestTs && null != siacTMovgestTs.getSiacDMovgestTsTipo()) {
			if (Constanti.MOVGEST_TS_TIPO_TESTATA.equals(siacTMovgestTs.getSiacDMovgestTsTipo().getMovgestTsTipoCode())) {
				res = true;
			}
		}
		return res;
	}
	
	
	/**
	 * 
	 * Si occupa di CARICARE e VERIFICARE i dati relativi all'input del servizio di reintroito.
	 * 
	 * IMPORTANTE: asssume che il chiamante abbia gia' caricato e verificato l'esistenza dell'ordinativo di
	 * pagamento: perche' dal service chiamante occorre caricare seperamente i suoi eventuali ord incasso
	 * 
	 * In caso di errori ritorna gli errori trovati in listaErrori dentro OrdinativoInReintroitoInfoDto
	 * e il chiamanete deve quindi interrompersi rilanciando tali errori.
	 * 
	 * Se va tutto bene il chiamanete potra' procedere al reintroito utilizzando
	 * le varie struttre dati di comodo popolate dentro OrdinativoInReintroitoInfoDto.
	 * 
	 * @param datiInput
	 * @param datiOperazione
	 * @return
	 * @throws RuntimeException
	 */
	public OrdinativoInReintroitoInfoDto caricaEVerificaDatiPerReintroitoOrdinativoPagamento(OrdinativoInReintroitoDatiDiInputDto datiInput,OrdinativoInReintroitoInfoDto esito, DatiOperazioneDto datiOperazione) throws RuntimeException {
		
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		//DATI DI CONTESTO:
		Integer idEnte = datiOperazione.getSiacTEnteProprietario().getUid();
		Bilancio bilancio = datiInput.getBilancio();
		Richiedente richiedente = datiInput.getRichiedente();
		Ente ente = datiInput.getEnte();
		
		//CARICHIAMO L'ATTO:
		esito = controlliAttoPerReintroito(datiInput, esito, datiOperazione);
		if(esito.presenzaErrori()){
			return esito;
		}
		
		//L'ORDINATIVO DI PAGAMENTO E' GIA' STATO POPOLATO DAL CHIAMANTE: (vedi commento in javadoc)
		OrdinativoPagamento ordinativoPagamentoRicaricato = esito.getOrdinativoPagamento();
		//
		
		//CONTROLLI RITENUTE RICEVUTE:
		esito = controlliRitenute(ordinativoPagamentoRicaricato , datiInput, esito, datiOperazione);
		if(esito.presenzaErrori()){
			return esito;
		}
		
		//OK QUI HO LE RITENUTE IN INPUT VALIDATATE (a meno di controlli fini su impegni e acc vedi dopo)
		//MI RICONDUCO PER UNIFORMITA' ALLA SITUAZIONE DI RITENUTE SPLIT INDICATE PUNTUALMENTE:
		datiInput = impostaSplitSeRichiesteUguali(datiInput,ordinativoPagamentoRicaricato.getElencoOrdinativiCollegati());
		//
		
		
		//RICERCHIAMO I VARI IMPEGNI E ACCERTAMENTI RICEVUTI IN INPUT AL SERVIZIO:
		
		HashMap<String, EsitoRicercaMovimentoPkDto> impegniCaricati = new HashMap<String, EsitoRicercaMovimentoPkDto>();
		HashMap<String, EsitoRicercaMovimentoPkDto> accertamentiCaricati = new HashMap<String, EsitoRicercaMovimentoPkDto>();
		
		//CARICO L'IMPEGNO DESTINAZIONE:
		MovimentoKey impegnoDestinazioneKey = datiInput.getImpegnoSuCuiSpostare();
		EsitoRicercaMovimentoPkDto impNettoCaricato = caricaImpegnoPerReintroito(impegnoDestinazioneKey, richiedente, ente,impegniCaricati);
		esito = controlloEsistenzaEStatoMovimento(impNettoCaricato, datiInput.getImpegnoSuCuiSpostare(), esito, true);
		if(esito.presenzaErrori()){
			return esito;
		}
		//SETTO LE INFO SULL'IMPEGNO DESTINAZIONE APPENA CARICATO CON SUCCESSO:
		esito.setImpegnoDestinazione(buildImpegnoPerReintroitoInfoDto(impNettoCaricato,impegnoDestinazioneKey));
		//
		
		//CONTROLLIAMO GLI IMPEGNI DELLE RITENUTE:
		if(!StringUtils.isEmpty(datiInput.getRitenuteSplit())){
			esito =  controlliEsistenzaEStatoMovimentiRitenute(datiInput, esito, richiedente, ente, datiOperazione,ordinativoPagamentoRicaricato, impegniCaricati,accertamentiCaricati);
			if(esito.presenzaErrori()){
				return esito;
			}
		}
		
		//CONTROLLIAMO LA COERENZA DEI SOGGETTI:
		
		
		return esito;
	}
	
	public OrdinativoInReintroitoInfoDto controlliDisponibilitaMovimentiPerReintroito(OrdinativoInReintroitoInfoDto esito,
			DatiOperazioneDto datiOperazione, Richiedente richiedente) throws RuntimeException {
		
		//CONTROLLIAMO I DISPONIBILI A LIQUIDARE DEI VARI IMPEGNI
		//E I DISP A PAGARE DEI CAPITOLI DEI VARI IMPEGNI:
		esito = controlliDisponibilitaImpegni(esito);
		if(esito.presenzaErrori()){
			return esito;
		}
		
		//CONTROLLIAMO I DISPONIBILI A PAGARE DEI VARI ACCERTAMETI:
		esito = controlliDisponibilitaPagareAccertamenti(esito,richiedente);
		if(esito.presenzaErrori()){
			return esito;
		}
		
		return esito;
	}
	
	public OrdinativoInReintroitoInfoDto controlliCoerenzaSoggettiPerReintroito(OrdinativoInReintroitoInfoDto esito, DatiOperazioneDto datiOperazione) throws RuntimeException {
		
		ImpegnoPerReintroitoInfoDto impegnoDest = esito.getImpegnoDestinazione();

		OrdinativoPagamento ordPag = esito.getOrdinativoPagamento();
		
		esito = controlloCoerenzaSoggetto(impegnoDest,ordPag,esito,datiOperazione);
		if(esito.presenzaErrori()){
			return esito;
		}
		
		List<RitenutaSpiltPerReintroitoInfoDto> ritenuteSplit = esito.getListaRitenuteSplit();
		
		if(!StringUtils.isEmpty(ritenuteSplit)){
			for(RitenutaSpiltPerReintroitoInfoDto ritIt: ritenuteSplit){
				
				//CONTROLLIAMO IL SOGGETTO DELL'IMPEGNO
				esito = controlloCoerenzaSoggetto(ritIt.getImpegno(),ordPag,esito,datiOperazione);
				if(esito.presenzaErrori()){
					return esito;
				}
				
				//CONTROLLIAMO IL SOGGETTO DELL'ACCERTAMENTO
				esito = controlloCoerenzaSoggetto(ritIt.getAccertamento(),ritIt.getOrdinativoIncasso(),esito,datiOperazione);
				if(esito.presenzaErrori()){
					return esito;
				}
				
				
			}
		}
		
		return esito;
	}
	
	private OrdinativoInReintroitoInfoDto controlloCoerenzaSoggetto(AccertamentoPerReintroitoInfoDto accertamento,
			OrdinativoIncasso ordinativoIncasso,OrdinativoInReintroitoInfoDto esito,DatiOperazioneDto datiOperazione) {
		if(!soggettoCoerente(accertamento,ordinativoIncasso,datiOperazione)){
			String nomeEntita = ReintroitoUtils.buildNomeEntitaPerMessaggiDiErrore(accertamento.getKey(), false);
			String codiceEntita = ReintroitoUtils.buildCodiceEntitaPerMessaggiDiErrore(accertamento.getKey());
			String msg = "Non ha un soggetto compatibile con l'ordinativo di incasso.";
			esito.addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore(nomeEntita + ": " + codiceEntita, msg));
		}
		return esito;
	}

	private OrdinativoInReintroitoInfoDto controlloCoerenzaSoggetto(ImpegnoPerReintroitoInfoDto impegno,OrdinativoPagamento ordPag,
			OrdinativoInReintroitoInfoDto esito,DatiOperazioneDto datiOperazione) {
		if(!soggettoCoerente(impegno,ordPag,datiOperazione)){
			String nomeEntita = ReintroitoUtils.buildNomeEntitaPerMessaggiDiErrore(impegno.getKey(), true);
			String codiceEntita = ReintroitoUtils.buildCodiceEntitaPerMessaggiDiErrore(impegno.getKey());
			String msg = "Non ha un soggetto compatibile con l'ordinativo di pagamento.";
			esito.addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore(nomeEntita + ": " + codiceEntita, msg));
		}
		return esito;
	}

	private boolean soggettoCoerente(ImpegnoPerReintroitoInfoDto impegno, OrdinativoPagamento ordPag,DatiOperazioneDto datiOperazione) {
		Soggetto soggImp = impegno.getSoggettoImpOSub();
		ClasseSoggetto classeSoggImp = impegno.getClasseSoggettoImpOSub();
		Soggetto soggOrd = ordPag.getSoggetto();
		return soggettoCoerentePerReintroito(soggImp, classeSoggImp, soggOrd,datiOperazione);
	}
	
	private boolean soggettoCoerente(AccertamentoPerReintroitoInfoDto accertamento, OrdinativoIncasso ordinativoIncasso,DatiOperazioneDto datiOperazione) {
		Soggetto soggAcc = accertamento.getSoggettoAccOSub();
		ClasseSoggetto classeSoggAcc = accertamento.getClasseSoggettoAccOSub();
		Soggetto soggOrd = ordinativoIncasso.getSoggetto();
		return soggettoCoerentePerReintroito(soggAcc, classeSoggAcc, soggOrd,datiOperazione);
	}
	
	private boolean soggettoCoerentePerReintroito(Soggetto soggMov,ClasseSoggetto classeSoggMov, Soggetto soggOrd,DatiOperazioneDto datiOperazione) {
		
		//PREMESSA: gli ordinativi hanno sempre il soggetto
		//soggOrd non puo' essere nullo
		
		//1. PER STESSO SOGG OK
		if(soggMov!=null && !StringUtils.isEmpty(soggMov.getCodiceSoggetto())){
			//IL MOVIMENTO HA UN SOGGETTO
			if(StringUtils.sonoUguali(soggMov.getCodiceSoggetto() , soggOrd.getCodiceSoggetto())){
				//STESSO SOGGETTO: ritorno true
				 return true;
			} else {
				//SOGGETTI DIVERSI
				return false;
			}
		}
		
		/**
		 	SIAC-5745
		 	Commento i controlli sulla classe soggetto. 
		 	Il servizio da errore solo per soggetti esplicitamente diversi.
		 	Gli altri casi sulle classi sono gia' trattati dal front end che
		 	ha sottoposto un warning di conferma all'utente.
		 
		Integer idEnte = datiOperazione.getSiacTEnteProprietario().getUid();
		
		if(classeSoggMov!=null && !StringUtils.isEmpty(classeSoggMov.getCodice())){
			String codClasse = classeSoggMov.getCodice();
			SiacDSoggettoClasseFin siacDSoggettoClasseFin = CommonUtils.getFirst(siacDSoggettoClasseRepository.findValidoByCode(idEnte, datiOperazione.getSiacDAmbito().getAmbitoId(), codClasse,getNow()));
			if(siacDSoggettoClasseFin!=null){
				int idClasse = siacDSoggettoClasseFin.getUid();
				
				List<String> codiciSoggetti = siacRSoggettoClasseRepository.findCodiciSoggettiValidiByIdClasse(idClasse, getNow());
				
				if(StringUtils.isEmpty(codiciSoggetti)){
					//2. IL MOV HA UNA CLASSE PRIVA DI SOGGETTI OK
					return true;
				} else {
					//3. IL MOV HA UNA CLASSE CON SOGGETTI-> OK SE IL SOGG DELL'ORD E' TRA QUESTI
					String codSoggOrd = soggOrd.getCodiceSoggetto();
					if(StringUtils.contenutoIn(codSoggOrd, codiciSoggetti)){
						return true;
					} else {
						return false;
					}
				}
			}
		}
		*/
		
		//sempre per SIAC-5745 occore ritornare true come default invece che false:
		return true;
		
	}

	/**
	 * 
	 *     Verifica che il disponibile a incassare degli accertamenti
	 *	   sia maggiore della sommatoria degli ordinativi 
	 *	   di incasso delle rige di ritenute in cui e' indicato tale accertamento.
	 *
	 *     Per gli accertamenti che non rispettano tale condizione non viene 
	 *     rilanciato nessun errore ma vengono segnati per subire una modifica di importo
	 *     per mettere in pari la disponibilita coprendo il delta.
	 * 
	 * @param esito
	 * @return
	 */
	private OrdinativoInReintroitoInfoDto controlliDisponibilitaPagareAccertamenti(OrdinativoInReintroitoInfoDto esito,Richiedente richiedente){
		List<RitenutaSpiltPerReintroitoInfoDto> listaRitenuteInfo = esito.getListaRitenuteSplit();
		// CONTROLLIAMO I DISP AD ACCERTARE DEGLI GLI ACCERTAMENTI (o sub) DELLE EVENTUALI RITENUTE:
		
		//PER CMTO:
		boolean lanciaErrore = isAbilitato(richiedente.getAccount(), AzioniConsentite.PREDOCUMENTO_ENTRATA_MODIFICA_ACC_NON_AMMESSA.toString());
		//
		
		if(!StringUtils.isEmpty(listaRitenuteInfo)){
			
			List<RitenuteReintroitoConStessoMovimentoDto> raggruppatePerStessoAccertamento = esito.raggruppatePerAccertamenti();
			
			List<AccertamentoModAutomaticaPerReintroitoInfoDto> modificheAutomaticheNecessarie = new ArrayList<AccertamentoModAutomaticaPerReintroitoInfoDto>();
			
			for(RitenuteReintroitoConStessoMovimentoDto gruppoIt: raggruppatePerStessoAccertamento){
				
				//AccertamentoPerReintroitoInfoDto accInfo = ritenutaIt.getAccertamento();
				AccertamentoPerReintroitoInfoDto accInfo =  gruppoIt.getListaRitenute().get(0).getAccertamento();
				MovimentoKey movKey = accInfo.getKey();
				
			    List<RitenutaSpiltPerReintroitoInfoDto> ritenuteConQuestoAccOrQuestoSub = gruppoIt.getListaRitenute();
				//List<RitenutaSpiltPerReintroitoInfoDto> ritenuteConQuestoImpOrQuestoSub = ReintroitoUtils.conStessoAccertamento(listaRitenuteInfo, movKey);
				BigDecimal sommaImportiOrdinativiCollegati = ReintroitoUtils.sommaImportiOrdinativiIncasso(ritenuteConQuestoAccOrQuestoSub);
				BigDecimal dispIncassareAccOSub = accInfo.getDisponibileIncassareAccOSub();
				if(dispIncassareAccOSub.compareTo(sommaImportiOrdinativiCollegati)<0){
					
					if(lanciaErrore){
						//CASO PARTICOLARE PER CMTO che conduce ad errore bloccante
						String nomeEntita = ReintroitoUtils.buildNomeEntitaPerMessaggiDiErrore(movKey, false);
						String codiceEntita = ReintroitoUtils.buildCodiceEntitaPerMessaggiDiErrore(movKey);
						esito.addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore(nomeEntita + ": " + codiceEntita, "La sua disponibilita' a pagare e' minore degli importi da reintroitare"));
						return esito;
					} else {
						//per l'accertamento la mancata disponibilita ad incassare non conduce ad un errore bloccante
						//ma viene creata una modifica di importo su tale accertamento per colmare la differenza
						BigDecimal modifica = sommaImportiOrdinativiCollegati.subtract(dispIncassareAccOSub);
						AccertamentoModAutomaticaPerReintroitoInfoDto modificaRichiesta = new AccertamentoModAutomaticaPerReintroitoInfoDto(accInfo, modifica);
						modificheAutomaticheNecessarie.add(modificaRichiesta);
					}
					
				}
					
			}
			
			//RIPORTO LE EVENTUALI MODIFICHE AUTOMATICHE DA ESEGUIRE:
			esito.setModificheAutomaticheNecessarie(modificheAutomaticheNecessarie);
			//
			
		}
		return esito;
	}
	
	/**
	 * 
	 * Controlla che il disponibile a liquidare dell'impegno (o sub) destinazione e degli eventuali 
	 * impegni (o sub) delle ritenute sia/siano sufficiente/i.
	 * 
	 * Controlla che il disponibile a pagare del capitolo dell'impegno (o sub) destinazione e degli eventuali 
	 * capitoli degli impegni (o sub) delle ritenute sia/siano sufficiente/i.
	 * 
	 * @param esito
	 * @return
	 */
	private OrdinativoInReintroitoInfoDto controlliDisponibilitaImpegni(OrdinativoInReintroitoInfoDto esito){
		//   Verifichiamo che il disponibile a liquidare dell'impegno di destinazione
		//   sia maggiore dell'importo netto + (l'eventuale) sommatoria degli ordinativi 
		//   di incasso delle rige di ritenute in cui e' (eventualmente) indicato tale impegno
		
		//   Analogamente facciamo per il disp a pagare dei capitoli degli impegni o sub
		
		OrdinativoPagamento ordinativoPagamentoRicaricato = esito.getOrdinativoPagamento();
		
		MovimentoKey impegnoDestinazioneKey = esito.getImpegnoDestinazione().getKey();
		
		BigDecimal importoNetto = ReintroitoUtils.calcolaImportoNettoPerReintroito(ordinativoPagamentoRicaricato);
		
		List<RitenutaSpiltPerReintroitoInfoDto> listaRitenuteInfo = esito.getListaRitenuteSplit();
		
		//CONTROLLIAMO IL DISP A LIQUIDARE DELL'IMPEGNO DESTINZIONE:
		esito = controlloDisponibilitaLiquidareImpegno(esito.getImpegnoDestinazione(), esito, importoNetto);
		if(esito.presenzaErrori()){
			return esito;
		}
		
		//CONTROLLIAMO IL DISP A PAGARE DEL CAPITOLO DELL'IMPEGNO DESTINZIONE:
		esito = controlloDisponibilitaPagareCapitoloImpegno(esito.getImpegnoDestinazione(), esito, importoNetto);
		if(esito.presenzaErrori()){
			return esito;
		}
		
		// CONTROLLIAMO I DISP A LIQUIDARE E A PAGARE DEI CAPITOLI DEGLI GLI ALTRI IMPEGNI (o sub) :
		if(!StringUtils.isEmpty(listaRitenuteInfo)){
			for(RitenutaSpiltPerReintroitoInfoDto ritenutaIt: listaRitenuteInfo){
				//DEVO ESCLUDERE QUELLO DESTINAZIONE IN QUANTO E' GIA' STATO VERIFICATO SOPRA:
				if(!ReintroitoUtils.sonoUguali(ritenutaIt.getImpegno().getKey(), impegnoDestinazioneKey)){
					//qui bisogna passare importo netto a null:
					esito = controlloDisponibilitaLiquidareImpegno(ritenutaIt.getImpegno(), esito, null);
					if(esito.presenzaErrori()){
						return esito;
					}
					//qui bisogna passare importo netto a null:
					esito = controlloDisponibilitaPagareCapitoloImpegno(ritenutaIt.getImpegno(), esito, null);
					if(esito.presenzaErrori()){
						return esito;
					}
				}
			}
		}
		
		return esito;
	}
	
	/**
	 * Dato un certo impegno (o sub) verifica che il suo disponibile a liquidare sia maggiore o uguale
	 * alla sommatoria degli importi degli ordinativi di incasso per i quali e' stato indicato di utilizzare
	 * tale impegno per il reintroito piu' il valore di importoNetto.
	 * 
	 * importoNetto passatoci o meno dal chiamante e' definito come:
	 * Importo ord pag in reintroito - SOMMATORIA importi degli ordinativi di incasso ad esso collegati
	 * 
	 * @param impInfo
	 * @param esito
	 * @param importoNetto
	 * @return
	 */
	private OrdinativoInReintroitoInfoDto controlloDisponibilitaLiquidareImpegno(ImpegnoPerReintroitoInfoDto impInfo,OrdinativoInReintroitoInfoDto esito, BigDecimal importoNetto){
		MovimentoKey movKey = impInfo.getKey();
		if(importoNetto==null){
			//il chiamante ci passare importo netto valorizzato per il caso dell'impegno destinazione del reintroito
			//per il quale si deve verificare che:
			// dispLiqImpOSubDestinazione >= importoNetto + sommaImportiOrdinativiCollegati
			//invece per tutti gli altri impegni delle (eventuali) ritenute importo netto non va considerato in quella uguaglianza
			//da rispettare
			importoNetto = BigDecimal.ZERO;
		}
		List<RitenutaSpiltPerReintroitoInfoDto> listaRitenuteInfo = esito.getListaRitenuteSplit();
		List<RitenutaSpiltPerReintroitoInfoDto> ritenuteConQuestoImpOrQuestoSub = ReintroitoUtils.conStessoImpegno(listaRitenuteInfo, movKey);
		BigDecimal sommaImportiOrdinativiCollegati = ReintroitoUtils.sommaImportiOrdinativiIncasso(ritenuteConQuestoImpOrQuestoSub);
		BigDecimal dispLiqImpOSub = impInfo.getDisponibileLiquidareImpOSub();
		if(dispLiqImpOSub.compareTo(importoNetto.add(sommaImportiOrdinativiCollegati))<0){
			String nomeEntita = ReintroitoUtils.buildNomeEntitaPerMessaggiDiErrore(movKey, true);
			String codiceEntita = ReintroitoUtils.buildCodiceEntitaPerMessaggiDiErrore(movKey);
			esito.addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore(nomeEntita + ": " + codiceEntita, "La sua disponibilita' a liquidare e' minore degli importi da reintroitare"));
			return esito;
		}
		return esito;
	}
	
	/**
	 * Dato un certo impegno (o sub) verifica che il disponibile a pagare del suo capitolo sia maggiore o uguale
	 * alla sommatoria degli importi degli ordinativi di incasso per i quali e' stato indicato di utilizzare
	 * tale impegno per il reintroito piu' il valore di importoNetto.
	 * 
	 * importoNetto passatoci o meno dal chiamante e' definito come:
	 * Importo ord pag in reintroito - SOMMATORIA importi degli ordinativi di incasso ad esso collegati
	 * 
	 * @param impInfo
	 * @param esito
	 * @param importoNetto
	 * @return
	 */
	private OrdinativoInReintroitoInfoDto controlloDisponibilitaPagareCapitoloImpegno(ImpegnoPerReintroitoInfoDto impInfo,OrdinativoInReintroitoInfoDto esito, BigDecimal importoNetto){
		MovimentoKey movKey = impInfo.getKey();
		if(importoNetto==null){
			//il chiamante ci passare importo netto valorizzato per il caso dell'impegno destinazione del reintroito
			//per il quale si deve verificare che:
			// dispLiqImpOSubDestinazione >= importoNetto + sommaImportiOrdinativiCollegati
			//invece per tutti gli altri impegni delle (eventuali) ritenute importo netto non va considerato in quella uguaglianza
			//da rispettare
			importoNetto = BigDecimal.ZERO;
		}
		List<RitenutaSpiltPerReintroitoInfoDto> listaRitenuteInfo = esito.getListaRitenuteSplit();
		List<RitenutaSpiltPerReintroitoInfoDto> ritenuteConQuestoImpOrQuestoSub = ReintroitoUtils.conStessoImpegno(listaRitenuteInfo, movKey);
		BigDecimal sommaImportiOrdinativiCollegati = ReintroitoUtils.sommaImportiOrdinativiIncasso(ritenuteConQuestoImpOrQuestoSub);
		BigDecimal dispPagareCapitolo = impInfo.getImpegno().getCapitoloUscitaGestione().getImportiCapitolo().getDisponibilitaPagare();
		if(dispPagareCapitolo.compareTo(importoNetto.add(sommaImportiOrdinativiCollegati))<0){
			String nomeEntita = ReintroitoUtils.buildNomeEntitaPerMessaggiDiErrore(movKey, true);
			String codiceEntita = ReintroitoUtils.buildCodiceEntitaPerMessaggiDiErrore(movKey);
			esito.addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore(nomeEntita + ": " + codiceEntita, "La sua disponibilita' a pagare del suo capitolo e' minore degli importi da reintroitare"));
			return esito;
		}
		return esito;
	}
	
	/**
	 * 
	 * 1. Ricerca e quindi verifica l'esistenza dei movimenti ricevuti in input
	 * 2. Verifica lo stato dei movimenti ricevuti in input
	 * 3. Popola le strutture dati AccertamentoPerReintroitoInfoDto e ImpegnoPerReintroitoInfoDto 
	 *    con i dati dei movimenti caricati 
	 * 4. Popola le strutture dati RitenutaSpiltPerReintroitoInfoDto con i dati degli ordinativi di incasso
	 *    relativi ad ogni riga
	 *    
	 *    
	 *    ps. utilizza i paramentri HashMap<String, EsitoRicercaMovimentoPkDto> impegniCaricati,
	 *                              HashMap<String, EsitoRicercaMovimentoPkDto> accertamentiCaricati
	 *    come cache per non ricercare due volte lo stesso movimento/sub movimento
	 * 
	 * @param datiInput
	 * @param esito
	 * @param richiedente
	 * @param ente
	 * @param datiOperazione
	 * @param ordinativoPagamentoRicaricato
	 * @param impegniCaricati
	 * @param accertamentiCaricati
	 * @return
	 */
	private OrdinativoInReintroitoInfoDto controlliEsistenzaEStatoMovimentiRitenute(OrdinativoInReintroitoDatiDiInputDto datiInput,
			OrdinativoInReintroitoInfoDto esito,
			Richiedente richiedente, Ente ente, DatiOperazioneDto datiOperazione,
			OrdinativoPagamento ordinativoPagamentoRicaricato,
			HashMap<String, EsitoRicercaMovimentoPkDto> impegniCaricati,HashMap<String, EsitoRicercaMovimentoPkDto> accertamentiCaricati){
		
		if(!StringUtils.isEmpty(datiInput.getRitenuteSplit())){
			
			List<RitenutaSpiltPerReintroitoInfoDto> listaRitenuteSplit = new ArrayList<RitenutaSpiltPerReintroitoInfoDto>();
			
			for(ReintroitoRitenutaSplit it: datiInput.getRitenuteSplit()){
				
				//IMPEGNO:
				EsitoRicercaMovimentoPkDto caricatoItImp = caricaImpegnoPerReintroito(it.getImpegnoDiDestinazione(), richiedente, ente,impegniCaricati);
				esito = controlloEsistenzaEStatoMovimento(caricatoItImp, it.getImpegnoDiDestinazione(), esito, true);
				if(esito.presenzaErrori()){
					return esito;
				}
				
				//ACCERTAMENTO:
				EsitoRicercaMovimentoPkDto caricatoItAcc = caricaAccertamentoPerReintroito(it.getAccertamentoDiDestinazione(), richiedente, ente,accertamentiCaricati);
				esito = controlloEsistenzaEStatoMovimento(caricatoItAcc, it.getAccertamentoDiDestinazione(), esito, false);
				if(esito.presenzaErrori()){
					return esito;
				}
				
				//OK CONTROLLI SUPERATI ..
				
				//COSTRUISCO UN OGGETTO RitenutaSpiltPerReintroitoInfoDto
				RitenutaSpiltPerReintroitoInfoDto ritenutaInfo = buildRitenutaSpiltPerReintroitoInfoDto(caricatoItImp, caricatoItAcc, ordinativoPagamentoRicaricato, it);
				
				//E LO AGGIUNGO ALLA LISTA: 
				listaRitenuteSplit.add(ritenutaInfo);
				
			}
			
			//SETTO LA LISTA COSTRUITA:
			esito.setListaRitenuteSplit(listaRitenuteSplit);
			
		}
		return esito;
	}
	
	
	/**
	 * 
	 * Costruisce un elemento RitenutaSpiltPerReintroitoInfoDto a partire dai dati appena caricati 
	 * dell'accertamento, dell'impegno e dell'ordinativo di incasso di una riga split.
	 * 
	 * @param caricatoItImp
	 * @param caricatoItAcc
	 * @param ordinativoPagamentoRicaricato
	 * @param it
	 * @return
	 */
	private RitenutaSpiltPerReintroitoInfoDto buildRitenutaSpiltPerReintroitoInfoDto(EsitoRicercaMovimentoPkDto caricatoItImp,
			EsitoRicercaMovimentoPkDto caricatoItAcc, OrdinativoPagamento ordinativoPagamentoRicaricato,
			ReintroitoRitenutaSplit ritenutaSplit){
		//OK CONTROLLI SUPERATI AGGIUNGO L'ELEMENTO:
		RitenutaSpiltPerReintroitoInfoDto ritenutaInfo = new RitenutaSpiltPerReintroitoInfoDto();
		
		//SETTO L'ACCERTAMENTO:
		ritenutaInfo.setAccertamento(buildAccertamentoPerReintroitoInfoDto(caricatoItAcc,ritenutaSplit.getAccertamentoDiDestinazione()));
		
		//SETTO L'IMPEGNO:
		ritenutaInfo.setImpegno(buildImpegnoPerReintroitoInfoDto(caricatoItImp,ritenutaSplit.getImpegnoDiDestinazione()));
		
		//RECUPERO E SETTO L'ORDINATIVO DI INCASSO ITERATO:
		List<Ordinativo> elencoOrdinativiIncasso = ordinativoPagamentoRicaricato.getElencoOrdinativiCollegati();
		Ordinativo ordIt = ReintroitoUtils.getByKeyOrdinativo(elencoOrdinativiIncasso, ritenutaSplit.getOrdinativoIncasso());
		ritenutaInfo.setOrdinativoIncasso((OrdinativoIncasso)ordIt);
		ritenutaInfo.setOrdinativoIncassoKey(ritenutaSplit.getOrdinativoIncasso());
		//
		
		return ritenutaInfo;
	}
	
	/**
	 * Costruisce un oggetto AccertamentoPerReintroitoInfoDto a partire dal risultato della ricerca accertamento pk
	 * @param caricatoItAcc
	 * @param movimentoKey 
	 * @return
	 */
	private AccertamentoPerReintroitoInfoDto buildAccertamentoPerReintroitoInfoDto(EsitoRicercaMovimentoPkDto caricatoItAcc, MovimentoKey movimentoKey){
		AccertamentoPerReintroitoInfoDto accInfo = new AccertamentoPerReintroitoInfoDto();
		accInfo.setEsitoRicercaMovPkDto(clone(caricatoItAcc));
		Accertamento acc = (Accertamento) caricatoItAcc.getMovimentoGestione();
		//Carico i dati di movgest e movgestts:
		MovGestInfoDto mgiDtoAcc = caricaSiactMovgestByAccertamento(acc);
		accInfo.setMovGestInfoAccertamento(mgiDtoAcc);
		//
		accInfo.setAccertamento(acc);
		if(!StringUtils.isEmpty(acc.getElencoSubAccertamenti())){
			SubAccertamento subAccertamento = acc.getElencoSubAccertamenti().get(0);
			//Carico i dati di movgest e movgestts:
			MovGestInfoDto mgiDtoSubAcc = caricaSiactMovgestBySubAccertamento(subAccertamento);
			accInfo.setMovGestInfoSubAccertamento(mgiDtoSubAcc);
			//
			accInfo.setSubAccertamento(subAccertamento);
		}
		accInfo.setKey(movimentoKey);
		return accInfo;
	}
	
	/**
	 * Costruisce un oggetto ImpegnoPerReintroitoInfoDto a partire dal risultato della ricerca impegno pk
	 * @param caricatoItImp
	 * @return
	 */
	private ImpegnoPerReintroitoInfoDto buildImpegnoPerReintroitoInfoDto(EsitoRicercaMovimentoPkDto caricatoItImp, MovimentoKey movimentoKey){
		ImpegnoPerReintroitoInfoDto impInfo = new ImpegnoPerReintroitoInfoDto();
		impInfo.setEsitoRicercaMovPkDto(clone(caricatoItImp));
		Impegno imp = (Impegno) caricatoItImp.getMovimentoGestione();
		//Carico i dati di movgest e movgestts:
		MovGestInfoDto mgiDtoImp = caricaSiactMovgestByImpegno(imp);
		impInfo.setMovGestInfoImpegno(mgiDtoImp);
		//
		impInfo.setImpegno(imp);
		if(!StringUtils.isEmpty(imp.getElencoSubImpegni())){
			SubImpegno subImpegno = imp.getElencoSubImpegni().get(0);
			//Carico i dati di movgest e movgestts:
			MovGestInfoDto mgiDtoSubImp = caricaSiactMovgestBySubImpegno(subImpegno);
			impInfo.setMovGestInfoSubImpegno(mgiDtoSubImp);
			//
			impInfo.setSubImpegno(subImpegno);
		}
		impInfo.setKey(movimentoKey);
		return impInfo;
	}
	
	/**
	 * Controllo in comune a tutti gli accertamenti e impegni indicati per il reintroito.
	 * 
	 * Qui si mettono i soli controlli 'puntuali' che vertono sul singolo movimento senza
	 * considerare controlli intrecciati o legati agli altri movimenti (che vengono svolti da altri
	 * metodi specifici)
	 * 
	 * @param caricatoItImp
	 * @param impRichiesto
	 * @param esito
	 * @param impegno
	 * @return
	 */
	private OrdinativoInReintroitoInfoDto controlloEsistenzaEStatoMovimento(EsitoRicercaMovimentoPkDto caricatoItImp,MovimentoKey impRichiesto,OrdinativoInReintroitoInfoDto esito,boolean impegno) {
		
		//1. esistenza
		esito = controlloEsistenzaMovimento(caricatoItImp, impRichiesto, esito, impegno);
		if(esito.presenzaErrori()){
			return esito;
		}
		
		//2. stato
		esito = controlloStatoMovimento(caricatoItImp, impRichiesto, esito, impegno);
		if(esito.presenzaErrori()){
			return esito;
		}
		
		/**  SIAC-5827 rimosso controllo su piano dei conti,
			 resta solo piu un warning lato finapp
			 
		//3. pdc
		esito = controlloPianoDeiContiMovimento(caricatoItImp, impRichiesto, esito, impegno);
		if(esito.presenzaErrori()){
			return esito;
		}
		*/
		
		return esito;
	}
	
	
	/**
	 * Verifica che il movimento indicato abbia come piano dei conti 
	 * 
	 * U.7.01.99.01.001 di quinto livello per impegno
	 * E.9.01.99.99.999 di quinto livello per accertamento
	 * 
	 * @param caricatoItImp
	 * @param movKey
	 * @param esito
	 * @param impegno
	 * @return
	 */
	private OrdinativoInReintroitoInfoDto controlloPianoDeiContiMovimento(EsitoRicercaMovimentoPkDto caricatoItImp,MovimentoKey movKey,OrdinativoInReintroitoInfoDto esito,boolean impegno) {
		
		
		MovimentoGestione movimento = caricatoItImp.getMovimentoGestione();
		
		String nomeEntita = ReintroitoUtils.buildNomeEntitaPerMessaggiDiErrore(movKey, impegno);
		String codiceEntita = ReintroitoUtils.buildCodiceEntitaPerMessaggiDiErrore(movKey);
		String nomeEsteso = nomeEntita  + " " + codiceEntita;
		
		if(impegno){
			
			//CONTROLLO PER IMPEGNO
			
			if(!Constanti.D_CLASS_TIPO_PIANO_DEI_CONTI_V.equalsIgnoreCase(movimento.getCodicePdc())
					|| !movimento.getCodPdc().equalsIgnoreCase("U.7.01.99.01.001")){
				String errorePdc = " - Attenzione l'impegno selezionato non e' in partita di giro, deve appartenere al piano dei conti: U.7.01.99.01.001";
				esito.addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore(nomeEsteso, errorePdc));
			}
			
		} else {
			
			//CONTROLLO PER ACCERTAMENTO
			
			if(!Constanti.D_CLASS_TIPO_PIANO_DEI_CONTI_V.equalsIgnoreCase(movimento.getCodicePdc())
					|| !movimento.getCodPdc().equalsIgnoreCase("E.9.01.99.99.999")){
				String errorePdc = " - Attenzione l'accertamento selezionato non e' in partita di giro, deve appartenere al piano dei conti: E.9.01.99.99.999";
				esito.addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore(nomeEsteso, errorePdc));
			}
			
		}
		
		return esito;
	}
	
	/**
	 * Messo a fattor comune il controllo di esistenza di un impegno/accertamento/subimpegno/subaccertamento
	 * per i vari movimenti indicati in input in fase di reintroito di un ordinativo di pagamneto
	 * @param caricatoItImp
	 * @param impRichiesto
	 * @param esito
	 * @param impegno
	 * @return
	 */
	private OrdinativoInReintroitoInfoDto controlloEsistenzaMovimento(EsitoRicercaMovimentoPkDto caricatoItImp,MovimentoKey impRichiesto,OrdinativoInReintroitoInfoDto esito,boolean impegno) {
		
		String nomeEntita = ReintroitoUtils.buildNomeEntitaPerMessaggiDiErrore(impRichiesto, impegno);
		String codiceEntita = ReintroitoUtils.buildCodiceEntitaPerMessaggiDiErrore(impRichiesto);
		
		boolean errore = false;
		if(caricatoItImp==null || caricatoItImp.getMovimentoGestione()==null || caricatoItImp.getMovimentoGestione().getNumero()==null){
			errore = true;
		} else if(CommonUtils.maggioreDiZero(impRichiesto.getNumeroSubMovimento()) && caricatoItImp != null && caricatoItImp.getMovimentoGestione()!=null){
			//CONTROLLIA DI NON ESSERE NEL CASO IN CUI SIA STATO INDICATO UN SUB
			//MA IL SERVIZIO NON CI HA RESITUITO TALE SUB DOVE E' ATTESO
			errore = subRichiestoNonTrovato(caricatoItImp, impRichiesto, impegno);
		}
		
		if(errore){
			esito.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore(nomeEntita,codiceEntita));
		}
		
		return esito;
	}
	
	/**
	 * Metodo interno a controlloEsistenzaMovimento
	 * 
	 * CONTROLLIA DI NON ESSERE NEL CASO IN CUI SIA STATO INDICATO UN SUB
     * MA IL SERVIZIO NON CI HA RESITUITO TALE SUB DOVE E' ATTESO CIOE'
     * COME PRIMO ED UNICO ELMENTO DI impegno.elencoSubImpegni o accertamento.elencoSubAccertamenti
     * 
	 * @param caricatoItImp
	 * @param impRichiesto
	 * @param impegno
	 * @return
	 */
	private boolean subRichiestoNonTrovato(EsitoRicercaMovimentoPkDto caricatoItImp,MovimentoKey impRichiesto, boolean impegno){
		boolean errore = false;
		if(impegno){
			Impegno imp = (Impegno) caricatoItImp.getMovimentoGestione();
			if(StringUtils.isEmpty(imp.getElencoSubImpegni())){
				errore = true;
			} else {
				SubImpegno subCheMiApetto = imp.getElencoSubImpegni().get(0);
				if(subCheMiApetto == null || subCheMiApetto.getNumero()==null 
						|| (subCheMiApetto.getNumero().intValue()!= impRichiesto.getNumeroSubMovimento().intValue())){
					errore = true;
				}
			}
		} else {
			Accertamento acc = (Accertamento) caricatoItImp.getMovimentoGestione();
			if(StringUtils.isEmpty(acc.getElencoSubAccertamenti())){
				errore = true;
			} else {
				SubAccertamento subCheMiApetto = acc.getElencoSubAccertamenti().get(0);
				if(subCheMiApetto == null || subCheMiApetto.getNumero()==null 
						|| (subCheMiApetto.getNumero().intValue()!= impRichiesto.getNumeroSubMovimento().intValue())){
					errore = true;
				}
			}
		}
		return errore;
	}
	
	private OrdinativoInReintroitoInfoDto controlloStatoMovimento(EsitoRicercaMovimentoPkDto caricatoItImp,MovimentoKey impRichiesto,OrdinativoInReintroitoInfoDto esito,boolean impegno) {
		
		boolean isSub = CommonUtils.maggioreDiZero(impRichiesto.getNumeroSubMovimento());
		
		String nomeEntita = ReintroitoUtils.buildNomeEntitaPerMessaggiDiErrore(impRichiesto, impegno);
		String codiceEntita = ReintroitoUtils.buildCodiceEntitaPerMessaggiDiErrore(impRichiesto);
		
		MovimentoGestione mg = caricatoItImp.getMovimentoGestione();
		
		String statoCode = null;
		if(impegno){
			Impegno imp = (Impegno)mg;
			if(isSub){
				SubImpegno subImp = (SubImpegno) imp.getElencoSubImpegni().get(0);
				statoCode = subImp.getStatoOperativoMovimentoGestioneSpesa();
			} else {
				statoCode = imp.getStatoOperativoMovimentoGestioneSpesa();
			}
		} else {
			Accertamento acc = (Accertamento)mg;
			if(isSub){
				SubAccertamento subAcc = (SubAccertamento) acc.getElencoSubAccertamenti().get(0);
				statoCode = subAcc.getStatoOperativoMovimentoGestioneEntrata();
			} else {
				statoCode = acc.getStatoOperativoMovimentoGestioneEntrata();
			}
		}
		
		if(!Constanti.MOVGEST_STATO_DEFINITIVO.equals(statoCode) && !Constanti.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE.equals(statoCode)){
			esito.addErrore(ErroreFin.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore(nomeEntita + " " +codiceEntita," non definitivo"));
		}
		
		return esito;
	}
	
	/**
	 * 	il chiamante tramite il parametro ritenute ci indica che le vuole tutte reintroitate
	 * 	allo stesso modo, dal punto di vista algoritmico/implementativo e' piu'
	 * 	semplice ricondursi ad un formato unico, ovvero anche nel caso in cui le voglia
	 * 	tutte uguali spalmo tale indicazione su tutti gli ordinativi di incasso:
	 * @param datiInput
	 * @param ordinativiIncassoSulDb
	 * @return
	 */
	private OrdinativoInReintroitoDatiDiInputDto impostaSplitSeRichiesteUguali(OrdinativoInReintroitoDatiDiInputDto datiInput,List<Ordinativo> ordinativiIncassoSulDb) {
		ReintroitoRitenute ritenute = datiInput.getRitenute();
		List<ReintroitoRitenutaSplit> ritenuteSplit = datiInput.getRitenuteSplit();
		if(!ReintroitoUtils.isEmpty(ritenute) && ReintroitoUtils.isEmpty(ritenuteSplit)
				&& !StringUtils.isEmpty(ordinativiIncassoSulDb)){
			List<ReintroitoRitenutaSplit> ritSplits = ReintroitoUtils.buildSplits(ritenute, ordinativiIncassoSulDb);
			//setto l'elenco esploso:
			datiInput.setRitenuteSplit(ritSplits);
			//azzerto il riepilogo:
			datiInput.setRitenute(null);
		}
		return datiInput;
	}

	/**
	 * 
	 * Tutti i controlli che riguardano le ritenute da reintroitare
	 * 
	 * @param ordinativoPagamentoRicaricato
	 * @param datiInput
	 * @param esito
	 * @param datiOperazione
	 * @return
	 */
	private OrdinativoInReintroitoInfoDto controlliRitenute(OrdinativoPagamento ordinativoPagamentoRicaricato,OrdinativoInReintroitoDatiDiInputDto datiInput, OrdinativoInReintroitoInfoDto esito,DatiOperazioneDto datiOperazione){
		List<Ordinativo> elencoOrdinativiIncasso = ordinativoPagamentoRicaricato.getElencoOrdinativiCollegati();
		
		if(!StringUtils.isEmpty(elencoOrdinativiIncasso)){
			//SE L'ORDINATIVO DI PAGAMENTO HA DEGLI ORDINATIVI DI INCASSO MI ASPETTO
			//CHE IL CHIAMANTE MI ABBIA INDICATO COME REINTROITARLI 
			
			ReintroitoRitenute ritenute = datiInput.getRitenute();
			List<ReintroitoRitenutaSplit> ritenuteSplit = datiInput.getRitenuteSplit();
			
			//1. Controlliamo che siano formalmente popolati correttamente:
			if(!ReintroitoUtils.tuttiCoerenti(ritenuteSplit)){
				//ALMENO UNA RIGA E' SPROVVISTA DI UN DATO TRA ORDINATIVO DI INCASSO, IMPEGNO E ACCERTAMENTO
				//(CHE INVECE DEVONO ESSERCI TUTTI PER OGNI RIGA)
				esito.addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore("Ritenute", "Compilare tutte le righe con ordinativi, impegni e accertamenti"));
				return esito;
			}
			
			//INDICARE ritenute o ritenuteSpli:
			if(ReintroitoUtils.isEmpty(ritenute) && ReintroitoUtils.isEmpty(ritenuteSplit)){
				//L'ord di pagamento ha degli ord incasso collegati occorre indicare come reintroitarli
				esito.addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore("Ritenute", "L'ordinativo ha degli ordinativi di incasso collegati, indicare come reintroitarli."));
				return esito;
			}
			
			//INDICARE uno solo tra ritenute e ritenuteSplit:
			if(!ReintroitoUtils.isEmpty(ritenute) && !ReintroitoUtils.isEmpty(ritenuteSplit)){
				//L'ord di pagamento ha degli ord incasso collegati occorre indicare come reintroitarli
				esito.addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore("Ritenute", "Indicare uno solo tra ritenute e ritenuteSplit."));
				return esito;
			}
			
			//2. A questo punto sapendo che ci sono ordinativi sul db e che i dati in input sono almeno 
			//formalmente corretti verifichiamo che gli ordinativi di incasso in input
			//corrispondano a quelli presenti sul db:
			esito = controlliCorrispondenzeRitenute(ordinativoPagamentoRicaricato, datiInput, esito, datiOperazione);
			if(esito.presenzaErrori()){
				return esito;
			}
			//
			
		}
		
		return esito;
	}
	
	/**
	 * 
	 * PREMESSA: Questo metodo viene chiamato dove gia' e' certo
	 * che l'ordinativo di pagamento abbia degli ordinativi di incasso collegati.
	 * 
	 * Data questa premessa lo scopo di questo meotodo
	 * e' controllare che in input al servizio di reintroito siano stati indicati
	 * i rifermenti corretti per reintroitare tali ordinativi di incasso.
	 * 
	 * Verifica in sostanza il 'match' tra input e situzione sul db.
	 * 
	 * @param ordinativoPagamentoRicaricato
	 * @param datiInput
	 * @param esito
	 * @param datiOperazione
	 * @return
	 */
	private OrdinativoInReintroitoInfoDto controlliCorrispondenzeRitenute(OrdinativoPagamento ordinativoPagamentoRicaricato,OrdinativoInReintroitoDatiDiInputDto datiInput, OrdinativoInReintroitoInfoDto esito,DatiOperazioneDto datiOperazione){
		
		//strutture dati di comodo:
		List<Ordinativo> ordinativiDb = ordinativoPagamentoRicaricato.getElencoOrdinativiCollegati();
		List<OrdinativoKey> ordinativiDbKeys = ReintroitoUtils.toOrdKeys(ordinativiDb);
		List<ReintroitoRitenutaSplit> ritenuteSplit = datiInput.getRitenuteSplit();
		List<OrdinativoKey> ordinativiInInputKey = ReintroitoUtils.estraiListaOrdinativiIncasso(ritenuteSplit);
		ReintroitoRitenute ritenute = datiInput.getRitenute();
		
		//CASO 1. 
		if(!ReintroitoUtils.isEmpty(ritenute) && ReintroitoUtils.isEmpty(ritenuteSplit)){
			//QUESTO E' IL CASO PIU' SEMPLICE, IN INPUT AL SERVIIZIO
			//ARRIVA POPOLATO SOLO L'OGGETO DI RIEPILOGO 'RITENUTE' 
			//CHE INDICA AL SERVIZIO DI REINTROITARE TUTTI GLI ORDINATIVI DI INCASSO 
			//ALLO STESSO MODO, IL METODO TERMINA SUBITO SENZA RIPORTARE ERRORI:
			return esito;
		}
		
		//CASO 2. Cerchiamo quelli presenti sul db ma non ricevuti in input
		for(OrdinativoKey dbIt: ordinativiDbKeys){
			if(dbIt!=null){
				boolean presenteInListaInput = ReintroitoUtils.contenutoInLista(ordinativiInInputKey, dbIt);
				if(!presenteInListaInput){
					String annoNumero = dbIt.getAnno()+"/"+dbIt.getNumero();
					esito.addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore("Ritenute", "Non e' stato indicato come reintroitare l'ordinativo di incasso: "+annoNumero));
					return esito;
				}
			}
		}
		
		//CASO 3. Cerchiamo quelli ricevuti in input ma inesistenti sul db
		for(OrdinativoKey ricevutoIt: ordinativiInInputKey){
			if(ricevutoIt!=null){
				boolean presenteSuDb = ReintroitoUtils.contenutoInLista(ordinativiDbKeys, ricevutoIt);
				if(!presenteSuDb){
					String annoNumero = ricevutoIt.getAnno()+"/"+ricevutoIt.getNumero();
					esito.addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore("Ritenute", "L'ordinativo di incasso: "+annoNumero + " non fa parte di quelli collegati all'ordinativo di pagamento."));
					return esito;
				}
			}
		}
		
		return esito;
	}
	
	/**
	 * 
	 * Carica l'ordinativo indicato verificandone quindi l'esistenza sul db.
	 * 
	 * @param datiInput
	 * @param esito
	 * @param datiOperazione
	 * @return
	 */
	private OrdinativoInReintroitoInfoDto controlliAttoPerReintroito(OrdinativoInReintroitoDatiDiInputDto datiInput, OrdinativoInReintroitoInfoDto esito,DatiOperazioneDto datiOperazione){
		Integer idEnte = datiOperazione.getSiacTEnteProprietario().getUid();
		//CARICHIAMO L'ATTO:
		boolean provvedimentoDaMovimenti = datiInput.isUtilizzaProvvedimentoDaMovimento();
		//riporto il valore del flag:
		esito.setUtilizzaProvvedimentoDaMovimenti(provvedimentoDaMovimenti);
		//
		if(!provvedimentoDaMovimenti){
			AttoAmministrativo attoInput = datiInput.getAttoAmministrativo();
			AttoAmministrativo attoAmministrativoReload = getAttoAministrativo(idEnte, attoInput);
			if(attoAmministrativoReload==null || attoAmministrativoReload.getNumero()<=0){
				//ATTO NON TROVATO
				esito.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Atto amministrativo", attoInput.getAnno() + " / " + attoInput.getNumero()));
				return esito;
			} else {
				esito.setAttoAmministrativo(attoAmministrativoReload);
			}
		}
		return esito;
	}
	
	private EsitoRicercaMovimentoPkDto caricaImpegnoPerReintroito(MovimentoKey impKey, Richiedente richiedente, Ente ente,HashMap<String, EsitoRicercaMovimentoPkDto> impegniCaricati){
		
		EsitoRicercaMovimentoPkDto esito = new EsitoRicercaMovimentoPkDto();
		
		String movKey = ReintroitoUtils.buildMovKey(ente, impKey);
		
		//SE E' GIA' STATO CARICATO HO I DATI IN CACHE:
		if(impegniCaricati!=null && impegniCaricati.containsKey(movKey)){
			return clone(impegniCaricati.get(movKey));
		}
		//
		
		//CARICO L'IMPEGNO:
		
		//SIAC-5686 setto carica dati ulteriori a true perche' serve il soggetto:
		boolean caricaDatiUlteriori = true;
		//
		
		String tipoMovimento = Constanti.MOVGEST_TIPO_IMPEGNO;
		DatiOpzionaliElencoSubTuttiConSoloGliIds caricaDatiOpzionaliDto = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		caricaDatiOpzionaliDto.setEscludiAnnullati(true);
		caricaDatiOpzionaliDto.setCaricaDisponibilePagare(false);
		
		//OTTIMIZZAZIONI CHIAMATA RICERCA IMPEGNO:
		PaginazioneSubMovimentiDto paginazioneSubMovimentiDto = new PaginazioneSubMovimentiDto();
		if(ReintroitoUtils.isSub(impKey)){
			//Selezionato un SUB 
			paginazioneSubMovimentiDto.setNoSub(false);
			paginazioneSubMovimentiDto.setNumeroSubMovimentoRichiesto(impKey.getNumeroSubMovimento());
		} else {
			//Non selezionato un SUB
			paginazioneSubMovimentiDto.setNumeroSubMovimentoRichiesto(null);
			paginazioneSubMovimentiDto.setNoSub(true);
		}
		//
		BigDecimal numeroMovimento = impKey.getNumeroMovimento();
		Integer annoMovimento = impKey.getAnnoMovimento();
		String annoEsercizio = impKey.getAnnoEsercizio().toString();
		OttimizzazioneMovGestDto ottimizazioneDaChiamante = null;
		
		esito =  impegnoOttimizzatoDad.ricercaMovimentoPk(richiedente, ente, annoEsercizio, annoMovimento, numeroMovimento, paginazioneSubMovimentiDto, caricaDatiOpzionaliDto, tipoMovimento, caricaDatiUlteriori, ottimizazioneDaChiamante );
	
		//aggiungo alla cache:
		if(impegniCaricati!=null){
			impegniCaricati.put(movKey, esito);
		}
		//
		
		return esito;
	}
	
	private EsitoRicercaMovimentoPkDto caricaAccertamentoPerReintroito(MovimentoKey accKey, Richiedente richiedente, Ente ente,HashMap<String, EsitoRicercaMovimentoPkDto> accertamentiCaricati){
		
		EsitoRicercaMovimentoPkDto esito = new EsitoRicercaMovimentoPkDto();
		
		String movKey = ReintroitoUtils.buildMovKey(ente, accKey);
		
		//SE E' GIA' STATO CARICATO HO I DATI IN CACHE:
		if(accertamentiCaricati!=null && accertamentiCaricati.containsKey(movKey)){
			return clone(accertamentiCaricati.get(movKey));
		}
		//
		
		//CARICO L'ACCERTAMENTO:
		
		//SIAC-5686 setto carica dati ulteriori a true perche' serve il soggetto:
		boolean caricaDatiUlteriori = true;
		//
		
		String tipoMovimento = Constanti.MOVGEST_TIPO_ACCERTAMENTO;
		DatiOpzionaliElencoSubTuttiConSoloGliIds caricaDatiOpzionaliDto = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		caricaDatiOpzionaliDto.setEscludiAnnullati(true);
		
		//OTTIMIZZAZIONI CHIAMATA RICERCA ACCERTAMENTO:
		PaginazioneSubMovimentiDto paginazioneSubMovimentiDto = new PaginazioneSubMovimentiDto();
		if(ReintroitoUtils.isSub(accKey)){
			//Selezionato un SUB 
			paginazioneSubMovimentiDto.setNoSub(false);
			paginazioneSubMovimentiDto.setNumeroSubMovimentoRichiesto(accKey.getNumeroSubMovimento());
		} else {
			//Non selezionato un SUB
			paginazioneSubMovimentiDto.setNumeroSubMovimentoRichiesto(null);
			paginazioneSubMovimentiDto.setNoSub(true);
		}
		//
		BigDecimal numeroMovimento = accKey.getNumeroMovimento();
		Integer annoMovimento = accKey.getAnnoMovimento();
		String annoEsercizio = accKey.getAnnoEsercizio().toString();
		OttimizzazioneMovGestDto ottimizazioneDaChiamante = null;
		
		esito = accertamentoOttimizzatoDad.ricercaMovimentoPk(richiedente, ente, annoEsercizio, annoMovimento, numeroMovimento, paginazioneSubMovimentiDto, caricaDatiOpzionaliDto, tipoMovimento, caricaDatiUlteriori, ottimizazioneDaChiamante );
		
		//aggiungo alla cache:
		if(accertamentiCaricati!=null){
			accertamentiCaricati.put(movKey, esito);
		}
		//
		
		return esito;
	}
	
	/**
	 * 
	 *  Non ricarica o controlla i dati, assume che il chiamante abbia gia' svolto
	 *  gli opportuni caricamenti e verifiche.
	 * 
	 *	Scollegare l'ordinativo dalla liquidazione (o dalle liquidazioni se ho tante quote)  
	 *	Scollegare l'ordinativo dal capitolo
	 *	Scollegare l'ordinativo dalle classificazioni finanziarie
	 *	Collegare nuovamente l'ordinativo alla liquidazione creata e conseguentemente al suo capitolo e alle sue classificazioni finanziarie 
	 *	Aggiornare la DATA SPOSTAMENTO
	 * 
	 */
	public void spostaOrdinativoPagamentoPerReintroito(OrdinativoPagamento ordinativoPagamentoRicaricato,
			Liquidazione nuovaLiquidazione, DatiOperazioneDto datiOperazioneDto){
		
		Integer idTOrdinativo = ordinativoPagamentoRicaricato.getUid();
		SiacTOrdinativoFin siacTOrd = siacTOrdinativoRepository.findOne(idTOrdinativo);
		
		//1. cambio la liquidazione
		Integer idLiquidazione = nuovaLiquidazione.getUid();
		int idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		List<SiacTOrdinativoTFin> subOrdinativi = siacTOrdinativoTRepository.findSubOrdinativiByOrdinativo(idTOrdinativo, datiOperazioneDto.getTs(),idEnte);
		
		BigDecimal sommaSub = sommaImportiAttuali(subOrdinativi,datiOperazioneDto);
		
		ArrayList<SiacTOrdinativoTFin> subDaEliminare = new ArrayList<SiacTOrdinativoTFin>();
		
		boolean first = true;
		for(SiacTOrdinativoTFin subIt: subOrdinativi){
			if(subIt!=null){
				//RIMUOVO l'eventuale DOCUMENTO: SIAC-6801, questo va fatto per tutti i subdoc collegati altrimenti il subdocumentopotra' mai piu' avere un nuovo ordinativo collegato
				rimuoviSubDocumento(subIt, datiOperazioneDto);
				if(first){
					
					//CAMBIO LA LIQUDAZIONE AL PRIMO SUB ORDINATIVO:
					SiacRLiquidazioneOrdFin siacRLiquidazioneOrd = salvaSiacRLiquidazioneOrd(subIt, datiOperazioneDto,idLiquidazione);
					subIt.setSiacRLiquidazioneOrds(toList(siacRLiquidazioneOrd));
					
					//E GLI SETTO COME IMPORTO LA SOMMA DI TUTTI I SUB ORDINATIVI (restara' solo lui):
					salvaImportoOrdinativoTs(subIt, datiOperazioneDto, sommaSub, Constanti.D_ORDINATIVO_TS_DET_TIPO_IMPORTO_ATTUALE);
					first = false;
					continue;
					
				}
				//SEGNO DA ELIMINARE TUTTI I SUB ORDINATIVI SUCCESSIVI AL PRIMO
//				subDaEliminare.add(subIt); SIAC-6801
				DatiOperazioneUtils.annullaRecord(subIt, siacTOrdinativoTRepository, datiOperazioneDto,siacTAccountRepository);
				//SIAC-6665
				aggiornaImportoAttualeOrdinativo(subIt,  BigDecimal.ZERO);
				//SIAC-6801
				scollegaOrdinativoDallaLiquidazione(datiOperazioneDto, subIt);
				
			}
		}
		
		//ELIMINO I SUB ORDINATIVI SUCCESSIVI AL PRIMO:
		if(!StringUtils.isEmpty(subDaEliminare)){
			//ELIMINA RIMOSSI
			for(SiacTOrdinativoTFin iterato : subDaEliminare){
				
				
			}
		}
		
		//2. cambio il capitolo
		CapitoloUscitaGestione nuovoCapitolo = nuovaLiquidazione.getCapitoloUscitaGestione();
		salvaOrdinativoBilElem(nuovoCapitolo, datiOperazioneDto, siacTOrd);
		
		//3. cambio i classificatori finanziari:
		siacTOrd = siacTOrdinativoRepository.findOne(idTOrdinativo);
		entityRefresh(siacTOrd);
		AttributoTClassInfoDto attributoInfo = new AttributoTClassInfoDto();
		attributoInfo.setTipoOggetto(OggettoDellAttributoTClass.T_ORDINATIVO);
		attributoInfo.setSiacTOrdinativo(siacTOrd);
		//copio i dati della te dalla liquidazione all'ordinativo:
		ordinativoPagamentoRicaricato = (OrdinativoPagamento) TransazioneElementareEntityToModelConverter.copiaDatiTransazioneElementare(nuovaLiquidazione, ordinativoPagamentoRicaricato);
		//e li salvo:
		salvaTransazioneElementare(attributoInfo, datiOperazioneDto, ordinativoPagamentoRicaricato);
		
		//4. Aggiorno la data spostamento:
		siacTOrd = siacTOrdinativoRepository.findOne(idTOrdinativo);
		entityRefresh(siacTOrd);
		siacTOrd.setOrdSpostamentoData(getNow());
		//
	}

	/**
	 * @param datiOperazioneDto
	 * @param subIt
	 */
	private void scollegaOrdinativoDallaLiquidazione(DatiOperazioneDto datiOperazioneDto, SiacTOrdinativoTFin subIt) {
		for (SiacRLiquidazioneOrdFin siacRLiquidazioneOrdFin : subIt.getSiacRLiquidazioneOrds()) {
			if(siacRLiquidazioneOrdFin.getDataCancellazione() != null || siacRLiquidazioneOrdFin.getDataFineValidita() != null) {
				continue;
			}
			DatiOperazioneUtils.cancellaRecord(siacRLiquidazioneOrdFin, siacRLiquidazioneOrdRepository, datiOperazioneDto, siacTAccountRepository);					
		}
	}

	/**
	 * @param iterato
	 * @param importoAttuale
	 */
	private void aggiornaImportoAttualeOrdinativo(SiacTOrdinativoTFin iterato, BigDecimal importoAttuale) {
		SiacTOrdinativoTsDetFin tsDet = siacTOrdinativoTsDetRepository.findOrdinativoTsDetByOrdTsIdAndOrdTsDetIdTipoCode(iterato.getUid(), "A");
		tsDet.setOrdTsDetImporto(importoAttuale);
		siacTOrdinativoTsDetRepository.saveAndFlush(tsDet);
	}
	
	/**
	 * 
	 * Rumuovi i legami con i Documenti 
	 * 
	 * @param ordinativoPagamentoRicaricato
	 * @param datiOperazioneDto
	 */
	public void rimuoviDocumentiSpesaPerReintroito(OrdinativoPagamento ordinativoPagamentoRicaricato,
			DatiOperazioneDto datiOperazioneDto){
		
		Integer idTOrdinativo = ordinativoPagamentoRicaricato.getUid();
		
		//1. cambio la liquidazione
		int idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		List<SiacTOrdinativoTFin> subOrdinativi = siacTOrdinativoTRepository.findSubOrdinativiByOrdinativo(idTOrdinativo, datiOperazioneDto.getTs(),idEnte);
		
		for(SiacTOrdinativoTFin subIt: subOrdinativi){
			if(subIt!=null){
				//RIMUOVO l'eventuale DOCUMENTO:
				rimuoviSubDocumento(subIt, datiOperazioneDto);	
			}
		}
	}
	
	private void rimuoviSubDocumento(SiacTOrdinativoTFin subOrdinativo, DatiOperazioneDto datiOperazioneDto){
		//RIMUOVO l'eventuale DOCUMENTO:
		List<SiacRSubdocOrdinativoTFin> rDocs = subOrdinativo.getSiacRSubdocOrdinativoTs();
		List<SiacRSubdocOrdinativoTFin> rDocsValidi = CommonUtils.soloValidiSiacTBase(rDocs, datiOperazioneDto.getTs());
		if(!StringUtils.isEmpty(rDocsValidi)){
			for(SiacRSubdocOrdinativoTFin it: rDocsValidi){
				DatiOperazioneUtils.cancellaRecord(it, siacRSubdocOrdinativoTFinRepository, datiOperazioneDto,siacTAccountRepository);
			}
		}
	}
	
	private BigDecimal sommaImportiAttuali(List<SiacTOrdinativoTFin> subOrdinativi, DatiOperazioneDto datiOperazione){
		BigDecimal somma = BigDecimal.ZERO;
		for(SiacTOrdinativoTFin it: subOrdinativi){
			if(it!=null){
				BigDecimal importoIt = estraiImportoAttuale(it, datiOperazione);
				somma = somma.add(importoIt);
			}
		}
		return somma;
	}
	
	private BigDecimal estraiImportoAttuale(SiacTOrdinativoTFin siacTOrdinativoTFin, DatiOperazioneDto datiOperazione){
		Integer idSiacTOrdinativoTs = siacTOrdinativoTFin.getOrdTsId();
		Integer idEnte = datiOperazione.getSiacTEnteProprietario().getEnteProprietarioId();
		Timestamp now = datiOperazione.getTs();
		List<SiacTOrdinativoTsDetFin> importoAttualeList = siacTOrdinativoTsDetRepository.findValidoByTipo(idEnte, now, Constanti.D_ORDINATIVO_TS_DET_TIPO_IMPORTO_ATTUALE, idSiacTOrdinativoTs );
		SiacTOrdinativoTsDetFin siacTOrdinativoTsDet = CommonUtils.getFirst(importoAttualeList);
		return siacTOrdinativoTsDet.getOrdTsDetImporto();
	}
	
	/**
	 * 
	 *  Non ricarica o controlla i dati, assume che il chiamante abbia gia' svolto
	 *  gli opportuni caricamenti e verifiche.
	 * 
	 *	Scollegare l'ordinativo dall'accertamento
	 *	Scollegare l'ordinativo dal capitolo
	 *	Scollegare l'ordinativo dalle classificazioni finanziarie
	 *	Collegare nuovamente l'ordinativo all'accertamento indicato e conseguentemente al suo capitolo e alle sue classificazioni finanziarie 
	 *	Aggiornare la DATA SPOSTAMENTO
	 * 
	 */
	public void spostaOrdinativoIncassoPerReintroito(OrdinativoIncasso ordinativoIncassoRicaricato,AccertamentoPerReintroitoInfoDto nuovoAcc,
			DatiOperazioneDto datiOperazioneDto){
		
		SiacTMovgestTsFin nuovoSiacTMovgestTsFin = nuovoAcc.getMovGestInfoAccertamento().getSiacTMovgestTs();
		SiacTMovgestTsFin nuovoSubSiacTMovgestTsFin = null;
		boolean isSub = false;
		if(nuovoAcc.getMovGestInfoSubAccertamento()!=null){
			nuovoSubSiacTMovgestTsFin = nuovoAcc.getMovGestInfoSubAccertamento().getSiacTMovgestTs();
			isSub = true;
		}
		
		Integer idTOrdinativo = ordinativoIncassoRicaricato.getUid();
		SiacTOrdinativoFin siacTOrd = siacTOrdinativoRepository.findOne(idTOrdinativo);
		
		//1. cambio l'accertamento
		Integer idNuovoAccertamento = nuovoSiacTMovgestTsFin.getMovgestTsId();
		if(nuovoSubSiacTMovgestTsFin!=null && nuovoSubSiacTMovgestTsFin.getMovgestTsId()!=null){
			//ho ricevuto un sub
			idNuovoAccertamento = nuovoSubSiacTMovgestTsFin.getMovgestTsId();
		}
		
		int idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		List<SiacTOrdinativoTFin> subOrdinativi = siacTOrdinativoTRepository.findSubOrdinativiByOrdinativo(idTOrdinativo, datiOperazioneDto.getTs(),idEnte);
		for(SiacTOrdinativoTFin subIt: subOrdinativi){
			SiacROrdinativoTsMovgestTFin sROrdTsMovTFin = salvaSiacROrdinativoTsMovgestTs(subIt, datiOperazioneDto, idNuovoAccertamento);
			subIt.setSiacROrdinativoTsMovgestTs(toList(sROrdTsMovTFin));
		}
		
		//2. cambio il capitolo
		//capitolo (e' sempre sull'accertamento anche per i sub):
		CapitoloEntrataGestione nuovoCapitolo = nuovoAcc.getAccertamento().getCapitoloEntrataGestione();
		//
		salvaOrdinativoBilElem(nuovoCapitolo, datiOperazioneDto, siacTOrd);
		
		//3. cambio i classificatori finanziari:
		siacTOrd = siacTOrdinativoRepository.findOne(idTOrdinativo);
		entityRefresh(siacTOrd);
		AttributoTClassInfoDto attributoInfo = new AttributoTClassInfoDto();
		attributoInfo.setTipoOggetto(OggettoDellAttributoTClass.T_ORDINATIVO);
		attributoInfo.setSiacTOrdinativo(siacTOrd);
		//copio i dati della te dalla liquidazione all'ordinativo:
		if(!isSub){
			ordinativoIncassoRicaricato = (OrdinativoIncasso) TransazioneElementareEntityToModelConverter.copiaDatiTransazioneElementare(nuovoAcc.getAccertamento(), ordinativoIncassoRicaricato);
		} else {
			ordinativoIncassoRicaricato = (OrdinativoIncasso) TransazioneElementareEntityToModelConverter.copiaDatiTransazioneElementare(nuovoAcc.getSubAccertamento(), ordinativoIncassoRicaricato);	
		}
		//e li salvo:
		salvaTransazioneElementare(attributoInfo, datiOperazioneDto, ordinativoIncassoRicaricato);
		
		
		// SIAC-6252, punto 2
		saveOrdinativoClass(null, siacTClassRepository.findByDescAndEnte(idEnte, "COMPENSAZIONE").get(0), siacTOrd, datiOperazioneDto);
		
		//siacROrdinativoClassRepository.insertRelClassifCompensazione(idEnte, idTOrdinativo);
		
		//27 FEBBRAIO 2018 - SIAC-5959 - oltre alle azioni gia' previste, scollegare
		//l'ordinativo di incasso dall'onere del documento (tabella siac_r_doc_onere_ordinativo_ts)
		scollegaRDocOneri(siacTOrd.getSiacTOrdinativoTs(), datiOperazioneDto);
		//
		
		//4. Aggiorno la dat spostamento:
		siacTOrd = siacTOrdinativoRepository.findOne(idTOrdinativo);
		entityRefresh(siacTOrd);
		siacTOrd.setOrdSpostamentoData(getNow());
		//
	}
	
	private void scollegaRDocOneri(List<SiacTOrdinativoTFin> listaSiacTOrdinativoT, DatiOperazioneDto datiOperazione){
		if(!isEmpty(listaSiacTOrdinativoT)) {
			for(SiacTOrdinativoTFin it : listaSiacTOrdinativoT){
				if(it!=null){
					scollegaRDocOneri(it, datiOperazione);
				}
			}
		}
	}
	
	private void scollegaRDocOneri(SiacTOrdinativoTFin siacTOrdinativo, DatiOperazioneDto datiOperazione){
		List<SiacRDocOnereOrdinativoTsFin> listaRel = siacTOrdinativo.getSiacRDocOnereOrdinativoTsFin();
		if(!isEmpty(listaRel)) {
			Timestamp ts = datiOperazione.getTs();
			DatiOperazioneDto datiOperazioneCancella = clone(datiOperazione);
			datiOperazioneCancella.setOperazione(Operazione.CANCELLAZIONE_LOGICA_RECORD);
			for(SiacRDocOnereOrdinativoTsFin it : listaRel){
				if(DatiOperazioneUtils.isValido(it, ts)){
					//CANCELLO LOGICAMENTE IL LEGAME CON IL VECCHIO STATO:
					DatiOperazioneUtils.cancellaRecord(it, siacRDocOnereOrdinativoTsFinRepository, datiOperazioneCancella, siacTAccountRepository);
					//
				}
			}
		}
	}
	
	/**
	 * Metodo di comodo per mettere a fattor comune il setting degli importi
	 * per i due servizi di ricerca dei sub ordinativi (di incasso e pagamento)
	 * @param datiOttimizzazione
	 * @param subOrdinativoIt
	 * @param siacTOrdinativoT
	 */
	protected void settaImportiSubOrdinativo(OttimizzazioneOrdinativoPagamentoDto datiOttimizzazione,
			SubOrdinativo subOrdinativoIt,SiacTOrdinativoTFin siacTOrdinativoT){
		//importi sub:
		BigDecimal iniziale = datiOttimizzazione.estraiImportoSubOrdinativo(siacTOrdinativoT, Constanti.D_ORDINATIVO_TS_DET_TIPO_IMPORTO_INIZIALE);
		BigDecimal attuale = datiOttimizzazione.estraiImportoSubOrdinativo(siacTOrdinativoT, Constanti.D_ORDINATIVO_TS_DET_TIPO_IMPORTO_ATTUALE);
		subOrdinativoIt.setImportoAttuale(attuale);
		subOrdinativoIt.setImportoIniziale(iniziale);
		//
	}

	
	protected void setDaTrasmettere(Integer idOrdinativo, Boolean daTrasmettere) {
		siacTOrdinativoRepository.setDaTrasmettere(idOrdinativo, daTrasmettere);
	}
}