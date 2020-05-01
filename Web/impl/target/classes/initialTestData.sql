
INSERT INTO `user` (`id`, `certificate_number`, `name`, `surname`, `gender`, `profession`, `date_employment`, `isadmin`) VALUES (DEFAULT, 123, 'Tom', 'White', 'MALE', 'labour protection engineer', '2010-06-01', true);
INSERT INTO `user` (`id`, `certificate_number`, `name`, `surname`, `gender`, `profession`, `date_employment`, `isadmin`) VALUES (DEFAULT, 456, 'Catrin', 'Paul', 'FEMALE', 'assembler', '2011-07-01', false);
INSERT INTO `user` (`id`, `certificate_number`, `name`, `surname`, `gender`, `profession`, `date_employment`, `isadmin`) VALUES (DEFAULT, 789, 'Antony', 'Recker', 'MALE', 'welder', '2018-02-15', false);
INSERT INTO `user` (`id`, `certificate_number`, `name`, `surname`, `gender`, `profession`, `date_employment`, `isadmin`) VALUES (DEFAULT, 159, 'Margaret', 'Nory', 'FEMALE', 'electrician', '2016-06-21', false);
INSERT INTO `user` (`id`, `certificate_number`, `name`, `surname`, `gender`, `profession`, `date_employment`, `isadmin`) VALUES (DEFAULT, 753, 'Tonny', 'Verdeno', 'MALE', 'engineer', '2020-03-01', false);

INSERT INTO `user_creds` (`id`, `login`, `pass`, `user_fk`) VALUES (DEFAULT, 'admin', 'admin', 1);
INSERT INTO `user_creds` (`id`, `login`, `pass`, `user_fk`) VALUES (DEFAULT, 'aaa', '456', 2);
INSERT INTO `user_creds` (`id`, `login`, `pass`, `user_fk`) VALUES (DEFAULT, 'bbb', '789', 3);
INSERT INTO `user_creds` (`id`, `login`, `pass`, `user_fk`) VALUES (DEFAULT, 'ccc', '159', 4);
INSERT INTO `user_creds` (`id`, `login`, `pass`, `user_fk`) VALUES (DEFAULT, 'user', 'user', 5);

INSERT INTO `briefing` (`id`, `name`, `interval_in_months`) VALUES (DEFAULT, 'safety instruction', 6);
INSERT INTO `briefing` (`id`, `name`, `interval_in_months`) VALUES (DEFAULT, 'work with power tools', 12);
INSERT INTO `briefing` (`id`, `name`, `interval_in_months`) VALUES (DEFAULT, 'loading and unloading', 12);
INSERT INTO `briefing` (`id`, `name`, `interval_in_months`) VALUES (DEFAULT, 'work with a brush cutter', 12);
INSERT INTO `briefing` (`id`, `name`, `interval_in_months`) VALUES (DEFAULT, 'work with a lawn mower', 12);
INSERT INTO `briefing` (`id`, `name`, `interval_in_months`) VALUES (DEFAULT, 'work at height', 12);

INSERT INTO `user_has_briefing` (`id`, `last_date`, `briefing_id`, `user_id`) VALUES (DEFAULT, '2020-03-12', 1, 1);
INSERT INTO `user_has_briefing` (`id`, `last_date`, `briefing_id`, `user_id`) VALUES (DEFAULT, '2019-03-12', 2, 1);
INSERT INTO `user_has_briefing` (`id`, `last_date`, `briefing_id`, `user_id`) VALUES (DEFAULT, '2020-03-12', 1, 2);
INSERT INTO `user_has_briefing` (`id`, `last_date`, `briefing_id`, `user_id`) VALUES (DEFAULT, '2019-12-28', 4, 2);
INSERT INTO `user_has_briefing` (`id`, `last_date`, `briefing_id`, `user_id`) VALUES (DEFAULT, '2020-01-28', 1, 3);
INSERT INTO `user_has_briefing` (`id`, `last_date`, `briefing_id`, `user_id`) VALUES (DEFAULT, '2019-08-21', 1, 4);
INSERT INTO `user_has_briefing` (`id`, `last_date`, `briefing_id`, `user_id`) VALUES (DEFAULT, '2019-02-17', 4, 4);
INSERT INTO `user_has_briefing` (`id`, `last_date`, `briefing_id`, `user_id`) VALUES (DEFAULT, '2019-07-28', 5, 4);
INSERT INTO `user_has_briefing` (`id`, `last_date`, `briefing_id`, `user_id`) VALUES (DEFAULT, '2019-12-28', 1, 5);
INSERT INTO `user_has_briefing` (`id`, `last_date`, `briefing_id`, `user_id`) VALUES (DEFAULT, '2019-05-12', 3, 5);

INSERT INTO `task` (`id`, `name`, `type`, `status`, `deadline`, `creator`, `executor`) VALUES (DEFAULT, 'to do something one', 'PERSONAL', 'OPEN', '2020-07-15', 1, 2);
INSERT INTO `task` (`id`, `name`, `type`, `status`, `deadline`, `creator`, `executor`) VALUES (DEFAULT, 'to do something two', 'ELECTRONIC', 'CLOSE', NULL, 1, 3);
INSERT INTO `task` (`id`, `name`, `type`, `status`, `deadline`, `creator`, `executor`) VALUES (DEFAULT, 'to do something three', 'WELDING', 'EXECUTING', NULL, 1, 4);
INSERT INTO `task` (`id`, `name`, `type`, `status`, `deadline`, `creator`, `executor`) VALUES (DEFAULT, 'to do something four', 'ADJUSTMENT', 'OPEN', NULL, 1, 5);
INSERT INTO `task` (`id`, `name`, `type`, `status`, `deadline`, `creator`, `executor`) VALUES (DEFAULT, 'to do something five', 'PERSONAL', 'OPEN', '2020-09-03', 1, 2);
INSERT INTO `task` (`id`, `name`, `type`, `status`, `deadline`, `creator`, `executor`) VALUES (DEFAULT, 'to do something six', 'ELECTRONIC', 'OPEN', '2020-05-28', 1, 3);
INSERT INTO `task` (`id`, `name`, `type`, `status`, `deadline`, `creator`, `executor`) VALUES (DEFAULT, 'to do something seven', 'WELDING', 'CLOSE', '2020-03-19', 1, 4);
INSERT INTO `task` (`id`, `name`, `type`, `status`, `deadline`, `creator`, `executor`) VALUES (DEFAULT, 'to do something eight', 'ADJUSTMENT', 'CLOSE', '2020-02-11', 1, 5);
INSERT INTO `task` (`id`, `name`, `type`, `status`, `deadline`, `creator`, `executor`) VALUES (DEFAULT, 'to do something nine', 'PERSONAL', 'CLOSE', '2020-03-19', 1, 2);
INSERT INTO `task` (`id`, `name`, `type`, `status`, `deadline`, `creator`, `executor`) VALUES (DEFAULT, 'to do something ten', 'ELECTRONIC', 'EXECUTING', '2020-05-19', 1, 3);
INSERT INTO `task` (`id`, `name`, `type`, `status`, `deadline`, `creator`, `executor`) VALUES (DEFAULT, 'to do something eleven', 'WELDING', 'EXECUTING', '2020-06-03', 1, 4);
INSERT INTO `task` (`id`, `name`, `type`, `status`, `deadline`, `creator`, `executor`) VALUES (DEFAULT, 'to do something twelve', 'ADJUSTMENT', 'EXECUTING', '2020-06-12', 1, 5);

INSERT INTO `message` (`id`, `text`, `date`, `user_from`, `User_to`) VALUES (DEFAULT, 'hi', '2020-03-20 20:20:20', 1, 4);
INSERT INTO `message` (`id`, `text`, `date`, `user_from`, `User_to`) VALUES (DEFAULT, 'hi', '2020-03-20 20:21:00', 4, 1);
INSERT INTO `message` (`id`, `text`, `date`, `user_from`, `User_to`) VALUES (DEFAULT, 'what\'s up?', '2020-03-20 20:22:00', 1, 4);
INSERT INTO `message` (`id`, `text`, `date`, `user_from`, `User_to`) VALUES (DEFAULT, 'nice', '2020-03-20 20:23:00', 4, 1);
INSERT INTO `message` (`text`, `date`, `user_from`) VALUES ('common', '2020-02-21 20:23:00', '2');
INSERT INTO `message` (`text`, `date`, `user_from`) VALUES ('chat', '2020-02-21 20:24:00', '3');
INSERT INTO `message` (`text`, `date`, `user_from`) VALUES ('messages', '2020-02-21 20:25:00', '5');

