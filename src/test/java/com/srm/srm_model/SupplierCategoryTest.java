package com.srm.srm_model;

import com.srm.srm_model.service.SupplierService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

@SpringBootTest
public class SupplierCategoryTest {

    @Autowired
    private SupplierService supplierService;

    @Test
    public void testGetAllCategories() {
        try {
            // 调用服务方法
            List<String> categories = supplierService.getAllCategories();

            // 打印结果信息
            System.out.println("返回结果类型: " + (categories != null ? categories.getClass().getName() : "null"));
            System.out.println("返回结果大小: " + (categories != null ? categories.size() : 0));
            System.out.println("返回结果内容: " + categories);

            // 如果有数据，打印第一条数据的类型
            if (categories != null && !categories.isEmpty()) {
                Object firstItem = categories.get(0);
                System.out.println("第一条数据类型: " + firstItem.getClass().getName());
                System.out.println("第一条数据内容: " + firstItem);
            }
        } catch (Exception e) {
            System.out.println("测试过程中发生异常: ");
            e.printStackTrace();
        }
    }
}