t.household_id IN
  (SELECT hp.household_id
  FROM seaware.household_phone hp
  WHERE
  (hp.phone_number=:phone_number))
  AND (
  (Substr(seaware.MST_Common.ClientIDMainPhone(t.CLIENT_ID), 1, Instr(seaware.MST_Common.ClientIDMainPhone(t.CLIENT_ID), '-') - 1) = :country_code AND Substr(seaware.MST_Common.ClientIDMainPhone(t.CLIENT_ID), Instr(seaware.MST_Common.ClientIDMainPhone(t.CLIENT_ID), '-') + 1) = :phone_number)
  OR(NVL(:country_code,0)=0 AND Substr(seaware.MST_Common.ClientIDMainPhone(t.CLIENT_ID), Instr(seaware.MST_Common.ClientIDMainPhone(t.CLIENT_ID), '-') + 1) = :phone_number))