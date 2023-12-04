/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.entitymapping;

import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassa;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaProvvisorio;
import it.csi.siac.siacintegser.model.enumeration.TipoProvvisorioDiCassa;




public class ProvvisoriDiCassaIntegToFinConverter extends AbstractIntegToFinConverter {
	
	
	/**
	 * converte i campi che non sono stati convertiti dal dozer
	 * @param requestInteg
	 * @param requestFin
	 * @return
	 */
	public static it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassa requestIntegToRequestFin(
			it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.provvisori.RicercaProvvisoriDiCassa requestInteg,
			it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassa requestFin ) {
		
		if(requestInteg!=null){
			
			
			if(requestFin==null){
				requestFin = new RicercaProvvisoriDiCassa();
			}
			ParametroRicercaProvvisorio parametroRicercaFin = requestFin.getParametroRicercaProvvisorio();
			if(parametroRicercaFin==null){
				parametroRicercaFin = new ParametroRicercaProvvisorio();
			}
			
			
			//DA REGOLARIZZARE:
			parametroRicercaFin.setFlagDaRegolarizzare(siNoToStringTrueFalse(requestInteg.getDaRegolarizzare()));
			
			//ANNULLATO:
			parametroRicercaFin.setFlagAnnullato(siNoIndifferenteToStringTrueFalse(requestInteg.getAnnullato()));
			
			//TIPO PROVVISORIO:
			parametroRicercaFin.setTipoProvvisorio(tipoProvvisorioIntegToTipoProvvisorioFin(requestInteg.getTipoProvvisorioDiCassa()));
			
		}
		return requestFin;
	}
	
	public static it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa.TipoProvvisorioDiCassa tipoProvvisorioIntegToTipoProvvisorioFin(TipoProvvisorioDiCassa tipoInteg){
		it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa.TipoProvvisorioDiCassa tipoFin = null;
		if(tipoInteg!=null){
			if(tipoInteg.equals(TipoProvvisorioDiCassa.ENTRATA)){
				tipoFin = it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa.TipoProvvisorioDiCassa.E;
			} else if(tipoInteg.equals(TipoProvvisorioDiCassa.SPESA)){
				tipoFin = it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa.TipoProvvisorioDiCassa.S;
			}
		} else {
			tipoFin = null;
		}
		return tipoFin;
	}

}
