
DELETE FROM consultas;
DELETE FROM financeiro;


DELETE FROM dentista_especialidade;


DELETE FROM usuarios WHERE email IN (
                                     'admin@clinica.com', 'admin@risum.com', 'rodrigo@risum.com', 'amanda@risum.com',
                                     'bruno@risum.com', 'camila@risum.com', 'diego@risum.com',
                                     'elena@risum.com', 'fabio@risum.com', 'gisele@risum.com',
                                     'heitor@risum.com', 'isabela@risum.com'
    );

DELETE FROM dentistas WHERE email IN (
                                      'rodrigo@risum.com', 'amanda@risum.com', 'bruno@risum.com',
                                      'camila@risum.com', 'diego@risum.com', 'elena@risum.com',
                                      'fabio@risum.com', 'gisele@risum.com', 'heitor@risum.com',
                                      'isabela@risum.com'
    );


DELETE FROM usuarios WHERE email IN (
                                     'admin@clinica.com', 'admin@risum.com', 'rodrigo@risum.com', 'amanda@risum.com',
                                     'bruno@risum.com', 'camila@risum.com', 'diego@risum.com',
                                     'elena@risum.com', 'fabio@risum.com', 'gisele@risum.com',
                                     'heitor@risum.com', 'isabela@risum.com'
    );

DELETE FROM dentistas WHERE email IN (
                                      'rodrigo@risum.com', 'amanda@risum.com', 'bruno@risum.com',
                                      'camila@risum.com', 'diego@risum.com', 'elena@risum.com',
                                      'fabio@risum.com', 'gisele@risum.com', 'heitor@risum.com',
                                      'isabela@risum.com'
    );


-- CADASTRO DE ESPECIALIDADES

INSERT INTO especialidades (nome) VALUES ('Ortodontia') ON CONFLICT (nome) DO NOTHING;
INSERT INTO especialidades (nome) VALUES ('Endodontia') ON CONFLICT (nome) DO NOTHING;
INSERT INTO especialidades (nome) VALUES ('Implantodontia') ON CONFLICT (nome) DO NOTHING;


--  CADASTRO DE USUÁRIOS (Tabela 'usuarios' para Login)

-- Administrador Geral
INSERT INTO usuarios (nome, email, senha, perfil, ativo, cpf)
VALUES ('Administrador', 'admin@risum.com', '$2a$10$Y50UaMFOxteibQEYfDj67uL5W9M1C.D7CInMv9p8Piz70o7D4GZpG', 'ADMIN', true, '000.000.000-00');

-- Usuários dos 10 Dentistas (Senha: admin123)
INSERT INTO usuarios (nome, email, senha, perfil, ativo, cpf) VALUES
                                                                  ('Dr. Rodrigo Mendes', 'rodrigo@risum.com', '$2a$10$Y50UaMFOxteibQEYfDj67uL5W9M1C.D7CInMv9p8Piz70o7D4GZpG', 'DENTISTA', true, '111.111.111-11'),
                                                                  ('Dra. Amanda Silva', 'amanda@risum.com', '$2a$10$Y50UaMFOxteibQEYfDj67uL5W9M1C.D7CInMv9p8Piz70o7D4GZpG', 'DENTISTA', true, '222.222.222-22'),
                                                                  ('Dr. Bruno Costa', 'bruno@risum.com', '$2a$10$Y50UaMFOxteibQEYfDj67uL5W9M1C.D7CInMv9p8Piz70o7D4GZpG', 'DENTISTA', true, '333.333.333-33'),
                                                                  ('Dra. Camila Oliveira', 'camila@risum.com', '$2a$10$Y50UaMFOxteibQEYfDj67uL5W9M1C.D7CInMv9p8Piz70o7D4GZpG', 'DENTISTA', true, '444.444.444-44'),
                                                                  ('Dr. Diego Santos', 'diego@risum.com', '$2a$10$Y50UaMFOxteibQEYfDj67uL5W9M1C.D7CInMv9p8Piz70o7D4GZpG', 'DENTISTA', true, '555.555.555-55'),
                                                                  ('Dra. Elena Ribeiro', 'elena@risum.com', '$2a$10$Y50UaMFOxteibQEYfDj67uL5W9M1C.D7CInMv9p8Piz70o7D4GZpG', 'DENTISTA', true, '666.666.666-66'),
                                                                  ('Dr. Fábio Lima', 'fabio@risum.com', '$2a$10$Y50UaMFOxteibQEYfDj67uL5W9M1C.D7CInMv9p8Piz70o7D4GZpG', 'DENTISTA', true, '777.777.777-77'),
                                                                  ('Dra. Gisele Almeida', 'gisele@risum.com', '$2a$10$Y50UaMFOxteibQEYfDj67uL5W9M1C.D7CInMv9p8Piz70o7D4GZpG', 'DENTISTA', true, '888.888.888-88'),
                                                                  ('Dr. Heitor Souza', 'heitor@risum.com', '$2a$10$Y50UaMFOxteibQEYfDj67uL5W9M1C.D7CInMv9p8Piz70o7D4GZpG', 'DENTISTA', true, '999.999.999-99'),
                                                                  ('Dra. Isabela Rocha', 'isabela@risum.com', '$2a$10$Y50UaMFOxteibQEYfDj67uL5W9M1C.D7CInMv9p8Piz70o7D4GZpG', 'DENTISTA', true, '123.123.123-12');

-- CADASTRO DOS DENTISTAS (Tabela 'dentistas')

INSERT INTO dentistas (nome, email, cpf, cro, ativo, em_ferias) VALUES
                                                                    ('Dr. Rodrigo Mendes', 'rodrigo@risum.com', '111.111.111-11', 'CRO-PR 12345', true, false),
                                                                    ('Dra. Amanda Silva', 'amanda@risum.com', '222.222.222-22', 'CRO-PR 67890', true, false),
                                                                    ('Dr. Bruno Costa', 'bruno@risum.com', '333.333.333-33', 'CRO-PR 54321', true, false),
                                                                    ('Dra. Camila Oliveira', 'camila@risum.com', '444.444.444-44', 'CRO-PR 11223', true, false),
                                                                    ('Dr. Diego Santos', 'diego@risum.com', '555.555.555-55', 'CRO-PR 44556', true, false),
                                                                    ('Dra. Elena Ribeiro', 'elena@risum.com', '666.666.666-66', 'CRO-PR 77889', true, false),
                                                                    ('Dr. Fábio Lima', 'fabio@risum.com', '777.777.777-77', 'CRO-PR 99001', true, false),
                                                                    ('Dra. Gisele Almeida', 'gisele@risum.com', '888.888.888-88', 'CRO-PR 22334', true, false),
                                                                    ('Dr. Heitor Souza', 'heitor@risum.com', '999.999.999-99', 'CRO-PR 55667', true, false),
                                                                    ('Dra. Isabela Rocha', 'isabela@risum.com', '123.123.123-12', 'CRO-PR 88990', true, false);


-- VÍNCULO DE DENTISTAS E ESPECIALIDADES (Tabela Intermediária)

INSERT INTO dentista_especialidade (id_dentista, id_especialidade) VALUES
                                                                       ((SELECT id FROM dentistas WHERE email = 'rodrigo@risum.com'), (SELECT id FROM especialidades WHERE nome = 'Ortodontia')),
                                                                       ((SELECT id FROM dentistas WHERE email = 'rodrigo@risum.com'), (SELECT id FROM especialidades WHERE nome = 'Implantodontia')),
                                                                       ((SELECT id FROM dentistas WHERE email = 'amanda@risum.com'), (SELECT id FROM especialidades WHERE nome = 'Endodontia')),
                                                                       ((SELECT id FROM dentistas WHERE email = 'bruno@risum.com'), (SELECT id FROM especialidades WHERE nome = 'Implantodontia')),
                                                                       ((SELECT id FROM dentistas WHERE email = 'camila@risum.com'), (SELECT id FROM especialidades WHERE nome = 'Ortodontia')),
                                                                       ((SELECT id FROM dentistas WHERE email = 'diego@risum.com'), (SELECT id FROM especialidades WHERE nome = 'Endodontia')),
                                                                       ((SELECT id FROM dentistas WHERE email = 'elena@risum.com'), (SELECT id FROM especialidades WHERE nome = 'Ortodontia')),
                                                                       ((SELECT id FROM dentistas WHERE email = 'elena@risum.com'), (SELECT id FROM especialidades WHERE nome = 'Endodontia')),
                                                                       ((SELECT id FROM dentistas WHERE email = 'fabio@risum.com'), (SELECT id FROM especialidades WHERE nome = 'Implantodontia')),
                                                                       ((SELECT id FROM dentistas WHERE email = 'gisele@risum.com'), (SELECT id FROM especialidades WHERE nome = 'Endodontia')),
                                                                       ((SELECT id FROM dentistas WHERE email = 'heitor@risum.com'), (SELECT id FROM especialidades WHERE nome = 'Ortodontia')),
                                                                       ((SELECT id FROM dentistas WHERE email = 'isabela@risum.com'), (SELECT id FROM especialidades WHERE nome = 'Implantodontia')),
                                                                       ((SELECT id FROM dentistas WHERE email = 'isabela@risum.com'), (SELECT id FROM especialidades WHERE nome = 'Ortodontia'));