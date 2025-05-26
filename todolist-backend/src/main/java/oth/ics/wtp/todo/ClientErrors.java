package oth.ics.wtp.todo;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ClientErrors {
    private static final Logger logger = LoggerFactory.getLogger(ClientErrors.class);



    public static ResponseStatusException todoListNotFound(long todolistID) {
        return log(new ResponseStatusException(HttpStatus.NOT_FOUND,"todolist with id " + todolistID));
    }

    public static ResponseStatusException todoNotFound(long todoID) {
        return  log(new ResponseStatusException(HttpStatus.NOT_FOUND,"todo with id " + todoID));
    }
    private static ResponseStatusException log(ResponseStatusException e) {
        logger.error(ExceptionUtils.getMessage(e) + ExceptionUtils.getStackTrace(e));
        return e;
    }
}
