/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.helper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDBilElemDetCompTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDMovgestStato;
import it.csi.siac.siacbilser.integration.entity.SiacDMovgestTsDetTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDSiopeAssenzaMotivazione;
import it.csi.siac.siacbilser.integration.entity.SiacDSoggettoClasse;
import it.csi.siac.siacbilser.integration.entity.SiacRMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacRMovgestTsAttr;
import it.csi.siac.siacbilser.integration.entity.SiacRMutuoMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestTsDet;
import it.csi.siac.siacbilser.integration.entity.SiacTMutuo;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDMovgestTsDetTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDMovgestTsTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.integration.entity.helper.base.SiacTEnteBaseHelper;
import it.csi.siac.siacbilser.model.StatoOperativoMovimentoGestione;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommon.util.collections.Filter;
import it.csi.siac.siaccommonser.util.entity.EntityUtil;


@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTMovgestHelper extends SiacTEnteBaseHelper {

	@Autowired private SiacTMovgestTHelper siacTMovgestTHelper;
	@Autowired private SiacTAttrHelper siacTAttrHelper;

	public SiacTMovgestT getSiacTMovgestTestata(SiacTMovgest siacTMovgest) {
		return CollectionUtil.getOneOnly(CollectionUtil.filter(siacTMovgest.getSiacTMovgestTs(), new ValidTestataFilter()));
	}

	public List<SiacTMovgestT> getSiacTMovgestSubList(SiacTMovgest o1) {
		return CollectionUtil.filter(o1.getSiacTMovgestTs(), new ValidSubMovgestFilter());
	}
	
	public List<SiacTMovgestT> getSiacTMovgestSubNotAnnullatoList(SiacTMovgest o1) {
		return CollectionUtil.filter(o1.getSiacTMovgestTs(), new ValidNotAnnullatoSubMovgestFilter());
	}	
	
	public SiacTBilElem getSiacTBilElem(SiacTMovgest o1) {
		try {
			return 
				EntityUtil.getValid(
					EntityUtil.findFirstValid(o1.getSiacRMovgestBilElems())
					.getSiacTBilElem()
				);
		}
		catch (NullPointerException npe) {
			return null;
		}
	}


	public SiacTMovgest getSiacRMovgestAggiudicazioneSiacTmovgestDa(SiacTMovgest o1) {
		try {
			return 
				EntityUtil.getValid(
					EntityUtil.findFirstValid(o1.getSiacRMovgestAggiudicazionesA())
					.getSiacTMovgestDa()
				);
		}
		catch (NullPointerException npe) {
			return null;
		}
	}

	public SiacTAttoAmm getSiacTAttoAmm(SiacTMovgest o1) {
		return siacTMovgestTHelper.getSiacTAttoAmm(getSiacTMovgestTestata(o1));
	}

	public List<SiacTClass> getSiacTClassList(SiacTMovgest o1) {
		return siacTMovgestTHelper.getSiacTClassList(getSiacTMovgestTestata(o1));
	}
	
	public List<SiacTMovgestTsDet> getSiacTMovgestTsDetList(SiacTMovgest o1) {
		return siacTMovgestTHelper.getSiacTMovgestTsDetList(getSiacTMovgestTestata(o1));
	}
	
	public SiacTSoggetto getSiacTSoggetto(SiacTMovgest o1) {
		return siacTMovgestTHelper.getSiacTSoggetto(getSiacTMovgestTestata(o1));
	}

	public SiacDSoggettoClasse getSiacDSoggettoClasse(SiacTMovgest o1) {
		return siacTMovgestTHelper.getSiacDSoggettoClasse(getSiacTMovgestTestata(o1));
	}

	public SiacDMovgestStato getSiacDMovgestStato(SiacTMovgest o1) {
		return siacTMovgestTHelper.getSiacDMovgestStato(getSiacTMovgestTestata(o1));
	}

	public  List<SiacRMovgestT> getSiacRmovgestT1List(SiacTMovgest o1) {
		return siacTMovgestTHelper.getSiacRmovgestT1List(getSiacTMovgestTestata(o1));
	}

	public List<SiacRMovgestTsAttr> getSiacRattrList(SiacTMovgest o1) {
		return siacTMovgestTHelper.getSiacRattrList(getSiacTMovgestTestata(o1));
	}

	public SiacDSiopeAssenzaMotivazione getSiacDSiopeAssenzaMotivazione(SiacTMovgest o1) {
		return siacTMovgestTHelper.getSiacDSiopeAssenzaMotivazione(getSiacTMovgestTestata(o1));
	}
	
	public class ValidTestataFilter implements Filter<SiacTMovgestT> {

		@Override
		public boolean isAcceptable(SiacTMovgestT source) {
			return source.isEntitaValida() && siacTMovgestTHelper.equalsSiacDMovgestTsTipo(source, SiacDMovgestTsTipoEnum.Testata);
		}
	}
	
	private class ValidSubMovgestFilter implements Filter<SiacTMovgestT> {

		@Override
		public boolean isAcceptable(SiacTMovgestT source) {
			return source.isEntitaValida() && siacTMovgestTHelper.equalsSiacDMovgestTsTipo(source, SiacDMovgestTsTipoEnum.Sub);
		}
	}
	
	private class ValidNotAnnullatoSubMovgestFilter extends ValidSubMovgestFilter {

		@Override
		public boolean isAcceptable(SiacTMovgestT source) {
			return super.isAcceptable(source) && 
					!StatoOperativoMovimentoGestione.ANNULLATO.getCodice().equals(siacTMovgestTHelper.getSiacDMovgestStato(source).getMovgestStatoCode());
		}
	}
	
	public SiacRMovgestTsAttr getSiacRattr(SiacTMovgest o1, SiacTAttrEnum siacTAttrEnum) {
		return CollectionUtil.getOneOnly(CollectionUtil.filter(siacTMovgestTHelper.getSiacRattrList(getSiacTMovgestTestata(o1)), new Filter<SiacRMovgestTsAttr>() {
			@Override
			public boolean isAcceptable(SiacRMovgestTsAttr source) {
				return source.isEntitaValida() && siacTAttrHelper.checkAttrCode(source.getSiacTAttr(), siacTAttrEnum);
			}

		}));
	}
	
	public SiacTMovgestTsDet getSiacTMovgestTsDet(SiacTMovgest o1, SiacDMovgestTsDetTipoEnum siacDMovgestTsDetTipoEnum) {
		return siacTMovgestTHelper.getSiacTMovgestTsDet(getSiacTMovgestTestata(o1), siacDMovgestTsDetTipoEnum);
	}
	
	public SiacDBilElemDetCompTipo getSiacDBilElemDetTipo(SiacTMovgest o1) {
		try {
			return 
				EntityUtil.getValid(
					EntityUtil.findFirstValid(o1.getSiacRMovgestBilElems())
					.getSiacDBilElemDetCompTipo()
				);
		}
		catch (NullPointerException npe) {
			return null;
		}
	}
	
	public List<SiacRMutuoMovgestT> getSiacRMutuoMovgestTList(SiacTMovgest o1) {
		return siacTMovgestTHelper.getSiacRMutuoMovgestTList(getSiacTMovgestTestata(o1));
	}	
}


