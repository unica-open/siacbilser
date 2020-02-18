select a.account_code,  c.cassaecon_code, c.cassaecon_desc
from siac_t_account a, siac_r_account_cassa_econ b, siac_t_cassa_econ c
where
a.account_id = 49 and
a.account_id = b.account_id and
b.cassaecon_id = c.cassaecon_id and
a.ente_proprietario_id = 3 and
b.ente_proprietario_id = 3 and
c.ente_proprietario_id = 3


select *
from siac_t_account a
where a.account_id = 1796 --cmto prod
