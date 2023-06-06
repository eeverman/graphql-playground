CREATE SCHEMA IF NOT EXISTS wqx_dump;

CREATE TABLE if not exists wqx_dump."MONITORING_LOCATION"
(
    "MLOC_UID"                       numeric(20)     NULL,
    "CNTY_UID"                       int4            NULL,
    "CNTRY_UID"                      int4            NULL,
    "HCMTH_UID"                      int4            NULL,
    "HRDAT_UID"                      int4            NULL,
    "MLTYP_UID"                      int4            NULL,
    "MSUNT_UID_VERTICAL_MEASURE"     int4            NULL,
    "ORG_UID"                        numeric(20)     NULL,
    "ST_UID"                         int4            NULL,
    "VCMTH_UID"                      int4            NULL,
    "VRDAT_UID"                      int4            NULL,
    "MLOC_ID"                        varchar(55)     NULL,
    "MLOC_NAME"                      varchar(255)    NULL,
    "MLOC_DESC"                      varchar(4000)   NULL,
    "MLOC_HUC_8"                     varchar(8)      NULL,
    "MLOC_HUC_12"                    varchar(12)     NULL,
    "MLOC_LATITUDE"                  numeric(38, 10) NULL,
    "MLOC_LONGITUDE"                 numeric(38, 10) NULL,
    "MLOC_TRIBAL_LAND_YN"            varchar(1)      NULL,
    "MLOC_TRIBAL_LAND_NAME"          varchar(512)    NULL,
    "MLOC_VERTICAL_MEASURE"          varchar(60)     NULL,
    "MSUNT_UID_HORIZONTAL_ACCURACY"  int4            NULL,
    "MLOC_HORIZONTAL_ACCURACY"       varchar(60)     NULL,
    "MLOC_LAST_CHANGE_DATE"          timestamp       NULL,
    "USR_UID_LAST_CHANGE"            int4            NULL,
    "MLOC_TRANS_ID"                  varchar(100)    NULL,
    "MLOC_DRAINAGE_AREA"             varchar(60)     NULL,
    "MSUNT_UID_DRAINAGE_AREA"        int4            NULL,
    "MLOC_CONTRIB_DRAINAGE_AREA"     varchar(60)     NULL,
    "MSUNT_UID_CONTRB_DRAINAGE_AREA" int4            NULL,
    "MLOC_VERTICAL_ACCURACY"         varchar(60)     NULL,
    "MSUNT_UID_VERTICAL_ACCURACY"    int4            NULL,
    "MLOC_SOURCE_MAP_SCALE"          varchar(60)     NULL
);

insert into wqx_dump."MONITORING_LOCATION"("MLOC_UID", "MLOC_ID", "MLOC_NAME", "MLOC_TRIBAL_LAND_YN", "MLOC_TRIBAL_LAND_NAME")
values (1, 'NARS_WQX-OWW04440-0455', 'Nice place', 'Y', 'Tribal name 1');

insert into wqx_dump."MONITORING_LOCATION"("MLOC_UID", "MLOC_ID", "MLOC_NAME", "MLOC_TRIBAL_LAND_YN", "MLOC_TRIBAL_LAND_NAME")
values (2, 'MNPCA-S003-680', 'Far from home', 'N', '');

insert into wqx_dump."MONITORING_LOCATION"("MLOC_UID", "MLOC_ID", "MLOC_NAME", "MLOC_TRIBAL_LAND_YN", "MLOC_TRIBAL_LAND_NAME")
values (3, 'NARS-OWW04440-0455', 'Previous place', 'Y', 'Tribal name 1 before times');
