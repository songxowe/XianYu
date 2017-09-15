
-- Create table
create table XIANYU_CATEGORY
(
  id   NUMBER(10) not null,
  name VARCHAR2(50),
  img  VARCHAR2(50)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table XIANYU_CATEGORY
  add primary key (ID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );


-- Create table
create table XIANYU_USER
(
  id       NUMBER(10) not null,
  username VARCHAR2(50) not null,
  password VARCHAR2(32) not null,
  sex      VARCHAR2(20),
  address  VARCHAR2(100),
  phone    VARCHAR2(20),
  imgurl   VARCHAR2(100)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table XIANYU_USER
  add primary key (ID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );


create table xianyu_product(
id number(10)primary key ,
title varchar2(60) not null,
info varchar2(100),
user_id number(10),
price  number(10,2),
priceinfo varchar2(50),
yutang_id number(10),
looknum number(10) default 0,
posttime date not null
);


create table xianyu_product_img(
id number(10) primary key ,
imgurl varchar2(100),
product_id number(10)
);


create table xianyu_product_comment(
id number(10) primary key,
text varchar2(100),
user_id number(10),
to_user_id number(10),
product_id  number(10),
time date
);



create table xianyu_product_good(
id number(10) primary key ,
product_id number(10),
user_id number(10),
time date
);


create table xianyu_yutang_user(
id number(10) primary key ,
yutang_id number(10),
user_id number(10),
identity varchar2(50),
);






select * from xianyu_user;



select * from xianyu_category;

select * from xianyu_yutang;

select * from xianyu_product;

select * from (select p.*,row_number()over(order by id)rn from xianyu_product p)
where rn<=8 and category_id=3 and id!=1;




select * from (select  p.id pid, p.title,p.info,p.price,p.priceinfo,
p.looknum,to_char(p.posttime,'yyyy-MM-dd HH:mi:ss') ,u.id userid, u.username, u.address,u.imgurl,
y.id yid,y.name, row_number()over(order by p.posttime desc)rn
 from xianyu_product p , xianyu_user u ,xianyu_yutang y where y.id=p.yutang_id
 and p.user_id=u.id
  ) where pid=1;

insert into xianyu_product(id,title,info,user_id,price,priceinfo,yutang_id,looknum,posttime,
category_id) values(seq_xianyu_product.nextval,'鼠标，九成新，便宜出','50买的，30出',
2,30,'',1,0,sysdate,3);

insert into xianyu_product_img(id,imgurl,product_id)
values(seq_xianyu_product_img.nextval,'product2.png',2);


select * from xianyu_product_img;

select * from xianyu_product_comment ;

select id,text,user_id,to_user_id,product_id,to_char(time,'yyyy-mm-dd HH24:mi:ss') from xianyu_product_comment;


select * from xianyu_product_good;
seq_xianyu_product_good.nextval



select u.yutang_id, u.identity,y.name,y.imgurl,y.lat,y.lng from (select * from xianyu_yutang_user  where user_id=1)u , xianyu_yutang y
where u.yutang_id=y.id
;

select count(*) from xianyu_yutang_user where  yutang_id=1;

 