/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.dozer.CustomConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siaccommon.model.ModelDetailEnum;
import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siaccommonser.integration.entitymapping.Converter;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfinser.CommonUtil;
import it.csi.siac.siacfinser.StringUtilsFin;

public class ExtendedBaseFinDad extends BaseDadImpl {
	
	@Autowired
	protected ApplicationContext appCtx;

	@PersistenceContext
	protected EntityManager entityManager;
	
	protected Ente ente;
	protected String loginOperazione;
	
	
	protected void flushAndClearEntMng() {
		entityManager.flush();
		entityManager.clear();
	}
	
	protected void flushEntMng() {
		entityManager.flush();
	}

	public void setEnte(Ente ente) {
		this.ente = ente;
	}

	public void setLoginOperazione(String loginOperazione) {
		this.loginOperazione = loginOperazione;
	}
	
	// SEMPLICI WRAPPER DI METODI DI COMMONUTILS e String Utils per scrivere piu velocemente le
		// chiamate ad essi:
		protected <T extends Object> List<T> toList(T... oggetto) {
			return CommonUtil.toList(oggetto);
		}

		protected <T extends Object> List<T> toList(List<T>... liste) {
			return CommonUtil.toList(liste);
		}

		protected <T extends Object> List<T> addAll(List<T> listaTo,
				List<T> listaFrom) {
			return CommonUtil.addAll(listaTo, listaFrom);
		}
		
		protected boolean isEmpty(String s){
			return StringUtilsFin.isEmpty(s);
		}
		
		protected <OBJ extends Object> boolean isEmpty(List<OBJ> list){
			return StringUtilsFin.isEmpty(list);
		}
		
		public <T extends Object> T getFirst(List<T> lista){
			return CommonUtil.getFirst(lista);
		}
		
		@Override
		protected Converter[] getConverterByModelDetail(ModelDetailEnum... modelDetails) {
			return Converters.byModelDetails(modelDetails);
		}
		@Override
		protected CustomConverter getConverterFromComponent(Class<? extends CustomConverter> converterClass) {
			return appCtx.getBean(Utility.toDefaultBeanName(converterClass), converterClass);
		}
}
