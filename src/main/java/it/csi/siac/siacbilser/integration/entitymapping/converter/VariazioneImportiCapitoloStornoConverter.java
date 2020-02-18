/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDetVar;
import it.csi.siac.siacbilser.integration.entity.SiacTPeriodo;
import it.csi.siac.siacbilser.integration.entity.SiacTVariazione;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemDetTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTBilElemDetVarElemDetFlagEnum;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siaccommon.util.log.LogUtil;

/**
 * The Class VariazioneImportiCapitoloStornoConverter.
 * @deprecated da eliminare con gli storni
 */
@Component

@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Deprecated
public class VariazioneImportiCapitoloStornoConverter extends DozerConverter<List<DettaglioVariazioneImportoCapitolo>, SiacTVariazione /*List<SiacTBilElemDetVar>*/>  {
	
	/** The log. */
	private LogUtil log = new LogUtil(this.getClass());
	
	/** The siac t periodo repository. */
	@Autowired
	private SiacTPeriodoRepository siacTPeriodoRepository;	
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;

	/**
	 * Instantiates a new variazione importi capitolo storno converter.
	 */
	@SuppressWarnings("unchecked")
	public VariazioneImportiCapitoloStornoConverter() {	
//		super((Class<ArrayList<DettaglioVariazioneImportoCapitolo>>) (new ArrayList<DettaglioVariazioneImportoCapitolo>()).getClass(), 
//				SiacTVariazione.class );
		
		super((Class<List<DettaglioVariazioneImportoCapitolo>>) (Class<?>) List.class, 
				SiacTVariazione.class );
	}


	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public List<DettaglioVariazioneImportoCapitolo> convertFrom(SiacTVariazione src, List<DettaglioVariazioneImportoCapitolo> dest) {
		
		dest = new ArrayList<DettaglioVariazioneImportoCapitolo>();		
		
		for(SiacRVariazioneStato siacRVariazioneStato:src.getSiacRVariazioneStatos()){
			if(siacRVariazioneStato.getDataCancellazione()==null){
				for(SiacTBilElemDetVar siacTBilElemDetVar: siacRVariazioneStato.getSiacTBilElemDetVars()){
					if(siacTBilElemDetVar.getDataCancellazione()==null 
							&& SiacTBilElemDetVarElemDetFlagEnum.Sorgente.getCodice().equals(siacTBilElemDetVar.getElemDetFlag())){
						
						Integer annoCompetenza = Integer.parseInt(siacTBilElemDetVar.getSiacTPeriodo().getAnno());
						DettaglioVariazioneImportoCapitolo dettVi= byAnnoCompetenza(dest, annoCompetenza);
						String fieldName = SiacDBilElemDetTipoEnum.byCodice(siacTBilElemDetVar.getSiacDBilElemDetTipo().getElemDetTipoCode()).getImportiCapitoloFieldName();
						
						BeanWrapper bwDettVi = PropertyAccessorFactory.forBeanPropertyAccess(dettVi);
						bwDettVi.setPropertyValue(fieldName, siacTBilElemDetVar.getElemDetImporto());
						dettVi = (DettaglioVariazioneImportoCapitolo) bwDettVi.getWrappedInstance();
						
					}
				}
				
			}
		}
		
		
		return dest;
	}

	/**
	 * By anno competenza.
	 *
	 * @param dest the dest
	 * @param annoCompetenza the anno competenza
	 * @return the dettaglio variazione importo capitolo
	 */
	private DettaglioVariazioneImportoCapitolo byAnnoCompetenza(List<DettaglioVariazioneImportoCapitolo> dest, Integer annoCompetenza) {
	
//		for (DettaglioVariazioneImportoCapitolo dettaglioVariazioneImportoCapitolo : dest) {
//			if(annoCompetenza.equals(dettaglioVariazioneImportoCapitolo.getAnnoCompetenza())){
//				return dettaglioVariazioneImportoCapitolo;				
//			}
//		}
		DettaglioVariazioneImportoCapitolo dettaglioVariazioneImportoCapitolo = new DettaglioVariazioneImportoCapitolo();
//		dettaglioVariazioneImportoCapitolo.setAnnoCompetenza(annoCompetenza);
		dest.add(dettaglioVariazioneImportoCapitolo);
		
		return dettaglioVariazioneImportoCapitolo;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTVariazione convertTo(List<DettaglioVariazioneImportoCapitolo> src, SiacTVariazione dest) {
//		final String methodName = "convertTo";
		
		SiacRVariazioneStato siacRVariazioneStato = dest.getSiacRVariazioneStatos().get(0); //new SiacRVariazioneStato();		
		siacRVariazioneStato.setSiacTBilElemDetVars(new ArrayList<SiacTBilElemDetVar>());
		
//		for (DettaglioVariazioneImportoCapitolo dettVic : src) {
//			
//			SiacTPeriodo periodo = siacTPeriodoRepository.findByAnnoAndEnteProprietario(""+dettVic.getAnnoCompetenza(),  dest.getSiacTEnteProprietario());
//			
//			SiacTBilElemDetVar det1 = createSiacTBilElemDetVar(dest,periodo, dettVic.getStanziamento(), SiacDBilElemDetTipoEnum.Stanziamento,SiacTBilElemDetVarElemDetFlagEnum.Sorgente);
//			SiacTBilElemDetVar det1abs = createSiacTBilElemDetVarNeg(dest,periodo, dettVic.getStanziamento(), SiacDBilElemDetTipoEnum.Stanziamento,SiacTBilElemDetVarElemDetFlagEnum.Destinazione);
//			log.debug(methodName,"annoCompetenza: "+dettVic.getAnnoCompetenza() +" tipoImporto: "+ SiacDBilElemDetTipoEnum.Stanziamento + ": "+ dettVic.getStanziamento());
//			siacRVariazioneStato.addSiacTBilElemDetVar(det1);
//			siacRVariazioneStato.addSiacTBilElemDetVar(det1abs);
//			
//			SiacTBilElemDetVar det2 = createSiacTBilElemDetVar(dest,periodo, dettVic.getStanziamentoCassa(), SiacDBilElemDetTipoEnum.StanziamentoCassa,SiacTBilElemDetVarElemDetFlagEnum.Sorgente);
//			SiacTBilElemDetVar det2abs = createSiacTBilElemDetVarNeg(dest,periodo, dettVic.getStanziamentoCassa(), SiacDBilElemDetTipoEnum.StanziamentoCassa,SiacTBilElemDetVarElemDetFlagEnum.Destinazione);
//			log.debug(methodName,"annoCompetenza: "+dettVic.getAnnoCompetenza() +" tipoImporto: "+ SiacDBilElemDetTipoEnum.StanziamentoCassa + ": "+ dettVic.getStanziamentoCassa());
//			siacRVariazioneStato.addSiacTBilElemDetVar(det2);
//			siacRVariazioneStato.addSiacTBilElemDetVar(det2abs);
//			
//			SiacTBilElemDetVar det3 = createSiacTBilElemDetVar(dest,periodo, dettVic.getStanziamentoResiduo(), SiacDBilElemDetTipoEnum.StanziamentoResiduo,SiacTBilElemDetVarElemDetFlagEnum.Sorgente);
//			SiacTBilElemDetVar det3abs = createSiacTBilElemDetVarNeg(dest,periodo, dettVic.getStanziamentoResiduo(), SiacDBilElemDetTipoEnum.StanziamentoResiduo,SiacTBilElemDetVarElemDetFlagEnum.Destinazione);
//			log.debug(methodName,"annoCompetenza: "+dettVic.getAnnoCompetenza() +" tipoImporto: "+ SiacDBilElemDetTipoEnum.StanziamentoResiduo + ": "+ dettVic.getStanziamentoResiduo());
//			siacRVariazioneStato.addSiacTBilElemDetVar(det3);
//			siacRVariazioneStato.addSiacTBilElemDetVar(det3abs);
//			
//		}
		
//		dest.setSiacRVariazioneStatos(new ArrayList<SiacRVariazioneStato>());
//		dest.addSiacRVariazioneStato(siacRVariazioneStato);		
		return dest;		
	}
	
	/**
	 * Creates the siac t bil elem det var neg.
	 *
	 * @param var the var
	 * @param periodo the periodo
	 * @param importo the importo
	 * @param tipoImporto the tipo importo
	 * @param elemdDetFlag the elemd det flag
	 * @return the siac t bil elem det var
	 */
	private SiacTBilElemDetVar createSiacTBilElemDetVarNeg(SiacTVariazione var, SiacTPeriodo periodo, BigDecimal importo, SiacDBilElemDetTipoEnum tipoImporto, SiacTBilElemDetVarElemDetFlagEnum elemdDetFlag) {
		
		if(importo!=null){
			importo = importo.multiply(new BigDecimal(-1));
		}
		
		return createSiacTBilElemDetVar(var, periodo, importo, tipoImporto, elemdDetFlag);
	}

	/**
	 * Creates the siac t bil elem det var.
	 *
	 * @param var the var
	 * @param periodo the periodo
	 * @param importo the importo
	 * @param tipoImporto the tipo importo
	 * @param elemdDetFlag the elemd det flag
	 * @return the siac t bil elem det var
	 */
	private SiacTBilElemDetVar createSiacTBilElemDetVar(SiacTVariazione var, SiacTPeriodo periodo, BigDecimal importo, SiacDBilElemDetTipoEnum tipoImporto, SiacTBilElemDetVarElemDetFlagEnum elemdDetFlag) {
		SiacTBilElemDetVar det1 = new SiacTBilElemDetVar();
		det1.setSiacTEnteProprietario(var.getSiacTEnteProprietario());
		det1.setLoginOperazione(var.getLoginOperazione());
		det1.setSiacTPeriodo(periodo);
		det1.setElemDetImporto(importo);
		//det1.setSiacDBilElemDetTipo(tipoImporto.getEntity());
		det1.setSiacDBilElemDetTipo((SiacDBilElemDetTipo) eef.getEntity(tipoImporto,var.getSiacTEnteProprietario().getUid()));
		
		det1.setElemDetFlag(elemdDetFlag.getCodice());
		
		
		return det1;
	}
	
	
	

	

	

}
