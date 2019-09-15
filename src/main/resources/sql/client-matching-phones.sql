t.household_id IN
  (SELECT hp.household_id
  FROM seaware.household_phone hp
  WHERE hp.phone_number IN (:phones))
AND t.FIRST_NAME = :first_name AND t.LAST_NAME = :last_name