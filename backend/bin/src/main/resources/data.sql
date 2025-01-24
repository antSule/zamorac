DROP TABLE IF EXISTS concert;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS app_users;


CREATE TABLE concert (
    id BIGSERIAL PRIMARY KEY,
    date DATE NOT NULL,
    time TIME NOT NULL,
    performer VARCHAR(255) NOT NULL
);

INSERT INTO concert (date, time, performer) VALUES
('2024-12-01', '19:00:00', 'The Rolling Stones'),
('2024-12-02', '20:00:00', 'Taylor Swift'),
('2024-12-03', '18:30:00', 'Coldplay'),
('2024-12-04', '21:00:00', 'Adele'),
('2024-12-05', '20:30:00', 'Drake'),
('2024-12-06', '19:30:00', 'Beyoncé'),
('2024-12-07', '20:00:00', 'Bruno Mars'),
('2024-12-08', '18:00:00', 'Ed Sheeran'),
('2024-12-09', '19:00:00', 'Billie Eilish'),
('2024-12-10', '20:30:00', 'Eminem'),
('2024-12-11', '21:00:00', 'Lady Gaga'),
('2024-12-12', '19:00:00', 'Justin Bieber'),
('2024-12-13', '20:00:00', 'Ariana Grande'),
('2024-12-14', '18:30:00', 'The Weeknd'),
('2024-12-15', '20:30:00', 'Post Malone'),
('2024-12-16', '21:00:00', 'Imagine Dragons'),
('2024-12-17', '19:30:00', 'Shawn Mendes'),
('2024-12-18', '18:00:00', 'Maroon 5'),
('2024-12-19', '20:00:00', 'Katy Perry'),
('2024-12-20', '21:00:00', 'Foo Fighters');


CREATE TABLE app_users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL,
    auth_provider VARCHAR(50),
    verification_code VARCHAR(255),
    verification_expiration TIMESTAMP,
    spotify_user_id VARCHAR(255)
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role VARCHAR(50) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_users(id) ON DELETE CASCADE
);


INSERT INTO app_users (username, email, password, enabled, auth_provider)
VALUES ('admin', 'admin@admin.com', '$2b$12$UVbYTEkHs6LI/ElOE/3Oqej54doY6rC6d0al6lMUHqWF0HNgyWcxm', true, '2');

INSERT INTO user_roles (user_id, role) VALUES
((SELECT id FROM app_users WHERE username = 'admin'), 'ADMIN');

