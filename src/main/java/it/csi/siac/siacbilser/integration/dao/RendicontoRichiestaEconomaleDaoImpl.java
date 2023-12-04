/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Date;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRGiustificativoMovgest;
import it.csi.siac.siacbilser.integration.entity.SiacRGiustificativoStato;
import it.csi.siac.siacbilser.integration.entity.SiacRMovimentoModpag;
import it.csi.siac.siacbilser.integration.entity.SiacTGiustificativo;
import it.csi.siac.siacbilser.integration.entity.SiacTGiustificativoDet;
import it.csi.siac.siacbilser.integration.entity.SiacTMovimento;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class CassaEconomaleDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RendicontoRichiestaEconomaleDaoImpl extends JpaDao<SiacTGiustificativo, Integer> implements RendicontoRichiestaEconomaleDao {
	
	public SiacTGiustificativo create(SiacTGiustificativo g){
		Date now = new Date();
		g.setDataModificaInserimento(now);
		
		if(g.getSiacTGiustificativoDets() != null){
			for(SiacTGiustificativoDet gd: g.getSiacTGiustificativoDets()){
				gd.setDataModificaInserimento(now);
				gd.setUid(null);		
				
				if(gd.getSiacRGiustificativoStatos()!=null){
					for(SiacRGiustificativoStato rgs : gd.getSiacRGiustificativoStatos()){
						rgs.setDataModificaInserimento(now);
					}
				}
				
			}
			
		}
		
		if(g.getSiacTMovimentos() != null){
			for(SiacTMovimento d: g.getSiacTMovimentos()){
				d.setDataModificaInserimento(now);
				d.setUid(null);
				
				if(d.getSiacRMovimentoModpags()!=null){
					for(SiacRMovimentoModpag rMod : d.getSiacRMovimentoModpags()){
						rMod.setDataModificaInserimento(now);
					}
				}
			}
			
		}
		
		if(g.getSiacRGiustificativoMovgests() != null){
			for(SiacRGiustificativoMovgest gm : g.getSiacRGiustificativoMovgests()){
				gm.setDataModificaInserimento(now);
			}
		}
		
		g.setUid(null);		
		super.save(g);
		return g;
	}
	

	public SiacTGiustificativo update(SiacTGiustificativo g){
		
		SiacTGiustificativo gAttuale = this.findById(g.getUid());
		
		Date now = new Date();
		g.setDataModificaAggiornamento(now);	

		
		//cancellazione elementi collegati	
		if(gAttuale.getSiacTGiustificativoDets() != null){
			for(SiacTGiustificativoDet gd: gAttuale.getSiacTGiustificativoDets()){
				gd.setDataCancellazioneIfNotSet(now);
				
				if(gd.getSiacRGiustificativoStatos()!=null){
					for(SiacRGiustificativoStato rgs : gd.getSiacRGiustificativoStatos()){
						rgs.setDataCancellazioneIfNotSet(now);
					}
				}
				
			}
			
		}
		
		if(gAttuale.getSiacTMovimentos() != null){
			for(SiacTMovimento d: gAttuale.getSiacTMovimentos()){
				d.setDataCancellazioneIfNotSet(now);
				
				if(d.getSiacRMovimentoModpags()!=null){
					for(SiacRMovimentoModpag rMod : d.getSiacRMovimentoModpags()){
						rMod.setDataCancellazioneIfNotSet(now);
					}
				}
			}

			
		}
		
		if(gAttuale.getSiacRGiustificativoMovgests() != null){
			for(SiacRGiustificativoMovgest gm : gAttuale.getSiacRGiustificativoMovgests()){
				gm.setDataCancellazioneIfNotSet(now);
			}
		}
		
		entityManager.flush();
		
		//inserisco nuovi elementi
		if(g.getSiacTGiustificativoDets() != null){
			for(SiacTGiustificativoDet gd: g.getSiacTGiustificativoDets()){
				gd.setDataModificaInserimento(now);
				gd.setUid(null);
				
				if(gd.getSiacRGiustificativoStatos()!=null){
					for(SiacRGiustificativoStato rgs : gd.getSiacRGiustificativoStatos()){
						rgs.setDataModificaInserimento(now);
					}
				}
				
			}
			
		}
		
		if(g.getSiacTMovimentos() != null){
			for(SiacTMovimento d: g.getSiacTMovimentos()){
				d.setDataModificaInserimento(now);
				d.setUid(null);
				
				if(d.getSiacRMovimentoModpags()!=null){
					for(SiacRMovimentoModpag rMod : d.getSiacRMovimentoModpags()){
						rMod.setDataModificaInserimento(now);
					}
				}
			}
			
		}
		
		if(g.getSiacRGiustificativoMovgests() != null){
			for(SiacRGiustificativoMovgest gm : g.getSiacRGiustificativoMovgests()){
				gm.setDataModificaInserimento(now);
			}
		}
		
		super.update(g);
		return g;
	}
	
	
	

}
