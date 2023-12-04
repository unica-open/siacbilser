/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.helper;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacRMutuoMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacRMutuoProgramma;
import it.csi.siac.siacbilser.integration.entity.SiacRMutuoRipartizione;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.SiacTMutuo;
import it.csi.siac.siacbilser.integration.entity.SiacTMutuoRata;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDMovgestTipoEnum;
import it.csi.siac.siacbilser.integration.entity.helper.base.SiacTEnteBaseHelper;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommon.util.collections.Filter;
import it.csi.siac.siaccommon.util.date.DateUtil;
import it.csi.siac.siaccommon.util.number.NumberUtil;
import it.csi.siac.siaccommonser.util.entity.EntityUtil;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTMutuoHelper extends SiacTEnteBaseHelper {
	
	
	@Autowired SiacTBilElemHelper siacTBilElemHelper;
	@Autowired SiacTMovgestHelper siacTMovgestHelper;
	@Autowired SiacTClassHelper siacTClassHelper;
	
	private static final Comparator<SiacTMutuoRata> SiacTMutuoRataDataScadenzaComparator = new Comparator<SiacTMutuoRata>() {
		@Override
		public int compare(SiacTMutuoRata arg0, SiacTMutuoRata arg1) {
			return DateUtil.compareTo(arg0.getMutuoRataDataScadenza(), arg1.getMutuoRataDataScadenza());
		}};
		
	private static final Comparator<SiacRMutuoMovgestT> SiacRMutuoMovgestTUidComparator = new Comparator<SiacRMutuoMovgestT>() {
		@Override
		public int compare(SiacRMutuoMovgestT arg0, SiacRMutuoMovgestT arg1) {
			return arg0.getUid().compareTo(arg1.getUid());
		}};

		
	private static final Filter<SiacTMutuoRata> SiacTMutuoRataNonScadutaFilter = new Filter<SiacTMutuoRata>() {
		@Override
		public boolean isAcceptable(SiacTMutuoRata source) {
			return !source.isScaduta();
		}};

	public BigDecimal getDebitoResiduo(SiacTMutuo siacTMutuo) {
		try {
			List<SiacTMutuoRata> siacTMutuoRatas = getSiacTMutuoRataList(siacTMutuo);
			
			if (CollectionUtils.isEmpty(siacTMutuoRatas)) {
				return siacTMutuo.getMutuoSommaIniziale();
			}
			
			List<SiacTMutuoRata> siacTMutuoRatasNonScadute = 
					CollectionUtil.sort(getSiacTMutuoRataFilteredList(siacTMutuoRatas, SiacTMutuoRataNonScadutaFilter), SiacTMutuoRataDataScadenzaComparator);

			if (CollectionUtil.isEmpty(siacTMutuoRatasNonScadute)) {
				return BigDecimal.ZERO;
			}
				
			return CollectionUtil.getFirst(siacTMutuoRatasNonScadute).getMutuoRataDebitoIniziale();
		}
		catch (NullPointerException npe) {
			return null;
		}
	}

	public List<SiacTMutuoRata> getSiacTMutuoRataList(SiacTMutuo siacTMutuo) {
		try {
			return EntityUtil.getAllValid(siacTMutuo.getSiacTMutuoRatas());
		}
		catch (NullPointerException npe) {
			return null;
		}
	}

	public List<SiacTMutuoRata> getSiacTMutuoRataSortedList(SiacTMutuo siacTMutuo) {
		try {
			return CollectionUtil.sort(getSiacTMutuoRataList(siacTMutuo), SiacTMutuoRataDataScadenzaComparator);
		}
		catch (NullPointerException npe) {
			return null;
		}
	}
	
	private List<SiacTMutuoRata> getSiacTMutuoRataFilteredList(List<SiacTMutuoRata> siacTMutuoRataList, Filter<SiacTMutuoRata> filter) {
		try {
			return CollectionUtil.filter(siacTMutuoRataList, filter);
		}
		catch (NullPointerException npe) {
			return null;
		}
	}
	
	public List<SiacTMutuoRata> getElencoRateNonScadute(SiacTMutuo siacTMutuo) {
		try {
			return getSiacTMutuoRataFilteredList(getSiacTMutuoRataList(siacTMutuo), SiacTMutuoRataNonScadutaFilter);
		}
		catch (NullPointerException npe) {
			return null;
		}
	}
	
	public Date getDataInizioPianoAmmortamento(SiacTMutuo siacTMutuo) {
		List<SiacTMutuoRata> siacTMutuoRatas = getSiacTMutuoRataSortedList(siacTMutuo);

		return CollectionUtils.isEmpty(siacTMutuoRatas) ?	null : DateUtil.getFirstDayInMonth(CollectionUtil.getFirst(siacTMutuoRatas).getMutuoRataDataScadenza()); 
	}
	
	public Date getDataScadenzaUltimaRata(SiacTMutuo siacTMutuo) {
		List<SiacTMutuoRata> siacTMutuoRatas = getSiacTMutuoRataSortedList(siacTMutuo);
		
		return CollectionUtils.isEmpty(siacTMutuoRatas) ?	null : CollectionUtil.getLast(siacTMutuoRatas).getMutuoRataDataScadenza();  
	}
	
	public Date getDataScadenzaPrimaRata(SiacTMutuo siacTMutuo) {
		List<SiacTMutuoRata> siacTMutuoRatas = getSiacTMutuoRataSortedList(siacTMutuo);
		
		return CollectionUtils.isEmpty(siacTMutuoRatas) ?	null : CollectionUtil.getFirst(siacTMutuoRatas).getMutuoRataDataScadenza();  
	}
	
	public SiacTMutuoRata getPrimaRataNonScaduta(SiacTMutuo siacTMutuo) {
		
		List<SiacTMutuoRata> siacTMutuoRatasNonScadute = CollectionUtil.sort(getElencoRateNonScadute(siacTMutuo), SiacTMutuoRataDataScadenzaComparator);
		
		return CollectionUtils.isEmpty(siacTMutuoRatasNonScadute) ?	null : CollectionUtil.getFirst(siacTMutuoRatasNonScadute); 
		
	}
	
	public List<SiacRMutuoMovgestT> getSiacRMutuoMovgestTList(SiacTMutuo siacTMutuo) {
		try {
			return EntityUtil.getAllValid(siacTMutuo.getSiacRMutuoMovgestT());
		}
		catch (NullPointerException npe) {
			return null;
		}
	}	
	
	public SiacRMutuoMovgestT getSiacRMutuoMovgestT(SiacTMutuo siacTMutuo, Integer movgestId) {
		return CollectionUtil.findFirst(getSiacRMutuoMovgestTList(siacTMutuo), new Filter<SiacRMutuoMovgestT>() {
			@Override
			public boolean isAcceptable(SiacRMutuoMovgestT source) {
				return source.isEntitaValida() && source.getSiacTMovgestT().getSiacTMovgest().getUid().equals(movgestId);
			}
		});
	}	
	
	/**
	 * Restituisce l'elenco delle associazioni tra mutuo e determinato tipo di movimento, valide nell'anno di bilancio corrente
	 * @param siacTMutuo
	 * @param SiacDMovgestTipoEnum I o A
	 * 
	 * 
	 * */
	public List<SiacRMutuoMovgestT> getSiacRMutuoMovgestTFilteredList(SiacTMutuo siacTMutuo, SiacDMovgestTipoEnum siacDMovgestTipoEnum) {
		try {
			return CollectionUtil.filter(getSiacRMutuoMovgestTList(siacTMutuo), new Filter<SiacRMutuoMovgestT>() {
				@Override
				public boolean isAcceptable(SiacRMutuoMovgestT source) {
					return 
						source.getSiacTMovgestT().getSiacTMovgest().getSiacDMovgestTipo().getMovgestTipoCode().equals(siacDMovgestTipoEnum.getCodice()) &&
						Integer.valueOf(Utility.BTL.get().getAnno()).equals(NumberUtil.safeParseInt(source.getSiacTMovgestT().getSiacTMovgest().getSiacTBil().getSiacTPeriodo().getAnno()));
				}});
		}
		catch (NullPointerException npe) {
			return null;
		}
	}
	
	
	public SiacTMovgest getPrimoMovimentoGestioneAssociato(SiacTMutuo siacTMutuo, SiacDMovgestTipoEnum siacDMovgestTipoEnum) {
		try {
			SiacRMutuoMovgestT siacRMutuoMovgestTFirst = 
					CollectionUtil.getFirst(CollectionUtil.sort(getSiacRMutuoMovgestTFilteredList(siacTMutuo, siacDMovgestTipoEnum), SiacRMutuoMovgestTUidComparator));
			
			return siacRMutuoMovgestTFirst.getSiacTMovgestT().getSiacTMovgest();
		}
		catch (NullPointerException npe) {
			return null;
		}
	}
	
	public SiacTClass getTipoFinanziamento(SiacTMutuo siacTMutuo) {
		try {
			
			List<SiacTClass> siacTClassList = siacTBilElemHelper.getSiacTClassList(siacTMovgestHelper.getSiacTBilElem(getPrimoMovimentoGestioneAssociato(siacTMutuo, SiacDMovgestTipoEnum.Impegno)));
			
			SiacTClass siacTClass = CollectionUtil.getOneOnly(siacTClassHelper.filterBySiacDClassTipo(siacTClassList, SiacDClassTipoEnum.TipoFinanziamento));
			
			return siacTClass;
			
		}
		catch (NullPointerException npe) {
			return null;
		}
	}
	
	public List<SiacRMutuoRipartizione> getSiacRMutuoRipartizioneList(SiacTMutuo siacTMutuo) {
		try {
			return EntityUtil.getAllValid(siacTMutuo.getSiacRMutuoRipartizione());
		}
		catch (NullPointerException npe) {
			return null;
		}
	}	
	
	/**
	 * Restituisce l'elenco delle ripartizioni capitoli del mutuo valide nell'anno di bilancio corrente
	 * @param siacTMutuo
	 * 
	 * 
	 * */
	public List<SiacRMutuoRipartizione> getSiacRMutuoRipartizioneFilteredList(SiacTMutuo siacTMutuo) {
		try {
			return CollectionUtil.filter(getSiacRMutuoRipartizioneList(siacTMutuo), new Filter<SiacRMutuoRipartizione>() {
				@Override
				public boolean isAcceptable(SiacRMutuoRipartizione source) {
					return 
						Integer.valueOf(Utility.BTL.get().getAnno()).equals(NumberUtil.safeParseInt(source.getSiacTBilElem().getSiacTBil().getSiacTPeriodo().getAnno()));
				}});
		}
		catch (NullPointerException npe) {
			return null;
		}
	}
	
	public List<SiacRMutuoProgramma> getSiacRMutuoProgrammaList(SiacTMutuo siacTMutuo) {
		try {
			return EntityUtil.getAllValid(siacTMutuo.getSiacRMutuoProgramma());
		}
		catch (NullPointerException npe) {
			return null;
		}
	}
	
	/**
	 * Restituisce l'elenco delle associazioni tra mutuo e programma(progetto) valide,  nell'anno di bilancio corrente.
	 * @param siacTMutuo
	 * 
	 * */
	public List<SiacRMutuoProgramma> getSiacRMutuoProgrammaFilteredList(SiacTMutuo siacTMutuo) {
		try {
			return CollectionUtil.filter(getSiacRMutuoProgrammaList(siacTMutuo), new Filter<SiacRMutuoProgramma>() {
				@Override
				public boolean isAcceptable(SiacRMutuoProgramma source) {
					return 
							Integer.valueOf(Utility.BTL.get().getAnno()).equals(NumberUtil.safeParseInt(source.getSiacTProgramma().getSiacTBil().getSiacTPeriodo().getAnno()));
				}});
		}
		catch (NullPointerException npe) {
			return null;
		}
	}
	
	public SiacRMutuoProgramma getSiacRMutuoProgramma(SiacTMutuo siacTMutuo, Integer programmaId) {
		return CollectionUtil.findFirst(getSiacRMutuoProgrammaList(siacTMutuo), new Filter<SiacRMutuoProgramma>() {
			@Override
			public boolean isAcceptable(SiacRMutuoProgramma source) {
				return source.isEntitaValida() && source.getSiacTProgramma().getUid().equals(programmaId);
			}
		});
	}	
}
