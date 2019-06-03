DROP DATABASE IF EXISTS weight_control;
CREATE DATABASE weight_control; 
USE weight_control;

CREATE TABLE diets 
(
	diet_id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	diet_name VARCHAR(20),
    start_weight DOUBLE,
    desired_weight DOUBLE,
    number_of_days INT,
    height DOUBLE
);

CREATE TABLE days 
(
	day_id DATE NOT NULL,
    diet_id INT,
    goal_weight DOUBLE,
    morning_weight DOUBLE,
    allowed_food_intake DOUBLE,
    like_dislike BOOL,
	PRIMARY KEY(day_id, diet_id),
    FOREIGN KEY(diet_id) REFERENCES diets(diet_id) ON DELETE CASCADE
);

CREATE TABLE food_weigh_ins
(
	food_weigh_in_id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	day_id DATE NOT NULL,
	diet_id INT NOT NULL,
    weight DOUBLE NOT NULL,
    meal_type VARCHAR(9) NOT NULL,
    FOREIGN KEY(day_id, diet_id) REFERENCES days(day_id, diet_id) ON DELETE CASCADE
);

CREATE TABLE body_weigh_ins 
(
	body_weigh_in_id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	day_id DATE NOT NULL,
	diet_id INT NOT NULL,
    weight DOUBLE NOT NULL,
    FOREIGN KEY(day_id, diet_id) REFERENCES days(day_id, diet_id)
);