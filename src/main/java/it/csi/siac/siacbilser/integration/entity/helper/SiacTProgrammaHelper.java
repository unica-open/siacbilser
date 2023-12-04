/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.helper;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDProgrammaStato;
import it.csi.siac.siacbilser.integration.entity.SiacRMutuoProgramma;
import it.csi.siac.siacbilser.integration.entity.SiacRProgrammaAttr;
import it.csi.siac.siacbilser.integration.entity.SiacRProgrammaClass;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTProgramma;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.integration.entity.helper.base.SiacTEnteBaseHelper;
import it.csi.siac.siacbilser.model.mutuo.MutuoAssociatoEntita;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommon.util.collections.Filter;
import it.csi.siac.siaccommon.util.collections.Function;
import it.csi.siac.siaccommonser.util.entity.EntityUtil;


@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTProgrammaHelper extends SiacTEnteBaseHelper {
	
	@Autowired SiacTAttrHelper siacTAttrHelper;

	public SiacTAttoAmm getSiacTAttoAmm(SiacTProgramma o1) {
		try {
			return 
				EntityUtil.getValid(
					EntityUtil.findFirstValid(o1.getSiacRProgrammaAttoAmms())
					.getSiacTAttoAmm()
				);
		}
		catch (NullPointerException npe) {
			return null;
		}
	}
	
	public List<SiacTClass> getSiacTClassList(SiacTProgramma obj) {
		try {
			return EntityUtil.getAllValidMappedBy(obj.getSiacRProgrammaClasses(), new Function<SiacRProgrammaClass, SiacTClass>(){
				@Override
				public SiacTClass map(SiacRProgrammaClass s) {
					return s.getSiacTClass();
				}}
			);
		}
		catch (NullPointerException npe) {
			return null;
		}
	}
	
	public List<SiacRProgrammaAttr> getSiacRattrList(SiacTProgramma o1) {
		try {
			return 
				EntityUtil.getAllValid(o1.getSiacRProgrammaAttrs());
		}
		catch (NullPointerException npe) {
			return null;
		}
	}	
	
	public SiacRProgrammaAttr getSiacRAttr(SiacTProgramma o1, SiacTAttrEnum siacTAttrEnum) {
		return CollectionUtil.getOneOnly(CollectionUtil.filter(getSiacRattrList(o1), new Filter<SiacRProgrammaAttr>() {
			@Override
			public boolean isAcceptable(SiacRProgrammaAttr source) {
				return source.isEntitaValida() && siacTAttrHelper.checkAttrCode(source.getSiacTAttr(), siacTAttrEnum);
			}
		}));
	}
	public SiacDProgrammaStato getSiacDMovgestStato(SiacTProgramma o1) {
		try {
			return EntityUtil.getValid(EntityUtil.findFirstValid(o1.getSiacRProgrammaStatos()).getSiacDProgrammaStato());
		}
		catch (NullPointerException npe) {
			return null;
		}
	}	
	
	public List<SiacRMutuoProgramma> getSiacRMutuoProgrammaList(SiacTProgramma o1) {
		try {
			return EntityUtil.getAllValid(o1.getSiacRMutuoProgramma());
		}
		catch (NullPointerException npe) {
			return null;
		}
	}	
}


