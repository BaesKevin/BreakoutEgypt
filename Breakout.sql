drop database if exists dbBreakout;
create database dbBreakout;
use dbBreakout;

create table users(
	userid int primary key auto_increment,
    username varchar(50) not null,
    email varchar(50),
    `hash` varchar(150),
    gold int(150) default 0,
    diamonds int(150) default 0
) engine=innodb;

ALTER TABLE `dbbreakout`.`users` 
CHANGE COLUMN `email` `email` VARCHAR(50) NOT NULL ,
ADD UNIQUE INDEX `email_UNIQUE` (`email` ASC);

create table levelpacks(
	id int primary key auto_increment,
    name varchar(50),
    description varchar(150),
    default_open_levels int,
    total_levels int
)engine=innodb;

create table difficulties(
	difficultyid int primary key auto_increment,
    difficultyname varchar(50),
    liveregeneration bool,
    lives_per_level int,
    powerup_duration int,
    powerdown_duration int,
    points_per_block int,
    powerup_ratio int,
    powerdown_ratio int,
    ball_speed int,
    brickscore_time_penalty int
)engine=innodb;

create table levelprogression(
	userid int,
    levelpackid int,
    difficultyid int,
    highest_level_reached int,
    isCampaign bool,
    foreign key(userid) references users(userid),
    foreign key(levelpackid) references levelpacks(id),
    foreign key(difficultyid) references difficulties(difficultyid)
)engine=innodb;

ALTER TABLE `dbbreakout`.`levelprogression` 
DROP FOREIGN KEY `levelprogression_ibfk_1`,
DROP FOREIGN KEY `levelprogression_ibfk_2`,
DROP FOREIGN KEY `levelprogression_ibfk_3`;
ALTER TABLE `dbbreakout`.`levelprogression` 
CHANGE COLUMN `userid` `userid` INT(11) NOT NULL ,
CHANGE COLUMN `levelpackid` `levelpackid` INT(11) NOT NULL ,
CHANGE COLUMN `difficultyid` `difficultyid` INT(11) NOT NULL ,
ADD PRIMARY KEY (`userid`, `levelpackid`, `difficultyid`);
ALTER TABLE `dbbreakout`.`levelprogression` 
ADD CONSTRAINT `levelprogression_ibfk_1`
  FOREIGN KEY (`userid`)
  REFERENCES `dbbreakout`.`users` (`userid`),
ADD CONSTRAINT `levelprogression_ibfk_2`
  FOREIGN KEY (`levelpackid`)
  REFERENCES `dbbreakout`.`levelpacks` (`id`),
ADD CONSTRAINT `levelprogression_ibfk_3`
  FOREIGN KEY (`difficultyid`)
  REFERENCES `dbbreakout`.`difficulties` (`difficultyid`);
ALTER TABLE `dbbreakout`.`levelprogression` 
CHANGE COLUMN `isCampaign` `isCampaign` TINYINT(1) NULL DEFAULT 0 ;


create table level(
	levelid int primary key auto_increment,
    levelpackid int,
    levelNumber int,
    name varchar(50),
    description varchar(150),
    isMultiplayer bool,
    foreign key(levelpackid) references levelpacks(id)
)engine=innodb;

create table shapedimensions(
	idshapedimension int primary key auto_increment,
    x float,
    y float,
    width int,
    height int
)engine=innodb;

create table balls(
	ballid int primary key auto_increment,
    shapedimensionid int,
    xspeed int,
    yspeed int,
    isStartingBall tinyint,
    playerIndex int,
    foreign key(shapedimensionid) references shapedimensions(idshapedimension)
)engine=innodb;

create table levelballs(
	levelid int,
    idball int,
    foreign key(levelid) references level(levelid),
    foreign key(idball) references balls(ballid)
)engine=innodb;

create table brick_types(
	id int primary key auto_increment,
    typename varchar(50)
)engine=innodb;

create table bricks(
	brickid int primary key auto_increment,
    shapedimensionid int,
    typeid int,
    isbreakable bool,
    isVisible bool,
    istarget bool,
    isInverted bool,
    playerIndex int,
    foreign key(shapedimensionid) references shapedimensions(idshapedimension),
    foreign key(typeid) references brick_types(id)
)engine=innodb;

create table toggleeffects(
	brickid int,
    totogglebrickid int,
    foreign key(brickid) references bricks(brickid),
    foreign key(totogglebrickid) references bricks(brickid)
)engine=innodb;

create table spawnballeffect(
	brickid int,
    ballid int,
    amountofballs int,
    foreign key(ballid) references balls(ballid),
    foreign key(brickid) references bricks(brickid)
) engine=innodb;

create table explosiveeffects(
	brickid int,
    centerbrickid int,
    radius int,
    foreign key(brickid) references bricks(brickid),
    foreign key(centerbrickid) references bricks(brickid)
) engine=innodb;

create table levelbricks(
	levelid int,
	brickid int,
	foreign key(levelid) references level(levelid),
	foreign key(brickid) references bricks(brickid)
)engine=innodb;

create table paddles(
	paddleid int primary key auto_increment,
    shapedimensionid int,
    playerIndex int,
    constraint foreign key(shapedimensionid) references shapedimensions(idshapedimension)
)engine=innodb;

create table levelpaddles(
	levelid int,
    idpaddle int,
    foreign key(levelid) references level(levelid),
    foreign key(idpaddle) references paddles(paddleid)
)engine=innodb;

create table level_scores(
	scoreId int primary key auto_increment,
    levelid int,
    userid int,
    difficultyid int,
    time bigint,
    points int,
    foreign key(levelid) references level(levelid),
    foreign key(userid) references users(userid),
    foreign key(difficultyid) references difficulties(difficultyid) 
) engine=innodb;

insert into brick_types(typename) values('REGULAR');
insert into brick_types(typename) values('UNBREAKABLE');
insert into brick_types(typename) values('EXPLOSIVE');
insert into brick_types(typename) values('SWITCH');
insert into brick_types(typename) values('TARGET');

insert into difficulties(difficultyname,
		ball_speed,
        lives_per_level,
        liveregeneration,
        points_per_block,
        powerup_ratio,
        powerup_duration,
		powerdown_duration,
        brickscore_time_penalty) values(
        "EASY",50,-1,true,8000,80,15,15,null);
insert into difficulties(difficultyname,
		ball_speed,
        lives_per_level,
        liveregeneration,
        points_per_block,
        powerup_ratio,
        powerup_duration,
		powerdown_duration,
        brickscore_time_penalty) values(
        "MEDIUM",65,3,true,6000,50,10,10,null);
insert into difficulties(difficultyname,
		ball_speed,
        lives_per_level,
        liveregeneration,
        points_per_block,
        powerup_ratio,
        powerup_duration,
		powerdown_duration,
        brickscore_time_penalty) values(
        "HARD",80,3,false,4000,30,5,5,null);
insert into difficulties(difficultyname,
		ball_speed,
        lives_per_level,
        liveregeneration,
        points_per_block,
        powerup_ratio,
        powerup_duration,
		powerdown_duration,
        brickscore_time_penalty) values(
        "BRUTAL",100,1,false,2000,0,0,0,null);
        
insert into levelpacks(name, description, default_open_levels, total_levels)
values('arcade', 'arcade', 3, 5), ('multiplayer', 'multiplayer', 1, 2);