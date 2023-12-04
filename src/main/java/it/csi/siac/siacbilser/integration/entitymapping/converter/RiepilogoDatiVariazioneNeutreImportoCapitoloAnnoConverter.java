/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDVariazioneStatoEnum;
import it.csi.siac.siacbilser.model.DatiVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.DatiVariazioneImportoCapitoloAnno;
import it.csi.siac.siacbilser.model.RiepilogoDatiVariazioneImportoCapitoloAnno;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneBilancio;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;

/**
 * The Class RiepilogoDatiVariazioneImportoCapitoloAnnoConverter.
 */
@Component
public class RiepilogoDatiVariazioneNeutreImportoCapitoloAnnoConverter extends DozerConverter<RiepilogoDatiVariazioneImportoCapitoloAnno, List<Object[]>> {
	
	/** The log. */
	private LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	/**
	 * Instantiates a new vincolo stato converter.
	 */
	@SuppressWarnings("unchecked")
	public RiepilogoDatiVariazioneNeutreImportoCapitoloAnnoConverter() {
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
				|| !(arr[3] instanceof Integer)) {
			log.info(methodName, "Ignoro la riga " + Arrays.toString(arr) + " in quanto i dati non sono di tipo corretto");
			return;
		}
		
		
		// Anno, codice stato variazione, count
		Integer anno = Integer.valueOf((String)arr[0]);
		StatoOperativoVariazioneBilancio sovdb = SiacDVariazioneStatoEnum.byCodice((String)arr[1]).getStatoOperativoVariazioneDiBilancio();
		//Long count = (Long)arr[2];
		Integer idVariazione = (Integer)arr[3];
		
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
		//dvic.setNumero(count);
		dvic.setVariazioneId(idVariazione);
	}

	@Override
	public List<Object[]> convertTo(RiepilogoDatiVariazioneImportoCapitoloAnno src, List<Object[]> dest) {
		// Non converto
		return dest;
	}

}
