/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.oil;

import java.util.List;
import java.util.Map;

import it.csi.siac.siacfin2ser.model.TipoOrdinativo;

public interface OrdinativoMifDao
{
	public TipoOrdinativo getCodiceTipo(int idElaborazione);

	public List<Integer> getAnniEsercizioOrdinativiEntrata(int idElaborazione);
	
	public List<Integer> getAnniEsercizioOrdinativiSpesa(int idElaborazione);

	public List<String> getCodiciIstatAnnoEsercizioOrdinativiEntrata(int idElaborazione, Integer anno);
	
	public List<String> getCodiciIstatAnnoEsercizioOrdinativiSpesa(int idElaborazione, Integer anno);

	public int countOrdinativiEntrata(int idElaborazione);

	public int countOrdinativiSpesa(int idElaborazione);

	@Deprecated public int countOrdinativiAnnoEntrata(int idElaborazione, Integer anno);

	@Deprecated public int countOrdinativiAnnoSpesa(int idElaborazione, Integer anno);

	public int countOrdinativiAnnoCodiceIstatEntrata(int idElaborazione, Integer anno, String codiceIstat);

	public int countOrdinativiAnnoCodiceIstatSpesa(int idElaborazione, Integer anno, String codiceIstat);

	public List<Map<String, Object>> leggiStrutturaXml(int idElaborazione);

	public List<Map<String, Object>> queryForList(String sql, Map<String, Object> params);
}
