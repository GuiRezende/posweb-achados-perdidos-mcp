CREATE SCHEMA IF NOT EXISTS objetos_service
AUTHORIZATION admin;

CREATE SCHEMA IF NOT EXISTS ocorrencias_service
AUTHORIZATION admin;

CREATE SCHEMA IF NOT EXISTS reivindicacoes_service
AUTHORIZATION admin;

GRANT USAGE, CREATE
ON SCHEMA objetos_service
TO admin;

GRANT USAGE, CREATE
ON SCHEMA ocorrencias_service
TO admin;

GRANT USAGE, CREATE
ON SCHEMA reivindicacoes_service
TO admin;