/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRDocOnere;
import it.csi.siac.siacbilser.integration.entity.SiacRDocOnereOrdinativoT;
import it.csi.siac.siacbilser.integration.entity.SiacROrdinativoStato;
import it.csi.siac.siacbilser.integration.entity.SiacTOrdinativo;
import it.csi.siac.siacbilser.integration.entity.SiacTOrdinativoT;
import it.csi.siac.siacbilser.integration.entity.SiacTOrdinativoTsDet;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDOrdinativoStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDOrdinativoTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDOrdinativoTsDetTipoEnum;
import it.csi.siac.siacfin2ser.model.DettaglioOnere;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo.StatoOperativoOrdinativo;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativo;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoPagamento;


/**
 * The Class OnereAttrConverter.
 */
@Component
public class DettaglioOnereSubOrdinativiConverter extends DozerConverter<DettaglioOnere, SiacRDocOnere > {
	
	/**
	 * Instantiates a new onere attr converter.
	 */
	public DettaglioOnereSubOrdinativiConverter() {
		super(DettaglioOnere.class, SiacRDocOnere.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public DettaglioOnere convertFrom(SiacRDocOnere siacRDocOnere, DettaglioOnere dettaglioOnere) {
		
		if(siacRDocOnere.getSiacRDocOnereOrdinativoTs() == null){
			return dettaglioOnere;
		}
		List<SubOrdinativoIncasso> listaIncasso = new ArrayList<SubOrdinativoIncasso>();
		List<SubOrdinativoPagamento> listaPagamento = new ArrayList<SubOrdinativoPagamento>();
		
		for(SiacRDocOnereOrdinativoT siacRDocOnereOrdinativoT : siacRDocOnere.getSiacRDocOnereOrdinativoTs()){
			
			if(!(siacRDocOnereOrdinativoT.getDataCancellazione() == null 
					&& siacRDocOnereOrdinativoT.getSiacTOrdinativoT() != null 
					&& siacRDocOnereOrdinativoT.getSiacTOrdinativoT().getDataCancellazione() == null)){
				
				continue;
			}
			
			SiacTOrdinativoT siacTOrdinativoT = siacRDocOnereOrdinativoT.getSiacTOrdinativoT();
			SiacTOrdinativo siacTOrdinativo = siacTOrdinativoT.getSiacTOrdinativo();
			if(siacTOrdinativo.getDataCancellazione()!=null || isOrdinativoAnnullato(siacTOrdinativo)){
				continue;
			}
			
			String ordTipoCode = siacTOrdinativo.getSiacDOrdinativoTipo().getOrdTipoCode();
			SiacDOrdinativoTipoEnum siacDOrdinativoTipoEnum = SiacDOrdinativoTipoEnum.byCodice(ordTipoCode);
			boolean isIncasso = SiacDOrdinativoTipoEnum.Incasso.equals(siacDOrdinativoTipoEnum);
			boolean isPagamento = SiacDOrdinativoTipoEnum.Pagamento.equals(siacDOrdinativoTipoEnum);
			
			
			SubOrdinativo subOrdinativo = isIncasso?new SubOrdinativoIncasso():new SubOrdinativoPagamento();
			subOrdinativo.setUid(siacTOrdinativoT.getUid());
			subOrdinativo.setIdOrdinativo(new BigDecimal(siacTOrdinativoT.getSiacTOrdinativo().getUid()));
			subOrdinativo.setAnno(siacTOrdinativoT.getSiacTOrdinativo().getOrdAnno());
			subOrdinativo.setNumero(Integer.valueOf(siacTOrdinativoT.getOrdTsCode()));
			subOrdinativo.setNumeroOrdinativo(siacTOrdinativoT.getSiacTOrdinativo().getOrdNumero().intValue());
			if(siacTOrdinativoT.getSiacTOrdinativoTsDets()!=null) {
				for (SiacTOrdinativoTsDet siacTOrdinativoTsDet : siacTOrdinativoT.getSiacTOrdinativoTsDets()) {
					if (SiacDOrdinativoTsDetTipoEnum.ATTUALE.getCodice().equals(siacTOrdinativoTsDet.getSiacDOrdinativoTsDetTipo().getOrdTsDetTipoCode())
							&& siacTOrdinativoTsDet.getDataCancellazione() == null) {
						subOrdinativo.setImportoAttuale(siacTOrdinativoTsDet.getOrdTsDetImporto());
						break;
					}
				}
				
			}
			
			// Stato
			popolaStato(siacTOrdinativo, subOrdinativo);
			
			
			if(isIncasso) {
				listaIncasso.add((SubOrdinativoIncasso)subOrdinativo);
			} else if (isPagamento) {
				listaPagamento.add((SubOrdinativoPagamento)subOrdinativo);
			}
				
		}
		dettaglioOnere.setSubordinativiIncasso(listaIncasso);
		dettaglioOnere.setSubordinativiPagamento(listaPagamento);
		return dettaglioOnere;
	}



	private void popolaStato(SiacTOrdinativo siacTOrdinativo, SubOrdinativo subOrdinativo) {
		if(siacTOrdinativo.getSiacROrdinativoStatos() != null) {
			for(SiacROrdinativoStato sros : siacTOrdinativo.getSiacROrdinativoStatos()) {
				if(sros.getDataCancellazione() == null) {
					subOrdinativo.setStatoOperativoOrdinativo(SiacDOrdinativoStatoEnum.byCodice(sros.getSiacDOrdinativoStato().getOrdinativoStatoCode()).getStatoOperativo());
				}
			}
		}
	}
	
	private boolean isOrdinativoAnnullato(SiacTOrdinativo siacTOrdinativo){
		if(siacTOrdinativo.getSiacROrdinativoStatos() != null) {
			for(SiacROrdinativoStato sros : siacTOrdinativo.getSiacROrdinativoStatos()) {
				if(StatoOperativoOrdinativo.ANNULLATO.equals(SiacDOrdinativoStatoEnum.byCodice(sros.getSiacDOrdinativoStato().getOrdinativoStatoCode()).getStatoOperativo())) {
					return true;
				}
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacRDocOnere convertTo(DettaglioOnere dettaglioOnere, SiacRDocOnere dest) {	
		

		return dest;	
		
	}
	
	
	



	

}
