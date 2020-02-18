/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.elementobilancio;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dao.SiacTBilElemRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTClassDao;
import it.csi.siac.siacbilser.integration.dao.SiacTClassRepository;
import it.csi.siac.siacbilser.integration.dao.base.ExtendedJpaDao;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemAttr;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemCategoria;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemClass;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemRelTempo;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemStato;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDet;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.integration.utility.CompareOperator;

// TODO: Auto-generated Javadoc
/**
 * The Class CapitoloDaoImpl.
 */
@Component
@Transactional
public class CapitoloDaoImpl extends ExtendedJpaDao<SiacTBilElem, Integer> implements CapitoloDao {
	
	/** The siac t bil elem repository. */
	@Autowired
	private SiacTBilElemRepository siacTBilElemRepository;
	
	/** The siac t class dao. */
	@Autowired
	private SiacTClassDao siacTClassDao;
	
	/** The siac t class repository. */
	@Autowired
	private SiacTClassRepository siacTClassRepository;
	
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.elementobilancio.CapitoloDao#create(it.csi.siac.siacbilser.integration.entity.SiacTBilElem)
	 */
	@Override
	public SiacTBilElem create(SiacTBilElem bilElem) {
		Date now = new Date();
		bilElem.setDataModificaInserimento(now);
		
		
		if(bilElem.getSiacRBilElemStatos()!=null){
			for(SiacRBilElemStato stato : bilElem.getSiacRBilElemStatos()){
				stato.setDataModificaInserimento(now);
			}
		}
		
		if(bilElem.getSiacTBilElemDets()!=null){
			for(SiacTBilElemDet deb  :bilElem.getSiacTBilElemDets()){
				deb.setDataModificaInserimento(now);		
			}
		}
		
		if(bilElem.getSiacRBilElemClasses()!=null){
			for(SiacRBilElemClass clas  :bilElem.getSiacRBilElemClasses()){
				clas.setDataModificaInserimento(now);			
			}
		}
		
		if(bilElem.getSiacRBilElemAttrs()!=null){
			for(SiacRBilElemAttr attr  :bilElem.getSiacRBilElemAttrs()){
				attr.setDataModificaInserimento(now);			
			}
		}
		
		if(bilElem.getSiacRBilElemCategorias()!=null){
			for(SiacRBilElemCategoria attr  :bilElem.getSiacRBilElemCategorias()){
				attr.setDataModificaInserimento(now);			
			}
		}
		
		bilElem.setElemId(null);
		
		super.save(bilElem);
		//bilElemRepository.saveAndFlush(bilElem);
		
		return bilElem;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.dao.base.JpaDao#update(java.lang.Object)
	 */
	@Override
	public SiacTBilElem update(SiacTBilElem bilElem) {	
		
		SiacTBilElem bilElemAttuale = this.findById(bilElem.getUid());
				
		Date now = new Date();
		bilElem.setDataModificaAggiornamento(now);
		
		if(bilElem.getSiacRBilElemStatos()!=null){
			for(SiacRBilElemStato stato : bilElem.getSiacRBilElemStatos()){
				stato.setDataModificaAggiornamento(now);
				//purtroppo va testato lo zero perchè dalla conversione da int a Integer arriva uno 0 ;(
				if(stato.getBilElemStatoId()==null || stato.getBilElemStatoId()==0){ 
					stato.setBilElemStatoId(trovaVecchioIdElemStato(bilElemAttuale));
				}
			}
		}
		
		if(bilElem.getSiacTBilElemDets()!=null) {
			for(SiacTBilElemDet deb  :bilElem.getSiacTBilElemDets()){
				deb.setDataModificaAggiornamento(now);
				
				if(deb.getElemDetId()==null || deb.getElemDetId()==0){
					Integer elemDetId = trovaVecchioIdElemDet(deb,bilElem.getUid());
					deb.setElemDetId(elemDetId);
				}
			}
		}
		
		if(bilElem.getSiacRBilElemClasses()!=null) {
			for(SiacRBilElemClass rclassNew : bilElem.getSiacRBilElemClasses()){
				rclassNew.setDataModificaAggiornamento(now);				
				
				if(rclassNew.getElemClassifId()==null || rclassNew.getElemClassifId()==0){
					Integer classifIdNew = rclassNew.getSiacTClass().getClassifId();
					Integer elemClassifId = trovaVecchioIdDiRelazionePerIlClassificatoreDellaStessaFamigliaOTipoDiQuelloPassato(classifIdNew, bilElem.getUid());
					rclassNew.setElemClassifId(elemClassifId);
					
					
						
				}
				
			}
		}
		
		
		if(bilElem.getSiacRBilElemAttrs()!=null){
			for(SiacRBilElemAttr attrNew  :bilElem.getSiacRBilElemAttrs()){
				attrNew.setDataModificaAggiornamento(now);		
				
				if(attrNew.getBilElemAttrId()==null || attrNew.getBilElemAttrId()==0){
					String attrCodeTipoNew = attrNew.getSiacTAttr().getAttrCode();
					Integer bilElemAttrId = trovaVecchioIdDiRelazioneAttributo(attrCodeTipoNew, bilElem.getUid());
					attrNew.setBilElemAttrId(bilElemAttrId);
						
				}
			}
			
			
		}
		
		//cancello vecchi id di relazione
		if(bilElemAttuale.getSiacRBilElemRelTempos()!=null) {
			for(SiacRBilElemRelTempo r : bilElemAttuale.getSiacRBilElemRelTempos()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(bilElemAttuale.getSiacRBilElemCategorias()!=null){
			for(SiacRBilElemCategoria attr  :bilElemAttuale.getSiacRBilElemCategorias()){
				attr.setDataCancellazioneIfNotSet(now);			
			}
		}
		
		//aggiungo i nuovi id di relazione
		if(bilElem.getSiacRBilElemCategorias()!=null){
			for(SiacRBilElemCategoria attr  :bilElem.getSiacRBilElemCategorias()){
				attr.setDataModificaInserimento(now);			
			}
		}
		
		if(bilElem.getSiacRBilElemRelTempos()!=null) {
			for(SiacRBilElemRelTempo r : bilElem.getSiacRBilElemRelTempos()){
				r.setDataModificaInserimento(now);
			}
		}
		
		return super.update(bilElem);
	}
	
	

	/**
	 * Aggiorna esclusivamente la relazione tra il capitolo e i classificatori lasciando invariato il resto.
	 *
	 * @param bilElem the bil elem
	 */
	public void aggiornaClassificatori(SiacTBilElem bilElem) {
		final String methodName = "aggiornaClassificatori";
		Date now = new Date();	
		
		if(bilElem.getSiacRBilElemClasses()!=null) {
			for(SiacRBilElemClass rclassNew : bilElem.getSiacRBilElemClasses()){
				rclassNew.setDataModificaAggiornamento(now);				
				
				if(rclassNew.getElemClassifId()==null || rclassNew.getElemClassifId()==0){
					Integer classifIdNew = rclassNew.getSiacTClass().getClassifId();
					Integer elemClassifId = trovaVecchioIdDiRelazionePerIlClassificatoreDellaStessaFamigliaOTipoDiQuelloPassato(classifIdNew, bilElem.getUid());
					rclassNew.setElemClassifId(elemClassifId);
				}
				log.debug(methodName,"Try to merge: rclass_id:"+rclassNew.getUid() + " bil_elem_id:"+rclassNew.getSiacTBilElem().getElemId() 
				   + " classif_id: "+rclassNew.getSiacTClass().getUid());
				
				entityManager.merge(rclassNew);
				
			}
		}		
		
	}

	/**
	 * Trova vecchio id di relazione attributo.
	 *
	 * @param attrCodeTipoNew the attr code tipo new
	 * @param bilElemAttuale the bil elem attuale
	 * @return the integer
	 */
	private Integer trovaVecchioIdDiRelazioneAttributo(String attrCodeTipoNew, Integer bilElemAttuale) {
		return siacTBilElemRepository.findIdBilElemAttrAssociatoACapitoloByTipoAttrCode(bilElemAttuale, attrCodeTipoNew);
	}

	/**
	 * Trova vecchio id elem stato.
	 *
	 * @param bilElemAttuale the bil elem attuale
	 * @return the integer
	 */
	private Integer trovaVecchioIdElemStato(SiacTBilElem bilElemAttuale) {
		return bilElemAttuale.getSiacRBilElemStatos().get(0).getBilElemStatoId();
	}
	
	
	/**
	 * Trova vecchio id elem det.
	 *
	 * @param deb the deb
	 * @param elemId the elem id
	 * @return the integer
	 */
	private Integer trovaVecchioIdElemDet(SiacTBilElemDet deb, Integer elemId) {
		
		return siacTBilElemRepository.findIdElemDetAssiociatoACapitoloByTipo(elemId, deb.getSiacDBilElemDetTipo().getElemDetTipoCode(), deb.getSiacTPeriodo());
		
	}

	/**
	 * Restituisce la chiave di SiacRBilElemClass di un classificatore dello 
	 * stesso tipo/famiglia di classifIdNew associato al capitolo il cui id è bilElemId.
	 *
	 * @param classifIdNew the classif id new
	 * @param bilElemId the bil elem id
	 * @return the integer
	 */
	private Integer trovaVecchioIdDiRelazionePerIlClassificatoreDellaStessaFamigliaOTipoDiQuelloPassato(Integer classifIdNew, Integer bilElemId) {		
		final String methodName = "trovaVecchioIdDiRelazionePerIlClassificatoreDellaStessaFamigliaOTipoDiQuelloPassato";
		
		String famigliaCode = siacTClassRepository.findCodiceFamigliaClassificatoreByClassifId(classifIdNew);
		if(famigliaCode == null){
			String classifTipoCode = siacTClassRepository.findCodiceTipoClassificatoreByClassifId(classifIdNew);
			Integer result = siacTBilElemRepository.findRBilElemClassIdByElemIdAndTipoCode(bilElemId, classifTipoCode);
			log.info(methodName, "returning: "+result + " for classifIdNew: "+classifIdNew + " [Tipo: "+classifTipoCode+"]");
			return result;
			
		}
		Integer result =  siacTBilElemRepository.findRBilElemClassIdByElemIdAndFamiglia(bilElemId, famigliaCode);
		log.info(methodName, "returning: "+result + " for classifIdNew: "+classifIdNew + " [Famiglia: "+famigliaCode+"]");
		return result;
		
		
		
	}
	
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.elementobilancio.CapitoloDao#deleteLogical(it.csi.siac.siacbilser.integration.entity.SiacTBilElem)
	 */
	public SiacTBilElem deleteLogical(SiacTBilElem bilElem) {
		
		Date now = new Date();		
		//capitoloUscitaPrevisione.setDataFineValidita(now);
		bilElem.setDataCancellazione(now);
		
		if(bilElem.getSiacRBilElemStatos()!=null){
			for(SiacRBilElemStato stato : bilElem.getSiacRBilElemStatos()){				
				//stato.setDataFineValidita(now);
				stato.setDataCancellazione(now);
			}
		}
		
		if(bilElem.getSiacTBilElemDets()!=null){
			for(SiacTBilElemDet deb  :bilElem.getSiacTBilElemDets()){				
				//deb.setDataFineValidita(now);
				deb.setDataCancellazione(now);
			}
		}
		
		if(bilElem.getSiacRBilElemClasses()!=null){
			for(SiacRBilElemClass clas  :bilElem.getSiacRBilElemClasses()){			
				//clas.setDataModifica(now);
				//clas.setDataFineValidita(now);
				clas.setDataCancellazione(now);			
			}
		}
		
		
		return super.update(bilElem);
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.elementobilancio.CapitoloDao#ricercaSinteticaCapitolo(it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario, java.lang.String, it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<SiacTBilElem> ricercaSinteticaCapitolo(SiacTEnteProprietario enteDto, String annoEsercizio,SiacDBilElemTipoEnum tipoCapitolo, Integer uidCategoriaCapitolo, String codiceCategoriaCapitolo, String annoCapitolo,
			String numeroCapitolo, String numeroArticolo, String numeroUEB, String stato,
			
			String exAnnoCapitolo,
			String exNumeroCapitolo,
			String exNumeroArticolo,
			String exNumeroUEB,
			
			String faseBilancio, 
			String descrizioneCapitolo,	
			String descrizioneArticolo,
			
			String flagAssegnabile,
			String flagFondoSvalutazioneCrediti,
			String flagFunzioniDelegate,
			String flagPerMemoria,
			String flagRilevanteIva,
			String flagTrasferimentoOrganiComunitari,	
			String flagEntrateRicorrenti,
			String flagFondoPluriennaleVinc,
			
			//classificatori generici
			String codiceTipoFinanziamento,
			String codiceTipoFondo,
			String codiceTipoVincolo,			
			String codiceRicorrenteEntrata, String codiceRicorrenteSpesa,
			String codicePerimetroSanitarioEntrata, String codicePerimetroSanitarioSpesa,
			String codiceTransazioneUnioneEuropeaEntrata, String codiceTransazioneUnioneEuropeaSpesa,
			String codicePoliticheRegionaliUnitarie, 
			String codiceClassificatoreGenerico1,
			String codiceClassificatoreGenerico2,
			String codiceClassificatoreGenerico3,
			String codiceClassificatoreGenerico4,
			String codiceClassificatoreGenerico5,
			String codiceClassificatoreGenerico6,
			String codiceClassificatoreGenerico7,
			String codiceClassificatoreGenerico8,
			String codiceClassificatoreGenerico9,
			String codiceClassificatoreGenerico10,
			
			String codiceClassificatoreGenerico31,
			String codiceClassificatoreGenerico32,
			String codiceClassificatoreGenerico33,
			String codiceClassificatoreGenerico34,
			String codiceClassificatoreGenerico35,
			String codiceClassificatoreGenerico36,
			String codiceClassificatoreGenerico37,
			String codiceClassificatoreGenerico38,
			String codiceClassificatoreGenerico39,
			String codiceClassificatoreGenerico40,
			String codiceClassificatoreGenerico41,
			String codiceClassificatoreGenerico42,
			String codiceClassificatoreGenerico43,
			String codiceClassificatoreGenerico44,
			String codiceClassificatoreGenerico45,
			String codiceClassificatoreGenerico46,
			String codiceClassificatoreGenerico47,
			String codiceClassificatoreGenerico48,
			String codiceClassificatoreGenerico49,
			String codiceClassificatoreGenerico50,
			
			//classificatori gerarchici
			String codicePianoDeiConti,
			String codiceCofog, String codiceTipoCofog,
			String codiceStruttAmmCont, String codiceTipoStruttAmmCont,
			String codiceSiopeEntrata, String codiceTipoSiopeEntrata,
			String codiceSiopeSpesa, String codiceTipoSiopeSpesa,
			String codiceMissione, String codiceProgrmma,
			String codiceTitoloSpesa, String codiceMacroaggregato,
			String codiceTitoloEntrata, String codiceTipologia, String codiceCategoria,
			
			Integer attoleggeNumero,
			String attoleggeAnno,
			String attoleggeArticolo,
			String attoleggeComma,
			String attoleggePunto,
			String attoleggeTipoCode,
			// SIAC-4088
			Boolean collegatoFondiDubbiaEsigibilita,
						
			Pageable pageable){
		
		StringBuilder jpql = new StringBuilder();
		Map<String,Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT cup.* ");
		componiQueryRicercaSinteticaNativaCapitolo(jpql, param,
				enteDto.getUid(),
				annoEsercizio,
				tipoCapitolo.getCodice(),
				uidCategoriaCapitolo,
				codiceCategoriaCapitolo,
				annoCapitolo,
				numeroCapitolo,
				numeroArticolo,
				numeroUEB,
				stato,
				exAnnoCapitolo,
				exNumeroCapitolo,
				exNumeroArticolo,
				exNumeroUEB,
				faseBilancio,
				descrizioneCapitolo,
				descrizioneArticolo,
				flagAssegnabile,
				flagFondoSvalutazioneCrediti,
				flagFunzioniDelegate,
				flagPerMemoria,
				flagRilevanteIva,
				flagTrasferimentoOrganiComunitari,
				flagEntrateRicorrenti,
				flagFondoPluriennaleVinc,
				codiceTipoFinanziamento,
				codiceTipoFondo,
				codiceTipoVincolo,
				codiceRicorrenteEntrata,
				codiceRicorrenteSpesa,
				codicePerimetroSanitarioEntrata,
				codicePerimetroSanitarioSpesa,
				codiceTransazioneUnioneEuropeaEntrata,
				codiceTransazioneUnioneEuropeaSpesa,
				codicePoliticheRegionaliUnitarie,
				codiceClassificatoreGenerico1,
				codiceClassificatoreGenerico2,
				codiceClassificatoreGenerico3,
				codiceClassificatoreGenerico4,
				codiceClassificatoreGenerico5,
				codiceClassificatoreGenerico6,
				codiceClassificatoreGenerico7,
				codiceClassificatoreGenerico8,
				codiceClassificatoreGenerico9,
				codiceClassificatoreGenerico10,
				codiceClassificatoreGenerico31,
				codiceClassificatoreGenerico32,
				codiceClassificatoreGenerico33,
				codiceClassificatoreGenerico34,
				codiceClassificatoreGenerico35,
				codiceClassificatoreGenerico36,
				codiceClassificatoreGenerico37,
				codiceClassificatoreGenerico38,
				codiceClassificatoreGenerico39,
				codiceClassificatoreGenerico40,
				codiceClassificatoreGenerico41,
				codiceClassificatoreGenerico42,
				codiceClassificatoreGenerico43,
				codiceClassificatoreGenerico44,
				codiceClassificatoreGenerico45,
				codiceClassificatoreGenerico46,
				codiceClassificatoreGenerico47,
				codiceClassificatoreGenerico48,
				codiceClassificatoreGenerico49,
				codiceClassificatoreGenerico50,
				findFigliClassificatoreIdsPianoDeiConti(annoEsercizio, enteDto, codicePianoDeiConti),
				findFigliClassificatoreIdsCofog(annoEsercizio, enteDto, codiceCofog, codiceTipoCofog),
				findFigliClassificatoreIdsStrutturaAmministrativaContabile(annoEsercizio, enteDto, codiceStruttAmmCont, codiceTipoStruttAmmCont),
				findFigliClassificatoreIdsSiopeEntrata(annoEsercizio, enteDto, codiceSiopeEntrata, codiceTipoSiopeEntrata),
				findFigliClassificatoreIdsSiopeSpesa(annoEsercizio, enteDto, codiceSiopeSpesa, codiceTipoSiopeSpesa),
				findFigliClassificatoreIdsMissioneProgramma(annoEsercizio, enteDto, codiceMissione, codiceProgrmma), 
				findFigliClassificatoreIdsTitoloUscitaMacroaggregato(annoEsercizio, enteDto, codiceTitoloSpesa, codiceMacroaggregato), 
				findFigliClassificatoreIdsEntrataTipologiaCategoria(annoEsercizio, enteDto, codiceTitoloEntrata, codiceTipologia, codiceCategoria),
				attoleggeNumero,
				attoleggeAnno,
				attoleggeArticolo,
				attoleggeComma,
				attoleggePunto,
				attoleggeTipoCode,
				collegatoFondiDubbiaEsigibilita);
		
		jpql.append(" ORDER BY cup.elem_code, cup.elem_code2, cup.elem_code3 ");
		
		return getNativePagedList(jpql.toString(), param, pageable, SiacTBilElem.class);
	}

	/**
	 * Find figli classificatore ids entrata tipologia categoria.
	 *
	 * @param siacTEnteProprietario the siac t ente proprietario
	 * @param codiceTitoloEntrata the codice titolo entrata
	 * @param codiceTipologia the codice tipologia
	 * @param codiceCategoria the codice categoria
	 * @return the list
	 */
	private List<Integer> findFigliClassificatoreIdsEntrataTipologiaCategoria(String annoEsercizio, SiacTEnteProprietario siacTEnteProprietario, String codiceTitoloEntrata, String codiceTipologia, String codiceCategoria) {
		//EntrataTitolitipologiecategorie	-> TitoloEntrata, Tipologia, Categoria (solo Entrata)			
//		String codiceTitoloEntrataTipologiaCategoria = codiceCategoria;
//		SiacDClassTipoEnum tipoTitoloEntrataTipologiaCategoria = SiacDClassTipoEnum.Categoria;
//		if(codiceTitoloEntrataTipologiaCategoria==null){
//			codiceTitoloEntrataTipologiaCategoria = codiceTipologia;
//			tipoTitoloEntrataTipologiaCategoria = SiacDClassTipoEnum.Tipologia;
//			if(codiceTitoloEntrataTipologiaCategoria==null){
//				codiceTitoloEntrataTipologiaCategoria = codiceTitoloEntrata;
//				tipoTitoloEntrataTipologiaCategoria = SiacDClassTipoEnum.TitoloEntrata;
//			}
//		}	
//		List<Integer> classifIdTitoloEntrataTipologiaCategoria = siacTClassDao.findFigliClassificatoreIds(codiceTitoloEntrataTipologiaCategoria, tipoTitoloEntrataTipologiaCategoria, SiacDClassFamEnum.EntrataTitolitipologiecategorie);
		
		
		List<Integer> classifIdTitoloEntrataTipologiaCategoria = siacTClassDao.findFigliClassificatoreIds(annoEsercizio, SiacDClassTipoEnum.TitoloEntrata, siacTEnteProprietario.getEnteProprietarioId(), codiceTitoloEntrata, codiceTipologia, codiceCategoria);
		
		return classifIdTitoloEntrataTipologiaCategoria;
	}

	/**
	 * Find figli classificatore ids titolo uscita macroaggregato.
	 *
	 * @param siacTEnteProprietario the siac t ente proprietario
	 * @param codiceTitoloSpesa the codice titolo spesa
	 * @param codiceMacroaggregato the codice macroaggregato
	 * @return the list
	 */
	private List<Integer> findFigliClassificatoreIdsTitoloUscitaMacroaggregato(String annoEsercizio, SiacTEnteProprietario siacTEnteProprietario, String codiceTitoloSpesa, String codiceMacroaggregato) {
		//SpesaTitolimacroaggregati			-> TitoloSpesa, Macroaggregato		  (solo Uscita)
//		String codiceTitoloSpesaMacroaggregato = codiceMacroaggregato;
//		SiacDClassTipoEnum tipoTitoloSpesaMacroaggregato = SiacDClassTipoEnum.Macroaggregato;
//		if(codiceTitoloSpesaMacroaggregato==null){
//			codiceTitoloSpesaMacroaggregato = codiceTitoloSpesa;
//			tipoTitoloSpesaMacroaggregato = SiacDClassTipoEnum.TitoloSpesa;
//		}	
//		List<Integer> classifIdTitoloUscitaMacroaggregato = siacTClassDao.findFigliClassificatoreIds(codiceTitoloSpesaMacroaggregato, tipoTitoloSpesaMacroaggregato, SiacDClassFamEnum.SpesaTitolimacroaggregati);
		
		List<Integer> classifIdTitoloUscitaMacroaggregato = siacTClassDao.findFigliClassificatoreIds(annoEsercizio, SiacDClassTipoEnum.TitoloSpesa, siacTEnteProprietario.getEnteProprietarioId(), codiceTitoloSpesa, codiceMacroaggregato);
		
		
		return classifIdTitoloUscitaMacroaggregato;
	}

	/**
	 * Find figli classificatore ids missione programma.
	 *
	 * @param siacTEnteProprietario the siac t ente proprietario
	 * @param codiceMissione the codice missione
	 * @param codiceProgrmma the codice progrmma
	 * @return the list
	 */
	private List<Integer> findFigliClassificatoreIdsMissioneProgramma(String annoEsercizio, SiacTEnteProprietario siacTEnteProprietario, String codiceMissione, String codiceProgrmma) {
		//SpesaMissioniprogrammi			-> Missione, Programma (solo Uscita)
//		String codiceMissioneProgramma = codiceProgrmma;
//		SiacDClassTipoEnum tipoMissioneProgramma = SiacDClassTipoEnum.Programma;
//		if(codiceMissioneProgramma==null){
//			codiceMissioneProgramma = codiceMissione;
//			tipoMissioneProgramma = SiacDClassTipoEnum.Missione;
//		}	
//		List<Integer> classifIdMissioneProgramma = siacTClassDao.findFigliClassificatoreIds(codiceMissioneProgramma, tipoMissioneProgramma, SiacDClassFamEnum.SpesaMissioniprogrammi);
		
		
		
		
		List<Integer> classifIdMissioneProgramma = siacTClassDao.findFigliClassificatoreIds(annoEsercizio, SiacDClassTipoEnum.Missione, siacTEnteProprietario.getEnteProprietarioId(), codiceMissione, codiceProgrmma);		
		
		return classifIdMissioneProgramma;
	}

	/**
	 * Find figli classificatore ids struttura amministrativa contabile.
	 *
	 * @param siacTEnteProprietario the siac t ente proprietario
	 * @param codiceStruttAmmCont the codice strutt amm cont
	 * @param codiceTipoStruttAmmCont the codice tipo strutt amm cont
	 * @return the list
	 */
	private List<Integer> findFigliClassificatoreIdsStrutturaAmministrativaContabile(String annoEsercizio, SiacTEnteProprietario siacTEnteProprietario, String codiceStruttAmmCont, String codiceTipoStruttAmmCont) {
		//StrutturaAmministrativaContabile 	-> Centro di Responsabilità, CDC (Settore)
					//codiceStruttAmmCont-codiceTipoStruttAmmCont  //005-CDR troverà //004-CDC
		//List<Integer> classifIdStruttAmmCont = siacTClassDao.findFigliClassificatoreIds(codiceStruttAmmCont, SiacDClassTipoEnum.byCodiceEvenNull(codiceTipoStruttAmmCont) , SiacDClassFamEnum.StrutturaAmministrativaContabile);
		
		List<Integer> classifIdStruttAmmCont = siacTClassDao.findFigliClassificatoreIds(annoEsercizio, SiacDClassTipoEnum.byCodiceEvenNull(codiceTipoStruttAmmCont), siacTEnteProprietario.getEnteProprietarioId(), codiceStruttAmmCont);
		
		return classifIdStruttAmmCont;
	}
	
	/**
	 * Find figli classificatore ids siope entrata.
	 *
	 * @param siacTEnteProprietario the siac t ente proprietario
	 * @param codiceSiopeEntrata the codice siope entrata
	 * @param codiceTipoSiopeEntrata the codice tipo siope entrata
	 * @return the list
	 */
	private List<Integer> findFigliClassificatoreIdsSiopeEntrata(String annoEsercizio, SiacTEnteProprietario siacTEnteProprietario, String codiceSiopeEntrata, String codiceTipoSiopeEntrata) {

		//List<Integer> classifIdStruttAmmCont = siacTClassDao.findFigliClassificatoreIds(codiceSiopeEntrata, SiacDClassTipoEnum.byCodiceEvenNull(codiceTipoSiopeEntrata) , SiacDClassFamEnum.SiopeEntrata);
		
		List<Integer> classifIdStruttAmmCont = siacTClassDao.findFigliClassificatoreIds(annoEsercizio, SiacDClassTipoEnum.byCodiceEvenNull(codiceTipoSiopeEntrata), siacTEnteProprietario.getEnteProprietarioId(), codiceSiopeEntrata);
		
		return classifIdStruttAmmCont;
	}
	
	/**
	 * Find figli classificatore ids siope spesa.
	 *
	 * @param siacTEnteProprietario the siac t ente proprietario
	 * @param codiceSiopeSpesa the codice siope spesa
	 * @param codiceTipoSiopeSpesa the codice tipo siope spesa
	 * @return the list
	 */
	private List<Integer> findFigliClassificatoreIdsSiopeSpesa(String annoEsercizio, SiacTEnteProprietario siacTEnteProprietario, String codiceSiopeSpesa, String codiceTipoSiopeSpesa) {

		//List<Integer> classifIdStruttAmmCont = siacTClassDao.findFigliClassificatoreIds(codiceSiopeSpesa, SiacDClassTipoEnum.byCodiceEvenNull(codiceTipoSiopeSpesa) , SiacDClassFamEnum.SiopeSpesa);
		
		List<Integer> classifIdStruttAmmCont = siacTClassDao.findFigliClassificatoreIds(annoEsercizio, SiacDClassTipoEnum.byCodiceEvenNull(codiceTipoSiopeSpesa), siacTEnteProprietario.getEnteProprietarioId(), codiceSiopeSpesa);
		
		return classifIdStruttAmmCont;
	}

	/**
	 * Find figli classificatore ids cofog.
	 *
	 * @param siacTEnteProprietario the siac t ente proprietario
	 * @param codiceCofog the codice cofog
	 * @param codiceTipoCofog the codice tipo cofog
	 * @return the list
	 */
	private List<Integer> findFigliClassificatoreIdsCofog(String annoEsercizio, SiacTEnteProprietario siacTEnteProprietario, String codiceCofog, String codiceTipoCofog) {
		//Cofog		-> CofogDivisione,CofogGruppo,CofogClasse
		//List<Integer> classifIdCofog = siacTClassDao.findFigliClassificatoreIds(codiceCofog, SiacDClassTipoEnum.byCodiceEvenNull(codiceTipoCofog) , SiacDClassFamEnum.Cofog);
		List<Integer> classifIdCofog = siacTClassDao.findFigliClassificatoreIds(annoEsercizio, SiacDClassTipoEnum.byCodiceEvenNull(codiceTipoCofog), siacTEnteProprietario.getEnteProprietarioId(), codiceCofog);
		
		return classifIdCofog;
	}

	/**
	 * Find figli classificatore ids piano dei conti.
	 *
	 * @param siacTEnteProprietario the siac t ente proprietario
	 * @param codicePianoDeiConti the codice piano dei conti
	 * @return the list
	 */
	private List<Integer> findFigliClassificatoreIdsPianoDeiConti(String annoEsercizio, SiacTEnteProprietario siacTEnteProprietario, String codicePianoDeiConti) {
		//PianoDeiConti			 			-> PrimoLivelloPDC,SecondoLivelloPDC,TerzoLivelloPDC...QuintoLivelloPDC
					//codicePianoDeiConti =  "E.9.02.00.00.000";//troverà "E.9.02.03.04.000";
		//List<Integer> classifIdPianoDeiConti = siacTClassDao.findFigliClassificatoreIds(codicePianoDeiConti, SiacDClassTipoEnum.PianoDeiConti , SiacDClassFamEnum.PianoDeiConti);
		
		List<Integer> classifIdPianoDeiConti = siacTClassDao.findFigliClassificatoreIds(annoEsercizio, SiacDClassTipoEnum.PianoDeiConti, siacTEnteProprietario.getEnteProprietarioId(), codicePianoDeiConti);
		
		return classifIdPianoDeiConti;
	}
	
	
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.elementobilancio.CapitoloDao#ricercaSinteticaCapitolo(it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario, java.lang.String, it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.List, java.util.List, java.util.List, java.util.List, java.util.List, java.util.List, java.util.List, java.util.List, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<SiacTBilElem> ricercaSinteticaCapitolo(SiacTEnteProprietario enteDto, String annoEsercizio, SiacDBilElemTipoEnum tipoCapitolo, Integer uidCategoriaCapitolo, String codiceCategoriaCapitolo, String annoCapitolo,
			String numeroCapitolo, String numeroArticolo, String numeroUEB, String stato, 
			
			String exAnnoCapitolo,
			String exNumeroCapitolo,
			String exNumeroArticolo,
			String exNumeroUEB,
			
			String faseBilancio, 
			String descrizioneCapitolo,
			String descrizioneArticolo,
			
			String flagAssegnabile,
			String flagFondoSvalutazioneCrediti,
			String flagFunzioniDelegate,
			String flagPerMemoria,
			String flagRilevanteIva,
			String flagTrasferimentoOrganiComunitari,	
			String flagEntrateRicorrenti,
			String flagFondoPluriennaleVinc,
			
			//classificatori generici
			String codiceTipoFinanziamento,
			String codiceTipoFondo,
			String codiceTipoVincolo,			
			String codiceRicorrenteEntrata, String codiceRicorrenteSpesa,
			String codicePerimetroSanitarioEntrata, String codicePerimetroSanitarioSpesa,
			String codiceTransazioneUnioneEuropeaEntrata, String codiceTransazioneUnioneEuropeaSpesa,
			String codicePoliticheRegionaliUnitarie, 
			String codiceClassificatoreGenerico1,
			String codiceClassificatoreGenerico2,
			String codiceClassificatoreGenerico3,
			String codiceClassificatoreGenerico4,
			String codiceClassificatoreGenerico5,
			String codiceClassificatoreGenerico6,
			String codiceClassificatoreGenerico7,
			String codiceClassificatoreGenerico8,
			String codiceClassificatoreGenerico9,
			String codiceClassificatoreGenerico10,
			
			String codiceClassificatoreGenerico31,
			String codiceClassificatoreGenerico32,
			String codiceClassificatoreGenerico33,
			String codiceClassificatoreGenerico34,
			String codiceClassificatoreGenerico35,
			String codiceClassificatoreGenerico36,
			String codiceClassificatoreGenerico37,
			String codiceClassificatoreGenerico38,
			String codiceClassificatoreGenerico39,
			String codiceClassificatoreGenerico40,
			String codiceClassificatoreGenerico41,
			String codiceClassificatoreGenerico42,
			String codiceClassificatoreGenerico43,
			String codiceClassificatoreGenerico44,
			String codiceClassificatoreGenerico45,
			String codiceClassificatoreGenerico46,
			String codiceClassificatoreGenerico47,
			String codiceClassificatoreGenerico48,
			String codiceClassificatoreGenerico49,
			String codiceClassificatoreGenerico50,
			
			
			List<Integer> classifIdPianoDeiConti, 
			List<Integer> classifIdCofog,
			List<Integer> classifIdStruttAmmCont,
			List<Integer> classifIdSiopeEntrata,  
			List<Integer> classifIdSiopeSpesa,  
			List<Integer> classifIdMissioneProgramma, 
			List<Integer> classifIdTitoloUscitaMacroaggregato, 
			List<Integer> classifIdTitoloEntrataTipologiaCategoria,
			
			Integer attoleggeNumero,
			String attoleggeAnno,
			String attoleggeArticolo,
			String attoleggeComma,
			String attoleggePunto,
			String attoleggeTipoCode,
			
			Boolean collegatoFondiDubbiaEsigibilita,
						
			Pageable pageable) {
		
		
		StringBuilder jpql = new StringBuilder();
		Map<String,Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaSinteticaCapitolo(jpql, param, enteDto, annoEsercizio, tipoCapitolo, uidCategoriaCapitolo, codiceCategoriaCapitolo, annoCapitolo, numeroCapitolo, numeroArticolo, numeroUEB, stato, exAnnoCapitolo,
				exNumeroCapitolo, exNumeroArticolo, exNumeroUEB, faseBilancio, descrizioneCapitolo, descrizioneArticolo, flagAssegnabile,
				flagFondoSvalutazioneCrediti, flagFunzioniDelegate, flagPerMemoria, flagRilevanteIva, flagTrasferimentoOrganiComunitari, 
				flagEntrateRicorrenti, flagFondoPluriennaleVinc, codiceTipoFinanziamento, codiceTipoFondo, codiceTipoVincolo,
				codiceRicorrenteEntrata, codiceRicorrenteSpesa,
				codicePerimetroSanitarioEntrata, codicePerimetroSanitarioSpesa,
				codiceTransazioneUnioneEuropeaEntrata, codiceTransazioneUnioneEuropeaSpesa,
				codicePoliticheRegionaliUnitarie, 
				codiceClassificatoreGenerico1,
				codiceClassificatoreGenerico2, codiceClassificatoreGenerico3, codiceClassificatoreGenerico4, codiceClassificatoreGenerico5,
				codiceClassificatoreGenerico6, codiceClassificatoreGenerico7, codiceClassificatoreGenerico8, codiceClassificatoreGenerico9,
				codiceClassificatoreGenerico10, 
				
				codiceClassificatoreGenerico31,
				codiceClassificatoreGenerico32,
				codiceClassificatoreGenerico33,
				codiceClassificatoreGenerico34,
				codiceClassificatoreGenerico35,
				codiceClassificatoreGenerico36,
				codiceClassificatoreGenerico37,
				codiceClassificatoreGenerico38,
				codiceClassificatoreGenerico39,
				codiceClassificatoreGenerico40,
				codiceClassificatoreGenerico41,
				codiceClassificatoreGenerico42,
				codiceClassificatoreGenerico43,
				codiceClassificatoreGenerico44,
				codiceClassificatoreGenerico45,
				codiceClassificatoreGenerico46,
				codiceClassificatoreGenerico47,
				codiceClassificatoreGenerico48,
				codiceClassificatoreGenerico49,
				codiceClassificatoreGenerico50,
				
				classifIdPianoDeiConti, classifIdCofog, classifIdStruttAmmCont,
				classifIdSiopeEntrata,  
				classifIdSiopeSpesa,				
				classifIdMissioneProgramma,
				classifIdTitoloUscitaMacroaggregato, classifIdTitoloEntrataTipologiaCategoria,
				attoleggeNumero,
				attoleggeAnno,
				attoleggeArticolo,
				attoleggeComma,
				attoleggePunto,
				attoleggeTipoCode,
				collegatoFondiDubbiaEsigibilita);
		jpql.append(" ORDER BY cup.elemCode, cup.elemCode2, cup.elemCode3 ");
		
		
		
		return getPagedList(jpql.toString(), param, pageable);
	}
	
	/**
	 * Componi query ricerca sintetica capitolo.
	 *
	 * @param jpql the jpql
	 * @param param the param
	 * @param enteDto the ente dto
	 * @param annoEsercizio the anno esercizio
	 * @param tipoCapitolo the tipo capitolo
	 * @param annoCapitolo the anno capitolo
	 * @param numeroCapitolo the numero capitolo
	 * @param numeroArticolo the numero articolo
	 * @param numeroUEB the numero ueb
	 * @param stato the stato
	 * @param exAnnoCapitolo the ex anno capitolo
	 * @param exNumeroCapitolo the ex numero capitolo
	 * @param exNumeroArticolo the ex numero articolo
	 * @param exNumeroUEB the ex numero ueb
	 * @param faseBilancio the fase bilancio
	 * @param descrizioneCapitolo the descrizione capitolo
	 * @param descrizioneArticolo the descrizione articolo
	 * @param flagAssegnabile the flag assegnabile
	 * @param flagFondoSvalutazioneCrediti the flag fondo svalutazione crediti
	 * @param flagFunzioniDelegate the flag funzioni delegate
	 * @param flagPerMemoria the flag per memoria
	 * @param flagRilevanteIva the flag rilevante iva
	 * @param flagTrasferimentoOrganiComunitari the flag trasferimento organi comunitari
	 * @param flagEntrateRicorrenti the flag entrate ricorrenti
	 * @param flagFondoPluriennaleVinc the flag fondo pluriennale vinc
	 * @param codiceTipoFinanziamento the codice tipo finanziamento
	 * @param codiceTipoFondo the codice tipo fondo
	 * @param codiceTipoVincolo the codice tipo vincolo
	 * @param codiceRicorrenteEntrata the codice ricorrente entrata
	 * @param codiceRicorrenteSpesa the codice ricorrente spesa
	 * @param codicePerimetroSanitarioEntrata the codice perimetro sanitario entrata
	 * @param codicePerimetroSanitarioSpesa the codice perimetro sanitario spesa
	 * @param codiceTransazioneUnioneEuropeaEntrata the codice transazione unione europea entrata
	 * @param codiceTransazioneUnioneEuropeaSpesa the codice transazione unione europea spesa
	 * @param codicePoliticheRegionaliUnitarie the codice politiche regionali unitarie
	 * @param codiceClassificatoreGenerico1 the codice classificatore generico1
	 * @param codiceClassificatoreGenerico2 the codice classificatore generico2
	 * @param codiceClassificatoreGenerico3 the codice classificatore generico3
	 * @param codiceClassificatoreGenerico4 the codice classificatore generico4
	 * @param codiceClassificatoreGenerico5 the codice classificatore generico5
	 * @param codiceClassificatoreGenerico6 the codice classificatore generico6
	 * @param codiceClassificatoreGenerico7 the codice classificatore generico7
	 * @param codiceClassificatoreGenerico8 the codice classificatore generico8
	 * @param codiceClassificatoreGenerico9 the codice classificatore generico9
	 * @param codiceClassificatoreGenerico10 the codice classificatore generico10
	 * @param classifIdPianoDeiConti the classif id piano dei conti
	 * @param classifIdCofog the classif id cofog
	 * @param classifIdStruttAmmCont the classif id strutt amm cont
	 * @param classifIdSiopeEntrata the classif id siope entrata
	 * @param classifIdSiopeSpesa the classif id siope spesa
	 * @param classifIdMissioneProgramma the classif id missione programma
	 * @param classifIdTitoloUscitaMacroaggregato the classif id titolo uscita macroaggregato
	 * @param classifIdTitoloEntrataTipologiaCategoria the classif id titolo entrata tipologia categoria
	 * @param attoleggeNumero the attolegge numero
	 * @param attoleggeAnno the attolegge anno
	 * @param attoleggeArticolo the attolegge articolo
	 * @param attoleggeComma the attolegge comma
	 * @param attoleggePunto the attolegge punto
	 * @param attoleggeTipoCode the attolegge tipo code
	 * @param groupUeb the group ueb
	 * @param codiceClassificatoreGenerico31 
	 */
	private void componiQueryRicercaSinteticaCapitolo(StringBuilder jpql, Map<String, Object> param, 
			SiacTEnteProprietario enteDto, String annoEsercizio, SiacDBilElemTipoEnum tipoCapitolo, Integer uidCategoriaCapitolo, String codiceCategoriaCapitolo,  String annoCapitolo,
			String numeroCapitolo, String numeroArticolo, String numeroUEB, String stato, String exAnnoCapitolo, String exNumeroCapitolo,
			String exNumeroArticolo, String exNumeroUEB, String faseBilancio, String descrizioneCapitolo, String descrizioneArticolo,
			String flagAssegnabile, String flagFondoSvalutazioneCrediti, String flagFunzioniDelegate, String flagPerMemoria, String flagRilevanteIva,
			String flagTrasferimentoOrganiComunitari, String flagEntrateRicorrenti, String flagFondoPluriennaleVinc, 
			
			String codiceTipoFinanziamento, String codiceTipoFondo, String codiceTipoVincolo, 
			String codiceRicorrenteEntrata, String codiceRicorrenteSpesa,
			String codicePerimetroSanitarioEntrata, String codicePerimetroSanitarioSpesa,
			String codiceTransazioneUnioneEuropeaEntrata, String codiceTransazioneUnioneEuropeaSpesa,
			String codicePoliticheRegionaliUnitarie, 
			String codiceClassificatoreGenerico1, String codiceClassificatoreGenerico2,	String codiceClassificatoreGenerico3, 
			String codiceClassificatoreGenerico4, String codiceClassificatoreGenerico5, String codiceClassificatoreGenerico6, 
			String codiceClassificatoreGenerico7, String codiceClassificatoreGenerico8, String codiceClassificatoreGenerico9, String codiceClassificatoreGenerico10, 	
			String codiceClassificatoreGenerico31,
			String codiceClassificatoreGenerico32,
			String codiceClassificatoreGenerico33,
			String codiceClassificatoreGenerico34,
			String codiceClassificatoreGenerico35,
			String codiceClassificatoreGenerico36,
			String codiceClassificatoreGenerico37,
			String codiceClassificatoreGenerico38,
			String codiceClassificatoreGenerico39,
			String codiceClassificatoreGenerico40,
			String codiceClassificatoreGenerico41,
			String codiceClassificatoreGenerico42,
			String codiceClassificatoreGenerico43,
			String codiceClassificatoreGenerico44,
			String codiceClassificatoreGenerico45,
			String codiceClassificatoreGenerico46,
			String codiceClassificatoreGenerico47,
			String codiceClassificatoreGenerico48,
			String codiceClassificatoreGenerico49,
			String codiceClassificatoreGenerico50,
			List<Integer> classifIdPianoDeiConti, List<Integer> classifIdCofog, List<Integer> classifIdStruttAmmCont,
			List<Integer> classifIdSiopeEntrata,  
			List<Integer> classifIdSiopeSpesa,  
			List<Integer> classifIdMissioneProgramma,
			List<Integer> classifIdTitoloUscitaMacroaggregato, List<Integer> classifIdTitoloEntrataTipologiaCategoria,
			
			Integer attoleggeNumero,
			String attoleggeAnno,
			String attoleggeArticolo,
			String attoleggeComma,
			String attoleggePunto,
			String attoleggeTipoCode,
			Boolean collegatoFondiDubbiaEsigibilita) {
		
		String annoBilancio = annoEsercizio != null ? annoEsercizio : annoCapitolo;
		
		jpql.append(" FROM SiacTBilElem cup ");
		jpql.append(" WHERE  cup.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append(" AND cup.dataCancellazione IS NULL ");
		jpql.append(" AND cup.siacDBilElemTipo.elemTipoCode = :elemTipoCode ");
		
		param.put("enteProprietarioId", enteDto.getUid());
		param.put("elemTipoCode", tipoCapitolo.getCodice());
		
		if(StringUtils.isNotBlank(annoBilancio)) {
			jpql.append(" AND cup.siacTBil.siacTPeriodo.anno = :annoBilancio ");
			param.put("annoBilancio", annoBilancio);
		}
		if(StringUtils.isNotBlank(numeroCapitolo)) {
			jpql.append(" AND cup.elemCode = :numeroCapitolo ");
			param.put("numeroCapitolo", numeroCapitolo);
		}
		if(StringUtils.isNotBlank(numeroArticolo)) {
			jpql.append(" AND cup.elemCode2 = :numeroArticolo ");
			param.put("numeroArticolo", numeroArticolo);
		}
		if(StringUtils.isNotBlank(numeroUEB)) {
			jpql.append(" AND cup.elemCode3 = :numeroUEB ");
			param.put("numeroUEB", numeroUEB);
		}
		if(StringUtils.isNotBlank(stato)) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM cup.siacRBilElemCategorias bec ");
			jpql.append("     WHERE bec.dataCancellazione IS NULL ");
			jpql.append("     AND bec.siacDBilElemCategoria.elemCatCode = :elemCatCode ");
			jpql.append(" ) ");
			param.put("stato", stato);
		}
		
		if(uidCategoriaCapitolo!=null){
			jpql.append(" AND EXISTS (FROM cup.siacRBilElemCategorias bec ");
			jpql.append("               WHERE bec.dataCancellazione IS NULL");
			jpql.append("               AND bec.siacDBilElemCategoria.elemCatId = :elemCatId ) ");
			param.put("elemCatId", uidCategoriaCapitolo);
		} else if(codiceCategoriaCapitolo!=null){
			jpql.append(" AND EXISTS (FROM cup.siacRBilElemCategorias bec ");
			jpql.append("               WHERE bec.dataCancellazione IS NULL");
			jpql.append("               AND bec.siacDBilElemCategoria.elemCatCode = :elemCatCode ) ");
			param.put("elemCatCode", codiceCategoriaCapitolo);
		}
		

		appenExCapitoloFilter(jpql,param, exAnnoCapitolo, exNumeroCapitolo, exNumeroArticolo, exNumeroUEB, faseBilancio);
		
		
		if(!StringUtils.isBlank(descrizioneCapitolo)){
			
			jpql.append("       AND ( "+Utility.toJpqlSearchLike("cup.elemDesc", "CONCAT('%',:descrizioneCapitolo,'%')" ) +" )");	
			
			param.put("descrizioneCapitolo", descrizioneCapitolo);
		}
		
		if(!StringUtils.isBlank(descrizioneArticolo)){
			
			jpql.append("       AND ( "+Utility.toJpqlSearchLike("cup.elemDesc2", "CONCAT('%',:descrizioneArticolo,'%')" ) +" )");	
			
			param.put("descrizioneArticolo", descrizioneArticolo);
		}
		
		
		if(!StringUtils.isBlank(faseBilancio)){
			jpql.append("       AND ( EXISTS (SELECT bfo ");
			jpql.append("                         FROM   SiacRBilFaseOperativa bfo ");
			jpql.append("                         WHERE  bfo.siacDFaseOperativa.faseOperativaCode = :faseBilancio ");
			jpql.append("                                AND bfo.siacTEnteProprietario = :ente"); 
			jpql.append("                                AND bfo.siacTBil = cup.siacTBil) ) ");			
			param.put("faseBilancio", faseBilancio);
		}
						
		appendAttoLeggeFilter(jpql, param, attoleggeNumero, attoleggeAnno, attoleggeArticolo, attoleggeComma, attoleggePunto, attoleggeTipoCode);
		
		
		//flags
		appendFlagFilter(jpql,param, flagAssegnabile, SiacTAttrEnum.FlagAssegnabile); 
		appendFlagFilter(jpql,param, flagFondoSvalutazioneCrediti, SiacTAttrEnum.FlagFondoSvalutazioneCrediti); 
		appendFlagFilter(jpql,param, flagFunzioniDelegate, SiacTAttrEnum.FlagFunzioniDelegate); 
		appendFlagFilter(jpql,param, flagPerMemoria, SiacTAttrEnum.FlagPerMemoria); 
		appendFlagFilter(jpql,param, flagRilevanteIva, SiacTAttrEnum.FlagRilevanteIva); 
		appendFlagFilter(jpql,param, flagTrasferimentoOrganiComunitari, SiacTAttrEnum.FlagTrasferimentoOrganiComunitari); 
		appendFlagFilter(jpql, param, flagEntrateRicorrenti, SiacTAttrEnum.FlagEntrateRicorrenti);
		appendFlagFilter(jpql, param, flagFondoPluriennaleVinc, SiacTAttrEnum.FlagFondoPluriennaleVinc);
		
			
		//classificatori generici
		appendClassifCodeFilter(jpql, param, codiceTipoFinanziamento, SiacDClassTipoEnum.TipoFinanziamento);		
		appendClassifCodeFilter(jpql, param, codiceTipoFondo, SiacDClassTipoEnum.TipoFondo);		
		appendClassifCodeFilter(jpql, param, codiceTipoVincolo, SiacDClassTipoEnum.TipoVincolo);
		
		appendClassifCodeFilter(jpql, param, codiceRicorrenteEntrata, SiacDClassTipoEnum.RicorrenteEntrata);
		appendClassifCodeFilter(jpql, param, codiceRicorrenteSpesa, SiacDClassTipoEnum.RicorrenteSpesa);
		appendClassifCodeFilter(jpql, param, codicePerimetroSanitarioEntrata, SiacDClassTipoEnum.PerimetroSanitarioEntrata);
		appendClassifCodeFilter(jpql, param, codicePerimetroSanitarioSpesa, SiacDClassTipoEnum.PerimetroSanitarioSpesa);
		appendClassifCodeFilter(jpql, param, codiceTransazioneUnioneEuropeaEntrata, SiacDClassTipoEnum.TransazioneUnioneEuropeaEntrata);
		appendClassifCodeFilter(jpql, param, codiceTransazioneUnioneEuropeaSpesa, SiacDClassTipoEnum.TransazioneUnioneEuropeaSpesa);
		appendClassifCodeFilter(jpql, param, codicePoliticheRegionaliUnitarie, SiacDClassTipoEnum.PoliticheRegionaliUnitarie);
		
		appendClassifCodeFilter(jpql, param, codiceClassificatoreGenerico1, SiacDClassTipoEnum.Classificatore1);
		appendClassifCodeFilter(jpql, param, codiceClassificatoreGenerico2, SiacDClassTipoEnum.Classificatore2);
		appendClassifCodeFilter(jpql, param, codiceClassificatoreGenerico3, SiacDClassTipoEnum.Classificatore3);
		appendClassifCodeFilter(jpql, param, codiceClassificatoreGenerico4, SiacDClassTipoEnum.Classificatore4);
		appendClassifCodeFilter(jpql, param, codiceClassificatoreGenerico5, SiacDClassTipoEnum.Classificatore5);
		appendClassifCodeFilter(jpql, param, codiceClassificatoreGenerico6, SiacDClassTipoEnum.Classificatore6);
		appendClassifCodeFilter(jpql, param, codiceClassificatoreGenerico7, SiacDClassTipoEnum.Classificatore7);
		appendClassifCodeFilter(jpql, param, codiceClassificatoreGenerico8, SiacDClassTipoEnum.Classificatore8);
		appendClassifCodeFilter(jpql, param, codiceClassificatoreGenerico9, SiacDClassTipoEnum.Classificatore9);
		appendClassifCodeFilter(jpql, param, codiceClassificatoreGenerico10, SiacDClassTipoEnum.Classificatore10);
		
		appendClassifCodeFilter(jpql, param, codiceClassificatoreGenerico31, SiacDClassTipoEnum.Classificatore31);
		appendClassifCodeFilter(jpql, param, codiceClassificatoreGenerico32, SiacDClassTipoEnum.Classificatore32);
		appendClassifCodeFilter(jpql, param, codiceClassificatoreGenerico33, SiacDClassTipoEnum.Classificatore33);
		appendClassifCodeFilter(jpql, param, codiceClassificatoreGenerico34, SiacDClassTipoEnum.Classificatore34);
		appendClassifCodeFilter(jpql, param, codiceClassificatoreGenerico35, SiacDClassTipoEnum.Classificatore35);
		appendClassifCodeFilter(jpql, param, codiceClassificatoreGenerico36, SiacDClassTipoEnum.Classificatore36);
		appendClassifCodeFilter(jpql, param, codiceClassificatoreGenerico37, SiacDClassTipoEnum.Classificatore37);
		appendClassifCodeFilter(jpql, param, codiceClassificatoreGenerico38, SiacDClassTipoEnum.Classificatore38);
		appendClassifCodeFilter(jpql, param, codiceClassificatoreGenerico39, SiacDClassTipoEnum.Classificatore39);
		appendClassifCodeFilter(jpql, param, codiceClassificatoreGenerico40, SiacDClassTipoEnum.Classificatore40);
		appendClassifCodeFilter(jpql, param, codiceClassificatoreGenerico41, SiacDClassTipoEnum.Classificatore41);
		appendClassifCodeFilter(jpql, param, codiceClassificatoreGenerico42, SiacDClassTipoEnum.Classificatore42);
		appendClassifCodeFilter(jpql, param, codiceClassificatoreGenerico43, SiacDClassTipoEnum.Classificatore43);
		appendClassifCodeFilter(jpql, param, codiceClassificatoreGenerico44, SiacDClassTipoEnum.Classificatore44);
		appendClassifCodeFilter(jpql, param, codiceClassificatoreGenerico45, SiacDClassTipoEnum.Classificatore45);
		appendClassifCodeFilter(jpql, param, codiceClassificatoreGenerico46, SiacDClassTipoEnum.Classificatore46);
		appendClassifCodeFilter(jpql, param, codiceClassificatoreGenerico47, SiacDClassTipoEnum.Classificatore47);
		appendClassifCodeFilter(jpql, param, codiceClassificatoreGenerico48, SiacDClassTipoEnum.Classificatore48);
		appendClassifCodeFilter(jpql, param, codiceClassificatoreGenerico49, SiacDClassTipoEnum.Classificatore49);
		appendClassifCodeFilter(jpql, param, codiceClassificatoreGenerico50, SiacDClassTipoEnum.Classificatore50);
		
		
		
		//classificatori gerarchici
		appendClassifIdsFilter(jpql, classifIdTitoloEntrataTipologiaCategoria, param, "classifIdTitoloEntrataTipologiaCategoria");
		appendClassifIdsFilter(jpql, classifIdTitoloUscitaMacroaggregato, param, "classifIdTitoloUscitaMacroaggregato");
		appendClassifIdsFilter(jpql, classifIdMissioneProgramma, param, "classifIdMissioneProgramma");
		appendClassifIdsFilter(jpql, classifIdPianoDeiConti, param, "classifIdPianoDeiConti");
		appendClassifIdsFilter(jpql, classifIdStruttAmmCont, param, "classifIdStruttAmmCont");
		
		appendClassifIdsFilter(jpql, classifIdSiopeEntrata, param, "classifIdSiopeEntrata");
		appendClassifIdsFilter(jpql, classifIdSiopeSpesa, param, "classifIdSiopeSpesa");
		
		appendClassifIdsFilter(jpql, classifIdCofog, param, "classifIdCofog");
		
		// SIAC-4088
		if(collegatoFondiDubbiaEsigibilita != null) {
			jpql.append(" AND ");
			jpql.append(Boolean.TRUE.equals(collegatoFondiDubbiaEsigibilita) ? "" : "NOT ");
			jpql.append(" EXISTS ( ");
			jpql.append("     FROM cup.siacRBilElemAccFondiDubbiaEsigs rbeafde ");
			jpql.append("     WHERE rbeafde.dataCancellazione IS NULL ");
			jpql.append("     AND  rbeafde.siacTAccFondiDubbiaEsig.dataCancellazione IS NULL ");
			jpql.append(" ) ");
		}
	}
	
	private void componiQueryRicercaSinteticaNativaCapitolo(StringBuilder jpql, Map<String, Object> param, 
			Integer enteProprietarioId,
			String annoEsercizio,
			String elemTipoCode,
			Integer uidCategoriaCapitolo,
			String codiceCategoriaCapitolo,
			String annoCapitolo,
			String numeroCapitolo,
			String numeroArticolo,
			String numeroUEB,
			String stato,
			String exAnnoCapitolo,
			String exNumeroCapitolo,
			String exNumeroArticolo,
			String exNumeroUEB,
			String faseBilancio,
			String descrizioneCapitolo,
			String descrizioneArticolo,
			String flagAssegnabile,
			String flagFondoSvalutazioneCrediti,
			String flagFunzioniDelegate,
			String flagPerMemoria,
			String flagRilevanteIva,
			String flagTrasferimentoOrganiComunitari,
			String flagEntrateRicorrenti,
			String flagFondoPluriennaleVinc, 
			
			String codiceTipoFinanziamento,
			String codiceTipoFondo,
			String codiceTipoVincolo,
			String codiceRicorrenteEntrata,
			String codiceRicorrenteSpesa,
			String codicePerimetroSanitarioEntrata,
			String codicePerimetroSanitarioSpesa,
			String codiceTransazioneUnioneEuropeaEntrata,
			String codiceTransazioneUnioneEuropeaSpesa,
			String codicePoliticheRegionaliUnitarie, 
			String codiceClassificatoreGenerico1,
			String codiceClassificatoreGenerico2,
			String codiceClassificatoreGenerico3,
			String codiceClassificatoreGenerico4,
			String codiceClassificatoreGenerico5,
			String codiceClassificatoreGenerico6,
			String codiceClassificatoreGenerico7,
			String codiceClassificatoreGenerico8,
			String codiceClassificatoreGenerico9,
			String codiceClassificatoreGenerico10,
			String codiceClassificatoreGenerico31,
			String codiceClassificatoreGenerico32,
			String codiceClassificatoreGenerico33,
			String codiceClassificatoreGenerico34,
			String codiceClassificatoreGenerico35,
			String codiceClassificatoreGenerico36,
			String codiceClassificatoreGenerico37,
			String codiceClassificatoreGenerico38,
			String codiceClassificatoreGenerico39,
			String codiceClassificatoreGenerico40,
			String codiceClassificatoreGenerico41,
			String codiceClassificatoreGenerico42,
			String codiceClassificatoreGenerico43,
			String codiceClassificatoreGenerico44,
			String codiceClassificatoreGenerico45,
			String codiceClassificatoreGenerico46,
			String codiceClassificatoreGenerico47,
			String codiceClassificatoreGenerico48,
			String codiceClassificatoreGenerico49,
			String codiceClassificatoreGenerico50,
			List<Integer> classifIdPianoDeiConti,
			List<Integer> classifIdCofog,
			List<Integer> classifIdStruttAmmCont,
			List<Integer> classifIdSiopeEntrata,
			List<Integer> classifIdSiopeSpesa,
			List<Integer> classifIdMissioneProgramma,
			List<Integer> classifIdTitoloUscitaMacroaggregato,
			List<Integer> classifIdTitoloEntrataTipologiaCategoria,
			
			Integer attoleggeNumero,
			String attoleggeAnno,
			String attoleggeArticolo,
			String attoleggeComma,
			String attoleggePunto,
			String attoleggeTipoCode,
			Boolean collegatoFondiDubbiaEsigibilita) {
		
		String annoBilancio = annoEsercizio != null ? annoEsercizio : annoCapitolo;
		
		jpql.append(" FROM siac_t_bil_elem cup ");
		
		StringBuilder where = new StringBuilder();
		// join table => join condition
		Map<String, String> joins = new LinkedHashMap<String, String>();
		
		joins.put("siac_d_bil_elem_tipo", "siac_d_bil_elem_tipo dbet ON (dbet.elem_tipo_id = cup.elem_tipo_id AND dbet.data_cancellazione IS NULL)");
		
		// Ente e tipo
		where.append(" WHERE cup.ente_proprietario_id = :enteProprietarioId ");
		where.append(" AND cup.data_cancellazione IS NULL ");
		where.append(" AND dbet.elem_tipo_code = :elemTipoCode ");
		
		param.put("enteProprietarioId", enteProprietarioId);
		param.put("elemTipoCode", elemTipoCode);
		
		// Anno
		if(StringUtils.isNotBlank(annoBilancio)) {
			joins.put("siac_t_bil", "siac_t_bil tb ON (tb.bil_id = cup.bil_id AND tb.data_cancellazione IS NULL)");
			joins.put("siac_t_periodo", "siac_t_periodo tp ON (tp.periodo_id = tb.periodo_id AND tp.data_cancellazione IS NULL)");
			where.append(" AND tp.anno = :annoBilancio ");
			param.put("annoBilancio", annoBilancio);
		}
		// Numero capitolo
		if(StringUtils.isNotBlank(numeroCapitolo)) {
			where.append(" AND cup.elem_code = :numeroCapitolo ");
			param.put("numeroCapitolo", numeroCapitolo);
		}
		// Numero articolo
		if(StringUtils.isNotBlank(numeroArticolo)) {
			where.append(" AND cup.elem_code2 = :numeroArticolo ");
			param.put("numeroArticolo", numeroArticolo);
		}
		// Numero UEB
		if(StringUtils.isNotBlank(numeroUEB)) {
			where.append(" AND cup.elem_code3 = :numeroUEB ");
			param.put("numeroUEB", numeroUEB);
		}
		// Stato
		if(StringUtils.isNotBlank(stato)) {
			joins.put("siac_r_bil_elem_stato", "siac_r_bil_elem_stato rbes ON (rbes.elem_id = cup.elem_id AND rbes.data_cancellazione IS NULL)");
			joins.put("siac_d_bil_elem_stato", "siac_d_bil_elem_stato dbes ON (dbes.elem_stato_id = rbes.elem_stato_id)");
			where.append(" AND dbes.elem_stato_code = :stato");
			param.put("stato", stato);
		}
		
		// Categoria (via uid e codice)
		if(uidCategoriaCapitolo!=null){
			joins.put("siac_r_bil_elem_categoria", "siac_r_bil_elem_categoria rbec ON (rbec.elem_id = cup.elem_id AND rbec.data_cancellazione IS NULL)");
			where.append(" AND rbec.elem_cat_id = :elemCatId ");
			param.put("elemCatId", uidCategoriaCapitolo);
		} else if(codiceCategoriaCapitolo!=null){
			joins.put("siac_r_bil_elem_categoria", "siac_r_bil_elem_categoria rbec ON (rbec.elem_id = cup.elem_id AND rbec.data_cancellazione IS NULL)");
			joins.put("siac_d_bil_elem_categoria", "siac_d_bil_elem_categoria dbec ON (dbec.elem_cat_id = rbec.elem_cat_id)");
			where.append(" AND rbec.elem_cat_code = :elemCatCode ");
			param.put("elemCatCode", codiceCategoriaCapitolo);
		}

		// Ex capitolo
		appendExCapitoloNativeFilter(where, joins, param, exAnnoCapitolo, exNumeroCapitolo, exNumeroArticolo, exNumeroUEB, faseBilancio);
		
		// Descrizione capitolo
		if(StringUtils.isNotBlank(descrizioneCapitolo)){
			where.append(" AND ").append(Utility.toJpqlSearchLike("cup.elem_desc", "CONCAT('%',:descrizioneCapitolo,'%')" ));
			param.put("descrizioneCapitolo", descrizioneCapitolo);
		}
		// Descrizione articolo
		if(StringUtils.isNotBlank(descrizioneArticolo)){
			where.append(" AND ").append(Utility.toJpqlSearchLike("cup.elemDesc2", "CONCAT('%',:descrizioneArticolo,'%')"));
			param.put("descrizioneArticolo", descrizioneArticolo);
		}
		// Fase bilancio
		if(StringUtils.isNotBlank(faseBilancio)){
			joins.put("siac_r_bil_fase_operativa", "siac_r_bil_fase_operativa rbfo ON (rbfo.bil_id = tb.bil_id AND rbfo.data_cancellazione IS NULL)");
			joins.put("siac_d_fase_operativa", "siac_d_fase_operativa dfo ON (dfo.fase_operativa_id = rbfo.fase_operativa_id)");
			where.append(" AND dfo.fase_operativa_code = :faseBilancio ");
			param.put("faseBilancio", faseBilancio);
		}
		
		// Atto di legge
		appendAttoLeggeNativeFilter(where, joins, param, attoleggeNumero, attoleggeAnno, attoleggeArticolo, attoleggeComma, attoleggePunto, attoleggeTipoCode);
		
		// Flags
		appendFlagNativeFilter(where, joins, param, flagAssegnabile, SiacTAttrEnum.FlagAssegnabile);
		appendFlagNativeFilter(where, joins, param, flagFondoSvalutazioneCrediti, SiacTAttrEnum.FlagFondoSvalutazioneCrediti);
		appendFlagNativeFilter(where, joins, param, flagFunzioniDelegate, SiacTAttrEnum.FlagFunzioniDelegate);
		appendFlagNativeFilter(where, joins, param, flagPerMemoria, SiacTAttrEnum.FlagPerMemoria);
		appendFlagNativeFilter(where, joins, param, flagRilevanteIva, SiacTAttrEnum.FlagRilevanteIva);
		appendFlagNativeFilter(where, joins, param, flagTrasferimentoOrganiComunitari, SiacTAttrEnum.FlagTrasferimentoOrganiComunitari); 
		appendFlagNativeFilter(where, joins, param, flagEntrateRicorrenti, SiacTAttrEnum.FlagEntrateRicorrenti);
		appendFlagNativeFilter(where, joins, param, flagFondoPluriennaleVinc, SiacTAttrEnum.FlagFondoPluriennaleVinc);
		
		// Classificatori generici
		appendClassifCodeNativeFilter(where, joins, param, codiceTipoFinanziamento, SiacDClassTipoEnum.TipoFinanziamento);
		appendClassifCodeNativeFilter(where, joins, param, codiceTipoFondo, SiacDClassTipoEnum.TipoFondo);
		appendClassifCodeNativeFilter(where, joins, param, codiceTipoVincolo, SiacDClassTipoEnum.TipoVincolo);
		appendClassifCodeNativeFilter(where, joins, param, codiceRicorrenteEntrata, SiacDClassTipoEnum.RicorrenteEntrata);
		appendClassifCodeNativeFilter(where, joins, param, codiceRicorrenteSpesa, SiacDClassTipoEnum.RicorrenteSpesa);
		appendClassifCodeNativeFilter(where, joins, param, codicePerimetroSanitarioEntrata, SiacDClassTipoEnum.PerimetroSanitarioEntrata);
		appendClassifCodeNativeFilter(where, joins, param, codicePerimetroSanitarioSpesa, SiacDClassTipoEnum.PerimetroSanitarioSpesa);
		appendClassifCodeNativeFilter(where, joins, param, codiceTransazioneUnioneEuropeaEntrata, SiacDClassTipoEnum.TransazioneUnioneEuropeaEntrata);
		appendClassifCodeNativeFilter(where, joins, param, codiceTransazioneUnioneEuropeaSpesa, SiacDClassTipoEnum.TransazioneUnioneEuropeaSpesa);
		appendClassifCodeNativeFilter(where, joins, param, codicePoliticheRegionaliUnitarie, SiacDClassTipoEnum.PoliticheRegionaliUnitarie);
		appendClassifCodeNativeFilter(where, joins, param, codiceClassificatoreGenerico1, SiacDClassTipoEnum.Classificatore1);
		appendClassifCodeNativeFilter(where, joins, param, codiceClassificatoreGenerico2, SiacDClassTipoEnum.Classificatore2);
		appendClassifCodeNativeFilter(where, joins, param, codiceClassificatoreGenerico3, SiacDClassTipoEnum.Classificatore3);
		appendClassifCodeNativeFilter(where, joins, param, codiceClassificatoreGenerico4, SiacDClassTipoEnum.Classificatore4);
		appendClassifCodeNativeFilter(where, joins, param, codiceClassificatoreGenerico5, SiacDClassTipoEnum.Classificatore5);
		appendClassifCodeNativeFilter(where, joins, param, codiceClassificatoreGenerico6, SiacDClassTipoEnum.Classificatore6);
		appendClassifCodeNativeFilter(where, joins, param, codiceClassificatoreGenerico7, SiacDClassTipoEnum.Classificatore7);
		appendClassifCodeNativeFilter(where, joins, param, codiceClassificatoreGenerico8, SiacDClassTipoEnum.Classificatore8);
		appendClassifCodeNativeFilter(where, joins, param, codiceClassificatoreGenerico9, SiacDClassTipoEnum.Classificatore9);
		appendClassifCodeNativeFilter(where, joins, param, codiceClassificatoreGenerico10, SiacDClassTipoEnum.Classificatore10);
		appendClassifCodeNativeFilter(where, joins, param, codiceClassificatoreGenerico31, SiacDClassTipoEnum.Classificatore31);
		appendClassifCodeNativeFilter(where, joins, param, codiceClassificatoreGenerico32, SiacDClassTipoEnum.Classificatore32);
		appendClassifCodeNativeFilter(where, joins, param, codiceClassificatoreGenerico33, SiacDClassTipoEnum.Classificatore33);
		appendClassifCodeNativeFilter(where, joins, param, codiceClassificatoreGenerico34, SiacDClassTipoEnum.Classificatore34);
		appendClassifCodeNativeFilter(where, joins, param, codiceClassificatoreGenerico35, SiacDClassTipoEnum.Classificatore35);
		appendClassifCodeNativeFilter(where, joins, param, codiceClassificatoreGenerico36, SiacDClassTipoEnum.Classificatore36);
		appendClassifCodeNativeFilter(where, joins, param, codiceClassificatoreGenerico37, SiacDClassTipoEnum.Classificatore37);
		appendClassifCodeNativeFilter(where, joins, param, codiceClassificatoreGenerico38, SiacDClassTipoEnum.Classificatore38);
		appendClassifCodeNativeFilter(where, joins, param, codiceClassificatoreGenerico39, SiacDClassTipoEnum.Classificatore39);
		appendClassifCodeNativeFilter(where, joins, param, codiceClassificatoreGenerico40, SiacDClassTipoEnum.Classificatore40);
		appendClassifCodeNativeFilter(where, joins, param, codiceClassificatoreGenerico41, SiacDClassTipoEnum.Classificatore41);
		appendClassifCodeNativeFilter(where, joins, param, codiceClassificatoreGenerico42, SiacDClassTipoEnum.Classificatore42);
		appendClassifCodeNativeFilter(where, joins, param, codiceClassificatoreGenerico43, SiacDClassTipoEnum.Classificatore43);
		appendClassifCodeNativeFilter(where, joins, param, codiceClassificatoreGenerico44, SiacDClassTipoEnum.Classificatore44);
		appendClassifCodeNativeFilter(where, joins, param, codiceClassificatoreGenerico45, SiacDClassTipoEnum.Classificatore45);
		appendClassifCodeNativeFilter(where, joins, param, codiceClassificatoreGenerico46, SiacDClassTipoEnum.Classificatore46);
		appendClassifCodeNativeFilter(where, joins, param, codiceClassificatoreGenerico47, SiacDClassTipoEnum.Classificatore47);
		appendClassifCodeNativeFilter(where, joins, param, codiceClassificatoreGenerico48, SiacDClassTipoEnum.Classificatore48);
		appendClassifCodeNativeFilter(where, joins, param, codiceClassificatoreGenerico49, SiacDClassTipoEnum.Classificatore49);
		appendClassifCodeNativeFilter(where, joins, param, codiceClassificatoreGenerico50, SiacDClassTipoEnum.Classificatore50);
		
		
		//classificatori gerarchici
		appendClassifIdsNativeFilter(where, joins, classifIdTitoloEntrataTipologiaCategoria, param, "classifIdTitoloEntrataTipologiaCategoria");
		appendClassifIdsNativeFilter(where, joins, classifIdTitoloUscitaMacroaggregato, param, "classifIdTitoloUscitaMacroaggregato");
		appendClassifIdsNativeFilter(where, joins, classifIdMissioneProgramma, param, "classifIdMissioneProgramma");
		appendClassifIdsNativeFilter(where, joins, classifIdPianoDeiConti, param, "classifIdPianoDeiConti");
		appendClassifIdsNativeFilter(where, joins, classifIdStruttAmmCont, param, "classifIdStruttAmmCont");
		appendClassifIdsNativeFilter(where, joins, classifIdSiopeEntrata, param, "classifIdSiopeEntrata");
		appendClassifIdsNativeFilter(where, joins, classifIdSiopeSpesa, param, "classifIdSiopeSpesa");
		appendClassifIdsNativeFilter(where, joins, classifIdCofog, param, "classifIdCofog");
		
		// SIAC-4088
		if(collegatoFondiDubbiaEsigibilita != null) {
			where.append(" AND ").append(Boolean.TRUE.equals(collegatoFondiDubbiaEsigibilita) ? "" : "NOT ").append(" EXISTS ( ");
			where.append("     SELECT 1 ");
			where.append("     FROM siac_r_bil_elem_acc_fondi_dubbia_esig rbeafde ");
			where.append("     JOIN siac_t_acc_fondi_dubbia_esig tafde ON (tafde.acc_fde_id = rbeafde.acc_fde_id AND tafde.data_cancellazione IS NULL) ");
			where.append("     WHERE rbeafde.elem_id = cup.elem_id ");
			where.append("     AND rbeafde.data_cancellazione IS NULL ");
			where.append(" ) ");
		}
		
		
		for(String joinCondition : joins.values()) {
			jpql.append(" JOIN ").append(joinCondition);
		}
		jpql.append(where);
	}

	/**
	 * Append atto legge filter.
	 *
	 * @param jpql the jpql
	 * @param param the param
	 * @param attoleggeNumero the attolegge numero
	 * @param attoleggeAnno the attolegge anno
	 * @param attoleggeArticolo the attolegge articolo
	 * @param attoleggeComma the attolegge comma
	 * @param attoleggePunto the attolegge punto
	 * @param attoleggeTipoCode the attolegge tipo code
	 */
	private void appendAttoLeggeFilter(StringBuilder jpql, Map<String, Object> param, Integer attoleggeNumero, String attoleggeAnno,
			String attoleggeArticolo, String attoleggeComma, String attoleggePunto, String attoleggeTipoCode) {
		if(!StringUtils.isBlank(attoleggeAnno) && 
				(attoleggeNumero!=null 
				|| !StringUtils.isBlank(attoleggeArticolo)
				|| !StringUtils.isBlank(attoleggeComma)
				|| !StringUtils.isBlank(attoleggePunto)
				|| !StringUtils.isBlank(attoleggeTipoCode))
				){
			jpql.append("       AND ( EXISTS (SELECT al ");
			jpql.append("                         FROM   cup.siacRBilElemAttoLegges al ");
			jpql.append("                         WHERE  ");
			jpql.append("                               al.siacTAttoLegge.attoleggeAnno = :attoleggeAnno  ");
			param.put("attoleggeAnno", attoleggeAnno);
			
			if(attoleggeNumero!=null){
				jpql.append("							AND al.siacTAttoLegge.attoleggeNumero = :attoleggeNumero ");
				param.put("attoleggeNumero", attoleggeNumero);
			}
			
			if(!StringUtils.isBlank(attoleggeArticolo)){
				jpql.append("                                AND  al.siacTAttoLegge.attoleggeArticolo = :attoleggeArticolo  ");
				param.put("attoleggeArticolo", attoleggeArticolo);
			}
			
			if(!StringUtils.isBlank(attoleggeComma)){
				jpql.append("                                AND al.siacTAttoLegge.attoleggeComma = :attoleggeComma  ");
				param.put("attoleggeComma", attoleggeComma);
			}
			
			if(!StringUtils.isBlank(attoleggePunto)){
				jpql.append("                                AND al.siacTAttoLegge.attoleggePunto = :attoleggePunto  ");
				param.put("attoleggePunto", attoleggePunto);
			}
			if(!StringUtils.isBlank(attoleggeTipoCode)){
				jpql.append("                                AND al.siacTAttoLegge.siacDAttoLeggeTipo.attoleggeTipoCode = :attoleggeTipoCode  ");
				param.put("attoleggeTipoCode", attoleggeTipoCode);
			}
			
			jpql.append("                                ) ) ");				
		}
	}
	
	private void appendAttoLeggeNativeFilter(StringBuilder where, Map<String, String> joins, Map<String, Object> param, Integer attoleggeNumero, String attoleggeAnno,
			String attoleggeArticolo, String attoleggeComma, String attoleggePunto, String attoleggeTipoCode) {
		if(StringUtils.isBlank(attoleggeAnno) || (attoleggeAnno == null && StringUtils.isBlank(attoleggeArticolo) && StringUtils.isBlank(attoleggeComma) || StringUtils.isBlank(attoleggePunto) || StringUtils.isBlank(attoleggeTipoCode))) {
			return;
		}
		joins.put("siac_r_bil_elem_atto_legge", "siac_r_bil_elem_atto_legge rbeal ON (rbeal.elem_id = cup.elem_id AND rbeal.data_cancellazione IS NULL)");
		joins.put("siac_t_atto_legge", "siac_t_atto_legge tal ON (tal.attolegge_id = rbeal.attolegge_id)");
		
		where.append(" AND tal.attolegge_anno = :attoleggeAnno ");
		param.put("attoleggeAnno", attoleggeAnno);
		
		if(attoleggeNumero != null){
			where.append(" AND tal.attolegge_numero = :attoleggeNumero ");
			param.put("attoleggeNumero", attoleggeNumero);
		}
		if(StringUtils.isNotBlank(attoleggeArticolo)){
			where.append(" AND tal.attolegge_articolo = :attoleggeArticolo ");
			param.put("attoleggeArticolo", attoleggeArticolo);
		}
		if(StringUtils.isNotBlank(attoleggeComma)){
			where.append(" AND tal.attolegge_comma = :attoleggeComma ");
			param.put("attoleggeComma", attoleggeComma);
		}
		if(StringUtils.isNotBlank(attoleggePunto)){
			where.append(" AND tal.attolegge_punto = :attoleggePunto ");
			param.put("attoleggePunto", attoleggePunto);
		}
		if(StringUtils.isNotBlank(attoleggeTipoCode)){
			joins.put("siac_d_atto_legge_tipo", "siac_d_atto_legge_tipo dalt ON (dalt.attolegge_tipo_id = tal.attolegge_tipo_id)");
			where.append(" AND dalt.attolegge_tipo_code = :attoleggeTipoCode ");
			param.put("attoleggeTipoCode", attoleggeTipoCode);
		}
	}

	/**
	 * Appen ex capitolo filter.
	 *
	 * @param jpql the jpql
	 * @param param the param
	 * @param exAnnoCapitolo the ex anno capitolo
	 * @param exNumeroCapitolo the ex numero capitolo
	 * @param exNumeroArticolo the ex numero articolo
	 * @param exNumeroUEB the ex numero ueb
	 * @param faseBilancio the fase bilancio
	 */
	private void appenExCapitoloFilter(StringBuilder jpql, Map<String, Object> param, 
			String exAnnoCapitolo, String exNumeroCapitolo, String exNumeroArticolo, String exNumeroUEB, String faseBilancio) {
		
		if (!StringUtils.isBlank(exAnnoCapitolo) || !StringUtils.isBlank(exNumeroCapitolo) || !StringUtils.isBlank(exNumeroArticolo)
				|| !StringUtils.isBlank(exNumeroUEB)) {
			
			jpql.append("       AND ( EXISTS (SELECT brt ");
			jpql.append("                         FROM   cup.siacRBilElemRelTempos brt ");
			jpql.append("                         WHERE  brt.siacTBilElem = cup ");
			if (!StringUtils.isBlank(exAnnoCapitolo)) {
				jpql.append("                                AND brt.siacTBilElemOld.siacTBil.siacTPeriodo.anno = :exAnnoCapitolo ");
				param.put("exAnnoCapitolo", faseBilancio);
			}
			if (!StringUtils.isBlank(exNumeroCapitolo)) {
				jpql.append("                                AND brt.siacTBilElemOld.elemCode = :exNumeroCapitolo ");
				param.put("exNumeroCapitolo", faseBilancio);
			}
			if (!StringUtils.isBlank(exNumeroArticolo)) {
				jpql.append("                                AND brt.siacTBilElemOld.elemCode2 = :exNumeroArticolo ");
				param.put("exNumeroArticolo", faseBilancio);
			}
			if (!StringUtils.isBlank(exNumeroUEB)) {
				jpql.append("                                AND brt.siacTBilElemOld.elemCode3 = :exNumeroUEB ");
				param.put("exNumeroUEB", faseBilancio);
			}

			jpql.append("                                ) ) ");

		}
	}
	
	private void appendExCapitoloNativeFilter(StringBuilder where, Map<String, String> joins, Map<String, Object> param,
			String exAnnoCapitolo, String exNumeroCapitolo, String exNumeroArticolo, String exNumeroUEB, String faseBilancio) {
		if(StringUtils.isBlank(exAnnoCapitolo) && StringUtils.isBlank(exNumeroCapitolo) && StringUtils.isBlank(exNumeroArticolo) && StringUtils.isBlank(exNumeroUEB)) {
			return;
		}
		joins.put("siac_r_bil_elem_rel_tempo", "siac_r_bil_elem_rel_tempo rbert ON (rbert.elem_id = cup.elem_id AND rbert.data_cancellazione IS NULL)");
		joins.put("siac_t_bil_elem::old", "siac_t_bil_elem tbe_old ON (tbe_old.elem_id = rbert.elem_id_old)");
		
		if (StringUtils.isNotBlank(exAnnoCapitolo)) {
			joins.put("siac_t_bil::old", "siac_t_bil tb_old ON (tb_old.bil_id = tbe_old.bil_id)");
			joins.put("siac_t_periodo::old", "siac_t_periodo tp_old ON (tp_old.periodo_id = tb_old.periodo_id)");
			
			where.append(" AND tp_old.anno = :exAnnoCapitolo");
			param.put("exAnnoCapitolo", faseBilancio);
		}
		if (StringUtils.isNotBlank(exNumeroCapitolo)) {
			where.append(" AND tbe_old.elem_code = :exNumeroCapitolo ");
			param.put("exNumeroCapitolo", exNumeroCapitolo);
		}
		if (StringUtils.isNotBlank(exNumeroArticolo)) {
			where.append(" AND tbe_old.elem_code2 = :exNumeroArticolo ");
			param.put("exNumeroArticolo", exNumeroArticolo);
		}
		if (StringUtils.isNotBlank(exNumeroUEB)) {
			where.append(" AND tbe_old.elem_code3 = :exNumeroUEB ");
			param.put("exNumeroUEB", exNumeroUEB);
		}
	}
	
	/**
	 * Append flag filter.
	 *
	 * @param jpql the jpql
	 * @param param the param
	 * @param flagValue the flag value
	 * @param attrType the attr type
	 */
	private void appendFlagFilter(StringBuilder jpql, Map<String, Object> param, String flagValue, SiacTAttrEnum attrType) {
		if(!StringUtils.isBlank(flagValue)){
			
			String paramName =  attrType.name();
			
			jpql.append("       AND ( EXISTS (SELECT bea ");
			jpql.append("                         FROM   SiacRBilElemAttr bea ");
			jpql.append("                         WHERE  bea.boolean_ = :"+paramName+" ");
			jpql.append("							  	 AND bea.siacTAttr.attrCode = '"+attrType.getCodice()+"'");
			jpql.append("                                AND bea.siacTBilElem = cup ) ) ");		
			
			param.put(paramName, flagValue);
		}
	}
	
	/**
	 * Append flag filter.
	 *
	 * @param jpql the jpql
	 * @param param the param
	 * @param flagValue the flag value
	 * @param attrType the attr type
	 */
	private void appendFlagNativeFilter(StringBuilder where, Map<String, String> joins, Map<String, Object> param, String flagValue, SiacTAttrEnum attrType) {
		if(StringUtils.isBlank(flagValue)) {
			return;
		}
		
		where.append(" AND EXISTS ( ");
		where.append("     SELECT 1 ");
		where.append("     FROM siac_r_bil_elem_attr rbea ");
		where.append("     JOIN siac_t_attr ta ON (ta.attr_id = rbea.attr_id) ");
		where.append("     WHERE rbea.data_cancellazione IS NULL ");
		where.append("     AND rbea.elem_id = cup.elem_id ");
		where.append("     AND rbea.boolean = :").append(attrType.name());
		where.append("     AND ta.attr_code = '").append(attrType.getCodice()).append("'");
		where.append(" ) ");
		
		param.put(attrType.name(), flagValue);
	}
	
	/**
	 * Append classif code filter.
	 *
	 * @param jpql the jpql
	 * @param param the param
	 * @param classifCode the classif code
	 * @param classifType the classif type
	 */
	private void appendClassifCodeFilter(StringBuilder jpql, Map<String, Object> param, String classifCode, SiacDClassTipoEnum classifType) {
		if(!StringUtils.isBlank(classifCode)){
			
			String paramName = "codice"+classifType.name();
			
			jpql.append("		AND (EXISTS (SELECT cod ");
			jpql.append("                         FROM   SiacRBilElemClass cod ");
			jpql.append("                         WHERE  cod.siacTClass.siacDClassTipo.classifTipoCode = '"+classifType.getCodice()+"' ");
			jpql.append("                                AND cod.siacTClass.classifCode = :"+paramName+" ");
			jpql.append("							   	   AND cod.siacTBilElem = cup )   ) ");	
			param.put(paramName, classifCode);
		}
	}
	
	/**
	 * Append classif code filter.
	 *
	 * @param jpql the jpql
	 * @param param the param
	 * @param classifCode the classif code
	 * @param classifType the classif type
	 */
	private void appendClassifCodeNativeFilter(StringBuilder where, Map<String, String> joins, Map<String, Object> param, String classifCode, SiacDClassTipoEnum classifType) {
		if(StringUtils.isBlank(classifCode)){
			return;
		}
		String paramName = "codice" + classifType.name();
		
		where.append(" AND EXISTS ( ");
		where.append("     SELECT 1 ");
		where.append("     FROM siac_r_bil_elem_class rbecl ");
		where.append("     JOIN siac_t_class tc ON (tc.classif_id = rbecl.classif_id) ");
		where.append("     JOIN siac_d_class_tipo dct ON (dct.classif_tipo_id = tc.classif_tipo_id) ");
		where.append("     WHERE rbecl.elem_id = cup.elem_id ");
		where.append("     AND rbecl.data_cancellazione IS NULL ");
		where.append("     AND dct.classif_tipo_code = '").append(classifType.getCodice()).append("'");
		where.append("     AND tc.classif_code = :").append(paramName);
		where.append(" ) ");
		param.put(paramName, classifCode);
	}
	

	/**
	 * Append classif ids filter.
	 *
	 * @param jpql the jpql
	 * @param classifIds the classif ids
	 * @param param the param
	 * @param paramName the param name
	 */
	private void appendClassifIdsFilter(StringBuilder jpql, List<Integer> classifIds, Map<String, Object> param, String paramName) {
		if(classifIds!= null && !classifIds.isEmpty()){
			jpql.append("       AND ( EXISTS (SELECT cod ");
			jpql.append("                         FROM   SiacRBilElemClass cod ");
			jpql.append("                         WHERE  cod.siacTClass.classifId IN :"+paramName+" ");
			jpql.append("							   	   AND cod.siacTBilElem = cup )   ) "); 	
			param.put(paramName, classifIds);
		}
	}
	
	private void appendClassifIdsNativeFilter(StringBuilder where, Map<String, String> joins, List<Integer> classifIds, Map<String, Object> param, String paramName) {
		if(classifIds == null || classifIds.isEmpty()){
			return;
		}
		where.append(" AND EXISTS ( ");
		where.append("     SELECT 1 ");
		where.append("     FROM siac_r_bil_elem_class rbecl ");
		where.append("     WHERE rbecl.elem_id = cup.elem_id ");
		where.append("     AND rbecl.data_cancellazione IS NULL ");
		where.append("     AND rbecl.classif_id IN (:").append(paramName).append(") ");
		where.append(" ) ");
		param.put(paramName, classifIds);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.elementobilancio.CapitoloDao#countRicercaSinteticaCapitolo(it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario, java.lang.String, it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Long countRicercaSinteticaCapitolo(SiacTEnteProprietario enteDto, String annoEsercizio, SiacDBilElemTipoEnum tipoCapitolo, Integer uidCategoriaCapitolo, String codiceCategoriaCapitolo,
			String annoCapitolo, String numeroCapitolo, String numeroArticolo, String numeroUEB, String stato, String exAnnoCapitolo,
			String exNumeroCapitolo, String exNumeroArticolo, String exNumeroUEB, String faseBilancio, String descrizioneCapitolo,
			String descrizioneArticolo, String flagAssegnabile, String flagFondoSvalutazioneCrediti, String flagFunzioniDelegate,
			String flagPerMemoria, String flagRilevanteIva, String flagTrasferimentoOrganiComunitari, String flagEntrateRicorrenti, String flagFondoPluriennaleVinc,
			String codiceTipoFinanziamento, String codiceTipoFondo,	String codiceTipoVincolo,
			String codiceRicorrenteEntrata, String codiceRicorrenteSpesa,
			String codicePerimetroSanitarioEntrata, String codicePerimetroSanitarioSpesa,
			String codiceTransazioneUnioneEuropeaEntrata, String codiceTransazioneUnioneEuropeaSpesa,
			String codicePoliticheRegionaliUnitarie, 
			
			String codiceClassificatoreGenerico1,
			String codiceClassificatoreGenerico2, String codiceClassificatoreGenerico3, String codiceClassificatoreGenerico4,
			String codiceClassificatoreGenerico5, String codiceClassificatoreGenerico6, String codiceClassificatoreGenerico7,
			String codiceClassificatoreGenerico8, String codiceClassificatoreGenerico9, String codiceClassificatoreGenerico10,
			
			String codiceClassificatoreGenerico31,
			String codiceClassificatoreGenerico32,
			String codiceClassificatoreGenerico33,
			String codiceClassificatoreGenerico34,
			String codiceClassificatoreGenerico35,
			String codiceClassificatoreGenerico36,
			String codiceClassificatoreGenerico37,
			String codiceClassificatoreGenerico38,
			String codiceClassificatoreGenerico39,
			String codiceClassificatoreGenerico40,
			String codiceClassificatoreGenerico41,
			String codiceClassificatoreGenerico42,
			String codiceClassificatoreGenerico43,
			String codiceClassificatoreGenerico44,
			String codiceClassificatoreGenerico45,
			String codiceClassificatoreGenerico46,
			String codiceClassificatoreGenerico47,
			String codiceClassificatoreGenerico48,
			String codiceClassificatoreGenerico49,
			String codiceClassificatoreGenerico50,
			
			
			String codicePianoDeiConti, String codiceCofog, String codiceTipoCofog, String codiceStruttAmmCont, String codiceTipoStruttAmmCont,
			
			String codiceSiopeEntrata, String codiceTipoSiopeEntrata,
			String codiceSiopeSpesa, String codiceTipoSiopeSpesa,
			
			String codiceMissione, String codiceProgrmma, String codiceTitoloSpesa, String codiceMacroaggregato, String codiceTitoloEntrata,
			String codiceTipologia, String codiceCategoria,
			Integer attoleggeNumero,
			String attoleggeAnno,
			String attoleggeArticolo,
			String attoleggeComma,
			String attoleggePunto,
			String attoleggeTipoCode,
			Boolean collegatoFondiDubbiaEsigibilita
			) {
		
		
		
		return countRicercaSinteticaCapitolo(enteDto,annoEsercizio,tipoCapitolo,uidCategoriaCapitolo,codiceCategoriaCapitolo,annoCapitolo,numeroCapitolo,numeroArticolo,numeroUEB,stato, 
				
				exAnnoCapitolo,
				exNumeroCapitolo,
				exNumeroArticolo,
				exNumeroUEB,
				
				faseBilancio,
				descrizioneCapitolo,
				descrizioneArticolo,
				
				flagAssegnabile,
				flagFondoSvalutazioneCrediti,
				flagFunzioniDelegate,
				flagPerMemoria,
				flagRilevanteIva,
				flagTrasferimentoOrganiComunitari,	
				flagEntrateRicorrenti,
				flagFondoPluriennaleVinc,
				
				codiceTipoFinanziamento,
				codiceTipoFondo,
				codiceTipoVincolo,
				codiceRicorrenteEntrata, codiceRicorrenteSpesa,
				codicePerimetroSanitarioEntrata, codicePerimetroSanitarioSpesa,
				codiceTransazioneUnioneEuropeaEntrata, codiceTransazioneUnioneEuropeaSpesa,
				codicePoliticheRegionaliUnitarie, 
				
				codiceClassificatoreGenerico1,
				codiceClassificatoreGenerico2,
				codiceClassificatoreGenerico3,
				codiceClassificatoreGenerico4,
				codiceClassificatoreGenerico5,
				codiceClassificatoreGenerico6,
				codiceClassificatoreGenerico7,
				codiceClassificatoreGenerico8,
				codiceClassificatoreGenerico9,
				codiceClassificatoreGenerico10,
				
				codiceClassificatoreGenerico31,
				codiceClassificatoreGenerico32,
				codiceClassificatoreGenerico33,
				codiceClassificatoreGenerico34,
				codiceClassificatoreGenerico35,
				codiceClassificatoreGenerico36,
				codiceClassificatoreGenerico37,
				codiceClassificatoreGenerico38,
				codiceClassificatoreGenerico39,
				codiceClassificatoreGenerico40,
				codiceClassificatoreGenerico41,
				codiceClassificatoreGenerico42,
				codiceClassificatoreGenerico43,
				codiceClassificatoreGenerico44,
				codiceClassificatoreGenerico45,
				codiceClassificatoreGenerico46,
				codiceClassificatoreGenerico47,
				codiceClassificatoreGenerico48,
				codiceClassificatoreGenerico49,
				codiceClassificatoreGenerico50,
				
				findFigliClassificatoreIdsPianoDeiConti(annoEsercizio, enteDto, codicePianoDeiConti),
				findFigliClassificatoreIdsCofog(annoEsercizio, enteDto, codiceCofog, codiceTipoCofog),
				findFigliClassificatoreIdsStrutturaAmministrativaContabile(annoEsercizio, enteDto, codiceStruttAmmCont, codiceTipoStruttAmmCont),
				findFigliClassificatoreIdsSiopeEntrata(annoEsercizio, enteDto, codiceSiopeEntrata, codiceTipoSiopeEntrata),
				findFigliClassificatoreIdsSiopeSpesa(annoEsercizio, enteDto, codiceSiopeSpesa, codiceTipoSiopeSpesa),
				findFigliClassificatoreIdsMissioneProgramma(annoEsercizio, enteDto, codiceMissione, codiceProgrmma), 
				findFigliClassificatoreIdsTitoloUscitaMacroaggregato(annoEsercizio, enteDto, codiceTitoloSpesa, codiceMacroaggregato), 
				findFigliClassificatoreIdsEntrataTipologiaCategoria(annoEsercizio, enteDto, codiceTitoloEntrata, codiceTipologia, codiceCategoria),
				
				attoleggeNumero,
				attoleggeAnno,
				attoleggeArticolo,
				attoleggeComma,
				attoleggePunto,
				attoleggeTipoCode,
				
				collegatoFondiDubbiaEsigibilita
				);
		
		
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.elementobilancio.CapitoloDao#countRicercaSinteticaCapitolo(it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario, java.lang.String, it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.List, java.util.List, java.util.List, java.util.List, java.util.List, java.util.List, java.util.List, java.util.List, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Long countRicercaSinteticaCapitolo(SiacTEnteProprietario enteDto, String annoEsercizio, SiacDBilElemTipoEnum tipoCapitolo, Integer uidCategoriaCapitolo, String codiceCategoriaCapitolo,
			String annoCapitolo, String numeroCapitolo, String numeroArticolo, String numeroUEB, String stato, String exAnnoCapitolo,
			String exNumeroCapitolo, String exNumeroArticolo, String exNumeroUEB, String faseBilancio, String descrizioneCapitolo,
			String descrizioneArticolo, String flagAssegnabile, String flagFondoSvalutazioneCrediti, String flagFunzioniDelegate,
			String flagPerMemoria, String flagRilevanteIva, String flagTrasferimentoOrganiComunitari, String flagEntrateRicorrenti,
			String flagFondoPluriennaleVinc,
			String codiceTipoFinanziamento, String codiceTipoFondo, String codiceTipoVincolo,
			String codiceRicorrenteEntrata, String codiceRicorrenteSpesa,
			String codicePerimetroSanitarioEntrata, String codicePerimetroSanitarioSpesa,
			String codiceTransazioneUnioneEuropeaEntrata, String codiceTransazioneUnioneEuropeaSpesa,
			String codicePoliticheRegionaliUnitarie,  String codiceClassificatoreGenerico1,
			String codiceClassificatoreGenerico2, String codiceClassificatoreGenerico3, String codiceClassificatoreGenerico4,
			String codiceClassificatoreGenerico5, String codiceClassificatoreGenerico6, String codiceClassificatoreGenerico7,
			String codiceClassificatoreGenerico8, String codiceClassificatoreGenerico9, String codiceClassificatoreGenerico10,
			
			String codiceClassificatoreGenerico31,
			String codiceClassificatoreGenerico32,
			String codiceClassificatoreGenerico33,
			String codiceClassificatoreGenerico34,
			String codiceClassificatoreGenerico35,
			String codiceClassificatoreGenerico36,
			String codiceClassificatoreGenerico37,
			String codiceClassificatoreGenerico38,
			String codiceClassificatoreGenerico39,
			String codiceClassificatoreGenerico40,
			String codiceClassificatoreGenerico41,
			String codiceClassificatoreGenerico42,
			String codiceClassificatoreGenerico43,
			String codiceClassificatoreGenerico44,
			String codiceClassificatoreGenerico45,
			String codiceClassificatoreGenerico46,
			String codiceClassificatoreGenerico47,
			String codiceClassificatoreGenerico48,
			String codiceClassificatoreGenerico49,
			String codiceClassificatoreGenerico50,
			
			
			List<Integer> classifIdPianoDeiConti, List<Integer> classifIdCofog, List<Integer> classifIdStruttAmmCont,
			List<Integer> classifIdSiopeEntrata,  
			List<Integer> classifIdSiopeSpesa,  
			List<Integer> classifIdMissioneProgramma, List<Integer> classifIdTitoloUscitaMacroaggregato,
			List<Integer> classifIdTitoloEntrataTipologiaCategoria,
			Integer attoleggeNumero,
			String attoleggeAnno,
			String attoleggeArticolo,
			String attoleggeComma,
			String attoleggePunto,
			String attoleggeTipoCode,
			Boolean collegatoFondiDubbiaEsigibilita) {
				
		
		StringBuilder jpql = new StringBuilder();
		Map<String,Object> param = new HashMap<String, Object>();
		
		jpql.append("SELECT CONCAT(cup.elemCode,' ',cup.elemCode2,' ',count(*)) ");
		componiQueryRicercaSinteticaCapitolo(jpql, param, enteDto, annoEsercizio, tipoCapitolo, uidCategoriaCapitolo,codiceCategoriaCapitolo, annoCapitolo, numeroCapitolo, numeroArticolo, numeroUEB, stato, exAnnoCapitolo,
				exNumeroCapitolo, exNumeroArticolo, exNumeroUEB, faseBilancio, descrizioneCapitolo, descrizioneArticolo, flagAssegnabile,
				flagFondoSvalutazioneCrediti, flagFunzioniDelegate, flagPerMemoria, flagRilevanteIva, flagTrasferimentoOrganiComunitari,
				flagEntrateRicorrenti, flagFondoPluriennaleVinc, codiceTipoFinanziamento, codiceTipoFondo, codiceTipoVincolo,
				codiceRicorrenteEntrata, codiceRicorrenteSpesa,
				codicePerimetroSanitarioEntrata, codicePerimetroSanitarioSpesa,
				codiceTransazioneUnioneEuropeaEntrata, codiceTransazioneUnioneEuropeaSpesa,
				codicePoliticheRegionaliUnitarie, 
				codiceClassificatoreGenerico1,
				codiceClassificatoreGenerico2, codiceClassificatoreGenerico3, codiceClassificatoreGenerico4, codiceClassificatoreGenerico5,
				codiceClassificatoreGenerico6, codiceClassificatoreGenerico7, codiceClassificatoreGenerico8, codiceClassificatoreGenerico9,
				codiceClassificatoreGenerico10,
				
				codiceClassificatoreGenerico31,
				codiceClassificatoreGenerico32,
				codiceClassificatoreGenerico33,
				codiceClassificatoreGenerico34,
				codiceClassificatoreGenerico35,
				codiceClassificatoreGenerico36,
				codiceClassificatoreGenerico37,
				codiceClassificatoreGenerico38,
				codiceClassificatoreGenerico39,
				codiceClassificatoreGenerico40,
				codiceClassificatoreGenerico41,
				codiceClassificatoreGenerico42,
				codiceClassificatoreGenerico43,
				codiceClassificatoreGenerico44,
				codiceClassificatoreGenerico45,
				codiceClassificatoreGenerico46,
				codiceClassificatoreGenerico47,
				codiceClassificatoreGenerico48,
				codiceClassificatoreGenerico49,
				codiceClassificatoreGenerico50,
				
				
				
				classifIdPianoDeiConti, classifIdCofog, classifIdStruttAmmCont,				
				classifIdSiopeEntrata,
				classifIdSiopeSpesa,				
				classifIdMissioneProgramma,
				classifIdTitoloUscitaMacroaggregato, classifIdTitoloEntrataTipologiaCategoria,				
				attoleggeNumero,
				attoleggeAnno,
				attoleggeArticolo,
				attoleggeComma,
				attoleggePunto,
				attoleggeTipoCode,
				collegatoFondiDubbiaEsigibilita);
		jpql.append(" GROUP BY cup.elemCode, cup.elemCode2 ");
		
		Query query = createQuery(jpql.toString(), param);		

		@SuppressWarnings("unchecked")
		List<String> result = query.getResultList();
		
		return (long) result.size();
	}
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.elementobilancio.CapitoloDao#findImportoDerivato(it.csi.siac.siacbilser.integration.entity.SiacTBilElem, java.lang.String)
	 */
	@Override
	public BigDecimal findImportoDerivato(SiacTBilElem siacTBilElem, String functionName) {		
		return findImportoDerivato(siacTBilElem.getElemId(), functionName);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.elementobilancio.CapitoloDao#findImportoDerivato(java.lang.Integer, java.lang.String)
	 */
	@Override
	public BigDecimal findImportoDerivato(Integer bilElemId , String functionName) {
		final String methodName = "findImportoDerivato";
		
		Query query = entityManager.createNativeQuery("SELECT "+ functionName + "(:bilElemId)");
		
		query.setParameter("bilElemId", bilElemId);		
		
		long startTimeMillis = System.currentTimeMillis();
		BigDecimal result = (BigDecimal) query.getSingleResult();
		
		long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
		
		log.debug(methodName, "Returning result: "+ result + " for bilElemId: "+ bilElemId + " and functionName: "+ functionName + " elapsed millis: " + elapsedTimeMillis);
		
		// SIAC-3770
		if(result == null) {
			log.warn(methodName, "L'invocazione della function " + functionName + " per capitolo " + bilElemId + " restituisce null. Risultato non accettabile: ritorno 0");
			result = BigDecimal.ZERO;
		}
		
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> importiRicercaSinteticaCapitolo(SiacTEnteProprietario enteDto, String annoEsercizio, SiacDBilElemTipoEnum tipoCapitolo, Integer uidCategoriaCapitolo,
			String codiceCategoriaCapitolo, String annoCapitolo, String numeroCapitolo, String numeroArticolo, String numeroUEB, String stato, String exAnnoCapitolo, String exNumeroCapitolo,
			String exNumeroArticolo, String exNumeroUEB, String faseBilancio, String descrizioneCapitolo, String descrizioneArticolo, String flagAssegnabile, String flagFondoSvalutazioneCrediti,
			String flagFunzioniDelegate, String flagPerMemoria, String flagRilevanteIva, String flagTrasferimentoOrganiComunitari, String flagEntrateRicorrenti, String flagFondoPluriennaleVinc,
			String codiceTipoFinanziamento, String codiceTipoFondo, String codiceTipoVincolo, String codiceRicorrenteEntrata, String codiceRicorrenteSpesa,
			String codicePerimetroSanitarioEntrata, String codicePerimetroSanitarioSpesa, String codiceTransazioneUnioneEuropeaEntrata, String codiceTransazioneUnioneEuropeaSpesa,
			String codicePoliticheRegionaliUnitarie, String codiceClassificatoreGenerico1, String codiceClassificatoreGenerico2, String codiceClassificatoreGenerico3,
			String codiceClassificatoreGenerico4, String codiceClassificatoreGenerico5, String codiceClassificatoreGenerico6, String codiceClassificatoreGenerico7,
			String codiceClassificatoreGenerico8, String codiceClassificatoreGenerico9, String codiceClassificatoreGenerico10, String codiceClassificatoreGenerico31,
			String codiceClassificatoreGenerico32, String codiceClassificatoreGenerico33, String codiceClassificatoreGenerico34, String codiceClassificatoreGenerico35,
			String codiceClassificatoreGenerico36, String codiceClassificatoreGenerico37, String codiceClassificatoreGenerico38, String codiceClassificatoreGenerico39,
			String codiceClassificatoreGenerico40, String codiceClassificatoreGenerico41, String codiceClassificatoreGenerico42, String codiceClassificatoreGenerico43,
			String codiceClassificatoreGenerico44, String codiceClassificatoreGenerico45, String codiceClassificatoreGenerico46, String codiceClassificatoreGenerico47,
			String codiceClassificatoreGenerico48, String codiceClassificatoreGenerico49, String codiceClassificatoreGenerico50, String codicePianoDeiConti,
			String codiceCofog, String codiceTipoCofog, String codiceStruttAmmCont, String codiceTipoStruttAmmCont, String codiceSiopeEntrata, String codiceTipoSiopeEntrata,
			String codiceSiopeSpesa, String codiceTipoSiopeSpesa, String codiceMissione, String codiceProgrmma, String codiceTitoloSpesa, String codiceMacroaggregato,
			String codiceTitoloEntrata, String codiceTipologia, String codiceCategoria, Integer attoleggeNumero, String attoleggeAnno, String attoleggeArticolo,
			String attoleggeComma, String attoleggePunto, String attoleggeTipoCode, Boolean collegatoFondiDubbiaEsigibilita) {
		
		StringBuilder jpql = new StringBuilder();
		Map<String,Object> param = new HashMap<String, Object>();
		
		// Calcolo in nativo
		jpql.append(" WITH capitoli AS (");
		jpql.append("     SELECT cup.elem_id ");
		componiQueryRicercaSinteticaNativaCapitolo(jpql, param,
				enteDto.getUid(),
				annoEsercizio, tipoCapitolo.getCodice(),
				uidCategoriaCapitolo,
				codiceCategoriaCapitolo,
				annoCapitolo,
				numeroCapitolo,
				numeroArticolo,
				numeroUEB,
				stato,
				exAnnoCapitolo,
				exNumeroCapitolo,
				exNumeroArticolo,
				exNumeroUEB,
				faseBilancio,
				descrizioneCapitolo,
				descrizioneArticolo,
				flagAssegnabile,
				flagFondoSvalutazioneCrediti,
				flagFunzioniDelegate,
				flagPerMemoria,
				flagRilevanteIva,
				flagTrasferimentoOrganiComunitari,
				flagEntrateRicorrenti,
				flagFondoPluriennaleVinc,
				codiceTipoFinanziamento,
				codiceTipoFondo,
				codiceTipoVincolo,
				codiceRicorrenteEntrata,
				codiceRicorrenteSpesa,
				codicePerimetroSanitarioEntrata,
				codicePerimetroSanitarioSpesa,
				codiceTransazioneUnioneEuropeaEntrata,
				codiceTransazioneUnioneEuropeaSpesa,
				codicePoliticheRegionaliUnitarie,
				codiceClassificatoreGenerico1,
				codiceClassificatoreGenerico2,
				codiceClassificatoreGenerico3,
				codiceClassificatoreGenerico4,
				codiceClassificatoreGenerico5,
				codiceClassificatoreGenerico6,
				codiceClassificatoreGenerico7,
				codiceClassificatoreGenerico8,
				codiceClassificatoreGenerico9,
				codiceClassificatoreGenerico10,
				codiceClassificatoreGenerico31,
				codiceClassificatoreGenerico32,
				codiceClassificatoreGenerico33,
				codiceClassificatoreGenerico34,
				codiceClassificatoreGenerico35,
				codiceClassificatoreGenerico36,
				codiceClassificatoreGenerico37,
				codiceClassificatoreGenerico38,
				codiceClassificatoreGenerico39,
				codiceClassificatoreGenerico40,
				codiceClassificatoreGenerico41,
				codiceClassificatoreGenerico42,
				codiceClassificatoreGenerico43,
				codiceClassificatoreGenerico44,
				codiceClassificatoreGenerico45,
				codiceClassificatoreGenerico46,
				codiceClassificatoreGenerico47,
				codiceClassificatoreGenerico48,
				codiceClassificatoreGenerico49,
				codiceClassificatoreGenerico50,
				findFigliClassificatoreIdsPianoDeiConti(annoEsercizio, enteDto, codicePianoDeiConti),
				findFigliClassificatoreIdsCofog(annoEsercizio, enteDto, codiceCofog, codiceTipoCofog),
				findFigliClassificatoreIdsStrutturaAmministrativaContabile(annoEsercizio, enteDto, codiceStruttAmmCont, codiceTipoStruttAmmCont),
				findFigliClassificatoreIdsSiopeEntrata(annoEsercizio, enteDto, codiceSiopeEntrata, codiceTipoSiopeEntrata),
				findFigliClassificatoreIdsSiopeSpesa(annoEsercizio, enteDto, codiceSiopeSpesa, codiceTipoSiopeSpesa),
				findFigliClassificatoreIdsMissioneProgramma(annoEsercizio, enteDto, codiceMissione, codiceProgrmma), 
				findFigliClassificatoreIdsTitoloUscitaMacroaggregato(annoEsercizio, enteDto, codiceTitoloSpesa, codiceMacroaggregato), 
				findFigliClassificatoreIdsEntrataTipologiaCategoria(annoEsercizio, enteDto, codiceTitoloEntrata, codiceTipologia, codiceCategoria),
				attoleggeNumero,
				attoleggeAnno,
				attoleggeArticolo,
				attoleggeComma,
				attoleggePunto,
				attoleggeTipoCode,
				collegatoFondiDubbiaEsigibilita);
		
		jpql.append(" ) ");
		jpql.append(" SELECT dbedt.elem_det_tipo_code, COALESCE(SUM(tbed.elem_det_importo), 0) ");
		jpql.append(" FROM capitoli ");
		jpql.append(" JOIN siac_t_bil_elem_det tbed ON (tbed.elem_id = capitoli.elem_id AND tbed.data_cancellazione IS NULL) ");
		jpql.append(" JOIN siac_d_bil_elem_det_tipo dbedt ON (dbedt.elem_det_tipo_id = tbed.elem_det_tipo_id) ");
		jpql.append(" JOIN siac_t_periodo tp ON (tp.periodo_id = tbed.periodo_id) ");
		jpql.append(" WHERE tp.anno = :annoImporto ");
		jpql.append(" GROUP BY dbedt.elem_det_tipo_code ");
		
		param.put("annoImporto", annoEsercizio);
		
		Query query = createNativeQuery(jpql.toString(), param);
		return query.getResultList();
	}

	@Override
	public Long countMovgestNonAnnullatiByBilElemIdsAndMovgestAnnoAndOperator(List<Integer> elemIds, Integer movgestAnno, CompareOperator jpqlCompareOperator) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT COALESCE(COUNT(tm1), 0) ");
		jpql.append(" FROM SiacTMovgest tm1 ");
		jpql.append(" WHERE tm1.movgestId IN ( ");
		jpql.append("     SELECT DISTINCT tm.movgestId ");
		jpql.append("     FROM SiacTMovgest tm, SiacRMovgestBilElem rmbe, SiacTMovgestT tmt, SiacRMovgestTsStato rmts ");
		jpql.append("     WHERE rmbe.siacTMovgest = tm ");
		jpql.append("     AND tmt.siacTMovgest = tm ");
		jpql.append("     AND rmts.siacTMovgestT = tmt ");
		jpql.append("     AND tm.dataCancellazione IS NULL ");
		jpql.append("     AND tm.dataFineValidita IS NULL ");
		jpql.append("     AND rmbe.dataCancellazione IS NULL ");
		jpql.append("     AND rmbe.dataFineValidita IS NULL ");
		jpql.append("     AND tmt.dataCancellazione IS NULL ");
		jpql.append("     AND tmt.dataFineValidita IS NULL ");
		jpql.append("     AND rmts.dataCancellazione IS NULL ");
		jpql.append("     AND rmts.dataFineValidita IS NULL ");
		jpql.append("     AND tm.movgestAnno ").append(jpqlCompareOperator.getJpql()).append(" :movgestAnno ");
		jpql.append("     AND rmbe.siacTBilElem.elemId IN (:elemIds) ");
		jpql.append("     AND rmts.siacDMovgestStato.movgestStatoCode <> 'A' ");
		jpql.append(" ) ");
		
		param.put("elemIds", elemIds);
		param.put("movgestAnno", movgestAnno);
		
		Query query = createQuery(jpql.toString(), param);
		return (Long) query.getSingleResult();
	}

	@Override
	public Long countLiquidazioniNonAnnullateByBilElemIdsAndLiqAnnoAndOperator(List<Integer> elemIds, Integer movgestAnno, CompareOperator jpqlCompareOperator) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT COALESCE(COUNT(tl1), 0) ");
		jpql.append(" FROM SiacTLiquidazione tl1 ");
		jpql.append(" WHERE tl1.liqId IN ( ");
		jpql.append("     SELECT DISTINCT tl.liqId ");
		jpql.append("     FROM SiacTLiquidazione tl, SiacRLiquidazioneMovgest rlm, SiacTMovgest tm, SiacRMovgestBilElem rmbe, SiacRLiquidazioneStato rls ");
		jpql.append("     WHERE rlm.siacTLiquidazione = tl ");
		jpql.append("     AND rlm.siacTMovgestT.siacTMovgest = tm ");
		jpql.append("     AND rmbe.siacTMovgest = tm ");
		jpql.append("     AND rls.siacTLiquidazione = tl ");
		jpql.append("     AND tm.dataCancellazione IS NULL ");
		jpql.append("     AND tm.dataFineValidita IS NULL ");
		jpql.append("     AND rmbe.dataCancellazione IS NULL ");
		jpql.append("     AND rmbe.dataFineValidita IS NULL ");
		jpql.append("     AND rls.dataCancellazione IS NULL ");
		jpql.append("     AND rls.dataFineValidita IS NULL ");
		jpql.append("     AND tm.movgestAnno ").append(jpqlCompareOperator.getJpql()).append(" :movgestAnno ");
		jpql.append("     AND rmbe.siacTBilElem.elemId IN (:elemIds) ");
		jpql.append("     AND rls.siacDLiquidazioneStato.liqStatoCode <> 'A' ");
		jpql.append(" ) ");
		
		param.put("elemIds", elemIds);
		param.put("movgestAnno", movgestAnno);

		Query query = createQuery(jpql.toString(), param);
		return (Long) query.getSingleResult();
	}



	@Override
	public BigDecimal sumMovgestImportoNonAnnullatiByBilElemIdsAndMovgestAnnoAndOperator(List<Integer> elemIds, Integer movgestAnno, CompareOperator jpqlCompareOperator) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		jpql.append(" SELECT COALESCE(SUM(tmtd1.movgestTsDetImporto), 0) ");
		jpql.append(" FROM SiacTMovgestTsDet tmtd1 ");
		jpql.append(" WHERE tmtd1.movgestTsDetId IN ( ");
		jpql.append("     SELECT DISTINCT tmtd.movgestTsDetId ");
		jpql.append("     FROM SiacTMovgestTsDet tmtd, SiacTMovgestT tmt, SiacTMovgest tm, SiacRMovgestBilElem rmbe, SiacRMovgestTsStato rmts ");
		jpql.append("     WHERE tmtd.siacTMovgestT = tmt ");
		jpql.append("     AND rmbe.siacTMovgest = tm ");
		jpql.append("     AND tmt.siacTMovgest = tm ");
		jpql.append("     AND rmts.siacTMovgestT = tmt ");
		jpql.append("     AND tmtd.dataCancellazione IS NULL ");
		jpql.append("     AND tm.dataCancellazione IS NULL ");
		jpql.append("     AND tm.dataFineValidita IS NULL ");
		jpql.append("     AND rmbe.dataCancellazione IS NULL ");
		jpql.append("     AND rmbe.dataFineValidita IS NULL ");
		jpql.append("     AND tmt.dataCancellazione IS NULL ");
		jpql.append("     AND tmt.dataFineValidita IS NULL ");
		jpql.append("     AND rmts.dataCancellazione IS NULL ");
		jpql.append("     AND rmts.dataFineValidita IS NULL ");
		jpql.append("     AND tmt.siacDMovgestTsTipo.movgestTsTipoCode = 'T' ");
		jpql.append("     AND tm.movgestAnno ").append(jpqlCompareOperator.getJpql()).append(" :movgestAnno ");
		jpql.append("     AND rmbe.siacTBilElem.elemId IN (:elemIds) ");
		jpql.append("     AND rmts.siacDMovgestStato.movgestStatoCode <> 'A' ");
		jpql.append("     AND tmtd.siacDMovgestTsDetTipo.movgestTsDetTipoCode = 'A' ");
		jpql.append(" ) ");
		
		param.put("elemIds", elemIds);
		param.put("movgestAnno", movgestAnno);
		
		Query query = createQuery(jpql.toString(), param);
		return (BigDecimal) query.getSingleResult();
	}
	
	@Override
	public BigDecimal sumMovgestImportoDaRiaccNonAnnullatiByBilElemIdsAndMovgestAnnoAndOperator(List<Integer> elemIds, Integer movgestAnno, CompareOperator jpqlCompareOperator) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		String attrCode = SiacTAttrEnum.FlagDaRiaccertamento.getCodice();
		
		jpql.append(" SELECT COALESCE(SUM(tmtd1.movgestTsDetImporto), 0) ");
		jpql.append(" FROM SiacTMovgestTsDet tmtd1 ");
		jpql.append(" WHERE tmtd1.movgestTsDetId IN ( ");
		jpql.append("     SELECT DISTINCT tmtd.movgestTsDetId ");
		jpql.append("     FROM SiacTMovgestTsDet tmtd, SiacTMovgestT tmt, SiacTMovgest tm, SiacRMovgestBilElem rmbe, SiacRMovgestTsStato rmts, SiacRMovgestTsAttr rmta ");
		jpql.append("     WHERE tmtd.siacTMovgestT = tmt ");
		jpql.append("     AND rmbe.siacTMovgest = tm ");
		jpql.append("     AND tmt.siacTMovgest = tm ");
		jpql.append("     AND rmts.siacTMovgestT = tmt ");
		jpql.append("     AND rmta.siacTMovgestT = tmt ");
		jpql.append("     AND tmtd.dataCancellazione IS NULL ");
		jpql.append("     AND tm.dataCancellazione IS NULL ");
		jpql.append("     AND tm.dataFineValidita IS NULL ");
		jpql.append("     AND rmbe.dataCancellazione IS NULL ");
		jpql.append("     AND rmbe.dataFineValidita IS NULL ");
		jpql.append("     AND tmt.dataCancellazione IS NULL ");
		jpql.append("     AND tmt.dataFineValidita IS NULL ");
		jpql.append("     AND rmts.dataCancellazione IS NULL ");
		jpql.append("     AND rmts.dataFineValidita IS NULL ");
		jpql.append("     AND rmta.dataCancellazione IS NULL ");
		jpql.append("     AND rmta.dataFineValidita IS NULL ");
		jpql.append("     AND tmt.siacDMovgestTsTipo.movgestTsTipoCode = 'T' ");
		jpql.append("     AND tm.movgestAnno ").append(jpqlCompareOperator.getJpql()).append(" :movgestAnno ");
		jpql.append("     AND rmbe.siacTBilElem.elemId IN (:elemIds) ");
		jpql.append("     AND rmts.siacDMovgestStato.movgestStatoCode <> 'A' ");
		jpql.append("     AND tmtd.siacDMovgestTsDetTipo.movgestTsDetTipoCode = 'A' ");
		jpql.append("     AND rmta.siacTAttr.attrCode = :attrCode");
		jpql.append("     AND rmta.boolean_ = 'S' ");
		jpql.append(" ) ");
		
		param.put("elemIds", elemIds);
		param.put("movgestAnno", movgestAnno);
		param.put("attrCode", attrCode);
		
		Query query = createQuery(jpql.toString(), param);
		return (BigDecimal) query.getSingleResult();
	}
	
	@Override
	public BigDecimal sumMovgestImportoDaEserciziPrecNonAnnullatiByBilElemIdsAndMovgestAnnoAndOperator(List<Integer> elemIds, Integer movgestAnno, CompareOperator jpqlCompareOperator,String bilAnno) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		String attrCode = SiacTAttrEnum.FlagDaRiaccertamento.getCodice();
		
		jpql.append(" SELECT COALESCE(SUM(tmtd1.movgestTsDetImporto), 0) ");
		jpql.append(" FROM SiacTMovgestTsDet tmtd1 ");
		jpql.append(" WHERE tmtd1.movgestTsDetId IN ( ");
		jpql.append("     SELECT DISTINCT tmtd.movgestTsDetId ");
		jpql.append("     FROM SiacTMovgestTsDet tmtd, SiacTMovgestT tmt, SiacTMovgest tm, SiacRMovgestBilElem rmbe, SiacRMovgestTsStato rmts, SiacRMovgestTsAttoAmm rmtam, SiacTAttoAmm tam");
		jpql.append("     WHERE tmtd.siacTMovgestT = tmt ");
		jpql.append("     AND rmbe.siacTMovgest = tm ");
		jpql.append("     AND tmt.siacTMovgest = tm ");
		jpql.append("     AND rmts.siacTMovgestT = tmt ");
		jpql.append("     AND rmtam.siacTMovgestT = tmt ");
		jpql.append("     AND rmtam.siacTAttoAmm = tam ");		
		jpql.append("     AND tmtd.dataCancellazione IS NULL ");
		jpql.append("     AND tm.dataCancellazione IS NULL ");
		jpql.append("     AND tm.dataFineValidita IS NULL ");
		jpql.append("     AND rmbe.dataCancellazione IS NULL ");
		jpql.append("     AND rmbe.dataFineValidita IS NULL ");
		jpql.append("     AND tmt.dataCancellazione IS NULL ");
		jpql.append("     AND tmt.dataFineValidita IS NULL ");
		jpql.append("     AND rmts.dataCancellazione IS NULL ");
		jpql.append("     AND rmts.dataFineValidita IS NULL ");
		jpql.append("     AND rmtam.dataCancellazione IS NULL ");
		jpql.append("     AND rmtam.dataFineValidita IS NULL ");
		jpql.append("     AND tmt.siacDMovgestTsTipo.movgestTsTipoCode = 'T' ");
		jpql.append("     AND tm.movgestAnno ").append(jpqlCompareOperator.getJpql()).append(" :movgestAnno ");
		jpql.append("     AND rmbe.siacTBilElem.elemId IN (:elemIds) ");
		jpql.append("     AND rmts.siacDMovgestStato.movgestStatoCode <> 'A' ");
		jpql.append("     AND tmtd.siacDMovgestTsDetTipo.movgestTsDetTipoCode = 'A' ");
		jpql.append("     AND tam.attoammAnno < :bilAnno ");
// SIAC-5173
		jpql.append("     AND NOT EXISTS ( ");
		jpql.append("         SELECT 1 FROM SiacRMovgestTsAttr rmta "); 
		jpql.append("     	  WHERE rmta.siacTMovgestT = tmt ");
		jpql.append("         AND rmta.dataCancellazione IS NULL ");
		jpql.append("         AND rmta.dataFineValidita IS NULL ");
		jpql.append("         AND rmta.siacTAttr.attrCode = :attrCode");
		jpql.append("         AND rmta.boolean_ = 'S' ");				
		jpql.append("     ) ");
		jpql.append(" ) ");
		
		param.put("elemIds", elemIds);
		param.put("movgestAnno", movgestAnno);
		param.put("bilAnno", bilAnno);
		param.put("attrCode", attrCode);
		
		Query query = createQuery(jpql.toString(), param);
		return (BigDecimal) query.getSingleResult();
	}
	
	

	
	
	
	
	// SIAC-6899 
	@Override
	public BigDecimal sumMovgestImportoFinanziatodaAvanzodaFPVNonAnnullatiByBilElemIdsAndMovgestAnnoAndOperator(List<Integer> elemIds, Integer movgestAnno, CompareOperator jpqlCompareOperator,List<String>  avavincoloTipoCode) {
		StringBuilder jpql = new StringBuilder();
		final String methodName = "sumMovgestImportoFinanziatodaAvanzoNonAnnullatiByBilElemIdsAndMovgestAnnoAndOperator";
		BigDecimal result = BigDecimal.ZERO;
		Map<String, Object> param = new HashMap<String, Object>();

		String attrCode = SiacTAttrEnum.FlagDaRiaccertamento.getCodice();

		String sqlQuery = " select COALESCE( sum(det.movgest_ts_det_importo)		, 0) 	                             "+
				" from siac_t_movgest mov,siac_d_movgest_tipo tipo,                                                      "+
				"      siac_r_movgest_bil_elem re,                                    									  "+
				"      siac_t_movgest_ts ts,siac_d_movgest_ts_tipo tipots,                                               "+
				"      siac_r_movgest_ts_stato rs,siac_d_movgest_stato stato,                                            "+
				"      siac_t_movgest_ts_det det,siac_d_movgest_ts_det_tipo tipod,                            		     "+
				"      siac_r_movgest_ts r, siac_t_avanzovincolo avav,siac_d_avanzovincolo_tipo avtipo,                  "+
				"      siac_t_bil bil,siac_t_periodo per                                                                 "+
				" where tipo.ente_proprietario_id=2                                                                      "+
				" and   tipo.movgest_tipo_code='I'                                                                       "+
				" and   mov.movgest_tipo_id=tipo.movgest_tipo_id                                                         "+
				" and   cast(mov.movgest_anno as integer)= :movgestAnno                                                  "+
				" and   re.movgest_id=mov.movgest_id                                                                     "+
				" and   re.elem_id  IN (:elemIds)                                                                        "+
				" and   ts.movgest_id=mov.movgest_id                                                                     "+
				" and   tipots.movgest_ts_tipo_id=ts.movgest_ts_tipo_id                                                  "+
				" and   tipots.movgest_ts_tipo_code='T'                                                                  "+
				" and   rs.movgest_ts_id=ts.movgest_ts_id                                                                "+
				" and   stato.movgest_stato_id=rs.movgest_stato_id                                                       "+
				" and   stato.movgest_stato_code!='A'                                                                    "+
				" and   det.movgest_ts_id=ts.movgest_ts_id                                                               "+
				" and   tipod.movgest_ts_det_tipo_id=det.movgest_ts_det_tipo_id                                          "+
				" and   tipod.movgest_ts_det_tipo_code='A'                                                               "+
				" and   r.movgest_ts_b_id=ts.movgest_ts_id                                                               "+
				" and   avav.avav_id=r.avav_id                                                                           "+
				" and  cast( extract (year from avav.validita_inizio)as integer) = cast(per.anno as integer)             "+
				" and   bil.periodo_id=per.periodo_id                                                                    "+
				" and   bil.bil_id=mov.bil_id                                                                            "+
				" and   avtipo.avav_tipo_id=avav.avav_tipo_id                                                            "+
				" and   avtipo.avav_tipo_code IN (:avavincoloTipoCode)                                                   "+
				" and   re.data_cancellazione is null                                                                    "+
				" and   re.validita_fine is null                                                                         "+
				" and   rs.data_cancellazione is null                                                                    "+
				" and   rs.validita_fine is null                                                                         "+
				" and   mov.data_cancellazione is null                                                                   "+
				" and   mov.validita_fine is null                                                                        "+
				" and   ts.data_cancellazione is null                                                                    "+
				" and   Ts.validita_fine is null                                                                         "+
				" and   det.data_cancellazione is null                                                                   "+
				" and   det.validita_fine is null                                                                        "+
				" and   r.data_cancellazione is null                                                                     "+
				" and   r.validita_fine is null                                                                          "+
				" and   avav.data_cancellazione is null                                                                  "+
				" and   date_trunc('DAY',coalesce(avav.validita_fine,cast ( (per.anno||'-12-31')as timestamp)))          "+
				"  <= date_trunc('DAY',cast ((per.anno||'-12-31')as timestamp))  										 ";


		Query query = entityManager.createNativeQuery(sqlQuery);
		query.setParameter("elemIds", elemIds);
		query.setParameter("movgestAnno", movgestAnno);
		query.setParameter("avavincoloTipoCode", avavincoloTipoCode);


		try{
			result = (BigDecimal) query.getSingleResult();
		}catch (Throwable t){
			log.error("Errore","sumMovgestImportoFinanziatodaAvanzodaFPVNonAnnullatiByBilElemIdsAndMovgestAnnoAndOperator",t);
			result = BigDecimal.ZERO;
		}

		log.debug(methodName, "Returning result: "+ result + " for elemIds: "+ elemIds);
		return result;
	}
	
	
	
	
	@Override
	public BigDecimal sumMovgestImportoDaPrenotazioneNonAnnullatiByBilElemIdsAndMovgestAnnoAndOperator(List<Integer> elemIds, Integer movgestAnno, CompareOperator jpqlCompareOperator) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		String attrCode = SiacTAttrEnum.FlagPrenotazione.getCodice();
		
		jpql.append(" SELECT COALESCE(SUM(tmtd1.movgestTsDetImporto), 0) ");
		jpql.append(" FROM SiacTMovgestTsDet tmtd1 ");
		jpql.append(" WHERE tmtd1.movgestTsDetId IN ( ");
		jpql.append("     SELECT DISTINCT tmtd.movgestTsDetId ");
		jpql.append("     FROM SiacTMovgestTsDet tmtd, SiacTMovgestT tmt, SiacTMovgest tm, SiacRMovgestBilElem rmbe, SiacRMovgestTsStato rmts, SiacRMovgestTsAttr rmta ");
		jpql.append("     WHERE tmtd.siacTMovgestT = tmt ");
		jpql.append("     AND rmbe.siacTMovgest = tm ");
		jpql.append("     AND tmt.siacTMovgest = tm ");
		jpql.append("     AND rmts.siacTMovgestT = tmt ");
		jpql.append("     AND rmta.siacTMovgestT = tmt ");
		jpql.append("     AND tmtd.dataCancellazione IS NULL ");
		jpql.append("     AND tm.dataCancellazione IS NULL ");
		jpql.append("     AND tm.dataFineValidita IS NULL ");
		jpql.append("     AND rmbe.dataCancellazione IS NULL ");
		jpql.append("     AND rmbe.dataFineValidita IS NULL ");
		jpql.append("     AND tmt.dataCancellazione IS NULL ");
		jpql.append("     AND tmt.dataFineValidita IS NULL ");
		jpql.append("     AND rmts.dataCancellazione IS NULL ");
		jpql.append("     AND rmts.dataFineValidita IS NULL ");
		jpql.append("     AND rmta.dataCancellazione IS NULL ");
		jpql.append("     AND rmta.dataFineValidita IS NULL ");
		jpql.append("     AND tmt.siacDMovgestTsTipo.movgestTsTipoCode = 'T' ");
		jpql.append("     AND tm.movgestAnno ").append(jpqlCompareOperator.getJpql()).append(" :movgestAnno ");
		jpql.append("     AND rmbe.siacTBilElem.elemId IN (:elemIds) ");
		jpql.append("     AND rmts.siacDMovgestStato.movgestStatoCode <> 'A' ");
		jpql.append("     AND tmtd.siacDMovgestTsDetTipo.movgestTsDetTipoCode = 'A' ");
		jpql.append("     AND rmta.siacTAttr.attrCode = :attrCode");
		jpql.append("     AND rmta.boolean_ = 'S' ");
		jpql.append(" ) ");
		
		param.put("elemIds", elemIds);
		param.put("movgestAnno", movgestAnno);
		param.put("attrCode", attrCode);
		
		Query query = createQuery(jpql.toString(), param);
		return (BigDecimal) query.getSingleResult();
	}

	@Override
	public BigDecimal sumLiquidazioniImportoNonAnnullateByBilElemIdsAndLiqAnnoAndOperator(List<Integer> elemIds, Integer movgestAnno, CompareOperator jpqlCompareOperator) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT COALESCE(SUM(tl1.liqImporto), 0) ");
		jpql.append(" FROM SiacTLiquidazione tl1 ");
		jpql.append(" WHERE tl1.liqId IN ( ");
		jpql.append("     SELECT DISTINCT tl.liqId ");
		jpql.append("     FROM SiacTLiquidazione tl, SiacRLiquidazioneMovgest rlm, SiacTMovgest tm, SiacRMovgestBilElem rmbe, SiacRLiquidazioneStato rls ");
		jpql.append("     WHERE rlm.siacTLiquidazione = tl ");
		jpql.append("     AND rlm.siacTMovgestT.siacTMovgest = tm ");
		jpql.append("     AND rmbe.siacTMovgest = tm ");
		jpql.append("     AND rls.siacTLiquidazione = tl ");
		jpql.append("     AND tm.movgestAnno ").append(jpqlCompareOperator.getJpql()).append(" :movgestAnno ");
		jpql.append("     AND rmbe.siacTBilElem.elemId IN (:elemIds) ");
		jpql.append("     AND rls.siacDLiquidazioneStato.liqStatoCode <> 'A' ");
		jpql.append("     AND tm.dataCancellazione IS NULL ");
		jpql.append("     AND tm.dataFineValidita IS NULL ");
		jpql.append("     AND rmbe.dataCancellazione IS NULL ");
		jpql.append("     AND rmbe.dataFineValidita IS NULL ");
		jpql.append("     AND rls.dataCancellazione IS NULL ");
		jpql.append("     AND rls.dataFineValidita IS NULL ");
		//SIAC-6752
		jpql.append("     AND rlm.dataCancellazione IS NULL ");
		jpql.append("     AND rlm.dataFineValidita IS NULL ");
		jpql.append("     AND tl.dataCancellazione IS NULL ");
		jpql.append(" ) ");
		
		param.put("elemIds", elemIds);
		param.put("movgestAnno", movgestAnno);
		
		Query query = createQuery(jpql.toString(), param);
		return (BigDecimal) query.getSingleResult();
	}
	
	@Override
	public BigDecimal sumLiquidazioniImportoDaPrenotazioneNonAnnullateByBilElemIdsAndLiqAnnoAndOperator(List<Integer> elemIds, Integer movgestAnno, CompareOperator jpqlCompareOperator) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		String attrCode = SiacTAttrEnum.FlagPrenotazione.getCodice();
		
		jpql.append(" SELECT COALESCE(SUM(tl1.liqImporto), 0) ");
		jpql.append(" FROM SiacTLiquidazione tl1 ");
		jpql.append(" WHERE tl1.liqId IN ( ");
		jpql.append("     SELECT DISTINCT tl.liqId ");
		jpql.append("     FROM SiacTLiquidazione tl, SiacRLiquidazioneMovgest rlm, SiacTMovgest tm, SiacRMovgestBilElem rmbe, SiacRLiquidazioneStato rls, SiacRMovgestTsAttr rmta ");
		jpql.append("     WHERE rlm.siacTLiquidazione = tl ");
		jpql.append("     AND rlm.siacTMovgestT.siacTMovgest = tm ");
		jpql.append("     AND rmbe.siacTMovgest = tm ");
		jpql.append("     AND rmta.siacTMovgestT = rlm.siacTMovgestT ");
		jpql.append("     AND rls.siacTLiquidazione = tl ");
		jpql.append("     AND tm.dataCancellazione IS NULL ");
		jpql.append("     AND tm.dataFineValidita IS NULL ");
		jpql.append("     AND rmbe.dataCancellazione IS NULL ");
		jpql.append("     AND rmbe.dataFineValidita IS NULL ");
		jpql.append("     AND rls.dataCancellazione IS NULL ");
		jpql.append("     AND rls.dataFineValidita IS NULL ");
		jpql.append("     AND rmta.dataCancellazione IS NULL ");
		jpql.append("     AND rmta.dataFineValidita IS NULL ");
		jpql.append("     AND tm.movgestAnno ").append(jpqlCompareOperator.getJpql()).append(" :movgestAnno ");
		jpql.append("     AND rmbe.siacTBilElem.elemId IN (:elemIds) ");
		jpql.append("     AND rls.siacDLiquidazioneStato.liqStatoCode <> 'A' ");
		jpql.append("     AND rmta.siacTAttr.attrCode = :attrCode");
		jpql.append("     AND rmta.boolean_ = 'S' "); 
		jpql.append(" ) ");
		
		param.put("elemIds", elemIds);
		param.put("movgestAnno", movgestAnno);
		param.put("attrCode", attrCode);
		
		Query query = createQuery(jpql.toString(), param);
		return (BigDecimal) query.getSingleResult();
	}
	
	
	@Override
	public Long countOrdinativiIncassoNonAnnullatiByBilElemIdsAndOrdAnnoAndOperator(List<Integer> elemIds, Integer movgestAnno, CompareOperator jpqlCompareOperator) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		jpql.append(" SELECT COALESCE(COUNT(tor1), 0) ");
		jpql.append(" FROM SiacTOrdinativo tor1 ");
		jpql.append(" WHERE tor1.ordId IN ( ");
		jpql.append("     SELECT DISTINCT tor.ordId ");
		jpql.append("     FROM SiacTOrdinativo tor, SiacROrdinativoBilElem robe, SiacROrdinativoStato ros, SiacROrdinativoTsMovgestT rotsmt ");
		jpql.append("     WHERE tor = rotsmt.siacTOrdinativoT.siacTOrdinativo ");
		jpql.append("     AND ros.siacTOrdinativo = tor ");
		jpql.append("     AND robe.siacTOrdinativo = tor ");
		
		jpql.append("     AND tor.dataCancellazione IS NULL ");
		jpql.append("     AND tor.dataFineValidita IS NULL ");
		jpql.append("     AND robe.dataCancellazione IS NULL ");
		jpql.append("     AND robe.dataFineValidita IS NULL ");
		jpql.append("     AND ros.dataCancellazione IS NULL ");
		jpql.append("     AND ros.dataFineValidita IS NULL ");
		jpql.append("     AND rotsmt.dataCancellazione IS NULL ");
		jpql.append("     AND rotsmt.dataFineValidita IS NULL ");
		
		jpql.append("     AND rotsmt.siacTMovgestT.siacTMovgest.movgestAnno ").append(jpqlCompareOperator.getJpql()).append(" :movgestAnno ");
		jpql.append("     AND robe.siacTBilElem.elemId IN (:elemIds) ");
		jpql.append("     AND ros.siacDOrdinativoStato.ordinativoStatoCode <> 'A' ");
		jpql.append(" ) ");
		
		param.put("elemIds", elemIds);
		param.put("movgestAnno", movgestAnno);
		
		Query query = createQuery(jpql.toString(), param);
		return (Long) query.getSingleResult();
	}

	@Override
	public BigDecimal sumOrdinativoIncassoImportoNonAnnullatiByBilElemIdsAndOrdAnnoAndOperator(List<Integer> elemIds, Integer movgestAnno, CompareOperator jpqlCompareOperator) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT COALESCE(SUM(totd1.ordTsDetImporto), 0) ");
		jpql.append(" FROM SiacTOrdinativoTsDet totd1 ");
		jpql.append(" WHERE totd1.ordTsDetId IN ( ");
		jpql.append("     SELECT DISTINCT totd.ordTsDetId ");
		jpql.append("     FROM SiacTOrdinativoTsDet totd, SiacTOrdinativo tor, SiacROrdinativoBilElem robe, SiacROrdinativoStato ros, SiacROrdinativoTsMovgestT rotsmt ");
		jpql.append("     WHERE totd.siacTOrdinativoT.siacTOrdinativo = tor ");
		jpql.append("     AND totd.siacTOrdinativoT = rotsmt.siacTOrdinativoT ");
		jpql.append("     AND ros.siacTOrdinativo = tor ");
		jpql.append("     AND robe.siacTOrdinativo = tor ");
		
		jpql.append("     AND totd.dataCancellazione IS NULL ");
		jpql.append("     AND totd.dataFineValidita IS NULL ");
		jpql.append("     AND tor.dataCancellazione IS NULL ");
		jpql.append("     AND tor.dataFineValidita IS NULL ");
		jpql.append("     AND robe.dataCancellazione IS NULL ");
		jpql.append("     AND robe.dataFineValidita IS NULL ");
		jpql.append("     AND ros.dataCancellazione IS NULL ");
		jpql.append("     AND ros.dataFineValidita IS NULL ");
		jpql.append("     AND rotsmt.dataCancellazione IS NULL ");
		jpql.append("     AND rotsmt.dataFineValidita IS NULL ");
		
//		jpql.append("     AND totd.siacTOrdinativoT.siacDMovgestTsTipo.movgestTsTipoCode = 'T' ");
		jpql.append("     AND rotsmt.siacTMovgestT.siacTMovgest.movgestAnno ").append(jpqlCompareOperator.getJpql()).append(" :movgestAnno ");
		jpql.append("     AND robe.siacTBilElem.elemId IN (:elemIds) ");
		jpql.append("     AND ros.siacDOrdinativoStato.ordinativoStatoCode <> 'A' ");
		jpql.append("     AND totd.siacDOrdinativoTsDetTipo.ordTsDetTipoCode = 'A' ");
		jpql.append(" ) ");
		
		param.put("elemIds", elemIds);
		param.put("movgestAnno", movgestAnno);
		
		Query query = createQuery(jpql.toString(), param);
		return (BigDecimal) query.getSingleResult();
	}
	
	@Override
	public Long countOrdinativiPagamentoNonAnnullatiByBilElemIdsAndOrdAnnoAndOperator(List<Integer> elemIds, Integer movgestAnno, CompareOperator jpqlCompareOperator) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		jpql.append(" SELECT COALESCE(COUNT(tor1), 0) ");
		jpql.append(" FROM SiacTOrdinativo tor1 ");
		jpql.append(" WHERE tor1.ordId IN ( ");
		jpql.append("     SELECT DISTINCT tor.ordId ");
		jpql.append("     FROM SiacTOrdinativo tor, SiacTOrdinativoT tot, SiacROrdinativoBilElem robe, SiacROrdinativoStato ros, SiacRLiquidazioneMovgest rlm, SiacRLiquidazioneOrd rlo ");
		jpql.append("     WHERE robe.siacTOrdinativo = tor");
		jpql.append("     AND ros.siacTOrdinativo = tor ");
		jpql.append("     AND tor = tot.siacTOrdinativo ");
		jpql.append("     AND rlo.siacTOrdinativoT = tot ");
		jpql.append("     AND rlm.siacTLiquidazione = rlo.siacTLiquidazione ");
		
		jpql.append("     AND tor.dataCancellazione IS NULL ");
		jpql.append("     AND tor.dataFineValidita IS NULL ");
		jpql.append("     AND robe.dataCancellazione IS NULL ");
		jpql.append("     AND robe.dataFineValidita IS NULL ");
		jpql.append("     AND ros.dataCancellazione IS NULL ");
		jpql.append("     AND ros.dataFineValidita IS NULL ");
		jpql.append("     AND rlm.dataCancellazione IS NULL ");
		jpql.append("     AND rlm.dataFineValidita IS NULL ");
		jpql.append("     AND rlo.dataCancellazione IS NULL ");
		jpql.append("     AND rlo.dataFineValidita IS NULL ");
		
		jpql.append("     AND rlm.siacTMovgestT.siacTMovgest.movgestAnno ").append(jpqlCompareOperator.getJpql()).append(" :movgestAnno ");
		jpql.append("     AND robe.siacTBilElem.elemId IN (:elemIds) ");
		jpql.append("     AND ros.siacDOrdinativoStato.ordinativoStatoCode <> 'A' ");
		jpql.append(" ) ");
		
		param.put("elemIds", elemIds);
		param.put("movgestAnno", movgestAnno);
		
		Query query = createQuery(jpql.toString(), param);
		return (Long) query.getSingleResult();
	}
	
	@Override
	public BigDecimal sumOrdinativoPagamentoImportoNonAnnullatiByBilElemIdsAndOrdAnnoAndOperator(List<Integer> elemIds, Integer movgestAnno, CompareOperator jpqlCompareOperator) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT COALESCE(SUM(totd1.ordTsDetImporto), 0) ");
		jpql.append(" FROM SiacTOrdinativoTsDet totd1 ");
		jpql.append(" WHERE totd1.ordTsDetId IN ( ");
		jpql.append("     SELECT DISTINCT totd.ordTsDetId ");
		jpql.append("     FROM SiacTOrdinativoTsDet totd, SiacTOrdinativo tor, SiacROrdinativoBilElem robe, SiacROrdinativoStato ros, SiacRLiquidazioneMovgest rlm, SiacRLiquidazioneOrd rlo ");
		jpql.append("     WHERE totd.siacTOrdinativoT.siacTOrdinativo = tor ");
		jpql.append("     AND rlo.siacTOrdinativoT = totd.siacTOrdinativoT ");
		jpql.append("     AND rlm.siacTLiquidazione = rlo.siacTLiquidazione  ");
		jpql.append("     AND ros.siacTOrdinativo = tor ");
		jpql.append("     AND robe.siacTOrdinativo = tor");
		
		jpql.append("     AND totd.dataCancellazione IS NULL ");
		jpql.append("     AND totd.dataFineValidita IS NULL ");
		jpql.append("     AND tor.dataCancellazione IS NULL ");
		jpql.append("     AND tor.dataFineValidita IS NULL ");
		jpql.append("     AND robe.dataCancellazione IS NULL ");
		jpql.append("     AND robe.dataFineValidita IS NULL ");
		jpql.append("     AND ros.dataCancellazione IS NULL ");
		jpql.append("     AND ros.dataFineValidita IS NULL ");
		jpql.append("     AND rlm.dataCancellazione IS NULL ");
		jpql.append("     AND rlm.dataFineValidita IS NULL ");
		jpql.append("     AND rlo.dataCancellazione IS NULL ");
		jpql.append("     AND rlo.dataFineValidita IS NULL ");
		
//		jpql.append("     AND totd.siacTOrdinativoT.siacDMovgestTsTipo.movgestTsTipoCode = 'T' ");
		jpql.append("     AND rlm.siacTMovgestT.siacTMovgest.movgestAnno ").append(jpqlCompareOperator.getJpql()).append(" :movgestAnno ");
		jpql.append("     AND robe.siacTBilElem.elemId IN (:elemIds) ");
		jpql.append("     AND ros.siacDOrdinativoStato.ordinativoStatoCode <> 'A' ");
		jpql.append("     AND totd.siacDOrdinativoTsDetTipo.ordTsDetTipoCode = 'A' ");
		jpql.append(" ) ");
		
		param.put("elemIds", elemIds);
		param.put("movgestAnno", movgestAnno);
		
		Query query = createQuery(jpql.toString(), param);
		return (BigDecimal) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findCapitoliBySubdocIds(List<Integer> subdocIds) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();

		jpql.append(" SELECT rmbe.siacTBilElem.elemId, ");
		jpql.append("     CAST(rmbe.siacTBilElem.siacTBil.siacTPeriodo.anno AS integer), ");
		jpql.append("     CAST(rmbe.siacTBilElem.elemCode AS integer), ");
		jpql.append("     CAST(rmbe.siacTBilElem.elemCode2 AS integer), ");
		jpql.append("     CAST(rmbe.siacTBilElem.elemCode3 AS integer), ");
		jpql.append("     rsmt.siacTSubdoc.subdocId, ");
		jpql.append("     rsmt.siacTSubdoc.siacTDoc.docAnno, ");
		jpql.append("     rsmt.siacTSubdoc.siacTDoc.docNumero, ");
		jpql.append("     rsmt.siacTSubdoc.subdocNumero, ");
		jpql.append("     rsmt.siacTSubdoc.subdocImporto, ");
		jpql.append("     rsmt.siacTSubdoc.subdocImportoDaDedurre ");
		jpql.append(" FROM SiacRMovgestBilElem rmbe, SiacRSubdocMovgestT rsmt ");
		jpql.append(" WHERE rmbe.dataCancellazione IS NULL ");
		jpql.append(" AND rsmt.siacTMovgestT.siacTMovgest = rmbe.siacTMovgest ");
		jpql.append(" AND rsmt.dataCancellazione IS NULL ");
		jpql.append(" AND rsmt.siacTSubdoc.subdocId IN (:subdocIds) ");
		jpql.append(" ORDER BY rmbe.siacTBilElem.elemId ");
		
		param.put("subdocIds", subdocIds);
		Query query = createQuery(jpql.toString(), param);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findCapitoliByElenco(List<Integer> eldocIds) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();

		jpql.append(" SELECT rmbe.siacTBilElem.elemId, ");
		jpql.append("     CAST(rmbe.siacTBilElem.siacTBil.siacTPeriodo.anno AS integer), ");
		jpql.append("     CAST(rmbe.siacTBilElem.elemCode AS integer), ");
		jpql.append("     CAST(rmbe.siacTBilElem.elemCode2 AS integer), ");
		jpql.append("     CAST(rmbe.siacTBilElem.elemCode3 AS integer), ");
		jpql.append("     rsmt.siacTSubdoc.subdocId, ");
		jpql.append("     rsmt.siacTSubdoc.siacTDoc.docAnno, ");
		jpql.append("     rsmt.siacTSubdoc.siacTDoc.docNumero, ");
		jpql.append("     rsmt.siacTSubdoc.subdocNumero, ");
		jpql.append("     rsmt.siacTSubdoc.subdocImporto, ");
		jpql.append("     rsmt.siacTSubdoc.subdocImportoDaDedurre ");
		jpql.append(" FROM SiacRMovgestBilElem rmbe, SiacRSubdocMovgestT rsmt ");
		jpql.append(" WHERE rmbe.dataCancellazione IS NULL ");
		jpql.append(" AND rsmt.siacTMovgestT.siacTMovgest = rmbe.siacTMovgest ");
		jpql.append(" AND rsmt.dataCancellazione IS NULL ");
		jpql.append(" AND EXISTS (");
		jpql.append("     FROM SiacRElencoDocSubdoc reds  ");
		jpql.append("     WHERE reds.siacTElencoDoc.eldocId IN (:eldocIds) ");
		jpql.append("     AND rsmt.siacTSubdoc = reds.siacTSubdoc ");
		jpql.append("     AND reds.dataCancellazione IS NULL ");
		jpql.append("     AND reds.siacTSubdoc.dataCancellazione IS NULL ");
		jpql.append(" ) ");
//		jpql.append(" AND rsmt.siacTSubdoc.subdocId IN ( ");
//		jpql.append("     SELECT reds.siacTSubdoc.subdocId ");
//		jpql.append("     FROM SiacRElencoDocSubdoc reds  ");
//		jpql.append("     WHERE reds.siacTElencoDoc.eldocId IN (:eldocIds) ");
//		jpql.append("     AND reds.dataCancellazione IS NULL ");
//		jpql.append("     AND reds.siacTSubdoc.dataCancellazione is null ");
//		jpql.append( " ) ");
		jpql.append(" ORDER BY rmbe.siacTBilElem.elemId ");
		
		param.put("eldocIds", eldocIds);
		Query query = createQuery(jpql.toString(), param);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> ricercaIdCapitoli(Integer enteProprietarioId, String annoBilancio, String elemTipoCode, String numeroCapitolo, String numeroArticolo, String numeroUEB) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		
		jpql.append(" SELECT tbe.elemId ");
		jpql.append(" FROM SiacTBilElem tbe ");
		jpql.append(" WHERE tbe.dataCancellazione IS NULL ");
		jpql.append(" AND tbe.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append(" AND tbe.siacDBilElemTipo.elemTipoCode = :elemTipoCode ");
		params.put("enteProprietarioId", enteProprietarioId);
		params.put("elemTipoCode", elemTipoCode);
		
		if(StringUtils.isNotBlank(annoBilancio)) {
			jpql.append(" AND tbe.siacTBil.siacTPeriodo.anno = :annoBilancio ");
			params.put("annoBilancio", annoBilancio);
		}
		if(StringUtils.isNotBlank(numeroCapitolo)) {
			jpql.append(" AND tbe.elemCode = :numeroCapitolo ");
			params.put("numeroCapitolo", numeroCapitolo);
		}
		if(StringUtils.isNotBlank(numeroArticolo)) {
			jpql.append(" AND tbe.elemCode2 = :numeroArticolo ");
			params.put("numeroArticolo", numeroArticolo);
		}
		if(StringUtils.isNotBlank(numeroUEB)) {
			jpql.append(" AND tbe.elemCode3 = :numeroUEB ");
			params.put("numeroUEB", numeroUEB);
		}
		Query query = createQuery(jpql.toString(), params);
		return query.getResultList();
	}
	
	
	
	/*
	 ############## Query di verifica del legame tra Ordinativo Pagamento, Liquidazion, Impegno, Capitolo
	 
	 select 
	'Ordinativo '  ||o.ord_anno || '/' || o.ord_numero || ': ' || otd.ord_ts_det_importo || ' '||dotdt.ord_ts_det_tipo_code,
	'Liquidazione '||l.liq_anno || '/' || l.liq_numero || ': ' || l.liq_importo,
	'Impegno    '  ||m.movgest_anno || '/' || m.movgest_numero || ' - ' || mt.movgest_ts_code,
	'Capitolo Mov '||be_mov.elem_code || '/' || be_mov.elem_code2 || ' - ' || be_mov.elem_code3,
	'Capitolo Ord '||be_ord.elem_code || '/' || be_ord.elem_code2 || '/' || be_ord.elem_code3
	from 
	siac_t_ordinativo o 
	join siac_t_ordinativo_ts ot on o.ord_id = ot.ord_id 
	join siac_t_ordinativo_ts_det otd on (otd.ord_ts_id = ot.ord_ts_id and otd.data_cancellazione is null and otd.validita_fine is null)
	join siac_d_ordinativo_ts_det_tipo dotdt on dotdt.ord_ts_det_tipo_id = otd.ord_ts_det_tipo_id
	join siac_r_ordinativo_stato ros on (ros.ord_id = o.ord_id and ros.data_cancellazione is null and ros.validita_fine is null) 
	join siac_r_liquidazione_ord rlo on (rlo.sord_id = ot.ord_ts_id and rlo.data_cancellazione is null and rlo.validita_fine is null)
	join siac_t_liquidazione l on l.liq_id = rlo.liq_id 
	--join siac_r_ordinativo_ts_movgest_ts rotmt on (rotmt.ord_ts_id = ot.ord_ts_id and rotmt.data_cancellazione is null and rotmt.validita_fine is null)
	join siac_r_liquidazione_movgest rlm on rlm.liq_id = l.liq_id
	join siac_t_movgest_ts mt on mt.movgest_ts_id = rlm.movgest_ts_id 
	join siac_t_movgest m on mt.movgest_id = m.movgest_id
	join siac_r_ordinativo_bil_elem obe on obe.ord_id = o.ord_id 
	join siac_t_bil_elem be_ord on obe.elem_id = be_ord.elem_id
	join siac_r_movgest_bil_elem rmbe on rmbe.movgest_id = m.movgest_id
	join siac_t_bil_elem be_mov on rmbe.elem_id = be_mov.elem_id
	where o.ente_proprietario_id = 5
	--and be_mov.elem_id = be_ord.elem_id
	and be_ord.elem_id = 121103
	and dotdt.ord_ts_det_tipo_code = 'A' --importo attuale ordinativo
	order by be_ord.elem_id
	
	
	 */
	

}
