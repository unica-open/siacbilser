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
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.CommonUtils;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.EntitaUtils;
import it.csi.siac.siacfinser.integration.dao.carta.CartaContabileDao;
import it.csi.siac.siacfinser.integration.dao.carta.SiacDCartacontStatoRepository;
import it.csi.siac.siacfinser.integration.dao.carta.SiacDCommissioniEsteroRepository;
import it.csi.siac.siacfinser.integration.dao.carta.SiacDValutaFinRepository;
import it.csi.siac.siacfinser.integration.dao.carta.SiacRCartacontDetModpagRepository;
import it.csi.siac.siacfinser.integration.dao.carta.SiacRCartacontDetMovgestTRepository;
import it.csi.siac.siacfinser.integration.dao.carta.SiacRCartacontDetSoggettoRepository;
import it.csi.siac.siacfinser.integration.dao.carta.SiacRCartacontDetSubdocRepository;
import it.csi.siac.siacfinser.integration.dao.carta.SiacRCartacontStatoRepository;
import it.csi.siac.siacfinser.integration.dao.carta.SiacTCartaContDetRepository;
import it.csi.siac.siacfinser.integration.dao.carta.SiacTCartaContEsteraRepository;
import it.csi.siac.siacfinser.integration.dao.carta.SiacTCartaContRepository;
import it.csi.siac.siacfinser.integration.dao.common.SiacDAmbitoRepository;
import it.csi.siac.siacfinser.integration.dao.common.dto.AttributoTClassInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.CollegaQuotaDocumentoARigaCartaInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoGestioneCartaContabileDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoRicercaMovimentoPkDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OggettoDellAttributoTClass;
import it.csi.siac.siacfinser.integration.dao.common.dto.PaginazioneSubMovimentiDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaCartaContabileParamDto;
import it.csi.siac.siacfinser.integration.dao.liquidazione.SiacDContotesoreriaFinRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTMovgestRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTMovgestTsRepository;
import it.csi.siac.siacfinser.integration.dao.mutuo.SiacRMutuoVoceCartacontDetRepository;
import it.csi.siac.siacfinser.integration.dao.mutuo.SiacTMutuoVoceRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacROrdinativoAttoAmmRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacRSoggettoRelazFinRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacTModpagFinRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacTSoggettoFinRepository;
import it.csi.siac.siacfinser.integration.dao.subdoc.SiacTSubdocFinRepository;
import it.csi.siac.siacfinser.integration.entity.SiacDAmbitoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDCartacontStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDCommissioniesteroFin;
import it.csi.siac.siacfinser.integration.entity.SiacDContotesoreriaFin;
import it.csi.siac.siacfinser.integration.entity.SiacDDocStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDDocTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDValutaFin;
import it.csi.siac.siacfinser.integration.entity.SiacRCartacontDetModpagFin;
import it.csi.siac.siacfinser.integration.entity.SiacRCartacontDetMovgestTFin;
import it.csi.siac.siacfinser.integration.entity.SiacRCartacontDetSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRCartacontDetSubdocFin;
import it.csi.siac.siacfinser.integration.entity.SiacRCartacontStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRDocStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMutuoVoceCartacontDet;
import it.csi.siac.siacfinser.integration.entity.SiacRSubdocMovgestTFin;
import it.csi.siac.siacfinser.integration.entity.SiacTAttoAmmFin;
import it.csi.siac.siacfinser.integration.entity.SiacTBilFin;
import it.csi.siac.siacfinser.integration.entity.SiacTCartacontDetFin;
import it.csi.siac.siacfinser.integration.entity.SiacTCartacontEsteraFin;
import it.csi.siac.siacfinser.integration.entity.SiacTCartacontFin;
import it.csi.siac.siacfinser.integration.entity.SiacTDocFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModpagFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMutuoVoceFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSubdocFin;
import it.csi.siac.siacfinser.integration.entity.mapping.FinMapId;
import it.csi.siac.siacfinser.integration.util.DatiOperazioneUtils;
import it.csi.siac.siacfinser.integration.util.ObjectStreamerHandler;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.carta.CartaContabile;
import it.csi.siac.siacfinser.model.carta.CartaContabile.StatoOperativoCartaContabile;
import it.csi.siac.siacfinser.model.carta.CartaEstera;
import it.csi.siac.siacfinser.model.carta.PreDocumentoCarta;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.mutuo.VoceMutuo;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaCartaContabile;
import it.csi.siac.siacfinser.model.ric.RicercaCartaContabileK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto.StatoOperativoSedeSecondaria;


@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class CartaContabileDad extends AbstractFinDad {

	@Autowired
	ObjectStreamerHandler objectStreamerHandler;

	@Autowired
	CartaContabileDao cartaContabileDao;

	@Autowired
	SoggettoFinDad soggettoDad;

	@Autowired
	ImpegnoOttimizzatoDad impegnoOttimizzatoDad;

	@Autowired
	SiacTCartaContDetRepository siacTCartaContDetRepository;

	@Autowired
	SiacTSubdocFinRepository siacTSubdocRepository;

	@Autowired
	SiacRCartacontDetSubdocRepository siacRCartacontDetSubdocRepository;

	@Autowired
	SiacTCartaContRepository siacTCartaContRepository;

	@Autowired
	SiacRCartacontStatoRepository siacRCartacontStatoRepository;

	@Autowired
	SiacDCartacontStatoRepository siacDCartacontStatoRepository;

	@Autowired
	SiacDAmbitoRepository siacDAmbitoRepository;

	@Autowired
	SiacROrdinativoAttoAmmRepository siacROrdinativoAttoAmmRepository;

	@Autowired
	SiacDCommissioniEsteroRepository siacDCommissioniEsteroRepository;

	@Autowired
	SiacDValutaFinRepository siacDValutaRepository;

	@Autowired
	SiacTCartaContEsteraRepository siacTCartaContEsteraRepository;

	@Autowired
	SiacDContotesoreriaFinRepository siacDContotesoreriaRepository;

	@Autowired
	SiacTModpagFinRepository siacTModpagRepository;
	
	@Autowired
	SiacRSoggettoRelazFinRepository siacRSoggettoRelazFinRepository;

	@Autowired
	SiacRCartacontDetModpagRepository siacRCartacontDetModpagRepository;

	@Autowired
	SiacRCartacontDetSoggettoRepository siacRCartacontDetSoggettoRepository;

	@Autowired
	SiacTSoggettoFinRepository siacTSoggettoRepository;

	@Autowired
	SiacTMovgestTsRepository siacTMovgestTsRepository;
	
	@Autowired
	SiacTMutuoVoceRepository siacTMutuoVoceRepository;

	@Autowired
	SiacTMovgestRepository siacTMovgestRepository;

	@Autowired
	SiacRCartacontDetMovgestTRepository siacRCartacontDetMovgestTRepository;
	
	@Autowired
	SiacRMutuoVoceCartacontDetRepository siacRMutuoVoceCartacontDetRepository;

	/**
	 * converte ParametroRicercaCartaContabile (che e' un model) in un oggetto (non model) da poter passare al DaoImpl
	 * 
	 * @param prcc
	 * @return
	 */
	private RicercaCartaContabileParamDto convertForDao(ParametroRicercaCartaContabile prcc){
		RicercaCartaContabileParamDto convertito = new RicercaCartaContabileParamDto();

		convertito.setAnnoEsercizio(prcc.getAnnoEsercizio());
		convertito.setNumeroCartaContabileDa(prcc.getNumeroCartaContabileDa());
		convertito.setNumeroCartaContabileA(prcc.getNumeroCartaContabileA());
		convertito.setDataScadenzaDa(prcc.getDataScadenzaDa());
		convertito.setDataScadenzaA(prcc.getDataScadenzaA());
		convertito.setStatoOperativo(prcc.getStatoOperativo());
		convertito.setOggetto(prcc.getOggetto());
		convertito.setAnnoCapitolo(prcc.getAnnoCapitolo());
		convertito.setNumeroCapitolo(prcc.getNumeroCapitolo());
		convertito.setNumeroArticolo(prcc.getNumeroArticolo());
		convertito.setNumeroUEB(prcc.getNumeroUEB());
		convertito.setAnnoImpegno(prcc.getAnnoImpegno());
		convertito.setNumeroImpegno(prcc.getNumeroImpegno());
		convertito.setNumeroSubImpegno(prcc.getNumeroSubImpegno());
		convertito.setAnnoProvvedimento(prcc.getAnnoProvvedimento());
		convertito.setNumeroProvvedimento(prcc.getNumeroProvvedimento());
		convertito.setTipoProvvedimento(prcc.getTipoProvvedimento());
		convertito.setStrutturaAmministrativaProvvedimento(prcc.getStrutturaAmministrativaProvvedimento());
		convertito.setCodiceCreditore(prcc.getCodiceCreditore());
		convertito.setSubDocumentoSpesaId(prcc.getSubDocumentoSpesaId());

		//Termino restituendo l'oggetto di ritorno: 
        return convertito;
	}

	/**
	 * Metodo per la ricerca delle carte contabili utilizzato dal servizio RicercaCartaContabileService.
	 * 
	 * @param prm
	 * @param richiedente
	 * @param ente
	 * @param datiOperazione
	 * @param numeroPagina
	 * @param numeroRisultatiPerPagina
	 * @param cacheSoggetto
	 * @param cacheMdp
	 * @return
	 */
	public List<CartaContabile> ricercaCarteContabili(ParametroRicercaCartaContabile prm,  Richiedente richiedente, Ente ente, DatiOperazioneDto datiOperazione, 
			                                          int numeroPagina, int numeroRisultatiPerPagina, 
			                                          HashMap<String, Soggetto> cacheSoggetto,
			                                          HashMap<String, List<ModalitaPagamentoSoggetto>> cacheMdp){

		Integer idEnte = datiOperazione.getSiacTEnteProprietario().getEnteProprietarioId();

		List<SiacTCartacontFin> elencoSiacTCartacont = new ArrayList<SiacTCartacontFin>();
		List<CartaContabile> elencoCarteContabili = new ArrayList<CartaContabile>();

		RicercaCartaContabileParamDto params = convertForDao(prm);
		elencoSiacTCartacont = cartaContabileDao.ricercaCarteContabili(params, datiOperazione, numeroPagina, numeroRisultatiPerPagina);

		if(null!=elencoSiacTCartacont && elencoSiacTCartacont.size() > 0){
			for(SiacTCartacontFin siacTCartacont : elencoSiacTCartacont){

				// carta contabile : inizio
				CartaContabile cartaContabile = new CartaContabile();
				cartaContabile = map(siacTCartacont, CartaContabile.class, FinMapId.SiacTCartacont_CartaContabile);

				// attributi metadatati della carta contabile : inizio
				AttributoTClassInfoDto attributoInfo = new AttributoTClassInfoDto();
				attributoInfo.setTipoOggetto(OggettoDellAttributoTClass.T_CARTACONT);
				attributoInfo.setSiacTCartacont(siacTCartacont);

				// note
				String noteCc = getValoreAttr(attributoInfo, datiOperazione, idEnte, Constanti.T_ATTR_CODE_NOTE_CC);
				cartaContabile.setNote(noteCc);

				// motivo urgenza
				String motivoUrgenza = getValoreAttr(attributoInfo, datiOperazione, idEnte, Constanti.T_ATTR_CODE_MOTIVO_URGENZA);
				cartaContabile.setMotivoUrgenza(motivoUrgenza);
				// attributi metadatati della carta contabile : fine

				// carta contabile : fine

				// pre-documenti carta : inizio
				List<PreDocumentoCarta> elencoPreDocumentiCarta = new ArrayList<PreDocumentoCarta>();
				List<SiacTCartacontDetFin> elencoSiacTCartacontDet = siacTCartacont.getSiacTCartacontDets();
				BigDecimal totaleImportoDaRegolarizzareRigheCarta = BigDecimal.ZERO;
				BigDecimal totaleValutaEsteraRigheCarta = BigDecimal.ZERO;
				if(elencoSiacTCartacontDet!=null && elencoSiacTCartacontDet.size() > 0){
					for(SiacTCartacontDetFin siacTCartacontDet : elencoSiacTCartacontDet){
						if(siacTCartacontDet!=null && siacTCartacontDet.getDataFineValidita()==null){
							PreDocumentoCarta preDocumentoCarta = new PreDocumentoCarta();
							preDocumentoCarta = map(siacTCartacontDet, PreDocumentoCarta.class, FinMapId.SiacTCartacontDet_PreDocumentoCarta);

							// attributi metadatati del pre-documento carta : inizio
							attributoInfo = new AttributoTClassInfoDto();
							attributoInfo.setTipoOggetto(OggettoDellAttributoTClass.T_CARTACONT_DET);
							attributoInfo.setSiacTCartacontDet(siacTCartacontDet);

							//note
							String noteCcDet = getValoreAttr(attributoInfo, datiOperazione, idEnte, Constanti.T_ATTR_CODE_NOTE_CC);
							preDocumentoCarta.setNote(noteCcDet);
							
							// Jira-1461 
							// per ora questa parte e' commentata, in attesa di istruzioni per capire
							// se e' veramente necessario per il servizio tirare fuori tutti questi dati
							// oppure se bisogna tirar fuori solamente i dati base delle sottoentita
							// Soggetto, subimpegno e impegno
							
							// attributi metadatati del pre-documento carta : fine
/*
								// soggetto + eventuale sede secondaria + modalita' di pagamento : inizio
								Soggetto soggettoPreDocumento = new Soggetto();
								SedeSecondariaSoggetto sedeSecondariaPreDocumento = new SedeSecondariaSoggetto();
								String codSoggettoPreDocumento = null;
								boolean isSedeSecondaria = false;

								List<SiacRCartacontDetSoggettoFin> listaSiacRCartacontDetSoggetto = siacTCartacontDet.getSiacRCartacontDetSoggettos();
								if(listaSiacRCartacontDetSoggetto!=null && listaSiacRCartacontDetSoggetto.size() > 0){
									for(SiacRCartacontDetSoggettoFin siacRCartacontDetSoggetto : listaSiacRCartacontDetSoggetto){
										if(siacRCartacontDetSoggetto!=null && siacRCartacontDetSoggetto.getDataFineValidita()==null){
											codSoggettoPreDocumento = siacRCartacontDetSoggetto.getSiacTSoggetto().getSoggettoCode();
											
											// caching su soggetto
											if(cacheSoggetto==null){
//											
												soggettoPreDocumento = soggettoDad.ricercaSoggetto(idEnte, codSoggettoPreDocumento, false, true);
											
											}else{
												
												soggettoPreDocumento = cacheSoggetto.get(codSoggettoPreDocumento);
												
												if(null==soggettoPreDocumento){
													soggettoPreDocumento = soggettoDad.ricercaSoggetto(idEnte, codSoggettoPreDocumento, false, true);
													cacheSoggetto.put(codSoggettoPreDocumento, soggettoPreDocumento);
												}
												
											}
											Integer idSoggetto = siacRCartacontDetSoggetto.getSiacTSoggetto().getSoggettoId();
											if (soggettoDad.isSedeSecondaria(idSoggetto, idEnte)) {
												isSedeSecondaria = true;
												sedeSecondariaPreDocumento = soggettoDad.ricercaSedeSecondariaPerChiave(idEnte, codSoggettoPreDocumento, idSoggetto);
												List<SedeSecondariaSoggetto> listaSediSecondarie = new ArrayList<SedeSecondariaSoggetto>();
												listaSediSecondarie.add(sedeSecondariaPreDocumento);
												soggettoPreDocumento.setSediSecondarie(listaSediSecondarie);
											}
										}
									}
								}

								ModalitaPagamentoSoggetto modalitaPagamentoPreDocumento = new ModalitaPagamentoSoggetto();
								List<SiacRCartacontDetModpagFin> listaSiacRCartacontDetModpag = siacTCartacontDet.getSiacRCartacontDetModpags();
								if(listaSiacRCartacontDetModpag!=null && listaSiacRCartacontDetModpag.size() > 0){
									for(SiacRCartacontDetModpagFin siacRCartacontDetModpag : listaSiacRCartacontDetModpag){
										if(siacRCartacontDetModpag!=null && siacRCartacontDetModpag.getDataFineValidita()==null){
											
											List<ModalitaPagamentoSoggetto> listaModalitaPagamento = null;
//											cacheMdp
											
											if(null==cacheMdp){
											
												listaModalitaPagamento = soggettoDad.ricercaModalitaPagamentoPerChiave(idEnte, codSoggettoPreDocumento, siacRCartacontDetModpag.getSiacTModpag().getModpagId());
												
											}else{
												
												listaModalitaPagamento = cacheMdp.get(codSoggettoPreDocumento+"||"+siacRCartacontDetModpag.getSiacTModpag().getModpagId());
												
												if(null==listaModalitaPagamento){
													listaModalitaPagamento = soggettoDad.ricercaModalitaPagamentoPerChiave(idEnte, codSoggettoPreDocumento, siacRCartacontDetModpag.getSiacTModpag().getModpagId());
													cacheMdp.put(codSoggettoPreDocumento+"||"+siacRCartacontDetModpag.getSiacTModpag().getModpagId(), listaModalitaPagamento);
												}
												
											}
											
											if(listaModalitaPagamento!=null && listaModalitaPagamento.size() > 0){
												modalitaPagamentoPreDocumento = listaModalitaPagamento.get(0);
												List<ModalitaPagamentoSoggetto> elencoModalitaPagamento = new ArrayList<ModalitaPagamentoSoggetto>();
												elencoModalitaPagamento.add(modalitaPagamentoPreDocumento);
												if(isSedeSecondaria){
													soggettoPreDocumento.getSediSecondarie().get(0).setModalitaPagamentoSoggettos(elencoModalitaPagamento);
												} else {
													soggettoPreDocumento.setModalitaPagamentoList(elencoModalitaPagamento);
												}												
											}
										}
									}
								}

								preDocumentoCarta.setModalitaPagamentoSoggetto(modalitaPagamentoPreDocumento);
								preDocumentoCarta.setSedeSecondariaSoggetto(sedeSecondariaPreDocumento);
								preDocumentoCarta.setSoggetto(soggettoPreDocumento);
								// soggetto + eventuale sede secondaria + modalita' di pagamento : fine

								// impegno + eventuale sub-impegno : inizio
								Impegno impegnoPreDocumento = new Impegno();
								SubImpegno subImpegnoPreDocumento = new SubImpegno();

								List<SiacRCartacontDetMovgestTFin> listaSiacRCartacontDetMovgestT = siacTCartacontDet.getSiacRCartacontDetMovgestTs();
								if(listaSiacRCartacontDetMovgestT!=null && listaSiacRCartacontDetMovgestT.size() > 0){
									for(SiacRCartacontDetMovgestTFin siacRCartacontDetMovgestT : listaSiacRCartacontDetMovgestT){
										if(siacRCartacontDetMovgestT!=null && siacRCartacontDetMovgestT.getDataFineValidita()==null){

											String movgestTsTipoCode = siacRCartacontDetMovgestT.getSiacTMovgestT().getSiacDMovgestTsTipo().getMovgestTsTipoCode();

											String annoBilancio = siacRCartacontDetMovgestT.getSiacTMovgestT().getSiacTMovgest().getSiacTBil().getSiacTPeriodo().getAnno();
											Integer annoImpegno = siacRCartacontDetMovgestT.getSiacTMovgestT().getSiacTMovgest().getMovgestAnno();
											BigDecimal numeroImpegno = siacRCartacontDetMovgestT.getSiacTMovgestT().getSiacTMovgest().getMovgestNumero();

											impegnoPreDocumento = (Impegno) impegnoDad.ricercaMovimentoPk(richiedente, ente, annoBilancio, annoImpegno, numeroImpegno, Constanti.MOVGEST_TIPO_IMPEGNO, true);
											if(movgestTsTipoCode.equals(Constanti.MOVGEST_TS_TIPO_TESTATA)) {
												// il pre-documento e' legato ad un impegno

												preDocumentoCarta.setImpegno(impegnoPreDocumento);
												// pulisco l'eventuale sub rimasto da operazioni precedenti
												preDocumentoCarta.setSubImpegno(null);

											} else if (movgestTsTipoCode.equals(Constanti.MOVGEST_TS_TIPO_SUBIMPEGNO)) {
												// il pre-documento e' legato ad un sub-impegno
												Integer idSubImpegno = siacRCartacontDetMovgestT.getSiacTMovgestT().getMovgestTsId();

												List<SubImpegno> listSubEstratti = impegnoPreDocumento.getElencoSubImpegni();

												for (SubImpegno subImpegnoIterato : listSubEstratti){
													if (subImpegnoIterato.getUid()==idSubImpegno) {
														subImpegnoPreDocumento = subImpegnoIterato;
													}
												}

												preDocumentoCarta.setSubImpegno(subImpegnoPreDocumento);
												preDocumentoCarta.setImpegno(impegnoPreDocumento);
											}
										}
									}
								}
								// impegno + eventuale sub-impegno : fine
*/
							totaleImportoDaRegolarizzareRigheCarta = totaleImportoDaRegolarizzareRigheCarta.add(preDocumentoCarta.getImportoDaRegolarizzare());
							if (preDocumentoCarta.getImportoValutaEstera()!=null) {
								totaleValutaEsteraRigheCarta = totaleValutaEsteraRigheCarta.add(preDocumentoCarta.getImportoValutaEstera()); 
							}

							elencoPreDocumentiCarta.add(preDocumentoCarta);
						}
					}
				}

				// Ordino per numero crescente la lista do PreDocumentiCarta
				if(elencoPreDocumentiCarta != null && elencoPreDocumentiCarta.size()>0){
					Collections.sort(elencoPreDocumentiCarta, new PreDocumentoCartaComparator());
				}

				cartaContabile.setListaPreDocumentiCarta(elencoPreDocumentiCarta);
				// pre-documenti carta : fine

				// carta contabile estera : inizio
				List<CartaEstera> elencoCarteContabiliEstere = new ArrayList<CartaEstera>();
				List<SiacTCartacontEsteraFin> elencoSiacTCartacontEstera = siacTCartacont.getSiacTCartacontEsteras();
				if(null!=elencoSiacTCartacontEstera && elencoSiacTCartacontEstera.size() > 0){
					for(SiacTCartacontEsteraFin siacTCartacontEstera : elencoSiacTCartacontEstera){
						if(siacTCartacontEstera!=null && siacTCartacontEstera.getDataFineValidita()==null){
							CartaEstera cartaEstera = new CartaEstera();
							cartaEstera = map(siacTCartacontEstera, CartaEstera.class, FinMapId.SiacTCartacontEstera_CartaEstera);
//							cartaEstera = EntityCartaContabileToModelCartaContabileConverter.siacTCartaContabileEsteraEntityToCartaContabileEsteraModel(siacTCartacontEstera, cartaEstera);

							// attributi metadatati della carta contabile estera : inizio
							attributoInfo = new AttributoTClassInfoDto();
							attributoInfo.setTipoOggetto(OggettoDellAttributoTClass.T_CARTACONT_ESTERA);
							attributoInfo.setSiacTCartacontEstera(siacTCartacontEstera);

							//tipo pagamento
							String isAssegno = getValoreAttr(attributoInfo, datiOperazione, idEnte, Constanti.T_ATTR_CODE_MODALITA_ACCREDITO_ASSEGNO);
							String isBonifico = getValoreAttr(attributoInfo, datiOperazione, idEnte, Constanti.T_ATTR_CODE_MODALITA_ACCREDITO_BONIFICO);
							String isAltro = getValoreAttr(attributoInfo, datiOperazione, idEnte, Constanti.T_ATTR_CODE_MODALITA_ACCREDITO_ALTRO);
							String isSpedizione = getValoreAttr(attributoInfo, datiOperazione, idEnte, Constanti.T_ATTR_CODE_RECAPITOASSEGNO_SPEDIZIONE);
							String isConsegna = getValoreAttr(attributoInfo, datiOperazione, idEnte, Constanti.T_ATTR_CODE_RECAPITOASSEGNO_CONSEGNA);

							if ("S".equalsIgnoreCase(isAssegno)) {
								cartaEstera.setTipologiaPagamento("Assegno");
								if ("S".equalsIgnoreCase(isSpedizione)) {
									cartaEstera.setMetodoConsegna("Da spedire");
								} else if ("S".equalsIgnoreCase(isConsegna)) {
									cartaEstera.setMetodoConsegna("Da consegnare");
								}
							} else if ("S".equalsIgnoreCase(isBonifico)) {
								cartaEstera.setTipologiaPagamento("Bonifico");
							} else if ("S".equalsIgnoreCase(isAltro)) {
								cartaEstera.setTipologiaPagamento("Altro");
							}
							// attributi metadatati della carta contabile estera : fine

							cartaEstera.setTotaleValutaEstera(totaleValutaEsteraRigheCarta);
							elencoCarteContabiliEstere.add(cartaEstera);
						}
					}
				}
				cartaContabile.setListaCarteEstere(elencoCarteContabiliEstere);
				// carta contabile estera : fine

				cartaContabile.setImportoDaRegolarizzare(totaleImportoDaRegolarizzareRigheCarta);
				elencoCarteContabili.add(cartaContabile);
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return elencoCarteContabili;
	}

	/**
	 * Calcola il numero di carte contabili che soddisfano i parametri di ricerca del servizio RicercaCartaContabileService.
	 * 
	 * @param prm
	 * @param datiOperazione
	 * @return
	 */
	public Long calcolaNumeroCarteContabiliDaEstrarre(ParametroRicercaCartaContabile prm, DatiOperazioneDto datiOperazione){
		Long conteggioCarteContabili = new Long(0);
		RicercaCartaContabileParamDto params = convertForDao(prm);
		conteggioCarteContabili = cartaContabileDao.contaCarteContabili(params, datiOperazione);
		//Termino restituendo l'oggetto di ritorno: 
        return conteggioCarteContabili;
	}

	/**
	 * Metodo per il calcolo del progressivo dei legami con SubDocumentiSpesa.
	 * Utilizzato nel servizio RegolarizzaCartaContabileService.
	 * 
	 * @param carta
	 * @param preDocumento
	 * @param datiOperazione
	 * @return
	 */
	public Integer calcolaProgressivoLegamiPreDocumentoCartaSubDocumenti(CartaContabile carta, PreDocumentoCarta preDocumento, DatiOperazioneDto datiOperazione){
		Integer progressivoLegamiRigaSubDocumenti = new Integer(0);

		Integer idEnte = datiOperazione.getSiacTEnteProprietario().getEnteProprietarioId();

		Integer annoBilancioCarta = carta.getBilancio().getAnno();
		Integer numeroCarta = carta.getNumero();
		Integer numeroPreDocumentoCarta = preDocumento.getNumero();

		SiacTCartacontDetFin siacTCartacontDet = siacTCartaContDetRepository.findCartaContDetByAnnoCartaNumeroCartaNumeroDetCarta(idEnte,
				numeroCarta,
				annoBilancioCarta.toString(),
				numeroPreDocumentoCarta,
				datiOperazione.getTs()); 

		if(siacTCartacontDet!=null){
			List<SiacRCartacontDetSubdocFin> elencoSiacRCartacontDetSubdoc = siacTCartacontDet.getSiacRCartacontDetSubdocs();
			if(elencoSiacRCartacontDetSubdoc!=null && elencoSiacRCartacontDetSubdoc.size()>0){
				for(SiacRCartacontDetSubdocFin siacRCartacontDetSubdoc : elencoSiacRCartacontDetSubdoc){
					if(siacRCartacontDetSubdoc!=null && siacRCartacontDetSubdoc.getDataFineValidita()==null){
						progressivoLegamiRigaSubDocumenti = progressivoLegamiRigaSubDocumenti + 1;
					}
				}
			}
			progressivoLegamiRigaSubDocumenti = progressivoLegamiRigaSubDocumenti + 1;
		}
		//Termino restituendo l'oggetto di ritorno: 
        return progressivoLegamiRigaSubDocumenti;
	}

	/**
	 * Operazione di regolarizzazione del predocumento.
	 * 
	 * @param carta
	 * @param preDocumento
	 * @param documento
	 * @param subDocumento
	 * @param datiOperazione
	 * @return
	 */
	public boolean regolarizzaPreDocumentoCarta(CartaContabile carta, PreDocumentoCarta preDocumento, DocumentoSpesa documento, SubdocumentoSpesa subDocumento, DatiOperazioneDto datiOperazione){
		boolean result = false;

		Integer idEnte = datiOperazione.getSiacTEnteProprietario().getEnteProprietarioId();

		Integer annoBilancioCarta = carta.getBilancio().getAnno();
		Integer numeroCarta = carta.getNumero();
		Integer numeroPreDocumentoCarta = preDocumento.getNumero();

		Integer annoDocumento = documento.getAnno();
		String numeroDocumento = documento.getNumero();
		Integer numeroSubDocumento = subDocumento.getNumero();
		String tipoDocumento = documento.getTipoDocumento().getCodice();

		SiacTCartacontDetFin siacTCartacontDet = siacTCartaContDetRepository.findCartaContDetByAnnoCartaNumeroCartaNumeroDetCarta(idEnte,
				numeroCarta,
				annoBilancioCarta.toString(),
				numeroPreDocumentoCarta,
				datiOperazione.getTs()); 

		SiacTSubdocFin siacTSubdoc = siacTSubdocRepository.findSubDocumentoByAnnoDocNumDocNumSubDoc(idEnte,
				annoDocumento,
				numeroDocumento,
				numeroSubDocumento,
				tipoDocumento,
				getNow());

		if(siacTCartacontDet!=null && siacTSubdoc!=null){
			// inserisco la relazione tra siac_t_cartacont_det e siac_t_subdoc
			// attraverso la tavola di relazione siac_r_cartacont_det_subdoc
			SiacRCartacontDetSubdocFin siacRCartacontDetSubdoc = new SiacRCartacontDetSubdocFin(); 
			siacRCartacontDetSubdoc = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRCartacontDetSubdoc, datiOperazione, siacTAccountRepository);

			siacRCartacontDetSubdoc.setSiacTCartacontDet(siacTCartacontDet);
			siacRCartacontDetSubdoc.setSiacTSubdoc(siacTSubdoc);
			siacRCartacontDetSubdocRepository.saveAndFlush(siacRCartacontDetSubdoc);

			// aggiorno i dati su siac_t_cartacont_det
			datiOperazione.setOperazione(Operazione.MODIFICA);
			siacTCartacontDet = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTCartacontDet, datiOperazione, siacTAccountRepository);
			siacTCartaContDetRepository.saveAndFlush(siacTCartacontDet);

			result = true;
		}


		//Termino restituendo l'oggetto di ritorno: 
        return result;
	}

	/**
	 * 
	 * Operazione : annullaCartaContabile
	 * 
	 * Descrizione dell'operazione :
	 * 
	 * Annulla logicamente la carta contabile ricevuta in input
	 * 
	 * @param ente
	 * @param richiedente
	 * @param bilancio
	 * @param cartaContabile
	 * @param datiOperazioneDto
	 * 
	 * @return boolean
	 * 
	 */
	public boolean annullaCartaContabile(Ente ente, Richiedente richiedente, Bilancio bilancio, CartaContabile carta, DatiOperazioneDto datiOperazione){
		boolean result = false;

		// Annulla Carta: L'operazione annulla in archivio la carta indicata nel parametro di input,
		// annulla le sue righe (PreDocumentoCarta) e scollega tutti i SubDocumentoSpesa collegati.

		Integer annoBilancioCarta = bilancio.getAnno();
		Integer numeroCarta = carta.getNumero();

		int idEnte = ente.getUid();

		// leggo da db i dati della carta contabile da annullare
		SiacTCartacontFin siacTCartacont = siacTCartaContRepository.findCartaContByAnnoCartaNumeroCarta(idEnte,
				numeroCarta,
				annoBilancioCarta.toString(),
				datiOperazione.getTs());

		if(siacTCartacont!=null){
			// estraggo le sue righe di dettaglio
			List<SiacTCartacontDetFin> elencoSiacTCartacontDet = siacTCartacont.getSiacTCartacontDets();
			if(elencoSiacTCartacontDet!=null && elencoSiacTCartacontDet.size() > 0){
				for(SiacTCartacontDetFin siacTCartacontDet : elencoSiacTCartacontDet){
					if(siacTCartacontDet!=null && siacTCartacontDet.getDataFineValidita()==null){
						// per ogni riga di dettaglio valida verifico se e' legata a dei sub-documenti di spesa
						List<SiacRCartacontDetSubdocFin> elencoSiacRCartacontDetSubdoc = siacTCartacontDet.getSiacRCartacontDetSubdocs();
						if(elencoSiacRCartacontDetSubdoc!=null && elencoSiacRCartacontDetSubdoc.size()>0){
							for(SiacRCartacontDetSubdocFin siacRCartacontDetSubdoc : elencoSiacRCartacontDetSubdoc){
								if(siacRCartacontDetSubdoc!=null && siacRCartacontDetSubdoc.getDataFineValidita()==null){
									// per ogni riga valida di legame tra il preDocumentoCarta e un subDocumento
									// annullo logicamente il legame
									siacRCartacontDetSubdoc = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRCartacontDetSubdoc, datiOperazione, siacTAccountRepository);
									siacRCartacontDetSubdocRepository.saveAndFlush(siacRCartacontDetSubdoc);
								}
							}
						}

						// annullo la riga di dettaglio della carta contabile
						siacTCartacontDet = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTCartacontDet, datiOperazione, siacTAccountRepository);
						siacTCartaContDetRepository.saveAndFlush(siacTCartacontDet);
					}
				}
			}

			// aggiorno lo stato operativo della carta contabile
			List<SiacRCartacontStatoFin> listaRCartacontStato =  siacTCartacont.getSiacRCartacontStatos();
			if(listaRCartacontStato!=null && listaRCartacontStato.size()>0){
				for(SiacRCartacontStatoFin rCartaContStato : listaRCartacontStato){
					if(rCartaContStato!=null && rCartaContStato.getDataFineValidita()==null){
						// annullo il vecchio stato operativo della carta contabile
						rCartaContStato = DatiOperazioneUtils.impostaDatiOperazioneLogin(rCartaContStato, datiOperazione, siacTAccountRepository);
						siacRCartacontStatoRepository.saveAndFlush(rCartaContStato);

						// creo il nuovo legame con lo stato operativo annullato
						SiacDCartacontStatoFin siacDCartacontStatoAnnullato = siacDCartacontStatoRepository.findDCartaContStatoValidoByEnteAndCode(idEnte, Constanti.D_CARTA_CONTABILE_STATO_ANNULLATO, datiOperazione.getTs());
						SiacRCartacontStatoFin siacRCartacontStatoAnnullato = new SiacRCartacontStatoFin();
						datiOperazione.setOperazione(Operazione.INSERIMENTO);

						siacRCartacontStatoAnnullato = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRCartacontStatoAnnullato, datiOperazione, siacTAccountRepository);

						siacRCartacontStatoAnnullato.setSiacDCartacontStato(siacDCartacontStatoAnnullato);
						siacRCartacontStatoAnnullato.setSiacTCartacont(siacTCartacont);

						siacRCartacontStatoRepository.saveAndFlush(siacRCartacontStatoAnnullato);

						result = true;
					}
				}
			}
		}


		//Termino restituendo l'oggetto di ritorno: 
        return result;
	}	

	/**
	 * 
	 * Operazione : verificaDatiTrasmessiAnnullamentoCartaContabile
	 * 
	 * Descrizione dell'operazione :
	 * 
	 * Verifico la correttezza dei dati ricevuti in input, prima di effettuare l'operazione di annullamento di una carta contabile
	 * 
	 * @param ente
	 * @param richiedente
	 * @param bilancio
	 * @param cartaContabile
	 * @param datiOperazioneDto
	 * 
	 * @return List<Errore>
	 * 
	 */
	public List<Errore> verificaDatiTrasmessiAnnullamentoCartaContabile(Ente ente, Richiedente richiedente, Bilancio bilancio, CartaContabile carta, DatiOperazioneDto datiOperazione){

		int idEnte = ente.getUid();

		List<Errore> listaErrori = new ArrayList<Errore>();

		Integer annoBilancioCarta = bilancio.getAnno();
		Integer numeroCarta = carta.getNumero();

		SiacTCartacontFin siacTCartacont = siacTCartaContRepository.findCartaContByAnnoCartaNumeroCarta(idEnte, numeroCarta, annoBilancioCarta.toString(), datiOperazione.getTs());

		if(siacTCartacont!=null){
			// Transizione Stati: il passaggio dal vecchio al nuovo stato deve rispettare le transizioni descritte a par. 2.5.3,
			// Possono essere annullate solo le carte contabili in stato PROVVISORIO o COMPLETATO
			// in caso contrario viene emesso l'errore:
			// <FIN_ERR_0055: Stato Movimento Impossibile (statoOld=statoOperativoCartaContabile,  statoNew= 'ANNULLATO', movimento=Carta Contabile')>
			List<SiacRCartacontStatoFin> listaRCartacontStato =  siacTCartacont.getSiacRCartacontStatos();
			if(listaRCartacontStato!=null && listaRCartacontStato.size()>0){
				for(SiacRCartacontStatoFin rCartaContStato : listaRCartacontStato){
					if(rCartaContStato!=null && rCartaContStato.getDataFineValidita()==null){
						String statoOperativoCartaCode = rCartaContStato.getSiacDCartacontStato().getCartacStatoCode();
						String statoOperativoCartaDesc = rCartaContStato.getSiacDCartacontStato().getCartacStatoDesc();
						if(!statoOperativoCartaCode.equalsIgnoreCase(Constanti.D_CARTA_CONTABILE_STATO_COMPLETATO) &&
								!statoOperativoCartaCode.equalsIgnoreCase(Constanti.D_CARTA_CONTABILE_STATO_PROVVISORIO)){
							listaErrori.add(ErroreFin.STATO_MOVIMENTO_IMPOSSIBILE.getErrore(statoOperativoCartaDesc, "ANNULLATO", "CARTA CONTABILE"));				
						}
					}
				}
			}

			// Documenti Collegati: 
			// Se la carta in annullamento e' collegata ad almeno un SubDocumentoSpesa relativo ad un Documento di tipo CNN (Carta Contabile)
			// verificare che lo StatoOperativoDocumento sia diverso da L-LIQUIDATO, PL-PARZIALMENTE LIQUIDATO, PE - PARZIALMENTE EMESSO o EM-EMESSO
			// e procedere all'annullamento del documento (vedi operazione 'Annulla Documento Spesa' del servizio 'SPES006 - Servizio Gestione Documento di Spesa')
			// e alla cancellazione della relazione con la carta, in caso invece il Documento abbia uno degli stati citati l'operazione viene bloccata con il
			// seguente messaggio < FIN_INF_0260 entita' collegate (<entita'> 'documenti d tipo CCN liquidati',  <operazione>: non e' possibile procedere con
			// l'annullamento della carta.' >.
			List<SiacTCartacontDetFin> elencoSiacTCartacontDet = siacTCartacont.getSiacTCartacontDets();
			if(elencoSiacTCartacontDet!=null && elencoSiacTCartacontDet.size() > 0){
				for(SiacTCartacontDetFin siacTCartacontDet : elencoSiacTCartacontDet){
					if(siacTCartacontDet!=null && siacTCartacontDet.getDataFineValidita()==null){
						List<SiacRCartacontDetSubdocFin> elencoSiacRCartacontDetSubdoc = siacTCartacontDet.getSiacRCartacontDetSubdocs();
						if(elencoSiacRCartacontDetSubdoc!=null && elencoSiacRCartacontDetSubdoc.size()>0){
							for(SiacRCartacontDetSubdocFin siacRCartacontDetSubdoc : elencoSiacRCartacontDetSubdoc){
								if(siacRCartacontDetSubdoc!=null && siacRCartacontDetSubdoc.getDataFineValidita()==null){
									SiacTDocFin siacTDoc = siacRCartacontDetSubdoc.getSiacTSubdoc().getSiacTDoc();
									if(siacTDoc!=null){
										SiacDDocTipoFin siacDDocTipo = siacTDoc.getSiacDDocTipo();
										if(siacDDocTipo!=null){
											if(siacDDocTipo.getDocTipoCode().equalsIgnoreCase(Constanti.D_DOC_TIPO_CARTA_CONTABILE_CODE)){
												List<SiacRDocStatoFin> elencoSiacRDocStato = siacTDoc.getSiacRDocStatos();
												if(elencoSiacRDocStato!=null && elencoSiacRDocStato.size()>0){
													for(SiacRDocStatoFin siacRDocStato : elencoSiacRDocStato){
														if(siacRDocStato!=null && siacRDocStato.getDataFineValidita()==null){
															SiacDDocStatoFin siacDDocStato = siacRDocStato.getSiacDDocStato();
															if(siacDDocStato!=null){
																String statoOperativoDocumentoCode = siacDDocStato.getDocStatoCode();

																if(statoOperativoDocumentoCode.equalsIgnoreCase(Constanti.D_DOC_STATO_LIQUIDATO) ||
																		statoOperativoDocumentoCode.equalsIgnoreCase(Constanti.D_DOC_STATO_PARZIALMENTE_LIQUIDATO) ||
																		statoOperativoDocumentoCode.equalsIgnoreCase(Constanti.D_DOC_STATO_PARZIALMENTE_EMESSO) ||
																		statoOperativoDocumentoCode.equalsIgnoreCase(Constanti.D_DOC_STATO_EMESSO)){

																	listaErrori.add(ErroreFin.ENTITA_COLLEGATE.getErrore("DOCUMENTI DI TIPO CCN LIQUIDATI", "NON E' POSSIBILE PROCEDERE CON L'ANNULLAMENTO DELLA CARTA"));
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
					}
				}
			}
		} else {
			listaErrori.add(ErroreFin.ERRORE_IN_CARTA_CONTABILE.getErrore("ANNULLAMENTO CARTA CONTABILE"));
		}


		//Termino restituendo l'oggetto di ritorno: 
        return listaErrori;
	}

	/**
	 * 
	 * Operazione : estraiElencoSiacTDocDaAnnullare
	 * 
	 * Descrizione dell'operazione :
	 * 
	 * Estrea l'elenco dei documenti collegati ad una carta contabile passata in inbput
	 * 
	 * @param ente
	 * @param richiedente
	 * @param bilancio
	 * @param cartaContabile
	 * @param datiOperazioneDto
	 * 
	 * @return List<SiacTDocFin>
	 * 
	 */
	public List<SiacTDocFin> estraiElencoSiacTDocDaAnnullare(Ente ente, Richiedente richiedente, Bilancio bilancio, CartaContabile carta, DatiOperazioneDto datiOperazione){
		

		int idEnte = ente.getUid();

		List<SiacTDocFin> elencoSiacTDocDaAnnullare = new ArrayList<SiacTDocFin>();

		Integer annoBilancioCarta = bilancio.getAnno();
		Integer numeroCarta = carta.getNumero();

		SiacTCartacontFin siacTCartacont = siacTCartaContRepository.findCartaContByAnnoCartaNumeroCarta(idEnte,
				numeroCarta,
				annoBilancioCarta.toString(),
				datiOperazione.getTs());

		if(siacTCartacont!=null){
			List<SiacTCartacontDetFin> elencoSiacTCartacontDet = siacTCartacont.getSiacTCartacontDets();
			if(elencoSiacTCartacontDet!=null && elencoSiacTCartacontDet.size() > 0){
				for(SiacTCartacontDetFin siacTCartacontDet : elencoSiacTCartacontDet){
					if(siacTCartacontDet!=null && siacTCartacontDet.getDataFineValidita()==null){
						List<SiacRCartacontDetSubdocFin> elencoSiacRCartacontDetSubdoc = siacTCartacontDet.getSiacRCartacontDetSubdocs();
						if(elencoSiacRCartacontDetSubdoc!=null && elencoSiacRCartacontDetSubdoc.size()>0){
							for(SiacRCartacontDetSubdocFin siacRCartacontDetSubdoc : elencoSiacRCartacontDetSubdoc){
								if(siacRCartacontDetSubdoc!=null && siacRCartacontDetSubdoc.getDataFineValidita()==null){
									SiacTDocFin siacTDoc = siacRCartacontDetSubdoc.getSiacTSubdoc().getSiacTDoc();
									if(siacTDoc!=null){
										SiacDDocTipoFin siacDDocTipo = siacTDoc.getSiacDDocTipo();
										if(siacDDocTipo!=null){
											if(siacDDocTipo.getDocTipoCode().equalsIgnoreCase(Constanti.D_DOC_TIPO_CARTA_CONTABILE_CODE)){
												// aggiungo il documento da annullare
												elencoSiacTDocDaAnnullare.add(siacTDoc);
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


		//Termino restituendo l'oggetto di ritorno: 
        return elencoSiacTDocDaAnnullare;
	}

	/**
	 * Metodo che controlla la carta contabile in inserimento.
	 * 
	 *  -	Atto Amministrativo: se presente verifica che lo stato del Provvedimento non sia annullato (vedi Ricerca Provvedimento)
		-	Soggetto: verifica che esista e sia in stato VALIDO o SOSPESO (l'obbligatorieta' del dato e' garantita dall'input)
		In caso il risultato sia falso il servizio restituisce l'errore:
		<FIN_ERR_0094 Soggetto Bloccato>
		-	Sede: se indicata verificare che lo stato sia VALIDO, in caso contrario emettere l'errore:
		<FIN_ERR_0249: Stato elemento soggetto non valido (parte = "Sede Secondaria")>
		-	Modalita' d Pagamento: verificare che lo stato sia VALIDO, in caso contrario emettere l'errore:
		<FIN_ERR_0249: Stato elemento soggetto non valido (parte = "Modalita' di Pagamento")>
		-	Impegno: se indicato verificare StatoOperativoMovimentoGestioneSpesa = DEFINITIVO o NON LIQUIDABILE.
		caso di anomalia viene emesso il seguente errore:
		<FIN_ERR_0261	: Errore in Carta Contabile (motivo= "errore nell'impegno collegato)">
		-	Subimpegno: 
				-se lo stato impegno e' NON LIQUIDABILE deve essere indicato il SUBIMPEGNO, in caso contrario il servizio restituisce il messaggio 
				- se e' presente, lo stato subimpegno deve essere DEFINITIVO
		caso di anomalia viene emesso il seguente errore:
		<FIN_ERR_0261	: Errore in Carta Contabile (motivo= "errore nel subimpegno collegato)">
		- SubDocumento: se indicato il subDocumentoSpesa deve :
			-	far riferimento a un DocumentoSpesa non ANNULLATO, 
			-  non essere ancora stato collegato a carte contabili (vedi: Operazione ricercaCartaContabile) 
		in caso di anomalia viene emesso il seguente errore:
		<FIN_ERR_0261	: Errore in Carta Contabile (motivo= "errore nella quota documento collegata)">
		Per effettuare i precedenti controlli  ricercare i documento come indicato in Ricerca documento di spesa,
		- Importo Riga: se non e' presente un subDocumento collegato ed e' presente impegno o subimpegno, l'importo riga deve sottostare alla disponibilita' secondo la regola che segue.
		(DISPONIBILE A LIQUIDARE IMPEGNO (o SUBIMPEGNO)  >=  IMPORTO RIGA
		In caso il risultato sia falso il servizio restituisce l'errore:
		<FIN_ERR_0001: Disponibilita' Insufficiente (operazione = Aggiornamento Carta Contabile)>
	 * 
	 * 
	 * @param richiedente
	 * @param ente
	 * @param bilancio
	 * @param cartaContabileInput
	 * @param datiOperazione
	 * @return
	 */
	public List<Errore> controlliInserisciCartaContabile(Richiedente richiedente, Ente ente, Bilancio bilancio, CartaContabile cartaContabileInput, DatiOperazioneDto datiOperazione){
		List<Errore> listaErrori=new ArrayList<Errore>();

		//Provvedimento carta
		if (cartaContabileInput.getAttoAmministrativo()!=null && cartaContabileInput.getAttoAmministrativo().getStatoOperativo()!=null) {
			if (cartaContabileInput.getAttoAmministrativo().getStatoOperativo().equalsIgnoreCase(Constanti.ATTO_AMM_STATO_ANNULLATO)) {
				listaErrori.add(ErroreAtt.PROVVEDIMENTO_ANNULLATO.getErrore("Provvedimento Annullato"));
			}
		}

		//Righe
		if (cartaContabileInput.getListaPreDocumentiCarta()!=null && cartaContabileInput.getListaPreDocumentiCarta().size()>0) {
			for (PreDocumentoCarta preDocumentoCarta : cartaContabileInput.getListaPreDocumentiCarta()) {
				// Soggetto: Verifica che il soggetto sia VALIDO o SOSPESO
				Soggetto soggettoCheck = soggettoDad.ricercaSoggetto(Constanti.AMBITO_FIN, ente.getUid(), preDocumentoCarta.getSoggetto().getCodiceSoggetto(), false, true);
				if(null==soggettoCheck){
					listaErrori.add(ErroreFin.SOGGETTO_BLOCCATO.getErrore(""));
				} else if (!soggettoCheck.getStatoOperativo().name().equalsIgnoreCase(StatoOperativoAnagrafica.VALIDO.name()) && 
						!soggettoCheck.getStatoOperativo().name().equalsIgnoreCase(StatoOperativoAnagrafica.SOSPESO.name())){
					listaErrori.add(ErroreFin.SOGGETTO_BLOCCATO.getErrore(""));
				} else {
					List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = preDocumentoCarta.getSoggetto().getElencoModalitaPagamento();
					if (listaModalitaPagamentoSoggetto!=null && listaModalitaPagamentoSoggetto.size()==1) {
						ModalitaPagamentoSoggetto modalitaPagamentoSoggetto=listaModalitaPagamentoSoggetto.get(0);
						if(!modalitaPagamentoSoggetto.getStato().toString().equalsIgnoreCase(Constanti.STATO_VALIDO)){
							listaErrori.add(ErroreFin.MOD_PAGAMENTO_STATO.getErrore(""));
						}
					}
				}

				//Controlli SubDocumento
				if (preDocumentoCarta.getListaSubDocumentiSpesaCollegati()!= null && preDocumentoCarta.getListaSubDocumentiSpesaCollegati().size()>0) {
					for (SubdocumentoSpesa subdocumentoSpesaCollegato : preDocumentoCarta.getListaSubDocumentiSpesaCollegati()) {
						if (subdocumentoSpesaCollegato.getDocumento()!=null) {
							//Controllo sullo stato del documento
							StatoOperativoDocumento stato=subdocumentoSpesaCollegato.getDocumento().getStatoOperativoDocumento();
							if (stato.equals(Constanti.D_DOC_STATO_ANNULLATO)) {
								listaErrori.add(ErroreFin.ERRORE_IN_CARTA_CONTABILE.getErrore("(errore nel documento collegato)"));
							} else {
								//controllare che non sia collegato ad altre carte contabili
								if (subdocumentoSpesaCollegato.getUid()!=0) {
									ParametroRicercaCartaContabile parametroRicercaCartaContabile = new ParametroRicercaCartaContabile();
									parametroRicercaCartaContabile.setSubDocumentoSpesaId(subdocumentoSpesaCollegato.getUid());
									List<CartaContabile> carteContabiliCollegate=ricercaCarteContabili(parametroRicercaCartaContabile, richiedente, ente, datiOperazione, 0, 0, null, null);
									if (carteContabiliCollegate!=null && carteContabiliCollegate.size()>0) {
										listaErrori.add(ErroreFin.ERRORE_IN_CARTA_CONTABILE.getErrore("(errore nel documento collegato)"));
									}
								}
							}
						}
					}
				}

				//Impegno - subimpegno
				if (preDocumentoCarta.getImpegno()!=null && preDocumentoCarta.getImpegno().getNumero()!=null && preDocumentoCarta.getImpegno().getAnnoMovimento()!=0) {

					String annoEsercizio=String.valueOf(bilancio.getAnno());
					
					//APRILE 2016: OTTIMIZZAZIONI CHIAMATA RICERCA IMPEGNO:
					PaginazioneSubMovimentiDto paginazioneSubMovimentiDto = new PaginazioneSubMovimentiDto();
					if(preDocumentoCarta.getImpegno().getElencoSubImpegni()!=null 
							&& preDocumentoCarta.getImpegno().getElencoSubImpegni().size()==1
							&& preDocumentoCarta.getImpegno().getElencoSubImpegni().get(0)!=null){
						//Selezionato un SUB 
						BigDecimal numeroSub = preDocumentoCarta.getImpegno().getElencoSubImpegni().get(0).getNumero();
						paginazioneSubMovimentiDto.setNoSub(false);
						paginazioneSubMovimentiDto.setNumeroSubMovimentoRichiesto(numeroSub);
					} else {
						//Non selezionato un SUB
						paginazioneSubMovimentiDto.setNumeroSubMovimentoRichiesto(null);
						paginazioneSubMovimentiDto.setNoSub(true);
					}
					//
					
					Impegno impegnoDaDB = null;
					
					EsitoRicercaMovimentoPkDto esitoRicercaMov = impegnoOttimizzatoDad.ricercaMovimentoPk(richiedente, ente, annoEsercizio, preDocumentoCarta.getImpegno().getAnnoMovimento(), preDocumentoCarta.getImpegno().getNumero(),paginazioneSubMovimentiDto,null, Constanti.MOVGEST_TIPO_IMPEGNO, false);
					
					if(esitoRicercaMov!=null){
						impegnoDaDB = (Impegno) esitoRicercaMov.getMovimentoGestione();
					}
					
					if (impegnoDaDB!=null) {
						preDocumentoCarta.getImpegno().setUid(impegnoDaDB.getUid());
						preDocumentoCarta.getImpegno().setStatoOperativoMovimentoGestioneSpesa(impegnoDaDB.getStatoOperativoMovimentoGestioneSpesa());
						preDocumentoCarta.getImpegno().setDisponibilitaLiquidare(impegnoDaDB.getDisponibilitaLiquidare());

						String statoImpegno=preDocumentoCarta.getImpegno().getStatoOperativoMovimentoGestioneSpesa();
						if (!statoImpegno.equalsIgnoreCase(Constanti.MOVGEST_STATO_DEFINITIVO)  && !statoImpegno.equalsIgnoreCase(Constanti.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE)) {
							listaErrori.add(ErroreFin.ERRORE_IN_CARTA_CONTABILE.getErrore("(errore nell'impegno collegato)"));
						} else if (statoImpegno.equalsIgnoreCase(Constanti.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE)) {
							List<SubImpegno> listaSubImpegni = preDocumentoCarta.getImpegno().getElencoSubImpegni();
							if (listaSubImpegni==null || listaSubImpegni.size()!=1) {
								listaErrori.add(ErroreFin.ERRORE_IN_CARTA_CONTABILE.getErrore("(errore nell'impegno collegato)"));
							} else {
								SubImpegno subImpegno=listaSubImpegni.get(0);
								boolean subTrovato = false;
								if (impegnoDaDB.getElencoSubImpegni()!=null && impegnoDaDB.getElencoSubImpegni().size()>0) {
									for (SubImpegno subImpegnoDaDB : impegnoDaDB.getElencoSubImpegni()) {
										if (subImpegnoDaDB.getNumero().compareTo(subImpegno.getNumero())==0) {
											subImpegno.setUid(subImpegnoDaDB.getUid());
											subImpegno.setAnnoMovimento(subImpegnoDaDB.getAnnoMovimento());
											subImpegno.setStatoOperativoMovimentoGestioneSpesa(subImpegnoDaDB.getStatoOperativoMovimentoGestioneSpesa());
											subImpegno.setDisponibilitaLiquidare(subImpegnoDaDB.getDisponibilitaLiquidare());
											subTrovato = true;
										}
									}
								}
								if(!subTrovato){
									listaErrori.add(ErroreFin.ERRORE_IN_CARTA_CONTABILE.getErrore("(errore nel subimpegno collegato)"));
								}
								if (!subImpegno.getStatoOperativoMovimentoGestioneSpesa().equalsIgnoreCase(Constanti.MOVGEST_STATO_DEFINITIVO)) {
									listaErrori.add(ErroreFin.ERRORE_IN_CARTA_CONTABILE.getErrore("(errore nel subimpegno collegato)"));
								} else {
									// DISPONIBILE A LIQUIDARE SUBIMPEGNO  >=  IMPORTO RIGA
									if (subImpegno.getDisponibilitaLiquidare().compareTo(preDocumentoCarta.getImporto())==-1) {
										listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE_ORIG.getErrore("Aggiornamento Carta Contabile"));
									}
								}
							}
						}else {
							// DISPONIBILE A LIQUIDARE IMPEGNO  >=  IMPORTO RIGA
							if (preDocumentoCarta.getImpegno().getDisponibilitaLiquidare().compareTo(preDocumentoCarta.getImporto())==-1) {
								listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE_ORIG.getErrore("Aggiornamento Carta Contabile"));
							}
						}
					} else {
						listaErrori.add(ErroreFin.ERRORE_IN_CARTA_CONTABILE.getErrore("(errore nell'impegno collegato)"));
					}
				}
			}
		}


		//Termino restituendo l'oggetto di ritorno: 
        return listaErrori;
	}

	/**
	 * Metodo che controlla la carta contabile in aggiornamento.
	 *          
	 *          		****	TESTATA CARTA ****
	 *  ** STATO operativo
	 *  
	 *  - Stato Operativo: 
		-l'operazione e' permessa solo se statoOperativoCartaContabile diverso da ANNULLATO, in caso contrario si emette l'errore 
		<FIN_ERR_0261: Errore in Carta Contabile (motivo= "Carta Annullata")">
		-	se lo statoOperativoCartaContabile di origine e' diverso da PROVVISORIO non e' ammessa nessuna operazione di aggiornamento ad eccezione di:
		-	Transazione di stato (vedi sotto)
		-	Collegamento a subDocumento
			in caso contrario si emette l'errore: 
			<FIN_ERR_0261: Errore in Carta Contabile (motivo= "Carta non Provvisoria")">
		-	se e' stato aggiornato lo stato operativo, deve essere rispettato quanto descritto a paragrafo Controlli Cambiamento di Stato in caso contrario si emette l'errore 
		<FIN_ERR_0261: Errore in Carta Contabile (motivo= "transizione di stato errata")">
		Atto Amministrativo: se presente verifica che lo stato del Provvedimento non sia annullato
	 * 
	 * 
	 *  **** Per ogni riga (predocumento carta) 
	 *  -	Soggetto: verifica che esista e sia in stato VALIDO o SOSPESO (l'obbligatorieta' del dato e' 		garantita dall'input)
		In caso il risultato sia falso il servizio restituisce l'errore: 
		<FIN_ERR_0094 Soggetto Bloccato>
		
		- Sede: se indicata verificare che lo stato sia VALIDO, in caso contrario emettere l'errore:
		<FIN_ERR_0249: Stato elemento soggetto non valido (parte = "Sede Secondaria")>
		
		-	Modalita' d Pagamento: verificare che lo stato sia VALIDO e con dataScadenza >= alla data corrente, in caso contrario emettere l'errore:
		<FIN_ERR_0249: Stato elemento soggetto non valido (parte = "Modalita' di Pagamento")>
		
		-	Impegno: se aggiornata la relazione con l'impegno,   verificare   
		-	StatoOperativoMovimentoGestioneSpesa = DEFINITIVO o NON LIQUIDABILE. caso di anomalia viene emesso il seguente errore: <FIN_ERR_0261	: Errore in Carta Contabile (motivo= "errore nell'impegno collegato)">
		
		-	Subimpegno: 
			-	se lo stato impegno e' NON LIQUIDABILE deve essere indicato il SUBIMPEGNO, in caso contrario il servizio restituisce il messaggio 
			- se e' presente, lo stato subimpegno deve essere DEFINITIVO
			caso di anomalia viene emesso il seguente errore:
			<FIN_ERR_0261	: Errore in Carta Contabile (motivo= "errore nel subimpegno collegato)">
		
		-SubDocumento: se indicato il subDocumentoSpesa deve :
			-	far riferimento a un DocumentoSpesa non ANNULLATO, , STORNATO, EMESSO
			-	non essere ancora stato collegato a carte contabili 
			in caso di anomalia viene emesso il seguente errore:
		<FIN_ERR_0261	: Errore in Carta Contabile (motivo= "errore nella quota documento collegata)">
		Per effettuare i precedenti controlli  ricercare i documento come indicato in Ricerca documento di spesa,
		
		-Importo Riga: 
			-	Se modificato e collegato a subDocumentoSpesa errore: 
		<FIN_ERR_0261: Errore in Carta Contabile (motivo= "importo non modificabile: quota documento collegata)">
			-	Controllare che venga rispettata la disponibilita' a liquidare dell'impegno (o subimpegno) solo se la riga e' collegata a un impegno o a un subimpegno. Questo controllo deve avvenire valutando il delta del importoDaRegolarizzare che e' l'importo della riga che effettivamente  influenza il disponibile dell'impegno.
		La disponibilita' deve quindi essere verificata secondo la regola che segue.
		(DISPONIBILE A LIQUIDARE IMPEGNO (o SUBIMPEGNO) + VECCHIO IMPORTO DA REGOLARIZZARE)  >=  IMPORTO DA REGOLARIZZARE
		In caso il risultato sia falso il servizio restituisce l'errore:
		<FIN_ERR_0001: Disponibilita' Insufficiente (operazione = Aggiornamento Carta Contabile)>

	 * 
	 * @param richiedente
	 * @param ente
	 * @param bilancio
	 * @param cartaContabileInput
	 * @param cartaContabileOld
	 * @param datiOperazione
	 * @return
	 */
	public List<Errore> controlliAggiornaCartaContabile(Richiedente richiedente, Ente ente, Bilancio bilancio, CartaContabile cartaContabileInput, CartaContabile cartaContabileOld, DatiOperazioneDto datiOperazione){
		List<Errore> listaErrori=new ArrayList<Errore>();

		//Stato operativo
		if (cartaContabileOld.getStatoOperativoCartaContabile()!=null && 
				cartaContabileOld.getStatoOperativoCartaContabile().equals(CartaContabile.StatoOperativoCartaContabile.ANNULLATO)) {
			listaErrori.add(ErroreFin.ERRORE_IN_CARTA_CONTABILE.getErrore("(Carta annullata)"));
		} else if (cartaContabileOld.getStatoOperativoCartaContabile()!=null && 
				!cartaContabileOld.getStatoOperativoCartaContabile().equals(CartaContabile.StatoOperativoCartaContabile.PROVVISORIO)) {
			//si puo' variare solo stato e subdocumento
			//listaErrori.add(ErroreFin.ERRORE_IN_CARTA_CONTABILE.getErrore("(Carta non provvisoria)"));
		}

		//Cambiamento stato
		if (cartaContabileOld.getStatoOperativoCartaContabile()!=null && cartaContabileInput.getStatoOperativoCartaContabile()!=null &&
				cartaContabileOld.getStatoOperativoCartaContabile().equals(CartaContabile.StatoOperativoCartaContabile.PROVVISORIO) &&
				cartaContabileInput.getStatoOperativoCartaContabile().equals(CartaContabile.StatoOperativoCartaContabile.TRASMESSO)) {
			listaErrori.add(ErroreFin.ERRORE_IN_CARTA_CONTABILE.getErrore("(Transazione di stato errata)"));
		}

		if (cartaContabileOld.getStatoOperativoCartaContabile()!=null && cartaContabileInput.getStatoOperativoCartaContabile()!=null &&
				cartaContabileOld.getStatoOperativoCartaContabile().equals(CartaContabile.StatoOperativoCartaContabile.TRASMESSO) &&
				(cartaContabileInput.getStatoOperativoCartaContabile().equals(CartaContabile.StatoOperativoCartaContabile.ANNULLATO) ||
						cartaContabileInput.getStatoOperativoCartaContabile().equals(CartaContabile.StatoOperativoCartaContabile.PROVVISORIO))) {
			listaErrori.add(ErroreFin.ERRORE_IN_CARTA_CONTABILE.getErrore("(Transazione di stato errata)"));
		}

		//Provvedimento carta
		if (cartaContabileInput.getAttoAmministrativo()!=null && cartaContabileInput.getAttoAmministrativo().getStatoOperativo()!=null) {
			if (cartaContabileInput.getAttoAmministrativo().getStatoOperativo().equalsIgnoreCase(Constanti.ATTO_AMM_STATO_ANNULLATO)) {
				listaErrori.add(ErroreAtt.PROVVEDIMENTO_ANNULLATO.getErrore("Provvedimento Annullato"));
			}
		}

		//Righe
		if (cartaContabileInput.getListaPreDocumentiCarta()!=null && cartaContabileInput.getListaPreDocumentiCarta().size()>0 &&
				cartaContabileOld.getListaPreDocumentiCarta()!=null && cartaContabileOld.getListaPreDocumentiCarta().size()>0) {
			for (PreDocumentoCarta preDocumentoCarta : cartaContabileInput.getListaPreDocumentiCarta()) {
				for (PreDocumentoCarta preDocumentoCartaOld : cartaContabileOld.getListaPreDocumentiCarta()) {
					if (preDocumentoCartaOld.getNumero().compareTo(preDocumentoCarta.getNumero())==0) {
						// Soggetto: Verifica che il soggetto sia VALIDO o SOSPESO
						Soggetto soggettoCheck = soggettoDad.ricercaSoggetto(Constanti.AMBITO_FIN, ente.getUid(), preDocumentoCarta.getSoggetto().getCodiceSoggetto(), false, true);
						if(null==soggettoCheck){
							listaErrori.add(ErroreFin.SOGGETTO_BLOCCATO.getErrore(""));
						} else if (!soggettoCheck.getStatoOperativo().name().equalsIgnoreCase(StatoOperativoAnagrafica.VALIDO.name()) && 
								!soggettoCheck.getStatoOperativo().name().equalsIgnoreCase(StatoOperativoAnagrafica.SOSPESO.name())){
							listaErrori.add(ErroreFin.SOGGETTO_BLOCCATO.getErrore(""));
						} else {
							// ModalitaPagamentoSoggetto
							List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = preDocumentoCarta.getSoggetto().getElencoModalitaPagamento();
							if (listaModalitaPagamentoSoggetto!=null && listaModalitaPagamentoSoggetto.size()==1) {
								//se la modalita di pagamento cessione e' presente, prendo quella. altrimenti quella non cessione 
								ModalitaPagamentoSoggetto modalitaPagamentoSoggetto= EntitaUtils.entitaConUid(listaModalitaPagamentoSoggetto.get(0)) && EntitaUtils.entitaConUid(listaModalitaPagamentoSoggetto.get(0).getModalitaPagamentoSoggettoCessione2())? 
										listaModalitaPagamentoSoggetto.get(0).getModalitaPagamentoSoggettoCessione2() 
										: listaModalitaPagamentoSoggetto.get(0);
								if(modalitaPagamentoSoggetto == null || !Constanti.STATO_VALIDO.equalsIgnoreCase(modalitaPagamentoSoggetto.getCodiceStatoModalitaPagamento())){
									listaErrori.add(ErroreFin.MOD_PAGAMENTO_STATO_ORIG.getErrore("Modalita' di Pagamento"));
								}
							}

							// SedeSecondariaSoggetto
							List<SedeSecondariaSoggetto> listaSedi=preDocumentoCarta.getSoggetto().getSediSecondarie();
							if (listaSedi!=null && listaSedi.size()==1) {
								SedeSecondariaSoggetto sede=listaSedi.get(0);
								if (!sede.getStatoOperativoSedeSecondaria().equals(StatoOperativoSedeSecondaria.VALIDO)) {
									listaErrori.add(ErroreFin.MOD_PAGAMENTO_STATO_ORIG.getErrore("Sede Secondaria"));
								}
							}
						}

						//Controlli SubDocumento
						if (preDocumentoCarta.getListaSubDocumentiSpesaCollegati()!= null && preDocumentoCarta.getListaSubDocumentiSpesaCollegati().size()>0) {
							for (SubdocumentoSpesa subdocumentoSpesaCollegato : preDocumentoCarta.getListaSubDocumentiSpesaCollegati()) {
								if (subdocumentoSpesaCollegato.getDocumento()!=null) {
									//Controllo sullo stato del documento
									StatoOperativoDocumento stato=subdocumentoSpesaCollegato.getDocumento().getStatoOperativoDocumento();
									if (stato.equals(Constanti.D_DOC_STATO_ANNULLATO) ||
											stato.equals(Constanti.D_DOC_STATO_STORNATO) ||
											stato.equals(Constanti.D_DOC_STATO_EMESSO)) {
										listaErrori.add(ErroreFin.ERRORE_IN_CARTA_CONTABILE.getErrore("(errore nel documento collegato)"));
									} else {
										//controllo che non sia collegato ad altre carte contabili
										if (subdocumentoSpesaCollegato.getUid()!=0) {
											ParametroRicercaCartaContabile parametroRicercaCartaContabile = new ParametroRicercaCartaContabile();
											parametroRicercaCartaContabile.setSubDocumentoSpesaId(subdocumentoSpesaCollegato.getUid());
											List<CartaContabile> carteContabiliCollegate=ricercaCarteContabili(parametroRicercaCartaContabile, richiedente, ente, datiOperazione, 0, 0, null, null);
											if (carteContabiliCollegate!=null && carteContabiliCollegate.size()>0) {
												//TODO - controllare che la carta non sia quella in aggiornamento
//													listaErrori.add(ErroreFin.ERRORE_IN_CARTA_CONTABILE.getErrore("(errore nel documento collegato)"));
											}
										}
									}
								}
							}
						}
						
						//Impegno - subimpegno
						boolean isImpegnoSubChanged=checkImpegnoSubChanged(preDocumentoCarta.getImpegno(), preDocumentoCartaOld.getImpegno());
						if (isImpegnoSubChanged) {
							if (preDocumentoCarta.getImpegno()!=null && preDocumentoCarta.getImpegno().getNumero()!=null && preDocumentoCarta.getImpegno().getAnnoMovimento()!=0) {

								String annoEsercizio=String.valueOf(bilancio.getAnno());
								
								
								//APRILE 2016: OTTIMIZZAZIONI CHIAMATA RICERCA IMPEGNO:
								PaginazioneSubMovimentiDto paginazioneSubMovimentiDto = new PaginazioneSubMovimentiDto();
								if(preDocumentoCarta.getImpegno().getElencoSubImpegni()!=null 
										&& preDocumentoCarta.getImpegno().getElencoSubImpegni().size()==1
										&& preDocumentoCarta.getImpegno().getElencoSubImpegni().get(0)!=null){
									//Selezionato un SUB 
									BigDecimal numeroSub = preDocumentoCarta.getImpegno().getElencoSubImpegni().get(0).getNumero();
									paginazioneSubMovimentiDto.setNoSub(false);
									paginazioneSubMovimentiDto.setNumeroSubMovimentoRichiesto(numeroSub);
								} else {
									//Non selezionato un SUB
									paginazioneSubMovimentiDto.setNumeroSubMovimentoRichiesto(null);
									paginazioneSubMovimentiDto.setNoSub(true);
								}
								//
								
								EsitoRicercaMovimentoPkDto esitoRicercaMov = impegnoOttimizzatoDad.ricercaMovimentoPk(richiedente, ente, annoEsercizio, preDocumentoCarta.getImpegno().getAnnoMovimento(), preDocumentoCarta.getImpegno().getNumero(),paginazioneSubMovimentiDto,null, Constanti.MOVGEST_TIPO_IMPEGNO, false);
								
								Impegno impegnoDaDB = null;
								if(esitoRicercaMov!=null){
									 impegnoDaDB = (Impegno) esitoRicercaMov.getMovimentoGestione();
								}
								
								if (impegnoDaDB!=null) {
									preDocumentoCarta.getImpegno().setUid(impegnoDaDB.getUid());
									preDocumentoCarta.getImpegno().setStatoOperativoMovimentoGestioneSpesa(impegnoDaDB.getStatoOperativoMovimentoGestioneSpesa());
									preDocumentoCarta.getImpegno().setDisponibilitaLiquidare(impegnoDaDB.getDisponibilitaLiquidare());

									String statoImpegno=preDocumentoCarta.getImpegno().getStatoOperativoMovimentoGestioneSpesa();
									if (!statoImpegno.equalsIgnoreCase(Constanti.MOVGEST_STATO_DEFINITIVO)  && !statoImpegno.equalsIgnoreCase(Constanti.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE)) {
										listaErrori.add(ErroreFin.ERRORE_IN_CARTA_CONTABILE.getErrore("(errore nell'impegno collegato)"));
									} else if (statoImpegno.equalsIgnoreCase(Constanti.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE)) {
										List<SubImpegno> listaSubImpegni = preDocumentoCarta.getImpegno().getElencoSubImpegni();
										if (listaSubImpegni==null || listaSubImpegni.size()!=1) {
											listaErrori.add(ErroreFin.ERRORE_IN_CARTA_CONTABILE.getErrore("(errore nell'impegno collegato)"));
										} else {
											SubImpegno subImpegno=listaSubImpegni.get(0);
											boolean subTrovato = false;
											if (impegnoDaDB.getElencoSubImpegni()!=null && impegnoDaDB.getElencoSubImpegni().size()>0) {
												for (SubImpegno subImpegnoDaDB : impegnoDaDB.getElencoSubImpegni()) {
													if (subImpegnoDaDB.getNumero().compareTo(subImpegno.getNumero())==0) {
														subImpegno.setUid(subImpegnoDaDB.getUid());
														subImpegno.setAnnoMovimento(subImpegnoDaDB.getAnnoMovimento());
														subImpegno.setStatoOperativoMovimentoGestioneSpesa(subImpegnoDaDB.getStatoOperativoMovimentoGestioneSpesa());
														subImpegno.setDisponibilitaLiquidare(subImpegnoDaDB.getDisponibilitaLiquidare());
														subTrovato = true;
													}
												}
											}
											if(!subTrovato){
												listaErrori.add(ErroreFin.ERRORE_IN_CARTA_CONTABILE.getErrore("(errore nel subimpegno collegato)"));
											}
											if (!subImpegno.getStatoOperativoMovimentoGestioneSpesa().equalsIgnoreCase(Constanti.MOVGEST_STATO_DEFINITIVO)) {
												listaErrori.add(ErroreFin.ERRORE_IN_CARTA_CONTABILE.getErrore("(errore nel subimpegno collegato)"));
											} else {
												// DISPONIBILE A LIQUIDARE SUBIMPEGNO  >=  IMPORTO RIGA
												if (subImpegno.getDisponibilitaLiquidare().compareTo(preDocumentoCarta.getImporto())==-1) {
													listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE_ORIG.getErrore("Aggiornamento Carta Contabile"));
												}
											}
										}
									}else {
										// DISPONIBILE A LIQUIDARE IMPEGNO  >=  IMPORTO RIGA
										if (preDocumentoCarta.getImpegno().getDisponibilitaLiquidare().compareTo(preDocumentoCarta.getImporto())==-1) {
											listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE_ORIG.getErrore("Aggiornamento Carta Contabile"));
										}
									}
								} else {
									listaErrori.add(ErroreFin.ERRORE_IN_CARTA_CONTABILE.getErrore("(errore nell'impegno collegato)"));
								}
							}
						}

						//Importo riga
						if (preDocumentoCarta.getImporto()!=null && preDocumentoCartaOld.getImporto()!=null && 
								preDocumentoCarta.getImporto().compareTo(preDocumentoCartaOld.getImporto())!=0) {
							//preDocumentiSpesa collegati
							if (preDocumentoCartaOld.getListaSubDocumentiSpesaCollegati()!=null &&
									preDocumentoCartaOld.getListaSubDocumentiSpesaCollegati().size()>0) {
								listaErrori.add(ErroreFin.ERRORE_IN_CARTA_CONTABILE.getErrore("(importo non modificabile: quota documento collegata)"));
							}
							//Controllo disponibilita' a liquidare
							if (!isImpegnoSubChanged) {
								if (preDocumentoCarta.getImpegno()!=null) {
									String annoEsercizio=String.valueOf(bilancio.getAnno());
									
									//APRILE 2016: OTTIMIZZAZIONI CHIAMATA RICERCA IMPEGNO:
									PaginazioneSubMovimentiDto paginazioneSubMovimentiDto = new PaginazioneSubMovimentiDto();
									if(preDocumentoCarta.getImpegno().getElencoSubImpegni()!=null 
											&& preDocumentoCarta.getImpegno().getElencoSubImpegni().size()==1
											&& preDocumentoCarta.getImpegno().getElencoSubImpegni().get(0)!=null){
										//Selezionato un SUB 
										BigDecimal numeroSub = preDocumentoCarta.getImpegno().getElencoSubImpegni().get(0).getNumero();
										paginazioneSubMovimentiDto.setNoSub(false);
										paginazioneSubMovimentiDto.setNumeroSubMovimentoRichiesto(numeroSub);
									} else {
										//Non selezionato un SUB
										paginazioneSubMovimentiDto.setNumeroSubMovimentoRichiesto(null);
										paginazioneSubMovimentiDto.setNoSub(true);
									}
									//
									
									Impegno impegnoDaDB = null;
									
									EsitoRicercaMovimentoPkDto esitoRicercaMov = impegnoOttimizzatoDad.ricercaMovimentoPk(richiedente, ente, annoEsercizio, preDocumentoCarta.getImpegno().getAnnoMovimento(), preDocumentoCarta.getImpegno().getNumero(),paginazioneSubMovimentiDto,null, Constanti.MOVGEST_TIPO_IMPEGNO, false);

									if(esitoRicercaMov!=null){
										impegnoDaDB = (Impegno) esitoRicercaMov.getMovimentoGestione();
									}
									
									if (impegnoDaDB!=null) {
										preDocumentoCarta.getImpegno().setUid(impegnoDaDB.getUid());
										preDocumentoCarta.getImpegno().setStatoOperativoMovimentoGestioneSpesa(impegnoDaDB.getStatoOperativoMovimentoGestioneSpesa());
										preDocumentoCarta.getImpegno().setDisponibilitaLiquidare(impegnoDaDB.getDisponibilitaLiquidare());

										if (preDocumentoCarta.getImpegno().getElencoSubImpegni()!=null &&
												preDocumentoCarta.getImpegno().getElencoSubImpegni().size()==1) {
											SubImpegno subImpegno=preDocumentoCarta.getImpegno().getElencoSubImpegni().get(0);
											if (impegnoDaDB.getElencoSubImpegni()!=null && impegnoDaDB.getElencoSubImpegni().size()>0) {
												for (SubImpegno subImpegnoDaDB : impegnoDaDB.getElencoSubImpegni()) {
													if (subImpegnoDaDB.getNumero().compareTo(subImpegno.getNumero())==0) {
														subImpegno.setUid(subImpegnoDaDB.getUid());
														subImpegno.setAnnoMovimento(subImpegnoDaDB.getAnnoMovimento());
														subImpegno.setStatoOperativoMovimentoGestioneSpesa(subImpegnoDaDB.getStatoOperativoMovimentoGestioneSpesa());
														subImpegno.setDisponibilitaLiquidare(subImpegnoDaDB.getDisponibilitaLiquidare());
													}
												}
											}

											BigDecimal dispLiqSub=subImpegno.getDisponibilitaLiquidare();
											if (dispLiqSub.add(preDocumentoCartaOld.getImporto()).compareTo(preDocumentoCarta.getImporto())==-1) {
												listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE_ORIG.getErrore("Aggiornamento Carta Contabile"));
											}
										} else {
											BigDecimal dispLiq=preDocumentoCarta.getImpegno().getDisponibilitaLiquidare();
											if (dispLiq.add(preDocumentoCartaOld.getImporto()).compareTo(preDocumentoCarta.getImporto())==-1) {
												listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE_ORIG.getErrore("Aggiornamento Carta Contabile"));
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


		//Termino restituendo l'oggetto di ritorno: 
        return listaErrori;
	}

	/**
	 * Metodo che controlla se l'impegno di un predocumento in aggiornamento e' cambiato.
	 * 
	 * @param impegnoNew
	 * @param impegnoOld
	 * @return
	 */
	private boolean checkImpegnoSubChanged(Impegno impegnoNew, Impegno impegnoOld) {
		boolean esito=false;

		if ((impegnoNew==null && impegnoOld!=null) ||
				(impegnoNew!=null && impegnoOld==null)) {
			return true;
		}

		if (impegnoNew!=null && impegnoOld!=null) {
			if ((impegnoNew.getElencoSubImpegni()!=null && impegnoNew.getElencoSubImpegni().size()==1) && 
					(impegnoOld.getElencoSubImpegni()==null || impegnoOld.getElencoSubImpegni().size()==0)) {
				return true;
			} else if ((impegnoOld.getElencoSubImpegni()!=null && impegnoOld.getElencoSubImpegni().size()==1) && 
					(impegnoNew.getElencoSubImpegni()==null || impegnoNew.getElencoSubImpegni().size()==0)) {
				return true;
			}

			if (impegnoNew.getNumero()!=null && 
					impegnoNew.getAnnoMovimento()!=0 && 
					impegnoOld.getNumero()!=null && 
					impegnoOld.getAnnoMovimento()!=0) {
				if (impegnoNew.getNumero().compareTo(impegnoOld.getNumero())!=0) {
					return true;
				} else if (impegnoNew.getAnnoMovimento()!=impegnoOld.getAnnoMovimento()) {
					return true;
				}

				if (impegnoOld.getElencoSubImpegni()!=null && impegnoOld.getElencoSubImpegni().size()==1 && 
						impegnoNew.getElencoSubImpegni()!=null && impegnoNew.getElencoSubImpegni().size()==1) {
					if (impegnoNew.getElencoSubImpegni().get(0).getNumero().compareTo(impegnoOld.getElencoSubImpegni().get(0).getNumero())!=0) {
						return true;
					}
				}
			}
		}

		//Termino restituendo l'oggetto di ritorno: 
        return esito;
	}

	/**
	 * Operazione di inserimento carta contabile.
	 * Richiamato dal servizio InserisceCartaContabileService.
	 * 
	 * @param richiedente
	 * @param ente
	 * @param bilancio
	 * @param cartaContabileInput
	 * @param datiOperazione
	 * @return
	 */
	public EsitoGestioneCartaContabileDto inserisciCartaContabile(Richiedente richiedente, String codiceAmbito, Ente ente, Bilancio bilancio, CartaContabile cartaContabileInput, DatiOperazioneDto datiOperazione){
		EsitoGestioneCartaContabileDto esito=new EsitoGestioneCartaContabileDto();

		List<Errore> esitoControlli=controlliInserisciCartaContabile(richiedente, ente, bilancio, cartaContabileInput, datiOperazione);

		if (esitoControlli!=null && esitoControlli.size()>0) {
			esito.setListaErrori(esitoControlli);
			esito.setCartaContabile(null);
			return esito;
		}

		int idEnte=ente.getUid();

		String loginOperazione = richiedente.getAccount().getNome();

		SiacDAmbitoFin  siacDAmbitoPerCode = siacDAmbitoRepository.findAmbitoByCode(Constanti.AMBITO_FIN, idEnte);
		Integer idAmbito = siacDAmbitoPerCode.getAmbitoId();

		long nuovoCode = getMaxCode(ProgressivoType.CARTA, idEnte, idAmbito, loginOperazione, bilancio.getAnno());

		//1. Carta contabile
		SiacTCartacontFin siacTCartacontIns=new SiacTCartacontFin();

		siacTCartacontIns.setCartacNumero(new Integer((int)nuovoCode));

		//Inserire dati cartaContabileInput nella siacTCartacontIns
		if (cartaContabileInput.getOggetto()!=null) {
			cartaContabileInput.setOggetto(cartaContabileInput.getOggetto().toUpperCase());
		}
		siacTCartacontIns.setCartacOggetto(cartaContabileInput.getOggetto());
		siacTCartacontIns.setCartacImporto(cartaContabileInput.getImporto());
		if (cartaContabileInput.getCausale()!=null) {
			cartaContabileInput.setCausale(cartaContabileInput.getCausale().toUpperCase());
		}
		siacTCartacontIns.setCartacCausale(cartaContabileInput.getCausale());
		siacTCartacontIns.setCartacImportoValuta(cartaContabileInput.getImportoValuta());
		siacTCartacontIns.setCartacNumeroReg(cartaContabileInput.getNumRegistrazione());

		if (cartaContabileInput.getDataScadenza() != null) {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(cartaContabileInput.getDataScadenza());
			Timestamp dataScadenzaTs = new Timestamp(calendar.getTimeInMillis());
			siacTCartacontIns.setCartacDataScadenza(dataScadenzaTs);
		}

		if (cartaContabileInput.getDataEsecuzionePagamento() != null) {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(cartaContabileInput.getDataEsecuzionePagamento());
			Timestamp dataPagamentoTs = new Timestamp(calendar.getTimeInMillis());
			siacTCartacontIns.setCartacDataPagamento(dataPagamentoTs);
		}

		//Bilancio
		SiacTBilFin siacTBilCarta = new SiacTBilFin();
		List<SiacTBilFin> siacTBilList = siacTBilRepository.getValidoByAnno(idEnte, Integer.toString(bilancio.getAnno()), datiOperazione.getTs());
		if(siacTBilList!=null && siacTBilList.size()>0 && siacTBilList.get(0)!=null){
			siacTBilCarta = siacTBilList.get(0);
		}

		siacTCartacontIns.setSiacTBil(siacTBilCarta);

		siacTCartacontIns = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTCartacontIns, datiOperazione, siacTAccountRepository);
		siacTCartacontIns = siacTCartaContRepository.saveAndFlush(siacTCartacontIns);

		//Atto Amm
		if (cartaContabileInput.getAttoAmministrativo()!=null && cartaContabileInput.getAttoAmministrativo().getAnno()!=0 &&
				cartaContabileInput.getAttoAmministrativo().getTipoAtto()!=null && cartaContabileInput.getAttoAmministrativo().getNumero()!=0) {
			//invochiamo la routine centralizzata per l'estrazione dell'atto:
			SiacTAttoAmmFin siacTAttoAmm = getSiacTAttoAmmFromAttoAmministrativo(cartaContabileInput.getAttoAmministrativo(), idEnte);
			//procediamo al salvataggio del legame con l'atto trovato:
			if (siacTAttoAmm!=null){
				siacTCartacontIns.setSiacTAttoAmm(siacTAttoAmm);
				siacTCartacontIns = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTCartacontIns, datiOperazione, siacTAccountRepository);
				siacTCartacontIns = siacTCartaContRepository.saveAndFlush(siacTCartacontIns);
			}
		}

		//stato carta
		SiacDCartacontStatoFin siacDCartacontStato=new SiacDCartacontStatoFin();

		String statoOP = Constanti.statoOperativoCartaContabileEnumToString(StatoOperativoCartaContabile.PROVVISORIO);
		siacDCartacontStato=siacDCartacontStatoRepository.findDCartaContStatoValidoByEnteAndCode(idEnte, statoOP, datiOperazione.getTs());

		SiacRCartacontStatoFin siacRCartacontStato=new SiacRCartacontStatoFin();
		siacRCartacontStato = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRCartacontStato, datiOperazione, siacTAccountRepository);
		siacRCartacontStato.setSiacTCartacont(siacTCartacontIns);
		siacRCartacontStato.setSiacDCartacontStato(siacDCartacontStato);
		siacRCartacontStatoRepository.saveAndFlush(siacRCartacontStato);

		//Attributi carta contabile (note, motivo urgenza)
		AttributoTClassInfoDto attributoInfo = new AttributoTClassInfoDto();
		attributoInfo.setTipoOggetto(OggettoDellAttributoTClass.T_CARTACONT);
		attributoInfo.setSiacTCartacont(siacTCartacontIns);

		if (cartaContabileInput.getNote()!=null) {
			cartaContabileInput.setNote(cartaContabileInput.getNote().toUpperCase());
		}
		salvaAttributoTAttr(attributoInfo, datiOperazione, cartaContabileInput.getNote(), Constanti.T_ATTR_CODE_NOTE_CC);
		if (cartaContabileInput.getMotivoUrgenza()!=null) {
			cartaContabileInput.setMotivoUrgenza(cartaContabileInput.getMotivoUrgenza().toUpperCase());
		}
		salvaAttributoTAttr(attributoInfo, datiOperazione, cartaContabileInput.getMotivoUrgenza(), Constanti.T_ATTR_CODE_MOTIVO_URGENZA);

		//Firma 1:
		if(!isEmpty(cartaContabileInput.getFirma1())){
			salvaAttributoTAttr(attributoInfo, datiOperazione, cartaContabileInput.getFirma1(), Constanti.T_ATTR_FIRMA_1_CARTA_CONT);
		}
		
		//Firma 2:
		if(!isEmpty(cartaContabileInput.getFirma2())){
			salvaAttributoTAttr(attributoInfo, datiOperazione, cartaContabileInput.getFirma2(), Constanti.T_ATTR_FIRMA_2_CARTA_CONT);
		}
		

		//2. Carta Estera
		if (cartaContabileInput.getCartaEstera()!=null) {
			CartaEstera cartaEstera=cartaContabileInput.getCartaEstera();
			SiacTCartacontEsteraFin siacTCartacontEstera = new SiacTCartacontEsteraFin();

			siacTCartacontEstera.setSiacTCartacont(siacTCartacontIns);
			if (cartaEstera.getCausalePagamento()!=null) {
				cartaEstera.setCausalePagamento(cartaEstera.getCausalePagamento().toUpperCase());
			}
			siacTCartacontEstera.setCartacestCausalepagamento(cartaEstera.getCausalePagamento());
			if (cartaEstera.getIstruzioni()!=null) {
				cartaEstera.setIstruzioni(cartaEstera.getIstruzioni().toUpperCase());
			}
			siacTCartacontEstera.setCartacestIstruzioni(cartaEstera.getIstruzioni());
			siacTCartacontEstera.setCartacestDiversotitolare(cartaEstera.getDiversoTitolare());

			//Commissioni Estero
			SiacDCommissioniesteroFin siacDCommissioniestero=new SiacDCommissioniesteroFin();

			String codCommissioni = cartaEstera.getCommissioniEstero().getCodice();
			siacDCommissioniestero=siacDCommissioniEsteroRepository.findDCommissioneEsteroValidoByEnteAndCode(idEnte, codCommissioni, datiOperazione.getTs());

			siacTCartacontEstera.setSiacDCommissioniestero(siacDCommissioniestero);

			//Valuta
			SiacDValutaFin siacDValuta=new SiacDValutaFin();

			String codvaluta = cartaEstera.getValuta().getCodice();
			siacDValuta=siacDValutaRepository.findDValutaValidaByEnteAndCode(idEnte, codvaluta, datiOperazione.getTs());

			siacTCartacontEstera.setSiacDValuta(siacDValuta);

			siacTCartacontEstera = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTCartacontEstera, datiOperazione, siacTAccountRepository);
			siacTCartacontEstera = siacTCartaContEsteraRepository.saveAndFlush(siacTCartacontEstera);

			//Attributi carta estera (bonifico assegno)
			AttributoTClassInfoDto attributoInfoEstera = new AttributoTClassInfoDto();
			attributoInfoEstera.setTipoOggetto(OggettoDellAttributoTClass.T_CARTACONT_ESTERA);
			attributoInfoEstera.setSiacTCartacontEstera(siacTCartacontEstera);


			if (cartaEstera.getTipoPagamento().equals(CartaEstera.TipoPagamento.Bonifico)) {
				salvaAttributoTAttr(attributoInfoEstera, datiOperazione, true, Constanti.T_ATTR_CODE_MODALITA_ACCREDITO_BONIFICO);
				salvaAttributoTAttr(attributoInfoEstera, datiOperazione, false, Constanti.T_ATTR_CODE_MODALITA_ACCREDITO_ASSEGNO);
				salvaAttributoTAttr(attributoInfoEstera, datiOperazione, false, Constanti.T_ATTR_CODE_MODALITA_ACCREDITO_ALTRO);
			} else if (cartaEstera.getTipoPagamento().equals(CartaEstera.TipoPagamento.Assegno)) {
				salvaAttributoTAttr(attributoInfoEstera, datiOperazione, false, Constanti.T_ATTR_CODE_MODALITA_ACCREDITO_BONIFICO);
				salvaAttributoTAttr(attributoInfoEstera, datiOperazione, true, Constanti.T_ATTR_CODE_MODALITA_ACCREDITO_ASSEGNO);
				salvaAttributoTAttr(attributoInfoEstera, datiOperazione, false, Constanti.T_ATTR_CODE_MODALITA_ACCREDITO_ALTRO);
			} else if (cartaEstera.getTipoPagamento().equals(CartaEstera.TipoPagamento.Altro)) {
				salvaAttributoTAttr(attributoInfoEstera, datiOperazione, false, Constanti.T_ATTR_CODE_MODALITA_ACCREDITO_BONIFICO);
				salvaAttributoTAttr(attributoInfoEstera, datiOperazione, false, Constanti.T_ATTR_CODE_MODALITA_ACCREDITO_ASSEGNO);
				salvaAttributoTAttr(attributoInfoEstera, datiOperazione, true, Constanti.T_ATTR_CODE_MODALITA_ACCREDITO_ALTRO);
			} else {
				salvaAttributoTAttr(attributoInfoEstera, datiOperazione, false, Constanti.T_ATTR_CODE_MODALITA_ACCREDITO_BONIFICO);
				salvaAttributoTAttr(attributoInfoEstera, datiOperazione, false, Constanti.T_ATTR_CODE_MODALITA_ACCREDITO_ASSEGNO);
				salvaAttributoTAttr(attributoInfoEstera, datiOperazione, false, Constanti.T_ATTR_CODE_MODALITA_ACCREDITO_ALTRO);
			}

		}

		//3. PreDocumenti Carta (righe)
		if (cartaContabileInput.getListaPreDocumentiCarta()!=null && cartaContabileInput.getListaPreDocumentiCarta().size()>0) {

			for (PreDocumentoCarta preDocumentoCarta : cartaContabileInput.getListaPreDocumentiCarta()) {
				if (preDocumentoCarta!=null) {
					SiacTCartacontDetFin siacTCartacontDet=new SiacTCartacontDetFin();

					siacTCartacontDet.setCartacDetNumero(preDocumentoCarta.getNumero());
					if (preDocumentoCarta.getDescrizione()!=null) {
						preDocumentoCarta.setDescrizione(preDocumentoCarta.getDescrizione().toUpperCase());
					}
					siacTCartacontDet.setCartacDetDesc(preDocumentoCarta.getDescrizione());

					if (preDocumentoCarta.getDataDocumento()!=null) {
						Calendar calendar = new GregorianCalendar();
						calendar.setTime(preDocumentoCarta.getDataDocumento());
						Timestamp dataDocumento = new Timestamp(calendar.getTimeInMillis());
						siacTCartacontDet.setCartacDetData(dataDocumento);
					}

					siacTCartacontDet.setCartacDetImporto(preDocumentoCarta.getImporto());
					siacTCartacontDet.setCartacDetImportoValuta(preDocumentoCarta.getImportoValutaEstera());
					siacTCartacontDet.setSiacTCartacont(siacTCartacontIns);

					//Conto tesoreria
					SiacDContotesoreriaFin siacDContotesoreria=new SiacDContotesoreriaFin();

					String codConto = preDocumentoCarta.getContoTesoreria().getCodice();
					siacDContotesoreria=siacDContotesoreriaRepository.findContotesoreriaByCode(idEnte, codConto, datiOperazione.getTs());

					siacTCartacontDet.setSiacDContotesoreria(siacDContotesoreria);


					siacTCartacontDet = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTCartacontDet, datiOperazione, siacTAccountRepository);
					siacTCartacontDet = siacTCartaContDetRepository.saveAndFlush(siacTCartacontDet);


					//Attributi predocumento carta (note)
					AttributoTClassInfoDto attributoInfoDet = new AttributoTClassInfoDto();
					attributoInfoDet.setTipoOggetto(OggettoDellAttributoTClass.T_CARTACONT_DET);
					attributoInfoDet.setSiacTCartacontDet(siacTCartacontDet);

					if (preDocumentoCarta.getNote()!=null) {
						preDocumentoCarta.setNote(preDocumentoCarta.getNote().toUpperCase());
					}
					salvaAttributoTAttr(attributoInfoDet, datiOperazione, preDocumentoCarta.getNote(), Constanti.T_ATTR_CODE_NOTE_CC);


					//Soggetto - Sede secondaria soggetto
					SiacRCartacontDetSoggettoFin siacRCartacontDetSoggetto = new SiacRCartacontDetSoggettoFin();

					Soggetto soggettoRiga=preDocumentoCarta.getSoggetto();
					SiacTSoggettoFin siacTSoggetto = null;

					if (soggettoRiga!=null) {
						List<SedeSecondariaSoggetto> listaSediSecondarie = soggettoRiga.getSediSecondarie();
						SedeSecondariaSoggetto sedeSecondariaSoggetto = null;
						String codSoggetto = soggettoRiga.getCodiceSoggetto();

						if (listaSediSecondarie != null && listaSediSecondarie.size()>0) {
							sedeSecondariaSoggetto = listaSediSecondarie.get(0);
						}

						if (sedeSecondariaSoggetto != null) {

							SiacTSoggettoFin sedeSecondariaEntity = siacTSoggettoRepository.findOne(sedeSecondariaSoggetto.getUid());

							if (sedeSecondariaEntity != null) {
								SiacRCartacontDetSoggettoFin siacRCartacontDetSoggettoSedeSec = new SiacRCartacontDetSoggettoFin();
								siacRCartacontDetSoggetto = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRCartacontDetSoggettoSedeSec, datiOperazione, siacTAccountRepository);

								siacRCartacontDetSoggettoSedeSec.setSiacTCartacontDet(siacTCartacontDet);
								siacRCartacontDetSoggettoSedeSec.setSiacTSoggetto(sedeSecondariaEntity);
								siacRCartacontDetSoggettoRepository.saveAndFlush(siacRCartacontDetSoggettoSedeSec);
							}

						} else {
							siacTSoggetto = siacTSoggettoRepository.ricercaSoggettoNoSeSede(codiceAmbito, idEnte, codSoggetto, Constanti.SEDE_SECONDARIA, getNow());

							if (siacTSoggetto != null) {

								siacRCartacontDetSoggetto = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRCartacontDetSoggetto, datiOperazione, siacTAccountRepository);

								siacRCartacontDetSoggetto.setSiacTCartacontDet(siacTCartacontDet);
								siacRCartacontDetSoggetto.setSiacTSoggetto(siacTSoggetto);
								siacRCartacontDetSoggettoRepository.saveAndFlush(siacRCartacontDetSoggetto);
							}
						}
					}

					//Modalita' pagamento soggetto
					List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = preDocumentoCarta.getSoggetto().getElencoModalitaPagamento();

					if (listaModalitaPagamentoSoggetto != null && listaModalitaPagamentoSoggetto.size()==1) {
						ModalitaPagamentoSoggetto modalitaPagamentoSoggetto = listaModalitaPagamentoSoggetto.get(0);
						if (modalitaPagamentoSoggetto != null) {
							
							SiacTModpagFin siacTModpag = caricaSiacTModpagFinTenendoContoDelBugSuUidModPagCessione(modalitaPagamentoSoggetto);
							
							SiacRCartacontDetModpagFin siacRCartacontDetModpag=new SiacRCartacontDetModpagFin();
							siacRCartacontDetModpag = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRCartacontDetModpag, datiOperazione, siacTAccountRepository);
							siacRCartacontDetModpag.setSiacTCartacontDet(siacTCartacontDet);
							siacRCartacontDetModpag.setSiacTModpag(siacTModpag);
							siacRCartacontDetModpagRepository.saveAndFlush(siacRCartacontDetModpag);
						}
					}


					//Impegno - Subimpegno
					Impegno impegnoRiga=preDocumentoCarta.getImpegno();

					if (impegnoRiga!=null) {
						String annoEsercizio=String.valueOf(bilancio.getAnno());
						SiacTMovgestFin siacTMovgest = siacTMovgestRepository.ricercaSiacTMovgestPk(idEnte, annoEsercizio,
								impegnoRiga.getAnnoMovimento(),
								impegnoRiga.getNumero(),
								Constanti.MOVGEST_TIPO_IMPEGNO);

						List<SubImpegno> listaSubImpegnoRiga=impegnoRiga.getElencoSubImpegni();
						if (listaSubImpegnoRiga!=null && listaSubImpegnoRiga.size()==1) {
							//SubImpegno
							SubImpegno subImpegnoRiga=listaSubImpegnoRiga.get(0);

							String codiceSubImpegnoRiga = subImpegnoRiga.getNumero().toString();
							List<SiacTMovgestTsFin> listaSiacTMovgestTs = siacTMovgestTsRepository.findSubMovgestTsByCodeAndMovgestId(idEnte, getNow(), siacTMovgest.getUid(), codiceSubImpegnoRiga);
							if (listaSiacTMovgestTs != null && listaSiacTMovgestTs.size() > 0) {
								SiacTMovgestTsFin siacTMovgestTsSubImpegno = listaSiacTMovgestTs.get(0);

								SiacRCartacontDetMovgestTFin siacRCartacontDetMovgestTsSub = new SiacRCartacontDetMovgestTFin();
								siacRCartacontDetMovgestTsSub = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRCartacontDetMovgestTsSub, datiOperazione, siacTAccountRepository);
								siacRCartacontDetMovgestTsSub.setSiacTCartacontDet(siacTCartacontDet);
								siacRCartacontDetMovgestTsSub.setSiacTMovgestT(siacTMovgestTsSubImpegno);
								siacRCartacontDetMovgestTsSub = siacRCartacontDetMovgestTRepository.saveAndFlush(siacRCartacontDetMovgestTsSub);
							}
						} else {
							//Impegno
							List<SiacTMovgestTsFin> siacTMovgestTsList = siacTMovgestTsRepository.findMovgestTsByMovgest(idEnte, getNow(), siacTMovgest.getUid());

							if (siacTMovgestTsList != null && siacTMovgestTsList.size() > 0) {
								SiacTMovgestTsFin siacTMovgestTs = siacTMovgestTsList.get(0);


								SiacRCartacontDetMovgestTFin siacRCartacontDetMovgestTsImp = new SiacRCartacontDetMovgestTFin();
								siacRCartacontDetMovgestTsImp = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRCartacontDetMovgestTsImp, datiOperazione, siacTAccountRepository);
								siacRCartacontDetMovgestTsImp.setSiacTCartacontDet(siacTCartacontDet);
								siacRCartacontDetMovgestTsImp.setSiacTMovgestT(siacTMovgestTs);
								siacRCartacontDetMovgestTsImp = siacRCartacontDetMovgestTRepository.saveAndFlush(siacRCartacontDetMovgestTsImp);
							}
						}
						
						//INOLTRE PUO' ESSERCI UN LEGAME CON UN MUTUO:
						SiacRMutuoVoceCartacontDet siacRMutuoVoceCartacontDet =null;
						if(impegnoRiga.getListaVociMutuo()!=null && impegnoRiga.getListaVociMutuo().size()>0){
							VoceMutuo voceMutuoDaAssociare = impegnoRiga.getListaVociMutuo().get(0);
							if(voceMutuoDaAssociare!=null){
								siacRMutuoVoceCartacontDet = new SiacRMutuoVoceCartacontDet();
								siacRMutuoVoceCartacontDet.setSiacTCartacontDet(siacTCartacontDet);
								BigDecimal idVoceMutuo = voceMutuoDaAssociare.getIdVoceMutuo();
								SiacTMutuoVoceFin siacTMutuoVoce = siacTMutuoVoceRepository.findOne(idVoceMutuo.intValue());
								siacRMutuoVoceCartacontDet.setSiacTMutuoVoce(siacTMutuoVoce);
								siacRMutuoVoceCartacontDet  = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRMutuoVoceCartacontDet, datiOperazione, siacTAccountRepository);
								siacRMutuoVoceCartacontDet = siacRMutuoVoceCartacontDetRepository.saveAndFlush(siacRMutuoVoceCartacontDet);
							}
						}
						
						//
						
						
					}

					//SubDocumento Spesa
					if (preDocumentoCarta.getListaSubDocumentiSpesaCollegati()!=null &&
							preDocumentoCarta.getListaSubDocumentiSpesaCollegati().size()>0) {
						for (SubdocumentoSpesa subdocumentoSpesaCollegato : preDocumentoCarta.getListaSubDocumentiSpesaCollegati()) {
							if (subdocumentoSpesaCollegato!=null &&
									subdocumentoSpesaCollegato.getNumero()!=null &&
									subdocumentoSpesaCollegato.getDocumento()!=null &&
									subdocumentoSpesaCollegato.getDocumento().getAnno()!=null &&
									subdocumentoSpesaCollegato.getDocumento().getNumero()!=null) {
								SiacTSubdocFin siacTSubdoc = siacTSubdocRepository.findSubDocumentoByAnnoDocNumDocNumSubDoc(idEnte,
										subdocumentoSpesaCollegato.getDocumento().getAnno(),
										subdocumentoSpesaCollegato.getDocumento().getNumero(),										
										subdocumentoSpesaCollegato.getNumero(),
										subdocumentoSpesaCollegato.getDocumento().getTipoDocumento().getCodice(),
										getNow());

								if(siacTCartacontDet!=null && siacTSubdoc!=null){
									SiacRCartacontDetSubdocFin siacRCartacontDetSubdoc = new SiacRCartacontDetSubdocFin(); 

									siacRCartacontDetSubdoc.setSiacTCartacontDet(siacTCartacontDet);
									siacRCartacontDetSubdoc.setSiacTSubdoc(siacTSubdoc);
									siacRCartacontDetSubdoc  = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRCartacontDetSubdoc, datiOperazione, siacTAccountRepository);
									siacRCartacontDetSubdocRepository.saveAndFlush(siacRCartacontDetSubdoc);
								}
							}
						}
					}

				}
			}
		}

		SiacTCartacontFin siacTCartacontSalvata= siacTCartacontIns;
		if (siacTCartacontIns.getCartacId()!=null) {
			siacTCartacontSalvata= siacTCartaContRepository.findOne(siacTCartacontIns.getCartacId());
			entityRefresh(siacTCartacontSalvata);
		}
		
		CartaContabile cartaInserita=creaCarta(richiedente, codiceAmbito, ente, siacTCartacontSalvata);
		
		esito.setCartaContabile(cartaInserita);


		//Termino restituendo l'oggetto di ritorno: 
        return esito;
	}

	/**
	 * 
	 * Il caricamento va fatto tenendo conto del fatto che per mod pag di tipo cessione 
	 * non abbiamo l'uid della mod pag ma della soggetto relaz
	 * vedi soggettofindad.modalitaPagamentoEntityToModalitaPagamentoCessioniModel
	 * dove setta modPagDef.setUid(siacRSoggettoRelaz.getUid());
	 * 
	 * @param modalitaPagamentoSoggetto
	 * @return
	 */
	private SiacTModpagFin caricaSiacTModpagFinTenendoContoDelBugSuUidModPagCessione(ModalitaPagamentoSoggetto modalitaPagamentoSoggetto){
		SiacTModpagFin siacTModpag = null;
		if(modalitaPagamentoSoggetto.getModalitaPagamentoSoggettoCessione2() != null && modalitaPagamentoSoggetto.getModalitaPagamentoSoggettoCessione2().getUid() != 0) {
			siacTModpag = siacTModpagRepository.findOne(modalitaPagamentoSoggetto.getModalitaPagamentoSoggettoCessione2().getUid());
		} else {
			siacTModpag = siacTModpagRepository.findOne(modalitaPagamentoSoggetto.getUid());
		}
		return siacTModpag;
	}
	
	/**
	 * Operazione di aggiornamento della carta contabile.
	 * Richiamato dal servizio AggiornaCartaContabileService.
	 * 
	 * @param richiedente
	 * @param ente
	 * @param bilancio
	 * @param cartaContabileInput
	 * @param datiOperazione
	 * @return
	 */
	public EsitoGestioneCartaContabileDto aggiornaCartaContabile(Richiedente richiedente, String codiceAmbito, Ente ente, Bilancio bilancio, CartaContabile cartaContabileInput, DatiOperazioneDto datiOperazione){
		EsitoGestioneCartaContabileDto esito=new EsitoGestioneCartaContabileDto();

		CartaContabile cartaContabileDaDB = new CartaContabile();
		cartaContabileDaDB.setNumero(cartaContabileInput.getNumero());

		RicercaCartaContabileK rcck=new RicercaCartaContabileK();
		rcck.setBilancio(bilancio);
		rcck.setCartaContabile(cartaContabileDaDB);

		cartaContabileDaDB = ricercaCartaContabile(rcck, richiedente, codiceAmbito, ente, datiOperazione, true);

		List<Errore> esitoControlli=controlliAggiornaCartaContabile(richiedente, ente, bilancio, cartaContabileInput, cartaContabileDaDB, datiOperazione);

		if (esitoControlli!=null && esitoControlli.size()>0) {
			esito.setListaErrori(esitoControlli);
			esito.setCartaContabile(null);
			return esito;
		}

		int idEnte=ente.getUid();

		//1. Carta contabile
		SiacTCartacontFin siacTCartacontAgg=siacTCartaContRepository.findOne(cartaContabileDaDB.getUid());

		boolean cambioStato=false;
		//stato carta
		if (cartaContabileInput.getStatoOperativoCartaContabile()!=null && 
				!cartaContabileInput.getStatoOperativoCartaContabile().equals(cartaContabileDaDB.getStatoOperativoCartaContabile())) {
			SiacDCartacontStatoFin siacDCartacontStato=new SiacDCartacontStatoFin();

			String statoOP = Constanti.statoOperativoCartaContabileEnumToString(cartaContabileInput.getStatoOperativoCartaContabile());
			siacDCartacontStato=siacDCartacontStatoRepository.findDCartaContStatoValidoByEnteAndCode(idEnte, statoOP, datiOperazione.getTs());

			if (siacTCartacontAgg.getSiacRCartacontStatos()!=null && siacTCartacontAgg.getSiacRCartacontStatos().size()>0) {
				for (SiacRCartacontStatoFin siacRCartacontStatoOld : siacTCartacontAgg.getSiacRCartacontStatos()) {
					if (siacRCartacontStatoOld.getDataFineValidita()==null && siacRCartacontStatoOld.getDataCancellazione()==null) {
						DatiOperazioneDto datiOperazioneCancella=new DatiOperazioneDto(datiOperazione.getCurrMillisec(), 
								Operazione.CANCELLAZIONE_LOGICA_RECORD, 
								datiOperazione.getSiacTEnteProprietario(), 
								datiOperazione.getSiacDAmbito(), 
								datiOperazione.getAccountCode());
						DatiOperazioneUtils.annullaRecord(siacRCartacontStatoOld, siacRCartacontStatoRepository, datiOperazioneCancella, siacTAccountRepository);
					}
				}
			}

			SiacRCartacontStatoFin siacRCartacontStato=new SiacRCartacontStatoFin();
			siacRCartacontStato = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRCartacontStato, datiOperazione, siacTAccountRepository);
			siacRCartacontStato.setSiacTCartacont(siacTCartacontAgg);
			siacRCartacontStato.setSiacDCartacontStato(siacDCartacontStato);
			siacRCartacontStatoRepository.saveAndFlush(siacRCartacontStato);

			cambioStato=true;
		}

		if (!cambioStato) {

			//Inserire dati cartaContabileInput nella siacTCartacontAgg
			if (cartaContabileInput.getOggetto()!=null) {
				cartaContabileInput.setOggetto(cartaContabileInput.getOggetto().toUpperCase());
			}
			siacTCartacontAgg.setCartacOggetto(cartaContabileInput.getOggetto());
			siacTCartacontAgg.setCartacImporto(cartaContabileInput.getImporto());
			if (cartaContabileInput.getCausale()!=null) {
				cartaContabileInput.setCausale(cartaContabileInput.getCausale().toUpperCase());
			}
			siacTCartacontAgg.setCartacCausale(cartaContabileInput.getCausale());
			siacTCartacontAgg.setCartacImportoValuta(cartaContabileInput.getImportoValuta());
			siacTCartacontAgg.setCartacNumeroReg(cartaContabileInput.getNumRegistrazione());

			if (cartaContabileInput.getDataScadenza() != null) {
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(cartaContabileInput.getDataScadenza());
				Timestamp dataScadenzaTs = new Timestamp(calendar.getTimeInMillis());
				siacTCartacontAgg.setCartacDataScadenza(dataScadenzaTs);
			}

			if (cartaContabileInput.getDataEsecuzionePagamento() != null) {
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(cartaContabileInput.getDataEsecuzionePagamento());
				Timestamp dataPagamentoTs = new Timestamp(calendar.getTimeInMillis());
				siacTCartacontAgg.setCartacDataPagamento(dataPagamentoTs);
			}

			//Bilancio
			SiacTBilFin siacTBilCarta = new SiacTBilFin();
			List<SiacTBilFin> siacTBilList = siacTBilRepository.getValidoByAnno(idEnte, Integer.toString(bilancio.getAnno()), datiOperazione.getTs());
			if(siacTBilList!=null && siacTBilList.size()>0 && siacTBilList.get(0)!=null){
				siacTBilCarta = siacTBilList.get(0);
			}

			siacTCartacontAgg.setSiacTBil(siacTBilCarta);

			siacTCartacontAgg = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTCartacontAgg, datiOperazione, siacTAccountRepository);
			siacTCartacontAgg = siacTCartaContRepository.saveAndFlush(siacTCartacontAgg);

			//Atto Amm
			if (cartaContabileInput.getAttoAmministrativo()!=null && cartaContabileInput.getAttoAmministrativo().getAnno()!=0 &&
					cartaContabileInput.getAttoAmministrativo().getTipoAtto()!=null && cartaContabileInput.getAttoAmministrativo().getNumero()!=0) {
				//carichiamo l'atto tramite la funzione centralizzata:
				SiacTAttoAmmFin siacTAttoAmm = getSiacTAttoAmmFromAttoAmministrativo(cartaContabileInput.getAttoAmministrativo(), idEnte);
				//aggiorniamo il legame con l'atto trovato:
				if (siacTAttoAmm!=null){
					siacTCartacontAgg.setSiacTAttoAmm(siacTAttoAmm);
					siacTCartacontAgg = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTCartacontAgg, datiOperazione, siacTAccountRepository);
					siacTCartacontAgg = siacTCartaContRepository.saveAndFlush(siacTCartacontAgg);
				}
			}

			//Attributi carta contabile (note, motivo urgenza)
			AttributoTClassInfoDto attributoInfo = new AttributoTClassInfoDto();
			attributoInfo.setTipoOggetto(OggettoDellAttributoTClass.T_CARTACONT);
			attributoInfo.setSiacTCartacont(siacTCartacontAgg);

			if (cartaContabileInput.getNote()!=null && !cartaContabileInput.getNote().equalsIgnoreCase(cartaContabileDaDB.getNote())) {
				if (cartaContabileInput.getNote()!=null) {
					cartaContabileInput.setNote(cartaContabileInput.getNote().toUpperCase());
				}
				salvaAttributoTAttr(attributoInfo, datiOperazione, cartaContabileInput.getNote(), Constanti.T_ATTR_CODE_NOTE_CC);
			}

			if (cartaContabileInput.getMotivoUrgenza()!=null && !cartaContabileInput.getMotivoUrgenza().equalsIgnoreCase(cartaContabileDaDB.getMotivoUrgenza())) {
				if (cartaContabileInput.getMotivoUrgenza()!=null) {
					cartaContabileInput.setMotivoUrgenza(cartaContabileInput.getMotivoUrgenza().toUpperCase());
				}
				salvaAttributoTAttr(attributoInfo, datiOperazione, cartaContabileInput.getMotivoUrgenza(), Constanti.T_ATTR_CODE_MOTIVO_URGENZA);
			}
			
			//Firma 1:
			if(!isEmpty(cartaContabileInput.getFirma1()) && !cartaContabileInput.getFirma1().equalsIgnoreCase(cartaContabileDaDB.getFirma1())){
				salvaAttributoTAttr(attributoInfo, datiOperazione, cartaContabileInput.getFirma1(), Constanti.T_ATTR_FIRMA_1_CARTA_CONT);
			}
			
			//Firma 2:
			if(!isEmpty(cartaContabileInput.getFirma2()) && !cartaContabileInput.getFirma2().equalsIgnoreCase(cartaContabileDaDB.getFirma2())){
				salvaAttributoTAttr(attributoInfo, datiOperazione, cartaContabileInput.getFirma2(), Constanti.T_ATTR_FIRMA_2_CARTA_CONT);
			}


			//2. Carta Estera
			if (cartaContabileInput.getCartaEstera()!=null) {
				//La ricerca per chiave restituisce una lista...
				List<CartaEstera> listaCartaEsteraDaDB=cartaContabileDaDB.getListaCarteEstere();
				CartaEstera cartaEsteraDaDB=null;
				if (listaCartaEsteraDaDB!=null && listaCartaEsteraDaDB.size()>0) {
					cartaEsteraDaDB=listaCartaEsteraDaDB.get(0);
				}

				CartaEstera cartaEstera=cartaContabileInput.getCartaEstera();
				SiacTCartacontEsteraFin siacTCartacontEstera = new SiacTCartacontEsteraFin();

				if (cartaEsteraDaDB!=null) {
					siacTCartacontEstera=siacTCartaContEsteraRepository.findOne(cartaEsteraDaDB.getIdCartaEstera());

					siacTCartacontEstera.setSiacTCartacont(siacTCartacontAgg);
					if (cartaEstera.getCausalePagamento()!=null) {
						cartaEstera.setCausalePagamento(cartaEstera.getCausalePagamento().toUpperCase());
					}
					siacTCartacontEstera.setCartacestCausalepagamento(cartaEstera.getCausalePagamento());
					if (cartaEstera.getIstruzioni()!=null) {
						cartaEstera.setIstruzioni(cartaEstera.getIstruzioni().toUpperCase());
					}
					siacTCartacontEstera.setCartacestIstruzioni(cartaEstera.getIstruzioni());
					siacTCartacontEstera.setCartacestDiversotitolare(cartaEstera.getDiversoTitolare());

					//Commissioni Estero
					SiacDCommissioniesteroFin siacDCommissioniestero=new SiacDCommissioniesteroFin();

					String codCommissioni = cartaEstera.getCommissioniEstero().getCodice();
					siacDCommissioniestero=siacDCommissioniEsteroRepository.findDCommissioneEsteroValidoByEnteAndCode(idEnte, codCommissioni, datiOperazione.getTs());

					siacTCartacontEstera.setSiacDCommissioniestero(siacDCommissioniestero);

					//Valuta
					SiacDValutaFin siacDValuta=new SiacDValutaFin();

					String codvaluta = cartaEstera.getValuta().getCodice();
					siacDValuta=siacDValutaRepository.findDValutaValidaByEnteAndCode(idEnte, codvaluta, datiOperazione.getTs());

					siacTCartacontEstera.setSiacDValuta(siacDValuta);

					siacTCartacontEstera = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTCartacontEstera, datiOperazione, siacTAccountRepository);
					siacTCartacontEstera = siacTCartaContEsteraRepository.saveAndFlush(siacTCartacontEstera);

					//Attributi carta estera (bonifico assegno)
					AttributoTClassInfoDto attributoInfoEstera = new AttributoTClassInfoDto();
					attributoInfoEstera.setTipoOggetto(OggettoDellAttributoTClass.T_CARTACONT_ESTERA);
					attributoInfoEstera.setSiacTCartacontEstera(siacTCartacontEstera);

					if (!cartaEstera.getTipoPagamento().equals(cartaEsteraDaDB.getTipoPagamento())) {
						if (cartaEstera.getTipoPagamento().equals(CartaEstera.TipoPagamento.Bonifico)) {
							salvaAttributoTAttr(attributoInfoEstera, datiOperazione, true, Constanti.T_ATTR_CODE_MODALITA_ACCREDITO_BONIFICO);
							salvaAttributoTAttr(attributoInfoEstera, datiOperazione, false, Constanti.T_ATTR_CODE_MODALITA_ACCREDITO_ASSEGNO);
							salvaAttributoTAttr(attributoInfoEstera, datiOperazione, false, Constanti.T_ATTR_CODE_MODALITA_ACCREDITO_ALTRO);
						} else if (cartaEstera.getTipoPagamento().equals(CartaEstera.TipoPagamento.Assegno)) {
							salvaAttributoTAttr(attributoInfoEstera, datiOperazione, false, Constanti.T_ATTR_CODE_MODALITA_ACCREDITO_BONIFICO);
							salvaAttributoTAttr(attributoInfoEstera, datiOperazione, true, Constanti.T_ATTR_CODE_MODALITA_ACCREDITO_ASSEGNO);
							salvaAttributoTAttr(attributoInfoEstera, datiOperazione, false, Constanti.T_ATTR_CODE_MODALITA_ACCREDITO_ALTRO);
						} else if (cartaEstera.getTipoPagamento().equals(CartaEstera.TipoPagamento.Altro)) {
							salvaAttributoTAttr(attributoInfoEstera, datiOperazione, false, Constanti.T_ATTR_CODE_MODALITA_ACCREDITO_BONIFICO);
							salvaAttributoTAttr(attributoInfoEstera, datiOperazione, false, Constanti.T_ATTR_CODE_MODALITA_ACCREDITO_ASSEGNO);
							salvaAttributoTAttr(attributoInfoEstera, datiOperazione, true, Constanti.T_ATTR_CODE_MODALITA_ACCREDITO_ALTRO);
						} else {
							salvaAttributoTAttr(attributoInfoEstera, datiOperazione, false, Constanti.T_ATTR_CODE_MODALITA_ACCREDITO_BONIFICO);
							salvaAttributoTAttr(attributoInfoEstera, datiOperazione, false, Constanti.T_ATTR_CODE_MODALITA_ACCREDITO_ASSEGNO);
							salvaAttributoTAttr(attributoInfoEstera, datiOperazione, false, Constanti.T_ATTR_CODE_MODALITA_ACCREDITO_ALTRO);
						}
					}
				}
			}


			//3. PreDocumenti Carta (righe)
			if (cartaContabileInput.getListaPreDocumentiCarta()!=null && cartaContabileInput.getListaPreDocumentiCarta().size()>0) {

				List<PreDocumentoCarta> righeDaAggiornare=new ArrayList<PreDocumentoCarta>();
				List<PreDocumentoCarta> righeDaInserire=new ArrayList<PreDocumentoCarta>();
				List<PreDocumentoCarta> righeDaEliminare=new ArrayList<PreDocumentoCarta>();

				for (PreDocumentoCarta preDocumentoCartaInput : cartaContabileInput.getListaPreDocumentiCarta()) {
					for (PreDocumentoCarta preDocumentoCartaDaDB : cartaContabileDaDB.getListaPreDocumentiCarta()) {
						if (preDocumentoCartaInput.getNumero().compareTo(preDocumentoCartaDaDB.getNumero())==0) {
							preDocumentoCartaInput.setIdPreDocumentoCarta(preDocumentoCartaDaDB.getIdPreDocumentoCarta());
							righeDaAggiornare.add(preDocumentoCartaInput);
							break;
						}
					}
				}

				for (PreDocumentoCarta preDocumentoCartaDaDB : cartaContabileDaDB.getListaPreDocumentiCarta()) {
					boolean esiste=false;
					for (PreDocumentoCarta preDocumentoCartaDaAgg : righeDaAggiornare) {
						if (preDocumentoCartaDaAgg.getNumero().compareTo(preDocumentoCartaDaDB.getNumero())==0) {
							esiste=true;
							break;
						}
					}
					if (!esiste) {
						righeDaEliminare.add(preDocumentoCartaDaDB);
					}
				}

				for (PreDocumentoCarta preDocumentoCartaInput : cartaContabileInput.getListaPreDocumentiCarta()) {
					boolean esiste=false;
					for (PreDocumentoCarta preDocumentoCartaDaAgg : righeDaAggiornare) {
						if (preDocumentoCartaDaAgg.getNumero().compareTo(preDocumentoCartaInput.getNumero())==0) {
							esiste=true;
							break;
						}
					}
					if (!esiste) {
						righeDaInserire.add(preDocumentoCartaInput);
					}
				}



				for (PreDocumentoCarta preDocumentoCartaDaAggiornare : righeDaAggiornare) {
					DatiOperazioneDto datiOperazioneIns=new DatiOperazioneDto(datiOperazione.getCurrMillisec(), Operazione.MODIFICA, datiOperazione.getSiacTEnteProprietario(), datiOperazione.getAccountCode());

					List<SiacTCartacontDetFin> listaSiacTCartacontDetDaDB=siacTCartacontAgg.getSiacTCartacontDets();
					if (listaSiacTCartacontDetDaDB!=null && listaSiacTCartacontDetDaDB.size()>0) {
						for (SiacTCartacontDetFin siacTCartacontDetDaDB : listaSiacTCartacontDetDaDB) {
							if (siacTCartacontDetDaDB.getCartacDetNumero().compareTo(preDocumentoCartaDaAggiornare.getNumero())==0) {
								aggiornaRiga(codiceAmbito, idEnte, bilancio, preDocumentoCartaDaAggiornare, siacTCartacontDetDaDB, siacTCartacontAgg, datiOperazioneIns);
							}
						}
					}
				}

				for (PreDocumentoCarta preDocumentoCartaDaInserire : righeDaInserire) {
					DatiOperazioneDto datiOperazioneIns=new DatiOperazioneDto(datiOperazione.getCurrMillisec(), Operazione.INSERIMENTO, datiOperazione.getSiacTEnteProprietario(), datiOperazione.getAccountCode());
					inserisciRiga(codiceAmbito, idEnte, bilancio, preDocumentoCartaDaInserire, siacTCartacontAgg, datiOperazioneIns);
				}

				for (PreDocumentoCarta preDocumentoCartaDaEliminare : righeDaEliminare) {
					DatiOperazioneDto datiOperazioneCancella=new DatiOperazioneDto(getCurrentMillisecondsTrentaMaggio2017(), Operazione.CANCELLAZIONE_LOGICA_RECORD, datiOperazione.getSiacTEnteProprietario(), datiOperazione.getAccountCode());
					cancellaRiga(preDocumentoCartaDaEliminare, datiOperazioneCancella);
				}
			}
		}

		SiacTCartacontFin siacTCartacontSalvata= siacTCartacontAgg;
		if (siacTCartacontAgg.getCartacId()!=null) {
			siacTCartacontSalvata= siacTCartaContRepository.findOne(siacTCartacontAgg.getCartacId());
			entityRefresh(siacTCartacontSalvata);
		}
		
		CartaContabile cartaAggiornata=creaCarta(richiedente, codiceAmbito, ente, siacTCartacontAgg);

		esito.setCartaContabile(cartaAggiornata);


		//Termino restituendo l'oggetto di ritorno: 
        return esito;
	}
	
	/**
	 * Metodo che salva le modifiche apportate ad un predocumento della carta.
	 * 
	 * @param idEnte
	 * @param bilancio
	 * @param rigaDaAggiornare
	 * @param siacTCartacontDetDaDB
	 * @param siacTCartacont
	 * @param datiOperazione
	 */
	public void collegaQuotaDocumentoARigaCarta(Integer uidCartaCont, Integer numeroRiga,SubdocumentoSpesa subDocSpesaDaCollegare,Bilancio bilancio, DatiOperazioneDto datiOperazione) {
		
		Integer idEnte = datiOperazione.getSiacTEnteProprietario().getUid();
		
		//carico i dati preliminari necessari:
		SiacTCartacontFin siacTCartacont = siacTCartaContRepository.findOne(uidCartaCont);
		List<SiacTCartacontDetFin> listaSiacTCartacontDetDaDB = siacTCartacont.getSiacTCartacontDets();
		SiacTCartacontDetFin siacTCartacontDetDaDB = getByNumeroRiga(listaSiacTCartacontDetDaDB, numeroRiga);
		//
		
		//Impegno - Subimpegno
		Impegno impegnoRiga = subDocSpesaDaCollegare.getImpegno();
		if(subDocSpesaDaCollegare.getSubImpegno()!=null){
			impegnoRiga.setElencoSubImpegni(toList(subDocSpesaDaCollegare.getSubImpegno()));
		}

		//richiamo il metodo di aggiornamento del movimento di una riga:
		aggiornaImpegnoRiga(impegnoRiga, bilancio, idEnte, siacTCartacontDetDaDB, datiOperazione);
		//
		
		//Subdocumento spesa
		List<SubdocumentoSpesa> listaSubdocumentoSpesaDaAggiornare = toList(subDocSpesaDaCollegare);
		
		//Richiamo il metodo che aggiorna il sub documento:
		aggiornaSubDocRiga(listaSubdocumentoSpesaDaAggiornare, siacTCartacontDetDaDB, datiOperazione, idEnte);
		
	}
	
	private SiacTCartacontDetFin getByNumeroRiga(List<SiacTCartacontDetFin> listaSiacTCartacontDetDaDB,Integer numeroRiga){
		SiacTCartacontDetFin trovato = null;
		if(!isEmpty(listaSiacTCartacontDetDaDB) && numeroRiga!=null){
			for(SiacTCartacontDetFin it: listaSiacTCartacontDetDaDB){
				if(it!=null && it.getCartacDetNumero()!=null && it.getCartacDetNumero().equals(numeroRiga)){
					trovato = it;
					break;
				}
			}
		}
		return trovato;
	}
	
	

	/**
	 * Metodo che salva le modifiche apportate ad un predocumento della carta.
	 * 
	 * @param idEnte
	 * @param bilancio
	 * @param rigaDaAggiornare
	 * @param siacTCartacontDetDaDB
	 * @param siacTCartacont
	 * @param datiOperazione
	 */
	private void aggiornaRiga(String codiceAmbito, Integer idEnte, Bilancio bilancio, PreDocumentoCarta rigaDaAggiornare, SiacTCartacontDetFin siacTCartacontDetDaDB, SiacTCartacontFin siacTCartacont, DatiOperazioneDto datiOperazione) {

		siacTCartacontDetDaDB.setCartacDetNumero(rigaDaAggiornare.getNumero());
		if (rigaDaAggiornare.getDescrizione()!=null) {
			rigaDaAggiornare.setDescrizione(rigaDaAggiornare.getDescrizione().toUpperCase());
		}
		siacTCartacontDetDaDB.setCartacDetDesc(rigaDaAggiornare.getDescrizione());

		if (rigaDaAggiornare.getDataDocumento()!=null) {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(rigaDaAggiornare.getDataDocumento());
			Timestamp dataDocumento = new Timestamp(calendar.getTimeInMillis());
			siacTCartacontDetDaDB.setCartacDetData(dataDocumento);
		}

		siacTCartacontDetDaDB.setCartacDetImporto(rigaDaAggiornare.getImporto());
		siacTCartacontDetDaDB.setCartacDetImportoValuta(rigaDaAggiornare.getImportoValutaEstera());
		siacTCartacontDetDaDB.setSiacTCartacont(siacTCartacont);

		//Conto tesoreria
		if(rigaDaAggiornare.getContoTesoreria()!=null && rigaDaAggiornare.getContoTesoreria().getCodice()!=null){
			SiacDContotesoreriaFin siacDContotesoreria=new SiacDContotesoreriaFin();
			String codConto = rigaDaAggiornare.getContoTesoreria().getCodice();
			siacDContotesoreria=siacDContotesoreriaRepository.findContotesoreriaByCode(idEnte, codConto, datiOperazione.getTs());
			siacTCartacontDetDaDB.setSiacDContotesoreria(siacDContotesoreria);
		}

		siacTCartacontDetDaDB = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTCartacontDetDaDB, datiOperazione, siacTAccountRepository);
		siacTCartacontDetDaDB = siacTCartaContDetRepository.saveAndFlush(siacTCartacontDetDaDB);


		//Attributi predocumento carta (note)
		AttributoTClassInfoDto attributoInfoDet = new AttributoTClassInfoDto();
		attributoInfoDet.setTipoOggetto(OggettoDellAttributoTClass.T_CARTACONT_DET);
		attributoInfoDet.setSiacTCartacontDet(siacTCartacontDetDaDB);

		if (rigaDaAggiornare.getNote()!=null) {
			rigaDaAggiornare.setNote(rigaDaAggiornare.getNote().toUpperCase());
		}
		salvaAttributoTAttr(attributoInfoDet, datiOperazione, rigaDaAggiornare.getNote(), Constanti.T_ATTR_CODE_NOTE_CC);


		//Soggetto - Sede secondaria soggetto
		SiacRCartacontDetSoggettoFin siacRCartacontDetSoggetto = new SiacRCartacontDetSoggettoFin();

		Soggetto soggettoRiga=rigaDaAggiornare.getSoggetto();
		SiacTSoggettoFin siacTSoggetto = null;

		if (soggettoRiga!=null) {
			List<SedeSecondariaSoggetto> listaSediSecondarie = soggettoRiga.getSediSecondarie();
			SedeSecondariaSoggetto sedeSecondariaSoggetto = null;
			String codSoggetto = soggettoRiga.getCodiceSoggetto();

			if (listaSediSecondarie != null && listaSediSecondarie.size()>0) {
				sedeSecondariaSoggetto = listaSediSecondarie.get(0);
			}
			
			List<SiacRCartacontDetSoggettoFin> listaRCartacontDetSoggetto = siacRCartacontDetSoggettoRepository.findValidoByIdDetCarta(idEnte, siacTCartacontDetDaDB.getUid(), datiOperazione.getTs());
			
			if (listaRCartacontDetSoggetto!=null && listaRCartacontDetSoggetto.size()>0) {
				
				if (sedeSecondariaSoggetto != null) {

					//Estraggo l'associazione corrente con il sogg
					SiacTSoggettoFin sedeSecondariaEntity = siacTSoggettoRepository.findOne(sedeSecondariaSoggetto.getUid());

					//Controllo che sia cambiata
					Integer oldId=listaRCartacontDetSoggetto.get(0).getSiacTSoggetto().getUid();
					if (sedeSecondariaEntity != null && oldId.compareTo(sedeSecondariaEntity.getUid())!=0) {
						//annullo quella corrente
						DatiOperazioneDto datiOperazioneCancella=new DatiOperazioneDto(datiOperazione.getCurrMillisec(), 
								Operazione.CANCELLAZIONE_LOGICA_RECORD, 
								datiOperazione.getSiacTEnteProprietario(), 
								datiOperazione.getSiacDAmbito(), 
								datiOperazione.getAccountCode());
						DatiOperazioneUtils.annullaRecord(listaRCartacontDetSoggetto.get(0), siacRCartacontDetSoggettoRepository, datiOperazioneCancella, siacTAccountRepository);

						//Inserisco quella nuova
						SiacRCartacontDetSoggettoFin siacRCartacontDetSoggettoSedeSec = new SiacRCartacontDetSoggettoFin();
						siacRCartacontDetSoggetto = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRCartacontDetSoggettoSedeSec, datiOperazione, siacTAccountRepository);

						siacRCartacontDetSoggettoSedeSec.setSiacTCartacontDet(siacTCartacontDetDaDB);
						siacRCartacontDetSoggettoSedeSec.setSiacTSoggetto(sedeSecondariaEntity);
						siacRCartacontDetSoggettoRepository.saveAndFlush(siacRCartacontDetSoggettoSedeSec);
					}

				} else {
					//Estraggo l'associazione corrente con il sogg
					siacTSoggetto = siacTSoggettoRepository.ricercaSoggettoNoSeSede(codiceAmbito, idEnte, codSoggetto, Constanti.SEDE_SECONDARIA, getNow());

					//Controllo che sia cambiata
					Integer oldId=listaRCartacontDetSoggetto.get(0).getSiacTSoggetto().getUid();
					if (siacTSoggetto != null && oldId.compareTo(siacTSoggetto.getUid())!=0) {
						//annullo quella corrente
						DatiOperazioneDto datiOperazioneCancella=new DatiOperazioneDto(datiOperazione.getCurrMillisec(), 
								Operazione.CANCELLAZIONE_LOGICA_RECORD, 
								datiOperazione.getSiacTEnteProprietario(), 
								datiOperazione.getSiacDAmbito(), 
								datiOperazione.getAccountCode());
						DatiOperazioneUtils.annullaRecord(listaRCartacontDetSoggetto.get(0), siacRCartacontDetSoggettoRepository, datiOperazioneCancella, siacTAccountRepository);

						//Inserisco quella nuova
						siacRCartacontDetSoggetto = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRCartacontDetSoggetto, datiOperazione, siacTAccountRepository);

						siacRCartacontDetSoggetto.setSiacTCartacontDet(siacTCartacontDetDaDB);
						siacRCartacontDetSoggetto.setSiacTSoggetto(siacTSoggetto);
						siacRCartacontDetSoggettoRepository.saveAndFlush(siacRCartacontDetSoggetto);
					}
				}
			}
			
			

		}

		//Modalita' pagamento soggetto
		List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = rigaDaAggiornare.getSoggetto().getElencoModalitaPagamento();

		if (listaModalitaPagamentoSoggetto != null && listaModalitaPagamentoSoggetto.size()==1) {
			ModalitaPagamentoSoggetto modalitaPagamentoSoggetto = listaModalitaPagamentoSoggetto.get(0);
			if (modalitaPagamentoSoggetto != null) {
				
				modalitaPagamentoSoggetto.getUid();
				rigaDaAggiornare.getIdPreDocumentoCarta();
				siacTCartacontDetDaDB.getUid();
				
				SiacTModpagFin siacTModpag = caricaSiacTModpagFinTenendoContoDelBugSuUidModPagCessione(modalitaPagamentoSoggetto);
				
				if (siacTCartacontDetDaDB.getUid()!=null && siacTModpag!=null && siacTModpag.getUid()!=null) {
					//Estraggo l'associazione corrente con la modpag
					List<SiacRCartacontDetModpagFin> listaRCartacontDetModpag = siacRCartacontDetModpagRepository.findValidoByIdDetCarta(idEnte, siacTCartacontDetDaDB.getUid(), datiOperazione.getTs());
				
					//controllo che sia cambiata
					if (listaRCartacontDetModpag!=null && listaRCartacontDetModpag.size()>0) {
						Integer oldId=listaRCartacontDetModpag.get(0).getSiacTModpag().getModpagId();
						if (siacTModpag.getUid().compareTo(oldId)!=0) {
							//annullo quella corrente
							DatiOperazioneDto datiOperazioneCancella=new DatiOperazioneDto(datiOperazione.getCurrMillisec(), 
									Operazione.CANCELLAZIONE_LOGICA_RECORD, 
									datiOperazione.getSiacTEnteProprietario(), 
									datiOperazione.getSiacDAmbito(), 
									datiOperazione.getAccountCode());
							DatiOperazioneUtils.annullaRecord(listaRCartacontDetModpag.get(0), siacRCartacontDetModpagRepository, datiOperazioneCancella, siacTAccountRepository);
							
							//Inserisco quella nuova
							SiacRCartacontDetModpagFin siacRCartacontDetModpag=new SiacRCartacontDetModpagFin();

							siacRCartacontDetModpag = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRCartacontDetModpag, datiOperazione, siacTAccountRepository);
							siacRCartacontDetModpag.setSiacTCartacontDet(siacTCartacontDetDaDB);
							siacRCartacontDetModpag.setSiacTModpag(siacTModpag);
							siacRCartacontDetModpagRepository.saveAndFlush(siacRCartacontDetModpag);
						}
					}
				}
			}
		}

		//Impegno - Subimpegno
		Impegno impegnoRiga=rigaDaAggiornare.getImpegno();

		//Richiamo il metodo di aggiornamento del movimento di una riga:
		aggiornaImpegnoRiga(impegnoRiga, bilancio, idEnte, siacTCartacontDetDaDB, datiOperazione);
		//
		
		//Subdocumento spesa
		List<SubdocumentoSpesa> listaSubdocumentoSpesaDaAggiornare = rigaDaAggiornare.getListaSubDocumentiSpesaCollegati();
		
		//Richiamo il metodo che aggiorna il sub documento:
		aggiornaSubDocRiga(listaSubdocumentoSpesaDaAggiornare, siacTCartacontDetDaDB, datiOperazione, idEnte);
	}
	
	/**
	 * Collega alla riga indicata gli elementi in listaSubdocumentoSpesaDaAggiornare se ancora non sono collegati.
	 * 
	 * Gli elementi gia' collegati ma non indicati in listaSubdocumentoSpesaDaAggiornare verrano scollegati.
	 * 
	 * 
	 * @param listaSubdocumentoSpesaDaAggiornare
	 * @param siacTCartacontDetDaDB
	 * @param datiOperazione
	 * @param idEnte
	 */
	private void aggiornaSubDocRiga(List<SubdocumentoSpesa> listaSubdocumentoSpesaDaAggiornare,SiacTCartacontDetFin siacTCartacontDetDaDB, DatiOperazioneDto datiOperazione, Integer idEnte){
		//SubDocumento Spesa
		
		List<SiacRCartacontDetSubdocFin> listaRCartacontDetSubdocDaDB=siacTCartacontDetDaDB.getSiacRCartacontDetSubdocs();
		if (listaRCartacontDetSubdocDaDB!=null && listaRCartacontDetSubdocDaDB.size()>0) {
			listaRCartacontDetSubdocDaDB=DatiOperazioneUtils.soloValidi(listaRCartacontDetSubdocDaDB, datiOperazione.getTs());
		}


		//se arriva da FE ma non c'e' su db lo inserisco
		if (listaSubdocumentoSpesaDaAggiornare!=null && listaSubdocumentoSpesaDaAggiornare.size()>0) {
			for (SubdocumentoSpesa subdocumentoSpesaDaAggiornare : listaSubdocumentoSpesaDaAggiornare) {
				boolean esiste=false;
				
				if (listaRCartacontDetSubdocDaDB!=null && listaRCartacontDetSubdocDaDB.size()>0) {

					for (SiacRCartacontDetSubdocFin siacRCartacontDetSubdocDaDB : listaRCartacontDetSubdocDaDB) {
						if (subdocumentoSpesaDaAggiornare.getUid()==siacRCartacontDetSubdocDaDB.getSiacTSubdoc().getUid().intValue()) {
							esiste=true;
							break;
						}
					}
				}
				
				if (!esiste) {
					//subdocumentoSpesaDaAggiornare e' da inserire
					inserisciSubDoc(idEnte, subdocumentoSpesaDaAggiornare, siacTCartacontDetDaDB, datiOperazione);
				}
			}

		}

		//se c'e' su db ma non mi arriva da FE lo cancello
		if (listaRCartacontDetSubdocDaDB!=null && listaRCartacontDetSubdocDaDB.size()>0) {
			for (SiacRCartacontDetSubdocFin siacRCartacontDetSubdocDaDB : listaRCartacontDetSubdocDaDB) {
				boolean esiste=false;

				if (listaSubdocumentoSpesaDaAggiornare!=null && listaSubdocumentoSpesaDaAggiornare.size()>0) {

					for (SubdocumentoSpesa subdocumentoSpesaDaAggiornare : listaSubdocumentoSpesaDaAggiornare) {
						if (subdocumentoSpesaDaAggiornare.getUid()==siacRCartacontDetSubdocDaDB.getSiacTSubdoc().getUid().intValue()) {
							esiste=true;
							break;
						}
					}
				}
				
				if (!esiste) {
					//siacRCartacontDetSubdocDaDB da eliminare
					DatiOperazioneDto datiOperazioneCancella=new DatiOperazioneDto(getCurrentMillisecondsTrentaMaggio2017(), Operazione.CANCELLAZIONE_LOGICA_RECORD, datiOperazione.getSiacTEnteProprietario(), datiOperazione.getAccountCode());
					DatiOperazioneUtils.cancellaRecord(siacRCartacontDetSubdocDaDB, siacRCartacontDetSubdocRepository, datiOperazioneCancella, siacTAccountRepository);
				}
			}
		}
	}
	
	/**
	 * Gestisce l'aggiornamento dell'impegno o sub di una riga gia esintente, se la riga non aveva ancora 
	 * un impegno lo inserisce altrimenti lo cambia col nuovo indicato.
	 * 
	 * In caso di sub si aspetta il sub impegno come unico elemento in impegnoRiga.getElencoSubImpegni.
	 * 
	 * Se viene passato impegnoRiga nullo elimina l'eventuale legame gia' esistente un impegno o sub.
	 * 
	 * 
	 * @param impegnoRiga
	 * @param bilancio
	 * @param idEnte
	 * @param siacTCartacontDetDaDB
	 * @param datiOperazione
	 */
	private void aggiornaImpegnoRiga(Impegno impegnoRiga, Bilancio bilancio, Integer idEnte, SiacTCartacontDetFin siacTCartacontDetDaDB,DatiOperazioneDto datiOperazione){
		if (impegnoRiga!=null) {
			String annoEsercizio=String.valueOf(bilancio.getAnno());
			SiacTMovgestFin siacTMovgest = siacTMovgestRepository.ricercaSiacTMovgestPk(idEnte, annoEsercizio,
					impegnoRiga.getAnnoMovimento(),
					impegnoRiga.getNumero(),
					Constanti.MOVGEST_TIPO_IMPEGNO);

			List<SubImpegno> listaSubImpegnoRiga=impegnoRiga.getElencoSubImpegni();
			if (listaSubImpegnoRiga!=null && listaSubImpegnoRiga.size()==1) {
				//SubImpegno
				SubImpegno subImpegnoRiga=listaSubImpegnoRiga.get(0);

				String codiceSubImpegnoRiga = subImpegnoRiga.getNumero().toString();
				List<SiacTMovgestTsFin> listaSiacTMovgestTs = siacTMovgestTsRepository.findSubMovgestTsByCodeAndMovgestId(idEnte, getNow(), siacTMovgest.getUid(), codiceSubImpegnoRiga);
				if (listaSiacTMovgestTs != null && listaSiacTMovgestTs.size() > 0) {
					SiacTMovgestTsFin siacTMovgestTsSubImpegno = listaSiacTMovgestTs.get(0);


					//Estraggo l'associazione corrente con il subimpegno
					List<SiacRCartacontDetMovgestTFin> listRCartacontDetMovgestT = siacRCartacontDetMovgestTRepository.findValidoByIdDetCarta(idEnte, siacTCartacontDetDaDB.getUid(), datiOperazione.getTs());

					//Se non esiste la inserisco
					if (listRCartacontDetMovgestT==null || listRCartacontDetMovgestT.size()==0) {
						SiacRCartacontDetMovgestTFin siacRCartacontDetMovgestTsSub = new SiacRCartacontDetMovgestTFin();
						siacRCartacontDetMovgestTsSub = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRCartacontDetMovgestTsSub, datiOperazione, siacTAccountRepository);
						siacRCartacontDetMovgestTsSub.setSiacTCartacontDet(siacTCartacontDetDaDB);
						siacRCartacontDetMovgestTsSub.setSiacTMovgestT(siacTMovgestTsSubImpegno);
						siacRCartacontDetMovgestTsSub = siacRCartacontDetMovgestTRepository.saveAndFlush(siacRCartacontDetMovgestTsSub);
					} else {
						//Altrimenti controllo che sia cambiata
						Integer oldId=listRCartacontDetMovgestT.get(0).getSiacTMovgestT().getUid();
						if (siacTMovgestTsSubImpegno.getUid().compareTo(oldId)!=0) {
							//annullo quella corrente
							DatiOperazioneDto datiOperazioneCancella=new DatiOperazioneDto(datiOperazione.getCurrMillisec(), 
									Operazione.CANCELLAZIONE_LOGICA_RECORD, 
									datiOperazione.getSiacTEnteProprietario(), 
									datiOperazione.getSiacDAmbito(), 
									datiOperazione.getAccountCode());
							DatiOperazioneUtils.annullaRecord(listRCartacontDetMovgestT.get(0), siacRCartacontDetMovgestTRepository, datiOperazioneCancella, siacTAccountRepository);

							//Inserisco quella nuova
							SiacRCartacontDetMovgestTFin siacRCartacontDetMovgestTsSub = new SiacRCartacontDetMovgestTFin();
							siacRCartacontDetMovgestTsSub = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRCartacontDetMovgestTsSub, datiOperazione, siacTAccountRepository);
							siacRCartacontDetMovgestTsSub.setSiacTCartacontDet(siacTCartacontDetDaDB);
							siacRCartacontDetMovgestTsSub.setSiacTMovgestT(siacTMovgestTsSubImpegno);
							siacRCartacontDetMovgestTsSub = siacRCartacontDetMovgestTRepository.saveAndFlush(siacRCartacontDetMovgestTsSub);
						}						
					}
				}
			} else {
				//Impegno
				List<SiacTMovgestTsFin> siacTMovgestTsList = siacTMovgestTsRepository.findMovgestTsByMovgest(idEnte, getNow(), siacTMovgest.getUid());

				if (siacTMovgestTsList != null && siacTMovgestTsList.size() > 0) {
					SiacTMovgestTsFin siacTMovgestTs = siacTMovgestTsList.get(0);

					//Estraggo l'associazione corrente con l'impegno
					List<SiacRCartacontDetMovgestTFin> listRCartacontDetMovgestT = siacRCartacontDetMovgestTRepository.findValidoByIdDetCarta(idEnte, siacTCartacontDetDaDB.getUid(), datiOperazione.getTs());

					//Se non esiste la inserisco
					if (listRCartacontDetMovgestT==null || listRCartacontDetMovgestT.size()==0) {
						SiacRCartacontDetMovgestTFin siacRCartacontDetMovgestTsImp = new SiacRCartacontDetMovgestTFin();
						siacRCartacontDetMovgestTsImp = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRCartacontDetMovgestTsImp, datiOperazione, siacTAccountRepository);
						siacRCartacontDetMovgestTsImp.setSiacTCartacontDet(siacTCartacontDetDaDB);
						siacRCartacontDetMovgestTsImp.setSiacTMovgestT(siacTMovgestTs);
						siacRCartacontDetMovgestTsImp = siacRCartacontDetMovgestTRepository.saveAndFlush(siacRCartacontDetMovgestTsImp);
					} else {
						//Altrimenti controllo che sia cambiata
						Integer oldId=listRCartacontDetMovgestT.get(0).getSiacTMovgestT().getUid();
						if (siacTMovgestTs.getUid().compareTo(oldId)!=0) {
							//annullo quella corrente
							DatiOperazioneDto datiOperazioneCancella=new DatiOperazioneDto(datiOperazione.getCurrMillisec(), 
									Operazione.CANCELLAZIONE_LOGICA_RECORD, 
									datiOperazione.getSiacTEnteProprietario(), 
									datiOperazione.getSiacDAmbito(), 
									datiOperazione.getAccountCode());
							DatiOperazioneUtils.annullaRecord(listRCartacontDetMovgestT.get(0), siacRCartacontDetMovgestTRepository, datiOperazioneCancella, siacTAccountRepository);

							//Inserisco quella nuova
							SiacRCartacontDetMovgestTFin siacRCartacontDetMovgestTsImp = new SiacRCartacontDetMovgestTFin();
							siacRCartacontDetMovgestTsImp = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRCartacontDetMovgestTsImp, datiOperazione, siacTAccountRepository);
							siacRCartacontDetMovgestTsImp.setSiacTCartacontDet(siacTCartacontDetDaDB);
							siacRCartacontDetMovgestTsImp.setSiacTMovgestT(siacTMovgestTs);
							siacRCartacontDetMovgestTsImp = siacRCartacontDetMovgestTRepository.saveAndFlush(siacRCartacontDetMovgestTsImp);
						}
					}
				}
			}
		} else {
			//Se l'impegno/sub e' null puo' essere stato tolto
			//Estraggo l'associazione corrente con l'impegno/sub
			List<SiacRCartacontDetMovgestTFin> listRCartacontDetMovgestT = siacRCartacontDetMovgestTRepository.findValidoByIdDetCarta(idEnte, siacTCartacontDetDaDB.getUid(), datiOperazione.getTs());
			
			//Se esiste la annullo
			if (listRCartacontDetMovgestT!=null && listRCartacontDetMovgestT.size()>0) {
				DatiOperazioneDto datiOperazioneCancella=new DatiOperazioneDto(datiOperazione.getCurrMillisec(), 
						Operazione.CANCELLAZIONE_LOGICA_RECORD, 
						datiOperazione.getSiacTEnteProprietario(), 
						datiOperazione.getSiacDAmbito(), 
						datiOperazione.getAccountCode());
				DatiOperazioneUtils.annullaRecord(listRCartacontDetMovgestT.get(0), siacRCartacontDetMovgestTRepository, datiOperazioneCancella, siacTAccountRepository);
			}
		}
	}
	
	/**
	 * Operazione che associa un sub documento spesa ad una riga del documento.
	 * 
	 * @param idEnte
	 * @param subdocumentoSpesaDaInserire
	 * @param siacTCartacontDetDaDB
	 * @param datiOperazione
	 */
	private void inserisciSubDoc(Integer idEnte, SubdocumentoSpesa subdocumentoSpesaDaInserire, SiacTCartacontDetFin siacTCartacontDetDaDB, DatiOperazioneDto datiOperazione) {
		if (subdocumentoSpesaDaInserire!=null &&
				subdocumentoSpesaDaInserire.getDocumento()!= null &&
				subdocumentoSpesaDaInserire.getDocumento().getAnno()!=null &&
				subdocumentoSpesaDaInserire.getDocumento().getAnno()!=null &&
				subdocumentoSpesaDaInserire.getNumero()!=null) {

			SiacTSubdocFin siacTSubdoc = siacTSubdocRepository.findSubDocumentoByAnnoDocNumDocNumSubDoc(idEnte,
					subdocumentoSpesaDaInserire.getDocumento().getAnno(),
					subdocumentoSpesaDaInserire.getDocumento().getNumero(),
					subdocumentoSpesaDaInserire.getNumero(),
					subdocumentoSpesaDaInserire.getDocumento().getTipoDocumento().getCodice(),
					getNow());

			SiacRCartacontDetSubdocFin siacRCartacontDetSubdoc = new SiacRCartacontDetSubdocFin(); 
			siacRCartacontDetSubdoc = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRCartacontDetSubdoc, datiOperazione, siacTAccountRepository);

			siacRCartacontDetSubdoc.setSiacTCartacontDet(siacTCartacontDetDaDB);
			siacRCartacontDetSubdoc.setSiacTSubdoc(siacTSubdoc);

			siacRCartacontDetSubdocRepository.saveAndFlush(siacRCartacontDetSubdoc);

		}
	}

	/**
	 * Operazione utilizzata per inserire un predocumento della carta sul DB.
	 * 
	 * @param idEnte
	 * @param bilancio
	 * @param rigaDaInserire
	 * @param siacTCartacont
	 * @param datiOperazione
	 */
	private void inserisciRiga(String codiceAmbito, Integer idEnte, Bilancio bilancio, PreDocumentoCarta rigaDaInserire, SiacTCartacontFin siacTCartacont, DatiOperazioneDto datiOperazione) {
		SiacTCartacontDetFin siacTCartacontDet=new SiacTCartacontDetFin();

		siacTCartacontDet.setCartacDetNumero(rigaDaInserire.getNumero());
		if (rigaDaInserire.getDescrizione()!=null) {
			rigaDaInserire.setDescrizione(rigaDaInserire.getDescrizione().toUpperCase());
		}
		siacTCartacontDet.setCartacDetDesc(rigaDaInserire.getDescrizione());

		if (rigaDaInserire.getDataDocumento()!=null) {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(rigaDaInserire.getDataDocumento());
			Timestamp dataDocumento = new Timestamp(calendar.getTimeInMillis());
			siacTCartacontDet.setCartacDetData(dataDocumento);
		}

		siacTCartacontDet.setCartacDetImporto(rigaDaInserire.getImporto());
		siacTCartacontDet.setCartacDetImportoValuta(rigaDaInserire.getImportoValutaEstera());
		siacTCartacontDet.setSiacTCartacont(siacTCartacont);

		//Conto tesoreria
		if(rigaDaInserire.getContoTesoreria()!=null){
			SiacDContotesoreriaFin siacDContotesoreria=new SiacDContotesoreriaFin();
			String codConto = rigaDaInserire.getContoTesoreria().getCodice();
			siacDContotesoreria=siacDContotesoreriaRepository.findContotesoreriaByCode(idEnte, codConto, datiOperazione.getTs());
			siacTCartacontDet.setSiacDContotesoreria(siacDContotesoreria);
		}


		siacTCartacontDet = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTCartacontDet, datiOperazione, siacTAccountRepository);
		siacTCartacontDet = siacTCartaContDetRepository.saveAndFlush(siacTCartacontDet);


		//Attributi predocumento carta (note)
		AttributoTClassInfoDto attributoInfoDet = new AttributoTClassInfoDto();
		attributoInfoDet.setTipoOggetto(OggettoDellAttributoTClass.T_CARTACONT_DET);
		attributoInfoDet.setSiacTCartacontDet(siacTCartacontDet);

		if (rigaDaInserire.getNote()!=null) {
			rigaDaInserire.setNote(rigaDaInserire.getNote().toUpperCase());
		}
		salvaAttributoTAttr(attributoInfoDet, datiOperazione, rigaDaInserire.getNote(), Constanti.T_ATTR_CODE_NOTE_CC);


		//Soggetto - Sede secondaria soggetto
		SiacRCartacontDetSoggettoFin siacRCartacontDetSoggetto = new SiacRCartacontDetSoggettoFin();

		Soggetto soggettoRiga=rigaDaInserire.getSoggetto();
		SiacTSoggettoFin siacTSoggetto = null;

		if (soggettoRiga!=null) {
			List<SedeSecondariaSoggetto> listaSediSecondarie = soggettoRiga.getSediSecondarie();
			SedeSecondariaSoggetto sedeSecondariaSoggetto = null;
			String codSoggetto = soggettoRiga.getCodiceSoggetto();

			if (listaSediSecondarie != null && listaSediSecondarie.size()>0) {
				sedeSecondariaSoggetto = listaSediSecondarie.get(0);
			}

			if (sedeSecondariaSoggetto != null) {

				SiacTSoggettoFin sedeSecondariaEntity = siacTSoggettoRepository.findOne(sedeSecondariaSoggetto.getUid());

				if (sedeSecondariaEntity != null) {
					SiacRCartacontDetSoggettoFin siacRCartacontDetSoggettoSedeSec = new SiacRCartacontDetSoggettoFin();
					siacRCartacontDetSoggetto = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRCartacontDetSoggettoSedeSec, datiOperazione, siacTAccountRepository);

					siacRCartacontDetSoggettoSedeSec.setSiacTCartacontDet(siacTCartacontDet);
					siacRCartacontDetSoggettoSedeSec.setSiacTSoggetto(sedeSecondariaEntity);
					siacRCartacontDetSoggettoRepository.saveAndFlush(siacRCartacontDetSoggettoSedeSec);
				}

			} else {
				siacTSoggetto = siacTSoggettoRepository.ricercaSoggettoNoSeSede(codiceAmbito, idEnte, codSoggetto, Constanti.SEDE_SECONDARIA, getNow());

				if (siacTSoggetto != null) {

					siacRCartacontDetSoggetto = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRCartacontDetSoggetto, datiOperazione, siacTAccountRepository);

					siacRCartacontDetSoggetto.setSiacTCartacontDet(siacTCartacontDet);
					siacRCartacontDetSoggetto.setSiacTSoggetto(siacTSoggetto);
					siacRCartacontDetSoggettoRepository.saveAndFlush(siacRCartacontDetSoggetto);
				}
			}
		}

		//Modalita' pagamento soggetto
		List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = rigaDaInserire.getSoggetto().getElencoModalitaPagamento();

		if (listaModalitaPagamentoSoggetto != null && listaModalitaPagamentoSoggetto.size()==1) {
			ModalitaPagamentoSoggetto modalitaPagamentoSoggetto = listaModalitaPagamentoSoggetto.get(0);
			if (modalitaPagamentoSoggetto != null) {
				SiacTModpagFin siacTModpag = caricaSiacTModpagFinTenendoContoDelBugSuUidModPagCessione(modalitaPagamentoSoggetto);

				SiacRCartacontDetModpagFin siacRCartacontDetModpag=new SiacRCartacontDetModpagFin();
				siacRCartacontDetModpag = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRCartacontDetModpag, datiOperazione, siacTAccountRepository);
				siacRCartacontDetModpag.setSiacTCartacontDet(siacTCartacontDet);
				siacRCartacontDetModpag.setSiacTModpag(siacTModpag);
				siacRCartacontDetModpagRepository.saveAndFlush(siacRCartacontDetModpag);
			}
		}


		//Impegno - Subimpegno
		Impegno impegnoRiga=rigaDaInserire.getImpegno();

		if (impegnoRiga!=null) {
			String annoEsercizio=String.valueOf(bilancio.getAnno());
			SiacTMovgestFin siacTMovgest = siacTMovgestRepository.ricercaSiacTMovgestPk(idEnte, annoEsercizio,
					impegnoRiga.getAnnoMovimento(),
					impegnoRiga.getNumero(),
					Constanti.MOVGEST_TIPO_IMPEGNO);

			List<SubImpegno> listaSubImpegnoRiga=impegnoRiga.getElencoSubImpegni();
			if (listaSubImpegnoRiga!=null && listaSubImpegnoRiga.size()==1) {
				//SubImpegno
				SubImpegno subImpegnoRiga=listaSubImpegnoRiga.get(0);

				String codiceSubImpegnoRiga = subImpegnoRiga.getNumero().toString();
				List<SiacTMovgestTsFin> listaSiacTMovgestTs = siacTMovgestTsRepository.findSubMovgestTsByCodeAndMovgestId(idEnte, getNow(), siacTMovgest.getUid(), codiceSubImpegnoRiga);
				if (listaSiacTMovgestTs != null && listaSiacTMovgestTs.size() > 0) {
					SiacTMovgestTsFin siacTMovgestTsSubImpegno = listaSiacTMovgestTs.get(0);

					SiacRCartacontDetMovgestTFin siacRCartacontDetMovgestTsSub = new SiacRCartacontDetMovgestTFin();
					siacRCartacontDetMovgestTsSub = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRCartacontDetMovgestTsSub, datiOperazione, siacTAccountRepository);
					siacRCartacontDetMovgestTsSub.setSiacTCartacontDet(siacTCartacontDet);
					siacRCartacontDetMovgestTsSub.setSiacTMovgestT(siacTMovgestTsSubImpegno);
					siacRCartacontDetMovgestTsSub = siacRCartacontDetMovgestTRepository.saveAndFlush(siacRCartacontDetMovgestTsSub);
				}
			} else {
				//Impegno
				List<SiacTMovgestTsFin> siacTMovgestTsList = siacTMovgestTsRepository.findMovgestTsByMovgest(idEnte, getNow(), siacTMovgest.getUid());

				if (siacTMovgestTsList != null && siacTMovgestTsList.size() > 0) {
					SiacTMovgestTsFin siacTMovgestTs = siacTMovgestTsList.get(0);


					SiacRCartacontDetMovgestTFin siacRCartacontDetMovgestTsImp = new SiacRCartacontDetMovgestTFin();
					siacRCartacontDetMovgestTsImp = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRCartacontDetMovgestTsImp, datiOperazione, siacTAccountRepository);
					siacRCartacontDetMovgestTsImp.setSiacTCartacontDet(siacTCartacontDet);
					siacRCartacontDetMovgestTsImp.setSiacTMovgestT(siacTMovgestTs);
					siacRCartacontDetMovgestTsImp = siacRCartacontDetMovgestTRepository.saveAndFlush(siacRCartacontDetMovgestTsImp);
				}
			}
		}
		
		//SubDocumento Spesa
		if (rigaDaInserire.getListaSubDocumentiSpesaCollegati()!=null &&
				rigaDaInserire.getListaSubDocumentiSpesaCollegati().size()>0) {
			for (SubdocumentoSpesa subdocumentoSpesaCollegato : rigaDaInserire.getListaSubDocumentiSpesaCollegati()) {
				inserisciSubDoc(idEnte, subdocumentoSpesaCollegato, siacTCartacontDet, datiOperazione);
			}
		}

	}

	/**
	 * Operazione che cancella un predocumento della carta dal DB.
	 * 
	 * @param rigaDaCancellare
	 * @param datiOperazioneCancella
	 */
	private void cancellaRiga(PreDocumentoCarta rigaDaCancellare, DatiOperazioneDto datiOperazioneCancella) {

		SiacTCartacontDetFin siacTCartacontDetDaCancellare=siacTCartaContDetRepository.findOne(rigaDaCancellare.getIdPreDocumentoCarta());

		if (siacTCartacontDetDaCancellare!=null) {
			List<SiacRCartacontDetMovgestTFin> listaRCartacontDetMovgestTs = siacTCartacontDetDaCancellare.getSiacRCartacontDetMovgestTs();

			if (listaRCartacontDetMovgestTs!=null && listaRCartacontDetMovgestTs.size()>0) {
				for (SiacRCartacontDetMovgestTFin siacRCartacontDetMovgestTsDaCancellare : listaRCartacontDetMovgestTs) {
					DatiOperazioneUtils.cancellaRecord(siacRCartacontDetMovgestTsDaCancellare, siacRCartacontDetMovgestTRepository, datiOperazioneCancella, siacTAccountRepository);
				}
			}

			List<SiacRCartacontDetModpagFin> listaRCartacontDetModpag = siacTCartacontDetDaCancellare.getSiacRCartacontDetModpags();

			if (listaRCartacontDetModpag!=null && listaRCartacontDetModpag.size()>0) {
				for (SiacRCartacontDetModpagFin siacRCartacontDetModpagDaCancellare : listaRCartacontDetModpag) {
					DatiOperazioneUtils.cancellaRecord(siacRCartacontDetModpagDaCancellare, siacRCartacontDetModpagRepository, datiOperazioneCancella, siacTAccountRepository);
				}
			}

			List<SiacRCartacontDetSoggettoFin> listaRCartacontDetSoggetto = siacTCartacontDetDaCancellare.getSiacRCartacontDetSoggettos();

			if (listaRCartacontDetSoggetto!=null && listaRCartacontDetSoggetto.size()>0) {
				for (SiacRCartacontDetSoggettoFin siacRCartacontDetSoggettoDaCancellare : listaRCartacontDetSoggetto) {
					DatiOperazioneUtils.cancellaRecord(siacRCartacontDetSoggettoDaCancellare, siacRCartacontDetSoggettoRepository, datiOperazioneCancella, siacTAccountRepository);
				}
			}
			
			List<SiacRCartacontDetSubdocFin> listaRCartacontDetSubdoc= siacTCartacontDetDaCancellare.getSiacRCartacontDetSubdocs();
			
			if (listaRCartacontDetSubdoc!=null && listaRCartacontDetSubdoc.size()>0) {
				for (SiacRCartacontDetSubdocFin siacRCartacontDetSubdocDaCancellare : listaRCartacontDetSubdoc) {
					DatiOperazioneUtils.cancellaRecord(siacRCartacontDetSubdocDaCancellare, siacRCartacontDetSubdocRepository, datiOperazioneCancella, siacTAccountRepository);
				}
			}

			DatiOperazioneUtils.cancellaRecord(siacTCartacontDetDaCancellare, siacTCartaContDetRepository, datiOperazioneCancella, siacTAccountRepository);
		}
	}
	
	public boolean esisteCartaContabile(RicercaCartaContabileK pk, DatiOperazioneDto datiOperazione) {
		Integer idEnte = datiOperazione.getSiacTEnteProprietario().getEnteProprietarioId();

		SiacTCartacontFin siacTCartacont = cartaContabileDao.ricercaCartaContabile(pk, idEnte, datiOperazione);
		
		return siacTCartacont != null;
	}

	/**
	 * Operazione di ricerca carta contabile per chiave.
	 * Richiamato dal servizio RicercaCartaContabilePerChiaveService.
	 * 
	 * @param pk
	 * @param richiedente
	 * @param ente
	 * @param datiOperazione
	 * @return
	 */
	public CartaContabile ricercaCartaContabile(RicercaCartaContabileK pk, Richiedente richiedente, String codiceAmbito, Ente ente, 
			DatiOperazioneDto datiOperazione, boolean cercaMdpCessionePerChiaveModPag){

		Integer idEnte = datiOperazione.getSiacTEnteProprietario().getEnteProprietarioId();

		CartaContabile cartaContabile = new CartaContabile();

		SiacTCartacontFin siacTCartacont = cartaContabileDao.ricercaCartaContabile(pk, idEnte, datiOperazione);
		// carta contabile : inizio
		cartaContabile = map(siacTCartacont, CartaContabile.class, FinMapId.SiacTCartacont_CartaContabile);
		cartaContabile.setDataCreazioneSupport(cartaContabile.getDataCreazione());


		AttributoTClassInfoDto attributoInfo = new AttributoTClassInfoDto();
		attributoInfo.setTipoOggetto(OggettoDellAttributoTClass.T_CARTACONT);
		attributoInfo.setSiacTCartacont(siacTCartacont);

		//note
		String noteCc = getValoreAttr(attributoInfo, datiOperazione, idEnte, Constanti.T_ATTR_CODE_NOTE_CC);
		cartaContabile.setNote(noteCc);

		//motivo urgenza
		String motivoUrgenza = getValoreAttr(attributoInfo, datiOperazione, idEnte, Constanti.T_ATTR_CODE_MOTIVO_URGENZA);
		cartaContabile.setMotivoUrgenza(motivoUrgenza);
		
		//firma 1
		String firma1 = getValoreAttr(attributoInfo, datiOperazione, idEnte, Constanti.T_ATTR_FIRMA_1_CARTA_CONT);
		cartaContabile.setFirma1(firma1);

		//firma 2
		String firma2 = getValoreAttr(attributoInfo, datiOperazione, idEnte, Constanti.T_ATTR_FIRMA_2_CARTA_CONT);
		cartaContabile.setFirma2(firma2);

		//data trasmissione
		if (StatoOperativoCartaContabile.TRASMESSO.equals(cartaContabile.getStatoOperativoCartaContabile())) {
			cartaContabile.setDataTrasmissione(cartaContabile.getDataStatoOperativo());
		} else {
			cartaContabile.setDataTrasmissione(null);
		}
		// carta contabile : fine

		// pre-documenti carta : inizio
		List<PreDocumentoCarta> elencoPreDocumentiCarta = new ArrayList<PreDocumentoCarta>();
		List<SiacTCartacontDetFin> elencoSiacTCartacontDet = siacTCartacont.getSiacTCartacontDets();
		BigDecimal totaleImportoDaRegolarizzareRigheCarta = BigDecimal.ZERO;
		BigDecimal totaleValutaEsteraRigheCarta = BigDecimal.ZERO;
		if(elencoSiacTCartacontDet!=null && elencoSiacTCartacontDet.size() > 0){
			for(SiacTCartacontDetFin siacTCartacontDet : elencoSiacTCartacontDet){
				if(siacTCartacontDet!=null && siacTCartacontDet.getDataFineValidita()==null){
					PreDocumentoCarta preDocumentoCarta = new PreDocumentoCarta();
					preDocumentoCarta = map(siacTCartacontDet, PreDocumentoCarta.class, FinMapId.SiacTCartacontDet_PreDocumentoCarta);
					preDocumentoCarta.setDataCreazioneSupport(preDocumentoCarta.getDataCreazione());

					attributoInfo = new AttributoTClassInfoDto();
					attributoInfo.setTipoOggetto(OggettoDellAttributoTClass.T_CARTACONT_DET);
					attributoInfo.setSiacTCartacontDet(siacTCartacontDet);

					//note
					String noteCcDet = getValoreAttr(attributoInfo, datiOperazione, idEnte, Constanti.T_ATTR_CODE_NOTE_CC);
					preDocumentoCarta.setNote(noteCcDet);

					// soggetto + eventuale sede secondaria + modalita' di pagamento : inizio
					Soggetto soggettoPreDocumento = new Soggetto();
					SedeSecondariaSoggetto sedeSecondariaPreDocumento = new SedeSecondariaSoggetto();
					String codSoggettoPreDocumento = null;
					boolean isSedeSecondaria = false;

					List<SiacRCartacontDetSoggettoFin> listaSiacRCartacontDetSoggetto = siacTCartacontDet.getSiacRCartacontDetSoggettos();
					if(listaSiacRCartacontDetSoggetto!=null && listaSiacRCartacontDetSoggetto.size() > 0){
						for(SiacRCartacontDetSoggettoFin siacRCartacontDetSoggetto : listaSiacRCartacontDetSoggetto){
							if(siacRCartacontDetSoggetto!=null && siacRCartacontDetSoggetto.getDataFineValidita()==null){
								codSoggettoPreDocumento = siacRCartacontDetSoggetto.getSiacTSoggetto().getSoggettoCode();
								soggettoPreDocumento = soggettoDad.ricercaSoggetto(Constanti.AMBITO_FIN, idEnte, codSoggettoPreDocumento, false, true);
								Integer idSoggetto = siacRCartacontDetSoggetto.getSiacTSoggetto().getSoggettoId();
								if (soggettoDad.isSedeSecondaria(idSoggetto, idEnte)) {
									isSedeSecondaria = true;
									sedeSecondariaPreDocumento = soggettoDad.ricercaSedeSecondariaPerChiave(idEnte, codSoggettoPreDocumento, idSoggetto);
									List<SedeSecondariaSoggetto> listaSediSecondarie = new ArrayList<SedeSecondariaSoggetto>();
									listaSediSecondarie.add(sedeSecondariaPreDocumento);
									soggettoPreDocumento.setSediSecondarie(listaSediSecondarie);
								}
							}
						}
					}

					ModalitaPagamentoSoggetto modalitaPagamentoPreDocumento = new ModalitaPagamentoSoggetto();
					List<SiacRCartacontDetModpagFin> listaSiacRCartacontDetModpag = siacTCartacontDet.getSiacRCartacontDetModpags();
					if(listaSiacRCartacontDetModpag!=null && listaSiacRCartacontDetModpag.size() > 0){
						for(SiacRCartacontDetModpagFin siacRCartacontDetModpag : listaSiacRCartacontDetModpag){
							if(siacRCartacontDetModpag!=null && siacRCartacontDetModpag.getDataFineValidita()==null){
								List<ModalitaPagamentoSoggetto> listaModalitaPagamento = soggettoDad
										.ricercaModalitaPagamentoPerChiave(codiceAmbito, idEnte,
												codSoggettoPreDocumento,
												siacRCartacontDetModpag.getSiacTModpag().getModpagId(), null, cercaMdpCessionePerChiaveModPag);
								if(listaModalitaPagamento!=null && listaModalitaPagamento.size() > 0){
									modalitaPagamentoPreDocumento = listaModalitaPagamento.get(0);
									List<ModalitaPagamentoSoggetto> elencoModalitaPagamento = new ArrayList<ModalitaPagamentoSoggetto>();
									elencoModalitaPagamento.add(modalitaPagamentoPreDocumento);
									if(isSedeSecondaria){
										soggettoPreDocumento.getSediSecondarie().get(0).setModalitaPagamentoSoggettos(elencoModalitaPagamento);
									} else {
										soggettoPreDocumento.setModalitaPagamentoList(elencoModalitaPagamento);
									}												
								}
							}
						}
					}

					preDocumentoCarta.setModalitaPagamentoSoggetto(modalitaPagamentoPreDocumento);
					preDocumentoCarta.setSedeSecondariaSoggetto(sedeSecondariaPreDocumento);
					preDocumentoCarta.setSoggetto(soggettoPreDocumento);
					// soggetto + eventuale sede secondaria + modalita' di pagamento : fine

					List<SiacRCartacontDetMovgestTFin> listaSiacRCartacontDetMovgestT = siacTCartacontDet.getSiacRCartacontDetMovgestTs();
					
					Impegno[] impegnoSubImpegno = null;
					
					if(listaSiacRCartacontDetMovgestT!=null && listaSiacRCartacontDetMovgestT.size() > 0){
						for(SiacRCartacontDetMovgestTFin siacRCartacontDetMovgestT : listaSiacRCartacontDetMovgestT){
							if(siacRCartacontDetMovgestT!=null && siacRCartacontDetMovgestT.getDataFineValidita()==null){

								SiacTMovgestTsFin siacTMovgestT = siacRCartacontDetMovgestT.getSiacTMovgestT();
								
								impegnoSubImpegno = leggiImpegnoSubImpegno(siacTMovgestT, richiedente, ente);
							}
						}
					}
					
					// SIAC-6198 se l'impegno (o sub-impegno) non c'e' allora potrebbe essere legato al subdoc 
					if (impegnoSubImpegno == null) {
						SiacTMovgestTsFin siacTMovgestTsFin = leggiImpegnoSubimpegnoDaSubdoc(siacTCartacontDet);
						
						if (siacTMovgestTsFin != null) {
							impegnoSubImpegno = leggiImpegnoSubImpegno(siacTMovgestTsFin, richiedente, ente);
						}
					}
						
					if (impegnoSubImpegno != null) {
						preDocumentoCarta.setImpegno(impegnoSubImpegno[0]);
						preDocumentoCarta.setSubImpegno((SubImpegno) impegnoSubImpegno[1]);
					}
					
					// impegno + eventuale sub-impegno : fine

					totaleImportoDaRegolarizzareRigheCarta = totaleImportoDaRegolarizzareRigheCarta.add(preDocumentoCarta.getImportoDaRegolarizzare());
					if (preDocumentoCarta.getImportoValutaEstera()!=null) {
						totaleValutaEsteraRigheCarta = totaleValutaEsteraRigheCarta.add(preDocumentoCarta.getImportoValutaEstera()); 
					}

					elencoPreDocumentiCarta.add(preDocumentoCarta);
				}
			}				
		}

		// Ordino per numero crescente la lista do PreDocumentiCarta
		if(elencoPreDocumentiCarta != null && elencoPreDocumentiCarta.size()>0){
			Collections.sort(elencoPreDocumentiCarta, new PreDocumentoCartaComparator());
		}
		
		cartaContabile.setListaPreDocumentiCarta(elencoPreDocumentiCarta);
		// pre-documenti carta : fine

		// carta contabile estera : inizio
		List<CartaEstera> elencoCarteContabiliEstere = new ArrayList<CartaEstera>();
		List<SiacTCartacontEsteraFin> elencoSiacTCartacontEstera = siacTCartacont.getSiacTCartacontEsteras();
		if(null!=elencoSiacTCartacontEstera && elencoSiacTCartacontEstera.size() > 0){
			for(SiacTCartacontEsteraFin siacTCartacontEstera : elencoSiacTCartacontEstera){
				if(siacTCartacontEstera!=null && siacTCartacontEstera.getDataFineValidita()==null){

					CartaEstera cartaEstera = new CartaEstera();
					cartaEstera = map(siacTCartacontEstera, CartaEstera.class, FinMapId.SiacTCartacontEstera_CartaEstera);

					cartaEstera.setTotaleValutaEstera(totaleValutaEsteraRigheCarta);

					attributoInfo = new AttributoTClassInfoDto();
					attributoInfo.setTipoOggetto(OggettoDellAttributoTClass.T_CARTACONT_ESTERA);
					attributoInfo.setSiacTCartacontEstera(siacTCartacontEstera);

					//tipo pagamento
					String isAssegno = getValoreAttr(attributoInfo, datiOperazione, idEnte, Constanti.T_ATTR_CODE_MODALITA_ACCREDITO_ASSEGNO);
					String isBonifico = getValoreAttr(attributoInfo, datiOperazione, idEnte, Constanti.T_ATTR_CODE_MODALITA_ACCREDITO_BONIFICO);
					String isAltro = getValoreAttr(attributoInfo, datiOperazione, idEnte, Constanti.T_ATTR_CODE_MODALITA_ACCREDITO_ALTRO);
					String isSpedizione = getValoreAttr(attributoInfo, datiOperazione, idEnte, Constanti.T_ATTR_CODE_RECAPITOASSEGNO_SPEDIZIONE);
					String isConsegna = getValoreAttr(attributoInfo, datiOperazione, idEnte, Constanti.T_ATTR_CODE_RECAPITOASSEGNO_CONSEGNA);

					if ("S".equalsIgnoreCase(isAssegno)) {
						cartaEstera.setTipologiaPagamento("Assegno");
						if ("S".equalsIgnoreCase(isSpedizione)) {
							cartaEstera.setMetodoConsegna("Da spedire");
						} else if ("S".equalsIgnoreCase(isConsegna)) {
							cartaEstera.setMetodoConsegna("Da consegnare");
						}
					} else if ("S".equalsIgnoreCase(isBonifico)) {
						cartaEstera.setTipologiaPagamento("Bonifico");
					} else if ("S".equalsIgnoreCase(isAltro)) {
						cartaEstera.setTipologiaPagamento("Altro");
					}

					elencoCarteContabiliEstere.add(cartaEstera);
				}
			}
		}
		cartaContabile.setListaCarteEstere(elencoCarteContabiliEstere);
		// carta contabile estera : fine

		cartaContabile.setImportoDaRegolarizzare(totaleImportoDaRegolarizzareRigheCarta);
		//Termino restituendo l'oggetto di ritorno: 
        return cartaContabile;
	}

	private SiacTMovgestTsFin leggiImpegnoSubimpegnoDaSubdoc(SiacTCartacontDetFin siacTCartacontDet) {

		List<SiacRCartacontDetSubdocFin> siacRCartacontDetSubdocFins = siacTCartacontDet.getSiacRCartacontDetSubdocs();
		if (siacRCartacontDetSubdocFins != null) {
			for (SiacRCartacontDetSubdocFin siacRCartacontDetSubdocFin : siacRCartacontDetSubdocFins) {
				SiacTSubdocFin siacTSubdocFin = siacRCartacontDetSubdocFin.getSiacTSubdoc();
				if (siacTSubdocFin != null) {
					List<SiacRSubdocMovgestTFin> siacRSubdocMovgestTFins = siacTSubdocFin.getSiacRSubdocMovgestTs();
					if (siacRSubdocMovgestTFins != null) {
						for (SiacRSubdocMovgestTFin siacRSubdocMovgestTFin : siacRSubdocMovgestTFins) {
							if (siacRSubdocMovgestTFin.getSiacTMovgestT() != null) {
								return siacRSubdocMovgestTFin.getSiacTMovgestT();
							}
						}
					}
				}
			}
		}

		return null;
	}

	private Impegno[] leggiImpegnoSubImpegno(SiacTMovgestTsFin siacTMovgestT, Richiedente richiedente, Ente ente) {
																			
		Impegno impegnoPreDocumento = null;
		SubImpegno subImpegnoPreDocumento = null;

		String movgestTsTipoCode = siacTMovgestT.getSiacDMovgestTsTipo().getMovgestTsTipoCode();

		String annoBilancio = siacTMovgestT.getSiacTMovgest().getSiacTBil().getSiacTPeriodo().getAnno();
		Integer annoImpegno = siacTMovgestT.getSiacTMovgest().getMovgestAnno();
		BigDecimal numeroImpegno = siacTMovgestT.getSiacTMovgest().getMovgestNumero();

		PaginazioneSubMovimentiDto paginazioneSubMovimentiDto = new PaginazioneSubMovimentiDto();
		if (movgestTsTipoCode.equals(Constanti.MOVGEST_TS_TIPO_SUBIMPEGNO)) {
			//SUB IMPEGNO
			String numeroSub = siacTMovgestT.getMovgestTsCode();
			paginazioneSubMovimentiDto.setNoSub(false);
			paginazioneSubMovimentiDto.setNumeroSubMovimentoRichiesto(new BigDecimal(numeroSub));
		} else {
			//IMPEGNO NO SUB IMPEGNI
			paginazioneSubMovimentiDto.setNumeroSubMovimentoRichiesto(null);
			paginazioneSubMovimentiDto.setNoSub(true);
		}
		
		EsitoRicercaMovimentoPkDto esitoRicercaMov = impegnoOttimizzatoDad.ricercaMovimentoPk(
				richiedente, ente, annoBilancio, annoImpegno, numeroImpegno,
				paginazioneSubMovimentiDto, null, Constanti.MOVGEST_TIPO_IMPEGNO, true);
		
		if (esitoRicercaMov != null) {
			impegnoPreDocumento = (Impegno) esitoRicercaMov.getMovimentoGestione();
		}
		
		if(movgestTsTipoCode.equals(Constanti.MOVGEST_TS_TIPO_TESTATA)) {
			// il pre-documento e' legato ad un impegno
				// Jira-1370
			subImpegnoPreDocumento = null;
			
		} else if (movgestTsTipoCode.equals(Constanti.MOVGEST_TS_TIPO_SUBIMPEGNO)) {
			// il pre-documento e' legato ad un sub-impegno
			Integer idSubImpegno = siacTMovgestT.getMovgestTsId();

			List<SubImpegno> listSubEstratti = impegnoPreDocumento.getElencoSubImpegni();

			for (SubImpegno subImpegnoIterato : listSubEstratti){
				if (subImpegnoIterato.getUid() == idSubImpegno) {
					subImpegnoPreDocumento = subImpegnoIterato;
				}
			}

								// 16/10/2014 : commento temporaneamente
								// List<SubImpegno> listaSub = new ArrayList<SubImpegno>();
								// listaSub.add(subImpegnoPreDocumento);
																	
								 //preDocumentoCarta.setSubImpegno(subImpegnoPreDocumento);
								// impegnoPreDocumento.setElencoSubImpegni(listaSub);
								// preDocumentoCarta.setImpegno(impegnoPreDocumento);
		}
		
		return impegnoPreDocumento == null && subImpegnoPreDocumento == null ? null :
				new Impegno[]{impegnoPreDocumento, subImpegnoPreDocumento};
	}
	
	
	
	/**
	 * 
	 * Metodo specifico per il servizio CollegaQuotaDocumentoARigaCartaService.
	 * 
	 * Si occupa di caricare i soli dati minimi della carta contabile necessari all'esecuzione
	 * di tale servizio.
	 * 
	 * Oltre al caricamento effettua anche i controlli di merito (sempre specifici per il servizio in questione)
	 * che dipendono solo dalla carta (esistenza, stato accettabile, che non sia collegata a dei movimenti, ecc).
	 * 
	 * @param numeroCarta
	 * @param numeroRiga
	 * @param bilancio
	 * @param richiedente
	 * @param codiceAmbito
	 * @param datiOperazione
	 * @return
	 */
	public CollegaQuotaDocumentoARigaCartaInfoDto caricaDatiCartaPerCollegaQuotaDocumentoARigaCarta(
			Integer numeroCarta,Integer numeroRiga,
			Bilancio bilancio,
			Richiedente richiedente, String codiceAmbito, 
			DatiOperazioneDto datiOperazione){

		CollegaQuotaDocumentoARigaCartaInfoDto esito = new CollegaQuotaDocumentoARigaCartaInfoDto();
		
		Integer idEnte = datiOperazione.getSiacTEnteProprietario().getEnteProprietarioId();

		CartaContabile cartaContabile = new CartaContabile();
		
		RicercaCartaContabileK pk = new RicercaCartaContabileK();
		pk.setCartaContabile(new CartaContabile());
		pk.getCartaContabile().setNumero(numeroCarta);
		pk.setBilancio(bilancio);

		SiacTCartacontFin siacTCartacont = cartaContabileDao.ricercaCartaContabile(pk, idEnte, datiOperazione);
		
		if(siacTCartacont==null){
			//carta contabile inesistente
			String messaggio = numeroCarta + " per Bilancio: " + ((Integer)pk.getBilancio().getAnno()).toString();
			esito.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Carta Contabile", messaggio));
			return esito;
		}
		
		// carta contabile map dati minimi:
		cartaContabile = map(siacTCartacont, CartaContabile.class, FinMapId.SiacTCartacont_CartaContabile);
		cartaContabile.setDataCreazioneSupport(cartaContabile.getDataCreazione());
		//

		//STATO CARTA:
		if (StatoOperativoCartaContabile.TRASMESSO.equals(cartaContabile.getStatoOperativoCartaContabile())) {
			cartaContabile.setDataTrasmissione(cartaContabile.getDataStatoOperativo());
		} else {
			esito.addErrore(ErroreFin.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("Carta contabile" + " " +numeroCarta," non trasmesso"));
			return esito;
		}
		// carta contabile : fine

		//CARICHIAMO LA RIGA RICHIESTA:
		List<SiacTCartacontDetFin> elencoSiacTCartacontDet = siacTCartacont.getSiacTCartacontDets();
		SiacTCartacontDetFin rigaRichiesta = getByNumeroRiga(elencoSiacTCartacontDet, numeroRiga);
		
		if(rigaRichiesta==null || !CommonUtils.isValidoSiacTBase(rigaRichiesta, datiOperazione.getTs())){
			//riga indicata inesistete oppure invalidata:
			String messaggio = rigaRichiesta + " per la carta " + numeroCarta;
			esito.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Riga Carta Contabile", messaggio));
			return esito;
		}
		
		PreDocumentoCarta preDocumentoCarta = new PreDocumentoCarta();
		preDocumentoCarta = map(rigaRichiesta, PreDocumentoCarta.class, FinMapId.SiacTCartacontDet_PreDocumentoCarta);
		preDocumentoCarta.setDataCreazioneSupport(preDocumentoCarta.getDataCreazione());

		//SOGGETTO:
		List<SiacRCartacontDetSoggettoFin> listaSiacRCartacontDetSoggetto = rigaRichiesta.getSiacRCartacontDetSoggettos();
		SiacRCartacontDetSoggettoFin relazioneValidaConSoggetto = CommonUtils.getValidoSiacTBase(listaSiacRCartacontDetSoggetto, datiOperazione.getTs());
		if(relazioneValidaConSoggetto!=null){
			String codSoggettoRiga = relazioneValidaConSoggetto.getSiacTSoggetto().getSoggettoCode();
			Soggetto soggettoRiga = new Soggetto();
			soggettoRiga.setCodiceSoggetto(codSoggettoRiga);
			preDocumentoCarta.setSoggetto(soggettoRiga);
		} else {
			String messaggio = rigaRichiesta + " priva di Soggetto ";
			esito.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Riga Carta Contabile", messaggio));
			return esito;
		}

		// IMPEGNO + EVENTUALE SUB-IMPEGNO
		List<SiacRCartacontDetMovgestTFin> listaSiacRCartacontDetMovgestT = rigaRichiesta.getSiacRCartacontDetMovgestTs();
		SiacRCartacontDetMovgestTFin relazioneValidaVersoImpegni = CommonUtils.getValidoSiacTBase(listaSiacRCartacontDetMovgestT, datiOperazione.getTs());
		if(relazioneValidaVersoImpegni!=null){
			//Non puo' avere impegni
			esito.addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore("Riga carta", "La riga indicata e' gia' collegata ad un movimento"));
			return esito;
		}

		//Ritorno i dati caricati senza essermi imbattuto in errori bloccanti:
		esito.setCartaContabile(cartaContabile);
		esito.setPreDocumentoCarta(preDocumentoCarta);
        return esito;
	}

	// Classe implementata per ordinare la lista dei PreDocumentiCarta
	class PreDocumentoCartaComparator implements Comparator<PreDocumentoCarta> {
		@Override
		public int compare(PreDocumentoCarta objToCampareUno, PreDocumentoCarta objToCampareDue) {
				if(objToCampareUno!=null && objToCampareUno.getNumero()!=null && objToCampareDue!=null && objToCampareDue.getNumero()!=null){
					//da confrontare con date creazione
					return objToCampareUno.getNumero().compareTo(objToCampareDue.getNumero());
				} else {
		        return -1;
		    }
		}
	}
	
	/**
	 * Metodo che ritorna un oggetto CartaContabile popolato con i dati di una SiacTCartacontFin estratta dal database.
	 * 
	 * @param richiedente
	 * @param ente
	 * @param siacTCartacont
	 * @return
	 */
	private CartaContabile creaCarta(Richiedente richiedente,String codiceAmbito,  Ente ente, SiacTCartacontFin siacTCartacont) {

		Integer idEnte=ente.getUid();

		// carta contabile : inizio
		CartaContabile cartaContabile = new CartaContabile();
		cartaContabile = map(siacTCartacont, CartaContabile.class, FinMapId.SiacTCartacont_CartaContabile);
		// carta contabile : fine

		// pre-documenti carta : inizio
		List<PreDocumentoCarta> elencoPreDocumentiCarta = new ArrayList<PreDocumentoCarta>();
		List<SiacTCartacontDetFin> elencoSiacTCartacontDet = siacTCartacont.getSiacTCartacontDets();
		BigDecimal totaleImportoDaRegolarizzareRigheCarta = BigDecimal.ZERO;
		BigDecimal totaleValutaEsteraRigheCarta = BigDecimal.ZERO;
		if(elencoSiacTCartacontDet!=null && elencoSiacTCartacontDet.size() > 0){
			for(SiacTCartacontDetFin siacTCartacontDet : elencoSiacTCartacontDet){
				entityRefresh(siacTCartacontDet);
				
				if(siacTCartacontDet!=null && siacTCartacontDet.getDataFineValidita()==null){
					PreDocumentoCarta preDocumentoCarta = new PreDocumentoCarta();
					preDocumentoCarta = map(siacTCartacontDet, PreDocumentoCarta.class, FinMapId.SiacTCartacontDet_PreDocumentoCarta);

					// soggetto + eventuale sede secondaria + modalita' di pagamento : inizio
					Soggetto soggettoPreDocumento = new Soggetto();
					SedeSecondariaSoggetto sedeSecondariaPreDocumento = new SedeSecondariaSoggetto();
					String codSoggettoPreDocumento = null;
					boolean isSedeSecondaria = false;

					List<SiacRCartacontDetSoggettoFin> listaSiacRCartacontDetSoggetto = siacTCartacontDet.getSiacRCartacontDetSoggettos();
					if(listaSiacRCartacontDetSoggetto!=null && listaSiacRCartacontDetSoggetto.size() > 0){
						for(SiacRCartacontDetSoggettoFin siacRCartacontDetSoggetto : listaSiacRCartacontDetSoggetto){
							if(siacRCartacontDetSoggetto!=null && siacRCartacontDetSoggetto.getDataFineValidita()==null){
								codSoggettoPreDocumento = siacRCartacontDetSoggetto.getSiacTSoggetto().getSoggettoCode();
								soggettoPreDocumento = soggettoDad.ricercaSoggetto(Constanti.AMBITO_FIN, idEnte, codSoggettoPreDocumento, false, true);
								Integer idSoggetto = siacRCartacontDetSoggetto.getSiacTSoggetto().getSoggettoId();
								if (soggettoDad.isSedeSecondaria(idSoggetto, idEnte)) {
									isSedeSecondaria = true;
									sedeSecondariaPreDocumento = soggettoDad.ricercaSedeSecondariaPerChiave(idEnte, codSoggettoPreDocumento, idSoggetto);
									List<SedeSecondariaSoggetto> listaSediSecondarie = new ArrayList<SedeSecondariaSoggetto>();
									listaSediSecondarie.add(sedeSecondariaPreDocumento);
									soggettoPreDocumento.setSediSecondarie(listaSediSecondarie);
								}
							}
						}
					}

					ModalitaPagamentoSoggetto modalitaPagamentoPreDocumento = new ModalitaPagamentoSoggetto();
					List<SiacRCartacontDetModpagFin> listaSiacRCartacontDetModpag = siacTCartacontDet.getSiacRCartacontDetModpags();
					if(listaSiacRCartacontDetModpag!=null && listaSiacRCartacontDetModpag.size() > 0){
						for(SiacRCartacontDetModpagFin siacRCartacontDetModpag : listaSiacRCartacontDetModpag){
							if(siacRCartacontDetModpag!=null && siacRCartacontDetModpag.getDataFineValidita()==null){
								List<ModalitaPagamentoSoggetto> listaModalitaPagamento = soggettoDad
										.ricercaModalitaPagamentoPerChiave(codiceAmbito, idEnte,
												codSoggettoPreDocumento,
												siacRCartacontDetModpag.getSiacTModpag().getModpagId(), null, true);
								if(listaModalitaPagamento!=null && listaModalitaPagamento.size() > 0){
									modalitaPagamentoPreDocumento = listaModalitaPagamento.get(0);
									List<ModalitaPagamentoSoggetto> elencoModalitaPagamento = new ArrayList<ModalitaPagamentoSoggetto>();
									elencoModalitaPagamento.add(modalitaPagamentoPreDocumento);
									if(isSedeSecondaria){
										soggettoPreDocumento.getSediSecondarie().get(0).setModalitaPagamentoSoggettos(elencoModalitaPagamento);
									} else {
										soggettoPreDocumento.setModalitaPagamentoList(elencoModalitaPagamento);
									}												
								}
							}
						}
					}

					preDocumentoCarta.setModalitaPagamentoSoggetto(modalitaPagamentoPreDocumento);
					preDocumentoCarta.setSedeSecondariaSoggetto(sedeSecondariaPreDocumento);
					preDocumentoCarta.setSoggetto(soggettoPreDocumento);
					// soggetto + eventuale sede secondaria + modalita' di pagamento : fine

					// impegno + eventuale sub-impegno : inizio
					Impegno impegnoPreDocumento = new Impegno();
					SubImpegno subImpegnoPreDocumento = new SubImpegno();

					List<SiacRCartacontDetMovgestTFin> listaSiacRCartacontDetMovgestT = siacTCartacontDet.getSiacRCartacontDetMovgestTs();
					if(listaSiacRCartacontDetMovgestT!=null && listaSiacRCartacontDetMovgestT.size() > 0){
						for(SiacRCartacontDetMovgestTFin siacRCartacontDetMovgestT : listaSiacRCartacontDetMovgestT){
							if(siacRCartacontDetMovgestT!=null && siacRCartacontDetMovgestT.getDataFineValidita()==null){

								String movgestTsTipoCode = siacRCartacontDetMovgestT.getSiacTMovgestT().getSiacDMovgestTsTipo().getMovgestTsTipoCode();

								String annoBilancio = siacRCartacontDetMovgestT.getSiacTMovgestT().getSiacTMovgest().getSiacTBil().getSiacTPeriodo().getAnno();
								Integer annoImpegno = siacRCartacontDetMovgestT.getSiacTMovgestT().getSiacTMovgest().getMovgestAnno();
								BigDecimal numeroImpegno = siacRCartacontDetMovgestT.getSiacTMovgestT().getSiacTMovgest().getMovgestNumero();

								
								PaginazioneSubMovimentiDto paginazioneSubMovimentiDto = new PaginazioneSubMovimentiDto();
								if (movgestTsTipoCode.equals(Constanti.MOVGEST_TS_TIPO_SUBIMPEGNO)) {
									//SUB IMPEGNO
									String numeroSub = siacRCartacontDetMovgestT.getSiacTMovgestT().getMovgestTsCode();
									paginazioneSubMovimentiDto.setNoSub(false);
									paginazioneSubMovimentiDto.setNumeroSubMovimentoRichiesto(new BigDecimal(numeroSub));
								} else {
									//IMPEGNO NO SUB IMPEGNI
									paginazioneSubMovimentiDto.setNumeroSubMovimentoRichiesto(null);
									paginazioneSubMovimentiDto.setNoSub(true);
								}
								
								EsitoRicercaMovimentoPkDto esitoRicercaMov = impegnoOttimizzatoDad.ricercaMovimentoPk(richiedente, ente, annoBilancio, annoImpegno, numeroImpegno,paginazioneSubMovimentiDto,null, Constanti.MOVGEST_TIPO_IMPEGNO, true);
								
								if(esitoRicercaMov!=null){
									impegnoPreDocumento = (Impegno) esitoRicercaMov.getMovimentoGestione();
								}
								
								if(movgestTsTipoCode.equals(Constanti.MOVGEST_TS_TIPO_TESTATA)) {
									// il pre-documento e' legato ad un impegno

									preDocumentoCarta.setImpegno(impegnoPreDocumento);

								} else if (movgestTsTipoCode.equals(Constanti.MOVGEST_TS_TIPO_SUBIMPEGNO)) {
									// il pre-documento e' legato ad un sub-impegno
									Integer idSubImpegno = siacRCartacontDetMovgestT.getSiacTMovgestT().getMovgestTsId();

									List<SubImpegno> listSubEstratti = impegnoPreDocumento.getElencoSubImpegni();

									for (SubImpegno subImpegnoIterato : listSubEstratti){
										if (subImpegnoIterato.getUid()==idSubImpegno) {
											subImpegnoPreDocumento = subImpegnoIterato;
										}
									}

									preDocumentoCarta.setSubImpegno(subImpegnoPreDocumento);
									
									List<SubImpegno> listaSub=new ArrayList<SubImpegno>();
									listaSub.add(subImpegnoPreDocumento);
									impegnoPreDocumento.setElencoSubImpegni(listaSub);
									
									preDocumentoCarta.setImpegno(impegnoPreDocumento);
								}
							}
						}
					}
					// impegno + eventuale sub-impegno : fine

					totaleImportoDaRegolarizzareRigheCarta = totaleImportoDaRegolarizzareRigheCarta.add(preDocumentoCarta.getImportoDaRegolarizzare());

					if (preDocumentoCarta.getImportoValutaEstera()!=null) {
						totaleValutaEsteraRigheCarta = totaleValutaEsteraRigheCarta.add(preDocumentoCarta.getImportoValutaEstera()); 
					}

					elencoPreDocumentiCarta.add(preDocumentoCarta);
				}
			}
		}

		cartaContabile.setListaPreDocumentiCarta(elencoPreDocumentiCarta);
		// pre-documenti carta : fine

		// carta contabile estera : inizio
		List<CartaEstera> elencoCarteContabiliEstere = new ArrayList<CartaEstera>();
		List<SiacTCartacontEsteraFin> elencoSiacTCartacontEstera = siacTCartacont.getSiacTCartacontEsteras();
		if(null!=elencoSiacTCartacontEstera && elencoSiacTCartacontEstera.size() > 0){
			for(SiacTCartacontEsteraFin siacTCartacontEstera : elencoSiacTCartacontEstera){
				CartaEstera cartaEstera = new CartaEstera();
				cartaEstera = map(siacTCartacontEstera, CartaEstera.class, FinMapId.SiacTCartacontEstera_CartaEstera);

				cartaEstera.setTotaleValutaEstera(totaleValutaEsteraRigheCarta);
				elencoCarteContabiliEstere.add(cartaEstera);
			}
		}
		cartaContabile.setListaCarteEstere(elencoCarteContabiliEstere);
		// carta contabile estera : fine

		cartaContabile.setImportoDaRegolarizzare(totaleImportoDaRegolarizzareRigheCarta);


		//Termino restituendo l'oggetto di ritorno: 
        return cartaContabile;
	}
	
	/**
	 * Data una lista di id di sub documenti di spesa restiutuisce, tra essi, solo
	 * quelli che NON hanno collegamenti con carta
	 * @param subdocIdList
	 * @return
	 */
	public List<Integer> soloSubDocNonCollegatiACarte(List<Integer> subdocIdList){
		return cartaContabileDao.soloSubDocNonCollegatiACarte(subdocIdList);
	}
	
	/**
	 * Ritorna true se il sub documento indicato e' collegato a carte
	 * @param subdocId
	 * @param datiOperazione
	 * @return
	 */
	public boolean isCollegatoACarta(Integer subdocId, DatiOperazioneDto datiOperazione){
		List<SiacRCartacontDetSoggettoFin> collegamentiValidi = siacRCartacontDetSubdocRepository.findValidoBySubdocId(subdocId, datiOperazione.getTs());
		return !isEmpty(collegamentiValidi);
	}
	
}