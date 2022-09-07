package com.max.restaurant.model.dao.services;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.exceptions.DAOServiceException;
import com.max.restaurant.model.dao.daoimpl.CategoryDAO;
import com.max.restaurant.model.entity.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.max.restaurant.exceptions.UtilsExceptionMsgs.*;
import static com.max.restaurant.utils.UtilsLoggerMsgs.METHOD;

public class CategoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryService.class);
    private CategoryDAO categoryDAO;

    public Category findCategoryById(int id) throws DAOException {
        LOGGER.info(METHOD, "findCategoryById", "true");
        if (id < 1)
            throw new DAOServiceException(ID_EXC);
        categoryDAO = new CategoryDAO();
        return categoryDAO.findObjById(id);
    }

    public List<Category> findAllCategories() throws DAOException {
        LOGGER.info(METHOD, "findAllCategories", "true");
        categoryDAO = new CategoryDAO();
        return categoryDAO.findAll();
    }

    public void deleteCategory(Category category) throws DAOException {
        LOGGER.info(METHOD, "deleteCategory", "true");
        categoryDAO = new CategoryDAO();
        if (!categoryIsValid(category) && !categoryDAO.deleteObj(category))
            throw new DAOServiceException(USER_EXC);
    }

    public void updateCategory(Category category) throws DAOException {
        LOGGER.info(METHOD, "updateCategory", "true");
        categoryDAO = new CategoryDAO();
        if (!categoryIsValid(category) && !categoryDAO.updateObj(category, categoryDAO.getConnection()))
            throw new DAOServiceException(USER_EXC);
    }
    public void insertCategory(Category category) throws DAOException {
        LOGGER.info(METHOD, "insertCategory", "true");
        categoryDAO = new CategoryDAO();
        if (!categoryIsValid(category) && !categoryDAO.insertObj(category, categoryDAO.getConnection()))
            throw new DAOServiceException(USER_EXC);
    }

    private boolean categoryIsValid(Category category) {
        return (category.getName() != null && (category.getId() >= 0 ));
    }
}
