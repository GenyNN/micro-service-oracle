EXISTS
  (SELECT cp.client_id
  FROM seaware.CLIENT_PROGRAM cp
  WHERE TRUNC(sysdate) BETWEEN cp.DATE_FROM AND cp.DATE_TO
  AND t.client_id    =cp.client_id
  AND cp.club_account=:club_one)