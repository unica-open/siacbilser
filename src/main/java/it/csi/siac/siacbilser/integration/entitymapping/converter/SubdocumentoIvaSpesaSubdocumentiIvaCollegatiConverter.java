/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDRelazTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocIva;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocIva;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDRelazTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.SubdocumentoIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.TipoRelazione;



@Component
public class SubdocumentoIvaSpesaSubdocumentiIvaCollegatiConverter extends ExtendedDozerConverter<SubdocumentoIvaSpesa, SiacTSubdocIva > {
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;
	

	/**
	 * Instantiates a new subdocumento iva subdocumenti iva collegati converter.
	 */
	public SubdocumentoIvaSpesaSubdocumentiIvaCollegatiConverter() {
		super(SubdocumentoIvaSpesa.class, SiacTSubdocIva.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SubdocumentoIvaSpesa convertFrom(SiacTSubdocIva siacTSubdocIva, SubdocumentoIvaSpesa dest) {
		final String methodName = "convertFrom";
		
		log.debug(methodName, "dest.uid: "+ dest.getUid());
		
//		if(siacTSubdocIva.getSiacRSubdocIvasFiglio()!=null) {
//			for(SiacRSubdocIva r: siacTSubdocIva.getSiacRSubdocIvasFiglio()) {
//				if(r.getDataCancellazione()==null) {
//					
//					if(SiacDRelazTipoEnum.NotaCreditoIva.getCodice().equals(r.getSiacDRelazTipo().getRelazTipoCode())) {						
//						SiacTSubdocIva siacTSubdocIvaPadre = r.getSiacTSubdocIvaPadre();						
//						SubdocumentoIva notaDiCredito = mapNotNull(siacTSubdocIvaPadre, SubdocumentoIva.class, BilMapId.SiacTSubdocIva_SubdocumentoIva_Base);						
//						dest.addNotaDiCredito(notaDiCredito);	//padre					
//					} else if(SiacDRelazTipoEnum.QuotePerIvaDifferita.getCodice().equals(r.getSiacDRelazTipo().getRelazTipoCode())) {						
//						SiacTSubdocIva siacTSubdocIvaPadre = r.getSiacTSubdocIvaPadre();						
//						SubdocumentoIva quotaIvaDifferita = mapNotNull(siacTSubdocIvaPadre, SubdocumentoIva.class, BilMapId.SiacTSubdocIva_SubdocumentoIva_Base);						
//						dest.addQuotaIvaDifferita(quotaIvaDifferita);	//padre					
//					} 				
//					
//				}
//			}
//		}
		
		
		if(siacTSubdocIva.getSiacRSubdocIvasFiglio()!=null) {
			for(SiacRSubdocIva r: siacTSubdocIva.getSiacRSubdocIvasFiglio()) {			
				if(r.getDataCancellazione()==null) {
					
					SiacTSubdocIva siacTSubdocIvaPadre = r.getSiacTSubdocIvaPadre();						
					SubdocumentoIvaSpesa subdocIva = mapNotNull(siacTSubdocIvaPadre, SubdocumentoIvaSpesa.class, BilMapId.SiacTSubdocIva_SubdocumentoIvaSpesa_Base);
					dest.setSubdocumentoIvaPadre(subdocIva);					
					
					dest.setTipoRelazione(SiacDRelazTipoEnum.byCodice(r.getSiacDRelazTipo().getRelazTipoCode()).getTipoRelazione());	
					
					break;
					
				}
				
			}
		}
		
		
		
		if(siacTSubdocIva.getSiacRSubdocIvasPadre()!=null) {
			for(SiacRSubdocIva r: siacTSubdocIva.getSiacRSubdocIvasPadre()) {			
				if(r.getDataCancellazione()==null) {
					
					if(SiacDRelazTipoEnum.NotaCreditoIva.getCodice().equals(r.getSiacDRelazTipo().getRelazTipoCode())) {						
						SiacTSubdocIva siacTSubdocIvaFiglio = r.getSiacTSubdocIvaFiglio();						
						SubdocumentoIvaSpesa notaDiCredito = new SubdocumentoIvaSpesa();
						mapNotNull(siacTSubdocIvaFiglio, notaDiCredito, BilMapId.SiacTSubdocIva_SubdocumentoIva_Base);						
						dest.addNotaDiCredito(notaDiCredito);	//figlio					
					} else  if(SiacDRelazTipoEnum.QuotePerIvaDifferita.getCodice().equals(r.getSiacDRelazTipo().getRelazTipoCode())) {						
						SiacTSubdocIva siacTSubdocIvaFiglio = r.getSiacTSubdocIvaFiglio();	
						SubdocumentoIvaSpesa quotaIvaDifferita = new SubdocumentoIvaSpesa();
						mapNotNull(siacTSubdocIvaFiglio, quotaIvaDifferita, BilMapId.SiacTSubdocIva_SubdocumentoIva_Base);						
						dest.addQuotaIvaDifferita(quotaIvaDifferita); //figlio				
					} 
				}
				
			}
		}
		
		
		log.debug(methodName, "fine");
		
		return dest;
	}



	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTSubdocIva convertTo(SubdocumentoIvaSpesa subdoc, SiacTSubdocIva dest) {	
				
		dest.setSiacRSubdocIvasPadre(new ArrayList<SiacRSubdocIva>());
		dest.setSiacRSubdocIvasFiglio(new ArrayList<SiacRSubdocIva>());
		
		if(subdoc.getSubdocumentoIvaPadre() != null && subdoc.getTipoRelazione()!=null) {			
			addPadre(dest, subdoc.getSubdocumentoIvaPadre(), subdoc.getTipoRelazione());
		}
		
		for(SubdocumentoIvaSpesa s : subdoc.getListaNoteDiCredito()){
			addFiglio(dest, s, TipoRelazione.NOTA_CREDITO_IVA);
		}
		
		for(SubdocumentoIvaSpesa s : subdoc.getListaQuoteIvaDifferita()){
			addFiglio(dest, s, TipoRelazione.QUOTE_PER_IVA_DIFFERITA);
		}
		
		return dest;	
	}
	
	
	/**
	 * Aggiunge una relazione in cui questo documento (parametro dest) è filgio, mentre il padre è il documento passato nel parametro subdoc.
	 *
	 * @param dest the dest
	 * @param subdoc the subdoc
	 * @param tipoRelazione the tipo relazione
	 */
	private void addPadre(SiacTSubdocIva dest, SubdocumentoIva<?, ?, ?> subdoc, TipoRelazione tipoRelazione) {
		SiacRSubdocIva r = new SiacRSubdocIva();
		r.setSiacTSubdocIvaPadre(dest);
		
		SiacTSubdocIva siacTSubdocIva = new SiacTSubdocIva();
		siacTSubdocIva.setUid(subdoc.getUid());
		r.setSiacTSubdocIvaPadre(siacTSubdocIva);
		
		r.setSiacTSubdocIvaFiglio(dest);
		r.setLoginOperazione(dest.getLoginOperazione());
		r.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		//if(doc.getTipoRelazione()==null){ //se non è specificato il tipoi di relazione di default è DocumentoSubordinato
			subdoc.setTipoRelazione(tipoRelazione);
		//}
		
		SiacDRelazTipo siacDRelazTipo = eef.getEntity(SiacDRelazTipoEnum.byTipoRelazione(subdoc.getTipoRelazione()), dest.getSiacTEnteProprietario().getUid());
		r.setSiacDRelazTipo(siacDRelazTipo);
		
		dest.addSiacRSubdocIvasFiglio(r);
	}
	
	

	/**
	 * Aggiunge una relazione in cui questo documento (parametro dest) è padre, mentre il figlio è il documento passato nel parametro subdoc.
	 *
	 * @param dest the dest
	 * @param subdoc the subdoc
	 * @param tipoRelazione the tipo relazione
	 */
	private void addFiglio(SiacTSubdocIva dest, SubdocumentoIva<?, ?, ?> subdoc, TipoRelazione tipoRelazione) {
		SiacRSubdocIva r = new SiacRSubdocIva();
		r.setSiacTSubdocIvaPadre(dest);
		
		SiacTSubdocIva siacTSubdocIva = new SiacTSubdocIva();
		siacTSubdocIva.setUid(subdoc.getUid());
		r.setSiacTSubdocIvaFiglio(siacTSubdocIva);
		
		r.setSiacTSubdocIvaPadre(dest);
		r.setLoginOperazione(dest.getLoginOperazione());
		r.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		//if(doc.getTipoRelazione()==null){ //se non è specificato il tipoi di relazione di default è DocumentoSubordinato
			subdoc.setTipoRelazione(tipoRelazione);
		//}
		
		SiacDRelazTipo siacDRelazTipo = eef.getEntity(SiacDRelazTipoEnum.byTipoRelazione(subdoc.getTipoRelazione()), dest.getSiacTEnteProprietario().getUid());
		r.setSiacDRelazTipo(siacDRelazTipo);
		
		dest.addSiacRSubdocIvasPadre(r);
	}
	

	
	
	



	

}
