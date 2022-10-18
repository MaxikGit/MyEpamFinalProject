package com.max.restaurant.model.services;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.model.dao.daoimpl.CategoryDAO;
import com.max.restaurant.model.entity.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.max.restaurant.utils.UtilsLoggerMsgs.METHOD_STARTS_MSG;

/**
 * This is a service class, that presents methods to operate over {@link Category} objects on requests from controller
 * to DAO layer.
 */
public class CategoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryService.class);
    private CategoryDAO categoryDAO = new CategoryDAO();

    public List<Category> findAllCategories() throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "findAllCategories", "true");
        return categoryDAO.findAll();
    }

//    private boolean categoryIsValid(Category category) {
//        return (category.getName() != null && (category.getId() >= 0 ));
//    }

//    public Category findCategoryById(int id) throws DAOException {
//        LOGGER.info(METHOD_STARTS_MSG, "findCategoryById", "true");
//        if (id < 1)
//            throw new DAOServiceException(ID_EXC);
//        categoryDAO = new CategoryDAO();
//        return categoryDAO.findObjById(id);
//    }
//    public void deleteCategory(Category category) throws DAOException {
//        LOGGER.info(METHOD_STARTS_MSG, "deleteCategory", "true");
//        categoryDAO = new CategoryDAO();
//        if (!categoryIsValid(category) && !categoryDAO.deleteObj(category, categoryDAO.getConnection()))
//            throw new DAOServiceException(USER_EXC);
//    }
//
//    public void updateCategory(Category category) throws DAOException {
//        LOGGER.info(METHOD_STARTS_MSG, "updateCategory", "true");
//        categoryDAO = new CategoryDAO();
//        if (!categoryIsValid(category) && !categoryDAO.updateObj(category, categoryDAO.getConnection()))
//            throw new DAOServiceException(USER_EXC);
//    }
//    public void insertCategory(Category category) throws DAOException {
//        LOGGER.info(METHOD_STARTS_MSG, "insertCategory", "true");
//        categoryDAO = new CategoryDAO();
//        if (!categoryIsValid(category) && !categoryDAO.insertObj(category, categoryDAO.getConnection()))
//            throw new DAOServiceException(USER_EXC);
//    }
}