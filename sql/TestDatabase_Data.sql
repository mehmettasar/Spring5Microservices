INSERT INTO eat.role (id, name)
VALUES (1, 'USER');

SELECT setval('eat.role_id_seq', (SELECT count(*) FROM eat.role));


INSERT INTO eat.user (id, name, active, password, username)
VALUES (1, 'Normal user', true, '{bcrypt}$2a$10$i7LFiCo1JRm87ERePQOS3OkZ3Srgub8F7GyoWu6NmUuCLDTPq8zMW', 'user');    -- Raw password: user

SELECT setval('eat.user_id_seq', (SELECT count(*) FROM eat.user));


INSERT INTO eat.user_role (user_id, role_id)
VALUES (1, 1);


INSERT INTO eat.pizza (id, name, cost)
VALUES (1, 'Carbonara', 7.50)
      ,(2, 'Hawaiian', 8)
      ,(3, 'Margherita', 7);

SELECT setval('eat.pizza_id_seq', (SELECT count(*) FROM eat.pizza));

INSERT INTO eat.ingredient (id, name) 
VALUES (1, 'Bacon')
      ,(2, 'Cheese')
      ,(3, 'Egg')
      ,(4, 'Ham')
      ,(5, 'Mozzarella')
      ,(6, 'Oregano')
      ,(7, 'Parmesan')
      ,(8, 'Pineapple')
      ,(9, 'Tomato sauce');

SELECT setval('eat.ingredient_id_seq', (SELECT count(*) FROM eat.ingredient));


INSERT INTO eat.pizza_ingredient (pizza_id, ingredient_id) 
VALUES (1, 1), (1, 3), (1, 5), (1, 7)
      ,(2, 2), (2, 4), (2, 8)
      ,(3, 5), (3, 6), (3, 9);


INSERT INTO eat.order (id, code, created) 
VALUES (1, 'Order 1', '2018-12-31 16:00:00'::TIMESTAMP)
      ,(2, 'Order 2', '2019-01-02 18:00:00'::TIMESTAMP);
  
SELECT setval('eat.order_id_seq', (SELECT count(*) FROM eat.order));


INSERT INTO eat.order_line(id, order_id, pizza_id, cost, amount)
VALUES (1, 1, 1, 15, 2), (2, 1, 2, 8, 1)
      ,(3, 2, 1, 7.50, 1), (4, 2, 2, 16, 2), (5, 2, 3, 21, 3);

SELECT setval('eat.order_line_id_seq', (SELECT count(*) FROM eat.order_line));


----------- SECURITY -----------

INSERT INTO security.oauth_client_details (client_id, client_secret
                                          ,scope, authorized_grant_types
                                          ,web_server_redirect_uri, authorities
                                          ,access_token_validity, refresh_token_validity
                                          ,additional_information, autoapprove)
VALUES ('Spring5Microservices', '{bcrypt}$2a$10$NlKX/TyTk41qraDjxg98L.xFdu7IQYRoi3Z37PZmjekaQYAeaRZgO'   -- Raw password: Spring5Microservices
       ,'read,write,trust', 'implicit,refresh_token,password,authorization_code,client_credentials'
       ,null, null
       ,900, 3600
       ,null, true);


INSERT INTO security.jwt_client_details (client_id, client_secret
                                        ,signature_secret
                                        ,signature_algorithm, authentication_generator, token_type
                                        ,use_jwe, access_token_validity, refresh_token_validity)
VALUES ('Spring5Microservices', '{bcrypt}$2a$10$NlKX/TyTk41qraDjxg98L.xFdu7IQYRoi3Z37PZmjekaQYAeaRZgO'   -- Raw password: Spring5Microservices
        -- Raw password: Spring5Microservices_999#secret#789(jwt)$3411781_GTDSAET-569016310k
       ,'{cipher}04f1b9a71d880569283849aa911e4f3f3373a2522cba355e25e17f7ac7e262cb63d41295ab8bca038823b884858f05457306159cdfe68eb11c616028d6213b719887c07750e8c4b60dfea4196b1ddaffdcd462180028abc1a2d1dda69b8ac4bf'
       ,'HS512', 'SPRING5_MICROSERVICES', 'Bearer'
       ,true, 900, 3600);


