/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.helper;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDMovgestStato;
import it.csi.siac.siacbilser.integration.entity.SiacDMovgestTsDetTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDSiopeAssenzaMotivazione;
import it.csi.siac.siacbilser.integration.entity.SiacDSoggettoClasse;
import it.csi.siac.siacbilser.integration.entity.SiacRMovgestClass;
import it.csi.siac.siacbilser.integration.entity.SiacRMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacRMovgestTsAttr;
import it.csi.siac.siacbilser.integration.entity.SiacRMutuoMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestTsDet;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDMovgestTsDetTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDMovgestTsTipoEnum;
import it.csi.siac.siacbilser.integration.entity.helper.base.SiacTEnteBaseHelper;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommon.util.collections.Filter;
import it.csi.siac.siaccommon.util.collections.Function;
import it.csi.siac.siaccommonser.util.entity.EntityUtil;


@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTMovgestTHelper extends SiacTEnteBaseHelper {

	public SiacTAttoAmm getSiacTAttoAmm(SiacTMovgestT o) {
		try {
			return EntityUtil.findFirstValid(o.getSiacRMovgestTsAttoAmms()).getSiacTAttoAmm();
		}
		catch (NullPointerException npe) {
			return null;
		}
	}

	public List<SiacTMovgestTsDet> getSiacTMovgestTsDetList(SiacTMovgestT o) {
		try {
			return EntityUtil.getAllValid(o.getSiacTMovgestTsDets());
		}
		catch (NullPointerException npe) {
			return null;
		}
	}
	
	public List<SiacTClass> getSiacTClassList(SiacTMovgestT o) {
		try {
			return EntityUtil.getAllValidMappedBy(o.getSiacRMovgestClasses(), new Function<SiacRMovgestClass, SiacTClass>(){

				@Override
				public SiacTClass map(SiacRMovgestClass source) {
					return source.getSiacTClass();
				}});
		}
		catch (NullPointerException npe) {
			return null;
		}
	}
	
	
	public SiacTSoggetto getSiacTSoggetto(SiacTMovgestT o1) {
		try {
			return 
				EntityUtil.findFirstValid(o1.getSiacRMovgestTsSogs()).getSiacTSoggetto();
		}
		catch (NullPointerException npe) {
			return null;
		}
	}

	public SiacDSoggettoClasse getSiacDSoggettoClasse(SiacTMovgestT o1) {
		try {
			return 
				EntityUtil.findFirstValid(o1.getSiacRMovgestTsSogclasses()).getSiacDSoggettoClasse();
		}
		catch (NullPointerException npe) {
			return null;
		}
	}

	public  List<SiacRMovgestT> getSiacRmovgestT1List(SiacTMovgestT o1) {
		try {
			return 
				EntityUtil.getAllValid(o1.getSiacRMovgestT1s());
		}
		catch (NullPointerException npe) {
			return null;
		}
	}

	public List<SiacRMovgestTsAttr> getSiacRattrList(SiacTMovgestT o1) {
		try {
			return 
				EntityUtil.getAllValid(o1.getSiacRMovgestTsAttrs());
		}
		catch (NullPointerException npe) {
			return null;
		}
	}

	public SiacDSiopeAssenzaMotivazione getSiacDSiopeAssenzaMotivazione(SiacTMovgestT o1) {
		try {
			return 
				EntityUtil.getValid(o1.getSiacDSiopeAssenzaMotivazione());
		}
		catch (NullPointerException npe) {
			return null;
		}
	}

	public SiacDMovgestStato getSiacDMovgestStato(SiacTMovgestT o1) {
		try {
			return EntityUtil.getValid(EntityUtil.findFirstValid(o1.getSiacRMovgestTsStatos()).getSiacDMovgestStato());
		}
		catch (NullPointerException npe) {
			return null;
		}
	}
	
	public boolean equalsSiacDMovgestTsTipo(SiacTMovgestT siacTMovgestT, SiacDMovgestTsTipoEnum siacDMovgestTsTipo) { 
		return siacDMovgestTsTipo.equals(SiacDMovgestTsTipoEnum.byCodice(siacTMovgestT.getSiacDMovgestTsTipo().getMovgestTsTipoCode()));
	}
	
	public boolean checkSiacDMovgestTsDetTipo(SiacDMovgestTsDetTipo siacDMovgestTsDetTipo, SiacDMovgestTsDetTipoEnum siacDMovgestTsDetTipoEnum) {
		return siacDMovgestTsDetTipo != null && siacDMovgestTsDetTipo.isEntitaValida() && 
				siacDMovgestTsDetTipoEnum.getCodice().equals(siacDMovgestTsDetTipo.getMovgestTsDetTipoCode());
	}
	
	public List<SiacRMutuoMovgestT> getSiacRMutuoMovgestTList(SiacTMovgestT o1) {
		try {
			return EntityUtil.getAllValid(o1.getSiacRMutuoMovgestTs());
		}
		catch (NullPointerException npe) {
			return null;
		}
	}
	
	public SiacTMovgestTsDet getSiacTMovgestTsDet(SiacTMovgestT o1, SiacDMovgestTsDetTipoEnum siacDMovgestTsDetTipoEnum) {
		return CollectionUtil.getOneOnly(CollectionUtil.filter(getSiacTMovgestTsDetList(o1), new Filter<SiacTMovgestTsDet>() {
			@Override
			public boolean isAcceptable(SiacTMovgestTsDet source) {
				return source.isEntitaValida() && checkSiacDMovgestTsDetTipo(source.getSiacDMovgestTsDetTipo(), siacDMovgestTsDetTipoEnum);
			}
		}));
	}	
}


