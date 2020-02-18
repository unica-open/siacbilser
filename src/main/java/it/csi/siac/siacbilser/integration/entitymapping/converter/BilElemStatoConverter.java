/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemStato;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemStato;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemStatoEnum;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siaccommon.util.log.LogUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class BilElemStatoConverter.
 */
@Component
public class BilElemStatoConverter extends DozerConverter<Capitolo<?, ?>, SiacTBilElem> {
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;

	/** The log. */
	private LogUtil log = new LogUtil(this.getClass());
	
	/**
	 * Instantiates a new documento spesa sogg converter.
	 */
	@SuppressWarnings("unchecked")
	public BilElemStatoConverter() {
		super((Class<Capitolo<?, ?>>)(Class<?>)Capitolo.class, SiacTBilElem.class);
	}
	
	@Override
	public Capitolo<?, ?> convertFrom(SiacTBilElem src, Capitolo<?, ?> dest) {
		StatoOperativoElementoDiBilancio statoOperativoElementoDiBilancio = getStatoOperativoElementoDiBilancio(src);
		dest.setStatoOperativoElementoDiBilancio(statoOperativoElementoDiBilancio);
		return dest;
	}

	@Override
	public SiacTBilElem convertTo(Capitolo<?, ?> src, SiacTBilElem dest) {
		setSiacRBilElemStatos(src, dest);
		return dest;
	}
	
	
	/**
	 * ottiene StatoOperativoElementoDiBilancio da SiacTBilElem.
	 *
	 * @param src the src
	 * @return the stato operativo elemento di bilancio
	 */
	private StatoOperativoElementoDiBilancio getStatoOperativoElementoDiBilancio(SiacTBilElem src) {
		String methodName = "getStatoOperativoElementoDiBilancio";
		try{
			String codice = src.getSiacRBilElemStatos().get(0).getSiacDBilElemStato().getElemStatoCode();
			return SiacDBilElemStatoEnum.byCodice(codice).getStatoOperativoElementoDiBilancio(); 
		} catch (RuntimeException e){
			log.warn(methodName, "Stato operativo assente per SiacTBilElem.uid(): "+(src!=null?src.getUid():"null"), e);
//			e.printStackTrace();
			return null;
		}
		
	}


	/**
	 * imposta  <!-- StatoOperativoElementoDiBilancio -->
	 * dentro <!-- SiacTBilElem -->.
	 *
	 * @param src the stato
	 * @param bilElem the bil elem
	 */
	private void setSiacRBilElemStatos(Capitolo<?, ?> src, SiacTBilElem bilElem) {
		StatoOperativoElementoDiBilancio soedb = src.getStatoOperativoElementoDiBilancio();
		
		SiacDBilElemStato siacDBilElemStato = eef.getEntity(SiacDBilElemStatoEnum.byStatoOperativoElementoDiBilancio(soedb), bilElem.getSiacTEnteProprietario().getUid(), SiacDBilElemStato.class);
		SiacRBilElemStato siacRBilElemStato = new SiacRBilElemStato();
		siacRBilElemStato.setSiacDBilElemStato(siacDBilElemStato);
		siacRBilElemStato.setSiacTBilElem(bilElem);
		siacRBilElemStato.setSiacTEnteProprietario(bilElem.getSiacTEnteProprietario());
		siacRBilElemStato.setLoginOperazione(src.getLoginOperazione());

		List<SiacRBilElemStato> rBilElemStatoslist = new ArrayList<SiacRBilElemStato>();
		rBilElemStatoslist.add(siacRBilElemStato);
		bilElem.setSiacRBilElemStatos(rBilElemStatoslist);

	}

}
