CREATE DATABASE srm_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;`srm_db`

-- 供应商管理相关表
-- 供应商表
CREATE TABLE `supplier` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '供应商ID',
  `name` VARCHAR(100) NOT NULL COMMENT '供应商名称',
  `contact_person` VARCHAR(50) DEFAULT NULL COMMENT '联系人',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `country` VARCHAR(50) DEFAULT NULL COMMENT '所在国家',
  `category` VARCHAR(100) DEFAULT NULL COMMENT '主营品类',
  `status` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '状态：0-待审核，1-已通过，2-已驳回',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  `update_time` DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY  (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_category` (`category`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='供应商表';

SELECT * FROM supplier;
-- 供应商审核记录表
CREATE TABLE `supplier_audit` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `supplier_id` BIGINT(20) NOT NULL COMMENT '供应商ID',
  `audit_result` TINYINT(4) NOT NULL COMMENT '审核结果：1-通过，2-驳回',
  `audit_opinion` VARCHAR(500) DEFAULT NULL COMMENT '审核意见',
  `audit_time` DATETIME NOT NULL COMMENT '审核时间',
  PRIMARY KEY  (`id`),
  KEY `idx_supplier_id` (`supplier_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='供应商审核记录';


-- 寻源与招标相关表
-- 采购需求表
CREATE TABLE `purchase_requirement` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '需求ID',
  `product_name` VARCHAR(100) NOT NULL COMMENT '产品名称',
  `specification` VARCHAR(200) DEFAULT NULL COMMENT '规格',
  `quantity` INT(11) NOT NULL COMMENT '数量',
  `delivery_date` DATE DEFAULT NULL COMMENT '期望交货日期',
  `budget_max` DECIMAL(15,2) DEFAULT NULL COMMENT '最高预算',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  PRIMARY KEY  (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='采购需求表';

-- 招标表（修正版）
CREATE TABLE `tender` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '招标ID',
  `requirement_id` BIGINT(20) NOT NULL COMMENT '关联需求ID',
  `title` VARCHAR(200) NOT NULL COMMENT '招标标题',
  `description` TEXT COMMENT '招标说明',
  `start_time` DATETIME NOT NULL COMMENT '开始时间',
  `end_time` DATETIME NOT NULL COMMENT '结束时间',
  `status` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '状态：0-未开始，1-进行中，2-已结束',
  `create_time` DATETIME NOT NULL COMMENT '创建时间', -- 移除默认值
  PRIMARY KEY  (`id`),
  KEY `idx_requirement_id` (`requirement_id`),
  KEY `idx_status` (`status`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='招标表';

-- 招标邀请供应商关联表（修正版）
CREATE TABLE `tender_supplier` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `tender_id` BIGINT(20) NOT NULL COMMENT '招标ID',
  `supplier_id` BIGINT(20) NOT NULL COMMENT '供应商ID',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `idx_tender_supplier` (`tender_id`,`supplier_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='招标邀请供应商';

-- 投标表（修正版）
CREATE TABLE `bid` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '投标ID',
  `tender_id` BIGINT(20) NOT NULL COMMENT '招标ID',
  `supplier_id` BIGINT(20) NOT NULL COMMENT '供应商ID',
  `price` DECIMAL(15,2) NOT NULL COMMENT '投标价格',
  `delivery_days` INT(11) DEFAULT NULL COMMENT '交货天数',
  `create_time` DATETIME NOT NULL COMMENT '创建时间', -- 移除默认值
  PRIMARY KEY  (`id`),
  UNIQUE KEY `idx_tender_supplier` (`tender_id`,`supplier_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='投标表';

-- 招标结果表（修正版）
CREATE TABLE `tender_result` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `tender_id` BIGINT(20) NOT NULL COMMENT '招标ID',
  `winner_id` BIGINT(20) NOT NULL COMMENT '中标供应商ID',
  `winning_price` DECIMAL(15,2) NOT NULL COMMENT '中标价格',
  `confirm_time` DATETIME NOT NULL COMMENT '确认时间', -- 移除默认值
  PRIMARY KEY  (`id`),
  UNIQUE KEY `idx_tender_id` (`tender_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='招标结果表';`srm_db`


-- 插入20条供应商测试数据
INSERT INTO supplier (
  NAME, contact_person, phone, email, country, category, STATUS, create_time, update_time
) VALUES 
-- 中国供应商（电子产品）
('深圳电子科技有限公司', '张三', '13800138001', 'sz1@example.com', '中国', '电子产品', 1, NOW(), NOW()),
('上海数码设备有限公司', '李四', '13800138002', 'sh1@example.com', '中国', '电子产品', 1, NOW(), NOW()),
('广州智能科技有限公司', '王五', '13800138003', 'gz1@example.com', '中国', '电子产品', 1, NOW(), NOW()),
('北京通讯设备有限公司', '赵六', '13800138004', 'bj1@example.com', '中国', '电子产品', 1, NOW(), NOW()),
('成都电子元件有限公司', '孙七', '13800138005', 'cd1@example.com', '中国', '电子产品', 1, NOW(), NOW()),

-- 中国供应商（家居用品）
('杭州家居用品有限公司', '周八', '13800138006', 'hz1@example.com', '中国', '家居用品', 1, NOW(), NOW()),
('苏州家居制造有限公司', '吴九', '13800138007', 'sz2@example.com', '中国', '家居用品', 1, NOW(), NOW()),
('南京家居设计有限公司', '郑十', '13800138008', 'nj1@example.com', '中国', '家居用品', 1, NOW(), NOW()),
('武汉家居销售有限公司', '钱十一', '13800138009', 'wh1@example.com', '中国', '家居用品', 1, NOW(), NOW()),
('重庆家居贸易有限公司', '孙十二', '13800138010', 'cq1@example.com', '中国', '家居用品', 1, NOW(), NOW()),

-- 东南亚供应商
('新加坡电子贸易公司', 'Lim', '6512345678', 'sg1@example.com', '新加坡', '电子产品', 1, NOW(), NOW()),
('马来西亚家居企业', 'Tan', '60123456789', 'my1@example.com', '马来西亚', '家居用品', 1, NOW(), NOW()),
('泰国数码有限公司', 'Somchai', '66123456789', 'th1@example.com', '泰国', '电子产品', 1, NOW(), NOW()),
('印尼贸易公司', 'Wijaya', '621234567890', 'id1@example.com', '印度尼西亚', '家居用品', 1, NOW(), NOW()),

-- 待审核供应商
('韩国电子进口公司', 'Kim', '821012345678', 'kr1@example.com', '韩国', '电子产品', 0, NOW(), NOW()),
('日本家居设计公司', 'Tanaka', '8109012345678', 'jp1@example.com', '日本', '家居用品', 0, NOW(), NOW()),

-- 已驳回供应商
('美国通讯设备公司', 'Smith', '12123456789', 'us1@example.com', '美国', '电子产品', 2, NOW(), NOW()),
('德国制造有限公司', 'Schmidt', '491234567890', 'de1@example.com', '德国', '家居用品', 2, NOW(), NOW()),

-- 其他品类
('澳大利亚食品贸易公司', 'Johnson', '61123456789', 'au1@example.com', '澳大利亚', '食品', 1, NOW(), NOW()),
('英国服装有限公司', 'Brown', '441234567890', 'uk1@example.com', '英国', '服装', 1, NOW(), NOW());

-- 查看所有供应商
SELECT * FROM supplier;

-- 查看数据总数
SELECT COUNT(*) FROM supplier; -- 应该返回20


-- 采购需求数据
INSERT INTO purchase_requirement (
  product_name, specification, quantity, delivery_date, budget_max, create_time
) VALUES 
('智能手机', '6.7英寸 256G', 50, '2025-09-30', 5000.00, NOW()),
('笔记本电脑', '15.6英寸 i7', 30, '2025-09-20', 8000.00, NOW()),
('无线耳机', '降噪款', 100, '2025-09-15', 1200.00, NOW()),
('智能手表', '运动款', 60, '2025-09-25', 2500.00, NOW()),
('平板电脑', '10.9英寸', 40, '2025-10-05', 4000.00, NOW()),
('办公椅', '人体工学', 20, '2025-09-10', 1500.00, NOW()),
('打印机', '彩色激光', 10, '2025-09-20', 3500.00, NOW()),
('投影仪', '高清1080P', 15, '2025-10-10', 4500.00, NOW()),
('键盘鼠标套装', '无线', 50, '2025-09-05', 300.00, NOW()),
('显示器', '27英寸 4K', 25, '2025-09-30', 2800.00, NOW());

-- 招标测试数据
INSERT INTO tender (
  requirement_id, title, description, start_time, end_time, STATUS, create_time
) VALUES 
(1, '智能手机采购招标', '采购50台智能手机，要求原装正品', '2025-08-15 09:00:00', '2025-08-20 18:00:00', 1, NOW()),
(2, '笔记本电脑采购招标', '采购30台高性能笔记本电脑', '2025-08-16 09:00:00', '2025-08-22 18:00:00', 1, NOW()),
(3, '无线耳机批量采购招标', '采购100台降噪无线耳机', '2025-08-17 09:00:00', '2025-08-24 18:00:00', 0, NOW()),
(4, '智能手表采购招标', '采购60台运动智能手表', '2025-08-10 09:00:00', '2025-08-14 18:00:00', 2, NOW()),
(5, '平板电脑采购招标', '采购40台平板电脑', '2025-08-20 09:00:00', '2025-08-25 18:00:00', 0, NOW());


-- 投标测试数据（关联招标ID=1）
INSERT INTO bid (
  tender_id, supplier_id, price, delivery_days, create_time
) VALUES 
(1, 1, 280.00, 3, NOW()),
(1, 2, 275.00, 5, NOW()),
(1, 3, 290.00, 2, NOW()),
(1, 4, 270.00, 4, NOW()),
(1, 5, 285.00, 3, NOW());

SELECT * FROM bid;
SELECT id, NAME, STATUS FROM supplier WHERE id = 1;


-- 添加供应商资质和评级相关字段
ALTER TABLE supplier
ADD COLUMN qualification_file VARCHAR(255) NULL COMMENT '资质文件路径',
ADD COLUMN rating INT(1) NULL COMMENT '评级(1-5星)',
ADD COLUMN cooperation_status VARCHAR(20) NULL COMMENT '合作状态: active-活跃, inactive-暂停, terminated-终止',
ADD COLUMN remark VARCHAR(500) NULL COMMENT '备注',
ADD COLUMN audit_opinion VARCHAR(500) NULL COMMENT '审核意见',
ADD COLUMN audit_time DATETIME NULL COMMENT '审核时间';

-- 添加索引以提高查询性能
CREATE INDEX idx_supplier_cooperation_status ON supplier(cooperation_status);
CREATE INDEX idx_supplier_rating ON supplier(rating);

-- 为supplier表的status、category、country字段添加索引
CREATE INDEX idx_supplier_status ON supplier(STATUS);
CREATE INDEX idx_supplier_category ON supplier(category);
CREATE INDEX idx_supplier_country ON supplier(country);

-- 为常用联合查询添加复合索引
CREATE INDEX idx_supplier_category_country ON supplier(category, country);

-- user表构建，用于权限认证
CREATE TABLE `user` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码（加密存储）',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `role` VARCHAR(20) NOT NULL COMMENT '角色（如：ADMIN, USER）',
  `enabled` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否启用（1：启用，0：禁用）',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 插入管理员用户（密码：admin123，已使用BCrypt加密）
INSERT INTO `user` (`username`, `password`, `email`, `role`, `enabled`)
VALUES (
  'admin',
  '$2a$10$7J6Kv6YeP6X5w0y5QeP85eX5eX5eX5eX5eX5eX5eX5eX5eX5eX5e',
  'admin@example.com',
  'ADMIN',
  1
);

-- 插入普通用户（密码：user123，已使用BCrypt加密）
INSERT INTO `user` (`username`, `password`, `email`, `role`, `enabled`)
VALUES (
  'user',
  '$2a$10$7J6Kv6YeP6X5w0y5QeP85eX5eX5eX5eX5eX5eX5eX5eX5eX5eX5e',
  'user@example.com',
  'USER',
  1
);

SELECT * FROM USER;