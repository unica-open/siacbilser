/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemDetTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDVariazioneStatoEnum;
import it.csi.siac.siacbilser.model.DatiVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.DatiVariazioneImportoCapitoloAnno;
import it.csi.siac.siacbilser.model.RiepilogoDatiVariazioneImportoCapitoloAnno;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneDiBilancio;
import it.csi.siac.siaccommon.util.log.LogUtil;

/**
 * The Class RiepilogoDatiVariazioneImportoCapitoloAnnoConverter.
 */
@Component
public class RiepilogoDatiVariazioneImportoCapitoloAnnoTotaleConverter extends DozerConverter<RiepilogoDatiVariazioneImportoCapitoloAnno, List<Object[]>> {
	
	/** The log. */
	private LogUtil log = new LogUtil(this.getClass());
//	private static final ThreadLocal<Map<String, Method>> METHOD_CACHE = new ThreadLocal<Map<String, Method>>() {
//		protected Map<String,Method> initialValue() {
//			return new HashMap<String, Method>();
//		}
//	};
	
	/**
	 * Instantiates a new vincolo stato converter.
	 */
	@SuppressWarnings("unchecked")
	public RiepilogoDatiVariazioneImportoCapitoloAnnoTotaleConverter() {
		super(RiepilogoDatiVariazioneImportoCapitoloAnno.class, (Class<List<Object[]>>)(Class<?>)List.class);
	}

	@Override
	public RiepilogoDatiVariazioneImportoCapitoloAnno convertFrom(List<Object[]> src, RiepilogoDatiVariazioneImportoCapitoloAnno dest) {
		for(Object[] arr : src) {
			convertArray(arr, dest.getDatiVariazioneImportiCapitoloPerAnno());
		}
		return dest;
	}
	
	private void convertArray(Object[] arr, Map<Integer, DatiVariazioneImportoCapitoloAnno> dest) {
		final String methodName = "convertFrom";
		if(!(arr[0] instanceof String)
				|| !(arr[1] instanceof String)
				|| !(arr[2] instanceof String)
				|| !(arr[3] instanceof BigDecimal)) {
			log.info(methodName, "Ignoro la riga " + Arrays.toString(arr) + " in quanto i dati non sono di tipo corretto");
			return;
		}
		
		
		// Anno, codice stato variazione, tipo importo, importo
		Integer anno = Integer.valueOf((String)arr[0]);
		StatoOperativoVariazioneDiBilancio sovdb = SiacDVariazioneStatoEnum.byCodice((String)arr[1]).getStatoOperativoVariazioneDiBilancio();
		SiacDBilElemDetTipoEnum sdbedte = SiacDBilElemDetTipoEnum.byCodice((String)arr[2]);
		BigDecimal importo = (BigDecimal)arr[3];
		
		DatiVariazioneImportoCapitoloAnno dvica = dest.get(anno);
		if(dvica == null) {
			dvica = new DatiVariazioneImportoCapitoloAnno();
			dest.put(anno, dvica);
		}
		
		DatiVariazioneImportoCapitolo dvic = dvica.getDatiVariazioneCapitolo().get(sovdb);
		if(dvic == null) {
			dvic = new DatiVariazioneImportoCapitolo();
			dvica.getDatiVariazioneCapitolo().put(sovdb, dvic);
		}
		
		dvic.setAnno(anno);
		dvic.setStatoOperativoVariazioneDiBilancio(sovdb);
		
		String fieldName = sdbedte.getImportiCapitoloFieldName();
//		Map<String, Method> cache = METHOD_CACHE.get();
//		Method setter = cache.get(fieldName);
//		if(setter == null) {
//			try {
//				setter = dvic.getClass().getMethod("set" + StringUtils.capitalize(fieldName), BigDecimal.class);
//			} catch (SecurityException e) {
//				throw new IllegalStateException("Security exception in retrieving method", e);
//			} catch (NoSuchMethodException e) {
//				throw new IllegalStateException("No such method exception in retrieving method", e);
//			}
//		}
		
		// E' possibile utilizzare una cache dei metodi. Da valutare le tempistiche nel caso completo
		try {
			Method setter = dvic.getClass().getMethod("set" + StringUtils.capitalize(fieldName), BigDecimal.class);
			setter.invoke(dvic, importo);
		} catch (SecurityException e) {
			throw new IllegalStateException("Security exception in setting field", e);
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException("No such method exception in setting field", e);
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException("Illegal argument exception in setting field", e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException("Illegal access exception in setting field", e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException("Invocation target exception in setting field", e);
		}
	}

	@Override
	public List<Object[]> convertTo(RiepilogoDatiVariazioneImportoCapitoloAnno src, List<Object[]> dest) {
		// Non converto
		return dest;
	}

}
