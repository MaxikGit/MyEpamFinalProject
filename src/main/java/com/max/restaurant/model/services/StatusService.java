package com.max.restaurant.model.services;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.exceptions.DAOServiceException;
import com.max.restaurant.model.dao.daoimpl.StatusDAO;
import com.max.restaurant.model.entity.Category;
import com.max.restaurant.model.entity.Custom;
import com.max.restaurant.model.entity.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.max.restaurant.utils.UtilsExceptionMsgs.ID_EXC;
import static com.max.restaurant.utils.UtilsLoggerMsgs.METHOD_STARTS_MSG;

/**
 * This is a service class, that presents methods to operate over {@link Status} objects on requests
 * from controller to DAO layer.
 */
public class StatusService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatusService.class);
    private StatusDAO statusDAO;

    public Status findStatusById(int id) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "findStatusById", "true");
        if (id < 1)
            throw new DAOServiceException(ID_EXC);
        statusDAO = new StatusDAO();
        return statusDAO.findObjById(id);
    }

    public List<Status> findAll() throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "findAll", "true");
        statusDAO = new StatusDAO();
        return statusDAO.findAll();
    }

    private boolean categoryIsValid(Category category) {
        return (category.getName() != null && (category.getId() >= 0 ));
    }
}
