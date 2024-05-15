CREATE TABLE usuario (
                     id               SERIAL PRIMARY KEY,
                     name             TEXT     NOT NULL UNIQUE,
                     email            TEXT     NOT NULL UNIQUE,
                     password         TEXT,
                     secret           CHAR(200),
                     resetpass_token  CHAR(30) UNIQUE,
                     resetpass_limit  TIMESTAMP,
                     resetpass_force  BOOLEAN  NOT NULL,
                     verify_token     CHAR(30) UNIQUE,
                     verify_limit     TIMESTAMP,
                     verified         BOOLEAN  NOT NULL,
                     bloqueado        BOOLEAN  NOT NULL
                     );
CREATE INDEX usuario_email   ON usuario ( UPPER(email) );
CREATE INDEX usuario_name    ON usuario ( UPPER(name) );

COMMENT ON TABLE  usuario                  IS 'Cadastro de usuarios';
COMMENT ON COLUMN usuario.id               IS 'Codigo do usuario';
COMMENT ON COLUMN usuario.name             IS 'Nome do usuario';
COMMENT ON COLUMN usuario.email            IS 'Email do usuario';
COMMENT ON COLUMN usuario.password         IS 'Password do usuario';
COMMENT ON COLUMN usuario.secret           IS 'Chave para gerar token JWT';
COMMENT ON COLUMN usuario.resetpass_token  IS 'Token para resetar password';
COMMENT ON COLUMN usuario.resetpass_limit  IS 'Limite da validade do token para resetar password';
COMMENT ON COLUMN usuario.resetpass_force  IS 'Indica se e obrigatorio resetar a password';
COMMENT ON COLUMN usuario.verify_token     IS 'Token para verificacao do email';
COMMENT ON COLUMN usuario.verify_limit     IS 'Limite da validade do token para verificacao do email';
COMMENT ON COLUMN usuario.verified         IS 'Indica se o email do usuario ja foi verificado';
COMMENT ON COLUMN usuario.bloqueado        IS 'Indica que o acesso do usuario esta bloqueado';
