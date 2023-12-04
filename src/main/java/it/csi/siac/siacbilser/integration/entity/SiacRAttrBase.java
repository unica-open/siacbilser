/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;
import java.util.Date;

import it.csi.siac.siaccommonser.integration.entity.SiacTBase;

/**
 * Interfaccia base condivisa dalle r sugli attributi
 * @author Alessandro Marchino
 *
 */
public interface SiacRAttrBase<T extends SiacTBase> {

	public String getBoolean_();
	public void setBoolean_(String boolean_);
	
	public BigDecimal getNumerico();
	public void setNumerico(BigDecimal numerico);
	
	public BigDecimal getPercentuale();
	public void setPercentuale(BigDecimal percentuale);
	
	public Integer getTabellaId();
	public void setTabellaId(Integer tabellaId);
	
	public String getTesto();
	public void setTesto(String testo);
	
	public SiacTAttr getSiacTAttr();
	public void setSiacTAttr(SiacTAttr siacTAttr);
	
	public SiacTEnteProprietario getSiacTEnteProprietario();
	public void setSiacTEnteProprietario(SiacTEnteProprietario siacTEnteProprietario);
	
	public Date getDataCancellazione();
	public void setLoginOperazione(String loginOperazione);
	
	public void setEntity(T entity);
}
