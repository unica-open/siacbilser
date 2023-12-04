/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.SiacTMovgestImpegnoMapper;
import it.csi.siac.siacbilser.integration.dao.BackofficeModificaCigDao;
import it.csi.siac.siacbilser.integration.entity.SiacDSiopeAssenzaMotivazione;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ImpegnoSoggettoConverter;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.TipologiaAttributo;
import it.csi.siac.siacfin2ser.model.ImpegnoModelDetail;
import it.csi.siac.siacfin2ser.model.SubImpegnoModelDetail;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfinser.integration.dad.datacontainer.DisponibilitaMovimentoGestioneContainer;
import it.csi.siac.siacfinser.integration.helper.DisponibilitaLiquidareImpegnoHelper;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;
import it.csi.siac.siacfinser.model.siopeplus.SiopeTipoDebito;

/**
 * The Class ImpegnoBilDad.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class ImpegnoBilDad extends MovimentoGestioneBilDad<Impegno>  {
	
	@Autowired
	private ImpegnoSoggettoConverter impegnoSoggettoConverter;
	private DisponibilitaLiquidareImpegnoHelper disponibilitaLiquidareImpegnoHelper;
	
	@Autowired
	protected BackofficeModificaCigDao backofficeModificaCigDao;
	
	@Autowired SiacTMovgestImpegnoMapper siacTMovgestImpegnoMapper;
	
	public Impegno findMiniminalImpegnoDataByUid(Integer uid) {
		SiacTMovgestT siacTMovgestT = findTestataByUidMovimento(uid);
		if(siacTMovgestT == null) {
			return null;
		}
		
		return mapNotNull(siacTMovgestT.getSiacTMovgest(), Impegno.class, BilMapId.SiacTMovgest_Impegno);
	}
	
	public Impegno findImpegnoByUid(Integer uid, ImpegnoModelDetail... modelDetails) {
		SiacTMovgestT siacTMovgestT = findTestataByUidMovimento(uid);
		if(siacTMovgestT == null) {
			return null;
		}
		return mapNotNull(siacTMovgestT.getSiacTMovgest(), Impegno.class, BilMapId.SiacTMovgest_Impegno_ModelDetail, Converters.byModelDetails(modelDetails));
	}
	
	public SubImpegno findSubImpegnoByUid(Integer uid, SubImpegnoModelDetail... modelDetails) {
		SiacTMovgestT siacTMovgestT = siacTMovgestTRepository.findOne(uid);
		return mapNotNull(siacTMovgestT, SubImpegno.class, BilMapId.SiacTMovgestT_SubImpegno_ModelDetail, Converters.byModelDetails(modelDetails));
	}
	
	public void popolaInformazioniSoggetto(Impegno impegno){
		final String methodName = "popolaInformazioniSoggetto";
		
		SiacTMovgest siacTMovgest = movimentoGestioneDao.findById(impegno.getUid());
		impegnoSoggettoConverter.convertFrom(siacTMovgest, impegno);
		
		log.debug(methodName, impegno.getSoggetto()!=null?"Ottenuto Soggetto con codice: "+impegno.getSoggetto().getCodiceSoggetto() : "Non ho ottenuto nessun soggetto.");
	}
	

	public void popolaInformazioniCapitolo(Impegno impegno) {
		int uidCapitolo = siacTMovgestTRepository.findBilElemIdByMovgestId(impegno.getUid());
		CapitoloUscitaGestione capitolo = new CapitoloUscitaGestione();
		capitolo.setUid(uidCapitolo);
		impegno.setCapitoloUscitaGestione(capitolo);
	}
	
	public DisponibilitaMovimentoGestioneContainer ottieniDisponibilitaLiquidare(Impegno impegno, SubImpegno subImpegno, Integer annoBilancio) {
		Integer uid = null;
		// Per l'impegno devo ricavare l'uid della testata
		
		
		if(subImpegno != null && subImpegno.getUid() != 0) {
			uid = subImpegno.getUid();
		} else if(impegno != null && impegno.getUid() != 0) {
			SiacTMovgestT siacTMovgestT = findTestataByUidMovimento(impegno.getUid());
			if(siacTMovgestT == null) {
				return null;
			}
			uid = siacTMovgestT.getUid();
		}
		
		if(uid == null) {
			return null;
		}
		if(disponibilitaLiquidareImpegnoHelper == null) {
			disponibilitaLiquidareImpegnoHelper = new DisponibilitaLiquidareImpegnoHelper(appCtx);
			disponibilitaLiquidareImpegnoHelper.init();
		}
		return disponibilitaLiquidareImpegnoHelper.calcolaDisponibilitaALiquidare(uid, annoBilancio);
	}
	
	public DisponibilitaMovimentoGestioneContainer ottieniDisponibilitaLiquidareDaTestata(Integer uidTestata, Integer annoBilancio) {
		if(disponibilitaLiquidareImpegnoHelper == null) {
			disponibilitaLiquidareImpegnoHelper = new DisponibilitaLiquidareImpegnoHelper(appCtx);
			disponibilitaLiquidareImpegnoHelper.init();
		}
		return disponibilitaLiquidareImpegnoHelper.calcolaDisponibilitaALiquidare(uidTestata, annoBilancio);
	}
	
	public Impegno findImpegnoQuota(int uid) {
		final String methodName = "findImpegnoQuota";
		SiacTMovgest siacTMovgest = siacTMovgestTRepository.findSiacTMovgestBySubdoc(uid);
		if(siacTMovgest == null){
			log.debug(methodName, "nessun impegno trovato");
			return null;
		}
		Impegno impegno = new Impegno();
		impegno.setUid(siacTMovgest.getUid());
		impegno.setAnnoMovimento(siacTMovgest.getMovgestAnno());
		impegno.setNumeroBigDecimal(siacTMovgest.getMovgestNumero());
		log.debug(methodName, " impegno trovato: " + impegno.getUid());
		return impegno;
	}

	public SubImpegno findSubImpegnoQuota(int uid) {
		final String methodName = "findSubImpegnoQuota";
		SiacTMovgestT siacTMovgestTs = siacTMovgestTRepository.findSiacTMovgestTSSubimpegnoBySubdoc(uid);
		if(siacTMovgestTs == null){
			log.debug(methodName, "nessun subimpegno trovato");
			return null;
		}
		SubImpegno subimpegno = new SubImpegno();
		subimpegno.setUid(siacTMovgestTs.getUid());
		subimpegno.setAnnoMovimento(siacTMovgestTs.getSiacTMovgest().getMovgestAnno());
		subimpegno.setNumeroBigDecimal(siacTMovgestTs.getSiacTMovgest().getMovgestNumero());
		log.debug(methodName, " subimpegno trovato: " + subimpegno.getUid());
		return subimpegno;
	}
	
	public ElementoPianoDeiConti findPianoDeiContiAssociatoAMovimentoGestione(Subdocumento<?,?> subdoc) {
		final String methodName = "findPianoDeiContiAssociatoAMovimentoGestione";
		
		List<String> pianoDeiConti = new ArrayList<String>();
		pianoDeiConti.add(SiacDClassTipoEnum.PrimoLivelloPdc.getCodice());
		pianoDeiConti.add(SiacDClassTipoEnum.SecondoLivelloPdc.getCodice());
		pianoDeiConti.add(SiacDClassTipoEnum.TerzoLivelloPdc.getCodice());
		pianoDeiConti.add(SiacDClassTipoEnum.QuartoLivelloPdc.getCodice());
		pianoDeiConti.add(SiacDClassTipoEnum.QuintoLivelloPdc.getCodice());
		
		List<SiacTClass> siacTClassPdcs = siacTMovgestTRepository.findSiacTClassMovGestBySubdocIdECodiciTipo(subdoc.getUid(), pianoDeiConti);
		if(siacTClassPdcs == null || siacTClassPdcs.isEmpty()) {
			log.debug(methodName, "Nessun siacTClassPdc trovato.");
			return null;
		}
		
		int size = siacTClassPdcs.size();
		log.info(methodName, "Trovati: "+ size +". Expected: 1. Prendo sempre il primo.");
		if(size>1){
			log.error(methodName, "Trovati "+size+" piano dei conti per subdoc.uid:"+subdoc.getUid() +". Resituisco il primo!");
		}
		
		SiacTClass siacTClassPdc = siacTClassPdcs.get(0);
		
		ElementoPianoDeiConti pdc = new ElementoPianoDeiConti();
		pdc.setUid(siacTClassPdc.getUid());
		pdc.setCodice(siacTClassPdc.getClassifCode());
		pdc.setDescrizione(siacTClassPdc.getClassifDesc());
		
		log.debug(methodName, "siacTClassPdc trovato: " + siacTClassPdc.getUid());
		return pdc;
	}

	
	public Boolean getFlagAttivaGsa(int uidImpegno) {
		String methodName = "getFlagAttivaGsa";
		
		List<String> attrs = siacTMovgestTRepository.findBooleanAttrValues(uidImpegno, SiacTAttrEnum.FlagAttivaGsa.getCodice());
		boolean tmp = false;
		String attr = null;
		for(Iterator<String> it = attrs.iterator(); it.hasNext() && !tmp;) {
			attr = it.next();
			tmp = "S".equals(attr);
		}
		Boolean result = Boolean.valueOf(tmp);
		
		log.debug(methodName, "Returning: "+result + " (for uidImpegno: "+uidImpegno+" and 'attr' with value: "+attr+")");
		return result;
		
	}
	
	/**
	 * @param uidImpegno
	 * @return the CUP
	 */
	public String getCUP(int uidImpegno) {
		return getAttributoTesto(uidImpegno, TipologiaAttributo.CUP);
	}
	
	/**
	 * @param uidImpegno
	 * @return the CUP
	 */
	public String getCIG(int uidImpegno) {
		return getAttributoTesto(uidImpegno, TipologiaAttributo.CIG);
	}
	
	/**
	 * @param uidImpegno
	 * @param the tipologiaAttributo attributo to find
	 * @return the String il valore dell'attributo corrispondente alla Tipologia attributo
	 */
	public String getAttributoTesto(int uidImpegno, TipologiaAttributo tipologiaAttributo) {
		String methodName = "getAttributoTesto";
		
		SiacTAttrEnum siacTAttrEnum = SiacTAttrEnum.byTipologiaAttributo(tipologiaAttributo);
		
		if(!String.class.equals(siacTAttrEnum.getFieldType())){
			throw new IllegalArgumentException("Attributo "+tipologiaAttributo+" non di tipo testo!");
		}
		
		String result = siacTMovgestTRepository.findTestoAttrValueBySiacTMovgestId(uidImpegno, siacTAttrEnum.getCodice()); //TODO mettere in SiacTAttr
		log.debug(methodName, "Returning: "+result + " (for uidImpegno: "+uidImpegno+" and 'attr' "+siacTAttrEnum.getCodice()+". ");
		return result;
	}
	
	public void aggiungiCodiciClassificatoriAImpegnoEOSubImpegno(Impegno impegno, SubImpegno subimpegno) {
		
		List<String> ricorrenteSpesa = new ArrayList<String>();
		ricorrenteSpesa.add(SiacDClassTipoEnum.RicorrenteSpesa.getCodice());
		
		List<String> contoEconomico = new ArrayList<String>();
		contoEconomico.add(SiacDClassTipoEnum.PrimoLivelloContoEconomico.getCodice());
		contoEconomico.add(SiacDClassTipoEnum.SecondoLivelloContoEconomico.getCodice());
		contoEconomico.add(SiacDClassTipoEnum.TerzoLivelloContoEconomico.getCodice());
		contoEconomico.add(SiacDClassTipoEnum.QuartoLivelloContoEconomico.getCodice());
		contoEconomico.add(SiacDClassTipoEnum.QuintoLivelloContoEconomico.getCodice());
		
		List<String> pianoDeiConti = new ArrayList<String>();
		pianoDeiConti.add(SiacDClassTipoEnum.PrimoLivelloPdc.getCodice());
		pianoDeiConti.add(SiacDClassTipoEnum.SecondoLivelloPdc.getCodice());
		pianoDeiConti.add(SiacDClassTipoEnum.TerzoLivelloPdc.getCodice());
		pianoDeiConti.add(SiacDClassTipoEnum.QuartoLivelloPdc.getCodice());
		pianoDeiConti.add(SiacDClassTipoEnum.QuintoLivelloPdc.getCodice());
		
		List<String> transazioneEuropea = new ArrayList<String>();
		transazioneEuropea.add(SiacDClassTipoEnum.TransazioneUnioneEuropeaSpesa.getCodice());
		
		List<String> siope = new ArrayList<String>();
		siope.add(SiacDClassTipoEnum.PrimoLivelloSiopeSpesa.getCodice());
		siope.add(SiacDClassTipoEnum.SecondoLivelloSiopeSpesa.getCodice());
		siope.add(SiacDClassTipoEnum.TerzoLivelloSiopeSpesa.getCodice());
		
		List<String> cofog = new ArrayList<String>();
		cofog.add(SiacDClassTipoEnum.CofogGruppo.getCodice());
		cofog.add(SiacDClassTipoEnum.CofogClasse.getCodice());
		cofog.add(SiacDClassTipoEnum.CofogDivisione.getCodice());
		
		List<String> capSanitario = new ArrayList<String>();
		capSanitario.add(SiacDClassTipoEnum.PerimetroSanitarioSpesa.getCodice());
		
		List<String> prgPolReg = new ArrayList<String>();
		prgPolReg.add(SiacDClassTipoEnum.PoliticheRegionaliUnitarie.getCodice());
		
		
		if(impegno != null){
			impegno.setCodRicorrenteSpesa(siacTMovgestTRepository.findCodiceClassificatoreByUidImpegnoECodiceTipo(impegno.getUid(),ricorrenteSpesa));
			impegno.setCodContoEconomico(siacTMovgestTRepository.findCodiceClassificatoreByUidImpegnoECodiceTipo(impegno.getUid(), contoEconomico));
			impegno.setCodPdc(siacTMovgestTRepository.findCodiceClassificatoreByUidImpegnoECodiceTipo(impegno.getUid(), pianoDeiConti));
			impegno.setCodTransazioneEuropeaSpesa(siacTMovgestTRepository.findCodiceClassificatoreByUidImpegnoECodiceTipo(impegno.getUid(),transazioneEuropea));
			impegno.setCodSiope(siacTMovgestTRepository.findCodiceClassificatoreByUidImpegnoECodiceTipo(impegno.getUid(),siope));
			impegno.setCodCofog(siacTMovgestTRepository.findCodiceClassificatoreByUidImpegnoECodiceTipo(impegno.getUid(),cofog));
			impegno.setCodCapitoloSanitarioSpesa(siacTMovgestTRepository.findCodiceClassificatoreByUidImpegnoECodiceTipo(impegno.getUid(),capSanitario));
			impegno.setCodPrgPolReg(siacTMovgestTRepository.findCodiceClassificatoreByUidImpegnoECodiceTipo(impegno.getUid(),prgPolReg));
		} 
		if(subimpegno != null){
			subimpegno.setCodRicorrenteSpesa(siacTMovgestTRepository.findCodiceClassificatoreByUidSubImpegnoECodiceTipo(subimpegno.getUid(),ricorrenteSpesa));
			subimpegno.setCodContoEconomico(siacTMovgestTRepository.findCodiceClassificatoreByUidSubImpegnoECodiceTipo(subimpegno.getUid(), contoEconomico));
			subimpegno.setCodPdc(siacTMovgestTRepository.findCodiceClassificatoreByUidSubImpegnoECodiceTipo(subimpegno.getUid(), pianoDeiConti));
			subimpegno.setCodTransazioneEuropeaSpesa(siacTMovgestTRepository.findCodiceClassificatoreByUidSubImpegnoECodiceTipo(subimpegno.getUid(),transazioneEuropea));
			subimpegno.setCodSiope(siacTMovgestTRepository.findCodiceClassificatoreByUidSubImpegnoECodiceTipo(subimpegno.getUid(),siope));
			subimpegno.setCodCofog(siacTMovgestTRepository.findCodiceClassificatoreByUidSubImpegnoECodiceTipo(subimpegno.getUid(),cofog));
			subimpegno.setCodCapitoloSanitarioSpesa(siacTMovgestTRepository.findCodiceClassificatoreByUidSubImpegnoECodiceTipo(subimpegno.getUid(),capSanitario));
			subimpegno.setCodPrgPolReg(siacTMovgestTRepository.findCodiceClassificatoreByUidSubImpegnoECodiceTipo(subimpegno.getUid(),prgPolReg));
		}
		
	}

	public SiopeTipoDebito findSiopeTipoDebito(Impegno impegno, SubImpegno subImpegno) {
		SiacTMovgestT siacTMovgestT = null;
		if(subImpegno != null && subImpegno.getUid() != 0) {
			siacTMovgestT = siacTMovgestTRepository.findOne(subImpegno.getUid());
		} else  if(impegno != null && impegno.getUid() != 0) {
			siacTMovgestT = findTestataByUidMovimento(impegno.getUid());
		}
		return mapNotNull(siacTMovgestT != null ? siacTMovgestT.getSiacDSiopeTipoDebito() : null, SiopeTipoDebito.class, BilMapId.SiacDSiopeTipoDebito_SiopeTipoDebito);
	}


	// SIAC-6036
	/**
	 * @param uidImpegno
	 * @return the SiacDSiopeAssenzaMotivazione
	 */
	public SiopeAssenzaMotivazione getSiopeAssenzaMotivazione(int uidImpegno) {
		SiacDSiopeAssenzaMotivazione result = siacTMovgestTRepository.findSiopeAssenzaMotivazioneByMovgestId(uidImpegno);
		return mapNotNull(result, SiopeAssenzaMotivazione.class, BilMapId.SiacDSiopeAssenzaMotivazione_SiopeAssenzaMotivazione);
	}

	// Evolutiva BackofficeModificaCigRemedy
	/**
	 * Invoca la procedura per la modifica Documenti Quote
	 * */
    public Integer modificaCigDocumentiQuote(int uid, Integer uidTipoDebito, String cig, Integer uidMotivazioneAssenzaCig, String numeroRemedy) {
		return backofficeModificaCigDao.backofficeModificaCigMovgest(uid, uidTipoDebito, cig, uidMotivazioneAssenzaCig, numeroRemedy);
	}

    // Evolutiva BackofficeModificaCigRemedy
	/**
	 * Invoca la Procedura per la modifica Liquidazioni senza ordinativi
	 * */
    public Integer modificaCigLiquidazioniSenzaOrdinativiCollegati(int uid, Integer uidTipoDebito, String cig, Integer uidMotivazioneAssenzaCig, String numeroRemedy) {
		return backofficeModificaCigDao.backofficeModificaCigCollegati(uid, uidTipoDebito, cig, uidMotivazioneAssenzaCig, numeroRemedy);
	}
    
	public Impegno ricercaDettaglioImpegno(Impegno impegno, ImpegnoModelDetail... impegnoModelDetails) {
		return siacTMovgestImpegnoMapper.map(movimentoGestioneDao.findById(impegno.getUid()), mapperDecoratorHelper.getDecoratorsFromModelDetails(impegnoModelDetails));		
	}    
}
