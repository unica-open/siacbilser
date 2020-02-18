/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.CommonUtils;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.StringUtils;
import it.csi.siac.siacfinser.TimingUtils;
import it.csi.siac.siacfinser.integration.dad.datacontainer.DisponibilitaMovimentoGestioneContainer;
import it.csi.siac.siacfinser.integration.dao.common.SiacDAmbitoRepository;
import it.csi.siac.siacfinser.integration.dao.common.SiacDViaTipoRepository;
import it.csi.siac.siacfinser.integration.dao.common.SiacRSoggettoEnteProprietarioFinRepository;
import it.csi.siac.siacfinser.integration.dao.common.SiacTEnteProprietarioFinRepository;
import it.csi.siac.siacfinser.integration.dao.common.dto.AttributoTClassInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ClassificazioniInModificaInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ClassificazioniModInModificaInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ContattiInModificaInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ContattiModInModificaInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ContattoModModificatoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ContattoModificatoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoControlliAggiornamentoMDPDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoControlliDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoRicercaSoggettiDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.IndirizziInModificaInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.IndirizziModInModificaInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.InserisciSoggettoModDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ModalitaPagamentoInModificaInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OggettoDellAttributoTClass;
import it.csi.siac.siacfinser.integration.dao.common.dto.OneriInModificaInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OneriModInModificaInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneModalitaPagamentoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneSoggettoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaSoggettoParamDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.SediSecondarieInModificaInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.SoggettoTipoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.SoggettoTipoModDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ValidaSoggettoInfoDto;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacRModpagOrdineRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacRMovgestTsSogClasseRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacRMovgestTsStatoRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacROrdinativoModpagRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.PersonaFisicaDao;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacDAccreditoGruppoRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacDIndirizzoTipoRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacDModpagStatoRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacDOnereFinRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacDRecapitoModoRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacDRelazStatoRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacDRelazTipoRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacDSoggettoClasseRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacDSoggettoClasseTipoFinRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacDSoggettoStatoRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacDSoggettoTipoRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacDTipoAccreditoRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacRFormaGiuridicaModRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacRFormaGiuridicaRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacRIndirizzoSoggettoTipoModRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacRIndirizzoSoggettoTipoRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacRModpagStatoRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacRSoggettoClasseModRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacRSoggettoClasseRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacRSoggettoOnereModRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacRSoggettoOnereRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacRSoggettoRelazFinRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacRSoggettoRelazModRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacRSoggettoRelazStatoRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacRSoggettoStatoRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacRSoggettoTipoModRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacRSoggettoTipoRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacRSoggrelModpagModRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacRSoggrelModpagRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacRSubdocModpagRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacTCabRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacTComuneRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacTFormaGiuridicaRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacTIndirizzoSoggettoModRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacTIndirizzoSoggettoRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacTModpagFinRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacTModpagModRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacTNazioneRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacTPersonaFisicaModRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacTPersonaFisicaRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacTPersonaGiuridicaModRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacTPersonaGiuridicaRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacTRecapitoSoggettoModRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacTRecapitoSoggettoRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacTSepaRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacTSoggettoFinRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacTSoggettoModRepository;
import it.csi.siac.siacfinser.integration.entity.SiacDAccreditoGruppoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDAccreditoTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDAmbitoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDIndirizzoTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDModpagStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDOnereFin;
import it.csi.siac.siacfinser.integration.entity.SiacDRecapitoModoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDRelazStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDRelazTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDSoggettoClasseFin;
import it.csi.siac.siacfinser.integration.entity.SiacDSoggettoClasseTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDSoggettoStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDSoggettoTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDViaTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRDocSogFin;
import it.csi.siac.siacfinser.integration.entity.SiacRFormaGiuridicaFin;
import it.csi.siac.siacfinser.integration.entity.SiacRFormaGiuridicaModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRIndirizzoSoggettoTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRIndirizzoSoggettoTipoModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneOrdFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRModpagOrdineFin;
import it.csi.siac.siacfinser.integration.entity.SiacRModpagStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoModpagFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoAttrFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoAttrModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoClasseFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoClasseModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoEnteProprietarioFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoOnereFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoOnereModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoRelazFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoRelazModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoRelazStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoTipoModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggrelModpagFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggrelModpagModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSubdocModpagFin;
import it.csi.siac.siacfinser.integration.entity.SiacTCabFin;
import it.csi.siac.siacfinser.integration.entity.SiacTClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacTComuneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTEnteProprietarioFin;
import it.csi.siac.siacfinser.integration.entity.SiacTFormaGiuridicaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTIndirizzoSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTIndirizzoSoggettoModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTLiquidazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModpagFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModpagModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.SiacTNazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoTsDetFin;
import it.csi.siac.siacfinser.integration.entity.SiacTPersonaFisicaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTPersonaFisicaModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTPersonaGiuridicaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTPersonaGiuridicaModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTRecapitoSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTRecapitoSoggettoModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSepaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSoggettoModFin;
import it.csi.siac.siacfinser.integration.entity.mapping.FinMapId;
import it.csi.siac.siacfinser.integration.util.DatiOperazioneUtils;
import it.csi.siac.siacfinser.integration.util.EntityToEntityConverter;
import it.csi.siac.siacfinser.integration.util.EntityToModelConverter;
import it.csi.siac.siacfinser.integration.util.ObjectStreamerHandler;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.integration.util.OperazioneValidaSoggetto;
import it.csi.siac.siacfinser.model.TipoFonteDurc;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.ModalitaAccreditoSoggetto;
import it.csi.siac.siacfinser.model.codifiche.NaturaGiuridicaSoggetto;
import it.csi.siac.siacfinser.model.codifiche.TipoRelazioneSoggetto;
import it.csi.siac.siacfinser.model.codifiche.TipoSoggetto;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggetto;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.soggetto.ClassificazioneSoggetto;
import it.csi.siac.siacfinser.model.soggetto.ComuneNascita;
import it.csi.siac.siacfinser.model.soggetto.Contatto;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;
import it.csi.siac.siacfinser.model.soggetto.modpag.Banca;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto.TipoAccredito;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto.StatoOperativoSedeSecondaria;

/**
 * 
 * @author paolos
 * 
 */

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class SoggettoFinDad extends AbstractFinDad
{

	@Autowired
	PersonaFisicaDao personaFisicaDao;

	@Autowired
	ObjectStreamerHandler objectStreamerHandler;

	@Autowired
	SiacTSoggettoFinRepository siacTSoggettoRepository;
	
	@Autowired
	SiacTModpagFinRepository siacTModpagFinRepository;

	@Autowired
	SiacTSoggettoModRepository siacTSoggettoModRepository;

	@Autowired
	private SiacTCabRepository siacTCabRepository;

	@Autowired
	private SiacTSepaRepository siacTSepaRepository;

	@Autowired
	SiacTComuneRepository siacTComuneRepository;

	@Autowired
	SiacTNazioneRepository siacTNazioneRepository;

	@Autowired
	SiacDAmbitoRepository siacDAmbitoRepository;

	@Autowired
	SiacTPersonaFisicaRepository siacTPersonaFisicaRepository;

	@Autowired
	SiacTPersonaFisicaModRepository siacTPersonaFisicaModRepository;

	@Autowired
	SiacTPersonaGiuridicaRepository siacTPersonaGiuridicaRepository;

	@Autowired
	SiacTPersonaGiuridicaModRepository siacTPersonaGiuridicaModRepository;

	@Autowired
	SiacRFormaGiuridicaRepository siacRFormaGiuridicaRepository;

	@Autowired
	SiacTFormaGiuridicaRepository siacTFormaGiuridicaRepository;

	@Autowired
	SiacTEnteProprietarioFinRepository siacTEnteProprietarioRepository;

	@Autowired
	SiacDSoggettoStatoRepository siacDSoggettoStatoRepository;

	@Autowired
	SiacRSoggettoStatoRepository siacRSoggettoStatoRepository;

	@Autowired
	SiacRSoggettoClasseRepository siacRSoggettoClasseRepository;

	@Autowired
	SiacRSoggettoClasseModRepository siacRSoggettoClasseModRepository;

	@Autowired
	SiacDSoggettoClasseRepository siacDSoggettoClasseRepository;

	@Autowired
	SiacTIndirizzoSoggettoRepository siacTIndirizzoSoggettoRepository;

	@Autowired
	SiacTIndirizzoSoggettoModRepository siacTIndirizzoSoggettoModRepository;

	@Autowired
	SiacRIndirizzoSoggettoTipoRepository siacRIndirizzoSoggettoTipoRepository;

	@Autowired
	SiacRIndirizzoSoggettoTipoModRepository siacRIndirizzoSoggettoTipoModRepository;

	@Autowired
	SiacDViaTipoRepository siacDViaTipoRepository;

	@Autowired
	SiacDIndirizzoTipoRepository siacDIndirizzoTipoRepository;

	@Autowired
	SiacTRecapitoSoggettoRepository siacTRecapitoSoggettoRepository;

	@Autowired
	SiacTRecapitoSoggettoModRepository siacTRecapitoSoggettoModRepository;

	@Autowired
	SiacDRecapitoModoRepository siacDRecapitoModoRepository;

	@Autowired
	SiacDOnereFinRepository siacDOnereRepository;

	@Autowired
	SiacRSoggettoOnereRepository siacRSoggettoOnereRepository;

	@Autowired
	SiacRSoggettoOnereModRepository siacRSoggettoOnereModRepository;

	@Autowired
	SiacRSoggettoTipoRepository siacRSoggettoTipoRepository;

	@Autowired
	SiacDSoggettoTipoRepository siacDSoggettoTipoRepository;

	@Autowired
	SiacRSoggrelModpagModRepository siacRSoggrelModpagModRepository;

	@Autowired
	SiacRSoggettoRelazModRepository siacRSoggettoRelazModRepository;

	@Autowired
	SiacRSoggettoRelazFinRepository siacRSoggettoRelazRepository;

	@Autowired
	SiacDRelazTipoRepository siacDRelazTipoRepository;

	@Autowired
	SiacTModpagModRepository siacTModpagModRepository;

	@Autowired
	SiacTModpagFinRepository siacTModpagRepository;

	@Autowired
	SiacDTipoAccreditoRepository siacDTipoAccreditoRepository;

	@Autowired
	SiacRModpagStatoRepository siacRModpagStatoRepository;

	@Autowired
	SiacDModpagStatoRepository siacDModpagStatoRepository;

	@Autowired
	SiacRSoggrelModpagRepository siacRSoggrelModpagRepository;

	@Autowired
	SiacDRelazStatoRepository siacDRelazStatoRepository;

	@Autowired
	SiacRSoggettoRelazStatoRepository siacRSoggettoRelazStatoRepository;

	@Autowired
	SiacRSoggettoTipoModRepository siacRSoggettoTipoModRepository;

	@Autowired
	SiacRFormaGiuridicaModRepository siacRFormaGiuridicaModRepository;

	@Autowired
	SiacDAccreditoGruppoRepository siacDAccreditoGruppoRepository;

	@Autowired
	SiacROrdinativoModpagRepository siacROrdinativoModpagRepository;

	@Autowired
	SiacRMovgestTsStatoRepository siacRMovgestTsStatoRepository;

	@Autowired
	SiacRSubdocModpagRepository siacRSubdocModpagRepository;

	@Autowired
	SiacRModpagOrdineRepository siacRModpagOrdineRepository;
	
	@Autowired
	SiacDSoggettoClasseTipoFinRepository siacDSoggettoClasseTipoFinRepository;
	
	@Autowired
	SiacRMovgestTsSogClasseRepository	siacRMovgestTsSogClasseRepository;
	
	@Autowired
	SiacRSoggettoEnteProprietarioFinRepository siacRSoggettoEnteProprietarioFinRepository;

	//
	/**
	 * Esegue la cancellazione logica del record passato in input
	 * 
	 * @param idEnte
	 * @param soggetto
	 * @param loginOperazione
	 * @param richiedente
	 * @return
	 */
	public Soggetto cancellaSoggetto(Integer idEnte, Soggetto soggetto, String loginOperazione,
			Richiedente richiedente)
	{
		long currMillisec = System.currentTimeMillis();
		// ricarico l'ente:
		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository
				.findOne(idEnte);
		Operazione operazione = Operazione.CANCELLAZIONE_LOGICA_RECORD;
		StatoOperativoAnagrafica statoOperativoAnagrafica = StatoOperativoAnagrafica.ANNULLATO;
		Integer idSiacTSoggettoDaCancellare = soggetto.getUid();
		// imposto i dati operazione:
		DatiOperazioneDto datiOperazioneDto = new DatiOperazioneDto(currMillisec, operazione,
				siacTEnteProprietario, richiedente.getAccount().getId());
		// ricarico il soggetto:
		SiacTSoggettoFin siacTSoggetto = siacTSoggettoRepository.findOne(idSiacTSoggettoDaCancellare);
		Soggetto soggettoCancellato = new Soggetto();
		// cancellazione logica:
		if (siacTSoggetto != null)
		{
			salvaStatoOperativoAnagrafica(statoOperativoAnagrafica, siacTSoggetto, idEnte,
					datiOperazioneDto);
			siacTSoggetto = DatiOperazioneUtils.cancellaRecord(siacTSoggetto,
					siacTSoggettoRepository, datiOperazioneDto, siacTAccountRepository);
			soggettoCancellato = map(siacTSoggetto, Soggetto.class, FinMapId.SiacTSoggetto_Soggetto);
			soggettoCancellato = EntityToModelConverter.soggettoEntityToSoggettoModel(
					siacTSoggetto, soggettoCancellato);
		}

		// Termino restituendo l'oggetto di ritorno:
		return soggettoCancellato;
	}

	/**
	 * esegure la rimozione della sede dal soggetto
	 * 
	 * @param idEnte
	 * @param siacTSoggetto
	 * @param loginOperazione
	 * @param richiedente
	 * @return
	 */
	public Soggetto cancellaSedeDaTSoggetto(Integer idEnte, SiacTSoggettoFin siacTSoggetto,
			String loginOperazione, Richiedente richiedente)
	{
		long currMillisec = System.currentTimeMillis();
		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository
				.findOne(idEnte);

		Operazione operazione = Operazione.CANCELLAZIONE_LOGICA_RECORD;
		StatoOperativoAnagrafica statoOperativoAnagrafica = StatoOperativoAnagrafica.ANNULLATO;

		DatiOperazioneDto datiOperazioneDto = new DatiOperazioneDto(currMillisec, operazione,
				siacTEnteProprietario, richiedente.getAccount().getId());

		Soggetto soggettoCancellato = new Soggetto();

		if (siacTSoggetto != null)
		{
			salvaStatoOperativoAnagrafica(statoOperativoAnagrafica, siacTSoggetto, idEnte,
					datiOperazioneDto);
			siacTSoggetto = DatiOperazioneUtils.cancellaRecord(siacTSoggetto,
					siacTSoggettoRepository, datiOperazioneDto, siacTAccountRepository);
			soggettoCancellato = map(siacTSoggetto, Soggetto.class, FinMapId.SiacTSoggetto_Soggetto);
			soggettoCancellato = EntityToModelConverter.soggettoEntityToSoggettoModel(
					siacTSoggetto, soggettoCancellato);
		}

		// Termino restituendo l'oggetto di ritorno:
		return soggettoCancellato;
	}

	public Banca ricercaBanca(String abi, String cab, Integer idEnte)
	{
		SiacTCabFin siacTCab = siacTCabRepository.findAbiCab(abi, cab, idEnte);

		return mapNotNull(siacTCab, Banca.class, FinMapId.SiacTCab_Banca);
	}

	public SiacTSepaFin getInfoSepa(String codiceIsoNazione, int idEnte)
	{
		SiacTSepaFin siacTSepa = siacTSepaRepository.find(codiceIsoNazione, idEnte);

		return siacTSepa;
	}

	/**
	 * cancella la sede dal soggetto in modifica
	 * 
	 * @param idEnte
	 * @param siacTSoggetto
	 * @param loginOperazione
	 * @param richiedente
	 */
	public void cancellaSedeDaTSoggettoMod(Integer idEnte, SiacTSoggettoFin siacTSoggetto,
			String loginOperazione, Richiedente richiedente)
	{
		long currMillisec = System.currentTimeMillis();
		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository
				.findOne(idEnte);

		Operazione operazione = Operazione.CANCELLAZIONE_LOGICA_RECORD;
		DatiOperazioneDto datiOperazioneDto = new DatiOperazioneDto(currMillisec, operazione,
				siacTEnteProprietario, richiedente.getAccount().getId());

		List<SiacTSoggettoModFin> listaSoggMod = siacTSoggettoModRepository
				.findAllBySoggettoId(siacTSoggetto.getSoggettoId());

		if (null != listaSoggMod && listaSoggMod.size() > 0)
		{
			for (SiacTSoggettoModFin iterator : listaSoggMod)
			{
				// quando trovo quello valido
				if (iterator.getDataFineValidita() == null)
				{
					iterator = DatiOperazioneUtils.cancellaRecord(iterator,
							siacTSoggettoModRepository, datiOperazioneDto, siacTAccountRepository);
					break;
				}
			}
		}
	}

	/**
	 * Ricerca la sede secondaria per chiave
	 * 
	 * @param codiceEnte
	 * @param codSoggetto
	 * @param idSedeSecondaria
	 * @return
	 */
	public SedeSecondariaSoggetto ricercaSedeSecondariaPerChiave(Integer codiceEnte,
			String codSoggetto, Integer idSedeSecondaria)
	{

		SedeSecondariaSoggetto sedeSecondariaSoggettoTrovata = null;
		SiacTSoggettoFin sedeSecondariaEntity = siacTSoggettoRepository
				.ricercaSedeSecondariaPerChiave(codiceEnte, codSoggetto, idSedeSecondaria,
						Constanti.SEDE_SECONDARIA);

		if (sedeSecondariaEntity != null)
		{
			sedeSecondariaSoggettoTrovata = map(sedeSecondariaEntity, SedeSecondariaSoggetto.class,
					FinMapId.SiacTSoggetto_SedeSecondariaSoggetto);
			sedeSecondariaSoggettoTrovata = EntityToModelConverter
					.soggettoEntityToSedeSecondariaSoggettoModel(sedeSecondariaEntity,
							sedeSecondariaSoggettoTrovata,null);
		}
		// Termino restituendo l'oggetto di ritorno:
		return sedeSecondariaSoggettoTrovata;
	}

	/**
	 * Ricerca la sede secondaria in modifica per chiave
	 * 
	 * @param codiceEnte
	 * @param codSoggetto
	 * @param idSedeSecondaria
	 * @return
	 */
	public SedeSecondariaSoggetto ricercaSedeSecondariaModPerChiave(Integer codiceEnte,
			String codSoggetto, Integer idSedeSecondaria)
	{
		Timestamp now = new Timestamp(System.currentTimeMillis());
		SedeSecondariaSoggetto sedeSecondariaSoggettoTrovata = null;
		List<SiacTSoggettoModFin> soggmods = siacTSoggettoModRepository.findValidoBySoggettoId(
				idSedeSecondaria, now);
		SiacTSoggettoModFin sedeSecondariaEntity = null;
		if (soggmods != null && soggmods.size() > 0)
		{
			sedeSecondariaEntity = soggmods.get(0);
		}

		if (sedeSecondariaEntity != null)
		{
			sedeSecondariaSoggettoTrovata = map(sedeSecondariaEntity, SedeSecondariaSoggetto.class,
					FinMapId.SiacTSoggettoMod_SedeSecondariaSoggetto);
			sedeSecondariaSoggettoTrovata = EntityToModelConverter
					.soggettoEntityModToSedeSecondariaSoggettoModel(sedeSecondariaEntity,
							sedeSecondariaSoggettoTrovata);
		}
		// Termino restituendo l'oggetto di ritorno:
		return sedeSecondariaSoggettoTrovata;
	}

	/**
	 * Effettua la validazione della sede secondaria
	 * 
	 * @param loginOperazione
	 * @param idEnte
	 * @param idSiacTSoggettoInModifica
	 * @param richiedente
	 * @return
	 */
	public Soggetto validaSedeSecondaria(String loginOperazione, int idEnte,
			Integer idSiacTSoggettoInModifica, Richiedente richiedente)
	{

		long currMillisec = System.currentTimeMillis();
		Operazione operazione = Operazione.MODIFICA;

		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository
				.findOne(idEnte);

		// quando aggiorno un record:
		DatiOperazioneDto datiOperazioneModifica = new DatiOperazioneDto(currMillisec, operazione,
				siacTEnteProprietario, richiedente.getAccount().getId());

		// quando inserisco un nuovo record:
		Operazione operazioneInsert = Operazione.INSERIMENTO;
		DatiOperazioneDto datiOperazioneInserisci = new DatiOperazioneDto(currMillisec,
				operazioneInsert, siacTEnteProprietario, richiedente.getAccount().getId());
		// /////////////

		SiacTSoggettoFin siacTSoggettoOriginale = null;
		Integer idSoggMod = null;
		List<SiacTSoggettoModFin> listMod = siacTSoggettoModRepository.findValidoBySoggettoId(
				idSiacTSoggettoInModifica, datiOperazioneModifica.getTs());
		if ((listMod == null) || listMod.isEmpty())
		{
			// Da stato PROVVISORIO non si copia nulla
			siacTSoggettoOriginale = siacTSoggettoRepository.findOne(idSiacTSoggettoInModifica);

			// JIRA 1188-aggiornamento della data ultimo aggiornamento
			siacTSoggettoOriginale = DatiOperazioneUtils.impostaDatiOperazioneLogin(
					siacTSoggettoOriginale, datiOperazioneModifica, siacTAccountRepository);
			siacTSoggettoOriginale.setLoginCreazione(loginOperazione);
			// salvo sul db:
			siacTSoggettoOriginale = siacTSoggettoRepository.saveAndFlush(siacTSoggettoOriginale);
			entityRefresh(siacTSoggettoOriginale);
		}
		else
		{
			// Da stato IN_MODIFICA si copia SiacTSoggettoFin da mod
			SiacTSoggettoModFin siacTSoggettoMod = listMod.get(0);
			idSoggMod = siacTSoggettoMod.getSogModId();
			siacTSoggettoOriginale = EntityToEntityConverter.siacTSoggettoModToSiacTSoggetto(
					siacTSoggettoMod, loginOperazione);
			siacTSoggettoOriginale = DatiOperazioneUtils.impostaDatiOperazioneLogin(
					siacTSoggettoOriginale, datiOperazioneModifica, siacTAccountRepository);
			// salvo sul db:
			siacTSoggettoOriginale = siacTSoggettoRepository.saveAndFlush(siacTSoggettoOriginale);
			entityRefresh(siacTSoggettoOriginale);
		}
		// //

		// setto il nuovo stato:
		SiacRSoggettoStatoFin siacRSoggettoStato = salvaStatoOperativoAnagrafica(
				StatoOperativoAnagrafica.VALIDO, siacTSoggettoOriginale, idEnte,
				datiOperazioneInserisci);
		siacTSoggettoOriginale.setSiacRSoggettoStatos(toList(siacRSoggettoStato));
		// /////////

		// indirizzi PER ORA MANCA LA TABELLA siac_r_indirizzo_soggetto_tipo_mod
		if (!listMod.isEmpty())
		{
			// Da stato IN_MODIFICA si copia SiacTSoggettoFin da mod

			// invalido i vecchi:
			List<SiacTIndirizzoSoggettoFin> indirizziOld = siacTIndirizzoSoggettoRepository
					.findValidiByIdSoggetto(idSiacTSoggettoInModifica,
							datiOperazioneModifica.getTs());
			for (SiacTIndirizzoSoggettoFin siacTInd : indirizziOld)
			{
				eliminaIndirizzo(siacTInd, datiOperazioneModifica);
			}
			// scrivo i nuovi da mod:
			List<SiacTIndirizzoSoggettoModFin> indirizziSoggMod = siacTIndirizzoSoggettoModRepository
					.findValidoBySoggModId(idSoggMod, datiOperazioneModifica.getTs());
			for (SiacTIndirizzoSoggettoModFin siacTIndMod : indirizziSoggMod)
			{
				// manca la tabella siac_r_indirizzo_soggetto_tipo_mod
				saveIndirizzoFromIndirizzoMod(siacTIndMod, siacTSoggettoOriginale, idEnte,
						datiOperazioneModifica);
			}

			// recapiti (contatti):
			List<SiacTRecapitoSoggettoModFin> recapitiMod = siacTRecapitoSoggettoModRepository
					.findValidoBySoggModId(idSoggMod, datiOperazioneModifica.getTs());
			List<Contatto> listaContatti = contattiModToContattModel(recapitiMod);
			aggiornaContatti(listaContatti, datiOperazioneModifica, siacTSoggettoOriginale, idEnte);

			// bisogna cancellare i _mod:
			deleteFisicaTabelleSoggettoMod(siacTSoggettoOriginale);
		}

		entityRefresh(siacTSoggettoOriginale);
		// RITORNO RISULTATI:
		Soggetto soggetto = new Soggetto();
		soggetto.setCodiceSoggetto(siacTSoggettoOriginale.getSoggettoCode());
		soggetto = EntityToModelConverter.soggettoEntityToSoggettoModel(siacTSoggettoOriginale,
				soggetto);
		// Termino restituendo l'oggetto di ritorno:
		return soggetto;
	}

	/**
	 * si occupa di annullare la se
	 * 
	 * @param loginOperazione
	 * @param idEnte
	 * @param idSiacTSoggettoInModifica
	 * @param richiedente
	 * @return
	 */
	public SedeSecondariaSoggetto annullaSedeSecondaria(String loginOperazione, int idEnte,
			Integer idSiacTSoggettoInModifica, Richiedente richiedente)
	{

		SedeSecondariaSoggetto sedeSecondariaDaModificare = new SedeSecondariaSoggetto();

		long currMillisec = System.currentTimeMillis();

		Operazione operazioneModifica = Operazione.MODIFICA;
		Operazione operazioneCancellazioneLogica = Operazione.CANCELLAZIONE_LOGICA_RECORD;

		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository
				.findOne(idEnte);

		DatiOperazioneDto datiOperazioneModifica = new DatiOperazioneDto(currMillisec,
				operazioneModifica, siacTEnteProprietario, richiedente.getAccount().getId());
		DatiOperazioneDto datiOperazioneCancellazioneLogica = new DatiOperazioneDto(currMillisec,
				operazioneCancellazioneLogica, siacTEnteProprietario, richiedente.getAccount()
						.getId());

		SiacTSoggettoFin siacTSoggetto = siacTSoggettoRepository.findOne(idSiacTSoggettoInModifica);
		List<SiacTSoggettoModFin> siacTSoggettoMods = siacTSoggettoModRepository
				.findValidoBySoggettoId(idSiacTSoggettoInModifica, datiOperazioneModifica.getTs());
		SiacTSoggettoModFin siacTSoggettoMod = ((siacTSoggettoMods != null) && !siacTSoggettoMods
				.isEmpty()) ? siacTSoggettoMods.get(0) : null;

		// Cancello logicamente su siac_t_soggetto_mod e le sue eventuali foglie
		// *_mod
		if (siacTSoggettoMod != null)
		{
			// siac_t_indirizzo_soggetto_mod
			List<SiacTIndirizzoSoggettoModFin> siacTIndirizzosSoggettoMod = siacTSoggettoMod
					.getSiacTIndirizzoSoggettoMods();
			if (siacTIndirizzosSoggettoMod != null && siacTIndirizzosSoggettoMod.size() > 0)
			{
				for (SiacTIndirizzoSoggettoModFin siacTIndirizzoSoggettoMod : siacTIndirizzosSoggettoMod)
				{
					DatiOperazioneUtils.cancellaRecord(siacTIndirizzoSoggettoMod,
							siacTIndirizzoSoggettoModRepository, datiOperazioneCancellazioneLogica,
							siacTAccountRepository);
				}
			}
			// siac_t_recapito_soggetto_mod (contatti)
			List<SiacTRecapitoSoggettoModFin> siacTRecapitosSoggettoMod = siacTSoggettoMod
					.getSiacTRecapitoSoggettoMods();
			if (siacTRecapitosSoggettoMod != null && siacTRecapitosSoggettoMod.size() > 0)
			{
				for (SiacTRecapitoSoggettoModFin siacTRecapitoSoggettoMod : siacTRecapitosSoggettoMod)
				{
					DatiOperazioneUtils.cancellaRecord(siacTRecapitoSoggettoMod,
							siacTRecapitoSoggettoModRepository, datiOperazioneCancellazioneLogica,
							siacTAccountRepository);
				}
			}
			// siac_r_soggetto_relaz_mod
			List<SiacRSoggettoRelazModFin> siacRSoggettoRelazMods = siacTSoggettoMod
					.getSiacRSoggettoRelazMods();
			if (siacRSoggettoRelazMods != null && siacRSoggettoRelazMods.size() > 0)
			{
				for (SiacRSoggettoRelazModFin siacRSoggettoRelazMod : siacRSoggettoRelazMods)
				{
					DatiOperazioneUtils.cancellaRecord(siacRSoggettoRelazMod,
							siacRSoggettoRelazModRepository, datiOperazioneCancellazioneLogica,
							siacTAccountRepository);
				}
			}
			DatiOperazioneUtils.cancellaRecord(siacTSoggettoMod, siacTSoggettoModRepository,
					datiOperazioneCancellazioneLogica, siacTAccountRepository);

			// Riporto a VALIDO lo stato della sede sulla tabella
			// siac_t_soggetto
			StatoOperativoAnagrafica statoOperativoAnagrafica = StatoOperativoAnagrafica.VALIDO;
			salvaStatoOperativoAnagrafica(statoOperativoAnagrafica, siacTSoggetto, idEnte,
					datiOperazioneModifica);

			// RITORNO RISULTATI:
			sedeSecondariaDaModificare = map(siacTSoggetto, SedeSecondariaSoggetto.class,
					FinMapId.SiacTSoggetto_SedeSecondariaSoggetto);
			sedeSecondariaDaModificare = EntityToModelConverter
					.soggettoEntityToSedeSecondariaSoggettoModel(siacTSoggetto,
							sedeSecondariaDaModificare,null);
			// Termino restituendo l'oggetto di ritorno:
			return sedeSecondariaDaModificare;
		}
		else
		{
			DatiOperazioneUtils.cancellaRecord(siacTSoggetto, siacTSoggettoRepository,
					datiOperazioneCancellazioneLogica, siacTAccountRepository);
			sedeSecondariaDaModificare = null;
		}
		// Termino restituendo l'oggetto di ritorno:
		return sedeSecondariaDaModificare;
	}
	
	/**
	 * Wrapper di retrocompatibilita'
	 * @param codiceAmbito
	 * @param codiceEnte
	 * @param codSoggetto
	 * @param idModalitaPagamento
	 * @param codiceModalitaPagamento
	 * @return
	 */
	public List<ModalitaPagamentoSoggetto> ricercaModalitaPagamentoPerChiave(String codiceAmbito,
			Integer codiceEnte, String codSoggetto, Integer idModalitaPagamento,
			String codiceModalitaPagamento)
	{
		return ricercaModalitaPagamentoPerChiave(codiceAmbito, codiceEnte, codSoggetto, idModalitaPagamento,
				codiceModalitaPagamento, false);
	}

	public List<ModalitaPagamentoSoggetto> ricercaModalitaPagamentoPerChiave(String codiceAmbito,
			Integer codiceEnte, String codSoggetto, Integer idModalitaPagamento,
			String codiceModalitaPagamento, boolean cercaMdpCessionePerChiaveModPag)
	{
		return ricercaModalitaPagamentoPerChiave(codiceAmbito, codiceEnte, codSoggetto, idModalitaPagamento, codiceModalitaPagamento, null, cercaMdpCessionePerChiaveModPag);
	}

	/**
	 * ricercaModalitaPagamentoPerChiave
	 * 
	 * @param codiceEnte
	 * @param codSoggetto
	 * @param idModalitaPagamento
	 * @param cercaMdpCessionePerChiaveModPag 
	 * @return
	 */
	public List<ModalitaPagamentoSoggetto> ricercaModalitaPagamentoPerChiave(String codiceAmbito,
			Integer codiceEnte, String codSoggetto, Integer idModalitaPagamento,
			String codiceModalitaPagamento, OttimizzazioneModalitaPagamentoDto datiOttimizzazione)
	{
		return ricercaModalitaPagamentoPerChiave(codiceAmbito, codiceEnte, codSoggetto, idModalitaPagamento,
				codiceModalitaPagamento, datiOttimizzazione, false);
	}

	public List<ModalitaPagamentoSoggetto> ricercaModalitaPagamentoPerChiave(String codiceAmbito,
			Integer codiceEnte, String codSoggetto, Integer idModalitaPagamento,
			String codiceModalitaPagamento, OttimizzazioneModalitaPagamentoDto datiOttimizzazione, boolean cercaMdpCessionePerChiaveModPag)
	{

		List<ModalitaPagamentoSoggetto> modalitaPagamentoSoggettoList = new ArrayList<ModalitaPagamentoSoggetto>();
		ModalitaPagamentoSoggetto modPagDef = null;
		ModalitaPagamentoSoggetto modPagMod = null;

		SiacTModpagFin mdpDef = null;
		SiacTModpagModFin mdpMod = null;
		SiacTSoggettoFin siacTSoggetto = null;
		// se idMDP esiste e codiceMDP esiste o meno allora cerco comunque per
		// id
		if (null == idModalitaPagamento || idModalitaPagamento == 0)
		{
			// altrimenti cerco per codice modalita pagamento

			// chiamata al soggetto per avere il refresh
			siacTSoggetto = siacTSoggettoRepository.ricercaSoggettoNoSeSede(codiceAmbito,
					codiceEnte, codSoggetto, Constanti.SEDE_SECONDARIA, getNow());
			// chiamata per mpd def
			mdpDef = siacTSoggettoRepository.ricercaModalitaPagamentoPerChiaveCodiceDef(codiceEnte,
					codSoggetto, Integer.valueOf(codiceModalitaPagamento));

			// TODO
			// per i mod

		}
		else
		{
			// si comporta alla maniera classica come per tutti i punti
			// lavora su uid
			if(datiOttimizzazione!=null){
				//RAMO OTTIMIZZATO
				mdpDef = datiOttimizzazione.findSiacTModpagFinByID(idModalitaPagamento);
				mdpMod = datiOttimizzazione.findSiacTModpagModFinByModPagId(idModalitaPagamento);
			} else {
				//RAMO CLASSICO
				mdpDef = siacTSoggettoRepository.ricercaModalitaPagamentoPerChiaveDef(codiceEnte,codSoggetto, idModalitaPagamento);
				mdpMod = siacTSoggettoRepository.ricercaModalitaPagamentoPerChiaveMod(codiceEnte,codSoggetto, idModalitaPagamento);
			}
			
		}
		
		if (mdpDef != null)
		{
			modPagDef = map(mdpDef, ModalitaPagamentoSoggetto.class,FinMapId.SiacTModPag_ModalitaPagamentoSoggetto);
			modPagDef = EntityToModelConverter.modalitaPagamentoSingleEntityToModalitaPagamentoModel(mdpDef, modPagDef,codSoggetto);
			modalitaPagamentoSoggettoList.add(modPagDef);
		}

		if (mdpMod != null)
		{
			modPagMod = map(mdpMod, ModalitaPagamentoSoggetto.class,FinMapId.SiacTModPagMod_ModalitaPagamentoSoggetto);
			modPagMod = EntityToModelConverter.modalitaPagamentoModSingleEntityToModalitaPagamentoModel(mdpMod, modPagMod,codSoggetto);
			modPagMod.setInModifica(Boolean.TRUE);
			modalitaPagamentoSoggettoList.add(modPagMod);
		}

		if (mdpDef == null)
		{
			// TODO da fare lo stesso discorso per le cessioni
			if (null == idModalitaPagamento || idModalitaPagamento == 0)
			{
				// se non c'e' allora cerco per codice mdp
				// r_odine per id and codMdp
				List<SiacRModpagOrdineFin> siacRModpagOrdines = siacRModpagOrdineRepository.findBySoggettoECodiceMdp(codiceEnte,Integer.valueOf(codiceModalitaPagamento),siacTSoggetto.getSoggettoId());

				if (siacRModpagOrdines != null && !siacRModpagOrdines.isEmpty())
				{

					// prendo sempre l'unico record
					if (siacRModpagOrdines.get(0).getSiacRSoggrelModpag().getSiacRSoggettoRelaz() != null)
					{
						Integer idSoggrelmpagId = siacRModpagOrdines.get(0).getSiacRSoggrelModpag().getSiacRSoggettoRelaz().getSoggettoRelazId();
						if (null != idSoggrelmpagId)
						{
							// sostituisco la variabile cosi dopo rimane tutto
							// invariato
							idModalitaPagamento = idSoggrelmpagId;
							mdpDef = siacTSoggettoRepository.ricercaModalitaPagamentoCessionePerChiaveDef(idModalitaPagamento);
						}
					}

				}

			}
			else
			{
				// se c'e' uid allora faccio il giro classico
				// SIAC-5218
				if (cercaMdpCessionePerChiaveModPag)
					mdpDef = siacTSoggettoRepository.ricercaModalitaPagamentoCessionePerChiaveModPag(idModalitaPagamento);
				else
					mdpDef = siacTSoggettoRepository.ricercaModalitaPagamentoCessionePerChiaveDef(idModalitaPagamento);
			}

			SiacTModpagFin mdoModCessioni = siacTSoggettoRepository.ricercaModalitaPagamentoCessionePerChiaveMod(idModalitaPagamento);

			if (mdpDef != null)
			{
				modPagDef = map(mdpDef, ModalitaPagamentoSoggetto.class,FinMapId.SiacTModPag_ModalitaPagamentoSoggetto);
				modPagDef = modalitaPagamentoSingleEntityToModalitaPagamentoCessioniModel(codiceAmbito, mdpDef, modPagDef, codSoggetto, codiceEnte,idModalitaPagamento);
				// se cerco per codice lo setto
				modPagDef.setCodiceModalitaPagamento(codiceModalitaPagamento);
				modalitaPagamentoSoggettoList.add(modPagDef);
			}

			if (mdoModCessioni != null)
			{
				modPagMod = map(mdoModCessioni, ModalitaPagamentoSoggetto.class,FinMapId.SiacTModPag_ModalitaPagamentoSoggetto);
				modPagMod = modalitaPagamentoSingleEntityToModalitaPagamentoCessioniModModel(codiceAmbito, mdoModCessioni, modPagMod, codSoggetto, codiceEnte,idModalitaPagamento);
				modalitaPagamentoSoggettoList.add(modPagMod);
			}

		}

		modalitaPagamentoSoggettoList = modalitaPagamentoSoggettoHelper.componiDescrizioneArricchita(modalitaPagamentoSoggettoList, datiOttimizzazione, codiceEnte);
		
		// Termino restituendo l'oggetto di ritorno:
		return modalitaPagamentoSoggettoList;
	}

	public Soggetto ricercaSoggetto(String codiceAmbito, Integer codiceEnte,
			ParametroRicercaSoggettoK parametroSoggettoK, boolean caricaDatiUlteriori)
	{
		if (parametroSoggettoK.getCodice() != null)
			return ricercaSoggetto(codiceAmbito, codiceEnte, parametroSoggettoK.getCodice(),
					parametroSoggettoK.isIncludeModif(), caricaDatiUlteriori);

		if (parametroSoggettoK.getMatricola() != null)
			return ricercaSoggetto(siacTSoggettoRepository.ricercaSoggettoNoSeSedeByMatricola(
					codiceAmbito, codiceEnte, parametroSoggettoK.getMatricola(),
					Constanti.SEDE_SECONDARIA, getNow()), parametroSoggettoK.isIncludeModif(),
					caricaDatiUlteriori);

		return null;

	}

	public Soggetto ricercaSoggetto(String codiceAmbito, Integer codiceEnte, String codiceSoggetto,
			boolean includeModifica, boolean caricaDatiUlteriori)
	{
		// SIAC-6847
		String cod = codiceAmbito;
		Integer codEnt = codiceEnte;
		String codSog = codiceSoggetto;
		boolean incMod = includeModifica;
		boolean carDatUlt = caricaDatiUlteriori;
		SiacTSoggettoFin soggettoInIngresso = siacTSoggettoRepository.ricercaSoggettoNoSeSede(codiceAmbito,
				codiceEnte, codiceSoggetto, Constanti.SEDE_SECONDARIA, getNow());
		
		return ricercaSoggetto(siacTSoggettoRepository.ricercaSoggettoNoSeSede(codiceAmbito,
				codiceEnte, codiceSoggetto, Constanti.SEDE_SECONDARIA, getNow()), includeModifica,
				caricaDatiUlteriori);
	}
	

	private Soggetto ricercaSoggetto(SiacTSoggettoFin soggEntity, boolean includeModifica,
			boolean caricaDatiUlteriori)
	{


		Soggetto soggettoTrovato = null;

		if (soggEntity != null)
		{
			soggettoTrovato = map(soggEntity, Soggetto.class, FinMapId.SiacTSoggetto_Soggetto);
			log.debug("ricercaSoggetto","soggettoTrovato: " +soggettoTrovato.getUid());

			if (soggettoTrovato.getCodiceFiscale() != null)
				soggettoTrovato.setCodiceFiscale(soggettoTrovato.getCodiceFiscale().trim());

			// flag sulla residenza estera
			if (org.apache.commons.lang.StringUtils.isEmpty(soggettoTrovato
					.getCodiceFiscaleEstero()))
				soggettoTrovato.setResidenteEstero(false);
			else
				soggettoTrovato.setResidenteEstero(true);

			// 1. STATO, CONTATTI, ONERI E CLASSIFICAZIONI:

			List<SiacTSoggettoFin> dtos = new ArrayList<SiacTSoggettoFin>();
			dtos.add(soggEntity);

			List<Soggetto> soggettos = new ArrayList<Soggetto>();
			soggettos.add(soggettoTrovato);

			if (includeModifica)
			{

				// estrae soggetto con stato portato artificiosamente in
				// modifica
				soggettoTrovato = EntityToModelConverter.soggettoEntityToSoggettoModel(dtos,
						soggettos, true).get(0);

			}
			else
			{
				// estrae soggetto con lo stato reale
				soggettoTrovato = EntityToModelConverter.soggettoEntityToSoggettoModel(dtos,
						soggettos, false).get(0);
			}
			// /////////////////////
			if (caricaDatiUlteriori)
			{

				// 2. INDIRIZZI SOGGETTO
				//INDIRIZZI
				List<IndirizzoSoggetto> listaIndirizziSoggetto = caricaIndirizziSoggetto(soggEntity);
				soggettoTrovato.setIndirizzi(listaIndirizziSoggetto);
				impostaFlagAvviso(listaIndirizziSoggetto, soggettoTrovato);
				//

				// String note =
				// EntityToModelConverter.estraiNotaValida(soggEntity);
				// soggettoTrovato.setNote(note);

				// NOTE
				soggettoTrovato.setNote(EntityToModelConverter.estraiAttrSoggetto(soggEntity,Constanti.T_ATTR_CODE_NOTE_SOGGETTO));

				// MATRICOLA
				soggettoTrovato.setMatricola(EntityToModelConverter.estraiAttrSoggetto(soggEntity,Constanti.T_ATTR_CODE_MATRICOLA));

				List<Contatto> contatti = EntityToModelConverter.siacTRecapitoSoggettoToContattoList(soggEntity.getSiacTRecapitoSoggettos(), true);
				soggettoTrovato.setContatti(contatti);
				// END INDIRIZZI SOGGETTO

			}
			
			//SIAC-6847
			//passo al soggetto l'ambito in caso di CEC
			soggettoTrovato.setCodificaAmbito(soggEntity.getSiacDAmbito().getAmbitoCode());
		}

		return soggettoTrovato;
	}
	
	/**
	 * Wrapper di retrocompatibilita' rispetto al caricamento delle sedi decentrate introdotto ad OTTOBRE 2016 
	 * @param soggEntity
	 * @param includeModifica
	 * @param caricaDatiUlteriori
	 * @param ottimizzazioneSoggetto
	 * @param datiOperazioneDto
	 * @return
	 */
	public Soggetto ricercaSoggettoOPT(SiacTSoggettoFin soggEntity, boolean includeModifica,
			boolean caricaDatiUlteriori, OttimizzazioneSoggettoDto ottimizzazioneSoggetto, DatiOperazioneDto datiOperazioneDto
			)
	{
		boolean caricaSediSecondarie = false; 
		boolean isDecentrato = false;
		return ricercaSoggettoOPT(soggEntity, includeModifica, caricaDatiUlteriori, ottimizzazioneSoggetto, datiOperazioneDto,caricaSediSecondarie,isDecentrato);
		
	}
	
	/**
	 *  Wrapper di retrocompatibilita' rispetto al caricamento delle sedi decentrate introdotto ad OTTOBRE 2016 
	 * @param soggEntityList
	 * @param includeModifica
	 * @param caricaDatiUlteriori
	 * @param ottimizzazioneSoggetto
	 * @param datiOperazioneDto
	 * @return
	 */
	public List<Soggetto> ricercaSoggettoOPT(List<SiacTSoggettoFin> soggEntityList,
			boolean includeModifica, boolean caricaDatiUlteriori,
			OttimizzazioneSoggettoDto ottimizzazioneSoggetto, DatiOperazioneDto datiOperazioneDto) 
	{
		boolean caricaSediSecondarie = false; 
		boolean isDecentrato = false;
		return ricercaSoggettoOPT(soggEntityList, includeModifica, caricaDatiUlteriori, ottimizzazioneSoggetto, datiOperazioneDto,caricaSediSecondarie,isDecentrato);
		
	}

	/**
	 * wrapper di ricercaSoggettoOPT
	 * 
	 * @param soggEntity
	 * @param includeModifica
	 * @param caricaDatiUlteriori
	 * @param ottimizzazioneSoggetto
	 * @return
	 */
	public Soggetto ricercaSoggettoOPT(SiacTSoggettoFin soggEntity, boolean includeModifica,
			boolean caricaDatiUlteriori, OttimizzazioneSoggettoDto ottimizzazioneSoggetto, DatiOperazioneDto datiOperazioneDto,
			boolean caricaSediSecondarie,boolean isDecentrato)
	{
		List<SiacTSoggettoFin> soggEntityList = toList(soggEntity);
		List<Soggetto> listed = ricercaSoggettoOPT(soggEntityList, includeModifica,
				caricaDatiUlteriori, ottimizzazioneSoggetto, datiOperazioneDto,caricaSediSecondarie,isDecentrato);
		if (listed != null && listed.size() > 0)
		{
			// Termino restituendo l'oggetto di ritorno:
			return listed.get(0);
		}
		// Termino restituendo null, per soggetto non trovato
		return null;
	}

	/**
	 * Ricerca ottimizzata dei soggetti
	 * 
	 * @param soggEntityList
	 * @param includeModifica
	 * @param caricaDatiUlteriori
	 * @param ottimizzazioneSoggetto
	 * @return
	 */
	public List<Soggetto> ricercaSoggettoOPT(List<SiacTSoggettoFin> soggEntityList,
			boolean includeModifica, boolean caricaDatiUlteriori,
			OttimizzazioneSoggettoDto ottimizzazioneSoggetto, DatiOperazioneDto datiOperazioneDto,boolean caricaSediSecondarie,boolean isDecentrato) 
	{

		
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getUid();
		
		List<Soggetto> listaRitorno = new ArrayList<Soggetto>();

		if (soggEntityList != null && soggEntityList.size() > 0)
		{

			for (SiacTSoggettoFin soggEntity : soggEntityList)
			{

				Soggetto soggettoTrovato = null;
				
				/**
				 * Il chiamante in alcuni casi puo' passarci gia' un elenco di soggetti caricati, 
				 * ne approfittiamo per maggiore ottimizzazione:
				 */
				if(!StringUtils.isEmpty(ottimizzazioneSoggetto.getSoggettiGiaCaricati())){
					Soggetto soggettoGiaCaricato = CommonUtils.getById(ottimizzazioneSoggetto.getSoggettiGiaCaricati(),soggEntity.getUid());
					if(soggettoGiaCaricato!=null){
						soggettoTrovato = clone(soggettoGiaCaricato);
					}
				}
				
				if(soggettoTrovato==null || soggettoTrovato.getUid()==0){
					//DEVO CARICARLO SUL SERIO:
					
					soggettoTrovato = map(soggEntity, Soggetto.class, FinMapId.SiacTSoggetto_Soggetto);

					if (soggettoTrovato.getCodiceFiscale() != null)
						soggettoTrovato.setCodiceFiscale(soggettoTrovato.getCodiceFiscale().trim());

					// flag sulla residenza estera
					if (org.apache.commons.lang.StringUtils.isEmpty(soggettoTrovato.getCodiceFiscaleEstero()))
						soggettoTrovato.setResidenteEstero(false);
					else
						soggettoTrovato.setResidenteEstero(true);

					// 1. STATO, CONTATTI, ONERI E CLASSIFICAZIONI:

					List<SiacTSoggettoFin> dtos = new ArrayList<SiacTSoggettoFin>();
					dtos.add(soggEntity);

					List<Soggetto> soggettos = new ArrayList<Soggetto>();
					soggettos.add(soggettoTrovato);

					if (includeModifica)
					{

						// estrae soggetto con stato portato artificiosamente in
						// modifica
						soggettoTrovato = EntityToModelConverter.soggettoEntityToSoggettoModelOPT(dtos,soggettos, true, ottimizzazioneSoggetto).get(0);

					}
					else
					{
						// estrae soggetto con lo stato reale
						soggettoTrovato = EntityToModelConverter.soggettoEntityToSoggettoModelOPT(dtos,soggettos, false, ottimizzazioneSoggetto).get(0);
					}
					// /////////////////////
					if (caricaDatiUlteriori)
					{
						//INDIRIZZI:
						List<IndirizzoSoggetto> listaIndirizziSoggetto = caricaIndirizziSoggetto(soggEntity);
						soggettoTrovato.setIndirizzi(listaIndirizziSoggetto);
						impostaFlagAvviso(listaIndirizziSoggetto, soggettoTrovato);
						//
						
						//NOTE:
						//String note = EntityToModelConverter.estraiNotaValida(soggEntity);
						//soggettoTrovato.setNote(note);
						soggettoTrovato.setNote(EntityToModelConverter.estraiAttrSoggetto(soggEntity,Constanti.T_ATTR_CODE_NOTE_SOGGETTO));
						
						//CONTATTI:
						List<Contatto> contatti = EntityToModelConverter.siacTRecapitoSoggettoToContattoList(soggEntity.getSiacTRecapitoSoggettos(), true);
						soggettoTrovato.setContatti(contatti);

						// MATRICOLA:
						soggettoTrovato.setMatricola(EntityToModelConverter.estraiAttrSoggetto(soggEntity,Constanti.T_ATTR_CODE_MATRICOLA));

					}
					
					
					//ricerca modalita di pagamento
					List<ModalitaPagamentoSoggetto> listaModPag = ricercaModalitaPagamentoOPT(Constanti.AMBITO_FIN,soggEntity.getUid(), "Soggetto", false,ottimizzazioneSoggetto, datiOperazioneDto,soggEntity);
					if(listaModPag!=null && listaModPag.size()>0){
						soggettoTrovato.setElencoModalitaPagamento(listaModPag);
						soggettoTrovato.setModalitaPagamentoList(listaModPag);
					}
					
					//OTTOBRE 2016 - aggiunta la possibilita di caricare le sedi secondarie (esigenza nata in contesti di ottimizzazione)
					//ricerca sedi secondarie:
					if(caricaSediSecondarie){
						List<SedeSecondariaSoggetto> listaSedi = ricercaSediSecondarie(idEnte, soggEntity.getUid(), isDecentrato, includeModifica,datiOperazioneDto, ottimizzazioneSoggetto);
						soggettoTrovato.setSediSecondarie(listaSedi);
					}
					
					
					
				}
				
				//aggiungo alla lista da ritornare:
				listaRitorno.add(soggettoTrovato);
				//
			}
		}

		// Termino restituendo l'oggetto di ritorno:
		return listaRitorno;
	}
	
	/**
	 * Dato un soggetto carica gli indirizzi restituendo il principale.
	 * 
	 * Se e' dammiIlPrimoSeNessunoPrincipale e' passato a true il comportamento diventa:
	 * Ritorna il primo indirizzo trovato nel caso nessun indirizzo sia principale, altrimenti
	 * ritorna ovviamente quello principale.
	 * 
	 * @param soggEntity
	 * @return
	 */
	private IndirizzoSoggetto caricaIndirizzoPrincipale(SiacTSoggettoFin soggEntity, boolean dammiIlPrimoSeNessunoPrincipale){
		IndirizzoSoggetto principale = null;
		List<IndirizzoSoggetto> indirizzi = caricaIndirizziSoggetto(soggEntity);
		if(!isEmpty(indirizzi)){
			
			//con dammiIlPrimoSeNessunoPrincipale inizializzo al primo cosi
			//ne tornera' almeno uno anche in assenza di principale:
			if(dammiIlPrimoSeNessunoPrincipale){
				principale = getFirst(indirizzi);
			}
			//
			
			for(IndirizzoSoggetto it: indirizzi){
				if(it!=null && it.isCheckPrincipale()){
					principale = it;
					break;
				}
			}
		}
		return principale;
	}
	
	/**
	 * Carica gli indirizzi del soggetto indicato, presuppone che
	 * soggEntity sia gia stato caricato
	 * @param soggEntity
	 * @return
	 */
	private List<IndirizzoSoggetto> caricaIndirizziSoggetto(SiacTSoggettoFin soggEntity){
		// 2. INDIRIZZI SOGGETTO
		List<SiacTIndirizzoSoggettoFin> listaTIndirizziSoggetto = soggEntity.getSiacTIndirizzoSoggettos();
		List<IndirizzoSoggetto> listaIndirizziSoggetto = null;
		listaIndirizziSoggetto = convertiLista(listaTIndirizziSoggetto,IndirizzoSoggetto.class, FinMapId.SiacTIndirizzo_IndirizzoSoggetto);
		listaIndirizziSoggetto = EntityToModelConverter.indirizzoSoggettoEntityToIndirizzoSoggettoModel(listaTIndirizziSoggetto, listaIndirizziSoggetto);
		return listaIndirizziSoggetto;
	}
	
	/**
	 * Imposta il flag avviso in soggettoTrovato 
	 * valutando gli indirizzi in listaIndirizziSoggetto
	 * @param listaIndirizziSoggetto
	 * @param soggettoTrovato
	 */
	private void impostaFlagAvviso(List<IndirizzoSoggetto> listaIndirizziSoggetto, Soggetto soggettoTrovato){
		//
		if (listaIndirizziSoggetto != null && listaIndirizziSoggetto.size() > 0)
		{
			soggettoTrovato.setAvviso(false);
			for (IndirizzoSoggetto indirizzoSoggetto : listaIndirizziSoggetto)
			{
				// se trovo uno degli indirizzi con il
				// flag avviso a true allora
				if (indirizzoSoggetto.getAvviso() != null && !indirizzoSoggetto.getAvviso().trim().equals("") && indirizzoSoggetto.getAvviso().equalsIgnoreCase(Constanti.STRING_TRUE))
				{
					soggettoTrovato.setAvviso(true);
				}
			}
		}
	}
	
	/**
	 * Dato l'uid di un Ente si occupa di recuperare l'indirizzo principale del soggetto
	 * in relazione con l'ente tramite siac_r_soggetto_ente_proprietario.
	 * 
	 * Utilizza come istante di validita quanto indicato in datiOperazione, altrimenti istanzia data corrente.
	 * 
	 * Se e' dammiIlPrimoSeNessunoPrincipale e' passato a true il comportamento diventa:
	 * Ritorna il primo indirizzo trovato nel caso nessun indirizzo sia principale, altrimenti
	 * ritorna ovviamente quello principale.
	 * 
	 * @param uidEnte
	 * @param datiOperazione
	 * @return
	 */
	public IndirizzoSoggetto getIndizzoPrincipaleEnte(Integer uidEnte, DatiOperazioneDto datiOperazione, boolean dammiIlPrimoSeNessunoPrincipale){
		IndirizzoSoggetto indPrincipale = new IndirizzoSoggetto();
		if(uidEnte!=null){
			
			//Utilizza come istante di validita quanto indicato in datiOperazione,
			//altrimenti istanzia data corrente.
			Timestamp dataInput = getNow();
			if(datiOperazione!=null && datiOperazione.getTs()!=null){
				dataInput = datiOperazione.getTs();
			}
			
			//Relazione valida tra ente e soggetto:
			SiacRSoggettoEnteProprietarioFin valido = getFirst(siacRSoggettoEnteProprietarioFinRepository.findValidoByEnte(uidEnte, dataInput));
			if(valido!=null){
				
				//Ricarico il soggetto:
				SiacTSoggettoFin siacTSogg = siacTSoggettoRepository.findOne(valido.getSiacTSoggetto().getUid());
				
				//carichiamo l'indirizzo principale:
				indPrincipale = caricaIndirizzoPrincipale(siacTSogg,dammiIlPrimoSeNessunoPrincipale);
				
			}
		}
		return indPrincipale;
	}
	
	/**
	 * Recupera l'indirizzo principale del soggetto indicato.
	 * 
	 * Utilizza come istante di validita quanto indicato in datiOperazione, altrimenti istanzia data corrente.
	 * 
	 * Se e' dammiIlPrimoSeNessunoPrincipale e' passato a true il comportamento diventa:
	 * Ritorna il primo indirizzo trovato nel caso nessun indirizzo sia principale, altrimenti
	 * ritorna ovviamente quello principale.
	 * 
	 * @param uidEnte
	 * @param codiceSoggetto
	 * @param codiceAmbito
	 * @param datiOperazione
	 * @param dammiIlPrimoSeNessunoPrincipale
	 * @return
	 */
	public IndirizzoSoggetto getIndizzoPrincipaleSoggetto(Integer uidEnte, String codiceSoggetto,String codiceAmbito, DatiOperazioneDto datiOperazione, boolean dammiIlPrimoSeNessunoPrincipale){
		IndirizzoSoggetto indPrincipale = new IndirizzoSoggetto();
		if(uidEnte!=null && !isEmpty(codiceSoggetto) && !isEmpty(codiceAmbito)){
			
			//Utilizza come istante di validita quanto indicato in datiOperazione,
			//altrimenti istanzia data corrente.
			Timestamp dataInput = getNow();
			if(datiOperazione!=null && datiOperazione.getTs()!=null){
				dataInput = datiOperazione.getTs();
			}
			
			SiacTSoggettoFin siacTSogg = siacTSoggettoRepository.ricercaSoggettoNoSeSede(codiceAmbito,uidEnte, codiceSoggetto, Constanti.SEDE_SECONDARIA, dataInput);
			
			if(siacTSogg!=null){
				
				//carichiamo l'indirizzo principale:
				indPrincipale = caricaIndirizzoPrincipale(siacTSogg,dammiIlPrimoSeNessunoPrincipale);
				
			}
		}
		return indPrincipale;
	}

	/**
	 * ricercaSoggettoInModifica
	 * 
	 * @param codiceEnte
	 * @param codSoggetto
	 * @return
	 */
	public Soggetto ricercaSoggettoInModifica(Integer codiceEnte, String codSoggetto)
	{
		Soggetto soggettoTrovato = null;
		SiacTSoggettoModFin soggEntity = siacTSoggettoModRepository.ricercaSoggetto(codiceEnte,
				codSoggetto);
		if (soggEntity != null)
		{
			soggettoTrovato = map(soggEntity, Soggetto.class, FinMapId.SiacTSoggettoMod_Soggetto);
			// 1. STATO, CONTATTI, ONERI E CLASSIFICAZIONI:
			soggettoTrovato = EntityToModelConverter.soggettoEntityModToSoggettoModel(soggEntity,
					soggettoTrovato);

			// 2. INDIRIZZI SOGGETTO
			List<SiacTIndirizzoSoggettoModFin> listaTIndirizziSoggettoMod = soggEntity
					.getSiacTIndirizzoSoggettoMods();
			List<IndirizzoSoggetto> listaIndirizziSoggetto = null;
			listaIndirizziSoggetto = convertiLista(listaTIndirizziSoggettoMod,
					IndirizzoSoggetto.class, FinMapId.SiacTIndirizzoMod_IndirizzoSoggetto);

			listaIndirizziSoggetto = EntityToModelConverter
					.indirizzoSoggettoModEntityToIndirizzoSoggettoModel(listaTIndirizziSoggettoMod,
							listaIndirizziSoggetto);

			// 55
			// String note =
			// EntityToModelConverter.estraiNotaValidaMod(soggEntity);
			// soggettoTrovato.setNote(note);
			//
			// NOTE
			soggettoTrovato.setNote(EntityToModelConverter.estraiAttrSoggettoMod(soggEntity,
					Constanti.T_ATTR_CODE_NOTE_SOGGETTO));

			// MATRICOLA
			soggettoTrovato.setMatricola(EntityToModelConverter.estraiAttrSoggettoMod(soggEntity,
					Constanti.T_ATTR_CODE_MATRICOLA));

			soggettoTrovato.setIndirizzi(listaIndirizziSoggetto);
		}
		// Termino restituendo l'oggetto di ritorno:
		return soggettoTrovato;
	}

	/**
	 * ricercaModalitaPagamento
	 * 
	 * @param codEnte
	 * @param idSogg
	 * @param associatoA
	 * @param isIncludeModif
	 * @return
	 */
	public List<ModalitaPagamentoSoggetto> ricercaModalitaPagamento(String codiceAmbito,
			Integer codEnte, Integer idSogg, String associatoA, boolean isIncludeModif)
	{
		Timestamp now = new Timestamp(System.currentTimeMillis());
		List<ModalitaPagamentoSoggetto> listaModPags = new ArrayList<ModalitaPagamentoSoggetto>();
		List<SiacTModpagFin> listaTModPags = siacTSoggettoRepository.ricercaModPag(codEnte, idSogg,now);

		//MARZO 2016, NON VENIVANO ESCLUSE QUELLE NON PIU' VALIDE:
		listaTModPags = DatiOperazioneUtils.soloValidi(listaTModPags, getNow());
		//
		
		SiacTSoggettoFin siacTSoggetto = siacTSoggettoRepository.findOne(idSogg);

		if (listaTModPags != null && listaTModPags.size() > 0)
		{
			listaModPags = convertiLista(listaTModPags, ModalitaPagamentoSoggetto.class,
					FinMapId.SiacTModPag_ModalitaPagamentoSoggetto);
			if (listaTModPags != null && listaTModPags.size() > 0)
			{
				for (SiacTModpagFin siacTModpag : listaTModPags)
				{
					entityRefresh(siacTModpag);
				}
			}

			listaModPags = EntityToModelConverter.modalitaPagamentoEntityToModalitaPagamentoModel(
					listaTModPags, listaModPags, associatoA);

			// NEL CASO ABBIA ASSOCIATA UNA MODIFICA SETTO LO STATO INMODIFICA A
			// TRUE
			//
			// Modifica per jira-685
			//
			// - Se isIncludeModif == false : non sovrascrivo lo stato
			//
			// - Se isIncludeModif == true : sovrascrivo lo stato forzandolo a
			// IN_MODIFICA
			//
			List<ModalitaPagamentoSoggetto> appList = new ArrayList<ModalitaPagamentoSoggetto>();
			for (ModalitaPagamentoSoggetto mdpApp : listaModPags)
			{
				SiacTModpagModFin mdpModApp = siacTModpagModRepository.findValidoByMdpId(mdpApp
						.getUid());
				if (mdpModApp != null && isIncludeModif == true)
				{
					ModalitaPagamentoSoggetto modMdp = map(mdpModApp,
							ModalitaPagamentoSoggetto.class,
							FinMapId.SiacTModPagMod_ModalitaPagamentoSoggetto);
					modMdp = EntityToModelConverter
							.modalitaPagamentoModSingleEntityToModalitaPagamentoModel(mdpModApp,
									modMdp, associatoA);

					modMdp.setInModifica(true);
					modMdp.setLoginCreazione(mdpApp.getLoginCreazione());
					modMdp.setDataCreazione(mdpApp.getDataCreazione());
					modMdp.setLoginModifica(mdpModApp.getLoginOperazione());
					modMdp.setDescrizioneStatoModalitaPagamento(Constanti.STATO_IN_MODIFICA_no_underscore);
					modMdp.setUidOrigine(mdpApp.getUid());
					modMdp.setAssociatoA(mdpApp.getAssociatoA());
					modMdp.setTipoAccredito(mdpApp.getTipoAccredito());
					modMdp.setDataUltimaModifica(mdpApp.getDataModifica());
					modMdp.setLoginUltimaModifica(mdpApp.getLoginModifica());
					modMdp.setModalitaOriginale(mdpApp);
					modMdp.setCodiceModalitaPagamento(mdpApp.getCodiceModalitaPagamento());

					appList.add(modMdp);
				}
				else
				{
					appList.add(mdpApp);
				}
			}

			listaModPags = appList;
		}

		// Gestione cessione
		List<SiacTModpagFin> listaTModpagsCessioni = siacTSoggettoRepository.ricercaModPagCessioni(
				codEnte, idSogg, now);
		
		//MARZO 2016, NON VENIVANO ESCLUSE QUELLE NON PIU' VALIDE:
		listaTModpagsCessioni = DatiOperazioneUtils.soloValidi(listaTModpagsCessioni, getNow());
		//
		
		List<ModalitaPagamentoSoggetto> listaModPagsCessioni = null;
		if (listaTModpagsCessioni != null)
		{
			listaModPagsCessioni = convertiLista(listaTModpagsCessioni,
					ModalitaPagamentoSoggetto.class, FinMapId.SiacTModPag_ModalitaPagamentoSoggetto);

			entityRefresh(siacTSoggetto);
			listaModPagsCessioni = modalitaPagamentoEntityToModalitaPagamentoCessioniModel(
					siacTSoggetto, listaTModpagsCessioni, listaModPagsCessioni, idSogg, associatoA,
					now);
		}

		if (listaModPagsCessioni != null)
		{
			if (!listaModPagsCessioni.isEmpty())
			{

				// Gestisco lo stato in modifica
				//
				// Modifica per jira-685
				//
				// - Se isIncludeModif == false : non sovrascrivo lo stato
				//
				// - Se isIncludeModif == true : sovrascrivo lo stato forzandolo
				// a IN_MODIFICA
				//

				List<ModalitaPagamentoSoggetto> appList = new ArrayList<ModalitaPagamentoSoggetto>();
				for (ModalitaPagamentoSoggetto app : listaModPagsCessioni)
				{
					SiacTModpagFin mdoModCessioni = siacTSoggettoRepository
							.ricercaModalitaPagamentoCessionePerChiaveMod(app.getUid());

					if (mdoModCessioni != null && isIncludeModif == true)
					{

						ModalitaPagamentoSoggetto modPagMod = map(mdoModCessioni,
								ModalitaPagamentoSoggetto.class,
								FinMapId.SiacTModPag_ModalitaPagamentoSoggetto);
						modPagMod = modalitaPagamentoSingleEntityToModalitaPagamentoCessioniModModel(
								codiceAmbito, mdoModCessioni, modPagMod,
								siacTSoggetto.getSoggettoCode(), codEnte, idSogg);

						modPagMod.setLoginCreazione(app.getLoginCreazione());
						modPagMod.setDataCreazione(app.getDataCreazione());

						modPagMod.setInModifica(true);
						modPagMod
								.setDescrizioneStatoModalitaPagamento(Constanti.STATO_IN_MODIFICA_no_underscore);
						modPagMod.setUidOrigine(app.getUid());
						modPagMod.setAssociatoA(app.getAssociatoA());
						modPagMod.setTipoAccredito(app.getTipoAccredito());
						modPagMod.setDataUltimaModifica(app.getDataModifica());
						modPagMod.setLoginUltimaModifica(app.getLoginUltimaModifica());
						modPagMod.setCodiceModalitaPagamento(app.getCodiceModalitaPagamento());
						modPagMod.setModalitaOriginale(app);

						appList.add(modPagMod);
					}
					else
					{
						appList.add(app);
					}
				}

				listaModPagsCessioni = appList;
				listaModPags.addAll(listaModPagsCessioni);
			}
		}

		if (listaModPags != null && listaModPags.size() > 0){
			
			//PRIMA DI AGGIUNGERE LA MOD PAG NELLA LISTA FINALE
			//COSTRUISCO E SETTO LA DESCRIZIONE ARRICCHITA:
			listaModPags = modalitaPagamentoSoggettoHelper.componiDescrizioneArricchita(listaModPags,null ,codEnte);
			
			// correttiva all anomalia di ordinamento delle mdp
			Collections.sort(listaModPags, new MdpComparator());
		}

		// Termino restituendo l'oggetto di ritorno:
		return listaModPags;
	}
	
	/**
	 * ricercaModalitaPagamento
	 * 
	 * @param codEnte
	 * @param idSogg
	 * @param associatoA
	 * @param isIncludeModif
	 * @return
	 */
	public List<ModalitaPagamentoSoggetto> ricercaModalitaPagamentoOPT(
			String codiceAmbito, Integer idSogg,
			String associatoA, boolean isIncludeModif,
			OttimizzazioneSoggettoDto ottimizzazioneSoggetto, DatiOperazioneDto datiOperazioneDto,
			SiacTSoggettoFin siacTSoggetto) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		List<ModalitaPagamentoSoggetto> listaModPags = new ArrayList<ModalitaPagamentoSoggetto>();
		
		Integer codEnte = datiOperazioneDto.getSiacTEnteProprietario().getUid();
		
		
		
		if(siacTSoggetto==null && idSogg!=null){
			//Carico solo se strettamente necessario
			siacTSoggetto = siacTSoggettoRepository.findOne(idSogg);
			idSogg = siacTSoggetto.getUid(); 
		} else if (siacTSoggetto==null && idSogg==null){
			//input non valido, almeno uno tra siacTSoggetto deve essere valorizzato
			return listaModPags;
		} 

		//
		List<SiacTModpagFin> listaTModPagsNew = ottimizzazioneSoggetto.filtraSiacTModpagFinBySoggettoId(idSogg);
		List<SiacTModpagFin> listaTModpagsCessioniNew = ottimizzazioneSoggetto.getListaTModpagsCessioni(siacTSoggetto);
		//
		
		//TEST scommentare se si hanno dubbi:
		/*List<SiacTModpagFin> listaTModPagsOld = siacTSoggettoRepository.ricercaModPag(codEnte, idSogg, now);
		listaTModPagsOld = DatiOperazioneUtils.soloValidi(listaTModPagsOld, getNow());
		boolean modPagsUguali = CommonUtils.sonoUgualiSiacTBaseByUid(listaTModPagsNew, listaTModPagsOld);
		if(!modPagsUguali){
			String ciao ="metti un breakpoint qui per intercettare errori";
			ciao.charAt(0);
		}*/
		
		if (listaTModPagsNew != null && listaTModPagsNew.size() > 0) {
			
			listaModPags = convertiLista(listaTModPagsNew,ModalitaPagamentoSoggetto.class,FinMapId.SiacTModPag_ModalitaPagamentoSoggetto);
			
			//ROBA DEL METODO NON OTTIMIZZATO (deriva da copia incolla di tale metodo dal quale questo e' stato derivato) qui 
			//non dovrebbe servire, commento:
//			if (listaTModPagsNew != null && listaTModPagsNew.size() > 0) {
//				for (SiacTModpagFin siacTModpag : listaTModPags) {
//					entityRefresh(siacTModpag);
//				}
//			}

			listaModPags = EntityToModelConverter.modalitaPagamentoEntityToModalitaPagamentoModel(listaTModPagsNew, listaModPags, associatoA,ottimizzazioneSoggetto);

			// NEL CASO ABBIA ASSOCIATA UNA MODIFICA SETTO LO STATO INMODIFICA A
			// TRUE
			//
			// Modifica per jira-685
			//
			// - Se isIncludeModif == false : non sovrascrivo lo stato
			//
			// - Se isIncludeModif == true : sovrascrivo lo stato forzandolo a
			// IN_MODIFICA
			//
			List<ModalitaPagamentoSoggetto> appList = new ArrayList<ModalitaPagamentoSoggetto>();
			
			for (ModalitaPagamentoSoggetto mdpApp : listaModPags) {
				
				Integer idModPag = mdpApp.getUid();
				
				SiacTModpagModFin mdpModAppNew = ottimizzazioneSoggetto.findSiacTModpagModFinByModPagId(idModPag);
				
				//TEST scommentare se si hanno dubbi:
				/*SiacTModpagModFin mdpModOld = siacTModpagModRepository.findValidoByMdpId(idModPag);
				boolean modpagModFinUguali = CommonUtils.sonoUgualiSiacTBaseByUid(mdpModOld, mdpModAppNew);
				if(!modpagModFinUguali){
					String ciao ="metti un breakpoint qui per intercettare errori";
					ciao.charAt(0);
				}*/
				
				if (mdpModAppNew != null && isIncludeModif == true) {
					
					ModalitaPagamentoSoggetto modMdp = map(mdpModAppNew,ModalitaPagamentoSoggetto.class,FinMapId.SiacTModPagMod_ModalitaPagamentoSoggetto);
					
					modMdp = EntityToModelConverter.modalitaPagamentoModSingleEntityToModalitaPagamentoModel(mdpModAppNew, modMdp, associatoA);

					modMdp.setInModifica(true);
					modMdp.setLoginCreazione(mdpApp.getLoginCreazione());
					modMdp.setDataCreazione(mdpApp.getDataCreazione());
					modMdp.setLoginModifica(mdpModAppNew.getLoginOperazione());
					modMdp.setDescrizioneStatoModalitaPagamento(Constanti.STATO_IN_MODIFICA_no_underscore);
					modMdp.setUidOrigine(mdpApp.getUid());
					modMdp.setAssociatoA(mdpApp.getAssociatoA());
					modMdp.setTipoAccredito(mdpApp.getTipoAccredito());
					modMdp.setDataUltimaModifica(mdpApp.getDataModifica());
					modMdp.setLoginUltimaModifica(mdpApp.getLoginModifica());
					modMdp.setModalitaOriginale(mdpApp);
					modMdp.setCodiceModalitaPagamento(mdpApp.getCodiceModalitaPagamento());

					appList.add(modMdp);
				} else {
					appList.add(mdpApp);
				}
			}

			listaModPags = appList;
		}

		// Gestione cessione
		
		
		//TEST scommentare se si hanno dubbi:
		/*List<SiacTModpagFin> listaTModpagsCessioniOld = siacTSoggettoRepository.ricercaModPagCessioni(codEnte, idSogg, now);
		listaTModpagsCessioniOld = DatiOperazioneUtils.soloValidi(listaTModpagsCessioniOld, getNow());
		boolean modPagsCessioniUguali = CommonUtils.sonoUgualiSiacTBaseByUid(listaTModpagsCessioniNew, listaTModpagsCessioniOld);
		if(!modPagsCessioniUguali){
			String ciao ="metti un breakpoint qui per intercettare errori";
			ciao.charAt(0);
		}*/
		
		
		List<ModalitaPagamentoSoggetto> listaModPagsCessioni = null;
		if (listaTModpagsCessioniNew != null) {
			listaModPagsCessioni = convertiLista(listaTModpagsCessioniNew,ModalitaPagamentoSoggetto.class,FinMapId.SiacTModPag_ModalitaPagamentoSoggetto);
			//entityRefresh(siacTSoggetto);
			listaModPagsCessioni = modalitaPagamentoEntityToModalitaPagamentoCessioniModel(siacTSoggetto, listaTModpagsCessioniNew, listaModPagsCessioni,idSogg, associatoA, now, ottimizzazioneSoggetto);
		}

		if (listaModPagsCessioni != null) {
			if (!listaModPagsCessioni.isEmpty()) {

				// Gestisco lo stato in modifica
				//
				// Modifica per jira-685
				//
				// - Se isIncludeModif == false : non sovrascrivo lo stato
				//
				// - Se isIncludeModif == true : sovrascrivo lo stato forzandolo
				// a IN_MODIFICA
				//

				List<ModalitaPagamentoSoggetto> appList = new ArrayList<ModalitaPagamentoSoggetto>();
				for (ModalitaPagamentoSoggetto app : listaModPagsCessioni) {
					
					if(isIncludeModif){
						
						//RAMO NON ANCORA OTTIMIZZATO PER ORA NON SERVE PERCHE'
						//isIncludeModif sempre false
						
						SiacTModpagFin mdoModCessioni = siacTSoggettoRepository.ricercaModalitaPagamentoCessionePerChiaveMod(app.getUid());
						if (mdoModCessioni != null) {
							
							ModalitaPagamentoSoggetto modPagMod = map(mdoModCessioni,ModalitaPagamentoSoggetto.class,FinMapId.SiacTModPag_ModalitaPagamentoSoggetto);
							modPagMod = modalitaPagamentoSingleEntityToModalitaPagamentoCessioniModModel(
									codiceAmbito, mdoModCessioni, modPagMod,
									siacTSoggetto.getSoggettoCode(), codEnte,
									idSogg);

							modPagMod.setLoginCreazione(app.getLoginCreazione());
							modPagMod.setDataCreazione(app.getDataCreazione());

							modPagMod.setInModifica(true);
							modPagMod.setDescrizioneStatoModalitaPagamento(Constanti.STATO_IN_MODIFICA_no_underscore);
							modPagMod.setUidOrigine(app.getUid());
							modPagMod.setAssociatoA(app.getAssociatoA());
							modPagMod.setTipoAccredito(app.getTipoAccredito());
							modPagMod.setDataUltimaModifica(app.getDataModifica());
							modPagMod.setLoginUltimaModifica(app.getLoginUltimaModifica());
							modPagMod.setCodiceModalitaPagamento(app.getCodiceModalitaPagamento());
							modPagMod.setModalitaOriginale(app);

							appList.add(modPagMod);
						} else {
							appList.add(app);
						}
					} else {
						appList.add(app);
					}
					
					
				}

				listaModPagsCessioni = appList;
				listaModPags.addAll(listaModPagsCessioni);
			}
		}

		if (listaModPags != null && listaModPags.size() > 0) {
			
			//PRIMA DI AGGIUNGERE LA MOD PAG NELLA LISTA FINALE
			//COSTRUISCO E SETTO LA DESCRIZIONE ARRICCHITA:
			OttimizzazioneModalitaPagamentoDto ottimizzazioneModPag = estraiDatiModPag(ottimizzazioneSoggetto);
			listaModPags = modalitaPagamentoSoggettoHelper.componiDescrizioneArricchita(listaModPags,ottimizzazioneModPag ,codEnte);
			
			// correttiva all anomalia di ordinamento delle mdp
			Collections.sort(listaModPags, new MdpComparator());
		}

		// Termino restituendo l'oggetto di ritorno:
		return listaModPags;
	}
	
	/**
	 * Wrapper di retrocompatibilita'
	 * @param siacTSoggetto
	 * @param dtos
	 * @param soggettoModPags
	 * @param idSogg
	 * @param associatoA
	 * @param now
	 * @return
	 */
	private List<ModalitaPagamentoSoggetto> modalitaPagamentoEntityToModalitaPagamentoCessioniModel(
			SiacTSoggettoFin siacTSoggetto, List<SiacTModpagFin> dtos,
			List<ModalitaPagamentoSoggetto> soggettoModPags, Integer idSogg, String associatoA,
			Timestamp now){
		List<ModalitaPagamentoSoggetto> listaModPags = modalitaPagamentoEntityToModalitaPagamentoCessioniModel(siacTSoggetto, dtos, soggettoModPags, idSogg, associatoA, now,null);
		return listaModPags;
	}

	/**
	 * modalitaPagamentoEntityToModalitaPagamentoCessioniModel
	 * 
	 * @param dtos
	 * @param soggettoModPags
	 * @param idSogg
	 * @param associatoA
	 * @param now
	 * @return
	 */
	private List<ModalitaPagamentoSoggetto> modalitaPagamentoEntityToModalitaPagamentoCessioniModel(
			SiacTSoggettoFin siacTSoggetto, List<SiacTModpagFin> dtos,
			List<ModalitaPagamentoSoggetto> soggettoModPags, Integer idSogg, String associatoA,
			Timestamp now,OttimizzazioneSoggettoDto ottimizzazioneSoggetto){
		
		List<ModalitaPagamentoSoggetto> modPagListDef = new ArrayList<ModalitaPagamentoSoggetto>();

		for (ModalitaPagamentoSoggetto it : soggettoModPags){
			
			ModalitaPagamentoSoggetto modPagDef = new ModalitaPagamentoSoggetto();

			int idIterato = it.getUid();
			for (SiacTModpagFin itsiac : dtos){
				
				int idConfronto = itsiac.getModpagId();
				if (idIterato == idConfronto){
					
					SiacRSoggettoRelazFin siacRSoggettoRelaz = null;
					if(ottimizzazioneSoggetto!=null){
						//RAMO OTTIMIZZATO
						List<SiacRSoggettoRelazFin> filtratiBySoggEModPag = ottimizzazioneSoggetto.filtraSiacRSoggettoRelaz1BySoggettoIdAndModPag(idSogg, itsiac);
						if(filtratiBySoggEModPag!=null && filtratiBySoggEModPag.size()>0){
							siacRSoggettoRelaz = filtratiBySoggEModPag.get(0);
						}
					} else {
						//RAMO CLASSICO 
						siacRSoggettoRelaz = siacRSoggettoRelazRepository.findValidaBySoggettoEModpag(idSogg, itsiac.getUid(), now);
					}
					
					if (siacRSoggettoRelaz != null){
						modPagDef.setUid(siacRSoggettoRelaz.getUid());
						modPagDef.setDataCreazione(siacRSoggettoRelaz.getDataCreazione());
						modPagDef.setLoginCreazione(siacRSoggettoRelaz.getLoginOperazione());

						if (siacRSoggettoRelaz.getDataModifica() != null){
							modPagDef.setDataModifica(siacRSoggettoRelaz.getDataModifica());
						}
						if (siacRSoggettoRelaz.getDataFineValidita() != null){
							// Se data fine validita valorizzata:
							modPagDef.setDataFineValidita(siacRSoggettoRelaz.getDataFineValidita());
							modPagDef.setDataScadenza(siacRSoggettoRelaz.getDataFineValidita());
						}
						
						// setto altri campi di mod pag:
						
						//CESSIONE DEL CREDITO, SOGGETTO CESSIONE:
						modPagDef.setCessioneCodModPag(String.valueOf(itsiac.getUid()));
						modPagDef.setCessioneCodSoggetto(itsiac.getSiacTSoggetto().getSoggettoCode());
						modPagDef.getSoggettoCessione().setDenominazione(itsiac.getSiacTSoggetto().getSoggettoDesc());
						modPagDef.getSoggettoCessione().setCodiceSoggetto(itsiac.getSiacTSoggetto().getSoggettoCode());
						//SIAC-6261
						modPagDef.getSoggettoCessione().setDataFineValiditaDurc(itsiac.getSiacTSoggetto().getDataFineValiditaDurc());
						
						//CESSIONE DEL CREDITO, SOGGETTO QUIETENZANTE:
						modPagDef.setSoggettoQuietanzante(itsiac.getQuietanziante());
						modPagDef.setCodiceFiscaleQuietanzante(itsiac.getQuietanzianteCodiceFiscale());
						modPagDef.setDataNascitaQuietanzante(TimingUtils.convertiDataIn_GgMmYyyy(itsiac.getQuietanzanteNascitaData()));
						modPagDef.setLuogoNascitaQuietanzante(itsiac.getQuietanzianteNascitaLuogo());
						modPagDef.setStatoNascitaQuietanzante(itsiac.getQuietanzianteNascitaStato());
						//
						
						
						
						SiacDRelazTipoFin siacDRelazTipo = siacRSoggettoRelaz.getSiacDRelazTipo();
						
						modPagDef.setTipoAccredito(TipoAccredito.valueOf(siacDRelazTipo.getRelazTipoCode()));
						
						modPagDef.setModalitaAccreditoSoggetto(new ModalitaAccreditoSoggetto(siacDRelazTipo.getRelazTipoCode(), siacDRelazTipo.getRelazTipoDesc()));

						
						
						SiacRSoggettoRelazStatoFin relazStato = null;
						if(ottimizzazioneSoggetto!=null){
							//RAMO OTTIMIZZATO
							relazStato =  ottimizzazioneSoggetto.getSiacRSoggettoRelazStatoFinBySiacRSoggettoRelazFin(siacRSoggettoRelaz);
						} else {
							//RAMO CLASSICO 
							relazStato = siacRSoggettoRelazStatoRepository.findBySoggettoRelazId(siacRSoggettoRelaz.getUid());
						}
						
						modPagDef.setDescrizioneStatoModalitaPagamento(relazStato.getSiacDRelazStato().getRelazStatoCode());

						// Inserisco la modpag associata

						// STATO MODALITA' PAGAMENTO
						
						List<SiacRModpagStatoFin> listaRModPagStato = null;
						if(ottimizzazioneSoggetto!=null){
							//RAMO OTTIMIZZATO
							listaRModPagStato = ottimizzazioneSoggetto.filtraSiacRModpagStatoFinBySiacTModpagFin(itsiac);
						} else {
							//RAMO CLASSICO 
							listaRModPagStato = itsiac.getSiacRModpagStatos();
						}
						
						
						if (listaRModPagStato != null && listaRModPagStato.size() > 0)
						{
							SiacRModpagStatoFin trovatoValido = null;
							for (SiacRModpagStatoFin rModPagStato : listaRModPagStato)
							{

								// da verificare la data fine validita a null
								if (rModPagStato != null
										&& rModPagStato.getDataFineValidita() == null)
								{
									trovatoValido = rModPagStato;
									break;
								}
							}
							if (trovatoValido != null)
							{
								it.setIdStatoModalitaPagamento(trovatoValido.getSiacDModpagStato()
										.getModpagStatoId());
								it.setCodiceStatoModalitaPagamento(trovatoValido
										.getSiacDModpagStato().getModpagStatoCode());
								it.setDescrizioneStatoModalitaPagamento(trovatoValido
										.getSiacDModpagStato().getModpagStatoDesc());
							}
						}

						SiacRSoggrelModpagFin siacRSoggrelModpag = null;
						if(ottimizzazioneSoggetto!=null){
							//RAMO OTTIMIZZATO
							List<SiacRSoggrelModpagFin> siacRSoggrelModpagFinByRelaz = ottimizzazioneSoggetto.filtraSiacRSoggrelModpagFinBySiacRSoggettoRelazFin(siacRSoggettoRelaz);
							if(siacRSoggrelModpagFinByRelaz!=null && siacRSoggrelModpagFinByRelaz.size()>0){
								siacRSoggrelModpag = siacRSoggrelModpagFinByRelaz.get(0);
							}
						} else {
							//RAMO CLASSICO 
							siacRSoggrelModpag = siacRSoggrelModpagRepository.findValidiBySoggettoRelaz(siacRSoggettoRelaz.getUid());
						}
						
						if (siacRSoggrelModpag.getNote() != null)
						{
							modPagDef.setNote(siacRSoggrelModpag.getNote());
						}
						if (siacRSoggrelModpag.getDataFineValidita() != null)
						{
							// se data fine validita valorizzata
							modPagDef.setDataFineValidita(siacRSoggrelModpag.getDataFineValidita());
							modPagDef.setDataScadenza(siacRSoggettoRelaz.getDataFineValidita());
						}
						if (siacRSoggrelModpag.getLoginModifica() != null)
						{
							modPagDef.setLoginUltimaModifica(siacRSoggrelModpag.getLoginModifica());
						}

						modPagDef.setModalitaPagamentoSoggettoCessione2(it);
						modPagDef.setAssociatoA(associatoA);

					}

					if (siacTSoggetto != null){
						
						
						List<SiacRModpagOrdineFin> rModPagOrdines = null;
						if(ottimizzazioneSoggetto!=null){
							//RAMO OTTIMIZZATO
							rModPagOrdines = ottimizzazioneSoggetto.filtraSiacRModpagOrdineFinBySiacTSoggettoFin(siacTSoggetto);
						} else {
							//RAMO CLASSICO 
							rModPagOrdines = siacTSoggetto.getSiacRModpagOrdines();
						}
						
						if (rModPagOrdines==null || rModPagOrdines.isEmpty()){
							SiacRSoggettoRelazFin siacRSoggettoRelaz2 = siacRSoggettoRelaz.getSiacTSoggetto1().getSiacRSoggettoRelazs2().get(0);
							if ("SEDE_SECONDARIA".equals(siacRSoggettoRelaz2.getSiacDRelazTipo().getRelazTipoCode())){
								String codiceModPagCes = getCodiceModalitaPagamentoCessione(siacRSoggettoRelaz.getUid(), siacRSoggettoRelaz2.getSiacTSoggetto1().getSiacRModpagOrdines());
								modPagDef.setCodiceModalitaPagamento(codiceModPagCes);
							}
						} else {
							String codiceModPagCes = getCodiceModalitaPagamentoCessione(siacRSoggettoRelaz.getUid(), siacTSoggetto.getSiacRModpagOrdines());
							modPagDef.setCodiceModalitaPagamento(codiceModPagCes);
						}
							
					}
							
					
					// codice progressivo MDP e' un po' piu' complesso che le
					// modalita di pagamento classiche
//					if (siacTSoggetto != null)
//					{
//						boolean trovato = false;
//						List<SiacRSoggrelModpagFin> lisRSoggRMp = itsiac.getSiacRSoggrelModpags();
//						if (lisRSoggRMp != null && lisRSoggRMp.size() > 0)
//						{
//							for (int n = 0; n < lisRSoggRMp.size() && !trovato; n++)
//							{
//								SiacRSoggrelModpagFin rSRMpIt = lisRSoggRMp.get(n);
//								List<SiacRModpagOrdineFin> lSiacRMPO = rSRMpIt.getSiacRModpagOrdines();
//								lSiacRMPO = DatiOperazioneUtils.soloValidi(lSiacRMPO, null);
//								if (lSiacRMPO != null && lSiacRMPO.size() > 0)
//								{
//									for (int i = 0; i < lSiacRMPO.size() && !trovato; i++)
//									{
//										SiacRModpagOrdineFin lSiacRMPOIt = lSiacRMPO.get(i);
//										if (lSiacRMPOIt.getSiacTSoggetto().getUid() == siacTSoggetto
//												.getUid())
//										{
//											modPagDef.setCodiceModalitaPagamento(String
//													.valueOf(lSiacRMPOIt.getOrdine()));
//											trovato = true;
//										}
//									}
//								}
//							}
//						}
//					}

					// codice progressivo MDP
					// if(siacTSoggetto!=null){
					// if(!siacTSoggetto.getSiacRModpagOrdines().isEmpty()){
					// for (SiacRModpagOrdineFin rModpagOrdine :
					// siacTSoggetto.getSiacRModpagOrdines()) {
					// if(rModpagOrdine.getDataCancellazione()==null &&
					// rModpagOrdine.getSiacTModpag()==null){
					// if(itsiac.getSiacTSoggetto().getUid()==rModpagOrdine.getSiacTSoggetto().getUid()){
					//
					// modPagDef.setCodiceStatoModalitaPagamento(String.valueOf(rModpagOrdine.getOrdine()));
					// }
					// }
					// }
					// }
					// }

					break;
				}
			}
			
			modPagListDef.add(modPagDef);
		}

		// Termino restituendo l'oggetto di ritorno:
		return modPagListDef;
	}

	private String getCodiceModalitaPagamentoCessione(Integer soggRelazId, List<SiacRModpagOrdineFin> siacRModpagOrdines)
	{
		for (SiacRModpagOrdineFin siacRModpagOrdine : siacRModpagOrdines)
			if (siacRModpagOrdine.getSiacTModpag() == null)
			{
				SiacRSoggrelModpagFin siacRSoggrelModpag = siacRModpagOrdine.getSiacRSoggrelModpag();

				if (siacRSoggrelModpag != null)
					if (siacRSoggrelModpag.getSiacRSoggettoRelaz().getUid().equals(soggRelazId))
						return String.valueOf(siacRModpagOrdine.getOrdine());
			}

		return null;
	}

	
	private ModalitaPagamentoSoggetto modalitaPagamentoSingleEntityToModalitaPagamentoCessioniModelCore(SiacRSoggettoRelazFin siacRSoggettoRelaz
			,SiacTModpagFin dto,ModalitaPagamentoSoggetto soggettoModPag,String soggettoCode){
		
		ModalitaPagamentoSoggetto modPagDef = new ModalitaPagamentoSoggetto();
		
		modPagDef.setUid(siacRSoggettoRelaz.getUid());
		modPagDef.setDataCreazione(siacRSoggettoRelaz.getDataCreazione());
		modPagDef.setLoginCreazione(siacRSoggettoRelaz.getLoginOperazione());

		if (siacRSoggettoRelaz.getDataModifica() != null) {
			modPagDef.setDataModifica(siacRSoggettoRelaz.getDataModifica());
		}
		if (siacRSoggettoRelaz.getDataFineValidita() != null) {
			// se data fine validita valorizzata
			modPagDef.setDataFineValidita(siacRSoggettoRelaz.getDataFineValidita());
		}
		// setto altri campi di modPagDef:
		
		//CESSIONE DEL CREDITO, SOGGETTO CESSIONE:
		modPagDef.setCessioneCodModPag(String.valueOf(dto.getUid()));
		modPagDef.setCessioneCodSoggetto(dto.getSiacTSoggetto().getSoggettoCode());
		modPagDef.getSoggettoCessione().setDenominazione(dto.getSiacTSoggetto().getSoggettoDesc());
		modPagDef.getSoggettoCessione().setCodiceSoggetto(dto.getSiacTSoggetto().getSoggettoCode());
		//SIAC-6211
		modPagDef.getSoggettoCessione().setCodiceFiscale(dto.getSiacTSoggetto().getCodiceFiscale());
		modPagDef.getSoggettoCessione().setPartitaIva(dto.getSiacTSoggetto().getPartitaIva());
		
		//CESSIONE DEL CREDITO, SOGGETTO QUIETENZANTE:
		modPagDef.setSoggettoQuietanzante(dto.getQuietanziante());
		modPagDef.setCodiceFiscaleQuietanzante(dto.getQuietanzianteCodiceFiscale());
		modPagDef.setDataNascitaQuietanzante(TimingUtils.convertiDataIn_GgMmYyyy(dto.getQuietanzanteNascitaData()));
		modPagDef.setLuogoNascitaQuietanzante(dto.getQuietanzianteNascitaLuogo());
		modPagDef.setStatoNascitaQuietanzante(dto.getQuietanzianteNascitaStato());
		
		
		//TIPO ACCREDITO
		SiacDRelazTipoFin siacDRelazTipo = siacRSoggettoRelaz.getSiacDRelazTipo();
		modPagDef.setTipoAccredito(TipoAccredito.valueOf(siacDRelazTipo.getRelazTipoCode()));
		modPagDef.setModalitaAccreditoSoggetto(new ModalitaAccreditoSoggetto(siacDRelazTipo.getRelazTipoCode(), siacDRelazTipo.getRelazTipoDesc()));
		//
		

		SiacRSoggettoRelazStatoFin relazStato = siacRSoggettoRelazStatoRepository.findBySoggettoRelazId(siacRSoggettoRelaz.getUid());
		modPagDef.setDescrizioneStatoModalitaPagamento(relazStato.getSiacDRelazStato().getRelazStatoCode());
		

		// Inserisco la modpag associata

		// STATO MODALITA' PAGAMENTO
		List<SiacRModpagStatoFin> listaRModPagStato = dto.getSiacRModpagStatos();
		if (listaRModPagStato != null && listaRModPagStato.size() > 0) {
			SiacRModpagStatoFin trovatoValido = null;
			for (SiacRModpagStatoFin rModPagStato : listaRModPagStato) {

				// da verificare la data fine validita a null
				if (rModPagStato != null && rModPagStato.getDataFineValidita() == null){
					trovatoValido = rModPagStato;
					break;
				}
			}
			if (trovatoValido != null) {

				String statoCode = trovatoValido.getSiacDModpagStato().getModpagStatoCode();
				String statoDesc = trovatoValido.getSiacDModpagStato().getModpagStatoDesc();
				Integer statoId = trovatoValido.getSiacDModpagStato().getModpagStatoId();

				soggettoModPag.setIdStatoModalitaPagamento(statoId);
				soggettoModPag.setCodiceStatoModalitaPagamento(statoCode);
				soggettoModPag.setDescrizioneStatoModalitaPagamento(statoDesc);
			}
		}

		modPagDef.setModalitaPagamentoSoggettoCessione2(soggettoModPag);
		modPagDef.setAssociatoA(soggettoCode);

		SiacRSoggrelModpagFin siacRSoggrelModpag = siacRSoggrelModpagRepository.findValidiBySoggettoRelaz(siacRSoggettoRelaz.getUid());
		if (siacRSoggrelModpag.getNote() != null) {
			modPagDef.setNote(siacRSoggrelModpag.getNote());
		}
		if (siacRSoggrelModpag.getDataFineValidita() != null) {
			// se data fine validita valorizzata
			modPagDef.setDataFineValidita(siacRSoggrelModpag.getDataFineValidita());
		}
		if (siacRSoggrelModpag.getLoginModifica() != null) {
			modPagDef.setLoginUltimaModifica(siacRSoggrelModpag.getLoginModifica());
		}
		
		return modPagDef;
	}
	
	/**
	 * modalitaPagamentoSingleEntityToModalitaPagamentoCessioniModel
	 * 
	 * @param dto
	 * @param soggettoModPag
	 * @param soggettoCode
	 * @param enteId
	 * @param idSoggRelaz
	 * @return
	 */
	private ModalitaPagamentoSoggetto modalitaPagamentoSingleEntityToModalitaPagamentoCessioniModel(
			String codiceAmbito, SiacTModpagFin dto, ModalitaPagamentoSoggetto soggettoModPag,
			String soggettoCode, Integer enteId, Integer idSoggRelaz){

		ModalitaPagamentoSoggetto modPagDef = new ModalitaPagamentoSoggetto();
		
		Timestamp now = new Timestamp(System.currentTimeMillis());

		SiacTSoggettoFin siacTSoggettoDa = siacTSoggettoRepository.ricercaSoggettoNoSeSede(codiceAmbito, enteId, soggettoCode, Constanti.SEDE_SECONDARIA, getNow());

		SiacRSoggettoRelazFin siacRSoggettoRelaz = siacRSoggettoRelazRepository.findValidaBySoggettiEModpag(siacTSoggettoDa.getUid(), dto.getSiacTSoggetto().getUid(), dto.getUid(), now);
		if (siacRSoggettoRelaz != null) {
			
			modPagDef = modalitaPagamentoSingleEntityToModalitaPagamentoCessioniModelCore(siacRSoggettoRelaz, dto, soggettoModPag, soggettoCode);

		} else {
			// caso in cui sia una sede il mittente della cessione (soggettoDa)
			// -> cerco per id direttamente
			siacRSoggettoRelaz = siacRSoggettoRelazRepository.findOne(idSoggRelaz);
			if (siacRSoggettoRelaz != null) {
				modPagDef = modalitaPagamentoSingleEntityToModalitaPagamentoCessioniModelCore(siacRSoggettoRelaz, dto, soggettoModPag, soggettoCode);
			}
		}

		// Termino restituendo l'oggetto di ritorno:
		return modPagDef;
	}

	/**
	 * modalitaPagamentoSingleEntityToModalitaPagamentoCessioniModModel
	 * 
	 * @param dto
	 * @param soggettoModPag
	 * @param soggettoCode
	 * @param enteId
	 * @param idSoggRelaz
	 * @return
	 */
	private ModalitaPagamentoSoggetto modalitaPagamentoSingleEntityToModalitaPagamentoCessioniModModel(
			String codiceAmbito, SiacTModpagFin dto, ModalitaPagamentoSoggetto soggettoModPag,
			String soggettoCode, Integer enteId, Integer idSoggRelaz)
	{

		ModalitaPagamentoSoggetto modPagDef = new ModalitaPagamentoSoggetto();
		Timestamp now = new Timestamp(System.currentTimeMillis());

		SiacTSoggettoFin siacTSoggettoDa = siacTSoggettoRepository.ricercaSoggettoNoSeSede(
				codiceAmbito, enteId, soggettoCode, Constanti.SEDE_SECONDARIA, getNow());

		SiacRSoggettoRelazFin siacRSoggettoRelaz = siacRSoggettoRelazRepository
				.findValidaBySoggettoEModpag(siacTSoggettoDa.getUid(), dto.getUid(), now);
		if (siacRSoggettoRelaz != null)
		{

			SiacRSoggettoRelazModFin siacRSoggettoRelazMod = siacRSoggettoRelazModRepository
					.findValidaBySoggettoRelazId(siacRSoggettoRelaz.getUid());
			if (siacRSoggettoRelazMod != null)
			{

				modPagDef.setUid(siacRSoggettoRelazMod.getUid());
				modPagDef.setDataCreazione(siacRSoggettoRelazMod.getDataCreazione());
				modPagDef.setLoginCreazione(siacRSoggettoRelazMod.getLoginOperazione());

				if (siacRSoggettoRelazMod.getDataModifica() != null)
				{
					modPagDef.setDataModifica(siacRSoggettoRelazMod.getDataModifica());
				}
				if (siacRSoggettoRelazMod.getDataFineValidita() != null)
				{
					// se data fine validita valorizzata
					modPagDef.setDataFineValidita(siacRSoggettoRelazMod.getDataFineValidita());
				}// asd
				modPagDef.setCessioneCodModPag(String.valueOf(dto.getUid()));
				modPagDef.setCessioneCodSoggetto(dto.getSiacTSoggetto().getSoggettoCode());
				modPagDef.setTipoAccredito(TipoAccredito.valueOf(siacRSoggettoRelaz
						.getSiacDRelazTipo().getRelazTipoCode()));
				modPagDef.setModalitaAccreditoSoggetto(Constanti
						.componiModalitaAccreditoDalTipo(modPagDef.getTipoAccredito()));

				SiacRSoggettoRelazStatoFin relazStato = siacRSoggettoRelazStatoRepository
						.findBySoggettoRelazId(siacRSoggettoRelaz.getUid());
				modPagDef.setDescrizioneStatoModalitaPagamento(relazStato.getSiacDRelazStato()
						.getRelazStatoCode());

				// Inserisco la modpag associata

				// STATO MODALITA' PAGAMENTO
				List<SiacRModpagStatoFin> listaRModPagStato = dto.getSiacRModpagStatos();
				if (listaRModPagStato != null && listaRModPagStato.size() > 0)
				{
					SiacRModpagStatoFin trovatoValido = null;
					for (SiacRModpagStatoFin rModPagStato : listaRModPagStato)
					{

						// da verificare la data fine validita a null
						if (rModPagStato != null && rModPagStato.getDataFineValidita() == null)
						{
							// Se valido
							trovatoValido = rModPagStato;
							break;
						}
					}
					if (trovatoValido != null)
					{

						String statoCode = trovatoValido.getSiacDModpagStato().getModpagStatoCode();
						String statoDesc = trovatoValido.getSiacDModpagStato().getModpagStatoDesc();
						Integer statoId = trovatoValido.getSiacDModpagStato().getModpagStatoId();

						soggettoModPag.setIdStatoModalitaPagamento(statoId);
						soggettoModPag.setCodiceStatoModalitaPagamento(statoCode);
						soggettoModPag.setDescrizioneStatoModalitaPagamento(statoDesc);
					}
				}

				modPagDef.setModalitaPagamentoSoggettoCessione2(soggettoModPag);
				modPagDef.setAssociatoA(soggettoCode);
				modPagDef.setLoginCreazione(siacRSoggettoRelazMod.getLoginOperazione());
				modPagDef.setLoginModifica(siacRSoggettoRelazMod.getLoginOperazione());

				SiacRSoggrelModpagModFin siacRSoggrelModpagMod = siacRSoggrelModpagModRepository
						.findValidoBySoggRelazModId(siacRSoggettoRelazMod.getUid());
				if (siacRSoggrelModpagMod.getNote() != null)
				{
					modPagDef.setNote(siacRSoggrelModpagMod.getNote());
				}
				if (siacRSoggrelModpagMod.getDataFineValidita() != null)
				{
					// se data fine validita valorizzata
					modPagDef.setDataFineValidita(siacRSoggrelModpagMod.getDataFineValidita());
				}

			}
		}
		else
		{
			// caso in cui sia una sede il mittente della cessione (soggettoDa)
			// -> cerco per id direttamente
			siacRSoggettoRelaz = siacRSoggettoRelazRepository.findValidaBySoggettoEModpag(
					idSoggRelaz, dto.getUid(), now);
			if (siacRSoggettoRelaz != null)
			{

				SiacRSoggettoRelazModFin siacRSoggettoRelazMod = siacRSoggettoRelazModRepository
						.findValidaBySoggettoRelazId(siacRSoggettoRelaz.getUid());
				if (siacRSoggettoRelazMod != null)
				{

					modPagDef.setUid(siacRSoggettoRelazMod.getUid());
					modPagDef.setDataCreazione(siacRSoggettoRelazMod.getDataCreazione());
					modPagDef.setLoginCreazione(siacRSoggettoRelazMod.getLoginOperazione());

					if (siacRSoggettoRelazMod.getDataModifica() != null)
					{
						modPagDef.setDataModifica(siacRSoggettoRelazMod.getDataModifica());
					}
					if (siacRSoggettoRelazMod.getDataFineValidita() != null)
					{
						// se data fine validita valorizzata
						modPagDef.setDataFineValidita(siacRSoggettoRelazMod.getDataFineValidita());
					}// asd
					modPagDef.setCessioneCodModPag(String.valueOf(dto.getUid()));
					modPagDef.setCessioneCodSoggetto(dto.getSiacTSoggetto().getSoggettoCode());
					modPagDef.setTipoAccredito(TipoAccredito.valueOf(siacRSoggettoRelaz
							.getSiacDRelazTipo().getRelazTipoCode()));
					modPagDef.setModalitaAccreditoSoggetto(Constanti
							.componiModalitaAccreditoDalTipo(modPagDef.getTipoAccredito()));

					SiacRSoggettoRelazStatoFin relazStato = siacRSoggettoRelazStatoRepository
							.findBySoggettoRelazId(siacRSoggettoRelaz.getUid());
					modPagDef.setDescrizioneStatoModalitaPagamento(relazStato.getSiacDRelazStato()
							.getRelazStatoCode());

					// Inserisco la modpag associata

					// STATO MODALITA' PAGAMENTO
					List<SiacRModpagStatoFin> listaRModPagStato = dto.getSiacRModpagStatos();
					if (listaRModPagStato != null && listaRModPagStato.size() > 0)
					{
						SiacRModpagStatoFin trovatoValido = null;
						for (SiacRModpagStatoFin rModPagStato : listaRModPagStato)
						{

							// da verificare la data fine validita a null
							if (rModPagStato != null && rModPagStato.getDataFineValidita() == null)
							{
								// Se valido
								trovatoValido = rModPagStato;
								break;
							}
						}
						if (trovatoValido != null)
						{

							String statoCode = trovatoValido.getSiacDModpagStato()
									.getModpagStatoCode();
							String statoDesc = trovatoValido.getSiacDModpagStato()
									.getModpagStatoDesc();
							Integer statoId = trovatoValido.getSiacDModpagStato()
									.getModpagStatoId();

							soggettoModPag.setIdStatoModalitaPagamento(statoId);
							soggettoModPag.setCodiceStatoModalitaPagamento(statoCode);
							soggettoModPag.setDescrizioneStatoModalitaPagamento(statoDesc);
						}
					}

					modPagDef.setModalitaPagamentoSoggettoCessione2(soggettoModPag);
					modPagDef.setAssociatoA(soggettoCode);
					modPagDef.setLoginCreazione(siacRSoggettoRelazMod.getLoginOperazione());
					modPagDef.setLoginModifica(siacRSoggettoRelazMod.getLoginOperazione());

					SiacRSoggrelModpagModFin siacRSoggrelModpagMod = siacRSoggrelModpagModRepository
							.findValidoBySoggRelazModId(siacRSoggettoRelazMod.getUid());
					if (siacRSoggrelModpagMod.getNote() != null)
					{
						modPagDef.setNote(siacRSoggrelModpagMod.getNote());
					}
					if (siacRSoggrelModpagMod.getDataFineValidita() != null)
					{
						// se data fine validita valorizzata
						modPagDef.setDataFineValidita(siacRSoggrelModpagMod.getDataFineValidita());
					}

				}
			}
			else
			{
				// caso in cui sia una sede il mittente della cessione
				// (soggettoDa) -> cerco per id direttamente
				siacRSoggettoRelaz = siacRSoggettoRelazRepository.findOne(idSoggRelaz);
				if (siacRSoggettoRelaz != null)
				{

					SiacRSoggettoRelazModFin siacRSoggettoRelazMod = siacRSoggettoRelazModRepository
							.findValidaBySoggettoRelazId(siacRSoggettoRelaz.getUid());
					if (siacRSoggettoRelazMod != null)
					{

						modPagDef.setUid(siacRSoggettoRelazMod.getUid());
						modPagDef.setDataCreazione(siacRSoggettoRelazMod.getDataCreazione());
						modPagDef.setLoginCreazione(siacRSoggettoRelazMod.getLoginOperazione());

						if (siacRSoggettoRelazMod.getDataModifica() != null)
						{
							modPagDef.setDataModifica(siacRSoggettoRelazMod.getDataModifica());
						}
						if (siacRSoggettoRelazMod.getDataFineValidita() != null)
						{
							// se data fine validita valorizzata
							modPagDef.setDataFineValidita(siacRSoggettoRelazMod
									.getDataFineValidita());
						}
						// altri set:
						modPagDef.setCessioneCodModPag(String.valueOf(dto.getUid()));
						modPagDef.setCessioneCodSoggetto(dto.getSiacTSoggetto().getSoggettoCode());
						modPagDef.setTipoAccredito(TipoAccredito.valueOf(siacRSoggettoRelaz
								.getSiacDRelazTipo().getRelazTipoCode()));
						modPagDef.setModalitaAccreditoSoggetto(Constanti
								.componiModalitaAccreditoDalTipo(modPagDef.getTipoAccredito()));

						SiacRSoggettoRelazStatoFin relazStato = siacRSoggettoRelazStatoRepository
								.findBySoggettoRelazId(siacRSoggettoRelaz.getUid());
						modPagDef.setDescrizioneStatoModalitaPagamento(relazStato
								.getSiacDRelazStato().getRelazStatoCode());

						// Inserisco la modpag associata

						// STATO MODALITA' PAGAMENTO
						List<SiacRModpagStatoFin> listaRModPagStato = dto.getSiacRModpagStatos();
						if (listaRModPagStato != null && listaRModPagStato.size() > 0)
						{
							SiacRModpagStatoFin trovatoValido = null;
							for (SiacRModpagStatoFin rModPagStato : listaRModPagStato)
							{

								// da verificare la data fine validita a null
								if (rModPagStato != null
										&& rModPagStato.getDataFineValidita() == null)
								{
									// Se valido
									trovatoValido = rModPagStato;
									break;
								}
							}
							if (trovatoValido != null)
							{

								String statoCode = trovatoValido.getSiacDModpagStato()
										.getModpagStatoCode();
								String statoDesc = trovatoValido.getSiacDModpagStato()
										.getModpagStatoDesc();
								Integer statoId = trovatoValido.getSiacDModpagStato()
										.getModpagStatoId();

								soggettoModPag.setIdStatoModalitaPagamento(statoId);
								soggettoModPag.setCodiceStatoModalitaPagamento(statoCode);
								soggettoModPag.setDescrizioneStatoModalitaPagamento(statoDesc);
							}
						}

						modPagDef.setModalitaPagamentoSoggettoCessione2(soggettoModPag);
						modPagDef.setAssociatoA(soggettoCode);
						modPagDef.setLoginCreazione(siacRSoggettoRelazMod.getLoginOperazione());
						modPagDef.setLoginModifica(siacRSoggettoRelazMod.getLoginOperazione());

						SiacRSoggrelModpagModFin siacRSoggrelModpagMod = siacRSoggrelModpagModRepository
								.findValidoBySoggRelazModId(siacRSoggettoRelazMod.getUid());
						if (siacRSoggrelModpagMod.getNote() != null)
						{
							modPagDef.setNote(siacRSoggrelModpagMod.getNote());
						}
						if (siacRSoggrelModpagMod.getDataFineValidita() != null)
						{
							// se data fine validita valorizzata
							modPagDef.setDataFineValidita(siacRSoggrelModpagMod
									.getDataFineValidita());
						}

					}
				}
			}
		}

		// Termino restituendo l'oggetto di ritorno:
		return modPagDef;
	}
	
	/**
	 * Wrapper di retro compatibilita'
	 * @param codEnte
	 * @param idSogg
	 * @param isDecentrato
	 * @param isIncludeModif
	 * @return
	 */
	public List<SedeSecondariaSoggetto> ricercaSediSecondarie(Integer codEnte, Integer idSogg,
			boolean isDecentrato, boolean isIncludeModif,DatiOperazioneDto datiOperazioneDto){
		return ricercaSediSecondarie(codEnte, idSogg, isDecentrato, isIncludeModif,datiOperazioneDto, null);
	}

	/**
	 * ricercaSediSecondarie
	 * 
	 * @param codEnte
	 * @param idSogg
	 * @param isDecentrato
	 * @param isIncludeModif
	 * @return
	 */
	public List<SedeSecondariaSoggetto> ricercaSediSecondarie(Integer codEnte, Integer idSogg,
			boolean isDecentrato, boolean isIncludeModif,DatiOperazioneDto datiOperazioneDto,OttimizzazioneSoggettoDto ottimizzazioneSoggetto){
		
		Map<Integer, Boolean> mappaSediModificate = null;
		List<SedeSecondariaSoggetto> listaTrovata = null;
		
		List<SiacTSoggettoFin> listaSediSecondarie = null;
				
		if(ottimizzazioneSoggetto!=null){
			//RAMO OTTIMIZZATO
			listaSediSecondarie = ottimizzazioneSoggetto.estraiSediSecondarie(idSogg);
		} else {
			//RAMO CLASSICO
			listaSediSecondarie = siacTSoggettoRepository.ricercaSedi(codEnte,idSogg, Constanti.SEDE_SECONDARIA);
		}

		if ((listaSediSecondarie != null) && !listaSediSecondarie.isEmpty())
		{
			// Elimino le sedi eliminate o annullate
			List<SiacTSoggettoFin> listaSediSecondarieNonEliminate = new ArrayList<SiacTSoggettoFin>();
			for (SiacTSoggettoFin siacTSoggetto : listaSediSecondarie)
			{
				if (siacTSoggetto.getDataFineValidita() == null)
				{
					listaSediSecondarieNonEliminate.add(siacTSoggetto);
				}
			}
			listaSediSecondarie = listaSediSecondarieNonEliminate;

			//
			// - Se isDecentrato == false : vengono restituiti i dati estratti
			// dalla tabella siac_t_soggetto
			//
			// - Se isDecentrato == true : se presenti e validi, verranno
			// restituiti i dati letti dalla tabella siac_t_soggetto_mod
			// se non presenti, verranno restituiti i dati letti dalla tabella
			// siac_t_soggetto
			//
			// Modifica per jira-686
			//
			// - Se isIncludeModif == false : non sovrascrivo lo stato
			//
			// - Se isIncludeModif == true : sovrascrivo lo stato forzandolo a
			// IN_MODIFICA
			//

			if (isDecentrato && isIncludeModif == true)
			{
				mappaSediModificate = new HashMap<Integer, Boolean>();
				Timestamp now = new Timestamp(System.currentTimeMillis());
				for (int i = 0; i < listaSediSecondarie.size(); i++)
				{
					boolean isMod = false;
					SiacTSoggettoFin sedeIterata = listaSediSecondarie.get(i);
					List<SiacTSoggettoModFin> mods = siacTSoggettoModRepository
							.findValidoBySoggettoId(sedeIterata.getSoggettoId(), now);
					if (mods != null && mods.size() > 0)
					{

						SiacTSoggettoModFin sedeMod = mods.get(0);
						List<SiacRSoggettoStatoFin> backupStato = sedeIterata.getSiacRSoggettoStatos();
						sedeIterata = EntityToEntityConverter.siacTSoggettoModToSiacTSoggetto(
								sedeMod, null);
						sedeIterata.setSiacRSoggettoStatos(backupStato);
						sedeIterata.getSiacTIndirizzoSoggettos().get(0)
								.setPrincipale(Constanti.TRUE);

						listaSediSecondarie.set(i, sedeIterata);
						isMod = true;
					}
					mappaSediModificate.put(sedeIterata.getUid(), isMod);
				}
			}

			listaTrovata = convertiLista(listaSediSecondarie, SedeSecondariaSoggetto.class,
					FinMapId.SiacTSoggetto_SedeSecondariaSoggetto);

			listaTrovata = EntityToModelConverter.soggettoEntityToSedeSecondariaSoggettoModel(
					listaSediSecondarie, listaTrovata, mappaSediModificate, isIncludeModif,ottimizzazioneSoggetto);
			
			//OTTOBRE 2016 introduco anche il caricamenteo delle mdp  (solo da scenario ottimizzato)
			//MODALITA' PAGAMENTO DELLE SEDI SECONDARIE
			if(ottimizzazioneSoggetto!=null){
				// (solo da scenario ottimizzato)
				for (SedeSecondariaSoggetto sedeIterata : listaTrovata){
					//ricerca modalita di pagamento
					SiacTSoggettoFin sedeEntityIt = CommonUtils.getByIdSiacTBase(listaSediSecondarie, sedeIterata.getUid());
					if(sedeEntityIt!=null){
						List<ModalitaPagamentoSoggetto> listaModPagSedeIterata = ricercaModalitaPagamentoOPT(Constanti.AMBITO_FIN,sedeIterata.getUid(), 
								sedeIterata.getDenominazione(), false,ottimizzazioneSoggetto, datiOperazioneDto,sedeEntityIt);
						sedeIterata.setModalitaPagamentoSoggettos(listaModPagSedeIterata);
					}
				}
			}
			//
			
		}

		// Termino restituendo l'oggetto di ritorno:
		return listaTrovata;
	}

	/**
	 * controlloTipoSogg
	 * 
	 * @param codice
	 * @return
	 */
	private boolean controlloTipoSogg(String codice)
	{
		if (!(codice.equalsIgnoreCase("PGI") || codice.equalsIgnoreCase("PG")))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * inserisciSoggetto
	 * 
	 * @param soggettoInput
	 * @param richiedente
	 * @param ente
	 * @param inserisciSoggettoProvvisorio
	 * @return
	 */
	public Soggetto inserisciSoggetto(Soggetto soggettoInput, Richiedente richiedente, Ente ente,
			boolean inserisciSoggettoProvvisorio, DatiOperazioneDto datiOperazione,long nuovoCode)
	{

		long currMillisec = getCurrentMilliseconds();   
		Date dateInserimento = new Date(currMillisec);
		Timestamp timestampInserimento = null;
		Operazione operazione = Operazione.INSERIMENTO;

		if (controlloTipoSogg(soggettoInput.getTipoSoggetto().getCodice()))
		{
			soggettoInput.setNome(soggettoInput.getNome().toUpperCase());
			soggettoInput.setCognome(soggettoInput.getCognome().toUpperCase());
		}
		int idEnte = ente.getUid();

		// prendo ambito da livello service
		SiacDAmbitoFin siacDAmbitoPerCode = datiOperazione.getSiacDAmbito();
		Integer idAmbito = siacDAmbitoPerCode.getAmbitoId();

		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository
				.findOne(idEnte);

		DatiOperazioneDto datiOperazioneDto = new DatiOperazioneDto(currMillisec, operazione,
				siacTEnteProprietario, richiedente.getAccount().getId());

		SiacTSoggettoFin siacTSoggetto = new SiacTSoggettoFin();

		if (!StringUtils.isEmpty(soggettoInput.getPartitaIva()))
		{
			String partitaIva = soggettoInput.getPartitaIva().trim().toUpperCase();
			siacTSoggetto.setPartitaIva(partitaIva);
		}
		if (!StringUtils.isEmpty(soggettoInput.getCodiceFiscale()))
		{
			String codFisc = soggettoInput.getCodiceFiscale().trim().toUpperCase();
			siacTSoggetto.setCodiceFiscale(codFisc);
		}
		if (!StringUtils.isEmpty(soggettoInput.getCodiceFiscaleEstero()))
		{
			String codFiscEst = soggettoInput.getCodiceFiscaleEstero().trim().toUpperCase();
			siacTSoggetto.setCodiceFiscaleEstero(codFiscEst);
		}
		
		siacTSoggetto.setDataFineValiditaDurc(soggettoInput.getDataFineValiditaDurc());
		siacTSoggetto.setTipoFonteDurc(soggettoInput.getTipoFonteDurc());
		siacTSoggetto.setNoteDurc(soggettoInput.getNoteDurc());
		
		siacTSoggetto.setFonteDurc(soggettoInput.getFonteDurcClassifId() != null ?
				new SiacTClassFin(soggettoInput.getFonteDurcClassifId())
				: null );
		
		// LOGIN INS/MDF DURC - SIAC-6874
		if (siacTSoggetto.getDataFineValiditaDurc() != null || 
			siacTSoggetto.getTipoFonteDurc() != null || 
			siacTSoggetto.getFonteDurc() != null || 
			org.apache.commons.lang.StringUtils.isNotBlank(siacTSoggetto.getNoteDurc())) {
		
			String loginOperazione = DatiOperazioneUtils.determinaUtenteLogin(datiOperazione,siacTAccountRepository);
			
			siacTSoggetto.setLoginInserimentoDurc(loginOperazione );
			siacTSoggetto.setLoginModificaDurc(loginOperazione);
		}
		
		
		
		//SIAC-6565-CR12115	
		if (!StringUtils.isEmpty(soggettoInput.getCanalePA()))
		{
			siacTSoggetto.setCanalePA(soggettoInput.getCanalePA());
		}
		
		if (!StringUtils.isEmpty(soggettoInput.getCodDestinatario()))
		{
			siacTSoggetto.setCodDestinatario(soggettoInput.getCodDestinatario().trim().toUpperCase());
		}
		if (!StringUtils.isEmpty(soggettoInput.getEmailPec()))
		{

			siacTSoggetto.setEmailPec(soggettoInput.getEmailPec().trim().toUpperCase());
		}
		
		if (soggettoInput.getFonteDurcClassifId() != null) {
			siacTSoggetto.setFonteDurc(new SiacTClassFin(soggettoInput.getFonteDurcClassifId()));
		}

		String codiceTipo = soggettoInput.getTipoSoggetto().getCodice();

		boolean isPersonaFisica = false;
		if (codiceTipo.trim().toUpperCase().startsWith(Constanti.PERSONA_FISICA))
		{
			isPersonaFisica = true;
		}

		SiacTPersonaFisicaFin personaFisica = new SiacTPersonaFisicaFin();
		SiacTPersonaGiuridicaFin personaGiuridica = new SiacTPersonaGiuridicaFin();

		//CODICE DEL SOGGETTO:
		siacTSoggetto.setSoggettoCode(Long.toString(nuovoCode));
		//

		String soggettoDesc = buildSoggettoDesc(soggettoInput, isPersonaFisica);
		siacTSoggetto.setSoggettoDesc(soggettoDesc);
		// Controllo qual'e' la data da valorizzare in base al tipo di
		// inserimento
		if (inserisciSoggettoProvvisorio)
		{
			// :Data provvisorio - utilizziamo per ora dataModifica
			siacTSoggetto.setDataModifica(dateInserimento);
		}
		else
		{
			siacTSoggetto.setDataInizioValidita(dateInserimento);
		}
		// becchiamo l'ambito:
		SiacDAmbitoFin siacDAmbito = siacDAmbitoRepository.findOne(idAmbito);
		siacTSoggetto.setSiacDAmbito(siacDAmbito);
		// imposta param dati operazione
		siacTSoggetto = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTSoggetto,
				datiOperazioneDto, siacTAccountRepository);
		

		
		siacTSoggetto.setDataFineValiditaDurc(soggettoInput.getDataFineValiditaDurc());
		siacTSoggetto.setTipoFonteDurc(soggettoInput.getTipoFonteDurc());
		siacTSoggetto.setNoteDurc(soggettoInput.getNoteDurc());
		
		siacTSoggetto.setFonteDurc(soggettoInput.getFonteDurcClassifId() != null ?
				new SiacTClassFin(soggettoInput.getFonteDurcClassifId())
				: null );
		
		// salvo sul db:
		siacTSoggetto = siacTSoggettoRepository.saveAndFlush(siacTSoggetto);
		
		if (isPersonaFisica)
		{
			// PERSONA FISICA
			timestampInserimento = TimingUtils.convertiDataInTimeStamp(soggettoInput
					.getDataNascita());
			personaFisica.setNascitaData(timestampInserimento);
			savePersonaFisica(soggettoInput, personaFisica, timestampInserimento, siacTSoggetto,
					datiOperazioneDto, true);
		}
		else
		{
			// PERSONA GIURIDICA
			savePersonaGiuridica(soggettoInput, personaGiuridica, siacTSoggetto, datiOperazioneDto);
		}

		// stato:
		StatoOperativoAnagrafica statoOperativoAnagrafica;
		if (inserisciSoggettoProvvisorio)
		{
			statoOperativoAnagrafica = StatoOperativoAnagrafica.PROVVISORIO;
		}
		else
		{
			statoOperativoAnagrafica = StatoOperativoAnagrafica.VALIDO;
		}
		if (statoOperativoAnagrafica != null)
		{
			salvaStatoOperativoAnagrafica(statoOperativoAnagrafica, siacTSoggetto, idEnte,
					datiOperazioneDto);
		}

		// natura giuridica:
		NaturaGiuridicaSoggetto naturaGiuridicaSogg = soggettoInput.getNaturaGiuridicaSoggetto();
		if (naturaGiuridicaSogg != null
				&& !StringUtils.isEmpty(naturaGiuridicaSogg.getSoggettoTipoCode()))
		{
			String codeFg = naturaGiuridicaSogg.getSoggettoTipoCode();
			SiacTFormaGiuridicaFin siacTFormaGiuridica = siacTFormaGiuridicaRepository
					.findValidaByCode(idEnte, codeFg, datiOperazioneDto.getTs()).get(0);
			saveRFormGiuridica(siacTFormaGiuridica, siacTSoggetto, datiOperazioneDto);
		}

		// classe:
		String[] classificazioni = soggettoInput.getTipoClassificazioneSoggettoId();
		if (classificazioni != null && classificazioni.length > 0)
		{
			for (String codeClass : classificazioni)
			{
				if (!StringUtils.isEmpty(codeClass))
				{
					salvaSoggettoClasse(siacTSoggetto, idEnte, idAmbito, codeClass,
							datiOperazioneDto);
				}
			}
		}

		// indirizzi
		List<IndirizzoSoggetto> listaIndirizzi = soggettoInput.getIndirizzi();
		if (listaIndirizzi != null && listaIndirizzi.size() > 0)
		{
			for (IndirizzoSoggetto indirizzoIterato : listaIndirizzi)
			{
				saveIndirizzo(indirizzoIterato, siacTSoggetto, idEnte, datiOperazioneDto);
			}
		}

		// recapiti:
		List<Contatto> listaContatti = soggettoInput.getContatti();
		if (listaContatti != null && listaContatti.size() > 0)
		{
			for (Contatto contattoIterato : listaContatti)
			{
				salvaContatto(contattoIterato, siacTSoggetto, idEnte, datiOperazioneDto, null);
			}
		}

		// ONERI:
		String[] oneriCodes = soggettoInput.getTipoOnereId();
		if (oneriCodes != null && oneriCodes.length > 0)
		{
			for (String codeOnere : oneriCodes)
			{
				salvaOnere(siacTSoggetto, idEnte, codeOnere, datiOperazioneDto);

			}
		}

		// Soggetto tipo:
		TipoSoggetto tipoSoggetto = soggettoInput.getTipoSoggetto();
		if (tipoSoggetto != null)
		{
			saveSoggettoTipo(tipoSoggetto, idEnte, siacTSoggetto, datiOperazioneDto);
		}

		// Inserimeto delle NOTE relative al soggetto
		SiacRSoggettoAttrFin siacRSoggettoAttr = new SiacRSoggettoAttrFin();
		siacRSoggettoAttr.setSiacTSoggetto(siacTSoggetto);

		// NOTE:
		AttributoTClassInfoDto attributoInfo = new AttributoTClassInfoDto();
		attributoInfo.setSiacTSoggetto(siacTSoggetto);
		attributoInfo.setTipoOggetto(OggettoDellAttributoTClass.T_SOGGETTO);
		salvaAttributoTAttr(attributoInfo, datiOperazioneDto, soggettoInput.getNote(),
				Constanti.T_ATTR_CODE_NOTE_SOGGETTO);

		// Matricola soggetto
		AttributoTClassInfoDto attributoInfoMatricola = new AttributoTClassInfoDto();
		attributoInfoMatricola.setSiacTSoggetto(siacTSoggetto);
		attributoInfoMatricola.setTipoOggetto(OggettoDellAttributoTClass.T_SOGGETTO);
		salvaAttributoTAttr(attributoInfoMatricola, datiOperazioneDto,
				soggettoInput.getMatricola(), Constanti.T_ATTR_CODE_MATRICOLA);

		// RITORNO RISULTATI:
		Soggetto soggetto = new Soggetto();
		soggetto.setCodiceSoggetto(siacTSoggetto.getSoggettoCode());
		soggetto = EntityToModelConverter.soggettoEntityToSoggettoModel(siacTSoggetto, soggetto);
		
		soggetto.setUid(siacTSoggetto.getUid());
		
		soggetto.setStatoOperativo(statoOperativoAnagrafica);

		//entityManager.flush();

		super.flushAndClearEntMng();
		
		// Termino restituendo l'oggetto di ritorno:
		return soggetto;
	}

	/**
	 * inserisciSedeSecondaria
	 * 
	 * @param sede
	 * @param datiOperazioneDto
	 * @param idEnte
	 * @param soggettoDopoModifica
	 * @param richiedente
	 * @param isDecentrato
	 * @return
	 */
	public SiacRSoggettoRelazFin inserisciSedeSecondaria(SedeSecondariaSoggetto sede,
			DatiOperazioneDto datiOperazioneDto, int idEnte, SiacTSoggettoFin soggettoDopoModifica,
			Richiedente richiedente, boolean isDecentrato)
	{

		SiacDAmbitoFin siacDAmbitoPerCode = datiOperazioneDto.getSiacDAmbito();
		Integer idAmbito = siacDAmbitoPerCode.getAmbitoId();

		SiacTSoggettoFin siacTSoggetto = new SiacTSoggettoFin();

		siacTSoggetto.setSoggettoCode(soggettoDopoModifica.getSoggettoCode());

		String soggettoDesc = sede.getDenominazione();

		siacTSoggetto.setSoggettoDesc(soggettoDesc);
		// becchiamo l'ambito:
		SiacDAmbitoFin siacDAmbito = siacDAmbitoRepository.findOne(idAmbito);
		siacTSoggetto.setSiacDAmbito(siacDAmbito);
		// //////////////
		siacTSoggetto = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTSoggetto,
				datiOperazioneDto, siacTAccountRepository);
		// salvo sul db:
		siacTSoggetto = siacTSoggettoRepository.saveAndFlush(siacTSoggetto);

		SiacRSoggettoRelazFin siacRSoggettoRelaz = new SiacRSoggettoRelazFin();
		siacRSoggettoRelaz = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRSoggettoRelaz,
				datiOperazioneDto, siacTAccountRepository);
		SiacDRelazTipoFin tipoRelaz = siacDRelazTipoRepository.findRelazione(
				Constanti.SEDE_SECONDARIA, idEnte, datiOperazioneDto.getTs()).get(0);
		siacRSoggettoRelaz.setSiacDRelazTipo(tipoRelaz);
		siacRSoggettoRelaz.setSiacTSoggetto1(soggettoDopoModifica);
		siacRSoggettoRelaz.setSiacTSoggetto2(siacTSoggetto);
		// salvo sul db:
		siacRSoggettoRelaz = siacRSoggettoRelazRepository.saveAndFlush(siacRSoggettoRelaz);

		entityRefresh(siacRSoggettoRelaz);

		// stato:
		StatoOperativoAnagrafica statoOperativoAnagrafica = StatoOperativoAnagrafica.VALIDO;
		if (isDecentrato)
		{
			statoOperativoAnagrafica = StatoOperativoAnagrafica.PROVVISORIO;
		}
		SiacRSoggettoStatoFin siacRSoggettoStato = salvaStatoOperativoAnagrafica(
				statoOperativoAnagrafica, siacTSoggetto, idEnte, datiOperazioneDto);
		siacTSoggetto.setSiacRSoggettoStatos(toList(siacRSoggettoStato));
		// ////////////////////////////////

		// indirizzi
		ArrayList<IndirizzoSoggetto> listaIndirizzi = new ArrayList<IndirizzoSoggetto>();
		IndirizzoSoggetto indirizzo = sede.getIndirizzoSoggettoPrincipale();
		if (indirizzo != null)
		{
			indirizzo.setIdTipoIndirizzo(Constanti.SEDE_LEGALE);
			indirizzo.setPrincipale(Constanti.TRUE);
			listaIndirizzi.add(indirizzo);
			if (listaIndirizzi != null && listaIndirizzi.size() > 0)
			{
				for (IndirizzoSoggetto indirizzoIterato : listaIndirizzi)
				{
					SiacTIndirizzoSoggettoFin indirizzoSalvato = saveIndirizzo(indirizzoIterato,
							siacTSoggetto, idEnte, datiOperazioneDto);
					entityRefresh(indirizzoSalvato);
				}
			}
		}

		// recapiti:
		List<Contatto> listaContatti = sede.getContatti();
		if (listaContatti != null && listaContatti.size() > 0)
		{
			for (Contatto contattoIterato : listaContatti)
			{
				salvaContatto(contattoIterato, siacTSoggetto, idEnte, datiOperazioneDto, null);
			}
		}

		entityRefresh(siacTSoggetto);
		// RITORNO RISULTATI:
		Soggetto soggetto = new Soggetto();
		soggetto.setCodiceSoggetto(siacTSoggetto.getSoggettoCode());
		soggetto = EntityToModelConverter.soggettoEntityToSoggettoModel(siacTSoggetto, soggetto);
		soggetto.setStatoOperativo(statoOperativoAnagrafica);

		// Termino restituendo l'oggetto di ritorno:
		return siacRSoggettoRelaz;
	}

	/**
	 * aggiornaSedeSecondaria
	 * 
	 * @param sede
	 * @param datiOperazioneDto
	 * @param idEnte
	 * @param loginOperazione
	 * @return
	 */
	public SiacTSoggettoFin aggiornaSedeSecondaria(SedeSecondariaSoggetto sede,
			DatiOperazioneDto datiOperazioneDto, int idEnte, String loginOperazione)
	{

		SiacDAmbitoFin siacDAmbitoPerCode = datiOperazioneDto.getSiacDAmbito();
		Integer idAmbito = siacDAmbitoPerCode.getAmbitoId();

		SiacTSoggettoFin siacTSoggetto = new SiacTSoggettoFin();

		siacTSoggetto.setSoggettoCode(sede.getCodiceSedeSecondaria());

		String soggettoDesc = sede.getDenominazione();

		siacTSoggetto.setSoggettoDesc(soggettoDesc);
		// becchiamo l'ambito:
		SiacDAmbitoFin siacDAmbito = siacDAmbitoRepository.findOne(idAmbito);
		siacTSoggetto.setSiacDAmbito(siacDAmbito);
		// //////////////
		siacTSoggetto = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTSoggetto,
				datiOperazioneDto, siacTAccountRepository);
		siacTSoggetto.setSoggettoId(sede.getUid());
		// salvo sul db:
		siacTSoggetto = siacTSoggettoRepository.saveAndFlush(siacTSoggetto);

		// stato:
		// va vambiato lo stato
		StatoOperativoAnagrafica statoOperativoAnagrafica = StatoOperativoAnagrafica.VALIDO;
		if (sede.getStatoOperativoSedeSecondaria().name()
				.equalsIgnoreCase(StatoOperativoSedeSecondaria.ANNULLATO.name()))
		{
			statoOperativoAnagrafica = StatoOperativoAnagrafica.ANNULLATO;
		}

		if (sede.getStatoOperativoSedeSecondaria().name()
				.equalsIgnoreCase(StatoOperativoSedeSecondaria.PROVVISORIO.name()))
		{
			statoOperativoAnagrafica = StatoOperativoAnagrafica.PROVVISORIO;
		}

		SiacRSoggettoStatoFin siacRSoggettoStato = salvaStatoOperativoAnagrafica(
				statoOperativoAnagrafica, siacTSoggetto, idEnte, datiOperazioneDto);
		siacTSoggetto.setSiacRSoggettoStatos(toList(siacRSoggettoStato));

		// ////////////////////////////////

		// indirizzi
		ArrayList<IndirizzoSoggetto> listaIndirizzi = new ArrayList<IndirizzoSoggetto>();
		IndirizzoSoggetto indirizzo = sede.getIndirizzoSoggettoPrincipale();
		if (indirizzo != null)
		{
			indirizzo.setPrincipale(Constanti.TRUE);
			indirizzo.setIdTipoIndirizzo(Constanti.SEDE_LEGALE);
			listaIndirizzi.add(indirizzo);
			aggiornamentoIndirizzi(listaIndirizzi, siacTSoggetto, datiOperazioneDto);
		}

		// recapiti:
		List<Contatto> listaContatti = sede.getContatti();
		aggiornaContatti(listaContatti, datiOperazioneDto, siacTSoggetto, idEnte);

		// RITORNO RISULTATI:
		Soggetto soggetto = new Soggetto();
		soggetto.setCodiceSoggetto(siacTSoggetto.getSoggettoCode());
		soggetto = EntityToModelConverter.soggettoEntityToSoggettoModel(siacTSoggetto, soggetto);
		// Termino restituendo l'oggetto di ritorno:
		return siacTSoggetto;
	}

	/**
	 * Ritorna l'eventuale soggetto in modifica del dato soggetto
	 * 
	 * @param idSoggetto
	 * @return
	 */
	private SiacTSoggettoModFin findSoggettoModBySoggetto(Integer idSoggetto)
	{
		List<SiacTSoggettoModFin> siacTSoggettoModList = siacTSoggettoModRepository
				.findValidoBySoggettoId(idSoggetto, getNow());
		SiacTSoggettoModFin mod = CommonUtils.getFirst(siacTSoggettoModList);
		// Termino restituendo l'oggetto di ritorno:
		return mod;
	}

	public SiacTSoggettoModFin aggiornaSedeSecondariaMod(SedeSecondariaSoggetto sede,
			DatiOperazioneDto datiOperazioneDto, int idEnte, String loginOperazione,
			Richiedente richiedente)
	{

		long currMillisec = System.currentTimeMillis();
		Timestamp timestampInserimento = new Timestamp(currMillisec);

		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository
				.findOne(idEnte);

		DatiOperazioneDto datiOperazioneModifica = new DatiOperazioneDto(currMillisec,
				Operazione.MODIFICA, siacTEnteProprietario, richiedente.getAccount().getId());

		SiacTSoggettoFin siacTSoggettoOriginale = siacTSoggettoRepository.findOne(sede.getUid());

		SiacTSoggettoModFin siacTSoggettoMod = null;
		List<SiacTSoggettoModFin> siacTSoggettoModList = siacTSoggettoModRepository
				.findValidoBySoggettoId(sede.getUid(), timestampInserimento);
		if (siacTSoggettoModList != null && siacTSoggettoModList.size() > 0)
		{
			siacTSoggettoMod = siacTSoggettoModList.get(0);
		}
		else
		{
			siacTSoggettoMod = EntityToEntityConverter
					.siacTSoggettoToSiacTSoggettoMod(siacTSoggettoOriginale);
		}

		// 06-febbraio-2014: commento il salvataggio dello stato perche' e' la
		// presenza del record in "_mod" a renderlo in modifica

		siacTSoggettoMod.setSoggettoDesc(sede.getDenominazione());

		siacTSoggettoMod = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTSoggettoMod,
				datiOperazioneDto, siacTAccountRepository);
		// salvo sul db:
		siacTSoggettoMod = siacTSoggettoModRepository.saveAndFlush(siacTSoggettoMod);

		// Indirizzi
		ArrayList<IndirizzoSoggetto> listaIndirizzi = new ArrayList<IndirizzoSoggetto>();
		IndirizzoSoggetto indirizzo = sede.getIndirizzoSoggettoPrincipale();
		if (indirizzo != null)
		{
			indirizzo.setPrincipale(Constanti.TRUE);
			indirizzo.setIdTipoIndirizzo(Constanti.SEDE_LEGALE);
			//
			// Jira-627
			// non presentava i dati corretti e aggiunta la patch del id=0
			// cosicche' venga sempre interpretato come un nuovo indirizzo
			indirizzo.setIndirizzoId(0);
			listaIndirizzi.add(indirizzo);
			aggiornamentoIndirizziMod(listaIndirizzi, siacTSoggettoMod, datiOperazioneDto);
		}

		List<Contatto> listaContatti = sede.getContatti();
		aggiornaContattiMod(listaContatti, datiOperazioneModifica, siacTSoggettoMod, idEnte);

		// Ritorno risultati:
		Soggetto soggetto = new Soggetto();
		soggetto.setCodiceSoggetto(siacTSoggettoMod.getSoggettoCode());
		soggetto = EntityToModelConverter.soggettoEntityModToSoggettoModel(siacTSoggettoMod,
				soggetto);
		// Termino restituendo l'oggetto di ritorno:
		return siacTSoggettoMod;
	}

	/**
	 * buildSoggettoDesc
	 * 
	 * @param soggettoInput
	 * @param isPersonaFisica
	 * @return
	 */
	private String buildSoggettoDesc(Soggetto soggettoInput, boolean isPersonaFisica)
	{
		String soggettoDesc = "";
		// nome + cognome or denominazione azienda
		if (isPersonaFisica)
		{
			soggettoDesc = soggettoInput.getCognome() + " " + soggettoInput.getNome();
		}
		else
		{
			soggettoDesc = soggettoInput.getDenominazione();
		}
		// Termino restituendo l'oggetto di ritorno:
		return soggettoDesc;
	}

	/**
	 * ricercaSoggetti
	 * 
	 * @param prs
	 * @param idEnte
	 * @param codiceAmbito
	 *            TODO
	 * @return
	 * @throws RuntimeException
	 */
	public List<Soggetto> ricercaSoggetti(ParametroRicercaSoggetto prs, Integer idEnte,
			String codiceAmbito) throws RuntimeException
	{
		List<Soggetto> soggettos = new ArrayList<Soggetto>();

		List<SiacTSoggettoFin> dtos = new ArrayList<SiacTSoggettoFin>();
		List<SiacTSoggettoFin> controlloCanc = new ArrayList<SiacTSoggettoFin>();

		// viene cambiato oggetto perche' i model non possono essere passati ai
		// DaoImpl (specifica CSI)
		RicercaSoggettoParamDto paramSearch = (RicercaSoggettoParamDto) map(prs,
				RicercaSoggettoParamDto.class); 
		    

		dtos = soggettoDao.ricercaSoggetti(idEnte, paramSearch, codiceAmbito);

		for (int i = 0; i < dtos.size(); i++)
		{
			if (dtos.get(i).getDataCancellazione() == null)
			{
				controlloCanc.add(dtos.get(i));
			}
		}

		if (controlloCanc != null && controlloCanc.size() > 0)
		{
			soggettos = convertiLista(controlloCanc, Soggetto.class,
					FinMapId.SiacTSoggetto_Soggetto);

			if (prs.isIncludeModif())
			{
				// estrae lo stato artificiosamente messo a in modifica
				soggettos = EntityToModelConverter.soggettoEntityToSoggettoModel(controlloCanc,
						soggettos, true);

			}
			else
			{
				// estra lo stato reale del soggetto
				soggettos = EntityToModelConverter.soggettoEntityToSoggettoModel(controlloCanc,
						soggettos, false);
			}

			for (Soggetto sogg : soggettos)
			{
				if (prs.getMatricola() != null)
					sogg.setMatricola(prs.getMatricola());

				// TIPI DI LEGAME TRA SOGGETTI

				sogg.setElencoSoggettiSuccessivi(new ArrayList<Soggetto>());
				sogg.setIdsSoggettiSuccessivi(new ArrayList<Integer>());
				List<Integer> idsLegamiSoggettiSuccessivi = new ArrayList<Integer>();

				if (sogg.getIdLegamiSoggettiSuccessivi() != null) {
					Collections.sort(sogg.getIdLegamiSoggettiSuccessivi());

					for (Integer idLegameSogg : sogg.getIdLegamiSoggettiSuccessivi()) {
						SiacRSoggettoRelazFin relaz = siacRSoggettoRelazRepository.findOne(idLegameSogg);

						if (isLegameSoggetto(relaz.getSiacDRelazTipo().getRelazTipoCode())) {
							SiacTSoggettoFin siacTSoggetto = siacTSoggettoRepository
									.findOne(relaz.getSiacTSoggetto2().getUid());
							Soggetto soggetto = map(siacTSoggetto, Soggetto.class, FinMapId.SiacTSoggetto_Soggetto);
							soggetto = EntityToModelConverter.soggettoEntityToSoggettoModel(siacTSoggetto,
									soggetto);
							soggetto.setTipoLegame(relaz.getSiacDRelazTipo().getRelazTipoDesc());

							sogg.getElencoSoggettiSuccessivi().add(soggetto);
							sogg.getIdsSoggettiSuccessivi().add(soggetto.getUid());
							idsLegamiSoggettiSuccessivi.add(idLegameSogg);
							
							break;
						}
					}
				}


				sogg.setElencoSoggettiPrecedenti(new ArrayList<Soggetto>());
				sogg.setIdsSoggettiPrecedenti(new ArrayList<Integer>());
				List<Integer> idsLegamiSoggettiPrecedenti = new ArrayList<Integer>();

				if (sogg.getIdLegamiSoggettiPrecedenti() != null) {
					Collections.sort(sogg.getIdLegamiSoggettiPrecedenti());
					for (Integer idLegameSogg : sogg.getIdLegamiSoggettiPrecedenti()) {

						SiacRSoggettoRelazFin relaz = siacRSoggettoRelazRepository.findOne(idLegameSogg);

						if (isLegameSoggetto(relaz.getSiacDRelazTipo().getRelazTipoCode())) {
							SiacTSoggettoFin siacTSoggetto = siacTSoggettoRepository
									.findOne(relaz.getSiacTSoggetto1().getUid());
							if (siacTSoggetto.getDataCancellazione() == null) {
								Soggetto soggetto = map(siacTSoggetto, Soggetto.class,
										FinMapId.SiacTSoggetto_Soggetto);
								soggetto = EntityToModelConverter.soggettoEntityToSoggettoModel(siacTSoggetto,
										soggetto);
								soggetto.setTipoLegame(relaz.getSiacDRelazTipo().getRelazTipoDesc());
								
								sogg.getElencoSoggettiPrecedenti().add(soggetto);
								sogg.getIdsSoggettiPrecedenti().add(soggetto.getUid());
								idsLegamiSoggettiPrecedenti.add(idLegameSogg);
							}
						}
					}

				}
			}
		}

		// Termino restituendo l'oggetto di ritorno:
		return soggettos;
	}

	
	private boolean isLegameSoggetto(String tipoRelazCode) {
		return !("SEDE_SECONDARIA".equalsIgnoreCase(tipoRelazCode) || 
				"CSI".equalsIgnoreCase(tipoRelazCode) || 
				"CSC".equalsIgnoreCase(tipoRelazCode));
	}

	/**
	 * ricercaSoggetti
	 * 
	 * @param prs
	 * @param idEnte
	 * @param codiceAmbito
	 *            TODO
	 * @return
	 * @throws RuntimeException
	 */
	public EsitoRicercaSoggettiDto ricercaSoggettiOttimizzato(ParametroRicercaSoggetto prs, Integer idEnte,
			String codiceAmbito, DatiOperazioneDto datiOperazione, Integer numPagina, Integer dimPagina) throws RuntimeException{
		
		EsitoRicercaSoggettiDto esito = new EsitoRicercaSoggettiDto();
		
		List<Soggetto> soggettos = new ArrayList<Soggetto>();
		
		List<Soggetto> soggettiDaRelazioni = new ArrayList<Soggetto>();

		List<SiacTSoggettoFin> dtos = new ArrayList<SiacTSoggettoFin>();
		List<SiacTSoggettoFin> escludendoICancellati = new ArrayList<SiacTSoggettoFin>();

		// viene cambiato oggetto perche' i model non possono essere passati ai
		// DaoImpl (specifica CSI)
		RicercaSoggettoParamDto paramSearch = (RicercaSoggettoParamDto) map(prs,RicercaSoggettoParamDto.class); 
		    
		dtos = soggettoDao.ricercaSoggettiOttimizzato(idEnte, paramSearch, codiceAmbito);

		for (int i = 0; i < dtos.size(); i++){
			if (dtos.get(i).getDataCancellazione() == null){
				escludendoICancellati.add(dtos.get(i));
			}
		}
		
		List<SiacTSoggettoFin> paginaRichiesta = null;
		if(numPagina!=null && dimPagina!=null){
			//richiesta paginazione
			paginaRichiesta = getPaginata(escludendoICancellati, numPagina, dimPagina);
			int numeroDiPagine = StringUtils.calcolaNumeroDiPagine(escludendoICancellati.size(), dimPagina);
			esito.setDimensionePagine(dimPagina);
			esito.setNumeroPaginaRestituita(numPagina);
			esito.setNumeroTotalePagine(numeroDiPagine);
			esito.setNumeroTotaleSoggetti(escludendoICancellati.size());
		} else {
			//no paginazione
			paginaRichiesta = escludendoICancellati;
			esito.setNumeroTotaleSoggetti(escludendoICancellati.size());
		}
		

		if (paginaRichiesta != null && paginaRichiesta.size() > 0){
			
			OttimizzazioneSoggettoDto ottimizzazioneSoggettoDto = caricaDatiOttimizzazioneRicercaSoggettoByDistintiSoggetti(paginaRichiesta);
			List<SiacTSoggettoFin> distintiSiacTSoggettiCoinvolti = ottimizzazioneSoggettoDto.getDistintiSiacTSoggettiCoinvolti();
			
			soggettos = ricercaSoggettoOPT(distintiSiacTSoggettiCoinvolti, true, false,ottimizzazioneSoggettoDto,datiOperazione);
			
			//  distintiSiacTSoggettiCoinvolti contiene anche soggetti che non c'entrano con quanto restituito dal dao (perche'????)
			soggettos = filtraSoggettiNonRichiesti(soggettos, dtos); 

			if (prs.isIncludeModif()){
				// estrae lo stato artificiosamente messo a in modifica
				soggettos = EntityToModelConverter.soggettoEntityToSoggettoModelOPT(paginaRichiesta,soggettos, true,ottimizzazioneSoggettoDto);
			} else {
				// estra lo stato reale del soggetto
				soggettos = EntityToModelConverter.soggettoEntityToSoggettoModelOPT(paginaRichiesta,soggettos, false,ottimizzazioneSoggettoDto);
			}
			
			//SOGGETTI DA RELAZIONI:
			List<SiacTSoggettoFin> siactTSoggDaRelaz = ottimizzazioneSoggettoDto.getDistintiSiacTSoggettiCoinvoltiTramiteRelazioni();
			OttimizzazioneSoggettoDto ottimizzazioneSoggettiRelazDto = caricaDatiOttimizzazioneRicercaSoggettoByDistintiSoggetti(siactTSoggDaRelaz);
			soggettiDaRelazioni = ricercaSoggettoOPT(siactTSoggDaRelaz, true, false, ottimizzazioneSoggettiRelazDto,datiOperazione);
			soggettiDaRelazioni = EntityToModelConverter.soggettoEntityToSoggettoModelOPT(siactTSoggDaRelaz,soggettiDaRelazioni, false,ottimizzazioneSoggettiRelazDto);
			//
			
			// ESTRAZIONE DEI TIPI DI LEGAME CHE POSSONO ESSERE UTILIZZATI
			// NELLA CREAZIONE DI LEGAMI TRA SOGGETTI
			List<SiacDRelazTipoFin> elencoLegamiValidi = siacDRelazTipoRepository.findRelazTipoByEnte(idEnte);
			// FINE ESTRAZIONE DEI TIPI DI LEGAME CHE POSSONO ESSERE
			// UTILIZZATI NELLA CREAZIONE DI LEGAMI TRA SOGGETTI

			for (Soggetto sogg : soggettos){
				
				if (prs.getMatricola() != null){
					sogg.setMatricola(prs.getMatricola());
				}

				List<Integer> idsLegamiSoggettiSuccessivi = sogg.getIdLegamiSoggettiSuccessivi();
				List<Integer> idsLegamiSoggettiPrecedenti = sogg.getIdLegamiSoggettiPrecedenti();

				if (idsLegamiSoggettiSuccessivi != null && idsLegamiSoggettiSuccessivi.size() > 0){
					Collections.sort(idsLegamiSoggettiSuccessivi);
				}

				if (idsLegamiSoggettiPrecedenti != null && idsLegamiSoggettiPrecedenti.size() > 0){
					Collections.sort(idsLegamiSoggettiPrecedenti);
				}

				List<Soggetto> soggettosSuccessivi = new ArrayList<Soggetto>();
				List<Soggetto> soggettosPrecedenti = new ArrayList<Soggetto>();
				

				if (null != idsLegamiSoggettiSuccessivi && idsLegamiSoggettiSuccessivi.size() > 0){
					for (int i = 0; i < idsLegamiSoggettiSuccessivi.size(); i++){
						SiacRSoggettoRelazFin relaz = ottimizzazioneSoggettoDto.findSiacRSoggettoRelazFinByID(idsLegamiSoggettiSuccessivi.get(i));
						for (SiacDRelazTipoFin siacDRelazTipoValida : elencoLegamiValidi){
							if (relaz.getSiacDRelazTipo().getRelazTipoCode().equalsIgnoreCase(siacDRelazTipoValida.getRelazTipoCode())){
								Soggetto soggetto = CommonUtils.getById(soggettiDaRelazioni, relaz.getSiacTSoggetto2().getUid());
								soggetto.setTipoLegame(siacDRelazTipoValida.getRelazTipoDesc());
								soggettosSuccessivi.add(soggetto);
							}
						}
					}
					if (null != soggettosSuccessivi && soggettosSuccessivi.size() > 0){
						sogg.setElencoSoggettiSuccessivi(soggettosSuccessivi);
					}
				}

				if (null != idsLegamiSoggettiPrecedenti && idsLegamiSoggettiPrecedenti.size() > 0){
					for (int i = 0; i < idsLegamiSoggettiPrecedenti.size(); i++){
						SiacRSoggettoRelazFin relaz = ottimizzazioneSoggettoDto.findSiacRSoggettoRelazFinByID(idsLegamiSoggettiPrecedenti.get(i));
						for (SiacDRelazTipoFin siacDRelazTipoValida : elencoLegamiValidi){
							if (relaz.getSiacDRelazTipo().getRelazTipoCode().equalsIgnoreCase(siacDRelazTipoValida.getRelazTipoCode())){
								SiacTSoggettoFin siacTSoggetto = ottimizzazioneSoggettiRelazDto.findSiacTSoggettoFinByID(relaz.getSiacTSoggetto1().getUid());
								if (siacTSoggetto.getDataCancellazione() == null){
									Soggetto soggetto = CommonUtils.getById(soggettiDaRelazioni, relaz.getSiacTSoggetto1().getUid());
									soggetto.setTipoLegame(siacDRelazTipoValida.getRelazTipoDesc());
									soggettosPrecedenti.add(soggetto);
								}
								
								
							}
						}
					}

					if (null != soggettosPrecedenti && soggettosPrecedenti.size() > 0){
						sogg.setElencoSoggettiPrecedenti(soggettosPrecedenti);
					}
				}
				
			}
		}

		// Termino restituendo l'oggetto di ritorno:
		
		esito.setPaginaRichiesta(soggettos);
		
		return esito;
	}
	

	private List<Soggetto> filtraSoggettiNonRichiesti(List<Soggetto> soggettos, List<SiacTSoggettoFin> dtos)
	{
		Set<Integer> dtosSet = new HashSet<Integer>();
		
		for (SiacTSoggettoFin dto : dtos)
			dtosSet.add(dto.getUid());
					
		List<Soggetto> list = new ArrayList<Soggetto>();
		
		for (Soggetto soggetto : soggettos)
			if (dtosSet.contains(soggetto.getUid()))
				list.add(soggetto);
							
		return list;
	}

	/**
	 * valutaOperazioneValidaSoggetto
	 * 
	 * @param statoIndicato
	 * @param idQuery
	 * @return
	 */
	public OperazioneValidaSoggetto valutaOperazioneValidaSoggetto(
			StatoOperativoAnagrafica statoIndicato, Integer idQuery)
	{
		OperazioneValidaSoggetto operazioneAggiornaSoggetto = OperazioneValidaSoggetto.UNDEFINED;

		long currMillisec = System.currentTimeMillis();
		Date dateInserimento = new Date(currMillisec);
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);

		List<SiacRSoggettoStatoFin> statoOldL = siacRSoggettoStatoRepository.findValidoByIdSoggetto(
				idQuery, timestampInserimento);

		if (statoOldL != null && statoOldL.size() > 0)
		{
			SiacRSoggettoStatoFin statoOld = statoOldL.get(0);
			if (statoOld != null)
			{
				String stato = statoOld.getSiacDSoggettoStato().getSoggettoStatoCode();
				if (StatoOperativoAnagrafica.VALIDO.equals(statoIndicato))
				{

					// se e' valido non mi basta devo verificare che in
					// realta' non ci siano dei record sulle mod, questo dovuto
					// al fatto
					// che e' stato introdotto lo stato Virtuale IN_MODIFICA

					SiacTSoggettoFin soggettoVerificaStato = siacTSoggettoRepository.findOne(idQuery);

					if (null != soggettoVerificaStato.getSiacTSoggettoMods()
							&& soggettoVerificaStato.getSiacTSoggettoMods().size() > 0)
					{

						for (SiacTSoggettoModFin soggettoMod : soggettoVerificaStato
								.getSiacTSoggettoMods())
						{

							if (soggettoMod.getDataFineValidita() == null)
							{
								// Se valido
								operazioneAggiornaSoggetto = OperazioneValidaSoggetto.VALIDA_DA_IN_MODIFICA;
							}
						}

					}

					if (StatoOperativoAnagrafica.IN_MODIFICA.name().equals(stato))
					{
						operazioneAggiornaSoggetto = OperazioneValidaSoggetto.VALIDA_DA_IN_MODIFICA;
					}
					else if (StatoOperativoAnagrafica.BLOCCATO.name().equals(stato))
					{
						operazioneAggiornaSoggetto = OperazioneValidaSoggetto.VALIDA_DA_BLOCCATO;
					}
					else if (StatoOperativoAnagrafica.SOSPESO.name().equals(stato))
					{
						operazioneAggiornaSoggetto = OperazioneValidaSoggetto.VALIDA_DA_SOSPESO;
					}
					else if (StatoOperativoAnagrafica.PROVVISORIO.name().equals(stato))
					{
						operazioneAggiornaSoggetto = OperazioneValidaSoggetto.VALIDA_DA_PROVVISORIO;
					}
				}
			}
		}
		// Termino restituendo l'oggetto di ritorno:
		return operazioneAggiornaSoggetto;
	}

	/**
	 * deleteFisicaTabelleSoggettoMod
	 * 
	 * @param siacTSoggettoOriginale
	 */
	private void deleteFisicaTabelleSoggettoMod(SiacTSoggettoFin siacTSoggettoOriginale)
	{
		Integer idSoggetto = siacTSoggettoOriginale.getSoggettoId();
		List<SiacTSoggettoModFin> siacTSoggettosMod = siacTSoggettoModRepository
				.findAllBySoggettoId(idSoggetto);
		siacTPersonaFisicaModRepository.deleteAllBySoggettoId(idSoggetto);
		siacTPersonaFisicaModRepository.flush();

		siacTPersonaGiuridicaModRepository.deleteAllBySoggettoId(idSoggetto);
		siacTPersonaGiuridicaModRepository.flush();

		siacTRecapitoSoggettoModRepository.deleteAllBySoggettoId(idSoggetto);
		siacTRecapitoSoggettoModRepository.flush();

		siacTModpagModRepository.deleteAllBySoggettoId(idSoggetto);
		siacTModpagModRepository.flush();

		for (SiacTSoggettoModFin it : siacTSoggettosMod)
		{
			List<SiacTIndirizzoSoggettoModFin> siacTIndirizzoSoggettoModL = it
					.getSiacTIndirizzoSoggettoMods();
			for (SiacTIndirizzoSoggettoModFin siacTIndirizzoSoggettoMod : siacTIndirizzoSoggettoModL)
			{
				siacRIndirizzoSoggettoTipoModRepository
						.deleteAllByIndirizzoModId(siacTIndirizzoSoggettoMod.getIndirizzoModId());
			}
		}
		siacRIndirizzoSoggettoTipoModRepository.flush();

		siacTIndirizzoSoggettoModRepository.deleteAllBySoggettoId(idSoggetto);
		siacTIndirizzoSoggettoModRepository.flush();

		for (SiacTSoggettoModFin it : siacTSoggettosMod)
		{
			Integer idSoggMod = it.getSogModId();
			siacRSoggrelModpagModRepository.deleteAllBySoggModId(idSoggMod);
			siacRSoggettoRelazModRepository.deleteAllBySoggModId(idSoggMod);
		}
		siacRSoggrelModpagModRepository.flush();
		siacRSoggettoRelazModRepository.flush();

		siacRSoggettoOnereModRepository.deleteAllBySoggettoId(idSoggetto);
		siacRSoggettoOnereModRepository.flush();

		siacRSoggettoClasseModRepository.deleteAllBySoggettoId(idSoggetto);
		siacRSoggettoClasseModRepository.flush();

		siacRSoggettoAttrModRepository.deleteAllBySoggettoId(idSoggetto);
		siacRSoggettoAttrModRepository.flush();

		for (SiacTSoggettoModFin it : siacTSoggettosMod)
		{
			siacTSoggettoModRepository.delete(it);
		}
		siacTSoggettoModRepository.flush();
		entityRefresh(siacTSoggettoOriginale);
	}

	/**
	 * Metodo che si occupa di pulire le tabelle _mod di Modpag
	 * 
	 * @param req
	 */
	public void annullaModalitaPagamentoInModifica(ModalitaPagamentoSoggetto modPagModToDelete,
			Soggetto soggettoDaModificare, Richiedente richiedente, Ente ente)
	{
		long currMillisec = System.currentTimeMillis();

		int idEnte = ente.getUid();
		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository
				.findOne(idEnte);

		Operazione operazioneModifica = Operazione.MODIFICA;
		Operazione operazioneCancellazioneLogica = Operazione.CANCELLAZIONE_LOGICA_RECORD;

		DatiOperazioneDto datiOperazioneModifica = new DatiOperazioneDto(currMillisec,
				operazioneModifica, siacTEnteProprietario, richiedente.getAccount().getId());
		DatiOperazioneDto datiOperazioneCancellazioneLogica = new DatiOperazioneDto(currMillisec,
				operazioneCancellazioneLogica, siacTEnteProprietario, richiedente.getAccount()
						.getId());

		// SETTARE T_modpag stato in valido ed eliminare record in t_modpag_mod
		// Cancello il record in t_modpag_mod
		SiacTModpagModFin siacTModpagModToDelete = siacTModpagModRepository.findValidoByModpagId(
				modPagModToDelete.getUid(), datiOperazioneCancellazioneLogica.getTs());
		if (siacTModpagModToDelete != null)
		{
			siacTModpagModRepository.delete(siacTModpagModToDelete);
			siacTModpagModRepository.flush();
		}

		if (siacTModpagModToDelete == null)
		{

			SiacRSoggettoRelazModFin siacRSoggettoRelazMod = siacRSoggettoRelazModRepository
					.findValidaBySoggettoRelazId(modPagModToDelete.getUid());
			if (siacRSoggettoRelazMod != null)
			{
				SiacRSoggrelModpagModFin siacRSoggrelModpagMod = siacRSoggrelModpagModRepository
						.findValidoBySoggRelazModId(siacRSoggettoRelazMod.getUid());

				siacRSoggrelModpagModRepository.delete(siacRSoggrelModpagMod);
				siacRSoggrelModpagModRepository.flush();
				siacRSoggettoRelazModRepository.delete(siacRSoggettoRelazMod);
				siacRSoggettoRelazModRepository.flush();

				SiacRSoggettoRelazFin siacRSoggettoRelaz = siacRSoggettoRelazRepository
						.findOne(modPagModToDelete.getUid());

				if (siacRSoggettoRelaz != null)
				{
					SiacRSoggettoRelazStatoFin siacRSoggettoRelazStato = siacRSoggettoRelazStatoRepository
							.findBySoggettoRelazId(modPagModToDelete.getUid());
					SiacDRelazStatoFin siacDRelazStato = siacDRelazStatoRepository
							.findDRelazStatoValidoByCode(idEnte, Constanti.STATO_VALIDO).get(0);

					siacRSoggettoRelazStato = DatiOperazioneUtils
							.impostaDatiOperazioneLogin(siacRSoggettoRelazStato,
									datiOperazioneModifica, siacTAccountRepository);
					siacRSoggettoRelazStato.setSiacTEnteProprietario(siacTEnteProprietario);
					siacRSoggettoRelazStato.setSiacDRelazStato(siacDRelazStato);
					siacRSoggettoRelazStato.setSiacRSoggettoRelaz(siacRSoggettoRelaz);
					siacRSoggettoRelazStato.setDataCreazione(siacRSoggettoRelazStato
							.getDataCreazione());
					siacRSoggettoRelaz.setDataModifica(new Date());
					// salvo sul db:
					siacRSoggettoRelazStatoRepository.saveAndFlush(siacRSoggettoRelazStato);
				}

			}

		}

		// Setto lo stato di t_modpag da in_modifica a valido //Chiedere a
		// cristian conferma
		SiacTModpagFin siacTModpagToReverse = siacTModpagRepository
				.findOne(modPagModToDelete.getUid());

		if (siacTModpagToReverse != null)
		{

			SiacRModpagStatoFin siacRModpagStatoToReverse = siacRModpagStatoRepository
					.findStatoValidoByMdpId(modPagModToDelete.getUid()).get(0);
			siacRModpagStatoToReverse = DatiOperazioneUtils.impostaDatiOperazioneLogin(
					siacRModpagStatoToReverse, datiOperazioneModifica, siacTAccountRepository);
			siacRModpagStatoToReverse.setSiacTModpag(siacTModpagToReverse);

			siacRModpagStatoToReverse.setDataModifica(new Date());
			siacRModpagStatoToReverse
					.setDataCreazione(siacRModpagStatoToReverse.getDataCreazione());

			SiacDModpagStatoFin siacDModpagStato = siacDModpagStatoRepository
					.findModPagStatoDValidoByCode(idEnte, Constanti.STATO_VALIDO,
							datiOperazioneModifica.getTs()).get(0);
			siacRModpagStatoToReverse.setSiacDModpagStato(siacDModpagStato);
			// salvo sul db:
			siacRModpagStatoRepository.saveAndFlush(siacRModpagStatoToReverse);

		}

	}

	/**
	 * deleteLogicaTabelleSoggettoMod
	 * 
	 * @param siacTSoggettoMod
	 * @param datiOperazioneCancellazioneLogica
	 */
	private void deleteLogicaTabelleSoggettoMod(SiacTSoggettoModFin siacTSoggettoMod,
			DatiOperazioneDto datiOperazioneCancellazioneLogica)
	{
		if (siacTSoggettoMod != null)
		{
			long currMillisec;

			List<SiacTPersonaFisicaModFin> siacTPersonaFisicaMods = siacTSoggettoMod
					.getSiacTPersonaFisicaMods();
			if (null != siacTPersonaFisicaMods && siacTPersonaFisicaMods.size() > 0)
			{
				for (SiacTPersonaFisicaModFin siacTPersonaFisicaMod : siacTPersonaFisicaMods)
				{
					if (null != siacTPersonaFisicaMod
							&& siacTPersonaFisicaMod.getDataFineValidita() == null)
					{
						// Se valido
						currMillisec = System.currentTimeMillis();
						datiOperazioneCancellazioneLogica.setCurrMillisec(currMillisec);
						siacTPersonaFisicaMod = DatiOperazioneUtils.impostaDatiOperazioneLogin(
								siacTPersonaFisicaMod, datiOperazioneCancellazioneLogica,
								siacTAccountRepository);
						// salvo sul db:
						siacTPersonaFisicaModRepository.saveAndFlush(siacTPersonaFisicaMod);
					}
				}
			}

			List<SiacTPersonaGiuridicaModFin> siacTPersonaGiuridicaMods = siacTSoggettoMod
					.getSiacTPersonaGiuridicaMods();
			if (null != siacTPersonaGiuridicaMods && siacTPersonaGiuridicaMods.size() > 0)
			{
				for (SiacTPersonaGiuridicaModFin siacTPersonaGiuridicaMod : siacTPersonaGiuridicaMods)
				{
					if (null != siacTPersonaGiuridicaMod
							&& siacTPersonaGiuridicaMod.getDataFineValidita() == null)
					{
						// Se valido
						currMillisec = System.currentTimeMillis();
						datiOperazioneCancellazioneLogica.setCurrMillisec(currMillisec);
						siacTPersonaGiuridicaMod = DatiOperazioneUtils.impostaDatiOperazioneLogin(
								siacTPersonaGiuridicaMod, datiOperazioneCancellazioneLogica,
								siacTAccountRepository);
						// salvo sul db:
						siacTPersonaGiuridicaModRepository.saveAndFlush(siacTPersonaGiuridicaMod);
					}
				}
			}

			// siac_t_recapito_soggetto_mod
			List<SiacTRecapitoSoggettoModFin> siacTRecapitosSoggettoMod = siacTSoggettoMod
					.getSiacTRecapitoSoggettoMods();
			if (siacTRecapitosSoggettoMod != null && siacTRecapitosSoggettoMod.size() > 0)
			{
				for (SiacTRecapitoSoggettoModFin siacTRecapitoSoggettoMod : siacTRecapitosSoggettoMod)
				{
					currMillisec = System.currentTimeMillis();
					datiOperazioneCancellazioneLogica.setCurrMillisec(currMillisec);
					siacTRecapitoSoggettoMod = DatiOperazioneUtils.impostaDatiOperazioneLogin(
							siacTRecapitoSoggettoMod, datiOperazioneCancellazioneLogica,
							siacTAccountRepository);
					// salvo sul db:
					siacTRecapitoSoggettoModRepository.saveAndFlush(siacTRecapitoSoggettoMod);
				}
			}

			// siac_t_modpag_mod
			List<SiacTModpagModFin> siacTModpagsMod = siacTSoggettoMod.getSiacTModpagMods();
			if (siacTModpagsMod != null && siacTModpagsMod.size() > 0)
			{
				for (SiacTModpagModFin siacTModpagMod : siacTModpagsMod)
				{
					currMillisec = System.currentTimeMillis();
					datiOperazioneCancellazioneLogica.setCurrMillisec(currMillisec);
					siacTModpagMod = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTModpagMod,
							datiOperazioneCancellazioneLogica, siacTAccountRepository);
					// salvo sul db:
					siacTModpagModRepository.saveAndFlush(siacTModpagMod);
				}
			}

			// siac_t_indirizzo_soggetto_mod
			List<SiacTIndirizzoSoggettoModFin> siacTIndirizzosSoggettoMod = siacTSoggettoMod
					.getSiacTIndirizzoSoggettoMods();
			if (siacTIndirizzosSoggettoMod != null && siacTIndirizzosSoggettoMod.size() > 0)
			{
				for (SiacTIndirizzoSoggettoModFin siacTIndirizzoSoggettoMod : siacTIndirizzosSoggettoMod)
				{
					currMillisec = System.currentTimeMillis();
					datiOperazioneCancellazioneLogica.setCurrMillisec(currMillisec);

					List<SiacRIndirizzoSoggettoTipoModFin> listaSiacRIndirizzoSoggettoTipoMod = siacTIndirizzoSoggettoMod
							.getSiacRIndirizzoSoggettoTipoMods();
					if (null != listaSiacRIndirizzoSoggettoTipoMod
							&& listaSiacRIndirizzoSoggettoTipoMod.size() > 0)
					{
						for (SiacRIndirizzoSoggettoTipoModFin rIndirizzoSoggettoTipoMod : listaSiacRIndirizzoSoggettoTipoMod)
						{
							if (rIndirizzoSoggettoTipoMod != null
									&& rIndirizzoSoggettoTipoMod.getDataFineValidita() == null)
							{
								// Se valido
								rIndirizzoSoggettoTipoMod = DatiOperazioneUtils
										.impostaDatiOperazioneLogin(rIndirizzoSoggettoTipoMod,
												datiOperazioneCancellazioneLogica,
												siacTAccountRepository);
								// salvo sul db:
								siacRIndirizzoSoggettoTipoModRepository
										.saveAndFlush(rIndirizzoSoggettoTipoMod);
							}
						}
					}

					siacTIndirizzoSoggettoMod = DatiOperazioneUtils.impostaDatiOperazioneLogin(
							siacTIndirizzoSoggettoMod, datiOperazioneCancellazioneLogica,
							siacTAccountRepository);
					// salvo sul db:
					siacTIndirizzoSoggettoModRepository.saveAndFlush(siacTIndirizzoSoggettoMod);
				}
			}

			// siac_r_soggrel_modpag_mod
			List<SiacRSoggrelModpagModFin> siacRSoggrelsModpagMod = siacTSoggettoMod
					.getSiacRSoggrelModpagMods();
			if (siacRSoggrelsModpagMod != null && siacRSoggrelsModpagMod.size() > 0)
			{
				for (SiacRSoggrelModpagModFin siacRSoggrelModpagMod : siacRSoggrelsModpagMod)
				{
					currMillisec = System.currentTimeMillis();
					datiOperazioneCancellazioneLogica.setCurrMillisec(currMillisec);
					siacRSoggrelModpagMod = DatiOperazioneUtils.impostaDatiOperazioneLogin(
							siacRSoggrelModpagMod, datiOperazioneCancellazioneLogica,
							siacTAccountRepository);
					// salvo sul db:
					siacRSoggrelModpagModRepository.saveAndFlush(siacRSoggrelModpagMod);
				}
			}

			// siac_r_soggetto_relaz_mod
			List<SiacRSoggettoRelazModFin> siacRSoggettoRelazMods = siacTSoggettoMod
					.getSiacRSoggettoRelazMods();
			if (siacRSoggettoRelazMods != null && siacRSoggettoRelazMods.size() > 0)
			{
				for (SiacRSoggettoRelazModFin siacRSoggettoRelazMod : siacRSoggettoRelazMods)
				{
					currMillisec = System.currentTimeMillis();
					datiOperazioneCancellazioneLogica.setCurrMillisec(currMillisec);
					siacRSoggettoRelazMod = DatiOperazioneUtils.impostaDatiOperazioneLogin(
							siacRSoggettoRelazMod, datiOperazioneCancellazioneLogica,
							siacTAccountRepository);
					// salvo sul db:
					siacRSoggettoRelazModRepository.saveAndFlush(siacRSoggettoRelazMod);
				}
			}

			// siac_r_soggetto_onere_mod
			List<SiacRSoggettoOnereModFin> siacRSoggettoOnereMods = siacTSoggettoMod
					.getSiacRSoggettoOnereMods();
			if (siacRSoggettoOnereMods != null && siacRSoggettoOnereMods.size() > 0)
			{
				for (SiacRSoggettoOnereModFin siacRSoggettoOnereMod : siacRSoggettoOnereMods)
				{
					currMillisec = System.currentTimeMillis();
					datiOperazioneCancellazioneLogica.setCurrMillisec(currMillisec);
					siacRSoggettoOnereMod = DatiOperazioneUtils.impostaDatiOperazioneLogin(
							siacRSoggettoOnereMod, datiOperazioneCancellazioneLogica,
							siacTAccountRepository);
					// salvo sul db:
					siacRSoggettoOnereModRepository.saveAndFlush(siacRSoggettoOnereMod);
				}
			}

			// siac_r_soggetto_classe_mod
			List<SiacRSoggettoClasseModFin> siacRSoggettoClasseMods = siacTSoggettoMod
					.getSiacRSoggettoClasseMods();
			if (siacRSoggettoClasseMods != null && siacRSoggettoClasseMods.size() > 0)
			{
				for (SiacRSoggettoClasseModFin siacRSoggettoClasseMod : siacRSoggettoClasseMods)
				{
					currMillisec = System.currentTimeMillis();
					datiOperazioneCancellazioneLogica.setCurrMillisec(currMillisec);
					siacRSoggettoClasseMod = DatiOperazioneUtils.impostaDatiOperazioneLogin(
							siacRSoggettoClasseMod, datiOperazioneCancellazioneLogica,
							siacTAccountRepository);
					// salvo sul db:
					siacRSoggettoClasseModRepository.saveAndFlush(siacRSoggettoClasseMod);
				}
			}

			// siac_r_soggetto_attr_mod
			List<SiacRSoggettoAttrModFin> siacRSoggettoAttrMods = siacTSoggettoMod
					.getSiacRSoggettoAttrMods();
			if (siacRSoggettoAttrMods != null && siacRSoggettoAttrMods.size() > 0)
			{
				for (SiacRSoggettoAttrModFin siacRSoggettoAttrMod : siacRSoggettoAttrMods)
				{
					currMillisec = System.currentTimeMillis();
					datiOperazioneCancellazioneLogica.setCurrMillisec(currMillisec);
					siacRSoggettoAttrMod = DatiOperazioneUtils.impostaDatiOperazioneLogin(
							siacRSoggettoAttrMod, datiOperazioneCancellazioneLogica,
							siacTAccountRepository);
					// salvo sul db:
					siacRSoggettoAttrModRepository.saveAndFlush(siacRSoggettoAttrMod);
				}
			}

			siacTSoggettoMod = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTSoggettoMod,
					datiOperazioneCancellazioneLogica, siacTAccountRepository);
			// salvo sul db:
			siacTSoggettoModRepository.saveAndFlush(siacTSoggettoMod);
		}
	}

	/**
	 * Metodo che si occupa di pulire le tabelle _mod
	 * 
	 * @param req
	 * @return
	 */
	public Soggetto annullaSoggettoInModifica(Soggetto soggettoDaModificare,
			Richiedente richiedente, Ente ente)
	{
		long currMillisec = System.currentTimeMillis();

		Operazione operazioneModifica = Operazione.MODIFICA;
		Operazione operazioneCancellazioneLogica = Operazione.CANCELLAZIONE_LOGICA_RECORD;

		int idEnte = ente.getUid();
		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository
				.findOne(idEnte);

		DatiOperazioneDto datiOperazioneModifica = new DatiOperazioneDto(currMillisec,
				operazioneModifica, siacTEnteProprietario, richiedente.getAccount().getId());
		DatiOperazioneDto datiOperazioneCancellazioneLogica = new DatiOperazioneDto(currMillisec,
				operazioneCancellazioneLogica, siacTEnteProprietario, richiedente.getAccount()
						.getId());

		SiacTSoggettoModFin siacTSoggettoMod = siacTSoggettoModRepository.findOne(soggettoDaModificare
				.getUid());
		SiacTSoggettoFin siacTSoggetto = siacTSoggettoRepository.findOne(siacTSoggettoMod
				.getSiacTSoggetto().getUid());

		// Cancello fisicamente su siac_t_soggetto_mod e le sue eventuali foglie
		// *_mod
		// 05/12/2013 : Vecchia versione
		deleteLogicaTabelleSoggettoMod(siacTSoggettoMod, datiOperazioneCancellazioneLogica);

		// Riporto a VALIDO lo stato del soggetto sulla tabella siac_t_soggetto
		StatoOperativoAnagrafica statoOperativoAnagrafica = StatoOperativoAnagrafica.VALIDO;
		salvaStatoOperativoAnagrafica(statoOperativoAnagrafica, siacTSoggetto, idEnte,
				datiOperazioneModifica);
		soggettoDaModificare = map(siacTSoggetto, Soggetto.class, FinMapId.SiacTSoggetto_Soggetto);
		soggettoDaModificare = EntityToModelConverter.soggettoEntityToSoggettoModel(siacTSoggetto,
				soggettoDaModificare);

		// Termino restituendo l'oggetto di ritorno:
		return soggettoDaModificare;
	}

	/**
	 * validaDaInModifica
	 * 
	 * @param richiedente
	 * @param ente
	 * @param idSiacTSoggettoInModifica
	 * @param idPadreSiacTSoggettoInModifica
	 * @return
	 */
	public Soggetto validaDaInModifica(Richiedente richiedente, Ente ente,
			Integer idSiacTSoggettoInModifica, Integer idPadreSiacTSoggettoInModifica,
			DatiOperazioneDto datiOperazione)
	{
		long currMillisec = System.currentTimeMillis();
		String loginOperazione = richiedente.getAccount().getNome();
		Operazione operazione = Operazione.MODIFICA;

		int idEnte = ente.getUid();

		SiacDAmbitoFin siacDAmbitoPerCode = datiOperazione.getSiacDAmbito();
		Integer idAmbito = siacDAmbitoPerCode.getAmbitoId();

		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository
				.findOne(idEnte);

		// quando aggiorno un record:
		DatiOperazioneDto datiOperazioneModifica = new DatiOperazioneDto(currMillisec, operazione,
				siacTEnteProprietario, richiedente.getAccount().getId());

		// quando inserisco un nuovo record:
		Operazione operazioneInsert = Operazione.INSERIMENTO;
		DatiOperazioneDto datiOperazioneInserisci = new DatiOperazioneDto(currMillisec,
				operazioneInsert, siacTEnteProprietario, richiedente.getAccount().getId());
		// /////////////

		SiacTSoggettoFin siacTSoggettoOriginale = siacTSoggettoRepository
				.findOne(idPadreSiacTSoggettoInModifica);
		SiacTSoggettoModFin siacTSoggettoMod = siacTSoggettoModRepository.findAllBySoggettoModId(
				idSiacTSoggettoInModifica).get(0);
		Integer idSoggMod = siacTSoggettoMod.getSogModId();

		//
		List<SiacRSoggettoAttrFin> listaAttrs = siacTSoggettoOriginale.getSiacRSoggettoAttrs();
		if (null != listaAttrs && listaAttrs.size() > 0)
		{
			for (SiacRSoggettoAttrFin iterato : listaAttrs)
			{
				if (iterato.getDataFineValidita() == null)
				{
					// Se valido
					Operazione operazioneAnnulla = Operazione.ANNULLA;
					DatiOperazioneDto datiAnnulla = new DatiOperazioneDto(currMillisec,
							operazioneAnnulla, siacTEnteProprietario, richiedente.getAccount()
									.getId());
					DatiOperazioneUtils.annullaRecord(iterato, siacRSoggettoAttrRepository,
							datiAnnulla, siacTAccountRepository);
					// salvo sul db:
					iterato = siacRSoggettoAttrRepository.saveAndFlush(iterato);
				}
			}
		}

		// TRAVASO SU SiacTSoggettoFin
		siacTSoggettoOriginale = EntityToEntityConverter.siacTSoggettoModToSiacTSoggetto(
				siacTSoggettoMod, loginOperazione);
		siacTSoggettoOriginale = DatiOperazioneUtils.impostaDatiOperazioneLogin(
				siacTSoggettoOriginale, datiOperazioneModifica, siacTAccountRepository);
		siacTSoggettoOriginale.setLoginCreazione(loginOperazione);
		// salvo sul db:
		siacTSoggettoOriginale = siacTSoggettoRepository.saveAndFlush(siacTSoggettoOriginale);
		// //

		// setto il nuovo stato:
		salvaStatoOperativoAnagrafica(StatoOperativoAnagrafica.VALIDO, siacTSoggettoOriginale,
				idEnte, datiOperazioneInserisci);
		// /////////

		// prima della modifica era persona fisica o giuridica ???
		SoggettoTipoDto soggettoTipoDto = soggettoDao
				.getPersonaFisicaOppureGiuridica(idPadreSiacTSoggettoInModifica);
		boolean isPersonaFisicPrimaModifica = soggettoTipoDto.isPersonaFisica();
		// ///////////

		SoggettoTipoModDto soggettoTipoModDto = soggettoDao
				.getPersonaFisicaOppureGiuridicaMod(idSoggMod);
		boolean isPersonaFisicaDopoModifica = soggettoTipoModDto.isPersonaFisica();

		// QUESTO DUPLICAVA I RECORD SU PERSONA FISICA / GIURIDICA
		if (isPersonaFisicPrimaModifica != isPersonaFisicaDopoModifica)
		{
			if (isPersonaFisicaDopoModifica)
			{
				// caso era giuridica e diventa fisica
				// invalido la persona giuridica:
				SiacTPersonaGiuridicaFin personaGiuridica = soggettoTipoDto
						.getSiactTpersonaGiuridica();
				personaGiuridica = DatiOperazioneUtils.cancellaRecord(personaGiuridica,
						siacTPersonaGiuridicaRepository, datiOperazioneModifica,
						siacTAccountRepository);
				// inserisco una persona fisica:
				SiacTPersonaFisicaModFin siacTPersonaFisicaMod = soggettoTipoModDto
						.getSiactTpersonaFisicaMod();
				SiacTPersonaFisicaFin personaFisica = EntityToEntityConverter
						.siacTPersonaFisicaModToSiacTPersonaFisica(siacTPersonaFisicaMod);
				personaFisica = savePersonaFisica(personaFisica, datiOperazioneInserisci);
			}
			else
			{
				// caso era fisica e diventa giuridica
				// invalido la persona fisica:
				SiacTPersonaFisicaFin personaFisica = soggettoTipoDto.getSiactTpersonaFisica();
				personaFisica = DatiOperazioneUtils.cancellaRecord(personaFisica,
						siacTPersonaFisicaRepository, datiOperazioneModifica,
						siacTAccountRepository);
				// inserisco una persona fisica:
				SiacTPersonaGiuridicaModFin siacTPersonaGiuridicaMod = soggettoTipoModDto
						.getSiactTpersonaGiuridicaMod();
				SiacTPersonaGiuridicaFin personaGiuridica = EntityToEntityConverter
						.siacTPersonaGiuridicaModToSiacTPersonaGiuridica(siacTPersonaGiuridicaMod);
				personaGiuridica = savePersonaGiuridica(personaGiuridica, datiOperazioneInserisci);
			}
		}
		else
		{
			// caso resta dello stesso tipo
			if (isPersonaFisicaDopoModifica)
			{
				// PERSONA FISICA
				// invalido la vecchia persona fisica:
				SiacTPersonaFisicaFin personaFisicaDelete = soggettoTipoDto.getSiactTpersonaFisica();
				personaFisicaDelete = DatiOperazioneUtils.cancellaRecord(personaFisicaDelete,
						siacTPersonaFisicaRepository, datiOperazioneModifica,
						siacTAccountRepository);

				// inserisco una nuova occorrenza di persona fisica
				SiacTPersonaFisicaModFin siacTPersonaFisicaMod = soggettoTipoModDto
						.getSiactTpersonaFisicaMod();
				SiacTPersonaFisicaFin personaFisica = EntityToEntityConverter
						.siacTPersonaFisicaModToSiacTPersonaFisica(siacTPersonaFisicaMod);
				personaFisica = savePersonaFisica(personaFisica, datiOperazioneInserisci);
			}
			else
			{
				// PERSONA GIURIDICA
				// invalido la vecchia persona giuridica:
				SiacTPersonaGiuridicaFin personaGiuridicaDelete = soggettoTipoDto
						.getSiactTpersonaGiuridica();
				personaGiuridicaDelete = DatiOperazioneUtils.cancellaRecord(personaGiuridicaDelete,
						siacTPersonaGiuridicaRepository, datiOperazioneModifica,
						siacTAccountRepository);

				// inserisco una nuova occorrenza di persona giuridica
				SiacTPersonaGiuridicaModFin siacTPersonaGiuridicaMod = soggettoTipoModDto
						.getSiactTpersonaGiuridicaMod();
				SiacTPersonaGiuridicaFin personaGiuridica = EntityToEntityConverter
						.siacTPersonaGiuridicaModToSiacTPersonaGiuridica(siacTPersonaGiuridicaMod);
				personaGiuridica = savePersonaGiuridica(personaGiuridica, datiOperazioneInserisci);
			}
		}

		// classificazioni:
		String[] classificazioni = getElencoCodiciClassificazioniMod(idSoggMod);
		updateClassificazioni(classificazioni, datiOperazioneModifica, siacTSoggettoOriginale,
				idEnte, idAmbito);

		// ONERI:
		String[] oneriCodes = getElencoCodiciOneriMod(idSoggMod);
		updateOneri(oneriCodes, datiOperazioneModifica, siacTSoggettoOriginale, idEnte, idAmbito);

		// indirizzi PER ORA MANCA LA TABELLA siac_r_indirizzo_soggetto_tipo_mod

		// invalido i vecchi:
		List<SiacTIndirizzoSoggettoFin> indirizziOld = siacTIndirizzoSoggettoRepository
				.findValidiByIdSoggetto(siacTSoggettoOriginale.getUid(),
						datiOperazioneModifica.getTs());
		for (SiacTIndirizzoSoggettoFin siacTInd : indirizziOld)
		{
			eliminaIndirizzo(siacTInd, datiOperazioneModifica);
		}

		// scrivo i nuovi da mod:
		List<SiacTIndirizzoSoggettoModFin> indirizziSoggMod = siacTIndirizzoSoggettoModRepository
				.findValidoBySoggModId(idSoggMod, datiOperazioneModifica.getTs());
		for (SiacTIndirizzoSoggettoModFin siacTIndMod : indirizziSoggMod)
		{
			saveIndirizzoFromIndirizzoMod(siacTIndMod, siacTSoggettoOriginale, idEnte,
					datiOperazioneInserisci);
		}

		// recapiti:
		List<SiacTRecapitoSoggettoModFin> recapitiMod = siacTRecapitoSoggettoModRepository
				.findValidoBySoggModId(idSoggMod, datiOperazioneModifica.getTs());
		List<Contatto> listaContatti = contattiModToContattModel(recapitiMod);
		aggiornaContatti(listaContatti, datiOperazioneModifica, siacTSoggettoOriginale, idEnte);

		Operazione operazioneCancella = Operazione.CANCELLAZIONE_LOGICA_RECORD;
		DatiOperazioneDto datiOperazioneCancellazioneLogica = new DatiOperazioneDto(currMillisec,
				operazioneCancella, siacTEnteProprietario, richiedente.getAccount().getId());

		deleteLogicaTabelleSoggettoMod(siacTSoggettoMod, datiOperazioneCancellazioneLogica);

		entityRefresh(siacTSoggettoOriginale);
		entityRefresh(siacTSoggettoMod);
		// RITORNO RISULTATI:
		Soggetto soggetto = new Soggetto();
		soggetto.setCodiceSoggetto(siacTSoggettoMod.getSoggettoCode());
		soggetto = EntityToModelConverter.soggettoEntityToSoggettoModel(siacTSoggettoOriginale,
				soggetto);
		// Termino restituendo l'oggetto di ritorno:
		return soggetto;

	}

	/**
	 * contattiModToContattModel
	 * 
	 * @param recapitiMod
	 * @return
	 */
	private List<Contatto> contattiModToContattModel(List<SiacTRecapitoSoggettoModFin> recapitiMod)
	{
		ArrayList<Contatto> listaContatti = new ArrayList<Contatto>();
		if (recapitiMod != null && recapitiMod.size() > 0)
		{
			for (SiacTRecapitoSoggettoModFin modIterato : recapitiMod)
			{
				if (modIterato != null)
				{
					Contatto contatto = new Contatto();
					contatto.setDescrizione(modIterato.getRecapitoDesc());
					contatto.setContattoCodModo(modIterato.getSiacDRecapitoModo()
							.getRecapitoModoCode());
					contatto.setAvviso(StringUtils.checkStringBooleanForDb(modIterato.getAvviso()));
					listaContatti.add(contatto);
				}
			}
		}
		// Termino restituendo l'oggetto di ritorno:
		return listaContatti;
	}

	/**
	 * updateOneri
	 * 
	 * @param oneriCodes
	 * @param datiOperazione
	 * @param siacTSoggetto
	 * @param idEnte
	 * @param idAmbito
	 */
	private void updateOneri(String[] oneriCodes, DatiOperazioneDto datiOperazione,
			SiacTSoggettoFin siacTSoggetto, Integer idEnte, Integer idAmbito)
	{
		Integer idSiacTSoggetto = siacTSoggetto.getSoggettoId();
		DatiOperazioneDto datiOperazioneModifica = DatiOperazioneDto.buildDatiOperazione(
				datiOperazione, Operazione.MODIFICA);
		DatiOperazioneDto datiOperazioneInserisci = DatiOperazioneDto.buildDatiOperazione(
				datiOperazione, Operazione.INSERIMENTO);
		OneriInModificaInfoDto oneriInMod = valutaOneriDaModificare(oneriCodes, idSiacTSoggetto);
		ArrayList<SiacRSoggettoOnereFin> oneriEliminati = oneriInMod.getOneriEliminati();
		ArrayList<String> nuoviOneri = oneriInMod.getNuoviOneri();
		if (oneriEliminati != null && oneriEliminati.size() > 0)
		{
			for (SiacRSoggettoOnereFin daElimin : oneriEliminati)
			{
				datiOperazioneModifica.setCurrMillisec(System.currentTimeMillis());
				DatiOperazioneUtils.cancellaRecord(daElimin, siacRSoggettoOnereRepository,
						datiOperazioneModifica, siacTAccountRepository);
			}
		}
		if (nuoviOneri != null && nuoviOneri.size() > 0)
		{
			for (String codeOnere : nuoviOneri)
			{
				salvaOnere(siacTSoggetto, idEnte, codeOnere, datiOperazioneInserisci);
			}
		}
	}

	/**
	 * updateOneriMod
	 * 
	 * @param oneriCodes
	 * @param datiOperazione
	 * @param siacTSoggetto
	 * @param idEnte
	 * @param idAmbito
	 */
	private void updateOneriMod(String[] oneriCodes, DatiOperazioneDto datiOperazione,
			SiacTSoggettoModFin siacTSoggetto, Integer idEnte, Integer idAmbito)
	{
		Integer idSiacTSoggetto = siacTSoggetto.getSogModId();
		DatiOperazioneDto datiOperazioneModifica = DatiOperazioneDto.buildDatiOperazione(
				datiOperazione, Operazione.MODIFICA);
		DatiOperazioneDto datiOperazioneInserisci = DatiOperazioneDto.buildDatiOperazione(
				datiOperazione, Operazione.INSERIMENTO);
		OneriModInModificaInfoDto oneriInMod = valutaOneriDaModificareMod(oneriCodes,
				idSiacTSoggetto);
		ArrayList<SiacRSoggettoOnereModFin> oneriEliminati = oneriInMod.getOneriEliminati();
		ArrayList<String> nuoviOneri = oneriInMod.getNuoviOneri();
		if (oneriEliminati != null && oneriEliminati.size() > 0)
		{
			for (SiacRSoggettoOnereModFin daElimin : oneriEliminati)
			{
				datiOperazioneModifica.setCurrMillisec(System.currentTimeMillis());
				DatiOperazioneUtils.cancellaRecord(daElimin, siacRSoggettoOnereModRepository,
						datiOperazioneModifica, siacTAccountRepository);
			}
		}
		if (nuoviOneri != null && nuoviOneri.size() > 0)
		{
			for (String codeOnere : nuoviOneri)
			{
				salvaOnereMod(siacTSoggetto.getSiacTSoggetto(), siacTSoggetto, idEnte, codeOnere,
						datiOperazioneInserisci);
			}
		}
	}

	/**
	 * updateClassificazioni
	 * 
	 * @param classificazioni
	 * @param datiOperazione
	 * @param siacTSoggetto
	 * @param idEnte
	 * @param idAmbito
	 */
	private void updateClassificazioni(String[] classificazioni, DatiOperazioneDto datiOperazione,
			SiacTSoggettoFin siacTSoggetto, Integer idEnte, Integer idAmbito)
	{
		Integer idSiacTSoggetto = siacTSoggetto.getSoggettoId();
		DatiOperazioneDto datiOperazioneModifica = DatiOperazioneDto.buildDatiOperazione(
				datiOperazione, Operazione.MODIFICA);
		DatiOperazioneDto datiOperazioneInserisci = DatiOperazioneDto.buildDatiOperazione(
				datiOperazione, Operazione.INSERIMENTO);
		ClassificazioniInModificaInfoDto classInMod = valutaClassificazioniDaModificare(
				classificazioni, idSiacTSoggetto);
		ArrayList<SiacRSoggettoClasseFin> classDaEliminare = classInMod.getClassificazioniEliminate();
		ArrayList<String> nuoveClasse = classInMod.getNuoveClassi();
		if (classDaEliminare != null && classDaEliminare.size() > 0)
		{
			for (SiacRSoggettoClasseFin daElimin : classDaEliminare)
			{
				datiOperazioneModifica.setCurrMillisec(System.currentTimeMillis());
				DatiOperazioneUtils.cancellaRecord(daElimin, siacRSoggettoClasseRepository,
						datiOperazioneModifica, siacTAccountRepository);
			}
		}
		if (nuoveClasse != null && nuoveClasse.size() > 0)
		{
			for (String codeClass : nuoveClasse)
			{
				if (!StringUtils.isEmpty(codeClass))
				{
					salvaSoggettoClasse(siacTSoggetto, idEnte, idAmbito, codeClass,
							datiOperazioneInserisci);
				}
			}
		}
	}

	/**
	 * updateClassificazioniMod
	 * 
	 * @param classificazioni
	 * @param datiOperazione
	 * @param siacTSoggetto
	 * @param idEnte
	 * @param idAmbito
	 */
	private void updateClassificazioniMod(String[] classificazioni,
			DatiOperazioneDto datiOperazione, SiacTSoggettoModFin siacTSoggetto, Integer idEnte,
			Integer idAmbito)
	{
		Integer idSiacTSoggetto = siacTSoggetto.getSogModId();
		DatiOperazioneDto datiOperazioneModifica = DatiOperazioneDto.buildDatiOperazione(
				datiOperazione, Operazione.MODIFICA);
		DatiOperazioneDto datiOperazioneInserisci = DatiOperazioneDto.buildDatiOperazione(
				datiOperazione, Operazione.INSERIMENTO);
		ClassificazioniModInModificaInfoDto classInMod = valutaClassificazioniModDaModificare(
				classificazioni, idSiacTSoggetto);
		ArrayList<SiacRSoggettoClasseModFin> classDaEliminare = classInMod
				.getClassificazioniEliminate();
		ArrayList<String> nuoveClasse = classInMod.getNuoveClassi();
		if (classDaEliminare != null && classDaEliminare.size() > 0)
		{
			for (SiacRSoggettoClasseModFin daElimin : classDaEliminare)
			{
				datiOperazioneModifica.setCurrMillisec(System.currentTimeMillis());
				DatiOperazioneUtils.cancellaRecord(daElimin, siacRSoggettoClasseModRepository,
						datiOperazioneModifica, siacTAccountRepository);
			}
		}
		if (nuoveClasse != null && nuoveClasse.size() > 0)
		{
			for (String codeClass : nuoveClasse)
			{
				if (!StringUtils.isEmpty(codeClass))
				{
					salvaSoggettoClasseMod(siacTSoggetto.getSiacTSoggetto(), siacTSoggetto, idEnte,
							idAmbito, codeClass, datiOperazioneInserisci);
				}
			}
		}
	}

	/**
	 * updateModalitaDiPagamento
	 * 
	 * @param mdpToUpdate
	 * @param siacTSoggetto
	 * @param idEnte
	 * @param datiOperazione
	 */
	private void updateModalitaDiPagamento(ModalitaPagamentoSoggetto mdpToUpdate,
			SiacTSoggettoFin siacTSoggetto, Integer idEnte, DatiOperazioneDto datiOperazione)
	{

		DatiOperazioneDto datiOperazioneInserisci = datiOperazione;
		datiOperazioneInserisci.setOperazione(Operazione.INSERIMENTO);
		DatiOperazioneDto datiOperazioneModifica = datiOperazione;
		datiOperazioneModifica.setOperazione(Operazione.MODIFICA);

		SiacTModpagFin siacTModPagToUpdate = new SiacTModpagFin();
		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository
				.findOne(idEnte);

		String tipoAccredito = "";
		if (mdpToUpdate.getTipoAccredito() != null)
		{
			tipoAccredito = mdpToUpdate.getTipoAccredito().toString();
		}
		else
		{
			tipoAccredito = mdpToUpdate.getModalitaAccreditoSoggetto().getCodice();
		}

		// SiacDAccreditoTipoFin dAccreditoTipoForCheck =
		// siacDTipoAccreditoRepository.findTipoAccreditoValidoByCode(idEnte,
		// tipoAccredito,datiOperazione.getTs()).get(0);
		// qui devo prendere la modalita scelta e andare in join con il gruppo
		SiacDAccreditoTipoFin dAccreditoTipoForCheck = siacDTipoAccreditoRepository
				.findTipoAccreditoValidoPerGruppoByCode(idEnte,
						mdpToUpdate.getModalitaAccreditoSoggetto().getCodice(),
						datiOperazione.getTs()).get(0);

		SiacDAccreditoGruppoFin dAccreditoGruppoForCheck = dAccreditoTipoForCheck
				.getSiacDAccreditoGruppo(); // siacDAccreditoGruppoRepository.findGruppoByTipoId(idEnte,
											// dAccreditoTipoForCheck.getSiacDAccreditoGruppo().getUid());

		// Controllo che sia di tipo bancario
		if (dAccreditoGruppoForCheck.getAccreditoGruppoCode().equals(
				Constanti.D_ACCREDITO_TIPO_CODE_Circuito_bancario)
				|| dAccreditoGruppoForCheck.getAccreditoGruppoCode().equals(
						Constanti.D_ACCREDITO_TIPO_CODE_Circuito_Banca_d_Italia)
				|| dAccreditoGruppoForCheck.getAccreditoGruppoCode().equals(
						Constanti.D_ACCREDITO_TIPO_CODE_Circuito_Postale))
		{

			siacTModPagToUpdate = siacTModpagRepository.findOne(mdpToUpdate.getUid());

			siacTModPagToUpdate.setUid(mdpToUpdate.getUid());

			// imposto operazioni di base
			siacTModPagToUpdate = DatiOperazioneUtils.impostaDatiOperazioneLogin(
					siacTModPagToUpdate, datiOperazioneModifica, siacTAccountRepository);

			if (mdpToUpdate.getBic() != null)
				siacTModPagToUpdate.setBic(mdpToUpdate.getBic().trim());

			if (mdpToUpdate.getIban() != null)
				siacTModPagToUpdate.setIban(mdpToUpdate.getIban().trim());

			if (mdpToUpdate.getDenominazioneBanca() != null)
				siacTModPagToUpdate.setDenominazioneBanca(mdpToUpdate.getDenominazioneBanca().trim());

			if (mdpToUpdate.getContoCorrente() != null)
				siacTModPagToUpdate.setContocorrente(mdpToUpdate.getContoCorrente().trim());


			// NUOVO CAMPO
			// Intestazione conto
			if (mdpToUpdate.getIntestazioneConto() != null)
			{
				siacTModPagToUpdate
						.setContocorrenteIntestazione(mdpToUpdate.getIntestazioneConto());
			}

			// fine NUOVO CAMPO

			// Inserire Associazione modalita Pagamento

			// Gestisco il tipo accredito

			SiacDAccreditoTipoFin siacDAccreditoTipo = siacDTipoAccreditoRepository
					.findTipoAccreditoValidoByCode(idEnte,
							mdpToUpdate.getModalitaAccreditoSoggetto().getCodice(),
							datiOperazioneModifica.getTs()).get(0);
			siacTModPagToUpdate.setSiacDAccreditoTipo(siacDAccreditoTipo);

			if (mdpToUpdate.getNote() != null)
			{
				siacTModPagToUpdate.setNote(mdpToUpdate.getNote());
			}
			if (mdpToUpdate.getDataFineValidita() != null)
			{
				// se data fine validita valorizzata
				// setto alla mezzanotte:
				Timestamp dataScadenza = TimingUtils.setToMidNigth(mdpToUpdate
						.getDataFineValidita());
				siacTModPagToUpdate.setDataFineValidita(dataScadenza);

			}
			else
			{
				// se data fine validita non valorizzata
				siacTModPagToUpdate.setDataFineValidita(null);
			}
			
			siacTModPagToUpdate.setPerStipendi(mdpToUpdate.getPerStipendi());


			// Setto il soggetto ID
			SiacTModpagFin siacTModpagToCheck = siacTModpagRepository.findOne(mdpToUpdate.getUid());
			SiacTSoggettoFin siacTSoggettoDaAssociare = siacTSoggettoRepository
					.findOne(siacTModpagToCheck.getSiacTSoggetto().getUid());
			siacTModPagToUpdate.setSiacTSoggetto(siacTSoggettoDaAssociare);

			siacTModPagToUpdate.setDataModifica(new Date());
			siacTModPagToUpdate.setDataCreazione(siacTModpagToCheck.getDataCreazione());
			// salvo sul db:
			SiacTModpagFin siacTModPagInserito = siacTModpagRepository
					.saveAndFlush(siacTModPagToUpdate);

			// Nel caso lo stato cambia ()
			SiacRModpagStatoFin siacRModpagStatoToCheck = siacRModpagStatoRepository
					.findStatoValidoByMdpId(mdpToUpdate.getUid()).get(0);
			if (!siacRModpagStatoToCheck.getSiacDModpagStato().getModpagStatoDesc()
					.equalsIgnoreCase(mdpToUpdate.getDescrizioneStatoModalitaPagamento()))
			{
				SiacRModpagStatoFin siacRModpagStatoToUpdate = siacRModpagStatoRepository
						.findStatoValidoByMdpId(mdpToUpdate.getUid()).get(0);
				siacRModpagStatoToUpdate = DatiOperazioneUtils.impostaDatiOperazioneLogin(
						siacRModpagStatoToUpdate, datiOperazione, siacTAccountRepository);

				siacRModpagStatoToUpdate.setSiacTModpag(siacTModPagInserito);

				siacRModpagStatoToUpdate.setDataModifica(new Date());
				siacRModpagStatoToUpdate.setDataCreazione(siacRModpagStatoToCheck
						.getDataCreazione());

				SiacDModpagStatoFin siacDModpagStato = siacDModpagStatoRepository
						.findModPagStatoDValidoByCode(idEnte,
								mdpToUpdate.getDescrizioneStatoModalitaPagamento().toUpperCase(),
								datiOperazioneModifica.getTs()).get(0);
				siacRModpagStatoToUpdate.setSiacDModpagStato(siacDModpagStato);
				// salvo sul db:
				siacRModpagStatoRepository.saveAndFlush(siacRModpagStatoToUpdate);
			}
		}

		// Controllo che sia di tipo contante
		if (dAccreditoGruppoForCheck.getAccreditoGruppoCode().equals(
				Constanti.D_ACCREDITO_TIPO_CODE_Contanti))
		{

			siacTModPagToUpdate = siacTModpagRepository.findOne(mdpToUpdate.getUid());

			siacTModPagToUpdate.setUid(mdpToUpdate.getUid());

			// imposto operazioni di base
			siacTModPagToUpdate = DatiOperazioneUtils.impostaDatiOperazioneLogin(
					siacTModPagToUpdate, datiOperazioneModifica, siacTAccountRepository);

			if (mdpToUpdate.getCodiceFiscaleQuietanzante() != null)
			{
				siacTModPagToUpdate.setQuietanzianteCodiceFiscale(mdpToUpdate
						.getCodiceFiscaleQuietanzante().trim());
			}
			if (mdpToUpdate.getSoggettoQuietanzante() != null)
			{
				siacTModPagToUpdate.setQuietanziante(mdpToUpdate.getSoggettoQuietanzante());
			}

			if (mdpToUpdate.getNote() != null)
			{
				siacTModPagToUpdate.setNote(mdpToUpdate.getNote());
			}
			if (mdpToUpdate.getDataFineValidita() != null)
			{
				// se data fine validita valorizzata
				// setto alla mezzanotte:
				Timestamp dataScadenza = TimingUtils.setToMidNigth(mdpToUpdate
						.getDataFineValidita());
				siacTModPagToUpdate.setDataFineValidita(dataScadenza);

			}
			else
			{
				// se data fine non validita valorizzata
				siacTModPagToUpdate.setDataFineValidita(null);
			}

			// nuovi campi

			// nuovo campo luogo e stato nascita
			if (mdpToUpdate.getComuneNascita() != null)
			{
				siacTModPagToUpdate.setQuietanzianteNascitaLuogo(mdpToUpdate.getComuneNascita()
						.getDescrizione());
				siacTModPagToUpdate.setQuietanzianteNascitaStato(mdpToUpdate.getComuneNascita()
						.getNazioneCode());
			}

			// data di nascita quietanzante
			if (mdpToUpdate.getDataNascitaQuietanzante() != null)
			{
				siacTModPagToUpdate.setQuietanzanteNascitaData(TimingUtils
						.buildTimestamp(mdpToUpdate.getDataNascitaQuietanzante()));
			}

			siacTModPagToUpdate.setPerStipendi(mdpToUpdate.getPerStipendi());

			// fine - nuovi campi

			SiacDAccreditoTipoFin siacDAccreditoTipo = siacDTipoAccreditoRepository
					.findTipoAccreditoValidoByCode(idEnte,
							mdpToUpdate.getModalitaAccreditoSoggetto().getCodice(),
							datiOperazioneModifica.getTs()).get(0);
			siacTModPagToUpdate.setSiacDAccreditoTipo(siacDAccreditoTipo);

			// Setto il soggetto ID
			SiacTModpagFin siacTModpagToCheck = siacTModpagRepository.findOne(mdpToUpdate.getUid());
			SiacTSoggettoFin siacTSoggettoDaAssociare = siacTSoggettoRepository
					.findOne(siacTModpagToCheck.getSiacTSoggetto().getUid());
			siacTModPagToUpdate.setSiacTSoggetto(siacTSoggettoDaAssociare);

			siacTModPagToUpdate.setDataModifica(new Date());
			siacTModPagToUpdate.setDataCreazione(siacTModpagToCheck.getDataCreazione());
			// salvo sul db:
			SiacTModpagFin siacTModpagInserito = siacTModpagRepository
					.saveAndFlush(siacTModPagToUpdate);

			// Nel caso lo stato cambia ()
			SiacRModpagStatoFin siacRModpagStatoToCheck = siacRModpagStatoRepository
					.findStatoValidoByMdpId(mdpToUpdate.getUid()).get(0);
			if (!siacRModpagStatoToCheck.getSiacDModpagStato().getModpagStatoDesc()
					.equalsIgnoreCase(mdpToUpdate.getDescrizioneStatoModalitaPagamento()))
			{
				SiacRModpagStatoFin siacRModpagStatoToUpdate = siacRModpagStatoRepository
						.findStatoValidoByMdpId(mdpToUpdate.getUid()).get(0);
				siacRModpagStatoToUpdate = DatiOperazioneUtils.impostaDatiOperazioneLogin(
						siacRModpagStatoToUpdate, datiOperazioneModifica, siacTAccountRepository);

				siacRModpagStatoToUpdate.setSiacTModpag(siacTModpagInserito);

				siacRModpagStatoToUpdate.setDataModifica(new Date());
				siacRModpagStatoToUpdate.setDataCreazione(siacRModpagStatoToCheck
						.getDataCreazione());

				SiacDModpagStatoFin siacDModpagStato = siacDModpagStatoRepository
						.findModPagStatoDValidoByCode(idEnte,
								mdpToUpdate.getDescrizioneStatoModalitaPagamento().toUpperCase(),
								datiOperazioneModifica.getTs()).get(0);
				siacRModpagStatoToUpdate.setSiacDModpagStato(siacDModpagStato);
				// salvo sul db:
				siacRModpagStatoRepository.saveAndFlush(siacRModpagStatoToUpdate);
			}

		}

		// Controllo che sia di tipo contante
		if (dAccreditoGruppoForCheck.getAccreditoGruppoCode().equals(
				Constanti.D_ACCREDITO_TIPO_CODE_Generico))
		{

			siacTModPagToUpdate = siacTModpagRepository.findOne(mdpToUpdate.getUid());

			siacTModPagToUpdate.setUid(mdpToUpdate.getUid());

			// imposto operazioni di base
			siacTModPagToUpdate = DatiOperazioneUtils.impostaDatiOperazioneLogin(
					siacTModPagToUpdate, datiOperazioneModifica, siacTAccountRepository);

			if (mdpToUpdate.getNote() != null)
			{
				siacTModPagToUpdate.setNote(mdpToUpdate.getNote());
			}
			if (mdpToUpdate.getDataFineValidita() != null)
			{
				// se data fine validita valorizzata
				// setto alla mezzanotte:
				Timestamp dataScadenza = TimingUtils.setToMidNigth(mdpToUpdate
						.getDataFineValidita());
				siacTModPagToUpdate.setDataFineValidita(dataScadenza);

			}
			else
			{
				// se data fine non validita valorizzata
				siacTModPagToUpdate.setDataFineValidita(null);
			}

			siacTModPagToUpdate.setPerStipendi(mdpToUpdate.getPerStipendi());

			SiacDAccreditoTipoFin siacDAccreditoTipo = siacDTipoAccreditoRepository
					.findTipoAccreditoValidoByCode(idEnte,
							mdpToUpdate.getModalitaAccreditoSoggetto().getCodice(),
							datiOperazioneModifica.getTs()).get(0);
			siacTModPagToUpdate.setSiacDAccreditoTipo(siacDAccreditoTipo);

			// Setto il soggetto ID
			SiacTModpagFin siacTModpagToCheck = siacTModpagRepository.findOne(mdpToUpdate.getUid());
			SiacTSoggettoFin siacTSoggettoDaAssociare = siacTSoggettoRepository
					.findOne(siacTModpagToCheck.getSiacTSoggetto().getUid());
			siacTModPagToUpdate.setSiacTSoggetto(siacTSoggettoDaAssociare);

			siacTModPagToUpdate.setDataModifica(new Date());
			siacTModPagToUpdate.setDataCreazione(siacTModpagToCheck.getDataCreazione());
			// salvo sul db:
			SiacTModpagFin siacTModpagInserito = siacTModpagRepository
					.saveAndFlush(siacTModPagToUpdate);

			// Nel caso lo stato cambia ()
			SiacRModpagStatoFin siacRModpagStatoToCheck = siacRModpagStatoRepository
					.findStatoValidoByMdpId(mdpToUpdate.getUid()).get(0);
			if (!siacRModpagStatoToCheck.getSiacDModpagStato().getModpagStatoDesc()
					.equalsIgnoreCase(mdpToUpdate.getDescrizioneStatoModalitaPagamento()))
			{
				SiacRModpagStatoFin siacRModpagStatoToUpdate = siacRModpagStatoRepository
						.findStatoValidoByMdpId(mdpToUpdate.getUid()).get(0);
				siacRModpagStatoToUpdate = DatiOperazioneUtils.impostaDatiOperazioneLogin(
						siacRModpagStatoToUpdate, datiOperazioneModifica, siacTAccountRepository);

				siacRModpagStatoToUpdate.setSiacTModpag(siacTModpagInserito);

				siacRModpagStatoToUpdate.setDataModifica(new Date());
				siacRModpagStatoToUpdate.setDataCreazione(siacRModpagStatoToCheck
						.getDataCreazione());

				SiacDModpagStatoFin siacDModpagStato = siacDModpagStatoRepository
						.findModPagStatoDValidoByCode(idEnte,
								mdpToUpdate.getDescrizioneStatoModalitaPagamento().toUpperCase(),
								datiOperazioneModifica.getTs()).get(0);
				siacRModpagStatoToUpdate.setSiacDModpagStato(siacDModpagStato);
				// salvo sul db:
				siacRModpagStatoRepository.saveAndFlush(siacRModpagStatoToUpdate);
			}

		}

		// Controllo che sia di tipo cessione
		if (dAccreditoGruppoForCheck.getAccreditoGruppoCode().equals(
				Constanti.D_ACCREDITO_TIPO_CODE_Cessione_del_credito)
				|| dAccreditoGruppoForCheck.getAccreditoGruppoCode().equals(
						Constanti.D_ACCREDITO_TIPO_CODE_Cessione_dell_incasso))
		{

			SiacRSoggettoRelazFin soggRelazToUpdate = siacRSoggettoRelazRepository.findOne(mdpToUpdate
					.getUid());
			SiacDRelazTipoFin siacDRelazTipo = siacDRelazTipoRepository.findRelazTipoValidoByCode(
					idEnte, tipoAccredito, datiOperazioneModifica.getTs()).get(0);

			soggRelazToUpdate.setSiacDRelazTipo(siacDRelazTipo);

			soggRelazToUpdate.setDataModifica(new Date());
			soggRelazToUpdate.setDataCreazione(soggRelazToUpdate.getDataCreazione());

			// Setto soggetto Da
			soggRelazToUpdate.setSiacTSoggetto1(soggRelazToUpdate.getSiacTSoggetto1());
			// Setto soggetto a
			soggRelazToUpdate.setSiacTSoggetto2(soggRelazToUpdate.getSiacTSoggetto2());

			soggRelazToUpdate.setSiacTEnteProprietario(siacTEnteProprietario);

			if (mdpToUpdate.getDataFineValidita() != null)
			{
				// se data fine validita valorizzata
				// setto alla mezzanotte:
				Timestamp dataScadenza = TimingUtils.setToMidNigth(mdpToUpdate
						.getDataFineValidita());
				siacTModPagToUpdate.setDataFineValidita(dataScadenza);
				soggRelazToUpdate.setDataFineValidita(dataScadenza);
			}
			else
			{
				// se data fine validita non valorizzata
				soggRelazToUpdate.setDataFineValidita(null);
			}
			// salvo sul db:
			soggRelazToUpdate = siacRSoggettoRelazRepository.saveAndFlush(soggRelazToUpdate);

			// SiacRSoggrel_modPag gestione
			SiacRSoggrelModpagFin siacRSoggrelModpagToUpdate = siacRSoggrelModpagRepository
					.findValidiBySoggettoRelaz(soggRelazToUpdate.getUid());

			// imposto operazioni di base
			siacRSoggrelModpagToUpdate = DatiOperazioneUtils.impostaDatiOperazioneLogin(
					siacRSoggrelModpagToUpdate, datiOperazioneModifica, siacTAccountRepository);

			siacRSoggrelModpagToUpdate.setSiacRSoggettoRelaz(soggRelazToUpdate);
			siacRSoggrelModpagToUpdate.setSiacTEnteProprietario(siacTEnteProprietario);

			siacRSoggrelModpagToUpdate.setDataModifica(new Date());
			siacRSoggrelModpagToUpdate.setDataCreazione(siacRSoggrelModpagToUpdate
					.getDataCreazione());

			if (mdpToUpdate.getDataFineValidita() != null)
			{
				// setto la data alla mezzanotte:
				Timestamp dataScadenza = TimingUtils.setToMidNigth(mdpToUpdate
						.getDataFineValidita());
				siacRSoggrelModpagToUpdate.setDataFineValidita(dataScadenza);

			}
			else
			{
				siacRSoggrelModpagToUpdate.setDataFineValidita(null);
			}

			if (mdpToUpdate.getNote() != null)
			{
				siacRSoggrelModpagToUpdate.setNote(mdpToUpdate.getNote());
			}

			siacTModPagToUpdate = siacTModpagRepository.findOne(Integer.valueOf(mdpToUpdate
					.getModalitaPagamentoSoggettoCessione2().getUid()));
			siacRSoggrelModpagToUpdate.setSiacTModpag(siacTModPagToUpdate);
			// salvo sul db:
			siacRSoggrelModpagRepository.saveAndFlush(siacRSoggrelModpagToUpdate);

			SiacDRelazStatoFin siacDRelazStato = siacDRelazStatoRepository
					.findDRelazStatoValidoByCode(idEnte,
							mdpToUpdate.getDescrizioneStatoModalitaPagamento().toUpperCase())
					.get(0);
			SiacRSoggettoRelazStatoFin siacRSoggettoRelazStato = siacRSoggettoRelazStatoRepository
					.findBySoggettoRelazId(soggRelazToUpdate.getUid());
			siacRSoggettoRelazStato.setSiacDRelazStato(siacDRelazStato);
			siacRSoggettoRelazStato.setSiacRSoggettoRelaz(soggRelazToUpdate);
			// salvo sul db:
			siacRSoggettoRelazStatoRepository.saveAndFlush(siacRSoggettoRelazStato);
		}

	}

	/**
	 * validaDaProvvisorio
	 * 
	 * @param richiedente
	 * @param idSiacTSoggettoInModifica
	 * @param ente
	 */
	public void validaDaProvvisorio(Richiedente richiedente, Integer idSiacTSoggettoInModifica,
			Ente ente)
	{
		SiacTSoggettoFin siacTSoggetto = cambiaStato(richiedente, idSiacTSoggettoInModifica, ente,
				Operazione.MODIFICA, StatoOperativoAnagrafica.VALIDO);

		String loginOperazione = richiedente.getAccount().getNome();
		long currMillisec = System.currentTimeMillis();

		Operazione operazione = Operazione.MODIFICA;
		int idEnte = ente.getUid();
		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository
				.findOne(idEnte);
		DatiOperazioneDto datiOperazioneModifica = new DatiOperazioneDto(currMillisec, operazione,
				siacTEnteProprietario, richiedente.getAccount().getId());

		// JIRA 1188-aggiornamento della data ultimo aggiornamento
		siacTSoggetto = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTSoggetto,
				datiOperazioneModifica, siacTAccountRepository);
		siacTSoggetto.setLoginCreazione(loginOperazione);
		// salvo sul db:
		siacTSoggetto = siacTSoggettoRepository.saveAndFlush(siacTSoggetto);
		entityRefresh(siacTSoggetto);
	}

	private SiacTSoggettoFin cambiaStato(Richiedente richiedente, Integer idSiacTSoggettoInModifica, Ente ente,
			Operazione operazione, StatoOperativoAnagrafica statoOperativoAnagrafica) {
		return cambiaStato(richiedente, idSiacTSoggettoInModifica, ente, operazione, statoOperativoAnagrafica, null);
	}
	
	/**
	 * cambiaStato
	 * 
	 * @param richiedente
	 * @param idSiacTSoggettoInModifica
	 * @param ente
	 * @param operazione
	 * @param statoOperativoAnagrafica
	 * @param notaOperazione
	 * @return
	 */
	private SiacTSoggettoFin cambiaStato(Richiedente richiedente, Integer idSiacTSoggettoInModifica,
			Ente ente, Operazione operazione, StatoOperativoAnagrafica statoOperativoAnagrafica, String notaOperazione)
	{
		long currMillisec = System.currentTimeMillis();
		int idEnte = ente.getUid();
		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository
				.findOne(idEnte);
		DatiOperazioneDto datiOperazioneModifica = new DatiOperazioneDto(currMillisec, operazione,
				siacTEnteProprietario, richiedente.getAccount().getId());
		SiacTSoggettoFin siacTSoggetto = siacTSoggettoRepository.findOne(idSiacTSoggettoInModifica);

		salvaStatoOperativoAnagrafica(statoOperativoAnagrafica, siacTSoggetto, idEnte,
				datiOperazioneModifica, notaOperazione);
		// Termino restituendo l'oggetto di ritorno:
		return siacTSoggetto;
	}

	/**
	 * validaDaBloccato
	 * 
	 * @param richiedente
	 * @param idSiacTSoggettoInModifica
	 * @param ente
	 */
	public void validaDaBloccato(Richiedente richiedente, Integer idSiacTSoggettoInModifica,
			Ente ente)
	{
		SiacTSoggettoFin siacTSoggetto = cambiaStato(richiedente, idSiacTSoggettoInModifica, ente,
				Operazione.MODIFICA, StatoOperativoAnagrafica.VALIDO);
		entityRefresh(siacTSoggetto);
	}

	/**
	 * validaDaSospeso
	 * 
	 * @param richiedente
	 * @param idSiacTSoggettoInModifica
	 * @param ente
	 */
	public void validaDaSospeso(Richiedente richiedente, Integer idSiacTSoggettoInModifica,
			Ente ente)
	{
		SiacTSoggettoFin siacTSoggetto = cambiaStato(richiedente, idSiacTSoggettoInModifica, ente,
				Operazione.MODIFICA, StatoOperativoAnagrafica.VALIDO);
		entityRefresh(siacTSoggetto);
	}

	/**
	 * annullaSoggetto
	 * 
	 * @param richiedente
	 * @param idSiacTSoggettoInModifica
	 * @param ente
	 */
	public void annullaSoggetto(Richiedente richiedente, Integer idSiacTSoggettoInModifica,
			Ente ente)
	{
		SiacTSoggettoFin siacTSoggetto = cambiaStato(richiedente, idSiacTSoggettoInModifica, ente,
				Operazione.ANNULLA, StatoOperativoAnagrafica.ANNULLATO);
		entityRefresh(siacTSoggetto);
	}

	/**
	 * sospendiSoggetto
	 * 
	 * @param richiedente
	 * @param idSiacTSoggettoInModifica
	 * @param ente
	 * @param motivoSospensione 
	 */
	public void sospendiSoggetto(Richiedente richiedente, Integer idSiacTSoggettoInModifica,
			Ente ente, String motivoSospensione)
	{
		SiacTSoggettoFin siacTSoggetto = cambiaStato(richiedente, idSiacTSoggettoInModifica, ente,
				Operazione.SOSPENDI, StatoOperativoAnagrafica.SOSPESO, motivoSospensione);
		entityRefresh(siacTSoggetto);
	}

	/**
	 * bloccaSoggetto
	 * 
	 * @param richiedente
	 * @param idSiacTSoggettoInModifica
	 * @param ente
	 */
	//SIAC-7114
	//a seguito della manutenzione si uniforma il comportamento come sospendiSoggetto
	public void bloccaSoggetto(Richiedente richiedente, Integer idSiacTSoggettoInModifica, Ente ente, String motivoSospensione)
	{
		SiacTSoggettoFin siacTSoggetto = cambiaStato(richiedente, idSiacTSoggettoInModifica, ente,
				Operazione.BLOCCA, StatoOperativoAnagrafica.BLOCCATO, motivoSospensione);
		entityRefresh(siacTSoggetto);
	}
	
	
	public EsitoControlliDto controlliPerAggiungiSoggettoAllaClassificazione(String codiceSoggetto,String codiceClassificazione, 
			String codiceAmbito, Richiedente richiedente, Integer idEnte, DatiOperazioneDto datiOperazioneDto){
		
		EsitoControlliDto esitoControlli = new EsitoControlliDto();
		
		SiacTSoggettoFin siacTSoggetto = siacTSoggettoRepository.ricercaSoggettoNoSeSede(codiceAmbito, idEnte, codiceSoggetto, Constanti.SEDE_SECONDARIA, getNow());
		
		
		SiacDSoggettoStatoFin siacDSoggettoStato = getStatoSoggetto(siacTSoggetto);
		
		if (!Constanti.STATO_VALIDO.equals(siacDSoggettoStato.getSoggettoStatoCode())
				&& !Constanti.STATO_IN_MODIFICA.equals(siacDSoggettoStato.getSoggettoStatoCode()))
		{
			esitoControlli.addErrore(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore(
					"soggetto " + siacTSoggetto.getSoggettoCode(), siacDSoggettoStato.getSoggettoStatoCode()));

			return esitoControlli;
		}
		
		// select by codice dove il code e' costante
		SiacDAmbitoFin siacDAmbitoPerCode = datiOperazioneDto.getSiacDAmbito();
		Integer idAmbito = siacDAmbitoPerCode.getAmbitoId();

		// classificazioni:
		Integer idSiacTSoggettoInModifica = siacTSoggetto.getSoggettoId();
		
		
		List<SiacRSoggettoClasseFin> listaValidi = siacRSoggettoClasseRepository.findValidiByIdSoggetto(idSiacTSoggettoInModifica, getNow());
		String[] classificazioniDaMantenere = null;
		//listaDaMantenere avra' tutte le classificazioni del soggetto tranne quella che si intende disassociare:
		boolean giaPresente = false;
		ArrayList<String> listaDaMantenere = new ArrayList<String>();
		if (listaValidi != null && listaValidi.size() > 0){
			for (SiacRSoggettoClasseFin iteratoOld : listaValidi){
				String codOld = iteratoOld.getSiacDSoggettoClasse().getSoggettoClasseCode();
				if(!codOld.equals(codiceClassificazione)){
					listaDaMantenere.add(codOld);
				} else {
					giaPresente = true;
				}
			}
		}
		
		if(giaPresente){
			Errore error = ErroreFin.WARNING_GENERICO.getErrore("Il soggetto e' gia' collegato alla classe");
			esitoControlli.addErrore(error);
		}
		
		return esitoControlli;
	}
	
	private SiacDSoggettoStatoFin getStatoSoggetto(SiacTSoggettoFin siacTSoggetto)
	{
		if (siacTSoggetto != null)
			for (SiacRSoggettoStatoFin siacRSoggettoStato : siacTSoggetto.getSiacRSoggettoStatos())
				if (siacRSoggettoStato.getDataFineValidita() == null && siacRSoggettoStato.getDataCancellazione()== null)
					return siacRSoggettoStato.getSiacDSoggettoStato();

		return null;
	}

	/**
	 * Il metodo e' idem-potente: se il soggetto risulta gia collegato non succede nulla
	 * @param codiceSoggetto
	 * @param codiceClassificazione
	 * @param codiceAmbito
	 * @param richiedente
	 * @param idEnte
	 * @param datiOperazioneDto
	 */
	public void aggiungiSoggettoAllaClassificazione(String codiceSoggetto,String codiceClassificazione, 
			String codiceAmbito, Richiedente richiedente, Integer idEnte, DatiOperazioneDto datiOperazioneDto){
		
		
		SiacTSoggettoFin siacTSoggetto = siacTSoggettoRepository.ricercaSoggettoNoSeSede(codiceAmbito, idEnte, codiceSoggetto, Constanti.SEDE_SECONDARIA, getNow());
		
		// select by codice dove il code e' costante
		SiacDAmbitoFin siacDAmbitoPerCode = datiOperazioneDto.getSiacDAmbito();
		Integer idAmbito = siacDAmbitoPerCode.getAmbitoId();
		

		// classificazioni:
		
		Integer idSiacTSoggettoInModifica = siacTSoggetto.getSoggettoId();
		
		
		List<SiacRSoggettoClasseFin> listaValidi = siacRSoggettoClasseRepository.findValidiByIdSoggetto(idSiacTSoggettoInModifica, getNow());
		String[] classificazioniDaMantenere = null;
		//listaDaMantenere avra' tutte le classificazioni del soggetto tranne quella che si intende disassociare:
		boolean giaPresente = false;
		ArrayList<String> listaDaMantenere = new ArrayList<String>();
		if (listaValidi != null && listaValidi.size() > 0){
			for (SiacRSoggettoClasseFin iteratoOld : listaValidi){
				String codOld = iteratoOld.getSiacDSoggettoClasse().getSoggettoClasseCode();
				if(!codOld.equals(codiceClassificazione)){
					listaDaMantenere.add(codOld);
				} else {
					giaPresente = true;
				}
			}
		}
		
		if(!giaPresente){
			
			//AGGIUNGO QUELLO NUOVO:
			listaDaMantenere.add(codiceClassificazione);
			
			if(listaDaMantenere.size()>0){
				classificazioniDaMantenere = listaDaMantenere.toArray(new String[listaDaMantenere.size()]);
			}
			
			datiOperazioneDto.setOperazione(Operazione.MODIFICA);
			updateClassificazioni(classificazioniDaMantenere, datiOperazioneDto, siacTSoggetto, idEnte,idAmbito);
			
		}
	}
	
	
	public void rimuoviSoggettoDaClassificazione(String codiceSoggetto,String codiceClassificazione, 
			String codiceAmbito, Richiedente richiedente, Integer idEnte, DatiOperazioneDto datiOperazioneDto){
		
		
		SiacTSoggettoFin siacTSoggetto = siacTSoggettoRepository.ricercaSoggettoNoSeSede(
				codiceAmbito, idEnte, codiceSoggetto, Constanti.SEDE_SECONDARIA, getNow());
		
		// select by codice dove il code e' costante
		SiacDAmbitoFin siacDAmbitoPerCode = datiOperazioneDto.getSiacDAmbito();
		Integer idAmbito = siacDAmbitoPerCode.getAmbitoId();
		

		// classificazioni:
		
		Integer idSiacTSoggettoInModifica = siacTSoggetto.getSoggettoId();
		
		
		List<SiacRSoggettoClasseFin> listaValidi = siacRSoggettoClasseRepository.findValidiByIdSoggetto(idSiacTSoggettoInModifica, getNow());
		String[] classificazioniDaMantenere = null;
		//listaDaMantenere avra' tutte le classificazioni del soggetto tranne quella che si intende disassociare:
		ArrayList<String> listaDaMantenere = new ArrayList<String>();
		if (listaValidi != null && listaValidi.size() > 0){
			for (SiacRSoggettoClasseFin iteratoOld : listaValidi){
				String codOld = iteratoOld.getSiacDSoggettoClasse().getSoggettoClasseCode();
				if(!codOld.equals(codiceClassificazione)){
					listaDaMantenere.add(codOld);
				}
			}
		}
		if(listaDaMantenere.size()>0){
			classificazioniDaMantenere = listaDaMantenere.toArray(new String[listaDaMantenere.size()]);
		}
		
		datiOperazioneDto.setOperazione(Operazione.MODIFICA);
		updateClassificazioni(classificazioniDaMantenere, datiOperazioneDto, siacTSoggetto, idEnte,idAmbito);
		
	}

	/**
	 * aggiornaSoggetto
	 * 
	 * @param idSiacTSoggettoInModifica
	 * @param soggettoDaModificare
	 * @param ente
	 * @param richiedente
	 * @param aggiornaSoloSedi
	 * @return
	 */
	public Soggetto aggiornaSoggetto(Integer idSiacTSoggettoInModifica,
			Soggetto soggettoDaModificare, String codiceAmbito, Ente ente, Richiedente richiedente,
			boolean aggiornaSoloSedi, DatiOperazioneDto datiOperazione)
	{
		List<ModalitaPagamentoSoggetto> listaDefenitivaMdp = new ArrayList<ModalitaPagamentoSoggetto>();
		long currMillisec = System.currentTimeMillis();
		Date dateInserimento = new Date(currMillisec);
		Timestamp timestampInserimento = new Timestamp(currMillisec);
		Operazione operazione = Operazione.MODIFICA;

		int idEnte = ente.getUid();

		// select by codice dove il code e' costante
		SiacDAmbitoFin siacDAmbitoPerCode = datiOperazione.getSiacDAmbito();
		Integer idAmbito = siacDAmbitoPerCode.getAmbitoId();

		if (controlloTipoSogg(soggettoDaModificare.getTipoSoggetto().getSoggettoTipoCode()))
		{
			soggettoDaModificare.setNome(soggettoDaModificare.getNome().toUpperCase());
			soggettoDaModificare.setCognome(soggettoDaModificare.getCognome().toUpperCase());
		}

		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository
				.findOne(idEnte);

		// quando aggiorno un record:
		DatiOperazioneDto datiOperazioneModifica = new DatiOperazioneDto(currMillisec, operazione,
				siacTEnteProprietario, siacDAmbitoPerCode, richiedente.getAccount().getId());
		// quando inserisco un nuovo record:
		Operazione operazioneInsert = Operazione.INSERIMENTO;
		DatiOperazioneDto datiOperazioneInserisci = new DatiOperazioneDto(currMillisec,
				operazioneInsert, siacTEnteProprietario, siacDAmbitoPerCode, richiedente
						.getAccount().getId());
		// quando cancello un record:
		Operazione operazioneCancella = Operazione.CANCELLAZIONE_LOGICA_RECORD;
		DatiOperazioneDto datiOperazioneCancella = new DatiOperazioneDto(currMillisec,
				operazioneCancella, siacTEnteProprietario, siacDAmbitoPerCode, richiedente
						.getAccount().getId());
		// /////////////

		SiacTSoggettoFin siacTSoggetto = null;
		siacTSoggetto = siacTSoggettoRepository.findOne(idSiacTSoggettoInModifica);

		if (!aggiornaSoloSedi)
		{ 
			// LOGIN INS/MDF DURC - SIAC-6874
			if (datiDurcModificati(siacTSoggetto, soggettoDaModificare)) {
				String loginOperazione = DatiOperazioneUtils.determinaUtenteLogin(datiOperazione, siacTAccountRepository);
				
				if (siacTSoggetto.getDataFineValiditaDurc() == null && 
					siacTSoggetto.getFonteDurc() == null &&
					org.apache.commons.lang.StringUtils.isBlank(siacTSoggetto.getNoteDurc())) {
					
					siacTSoggetto.setLoginInserimentoDurc(loginOperazione);
				}
				
				siacTSoggetto.setLoginModificaDurc(loginOperazione);
			}
			
			siacTSoggetto.setDataFineValiditaDurc(soggettoDaModificare.getDataFineValiditaDurc());
			siacTSoggetto.setTipoFonteDurc(soggettoDaModificare.getTipoFonteDurc());
			
			siacTSoggetto.setFonteDurc(soggettoDaModificare.getFonteDurcClassifId() != null ? 
					new SiacTClassFin(soggettoDaModificare.getFonteDurcClassifId()) 
					: null);
			
			siacTSoggetto.setNoteDurc(soggettoDaModificare.getNoteDurc());

			if (!StringUtils.isEmpty(soggettoDaModificare.getPartitaIva()))
			{
				String partitaIva = soggettoDaModificare.getPartitaIva().trim().toUpperCase();
				siacTSoggetto.setPartitaIva(partitaIva);
			}
			else
			{
				siacTSoggetto.setPartitaIva(null);
			}
			if (!StringUtils.isEmpty(soggettoDaModificare.getCodiceFiscale()))
			{
				String codFisc = soggettoDaModificare.getCodiceFiscale().trim().toUpperCase();
				siacTSoggetto.setCodiceFiscale(codFisc);
			}
			else
			{
				siacTSoggetto.setCodiceFiscale(null);
			}
			if (!StringUtils.isEmpty(soggettoDaModificare.getCodiceFiscaleEstero()))
			{
				String codFiscEst = soggettoDaModificare.getCodiceFiscaleEstero().trim()
						.toUpperCase();
				siacTSoggetto.setCodiceFiscaleEstero(codFiscEst);
			}
			else
			{
				siacTSoggetto.setCodiceFiscaleEstero(null);
			}

			String codiceTipo = (soggettoDaModificare.getTipoSoggetto().getCodice() != null) ? soggettoDaModificare
					.getTipoSoggetto().getCodice() : soggettoDaModificare.getTipoSoggetto()
					.getSoggettoTipoCode();

			boolean isPersonaFisicaDopoModifica = false;
			if (codiceTipo.trim().toUpperCase().startsWith(Constanti.PERSONA_FISICA))
			{
				isPersonaFisicaDopoModifica = true;
			}

			// prima della modifica era persona fisica o giuridica:
			SoggettoTipoDto soggettoTipoDto = soggettoDao
					.getPersonaFisicaOppureGiuridica(idSiacTSoggettoInModifica);
			boolean isPersonaFisicPrimaModifica = false;
			if (soggettoTipoDto.isPersonaFisica())
			{
				isPersonaFisicPrimaModifica = true;
			}
			// ///////////

			String soggettoDesc = buildSoggettoDesc(soggettoDaModificare,
					isPersonaFisicaDopoModifica);
			siacTSoggetto.setSoggettoDesc(soggettoDesc);

			siacTSoggetto.setDataInizioValidita(dateInserimento);
			siacTSoggetto = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTSoggetto,
					datiOperazioneModifica, siacTAccountRepository);
	
			
			
			//
			siacTSoggetto.setCanalePA(soggettoDaModificare.getCanalePA());
			siacTSoggetto.setCodDestinatario(soggettoDaModificare.getCodDestinatario());
			siacTSoggetto.setEmailPec(soggettoDaModificare.getEmailPec());
			
			// salvo sul db:
			siacTSoggetto = siacTSoggettoRepository.saveAndFlush(siacTSoggetto);

			if (isPersonaFisicPrimaModifica != isPersonaFisicaDopoModifica)
			{
				if (isPersonaFisicaDopoModifica)
				{
					// caso era giuridica e diventa fisica
					// invalido la persona giuridica:
					SiacTPersonaGiuridicaFin personaGiuridica = soggettoTipoDto
							.getSiactTpersonaGiuridica();
					personaGiuridica = DatiOperazioneUtils.cancellaRecord(personaGiuridica,
							siacTPersonaGiuridicaRepository, datiOperazioneModifica,
							siacTAccountRepository);
					// inserisco una persona fisica:
					SiacTPersonaFisicaFin personaFisica = new SiacTPersonaFisicaFin();
					personaFisica = savePersonaFisica(soggettoDaModificare, personaFisica,
							timestampInserimento, siacTSoggetto, datiOperazioneInserisci, false);
				}
				else
				{
					// caso era fisica e diventa giuridica
					// invalido la persona fisica:
					SiacTPersonaFisicaFin personaFisica = soggettoTipoDto.getSiactTpersonaFisica();
					personaFisica = DatiOperazioneUtils.cancellaRecord(personaFisica,
							siacTPersonaFisicaRepository, datiOperazioneModifica,
							siacTAccountRepository);
					// inserisco una persona giuridica:
					SiacTPersonaGiuridicaFin personaGiuridica = new SiacTPersonaGiuridicaFin();
					personaGiuridica = savePersonaGiuridica(soggettoDaModificare, personaGiuridica,
							siacTSoggetto, datiOperazioneInserisci);
				}
			}
			else
			{
				// caso resta dello stesso tipo
				if (isPersonaFisicaDopoModifica)
				{
					// PERSONA FISICA
					// invalido la vecchia occorrenza di persona fisica:
					SiacTPersonaFisicaFin personaFisicaDelete = soggettoTipoDto
							.getSiactTpersonaFisica();
					personaFisicaDelete = DatiOperazioneUtils.cancellaRecord(personaFisicaDelete,
							siacTPersonaFisicaRepository, datiOperazioneModifica,
							siacTAccountRepository);

					// inserisco una nuova occorrenza di persona fisica
					SiacTPersonaFisicaFin personaFisicaNew = EntityToEntityConverter
							.siacTPersonaFisicaToSiacTPersonaFisica(personaFisicaDelete);
					personaFisicaNew = savePersonaFisica(soggettoDaModificare, personaFisicaNew,
							timestampInserimento, siacTSoggetto, datiOperazioneInserisci, false);
				}
				else
				{
					// PERSONA GIURIDICA
					// invalido la vecchia occorrenza di persona giuridica
					SiacTPersonaGiuridicaFin personaGiuridicaDelete = soggettoTipoDto
							.getSiactTpersonaGiuridica();
					personaGiuridicaDelete = DatiOperazioneUtils.cancellaRecord(
							personaGiuridicaDelete, siacTPersonaGiuridicaRepository,
							datiOperazioneModifica, siacTAccountRepository);

					// inserisco una nuova occorrenza di persona giuridica
					SiacTPersonaGiuridicaFin personaGiuridicaNew = EntityToEntityConverter
							.siacTPersonaGiuridicaToSiacTPersonaGiuridica(personaGiuridicaDelete);
					personaGiuridicaNew = savePersonaGiuridica(soggettoDaModificare,
							personaGiuridicaNew, siacTSoggetto, datiOperazioneInserisci);
				}
			}

			// Soggetto tipo:
			TipoSoggetto tipoSoggetto = soggettoDaModificare.getTipoSoggetto();
			if (tipoSoggetto != null)
			{
				List<SiacRSoggettoTipoFin> listaSiacRSogg = siacRSoggettoTipoRepository
						.findValidoByIdSoggetto(idSiacTSoggettoInModifica,
								datiOperazioneModifica.getTs());
				// ci aspettiamo uno ed uno solo valido in listaSiacRSogg:
				if (listaSiacRSogg != null && listaSiacRSogg.size() > 0)
				{
					SiacRSoggettoTipoFin siacRSoggTipoOld = listaSiacRSogg.get(0);
					String oldCode = siacRSoggTipoOld.getSiacDSoggettoTipo().getSoggettoTipoCode();
					String newCode = tipoSoggetto.getCodice();
					if (!oldCode.equals(newCode))
					{
						// e' stato cambiato, occorre aggiornare i dati
						// invalido la relazione old:
						siacRSoggTipoOld = DatiOperazioneUtils.cancellaRecord(siacRSoggTipoOld,
								siacRSoggettoTipoRepository, datiOperazioneModifica,
								siacTAccountRepository);
						// inserisco un nuovo legame:
						saveSoggettoTipo(tipoSoggetto, idEnte, siacTSoggetto,
								datiOperazioneInserisci);
					}
				}
			}

			// natura giuridica:
			NaturaGiuridicaSoggetto naturaGiuridicaSogg = soggettoDaModificare
					.getNaturaGiuridicaSoggetto();
			if (null != naturaGiuridicaSogg
					&& !StringUtils.isEmpty(naturaGiuridicaSogg.getSoggettoTipoCode()))
			{
				// Per il soggetto modificato e' stata selezionata una natura
				// giuridica
				List<SiacRFormaGiuridicaFin> listRFormaGiuOld = siacRFormaGiuridicaRepository
						.findValidaByIdSoggetto(idSiacTSoggettoInModifica,
								datiOperazioneModifica.getTs());
				if (null != listRFormaGiuOld && listRFormaGiuOld.size() > 0)
				{
					// Il soggetto aveva gia' una natura giuridica associata,
					// verifico se cambiata e in caso affermativo la aggiorno
					// Dovrebbe venire restituito sempre un solo record valido
					SiacRFormaGiuridicaFin rFormaGiuOld = listRFormaGiuOld.get(0);
					SiacTFormaGiuridicaFin siacTFormaGiuridicaOld = rFormaGiuOld
							.getSiacTFormaGiuridica();
					String codeOld = siacTFormaGiuridicaOld.getFormaGiuridicaIstatCodice();
					String codeNew = naturaGiuridicaSogg.getSoggettoTipoCode();
					if (null != codeNew && !StringUtils.isEmpty(codeNew))
					{
						if (!codeOld.equals(codeNew))
						{
							// invalido record di relazione vecchio:
							rFormaGiuOld = DatiOperazioneUtils.cancellaRecord(rFormaGiuOld,
									siacRFormaGiuridicaRepository, datiOperazioneModifica,
									siacTAccountRepository);
							// inserisco nuovo record di relazione:
							SiacTFormaGiuridicaFin siacTFormaGiuridicaNew = siacTFormaGiuridicaRepository
									.findValidaByCode(idEnte, codeNew,
											datiOperazioneModifica.getTs()).get(0);
							saveRFormGiuridica(siacTFormaGiuridicaNew, siacTSoggetto,
									datiOperazioneInserisci);
						}
					}
				}
				else
				{
					// Il soggetto non aveva ancora una natura giuridica
					// associata
					// Inserisco la nuova natura giuridica selezionata a video
					String codeFg = naturaGiuridicaSogg.getSoggettoTipoCode();
					SiacTFormaGiuridicaFin siacTFormaGiuridica = siacTFormaGiuridicaRepository
							.findValidaByCode(idEnte, codeFg, datiOperazioneInserisci.getTs()).get(
									0);
					saveRFormGiuridica(siacTFormaGiuridica, siacTSoggetto, datiOperazioneInserisci);
				}
			}
			else
			{
				// Per il soggetto modificato non stata selezionata una natura
				// giuridica
				// Per il soggetto modificato non stata selezionata una natura
				// giuridica
				// Verifico se il soggetto aveva una natura giuridica associata
				List<SiacRFormaGiuridicaFin> listRFormaGiuOld = siacRFormaGiuridicaRepository
						.findValidaByIdSoggetto(idSiacTSoggettoInModifica,
								datiOperazioneModifica.getTs());
				if (null != listRFormaGiuOld && listRFormaGiuOld.size() > 0)
				{
					// Il soggetto aveva una natura giuridica associata, la
					// cancello logicamente
					SiacRFormaGiuridicaFin rFormaGiuOld = listRFormaGiuOld.get(0);
					rFormaGiuOld = DatiOperazioneUtils.cancellaRecord(rFormaGiuOld,
							siacRFormaGiuridicaRepository, datiOperazioneModifica,
							siacTAccountRepository);
				}
			}

			// classificazioni:
			String[] classificazioni = soggettoDaModificare.getTipoClassificazioneSoggettoId();
			updateClassificazioni(classificazioni, datiOperazioneModifica, siacTSoggetto, idEnte,
					idAmbito);

			// ONERI:
			String[] oneriCodes = soggettoDaModificare.getTipoOnereId();
			updateOneri(oneriCodes, datiOperazioneModifica, siacTSoggetto, idEnte, idAmbito);

			// indirizzi
			List<IndirizzoSoggetto> listaIndirizzi = soggettoDaModificare.getIndirizzi();
			aggiornamentoIndirizzi(listaIndirizzi, siacTSoggetto, datiOperazioneModifica);

			// modalita di pagamento
			List<ModalitaPagamentoSoggetto> listamodalita = soggettoDaModificare
					.getModalitaPagamentoList();
			List<SedeSecondariaSoggetto> sedi = soggettoDaModificare.getSediSecondarie();

			if (listamodalita != null && listamodalita.size() > 0)
			{
				ModalitaPagamentoInModificaInfoDto info = valutaModPagDaModificare(listamodalita,
						idSiacTSoggettoInModifica, datiOperazioneInserisci, idEnte, sedi);
				ArrayList<ModalitaPagamentoSoggetto> modalitaDaInserire = info
						.getModalitaDaInserire();
				ArrayList<ModalitaPagamentoSoggetto> modalitaDaModificare = info
						.getModalitaDaModificare();
				ArrayList<ModalitaPagamentoSoggetto> modalitaPagamentoCessioneToDelete = info
						.getModalitaPagamentoSoggettoCessioneDaEliminare();
				ArrayList<SiacTModpagFin> modalitaDaEliminare = info.getModalitaDaEliminare();

				// Inserimento Nuovi
				if (modalitaDaInserire != null && modalitaDaInserire.size() > 0)
				{
					for (ModalitaPagamentoSoggetto modPagToInsert : modalitaDaInserire)
					{
						modPagToInsert = saveModalitaPagamento(modPagToInsert, siacTSoggetto,
								codiceAmbito, idEnte, datiOperazioneInserisci);
						listaDefenitivaMdp.add(modPagToInsert);
					}
				}

				// Modifico modalita
				if (modalitaDaModificare != null && modalitaDaModificare.size() > 0)
				{
					for (ModalitaPagamentoSoggetto modPagToUpdate : modalitaDaModificare)
					{
						updateModalitaDiPagamento(modPagToUpdate, siacTSoggetto, idEnte,
								datiOperazioneInserisci);
						listaDefenitivaMdp.add(modPagToUpdate);
					}
				}

				// Elimino modalita
				if (modalitaDaEliminare != null && modalitaDaEliminare.size() > 0)
				{
					for (SiacTModpagFin modPagToDelete : modalitaDaEliminare)
					{
						eliminaModalitaPagamento(modPagToDelete, datiOperazioneCancella);
					}
				}

				if (modalitaPagamentoCessioneToDelete != null
						&& modalitaPagamentoCessioneToDelete.size() > 0)
				{
					for (ModalitaPagamentoSoggetto mdpAppToDelete : modalitaPagamentoCessioneToDelete)
					{
						eliminaModalitaPagamentoCessione(mdpAppToDelete, datiOperazioneCancella);
					}
				}
			}
			else
			{
				List<SiacTModpagFin> modpagList = siacTModpagRepository.findValidiByIdSoggetto(
						idSiacTSoggettoInModifica, timestampInserimento);
				if (modpagList != null && modpagList.size() > 0)
				{
					for (SiacTModpagFin modpagToDelete : modpagList)
					{
						eliminaModalitaPagamento(modpagToDelete, datiOperazioneCancella);
					}
				}

				Timestamp now = new Timestamp(System.currentTimeMillis());

				List<SiacTModpagFin> listaTModpagsCessioni = siacTSoggettoRepository
						.ricercaModPagCessioni(idEnte, idSiacTSoggettoInModifica, now);
				List<ModalitaPagamentoSoggetto> listaModPagsCessioni = null;
				if (listaTModpagsCessioni != null)
				{
					listaModPagsCessioni = convertiLista(listaTModpagsCessioni,
							ModalitaPagamentoSoggetto.class,
							FinMapId.SiacTModPag_ModalitaPagamentoSoggetto);

					// TODO capire se aggiungere il parametro aggiuntivo
					// SiacTSoggettoFin
					listaModPagsCessioni = modalitaPagamentoEntityToModalitaPagamentoCessioniModel(
							null, listaTModpagsCessioni, listaModPagsCessioni,
							idSiacTSoggettoInModifica, "", now);
					for (ModalitaPagamentoSoggetto mdpAppToDelete : listaModPagsCessioni)
					{
						eliminaModalitaPagamentoCessione(mdpAppToDelete, datiOperazioneCancella);
					}
				}

			}

			// recapiti:
			List<Contatto> listaContatti = soggettoDaModificare.getContatti();
			aggiornaContatti(listaContatti, datiOperazioneModifica, siacTSoggetto, idEnte);

			// Inserimeto delle NOTE relative al soggetto
			SiacRSoggettoAttrFin s = new SiacRSoggettoAttrFin();
			s.setSiacTSoggetto(siacTSoggetto);

			// NOTE:
			AttributoTClassInfoDto attributoInfo = new AttributoTClassInfoDto();
			attributoInfo.setSiacTSoggetto(siacTSoggetto);
			attributoInfo.setTipoOggetto(OggettoDellAttributoTClass.T_SOGGETTO);

			String note = soggettoDaModificare.getNote();
			if (note != null)
			{
				note = note.toUpperCase();
			}

			salvaAttributoTAttr(attributoInfo, datiOperazioneModifica, note,
					Constanti.T_ATTR_CODE_NOTE_SOGGETTO);

			// MATRICOLA
			AttributoTClassInfoDto attributoInfoMatricola = new AttributoTClassInfoDto();
			attributoInfoMatricola.setSiacTSoggetto(siacTSoggetto);
			attributoInfoMatricola.setTipoOggetto(OggettoDellAttributoTClass.T_SOGGETTO);
			String matricola = soggettoDaModificare.getMatricola();
			if (matricola != null)
			{
				matricola = matricola.toUpperCase();
			}
			salvaAttributoTAttr(attributoInfo, datiOperazioneModifica, matricola,
					Constanti.T_ATTR_CODE_MATRICOLA);

		}
		else
		{
			// SEDI SECONDARIE:
			gestisciSediSecondarie(siacTSoggetto, richiedente, datiOperazioneInserisci, false,
					soggettoDaModificare.getSediSecondarie(),
					soggettoDaModificare.getCodiceSoggetto());
		}

		entityRefresh(siacTSoggetto);
		// RITORNO RISULTATI:
		Soggetto soggetto = new Soggetto();
		soggetto.setCodiceSoggetto(siacTSoggetto.getSoggettoCode());
		soggetto = EntityToModelConverter.soggettoEntityToSoggettoModel(siacTSoggetto, soggetto);
		soggetto.setModalitaPagamentoList(listaDefenitivaMdp);
		// Termino restituendo l'oggetto di ritorno:
		return soggetto;
	}

	/**
	 * aggiornamentoIndirizzi
	 * 
	 * @param listaIndirizzi
	 * @param siacTSoggetto
	 * @param datiOperazione
	 */
	private void aggiornamentoIndirizzi(List<IndirizzoSoggetto> listaIndirizzi,
			SiacTSoggettoFin siacTSoggetto, DatiOperazioneDto datiOperazione)
	{
		Integer idSiacTSoggettoInModifica = siacTSoggetto.getSoggettoId();
		DatiOperazioneDto datiOperazioneModifica = DatiOperazioneDto.buildDatiOperazione(
				datiOperazione, Operazione.MODIFICA);
		DatiOperazioneDto datiOperazioneInserisci = DatiOperazioneDto.buildDatiOperazione(
				datiOperazione, Operazione.INSERIMENTO);
		Integer idEnte = datiOperazione.getSiacTEnteProprietario().getEnteProprietarioId();

		if (listaIndirizzi != null && listaIndirizzi.size() > 0)
		{
			IndirizziInModificaInfoDto info = valutaIndirizziDaModificare(listaIndirizzi,
					idSiacTSoggettoInModifica);
			ArrayList<IndirizzoSoggetto> indirizziDaInserire = info.getIndirizziDaInserire();
			ArrayList<IndirizzoSoggetto> indirizziDaModificare = info.getIndirizziDaModificare();
			ArrayList<SiacTIndirizzoSoggettoFin> indirizziDaEliminare = info.getIndirizziDaEliminare();
			// 1. Inseriamo quelli nuovi:
			if (indirizziDaInserire != null && indirizziDaInserire.size() > 0)
			{
				for (IndirizzoSoggetto indirizzoIterato : indirizziDaInserire)
				{
					SiacTIndirizzoSoggettoFin inserito = saveIndirizzo(indirizzoIterato,
							siacTSoggetto, idEnte, datiOperazioneInserisci);
					entityRefresh(inserito);
				}
			}
			// 2. Modifica esistenti:
			if (indirizziDaModificare != null && indirizziDaModificare.size() > 0)
			{
				for (IndirizzoSoggetto indirizzoIterato : indirizziDaModificare)
				{
					updateIndirizzo(indirizzoIterato, siacTSoggetto, idEnte, datiOperazioneModifica);
				}
			}
			// 3. Invalido gli indirizzi da eliminare:
			if (indirizziDaEliminare != null && indirizziDaEliminare.size() > 0)
			{
				for (SiacTIndirizzoSoggettoFin indirizzoIterato : indirizziDaEliminare)
				{
					eliminaIndirizzo(indirizzoIterato, datiOperazioneModifica);
				}
			}
		}
	}

	/**
	 * aggiornamentoIndirizziMod
	 * 
	 * @param listaIndirizzi
	 * @param siacTSoggetto
	 * @param datiOperazione
	 */
	private void aggiornamentoIndirizziMod(List<IndirizzoSoggetto> listaIndirizzi,
			SiacTSoggettoModFin siacTSoggetto, DatiOperazioneDto datiOperazione)
	{
		Integer idSiacTSoggettoInModifica = siacTSoggetto.getSogModId();
		DatiOperazioneDto datiOperazioneModifica = DatiOperazioneDto.buildDatiOperazione(
				datiOperazione, Operazione.MODIFICA);
		DatiOperazioneDto datiOperazioneInserisci = DatiOperazioneDto.buildDatiOperazione(
				datiOperazione, Operazione.INSERIMENTO);
		Integer idEnte = datiOperazione.getSiacTEnteProprietario().getEnteProprietarioId();

		if (listaIndirizzi != null && listaIndirizzi.size() > 0)
		{
			IndirizziModInModificaInfoDto info = valutaIndirizziModDaModificare(listaIndirizzi,
					idSiacTSoggettoInModifica);
			ArrayList<IndirizzoSoggetto> indirizziDaInserire = info.getIndirizziDaInserire();
			// ArrayList<IndirizzoSoggetto> indirizziDaModificare =
			// info.getIndirizziDaModificare();
			ArrayList<SiacTIndirizzoSoggettoModFin> indirizziDaEliminare = info
					.getIndirizziDaEliminare();
			// 1. Inseriamo quelli nuovi:
			if (indirizziDaInserire != null && indirizziDaInserire.size() > 0)
			{
				for (IndirizzoSoggetto indirizzoIterato : indirizziDaInserire)
				{
					saveIndirizzoMod(indirizzoIterato, siacTSoggetto.getSiacTSoggetto(),
							siacTSoggetto, idEnte, datiOperazioneInserisci);
				}
			}
			// 2. Modifica esistenti:
			// if(indirizziDaModificare!=null &&
			// indirizziDaModificare.size()>0){
			// for(IndirizzoSoggetto indirizzoIterato: indirizziDaModificare){
			// log.dbug(, "Dopo updateIndirizzoMod() ");
			// }
			// }
			// 3. Invalido gli indirizzi da eliminare:
			if (indirizziDaEliminare != null && indirizziDaEliminare.size() > 0)
			{
				for (SiacTIndirizzoSoggettoModFin indirizzoIterato : indirizziDaEliminare)
				{
					eliminaIndirizzoMod(indirizzoIterato, datiOperazioneModifica);
				}
			}
		}
	}

	/**
	 * aggiornaContatti
	 * 
	 * @param listaContatti
	 * @param datiOperazione
	 * @param siacTSoggetto
	 * @param idEnte
	 */
	private void aggiornaContatti(List<Contatto> listaContatti, DatiOperazioneDto datiOperazione,
			SiacTSoggettoFin siacTSoggetto, Integer idEnte)
	{
		Integer idSiacTSoggetto = siacTSoggetto.getSoggettoId();
		DatiOperazioneDto datiOperazioneModifica = DatiOperazioneDto.buildDatiOperazione(
				datiOperazione, Operazione.MODIFICA);
		DatiOperazioneDto datiOperazioneInserisci = DatiOperazioneDto.buildDatiOperazione(
				datiOperazione, Operazione.INSERIMENTO);
		ContattiInModificaInfoDto info = valutaContattiDaModificare(listaContatti, idSiacTSoggetto);
		ArrayList<Contatto> listaContattiDaSalvare = info.getListaContattiNuovi();
		ArrayList<ContattoModificatoDto> listaModificati = info.getListaModificati();
		ArrayList<SiacTRecapitoSoggettoFin> listaDaInvalidare = info.getListaDaInvalidare();
		// 1. contatti da invalidare:
		if (listaDaInvalidare != null && listaDaInvalidare.size() > 0)
		{
			for (SiacTRecapitoSoggettoFin daInvalidare : listaDaInvalidare)
			{
				datiOperazioneModifica.setCurrMillisec(System.currentTimeMillis());
				DatiOperazioneUtils.cancellaRecord(daInvalidare, siacTRecapitoSoggettoRepository,
						datiOperazioneModifica, siacTAccountRepository);
			}
		}
		// 2. contatti da salvare:
		for (Contatto contattoIterato : listaContattiDaSalvare)
		{
			if (!StringUtils.isEmpty(contattoIterato.getDescrizione()))
			{
				datiOperazioneInserisci.setCurrMillisec(System.currentTimeMillis());
				salvaContatto(contattoIterato, siacTSoggetto, idEnte, datiOperazioneInserisci, null);
			}
		}
		// 3. contatti da aggiornare:
		for (ContattoModificatoDto conInfoIt : listaModificati)
		{
			Integer idContattoDaAggiornare = conInfoIt.getDaModificareSulDb().getRecapitoId();
			datiOperazioneInserisci.setCurrMillisec(System.currentTimeMillis());
			salvaContatto(conInfoIt.getContattoFromFrontEnd(), siacTSoggetto, idEnte,
					datiOperazioneModifica, idContattoDaAggiornare);
		}
	}

	/**
	 * aggiornaContattiMod
	 * 
	 * @param listaContatti
	 * @param datiOperazione
	 * @param siacTSoggettoMod
	 * @param idEnte
	 */
	private void aggiornaContattiMod(List<Contatto> listaContatti,
			DatiOperazioneDto datiOperazione, SiacTSoggettoModFin siacTSoggettoMod, Integer idEnte)
	{
		Integer idSiacTSoggettoMod = siacTSoggettoMod.getSogModId();
		DatiOperazioneDto datiOperazioneModifica = DatiOperazioneDto.buildDatiOperazione(
				datiOperazione, Operazione.MODIFICA);
		DatiOperazioneDto datiOperazioneInserisci = DatiOperazioneDto.buildDatiOperazione(
				datiOperazione, Operazione.INSERIMENTO);
		ContattiModInModificaInfoDto info = valutaContattiDaModificareMod(listaContatti,
				idSiacTSoggettoMod);
		ArrayList<Contatto> listaContattiDaSalvare = info.getListaContattiNuovi();
		ArrayList<ContattoModModificatoDto> listaModificati = info.getListaModificati();
		ArrayList<SiacTRecapitoSoggettoModFin> listaDaInvalidare = info.getListaDaInvalidare();
		// 1. contatti da invalidare:
		if (listaDaInvalidare != null && listaDaInvalidare.size() > 0)
		{
			for (SiacTRecapitoSoggettoModFin daInvalidare : listaDaInvalidare)
			{
				datiOperazioneModifica.setCurrMillisec(System.currentTimeMillis());
				DatiOperazioneUtils.cancellaRecord(daInvalidare,
						siacTRecapitoSoggettoModRepository, datiOperazioneModifica,
						siacTAccountRepository);
			}
		}
		// 2. contatti da inserire:
		for (Contatto contattoIterato : listaContattiDaSalvare)
		{
			if (!StringUtils.isEmpty(contattoIterato.getDescrizione()))
			{
				datiOperazioneInserisci.setCurrMillisec(System.currentTimeMillis());
				salvaContattoMod(contattoIterato, siacTSoggettoMod.getSiacTSoggetto(),
						siacTSoggettoMod, idEnte, datiOperazioneInserisci, null);
			}
		}
		// 3. contatti da aggiornare:
		for (ContattoModModificatoDto conInfoIt : listaModificati)
		{
			Integer idContattoDaAggiornare = conInfoIt.getDaModificareSulDb().getRecapitoModId();
			datiOperazioneInserisci.setCurrMillisec(System.currentTimeMillis());
			salvaContattoMod(conInfoIt.getContattoFromFrontEnd(),
					siacTSoggettoMod.getSiacTSoggetto(), siacTSoggettoMod, idEnte,
					datiOperazioneInserisci, idContattoDaAggiornare);
		}
	}

	/**
	 * inserisciSoggettoMod
	 * 
	 * @param idSoggetto
	 * @param soggettoDaModificare
	 * @param richiedente
	 * @param ente
	 * @param aggiornaSoloSedi
	 * @return
	 */
	public Soggetto inserisciSoggettoMod(Integer idSoggetto, Soggetto soggettoDaModificare,
			Richiedente richiedente, String codiceAmbito, Ente ente, boolean aggiornaSoloSedi,
			DatiOperazioneDto datiOperazione)
	{
		InserisciSoggettoModDto checkMod = isSoggettoModificato(idSoggetto, soggettoDaModificare,
				richiedente, ente);

		boolean soggettoModificato = checkMod.isModificato();

		boolean eseguimodpag = isModPagModificate(idSoggetto,
				soggettoDaModificare.getModalitaPagamentoList(),
				soggettoDaModificare.getSediSecondarie(), richiedente, ente);

		SiacTSoggettoFin siacTSoggettoOriginale = checkMod.getSiacTSoggettoOriginale();
		DatiOperazioneDto datiOperazioneInserisci = checkMod.getDatiOperazioneInserisci();

		datiOperazioneInserisci.setSiacDAmbito(datiOperazione.getSiacDAmbito());

		Soggetto soggetto = new Soggetto();
		boolean eseguiSedi = true;
		if (soggettoModificato)
		{
			if (StatoOperativoAnagrafica.PROVVISORIO.name().equals(
					checkMod.getStato().getSiacDSoggettoStato().getSoggettoStatoCode()))
			{
				soggetto = aggiornaSoggetto(idSoggetto, soggettoDaModificare, codiceAmbito, ente,
						richiedente, aggiornaSoloSedi, datiOperazione);
				eseguiSedi = false;
			}
			else
			{
				soggetto = inserisciSoggettoModSaveRoutine(idSoggetto, soggettoDaModificare,
						richiedente, ente, soggettoModificato, datiOperazione);
			}
		}
		else
		{
			soggetto.setCodiceSoggetto(checkMod.getSoggettoCode());
			soggetto = EntityToModelConverter.soggettoEntityToSoggettoModel(siacTSoggettoOriginale,
					soggetto);
		}
		// SEDI SECONDARIE:
		if (eseguiSedi)
		{
			gestisciSediSecondarie(siacTSoggettoOriginale, richiedente, datiOperazioneInserisci,
					true, soggettoDaModificare.getSediSecondarie(),
					soggettoDaModificare.getCodiceSoggetto());
		}
		// MODALITA' PAGAMENTO
		if (eseguimodpag)
		{
			gestisciModPagMod(richiedente, codiceAmbito, ente, siacTSoggettoOriginale,
					datiOperazioneInserisci, soggettoDaModificare.getModalitaPagamentoList(),
					soggettoDaModificare.getSediSecondarie());
		}

		// Termino restituendo l'oggetto di ritorno:
		return soggetto;

	}

	/**
	 * inserisciSoggettoModSaveRoutine
	 * 
	 * @param idSiacTSoggettoInModifica
	 * @param soggettoDaModificare
	 * @param richiedente
	 * @param ente
	 * @param soggettoModificato
	 * @return
	 */
	private Soggetto inserisciSoggettoModSaveRoutine(Integer idSiacTSoggettoInModifica,
			Soggetto soggettoDaModificare, Richiedente richiedente, Ente ente,
			boolean soggettoModificato, DatiOperazioneDto datiOperazioneDto)
	{

		long currMillisec = System.currentTimeMillis();
		Date dateInserimento = new Date(currMillisec);
		Timestamp timestampInserimento = new Timestamp(currMillisec);
		Operazione operazione = Operazione.MODIFICA;

		int idEnte = ente.getUid();

		SiacDAmbitoFin siacDAmbitoPerCode = datiOperazioneDto.getSiacDAmbito();
		Integer idAmbito = siacDAmbitoPerCode.getAmbitoId();

		if (controlloTipoSogg(soggettoDaModificare.getTipoSoggetto().getSoggettoTipoCode()))
		{
			soggettoDaModificare.setNome(soggettoDaModificare.getNome().toUpperCase());
			soggettoDaModificare.setCognome(soggettoDaModificare.getCognome().toUpperCase());
		}

		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository
				.findOne(idEnte);

		// quando aggiorno un record:
		DatiOperazioneDto datiOperazioneModifica = new DatiOperazioneDto(currMillisec, operazione,
				siacTEnteProprietario, richiedente.getAccount().getId());

		// quando inserisco un nuovo record:
		Operazione operazioneInsert = Operazione.INSERIMENTO;
		DatiOperazioneDto datiOperazioneInserisci = new DatiOperazioneDto(currMillisec,
				operazioneInsert, siacTEnteProprietario, richiedente.getAccount().getId());

		// /////////////

		SiacTSoggettoModFin siacTSoggettoMod = null;

		SiacTSoggettoFin siacTSoggettoOriginale = siacTSoggettoRepository
				.findOne(idSiacTSoggettoInModifica);

		siacTSoggettoMod = EntityToEntityConverter
				.siacTSoggettoToSiacTSoggettoMod(siacTSoggettoOriginale);

		
		siacTSoggettoMod.setDataFineValiditaDurc(soggettoDaModificare.getDataFineValiditaDurc());
		siacTSoggettoMod.setTipoFonteDurc(soggettoDaModificare.getTipoFonteDurc());
		siacTSoggettoMod.setFonteDurc(soggettoDaModificare.getFonteDurcClassifId() != null ?
				new SiacTClassFin(soggettoDaModificare.getFonteDurcClassifId())
				: null);
		siacTSoggettoMod.setNoteDurc(soggettoDaModificare.getNoteDurc());
		
		
		if (!StringUtils.isEmpty(soggettoDaModificare.getPartitaIva()))
		{
			String partitaIva = soggettoDaModificare.getPartitaIva().trim().toUpperCase();
			siacTSoggettoMod.setPartitaIva(partitaIva);
		}
		if (!StringUtils.isEmpty(soggettoDaModificare.getCodiceFiscale()))
		{
			String codFisc = soggettoDaModificare.getCodiceFiscale().trim().toUpperCase();
			siacTSoggettoMod.setCodiceFiscale(codFisc);
		}
		if (!StringUtils.isEmpty(soggettoDaModificare.getCodiceFiscaleEstero()))
		{
			String codFiscEst = soggettoDaModificare.getCodiceFiscaleEstero().trim().toUpperCase();
			siacTSoggettoMod.setCodiceFiscaleEstero(codFiscEst);
		}

		// SIAC-6565-CR1215
		if (!StringUtils.isEmpty(soggettoDaModificare.getCanalePA()))
		{
			siacTSoggettoMod.setCanalePA(soggettoDaModificare.getCanalePA());
		}
		
		if (!StringUtils.isEmpty(soggettoDaModificare.getCodDestinatario()))
		{
			siacTSoggettoMod.setCodDestinatario(soggettoDaModificare.getCodDestinatario());
		}
		
		if (!StringUtils.isEmpty(soggettoDaModificare.getEmailPec()))
		{
			siacTSoggettoMod.setEmailPec(soggettoDaModificare.getEmailPec());
		}
		
		String codiceTipo = soggettoDaModificare.getTipoSoggetto().getCodice();

		boolean isPersonaFisicaDopoModifica = false;
		if (codiceTipo.trim().toUpperCase().startsWith(Constanti.PERSONA_FISICA))
		{
			isPersonaFisicaDopoModifica = true;
		}

		// prima della modifica era persona fisica o giuridica:
		SoggettoTipoDto soggettoTipoDto = soggettoDao
				.getPersonaFisicaOppureGiuridica(idSiacTSoggettoInModifica);
		boolean isPersonaFisicPrimaModifica = false;
		if (soggettoTipoDto.isPersonaFisica())
		{
			isPersonaFisicPrimaModifica = true;
		}
		// ///////////

		String soggettoDesc = buildSoggettoDesc(soggettoDaModificare, isPersonaFisicaDopoModifica);
		siacTSoggettoMod.setSoggettoDesc(soggettoDesc);

		siacTSoggettoMod.setDataInizioValidita(dateInserimento);
		siacTSoggettoMod = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTSoggettoMod,
				datiOperazioneInserisci, siacTAccountRepository);

		if (!soggettoModificato)
		{
			soggettoModificato = isModificato(siacTSoggettoOriginale, siacTSoggettoMod);
		}
		// salvo sul db:
		siacTSoggettoMod = siacTSoggettoModRepository.saveAndFlush(siacTSoggettoMod);

		// codice per verificare se ci sono modifiche rispetto alla persona
		// fisica/giuridica:
		if (!soggettoModificato)
		{
			if (isPersonaFisicaDopoModifica != isPersonaFisicPrimaModifica)
			{
				soggettoModificato = true;
			}
		}

		if (isPersonaFisicaDopoModifica)
		{
			// PERSONA FISICA
			SiacTPersonaFisicaModFin personaFisicaMod = new SiacTPersonaFisicaModFin();
			personaFisicaMod = savePersonaFisicaMod(soggettoDaModificare, personaFisicaMod,
					timestampInserimento, siacTSoggettoMod, datiOperazioneInserisci);
			if (!soggettoModificato)
			{
				soggettoModificato = isModificato(soggettoTipoDto.getSiactTpersonaFisica(),
						personaFisicaMod);
			}
		}
		else
		{
			// PERSONA GIURIDICA
			SiacTPersonaGiuridicaModFin personaGiuridicaMod = new SiacTPersonaGiuridicaModFin();
			personaGiuridicaMod = savePersonaGiuridicaMod(soggettoDaModificare,
					personaGiuridicaMod, siacTSoggettoOriginale, siacTSoggettoMod,
					datiOperazioneInserisci);
			if (!soggettoModificato)
			{
				soggettoModificato = isModificato(soggettoTipoDto.getSiactTpersonaGiuridica(),
						personaGiuridicaMod);
			}
		}

		// Soggetto tipo:
		// insiste sulla tabella siac_r_soggetto_tipo_mod
		TipoSoggetto tipoSoggetto = soggettoDaModificare.getTipoSoggetto();
		if (tipoSoggetto != null)
		{
			// integrazione con il codice di claudio
			SiacRSoggettoTipoModFin siacRSoggettoTipoMod = saveSoggettoTipoMod(tipoSoggetto, idEnte,
					siacTSoggettoMod, datiOperazioneInserisci);
			if (!soggettoModificato)
			{
				soggettoModificato = isModificatoSoggettoTipo(siacTSoggettoOriginale,
						siacRSoggettoTipoMod);
			}
		}

		// natura giuridica:
		// insiste sulla tabella siac_r_forma_giuridica_tipo_mod
		NaturaGiuridicaSoggetto naturaGiuridicaSogg = soggettoDaModificare
				.getNaturaGiuridicaSoggetto();
		if (naturaGiuridicaSogg != null
				&& !StringUtils.isEmpty(naturaGiuridicaSogg.getSoggettoTipoCode()))
		{
			String codeFg = naturaGiuridicaSogg.getSoggettoTipoCode();
			SiacTFormaGiuridicaFin siacTFormaGiuridica = siacTFormaGiuridicaRepository
					.findValidaByCode(idEnte, codeFg, datiOperazioneModifica.getTs()).get(0);
			SiacRFormaGiuridicaModFin rFormaGiuridicaMod = saveRFormGiuridicaMod(siacTFormaGiuridica,
					siacTSoggettoMod, datiOperazioneInserisci);
			if (!soggettoModificato)
			{
				soggettoModificato = isModificatoFormaGiuridica(siacTSoggettoOriginale,
						rFormaGiuridicaMod);
			}
		}

		// classe:
		String[] classificazioni = soggettoDaModificare.getTipoClassificazioneSoggettoId();
		if (!soggettoModificato)
		{
			ClassificazioniInModificaInfoDto classificazioniInModificaInfoDto = valutaClassificazioniDaModificare(
					classificazioni, idSiacTSoggettoInModifica);
			soggettoModificato = !classificazioniInModificaInfoDto.isRimasteUguali();
		}
		if (classificazioni != null && classificazioni.length > 0)
		{
			for (String codeClass : classificazioni)
			{
				if (!StringUtils.isEmpty(codeClass))
				{
					salvaSoggettoClasseMod(siacTSoggettoOriginale, siacTSoggettoMod, idEnte,
							idAmbito, codeClass, datiOperazioneInserisci);
				}
			}
		}

		// ONERI:
		String[] oneriCodes = soggettoDaModificare.getTipoOnereId();
		if (!soggettoModificato)
		{
			OneriInModificaInfoDto oneriInModificaInfoDto = valutaOneriDaModificare(oneriCodes,
					idSiacTSoggettoInModifica);
			soggettoModificato = !oneriInModificaInfoDto.isRimastiUguali();
		}
		if (oneriCodes != null && oneriCodes.length > 0)
		{
			for (String codeOnere : oneriCodes)
			{
				salvaOnereMod(siacTSoggettoOriginale, siacTSoggettoMod, idEnte, codeOnere,
						datiOperazioneInserisci);
			}
		}

		// indirizzi
		List<IndirizzoSoggetto> listaIndirizzi = soggettoDaModificare.getIndirizzi();
		if (!soggettoModificato)
		{
			IndirizziInModificaInfoDto indInMod = valutaIndirizziDaModificare(listaIndirizzi,
					idSiacTSoggettoInModifica);
			soggettoModificato = !indInMod.isRimastiUguali();
		}
		if (listaIndirizzi != null && listaIndirizzi.size() > 0)
		{
			for (IndirizzoSoggetto indirizzoIterato : listaIndirizzi)
			{
				saveIndirizzoMod(indirizzoIterato, siacTSoggettoOriginale, siacTSoggettoMod,
						idEnte, datiOperazioneInserisci);
			}
		}

		// recapiti:
		List<Contatto> listaContatti = soggettoDaModificare.getContatti();
		if (!soggettoModificato)
		{
			ContattiInModificaInfoDto contInMod = valutaContattiDaModificare(listaContatti,
					idSiacTSoggettoInModifica);
			soggettoModificato = !contInMod.isRimastiUguali();
		}
		if (listaContatti != null && listaContatti.size() > 0)
		{
			for (Contatto contattoIterato : listaContatti)
			{
				salvaContattoMod(contattoIterato, siacTSoggettoOriginale, siacTSoggettoMod, idEnte,
						datiOperazioneInserisci, null);
			}
		}

		// INSERIMENTO DELLE NOTE
		// Controllo modifica note
		AttributoTClassInfoDto attributoInfo = new AttributoTClassInfoDto();
		attributoInfo.setSiacTSoggetto(siacTSoggettoOriginale);
		attributoInfo.setTipoOggetto(OggettoDellAttributoTClass.T_SOGGETTO);// si
																			// confronta
																			// per
																			// capire
																			// se
																			// modificato
																			// su
																			// SOGGETTO
																			// (non
																			// soggetto
																			// mod)
		boolean noteMod = isAttributoTAttrModificato(attributoInfo, datiOperazioneInserisci,
				soggettoDaModificare.getNote(), Constanti.T_ATTR_CODE_NOTE_SOGGETTO);
		if (noteMod)
		{
			soggettoModificato = true;
		}
		// NOTE, si inserisce sulla tabella MOD:
		AttributoTClassInfoDto attributoInfoMod = new AttributoTClassInfoDto();
		attributoInfoMod.setSiacTSoggettoMod(siacTSoggettoMod);
		attributoInfoMod.setTipoOggetto(OggettoDellAttributoTClass.T_SOGGETTO_MOD);
		salvaAttributoTAttr(attributoInfoMod, datiOperazioneInserisci,
				soggettoDaModificare.getNote(), Constanti.T_ATTR_CODE_NOTE_SOGGETTO);

		// MATRICOLA
		boolean matricolaMod = isAttributoTAttrModificato(attributoInfo, datiOperazioneInserisci,
				soggettoDaModificare.getMatricola(), Constanti.T_ATTR_CODE_MATRICOLA);
		if (matricolaMod)
		{
			soggettoModificato = true;
		}

		// MATRICOLA, si inserisce sulla tabella MOD:
		AttributoTClassInfoDto attributoMatricolaMod = new AttributoTClassInfoDto();
		attributoMatricolaMod.setSiacTSoggettoMod(siacTSoggettoMod);
		attributoMatricolaMod.setTipoOggetto(OggettoDellAttributoTClass.T_SOGGETTO_MOD);
		salvaAttributoTAttr(attributoInfoMod, datiOperazioneInserisci,
				soggettoDaModificare.getMatricola(), Constanti.T_ATTR_CODE_MATRICOLA);

		// RITORNO RISULTATI:
		Soggetto soggetto = new Soggetto();
		soggetto.setCodiceSoggetto(siacTSoggettoMod.getSoggettoCode());
		soggetto = EntityToModelConverter.soggettoEntityToSoggettoModel(siacTSoggettoOriginale,
				soggetto);
		// Termino restituendo l'oggetto di ritorno:
		return soggetto;
	}

	/**
	 * gestisciModPagMod
	 * 
	 * @param richiedente
	 * @param ente
	 * @param siacTSoggettoOriginale
	 * @param datiOperazioneModifica
	 * @param listamodalita
	 * @param sedi
	 */
	private void gestisciModPagMod(Richiedente richiedente, String codiceAmbito, Ente ente,
			SiacTSoggettoFin siacTSoggettoOriginale, DatiOperazioneDto datiOperazioneModifica,
			List<ModalitaPagamentoSoggetto> listamodalita, List<SedeSecondariaSoggetto> sedi)
	{
		long currMillisec = System.currentTimeMillis();
		Timestamp timestampInserimento = new Timestamp(currMillisec);
		datiOperazioneModifica.setOperazione(Operazione.MODIFICA);
		int idEnte = ente.getUid();

		ModalitaPagamentoInModificaInfoDto info = valutaModPagDaModificare(listamodalita,
				siacTSoggettoOriginale.getUid(), datiOperazioneModifica, idEnte, sedi);
		ArrayList<ModalitaPagamentoSoggetto> modalitaDaInserire = info.getModalitaDaInserire();
		ArrayList<ModalitaPagamentoSoggetto> modalitaDaModificare = info.getModalitaDaModificare();
		ArrayList<ModalitaPagamentoSoggetto> modalitaPagamentoCessioneToDelete = info
				.getModalitaPagamentoSoggettoCessioneDaEliminare();
		ArrayList<SiacTModpagFin> modalitaDaEliminare = info.getModalitaDaEliminare();
		ArrayList<ModalitaPagamentoSoggetto> modalitaPagamentoSoggettoInModifica = info
				.getModalitaPagamentoSoggettoInModifica();

		// Inserimento Nuovi
		if (modalitaDaInserire != null && modalitaDaInserire.size() > 0)
		{
			for (ModalitaPagamentoSoggetto modPagToInsert : modalitaDaInserire)
			{
				saveModalitaPagamento(modPagToInsert, siacTSoggettoOriginale, codiceAmbito, idEnte,
						datiOperazioneModifica);
			}
		}

		// Modifico modalita
		if (modalitaDaModificare != null && modalitaDaModificare.size() > 0)
		{
			for (ModalitaPagamentoSoggetto modPagToUpdate : modalitaDaModificare)
			{
				if (modPagToUpdate.isModificaPropriaOccorrenza())
				{
					updateModalitaDiPagamento(modPagToUpdate, siacTSoggettoOriginale, idEnte, datiOperazioneModifica);
				}
				else
				{
					salvaModalitaPagamentoMod(siacTSoggettoOriginale, idEnte, modPagToUpdate, datiOperazioneModifica);
				}
			}
		}

		// Modifico modalita in modifica
		if (modalitaPagamentoSoggettoInModifica != null && modalitaPagamentoSoggettoInModifica.size() > 0)
		{
			for (ModalitaPagamentoSoggetto app : modalitaPagamentoSoggettoInModifica)
			{
				updateModalitaPagamentoMod(siacTSoggettoOriginale, idEnte, app, datiOperazioneModifica);
			}
		}

		// Elimino modalita
		if (modalitaDaEliminare != null && modalitaDaEliminare.size() > 0)
		{
			for (SiacTModpagFin modPagToDelete : modalitaDaEliminare)
			{
				eliminaModalitaPagamento(modPagToDelete, datiOperazioneModifica);
			}
		}

		// Elimina Modalita Cessione
		if (modalitaPagamentoCessioneToDelete != null && modalitaPagamentoCessioneToDelete.size() > 0)
		{
			for (ModalitaPagamentoSoggetto modPagToDelete : modalitaPagamentoCessioneToDelete)
			{
				eliminaModalitaPagamentoCessione(modPagToDelete, datiOperazioneModifica);
			}
		}

		if (listamodalita == null || listamodalita.isEmpty())
		{
			List<SiacTModpagFin> modpagList = siacTModpagRepository.findValidiByIdSoggetto(
					siacTSoggettoOriginale.getUid(), timestampInserimento);
			if (modpagList != null && modpagList.size() > 0)
			{
				for (SiacTModpagFin modpagToDelete : modpagList)
				{
					eliminaModalitaPagamento(modpagToDelete, datiOperazioneModifica);
				}
			}
			Timestamp now = new Timestamp(System.currentTimeMillis());
			List<SiacTModpagFin> listaTModpagsCessioni = siacTSoggettoRepository.ricercaModPagCessioni(idEnte,
					siacTSoggettoOriginale.getUid(), now);
			List<ModalitaPagamentoSoggetto> listaModPagsCessioni = null;
			if (listaTModpagsCessioni != null)
			{
				listaModPagsCessioni = convertiLista(listaTModpagsCessioni, ModalitaPagamentoSoggetto.class,
						FinMapId.SiacTModPag_ModalitaPagamentoSoggetto);

				// TODO capire se aggiungere il parametro aggiuntivo
				// SiacTSoggettoFin
				listaModPagsCessioni = modalitaPagamentoEntityToModalitaPagamentoCessioniModel(null,
						listaTModpagsCessioni, listaModPagsCessioni, siacTSoggettoOriginale.getUid(), "", now);

				for (ModalitaPagamentoSoggetto mdpAppToDelete : listaModPagsCessioni)
				{
					eliminaModalitaPagamentoCessione(mdpAppToDelete, datiOperazioneModifica);
				}
			}
		}
	}

	/**
	 * isSoggettoModificato
	 * 
	 * @param idSiacTSoggettoInModifica
	 * @param soggettoDaModificare
	 * @param richiedente
	 * @param ente
	 * @return
	 */
	private InserisciSoggettoModDto isSoggettoModificato(Integer idSiacTSoggettoInModifica,
			Soggetto soggettoDaModificare, Richiedente richiedente, Ente ente)
	{

		InserisciSoggettoModDto ritorno = new InserisciSoggettoModDto();
		boolean soggettoModificato = false;
		long currMillisec = System.currentTimeMillis();
		Date dateInserimento = new Date(currMillisec);
		Timestamp timestampInserimento = new Timestamp(currMillisec);
		Operazione operazione = Operazione.MODIFICA;

		int idEnte = ente.getUid();

		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository
				.findOne(idEnte);

		// quando aggiorno un record:
		DatiOperazioneDto datiOperazioneModifica = new DatiOperazioneDto(currMillisec, operazione,
				siacTEnteProprietario, richiedente.getAccount().getId());

		// quando inserisco un nuovo record:
		Operazione operazioneInsert = Operazione.INSERIMENTO;
		DatiOperazioneDto datiOperazioneInserisci = new DatiOperazioneDto(currMillisec,
				operazioneInsert, siacTEnteProprietario, richiedente.getAccount().getId());
		// /////////////

		SiacTSoggettoModFin siacTSoggettoMod = null;

		SiacTSoggettoFin siacTSoggettoOriginale = siacTSoggettoRepository
				.findOne(idSiacTSoggettoInModifica);

		List<SiacRSoggettoStatoFin> lst = siacRSoggettoStatoRepository.findValidoByIdSoggetto(
				idSiacTSoggettoInModifica, timestampInserimento);
		if (lst != null && lst.size() > 0)
		{
			SiacRSoggettoStatoFin stato = lst.get(0);
			ritorno.setStato(stato);
		}

		siacTSoggettoMod = EntityToEntityConverter
				.siacTSoggettoToSiacTSoggettoMod(siacTSoggettoOriginale);

		if (!StringUtils.isEmpty(soggettoDaModificare.getPartitaIva()))
		{
			String partitaIva = soggettoDaModificare.getPartitaIva().trim().toUpperCase();
			siacTSoggettoMod.setPartitaIva(partitaIva);
		}
		if (!StringUtils.isEmpty(soggettoDaModificare.getCodiceFiscale()))
		{
			String codFisc = soggettoDaModificare.getCodiceFiscale().trim().toUpperCase();
			siacTSoggettoMod.setCodiceFiscale(codFisc);
		}
		if (!StringUtils.isEmpty(soggettoDaModificare.getCodiceFiscaleEstero()))
		{
			String codFiscEst = soggettoDaModificare.getCodiceFiscaleEstero().trim().toUpperCase();
			siacTSoggettoMod.setCodiceFiscaleEstero(codFiscEst);
		}

		String codiceTipo = soggettoDaModificare.getTipoSoggetto().getCodice();

		// SIAC-6565-CR1215
		if (!StringUtils.isEmpty(soggettoDaModificare.getCanalePA()))
		{
			siacTSoggettoMod.setCanalePA(soggettoDaModificare.getCanalePA());
		}
		
		if (!StringUtils.isEmpty(soggettoDaModificare.getCodDestinatario()))
		{
			siacTSoggettoMod.setCodDestinatario(soggettoDaModificare.getCodDestinatario());
		}
		
		if (!StringUtils.isEmpty(soggettoDaModificare.getEmailPec()))
		{
			siacTSoggettoMod.setEmailPec(soggettoDaModificare.getEmailPec());
		}
		
		boolean isPersonaFisicaDopoModifica = false;
		if (codiceTipo.trim().toUpperCase().startsWith(Constanti.PERSONA_FISICA))
		{
			isPersonaFisicaDopoModifica = true;
		}

		// prima della modifica era persona fisica o giuridica:
		SoggettoTipoDto soggettoTipoDto = soggettoDao
				.getPersonaFisicaOppureGiuridica(idSiacTSoggettoInModifica);
		boolean isPersonaFisicPrimaModifica = false;
		if (soggettoTipoDto.isPersonaFisica())
		{
			isPersonaFisicPrimaModifica = true;
		}
		// ///////////

		String soggettoDesc = buildSoggettoDesc(soggettoDaModificare, isPersonaFisicaDopoModifica);
		siacTSoggettoMod.setSoggettoDesc(soggettoDesc);

		siacTSoggettoMod.setDataInizioValidita(dateInserimento);
		siacTSoggettoMod = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTSoggettoMod,
				datiOperazioneInserisci, siacTAccountRepository);

		soggettoModificato = isModificato(siacTSoggettoOriginale, siacTSoggettoMod);

		// codice per verificare se ci sono modifiche rispetto alla persona
		// fisica/giuridica:
		if (!soggettoModificato)
		{
			if (isPersonaFisicaDopoModifica != isPersonaFisicPrimaModifica)
			{
				soggettoModificato = true;
			}
			else
			{
				if (isPersonaFisicaDopoModifica)
				{
					// PERSONA FISICA
					SiacTPersonaFisicaModFin personaFisicaMod = new SiacTPersonaFisicaModFin();
					personaFisicaMod = buildPersonaFisicaModForSave(soggettoDaModificare,
							personaFisicaMod, timestampInserimento, siacTSoggettoMod,
							datiOperazioneInserisci);
					if (!soggettoModificato)
					{
						soggettoModificato = isModificato(soggettoTipoDto.getSiactTpersonaFisica(),
								personaFisicaMod);
					}
				}
				else
				{
					// PERSONA GIURIDICA
					SiacTPersonaGiuridicaModFin personaGiuridicaMod = new SiacTPersonaGiuridicaModFin();
					personaGiuridicaMod = buildPersonaGiuridicaModForSave(soggettoDaModificare,
							personaGiuridicaMod, siacTSoggettoOriginale, siacTSoggettoMod,
							datiOperazioneInserisci);
					if (!soggettoModificato)
					{
						soggettoModificato = isModificato(
								soggettoTipoDto.getSiactTpersonaGiuridica(), personaGiuridicaMod);
					}
				}
			}
		}
		
		// DURC
		if (!soggettoModificato 
				&& !Constanti.PERSONA_FISICA.equals(soggettoDaModificare.getTipoSoggetto().getSoggettoTipoCode())
				&& TipoFonteDurc.MANUALE.getCodice().equals(soggettoDaModificare.getTipoFonteDurc())) {
			
			soggettoModificato = datiDurcModificati(siacTSoggettoOriginale, soggettoDaModificare);
			
		}

		// Soggetto tipo:
		TipoSoggetto tipoSoggetto = soggettoDaModificare.getTipoSoggetto();
		if (tipoSoggetto != null)
		{
			// integrazione con il codice di claudio
			SiacRSoggettoTipoModFin siacRSoggettoTipoMod = buildSoggettoTipoModBeforeSave(
					tipoSoggetto, idEnte, siacTSoggettoMod, datiOperazioneInserisci);
			if (!soggettoModificato)
			{
				soggettoModificato = isModificatoSoggettoTipo(siacTSoggettoOriginale,
						siacRSoggettoTipoMod);
			}
		}

		// natura giuridica:
		NaturaGiuridicaSoggetto naturaGiuridicaSogg = soggettoDaModificare
				.getNaturaGiuridicaSoggetto();
		if (naturaGiuridicaSogg != null
				&& !StringUtils.isEmpty(naturaGiuridicaSogg.getSoggettoTipoCode()))
		{
			// integrazione con il codice di claudio
			String codeFg = naturaGiuridicaSogg.getSoggettoTipoCode();
			SiacTFormaGiuridicaFin siacTFormaGiuridica = siacTFormaGiuridicaRepository
					.findValidaByCode(idEnte, codeFg, datiOperazioneModifica.getTs()).get(0);
			SiacRFormaGiuridicaModFin rFormaGiuridicaMod = buildRFormGiuridicaMod(siacTFormaGiuridica,
					siacTSoggettoMod, datiOperazioneInserisci);
			if (!soggettoModificato)
			{
				soggettoModificato = isModificatoFormaGiuridica(siacTSoggettoOriginale,
						rFormaGiuridicaMod);
			}
		}

		// classe:
		String[] classificazioni = soggettoDaModificare.getTipoClassificazioneSoggettoId();
		if (!soggettoModificato)
		{
			ClassificazioniInModificaInfoDto classificazioniInModificaInfoDto = valutaClassificazioniDaModificare(
					classificazioni, idSiacTSoggettoInModifica);
			soggettoModificato = !classificazioniInModificaInfoDto.isRimasteUguali();
		}

		// ONERI:
		String[] oneriCodes = soggettoDaModificare.getTipoOnereId();
		if (!soggettoModificato)
		{
			OneriInModificaInfoDto oneriInModificaInfoDto = valutaOneriDaModificare(oneriCodes,
					idSiacTSoggettoInModifica);
			soggettoModificato = !oneriInModificaInfoDto.isRimastiUguali();
		}

		// indirizzi
		List<IndirizzoSoggetto> listaIndirizzi = soggettoDaModificare.getIndirizzi();
		if (!soggettoModificato)
		{
			IndirizziInModificaInfoDto indInMod = valutaIndirizziDaModificare(listaIndirizzi,
					idSiacTSoggettoInModifica);
			soggettoModificato = !indInMod.isRimastiUguali();
		}

		// recapiti:
		List<Contatto> listaContatti = soggettoDaModificare.getContatti();
		if (!soggettoModificato)
		{
			ContattiInModificaInfoDto contInMod = valutaContattiDaModificare(listaContatti,
					idSiacTSoggettoInModifica);
			soggettoModificato = !contInMod.isRimastiUguali();
		}

		// NOTE:
		List<SiacRSoggettoAttrFin> listaOld = siacRSoggettoAttrRepository
				.findValidaByIdSoggettoAndCode(idSiacTSoggettoInModifica,
						Constanti.T_ATTR_CODE_NOTE_SOGGETTO, timestampInserimento);
		String notaTestOld = null;
		if (listaOld != null && listaOld.size() > 0)
		{
			SiacRSoggettoAttrFin notaOld = listaOld.get(0);
			notaTestOld = notaOld.getTesto();
		}
		if (!StringUtils.sonoUguali(notaTestOld, soggettoDaModificare.getNote()))
		{
			soggettoModificato = true;
		}

		// MATRICOLA
		List<SiacRSoggettoAttrFin> listaAttrMatricolaOld = siacRSoggettoAttrRepository
				.findValidaByIdSoggettoAndCode(idSiacTSoggettoInModifica,
						Constanti.T_ATTR_CODE_MATRICOLA, timestampInserimento);
		String matricolaTestOld = null;
		if (listaAttrMatricolaOld != null && listaAttrMatricolaOld.size() > 0)
		{
			SiacRSoggettoAttrFin matricolaOld = listaAttrMatricolaOld.get(0);
			matricolaTestOld = matricolaOld.getTesto();
		}
		if (!StringUtils.sonoUguali(matricolaTestOld, soggettoDaModificare.getMatricola()))
		{
			soggettoModificato = true;
		}

		ritorno.setSoggettoCode(siacTSoggettoOriginale.getSoggettoCode());
		ritorno.setDatiOperazioneInserisci(datiOperazioneInserisci);
		ritorno.setSiacTSoggettoOriginale(siacTSoggettoOriginale);
		ritorno.setModificato(soggettoModificato);
		// Termino restituendo l'oggetto di ritorno:
		return ritorno;
	}

	private boolean datiDurcModificati(SiacTSoggettoFin siacTSoggettoOriginale, Soggetto soggettoDaModificare) {

		if (soggettoDaModificare.getDataFineValiditaDurc() != null && siacTSoggettoOriginale.getDataFineValiditaDurc() == null ||
			soggettoDaModificare.getDataFineValiditaDurc() == null && siacTSoggettoOriginale.getDataFineValiditaDurc() != null ||
			(soggettoDaModificare.getDataFineValiditaDurc() != null || siacTSoggettoOriginale.getDataFineValiditaDurc() != null) &&
			soggettoDaModificare.getDataFineValiditaDurc().compareTo(siacTSoggettoOriginale.getDataFineValiditaDurc()) != 0) {    
			return true;
		}

		if (siacTSoggettoOriginale.getFonteDurc() != null && !soggettoDaModificare.getFonteDurcClassifId()
				.equals(siacTSoggettoOriginale.getFonteDurc().getClassifId())) {
			return true;
		}

		if (! soggettoDaModificare.getNoteDurc().equals(siacTSoggettoOriginale.getNoteDurc())) {
			return true;
		}

		return false;
	}

	/**
	 * isModPagModificate
	 * 
	 * @param idSiacTSoggettoInModifica
	 * @param listamodalita
	 * @param sedi
	 * @param richiedente
	 * @param ente
	 * @return
	 */
	private boolean isModPagModificate(Integer idSiacTSoggettoInModifica,
			List<ModalitaPagamentoSoggetto> listamodalita, List<SedeSecondariaSoggetto> sedi,
			Richiedente richiedente, Ente ente)
	{
		long currMillisec = System.currentTimeMillis();
		int idEnte = ente.getUid();
		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository
				.findOne(idEnte);

		// quando aggiorno un record:
		// quando inserisco un nuovo record:
		Operazione operazioneInsert = Operazione.INSERIMENTO;
		DatiOperazioneDto datiOperazioneInserisci = new DatiOperazioneDto(currMillisec,
				operazioneInsert, siacTEnteProprietario, richiedente.getAccount().getId());

		// modalita di pagamento
		ModalitaPagamentoInModificaInfoDto info = valutaModPagDaModificare(listamodalita,
				idSiacTSoggettoInModifica, datiOperazioneInserisci, idEnte, sedi);
		
		return !info.isRimasteUguali();
	}

	/**
	 * isModificatoFormaGiuridica
	 * 
	 * @param siacTSoggettoOriginale
	 * @param rFormaGiuridicaMod
	 * @return
	 */
	private boolean isModificatoFormaGiuridica(SiacTSoggettoFin siacTSoggettoOriginale,
			SiacRFormaGiuridicaModFin rFormaGiuridicaMod)
	{
		boolean modificato = false;
		Timestamp now = new Timestamp(System.currentTimeMillis());
		List<SiacRFormaGiuridicaFin> lt = siacRFormaGiuridicaRepository.findValidaByIdSoggetto(
				siacTSoggettoOriginale.getSoggettoId(), now);
		if (lt.isEmpty())
		{
			return true;
		}
		SiacRFormaGiuridicaFin siacRFormaGiuridica = lt.get(0);
		String confrontoOld = siacRFormaGiuridica.getSiacTFormaGiuridica()
				.getFormaGiuridicaIstatCodice();
		String confrontoNew = rFormaGiuridicaMod.getSiacTFormaGiuridica()
				.getFormaGiuridicaIstatCodice();
		if (!StringUtils.sonoUguali(confrontoOld, confrontoNew))
		{
			return true;
		}
		return modificato;
	}

	/**
	 * isModificatoSoggettoTipo
	 * 
	 * @param siacTSoggettoOriginale
	 * @param siacRSoggettoTipoMod
	 * @return
	 */
	private boolean isModificatoSoggettoTipo(SiacTSoggettoFin siacTSoggettoOriginale,
			SiacRSoggettoTipoModFin siacRSoggettoTipoMod)
	{
		boolean modificato = false;
		Timestamp now = new Timestamp(System.currentTimeMillis());
		List<SiacRSoggettoTipoFin> lt = siacRSoggettoTipoRepository.findValidoByIdSoggetto(
				siacTSoggettoOriginale.getSoggettoId(), now);
		SiacRSoggettoTipoFin siacRSoggettoTipo = lt.get(0);
		String confrontoOld = siacRSoggettoTipo.getSiacDSoggettoTipo().getSoggettoTipoCode();
		String confrontoNew = siacRSoggettoTipoMod.getSiacDSoggettoTipo().getSoggettoTipoCode();
		if (!StringUtils.sonoUguali(confrontoOld, confrontoNew))
		{
			return true;
		}
		// Termino restituendo l'oggetto di ritorno:
		return modificato;
	}

	/**
	 * isModificatoSoggettoTipoMod
	 * 
	 * @param siacTSoggettoOriginale
	 * @param siacRSoggettoTipoMod
	 * @return
	 */
	private boolean isModificatoSoggettoTipoMod(SiacTSoggettoModFin siacTSoggettoOriginale,
			SiacRSoggettoTipoModFin siacRSoggettoTipoMod)
	{
		boolean modificato = false;
		Timestamp now = new Timestamp(System.currentTimeMillis());
		List<SiacRSoggettoTipoModFin> lt = siacRSoggettoTipoModRepository.findValidoByIdSoggetto(
				siacTSoggettoOriginale.getSogModId(), now);
		SiacRSoggettoTipoModFin siacRSoggettoTipo = lt.get(0);
		String confrontoOld = siacRSoggettoTipo.getSiacDSoggettoTipo().getSoggettoTipoCode();
		String confrontoNew = siacRSoggettoTipoMod.getSiacDSoggettoTipo().getSoggettoTipoCode();
		if (!StringUtils.sonoUguali(confrontoOld, confrontoNew))
		{
			return true;
		}
		// Termino restituendo l'oggetto di ritorno:
		return modificato;
	}

	/**
	 * isModificato
	 * 
	 * @param siactTpersonaGiuridica
	 * @param personaGiuridicaMod
	 * @return
	 */
	private boolean isModificato(SiacTPersonaGiuridicaFin siactTpersonaGiuridica,
			SiacTPersonaGiuridicaModFin personaGiuridicaMod)
	{
		boolean modificato = false;
		if (!StringUtils.sonoUguali(siactTpersonaGiuridica.getRagioneSociale(),
				siactTpersonaGiuridica.getRagioneSociale()))
		{
			return true;
		}
		// Termino restituendo false
		return modificato;
	}

	/**
	 * isModificato
	 * 
	 * @param siactTpersonaGiuridica
	 * @param personaGiuridicaMod
	 * @return
	 */
	private boolean isModificato(SiacTPersonaGiuridicaModFin siactTpersonaGiuridica,
			SiacTPersonaGiuridicaModFin personaGiuridicaMod)
	{
		boolean modificato = false;
		if (!StringUtils.sonoUguali(siactTpersonaGiuridica.getRagioneSociale(),
				siactTpersonaGiuridica.getRagioneSociale()))
		{
			return true;
		}
		return modificato;
	}

	/**
	 * isModificato
	 * 
	 * @param siactTpersonaFisica
	 * @param personaFisicaMod
	 * @return
	 */
	private boolean isModificato(SiacTPersonaFisicaFin siactTpersonaFisica,
			SiacTPersonaFisicaModFin personaFisicaMod)
	{
		boolean modificato = false;
		if (!StringUtils.sonoUguali(siactTpersonaFisica.getNome(), personaFisicaMod.getNome()))
		{
			return true;
		}
		if (!StringUtils
				.sonoUguali(siactTpersonaFisica.getCognome(), personaFisicaMod.getCognome()))
		{
			return true;
		}
		if (!StringUtils.sonoUguali(siactTpersonaFisica.getSesso(), personaFisicaMod.getSesso()))
		{
			return true;
		}
		if (!StringUtils.sonoUguali(siactTpersonaFisica.getNascitaData(),
				personaFisicaMod.getNascitaData()))
		{
			return true;
		}
		if (!StringUtils.sonoUguali(siactTpersonaFisica.getSiacTComune().getComuneId(),
				personaFisicaMod.getSiacTComune().getComuneId()))
		{
			return true;
		}
		return modificato;
	}

	/**
	 * isModificato
	 * 
	 * @param siactTpersonaFisica
	 * @param personaFisicaMod
	 * @return
	 */
	private boolean isModificato(SiacTPersonaFisicaModFin siactTpersonaFisica,
			SiacTPersonaFisicaModFin personaFisicaMod)
	{
		boolean modificato = false;
		if (!StringUtils.sonoUguali(siactTpersonaFisica.getNome(), personaFisicaMod.getNome()))
		{
			return true;
		}
		if (!StringUtils
				.sonoUguali(siactTpersonaFisica.getCognome(), personaFisicaMod.getCognome()))
		{
			return true;
		}
		if (!StringUtils.sonoUguali(siactTpersonaFisica.getSesso(), personaFisicaMod.getSesso()))
		{
			return true;
		}
		if (!StringUtils.sonoUguali(siactTpersonaFisica.getNascitaData(),
				personaFisicaMod.getNascitaData()))
		{
			return true;
		}
		if (!StringUtils.sonoUguali(siactTpersonaFisica.getSiacTComune().getComuneId(),
				personaFisicaMod.getSiacTComune().getComuneId()))
		{
			return true;
		}
		return modificato;
	}

	/**
	 * isModificato
	 * 
	 * @param siacTSoggetto
	 * @param siacTSoggettoMod
	 * @return
	 */
	public boolean isModificato(SiacTSoggettoFin siacTSoggetto, SiacTSoggettoModFin siacTSoggettoMod)
	{
		boolean modificato = false;
		if (!StringUtils
				.sonoUguali(siacTSoggetto.getPartitaIva(), siacTSoggettoMod.getPartitaIva()))
		{
			return true;
		}
		if (!StringUtils.sonoUgualiTrimmed(siacTSoggetto.getCodiceFiscaleEstero(),
				siacTSoggettoMod.getCodiceFiscaleEstero()))
		{
			return true;
		}
		if (!StringUtils.sonoUgualiTrimmed(siacTSoggetto.getCodiceFiscale(),
				siacTSoggettoMod.getCodiceFiscale()))
		{
			return true;
		}
		if (!StringUtils.sonoUguali(siacTSoggetto.getSoggettoDesc(),
				siacTSoggettoMod.getSoggettoDesc()))
		{
			return true;
		}
		
		
		if (!StringUtils.sonoUguali(siacTSoggetto.getCanalePA(),
				siacTSoggettoMod.getCanalePA()))
		{
			return true;
		}
		if (!StringUtils.sonoUguali(siacTSoggetto.getEmailPec(),
				siacTSoggettoMod.getEmailPec()))
		{
			return true;
		}
		if (!StringUtils.sonoUguali(siacTSoggetto.getCodDestinatario(),
				siacTSoggettoMod.getCodDestinatario()))
		{
			return true;
		}
		
		
		return modificato;
	}

	/**
	 * isModificato
	 * 
	 * @param siacTSoggetto
	 * @param siacTSoggettoMod
	 * @return
	 */
	public boolean isModificato(SiacTSoggettoModFin siacTSoggetto, SiacTSoggettoModFin siacTSoggettoMod)
	{
		boolean modificato = false;
		if (!StringUtils
				.sonoUguali(siacTSoggetto.getPartitaIva(), siacTSoggettoMod.getPartitaIva()))
		{
			return true;
		}
		if (!StringUtils.sonoUguali(siacTSoggetto.getCodiceFiscaleEstero(),
				siacTSoggettoMod.getCodiceFiscaleEstero()))
		{
			return true;
		}
		if (!StringUtils.sonoUguali(siacTSoggetto.getCodiceFiscale(),
				siacTSoggettoMod.getCodiceFiscale()))
		{
			return true;
		}
		if (!StringUtils.sonoUguali(siacTSoggetto.getSoggettoDesc(),
				siacTSoggettoMod.getSoggettoDesc()))
		{
			return true;
		}
		return modificato;
	}

	/**
	 * Si occupa di aggiornare il soggetto mod
	 * 
	 * @param idSiacTSoggetto
	 * @param soggettoDaModificare
	 * @param richiedente
	 * @param ente
	 * @param isAggiornaSoloSedi
	 * @return
	 */
	public Soggetto aggiornaSoggettoMod(Integer idSiacTSoggetto, Soggetto soggettoDaModificare,
			Richiedente richiedente, String codiceAmbito, Ente ente, boolean isAggiornaSoloSedi,
			DatiOperazioneDto datiOperazioneDto)
	{
		boolean soggettoModificato = false;
		long currMillisec = System.currentTimeMillis();
		Date dateInserimento = new Date(currMillisec);
		Timestamp timestampInserimento = new Timestamp(currMillisec);
		Operazione operazione = Operazione.MODIFICA;

		int idEnte = ente.getUid();
		// prendo ambito da livello service
		SiacDAmbitoFin siacDAmbitoPerCode = datiOperazioneDto.getSiacDAmbito();
		Integer idAmbito = siacDAmbitoPerCode.getAmbitoId();

		if (controlloTipoSogg(soggettoDaModificare.getTipoSoggetto().getSoggettoTipoCode()))
		{
			soggettoDaModificare.setNome(soggettoDaModificare.getNome().toUpperCase());
			soggettoDaModificare.setCognome(soggettoDaModificare.getCognome().toUpperCase());
		}

		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository
				.findOne(idEnte);

		// quando aggiorno un record:
		DatiOperazioneDto datiOperazioneModifica = new DatiOperazioneDto(currMillisec, operazione,
				siacTEnteProprietario, siacDAmbitoPerCode, richiedente.getAccount().getId());

		// quando inserisco un nuovo record:
		Operazione operazioneInsert = Operazione.INSERIMENTO;
		DatiOperazioneDto datiOperazioneInserisci = new DatiOperazioneDto(currMillisec,
				operazioneInsert, siacTEnteProprietario, siacDAmbitoPerCode, richiedente
						.getAccount().getId());
		// /////////////

		SiacTSoggettoFin siacTSoggettoOriginale = null;

		SiacTSoggettoModFin siacTSoggettoMod = findSoggettoModBySoggetto(idSiacTSoggetto);
		Integer idSiacTSoggettoMod = siacTSoggettoMod.getSogModId();
		siacTSoggettoOriginale = siacTSoggettoRepository.findOne(idSiacTSoggetto);

		SiacTSoggettoModFin siacTSoggettoModPrimaDellaModifica = siacTSoggettoModRepository
				.findOne(idSiacTSoggettoMod);
		//

		if (!isAggiornaSoloSedi)
		{
			siacTSoggettoMod.setDataFineValiditaDurc(soggettoDaModificare.getDataFineValiditaDurc());
			siacTSoggettoMod.setTipoFonteDurc(soggettoDaModificare.getTipoFonteDurc());
			siacTSoggettoMod.setFonteDurc(soggettoDaModificare.getFonteDurcClassifId() != null ?
					new SiacTClassFin(soggettoDaModificare.getFonteDurcClassifId())
					: null);
			siacTSoggettoMod.setNoteDurc(soggettoDaModificare.getNoteDurc());
			
			if (!StringUtils.isEmpty(soggettoDaModificare.getPartitaIva()))
			{
				String partitaIva = soggettoDaModificare.getPartitaIva().trim().toUpperCase();
				siacTSoggettoMod.setPartitaIva(partitaIva);
			}
			if (!StringUtils.isEmpty(soggettoDaModificare.getCodiceFiscale()))
			{
				String codFisc = soggettoDaModificare.getCodiceFiscale().trim().toUpperCase();
				siacTSoggettoMod.setCodiceFiscale(codFisc);
			}
			if (!StringUtils.isEmpty(soggettoDaModificare.getCodiceFiscaleEstero()))
			{
				String codFiscEst = soggettoDaModificare.getCodiceFiscaleEstero().trim()
						.toUpperCase();
				siacTSoggettoMod.setCodiceFiscaleEstero(codFiscEst);
			}

			// SIAC-6565-CR1215
			if (!StringUtils.isEmpty(soggettoDaModificare.getCanalePA()))
			{
				siacTSoggettoMod.setCanalePA(soggettoDaModificare.getCanalePA());
			}
			
			if (!StringUtils.isEmpty(soggettoDaModificare.getCodDestinatario()))
			{
				siacTSoggettoMod.setCodDestinatario(soggettoDaModificare.getCodDestinatario());
			}
			
			if (!StringUtils.isEmpty(soggettoDaModificare.getEmailPec()))
			{
				siacTSoggettoMod.setEmailPec(soggettoDaModificare.getEmailPec());
			}
			
			String codiceTipo = soggettoDaModificare.getTipoSoggetto().getCodice();

			boolean isPersonaFisicaDopoModifica = false;
			if (codiceTipo.trim().toUpperCase().startsWith(Constanti.PERSONA_FISICA))
			{
				isPersonaFisicaDopoModifica = true;
			}

			// prima della modifica era persona fisica o giuridica:
			SoggettoTipoModDto soggettoTipoDto = soggettoDao
					.getPersonaFisicaOppureGiuridicaMod(idSiacTSoggettoMod);
			boolean isPersonaFisicPrimaModifica = false;
			if (soggettoTipoDto.isPersonaFisica())
			{
				isPersonaFisicPrimaModifica = true;
			}
			// ///////////

			String soggettoDesc = buildSoggettoDesc(soggettoDaModificare,
					isPersonaFisicaDopoModifica);
			siacTSoggettoMod.setSoggettoDesc(soggettoDesc);

			siacTSoggettoMod.setDataInizioValidita(dateInserimento);
			// //////////////
			siacTSoggettoMod = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTSoggettoMod,
					datiOperazioneInserisci, siacTAccountRepository);
			// salvo sul db:
			siacTSoggettoMod = siacTSoggettoModRepository.saveAndFlush(siacTSoggettoMod);

			soggettoModificato = isModificato(siacTSoggettoModPrimaDellaModifica, siacTSoggettoMod);

			if (isPersonaFisicPrimaModifica != isPersonaFisicaDopoModifica)
			{
				soggettoModificato = true;
				if (isPersonaFisicaDopoModifica)
				{
					// caso era giuridica e diventa fisica
					// invalido la persona giuridica:
					SiacTPersonaGiuridicaModFin personaGiuridica = soggettoTipoDto
							.getSiactTpersonaGiuridicaMod();
					personaGiuridica = DatiOperazioneUtils.cancellaRecord(personaGiuridica,
							siacTPersonaGiuridicaRepository, datiOperazioneModifica,
							siacTAccountRepository);
					// inserisco una persona fisica:
					SiacTPersonaFisicaModFin personaFisica = new SiacTPersonaFisicaModFin();
					personaFisica = savePersonaFisicaMod(soggettoDaModificare, personaFisica,
							timestampInserimento, siacTSoggettoMod, datiOperazioneInserisci);
				}
				else
				{
					// caso era fisica e diventa giuridica
					// invalido la persona fisica:
					SiacTPersonaFisicaModFin personaFisica = soggettoTipoDto
							.getSiactTpersonaFisicaMod();
					personaFisica = DatiOperazioneUtils.cancellaRecord(personaFisica,
							siacTPersonaFisicaRepository, datiOperazioneModifica,
							siacTAccountRepository);
					// inserisco una persona fisica:
					SiacTPersonaGiuridicaModFin personaGiuridica = new SiacTPersonaGiuridicaModFin();
					personaGiuridica = savePersonaGiuridicaMod(soggettoDaModificare,
							personaGiuridica, siacTSoggettoOriginale, siacTSoggettoMod,
							datiOperazioneInserisci);
				}
			}
			else
			{
				// caso resta dello stesso tipo
				if (isPersonaFisicaDopoModifica)
				{
					// PERSONA FISICA
					// invalido la vecchia occorrenza di persona fisica:
					SiacTPersonaFisicaModFin personaFisicaDelete = soggettoTipoDto
							.getSiactTpersonaFisicaMod();
					personaFisicaDelete = DatiOperazioneUtils.cancellaRecord(personaFisicaDelete,
							siacTPersonaFisicaModRepository, datiOperazioneModifica,
							siacTAccountRepository);

					// inserisco una nuova occorrenza di persona fisica
					SiacTPersonaFisicaModFin personaFisicaNew = EntityToEntityConverter
							.siacTPersonaFisicaModToSiacTPersonaFisicaMod(personaFisicaDelete);
					personaFisicaNew = savePersonaFisicaMod(soggettoDaModificare, personaFisicaNew,
							timestampInserimento, siacTSoggettoMod, datiOperazioneInserisci);
					if (!soggettoModificato)
					{
						soggettoModificato = isModificato(
								soggettoTipoDto.getSiactTpersonaFisicaMod(), personaFisicaNew);
					}
				}
				else
				{
					// PERSONA GIURIDICA - solo update del record
					// invalido la vecchia occorrenza di persona giuridica
					SiacTPersonaGiuridicaModFin personaGiuridicaDelete = soggettoTipoDto
							.getSiactTpersonaGiuridicaMod();
					personaGiuridicaDelete = DatiOperazioneUtils.cancellaRecord(
							personaGiuridicaDelete, siacTPersonaGiuridicaModRepository,
							datiOperazioneModifica, siacTAccountRepository);
					// inserisco una nuova occorrenza di persona giuridica
					SiacTPersonaGiuridicaModFin personaGiuridicaNew = EntityToEntityConverter
							.siacTPersonaGiuridicaModToSiacTPersonaGiuridicaMod(personaGiuridicaDelete);
					personaGiuridicaNew = savePersonaGiuridicaMod(soggettoDaModificare,
							personaGiuridicaNew, siacTSoggettoOriginale, siacTSoggettoMod,
							datiOperazioneInserisci);
					if (!soggettoModificato)
					{
						soggettoModificato = isModificato(
								soggettoTipoDto.getSiactTpersonaGiuridicaMod(), personaGiuridicaNew);
					}
				}
			}

			// Soggetto tipo:
			TipoSoggetto tipoSoggetto = soggettoDaModificare.getTipoSoggetto();
			if (tipoSoggetto != null)
			{
				List<SiacRSoggettoTipoModFin> listaSiacRSogg = siacRSoggettoTipoModRepository
						.findValidoByIdSoggettoMod(idSiacTSoggettoMod,
								datiOperazioneModifica.getTs());
				// ci aspettiamo uno ed uno solo valido in listaSiacRSogg:
				SiacRSoggettoTipoModFin siacRSoggTipoOld = listaSiacRSogg.get(0);
				String oldCode = siacRSoggTipoOld.getSiacDSoggettoTipo().getSoggettoTipoCode();
				String newCode = tipoSoggetto.getCodice();
				if (!oldCode.equals(newCode))
				{
					// e' stato cambiato, occorre aggiornare i dati
					// invalido la relazione old:
					siacRSoggTipoOld = DatiOperazioneUtils.cancellaRecord(siacRSoggTipoOld,
							siacRSoggettoTipoRepository, datiOperazioneModifica,
							siacTAccountRepository);
					// inserisco un nuovo legame:
					SiacRSoggettoTipoModFin siacRSoggettoTipoMod = saveSoggettoTipoMod(tipoSoggetto,
							idEnte, siacTSoggettoMod, datiOperazioneInserisci);
					if (!soggettoModificato)
					{
						soggettoModificato = isModificatoSoggettoTipoMod(
								siacTSoggettoModPrimaDellaModifica, siacRSoggettoTipoMod);
					}
				}
			}

			// natura giuridica:
			NaturaGiuridicaSoggetto naturaGiuridicaSogg = soggettoDaModificare
					.getNaturaGiuridicaSoggetto();
			if (naturaGiuridicaSogg != null
					&& !StringUtils.isEmpty(naturaGiuridicaSogg.getSoggettoTipoCode()))
			{

				// Per il soggetto modificato e' stata selezionata una natura
				// giuridica
				// 20/12/2013 : Jira-136 : Commento perche' andava in crash
				// l'applicativo se il soggetto, prima della modifica, non aveva
				// una forma giuridica associata

				List<SiacRFormaGiuridicaModFin> listRFormaGiuOld = siacRFormaGiuridicaModRepository
						.findValidaByIdSoggettoMod(idSiacTSoggettoMod,
								datiOperazioneModifica.getTs());
				if (null != listRFormaGiuOld && listRFormaGiuOld.size() > 0)
				{

					// Il soggetto aveva gia' una natura giuridica associata,
					// verifico se e' cambiata e in caso affermativo la aggiorno
					// Dovrebbe venire restituito sempre un solo record valido

					SiacRFormaGiuridicaModFin rFormaGiuOld = listRFormaGiuOld.get(0);
					SiacTFormaGiuridicaFin siacTFormaGiuridicaOld = rFormaGiuOld
							.getSiacTFormaGiuridica();
					String codeOld = siacTFormaGiuridicaOld.getFormaGiuridicaIstatCodice();

					String codeNew = naturaGiuridicaSogg.getSoggettoTipoCode();
					if (null != codeNew && !StringUtils.isEmpty(codeNew))
					{
						if (!codeOld.equals(codeNew))
						{
							soggettoModificato = true;
							// invalido record di relazione vecchio:
							rFormaGiuOld = DatiOperazioneUtils.cancellaRecord(rFormaGiuOld,
									siacRFormaGiuridicaModRepository, datiOperazioneModifica,
									siacTAccountRepository);
							// inserisco nuovo record di relazione:
							SiacTFormaGiuridicaFin siacTFormaGiuridicaNew = siacTFormaGiuridicaRepository
									.findValidaByCode(idEnte, codeNew,
											datiOperazioneModifica.getTs()).get(0);
							saveRFormGiuridicaMod(siacTFormaGiuridicaNew, siacTSoggettoMod,
									datiOperazioneInserisci);
						}
					}
				}
				else
				{
					soggettoModificato = true;
					// Il soggetto non aveva ancora una natura giuridica
					// associata
					// Inserisco la nuova natura giuridica selezionata a video

					String codeFg = naturaGiuridicaSogg.getSoggettoTipoCode();
					SiacTFormaGiuridicaFin siacTFormaGiuridica = siacTFormaGiuridicaRepository
							.findValidaByCode(idEnte, codeFg, datiOperazioneInserisci.getTs()).get(
									0);
					saveRFormGiuridicaMod(siacTFormaGiuridica, siacTSoggettoMod,
							datiOperazioneInserisci);
				}
			}
			else
			{
				// Per il soggetto modificato non e' stata selezionata una
				// natura giuridica
				// Verifico se il soggetto aveva una natura giuridica associata
				List<SiacRFormaGiuridicaModFin> listRFormaGiuOld = siacRFormaGiuridicaModRepository
						.findValidaByIdSoggetto(idSiacTSoggetto, datiOperazioneModifica.getTs());
				if (null != listRFormaGiuOld && listRFormaGiuOld.size() > 0)
				{
					soggettoModificato = true;
					// Il soggetto aveva una natura giuridica associata, la
					// cancello logicamente
					SiacRFormaGiuridicaModFin rFormaGiuOld = listRFormaGiuOld.get(0);
					rFormaGiuOld = DatiOperazioneUtils.cancellaRecord(rFormaGiuOld,
							siacRFormaGiuridicaRepository, datiOperazioneModifica,
							siacTAccountRepository);
				}
			}

			String[] classificazioni = soggettoDaModificare.getTipoClassificazioneSoggettoId();
			if (!soggettoModificato)
			{
				ClassificazioniModInModificaInfoDto classificazioniInModificaInfoDto = valutaClassificazioniModDaModificare(
						classificazioni, siacTSoggettoMod.getSogModId());
				soggettoModificato = !classificazioniInModificaInfoDto.isRimasteUguali();
			}
			updateClassificazioniMod(classificazioni, datiOperazioneModifica, siacTSoggettoMod,
					idEnte, idAmbito);

			String[] oneriCodes = soggettoDaModificare.getTipoOnereId();
			if (!soggettoModificato)
			{
				OneriModInModificaInfoDto oneriInModificaInfoDto = valutaOneriDaModificareMod(
						oneriCodes, siacTSoggettoMod.getSogModId());
				soggettoModificato = !oneriInModificaInfoDto.isRimastiUguali();
			}
			updateOneriMod(oneriCodes, datiOperazioneModifica, siacTSoggettoMod, idEnte, idAmbito);

			// indirizzi
			List<IndirizzoSoggetto> listaIndirizzi = soggettoDaModificare.getIndirizzi();
			if (!soggettoModificato)
			{
				IndirizziModInModificaInfoDto indInMod = valutaIndirizziModDaModificare(
						listaIndirizzi, siacTSoggettoMod.getSogModId());
				soggettoModificato = !indInMod.isRimastiUguali();
			}
			aggiornamentoIndirizziMod(listaIndirizzi, siacTSoggettoMod, datiOperazioneModifica);

			List<Contatto> listaContatti = soggettoDaModificare.getContatti();
			if (!soggettoModificato)
			{
				ContattiModInModificaInfoDto contInMod = valutaContattiDaModificareMod(
						listaContatti, siacTSoggettoMod.getSogModId());
				soggettoModificato = !contInMod.isRimastiUguali();
			}
			aggiornaContattiMod(listaContatti, datiOperazioneModifica, siacTSoggettoMod, idEnte);

			// modalita di pagamento
			SiacTSoggettoFin siacTSoggettoToCheck = siacTSoggettoOriginale;
			if (siacTSoggettoToCheck == null)
			{
				siacTSoggettoToCheck = siacTSoggettoMod.getSiacTSoggetto();
			}

			List<ModalitaPagamentoSoggetto> listamodalita = soggettoDaModificare
					.getModalitaPagamentoList();

			List<SedeSecondariaSoggetto> sedi = soggettoDaModificare.getSediSecondarie();
			if (listamodalita != null && listamodalita.size() > 0)
			{
				ModalitaPagamentoInModificaInfoDto info = valutaModPagDaModificare(listamodalita,
						siacTSoggettoToCheck.getUid(), datiOperazioneInserisci, idEnte, sedi);
				if (!soggettoModificato)
				{
					soggettoModificato = !info.isRimasteUguali();
				}
				ArrayList<ModalitaPagamentoSoggetto> modalitaDaInserire = info
						.getModalitaDaInserire();
				ArrayList<ModalitaPagamentoSoggetto> modalitaDaModificare = info
						.getModalitaDaModificare();
				ArrayList<ModalitaPagamentoSoggetto> modalitaPagamentoCessioneToDelete = info
						.getModalitaPagamentoSoggettoCessioneDaEliminare();
				ArrayList<ModalitaPagamentoSoggetto> modalitaPagamentoSoggettoInModifica = info
						.getModalitaPagamentoSoggettoInModifica();
				ArrayList<SiacTModpagFin> modalitaDaEliminare = info.getModalitaDaEliminare();

				// Inserimento Nuovi
				if (modalitaDaInserire != null && modalitaDaInserire.size() > 0)
				{
					for (ModalitaPagamentoSoggetto modPagToInsert : modalitaDaInserire)
					{
						saveModalitaPagamento(modPagToInsert, siacTSoggettoToCheck, codiceAmbito,
								idEnte, datiOperazioneInserisci);
					}
				}

				// Modifico modalita
				if (modalitaDaModificare != null && modalitaDaModificare.size() > 0)
				{
					for (ModalitaPagamentoSoggetto modPagToUpdate : modalitaDaModificare)
					{
						if (modPagToUpdate.isModificaPropriaOccorrenza())
						{
							updateModalitaDiPagamento(modPagToUpdate, siacTSoggettoToCheck, idEnte,
									datiOperazioneInserisci);
						}
						else
						{
							salvaModalitaPagamentoMod(siacTSoggettoToCheck, idEnte, modPagToUpdate,
									datiOperazioneInserisci);
						}
					}
				}

				// Modifico modalita in modifica
				if (modalitaPagamentoSoggettoInModifica != null
						&& modalitaPagamentoSoggettoInModifica.size() > 0)
				{
					for (ModalitaPagamentoSoggetto app : modalitaPagamentoSoggettoInModifica)
					{
						updateModalitaPagamentoMod(siacTSoggettoToCheck, idEnte, app,
								datiOperazioneInserisci);
					}
				}

				// Elimino modalita
				if (modalitaDaEliminare != null && modalitaDaEliminare.size() > 0)
				{
					for (SiacTModpagFin modPagToDelete : modalitaDaEliminare)
					{
						eliminaModalitaPagamento(modPagToDelete, datiOperazioneModifica);
					}
				}

				// Elimina Modalita Cessione
				if (modalitaPagamentoCessioneToDelete != null
						&& modalitaPagamentoCessioneToDelete.size() > 0)
				{
					for (ModalitaPagamentoSoggetto modPagToDelete : modalitaPagamentoCessioneToDelete)
					{
						eliminaModalitaPagamentoCessione(modPagToDelete, datiOperazioneModifica);
					}

				}

			}
			else
			{
				List<SiacTModpagFin> modpagList = siacTModpagRepository.findValidiByIdSoggetto(
						siacTSoggettoToCheck.getUid(), timestampInserimento);
				if (modpagList != null && modpagList.size() > 0)
				{
					for (SiacTModpagFin modpagToDelete : modpagList)
					{
						eliminaModalitaPagamento(modpagToDelete, datiOperazioneModifica);
					}
				}

				Timestamp now = new Timestamp(System.currentTimeMillis());

				List<SiacTModpagFin> listaTModpagsCessioni = siacTSoggettoRepository
						.ricercaModPagCessioni(idEnte, siacTSoggettoToCheck.getUid(), now);
				List<ModalitaPagamentoSoggetto> listaModPagsCessioni = null;
				if (listaTModpagsCessioni != null && listaTModpagsCessioni.size() > 0)
				{
					listaModPagsCessioni = convertiLista(listaTModpagsCessioni,
							ModalitaPagamentoSoggetto.class,
							FinMapId.SiacTModPag_ModalitaPagamentoSoggetto);
					// TODO capire se aggiungere il parametro aggiuntivo
					// SiacTSoggettoFin
					listaModPagsCessioni = modalitaPagamentoEntityToModalitaPagamentoCessioniModel(
							null, listaTModpagsCessioni, listaModPagsCessioni,
							siacTSoggettoToCheck.getUid(), "", now);
					for (ModalitaPagamentoSoggetto mdpAppToDelete : listaModPagsCessioni)
					{
						eliminaModalitaPagamentoCessione(mdpAppToDelete, datiOperazioneModifica);
					}
				}
			}

			// INSERIMENTO DELLE NOTE
			SiacRSoggettoAttrModFin sMod = new SiacRSoggettoAttrModFin();
			sMod.setSiacTSoggettoMod(siacTSoggettoMod);

			// NOTE
			AttributoTClassInfoDto attributoInfo = new AttributoTClassInfoDto();
			attributoInfo.setSiacTSoggettoMod(siacTSoggettoMod);
			attributoInfo.setTipoOggetto(OggettoDellAttributoTClass.T_SOGGETTO_MOD);

			boolean noteMod = isAttributoTAttrModificato(attributoInfo, datiOperazioneInserisci,
					soggettoDaModificare.getNote(), Constanti.T_ATTR_CODE_NOTE_SOGGETTO);
			if (noteMod)
			{
				soggettoModificato = true;
			}

			salvaAttributoTAttr(attributoInfo, datiOperazioneInserisci,
					soggettoDaModificare.getNote(), Constanti.T_ATTR_CODE_NOTE_SOGGETTO);

		}
		else
		{
			// SEDI SECONDARIE: IL SOGGETTO e' NULL INVECE da siacTSoggettoMod
			// il soggetto(riferito alla tabella siac_t_soggetto) e' valorizzato
			gestisciSediSecondarie(siacTSoggettoOriginale, richiedente, datiOperazioneInserisci,
					true, soggettoDaModificare.getSediSecondarie(),
					soggettoDaModificare.getCodiceSoggetto());// a volte il
																// soggetto e'
																// null
		}

		if (soggettoModificato)
		{
			// salvaStatoOperativoAnagrafica(StatoOperativoAnagrafica.IN_MODIFICA,
			// siacTSoggettoOriginale, idEnte, datiOperazioneInserisci); //
			// siacTSoggettoOriginale NULL!
		}

		// RITORNO RISULTATI:
		Soggetto soggetto = new Soggetto();
		soggetto.setCodiceSoggetto(siacTSoggettoMod.getSoggettoCode());

		soggetto = EntityToModelConverter.soggettoEntityToSoggettoModel(siacTSoggettoOriginale,
				soggetto); // NON FUNZIONA IL SOGGETTO e' NULL
		// Termino restituendo l'oggetto di ritorno:
		return soggetto;

	}

	/**
	 * saveRFormGiuridicaMod
	 * 
	 * @param siacTFormaGiuridica
	 * @param siacTSoggettoMod
	 * @param datiOperazioneDto
	 * @return
	 */
	private SiacRFormaGiuridicaModFin saveRFormGiuridicaMod(SiacTFormaGiuridicaFin siacTFormaGiuridica,
			SiacTSoggettoModFin siacTSoggettoMod, DatiOperazioneDto datiOperazioneDto)
	{
		SiacRFormaGiuridicaModFin rFormaGiuridicaMod = buildRFormGiuridicaMod(siacTFormaGiuridica,
				siacTSoggettoMod, datiOperazioneDto);
		// salvo sul db:
		rFormaGiuridicaMod = siacRFormaGiuridicaModRepository.saveAndFlush(rFormaGiuridicaMod);
		// Termino restituendo l'oggetto di ritorno:
		return rFormaGiuridicaMod;
	}

	/**
	 * buildRFormGiuridicaMod
	 * 
	 * @param siacTFormaGiuridica
	 * @param siacTSoggettoMod
	 * @param datiOperazioneDto
	 * @return
	 */
	private SiacRFormaGiuridicaModFin buildRFormGiuridicaMod(SiacTFormaGiuridicaFin siacTFormaGiuridica,
			SiacTSoggettoModFin siacTSoggettoMod, DatiOperazioneDto datiOperazioneDto)
	{
		SiacRFormaGiuridicaModFin rFormaGiuridicaMod = new SiacRFormaGiuridicaModFin();
		rFormaGiuridicaMod = DatiOperazioneUtils.impostaDatiOperazioneLogin(rFormaGiuridicaMod,
				datiOperazioneDto, siacTAccountRepository);
		rFormaGiuridicaMod.setSiacTSoggettoMod(siacTSoggettoMod);
		rFormaGiuridicaMod.setSiacTFormaGiuridica(siacTFormaGiuridica);
		rFormaGiuridicaMod.setSiacTSoggetto(siacTSoggettoMod.getSiacTSoggetto());
		// Termino restituendo l'oggetto di ritorno:
		return rFormaGiuridicaMod;
	}

	/**
	 * saveSoggettoTipoMod
	 * 
	 * @param tipoSoggetto
	 * @param idEnte
	 * @param siacTSoggettoMod
	 * @param datiOperazioneDto
	 * @return
	 */
	private SiacRSoggettoTipoModFin saveSoggettoTipoMod(TipoSoggetto tipoSoggetto, int idEnte,
			SiacTSoggettoModFin siacTSoggettoMod, DatiOperazioneDto datiOperazioneDto)
	{
		SiacRSoggettoTipoModFin siacRSoggettoTipoMod = buildSoggettoTipoModBeforeSave(tipoSoggetto,
				idEnte, siacTSoggettoMod, datiOperazioneDto);
		if (siacRSoggettoTipoMod != null)
		{
			// salvo sul db:
			siacRSoggettoTipoMod = siacRSoggettoTipoModRepository
					.saveAndFlush(siacRSoggettoTipoMod);
		}
		// Termino restituendo l'oggetto di ritorno:
		return siacRSoggettoTipoMod;
	}

	/**
	 * buildSoggettoTipoModBeforeSave
	 * 
	 * @param tipoSoggetto
	 * @param idEnte
	 * @param siacTSoggettoMod
	 * @param datiOperazioneDto
	 * @return
	 */
	private SiacRSoggettoTipoModFin buildSoggettoTipoModBeforeSave(TipoSoggetto tipoSoggetto,
			int idEnte, SiacTSoggettoModFin siacTSoggettoMod, DatiOperazioneDto datiOperazioneDto)
	{
		SiacDSoggettoTipoFin siacDSoggettoTipo = null;
		SiacRSoggettoTipoModFin siacRSoggettoTipoMod = null;
		if (!StringUtils.isEmpty(tipoSoggetto.getCodice()))
		{
			String code = tipoSoggetto.getCodice().trim().toUpperCase();
			siacDSoggettoTipo = siacDSoggettoTipoRepository.findValidoByCode(idEnte, code,
					datiOperazioneDto.getTs()).get(0);
		}
		else if (!StringUtils.isEmpty(tipoSoggetto.getSoggettoTipoCode()))
		{
			String code = tipoSoggetto.getSoggettoTipoCode().trim().toUpperCase();
			siacDSoggettoTipo = siacDSoggettoTipoRepository.findValidoByCode(idEnte, code,
					datiOperazioneDto.getTs()).get(0);
		}

		if (siacDSoggettoTipo != null)
		{
			siacRSoggettoTipoMod = new SiacRSoggettoTipoModFin();
			siacRSoggettoTipoMod = DatiOperazioneUtils.impostaDatiOperazioneLogin(
					siacRSoggettoTipoMod, datiOperazioneDto, siacTAccountRepository);
			siacRSoggettoTipoMod.setSiacTSoggettoMod(siacTSoggettoMod);
			siacRSoggettoTipoMod.setSiacTSoggetto(siacTSoggettoMod.getSiacTSoggetto());
			siacRSoggettoTipoMod.setSiacDSoggettoTipo(siacDSoggettoTipo);
		}
		// Termino restituendo l'oggetto di ritorno:
		return siacRSoggettoTipoMod;
	}


	private SiacRSoggettoStatoFin salvaStatoOperativoAnagrafica(StatoOperativoAnagrafica statoOperativoAnagrafica,
			SiacTSoggettoFin siacTSoggetto, Integer idEnte, DatiOperazioneDto datiOperazioneDto) {
		return salvaStatoOperativoAnagrafica(statoOperativoAnagrafica, siacTSoggetto, idEnte, datiOperazioneDto, null);
	}

		/**
	 * salvaStatoOperativoAnagrafica
	 * 
	 * @param statoOperativoAnagrafica
	 * @param siacTSoggetto
	 * @param idEnte
	 * @param datiOperazioneDto
	 * @param notaOperazione 
	 * @return
	 */

	private SiacRSoggettoStatoFin salvaStatoOperativoAnagrafica(
			StatoOperativoAnagrafica statoOperativoAnagrafica, SiacTSoggettoFin siacTSoggetto,
			Integer idEnte, DatiOperazioneDto datiOperazioneDto, String notaOperazione)
	{
		String statoCode = statoOperativoAnagrafica.name();
		SiacRSoggettoStatoFin siacRSoggettoStato = null;
		boolean inserisciPrimaDiCancella = false;
		if (datiOperazioneDto.getOperazione().equals(Operazione.INSERIMENTO)
				|| datiOperazioneDto.getOperazione().equals(Operazione.SOSPENDI)
				|| datiOperazioneDto.getOperazione().equals(Operazione.BLOCCA))
		{
			inserisciPrimaDiCancella = true;
		}
		// invalido il vecchio stato (se siamo in aggiornamento):
		if (siacTSoggetto.getSoggettoId() != null && siacTSoggetto.getSoggettoId().intValue() > 0)
		{
			List<SiacRSoggettoStatoFin> statoOldL = siacRSoggettoStatoRepository
					.findValidoByIdSoggetto(siacTSoggetto.getSoggettoId(),
							datiOperazioneDto.getTs());
			if (statoOldL != null && statoOldL.size() > 0)
			{
				SiacRSoggettoStatoFin statoOld = statoOldL.get(0);
				if (statoOld != null)
				{
					String codeOld = statoOld.getSiacDSoggettoStato().getSoggettoStatoCode();
					if (statoCode.equals(codeOld))
					{
						// se sono uguali non c'e' nessun passaggio di stato
						siacRSoggettoStato = statoOld;
					}
					else
					{
						/*
						 * VARIAZIONE per correzione Jira-352
						 */
						if ((!datiOperazioneDto.getOperazione().equals(
								Operazione.CANCELLAZIONE_LOGICA_RECORD) && statoOperativoAnagrafica
								.equals(StatoOperativoAnagrafica.ANNULLATO))
								|| statoOperativoAnagrafica.equals(StatoOperativoAnagrafica.VALIDO))
						{

							DatiOperazioneUtils.annullaRecord(statoOld,
									siacRSoggettoStatoRepository, datiOperazioneDto,
									siacTAccountRepository);
						}
						else
						{
							DatiOperazioneUtils.cancellaRecord(statoOld,
									siacRSoggettoStatoRepository, datiOperazioneDto,
									siacTAccountRepository);
						}
					}
				}
			}
		}
		//
		// inserisco il nuovo stato se lo stato e' <> ANNULLATO:
		if (datiOperazioneDto != null
				&& (!datiOperazioneDto.getOperazione().equals(
						Operazione.CANCELLAZIONE_LOGICA_RECORD) || inserisciPrimaDiCancella))
		{

			List<SiacDSoggettoStatoFin> soggstats = siacDSoggettoStatoRepository
					.findValidoByEnteAndByCode(idEnte, datiOperazioneDto.getTs(), statoCode);
			SiacDSoggettoStatoFin soggettoStato = soggstats.get(0);
			// JIra 1381
			if (siacRSoggettoStato == null)
			{
				siacRSoggettoStato = new SiacRSoggettoStatoFin();
			}
			datiOperazioneDto.setOperazione(Operazione.INSERIMENTO);
			siacRSoggettoStato = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRSoggettoStato,
					datiOperazioneDto, siacTAccountRepository);
			siacRSoggettoStato.setSiacDSoggettoStato(soggettoStato);
			siacRSoggettoStato.setSiacTSoggetto(siacTSoggetto);
			siacRSoggettoStato.setNotaOperazione(notaOperazione);
			// salvo sul db:
			siacRSoggettoStato = siacRSoggettoStatoRepository.saveAndFlush(siacRSoggettoStato);

		}
		// Termino restituendo l'oggetto di ritorno:
		return siacRSoggettoStato;
	}

	/**
	 * salvaOnere
	 * 
	 * @param siacTSoggetto
	 * @param idEnte
	 * @param codeOnere
	 * @param datiOperazioneInserisci
	 */
	private void salvaOnere(SiacTSoggettoFin siacTSoggetto, Integer idEnte, String codeOnere,
			DatiOperazioneDto datiOperazioneInserisci)
	{
		SiacRSoggettoOnereFin siacRSoggettoOnere = new SiacRSoggettoOnereFin();
		siacRSoggettoOnere = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRSoggettoOnere,
				datiOperazioneInserisci, siacTAccountRepository);
		siacRSoggettoOnere.setSiacTSoggetto(siacTSoggetto);
		SiacDOnereFin siacDOnere = siacDOnereRepository.findOnereValidoByCode(idEnte, codeOnere,
				datiOperazioneInserisci.getTs()).get(0);
		siacRSoggettoOnere.setSiacDOnere(siacDOnere);
		// salvo sul db:
		siacRSoggettoOnereRepository.saveAndFlush(siacRSoggettoOnere);
	}

	/**
	 * salvaOnereMod
	 * 
	 * @param siacTSoggetto
	 * @param siacTSoggettoMod
	 * @param idEnte
	 * @param codeOnere
	 * @param datiOperazioneInserisci
	 */
	private void salvaOnereMod(SiacTSoggettoFin siacTSoggetto, SiacTSoggettoModFin siacTSoggettoMod,
			Integer idEnte, String codeOnere, DatiOperazioneDto datiOperazioneInserisci)
	{
		SiacRSoggettoOnereModFin siacRSoggettoOnere = new SiacRSoggettoOnereModFin();
		siacRSoggettoOnere = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRSoggettoOnere,
				datiOperazioneInserisci, siacTAccountRepository);
		siacRSoggettoOnere.setDataInizioValidita(getNow());
		siacRSoggettoOnere.setSiacTSoggetto(siacTSoggetto);
		SiacDOnereFin siacDOnere = siacDOnereRepository.findOnereValidoByCode(idEnte, codeOnere,
				datiOperazioneInserisci.getTs()).get(0);
		siacRSoggettoOnere.setSiacDOnere(siacDOnere);
		siacRSoggettoOnere.setSiacTSoggettoMod(siacTSoggettoMod);
		// salvo sul db:
		siacRSoggettoOnereModRepository.saveAndFlush(siacRSoggettoOnere);
	}

	/**
	 * salvaSoggettoClasse
	 * 
	 * @param siacTSoggetto
	 * @param idEnte
	 * @param idAmbito
	 * @param codeClass
	 * @param datiOperazioneDto
	 */
	private void salvaSoggettoClasse(SiacTSoggettoFin siacTSoggetto, Integer idEnte, Integer idAmbito,
			String codeClass, DatiOperazioneDto datiOperazioneDto)
	{
		SiacRSoggettoClasseFin siacRSoggettoClasse = new SiacRSoggettoClasseFin();
		SiacDSoggettoClasseFin siacDSoggettoClasse = siacDSoggettoClasseRepository.findValidoByCode(
				idEnte, idAmbito, codeClass, datiOperazioneDto.getTs()).get(0);
		siacRSoggettoClasse.setSiacDSoggettoClasse(siacDSoggettoClasse);
		siacRSoggettoClasse = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRSoggettoClasse,
				datiOperazioneDto, siacTAccountRepository);
		siacRSoggettoClasse.setSiacTSoggetto(siacTSoggetto);
		// salvo sul db:
		siacRSoggettoClasseRepository.saveAndFlush(siacRSoggettoClasse);
	}

	/**
	 * salvaSoggettoClasseMod
	 * 
	 * @param siacTSoggetto
	 * @param siacTSoggettoMod
	 * @param idEnte
	 * @param idAmbito
	 * @param codeClass
	 * @param datiOperazioneInserisci
	 * @return
	 */
	private SiacRSoggettoClasseModFin salvaSoggettoClasseMod(SiacTSoggettoFin siacTSoggetto,
			SiacTSoggettoModFin siacTSoggettoMod, Integer idEnte, Integer idAmbito, String codeClass,
			DatiOperazioneDto datiOperazioneInserisci)
	{
		SiacRSoggettoClasseModFin siacRSoggettoClasseMod = new SiacRSoggettoClasseModFin();
		SiacDSoggettoClasseFin siacDSoggettoClasse = siacDSoggettoClasseRepository.findValidoByCode(
				idEnte, idAmbito, codeClass, datiOperazioneInserisci.getTs()).get(0);
		siacRSoggettoClasseMod.setSiacDSoggettoClasse(siacDSoggettoClasse);
		siacRSoggettoClasseMod = DatiOperazioneUtils.impostaDatiOperazioneLogin(
				siacRSoggettoClasseMod, datiOperazioneInserisci, siacTAccountRepository);
		siacRSoggettoClasseMod.setDataInizioValidita(getNow());
		siacRSoggettoClasseMod.setSiacTSoggetto(siacTSoggetto);
		siacRSoggettoClasseMod.setSiacTSoggettoMod(siacTSoggettoMod);
		// salvo sul db:
		siacRSoggettoClasseMod = siacRSoggettoClasseModRepository
				.saveAndFlush(siacRSoggettoClasseMod);
		// Termino restituendo l'oggetto di ritorno:
		return siacRSoggettoClasseMod;
	}

	/**
	 * salvaContatto
	 * 
	 * @param contatto
	 * @param siacTSoggetto
	 * @param idEnte
	 * @param datiOperazioneInserisci
	 * @param idRecapitoDaAggiornare
	 */
	private void salvaContatto(Contatto contatto, SiacTSoggettoFin siacTSoggetto, Integer idEnte,
			DatiOperazioneDto datiOperazioneInserisci, Integer idRecapitoDaAggiornare)
	{
		String avviso = contatto.getAvviso();
		String descrizione = StringUtils.toUpperSafe(contatto.getDescrizione());
		String contattoCodModo = contatto.getContattoCodModo();
		SiacDRecapitoModoFin siacDRecapitoModo = siacDRecapitoModoRepository.findValidoByCode(idEnte,
				datiOperazioneInserisci.getTs(), contattoCodModo).get(0);
		SiacTRecapitoSoggettoFin siacTRecapitoSoggetto = new SiacTRecapitoSoggettoFin();
		if (idRecapitoDaAggiornare != null)
		{
			siacTRecapitoSoggetto.setRecapitoId(idRecapitoDaAggiornare);
		}
		siacTRecapitoSoggetto = DatiOperazioneUtils.impostaDatiOperazioneLogin(
				siacTRecapitoSoggetto, datiOperazioneInserisci, siacTAccountRepository);
		siacTRecapitoSoggetto.setSiacDRecapitoModo(siacDRecapitoModo);
		siacTRecapitoSoggetto.setSiacTSoggetto(siacTSoggetto);
		siacTRecapitoSoggetto.setRecapitoCode(siacDRecapitoModo.getRecapitoModoDesc());
		siacTRecapitoSoggetto.setRecapitoDesc(descrizione);
		siacTRecapitoSoggetto.setAvviso(StringUtils.checkStringBooleanForDb(avviso));
		// salvo sul db:
		siacTRecapitoSoggettoRepository.saveAndFlush(siacTRecapitoSoggetto);
	}

	/**
	 * puo' essere usato sia per insert che per update: se
	 * idRecapitoDaAggiornare e' valorizzato si comporta da update altrimenti da
	 * insert
	 * 
	 * @param contatto
	 * @param siacTSoggetto
	 * @param siacTSoggettoMod
	 * @param idEnte
	 * @param datiOperazioneInserisci
	 * @param idRecapitoDaAggiornare
	 */
	private void salvaContattoMod(Contatto contatto, SiacTSoggettoFin siacTSoggetto,
			SiacTSoggettoModFin siacTSoggettoMod, Integer idEnte,
			DatiOperazioneDto datiOperazioneInserisci, Integer idRecapitoDaAggiornare)
	{
		String avviso = contatto.getAvviso();
		String descrizione = contatto.getDescrizione();
		String contattoCodModo = contatto.getContattoCodModo();
		SiacDRecapitoModoFin siacDRecapitoModo = siacDRecapitoModoRepository.findValidoByCode(idEnte,
				datiOperazioneInserisci.getTs(), contattoCodModo).get(0);
		SiacTRecapitoSoggettoModFin siacTRecapitoSoggettoMod = new SiacTRecapitoSoggettoModFin();
		if (idRecapitoDaAggiornare != null)
		{
			siacTRecapitoSoggettoMod.setRecapitoModId(idRecapitoDaAggiornare);
		}
		
		
		siacTRecapitoSoggettoMod = DatiOperazioneUtils.impostaDatiOperazioneLogin(
				siacTRecapitoSoggettoMod, datiOperazioneInserisci, siacTAccountRepository);
		siacTRecapitoSoggettoMod.setSiacDRecapitoModo(siacDRecapitoModo);
		siacTRecapitoSoggettoMod.setSiacTSoggetto(siacTSoggetto);
		siacTRecapitoSoggettoMod.setRecapitoCode(siacDRecapitoModo.getRecapitoModoDesc());
		siacTRecapitoSoggettoMod.setRecapitoDesc(descrizione);
		siacTRecapitoSoggettoMod.setAvviso(StringUtils.checkStringBooleanForDb(avviso));
		siacTRecapitoSoggettoMod.setSiacTSoggettoMod(siacTSoggettoMod);
		// salvo sul db:
		
		//fix per jira  SIAC-2255 idx_siac_t_recapito_soggetto_mod_1 prevede anche la validita inzio
		siacTRecapitoSoggettoMod.setDataInizioValidita(getNow());
		//
		siacTRecapitoSoggettoModRepository.saveAndFlush(siacTRecapitoSoggettoMod);
	}

	private SiacRModpagOrdineFin calcolaMaxOrdine(Integer idEnte, DatiOperazioneDto datiOperazioneDto,
			SiacTSoggettoFin siacTSoggetto, SiacTModpagFin siacTModpag,
			SiacRSoggrelModpagFin siacRSoggrelModpag)
	{
		// calcolo il max per la nuova numerazione del MDP
		Integer maxDb = siacRModpagOrdineRepository.findMaxOrdineModPag(idEnte,
				siacTSoggetto.getUid(), getNow());

		Integer nuovo = null;

		SiacRModpagOrdineFin siacRModpagOrdine = null;

		if (null == maxDb || maxDb == 0)
		{
			siacRModpagOrdine = new SiacRModpagOrdineFin();
			// se non c'e' niente allora il numeratore parte da 1
			siacRModpagOrdine.setOrdine(1);
		}
		else
		{
			// incremento di 1
			nuovo = new Integer(maxDb);
			nuovo = nuovo + 1;
			siacRModpagOrdine = new SiacRModpagOrdineFin();
			siacRModpagOrdine.setOrdine(nuovo);
		}

		siacRModpagOrdine.setUid(null);
		siacRModpagOrdine = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRModpagOrdine,
				datiOperazioneDto, siacTAccountRepository);
		siacRModpagOrdine.setSiacTSoggetto(siacTSoggetto);

		if (null == siacRSoggrelModpag)
		{
			// allora sto inserendo una MDP tipo contanti o banca
			siacRModpagOrdine.setSiacTModpag(siacTModpag);
		}

		if (null == siacTModpag)
		{
			// allora sto inserendo una cessione
			siacRModpagOrdine.setSiacRSoggrelModpag(siacRSoggrelModpag);
		}

		siacRModpagOrdine.setLoginCreazione(siacRModpagOrdine.getLoginOperazione());
		siacRModpagOrdine.setDataCreazione(new Date());
		siacRModpagOrdine.setDataInizioValidita(new Date());

		return siacRModpagOrdine;
	}

	/**
	 * saveModalitaPagamento
	 * 
	 * @param modPagToInsert
	 * @param siacTSoggetto
	 * @param idEnte
	 * @param datiOperazioneDto
	 * @return
	 */
	private ModalitaPagamentoSoggetto saveModalitaPagamento(
			ModalitaPagamentoSoggetto modPagToInsert, SiacTSoggettoFin siacTSoggetto,
			String codiceAmbito, Integer idEnte, DatiOperazioneDto datiOperazioneDto)
	{
		ModalitaPagamentoSoggetto mdpNew = modPagToInsert;

		datiOperazioneDto.setOperazione(Operazione.INSERIMENTO);

		SiacTModpagFin siacTModPagToInsert = new SiacTModpagFin();
		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository
				.findOne(idEnte);
		// qui devo prendere la modalita scelta e andare in join con il gruppo
		SiacDAccreditoTipoFin dAccreditoTipoForCheck = siacDTipoAccreditoRepository
				.findTipoAccreditoValidoPerGruppoByCode(idEnte,
						modPagToInsert.getModalitaAccreditoSoggetto().getCodice(),
						datiOperazioneDto.getTs()).get(0);
		// SiacDAccreditoTipoFin dAccreditoTipoForCheck =
		// siacDTipoAccreditoRepository.findTipoAccreditoValidoByCode(idEnte,
		// modPagToInsert.getTipoAccredito().toString(),datiOperazioneDto.getTs()).get(0);
		SiacDAccreditoGruppoFin dAccreditoGruppoForCheck = dAccreditoTipoForCheck
				.getSiacDAccreditoGruppo(); // siacDAccreditoGruppoRepository.findGruppoByTipoId(idEnte,
											// dAccreditoTipoForCheck.getSiacDAccreditoGruppo().getUid());

		// Controllo che sia di tipo bancario
		if (dAccreditoGruppoForCheck.getAccreditoGruppoCode().equals(
				Constanti.D_ACCREDITO_TIPO_CODE_Circuito_bancario)
				|| dAccreditoGruppoForCheck.getAccreditoGruppoCode().equals(
						Constanti.D_ACCREDITO_TIPO_CODE_Circuito_Banca_d_Italia)
				|| dAccreditoGruppoForCheck.getAccreditoGruppoCode().equals(
						Constanti.D_ACCREDITO_TIPO_CODE_Circuito_Postale))
		{

			siacTModPagToInsert = DatiOperazioneUtils.impostaDatiOperazioneLogin(
					siacTModPagToInsert, datiOperazioneDto, siacTAccountRepository);

			if (modPagToInsert.getBic() != null)
			{
				siacTModPagToInsert.setBic(modPagToInsert.getBic().trim());
			}

			if (modPagToInsert.getDenominazioneBanca() != null)
			{
				siacTModPagToInsert.setDenominazioneBanca(modPagToInsert.getDenominazioneBanca().trim());
			}

			if (modPagToInsert.getIban() != null)
			{
				if (!modPagToInsert.getIban().trim().isEmpty())
				{
					siacTModPagToInsert.setIban(modPagToInsert.getIban().trim());
				}

			}
			if (modPagToInsert.getContoCorrente() != null)
			{
				siacTModPagToInsert.setContocorrente(modPagToInsert.getContoCorrente().trim());
			}

			// NUOVO CAMPO
			// Intestazione conto
			if (modPagToInsert.getIntestazioneConto() != null)
			{
				siacTModPagToInsert.setContocorrenteIntestazione(modPagToInsert
						.getIntestazioneConto());
			}
			
			
			siacTModPagToInsert.setPerStipendi(modPagToInsert.getPerStipendi());


			// fine NUOVO CAMPO

			// Inserire Associazione modalita Pagamento

			// Gestisco il tipo accredito

			// SiacDAccreditoTipoFin siacDAccreditoTipo =
			// siacDTipoAccreditoRepository.findTipoAccreditoValidoByCode(idEnte,
			// modPagToInsert.getTipoAccredito().toString(),datiOperazioneDto.getTs()).get(0);
			SiacDAccreditoTipoFin siacDAccreditoTipo = siacDTipoAccreditoRepository
					.findTipoAccreditoValidoByCode(idEnte,
							modPagToInsert.getModalitaAccreditoSoggetto().getCodice(),
							datiOperazioneDto.getTs()).get(0);
			siacTModPagToInsert.setSiacDAccreditoTipo(siacDAccreditoTipo);

			if (modPagToInsert.getNote() != null)
			{
				siacTModPagToInsert.setNote(modPagToInsert.getNote());
			}
			if (modPagToInsert.getDataFineValidita() != null)
			{
				// se data fine validita valorizzata
				// setto alla mezzanotte:
				Timestamp dataScadenza = TimingUtils.setToMidNigth(modPagToInsert
						.getDataFineValidita());
				siacTModPagToInsert.setDataFineValidita(dataScadenza);
			}

			// se e' valido data inizio e data validita'
			siacTModPagToInsert.setDataCreazione(new Date());
			siacTModPagToInsert.setDataInizioValidita(new Date());

			if (siacTSoggetto.getSoggettoCode().equalsIgnoreCase(
					String.valueOf(modPagToInsert.getCodiceSoggettoAssociato())))
			{
				siacTModPagToInsert.setSiacTSoggetto(siacTSoggetto);
			}
			else
			{
				SiacTSoggettoFin sedSec = siacTSoggettoRepository.ricercaSedeSecondariaPerChiave(
						idEnte, siacTSoggetto.getSoggettoCode(),
						modPagToInsert.getCodiceSoggettoAssociato(), Constanti.SEDE_SECONDARIA);
				if (sedSec != null)
				{
					siacTModPagToInsert.setSiacTSoggetto(sedSec);
				}
				else
				{
					siacTModPagToInsert.setSiacTSoggetto(siacTSoggetto);
				}
			}
			// salvo sul db:
			SiacTModpagFin siacTModPagInserito = siacTModpagRepository
					.saveAndFlush(siacTModPagToInsert);

			// DA CONTROLLARE
			// inserisco lo stato della modalita di pagamento
			SiacRModpagStatoFin siacRModpagStato = new SiacRModpagStatoFin();

			siacRModpagStato = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRModpagStato,
					datiOperazioneDto, siacTAccountRepository);
			siacRModpagStato.setSiacTModpag(siacTModPagInserito);
			siacRModpagStato.setDataCreazione(new Date());
			siacRModpagStato.setDataInizioValidita(new Date());
			SiacDModpagStatoFin siacDModpagStato = siacDModpagStatoRepository
					.findModPagStatoDValidoByCode(idEnte,
							modPagToInsert.getDescrizioneStatoModalitaPagamento().toUpperCase(),
							datiOperazioneDto.getTs()).get(0);
			siacRModpagStato.setSiacDModpagStato(siacDModpagStato);
			// salvo sul db:
			siacRModpagStatoRepository.saveAndFlush(siacRModpagStato);

			mdpNew.setUid(siacTModPagInserito.getUid());

			// calcolo il max per la nuova numerazione del MDP
			SiacRModpagOrdineFin siacRModpagOrdine = calcolaMaxOrdine(idEnte, datiOperazioneDto,
					siacTSoggetto, siacTModPagInserito, null);
			// inserisco il dato nella tavola siac_r_modpag_ordine
			siacRModpagOrdineRepository.saveAndFlush(siacRModpagOrdine);

		}

		// Controllo che sia di tipo generico
		if (dAccreditoGruppoForCheck.getAccreditoGruppoCode().equals(
				Constanti.D_ACCREDITO_TIPO_CODE_Generico))
		{

			siacTModPagToInsert = DatiOperazioneUtils.impostaDatiOperazioneLogin(
					siacTModPagToInsert, datiOperazioneDto, siacTAccountRepository);

			if (modPagToInsert.getNote() != null)
			{
				siacTModPagToInsert.setNote(modPagToInsert.getNote());
			}
			if (modPagToInsert.getDataFineValidita() != null)
			{
				// se data fine validita valorizzata
				// settiamo alla mezzanotte:
				Timestamp dataScadenza = TimingUtils.setToMidNigth(modPagToInsert
						.getDataFineValidita());
				siacTModPagToInsert.setDataFineValidita(dataScadenza);
			}

			SiacDAccreditoTipoFin siacDAccreditoTipo = siacDTipoAccreditoRepository
					.findTipoAccreditoValidoByCode(idEnte,
							modPagToInsert.getModalitaAccreditoSoggetto().getCodice(),
							datiOperazioneDto.getTs()).get(0);
			siacTModPagToInsert.setSiacDAccreditoTipo(siacDAccreditoTipo);

			siacTModPagToInsert.setDataCreazione(new Date());
			siacTModPagToInsert.setDataInizioValidita(new Date());

			if (siacTSoggetto.getSoggettoCode().equalsIgnoreCase(
					String.valueOf(modPagToInsert.getCodiceSoggettoAssociato())))
			{
				siacTModPagToInsert.setSiacTSoggetto(siacTSoggetto);
			}
			else
			{
				SiacTSoggettoFin sedSec = siacTSoggettoRepository.ricercaSedeSecondariaPerChiave(
						idEnte, siacTSoggetto.getSoggettoCode(),
						modPagToInsert.getCodiceSoggettoAssociato(), Constanti.SEDE_SECONDARIA);

				if (sedSec != null)
				{
					siacTModPagToInsert.setSiacTSoggetto(sedSec);
				}
				else
				{
					siacTModPagToInsert.setSiacTSoggetto(siacTSoggetto);
				}
			}
			
			siacTModPagToInsert.setPerStipendi(modPagToInsert.getPerStipendi());

			// salvo sul db:
			SiacTModpagFin siacTModpagInserito = siacTModpagRepository
					.saveAndFlush(siacTModPagToInsert);

			// inserisco lo stato della modalita di pagamento
			SiacRModpagStatoFin siacRModpagStato = new SiacRModpagStatoFin();

			siacRModpagStato = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRModpagStato,
					datiOperazioneDto, siacTAccountRepository);

			siacRModpagStato.setSiacTModpag(siacTModpagInserito);

			siacRModpagStato.setDataCreazione(new Date());
			siacRModpagStato.setDataInizioValidita(new Date());

			SiacDModpagStatoFin siacDModpagStato = siacDModpagStatoRepository
					.findModPagStatoDValidoByCode(idEnte,
							modPagToInsert.getDescrizioneStatoModalitaPagamento().toUpperCase(),
							datiOperazioneDto.getTs()).get(0);
			siacRModpagStato.setSiacDModpagStato(siacDModpagStato);
			// salvo sul db:
			siacRModpagStatoRepository.saveAndFlush(siacRModpagStato);
			mdpNew.setUid(siacTModpagInserito.getUid());

			// calcolo il max per la nuova numerazione del MDP
			SiacRModpagOrdineFin siacRModpagOrdine = calcolaMaxOrdine(idEnte, datiOperazioneDto,
					siacTSoggetto, siacTModpagInserito, null);
			// inserisco il dato nella tavola siac_r_modpag_ordine
			siacRModpagOrdineRepository.saveAndFlush(siacRModpagOrdine);

		}

		// Controllo che sia di tipo contante
		if (dAccreditoGruppoForCheck.getAccreditoGruppoCode().equals(
				Constanti.D_ACCREDITO_TIPO_CODE_Contanti))
		{

			siacTModPagToInsert = DatiOperazioneUtils.impostaDatiOperazioneLogin(
					siacTModPagToInsert, datiOperazioneDto, siacTAccountRepository);

			if (modPagToInsert.getCodiceFiscaleQuietanzante() != null)
			{
				siacTModPagToInsert.setQuietanzianteCodiceFiscale(modPagToInsert
						.getCodiceFiscaleQuietanzante().trim());
			}
			if (modPagToInsert.getSoggettoQuietanzante() != null)
			{
				siacTModPagToInsert.setQuietanziante(modPagToInsert.getSoggettoQuietanzante());
			}

			// NUOVI CAMPI
			// data nascita quietanzante
			if (modPagToInsert.getDataNascitaQuietanzante() != null)
			{
				siacTModPagToInsert.setQuietanzanteNascitaData(TimingUtils
						.buildTimestamp(modPagToInsert.getDataNascitaQuietanzante()));
			}

			// nazione di nascita e luogo di nascita
			if (modPagToInsert.getComuneNascita() != null)
			{
				// nazione
				siacTModPagToInsert.setQuietanzianteNascitaStato(modPagToInsert.getComuneNascita()
						.getNazioneCode());
				// luogo
				siacTModPagToInsert.setQuietanzianteNascitaLuogo(modPagToInsert.getComuneNascita()
						.getDescrizione());

			}
			
			
			siacTModPagToInsert.setPerStipendi(modPagToInsert.getPerStipendi());

			// fine NUOVI CAMPI

			if (modPagToInsert.getNote() != null)
			{
				siacTModPagToInsert.setNote(modPagToInsert.getNote());
			}
			if (modPagToInsert.getDataFineValidita() != null)
			{
				// se data fine validita valorizzata
				// settiamo alla mezzanotte:
				Timestamp dataScadenza = TimingUtils.setToMidNigth(modPagToInsert
						.getDataFineValidita());
				siacTModPagToInsert.setDataFineValidita(dataScadenza);
			}

			SiacDAccreditoTipoFin siacDAccreditoTipo = siacDTipoAccreditoRepository
					.findTipoAccreditoValidoByCode(idEnte,
							modPagToInsert.getModalitaAccreditoSoggetto().getCodice(),
							datiOperazioneDto.getTs()).get(0);
			siacTModPagToInsert.setSiacDAccreditoTipo(siacDAccreditoTipo);

			siacTModPagToInsert.setDataCreazione(new Date());
			siacTModPagToInsert.setDataInizioValidita(new Date());

			if (siacTSoggetto.getSoggettoCode().equalsIgnoreCase(
					String.valueOf(modPagToInsert.getCodiceSoggettoAssociato())))
			{
				siacTModPagToInsert.setSiacTSoggetto(siacTSoggetto);
			}
			else
			{
				SiacTSoggettoFin sedSec = siacTSoggettoRepository.ricercaSedeSecondariaPerChiave(
						idEnte, siacTSoggetto.getSoggettoCode(),
						modPagToInsert.getCodiceSoggettoAssociato(), Constanti.SEDE_SECONDARIA);

				if (sedSec != null)
				{
					siacTModPagToInsert.setSiacTSoggetto(sedSec);
				}
				else
				{
					siacTModPagToInsert.setSiacTSoggetto(siacTSoggetto);
				}
			}
			// salvo sul db:
			SiacTModpagFin siacTModpagInserito = siacTModpagRepository
					.saveAndFlush(siacTModPagToInsert);

			// inserisco lo stato della modalita di pagamento
			SiacRModpagStatoFin siacRModpagStato = new SiacRModpagStatoFin();

			siacRModpagStato = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRModpagStato,
					datiOperazioneDto, siacTAccountRepository);

			siacRModpagStato.setSiacTModpag(siacTModpagInserito);

			siacRModpagStato.setDataCreazione(new Date());
			siacRModpagStato.setDataInizioValidita(new Date());

			SiacDModpagStatoFin siacDModpagStato = siacDModpagStatoRepository
					.findModPagStatoDValidoByCode(idEnte,
							modPagToInsert.getDescrizioneStatoModalitaPagamento().toUpperCase(),
							datiOperazioneDto.getTs()).get(0);
			siacRModpagStato.setSiacDModpagStato(siacDModpagStato);
			// salvo sul db:
			siacRModpagStatoRepository.saveAndFlush(siacRModpagStato);
			mdpNew.setUid(siacTModpagInserito.getUid());

			// calcolo il max per la nuova numerazione del MDP
			SiacRModpagOrdineFin siacRModpagOrdine = calcolaMaxOrdine(idEnte, datiOperazioneDto,
					siacTSoggetto, siacTModpagInserito, null);
			// inserisco il dato nella tavola siac_r_modpag_ordine
			siacRModpagOrdineRepository.saveAndFlush(siacRModpagOrdine);

		}

		// Controllo che sia di tipo cessione
		if (dAccreditoGruppoForCheck.getAccreditoGruppoCode().equals(
				Constanti.D_ACCREDITO_TIPO_CODE_Cessione_del_credito)
				|| dAccreditoGruppoForCheck.getAccreditoGruppoCode().equals(
						Constanti.D_ACCREDITO_TIPO_CODE_Cessione_dell_incasso))
		{

			SiacRSoggettoRelazFin soggRelaz = new SiacRSoggettoRelazFin();
			soggRelaz = DatiOperazioneUtils.impostaDatiOperazioneLogin(soggRelaz,
					datiOperazioneDto, siacTAccountRepository);
			SiacDRelazTipoFin siacDRelazTipo = siacDRelazTipoRepository
					.findRelazTipoValidoByCode(idEnte,
							modPagToInsert.getModalitaOriginale().getTipoAccredito().toString(), datiOperazioneDto.getTs())
					.get(0);
			soggRelaz.setSiacDRelazTipo(siacDRelazTipo);
			soggRelaz.setDataCreazione(new Date());
			soggRelaz.setDataInizioValidita(new Date());

			if (siacTSoggetto.getSoggettoCode().equalsIgnoreCase(
					String.valueOf(modPagToInsert.getCodiceSoggettoAssociato())))
			{
				soggRelaz.setSiacTSoggetto1(siacTSoggetto);
			}
			else
			{
				SiacTSoggettoFin sedSec = siacTSoggettoRepository.ricercaSedeSecondariaPerChiave(
						idEnte, siacTSoggetto.getSoggettoCode(),
						modPagToInsert.getCodiceSoggettoAssociato(), Constanti.SEDE_SECONDARIA);
				if (sedSec != null)
				{
					soggRelaz.setSiacTSoggetto1(sedSec);
				}
				else
				{
					soggRelaz.setSiacTSoggetto1(siacTSoggetto);
				}
			}

			// Setto soggetto a
			SiacTSoggettoFin soggA = siacTSoggettoRepository.ricercaSoggettoNoSeSede(codiceAmbito,
					idEnte, modPagToInsert.getCessioneCodSoggetto(), Constanti.SEDE_SECONDARIA,
					getNow());
			soggRelaz.setSiacTSoggetto2(soggA);

			soggRelaz.setSiacTEnteProprietario(siacTEnteProprietario);

			if (modPagToInsert.getDataFineValidita() != null)
			{
				// se data fine validita valorizzata
				// settiamo alla mezzanotte:
				Timestamp dataScadenza = TimingUtils.setToMidNigth(modPagToInsert
						.getDataFineValidita());
				soggRelaz.setDataFineValidita(dataScadenza);
			}
			// salvo sul db:
			SiacRSoggettoRelazFin soggRelazJustInsert = siacRSoggettoRelazRepository
					.saveAndFlush(soggRelaz);

			// SiacRSoggrel_modPag gestione
			SiacRSoggrelModpagFin siacRSoggrelModpag = new SiacRSoggrelModpagFin();
			siacRSoggrelModpag = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRSoggrelModpag,
					datiOperazioneDto, siacTAccountRepository);
			siacRSoggrelModpag.setSiacRSoggettoRelaz(soggRelazJustInsert);
			siacRSoggrelModpag.setSiacTEnteProprietario(siacTEnteProprietario);

			siacRSoggrelModpag.setDataCreazione(new Date());
			siacRSoggrelModpag.setDataInizioValidita(new Date());

			if (soggRelaz.getDataFineValidita() != null)
			{
				// se data fine validita valorizzata
				siacRSoggrelModpag.setDataFineValidita(soggRelaz.getDataFineValidita());
			}

			if (modPagToInsert.getNote() != null)
			{
				siacRSoggrelModpag.setNote(modPagToInsert.getNote());
			}

			// Inserisco dentro la tabella siac r soggetto relaz stato
			String relazCode = "";
			if (modPagToInsert.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase(
					Constanti.STATO_VALIDO))
			{
				relazCode = Constanti.STATO_VALIDO;
			}
			else
			{
				relazCode = Constanti.STATO_PROVVISORIO;
			}

			siacTModPagToInsert = siacTModpagRepository.findOne(Integer.valueOf(modPagToInsert
					.getCessioneCodModPag()));
			siacRSoggrelModpag.setSiacTModpag(siacTModPagToInsert);
			// salvo sul db:
			siacRSoggrelModpag = siacRSoggrelModpagRepository.saveAndFlush(siacRSoggrelModpag);

			SiacDRelazStatoFin siacDRelazStato = siacDRelazStatoRepository
					.findDRelazStatoValidoByCode(idEnte, relazCode).get(0);
			SiacRSoggettoRelazStatoFin siacRSoggettoRelazStato = new SiacRSoggettoRelazStatoFin();
			siacRSoggettoRelazStato = DatiOperazioneUtils.impostaDatiOperazioneLogin(
					siacRSoggettoRelazStato, datiOperazioneDto, siacTAccountRepository);
			siacRSoggettoRelazStato.setSiacDRelazStato(siacDRelazStato);
			siacRSoggettoRelazStato.setSiacRSoggettoRelaz(soggRelazJustInsert);
			// salvo sul db:
			siacRSoggettoRelazStatoRepository.saveAndFlush(siacRSoggettoRelazStato);

			mdpNew.setUid(siacRSoggrelModpag.getUid());

			// calcolo il max per la nuova numerazione del MDP
			SiacRModpagOrdineFin siacRModpagOrdine = calcolaMaxOrdine(idEnte, datiOperazioneDto,
					siacTSoggetto, null, siacRSoggrelModpag);
			// inserisco il dato nella tavola siac_r_modpag_ordine
			siacRModpagOrdineRepository.saveAndFlush(siacRModpagOrdine);

		}

		// Termino restituendo l'oggetto di ritorno:
		return mdpNew;
	}

	/**
	 * updateModalitaPagamentoMod
	 * 
	 * @param siacTsoggetto
	 * @param idEnte
	 * @param updateMod
	 * @param datiOperazione
	 */
	private void updateModalitaPagamentoMod(SiacTSoggettoFin siacTsoggetto, Integer idEnte,
			ModalitaPagamentoSoggetto updateMod, DatiOperazioneDto datiOperazione)
	{

		datiOperazione.setOperazione(Operazione.MODIFICA);

		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository
				.findOne(idEnte);

		SiacDAccreditoTipoFin dAccreditoTipoForCheck = siacDTipoAccreditoRepository
				.findTipoAccreditoValidoByCode(idEnte,
						updateMod.getModalitaAccreditoSoggetto().getCodice(),
						datiOperazione.getTs()).get(0);
		SiacDAccreditoGruppoFin dAccreditoGruppoForCheck = dAccreditoTipoForCheck.getSiacDAccreditoGruppo();
				// siacDAccreditoGruppoRepository.findGruppoByTipoId(idEnte, dAccreditoTipoForCheck.getSiacDAccreditoGruppo().getUid());

		// Controllo che sia di tipo bancario
		if (dAccreditoGruppoForCheck.getAccreditoGruppoCode().equals(
				Constanti.D_ACCREDITO_TIPO_CODE_Circuito_bancario)
				|| dAccreditoGruppoForCheck.getAccreditoGruppoCode().equals(
						Constanti.D_ACCREDITO_TIPO_CODE_Circuito_Banca_d_Italia)
				|| dAccreditoGruppoForCheck.getAccreditoGruppoCode().equals(
						Constanti.D_ACCREDITO_TIPO_CODE_Circuito_Postale))
		{

			SiacTModpagModFin mod = siacTModpagModRepository.findOne(updateMod.getUid());

			mod.setDataCreazione(mod.getDataCreazione());
			mod.setDataModifica(new Date());
			mod.setSiacTEnteProprietario(siacTEnteProprietario);

			if (!StringUtils.isEmpty(updateMod.getIban()))
			{
				mod.setIban(updateMod.getIban());
			}

			if (!StringUtils.isEmpty(updateMod.getBic()))
			{
				mod.setBic(updateMod.getBic());
			}

			if (!StringUtils.isEmpty(updateMod.getDenominazioneBanca()))
			{
				mod.setDenominazioneBanca(updateMod.getDenominazioneBanca());
			}

			if (!StringUtils.isEmpty(updateMod.getContoCorrente()))
			{
				mod.setContocorrente(updateMod.getContoCorrente());
			}

			if (!StringUtils.isEmpty(updateMod.getNote()))
			{
				mod.setNote(updateMod.getNote());
			}

			mod.setPerStipendi(updateMod.getPerStipendi());

			// nuovi campi
			if (!StringUtils.isEmpty(updateMod.getIntestazioneConto()))
			{
				mod.setContocorrenteIntestazione(updateMod.getIntestazioneConto());
			}

			// fine - nuovi campi

			if (updateMod.getDataFineValidita() != null)
			{
				// se data fine validita valorizzata
				// Settiamo alla mezzanotte:
				Timestamp dataScadenza = TimingUtils.setToMidNigth(updateMod.getDataFineValidita());
				mod.setDataFineValidita(dataScadenza);
			}
			// salvo sul db:
			siacTModpagModRepository.saveAndFlush(mod);

		}

		if (dAccreditoGruppoForCheck.getAccreditoGruppoCode().equals(
				Constanti.D_ACCREDITO_TIPO_CODE_Contanti))
		{

			SiacTModpagModFin mod = siacTModpagModRepository.findOne(updateMod.getUid());

			mod.setDataCreazione(mod.getDataCreazione());
			mod.setDataModifica(new Date());
			mod.setSiacTEnteProprietario(siacTEnteProprietario);

			if (!StringUtils.isEmpty(updateMod.getCodiceFiscaleQuietanzante()))
			{
				mod.setQuietanzianteCodiceFiscale(updateMod.getCodiceFiscaleQuietanzante());
			}

			if (!StringUtils.isEmpty(updateMod.getSoggettoQuietanzante()))
			{
				mod.setQuietanziante(updateMod.getSoggettoQuietanzante());
			}

			if (!StringUtils.isEmpty(updateMod.getNote()))
			{
				mod.setNote(updateMod.getNote());
			}

			
			mod.setPerStipendi(updateMod.getPerStipendi());

			// nuovi campi
			// luogo e stato di nascita
			if (updateMod.getComuneNascita() != null)
			{
				mod.setQuietanzianteNascitaLuogo(updateMod.getComuneNascita().getDescrizione());
				mod.setQuietanzianteNascitaStato(updateMod.getComuneNascita().getNazioneCode());
			}

			// data di nascita quietanzante
			if (updateMod.getDataNascitaQuietanzante() != null)
			{
				mod.setQuietanzanteNascitaData(TimingUtils.buildTimestamp(updateMod
						.getDataNascitaQuietanzante()));
			}

			// fine - nuovi campi

			if (updateMod.getDataFineValidita() != null)
			{
				// se data fine validita valorizzata
				// Settiamo alla mezzanotte:
				Timestamp dataScadenza = TimingUtils.setToMidNigth(updateMod.getDataFineValidita());
				mod.setDataFineValidita(dataScadenza);
			}
			// salvo sul db:
			siacTModpagModRepository.saveAndFlush(mod);
		}

		if (dAccreditoGruppoForCheck.getAccreditoGruppoCode().equals(
				Constanti.D_ACCREDITO_TIPO_CODE_Cessione_del_credito)
				|| dAccreditoGruppoForCheck.getAccreditoGruppoCode().equals(
						Constanti.D_ACCREDITO_TIPO_CODE_Cessione_dell_incasso))
		{

			SiacRSoggrelModpagModFin mod = siacRSoggrelModpagModRepository.findOne(updateMod.getUid());

			mod.setDataCreazione(mod.getDataCreazione());
			mod.setDataModifica(new Date());
			mod.setSiacTEnteProprietario(siacTEnteProprietario);

			if (!StringUtils.isEmpty(updateMod.getNote()))
			{
				mod.setNote(updateMod.getNote());
			}

			if (updateMod.getDataFineValidita() != null)
			{
				// se data fine validita valorizzata
				// settiamo alla mezzanotte:
				Timestamp dataScadenza = TimingUtils.setToMidNigth(updateMod.getDataFineValidita());
				mod.setDataFineValidita(dataScadenza);
			}
			// salvo sul db:
			siacRSoggrelModpagModRepository.saveAndFlush(mod);

		}

	}

	/**
	 * salvaModalitaPagamentoMod
	 * 
	 * @param siacTSoggetto
	 * @param idEnte
	 * @param mdpModToInsert
	 * @param datiOperazioneDto
	 */
	private void salvaModalitaPagamentoMod(SiacTSoggettoFin siacTSoggetto, Integer idEnte,
			ModalitaPagamentoSoggetto mdpModToInsert, DatiOperazioneDto datiOperazioneDto)
	{
		SiacTModpagModFin siacTModPagModToInsert = new SiacTModpagModFin();

		datiOperazioneDto.setOperazione(Operazione.MODIFICA);

		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository
				.findOne(idEnte);

		// SiacDAccreditoTipoFin dAccreditoTipoForCheck =
		// siacDTipoAccreditoRepository.findTipoAccreditoValidoByCode(idEnte,
		// mdpModToInsert.getTipoAccredito().toString(),datiOperazioneDto.getTs()).get(0);
		SiacDAccreditoTipoFin dAccreditoTipoForCheck = siacDTipoAccreditoRepository
				.findTipoAccreditoValidoPerGruppoByCode(idEnte,
						mdpModToInsert.getModalitaAccreditoSoggetto().getCodice(),
						datiOperazioneDto.getTs()).get(0);

		SiacDAccreditoGruppoFin dAccreditoGruppoForCheck = siacDAccreditoGruppoRepository
				.findGruppoByTipoId(idEnte, dAccreditoTipoForCheck.getAccreditoTipoId());

		// Controllo che sia di tipo bancario
		if (dAccreditoGruppoForCheck.getAccreditoGruppoCode().equals(
				Constanti.D_ACCREDITO_TIPO_CODE_Circuito_bancario)
				|| dAccreditoGruppoForCheck.getAccreditoGruppoCode().equals(
						Constanti.D_ACCREDITO_TIPO_CODE_Circuito_Banca_d_Italia)
				|| dAccreditoGruppoForCheck.getAccreditoGruppoCode().equals(
						Constanti.D_ACCREDITO_TIPO_CODE_Circuito_Postale))
		{
			siacTModPagModToInsert = DatiOperazioneUtils.impostaDatiOperazioneLogin(
					siacTModPagModToInsert, datiOperazioneDto, siacTAccountRepository);

			// if (mdpModToInsert.getBic() != null)
			siacTModPagModToInsert.setBic(org.apache.commons.lang3.StringUtils.trim(mdpModToInsert.getBic()));

			// if (mdpModToInsert.getDenominazioneBanca() != null)
			siacTModPagModToInsert.setDenominazioneBanca(org.apache.commons.lang3.StringUtils.trim(mdpModToInsert
					.getDenominazioneBanca()));

			// if (mdpModToInsert.getIban() != null)
			siacTModPagModToInsert.setIban(org.apache.commons.lang3.StringUtils.trim(mdpModToInsert.getIban()));

			// if (mdpModToInsert.getContoCorrente() != null)
			siacTModPagModToInsert.setContocorrente(org.apache.commons.lang3.StringUtils.trim(mdpModToInsert
					.getContoCorrente()));

			// if (mdpModToInsert.getIntestazioneConto() != null)
			siacTModPagModToInsert.setContocorrenteIntestazione(org.apache.commons.lang3.StringUtils
					.trim(mdpModToInsert.getIntestazioneConto()));

			// if (mdpModToInsert.getLuogoNascitaQuietanzante() != null)
			siacTModPagModToInsert.setQuietanzianteNascitaLuogo(org.apache.commons.lang3.StringUtils
					.trim(mdpModToInsert.getLuogoNascitaQuietanzante()));

			siacTModPagModToInsert.setNote(org.apache.commons.lang3.StringUtils.trim(mdpModToInsert.getNote()));
			
			siacTModPagModToInsert.setPerStipendi(mdpModToInsert.getPerStipendi());

			siacTModPagModToInsert.setDataFineValidita(mdpModToInsert.getDataFineValidita());
		
			// Inserire Associazione modalita Pagamento

			// Gestisco il tipo accredito

			SiacDAccreditoTipoFin siacDAccreditoTipo = siacDTipoAccreditoRepository
					.findTipoAccreditoValidoByCode(idEnte,
							mdpModToInsert.getModalitaAccreditoSoggetto().getCodice(),
							datiOperazioneDto.getTs()).get(0);
			siacTModPagModToInsert.setSiacDAccreditoTipo(siacDAccreditoTipo);

			// Inserisco il riferimento alla mdp esistente nella tabella
			// definitiva
			SiacTModpagFin siacTModPagInDefinitiva = siacTModpagRepository.findOne(mdpModToInsert
					.getUid());
			if (siacTModPagInDefinitiva != null)
				siacTModPagModToInsert.setSiacTModpag(siacTModPagInDefinitiva);

			if (mdpModToInsert.getNote() != null)
				siacTModPagModToInsert.setNote(mdpModToInsert.getNote());

			if (mdpModToInsert.getDataFineValidita() != null)
			{
				// se data fine validita valorizzata
				// setto alla mezzanotte:
				Timestamp dataScadenza = TimingUtils.setToMidNigth(mdpModToInsert
						.getDataFineValidita());
				siacTModPagModToInsert.setDataFineValidita(dataScadenza);
			}

			siacTModPagModToInsert.setDataCreazione(new Date());

			// Setto il soggetto ID
			SiacTModpagFin siacTModpagToCheck = siacTModpagRepository.findOne(mdpModToInsert.getUid());
			SiacTSoggettoFin siacTSoggettoDaAssociare = siacTSoggettoRepository
					.findOne(siacTModpagToCheck.getSiacTSoggetto().getUid());
			siacTModPagModToInsert.setSiacTSoggetto(siacTSoggettoDaAssociare);
			// salvo sul db:
			siacTModpagModRepository.saveAndFlush(siacTModPagModToInsert);

			// Abnilito modifica

			siacTModPagInDefinitiva.setDataModifica(new Date());
			siacTModPagInDefinitiva.setDataCreazione(siacTModPagInDefinitiva.getDataCreazione());
			siacTModPagInDefinitiva.setSiacTEnteProprietario(siacTEnteProprietario);
			// salvo sul db:
			siacTModpagRepository.saveAndFlush(siacTModPagInDefinitiva);

			// Setto lo stato in_modifica sulla definitiva
			SiacRModpagStatoFin siacRModpagStatoToUpdate = siacRModpagStatoRepository
					.findStatoValidoByMdpId(mdpModToInsert.getUid()).get(0);
			siacRModpagStatoToUpdate = DatiOperazioneUtils.impostaDatiOperazioneLogin(
					siacRModpagStatoToUpdate, datiOperazioneDto, siacTAccountRepository);

			siacRModpagStatoToUpdate.setSiacTModpag(siacTModpagToCheck);

			siacRModpagStatoToUpdate.setDataModifica(new Date());
			siacRModpagStatoToUpdate.setDataCreazione(siacTModpagToCheck.getDataCreazione());

			SiacDModpagStatoFin siacDModpagStato = siacDModpagStatoRepository
					.findModPagStatoDValidoByCode(idEnte, Constanti.STATO_VALIDO,
							datiOperazioneDto.getTs()).get(0);
			siacRModpagStatoToUpdate.setSiacDModpagStato(siacDModpagStato);
			// salvo sul db:
			siacRModpagStatoRepository.saveAndFlush(siacRModpagStatoToUpdate);

		}

		// Controllo che sia di tipo contante
		if (dAccreditoGruppoForCheck.getAccreditoGruppoCode().equals(
				Constanti.D_ACCREDITO_TIPO_CODE_Contanti))
		{

			siacTModPagModToInsert = DatiOperazioneUtils.impostaDatiOperazioneLogin(
					siacTModPagModToInsert, datiOperazioneDto, siacTAccountRepository);

			// Inserisco il riferimento alla mdp esistente nella tabella
			// definitiva
			SiacTModpagFin siacTModPagInDefinitiva = siacTModpagRepository.findOne(mdpModToInsert
					.getUid());
			if (siacTModPagInDefinitiva != null)
			{
				siacTModPagModToInsert.setSiacTModpag(siacTModPagInDefinitiva);
			}

			if (mdpModToInsert.getCodiceFiscaleQuietanzante() != null)
			{
				siacTModPagModToInsert.setQuietanzianteCodiceFiscale(mdpModToInsert
						.getCodiceFiscaleQuietanzante().trim());
			}
			if (mdpModToInsert.getSoggettoQuietanzante() != null)
			{
				siacTModPagModToInsert.setQuietanziante(mdpModToInsert.getSoggettoQuietanzante());
			}

			// nuovi campi
			// luogo e stato di nascita
			if (mdpModToInsert.getComuneNascita() != null)
			{

				siacTModPagModToInsert.setQuietanzianteNascitaLuogo(mdpModToInsert
						.getComuneNascita().getDescrizione());
				siacTModPagModToInsert.setQuietanzianteNascitaStato(mdpModToInsert
						.getComuneNascita().getNazioneCode());
			}

			// data di nascita
			if (mdpModToInsert.getDataNascitaQuietanzante() != null)
			{
				siacTModPagModToInsert.setQuietanzanteNascitaData(TimingUtils
						.buildTimestamp(mdpModToInsert.getDataNascitaQuietanzante()));
			}

			// fine - nuovi campi

			siacTModPagModToInsert.setPerStipendi(mdpModToInsert.getPerStipendi());

			if (mdpModToInsert.getNote() != null)
			{
				siacTModPagModToInsert.setNote(mdpModToInsert.getNote());
			}
			if (mdpModToInsert.getDataFineValidita() != null)
			{
				// se data fine validita valorizzata
				// Setto alla mezzanotte:
				Timestamp dataScadenza = TimingUtils.setToMidNigth(mdpModToInsert
						.getDataFineValidita());
				siacTModPagModToInsert.setDataFineValidita(dataScadenza);
			}

			SiacDAccreditoTipoFin siacDAccreditoTipo = siacDTipoAccreditoRepository
					.findTipoAccreditoValidoByCode(idEnte,
							mdpModToInsert.getModalitaAccreditoSoggetto().getCodice(),
							datiOperazioneDto.getTs()).get(0);
			siacTModPagModToInsert.setSiacDAccreditoTipo(siacDAccreditoTipo);

			// se e' valido data inizio e data validita'
			if (mdpModToInsert.getDescrizioneStatoModalitaPagamento()
					.equals(Constanti.STATO_VALIDO))
			{
				siacTModPagModToInsert.setDataCreazione(new Date());
			}

			// Setto il soggetto ID
			SiacTModpagFin siacTModpagToCheck = siacTModpagRepository.findOne(mdpModToInsert.getUid());
			SiacTSoggettoFin siacTSoggettoDaAssociare = siacTSoggettoRepository
					.findOne(siacTModpagToCheck.getSiacTSoggetto().getUid());
			siacTModPagModToInsert.setSiacTSoggetto(siacTSoggettoDaAssociare);

			// salvo sul db:
			siacTModpagModRepository.saveAndFlush(siacTModPagModToInsert);

			// Abilito modifica
			siacTModPagInDefinitiva.setDataModifica(new Date());
			siacTModPagInDefinitiva.setDataCreazione(siacTModPagInDefinitiva.getDataCreazione());
			siacTModPagInDefinitiva.setSiacTEnteProprietario(siacTEnteProprietario);
			// salvo sul db:
			siacTModpagRepository.saveAndFlush(siacTModPagInDefinitiva);

			// Setto lo stato in_modifica sulla definitiva
			SiacRModpagStatoFin siacRModpagStatoToUpdate = siacRModpagStatoRepository
					.findStatoValidoByMdpId(mdpModToInsert.getUid()).get(0);
			siacRModpagStatoToUpdate = DatiOperazioneUtils.impostaDatiOperazioneLogin(
					siacRModpagStatoToUpdate, datiOperazioneDto, siacTAccountRepository);

			siacRModpagStatoToUpdate.setSiacTModpag(siacTModpagToCheck);

			siacRModpagStatoToUpdate.setDataModifica(new Date());
			siacRModpagStatoToUpdate.setDataCreazione(siacTModpagToCheck.getDataCreazione());

			SiacDModpagStatoFin siacDModpagStato = siacDModpagStatoRepository
					.findModPagStatoDValidoByCode(idEnte, Constanti.STATO_VALIDO,
							datiOperazioneDto.getTs()).get(0);
			siacRModpagStatoToUpdate.setSiacDModpagStato(siacDModpagStato);
			// salvo sul db:
			siacRModpagStatoRepository.saveAndFlush(siacRModpagStatoToUpdate);

		}

		
		
		// Controllo che sia di tipo generico
		if (dAccreditoGruppoForCheck.getAccreditoGruppoCode().equals(
				Constanti.D_ACCREDITO_TIPO_CODE_Generico))
		{

			siacTModPagModToInsert = DatiOperazioneUtils.impostaDatiOperazioneLogin(
					siacTModPagModToInsert, datiOperazioneDto, siacTAccountRepository);

			// Inserisco il riferimento alla mdp esistente nella tabella
			// definitiva
			SiacTModpagFin siacTModPagInDefinitiva = siacTModpagRepository.findOne(mdpModToInsert
					.getUid());
			if (siacTModPagInDefinitiva != null)
			{
				siacTModPagModToInsert.setSiacTModpag(siacTModPagInDefinitiva);
			}

			if (mdpModToInsert.getCodiceFiscaleQuietanzante() != null)
			{
				siacTModPagModToInsert.setQuietanzianteCodiceFiscale(mdpModToInsert
						.getCodiceFiscaleQuietanzante().trim());
			}
			if (mdpModToInsert.getSoggettoQuietanzante() != null)
			{
				siacTModPagModToInsert.setQuietanziante(mdpModToInsert.getSoggettoQuietanzante());
			}

			// nuovi campi
			// luogo e stato di nascita
			if (mdpModToInsert.getComuneNascita() != null)
			{

				siacTModPagModToInsert.setQuietanzianteNascitaLuogo(mdpModToInsert
						.getComuneNascita().getDescrizione());
				siacTModPagModToInsert.setQuietanzianteNascitaStato(mdpModToInsert
						.getComuneNascita().getNazioneCode());
			}

			// data di nascita
			if (mdpModToInsert.getDataNascitaQuietanzante() != null)
			{
				siacTModPagModToInsert.setQuietanzanteNascitaData(TimingUtils
						.buildTimestamp(mdpModToInsert.getDataNascitaQuietanzante()));
			}

			// fine - nuovi campi

			siacTModPagModToInsert.setPerStipendi(mdpModToInsert.getPerStipendi());

			if (mdpModToInsert.getNote() != null)
			{
				siacTModPagModToInsert.setNote(mdpModToInsert.getNote());
			}
			if (mdpModToInsert.getDataFineValidita() != null)
			{
				// se data fine validita valorizzata
				// Setto alla mezzanotte:
				Timestamp dataScadenza = TimingUtils.setToMidNigth(mdpModToInsert
						.getDataFineValidita());
				siacTModPagModToInsert.setDataFineValidita(dataScadenza);
			}

			SiacDAccreditoTipoFin siacDAccreditoTipo = siacDTipoAccreditoRepository
					.findTipoAccreditoValidoByCode(idEnte,
							mdpModToInsert.getModalitaAccreditoSoggetto().getCodice(),
							datiOperazioneDto.getTs()).get(0);
			siacTModPagModToInsert.setSiacDAccreditoTipo(siacDAccreditoTipo);

			// se e' valido data inizio e data validita'
			if (mdpModToInsert.getDescrizioneStatoModalitaPagamento()
					.equals(Constanti.STATO_VALIDO))
			{
				siacTModPagModToInsert.setDataCreazione(new Date());
			}

			// Setto il soggetto ID
			SiacTModpagFin siacTModpagToCheck = siacTModpagRepository.findOne(mdpModToInsert.getUid());
			SiacTSoggettoFin siacTSoggettoDaAssociare = siacTSoggettoRepository
					.findOne(siacTModpagToCheck.getSiacTSoggetto().getUid());
			siacTModPagModToInsert.setSiacTSoggetto(siacTSoggettoDaAssociare);

			// salvo sul db:
			siacTModpagModRepository.saveAndFlush(siacTModPagModToInsert);

			// Abilito modifica
			siacTModPagInDefinitiva.setDataModifica(new Date());
			siacTModPagInDefinitiva.setDataCreazione(siacTModPagInDefinitiva.getDataCreazione());
			siacTModPagInDefinitiva.setSiacTEnteProprietario(siacTEnteProprietario);
			// salvo sul db:
			siacTModpagRepository.saveAndFlush(siacTModPagInDefinitiva);

			// Setto lo stato in_modifica sulla definitiva
			SiacRModpagStatoFin siacRModpagStatoToUpdate = siacRModpagStatoRepository
					.findStatoValidoByMdpId(mdpModToInsert.getUid()).get(0);
			siacRModpagStatoToUpdate = DatiOperazioneUtils.impostaDatiOperazioneLogin(
					siacRModpagStatoToUpdate, datiOperazioneDto, siacTAccountRepository);

			siacRModpagStatoToUpdate.setSiacTModpag(siacTModpagToCheck);

			siacRModpagStatoToUpdate.setDataModifica(new Date());
			siacRModpagStatoToUpdate.setDataCreazione(siacTModpagToCheck.getDataCreazione());

			SiacDModpagStatoFin siacDModpagStato = siacDModpagStatoRepository
					.findModPagStatoDValidoByCode(idEnte, Constanti.STATO_VALIDO,
							datiOperazioneDto.getTs()).get(0);
			siacRModpagStatoToUpdate.setSiacDModpagStato(siacDModpagStato);
			// salvo sul db:
			siacRModpagStatoRepository.saveAndFlush(siacRModpagStatoToUpdate);

		}

		
		
		
		
		// Controllo che sia di tipo cessione
		if (dAccreditoGruppoForCheck.getAccreditoGruppoCode().equals(
				Constanti.D_ACCREDITO_TIPO_CODE_Cessione_del_credito)
				|| dAccreditoGruppoForCheck.getAccreditoGruppoCode().equals(
						Constanti.D_ACCREDITO_TIPO_CODE_Cessione_dell_incasso))
		{

			SiacRSoggettoRelazFin soggRelaz = siacRSoggettoRelazRepository.findOne(mdpModToInsert
					.getUid());

			SiacRSoggettoRelazModFin siacRSoggettoRelazMod = new SiacRSoggettoRelazModFin();
			siacRSoggettoRelazMod = DatiOperazioneUtils.impostaDatiOperazioneLogin(
					siacRSoggettoRelazMod, datiOperazioneDto, siacTAccountRepository);

			SiacDRelazTipoFin siacDRelazTipo = siacDRelazTipoRepository
					.findRelazTipoValidoByCode(idEnte,
							mdpModToInsert.getTipoAccredito().toString(), datiOperazioneDto.getTs())
					.get(0);

			siacRSoggettoRelazMod.setSiacDRelazTipo(siacDRelazTipo);
			siacRSoggettoRelazMod.setDataCreazione(new Date());
			siacRSoggettoRelazMod.setDataInizioValidita(new Date());
			siacRSoggettoRelazMod.setSiacTSoggetto1(soggRelaz.getSiacTSoggetto1());
			siacRSoggettoRelazMod.setSiacTSoggetto2(soggRelaz.getSiacTSoggetto2());
			siacRSoggettoRelazMod.setSiacTEnteProprietario(siacTEnteProprietario);
			siacRSoggettoRelazMod.setSiacRSoggettoRelaz(soggRelaz);
			// salvo sul db:
			SiacRSoggettoRelazModFin siacRSoggettoRelazModInsert = siacRSoggettoRelazModRepository
					.saveAndFlush(siacRSoggettoRelazMod);

			soggRelaz.setSiacTEnteProprietario(siacTEnteProprietario);
			soggRelaz.setDataCreazione(soggRelaz.getDataCreazione());
			soggRelaz.setDataModifica(new Date());

			if (mdpModToInsert.getDataFineValidita() != null)
			{
				// se data fine validita valorizzata
				// settiamo alla mezzanotte:
				Timestamp dataScadenza = TimingUtils.setToMidNigth(mdpModToInsert
						.getDataFineValidita());
				soggRelaz.setDataFineValidita(dataScadenza);
			}
			else
			{
				soggRelaz.setDataFineValidita(null);
			}
			// salvo sul db:
			siacRSoggettoRelazRepository.saveAndFlush(soggRelaz);

			SiacRSoggrelModpagModFin siacRSoggrelModpagMod = new SiacRSoggrelModpagModFin();
			siacRSoggrelModpagMod = DatiOperazioneUtils.impostaDatiOperazioneLogin(
					siacRSoggrelModpagMod, datiOperazioneDto, siacTAccountRepository);

			siacRSoggrelModpagMod.setSiacRSoggettoRelazMod(siacRSoggettoRelazModInsert);
			siacRSoggrelModpagMod.setSiacTEnteProprietario(siacTEnteProprietario);
			siacRSoggrelModpagMod.setDataCreazione(new Date());
			siacRSoggrelModpagMod.setDataInizioValidita(new Date());

			if (mdpModToInsert.getDataInizioValidita() != null)
			{
				// se data fine inizio valorizzata
				Timestamp dataScadenza = TimingUtils.convertiDataInTimeStamp(mdpModToInsert
						.getDataFineValidita());
				siacRSoggrelModpagMod.setDataFineValidita(dataScadenza);
			}

			if (mdpModToInsert.getNote() != null)
			{
				siacRSoggrelModpagMod.setNote(mdpModToInsert.getNote());
			}

			if (mdpModToInsert.getDataFineValidita() != null)
			{
				// se data fine validita valorizzata
				// settiamo alla mezzanotte:
				Timestamp dataScadenza = TimingUtils.setToMidNigth(mdpModToInsert
						.getDataFineValidita());
				siacRSoggrelModpagMod.setDataFineValidita(dataScadenza);
			}
			else
			{
				siacRSoggrelModpagMod.setDataFineValidita(null);
			}

			SiacTModpagFin siacTModpag = siacTModpagRepository.findOne(mdpModToInsert
					.getModalitaPagamentoSoggettoCessione2().getUid());
			siacRSoggrelModpagMod.setSiacTModpag(siacTModpag);
			siacRSoggrelModpagMod.setSiacRSoggrelModpag(soggRelaz.getSiacRSoggrelModpags().get(0));
			// salvo sul db:
			siacRSoggrelModpagModRepository.saveAndFlush(siacRSoggrelModpagMod);

			SiacDRelazStatoFin siacDRelazStato = siacDRelazStatoRepository
					.findDRelazStatoValidoByCode(idEnte, Constanti.STATO_VALIDO).get(0);
			SiacRSoggettoRelazStatoFin siacRSoggettoRelazStato = siacRSoggettoRelazStatoRepository
					.findBySoggettoRelazId(soggRelaz.getUid());

			siacRSoggettoRelazStato.setSiacDRelazStato(siacDRelazStato);
			siacRSoggettoRelazStato.setDataModifica(new Date());
			siacRSoggettoRelazStato.setDataCreazione(soggRelaz.getDataCreazione());
			siacRSoggettoRelazStato.setSiacRSoggettoRelaz(soggRelaz);
			// salvo sul db:
			siacRSoggettoRelazStatoRepository.saveAndFlush(siacRSoggettoRelazStato);
		}

	}

	/**
	 * eliminaIndirizzo
	 * 
	 * @param indirizzo
	 * @param datiOperazione
	 */
	private void eliminaIndirizzo(SiacTIndirizzoSoggettoFin indirizzo, DatiOperazioneDto datiOperazione)
	{
		if (indirizzo != null)
		{
			Integer idIndirizzoSogg = indirizzo.getIndirizzoId();
			if (idIndirizzoSogg != null)
			{
				List<SiacRIndirizzoSoggettoTipoFin> siacRIndirizzoSoggettoTipoList = siacRIndirizzoSoggettoTipoRepository
						.findValidoByIdIndirizzoSoggetto(idIndirizzoSogg, datiOperazione.getTs());
				if (siacRIndirizzoSoggettoTipoList != null
						&& siacRIndirizzoSoggettoTipoList.size() > 0)
				{
					SiacRIndirizzoSoggettoTipoFin siacRIndirizzoSoggettoTipo = siacRIndirizzoSoggettoTipoList
							.get(0);
					DatiOperazioneUtils.cancellaRecord(siacRIndirizzoSoggettoTipo,
							siacRIndirizzoSoggettoTipoRepository, datiOperazione,
							siacTAccountRepository);
				}
			}
			DatiOperazioneUtils.cancellaRecord(indirizzo, siacTIndirizzoSoggettoRepository,
					datiOperazione, siacTAccountRepository);
		}
	}

	/**
	 * eliminaModalitaPagamento
	 * 
	 * @param mdpToDelete
	 * @param datiOperazione
	 */
	private void eliminaModalitaPagamento(SiacTModpagFin mdpToDelete, DatiOperazioneDto datiOperazione)
	{
		Integer idModpag = mdpToDelete.getUid();

		SiacTModpagFin siacTModpagToDelete = siacTModpagRepository.findOne(idModpag);
		SiacRModpagStatoFin siacRModpagStato = siacRModpagStatoRepository.findStatoValidoByMdpId(
				idModpag).get(0);

		siacRModpagStato.setDataCancellazione(new Date());
		// salvo sul db:
		siacRModpagStatoRepository.saveAndFlush(siacRModpagStato);

		SiacTModpagModFin siacTModpagMod = siacTModpagModRepository.findValidoByModpagId(idModpag,
				datiOperazione.getTs());
		if (siacTModpagMod != null)
		{
			siacTModpagMod.setDataCancellazione(new Date());
			// salvo sul db:
			siacTModpagModRepository.saveAndFlush(siacTModpagMod);
		}
		siacTModpagToDelete.setDataCancellazione(new Date());
		// salvo sul db:
		siacTModpagRepository.saveAndFlush(siacTModpagToDelete);
	}

	/**
	 * eliminaModalitaPagamentoCessione
	 * 
	 * @param mdpToDelete
	 * @param datiOperazione
	 */
	private void eliminaModalitaPagamentoCessione(ModalitaPagamentoSoggetto mdpToDelete,
			DatiOperazioneDto datiOperazione)
	{
		Integer idMdp = mdpToDelete.getUid();

		SiacRSoggettoRelazFin siacRSoggettoRelazToDelete = siacRSoggettoRelazRepository.findOne(idMdp);
		SiacRSoggrelModpagFin siacRSoggrelModpagToDelete = siacRSoggrelModpagRepository
				.findValidiBySoggettoRelaz(siacRSoggettoRelazToDelete.getUid());
		SiacRSoggettoRelazStatoFin siacRSoggetoRelazStatoToDelete = siacRSoggettoRelazStatoRepository
				.findBySoggettoRelazId(siacRSoggettoRelazToDelete.getUid());

		siacRSoggetoRelazStatoToDelete.setDataCancellazione(new Date());
		// salvo sul db:
		siacRSoggettoRelazStatoRepository.saveAndFlush(siacRSoggetoRelazStatoToDelete);

		SiacRSoggettoRelazModFin siacRSoggettoRelazMod = siacRSoggettoRelazModRepository
				.findValidaBySoggettoRelazId(siacRSoggettoRelazToDelete.getUid());
		if (siacRSoggettoRelazMod != null)
		{
			SiacRSoggrelModpagModFin siacRSoggrelModpagMod = siacRSoggrelModpagModRepository
					.findValidoBySoggRelazModId(siacRSoggettoRelazMod.getUid());
			// inizializzo la data cancellazione:
			siacRSoggrelModpagMod.setDataCancellazione(new Date());
			// salvo sul db:
			siacRSoggettoRelazModRepository.saveAndFlush(siacRSoggettoRelazMod);
			// inizializzo la data cancellazione:
			siacRSoggrelModpagMod.setDataCancellazione(new Date());
			// salvo sul db:
			siacRSoggrelModpagModRepository.saveAndFlush(siacRSoggrelModpagMod);
		}
		// inizializzo la data cancellazione:
		siacRSoggrelModpagToDelete.setDataCancellazione(new Date());
		// salvo sul db:
		siacRSoggrelModpagRepository.saveAndFlush(siacRSoggrelModpagToDelete);
		// inizializzo la data cancellazione:
		siacRSoggettoRelazToDelete.setDataCancellazione(new Date());
		// salvo sul db:
		siacRSoggettoRelazRepository.saveAndFlush(siacRSoggettoRelazToDelete);
	}

	/**
	 * updateIndirizzo
	 * 
	 * @param indirizzo
	 * @param siacTSoggetto
	 * @param idEnte
	 * @param datiOperazione
	 */
	private void updateIndirizzo(IndirizzoSoggetto indirizzo, SiacTSoggettoFin siacTSoggetto,
			Integer idEnte, DatiOperazioneDto datiOperazione)
	{

		DatiOperazioneDto datiOperazioneInserisci = datiOperazione;
		datiOperazioneInserisci.setOperazione(Operazione.INSERIMENTO);
		DatiOperazioneDto datiOperazioneModifica = datiOperazione;
		datiOperazioneModifica.setOperazione(Operazione.MODIFICA);

		String dug = indirizzo.getSedime();
		if (!StringUtils.isEmpty(dug))
		{
			dug = dug.trim().toUpperCase();
		}
		String tipoIndirizzoCodeNew = indirizzo.getIdTipoIndirizzo();

		Integer idIndirizzoInModifica = indirizzo.getIndirizzoId();

		SiacTIndirizzoSoggettoFin siacTIndirizzoSoggetto = siacTIndirizzoSoggettoRepository.findOne(idIndirizzoInModifica);

		Integer idIndirizzoSogg = siacTIndirizzoSoggetto.getIndirizzoId();

		// gestione comune:
		
		//GESTIONE COMUNE:
		SiacTComuneFin comuneTrovato = gestisciComuneIndirizzoSoggetto(indirizzo, datiOperazione);
		if(comuneTrovato==null){
			//in caso di errore imprevisto in gestisciComuneIndirizzoSoggetto 
			//evitiamo di non salvare il comune:
			//non dovrebbe comunque mai succedere.
			comuneTrovato = siacTIndirizzoSoggetto.getSiacTComune();
		}
		//

		// gestione d via tipo:
		SiacDViaTipoFin siacDViaTipo = null;
		if (isSedimeModificato(siacTIndirizzoSoggetto, indirizzo)){
			// e' cambiato il sedime
			siacDViaTipo = gestioneDviaTipo(idEnte, dug, datiOperazioneInserisci);
		} else{
			// rimasta uguale
			siacDViaTipo = siacTIndirizzoSoggetto.getSiacDViaTipo();
		}

		// gestione siac t indirizzo soggetto:
		siacTIndirizzoSoggetto = saveSiacTIndirizzoSoggetto(siacTIndirizzoSoggetto, comuneTrovato,
				siacDViaTipo, siacTSoggetto, indirizzo, datiOperazioneModifica);

		// gestione tipo indirizzo:
		List<SiacRIndirizzoSoggettoTipoFin> listaappoggio = siacRIndirizzoSoggettoTipoRepository
				.findValidoByIdIndirizzoSoggetto(idIndirizzoSogg, datiOperazioneModifica.getTs());
		if (listaappoggio != null && listaappoggio.size() > 0)
		{
			SiacRIndirizzoSoggettoTipoFin siacRIndirizzoSoggettoTipoOld = listaappoggio.get(0);
			SiacDIndirizzoTipoFin siacDIndirizzoTipoOld = siacRIndirizzoSoggettoTipoOld
					.getSiacDIndirizzoTipo();
			String codeTipoIndirizzoOld = siacDIndirizzoTipoOld.getIndirizzoTipoCode();
			if (!codeTipoIndirizzoOld.equals(tipoIndirizzoCodeNew))
			{
				// sono diversi
				// 1. invalido la vecchia relazione:
				siacRIndirizzoSoggettoTipoOld = DatiOperazioneUtils.cancellaRecord(
						siacRIndirizzoSoggettoTipoOld, siacRIndirizzoSoggettoTipoRepository,
						datiOperazioneModifica, siacTAccountRepository);
				SiacDIndirizzoTipoFin siacDIndirizzoTipo2 = siacDIndirizzoTipoRepository
						.findTipoIndirizzoValidoByCode(idEnte, tipoIndirizzoCodeNew,
								datiOperazione.getTs()).get(0);
				// 2. inserisco una nuova relazione siac r indirizzo soggetto
				// tipo:
				SiacRIndirizzoSoggettoTipoFin siacRIndirizzoSoggettoTipo = new SiacRIndirizzoSoggettoTipoFin();
				siacRIndirizzoSoggettoTipo = DatiOperazioneUtils
						.impostaDatiOperazioneLogin(siacRIndirizzoSoggettoTipo,
								datiOperazioneInserisci, siacTAccountRepository);
				siacRIndirizzoSoggettoTipo.setSiacTIndirizzoSoggetto(siacTIndirizzoSoggetto);
				siacRIndirizzoSoggettoTipo.setSiacDIndirizzoTipo(siacDIndirizzoTipo2);
				// salvo sul db:
				siacRIndirizzoSoggettoTipoRepository.saveAndFlush(siacRIndirizzoSoggettoTipo);
			}
		}

	}

	/**
	 * eliminaIndirizzoMod
	 * 
	 * @param indirizzo
	 * @param datiOperazione
	 */
	private void eliminaIndirizzoMod(SiacTIndirizzoSoggettoModFin indirizzo,
			DatiOperazioneDto datiOperazione)
	{
		Integer idIndirizzoSogg = indirizzo.getIndirizzoModId();
		SiacRIndirizzoSoggettoTipoModFin siacRIndirizzoSoggettoTipoMod = siacRIndirizzoSoggettoTipoModRepository
				.findValidoByIdIndirizzoSoggettoMod(idIndirizzoSogg, datiOperazione.getTs()).get(0);
		DatiOperazioneUtils.cancellaRecord(siacRIndirizzoSoggettoTipoMod,
				siacRIndirizzoSoggettoTipoModRepository, datiOperazione, siacTAccountRepository);
		DatiOperazioneUtils.cancellaRecord(indirizzo, siacTIndirizzoSoggettoModRepository,
				datiOperazione, siacTAccountRepository);
	}

	/**
	 * gestioneDviaTipo
	 * 
	 * @param idEnte
	 * @param dug
	 * @param datiOperazioneInserisci
	 * @return
	 */
	private SiacDViaTipoFin gestioneDviaTipo(Integer idEnte, String dug,
			DatiOperazioneDto datiOperazioneInserisci)
	{
		SiacDViaTipoFin siacDViaTipo = null;
		// cerco sul db:
		List<SiacDViaTipoFin> listaDviatipo = siacDViaTipoRepository.findByTipo(idEnte, dug);
		if (listaDviatipo != null && listaDviatipo.size() > 0)
		{
			siacDViaTipo = listaDviatipo.get(0);
		}
		else
		{
			SiacDViaTipoFin siacDViaTipoNew = new SiacDViaTipoFin();
			siacDViaTipoNew = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacDViaTipoNew,
					datiOperazioneInserisci, siacTAccountRepository);
			siacDViaTipoNew.setViaTipoDesc(dug);
			siacDViaTipoNew.setViaTipoCode(dug);
			// salvo sul db:
			siacDViaTipo = siacDViaTipoRepository.saveAndFlush(siacDViaTipoNew);
		}
		// Termino restituendo l'oggetto di ritorno:
		return siacDViaTipo;
	}

	/**
	 * saveSiacTIndirizzoSoggetto
	 * 
	 * @param siacTIndirizzoSoggetto
	 * @param comuneTrovato
	 * @param siacDViaTipo
	 * @param siacTSoggetto
	 * @param indirizzo
	 * @param datiOperazioneDto
	 * @return
	 */
	private SiacTIndirizzoSoggettoFin saveSiacTIndirizzoSoggetto(
			SiacTIndirizzoSoggettoFin siacTIndirizzoSoggetto, SiacTComuneFin comuneTrovato,
			SiacDViaTipoFin siacDViaTipo, SiacTSoggettoFin siacTSoggetto, IndirizzoSoggetto indirizzo,
			DatiOperazioneDto datiOperazioneDto)
	{

		String cap = indirizzo.getCap();
		String toponimo = indirizzo.getDenominazione();
		String civico = indirizzo.getNumeroCivico();
		boolean avviso = Boolean.valueOf(indirizzo.getAvviso());
		boolean principale = Boolean.valueOf(indirizzo.getPrincipale());
		if (!principale)
			principale = Constanti.TRUE.equals(indirizzo.getPrincipale());

		siacTIndirizzoSoggetto.setToponimo(toponimo);
		siacTIndirizzoSoggetto.setNumeroCivico(civico);
		siacTIndirizzoSoggetto.setZipCode(cap);

		if (null == comuneTrovato.getUid() || comuneTrovato.getUid() == 0)
		{
			siacTIndirizzoSoggetto.setSiacTComune(null);
		}
		else
			siacTIndirizzoSoggetto.setSiacTComune(comuneTrovato);

		siacTIndirizzoSoggetto.setAvviso(StringUtils.booleanToStringForDb(avviso));
		siacTIndirizzoSoggetto.setPrincipale(StringUtils.booleanToStringForDb(principale));
		siacTIndirizzoSoggetto.setSiacDViaTipo(siacDViaTipo);
		siacTIndirizzoSoggetto.setSiacTSoggetto(siacTSoggetto);

		return saveSiacTIndirizzoSoggetto(siacTIndirizzoSoggetto, datiOperazioneDto);
	}

	/**
	 * saveSiacTIndirizzoSoggetto
	 * 
	 * @param siacTIndirizzoSoggetto
	 * @param datiOperazioneDto
	 * @return
	 */
	private SiacTIndirizzoSoggettoFin saveSiacTIndirizzoSoggetto(
			SiacTIndirizzoSoggettoFin siacTIndirizzoSoggetto, DatiOperazioneDto datiOperazioneDto)
	{
		siacTIndirizzoSoggetto = DatiOperazioneUtils.impostaDatiOperazioneLogin(
				siacTIndirizzoSoggetto, datiOperazioneDto, siacTAccountRepository);
		// salvo sul db:
		SiacTIndirizzoSoggettoFin siacTIndiriInserito = siacTIndirizzoSoggettoRepository
				.saveAndFlush(siacTIndirizzoSoggetto);
		// Termino restituendo l'oggetto di ritorno:
		return siacTIndiriInserito;
	}

	/**
	 * saveSiacTIndirizzoSoggettoMod
	 * 
	 * @param siacTIndirizzoSoggettoMod
	 * @param comuneTrovato
	 * @param siacDViaTipo
	 * @param siacTSoggetto
	 * @param siacTSoggettoMod
	 * @param indirizzo
	 * @param datiOperazioneDto
	 * @return
	 */
	private SiacTIndirizzoSoggettoModFin saveSiacTIndirizzoSoggettoMod(
			SiacTIndirizzoSoggettoModFin siacTIndirizzoSoggettoMod, SiacTComuneFin comuneTrovato,
			SiacDViaTipoFin siacDViaTipo, SiacTSoggettoFin siacTSoggetto,
			SiacTSoggettoModFin siacTSoggettoMod, IndirizzoSoggetto indirizzo,
			DatiOperazioneDto datiOperazioneDto)
	{

		String cap = indirizzo.getCap();
		String toponimo = indirizzo.getDenominazione();
		String civico = indirizzo.getNumeroCivico();
		boolean avviso = Boolean.valueOf(indirizzo.getAvviso());
		boolean principale = Boolean.valueOf(indirizzo.getPrincipale());

		siacTIndirizzoSoggettoMod = DatiOperazioneUtils.impostaDatiOperazioneLogin(
				siacTIndirizzoSoggettoMod, datiOperazioneDto, siacTAccountRepository);
		siacTIndirizzoSoggettoMod.setToponimo(toponimo);
		siacTIndirizzoSoggettoMod.setNumeroCivico(civico);
		siacTIndirizzoSoggettoMod.setZipCode(cap);
		siacTIndirizzoSoggettoMod.setSiacTComune(comuneTrovato);
		siacTIndirizzoSoggettoMod.setAvviso(StringUtils.booleanToStringForDb(avviso));
		siacTIndirizzoSoggettoMod.setPrincipale(StringUtils.booleanToStringForDb(principale));
		siacTIndirizzoSoggettoMod.setSiacDViaTipo(siacDViaTipo);
		siacTIndirizzoSoggettoMod.setSiacTSoggetto(siacTSoggetto);
		siacTIndirizzoSoggettoMod.setSiacTSoggettoMod(siacTSoggettoMod);
		// salvo sul db:
		siacTIndirizzoSoggettoMod = siacTIndirizzoSoggettoModRepository
				.saveAndFlush(siacTIndirizzoSoggettoMod);
		// Termino restituendo l'oggetto di ritorno:
		return siacTIndirizzoSoggettoMod;
	}

	/**
	 * saveIndirizzoFromIndirizzoMod
	 * 
	 * @param siacTIndirizzoSoggettoMod
	 * @param siacTSoggetto
	 * @param idEnte
	 * @param datiOperazioneDto
	 * @return
	 */
	private SiacTIndirizzoSoggettoFin saveIndirizzoFromIndirizzoMod(
			SiacTIndirizzoSoggettoModFin siacTIndirizzoSoggettoMod, SiacTSoggettoFin siacTSoggetto,
			Integer idEnte, DatiOperazioneDto datiOperazioneDto)
	{
		// gestione siac_t_indirizzo_soggetto:
		SiacTIndirizzoSoggettoFin siacTIndirizzoSoggetto = EntityToEntityConverter
				.siacTIndirizzoSoggettoModToSiacTIndirizzoSoggetto(siacTIndirizzoSoggettoMod);
		DatiOperazioneDto datiOperazioneInserisci = DatiOperazioneDto.buildDatiOperazione(
				datiOperazioneDto, Operazione.INSERIMENTO);
		SiacTIndirizzoSoggettoFin siacTIndiriInserito = saveSiacTIndirizzoSoggetto(
				siacTIndirizzoSoggetto, datiOperazioneInserisci);

		// gestione siac_r_indirizzo_soggetto_tipo:
		SiacRIndirizzoSoggettoTipoModFin siacRIndirizzoSoggettoTipoMod = siacRIndirizzoSoggettoTipoModRepository
				.findValidoByIdIndirizzoSoggettoMod(siacTIndirizzoSoggettoMod.getIndirizzoModId(),
						datiOperazioneDto.getTs()).get(0);
		SiacDIndirizzoTipoFin siacDIndirizzoTipo = siacRIndirizzoSoggettoTipoMod
				.getSiacDIndirizzoTipo();

		SiacRIndirizzoSoggettoTipoFin siacRIndirizzoSoggettoTipo = new SiacRIndirizzoSoggettoTipoFin();
		siacRIndirizzoSoggettoTipo = DatiOperazioneUtils.impostaDatiOperazioneLogin(
				siacRIndirizzoSoggettoTipo, datiOperazioneDto, siacTAccountRepository);
		siacRIndirizzoSoggettoTipo.setSiacTIndirizzoSoggetto(siacTIndiriInserito);
		siacRIndirizzoSoggettoTipo.setSiacDIndirizzoTipo(siacDIndirizzoTipo);
		// salvo sul db:
		siacRIndirizzoSoggettoTipoRepository.saveAndFlush(siacRIndirizzoSoggettoTipo);

		List<SiacRIndirizzoSoggettoTipoFin> siacRIndirizzoSoggettoTipos = new ArrayList<SiacRIndirizzoSoggettoTipoFin>();
		siacRIndirizzoSoggettoTipos.add(siacRIndirizzoSoggettoTipo);
		siacTIndiriInserito.setSiacRIndirizzoSoggettoTipos(siacRIndirizzoSoggettoTipos);

		// Termino restituendo l'oggetto di ritorno:
		return siacTIndiriInserito;
	}

	/**
	 * saveIndirizzo
	 * 
	 * @param indirizzo
	 * @param siacTSoggetto
	 * @param idEnte
	 * @param datiOperazioneDto
	 * @return
	 */
	private SiacTIndirizzoSoggettoFin saveIndirizzo(IndirizzoSoggetto indirizzo,
			SiacTSoggettoFin siacTSoggetto, Integer idEnte, DatiOperazioneDto datiOperazioneDto)
	{
		String dug = indirizzo.getSedime();
		if (!StringUtils.isEmpty(dug))
		{
			dug = dug.trim().toUpperCase();
		}
		String tipoIndirizzoCode = indirizzo.getIdTipoIndirizzo();
		// gestione tipo indirizzo:
		SiacDIndirizzoTipoFin siacDIndirizzoTipo = siacDIndirizzoTipoRepository
				.findTipoIndirizzoValidoByCode(idEnte, tipoIndirizzoCode, datiOperazioneDto.getTs())
				.get(0);

		//GESTIONE COMUNE:
		SiacTComuneFin comuneTrovato = gestisciComuneIndirizzoSoggetto(indirizzo, datiOperazioneDto);
		//
		
		// gestione d via tipo:
		SiacDViaTipoFin siacDViaTipo = gestioneDviaTipo(idEnte, dug, datiOperazioneDto);

		// gestione siac t indirizzo soggetto:
		SiacTIndirizzoSoggettoFin siacTIndirizzoSoggetto = new SiacTIndirizzoSoggettoFin();
		SiacTIndirizzoSoggettoFin siacTIndiriInserito = saveSiacTIndirizzoSoggetto(
				siacTIndirizzoSoggetto, comuneTrovato, siacDViaTipo, siacTSoggetto, indirizzo,
				datiOperazioneDto);

		// gestione siac r indirizzo soggetto tipo:
		SiacRIndirizzoSoggettoTipoFin siacRIndirizzoSoggettoTipo = new SiacRIndirizzoSoggettoTipoFin();
		siacRIndirizzoSoggettoTipo = DatiOperazioneUtils.impostaDatiOperazioneLogin(
				siacRIndirizzoSoggettoTipo, datiOperazioneDto, siacTAccountRepository);
		siacRIndirizzoSoggettoTipo.setSiacTIndirizzoSoggetto(siacTIndiriInserito);
		siacRIndirizzoSoggettoTipo.setSiacDIndirizzoTipo(siacDIndirizzoTipo);
		// salvo sul db:
		siacRIndirizzoSoggettoTipoRepository.saveAndFlush(siacRIndirizzoSoggettoTipo);

		// Termino restituendo l'oggetto di ritorno:
		return siacTIndiriInserito;
	}

	/**
	 * saveIndirizzoMod
	 * 
	 * @param indirizzo
	 * @param siacTSoggetto
	 * @param siacTSoggettoMod
	 * @param idEnte
	 * @param datiOperazioneDto
	 * @return
	 */
	private SiacTIndirizzoSoggettoModFin saveIndirizzoMod(IndirizzoSoggetto indirizzo,
			SiacTSoggettoFin siacTSoggetto, SiacTSoggettoModFin siacTSoggettoMod, Integer idEnte,
			DatiOperazioneDto datiOperazioneDto)
	{
		String dug = indirizzo.getSedime();
		if (!StringUtils.isEmpty(dug))
		{
			dug = dug.trim().toUpperCase();
		}

		String tipoIndirizzoCode = indirizzo.getIdTipoIndirizzo();
		SiacDIndirizzoTipoFin siacDIndirizzoTipo = siacDIndirizzoTipoRepository
				.findTipoIndirizzoValidoByCode(idEnte, tipoIndirizzoCode, datiOperazioneDto.getTs())
				.get(0);

		//GESTIONE COMUNE:
		SiacTComuneFin comuneTrovato = gestisciComuneIndirizzoSoggetto(indirizzo, datiOperazioneDto);
		//

		// gestione d via tipo:
		SiacDViaTipoFin siacDViaTipo = gestioneDviaTipo(idEnte, dug, datiOperazioneDto);

		// gestione siac_t_indirizzo_soggetto:
		SiacTIndirizzoSoggettoModFin siacTIndirizzoSoggettoMod = new SiacTIndirizzoSoggettoModFin();
		SiacTIndirizzoSoggettoModFin siacTIndiriInserito = saveSiacTIndirizzoSoggettoMod(
				siacTIndirizzoSoggettoMod, comuneTrovato, siacDViaTipo, siacTSoggetto,
				siacTSoggettoMod, indirizzo, datiOperazioneDto);

		// gestione siac_r_indirizzo_soggetto_tipo_mod:
		SiacRIndirizzoSoggettoTipoModFin siacRIndirizzoSoggettoTipoMod = new SiacRIndirizzoSoggettoTipoModFin();
		siacRIndirizzoSoggettoTipoMod = DatiOperazioneUtils.impostaDatiOperazioneLogin(
				siacRIndirizzoSoggettoTipoMod, datiOperazioneDto, siacTAccountRepository);
		siacRIndirizzoSoggettoTipoMod.setSiacTIndirizzoSoggettoMod(siacTIndiriInserito);
		siacRIndirizzoSoggettoTipoMod.setSiacDIndirizzoTipo(siacDIndirizzoTipo);
		// salvo sul db:
		
		//fix per jira  SIAC-2255 validita_inizio prevede anche la validita inzio
		siacRIndirizzoSoggettoTipoMod.setDataInizioValidita(getNow());
		//
		siacRIndirizzoSoggettoTipoModRepository.saveAndFlush(siacRIndirizzoSoggettoTipoMod);

		List<SiacRIndirizzoSoggettoTipoModFin> siacRIndirizzoSoggettoTipoMods = new ArrayList<SiacRIndirizzoSoggettoTipoModFin>();
		siacRIndirizzoSoggettoTipoMods.add(siacRIndirizzoSoggettoTipoMod);
		siacTIndiriInserito.setSiacRIndirizzoSoggettoTipoMods(siacRIndirizzoSoggettoTipoMods);

		// Termino restituendo l'oggetto di ritorno:
		return siacTIndiriInserito;
	}

	/**
	 * valutaContattiDaModificare
	 * 
	 * @param listaContatti
	 * @param idSoggetto
	 * @return
	 */
	private ContattiInModificaInfoDto valutaContattiDaModificare(List<Contatto> listaContatti,
			Integer idSoggetto)
	{
		ContattiInModificaInfoDto contattiInModificaInfoDto = new ContattiInModificaInfoDto();
		long currMillisec = System.currentTimeMillis();
		Date dateInserimento = new Date(currMillisec);
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);
		List<SiacTRecapitoSoggettoFin> recapitiOld = siacTRecapitoSoggettoRepository
				.findValidiByIdSoggetto(idSoggetto, timestampInserimento);

		ArrayList<Contatto> listaContattiNuovi = new ArrayList<Contatto>();
		ArrayList<Contatto> listaRimastiUguali = new ArrayList<Contatto>();
		ArrayList<ContattoModificatoDto> listaModificati = new ArrayList<ContattoModificatoDto>();
		ArrayList<SiacTRecapitoSoggettoFin> listaDaInvalidare = new ArrayList<SiacTRecapitoSoggettoFin>();

		ArrayList<String> codiciNuovi = new ArrayList<String>();
		if (listaContatti != null)
		{
			for (Contatto contattoIterato : listaContatti)
			{
				String codIterato = contattoIterato.getContattoCodModo();
				codiciNuovi.add(codIterato);
				boolean eraGiaPresente = false;
				for (SiacTRecapitoSoggettoFin siacTOld : recapitiOld)
				{
					String codOld = siacTOld.getSiacDRecapitoModo().getRecapitoModoCode();
					if (StringUtils.sonoUgualiTrimmed(codOld, codIterato))
					{
						String descrizioneIterata = contattoIterato.getDescrizione().trim()
								.toUpperCase();
						String descrizioneOld = siacTOld.getRecapitoDesc().trim().toUpperCase();
						String avvisoIterato = StringUtils.checkStringBooleanForDb(contattoIterato
								.getAvviso());
						String avvisoOld = siacTOld.getAvviso().trim().toUpperCase();
						if (StringUtils.sonoUgualiTrimmed(descrizioneIterata, descrizioneOld)
								&& StringUtils.sonoUgualiTrimmed(avvisoIterato, avvisoOld))
						{
							// rimasto uguale
							listaRimastiUguali.add(contattoIterato);
						}
						else
						{
							// modificato
							listaModificati
									.add(new ContattoModificatoDto(siacTOld, contattoIterato));
							if (StringUtils.isEmpty(descrizioneIterata))
							{
								listaDaInvalidare.add(siacTOld);
							}
						}
						eraGiaPresente = true;
					}
				}
				if (!eraGiaPresente)
				{
					listaContattiNuovi.add(contattoIterato);
				}
			}
		}
		for (SiacTRecapitoSoggettoFin siacTOld : recapitiOld)
		{
			String codOld = siacTOld.getSiacDRecapitoModo().getRecapitoModoCode();
			if (!StringUtils.contenutoIn(codOld, codiciNuovi))
			{
				if (!listaDaInvalidare.contains(siacTOld))
				{
					listaDaInvalidare.add(siacTOld);
				}
			}
		}
		contattiInModificaInfoDto.setListaContattiNuovi(listaContattiNuovi);
		contattiInModificaInfoDto.setListaDaInvalidare(listaDaInvalidare);
		contattiInModificaInfoDto.setListaModificati(listaModificati);
		contattiInModificaInfoDto.setListaRimastiUguali(listaRimastiUguali);

		if ((listaContattiNuovi == null || listaContattiNuovi.size() == 0)
				&& (listaDaInvalidare == null || listaDaInvalidare.size() == 0)
				&& (listaModificati == null || listaModificati.size() == 0))
		{
			contattiInModificaInfoDto.setRimastiUguali(true);
		}
		else
		{
			contattiInModificaInfoDto.setRimastiUguali(false);
		}
		// Termino restituendo l'oggetto di ritorno:
		return contattiInModificaInfoDto;
	}

	/**
	 * valutaContattiDaModificareMod
	 * 
	 * @param listaContatti
	 * @param idSoggetto
	 * @return
	 */
	private ContattiModInModificaInfoDto valutaContattiDaModificareMod(
			List<Contatto> listaContatti, Integer idSoggetto)
	{
		ContattiModInModificaInfoDto contattiModInModificaInfoDto = new ContattiModInModificaInfoDto();
		long currMillisec = System.currentTimeMillis();
		Date dateInserimento = new Date(currMillisec);
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);
		List<SiacTRecapitoSoggettoModFin> recapitiOld = siacTRecapitoSoggettoModRepository
				.findValidoBySoggModId(idSoggetto, timestampInserimento);

		ArrayList<Contatto> listaContattiNuovi = new ArrayList<Contatto>();
		ArrayList<Contatto> listaRimastiUguali = new ArrayList<Contatto>();
		ArrayList<ContattoModModificatoDto> listaModificati = new ArrayList<ContattoModModificatoDto>();
		ArrayList<SiacTRecapitoSoggettoModFin> listaDaInvalidare = new ArrayList<SiacTRecapitoSoggettoModFin>();

		ArrayList<String> codiciNuovi = new ArrayList<String>();
		if (listaContatti != null && listaContatti.size() > 0)
		{
			for (Contatto contattoIterato : listaContatti)
			{
				String codIterato = contattoIterato.getContattoCodModo();
				codiciNuovi.add(codIterato);
				boolean eraGiaPresente = false;
				for (SiacTRecapitoSoggettoModFin siacTOld : recapitiOld)
				{
					String codOld = siacTOld.getSiacDRecapitoModo().getRecapitoModoCode();
					if (codOld.equals(codIterato))
					{
						String descrizioneIterata = contattoIterato.getDescrizione().trim()
								.toUpperCase();
						String descrizioneOld = siacTOld.getRecapitoDesc().trim().toUpperCase();
						String avvisoIterato = StringUtils.checkStringBooleanForDb(contattoIterato
								.getAvviso());
						String avvisoOld = siacTOld.getAvviso().trim().toUpperCase();
						if (descrizioneIterata.equals(descrizioneOld)
								&& avvisoIterato.equalsIgnoreCase(avvisoOld))
						{
							// rimasto uguale
							listaRimastiUguali.add(contattoIterato);
						}
						else
						{
							// modificato
							listaModificati.add(new ContattoModModificatoDto(siacTOld,
									contattoIterato));
							listaDaInvalidare.add(siacTOld);
						}
						eraGiaPresente = true;
					}
				}
				if (!eraGiaPresente)
				{
					listaContattiNuovi.add(contattoIterato);
				}
			}
		}
		for (SiacTRecapitoSoggettoModFin siacTOld : recapitiOld)
		{
			String codOld = siacTOld.getSiacDRecapitoModo().getRecapitoModoCode();
			if (!codiciNuovi.contains(codOld))
			{
				if (!listaDaInvalidare.contains(siacTOld))
				{
					listaDaInvalidare.add(siacTOld);
				}
			}
		}
		contattiModInModificaInfoDto.setListaContattiNuovi(listaContattiNuovi);
		contattiModInModificaInfoDto.setListaDaInvalidare(listaDaInvalidare);
		contattiModInModificaInfoDto.setListaModificati(listaModificati);
		contattiModInModificaInfoDto.setListaRimastiUguali(listaRimastiUguali);

		if ((listaContattiNuovi == null || listaContattiNuovi.size() == 0)
				&& (listaDaInvalidare == null || listaDaInvalidare.size() == 0)
				&& (listaModificati == null || listaModificati.size() == 0))
		{
			contattiModInModificaInfoDto.setRimastiUguali(true);
		}
		else
		{
			contattiModInModificaInfoDto.setRimastiUguali(false);
		}
		// Termino restituendo l'oggetto di ritorno:
		return contattiModInModificaInfoDto;
	}

	/**
	 * valutaOneriDaModificare
	 * 
	 * @param oneriCodes
	 * @param idSiacTSoggettoInModifica
	 * @return
	 */
	private OneriInModificaInfoDto valutaOneriDaModificare(String[] oneriCodes,
			Integer idSiacTSoggettoInModifica)
	{
		OneriInModificaInfoDto oneriInModificaInfoDto = new OneriInModificaInfoDto();
		ArrayList<String> listaOneri = new ArrayList<String>();
		long currMillisec = System.currentTimeMillis();
		Date dateInserimento = new Date(currMillisec);
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);

		if (oneriCodes != null && oneriCodes.length > 0)
		{
			listaOneri = new ArrayList<String>(Arrays.asList(oneriCodes));
		}
		ArrayList<SiacRSoggettoOnereFin> daEliminare = new ArrayList<SiacRSoggettoOnereFin>();
		ArrayList<String> nuoviOneri = new ArrayList<String>();
		List<SiacRSoggettoOnereFin> listaValidi = siacRSoggettoOnereRepository.findValidiByIdSoggetto(
				idSiacTSoggettoInModifica, timestampInserimento);
		ArrayList<String> oneriOld = new ArrayList<String>();
		if (listaValidi != null && listaValidi.size() > 0)
		{
			for (SiacRSoggettoOnereFin iteratoOld : listaValidi)
			{
				String codOld = iteratoOld.getSiacDOnere().getOnereCode();
				oneriOld.add(codOld); // mancava questa parte
				if (!listaOneri.contains(codOld))
				{
					daEliminare.add(iteratoOld);
				}
			}
		}
		if (oneriCodes != null && oneriCodes.length > 0)
		{
			for (String onereNew : oneriCodes)
			{
				if (!oneriOld.contains(onereNew))
				{
					nuoviOneri.add(onereNew);
				}
			}
		}
		if ((nuoviOneri == null || nuoviOneri.size() == 0)
				&& (daEliminare == null || daEliminare.size() == 0))
		{
			oneriInModificaInfoDto.setRimastiUguali(true);
		}
		else
		{
			oneriInModificaInfoDto.setRimastiUguali(false);
		}
		oneriInModificaInfoDto.setNuoviOneri(nuoviOneri);
		oneriInModificaInfoDto.setOneriEliminati(daEliminare);
		// Termino restituendo l'oggetto di ritorno:
		return oneriInModificaInfoDto;
	}

	/**
	 * 
	 * @param oneriCodes
	 * @param idSiacTSoggettoInModifica
	 * @return
	 */
	private OneriModInModificaInfoDto valutaOneriDaModificareMod(String[] oneriCodes,
			Integer idSiacTSoggettoInModifica)
	{
		OneriModInModificaInfoDto oneriInModificaInfoDto = new OneriModInModificaInfoDto();
		ArrayList<String> listaOneri = new ArrayList<String>();
		long currMillisec = System.currentTimeMillis();
		Date dateInserimento = new Date(currMillisec);
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);

		if (oneriCodes != null && oneriCodes.length > 0)
		{
			listaOneri = new ArrayList<String>(Arrays.asList(oneriCodes));
		}
		ArrayList<SiacRSoggettoOnereModFin> daEliminare = new ArrayList<SiacRSoggettoOnereModFin>();
		ArrayList<String> nuoviOneri = new ArrayList<String>();
		List<SiacRSoggettoOnereModFin> listaValidi = siacRSoggettoOnereModRepository
				.findValidoBySoggModId(idSiacTSoggettoInModifica, timestampInserimento);
		ArrayList<String> oneriOld = new ArrayList<String>();
		if (listaValidi != null && listaValidi.size() > 0)
		{
			for (SiacRSoggettoOnereModFin iteratoOld : listaValidi)
			{
				String codOld = iteratoOld.getSiacDOnere().getOnereCode();
				oneriOld.add(codOld);
				if (!listaOneri.contains(codOld))
				{
					daEliminare.add(iteratoOld);
				}
			}
		}
		if (oneriCodes != null && oneriCodes.length > 0)
		{
			for (String onereNew : oneriCodes)
			{
				if (!oneriOld.contains(onereNew))
				{
					nuoviOneri.add(onereNew);
				}
			}
		}
		if ((nuoviOneri == null || nuoviOneri.size() == 0)
				&& (daEliminare == null || daEliminare.size() == 0))
		{
			oneriInModificaInfoDto.setRimastiUguali(true);
		}
		else
		{
			oneriInModificaInfoDto.setRimastiUguali(false);
		}
		oneriInModificaInfoDto.setNuoviOneri(nuoviOneri);
		oneriInModificaInfoDto.setOneriEliminati(daEliminare);
		// Termino restituendo l'oggetto di ritorno:
		return oneriInModificaInfoDto;
	}

	/**
	 * getElencoCodiciClassificazioniMod
	 * 
	 * @param idSoggMod
	 * @return
	 */
	private String[] getElencoCodiciClassificazioniMod(Integer idSoggMod)
	{
		String[] elencoClass = null;
		long currMillisec = System.currentTimeMillis();
		Date dateInserimento = new Date(currMillisec);
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);
		ArrayList<String> lista = new ArrayList<String>();
		List<SiacRSoggettoClasseModFin> siacRSoggClass = siacRSoggettoClasseModRepository
				.findValidoBySoggModId(idSoggMod, timestampInserimento);
		if (siacRSoggClass != null && siacRSoggClass.size() > 0)
		{
			for (SiacRSoggettoClasseModFin it : siacRSoggClass)
			{
				if (it != null)
				{
					lista.add(it.getSiacDSoggettoClasse().getSoggettoClasseCode());
				}
			}
		}
		elencoClass = lista.toArray(new String[lista.size()]);
		// Termino restituendo l'oggetto di ritorno:
		return elencoClass;
	}

	/**
	 * getElencoCodiciOneriMod
	 * 
	 * @param idSoggMod
	 * @return
	 */
	private String[] getElencoCodiciOneriMod(Integer idSoggMod)
	{
		String[] elencoCods = null;
		long currMillisec = System.currentTimeMillis();
		Date dateInserimento = new Date(currMillisec);
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);
		ArrayList<String> lista = new ArrayList<String>();
		List<SiacRSoggettoOnereModFin> siacRSoggOnere = siacRSoggettoOnereModRepository
				.findValidoBySoggModId(idSoggMod, timestampInserimento);
		if (siacRSoggOnere != null && siacRSoggOnere.size() > 0)
		{
			for (SiacRSoggettoOnereModFin it : siacRSoggOnere)
			{
				if (it != null)
				{
					lista.add(it.getSiacDOnere().getOnereCode());
				}
			}
		}
		elencoCods = lista.toArray(new String[lista.size()]);
		// Termino restituendo l'oggetto di ritorno:
		return elencoCods;
	}

	/**
	 * valutaClassificazioniDaModificare
	 * 
	 * @param classificazioni
	 * @param idSiacTSoggettoInModifica
	 * @return
	 */
	private ClassificazioniInModificaInfoDto valutaClassificazioniDaModificare(
			String[] classificazioni, Integer idSiacTSoggettoInModifica)
	{
		ClassificazioniInModificaInfoDto classificazioniInModificaInfoDto = new ClassificazioniInModificaInfoDto();
		ArrayList<String> listaClass = new ArrayList<String>();
		long currMillisec = System.currentTimeMillis();
		Date dateInserimento = new Date(currMillisec);
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);
		if (classificazioni != null && classificazioni.length > 0)
		{
			listaClass = new ArrayList<String>(Arrays.asList(classificazioni));
		}
		ArrayList<SiacRSoggettoClasseFin> daEliminare = new ArrayList<SiacRSoggettoClasseFin>();
		ArrayList<String> nuoveClassificazioni = new ArrayList<String>();
		List<SiacRSoggettoClasseFin> listaValidi = siacRSoggettoClasseRepository
				.findValidiByIdSoggetto(idSiacTSoggettoInModifica, timestampInserimento);
		ArrayList<String> classificazioniOld = new ArrayList<String>();
		if (listaValidi != null && listaValidi.size() > 0)
		{
			for (SiacRSoggettoClasseFin iteratoOld : listaValidi)
			{
				String codOld = iteratoOld.getSiacDSoggettoClasse().getSoggettoClasseCode();
				classificazioniOld.add(codOld); // mancava questa parte
				if (!listaClass.contains(codOld))
				{
					daEliminare.add(iteratoOld);
				}
			}
		}
		if (classificazioni != null && classificazioni.length > 0)
		{
			for (String classNewIt : classificazioni)
			{
				if (!classificazioniOld.contains(classNewIt))
				{
					nuoveClassificazioni.add(classNewIt);
				}
			}
		}
		if ((nuoveClassificazioni == null || nuoveClassificazioni.size() == 0)
				&& (daEliminare == null || daEliminare.size() == 0))
		{
			classificazioniInModificaInfoDto.setRimasteUguali(true);
		}
		else
		{
			classificazioniInModificaInfoDto.setRimasteUguali(false);
		}
		classificazioniInModificaInfoDto.setClassificazioniEliminate(daEliminare);
		classificazioniInModificaInfoDto.setNuoveClassi(nuoveClassificazioni);
		// Termino restituendo l'oggetto di ritorno:
		return classificazioniInModificaInfoDto;
	}

	/**
	 * valutaClassificazioniModDaModificare
	 * 
	 * @param classificazioni
	 * @param idSiacTSoggettoInModifica
	 * @return
	 */
	private ClassificazioniModInModificaInfoDto valutaClassificazioniModDaModificare(
			String[] classificazioni, Integer idSiacTSoggettoInModifica)
	{
		ClassificazioniModInModificaInfoDto classificazioniInModificaInfoDto = new ClassificazioniModInModificaInfoDto();
		ArrayList<String> listaClass = new ArrayList<String>();
		long currMillisec = System.currentTimeMillis();
		Date dateInserimento = new Date(currMillisec);
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);
		if (classificazioni != null && classificazioni.length > 0)
		{
			listaClass = new ArrayList<String>(Arrays.asList(classificazioni));
		}
		ArrayList<SiacRSoggettoClasseModFin> daEliminare = new ArrayList<SiacRSoggettoClasseModFin>();
		ArrayList<String> nuoveClassificazioni = new ArrayList<String>();
		List<SiacRSoggettoClasseModFin> listaValidi = siacRSoggettoClasseModRepository
				.findValidoBySoggModId(idSiacTSoggettoInModifica, timestampInserimento);
		ArrayList<String> classificazioniOld = new ArrayList<String>();
		if (listaValidi != null && listaValidi.size() > 0)
		{
			for (SiacRSoggettoClasseModFin iteratoOld : listaValidi)
			{
				String codOld = iteratoOld.getSiacDSoggettoClasse().getSoggettoClasseCode();
				classificazioniOld.add(codOld);
				if (!listaClass.contains(codOld))
				{
					daEliminare.add(iteratoOld);
				}
			}
		}
		if (classificazioni != null && classificazioni.length > 0)
		{
			for (String classNewIt : classificazioni)
			{
				if (!classificazioniOld.contains(classNewIt))
				{
					nuoveClassificazioni.add(classNewIt);
				}
			}
		}
		if ((nuoveClassificazioni == null || nuoveClassificazioni.size() == 0)
				&& (daEliminare == null || daEliminare.size() == 0))
		{
			classificazioniInModificaInfoDto.setRimasteUguali(true);
		}
		else
		{
			classificazioniInModificaInfoDto.setRimasteUguali(false);
		}
		classificazioniInModificaInfoDto.setClassificazioniEliminate(daEliminare);
		classificazioniInModificaInfoDto.setNuoveClassi(nuoveClassificazioni);
		// Termino restituendo l'oggetto di ritorno:
		return classificazioniInModificaInfoDto;
	}

	/**
	 * valutaIndirizziDaModificare
	 * 
	 * @param listaIndirizzi
	 * @param idSoggetto
	 * @return
	 */
	private IndirizziInModificaInfoDto valutaIndirizziDaModificare(
			List<IndirizzoSoggetto> listaIndirizzi, Integer idSoggetto)
	{
		IndirizziInModificaInfoDto info = new IndirizziInModificaInfoDto();
		ArrayList<IndirizzoSoggetto> indirizziDaInserire = new ArrayList<IndirizzoSoggetto>();
		ArrayList<IndirizzoSoggetto> indirizziDaModificare = new ArrayList<IndirizzoSoggetto>();
		long currMillisec = System.currentTimeMillis();
		Date dateInserimento = new Date(currMillisec);
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);
		List<SiacTIndirizzoSoggettoFin> indirizziOld = siacTIndirizzoSoggettoRepository
				.findValidiByIdSoggetto(idSoggetto, timestampInserimento);
		ArrayList<Integer> listaIdRicevutiDalFrontEnd = new ArrayList<Integer>();
		for (IndirizzoSoggetto indirizzoIterato : listaIndirizzi)
		{
			if (indirizzoIterato.getIndirizzoId() == null
					|| indirizzoIterato.getIndirizzoId().intValue() == 0)
			{
				// se non mi viene passato un id vuol dire che e' nuovo
				indirizziDaInserire.add(indirizzoIterato);
			}
			else
			{
				// altrimenti si tratta di modificarne uno gia nel database
				boolean modificato = isIndirizzoDelSoggettoModificato(indirizzoIterato, idSoggetto);
				if (modificato)
				{
					indirizziDaModificare.add(indirizzoIterato);
				}
				listaIdRicevutiDalFrontEnd.add(indirizzoIterato.getIndirizzoId());
			}
		}
		ArrayList<SiacTIndirizzoSoggettoFin> indirizziDaEliminare = new ArrayList<SiacTIndirizzoSoggettoFin>();
		for (SiacTIndirizzoSoggettoFin iteratoOld : indirizziOld)
		{
			if (!listaIdRicevutiDalFrontEnd.contains(iteratoOld.getUid()))
			{
				// si deduce che se non e' stat passato dal front end vuol dire
				// che va ranzato:
				indirizziDaEliminare.add(iteratoOld);
			}
		}

		info.setIndirizziDaEliminare(indirizziDaEliminare);
		info.setIndirizziDaInserire(indirizziDaInserire);
		info.setIndirizziDaModificare(indirizziDaModificare);
		info.setIndirizziOld(indirizziOld);
		info.setListaIdRicevutiDalFrontEnd(listaIdRicevutiDalFrontEnd);
		if ((indirizziDaInserire == null || indirizziDaInserire.size() == 0)
				&& (indirizziDaEliminare == null || indirizziDaEliminare.size() == 0)
				&& (indirizziDaModificare == null || indirizziDaModificare.size() == 0))
		{
			info.setRimastiUguali(true);
		}
		else
		{
			info.setRimastiUguali(false);
		}
		// Termino restituendo l'oggetto di ritorno:
		return info;
	}

	/**
	 * valutaIndirizziModDaModificare
	 * 
	 * @param listaIndirizzi
	 * @param idSoggetto
	 * @return
	 */
	private IndirizziModInModificaInfoDto valutaIndirizziModDaModificare(
			List<IndirizzoSoggetto> listaIndirizzi, Integer idSoggetto)
	{
		IndirizziModInModificaInfoDto info = new IndirizziModInModificaInfoDto();
		ArrayList<IndirizzoSoggetto> indirizziDaInserire = new ArrayList<IndirizzoSoggetto>();
		ArrayList<IndirizzoSoggetto> indirizziDaModificare = new ArrayList<IndirizzoSoggetto>();
		long currMillisec = System.currentTimeMillis();
		Date dateInserimento = new Date(currMillisec);
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);
		List<SiacTIndirizzoSoggettoModFin> indirizziOld = siacTIndirizzoSoggettoModRepository
				.findValidoBySoggModId(idSoggetto, timestampInserimento);
		ArrayList<Integer> listaIdRicevutiDalFrontEnd = new ArrayList<Integer>();
		for (IndirizzoSoggetto indirizzoIterato : listaIndirizzi)
		{
			if (indirizzoIterato.getIndirizzoId() == null
					|| indirizzoIterato.getIndirizzoId().intValue() == 0)
			{
				// se non mi viene passato un id vuol dire che e' nuovo
				indirizziDaInserire.add(indirizzoIterato);
			}
			else
			{
				// altrimenti si tratta di modificarne uno gia nel database
				boolean modificato = isIndirizzoDelSoggettoModModificato(indirizzoIterato,
						idSoggetto);
				if (modificato)
				{
					indirizziDaModificare.add(indirizzoIterato);
				}
				listaIdRicevutiDalFrontEnd.add(indirizzoIterato.getIndirizzoId());
			}
		}
		ArrayList<SiacTIndirizzoSoggettoModFin> indirizziDaEliminare = new ArrayList<SiacTIndirizzoSoggettoModFin>();
		for (SiacTIndirizzoSoggettoModFin iteratoOld : indirizziOld)
		{
			if (!listaIdRicevutiDalFrontEnd.contains(iteratoOld.getUid()))
			{
				// si deduce che se non e' stat passato dal front end vuol dire
				// che va ranzato:
				indirizziDaEliminare.add(iteratoOld);
			}
		}

		info.setIndirizziDaEliminare(indirizziDaEliminare);
		info.setIndirizziDaInserire(indirizziDaInserire);
		info.setIndirizziDaModificare(indirizziDaModificare);
		info.setIndirizziOld(indirizziOld);
		info.setListaIdRicevutiDalFrontEnd(listaIdRicevutiDalFrontEnd);
		if ((indirizziDaInserire == null || indirizziDaInserire.size() == 0)
				&& (indirizziDaEliminare == null || indirizziDaEliminare.size() == 0)
				&& (indirizziDaModificare == null || indirizziDaModificare.size() == 0))
		{
			info.setRimastiUguali(true);
		}
		else
		{
			info.setRimastiUguali(false);
		}
		// Termino restituendo l'oggetto di ritorno:
		return info;
	}

	/**
	 * valutaModPagDaModificare
	 * 
	 * @param listamodalita
	 * @param idSoggetto
	 * @param datiOperazioneDto
	 * @param idEnte
	 * @param sedi
	 * @return
	 */
	private ModalitaPagamentoInModificaInfoDto valutaModPagDaModificare(
			List<ModalitaPagamentoSoggetto> listamodalita, Integer idSoggetto,
			DatiOperazioneDto datiOperazioneDto, Integer idEnte, List<SedeSecondariaSoggetto> sedi)
	{
		ModalitaPagamentoInModificaInfoDto info = new ModalitaPagamentoInModificaInfoDto();
		ArrayList<ModalitaPagamentoSoggetto> modalitaDaInserire = new ArrayList<ModalitaPagamentoSoggetto>();
		ArrayList<ModalitaPagamentoSoggetto> modalitaDaModificare = new ArrayList<ModalitaPagamentoSoggetto>();
		ArrayList<ModalitaPagamentoSoggetto> modalitaPagamentoSoggettoCessioneToDelete = new ArrayList<ModalitaPagamentoSoggetto>();
		ArrayList<ModalitaPagamentoSoggetto> modalitaPagamentoSoggettoInModifica = new ArrayList<ModalitaPagamentoSoggetto>();
		long currMillisec = System.currentTimeMillis();
		Date dateInserimento = new Date(currMillisec);
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);
		List<SiacTModpagFin> modalitaOld = siacTModpagRepository.findValidiByIdSoggetto(idSoggetto,
				timestampInserimento);
		ArrayList<Integer> listaIdModificati = new ArrayList<Integer>();

		// Primo ciclo per mdp associate al soggetto e quelle nuove

		if (listamodalita != null && listamodalita.size() > 0)
			for (ModalitaPagamentoSoggetto modPagIterato : listamodalita)
			{
				// Se non ha un uid vuol dire che e' nuovo
				if (modPagIterato.getUid() == 0)
				{
					modalitaDaInserire.add(modPagIterato);
				}
				else
				{
					if (!modPagIterato.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase(
							Constanti.STATO_IN_MODIFICA_no_underscore))
					{
						// Altrimenti va in modifica
						if (isInModificaModPag(modPagIterato, idSoggetto, idEnte, datiOperazioneDto))
						{
							modalitaDaModificare.add(modPagIterato);
						}
	
					}
					else
					{
						modalitaPagamentoSoggettoInModifica.add(modPagIterato);
					}
	
					if (modPagIterato.getUidOrigine() != null && modPagIterato.getUidOrigine() != 0)
					{
						listaIdModificati.add(modPagIterato.getUidOrigine());
					}
					else
					{
						listaIdModificati.add(modPagIterato.getUid());
					}
	
				}
			}

		ArrayList<SiacTModpagFin> modalitaDaEliminare = new ArrayList<SiacTModpagFin>();
		for (SiacTModpagFin iterateOld : modalitaOld)
		{
			if (!listaIdModificati.contains(iterateOld.getUid()))
			{
				// Non e' presente e va eliminato
				modalitaDaEliminare.add(iterateOld);
			}
		}

		// secondo ciclo per mdp associate alle sedi secondarie del soggetto
		// solo per l'eliminazione
		if (sedi != null && !sedi.isEmpty())
		{
			for (SedeSecondariaSoggetto sedeApp : sedi)
			{
				List<SiacTModpagFin> modalitaSedSecOld = siacTModpagRepository.findValidiByIdSoggetto(
						Integer.valueOf(sedeApp.getUid()), timestampInserimento);
				for (SiacTModpagFin iterateOld : modalitaSedSecOld)
				{
					// boolean mdpMod = false;
					if (!listaIdModificati.contains(iterateOld.getUid()))
					{
						// Non e' presente e va eliminato almeno che non sia una
						// mdp in modifica allora effettuo il controllo
						modalitaDaEliminare.add(iterateOld);
					}
				}

				Timestamp now = new Timestamp(System.currentTimeMillis());

				// Controllo tipo cessioni soggetto principale
				List<SiacTModpagFin> listaTModpagsCessioni = siacTSoggettoRepository
						.ricercaModPagCessioni(idEnte, sedeApp.getUid(), now);
				List<ModalitaPagamentoSoggetto> listaModPagsCessioni = null;
				if (listaTModpagsCessioni != null)
				{
					listaModPagsCessioni = convertiLista(listaTModpagsCessioni,
							ModalitaPagamentoSoggetto.class,
							FinMapId.SiacTModPag_ModalitaPagamentoSoggetto);
					// TODO capire se aggiungere il parametro aggiuntivo
					// SiacTSoggettoFin
					listaModPagsCessioni = modalitaPagamentoEntityToModalitaPagamentoCessioniModel(
							null, listaTModpagsCessioni, listaModPagsCessioni, sedeApp.getUid(),
							"", now);
					for (ModalitaPagamentoSoggetto mdpApp : listaModPagsCessioni)
					{
						boolean exist = false;
						for (Integer idMod : listaIdModificati)
						{
							if (mdpApp.getUid() == idMod)
							{
								exist = true;
							}
						}
						if (!exist)
						{
							modalitaPagamentoSoggettoCessioneToDelete.add(mdpApp);
						}
					}
				}

			}// end for
		}// end if

		Timestamp now = new Timestamp(System.currentTimeMillis());

		// Controllo tipo cessioni soggetto principale
		List<SiacTModpagFin> listaTModpagsCessioni = siacTSoggettoRepository.ricercaModPagCessioni(
				idEnte, idSoggetto, now);
		List<ModalitaPagamentoSoggetto> listaModPagsCessioni = null;
		if (listaTModpagsCessioni != null)
		{
			listaModPagsCessioni = convertiLista(listaTModpagsCessioni,
					ModalitaPagamentoSoggetto.class, FinMapId.SiacTModPag_ModalitaPagamentoSoggetto);
			// TODO capire se aggiungere il parametro aggiuntivo SiacTSoggettoFin
			listaModPagsCessioni = modalitaPagamentoEntityToModalitaPagamentoCessioniModel(null,
					listaTModpagsCessioni, listaModPagsCessioni, idSoggetto, "", now);
			for (ModalitaPagamentoSoggetto mdpApp : listaModPagsCessioni)
			{
				boolean exist = false;
				for (Integer idMod : listaIdModificati)
				{
					if (mdpApp.getUid() == idMod)
					{
						exist = true;
					}
				}
				if (!exist)
				{
					modalitaPagamentoSoggettoCessioneToDelete.add(mdpApp);
				}
			}
		}

		// Controllo tipo cessioni soggetto sede secondaria
		info.setModalitaDaEliminare(modalitaDaEliminare);
		info.setModalitaDaInserire(modalitaDaInserire);
		info.setModalitaDaModificare(modalitaDaModificare);
		info.setModalitaOld(modalitaOld);
		info.setListaIdModificati(listaIdModificati);
		info.setModalitaPagamentoSoggettoCessioneDaEliminare(modalitaPagamentoSoggettoCessioneToDelete);
		info.setModalitaPagamentoSoggettoInModifica(modalitaPagamentoSoggettoInModifica);

		if ((modalitaDaInserire == null || modalitaDaInserire.size() == 0)
				&& (modalitaDaEliminare == null || modalitaDaEliminare.size() == 0)
				&& (modalitaDaModificare == null || modalitaDaModificare.size() == 0)
				&& (modalitaPagamentoSoggettoCessioneToDelete == null || modalitaPagamentoSoggettoCessioneToDelete.size() == 0)
				&& modalitaPagamentoSoggettoInModifica.size() == 0)
		{
			info.setRimasteUguali(true);
		}
		else
		{
			info.setRimasteUguali(false);
		}
		// Termino restituendo l'oggetto di ritorno:
		return info;
	}

	/**
	 * isInModificaModPag
	 * 
	 * @param modPagToCheck
	 * @param idSoggetto
	 * @param idEnte
	 * @param datiOperazioneDto
	 * @return
	 */
	private boolean isInModificaModPag(ModalitaPagamentoSoggetto modPagToCheck, Integer idSoggetto,
			Integer idEnte, DatiOperazioneDto datiOperazioneDto)
	{

		boolean isInModifica = false;

		// Controllo che esita nella tabella siac_t_modpag_mod
		SiacTModpagModFin siacModpagModToCheck = siacTModpagModRepository.findValidoByModpagId(
				modPagToCheck.getUid(), datiOperazioneDto.getTs());

		if (siacModpagModToCheck != null)
		{

			// Controllo che non sia di tipo cessione xke la gestione e'
			// differente
			if (!modPagToCheck.isTipoCessione())
			{

				// Controllo codice fiscale quietanzante
				if (modPagToCheck.getCodiceFiscaleQuietanzante() != null)
				{
					if (siacModpagModToCheck.getQuietanzianteCodiceFiscale() == null)
					{
						isInModifica = true;
					}
					else
					{
						if (!modPagToCheck
								.getCodiceFiscaleQuietanzante()
								.trim()
								.equalsIgnoreCase(
										siacModpagModToCheck.getQuietanzianteCodiceFiscale().trim()))
						{
							isInModifica = true;
						}
					}
				}
				else
				{
					if (siacModpagModToCheck.getQuietanzianteCodiceFiscale() != null)
					{
						isInModifica = true;
					}
				}

				// Contollo soggetto quietanzante
				if (modPagToCheck.getSoggettoQuietanzante() != null)
				{
					if (siacModpagModToCheck.getQuietanziante() == null)
					{
						isInModifica = true;
					}
					else
					{
						if (!modPagToCheck.getSoggettoQuietanzante().equalsIgnoreCase(
								siacModpagModToCheck.getQuietanziante()))
						{
							isInModifica = true;
						}
					}
				}
				else
				{
					if (siacModpagModToCheck.getQuietanziante() != null)
					{
						isInModifica = true;
					}
				}

				// Controllo note
				if (modPagToCheck.getNote() != null)
				{
					if (siacModpagModToCheck.getNote() == null)
					{
						isInModifica = true;
					}
					else
					{
						if (!modPagToCheck.getNote().equalsIgnoreCase(
								siacModpagModToCheck.getNote()))
						{
							isInModifica = true;
						}
					}
				}
				else
				{
					if (siacModpagModToCheck.getNote() != null)
					{
						isInModifica = true;
					}
				}

				
				// Controllo perStipendi
				if (modPagToCheck.getPerStipendi() != null)
				{
					if (siacModpagModToCheck.getPerStipendi() == null)
					{
						isInModifica = true;
					}
					else
					{
						if (!modPagToCheck.getPerStipendi().equals(
								siacModpagModToCheck.getPerStipendi()))
						{
							isInModifica = true;
						}
					}
				}
				else
				{
					if (siacModpagModToCheck.getPerStipendi() != null)
					{
						isInModifica = true;
					}
				}

			
				
				
				
				
				// Controllo data fine validita'
				if (modPagToCheck.getDataFineValidita() != null)
				{
					// se modPagToCheck data fine validita valorizzata
					if (siacModpagModToCheck.getDataFineValidita() == null)
					{
						isInModifica = true;
					}
					else
					{
						int compare = modPagToCheck.getDataFineValidita().compareTo(
								siacModpagModToCheck.getDataFineValidita());
						if (compare != 0)
						{
							isInModifica = true;
						}
					}
				}
				else
				{
					// se modPagToCheck data fine validita non valorizzata
					if (siacModpagModToCheck.getDataFineValidita() != null)
					{
						// se siacModpagModToCheck data fine validita
						// valorizzata
						isInModifica = true;
					}
				}

				// Controllo Iban
				if (modPagToCheck.getIban() != null)
				{
					if (siacModpagModToCheck.getIban() == null)
					{
						isInModifica = true;
					}
					else
					{
						if (!modPagToCheck.getIban().trim()
								.equalsIgnoreCase(siacModpagModToCheck.getIban().trim()))
						{
							isInModifica = true;
						}
					}
				}
				else
				{
					if (siacModpagModToCheck.getIban() != null)
					{
						isInModifica = true;
					}
				}

				
				
				if (modPagToCheck.getDenominazioneBanca() != null)
				{
					if (siacModpagModToCheck.getDenominazioneBanca() == null)
					{
						isInModifica = true;
					}
					else
					{
						if (!modPagToCheck.getDenominazioneBanca().trim()
								.equalsIgnoreCase(siacModpagModToCheck.getDenominazioneBanca().trim()))
						{
							isInModifica = true;
						}
					}
				}
				else
				{
					if (siacModpagModToCheck.getDenominazioneBanca() != null)
					{
						isInModifica = true;
					}
				}
				
				
				
				
				
				
				
				
				
				// Controllo conto corrente
				if (modPagToCheck.getContoCorrente() != null)
				{
					if (siacModpagModToCheck.getContocorrente() == null)
					{
						isInModifica = true;
					}
					else
					{
						if (!modPagToCheck.getContoCorrente().trim()
								.equalsIgnoreCase(siacModpagModToCheck.getContocorrente().trim()))
						{
							isInModifica = true;
						}
					}
				}
				else
				{
					if (siacModpagModToCheck.getContocorrente() != null)
					{
						isInModifica = true;
					}
				}

				// nuovi campi

				// intestazione conto
				if (modPagToCheck.getIntestazioneConto() != null)
				{
					if (siacModpagModToCheck.getContocorrenteIntestazione() == null)
					{
						isInModifica = true;
					}
					else
					{
						if (!modPagToCheck
								.getIntestazioneConto()
								.trim()
								.equalsIgnoreCase(
										siacModpagModToCheck.getContocorrenteIntestazione().trim()))
						{
							isInModifica = true;
						}
					}
				}
				else
				{
					if (siacModpagModToCheck.getContocorrenteIntestazione() != null)
					{
						isInModifica = true;
					}
				}

				// fine - nuovi campi

				// Controllo bic
				if (modPagToCheck.getBic() != null)
				{
					if (siacModpagModToCheck.getBic() == null)
					{
						isInModifica = true;
					}
					else
					{
						if (!modPagToCheck.getBic().trim()
								.equalsIgnoreCase(siacModpagModToCheck.getBic().trim()))
						{
							isInModifica = true;
						}
					}
				}
				else
				{
					if (siacModpagModToCheck.getBic() != null)
					{
						isInModifica = true;
					}
				}

				List<SiacRModpagStatoFin> siacRModpagStatoToCheckList = siacRModpagStatoRepository
						.findStatoValidoByMdpId(siacModpagModToCheck.getUid());
				if (siacRModpagStatoToCheckList != null)
				{
					if (!siacRModpagStatoToCheckList.isEmpty())
					{
						SiacRModpagStatoFin siacRModpagStatoToCheck = siacRModpagStatoToCheckList
								.get(0);
						if (!modPagToCheck.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase(
								siacRModpagStatoToCheck.getSiacDModpagStato().getModpagStatoDesc()))
						{
							isInModifica = true;

						}
					}
				}

			}
			else
			{
				SiacRSoggettoRelazFin siacRSoggettoRelazToCheck = siacRSoggettoRelazRepository
						.findOne(modPagToCheck.getUid());
				SiacRSoggettoRelazStatoFin siacRSoggettoRelazStatoToCheck = siacRSoggettoRelazStatoRepository
						.findBySoggettoRelazId(siacRSoggettoRelazToCheck.getUid());
				SiacRSoggrelModpagFin siacRSoggrelModpagToCheck = siacRSoggrelModpagRepository
						.findValidiBySoggettoRelaz(siacRSoggettoRelazToCheck.getUid());

				if (siacRSoggettoRelazToCheck != null)
				{
					// Controllo Data Fine Validita
					if (modPagToCheck.getDataFineValidita() != null)
					{
						// se modPagToCheck data fine validita valorizzata
						if (siacRSoggettoRelazToCheck.getDataFineValidita() == null)
						{
							// se siacRSoggettoRelazToCheck data fine NON
							// validita valorizzata
							isInModifica = true;
						}
						else
						{
							// se siacRSoggettoRelazToCheck data fine validita
							// valorizzata
							int compare = modPagToCheck.getDataFineValidita().compareTo(
									siacRSoggettoRelazToCheck.getDataFineValidita());
							if (compare != 0)
							{
								isInModifica = true;
							}
						}
					}
					else
					{
						// se modPagToCheck data fine validita NON valorizzata
						if (siacRSoggettoRelazToCheck.getDataFineValidita() != null)
						{
							// se siacRSoggettoRelazToCheck data fine validita
							// valorizzata
							isInModifica = true;
						}
					}

					// Controllo Note
					if (modPagToCheck.getNote() != null)
					{
						if (siacRSoggrelModpagToCheck.getNote() == null)
						{
							isInModifica = true;
						}
						else
						{
							if (!modPagToCheck.getNote().equalsIgnoreCase(
									siacRSoggrelModpagToCheck.getNote()))
							{
								isInModifica = true;
							}
						}
					}
					else
					{
						if (siacRSoggrelModpagToCheck.getNote() != null)
						{
							isInModifica = true;
						}
					}

					if (!siacRSoggettoRelazStatoToCheck.getSiacDRelazStato().getRelazStatoCode()
							.equalsIgnoreCase(modPagToCheck.getDescrizioneStatoModalitaPagamento()))
					{
						isInModifica = true;
					}

				}

			}

			// se non esiste controllo che le informazioni sulla mdp portate dal
			// web siano uguali a quelle reperite dal db
		}
		else
		{
			SiacTModpagFin siacTModPagDBToCheck = siacTModpagRepository
					.findOne(modPagToCheck.getUid());

			// Controllo se esiste nella tabella siac_t_modpag xke nel caso non
			// esista si tratterebbe di una tipologia cessione
			if (siacTModPagDBToCheck != null)
			{

				// Controllo che non sia di tipo cessione xke la gestione e'
				// differente
				if (modPagToCheck.getCessioneCodSoggetto() == null)
				{
					// NON PUO CAMBIARE DENOMINAZIONE O TIPO ACCREDITO

					// Controllo codice fiscale quietanzante
					if (modPagToCheck.getCodiceFiscaleQuietanzante() != null)
					{
						if (siacTModPagDBToCheck.getQuietanzianteCodiceFiscale() == null)
						{
							isInModifica = true;
						}
						else
						{
							if (!modPagToCheck
									.getCodiceFiscaleQuietanzante()
									.trim()
									.equalsIgnoreCase(
											siacTModPagDBToCheck.getQuietanzianteCodiceFiscale()
													.trim()))
							{
								isInModifica = true;
							}
						}
					}
					else
					{
						if (siacTModPagDBToCheck.getQuietanzianteCodiceFiscale() != null)
						{
							isInModifica = true;
						}
					}

					// Contollo soggetto quietanzante
					if (modPagToCheck.getSoggettoQuietanzante() != null)
					{
						if (siacTModPagDBToCheck.getQuietanziante() == null)
						{
							isInModifica = true;
						}
						else
						{
							if (!modPagToCheck.getSoggettoQuietanzante().equalsIgnoreCase(
									siacTModPagDBToCheck.getQuietanziante()))
							{
								isInModifica = true;
							}
						}
					}
					else
					{
						if (siacTModPagDBToCheck.getQuietanziante() != null)
						{
							isInModifica = true;
						}
					}

					// Controllo note
					if (modPagToCheck.getNote() != null)
					{
						if (siacTModPagDBToCheck.getNote() == null)
						{
							isInModifica = true;
						}
						else
						{
							if (!modPagToCheck.getNote().equalsIgnoreCase(
									siacTModPagDBToCheck.getNote()))
							{
								isInModifica = true;
							}
						}
					}
					else
					{
						if (siacTModPagDBToCheck.getNote() != null)
						{
							isInModifica = true;
						}
					}

					
					
					
					if (modPagToCheck.getPerStipendi() != null)
					{
						if (siacTModPagDBToCheck.getPerStipendi() == null)
						{
							isInModifica = true;
						}
						else
						{
							if (!modPagToCheck.getPerStipendi().equals(
									siacTModPagDBToCheck.getPerStipendi()))
							{
								isInModifica = true;
							}
						}
					}
					else
					{
						if (siacTModPagDBToCheck.getPerStipendi() != null)
						{
							isInModifica = true;
						}
					}

					// Controllo data fine validita
					if (modPagToCheck.getDataFineValidita() != null)
					{
						// se modPagToCheck data fine validita valorizzata
						if (siacTModPagDBToCheck.getDataFineValidita() == null)
						{
							// se siacTModPagDBToCheck data fine validita NON
							// valorizzata
							isInModifica = true;
						}
						else
						{
							// se siacTModPagDBToCheck data fine validita
							// valorizzata
							int compare = TimingUtils.resetMillisecond(
									modPagToCheck.getDataFineValidita()).compareTo(
									TimingUtils.resetMillisecond(siacTModPagDBToCheck
											.getDataFineValidita()));
							if (compare != 0)
							{
								isInModifica = true;
							}
						}
					}
					else
					{
						// se modPagToCheck data fine validita NON valorizzata
						if (siacTModPagDBToCheck.getDataFineValidita() != null)
						{
							// se siacTModPagDBToCheck data fine validita
							// valorizzata
							isInModifica = true;
						}
					}

					// Controllo Iban
					if (modPagToCheck.getIban() != null)
					{
						if (siacTModPagDBToCheck.getIban() == null)
						{
							isInModifica = true;
						}
						else
						{
							if (!modPagToCheck.getIban().trim()
									.equalsIgnoreCase(siacTModPagDBToCheck.getIban().trim()))
							{
								isInModifica = true;
							}
						}
					}
					else
					{
						if (siacTModPagDBToCheck.getIban() != null)
						{
							isInModifica = true;
						}
					}

					
					if (modPagToCheck.getDenominazioneBanca() != null)
					{
						if (siacTModPagDBToCheck.getDenominazioneBanca() == null)
						{
							isInModifica = true;
						}
						else
						{
							if (!modPagToCheck.getDenominazioneBanca().trim()
									.equalsIgnoreCase(siacTModPagDBToCheck.getDenominazioneBanca().trim()))
							{
								isInModifica = true;
							}
						}
					}
					else
					{
						if (siacTModPagDBToCheck.getDenominazioneBanca() != null)
						{
							isInModifica = true;
						}
					}

					
					
					
					
					// Controllo conto corrente
					if (modPagToCheck.getContoCorrente() != null)
					{
						if (siacTModPagDBToCheck.getContocorrente() == null)
						{
							isInModifica = true;
						}
						else
						{
							if (!modPagToCheck
									.getContoCorrente()
									.trim()
									.equalsIgnoreCase(
											siacTModPagDBToCheck.getContocorrente().trim()))
							{
								isInModifica = true;
							}
						}
					}
					else
					{
						if (siacTModPagDBToCheck.getContocorrente() != null)
						{
							isInModifica = true;
						}
					}

					// nuovi campi

					// intestazione conto
					if (modPagToCheck.getIntestazioneConto() != null)
					{
						if (siacTModPagDBToCheck.getContocorrenteIntestazione() == null)
						{
							isInModifica = true;
						}
						else
						{
							if (!modPagToCheck
									.getIntestazioneConto()
									.trim()
									.equalsIgnoreCase(
											siacTModPagDBToCheck.getContocorrenteIntestazione()
													.trim()))
							{
								isInModifica = true;
							}
						}
					}
					else
					{
						if (siacTModPagDBToCheck.getContocorrenteIntestazione() != null)
						{
							isInModifica = true;
						}
					}

					// luogo nascita e stato nascita
					if (modPagToCheck.getComuneNascita() != null)
					{
						if (siacTModPagDBToCheck.getQuietanzianteNascitaLuogo() == null)
						{
							isInModifica = true;
						}
						else
						{
							if (!modPagToCheck
									.getComuneNascita()
									.getDescrizione()
									.trim()
									.equalsIgnoreCase(
											siacTModPagDBToCheck.getQuietanzianteNascitaLuogo()
													.trim()))
							{
								isInModifica = true;
							}
						}
					}
					else
					{
						if (siacTModPagDBToCheck.getQuietanzianteNascitaLuogo() != null)
						{
							isInModifica = true;
						}
					}

					// data di nascita
					if (modPagToCheck.getDataNascitaQuietanzante() != null)
					{
						if (siacTModPagDBToCheck.getQuietanzanteNascitaData() == null)
						{
							isInModifica = true;
						}
						else
						{
							if (!modPagToCheck
									.getDataNascitaQuietanzante()
									.trim()
									.equalsIgnoreCase(
											TimingUtils
													.convertiDataIn_GgMmYyyy(siacTModPagDBToCheck
															.getQuietanzanteNascitaData())))
							{
								isInModifica = true;
							}
						}
					}
					else
					{
						if (siacTModPagDBToCheck.getQuietanzanteNascitaData() != null)
						{
							isInModifica = true;
						}
					}

					// fine - nuovi campi

					// Controllo bic
					if (modPagToCheck.getBic() != null)
					{
						if (siacTModPagDBToCheck.getBic() == null)
						{
							isInModifica = true;
						}
						else
						{
							if (!modPagToCheck.getBic().trim()
									.equalsIgnoreCase(siacTModPagDBToCheck.getBic().trim()))
							{
								isInModifica = true;
							}
						}
					}
					else
					{
						if (siacTModPagDBToCheck.getBic() != null)
						{
							isInModifica = true;
						}
					}

					// Controllo lo stato modalita pagamento
					// Prendo record relativo stato modalita
					SiacRModpagStatoFin siacRModpagStatoToCheck = siacRModpagStatoRepository
							.findStatoValidoByMdpId(siacTModPagDBToCheck.getUid()).get(0);
					if (siacRModpagStatoToCheck != null)
					{
						if (modPagToCheck.getDescrizioneStatoModalitaPagamento() != null)
						{
							if (!modPagToCheck.getDescrizioneStatoModalitaPagamento()
									.equalsIgnoreCase(
											siacRModpagStatoToCheck.getSiacDModpagStato()
													.getModpagStatoDesc()))
							{
								isInModifica = true;
							}
						}
					}

				}
				else
				{
					// Se nella tabella siac_t_modpag non esiste potrebbe essere
					// di tipo cessione e va trattato in maniera differente
					if (modPagToCheck.getCessioneCodSoggetto() != null)
					{
						SiacRSoggettoRelazFin siacRSoggettoRelazToCheck = siacRSoggettoRelazRepository
								.findOne(modPagToCheck.getUid());
						SiacRSoggettoRelazStatoFin siacRSoggettoRelazStatoToCheck = siacRSoggettoRelazStatoRepository
								.findBySoggettoRelazId(siacRSoggettoRelazToCheck.getUid());
						SiacRSoggrelModpagFin siacRSoggrelModpagToCheck = siacRSoggrelModpagRepository
								.findValidiBySoggettoRelaz(siacRSoggettoRelazToCheck.getUid());

						if (siacRSoggettoRelazToCheck != null)
						{

							// Controllo Data Fine Validita
							if (modPagToCheck.getDataFineValidita() != null)
							{
								// se modPagToCheck data fine validita
								// valorizzata
								if (siacRSoggettoRelazToCheck.getDataFineValidita() == null)
								{
									// se siacRSoggettoRelazToCheck data fine
									// validita NON valorizzata
									isInModifica = true;
								}
								else
								{
									// se siacRSoggettoRelazToCheck data fine
									// validita valorizzata
									int compare = modPagToCheck.getDataFineValidita().compareTo(
											siacRSoggrelModpagToCheck.getDataFineValidita());
									if (compare != 0)
									{
										isInModifica = true;
									}
								}
							}
							else
							{
								// se modPagToCheck data fine validita NON
								// valorizzata
								if (siacRSoggettoRelazToCheck.getDataFineValidita() != null)
								{
									isInModifica = true;
								}
							}

							// Controllo Note
							if (modPagToCheck.getNote() != null)
							{
								if (siacRSoggrelModpagToCheck.getNote() == null)
								{
									isInModifica = true;
								}
								else
								{
									if (!modPagToCheck.getNote().equalsIgnoreCase(
											siacRSoggrelModpagToCheck.getNote()))
									{
										isInModifica = true;
									}
								}
							}
							else
							{
								if (siacRSoggrelModpagToCheck.getNote() != null)
								{
									isInModifica = true;
								}
							}

							if (!siacRSoggettoRelazStatoToCheck
									.getSiacDRelazStato()
									.getRelazStatoCode()
									.equalsIgnoreCase(
											modPagToCheck.getDescrizioneStatoModalitaPagamento()))
							{
								isInModifica = true;
							}

						}
					}
				}

				// Se non esiste potrebbe essere di tipo cessione
			}
			else
			{

				// Se nella tabella siac_t_modpag non esiste potrebbe essere di
				// tipo cessione e va trattato in maniera differente
//				if (String.valueOf(modPagToCheck.getTipoAccredito()).equals(
//						Constanti.D_ACCREDITO_TIPO_CODE_Cessione_del_credito)
//						|| String.valueOf(modPagToCheck.getTipoAccredito()).equals(
//								Constanti.D_ACCREDITO_TIPO_CODE_Cessione_dell_incasso))
//				{
					SiacRSoggettoRelazFin siacRSoggettoRelazToCheck = siacRSoggettoRelazRepository
							.findOne(modPagToCheck.getUid());
					
				if (siacRSoggettoRelazToCheck != null) {	
					SiacRSoggettoRelazStatoFin siacRSoggettoRelazStatoToCheck = siacRSoggettoRelazStatoRepository
							.findBySoggettoRelazId(siacRSoggettoRelazToCheck.getUid());
					SiacRSoggrelModpagFin siacRSoggrelModpagToCheck = siacRSoggrelModpagRepository
							.findValidiBySoggettoRelaz(siacRSoggettoRelazToCheck.getUid());

					if (siacRSoggettoRelazToCheck != null)
					{
						// Controllo Data Fine Validita
						if (modPagToCheck.getDataFineValidita() != null)
						{
							// se modPagToCheck data fine validita valorizzata
							if (siacRSoggettoRelazToCheck.getDataFineValidita() == null)
							{
								// se siacRSoggettoRelazToCheck data fine
								// validita NON valorizzata
								isInModifica = true;
							}
							else
							{
								// se siacRSoggettoRelazToCheck data fine
								// validita valorizzata
								int compare = modPagToCheck.getDataFineValidita().compareTo(
										siacRSoggettoRelazToCheck.getDataFineValidita());
								if (compare != 0)
								{
									isInModifica = true;
								}
							}
						}
						else
						{
							// se modPagToCheck data fine validita NON
							// valorizzata
							if (siacRSoggettoRelazToCheck.getDataFineValidita() != null)
							{
								// se siacRSoggettoRelazToCheck data fine
								// validita valorizzata
								isInModifica = true;
							}
						}
						// Controllo Note
						if (modPagToCheck.getNote() != null)
						{
							if (siacRSoggrelModpagToCheck.getNote() == null)
							{
								isInModifica = true;
							}
							else
							{
								if (!modPagToCheck.getNote().equalsIgnoreCase(
										siacRSoggrelModpagToCheck.getNote()))
								{
									isInModifica = true;
								}
							}
						}
						else
						{
							if (siacRSoggrelModpagToCheck.getNote() != null)
							{
								isInModifica = true;
							}
						}
						if (!siacRSoggettoRelazStatoToCheck
								.getSiacDRelazStato()
								.getRelazStatoCode()
								.equalsIgnoreCase(
										modPagToCheck.getDescrizioneStatoModalitaPagamento()))
						{
							isInModifica = true;
						}

					}
				}

			}

		}
		// Termino restituendo l'oggetto di ritorno:
		return isInModifica;
	}

	/**
	 * saveRFormGiuridica
	 * 
	 * @param siacTFormaGiuridica
	 * @param siacTSoggetto
	 * @param datiOperazioneDto
	 */
	private void saveRFormGiuridica(SiacTFormaGiuridicaFin siacTFormaGiuridica,
			SiacTSoggettoFin siacTSoggetto, DatiOperazioneDto datiOperazioneDto)
	{
		SiacRFormaGiuridicaFin rFormaGiuridica = new SiacRFormaGiuridicaFin();
		rFormaGiuridica = DatiOperazioneUtils.impostaDatiOperazioneLogin(rFormaGiuridica,
				datiOperazioneDto, siacTAccountRepository);
		rFormaGiuridica.setSiacTSoggetto(siacTSoggetto);
		rFormaGiuridica.setSiacTFormaGiuridica(siacTFormaGiuridica);
		// salvo sul db:
		siacRFormaGiuridicaRepository.saveAndFlush(rFormaGiuridica);
	}

	/**
	 * saveSoggettoTipo
	 * 
	 * @param tipoSoggetto
	 * @param idEnte
	 * @param siacTSoggetto
	 * @param datiOperazioneDto
	 * @return
	 */
	private SiacRSoggettoTipoFin saveSoggettoTipo(TipoSoggetto tipoSoggetto, Integer idEnte,
			SiacTSoggettoFin siacTSoggetto, DatiOperazioneDto datiOperazioneDto)
	{
		SiacDSoggettoTipoFin siacDSoggettoTipo = null;
		if (!StringUtils.isEmpty(tipoSoggetto.getCodice()))
		{
			String code = tipoSoggetto.getCodice().trim().toUpperCase();
			siacDSoggettoTipo = siacDSoggettoTipoRepository.findValidoByCode(idEnte, code,
					datiOperazioneDto.getTs()).get(0);
		}
		if (siacDSoggettoTipo != null)
		{
			SiacRSoggettoTipoFin siacRSoggettoTipo = new SiacRSoggettoTipoFin();
			siacRSoggettoTipo = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRSoggettoTipo,
					datiOperazioneDto, siacTAccountRepository);
			siacRSoggettoTipo.setSiacTSoggetto(siacTSoggetto);
			siacRSoggettoTipo.setSiacDSoggettoTipo(siacDSoggettoTipo);
			// salvo sul db:
			return siacRSoggettoTipoRepository.saveAndFlush(siacRSoggettoTipo);
		}
		return null;
	}

	/**
	 * savePersonaFisica
	 * 
	 * @param soggetto
	 * @param personaFisica
	 * @param timestampInserimento
	 * @param siacTSoggetto
	 * @param datiOperazioneDto
	 * @param fromInsert
	 * @return
	 */
	private SiacTPersonaFisicaFin savePersonaFisica(Soggetto soggetto,
			SiacTPersonaFisicaFin personaFisica, Timestamp timestampInserimento,
			SiacTSoggettoFin siacTSoggetto, DatiOperazioneDto datiOperazioneDto, boolean fromInsert)
	{
		if (personaFisica == null)
		{
			// caso di inserimento nuovo record
			personaFisica = new SiacTPersonaFisicaFin();
		}
		personaFisica.setNome(soggetto.getNome());
		personaFisica.setCognome(soggetto.getCognome());
		String sesso = null;
		if (fromInsert)
		{
			if (Constanti.MASCHIO.equals(soggetto.getSesso().name().toUpperCase()))
			{
				sesso = Constanti.SESSO_M;
			}
			else
			{
				sesso = Constanti.SESSO_F;
			}
		}
		else
		{
			if (Constanti.MASCHIO.equals(soggetto.getSessoStringa().toUpperCase()))
			{
				sesso = Constanti.SESSO_M;
			}
			else
			{
				sesso = Constanti.SESSO_F;
			}
		}
		personaFisica.setSesso(sesso);
		personaFisica.setNascitaData(new Timestamp(soggetto.getDataNascita().getTime()));
		
		SiacTComuneFin comuneTrovato = gestisciComuneNascita(soggetto.getComuneNascita(), datiOperazioneDto);
		
		personaFisica.setSiacTComune(comuneTrovato);
		personaFisica.setSiacTSoggetto(siacTSoggetto);
		return savePersonaFisica(personaFisica, datiOperazioneDto);
	}

	/**
	 * Gestisce il recupero del record SiacTComuneFin per dati indicati in IndirizzoSoggetto
	 * @param indirizzo
	 * @param datiOperazione
	 * @return
	 */
	private SiacTComuneFin gestisciComuneIndirizzoSoggetto(IndirizzoSoggetto indirizzo, DatiOperazioneDto datiOperazione){
		SiacTComuneFin comuneTrovato = null;
		if(indirizzo!=null){
			//leggiamo le variabili da passare al metodo centralizzato:
			String codeIstat = indirizzo.getIdComune();
			String descrizioneComune = indirizzo.getComune();
			String codiceNazione = indirizzo.getCodiceNazione();
			//chiamiamo il metodo centralizzato:
			comuneTrovato = gestisciComune(codeIstat , descrizioneComune , codiceNazione , datiOperazione);
		}
		return comuneTrovato;
	}
	
	/**
	 * Gestisce il recupero del record SiacTComuneFin per dati indicati in ComuneNascita
	 * @param comuneNascita
	 * @param datiOperazione
	 * @return
	 */
	private SiacTComuneFin gestisciComuneNascita(ComuneNascita comuneNascita,DatiOperazioneDto datiOperazione){
		SiacTComuneFin comuneTrovato = null;
		if(comuneNascita != null){
			//leggiamo le variabili da passare al metodo centralizzato:
			String codeIstat = comuneNascita.getComuneIstatCode();
			String descrizioneComune = comuneNascita.getDescrizione();
			String codiceNazione = comuneNascita.getNazioneCode();
			//chiamiamo il metodo centralizzato:
			comuneTrovato = gestisciComune(codeIstat , descrizioneComune , codiceNazione , datiOperazione);
		}
		return comuneTrovato;
	}
	
	/**
	 * Metodo centralizzato per gestire il recupero del SiacTComuneFin
	 * @param codeIstat
	 * @param descrizioneComune
	 * @param codiceNazione
	 * @param datiOperazione
	 * @return
	 */
	private SiacTComuneFin gestisciComune(String codeIstat,String descrizioneComune,String codiceNazione, DatiOperazioneDto datiOperazione) {
		
		return "1".equals(codiceNazione) ? //COMUNE ITALIANO ?   
					siacTComuneRepository.caricaValidoByCodeIstat(datiOperazione.getSiacTEnteProprietario().getEnteProprietarioId(), 
							StringUtils.formatCodIstaForQuery(codeIstat) ,datiOperazione.getTs()) :  
					gestisciComuneEstero(descrizioneComune, codiceNazione, datiOperazione);
	}

	private SiacTComuneFin gestisciComuneEstero(String descrizioneComune, String codiceNazione,
			DatiOperazioneDto datiOperazioneDto)
	{
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getUid();

		SiacTComuneFin comuneTrovato;
		
		SiacTNazioneFin siacTNazione = siacTNazioneRepository.findByCodice(
				codiceNazione, idEnte);

		List<SiacTComuneFin> comuni = siacTComuneRepository.findComuneByNomePuntuale(
				descrizioneComune, siacTNazione.getNazioneCode(), idEnte);

		if (comuni.isEmpty())
		{
			comuneTrovato = new SiacTComuneFin();
			comuneTrovato.setDataCreazione(new Date());
			comuneTrovato.setDataInizioValidita(new Date());
			comuneTrovato.setDataModifica(new Date());
			comuneTrovato.setLoginOperazione(DatiOperazioneUtils.determinaUtenteLogin(
					datiOperazioneDto, siacTAccountRepository));
			comuneTrovato.setComuneDesc(descrizioneComune);
			comuneTrovato.setSiacTNazione(siacTNazione);

			SiacTEnteProprietarioFin ente = new SiacTEnteProprietarioFin();
			ente.setUid(idEnte);
			comuneTrovato.setSiacTEnteProprietario(ente);

			siacTComuneRepository.saveAndFlush(comuneTrovato);
		}
		else
			comuneTrovato = comuni.get(0);
		
		return comuneTrovato;
	}
	

	/**
	 * savePersonaFisica
	 * 
	 * @param personaFisica
	 * @param datiOperazioneDto
	 * @return
	 */
	private SiacTPersonaFisicaFin savePersonaFisica(SiacTPersonaFisicaFin personaFisica,
			DatiOperazioneDto datiOperazioneDto)
	{
		// imposto i dati operazione:
		personaFisica = DatiOperazioneUtils.impostaDatiOperazioneLogin(personaFisica,
				datiOperazioneDto, siacTAccountRepository);
		// salvo sul db e ritorno il risultato:
		return siacTPersonaFisicaRepository.saveAndFlush(personaFisica);
	}

	private SiacTPersonaFisicaModFin savePersonaFisicaMod(Soggetto soggetto,
			SiacTPersonaFisicaModFin personaFisicaMod, Timestamp timestampInserimento,
			SiacTSoggettoModFin siacTSoggettoMod, DatiOperazioneDto datiOperazioneDto)
	{
		// costruisco l'oggetto da salvare:
		personaFisicaMod = buildPersonaFisicaModForSave(soggetto, personaFisicaMod,
				timestampInserimento, siacTSoggettoMod, datiOperazioneDto);
		// salvo sul db e ritorno il risultato:
		return siacTPersonaFisicaModRepository.saveAndFlush(personaFisicaMod);
	}

	/**
	 * buildPersonaFisicaModForSave
	 * 
	 * @param soggetto
	 * @param personaFisicaMod
	 * @param timestampInserimento
	 * @param siacTSoggettoMod
	 * @param datiOperazioneDto
	 * @return
	 */
	private SiacTPersonaFisicaModFin buildPersonaFisicaModForSave(Soggetto soggetto,
			SiacTPersonaFisicaModFin personaFisicaMod, Timestamp timestampInserimento,
			SiacTSoggettoModFin siacTSoggettoMod, DatiOperazioneDto datiOperazioneDto)
	{
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		if (personaFisicaMod == null)
		{
			// caso di inserimento nuovo record
			personaFisicaMod = new SiacTPersonaFisicaModFin();
		}
		personaFisicaMod.setNome(soggetto.getNome());
		personaFisicaMod.setCognome(soggetto.getCognome());
		String sesso = null;
		if (Constanti.MASCHIO.equals(soggetto.getSesso().name().toUpperCase()))
		{
			sesso = Constanti.SESSO_M;
		}
		else
		{
			sesso = Constanti.SESSO_F;
		}
		personaFisicaMod.setSesso(sesso);
		personaFisicaMod.setNascitaData(TimingUtils.convertiDataInTimeStamp(soggetto
				.getDataNascita()));

		personaFisicaMod = DatiOperazioneUtils.impostaDatiOperazioneLogin(personaFisicaMod,
				datiOperazioneDto, siacTAccountRepository);
		
		SiacTComuneFin comuneTrovato = gestisciComuneNascita(soggetto.getComuneNascita(), datiOperazioneDto);

		
		personaFisicaMod.setSiacTComune(comuneTrovato);
		personaFisicaMod.setSiacTSoggettoMod(siacTSoggettoMod);
		personaFisicaMod.setSiacTSoggetto(siacTSoggettoMod.getSiacTSoggetto());
		// Termino restituendo l'oggetto di ritorno:
		return personaFisicaMod;
	}

	/**
	 * savePersonaGiuridica
	 * 
	 * @param soggetto
	 * @param personaGiuridica
	 * @param siacTSoggetto
	 * @param datiOperazioneDto
	 * @return
	 */
	private SiacTPersonaGiuridicaFin savePersonaGiuridica(Soggetto soggetto,
			SiacTPersonaGiuridicaFin personaGiuridica, SiacTSoggettoFin siacTSoggetto,
			DatiOperazioneDto datiOperazioneDto)
	{
		personaGiuridica.setRagioneSociale(soggetto.getDenominazione());
		personaGiuridica.setSiacTSoggetto(siacTSoggetto);
		return savePersonaGiuridica(personaGiuridica, datiOperazioneDto);
	}

	/**
	 * savePersonaGiuridica
	 * 
	 * @param personaGiuridica
	 * @param datiOperazioneDto
	 * @return
	 */
	private SiacTPersonaGiuridicaFin savePersonaGiuridica(SiacTPersonaGiuridicaFin personaGiuridica,
			DatiOperazioneDto datiOperazioneDto)
	{
		personaGiuridica = DatiOperazioneUtils.impostaDatiOperazioneLogin(personaGiuridica,
				datiOperazioneDto, siacTAccountRepository);
		return siacTPersonaGiuridicaRepository.save(personaGiuridica);
	}

	/**
	 * savePersonaGiuridicaMod
	 * 
	 * @param soggetto
	 * @param personaGiuridica
	 * @param siacTSoggetto
	 * @param siacTSoggettoMod
	 * @param datiOperazioneDto
	 * @return
	 */
	private SiacTPersonaGiuridicaModFin savePersonaGiuridicaMod(Soggetto soggetto,
			SiacTPersonaGiuridicaModFin personaGiuridica, SiacTSoggettoFin siacTSoggetto,
			SiacTSoggettoModFin siacTSoggettoMod, DatiOperazioneDto datiOperazioneDto)
	{
		personaGiuridica = buildPersonaGiuridicaModForSave(soggetto, personaGiuridica,
				siacTSoggetto, siacTSoggettoMod, datiOperazioneDto);
		return siacTPersonaGiuridicaModRepository.save(personaGiuridica);
	}

	/**
	 * buildPersonaGiuridicaModForSave
	 * 
	 * @param soggetto
	 * @param personaGiuridica
	 * @param siacTSoggetto
	 * @param siacTSoggettoMod
	 * @param datiOperazioneDto
	 * @return
	 */
	private SiacTPersonaGiuridicaModFin buildPersonaGiuridicaModForSave(Soggetto soggetto,
			SiacTPersonaGiuridicaModFin personaGiuridica, SiacTSoggettoFin siacTSoggetto,
			SiacTSoggettoModFin siacTSoggettoMod, DatiOperazioneDto datiOperazioneDto)
	{
		personaGiuridica.setRagioneSociale(soggetto.getDenominazione());
		personaGiuridica = DatiOperazioneUtils.impostaDatiOperazioneLogin(personaGiuridica,
				datiOperazioneDto, siacTAccountRepository);
		personaGiuridica.setSiacTSoggetto(siacTSoggetto);
		personaGiuridica.setSiacTSoggettoMod(siacTSoggettoMod);
		// Termino restituendo l'oggetto di ritorno:
		return personaGiuridica;
	}

	/**
	 * Metodo che controlla se sono presenti altri soggetti non annullati con
	 * stesso codiceFiscale o partitaIva del nuovo soggetto da inserire
	 * 
	 * @param req
	 * @return boolean, array di 2 - posizione 1 codiceFiscale - posizione 2
	 *         partitaIva
	 */
	public boolean[] controlloDuplicazioneCodice(String codiceSoggetto, String codiceFiscale,
			String partitaIva, Ente ente)
	{
		boolean codiceFiscalePresente = false;
		boolean partitaIvaPresente = false;
		List<SiacTSoggettoFin> soggettiTrovato;
		String statoCode = StatoOperativoAnagrafica.ANNULLATO.name();
		long currMillisec = System.currentTimeMillis();
		Date dateInserimento = new Date(currMillisec);
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);
		if (!StringUtils.isEmpty(codiceFiscale))
		{
			soggettiTrovato = siacTSoggettoRepository.findSoggettoByCodiceFiscale(
					codiceFiscale.toUpperCase(), ente.getUid(), timestampInserimento);
			if (soggettiTrovato != null && soggettiTrovato.size() > 0)
			{
				for (SiacTSoggettoFin currentSoggetto : soggettiTrovato)
				{
					if (!codiceFiscalePresente)
					{
						List<SiacRSoggettoStatoFin> listStati = currentSoggetto
								.getSiacRSoggettoStatos();
						if (listStati != null && listStati.size() > 0)
						{
							for (SiacRSoggettoStatoFin currentStatoPerSoggetto : listStati)
							{
								if (currentStatoPerSoggetto.getDataFineValidita() == null
										&& !statoCode.equalsIgnoreCase(currentStatoPerSoggetto
												.getSiacDSoggettoStato().getSoggettoStatoCode()))
								{
									// trovato valido con stesso codice
									if (!StringUtils.isEmpty(codiceSoggetto))
									{
										if (!codiceSoggetto.equalsIgnoreCase(currentSoggetto
												.getSoggettoCode()))
										{
											codiceFiscalePresente = true;
											break;
										}
									}
									else
									{
										codiceFiscalePresente = true;
										break;
									}
								}
							}
						}
					}
				}
			}
		}
		if (!StringUtils.isEmpty(partitaIva))
		{
			soggettiTrovato = siacTSoggettoRepository.findSoggettoByPartitaIva(partitaIva,
					ente.getUid(), timestampInserimento);
			if (soggettiTrovato != null && soggettiTrovato.size() > 0)
			{
				for (SiacTSoggettoFin currentSoggetto : soggettiTrovato)
				{
					if (partitaIvaPresente)
					{
						List<SiacRSoggettoStatoFin> listStati = currentSoggetto
								.getSiacRSoggettoStatos();
						if (listStati != null && listStati.size() > 0)
						{
							for (SiacRSoggettoStatoFin currentStatoPerSoggetto : listStati)
							{
								if (currentStatoPerSoggetto.getDataFineValidita() == null
										&& !statoCode.equalsIgnoreCase(currentStatoPerSoggetto
												.getSiacDSoggettoStato().getSoggettoStatoCode()))
								{
									// trovato valido con stesso codice
									if (!StringUtils.isEmpty(codiceSoggetto))
									{
										if (!codiceSoggetto.equalsIgnoreCase(currentSoggetto
												.getSoggettoCode()))
										{
											partitaIvaPresente = true;
											break;
										}
									}
									else
									{
										partitaIvaPresente = true;
										break;
									}
								}
							}
						}
					}
				}
			}
		}
		return new boolean[] { codiceFiscalePresente, partitaIvaPresente };
	}

	/**
	 * controlloCompatibilitaStatoSoggettoInModifica Utilizzato da :
	 * AnnullaSoggettoInModificaService() Il servizio rilegge i dati del
	 * soggetto in archivio e verifica che risulti ancora (in
	 * StatoOperativoAnagrafica = VALIDO e IN_MODIFICA = true) in caso contrario
	 * viene segnalato l?errore <COR_ERR_0028 Operazione incompatibile con stato
	 * dell'entit? (entit?: il soggetto , stato: StatoOperativoAnagrafica)>.
	 * 
	 * @param req
	 * @return
	 */
	public boolean controlloCompatibilitaStatoSoggettoInModifica(Soggetto req)
	{

		boolean statoEntita = false;

		Integer idSoggettoInput = req.getUid();
		SiacTSoggettoModFin soggettoModInput = siacTSoggettoModRepository.findOne(idSoggettoInput);

		SiacTSoggettoFin soggettoInput = siacTSoggettoRepository.findOne(soggettoModInput
				.getSiacTSoggetto().getUid());

		String statoCode = StatoOperativoAnagrafica.VALIDO.name();

		if (soggettoInput != null)
		{
			List<SiacRSoggettoStatoFin> listStati = soggettoInput.getSiacRSoggettoStatos();
			if (listStati != null && listStati.size() > 0)
			{
				for (SiacRSoggettoStatoFin currentStatoPerSoggetto : listStati)
				{
					if (currentStatoPerSoggetto.getDataFineValidita() == null
							&& statoCode.equalsIgnoreCase(currentStatoPerSoggetto
									.getSiacDSoggettoStato().getSoggettoStatoCode()))
					{
						// trovato valido con stesso codice
						statoEntita = true;
						break;
					}
				}
			}
		}

		// Termino restituendo l'oggetto di ritorno:
		return statoEntita;
	}

	/**
	 * controlloCompatibilitaStatoSoggettoProvvisorio Verificare che lo stato
	 * del soggetto presente in archivio sia coerente con l?operazione che si
	 * sta effettuando: i. SoggettoInArchivio.StatoOperativoAnagrafica deve
	 * essere ?Valido? o ?Provvisorio? ii. Se
	 * (SoggettoInArchivio.StatoOperativoAnagrafica = VALIDO e
	 * SoggettoInArchivio.inModifica = TRUE) o Se
	 * SoggettoInArchivio.StatoOperativoAnagrafica = PROVVISORIO l?ultima
	 * modifica in archivio deve essere stata fatta dal richiedente.
	 * 
	 * @param loginOperazione
	 * @param idSoggettoInput
	 * @return
	 */
	public boolean controlloCompatibilitaStatoSoggettoProvvisorio(String loginOperazione,
			Integer idSoggettoInput)
	{

		boolean statoEntita = false;

		// 22/09/2014 : f+c
		//
		SiacTSoggettoFin soggettoInput = siacTSoggettoRepository.findOne(idSoggettoInput);

		String statoCodeValido = StatoOperativoAnagrafica.VALIDO.name();
		String statoCodeProvvisorio = StatoOperativoAnagrafica.PROVVISORIO.name();
		String statoCodeInModifica = StatoOperativoAnagrafica.IN_MODIFICA.name();

		if (soggettoInput != null)
		{
			List<SiacRSoggettoStatoFin> listStati = soggettoInput.getSiacRSoggettoStatos();
			if (listStati != null && listStati.size() > 0)
			{
				for (SiacRSoggettoStatoFin currentStatoPerSoggetto : listStati)
				{
					if (currentStatoPerSoggetto.getDataFineValidita() == null)
					{
						// trovato valido
						if (statoCodeValido.equalsIgnoreCase(currentStatoPerSoggetto
								.getSiacDSoggettoStato().getSoggettoStatoCode())
								|| statoCodeProvvisorio.equalsIgnoreCase(currentStatoPerSoggetto
										.getSiacDSoggettoStato().getSoggettoStatoCode()))
						{
							statoEntita = true;
							break;
						}
						if (statoCodeInModifica.equalsIgnoreCase(currentStatoPerSoggetto
								.getSiacDSoggettoStato().getSoggettoStatoCode())
								|| statoCodeProvvisorio.equalsIgnoreCase(currentStatoPerSoggetto
										.getSiacDSoggettoStato().getSoggettoStatoCode()))
						{
							if (soggettoInput.getLoginModifica().equalsIgnoreCase(loginOperazione))
							{
								statoEntita = true;
								break;
							}
						}
					}
				}
			}
		}

		// Termino restituendo l'oggetto di ritorno:
		return statoEntita;
	}

	/**
	 * controlloSediSecondarieSoggettoProvvisorio
	 * 
	 * @param idSoggettoInput
	 * @param ente
	 * @param loginOperazione
	 * @return
	 */
	public boolean[] controlloSediSecondarieSoggettoProvvisorio(Integer idSoggettoInput, Ente ente,
			String loginOperazione)
	{

		boolean statoSede = false;
		boolean sedeGiaPresente = false;

		String statoCodeValido = StatoOperativoAnagrafica.VALIDO.name();
		String statoCodeProvvisorio = StatoOperativoAnagrafica.PROVVISORIO.name();
		String statoCodeInModifica = StatoOperativoAnagrafica.IN_MODIFICA.name();

		SiacTSoggettoFin soggettoInputDb = siacTSoggettoRepository.findOne(idSoggettoInput);
		List<SiacTSoggettoFin> listaSediSecondarieDb = siacTSoggettoRepository.ricercaSedi(
				ente.getUid(), idSoggettoInput, Constanti.SEDE_SECONDARIA);

		if (listaSediSecondarieDb != null && listaSediSecondarieDb.size() > 0)
		{
			for (SiacTSoggettoFin currentSedeSecondariaDb : listaSediSecondarieDb)
			{
				List<SiacRSoggettoStatoFin> listStati = currentSedeSecondariaDb
						.getSiacRSoggettoStatos();
				if (listStati != null && listStati.size() > 0)
				{
					for (SiacRSoggettoStatoFin currentStatoSede : listStati)
					{
						if (currentStatoSede.getDataFineValidita() == null)
						{
							// trovato valido
							if (statoCodeValido.equalsIgnoreCase(currentStatoSede
									.getSiacDSoggettoStato().getSoggettoStatoCode())
									|| statoCodeProvvisorio.equalsIgnoreCase(currentStatoSede
											.getSiacDSoggettoStato().getSoggettoStatoCode()))
							{
							}
							else
							{
								statoSede = true;
								break;
							}
							if (statoCodeInModifica.equalsIgnoreCase(currentStatoSede
									.getSiacDSoggettoStato().getSoggettoStatoCode())
									|| statoCodeProvvisorio.equalsIgnoreCase(currentStatoSede
											.getSiacDSoggettoStato().getSoggettoStatoCode()))
							{
								if (!soggettoInputDb.getLoginModifica().equalsIgnoreCase(
										loginOperazione))
								{
									statoSede = true;
									break;
								}
							}
						}
					}
				}
			}
		}

		return new boolean[] { statoSede, sedeGiaPresente };
	}

	/**
	 * controlliAggiornamentoSediSecondarie
	 * 
	 * @param idSoggetto
	 * @param sedi
	 * @param ente
	 * @param richiedente
	 * @return
	 * @throws RuntimeException
	 */
	public List<Errore> controlliAggiornamentoSediSecondarie(Integer idSoggetto,
			List<SedeSecondariaSoggetto> sedi, Ente ente, Richiedente richiedente,DatiOperazioneDto datiOperazioneDto)
			throws RuntimeException
	{
		Integer idEnte = ente.getUid();

		List<Errore> listaErrori = new ArrayList<Errore>();

		List<SedeSecondariaSoggetto> listaSediDb = this.ricercaSediSecondarie(idEnte, idSoggetto,
				true, true,datiOperazioneDto);
		SediSecondarieInModificaInfoDto infoModifiche = valutaSediSecondarie(sedi, idSoggetto);

		ArrayList<SedeSecondariaSoggetto> listaDaAggiornare = infoModifiche.getListaDaAggiornare();
		ArrayList<SedeSecondariaSoggetto> listaDaInserire = infoModifiche.getListaDaInserire();
		ArrayList<SiacRSoggettoRelazFin> listaDaEliminare = infoModifiche.getListaDaEliminare();

		if (listaDaAggiornare != null && listaDaAggiornare.size() > 0 && listaSediDb != null
				&& listaSediDb.size() > 0)
		{
			for (SedeSecondariaSoggetto sedeSecondariaSoggettoAgg : listaDaAggiornare)
			{
				for (SedeSecondariaSoggetto sedeSecondariaDb : listaSediDb)
				{
					if (sedeSecondariaSoggettoAgg.getDenominazione().equalsIgnoreCase(
							sedeSecondariaDb.getDenominazione())
							&& sedeSecondariaSoggettoAgg.getUid() != sedeSecondariaDb.getUid())
					{
						listaErrori.add(ErroreFin.ENTITA_GIA_PRESENTE.getErrore("Sede secondaria",
								sedeSecondariaSoggettoAgg.getDenominazione()));
					}
				}
			}

			if (listaErrori.size() == 0)
			{
				// Controlli su annullamento sede secondaria
				for (SedeSecondariaSoggetto sedeSecondariaSoggettoAgg : listaDaAggiornare)
				{
					if (sedeSecondariaSoggettoAgg.getStatoOperativoSedeSecondaria().equals(
							StatoOperativoSedeSecondaria.ANNULLATO))
					{
						List<SiacTModpagFin> listaModPags = siacTModpagRepository
								.findValidiByIdSoggetto(
										Integer.valueOf(sedeSecondariaSoggettoAgg.getUid()),
										getNow());
						if (listaModPags != null && listaModPags.size() > 0)
						{
							for (SiacTModpagFin siacTModpag : listaModPags)
							{
								listaErrori = controlliCancellazioneMdp(siacTModpag, idEnte);
							}
						}
					}
				}
			}
		}

		if (listaDaInserire != null && listaDaInserire.size() > 0 && listaSediDb != null
				&& listaSediDb.size() > 0)
		{
			for (SedeSecondariaSoggetto sedeSecondariaSoggettoIns : listaDaInserire)
			{
				for (SedeSecondariaSoggetto sedeSecondariaDb : listaSediDb)
				{
					if (sedeSecondariaSoggettoIns.getDenominazione().equalsIgnoreCase(
							sedeSecondariaDb.getDenominazione()))
					{
						listaErrori.add(ErroreFin.ENTITA_GIA_PRESENTE.getErrore("Sede secondaria",
								sedeSecondariaSoggettoIns.getDenominazione()));
					}
				}
			}
		}

		// Controlli su cancellazione sede
		if (listaDaEliminare != null && listaDaEliminare.size() > 0)
		{
			for (SiacRSoggettoRelazFin it : listaDaEliminare)
			{
				SiacTSoggettoFin siacTSoggettoSede = it.getSiacTSoggetto2();

				// LIQUIDAZIONI
				listaErrori = subControlloEsistenzaLiquidazioni(siacTSoggettoSede, listaErrori,
						Operazione.CANCELLAZIONE_LOGICA_RECORD, true);

				// ORDINATIVI
				listaErrori = subControlloEsistenzaOrdinativi(siacTSoggettoSede, listaErrori,
						Operazione.CANCELLAZIONE_LOGICA_RECORD, true);
			}
		}

		// Termino restituendo l'oggetto di ritorno:
		return listaErrori;
	}

	/**
	 * controlliMdp
	 * 
	 * @param mdpList
	 * @param sediList
	 * @param ente
	 * @param richiedente
	 * @param idSiacTSoggettoInModifica
	 * @return
	 */
	public EsitoControlliAggiornamentoMDPDto controlliMdp(List<ModalitaPagamentoSoggetto> mdpList,
			List<SedeSecondariaSoggetto> sediList, Ente ente, Richiedente richiedente,
			Integer idSiacTSoggettoInModifica)
	{

		EsitoControlliAggiornamentoMDPDto esito = new EsitoControlliAggiornamentoMDPDto();
		List<Errore> listaErrori = new ArrayList<Errore>();

		int idEnte = ente.getUid();
		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository
				.findOne(idEnte);

		if (mdpList != null && mdpList.size() > 0)
		{

			Operazione operazioneInsert = Operazione.INSERIMENTO;
			long currMillisec = System.currentTimeMillis();
			DatiOperazioneDto datiOperazioneInserisci = new DatiOperazioneDto(currMillisec,
					operazioneInsert, siacTEnteProprietario, richiedente.getAccount().getId());

			ModalitaPagamentoInModificaInfoDto info = valutaModPagDaModificare(mdpList,
					idSiacTSoggettoInModifica, datiOperazioneInserisci, idEnte, sediList);
			ArrayList<ModalitaPagamentoSoggetto> modalitaDaModificare = info
					.getModalitaDaModificare();
			ArrayList<SiacTModpagFin> modalitaDaEliminare = info.getModalitaDaEliminare();

			// Controllo aggiornamento mdp
			if (modalitaDaModificare != null && modalitaDaModificare.size() > 0)
			{
				for (ModalitaPagamentoSoggetto modPagToUpdate : modalitaDaModificare)
				{
					EsitoControlliAggiornamentoMDPDto esitoControlli = controlliAggiornamentoMdp(
							modPagToUpdate, idEnte);
					esito = esitoControlli;

					if (esitoControlli != null && esitoControlli.getListaErrori() != null
							&& esitoControlli.getListaErrori().size() > 0)
					{
						return esitoControlli;
					}
				}
			}

			// Controllo cancellazione mdp
			if (modalitaDaEliminare != null && modalitaDaEliminare.size() > 0)
			{
				for (SiacTModpagFin modPagToDelete : modalitaDaEliminare)
				{
					listaErrori = controlliCancellazioneMdp(modPagToDelete, idEnte);
				}
				if (listaErrori != null && listaErrori.size() > 0)
				{
					esito.setListaErrori(listaErrori);
					esito.setModalitaPagamentoDaAnnullare(null);

					return esito;
				}
			}

		}
		else
		{
			// Controllo cancellazione - caso in cui sia presente una sola mdp

//			List<SiacTModpagModFin> modpagList = siacTModpagModRepository.findValidoBySoggettoId(
//					idSiacTSoggettoInModifica, getNow());
//
//			if (modpagList != null && modpagList.size() > 0)
//			{
//				if (modpagList != null && modpagList.size() > 0)
//				{
//					for (SiacTModpagModFin modpagModToDelete : modpagList)
//					{
//						listaErrori = controlliCancellazioneMdpMod(modpagModToDelete, idEnte);
//						esito.setListaErrori(listaErrori);
//					}
//				}
//
//			}

			
			List<SiacTModpagFin> modpagList = siacTModpagRepository.findValidiByIdSoggetto(idSiacTSoggettoInModifica,
					getNow());
			if (modpagList != null && modpagList.size() > 0) {
				for (SiacTModpagFin modpagToDelete : modpagList) {
					listaErrori = controlliCancellazioneMdp(modpagToDelete, idEnte);
					esito.setListaErrori(listaErrori);
				}
			}
			 
		}

		return esito;

	}

	/**
	 * controlliAggiornamentoMdp
	 * 
	 * @param mdpToUpdate
	 * @param idEnte
	 * @return
	 */
	private EsitoControlliAggiornamentoMDPDto controlliAggiornamentoMdp(
			ModalitaPagamentoSoggetto mdpToUpdate, Integer idEnte)
	{

		EsitoControlliAggiornamentoMDPDto esito = new EsitoControlliAggiornamentoMDPDto();
		List<Errore> listaErrori = new ArrayList<Errore>();

		// Verifico se si trata di aggiornamento o annullamento
		List<SiacRModpagStatoFin> statoValidoByMdpId = siacRModpagStatoRepository
				.findStatoValidoByMdpId(mdpToUpdate.getUid());
		
		if (statoValidoByMdpId.isEmpty())
			return esito;
		
		SiacRModpagStatoFin siacRModpagStatoToCheck = statoValidoByMdpId.get(0);
		
		if (siacRModpagStatoToCheck.getSiacDModpagStato().getModpagStatoDesc()
				.equalsIgnoreCase(mdpToUpdate.getDescrizioneStatoModalitaPagamento()))
		{
			// Aggiornamento mdp

			// controllo che la modalita' non sia ancora stata collegata ad un
			// ORDINATIVO in stato EMESSO
			List<SiacROrdinativoModpagFin> siacROrdinativoModpagList;
			siacROrdinativoModpagList = siacROrdinativoModpagRepository.findValidoByIdMdp(idEnte,
					getNow(), mdpToUpdate.getUid());

			if (siacROrdinativoModpagList != null && siacROrdinativoModpagList.size() > 0)
			{
				for (SiacROrdinativoModpagFin siacROrdinativoModpag : siacROrdinativoModpagList)
				{

					List<SiacROrdinativoStatoFin> siacROrdinativoStatoList;
					siacROrdinativoStatoList = siacROrdinativoModpag.getSiacTOrdinativo()
							.getSiacROrdinativoStatos();
					siacROrdinativoStatoList = DatiOperazioneUtils.soloValidi(
							siacROrdinativoStatoList, getNow());

					if (siacROrdinativoStatoList != null && siacROrdinativoStatoList.size() > 0)
					{
						for (SiacROrdinativoStatoFin siacROrdinativoStato : siacROrdinativoStatoList)
						{
							if (siacROrdinativoStato.getSiacDOrdinativoStato().getOrdStatoCode()
									.compareToIgnoreCase(Constanti.D_ORDINATIVO_STATO_INSERITO) == 0)
							{
								listaErrori.add(ErroreFin.ENTITA_NON_AGGIORNABILE.getErrore(
										"Modalita' di pagamento", "ordinativo emesso"));
							}
						}
					}
				}
			}

		}
		else if (mdpToUpdate.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase(
				Constanti.STATO_ANNULLATO))
		{
			esito.setModalitaPagamentoDaAnnullare(mdpToUpdate);
			// Annullamento mdp

			// controllo che la mdp non sia legata alla sede
//			SiacTModpagFin mdpDaAnnullare = siacTModpagRepository.findOne(mdpToUpdate.getUid());
//			if (isSedeSecondaria(mdpDaAnnullare.getSiacTSoggetto().getSoggettoId(), idEnte))
//			{
//				listaErrori.add(ErroreFin.ESISTONO_MOVIMENTI_COLLEGATI.getErrore("ANNULLAMENTO",
//						"(modalita' legata a sede secondaria)"));
//			}

			// controllo che le liquidazioni non siano legate alla modalita'
			// senza Ordinativo o con importo Ordinativo inferiore a quello
			// della liquidazione
			// estraggo le siac_t_liq e calcolo la somma delle liquidazioni
			// legate alla mdp
			List<SiacTLiquidazioneFin> siacTLiquidazioneList;
			siacTLiquidazioneList = siacTLiquidazioneRepository
					.findLiquidazioneValidaByEnteAndMdpId(idEnte, mdpToUpdate.getUid(), getNow());
			if (siacTLiquidazioneList != null && siacTLiquidazioneList.size() > 0)
			{
				BigDecimal sommaImportiLiquidazioni = BigDecimal.ZERO;
				BigDecimal sommaImportiOrdinativi = BigDecimal.ZERO;
				for (SiacTLiquidazioneFin siacTLiquidazione : siacTLiquidazioneList)
				{

					List<SiacRLiquidazioneStatoFin> siacRLiquidazioneStatoList;
					siacRLiquidazioneStatoList = siacTLiquidazione.getSiacRLiquidazioneStatos();

					if (siacRLiquidazioneStatoList != null && siacRLiquidazioneStatoList.size() > 0)
					{
						siacRLiquidazioneStatoList = DatiOperazioneUtils.soloValidi(
								siacRLiquidazioneStatoList, getNow());

						for (SiacRLiquidazioneStatoFin siacRLiquidazioneStato : siacRLiquidazioneStatoList)
						{
							if (!siacRLiquidazioneStato.getSiacDLiquidazioneStato()
									.getLiqStatoCode()
									.equalsIgnoreCase(Constanti.LIQUIDAZIONE_STATO_ANNULLATO))
							{

								sommaImportiLiquidazioni = sommaImportiLiquidazioni
										.add(siacTLiquidazione.getLiqImporto());

								// estraggo le siac_r_liq_ord e calcolo la somma
								// degli ordinativi legati alla liquidazione
								List<SiacRLiquidazioneOrdFin> siacRLiquidazioneOrdList;
								siacRLiquidazioneOrdList = siacTLiquidazione
										.getSiacRLiquidazioneOrds();
								siacRLiquidazioneOrdList = DatiOperazioneUtils.soloValidi(
										siacRLiquidazioneOrdList, getNow());

								if (siacRLiquidazioneOrdList != null
										&& siacRLiquidazioneOrdList.size() > 0)
								{
									for (SiacRLiquidazioneOrdFin siacRLiquidazioneOrd : siacRLiquidazioneOrdList)
									{

										List<SiacTOrdinativoTsDetFin> siacTOrdinativoTsDetList;
										siacTOrdinativoTsDetList = siacRLiquidazioneOrd
												.getSiacTOrdinativoT().getSiacTOrdinativoTsDets();
										siacTOrdinativoTsDetList = DatiOperazioneUtils.soloValidi(
												siacTOrdinativoTsDetList, getNow());

										if (siacTOrdinativoTsDetList != null
												&& siacTOrdinativoTsDetList.size() > 0)
										{
											for (SiacTOrdinativoTsDetFin siacTOrdinativoTsDet : siacTOrdinativoTsDetList)
											{
												if (siacTOrdinativoTsDet
														.getSiacDOrdinativoTsDetTipo()
														.getOrdTsDetTipoCode()
														.equalsIgnoreCase(
																Constanti.D_ORDINATIVO_TS_DET_TIPO_IMPORTO_ATTUALE))
												{
													sommaImportiOrdinativi = sommaImportiOrdinativi
															.add(siacTOrdinativoTsDet
																	.getOrdTsDetImporto());
												}
											}
										}
									}
								}
							}
						}
					}
				}

				// verifica corrispondenza importo sommaLiq con SommaOrd
				if (sommaImportiOrdinativi.compareTo(sommaImportiLiquidazioni) != 0)
				{
					listaErrori
							.add(ErroreFin.ESISTONO_MOVIMENTI_COLLEGATI
									.getErrore(
											"ANNULLAMENTO",
											"(modalita' legata a liquidazioni senza mandato o con mandato con importo inferiore alla liquidazione)"));
				}
			}

			// controllo che la modalita' non sia ancora stata collegata ad un
			// ORDINATIVO in stato QUIETANZATO
			List<SiacROrdinativoModpagFin> siacROrdinativoModpagList;
			siacROrdinativoModpagList = siacROrdinativoModpagRepository.findValidoByIdMdp(idEnte,
					getNow(), mdpToUpdate.getUid());

			if (siacROrdinativoModpagList != null && siacROrdinativoModpagList.size() > 0)
			{
				for (SiacROrdinativoModpagFin siacROrdinativoModpag : siacROrdinativoModpagList)
				{

					List<SiacROrdinativoStatoFin> siacROrdinativoStatoList;
					siacROrdinativoStatoList = siacROrdinativoModpag.getSiacTOrdinativo()
							.getSiacROrdinativoStatos();

					if (siacROrdinativoStatoList != null && siacROrdinativoStatoList.size() > 0)
					{
						siacROrdinativoStatoList = DatiOperazioneUtils.soloValidi(
								siacROrdinativoStatoList, getNow());

						if (siacROrdinativoStatoList != null && siacROrdinativoStatoList.size() > 0)
						{
							for (SiacROrdinativoStatoFin siacROrdinativoStato : siacROrdinativoStatoList)
							{
								if (siacROrdinativoStato
										.getSiacDOrdinativoStato()
										.getOrdStatoCode()
										.compareToIgnoreCase(
												Constanti.D_ORDINATIVO_STATO_QUIETANZATO) == 0)
								{
									listaErrori.add(ErroreFin.ESISTONO_MOVIMENTI_COLLEGATI
											.getErrore("ANNULLAMENTO", "(ordinativi QUIETANZATI)"));
								}
							}
						}
					}
				}
			}

			// controllo che la mdp non sia legata a mdp di tipo cessioni
			List<SiacRSoggrelModpagFin> siacRSoggrelModpagList;
			siacRSoggrelModpagList = siacRSoggrelModpagRepository.findCessioniByIdModPag(
					mdpToUpdate.getUid(), getNow(), idEnte);
			if (siacRSoggrelModpagList != null && siacRSoggrelModpagList.size() > 0)
			{
				listaErrori.add(ErroreFin.ESISTONO_MOVIMENTI_COLLEGATI.getErrore("ANNULLAMENTO",
						"(cessioni di pagamento)"));
			}
		}

		esito.setListaErrori(listaErrori);

		return esito;

	}
	

	private List<Errore> controlliCancellazioneMdpOperazioneInterna(Integer idMdp, Integer idEnte)
	{
		List<Errore> listaErrori = new ArrayList<Errore>();

		// controllo liquidazioni collegate
		if (checkLiquidazioniCollegate(siacTLiquidazioneRepository.findLiquidazioneValidaByEnteAndMdpId(
				idEnte, idMdp, getNow()))) {
			listaErrori.add(ErroreFin.CANCELLAZIONE_SOGGETTO_IMPOSSIBILE.getErrore(
					"Modalita' di pagamento", "liquidazioni"));
		}
		

		// controllo ordinativi collegati (incasso - pagamento)
		if (checkOrdinativiCollegati(siacROrdinativoModpagRepository.findValidoByIdMdp(idEnte,
				getNow(), idMdp))) {
			listaErrori.add(ErroreFin.CANCELLAZIONE_SOGGETTO_IMPOSSIBILE.getErrore(
					"Modalita' di pagamento", "ordinativi"));
		}
		
		// controllo mdp cessioni
		List<SiacRSoggrelModpagFin> siacRSoggrelModpagList = siacRSoggrelModpagRepository.findCessioniByIdModPag(idMdp,
				getNow(), idEnte);
		
		if (siacRSoggrelModpagList != null && siacRSoggrelModpagList.size() > 0)
		{
			listaErrori.add(ErroreFin.CANCELLAZIONE_SOGGETTO_IMPOSSIBILE.getErrore(
					"Modalita' di pagamento", "cessioni di pagamento"));
		}

		// Controllo sui documenti collegati
		List<SiacRSubdocModpagFin> listaRSubdocModpag = siacRSubdocModpagRepository
				.findSiacRSubdocModpagByIdModPag(idMdp, getNow(), idEnte);
		
		if (listaRSubdocModpag != null && listaRSubdocModpag.size() > 0)
		{
			// Controllare se il doc e' valido?
			listaErrori.add(ErroreFin.CANCELLAZIONE_SOGGETTO_IMPOSSIBILE.getErrore(
					"Modalita' di pagamento", "documenti"));
		}

		return listaErrori;
	}

	private boolean checkLiquidazioniCollegate(List<SiacTLiquidazioneFin> siacTLiquidazioneList) {
		if (siacTLiquidazioneList != null && siacTLiquidazioneList.size() > 0)
		{
			for (SiacTLiquidazioneFin siacTLiquidazione : siacTLiquidazioneList)
			{
				List<SiacRLiquidazioneStatoFin> siacRLiquidazioneStatoList;
				siacRLiquidazioneStatoList = siacTLiquidazione.getSiacRLiquidazioneStatos();

				if (siacRLiquidazioneStatoList != null && siacRLiquidazioneStatoList.size() > 0)
				{
					siacRLiquidazioneStatoList = DatiOperazioneUtils.soloValidi(
							siacRLiquidazioneStatoList, getNow());

					for (SiacRLiquidazioneStatoFin siacRLiquidazioneStato : siacRLiquidazioneStatoList)
					{
						if (!siacRLiquidazioneStato.getSiacDLiquidazioneStato().getLiqStatoCode()
								.equalsIgnoreCase(Constanti.LIQUIDAZIONE_STATO_ANNULLATO))
						{
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	private boolean checkOrdinativiCollegati(List<SiacROrdinativoModpagFin> siacROrdinativoModpagList) {

		if (siacROrdinativoModpagList != null && siacROrdinativoModpagList.size() > 0)
		{
			for (SiacROrdinativoModpagFin siacROrdinativoModpag : siacROrdinativoModpagList)
			{

				List<SiacROrdinativoStatoFin> SiacROrdinativoStatoList;
				SiacROrdinativoStatoList = siacROrdinativoModpag.getSiacTOrdinativo()
						.getSiacROrdinativoStatos();

				if (SiacROrdinativoStatoList != null && SiacROrdinativoStatoList.size() > 0)
				{
					SiacROrdinativoStatoList = DatiOperazioneUtils.soloValidi(
							SiacROrdinativoStatoList, getNow());

					for (SiacROrdinativoStatoFin siacROrdinativoStato : SiacROrdinativoStatoList)
					{
						if (!siacROrdinativoStato.getSiacDOrdinativoStato().getOrdStatoCode()
								.equalsIgnoreCase(Constanti.D_ORDINATIVO_STATO_ANNULLATO))
						{
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	/**
	 * controlliCancellazioneMdp
	 * 
	 * @param mdpToDelete
	 * @param idEnte
	 * @return
	 */
	private List<Errore> controlliCancellazioneMdp(SiacTModpagFin mdpToDelete, Integer idEnte)
	{

		// utilizzata la funzione operazione interna dove cambiano solo gli id
		// presi opportunamente dalla tavole corrette
		return controlliCancellazioneMdpOperazioneInterna(mdpToDelete.getUid(), idEnte);

	}

	private List<Errore> controlliCancellazioneMdpMod(SiacTModpagModFin mdpToDelete, Integer idEnte)
	{

		// utilizzata la funzione operazione interna dove cambiano solo gli id
		// presi opportunamente dalla tavole corrette
		return controlliCancellazioneMdpOperazioneInterna(mdpToDelete.getUid(), idEnte);

	}

	/**
	 * valutaSediSecondarie per capire chi e' stato inserito, modificato,
	 * eliminato o invariato
	 * 
	 * @param sedi
	 * @param idSoggetto
	 * @return
	 */
	private SediSecondarieInModificaInfoDto valutaSediSecondarie(List<SedeSecondariaSoggetto> sedi,
			Integer idSoggetto)
	{
		// MODIFICA FATTA FE
		// Inserito nel dto SedeSecondariaSoggetto il campo boolean "modificato"
		// che e'
		// TRUE, se la sede e' stata modificata e successivamente salvata
		// FALSE, nel resto dei casi
		// Valutare se conviene utilizzare questa strada piuttosto che i singoli
		// controlli su tutti i campi
		SediSecondarieInModificaInfoDto sediSecondarieInModificaInfoDto = new SediSecondarieInModificaInfoDto();
		long currMillisec = System.currentTimeMillis();
		Date dateInserimento = new Date(currMillisec);
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);
		List<SiacRSoggettoRelazFin> relazioni = siacRSoggettoRelazRepository.findBySoggettoETipo(
				idSoggetto, Constanti.SEDE_SECONDARIA, timestampInserimento);
		//
		if (sedi == null)
		{
			sedi = new ArrayList<SedeSecondariaSoggetto>(0);
		}
		//

		//
		ArrayList<SiacRSoggettoRelazFin> listaInvariati = new ArrayList<SiacRSoggettoRelazFin>();
		ArrayList<SedeSecondariaSoggetto> listaDaAggiornare = new ArrayList<SedeSecondariaSoggetto>();
		ArrayList<SedeSecondariaSoggetto> listaDaInserire = new ArrayList<SedeSecondariaSoggetto>();
		ArrayList<SiacRSoggettoRelazFin> listaDaEliminare = new ArrayList<SiacRSoggettoRelazFin>();
		// /

		ArrayList<Integer> listaIdOld = new ArrayList<Integer>();
		ArrayList<Integer> listaIdNew = new ArrayList<Integer>();
		for (SedeSecondariaSoggetto sss : sedi)
		{
			if (sss.getUid() > 0)
			{
				listaIdNew.add(sss.getUid());
			}
			else
			{
				listaDaInserire.add(sss);
			}
		}

		if (relazioni != null && relazioni.size() > 0)
		{
			for (SiacRSoggettoRelazFin relazione : relazioni)
			{

				Integer idOld = relazione.getSiacTSoggetto2().getSoggettoId();

				listaIdOld.add(idOld);
				for (SedeSecondariaSoggetto sss : sedi)
				{
					if (sss.getUid() == idOld)
					{
						boolean sedeModificata = false;
						List<SiacTSoggettoModFin> soggmods = siacTSoggettoModRepository
								.findValidoBySoggettoId(idOld, timestampInserimento);
						if (soggmods != null && soggmods.size() > 0)
						{
							// siamo nel caso in cui la sede e' in modifica
							SiacTSoggettoModFin sedeMod = soggmods.get(0);
							ContattiModInModificaInfoDto cinm = valutaContattiDaModificareMod(
									sss.getContatti(), sedeMod.getSogModId());
							if (!cinm.isRimastiUguali())
							{
								// almeno un contatto e' stato modificato
								sedeModificata = true;
							}
							if (!sedeModificata)
							{
								sedeModificata = isIndirizzoSedeModModificato(sss, sedeMod);
							}
						}
						else
						{
							// la sede non e' in modifica
							ContattiInModificaInfoDto cinm = valutaContattiDaModificare(
									sss.getContatti(), idOld);
							if (!cinm.isRimastiUguali())
							{
								// almeno un contatto e' stato modificato
								sedeModificata = true;
							}
							if (!sedeModificata)
							{
								sedeModificata = isIndirizzoSedeModificato(sss, idOld);
							}
						}
						if (sedeModificata)
						{
							listaDaAggiornare.add(sss);
						}
						else
						{
							listaInvariati.add(relazione);
						}
					}
				}
				if (!listaIdNew.contains(idOld))
				{
					listaDaEliminare.add(relazione);
				}
			}
		}
		sediSecondarieInModificaInfoDto.setListaInvariati(listaInvariati);
		sediSecondarieInModificaInfoDto.setListaDaAggiornare(listaDaAggiornare);
		sediSecondarieInModificaInfoDto.setListaDaEliminare(listaDaEliminare);
		sediSecondarieInModificaInfoDto.setListaDaInserire(listaDaInserire);

		// Termino restituendo l'oggetto di ritorno:
		return sediSecondarieInModificaInfoDto;
	}

	/**
	 * isIndirizzoSedeModModificato valuta se l'indirizzo e' stato modificato
	 * 
	 * @param sedeSecondariaSoggetto
	 * @param siacTSoggettoMod
	 * @return
	 */
	private boolean isIndirizzoSedeModModificato(SedeSecondariaSoggetto sedeSecondariaSoggetto,
			SiacTSoggettoModFin siacTSoggettoMod)
	{
		boolean modificato = false;
		long currMillisec = System.currentTimeMillis();
		Date dateInserimento = new Date(currMillisec);
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);

		IndirizzoSoggetto indsogg = sedeSecondariaSoggetto.getIndirizzoSoggettoPrincipale();

		List<SiacTIndirizzoSoggettoModFin> indirSoggs = siacTIndirizzoSoggettoModRepository
				.findValidoBySoggModId(siacTSoggettoMod.getSogModId(), timestampInserimento);
		SiacTIndirizzoSoggettoModFin indirizzoDb = indirSoggs.get(0);

		if (!StringUtils.sonoUguali(sedeSecondariaSoggetto.getDenominazione(),
				siacTSoggettoMod.getSoggettoDesc()))
		{
			return true;
		}
		if (!StringUtils.sonoUguali(indsogg.getDenominazione(), indirizzoDb.getToponimo()))
		{
			return true;
		}
		if (!StringUtils.sonoUguali(indsogg.getCap(), indirizzoDb.getZipCode()))
		{
			return true;
		}
		
		//COMUNE
		if(isComuneModificato(indirizzoDb.getSiacTComune(), indsogg)){
			return true;
		}
		
		//SEDIME:
		if(isSedimeModificato(indirizzoDb, indsogg)){
			return true;
		}
		
		if (!StringUtils.sonoUguali(indsogg.getNumeroCivico(), indirizzoDb.getNumeroCivico()))
		{
			return true;
		}
		return modificato;
	}

	/**
	 * isIndirizzoSedeModificato valuta se l'indirizzo e' stato modificato
	 * 
	 * @param sedeSecondariaSoggetto
	 * @param idSede
	 * @return
	 */
	private boolean isIndirizzoSedeModificato(SedeSecondariaSoggetto sedeSecondariaSoggetto,
			Integer idSede)
	{
		boolean modificato = false;
		long currMillisec = System.currentTimeMillis();
		Date dateInserimento = new Date(currMillisec);
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);

		SiacTSoggettoFin siacTSoggetto = siacTSoggettoRepository.findOne(idSede);

		IndirizzoSoggetto indsogg = sedeSecondariaSoggetto.getIndirizzoSoggettoPrincipale();

		List<SiacTIndirizzoSoggettoFin> indirSoggs = siacTIndirizzoSoggettoRepository
				.findValidiByIdSoggetto(idSede, timestampInserimento);
		SiacTIndirizzoSoggettoFin indirizzoDb = indirSoggs.get(0);

		if (!StringUtils.sonoUguali(sedeSecondariaSoggetto.getDenominazione(),
				siacTSoggetto.getSoggettoDesc()))
		{
			return true;
		}
		if (!StringUtils.sonoUguali(indsogg.getDenominazione(), indirizzoDb.getToponimo()))
		{
			return true;
		}
		if (!StringUtils.sonoUguali(indsogg.getCap(), indirizzoDb.getZipCode()))
		{
			return true;
		}
		
		//COMUNE
		if(isComuneModificato(indirizzoDb.getSiacTComune(), indsogg)){
			return true;
		}
		
		//SEDIME:
		if(isSedimeModificato(indirizzoDb, indsogg)){
			return true;
		}
		
		//NUMERO CIVICO:
		if (!StringUtils.sonoUguali(indsogg.getNumeroCivico(), indirizzoDb.getNumeroCivico()))
		{
			return true;
		}

		if (indsogg.isCheckAvviso())
		{
			if (!indirizzoDb.getAvviso().equalsIgnoreCase(Constanti.TRUE))
			{
				return true;
			}
		}
		else if (!indsogg.isCheckAvviso())
		{
			if (indirizzoDb.getAvviso().equalsIgnoreCase(Constanti.TRUE))
			{
				return true;
			}
		}

		// Controllo sullo stato della sede
		if (siacTSoggetto.getSiacRSoggettoStatos() != null
				&& siacTSoggetto.getSiacRSoggettoStatos().size() > 0)
		{
			for (SiacRSoggettoStatoFin statoSoggetto : siacTSoggetto.getSiacRSoggettoStatos())
			{
				if (statoSoggetto.getDataFineValidita() == null
						&& statoSoggetto.getSiacDSoggettoStato() != null
						&& statoSoggetto.getSiacDSoggettoStato().getSoggettoStatoCode() != null)
				{
					// trovato valido con stesso codice
					if (!StringUtils.sonoUguali(statoSoggetto.getSiacDSoggettoStato()
							.getSoggettoStatoCode().toUpperCase(), sedeSecondariaSoggetto
							.getDescrizioneStatoOperativoSedeSecondaria().toUpperCase()))
					{
						return true;
					}
				}
			}
		}
		return modificato;
	}

	/**
	 * isIndirizzoDelSoggettoModificato valuta se l'indirizzo e' stato
	 * modificato
	 * 
	 * @param indsogg
	 * @param idSoggetto
	 * @return
	 */
	private boolean isIndirizzoDelSoggettoModificato(IndirizzoSoggetto indsogg, Integer idSoggetto)
	{
		boolean modificato = false;
		
		//RICARICO L'INDIRIZZO:
		SiacTIndirizzoSoggettoFin indirizzoDb = siacTIndirizzoSoggettoRepository.findOne(indsogg.getIndirizzoId());

		//TOPONIMO
		if (!StringUtils.sonoUguali(indsogg.getDenominazione(), indirizzoDb.getToponimo())){
			return true;
		}
		
		//CAP
		if (!StringUtils.sonoUguali(indsogg.getCap(), indirizzoDb.getZipCode())){
			return true;
		}
		
		//COMUNE
		if(isComuneModificato(indirizzoDb.getSiacTComune(), indsogg)){
			return true;
		}
		
		//SEDIME
		if(isSedimeModificato(indirizzoDb, indsogg)){
			return true;
		}
		
		//NUMERO CIVICO
		if (!StringUtils.sonoUguali(indsogg.getNumeroCivico(), indirizzoDb.getNumeroCivico())){
			return true;
		}
		
		//AVVISO
		if (!StringUtils.sonoUgualiBoolDb(indsogg.getAvviso(), indirizzoDb.getAvviso())){
			return true;
		}
		
		//PRINCIPALE
		if (!StringUtils.sonoUgualiBoolDb(indsogg.getPrincipale(), indirizzoDb.getPrincipale())){
			return true;
		}
		
		return modificato;
	}
	
	/**
	 * Verifica se il sedime indicato in IndirizzoSoggetto e' variato rispetto a quello 
	 * ancora persisente sul db rappresentato da indirizzoDb
	 * @param indirizzoDb
	 * @param indsogg
	 * @return
	 */
	private boolean isSedimeModificato(SiacTIndirizzoSoggettoFin indirizzoDb, IndirizzoSoggetto indsogg){
		String sedimeSulDb = null;
		if(indirizzoDb.getSiacDViaTipo()!=null){
			// SIAC-5175 gestiamo il caso in cui sedime sul db possa essere nullo
			// caso verificatesi per dati da migrazione
			sedimeSulDb = indirizzoDb.getSiacDViaTipo().getViaTipoDesc();
		}
		String dug = indsogg.getSedime();
		if (!StringUtils.isEmpty(dug)){
			dug = dug.trim().toUpperCase();
		}
		if (!StringUtils.sonoUguali(dug, sedimeSulDb)){
			return true;
		}
		return false;
	}
	
	/**
	 * Verifica se il sedime indicato in IndirizzoSoggetto e' variato rispetto a quello 
	 * ancora persisente sul db rappresentato da indirizzoDb
	 * @param indirizzoDb
	 * @param indsogg
	 * @return
	 */
	private boolean isSedimeModificato(SiacTIndirizzoSoggettoModFin indirizzoDb, IndirizzoSoggetto indsogg){
		String sedimeSulDb = null;
		if(indirizzoDb.getSiacDViaTipo()!=null){
			// SIAC-5175 gestiamo il caso in cui sedime sul db possa essere nullo
			// caso verificatesi per dati da migrazione
			sedimeSulDb = indirizzoDb.getSiacDViaTipo().getViaTipoDesc();
		}
		if (!StringUtils.sonoUguali(indsogg.getSedime(), sedimeSulDb)){
			return true;
		}
		return false;
	}
	
	/**
	 * Verifica se il comune indicato in IndirizzoSoggetto e' variato rispetto a quello 
	 * ancora persisente sul db rappresentato da comuneSulDb
	 * @param comuneSulDb
	 * @param indsogg
	 * @return
	 */
	private boolean isComuneModificato(SiacTComuneFin comuneSulDb,IndirizzoSoggetto indsogg){
		boolean modificato = false;
		//COMUNE
		if (!StringUtils.sonoUguali(StringUtils.formatCodIstaForQuery(indsogg.getIdComune()),comuneSulDb.getComuneIstatCode())){
			return true;
		}
		return modificato;
	}

	/**
	 * isIndirizzoDelSoggettoModModificato valuta se l'indirizzo e' stato
	 * modificato
	 * 
	 * @param indsogg
	 * @param idSoggettoMod
	 * @return
	 */
	private boolean isIndirizzoDelSoggettoModModificato(IndirizzoSoggetto indsogg,
			Integer idSoggettoMod)
	{
		boolean modificato = false;

		SiacTIndirizzoSoggettoModFin indirizzoDb = siacTIndirizzoSoggettoModRepository.findOne(indsogg
				.getIndirizzoId());

		if (indirizzoDb != null)
		{
			if (!StringUtils.sonoUguali(indsogg.getDenominazione(), indirizzoDb.getToponimo()))
			{
				return true;
			}
			if (!StringUtils.sonoUguali(indsogg.getCap(), indirizzoDb.getZipCode()))
			{
				return true;
			}
			
			//COMUNE
			if(isComuneModificato(indirizzoDb.getSiacTComune(), indsogg)){
				return true;
			}
			
			//SEDIME:
			if(isSedimeModificato(indirizzoDb, indsogg)){
				return true;
			}
			
			if (!StringUtils.sonoUguali(indsogg.getNumeroCivico(), indirizzoDb.getNumeroCivico()))
			{
				return true;
			}
			if (!StringUtils.sonoUguali(indsogg.getAvviso(), indirizzoDb.getAvviso()))
			{
				return true;
			}
			if (!StringUtils.sonoUguali(indsogg.getPrincipale(), indirizzoDb.getPrincipale()))
			{
				return true;
			}

			List<SiacRIndirizzoSoggettoTipoModFin> listaSiacRIndirizzoSoggettoTipoMod = indirizzoDb
					.getSiacRIndirizzoSoggettoTipoMods();
			if (null != listaSiacRIndirizzoSoggettoTipoMod
					&& listaSiacRIndirizzoSoggettoTipoMod.size() > 0)
			{
				for (SiacRIndirizzoSoggettoTipoModFin rIndirizzoSoggettoTipoMod : listaSiacRIndirizzoSoggettoTipoMod)
				{
					if (rIndirizzoSoggettoTipoMod != null
							&& rIndirizzoSoggettoTipoMod.getDataFineValidita() == null)
					{
						// trovato valido
						if (!StringUtils.sonoUguali(indsogg.getIdTipoIndirizzo(),
								rIndirizzoSoggettoTipoMod.getSiacDIndirizzoTipo()
										.getIndirizzoTipoCode()))
						{
							return true;
						}
					}
				}
			}

		}

		return modificato;
	}

	/**
	 * gestisciSediSecondarie serve per capire quali sedi siano state inserite,
	 * aggiornate o modificate
	 * 
	 * @param soggettoDopoModifica
	 * @param richiedente
	 * @param datiOperazioneDto
	 * @param isDecentrato
	 * @param sedi
	 * @param codiceSoggetto
	 * @return
	 */
	private ArrayList<SiacRSoggettoRelazFin> gestisciSediSecondarie(
			SiacTSoggettoFin soggettoDopoModifica, Richiedente richiedente,
			DatiOperazioneDto datiOperazioneDto, boolean isDecentrato,
			List<SedeSecondariaSoggetto> sedi, String codiceSoggetto)
	{
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		Integer isSoggettoPrincipale = soggettoDopoModifica.getSoggettoId();

		// 08/05/2014 : Commento la chiamata al metodo isDecentrato(..) perche'
		// non valutava in maniera corretta
		// se l'utente era master o decentrato
		String loginOperazione = DatiOperazioneUtils.determinaUtenteLogin(datiOperazioneDto,
				siacTAccountRepository);

		SediSecondarieInModificaInfoDto infoModifiche = valutaSediSecondarie(sedi,
				isSoggettoPrincipale);

		ArrayList<SedeSecondariaSoggetto> listaDaAggiornare = infoModifiche.getListaDaAggiornare();
		ArrayList<SedeSecondariaSoggetto> listaDaInserire = infoModifiche.getListaDaInserire();
		ArrayList<SiacRSoggettoRelazFin> listaDaEliminare = infoModifiche.getListaDaEliminare();
		ArrayList<SiacRSoggettoRelazFin> listaInvariati = infoModifiche.getListaInvariati();

		List<SiacRSoggettoRelazFin> listarelaz = soggettoDopoModifica.getSiacRSoggettoRelazs1();

		ArrayList<SiacRSoggettoRelazFin> listarelazRicostruita = new ArrayList<SiacRSoggettoRelazFin>();
		listarelazRicostruita.addAll(listaInvariati);

		if (listaDaEliminare != null)
		{
			for (SiacRSoggettoRelazFin it : listaDaEliminare)
			{
				String statoTrovato = "";
				for (SiacRSoggettoStatoFin itStato : it.getSiacTSoggetto2().getSiacRSoggettoStatos())
				{
					if (itStato != null && itStato.getDataFineValidita() == null)
					{
						// trovato valido
						statoTrovato = itStato.getSiacDSoggettoStato().getSoggettoStatoCode();

					}
				}

				if (isDecentrato
						&& !statoTrovato.equals(StatoOperativoAnagrafica.PROVVISORIO.name()))
				{
					cancellaSedeDaTSoggettoMod(idEnte, it.getSiacTSoggetto2(), loginOperazione,
							richiedente);
				}
				else
				{
					cancellaSedeDaTSoggetto(idEnte, it.getSiacTSoggetto2(), loginOperazione,
							richiedente);
					siacRSoggettoRelazRepository.delete(it);
					siacRSoggettoRelazRepository.flush();
				}

			}
		}
		if (listaDaInserire != null)
		{
			for (SedeSecondariaSoggetto sede : listaDaInserire)
			{
				SiacRSoggettoRelazFin sedeIns = inserisciSedeSecondaria(sede, datiOperazioneDto,
						idEnte, soggettoDopoModifica, richiedente, isDecentrato);
				entityRefresh(sedeIns);
				listarelazRicostruita.add(sedeIns);
			}
		}
		if (listaDaAggiornare != null)
		{
			for (SedeSecondariaSoggetto sede : listaDaAggiornare)
			{
				// boolean isDecentrato =
				// isDecentrato(req.getRichiedente().getAccount());

				sede.setCodiceSedeSecondaria(codiceSoggetto);
				if (isDecentrato
						&& !sede.getStatoOperativoSedeSecondaria().equals(
								StatoOperativoSedeSecondaria.PROVVISORIO))
				{
					SiacTSoggettoModFin sedeModAgg = aggiornaSedeSecondariaMod(sede,
							datiOperazioneDto, idEnte, loginOperazione, richiedente);
					entityRefresh(sedeModAgg);
					SiacRSoggettoRelazFin finded = findBySede(listarelaz, sede.getUid());
					listarelazRicostruita.add(finded);
				}
				else
				{
					SiacTSoggettoFin sedeAgg = aggiornaSedeSecondaria(sede, datiOperazioneDto, idEnte,
							loginOperazione);
					entityRefresh(sedeAgg);
					SiacRSoggettoRelazFin finded = findBySede(listarelaz, sedeAgg.getSoggettoId());
					finded.setSiacTSoggetto2(sedeAgg);
					listarelazRicostruita.add(finded);
				}
			}
		}

		soggettoDopoModifica.setSiacRSoggettoRelazs1(listarelazRicostruita);
		entityRefresh(soggettoDopoModifica);
		// Termino restituendo l'oggetto di ritorno:
		return listarelazRicostruita;
	}

	/**
	 * Cerca dentro la lista indicata l'elemento con l'id sede indicato
	 * 
	 * @param listarelaz
	 * @param idSede
	 * @return
	 */
	private SiacRSoggettoRelazFin findBySede(List<SiacRSoggettoRelazFin> listarelaz, long idSede)
	{
		SiacRSoggettoRelazFin trovato = null;
		for (SiacRSoggettoRelazFin it : listarelaz)
		{
			if (it.getSiacTSoggetto2().getUid().longValue() == idSede)
			{
				trovato = it;
			}
		}
		return trovato;
	}

	/**
	 * ritorna true in caso di esistennza di legame
	 * 
	 * @param soggettoCorrente
	 * @param soggettoPrecedente
	 * @param tipoRelazione
	 * @param ente
	 * @return
	 */
	public boolean verificaEsistenzaLegameTraSoggetti(Soggetto soggettoCorrente,
			Soggetto soggettoPrecedente, TipoRelazioneSoggetto tipoRelazione, String codiceAmbito,
			Ente ente)
	{
		boolean validitaLegame = false;
		int idEnte = ente.getUid();
		String tipoLegame = tipoRelazione.name();
		SiacTSoggettoFin siacTSoggettoCorrente = siacTSoggettoRepository.ricercaSoggettoNoSeSede(
				codiceAmbito, idEnte, soggettoCorrente.getCodiceSoggetto(),
				Constanti.SEDE_SECONDARIA, getNow());
		SiacTSoggettoFin siacTSoggettoPrecedente = siacTSoggettoRepository.ricercaSoggettoNoSeSede(
				codiceAmbito, idEnte, soggettoPrecedente.getCodiceSoggetto(),
				Constanti.SEDE_SECONDARIA, getNow());
		if (siacTSoggettoCorrente != null && siacTSoggettoPrecedente != null)
		{
			SiacRSoggettoRelazFin siacRSoggettoRelaz = siacRSoggettoRelazRepository
					.findValidaBySoggettiETipo(siacTSoggettoPrecedente.getUid(),
							siacTSoggettoCorrente.getUid(), tipoLegame, idEnte);
			if (siacRSoggettoRelaz != null)
			{
				validitaLegame = true;
			}
		}
		return validitaLegame;
	}

	/**
	 * si occupa di annullare il legame tra i soggetti
	 * 
	 * @param richiedente
	 * @param ente
	 * @param tipoRelazioneSoggetto
	 * @param soggettoCorrente
	 * @param soggettoPrecedente
	 * @return
	 */
	public Soggetto annullaLegameSoggetti(Richiedente richiedente, String codiceAmbito, Ente ente,
			TipoRelazioneSoggetto tipoRelazioneSoggetto, Soggetto soggettoCorrente,
			Soggetto soggettoPrecedente)
	{
		long currMillisec = System.currentTimeMillis();
		// String loginOperazione = richiedente.getAccount().getNome();
		Operazione operazione = Operazione.ANNULLA;
		int idEnte = ente.getUid();
		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository
				.findOne(idEnte);
		DatiOperazioneDto datiOperazioneAnnullaLegame = new DatiOperazioneDto(currMillisec,
				operazione, siacTEnteProprietario, richiedente.getAccount().getId());

		String tipoLegame = tipoRelazioneSoggetto.name();

		SiacTSoggettoFin siacTSoggettoCorrente = siacTSoggettoRepository.ricercaSoggettoNoSeSede(
				codiceAmbito, idEnte, soggettoCorrente.getCodiceSoggetto(),
				Constanti.SEDE_SECONDARIA, getNow());

		SiacTSoggettoFin siacTSoggettoPrecedente = siacTSoggettoRepository.ricercaSoggettoNoSeSede(
				codiceAmbito, idEnte, soggettoPrecedente.getCodiceSoggetto(),
				Constanti.SEDE_SECONDARIA, getNow());

		if (siacTSoggettoCorrente != null && siacTSoggettoPrecedente != null)
		{
			SiacRSoggettoRelazFin siacRSoggettoRelaz = siacRSoggettoRelazRepository
					.findValidaBySoggettiETipo(siacTSoggettoPrecedente.getUid(),
							siacTSoggettoCorrente.getUid(), tipoLegame, idEnte);
			if (siacRSoggettoRelaz != null)
			{
				siacRSoggettoRelaz = DatiOperazioneUtils.impostaDatiOperazioneLogin(
						siacRSoggettoRelaz, datiOperazioneAnnullaLegame, siacTAccountRepository);
				// salvo sul db:
				siacRSoggettoRelazRepository.saveAndFlush(siacRSoggettoRelaz);
			}
		}

		soggettoCorrente = map(siacTSoggettoCorrente, Soggetto.class,
				FinMapId.SiacTSoggetto_Soggetto);
		soggettoCorrente = EntityToModelConverter.soggettoEntityToSoggettoModel(
				siacTSoggettoCorrente, soggettoCorrente);

		// Termino restituendo l'oggetto di ritorno:
		return soggettoCorrente;
	}

	/**
	 * si occupa di aggiornare il legame tra i soggetti
	 * 
	 * @param richiedente
	 * @param ente
	 * @param tipoRelazioneSoggetto
	 * @param soggettoCorrente
	 * @param soggettoPrecedente
	 * @return
	 */
	public Soggetto aggiornaLegameSoggetti(Richiedente richiedente, String codiceAmbito, Ente ente,
			TipoRelazioneSoggetto tipoRelazioneSoggetto, Soggetto soggettoCorrente,
			Soggetto soggettoPrecedente)
	{
		long currMillisec = System.currentTimeMillis();
		// String loginOperazione = richiedente.getAccount().getNome();
		Operazione operazione = Operazione.INSERIMENTO;
		int idEnte = ente.getUid();
		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository
				.findOne(idEnte);
		DatiOperazioneDto datiOperazioneAggiornaLegame = new DatiOperazioneDto(currMillisec,
				operazione, siacTEnteProprietario, richiedente.getAccount().getId());
		String tipoLegame = tipoRelazioneSoggetto.name();

		SiacTSoggettoFin siacTSoggettoCorrente = siacTSoggettoRepository.ricercaSoggettoNoSeSede(
				codiceAmbito, idEnte, soggettoCorrente.getCodiceSoggetto(),
				Constanti.SEDE_SECONDARIA, getNow());

		SiacTSoggettoFin siacTSoggettoPrecedente = siacTSoggettoRepository.ricercaSoggettoNoSeSede(
				codiceAmbito, idEnte, soggettoPrecedente.getCodiceSoggetto(),
				Constanti.SEDE_SECONDARIA, getNow());

		if (siacTSoggettoCorrente != null && siacTSoggettoPrecedente != null)
		{
			SiacRSoggettoRelazFin siacRSoggettoRelaz = new SiacRSoggettoRelazFin();
			siacRSoggettoRelaz = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRSoggettoRelaz,
					datiOperazioneAggiornaLegame, siacTAccountRepository);
			SiacDRelazTipoFin tipoRelaz = siacDRelazTipoRepository.findRelazione(tipoLegame, idEnte,
					datiOperazioneAggiornaLegame.getTs()).get(0);
			siacRSoggettoRelaz.setSiacDRelazTipo(tipoRelaz);
			siacRSoggettoRelaz.setSiacTSoggetto1(siacTSoggettoPrecedente);
			siacRSoggettoRelaz.setSiacTSoggetto2(siacTSoggettoCorrente);
			// salvo sul db:
			siacRSoggettoRelazRepository.saveAndFlush(siacRSoggettoRelaz);
		}
		// mappo sul model:
		soggettoCorrente = map(siacTSoggettoCorrente, Soggetto.class,
				FinMapId.SiacTSoggetto_Soggetto);
		soggettoCorrente = EntityToModelConverter.soggettoEntityToSoggettoModel(
				siacTSoggettoCorrente, soggettoCorrente);

		// Termino restituendo l'oggetto di ritorno:
		return soggettoCorrente;
	}

	/**
	 * dato un idSoggetto (cioe' un id che si riferisce ad un preciso
	 * siac_t_soggetto) ci restituisce: -false se il record referenziato e' di
	 * tipo soggetto -true se il record referenziato e' di tipo sede
	 * 
	 * @param idSoggetto
	 * @param idEnte
	 * @return
	 */
	public boolean isSedeSecondaria(Integer idSoggetto, Integer idEnte)
	{
		Timestamp now = getNow();
		List<SiacTSoggettoFin> sedes = siacTSoggettoRepository.caricaSoloSeSede(idEnte, idSoggetto,
				Constanti.SEDE_SECONDARIA, now);
		if (sedes != null && sedes.size() > 0 && sedes.get(0) != null
				&& sedes.get(0).getUid() != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Questo metodo lavora in base alla "convenzione" adottata su come popolare
	 * il model Soggetto rispetto al tipo di aggiornamento che si vuole
	 * realizzare. La convenzione e' la seguente:
	 * 
	 * * Soggetto in Modica in input. Soggetto.codiceSoggettoPadre deve essere
	 * valorizzato solo in caso di Soggetto in modifica. In tale situazione
	 * Soggetto.codiceSoggettoPadre dovra' essere popolato con il codice del
	 * Soggetto (fisicamente parliamo di un Siac_t_Soggetto) e
	 * Soggetto.codiceSoggetto dovra' essere valorizzato con il codice del
	 * SoggettoMod in questione (fisicamente parliamo di un
	 * Siac_t_Soggetto_mod).
	 * 
	 * * Soggetto "normale" in input. Se si vuole indicare un Soggetto in input
	 * nel model si deve ricevere Soggetto.codiceSoggettoPadre non valorizzato e
	 * Soggetto.codiceSoggetto valorizzato con il codice del soggetto in
	 * questione.
	 * 
	 * * Sede secondaria. Dato che le sedi secondarie di un certo Soggetto hanno
	 * tutte lo stesso codice del Soggetto a cui appartengono non e' possibile
	 * individuare una certa sede tramite il codice. La convenzione e' quindi non
	 * valorizzare Soggetto.codiceSoggettoPadre e nemmeno
	 * Soggetto.codiceSoggettoPadre ma valorizzare direttamente Soggetto.uid con
	 * l'id fisico della Sede Secondaria per la quale si richiede
	 * l'aggiornamento.
	 * 
	 * @param soggettoDaModificare
	 * @param idEnte
	 * @return
	 */
	public ValidaSoggettoInfoDto getIdForQuery(Soggetto soggettoDaModificare, String codiceAmbito,
			Integer idEnte)
	{
		String codiceSoggettoPadre = soggettoDaModificare.getCodiceSoggettoPadre();
		String codiceSoggetto = soggettoDaModificare.getCodiceSoggetto();
		ValidaSoggettoInfoDto info = new ValidaSoggettoInfoDto();
		info.setIdSiacTSoggettoMod(null);
		Integer idQuery = null;
		if (!StringUtils.isEmpty(codiceSoggettoPadre))
		{
			// Tramite il codice Soggetto Padre e il codice Soggetto ricevuti in
			// input si recupera i relativi oggetti SiacTSoggettoFin e
			// SiacTSoggettoModFin
			SiacTSoggettoFin siacTSoggetto = siacTSoggettoRepository.ricercaSoggettoNoSeSede(
					codiceAmbito, idEnte, codiceSoggettoPadre, Constanti.SEDE_SECONDARIA, getNow());
			SiacTSoggettoModFin siacTSoggettoMod = siacTSoggettoModRepository.ricercaSoggetto(idEnte,
					codiceSoggetto);
			info.setIdSiacTSoggettoMod(siacTSoggettoMod.getSogModId());
			idQuery = siacTSoggetto.getSoggettoId();
		}
		else if (!StringUtils.isEmpty(codiceSoggetto))
		{
			// Tramite il codice Soggetto si accede a SiacTSoggettoFin e si "salva"
			// l'id fisico nell'oggetto di ritorno
			SiacTSoggettoFin siacTSoggetto = siacTSoggettoRepository.ricercaSoggettoNoSeSede(
					codiceAmbito, idEnte, codiceSoggetto, Constanti.SEDE_SECONDARIA, getNow());
			idQuery = siacTSoggetto.getSoggettoId();
		}
		else if (StringUtils.isEmpty(codiceSoggetto) && soggettoDaModificare.getUid() > 0)
		{
			// SIAMO NEL CASO SEDE SECONDARIA CHE NON HA UN CODICE SUO E NON
			// PUO' CHE ESSERE INDENTIFICATA TRAMITE Primary Key:
			idQuery = soggettoDaModificare.getUid();
			// Si utilizza direttamente Soggetto.uid come id fisico di
			// SiacTSoggettoFin (inteso come sede in questo caso)
		}

		info.setIdSiacTSoggetto(idQuery);

		// Termino restituendo l'oggetto di ritorno:
		return info;
	}

	/**
	 * sub metodo che verifica dipendenze con movimenti gestione
	 * 
	 * @param siacTSoggetto
	 * @param listaErrori
	 * @return
	 */
	private List<Errore> subControlloEsistenzaMovimentiGestione(SiacTSoggettoFin siacTSoggetto,
			List<Errore> listaErrori, Operazione operazione, Integer idEnte)
	{
		List<SiacRMovgestTsSogFin> listaRMovGestTsSog = siacTSoggetto.getSiacRMovgestTsSogs();
		if (null != listaRMovGestTsSog && listaRMovGestTsSog.size() > 0)
		{
			for (SiacRMovgestTsSogFin siacRMovgestTsSog : listaRMovGestTsSog)
			{
				if (siacRMovgestTsSog.getDataFineValidita() == null)
				{
					// trovato valido
					SiacTMovgestTsFin siacTMovgestTs = siacRMovgestTsSog.getSiacTMovgestT();
					DisponibilitaMovimentoGestioneContainer disponibilita = null;

					if (siacTMovgestTs.getDataFineValidita() == null)
					{
						// valido
						if (operazione.equals(Operazione.ANNULLA))
						{
							// recupero lo stato valido nel momento
							// dell'elaborazione
							String statoValido = "";
							List<SiacRMovgestTsStatoFin> listaRMovgestTsStato = siacRMovgestTsStatoRepository
									.findValido(idEnte, getNow(), siacTMovgestTs.getMovgestTsId());
							SiacRMovgestTsStatoFin siacRMovgestTsStato = listaRMovgestTsStato.get(0);
							if (siacRMovgestTsStato != null)
							{
								statoValido = siacRMovgestTsStato.getSiacDMovgestStato()
										.getMovgestStatoCode();
							}

							// disponibilita
							if (siacTMovgestTs.getSiacTMovgest().getSiacDMovgestTipo()
									.getMovgestTipoCode()
									.equalsIgnoreCase(Constanti.MOVGEST_TIPO_ACCERTAMENTO))
							{
								if (siacTMovgestTs.getSiacDMovgestTsTipo().getMovgestTsTipoCode()
										.equalsIgnoreCase(Constanti.MOVGEST_TS_TIPO_TESTATA))
								{
									// accertamento
									disponibilita = calcolaDisponibiltaAIncassareAccertamento(siacTMovgestTs, statoValido, idEnte);
								}
								else
								{
									// subaccertamento
									disponibilita = calcolaDisponibiltaAIncassareSubAccertamento(siacTMovgestTs, statoValido, idEnte);
								}
							}
							else
							{
								if (siacTMovgestTs.getSiacDMovgestTsTipo().getMovgestTsTipoCode()
										.equalsIgnoreCase(Constanti.MOVGEST_TS_TIPO_TESTATA))
								{
									// impegno
									disponibilita = calcolaDisponibilitaAPagareImpegno(
											siacTMovgestTs,
											statoValido, idEnte);
								}
								else
								{
									// subimpegno
									disponibilita = calcolaDisponibilitaAPagareSubImpegno(
											siacTMovgestTs, statoValido, idEnte);
								}
							}
						}

						if (siacTMovgestTs.getSiacTMovgest().getSiacDMovgestTipo()
								.getMovgestTipoCode()
								.equalsIgnoreCase(Constanti.MOVGEST_TIPO_ACCERTAMENTO))
						{
							// accertamento
							if (siacTMovgestTs.getSiacDMovgestTsTipo().getMovgestTsTipoCode()
									.equalsIgnoreCase(Constanti.MOVGEST_TS_TIPO_TESTATA))
							{
								// accertamento
								// se sono in cancellazione non prendo in
								// considerazione la disponibilita
								if (operazione.equals(Operazione.CANCELLAZIONE_LOGICA_RECORD)
										|| (operazione.equals(Operazione.ANNULLA) && disponibilita.getDisponibilita()
												.compareTo(BigDecimal.ZERO) > 0))
								{

									if (operazione.equals(Operazione.ANNULLA))
									{
										listaErrori.add(ErroreFin.ANNULLAMENTO_SOGGETTO_IMPOSSIBILE
												.getErrore("", "accertamenti"));
									}
									else
									{
										listaErrori
												.add(ErroreFin.CANCELLAZIONE_SOGGETTO_IMPOSSIBILE
														.getErrore("", "accertamenti"));
									}
									break;
								}
							}
							else
							{
								// subaccertamento
								if (operazione.equals(Operazione.CANCELLAZIONE_LOGICA_RECORD)
										|| (operazione.equals(Operazione.ANNULLA) && disponibilita.getDisponibilita()
												.compareTo(BigDecimal.ZERO) > 0))
								{
									if (operazione.equals(Operazione.ANNULLA))
									{
										listaErrori.add(ErroreFin.ANNULLAMENTO_SOGGETTO_IMPOSSIBILE
												.getErrore("", "sub-accertamenti"));
									}
									else
									{
										listaErrori
												.add(ErroreFin.CANCELLAZIONE_SOGGETTO_IMPOSSIBILE
														.getErrore("", "sub-accertamenti"));
									}
									break;
								}
							}
						}
						else
						{
							// impegno
							if (siacTMovgestTs.getSiacDMovgestTsTipo().getMovgestTsTipoCode()
									.equalsIgnoreCase(Constanti.MOVGEST_TS_TIPO_TESTATA))
							{
								// impegno
								if (operazione.equals(Operazione.CANCELLAZIONE_LOGICA_RECORD)
										|| (operazione.equals(Operazione.ANNULLA) && disponibilita.getDisponibilita()
												.compareTo(BigDecimal.ZERO) > 0))
								{
									if (operazione.equals(Operazione.ANNULLA))
									{
										listaErrori.add(ErroreFin.ANNULLAMENTO_SOGGETTO_IMPOSSIBILE
												.getErrore("", "impegni"));
									}
									else
									{
										listaErrori
												.add(ErroreFin.CANCELLAZIONE_SOGGETTO_IMPOSSIBILE
														.getErrore("", "impegni"));
									}
									break;
								}
							}
							else
							{
								// subimpegno
								if (operazione.equals(Operazione.CANCELLAZIONE_LOGICA_RECORD)
										|| (operazione.equals(Operazione.ANNULLA) && disponibilita.getDisponibilita()
												.compareTo(BigDecimal.ZERO) > 0))
								{
									if (operazione.equals(Operazione.ANNULLA))
									{
										listaErrori.add(ErroreFin.ANNULLAMENTO_SOGGETTO_IMPOSSIBILE
												.getErrore("", "sub-impegni"));
									}
									else
									{
										listaErrori
												.add(ErroreFin.CANCELLAZIONE_SOGGETTO_IMPOSSIBILE
														.getErrore("", "sub-impegni"));
									}
									break;
								}
							}
						}//
					}// fine datafine=null
				}
			}
		}
		// Termino restituendo l'oggetto di ritorno:
		return listaErrori;
	}

	/**
	 * sub metodo che verifica dipendenze con liquidazioni
	 * 
	 * @param siacTSoggetto
	 * @param listaErrori
	 * @return
	 */
	private List<Errore> subControlloEsistenzaLiquidazioni(SiacTSoggettoFin siacTSoggetto,
			List<Errore> listaErrori, Operazione operazione, boolean isSede)
	{
		List<SiacRLiquidazioneSoggettoFin> listaRLiquidazioneSoggettos = siacTSoggetto
				.getSiacRLiquidazioneSoggettos();
		if (null != listaRLiquidazioneSoggettos && listaRLiquidazioneSoggettos.size() > 0)
		{
			for (SiacRLiquidazioneSoggettoFin siacRLiquidazioneSoggetto : listaRLiquidazioneSoggettos)
			{
				if (siacRLiquidazioneSoggetto.getDataFineValidita() == null)
				{
					// trovato valido
					if (operazione.equals(Operazione.ANNULLA))
					{
						if (isSede == false)
						{
							listaErrori.add(ErroreFin.ANNULLAMENTO_SOGGETTO_IMPOSSIBILE.getErrore(
									"", "liquidazioni"));
						}
						else
						{
							listaErrori.add(ErroreFin.ANNULLAMENTO_SOGGETTO_IMPOSSIBILE.getErrore(
									"La sede", "liquidazioni"));
						}
					}
					else
					{
						if (isSede == false)
						{
							listaErrori.add(ErroreFin.CANCELLAZIONE_SOGGETTO_IMPOSSIBILE.getErrore(
									"", "liquidazioni"));
						}
						else
						{
							listaErrori.add(ErroreFin.CANCELLAZIONE_SOGGETTO_IMPOSSIBILE.getErrore(
									"La sede", "liquidazioni"));
						}
					}
					break;
				}
			}
		}
		// Termino restituendo l'oggetto di ritorno:
		return listaErrori;
	}

	/**
	 * sub metodo che verifica dipendenze con ordinativi
	 * 
	 * @param siacTSoggetto
	 * @param listaErrori
	 * @return
	 */
	private List<Errore> subControlloEsistenzaOrdinativi(SiacTSoggettoFin siacTSoggetto,
			List<Errore> listaErrori, Operazione operazione, boolean isSede)
	{
		List<SiacROrdinativoSoggettoFin> listaROrdinativoSoggettos = siacTSoggetto
				.getSiacROrdinativoSoggettos();
		if (null != listaROrdinativoSoggettos && listaROrdinativoSoggettos.size() > 0)
		{
			for (SiacROrdinativoSoggettoFin siacROrdinativoSoggetto : listaROrdinativoSoggettos)
			{
				if (siacROrdinativoSoggetto.getDataFineValidita() == null)
				{
					// trovato valido
					if (siacROrdinativoSoggetto.getSiacTOrdinativo().getSiacDOrdinativoTipo()
							.getOrdTipoCode().equalsIgnoreCase(Constanti.ORDINATIVO_TIPO_INCASSO))
					{
						// incasso
						if (isSede == false)
						{
							if (operazione.equals(Operazione.ANNULLA))
							{
								listaErrori.add(ErroreFin.ANNULLAMENTO_SOGGETTO_IMPOSSIBILE
										.getErrore("", "ordinativi incasso"));
							}
							else
							{
								listaErrori.add(ErroreFin.CANCELLAZIONE_SOGGETTO_IMPOSSIBILE
										.getErrore("", "ordinativi incasso"));
							}
						}
						else
						{
							if (operazione.equals(Operazione.ANNULLA))
							{
								listaErrori.add(ErroreFin.ANNULLAMENTO_SOGGETTO_IMPOSSIBILE
										.getErrore("La sede", "ordinativi incasso"));
							}
							else
							{
								listaErrori.add(ErroreFin.CANCELLAZIONE_SOGGETTO_IMPOSSIBILE
										.getErrore("La sede", "ordinativi incasso"));
							}
						}
						break;
					}
					else
					{
						// pagamento
						if (isSede == false)
						{
							if (operazione.equals(Operazione.ANNULLA))
							{
								listaErrori.add(ErroreFin.ANNULLAMENTO_SOGGETTO_IMPOSSIBILE
										.getErrore("", "ordinativi pagamento"));
							}
							else
							{
								listaErrori.add(ErroreFin.CANCELLAZIONE_SOGGETTO_IMPOSSIBILE
										.getErrore("", "ordinativi pagamento"));
							}
						}
						else
						{
							if (operazione.equals(Operazione.ANNULLA))
							{
								listaErrori.add(ErroreFin.ANNULLAMENTO_SOGGETTO_IMPOSSIBILE
										.getErrore("La sede", "ordinativi pagamento"));
							}
							else
							{
								listaErrori.add(ErroreFin.CANCELLAZIONE_SOGGETTO_IMPOSSIBILE
										.getErrore("La sede", "ordinativi pagamento"));
							}
						}
						break;
					}
				}
			}
		}
		// Termino restituendo l'oggetto di ritorno:
		return listaErrori;
	}

	/**
	 * sub metodo che verifica dipendenze con documenti
	 * 
	 * @param siacTSoggetto
	 * @param listaErrori
	 * @return
	 */
	private List<Errore> subControlloEsistenzaDocumenti(SiacTSoggettoFin siacTSoggetto,
			List<Errore> listaErrori, Operazione operazione)
	{
		List<SiacRDocSogFin> lisRdocSogs = siacTSoggetto.getSiacRDocSogs();
		if (null != lisRdocSogs && lisRdocSogs.size() > 0)
		{
			for (SiacRDocSogFin siacRDocSog : lisRdocSogs)
			{
				if (siacRDocSog.getDataFineValidita() == null)
				{
					// trovato valido
					if (operazione.equals(Operazione.ANNULLA))
					{
						listaErrori.add(ErroreFin.ANNULLAMENTO_SOGGETTO_IMPOSSIBILE.getErrore("",
								"documenti"));
					}
					else
					{
						listaErrori.add(ErroreFin.CANCELLAZIONE_SOGGETTO_IMPOSSIBILE.getErrore("",
								"documenti"));
					}
					break;
				}
			}
		}
		// Termino restituendo l'oggetto di ritorno:
		return listaErrori;
	}

	/**
	 * sub metodo che verifica dipendenze con cessioni di credito / debito
	 * 
	 * @param siacTSoggetto
	 * @param listaErrori
	 * @return
	 */
	private List<Errore> subControlloEsistenzaCessioni(SiacTSoggettoFin siacTSoggetto,
			List<Errore> listaErrori)
	{
		List<SiacRSoggettoRelazFin> listRelazs = siacTSoggetto.getSiacRSoggettoRelazs2();
		if (null != listRelazs && listRelazs.size() > 0)
		{
			for (SiacRSoggettoRelazFin siacRSoggettoRelaz : listRelazs)
			{
				if (siacRSoggettoRelaz.getDataFineValidita() == null)
				{
					// trovato valido
					if (siacRSoggettoRelaz
							.getSiacDRelazTipo()
							.getRelazTipoCode()
							.equalsIgnoreCase(Constanti.D_ACCREDITO_TIPO_CODE_Cessione_dell_incasso))
					{
						listaErrori.add(ErroreFin.ANNULLAMENTO_SOGGETTO_IMPOSSIBILE.getErrore("",
								"cessioni di incasso"));
						break;
					}
					else if (siacRSoggettoRelaz.getSiacDRelazTipo().getRelazTipoCode()
							.equalsIgnoreCase(Constanti.D_ACCREDITO_TIPO_CODE_Cessione_del_credito))
					{
						listaErrori.add(ErroreFin.ANNULLAMENTO_SOGGETTO_IMPOSSIBILE.getErrore("",
								"cessioni di credito"));
						break;
					}
				}
			}
		}
		// Termino restituendo l'oggetto di ritorno:
		return listaErrori;
	}

	/**
	 * sub metodo che verifica dipendenze con sedi secondarie
	 * 
	 * @param siacTSoggetto
	 * @param listaErrori
	 * @return
	 */
	private List<Errore> subControlloEsistenzaSediSecondarie(SiacTSoggettoFin siacTSoggetto,
			List<Errore> listaErrori)
	{
		List<SiacRSoggettoRelazFin> listRelazs = siacTSoggetto.getSiacRSoggettoRelazs1();
		if (null != listRelazs && listRelazs.size() > 0)
			for (SiacRSoggettoRelazFin siacRSoggettoRelaz : listRelazs)
				if (siacRSoggettoRelaz.getDataFineValidita() == null)
					if (siacRSoggettoRelaz.getSiacTSoggetto2().getDataCancellazione() == null)
						if (siacRSoggettoRelaz.getSiacDRelazTipo().getRelazTipoCode()
								.equalsIgnoreCase(Constanti.SEDE_SECONDARIA))
						{
							listaErrori.add(ErroreFin.CANCELLAZIONE_SOGGETTO_IMPOSSIBILE.getErrore("",
									"sedi secondarie"));
							break;
						}
		

		return listaErrori;
	}

	/**
	 * sub metodo che verifica dipendenze con modalita' di pagamento
	 * 
	 * @param siacTSoggetto
	 * @param listaErrori
	 * @return
	 */
	private List<Errore> subControlloEsistenzaModalitaDiPagamento(SiacTSoggettoFin siacTSoggetto,
			List<Errore> listaErrori)
	{
		List<SiacTModpagFin> listaSiacTModpag = siacTSoggetto.getSiacTModpags();
		if (null != listaSiacTModpag && listaSiacTModpag.size() > 0)
		{
			for (SiacTModpagFin siacTModpag : listaSiacTModpag)
			{
				if (siacTModpag.getDataFineValidita() == null && siacTModpag.getDataCancellazione() == null)
				{
					listaErrori.add(ErroreFin.CANCELLAZIONE_SOGGETTO_IMPOSSIBILE.getErrore("",
							"modalita'  di pagamento"));
					break;
				}
			}
		}
		// Termino restituendo l'oggetto di ritorno:
		return listaErrori;
	}
	
	
	public Soggetto completaInformazioni(Soggetto sogg,String codiceAmbito, Integer idEnte,String codiceSoggetto,boolean isIncludeModif, Richiedente richiedente,DatiOperazioneDto datiOperazioneDto){
		String methodName = "completaInformazioni";
		List<SedeSecondariaSoggetto> listaSediSecondarie = null;
		List<ModalitaPagamentoSoggetto> listaModPag = null ;
		Soggetto soggMod = null;
		if(sogg!=null){
			//2 Lista SediSecondarie
			log.debug(methodName, "- chiamo ricercaSediSecondarie()");	
			boolean isDecentrato = isDecentrato(richiedente.getAccount());
			listaSediSecondarie = ricercaSediSecondarie(idEnte, sogg.getUid(), isDecentrato, isIncludeModif,datiOperazioneDto);
			//3 Lista ModalitaPagamento
			log.debug(methodName, "chiamo ricercaModalitaPagamento()");			
			listaModPag = ricercaModalitaPagamento(codiceAmbito, idEnte, sogg.getUid(), "Soggetto", isIncludeModif);	
			if (listaSediSecondarie != null && listaSediSecondarie.size() > 0) {
				for (SedeSecondariaSoggetto currentSedeSecondaria : listaSediSecondarie) {
					List<ModalitaPagamentoSoggetto> listaModPagSupportSedi = ricercaModalitaPagamento(codiceAmbito, idEnte, currentSedeSecondaria.getUid(), currentSedeSecondaria.getDenominazione(), isIncludeModif);
					if (listaModPagSupportSedi != null && listaModPagSupportSedi.size() > 0) {
						if (listaModPag == null) {
							listaModPag = new ArrayList<ModalitaPagamentoSoggetto>();
						}
						for (ModalitaPagamentoSoggetto currentModPagSede : listaModPagSupportSedi) {
							listaModPag.add(currentModPagSede);
						}
					}
				}
			}
			
			sogg.setSediSecondarie(listaSediSecondarie);
			sogg.setModalitaPagamentoList(listaModPag);
		}
		return sogg;
	}

	/**
	 * metodo che si occupa di verificare l'esistenza di legami del soggetto con
	 * altre entita: movimenti, liquidazioni, ordinativi, ecc.
	 * 
	 * @param idSoggetto
	 * @param ente
	 * @param operazione
	 * @return
	 * @throws RuntimeException
	 */
	public List<Errore> controlliEsistenzaLegamiSoggettoEntita(Integer idSoggetto, Ente ente,
			Operazione operazione) throws RuntimeException
	{

		Integer idEnte = ente.getUid();

		// operazione puo' valere ANNULLA O CANCELLAZIONE LOGICA
		List<Errore> listaErrori = new ArrayList<Errore>();

		SiacTSoggettoFin siacTSoggetto = siacTSoggettoRepository.findOne(idSoggetto);
		if (null != siacTSoggetto)
		{
			// MOVIMENTI DI GESTIONE
			listaErrori = subControlloEsistenzaMovimentiGestione(siacTSoggetto, listaErrori,
					operazione, idEnte);

			// LIQUIDAZIONI
			listaErrori = subControlloEsistenzaLiquidazioni(siacTSoggetto, listaErrori, operazione,
					false);

			// ORDINATIVI
			listaErrori = subControlloEsistenzaOrdinativi(siacTSoggetto, listaErrori, operazione,
					false);

			if (operazione.equals(Operazione.ANNULLA))
			{
				// CESSIONE CREDITO / DEBITO
				listaErrori = subControlloEsistenzaCessioni(siacTSoggetto, listaErrori);
			}

			if (operazione.equals(Operazione.CANCELLAZIONE_LOGICA_RECORD))
			{
				// DOCUMENTI
				listaErrori = subControlloEsistenzaDocumenti(siacTSoggetto, listaErrori, operazione);

				// SEDI SECONDARIE
				listaErrori = subControlloEsistenzaSediSecondarie(siacTSoggetto, listaErrori);

				// MODALITA DI PAGAMENTO
				listaErrori = subControlloEsistenzaModalitaDiPagamento(siacTSoggetto, listaErrori);
			}
		}
		// Termino restituendo l'oggetto di ritorno:
		return listaErrori;
	}

	public String ricercaCodiceSoggetto(Integer uidSoggetto)
	{
		return siacTSoggettoRepository.ricercaCodiceSoggetto(uidSoggetto);
	}
	
	
	public boolean isModpagCollegataSubdocNonIncassati(Integer modpagId)
	{
		return siacTSoggettoRepository.isModpagCollegataSubdocNonIncassati(modpagId);
	}
	
	
	/**
	 * Elimina logicamente la classe sogegtto identificata da <code>item</code>
	 * 
	 * @param item
	 * @param ente
	 * @param richiedente
	 * @param errori
	 */
	public void annullaClasseSoggetto(ClassificazioneSoggetto item, Ente ente, Richiedente richiedente,
	List<Errore> errori) {
		final Timestamp NOW = getNow();
		Integer idClasseSoggetto = item.getIdSoggClasse();
		
		SiacTEnteProprietarioFin enteProprietario = siacTEnteProprietarioRepository.findOne(ente.getUid());
		if (enteProprietario == null) {
			Errore e = ErroreCore.ENTITA_NON_TROVATA.getErrore("SiacTEnteProprietarioFin", ente.getUid());
			errori.add(e);
			return;
		}
		
		// ricarica la classe soggetto dal db
		SiacDSoggettoClasseFin one = siacDSoggettoClasseRepository.findOne(idClasseSoggetto);
						
		if (one == null) {
			Errore e = ErroreCore.ENTITA_NON_TROVATA.getErrore("SiacDSoggettoClasseFin", idClasseSoggetto);
			errori.add(e);
			return;
		}		
		
		// TODO: controlla che non esistano movimenti ?
		// con la classe di soggetto che si intende annullare
		{
			Long count = siacRMovgestTsSogClasseRepository.countValidiBySogClasseIdAndEnte(
						enteProprietario.getEnteProprietarioId(), 
						idClasseSoggetto, 
						NOW);
			
			if ( count > 0 ) {
				String ecode = ErroreCore.ESISTONO_ENTITA_COLLEGATE.getCodice();
				Errore e = new Errore(ecode, MessageFormat.format("Esistono {0} movimenti con classe soggetto {1}",count, one.getSoggettoClasseCode()));
				errori.add( e );
				return;
			}
		}

		{
			// controlla che non sia presente nessuna relazione con la tabella siac_r_soggetto_classe
			Long count = siacRSoggettoClasseRepository.countValidiBySoggettoClasseId(idClasseSoggetto, NOW);
			if ( count > 0 ) {
				String ecode = ErroreCore.ESISTONO_ENTITA_COLLEGATE.getCodice();
				Errore e = new Errore(ecode, MessageFormat.format("Esistono {0} soggetti con classe {1}",count, one.getSoggettoClasseCode()));
				errori.add( e );
				return;
			}
		}
		
//		// ricarica la classe soggetto dal db ed aggiorna la data di cancellazione
//		SiacDSoggettoClasseFin one = siacDSoggettoClasseRepository.findOne(idClasseSoggetto);
//						
//		if (one == null) {
//			Errore e = ErroreCore.ENTITA_NON_TROVATA.getErrore("SiacDSoggettoClasseFin", idClasseSoggetto);
//			errori.add(e);
//			return;
//		}		
		
		one.setDataCancellazione(NOW);
		one.setDataModifica(NOW);
		
		// valorizza l'account
		String accountName = siacTAccountRepository.findOne(richiedente.getAccount().getUid()).getAccountCode();
		one.setLoginOperazione(accountName);

		siacDSoggettoClasseRepository.save(one);
		
	}
	
//	public void chiudiClasseSoggetto(ClassificazioneSoggetto item, Ente ente, Richiedente richiedente,
//			List<Errore> errori) {
//		Integer idClasseSoggetto = item.getIdSoggClasse();
//		SiacTEnteProprietarioFin enteProprietario = siacTEnteProprietarioRepository.findOne(ente.getUid());
//
//		if (enteProprietario == null) {
//			Errore e = ErroreCore.ENTITA_NON_TROVATA.getErrore("SiacTEnteProprietarioFin", ente.getUid());
//			errori.add(e);
//			return;
//		}
//
//		// ricarica la classe soggetto dal db
//		SiacDSoggettoClasseFin one = siacDSoggettoClasseRepository.findOne(idClasseSoggetto);
//		if (one == null) {
//			Errore e = ErroreCore.ENTITA_NON_TROVATA.getErrore("SiacDSoggettoClasseFin", idClasseSoggetto);
//			errori.add(e);
//			return;
//		}
//
//		Timestamp now = getNow();
//		one.setDataCancellazione(now);
//
//		one.setDataFineValidita(item.getDataFineValidita() == null ? now : item.getDataFineValidita());
//
//		// valorizza l'account
//		String accountName = siacTAccountRepository.findOne(richiedente.getAccount().getUid()).getAccountCode();
//		one.setLoginOperazione(accountName);
//
//		siacDSoggettoClasseRepository.save(one);
//	}

	public void modificaClasseSoggetto(ClassificazioneSoggetto item, Ente ente,
			Richiedente richiedente, List<Errore> errori) {
		
		SiacTEnteProprietarioFin enteProprietario = siacTEnteProprietarioRepository.findOne(ente.getUid());
		if (enteProprietario == null) {
			Errore e = ErroreCore.ENTITA_NON_TROVATA.getErrore("SiacTEnteProprietarioFin", ente.getUid());
			errori.add(e);
			return;
		}
		
		SiacDSoggettoClasseFin one = siacDSoggettoClasseRepository.findOne(item.getIdSoggClasse());
		if ( one != null ) {
			// modifica l'entita 
//			one.setSoggettoClasseCode(item.getSoggettoClasseCode());
			one.setSoggettoClasseDesc(item.getSoggettoClasseDesc());
						
			String accountCode = siacTAccountRepository.findOne(richiedente.getAccount().getUid()).getAccountCode();
			one.setLoginOperazione(accountCode);
			one.setDataModifica(getNow());
			
			siacDSoggettoClasseRepository.save(one);
			
			return;
		}
		
		// 
		Errore e = ErroreCore.ENTITA_NON_TROVATA.getErrore("SiacDSoggettoClasseFin", item.getIdSoggClasse());
		errori.add(e);
		return;
	}
	
	public List<CodificaFin> listaSoggettiDellaClasse(DatiOperazioneDto datiOperazione, String codClasse){
		List<CodificaFin> listaSoggettiDellaClasse = new ArrayList<CodificaFin>();
		Integer idEnte = datiOperazione.getSiacTEnteProprietario().getUid();
		SiacDSoggettoClasseFin siacDSoggettoClasseFin = CommonUtils.getFirst(siacDSoggettoClasseRepository.findValidoByCode(idEnte, datiOperazione.getSiacDAmbito().getAmbitoId(), codClasse,getNow()));
		if(siacDSoggettoClasseFin!=null){
			int idClasse = siacDSoggettoClasseFin.getUid();
			List<Object[]> soggettiDellaClasse = siacRSoggettoClasseRepository.findIdCodiceSoggettiValidiByIdClasse(idClasse, getNow());
			if(!StringUtils.isEmpty(soggettiDellaClasse)){
				for(Object[] it: soggettiDellaClasse){
					if(it!=null && it.length==2){
						CodificaFin soggIt = new CodificaFin();
						if(it[0]!=null){
							soggIt.setUid((Integer)it[0]);
						}
						if(it[1]!=null){
							soggIt.setCodice((String)it[1]);
						}
						listaSoggettiDellaClasse.add(soggIt);
					}
				}
			}
		}
		return listaSoggettiDellaClasse;
	}
	
	public void inserisciClasseSoggetto(ClassificazioneSoggetto item, Ente ente,
			Richiedente richiedente, List<Errore> errori) {

		SiacTEnteProprietarioFin enteProprietario = siacTEnteProprietarioRepository.findOne(ente.getUid());
		if (enteProprietario == null) {
			Errore e = ErroreCore.ENTITA_NON_TROVATA.getErrore("SiacTEnteProprietarioFin", ente.getUid());
			errori.add(e);
			return;
		}

		// Rimappa la "codifica" nell'entita'
		SiacDSoggettoClasseFin entity = new SiacDSoggettoClasseFin();
		entity.setSoggettoClasseCode(item.getSoggettoClasseCode());
		entity.setSoggettoClasseDesc(item.getSoggettoClasseDesc());

		entity.setSiacTEnteProprietario(enteProprietario);

		// Lookup dell'ambito (che dovrebbe essere sempre fin) 
		SiacDAmbitoFin ambito = siacDAmbitoRepository.findAmbitoByCode("AMBITO_FIN",
				enteProprietario.getEnteProprietarioId());
		
		if (ambito == null) {
			Errore e = ErroreCore.ENTITA_NON_TROVATA.getErrore("SiacDAmbitoFin", "AMBITO_FIN");
			errori.add(e);
			return; 
		}

		
		// TODO controllare che il nuovo codice non esista gia ?
		Timestamp today = getNow(); 
		List<SiacDSoggettoClasseFin> checks = siacDSoggettoClasseRepository.findSoggettoClasseByCodeAndAmbitoAndEnte(item.getSoggettoClasseCode(), ente.getUid(), today, ambito.getAmbitoId());
		if ( checks.size() > 0 ) {
			Errore e = ErroreCore.ENTITA_PRESENTE.getErrore("codice ", item.getSoggettoClasseCode());
			errori.add(e);
			return;
		}
		
		// completa i campi dell'entità con i valori mancanti
		{
			Timestamp now = getNow();
			// entity.setAmbitoId(ambitoId);

			entity.setDataCreazione(now);
			entity.setDataModifica(now);
			entity.setDataFineValidita(null);
			entity.setDataInizioValidita(now);

			String accountName = siacTAccountRepository.findOne(richiedente.getAccount().getUid()).getAccountCode();
			entity.setLoginOperazione(accountName);

//			// valorizza il'ambito
//			SiacDAmbitoFin ambito = siacDAmbitoRepository.findAmbitoByCode("AMBITO_FIN",
//					enteProprietario.getEnteProprietarioId());
//			if (ambito == null) {
//				Errore e = ErroreCore.ENTITA_NON_TROVATA.getErrore("SiacDAmbitoFin", "AMBITO_FIN");
//				errori.add(e);
//				return;
//			}
			
			entity.setAmbitoId(ambito.getAmbitoId());

			SiacDSoggettoClasseTipoFin classeTipo = siacDSoggettoClasseTipoFinRepository.findByCodiceEAmbito("ND",
					ambito.getAmbitoId(), enteProprietario.getEnteProprietarioId(), now);

			entity.setSiacDSoggettoClasseTipo(classeTipo);
		}

		SiacDSoggettoClasseFin saved = siacDSoggettoClasseRepository.save(entity);
	}

	public void aggiornaDatiDurcSoggetto(Integer idSoggetto, Date dataFineValiditaDurc, Character tipoFonteDurc, 
			Integer fonteDurcClassifId, String fonteDurcAutomatica, String noteDurc, String loginOperazione) {
		
		SiacTClassFin fonteDurc = null;

		if (TipoFonteDurc.MANUALE.getCodice().equals(tipoFonteDurc) && fonteDurcClassifId != null) {
			fonteDurc = new SiacTClassFin();
			fonteDurc.setUid(fonteDurcClassifId);
		} 
		
		siacTSoggettoRepository.aggiornaDatiDurcSoggetto(
				idSoggetto,
				dataFineValiditaDurc, 
				tipoFonteDurc, 
				fonteDurc, 
				fonteDurcAutomatica, 
				noteDurc, 
				loginOperazione
		);
	}

}

// Classe per ordinamento della lista
class MdpComparator implements Comparator<ModalitaPagamentoSoggetto>
{
	@Override
	public int compare(ModalitaPagamentoSoggetto objToCampareUno,
			ModalitaPagamentoSoggetto objToCampareDue)
	{
		if (objToCampareUno != null && objToCampareUno.getDataCreazione() != null
				&& objToCampareDue != null && objToCampareDue.getDataCreazione() != null)
		{
			// da confrontare con date creazione
			return objToCampareUno.getDataCreazione().compareTo(objToCampareDue.getDataCreazione());
		}
		else
		{
			return -1;
		}
	}
}