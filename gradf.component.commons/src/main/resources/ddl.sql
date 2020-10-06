/*==============================================================*/
/* Table: gradf_data_owner                                      */
/*==============================================================*/
create table if not exists gradf_data_owner
(
    record_id            bigint not null  comment '',
    data_id              bigint not null  comment '数据整型ID',
    data_type            varchar(32) not null  comment '数据类型标识',
    owner_id             bigint not null  comment '属主整型ID',
    owner_type           varchar(32) not null  comment '属主类型标识,属主可以是所有者/组/其它等',
    owner_permission     tinyint not null  comment '属主对数据的权限',
    primary key (record_id)
) comment '数据属主及权限';

/*==============================================================*/
/* Table: gradf_extend_property                                 */
/*==============================================================*/
create table if not exists gradf_extend_property
(
    property_id          bigint not null  comment '',
    business_id          bigint not null  comment '被扩展对象的ID',
    business_type        varchar(32) not null  comment '被扩展对象的类型/标识',
    property_key         varchar(32) not null  comment '相当于字段名',
    property_text        varchar(60) not null  comment '相当于字段说明',
    property_value       varchar(60) not null  comment '相当于字段值',
    data_type            varchar(20) not null  comment '扩展属性数据类型',
    property_summary     varchar(60) not null  comment '更详尽的说明',
    primary key (property_id),
    key AK_property_ak (business_id, business_type, property_key)
) comment '扩展属性表';
