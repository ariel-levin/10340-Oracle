CREATE table "WAREHOUSES" (
    "WH_NUM"     NUMBER NOT NULL,
    "WH_NAME"    VARCHAR2(64) NOT NULL,
    "WH_STREET"  VARCHAR2(64),
    "WH_CITY"    VARCHAR2(32),
    "WH_PHONE"   VARCHAR2(32),
    constraint  "WAREHOUSES_PK" primary key ("WH_NUM")
)
