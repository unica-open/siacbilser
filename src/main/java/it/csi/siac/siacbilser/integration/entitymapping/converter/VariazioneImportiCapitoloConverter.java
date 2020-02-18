/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dozer.DozerConverter;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.SiacTPeriodoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemDetTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRVariazioneStato;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDet;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDetVar;
import it.csi.siac.siacbilser.integration.entity.SiacTPeriodo;
import it.csi.siac.siacbilser.integration.entity.SiacTVariazione;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemDetTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTBilElemDetVarElemDetFlagEnum;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siaccommon.util.log.LogUtil;

/**
 * The Class VariazioneImportiCapitoloConverter.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)

public class VariazioneImportiCapitoloConverter extends DozerConverter<VariazioneImportoCapitolo, SiacTVariazione>  {
	
	/** The log util */
	private final LogUtil log = new LogUtil(getClass());
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;
	
	/** The siac t periodo repository. */
	@Autowired
	private SiacTPeriodoRepository siacTPeriodoRepository;
	
	/** The importi capitolo converter. */
	@Autowired
	private ImportiCapitoloUPConverter importiCapitoloConverter;

	/**
	 * Instantiates a new variazione importi capitolo converter.
	 */
	public VariazioneImportiCapitoloConverter() {
		super(VariazioneImportoCapitolo.class, SiacTVariazione.class);
	}

	@Override
	public VariazioneImportoCapitolo convertFrom(SiacTVariazione src, VariazioneImportoCapitolo dest) {
		final String methodName = "convertFrom";
		
		List<DettaglioVariazioneImportoCapitolo> res = new ArrayList<DettaglioVariazioneImportoCapitolo>();
		int annoVariazione = Integer.valueOf(src.getSiacTBil().getSiacTPeriodo().getAnno());
		
		for(SiacRVariazioneStato siacRVariazioneStato:src.getSiacRVariazioneStatos()){
			if(siacRVariazioneStato.getDataCancellazione()==null) {
				for(SiacTBilElemDetVar siacTBilElemDetVar: siacRVariazioneStato.getSiacTBilElemDetVars()){
					if(siacTBilElemDetVar.getDataCancellazione()==null) {
						log.debug(methodName, "siacTBilElemDetVar id: " + siacTBilElemDetVar.getElemDetVarId()
							+ " capitolo uid:" + (siacTBilElemDetVar.getSiacTBilElem() != null ? siacTBilElemDetVar.getSiacTBilElem().getUid() : "null")
							+ " anno: " + siacTBilElemDetVar.getSiacTPeriodo().getAnno()
							+ " importo: " + siacTBilElemDetVar.getElemDetImporto());
						
						DettaglioVariazioneImportoCapitolo dettVi = byCapitolo(res, siacTBilElemDetVar);
						int delta = Integer.valueOf(siacTBilElemDetVar.getSiacTPeriodo().getAnno()) - annoVariazione;
						
						//Imposto l'importo
						String fieldName = SiacDBilElemDetTipoEnum.byCodice(siacTBilElemDetVar.getSiacDBilElemDetTipo().getElemDetTipoCode()).getImportiCapitoloFieldName();
						if(delta != 0) {
							fieldName += delta;
						}
						BeanWrapper bwDettVi = PropertyAccessorFactory.forBeanPropertyAccess(dettVi);
						bwDettVi.setPropertyValue(fieldName, siacTBilElemDetVar.getElemDetImporto());
						dettVi = (DettaglioVariazioneImportoCapitolo) bwDettVi.getWrappedInstance();
					}
				}
				
			}
		}
		dest.setListaDettaglioVariazioneImporto(res);
		return dest;
	}

	
	
	
	/**
	 * Map to capitolo.
	 *
	 * @param siacTBilElem the siac t bil elem
	 * @param annoCompetenza 
	 * @return the capitolo
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Capitolo mapToCapitolo(SiacTBilElem siacTBilElem, Integer annoCompetenza) {
		if(siacTBilElem==null){
			return null;
		}
		
		Capitolo capitolo = SiacDBilElemTipoEnum.byCodice(siacTBilElem.getSiacDBilElemTipo().getElemTipoCode()).getCapitoloInstance();
		capitolo.setUid(siacTBilElem.getUid());
		capitolo.setAnnoCapitolo(Integer.parseInt(siacTBilElem.getSiacTBil().getSiacTPeriodo().getAnno()));
		capitolo.setNumeroCapitolo(Integer.parseInt(siacTBilElem.getElemCode()));
		capitolo.setNumeroArticolo(Integer.parseInt(siacTBilElem.getElemCode2()));
		capitolo.setNumeroUEB(Integer.parseInt(siacTBilElem.getElemCode3()));
		capitolo.setDescrizione(siacTBilElem.getElemDesc());
		capitolo.setDescrizioneArticolo(siacTBilElem.getElemDesc2());
		
		Map<String, List<SiacTBilElemDet>> siacTBileElemDetByAnno = new HashMap<String, List<SiacTBilElemDet>>();
		for(SiacTBilElemDet elemDet: siacTBilElem.getSiacTBilElemDets()){
			
			String anno = elemDet.getSiacTPeriodo().getAnno();
			List<SiacTBilElemDet> elemDetsByAnno = siacTBileElemDetByAnno.get(anno);
			if(elemDetsByAnno == null) {
				elemDetsByAnno = new ArrayList<SiacTBilElemDet>();
				siacTBileElemDetByAnno.put(anno, elemDetsByAnno);
			}
			elemDetsByAnno.add(elemDet);
		}
		ImportiCapitolo ic = toImportiCapitolo(siacTBileElemDetByAnno, capitolo.getTipoCapitolo().newImportiCapitoloInstance(), capitolo.getAnnoCapitolo().intValue());
		ImportiCapitolo ic1 = toImportiCapitolo(siacTBileElemDetByAnno, capitolo.getTipoCapitolo().newImportiCapitoloInstance(), capitolo.getAnnoCapitolo().intValue() + 1);
		ImportiCapitolo ic2 = toImportiCapitolo(siacTBileElemDetByAnno, capitolo.getTipoCapitolo().newImportiCapitoloInstance(), capitolo.getAnnoCapitolo().intValue() + 2);
		
		capitolo.setImportiCapitolo(ic);
		capitolo.getListaImportiCapitolo().add(ic);
		capitolo.getListaImportiCapitolo().add(ic1);
		capitolo.getListaImportiCapitolo().add(ic2);
		
		return capitolo;
	}
	
	private ImportiCapitolo toImportiCapitolo(Map<String, List<SiacTBilElemDet>> siacTBileElemDetByAnno, ImportiCapitolo template, int anno) {
		return importiCapitoloConverter.toImportiCapitolo(siacTBileElemDetByAnno.get(String.valueOf(anno)),
				template,
				Integer.valueOf(anno),
				EnumSet.noneOf(ImportiCapitoloEnum.class));
	}

	/**
	 * By capitolo.
	 *
	 * @param dest the dest
	 * @param siacTBilElemDetVar the siac t bil elem det var
	 * @return the dettaglio variazione importo capitolo
	 */
	@SuppressWarnings("rawtypes")
	private DettaglioVariazioneImportoCapitolo byCapitolo(List<DettaglioVariazioneImportoCapitolo> dest, SiacTBilElemDetVar siacTBilElemDetVar) {
		
		SiacTBilElem siacTBilElem = siacTBilElemDetVar.getSiacTBilElem();
	
		//Ricerco se gia' esiste un DettaglioVariazioneImportoCapitolo con lo stesso capitolo. Se sì lo restituisco.. 
		for (DettaglioVariazioneImportoCapitolo dettaglioVariazioneImportoCapitolo : dest) {
			if(siacTBilElem.getUid().equals(dettaglioVariazioneImportoCapitolo.getCapitolo().getUid())){
				return dettaglioVariazioneImportoCapitolo;
			}
		}
		
		//Altrimenti creo DettaglioVariazioneImportoCapitolo e lo restitiusco.
		
		DettaglioVariazioneImportoCapitolo dettVi = new DettaglioVariazioneImportoCapitolo();
		dest.add(dettVi);
		
		//Imposto l'anno di competenza
		Integer annoCompetenza = Integer.parseInt(siacTBilElemDetVar.getSiacTPeriodo().getAnno());
		
		//Imposto il capitolo
		Capitolo capitolo = mapToCapitolo(siacTBilElem, annoCompetenza);
		dettVi.setCapitolo(capitolo);
		
		//Imposto i flags
		SiacTBilElemDetVarElemDetFlagEnum.setFlag(dettVi, siacTBilElemDetVar.getElemDetFlag());
		return dettVi;
	}

	@Override
	public SiacTVariazione convertTo(VariazioneImportoCapitolo src, SiacTVariazione dest) {
		if(src.getListaDettaglioVariazioneImporto() == null) {
			return dest;
		}
		SiacRVariazioneStato siacRVariazioneStato = dest.getSiacRVariazioneStatos().get(0);
		siacRVariazioneStato.setSiacTBilElemDetVars(new ArrayList<SiacTBilElemDetVar>());
		
		// SIAC-6883
		for (DettaglioVariazioneImportoCapitolo dettVic : src.getListaDettaglioVariazioneImporto()) {
			// FIXME: verificare la mappatura per evitare errori
			int anno = Integer.parseInt(dest.getSiacTBil().getSiacTPeriodo().getAnno());
			SiacTPeriodo periodo = siacTPeriodoRepository.findByAnnoAndEnteProprietario(Integer.toString(anno), dest.getSiacTEnteProprietario());
			SiacTPeriodo periodo1 = siacTPeriodoRepository.findByAnnoAndEnteProprietario(Integer.toString(anno + 1), dest.getSiacTEnteProprietario());
			SiacTPeriodo periodo2 = siacTPeriodoRepository.findByAnnoAndEnteProprietario(Integer.toString(anno + 2), dest.getSiacTEnteProprietario());
			
			siacRVariazioneStato.addSiacTBilElemDetVar(createSiacTBilElemDetVar(dest, periodo, dettVic.getStanziamento(), SiacDBilElemDetTipoEnum.Stanziamento, dettVic));
			siacRVariazioneStato.addSiacTBilElemDetVar(createSiacTBilElemDetVar(dest, periodo, dettVic.getStanziamentoCassa(), SiacDBilElemDetTipoEnum.StanziamentoCassa, dettVic));
			siacRVariazioneStato.addSiacTBilElemDetVar(createSiacTBilElemDetVar(dest, periodo, dettVic.getStanziamentoResiduo(), SiacDBilElemDetTipoEnum.StanziamentoResiduo, dettVic));
			siacRVariazioneStato.addSiacTBilElemDetVar(createSiacTBilElemDetVar(dest, periodo1, dettVic.getStanziamento1(), SiacDBilElemDetTipoEnum.Stanziamento, dettVic));
			siacRVariazioneStato.addSiacTBilElemDetVar(createSiacTBilElemDetVar(dest, periodo2, dettVic.getStanziamento2(), SiacDBilElemDetTipoEnum.Stanziamento, dettVic));
		}
		return dest;
	}
	
	/**
	 * Creates the siac t bil elem det var.
	 *
	 * @param siacTVariazione the var
	 * @param siacTPeriodo the periodo
	 * @param importo the importo
	 * @param tipoImporto the tipo importo
	 * @param dettVic the dett vic
	 * @return the siac t bil elem det var
	 */
	public SiacTBilElemDetVar createSiacTBilElemDetVar(SiacTVariazione siacTVariazione, SiacTPeriodo siacTPeriodo, BigDecimal importo, SiacDBilElemDetTipoEnum tipoImporto, DettaglioVariazioneImportoCapitolo dettVic) {
		final String methodName = "createSiacTBilElemDetVar";
		
		SiacTBilElemDetVar siacTBilElemDetVar = new SiacTBilElemDetVar();
		siacTBilElemDetVar.setSiacTEnteProprietario(siacTVariazione.getSiacTEnteProprietario());
		siacTBilElemDetVar.setLoginOperazione(siacTVariazione.getLoginOperazione());
		siacTBilElemDetVar.setSiacTPeriodo(siacTPeriodo);
		siacTBilElemDetVar.setElemDetImporto(importo);
		siacTBilElemDetVar.setSiacDBilElemDetTipo(eef.getEntity(tipoImporto,siacTVariazione.getSiacTEnteProprietario().getUid(), SiacDBilElemDetTipo.class));
		
		SiacTBilElemDetVarElemDetFlagEnum elemdDetFlag = SiacTBilElemDetVarElemDetFlagEnum.evaluateFlag(dettVic);
		if(elemdDetFlag!=null){
			siacTBilElemDetVar.setElemDetFlag(elemdDetFlag.getCodice());
		}
		
		SiacTBilElem siacTBilElem = new SiacTBilElem();
		siacTBilElem.setUid(dettVic.getCapitolo().getUid());
		siacTBilElemDetVar.setSiacTBilElem(siacTBilElem);
		
		log.debug(methodName, "tipoImporto: " + tipoImporto + ": " + dettVic.getStanziamento());
		
		return siacTBilElemDetVar;
	}

}
