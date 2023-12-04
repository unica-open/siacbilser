/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.soggetto;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siacfinser.integration.entity.SiacTModpagFin;

@Component
@Transactional
public class ModalitaPagamentoDaoImpl extends JpaDao<SiacTModpagFin, Integer> implements ModalitaPagamentoDao
{
	@Override
	public Integer findAccreditoTipoIdByGestioneLivello(Integer idEnte, String codiceGestioneLivello)
	{
		Query q = entityManager.createNativeQuery(
				"SELECT at.accredito_tipo_id FROM "
				+ " siac_d_gestione_livello gl, "
				+ " siac_d_accredito_tipo at, "
				+ " siac_r_gestione_ente ge "
				+ " WHERE gl.gestione_livello_code=:gestione_livello_code "
				+ " AND gl.gestione_livello_desc=at.accredito_tipo_code "
				+ " AND gl.ente_proprietario_id=at.ente_proprietario_id "
				+ " AND gl.ente_proprietario_id=ge.ente_proprietario_id "
				+ " AND gl.ente_proprietario_id=:ente_proprietario_id "
				+ " AND gl.gestione_livello_id=ge.gestione_livello_id "
				+ " AND gl.data_cancellazione IS NULL "
				+ " AND gl.validita_inizio <= now() "
				+ " AND (gl.validita_fine IS NULL OR gl.validita_fine > now())"
				+ " AND ge.data_cancellazione IS NULL "
				+ " AND ge.validita_inizio <= now() "
				+ " AND (ge.validita_fine IS NULL OR ge.validita_fine > now())"
				);

		q.setParameter("gestione_livello_code", codiceGestioneLivello);
		q.setParameter("ente_proprietario_id", idEnte);

		List<Integer> l = q.getResultList();

		if (l.isEmpty())
			throw new IllegalStateException(String.format("Gestione livello %s non definito per l'ente %d",
					codiceGestioneLivello, idEnte));
		
		return l.get(0);
	}
	
	@Override
	public Integer findModalitaPagamentoContoCorrente(Integer idSoggetto, String iban, Integer idEnte)
	{
		Query q = entityManager.createNativeQuery(
				"SELECT modpag_id FROM siac_t_modpag "
				+ " WHERE soggetto_id=:soggetto_id "
				+ " AND iban=:iban "
				+ " AND ente_proprietario_id=:ente_proprietario_id "
				+ " AND data_cancellazione IS NULL "
				+ " AND validita_inizio <= now() "
				+ " AND (validita_fine IS NULL OR validita_fine > now())");

		q.setParameter("soggetto_id", idSoggetto);
		q.setParameter("iban", iban);
		q.setParameter("ente_proprietario_id", idEnte);

		List<Integer> l = q.getResultList();

		return l.isEmpty() ? null : l.get(0);
	}
	
	@Override
	public Integer findModalitaPagamentoGiroFondi(Integer idSoggetto, String numeroConto, Integer idEnte, Integer accreditoTipoId)
	{
		Query q = entityManager.createNativeQuery(
				"SELECT modpag_id FROM siac_t_modpag "
				+ " WHERE soggetto_id=:soggetto_id "
				+ " AND accredito_tipo_id=:accredito_tipo_id "
				+ " AND contocorrente=:numeroConto"
				+ " AND ente_proprietario_id=:ente_proprietario_id "
				+ " AND data_cancellazione IS NULL "
				+ " AND validita_inizio <= now() "
				+ " AND (validita_fine IS NULL OR validita_fine > now())");

		q.setParameter("soggetto_id", idSoggetto);
		q.setParameter("accredito_tipo_id", accreditoTipoId);
		q.setParameter("numeroConto", numeroConto);
		q.setParameter("ente_proprietario_id", idEnte);

		List<Integer> l = q.getResultList();

		return l.isEmpty() ? null : l.get(0);
	}
	
	@Override
	public Integer findModalitaPagamentoByAccreditoTipoId(Integer idSoggetto, Integer idEnte, Integer accreditoTipoId)
	{
		Query q = entityManager.createNativeQuery(
				"SELECT modpag_id FROM siac_t_modpag "
				+ " WHERE soggetto_id=:soggetto_id "
				+ " AND accredito_tipo_id=:accredito_tipo_id "
				+ " AND ente_proprietario_id=:ente_proprietario_id "
				+ " AND data_cancellazione IS NULL "
				+ " AND validita_inizio <= now() "
				+ " AND (validita_fine IS NULL OR validita_fine > now())");

		q.setParameter("soggetto_id", idSoggetto);
		q.setParameter("accredito_tipo_id", accreditoTipoId);
		q.setParameter("ente_proprietario_id", idEnte);

		List<Integer> l = q.getResultList();

		return l.isEmpty() ? null : l.get(0);
	}
	
	@Override
	public Integer insertModalitaPagamentoContoCorrente(Integer idSoggetto, String iban, Integer idEnte, String login)
	{
		Integer idTipoAccredito = findAccreditoTipoIdByGestioneLivello(idEnte, TipologiaGestioneLivelli.ACCREDITO_CONTO_BANCA.getCodice());
		inserisciModpag(idSoggetto, idTipoAccredito, iban, null, null, null, idEnte, login);
		inserisciModpagOrdine(idSoggetto, idEnte, login);
		inserisciModpagStato(idEnte, login);
		
		return getLastInsertIdModalitaPagamento();
	}

	@Override
	public Integer insertModalitaPagamentoGiroFondi(Integer idSoggetto, String numeroConto, Integer idEnte, String login)
	{
		Integer idTipoAccredito = findAccreditoTipoIdByGestioneLivello(idEnte, TipologiaGestioneLivelli.ACCREDITO_GIRO_FONDI.getCodice());
		inserisciModpag(idSoggetto, idTipoAccredito, null, numeroConto, null, null, idEnte, login);
		inserisciModpagOrdine(idSoggetto, idEnte, login);
		inserisciModpagStato(idEnte, login);
		
		return getLastInsertIdModalitaPagamento();
	}

	@Override
	public Integer insertModalitaPagamentoContanti(Integer idSoggetto, String quietanzante,
			String codiceFiscaleQuietanzante, Integer idEnte, String login)
	{
		Integer idTipoAccredito = findAccreditoTipoIdByGestioneLivello(idEnte, TipologiaGestioneLivelli.ACCREDITO_CONTANTI.getCodice());
		inserisciModpag(idSoggetto, idTipoAccredito, null, null, quietanzante, codiceFiscaleQuietanzante, idEnte, login);
		inserisciModpagOrdine(idSoggetto, idEnte, login);
		inserisciModpagStato(idEnte, login);
		
		return getLastInsertIdModalitaPagamento();
	}

	private int getLastInsertIdModalitaPagamento() {
		return ((Number) entityManager.createNativeQuery("SELECT CURRVAL('siac_t_modpag_modpag_id_seq')").getSingleResult()).intValue();
	}

	private void inserisciModpagStato(Integer idEnte, String login)
	{
		Query q = entityManager.createNativeQuery(
				"INSERT INTO siac_r_modpag_stato "
				+ " ( "
				+ "    modpag_id, "
				+ "    modpag_stato_id, "
				+ "    validita_inizio, "
				+ "    ente_proprietario_id, "
				+ "    login_operazione "
				+ " ) "
				+ " VALUES "
				+ " ( "
				+ "   (SELECT CURRVAL('siac_t_modpag_modpag_id_seq')), "
				+ "   (SELECT modpag_stato_id FROM siac_d_modpag_stato "
				+ "       WHERE modpag_stato_code='VALIDO' "
				+ "       AND ente_proprietario_id=:ente_proprietario_id "
				+ "       AND data_cancellazione IS NULL "
				+ "       AND validita_inizio <= now() "
				+ "       AND (validita_fine IS NULL OR validita_fine > now())), "
				+ "    now(), "
				+ "    :ente_proprietario_id, "
				+ "    :login "
				+ " )"
			);

		q.setParameter("ente_proprietario_id", idEnte);
		q.setParameter("login", login);

		q.executeUpdate();
	}

	private void inserisciModpagOrdine(Integer idSoggetto, Integer idEnte, String login)
	{
		Query q = entityManager.createNativeQuery(
				"INSERT INTO siac_r_modpag_ordine "
				+ " ( "
				+ "    soggetto_id, "
				+ "    modpag_id, "
				+ "    ordine, "
				+ "    validita_inizio, "
				+ "    ente_proprietario_id, "
				+ "    login_operazione, "
				+ "    login_creazione "
				+ "  ) "
				+ " VALUES "
				+ " ( "
				+ "     :soggetto_id, "
				+ "    (SELECT CURRVAL('siac_t_modpag_modpag_id_seq')), "
				+ "    (SELECT COALESCE(MAX(ordine), 0)+1 FROM siac_r_modpag_ordine "
				+ "    	WHERE soggetto_id=:soggetto_id "
				+ "        AND data_cancellazione IS NULL "
				+ "        AND validita_inizio <= now() "
				+ "        AND (validita_fine IS NULL OR validita_fine > now())), "
				+ "	now(), "
				+ "    :ente_proprietario_id, "
				+ "    :login, "
				+ "    :login "
				+ " )"
			);

		q.setParameter("soggetto_id", idSoggetto);
		q.setParameter("ente_proprietario_id", idEnte);
		q.setParameter("login", login);

		q.executeUpdate();
	}

	private void inserisciModpag(Integer idSoggetto, Integer idTipoAccredito, String iban, String contoCorrente,
			String quietanzante, String codiceFiscaleQuietanzante, Integer idEnte, String login)
	{
		Query q = entityManager.createNativeQuery(
				"INSERT INTO siac_t_modpag "
				+ " ( "
				+ "    soggetto_id, "
				+ "    accredito_tipo_id, "
				+ "    iban, "
				+ "    contocorrente, "
				+ "    quietanziante, "
				+ "    quietanziante_codice_fiscale, "
				+ "    validita_inizio, "
				+ "    ente_proprietario_id, "
				+ "    login_operazione, "
				+ "    login_creazione "
				+ " ) "
				+ " VALUES "
				+ " ( "
				+ "    :soggetto_id, "
				+ "    :accredito_tipo_id, "
				+ "    :iban, "
				+ "    :conto_corrente, "
				+ "    :quietanziante, "
				+ "    :quietanziante_codice_fiscale, "
				+ "    now(), "
				+ "    :ente_proprietario_id, "
				+ "    :login, "
				+ "    :login "
				+ " )"
			);

		q.setParameter("soggetto_id", idSoggetto);
		q.setParameter("accredito_tipo_id", idTipoAccredito);
		q.setParameter("iban", iban);
		q.setParameter("conto_corrente", contoCorrente);
		q.setParameter("quietanziante", quietanzante);
		q.setParameter("quietanziante_codice_fiscale", codiceFiscaleQuietanzante);
		q.setParameter("ente_proprietario_id", idEnte);
		q.setParameter("login", login);

		q.executeUpdate();
	}

}