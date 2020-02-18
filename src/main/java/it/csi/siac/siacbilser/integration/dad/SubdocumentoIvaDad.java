/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.SiacDIvaRegistrazioneTipoRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTIvaStampaRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTPeriodoRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTSubdocIvaNumRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTSubdocIvaRepository;
import it.csi.siac.siacbilser.integration.dao.SubdocumentoIvaDao;
import it.csi.siac.siacbilser.integration.entity.SiacDIvaRegistrazioneTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDSubdocIvaStato;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocIvaStato;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaStampa;
import it.csi.siac.siacbilser.integration.entity.SiacTPeriodo;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocIva;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocIvaNum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDIvaRegistrazioneTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDIvaStampaStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDIvaStampaTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDPeriodoTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDSubdocIvaStatoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.SubdocumentoIvaStatoConverter;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacbilser.integration.utility.function.SimpleJDBCFunctionInvoker;
import it.csi.siac.siacfin2ser.model.Periodo;
import it.csi.siac.siacfin2ser.model.StampaIva;
import it.csi.siac.siacfin2ser.model.StatoSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaModelDetail;
import it.csi.siac.siacfin2ser.model.TipoChiusura;
import it.csi.siac.siacfin2ser.model.TipoRegistrazioneIva;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;

/**
 * Data access delegate di un SubdocumentoIva.
 */
public class SubdocumentoIvaDad extends ExtendedBaseDadImpl {
	
	/** The subdocumento iva dao. */
	@Autowired
	protected SubdocumentoIvaDao subdocumentoIvaDao;
	
	/** The siac t subdoc iva repository. */
	@Autowired
	protected SiacTSubdocIvaRepository siacTSubdocIvaRepository;
	
	/** The siac t subdoc iva num repository. */
	@Autowired
	protected SiacTSubdocIvaNumRepository siacTSubdocIvaNumRepository;
	
	/** The siac d iva registrazione tipo repository. */
	@Autowired
	protected SiacDIvaRegistrazioneTipoRepository siacDIvaRegistrazioneTipoRepository;
	
	/** The subdocumento iva stato converter. */
	@Autowired
	protected SubdocumentoIvaStatoConverter subdocumentoIvaStatoConverter;
	
	// CR-3791
	/** The siac t iva stampa repository. */
	@Autowired
	protected SiacTIvaStampaRepository siacTIvaStampaRepository;
	/** The siac t periodo repository. */
	@Autowired
	protected SiacTPeriodoRepository siacTPeriodoRepository;
	
	@Autowired
	private SimpleJDBCFunctionInvoker fi;
	
	
	/** The eef. */
	@Autowired
	protected EnumEntityFactory eef;

	/**
	 * Elimina subdocumento iva.
	 *
	 * @param subdocumento the subdocumento
	 */
	public void eliminaSubdocumentoIva(SubdocumentoIva<?, ?, ?> subdocumento){
		SiacTSubdocIva siacTSubdocIva = new SiacTSubdocIva();
		siacTSubdocIva.setUid(subdocumento.getUid());
		//siacTSubdocIva.setLoginCancellazione(loginOperazione);
		subdocumentoIvaDao.delete(siacTSubdocIva);
	}
	
	/**
	 * Ottiene la lista di subdocumentiIva a partire da subdocumento iva, data protocollo, stato e famiglia del documento.
	 * 
	 * @param subdocIva               il subdocumento iva
	 * @param protocolloProvvisorioDa la data iniziale del protocollo provvisorio
	 * @param protocolloProvvisorioA  la data finale del protocollo provvisorio
	 * @param protocolloDefinitivoDa  la data iniziale del protocollo definitivo
	 * @param protocolloDefinitivoA   la data finale del protocollo definitivo
	 * @param siacDSubdocIvaStatoEnum lo stato del subdocumento iva
	 * @param siacDDocFamTipoEnums    la famiglia del documento
	 * 
	 * @return la lista dei SiacTSubdocIva corrispondenti ai parametri
	 */
	protected <SI extends SubdocumentoIva<?, ?, ?>> List<SiacTSubdocIva> ottieniListaNonQPIDByDataProtocolloAndStatoAndDocFamTipo(
			SI subdocIva,
			Date protocolloProvvisorioDa, Date protocolloProvvisorioA,
			Date protocolloDefinitivoDa, Date protocolloDefinitivoA,
			SiacDSubdocIvaStatoEnum siacDSubdocIvaStatoEnum, Collection<SiacDDocFamTipoEnum> siacDDocFamTipoEnums) {
		return subdocumentoIvaDao.ricercaSubdocumentoIva(subdocIva.getEnte().getUid(),
				siacDDocFamTipoEnums,
				mapToString(subdocIva.getAnnoEsercizio()),
				subdocIva.getProgressivoIVA(),
				siacDSubdocIvaStatoEnum,
				protocolloProvvisorioDa,
				protocolloProvvisorioA,
				protocolloDefinitivoDa,
				protocolloDefinitivoA,
				subdocIva.getRegistroIva() != null ? mapToUidIfNotZero(subdocIva.getRegistroIva()) : null);
	}
	
	/**
	 * Ottiene il numero di un nuovo subdocumento iva.
	 *
	 * @param anno the anno
	 * @return numero subdocumento iva
	 */
	@Transactional(propagation=Propagation.MANDATORY)
	public Integer staccaNumeroSubdocumento(Integer anno) {
		final String methodName = "staccaNumeroSubdocumento";
		log.debug(methodName, loginOperazione);
		SiacTSubdocIvaNum siacTSubdocIvaNum = siacTSubdocIvaNumRepository.findByAnnoAndEnte(anno, ente.getUid());
		
		Date now = new Date();		
		if(siacTSubdocIvaNum == null) {			
			siacTSubdocIvaNum = new SiacTSubdocIvaNum();
			SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
			siacTEnteProprietario.setUid(ente.getUid());
			siacTSubdocIvaNum.setSiacTEnteProprietario(siacTEnteProprietario);
			siacTSubdocIvaNum.setDataCreazione(now);
			siacTSubdocIvaNum.setDataInizioValidita(now);
			siacTSubdocIvaNum.setLoginOperazione(loginOperazione);
			siacTSubdocIvaNum.setSubdocivaNumero(1); //La numerazione parte da 1
			siacTSubdocIvaNum.setSubdocivaAnno(anno);
		}
		
		siacTSubdocIvaNum.setLoginOperazione(loginOperazione);	
		siacTSubdocIvaNum.setDataModifica(now);	
		
		siacTSubdocIvaNumRepository.saveAndFlush(siacTSubdocIvaNum);
		
		Integer numeroSubdocumentoIva = siacTSubdocIvaNum.getSubdocivaNumero();
		log.info(methodName, "returning numeroSubdocumentoIva: "+ numeroSubdocumentoIva);
		return numeroSubdocumentoIva;
	}
	
	/**
	 * Find sato subdocumento iva.
	 *
	 * @param subdocIva the subdoc iva
	 * @return the stato subdocumento iva
	 */
	public StatoSubdocumentoIva findStatoSubdocumentoIva(SubdocumentoIva<?, ?, ?> subdocIva) {
		SiacTSubdocIva siacTSubdocIva = siacTSubdocIvaRepository.findOne(subdocIva.getUid());
		if(siacTSubdocIva==null){
			throw new IllegalStateException("Impossibile trovare subdocumento iva con uid: "+subdocIva.getUid());
		}
		return subdocumentoIvaStatoConverter.convertFrom(siacTSubdocIva,null);
	}

	public TipoRegistrazioneIva findTipoRegistrazioneIvaIntrastat(){
		return findTipoRegistrazioneIva(SiacDIvaRegistrazioneTipoEnum.Intrastat);
		
	}
	
	public TipoRegistrazioneIva findTipoRegistrazioneIvaNormale() {		
		return findTipoRegistrazioneIva(SiacDIvaRegistrazioneTipoEnum.Normale);		
	}

	private TipoRegistrazioneIva findTipoRegistrazioneIva(SiacDIvaRegistrazioneTipoEnum tipo) {
		SiacDIvaRegistrazioneTipo siacDIvaRegistrazioneTipo = eef.getEntity(tipo, ente.getUid());		
		return mapNotNull(siacDIvaRegistrazioneTipo, TipoRegistrazioneIva.class, BilMapId.SiacDIvaRegistrazioneTipo_TipoRegistrazioneIva);
	}
	
	public void aggiornaDataENumeroOrdinativo(SubdocumentoIva<?, ?, ?> subdocumentoIva, Date dataEmissione,Integer numero) {
		SiacTSubdocIva siacTSubdocIva = siacTSubdocIvaRepository.findOne(subdocumentoIva.getUid());
		siacTSubdocIva.setSubdocivaDataOrdinativoadoc(dataEmissione);
		siacTSubdocIva.setSubdocivaNumordinativodoc(String.valueOf(numero));
		siacTSubdocIvaRepository.saveAndFlush(siacTSubdocIva);
	}
	
	/**
	 * Conta gli ordinativi associati al subdocumento iva
	 * @param subdocumentoIva il subdocumento
	 * @return il numero degli ordinativi associati
	 */
	public Long countOrdinativiAssociatiAQuoteDocumentoCollegato(SubdocumentoIva<?, ?, ?> subdocumentoIva) {
//		Collection<String> siacDDocFamTipoCodes = new HashSet<String>();
//		siacDDocFamTipoCodes.add(SiacDDocFamTipoEnum.IvaSpesa.getCodice());
//		siacDDocFamTipoCodes.add(SiacDDocFamTipoEnum.IvaEntrata.getCodice());
		
		return siacTSubdocIvaRepository.countSiacTOrdinativoBySubdocivaId(subdocumentoIva.getUid()); //, siacDDocFamTipoCodes);
	}
	
	/**
	 * Conta le liquidazioni associate al subdocumento iva
	 * @param subdocumentoIva il subdocumento
	 * @return il numero delle liquidazioni associate
	 */
	public Long countLiquidazioniAssociateAQuoteDocumentoCollegato(SubdocumentoIva<?, ?, ?> subdocumentoIva) {
//		Collection<String> siacDDocFamTipoCodes = new HashSet<String>();
//		siacDDocFamTipoCodes.add(SiacDDocFamTipoEnum.IvaSpesa.getCodice());
//		siacDDocFamTipoCodes.add(SiacDDocFamTipoEnum.IvaEntrata.getCodice());
		
		return siacTSubdocIvaRepository.countSiacTLiquidazioneBySubdocivaId(subdocumentoIva.getUid()/*, siacDDocFamTipoCodes*/);
	}
	
	/**
	 * Ricerca le stampe iva a partire dal subdocumento iva
	 * @param subdocumentoIva il subdocumento iva
	 * @return le stampe collegate ai dati del subdocumento (registro + periodo)
	 */
	public List<StampaIva> findStampaIvaBySubdocumentoIva(SubdocumentoIva<?, ?, ?> subdocumentoIva, Date dataProtProvAttuale, Date dataProtDefAttuale) {
		List<SiacTIvaStampa> res = new ArrayList<SiacTIvaStampa>();
		TipoChiusura tc = subdocumentoIva.getRegistroIva().getGruppoAttivitaIva().getTipoChiusura();
		Integer enteProprietarioId = subdocumentoIva.getEnte().getUid();
		//stampe del periodo in cui voglio inserire il documento iva
		if(subdocumentoIva.getDataProtocolloProvvisorio() != null) {
			List<SiacTIvaStampa> stampe = ottieniStampe(subdocumentoIva.getDataProtocolloProvvisorio(), tc, enteProprietarioId, subdocumentoIva.getRegistroIva().getUid());
			res.addAll(stampe);
		}
		if(subdocumentoIva.getDataProtocolloDefinitivo() != null) {
			List<SiacTIvaStampa> stampe = ottieniStampe(subdocumentoIva.getDataProtocolloDefinitivo(), tc, enteProprietarioId, subdocumentoIva.getRegistroIva().getUid());
			res.addAll(stampe);
		}
		//stamper del periodo da cui proviene il documento iva
		if(dataProtProvAttuale != null) {
			List<SiacTIvaStampa> stampe = ottieniStampe(dataProtProvAttuale, tc, enteProprietarioId, subdocumentoIva.getRegistroIva().getUid());
			res.addAll(stampe);
		}
		if(dataProtDefAttuale != null) {
			List<SiacTIvaStampa> stampe = ottieniStampe(dataProtDefAttuale, tc, enteProprietarioId, subdocumentoIva.getRegistroIva().getUid());
			res.addAll(stampe);
		}
		
		return convertiLista(res, StampaIva.class, BilMapId.SiacTIvaStampa_StampaIva_Base);
	}
	
	
	
	private List<SiacTIvaStampa> ottieniStampe(Date data, TipoChiusura tc, Integer enteProprietarioId, Integer ivaregId) {
		Periodo periodo = Periodo.byDateAndTipoChiusura(data, tc);
		SiacDPeriodoTipoEnum sdpte = SiacDPeriodoTipoEnum.byPeriodo(periodo);
		SiacTPeriodo siacTPeriodo = siacTPeriodoRepository.findByDataAndEnteProprietarioAndTipoPeriodo(data, enteProprietarioId, sdpte.getCodice());
		if(siacTPeriodo == null) {
			throw new IllegalArgumentException("Nessun periodo corrispondente al tipo chiusura " + tc.getDescrizione() + " e alla data " + Utility.formatDate(data));
		}
		List<SiacTIvaStampa> stampe = siacTIvaStampaRepository.findByRegistroEPeriodoETipoStampaIvaEStato(ivaregId, siacTPeriodo.getUid(),
				SiacDIvaStampaTipoEnum.Registro.getCodice(), SiacDIvaStampaStatoEnum.Definitiva.getCodice());
		return stampe;
	}
	
	/**
	 * Conta i subdoc iva con pari numero protocollo definitivo
	 * @param subdocIva il subdoc
	 * @return il numero di subdoc con pari numero protocollo definitivo
	 */
	public Long contaSubdocumentiIvaConPariNumeroProtocolloDefinitivoEDiversoUid(SubdocumentoIva<?, ?, ?> subdocIva) {
		if(subdocIva.getNumeroProtocolloDefinitivo() == null) {
			return Long.valueOf(0);
		}
		return siacTSubdocIvaRepository.countBySubdocivaProtDef(subdocIva.getUid(), subdocIva.getNumeroProtocolloDefinitivo().toString(), subdocIva.getEnte().getUid());
	}
	/**
	 * Conta i subdoc iva con pari numero protocollo provvisorio
	 * @param subdocIva il subdoc
	 * @return il numero di subdoc con pari numero protocollo provvisorio
	 */
	public Long contaSubdocumentiIvaConPariNumeroProtocolloProvvisorioEDiversoUid(SubdocumentoIva<?, ?, ?> subdocIva) {
		if(subdocIva.getNumeroProtocolloProvvisorio() == null) {
			return Long.valueOf(0);
		}
		return siacTSubdocIvaRepository.countBySubdocivaProtProv(subdocIva.getUid(), subdocIva.getNumeroProtocolloProvvisorio().toString(), subdocIva.getEnte().getUid());
	}
	
	/**
	 * 
	 * @param uid
	 * @param anno
	 * @param periodo
	 */
	public Long countSubdocIvaByRegistroAndBilancioAndPeriodoProv(int uidRegistro, int anno, Periodo periodo) {
		final String methodName = "countSubdocIvaByRegistroAndBilancioAndPeriodoProv";
		Date inizioPeriodo = periodo.getInizioPeriodo(anno);
		Date finePeriodo = periodo.getFinePeriodo(anno);
		
		Long result = siacTSubdocIvaRepository.countByUidRegistroAndInizioFinePeriodoProtProv(uidRegistro, inizioPeriodo, finePeriodo, ente.getUid());
		log.debug(methodName, "returning: "+result);
		return result;
		
	}
	
	/**
	 * 
	 * @param uid
	 * @param anno
	 * @param periodo
	 */
	public Long countSubdocIvaByRegistroAndBilancioAndPeriodoDef(int uidRegistro, int anno, Periodo periodo) {
		final String methodName = "countSubdocIvaByRegistroAndBilancioAndPeriodoDef";
		Date inizioPeriodo = periodo.getInizioPeriodo(anno);
		Date finePeriodo = periodo.getFinePeriodo(anno);
		
		Long result = siacTSubdocIvaRepository.countByUidRegistroAndInizioFinePeriodoProtDef(uidRegistro, inizioPeriodo, finePeriodo, ente.getUid());
		log.debug(methodName, "returning: "+result);
		return result;
	}
	
	/**
	 * Annulla subdociva def.
	 *
	 * @param subdocIva the subdoc iva
	 */
	public void annullaSubdocivaDef(List<SubdocumentoIva<?, ?, ?>> subdocIva) {
		for (SubdocumentoIva<?, ?, ?> subdocumentoIva : subdocIva) {
			SiacTSubdocIva siacTSubdocIva = siacTSubdocIvaRepository.findOne(subdocumentoIva.getUid());
			siacTSubdocIva.setSubdocivaDataOrdinativoadoc(null);
			siacTSubdocIva.setSubdocivaNumordinativodoc(null);
			siacTSubdocIva.setSubdocivaDataProtDef(null);
			siacTSubdocIva.setSubdocivaNumero(null);
			aggiornaStatoSubdocIva(siacTSubdocIva, SiacDSubdocIvaStatoEnum.byCodice(StatoSubdocumentoIva.PROVVISORIO.getCodice()));
			siacTSubdocIvaRepository.saveAndFlush(siacTSubdocIva);
		}
	}

	private void aggiornaStatoSubdocIva(SiacTSubdocIva siacTSubdocIva, SiacDSubdocIvaStatoEnum enumStato) {
		Date now = new Date();
		for (SiacRSubdocIvaStato siacRSubdocIvaStato : siacTSubdocIva.getSiacRSubdocIvaStatos()) {
			if(siacRSubdocIvaStato.getDataCancellazione() != null) {
				//non devo aggiornare lo stato, esso e' gia' quello necessario
				continue;
			}
			if(enumStato.getCodice().equals(siacRSubdocIvaStato.getSiacDSubdocIvaStato().getSubdocivaStatoCode())) {
				//non devo aggiornare lo stato, esso e' gia' quello necessario
				return;
			}
			siacRSubdocIvaStato.setDataCancellazioneIfNotSet(now);
			siacRSubdocIvaStato.setLoginOperazione(loginOperazione);
		}
		
		SiacRSubdocIvaStato siacRSubdocIvaStato = new SiacRSubdocIvaStato();
		siacRSubdocIvaStato.setSiacTEnteProprietario(siacTSubdocIva.getSiacTEnteProprietario());
		siacRSubdocIvaStato.setLoginOperazione(loginOperazione);
		siacRSubdocIvaStato.setDataCreazione(now);
		siacRSubdocIvaStato.setDataInizioValidita(now);
		SiacDSubdocIvaStato siacDSubdocIvaStato = eef.getEntity(enumStato, siacTSubdocIva.getSiacTEnteProprietario().getUid());
		siacRSubdocIvaStato.setSiacDSubdocIvaStato(siacDSubdocIvaStato);
		siacTSubdocIva.addSiacRSubdocIvaStato(siacRSubdocIvaStato);
	}

	
	/**
	 * Carica subdocumenti iva by ordinativo.
	 *
	 * @param ordinativo the ordinativo
	 * @return the list
	 */
	@SuppressWarnings("unchecked")
	public List<SubdocumentoIva<?,?,?>> caricaSubdocumentiIvaByOrdinativo(Ordinativo ordinativo, SubdocumentoIvaModelDetail...modelDetails ){
		List<SiacTSubdocIva> siacTSubdocIvas = subdocumentoIvaDao.ricercaSubdocumentoIvaByOrdinativo(ordinativo.getUid(), SiacDSubdocIvaStatoEnum.byStatoOperativo(StatoSubdocumentoIva.PROVVISORIO_DEFINITIVO).getCodice());
		return convertiLista(siacTSubdocIvas, (Class<SubdocumentoIva<?, ?, ?>>)(Class<?>)SubdocumentoIva.class, BilMapId.SiacTSubdocIva_SubdocumentoIva_Minimal, Converters.byModelDetails(modelDetails) );
	}
	
	/**
	 * Annulla subdocumenti ed allinea registro.
	 *
	 * @param subdocIvas the subdoc ivas
	 */
	public void annullaSubdocumentiEdAllineaRegistro(List<SubdocumentoIva<?,?,?>> subdocIvas, Integer annoBilancio) {
		final String methodName ="annullaSubdocumentiEdAllineaRegistro";
		for (SubdocumentoIva<?, ?, ?> subdocumentoIva : subdocIvas) {
			String descrSubdociva = new StringBuilder().append("uid: ").append("" + subdocumentoIva.getUid()).toString();
			log.debug(methodName, "chiamo la function di annullamento dati iva per il subdocumento iva: " + descrSubdociva);
			fi.invokeFunctionSingleResult("fnc_siac_annulla_registrazione_iva", subdocumentoIva.getUid(), annoBilancio.toString(), ente.getUid(), loginOperazione);
		}
		
	}

}
