SELECT
  t.FULL_NAME,
  t.FIRST_NAME,
  t.LAST_NAME,
  t.CLIENT_ID ,
  TO_CHAR(t.BIRTHDAY, 'DD.MM.YYYY') BIRTHDAY,
  t.email,
  SUBSTR( seaware.MST_Common.ClientIDMainPhone(t.CLIENT_ID),1,255) PHONE_NUMBER,
  SUBSTR( a.ADDRESS_LINE1
  ||a.ADDRESS_LINE2
  || a.ADDRESS_LINE3
  ||a.ADDRESS_LINE4,1,100) ADDRESS,
  DECODE(NVL(a.ZIP,''),'','',a.ZIP,a.ZIP
  ||',')
  ||a.ADDRESS_CITY ZIP_CITY,
  (SELECT ca.account_no
  FROM seaware.PTS_CLUB_ACCOUNT ca,
    seaware.CLIENT_PROGRAM cp
  WHERE cp.client_id=t.client_id
  AND sysdate BETWEEN cp.DATE_FROM AND cp.DATE_TO
  AND ca.account_no = cp.club_account
  AND ca.is_active  ='Y'
  AND rownum        =1
  ) CLUB_ACCOUNT,
  (SELECT cp.FREQUENT_PGM_CODE
  FROM seaware.PTS_CLUB_ACCOUNT ca,
    seaware.CLIENT_PROGRAM cp
  WHERE cp.client_id = t.client_id
  AND sysdate BETWEEN cp.DATE_FROM AND cp.DATE_TO
  AND ca.account_no = cp.club_account
  AND ca.is_active  ='Y'
  AND rownum        =1
  ) CLUB_ACCOUNT_TYPE
FROM seaware.Client t
LEFT OUTER JOIN seaware.HOUSEHOLD_ADDRESS a
ON a.household_addr_id = t.household_addr_id
WHERE (t.IS_ACTIVE     ='Y'
AND t.IS_DECEASED      ='N'
AND ($(specific.clause))
)
