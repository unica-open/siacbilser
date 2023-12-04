/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.helper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDDocStato;
import it.csi.siac.siacbilser.integration.entity.SiacRDocAttr;
import it.csi.siac.siacbilser.integration.entity.SiacRDocOrdine;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTOrdinativo;
import it.csi.siac.siacbilser.integration.entity.SiacTOrdine;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.helper.base.SiacTEnteBaseHelper;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommon.util.collections.Function;
import it.csi.siac.siaccommon.util.collections.Predicate;
import it.csi.siac.siaccommonser.util.entity.EntityUtil;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTDocHelper extends SiacTEnteBaseHelper {

	@Autowired private SiacTSubdocHelper siacTSubdocHelper;
	@Autowired private SiacTOrdinativoHelper siacTOrdinativoHelper;
	
	public SiacTSoggetto getSiacTSoggetto(SiacTDoc siacTDoc) {
		try {
			return EntityUtil.getValid(EntityUtil.findFirstValid(siacTDoc.getSiacRDocSogs()).getSiacTSoggetto());
		}
		catch (NullPointerException npe) {
			return null;
		}
	}
	
	public List<SiacRDocAttr> getSiacRDocAttrList(SiacTDoc siacTDoc) {
		try {
			return EntityUtil.getAllValid(siacTDoc.getSiacRDocAttrs());
		}
		catch (NullPointerException npe) {
			return null;
		}
	}

	public List<SiacTOrdine> getSiacTOrdineList(SiacTDoc siacTDoc) {
		try {
			return EntityUtil.getAllValidMappedBy(siacTDoc.getSiacRDocOrdines(), new Function<SiacRDocOrdine, SiacTOrdine>(){
				@Override
				public SiacTOrdine map(SiacRDocOrdine siacRDocOrdine) {
					return siacRDocOrdine.getSiacTOrdine();
				}}
			);
		}
		catch (NullPointerException npe) {
			return null;
		}
	}

	public List<SiacTSubdoc> getSiacTSubdocList(SiacTDoc siacTDoc) {
		try {
			return EntityUtil.getAllValid(siacTDoc.getSiacTSubdocs());
		}
		catch (NullPointerException npe) {
			return null;
		}
	}

	public SiacDDocStato getSiacDDocStato(SiacTDoc siacTDoc) {
		try {
			return EntityUtil.getValid(EntityUtil.findFirstValid(siacTDoc.getSiacRDocStatos()).getSiacDDocStato());
		}
		catch (NullPointerException npe) {
			return null;
		}
	}
	
	public List<SiacTOrdinativo> getMandatiNonAnnullati(SiacTDoc siacTDoc) {
		
		List<SiacTOrdinativo> siacTOrdinativoList = new ArrayList<SiacTOrdinativo>();
		
		CollectionUtil.apply(getSiacTSubdocList(siacTDoc), new Predicate<SiacTSubdoc>() {

			@Override
			public void apply(SiacTSubdoc source) {
				CollectionUtil.addAll(
					siacTOrdinativoList, 
					CollectionUtil.filter(siacTSubdocHelper.getSiacTOrdinativoList(source), 
							siacTOrdinativoHelper.getValidNotAnnullatoSiacTOrdinativoFilter()));	
			}});

		return siacTOrdinativoList;
	}
}
