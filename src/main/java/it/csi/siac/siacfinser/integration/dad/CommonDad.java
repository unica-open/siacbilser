/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.model.StatoOperativoProgetto;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfin2ser.model.CodiceBollo;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.TimingUtils;
import it.csi.siac.siacfinser.integration.dao.carta.SiacDCartacontStatoRepository;
import it.csi.siac.siacfinser.integration.dao.carta.SiacDCommissioniEsteroRepository;
import it.csi.siac.siacfinser.integration.dao.carta.SiacDValutaFinRepository;
import it.csi.siac.siacfinser.integration.dao.common.SiacDAmbitoRepository;
import it.csi.siac.siacfinser.integration.dao.common.SiacDDocTipoFinRepository;
import it.csi.siac.siacfinser.integration.dao.common.SiacDViaTipoRepository;
import it.csi.siac.siacfinser.integration.dao.common.SiacTEnteProprietarioFinRepository;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoCercaProgrammaDto;
import it.csi.siac.siacfinser.integration.dao.liquidazione.SiacDContotesoreriaFinRepository;
import it.csi.siac.siacfinser.integration.dao.liquidazione.SiacDDistintaRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacDModificaTipoRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacDMovgestStatoRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacDMovgestTipoRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacRMovgestTsAttoAmmRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTProgrammaFinRepository;
import it.csi.siac.siacfinser.integration.dao.mutuo.SiacDMutuoTipoRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacDCodiceBolloFinRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacDCommissioneTipoRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacDNoteTesoriereFinRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacDOrdinativoStatoRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacDAccreditoGruppoRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacDFormaGiuridicaTipoRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacDIndirizzoTipoRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacDOnereFinRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacDRecapitoModoRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacDRelazTipoRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacDSoggettoClasseRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacDSoggettoStatoRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacDSoggettoTipoRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacDTipoAccreditoRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacTComuneRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacTFormaGiuridicaRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacTNazioneRepository;
import it.csi.siac.siacfinser.integration.entity.SiacDAccreditoGruppoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDAccreditoTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDAmbitoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDCartacontStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDCodicebolloFin;
import it.csi.siac.siacfinser.integration.entity.SiacDCommissioneTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDCommissioniesteroFin;
import it.csi.siac.siacfinser.integration.entity.SiacDContotesoreriaFin;
import it.csi.siac.siacfinser.integration.entity.SiacDDistintaFin;
import it.csi.siac.siacfinser.integration.entity.SiacDDocTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDFormaGiuridicaTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDIndirizzoTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDModificaTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDMovgestStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDMutuoTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDNoteTesoriereFin;
import it.csi.siac.siacfinser.integration.entity.SiacDOnereFin;
import it.csi.siac.siacfinser.integration.entity.SiacDRecapitoModoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDRelazTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDSiopeAssenzaMotivazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacDSoggettoClasseFin;
import it.csi.siac.siacfinser.integration.entity.SiacDSoggettoStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDSoggettoTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDValutaFin;
import it.csi.siac.siacfinser.integration.entity.SiacDViaTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsAttoAmmFin;
import it.csi.siac.siacfinser.integration.entity.SiacTClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacTComuneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTEnteProprietarioFin;
import it.csi.siac.siacfinser.integration.entity.SiacTFormaGiuridicaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.SiacTNazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTProgrammaFin;
import it.csi.siac.siacfinser.integration.entity.mapping.FinMapId;
import it.csi.siac.siacfinser.integration.util.DatiOperazioneUtils;
import it.csi.siac.siacfinser.integration.util.EntityToModelConverter;
import it.csi.siac.siacfinser.integration.util.ObjectStreamerHandler;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.AttoAmministrativoStoricizzato;
import it.csi.siac.siacfinser.model.codifiche.CodificaExtFin;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.soggetto.ComuneNascita;

/**
 * 
 * @author paolos
 *
 */

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class CommonDad extends AbstractFinDad {

	@Autowired
	SiacDDocTipoFinRepository siacDDocTipoRepository;
	
	@Autowired
	SiacTNazioneRepository siacTNazioneRepository;
	
	@Autowired
	SiacTFormaGiuridicaRepository siacTFormaGiuridicaRepository;
	
	@Autowired
	SiacTComuneRepository siacTComuneRepository;
	
	@Autowired
	SiacDSoggettoTipoRepository siacDSoggettoTipoRepository;
	
	@Autowired
	SiacDSoggettoClasseRepository siacDSoggettoClasseRepository;
	
	@Autowired
	SiacDIndirizzoTipoRepository siacDIndirizzoTipoRepository;
	
	@Autowired
	SiacDSoggettoStatoRepository siacDSoggettoStatoRepository;
	
	@Autowired
	SiacDFormaGiuridicaTipoRepository siacDFormaGiuridicaTipoRepository;
	
	@Autowired
	SiacDRecapitoModoRepository siacDRecapitoModoRepository;
	
	@Autowired
	SiacDOnereFinRepository siacDOnereRepository;
	
	@Autowired
	ObjectStreamerHandler objectStreamerHandler;
	
	@Autowired
	SiacDTipoAccreditoRepository siacDTipoAccreditoRepository;
	
	@Autowired
	SiacDViaTipoRepository siacDViaTipoRepository;
	
	@Autowired
	SiacDRelazTipoRepository siacDRelazTipoRepository;
	
	@Autowired
	SiacDAmbitoRepository siacDAmbitoRepository;
	
	@Autowired
	SiacDMovgestStatoRepository siacDMovgestStatoRepository;
	@Autowired
	SiacDAccreditoGruppoRepository siacDAccreditoGruppoRepository;
	
	@Autowired
	SiacDMovgestTipoRepository siacDMovgestTipoRepository;
	
	@Autowired
	SiacTEnteProprietarioFinRepository siacTEnteProprietarioRepository;
	
	@Autowired
	SiacTProgrammaFinRepository siacTProgrammaRepository;
	
	@Autowired
	SiacDModificaTipoRepository siacDModificaTipoRepository;
	
	@Autowired
	SiacDMutuoTipoRepository siacDMutuoTipoRepository;
	
	@Autowired
	SiacDContotesoreriaFinRepository siacDContotesoreriaRepository;
	
	@Autowired
	SiacDDistintaRepository siacDDistintaRepository;
	
	@Autowired
	SiacDCodiceBolloFinRepository siacDCodiceBolloRepository;
	
	@Autowired
	SiacDCommissioneTipoRepository siacDCommissioneTipoRepository;
	
	@Autowired
	SiacDNoteTesoriereFinRepository siacDNoteTesoriereRepository;
	
	@Autowired
	SiacDOrdinativoStatoRepository siacDOrdinativoStatoRepository;
	
	@Autowired
	SiacDCartacontStatoRepository siacDCartacontStatoRepository;
	
	@Autowired
	SiacDValutaFinRepository siacDValutaRepository;
	
	@Autowired
	SiacDCommissioniEsteroRepository siacDCommissioniEsteroRepository;
	
	@Autowired
	SiacRMovgestTsAttoAmmRepository siacRMovgestTsAttoAmmRepository;
	
	private static final String CODE_TIPO_LEGAME_SOGGETTO = "Soggetto";
	
	/**
	 * RAFFAELA INSERISCO RIGA DI TEST PER REVERT CLAUDIO!
	 */
	
	/**
	 * caricamento per ajax del sedime in indirizzo soggetto
	 * @param descrizione
	 * @param ente
	 * @return
	 */
	public ArrayList<CodificaExtFin> findSedimeLike(String descrizione, Ente ente){
		List<SiacDViaTipoFin> dtos = siacDViaTipoRepository.findListaSedimeLike(descrizione, ente.getUid());
		return new ArrayList<CodificaExtFin>(convertiLista(dtos, 
							                CodificaExtFin.class, 
							                FinMapId.SiacDViaTipo_CodificaExtFin));
	}
	/**
	 *  caricamento combo tipo oneri
	 * @param ente
	 * @return
	 */
	public ArrayList<CodificaFin> findTipoOneri(Ente ente){
		long currMillisec = getCurrentMillisecondsTrentaMaggio2017();
		Date dateInserimento = new Date(currMillisec);			
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);	
		
		List<SiacDOnereFin> dtos = new ArrayList<SiacDOnereFin>();
		
		dtos = siacDOnereRepository.findOneri(ente.getUid(),timestampInserimento);
		
		return new ArrayList<CodificaFin>(convertiLista(dtos, 
				                                        CodificaFin.class, 
				                                        FinMapId.SiacDOnere_CodificaFin));	
	}
	
	/**
	 * caricamento combo tipo motivo
	 * @param ente
	 * @return
	 */
	public ArrayList<CodificaFin> findTipoMotivo(Ente ente){
		long currMillisec = getCurrentMillisecondsTrentaMaggio2017();
		Date dateInserimento = new Date(currMillisec);			
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);	
		
		List<SiacDModificaTipoFin> dtos = new ArrayList<SiacDModificaTipoFin>();
		
		dtos = siacDModificaTipoRepository.findValidiByEnte(ente.getUid(), timestampInserimento);
		
		return new ArrayList<CodificaFin>(convertiLista(dtos, 
				                                        CodificaFin.class, 
				                                        FinMapId.SiacDModificaTipo_CodificaFin));	
	}
	
	/**
	 * caricamento combo dei tipi di accredito
	 * @param ente
	 * @return
	 */
	public ArrayList<CodificaFin> findTipoAccredito(Ente ente){
		long currMillisec = getCurrentMillisecondsTrentaMaggio2017();
		Date dateInserimento = new Date(currMillisec);			
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);
		
		List<SiacDAccreditoTipoFin> dtos = new ArrayList<SiacDAccreditoTipoFin>();
		
		dtos = siacDTipoAccreditoRepository.findTipoAccreditiOrdered(ente.getUid(),timestampInserimento);
		
		return new ArrayList<CodificaFin>(convertiLista(dtos, 
				                                        CodificaFin.class, 
				                                        FinMapId.SiacDTipoAccredito_CodificaFin));	
	}
	
	/**
	 * Metodo che si occupa di valorizzare un oggetto DatiOperazioneDto che viene poi "passato di mano" tra 
	 * i vari metodi che compongono ogni servizio, per evitare di ricaricare i dati piu comuni
	 * 
	 * tranne in soggetto gli altri service invocano sempre questo metodo che a sua volta 
	 * chiama l'altro passando null come ambito
	 * 
	 * @param ente
	 * @param richiedente
	 * @param operazione
	 * @return
	 */
	public DatiOperazioneDto inizializzaDatiOperazione(Ente ente,Richiedente richiedente,Operazione operazione, Integer annoBilancio){
		
		return inizializzaDatiOperazione( ente, richiedente, operazione, null, annoBilancio);

	}
	
	
	/**
	 * 
	 * inizializzazione dati invocata dai service di soggetto
	 * 
	 * @param ente
	 * @param richiedente
	 * @param operazione
	 * @param ambito
	 * @return
	 */
	public DatiOperazioneDto inizializzaDatiOperazione(Ente ente,Richiedente richiedente,Operazione operazione, String ambito,Integer annoBilancio){
		
		long currMillisec = getCurrentMilliseconds();
		
		int idEnte = ente.getUid();
		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository.findOne(idEnte);
		
		if(null==ambito || "".equals(ambito)){
			// se non arriva assumo che di default sia quello di FIN
			ambito = Constanti.AMBITO_FIN;
		}
		
		SiacDAmbitoFin  siacDAmbitoPerCode = siacDAmbitoRepository.findAmbitoByCode(ambito, idEnte);
		DatiOperazioneDto datiOperazioneDto = new DatiOperazioneDto(currMillisec, operazione, siacTEnteProprietario,siacDAmbitoPerCode, richiedente.getAccount().getId(), annoBilancio);
        return datiOperazioneDto;
	}
	
	/**
	 * Estrare dall'oggetto dati operazione il codice dell'utente loggato andando a caricarlo sul database
	 * @param datiOperazione
	 * @return
	 */
	public String determinaUtenteLogin(DatiOperazioneDto datiOperazione){
		String loginOperazione = DatiOperazioneUtils.determinaUtenteLogin(datiOperazione, siacTAccountRepository);
		return loginOperazione;
	}
	
	/**
	 * caricamento combo delle modalita' di recapito del soggetto
	 * @param ente
	 * @return
	 */
	public ArrayList<CodificaExtFin> findRecapitoModo(Ente ente){
		long currMillisec = getCurrentMillisecondsTrentaMaggio2017();
		Date dateInserimento = new Date(currMillisec);			
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);	
		
		List<SiacDRecapitoModoFin> dtos = new ArrayList<SiacDRecapitoModoFin>();
		
		dtos = siacDRecapitoModoRepository.findRecapitoModo(ente.getUid(),timestampInserimento);
		
		return new ArrayList<CodificaExtFin>(convertiLista(dtos, 
				                                        CodificaExtFin.class, 
				                                        FinMapId.SiacDRecapitoModo_CodificaFin));	
	}
	
	/**
	 * caricamento combo forma giuridica soggetto
	 * @param ente
	 * @return
	 */
	public ArrayList<CodificaFin> findFormaGiuridicaTipo(Ente ente){
		long currMillisec = getCurrentMillisecondsTrentaMaggio2017();
		Date dateInserimento = new Date(currMillisec);			
		Timestamp timestampInserimento= TimingUtils.convertiDataInTimeStamp(dateInserimento);	
		List<SiacDFormaGiuridicaTipoFin> dtos = new ArrayList<SiacDFormaGiuridicaTipoFin>();
		dtos = siacDFormaGiuridicaTipoRepository.findFormeGiuridicheTipo(ente.getUid(),timestampInserimento);
		
		return new ArrayList<CodificaFin>(convertiLista(dtos, 
				                                        CodificaFin.class, 
				                                        FinMapId.SiacDFormaGiuridicaTipo_CodificaFin));	
	}
	
	/**
	 * caricamento combo stati operativi del soggetto
	 * 
	 * @param ente
	 * @return
	 */
	public ArrayList<CodificaFin> findStatiOperativi(Ente ente){
		
		long currMillisec = getCurrentMillisecondsTrentaMaggio2017();
		Date dateInserimento = new Date(currMillisec);			
		Timestamp timestampInserimento= TimingUtils.convertiDataInTimeStamp(dateInserimento);		
		List<SiacDSoggettoStatoFin> dtos2 = new ArrayList<SiacDSoggettoStatoFin>();	
		dtos2 = siacDSoggettoStatoRepository.findListaSoggettoStatoOperativo(ente.getUid(),timestampInserimento);
		
		return new ArrayList<CodificaFin>(convertiLista(dtos2, CodificaFin.class, FinMapId.SiacDSoggettoStato_CodificaFin));	
	}
	
	/**
	 * caricamento combo dei tipi di indirizzo della sede del soggetto
	 * 
	 * @param ente
	 * @return
	 */
	public ArrayList<CodificaFin> findTipiIndirizziSede(Ente ente) {
		long currMillisec = getCurrentMillisecondsTrentaMaggio2017();
		Date dateInserimento = new Date(currMillisec);			
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);
		List<SiacDIndirizzoTipoFin> dtos = new ArrayList<SiacDIndirizzoTipoFin>();
		
		dtos = siacDIndirizzoTipoRepository.findTipoIndirizzoSede(ente.getUid(),timestampInserimento);
		
		return new ArrayList<CodificaFin>(convertiLista(dtos, CodificaFin.class, FinMapId.SiacDIndirizzoTipo_CodificaFin));	
	}
	
	/**
	 * caricamento combo utilizzata in soggetto con la lista delle nazioni
	 * 
	 * @param ente
	 * @return
	 */
	public ArrayList<CodificaFin> findNazioni(Ente ente) {

		List<SiacTNazioneFin> dtos = new ArrayList<SiacTNazioneFin>();
		dtos = siacTNazioneRepository.findNazioni(ente.getUid());

		return new ArrayList<CodificaFin>(convertiLista(dtos, CodificaFin.class, FinMapId.SiacTNazione_CodificaFin));		
	}
	
    /**
     * caricamento combo con i tipi di natura giurdica
     * 
     * @param ente
     * @return
     */
	public ArrayList<CodificaExtFin> findListaNaturaGiuridica(Ente ente){
		long currMillisec = getCurrentMillisecondsTrentaMaggio2017();
		Date dateInserimento = new Date(currMillisec);			
		Timestamp timestampInserimento= TimingUtils.convertiDataInTimeStamp(dateInserimento);
		
		List<SiacDSoggettoTipoFin> dtos = new ArrayList<SiacDSoggettoTipoFin>();
		dtos = siacDSoggettoTipoRepository.findListaNaturaGiuridica(ente.getUid(),timestampInserimento);
		
		ArrayList<CodificaExtFin> result = new ArrayList<CodificaExtFin>(dtos.size());
		result.addAll(convertiLista( dtos , CodificaExtFin.class, FinMapId.SiacDSoggettoTipo_CodificaFin));
		//Termino restituendo l'oggetto di ritorno: 
        return result;
	}	
	
	/**
	 * caricamento combo con le nature giuridica del soggetto
	 * 
	 * @param ente
	 * @return
	 */
	public ArrayList<CodificaFin> findListaNaturaGiuridicaSoggetto(Ente ente){
		List<SiacTFormaGiuridicaFin> dtos = new ArrayList<SiacTFormaGiuridicaFin>();
		long currMillisec = getCurrentMillisecondsTrentaMaggio2017();
		Date dateInserimento = new Date(currMillisec);			
		Timestamp timestampInserimento= TimingUtils.convertiDataInTimeStamp(dateInserimento);
		
		dtos = siacTFormaGiuridicaRepository.findListaNaturaGiuridicaSoggetto(ente.getUid(),timestampInserimento);
		
		return new ArrayList<CodificaFin>(convertiLista( dtos , CodificaFin.class,FinMapId.SiacTFormaNaturaGiuridica_CodificaFin));
		
	}
	
	/**
	 * caricamento combo lista comuni
	 * 
	 * @param descrizioneComune
	 * @param idNazione
	 * @param enteId
	 * @param codiceNazione 
	 * @return
	 */
	public List<ComuneNascita> findListaComuni(String descrizioneComune, String codiceNazione, String codiceCatastale, Integer enteId){
		List<ComuneNascita> listaRitorno = null;
		List<SiacTComuneFin> dtos = new ArrayList<SiacTComuneFin>();
		
		dtos = siacTComuneRepository.findListaComuni(StringUtils.defaultString(descrizioneComune), StringUtils.defaultString( codiceNazione), StringUtils.defaultString(codiceCatastale), enteId);
				
		listaRitorno =  convertiLista( dtos , ComuneNascita.class,FinMapId.SiacTComune_ComuneNascita);
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	/**
	 * caricamento comuni per ajax nel soggetto
	 * @param nomeComune
	 * @param codiceNazione
	 * @param ricercaPuntuale
	 * @param enteId
	 * @return
	 */
	public ArrayList<CodificaFin> findComuneByDescrizione(String nomeComune, String codiceNazione, boolean ricercaPuntuale, Integer enteId){
		
		CodificaFin codificaFin = new CodificaFin();
		
		List<SiacTComuneFin> siacTComuneS = null;
		if(ricercaPuntuale){
			// ricerca puntale comune
			siacTComuneS =  siacTComuneRepository.findComuneByNomePuntuale(nomeComune, codiceNazione, enteId);
		}else{
			// ricerca like su comune
			if(null!=nomeComune){
				siacTComuneS =  siacTComuneRepository.findComuneByNomeLike(nomeComune, codiceNazione, enteId);
			}	
		}
		
		if(siacTComuneS!=null && siacTComuneS.size()>0){
			
			codificaFin.setCodice(siacTComuneS.get(0).getComuneIstatCode());
			codificaFin.setId(siacTComuneS.get(0).getComuneId());
			codificaFin.setDescrizione(siacTComuneS.get(0).getComuneDesc());
		}
		
		ArrayList<CodificaFin> listaCod = new ArrayList<CodificaFin>();
		listaCod.add(codificaFin);
		
		//Termino restituendo l'oggetto di ritorno: 
        return listaCod;
	}
		
	/**
	 * caricamento combo con gruppi di accredito in mdp
	 * @param enteId
	 * @param tipoAccreditoId
	 * @return
	 */
	public CodificaFin findGruppoAccreditoByTipoId(Integer enteId, Integer tipoAccreditoId){
			
		CodificaFin codificaFin = new CodificaFin();
		
		SiacDAccreditoGruppoFin gruppo = siacDAccreditoGruppoRepository.findGruppoByTipoId(enteId, tipoAccreditoId);
	
		codificaFin.setId(gruppo.getUid());
		codificaFin.setCodice(gruppo.getAccreditoGruppoCode());
		codificaFin.setDescrizione(gruppo.getAccreditoGruppoDesc());
		
		//Termino restituendo l'oggetto di ritorno: 
        return codificaFin;
	}
	
	/**
	 * caricamento combo con la lista delle classi del soggetto
	 * @param ente
	 * @return
	 */
	public ArrayList<CodificaFin> findSoggettiClasse(Ente ente){
		long currMillisec = getCurrentMillisecondsTrentaMaggio2017();
		Date dateInserimento = new Date(currMillisec);			
		Timestamp timestampInserimento= TimingUtils.convertiDataInTimeStamp(dateInserimento);	
		
		
		List<SiacDSoggettoClasseFin> dtos = new ArrayList<SiacDSoggettoClasseFin>();
		
		SiacDAmbitoFin  siacDAmbitoPerCode = siacDAmbitoRepository.findAmbitoByCode(Constanti.AMBITO_FIN, ente.getUid());
		Integer idAmbito = siacDAmbitoPerCode.getAmbitoId();
		
		dtos = siacDSoggettoClasseRepository.findSoggettiClasse(ente.getUid(), idAmbito, timestampInserimento);

		return new ArrayList<CodificaFin>(convertiLista( dtos , CodificaFin.class, FinMapId.SiacDSoggettoClasse_CodificaFin));
		
	}
	
	/**
	 * caricamento combo con i tipi di legame tra soggetti
	 * @param ente
	 * @return
	 */
	public ArrayList<CodificaFin> findTipoLegame(Ente ente){
		List<SiacDRelazTipoFin> dtos = new ArrayList<SiacDRelazTipoFin>();
		dtos = siacDRelazTipoRepository.findRelazTipoByEnte(ente.getUid());
		return new ArrayList<CodificaFin>(convertiLista( dtos , 
							  CodificaFin.class,
				              FinMapId.SiacDRelazTipo_CodificaFin));
	}
	
	/**
	 * caricamento dati con i tipi di stato per i movimenti di gestione
	 * @param ente
	 * @return
	 */
	public List<AttoAmministrativoStoricizzato> leggiStoricoAggiornamentoProvvedimento(Integer uidMovgest, String tipoMovimento){
		
		List<AttoAmministrativoStoricizzato> storicoAggiornamentoProvvedimento = new ArrayList<AttoAmministrativoStoricizzato>();
		
		// leggi uid ts dato mogvest (questo vale per l'impegno, se da frontend arriva il sub non mi sreve ricercarlo, è gia l'id della siacTmovgestTs)
		Integer uidMovgestTs = null;
		
		if(tipoMovimento!= null && (tipoMovimento.equalsIgnoreCase(Constanti.MOVGEST_TS_TIPO_TESTATA) 
				|| tipoMovimento.equalsIgnoreCase(Constanti.MOVGEST_TIPO_IMPEGNO)
					||  tipoMovimento.equalsIgnoreCase(Constanti.MOVGEST_TIPO_ACCERTAMENTO))){
			
			//System.out.println("tipoMovimento: " + tipoMovimento);
			//System.out.println("uidMovgest: " + uidMovgest);
			
			SiacTMovgestTsFin siacTMovgestTs = findTestataByUidMovimento(uidMovgest);
			uidMovgestTs = siacTMovgestTs.getUid();
		}else{
			
			//System.out.println("(sub) uidMovgest: " + uidMovgest);
			uidMovgestTs = uidMovgest;
		}
		
		
		List<SiacRMovgestTsAttoAmmFin> siacRMovgestTsAttoAmmFin = siacRMovgestTsAttoAmmRepository.findStoricoSiacRMovgestTsAttoAmm(uidMovgestTs);
		
		if(siacRMovgestTsAttoAmmFin!=null && !siacRMovgestTsAttoAmmFin.isEmpty()){
			for (SiacRMovgestTsAttoAmmFin siacRMovgestTsAttoAmmFin2 : siacRMovgestTsAttoAmmFin) {
				
				AttoAmministrativoStoricizzato attostoricizzato = EntityToModelConverter.convertiSiacTAttoAmmToAttoAmministrativo(siacRMovgestTsAttoAmmFin2.getSiacTAttoAmm());
				attostoricizzato.setDataCancellazione(siacRMovgestTsAttoAmmFin2.getDataCancellazione());
				
				storicoAggiornamentoProvvedimento.add(attostoricizzato);
				
			}
			
			
		}
		return storicoAggiornamentoProvvedimento;	
	}
	
	

	
	/**
	 * caricamento dati con i tipi di stato per i movimenti di gestione
	 * @param ente
	 * @return
	 */
	public ArrayList<CodificaFin> findStatoOperativoMovgest(Ente ente){
		long currMillisec = getCurrentMillisecondsTrentaMaggio2017();
		Date dateInserimento = new Date(currMillisec);			
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);		
		List<SiacDMovgestStatoFin> dtos2 = new ArrayList<SiacDMovgestStatoFin>();	
		dtos2 = siacDMovgestStatoRepository.findListaMovgestStatoOperativo(ente.getUid(),timestampInserimento);
		
		return new ArrayList<CodificaFin>(convertiLista(dtos2, CodificaFin.class, FinMapId.SiacDMovgestStato_CodificaFin));	
	}
	
	/**
	 * caricamento del tipo impegno
	 * @param ente
	 * @return
	 */
	@SuppressWarnings("static-access")
	public ArrayList<CodificaFin> findTipoImpegno(Ente ente) {
		long currMillisec = getCurrentMillisecondsTrentaMaggio2017();
		Date dateInserimento = new Date(currMillisec);			
		Timestamp timestampInserimento=  null;
		TimingUtils tu= new TimingUtils();
		timestampInserimento= tu.convertiDataInTimeStamp(dateInserimento);		
		List<SiacTClassFin> dtos2 = new ArrayList<SiacTClassFin>();
		dtos2 = siacTClassRepository.findByTipoAndEnte(ente.getUid(), timestampInserimento, Constanti.D_CLASS_TIPO_TIPO_IMPEGNO );
		return new ArrayList<CodificaFin>(convertiLista(dtos2, CodificaExtFin.class, FinMapId.SiacTClass_CodificaExtFin));	
	}
	
	/**
	 * caricamento del tipo siope spesa I
	 * @param ente
	 * @return
	 */
	public ArrayList<CodificaFin> findTipoSiopeSpesaI(Ente ente,Bilancio bilancio) {
		return findTipoSiopeInternal(ente, bilancio, Constanti.D_CLASS_TIPO_SIOPE_SPESA_I);
	}
	
	/**
	 * caricamento del tipo siope entrata I
	 * @param ente
	 * @return
	 */
	public ArrayList<CodificaFin> findTipoSiopeEntrataI(Ente ente,Bilancio bilancio) {
		return findTipoSiopeInternal(ente, bilancio, Constanti.D_CLASS_TIPO_SIOPE_ENTRATA_I);
	}
	
	/**
	 * caricamento delle motivazioni di assenza cig
	 * @param ente
	 * @return
	 */
	@SuppressWarnings("static-access")
	public ArrayList<CodificaFin> findMotivazioniAssenzaCig(Ente ente,Bilancio bilancio) {
		long currMillisec = getCurrentMillisecondsTrentaMaggio2017();
		Date dateInserimento = new Date(currMillisec);			
		Timestamp timestampInserimento=  null;
		TimingUtils tu= new TimingUtils();
		timestampInserimento= tu.convertiDataInTimeStamp(dateInserimento);		
		List<SiacDSiopeAssenzaMotivazioneFin> dtos2 = new ArrayList<SiacDSiopeAssenzaMotivazioneFin>();
		dtos2 = siacDSiopeAssenzaMotivazioneFinRepository.findByEnte(ente.getUid(),timestampInserimento);
		return new ArrayList<CodificaFin>(convertiLista(dtos2, CodificaExtFin.class, FinMapId.SiacDSiopeAssenzaMotivazioneFin_CodificaExtFin));
		/*
		ArrayList<CodificaFin> array = new ArrayList<CodificaFin>();
		CodificaFin uno = new CodificaFin();
		uno.setDescrizione("ciao ciao");
		uno.setCodice("ciao ciao code");
		array.add(uno);
		
		CodificaFin due = new CodificaFin();
		due.setDescrizione("urca urca");
		due.setCodice("urca urca code");
		array.add(due);
		
		return array;*/
	}
	
	/**
	 * dClassTipoSiope da valorizzare con D_CLASS_TIPO_SIOPE_SPESA_I o con D_CLASS_TIPO_SIOPE_ENTRATA_I
	 * @param ente
	 * @param bilancio
	 * @param dClassTipoSiope
	 * @return
	 */
	private ArrayList<CodificaFin> findTipoSiopeInternal(Ente ente,Bilancio bilancio, String dClassTipoSiope) {
		
		List<SiacTClassFin> dtos2 = new ArrayList<SiacTClassFin>();
		
		// 	SIAC-4337
		int annoDiSistema = TimingUtils.getAnnoCorrente();
		int annoBilancio = 0;
		if(bilancio!=null){
			annoBilancio = bilancio.getAnno();
		} else {
			//se il chiamante non ci passa il bilancio assumiamo che sia quello di sistema
			//per retrocompatibilita'
			annoBilancio = annoDiSistema;
		}
		
		if(annoBilancio==annoDiSistema){
			Timestamp timestampInserimento=  getNow();
			dtos2 = siacTClassRepository.findByTipoAndEnte(ente.getUid(), timestampInserimento, dClassTipoSiope );
			return new ArrayList<CodificaFin>(convertiLista(dtos2, CodificaExtFin.class, FinMapId.SiacTClass_CodificaExtFin));	
		} else {
			
			Timestamp inizioAnno =  TimingUtils.getStartYearTs(annoBilancio);
			Timestamp fineAnno =  TimingUtils.getEndYearTs(annoBilancio);
			
			dtos2 = siacTClassRepository.findByTipoAndEnte(ente.getUid(), dClassTipoSiope,inizioAnno,fineAnno);
			return new ArrayList<CodificaFin>(convertiLista(dtos2, CodificaExtFin.class, FinMapId.SiacTClass_CodificaExtFin));	
		}
	}
	
	/**
	 * caricamento del programma
	 * @param codiceProgetto
	 * @param richiedente
	 * @return
	 */
	public EsitoCercaProgrammaDto cercaProgramma(String codiceProgetto, String codiceTipoProgramma, Integer idBilancio, Richiedente richiedente) {
		
		EsitoCercaProgrammaDto esitoCercaProgrammaDto = new EsitoCercaProgrammaDto();
		
		
//		Long contaProgrammi = siacTProgrammaRepository.verificaCodiceProgramma(codiceProgetto, richiedente.getAccount().getEnte().getUid(),timestampInserimento);
//		boolean trovato = false;
//		if(contaProgrammi>0){
//			trovato = true;
//		}
		//SiacTProgrammaFin siacTProgramma = siacTProgrammaRepository.getProgrammaByCodice(codiceProgetto, richiedente.getAccount().getEnte().getUid(), getNow());
		
		SiacTProgrammaFin siacTProgramma = siacTProgrammaRepository.findProgrammaByCodiceAndStatoOperativoAndEnteProprietarioId(
				codiceProgetto,
				StatoOperativoProgetto.VALIDO.getCodice(),
				codiceTipoProgramma,
				idBilancio,
				richiedente.getAccount().getEnte().getUid());
		
		// parte nuova
		if(null==siacTProgramma){
			esitoCercaProgrammaDto.setEsitenzaProgramma(false);
			esitoCercaProgrammaDto.setFlagRilevabileFPV(false);
		}else{
			
			// esiste il programma su db
			esitoCercaProgrammaDto.setEsitenzaProgramma(true);
		
			boolean rilevanteFPV = isRilevanteFPV(siacTProgramma);
			
			// setto l'esistenza o meno del flagFPV
			esitoCercaProgrammaDto.setFlagRilevabileFPV(rilevanteFPV);
			
			//VALORE COMPLESSIVO DEL PROGETTO:
			BigDecimal valoreComplessivo = getValoreComplessivo(siacTProgramma);
			esitoCercaProgrammaDto.setValoreComplessivo(valoreComplessivo);
		
		}
		//Termino restituendo l'oggetto di ritorno: 
        return esitoCercaProgrammaDto;
	}
	
	/**
	 * caricamento combo con i tipi di mutuo (boc, fideiussione...)
	 * @param ente
	 * @return
	 */
	public ArrayList<CodificaFin> findTipoMutuo(Ente ente){
		long currMillisec = getCurrentMillisecondsTrentaMaggio2017();
		Date dateInserimento = new Date(currMillisec);			
		Timestamp timestampInserimento= TimingUtils.convertiDataInTimeStamp(dateInserimento);	
		List<SiacDMutuoTipoFin> dtos = new ArrayList<SiacDMutuoTipoFin>();

		dtos = siacDMutuoTipoRepository.findDMutuoTipoValidoByEnte(ente.getUid(),timestampInserimento );

		return new ArrayList<CodificaFin>(convertiLista( dtos , CodificaFin.class, FinMapId.SiacDMutuoTipo_CodificaFin));
	}
	
	/**
	 * caricamento combo con i tipi di conto tesoreria
	 * @param ente
	 * @return
	 */
	public ArrayList<CodificaFin> findContoTesoreria(Ente ente){
		long currMillisec = getCurrentMillisecondsTrentaMaggio2017();
		Date dateInserimento = new Date(currMillisec);			
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);	
		List<SiacDContotesoreriaFin> dtos = new ArrayList<SiacDContotesoreriaFin>();

		dtos = siacDContotesoreriaRepository.findContotesoreriaByEnte(ente.getUid(),timestampInserimento);

		//System.out.println("dtos.size: " + dtos!=null && !dtos.isEmpty() ? dtos.size(): "lista nulla!");
		
		return new ArrayList<CodificaFin>(convertiLista( dtos , 
							  CodificaFin.class,
				              FinMapId.SiacDContoTesoreria_CodificaFin));
		
	}

	/**
	 * caricamento combo con i tipi di note del tesoriere
	 * @param ente
	 * @return
	 */
	public ArrayList<CodificaFin> findNoteTesoriere(Ente ente){
		long currMillisec = getCurrentMillisecondsTrentaMaggio2017();
		Date dateInserimento = new Date(currMillisec);			
		Timestamp timestampInserimento= TimingUtils.convertiDataInTimeStamp(dateInserimento);	
		List<SiacDNoteTesoriereFin> dtos = new ArrayList<SiacDNoteTesoriereFin>();

		dtos = siacDNoteTesoriereRepository.findNoteTesoriereByEnte(ente.getUid(), timestampInserimento);

		return new ArrayList<CodificaFin>(convertiLista( dtos , 
							  CodificaFin.class,
				              FinMapId.SiacDNoteTesoriere_CodificaFin));
		
	}

	/**
	 * caricamento combo con i codici bollo ammessi
	 * @param ente
	 * @return
	 */
	public ArrayList<CodificaFin> findCodiceBollo(Ente ente){
		long currMillisec = getCurrentMillisecondsTrentaMaggio2017();
		Date dateInserimento = new Date(currMillisec);			
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);	
		List<SiacDCodicebolloFin> dtos = new ArrayList<SiacDCodicebolloFin>();

		dtos = siacDCodiceBolloRepository.findDCodiceBolloValidiByEnte(ente.getUid(),timestampInserimento);
		
		ArrayList<CodificaFin> result = (ArrayList<CodificaFin>) convertiLista(dtos ,CodificaFin.class, FinMapId.SiacDCodiceBollo_CodificaFin);
		
		//Termino restituendo l'oggetto di ritorno: 
        return result;
	}
	
	/**
	 * cerca le commissioni di un ordinativo
	 * @param ente
	 * @return
	 */
	public ArrayList<CodificaFin> findCommissioniOrdinativo(Ente ente){
		long currMillisec = getCurrentMillisecondsTrentaMaggio2017();
		Date dateInserimento = new Date(currMillisec);			
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);	
		List<SiacDCommissioneTipoFin> dtos = new ArrayList<SiacDCommissioneTipoFin>();

		dtos = siacDCommissioneTipoRepository.findDCommissioniTipoValidoByEnte(ente.getUid(),timestampInserimento);

		return new ArrayList<CodificaFin>(convertiLista(dtos, CodificaFin.class, FinMapId.SiacDCommissioni_CodificaFin));
	}

	/**
	 * cerca tutti i tipo avviso per l'ente indicato
	 * @param ente
	 * @return
	 */
	// @SuppressWarnings("static-access")
	public ArrayList<CodificaFin> findTipoAvviso(Ente ente) {
		long currMillisec = getCurrentMillisecondsTrentaMaggio2017();
		Date dateInserimento = new Date(currMillisec);			
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);		
		List<SiacTClassFin> dtos2 = new ArrayList<SiacTClassFin>();
		dtos2 = siacTClassRepository.findByTipoAndEnte(ente.getUid(), timestampInserimento, Constanti.D_CLASS_TIPO_TIPO_AVVISO);
		return new ArrayList<CodificaFin>(convertiLista(dtos2, CodificaExtFin.class, FinMapId.SiacTClass_CodificaExtFin));	
	}
	
	/**
	 * cerca tutte le distintatipoS per l'ente indicato
	 * @param ente
	 * @return
	 */
	public ArrayList<CodificaFin> findDistintaTipoS(Ente ente){
		long currMillisec = getCurrentMillisecondsTrentaMaggio2017();
		Date dateInserimento = new Date(currMillisec);			
		Timestamp timestampInserimento= TimingUtils.convertiDataInTimeStamp(dateInserimento);	
		List<SiacDDistintaFin> dtos = new ArrayList<SiacDDistintaFin>();

		dtos = siacDDistintaRepository.findDistintaByCodTipo(ente.getUid(),timestampInserimento,"S");

		//System.out.println("dtos.size: " + dtos!=null && !dtos.isEmpty() ? dtos.size(): "lista nulla!");
		return new ArrayList<CodificaFin>(convertiLista(dtos, CodificaFin.class, FinMapId.SiacDDistinta_CodificaFin));
	}
	
	/**
	 * cerca tutte le distintatipoE per l'ente indicato
	 * @param ente
	 * @return
	 */
	public ArrayList<CodificaFin> findDistintaTipoE(Ente ente){
		long currMillisec = getCurrentMillisecondsTrentaMaggio2017();
		Date dateInserimento = new Date(currMillisec);			
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);	
		List<SiacDDistintaFin> dtos = new ArrayList<SiacDDistintaFin>();

		dtos = siacDDistintaRepository.findDistintaByCodTipo(ente.getUid(),timestampInserimento,"E");

		return new ArrayList<CodificaFin>(convertiLista(dtos, CodificaFin.class, FinMapId.SiacDDistinta_CodificaFin));
	}
	
	/**
	 * cerca gli stato operativi disponibili per l'ente indicato
	 * @param ente
	 * @return
	 */
	public ArrayList<CodificaFin> findStatoOperativoOrdinativo(Ente ente){
		long currMillisec = getCurrentMillisecondsTrentaMaggio2017();
		Date dateInserimento = new Date(currMillisec);			
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);		
		List<SiacDMovgestStatoFin> dtos2 = new ArrayList<SiacDMovgestStatoFin>();	
		dtos2 = siacDOrdinativoStatoRepository.findListaOrdinativoStatoOperativo(ente.getUid(),timestampInserimento);
		
		return new ArrayList<CodificaFin>(convertiLista(dtos2, CodificaFin.class, FinMapId.SiacDOrdinativoStato_CodificaFin));	
	}
	
	/**
	 * cerca gli stati operativi carta contabile per l'ente indicato
	 * @param ente
	 * @return
	 */
	public ArrayList<CodificaFin> findStatiOpCartaCont(Ente ente){
		long currMillisec = getCurrentMillisecondsTrentaMaggio2017();
		Date dateInserimento = new Date(currMillisec);			
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);	
		List<SiacDCartacontStatoFin> dtos = new ArrayList<SiacDCartacontStatoFin>();

		dtos = siacDCartacontStatoRepository.findDCartaContStatoValidiByEnte(ente.getUid(), timestampInserimento);

		return new ArrayList<CodificaFin>(convertiLista(dtos , CodificaFin.class, FinMapId.SiacDCartaContStato_CodificaFin));
	}

	/**
	 * caricamento combo con le valute estere usate nella carta
	 * @param ente
	 * @return
	 */
	public ArrayList<CodificaFin> findValuta(Ente ente){
		long currMillisec = getCurrentMillisecondsTrentaMaggio2017();
		Date dateInserimento = new Date(currMillisec);			
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);		
		List<SiacDValutaFin> dtos = new ArrayList<SiacDValutaFin>();	
		dtos = siacDValutaRepository.findDValutaValideByEnte(ente.getUid(),timestampInserimento);
		
		return new ArrayList<CodificaFin>(convertiLista(dtos, CodificaFin.class, FinMapId.SiacDValuta_CodificaFin));	
	}
	
	/**
	 * cerca le commissioni estero per l'ente indicato
	 * @param ente
	 * @return
	 */
	public ArrayList<CodificaFin> findCommissioniEstero(Ente ente){
		
		long currMillisec = getCurrentMillisecondsTrentaMaggio2017();
		Date dateInserimento = new Date(currMillisec);			
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);		
		List<SiacDCommissioniesteroFin> dtos = new ArrayList<SiacDCommissioniesteroFin>();	
		dtos = siacDCommissioniEsteroRepository.findDCommissioniEsteroValideByEnte(ente.getUid(),timestampInserimento);
		
		return new ArrayList<CodificaFin>(convertiLista(dtos, CodificaFin.class, FinMapId.SiacDCommissioniestero_CodificaFin));	
	}
	
	/**
	 * cerca i tipodocumento spesa per l'ente indicato
	 * @param ente
	 * @return
	 */
	public ArrayList<CodificaFin> findTipoDocumentoSpesa(Ente ente){
		long currMillisec = getCurrentMillisecondsTrentaMaggio2017();
		Date dateInserimento = new Date(currMillisec);			
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);		
		List<SiacDDocTipoFin> dtos = new ArrayList<SiacDDocTipoFin>();	
		dtos = siacDDocTipoRepository.findDDocTipoValidiByEnteAndCodFam(ente.getUid(),Constanti.D_DOC_TIPO_CARTA_CONTABILE_FAMIGLIA_SPESA, timestampInserimento);
		
		return new ArrayList<CodificaFin>(convertiLista(dtos, CodificaFin.class, FinMapId.SiacDDocumentoTipo_CodificaFin));	
	}
	
	/**
	 * carica il tipo documento indicato in input
	 * @param codiceTipoDocumento
	 * @param codiceFamiglia
	 * @param datiOperazione
	 * @return
	 */
	public TipoDocumento getTipoDocumento(String codiceTipoDocumento, String codiceFamiglia, DatiOperazioneDto datiOperazione){
 
		TipoDocumento tipoDocumento = new TipoDocumento();
		Integer idEnte = datiOperazione.getSiacTEnteProprietario().getEnteProprietarioId();
		
		SiacDDocTipoFin siacDDocTipo = siacDDocTipoRepository.findDDocTipoValidoByEnteAndCodAndCodFam(idEnte, codiceTipoDocumento, codiceFamiglia, datiOperazione.getTs());
				
		if(siacDDocTipo!=null){
			tipoDocumento.setUid(siacDDocTipo.getDocTipoId());
			tipoDocumento.setCodice(siacDDocTipo.getDocTipoCode());
			tipoDocumento.setDescrizione(siacDDocTipo.getDocTipoDesc());
		}
		
		return tipoDocumento;	
	}

	/**
	 * carica il codice bollo richiesto
	 * @param codBollo
	 * @param datiOperazione
	 * @return
	 */
	public CodiceBollo getCodiceBollo(String codBollo, DatiOperazioneDto datiOperazione){
 
		CodiceBollo codiceBollo = new CodiceBollo();
		Integer idEnte = datiOperazione.getSiacTEnteProprietario().getEnteProprietarioId();
		
		SiacDCodicebolloFin siacDCodicebollo = siacDCodiceBolloRepository.findDCodiceBolloValidoByEnteAndCode(idEnte, codBollo, datiOperazione.getTs());
				
		if(siacDCodicebollo!=null){
			codiceBollo.setUid(siacDCodicebollo.getCodbolloId());
			codiceBollo.setCodice(siacDCodicebollo.getCodbolloCode());
			codiceBollo.setDescrizione(siacDCodicebollo.getCodbolloDesc());
		}
		
		return codiceBollo;	
	}
	
	
	/**
	 * caricamento combo con i tipi di legame per la famiglia soggetto soggetti
	 * @param ente
	 * @return
	 */
	public ArrayList<CodificaFin> findTipoLegameSoggetto(Ente ente){
		List<SiacDRelazTipoFin> dtos = new ArrayList<SiacDRelazTipoFin>();
		dtos = siacDRelazTipoRepository.findRelazTipoLegameTipizzatoByEnte(ente.getUid(), CODE_TIPO_LEGAME_SOGGETTO);
		return new ArrayList<CodificaFin>(convertiLista( dtos , 
							  CodificaFin.class,
				              FinMapId.SiacDRelazTipo_CodificaFin));
	}
	
	
	
	/**
	 * caricamento combo tipo motivo per fase ROR (riaccertamento)
	 * @param ente
	 * @return
	 */
	public ArrayList<CodificaFin> findTipoMotivoROR(Ente ente){
		
		long currMillisec = getCurrentMillisecondsTrentaMaggio2017();
		
		Date dateInserimento = new Date(currMillisec);			
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);	
		
		List<SiacDModificaTipoFin> dtos = new ArrayList<SiacDModificaTipoFin>();
		
		dtos = siacDModificaTipoRepository.findListaMotiviRORValidiByEnte(ente.getUid(), timestampInserimento);
		
		return new ArrayList<CodificaFin>(convertiLista(dtos, 
				                                        CodificaFin.class, 
				                                        FinMapId.SiacDModificaTipo_CodificaFin));	
	}
	
	public void flushAndClearEntMng(){
		//REINTROITI DICEMBRE 2017 per fare vedere la liquidazione
		//appena creata al ricerca liquidazione:
		super.flushAndClearEntMng();
		//
	}
	
	public void flushEntMng(){
		//REINTROITI DICEMBRE 2017 per fare vedere la liquidazione
		//appena creata al ricerca liquidazione:
		super.flushEntMng();
		//
	}
	
}