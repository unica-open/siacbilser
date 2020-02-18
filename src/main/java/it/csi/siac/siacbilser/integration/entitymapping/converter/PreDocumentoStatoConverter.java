/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.SiacRPredocStatoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDPredocStato;
import it.csi.siac.siacbilser.integration.entity.SiacRPredocStato;
import it.csi.siac.siacbilser.integration.entity.SiacTPredoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDPredocStatoEnum;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siacfin2ser.model.PreDocumento;

// TODO: Auto-generated Javadoc
/**
 * The Class PreDocumentoStatoConverter.
 */
@Component
public class PreDocumentoStatoConverter extends DozerConverter<PreDocumento<?, ?>, SiacTPredoc > {
	
	/** The log. */
	private LogUtil log = new LogUtil(this.getClass());
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;
	
	/** The siac r predoc stato repository. */
	@Autowired
	private SiacRPredocStatoRepository siacRPredocStatoRepository;

	/**
	 * Instantiates a new pre documento stato converter.
	 */
	@SuppressWarnings("unchecked")
	public PreDocumentoStatoConverter() {
		super((Class<PreDocumento<?, ?>>)(Class<?>)PreDocumento.class, SiacTPredoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public PreDocumento<?, ?> convertFrom(SiacTPredoc src, PreDocumento<?, ?> dest) {
		for (SiacRPredocStato siacRPredocStato : src.getSiacRPredocStatos()) {
			if(siacRPredocStato.getDataCancellazione()==null){
				dest.setStatoOperativoPreDocumento(SiacDPredocStatoEnum.byCodice(siacRPredocStato.getSiacDPredocStato().getPredocStatoCode()).getStatoOperativo());
			}
		}
		
		
		List<SiacRPredocStato> siacRPredocStatos = siacRPredocStatoRepository.findPredocStatoByPredocIdOrderedyByDataCreazione(src.getPredocId());		
		Integer statoId = null;
		SiacDPredocStatoEnum  stato = null;

		for (SiacRPredocStato siacRPredocStato : siacRPredocStatos) {
			SiacDPredocStato siacDPredocStato = siacRPredocStato.getSiacDPredocStato();
			stato = SiacDPredocStatoEnum.byCodice(siacDPredocStato.getPredocStatoCode());
			Integer statoIdNew = siacDPredocStato.getUid();
			if (!statoIdNew.equals(statoId)) {
				statoId = statoIdNew;
				Date dataInizioValiditaStato  = siacRPredocStato.getDataCreazione();			

				
				if(SiacDPredocStatoEnum.Definito.equals(stato)){
					dest.setDataDefinizione(dataInizioValiditaStato);
				}
				
				if(SiacDPredocStatoEnum.Completo.equals(stato)){
					dest.setDataCompletamento(dataInizioValiditaStato);
				}
				
				if(SiacDPredocStatoEnum.Annullato.equals(stato)){
					dest.setDataAnnullamento(dataInizioValiditaStato);
				}
				
				dest.setDataInizioValiditaStato(dataInizioValiditaStato);		
				
			}		
			
		}

	
		
		
		
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTPredoc convertTo(PreDocumento<?, ?> src, SiacTPredoc dest) {
		final String methodName = "convertTo";
		
		if(dest== null) {
			return dest;
		}
		
		List<SiacRPredocStato> siacRPredocStatos = new ArrayList<SiacRPredocStato>();
		SiacRPredocStato siacRPredocStato = new SiacRPredocStato();
	
		SiacDPredocStatoEnum variazioneStato =  SiacDPredocStatoEnum.byStatoOperativo(src.getStatoOperativoPreDocumento());
		SiacDPredocStato siacDPredocStato = eef.getEntity(variazioneStato, dest.getSiacTEnteProprietario().getUid(), SiacDPredocStato.class); 
				
		log .debug(methodName, "setting siacDPredocStato to: "+siacDPredocStato.getPredocStatoCode()+ " ["+siacDPredocStato.getUid()+"]");
		siacRPredocStato.setSiacDPredocStato(siacDPredocStato);
		siacRPredocStato.setSiacTPredoc(dest);		
		
		siacRPredocStato.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRPredocStato.setLoginOperazione(dest.getLoginOperazione());
		
		
		siacRPredocStatos.add(siacRPredocStato);
		dest.setSiacRPredocStatos(siacRPredocStatos);
		
		return dest;
	}



	

}
