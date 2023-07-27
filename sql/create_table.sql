-- auto-generated definition
create table user
(
    id           bigint auto_increment
        primary key,
    username     varchar(256)                       null comment '�û��ǳ�',
    userAccont   varchar(256)                       null comment '�˺�',
    avatarUrl    varchar(256)                       null comment '�Ա�',
    gender       tinyint                            null comment '�Ա�',
    userPassword varchar(512)                       not null comment '����',
    email        varchar(512)                       null comment '�ʼ�',
    userStatus   int      default 0                 null comment '״̬ 0-����',
    phone        varchar(128)                       null comment '�绰',
    createTime   datetime default CURRENT_TIMESTAMP null comment '����ʱ��',
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '����ʱ��',
    isDelete     tinyint  default 0                 not null comment '�Ƿ�ɾ��',
    userRole     int      default 0                 not null comment '�û���ɫ 0-��ͨ�û� 1-����Ա ',
    planetCode   varchar(512)                       null comment '������'
)
    comment '�û���';

