/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.rendiconto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.base.HelperExecutor;
import it.csi.siac.siacbilser.business.service.stampa.base.JAXBBaseReportHandler;
import it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.rendiconto.model.StampaRendicontoCassaCapitoloMovimenti;
import it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.rendiconto.model.StampaRendicontoCassaIntestazione;
import it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.rendiconto.model.StampaRendicontoCassaMovimento;
import it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.rendiconto.model.StampaRendicontoCassaOperazioneCassa;
import it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.rendiconto.model.StampaRendicontoCassaReportModel;
import it.csi.siac.siacbilser.business.utility.DummyMapper;
import it.csi.siac.siacbilser.integration.dad.CassaEconomaleDad;
import it.csi.siac.siacbilser.integration.dad.MovimentoDad;
import it.csi.siac.siacbilser.integration.dad.OperazioneDiCassaDad;
import it.csi.siac.siacbilser.integration.dad.AttoAmministrativoDad;
import it.csi.siac.siacbilser.integration.dad.StampeCassaFileDad;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.exception.HelperException;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siaccecser.model.OperazioneCassa;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.StampaRendiconto;
import it.csi.siac.siaccecser.model.StampeCassaFile;
import it.csi.siac.siaccecser.model.StatoOperativoOperazioneCassa;
import it.csi.siac.siaccecser.model.StatoOperativoRichiestaEconomale;
import it.csi.siac.siaccecser.model.TipoDocumento;
import it.csi.siac.siaccecser.model.TipoOperazioneCassa;
import it.csi.siac.siaccecser.model.TipoStampa;
import it.csi.siac.siaccecser.model.TipologiaOperazioneCassa;
import it.csi.siac.siaccorser.frontend.webservice.msg.report.GeneraReportResponse;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StampaRendicontoCassaReportHandler extends JAXBBaseReportHandler<StampaRendicontoCassaReportModel> {
	
	//Costante per la verifica sul tipo di richiesta
	private static final String CODIFICA_TIPO_RICHIESTA_ECONOMALE_PAGAMENTO_FATTURE = "PAGAMENTO_FATTURE";
	private static final String CODIFICA_TIPO_RICHIESTA_ECONOMALE_ANTICIPO_SPESE_MISSIONE = "ANTICIPO_SPESE_MISSIONE";
	private static final String CODIFICA_TIPO_RICHIESTA_ECONOMALE_ANTICIPO_SPESE_PER_TRASFERTA = "ANTICIPO_TRASFERTA_DIPENDENTI";

	//DADs
	@Autowired
	private CassaEconomaleDad cassaEconomaleDad;
	@Autowired
	private OperazioneDiCassaDad operazioneDiCassaDad;
	@Autowired
	private MovimentoDad movimentoDad;
	@Autowired 
	private StampeCassaFileDad stampeCassaFileDad;
	@Autowired
	private AttoAmministrativoDad attoAmministrativoDad;
	@Autowired
	private HelperExecutor helperExecutor;
	
	private CassaEconomale cassaEconomale;
	private StampeCassaFile datiStampaUltimoRendiconto;
	
	private Date dataStampaRendiconto;
	private Integer numeroRendiconto; //nuovo numero
	//TODO Ricalcolare ultimi
	private Date periodoInizio;
	private Date periodoFine;	
	private TipoStampa tipoStampa;

	private Bilancio bilancio;

//	private boolean isCassaMista;

	private boolean isGestioneMultipla;
	private BigDecimal importoTotaleOperazioniCassa = BigDecimal.ZERO;
	
	private List<OperazioneCassa> listaOperazioneDiCassa = new ArrayList<OperazioneCassa>();
	private List<Movimento> listaMovimento =new ArrayList<Movimento>();
	
	private List<StampaRendicontoCassaOperazioneCassa> listaOperazioneDiCassaPerStampa = new ArrayList<StampaRendicontoCassaOperazioneCassa>();
	private List <StampaRendicontoCassaCapitoloMovimenti> listaStampaRendicontoCassaCapitoloMovimenti = new ArrayList<StampaRendicontoCassaCapitoloMovimenti>();
	
	protected Integer primaPaginaDaStampare;
	
	// SIAC-4799
	private Soggetto soggetto;
	private ModalitaPagamentoSoggetto modalitaPagamentoSoggetto;
	private String causale;
	private StrutturaAmministrativoContabile strutturaAmministrativoContabile;
	private Boolean anticipiSpesaDaInserire;
	
	private StampaRendicontoCassaAllegatoAttoHelper allegatoAttoHelper;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT)
	public void elaborate() {	
		super.elaborate();
	}
	
	@Override
	protected void elaborateData() {
		final String methodName = "elaborateData";
		log.debug(methodName, "Inizio elaborazione Report ");
		cassaEconomaleDad.setEnte(ente);
		cassaEconomale = cassaEconomaleDad.ricercaDettaglioCassaEconomale(cassaEconomale.getUid());
		result.setCassaEconomale(cassaEconomale);
		log.debug(methodName, "TipoCassa : " + cassaEconomale.getTipoDiCassa().getDescrizione());
		Long numeroCassePerEnte = cassaEconomaleDad.contaCassaEconomalePerEnte(bilancio);
		isGestioneMultipla = numeroCassePerEnte > 0;
		log.debug(methodName, "Ente gestione multipla : " + isGestioneMultipla);
		
		setDatiStampaUltimoRendiconto(caricaDatiUltimoRendiconto());
		

		log.debug(methodName, "elaborazione intestazione");
		//Intestazione
		StampaRendicontoCassaIntestazione intestazioneStampa = elaboraIntestazione();
		//Sezione 1 dati capitolo
		
		//Movimenti sez 2  - 3
		//recupero i movimenti del periodo ordinati per num movimento
		listaMovimento = movimentoDad.findByPeriodoMovimentoCassaEconId(periodoInizio, periodoFine, cassaEconomale.getUid(), getEnte().getUid(), bilancio.getUid());
		// li separo per capitolo in una Map
		Map<String, List<Movimento>> map = new HashMap<String, List<Movimento>>();
	
		for(Iterator<Movimento> it = listaMovimento.iterator(); it.hasNext();) {
			
			Movimento movimento = it.next();
			String numeroCapitoloArticolo = "";
			if(movimento.getRendicontoRichiesta()!=null){
				numeroCapitoloArticolo=computeChiaveMappaDaRendicontoRichiesta(movimento.getRendicontoRichiesta());// movimento.getRendicontoRichiesta().getImpegno().getCapitoloUscitaGestione().getNumeroCapitolo() + "/" +  movimento.getRendicontoRichiesta().getImpegno().getCapitoloUscitaGestione().getNumeroArticolo();
				StatoOperativoAtti provv = attoAmministrativoDad.findStatoOperativoAttoAmministrativo(movimento.getRendicontoRichiesta().getImpegno().getAttoAmministrativo());
				movimento.getRendicontoRichiesta().getImpegno().getAttoAmministrativo().setStatoOperativo(provv);
			}  else if (movimento.getRichiestaEconomale()!=null) {
				//recupero il capitolo dall'impegno legato alla richiesta
				
				numeroCapitoloArticolo = computeChiaveMappaDaRichiestaEconomale(movimento.getRichiestaEconomale());// movimento.getRichiestaEconomale().getImpegno().getCapitoloUscitaGestione().getNumeroCapitolo() + "/" +  movimento.getRichiestaEconomale().getImpegno().getCapitoloUscitaGestione().getNumeroArticolo();

				//recupero lo stato del provvedimento
				StatoOperativoAtti provv = attoAmministrativoDad.findStatoOperativoAttoAmministrativo(movimento.getRichiestaEconomale().getImpegno().getAttoAmministrativo());
				movimento.getRichiestaEconomale().getImpegno().getAttoAmministrativo().setStatoOperativo(provv);
			} else {
				log.debug(methodName, "Il movimento non ha ne' richiesta ne' rendiconto lo ignoro" + movimento.getUid());
				it.remove();
				continue;
			}
			inserisciMovimentoInMappatura(map, movimento, numeroCapitoloArticolo);
		}
		
		for (List<Movimento> listMovCapitolo: map.values()) {
			StampaRendicontoCassaCapitoloMovimenti capMovimento = new StampaRendicontoCassaCapitoloMovimenti();
			//creo Wrapper per i movimenti
			creoMovimentiPerStampa(capMovimento, listMovCapitolo);
			result.setImportoTotaleMovimentoCapitoliASM(result.getImportoTotaleMovimentoCapitoliASM().add(capMovimento.getImportoTotaleUsciteEntrateASM()));
			result.setImportoTotaleMovimentoCapitoliNOASM(result.getImportoTotaleMovimentoCapitoliNOASM().add(capMovimento.getImportoTotaleUsciteEntrateNOASM()));
			//importoTotaleCapitoli = importoTotaleCapitoli.add(capMovimento.getImportoTotaleUsciteEntrate());
			//segue antecedente LOTTO M
			//importoTotaleCapitoli = importoTotaleCapitoli.add(capMovimento.getImportoParziale());
			//numTotaleMovimentiCapitoli = numTotaleMovimentiCapitoli + capMovimento.getNumMovimentiCapitolo();
			result.setNumeroMovimentiTotaleASM(result.getNumeroMovimentiTotaleASM()+ capMovimento.getNumMovimentiCapitoloASM());
			result.setNumeroMovimentiTotaleNOASM(result.getNumeroMovimentiTotaleNOASM()+ capMovimento.getNumMovimentiCapitoloNOASM());
			Capitolo<?,?> c = null;
			Movimento movimento = listMovCapitolo.get(0);

			if (movimento.getRichiestaEconomale()!=null) {
				//recupero il capitolo dall'impegno legato alla richiesta
				c= movimento.getRichiestaEconomale().getImpegno().getCapitoloUscitaGestione();
				capMovimento.setImpegno(movimento.getRichiestaEconomale().getImpegno() !=null ? movimento.getRichiestaEconomale().getImpegno() : null);
				capMovimento.setSubImpegno(movimento.getRichiestaEconomale().getSubImpegno() !=null ? movimento.getRichiestaEconomale().getSubImpegno() : null);
			
			} else if(movimento.getRendicontoRichiesta()!=null){
				capMovimento.setImpegno(movimento.getRendicontoRichiesta().getImpegno() !=null ? movimento.getRendicontoRichiesta().getImpegno() : null);
				capMovimento.setSubImpegno(movimento.getRendicontoRichiesta().getSubImpegno() !=null ? movimento.getRendicontoRichiesta().getSubImpegno() : null);
				c= movimento.getRendicontoRichiesta().getImpegno().getCapitoloUscitaGestione();
			} 
			
			capMovimento.setCapitolo(c); //B1 B2 B3
			log.debug(methodName, "B1: numero Capitolo :" + capMovimento.getCapitolo().getNumeroCapitolo());
			log.debug(methodName, "B2: numero Articolo :" + capMovimento.getCapitolo().getNumeroArticolo());
			log.debug(methodName, "B3: descrizione Capitolo :" + capMovimento.getCapitolo().getDescrizione());
			
			listaStampaRendicontoCassaCapitoloMovimenti.add(capMovimento);
			
		}
		result.setIntestazione(intestazioneStampa);	
		result.setListaMovimentiPerCapitoli(listaStampaRendicontoCassaCapitoloMovimenti);
		result.setNumeroMovimentiTotale(result.getNumeroMovimentiTotaleASM() + result.getNumeroMovimentiTotaleNOASM());
		log.debug(methodName, "M1: numero totale movimenti conteggiati NO ASM :" + result.getNumeroMovimentiTotaleNOASM());
		log.debug(methodName, "S1: numero totale movimenti conteggiati ASM :" + result.getNumeroMovimentiTotaleASM());
		log.debug(methodName, "T1: numero totale movimenti conteggiati (M1 + S1) :" + result.getNumeroMovimentiTotale());
		
		log.debug(methodName, "N1: Totale complessivo rendiconto NO ASM :" + result.getImportoTotaleMovimentoCapitoliNOASM());
		log.debug(methodName, "S2: Totale complessivo rendiconto ASM :" + result.getImportoTotaleMovimentoCapitoliASM());
		
		
		//Operazioni di cassa sez 4 e 5
		popolaOperazioniCassa(periodoInizio, periodoFine);
		result.setImportoTotaleRendiconto(result.getImportoTotaleMovimentoCapitoliNOASM().add(result.getImportoTotaleOperazioniCassa()));
		log.debug(methodName, "R1: Totale complessivo rendiconto :" + result.getImportoTotaleRendiconto());
		result.setImportoTotaleMovimentoCapitoli(result.getImportoTotaleMovimentoCapitoliASM().add( result.getImportoTotaleRendiconto()));
		log.debug(methodName, "U1: Totale complessivo rendiconto(R1+S2) :" + result.getImportoTotaleMovimentoCapitoli());
	}
	
	/**
	 * 
	 * @param rendicontoRichiesta
	 * @return
	 */
	private String computeChiaveMappaDaRendicontoRichiesta(RendicontoRichiesta rendicontoRichiesta) {
		// movimento.getRendicontoRichiesta().getImpegno().getCapitoloUscitaGestione().getNumeroCapitolo()
		//+ "/" +  movimento.getRendicontoRichiesta().getImpegno().getCapitoloUscitaGestione().getNumeroArticolo();
		Impegno impegno = rendicontoRichiesta.getImpegno();
		CapitoloUscitaGestione cug = impegno != null ? impegno.getCapitoloUscitaGestione() : null;
		
		StringBuilder sb =  new StringBuilder();
		sb.append(cug != null ? cug.getNumeroCapitolo() : "null");
		sb.append("-");
		sb.append(cug != null ? cug.getNumeroArticolo() : "null");
		sb.append("-");
		sb.append(impegno != null ? impegno.getNumeroBigDecimal() : "null");
		sb.append("-");
		sb.append(rendicontoRichiesta.getSubImpegno() !=null ? rendicontoRichiesta.getSubImpegno().getNumeroBigDecimal():"");
		
		return sb.toString();
	}


	/**
	 * 
	 * @param richiestaEconomale
	 * @return
	 */
	private String computeChiaveMappaDaRichiestaEconomale(RichiestaEconomale richiestaEconomale) {
		//movimento.getRichiestaEconomale().getImpegno().getCapitoloUscitaGestione().getNumeroCapitolo() + "/" +  movimento.getRichiestaEconomale().getImpegno().getCapitoloUscitaGestione().getNumeroArticolo();
		Impegno impegno = richiestaEconomale.getImpegno();
		CapitoloUscitaGestione cug = impegno != null ? impegno.getCapitoloUscitaGestione() : null;

		StringBuilder sb =  new StringBuilder();
		sb.append(cug != null ? cug.getNumeroCapitolo() : "null");
		sb.append("-");
		sb.append(cug != null ? cug.getNumeroArticolo() : "null");
		sb.append("-");
		sb.append(impegno != null ? impegno.getNumeroBigDecimal() : "null");
		sb.append("-");
		sb.append(richiestaEconomale.getSubImpegno() !=null ? richiestaEconomale.getSubImpegno().getNumeroBigDecimal():"");
		
		return sb.toString();
	}


	private void creoMovimentiPerStampa(StampaRendicontoCassaCapitoloMovimenti capMovimento, List<Movimento> listaMovimenti){
		final String methodName = "creoMovimentiPerStampa";
		List <StampaRendicontoCassaMovimento> listWrappedMov = new ArrayList<StampaRendicontoCassaMovimento>();
	
		
	//	BigDecimal importoParzialePerCap = BigDecimal.ZERO;
		int numMovCapitoloNOASM = 0;
		int numMovCapitoloASM = 0;
			
		for (Movimento mov : listaMovimenti){
			if (mov!=null) {
				StampaRendicontoCassaMovimento movStampa= new StampaRendicontoCassaMovimento();
				DummyMapper.mapNotNullNotEmpty(mov, movStampa);
				movStampa.setFlagAnticipoSpeseMissione(CODIFICA_TIPO_RICHIESTA_ECONOMALE_ANTICIPO_SPESE_MISSIONE.equals(mov.getRichiestaEconomale().getTipoRichiestaEconomale().getCodice()));
				movStampa.setFlagFattura(CODIFICA_TIPO_RICHIESTA_ECONOMALE_PAGAMENTO_FATTURE.equals(mov.getRichiestaEconomale().getTipoRichiestaEconomale().getCodice()));
				log.debug(methodName, " TIPO RICHIESTA " +  mov.getRichiestaEconomale().getTipoRichiestaEconomale().getCodice());
				
				log.debug(methodName, " C1: Numero Movimento " +  mov.getNumeroMovimento());
				log.debug(methodName, " D1: Data Movimento " +  mov.getDataMovimento());

				if (movStampa.getFlagFattura().booleanValue()) {
					//navigo per i subdocumenti per prelevare i dati che mi servono
					List<SubdocumentoSpesa> listaSubSpesa = mov.getRichiestaEconomale().getSubdocumenti();
					for (SubdocumentoSpesa ss :listaSubSpesa ) {
				
						if (movStampa.getNumeroFattura().isEmpty()) {
							movStampa.setSoggetto(ss.getDocumento().getSoggetto());
							log.debug(methodName, " E1: Soggetto/Beneficiario " +  movStampa.getSoggetto().getDenominazione());
							movStampa.setNumeroFattura(ss.getDocumento().getNumero());
							movStampa.setAnnoFattura(ss.getDocumento().getAnno() !=null ? ss.getDocumento().getAnno().toString() :"");
							log.debug(methodName, " F1:Anno Fattura / Numero Fattura " +  movStampa.getAnnoFattura() +"/"+movStampa.getNumeroFattura());
						}
					
					}
				} else {

					if (mov.getRichiestaEconomale().getSoggetto()!=null ) {
						movStampa.setSoggetto(mov.getRichiestaEconomale().getSoggetto());
						log.debug(methodName, " E1: Soggetto/Beneficiario " + (movStampa.getSoggetto().getDenominazione()!=null ? movStampa.getSoggetto().getDenominazione():""));
					} else {
						Soggetto s = new Soggetto();
						s.setMatricola(mov.getRichiestaEconomale().getMatricola());
						s.setDenominazione(mov.getRichiestaEconomale().getCognome()!= null ? mov.getRichiestaEconomale().getCognome():"");
						movStampa.setSoggetto(s);
						log.debug(methodName, " E1: Soggetto/Beneficiario " + (movStampa.getSoggetto().getDenominazione()!=null ? movStampa.getSoggetto().getDenominazione():""));
					}
				}
				BigDecimal importoMovE = BigDecimal.ZERO;	
				BigDecimal importoMovU = BigDecimal.ZERO;	
				if (mov.getRendicontoRichiesta() != null) { // è rendiconto
					log.debug(methodName, " RENDICONTO " );
					movStampa.setFlagIntegrato(mov.getRendicontoRichiesta().getImportoIntegrato().intValue()>0);
					
					if (!(mov.getRendicontoRichiesta().getImportoIntegrato().intValue()>0)) {
						movStampa.setImportoRigaMovimento(mov.getRendicontoRichiesta().getImportoRestituito());
						log.debug(methodName, " H1: Importo Restituito Rendiconto " + movStampa.getImportoRigaMovimento());
						importoMovE = movStampa.getImportoRigaMovimento();
						//segue vecchio calcolo antecedente LOTTO M
					//	importoParzialePerCap = importoParzialePerCap.add(mov.getRendicontoRichiesta().getImportoRestituito());
					} else {
						
						movStampa.setImportoRigaMovimento(mov.getRendicontoRichiesta().getImportoIntegrato());
						log.debug(methodName, " H1: Importo Integrato Rendiconto " + movStampa.getImportoRigaMovimento());
						//capMovimento.setImportoUscite(capMovimento.getImportoUscite().add(movStampa.getImportoRigaMovimento()));
						importoMovU = movStampa.getImportoRigaMovimento();
						//log.debug(methodName, " Parziale Uscite " + capMovimento.getImportoUscite());
					}
				} else {
					log.debug(methodName, " RICHIESTA" );
					movStampa.setImportoRigaMovimento(mov.getRichiestaEconomale().getImporto());
					log.debug(methodName, " H1: Importo Richiesta " + mov.getRichiestaEconomale().getImporto());
					//capMovimento.setImportoUscite(capMovimento.getImportoUscite().add(movStampa.getImportoRigaMovimento()));
					importoMovU = movStampa.getImportoRigaMovimento();
					
					//segue vecchio calcolo antecedente LOTTO M
				//	importoParzialePerCap = importoParzialePerCap.add(mov.getRichiestaEconomale().getImporto());
					
				}
				// Controlla che lo stato sia 'ANNULLATO'
				boolean statoAnnullato = StatoOperativoRichiestaEconomale.ANNULLATA.equals(mov.getRichiestaEconomale().getStatoOperativoRichiestaEconomale());
				
				if (movStampa.getFlagAnticipoSpeseMissione().booleanValue()) {
					numMovCapitoloASM++;
					
					if(!statoAnnullato) {
						log.debug(methodName, " Stato operativo non annullato per ASM: aggiungo gli importi al totale");
						capMovimento.setImportoEntrateASM(capMovimento.getImportoEntrateASM().add(importoMovE));
						capMovimento.setImportoUsciteASM(capMovimento.getImportoUsciteASM().add(importoMovU));
						log.debug(methodName, " Parziale Entrate ASM " + capMovimento.getImportoEntrateASM());
						log.debug(methodName, " Parziale Uscite ASM " + capMovimento.getImportoUsciteASM());
					}
				} else {
					numMovCapitoloNOASM++;
					
					if(!statoAnnullato) {
						log.debug(methodName, " Stato operativo non annullato per non-ASM: aggiungo gli importi al totale");
						capMovimento.setImportoEntrateNOASM(capMovimento.getImportoEntrateNOASM().add(importoMovE));
						capMovimento.setImportoUsciteNOASM(capMovimento.getImportoUsciteNOASM().add(importoMovU));
						log.debug(methodName, " Parziale Entrate NO ASM " + capMovimento.getImportoEntrateNOASM());
						log.debug(methodName, " Parziale Uscite NO ASM " + capMovimento.getImportoUsciteNOASM());
					}
				}
				
				//SIAC_3076
				// nel caso di richieste di antipo Spese per trasferta o per anticipo missione bisogna mostrare 
				//occorre mettere il campo descrizione che per quelle due richieste è 'motivo della trasferta
				if((CODIFICA_TIPO_RICHIESTA_ECONOMALE_ANTICIPO_SPESE_MISSIONE.equals(mov.getRichiestaEconomale().getTipoRichiestaEconomale().getCodice())
						|| CODIFICA_TIPO_RICHIESTA_ECONOMALE_ANTICIPO_SPESE_PER_TRASFERTA.equals(mov.getRichiestaEconomale().getTipoRichiestaEconomale().getCodice())
					)&& movStampa.getRichiestaEconomale().getDatiTrasfertaMissione()!=null){
				 movStampa.getRichiestaEconomale().setDescrizioneDellaRichiesta(StringUtils.isNotBlank(movStampa.getRichiestaEconomale().getDatiTrasfertaMissione().getMotivo())? movStampa.getRichiestaEconomale().getDatiTrasfertaMissione().getMotivo():"");
				}
				
				listWrappedMov.add(movStampa);

			}
		}
		capMovimento.setNumMovimentiCapitoloASM(numMovCapitoloASM);
		log.debug(methodName, " I1: Numero Movimenti ASM " + capMovimento.getNumMovimentiCapitoloASM());
		capMovimento.setNumMovimentiCapitoloNOASM(numMovCapitoloNOASM);
		log.debug(methodName, " I1: Numero Movimenti NO ASM " + capMovimento.getNumMovimentiCapitoloNOASM());
		capMovimento.setNumMovimentiCapitolo(Integer.valueOf(listaMovimenti.size()));
		log.debug(methodName, " Numero Movimenti Capitolo" + capMovimento.getNumMovimentiCapitoloASM() + capMovimento.getNumMovimentiCapitoloNOASM());
		
		log.debug(methodName, " L1: Importo Entrate Movimenti ASM " + capMovimento.getImportoEntrateASM());
		log.debug(methodName, " L1: Importo Entrate Movimenti NOASM " + capMovimento.getImportoEntrateNOASM());
		log.debug(methodName, " L2: Importo Uscite Movimenti ASM " + capMovimento.getImportoUsciteASM());
		log.debug(methodName, " L2: Importo Uscite Movimenti NOASM " + capMovimento.getImportoUsciteNOASM());
		capMovimento.setImportoTotaleUsciteEntrateASM(capMovimento.getImportoUsciteASM().subtract(capMovimento.getImportoEntrateASM()));
		log.debug(methodName, " L3: L2-L1 ASM" + capMovimento.getImportoTotaleUsciteEntrateASM());
		capMovimento.setImportoTotaleUsciteEntrateNOASM(capMovimento.getImportoUsciteNOASM().subtract(capMovimento.getImportoEntrateNOASM()));
		log.debug(methodName, " L3: L2-L1 NOASM" + capMovimento.getImportoTotaleUsciteEntrateNOASM());
		
		//capMovimento.setImportoTotaleUsciteEntrate(capMovimento.getImportoUscite().subtract(capMovimento.getImportoEntrate()));
		//log.debug(methodName, " L3: L2-L1 Importo Rendioconto Movimenti" + capMovimento.getImportoTotaleUsciteEntrate());
		//segue vecchio importo antecedente LOTTO M
		//capMovimento.setImportoParziale(importoParzialePerCap);
		
		capMovimento.setListMovimentos(listWrappedMov);
	}

	private void  inserisciMovimentoInMappatura(Map<String, List<Movimento>> map, Movimento movimento, String numeroCapitoloArticolo) {
		List<Movimento> listaMovimentiPerCapitolo = map.get(numeroCapitoloArticolo);

		if (listaMovimentiPerCapitolo == null)	{
			listaMovimentiPerCapitolo = new ArrayList<Movimento>();
			map.put(numeroCapitoloArticolo, listaMovimentiPerCapitolo);
		}
		listaMovimentiPerCapitolo.add(movimento);
	}

	private StampaRendicontoCassaIntestazione elaboraIntestazione() {
		final String methodName = "elaboraIntestazione";
		StampaRendicontoCassaIntestazione intestazione = new StampaRendicontoCassaIntestazione();

		
		intestazione.setTipoStampa(tipoStampa);
		log.debug(methodName, " A1: Tipo Stampa " + intestazione.getTipoStampa().getDescrizione());
		intestazione.setNumeroDiPagina(primaPaginaDaStampare);
		log.debug(methodName, " A2: numero pagina " + intestazione.getNumeroDiPagina());
		intestazione.setEnte(cassaEconomale.getEnte());
		log.debug(methodName, " A3: Ente " + intestazione.getEnte().getUid());
		intestazione.setDirezione(cassaEconomale.getVariabiliStampa() != null && cassaEconomale.getVariabiliStampa().getIntestazioneDirezione()!= null ? cassaEconomale.getVariabiliStampa().getIntestazioneDirezione() : "");
		log.debug(methodName, " A4: Direzione " + intestazione.getDirezione());
		intestazione.setSettore(cassaEconomale.getVariabiliStampa() != null && cassaEconomale.getVariabiliStampa().getIntestazioneSettore()!= null ? cassaEconomale.getVariabiliStampa().getIntestazioneSettore() : "");
		log.debug(methodName, " A5: Settore " + intestazione.getSettore());
		intestazione.setUfficio(cassaEconomale.getVariabiliStampa() != null && cassaEconomale.getVariabiliStampa().getIntestazioneUfficio()!= null ? cassaEconomale.getVariabiliStampa().getIntestazioneUfficio() : "");
		log.debug(methodName, " A6: Ufficio " + intestazione.getUfficio());
				
		if (isGestioneMultipla) {
			intestazione.setRiferimentoCassaEconomale(cassaEconomale.getCodice() + " " + cassaEconomale.getDescrizione());
		}else{
			intestazione.setRiferimentoCassaEconomale(cassaEconomale.getCodice());
		}
		log.debug(methodName, " A7: CassaEconomale " + intestazione.getRiferimentoCassaEconomale());
		intestazione.setDataStampaRendiconto(dataStampaRendiconto);
		log.debug(methodName, " A8: Data Rendiconto " + intestazione.getDataStampaRendiconto());
		this.setNumeroRendiconto(Integer.valueOf(datiStampaUltimoRendiconto.getStampaRendiconto().getNumeroRendiconto().intValue()+1));
		intestazione.setNumeroRendiconto(numeroRendiconto);
		log.debug(methodName, " A9: Data Rendiconto " + intestazione.getNumeroRendiconto());
		intestazione.setPeriodoInizio(periodoInizio);
		log.debug(methodName, " A10: Data Periodo Inizio " + intestazione.getPeriodoInizio());
		intestazione.setPeriodoFine(periodoFine);
		log.debug(methodName, " A11: Data Periodo Fine " + intestazione.getPeriodoFine());
		
		
		
		return intestazione;
	}

	
	private void popolaOperazioniCassa(Date periodoInizio, Date periodoFine) {
		final String methodName = "popolaOperazioniCassa";
		TipoOperazioneCassa top = new TipoOperazioneCassa();
		
		top.setInclusoInGiornale(true);
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();		
		parametriPaginazione.setNumeroPagina(0);
		parametriPaginazione.setElementiPerPagina(Integer.MAX_VALUE);
		//recuperto i tipidioperazionecassa
		operazioneDiCassaDad.setEnte(ente);
		List<TipoOperazioneCassa> listaTipoOperazioneCassa= operazioneDiCassaDad.ricercaTipoOperazioneCassa(cassaEconomale);
		//TODO verificare se sostituibile con una sola query
		for(TipoOperazioneCassa tipoOperazioneCassa : listaTipoOperazioneCassa) {
			if (tipoOperazioneCassa.getInclusoInRendiconto()) {
				List<OperazioneCassa> listaOperazioneCassaPerTipoOp = operazioneDiCassaDad.ricercaSinteticaOperazioneDiCassaPerPeriodo(bilancio, cassaEconomale, periodoInizio, periodoFine , tipoOperazioneCassa, null, Arrays.asList(StatoOperativoOperazioneCassa.ANNULLATO), parametriPaginazione);
				if (listaOperazioneCassaPerTipoOp!= null && !listaOperazioneCassaPerTipoOp.isEmpty()) {
					for (OperazioneCassa oc : listaOperazioneCassaPerTipoOp) {
						StampaRendicontoCassaOperazioneCassa operazioneStampa = new StampaRendicontoCassaOperazioneCassa();
						DummyMapper.mapNotNullNotEmpty(oc, operazioneStampa);
						String descrizioneOp = operazioneStampa.getTipoOperazioneCassa().getDescrizione() + " - "  + operazioneStampa.getTipoOperazioneCassa().getTipologiaOperazioneCassa().getDescrizione();
						operazioneStampa.setDescrizione(descrizioneOp);
						log.debug(methodName, " O1 : Descrizione cassa " + operazioneStampa.getDescrizione());
						log.debug(methodName, " P1 : Importo " + operazioneStampa.getImporto());
						if (TipologiaOperazioneCassa.SPESA.equals(tipoOperazioneCassa.getTipologiaOperazioneCassa())) {
							//FIXME: abs aggiunto per evitare problemi nei conti del totale se considerate le vecchie operazioniCassa con val negativo
							importoTotaleOperazioniCassa = importoTotaleOperazioniCassa.add(operazioneStampa.getImporto().abs()); 
						} else {
							//FIXME: abs aggiunto per evitare problemi nei conti del totale se considerate le vecchie operazioniCassa con val negativo
							importoTotaleOperazioniCassa = importoTotaleOperazioniCassa.subtract(operazioneStampa.getImporto().abs());
						}
						listaOperazioneDiCassaPerStampa.add(operazioneStampa);
					}
					
					listaOperazioneDiCassa.addAll(listaOperazioneCassaPerTipoOp);
				}
			}
			
		}
		result.setListaOperazioniDiCassa(listaOperazioneDiCassaPerStampa);
		//Impostare a zero se è negativo
		log.debug(methodName, "Totale Importo Casse prima del controllo segno " + importoTotaleOperazioniCassa);
		result.setImportoTotaleOperazioniCassa(importoTotaleOperazioniCassa.signum() >= 0 ? importoTotaleOperazioniCassa : BigDecimal.ZERO);
		log.debug(methodName, " Q1: Totale Importo Casse " + result.getImportoTotaleOperazioniCassa());
		
		
	}
	@Override
	public String getCodiceTemplate() {
		return "StampaRendicontoCassa";
	}
	
	// SIAC-4799
	private void inserisciAttoAllegato() {
		final String methodName = "inserisciAttoAllegato";
		// Inizializzo l'helper
		allegatoAttoHelper = new StampaRendicontoCassaAllegatoAttoHelper(richiedente, ente, bilancio, tipoStampa,
				listaStampaRendicontoCassaCapitoloMovimenti, soggetto, modalitaPagamentoSoggetto,
				strutturaAmministrativoContabile, causale, anticipiSpesaDaInserire, serviceExecutor, attoAmministrativoDad);
		
		// Esecuzione dell'helper
		try {
			helperExecutor.executeHelperTxRequiresNew(allegatoAttoHelper);
		} catch(HelperException he) {
			log.info(methodName, "Exception nell'invocazione dell'helper: " + he.getMessage() + ". Si prosegue l'elaborazione");
		}
		log.debug(methodName, "Inserito allegato atto in nuova transazione");
	}

	@Override
	protected void handleResponse(GeneraReportResponse res) {
		final String methodName = "handleResponse";
		log.debug(methodName, "numero di pagine generata: "+ res.getNumeroPagineGenerate());
		
		inserisciAttoAllegato();
		persistiStampaFile(res);
	}
	
	/**
	 * Persiste la stampa  su database.
	 * 
	 * @param res la risposta del metodo di generazione del report
	 */
	private void persistiStampaFile(GeneraReportResponse res) {
		final String methodName = "persistiStampaFile";
		log.debug(methodName, "Persistenza della stampa");
		stampeCassaFileDad.setEnte(ente);
		stampeCassaFileDad.setLoginOperazione(getRichiedente().getOperatore().getCodiceFiscale());
		StampeCassaFile stampeCF = stampeCassaFileDad.inserisciStampa(creaStampaRendiconto(res));
	
		
		log.info(methodName, "Stampa terminata con successo. Uid record inserito su database: " + stampeCF.getUid());
	}
	
	/**
	 * Crea una StampaIva per la persistenza.
	 * 
	 * @param res la response della generazione del report
	 * @return la stampa iva creata
	 */
	private StampeCassaFile creaStampaRendiconto(GeneraReportResponse res) {
		
		StampeCassaFile result = new StampeCassaFile();
		
		
		result.setAnnoEsercizio(bilancio.getAnno());
		
		File file = res.getReport();
		
		result.setCodice(file.getCodice());
		
		result.setEnte(getEnte());
		result.setCassaEconomale(cassaEconomale);
		result.setBilancio(bilancio);
		result.setTipoStampa(tipoStampa);
		result.setTipoDocumento(TipoDocumento.RENDICONTO);
		// SIAC-4799
		result.setAllegatoAtto(allegatoAttoHelper.getAllegatoAtto());
		
		
		
		List<File> listaFile = new ArrayList<File>();
		
		listaFile.add(file);
		result.setFiles(listaFile);
		StampaRendiconto sr = new StampaRendiconto();
		sr.setCassaEconomale(cassaEconomale);
		
		sr.setDataRendiconto(dataStampaRendiconto);
		sr.setNumeroRendiconto(numeroRendiconto);
		sr.setPeriodoDataInizio(periodoInizio);
		sr.setPeriodoDataFine(periodoFine);
		if (TipoStampa.DEFINITIVA.equals(tipoStampa)) {
			// salvo i dati necessari solo x la definitiva
			result.setMovimenti(listaMovimento);
			result.setOperazioniCassa(listaOperazioneDiCassa);
		}
		result.setStampaRendiconto(sr);
		return result;
	}
	
	
	/**
	 * Ottiene i dati di dettaglio dell'ente.
	 */
	private StampeCassaFile caricaDatiUltimoRendiconto() {
		final String methodName = "persistiStampaFile";
		StampeCassaFile scf = new StampeCassaFile();

		scf.setCassaEconomale(cassaEconomale);
		
		scf.setBilancio(bilancio);
		scf.setTipoDocumento(TipoDocumento.RENDICONTO);
		stampeCassaFileDad.setEnte(ente);
		StampaRendiconto stampaUltimo = stampeCassaFileDad.findStampaUltimoNumeroRendiconto(scf);
		if (stampaUltimo == null) {
			log.debug(methodName, "nessun numero di rendiconto salvato questo è il primo");
			stampaUltimo = new StampaRendiconto();
			stampaUltimo.setNumeroRendiconto(Integer.valueOf(0));
			
		}
		log.debug(methodName, "Ultimo RendicontoSalvato :  "+ stampaUltimo.getUltimoNumeroRendiconto());
	//	stampaUltimo.setNumeroRendiconto(Integer.valueOf(stampaUltimo.getNumeroRendiconto().intValue()+1));
		scf.setStampaRendiconto(stampaUltimo);
		
		
		return scf;
	}

	/**
	 * @param cassaEconomale the cassaEconomale to set
	 */
	public void setCassaEconomale(CassaEconomale cassaEconomale) {
		this.cassaEconomale = cassaEconomale;
	}
	/**
	 * @param dataStampaRendiconto the dataStampaRendiconto to set
	 */
	public void setDataStampaRendiconto(Date dataStampaRendiconto) {
		this.dataStampaRendiconto = dataStampaRendiconto == null ? null : new Date(dataStampaRendiconto.getTime());
	}
	/**
	 * @param tipoStampa the tipoStampa to set
	 */
	public void setTipoStampa(TipoStampa tipoStampa) {
		this.tipoStampa = tipoStampa;
	}
	/**
	 * @param bilancio the bilancio to set
	 */
	public void setBilancio(Bilancio bilancio) {
		this.bilancio = bilancio;
	}
	/**
	 * @param primaPaginaDaStampare the primaPaginaDaStampare to set
	 */
	public void setPrimaPaginaDaStampare(Integer primaPaginaDaStampare) {
		this.primaPaginaDaStampare = primaPaginaDaStampare;
	}
	/**
	 * @param numeroRendiconto the numeroRendiconto to set
	 */
	public void setNumeroRendiconto(Integer numeroRendiconto) {
		this.numeroRendiconto = numeroRendiconto;
	}
	/**
	 * @param periodoInizio the periodoInizio to set
	 */
	public void setPeriodoInizio(Date periodoInizio) {
		this.periodoInizio = periodoInizio == null ? null : new Date(periodoInizio.getTime());
	}
	/**
	 * @param periodoFine the periodoFine to set
	 */
	public void setPeriodoFine(Date periodoFine) {
		this.periodoFine = periodoFine == null ? null : new Date(periodoFine.getTime());
	}
	/**
	 * @param datiStampaUltimoRendiconto the datiStampaUltimoRendiconto to set
	 */
	public void setDatiStampaUltimoRendiconto(StampeCassaFile datiStampaUltimoRendiconto) {
		this.datiStampaUltimoRendiconto = datiStampaUltimoRendiconto;
	}

	/**
	 * @param soggetto the soggetto to set
	 */
	public void setSoggetto(Soggetto soggetto) {
		this.soggetto = soggetto;
	}

	/**
	 * @param modalitaPagamentoSoggetto the modalitaPagamentoSoggetto to set
	 */
	public void setModalitaPagamentoSoggetto(ModalitaPagamentoSoggetto modalitaPagamentoSoggetto) {
		this.modalitaPagamentoSoggetto = modalitaPagamentoSoggetto;
	}

	/**
	 * @param causale the causale to set
	 */
	public void setCausale(String causale) {
		this.causale = causale;
	}

	/**
	 * @param strutturaAmministrativoContabile the strutturaAmministrativoContabile to set
	 */
	public void setStrutturaAmministrativoContabile(StrutturaAmministrativoContabile strutturaAmministrativoContabile) {
		this.strutturaAmministrativoContabile = strutturaAmministrativoContabile;
	}

	/**
	 * @param anticipiSpesaDaInserire the anticipiSpesaDaInserire to set
	 */
	public void setAnticipiSpesaDaInserire(Boolean anticipiSpesaDaInserire) {
		this.anticipiSpesaDaInserire = anticipiSpesaDaInserire;
	}
	

}
