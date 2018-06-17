--------------------------------------------- [ Init ] --------------------------------------------

DROP SCHEMA IF EXISTS PAE CASCADE;
CREATE SCHEMA PAE;

CREATE SEQUENCE PAE.pk_journees_entreprises;
CREATE SEQUENCE PAE.pk_utilisateurs;
CREATE SEQUENCE PAE.pk_adresses_facturation;
CREATE SEQUENCE PAE.pk_entreprises;
CREATE SEQUENCE PAE.pk_personnes_contact;


---------------------------------------- [ Create tables ] ----------------------------------------

CREATE TABLE PAE.journees_entreprises (
    id_journee_entreprises INTEGER PRIMARY KEY DEFAULT nextval('PAE.pk_journees_entreprises'),
    annee_academique VARCHAR(20),
    date_journee TIMESTAMP,
    date_invitations TIMESTAMP,
    cloturee boolean 
);

CREATE TABLE PAE.utilisateurs (
    id_utilisateur INTEGER PRIMARY KEY DEFAULT nextval('PAE.pk_utilisateurs'),
    pseudo VARCHAR(50),
    nom VARCHAR(100),
    prenom VARCHAR(100),
    email VARCHAR(100),
    est_responsable BOOLEAN,
    mot_de_passe VARCHAR(100)
);

CREATE TABLE PAE.communes (
    code_postal INTEGER PRIMARY KEY,
    nomCommune VARCHAR(50)
);

CREATE TABLE PAE.adresses_facturation (
    id_adresse_facturation INTEGER PRIMARY KEY DEFAULT nextval('PAE.pk_adresses_facturation'),
    rue VARCHAR(100),
    numero VARCHAR(20),
    boite VARCHAR(20),
    commune INTEGER REFERENCES PAE.communes(code_postal)
);

CREATE TABLE PAE.entreprises (
    id_entreprise INTEGER PRIMARY KEY DEFAULT nextval('PAE.pk_entreprises'),
    adresse_facturation INTEGER REFERENCES PAE.adresses_facturation(id_adresse_facturation),
    createur INTEGER REFERENCES PAE.utilisateurs(id_utilisateur),
    nom VARCHAR(100),
    date_premier_contact TIMESTAMP,
    num_version INTEGER
);

CREATE TABLE PAE.personnes_contact (
    id_personne_contact INTEGER PRIMARY KEY DEFAULT nextval('PAE.pk_personnes_contact'),
    entreprise INTEGER REFERENCES PAE.entreprises(id_entreprise),
    nom VARCHAR(100),
    prenom VARCHAR(100),
    email VARCHAR(100),
    telephone VARCHAR(20),
    est_actif BOOLEAN,
    num_version INTEGER
);

CREATE TABLE PAE.participations_entreprises (
    journee_entreprises INTEGER REFERENCES PAE.journees_entreprises(id_journee_entreprises),
    entreprise INTEGER REFERENCES PAE.entreprises(id_entreprise),
    etat varchar(20),    
    num_version_participation INTEGER,
    annulee BOOLEAN,
    PRIMARY KEY (journee_entreprises, entreprise)
);

CREATE TABLE PAE.participations_contacts (
    journee_entreprises INTEGER REFERENCES PAE.journees_entreprises(id_journee_entreprises),
    entreprise INTEGER REFERENCES PAE.entreprises(id_entreprise),
    personne_contact INTEGER REFERENCES PAE.personnes_contact(id_personne_contact),
    num_version_participation_contact INTEGER,
    PRIMARY KEY(journee_entreprises, entreprise, personne_contact)
);


-------------------------------------------- [ Grants ] -------------------------------------------

-- Plutôt que d'utiliser des grant on a décidé de "sacrifier" la db de Gauthier (sous le conseil de M. Grolaux) en donnant l'accès
-- à celle-ci à tout le monde via un fichier properties contenant les mêmes infos pour éviter les conflits de commit


-- login roles : [gauthier_lallemand], ahmad_bashir, benjamin_berge, christophe_bortier, roland_bura

-- GRANT CONNECT
--     ON DATABASE dbgauthier_lallemand
--     TO ahmad_bashir, benjamin_berge, christophe_bortier, roland_bura;
-- GRANT USAGE
--     ON SCHEMA PAE
--     TO ahmad_bashir, benjamin_berge, christophe_bortier, roland_bura;
-- GRANT USAGE, SELECT, UPDATE
--     ON PAE.pk_journees_entreprises, PAE.pk_entreprises, PAE.pk_personnes_contact, PAE.pk_adresses_facturation, PAE.pk_utilisateurs
--     TO ahmad_bashir, benjamin_berge, christophe_bortier, roland_bura;
-- GRANT SELECT, INSERT, UPDATE
--     ON ALL TABLES IN SCHEMA PAE
--     TO ahmad_bashir, benjamin_berge, christophe_bortier, roland_bura;
-- GRANT DELETE, TRUNCATE
--     ON PAE.participations_entreprises, PAE.participations_contacts
--     TO ahmad_bashir, benjamin_berge, christophe_bortier, roland_bura;


-------------------------------------------- [ Views ] --------------------------------------------

CREATE OR REPLACE VIEW PAE.nombre_entreprises_invitees (nombre_invitees) AS
    -- Compte le nombre d'entreprises invitées pour la dernière JE (= dernière JE insérée)
    SELECT count(*)
    FROM PAE.participations_entreprises pe, PAE.journees_entreprises je
    WHERE (SELECT max(id_journee_entreprises) FROM PAE.journees_entreprises) = je.id_journee_entreprises
          AND je.id_journee_entreprises = pe.journee_entreprises
          AND pe.etat = 'invitée'
          AND pe.annulee = FALSE;

CREATE OR REPLACE VIEW PAE.nombre_entreprises_confirmees (nombre_confirmees) AS
    -- Compte le nombre d'entreprises confirmées pour la dernière JE (= dernière JE insérée) 
    SELECT count(*)
    FROM PAE.participations_entreprises pe, PAE.journees_entreprises je
    WHERE (SELECT max(id_journee_entreprises) FROM PAE.journees_entreprises) = je.id_journee_entreprises
          AND je.id_journee_entreprises = pe.journee_entreprises
          AND pe.etat = 'confirmée'
          AND pe.annulee = FALSE;

CREATE OR REPLACE VIEW PAE.nombre_contacts_presents (nombre_contacts) AS
    -- Compte le nombre de personnes de contact présents pour la dernière JE (= dernière JE insérée)
    SELECT count(*)
    FROM PAE.participations_contacts pc, PAE.journees_entreprises je
    WHERE (SELECT max(id_journee_entreprises) FROM PAE.journees_entreprises) = je.id_journee_entreprises
          AND je.id_journee_entreprises = pc.journee_entreprises;



    
-------------------------------------- [ Demo ] -------------------------------------------------------
select * from PAE.entreprises;
select * from PAE.journees_entreprises;
select * from PAE.participations_entreprises;
select * from PAE.utilisateurs;

insert into PAE.utilisateurs values 
(DEFAULT,'don','donatien','grolaux','donatien.grolaux@vinci.be',true,'$2a$10$PYb3U8L7h5yQaXqmZ7stgeiJp0gEM0F/8Xpco3yQALU3S5qntRYSy'),
(DEFAULT,'bri','brigitte','lehman','brigitte.lehmann@vinci.be',true,'$2a$10$PYb3U8L7h5yQaXqmZ7stgeiJp0gEM0F/8Xpco3yQALU3S5qntRYSy');


insert into PAE.journees_entreprises values 
(DEFAULT,'2012-2013','13/11/2013','10/11/2013',true),
(DEFAULT,'2013-2014','12/11/2014','10/11/2014',true),
(DEFAULT,'2014-2015','28/10/2015','20/10/2015',true),
(DEFAULT,'2015-2016','26/10/2016','20/10/2016',true);

insert into PAE.communes values
(1000,'Brussels'),
(1348,'LLN'),
(1170,'Brussels/Bruxelles'),
(1435,'Mont-Saint-Guibert'),
(1410,'Waterloo');

insert into PAE.adresses_facturation values
(DEFAULT,'Waterloolaan','16',DEFAULT, 1000),
(DEFAULT,'Avenue de l espérance','40','b', 1348),
(DEFAULT,'Vorstlaan','36',DEFAULT, 1170),
(DEFAULT,'rue André Dumont','5',DEFAULT, 1435),
(DEFAULT,'Drève Richelle','161','L b46', 1410);

insert into PAE.entreprises values
(DEFAULT,1,1,'Accenture','2012-05-06 00:00:00',0),
(DEFAULT,2,1,'CodeltBlue','2012-05-06 00:00:00',0),
(DEFAULT,3,1,'STERIA','2012-05-06 00:00:00',0),
(DEFAULT,4,1,'Eezee-IT','2012-05-06 00:00:00',0),
(DEFAULT,5,1,'Bewan','2012-05-06 00:00:00',0);



insert into PAE.personnes_contact values
(DEFAULT,1,'De Troyer','Stéphanie','stephanie.de.troyer@accenture.com',' ',true,0),
(DEFAULT,1,'Van Dyck','Marijke','marijke.vandyck@accenture.com',' ',true,0),
(DEFAULT,1,'Marecaux','Aimée','aimée.marecaux@accenture.com',' ',true,0),
(DEFAULT,2,'Lepape','Vincent','Vincent.lepape@CodeltBlue.Com','0479/97.95.05',true,0),
(DEFAULT,3,'Alvarez','Roberto','roberto.alvarez@steria.be',' ',true,0),
(DEFAULT,4,'Rigo','Nicolas','nicolas.rigo@eezee-it.com',' ',true,0),
(DEFAULT,5,'BRASSINE','Virginie','Virginie.BRASSINNE@bewan.be',' ',true,0),
(DEFAULT,5,'isabelle','Croiset','isabelle.croiset@bewan.be',' ',true,0),
(DEFAULT,5,'Bénédicte','Dedecker','drh@bewan.be',' ',true,0);

insert into PAE.participations_entreprises values
(2,1,'payee',0,false),
(3,1,'payee',0,false),
(4,1,'payee',0,false),
(1,2,'payee',0,false),
(1,3,'payee',0,false),
(2,3,'payee',0,false),
(3,3,'payee',0,false),
(4,3,'payee',0,false),
(1,4,'payee',0,false),
(2,4,'confirme',0,true),
(1,5,'payee',0,false),
(4,5,'payee',0,false);

-- je company contact
insert into PAE.participations_contacts values
(2,1,1,0),
(3,1,2,0),
(3,1,3,0),
(4,1,3,0),
(1,2,4,0),
(1,3,5,0),
(2,3,5,0),
(3,3,5,0),
(4,3,5,0),
(1,4,6,0),
(1,5,7,0),
(1,5,8,0),
(4,5,9,0);






