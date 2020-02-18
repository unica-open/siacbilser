/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Set;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;

import it.csi.siac.siacbilser.integration.dao.CassaEconomaleDao;
import it.csi.siac.siacbilser.integration.entity.SiacTCassaEcon;
import it.csi.siac.siacbilser.model.ImportiCassaEconomaleEnum;
import it.csi.siac.siacbilser.model.ImportoDerivato;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccommon.util.log.LogUtil;

/**
 * The Class CassaEconomaleImportiConverter.
 */
@Component
public class CassaEconomaleImportiConverter extends ExtendedDozerConverter<CassaEconomale, SiacTCassaEcon > {
	
	@Autowired
	private CassaEconomaleDao cassaEconomaleDao;

	/**
	 * Instantiates a new cassa economale importi converter converter.
	 */
	public CassaEconomaleImportiConverter() {
		super(CassaEconomale.class, SiacTCassaEcon.class);
	}

	@Override
	public CassaEconomale convertFrom(SiacTCassaEcon src, final CassaEconomale dest) {
		return dest;
	}

	@Override
	public SiacTCassaEcon convertTo(CassaEconomale src, SiacTCassaEcon dest) {
		return dest;
	}

	public void popolaImportiDerivati(final CassaEconomale dest, final Integer annoBilancio, final Set<ImportiCassaEconomaleEnum> importiDerivatiRichiesti) {
		ReflectionUtils.doWithFields(CassaEconomale.class, new PopolaImportiDerivatiCassaEconomaleFieldCallback(dest, importiDerivatiRichiesti, annoBilancio, cassaEconomaleDao), new FieldFilter() {
			@Override
			public boolean matches(Field field) {
				return field.isAnnotationPresent(ImportoDerivato.class);
			}
		});
	}
	
	/**
	 * Callback per il popolamento degli importi derivati
	 * @author Marchino Alessandro
	 */
	private static class PopolaImportiDerivatiCassaEconomaleFieldCallback implements FieldCallback {
		private final LogUtil log = new LogUtil(getClass());
		
		private final Set<ImportiCassaEconomaleEnum> importiDerivatiRichiesti;
		private final Integer annoBilancio;
		private final CassaEconomaleDao cassaEconomaleDao;
		
		private final Integer uid;
		private final BeanWrapper beanWrapper;

		/**
		 * Costruttore
		 * @param cassaEconomale
		 * @param importiDerivatiRichiesti
		 * @param annoBilancio
		 * @param cassaEconomaleDao
		 */
		PopolaImportiDerivatiCassaEconomaleFieldCallback(CassaEconomale cassaEconomale, Set<ImportiCassaEconomaleEnum> importiDerivatiRichiesti, Integer annoBilancio, CassaEconomaleDao cassaEconomaleDao) {
			this.importiDerivatiRichiesti = importiDerivatiRichiesti;
			this.annoBilancio = annoBilancio;
			this.cassaEconomaleDao = cassaEconomaleDao;
			
			this.uid = Integer.valueOf(cassaEconomale.getUid());
			this.beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(cassaEconomale);
		}

		@Override
		public void doWith(Field field) {
			final String methodName = "doWith";
			ImportoDerivato annotation = field.getAnnotation(ImportoDerivato.class);
			if(annotation == null || annotation.value() == null) {
				// Se non ho l'annotazione (improbabile) o non ho la funzione specificata (protezione), esco
				return;
			}
			if((importiDerivatiRichiesti != null && !importiDerivatiRichiesti.contains(ImportiCassaEconomaleEnum.valueOf(field.getName())))
					|| (importiDerivatiRichiesti == null && !annotation.calcolareDiDefault())) {
				// Se ho la lista di importi richiesti che non contiene l'elemento, ignoro
				// Se non ho la lista ma l'importo non e' da calcolare di default, ignoro
				log.debug(methodName, "field saltato: " + field.getName() + " for cassaEconomale: " + uid);
				return;
			}
			
			log.info(methodName, "invoking function " + annotation.value().getFunctionName() + " for cassaEconomale: " + uid);
			BigDecimal importo = cassaEconomaleDao.findImportoDerivato(uid, annoBilancio, annotation.value().getFunctionName());
			
			try {
				beanWrapper.setPropertyValue(field.getName(), importo);
			} catch(BeansException be) {
				log.warn("Cannot set importo derivato: " + field.getName() + " for cassaEconomale: " + uid, be);
			}
		}
	}
}
