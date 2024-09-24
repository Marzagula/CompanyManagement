-- Inserty dla tabeli JOB
-- IT Department Jobs
INSERT INTO JOB (TITLE, DEPARTMENT_ID, DESCRIPTION) VALUES ('Junior Developer', 1, 'Entry-level software developer');
INSERT INTO JOB (TITLE, DEPARTMENT_ID, DESCRIPTION) VALUES ('Senior Developer', 1, 'Experienced software developer');
INSERT INTO JOB (TITLE, DEPARTMENT_ID, DESCRIPTION) VALUES ('DevOps Engineer', 1, 'Responsible for IT infrastructure and automation');

-- PRODUCT Department Jobs
INSERT INTO JOB (TITLE, DEPARTMENT_ID, DESCRIPTION) VALUES ('Product Manager', 2, 'Oversees product development and strategy');
INSERT INTO JOB (TITLE, DEPARTMENT_ID, DESCRIPTION) VALUES ('Product Owner', 2, 'Defines product vision and manages product backlog');
INSERT INTO JOB (TITLE, DEPARTMENT_ID, DESCRIPTION) VALUES ('UX Designer', 2, 'Focuses on user experience and interface design');

-- SALES Department Jobs
INSERT INTO JOB (TITLE, DEPARTMENT_ID, DESCRIPTION) VALUES ('Sales Executive', 3, 'Handles sales and client acquisition');
INSERT INTO JOB (TITLE, DEPARTMENT_ID, DESCRIPTION) VALUES ('Sales Manager', 3, 'Manages sales team and strategies');
INSERT INTO JOB (TITLE, DEPARTMENT_ID, DESCRIPTION) VALUES ('Account Manager', 3, 'Maintains client relationships and sales operations');

-- ACCOUNTING Department Jobs
INSERT INTO JOB (TITLE, DEPARTMENT_ID, DESCRIPTION) VALUES ('Junior Accountant', 4, 'Handles basic accounting tasks and entries');
INSERT INTO JOB (TITLE, DEPARTMENT_ID, DESCRIPTION) VALUES ('Senior Accountant', 4, 'Oversees financial reporting and accounting procedures');
INSERT INTO JOB (TITLE, DEPARTMENT_ID, DESCRIPTION) VALUES ('Finance Manager', 4, 'Manages financial planning and analysis');

-- HR Department Jobs
INSERT INTO JOB (TITLE, DEPARTMENT_ID, DESCRIPTION) VALUES ('HR Specialist', 5, 'Manages recruitment and employee relations');
INSERT INTO JOB (TITLE, DEPARTMENT_ID, DESCRIPTION) VALUES ('HR Manager', 5, 'Oversees HR policies and department operations');
INSERT INTO JOB (TITLE, DEPARTMENT_ID, DESCRIPTION) VALUES ('Recruitment Coordinator', 5, 'Coordinates recruitment processes and activities');

INSERT INTO CLAUSE (clause_type, clause_title, clause_description)
VALUES (
    'age',
    'Klauzula dotycząca wieku podatnika',
    'Podatnik, który nie ukończył 26. roku życia, może korzystać z ulg podatkowych.'
);

INSERT INTO CLAUSE (clause_type, clause_title, clause_description)
VALUES (
    'residence_outside',
    'Praca poza miejscem zamieszkania',
    'Podatnik, który pracuje poza miejscem zamieszkania ponosi większe koszty pracy.'
);