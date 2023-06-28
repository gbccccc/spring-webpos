package com.example.batch.service;

import com.example.batch.mapper.ProductMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.example.batch.model.Product;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProductWriter implements ItemWriter<Product>, StepExecutionListener {
    SqlSessionFactory sqlSessionFactory = null;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

    @Override
    public void write(List<? extends Product> list) {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        List<? extends Product> newList= new ArrayList<>(list);
        newList.sort(Comparator.comparing(Product::getAsin));
        try {
            ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
            for (Product product : newList) {
                mapper.insertProductBasic(product);
                if (!product.getCategory().isEmpty()) {
                    mapper.insertCategories(product.getAsin(), product.getCategory());
                }
                if (!product.getImageURLHighRes().isEmpty()) {
                    mapper.insertImagesURLHighres(product.getAsin(), product.getImageURLHighRes());
                }
            }
        } finally {
            sqlSession.commit();
            sqlSession.close();
        }
    }
}
