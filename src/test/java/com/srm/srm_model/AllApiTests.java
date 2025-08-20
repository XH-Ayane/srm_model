package com.srm.srm_model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srm.srm_model.entity.*;
import com.srm.srm_model.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.security.test.context.support.WithMockUser;
import static org.hamcrest.Matchers.containsString;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "testuser", roles = { "USER" })
public class AllApiTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private PurchaseRequirementService requirementService;

    @MockBean
    private SupplierService supplierService;

    @MockBean
    private TenderService tenderService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private PurchaseRequirement requirement;
    private Supplier supplier;
    private Tender tender;
    private User user;

    @BeforeEach
    void setUp() throws Exception {
        // 初始化MockMvc并添加Spring Security支持
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        // 初始化采购需求
        requirement = new PurchaseRequirement();
        requirement.setId(1L);
        requirement.setProductName("原材料采购");
        requirement.setQuantity(100);

        // 初始化供应商
        supplier = new Supplier();
        supplier.setId(1L);
        supplier.setName("XX公司");
        supplier.setCategory("电子元件");
        supplier.setCountry("中国");

        // 初始化招标
        tender = new Tender();
        tender.setId(1L);
        tender.setTitle("设备采购招标");
        tender.setRequirementId(1L);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        tender.setEndTime(sdf.parse("2024-12-31"));
        tender.setStatus(1); // 1表示进行中

        // 初始化用户
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("test@example.com");
        user.setRole("USER");
        user.setEnabled(true);
    }

    // 采购需求接口测试
    @Test
    void testAddRequirement() throws Exception {
        Mockito.when(requirementService.addRequirement(Mockito.any(PurchaseRequirement.class))).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.post("/requirement/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requirement)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("采购需求创建成功"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    void testGetRequirementById() throws Exception {
        Mockito.when(requirementService.getRequirementById(1L)).thenReturn(requirement);

        mockMvc.perform(MockMvcRequestBuilders.get("/requirement/get/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.productName").value("原材料采购"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.quantity").value(100));
    }

    // 供应商接口测试
    @Test
    void testAddSupplier() throws Exception {
        Mockito.when(supplierService.addSupplier(Mockito.any(Supplier.class))).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.post("/supplier/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(supplier)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("供应商添加成功"));
    }

    @Test
    void testGetAllCategories() throws Exception {
        List<String> categories = new ArrayList<>();
        categories.add("电子元件");
        categories.add("机械设备");

        Mockito.when(supplierService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(MockMvcRequestBuilders.get("/supplier/api/supplierCategory")
                .param("method", "getAllCategories"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value("电子元件"));
    }

    @Test
    void testGetAllCountries() throws Exception {
        List<String> countries = new ArrayList<>();
        countries.add("中国");
        countries.add("美国");

        Mockito.when(supplierService.getAllCountries()).thenReturn(countries);

        mockMvc.perform(MockMvcRequestBuilders.get("/supplier/api/getAllCountries")
                .param("method", "getAllCountries"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0]").value("中国"));
    }

    // 招标接口测试
    @Test
    void testAddTender() throws Exception {
        Mockito.when(tenderService.addTender(Mockito.any(Tender.class))).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.post("/tender/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tender)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("招标创建成功"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    void testGetTenderById() throws Exception {
        Mockito.when(tenderService.getTenderById(1L)).thenReturn(tender);

        mockMvc.perform(MockMvcRequestBuilders.get("/tender/get/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("设备采购招标"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.status").value(1));
    }

    @Test
    void testGetTendersByStatus() throws Exception {
        List<Tender> tenders = new ArrayList<>();
        tenders.add(tender);

        Mockito.when(tenderService.getTendersByStatus(1)).thenReturn(tenders);

        mockMvc.perform(MockMvcRequestBuilders.get("/tender/listByStatus")
                .param("status", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].title").value("设备采购招标"));
    }

    // 注释掉不存在的用户接口测试
    /*
     * @Test
     * void testSaveUser() throws Exception {
     * Mockito.when(userService.saveUser(Mockito.any(User.class))).thenReturn(user);
     * 
     * mockMvc.perform(MockMvcRequestBuilders.post("/user/add")
     * .contentType(MediaType.APPLICATION_JSON)
     * .content(objectMapper.writeValueAsString(user)))
     * .andExpect(MockMvcResultMatchers.status().isOk())
     * .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
     * .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("用户添加成功"))
     * .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1));
     * }
     * 
     * @Test
     * void testGetUserById() throws Exception {
     * Mockito.when(userService.getUserById(1L)).thenReturn(user);
     * 
     * mockMvc.perform(MockMvcRequestBuilders.get("/user/get/{id}", 1))
     * .andExpect(MockMvcResultMatchers.status().isOk())
     * .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
     * .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value("testuser"
     * ))
     * .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(
     * "test@example.com"));
     * }
     * 
     * @Test
     * void testUpdateUser() throws Exception {
     * user.setUsername("updateduser");
     * Mockito.when(userService.updateUser(Mockito.any(User.class))).thenReturn(user
     * );
     * Mockito.when(userService.getUserById(1L)).thenReturn(user);
     * 
     * mockMvc.perform(MockMvcRequestBuilders.put("/user/update")
     * .contentType(MediaType.APPLICATION_JSON)
     * .content(objectMapper.writeValueAsString(user)))
     * .andExpect(MockMvcResultMatchers.status().isOk())
     * .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
     * .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("用户更新成功"));
     * 
     * // 验证更新是否生效
     * mockMvc.perform(MockMvcRequestBuilders.get("/user/get/{id}", 1))
     * .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value(
     * "updateduser"));
     * }
     * 
     * @Test
     * void testDeleteUser() throws Exception {
     * Mockito.when(userService.deleteUser(1L)).thenReturn(true);
     * 
     * mockMvc.perform(MockMvcRequestBuilders.delete("/user/delete/{id}", 1))
     * .andExpect(MockMvcResultMatchers.status().isOk())
     * .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
     * .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("用户删除成功"));
     * }
     */

    // 测试BasicController中的接口 - 调整以匹配实际实现
    @Test
    void testBasicControllerUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("theonefx"));
    }

    @Test
    void testBasicControllerSaveUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/save_user?username=testuser"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(containsString("user will save: username=testuser")));
    }

    @Test
    void testBasicControllerHello() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hello?name=testuser"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Hello testuser"));
    }
}