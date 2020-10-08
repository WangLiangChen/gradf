/*==============================================================*/
/* Table: gradf_group                                           */
/*==============================================================*/
create table if not exists gradf_group
(
    group_id             bigint not null  comment '',
    parent_id            bigint  comment '',
    group_key            varchar(20) not null  comment '',
    group_text           varchar(30) not null  comment '',
    sort                 bigint not null  comment '排序',
    create_datetime      datetime not null  comment '创建时间',
    modify_datetime      datetime not null  comment '修改时间',
    creator              bigint not null  comment '创建人',
    modifier             bigint not null  comment '修改人',
    summary              varchar(600) not null  comment '数据简述',
    data_mode            tinyint not null  comment '数据本身的权限模式',
    status               varchar(20) not null  comment '',
    primary key (group_id)
) comment '账户分组 从组织的维度划分用户';

/*==============================================================*/
/* Table: gradf_role                                            */
/*==============================================================*/
create table if not exists gradf_role
(
    role_id              bigint not null  comment '',
    parent_id            bigint  comment '',
    role_key             varchar(30) not null  comment '',
    role_text            varchar(30) not null  comment '',
    sort                 bigint not null  comment '排序',
    create_datetime      datetime not null  comment '创建时间',
    modify_datetime      datetime not null  comment '修改时间',
    creator              bigint not null  comment '创建人',
    modifier             bigint not null  comment '修改人',
    summary              varchar(600) not null  comment '数据简述',
    data_mode            tinyint not null  comment '数据本身的权限模式',
    status               varchar(20) not null  comment '',
    primary key (role_id)
) comment '帐户角色 从功能的角度划分用户';

/*==============================================================*/
/* Table: gradf_account                                         */
/*==============================================================*/
create table if not exists gradf_account
(
    account_id           bigint not null  comment '',
    nick_name            varchar(30) not null  comment '',
    login_password       varchar(100) not null  comment '',
    password_salt        varchar(100) not null  comment '加密密码用的盐',
    secret_key           varchar(32) not null  comment '用于JWT加密解密的差异化key，强制登录失效可通过修改此key实现',
    expire_datetime      datetime not null  comment '',
    password_expire      datetime not null  comment '',
    last_login           datetime not null  comment '最后登录时间',
    last_ip              varchar(20) not null  comment '最后登录IP',
    sort                 bigint not null  comment '排序',
    create_datetime      datetime not null  comment '创建时间',
    modify_datetime      datetime not null  comment '修改时间',
    creator              bigint not null  comment '创建人',
    modifier             bigint not null  comment '修改人',
    summary              varchar(600) not null  comment '数据简述',
    data_mode            tinyint not null  comment '数据本身的权限模式',
    status               varchar(20) not null  comment '',
    primary key (account_id)
) comment '帐户基本信息及帐户密码登录配置信息';

/*==============================================================*/
/* Table: gradf_account_login                                   */
/*==============================================================*/
create table if not exists gradf_account_login
(
    login_name           varchar(50) not null  comment '全局唯一登录名',
    account_id           bigint  comment '',
    login_mode           varchar(20) not null  comment '登录方式LOGIN_NAME/EMAIL/MOBILE/WEIXIN',
    login_config         varchar(1000) not null  comment '登录配置信息',
    primary key (login_name)
) comment '账户登录方式';

/*==============================================================*/
/* Table: gradf_group_role                                      */
/*==============================================================*/
create table if not exists gradf_group_role
(
    group_id             bigint not null  comment '',
    role_id              bigint not null  comment '',
    sort                 bigint not null  comment '排序',
    create_datetime      datetime not null  comment '',
    modify_datetime      datetime not null  comment '',
    creator              bigint not null  comment '',
    modifier             bigint not null  comment '',
    summary              varchar(200) not null  comment '',
    data_mode            tinyint not null  comment '数据本身的权限模式',
    status               varchar(20) not null  comment '',
    primary key (group_id, role_id)
) comment '帐户组 帐户角色关系';

/*==============================================================*/
/* Table: gradf_group_account                                   */
/*==============================================================*/
create table if not exists gradf_group_account
(
    group_id             bigint not null  comment '',
    account_id           bigint not null  comment '',
    sort                 bigint not null  comment '排序',
    create_datetime      datetime not null  comment '',
    modify_datetime      datetime not null  comment '',
    creator              bigint not null  comment '',
    modifier             bigint not null  comment '',
    summary              varchar(200) not null  comment '',
    data_mode            tinyint not null  comment '数据本身的权限模式',
    status               varchar(20) not null  comment '',
    primary key (group_id, account_id)
) comment '帐户组与帐户关系';

/*==============================================================*/
/* Table: gradf_role_account                                    */
/*==============================================================*/
create table if not exists gradf_role_account
(
    role_id              bigint not null  comment '',
    account_id           bigint not null  comment '',
    sort                 bigint not null  comment '排序',
    create_datetime      datetime not null  comment '',
    modify_datetime      datetime not null  comment '',
    creator              bigint not null  comment '',
    modifier             bigint not null  comment '',
    summary              varchar(200) not null  comment '',
    data_mode            tinyint not null  comment '数据本身的权限模式',
    status               varchar(20) not null  comment '',
    primary key (role_id, account_id)
) comment '角色帐户关系';

/*==============================================================*/
/* Table: gradf_resource                                        */
/*==============================================================*/
create table if not exists gradf_resource
(
    resource_id          bigint not null  comment '',
    parent_id            bigint  comment '',
    resource_key         varchar(30) not null  comment '',
    resource_text        varchar(30) not null  comment '',
    auth_type            tinyint not null  comment ' 授权类型0-登录可用1-角色授权可用',
    create_datetime      datetime not null  comment '',
    summary              varchar(200) not null  comment '',
    primary key (resource_id)
) comment '需要授权的资源';

/*==============================================================*/
/* Table: gradf_operation                                       */
/*==============================================================*/
create table if not exists gradf_operation
(
    operation_id         bigint not null  comment '',
    resource_id          bigint  comment '',
    operation_key        varchar(30) not null  comment '',
    operation_text       varchar(30) not null  comment '',
    depend_key           varchar(30) not null  comment '依赖于某个操作',
    operation_privilege  bigint not null  comment '操作对于资源的权限值 用于二进制判断权限的权限值,取值0123 运算时用2的x次方',
    create_datetime      datetime not null  comment '',
    summary              varchar(200) not null  comment '',
    primary key (operation_id)
) comment '需要授权的操作';

/*==============================================================*/
/* Table: gradf_url                                             */
/*==============================================================*/
create table if not exists gradf_url
(
    url_id               bigint not null  comment '',
    url_text             varchar(30) not null  comment 'url说明',
    url_path             varchar(100) not null  comment '',
    create_datetime      datetime not null  comment '',
    summary              varchar(200) not null  comment '',
    primary key (url_id)
) comment '需要授权的url';

/*==============================================================*/
/* Table: gradf_role_resource_privilege                         */
/*==============================================================*/
create table if not exists gradf_role_resource_privilege
(
    role_id              bigint not null  comment '',
    resource_id          bigint not null  comment '',
    privilege            bigint not null  comment '角色对资源的权限值',
    sort                 bigint not null  comment '排序',
    create_datetime      datetime not null  comment '',
    modify_datetime      datetime not null  comment '',
    creator              bigint not null  comment '',
    modifier             bigint not null  comment '',
    summary              varchar(200) not null  comment '',
    data_mode            tinyint not null  comment '数据本身的权限模式',
    status               varchar(20) not null  comment '',
    primary key (role_id, resource_id)
) comment '角色对资源的权限值，根据操作的operatio_privilege判断角色是否拥有某个操作';

/*==============================================================*/
/* Table: gradf_role_resource_operation                         */
/*==============================================================*/
create table if not exists gradf_role_resource_operation
(
    role_id              bigint not null  comment '',
    resource_id          bigint not null  comment '',
    operation_id         bigint not null  comment '',
    sort                 bigint not null  comment '排序',
    create_datetime      datetime not null  comment '',
    modify_datetime      datetime not null  comment '',
    creator              bigint not null  comment '',
    modifier             bigint not null  comment '',
    summary              varchar(200) not null  comment '',
    data_mode            tinyint not null  comment '数据本身的权限模式',
    status               varchar(20) not null  comment '',
    primary key (role_id, resource_id, operation_id)
);

alter table gradf_role_resource_operation comment '角色拥有的资源和操作';

/*==============================================================*/
/* Table: gradf_url_relation                                    */
/*==============================================================*/
create table if not exists gradf_url_relation
(
    role_id              bigint not null  comment '',
    resource_id          bigint not null  comment '',
    operation_id         bigint not null  comment '',
    url_id               bigint not null  comment '',
    sort                 bigint not null  comment '排序',
    create_datetime      datetime not null  comment '',
    modify_datetime      datetime not null  comment '',
    creator              bigint not null  comment '',
    modifier             bigint not null  comment '',
    summary              varchar(200) not null  comment '',
    data_mode            tinyint not null  comment '数据本身的权限模式',
    status               varchar(20) not null  comment '',
    primary key (role_id, resource_id, operation_id, url_id)
) comment '角色/资源/操作对应的url';

/*==============================================================*/
/* Table: gradf_operation_log                                   */
/*==============================================================*/
create table if not exists gradf_operation_log
(
    log_id               bigint not null  comment '',
    business_id          bigint not null  comment '',
    business_type        varchar(20) not null  comment '',
    business_name        varchar(30) not null  comment '业务名称',
    operator_id          bigint not null  comment '操作人员id',
    operator_name        varchar(30) not null  comment '操作人员名称',
    operation_flag       varchar(20) not null  comment '操作标识',
    operation_name       varchar(30) not null  comment '操作名称',
    source_class         varchar(100) not null  comment '操作源类名',
    source_method        varchar(50) not null  comment '操作源方法名',
    original_data        varchar(1000) not null  comment '原数据',
    new_data             varchar(1000) not null  comment '新数据',
    operation_parameter  varchar(1000) not null  comment '方法参数',
    operation_return     varchar(1000) not null  comment '方法返回值或异常',
    operation_success    tinyint not null  comment '方法是否执行成功',
    create_datetime      datetime not null  comment '',
    summary              varchar(500) not null  comment '',
    status               varchar(20) not null  comment 'INIT|CONFIRMED',
    primary key (log_id)
) comment '操作日志';

