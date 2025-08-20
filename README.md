# 跨境电商SRM系统 Cross-border E-commerce SRM System

## 项目简介 Project Introduction

本项目是一个专为跨境电商场景设计的供应商关系管理(SRM)系统，聚焦于简化用户认证环节，专注实现**供应商管理**和**寻源招标**两大核心模块。系统采用现代化技术栈开发，旨在帮助采购方高效管理供应商资源，规范招标流程，提升跨境采购效率。

This project is a Supplier Relationship Management (SRM) system designed specifically for cross-border e-commerce scenarios. It simplifies user authentication and focuses on implementing two core modules: **Supplier Management** and **Sourcing & Tendering**. Developed with modern technology stack, the system aims to help buyers efficiently manage supplier resources, standardize tendering processes, and improve cross-border procurement efficiency.

## 技术栈 Technology Stack

- **后端**：Spring Boot + MyBatis + Redis
- **前端**：Vue.js (vite) + Element Plus
- **数据库**：MySQL
- **API文档**：Swagger
- **服务器**：Nginx
- **构建工具**：Maven

- **Backend**: Spring Boot + MyBatis + Redis
- **Frontend**: Vue.js (vite) + Element Plus
- **Database**: MySQL
- **API Documentation**: Swagger
- **Server**: Nginx
- **Build Tool**: Maven

## 核心功能模块 Core Function Modules

### 1. 供应商管理模块 Supplier Management Module

- **供应商信息管理**：录入、编辑、查询供应商基本信息（名称、联系人、电话、邮箱、所在国家/城市、主营品类）
- **供应商审核**：查看待审核供应商列表，执行审核通过/驳回操作
- **供应商筛选**：按名称、国家、主营品类等条件搜索供应商，支持分页展示
- **状态管理**：设置供应商状态（待审核/已合作/暂停合作）

- **Supplier Information Management**: Enter, edit, and query basic supplier information (name, contact person, phone, email, country/city, main category)
- **Supplier Audit**: View pending supplier lists and perform approval/rejection operations
- **Supplier Filtering**: Search suppliers by name, country, main category, etc., with pagination support
- **Status Management**: Set supplier status (pending review/cooperating/suspended)

### 2. 寻源与招标管理模块 Sourcing and Tendering Management Module

- **采购需求管理**：创建采购需求（产品名称、规格、数量、期望交货期、预算范围）
- **招标管理**：发布招标信息，选择参与招标的供应商，查看招标进度
- **投标管理**：供应商提交投标报价及交货承诺，招标截止前允许修改报价
- **招标结果处理**：展示所有有效投标，按价格排序，选择中标供应商

- **Procurement Requirement Management**: Create procurement requirements (product name, specification, quantity, expected delivery date, budget range)
- **Tendering Management**: Publish tender information, select participating suppliers, and track tender progress
- **Bid Management**: Suppliers submit bids and delivery commitments, with the ability to modify bids before the deadline
- **Tender Result Processing**: Display all valid bids, sort by price, and select winning suppliers

## 项目结构 Project Structure

```
srm_model/
├── .gitignore
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/srm/srm_model/
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── mapper/
│   │   │   ├── model/
│   │   │   ├── security/
│   │   │   └── util/
│   │   └── resources/
│   └── test/
srm_fase/
├── .gitignore
├── package.json
├── src/
└── vite.config.js
```

## 如何运行 How to Run

### 后端运行 Backend Run

1. 确保已安装JDK 11+和Maven
2. 克隆代码仓库
3. 配置数据库连接信息（位于`src/main/resources/application.properties`）
4. 运行以下命令：

```bash
cd srm_model
mvn spring-boot:run
```

1. Ensure JDK 11+ and Maven are installed
2. Clone the code repository
3. Configure database connection information (in `src/main/resources/application.properties`)
4. Run the following commands:

```bash
cd srm_model
mvn spring-boot:run
```

### 前端运行 Frontend Run

1. 确保已安装Node.js 14+
2. 进入前端目录：

```bash
cd srm_fase
npm install
npm run dev
```

1. Ensure Node.js 14+ is installed
2. Enter the frontend directory:

```bash
cd srm_fase
npm install
npm run dev
```

## API文档 API Documentation

启动后端服务后，可访问以下地址查看API文档：
- Swagger UI: http://localhost:8080/swagger-ui.html
- 增强版文档: http://localhost:8080/doc.html

After starting the backend service, you can access the following addresses to view API documentation:
- Swagger UI: http://localhost:8080/swagger-ui.html
- Enhanced Documentation: http://localhost:8080/doc.html

## 数据库设计 Database Design

系统主要包含以下核心表：
- `supplier`：供应商信息表
- `supplier_audit`：供应商审核记录表
- `purchase_requirement`：采购需求表
- `tender`：招标表
- `bid`：投标记录表

The system mainly includes the following core tables:
- `supplier`: Supplier information table
- `supplier_audit`: Supplier audit record table
- `purchase_requirement`: Procurement requirement table
- `tender`: Tender table
- `bid`: Bid record table

## 贡献指南 Contribution Guidelines

1.  Fork 本仓库
2.  创建特性分支 (`git checkout -b feature/fooBar`)
3.  提交更改 (`git commit -am 'Add some fooBar'`)
4.  推送到分支 (`git push origin feature/fooBar`)
5.  创建新的Pull Request

1. Fork this repository
2. Create your feature branch (`git checkout -b feature/fooBar`)
3. Commit your changes (`git commit -am 'Add some fooBar'`)
4. Push to the branch (`git push origin feature/fooBar`)
5. Create a new Pull Request

## 版权信息 Copyright Information

© 2025 跨境电商SRM系统开发团队. 保留所有权利.

© 2025 XHAYANE. 保留所有权利.

© 2025 Cross-border E-commerce SRM System Development Team. All rights reserved.

© 2025 XHAYANE. All rights reserved.
