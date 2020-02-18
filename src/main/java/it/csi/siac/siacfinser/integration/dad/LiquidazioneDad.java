/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilser.integration.dad.DocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocLiquidazione;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.Entita.StatoEntita;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.CommonUtils;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.StringUtils;
import it.csi.siac.siacfinser.TimingUtils;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.integration.dad.datacontainer.DisponibilitaMovimentoGestioneContainer;
import it.csi.siac.siacfinser.integration.dao.common.SiacDAmbitoRepository;
import it.csi.siac.siacfinser.integration.dao.common.SiacTEnteProprietarioFinRepository;
import it.csi.siac.siacfinser.integration.dao.common.dto.AttributoTClassInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.CapitoliInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ChiaveLogicaCapitoloDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoGestioneLiquidazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoInserimentoMovimentoGestioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoRicercaMovimentoPkDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OggettoDellAttributoTClass;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneLiquidazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneModalitaPagamentoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneMovGestDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneOrdinativoPagamentoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneSoggettoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.PaginazioneSubMovimentiDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaEstesaLiquidazioniDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaLiquidazioneParamDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.SoggettoInRicercaLiquidazioneDto;
import it.csi.siac.siacfinser.integration.dao.liquidazione.LiquidazioneFinDao;
import it.csi.siac.siacfinser.integration.dao.liquidazione.SiacDContotesoreriaFinRepository;
import it.csi.siac.siacfinser.integration.dao.liquidazione.SiacDDistintaRepository;
import it.csi.siac.siacfinser.integration.dao.liquidazione.SiacDLiquidazioneStatoRepository;
import it.csi.siac.siacfinser.integration.dao.liquidazione.SiacRLiquidazioneAttoAmmRepository;
import it.csi.siac.siacfinser.integration.dao.liquidazione.SiacRLiquidazioneMovgestRepository;
import it.csi.siac.siacfinser.integration.dao.liquidazione.SiacRLiquidazioneOrdRepository;
import it.csi.siac.siacfinser.integration.dao.liquidazione.SiacRLiquidazioneSoggettoRepository;
import it.csi.siac.siacfinser.integration.dao.liquidazione.SiacRLiquidazioneStatoRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacRBilFaseOperativaRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacRBilStatoOpRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTAttoAmmFinRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTBilFinRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTMovgestRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTMovgestTsDetRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTMovgestTsRepository;
import it.csi.siac.siacfinser.integration.dao.mutuo.SiacRMutuoVoceLiquidazioneRepository;
import it.csi.siac.siacfinser.integration.dao.mutuo.SiacTMutuoVoceRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacTOrdinativoTsDetRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacRSoggettoRelazFinRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacTModpagFinRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacTSoggettoFinRepository;
import it.csi.siac.siacfinser.integration.entity.SiacDAmbitoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDContotesoreriaFin;
import it.csi.siac.siacfinser.integration.entity.SiacDDistintaFin;
import it.csi.siac.siacfinser.integration.entity.SiacDLiquidazioneStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDOrdinativoTsDetTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDSiopeAssenzaMotivazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacDSiopeTipoDebitoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRAttoAllegatoElencoDocFin;
import it.csi.siac.siacfinser.integration.entity.SiacRBilFaseOperativaFin;
import it.csi.siac.siacfinser.integration.entity.SiacRElencoDocSubdocFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneAttoAmmFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneAttrFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneOrdFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestBilElemFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsAttrFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMutuoVoceLiquidazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoRelazFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSubdocLiquidazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTAttoAmmFin;
import it.csi.siac.siacfinser.integration.entity.SiacTBilFin;
import it.csi.siac.siacfinser.integration.entity.SiacTClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacTDocFin;
import it.csi.siac.siacfinser.integration.entity.SiacTEnteProprietarioFin;
import it.csi.siac.siacfinser.integration.entity.SiacTLiquidazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModpagFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMutuoVoceFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoTsDetFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.mapping.FinMapId;
import it.csi.siac.siacfinser.integration.util.DatiOperazioneUtils;
import it.csi.siac.siacfinser.integration.util.EntityLiquidazioneToModelLiquidazioneConverter;
import it.csi.siac.siacfinser.integration.util.EntityToModelConverter;
import it.csi.siac.siacfinser.integration.util.ObjectStreamerHandler;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.integration.util.TransazioneElementareEntityToModelConverter;
import it.csi.siac.siacfinser.model.ContoTesoreria;
import it.csi.siac.siacfinser.model.Distinta;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.MovimentoGestione.AttributoMovimentoGestione;
import it.csi.siac.siacfinser.model.StoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.codifiche.ModalitaAccreditoSoggetto;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione.StatoOperativoLiquidazione;
import it.csi.siac.siacfinser.model.liquidazione.LiquidazioneAtti;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.mutuo.Mutuo;
import it.csi.siac.siacfinser.model.mutuo.VoceMutuo;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo.StatoOperativoOrdinativo;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaLiquidazione;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaStoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;
import it.csi.siac.siacfinser.model.siopeplus.SiopeTipoDebito;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto.TipoAccredito;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class LiquidazioneDad extends AbstractFinDad {

	@Autowired
	ObjectStreamerHandler objectStreamerHandler;

	@Autowired
	SiacRMutuoVoceLiquidazioneRepository siacRMutuoVoceLiquidazioneRepository;

	@Autowired
	SiacRLiquidazioneAttoAmmRepository siacRLiquidazioneAttoAmmRepository;

	@Autowired
	SiacRLiquidazioneMovgestRepository siacRLiquidazioneMovgestRepository;

	@Autowired
	SiacRLiquidazioneOrdRepository siacRLiquidazioneOrdRepository;

	@Autowired
	SiacRLiquidazioneSoggettoRepository siacRLiquidazioneSoggettoRepository;

	@Autowired
	SiacRLiquidazioneStatoRepository siacRLiquidazioneStatoRepository;

	@Autowired
	SiacTEnteProprietarioFinRepository siacTEnteProprietarioRepository;

	@Autowired
	SiacDContotesoreriaFinRepository siacDContotesoreriaRepository;

	@Autowired
	SiacTAttoAmmFinRepository siacTAttoAmmRepository;

	@Autowired
	SiacTSoggettoFinRepository siacTSoggettoRepository;

	@Autowired
	SiacTMovgestRepository siacTMovgestRepository;

	@Autowired
	SiacTMovgestTsRepository siacTMovgestTsRepository;

	@Autowired
	SiacTModpagFinRepository siacTModpagRepository;
	
	@Autowired
	SiacRSoggettoRelazFinRepository siacRSoggettoRelazFinRepository;

	@Autowired
	SiacDLiquidazioneStatoRepository siacDLiquidazioneStatoRepository;

	@Autowired
	SiacTMutuoVoceRepository siacTMutuoVoceRepository;

	@Autowired
	SiacDDistintaRepository siacDDistintaRepository;

	@Autowired
	SiacDAmbitoRepository siacDAmbitoRepository;

	@Autowired
	SiacRBilStatoOpRepository siacRBilStatoOpRepository;

	@Autowired
	SiacTMovgestTsDetRepository siacTMovgestTsDetRepository;

	@Autowired
	SiacTOrdinativoTsDetRepository siacTOrdinativoTsDetsRepository;

	@Autowired
	ImpegnoOttimizzatoDad impegnoOttimizzatoDad;
	
	@Autowired
	ImpegnoOptSubDad impegnoOptSubDad;

	@Autowired
	SoggettoFinDad soggettoDad;
	
	@Autowired
	DocumentoSpesaDad documentoSpesaDad;

	@Autowired
	DocumentoSpesaFinDad documentoSpesaFinDad;
	
	@Autowired
	LiquidazioneFinDao liquidazioneDao;
	
	@Autowired
	SiacTBilFinRepository siacTBilRepository;

	@Autowired
	SiacRBilFaseOperativaRepository siacRBilFaseOperativaRepository;
	
	@Autowired
	GeneraSequenceDad generaSequenceDad;
	
	@Autowired
	ImpegnoOttimizzatoDad impegnoDad;
	
	@Autowired
	private StoricoImpegnoAccertamentoDad storicoImpegnoAccertamentoDad;

	/**
	 * Controlli sulla liquidazione da inserire
	 * @param idEnte
	 * @param bilancio
	 * @param liquidazioneInput
	 * @return
	 */
	public EsitoGestioneLiquidazioneDto controlliDiMeritoInserisciLiquidazione(int idEnte, Bilancio bilancio, Liquidazione liquidazioneInput, DatiOperazioneDto datiOperazione) {
		EsitoGestioneLiquidazioneDto esito = new EsitoGestioneLiquidazioneDto();
		List<Errore> listaErrori = new ArrayList<Errore>();

		// Controlli di merito - inizio
		Impegno impegnoliq = liquidazioneInput.getImpegno();
		SubImpegno subImp = null;
		String codiceStatoMovGest = Constanti.convertiStatoMovgest(impegnoliq.getStatoOperativoMovimentoGestioneSpesa());
		
		
		// 	SIAC-3745 CR In INSERIMENTO LIQUIDAZIONE bloccare con errore l'operazione SE sono NULL i seguenti dati: 
		// 1. COFOG     2. SIOPE   3. V livello PdC finanziario 
		Errore erroreClassFinaziaria = controlliClassificazioneFinanziaria(idEnte, liquidazioneInput);
		if(erroreClassFinaziaria!=null){
			return addErroreToList(esito, listaErrori, erroreClassFinaziaria);
		}
		//
		
		if (!Constanti.MOVGEST_STATO_DEFINITIVO.equals(codiceStatoMovGest) && !Constanti.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE.equals(codiceStatoMovGest)) {	
			return addErroreToList(esito, listaErrori, ErroreFin.IMPEGNO_STATO_DEF_O_NON_LIQ.getErrore(""));
		}

		if (impegnoliq.getElencoSubImpegni() != null && !impegnoliq.getElencoSubImpegni().isEmpty()) {
			subImp = impegnoliq.getElencoSubImpegni().get(0);
			
			// jira 3190
			// se dovesse arrivare un sub annullato non lo considero
			if(Constanti.MOVGEST_STATO_ANNULLATO.equals(Constanti.convertiStatoMovgest(subImp.getStatoOperativoMovimentoGestioneSpesa()))){
				subImp = null;
			}
		}

		if (subImp != null && !Constanti.MOVGEST_STATO_DEFINITIVO.equals(Constanti.convertiStatoMovgest(subImp.getStatoOperativoMovimentoGestioneSpesa()))) {
			return addErroreToList(esito, listaErrori,ErroreFin.SUB_IMP_NON_DEFINITIVO.getErrore("") );
		}

		// Ricerca alternativa provvedimento
		AttoAmministrativo attoAmm = liquidazioneInput.getAttoAmministrativoLiquidazione();
		AttoAmministrativo attoAmministrativoEstratto = getAttoAministrativo(idEnte, attoAmm);
		// Jira 2033:
		// METODO inserimento liquidazione: Lo stato della liquidazione deve seguire lo stato della determina SOLO SE non viene passato a parametro, 
		// altrimenti deve assumere il valore specificato nel parametro.
		if(liquidazioneInput.getStatoOperativoLiquidazione()==null ){
			if (attoAmministrativoEstratto != null) {
				liquidazioneInput.setAttoAmministrativoLiquidazione(attoAmministrativoEstratto);
				if (Constanti.STATO_ANNULLATO.equals(attoAmministrativoEstratto.getStatoOperativo())){
					liquidazioneInput.setStatoOperativoLiquidazione(StatoOperativoLiquidazione.ANNULLATO);
				}	
				else if (Constanti.STATO_PROVVISORIO.equals(attoAmministrativoEstratto.getStatoOperativo())){
					liquidazioneInput.setStatoOperativoLiquidazione(StatoOperativoLiquidazione.PROVVISORIO);
				}	
				else{
					liquidazioneInput.setStatoOperativoLiquidazione(StatoOperativoLiquidazione.VALIDO);
					// JIRA- 1836
					//liquidazioneInput.setLiqManuale("M");			
				}	
			} else {
				return addErroreToList(esito, listaErrori,ErroreFin.INCONGRUENZA_NEI_PARAMETRI_.getErrore("Provvedimento"));
			}
		}

		// controllo x stato provvedimento
		if (Constanti.STATO_ANNULLATO.equals(liquidazioneInput.getAttoAmministrativoLiquidazione().getStatoOperativo())) {
			return addErroreToList(esito, listaErrori,ErroreFin.ATTO_AMM_ANNULLATO.getErrore(""));
		}

		// controllo su stato soggetto
		if (liquidazioneInput.getSoggettoLiquidazione().getStatoOperativo() != null) {
			if (StatoOperativoAnagrafica.VALIDO != liquidazioneInput.getSoggettoLiquidazione().getStatoOperativo() && StatoOperativoAnagrafica.SOSPESO != liquidazioneInput.getSoggettoLiquidazione().getStatoOperativo()) {
				return addErroreToList(esito, listaErrori,ErroreFin.SOGGETTO_BLOCCATO.getErrore(""));
			}
		} else {
			Soggetto sogg = soggettoDad.ricercaSoggetto(Constanti.AMBITO_FIN,idEnte, liquidazioneInput.getSoggettoLiquidazione().getCodiceSoggetto(), true, true);
			if (sogg != null) {
				if (StatoEntita.VALIDO != sogg.getStato()) {
					return addErroreToList(esito, listaErrori,ErroreFin.SOGGETTO_BLOCCATO.getErrore(""));
				}
			}
		}


		// controllo su importo liquidazione
		if(!liquidazioneInput.isForza()){
			BigDecimal importoLiq = liquidazioneInput.getImportoLiquidazione();
			if (subImp != null) {
				if (bilancio.getAnno() <= subImp.getAnnoMovimento()) {
					BigDecimal importoLiqSub = subImp.getDisponibilitaLiquidare();
					if (importoLiqSub.compareTo(importoLiq) < 0) {
						return addErroreToList(esito, listaErrori,ErroreFin.MOD_PAGAMENTO_NON_VALIDE.getErrore(""));
					}
				}
			} else {
				BigDecimal importoLiqImp = impegnoliq.getDisponibilitaLiquidare();
				
				if (bilancio.getAnno() <= impegnoliq.getAnnoMovimento()) {
					if (importoLiqImp.compareTo(importoLiq) < 0) {
						return addErroreToList(esito, listaErrori, ErroreFin.MOD_PAGAMENTO_NON_VALIDE.getErrore(""));
					}
				}
			}
		}
		
		//CONTROLLI SIOPE PLUS:
		boolean daImpegno = false;
		List<Errore> errSiopePlus = controlliSiopePlus(liquidazioneInput.getSiopeTipoDebito(), liquidazioneInput.getSiopeAssenzaMotivazione(), liquidazioneInput.getCig(), datiOperazione,daImpegno);
		if(!StringUtils.isEmpty(errSiopePlus)){
			return addErroreToList(esito, listaErrori, errSiopePlus.get(0));
		}
		//

		// Controlli di merito - fine

		esito.setLiquidazione(liquidazioneInput);

		//Termino restituendo l'oggetto di ritorno: 
        return esito;
	}
	
	/**
	 * GIUGNO 2016
	 * 
	 * nuovo metodo che implementa i controlli richiesti dalla CR JIRA SIAC-3745 
	 * 
	 * In INSERIMENTO LIQUIDAZIONE bloccare con errore l'operazione SE sono NULL i seguenti dati:
	 *	1. COFOG
	 *	2. SIOPE
	 *	3. V livello PdC finanziario
	 *	
	 *	Messaggio da emettere in caso di errore:
	 *	COR_ERR_0002 Dato Obbligatorio Omesso (elenco dei dati mancanti separati da virgola) 
	 * 
	 * 
	 * @param idEnte
	 * @param liquidazioneInput
	 * @return
	 */
	public Errore controlliClassificazioneFinanziaria(int idEnte, Liquidazione liquidazioneInput){
		
		Errore errore = null;
		
		//	 	SIAC-3745 CR In INSERIMENTO LIQUIDAZIONE bloccare con errore l'operazione SE sono NULL i seguenti dati: 
			// 1. COFOG     2. SIOPE   3. V livello PdC finanziario 
			
			//1. COFOG
			
		String datiMancanti = "";
		boolean presentiDatiMancanti = false;
		if(StringUtils.isEmpty(liquidazioneInput.getCodCofog())){
			datiMancanti = "COFOG";
			presentiDatiMancanti = true;
		}
		
		//2 SIOPE
		if(StringUtils.isEmpty(liquidazioneInput.getCodSiope())){
			if(presentiDatiMancanti){
				datiMancanti = datiMancanti + ", ";
			}
			datiMancanti = datiMancanti + "SIOPE";
			presentiDatiMancanti = true;
		}
		
		//3. V livello PdC finanziario 
		if(!isPianoDeiContiDiQuintoLivello(idEnte, liquidazioneInput)){
			if(presentiDatiMancanti){
				datiMancanti = datiMancanti + ", ";
			}
			datiMancanti = datiMancanti + "V livello PdC finanziario";
			presentiDatiMancanti = true;
		}
		
		if(presentiDatiMancanti){
			errore = ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore(datiMancanti);
		}
		
		return errore;
	}
	
	private boolean isPianoDeiContiDiQuintoLivello(int idEnte, Liquidazione liquidazioneInput){
		boolean trovatoPianoDeiContiDiQuintoLivello = false;
		String codicePianoDeiConti = liquidazioneInput.getCodPdc();
		if(!StringUtils.isEmpty(codicePianoDeiConti)){
			List<SiacTClassFin> lclass = siacTClassRepository.findByTipoCodesAndEnteAndSelezionato(idEnte,getNow(), Constanti.getCodiciPianoDeiConti(), codicePianoDeiConti);
			if(lclass!=null && lclass.size()>0 && lclass.get(0)!=null && lclass.get(0).getSiacDClassTipo()!=null){
				String codeConfronto = lclass.get(0).getSiacDClassTipo().getClassifTipoCode();
				if(Constanti.D_CLASS_TIPO_PIANO_DEI_CONTI_V.equals(codeConfronto)){
					trovatoPianoDeiContiDiQuintoLivello = true;
				}
			}
		} 
		return trovatoPianoDeiContiDiQuintoLivello;		
	}
	
	/**
	 * Metodo di comodo per aggiungere l'errore all'oggetto contentitore degli errori
	 * @param esito
	 * @param listaErrori
	 * @param e
	 * @return
	 */
	private EsitoGestioneLiquidazioneDto addErroreToList(EsitoGestioneLiquidazioneDto esito,List<Errore> listaErrori,Errore e){
		listaErrori.add(e);
		esito.setListaErrori(listaErrori);
		esito.setLiquidazione(null);
		return esito;
	}
	
	/**
	 * Operazione principale per l'inserimento della liquidazione richiamata dal servizio
	 * Prima dell'operazione di inserimento viene richiamata la funzione dei controlli
	 * 
	 * annoLiquidazioneOrigine: ha senso solo quando from doppia gestione true
	 * 
	 * @param ente
	 * @param richiedente
	 * @param annoEsercizio
	 * @param bilancio
	 * @param liquidazioneInput
	 * @param codice
	 * @param fromDoppiaGestione
	 * @param annoLiquidazioneOrigine
	 * @return
	 */
	public EsitoGestioneLiquidazioneDto operazioneInternaInserisciLiquidazione(Ente ente, Richiedente richiedente, String annoEsercizio, Bilancio bilancio,
			Liquidazione liquidazioneInput, long codice, boolean fromDoppiaGestione, DatiOperazioneDto datiOperazione,Integer annoLiquidazioneOrigine) {
		EsitoGestioneLiquidazioneDto esito = new EsitoGestioneLiquidazioneDto();

		// Operazione interna inserisci liquidazione
		EsitoGestioneLiquidazioneDto esitoControlliInserisciLiq = this.controlliDiMeritoInserisciLiquidazione(ente.getUid(),bilancio, liquidazioneInput,datiOperazione);

		if (esitoControlliInserisciLiq.getListaErrori() != null	&& esitoControlliInserisciLiq.getListaErrori().size() > 0) {
			esitoControlliInserisciLiq.setLiquidazione(null);
			return esitoControlliInserisciLiq;
		}
		Liquidazione liquidazione = esitoControlliInserisciLiq.getLiquidazione();

		EsitoGestioneLiquidazioneDto esitoInserisciLiq = this.inserisciLiquidazione(ente, richiedente, annoEsercizio,bilancio, liquidazione, codice, fromDoppiaGestione,annoLiquidazioneOrigine);

		if (esitoInserisciLiq.getListaErrori() != null && esitoInserisciLiq.getListaErrori().size() > 0) {
			esitoInserisciLiq.setLiquidazione(null);
			return esitoInserisciLiq;
		}
		// Operazione interna inserisci liquidazione

		esito.setLiquidazione(esitoInserisciLiq.getLiquidazione());

		//Termino restituendo l'oggetto di ritorno: 
        return esito;
	}

	/**
	 * Operazione principale per l'inserimento della liquidazione
	 * 
	 * annoLiquidazioneOrigine: ha senso solo quando from doppia gestione true
	 * 
	 * @param ente
	 * @param richiedente
	 * @param annoEsercizio
	 * @param bilancio
	 * @param liquidazioneInput
	 * @param codice
	 * @param fromDoppiaGestione
	 * @param annoLiquidazioneOrigine
	 * @return
	 */
	public EsitoGestioneLiquidazioneDto inserisciLiquidazione(Ente ente,Richiedente richiedente, String annoEsercizio, Bilancio bilancio,
			Liquidazione liquidazioneInput, long codice, boolean fromDoppiaGestione,Integer annoLiquidazioneOrigine) {

		EsitoGestioneLiquidazioneDto esito = new EsitoGestioneLiquidazioneDto();
		List<Errore> listaErrori = new ArrayList<Errore>();
		Liquidazione liqReturn = null;

		String loginOperazione = richiedente.getAccount().getNome();

		//GENNAIO 2018 per REINTROITI NON MI VEDEVA LA LIQUIDAZIONE APPENA INSERITA:
		long currMillisec = getCurrentMilliseconds();
		//long currMillisec = getCurrentMillisecondsTrentaMaggio2017();
		
		Date dateInserimento = new Date(currMillisec);
		Timestamp timestampInserimento = null;
		timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);

		int idEnte = ente.getUid();

		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository.findOne(idEnte);

		SiacDAmbitoFin siacDAmbitoPerCode = siacDAmbitoRepository.findAmbitoByCode(Constanti.AMBITO_FIN, idEnte);
		Integer idAmbito = siacDAmbitoPerCode.getAmbitoId();
		int anno = Integer.parseInt(annoEsercizio);

		long nuovoCode = codice;
		if (!fromDoppiaGestione) {
			//nuovoCode = getMaxCode(ProgressivoType.LIQUIDAZIONE, idEnte,idAmbito, loginOperazione, anno);
			nuovoCode = generaSequenceDad.getMaxCode(ProgressivoType.LIQUIDAZIONE, idEnte,idAmbito, loginOperazione, anno);
		}

		DatiOperazioneDto datiOperazioneDto = new DatiOperazioneDto(currMillisec, Operazione.INSERIMENTO, siacTEnteProprietario, siacDAmbitoPerCode, richiedente.getAccount().getId(), bilancio.getAnno());

		liqReturn = inserisciNuovaLiquidazione(liquidazioneInput,annoEsercizio, loginOperazione, idEnte, idAmbito, bilancio,datiOperazioneDto, timestampInserimento, nuovoCode,fromDoppiaGestione,annoLiquidazioneOrigine);

		// Inserimento liquidazione in doppia gestione
		boolean isdoppiagestione = inserireDoppiaGestione(bilancio,liquidazioneInput, datiOperazioneDto);
		if (isdoppiagestione) {

			// doppia gestione
			Bilancio bilancioPiuUno = new Bilancio();

			List<SiacTBilFin> siacTBilList = siacTBilRepository.getValidoByAnno(idEnte, String.valueOf(anno + 1),datiOperazioneDto.getTs());
			if (siacTBilList != null && siacTBilList.size() > 0 && siacTBilList.get(0) != null) {
				SiacTBilFin siacTBil = siacTBilList.get(0);
				if (siacTBil != null) {
					bilancioPiuUno.setAnno(anno + 1);
					bilancioPiuUno.setUid(siacTBil.getBilId());
				}

				Impegno impegnoOriginale = liquidazioneInput.getImpegno();
				
				
				//APRILE 2016: OTTIMIZZAZIONI CHIAMATA RICERCA IMPEGNO:
				PaginazioneSubMovimentiDto paginazioneSubMovimentiDto = new PaginazioneSubMovimentiDto();
				if(presenzaSub(impegnoOriginale)){
					//Selezionato un SUB 
					BigDecimal numeroSubOriginale = impegnoOriginale.getElencoSubImpegni().get(0).getNumero();
					paginazioneSubMovimentiDto.setNoSub(false);
					paginazioneSubMovimentiDto.setNumeroSubMovimentoRichiesto(numeroSubOriginale);
				} else {
					//Non selezionato un SUB
					paginazioneSubMovimentiDto.setNumeroSubMovimentoRichiesto(null);
					paginazioneSubMovimentiDto.setNoSub(true);
				}
				//
				
				EsitoRicercaMovimentoPkDto esitoRicercaMov = impegnoOttimizzatoDad.ricercaMovimentoPk(richiedente, ente,String.valueOf(anno + 1),impegnoOriginale.getAnnoMovimento(),impegnoOriginale.getNumero(),paginazioneSubMovimentiDto,null,Constanti.MOVGEST_TIPO_IMPEGNO, false);

				//SIAC-6989
				if(esitoRicercaMov==null || esitoRicercaMov.getMovimentoGestione() == null) {
					throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Condizioni per la doppia gestione soddisfatte, ma risulta impossibile trovare l'impegno residuo nell'anno di bilancio successivo."));
				}
				
				Impegno impegnoDoppiaGestione = (Impegno) esitoRicercaMov.getMovimentoGestione();
				
				if (presenzaSub(impegnoOriginale)) {
					BigDecimal numeroSubOriginale = impegnoOriginale.getElencoSubImpegni().get(0).getNumero();

					if (impegnoDoppiaGestione != null && impegnoDoppiaGestione.getElencoSubImpegni() != null && impegnoDoppiaGestione.getElencoSubImpegni().size() > 0) {
						List<SubImpegno> elencoSubImpegniDoppiaGestione = new ArrayList<SubImpegno>();
						for (SubImpegno subImpegno : impegnoDoppiaGestione.getElencoSubImpegni()) {
							if (subImpegno.getNumero().compareTo(numeroSubOriginale) == 0) {
								elencoSubImpegniDoppiaGestione.add(subImpegno);
								break;
							}
						}
						impegnoDoppiaGestione.setElencoSubImpegni(elencoSubImpegniDoppiaGestione);
					}
				} else {
					impegnoDoppiaGestione.setElencoSubImpegni(null);
				}

				liquidazioneInput.setImpegno(impegnoDoppiaGestione);

				BigDecimal disponibilitaLiquidare = BigDecimal.ZERO;
				if (impegnoDoppiaGestione.getElencoSubImpegni() != null) {
					disponibilitaLiquidare = impegnoDoppiaGestione.getElencoSubImpegni().get(0).getDisponibilitaLiquidare();
				} else {
					disponibilitaLiquidare = impegnoDoppiaGestione.getDisponibilitaLiquidare();
				}

				if(!liquidazioneInput.isForza()){
//					Nel caso in cui il chiamante abbia indicato il campo FORZA = TRUE questo controllo 
//					non deve essere effettuato (se non indicato il  campo forza si intende a FALSE)
					if (disponibilitaLiquidare.compareTo(liquidazioneInput.getImportoLiquidazione()) < 0) {
						return addErroreToList(esito, listaErrori, ErroreFin.DISPONIBILITA_INSUFFICIENTE_ORIG.getErrore("Inserimento liquidazione residua"));
					}
				}

				// 	SIAC-6021
				//qui lo passo a null perche' in inserimento non puo'
				//esserci ambiguita' come quando aggiorno una liquidazione 
				//che puo' essere di n anni prima di quello di bilancio
				Integer annoLiquidazioneOriginePerDoppiaGest = null;
				//
				
				// Inserimento liquidazione ribaltata con operazione interna
				EsitoGestioneLiquidazioneDto esitoOperazioneInternaInserisciLiqDG = this.operazioneInternaInserisciLiquidazione(ente,richiedente, String.valueOf(anno + 1),bilancioPiuUno, liquidazioneInput, nuovoCode,true,datiOperazioneDto,annoLiquidazioneOriginePerDoppiaGest);

				if (esitoOperazioneInternaInserisciLiqDG.getListaErrori() != null && esitoOperazioneInternaInserisciLiqDG.getListaErrori().size() > 0) {
					esitoOperazioneInternaInserisciLiqDG.setLiquidazione(null);
					return esitoOperazioneInternaInserisciLiqDG;
				}
			}

			// doppia gestione
		}

		SiacTLiquidazioneFin siacTLiquidazioneInsertFull = siacTLiquidazioneRepository.findOne(liqReturn.getIdLiquidazione());
		liqReturn = riempiDatiLiquidazioneReturn(siacTLiquidazioneInsertFull,liqReturn, idEnte);

		esito.setLiquidazione(liqReturn);
		esito.setListaErrori(listaErrori);

		

		//Termino restituendo l'oggetto di ritorno: 
        return esito;
	}
	
	private boolean presenzaSub(Impegno impegno){
		boolean presenzaSub = false;
		if(impegno != null && impegno.getElencoSubImpegni() != null && impegno.getElencoSubImpegni().size() > 0){
			presenzaSub = true;
		}
		return presenzaSub;
	}

	/**
	 * Operazione di inserimento effettivo della liquidazione.
	 * 
	 * annoLiquidazioneOrigine: ha senso solo quando from doppia gestione true
	 * 
	 * @param liquidazioneInput
	 * @param annoEsercizio
	 * @param loginOperazione
	 * @param idEnte
	 * @param idAmbito
	 * @param bilancio
	 * @param datiOperazioneDto
	 * @param timestampInserimento
	 * @param nuovoCode
	 * @param fromDoppiaGestione
	 * @param annoLiquidazioneOrigine
	 * @return
	 */
	private Liquidazione inserisciNuovaLiquidazione(
			Liquidazione liquidazioneInput, String annoEsercizio,
			String loginOperazione,  int idEnte, Integer idAmbito,
			Bilancio bilancio, DatiOperazioneDto datiOperazioneDto,
			Timestamp timestampInserimento, long nuovoCode,
			boolean fromDoppiaGestione, Integer annoLiquidazioneOrigine) {
		Liquidazione liqReturn = null;

		// 1 insert liquidazione SiacTLiquidazioneFin
		SiacTLiquidazioneFin siacTLiquidazioneInsert = new SiacTLiquidazioneFin();
		siacTLiquidazioneInsert = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTLiquidazioneInsert,datiOperazioneDto, siacTAccountRepository);
		
		SiacTBilFin siacTBilNew = siacTBilRepository.findOne(bilancio.getUid());

		siacTLiquidazioneInsert.setSiacTBil(siacTBilNew);
		Integer liqAnno = Integer.decode(annoEsercizio);
		if (fromDoppiaGestione) {
			//MARZO 2018 - FIX per SIAC-6021:
			if(CommonUtils.maggioreDiZero(annoLiquidazioneOrigine)){
				//se indicato un annoLiquidazioneOrigine lo usiamo:
				liqAnno = annoLiquidazioneOrigine;
			} else {
				//nel caso non e' stato indicato esplicitamente annoLiquidazioneOrigine 
				//facciamo come sempre
				liqAnno = liqAnno - 1;
			}
		}
		siacTLiquidazioneInsert.setLiqAnno(liqAnno);

		if (liquidazioneInput.getDescrizioneLiquidazione() != null){
			siacTLiquidazioneInsert.setLiqDesc(liquidazioneInput.getDescrizioneLiquidazione().toUpperCase());
		}

		siacTLiquidazioneInsert.setLiqImporto(liquidazioneInput.getImportoLiquidazione());

		if (liquidazioneInput.getLiqManuale() != null && Constanti.LIQUIDAZIONE_MAUALE.equalsIgnoreCase(liquidazioneInput.getLiqManuale())) {
			siacTLiquidazioneInsert.setLiqConvalidaManuale(Constanti.LIQUIDAZIONE_MAUALE_CODE);
		} else if (liquidazioneInput.getLiqManuale() != null && Constanti.LIQUIDAZIONE_AUTOMATICA.equalsIgnoreCase(liquidazioneInput.getLiqManuale())) {
			siacTLiquidazioneInsert.setLiqConvalidaManuale(Constanti.LIQUIDAZIONE_AUTOMATICA_CODE);
		}
		
		
		
		// JIRA 1976 IMPORTANTE RM verificata con laura la mancata gestione in inserimento dell'informazione liqAutomatica
		// sarà = S se il ser è chiamato da inserisciOrdinativoPagamento 
		// sarà = N se il ser è richiamato dal cdu
		if(!StringUtils.isEmpty(siacTLiquidazioneInsert.getLiqAutomatica())){
			siacTLiquidazioneInsert.setLiqAutomatica(liquidazioneInput.getLiqAutomatica());
		} else siacTLiquidazioneInsert.setLiqAutomatica(Constanti.LIQUIDAZIONE_LIQ_AUTOMATICA_NO); 

		siacTLiquidazioneInsert.setLiqEmissioneData(timestampInserimento);
		siacTLiquidazioneInsert.setLiqNumero(new BigDecimal(nuovoCode));

		// 1.1 ID contotesoreria in siacTLiquidazione
		ContoTesoreria contoTes = liquidazioneInput.getContoTesoreria();

		if (contoTes != null && contoTes.getCodice() != null && !contoTes.getCodice().isEmpty()) {
			SiacDContotesoreriaFin siacDContotesoreria = siacDContotesoreriaRepository.findContotesoreriaByCode(idEnte, contoTes.getCodice(),timestampInserimento);
			if (siacDContotesoreria != null){
				siacTLiquidazioneInsert.setSiacDContotesoreria(siacDContotesoreria);
			}
		}else{
			List<SiacDContotesoreriaFin> siacDContotesoreriaList = siacDContotesoreriaRepository.findContotesoreriaByEnte(idEnte, timestampInserimento);
			if (siacDContotesoreriaList != null && !siacDContotesoreriaList.isEmpty()){
				SiacDContotesoreriaFin siacDContotesoreria = siacDContotesoreriaList.get(0);
				siacTLiquidazioneInsert.setSiacDContotesoreria(siacDContotesoreria);
			}
			
		}

		// 1.2 ID distinta in siacTLiquidazione
		Distinta distinta = liquidazioneInput.getDistinta();
		if (distinta != null && distinta.getCodice() != null && !distinta.getCodice().isEmpty()) {
			SiacDDistintaFin siacDDistinta = siacDDistintaRepository.findDistintaByCode(idEnte, distinta.getCodice(),timestampInserimento, Constanti.D_DISTINTA_TIPO_SPESA);
			if (siacDDistinta != null){
				siacTLiquidazioneInsert.setSiacDDistinta(siacDDistinta);
			}
		}else{
			List<SiacDDistintaFin> siacDDistintaList = siacDDistintaRepository.findDistintaByCodTipo(idEnte,timestampInserimento, Constanti.D_DISTINTA_TIPO_SPESA); // uso questa perchè gia esiste
			if (siacDDistintaList != null && !siacDDistintaList.isEmpty()){
				SiacDDistintaFin siacDDistinta = siacDDistintaList.get(0);
				siacTLiquidazioneInsert.setSiacDDistinta(siacDDistinta);
			}
			
		}

		List<ModalitaPagamentoSoggetto> modPagSoggList = liquidazioneInput.getSoggettoLiquidazione().getModalitaPagamentoList();
		// jira 2874
		// salvo semplicemente l'id che arriva sulla liquidazione, in lettura andrò poi a discriminare
		// è stata eliminata la FK verso la siacTmodpag
		impostaModalitaDiPagamentoByTipoAccredito(siacTLiquidazioneInsert,	modPagSoggList);
		
		//SIOPE PLUS:
		// (attenzione: siope plus va qui prima del saveAndFlush su siacTLiquidazioneInsert
		//  perche' sono colonne di fk dentro SiacTLiquidazioneFin )
		impostaDatiSiopePlus(siacTLiquidazioneInsert, liquidazioneInput, datiOperazioneDto);
		
		//salvo sul db:
		siacTLiquidazioneInsert = siacTLiquidazioneRepository.saveAndFlush(siacTLiquidazioneInsert);
		
		AttributoTClassInfoDto attributoInfo = new AttributoTClassInfoDto();
		attributoInfo.setTipoOggetto(OggettoDellAttributoTClass.T_LIQUIDAZIONE);
		attributoInfo.setSiacTLiquidazione(siacTLiquidazioneInsert);

		if (siacTLiquidazioneInsert != null && siacTLiquidazioneInsert.getLiqId() != null) {

			// 1.4 stato operativo Liquidazione

			SiacDLiquidazioneStatoFin siacDLiquidazioneStato = new SiacDLiquidazioneStatoFin();
			String statoOP = Constanti.statoOperativoLiquidazioneEnumToString(liquidazioneInput.getStatoOperativoLiquidazione());
			siacDLiquidazioneStato = siacDLiquidazioneStatoRepository.findDLiquidazioneStatoByCodeAndEnte(idEnte, statoOP,timestampInserimento);

			SiacRLiquidazioneStatoFin siacRLiquidazioneStato = new SiacRLiquidazioneStatoFin();
			siacRLiquidazioneStato = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRLiquidazioneStato,datiOperazioneDto, siacTAccountRepository);
			siacRLiquidazioneStato.setSiacTLiquidazione(siacTLiquidazioneInsert);
			siacRLiquidazioneStato.setSiacDLiquidazioneStato(siacDLiquidazioneStato);
			//salvo sul db:
			siacRLiquidazioneStatoRepository.saveAndFlush(siacRLiquidazioneStato);

			// 1.5 insert attributi

			String cig = liquidazioneInput.getCig();
			if (cig != null && !cig.isEmpty()) {
				salvaAttributoCig(attributoInfo, datiOperazioneDto,cig.toUpperCase());
			}

			// 2 Insert Impegno-Liquidazione in relazione
			// siacRLiquidazioneMovgestRepository

			SiacRLiquidazioneMovgestFin siacRLiquidazioneMovgest = new SiacRLiquidazioneMovgestFin();

			siacRLiquidazioneMovgest = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRLiquidazioneMovgest,datiOperazioneDto, siacTAccountRepository);

			siacRLiquidazioneMovgest.setSiacTLiquidazione(siacTLiquidazioneInsert);

			Impegno impInput = liquidazioneInput.getImpegno();
			int impegnoID;
			List<SiacTMovgestTsFin> siacTMovgestTsList = null;
			
			if (impInput != null) {
				
				// 2.1 Insert Subimpegno Voce di Mutuo-Liquidazione in
				// relazionee
				// siacRMutuoVoceLiquidazioneRepository se presente
				List<SubImpegno> subImpList = impInput.getElencoSubImpegni();

				if (subImpList != null && !subImpList.isEmpty()) {
					SubImpegno subImpInput = subImpList.get(0);
					String codeSubImp = subImpInput.getNumero().toString();
					List<SiacTMovgestTsFin> l = siacTMovgestTsRepository.findSubMovgestTsByCodeAndMovgestId(idEnte,timestampInserimento,subImpInput.getIdMovimentoPadre(),codeSubImp);
					if (l != null && l.size() > 0) {
						SiacTMovgestTsFin siacTMovgestTsSubImpegno = l.get(0);

						SiacRLiquidazioneMovgestFin siacRLiquidazioneMovgestSub = new SiacRLiquidazioneMovgestFin();

						siacRLiquidazioneMovgestSub = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRLiquidazioneMovgestSub,datiOperazioneDto,siacTAccountRepository);

						siacRLiquidazioneMovgestSub.setSiacTLiquidazione(siacTLiquidazioneInsert);

						siacRLiquidazioneMovgestSub.setSiacTMovgestTs(siacTMovgestTsSubImpegno);
						//salvo sul db:
						siacRLiquidazioneMovgest = siacRLiquidazioneMovgestRepository.saveAndFlush(siacRLiquidazioneMovgestSub);

					}

					// 2.2 Insert Voce di Mutuo Impegno - Liquidazione in
					// relazione siacRMutuoVoceLiquidazioneRepository
					List<VoceMutuo> vociMutuoListSubImp = subImpInput.getListaVociMutuo();
					if (vociMutuoListSubImp != null && !vociMutuoListSubImp.isEmpty()) {
						VoceMutuo voceMutuoSubImpegno = new VoceMutuo();
						//System.out.println("liquidazione.numeroMutuo: " + liquidazioneInput.getNumeroMutuo());
						
						for(VoceMutuo voceMutuo: vociMutuoListSubImp){
							//System.out.println("vocediMutuo.numeroMutuo: " + voceMutuo.getNumeroMutuo());
							if(voceMutuo.getNumeroMutuo().equals(String.valueOf(liquidazioneInput.getNumeroMutuo()))){
								voceMutuoSubImpegno = voceMutuo;
								break;
							}
						}
						
						if (voceMutuoSubImpegno != null) {
							SiacTMutuoVoceFin siacTMutuoVoceSub = siacTMutuoVoceRepository.findOne(voceMutuoSubImpegno.getIdVoceMutuo().intValue());

							if (siacTMutuoVoceSub != null) {
								SiacRMutuoVoceLiquidazioneFin siacRMutuoVoceLiquidazione = new SiacRMutuoVoceLiquidazioneFin();
								siacRMutuoVoceLiquidazione = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRMutuoVoceLiquidazione,datiOperazioneDto,siacTAccountRepository);
								siacRMutuoVoceLiquidazione.setSiacTLiquidazione(siacTLiquidazioneInsert);
								siacRMutuoVoceLiquidazione.setSiacTMutuoVoce(siacTMutuoVoceSub);
								//salvo sul db:
								siacRMutuoVoceLiquidazioneRepository.saveAndFlush(siacRMutuoVoceLiquidazione);
							}
						}
					}

				} else {

					SiacTMovgestFin siacTMovgest = siacTMovgestRepository.ricercaSiacTMovgestPk(idEnte, annoEsercizio,impInput.getAnnoMovimento(),impInput.getNumero(),Constanti.MOVGEST_TIPO_IMPEGNO);
					impegnoID = siacTMovgest.getUid();
					
					siacTMovgestTsList = siacTMovgestTsRepository.findMovgestTsByMovgest(idEnte,timestampInserimento, impegnoID);

					if (siacTMovgestTsList != null && siacTMovgestTsList.size() > 0) {
						SiacTMovgestTsFin siacTMovgestTs = siacTMovgestTsList.get(0);
						siacRLiquidazioneMovgest.setSiacTMovgestTs(siacTMovgestTs);
						//salvo sul db:
						siacRLiquidazioneMovgest = siacRLiquidazioneMovgestRepository.saveAndFlush(siacRLiquidazioneMovgest);
					}

					// 2.3 Insert Voce di Mutuo Impegno - Liquidazione in
					// relazione siacRMutuoVoceLiquidazioneRepository

					List<VoceMutuo> vociMutuoListImp = impInput.getListaVociMutuo();

					if (vociMutuoListImp != null && !vociMutuoListImp.isEmpty()) {
						//VoceMutuo voceMutuoImpegno = vociMutuoListImp.get(0);
						VoceMutuo voceMutuoImpegno = new VoceMutuo();
						//System.out.println("liquidazione.numeroMutuo: " + liquidazioneInput.getNumeroMutuo());
						
						for(VoceMutuo voceMutuo: vociMutuoListImp){
							//System.out.println("vocediMutuo.numeroMutuo: " + voceMutuo.getNumeroMutuo());
							
							if(voceMutuo.getNumeroMutuo().equals(String.valueOf(liquidazioneInput.getNumeroMutuo()))){
								voceMutuoImpegno = voceMutuo;
								break;
							}
						}
						
						if (voceMutuoImpegno != null) {
							SiacTMutuoVoceFin siacTMutuoVoce = siacTMutuoVoceRepository.findOne(voceMutuoImpegno.getIdVoceMutuo().intValue());
							if (siacTMutuoVoce != null) {
								SiacRMutuoVoceLiquidazioneFin siacRMutuoVoceLiquidazione = new SiacRMutuoVoceLiquidazioneFin();
								siacRMutuoVoceLiquidazione = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRMutuoVoceLiquidazione,datiOperazioneDto,siacTAccountRepository);
								siacRMutuoVoceLiquidazione.setSiacTLiquidazione(siacTLiquidazioneInsert);
								siacRMutuoVoceLiquidazione.setSiacTMutuoVoce(siacTMutuoVoce);
								//salvo sul db:
								siacRMutuoVoceLiquidazioneRepository.saveAndFlush(siacRMutuoVoceLiquidazione);
							}
						}
					}
				}
			}

			// 6 Insert Soggetto-Liquidazione in relazione
			// siacRLiquidazioneSoggettoRepository

			SiacRLiquidazioneSoggettoFin siacRLiquidazioneSoggetto = new SiacRLiquidazioneSoggettoFin();

			Soggetto soggInput = liquidazioneInput.getSoggettoLiquidazione();
			SiacTSoggettoFin siacTSoggetto = null;

			if (soggInput != null) {
				// 7 Insert Sede Secondaria Soggetto-Liquidazione in relazione
				// siacRLiquidazioneSoggettoRepository se presente
				List<SedeSecondariaSoggetto> listaSediSec = soggInput.getSediSecondarie();
				SedeSecondariaSoggetto sedeSecondariaSoggetto = null;
				if (listaSediSec != null && !listaSediSec.isEmpty()) {
					sedeSecondariaSoggetto = listaSediSec.get(0);
				}
				String codSoggetto = soggInput.getCodiceSoggetto();
				if (sedeSecondariaSoggetto != null) {
					SiacTSoggettoFin sedeSecondariaEntity = siacTSoggettoRepository.findOne(sedeSecondariaSoggetto.getUid());
					if (sedeSecondariaEntity != null) {
						SiacRLiquidazioneSoggettoFin siacRLiquidazioneSoggettoSedeSec = new SiacRLiquidazioneSoggettoFin();
						siacRLiquidazioneSoggetto = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRLiquidazioneSoggettoSedeSec,datiOperazioneDto,siacTAccountRepository);
						siacRLiquidazioneSoggettoSedeSec.setSiacTLiquidazione(siacTLiquidazioneInsert);
						siacRLiquidazioneSoggettoSedeSec.setSiacTSoggetto(sedeSecondariaEntity);
						//salvo sul db:
						siacRLiquidazioneSoggettoRepository.saveAndFlush(siacRLiquidazioneSoggettoSedeSec);
					}

				} else {

					siacTSoggetto = siacTSoggettoRepository.ricercaSoggettoNoSeSede(datiOperazioneDto.getSiacDAmbito().getAmbitoCode(), idEnte, codSoggetto,Constanti.SEDE_SECONDARIA, getNow());
					if (siacTSoggetto != null) {
						siacRLiquidazioneSoggetto = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRLiquidazioneSoggetto,datiOperazioneDto,siacTAccountRepository);
						siacRLiquidazioneSoggetto.setSiacTLiquidazione(siacTLiquidazioneInsert);
						siacRLiquidazioneSoggetto.setSiacTSoggetto(siacTSoggetto);
						//salvo sul db:
						siacRLiquidazioneSoggettoRepository.saveAndFlush(siacRLiquidazioneSoggetto);
					}

				}

			}

			// 8 atto_amministrativo_liquidazione (siac_r_atto_amm +
			// siac_t_atto_amm)

			SiacTAttoAmmFin siacTAttoAmm = getAttoAmministrativo(liquidazioneInput, idEnte, timestampInserimento);
			
			if (siacTAttoAmm != null) {
				SiacRLiquidazioneAttoAmmFin siacRLiquidazioneAttoAmm = new SiacRLiquidazioneAttoAmmFin();
				siacRLiquidazioneAttoAmm = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRLiquidazioneAttoAmm,datiOperazioneDto, siacTAccountRepository);
				siacRLiquidazioneAttoAmm.setSiacTAttoAmm(siacTAttoAmm);
				siacRLiquidazioneAttoAmm.setSiacTLiquidazione(siacTLiquidazioneInsert);
				//salvo sul db:
				siacRLiquidazioneAttoAmmRepository.saveAndFlush(siacRLiquidazioneAttoAmm);
			}

			salvaTransazioneElementare(attributoInfo, datiOperazioneDto,liquidazioneInput);
			//
			liqReturn = new Liquidazione();
			liqReturn.setIdLiquidazione(siacTLiquidazioneInsert.getLiqId());
			liqReturn.setNumeroLiquidazione(siacTLiquidazioneInsert.getLiqNumero());
			liqReturn.setAnnoLiquidazione(siacTLiquidazioneInsert.getLiqAnno());
		}
		//Termino restituendo l'oggetto di ritorno: 
        return liqReturn;
	}
	
	/**
	 * imposta i dati di siope plus in un siacTMovgestTs in inserimento o aggiornamento
	 * a partire da quanto ricevuto in impegno
	 * @param siacTMovgestTs
	 * @param impegno
	 * @param datiOperazioneDto
	 */
	private void impostaDatiSiopePlus(SiacTLiquidazioneFin siacTLiquidazioneFin, Liquidazione liquidazione, DatiOperazioneDto datiOperazioneDto){
		
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getUid();
		
		boolean inAggiornamentoMovimento = false; //inserimento imp / sub
		if(siacTLiquidazioneFin.getUid()!=null && siacTLiquidazioneFin.getUid()>0){
			inAggiornamentoMovimento = true;  //aggiornamento imp / sub
		}
		
		//SIOPE TIPO DEBITO
		SiopeTipoDebito siopeTipoDebito = liquidazione.getSiopeTipoDebito();
		if(siopeTipoDebito!=null && !StringUtils.isEmpty(siopeTipoDebito.getCodice())){
			//recuperiamo il record della codifica ricevuta:
			List<SiacDSiopeTipoDebitoFin> dstdebitos = siacDSiopeTipoDebitoFinRepository.findByCode(idEnte, datiOperazioneDto.getTs(), siopeTipoDebito.getCodice());
			SiacDSiopeTipoDebitoFin siacDSiopeTipoDebito = CommonUtils.getFirst(dstdebitos);
			if(siacDSiopeTipoDebito!=null){
				siacTLiquidazioneFin.setSiacDSiopeTipoDebitoFin(siacDSiopeTipoDebito);
			}
		} else if(inAggiornamentoMovimento){
			//sto aggiornando un movimento e non ho ricevuto un tipo debito, cosa faccio?
			//lo tolgo oppure tengo quello che eventualmente gia' c'e'?
			//per ora nulla..
		}
		
		//MOTIVAZIONE ASSENZA CIG
		SiopeAssenzaMotivazione siopeAssenzaMotivazione = liquidazione.getSiopeAssenzaMotivazione();
		if(siopeAssenzaMotivazione!=null && !StringUtils.isEmpty(siopeAssenzaMotivazione.getCodice())){
			//recuperiamo il record della codifica ricevuta:
			List<SiacDSiopeAssenzaMotivazioneFin> aMtvs = siacDSiopeAssenzaMotivazioneFinRepository.findByCode(idEnte, datiOperazioneDto.getTs(), siopeAssenzaMotivazione.getCodice());
			SiacDSiopeAssenzaMotivazioneFin siacDSiopeAssenzaMotivazione = CommonUtils.getFirst(aMtvs);
			//puo' essere nullato a piacere (siacDSiopeAssenzaMotivazione puo' essere null):
			siacTLiquidazioneFin.setSiacDSiopeAssenzaMotivazione(siacDSiopeAssenzaMotivazione);
		} else {
			//puo' essere nullato a piacere (siopeAssenzaMotivazione puo' essere null):
			siacTLiquidazioneFin.setSiacDSiopeAssenzaMotivazione(null);
		}
	}

	protected void impostaModalitaDiPagamentoByTipoAccredito(SiacTLiquidazioneFin siacTLiquidazione, List<ModalitaPagamentoSoggetto> modPagSoggList) {
		final String methodName = "impostaModalitaDiPagamentoByTipoAccredito";
		
		if (modPagSoggList != null && !modPagSoggList.isEmpty()) {
			ModalitaPagamentoSoggetto modPag = modPagSoggList.get(0);
			
				
				if (modPag != null) {
					
					ModalitaAccreditoSoggetto modalitaAccreditoSoggetto = modPag.getModalitaAccreditoSoggetto();
					
					
					if(modalitaAccreditoSoggetto!=null){
					
						log.debug(methodName," verifico la modalitaAccreditoSoggetto " + modalitaAccreditoSoggetto.getCodice() );	
						
						if(!modalitaAccreditoSoggetto.getCodice().equals(TipoAccredito.CSI.toString())&&
								!modalitaAccreditoSoggetto.getCodice().equals(TipoAccredito.FA.toString()) &&
								!modalitaAccreditoSoggetto.getCodice().equals(TipoAccredito.PI.toString()) &&
								!modalitaAccreditoSoggetto.getCodice().equals(TipoAccredito.CPT.toString()) &&
								!modalitaAccreditoSoggetto.getCodice().equals(TipoAccredito.CSIG.toString()) &&
								!modalitaAccreditoSoggetto.getCodice().equals(TipoAccredito.SU.toString()) &&
								!modalitaAccreditoSoggetto.getCodice().equals(TipoAccredito.CSC.toString())){
							// inserisco in modpag_id
							
							//SiacTModpagFin siacTModpag = siacTModpagRepository.findOne(modPag.getUid());
							//siacTLiquidazioneInsert.setSiacTModpag(siacTModpag);
							SiacTModpagFin siacTModpag = new SiacTModpagFin();
							siacTModpag.setUid(modPag.getUid());
							siacTLiquidazione.setSiacTModpag(siacTModpag);
							
							siacTLiquidazione.setCessioneId(null);
							
						}else{
					
							// inserisco in modpag_cessione_id
							siacTLiquidazione.setSiacTModpag(null);
							siacTLiquidazione.setCessioneId(modPag.getUid());
						}
					}else{
						throw new BusinessException("inserisciNuovaLiquidazione: Errore!  modalitaAccreditoSoggetto e' nulla ! ");
					}
				}

		}
	}

	/**
	 * Operazione che crea un oggetto Liquidazione a fronte di dati della SiacTLiquidazioneFin
	 * 
	 * @param siacTLiquidazioneInsertFull
	 * @param liqReturn
	 * @param idEnte
	 * @return
	 */
	private Liquidazione riempiDatiLiquidazioneReturn(SiacTLiquidazioneFin siacTLiquidazioneInsertFull,Liquidazione liqReturn, int idEnte) {

		long currMillisec = getCurrentMillisecondsTrentaMaggio2017();
		Date dateInserimento = new Date(currMillisec);
		Timestamp timestampInserimento = null;
		timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);

		liqReturn = map(siacTLiquidazioneInsertFull, Liquidazione.class,FinMapId.SiacTLiquidazione_Liquidazione);

		Distinta distinta = null;
		SiacDDistintaFin siacDDistinta = siacTLiquidazioneInsertFull.getSiacDDistinta();
		if (siacDDistinta != null) {
			distinta = map(siacDDistinta, Distinta.class,FinMapId.SiacDDistinta_Distinta);
			liqReturn.setDistinta(distinta);
		}

		ContoTesoreria contoTesoreria = null;
		SiacDContotesoreriaFin siacDContotesoreria = siacTLiquidazioneInsertFull.getSiacDContotesoreria();

		if (siacDContotesoreria != null) {
			contoTesoreria = map(siacDContotesoreria, ContoTesoreria.class,FinMapId.SiacDContotesoreria_Contotesoreria);
			liqReturn.setContoTesoreria(contoTesoreria);
		}

		List<SiacRLiquidazioneStatoFin> siacRLiquidazioneStatoList = siacRLiquidazioneStatoRepository.findByEnteAndLiquidazione(idEnte, timestampInserimento,liqReturn.getIdLiquidazione());
		if (siacRLiquidazioneStatoList != null && !siacRLiquidazioneStatoList.isEmpty()){
			SiacRLiquidazioneStatoFin siacRLiquidazioneStato = siacRLiquidazioneStatoList.get(0);
			if(siacRLiquidazioneStato != null){
				SiacDLiquidazioneStatoFin dliqStato = siacRLiquidazioneStato.getSiacDLiquidazioneStato();
				String statocod = dliqStato.getLiqStatoCode();
				if(statocod != null) {
					if (Constanti.D_LIQUIDAZIONE_STATO_VALIDO.endsWith(statocod)){
						liqReturn.setStatoOperativoLiquidazione(StatoOperativoLiquidazione.VALIDO);
					} else if (Constanti.D_LIQUIDAZIONE_STATO_ANNULLATO.endsWith(statocod)){
						liqReturn.setStatoOperativoLiquidazione(StatoOperativoLiquidazione.ANNULLATO);
					}else{
						liqReturn.setStatoOperativoLiquidazione(StatoOperativoLiquidazione.PROVVISORIO);
					}
				}
			}
		}

		String codSogg = "";
		List<SiacRLiquidazioneSoggettoFin> soggetoList = siacRLiquidazioneSoggettoRepository.findByEnteAndLiquidazione(idEnte, timestampInserimento,liqReturn.getIdLiquidazione());
		if (soggetoList != null && !soggetoList.isEmpty()) {
			SiacRLiquidazioneSoggettoFin siacRLiquidazioneSoggetto = soggetoList.get(0);
			if (siacRLiquidazioneSoggetto != null) {
				SiacTSoggettoFin siacTSoggetto = siacRLiquidazioneSoggetto.getSiacTSoggetto();
				if (siacTSoggetto != null) {
					Soggetto soggetto = new Soggetto();
					soggetto.setUid(siacTSoggetto.getSoggettoId());
					soggetto.setCodiceSoggetto(siacTSoggetto.getSoggettoCode());
					soggetto.setCodiceFiscale(siacTSoggetto.getCodiceFiscale());
					soggetto.setPartitaIva(siacTSoggetto.getPartitaIva());
					soggetto.setDenominazione(siacTSoggetto.getSoggettoDesc());
					liqReturn.setSoggettoLiquidazione(soggetto);
					codSogg = siacTSoggetto.getSoggettoCode();
				}
			}
		}

		
		// jira 2874
		// qui devo controllare se ho l'id nella modpad, se non esiste vuol dire che ho una cessione
		// SiacTModpagFin mpag = siacTLiquidazioneInsertFull.getSiacTModpag();
		Integer mpag  = null;
		
		if(siacTLiquidazioneInsertFull.getSiacTModpag()!=null && siacTLiquidazioneInsertFull.getSiacTModpag().getUid()!=null){
			mpag = siacTLiquidazioneInsertFull.getSiacTModpag().getUid();
		}else{
			mpag = siacTLiquidazioneInsertFull.getCessioneId();
		}
			
		// List<ModalitaPagamentoSoggetto> modalitaPagamentoSoggettoList = soggettoDad.ricercaModalitaPagamentoPerChiave(Constanti.AMBITO_FIN, idEnte, codSogg , mpag.getUid(), null);
		List<ModalitaPagamentoSoggetto> modalitaPagamentoSoggettoList = soggettoDad.ricercaModalitaPagamentoPerChiave(Constanti.AMBITO_FIN, idEnte, codSogg , mpag, null);
		if(modalitaPagamentoSoggettoList!=null && !modalitaPagamentoSoggettoList.isEmpty()){
			liqReturn.setModalitaPagamentoSoggetto(modalitaPagamentoSoggettoList.get(0));
		}else{
			throw new BusinessException("Impossibile ottenere la modalita' di pagamento associata alla liquidazione [uid liquidazione: " + liqReturn.getUid() + "]  , " +
						"modalita pagamento uid [ "+ mpag  +"] ");
		}
			

		//ATTO AMMINISTRATIVO:
		List<SiacRLiquidazioneAttoAmmFin> siacRLiquidazioneAttoAmmList = siacRLiquidazioneAttoAmmRepository.findByEnteAndLiquidazione(idEnte, timestampInserimento,liqReturn.getIdLiquidazione());
		liqReturn = settaAttoAmministrativoInLiquidazione(siacRLiquidazioneAttoAmmList, liqReturn);
		

		List<SiacRLiquidazioneMovgestFin> siacRLiquidazioneMovgestRepositoryList = siacRLiquidazioneMovgestRepository.findByEnteAndLiquidazione(idEnte, timestampInserimento,liqReturn.getIdLiquidazione());
		if (siacRLiquidazioneMovgestRepositoryList != null && !siacRLiquidazioneMovgestRepositoryList.isEmpty()) {
			SiacRLiquidazioneMovgestFin siacRLiquidazioneMovgest = siacRLiquidazioneMovgestRepositoryList.get(0);
			if (siacRLiquidazioneMovgest != null) {
				SiacTMovgestTsFin movgestTS = siacRLiquidazioneMovgest.getSiacTMovgestTs();
				if (movgestTS != null) {
					Impegno impegno = new Impegno();
					Integer idPadre = movgestTS.getMovgestTsIdPadre();
					if (idPadre != null) {
						SubImpegno subImpegno = new SubImpegno();
						List<SubImpegno> listSub = new ArrayList<SubImpegno>();

						if (movgestTS.getSiacTMovgest() != null) {
							// Impegno
							impegno.setUid(movgestTS.getSiacTMovgest().getUid());
							impegno.setAnnoMovimento(movgestTS.getSiacTMovgest().getMovgestAnno());
							impegno.setDescrizione(movgestTS.getSiacTMovgest().getMovgestDesc());
							impegno.setNumero(movgestTS.getSiacTMovgest().getMovgestNumero());
						}

						// SubImpegno
						subImpegno.setNumero(new BigDecimal(movgestTS.getMovgestTsCode()));
						subImpegno.setDescrizione(movgestTS.getMovgestTsDesc());
						subImpegno.setUid(movgestTS.getMovgestTsId());

						listSub.add(subImpegno);
						impegno.setElencoSubImpegni(listSub);

					} else {
						if (movgestTS.getSiacTMovgest() != null) {
							impegno.setNumero(movgestTS.getSiacTMovgest().getMovgestNumero());
							impegno.setUid(movgestTS.getSiacTMovgest().getUid());
							impegno.setAnnoMovimento(movgestTS.getSiacTMovgest().getMovgestAnno());
							impegno.setDescrizione(movgestTS.getSiacTMovgest().getMovgestDesc());
						}
					}

					// Capitolo
					List<SiacRMovgestBilElemFin> listSiacRMovgestBilElem = movgestTS.getSiacTMovgest().getSiacRMovgestBilElems();
					if (listSiacRMovgestBilElem != null && !listSiacRMovgestBilElem.isEmpty()) {

						for (SiacRMovgestBilElemFin rMovgestBilElem : listSiacRMovgestBilElem) {
							if (rMovgestBilElem != null) {
								if (rMovgestBilElem.getDataFineValidita() == null) {
									if (rMovgestBilElem.getSiacTBilElem() != null){
										impegno.setChiaveCapitoloUscitaGestione(rMovgestBilElem.getSiacTBilElem().getElemId());
									}
								}
							}
						}
					}

					liqReturn.setImpegno(impegno);

				}
			}
		}

		return liqReturn;
	}

	/**
	 * Metodo per la gestione del residuo sull'aggiornamento della liquidazione
	 * 
	 * @param bilancio
	 * @param liq
	 * @param datiOperazioneDto
	 * @return
	 */
	private boolean aggiornareDoppiaGestione(Bilancio bilancio,Liquidazione liq, DatiOperazioneDto datiOperazioneDto) {
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		boolean statoLiqValido = false;
		if (liq.getStatoOperativoLiquidazione() == StatoOperativoLiquidazione.VALIDO || liq.getStatoOperativoLiquidazione() == StatoOperativoLiquidazione.ANNULLATO) {
			statoLiqValido = true;
		} else {
			return false;
		}

		boolean statoBilancioPerDoggiaGest = false;
		String annoBil = Integer.toString(bilancio.getAnno());
		SiacTBilFin siacTBil = siacTBilRepository.getValidoByAnno(idEnte, annoBil,datiOperazioneDto.getTs()).get(0);

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
			String bilCode = siacRBilFaseOperativaValido.getSiacDFaseOperativa().getFaseOperativaCode();
			if (Constanti.BIL_FASE_OPERATIVA_PREDISPOSIZIONE_CONSUNTIVO.equals(bilCode)) {
				statoBilancioPerDoggiaGest = true;
			}
		}

		if (statoLiqValido && statoBilancioPerDoggiaGest) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Operazione di ricerca dell'atto amministrativo di una liquidazione.
	 * 
	 * @param liquidaz
	 * @param idEnte
	 * @param timestampInserimento
	 * @return
	 */
	private SiacTAttoAmmFin getAttoAmministrativo(Liquidazione liquidaz, int idEnte, Timestamp timestampInserimento) {
		SiacTAttoAmmFin attoTrovato = null;
		if (liquidaz != null && liquidaz.getAttoAmministrativoLiquidazione() != null) {
			AttoAmministrativo attAmm = liquidaz.getAttoAmministrativoLiquidazione();
			attoTrovato = getSiacTAttoAmmFromAttoAmministrativo(attAmm, idEnte);
		} else {
			//TODO: in questo ramo manca la struttura ammministrativa, NON HO CAPITO QUANDO VIENE ESEGUITO QUESTO RAMO, MI SEMBRA UN RAMO MORTO (REFUSO)
			//MA NEL DUBBIO LASCIAMO QUESTO RAMO LANCIANDO UN ERRORE PER INTERCETTARE LA CASISTICA ED INTERVENIRE SE NON FOSSE UN RAMO MORTO:
			log.error("LiquidazioneDad.getAttoAmministrativo", " !!!!!!!!!!! ATTENZIONE IL CODICE HA ESEGUITO UN RAMO CON POTENZIALE MALFUNZIONAMENTO !!!!!!!!!!!");
			log.error("LiquidazioneDad.getAttoAmministrativo", " !!!!!!!!!!! INTERVENIRE LEGGENDO I COMMENTI NEL CODICE JAVA !!!!!!!!!!!");
			log.error("LiquidazioneDad.getAttoAmministrativo", " !!!!!!!!!!! METODO: LiquidazioneDad.getAttoAmministrativo !!!!!!!!!!!");
			AttoAmministrativo attAmm = new AttoAmministrativo();
			attAmm.setAnno(liquidaz.getAnnoAttoAmministrativoLiquidazione());
			attAmm.setNumero(liquidaz.getNumeroAttoAmministrativoLiquidazione());
			TipoAtto tipoAtto = new TipoAtto();
			tipoAtto.setCodice(liquidaz.getCodiceTipoAttoAmministrativoLiquidazione());
			attAmm.setTipoAtto(tipoAtto);
			attoTrovato = getSiacTAttoAmmFromAttoAmministrativo(attAmm, idEnte);
		}
		//Termino restituendo l'oggetto di ritorno: 
        return attoTrovato;
	}

	/**
	 * Operazione richiamata dal servizio RicercaLiquidazioniService che calcola il numero di liquidazioni 
	 * che soddisfano i criteri di ricerca.
	 * 
	 * @param prl
	 * @param idEnte
	 * @return
	 */
	public Long calcolaNumeroLiquidazioniDaEstrarre(ParametroRicercaLiquidazione prl, Integer idEnte) {
		Long conteggioLiquidazioni = new Long(0);

		// viene cambiato oggetto perche' i model non possono essere passati ai
		// DaoImpl (specifica CSI)
		RicercaLiquidazioneParamDto paramSearch = (RicercaLiquidazioneParamDto) map(prl, RicercaLiquidazioneParamDto.class);
		conteggioLiquidazioni = liquidazioneDao.contaLiquidazioni(idEnte,paramSearch);
		//Termino restituendo l'oggetto di ritorno: 
        return conteggioLiquidazioni;
	}

	/**
	 * Operazione richiamata dal servizio RicercaLiquidazioniService che estrae le liquidazioni 
	 * che soddisfano i criteri di ricerca.
	 * 
	 * @param prl
	 * @param idEnte
	 * @param numeroPagina
	 * @param numeroRisultatiPerPagina
	 * @return
	 */
	public List<Liquidazione> ricercaLiquidazioni(ParametroRicercaLiquidazione prl, 
			String codiceAmbito,Integer idEnte, int numeroPagina,int numeroRisultatiPerPagina,DatiOperazioneDto datiOperazioneDto) {
		List<SiacTLiquidazioneFin> elencoSiacTLiquidazione = new ArrayList<SiacTLiquidazioneFin>();
		List<Liquidazione> elencoLiquidazioni = new ArrayList<Liquidazione>();

		RicercaLiquidazioneParamDto paramSearch = (RicercaLiquidazioneParamDto) map(prl, RicercaLiquidazioneParamDto.class);
		elencoSiacTLiquidazione = liquidazioneDao.ricercaLiquidazioni(idEnte,paramSearch, numeroPagina, numeroRisultatiPerPagina);
		
		List<SiacRLiquidazioneSoggettoFin> distintiSiacRLiquidazioneSoggetto = liquidazioneDao.ricercaByLiquidazioneMassive(elencoSiacTLiquidazione, "SiacRLiquidazioneSoggettoFin");
	
		HashMap<String,SoggettoInRicercaLiquidazioneDto> cacheSoggettiCoinvolti = new HashMap<String, SoggettoInRicercaLiquidazioneDto>();
		
		OttimizzazioneLiquidazioneDto ottimizzazioneDto = new OttimizzazioneLiquidazioneDto();
		ottimizzazioneDto.setDistintiSiacRLiquidazioneSoggetto(distintiSiacRLiquidazioneSoggetto);
		
		if (null != elencoSiacTLiquidazione && elencoSiacTLiquidazione.size() > 0) {

			elencoLiquidazioni = convertiLista(elencoSiacTLiquidazione,Liquidazione.class, FinMapId.SiacTLiquidazione_Liquidazione); 
			elencoLiquidazioni = EntityLiquidazioneToModelLiquidazioneConverter.siacTLiquidazioneEntityToLiquidazioneModel(elencoSiacTLiquidazione, elencoLiquidazioni);

			for (Liquidazione it : elencoLiquidazioni) {
				int idIterato = it.getUid();
				for (SiacTLiquidazioneFin itsiac : elencoSiacTLiquidazione) {
					int idConfronto = itsiac.getLiqId();
					if (idIterato == idConfronto) {
						
						List<SiacRLiquidazioneSoggettoFin> rLiquidazioneSoggettos = ottimizzazioneDto.filtraSiacRLiquidazioneSoggettoByLiqId(itsiac.getLiqId());
						
						for (SiacRLiquidazioneSoggettoFin rLiquidazioneSogg : rLiquidazioneSoggettos) {
								
								// Estrazione Soggetto
								String soggettoCode = rLiquidazioneSogg.getSiacTSoggetto().getSoggettoCode();
								
								SoggettoInRicercaLiquidazioneDto soggettoInRicercaLiquidazioneDto = null;
								if(cacheSoggettiCoinvolti.containsKey(soggettoCode)){
									soggettoInRicercaLiquidazioneDto = cacheSoggettiCoinvolti.get(soggettoCode);
								} else {
									soggettoInRicercaLiquidazioneDto = caricaSoggettoInRicercaLiquidazioneDto(codiceAmbito, idEnte, soggettoCode,datiOperazioneDto);
									cacheSoggettiCoinvolti.put(soggettoCode, soggettoInRicercaLiquidazioneDto);
								}
								
								Soggetto soggetto = soggettoInRicercaLiquidazioneDto.getSoggetto();

								// Estrazione Sedi
								List<SedeSecondariaSoggetto> listaSediSecondarie = soggettoInRicercaLiquidazioneDto.getListaSediSecondarie();

								// Estrazione MDP
								List<ModalitaPagamentoSoggetto> listaModPag = soggettoInRicercaLiquidazioneDto.getListaModPag();

								// modalita pagamento Liquidazione -- aggiunto il parametro codiceMDP = null
								//List<ModalitaPagamentoSoggetto> modalitaPagamentoSoggettoList = soggettoDad.ricercaModalitaPagamentoPerChiave(codiceAmbito, idEnte, soggetto.getCodiceSoggetto(),itsiac.getSiacTModpag().getUid(), null);
								Integer idModalitaPagamento = null;
								if(itsiac.getSiacTModpag()!=null && itsiac.getSiacTModpag().getUid()!=null){
									idModalitaPagamento = itsiac.getSiacTModpag().getUid();
								}else{
									idModalitaPagamento = itsiac.getCessioneId();
								}
								if(idModalitaPagamento!=null){
									//Teoricamente non dovrebbe mai essere null ma nella pratica e' successo per alcuni 
									//dati ottenuti da migrazioni quindi mettiamo questo controllo su idModalitaPagamento!=null
									List<ModalitaPagamentoSoggetto> modalitaPagamentoSoggettoList = soggettoDad.ricercaModalitaPagamentoPerChiave(codiceAmbito, idEnte, soggetto.getCodiceSoggetto(),idModalitaPagamento, null);
									if (modalitaPagamentoSoggettoList != null && modalitaPagamentoSoggettoList.size() > 0) {
										it.setModalitaPagamentoSoggetto(modalitaPagamentoSoggettoList.get(0));
									}
								}

								// sede secondaria Liquidazione
								it.setSedeSecondariaSoggetto(rLiquidazioneSogg.getSiacTSoggetto().getUid());
								it.setSoggettoLiquidazione(soggetto);
								it.getSoggettoLiquidazione().setSediSecondarie(listaSediSecondarie);
								it.getSoggettoLiquidazione().setModalitaPagamentoList(listaModPag);
								
						}
					}
				}
			}

			// DISPONIBILITA A PAGARE
			java.sql.Timestamp now = getNow();

			for (Liquidazione liquidazioneTrovata : elencoLiquidazioni) {
				for (SiacTLiquidazioneFin siacTLiquidazione : elencoSiacTLiquidazione) {
					if (liquidazioneTrovata.getUid() == siacTLiquidazione.getUid()) {

						List<SiacRLiquidazioneOrdFin> listSiacRLiquidazioneOrd = siacRLiquidazioneOrdRepository.findValidoByIdLiquidazione(idEnte, now,siacTLiquidazione.getUid());
						
						BigDecimal disponibilitaPagare = siacTLiquidazione.getLiqImporto();
						

						if (listSiacRLiquidazioneOrd != null && listSiacRLiquidazioneOrd.size() > 0) {
							for (SiacRLiquidazioneOrdFin siacRLiquidazioneOrd : listSiacRLiquidazioneOrd) {
								
								// JIRA 1877 il calcolo della disponibilità a pagare della liquidazione lo fai solo se l'ordinativo a cui è legata è VALIDO
								SiacTOrdinativoFin siacTOrdinativoFin = siacRLiquidazioneOrd.getSiacTOrdinativoT().getSiacTOrdinativo();
								List<SiacROrdinativoStatoFin> siacROrdinativoStato = siacROrdinativoStatoRepository.findSiacROrdinativoStatoValidoByIdOrdinativo(idEnte, siacTOrdinativoFin.getUid(), now);
								String statoOrdinativo = siacROrdinativoStato.get(0).getSiacDOrdinativoStato().getOrdStatoCode();
								
								if(!statoOrdinativo.equalsIgnoreCase(Constanti.D_ORDINATIVO_STATO_ANNULLATO)){
									
								
									// trovo l'importo attuale della
									// quota/subOrdinativo
									List<SiacTOrdinativoTsDetFin> listSiacTOrdinativoTsDet = siacTOrdinativoTsDetsRepository.findOrdinativoTsDetValidoByOrdTsId(idEnte, siacRLiquidazioneOrd.getSiacTOrdinativoT().getOrdTsId(), now);
									if (listSiacTOrdinativoTsDet != null && listSiacTOrdinativoTsDet.size() > 0) {
										// cerca l'importo attuale
										for (SiacTOrdinativoTsDetFin siacTOrdinativoTsDet : listSiacTOrdinativoTsDet) {
																				
											SiacDOrdinativoTsDetTipoFin tipo = siacTOrdinativoTsDet.getSiacDOrdinativoTsDetTipo();
											String tipoCode = siacTOrdinativoTsDet.getSiacDOrdinativoTsDetTipo().getOrdTsDetTipoCode();
											if (tipo != null && tipoCode.equalsIgnoreCase(Constanti.D_ORDINATIVO_TS_DET_TIPO_IMPORTO_ATTUALE)) {
												disponibilitaPagare = disponibilitaPagare.subtract(siacTOrdinativoTsDet.getOrdTsDetImporto());
											}
										}
									}
								
								}
							}
						}
						liquidazioneTrovata.setDisponibilitaPagare(disponibilitaPagare);
					}
				}
			}
			
		}

		//Termino restituendo l'oggetto di ritorno: 
        return elencoLiquidazioni;
	}
	
	/**
	 * Per ora inutilizzato, da richiamare se serve "spingere l'ottimizzazione come in ricercaAccertamentiSubAccertamentiOPT
	 * @param listaSiacTLiquidazioni
	 * @return
	 */
	private OttimizzazioneSoggettoDto caricaDatiOttimizzazioneRicercaSoggetto(List<SiacTLiquidazioneFin> listaSiacTLiquidazioni){
		OttimizzazioneSoggettoDto ottimizzazioneSoggettoDto = new OttimizzazioneSoggettoDto();
		List<SiacTSoggettoFin> distintiSiacTSoggettiCoinvolti = soggettoDao.ricercaBySiacTLiquidazionePkMassive(listaSiacTLiquidazioni);
		List<SiacRLiquidazioneSoggettoFin> distintiSiacRLiquidazioneSoggetto = liquidazioneDao.ricercaByLiquidazioneMassive(listaSiacTLiquidazioni, "SiacRLiquidazioneSoggettoFin");
		
		//CHIAMO IL METODO CORE DEI SOGGETTI OTTIMIZZATI:
		ottimizzazioneSoggettoDto = caricaDatiOttimizzazioneRicercaSoggettoByDistintiSoggetti(distintiSiacTSoggettiCoinvolti);
		//
		
		ottimizzazioneSoggettoDto.setDistintiSiacRLiquidazioneSoggetto(distintiSiacRLiquidazioneSoggetto);
		//Termino restituendo l'oggetto di ritorno: 
        return ottimizzazioneSoggettoDto;
	}
	
	/**
	 * Metodo utilizzato dalla ricerca liquidazioni che serve ad estrarre soggetto, 
	 * sedi secondarie e modalit� di pagamento a fronte di un codice soggetto.
	 *  
	 * @param idEnte
	 * @param soggettoCode
	 * @return
	 */
	private SoggettoInRicercaLiquidazioneDto caricaSoggettoInRicercaLiquidazioneDto(String codiceAmbito,Integer idEnte,String soggettoCode,DatiOperazioneDto datiOperazioneDto){
		
		SoggettoInRicercaLiquidazioneDto soggettoInRicercaLiquidazioneDto = new SoggettoInRicercaLiquidazioneDto();
		
		Soggetto soggetto = new Soggetto();
		soggetto = soggettoDad.ricercaSoggetto(codiceAmbito,idEnte,soggettoCode, false, true);

		// Estrazione Sedi
		List<SedeSecondariaSoggetto> listaSediSecondarie = null;
		listaSediSecondarie = soggettoDad.ricercaSediSecondarie(idEnte,soggetto.getUid(), false, false,datiOperazioneDto);

		// Estrazione MDP
		List<ModalitaPagamentoSoggetto> listaModPag = null;
		listaModPag = soggettoDad.ricercaModalitaPagamento(codiceAmbito, idEnte,soggetto.getUid(), "Soggetto",false);
		
		if (listaSediSecondarie != null && listaSediSecondarie.size() > 0) {
			for (SedeSecondariaSoggetto currentSedeSecondaria : listaSediSecondarie) {
				List<ModalitaPagamentoSoggetto> listaModPagSupportSedi = soggettoDad.ricercaModalitaPagamento(codiceAmbito, idEnte,currentSedeSecondaria.getUid(),currentSedeSecondaria.getDenominazione(),false);
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
		
		soggettoInRicercaLiquidazioneDto.setListaModPag(listaModPag);
		soggettoInRicercaLiquidazioneDto.setListaSediSecondarie(listaSediSecondarie);
		soggettoInRicercaLiquidazioneDto.setSoggetto(soggetto);
		
		//Termino restituendo l'oggetto di ritorno: 
        return soggettoInRicercaLiquidazioneDto;
		
	}
	
	/**
	 * Wrapper di retro compatibilita'
	 * @param liquidazione
	 * @param tipoRicerca
	 * @param now
	 * @param richiedente
	 * @param annoEsercizio
	 * @param codiceAmbito
	 * @param ente
	 * @return
	 */
	public Liquidazione ricercaLiquidazionePerChiave(Liquidazione liquidazione, String tipoRicerca, Richiedente richiedente, Integer annoEsercizio, String codiceAmbito,Ente ente,DatiOperazioneDto datiOperazioneDto) {
		return ricercaLiquidazionePerChiave(liquidazione, tipoRicerca, richiedente, annoEsercizio, codiceAmbito, ente,datiOperazioneDto, null);
	}

	/**
	 * Metodo customizzato ad uso e consumo del servizio di reintroito ordinativo.
	 * 
	 * Serve per evitare di richiamare l'intero servizio di ricerca liquidazione per chiave
	 * ma di ricaricare i soli dati che non si dispongono per una liquidazione appena inserita,
	 * evitando cosi di ricaricarla inutilmente.
	 * 
	 * @param uidLiquidazione
	 * @return
	 */
	public Liquidazione completaDatiLiquidazionePerReintroiti(Integer uidLiquidazione) {
		
		Liquidazione liquidazione = new Liquidazione();
		
		SiacTLiquidazioneFin siacTLiquidazione = siacTLiquidazioneRepository.findOne(uidLiquidazione);
		
		List<SiacRLiquidazioneClassFin> listaSiacRLiquidazioneClass = siacTLiquidazione.getSiacRLiquidazioneClasses();
		List<SiacRLiquidazioneAttrFin> listaSiacRLiquidazioneAttr = siacTLiquidazione.getSiacRLiquidazioneAttrs();
		
		liquidazione = (Liquidazione) TransazioneElementareEntityToModelConverter.
				convertiDatiTransazioneElementare(liquidazione, listaSiacRLiquidazioneClass,listaSiacRLiquidazioneAttr);
		
		return liquidazione;
	}
	
	/**
	 * Metodo per la ricerca liquidazione per chiave richiamato dal servizio RicercaLiquidazionePerChiaveService e
	 * dove serve ricercare una liquidazione per chiave allinterno di un DAD.
	 * 
	 * @param liquidazione
	 * @param now
	 * @param richiedente
	 * @param annoEsercizio
	 * @param ente
	 * @return
	 */
	public Liquidazione ricercaLiquidazionePerChiave(Liquidazione liquidazione, String tipoRicerca, Richiedente richiedente, Integer annoEsercizio, 
			String codiceAmbito,Ente ente,DatiOperazioneDto datiOperazioneDto ,OttimizzazioneOrdinativoPagamentoDto datiOttimizzazione) {
		
		Timestamp now = datiOperazioneDto.getTs();
		
		Liquidazione liquidazioneTrovata = null;
		Integer idEnte = ente.getUid();
		
		SiacTLiquidazioneFin siacTLiquidazione =null;
		
		
		if(datiOttimizzazione!=null){
			//RAMO OTTIMIZZATO
			siacTLiquidazione = datiOttimizzazione.getSiacTLiquidazioneConsiderata();
		} else {
			//RAMO CLASSICO
			siacTLiquidazione = liquidazioneDao.ricercaLiquidazionePerChiave(idEnte,liquidazione.getAnnoLiquidazione(),liquidazione.getNumeroLiquidazione(), now,String.valueOf(annoEsercizio));
		}
				
		if (siacTLiquidazione != null) {
			
			//17 NOVEMBRE 2016 - Introdotta l'ottimizzazione specifica anche per la liquidazione singola:
			if(datiOttimizzazione==null){
				//se datiOttimizzazione non e' valorizzato dal chiamante (tipicamente il ricerca ordinativo di pagamento)
				//occorre inizializzarlo verso la singola liquidazione richiesta:
				datiOttimizzazione = caricaOttimizzazioneRicercaOrdPagDaSingolaLiquidazione(siacTLiquidazione, datiOperazioneDto);
			}
			
			Integer idLiquidazione = siacTLiquidazione.getLiqId();
			
			liquidazioneTrovata = map(siacTLiquidazione, Liquidazione.class,
					FinMapId.SiacTLiquidazione_Liquidazione);
			liquidazioneTrovata = EntityLiquidazioneToModelLiquidazioneConverter.siacTLiquidazioneEntityToLiquidazioneModelPerChiave(siacTLiquidazione,liquidazioneTrovata,datiOttimizzazione);
			
			List<SiacRSubdocLiquidazioneFin> siacRSubdocLiquidaziones = null;
			if(datiOttimizzazione!=null){
				//RAMO OTTIMIZZATO
				siacRSubdocLiquidaziones = datiOttimizzazione.filtraSiacRSubdocLiquidazioneFinByLiqId(idLiquidazione);
			} else {
				//RAMO CLASSICO
				siacRSubdocLiquidaziones = siacTLiquidazione.getSiacRSubdocLiquidaziones();
			}
			
			// JIRA 1976 Controllo subito se la ricerca e' stata chiamata dall'ordinativo:
			// se si controllo che non ci siano legami con le quote di documento, se ci sono non la restituisco
			
			if (siacRSubdocLiquidaziones != null)
			{
				if (tipoRicerca.equals(Constanti.TIPO_RICERCA_DA_ORDINATIVO))
					if (!siacRSubdocLiquidaziones.isEmpty()){
						// aggiungo il msg di errore! liquidazione agganciata a documento 
						throw new BusinessException("Liquidazione collegata a documento", Esito.FALLIMENTO);
					}

				// setto id del eventuale r_doc_liquidazione collegata
				
				if (!siacRSubdocLiquidaziones.isEmpty())
					for (SiacRSubdocLiquidazioneFin siacRSubdocLiquidazione : siacRSubdocLiquidaziones)
						if (siacRSubdocLiquidazione.getDataCancellazione() == null)
						{
							SubdocumentoSpesa sdp = new SubdocumentoSpesa();

							sdp.setUid(siacRSubdocLiquidazione.getSiacTSubdoc().getUid());

							liquidazioneTrovata.setSubdocumentoSpesa(sdp);

							// mi porto dietro anche il documento legato al
							// subDocumento da visualizzare
							// nella consultazione della liquidazione
							// jira 1538
							
							//DocumentoSpesa 
							DocumentoSpesa documentoSpesa = null;
							
							if(datiOttimizzazione!=null){
								//RAMO OTTIMIZZATO
								Integer subdocLiqId = siacRSubdocLiquidazione.getSubdocLiqId();
								SiacRSubdocLiquidazione siacRSubdocLiquidazioneBil = datiOttimizzazione.getSiacRSubdocLiquidazioneById(subdocLiqId);
								SiacTDoc siacTDocBil = siacRSubdocLiquidazioneBil.getSiacTSubdoc().getSiacTDoc();
								
								DocumentoSpesa giaCaricato = datiOttimizzazione.getDocumentoSpesaGiaCaricato(siacTDocBil.getDocId());
								if(giaCaricato!=null){
									documentoSpesa = clone(giaCaricato);
								} else {
									documentoSpesa = documentoSpesaFinDad.findDocumentoSpesaById(siacTDocBil);
									DocumentoSpesa daAggiungereInCache = clone(documentoSpesa);
									datiOttimizzazione.addDocumentoSpesaCaricato(siacTDocBil.getDocId(), daAggiungereInCache);
								}
								 
							} else {
								//RAMO CLASSICO
								SiacTDocFin siacTDoc = siacRSubdocLiquidazione.getSiacTSubdoc().getSiacTDoc();
								documentoSpesa = documentoSpesaDad.findDocumentoSpesaById(siacTDoc.getUid());
							}
							

							liquidazioneTrovata.getSubdocumentoSpesa().setDocumento(documentoSpesa);

							break;
						}
			}
			
			// Liquidazione (distinta)
			Distinta distinta = null;
			SiacDDistintaFin siacDDistinta = siacTLiquidazione.getSiacDDistinta();
			if (siacDDistinta != null) {
				distinta = map(siacDDistinta, Distinta.class,FinMapId.SiacDDistinta_Distinta);
				liquidazioneTrovata.setDistinta(distinta);
			}
			// Liquidazione (conto tesoreria)
			ContoTesoreria contoTesoreria = null;
			SiacDContotesoreriaFin siacDContotesoreria = siacTLiquidazione.getSiacDContotesoreria();

			if (siacDContotesoreria != null) {
				contoTesoreria = map(siacDContotesoreria, ContoTesoreria.class,FinMapId.SiacDContotesoreria_Contotesoreria);
				liquidazioneTrovata.setContoTesoreria(contoTesoreria);
			}

			// Provvedimento
			List<SiacRLiquidazioneAttoAmmFin> siacRLiquidazioneAttoAmms = null;
			if(datiOttimizzazione!=null){
				//RAMO OTTIMIZZATO
				siacRLiquidazioneAttoAmms = datiOttimizzazione.filtraSiacRLiquidazioneAttoAmmFinByLiqId(idLiquidazione);
			} else {
				//RAMO CLASSICO
				siacRLiquidazioneAttoAmms = siacTLiquidazione.getSiacRLiquidazioneAttoAmms();
			}
			if(siacRLiquidazioneAttoAmms!=null){
				liquidazioneTrovata = settaAttoAmministrativoInLiquidazione(siacRLiquidazioneAttoAmms, liquidazioneTrovata);
			}
			

			// Soggetto
			// dovrebbe essere una!
			
			List<SiacRLiquidazioneSoggettoFin> rLiqSogg = null;
			if(datiOttimizzazione!=null){
				//RAMO OTTIMIZZATO
				rLiqSogg = datiOttimizzazione.filtraSiacRLiquidazioneSoggettoByLiqId(idLiquidazione);
			} else {
				//RAMO CLASSICO
				rLiqSogg = siacTLiquidazione.getSiacRLiquidazioneSoggettos();
			}
			
			for (SiacRLiquidazioneSoggettoFin rLiquidazioneSogg : rLiqSogg) {
				if (rLiquidazioneSogg.getDataFineValidita() == null) {

					List<SedeSecondariaSoggetto> listaSediSecondarie = null;
					List<ModalitaPagamentoSoggetto> listaModPag = null;
					Soggetto soggetto = null;
					
					if(datiOttimizzazione!=null){
						//RAMO OTTIMIZZATO
						
						SiacTSoggettoFin siacTSoggettoFin = rLiquidazioneSogg.getSiacTSoggetto();
						OttimizzazioneSoggettoDto ottimizzazioneSoggetto = datiOttimizzazione.getOttimizzazioneSoggettoDto();
						
						Integer idSoggetto = siacTSoggettoFin.getSoggettoId();
						
						// 	SIAC-5018 caricamento se sede deve essere fatto diversamente
						boolean isSedeSecondaria = false;
						List<SiacRSoggettoRelazFin> relazDue = ottimizzazioneSoggetto.filtraSiacRSoggettoRelaz2BySoggettoId(idSoggetto);
						SiacRSoggettoRelazFin relaz = CommonUtils.getFirst(CommonUtils.soloValidiSiacTBase(relazDue, null));
						SiacTSoggettoFin soggettoDellaSede = null;
						if(relaz!=null && relaz.getSiacDRelazTipo() != null){
							if(Constanti.SEDE_SECONDARIA.equals(relaz.getSiacDRelazTipo().getRelazTipoCode())){
								isSedeSecondaria = true;
								soggettoDellaSede = relaz.getSiacTSoggetto1();
							}
						}
						
						// Estrazione Soggetto
						boolean caricaSediSecondarie = true;
						boolean isDecentrato = false;
						boolean caricaDatiUlteriori = true;
						boolean includeModifica = false;
						
						if(!isSedeSecondaria){
							soggetto = soggettoDad.ricercaSoggettoOPT( siacTSoggettoFin, includeModifica, caricaDatiUlteriori, ottimizzazioneSoggetto,
									datiOperazioneDto, caricaSediSecondarie , isDecentrato);
						} else {
							soggetto = soggettoDad.ricercaSoggettoOPT( soggettoDellaSede, includeModifica, caricaDatiUlteriori, ottimizzazioneSoggetto,
									datiOperazioneDto, caricaSediSecondarie , isDecentrato);
						}
						
						listaSediSecondarie= soggetto.getSediSecondarie();
						listaModPag = soggetto.getElencoModalitaPagamento();
						
					} else {
						//RAMO CLASSICO
						
						// Estrazione Soggetto
						soggetto = soggettoDad.ricercaSoggetto(codiceAmbito,idEnte, rLiquidazioneSogg.getSiacTSoggetto().getSoggettoCode(), false, true);

						// Estrazione Sedi
						listaSediSecondarie = soggettoDad.ricercaSediSecondarie(idEnte, soggetto.getUid(), false, false,datiOperazioneDto);

						// Estrazione MDP
						listaModPag = soggettoDad.ricercaModalitaPagamento(codiceAmbito, idEnte,soggetto.getUid(), "Soggetto", false);
					}
					
					
					if (listaSediSecondarie != null && listaSediSecondarie.size() > 0) {
						for (SedeSecondariaSoggetto currentSedeSecondaria : listaSediSecondarie) {
							
							List<ModalitaPagamentoSoggetto> listaModPagSupportSedi = null;
							if(datiOttimizzazione!=null){
								//RAMO OTTIMIZZATO
								//sono gia' state precaricate dentro al metodo ricercaSoggettoOPT
								listaModPagSupportSedi =currentSedeSecondaria.getModalitaPagamentoSoggettos();
							} else {
								//RAMO CLASSICO
								listaModPagSupportSedi = soggettoDad.ricercaModalitaPagamento(codiceAmbito, idEnte,currentSedeSecondaria.getUid(),currentSedeSecondaria.getDenominazione(), false);
							}
							
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

					// modalita pagamento Liquidazione aggiunto il parametro codiceMDP = null
					log.debug("ricercaLiquidazionePerChiave","ricercaLiquidazionePerChiave, Ricerco modalita pagamento Liquidazione, parametri: codiceAmbito: " + codiceAmbito +"idEnte: "+ idEnte + "soggetto.codiceSoggetto: " + soggetto.getCodiceSoggetto() );
					
					Integer idModalitaPagamento = null;
					
					if(siacTLiquidazione.getSiacTModpag()!=null && siacTLiquidazione.getSiacTModpag().getUid()!=null){
						idModalitaPagamento = siacTLiquidazione.getSiacTModpag().getUid();
					}else{
						idModalitaPagamento = siacTLiquidazione.getCessioneId();
					}
					
					List<ModalitaPagamentoSoggetto> modalitaPagamentoSoggettoList = null;
					
					if(idModalitaPagamento!=null){
						//Teoricamente idModalitaPagamento non dovrebbe mai essere null ma nella pratica e' successo per alcuni 
						//dati ottenuti da migrazioni quindi mettiamo questo controllo su idModalitaPagamento!=null
						
						if(datiOttimizzazione!=null){
							//RAMO OTTIMIZZATO
							OttimizzazioneModalitaPagamentoDto ottimizzazioneModPag = datiOttimizzazione.getModalitaPagamentoLiquidazioniCoinvolte();
							modalitaPagamentoSoggettoList = soggettoDad.ricercaModalitaPagamentoPerChiave(codiceAmbito, idEnte,soggetto.getCodiceSoggetto(), idModalitaPagamento, null,ottimizzazioneModPag);
						} else {
							//RAMO CLASSICO
							modalitaPagamentoSoggettoList = soggettoDad.ricercaModalitaPagamentoPerChiave(codiceAmbito, idEnte,soggetto.getCodiceSoggetto(), idModalitaPagamento, null);
						}
					}
					
					if (modalitaPagamentoSoggettoList != null && modalitaPagamentoSoggettoList.size() > 0) {
						ModalitaPagamentoSoggetto modPagSogg= modalitaPagamentoSoggettoList.get(0);
						
						log.debug("ricercaLiquidazionePerChiave","ModalitaAccreditoSoggetto.codice, descrizione : " + modPagSogg.getModalitaAccreditoSoggetto().getCodice()+", "+modPagSogg.getModalitaAccreditoSoggetto().getDescrizione()
								+ " Bic: "+ modPagSogg.getBic() +", Iban: " + modPagSogg.getIban() +", ContoCorrente: "+modPagSogg.getContoCorrente()+", IntestazioneConto:" +modPagSogg.getIntestazioneConto()
								+ " SoggettoQuietanzante: "+ modPagSogg.getSoggettoQuietanzante() 
								+ ", codiceFiscaleQuietanzante: " + modPagSogg.getCodiceFiscaleQuietanzante() 
								+ ", dataNascitaQuietanzante: "+modPagSogg.getDataNascitaQuietanzante()
								+ ", luogoNascitaQuietanzante:" +modPagSogg.getLuogoNascitaQuietanzante() 
								+ ", statoNascitaQuietanzante:" +modPagSogg.getStatoNascitaQuietanzante());
						liquidazioneTrovata.setModalitaPagamentoSoggetto(modalitaPagamentoSoggettoList.get(0));
						
					}

					// sede secondaria Liquidazione
					liquidazioneTrovata.setSedeSecondariaSoggetto(rLiquidazioneSogg.getSiacTSoggetto().getUid());

					liquidazioneTrovata.setSoggettoLiquidazione(soggetto);
					liquidazioneTrovata.getSoggettoLiquidazione().setSediSecondarie(listaSediSecondarie);
					
					// FIXME: possibile bacarozzo!
					// la ModalitaPagamentoList è valorizzata solo da listaModPag, che potrebbe essere vuota (lista caricata dalle sede sedondaria)
					// ma potrebbe esserci la lista di modalità legata direttamente al soggetto 
					liquidazioneTrovata.getSoggettoLiquidazione().setModalitaPagamentoList(listaModPag);
				}
			}

			// Mutuo --> da spostare in entity
			
			
			
			List<SiacRMutuoVoceLiquidazioneFin> rMutuoVoceLiqs = null;
			if(datiOttimizzazione!=null){
				//RAMO OTTIMIZZATO
				rMutuoVoceLiqs = datiOttimizzazione.filtraSiacRMutuoVoceLiquidazioneFinByLiqId(idLiquidazione);
			} else {
				//RAMO CLASSICO
				rMutuoVoceLiqs  = siacTLiquidazione.getSiacRMutuoVoceLiquidaziones();
			}
			
			for (SiacRMutuoVoceLiquidazioneFin rMutuoVoceLiquidaziones : rMutuoVoceLiqs) {
				if (rMutuoVoceLiquidaziones.getDataFineValidita() == null) {
					Mutuo mutuo = new Mutuo();
					mutuo.setNumeroRegistrazioneMutuo(rMutuoVoceLiquidaziones.getSiacTMutuoVoce().getSiacTMutuo().getMutNumRegistrazione());
					mutuo.setCodiceMutuo(rMutuoVoceLiquidaziones.getSiacTMutuoVoce().getSiacTMutuo().getMutCode());
					// TO-DO: voce Mutuo da serivizio
					// mutuoDad.ricercaMutuo(codiceEnte, numeroMutuo, now);
					liquidazioneTrovata.setNumeroMutuo(Integer.valueOf(mutuo.getCodiceMutuo()));
				}
			}

			// Impegno --> da spostare in entity
			List<SiacRLiquidazioneMovgestFin> rLiqMovGests = null;
			if(datiOttimizzazione!=null){
				//RAMO OTTIMIZZATO
				rLiqMovGests = datiOttimizzazione.filtraSiacRLiquidazioneMovgestFinByLiqId(idLiquidazione);
			} else {
				//RAMO CLASSICO
				rLiqMovGests  = siacTLiquidazione.getSiacRLiquidazioneMovgests();
			}
			
			
			//PREDISPONIAMO L'OTTIMIZZAZIONE DEL RICERCA IMPEGNO PK:
			OttimizzazioneMovGestDto ottimizzazioneDaChiamanteMovPk = null;
			if(datiOttimizzazione!=null){
				//RAMO OTTIMIZZATO
				ottimizzazioneDaChiamanteMovPk = datiOttimizzazione.getOttimizzazioneMovimentiCoinvolti();
			} else {
				//RAMO CLASSICO
				//ottimizzazioneDaChiamante resta null e verra' quindi ignorato dentro ricercaMovimentoPk
			}
			
			for (SiacRLiquidazioneMovgestFin rLiquidazioneMovgest : rLiqMovGests) {
				
				if (rLiquidazioneMovgest.getDataFineValidita() == null && rLiquidazioneMovgest.getDataCancellazione() == null) { 
					// se
					// bisogna
					// aggiungere
					// condizione
					// per
					// l'ente

					Impegno impegno = new Impegno();

					Integer idPadre = rLiquidazioneMovgest.getSiacTMovgestTs().getMovgestTsIdPadre();
					if (idPadre != null) {
						// SubImpegno subImpegno = new SubImpegno();
						List<SubImpegno> listSub = new ArrayList<SubImpegno>();

						// Impegno
						impegno.setUid(rLiquidazioneMovgest.getSiacTMovgestTs().getSiacTMovgest().getUid());
						impegno.setAnnoMovimento(rLiquidazioneMovgest.getSiacTMovgestTs().getSiacTMovgest().getMovgestAnno());
						impegno.setDescrizione(rLiquidazioneMovgest.getSiacTMovgestTs().getSiacTMovgest().getMovgestDesc());
						impegno.setNumero(rLiquidazioneMovgest.getSiacTMovgestTs().getSiacTMovgest().getMovgestNumero());

						
						// SubImpegno
						BigDecimal numeroSubimpegno = new BigDecimal(rLiquidazioneMovgest.getSiacTMovgestTs().getMovgestTsCode());
						List<SubImpegno> listSubTrovati = new ArrayList<SubImpegno>();
						
						if(tipoRicerca.equalsIgnoreCase(Constanti.TIPO_RICERCA_DA_CONSULTA_LIQUIDAZIONE))
						{
							// RM Estrazione minimal dell'impegno! come richiesto dal consulta!!!
							// Servono: Anno, Numero, SubImpegno, Descrizione impegno
							SubImpegno sub = new SubImpegno();
							sub.setNumero(numeroSubimpegno);
							
							listSub.add(sub);
														
						}else{
							
							//APRILE 2016: OTTIMIZZAZIONI CHIAMATA RICERCA IMPEGNO:
							PaginazioneSubMovimentiDto paginazioneSubMovimentiDto = new PaginazioneSubMovimentiDto();
							paginazioneSubMovimentiDto.setNoSub(false);
							paginazioneSubMovimentiDto.setEscludiSubAnnullati(true);
							paginazioneSubMovimentiDto.setNumeroSubMovimentoRichiesto(numeroSubimpegno);
							//
							
							DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubAnnullati = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
							datiOpzionaliElencoSubAnnullati.setEscludiAnnullati(true);
							
							datiOpzionaliElencoSubAnnullati.setCaricaDisponibilePagare(false);
							
							//Ricerca Movimento:
							if(ottimizzazioneDaChiamanteMovPk!=null){
								//RAMO OTTIMIZZATO
								ottimizzazioneDaChiamanteMovPk.setMovimentoDaChiamante(rLiquidazioneMovgest.getSiacTMovgestTs().getSiacTMovgest());
							}
							EsitoRicercaMovimentoPkDto esitoRicercaMov = impegnoOttimizzatoDad.ricercaMovimentoPk(richiedente, ente, annoEsercizio.toString(),impegno.getAnnoMovimento(),impegno.getNumero(),paginazioneSubMovimentiDto,datiOpzionaliElencoSubAnnullati,Constanti.MOVGEST_TIPO_IMPEGNO, true,ottimizzazioneDaChiamanteMovPk);
							//
							
							if(esitoRicercaMov!=null){
								impegno = (Impegno) esitoRicercaMov.getMovimentoGestione();
							}
						
							listSubTrovati = impegno.getElencoSubImpegni();
	
							for (SubImpegno subImpegno : listSubTrovati) {
								BigDecimal currentNumeroSub = subImpegno.getNumero();
								if (numeroSubimpegno.equals(currentNumeroSub)) {
									listSub.add(subImpegno);

									break;
								}

							}
						}
						
					

						impegno.setElencoSubImpegni(listSub);

					} else {
						impegno.setNumero(rLiquidazioneMovgest.getSiacTMovgestTs().getSiacTMovgest().getMovgestNumero());
						impegno.setUid(rLiquidazioneMovgest.getSiacTMovgestTs().getSiacTMovgest().getUid());
						impegno.setAnnoMovimento(rLiquidazioneMovgest.getSiacTMovgestTs().getSiacTMovgest().getMovgestAnno());
						impegno.setDescrizione(rLiquidazioneMovgest.getSiacTMovgestTs().getSiacTMovgest().getMovgestDesc());
						
						//Non selezionato un SUB
						
						//APRILE 2016: OTTIMIZZAZIONI CHIAMATA RICERCA IMPEGNO:
						PaginazioneSubMovimentiDto paginazioneSubMovimentiDto = new PaginazioneSubMovimentiDto();
						paginazioneSubMovimentiDto.setNumeroSubMovimentoRichiesto(null);
						paginazioneSubMovimentiDto.setNoSub(true);
						
						// estrazione Impegno
						
						//Ricerca Movimento:
						if(ottimizzazioneDaChiamanteMovPk!=null){
							//RAMO OTTIMIZZATO
							ottimizzazioneDaChiamanteMovPk.setMovimentoDaChiamante(rLiquidazioneMovgest.getSiacTMovgestTs().getSiacTMovgest());
						}
						
						//APRILE 2017 - escludo il disp a pagare per motivi di performance:
					    DatiOpzionaliElencoSubTuttiConSoloGliIds caricaDatiOpzionaliDto = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
					    caricaDatiOpzionaliDto.setCaricaDisponibilePagare(false);
						//EsitoRicercaMovimentoPkDto esitoRicercaMov = impegnoOttimizzatoDad.ricercaMovimentoPk(richiedente, ente, annoEsercizio.toString(),impegno.getAnnoMovimento(),impegno.getNumero(),paginazioneSubMovimentiDto,null,Constanti.MOVGEST_TIPO_IMPEGNO, true,ottimizzazioneDaChiamanteMovPk);
						EsitoRicercaMovimentoPkDto esitoRicercaMov = impegnoOttimizzatoDad.ricercaMovimentoPk(richiedente, ente, annoEsercizio.toString(),impegno.getAnnoMovimento(),impegno.getNumero(),paginazioneSubMovimentiDto,caricaDatiOpzionaliDto ,Constanti.MOVGEST_TIPO_IMPEGNO, true,ottimizzazioneDaChiamanteMovPk);
						
						
						if(esitoRicercaMov!=null){
							impegno = (Impegno) esitoRicercaMov.getMovimentoGestione();
							//Settiamo i dati minimi degli eventuali sub:
							impegno.setElencoSubImpegni(esitoRicercaMov.getElencoSubImpegniTuttiConSoloGliIds());
						}


					}
					// Capitolo
					List<SiacRMovgestBilElemFin> listSiacRMovgestBilElem = rLiquidazioneMovgest.getSiacTMovgestTs().getSiacTMovgest().getSiacRMovgestBilElems();

					for (SiacRMovgestBilElemFin rMovgestBilElem : listSiacRMovgestBilElem) {
						if (rMovgestBilElem.getDataFineValidita() == null) { 
							// verificare
							// se
							// bisogna
							// aggiungere
							// condizione
							// per
							// l'ente
							impegno.setChiaveCapitoloUscitaGestione(rMovgestBilElem.getSiacTBilElem().getElemId());
						}
					}
					liquidazioneTrovata.setImpegno(impegno);
				}
			}

			// DISPONIBILITA A PAGARE
			// jira 1538, va calcolata solo se la liquidazione non è annullata o porvvisoria
			BigDecimal disponibilitaPagare = BigDecimal.ZERO;
			if(liquidazioneTrovata.getStatoOperativoLiquidazione().equals(StatoOperativoLiquidazione.VALIDO)){
				disponibilitaPagare = calcolaDisponibiltaPagare(ente,siacTLiquidazione,datiOttimizzazione);
			}

			liquidazioneTrovata.setDisponibilitaPagare(disponibilitaPagare);

			// Ordinativi collegati
			List<SiacRLiquidazioneOrdFin> listaRLiquidazioneOrd = null;
			if(datiOttimizzazione!=null){
				//RAMO OTTIMIZZATO
				listaRLiquidazioneOrd = datiOttimizzazione.filtraSiacRLiquidazioneOrdFinBySiacTLiquidazioneFinAncheVersoAltriOrdinativi(siacTLiquidazione);
			} else {
				//RAMO CLASSICO
				listaRLiquidazioneOrd = siacTLiquidazione.getSiacRLiquidazioneOrds();
			}
			
			List<OrdinativoPagamento> listaOrdinativiLiquidazione = new ArrayList<OrdinativoPagamento>();

			if (listaRLiquidazioneOrd != null && listaRLiquidazioneOrd.size() > 0) {
				listaRLiquidazioneOrd = DatiOperazioneUtils.soloValidi(listaRLiquidazioneOrd, getNow());

				for (SiacRLiquidazioneOrdFin siacRLiquidazioneOrd : listaRLiquidazioneOrd) {
					SiacTOrdinativoFin siacTOrdinativo = siacRLiquidazioneOrd.getSiacTOrdinativoT().getSiacTOrdinativo();

					if (siacTOrdinativo.getDataCancellazione() == null) {
						OrdinativoPagamento ordinativoPagamento = new OrdinativoPagamento();
						ordinativoPagamento.setUid(siacTOrdinativo.getUid());
						ordinativoPagamento.setIdOrdinativo(new BigDecimal(siacTOrdinativo.getUid()));
						ordinativoPagamento.setAnno(siacTOrdinativo.getOrdAnno());
						ordinativoPagamento.setNumero(siacTOrdinativo.getOrdNumero().intValue());

						List<SiacROrdinativoStatoFin> listaROrdinativoStato = null;
						
						if(datiOttimizzazione!=null){
							//RAMO OTTIMIZZATO
							listaROrdinativoStato = datiOttimizzazione.filtraSiacROrdinativoStatoFinBySiacTOrdinativoFin(siacTOrdinativo);
						} else {
							//RAMO CLASSICO
							listaROrdinativoStato = siacTOrdinativo.getSiacROrdinativoStatos();
						}
						
						if (listaROrdinativoStato != null && listaROrdinativoStato.size() > 0) {
							for (SiacROrdinativoStatoFin rOrdinativoStato : listaROrdinativoStato) {
								if (rOrdinativoStato != null && rOrdinativoStato.getDataFineValidita() == null) {
									String codeStato = rOrdinativoStato.getSiacDOrdinativoStato().getOrdStatoCode();
									StatoOperativoOrdinativo statoOpOrdinativo = Constanti.statoOperativoOrdinativoStringToEnum(codeStato);
									ordinativoPagamento.setStatoOperativoOrdinativo(statoOpOrdinativo);
								}
							}
						}
						listaOrdinativiLiquidazione.add(ordinativoPagamento);
					}
				}

				liquidazioneTrovata.setListaOrdinativi(listaOrdinativiLiquidazione);
			}
		}

		//Termino restituendo l'oggetto di ritorno: 
        return liquidazioneTrovata;
	}
	
	public OttimizzazioneOrdinativoPagamentoDto caricaOttimizzazioneRicercaOrdPagDaSingolaLiquidazione(SiacTLiquidazioneFin siacTLiquidazioneFin,DatiOperazioneDto datiOperazioneDto){
		return caricaOttimizzazioneRicercaOrdPagOppureLiquidazione(null, siacTLiquidazioneFin, datiOperazioneDto,this.soggettoDad);
	}
	
	/**
	 * 
	 * Data una liquidazione restituisce gli (eventuali) ordinativi validi collegati in stato diverso da ANNULLATO
	 * 
	 * @param siacTLiquidazione
	 * @return
	 */
	private List<SiacTOrdinativoFin> getOrdinativiValidiNonAnnullati(SiacTLiquidazioneFin siacTLiquidazione){
		List<SiacTOrdinativoFin> ordinativi = new ArrayList<SiacTOrdinativoFin>();
		if(siacTLiquidazione!=null){
			List<SiacRLiquidazioneOrdFin> listaRLiquidazioneOrd = siacTLiquidazione.getSiacRLiquidazioneOrds();
			listaRLiquidazioneOrd = DatiOperazioneUtils.soloValidi(listaRLiquidazioneOrd, getNow());
			for (SiacRLiquidazioneOrdFin siacRLiquidazioneOrd : listaRLiquidazioneOrd) {
				SiacTOrdinativoFin siacTOrdinativo = siacRLiquidazioneOrd.getSiacTOrdinativoT().getSiacTOrdinativo();
				if (siacTOrdinativo.getDataCancellazione() == null) {
					
					//valutiamo ancora la possibilita' che sia in stato annullato:
					SiacROrdinativoStatoFin statoAttuale = DatiOperazioneUtils.getValido(siacTOrdinativo.getSiacROrdinativoStatos(), getNow());
					
					if(statoAttuale!=null && statoAttuale.getSiacDOrdinativoStato() != null
							&& !Constanti.D_ORDINATIVO_STATO_ANNULLATO.equals(statoAttuale.getSiacDOrdinativoStato().getOrdStatoCode())){
						ordinativi.add(siacTOrdinativo);
					}
				}
			}
		}
		return ordinativi;
	}

	public BigDecimal calcolaDisponibiltaPagare(Ente ente, SiacTLiquidazioneFin siacTLiquidazione) {
		return calcolaDisponibiltaPagare(ente, siacTLiquidazione.getUid(),siacTLiquidazione.getLiqImporto(),null);
	}

	public BigDecimal calcolaDisponibiltaPagare(Ente ente, Liquidazione liquidazione) {
		return calcolaDisponibiltaPagare(ente, liquidazione.getUid(), liquidazione.getImportoLiquidazione(),null);
	}
	
	public BigDecimal calcolaDisponibiltaPagare(Ente ente, SiacTLiquidazioneFin siacTLiquidazione,OttimizzazioneOrdinativoPagamentoDto datiOttimizzazione) {
		return calcolaDisponibiltaPagare(ente, siacTLiquidazione.getUid(),siacTLiquidazione.getLiqImporto(),datiOttimizzazione);
	}

	public BigDecimal calcolaDisponibiltaPagare(Ente ente, Liquidazione liquidazione,OttimizzazioneOrdinativoPagamentoDto datiOttimizzazione) {
		return calcolaDisponibiltaPagare(ente, liquidazione.getUid(), liquidazione.getImportoLiquidazione(),datiOttimizzazione);
	}
	
	
	/**
	 * Metodo per calcolare la disponibilita' a pagare della liquidazione.
	 * 
	 * @param ente
	 * @param liquidazioneId
	 * @param importoLiquidazione
	 * @return
	 */
	public BigDecimal calcolaDisponibiltaPagare(Ente ente, Integer liquidazioneId, BigDecimal importoLiquidazione,OttimizzazioneOrdinativoPagamentoDto datiOttimizzazione) {

		BigDecimal disponibilitaPagare = BigDecimal.ZERO;
		
		long startUno = System.currentTimeMillis();

		// DISPONIBILITA A PAGARE
		List<SiacRLiquidazioneOrdFin> listSiacRLiquidazioneOrd = null;
		
		if(datiOttimizzazione!=null){
			//RAMO OTTIMIZZATO
			listSiacRLiquidazioneOrd = datiOttimizzazione.filtraSiacRLiquidazioneOrdFinBySiacTLiquidazioneFinAncheVersoAltriOrdinativi(liquidazioneId);
		} else {
			//RAMO CLASSICO
			listSiacRLiquidazioneOrd = siacRLiquidazioneOrdRepository.findValidoByIdLiquidazione(ente.getUid(), getNow(),liquidazioneId);
		}
		
		
		disponibilitaPagare = importoLiquidazione;

		if (listSiacRLiquidazioneOrd != null && listSiacRLiquidazioneOrd.size() > 0) {
			for (SiacRLiquidazioneOrdFin siacRLiquidazioneOrd : listSiacRLiquidazioneOrd) {
				// Jira 2040 Aggiungere il controllo sullo stato dell'ordinativo
				SiacTOrdinativoFin siacTOrdinativo = siacRLiquidazioneOrd.getSiacTOrdinativoT().getSiacTOrdinativo();
				List<SiacROrdinativoStatoFin> listaROrdinativoStato = ordinativoDao.ricercaBySiacTOrdinativoFinMassive(toList(siacTOrdinativo) , "SiacROrdinativoStatoFin");
				// Faccio il check dello stato! devo escludere quelli annullati!
				if (listaROrdinativoStato != null && listaROrdinativoStato.size() > 0) {
					
					for (SiacROrdinativoStatoFin rOrdinativoStato : listaROrdinativoStato) {
						
						if (CommonUtils.isValidoSiacTBase(rOrdinativoStato, null)) {
							
							// se lo stato non è annullato considero l'importo dell'ordinativo 
							if (!rOrdinativoStato.getSiacDOrdinativoStato().getOrdStatoDesc().equals(StatoOperativoOrdinativo.ANNULLATO.toString())){
								// trovo l'importo attuale della quota/subOrdinativo
								
								List<SiacTOrdinativoTsDetFin> listSiacTOrdinativoTsDet = null;
								if(datiOttimizzazione!=null){
									//RAMO OTTIMIZZATO
									listSiacTOrdinativoTsDet = datiOttimizzazione.filtraSiacTOrdinativoTsDetFinBySiacTOrdinativoTFin(siacRLiquidazioneOrd.getSiacTOrdinativoT());
								} else {
									//RAMO CLASSICO
									 listSiacTOrdinativoTsDet = siacTOrdinativoTsDetsRepository.findOrdinativoTsDetValidoByOrdTsId(ente.getUid(),siacRLiquidazioneOrd.getSiacTOrdinativoT().getOrdTsId(), getNow());
								}
								
								if (listSiacTOrdinativoTsDet != null && listSiacTOrdinativoTsDet.size() > 0) {
									// cerca l'importo attuale
									for (SiacTOrdinativoTsDetFin siacTOrdinativoTsDet : listSiacTOrdinativoTsDet) {
										SiacDOrdinativoTsDetTipoFin tipo = siacTOrdinativoTsDet.getSiacDOrdinativoTsDetTipo();
										String tipoCode = siacTOrdinativoTsDet.getSiacDOrdinativoTsDetTipo().getOrdTsDetTipoCode();
										if (tipo != null && tipoCode.equalsIgnoreCase(Constanti.D_ORDINATIVO_TS_DET_TIPO_IMPORTO_ATTUALE)) {
											disponibilitaPagare = disponibilitaPagare.subtract(siacTOrdinativoTsDet.getOrdTsDetImporto());
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		long endUno = System.currentTimeMillis();
		
		long totUno = endUno - startUno;
				
		CommonUtils.println("totUno: " + totUno );
		
		//Termino restituendo l'oggetto di ritorno: 
        return disponibilitaPagare;
	}

	/**
	 * Setta l'atto nella liquidazione.
	 * 
	 * @param atti
	 * @return
	 */
	private Liquidazione settaAttoAmministrativoInLiquidazione(List<SiacRLiquidazioneAttoAmmFin> siacRLiquidazioneAttoAmms, Liquidazione liquidazione) {
		SiacRLiquidazioneAttoAmmFin siacRLiquidazioneAttoAmm = DatiOperazioneUtils.getValido(siacRLiquidazioneAttoAmms, null);
		if (siacRLiquidazioneAttoAmm != null){
			SiacTAttoAmmFin attoAmm = siacRLiquidazioneAttoAmm.getSiacTAttoAmm();
			if (attoAmm != null){
				AttoAmministrativo atto = EntityToModelConverter.siacTAttoToAttoAmministrativo(attoAmm);
				liquidazione.setAttoAmministrativoLiquidazione(atto);
			}
		}
		return liquidazione;
	}

	/**
	 * Metodo per controllare l'esistenza di una liquidazione sul DB.
	 * 
	 * @param annoLiquidazione
	 * @param numeroLiquidazione
	 * @param annoEsercizio
	 * @param datiOperazioneDto
	 * @return
	 */
	public boolean presenzaLiquidazione(Integer annoLiquidazione,BigDecimal numeroLiquidazione, String annoEsercizio,DatiOperazioneDto datiOperazioneDto) {
		boolean risultato = false;
		// findLiquidazioneByAnnoNumero
		SiacTLiquidazioneFin siacTLiquidazione = siacTLiquidazioneRepository.findLiquidazioneByAnnoNumeroAnnoBilancio(datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId(),annoLiquidazione, numeroLiquidazione, annoEsercizio,datiOperazioneDto.getTs());
		if (siacTLiquidazione != null) {
			risultato = true;
		}
		//Termino restituendo l'oggetto di ritorno: 
        return risultato;
	}

	/**
	 * Operazione di annullamento liquidazione richiamata dal servizio AnnullaLiquidazioneService.
	 * Tale metodo viene anche richiamato dal dad dell'ordinativo di pagamento per annullare una liquidazione automatica.
	 * 
	 * @param annoLiquidazione
	 * @param numeroLiquidazione
	 * @param annoEsercizio
	 * @param datiOperazioneDto
	 * @param richiedente
	 */
	public Liquidazione annullaLiquidazione(Integer annoLiquidazione,BigDecimal numeroLiquidazione, String annoEsercizio, DatiOperazioneDto datiOperazioneDto, Richiedente richiedente) {

		String methodName = "annullaLiquidazione";
		Liquidazione liquidazioneAnnullata = new Liquidazione();
		
		SiacDLiquidazioneStatoFin siacDLiquidazioneStato = new SiacDLiquidazioneStatoFin();
		siacDLiquidazioneStato = siacDLiquidazioneStatoRepository.findDLiquidazioneStatoByCodeAndEnte(datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId(),Constanti.D_LIQUIDAZIONE_STATO_ANNULLATO.toString(),datiOperazioneDto.getTs());

		SiacTLiquidazioneFin siacTLiquidazione = siacTLiquidazioneRepository.findLiquidazioneByAnnoNumeroAnnoBilancio(datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId(),annoLiquidazione, numeroLiquidazione, annoEsercizio, datiOperazioneDto.getTs());

		if (siacTLiquidazione != null) {
			
			liquidazioneAnnullata = map(siacTLiquidazione,Liquidazione.class, FinMapId.SiacTLiquidazione_Liquidazione_Base);
			
			List<SiacRLiquidazioneStatoFin> listaRelLiquidazione = siacTLiquidazione.getSiacRLiquidazioneStatos();

			if (listaRelLiquidazione != null && listaRelLiquidazione.size() > 0) {
				for (SiacRLiquidazioneStatoFin itStati : listaRelLiquidazione) {
					if (itStati.getDataFineValidita() == null) {
						itStati = DatiOperazioneUtils.impostaDatiOperazioneLogin(itStati, datiOperazioneDto,siacTAccountRepository);
						//salvo sul db:
						siacRLiquidazioneStatoRepository.saveAndFlush(itStati);

						// inserisco la riga con lo stato annullato
						SiacRLiquidazioneStatoFin siacRLiquidazioneStato = new SiacRLiquidazioneStatoFin();

						DatiOperazioneDto datiOperazioneInserimento = new DatiOperazioneDto(getCurrentMillisecondsTrentaMaggio2017(),Operazione.INSERIMENTO,datiOperazioneDto.getSiacTEnteProprietario(),richiedente.getAccount().getId());

						siacRLiquidazioneStato = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRLiquidazioneStato,datiOperazioneInserimento,siacTAccountRepository);
						siacRLiquidazioneStato.setSiacTLiquidazione(siacTLiquidazione);
						siacRLiquidazioneStato.setSiacDLiquidazioneStato(siacDLiquidazioneStato);
						//salvo sul db:
						siacRLiquidazioneStatoRepository.saveAndFlush(siacRLiquidazioneStato);
					}
				}
			}
			
			// SIAC-2980 : se la liquidazione ha il subdoc richiamo il ser dell'annulla stato , 
			// quindi in questo punto carico la liquidazione con l'informazione necessaria al ser di aggiorna stato quota documento
			List<SiacRSubdocLiquidazioneFin> listaRSubdocLiquidazioneFin = siacTLiquidazione.getSiacRSubdocLiquidaziones();
			SubdocumentoSpesa subDocSpesa = new SubdocumentoSpesa();
			
			if(listaRSubdocLiquidazioneFin!=null && !listaRSubdocLiquidazioneFin.isEmpty()){
			
				subDocSpesa.setUid(siacTLiquidazione.getSiacRSubdocLiquidaziones().get(0).getSiacTSubdoc().getUid());
				DocumentoSpesa docSpesa = new DocumentoSpesa();
				docSpesa = map(siacTLiquidazione.getSiacRSubdocLiquidaziones().get(0).getSiacTSubdoc().getSiacTDoc(), DocumentoSpesa.class, FinMapId.SiacTDocFin_DocumentoSpesa);
				subDocSpesa.setDocumento(docSpesa);
				
				liquidazioneAnnullata.setSubdocumentoSpesa(subDocSpesa);
				
				
			}
			
		}else{
			
			log.error(methodName, "Errore, la liquidazione con  "+annoLiquidazione +"/"+numeroLiquidazione+"] non esiste, impossibile annullare. ");
			throw new BusinessException( "Errore, la liquidazione con  "+annoLiquidazione +"/"+numeroLiquidazione+"] non esiste, impossibile annullare. ", Esito.FALLIMENTO);
		}

		// Doppia gestione
		// Cancella liquidazione residua
		String annoEsercizioSucc = String.valueOf((Integer.parseInt(annoEsercizio)) + 1);
		SiacTLiquidazioneFin siacTLiquidazioneResidua = siacTLiquidazioneRepository.findLiquidazioneByAnnoNumeroAnnoBilancio(datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId(),annoLiquidazione, numeroLiquidazione,annoEsercizioSucc, datiOperazioneDto.getTs());

		if (siacTLiquidazioneResidua != null) {
			DatiOperazioneDto datiOperazioneCancella = new DatiOperazioneDto(getCurrentMillisecondsTrentaMaggio2017(),Operazione.CANCELLAZIONE_LOGICA_RECORD,datiOperazioneDto.getSiacTEnteProprietario(), richiedente.getAccount().getId());
			DatiOperazioneUtils.cancellaRecord(siacTLiquidazioneResidua,siacTLiquidazioneRepository,datiOperazioneCancella, siacTAccountRepository);
		}
		
		return liquidazioneAnnullata;

	}

	/**
	 * Operazione che controlla la liquidazione prima del'aggiornamento.
	 * 
	 * @param idEnte
	 * @param liquidazioneInput
	 * @return
	 */
	public EsitoGestioneLiquidazioneDto controlliDiMeritoAggiornaLiquidazione(Ente ente, Liquidazione liquidazioneInput,String annoEsercizio, DatiOperazioneDto datiOperazione) {
		EsitoGestioneLiquidazioneDto esito = new EsitoGestioneLiquidazioneDto();
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		int idEnte = ente.getUid();
		SiacTLiquidazioneFin siacTLiquidazioneFin = liquidazioneDao.ricercaLiquidazionePerChiave(idEnte,liquidazioneInput.getAnnoLiquidazione(),liquidazioneInput.getNumeroLiquidazione(), getNow(),annoEsercizio);
		
		// Controlli di merito - inizio
		AttoAmministrativo attoAmm = liquidazioneInput.getAttoAmministrativoLiquidazione();
		AttoAmministrativo attoAmmCaricato = getAttoAministrativo(idEnte, attoAmm);
		if (attoAmmCaricato != null) {
			liquidazioneInput.setAttoAmministrativoLiquidazione(attoAmmCaricato);
		} else {
			return addErroreToList(esito, listaErrori,ErroreFin.INCONGRUENZA_NEI_PARAMETRI_.getErrore("Provvedimento"));
		}

//		AttoAmministrativo attoAmministrativo = liquidazioneInput.getAttoAmministrativoLiquidazione();
//		if (attoAmministrativo == null) {
//			return addErroreToList(esito, listaErrori,ErroreFin.INCONGRUENZA_NEI_PARAMETRI_.getErrore(""));
//		}

		//SIAC-7159
		//si invalida la Modalita Pagamento PagoPA
		if(checkModPagIsPagoPA(liquidazioneInput)) {
			return addErroreToList(esito, listaErrori, it.csi.siac.siacfin2ser.model.errore.ErroreFin.MOD_PAGO_PA_NON_AMMESSA.getErrore(""));
		}
		
		// controllo x stato provvedimento
		if (Constanti.STATO_ANNULLATO.equals(liquidazioneInput.getAttoAmministrativoLiquidazione().getStatoOperativo())) {
			return addErroreToList(esito, listaErrori,ErroreFin.ATTO_AMM_ANNULLATO.getErrore(""));
		}
		
		//leggo lo stato della liquidazione:
		String statoLiq = getStatoCode(siacTLiquidazioneFin);

		// Controllo di disponibilità della liquidazione
		if(!liquidazioneInput.isForza()){
			//  Nel caso in cui il chiamante abbia indicato il campo FORZA = TRUE 
			//  questo controllo non deve essere effettuato (se non indicato il  campo forza si intende a FALSE)
			if (liquidazioneInput.getImportoLiquidazioneDaAggiornare() != null && !liquidazioneInput.getImportoLiquidazione().equals(liquidazioneInput.getImportoLiquidazioneDaAggiornare())) {
				BigDecimal newImportoLiquidazione = liquidazioneInput.getImportoLiquidazione();
				
				//CONTROLLI IMPORTO
				
				//1. L'importo della liquidazione deve essere maggiore di zero
				if (BigDecimal.ZERO.compareTo(newImportoLiquidazione) >= 0) {
					return addErroreToList(esito, listaErrori,ErroreFin.MOD_DISPONIBILITA_LIQUIDAZIONE_INSUFFICIENTE.getErrore(""));
				}
	
				
				//2. L'importo della liquidazione deve essere minore uguale alla somma tra disponibilita liquidare impegno e importo precedente
				BigDecimal disponibilitaLiquidareImpegno = liquidazioneInput.getImpegno().getDisponibilitaLiquidare();
				if (liquidazioneInput.getImpegno().getElencoSubImpegni() != null && !liquidazioneInput.getImpegno().getElencoSubImpegni().isEmpty()) {
					SubImpegno subImpegno = liquidazioneInput.getImpegno().getElencoSubImpegni().get(0);
					if (subImpegno != null) {
						disponibilitaLiquidareImpegno = subImpegno.getDisponibilitaLiquidare();
					}
				}
				
				BigDecimal importoPrecedente = liquidazioneInput.getImportoLiquidazioneDaAggiornare();
				BigDecimal importoConfronto = disponibilitaLiquidareImpegno.add(importoPrecedente);
				if (importoConfronto != null) {
					if (newImportoLiquidazione.compareTo(importoConfronto) > 0) {
						return addErroreToList(esito, listaErrori,ErroreFin.MOD_DISPONIBILITA_LIQUIDAZIONE_INSUFFICIENTE.getErrore(""));
					}
				}
				
				//NUOVI CONTROLLI SULL'IMPORTO, SOLO SE LO STATO E' VALIDO:
				if (Constanti.LIQUIDAZIONE_STATO_VALIDO.equalsIgnoreCase(statoLiq)) {
					
					//3. L'importo della liquidazione deve essere maggiore uguale alla differenza di importo precedente meno disponibilita a pagare
					BigDecimal disponibilitaPagareLiquidazione = calcolaDisponibiltaPagare(ente, siacTLiquidazioneFin);
					importoConfronto = importoPrecedente.subtract(disponibilitaPagareLiquidazione);
					if (newImportoLiquidazione.compareTo(importoConfronto) < 0) {
						return addErroreToList(esito, listaErrori,ErroreFin.DISPONIBILITA_LIQUIDAZIONE_INSUFFICIENTE.getErrore(""));
					}
					
					//4. L'importo della liquidazione deve essere minore uguale dell'importo precedente
					if(newImportoLiquidazione.compareTo(importoPrecedente)>0){
						return addErroreToList(esito, listaErrori,ErroreCore.VALORE_NON_VALIDO.getErrore("importo", "(importo solo in diminuzione)"));
					}
					
				}
				
				
			}
		}
		
		
		//CONTROLLI SIOPE PLUS:
		boolean daImpegno = false;
		List<Errore> errSiopePlus = controlliSiopePlus(liquidazioneInput.getSiopeTipoDebito(), liquidazioneInput.getSiopeAssenzaMotivazione(), liquidazioneInput.getCig(), datiOperazione,daImpegno);
		if(!StringUtils.isEmpty(errSiopePlus)){
			return addErroreToList(esito, listaErrori, errSiopePlus.get(0));
		}
		
		if(!Constanti.LIQUIDAZIONE_STATO_PROVVISORIO.equalsIgnoreCase(statoLiq)) {
			//se non e' provvisoria non e' consentito modificare i dati di siope plus:
			if(isSiopePlusModificata(siacTLiquidazioneFin, liquidazioneInput, datiOperazione)){
				return addErroreToList(esito, listaErrori,ErroreCore.VALORE_NON_VALIDO.getErrore("SIOPE+", " (Per impegno non provvisorio non e' consentito modificare i dati di SIOPE+)"));
			}
		}
		
		//
		
		// Controlli di merito - fine
		
		esito.setLiquidazione(liquidazioneInput);

		//Termino restituendo l'oggetto di ritorno: 
        return esito;
	}
	
	//SIAC-7159
	//si invalida la Modalita Pagamento PagoPA
	private boolean checkModPagIsPagoPA(Liquidazione liquidazioneInput) {
		//INDIVIDUIAMO LA MODALITA PAGAMENTO SELEZIONATA:
		if(liquidazioneInput != null && liquidazioneInput.getModalitaPagamentoSoggetto() != null && "APA".equals(liquidazioneInput.getModalitaPagamentoSoggetto().getModalitaAccreditoSoggetto().getCodice())){
			return true;
		}
		return false;
	}
	
	
	public String getStatoCode(Integer idLiquidazione){
		if(idLiquidazione!=null){
			SiacTLiquidazioneFin siacTLiquidazioneFin = siacTLiquidazioneRepository.findOne(idLiquidazione);
			return getStatoCode(siacTLiquidazioneFin);
		}
		return null;
	}
	
	public String getStatoCode(Integer idEnte, Integer annoLiq, BigDecimal numeroLiq, String annoEsercizio){
		if(idEnte!=null && annoLiq!=null && numeroLiq!=null && annoEsercizio!=null){
			SiacTLiquidazioneFin siacTLiquidazioneFin = liquidazioneDao.ricercaLiquidazionePerChiave(idEnte,annoLiq,numeroLiq, getNow(),annoEsercizio);
			return getStatoCode(siacTLiquidazioneFin);
		}
		return null;
	}
	
	private String getStatoCode(SiacTLiquidazioneFin siacTLiquidazioneFin){
		if(siacTLiquidazioneFin!=null){
			List<SiacRLiquidazioneStatoFin> elencoSiacRLiquidazioneStato = siacTLiquidazioneFin.getSiacRLiquidazioneStatos();
			SiacRLiquidazioneStatoFin siacRLiquidazioneStato = CommonUtils.getValidoSiacTBase(elencoSiacRLiquidazioneStato, getNow());
			if(!isStatoLiqNull(siacRLiquidazioneStato)){
				return siacRLiquidazioneStato.getSiacDLiquidazioneStato().getLiqStatoCode();
			}
		}
		return null;
	}
	

	
	
	/**
	 * Operazione richiamata dal servizio AggiornaLiquidazioneService per l'aggiornamento della liquidazione.
	 * Prima di aggiornare la liquidazione in input esegue una serie di controlli.
	 * E' utilizzata anche sull'aggiornamento della liquidazione residua nella doppia gestione.
	 * 
	 * @param ente
	 * @param richiedente
	 * @param annoEsercizio
	 * @param bilancio
	 * @param liquidazioneInput
	 * @param elencoSubOrdinativi
	 * @return
	 */
	public EsitoGestioneLiquidazioneDto operazioneInternaAggiornaLiquidazione(Ente ente, Richiedente richiedente, String annoEsercizio,Bilancio bilancio,
			Liquidazione liquidazioneInput,List<SubOrdinativoPagamento> elencoSubOrdinativi, DatiOperazioneDto datiOperazione) {
		EsitoGestioneLiquidazioneDto esito = new EsitoGestioneLiquidazioneDto();

		// Operazione interna aggiorna liquidazione
		EsitoGestioneLiquidazioneDto esitoControlliAggiornaLiq = this.controlliDiMeritoAggiornaLiquidazione(ente,liquidazioneInput,annoEsercizio, datiOperazione);

		if (esitoControlliAggiornaLiq.getListaErrori() != null && esitoControlliAggiornaLiq.getListaErrori().size() > 0) {
			esitoControlliAggiornaLiq.setLiquidazione(null);
			return esitoControlliAggiornaLiq;
		}

		Liquidazione liquidazione = esitoControlliAggiornaLiq.getLiquidazione();
		
		// jira 3096
		// se lo stato operativo è passato sulla liquidazione non devo considerare quello dell'atto
		if(liquidazioneInput.getStatoOperativoLiquidazione()==null){
			 
			if (Constanti.STATO_ANNULLATO.equals(liquidazione.getAttoAmministrativoLiquidazione().getStatoOperativo())){
				liquidazione.setStatoOperativoLiquidazione(StatoOperativoLiquidazione.ANNULLATO);
			}else if (Constanti.STATO_PROVVISORIO.equals(liquidazione.getAttoAmministrativoLiquidazione().getStatoOperativo())){
				liquidazione.setStatoOperativoLiquidazione(StatoOperativoLiquidazione.PROVVISORIO);
			}else{
				liquidazione.setStatoOperativoLiquidazione(StatoOperativoLiquidazione.VALIDO);
			}   
		
		}
		
		EsitoGestioneLiquidazioneDto esitoAggiornaLiq = this.aggiornaLiquidazione(ente, richiedente, annoEsercizio, bilancio, liquidazione, elencoSubOrdinativi);

		if (esitoAggiornaLiq.getListaErrori() != null && esitoAggiornaLiq.getListaErrori().size() > 0) {
			esitoAggiornaLiq.setLiquidazione(null);
			return esitoAggiornaLiq;
		}
		// Operazione interna aggiorna liquidazione

		esito.setLiquidazione(liquidazione);

		//Termino restituendo l'oggetto di ritorno: 
        return esito;
	}

	/**
	 * Operazione di aggiornamento della liquidazione.
	 * 
	 * @param ente
	 * @param richiedente
	 * @param annoEsercizio
	 * @param bilancio
	 * @param liquidazioneInput
	 * @param elencoSubOrdinativi
	 * @return
	 */
	public EsitoGestioneLiquidazioneDto aggiornaLiquidazione(Ente ente,Richiedente richiedente, String annoEsercizio, 
			Bilancio bilancio,Liquidazione liquidazioneInput,List<SubOrdinativoPagamento> elencoSubOrdinativi) {
		
		EsitoGestioneLiquidazioneDto esito = new EsitoGestioneLiquidazioneDto();
		Liquidazione liqReturn = null;
		List<Errore> listaErrori = new ArrayList<Errore>();

		String loginOperazione = richiedente.getAccount().getNome();

		long currMillisec = getCurrentMillisecondsTrentaMaggio2017();
		Date dateInserimento = new Date(currMillisec);
		Timestamp timestampInserimento = null;
		timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);

		int idEnte = ente.getUid();

		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository.findOne(idEnte);

		DatiOperazioneDto datiOperazioneDto = new DatiOperazioneDto(currMillisec, Operazione.MODIFICA, siacTEnteProprietario, null, richiedente.getAccount().getId(), bilancio.getAnno());

		List<SiacTBilFin> siacTBilCurrentList = siacTBilRepository.getValidoByAnno(idEnte, Integer.toString(bilancio.getAnno()),datiOperazioneDto.getTs());
		if (siacTBilCurrentList != null && siacTBilCurrentList.size() > 0 && siacTBilCurrentList.get(0) != null) {
			bilancio.setUid(siacTBilCurrentList.get(0).getBilId());
		}

		liqReturn = aggiornamentoLiquidazione(liquidazioneInput, annoEsercizio,loginOperazione, Constanti.AMBITO_FIN, 
				idEnte, bilancio, datiOperazioneDto, timestampInserimento);

		// Aggiornamento liquidazione residua in doppia gestione
		boolean isdoppiagestione = aggiornareDoppiaGestione(bilancio,liquidazioneInput, datiOperazioneDto);
		if (isdoppiagestione) {
			//start doppia gestione
			liquidazioneInput.setCodiceStatoOperativoLiquidazione(liqReturn.getCodiceStatoOperativoLiquidazione());
			aggiornamentoInDoppiaGestioneLiquidazione(ente, richiedente,bilancio, liquidazioneInput, elencoSubOrdinativi,datiOperazioneDto,null);
			//end doppia gestione
		}
		esito.setListaErrori(listaErrori);
		esito.setLiquidazione(liqReturn);
		//Termino restituendo l'oggetto di ritorno: 
        return esito;
	}
	
	/*
	 * per la valutazione della modifica dei dati di siope plus 
	 * (tripletta CIG -  TIPO DEBITO - ASSENZA MOTIVAZIONE CIG)
	 */
	private boolean isSiopePlusModificata(SiacTLiquidazioneFin siacTLiquidazione, Liquidazione liquidazione, DatiOperazioneDto datiOperazioneDto){
		
		AttributoTClassInfoDto attributoInfo = new AttributoTClassInfoDto();
		attributoInfo.setTipoOggetto(OggettoDellAttributoTClass.T_LIQUIDAZIONE);
		attributoInfo.setSiacTLiquidazione(siacTLiquidazione);
		
		//CIG:
		if(isAttributoModificato(liquidazione.getCig(), Constanti.T_ATTR_CODE_CIG, datiOperazioneDto, attributoInfo)){
			return true;
		}
		
		//SIOPE TIPO DEBITO
		if(isModificatoSiopeTipoDebito(liquidazione, siacTLiquidazione, datiOperazioneDto)){
			return true;
		}
		
		//ASSENZA MOTIVAZIONE
		if(isModificatoSiopeAssenzaMotivazione(liquidazione, siacTLiquidazione, datiOperazioneDto)){
			return true;
		}
		
		return false;
	}
	
	/**
	 *  per la valutazione del siope pluse modificato
	 * @param liquidazione
	 * @param siacTMovgestTsSubImp
	 * @param datiOperazioneDto
	 * @return
	 */
	private boolean isModificatoSiopeTipoDebito(Liquidazione liquidazione,SiacTLiquidazioneFin siacTLiquidazione,DatiOperazioneDto datiOperazioneDto){
		boolean isModificato = false;
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		
		SiopeTipoDebito siopeTipoDebito = liquidazione.getSiopeTipoDebito();
		SiacDSiopeTipoDebitoFin siacDSiopeTipoDebitoNew = null;
		if(siopeTipoDebito!=null && !StringUtils.isEmpty(siopeTipoDebito.getCodice())){
			//recuperiamo il record della codifica ricevuta:
			List<SiacDSiopeTipoDebitoFin> dstdebitos = siacDSiopeTipoDebitoFinRepository.findByCode(idEnte, datiOperazioneDto.getTs(), siopeTipoDebito.getCodice());
			siacDSiopeTipoDebitoNew = CommonUtils.getFirst(dstdebitos);
		}
		
		SiacDSiopeTipoDebitoFin siopeTipoDebitoOld = siacTLiquidazione.getSiacDSiopeTipoDebitoFin();
		if(!CommonUtils.entrambiNullOrEntrambiIstanziati(siopeTipoDebitoOld, siacDSiopeTipoDebitoNew)){
			//uno dei due e' null e l'altro non lo e' --> sono sicuramenti diversi
			isModificato = true;
			return isModificato;
		} else if(CommonUtils.entrambiDiversiDaNull(siopeTipoDebitoOld, siacDSiopeTipoDebitoNew)){
			//c'e' la possibilita' che ci sia una modifica
			if(!DatiOperazioneUtils.hannoLoStessoUid(siopeTipoDebitoOld, siacDSiopeTipoDebitoNew)){
				isModificato = true;
				return isModificato;
			}
		}
		return isModificato;
	}
	
	/**
	 *  per la valutazione del siope plus modificato
	 * @param liquidazione
	 * @param siacTLiquidazione
	 * @param datiOperazioneDto
	 * @return
	 */
	private boolean isModificatoSiopeAssenzaMotivazione(Liquidazione liquidazione,SiacTLiquidazioneFin siacTLiquidazione,DatiOperazioneDto datiOperazioneDto){
		boolean isModificato = false;
		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		
		SiopeAssenzaMotivazione siopeAssenzaMotivazione = liquidazione.getSiopeAssenzaMotivazione();
		SiacDSiopeAssenzaMotivazioneFin siacDSiopeAssenzaMotivazioneNew = null;
		if(siopeAssenzaMotivazione!=null && !StringUtils.isEmpty(siopeAssenzaMotivazione.getCodice())){
			//recuperiamo il record della codifica ricevuta:
			List<SiacDSiopeAssenzaMotivazioneFin> aMtvs = siacDSiopeAssenzaMotivazioneFinRepository.findByCode(idEnte, datiOperazioneDto.getTs(), siopeAssenzaMotivazione.getCodice());
			siacDSiopeAssenzaMotivazioneNew = CommonUtils.getFirst(aMtvs);
		}
		
		SiacDSiopeAssenzaMotivazioneFin siacDSiopeAssenzaMotivazioneOld = siacTLiquidazione.getSiacDSiopeAssenzaMotivazione();
		if(!CommonUtils.entrambiNullOrEntrambiIstanziati(siacDSiopeAssenzaMotivazioneOld, siacDSiopeAssenzaMotivazioneNew)){
			//uno dei due e' null e l'altro non lo e' --> sono sicuramenti diversi
			isModificato = true;
			return isModificato;
		} else if(CommonUtils.entrambiDiversiDaNull(siacDSiopeAssenzaMotivazioneOld, siacDSiopeAssenzaMotivazioneNew)){
			//c'e' la possibilita' che ci sia una modifica
			if(!DatiOperazioneUtils.hannoLoStessoUid(siacDSiopeAssenzaMotivazioneOld, siacDSiopeAssenzaMotivazioneNew)){
				isModificato = true;
				return isModificato;
			}
		}
		return isModificato;
	}

	/**
	 * Implementa quanto descritto nel capitolo "2.5.3	Aggiornamento in Doppia Gestione" del documento
	 * "BIL--SIAC-FIN-SER-007-V01 - SPES005 Servizio Gestione Liquidazione.docx"
	 * La movimentazione della liquidazione sull’esercizio corrente comporta l'imputazione o aggiornamento, 
	 * se gi&agrave; presente, dell'equivalente movimento sull'esercizio successivo.
	 * <br/>
	 * Se la liquidazione e' in stato VALIDO, occorre calcolare la <strong>nuova diponibilit&agrave; a pagare </strong>
	 * in questo modo:
	 * <ul>
	 *    <li>Se la liquidazione e' nuova, <strong>nuova diponibilit&agrave; a pagare = importo liquidazione </strong> </li>
	 *    <li> Se la liquidazione e' in annullamento, <strong>nuova diponibilit&agrave; a pagare = 0 </strong></li>
	 *    <li> Se la liquidazione ha l'importo cambiato, <strong>nuova diponibilit&agrave; a pagare = diponibilit&agrave; a pagare - importo vecchio + nuovo importo</strong> </li>
	 *    <li>Se sono presenti subordinativi, <strong>nuova diponibilit&agrave; a pagare = diponibilit&agrave; a pagare - importo vecchio + nuovo importo</strong> </li>
	 * </ul>
	 *
	 * In base al valore così ottenuto, se la <strong>nuova diponibilit&agrave; a pagare &egrave; > 0 </strong>, la liquidazione nel bilancio successivo
	 * viene aggiornata o inderita (se non presente) con gli stessi valori della liquidazione in aggiornamento nell'0anno di bilancio ad eccezione 
	 * dell'importo, che deve essere pari alla disponibilit&agrave; a pagare precedentemente caslcolata. 
	 * 
	 * @param ente the ente
	 * @param richiedente the richiedente
	 * @param bilancio the bilancio
	 * @param liquidazioneInput the liquidazione input
	 * @param elencoSubOrdinativi the elenco sub ordinativi
	 * @param datiOperazioneDto the dati operazione dto
	 * @param capitoliInfo the capitoli info
	 * @return the esito gestione liquidazione dto
	 */
	public EsitoGestioneLiquidazioneDto aggiornamentoInDoppiaGestioneLiquidazione(Ente ente, Richiedente richiedente, Bilancio bilancio,Liquidazione liquidazioneInput,
			List<SubOrdinativoPagamento> elencoSubOrdinativi, DatiOperazioneDto datiOperazioneDto,CapitoliInfoDto capitoliInfo) {
		
		final String methodName = "aggiornamentoInDoppiaGestioneLiquidazione";

		EsitoGestioneLiquidazioneDto esito = new EsitoGestioneLiquidazioneDto();

		String annoEsercizio = String.valueOf(bilancio.getAnno());
		String statoOP = Constanti.statoOperativoLiquidazioneEnumToString(liquidazioneInput.getStatoOperativoLiquidazione());
		if (!Constanti.LIQUIDAZIONE_STATO_VALIDO.equalsIgnoreCase(statoOP)) {
			log.debug(methodName, "Liquidazione non in stato valido, esco.");
			return esito;
		}
		
		int idEnte = ente.getUid();

		Bilancio bilancioPiuUno = new Bilancio();
		bilancioPiuUno.setAnno(bilancio.getAnno() + 1);
		List<SiacTBilFin> siacTBilList = siacTBilRepository.getValidoByAnno(idEnte, Integer.toString(bilancioPiuUno.getAnno()),datiOperazioneDto.getTs());
		
		if(siacTBilList == null || siacTBilList.isEmpty() || siacTBilList.get(0) == null) {
			log.debug(methodName, "Non esistono bilanci successivi, esco.");
			return esito;
		}
		

		bilancioPiuUno.setUid(siacTBilList.get(0).getUid());
		
		//ricarico la liquidazione origine:
		SiacTLiquidazioneFin siacTLiquidazioneInput = siacTLiquidazioneRepository.findLiquidazioneByAnnoNumeroAnnoBilancio(idEnte,liquidazioneInput.getAnnoLiquidazione(),liquidazioneInput.getNumeroLiquidazione(),String.valueOf(bilancio.getAnno()),datiOperazioneDto.getTs());;
		
		//FIX PER  SIAC-4944:
		if(liquidazioneInput.getUid()==0 && siacTLiquidazioneInput!=null){
			liquidazioneInput.setUid(siacTLiquidazioneInput.getUid());
		}
		//

		// Calcolo nuova disponibilita' a pagare
		BigDecimal disponibilitaPagare = calcolaDisponibiltaPagare(ente, liquidazioneInput);

		if (elencoSubOrdinativi != null && elencoSubOrdinativi.size() > 0) {
			for (SubOrdinativoPagamento subOrdinativoPagamento : elencoSubOrdinativi) {
				if (subOrdinativoPagamento.getImportoIniziale() != null) {
					disponibilitaPagare = disponibilitaPagare.add(subOrdinativoPagamento.getImportoIniziale());
				}
				if (subOrdinativoPagamento.getImportoAttuale() != null) {
					disponibilitaPagare = disponibilitaPagare.subtract(subOrdinativoPagamento.getImportoAttuale());
				}
			}
		}

		if (disponibilitaPagare.compareTo(BigDecimal.ZERO) == 0) {
			// Disponibilità a pagare zero -  devo cancellare la liquidazione nell'anno di bilancio successivo.
			SiacTLiquidazioneFin siacTLiquidazioneAnnoBilancioPiuUno = siacTLiquidazioneRepository.findLiquidazioneByAnnoNumeroAnnoBilancio(idEnte,liquidazioneInput.getAnnoLiquidazione(),liquidazioneInput.getNumeroLiquidazione(),String.valueOf(bilancioPiuUno.getAnno()),datiOperazioneDto.getTs());
			
			if (siacTLiquidazioneAnnoBilancioPiuUno != null) {
				
				//controllo sul gia pagato nell'anno successivo (SIAC-5915) :
				Errore erroreGiaPagato = controlloGiaPagatoLiqResidua(BigDecimal.ZERO, ente, siacTLiquidazioneAnnoBilancioPiuUno.getUid(), siacTLiquidazioneAnnoBilancioPiuUno.getLiqImporto());
				if(erroreGiaPagato!=null){
					esito.addErrore(erroreGiaPagato);
					return esito;
				}
				//
				
				
				DatiOperazioneDto datiOperazioneCancella = new DatiOperazioneDto(getCurrentMillisecondsTrentaMaggio2017(),Operazione.CANCELLAZIONE_LOGICA_RECORD,datiOperazioneDto.getSiacTEnteProprietario(),richiedente.getAccount().getId());
				DatiOperazioneUtils.cancellaRecord(siacTLiquidazioneAnnoBilancioPiuUno,siacTLiquidazioneRepository,datiOperazioneCancella,siacTAccountRepository);
			}
			return esito;
		} 
		Liquidazione liquidazioneDaRicercare = new Liquidazione();
		liquidazioneDaRicercare.setAnnoLiquidazione(liquidazioneInput.getAnnoLiquidazione());
		liquidazioneDaRicercare.setNumeroLiquidazione(liquidazioneInput.getNumeroLiquidazione());

		//18 novembre, fix su ambito non valorizzato:
		String ambitoCode = Constanti.AMBITO_FIN;
		if(datiOperazioneDto.getSiacDAmbito()!=null){
			ambitoCode = datiOperazioneDto.getSiacDAmbito().getAmbitoCode();
		}
		//
		
		Liquidazione liquidazioneAnnoBilancioPiuUno = ricercaLiquidazionePerChiave(liquidazioneDaRicercare, Constanti.TIPO_RICERCA_DA_LIQUIDAZIONE,
				richiedente, bilancioPiuUno.getAnno(),ambitoCode, ente,datiOperazioneDto);

		if (liquidazioneAnnoBilancioPiuUno != null) {
			if (liquidazioneAnnoBilancioPiuUno.getCodiceStatoOperativoLiquidazione().equalsIgnoreCase(Constanti.LIQUIDAZIONE_STATO_ANNULLATO)) {
				esito.addErrore(ErroreFin.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("liquidazione residua","annullato"));
				return esito;
			}
			
			// Disponibilita' a pagare maggiore di zero e esiste liq
			// residua - Aggiornare liquidazione residua
			BigDecimal nuovoImportoLiqResidua = disponibilitaPagare;
			
			//controllo sul gia pagato nell'anno successivo (SIAC-5915) :
			Errore erroreGiaPagato = controlloGiaPagatoLiqResidua(nuovoImportoLiqResidua, ente, liquidazioneAnnoBilancioPiuUno.getUid(), liquidazioneAnnoBilancioPiuUno.getImportoLiquidazione());
			if(erroreGiaPagato!=null){
				esito.addErrore(erroreGiaPagato);
				return esito;
			}
			//
			
			
			//setto il nuovo importo della liq residua:
			liquidazioneInput.setImportoLiquidazione(nuovoImportoLiqResidua);
			
			//invoco l'aggiornamento:
			EsitoGestioneLiquidazioneDto esitoOperazioneInternaAggiornaLiq = this.operazioneInternaAggiornaLiquidazione(ente,richiedente, annoEsercizio,bilancioPiuUno,
					liquidazioneInput,elencoSubOrdinativi, datiOperazioneDto);

			if (esitoOperazioneInternaAggiornaLiq.getListaErrori() != null && esitoOperazioneInternaAggiornaLiq.getListaErrori().size() > 0) {
				esitoOperazioneInternaAggiornaLiq.setLiquidazione(null);
				return esitoOperazioneInternaAggiornaLiq;
			}
			return esito;
		} 
		// Disponibilità a pagare maggiore di zero e NON esiste liq residua - Inserire liquidazione residua
		int anno = Integer.parseInt(annoEsercizio);
		Impegno impegnoOriginale = liquidazioneInput.getImpegno();
		
		//APRILE 2016: OTTIMIZZAZIONI CHIAMATA RICERCA IMPEGNO:
		PaginazioneSubMovimentiDto paginazioneSubMovimentiDto = new PaginazioneSubMovimentiDto();
		if(presenzaSub(impegnoOriginale)){
			//Selezionato un SUB 
			BigDecimal numeroSubOriginale = impegnoOriginale.getElencoSubImpegni().get(0).getNumero();
			paginazioneSubMovimentiDto.setNoSub(false);
			paginazioneSubMovimentiDto.setNumeroSubMovimentoRichiesto(numeroSubOriginale);
		} else {
			//Non selezionato un SUB
			paginazioneSubMovimentiDto.setNumeroSubMovimentoRichiesto(null);
			paginazioneSubMovimentiDto.setNoSub(true);
		}
		//
		
		EsitoRicercaMovimentoPkDto esitoRicercaMov = impegnoOttimizzatoDad.ricercaMovimentoPk(richiedente, ente,String.valueOf(anno + 1),impegnoOriginale.getAnnoMovimento(),impegnoOriginale.getNumero(),paginazioneSubMovimentiDto,null,Constanti.MOVGEST_TIPO_IMPEGNO, false);

		Impegno impegnoDoppiaGestione = null;
		if(esitoRicercaMov!=null){
			impegnoDoppiaGestione = (Impegno) esitoRicercaMov.getMovimentoGestione();
		}
		
		//22 FEBBRAIO 2017 - Non creava l'impegno residuo quando assente:
		if(impegnoDoppiaGestione==null){
			//non esiste l'impegno residuo --> va creato
			log.info(methodName, "Creo l'impegno residuo in annobilancio +1 per la liquidazione");
			
			BigDecimal dispPagareImp = impegnoOriginale.getDisponibilitaPagare();
			//calcolaDisponibilitaAPagareImpegno(siacTMovgest, siacTMovgestTs, statoCod, idEnte);
			
			Impegno impegnoResiduoDaCreare = clone(impegnoOriginale);
			impegnoResiduoDaCreare.setImportoAttuale(dispPagareImp);
			impegnoResiduoDaCreare.setImportoIniziale(dispPagareImp);
			
			SiacDAmbitoFin siacDAmbitoPerCode = siacDAmbitoRepository.findAmbitoByCode(Constanti.AMBITO_FIN, idEnte);
			
			Integer numeroImpegno=new Integer(impegnoResiduoDaCreare.getNumero().intValue());
			impegnoResiduoDaCreare.setListaModificheMovimentoGestioneSpesa(new ArrayList<ModificaMovimentoGestioneSpesa>());
			DatiOperazioneDto datiOperazioneDtoIns = new DatiOperazioneDto(getCurrentMillisecondsTrentaMaggio2017(), Operazione.INSERIMENTO, datiOperazioneDto.getSiacTEnteProprietario(), siacDAmbitoPerCode, richiedente.getAccount().getId(), datiOperazioneDto.getAnnoBilancio());
			
			//RISOLUAZIONE DEL CAPITOLO:
			CapitoloUscitaGestione capitolo = impegnoResiduoDaCreare.getCapitoloUscitaGestione();
			if(capitolo==null){
				ChiaveLogicaCapitoloDto chiaveLogicaCapitoloAnnoBilancio = chiaveFisicaToLogicaCapitolo(impegnoOriginale.getChiaveCapitoloUscitaGestione());
				capitolo = new CapitoloUscitaGestione();
				capitolo.setNumeroCapitolo(chiaveLogicaCapitoloAnnoBilancio.getNumeroCapitolo());
				capitolo.setNumeroArticolo(chiaveLogicaCapitoloAnnoBilancio.getNumeroArticolo());
				capitolo.setNumeroUEB(chiaveLogicaCapitoloAnnoBilancio.getNumeroUeb());
			}
			capitolo.setAnnoCapitolo(bilancioPiuUno.getAnno());
			impegnoResiduoDaCreare.setCapitoloUscitaGestione(capitolo);
			impegnoResiduoDaCreare.setAnnoCapitoloOrigine(bilancioPiuUno.getAnno());
			//
			
			//SIAC-5798 TOLGO I VINCOLI RESIDUI:
			((Impegno) impegnoResiduoDaCreare).setVincoliImpegno(null);
			//
			
			EsitoInserimentoMovimentoGestioneDto esitoOperazioneInternaCiclo = impegnoOttimizzatoDad.operazioneInternaInserisceImpegno(richiedente, ente, bilancioPiuUno, impegnoResiduoDaCreare, datiOperazioneDtoIns, numeroImpegno,null);
			
			if(esitoOperazioneInternaCiclo.getListaErrori() != null && !esitoOperazioneInternaCiclo.getListaErrori().isEmpty()) {
				List<String> testoErrori = new ArrayList<String>();
				for (Errore errore : esitoOperazioneInternaCiclo.getListaErrori()) {
					testoErrori.add(errore.getTesto());
				}
				log.warn(methodName, new StringBuilder()
						.append("Inserimento impegno residuo non andato a buon fine. Errori riscontrati: ")
						.append(org.apache.commons.lang3.StringUtils.join(testoErrori, ", ")).toString());
			}
			
			
			impegnoDoppiaGestione = (Impegno) esitoOperazioneInternaCiclo.getMovimentoGestione();
			if(impegnoDoppiaGestione == null) {
				log.warn(methodName, "L'impegno nell'anno di bilancio successivo non e' presente ne' e' stato possibile inserirlo. Esco.");
				return esito;
			}
			
			caricaChiaveLogicaCapitoloUscitaGestioneSuImpegnoByChiaveFisicaCapitolo(impegnoDoppiaGestione, impegnoDoppiaGestione.getChiaveCapitoloUscitaGestione());
			
			//SIAC-6702
			effettuaOperazioniSuStoricoImpegnoAccertamento(liquidazioneInput.getImpegno(), impegnoDoppiaGestione, bilancio, datiOperazioneDtoIns);
		}
				
		if (presenzaSub(impegnoOriginale)) {
			
			BigDecimal numeroSubOriginale = impegnoOriginale.getElencoSubImpegni().get(0).getNumero();

			if (impegnoDoppiaGestione.getElencoSubImpegni() != null && impegnoDoppiaGestione.getElencoSubImpegni().size() > 0) {
				List<SubImpegno> elencoSubImpegniDoppiaGestione = new ArrayList<SubImpegno>();
				for (SubImpegno subImpegno : impegnoDoppiaGestione.getElencoSubImpegni()) {
					if (subImpegno.getNumero().compareTo(numeroSubOriginale) == 0) {
						elencoSubImpegniDoppiaGestione.add(subImpegno);
						break;
					}
				}
				impegnoDoppiaGestione.setElencoSubImpegni(elencoSubImpegniDoppiaGestione);
			}

		} else {
			impegnoDoppiaGestione.setElencoSubImpegni(esitoRicercaMov.getElencoSubImpegniTuttiConSoloGliIds());
		}
		
		liquidazioneInput.setImpegno(impegnoDoppiaGestione);
		liquidazioneInput.setCapitoloUscitaGestione(impegnoDoppiaGestione.getCapitoloUscitaGestione());
		
		liquidazioneInput.setImportoLiquidazione(disponibilitaPagare);
		//devo sovrascrivere il capitolo della liquidazione altrimenti mi prende qyuello dell'anno di bilancio precedente
		
		// SIAC-6021
		Integer annoLiquidazioneOriginePerDoppiaGest = null;
		if(siacTLiquidazioneInput!=null){
			annoLiquidazioneOriginePerDoppiaGest = siacTLiquidazioneInput.getLiqAnno();
		}
		//
		
		// Inserimento liquidazione ribaltata con operazione
		// interna
		EsitoGestioneLiquidazioneDto esitoOperazioneInternaInserisciLiqDG = operazioneInternaInserisciLiquidazione(ente,richiedente, String.valueOf(anno + 1),
						bilancioPiuUno, liquidazioneInput,liquidazioneInput.getNumeroLiquidazione().longValue(), true,datiOperazioneDto,annoLiquidazioneOriginePerDoppiaGest);

		if (esitoOperazioneInternaInserisciLiqDG.getListaErrori() != null && esitoOperazioneInternaInserisciLiqDG.getListaErrori().size() > 0) {
			esitoOperazioneInternaInserisciLiqDG.setLiquidazione(null);
			List<String> testoErrori = new ArrayList<String>();
			for (Errore errore : esitoOperazioneInternaInserisciLiqDG.getListaErrori()) {
				testoErrori.add(errore.getTesto());
			}
			log.warn(methodName, new StringBuilder()
					.append("Inserimento liquidazione residua non andato a buon fine. Errori riscontrati: ")
					.append(org.apache.commons.lang3.StringUtils.join(testoErrori, ", ")).toString());
			return esitoOperazioneInternaInserisciLiqDG;
		}

		//Termino restituendo l'oggetto di ritorno: 
        return esito;
	}
	
	private void caricaChiaveLogicaCapitoloUscitaGestioneSuImpegnoByChiaveFisicaCapitolo(Impegno imp, int chiaveCapitolo) {
		if(chiaveCapitolo == 0) {
			return;
		}
		
		ChiaveLogicaCapitoloDto chiaveLogicaCapitolo = chiaveFisicaToLogicaCapitolo(chiaveCapitolo);
		CapitoloUscitaGestione capitolo = new CapitoloUscitaGestione();
		capitolo.setAnnoCapitolo(chiaveLogicaCapitolo.getAnnoCapitolo());
		capitolo.setNumeroCapitolo(chiaveLogicaCapitolo.getNumeroCapitolo());
		capitolo.setNumeroArticolo(chiaveLogicaCapitolo.getNumeroArticolo());
		capitolo.setNumeroUEB(chiaveLogicaCapitolo.getNumeroUeb());
		capitolo.setUid(chiaveCapitolo);
		imp.setCapitoloUscitaGestione(capitolo);
	}

	private void effettuaOperazioniSuStoricoImpegnoAccertamento(Impegno impegnoOldBilancio, Impegno impegnoNewBilancio, Bilancio bilancioOld,  DatiOperazioneDto datiOperazione) {
		final String methodName = "effettuaOperazioniSuStoricoImpegnoAccertamento";
	
		Ente ente = new Ente();
		ente.setUid(datiOperazione.getSiacTEnteProprietario().getUid());
		List<StoricoImpegnoAccertamento> storici = caricaStoricoImpegnoAccertamento(impegnoOldBilancio.getAnnoMovimento(), impegnoOldBilancio.getNumero(), bilancioOld, ente);
		
		if(storici == null) {
			log.debug(methodName, "Non sono presenti legami storici nell'impegno di origine, non li ribalto.");
			return;
		}
		
		for (StoricoImpegnoAccertamento storicoDaRibaltareSuNuovoImpegno : storici) {
			
			log.debug(methodName, "Ribalto il legame storico con uid: " + storicoDaRibaltareSuNuovoImpegno.getUid());
			
			SubImpegno subImpegnoAnnoBilancioPiuUno = getSubImpegnoAnnoBilancioPiuUno(impegnoNewBilancio, datiOperazione, storicoDaRibaltareSuNuovoImpegno.getSubImpegno());
			
			StoricoImpegnoAccertamento storicoRibaltato = new StoricoImpegnoAccertamento();
			storicoRibaltato.setEnte(ente);
			storicoRibaltato.setImpegno(impegnoNewBilancio);
			storicoRibaltato.setSubImpegno(subImpegnoAnnoBilancioPiuUno);
			storicoRibaltato.setAccertamento(storicoDaRibaltareSuNuovoImpegno.getAccertamento());
			storicoRibaltato.setSubAccertamento(storicoDaRibaltareSuNuovoImpegno.getSubAccertamento());
			storicoImpegnoAccertamentoDad.inserisciStorico(storicoRibaltato, datiOperazione);
		}
	}

	/**
	 * @param impegnoNewBilancio
	 * @param datiOperazione
	 * @param subImpegnoOldBilancio
	 * @return
	 */
	private SubImpegno getSubImpegnoAnnoBilancioPiuUno(Impegno impegnoNewBilancio, DatiOperazioneDto datiOperazione, SubImpegno subImpegnoOldBilancio) {
		
		if(subImpegnoOldBilancio == null || subImpegnoOldBilancio.getNumero() == null) {
			return null;
		}
		List<SiacTMovgestTsFin> movgests = siacTMovgestTsRepository.findSubMovgestTsByCodeAndMovgestId(datiOperazione.getSiacTEnteProprietario().getUid(), getNow(), impegnoNewBilancio.getUid(), subImpegnoOldBilancio.getNumero().toString());
		if(movgests == null || movgests.isEmpty()) {
			return null;
		}
		SiacTMovgestTsFin st = movgests.get(0);
		SubImpegno subImpegnoAnnoBilancioPiuUno = new SubImpegno();
		subImpegnoAnnoBilancioPiuUno.setUid(st.getMovgestTsId());
		return subImpegnoAnnoBilancioPiuUno;
	}
	
	private List<StoricoImpegnoAccertamento> caricaStoricoImpegnoAccertamento(int annoRiaccertato, BigDecimal numeroRiaccertato, Bilancio bilancioOld, Ente ente) {
		ParametroRicercaStoricoImpegnoAccertamento parametroRicercaStoricoImpegnoAccertamento = new ParametroRicercaStoricoImpegnoAccertamento();
		parametroRicercaStoricoImpegnoAccertamento.setBilancio(bilancioOld);
		parametroRicercaStoricoImpegnoAccertamento.setStoricoImpegnoAccertamento(new StoricoImpegnoAccertamento());
		parametroRicercaStoricoImpegnoAccertamento.getStoricoImpegnoAccertamento().setImpegno(new Impegno());
		parametroRicercaStoricoImpegnoAccertamento.getStoricoImpegnoAccertamento().getImpegno().setAnnoMovimento(annoRiaccertato);
		parametroRicercaStoricoImpegnoAccertamento.getStoricoImpegnoAccertamento().getImpegno().setNumero(numeroRiaccertato);
		parametroRicercaStoricoImpegnoAccertamento.getStoricoImpegnoAccertamento().setEnte(ente);
		return storicoImpegnoAccertamentoDad.ricercaSinteticaStorico(ente, parametroRicercaStoricoImpegnoAccertamento, 0, Integer.MAX_VALUE);
	}

	
	/**
	 * Controllo nato per la jira SIAC-5915 
	 * @param nuovoImportoLiq
	 * @param ente
	 * @param liquidazioneId
	 * @param importoLiquidazioneResidua
	 * @return
	 */
	private Errore controlloGiaPagatoLiqResidua(BigDecimal nuovoImportoLiq, Ente ente,Integer liquidazioneId, BigDecimal importoLiquidazioneResidua){
		Errore errore = null;
		BigDecimal disponibilitaPagareResidua = calcolaDisponibiltaPagare(ente, liquidazioneId, importoLiquidazioneResidua,null);
		BigDecimal giaPagato = importoLiquidazioneResidua.subtract(disponibilitaPagareResidua);
		if(nuovoImportoLiq.compareTo(giaPagato)<0){
			return ErroreBil.DISPONIBILITA_INSUFFICIENTE.getErrore("Aggiornamento Liquidazione residua non possibile, disponibilita' a pagare "+CommonUtils.convertiBigDecimalToImporto(disponibilitaPagareResidua));
		}
		//
		return errore;
	}

	/**
	 * Metodo per l'aggiornamento effettivo della liquidazione.
	 * 
	 * @param liquidazioneInput
	 * @param annoEsercizio
	 * @param loginOperazione
	 * @param idEnte
	 * @param bilancio
	 * @param datiOperazioneDto
	 * @param timestampInserimento
	 * @return
	 */
	private Liquidazione aggiornamentoLiquidazione(Liquidazione liquidazioneInput, String annoEsercizio,String loginOperazione,String codiceAmbito, int idEnte, Bilancio bilancio,
			DatiOperazioneDto datiOperazioneDto, Timestamp timestampInserimento) {
		Liquidazione liqReturn = null;

		// 1 find liquidazione SiacTLiquidazioneFin

		Integer anno = liquidazioneInput.getAnnoLiquidazione();
		BigDecimal numero = liquidazioneInput.getNumeroLiquidazione();

		SiacTLiquidazioneFin siacTLiquidazioneUpdate = siacTLiquidazioneRepository.findLiquidazioneByAnnoNumeroBilancio(idEnte, anno, numero,timestampInserimento, bilancio.getUid());
		
		//la presenza ordinativi ci serve per gestire l'aggiornamento dei campi,
		//in particolare:
		// - se presenzaOrdinativi == true l'unico campo aggiornabile sara' l'importo della liquidazione
		// - se presenzaOrdinativi == false tutti i campi sono modificabili
		entityRefresh(siacTLiquidazioneUpdate);//per sicurezza assoluta prima di leggere gli ordinativi..
		List<SiacTOrdinativoFin> ordinativiCollegati = getOrdinativiValidiNonAnnullati(siacTLiquidazioneUpdate);
		boolean presenzaOrdinativi = !StringUtils.isEmpty(ordinativiCollegati);
		//
		
		if (siacTLiquidazioneUpdate != null) {

			if(!presenzaOrdinativi){
				//invochiamo il metodo che aggiorna tutti i dati :
				liqReturn = aggiornaDatiLiquidazioneCore(liquidazioneInput, siacTLiquidazioneUpdate, timestampInserimento, idEnte, datiOperazioneDto, codiceAmbito);
			} else {
				//invochiamo il metodo che aggiorna solo l'importo:
				liqReturn = aggiornaImportoLiquidazioneCore(liquidazioneInput.getImportoLiquidazione(), siacTLiquidazioneUpdate, timestampInserimento, idEnte, datiOperazioneDto, codiceAmbito);
			}
			
			
		}
		//Termino restituendo l'oggetto di ritorno: 
        return liqReturn;
	}
	
	
	/**
	 * Aggiorna il solo stato della liquidazione
	 * @param liquidazione
	 * @param bilancio
	 * @param loginOperazione
	 * @param idEnte
	 * @param datiOperazioneDto
	 * @return
	 */
	public Liquidazione aggiornaLiquidazioneModulare(Liquidazione liquidazione, 
			Integer idEnte, 
			Bilancio bilancio, 
			Richiedente richiedente,
			boolean flagAggiornaStato, boolean flagAggiornaFlagManuale, 
			boolean flagAggiornaContoTesoreria, boolean flagAggiornaModalitaPagamento) {
		
		final String methodName = "aggiornaLiquidazioneModulare";
		
		Liquidazione liqReturn = new Liquidazione();
		
		//System.out.println("Richiamo aggiornaLiquidazioneModulare");
		
		Integer anno = liquidazione.getAnnoLiquidazione();
		BigDecimal numero = liquidazione.getNumeroLiquidazione();

		// Preparo l'ente db e l'oggetto datiOperazione di appoggio
		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository.findOne(idEnte);
		DatiOperazioneDto datiOperazioneDto = new DatiOperazioneDto(getNow().getTime(), Operazione.MODIFICA, siacTEnteProprietario, null, richiedente.getAccount().getId(), bilancio.getAnno());
				
		SiacTLiquidazioneFin siacTLiquidazione = siacTLiquidazioneRepository.findLiquidazioneByAnnoNumeroBilancio(idEnte, anno, numero, getNow(), bilancio.getUid());
		
		if (siacTLiquidazione != null) {

			if(flagAggiornaStato){
				
				aggiornaStato(liquidazione, liqReturn, datiOperazioneDto, siacTLiquidazione);
								
				//carico l'impegno/sub e il flagGsa che serve all'innesto 				
				List<SiacRLiquidazioneMovgestFin> rLiqMovGests  = siacTLiquidazione.getSiacRLiquidazioneMovgests();

				for (SiacRLiquidazioneMovgestFin rLiquidazioneMovgest : rLiqMovGests) {
								
					if (rLiquidazioneMovgest.getDataFineValidita() == null && rLiquidazioneMovgest.getDataCancellazione() == null) { 
						
						Impegno impegno = new Impegno();

						boolean liquidazioneLegataASubImpegno = rLiquidazioneMovgest.getSiacTMovgestTs().getMovgestTsIdPadre() !=null;
						
						if (liquidazioneLegataASubImpegno) {
							
							SubImpegno subImpegno = new SubImpegno();
							List<SubImpegno> listSubs = new ArrayList<SubImpegno>();
							
							subImpegno.setNumero(new BigDecimal(rLiquidazioneMovgest.getSiacTMovgestTs().getMovgestTsCode()));
							subImpegno.setUid(rLiquidazioneMovgest.getSiacTMovgestTs().getUid());
							
							// mi serve anche il PDC
							SiacTMovgestTsFin siacMovgestTs = rLiquidazioneMovgest.getSiacTMovgestTs();
							popolaPdcByMovimentoGestione(subImpegno, siacMovgestTs);
							
							listSubs.add(subImpegno);
							
							impegno.setElencoSubImpegni(listSubs);
							


						} 
							
						//IMPEGNO!
						impegno.setNumero(rLiquidazioneMovgest.getSiacTMovgestTs().getSiacTMovgest().getMovgestNumero());
						impegno.setUid(rLiquidazioneMovgest.getSiacTMovgestTs().getSiacTMovgest().getUid());
						impegno.setAnnoMovimento(rLiquidazioneMovgest.getSiacTMovgestTs().getSiacTMovgest().getMovgestAnno());
							
						if(!liquidazioneLegataASubImpegno){
							// mi serve anche il PDC ma solo se è l'impegno a essere legato alla liquidazione
							popolaPdcByMovimentoGestione(impegno, rLiquidazioneMovgest.getSiacTMovgestTs());
						}
							
						// mi serve per l'innnesto anche il flagAttivaGsa!
						List<SiacRMovgestTsAttrFin> listaSiacRMovgestTsAttr = rLiquidazioneMovgest.getSiacTMovgestTs().getSiacRMovgestTsAttrs();
						
						popolaFlagGsaByImpegno(impegno, listaSiacRMovgestTsAttr);
						
						liqReturn.setImpegno(impegno);
					}
				}
			}
			
			if(flagAggiornaFlagManuale)
				impostaLiqManuale(liquidazione.getLiqManuale(), siacTLiquidazione);
			
			if(flagAggiornaContoTesoreria)
				impostaContoTesoreria(liquidazione.getContoTesoreria(), idEnte , siacTLiquidazione);
			
			if(flagAggiornaModalitaPagamento)
				impostaModalitaDiPagamentoByTipoAccredito(siacTLiquidazione, liquidazione.getSoggettoLiquidazione().getModalitaPagamentoList());
			
			if(flagAggiornaFlagManuale || flagAggiornaContoTesoreria || flagAggiornaModalitaPagamento)
				siacTLiquidazioneRepository.saveAndFlush(siacTLiquidazione);				
			
			// RM 14/03/2017 jira SIAC-4592 Il servizio nuovo deve aggiornare anche l'importo della liquidazione 
			aggiornaImportoLiquidazione(liquidazione, siacTLiquidazione, idEnte, datiOperazioneDto);
			
		}else {
			log.error(methodName, "Errore imprevisto nella lettura della liquidazione anno/numero [ " + liquidazione.getAnnoLiquidazione() + "/" + liquidazione.getNumeroLiquidazione() + " ]");
			throw new BusinessException("AggiornaLiquidazioneModulare: Errore imprevisto nella lettura della liquidazione anno/numero [ " + liquidazione.getAnnoLiquidazione() + "/" + liquidazione.getNumeroLiquidazione() + " ]");
		}
		
		liqReturn.setUid(liquidazione.getUid());
		
        return liqReturn;
	}

	public void popolaFlagGsaByImpegno(Impegno impegno,
			List<SiacRMovgestTsAttrFin> listaSiacRMovgestTsAttr) {
		for(SiacRMovgestTsAttrFin siacRMovgestTsAttr : listaSiacRMovgestTsAttr){
			
			if(null!=siacRMovgestTsAttr && siacRMovgestTsAttr.getDataFineValidita()==null && siacRMovgestTsAttr.getDataCancellazione() == null){
				
				String codiceAttributo = siacRMovgestTsAttr.getSiacTAttr().getAttrCode();
				
				if(AttributoMovimentoGestione.FlagAttivaGsa.name().equalsIgnoreCase(codiceAttributo)){
					
					if(null!=siacRMovgestTsAttr.getBoolean_()){
						if(Constanti.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
							impegno.setFlagAttivaGsa(true);
						}else {
							impegno.setFlagAttivaGsa(false);
						}
					}				
				}
			
			}
		}
	}

	public void popolaPdcByMovimentoGestione(MovimentoGestione movgest,
			SiacTMovgestTsFin siacMovgestTs) {
		List<SiacRMovgestClassFin>  rMovgestClasses = siacMovgestTs.getSiacRMovgestClasses();
		
		if(rMovgestClasses!=null && !rMovgestClasses.isEmpty()){
			
			for(SiacRMovgestClassFin rMovgestClass : rMovgestClasses){
				
			
				if(null!=rMovgestClass && rMovgestClass.getDataFineValidita()== null ){
					
					String tipoCode = rMovgestClass.getSiacTClass().getSiacDClassTipo().getClassifTipoCode();
					
					if(StringUtils.contenutoIn(tipoCode, Constanti.getCodiciPianoDeiConti())){
						
						// PIANO DEI CONTI:
						movgest.setCodPdc(rMovgestClass.getSiacTClass().getClassifCode()); 
						movgest.setDescPdc(rMovgestClass.getSiacTClass().getClassifDesc());
						movgest.setIdPdc(rMovgestClass.getSiacTClass().getUid());	
							
					} 


				}

		
			}

		}
	}

	
	/**
	 * Preparo la liquidazione col conto tesoreria da aggiornare
	 * @param contoTesoreriaAgg
	 * @param idEnte
	 * @param liqReturn
	 * @param datiOperazioneDto
	 * @param siacTLiquidazione
	 */
	public void impostaContoTesoreria(ContoTesoreria contoTesoreriaAgg, Integer idEnte, SiacTLiquidazioneFin siacTLiquidazione) {
		
		SiacDContotesoreriaFin contoTesOld = siacTLiquidazione.getSiacDContotesoreria();

		if (contoTesOld != null && contoTesoreriaAgg != null) {
			String codNew = contoTesoreriaAgg.getCodice() == null ? "" : contoTesoreriaAgg.getCodice();
			String codOld = contoTesOld.getContotesCode() == null ? "" : contoTesOld.getContotesCode();
			
			if (!codNew.equals(codOld)) {
				SiacDContotesoreriaFin siacDContotesoreria = siacDContotesoreriaRepository.findContotesoreriaByCode(idEnte, codNew, getNow());
				if (siacDContotesoreria != null){
					siacTLiquidazione.setSiacDContotesoreria(siacDContotesoreria);
				}
			}
			
		} else if (contoTesOld != null && contoTesoreriaAgg == null) {
			
			siacTLiquidazione.setSiacDContotesoreria(null);
		
		} else if (contoTesOld == null && contoTesoreriaAgg != null) {
			
			SiacDContotesoreriaFin siacDContotesoreria = siacDContotesoreriaRepository.findContotesoreriaByCode(idEnte,contoTesoreriaAgg.getCodice(), getNow());
			
			if (siacDContotesoreria != null){
				siacTLiquidazione.setSiacDContotesoreria(siacDContotesoreria);
			}	
		}
	}
	
	

	/**
	 * Aggiorno lo stato
	 * @param liquidazione
	 * @param liqReturn
	 * @param datiOperazioneDto
	 * @param siacTLiquidazione
	 */
	public void aggiornaStato(Liquidazione liquidazione, Liquidazione liqReturn,
			DatiOperazioneDto datiOperazioneDto, SiacTLiquidazioneFin siacTLiquidazione) {
		
			// update stato liquidazione
			String statoOP = Constanti.statoOperativoLiquidazioneEnumToString(liquidazione.getStatoOperativoLiquidazione());
			
			List<SiacRLiquidazioneStatoFin> siacRLiquidazioneStatolist = siacTLiquidazione.getSiacRLiquidazioneStatos();
			
			if (siacRLiquidazioneStatolist != null && !siacRLiquidazioneStatolist.isEmpty()) {
				
				for (SiacRLiquidazioneStatoFin siacRLiquidazioneStato : siacRLiquidazioneStatolist) {

					if (siacRLiquidazioneStato != null && siacRLiquidazioneStato.getDataFineValidita() == null) {
						
						SiacDLiquidazioneStatoFin dStatoLiqOld = siacRLiquidazioneStato.getSiacDLiquidazioneStato();
							
						if (dStatoLiqOld != null && dStatoLiqOld.getLiqStatoCode() != null) {
								
							if (!dStatoLiqOld.getLiqStatoCode().equals(statoOP)) {
																	
								// annullo prima il legame vecchio e poi inserisco il nuovo
								DatiOperazioneDto datiOperazioneCancellazioneLogica = new DatiOperazioneDto(getCurrentMillisecondsTrentaMaggio2017(), Operazione.CANCELLAZIONE_LOGICA_RECORD, datiOperazioneDto.getSiacTEnteProprietario(), datiOperazioneDto.getAccountCode());
								DatiOperazioneUtils.cancellaRecord(siacRLiquidazioneStato, siacRLiquidazioneStatoRepository, datiOperazioneCancellazioneLogica, siacTAccountRepository);
								
								
								SiacDLiquidazioneStatoFin siacDLiquidazioneStato = siacDLiquidazioneStatoRepository.findDLiquidazioneStatoByCodeAndEnte(datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId(), statoOP, getNow());
								
								SiacRLiquidazioneStatoFin siacRLiquidazioneStatoAggiornato = new SiacRLiquidazioneStatoFin();
								
								siacRLiquidazioneStatoAggiornato.setSiacDLiquidazioneStato(siacDLiquidazioneStato);
								siacRLiquidazioneStatoAggiornato.setSiacTLiquidazione(siacTLiquidazione);
								
								datiOperazioneDto.setOperazione(Operazione.INSERIMENTO);
								siacRLiquidazioneStatoAggiornato = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRLiquidazioneStatoAggiornato,datiOperazioneDto,siacTAccountRepository);
								//salvo sul db:
								siacRLiquidazioneStatoRepository.saveAndFlush(siacRLiquidazioneStatoAggiornato);
								
								
								// Aggiorno l'oggetto di ritorno!									
								liqReturn.setIdLiquidazione(siacTLiquidazione.getLiqId());
								liqReturn.setNumeroLiquidazione(siacTLiquidazione.getLiqNumero());
								liqReturn.setAnnoLiquidazione(siacTLiquidazione.getLiqAnno());
								
								
								StatoOperativoLiquidazione statoOpLiquidazione = Constanti.statoOperativoLiquidazioneStringToEnum(siacDLiquidazioneStato.getLiqStatoCode());
								liqReturn.setStatoOperativoLiquidazione(statoOpLiquidazione);
									
							} else {
								// SIAC-6620: Lo stato e' il medesimo: non cambio alcunche' e ripopolo i dati in ritorno
								liqReturn.setIdLiquidazione(siacTLiquidazione.getLiqId());
								liqReturn.setNumeroLiquidazione(siacTLiquidazione.getLiqNumero());
								liqReturn.setAnnoLiquidazione(siacTLiquidazione.getLiqAnno());
								StatoOperativoLiquidazione statoOpLiquidazione = Constanti.statoOperativoLiquidazioneStringToEnum(dStatoLiqOld.getLiqStatoCode());
								liqReturn.setStatoOperativoLiquidazione(statoOpLiquidazione);
							}
						}
					}
				}
			}

	}
	
	/**
	 * Preparo la liquidazione con l'informazione liqManuale (automatico = A / manuale = M)
	 * @param liqManualeDaAggiornare
	 * @param siacTLiquidazione
	 */
	public void impostaLiqManuale(String liqManualeDaAggiornare, SiacTLiquidazioneFin siacTLiquidazione) {
		
		if (Constanti.LIQUIDAZIONE_AUTOMATICA.equalsIgnoreCase(liqManualeDaAggiornare)) {
			
			siacTLiquidazione.setLiqConvalidaManuale(Constanti.LIQUIDAZIONE_AUTOMATICA_CODE);
			
		} else if (Constanti.LIQUIDAZIONE_MAUALE.equalsIgnoreCase(liqManualeDaAggiornare)) {
			
			siacTLiquidazione.setLiqConvalidaManuale(Constanti.LIQUIDAZIONE_MAUALE_CODE);
		}
	}
	
	private Liquidazione aggiornaDatiLiquidazioneCore(Liquidazione liquidazioneInput, SiacTLiquidazioneFin siacTLiquidazioneUpdate
			,Timestamp timestampInserimento,int idEnte, DatiOperazioneDto datiOperazioneDto, String codiceAmbito){
	
		String oldLiqDesc = siacTLiquidazioneUpdate.getLiqDesc() == null ? "" : siacTLiquidazioneUpdate.getLiqDesc();
		String newLiqDesc = liquidazioneInput.getDescrizioneLiquidazione() == null ? "" : liquidazioneInput.getDescrizioneLiquidazione();
		
		if (!oldLiqDesc.equalsIgnoreCase(newLiqDesc)){
			siacTLiquidazioneUpdate.setLiqDesc(newLiqDesc);
		}
			
		BigDecimal liqImportoOld = siacTLiquidazioneUpdate.getLiqImporto() == null ? BigDecimal.ZERO : siacTLiquidazioneUpdate.getLiqImporto();
		BigDecimal liqImportoNew = liquidazioneInput.getImportoLiquidazione() == null ? BigDecimal.ZERO : liquidazioneInput.getImportoLiquidazione();

		if (liqImportoOld.compareTo(liqImportoNew) != 0){
			siacTLiquidazioneUpdate.setLiqImporto(liqImportoNew);
		}	
		String newLiqManuale = liquidazioneInput.getLiqManuale();

		if (Constanti.LIQUIDAZIONE_AUTOMATICA.equalsIgnoreCase(newLiqManuale)) {
			siacTLiquidazioneUpdate.setLiqConvalidaManuale(Constanti.LIQUIDAZIONE_AUTOMATICA_CODE);
		} else if (Constanti.LIQUIDAZIONE_MAUALE.equalsIgnoreCase(newLiqManuale)) {
			siacTLiquidazioneUpdate.setLiqConvalidaManuale(Constanti.LIQUIDAZIONE_MAUALE_CODE);
		}

		// update contotesoreria in siacTLiquidazione
		ContoTesoreria contoTesNew = liquidazioneInput.getContoTesoreria();
		SiacDContotesoreriaFin contoTesOld = siacTLiquidazioneUpdate.getSiacDContotesoreria();

		if (contoTesOld != null && contoTesNew != null) {
			String codNew = contoTesNew.getCodice() == null ? "" : contoTesNew.getCodice();
			String codOld = contoTesOld.getContotesCode() == null ? "" : contoTesOld.getContotesCode();
			if (!codNew.equals(codOld)) {
				SiacDContotesoreriaFin siacDContotesoreria = siacDContotesoreriaRepository.findContotesoreriaByCode(idEnte, codNew,timestampInserimento);
				if (siacDContotesoreria != null){
					siacTLiquidazioneUpdate.setSiacDContotesoreria(siacDContotesoreria);
				}
			}
		} else if (contoTesOld != null && contoTesNew == null) {
			siacTLiquidazioneUpdate.setSiacDContotesoreria(null);
		} else if (contoTesOld == null && contoTesNew != null) {
			SiacDContotesoreriaFin siacDContotesoreria = siacDContotesoreriaRepository.findContotesoreriaByCode(idEnte,contoTesNew.getCodice(), timestampInserimento);
			if (siacDContotesoreria != null){
				siacTLiquidazioneUpdate.setSiacDContotesoreria(siacDContotesoreria);
			}	
		}

		// update distinta in siacTLiquidazione
		Distinta distintaNew = liquidazioneInput.getDistinta();
		SiacDDistintaFin distintaOld = siacTLiquidazioneUpdate.getSiacDDistinta();
	
		if (distintaOld != null && distintaNew != null) {
			String codNew = distintaNew.getCodice() == null ? "": distintaNew.getCodice();
			String codOld = distintaOld.getDistCode() == null ? "": distintaOld.getDistCode();
			if (!codNew.equals(codOld)) {
				SiacDDistintaFin siacDDistinta = siacDDistintaRepository.findDistintaByCode(idEnte, codNew,timestampInserimento, Constanti.D_DISTINTA_TIPO_SPESA);
				if (siacDDistinta != null){
					siacTLiquidazioneUpdate.setSiacDDistinta(siacDDistinta);
				}	
			}
	
		} else if (distintaOld != null && distintaNew == null) {
			siacTLiquidazioneUpdate.setSiacDDistinta(null);
	
		} else if (distintaOld == null && distintaNew != null) {
			SiacDDistintaFin siacDDistinta = siacDDistintaRepository.findDistintaByCode(idEnte, distintaNew.getCodice(),timestampInserimento, Constanti.D_DISTINTA_TIPO_SPESA);
			if (siacDDistinta != null){
				siacTLiquidazioneUpdate.setSiacDDistinta(siacDDistinta);
			}	
		}

		// update mod pagamento
		List<ModalitaPagamentoSoggetto> modPagSoggList = liquidazioneInput.getSoggettoLiquidazione().getModalitaPagamentoList();

		// jira 2874
		// salvo semplicemente l'id che arriva sulla liquidazione, in lettura andra' poi a discriminare
		// e' stata eliminata la FK verso la siacTmodpag
		impostaModalitaDiPagamentoByTipoAccredito(siacTLiquidazioneUpdate,modPagSoggList);
		
		//SIOPE PLUS:
		// (attenzione: siope plus va qui prima del saveAndFlush su siacTLiquidazioneInsert
		//  perche' sono colonne di fk dentro SiacTLiquidazioneFin )
		impostaDatiSiopePlus(siacTLiquidazioneUpdate, liquidazioneInput, datiOperazioneDto);
		
		siacTLiquidazioneUpdate = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTLiquidazioneUpdate,datiOperazioneDto, siacTAccountRepository);
		
		//salvo sul db:
		siacTLiquidazioneUpdate = siacTLiquidazioneRepository.saveAndFlush(siacTLiquidazioneUpdate);

		AttributoTClassInfoDto attributoInfo = new AttributoTClassInfoDto();
		attributoInfo.setTipoOggetto(OggettoDellAttributoTClass.T_LIQUIDAZIONE);
		attributoInfo.setSiacTLiquidazione(siacTLiquidazioneUpdate);

		// update stato liquidazione
		String statoOP = Constanti.statoOperativoLiquidazioneEnumToString(liquidazioneInput.getStatoOperativoLiquidazione());
		List<SiacRLiquidazioneStatoFin> siacRLiquidazioneStatolist = siacTLiquidazioneUpdate.getSiacRLiquidazioneStatos();
		if (siacRLiquidazioneStatolist != null && !siacRLiquidazioneStatolist.isEmpty()) {
			for (SiacRLiquidazioneStatoFin siacRLiquidazioneStato : siacRLiquidazioneStatolist) {

				if (siacRLiquidazioneStato != null) {
					if (siacRLiquidazioneStato.getDataFineValidita() == null) {
						SiacDLiquidazioneStatoFin dStatoLiqOld = siacRLiquidazioneStato.getSiacDLiquidazioneStato();
						if (dStatoLiqOld != null && dStatoLiqOld.getLiqStatoCode() != null) {
							if (!dStatoLiqOld.getLiqStatoCode().equals(statoOP)) {
																
								// annullo prima il legame vecchio e poi inserisco il nuovo
								DatiOperazioneDto datiOperazioneCancellazioneLogica = new DatiOperazioneDto(getCurrentMillisecondsTrentaMaggio2017(), Operazione.CANCELLAZIONE_LOGICA_RECORD, datiOperazioneDto.getSiacTEnteProprietario(), datiOperazioneDto.getAccountCode());
								DatiOperazioneUtils.cancellaRecord(siacRLiquidazioneStato, siacRLiquidazioneStatoRepository, datiOperazioneCancellazioneLogica, siacTAccountRepository);
								
								
								SiacDLiquidazioneStatoFin siacDLiquidazioneStato = siacDLiquidazioneStatoRepository.findDLiquidazioneStatoByCodeAndEnte(idEnte, statoOP,timestampInserimento);
								
								SiacRLiquidazioneStatoFin siacRLiquidazioneStatoAggiornato = new SiacRLiquidazioneStatoFin();
								
								siacRLiquidazioneStatoAggiornato.setSiacDLiquidazioneStato(siacDLiquidazioneStato);
								siacRLiquidazioneStatoAggiornato.setSiacTLiquidazione(siacTLiquidazioneUpdate);
								
								datiOperazioneDto.setOperazione(Operazione.INSERIMENTO);
								siacRLiquidazioneStatoAggiornato = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRLiquidazioneStatoAggiornato,datiOperazioneDto,siacTAccountRepository);
								//salvo sul db:
								siacRLiquidazioneStatoRepository.saveAndFlush(siacRLiquidazioneStatoAggiornato);
							}
						}
					}
				}
			}
		}

		// update Sede Sec Soggetto-Liquidazione in relazione
		// siacRLiquidazioneSoggettoRepository

		Soggetto soggInput = liquidazioneInput.getSoggettoLiquidazione();

		if (soggInput != null) {

			List<SedeSecondariaSoggetto> listaSediSec = soggInput.getSediSecondarie();
			SedeSecondariaSoggetto sedeSecondariaSoggetto = null;
			if (listaSediSec != null && !listaSediSec.isEmpty() && listaSediSec.size() > 0) {
				sedeSecondariaSoggetto = listaSediSec.get(0);
			}
			String codSoggetto = soggInput.getCodiceSoggetto();
			if (sedeSecondariaSoggetto != null) {
				SiacTSoggettoFin siacTSoggettoNew = siacTSoggettoRepository.findOne(sedeSecondariaSoggetto.getUid());
				aggiornaSoggetto(idEnte, siacTLiquidazioneUpdate,siacTSoggettoNew, datiOperazioneDto);
			} else {
				SiacTSoggettoFin siacTSoggettoNew = siacTSoggettoRepository.ricercaSoggettoNoSeSede(codiceAmbito, idEnte, codSoggetto,Constanti.SEDE_SECONDARIA, getNow());
				aggiornaSoggetto(idEnte, siacTLiquidazioneUpdate, siacTSoggettoNew, datiOperazioneDto);
			}
		}

		// update AttoAmm

		SiacTAttoAmmFin siacTAttoAmmNew = getAttoAmministrativo(liquidazioneInput, idEnte, timestampInserimento);

		if (siacTAttoAmmNew != null) {

			List<SiacRLiquidazioneAttoAmmFin> listaRLiquidazioneAttoAmm = siacTLiquidazioneUpdate.getSiacRLiquidazioneAttoAmms();

			if (listaRLiquidazioneAttoAmm != null && !listaRLiquidazioneAttoAmm.isEmpty()) {
				for (SiacRLiquidazioneAttoAmmFin siacRLiquidazioneAttoAmm : listaRLiquidazioneAttoAmm) {
					if (siacRLiquidazioneAttoAmm != null) {

						if (siacRLiquidazioneAttoAmm.getDataFineValidita() == null) {
							SiacTAttoAmmFin siacTAttoAmmOld = siacRLiquidazioneAttoAmm.getSiacTAttoAmm();
							if (siacTAttoAmmOld != null) {
								if (siacTAttoAmmNew.getUid() != siacTAttoAmmOld.getUid()) {
									siacRLiquidazioneAttoAmm.setSiacTAttoAmm(siacTAttoAmmNew);
									siacRLiquidazioneAttoAmm = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRLiquidazioneAttoAmm,datiOperazioneDto,siacTAccountRepository);
									//salvo sul db:
									siacRLiquidazioneAttoAmmRepository.saveAndFlush(siacRLiquidazioneAttoAmm);
								}
							}
						}
					}
				}
			}
		}

		// update attributi (CIG ),

		salvaAttributoCig(attributoInfo, datiOperazioneDto,liquidazioneInput.getCig());

		salvaTransazioneElementare(attributoInfo, datiOperazioneDto,liquidazioneInput);

		Liquidazione liqReturn = new Liquidazione();
		liqReturn.setIdLiquidazione(siacTLiquidazioneUpdate.getLiqId());
		liqReturn.setNumeroLiquidazione(siacTLiquidazioneUpdate.getLiqNumero());
		liqReturn.setAnnoLiquidazione(siacTLiquidazioneUpdate.getLiqAnno());
		
		
		return liqReturn;
		
	}
	
	private Liquidazione aggiornaImportoLiquidazioneCore(BigDecimal nuovoImporto, SiacTLiquidazioneFin siacTLiquidazioneUpdate
			,Timestamp timestampInserimento,int idEnte, DatiOperazioneDto datiOperazioneDto, String codiceAmbito){
			
		BigDecimal liqImportoOld = siacTLiquidazioneUpdate.getLiqImporto() == null ? BigDecimal.ZERO : siacTLiquidazioneUpdate.getLiqImporto();
		BigDecimal liqImportoNew = nuovoImporto == null ? BigDecimal.ZERO : nuovoImporto;

		if (liqImportoOld.compareTo(liqImportoNew) != 0){
			siacTLiquidazioneUpdate.setLiqImporto(liqImportoNew);
		}	
		
		siacTLiquidazioneUpdate = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTLiquidazioneUpdate,datiOperazioneDto, siacTAccountRepository);
		
		//salvo sul db:
		siacTLiquidazioneUpdate = siacTLiquidazioneRepository.saveAndFlush(siacTLiquidazioneUpdate);

		Liquidazione liqReturn = new Liquidazione();
		liqReturn.setIdLiquidazione(siacTLiquidazioneUpdate.getLiqId());
		liqReturn.setNumeroLiquidazione(siacTLiquidazioneUpdate.getLiqNumero());
		liqReturn.setAnnoLiquidazione(siacTLiquidazioneUpdate.getLiqAnno());
		
		return liqReturn;
		
	}
	
	private void aggiornaSoggetto(Integer idEnte, SiacTLiquidazioneFin siacTLiquidazioneUpdate, SiacTSoggettoFin siacTSoggetto, DatiOperazioneDto datiOperazioneDto){
		if (siacTSoggetto != null) {
			List<SiacRLiquidazioneSoggettoFin> soggettoOld = siacRLiquidazioneSoggettoRepository.findByEnteAndLiquidazione(idEnte, getNow(), siacTLiquidazioneUpdate.getUid());
			if(soggettoOld!=null && soggettoOld.size()>0){
				SiacRLiquidazioneSoggettoFin soggettoPrecedente = soggettoOld.get(0);
				if(soggettoPrecedente.getSiacTSoggetto().getUid()!=siacTSoggetto.getUid()){
					//solo se e' cambiato il soggetto:
					SiacRLiquidazioneSoggettoFin siacRLiquidazioneSoggetto = new SiacRLiquidazioneSoggettoFin();
					
					siacRLiquidazioneSoggetto = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRLiquidazioneSoggetto,datiOperazioneDto,siacTAccountRepository);
					siacRLiquidazioneSoggetto.setSiacTLiquidazione(siacTLiquidazioneUpdate);
					siacRLiquidazioneSoggetto.setSiacTSoggetto(siacTSoggetto);
					//salvo sul db:
					siacRLiquidazioneSoggettoRepository.saveAndFlush(siacRLiquidazioneSoggetto);
					
					//ANNULLO L'OLD:
					for(SiacRLiquidazioneSoggettoFin oldIt: soggettoOld){
						DatiOperazioneUtils.annullaRecord(oldIt, siacRLiquidazioneSoggettoRepository, datiOperazioneDto, siacTAccountRepository);
					}
				}
			}
		}
	}

	@Override
	protected boolean checkStatoEntita(String stato) {
		return StatoOperativoLiquidazione.VALIDO.name().equals(stato);
	}

	@Override
	protected String determinaStatoEntita(Integer idEntita, Entita entita) {
		Liquidazione liq = (Liquidazione) entita;

		return liq.getStatoOperativoLiquidazione().name();
	}

	/**
	 * Operazione che controlla se una liquidazione ha degli ordinativi validi agganciati.
	 * 
	 * @param annoLiquidazione
	 * @param numeroLiquidazione
	 * @param annoEsercizio
	 * @param datiOperazioneDto
	 * @param richiedente
	 * @return
	 */
	public boolean checkOrdinativiAgganciati(Integer annoLiquidazione,
			BigDecimal numeroLiquidazione, String annoEsercizio,
			DatiOperazioneDto datiOperazioneDto, Richiedente richiedente) {

		boolean hasOrdinativi = false;

		SiacTLiquidazioneFin siacTLiquidazione = siacTLiquidazioneRepository.findLiquidazioneByAnnoNumeroAnnoBilancio(datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId(),annoLiquidazione, numeroLiquidazione, annoEsercizio,datiOperazioneDto.getTs());

		if (siacTLiquidazione != null) {
			List<SiacRLiquidazioneOrdFin> listaRLiquidazioneOrd = siacTLiquidazione.getSiacRLiquidazioneOrds();

			if (listaRLiquidazioneOrd != null && listaRLiquidazioneOrd.size() > 0) {
				for (SiacRLiquidazioneOrdFin siacRLiquidazioneOrd : listaRLiquidazioneOrd) {
					//SIAC-XXXX
					if (siacRLiquidazioneOrd.getDataCancellazione() == null && siacRLiquidazioneOrd.getSiacTOrdinativoT().getDataFineValidita() == null && siacRLiquidazioneOrd.getSiacTOrdinativoT().getDataCancellazione() == null && siacRLiquidazioneOrd.getSiacTOrdinativoT().getSiacTOrdinativo().getDataFineValidita() == null && siacRLiquidazioneOrd.getSiacTOrdinativoT().getSiacTOrdinativo().getDataCancellazione() == null) {					
						List<SiacROrdinativoStatoFin> listaROrdinativoStato = siacRLiquidazioneOrd.getSiacTOrdinativoT().getSiacTOrdinativo().getSiacROrdinativoStatos();
						for (SiacROrdinativoStatoFin siacROrdinativoStato : listaROrdinativoStato) {
							if (siacROrdinativoStato.getDataCancellazione() == null && siacROrdinativoStato.getDataFineValidita() == null && !siacROrdinativoStato.getSiacDOrdinativoStato().getOrdStatoCode().equalsIgnoreCase(Constanti.D_ORDINATIVO_STATO_ANNULLATO)) {
								hasOrdinativi = true;
								break;
							}
						}
					}
				}
			}
		}

		//Termino restituendo l'oggetto di ritorno: 
        return hasOrdinativi;
	}
	
	
	/**
	 * Operazione che controlla se una liquidazione è legata a un allegato atto
	 * 
	 * @param annoLiquidazione
	 * @param numeroLiquidazione
	 * @param annoEsercizio
	 * @param datiOperazioneDto
	 * @param richiedente
	 * @return
	 */
	public boolean checkLiquidazioneDaAllegatoAtto(Integer annoLiquidazione,
			BigDecimal numeroLiquidazione, Integer annoEsercizio,
			Ente ente, Richiedente richiedente) {

		boolean liquidazioneDaAllegatoAtto = false;
		
		Date now = new Date();

		SiacTLiquidazioneFin siacTLiquidazione = siacTLiquidazioneRepository.findLiquidazioneByAnnoNumeroAnnoBilancio(ente.getUid(), annoLiquidazione, numeroLiquidazione, String.valueOf(annoEsercizio), new Timestamp(now.getTime()));

		if (siacTLiquidazione != null) {
			
			List<SiacRSubdocLiquidazioneFin> listaRSubdocLiquidazione = siacTLiquidazione.getSiacRSubdocLiquidaziones();

			if (listaRSubdocLiquidazione != null && !listaRSubdocLiquidazione.isEmpty()) {
				
				for (SiacRSubdocLiquidazioneFin siacRSubdocLiquidazione : listaRSubdocLiquidazione) {
					
					if (siacRSubdocLiquidazione.getDataFineValidita() == null && siacRSubdocLiquidazione.getDataCancellazione() ==null) {
						
						if(siacRSubdocLiquidazione.getSiacTSubdoc()!=null && siacRSubdocLiquidazione.getSiacTSubdoc().getDataCancellazione()==null){
													
							List<SiacRElencoDocSubdocFin> listaRElencoDocSubdoc = siacRSubdocLiquidazione.getSiacTSubdoc().getSiacRElencoDocSubdocs();
							
							if(listaRElencoDocSubdoc!=null && !listaRElencoDocSubdoc.isEmpty()){
																
								for (SiacRElencoDocSubdocFin siacRElencoDocSubdoc : listaRElencoDocSubdoc) {
									
									if (siacRElencoDocSubdoc.getDataFineValidita() == null && 
											siacRElencoDocSubdoc.getDataCancellazione() ==null &&
											siacRElencoDocSubdoc.getSiacTElencoDoc()!=null && siacRElencoDocSubdoc.getSiacTElencoDoc().getDataCancellazione()==null) {
										
										List<SiacRAttoAllegatoElencoDocFin> listaRAttoAllegato = siacRElencoDocSubdoc.getSiacTElencoDoc().getSiacRAttoAllegatos();
										
										if(listaRAttoAllegato!=null && !listaRAttoAllegato.isEmpty()){
										
											for (SiacRAttoAllegatoElencoDocFin siacRAttoAllegato : listaRAttoAllegato) {
																								
												if(siacRAttoAllegato.getDataFineValidita()==null && siacRAttoAllegato.getDataCancellazione()==null &&
														siacRAttoAllegato.getSiacTAttoAllegato()!=null && siacRAttoAllegato.getSiacTAttoAllegato().getDataCancellazione()==null)
												{
													liquidazioneDaAllegatoAtto = true;
													break;
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

        return liquidazioneDaAllegatoAtto;
	}
	 
	
	
	/**
	 * Operazione che controlla se una liquidazione è legata a un impegno o sub
	 * 
	 * @param annoLiquidazione
	 * @param numeroLiquidazione
	 * @param annoEsercizio
	 * @param datiOperazioneDto
	 * @param richiedente
	 * @return
	 */
	public boolean checkLiquidazioneDaImpegnoOSub(Ente ente, Integer idMovgestTs) {

		boolean liquidazioneDaImpegnoOSub = false;
		
		Date now = new Date();

		List<SiacRLiquidazioneMovgestFin> listaRLiquidazioneMovgest = siacRLiquidazioneMovgestRepository.findByEnteAndMovGestTsId(ente.getUid(), new Timestamp(now.getTime()), idMovgestTs);

		if (listaRLiquidazioneMovgest != null && !listaRLiquidazioneMovgest.isEmpty()) {
				
			for (SiacRLiquidazioneMovgestFin siacRLiquidazioneMovgest : listaRLiquidazioneMovgest) {
				
				if (siacRLiquidazioneMovgest.getDataFineValidita() == null && siacRLiquidazioneMovgest.getDataCancellazione() ==null) {
					
					if(siacRLiquidazioneMovgest.getSiacTMovgestTs()!=null && siacRLiquidazioneMovgest.getSiacTMovgestTs().getDataCancellazione()==null){
						
						liquidazioneDaImpegnoOSub = true;
						break;
					
					}
				}
			}
			
		}

        return liquidazioneDaImpegnoOSub;
	}
	
	/**
	 * Ricerca le liquidazioni su piu anni 
	 * @param annoEsercizio
	 * @param atto
	 * @param idEnte
	 * @return
	 */
	public List<LiquidazioneAtti> ricercaEstesaLiquidazioni(Integer annoEsercizio, 
																 AttoAmministrativo atto,
																 Integer idEnte) {
	
		List<RicercaEstesaLiquidazioniDto> liquidazioniDto = new ArrayList<RicercaEstesaLiquidazioniDto>();
		
		liquidazioniDto = liquidazioneDao.ricercaEstesaLiquidazioni(annoEsercizio, atto, idEnte);
		
		return convertiLista(liquidazioniDto, LiquidazioneAtti.class, FinMapId.LiquidazioneDto_liquidazione);
	}
	
	
	private void aggiornaImportoLiquidazione(Liquidazione liquidazione, SiacTLiquidazioneFin siacTLiquidazione, Integer idEnte, DatiOperazioneDto datiOperazioneDto){
		
		
		String methodName= "aggiornaImportoLiquidazione";
		
			
		// Controllo di disponibilità della liquidazione
		if(!liquidazione.isForza()){
			//  Nel caso in cui il chiamante abbia indicato il campo FORZA = TRUE 
			//  questo controllo non deve essere effettuato (se non indicato il  campo forza si intende a FALSE)
			if (!liquidazione.getImportoLiquidazione().equals(siacTLiquidazione.getLiqImporto())) {
				
				BigDecimal newImportoLiquidazione = liquidazione.getImportoLiquidazione();
				
				log.debug(methodName,"newImportoLiquidazione: " + newImportoLiquidazione + "oldImportoLiquidazione: " + siacTLiquidazione.getLiqImporto());
				//CONTROLLI IMPORTO
				
				//1. L'importo della liquidazione deve essere maggiore di zero
				if (BigDecimal.ZERO.compareTo(newImportoLiquidazione) >= 0) {
					log.error(methodName,"Controllo 1--> L'importo della liquidazione deve essere maggiore di zero, disponibilita insufficiente");
					throw new BusinessException(ErroreFin.MOD_DISPONIBILITA_LIQUIDAZIONE_INSUFFICIENTE.getErrore(""));
				}

				
				//2. L'importo della liquidazione deve essere minore o uguale alla somma tra disponibilita' liquidare impegno e importo precedente
				BigDecimal disponibilitaLiquidareImpegno = BigDecimal.ZERO;
				if(liquidazione.getImpegno()!=null){
					disponibilitaLiquidareImpegno= liquidazione.getImpegno().getDisponibilitaLiquidare();
					
					log.debug(methodName,"disponibilitaLiquidareImpegno (impegno): " + disponibilitaLiquidareImpegno);
					
				}else if (liquidazione.getSubImpegno()!=null){
					disponibilitaLiquidareImpegno = liquidazione.getSubImpegno().getDisponibilitaLiquidare();
					
					log.debug(methodName,"disponibilitaLiquidareImpegno (sub): " + disponibilitaLiquidareImpegno);
				}else{
					
					log.error(methodName,"Controllo 2--> Errore nella lettura della disponibilita' a liquidare del movimento gestione.");
					throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Errore nella lettura della disponibilita' a liquidare del movimentto gestione."));
				}
				
				BigDecimal importoPrecedente = siacTLiquidazione.getLiqImporto();
				BigDecimal importoConfronto = disponibilitaLiquidareImpegno.add(importoPrecedente);
				if (importoConfronto != null) {
					if (newImportoLiquidazione.compareTo(importoConfronto) > 0) {
						
						log.error(methodName,"Controllo 3--> L'importo della liquidazione deve essere minore o uguale alla somma tra disponibilita' liquidare impegno e importo precedente.");
						throw new BusinessException(ErroreFin.MOD_DISPONIBILITA_LIQUIDAZIONE_INSUFFICIENTE.getErrore(""));
					}
				}
				
				//NUOVI CONTROLLI SULL'IMPORTO, SOLO SE LO STATO E' VALIDO:
				String statoLiq = getStatoCode(siacTLiquidazione);
				
				if (Constanti.LIQUIDAZIONE_STATO_VALIDO.equalsIgnoreCase(statoLiq)) {
					
					//3. L'importo della liquidazione deve essere maggiore uguale alla differenza di importo precedente meno disponibilita a pagare
					Ente ente = new Ente();
					ente.setUid(idEnte);
					BigDecimal disponibilitaPagareLiquidazione = calcolaDisponibiltaPagare(ente, siacTLiquidazione);
					importoConfronto = importoPrecedente.subtract(disponibilitaPagareLiquidazione);
					if (newImportoLiquidazione.compareTo(importoConfronto) < 0) {
						log.error(methodName,"Controllo 4--> L'importo della liquidazione deve essere maggiore uguale alla differenza di importo precedente meno disponibilita a pagare.");
						throw new BusinessException(ErroreFin.DISPONIBILITA_LIQUIDAZIONE_INSUFFICIENTE.getErrore(""));
					}
					
					//4. L'importo della liquidazione deve essere minore o uguale dell'importo precedente
					if(newImportoLiquidazione.compareTo(importoPrecedente)>0){
						log.error(methodName,"Controllo 5--> L'importo della liquidazione deve essere minore o uguale dell'importo precedente.");
						throw new BusinessException(ErroreCore.VALORE_NON_VALIDO.getErrore("importo", "(importo solo in diminuzione)"));
					}
				}
			}
		}
		
		// se tutti i controlli sono ok aggiorno l'importo!
		aggiornaImportoLiquidazioneCore(liquidazione.getImportoLiquidazione(), siacTLiquidazione, getNow(), idEnte, datiOperazioneDto, Constanti.AMBITO_FIN);
		
		log.trace(methodName,"Aggiornamento effettuato!");
	}


	public DisponibilitaMovimentoGestioneContainer calcolaDisponibilitaALiquidare(Liquidazione liquidazione, Integer idEnte, Integer annoBilancio, String uidAccount){
				
		String annoEsercizio = String.valueOf(annoBilancio);
		
		// leggiamo la siacTliq
		SiacTLiquidazioneFin tLiq = liquidazioneDao.ricercaLiquidazionePerChiave(idEnte, liquidazione.getAnnoLiquidazione(), liquidazione.getNumeroLiquidazione(), getNow(), annoEsercizio);
		
		List<SiacRLiquidazioneMovgestFin> listaRLiquidazioneMovgest = tLiq.getSiacRLiquidazioneMovgests();
		
		SiacTMovgestTsFin tMovgestTs = new SiacTMovgestTsFin();
		
		for (SiacRLiquidazioneMovgestFin siacRLiquidazioneMovgestFin : listaRLiquidazioneMovgest) {
			
			if(siacRLiquidazioneMovgestFin!=null && siacRLiquidazioneMovgestFin.getDataCancellazione()==null){
				tMovgestTs = siacRLiquidazioneMovgestFin.getSiacTMovgestTs();
				break;
			}
		}
		
		SiacTEnteProprietarioFin tEnte = new SiacTEnteProprietarioFin();
		tEnte.setUid(idEnte);
		DatiOperazioneDto datiOperazione = new DatiOperazioneDto(getCurrentMillisecondsTrentaMaggio2017(), Operazione.RICERCA, tEnte, uidAccount);
		return impegnoDad.calcolaDisponibilitaALiquidare(tMovgestTs.getUid(), datiOperazione,annoEsercizio);
	}

	public void flushAndClear()
	{
		liquidazioneDao.flushAndClear();
	}
}
