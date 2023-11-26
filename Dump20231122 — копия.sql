-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: cursach
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `athletes`
--

DROP TABLE IF EXISTS `athletes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `athletes` (
  `idAthletes` int NOT NULL AUTO_INCREMENT,
  `FIO` varchar(100) NOT NULL,
  `birth_date` date NOT NULL,
  `birth_country` varchar(100) NOT NULL,
  `image` varchar(100) DEFAULT NULL,
  `biography` text,
  `Users_idUsers` int NOT NULL,
  PRIMARY KEY (`idAthletes`),
  UNIQUE KEY `FIO_UNIQUE` (`FIO`),
  KEY `fk_Athletes_Users1_idx` (`Users_idUsers`),
  CONSTRAINT `fk_Athletes_Users1` FOREIGN KEY (`Users_idUsers`) REFERENCES `users` (`idUsers`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `athletes`
--

LOCK TABLES `athletes` WRITE;
/*!40000 ALTER TABLE `athletes` DISABLE KEYS */;
INSERT INTO `athletes` VALUES (1,'Arnold Schwarzenegger','1947-07-30','Austria','Arnold_Schwarzenegger.png','Арнольд Алоис Шварцнеггер (1947) – актер, бодибилдер, предприниматель, губернатор Калифорнии.\nРодился Арнольд Шварцнеггер 30 июля 1947 года в небольшой деревне Таль в Австрии. Еще в юношестве в биографии Шварцнеггера проявилось увлечение спортом. Сначала он играл в футбол, а затем увлекся бодибилдингом. В 1965 году пошел служить в армию, где ознакомился с делом механика-водителя. Во время службы победил в конкурсе «Мистер Европа», за участие в котором был сурово наказан. \nВ 1966 году, поселившись в Мюнхене, в биографии Арнольда Шварцнеггера некоторое время было посвящено работе в фитнес-клубе. Нарастив мускулатуру, на конкурсе «Мистер Вселенная» он добивается 2 места. В 1968 году эмигрирует в США. Там вскоре выигрывает «Мистер Олимпия» (в 1970, затем 1980).\nПервая киносъемка в биографии Шварцнеггера произошла в 1970 году. Арнольд быстро обучался актерскому мастерству, старался изменить акцент, а также сбросить вес, чтобы смотреться на экранах более естественно. Одними из знаменитейших фильмов Шварцнеггера являются: «Конон-варвар», «Хищник», «Терминатор» (а также 2,3 часть фильма), «Вспомнить все». Будучи миллионером, Шварцнеггер основал строительную компанию, а также громадную «Oak Productions». \nЗанявшись политикой, в 2003 году Арнольд был избран губернатором Калифорнии. За время первого срока нахождения на посту биография Шварцнеггера  наполнилась многими скандальными инцидентами. Однако это не помешало ему, ведь уже в 2006 году он был избран на второй срок. Шварцнеггер будет губернатором до 2011 года.\n',7),(2,'Larry Scott','1938-10-12','USA','Larry_Scott.png','Ларри Скотт (1938) – бодибилдер, первый победитель «Мистер Олимпия».\nРодился Ларри 12 октября 1938 года в городе Блэкфут штата Огайо. Впервые стал поднимать тяжести в 1956 году. Ларри занимался вместе с выдающимся бодибилдером – Винсом Жиронда.\nПрофессиональную карьеру начал в 1959 году. Тогда в биографии Ларри Скотта было выиграно первое звание – «Мистер Айдахо». Уже в следующем 1960 году Скотт добился четырех побед. Две – на конкурсе «Мистер Лос-Анджелес», и еще две на «Мистер Калифорния». В 1962 году Скотт выиграл «Мистер Америка» (в средней категории). В 1963, а затем и в 1964 – «Мистер Вселенная» также в средней категории. А в 1965 Ларри Скотт в США  стал первым победителем конкурса «Мистер Олимпия». Второй раз на этих же соревнованиях корону Скотт получил в 1966 году.\nНа протяжении 1960-х годов в своей биографии Скотт также был популярной моделью. Его фотографировали известнейшие мастера, а снимки появлялись в популярных журналах (например, «Joe Weider»). Участие в соревнованиях Ларри перестал принимать с 1966 года, а спортивную карьеру завершил в 1980 году. С 1960 до 1966 биография Ларри Скотта была самой известной среди всех бодибилдеров США.\nСейчас Скотт живет в городе Солт-Лейк-Сити в штате Юта. Там он запустил собственную тренировочную компанию. В 1999 году Ларри Скот был избран в Зал Славы Международной федерации бодибилдинга.\n\n',8),(3,'Ronnie Coleman','1964-05-13','USA','Ronnie_Coleman.png','Ронни Коулмен (1964) – американский профессиональный бодибилдер, восьмикратный победитель соревнований «Мистер Олимпия».\nРодился Ронни 13 мая в городе Бастроп, штат Луизиана. С детства в биографии Ронни Коулмена спорту отводилась важная роль. Ронни играл в баскетбол, футбол, бейсбол, а в 12 лет занялся пауэрлифтингом. Когда же поступил в Государственный университете Грамблинга, начал изучать экономику. Но недолго проработав по специальности, поступил в полицейскую школу.\nС того момента упорные тренировки в тренажерном зале стали нормой его жизни. Не смотря на то, что внушительные формы спортсмену достались по наследству от родителей, лишь упорные тренировки позволили отточить и еще более нарастить  мышечную массу.\nПервая победа Коулмена состоялась в 1990 году, тогда на соревновании «Мистер Техас» он стал победителем. Уже в следующем году на чемпионате мира среди любителей занял первое место в категории тяжелого веса. Первая победа на соревновании «Мистер Олимпия» в биографии Ронни Коулмена состоялась в 1998 году. С тех пор он еще семь раз становился победителем, став вторым культуристом, выигравшим «Олимпию» восемь раз с 1998 по 2005 (первым был Ли Хейни). В декабре 2007 года Коулмен женился, воспитывает двух дочерей.\n\n',9),(4,'Frank Zane','1942-06-28','USA','Frank_Zane.png','Фрэнк Зейн - легенда бодибилдинга, по мнению многих, спортсмен с самой гармонично развитой фигурой, - родился в Пенсильвании 28 июня 1942 года.',10),(5,'Lee Haney','1959-11-11','USA','Lee_Haney.png','Ли Хейни (1959) – профессиональный бодибилдер, мировой рекордсмен по количеству побед на соревнованиях «Мистер Олимпия».\nРодился Ли 11 ноября 1959 года в Спартанберге, Южная Каролина. Образование в биографии Ли Хейни было получено в колледже Спартанберга. Спортом увлекался уже с детства, а когда был студентом – входил в футбольную команду колледжа.\nЗанявшись бодибилдингом, Ли уже в 1979 году стал первым на соревновании «Мистер Америка» для подростков. В 1982 году ему удалось занять первое место на любительском чемпионате мира в категории тяжелого веса. В 1983 году он занял  несколько призовых мест на различных Гран-При: Англии (2-е место), Лас-Вегаса (1-е место), Швеции (2-е место), Швейцарии (3-е место). В 1983 Хейни участвовал в соревновании «Мистер Олимпия», став лишь 3-м. В том же году занял третье место на чемпионате мира среди профессионалов.\nВпервые в биографии Ли Хейни стал первым в конкурсе «Мистер Олимпия» в 1984. С тех пор становился победителем в 1984-1991 годах. В историю бодибилдинга Ли Хейни вошел как первый спортсмен, восемь раз выигрывавший самое престижное соревнование – «Мистер Олимпия». Такой рекорд был повторен лишь Ронни Колеманом, который в 2005 году также завоевал восьмую победу.\nВ 1998 году Билл Клинтон назначил Ли Хейни председателем совета Физической культуры и спорта.\n\n',11);
/*!40000 ALTER TABLE `athletes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `competition`
--

DROP TABLE IF EXISTS `competition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `competition` (
  `idCompetition` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `baner` varchar(100) DEFAULT NULL,
  `description` text,
  `holding_date` date NOT NULL,
  `Organizer_idOrganizer` int NOT NULL,
  `isRating` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`idCompetition`),
  KEY `fk_Competition_Organizer1_idx` (`Organizer_idOrganizer`),
  CONSTRAINT `fk_Competition_Organizer1` FOREIGN KEY (`Organizer_idOrganizer`) REFERENCES `organizer` (`idOrganizer`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `competition`
--

LOCK TABLES `competition` WRITE;
/*!40000 ALTER TABLE `competition` DISABLE KEYS */;
INSERT INTO `competition` VALUES (1,'Мистер Вселенная','Mr._universe.png','Мистер Вселенная (англ. Mr. Universe) — международное соревнование по культуризму, проводимое ежегодно в Великобритании под эгидой Национальной ассоциации бодибилдеров (англ. National Amateur Bodybuilders Association, NABBA). \nЯвляется одним из конкурсов Universe Championships.','2023-12-12',1,1),(2,'Мистер Олимпия','Mr._Olympia.png','Мистер Олимпия — самое значимое международное соревнование по культуризму, проводимое под эгидой Международной федерации бодибилдинга (англ. International Federation of Bodybuilding, IFBB).','2023-11-11',2,0),(3,'Мистер Вселенная','Mr._universe.png','Мистер Вселенная (англ. Mr. Universe) — международное соревнование по культуризму, проводимое ежегодно в Великобритании под эгидой Национальной ассоциации бодибилдеров (англ. National Amateur Bodybuilders Association, NABBA). \nЯвляется одним из конкурсов Universe Championships.','2024-12-12',1,0),(7,'test',NULL,'test','2023-11-30',2,1),(8,'test2',NULL,NULL,'2023-12-01',2,0);
/*!40000 ALTER TABLE `competition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `competition_has_jury`
--

DROP TABLE IF EXISTS `competition_has_jury`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `competition_has_jury` (
  `competition_idCompetition` int NOT NULL,
  `jury_idJury` int NOT NULL,
  PRIMARY KEY (`competition_idCompetition`,`jury_idJury`),
  KEY `fk_competition_has_jury_jury1_idx` (`jury_idJury`),
  KEY `fk_competition_has_jury_competition1_idx` (`competition_idCompetition`),
  CONSTRAINT `fk_competition_has_jury_competition1` FOREIGN KEY (`competition_idCompetition`) REFERENCES `competition` (`idCompetition`),
  CONSTRAINT `fk_competition_has_jury_jury1` FOREIGN KEY (`jury_idJury`) REFERENCES `jury` (`idJury`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `competition_has_jury`
--

LOCK TABLES `competition_has_jury` WRITE;
/*!40000 ALTER TABLE `competition_has_jury` DISABLE KEYS */;
INSERT INTO `competition_has_jury` VALUES (1,1),(2,1),(3,1),(7,1),(1,2),(2,2),(3,2),(7,2),(1,3),(2,3),(3,3),(7,3);
/*!40000 ALTER TABLE `competition_has_jury` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gradingcriteria`
--

DROP TABLE IF EXISTS `gradingcriteria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gradingcriteria` (
  `idGradingCriteria` int NOT NULL AUTO_INCREMENT,
  `nameCriteria` varchar(100) NOT NULL,
  PRIMARY KEY (`idGradingCriteria`),
  UNIQUE KEY `nameCriteria_UNIQUE` (`nameCriteria`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gradingcriteria`
--

LOCK TABLES `gradingcriteria` WRITE;
/*!40000 ALTER TABLE `gradingcriteria` DISABLE KEYS */;
INSERT INTO `gradingcriteria` VALUES (2,'Гармоничность  пропорций'),(1,'Объём мышечной массы'),(4,'Сбалансированность мышц'),(3,'Симметрия');
/*!40000 ALTER TABLE `gradingcriteria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jury`
--

DROP TABLE IF EXISTS `jury`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `jury` (
  `idJury` int NOT NULL AUTO_INCREMENT,
  `FIO` varchar(100) NOT NULL,
  `Users_idUsers` int NOT NULL,
  PRIMARY KEY (`idJury`),
  UNIQUE KEY `FIO_UNIQUE` (`FIO`),
  KEY `fk_Jury_Users1_idx` (`Users_idUsers`),
  CONSTRAINT `fk_Jury_Users1` FOREIGN KEY (`Users_idUsers`) REFERENCES `users` (`idUsers`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jury`
--

LOCK TABLES `jury` WRITE;
/*!40000 ALTER TABLE `jury` DISABLE KEYS */;
INSERT INTO `jury` VALUES (1,'Tony Jones',3),(2,'Henry Walker',4),(3,'Michael Smith',5);
/*!40000 ALTER TABLE `jury` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jury_evaluations`
--

DROP TABLE IF EXISTS `jury_evaluations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `jury_evaluations` (
  `Jury_idCompetition` int NOT NULL,
  `Jury_idAthlete` int NOT NULL,
  `Jury_idJury` int NOT NULL,
  `Jury_idGradingCriteria` int NOT NULL,
  `points` int NOT NULL,
  PRIMARY KEY (`Jury_idCompetition`,`Jury_idAthlete`,`Jury_idJury`,`Jury_idGradingCriteria`),
  KEY `fk_Partucupation_history_has_Jury_Jury1_idx` (`Jury_idJury`),
  KEY `fk_Partucupation_history_has_Jury_Partucupation_history1_idx` (`Jury_idCompetition`,`Jury_idAthlete`),
  KEY `fk_Partucupation_history_has_Jury_GradingCriteria1_idx` (`Jury_idGradingCriteria`),
  CONSTRAINT `fk_Partucupation_history_has_Jury_GradingCriteria1` FOREIGN KEY (`Jury_idGradingCriteria`) REFERENCES `gradingcriteria` (`idGradingCriteria`),
  CONSTRAINT `fk_Partucupation_history_has_Jury_Jury1` FOREIGN KEY (`Jury_idJury`) REFERENCES `jury` (`idJury`),
  CONSTRAINT `fk_Partucupation_history_has_Jury_Partucupation_history1` FOREIGN KEY (`Jury_idCompetition`, `Jury_idAthlete`) REFERENCES `participation_history` (`Competition_idCompetition`, `Athletes_idAthletes`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jury_evaluations`
--

LOCK TABLES `jury_evaluations` WRITE;
/*!40000 ALTER TABLE `jury_evaluations` DISABLE KEYS */;
INSERT INTO `jury_evaluations` VALUES (1,1,1,1,10),(1,1,1,2,5),(1,1,1,3,10),(1,1,1,4,10),(1,1,2,1,9),(1,2,1,1,7),(1,2,1,2,9),(1,2,1,3,9),(1,2,2,1,6),(1,3,1,1,8),(1,3,1,2,9),(1,3,1,3,9),(1,3,1,4,9),(1,3,2,1,7),(1,4,1,1,9),(1,4,2,1,8),(1,5,1,1,6),(1,5,2,1,5),(7,1,1,1,10),(7,1,1,2,10),(7,1,1,3,10),(7,1,1,4,5),(7,1,2,1,5),(7,1,2,2,5),(7,1,2,3,5),(7,1,2,4,5),(7,1,3,1,5),(7,1,3,2,5),(7,1,3,3,5),(7,1,3,4,5),(7,2,1,1,5),(7,2,1,2,5),(7,2,1,3,5),(7,2,1,4,5),(7,2,2,1,5),(7,2,2,2,5),(7,2,2,3,5),(7,2,2,4,5),(7,2,3,1,0),(7,2,3,2,8),(7,2,3,3,0),(7,2,3,4,0);
/*!40000 ALTER TABLE `jury_evaluations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organizer`
--

DROP TABLE IF EXISTS `organizer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `organizer` (
  `idOrganizer` int NOT NULL AUTO_INCREMENT,
  `FIO` varchar(100) NOT NULL,
  `Users_idUsers` int NOT NULL,
  PRIMARY KEY (`idOrganizer`),
  KEY `fk_Organizer_Users1_idx` (`Users_idUsers`),
  CONSTRAINT `fk_Organizer_Users1` FOREIGN KEY (`Users_idUsers`) REFERENCES `users` (`idUsers`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organizer`
--

LOCK TABLES `organizer` WRITE;
/*!40000 ALTER TABLE `organizer` DISABLE KEYS */;
INSERT INTO `organizer` VALUES (1,'Joe Vader',1),(2,'Jake Wood',2);
/*!40000 ALTER TABLE `organizer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `participation_history`
--

DROP TABLE IF EXISTS `participation_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `participation_history` (
  `Competition_idCompetition` int NOT NULL,
  `Athletes_idAthletes` int NOT NULL,
  `ranking_place` int DEFAULT NULL,
  PRIMARY KEY (`Competition_idCompetition`,`Athletes_idAthletes`),
  KEY `fk_Athletes_has_Competition_Competition1_idx` (`Competition_idCompetition`),
  KEY `fk_Athletes_has_Competition_Athletes_idx` (`Athletes_idAthletes`),
  CONSTRAINT `fk_Athletes_has_Competition_Athletes` FOREIGN KEY (`Athletes_idAthletes`) REFERENCES `athletes` (`idAthletes`),
  CONSTRAINT `fk_Athletes_has_Competition_Competition1` FOREIGN KEY (`Competition_idCompetition`) REFERENCES `competition` (`idCompetition`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `participation_history`
--

LOCK TABLES `participation_history` WRITE;
/*!40000 ALTER TABLE `participation_history` DISABLE KEYS */;
INSERT INTO `participation_history` VALUES (1,1,1),(1,2,3),(1,3,2),(1,4,4),(1,5,5),(2,1,NULL),(2,2,NULL),(2,3,NULL),(2,4,NULL),(2,5,NULL),(3,1,NULL),(7,1,1),(7,2,2);
/*!40000 ALTER TABLE `participation_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `idRole` int NOT NULL AUTO_INCREMENT,
  `name_role` varchar(100) NOT NULL,
  PRIMARY KEY (`idRole`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'Посетитель'),(2,'Спортсмен'),(3,'Организатор'),(4,'Жюри'),(5,'Администратор');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `idUsers` int NOT NULL AUTO_INCREMENT,
  `login` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `role_idRole` int NOT NULL,
  PRIMARY KEY (`idUsers`),
  UNIQUE KEY `login_UNIQUE` (`login`),
  KEY `fk_Users_Role1_idx` (`role_idRole`),
  CONSTRAINT `fk_Users_Role1` FOREIGN KEY (`role_idRole`) REFERENCES `role` (`idRole`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'org1','org1',3),(2,'org2','org2',3),(3,'jury1','jury1',4),(4,'jury2','jury2',4),(5,'jury3','jury3',4),(6,'admin','admin',5),(7,'sport1','sport1',2),(8,'sport2','sport2',2),(9,'sport3','sport3',2),(10,'sport4','sport4',2),(11,'sport5','sport5',2),(22,'v1','v1',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `visitor`
--

DROP TABLE IF EXISTS `visitor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `visitor` (
  `idvisitor` int NOT NULL AUTO_INCREMENT,
  `FIO` varchar(100) NOT NULL,
  `Users_idUsers` int NOT NULL,
  PRIMARY KEY (`idvisitor`),
  KEY `fk_visitor_users1_idx` (`Users_idUsers`),
  CONSTRAINT `fk_visitor_users1` FOREIGN KEY (`Users_idUsers`) REFERENCES `users` (`idUsers`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visitor`
--

LOCK TABLES `visitor` WRITE;
/*!40000 ALTER TABLE `visitor` DISABLE KEYS */;
INSERT INTO `visitor` VALUES (1,'Muhammad Ali',22);
/*!40000 ALTER TABLE `visitor` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-11-22 13:41:54
